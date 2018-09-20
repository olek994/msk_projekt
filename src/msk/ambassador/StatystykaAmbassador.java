package msk.ambassador;

import hla.rti.EventRetractionHandle;
import hla.rti.LogicalTime;
import hla.rti.ReflectedAttributes;
import hla.rti.jlc.EncodingHelpers;
import msk.BaseAmbassador;
import msk.Objects.Pasazer;
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

    public int pasazerClass                      =0;
    public int pasazerAttr_id                    =0;
    public int pasazerAttr_typ                   =0;
    public int pasazerAttr_numerStacji           =0;
    public int pasazerAttr_stacjaDocelowa        =0;
    public int pasazerNumerStworzenia            =1;
    public int pasazerOstatnioDodany             =0;
    public int pasazerOstatnioModyfikowany       =0;
    public boolean pasazerClassFlag_newInstance  = false;
    public boolean pasazerClassFlag_attrsUpdated = false;



    @Override
    public void reflectAttributeValues(int theObject, ReflectedAttributes theAttributes, byte[] tag, LogicalTime theTime, EventRetractionHandle retractionHandle) {
        if(this.objects.get(theObject) == this.promClass){
            Prom prom = getObjectInstances(Prom.class);
            for(int i = 0;i<theAttributes.size();i++){
                try{
                    int handle = theAttributes.getAttributeHandle(i);
                    byte[] value = theAttributes.getValue(i);

                    if(handle == promAttr_liczbaWolnychMiejsc && value != null){
                        prom.setLiczbaWolnychMiejsc(EncodingHelpers.decodeInt(value));
                    } else if (handle == promAttr_numerStacji && value != null){
                        prom.setNumerStacji(EncodingHelpers.decodeInt(value));
                    }

                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }

        }else if(this.objects.get(theObject) == this.stacjaClass) {

            //Wyszukanie ktora stacja zmienila stan
            int numerStacji = 0;
            for (int i = 0; i < theAttributes.size(); i++) {
                try {
                    int handle = theAttributes.getAttributeHandle(i);
                    byte[] value = theAttributes.getValue(i);

                    if (handle == stacjaAttr_numer && value != null) {
                        numerStacji = EncodingHelpers.decodeInt(value);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            Stacja stacja = getStacjeObjInstances(numerStacji);

        } else if(this.objects.get(theObject) == this.pasazerClass) {

            //Wyszukanie ktora stacja zmienila stan
            int idPasazera = 0;
            for (int i = 0; i < theAttributes.size(); i++) {
                try {
                    int handle = theAttributes.getAttributeHandle(i);
                    byte[] value = theAttributes.getValue(i);

                    if (handle == pasazerAttr_id && value != null) {
                        idPasazera = EncodingHelpers.decodeInt(value);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            Pasazer pasazer = getPasazerObjInstances(idPasazera);
        }
    }

    @Override
    public void discoverObjectInstance(int theObject, int theObjectClass, String objectName) {
        if(theObjectClass == this.promClass){
            Prom prom = new Prom();
            prom.setInstance(theObject);
            this.objectsInstance.put(Prom.class, prom);
            this.promClassFlag_newInstance = true;
        } else if(theObjectClass == this.stacjaClass){
            Stacja stacja = new Stacja();
            stacja.setInstance(theObject);
            this.objectsInstance.put(Stacja.class, stacja);
            this.stacjaClassFlag_newInstance = true;
        } else if(theObjectClass == this.pasazerClass){
            Pasazer pasazer = new Pasazer();
            pasazer.setInstance(theObject);
            this.objectsInstance.put(Pasazer.class, pasazer);
            this.pasazerClassFlag_newInstance = true;
        }

    }

}
