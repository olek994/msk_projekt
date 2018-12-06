package msk.federate;

import hla.rti.AttributeHandleSet;
import hla.rti.AttributeNotDefined;
import hla.rti.AttributeNotOwned;
import hla.rti.ConcurrentAccessAttempted;
import hla.rti.FederateNotExecutionMember;
import hla.rti.InvalidFederationTime;
import hla.rti.NameNotFound;
import hla.rti.ObjectClassNotDefined;
import hla.rti.ObjectNotKnown;
import hla.rti.RTIexception;
import hla.rti.RTIinternalError;
import hla.rti.RestoreInProgress;
import hla.rti.SaveInProgress;
import hla.rti.SuppliedAttributes;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;
import msk.BaseFederate;
import msk.Objects.Prom;
import msk.ambassador.PasazerAmbassador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PasazerFederate extends BaseFederate<PasazerAmbassador> {

    private static final int LICZBA_PASAZEROW = 5;
    private static final int LICZBA_SAMOCHODOW = 2;

    private Map<Integer, Integer> max_liczba_pasazerow;
    private Map<Integer, Integer> max_liczba_samochodow;

    private Map<Integer, List<Integer>> pasazerowieNastacji;
    private Map<Integer, List<Integer>> samochodyNaStacji;

    private Map<Integer, Integer> stacjaDocelowaPasazera; //klucz to idpasazera value to stacja docelowa

    private Map<Integer, List<Integer>> pasazerowieNaPromie; //Klucz to stacja docelowa

    private List<Integer> pasazerList;
    private int waitToaddNewPassanger = 0;
    private int indexOfNewPassanger = 1;
    private Random random;
    private int poprzedniaStacjaPromu = -1;
    private int oczekiwanieNaPojscieNaProm = 0;
    private int wychodzeniePasazerow = 0;

    @Override
    protected void init() throws Exception {
        pasazerList = new ArrayList<>();
        random = new Random();
        max_liczba_pasazerow = new HashMap<>();
        max_liczba_samochodow = new HashMap<>();
        pasazerowieNastacji = new HashMap<>();
        samochodyNaStacji = new HashMap<>();
        pasazerowieNaPromie = new HashMap<>();
        stacjaDocelowaPasazera = new HashMap<>();
        for (int i = 1; i <= 6; i++) {
            max_liczba_pasazerow.put(i, 5);
            max_liczba_samochodow.put(i, 3);
            pasazerowieNastacji.put(i, new LinkedList<>());
            samochodyNaStacji.put(i, new LinkedList<>());
            pasazerowieNaPromie.put(i, new LinkedList<>());
        }

    }


    @Override
    protected void update(double timeToAdvance) throws Exception {
        int numerStacji;
        int stacjaDocelowa = random.nextInt(6) + 1;
        int typ;
        if (waitToaddNewPassanger == 5) {
            numerStacji = random.nextInt(6) + 1;
            while (stacjaDocelowa == numerStacji) {
                stacjaDocelowa = random.nextInt(6) + 1;
            }
            typ = random.nextInt(2) + 1;
            int nowyPasazer;
            if (typ == 1) {
                if (max_liczba_pasazerow.get(numerStacji) > 0) {
                    nowyPasazer = createObject("Pasazer");
                    pasazerList.add(nowyPasazer);
                    pasazerowieNastacji.get(numerStacji).add(pasazerList.size() - 1);
                    update_PasazerAttr(indexOfNewPassanger, numerStacji, typ, stacjaDocelowa, 0, 0,timeToAdvance, indexOfNewPassanger - 1);
                    max_liczba_pasazerow.replace(numerStacji, max_liczba_pasazerow.get(numerStacji) - 1);
                    stacjaDocelowaPasazera.put(indexOfNewPassanger, stacjaDocelowa);

                    indexOfNewPassanger++;
                }
            } else if (typ == 2) {
                if (max_liczba_samochodow.get(numerStacji) > 0) {
                    nowyPasazer = createObject("Pasazer");
                    pasazerList.add(nowyPasazer);
                    samochodyNaStacji.get(numerStacji).add(pasazerList.size() - 1);
                    update_PasazerAttr(indexOfNewPassanger, numerStacji, typ, stacjaDocelowa, 0,0, timeToAdvance, indexOfNewPassanger - 1);
                    max_liczba_samochodow.replace(numerStacji, max_liczba_samochodow.get(numerStacji) - 1);

                    stacjaDocelowaPasazera.put(indexOfNewPassanger, stacjaDocelowa);

                    indexOfNewPassanger++;
                }
            }
            waitToaddNewPassanger = 0;
        }

        waitToaddNewPassanger++;

        //PROM//
        if (this.federationAmbassador.promClassFlag_newInstance) {
            Prom prom = this.federationAmbassador.getObjectInstances(Prom.class);
            this.federationAmbassador.promClassFlag_newInstance = false;

            System.out.println("Wykryto PROM PRZEZ PASAZERA");
        }
        if (this.federationAmbassador.promClassFlag_attrsUpdated) {
            Prom prom = this.federationAmbassador.getObjectInstances(Prom.class);
            this.federationAmbassador.promClassFlag_attrsUpdated = false;
            if (prom.getNumerStacji() != poprzedniaStacjaPromu) {
                //prom jest na nowej stacji
                System.out.println("Prom jest na nowej stacji: " + prom.getNumerStacji());
                poprzedniaStacjaPromu = prom.getNumerStacji();
                oczekiwanieNaPojscieNaProm = 0;

            } else {
                //prom jest na tej samej stacji czyli pobrał pasazera
            }

        }

        Prom prom = this.federationAmbassador.getObjectInstances(Prom.class);

        if (prom != null) {

            if(prom.getNumerStacji() == poprzedniaStacjaPromu && wychodzeniePasazerow == 7){
                List<Integer> pasazerowie = pasazerowieNaPromie.get(prom.getNumerStacji());
                if(pasazerowie.size() > 0){
                    System.out.println("PASAZER: "+pasazerowie.get(pasazerowie.size()-1)+", wysiada");
                    update_PasazerAttr_Wysiada(pasazerowie.get(pasazerowie.size()-1),1,timeToAdvance,pasazerowie.get(pasazerowie.size()-1)-1);
                    pasazerowie.remove(pasazerowie.size()-1);
                    pasazerowieNaPromie.replace(prom.getNumerStacji(),pasazerowie);
                }
                wychodzeniePasazerow = 0;
            }
            wychodzeniePasazerow++;

            if (prom.getNumerStacji() == poprzedniaStacjaPromu && oczekiwanieNaPojscieNaProm == 15 && prom.getLiczbaWolnychMiejsc() > 0) {
                List<Integer> pasazerowie = pasazerowieNastacji.get(prom.getNumerStacji());
                List<Integer> samochody = samochodyNaStacji.get(prom.getNumerStacji());

                if(samochody.size() == 0 && pasazerowie.size() > 0){ //jezeli nie ma samochodow to bierz pasazera
                    update_PasazerAttr_NaPromie(pasazerowie.get(pasazerowie.size() - 1) + 1, 1, timeToAdvance, pasazerowie.get(pasazerowie.size() - 1));
                    System.out.println("Pasazer: " + (pasazerowie.get(pasazerowie.size() - 1) + 1));

                    pasazerowieNaPromie.get(stacjaDocelowaPasazera.get((pasazerowie.get(pasazerowie.size() - 1) + 1))).add((pasazerowie.get(pasazerowie.size() - 1) + 1));

                    pasazerowie.remove(pasazerowie.size() - 1);
                    pasazerowieNastacji.replace(prom.getNumerStacji(), pasazerowie);
                }else if(pasazerowie.size() == 0 && samochody.size() > 0){
                    update_PasazerAttr_NaPromie(samochody.get(samochody.size()-1)+1,1,timeToAdvance,samochody.get(samochody.size()-1));
                    System.out.println("Samochod: "+(samochody.get(samochody.size()-1)+1));

                    pasazerowieNaPromie.get( stacjaDocelowaPasazera.get((samochody.get(samochody.size()-1)+1))).add((samochody.get(samochody.size()-1)+1));

                    samochody.remove(samochody.size()-1);
                    samochodyNaStacji.replace(prom.getNumerStacji(),samochody);
                }else if(samochody.size() > 0 && pasazerowie.size() > 0){
                    int losowyWybor = random.nextInt(2) + 1;
                    if (losowyWybor == 1) {
                        if (pasazerowie.size() > 0) {
                            update_PasazerAttr_NaPromie(pasazerowie.get(pasazerowie.size() - 1) + 1, 1, timeToAdvance, pasazerowie.get(pasazerowie.size() - 1));
                            System.out.println("Pasazer: " + (pasazerowie.get(pasazerowie.size() - 1) + 1));

                            pasazerowieNaPromie.get(stacjaDocelowaPasazera.get((pasazerowie.get(pasazerowie.size() - 1) + 1))).add((pasazerowie.get(pasazerowie.size() - 1) + 1));

                            pasazerowie.remove(pasazerowie.size() - 1);
                            pasazerowieNastacji.replace(prom.getNumerStacji(), pasazerowie);
                        }
                    } else if (losowyWybor == 2) {
                        if(samochody.size() > 0){
                            update_PasazerAttr_NaPromie(samochody.get(samochody.size()-1)+1,1,timeToAdvance,samochody.get(samochody.size()-1));
                            System.out.println("Samochod: "+(samochody.get(samochody.size()-1)+1));

                            pasazerowieNaPromie.get( stacjaDocelowaPasazera.get((samochody.get(samochody.size()-1)+1))).add((samochody.get(samochody.size()-1)+1));

                            samochody.remove(samochody.size()-1);
                            samochodyNaStacji.replace(prom.getNumerStacji(),samochody);
                        }
                    }
                }
        //TODO SPRAWDZIC CZY WSZCYSCY PASAZEROWIE PISZA SIE W GUI
                oczekiwanieNaPojscieNaProm = 0;
            }
            oczekiwanieNaPojscieNaProm++;

        }


    }


    @Override
    protected void publishAndSubscribe() throws RTIexception {

        int pasazerClass = rtiamb.getObjectClassHandle("ObjectRoot.Pasazer");
        int pasazerAttr_ID = rtiamb.getAttributeHandle("id", pasazerClass);
        int pasazerAttr_typ = rtiamb.getAttributeHandle("typ", pasazerClass);
        int pasazerAttr_numerStacji = rtiamb.getAttributeHandle("numerStacji", pasazerClass);
        int pasazerAttr_stacjaDocelowa = rtiamb.getAttributeHandle("stacjaDocelowa", pasazerClass);
        int pasazerAttr_naPromie = rtiamb.getAttributeHandle("naPromie", pasazerClass);
        int pasazerAttr_Wysiada = rtiamb.getAttributeHandle("wysiada", pasazerClass);

        AttributeHandleSet attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add(pasazerAttr_ID);
        attributes.add(pasazerAttr_numerStacji);
        attributes.add(pasazerAttr_typ);
        attributes.add(pasazerAttr_stacjaDocelowa);
        attributes.add(pasazerAttr_naPromie);
        attributes.add(pasazerAttr_Wysiada);

        rtiamb.publishObjectClass(pasazerClass, attributes);

        //-----PROM----//
        this.federationAmbassador.promClass = rtiamb.getObjectClassHandle("ObjectRoot.Prom");
        this.federationAmbassador.promAttr_liczbaWolnychMiejsc = rtiamb.getAttributeHandle("liczbaWolnychMiejsc", this.federationAmbassador.promClass);
        this.federationAmbassador.promAttr_numerStacji = rtiamb.getAttributeHandle("numerStacji", this.federationAmbassador.promClass);

        attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add(this.federationAmbassador.promAttr_liczbaWolnychMiejsc);
        attributes.add(this.federationAmbassador.promAttr_numerStacji);

        rtiamb.subscribeObjectClassAttributes(this.federationAmbassador.promClass, attributes);

    }


    private void update_PasazerAttr(int idPasazer, int numerStacji, int typ, int stacjaDocelowa, int naPromie,int wysiada, double time, int idx) throws ObjectClassNotDefined, RTIinternalError, NameNotFound, FederateNotExecutionMember, SaveInProgress, RestoreInProgress, AttributeNotOwned, ObjectNotKnown, AttributeNotDefined, InvalidFederationTime, ConcurrentAccessAttempted {
        if (idx > pasazerList.size() - 1) {
            return;
        }
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int obj = this.pasazerList.get(idx);
        int classHandle = rtiamb.getObjectClass(obj);
        int attrIdHandle = rtiamb.getAttributeHandle("id", classHandle);
        int attrNumerStacjiHandle = rtiamb.getAttributeHandle("numerStacji", classHandle);
        int attrTypHandle = rtiamb.getAttributeHandle("typ", classHandle);
        int attrStacjaDocelowaHandle = rtiamb.getAttributeHandle("stacjaDocelowa", classHandle);
        int attrNaPromieHandle = rtiamb.getAttributeHandle("naPromie", classHandle);
        int attrWysiadaHandle = rtiamb.getAttributeHandle("wysiada", classHandle);

        attributes.add(attrIdHandle, EncodingHelpers.encodeInt(idPasazer));
        attributes.add(attrNumerStacjiHandle, EncodingHelpers.encodeInt(numerStacji));
        attributes.add(attrTypHandle, EncodingHelpers.encodeInt(typ));
        attributes.add(attrStacjaDocelowaHandle, EncodingHelpers.encodeInt(stacjaDocelowa));
        attributes.add(attrNaPromieHandle, EncodingHelpers.encodeInt(naPromie));
        attributes.add(attrWysiadaHandle, EncodingHelpers.encodeInt(wysiada));
        rtiamb.updateAttributeValues(obj, attributes, generateTag(), convertTime(time));
    }

    private void update_PasazerAttr_NumerStacji(int idPasazer, int numerStacji, double time, int idx) throws ObjectNotKnown, FederateNotExecutionMember, RTIinternalError, NameNotFound, ObjectClassNotDefined, RestoreInProgress, AttributeNotOwned, AttributeNotDefined, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted {
        if (idx > pasazerList.size() - 1) {
            return;
        }
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int obj = this.pasazerList.get(idx);
        int classHandle = rtiamb.getObjectClass(obj);
        int attrIdHandle = rtiamb.getAttributeHandle("id", classHandle);
        int attrNumerStacjiHandle = rtiamb.getAttributeHandle("numerStacji", classHandle);
        attributes.add(attrIdHandle, EncodingHelpers.encodeInt(idPasazer));
        attributes.add(attrNumerStacjiHandle, EncodingHelpers.encodeInt(numerStacji));
        rtiamb.updateAttributeValues(obj, attributes, generateTag(), convertTime(time));
    }

    private void update_PasazerAttr_NaPromie(int idPasazer, int naPromie, double time, int idx) throws ObjectNotKnown, FederateNotExecutionMember, RTIinternalError, NameNotFound, ObjectClassNotDefined, RestoreInProgress, AttributeNotOwned, AttributeNotDefined, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted {
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int obj = this.pasazerList.get(idx);
        int classHandle = rtiamb.getObjectClass(obj);
        int attrIdHandle = rtiamb.getAttributeHandle("id", classHandle);
        int attrNaPromieHandle = rtiamb.getAttributeHandle("naPromie", classHandle);
        attributes.add(attrIdHandle, EncodingHelpers.encodeInt(idPasazer));
        attributes.add(attrNaPromieHandle, EncodingHelpers.encodeInt(naPromie));
        rtiamb.updateAttributeValues(obj, attributes, generateTag(), convertTime(time));
    }

    private void update_PasazerAttr_Wysiada(int idPasazer, int wysiada, double time, int idx) throws ObjectNotKnown, FederateNotExecutionMember, RTIinternalError, NameNotFound, ObjectClassNotDefined, RestoreInProgress, AttributeNotOwned, AttributeNotDefined, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted {
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int obj = this.pasazerList.get(idx);
        int classHandle = rtiamb.getObjectClass(obj);
        int attrIdHandle = rtiamb.getAttributeHandle("id", classHandle);
        int attrWysiadaHandle = rtiamb.getAttributeHandle("wysiada", classHandle);
        attributes.add(attrIdHandle, EncodingHelpers.encodeInt(idPasazer));
        attributes.add(attrWysiadaHandle, EncodingHelpers.encodeInt(wysiada));
        rtiamb.updateAttributeValues(obj, attributes, generateTag(), convertTime(time));
    }


    @Override
    protected void deleteObjectsAndInteractions() throws Exception {
        for (Integer pasazer : pasazerList) {
            this.deleteObject(pasazer);
        }
    }


    public static void main(String[] agrs) {
        try {
            new PasazerFederate().runFederate("Pasazer");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
