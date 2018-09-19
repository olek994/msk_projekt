package msk.federate;

import hla.rti.AttributeHandleSet;
import hla.rti.RTIexception;
import msk.BaseFederate;
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


    @Override
    protected void update(double timeToAdvance) throws Exception {
        if (this.federationAmbassador.promClassFlag_newInstance) {
            Prom prom = this.federationAmbassador.getObjectInstances(Prom.class);
            //TODO
            this.federationAmbassador.promClassFlag_newInstance = false;
        }

        if(this.federationAmbassador.promClassFlag_attrsUpdated) {
            Prom prom = this.federationAmbassador.getObjectInstances(Prom.class);
            //TODO
            this.federationAmbassador.promClassFlag_attrsUpdated = false;
        }


        if(this.federationAmbassador.stacjaClassFlag_newInstance) {
            Stacja stacja = this.federationAmbassador.getObjectInstances(Stacja.class);
            //TODO
            this.federationAmbassador.stacjaClassFlag_newInstance = false;
        }

        if(this.federationAmbassador.stacjaClassFlag_attrsUpdated) {
            Stacja stacja = this.federationAmbassador.getObjectInstances(Stacja.class);
            //TODO
            this.federationAmbassador.stacjaClassFlag_attrsUpdated = false;
        }

        //TODO ADD PASAZER

    }

    @Override
    protected void publishAndSubscribe() throws RTIexception {

        this.federationAmbassador.promClass                     = rtiamb.getObjectClassHandle("ObjectRoot.Prom");
        this.federationAmbassador.promAttr_numerStacji          = rtiamb.getAttributeHandle("numerStacji", this.federationAmbassador.promClass);
        this.federationAmbassador.promAttr_liczbaWolnychMiejsc  = rtiamb.getAttributeHandle("liczbaWolnychMiejsc", this.federationAmbassador.promClass);
        this.federationAmbassador.promAttr_liczbaZajetychMiejsc = rtiamb.getAttributeHandle("liczbaZajetychMiejsc", this.federationAmbassador.promClass);

        // TODO RtiFactory

        rtiamb.subscribeObjectClassAttributes(this.federationAmbassador.promClass, attributes);


        this.federationAmbassador.stacjaClass = rtiamb.getObjectClassHandle("ObjectRoot.Stacja");
        this.federationAmbassador.stacjaAttr_numer = rtiamb.getAttributeHandle("numer", this.federationAmbassador.stacjaClass);
        this.federationAmbassador.stacjaAttr_numerKolejnejStacji = rtiamb.getAttributeHandle("numerKolejnejStacji", this.federationAmbassador.stacjaClass);
        this.federationAmbassador.stacjaAttr_PromNaStacji = rtiamb.getAttributeHandle("PromNaStacji", this.federationAmbassador.stacjaClass);
        this.federationAmbassador.stacjaAttr_MaxDlugoscKolejki = rtiamb.getAttributeHandle("MaxDlugoscKolejki", this.federationAmbassador.stacjaClass);
        this.federationAmbassador.stacjaAttr_LiczbaPasazerow = rtiamb.getAttributeHandle("LiczbaPasazerow", this.federationAmbassador.stacjaClass);
        this.federationAmbassador.stacjaAttr_LiczbaSamochodow = rtiamb.getAttributeHandle("LiczbaSamochodow", this.federationAmbassador.stacjaClass);

        // TODO RtiFactory

        rtiamb.subscribeObjectClassAttributes(this.federationAmbassador.stacjaClass, attributes);


        //TODO PASAZER
    }

    public static void main(String[] args) {
        try {
            //new StatystykaFederate().runFederate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}