package msk.ambassador;

import hla.rti.*;
import hla.rti.jlc.EncodingHelpers;
import msk.BaseAmbassador;
import msk.Objects.Pasazer;
import msk.Objects.Stacja;

public class PromAmbassador extends BaseAmbassador {


    public int stacjaClass                     =0;
    public int stacjaAttr_numer                =0;
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
    public int pasazerAttr_naPromie              =0;
    public int pasazerAttr_wysiada               =0;
    public int pasazerNumerStworzenia            =1;
    public int pasazerOstatnioDodany             =0;
    public int pasazerOstatnioModyfikowany       =0;
    public boolean pasazerClassFlag_newInstance  = false;
    public boolean pasazerClassFlag_attrsUpdated = false;


    @Override
    public void reflectAttributeValues(int theObject, ReflectedAttributes theAttributes, byte[] tag, LogicalTime theTime, EventRetractionHandle retractionHandle) {
        if(this.objects.get(theObject) == this.stacjaClass){

            //Wyszukanie ktora stacja zmienila stan
            int numerStacji = 0;
            for(int i = 0;i<theAttributes.size();i++){
                try{
                    int handle = theAttributes.getAttributeHandle(i);
                    byte[] value = theAttributes.getValue(i);

                    if(handle == stacjaAttr_numer && value != null){
                        numerStacji = EncodingHelpers.decodeInt(value);
                    }

                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }

            //modyfikowanie wartosci zmiennych stacji
            Stacja stacja =  getStacjeObjInstances(numerStacji);
            for(int i = 0;i<theAttributes.size();i++){
                try{
                    int handle = theAttributes.getAttributeHandle(i);
                    byte[] value = theAttributes.getValue(i);

                    if(handle == stacjaAttr_numer && value != null){
                        stacja.setNumer(EncodingHelpers.decodeInt(value));
                    }

                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
            this.stacjeObjInstance.replace(numerStacji, stacja);
            this.stacjaClassFlag_attrsUpdated = true;
            this.stacjaOstatnioModyfikowana = numerStacji;

        }else if(this.objects.get(theObject) == this.pasazerClass){

            //Wyszukanie ktora stacja zmienila stan
            int idPasazera = 0;
            for(int i = 0;i<theAttributes.size();i++){
                try{
                    int handle = theAttributes.getAttributeHandle(i);
                    byte[] value = theAttributes.getValue(i);

                    if(handle == pasazerAttr_id && value != null){
                        idPasazera = EncodingHelpers.decodeInt(value);
                    }

                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }

            //modyfikowanie wartosci zmiennych stacji
            Pasazer pasazer =  getPasazerObjInstances(idPasazera);
            for(int i = 0;i<theAttributes.size();i++){
                try{
                    int handle = theAttributes.getAttributeHandle(i);
                    byte[] value = theAttributes.getValue(i);

                    if(handle == pasazerAttr_id && value != null){
                        pasazer.setId(EncodingHelpers.decodeInt(value));
                    } else if (handle == pasazerAttr_numerStacji && value != null){
                        pasazer.setNumerStacji(EncodingHelpers.decodeInt(value));
                    } else if (handle == pasazerAttr_typ && value != null){
                        pasazer.setTyp(EncodingHelpers.decodeInt(value));
                    } else if (handle == pasazerAttr_stacjaDocelowa && value != null){
                        pasazer.setStacjaDocelowa(EncodingHelpers.decodeInt(value));
                    }else if(handle == pasazerAttr_naPromie && value != null){
                        pasazer.setNaPromie(EncodingHelpers.decodeInt(value));
                    }else if(handle == pasazerAttr_wysiada && value != null){
                        pasazer.setWysiada(EncodingHelpers.decodeInt(value));
                    }

                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
            this.pasazerObjInstance.replace(idPasazera, pasazer);
            this.pasazerClassFlag_attrsUpdated = true;
            this.pasazerOstatnioModyfikowany = idPasazera;

        }
    }

    @Override
    public void discoverObjectInstance(int theObject, int theObjectClass, String objectName) {
        super.discoverObjectInstance(theObject, theObjectClass, objectName);
        if(theObjectClass == this.stacjaClass){
            System.out.println("DiscoverObject Stacja");
            Stacja stacja = new Stacja();
            stacja.setInstance(theObject);
            this.stacjeObjInstance.put(stacjaNumerStworzenia,stacja);
            this.stacjaClassFlag_newInstance = true;
            this.stacjaOstatnioDodana = stacjaNumerStworzenia;
            stacjaNumerStworzenia++;
        }
        if(theObjectClass == this.pasazerClass){
            System.out.println("DiscoverObject Pasazer");
            Pasazer pasazer = new Pasazer();
            pasazer.setInstance(theObject);
            this.pasazerObjInstance.put(pasazerNumerStworzenia,pasazer);
            this.pasazerClassFlag_newInstance = true;
            this.pasazerOstatnioDodany = pasazerNumerStworzenia;
            pasazerNumerStworzenia++;
        }
    }

    @Override
    public void removeObjectInstance(int theObject, byte[] userSuppliedTag, LogicalTime theTime, EventRetractionHandle retractionHandle) throws ObjectNotKnown, InvalidFederationTime, FederateInternalError {

    }
}
