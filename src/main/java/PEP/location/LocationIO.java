/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//#ifdef PEP_LOCATION

package PEP.location;

import Client.Config;
//#if android
import org.bombusmod.location.LocationAndroid;
import org.bombusmod.BombusModActivity;
//#endif        

/**
 *
 * @author Vitaly
 */
public abstract class LocationIO {

    protected final static int NONE = 0;
    protected final static int JSR179 = 1;
    protected final static int CELLID = 2;

    public abstract String getLatitude();
    public abstract String getLongitude();
    static int providerType;

    public static LocationIO getInstance() throws ClassNotFoundException {
//#if android
        return new LocationAndroid(BombusModActivity.getInstance());
//#else        
//#         if (providerType == LocationIO.NONE) {
//#         try {
//#             // Sony-Ericsson supports JSR-179 only since JP8
//#             if (Config.getInstance().phoneManufacturer == Config.SONYE && Config.getInstance().swapMenu) {
//#                 return fallback();
//#             }
//#             // this will throw an exception if JSR-179 is missing
//#             Class.forName("javax.microedition.location.Location");
//#             Class c = Class.forName("PEP.location.JSR179Location");
//#             providerType = LocationIO.JSR179;
//#             // Sony-Ericsson JP8 should throw exception if GPS is absent
//#             if (Config.getInstance().phoneManufacturer == Config.SONYE) {
//#                 new JSR179Location().getCoordinates();
//#             }
//# 
//#         } catch (Exception e) {
//#            return fallback();
//#         }
//#         }
//#         switch (providerType) {
//#             case LocationIO.JSR179:
//#                 return new JSR179Location();
//#             default:
//#                 return new CellIDLocation();
//#         }
//#endif        
    }
    public abstract void getCoordinates() throws Exception;
}

//#endif
