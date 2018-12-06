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
import msk.Objects.Stacja;
import msk.ambassador.StacjaAmbassador;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class StacjaFederate extends BaseFederate<StacjaAmbassador> {

    private static final int DLUGOSC_KOLEJKI = 15;
    private static final int LICZBA_STACJI = 6;
    private List<Integer> stacjeList;
    private boolean init = false;
    private int wattingForStationCreation = 0;
    private int poprzedniaStacjaPromu = -1;
    private Map<Integer,Integer> liczbaPasazerowNaStacji;
    private Map<Integer,Integer> liczbaSamochodowNaStacji;
    private int numerPoprzednioDodanejStacji = 0;
    private boolean poprzedniDoWylaczenia = false;
    private int oczekiwanieNaWylaczenie = 0;
    private int poprzedniaStacja = 0;
    private int watting = 0;
    private Random random;
    //private int liczbaWolnychMIejscNaPromie;
    @Override
    protected void init() throws Exception {
        this.stacjeList = new ArrayList<>();
        this.random = new Random();
        this.liczbaPasazerowNaStacji = new HashMap<>();
        this.liczbaSamochodowNaStacji = new HashMap<>();
    }

    @Override
    protected void update(double timeToAdvance) throws Exception {


        if(wattingForStationCreation == 5 && numerPoprzednioDodanejStacji < LICZBA_STACJI){
            int kolejnaStacja = numerPoprzednioDodanejStacji +2;
            if(kolejnaStacja == 7){
                kolejnaStacja = 1;
            }
            System.out.println("DODANO STACJE: "+(numerPoprzednioDodanejStacji+1));
            stacjeList.add(createObject("Stacja"));
            update_StacjaAttr(numerPoprzednioDodanejStacji+1,timeToAdvance,numerPoprzednioDodanejStacji);//numerPoprzednioDodanej moze posluzyc do listy (bo od 0 sie zaczyna)
            liczbaPasazerowNaStacji.put(numerPoprzednioDodanejStacji+1,0);
            liczbaSamochodowNaStacji.put(numerPoprzednioDodanejStacji+1,0);
            numerPoprzednioDodanejStacji++;
        }

        if(wattingForStationCreation == 5){
            wattingForStationCreation = 0;
        }

        wattingForStationCreation++;


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
                if(poprzedniaStacjaPromu != -1){
                    poprzedniDoWylaczenia = true;
                    poprzedniaStacja = poprzedniaStacjaPromu;
                }
                update_StacjaAttr_PromNaStacji(prom.getNumerStacji(),1,timeToAdvance,prom.getNumerStacji()-1);
                poprzedniaStacjaPromu = prom.getNumerStacji();
            }

        }

        if(poprzedniDoWylaczenia && oczekiwanieNaWylaczenie == 5){
            update_StacjaAttr_PromNaStacji(poprzedniaStacja,0,timeToAdvance,poprzedniaStacja-1);
            oczekiwanieNaWylaczenie = 0;
            poprzedniDoWylaczenia = false;
        }

        if(poprzedniDoWylaczenia){
            oczekiwanieNaWylaczenie++;
        }



        //--PASAZER--//
        if(this.federationAmbassador.pasazerClassFlag_newInstance){
            Pasazer pasazer = this.federationAmbassador.getPasazerObjInstances(this.federationAmbassador.pasazerOstatnioDodany);
            this.federationAmbassador.pasazerClassFlag_newInstance = false;
            this.federationAmbassador.pasazerOstatnioDodany = 0;


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
        AttributeHandleSet attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add(stacjaClassAttrNumer);

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
        this.federationAmbassador.pasazerAttr_naPromie          = rtiamb.getAttributeHandle("naPromie",this.federationAmbassador.pasazerClass);


        attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add(this.federationAmbassador.pasazerAttr_id);
        attributes.add(this.federationAmbassador.pasazerAttr_typ);
        attributes.add(this.federationAmbassador.pasazerAttr_numerStacji);
        attributes.add(this.federationAmbassador.pasazerAttr_stacjaDocelowa);
        attributes.add(this.federationAmbassador.pasazerAttr_naPromie);

        rtiamb.subscribeObjectClassAttributes(this.federationAmbassador.pasazerClass,attributes);

    }


    @Override
    protected void deleteObjectsAndInteractions() throws Exception {
        for(Integer stacja: stacjeList){
            this.deleteObject(stacja);
        }
    }

    private void update_StacjaAttr(int numer,double time, int idx) throws ObjectNotKnown, FederateNotExecutionMember, RTIinternalError, NameNotFound, ObjectClassNotDefined, RestoreInProgress, AttributeNotOwned, AttributeNotDefined, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted {
        if(idx > stacjeList.size()-1){
            return;
        }
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int obj = this.stacjeList.get(idx);
        int classHandle = rtiamb.getObjectClass(obj);
        int attrNumerHandle = rtiamb.getAttributeHandle("numer",classHandle);
        attributes.add(attrNumerHandle, EncodingHelpers.encodeInt(numer));
        rtiamb.updateAttributeValues(obj,attributes,generateTag(),convertTime(time));

    }

    private void update_StacjaAttr_dodaj_Pasazer(int numerStacji,double time,int idx) throws ObjectClassNotDefined, RTIinternalError, NameNotFound, FederateNotExecutionMember, SaveInProgress, RestoreInProgress, AttributeNotOwned, ObjectNotKnown, AttributeNotDefined, InvalidFederationTime, ConcurrentAccessAttempted {
        if(idx >= stacjeList.size()){
            return;
        }
        liczbaPasazerowNaStacji.replace(numerStacji,liczbaPasazerowNaStacji.get(numerStacji)+1);
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int obj = this.stacjeList.get(idx);
        int classHandle = rtiamb.getObjectClass(obj);
        int attrNumerHandle = rtiamb.getAttributeHandle("numer",classHandle);
        int attrLiczbaPasazerowHandle = rtiamb.getAttributeHandle("liczbaPasazerow",classHandle);
        attributes.add(attrNumerHandle, EncodingHelpers.encodeInt(numerStacji));
        attributes.add(attrLiczbaPasazerowHandle, EncodingHelpers.encodeInt(liczbaPasazerowNaStacji.get(numerStacji)));
        rtiamb.updateAttributeValues(obj,attributes,generateTag(),convertTime(time));
    }

    private void update_StacjaAttr_usun_Pasazer(int numerStacji,double time,int idx) throws ObjectClassNotDefined, RTIinternalError, NameNotFound, FederateNotExecutionMember, SaveInProgress, RestoreInProgress, AttributeNotOwned, ObjectNotKnown, AttributeNotDefined, InvalidFederationTime, ConcurrentAccessAttempted {
        if(idx >= stacjeList.size()){
            return;
        }
        if(liczbaPasazerowNaStacji.get(numerStacji) == 0){
            return;
        }
        liczbaPasazerowNaStacji.replace(numerStacji,liczbaPasazerowNaStacji.get(numerStacji)-1);
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int obj = this.stacjeList.get(idx);
        int classHandle = rtiamb.getObjectClass(obj);
        int attrNumerHandle = rtiamb.getAttributeHandle("numer",classHandle);
        int attrLiczbaPasazerowHandle = rtiamb.getAttributeHandle("liczbaPasazerow",classHandle);
        attributes.add(attrNumerHandle, EncodingHelpers.encodeInt(numerStacji));
        attributes.add(attrLiczbaPasazerowHandle, EncodingHelpers.encodeInt(liczbaPasazerowNaStacji.get(numerStacji)));
        rtiamb.updateAttributeValues(obj,attributes,generateTag(),convertTime(time));
    }

    private void update_StacjaAttr_dodaj_Samochod(int numerStacji, double timeToAdvance, int idx) throws ObjectClassNotDefined, RTIinternalError, NameNotFound, FederateNotExecutionMember, SaveInProgress, RestoreInProgress, AttributeNotOwned, ObjectNotKnown, AttributeNotDefined, InvalidFederationTime, ConcurrentAccessAttempted {
        if(idx >= stacjeList.size()){
            return;
        }
        liczbaSamochodowNaStacji.replace(numerStacji,liczbaSamochodowNaStacji.get(numerStacji)+1);
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int obj = this.stacjeList.get(idx);
        int classHandle = rtiamb.getObjectClass(obj);
        int attrNumerHandle = rtiamb.getAttributeHandle("numer",classHandle);
        int attrLLiczbaSamochodowHandle = rtiamb.getAttributeHandle("liczbaSamochodow",classHandle);
        attributes.add(attrNumerHandle, EncodingHelpers.encodeInt(numerStacji));
        attributes.add(attrLLiczbaSamochodowHandle, EncodingHelpers.encodeInt(liczbaSamochodowNaStacji.get(numerStacji)));
        rtiamb.updateAttributeValues(obj,attributes,generateTag(),convertTime(timeToAdvance));
    }

    private void update_StacjaAttr_usun_Samochod(int numerStacji,double time,int idx) throws ObjectClassNotDefined, RTIinternalError, NameNotFound, FederateNotExecutionMember, SaveInProgress, RestoreInProgress, AttributeNotOwned, ObjectNotKnown, AttributeNotDefined, InvalidFederationTime, ConcurrentAccessAttempted {
        if(idx >= stacjeList.size()){
            return;
        }
        if(liczbaSamochodowNaStacji.get(numerStacji) == 0){
            return;
        }
        liczbaSamochodowNaStacji.replace(numerStacji,liczbaSamochodowNaStacji.get(numerStacji)-1);
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int obj = this.stacjeList.get(idx);
        int classHandle = rtiamb.getObjectClass(obj);
        int attrNumerHandle = rtiamb.getAttributeHandle("numer",classHandle);
        int attrLLiczbaSamochodowHandle = rtiamb.getAttributeHandle("liczbaSamochodow",classHandle);
        attributes.add(attrNumerHandle, EncodingHelpers.encodeInt(numerStacji));
        attributes.add(attrLLiczbaSamochodowHandle, EncodingHelpers.encodeInt(liczbaSamochodowNaStacji.get(numerStacji)));
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
        attributes.add(attrNumerHandle, EncodingHelpers.encodeInt(numerStacji));
        rtiamb.updateAttributeValues(obj,attributes,generateTag(),convertTime(time));
    }

    public static void main(String[] agrs){
        try{
            new StacjaFederate().runFederate("Stacja");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
