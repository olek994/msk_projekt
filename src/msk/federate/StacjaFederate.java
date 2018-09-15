package msk.federate;

import hla.rti.RTIexception;
import msk.BaseAmbassador;
import msk.BaseFederate;
import msk.ambassador.StacjaAmbassador;

public class StacjaFederate extends BaseFederate<StacjaAmbassador> {

    private static int stacjaObj = 0;

    @Override
    protected void update(double timeToAdvance) {

    }

    @Override
    protected void publishAndSubscribe() throws RTIexception {

    }


    @Override
    protected void deleteObjectsAndInteractions() throws Exception {
        this.deleteObject(stacjaObj);
    }

    public static void main(String[] agrs){
        try{
            new StacjaFederate().runFederate("STACJA "+stacjaObj);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
