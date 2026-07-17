package com.tasnetwork.calibration.conveyor.AsyncHttpClient;
import org.json.JSONObject;
public class ServerProperties {

    public static int pollingFrequencyInSec=3;

    public JSONObject someValueIWantToKeep;
    public static String HTTP_Protocol="http://";
    public static String PublicURL_Id="192.168.1.100";
    public static String URLPort="8080";
    public static String EndURL="";
    //public static int RefreshGUI_Freq=3;
    public String currentCountryName;
    public String Device_UUID;
    public Boolean LoginSuccess;
    public String LoginEmailID;

    public static boolean serverStatus=false;
    public static boolean PC_MODE_FEATURE_ENABLE_ON_TAB=false;
    public final boolean CONNECTED=true;
    public final boolean DISCONNECTED=false;
//    public static SharedPreferences server;

    public static boolean EnableUpgradeUtilityFeature =true;
    public static boolean EnableScanWifiFeature =true;
    public final static String SERVER_CONNECTION_SUCCESS="success";
    public final static String SERVER_CONNECTION_FAILED="failure";
    public static final String SERVER_CONNECTION_SLAVE_WAITING="400";
    public static final String SERVER_CONNECTION_SLAVE_TIMEOUT="401";
    public static final String SERVER_CONNECTION_SLAVE_COMM_FAILED="402";
    protected ServerProperties(){}

    public static Boolean getServerStatus(){
    	return serverStatus;
    }
    public static void setServerStatus(Boolean value){
    	serverStatus = value;
    }

    public static void ClearMyProperties(){




    }

}
