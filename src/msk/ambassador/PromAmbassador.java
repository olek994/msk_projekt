package msk.ambassador;

import hla.rti.*;
import hla.rti.jlc.EncodingHelpers;
import msk.BaseAmbassador;
import msk.Objects.Stacja;

public class PromAmbassador extends BaseAmbassador {


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
                    } else if (handle == stacjaAttr_MaxDlugoscKolejki && value != null){
                        stacja.setMaxDlugoscKolejki(EncodingHelpers.decodeInt(value));
                    } else if (handle == stacjaAttr_numerKolejnejStacji && value != null){
                        stacja.setNumerKolejnejStacji(EncodingHelpers.decodeInt(value));
                    } else if (handle == stacjaAttr_LiczbaPasazerow && value != null){
                        stacja.setLiczbaPasazerow(EncodingHelpers.decodeInt(value));
                    } else if (handle == stacjaAttr_LiczbaSamochodow && value != null){
                        stacja.setLiczbaSamochodow(EncodingHelpers.decodeInt(value));
                    } else if (handle == stacjaAttr_PromNaStacji && value != null){
                        stacja.setPromNaStacji(EncodingHelpers.decodeInt(value));
                    }

                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
            this.stacjeObjInstance.replace(numerStacji, stacja);
            this.stacjaClassFlag_attrsUpdated = true;
            this.stacjaOstatnioModyfikowana = numerStacji;

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
    }

    @Override
    public void removeObjectInstance(int theObject, byte[] userSuppliedTag, LogicalTime theTime, EventRetractionHandle retractionHandle) throws ObjectNotKnown, InvalidFederationTime, FederateInternalError {

    }
}
