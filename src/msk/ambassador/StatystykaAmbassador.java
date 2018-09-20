package msk.ambassador;

import hla.rti.EventRetractionHandle;
import hla.rti.LogicalTime;
import hla.rti.ReflectedAttributes;
import msk.BaseAmbassador;
import msk.Objects.Prom;
import msk.Objects.Stacja;

/**
 * Project msk_projekt
 * Created by Szymon Sobotkiewicz on 19.09.2018
 */
public class StatystykaAmbassador extends BaseAmbassador {

    // parametery Poszczególnych federatów
    public int promClass                     =0;
    public int promAttr_liczbaWolnychMiejsc  =0;
    public int promAttr_numerStacji          =0;
    public int promAttr_liczbaZajetychMiejsc =0;
    public boolean promClassFlag_newInstance  = false;
    public boolean promClassFlag_attrsUpdated = false;

    public int stacjaClass                     =0;
    public int stacjaAttr_MaxDlugoscKolejki    =0;
    public int stacjaAttr_numer                =0;
    public int stacjaAttr_numerKolejnejStacji  =0;
    public int stacjaAttr_LiczbaPasazerow      =0;
    public int stacjaAttr_LiczbaSamochodow     =0;
    public int stacjaAttr_PromNaStacji         =0;
    public int stacjaNumerStworzenia           =1;
    public int stacjaOstatnioModyfikowana      =0;
    public int stacjaOstatnioDodana            =0;
    public boolean stacjaClassFlag_newInstance  = false;
    public boolean stacjaClassFlag_attrsUpdated = false;

    // TODO DODAC PASAZERA


    @Override
    public void reflectAttributeValues(int theObject, ReflectedAttributes theAttributes, byte[] tag, LogicalTime theTime, EventRetractionHandle retractionHandle) {
        if(this.objects.get(theObject) == this.promClass){
            Prom prom = getObjectInstances(Prom.class);

        } else if(this.objects.get(theObject) == this.stacjaClass){
            Stacja stacja = getObjectInstances(Stacja.class);
        } // TODO Pasazer
    }

    @Override
    public void discoverObjectInstance(int theObject, int theObjectClass, String objectName) {
        if(theObjectClass == this.promClass){

        } else if(theObjectClass == this.stacjaClass){

        } // TODO else if pasazerClass

    }

}
