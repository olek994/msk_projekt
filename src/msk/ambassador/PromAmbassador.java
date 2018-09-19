package msk.ambassador;

import hla.rti.*;
import hla.rti.jlc.EncodingHelpers;
import msk.BaseAmbassador;
import msk.Objects.Stacja;

public class PromAmbassador extends BaseAmbassador {


    public int stacjaClass                     =0;
    public int stacjaAttr_MaxDlugoscKolejki    =0;
    public int stacjaAttr_numer                =0;
    public int stacjaAttr_numerKolejnejStacji  =0;
    public int stacjaAttr_LiczbaPasazerow      =0;
    public int stacjaAttr_LiczbaSamochodow     =0;
    public int stacjaNumerStworzenia           =1;
    public int stacjaOstatnioModyfikowana      =0;
    public int stacjaOstatnioDodana            =0;
    public boolean stacjaClassFlag_newInstance  = false;
    public boolean stacjaClassFlag_attrsUpdated = false;

    @Override
    public void reflectAttributeValues(int theObject, ReflectedAttributes theAttributes, byte[] tag, LogicalTime theTime, EventRetractionHandle retractionHandle) {

    }

    @Override
    public void discoverObjectInstance(int theObject, int theObjectClass, String objectName) {

    }

    @Override
    public void removeObjectInstance(int theObject, byte[] userSuppliedTag, LogicalTime theTime, EventRetractionHandle retractionHandle) throws ObjectNotKnown, InvalidFederationTime, FederateInternalError {

    }
}
