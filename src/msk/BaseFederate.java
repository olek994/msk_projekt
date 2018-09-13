package msk;

import hla.rti.*;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;
import hla13.Example13Federate;
import hla13.Example13FederateAmbassador;
import org.portico.impl.hla13.types.DoubleTime;
import org.portico.impl.hla13.types.DoubleTimeInterval;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.net.MalformedURLException;

/**
 * Created by Aleksander Małkowicz, Date: 13.09.2018
 * Copyright by RelayOnIT, Warszawa 2018
 */
public abstract class BaseFederate<T extends BaseAmbassador>{

    protected static final String FED_FILE_NAME= "fom.fed";
    protected static final String FEDERATION_NAME = "mskFederation";


    protected RTIambassador rtiamb;
    protected T federationAmbassador;
    protected double timeStep = 10.0;


    private void log( String message )
    {
        System.out.println( "LOG federata   : " + message );
    }

//    private void waitForUser()
//    {
//        log( " >>>>>>>>>> Rozpocznij symulacj� <<<<<<<<<<" );
//        BufferedReader reader = new BufferedReader( new InputStreamReader(System.in) );
//        try
//        {
//            reader.readLine();
//        }
//        catch( Exception e )
//        {
//            log( "B��d podczas oczekiwania na rozpocz�cie symulacji " + e.getMessage() );
//            e.printStackTrace();
//        }
//    }


    protected LogicalTime convertTime(double time )
    {
        // PORTICO SPECIFIC!!
        return new DoubleTime( time );
    }


    private LogicalTimeInterval convertInterval(double time )
    {
        // PORTICO SPECIFIC!!
        return new DoubleTimeInterval( time );
    }

    ///////////////////////////////////////////////////////////////////////////
    ////////////////////////// Main Simulation Method /////////////////////////
    ///////////////////////////////////////////////////////////////////////////

    public void runFederate( String federateName ) throws Exception
    {
        this.rtiamb = RtiFactoryFactory.getRtiFactory().createRtiAmbassador();

        this.tryCreateFederation();

        this.getFederationAmbassadorAndJoinToFederation();

        this.waitForStart();

        this.enableTimePolicy();

        this.subscribeForEndSim();
        this.publishAndSubscribe();

        this.init();

        while (federationAmbassador.running){
            this.loop();
            rtiamb.tick();
        }

        this.destoryFederation();
    }

    private void loop() throws Exception {
        double timeToAdvance = federationAmbassador.federateTime + timeStep;
        advanceTime(timeStep);

        if(federationAmbassador.getGrantedTime() == timeToAdvance){
            timeToAdvance += federationAmbassador.federateLookahead;
            update(timeToAdvance);
        }

        federationAmbassador.federateTime = timeToAdvance;
    }

    protected abstract void update(double timeToAdvance) throws SaveInProgress, AttributeNotDefined, InvalidFederationTime, NameNotFound, RestoreInProgress, ObjectNotKnown, ObjectClassNotDefined, ConcurrentAccessAttempted, AttributeNotOwned, FederateNotExecutionMember, RTIinternalError;

    protected void init() throws Exception {

    }

    protected void waitForStart() throws  Exception{
        int startSimulation = rtiamb.getInteractionClassHandle("InteractionRoot.PoczatekSymulacji");
        federationAmbassador.setStartSimInteractionHandle(startSimulation);
        rtiamb.subscribeInteractionClass(startSimulation);

        log("Waiting for simulation start interaction...");
        while(!federationAmbassador.isReadyToRun) {
            rtiamb.tick();
        }
        System.out.println("RUNNING");
    }


    protected void tryCreateFederation() throws  Exception{
        try {
            File fom = new File( FED_FILE_NAME );
            rtiamb.createFederationExecution( FEDERATION_NAME, fom.toURI().toURL() );
            log( "Created Federation" );
        } catch( FederationExecutionAlreadyExists exists ) {
            log( "Didn't create federation, it already existed" );
        } catch( MalformedURLException urle ) {
            log( "Exception processing fom: " + urle.getMessage() );
            urle.printStackTrace();
        }
    }

    protected void getFederationAmbassadorAndJoinToFederation() throws Exception{
        try {
            ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
            Class<T> type = (Class<T>) superClass.getActualTypeArguments()[0];
            federationAmbassador = type.newInstance();
            if (federationAmbassador != null) {
                rtiamb.joinFederationExecution(getName(),FEDERATION_NAME,federationAmbassador);
                log("Dodanie do federacji jako "+ getName());
            }else {
                throw new Exception("Ambasador nie znaleziony");
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void enableTimePolicy() throws RTIexception
    {
        LogicalTime currentTime = convertTime( federationAmbassador.federateTime );
        LogicalTimeInterval lookahead = convertInterval( federationAmbassador.federateLookahead );

        this.rtiamb.enableTimeRegulation( currentTime, lookahead );

        while( !federationAmbassador.isRegulating )
        {
            rtiamb.tick();
        }

        this.rtiamb.enableTimeConstrained();

        while(!federationAmbassador.isConstrained)
        {
            rtiamb.tick();
        }
    }


    protected void subscribeForEndSim() throws Exception{
        int endSim = rtiamb.getInteractionClassHandle("InteractionRoot.KoniecSymulacji");
        federationAmbassador.setEndSimInteractionHandle(endSim);
        rtiamb.subscribeInteractionClass(endSim);
    }

    protected abstract void publishAndSubscribe() throws RTIexception;


//    private int registerObject() throws RTIexception
//    {
//        int classHandle = rtiamb.getObjectClassHandle( "ObjectRoot.A" );
//        return rtiamb.registerObjectInstance( classHandle );
//    }


    /**
     * This method will request a time advance to the current time, plus the given
     * timestep. It will then wait until a notification of the time advance grant
     * has been received.
     */
    private void advanceTime( double timestep ) throws RTIexception
    {
        // request the advance
        federationAmbassador.isAdvancing = true;
        LogicalTime newTime = convertTime( federationAmbassador.federateTime + timestep );
        rtiamb.timeAdvanceRequest( newTime );

        // wait for the time advance to be granted. ticking will tell the
        // LRC to start delivering callbacks to the federate
        while( federationAmbassador.isAdvancing )
        {
            rtiamb.tick();
        }
    }

    protected void deleteObject( int handle ) throws RTIexception
    {
        rtiamb.deleteObjectInstance( handle, generateTag() );
    }

    protected int createObject(String name) throws Exception{
        int classHandle = rtiamb.getObjectClassHandle("ObjectRoot."+name);
        return rtiamb.registerObjectInstance(classHandle);
    }

    protected byte[] generateTag()
    {
        return ("tag "+System.currentTimeMillis()).getBytes();
    }

    protected void destoryFederation() throws Exception {
        deleteObjectsAndInteractions();
        rtiamb.resignFederationExecution(ResignAction.NO_ACTION);
        rtiamb.destroyFederationExecution(FEDERATION_NAME);
    }

    protected void deleteObjectsAndInteractions() throws Exception{

    }
    protected String getName() {
        return this.getClass().getSimpleName();
    }
}
