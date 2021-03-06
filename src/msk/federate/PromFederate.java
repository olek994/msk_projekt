package msk.federate;

import hla.rti.*;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;
import javafx.application.Platform;
import javafx.scene.image.Image;
import msk.BaseFederate;
import msk.Objects.Pasazer;
import msk.Objects.Stacja;
import msk.ambassador.PromAmbassador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PromFederate extends BaseFederate<PromAmbassador> {

    public static int LICZBA_WOLNYCH_MIEJSC = 10;

    private int promObj = 0;
    private Random random = new Random();
    private boolean init = false;
    private int waittingForNextStation = 0;
    private int numerStacji = 0;
    private boolean cofanie = false;
    private Map<Integer,Integer> liczbaPasazerowNaStacji;
    private Map<Integer,Integer> liczbaSamochodowNaStacji;
    private int watting = 0;



    @Override
    protected void update(double timeToAdvance) throws Exception {

        if(!init){
            init = true;
            numerStacji++;
            updatePromObj_NumerStacji(numerStacji,LICZBA_WOLNYCH_MIEJSC,timeToAdvance);
        }


        //TODO SPRAWDZIC KOLEJNOSC W UPDATE FEDERATOW

        //--STACJA--//
        if(this.federationAmbassador.stacjaClassFlag_newInstance){
            Stacja stacja = this.federationAmbassador.getStacjeObjInstances(this.federationAmbassador.stacjaOstatnioDodana);
            this.federationAmbassador.stacjaClassFlag_newInstance = false;
            this.federationAmbassador.stacjaOstatnioDodana = 0;
            System.out.println("DODANO Stacje DO GUI: "+stacja.getNumer());
        }
        if(this.federationAmbassador.stacjaClassFlag_attrsUpdated) {
            Stacja stacja = this.federationAmbassador.getStacjeObjInstances(this.federationAmbassador.stacjaOstatnioModyfikowana);
            this.federationAmbassador.stacjaClassFlag_attrsUpdated = false;
            this.federationAmbassador.stacjaOstatnioModyfikowana = 0;


        }


        if(waittingForNextStation == 60 && !cofanie) {
            numerStacji++;
            updatePromObj_NumerStacji(numerStacji,-1,timeToAdvance);
            waittingForNextStation = 0;
        } else if (waittingForNextStation == 60 && cofanie){
            numerStacji--;
            updatePromObj_NumerStacji(numerStacji,-1,timeToAdvance);
            waittingForNextStation = 0;
        }

        if(numerStacji >= 6){
            cofanie = true;
        }
        if(numerStacji ==1){
            cofanie = false;
        }
        waittingForNextStation++;


        //--PASAZER--//
        if(this.federationAmbassador.pasazerClassFlag_newInstance){
            Pasazer pasazer = this.federationAmbassador.getPasazerObjInstances(this.federationAmbassador.pasazerOstatnioDodany);
            this.federationAmbassador.pasazerClassFlag_newInstance = false;
            this.federationAmbassador.pasazerOstatnioDodany = 0;


        }
        if(this.federationAmbassador.pasazerClassFlag_attrsUpdated){
            Pasazer pasazer = this.federationAmbassador.getPasazerObjInstances(this.federationAmbassador.pasazerOstatnioModyfikowany);
            this.federationAmbassador.pasazerClassFlag_attrsUpdated = false;

//            System.out.println("ZAKTUALIZOWANO PARAMETRY Pasazera: "+pasazer.getId());
            System.out.println("TYP PASAAZERA"+pasazer.getTyp());
            if(pasazer.getNaPromie() == 1 && pasazer.getWysiada() == 0){ //TODO OGARNAC TYP
                updatePromObj_LiczbaWolnychMiejsc(1,timeToAdvance);
                System.out.println("LICZBA WOLNYCH MIEJSC: "+LICZBA_WOLNYCH_MIEJSC);
            }else if(pasazer.getNaPromie() == 1 && pasazer.getWysiada() == 1){
                System.out.println("PASAZZER WYSIADL Z PROMU: "+pasazer.getId());
                updatePromObj_LiczbaWolnychMiejscDodajWolneMiejsca(1,timeToAdvance);
                this.federationAmbassador.removePasazerObjInstances(this.federationAmbassador.pasazerOstatnioModyfikowany);
            }

            this.federationAmbassador.pasazerOstatnioModyfikowany   = 0;
        }


    }

    @Override
    protected void init() throws Exception {
        this.promObj = createObject("Prom");
        liczbaPasazerowNaStacji = new HashMap<>();
        liczbaSamochodowNaStacji = new HashMap<>();

        for(int i =1;i<=6;i++){
            liczbaPasazerowNaStacji.put(i,0);
            liczbaSamochodowNaStacji.put(i,0);
        }
    }

    @Override
    protected void publishAndSubscribe() throws RTIexception {
        //Publikacja promu
        int promClass                           =rtiamb.getObjectClassHandle("ObjectRoot.Prom");
        int promClassAttrLiczbaWolnychMiejsc    =rtiamb.getAttributeHandle("liczbaWolnychMiejsc",promClass);
        int promClassAttrNumerStacji            =rtiamb.getAttributeHandle("numerStacji",promClass);

        AttributeHandleSet attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add(promClassAttrLiczbaWolnychMiejsc);
        attributes.add(promClassAttrNumerStacji);

        rtiamb.publishObjectClass(promClass,attributes);



        //----STACJA----//

        this.federationAmbassador.stacjaClass                     = rtiamb.getObjectClassHandle("ObjectRoot.Stacja");
        this.federationAmbassador.stacjaAttr_numer                = rtiamb.getAttributeHandle("numer",this.federationAmbassador.stacjaClass);

        attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add(this.federationAmbassador.stacjaAttr_numer);

        rtiamb.subscribeObjectClassAttributes(this.federationAmbassador.stacjaClass,attributes);


        //---PASAZER---//
        this.federationAmbassador.pasazerClass                  = rtiamb.getObjectClassHandle("ObjectRoot.Pasazer");
        this.federationAmbassador.pasazerAttr_id                = rtiamb.getAttributeHandle("id",this.federationAmbassador.pasazerClass);
        this.federationAmbassador.pasazerAttr_typ               = rtiamb.getAttributeHandle("typ",this.federationAmbassador.pasazerClass);
        this.federationAmbassador.pasazerAttr_numerStacji       = rtiamb.getAttributeHandle("numerStacji",this.federationAmbassador.pasazerClass);
        this.federationAmbassador.pasazerAttr_stacjaDocelowa    = rtiamb.getAttributeHandle("stacjaDocelowa",this.federationAmbassador.pasazerClass);
        this.federationAmbassador.pasazerAttr_naPromie          = rtiamb.getAttributeHandle("naPromie",this.federationAmbassador.pasazerClass);
        this.federationAmbassador.pasazerAttr_wysiada           = rtiamb.getAttributeHandle("wysiada",this.federationAmbassador.pasazerClass);


        attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add(this.federationAmbassador.pasazerAttr_id);
        attributes.add(this.federationAmbassador.pasazerAttr_typ);
        attributes.add(this.federationAmbassador.pasazerAttr_numerStacji);
        attributes.add(this.federationAmbassador.pasazerAttr_stacjaDocelowa);
        attributes.add(this.federationAmbassador.pasazerAttr_naPromie);
        attributes.add(this.federationAmbassador.pasazerAttr_wysiada);

        rtiamb.subscribeObjectClassAttributes(this.federationAmbassador.pasazerClass,attributes);


    }

    @Override
    protected void deleteObjectsAndInteractions() throws Exception {
        this.deleteObject(this.promObj);
    }

    private void updatePromObj_NumerStacji(int value,int liczbaWolnychMiejsc, double time) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, ObjectClassNotDefined, ObjectNotKnown, RestoreInProgress, AttributeNotOwned, AttributeNotDefined, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted {
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int classHandle = rtiamb.getObjectClass(this.promObj);
        int attrHandle = rtiamb.getAttributeHandle( "numerStacji", classHandle );
        int attrLiczbaWolnychMiejscHandle = rtiamb.getAttributeHandle( "liczbaWolnychMiejsc", classHandle );
        attributes.add(attrHandle, EncodingHelpers.encodeInt(value));
        if(liczbaWolnychMiejsc != -1){
            attributes.add(attrLiczbaWolnychMiejscHandle, EncodingHelpers.encodeInt(liczbaWolnychMiejsc));
        }
        rtiamb.updateAttributeValues(this.promObj, attributes, generateTag(), convertTime(time));
    }

    private void updatePromObj_LiczbaWolnychMiejsc(int value,double time) throws SaveInProgress, AttributeNotDefined, InvalidFederationTime, RestoreInProgress, ObjectNotKnown, ConcurrentAccessAttempted, AttributeNotOwned, FederateNotExecutionMember, RTIinternalError, NameNotFound, ObjectClassNotDefined {
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int classHandle = rtiamb.getObjectClass(this.promObj);
        int attrHandle = rtiamb.getAttributeHandle( "liczbaWolnychMiejsc", classHandle );
        attributes.add(attrHandle, EncodingHelpers.encodeInt(LICZBA_WOLNYCH_MIEJSC - value));
        rtiamb.updateAttributeValues(this.promObj, attributes, generateTag(), convertTime(time));
        LICZBA_WOLNYCH_MIEJSC -= value;
    }

    private void updatePromObj_LiczbaWolnychMiejscDodajWolneMiejsca(int value,double time) throws SaveInProgress, AttributeNotDefined, InvalidFederationTime, RestoreInProgress, ObjectNotKnown, ConcurrentAccessAttempted, AttributeNotOwned, FederateNotExecutionMember, RTIinternalError, NameNotFound, ObjectClassNotDefined {
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int classHandle = rtiamb.getObjectClass(this.promObj);
        int attrHandle = rtiamb.getAttributeHandle( "liczbaWolnychMiejsc", classHandle );
        attributes.add(attrHandle, EncodingHelpers.encodeInt(LICZBA_WOLNYCH_MIEJSC + value));
        rtiamb.updateAttributeValues(this.promObj, attributes, generateTag(), convertTime(time));
        LICZBA_WOLNYCH_MIEJSC += value;
    }


    public static void main(String[] agrs){
        try{
            new PromFederate().runFederate("PROM");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
