package msk.federate;

import hla.rti.*;
import hla.rti.jlc.RtiFactoryFactory;
import javafx.application.Platform;
import msk.BaseFederate;
import msk.GuiApplication;
import msk.Objects.Prom;
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

        //co okreslony czas (tick) sprawdzane sa czy jakis obiekt sie pojawil
        // czy moze zostal zaktualizowany i modyfikowane jest gui


        if(this.federationAmbassador.promClassFlag_newInstance){
            Prom prom = this.federationAmbassador.getObjectInstances(Prom.class);
            this.federationAmbassador.promClassFlag_newInstance = false;

            System.out.println("DODANO PROM DO GUI");
        }
        if(this.federationAmbassador.promClassFlag_attrsUpdated){
            Prom prom = this.federationAmbassador.getObjectInstances(Prom.class);
            this.federationAmbassador.promClassFlag_attrsUpdated = false;

            System.out.println("ZAKTUALIZOWANO PARAMETRY PROMU: "+ prom.getNumerStacji());
        }

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


        this.federationAmbassador.promClass                     = rtiamb.getObjectClassHandle("ObjectRoot.Prom");
        this.federationAmbassador.promAttr_liczbaWolnychMiejsc  = rtiamb.getAttributeHandle("liczbaWolnychMiejsc",this.federationAmbassador.promClass);
        this.federationAmbassador.promAttr_numerStacji          = rtiamb.getAttributeHandle("numerStacji",this.federationAmbassador.promClass);

        AttributeHandleSet attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add(this.federationAmbassador.promAttr_liczbaWolnychMiejsc);
        attributes.add(this.federationAmbassador.promAttr_numerStacji);

        rtiamb.subscribeObjectClassAttributes(this.federationAmbassador.promClass,attributes);
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
