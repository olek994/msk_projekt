package msk.federate;

import hla.rti.AttributeHandleSet;
import hla.rti.AttributeNotDefined;
import hla.rti.AttributeNotOwned;
import hla.rti.ConcurrentAccessAttempted;
import hla.rti.FederateNotExecutionMember;
import hla.rti.InvalidFederationTime;
import hla.rti.NameNotFound;
import hla.rti.ObjectClassNotDefined;
import hla.rti.ObjectNotKnown;
import hla.rti.RTIexception;
import hla.rti.RTIinternalError;
import hla.rti.RestoreInProgress;
import hla.rti.SaveInProgress;
import hla.rti.SuppliedAttributes;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;
import msk.BaseFederate;
import msk.ambassador.PasazerAmbassador;

import java.util.ArrayList;
import java.util.List;

public class PasazerFederate extends BaseFederate<PasazerAmbassador> {

    private List<Integer> pasazerList;
    private boolean init = false;

    @Override
    protected void update(double timeToAdvance) throws Exception {

        if(!init){
            init = true;
            update_PasazerAttr_NumerStacji(1,2,timeToAdvance,0);
        }

        //TODO DODAC WIECEJ PASAZEROW

    }

    @Override
    protected void init() throws Exception {
        pasazerList = new ArrayList<>();
        pasazerList.add(createObject("Pasazer"));
    }

    @Override
    protected void publishAndSubscribe() throws RTIexception {

        int pasazerClass                = rtiamb.getObjectClassHandle("ObjectRoot.Pasazer");
        int pasazerAttr_ID              = rtiamb.getAttributeHandle("id",pasazerClass);
        int pasazerAttr_typ             = rtiamb.getAttributeHandle("typ",pasazerClass);
        int pasazerAttr_numerStacji     = rtiamb.getAttributeHandle("numerStacji",pasazerClass);
        int pasazerAttr_stacjaDocelowa  = rtiamb.getAttributeHandle("stacjaDocelowa",pasazerClass);

        AttributeHandleSet attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add(pasazerAttr_ID);
        attributes.add(pasazerAttr_numerStacji);
        attributes.add(pasazerAttr_typ);
        attributes.add(pasazerAttr_stacjaDocelowa);

        rtiamb.publishObjectClass(pasazerClass,attributes);

    }


    private void update_PasazerAttr_NumerStacji(int idPasazer,int numerStacji,double time,int idx) throws ObjectNotKnown, FederateNotExecutionMember, RTIinternalError, NameNotFound, ObjectClassNotDefined, RestoreInProgress, AttributeNotOwned, AttributeNotDefined, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted {
        if(idx > pasazerList.size()-1){
            return;
        }
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int obj = this.pasazerList.get(idx);
        int classHandle = rtiamb.getObjectClass(obj);
        int attrIdHandle = rtiamb.getAttributeHandle("id",classHandle);
        int attrNumerStacjiHandle = rtiamb.getAttributeHandle("numerStacji",classHandle);
        attributes.add(attrIdHandle , EncodingHelpers.encodeInt(idPasazer));
        attributes.add(attrNumerStacjiHandle , EncodingHelpers.encodeInt(numerStacji));
        rtiamb.updateAttributeValues(obj,attributes,generateTag(),convertTime(time));
    }

    @Override
    protected void deleteObjectsAndInteractions() throws Exception {
        for(Integer pasazer: pasazerList){
            this.deleteObject(pasazer);
        }
    }
    
    
    public static void main(String[] agrs){
        try{
            new PasazerFederate().runFederate("Pasazer");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
