package com.tasnetwork.calibration.energymeter.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.testprofiles.TestProfileType;

public class ConstantReport {

	
	//public static final  String DUT_KEY = "MUT";
	
	public static final String RESULT_DATA_TYPE_ERROR_VALUE = "ERROR";
	public static final String RESULT_DATA_TYPE_STA_TIME = "STA_TIME";
	public static final String RESULT_DATA_TYPE_PULSE_COUNT = "PULSE_CNT";
	public static final String RESULT_DATA_TYPE_FAILURE_REASON = "FAIL_RSN";
	public static final String RESULT_DATA_TYPE_INITIAL_KWH = "I_KWH";
	public static final String RESULT_DATA_TYPE_FINAL_KWH = "F_KWH";
	public static final String RESULT_DATA_TYPE_REF_INITIAL_KWH = "REF_I_KWH";
	public static final String RESULT_DATA_TYPE_REF_FINAL_KWH = "REF_F_KWH";
	public static final String RESULT_DATA_TYPE_REF_DIFF_KWH = "REF_DIFF_KWH";
	public static final String RESULT_DATA_TYPE_CONST_ERROR_VALUE = "CALC_ERROR";
	public static final String RESULT_DATA_TYPE_DEVICE_NAME = "DEVICE_NAME";
	public static final String RESULT_DATA_TYPE_CTR = "CTR";
	public static final String RESULT_DATA_TYPE_PTR = "PTR";
	public static final String RESULT_DATA_TYPE_AVG = "AVG";
	public static final String RESULT_DATA_TYPE_EXECUTION_STATUS = "EXECUTE_STATUS";
	
	
	//public static final String RESULT_DATA_TYPE_ONE_PULSE_DURATION_IN_SEC = "ONE_PULSE_TIME";
	
	public static final String RESULT_DATA_TYPE_TARGET_VOLT = "TGT_VOLT";
	public static final String RESULT_DATA_TYPE_TARGET_CURRENT = "TGT_CURR";
	public static final String RESULT_DATA_TYPE_TARGET_PF = "TGT_PF";
	public static final String RESULT_DATA_TYPE_TARGET_FREQ = "TGT_FREQ";
	public static final String RESULT_DATA_TYPE_TARGET_POWER = "TGT_PWR";
	public static final String RESULT_DATA_TYPE_TARGET_APPARAENT_ENERGY = "TGT_APP_ENGY";
	public static final String RESULT_DATA_TYPE_TARGET_ACTIVE_ENERGY = "TGT_ACTV_ENGY";
	public static final String RESULT_DATA_TYPE_TARGET_REACTIVE_ENERGY = "TGT_RCTV_ENGY";
	
	public static final String RESULT_DATA_TYPE_ACTUAL_REFSTD_VOLT = "ACT_RSM_VOLT";
	public static final String RESULT_DATA_TYPE_ACTUAL_REFSTD_CURRENT = "ACT_RSM_CURR";
	public static final String RESULT_DATA_TYPE_ACTUAL_REFSTD_PF = "ACT_RSM_PF";
	public static final String RESULT_DATA_TYPE_ACTUAL_REFSTD_FREQ = "ACT_RSM_FREQ";
	public static final String RESULT_DATA_TYPE_ACTUAL_REFSTD_POWER = "ACT_RSM_PWR";
	public static final String RESULT_DATA_TYPE_ACTUAL_REFSTD_APPARAENT_ENERGY = "ACT_RSM_APP_ENGY";
	public static final String RESULT_DATA_TYPE_ACTUAL_REFSTD_ACTIVE_ENERGY = "ACT_RSM_ACTV_ENGY";
	public static final String RESULT_DATA_TYPE_ACTUAL_REFSTD_REACTIVE_ENERGY = "ACT_RSM_RCTV_ENGY";
	
	
	public static String RESULT_DATA_TYPE_ACTUAL_DUT_VOLT = "ACT_"+ConstantAppConfig.DUT_DISPLAY_KEY+"_VOLT";
	public static String RESULT_DATA_TYPE_ACTUAL_DUT_CURRENT = "ACT_"+ConstantAppConfig.DUT_DISPLAY_KEY+"_CURR";
	public static String RESULT_DATA_TYPE_ACTUAL_DUT_PF = "ACT_"+ConstantAppConfig.DUT_DISPLAY_KEY+"_PF";
	public static String RESULT_DATA_TYPE_ACTUAL_DUT_FREQ = "ACT_"+ConstantAppConfig.DUT_DISPLAY_KEY+"_FREQ";
	public static String RESULT_DATA_TYPE_ACTUAL_DUT_POWER = "ACT_"+ConstantAppConfig.DUT_DISPLAY_KEY+"_PWR";
	public static String RESULT_DATA_TYPE_ACTUAL_DUT_APPARAENT_ENERGY = "ACT_"+ConstantAppConfig.DUT_DISPLAY_KEY+"_APP_ENGY";
	public static String RESULT_DATA_TYPE_ACTUAL_DUT_ACTIVE_ENERGY = "ACT_"+ConstantAppConfig.DUT_DISPLAY_KEY+"_ACTV_ENGY";
	public static String RESULT_DATA_TYPE_ACTUAL_DUT_REACTIVE_ENERGY = "ACT_"+ConstantAppConfig.DUT_DISPLAY_KEY+"_RCTV_ENGY";
	
	public static final String RESULT_DATA_TYPE_ALLOWED_UPPER_LIMIT = "ALWD_UP_LIMIT";
	public static final String RESULT_DATA_TYPE_ALLOWED_LOWER_LIMIT = "ALWD_LOW_LIMIT";

	public static final String RESULT_DATA_TYPE_OPERATION_OUTPUT = "OPER_OP";
	
	public static final String  RESULT_EXECUTION_MODE_MAIN_CT = "MCT";
	public static final String  RESULT_EXECUTION_MODE_NEUTRAL_CT = "NCT";
	public static final String  RESULT_EXECUTION_MODE_MCT_NCT_OFF = "OFF";
	public static final String  RESULT_EXECUTION_MODE_MCT_NCT_UNDEFINED = "UDF";
	
/*	public static final String  RESULT_EXECUTION_MODE_ENERYGY_IMPORT = "IMPORT";
	public static final String  RESULT_EXECUTION_MODE_ENERYGY_EXPORT = "EXPORT";*/
	
	public static final String PARAMETER_TYPE_VOLTAGE = "Voltage";
	public static final String PARAMETER_TYPE_CURRENT = "Current";
	public static final String PARAMETER_TYPE_PF = "PF";
	public static final String PARAMETER_TYPE_FREQUENCY = "Frequency";
	public static final String PARAMETER_TYPE_HARMONICS = "Harmonics";		

	public static final String EXTENSION_TYPE_VOLTAGE_U = "U";	
	public static final String EXTENSION_TYPE_CURRENT_IB = "Ib";	
	public static final String EXTENSION_TYPE_CURRENT_IMAX = "Imax";	
	public static final String EXTENSION_TYPE_PHASE_UPF = "UPF";	
	public static final String EXTENSION_TYPE_PHASE_L = "L";	
	public static final String EXTENSION_TYPE_PHASE_C = "C";
	
	public static final String EXTENSION_TYPE_ENERGY_ACTIVE = "kWh";	
	public static final String EXTENSION_TYPE_ENERGY_REACTIVE = "kVARh";	
	public static final String EXTENSION_TYPE_ENERGY_APPARENT = "kVAh";	
	
	public static final String HEADER_TYPE_VOLTAGE = "Voltage";	
	public static final String HEADER_TYPE_CURRENT = "Current";	
	public static final String HEADER_TYPE_PF = "PF";	
	public static final String HEADER_TYPE_FREQUENCY = "Frequency";	
	public static final String HEADER_TYPE_HARMONICS = "Harmonics";	
	public static final String HEADER_TYPE_REFERENCE_VALUE = "Reference Value";	

	public static final String EXCEL_ALPHA = "Alpha";	
	public static final String EXCEL_BETA = "Beta";		
	public static final String EXCEL_GAMMMA = "Gamma";	
	
	public static final String REPORT_POPULATE_WFR = "WFR";
	public static final String RESULT_STATUS_PASS = "P ";
	public static final String RESULT_STATUS_FAIL = "F ";
	public static final String RESULT_STATUS_UNDEFINED = "N ";
	
	public static final String REPORT_POPULATE_PASS = "Pass";
	public static final String REPORT_POPULATE_FAIL = "Fail";
	public static final String REPORT_POPULATE_UNDEFINED = "Error";
	
	//public static  MultiValuedMap<String,String> TEST_TYPE_ALIAS_HASH_MAP = new ArrayListValuedHashMap<String,String>();
	public static  HashMap<String,String> TEST_TYPE_ALIAS_HASH_MAP = new LinkedHashMap<String,String>();

	public static ArrayList<String> REPORT_TEST_TYPES = new ArrayList<>();
	public static ArrayList<String> REPORT_TEST_TYPES_DISPLAY = new ArrayList<>();
	public static String REPORT_TEST_TYPES_NONE = "None";

	
	public static String SAVE_FILE_LOCATION =  "C:\\TAS_Network\\Procal_Excel\\";
	public static String INPUT_TEMPLATE_LOCATION =  "C:\\TAS_Network\\Procal_Excel\\";
	
	public static ArrayList<Integer> SELF_HEAT_TEMPL_ROWS = new ArrayList<Integer>();
	public static ArrayList<Integer> SELF_HEAT_TEMPL_METER_COLS = new ArrayList<Integer>();
	public static ArrayList<Integer> SELF_HEAT_TEMPL_TEST_COLS = new ArrayList<Integer>();
	public static int SELF_HEAT_TEMPL_NO_OF_TESTS = 0;
	public static String SELF_HEAT_TEMPL_VOLTAGE = "";
	public static ArrayList<String> SELF_HEAT_TEMPL_CURRENTS = new ArrayList<String>();
	public static ArrayList<String> SELF_HEAT_TEMPL_PFS = new ArrayList<String>();
	public static String SELF_HEAT_TEMPL_FILE_LOCATION = INPUT_TEMPLATE_LOCATION + "Self Heating.xls";
	

	public static ArrayList<Integer> REP_TEMPL_ROWS = new ArrayList<Integer>();
	public static ArrayList<Integer> REP_TEMPL_METER_COLS = new ArrayList<Integer>();
	public static int REP_TEMPL_TEST_COL = 0;
	public static int REP_TEMPL_NO_OF_TESTS = 0;
	public static String REP_TEMPL_VOLTAGE = "";
	public static ArrayList<String> REP_TEMPL_CURRENTS = new ArrayList<String>();
	public static String REP_TEMPL_PF = "";
	public static String REP_TEMP_FILE_LOCATION = INPUT_TEMPLATE_LOCATION + "REPEATABILITY.xls";
	public static String ALL_PROJECT_REPORT_FILENAME =  "ProjectReport.xls";


	public static ArrayList<Integer> FREQ_TEMPL_ROWS = new ArrayList<Integer>();
	public static ArrayList<Integer> FREQ_TEMPL_METER_COLS = new ArrayList<Integer>();
	public static ArrayList<Integer> FREQ_TEMPL_DEF_FREQ_COLS = new ArrayList<Integer>();

	public static String FREQ_TEMPL_VOLTAGE = "";
	public static ArrayList<String> FREQ_TEMPL_CURRENTS = new ArrayList<String>();
	public static ArrayList<String> FREQ_TEMPL_PFS = new ArrayList<String>();
	public static ArrayList<String> FREQ_TEMPL_FREQUENCIES = new ArrayList<String>();
	public static String FREQ_TEMPL_DEFAULT_FREQ = "";
	public static String FREQ_TEMPL_FILE_LOCATION = INPUT_TEMPLATE_LOCATION + "Frequency Variation.xls";


	public static ArrayList<Integer> VV_TEMPL_ROWS = new ArrayList<Integer>();
	public static ArrayList<Integer> VV_TEMPL_METER_COLS = new ArrayList<Integer>();
	public static ArrayList<Integer> VV_TEMPL_DEF_VOLT_COLS = new ArrayList<Integer>();

	public static ArrayList<String> VV_TEMPL_VOLTS = new ArrayList<>();
	public static ArrayList<String> VV_TEMPL_CURRENTS = new ArrayList<>();
	public static ArrayList<String> VV_TEMPL_PFS = new ArrayList<>();
	public static String VV_TEMPL_DEFAULT_VOLT = "";
	public static String VV_TEMPL_FILE_LOCATION = INPUT_TEMPLATE_LOCATION + "Voltage Variation.xls";
	

	//public static int ACC_TEMPL_ROW = 0;
	//public static int ACC_TEMPL_METER_COL = 0;
	public static ArrayList<Integer> ACC_TEMPL_ROW = new ArrayList<Integer>();
	public static ArrayList<Integer> ACC_TEMPL_METER_COL =new ArrayList<Integer>();
	public static ArrayList<Integer> ACC_TEMPL_DEF_I_COLS = new ArrayList<Integer>();
	public static String ACC_TEMPL_VOLT = "";
	public static ArrayList<String> ACC_TEMPL_CURRENTS = new ArrayList<String>();
	public static ArrayList<String> ACC_TEMPL_PFS = new ArrayList<String>();
	public static String ACC_TEMPL_FILE_LOCATION = INPUT_TEMPLATE_LOCATION + "-Accuracy.xls";
	public static int ACC_MAX_NO_OF_PAGES_IN_REPORT = 2;
	//public static int ACC_NO_OF_PAGES_IN_REPORT = 2;
	//public static int ACC_NO_OF_PF_VARIANT_IN_EACH_PAGE = 3;


	public static int RPS_TEMPL_ROW = 0;
	public static int RPS_TEMPL_METER_COL = 0;
	public static ArrayList<Integer> RPS_TEMPL_NORMAL_REV_COL = new ArrayList<Integer>();
	public static String RPS_TEMPL_VOLTAGE = "";
	public static String RPS_TEMPL_CURRENT = "";
	public static String RPS_TEMPL_PF = "";
	public static final String RPS_FILTER_TESTTYPE = "RPS";
	public static String RPS_TEMPL_FILE_LOCATION = INPUT_TEMPLATE_LOCATION + "Reverse Phase Sequence.xls";
	

	public static ArrayList<Integer> HARM_TEMPL_ROWS = new ArrayList<Integer>();
	public static ArrayList<Integer> HARM_TEMPL_METER_COLS = new ArrayList<Integer>();
	public static ArrayList<Integer> HARM_TEMPL_PHASE_COLS = new ArrayList<Integer>();
	public static String HARM_TEMPL_VOLTAGE = "";
	public static ArrayList<String> HARM_TEMPL_CURRENTS = new ArrayList<String>();
	public static String HARM_TEMPL_PF = "";
	public static ArrayList<String> HARM_TEMPL_HARM_TIMES = new ArrayList<String>();

	public static final String HARM_FILTER_TESTTYPE = "HARM";
	public static String HARM_TEMPL_FILE_LOCATION = INPUT_TEMPLATE_LOCATION + "Harmonic test.xls";

	public static int VU_TEMPL_ROW = 0;
	public static int VU_TEMPL_METER_COL = 0;
	public static int VU_TEMPL_DEF_VOLT_COL = 0;
	public static String VU_TEMPL_DEF_VOLT = "";
	public static ArrayList<String> VU_TEMPL_VOLTAGES = new ArrayList<String>();
	public static String VU_TEMPL_CURRENT = "";
	public static String VU_TEMPL_PF = "";
	public static String VU_TEMPL_FILE_LOCATION = INPUT_TEMPLATE_LOCATION + "Voltage Unbalance 3P4W.xls";


	public static ArrayList<Integer> UNBALANCED_LOAD_TEMPL_ROWS = new ArrayList<Integer>();
	public static ArrayList<Integer> UNBALANCED_LOAD_TEMPL_METER_COLS = new ArrayList<Integer>();

	public static ArrayList<Integer> UNBALANCED_LOAD_TEMPL_COLS = new ArrayList<Integer>();
	public static  String UNBALANCED_LOAD_TEMPL_DEF_VOLT = "ABC:100U";
	public static final String UNBALANCED_LOAD_TEMPL_DEF_CURRENT = "1.0Ib";
	//public static final String ACC_2_TEMPL_R_VOLT = "A:100U";
	//public static final String ACC_2_TEMPL_Y_VOLT = "B:100U";
	//public static final String ACC_2_TEMPL_B_VOLT = "C:100U";
	public static  ArrayList<String> UNBALANCED_LOAD_TEMPL_VOLTAGES = new ArrayList<String>(Arrays.asList("A:100U", "B:100U", "C:100U"));
	public static ArrayList<String> UNBALANCED_LOAD_TEMPL_CURRENTS = new ArrayList<String>();
	public static ArrayList<String> UNBALANCED_LOAD_TEMPL_PFS = new ArrayList<String>();
	public static String UNBALANCED_LOAD_TEMPL_FILE_LOCATION = INPUT_TEMPLATE_LOCATION + "Unbalanced Load 3P4W.xls";

	public static int CONST_TEMPL_ROW = 10;
	public static int CONST_TEMPL_METER_COL = 0;
	public static ArrayList<Integer> CONST_TEMPL_CONST_COLS = new ArrayList<Integer>();
	public static String CONST_TEMPL_POWER = "";
	public static String CONST_TEMPL_VOLTAGE = "";
	public static String CONST_TEMPL_CURRENT = "";
	public static String CONST_TEMPL_PF = "";
	public static String CONST_TEMPL_FILE_LOCATION = INPUT_TEMPLATE_LOCATION + "Meter Constant.xls";


	public static int CREEP_TEMPL_ROW = 0;
	public static int CREEP_TEMPL_METER_COL = 0;
	public static ArrayList<Integer> CREEP_TEMPL_CREEP_COLS = new ArrayList<Integer>();
	public static String CREEP_TEMPL_VOLTAGE = "";
	public static String CREEP_TEMPL_FILE_LOCATION = INPUT_TEMPLATE_LOCATION + "No load test.xls";

	public static int STA_TEMPL_ROW = 14;
	public static int STA_TEMPL_METER_COL = 1;
	public static ArrayList<Integer> STA_TEMPL_STA_COLS = new ArrayList<Integer>();
	public static String STA_TEMPL_VOLTAGE = "100U";
	public static String STA_TEMPL_CURRENT = "0.5Ib";
	public static String STA_TEMPL_FILE_LOCATION = INPUT_TEMPLATE_LOCATION + "start_Current.xls";
	
	public static final String REPORT_SELECTED_ALL_METERS = "All Meters";
	public static String METER_PROFILE_REPORT_TEMPL_FILE_LOCATION = INPUT_TEMPLATE_LOCATION + "MeterProfile.xlsx";
	public static String CONSOLIDATED_PDF_REPORT_FILE_NAME = "Report.pdf";
	
	public static final String REPORT_DATA_POPULATE_VERTICAL = "Vertical";
	public static final String REPORT_DATA_POPULATE_HORIZONTAL = "Horizontal";
	
	public static final int FREQ_REF_SKIP_COL_COUNT = 1;
	public static final int FREQ_SKIP_COL_COUNT = 1;//2;
	public static final int VV_REF_SKIP_COL_COUNT = 1;
	public static final int VV_SKIP_COL_COUNT = 1;//2;
	public static final int ACC_SKIP_COL_COUNT = 1;
	public static final int UNBALANCELOAD_SKIP_COL_COUNT = 1;
	public static final int HARM_WO_SKIP_COL_COUNT = 1;
	public static final int HARM_W_SKIP_COL_COUNT = 2;
	public static final int VU_REF_SKIP_COL_COUNT = 1;
	public static final int VU_SKIP_COL_COUNT = 2;
	
	public static void ConstReportInit(){
		ApplicationLauncher.logger.debug("ConstReportInit: Entry1");
		if(ProcalFeatureEnable.PHASE_DISPLAY_ENABLE_FEATURE){
			ApplicationLauncher.logger.debug("ConstReportInit: Entry2");
			UNBALANCED_LOAD_TEMPL_DEF_VOLT = ConstantApp.FIRST_PHASE_DISPLAY_NAME+ConstantApp.SECOND_PHASE_DISPLAY_NAME+ConstantApp.THIRD_PHASE_DISPLAY_NAME+":100U";
			UNBALANCED_LOAD_TEMPL_VOLTAGES = new ArrayList<String>(Arrays.asList(ConstantApp.FIRST_PHASE_DISPLAY_NAME+":100U",
																				ConstantApp.SECOND_PHASE_DISPLAY_NAME+":100U",
																				ConstantApp.THIRD_PHASE_DISPLAY_NAME+":100U"));
		}

		MapReportTestTypesandReportDisplay();

		/*ApplicationLauncher.logger.info("REPORT_TEST_TYPES: Mapping Start");
		ApplicationLauncher.logger.info("REPORT_TEST_TYPES: REPORT_TEST_TYPES.size():"+REPORT_TEST_TYPES.size());
		ApplicationLauncher.logger.info("REPORT_TEST_TYPES: REPORT_TEST_TYPES1.size():"+REPORT_TEST_TYPES1.size());
		ApplicationLauncher.logger.info("REPORT_TEST_TYPES: REPORT_TEST_TYPES_DISPLAY.size():"+REPORT_TEST_TYPES_DISPLAY.size());
		ApplicationLauncher.logger.info("REPORT_TEST_TYPES: REPORT_TEST_TYPES_DISPLAY1.size():"+REPORT_TEST_TYPES_DISPLAY1.size());

		for (int i = 0; i < (REPORT_TEST_TYPES1.size()); i++) {
			ApplicationLauncher.logger.info("REPORT_TEST_TYPES1:"+REPORT_TEST_TYPES1.get(i));
			if(!REPORT_TEST_TYPES1.get(i).equals(REPORT_TEST_TYPES.get(i))){
				ApplicationLauncher.logger.info("REPORT_TEST_TYPES:not matching:"+REPORT_TEST_TYPES.get(i));
				//ApplicationLauncher.logger.info("REPORT_TEST_TYPES:");
			}
			
			ApplicationLauncher.logger.info("REPORT_TEST_TYPES_DISPLAY1:"+REPORT_TEST_TYPES_DISPLAY1.get(i));
			if(!REPORT_TEST_TYPES_DISPLAY1.get(i).equals(REPORT_TEST_TYPES_DISPLAY.get(i))){
				ApplicationLauncher.logger.info("REPORT_TEST_TYPES_DISPLAY: not matching:"+REPORT_TEST_TYPES_DISPLAY.get(i));
			}
		}
		ApplicationLauncher.logger.info("REPORT_TEST_TYPES: Mapping End");*/

	}
	
	public static void MapReportTestTypesandReportDisplay(){
		String TestCaseType = "";
		for (int i = 0; i < (TestProfileType.values().length); i++) {
			boolean IsFeatureEnabledInConfig = false;
			try{
				IsFeatureEnabledInConfig = ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.getBoolean(TestProfileType.values()[i].toString());
			}catch (Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("ProjectController: initialize: Exception :"+e.getMessage());

			}
			if(IsFeatureEnabledInConfig){
				TestCaseType = TestProfileType.values()[i].toString();
				switch (TestCaseType) {



				case ConstantApp.TEST_PROFILE_STA:
					REPORT_TEST_TYPES.add(TestCaseType);
					REPORT_TEST_TYPES_DISPLAY.add(ConstantApp.DISPLAY_TC_TITLE_STARTING_CURRENT);
					break;


				case	ConstantApp.TEST_PROFILE_WARMUP:
					//REPORT_TEST_TYPES_DISPLAY1.add();
					break;


				case ConstantApp.TEST_PROFILE_NOLOAD:
					REPORT_TEST_TYPES.add(TestCaseType);
					REPORT_TEST_TYPES_DISPLAY.add(ConstantApp.DISPLAY_TC_TITLE_NOLOADTEST);
					break;


				case ConstantApp.TEST_PROFILE_ACCURACY:
					REPORT_TEST_TYPES.add(TestCaseType);
					REPORT_TEST_TYPES_DISPLAY.add(ConstantApp.DISPLAY_TC_TITLE_ACCURACY);
					break;


				case ConstantApp.TEST_PROFILE_INFLUENCE_VOLT:
					REPORT_TEST_TYPES.add(TestCaseType);
					REPORT_TEST_TYPES_DISPLAY.add(ConstantApp.DISPLAY_TC_TITLE_INF_VOLTAGE);
					break;


				case ConstantApp.TEST_PROFILE_INFLUENCE_FREQ:
					REPORT_TEST_TYPES.add(TestCaseType);
					REPORT_TEST_TYPES_DISPLAY.add(ConstantApp.DISPLAY_TC_TITLE_INF_FREQUENCY);
					break;


				case ConstantApp.TEST_PROFILE_INFLUENCE_HARMONIC:
					REPORT_TEST_TYPES.add(TestCaseType);
					REPORT_TEST_TYPES_DISPLAY.add(ConstantApp.DISPLAY_TC_TITLE_INF_HARMONICS);
					break;


				case ConstantApp.TEST_PROFILE_CUT_NUETRAL:
					//REPORT_TEST_TYPES_DISPLAY1.add();
					break;


				case ConstantApp.TEST_PROFILE_VOLTAGE_UNBALANCE:
					REPORT_TEST_TYPES.add(TestCaseType);
					REPORT_TEST_TYPES_DISPLAY.add(ConstantApp.DISPLAY_TC_TITLE_INF_VOLT_UNBALANCE);
					break;


				case ConstantApp.TEST_PROFILE_PHASE_REVERSAL:
					REPORT_TEST_TYPES.add(TestCaseType);
					REPORT_TEST_TYPES_DISPLAY.add(ConstantApp.DISPLAY_TC_TITLE_PHASE_REVERSAL);
					break;



				case ConstantApp.TEST_PROFILE_REPEATABILITY:
					REPORT_TEST_TYPES.add(TestCaseType);
					REPORT_TEST_TYPES_DISPLAY.add(ConstantApp.DISPLAY_TC_TITLE_REPEATABLITY);
					break;


				case ConstantApp.TEST_PROFILE_SELF_HEATING:
					REPORT_TEST_TYPES.add(TestCaseType);
					REPORT_TEST_TYPES_DISPLAY.add(ConstantApp.DISPLAY_TC_TITLE_SELF_HEATING);
					break;


				case ConstantApp.TEST_PROFILE_CONSTANT_TEST:
					REPORT_TEST_TYPES.add(TestCaseType);
					REPORT_TEST_TYPES_DISPLAY.add(ConstantApp.DISPLAY_TC_TITLE_CONST_TEST);
					break;


				case ConstantApp.TEST_PROFILE_CUSTOM_TEST:
					//REPORT_TEST_TYPES_DISPLAY1.add();
					break;

				default:
					break;
				}
			}
		}
		if(ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED){
			REPORT_TEST_TYPES.add(ConstantApp.TEST_PROFILE_UNBALANCED_LOAD);
			REPORT_TEST_TYPES_DISPLAY.add("Unbalanced Load");
		}
		if(ConstantAppConfig.METER_PROFILE_REPORT_ENABLED){
			REPORT_TEST_TYPES.add(ConstantApp.METER_PROFILE_REPORT);
			REPORT_TEST_TYPES_DISPLAY.add("Meter Profile");
		}
		
		
		TEST_TYPE_ALIAS_HASH_MAP.put(ConstantApp.DISPLAY_TC_TITLE_WARMUP, ConstantApp.WARMUP_ALIAS_NAME);
		TEST_TYPE_ALIAS_HASH_MAP.put(ConstantApp.DISPLAY_TC_TITLE_NOLOADTEST , ConstantApp.CREEP_ALIAS_NAME);
		TEST_TYPE_ALIAS_HASH_MAP.put(ConstantApp.DISPLAY_TC_TITLE_STARTING_CURRENT, ConstantApp.STA_ALIAS_NAME);
		TEST_TYPE_ALIAS_HASH_MAP.put(ConstantApp.DISPLAY_TC_TITLE_CONST_TEST, ConstantApp.CONST_TEST_ALIAS_NAME);
		TEST_TYPE_ALIAS_HASH_MAP.put(ConstantApp.DISPLAY_TC_TITLE_ACCURACY, ConstantApp.ACCURACY_ALIAS_NAME);
		TEST_TYPE_ALIAS_HASH_MAP.put(ConstantApp.DISPLAY_TC_TITLE_INF_VOLTAGE, ConstantApp.VOLTAGE_ALIAS_NAME);
		TEST_TYPE_ALIAS_HASH_MAP.put(ConstantApp.DISPLAY_TC_TITLE_INF_FREQUENCY, ConstantApp.FREQUENCY_ALIAS_NAME);
		//TEST_TYPE_ALIAS_HASH_MAP.put(ConstantApp.DISPLAY_TC_TITLE_INF_HARMONICS, ConstantApp.HARMONIC_WITHOUT_ALIAS_NAME);
		//TEST_TYPE_ALIAS_HASH_MAP.put(ConstantApp.DISPLAY_TC_TITLE_INF_HARMONICS, ConstantApp.HARMONIC_INPHASE_ALIAS_NAME);
		//TEST_TYPE_ALIAS_HASH_MAP.put(ConstantApp.DISPLAY_TC_TITLE_INF_HARMONICS, ConstantApp.HARMONIC_OUTOFPHASE_ALIAS_NAME);
		TEST_TYPE_ALIAS_HASH_MAP.put(ConstantApp.DISPLAY_TC_TITLE_CUT_NUETRAL, ConstantApp.CUTNEUTRAL_ALIAS_NAME);
		TEST_TYPE_ALIAS_HASH_MAP.put(ConstantApp.DISPLAY_TC_TITLE_INF_VOLT_UNBALANCE, ConstantApp.VOLT_UNBALANCE_ALIAS_NAME);
		//TEST_TYPE_ALIAS_HASH_MAP.put(ConstantApp.DISPLAY_TC_TITLE_PHASE_REVERSAL, ConstantApp.PHASEREVERSAL_NORMAL_ALIAS_NAME);
		TEST_TYPE_ALIAS_HASH_MAP.put(ConstantApp.DISPLAY_TC_TITLE_PHASE_REVERSAL, ConstantApp.PHASEREVERSAL_REV_ALIAS_NAME);
		//TEST_TYPE_ALIAS_HASH_MAP.put(ConstantApp.DISPLAY_TC_TITLE_REPEATABLITY, ConstantApp.REPEATABILITY_START_TEST_ALIAS_NAME);
		TEST_TYPE_ALIAS_HASH_MAP.put(ConstantApp.DISPLAY_TC_TITLE_REPEATABLITY, ConstantApp.REPEATABILITY_ALIAS_NAME);
		//TEST_TYPE_ALIAS_HASH_MAP.put(ConstantApp.DISPLAY_TC_TITLE_REPEATABLITY, ConstantApp.REPEATABILITY_END_TEST_ALIAS_NAME);
		//TEST_TYPE_ALIAS_HASH_MAP.put(ConstantApp.DISPLAY_TC_TITLE_SELF_HEATING, ConstantApp.SELF_HEATING_START_TEST_ALIAS_NAME);
		TEST_TYPE_ALIAS_HASH_MAP.put(ConstantApp.DISPLAY_TC_TITLE_SELF_HEATING, ConstantApp.SELF_HEATING_ALIAS_NAME);
		//TEST_TYPE_ALIAS_HASH_MAP.put(ConstantApp.DISPLAY_TC_TITLE_SELF_HEATING, ConstantApp.SELF_HEATING_END_TEST_ALIAS_NAME);
		TEST_TYPE_ALIAS_HASH_MAP.put(ConstantApp.DISPLAY_TC_TITLE_CUSTOM_TEST, ConstantApp.CUSTOM_TEST_ALIAS_NAME);
		TEST_TYPE_ALIAS_HASH_MAP.put(ConstantApp.DISPLAY_TC_TITLE_DUT_COMMAND, ConstantApp.DUT_COMMAND_TEST_ALIAS_NAME);
		
		
		RESULT_DATA_TYPE_ACTUAL_DUT_VOLT = "ACT_"+ConstantAppConfig.DUT_DISPLAY_KEY+"_VOLT";
		RESULT_DATA_TYPE_ACTUAL_DUT_CURRENT = "ACT_"+ConstantAppConfig.DUT_DISPLAY_KEY+"_CURR";
		RESULT_DATA_TYPE_ACTUAL_DUT_PF = "ACT_"+ConstantAppConfig.DUT_DISPLAY_KEY+"_PF";
		RESULT_DATA_TYPE_ACTUAL_DUT_FREQ = "ACT_"+ConstantAppConfig.DUT_DISPLAY_KEY+"_FREQ";
		RESULT_DATA_TYPE_ACTUAL_DUT_POWER = "ACT_"+ConstantAppConfig.DUT_DISPLAY_KEY+"_PWR";
		RESULT_DATA_TYPE_ACTUAL_DUT_APPARAENT_ENERGY = "ACT_"+ConstantAppConfig.DUT_DISPLAY_KEY+"_APP_ENGY";
		RESULT_DATA_TYPE_ACTUAL_DUT_ACTIVE_ENERGY = "ACT_"+ConstantAppConfig.DUT_DISPLAY_KEY+"_ACTV_ENGY";
		RESULT_DATA_TYPE_ACTUAL_DUT_REACTIVE_ENERGY = "ACT_"+ConstantAppConfig.DUT_DISPLAY_KEY+"_RCTV_ENGY";

	}

}
