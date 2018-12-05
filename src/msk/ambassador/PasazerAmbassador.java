package msk.ambassador;

import hla.rti.*;
import hla.rti.jlc.EncodingHelpers;
import msk.BaseAmbassador;
import msk.Objects.Prom;

public class PasazerAmbassador extends BaseAmbassador {

    public int promClass                     =0;
    public int promAttr_liczbaWolnychMiejsc  =0;
    public int promAttr_numerStacji          =0;
    public boolean promClassFlag_newInstance  = false;
    public boolean promClassFlag_attrsUpdated = false;


    @Override
    public void reflectAttributeValues(int theObject, ReflectedAttributes theAttributes, byte[] tag, LogicalTime theTime, EventRetractionHandle retractionHandle) {
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
    }

    @Override
    public void removeObjectInstance(int theObject, byte[] userSuppliedTag, LogicalTime theTime, EventRetractionHandle retractionHandle) throws ObjectNotKnown, InvalidFederationTime, FederateInternalError {

    }
}
