package msk.federate;

import hla.rti.*;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;
import javafx.application.Platform;
import javafx.scene.image.Image;
import msk.BaseFederate;
import msk.Objects.Stacja;
import msk.ambassador.PromAmbassador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PromFederate extends BaseFederate<PromAmbassador> {

    public static int LICZBA_WOLNYCH_MIEJSC = 30;

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
            System.out.println("LICZBA PASAZEROW: "+stacja.getLiczbaPasazerow());
            System.out.println("LICZBA PASAZEROW na promie stacjo: "+liczbaPasazerowNaStacji.get(stacja.getNumer()));
            System.out.println("LICZBA Samochodow: "+stacja.getLiczbaSamochodow());
            System.out.println("LICZBA Samochodow na promie stacji: "+liczbaSamochodowNaStacji.get(stacja.getNumer()));
            if(liczbaPasazerowNaStacji.get(stacja.getNumer()) < stacja.getLiczbaPasazerow()){
                liczbaPasazerowNaStacji.replace(stacja.getNumer(),stacja.getLiczbaPasazerow());
            }
            if(liczbaSamochodowNaStacji.get(stacja.getNumer()) <stacja.getLiczbaSamochodow()){
                liczbaSamochodowNaStacji.replace(stacja.getNumer(),stacja.getLiczbaPasazerow());
            }

            if (numerStacji == stacja.getNumer()) {

                System.out.println("NUMER STACJI: "+numerStacji);

                System.out.println("LICZBA Pasazerow PROM: "+liczbaPasazerowNaStacji+" liczba pasazerow stacja: "+stacja.getLiczbaPasazerow()+" Liczba wolnych miejsc: "+LICZBA_WOLNYCH_MIEJSC);



                //TODO OGARNIECIE USUWANIA PASAZEROW Z PROMU
                //DODAWANIE PASAZEROW DO PROMU
                if(liczbaPasazerowNaStacji.get(numerStacji) > stacja.getLiczbaPasazerow() && LICZBA_WOLNYCH_MIEJSC >= 1){
                    System.out.println("dodano pasazera na prom");
                    updatePromObj_LiczbaWolnychMiejsc(1,timeToAdvance);
                }else if(liczbaSamochodowNaStacji.get(numerStacji) > stacja.getLiczbaSamochodow() && LICZBA_WOLNYCH_MIEJSC >= 3){
                    System.out.println("Dodano samochod na prom");
                    updatePromObj_LiczbaWolnychMiejsc(3,timeToAdvance);
                }


                System.out.println("LICZBA PASAZEROW: " + liczbaPasazerowNaStacji.get(numerStacji));
                System.out.println("LICZBA SAMOCHODOW: " + liczbaSamochodowNaStacji.get(numerStacji));




            }

            liczbaPasazerowNaStacji.replace(numerStacji,stacja.getLiczbaPasazerow());
            liczbaSamochodowNaStacji.replace(numerStacji,stacja.getLiczbaSamochodow());

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



        //TODO DODAWANIE PASAZEROW
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
        int promClassAttrLiczbaZajetychMiejsc    =rtiamb.getAttributeHandle("liczbaZajetychMiejsc",promClass);
        int promClassAttrNumerStacji            =rtiamb.getAttributeHandle("numerStacji",promClass);

        AttributeHandleSet attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add(promClassAttrLiczbaWolnychMiejsc);
        attributes.add(promClassAttrNumerStacji);
        attributes.add(promClassAttrLiczbaZajetychMiejsc);

        rtiamb.publishObjectClass(promClass,attributes);



        //----STACJA----//

        this.federationAmbassador.stacjaClass                     = rtiamb.getObjectClassHandle("ObjectRoot.Stacja");
        this.federationAmbassador.stacjaAttr_numer                = rtiamb.getAttributeHandle("numer",this.federationAmbassador.stacjaClass);
        this.federationAmbassador.stacjaAttr_MaxDlugoscKolejki    = rtiamb.getAttributeHandle("maxDlugoscKolejki",this.federationAmbassador.stacjaClass);
        this.federationAmbassador.stacjaAttr_numerKolejnejStacji  = rtiamb.getAttributeHandle("numerKolejnejStacji",this.federationAmbassador.stacjaClass);
        this.federationAmbassador.stacjaAttr_LiczbaPasazerow      = rtiamb.getAttributeHandle("liczbaPasazerow",this.federationAmbassador.stacjaClass);
        this.federationAmbassador.stacjaAttr_LiczbaSamochodow     = rtiamb.getAttributeHandle("liczbaSamochodow",this.federationAmbassador.stacjaClass);
        this.federationAmbassador.stacjaAttr_PromNaStacji         = rtiamb.getAttributeHandle("promNaStacji",this.federationAmbassador.stacjaClass);

        attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add(this.federationAmbassador.stacjaAttr_numer);
        attributes.add(this.federationAmbassador.stacjaAttr_MaxDlugoscKolejki);
        attributes.add(this.federationAmbassador.stacjaAttr_numerKolejnejStacji);
        attributes.add(this.federationAmbassador.stacjaAttr_LiczbaPasazerow);
        attributes.add(this.federationAmbassador.stacjaAttr_LiczbaSamochodow);
        attributes.add(this.federationAmbassador.stacjaAttr_PromNaStacji);

        rtiamb.subscribeObjectClassAttributes(this.federationAmbassador.stacjaClass,attributes);



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


    public static void main(String[] agrs){
        try{
            new PromFederate().runFederate("PROM");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
