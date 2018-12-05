package msk.federate;

import hla.rti.AttributeHandleSet;
import hla.rti.ConcurrentAccessAttempted;
import hla.rti.FederateNotExecutionMember;
import hla.rti.InteractionClassNotDefined;
import hla.rti.InteractionClassNotPublished;
import hla.rti.InteractionParameterNotDefined;
import hla.rti.InvalidFederationTime;
import hla.rti.LogicalTime;
import hla.rti.NameNotFound;
import hla.rti.RTIexception;
import hla.rti.RTIinternalError;
import hla.rti.RestoreInProgress;
import hla.rti.SaveInProgress;
import hla.rti.SuppliedParameters;
import hla.rti.jlc.RtiFactoryFactory;
import javafx.application.Platform;
import javafx.scene.image.Image;
import msk.BaseFederate;
import msk.GuiApplication;
import msk.Objects.Pasazer;
import msk.Objects.Prom;
import msk.Objects.Stacja;
import msk.ambassador.GuiAmbassador;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aleksander Małkowicz, Date: 13.09.2018
 * Copyright by RelayOnIT, Warszawa 2018
 */
public class GuiFederate extends BaseFederate<GuiAmbassador> {

    private int sendStartInteraction = 0;
    private boolean stopSim = false;
    private Map<Integer, Integer> liczbaPasazerowNaStacji;
    private Map<Integer, Integer> liczbaSamochodowNaStacji;
    private GuiApplication guiApplication;

    public GuiFederate(GuiApplication guiApplication) {
        this.guiApplication = guiApplication;
    }

    @Override
    protected void init() throws Exception {
        liczbaPasazerowNaStacji = new HashMap<>();
        liczbaSamochodowNaStacji = new HashMap<>();

        liczbaPasazerowNaStacji.put(1,0);
        liczbaPasazerowNaStacji.put(2,0);
        liczbaPasazerowNaStacji.put(3,0);
        liczbaPasazerowNaStacji.put(4,0);
        liczbaPasazerowNaStacji.put(5,0);
        liczbaPasazerowNaStacji.put(6,0);
        liczbaSamochodowNaStacji.put(1,0);
        liczbaSamochodowNaStacji.put(2,0);
        liczbaSamochodowNaStacji.put(3,0);
        liczbaSamochodowNaStacji.put(4,0);
        liczbaSamochodowNaStacji.put(5,0);
        liczbaSamochodowNaStacji.put(6,0);
    }

    public void stopSimulation() {
        this.stopSim = true;
    }

    @Override
    protected void waitForStart() throws Exception {

    }

    @Override
    protected void deleteObjectsAndInteractions() {

    }

    @Override
    protected void update(double timeToAdvance) {
        Platform.runLater(() -> {
            this.guiApplication.simTime.setText(this.federationAmbassador.federateTime + "");
        });

        //co okreslony czas (tick) sprawdzane sa czy jakis obiekt sie pojawil
        // czy moze zostal zaktualizowany i modyfikowane jest gui



        //--PROM--//
        if (this.federationAmbassador.promClassFlag_newInstance) {
            Prom prom = this.federationAmbassador.getObjectInstances(Prom.class);
            this.federationAmbassador.promClassFlag_newInstance = false;

            System.out.println("DODANO PROM DO GUI");
        }
        if (this.federationAmbassador.promClassFlag_attrsUpdated) {
            Prom prom = this.federationAmbassador.getObjectInstances(Prom.class);
            this.federationAmbassador.promClassFlag_attrsUpdated = false;

            System.out.println("LICZBA MIEJSC: " + prom.getLiczbaWolnychMiejsc());

            System.out.println("ZAKTUALIZOWANO PARAMETRY PROMU: " + prom.getNumerStacji());


            Image imageOn;
            Image imageOf;
            switch (prom.getNumerStacji()) {
                case 1:
                    imageOf = new Image("/stacja_prawa_off.png");
                    Platform.runLater(() -> {
                        this.guiApplication.imageDrugaStacja.setImage(imageOf);
                    });
                    imageOn = new Image("/stacja_lewa_on.png");
                    Platform.runLater(() -> {
                        this.guiApplication.imagePierwszaStacja.setImage(imageOn);
                    });
                    break;
                case 2:

                    imageOf = new Image("/stacja_lewa_off.png");
                    Platform.runLater(() -> {
                        this.guiApplication.imageTrzeciaStacja.setImage(imageOf);
                        this.guiApplication.imagePierwszaStacja.setImage(imageOf);
                    });
                    imageOn = new Image("/stacja_prawa_on.png");
                    Platform.runLater(() -> {
                        this.guiApplication.imageDrugaStacja.setImage(imageOn);
                    });
                    break;
                case 3:

                    imageOf = new Image("/stacja_prawa_off.png");
                    Platform.runLater(() -> {
                        this.guiApplication.imageDrugaStacja.setImage(imageOf);
                        this.guiApplication.imageCzwartaStacja.setImage(imageOf);
                    });
                    imageOn = new Image("/stacja_lewa_on.png");
                    Platform.runLater(() -> {
                        this.guiApplication.imageTrzeciaStacja.setImage(imageOn);
                    });
                    break;
                case 4:

                    imageOf = new Image("/stacja_lewa_off.png");
                    Platform.runLater(() -> {
                        this.guiApplication.imageTrzeciaStacja.setImage(imageOf);
                        this.guiApplication.imagePiataStacja.setImage(imageOf);
                    });
                    imageOn = new Image("/stacja_prawa_on.png");
                    Platform.runLater(() -> {
                        this.guiApplication.imageCzwartaStacja.setImage(imageOn);
                    });
                    break;
                case 5:
                    imageOf = new Image("/stacja_prawa_off.png");
                    Platform.runLater(() -> {
                        this.guiApplication.imageCzwartaStacja.setImage(imageOf);
                        this.guiApplication.imageSzostaStacja.setImage(imageOf);
                    });
                    imageOn = new Image("/stacja_lewa_on.png");
                    Platform.runLater(() -> {
                        this.guiApplication.imagePiataStacja.setImage(imageOn);
                    });
                    break;
                case 6:

                    imageOf = new Image("/stacja_lewa_off.png");
                    Platform.runLater(() -> {
                        this.guiApplication.imagePiataStacja.setImage(imageOf);
                    });
                    imageOn = new Image("/stacja_prawa_on.png");
                    Platform.runLater(() -> {
                        this.guiApplication.imageSzostaStacja.setImage(imageOn);
                    });
                    break;
            }

        }

        Prom prom = this.federationAmbassador.getObjectInstances(Prom.class);
        if(prom != null){
            Platform.runLater(() -> {
                this.guiApplication.liczbaWolnychMiejscNaPromie.setText(prom.getLiczbaWolnychMiejsc()+ "");
            });
        }

        //--STACJA--//
        if (this.federationAmbassador.stacjaClassFlag_newInstance) {
            Stacja stacja = this.federationAmbassador.getStacjeObjInstances(this.federationAmbassador.stacjaOstatnioDodana);
            this.federationAmbassador.stacjaClassFlag_newInstance = false;
            this.federationAmbassador.stacjaOstatnioDodana = 0;

            System.out.println("DODANO Stacje DO GUI: " + stacja.getNumer());
        }
        if (this.federationAmbassador.stacjaClassFlag_attrsUpdated) {
            Stacja stacja = this.federationAmbassador.getStacjeObjInstances(this.federationAmbassador.stacjaOstatnioModyfikowana);
            this.federationAmbassador.stacjaClassFlag_attrsUpdated = false;
            this.federationAmbassador.stacjaOstatnioModyfikowana = 0;

            System.out.println("ZAKTUALIZOWANO PARAMETRY Stacji: " + stacja.getNumer() );



        }


        //--PASAZER--//
        if (this.federationAmbassador.pasazerClassFlag_newInstance) {
            Pasazer pasazer = this.federationAmbassador.getPasazerObjInstances(this.federationAmbassador.pasazerOstatnioDodany);
            this.federationAmbassador.pasazerClassFlag_newInstance = false;
            this.federationAmbassador.pasazerOstatnioDodany = 0;

            System.out.println("DODANO Pasazera DO GUI: " + pasazer.getId());
        }
        if (this.federationAmbassador.pasazerClassFlag_attrsUpdated) {
            Pasazer pasazer = this.federationAmbassador.getPasazerObjInstances(this.federationAmbassador.pasazerOstatnioModyfikowany);
            this.federationAmbassador.pasazerClassFlag_attrsUpdated = false;
            this.federationAmbassador.pasazerOstatnioModyfikowany = 0;

            System.out.println("ZAKTUALIZOWANO PARAMETRY Pasazera: " + pasazer.getId()+" Na promie "+pasazer.getNaPromie());

            System.out.println("PASAZER PROM: "+pasazer.getNaPromie()+", WYSIADA: "+pasazer.getWysiada());
            if(pasazer.getNaPromie() == 0){
                int pasazerowie;
                int samochody;
                switch (pasazer.getNumerStacji()) {
                    case 1:
                        pasazerowie = liczbaPasazerowNaStacji.get(1);
                        samochody = liczbaSamochodowNaStacji.get(1);
                        if(pasazer.getTyp() == 1){
                            liczbaPasazerowNaStacji.put(1,pasazerowie+1);
                        }else if(pasazer.getTyp() == 2){
                            liczbaSamochodowNaStacji.put(1,samochody+1);
                        }
                        Platform.runLater(() -> {
                            this.guiApplication.liczbaPasazerowPierwszej.setText(liczbaPasazerowNaStacji.get(1) + "");
                            this.guiApplication.liczbaSamochodowPierwszej.setText(liczbaSamochodowNaStacji.get(1) + "");
                        });

                        break;
                    case 2:
                        pasazerowie = liczbaPasazerowNaStacji.get(2);
                        samochody = liczbaSamochodowNaStacji.get(2);
                        if(pasazer.getTyp() == 1){
                            liczbaPasazerowNaStacji.put(2,pasazerowie+1);
                        }else if(pasazer.getTyp() == 2){
                            liczbaSamochodowNaStacji.put(2,samochody+1);
                        }
                        Platform.runLater(() -> {
                            this.guiApplication.liczbaPasazerowDrugiej.setText(liczbaPasazerowNaStacji.get(2) + "");
                            this.guiApplication.liczbaSamochodowDrugiej.setText(liczbaSamochodowNaStacji.get(2) + "");
                        });
                        break;
                    case 3:
                        pasazerowie = liczbaPasazerowNaStacji.get(3);
                        samochody = liczbaSamochodowNaStacji.get(3);
                        if(pasazer.getTyp() == 1){
                            liczbaPasazerowNaStacji.put(3,pasazerowie+1);
                        }else if(pasazer.getTyp() == 2){
                            liczbaSamochodowNaStacji.put(3,samochody+1);
                        }
                        Platform.runLater(() -> {
                            this.guiApplication.liczbaPasazerowTrzeciej.setText(liczbaPasazerowNaStacji.get(3) + "");
                            this.guiApplication.liczbaSamochodowTrzeciej.setText(liczbaSamochodowNaStacji.get(3) + "");
                        });
                        break;
                    case 4:
                        pasazerowie = liczbaPasazerowNaStacji.get(4);
                        samochody = liczbaSamochodowNaStacji.get(4);
                        if(pasazer.getTyp() == 1){
                            liczbaPasazerowNaStacji.put(4,pasazerowie+1);
                        }else if(pasazer.getTyp() == 2){
                            liczbaSamochodowNaStacji.put(4,samochody+1);
                        }
                        Platform.runLater(() -> {
                            this.guiApplication.liczbaPasazerowCzwartej.setText(liczbaPasazerowNaStacji.get(4) + "");
                            this.guiApplication.liczbaSamochodowCzwartej.setText(liczbaSamochodowNaStacji.get(4) + "");
                        });
                        break;
                    case 5:
                        pasazerowie = liczbaPasazerowNaStacji.get(5);
                        samochody = liczbaSamochodowNaStacji.get(5);
                        if(pasazer.getTyp() == 1){
                            liczbaPasazerowNaStacji.put(5,pasazerowie+1);
                        }else if(pasazer.getTyp() == 2){
                            liczbaSamochodowNaStacji.put(5,samochody+1);
                        }
                        Platform.runLater(() -> {
                            this.guiApplication.liczbaPasazerowPiatej.setText(liczbaPasazerowNaStacji.get(5) + "");
                            this.guiApplication.liczbaSamochodowPiatej.setText(liczbaSamochodowNaStacji.get(5) + "");
                        });
                        break;
                    case 6:
                        pasazerowie = liczbaPasazerowNaStacji.get(6);
                        samochody = liczbaSamochodowNaStacji.get(6);
                        if(pasazer.getTyp() == 1){
                            liczbaPasazerowNaStacji.put(6,pasazerowie+1);
                        }else if(pasazer.getTyp() == 2){
                            liczbaSamochodowNaStacji.put(6,samochody+1);
                        }
                        Platform.runLater(() -> {
                            this.guiApplication.liczbaPasazerowSzostej.setText(liczbaPasazerowNaStacji.get(6) + "");
                            this.guiApplication.liczbaSamochodowSzostej.setText(liczbaSamochodowNaStacji.get(6) + "");
                        });
                        break;
                }
            } else if(pasazer.getNaPromie() == 1 && pasazer.getWysiada() == 0){
                System.out.println("PASAZER WSZEDL NA PROM");
                int pasazerowie;
                int samochody;
                switch (pasazer.getNumerStacji()) {
                    case 1:
                        pasazerowie = liczbaPasazerowNaStacji.get(1);
                        samochody = liczbaSamochodowNaStacji.get(1);
                        if(pasazer.getTyp() == 1){
                            if(pasazerowie > 0){
                                liczbaPasazerowNaStacji.put(1,pasazerowie-1);
                            }
                        }else if(pasazer.getTyp() == 2){
                            if(samochody > 0){
                                liczbaSamochodowNaStacji.put(1,samochody-1);
                            }
                        }
                        Platform.runLater(() -> {
                            this.guiApplication.liczbaPasazerowPierwszej.setText(liczbaPasazerowNaStacji.get(1) + "");
                            this.guiApplication.liczbaSamochodowPierwszej.setText(liczbaSamochodowNaStacji.get(1) + "");
                        });

                        break;
                    case 2:
                        pasazerowie = liczbaPasazerowNaStacji.get(2);
                        samochody = liczbaSamochodowNaStacji.get(2);
                        if(pasazer.getTyp() == 1){
                            if(pasazerowie > 0){
                                liczbaPasazerowNaStacji.put(2,pasazerowie-1);
                            }
                        }else if(pasazer.getTyp() == 2){
                            if(samochody > 0){
                                liczbaSamochodowNaStacji.put(2,samochody-1);
                            }
                        }
                        Platform.runLater(() -> {
                            this.guiApplication.liczbaPasazerowDrugiej.setText(liczbaPasazerowNaStacji.get(2) + "");
                            this.guiApplication.liczbaSamochodowDrugiej.setText(liczbaSamochodowNaStacji.get(2) + "");
                        });
                        break;
                    case 3:
                        pasazerowie = liczbaPasazerowNaStacji.get(3);
                        samochody = liczbaSamochodowNaStacji.get(3);
                        if(pasazer.getTyp() == 1){
                            if(pasazerowie > 0){
                                liczbaPasazerowNaStacji.put(3,pasazerowie-1);
                            }
                        }else if(pasazer.getTyp() == 2){
                            if(samochody > 0){
                                liczbaSamochodowNaStacji.put(3,samochody-1);
                            }
                        }
                        Platform.runLater(() -> {
                            this.guiApplication.liczbaPasazerowTrzeciej.setText(liczbaPasazerowNaStacji.get(3) + "");
                            this.guiApplication.liczbaSamochodowTrzeciej.setText(liczbaSamochodowNaStacji.get(3) + "");
                        });
                        break;
                    case 4:
                        pasazerowie = liczbaPasazerowNaStacji.get(4);
                        samochody = liczbaSamochodowNaStacji.get(4);
                        if(pasazer.getTyp() == 1){
                            if(pasazerowie > 0){
                                liczbaPasazerowNaStacji.put(4,pasazerowie-1);
                            }
                        }else if(pasazer.getTyp() == 2){
                            if(samochody > 0){
                                liczbaSamochodowNaStacji.put(4,samochody-1);
                            }
                        }
                        Platform.runLater(() -> {
                            this.guiApplication.liczbaPasazerowCzwartej.setText(liczbaPasazerowNaStacji.get(4) + "");
                            this.guiApplication.liczbaSamochodowCzwartej.setText(liczbaSamochodowNaStacji.get(4) + "");
                        });
                        break;
                    case 5:
                        pasazerowie = liczbaPasazerowNaStacji.get(5);
                        samochody = liczbaSamochodowNaStacji.get(5);
                        if(pasazer.getTyp() == 1){
                            if(pasazerowie > 0){
                                liczbaPasazerowNaStacji.put(5,pasazerowie-1);
                            }
                        }else if(pasazer.getTyp() == 2){
                            if(samochody > 0){
                                liczbaSamochodowNaStacji.put(5,samochody-1);
                            }
                        }
                        Platform.runLater(() -> {
                            this.guiApplication.liczbaPasazerowPiatej.setText(liczbaPasazerowNaStacji.get(5) + "");
                            this.guiApplication.liczbaSamochodowPiatej.setText(liczbaSamochodowNaStacji.get(5) + "");
                        });
                        break;
                    case 6:
                        pasazerowie = liczbaPasazerowNaStacji.get(6);
                        samochody = liczbaSamochodowNaStacji.get(6);
                        if(pasazer.getTyp() == 1){
                            if(pasazerowie > 0){
                                liczbaPasazerowNaStacji.put(6,pasazerowie-1);
                            }
                        }else if(pasazer.getTyp() == 2){
                            if(samochody > 0){
                                liczbaSamochodowNaStacji.put(6,samochody-1);
                            }
                        }
                        Platform.runLater(() -> {
                            this.guiApplication.liczbaPasazerowSzostej.setText(liczbaPasazerowNaStacji.get(6) + "");
                            this.guiApplication.liczbaSamochodowSzostej.setText(liczbaSamochodowNaStacji.get(6) + "");
                        });
                        break;
                }
            }



        }

        if ((sendStartInteraction++) % 100 == 0) {
            sendStartInteraction(timeToAdvance);
        }

        if (stopSim) {
            sendStopInteraction(timeToAdvance);
            federationAmbassador.running = false;
        }


    }


    @Override
    protected void publishAndSubscribe() throws RTIexception {
        int startSimulation = rtiamb.getInteractionClassHandle("InteractionRoot.PoczatekSymulacji");
        rtiamb.publishInteractionClass(startSimulation);

        int stopSimulation = rtiamb.getInteractionClassHandle("InteractionRoot.KoniecSymulacji");
        rtiamb.publishInteractionClass(stopSimulation);

        //-----PROM----//
        this.federationAmbassador.promClass = rtiamb.getObjectClassHandle("ObjectRoot.Prom");
        this.federationAmbassador.promAttr_liczbaWolnychMiejsc = rtiamb.getAttributeHandle("liczbaWolnychMiejsc", this.federationAmbassador.promClass);
        this.federationAmbassador.promAttr_numerStacji = rtiamb.getAttributeHandle("numerStacji", this.federationAmbassador.promClass);

        AttributeHandleSet attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add(this.federationAmbassador.promAttr_liczbaWolnychMiejsc);
        attributes.add(this.federationAmbassador.promAttr_numerStacji);

        rtiamb.subscribeObjectClassAttributes(this.federationAmbassador.promClass, attributes);


        //----STACJA----//

        this.federationAmbassador.stacjaClass = rtiamb.getObjectClassHandle("ObjectRoot.Stacja");
        this.federationAmbassador.stacjaAttr_numer = rtiamb.getAttributeHandle("numer", this.federationAmbassador.stacjaClass);

        attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add(this.federationAmbassador.stacjaAttr_numer);

        rtiamb.subscribeObjectClassAttributes(this.federationAmbassador.stacjaClass, attributes);


        //---PASAZER---//
        this.federationAmbassador.pasazerClass = rtiamb.getObjectClassHandle("ObjectRoot.Pasazer");
        this.federationAmbassador.pasazerAttr_id = rtiamb.getAttributeHandle("id", this.federationAmbassador.pasazerClass);
        this.federationAmbassador.pasazerAttr_typ = rtiamb.getAttributeHandle("typ", this.federationAmbassador.pasazerClass);
        this.federationAmbassador.pasazerAttr_numerStacji = rtiamb.getAttributeHandle("numerStacji", this.federationAmbassador.pasazerClass);
        this.federationAmbassador.pasazerAttr_stacjaDocelowa = rtiamb.getAttributeHandle("stacjaDocelowa", this.federationAmbassador.pasazerClass);
        this.federationAmbassador.pasazerAttr_naPromie = rtiamb.getAttributeHandle("naPromie", this.federationAmbassador.pasazerClass);


        attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add(this.federationAmbassador.pasazerAttr_id);
        attributes.add(this.federationAmbassador.pasazerAttr_typ);
        attributes.add(this.federationAmbassador.pasazerAttr_numerStacji);
        attributes.add(this.federationAmbassador.pasazerAttr_stacjaDocelowa);
        attributes.add(this.federationAmbassador.pasazerAttr_naPromie);

        rtiamb.subscribeObjectClassAttributes(this.federationAmbassador.pasazerClass, attributes);

        //i dalej
    }


    private void sendStopInteraction(double timeToAdvance) {
        sendInteraction("KoniecSymulacji", timeToAdvance);
    }

    private void sendStartInteraction(double timeToAdvance) {
        sendInteraction("PoczatekSymulacji", timeToAdvance);
    }

    private void sendInteraction(String interactionClass, double timeToAdvance) {

        try {
            SuppliedParameters parameters = RtiFactoryFactory.getRtiFactory().createSuppliedParameters();
            int interactionHandle = 0;

            interactionHandle = this.rtiamb.getInteractionClassHandle("InteractionRoot." + interactionClass);

            LogicalTime time = convertTime(timeToAdvance);
            this.rtiamb.sendInteraction(interactionHandle, parameters, generateTag(), time);

        } catch (NameNotFound nameNotFound) {
            nameNotFound.printStackTrace();
        } catch (FederateNotExecutionMember federateNotExecutionMember) {
            federateNotExecutionMember.printStackTrace();
        } catch (RTIinternalError rtIinternalError) {
            rtIinternalError.printStackTrace();
        } catch (SaveInProgress saveInProgress) {
            saveInProgress.printStackTrace();
        } catch (InvalidFederationTime invalidFederationTime) {
            invalidFederationTime.printStackTrace();
        } catch (RestoreInProgress restoreInProgress) {
            restoreInProgress.printStackTrace();
        } catch (InteractionParameterNotDefined interactionParameterNotDefined) {
            interactionParameterNotDefined.printStackTrace();
        } catch (ConcurrentAccessAttempted concurrentAccessAttempted) {
            concurrentAccessAttempted.printStackTrace();
        } catch (InteractionClassNotPublished interactionClassNotPublished) {
            interactionClassNotPublished.printStackTrace();
        } catch (InteractionClassNotDefined interactionClassNotDefined) {
            interactionClassNotDefined.printStackTrace();
        }


    }


}
