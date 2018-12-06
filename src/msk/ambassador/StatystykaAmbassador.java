package msk.ambassador;

import hla.rti.EventRetractionHandle;
import hla.rti.LogicalTime;
import hla.rti.ReceivedInteraction;
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
    public boolean promClassFlag_newInstance  = false;
    public boolean promClassFlag_attrsUpdated = false;

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


    public int liczbaPrzeplynietychStacji = 0;
    public int liczbaDodanychPasazerow = 0;
    public int liczbaDodanychSamochodow =0;
    public int liczbaDodanychStacji = 0;
    public int liczbaPasazerowKtorzyWsiedliNaProm = 0;
    public int liczbaSamochodowKtoreWsiadlyNaProm = 0;
    public int liczbaWysiadajacychPasazerow = 0;
    public int liczbaWysiadajacychSamochodow = 0;
    public int liczbaMiejscNaPromie = 0;
    public double czasPrzyplynieciaPromu = 0.0;

    @Override
    public void reflectAttributeValues(int theObject, ReflectedAttributes theAttributes, byte[] tag, LogicalTime theTime, EventRetractionHandle retractionHandle) {
        //w zaleznosci od theObjcet beda ify ktore cos beda robic na gui
        if(this.objects.get(theObject) == this.promClass){

            Prom prom =  getObjectInstances(Prom.class);
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
            this.objectsInstance.replace(Prom.class, prom);
            this.promClassFlag_attrsUpdated = true;

        }else if(this.objects.get(theObject) == this.stacjaClass){

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

        }
        else if(this.objects.get(theObject) == this.pasazerClass){

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
        if(theObjectClass == this.promClass){
            System.out.println("DiscoverObject Prom");
            Prom prom = new Prom();
            prom.setInstance(theObject);
            this.objectsInstance.put(Prom.class,prom);
            this.promClassFlag_newInstance = true;
        }
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
    public void receiveInteraction(int interactionClass, ReceivedInteraction theInteraction, byte[] tag, LogicalTime theTime, EventRetractionHandle eventRetractionHandle) {
        if(interactionClass == this.startSimInteractionHandle){
            this.isReadyToRun = true;
        }else if (interactionClass == this.endSimInteractionHandle){
            System.out.println("------STATYSTYKI KONCOWE-----");
            System.out.println("Czas przyplyniecia promu na pierwsza stacje: "+czasPrzyplynieciaPromu);
            System.out.println("Czas zakonczenia symulacji: "+this.federateTime);
            System.out.println("Liczba miejsc na promie: "+this.liczbaMiejscNaPromie);
            System.out.println("Liczba stacji: "+liczbaDodanychStacji);
            System.out.println("Liczba pasazerow: "+liczbaDodanychPasazerow);
            System.out.println("Liczba samochodow: "+liczbaDodanychSamochodow);
            System.out.println("Szacowana liczba stacji do przewiezienia pasazerow w liczbie : "+liczbaDodanychPasazerow+" oraz samochodow w liczbie"+liczbaDodanychSamochodow+" to: "+liczbaPrzeplynietychStacji);
            System.out.println("Liczba wsiadajacych pasazerow: "+liczbaPasazerowKtorzyWsiedliNaProm);
            System.out.println("Liczba wsiadajacych samochodow: "+liczbaSamochodowKtoreWsiadlyNaProm);
            System.out.println("Liczba wysiadajacych pasazerow: "+liczbaWysiadajacychPasazerow);
            System.out.println("Liczba wysiadajacych samochodow: "+liczbaWysiadajacychSamochodow);
            this.running = false;
        }
    }
}
