package com.tasnetwork.calibration.energymeter.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.tasnetwork.calibration.energymeter.reportprofile.PrintStyle;

public class ConstantAppConfig {
	
	public static String configFilePathName = "/resources/"+ConstantVersion.configFileName;//config.json";
	public static String POWER_SOURCE_SINGLE_PHASE_OUTPUT_SELECTED = ConstantApp.FIRST_PHASE_DISPLAY_NAME;
	
	public static String TERMINAL_CONFIG_FILE_PATH =  "C:\\Reports\\Template\\";
	public static String TERMINAL_CONFIG_FILE_NAME =  "ProTampReport_V1_0.xlsx";

	public static String DB_URL = "jdbc:mysql://localhost:3306/";
	public static String DB_URL_TAIL_OPTION = "";
	public static String DB_USERNAME = "username";
	public static String DB_PASSWORD = "password";
	public static String DB_NAME = "procaldefault";
	public static String SQL_LOCATION = "DefaultMySQL_InstalledLocation";
	public static String SQL_BACKUP_LOCATION = "DefaultBackupLocation";

	public static String POWERSRC = "PowerSrc";
	public static String REFSTD = "RefStd";
	public static String LDU = "LDU";
	public static String ICT = "ICT";
	public static String HARMONICS_SRC = "Harmonics Source";
	public static String DUT = "DUT";
	public static long No_of_pulses = 0;
	public static long ScreenWidthThreshold = 0;
	public static long ScreenHeightThreshold = 0;
	public static long DeleteLogFilesforX_NoOfPreviousDays = 30;
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
	public static float LTCT_VOLT_MIN = 0;
	public static float LTCT_VOLT_MAX = 0;
	public static float LTCT_CURRENT_MIN = 0;
	public static float LTCT_CURRENT_MAX = 0;
	//public static float LTCT_CURRENT_IMAX_MAX = 0;
	
	public static float HTCT_VOLT_MIN = 0;
	public static float HTCT_VOLT_MAX = 0;
	public static float HTCT_CURRENT_MIN = 0;
	public static float HTCT_CURRENT_MAX = 0;
	//public static float HTCT_CURRENT_IMAX_MAX = 0;

	public static int DEGREE_MIN = 0;
	public static int DEGREE_MAX = 0;
	public static int FREQUENCY_MIN = 0;
	public static int FREQUENCY_MAX = 0;
	public static String CMD_PWR_SRC_PHASE_RY_NORMAL = "120";
	public static String CMD_PWR_SRC_PHASE_RB_NORMAL = "240";
	public static String CMD_PWR_SRC_PHASE_RY_PHASEREV = "240";
	public static String CMD_PWR_SRC_PHASE_RB_PHASEREV = "120";
	public static ArrayList<String> I_MAPPING_DEFAULT_VALUES = new ArrayList<String>();
	public static long I_MAPPING_SIZE = 15;
	public static ArrayList<String> PF_MAPPING_DEFAULT_VALUES = new ArrayList<String>();
	
	public static long PF_MAPPING_SIZE = 7;
	public static boolean REF_STD_PHASE_180_FLAG = true;
	public static boolean REF_STD_CONSTANT_CONFIG_ENABLE = true;
	public static boolean REF_STD_ENABLE_PARSING_LOGS = true;
	public static float ERROR_MIN = 0;
	public static float ERROR_MAX = 0;
	public static float ERROR_MIN_DEFAULT_VALUE = 0.25f;
	public static float ERROR_MAX_DEFAULT_VALUE = 1.00f;
	public static int TIME_DEFAULT_VALUE = 2;
	public static int PULSES_DEFAULT_VALUE = 10;
	public static int AVERAGE_DEFAULT_VALUE = 1;
	
	public static int SKIP_READING_DEFAULT_VALUE = 0;
	public static float DEVIATION_DEFAULT_VALUE = 0.1f;
	public static float VOLT_ZERO_ACCEPTED_VALUE = 4.0f;
	public static float CURRENT_ZERO_ACCEPTED_VALUE = 4.0f;
	public static float DEGREE_ZERO_ACCEPTED_VALUE = 1.0f;
	public static float DEGREE_180_ACCEPTED_VALUE = 178.0f;
	public static long POWER_SRC_REBOOT_UP_WAIT_TIME_IN_MSEC = 30000;
	public static long COOL_OFF_TIME_IN_MSEC = 15000;

	public static String RSS_LTCT_ACTIVE_PULSE_CONSTANT_DEFAULT = "100000000";
	public static String RSS_LTCT_REACTIVE_PULSE_CONSTANT_DEFAULT = "100000000";
	public static String RSS_HTCT_ACTIVE_PULSE_CONSTANT_DEFAULT = "100000000"; // default value for safety
	public static String RSS_HTCT_REACTIVE_PULSE_CONSTANT_DEFAULT = "100000000"; // default value for safety

	public static String LSCS_STATIC_POWER_SOURCE_INIT = "3.6028";
	public static boolean INSTANT_METRICS_DISPLAY_ALWAYS_ON_TOP = false;
	public static boolean INSTANT_METRICS_ENERGY_DISPLAY_IN_KWH = false;
	
	public static int REPORT_EXCEL_LAST_ROW = 999;
	public static int REPORT_EXCEL_LAST_COLUMN = 999;
	
	public static String DEPLOYMENT_REFERENCE_NUMBER_LABEL_NAME = "Refer No";
	
	public static int APP_INSTANCE_SERVER_PORT = 8082;
	
	public static int POWER_SRC_ACCEPTED_CONTINUOUS_FAILURE_COUNTER = 5;
	
	public static int LDU_STA_READING_WAIT_TIME_IN_SEC = 600;
	public static int LDU_CREEP_READING_WAIT_TIME_IN_SEC = 120;
	public static int LDU_DIAL_TEST_READING_WAIT_TIME_IN_SEC = 120;
	public static int LDU_REPEAT_SELFHEATING_READING_WAIT_TIME_IN_SEC = 120;

	
	public static int TIME_MIN = 0;
	public static int TIME_MAX = 0;
	
	public static float VOLT_UNBALANCE_ZERO_VOLT_PERCENTAGE_LESS_THAN_100V = 8;
	public static float VOLT_UNBALANCE_ZERO_VOLT_PERCENTAGE_GREATER_THAN_100V = 4;
	
	public static int ACC_NO_OF_PAGES_IN_REPORT = 1;
	public static int ACC_NO_OF_PF_VARIANT_IN_EACH_PAGE = 3;
	

	public static String   DefaultReportProfileDisplay = "DefaultReportProfile";
	
	//public static ArrayList<String> REPORT_PROFILE_LIST = new ArrayList<String> (Arrays.asList(DefaultReportProfileDisplay));
	
	public static String REPORT_CONFIG_FILE_PATH = "/resources/";
	public static String REPORT_CONFIG_FILE_NAME = "report_config.json";
	
	public static String REPORT_PROFILE_PATH = "/resources/";
	public static ArrayList<String> REPORT_PROFILE_LIST = new ArrayList<String> ();
	public static ArrayList<String> REPORT_PROFILE_CONFIG_PATH_LIST = new ArrayList<String> ();
	
	public static int  BNC_OUTPORT_PORT = 1 ;
	
	public static long DEPLOYMENT_DB_SEARCH_MAX_TIME_LIMIT_IN_DAYS= 7;
	
	
	
	public static boolean  METER_PROFILE_REPORT_ENABLED = false;
	
	public static String   REPORT_DATE_FORMAT = "yyyy-MM-dd";
	public static String   REPORT_TIME_FORMAT = "HH:mm:ss";
	public static String   REPORT_TIME_ZONE = "Asia/Calcutta";
	public static String   REPORT_TOTAL_NO_OF_PAGES = "5";
	
	public static String   METER_PROFILE_REPORT_PAGE_NUMBER = "1";
	
	public static String   PYTHON_EXE_LOCATION = "";
	public static String   PYTHON_SCRIPT_LOCATION = "";
	
	
	
	public static boolean GENERATE_INDIVIDUAL_METER_REPORT_ENABLED = false;
	public static boolean REPORT_HIGHLIGHT_COLOR_FOR_PASS = true;
	public static boolean REPORT_HIGHLIGHT_COLOR_FOR_FAIL = true;
	public static int     REPORT_FONT_SIZE = 11;
	public static String  REPORT_FONT_NAME = "Courier New";
	
	public static String reportPythonFilePathName = "/resources/"+ConstantVersion.pythonFileName;//config.json";
	
	public static int totalReportProfile = 1;
	
	public static boolean REPORT_DATA_REPLACE_ENABLED = false;
	
	public static boolean REPORT_CUSTOM_EXPORT_AS_PDF_ENABLED = false;
	
	public static int  REPORT_DATA_REPLACE_COUNT = 0 ;
	public static ArrayList<String> REPORT_DATA_FIND = new ArrayList<String> ();
	public static ArrayList<String> REPORT_DATA_REPLACE = new ArrayList<String> ();
	
	public static String LSCS_POWER_SOURCE_CALIBRATION_FILE_PATH = "/resources/";
	public static String LSCS_POWER_SOURCE_CALIBRATION_FILE_NAME = "lscs_calibration_config.json";
	
	public static String CONVEYOR_CONFIG_FILE_PATH = "/resources/";
	public static String CONVEYOR_CONFIG_FILE_NAME = "conveyor_config.json";
	
	public static String REFSTD_CONSTANT_CONFIG_FILE_NAME = "/resources/"+ "RefStd_Sands.json";
	
	public static String REFSTD_CONSTANT_CONFIG_FILE_PATH = "/resources/";
	
	public static ArrayList<String> REPORT_PROFILE_DEFAULT_ACTIVE_GROUP_NAME_LIST = new ArrayList<String>();
	public static String REPORT_PROFILE_DEFAULT_ACTIVE_CUSTOMER_ID = "0";
	
	public static String REPORT_PROFILEV2_FILE_PATH = "/resources/";
	public static String REPORT_PROFILEV2_FILE_NAME = "ReportProfileV2.json";
	
	public static String DUT_DISPLAY_KEY = "DUT";
	public static boolean DUT_COMM_FEATURE_ENABLED = false;
	
	public static int DEVICE_SETTINGS_POWER_SOURCE_ID = 1;
	public static int DEVICE_SETTINGS_REF_STD_ID = 2;
	public static int DEVICE_SETTINGS_LDU_ID = 3;
	public static int DEVICE_SETTINGS_ICT_ID = 4;
	public static int DEVICE_SETTINGS_DUT01_ID = 5;
	public static int DEVICE_SETTINGS_DUT02_ID = 6;
	public static int DEVICE_SETTINGS_DUT03_ID = 7;
	public static int DEVICE_SETTINGS_DUT04_ID = 8;
	public static int DEVICE_SETTINGS_DUT05_ID = 9;
	public static int DEVICE_SETTINGS_DUT06_ID = 10;
	public static int DEVICE_SETTINGS_DUT07_ID = 11;
	public static int DEVICE_SETTINGS_DUT08_ID = 12;
	public static int DEVICE_SETTINGS_DUT09_ID = 13;
	public static int DEVICE_SETTINGS_DUT10_ID = 14;
	
	public static int DEVICE_SETTINGS_DUT11_ID = 15;
	public static int DEVICE_SETTINGS_DUT12_ID = 16;
	public static int DEVICE_SETTINGS_DUT13_ID = 17;
	public static int DEVICE_SETTINGS_DUT14_ID = 18;
	public static int DEVICE_SETTINGS_DUT15_ID = 19;
	public static int DEVICE_SETTINGS_DUT16_ID = 20;
	public static int DEVICE_SETTINGS_DUT17_ID = 21;
	public static int DEVICE_SETTINGS_DUT18_ID = 22;
	public static int DEVICE_SETTINGS_DUT19_ID = 23;
	public static int DEVICE_SETTINGS_DUT20_ID = 24;
	
	public static int DEVICE_SETTINGS_DUT21_ID = 25;
	public static int DEVICE_SETTINGS_DUT22_ID = 26;
	public static int DEVICE_SETTINGS_DUT23_ID = 27;
	public static int DEVICE_SETTINGS_DUT24_ID = 28;
	public static int DEVICE_SETTINGS_DUT25_ID = 29;
	public static int DEVICE_SETTINGS_DUT26_ID = 30;
	public static int DEVICE_SETTINGS_DUT27_ID = 31;
	public static int DEVICE_SETTINGS_DUT28_ID = 32;
	public static int DEVICE_SETTINGS_DUT29_ID = 33;
	public static int DEVICE_SETTINGS_DUT30_ID = 34;
	
	public static int DEVICE_SETTINGS_DUT31_ID = 35;
	public static int DEVICE_SETTINGS_DUT32_ID = 36;
	public static int DEVICE_SETTINGS_DUT33_ID = 37;
	public static int DEVICE_SETTINGS_DUT34_ID = 38;
	public static int DEVICE_SETTINGS_DUT35_ID = 39;
	public static int DEVICE_SETTINGS_DUT36_ID = 40;
	public static int DEVICE_SETTINGS_DUT37_ID = 41;
	public static int DEVICE_SETTINGS_DUT38_ID = 42;
	public static int DEVICE_SETTINGS_DUT39_ID = 43;
	public static int DEVICE_SETTINGS_DUT40_ID = 44;
	
	public static int DEVICE_SETTINGS_DUT41_ID = 45;
	public static int DEVICE_SETTINGS_DUT42_ID = 46;
	public static int DEVICE_SETTINGS_DUT43_ID = 47;
	public static int DEVICE_SETTINGS_DUT44_ID = 48;
	public static int DEVICE_SETTINGS_DUT45_ID = 49;
	public static int DEVICE_SETTINGS_DUT46_ID = 50;
	public static int DEVICE_SETTINGS_DUT47_ID = 51;
	public static int DEVICE_SETTINGS_DUT48_ID = 52;
	
	
	public static int NO_OF_UAC_PROFILES = 4;
	public static ArrayList<String> UAC_PROFILE_LIST = new ArrayList<String>();
	public static String UAC_DEFAULT_PROFILE = "Admin";
	
	public static boolean  METER_ID_BLACKLIST_VALIDATION = false ;
	public static int  TOTAL_METER_ID_BLACKLISTED = 0 ;
	public static ArrayList<String> METER_ID_BLACKLISTED_LIST = new ArrayList<String>();
	
	public static boolean  METER_ID_WHITELIST_VALIDATION = false ;
	public static int  TOTAL_METER_ID_WHITELISTED = 0 ;
	public static ArrayList<String> METER_ID_WHITELISTED_LIST = new ArrayList<String>();
	
	public static boolean  METER_ID_VALIDATE_ALREADY_TESTED = false ;
	public static boolean  METER_ID_VALIDATE_FOR_EMPTY = false ;
	
	public static boolean  IMPORT_DEPLOYMENT_DATA_FEATURE_ENABLED = false ;
	
	public static String  IMPORT_DEPLOYMENT_DATA_RACK_POSITION_ID_HEADER = "Rack ID" ;
	public static String  IMPORT_DEPLOYMENT_DATA_RACK_POSITION_SELECTED_HEADER = "Select" ;
	public static String  IMPORT_DEPLOYMENT_DATA_SERIAL_NO_HEADER = "Serial No" ;
	public static String  IMPORT_DEPLOYMENT_DATA_CT_RATIO_HEADER = "CTR Ratio" ;
	public static String  IMPORT_DEPLOYMENT_DATA_PT_RATIO_HEADER = "PTR Ratio" ;
	public static String  IMPORT_DEPLOYMENT_DATA_METER_CONSTANT_HEADER = "Meter Constant" ;
	public static String  IMPORT_DEPLOYMENT_DATA_MAKE_HEADER = "Make" ;
	public static String  IMPORT_DEPLOYMENT_DATA_MODEL_NO_HEADER = "ModelNo" ;
	
	public static int PROPOWER_MANUAL_MODE_TIMER_INPUT_MIN_ACCEPTED = 5 ;
	public static int PROPOWER_MANUAL_MODE_TIMER_INPUT_MAX_ACCEPTED = 3600 ;
	public static int PROPOWER_MANUAL_MODE_DEFAULT_TIMER_INPUT = 120 ;
	
	public static boolean REF_STD_FEEDBACK_CONTROL_ENABLED = false;//true;
	public static boolean REF_STD_FEEDBACK_CONTROL_MULTIPLIER_ENABLED = false;//true;
	public static float REF_STD_FEEDBACK_CONTROL_DEGREE_ALLOWED_UPPER_LIMIT = 0.2f;
	public static float REF_STD_FEEDBACK_CONTROL_DEGREE_ALLOWED_LOWER_LIMIT = 0.4f;
	public static int REF_STD_FEEDBACK_CONTROL_DEGREE_RETRY_MAX_COUNT = 5;
	
	public static float REF_STD_FEEDBACK_CONTROL_DEGREE_ALLOWED_UPPER_LIMIT_SINGLE_PHASE = 0.12f;
	public static float REF_STD_FEEDBACK_CONTROL_DEGREE_ALLOWED_LOWER_LIMIT_SINGLE_PHASE = 0.14F;

	public static float REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_UPPER_LIMIT_PERCENT = 0.2f;
	public static float REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_LOWER_LIMIT_PERCENT = 0.4f;
	public static float REF_STD_FEEDBACK_CONTROL_CURRENT_ALLOWED_UPPER_LIMIT_PERCENT = 0.2f;
	public static float REF_STD_FEEDBACK_CONTROL_CURRENT_ALLOWED_LOWER_LIMIT_PERCENT = 0.4f;
	
	//public static float REF_STD_FEEDBACK_CONTROL_FINETUNE_CURRENT_ALLOWED_PERCENTAGE = 0.25f;
	public static int REF_STD_FEEDBACK_CONTROL_FINETUNE_CURRENT_WAIT_TIME_IN_SEC = 60;
	public static int REF_STD_FEEDBACK_CONTROL_FINETUNE_CURRENT_CONFIRMATION_COUNT = 3;
	public static int REF_STD_FEEDBACK_CONTROL_SEND_DATA_TO_POWER_SRC_REFRESH_TIME_IN_MILLI_SEC = 1500;
	
	
	public static boolean DEPLOYMENT_VOLT_PERCENT_VALIDATION = true;
	public static boolean DEPLOYMENT_CURRENT_PERCENT_VALIDATION = true;
	
	public static float TP_USER_ENTRY_VOLT_PERCENT_ALLOWED_MAX = 150.0F;	
	public static float TP_USER_ENTRY_VOLT_PERCENT_ALLOWED_MIN = 0.0F;
	public static float TP_USER_ENTRY_CURRENT_Ib_PERCENT_ALLOWED_MAX = 120.0F;
	public static float TP_USER_ENTRY_CURRENT_IMAX_PERCENT_ALLOWED_MAX = 400.0F;
	public static float TP_USER_ENTRY_CURRENT_PERCENT_ALLOWED_MIN_ABOVE = 0.0F;
	public static float TP_USER_ENTRY_PF_ALLOWED_MAX = 1.0F;
	public static float TP_USER_ENTRY_PF_ALLOWED_MIN = 0.0F;
	
	public static float TP_USER_ENTRY_ACTIVE_ENERGY_KWH_ALLOWED_MAX = 10.0F;
	public static float TP_USER_ENTRY_ACTIVE_ENERGY_KWH_ALLOWED_MIN_ABOVE = 0.0F;
	public static float TP_USER_ENTRY_REACTIVE_ENERGY_KVARH_ALLOWED_MAX = 15.0F;
	public static float TP_USER_ENTRY_REACTIVE_ENERGY_KVARH_ALLOWED_MIN_ABOVE = 0.0F;
	public static float TP_USER_ENTRY_APPARENT_ENERGY_KVAH_ALLOWED_MAX = 12.0F;
	public static float TP_USER_ENTRY_APPARENT_ENERGY_KVAH_ALLOWED_MIN_ABOVE = 0.0F;
	
	public static int TP_USER_ENTRY_ITERATION_ALLOWED_MAX = 30;
	public static int TP_USER_ENTRY_ITERATION_ALLOWED_MIN = 1;

	public static List<PrintStyle> PRINT_STYLE_LIST = new ArrayList<PrintStyle>();
	
	public static boolean  DEFAULT_LOGIN_ID_POPULATE_ENABLED = false ;
	public static boolean  LSC_LDU_INVALID_DATA_SKIP = false;
	
	public static float DIAL_TEST_SINGLE_PHASE_NEARING_TARGET1_PERCENT = 90.0f;
	public static float DIAL_TEST_SINGLE_PHASE_NEARING_TARGET1_CURRENT_REDUCTION_PERCENT = 10.0f;
	public static float DIAL_TEST_SINGLE_PHASE_NEARING_TARGET2_PERCENT = 98.0f;
	public static float DIAL_TEST_SINGLE_PHASE_NEARING_TARGET2_CURRENT_REDUCTION_PERCENT = 5.0f;
	
	public static float DIAL_TEST_THREE_PHASE_NEARING_TARGET1_PERCENT = 90.0f;
	public static float DIAL_TEST_THREE_PHASE_NEARING_TARGET1_CURRENT_REDUCTION_PERCENT = 10.0f;
	public static float DIAL_TEST_THREE_PHASE_NEARING_TARGET2_PERCENT = 98.0f;
	public static float DIAL_TEST_THREE_PHASE_NEARING_TARGET2_CURRENT_REDUCTION_PERCENT = 5.0f;
	
	
	public static int  CONSTANT_TEST_RESULT_CONFIG_LIMIT_COUNT = 0 ;
	
	public static ArrayList<String> CONSTANT_TEST_FILTER_UNIT = new ArrayList<String> (Arrays.asList("Dummy1")) ;
	public static ArrayList<String> CONSTANT_TEST_FILTER_VALUE = new ArrayList<String> (Arrays.asList("Dummy2"));
	public static ArrayList<String> CONSTANT_TEST_ACCEPTED_UPPER_LIMIT = new ArrayList<String> (Arrays.asList("Dummy3"));
	public static ArrayList<String> CONSTANT_TEST_ACCEPTED_LOWER_LIMIT = new ArrayList<String> (Arrays.asList("Dummy4"));
	
	public static String  CONSTANT_TEST_DEFAULT_ACCEPTED_UPPER_LIMIT = "0.5" ;
	public static String  CONSTANT_TEST_DEFAULT_ACCEPTED_LOWER_LIMIT = "-0.5" ;

}
