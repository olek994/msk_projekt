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

    private int liczbaPrzeplynietychStacji = 0;
    private int poprzedniaStacja = 0;

    @Override
    protected void update(double timeToAdvance) throws Exception {
        if (this.federationAmbassador.promClassFlag_newInstance) {
            Prom prom = this.federationAmbassador.getObjectInstances(Prom.class);
            liczbaPrzeplynietychStacji++;
            this.federationAmbassador.promClassFlag_newInstance = false;
        }

        if(this.federationAmbassador.promClassFlag_attrsUpdated) {
            Prom prom = this.federationAmbassador.getObjectInstances(Prom.class);
            if (prom.getNumerStacji() != poprzedniaStacja) {
                liczbaPrzeplynietychStacji++;
                poprzedniaStacja = prom.getNumerStacji();
                System.out.println("STATYSTYKA: LICZBA PRZEPLYNIETYCH STACJI: "+liczbaPrzeplynietychStacji);
            }
            this.federationAmbassador.promClassFlag_attrsUpdated = false;
        }


        if(this.federationAmbassador.stacjaClassFlag_newInstance) {
            Stacja stacja = this.federationAmbassador.getObjectInstances(Stacja.class);
            //TODO
            this.federationAmbassador.stacjaClassFlag_newInstance = false;
        }

        if(this.federationAmbassador.stacjaClassFlag_attrsUpdated) {
            Stacja stacja = this.federationAmbassador.getObjectInstances(Stacja.class);

            this.federationAmbassador.stacjaClassFlag_attrsUpdated = false;
        }

        if(this.federationAmbassador.pasazerClassFlag_newInstance) {
            Pasazer pasazer = this.federationAmbassador.getObjectInstances(Pasazer.class);
            //TODO
            this.federationAmbassador.pasazerClassFlag_newInstance = false;
        }

        if(this.federationAmbassador.pasazerClassFlag_attrsUpdated) {
            Pasazer pasazer = this.federationAmbassador.getObjectInstances(Pasazer.class);
            //TODO
            this.federationAmbassador.pasazerClassFlag_attrsUpdated = false;
        }

    }

    @Override
    protected void publishAndSubscribe() throws RTIexception {

        this.federationAmbassador.promClass                     = rtiamb.getObjectClassHandle("ObjectRoot.Prom");
        this.federationAmbassador.promAttr_numerStacji          = rtiamb.getAttributeHandle("numerStacji", this.federationAmbassador.promClass);
        this.federationAmbassador.promAttr_liczbaWolnychMiejsc  = rtiamb.getAttributeHandle("liczbaWolnychMiejsc", this.federationAmbassador.promClass);
        this.federationAmbassador.promAttr_liczbaZajetychMiejsc = rtiamb.getAttributeHandle("liczbaZajetychMiejsc", this.federationAmbassador.promClass);

        AttributeHandleSet attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add(this.federationAmbassador.promAttr_numerStacji);
        attributes.add(this.federationAmbassador.promAttr_liczbaWolnychMiejsc);
        attributes.add(this.federationAmbassador.promAttr_liczbaZajetychMiejsc);

        rtiamb.subscribeObjectClassAttributes(this.federationAmbassador.promClass, attributes);


        this.federationAmbassador.stacjaClass                   = rtiamb.getObjectClassHandle("ObjectRoot.Stacja");
        this.federationAmbassador.stacjaAttr_numer              = rtiamb.getAttributeHandle("numer", this.federationAmbassador.stacjaClass);
        this.federationAmbassador.stacjaAttr_numerKolejnejStacji= rtiamb.getAttributeHandle("numerKolejnejStacji", this.federationAmbassador.stacjaClass);
        this.federationAmbassador.stacjaAttr_PromNaStacji       = rtiamb.getAttributeHandle("PromNaStacji", this.federationAmbassador.stacjaClass);
        this.federationAmbassador.stacjaAttr_MaxDlugoscKolejki  = rtiamb.getAttributeHandle("MaxDlugoscKolejki", this.federationAmbassador.stacjaClass);
        this.federationAmbassador.stacjaAttr_LiczbaPasazerow    = rtiamb.getAttributeHandle("LiczbaPasazerow", this.federationAmbassador.stacjaClass);
        this.federationAmbassador.stacjaAttr_LiczbaSamochodow   = rtiamb.getAttributeHandle("LiczbaSamochodow", this.federationAmbassador.stacjaClass);

        attributes.add(this.federationAmbassador.stacjaAttr_numer);
        attributes.add(this.federationAmbassador.stacjaAttr_numerKolejnejStacji);
        attributes.add(this.federationAmbassador.stacjaAttr_PromNaStacji);
        attributes.add(this.federationAmbassador.stacjaAttr_MaxDlugoscKolejki);
        attributes.add(this.federationAmbassador.stacjaAttr_LiczbaPasazerow);
        attributes.add(this.federationAmbassador.stacjaAttr_LiczbaSamochodow);

        rtiamb.subscribeObjectClassAttributes(this.federationAmbassador.stacjaClass, attributes);


        this.federationAmbassador.pasazerClass                  = rtiamb.getObjectClassHandle("ObjectRoot.Pasazer");
        this.federationAmbassador.pasazerAttr_id                = rtiamb.getAttributeHandle("id", this.federationAmbassador.pasazerClass);
        this.federationAmbassador.pasazerAttr_typ               = rtiamb.getAttributeHandle("typ", this.federationAmbassador.pasazerClass);
        this.federationAmbassador.pasazerAttr_numerStacji       = rtiamb.getAttributeHandle("numerStacji", this.federationAmbassador.pasazerClass);
        this.federationAmbassador.pasazerAttr_stacjaDocelowa    = rtiamb.getAttributeHandle("stacjaDocelowa", this.federationAmbassador.pasazerClass);

        attributes.add(this.federationAmbassador.pasazerAttr_id);
        attributes.add(this.federationAmbassador.pasazerAttr_typ);
        attributes.add(this.federationAmbassador.pasazerAttr_numerStacji);
        attributes.add(this.federationAmbassador.pasazerAttr_stacjaDocelowa);

        rtiamb.subscribeObjectClassAttributes(this.federationAmbassador.pasazerClass, attributes);
    }

    public static void main(String[] args) {
        try {
            new StatystykaFederate().runFederate("STATYSTYKA");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}