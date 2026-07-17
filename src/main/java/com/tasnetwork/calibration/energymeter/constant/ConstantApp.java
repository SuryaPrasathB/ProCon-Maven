package com.tasnetwork.calibration.energymeter.constant;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TimerTask;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.testprofiles.TestProfileType;

public class ConstantApp {
	
/*	public static final String APPLICATION_NAME = "ProCAL";
	public static final String APPLICATION_VERSION = "S3.6.9";
	public static final String DB_SCHEMA_VERSION = "V1.9";*/
	
	public static final int METRICS_EXCEL_LOG_TIME_IN_MSEC = 1000;
	
	public static final String SERIAL_PORT_REF_STD="SerialCommRefStd";
	public static final String SERIAL_PORT_EIC="SerialCommEic";
	public static final String SERIAL_PORT_ELMEASURE="SerialCommElmeasure";
	public static final String SERIAL_PORT_DUT="SerialCommDut";
	public static final String SERIAL_PORT_QR_SCANNER="SerialCommQrScanner";
	
	public static final String USER_LICENCE_SKIP_SALTED_KEY_VALIDATION_DEFAULT_VALUE  = "25091978";//"-- --";
	public static final String SCREEN_WIDTH_THRESHOLD="ScreenWidthThreshold";
	public static final String SCREEN_HEIGHT_THRESHOLD="ScreenHeightThreshold";
	
	public static String   StandardReportProfileDisplay = "DefaultReportProfile";
	
	public static final String THEME_FXML="_W.fxml";
	//public static final String DB_URL = "jdbc:mysql://localhost:3306/";
	
	///////public static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/";
	public static final int AppInstanceServerPort = 8082;
	
	public static final int DEFAULT_PULSE_CONSTANT = -1;
	public static final String CALIBRATION_MODE_USER_NAME="calib";
	public static final String MAINTENANCE_MODE_USER_NAME="selftest";


	public static final String TableRowFiller = "";// "Nill";
	public static final Integer[] BaudRateConstant = {1200, 2400, 4800, 9600, 19200, 38400, 57600,115200};
	public static final String[] BaudRateConstantString = {"1200", "2400", "4800", "9600", "19200", "38400", "57600","115200"};
	public static final Integer DefaultBaudRateIndex = 3;

	
	public static String DefaultMeterType = "3 Phase Star Active";
	public static String METER_TYPE_SINGLE_PHASE_ACTIVE = "Single Phase Active";
	public static String METER_TYPE_THREE_PHASE_STAR_ACTIVE = "3 Phase Star Active";
	
	public static String METER_TYPE_THREE_PHASE_DELTA_ACTIVE = "3 Phase Delta Active";
	public static String METER_TYPE_THREE_PHASE_DELTA_REACTIVE = "3 Phase Delta Reactive";
	
	//public static String[] MeterType = {"Single Phase Active","Single Phase Reactive", "3 Phase Star Active","3 Phase Star Reactive","3 Phase Delta Active","3 Phase Delta Reactive"};
	public static String[] MeterType = {METER_TYPE_SINGLE_PHASE_ACTIVE ,"Single Phase Reactive", METER_TYPE_THREE_PHASE_STAR_ACTIVE,"3 Phase Star Reactive",METER_TYPE_THREE_PHASE_DELTA_ACTIVE,METER_TYPE_THREE_PHASE_DELTA_REACTIVE};
	//public static String[] MeterType = {METER_TYPE_SINGLE_PHASE_ACTIVE ,"Single Phase Reactive", METER_TYPE_THREE_PHASE_STAR_ACTIVE,"3 Phase Star Reactive"};
	public static final String TESTPOINT_RUNTYPE_TIMEBASED = "Time-based";
	public static final String TESTPOINT_RUNTYPE_PULSEBASED = "Pulse-based";
	public static final String METERTYPE_SINGLEPHASE = "Single Phase";
	
	public static final String METER_CT_TYPE_LTCT = "LTCT";
	public static final String METER_CT_TYPE_HTCT = "HTCT";
	//public static final String METER_CT_TYPE_WHOLE_CURRENT = "Whole current";
	public static final String DEFAULT_CT_TYPE =METER_CT_TYPE_LTCT;
	
	public static final String METERTYPE_THREEPHASE = "3 Phase";  
	public static final String METERTYPE_THREEPHASE_DELTA = "3 Phase Delta";
	public static final String METERTYPE_ACTIVE = "Active";
	public static final String METERTYPE_REACTIVE = "Reactive";
	public static final String SOURCE_TYPE_EM_MODEL="EM_Model";
	public static final String SOURCE_TYPE_EM_Device="EM_Device";
	public static final String SOURCE_TYPE_POWER_SOURCE="PowerSrc";
	public static final String SOURCE_TYPE_REF_STD="RefStd";
	public static final String SOURCE_TYPE_LDU="LDU";
	public static final String SOURCE_TYPE_ICT="ICT";
	public static final String SOURCE_TYPE_HARMONICS_SRC = "HarmonicsSrc";
	public static final String[] Frequency = {"46", "47","48","49","50","51","52","53","54","55"};
	
	
	public static final String REF_STD_TYPE_KRE_9XXX="Kre9xxx";
	public static final String REF_STD_METER_PULSE_TYPE_ACTIVE = "RefStdPulseActive";
	public static final String REF_STD_METER_PULSE_TYPE_REACTIVE = "RefStdPulseReactive";
	public static final String REF_STD_METER_PULSE_TYPE_APPARENT = "RefStdPulseApparent";
	
	public static final ArrayList<String> REF_STD_METER_PULSE_TYPE_LIST = new ArrayList<String>(Arrays.asList(REF_STD_METER_PULSE_TYPE_ACTIVE, 
			REF_STD_METER_PULSE_TYPE_REACTIVE,REF_STD_METER_PULSE_TYPE_APPARENT ));
	
	public static final String REF_STD_OPERATION_CURRENT_ABOVE_OR_EQUAL = "I-AboveOrEqual";
	public static final String REF_STD_OPERATION_CURRENT_BELOW = "I-Below";
	
	public static final ArrayList<String> REF_STD_OPERATION_CURRENT_LIST = new ArrayList<String>(Arrays.asList(REF_STD_OPERATION_CURRENT_ABOVE_OR_EQUAL, 
			REF_STD_OPERATION_CURRENT_BELOW ));
	
	
	public static final String REF_STD_OPERATION_VOLTAGE_ABOVE_OR_EQUAL = "V-AboveOrEqual";
	public static final String REF_STD_OPERATION_VOLTAGE_BELOW = "V-Below";
	
	public static final ArrayList<String> REF_STD_OPERATION_VOLTAGE_LIST = new ArrayList<String>(Arrays.asList(REF_STD_OPERATION_VOLTAGE_ABOVE_OR_EQUAL, 
			REF_STD_OPERATION_VOLTAGE_BELOW ));
	
	
	public static final String WARMUP_ALIAS_NAME = "WMP";
	public static final String CREEP_ALIAS_NAME = "NLD";
	public static final String STA_ALIAS_NAME = "STA";
	public static final String CONST_TEST_ALIAS_NAME = "CONST";
	public static final String ACCURACY_ALIAS_NAME = "LOE";
	public static final String VOLTAGE_ALIAS_NAME = "VV";
	public static final String FREQUENCY_ALIAS_NAME = "FV";
	public static final String HARMONIC_WITHOUT_ALIAS_NAME = "HARMWO";
	public static final String HARMONIC_INPHASE_ALIAS_NAME = "HARMIN";
	public static final String HARMONIC_OUTOFPHASE_ALIAS_NAME = "HARMOUT";
	public static final String CUTNEUTRAL_ALIAS_NAME = "CUTN";
	public static final String VOLT_UNBALANCE_ALIAS_NAME = "VU";
	public static final String PHASEREVERSAL_NORMAL_ALIAS_NAME = "RPSNORM";
	public static final String PHASEREVERSAL_REV_ALIAS_NAME = "RPSREV";
	public static final String REPEATABILITY_START_TEST_ALIAS_NAME = "REPSTART";
	public static final String REPEATABILITY_ALIAS_NAME = "REP";//2-Oct-2022//"REP";
	public static final String REPEATABILITY_END_TEST_ALIAS_NAME = "REPEND";
	public static final String SELF_HEATING_START_TEST_ALIAS_NAME = "SELFHWMP";
	public static final String SELF_HEATING_ALIAS_NAME = "SELFHT";//2-Oct-2022// "SELFH";
	public static final String SELF_HEATING_END_TEST_ALIAS_NAME = "SELFHEND";
	public static final String CUSTOM_TEST_ALIAS_NAME = "CSTM";
	public static final String DUT_COMMAND_TEST_ALIAS_NAME = "DUTCMD";
	
	
	public static final String IMPORT_MODE_ALIAS_NAME = "";
	public static final String EXPORT_MODE_ALIAS_NAME = "EXP ";
	public static final int EXPORT_MODE_DEVICE_ID_THRESHOLD = 1000;
	
	public static final String DEPLOYMENT_EXPORT_MODE = "EXPORT";
	public static final String DEPLOYMENT_IMPORT_MODE = "IMPORT";
	public static final String DEPLOYMENT_IMPORT_AND_EXPORT_MODE = "BOTH";
	public static final String DEPLOYMENT_IMPORT_EXPORT_UNDEFINED = "UNDF";
	

	//**************************************************//
	public static final int TOTAL_NO_OF_SYSTEM_CONFIG_KEY = 9;
	public static final String VOLTAGE_ACCEPTED_PERCENTAGE_KEY = "DefaultVoltAcceptedPercentage";
	public static final String CURRENT_ACCEPTED_PERCENTAGE_KEY = "DefaultCurrentAcceptedPercentage";
	public static final String PHASE_ACCEPTED_PERCENTAGE_KEY = "DefaultPhaseAcceptedPercentage";
	public static final String FREQUENCY_ACCEPTED_PERCENTAGE_KEY = "DefaultFrequencyAcceptedPercentage";
	public static final String HAR_VOLTAGE_ACCEPTED_PERCENTAGE_KEY = "DefaultHarVoltAcceptedPercentage";
	public static final String HAR_CURRENT_ACCEPTED_PERCENTAGE_KEY = "DefaultHarCurrentAcceptedPercentage";
	public static final String SYSTEM_CONFIG_KEY = "System_Config";
	public static final String SECOND_VALIDATION_WAIT_TIME_IN_SEC_KEY = "RefStandardValidationWaitTimeInSec";
	public static final String SECOND_VALIDATION_RETRY_COUNT_KEY = "RefStandardValidationRetryCount";
	/***********************************************************/




	
	public static final int REPEATABILITY_NO_OF_READING = 1;
	
	public static final String[] ACCESS_LEVEL = {"Admin", "Tester","Read Only"};
	public static String USER_ACCESS_LEVEL = "";
	public static String USER_NAME = "";
	
	public static final String TESTER_ACCESS_LEVEL = "Tester";
	public static final String READONLY_ACCESS_LEVEL = "Read Only";

	public static final String FREQ_UNIT = "Hz";
	
	public static final String PF_UPF = "UPF";


	public static final String PF_LEAD = "C";
	public static final String PF_LAG = "L";
	public static final float PF_MIN = 0f;
	public static final float PF_MAX = 1f;
	
	public static final float DEVIATION_DEFAULT_VALUE = 0.1f;
	
	public static final float SQRT_OF_THREE = 1.732f;
	
	public static final String SERIAL_PORT_ACCESS_FAILED = "Access Failed";
	public static final String SERIAL_PORT_COMMAND_FAILED = "CMD Failed";
	public static final String SERIAL_PORT_COMMAND_Success = "Success";

	
	/*  Test case Title*/
	
	public static final String DISPLAY_TC_TITLE_STARTING_CURRENT   = "Starting Current";
	public static final String DISPLAY_TC_TITLE_WARMUP   = "Warmup";
	public static final String DISPLAY_TC_TITLE_NOLOADTEST   = "No Load Test";
	public static final String DISPLAY_TC_TITLE_ACCURACY   = "Accuracy";
	public static final String DISPLAY_TC_TITLE_INF_VOLTAGE  = "Voltage Variation";
	public static final String DISPLAY_TC_TITLE_INF_FREQUENCY  = "Frequency Variation";
	public static final String DISPLAY_TC_TITLE_INF_HARMONICS   = "Harmonics";
	public static final String DISPLAY_TC_TITLE_CUT_NUETRAL   = "Nuetral Missing";
	public static final String DISPLAY_TC_TITLE_INF_VOLT_UNBALANCE   = "Voltage Unbalance";
	public static final String DISPLAY_TC_TITLE_PHASE_REVERSAL   = "Reverse Phase Sequence";
	public static final String DISPLAY_TC_TITLE_REPEATABLITY   = "Repeatability";
	public static final String DISPLAY_TC_TITLE_SELF_HEATING  = "Self Heating";
	public static final String DISPLAY_TC_TITLE_CONST_TEST   = "Constant Test";
	public static final String DISPLAY_TC_TITLE_CUSTOM_TEST   = "Custom Test";
	public static final String DISPLAY_TC_TITLE_DUT_COMMAND   = "Dut Command";
	
	public static final String IS_VALUE_IN_RANGE_FOR_VOLTAGE   = "Voltage";
	public static final String IS_VALUE_IN_RANGE_FOR_CURRENT   = "Current";
	public static final String IS_VALUE_IN_RANGE_FOR_FREQUENCY   = "Frequency";
	
	public static boolean DEBUG_FLAG = true;
	public static boolean INFO_FLAG = true;
	public static final String LEFT_STATUS_DEBUG   = "debug";
	public static final String LEFT_STATUS_INFO   = "info";

	

	public static final char END_CR = (char) 0x0d;
	public static final char END_CR2 = (char) 0x04;
	

//	public static final String TERMS_AND_CONDITIONS="This is sample test line1\n This is sample test line2";
	
	
	
	public static String FIRST_PHASE_DISPLAY_NAME = "R";           
	public static String SECOND_PHASE_DISPLAY_NAME = "Y";
	public static String THIRD_PHASE_DISPLAY_NAME = "B";
	
	public static String VOLTAGE_PHASE_SPLITTER = ":";

	public static final int HARMONIC_COMPONENT_MIN = 2;
	public static int HARMONIC_COMPONENT_MAX = 22;
	
	//public static final int TOTAL_NO_OF_SUPPORTED_RACK = 12;
	
	
	
	public static final String TEST_PROFILE_WARMUP = "Warmup";
	public static final String TEST_PROFILE_NOLOAD = "NoLoad";
	public static final String TEST_PROFILE_STA = "STA";
	public static final String TEST_PROFILE_CONSTANT_TEST = "ConstantTest";
	public static final String TEST_PROFILE_ACCURACY = "Accuracy";
	public static final String TEST_PROFILE_INFLUENCE_VOLT = "InfluenceVolt";
	public static final String TEST_PROFILE_INFLUENCE_FREQ = "InfluenceFreq";
	public static final String TEST_PROFILE_INFLUENCE_HARMONIC = "InfluenceHarmonic";
	public static final String TEST_PROFILE_CUT_NUETRAL = "CuttingNuetral";
	public static final String TEST_PROFILE_VOLTAGE_UNBALANCE = "VoltageUnbalance";
	public static final String TEST_PROFILE_PHASE_REVERSAL = "PhaseReversal";
	public static final String TEST_PROFILE_CUSTOM_TEST = "CustomTest";
	public static final String TEST_PROFILE_DUT_COMMAND = "DutCommand";
	public static final String TEST_PROFILE_REPEATABILITY = "Repeatability";
	public static final String TEST_PROFILE_SELF_HEATING = "SelfHeating";
	public static final String TEST_PROFILE_UNBALANCED_LOAD = "UnbalancedLoad";
	public static final String METER_PROFILE_REPORT = "MeterProfile";
	
	
	public static final int VOLTAGE_VARIATION_LIST_ACCEPTED_COUNT = 11;
	public static final int FREQUENCY_VARIATION_LIST_ACCEPTED_COUNT = 11;
	
	public static final int BNC_OUTPUTPORT_MAX = 3;
	
	public final static long NUMBER_OF_SECONDS_IN_A_DAY= 86400;// seconds in a day
	
	public static final String EXECUTION_STATUS_RERUN = "Re-Run";
	public static final String EXECUTION_STATUS_COMPLETED = "Completed";
	public static final String EXECUTION_STATUS_TIMED_OUT = "Time out";
	public static final String EXECUTION_STATUS_NOT_STARTED = "Not Started";
	public static final String EXECUTION_STATUS_INPROGRESS = "In Progress";
	public static final String EXECUTION_STATUS_ABORTED = "Aborted";
	public static final String EXECUTION_STATUS_SKIPPED = "Skipped";
	public static final String EXECUTION_STATUS_NOT_EXECUTED = "Not Executed";
	public static final String EXECUTION_STATUS_STARTED = "Started";
	
	public static final String LIVE_TABLE_EXECUTION_STATUS_DISPLAY_HEADER = "Status";
	public static final int LIVE_TABLE_EXECUTION_STATUS_ID = 1111;
	
	public static final String STA_IB_DB_SAVING_FORMAT = "0.0####";
	
	public static int PULSE_AVERAGE_MIN_VALUE = 1;
	
	public static final String UAC_LOGIN_SCREEN = "Login";
	public static final String UAC_PROJECT_SCREEN = "Project";
	public static final String UAC_DEPLOY_SCREEN = "Deploy";
	public static final String UAC_TEST_RUN_SCREEN = "TestRun";
	public static final String UAC_REPORT_SCREEN = "Report";
	public static final String UAC_METER_PROFILE_SCREEN = "MeterProfile";
	public static final String UAC_SETTINGS_SCREEN = "Settings";
	public static final String UAC_MANUAL_MODE_SCREEN = "ManualMode";
	//public static final String UAC_PLC_SCREEN = "PLC";
	public static final String UAC_ADMIN_SCREEN = "Admin";	
	public static final String UAC_DEVICE_SETTINGS_SCREEN = "DeviceSettings";
	public static final String UAC_STABILITY_SETTINGS_SCREEN = "StabilitySettings";
	public static final String UAC_REPORT_CONFIGURATION_SCREEN = "ReportConfiguration";
	public static final String UAC_REPORT_PROFILE_CONFIG_SCREEN = "ReportProfileConfig";
	public static final String UAC_BACKUP_SETTINGS_SCREEN = "BackupSettings";
	
	public static final String DISPLAY_CALIBRATION_VOLTAGE_RESOLUTION = "%03.03f";
	public static final String DISPLAY_CALIBRATION_CURRENT_RESOLUTION = "%02.05f";
	
	public static final String DISPLAY_VOLTAGE_RESOLUTION = "%03.02f";
	public static final String DISPLAY_CURRENT_RESOLUTION_LOW = "%02.04f";
	public static final String DISPLAY_CURRENT_RESOLUTION_HIGH = "%02.02f";
	public static final String DISPLAY_PHASE_ANGLE_PF_RESOLUTION = "%.04f";
	public static final String DISPLAY_PHASE_ANGLE_DEGREE_RESOLUTION = "%02.02f";
	
	
	public static final String FEEDBACK_CONTROL_CURRENT_RESOLUTION_LOW = "%02.04f";
	public static final String FEEDBACK_CONTROL_CURRENT_RESOLUTION_HIGH = "%02.03f";
	
	
	public static final String DISPLAY_FREQ_RESOLUTION = "%02.03f";
	public static final String DISPLAY_POWER_RESOLUTION = "%.02f";
	public static final String DISPLAY_ENERGY_RESOLUTION = "%.4f";
	
	public static final String DEFAULT_NO_OF_PULSES = "10";
	
	public static final String LABEL_DISPLAY_THREE_PHASE_STAR = "L - N";
	public static final String LABEL_DISPLAY_THREE_PHASE_DELTA = "L - L";
	
	public static final int CURRENT_PERCENT_CONVERTION_FACTOR = 100;
	
	public static final ArrayList<String> SCREEN_NAME_LIST = new ArrayList<String>(Arrays.asList(UAC_LOGIN_SCREEN,
			UAC_PROJECT_SCREEN,UAC_DEPLOY_SCREEN,UAC_TEST_RUN_SCREEN,
			UAC_REPORT_SCREEN,UAC_METER_PROFILE_SCREEN,UAC_SETTINGS_SCREEN,
			UAC_MANUAL_MODE_SCREEN,
			//UAC_PLC_SCREEN,
			UAC_DEVICE_SETTINGS_SCREEN,UAC_STABILITY_SETTINGS_SCREEN,
			UAC_REPORT_CONFIGURATION_SCREEN,
			UAC_REPORT_PROFILE_CONFIG_SCREEN,
			UAC_BACKUP_SETTINGS_SCREEN,
			UAC_ADMIN_SCREEN ));	
	

	
	//public static final Float LDU_AVERAGE_INIT_DATA = 211.0f;
	
	//public static final String  PROJECT_NAME_SEPERATOR = "_|_";
	
	public static void powerSourceInit(){
		if(!ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED){
			
			String[] singlePhaseMeterType = {"Single Phase Active","Single Phase Reactive"};
			MeterType = singlePhaseMeterType;
			DefaultMeterType = "Single Phase Active";
		}
	}

}
