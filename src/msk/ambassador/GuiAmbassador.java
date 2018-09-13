package msk.ambassador;

import hla.rti.EventRetractionHandle;
import hla.rti.LogicalTime;
import hla.rti.ReflectedAttributes;
import msk.BaseAmbassador;

/**
 * Created by Aleksander Małkowicz, Date: 13.09.2018
 * Copyright by RelayOnIT, Warszawa 2018
 */
public class GuiAmbassador extends BaseAmbassador {

    // parametery Poszczególnych federatów


    @Override
    public void reflectAttributeValues(int theObject, ReflectedAttributes theAttributes, byte[] tag, LogicalTime theTime, EventRetractionHandle retractionHandle) {
        //w zaleznosci od theObjcet beda ify ktore cos beda robic na gui


    }

    @Override
    public void discoverObjectInstance(int theObject, int theObjectClass, String objectName) {
        super.discoverObjectInstance(theObject, theObjectClass, objectName);

        //w zaleznosci od theObjcet beda dodawane nowe obiekt do HashMap


    }
}
