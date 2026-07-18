package com.tasnetwork.calibration.conveyor.constant;

import java.util.ArrayList;

import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfigReader;

public class ConstantConveyorConfig {

	public static String SAVE_FILE_LOCATION = "C:\\Reports\\";
	public static String REPORT_TEMPLATE_FILE_PATH = "C:\\Reports\\Template\\";
	public static String REPORT_TEMPLATE_FILE_NAME = "ProTampReport_V1_0.xlsx";

	public static String TERMINAL_CONFIG_FILE_PATH = "C:\\Reports\\Template\\";
	public static String TERMINAL_CONFIG_FILE_NAME = "ProTampReport_V1_0.xlsx";

	public static long DEPLOYMENT_DB_SEARCH_MAX_TIME_LIMIT_IN_DAYS = 7;
	public static int REF_STD_NO_OF_SAMPLES = 1;
	public static long REF_STD_BAUD_RATE = 57600;

	// public static String configFilePathName =
	// "/resources/"+ConstantVersion.configFileName;//config.json";

	// public static String reportPythonFilePathName =
	// "/resources/"+ConstantVersion.pythonFileName;
	public static boolean REPORT_CUSTOM_EXPORT_AS_PDF_ENABLED = false;
	public static String PYTHON_EXE_LOCATION = "";
	public static String PYTHON_SCRIPT_LOCATION = "";

	public static String DB_URL = "jdbc:mysql://localhost:3306/";
	public static String DB_URL_TAIL_OPTION = "";

	public static String DB_USERNAME = "username";
	public static String DB_PASSWORD = "password";
	public static String DB_NAME = "procon_v2_5_4";
	public static String SQL_LOCATION = "DefaultMySQL_InstalledLocation";
	public static String SQL_BACKUP_LOCATION = "DefaultBackupLocation";

	public static String POWERSRC = "PowerSrc";
	public static String MY_TERMINAL_ID = "1";
	public static String CLUSTER_NAME_01 = "Cluster1";
	// public static String BAY_NAME_FT = "FT1";
	public static String REFSTD = "RefStd";
	public static String LDU = "LDU";
	public static long No_of_pulses = 0;
	public static long ScreenWidthThreshold = 0;
	public static long ScreenHeightThreshold = 0;
	public static int POWERONWAITCOUNTER = 0;
	public static int SECOND_VALIDATION_RETRY_COUNT = 0;
	public static int SECOND_VALIDATION_WAIT_TIME = 0;
	public static int SKIP_TP_TIME_INSEC = 0;
	public static int VOLT_ACCEPTED_PERCENTAGE = 0;
	public static int CURRENT_ACCEPTED_PERCENTAGE = 0;
	public static int DEGREE_ACCEPTED_PERCENTAGE = 0;
	public static float FREQUENCY_ACCEPTED_PERCENTAGE = 0;
	public static int HAR_VOLT_ACCEPTED_PERCENTAGE = 0;
	public static int HAR_CURRENT_ACCEPTED_PERCENTAGE = 0;
	public static int VOLT_MIN = 0;
	public static int VOLT_MAX = 0;
	public static int CURRENT_MIN = 0;
	public static int CURRENT_MAX = 0;
	public static long DeleteLogFilesforX_NoOfPreviousDays = 30;

	public static String CMD_PWR_SRC_PHASE_RY_NORMAL = "120";
	public static String CMD_PWR_SRC_PHASE_RB_NORMAL = "240";
	public static String CMD_PWR_SRC_PHASE_RY_PHASEREV = "240";
	public static String CMD_PWR_SRC_PHASE_RB_PHASEREV = "120";
	public static ArrayList<String> I_MAPPING_DEFAULT_VALUES = new ArrayList<String>();
	public static long I_MAPPING_SIZE = 1;
	public static ArrayList<String> PF_MAPPING_DEFAULT_VALUES = new ArrayList<String>();

	public static long PF_MAPPING_SIZE = 7;

	public static float ERROR_MIN = 0;
	public static float ERROR_MAX = 0;

	public static float ERROR_MIN_DEFAULT_VALUE = 0.25f;
	public static float ERROR_MAX_DEFAULT_VALUE = 1.00f;

	public static int APP_INSTANCE_SERVER_PORT = 8082;

	public static int POWER_SRC_ACCEPTED_CONTINUOUS_FAILURE_COUNTER = 5;

	public static int LDU_STA_READING_WAIT_TIME_IN_SEC = 600;
	public static int LDU_CREEP_READING_WAIT_TIME_IN_SEC = 120;
	public static int LDU_DIAL_TEST_READING_WAIT_TIME_IN_SEC = 120;
	public static int LDU_REPEAT_SELFHEATING_READING_WAIT_TIME_IN_SEC = 120;

	public static int ACC_NO_OF_PAGES_IN_REPORT = 1;
	public static int ACC_NO_OF_PF_VARIANT_IN_EACH_PAGE = 3;

	public static boolean REF_STD_FEATURE_ENABLED = false;
	public static boolean REF_STD_APPLIED_PRECISION_CONNECTED = false;

	public static boolean CONVEYOR_DEBUG_SCREEN_DISPLAY_ENABLED = false;
	public static int PALLET_MANAGE_RECENT_NO_OF_DAYS_DISPLAY = 0;
	public static int WAITING_TO_VERIFIC_BAY_PALLET_MOVEMENT_WAIT_TIME_IN_SEC = 60;
	public static int VERIFIC_TO_STA1_BAY_PALLET_MOVEMENT_WAIT_TIME_IN_SEC = 60;
	public static int VERIFIC_TO_STA2_BAY_PALLET_MOVEMENT_WAIT_TIME_IN_SEC = 60;
	public static int STA1_TO_UNLOADING_BAY_PALLET_MOVEMENT_WAIT_TIME_IN_SEC = 60;
	public static int STA2_TO_UNLOADING_BAY_PALLET_MOVEMENT_WAIT_TIME_IN_SEC = 60;

	public static String PARAM_PROJECT_NAME = "projectName";

	public static String CONVEYOR_PROCAL_CALIB_IP_ADDRESS = "3.3.3.3";
	public static String CONVEYOR_PROCAL_CALIB_IP_PORT = "8888";
	public static String CONVEYOR_PROCAL_CALIB_END_PATH = "calib";
	public static String CONVEYOR_PROCAL_CALIB_PROJECT_NAME = "DevSysCalib01";

	public static String CONVEYOR_PROCAL_VERIFIC_IP_ADDRESS = "1.1.1.1";
	public static String CONVEYOR_PROCAL_VERIFIC_IP_PORT = "9999";
	public static String CONVEYOR_PROCAL_VERIFIC_END_PATH = "verific";
	public static String CONVEYOR_PROCAL_VERIFIC_PROJECT_NAME = "DevSysVerific03";
	public static String CONVEYOR_PROCAL_VERIFIC_CMD_LOCATION = "PATH";
	public static String CONVEYOR_PROCAL_VERIFIC_JAR_FILE = ".jar";

	public static String CONVEYOR_PROCAL_STA1_IP_ADDRESS = "0.0.0.0";
	public static String CONVEYOR_PROCAL_STA1_IP_PORT = "9000";
	public static String CONVEYOR_PROCAL_STA1_END_PATH = "sta1";
	public static String CONVEYOR_PROCAL_STA1_PROJECT_NAME = "DevSysSta1";
	public static String CONVEYOR_PROCAL_STA1_CMD_LOCATION = "PATH";
	public static String CONVEYOR_PROCAL_STA1_JAR_FILE = ".jar";

	public static String CONVEYOR_PROCAL_STA2_IP_ADDRESS = "2.2.2.2";
	public static String CONVEYOR_PROCAL_STA2_IP_PORT = "6666";
	public static String CONVEYOR_PROCAL_STA2_END_PATH = "sta2";
	public static String CONVEYOR_PROCAL_STA2_PROJECT_NAME = "DevSysSta2";
	public static String CONVEYOR_PROCAL_STA2_CMD_LOCATION = "PATH";
	public static String CONVEYOR_PROCAL_STA2_JAR_FILE = ".jar";

	public static String CONVEYOR_REJECTION_CMD_LOCATION;
	public static String CONVEYOR_REJECTION_PYTHON_SCRIPT;
	public static String CONVEYOR_REJECTION_PYTHON_SCRIPT_SCREEN_NUMBER = "";
	public static String CONVEYOR_REJECTION_PYTHON_EXECUTABLE_PATH; // Can be null
	public static String CONVEYOR_REJECTION_MONITOR_DISPLAY_NAME = "";
	public static String CONVEYOR_REJECTION_BAY_DISPLAY_NAME = "";
	public static String CONVEYOR_REJECTION_PORT = "5001";

	public static String CONVEYOR_REJECTION_MONITOR_MANUFACTURER_NAME = "SampleManu-1";
	public static String CONVEYOR_REJECTION_MONITOR_PID_NO = "SamplePid-1";
	public static String CONVEYOR_REJECTION_MONITOR_SERIAL_NO = "SampleSerial-1";

	public static int CONVEYOR_REJECTION_SCREEN_NUMBER;
	public static String CONVEYOR_REJECTION_CLUSTER_ID = "4";
	public static String CONVEYOR_UNLOADING_CLUSTER_ID = "5";
	public static String REST_API_TAIL_END_REJECTION_DISPLAY = "/api/rejection_meters2/";
	public static String REST_API_BATCH_UPDATE_TAIL_END_REJECTION_DISPLAY = "/api/meters/batch";
	public static String REST_API_IDLE_DISPLAY_TAIL_END_REJECTION_DISPLAY = "/api/idle";

	public static String REST_API_TAIL_END_UNLOADING_DISPLAY = "/api/unloading_meters2/";
	public static String REST_API_BATCH_UPDATE_TAIL_END_UNLOADING_DISPLAY = "/api/unloading_meters/batch";
	public static String REST_API_IDLE_DISPLAY_TAIL_END_UNLOADING_DISPLAY = "/api/idle";

	public static String CONVEYOR_UNLOADING_CMD_LOCATION;
	public static String CONVEYOR_UNLOADING_PYTHON_SCRIPT;
	public static String CONVEYOR_UNLOADING_PYTHON_SCRIPT_SCREEN_NUMBER = "";
	public static String CONVEYOR_UNLOADING_PYTHON_EXECUTABLE_PATH; // Can be null
	public static int CONVEYOR_UNLOADING_SCREEN_NUMBER;
	public static String CONVEYOR_UNLOADING_MONITOR_DISPLAY_NAME = "";
	public static String CONVEYOR_UNLOADING_BAY_DISPLAY_NAME = "";
	public static String CONVEYOR_UNLOADING_PORT = "5002";

	public static String CONVEYOR_UNLOADING_MONITOR_MANUFACTURER_NAME = "SampleManu-2";
	public static String CONVEYOR_UNLOADING_MONITOR_PID_NO = "SamplePid-2";
	public static String CONVEYOR_UNLOADING_MONITOR_SERIAL_NO = "SampleSerial-2";

	// public static String CONFIG_FILE_VERSION = "1";

}
