package com.tasnetwork.calibration.conveyor.constant;

public class ConstantProTamp {

	
	
	public static final String SLAVE_WAITING_RESPONSE_ID ="888";
	
	public static final String PREVIEW_COLUMN1="Import";
	public static final String PREVIEW_COLUMN2="Export";
	public static final String PREVIEW_COLUMN3="NmleImport";
	public static final String PREVIEW_COLUMN4="NmleExport";
	
	public static final String TEST_POINT_ID_FORMAT = "%03d";
	public static final String TEST_MODE_AUTO_OCCURANCE = "AutoOccuranceMode";
	public static final String TEST_MODE_AUTO_RESTORATION = "AutoRestorationMode";	
	//public static final String EXECUTION_MANUAL_MODE = "ManualMode";
	public static final String TEST_MODE_MANUAL_OCCURANCE = "ManualOccuranceMode";
	public static final String TEST_MODE_MANUAL_RESTORATION = "ManualRestorationMode";
	public static final int RESULT_OCCURANCE_ID = 1;
	public static final int RESULT_RESTORATION_ID = 2;
	public static final String RESULT_OCCURANCE_ID_STR = "1";
	public static final String RESULT_RESTORATION_ID_STR = "2";
	//public static final String TEST_POINT_ID_FORMAT_2CHAR = "%02d";
	public static final Boolean PROTAMP_SINGLE_PHASE = false; //if true  then set TEST_IMAGE_NAME_PREFIX="condition_0";
	//public static final String TEST_IMAGE_NAME_PREFIX="condition_0";
	//public static final String TEST_IMAGE_NAME_PREFIX="condition_";
	public static final String TEST_POINT_3PHASE_IMAGE_NAME_PREFIX="3p-TestPoint-";
	public static final String TEST_POINT_1PHASE_IMAGE_NAME_PREFIX="condition_";
	
/*    public static final String SLAVE_WAITING="400";
    public static final String SLAVE_TIMEOUT="401";
    public static final String SLAVE_COMM_FAILED="402";*/
    
    public static  Boolean TARGET_DEVICE_IS_LINUX = false;
    public static  Boolean TARGET_DEVICE_IS_WINDOWS = false;
    public static Boolean ScanDeviceFound = false;
    public static Boolean LoginScreenDisplayEnabled = true;
    public static String currentConnectedDeviceName = "";
    
    public static final String MODE_MANUAL = "M";
    public static final String MODE_AUTO = "A";
    public static final String MODE_NONE = "N";
    public static final String DEVICE_NAME_LS_PROTAMP_1PHASE = "lsprotamp";
    public static final String DEVICE_NAME_LS_PROTAMP_3PHASE = "lsprotamp3p";
    //public static final String [] DEVICE_NAME_LIST= {"lsprotamp", "lsprotamp3p"};
    public static final String [] DEVICE_NAME_LIST = {DEVICE_NAME_LS_PROTAMP_1PHASE, DEVICE_NAME_LS_PROTAMP_3PHASE};
    public static final String LOG_FOLDER_PATH="./logs/";
    public static final String [] UPGRADE_UTILITY_TYPE_LIST = {"Firmware", "GUI Application"};
    
    public static void setCurrentConnectedDeviceName(String DeviceName){
    	currentConnectedDeviceName = DeviceName;
    }
    
    public static String getCurrentConnectedDeviceName(){
    	return currentConnectedDeviceName ;
    }

    
//	public static boolean ENABLE_SERIALPORT = false;// Commented for ProTamp3pMigration
	
}
