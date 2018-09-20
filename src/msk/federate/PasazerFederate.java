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
import java.util.Random;

public class PasazerFederate extends BaseFederate<PasazerAmbassador> {

    private List<Integer> pasazerList;
    private int waitToaddNewPassanger = 0;
    private int indexOfNewPassanger = 1;
    private Random random;

    @Override
    protected void update(double timeToAdvance) throws Exception {
        int numerStacji;
        int stacjaDocelowa = 0;
        int typ;
        if(waitToaddNewPassanger == 20){
            numerStacji = random.nextInt(6)+1;
            while (stacjaDocelowa == numerStacji){
                stacjaDocelowa = random.nextInt(6)+1;
            }
            typ = random.nextInt(2)+1;
            pasazerList.add(createObject("Pasazer"));
            update_PasazerAttr(indexOfNewPassanger,numerStacji,typ,stacjaDocelowa,timeToAdvance,numerStacji-1);
            indexOfNewPassanger++;
            waitToaddNewPassanger = 0;
        }

        waitToaddNewPassanger++;

    }

    @Override
    protected void init() throws Exception {
        pasazerList = new ArrayList<>();
        random = new Random();
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


    private void update_PasazerAttr(int idPasazer,int numerStacji,int typ, int stacjaDocelowa, double time, int idx) throws ObjectClassNotDefined, RTIinternalError, NameNotFound, FederateNotExecutionMember, SaveInProgress, RestoreInProgress, AttributeNotOwned, ObjectNotKnown, AttributeNotDefined, InvalidFederationTime, ConcurrentAccessAttempted {
        if(idx > pasazerList.size()-1){
            return;
        }
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int obj = this.pasazerList.get(idx);
        int classHandle = rtiamb.getObjectClass(obj);
        int attrIdHandle = rtiamb.getAttributeHandle("id",classHandle);
        int attrNumerStacjiHandle = rtiamb.getAttributeHandle("numerStacji",classHandle);
        int attrTypHandle = rtiamb.getAttributeHandle("typ",classHandle);
        int attrStacjaDocelowaHandle = rtiamb.getAttributeHandle("stacjaDocelowa",classHandle);
        attributes.add(attrIdHandle , EncodingHelpers.encodeInt(idPasazer));
        attributes.add(attrNumerStacjiHandle , EncodingHelpers.encodeInt(numerStacji));
        attributes.add(attrTypHandle , EncodingHelpers.encodeInt(typ));
        attributes.add(attrStacjaDocelowaHandle , EncodingHelpers.encodeInt(stacjaDocelowa));
        rtiamb.updateAttributeValues(obj,attributes,generateTag(),convertTime(time));
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
