package msk.federate;

import hla.rti.AttributeHandleSet;
import hla.rti.RTIexception;
import hla.rti.jlc.RtiFactoryFactory;
import msk.BaseFederate;
import msk.Objects.Pasazer;
import msk.Objects.Prom;
import msk.Objects.Stacja;
import msk.ambassador.StatystykaAmbassador;

/**
 * Project msk_projekt
 * Created by Szymon Sobotkiewicz on 19.09.2018
 */
public class StatystykaFederate extends BaseFederate<StatystykaAmbassador> {


    private double start;
    private double end;

    private int poprzedniaStacjaPromu = 0;


    @Override
    protected void update(double timeToAdvance) throws Exception {

        //PROM
        if (this.federationAmbassador.promClassFlag_newInstance) {
            Prom prom = this.federationAmbassador.getObjectInstances(Prom.class);
            this.federationAmbassador.promClassFlag_newInstance = false;
            this.federationAmbassador.czasPrzyplynieciaPromu = this.federationAmbassador.federateTime;
        }
        if (this.federationAmbassador.promClassFlag_attrsUpdated) {
            Prom prom = this.federationAmbassador.getObjectInstances(Prom.class);
            this.federationAmbassador.promClassFlag_attrsUpdated = false;
            this.federationAmbassador.liczbaMiejscNaPromie = PromFederate.LICZBA_WOLNYCH_MIEJSC;
            if (prom.getNumerStacji() != poprzedniaStacjaPromu) {
                //prom jest na nowej stacji
                this.federationAmbassador.liczbaPrzeplynietychStacji++;
                poprzedniaStacjaPromu = prom.getNumerStacji();
                System.out.println("LICZBA PRZEPLYNIETYCH STACJI: "+this.federationAmbassador.liczbaPrzeplynietychStacji);
            } else {

            }

        }

        //--STACJA--//
        if(this.federationAmbassador.stacjaClassFlag_newInstance){
            Stacja stacja = this.federationAmbassador.getStacjeObjInstances(this.federationAmbassador.stacjaOstatnioDodana);
            this.federationAmbassador.stacjaClassFlag_newInstance = false;
            this.federationAmbassador.stacjaOstatnioDodana = 0;
            this.federationAmbassador.liczbaDodanychStacji++;
            System.out.println("Liczba dodanych stacji: "+this.federationAmbassador.liczbaDodanychStacji );
        }
        if(this.federationAmbassador.stacjaClassFlag_attrsUpdated) {
            Stacja stacja = this.federationAmbassador.getStacjeObjInstances(this.federationAmbassador.stacjaOstatnioModyfikowana);
            this.federationAmbassador.stacjaClassFlag_attrsUpdated = false;
            this.federationAmbassador.stacjaOstatnioModyfikowana = 0;


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
            if(pasazer.getNaPromie() == 0 && pasazer.getWysiada() == 0){
                if(pasazer.getTyp() == 1){
                    this.federationAmbassador.liczbaDodanychPasazerow++;
                    System.out.println("Liczba dodanych pasazerow: "+this.federationAmbassador.liczbaDodanychPasazerow);
                }else if(pasazer.getTyp() == 2){
                    this.federationAmbassador.liczbaDodanychSamochodow++;
                    System.out.println("Liczba dodanych samochodow: "+this.federationAmbassador.liczbaDodanychSamochodow);
                }
            }else if(pasazer.getNaPromie() == 1 && pasazer.getWysiada() == 0){
                if(pasazer.getTyp() == 1){
                    this.federationAmbassador.liczbaPasazerowKtorzyWsiedliNaProm++;
                    System.out.println("Liczba pasazerow ktorzy wsiedli na prom: "+this.federationAmbassador.liczbaPasazerowKtorzyWsiedliNaProm);
                }else if(pasazer.getTyp() == 2){
                    this.federationAmbassador.liczbaSamochodowKtoreWsiadlyNaProm++;
                    System.out.println("Liczba samochodow ktore wsiadly na prom: "+this.federationAmbassador.liczbaSamochodowKtoreWsiadlyNaProm);
                }
            }else if(pasazer.getNaPromie() == 1 && pasazer.getWysiada() == 1){
                if(pasazer.getTyp() == 1){
                    this.federationAmbassador.liczbaWysiadajacychPasazerow++;
                    System.out.println("Liczba pasazerow ktorzy wysiedli: "+this.federationAmbassador.liczbaWysiadajacychPasazerow);
                }else if(pasazer.getTyp() == 2){
                    this.federationAmbassador.liczbaWysiadajacychSamochodow++;
                    System.out.println("Liczba samochodow ktore wysiadly: "+this.federationAmbassador.liczbaWysiadajacychSamochodow);
                }
            }


        }
    }

    @Override
    protected void publishAndSubscribe() throws RTIexception {

        //-----PROM----//
        this.federationAmbassador.promClass                     = rtiamb.getObjectClassHandle("ObjectRoot.Prom");
        this.federationAmbassador.promAttr_liczbaWolnychMiejsc  = rtiamb.getAttributeHandle("liczbaWolnychMiejsc",this.federationAmbassador.promClass);
        this.federationAmbassador.promAttr_numerStacji          = rtiamb.getAttributeHandle("numerStacji",this.federationAmbassador.promClass);

        AttributeHandleSet attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add(this.federationAmbassador.promAttr_liczbaWolnychMiejsc);
        attributes.add(this.federationAmbassador.promAttr_numerStacji);

        rtiamb.subscribeObjectClassAttributes(this.federationAmbassador.promClass,attributes);
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

    public static void main(String[] args) {
        try {
            new StatystykaFederate().runFederate("Statystyka");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}