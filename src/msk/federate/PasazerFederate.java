package msk.federate;

import hla.rti.RTIexception;
import msk.BaseFederate;
import msk.ambassador.PasazerAmbassador;

public class PasazerFederate extends BaseFederate<PasazerAmbassador> {

    private static int pasazerObj;

    @Override
    protected void update(double timeToAdvance) {

    }

    @Override
    protected void publishAndSubscribe() throws RTIexception {

    }

    @Override
    protected void deleteObjectsAndInteractions() throws Exception {
        this.deleteObject(pasazerObj);
    }
    
    
    public static void main(String[] agrs){
        try{
            new PasazerFederate().runFederate("Pasazer "+pasazerObj);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
