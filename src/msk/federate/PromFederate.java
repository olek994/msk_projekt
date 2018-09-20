package msk.federate;

import hla.rti.*;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;
import msk.BaseFederate;
import msk.ambassador.PromAmbassador;

import java.util.Random;

public class PromFederate extends BaseFederate<PromAmbassador> {

    private int promObj = 0;
    private Random random = new Random();
    private boolean init = false;
    private float speed = 1f;
    private int waittingForNextStation = 0;
    private int numerStacji = 1;
    @Override
    protected void update(double timeToAdvance) throws SaveInProgress, AttributeNotDefined, InvalidFederationTime, NameNotFound, RestoreInProgress, ObjectNotKnown, ObjectClassNotDefined, ConcurrentAccessAttempted, AttributeNotOwned, FederateNotExecutionMember, RTIinternalError {
        if(waittingForNextStation == 100) {
            updatePromObj_NumerStacji(numerStacji,timeToAdvance);
            numerStacji++;
            waittingForNextStation = 0;
        }

        if(numerStacji > 6){
            numerStacji = 1;
        }
        waittingForNextStation++;


        //TODO DODAWANIE PASAZEROW
    }

    @Override
    protected void init() throws Exception {
        this.promObj = createObject("Prom");
    }

    @Override
    protected void publishAndSubscribe() throws RTIexception {
        //Publikacja promu
        int promClass                           =rtiamb.getObjectClassHandle("ObjectRoot.Prom");
        int promClassAttrLiczbaWolnychMiejsc    =rtiamb.getAttributeHandle("liczbaWolnychMiejsc",promClass);
        int promClassAttrLiczbaZajetychMiejsc    =rtiamb.getAttributeHandle("liczbaZajetychMiejsc",promClass);
        int promClassAttrNumerStacji            =rtiamb.getAttributeHandle("numerStacji",promClass);

        AttributeHandleSet attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add(promClassAttrLiczbaWolnychMiejsc);
        attributes.add(promClassAttrNumerStacji);
        attributes.add(promClassAttrLiczbaZajetychMiejsc);

        rtiamb.publishObjectClass(promClass,attributes);


    }

    @Override
    protected void deleteObjectsAndInteractions() throws Exception {
        this.deleteObject(this.promObj);
    }

    private void updatePromObj_NumerStacji(int value, double time) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, ObjectClassNotDefined, ObjectNotKnown, RestoreInProgress, AttributeNotOwned, AttributeNotDefined, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted {
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int classHandle = rtiamb.getObjectClass(this.promObj);
        int attrHandle = rtiamb.getAttributeHandle( "numerStacji", classHandle );
        attributes.add(attrHandle, EncodingHelpers.encodeInt(value));
        rtiamb.updateAttributeValues(this.promObj, attributes, generateTag(), convertTime(time));
    }

    private void updatePromObj_LiczbaZajetychMiejsc(int value,double time) throws SaveInProgress, AttributeNotDefined, InvalidFederationTime, RestoreInProgress, ObjectNotKnown, ConcurrentAccessAttempted, AttributeNotOwned, FederateNotExecutionMember, RTIinternalError, NameNotFound, ObjectClassNotDefined {
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int classHandle = rtiamb.getObjectClass(this.promObj);
        int attrHandle = rtiamb.getAttributeHandle( "liczbaZajetychMiejsc", classHandle );
        attributes.add(attrHandle, EncodingHelpers.encodeInt(value));
        rtiamb.updateAttributeValues(this.promObj, attributes, generateTag(), convertTime(time));
    }


    public static void main(String[] agrs){
        try{
            new PromFederate().runFederate("PROM");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
