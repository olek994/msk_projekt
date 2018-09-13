package msk.federate;

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
import msk.BaseFederate;
import msk.GuiApplication;
import msk.ambassador.GuiAmbassador;

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
        System.out.println("working");

        //co okreslony czas (tick) sprawdzane sa czy jakis obiekt sie pojawil
        // czy moze zostal zaktualizowany i modyfikowane jest gui


        if((sendStartInteraction ++) % 100 == 0){
            sendStartInteraction(timeToAdvance);
        }

        if(stopSim){
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
