package msk.federate;

import hla.rti.*;
import hla.rti.jlc.RtiFactoryFactory;
import javafx.application.Platform;
import javafx.scene.image.Image;
import msk.BaseFederate;
import msk.GuiApplication;
import msk.Objects.Pasazer;
import msk.Objects.Prom;
import msk.Objects.Stacja;
import msk.ambassador.GuiAmbassador;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Aleksander Małkowicz, Date: 13.09.2018
 * Copyright by RelayOnIT, Warszawa 2018
 */
public class GuiFederate extends BaseFederate<GuiAmbassador> {

    private int sendStartInteraction = 0;
    private boolean stopSim = false;

    private GuiApplication guiApplication;

    public GuiFederate(GuiApplication guiApplication) {
        this.guiApplication = guiApplication;
    }

    public void stopSimulation(){
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
            this.guiApplication.simTime.setText(this.federationAmbassador.federateTime+"");
        });

        //co okreslony czas (tick) sprawdzane sa czy jakis obiekt sie pojawil
        // czy moze zostal zaktualizowany i modyfikowane jest gui


        //--PROM--//
        if(this.federationAmbassador.promClassFlag_newInstance){
            Prom prom = this.federationAmbassador.getObjectInstances(Prom.class);
            this.federationAmbassador.promClassFlag_newInstance = false;

            System.out.println("DODANO PROM DO GUI");
        }
        if(this.federationAmbassador.promClassFlag_attrsUpdated){
            Prom prom = this.federationAmbassador.getObjectInstances(Prom.class);
            this.federationAmbassador.promClassFlag_attrsUpdated = false;

            System.out.println("LICZBA MIEJSC: "+ prom.getLiczbaWolnychMiejsc());

            Platform.runLater(() -> {
                this.guiApplication.liczbaWolnychMiejscNaPromie.setText(prom.getLiczbaWolnychMiejsc()+"");
            });

            System.out.println("ZAKTUALIZOWANO PARAMETRY PROMU: "+ prom.getNumerStacji());
        }

        //--STACJA--//
        if(this.federationAmbassador.stacjaClassFlag_newInstance){
            Stacja stacja = this.federationAmbassador.getStacjeObjInstances(this.federationAmbassador.stacjaOstatnioDodana);
            this.federationAmbassador.stacjaClassFlag_newInstance = false;
            this.federationAmbassador.stacjaOstatnioDodana = 0;

            System.out.println("DODANO Stacje DO GUI: "+stacja.getNumer());
        }
        if(this.federationAmbassador.stacjaClassFlag_attrsUpdated){
            Stacja stacja = this.federationAmbassador.getStacjeObjInstances(this.federationAmbassador.stacjaOstatnioModyfikowana);
            this.federationAmbassador.stacjaClassFlag_attrsUpdated = false;
            this.federationAmbassador.stacjaOstatnioModyfikowana = 0;
            String promNaStacji = stacja.getPromNaStacji() == 1 ? "Tak" : "Nie"; // TODO zmienic na ikony stacji na gui

            System.out.println("ZAKTUALIZOWANO PARAMETRY Stacji: "+ stacja.getNumer()+", "+stacja.getMaxDlugoscKolejki()+", "+stacja.getNumerKolejnejStacji()+" LiczbaPasazerow: "+stacja.getLiczbaPasazerow()+" LiczbaSamochodow: "+stacja.getLiczbaSamochodow()+" Prom na stacji: "+ promNaStacji);

            //TODO dodac reakcje na prom

            switch (stacja.getNumer()){
                case 1:
                    Platform.runLater(() -> {
                        this.guiApplication.liczbaPasazerowPierwszej.setText(stacja.getLiczbaPasazerow()+"");
                        this.guiApplication.liczbaSamochodowPierwszej.setText(stacja.getLiczbaSamochodow()+"");
                    });

                    if(stacja.getPromNaStacji() == 1){
                        Image image = new Image("/stacja_lewa_on.png");
                        Platform.runLater(() -> {
                                this.guiApplication.imagePierwszaStacja.setImage(image);
                        });
                    }else if(stacja.getPromNaStacji() == 0){
                        Image image = new Image("/stacja_lewa_off.png");
                        Platform.runLater(() -> {
                                this.guiApplication.imagePierwszaStacja.setImage(image);
                        });
                    }

                    break;
                case 2:
                    Platform.runLater(() -> {
                        this.guiApplication.liczbaPasazerowDrugiej.setText(stacja.getLiczbaPasazerow()+"");
                        this.guiApplication.liczbaSamochodowDrugiej.setText(stacja.getLiczbaSamochodow()+"");
                    });

                    if(stacja.getPromNaStacji() == 1){
                        Image image = new Image("/stacja_prawa_on.png");
                        Platform.runLater(() -> {
                            this.guiApplication.imageDrugaStacja.setImage(image);
                        });
                    }else if(stacja.getPromNaStacji() == 0){
                        Image image = new Image("/stacja_prawa_off.png");
                        Platform.runLater(() -> {
                            this.guiApplication.imageDrugaStacja.setImage(image);
                        });
                    }

                    break;
                case 3:
                    Platform.runLater(() -> {
                        this.guiApplication.liczbaPasazerowTrzeciej.setText(stacja.getLiczbaPasazerow()+"");
                        this.guiApplication.liczbaSamochodowTrzeciej.setText(stacja.getLiczbaSamochodow()+"");
                    });

                    if(stacja.getPromNaStacji() == 1){
                        Image image = new Image("/stacja_lewa_on.png");
                        Platform.runLater(() -> {
                            this.guiApplication.imageTrzeciaStacja.setImage(image);
                        });
                    }else if(stacja.getPromNaStacji() == 0){
                        Image image = new Image("/stacja_lewa_off.png");
                        Platform.runLater(() -> {
                            this.guiApplication.imageTrzeciaStacja.setImage(image);
                        });
                    }

                    break;
                case 4:

                    Platform.runLater(() -> {
                        this.guiApplication.liczbaPasazerowCzwartej.setText(stacja.getLiczbaPasazerow()+"");
                        this.guiApplication.liczbaSamochodowCzwartej.setText(stacja.getLiczbaSamochodow()+"");
                    });

                    if(stacja.getPromNaStacji() == 1){
                        Image image = new Image("/stacja_prawa_on.png");
                        Platform.runLater(() -> {
                            this.guiApplication.imageCzwartaStacja.setImage(image);
                        });
                    }else if(stacja.getPromNaStacji() == 0){
                        Image image = new Image("/stacja_prawa_off.png");
                        Platform.runLater(() -> {
                            this.guiApplication.imageCzwartaStacja.setImage(image);
                        });
                    }

                    break;
                case 5:
                    Platform.runLater(() -> {
                        this.guiApplication.liczbaPasazerowPiatej.setText(stacja.getLiczbaPasazerow()+"");
                        this.guiApplication.liczbaSamochodowPiatej.setText(stacja.getLiczbaSamochodow()+"");
                    });

                    if(stacja.getPromNaStacji() == 1){
                        Image image = new Image("/stacja_lewa_on.png");
                        Platform.runLater(() -> {
                            this.guiApplication.imagePiataStacja.setImage(image);
                        });
                    }else if(stacja.getPromNaStacji() == 0){
                        Image image = new Image("/stacja_lewa_off.png");
                        Platform.runLater(() -> {
                            this.guiApplication.imagePiataStacja.setImage(image);
                        });
                    }

                    break;
                case 6:
                    Platform.runLater(() -> {
                        this.guiApplication.liczbaPasazerowSzostej.setText(stacja.getLiczbaPasazerow()+"");
                        this.guiApplication.liczbaSamochodowSzostej.setText(stacja.getLiczbaSamochodow()+"");
                    });

                    if(stacja.getPromNaStacji() == 1){
                        Image image = new Image("/stacja_prawa_on.png");
                        Platform.runLater(() -> {
                            this.guiApplication.imageSzostaStacja.setImage(image);
                        });
                    }else if(stacja.getPromNaStacji() == 0){
                        Image image = new Image("/stacja_prawa_off.png");
                        Platform.runLater(() -> {
                            this.guiApplication.imageSzostaStacja.setImage(image);
                        });
                    }

                    break;
            }

        }


//        //--PASAZER--//
//        if(this.federationAmbassador.pasazerClassFlag_newInstance){
//            Pasazer pasazer = this.federationAmbassador.getPasazerObjInstances(this.federationAmbassador.pasazerOstatnioDodany);
//            this.federationAmbassador.pasazerClassFlag_newInstance = false;
//            this.federationAmbassador.pasazerOstatnioDodany = 0;
//
//            System.out.println("DODANO Pasazera DO GUI: "+pasazer.getId());
//        }
//        if(this.federationAmbassador.pasazerClassFlag_attrsUpdated){
//            Pasazer pasazer = this.federationAmbassador.getPasazerObjInstances(this.federationAmbassador.pasazerOstatnioModyfikowany);
//            this.federationAmbassador.pasazerClassFlag_attrsUpdated = false;
//            this.federationAmbassador.pasazerOstatnioModyfikowany   = 0;
//
//            System.out.println("ZAKTUALIZOWANO PARAMETRY Pasazera: "+pasazer.getId());
//        }

        if((sendStartInteraction ++) % 100 == 0){
            sendStartInteraction(timeToAdvance);
        }

        if(stopSim){
            sendStopInteraction(timeToAdvance);
            federationAmbassador.running = false;
        }

        //TODO DODAC GUI LABELKI

    }


    @Override
    protected void publishAndSubscribe() throws RTIexception {
        int startSimulation = rtiamb.getInteractionClassHandle("InteractionRoot.PoczatekSymulacji");
        rtiamb.publishInteractionClass(startSimulation);

        int stopSimulation = rtiamb.getInteractionClassHandle("InteractionRoot.KoniecSymulacji");
        rtiamb.publishInteractionClass(stopSimulation);

        //-----PROM----//
        this.federationAmbassador.promClass                         = rtiamb.getObjectClassHandle("ObjectRoot.Prom");
        this.federationAmbassador.promAttr_liczbaWolnychMiejsc      = rtiamb.getAttributeHandle("liczbaWolnychMiejsc",this.federationAmbassador.promClass);
        this.federationAmbassador.promAttr_liczbaZajetychMiejsc     = rtiamb.getAttributeHandle("liczbaZajetychMiejsc",this.federationAmbassador.promClass);
        this.federationAmbassador.promAttr_numerStacji              = rtiamb.getAttributeHandle("numerStacji",this.federationAmbassador.promClass);

        AttributeHandleSet attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add(this.federationAmbassador.promAttr_liczbaWolnychMiejsc);
        attributes.add(this.federationAmbassador.promAttr_liczbaZajetychMiejsc);
        attributes.add(this.federationAmbassador.promAttr_numerStacji);

        rtiamb.subscribeObjectClassAttributes(this.federationAmbassador.promClass,attributes);


        //----STACJA----//

        this.federationAmbassador.stacjaClass                     = rtiamb.getObjectClassHandle("ObjectRoot.Stacja");
        this.federationAmbassador.stacjaAttr_numer                = rtiamb.getAttributeHandle("numer",this.federationAmbassador.stacjaClass);
        this.federationAmbassador.stacjaAttr_MaxDlugoscKolejki    = rtiamb.getAttributeHandle("maxDlugoscKolejki",this.federationAmbassador.stacjaClass);
        this.federationAmbassador.stacjaAttr_numerKolejnejStacji  = rtiamb.getAttributeHandle("numerKolejnejStacji",this.federationAmbassador.stacjaClass);
        this.federationAmbassador.stacjaAttr_LiczbaPasazerow      = rtiamb.getAttributeHandle("liczbaPasazerow",this.federationAmbassador.stacjaClass);
        this.federationAmbassador.stacjaAttr_LiczbaSamochodow     = rtiamb.getAttributeHandle("liczbaSamochodow",this.federationAmbassador.stacjaClass);
        this.federationAmbassador.stacjaAttr_PromNaStacji         = rtiamb.getAttributeHandle("promNaStacji",this.federationAmbassador.stacjaClass);

        attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add(this.federationAmbassador.stacjaAttr_numer);
        attributes.add(this.federationAmbassador.stacjaAttr_MaxDlugoscKolejki);
        attributes.add(this.federationAmbassador.stacjaAttr_numerKolejnejStacji);
        attributes.add(this.federationAmbassador.stacjaAttr_LiczbaPasazerow);
        attributes.add(this.federationAmbassador.stacjaAttr_LiczbaSamochodow);
        attributes.add(this.federationAmbassador.stacjaAttr_PromNaStacji);

        rtiamb.subscribeObjectClassAttributes(this.federationAmbassador.stacjaClass,attributes);


//        //---PASAZER---//
//        this.federationAmbassador.pasazerClass                  = rtiamb.getObjectClassHandle("ObjectRoot.Pasazer");
//        this.federationAmbassador.pasazerAttr_id                = rtiamb.getAttributeHandle("id",this.federationAmbassador.pasazerClass);
//        this.federationAmbassador.pasazerAttr_typ               = rtiamb.getAttributeHandle("typ",this.federationAmbassador.pasazerClass);
//        this.federationAmbassador.pasazerAttr_numerStacji       = rtiamb.getAttributeHandle("numerStacji",this.federationAmbassador.pasazerClass);
//        this.federationAmbassador.pasazerAttr_stacjaDocelowa    = rtiamb.getAttributeHandle("stacjaDocelowa",this.federationAmbassador.pasazerClass);
//
//
//        attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
//        attributes.add(this.federationAmbassador.pasazerAttr_id);
//        attributes.add(this.federationAmbassador.pasazerAttr_typ);
//        attributes.add(this.federationAmbassador.pasazerAttr_numerStacji);
//        attributes.add(this.federationAmbassador.pasazerAttr_stacjaDocelowa);
//
//        rtiamb.subscribeObjectClassAttributes(this.federationAmbassador.pasazerClass,attributes);

        //i dalej
    }


    private void sendStopInteraction(double timeToAdvance) {
        sendInteraction("KoniecSymulacji",timeToAdvance);
    }
    private void sendStartInteraction(double timeToAdvance) {
        sendInteraction("PoczatekSymulacji",timeToAdvance);
    }

    private void sendInteraction(String interactionClass, double timeToAdvance) {

        try {
            SuppliedParameters parameters = RtiFactoryFactory.getRtiFactory().createSuppliedParameters();
            int interactionHandle = 0;

            interactionHandle = this.rtiamb.getInteractionClassHandle("InteractionRoot."+interactionClass);

            LogicalTime time = convertTime(timeToAdvance);
            this.rtiamb.sendInteraction(interactionHandle,parameters,generateTag(),time);

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
