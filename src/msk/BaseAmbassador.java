package msk;

import hla.rti.EventRetractionHandle;
import hla.rti.LogicalTime;
import hla.rti.ReceivedInteraction;
import hla.rti.ReflectedAttributes;
import hla.rti.jlc.NullFederateAmbassador;
import org.portico.impl.hla13.types.DoubleTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Aleksander Małkowicz, Date: 13.09.2018
 * Copyright by RelayOnIT, Warszawa 2018
 */
public abstract class BaseAmbassador extends NullFederateAmbassador {

    public double federateTime        = 0.0;
    public double federateLookahead   = 10.0;
    public double grantedTime         = 0.0;

    public boolean isRegulating       = false;
    public boolean isConstrained      = false;
    public boolean isAdvancing        = false;

    public boolean isAnnounced        = false;
    public boolean isReadyToRun       = false;

    public boolean running            = true;

    public int startSimInteractionHandle = 0;
    public int endSimInteractionHandle   = 0;

    public Map<Integer, Integer> objects = new HashMap<>();
    public Map<Class<? extends BaseObject>,BaseObject> objectsInstance = new HashMap<>();
    public Map<Integer,BaseObject> stacjeObjInstance = new HashMap<>();
    public Map<Integer,BaseObject> pasazerObjInstance = new HashMap<>();

    private double convertTime( LogicalTime logicalTime )
    {
        return ((DoubleTime)logicalTime).getTime();
    }

    private void log( String message )
    {
        System.out.println( this.getClass().getSimpleName() + message );
    }

    @Override
    public void timeRegulationEnabled( LogicalTime theFederateTime )
    {
        this.federateTime = convertTime( theFederateTime );
        this.isRegulating = true;
    }
    @Override
    public void timeConstrainedEnabled( LogicalTime theFederateTime )
    {
        this.federateTime = convertTime( theFederateTime );
        this.isConstrained = true;
    }
    @Override
    public void timeAdvanceGrant( LogicalTime theTime )
    {
        this.grantedTime = convertTime(theTime);
        this.federateTime = convertTime( theTime );
        this.isAdvancing = false;
    }

    public double getGrantedTime(){
        return grantedTime;
    }

    @Override
    public void discoverObjectInstance( int theObject,int theObjectClass,String objectName )
    {
        if(!this.objects.containsKey(theObject)){
            this.objects.put(theObject,theObjectClass);
        }else{
            this.objects.replace(theObject,theObjectClass);
        }
    }

    public <T extends BaseObject>  T  getObjectInstances(Class<T> clazz){
        return clazz.cast(this.objectsInstance.get(clazz));
    }

    public <T extends BaseObject> T getStacjeObjInstances(Integer numerStacji){
            return (T) this.stacjeObjInstance.get(numerStacji);
    }

    public <T extends BaseObject> T getPasazerObjInstances(Integer idPasazera){
            return (T) this.pasazerObjInstance.get(idPasazera);
    }



    public  void removeStacjeObjInstances(Integer numerStacji){
        if(this.stacjeObjInstance.containsKey(numerStacji)){
            this.stacjeObjInstance.remove(numerStacji);
        }
    }

    public  void removePasazerObjInstances(Integer idPasazera){
        if(this.stacjeObjInstance.containsKey(idPasazera)){
            this.stacjeObjInstance.remove(idPasazera);
        }
    }

    public <T extends BaseObject> void removeObjectInstance(Class<T> clazz){
        if(this.objectsInstance.containsKey(clazz)){
            this.objectsInstance.remove(clazz);
        }
    }
    @Override
    public void reflectAttributeValues( int theObject, ReflectedAttributes theAttributes, byte[] tag )
    {
        reflectAttributeValues( theObject, theAttributes, tag, null, null );
    }

    @Override
    public void reflectAttributeValues( int theObject, ReflectedAttributes theAttributes,
                                        byte[] tag, LogicalTime theTime, EventRetractionHandle retractionHandle )
    {

    }

    @Override
    public void receiveInteraction( int interactionClass,
                                    ReceivedInteraction theInteraction,
                                    byte[] tag )
    {
        receiveInteraction( interactionClass, theInteraction, tag, null, null );
    }
    @Override
    public void receiveInteraction( int interactionClass, ReceivedInteraction theInteraction, byte[] tag,LogicalTime theTime, EventRetractionHandle eventRetractionHandle )
    {
        if(interactionClass == this.startSimInteractionHandle){
            this.isReadyToRun = true;
        }else if (interactionClass == this.endSimInteractionHandle){
            this.running = false;
        }
    }

    public void setStartSimInteractionHandle(int startSimInteractionHandle){
        this.startSimInteractionHandle = startSimInteractionHandle;
    }

    public void setEndSimInteractionHandle(int endSimInteractionHandle){
        this.endSimInteractionHandle = endSimInteractionHandle;
    }



}
