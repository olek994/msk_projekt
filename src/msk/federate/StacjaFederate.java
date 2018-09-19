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
import msk.BaseAmbassador;
import msk.BaseFederate;
import msk.Objects.Pasazer;
import msk.Objects.Prom;
import msk.ambassador.StacjaAmbassador;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.ArrayList;
import java.util.List;

public class StacjaFederate extends BaseFederate<StacjaAmbassador> {

    private static final int DLUGOSC_KOLEJKI = 15;
    private static final int LICZBA_STACJI = 6;
    private List<Integer> stacjeList;
    private boolean init = false;
    private int watting = 0;
    private int poprzedniaStacjaPromu = -1;
    @Override
    protected void init() throws Exception {
        this.stacjeList = new ArrayList<>();
        stacjeList.add(createObject("Stacja"));
    }

    @Override
    protected void update(double timeToAdvance) throws Exception {

        if(!init){
            System.out.println("DODANO STACJE PIERWSZA");
            init = true;
            update_StacjaAttr(1,DLUGOSC_KOLEJKI,2,0,0,timeToAdvance,stacjeList.size()-1);
        }

        if(watting == 10){
            System.out.println("DODANO STACJE Druga");
            stacjeList.add(createObject("Stacja"));
            update_StacjaAttr(2,DLUGOSC_KOLEJKI,3,0,0,timeToAdvance,stacjeList.size()-1);
        }

        if(watting == 20){
            System.out.println("DODANO STACJE Trzecia");
            stacjeList.add(createObject("Stacja"));
            update_StacjaAttr(3,DLUGOSC_KOLEJKI,4,0,0,timeToAdvance,stacjeList.size()-1);
        }

        if(watting == 30){
            System.out.println("DODANO STACJE Czwarta");
            stacjeList.add(createObject("Stacja"));
            update_StacjaAttr(4,DLUGOSC_KOLEJKI,5,0,0,timeToAdvance,stacjeList.size()-1);
        }
        if(watting == 40){
            System.out.println("DODANO STACJE Piątą");
            stacjeList.add(createObject("Stacja"));
            update_StacjaAttr(5,DLUGOSC_KOLEJKI,6,0,0,timeToAdvance,stacjeList.size()-1);
        }
        if(watting == 50){
            System.out.println("DODANO STACJE szóstą");
            stacjeList.add(createObject("Stacja"));
            update_StacjaAttr(6,DLUGOSC_KOLEJKI,1,0,0,timeToAdvance,stacjeList.size()-1);
        }

        watting++;


        //-----nasluchiwanie----//
        if(this.federationAmbassador.promClassFlag_newInstance){
            Prom prom = this.federationAmbassador.getObjectInstances(Prom.class);
            this.federationAmbassador.promClassFlag_newInstance = false;

            System.out.println("Wykryto PROM PRZEZ STACJE");
        }
        if(this.federationAmbassador.promClassFlag_attrsUpdated){
            Prom prom = this.federationAmbassador.getObjectInstances(Prom.class);
            this.federationAmbassador.promClassFlag_attrsUpdated = false;
            if(prom.getNumerStacji() != poprzedniaStacjaPromu){
                //TODO ZMIANA ATRYBUTOW POPRZEDNIEJ STACJI
                update_StacjaAttr_PromNaStacji(prom.getNumerStacji(),1,timeToAdvance,prom.getNumerStacji()-1);
                poprzedniaStacjaPromu = prom.getNumerStacji();
            }


        }

        //--PASAZER--//
        if(this.federationAmbassador.pasazerClassFlag_newInstance){
            Pasazer pasazer = this.federationAmbassador.getPasazerObjInstances(this.federationAmbassador.pasazerOstatnioDodany);
            this.federationAmbassador.pasazerClassFlag_newInstance = false;
            this.federationAmbassador.pasazerOstatnioDodany = 0;

            System.out.println("WykrytoPasazeraPrzez stacje: "+pasazer.getId());
        }
        if(this.federationAmbassador.pasazerClassFlag_attrsUpdated){
            Pasazer pasazer = this.federationAmbassador.getPasazerObjInstances(this.federationAmbassador.pasazerOstatnioModyfikowany);
            this.federationAmbassador.pasazerClassFlag_attrsUpdated = false;
            this.federationAmbassador.pasazerOstatnioModyfikowany   = 0;

            System.out.println("ZAKTUALIZOWANO PARAMETRY Pasazera: "+pasazer.getId());
        }

        //TODO DODAC REAGOWANIE NA KTOREJ STACJI JEST PASAZER

    }

    @Override
    protected void publishAndSubscribe() throws RTIexception {

        int stacjaClass                         = rtiamb.getObjectClassHandle("ObjectRoot.Stacja");
        int stacjaClassAttrNumer                = rtiamb.getAttributeHandle("numer",stacjaClass);
        int stacjaClassAttrMaxDlugoscKolejki    = rtiamb.getAttributeHandle("maxDlugoscKolejki",stacjaClass);
        int stacjaClassAttrNumerKolejnejStacji  = rtiamb.getAttributeHandle("numerKolejnejStacji",stacjaClass);
        int stacjaClassAttrLiczbaPasazerow      = rtiamb.getAttributeHandle("liczbaPasazerow",stacjaClass);
        int stacjaClassAttrLiczbaSamochodow     = rtiamb.getAttributeHandle("liczbaSamochodow",stacjaClass);
        int stacjaClassAttrPromNaStacji     = rtiamb.getAttributeHandle("promNaStacji",stacjaClass);
        AttributeHandleSet attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add(stacjaClassAttrNumer);
        attributes.add(stacjaClassAttrMaxDlugoscKolejki);
        attributes.add(stacjaClassAttrNumerKolejnejStacji);
        attributes.add(stacjaClassAttrLiczbaPasazerow);
        attributes.add(stacjaClassAttrLiczbaSamochodow);
        attributes.add(stacjaClassAttrPromNaStacji);

        rtiamb.publishObjectClass(stacjaClass,attributes);


        //-----PROM----//
        this.federationAmbassador.promClass                     = rtiamb.getObjectClassHandle("ObjectRoot.Prom");
        this.federationAmbassador.promAttr_liczbaWolnychMiejsc  = rtiamb.getAttributeHandle("liczbaWolnychMiejsc",this.federationAmbassador.promClass);
        this.federationAmbassador.promAttr_numerStacji          = rtiamb.getAttributeHandle("numerStacji",this.federationAmbassador.promClass);

        attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add(this.federationAmbassador.promAttr_liczbaWolnychMiejsc);
        attributes.add(this.federationAmbassador.promAttr_numerStacji);

        rtiamb.subscribeObjectClassAttributes(this.federationAmbassador.promClass,attributes);



        //---PASAZER---//
        this.federationAmbassador.pasazerClass                  = rtiamb.getObjectClassHandle("ObjectRoot.Pasazer");
        this.federationAmbassador.pasazerAttr_id                = rtiamb.getAttributeHandle("id",this.federationAmbassador.pasazerClass);
        this.federationAmbassador.pasazerAttr_typ               = rtiamb.getAttributeHandle("typ",this.federationAmbassador.pasazerClass);
        this.federationAmbassador.pasazerAttr_numerStacji       = rtiamb.getAttributeHandle("numerStacji",this.federationAmbassador.pasazerClass);
        this.federationAmbassador.pasazerAttr_stacjaDocelowa    = rtiamb.getAttributeHandle("stacjaDocelowa",this.federationAmbassador.pasazerClass);


        attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add(this.federationAmbassador.pasazerAttr_id);
        attributes.add(this.federationAmbassador.pasazerAttr_typ);
        attributes.add(this.federationAmbassador.pasazerAttr_numerStacji);
        attributes.add(this.federationAmbassador.pasazerAttr_stacjaDocelowa);

        rtiamb.subscribeObjectClassAttributes(this.federationAmbassador.pasazerClass,attributes);

    }


    @Override
    protected void deleteObjectsAndInteractions() throws Exception {
        for(Integer stacja: stacjeList){
            this.deleteObject(stacja);
        }
    }

    private void update_StacjaAttr(int numer,int maxDlugoscKolejki,int numerKolejnejStacji,int liczbaPasazerow,int liczbaSamochodow,double time, int idx) throws ObjectNotKnown, FederateNotExecutionMember, RTIinternalError, NameNotFound, ObjectClassNotDefined, RestoreInProgress, AttributeNotOwned, AttributeNotDefined, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted {
        if(idx > stacjeList.size()-1){
            return;
        }
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int obj = this.stacjeList.get(idx);
        int classHandle = rtiamb.getObjectClass(obj);
        int attrNumerHandle = rtiamb.getAttributeHandle("numer",classHandle);
        int attrMaxDlugoscKolejkiHandle = rtiamb.getAttributeHandle("maxDlugoscKolejki",classHandle);
        int attrNumerKolejnejStacjiHandle = rtiamb.getAttributeHandle("numerKolejnejStacji",classHandle);
        int attrLiczbaPasazerowHandle = rtiamb.getAttributeHandle("liczbaPasazerow",classHandle);
        int attrNLiczbaSamochodowHandle = rtiamb.getAttributeHandle("liczbaSamochodow",classHandle);
        attributes.add(attrNumerHandle, EncodingHelpers.encodeInt(numer));
        attributes.add(attrMaxDlugoscKolejkiHandle, EncodingHelpers.encodeInt(maxDlugoscKolejki));
        attributes.add(attrNumerKolejnejStacjiHandle, EncodingHelpers.encodeInt(numerKolejnejStacji));
        attributes.add(attrLiczbaPasazerowHandle, EncodingHelpers.encodeInt(liczbaPasazerow));
        attributes.add(attrNLiczbaSamochodowHandle, EncodingHelpers.encodeInt(liczbaSamochodow));
        rtiamb.updateAttributeValues(obj,attributes,generateTag(),convertTime(time));

    }

    private void update_StacjaAttr_PromNaStacji(int numerStacji,int promNaStacji,double time, int idx) throws SaveInProgress, AttributeNotDefined, InvalidFederationTime, RestoreInProgress, ObjectNotKnown, ConcurrentAccessAttempted, AttributeNotOwned, FederateNotExecutionMember, RTIinternalError, NameNotFound, ObjectClassNotDefined {
        if(idx >= stacjeList.size()){
            return;
        }
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int obj = this.stacjeList.get(idx);
        int classHandle = rtiamb.getObjectClass(obj);
        int attrNumerHandle = rtiamb.getAttributeHandle("numer",classHandle);
        int attrPromNaStacjiHandle = rtiamb.getAttributeHandle("promNaStacji",classHandle);
        attributes.add(attrNumerHandle, EncodingHelpers.encodeInt(numerStacji));
        attributes.add(attrPromNaStacjiHandle, EncodingHelpers.encodeInt(promNaStacji));
        rtiamb.updateAttributeValues(obj,attributes,generateTag(),convertTime(time));
    }

    public static void main(String[] agrs){
        try{
            new StacjaFederate().runFederate("STACJA ");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
