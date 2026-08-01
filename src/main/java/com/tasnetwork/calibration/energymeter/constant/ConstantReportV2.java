package com.tasnetwork.calibration.energymeter.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

public class ConstantReportV2 {

	
	public static final String PRINT_STYLE_RESULT_TYPE_NONE = "None";
	public static final String PRINT_STYLE_RESULT_TYPE_VALUE = "ResultValue";
	public static final String PRINT_STYLE_RESULT_TYPE_STATUS = "ResultStatus";
	public static final String PRINT_STYLE_RESULT_TYPE_PAGE_STATUS =  "PageStatus";
	public static final String PRINT_STYLE_RESULT_TYPE_OVER_ALL_STATUS = "OverAllStatus";
	
	public static final boolean REPEAT_START_TO_END_FEATURE_ENABLED = true;
	public static final String POPULATE_COMPLY = "Complies";
	public static final String POPULATE_DOES_NOT_COMPLY = "Does not comply";
	public static final String POPULATE_ACTIVE_ENERGY = "Active Energy";
	public static final String POPULATE_REACTIVE_ENERGY = "Reactive Energy";
	public static final String REPORT_GROUP_IMPORT_ALL = "*.*";
	public static final String REPORT_PRINT_SELECT_RESULT_STYLE = "Select Result style";
	public static final String REPORT_PRINT_SELECT_GENERIC_HEADER_STYLE = "Select Generic Header style";
	public static final String REPORT_PRINT_SELECT_TABLE_HEADER_STYLE = "Select Table Header style";
	
	
	public static final String REPORT_META_DATATYPE_SERIAL_NO = "Serial No";
	public static final String REPORT_META_DATATYPE_DUT_SERIAL_NO = "Meter Serial No";
	
    public static final String REPORT_META_DATATYPE_RACK_POSITION_NO = "Rack position No";
	public static final String REPORT_META_DATATYPE_DUT_TYPE = "Meter Type";
	public static final String REPORT_META_DATATYPE_DUT_MAKE = "Meter Make";
	public static final String REPORT_META_DATATYPE_DUT_MODEL_NO = "Meter Model No";
	public static final String REPORT_META_DATATYPE_CAPACITY = "Capacity";
	public static final String REPORT_META_DATATYPE_BATCH_NO = "Batch No";
	public static final String REPORT_META_DATATYPE_METER_CONSTANT = "Meter Constant";
	public static final String REPORT_META_DATATYPE_PT_RATIO = "PT ratio";
	public static final String REPORT_META_DATATYPE_CT_RATIO = "CT ratio";
	public static String REPORT_META_DATATYPE_DUT_OVERALL_STATUS = ConstantAppConfig.DUT_DISPLAY_KEY+" OverAll Status";
	public static String REPORT_META_DATATYPE_DUT_PAGE_STATUS = ConstantAppConfig.DUT_DISPLAY_KEY+" Page Status";
	public static final String REPORT_META_DATATYPE_DUT_CLASS = "Meter Class";
	public static final String REPORT_META_DATATYPE_DUT_BASIC_CURRENT = "Meter Basic Current";
	public static final String REPORT_META_DATATYPE_DUT_MAX_CURRENT = "Meter Max Current";
	public static final String REPORT_META_DATATYPE_DUT_RATED_VOLT = "Meter Rated Volt";
	public static final String REPORT_META_DATATYPE_DUT_FREQ = "Meter Frequency";
	public static final String REPORT_META_DATATYPE_CT_TYPE = "CT Type";
	public static final String REPORT_META_DATATYPE_CUSTOMER_NAME = "Customer Name";
	public static final String REPORT_META_DATATYPE_LORA_ID = "Lora Id";
	public static final String REPORT_META_DATATYPE_EXEC_TIME_STAMP = "Execution Time Stamp";
	public static final String REPORT_META_DATATYPE_EXEC_DATE = "Execution Date";
	public static final String REPORT_META_DATATYPE_EXEC_TIME = "Execution Time";
	public static final String REPORT_META_DATATYPE_REPORT_GEN_TIME_STAMP = "Report Gen Time Stamp";
	public static final String REPORT_META_DATATYPE_REPORT_GEN_DATE = "Report Gen Date";
	public static final String REPORT_META_DATATYPE_REPORT_GEN_TIME = "Report Gen Time";
	public static final String REPORT_META_DATATYPE_APPROVED_TIME_STAMP = "Approved Time Stamp";
	public static final String REPORT_META_DATATYPE_APPROVED_DATE = "Approved Date";
	public static final String REPORT_META_DATATYPE_APPROVED_TIME = "Approved Time";
	public static final String REPORT_META_DATATYPE_TESTED_BY = "Tested by";
	public static final String REPORT_META_DATATYPE_WITNESSED_BY = "Witnessed by";
	public static final String REPORT_META_DATATYPE_APPROVED_BY = "Approved by";
	public static final String REPORT_META_DATATYPE_PAGE_NO = "Page No";
	public static final String REPORT_META_DATATYPE_MAX_NO_OF_PAGES = "Max No Of Pages";
	public static final String REPORT_META_DATATYPE_PAGE_NO_WITH_MAX_NO_OF_PAGES = "Page No/Max Pages";
	public static final String REPORT_META_DATATYPE_ENERGY_FLOW_MODE = "Import/Export Mode";
	public static final String REPORT_META_DATATYPE_EXECUTION_CT_MODE = "Main CT/Neutral CT";
	public static final String REPORT_META_DATATYPE_ACTIVE_REACTIVE_ENERGY = "Active/Reactive Energy";
	public static final String REPORT_META_DATATYPE_COMPLIES = "Complies/Does not comply";
	
	public static ArrayList<String> REPORT_META_DATATYPE_LIST = new ArrayList<String> ();

	
	public static final String NONE_DISPLAYED = "None";
	public static final String TEST_POINT_FILTER_SEPERATOR = "-";
	public static final String TEST_POINT_NAME_SEPERATOR = "_";
	
	public static final String OPERATION_PROCESS_DATA_TYPE_LOCAL_INPUT = "PageInput";//"LocalInput";
	public static final String OPERATION_PROCESS_DATA_TYPE_LOCAL_OUTPUT = "PageOutput";//"LocalOutput";
	public static final String OPERATION_PROCESS_DATA_TYPE_MASTER_INPUT = "BookInput";//"MasterInput";
	public static final String OPERATION_PROCESS_DATA_TYPE_MASTER_OUTPUT = "BookOutput";//"MasterOutput";
	
	public static final ArrayList<String> OPERATION_PROCESS_DATA_TYPE_LIST = new ArrayList<String>(Arrays.asList(
			
			OPERATION_PROCESS_DATA_TYPE_LOCAL_INPUT,
			OPERATION_PROCESS_DATA_TYPE_LOCAL_OUTPUT, 
			OPERATION_PROCESS_DATA_TYPE_MASTER_OUTPUT));
	
	public static final  String OPERATION_PROCESS_MODE_NONE = NONE_DISPLAYED;//"None";
	public static final  String OPERATION_PROCESS_MODE_INPUT = "Input";
	public static final  String OPERATION_PROCESS_MODE_OUTPUT = "Output";
	
	public static final String RP_DATA_OWNER_TEST_DATA_FILTER = "TestDataFilter";
	public static final String RP_DATA_OWNER_META_DATA_FILTER = "MetaDataFilter";
	
	
	public static final String RP_PRINT_TYPE_HEADER_ONLY = "PrintHeaderOnly";
	public static final String RP_PRINT_TYPE_ALL   = "PrintAll";
	

	public static final String PARAM_PROFILE_REPEAT_AVERAGE_KEY = "ParamProfileRepeatAverage";
	public static final String PARAM_PROFILE_SELF_HEAT_AVERAGE_KEY = "ParamProfileSelfHeatAverage";
	

	public static final String CELL_HEADER_POSITION_HEADER_REPEAT_RESULT_DATA_PREFIX_KEY = "Repeat Header-";
	public static final String CELL_HEADER_POSITION_HEADER_SELF_HEAT_RESULT_DATA_PREFIX_KEY = "Self Heat Header-";
	
	public static final String CELL_START_POSITION_HEADER_REPEAT_RESULT_DATA_PREFIX_KEY = "Repeat Result Value-";
	public static final String CELL_START_POSITION_HEADER_SELF_HEAT_RESULT_DATA_PREFIX_KEY = "Self Heat Result Value-";
	
	//public static final String CELL_START_POSITION_HEADER_RESULT_DATA_KEY = "Result Value";
	public static String RESULT_SOURCE_TYPE_RESULT_STATUS_KEY = "Result Status";
	
	//public static final String CELL_HEADER_POSITION_HEADER_RESULT_RSM_INITIAL = "Rsm Initial";
	//public static final String CELL_HEADER_POSITION_HEADER_RESULT_RSM_FINAL = "Rsm Final";
	public static final String RESULT_SOURCE_TYPE_DISPLAY_RSM_DIFFERENCE = "Rsm Difference";

	public static final String RESULT_SOURCE_TYPE_DISPLAY_RSM_INITIAL = "Rsm Initial";
	public static final String RESULT_SOURCE_TYPE_DISPLAY_RSM_FINAL = "Rsm Final";
	
	public static final  String POPULATE_HEADER1_KEY = "Header1";
	public static final  String POPULATE_HEADER2_KEY = "Header2";
	public static final  String POPULATE_HEADER3_KEY = "Header3";
	public static final  String POPULATE_HEADER4_KEY = "Header4";
	public static final  String POPULATE_HEADER5_KEY = "Header5";
	public static final  String POPULATE_HEADER_KEY_TEST_PERIOD_IN_MINUTES = "Test Period";
	public static final  String POPULATE_HEADER_KEY_WARMUP_PERIOD_IN_MINUTES = "Warmup Period";
	public static final  String POPULATE_HEADER_KEY_TARGET_VOLTAGE = "Target Voltage";
	public static final  String POPULATE_HEADER_KEY_TARGET_CURRENT = "Target Current";
	public static final  String POPULATE_HEADER_KEY_TARGET_PF = "Target PF";
	public static final  String POPULATE_HEADER_KEY_TARGET_FREQ = "Target Frequency";
	public static final  String POPULATE_HEADER_KEY_TARGET_ENERGY = "Target Energy";
	
	public static final  String POPULATE_LOCAL_UPPER_LIMIT_KEY = "LocalAllowedUpperLimit";
	public static final  String POPULATE_LOCAL_LOWER_LIMIT_KEY = "LocalAllowedLowerLimit";
	public static final  String POPULATE_LOCAL_MERGED_LIMIT_KEY = "LocalMergedLimit";
	public static final  String POPULATE_LOCAL_OUTPUT_STATUS_KEY = "PageOutputStatus";//"LocalOutputStatus";
	public static final  String POPULATE_MASTER_OUTPUT_STATUS_KEY = "BookOutputStatus";//"MasterOutputStatus";
	
	public static final  String POPULATE_MASTER_UPPER_LIMIT_KEY = "MasterAllowedUpperLimit";
	public static final  String POPULATE_MASTER_LOWER_LIMIT_KEY = "MasterAllowedLowerLimit";
	public static final  String POPULATE_MASTER_MERGED_LIMIT_KEY = "MasterMergedLimit";
	
	public static final  String PROCESS_PAGE_INPUT_DUT_INTIAL_AUTO_INSERT_KEY = "AutoInsertPageInputTestDutInitial_001";
	public static final  String PROCESS_PAGE_INPUT_DUT_FINAL_AUTO_INSERT_KEY = "AutoInsertPageInputTestDutFinal_001";
	public static final  String PROCESS_PAGE_OUTPUT_DUT_DIFF_AUTO_INSERT_KEY = "AutoInsertPageOutputTestDutDiff_001";
	public static final  String PROCESS_PAGE_OUTPUT_RSM_DIFF_AUTO_INSERT_KEY = "AutoInsertPageOutputTestRsmDiff_001";
	//public static final  String PROCESS_PAGE_OUTPUT_DUT_DIFF_AUTO_INSERT_RP_PRINT_KEY = "AutoInsertPageOutputRpPrintTestDutDiff_001";
	//public static final  String PROCESS_PAGE_OUTPUT_DUT_DIFF_AUTO_INSERTION_KEY = "PageOutputTestDutDiff_001";
	
	//public static String RESULT_DATA_TYPE_DISPLAY_ERROR_VALUE = ConstantAppConfig.DUT_DISPLAY_KEY+" Error Result";//resultDataTypeDisplayErrorValue
	//public static String RESULT_DATA_TYPE_DISPLAY_DUT_PULSE_COUNT = ConstantAppConfig.DUT_DISPLAY_KEY+" Pulse Count";
	//public static String RESULT_DATA_TYPE_DISPLAY_DUT_INITIAL_REGISTER = ConstantAppConfig.DUT_DISPLAY_KEY+" Initial Register";
	// static String RESULT_DATA_TYPE_DISPLAY_DUT_FINAL_REGISTER = ConstantAppConfig.DUT_DISPLAY_KEY+" Final Register";
	//public static String RESULT_DATA_TYPE_DISPLAY_DUT_ONE_PULSE_DURATION = ConstantAppConfig.DUT_DISPLAY_KEY+" One Pulse Duration";
	
	
	public static String RESULT_SOURCE_TYPE_DISPLAY_DUT_AVERAGE = ConstantAppConfig.DUT_DISPLAY_KEY+" Average";
	//public static String RESULT_SOURCE_TYPE_DISPLAY_DUT_AUTO_INSERT_CALC_ERROR = ConstantAppConfig.DUT_DISPLAY_KEY+" Calculated Error";
	
	
	public static String RESULT_SOURCE_TYPE_DISPLAY_ERROR_VALUE = ConstantAppConfig.DUT_DISPLAY_KEY+" Error Result Value";//resultDataTypeDisplayErrorValue
	public static String RESULT_SOURCE_TYPE_DISPLAY_DUT_PULSE_COUNT = ConstantAppConfig.DUT_DISPLAY_KEY+" Pulse Count";
	public static String RESULT_SOURCE_TYPE_DISPLAY_DUT_INITIAL_REGISTER = ConstantAppConfig.DUT_DISPLAY_KEY+" Initial Register";
	public static String RESULT_SOURCE_TYPE_DISPLAY_DUT_FINAL_REGISTER = ConstantAppConfig.DUT_DISPLAY_KEY+" Final Register";
	public static String RESULT_SOURCE_TYPE_DISPLAY_DUT_DIFFERENCE = ConstantAppConfig.DUT_DISPLAY_KEY+" Difference";
	public static String RESULT_SOURCE_TYPE_DISPLAY_DUT_AVERAGE_VALUE = ConstantAppConfig.DUT_DISPLAY_KEY+" Average Value";
	public static String RESULT_SOURCE_TYPE_DISPLAY_DUT_AVERAGE_STATUS = ConstantAppConfig.DUT_DISPLAY_KEY+" Average Status";
	

	public static String RESULT_SOURCE_TYPE_DISPLAY_DUT_REPEAT_AVERAGE_VALUE = ConstantAppConfig.DUT_DISPLAY_KEY+" Repeat Average Value";
	public static String RESULT_SOURCE_TYPE_DISPLAY_DUT_REPEAT_AVERAGE_STATUS = ConstantAppConfig.DUT_DISPLAY_KEY+" Repeat Average Status";
	

	public static String RESULT_SOURCE_TYPE_DISPLAY_DUT_SELF_HEAT_AVERAGE_VALUE = ConstantAppConfig.DUT_DISPLAY_KEY+" Self Heat Average Value";
	public static String RESULT_SOURCE_TYPE_DISPLAY_DUT_SELF_HEAT_AVERAGE_STATUS = ConstantAppConfig.DUT_DISPLAY_KEY+" Self Heat Average Status";
	
	public static String RESULT_SOURCE_TYPE_DISPLAY_DUT_ONE_PULSE_DURATION = ConstantAppConfig.DUT_DISPLAY_KEY+" One Pulse Duration";
	
	
	//public static final  String RESULT_DATA_TYPE_DISPLAY_REFSTD_INITIAL_REGISTER = "RSM Initial Register";
	//public static final  String RESULT_DATA_TYPE_DISPLAY_REFSTD_FINAL_REGISTER = "RSM Final Register";
	public static final  String RESULT_DATA_TYPE_DISPLAY_OPERATION = "Operation";
	
	public static final  String POPULATE_DATA_TYPE_NONE = NONE_DISPLAYED;
	public static final  String POPULATE_DATA_TYPE_ONLY_HEADERS = "Only Headers";
	public static        String POPULATE_DATA_TYPE_ALL_DUT = "All " + ConstantAppConfig.DUT_DISPLAY_KEY;
	public static        String POPULATE_DATA_TYPE_ALL_DUT_AVERAGE = "All " + ConstantAppConfig.DUT_DISPLAY_KEY + "Average";
	
	public static ArrayList<String> POPULATE_DATA_TYPE_LIST = new ArrayList<String>();
	
	public static ArrayList<String> POPULATE_PARAM_PROFILE_DATA_TYPE_LIST = new ArrayList<String>();
	
	//public static final  String OPERATION_PROCESS_PARAM_TYPE_ = NONE_DISPLAYED;
	//public static final  String POPULATE_DATA_TYPE_ONLY_HEADERS = "Only Headers";
	
	public static final  String TEST_ID_MASK = "XX";//Sum
	public static final  String PAGE_NO_PREFIX = "Page: ";//Sum
	public static final  String TEST_PERIOD_PREFIX = "Test Period: ";
	public static final  String TEST_PERIOD_MINUTE_POSTFIX = " Min";
	public static final  String TEST_PERIOD_SECONDS_POSTFIX = " Sec";
	
	public static final  String WARMUP_PERIOD_PREFIX = "Warmup Period: ";
	public static final  String WARMUP_PERIOD_MINUTE_POSTFIX = " Min";
	public static final  String WARMUP_PERIOD_SECONDS_POSTFIX = " Sec";
	public static final  String FREQ_POSTFIX = " Hz";
	public static final  String VOLT_POSTFIX = " V";
	public static final  String CURRENT_POSTFIX = " A";

	public static final  String OPERATION_METHOD_ADD = "Add";//Sum
	public static final  String OPERATION_METHOD_MAXIMUM = "Maximum";
	public static final  String OPERATION_METHOD_MINIMUM = "Minimum";
	public static final  String OPERATION_METHOD_DIFFERENCE = "Difference";
	public static final  String OPERATION_METHOD_MULTIPLY = "Multiply";
	public static final  String OPERATION_METHOD_AVERAGE = "Average";
	public static final  String OPERATION_METHOD_ERROR_PERCENTAGE = "ErrorPercentage";
	
	
	public static final  String POST_OPERATION_METHOD_ADD = "Add";
	public static final  String POST_OPERATION_METHOD_SUBTRACT = "Subtract";
	public static final  String POST_OPERATION_METHOD_MULTIPLY = "Multiply";
	public static final  String POST_OPERATION_METHOD_DIVIDE = "Divide";
	
	public static final ArrayList<String> INDEX_COLOR_LIST = new ArrayList<String>(Arrays.asList(
															
															"AQUA",
															"AUTOMATIC",
															"BLACK",
															"BLACK1",
															"BLUE",
															"BLUE_GREY",
															"BLUE1",
															"BRIGHT_GREEN",
															"BRIGHT_GREEN1",
															"BROWN",
															"CORAL",
															"CORNFLOWER_BLUE",
															"DARK_BLUE",
															"DARK_GREEN",
															"DARK_RED",
															"DARK_TEAL",
															"DARK_YELLOW",
															"GOLD",
															"GREEN",
															"GREY_25_PERCENT",
															"GREY_40_PERCENT",
															"GREY_50_PERCENT",
															"GREY_80_PERCENT",
															"INDIGO",
															"LAVENDER",
															"LEMON_CHIFFON",
															"LIGHT_BLUE",
															"LIGHT_CORNFLOWER_BLUE",
															"LIGHT_GREEN",
															"LIGHT_ORANGE",
															"LIGHT_TURQUOISE",
															"LIGHT_TURQUOISE1",
															"LIGHT_YELLOW",
															"LIME",
															"MAROON",
															"OLIVE_GREEN",
															"ORANGE",
															"ORCHID",
															"PALE_BLUE",
															"PINK",
															"PINK1",
															"PLUM",
															"RED",
															"RED1",
															"ROSE",
															"ROYAL_BLUE",
															"SEA_GREEN",
															"SKY_BLUE",
															"TAN",
															"TEAL",
															"TURQUOISE",
															"TURQUOISE1",
															"VIOLET",
															"WHITE",
															"WHITE1",
															"YELLOW",
															"YELLOW1"));

	
	public static final ArrayList<String> FILL_PATTERN_LIST = new ArrayList<String>(Arrays.asList(
			"ALT_BARS",
			"BIG_SPOTS",
			"BRICKS",
			"DIAMONDS",
			"FINE_DOTS",
			"LEAST_DOTS",
			"LESS_DOTS",
			"NO_FILL",
			"SOLID_FOREGROUND",
			"SPARSE_DOTS",
			"SQUARES",
			"THICK_BACKWARD_DIAG",
			"THICK_FORWARD_DIAG",
			"THICK_HORZ_BANDS",
			"THICK_VERT_BANDS",
			"THIN_BACKWARD_DIAG",
			"THIN_FORWARD_DIAG",
			"THIN_HORZ_BANDS",
			"THIN_VERT_BANDS"	));

	
	public static final ArrayList<String> POST_OPERATION_METHOD_LIST = new ArrayList<String>(Arrays.asList(
			POST_OPERATION_METHOD_ADD,
			POST_OPERATION_METHOD_SUBTRACT,
			POST_OPERATION_METHOD_MULTIPLY,
			POST_OPERATION_METHOD_DIVIDE			
			));//"Add","Subtract","Multiply","Divide"));
	
	public static final ArrayList<String> OPERATION_METHOD_LIST = new ArrayList<String>(Arrays.asList(ConstantReportV2.NONE_DISPLAYED,
			/*"Maximum","Minimum","Difference",
			"Multiply","Average","ErrorPercentage"));*/
			OPERATION_METHOD_ADD,
			OPERATION_METHOD_MAXIMUM, OPERATION_METHOD_MINIMUM, OPERATION_METHOD_DIFFERENCE,
			OPERATION_METHOD_MULTIPLY,OPERATION_METHOD_AVERAGE,
			OPERATION_METHOD_ERROR_PERCENTAGE));
	
	private static HashMap<String,String> resultDataTypeHashMap = new LinkedHashMap<String,String>();
	private static HashMap<String,String> resultSourceTypeHashMap = new LinkedHashMap<String,String>();
	
	public static HashMap<String, String> getResultDataTypeHashMap() {
		return resultDataTypeHashMap;
	}

	public void setResultDataTypeHashMap(HashMap<String, String> resultDataTypeHashMap) {
		ConstantReportV2.resultDataTypeHashMap = resultDataTypeHashMap;
	}
	
	public static void init(){
		
		ApplicationLauncher.logger.debug("ConstantReportV2: Init");
		/*resultDataTypeHashMap.put(RESULT_DATA_TYPE_DISPLAY_ERROR_VALUE, ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
		resultDataTypeHashMap.put(RESULT_DATA_TYPE_DISPLAY_DUT_PULSE_COUNT, ConstantReport.RESULT_DATA_TYPE_PULSE_COUNT);
		resultDataTypeHashMap.put(RESULT_DATA_TYPE_DISPLAY_DUT_INITIAL_REGISTER, ConstantReport.RESULT_DATA_TYPE_INITIAL_KWH);
		resultDataTypeHashMap.put(RESULT_DATA_TYPE_DISPLAY_DUT_FINAL_REGISTER, ConstantReport.RESULT_DATA_TYPE_FINAL_KWH);
		resultDataTypeHashMap.put(RESULT_DATA_TYPE_DISPLAY_REFSTD_INITIAL_REGISTER, ConstantReport.RESULT_DATA_TYPE_REF_INITIAL_KWH);
		resultDataTypeHashMap.put(RESULT_DATA_TYPE_DISPLAY_REFSTD_FINAL_REGISTER, ConstantReport.RESULT_DATA_TYPE_REF_FINAL_KWH);
		*/
		
		RESULT_SOURCE_TYPE_RESULT_STATUS_KEY = ConstantAppConfig.DUT_DISPLAY_KEY + " Result Status";
	    
		//RESULT_DATA_TYPE_DISPLAY_ERROR_VALUE = ConstantAppConfig.DUT_DISPLAY_KEY+" Error Result";//resultDataTypeDisplayErrorValue
		//RESULT_DATA_TYPE_DISPLAY_DUT_PULSE_COUNT = ConstantAppConfig.DUT_DISPLAY_KEY+" Pulse Count";
		//RESULT_DATA_TYPE_DISPLAY_DUT_INITIAL_REGISTER = ConstantAppConfig.DUT_DISPLAY_KEY+" Initial Register";
		//RESULT_DATA_TYPE_DISPLAY_DUT_FINAL_REGISTER = ConstantAppConfig.DUT_DISPLAY_KEY+" Final Register";
		//RESULT_DATA_TYPE_DISPLAY_DUT_ONE_PULSE_DURATION = ConstantAppConfig.DUT_DISPLAY_KEY+" One Pulse Duration";
		
		RESULT_SOURCE_TYPE_DISPLAY_DUT_AVERAGE = ConstantAppConfig.DUT_DISPLAY_KEY+" Average";
		
		//RESULT_SOURCE_TYPE_DISPLAY_DUT_AUTO_INSERT_CALC_ERROR = ConstantAppConfig.DUT_DISPLAY_KEY+" Calculated Error";

	    RESULT_SOURCE_TYPE_DISPLAY_ERROR_VALUE = ConstantAppConfig.DUT_DISPLAY_KEY+" Error Result Value";//resultDataTypeDisplayErrorValue
		RESULT_SOURCE_TYPE_DISPLAY_DUT_PULSE_COUNT = ConstantAppConfig.DUT_DISPLAY_KEY+" Pulse Count";
		RESULT_SOURCE_TYPE_DISPLAY_DUT_INITIAL_REGISTER = ConstantAppConfig.DUT_DISPLAY_KEY+" Initial Register";
		RESULT_SOURCE_TYPE_DISPLAY_DUT_FINAL_REGISTER = ConstantAppConfig.DUT_DISPLAY_KEY+" Final Register";
		RESULT_SOURCE_TYPE_DISPLAY_DUT_DIFFERENCE = ConstantAppConfig.DUT_DISPLAY_KEY+" Difference";
		RESULT_SOURCE_TYPE_DISPLAY_DUT_AVERAGE_VALUE = ConstantAppConfig.DUT_DISPLAY_KEY+" Average Value";
		RESULT_SOURCE_TYPE_DISPLAY_DUT_AVERAGE_STATUS = ConstantAppConfig.DUT_DISPLAY_KEY+" Average Status";
		
		RESULT_SOURCE_TYPE_DISPLAY_DUT_REPEAT_AVERAGE_VALUE = ConstantAppConfig.DUT_DISPLAY_KEY+" Repeat Average Value";
		RESULT_SOURCE_TYPE_DISPLAY_DUT_REPEAT_AVERAGE_STATUS = ConstantAppConfig.DUT_DISPLAY_KEY+" Repeat Average Status";
		

		RESULT_SOURCE_TYPE_DISPLAY_DUT_SELF_HEAT_AVERAGE_VALUE = ConstantAppConfig.DUT_DISPLAY_KEY+" Self Heat Average Value";
		RESULT_SOURCE_TYPE_DISPLAY_DUT_SELF_HEAT_AVERAGE_STATUS = ConstantAppConfig.DUT_DISPLAY_KEY+" Self Heat Average Status";

		
		RESULT_SOURCE_TYPE_DISPLAY_DUT_ONE_PULSE_DURATION = ConstantAppConfig.DUT_DISPLAY_KEY+" One Pulse Duration";
		
		REPORT_META_DATATYPE_DUT_OVERALL_STATUS = ConstantAppConfig.DUT_DISPLAY_KEY+" OverAll Status";
		REPORT_META_DATATYPE_DUT_PAGE_STATUS = ConstantAppConfig.DUT_DISPLAY_KEY+" Page Status";
		
		POPULATE_DATA_TYPE_ALL_DUT = "All " + ConstantAppConfig.DUT_DISPLAY_KEY;
		POPULATE_DATA_TYPE_ALL_DUT_AVERAGE = "All " + ConstantAppConfig.DUT_DISPLAY_KEY + " Average";
		
		
		//resultDataTypeHashMap.put(RESULT_DATA_TYPE_DISPLAY_ERROR_VALUE, ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
		//resultDataTypeHashMap.put(RESULT_DATA_TYPE_DISPLAY_DUT_PULSE_COUNT, ConstantReport.RESULT_DATA_TYPE_PULSE_COUNT);
		//resultDataTypeHashMap.put(RESULT_DATA_TYPE_DISPLAY_DUT_INITIAL_REGISTER, ConstantReport.RESULT_DATA_TYPE_INITIAL_KWH);
		//resultDataTypeHashMap.put(RESULT_DATA_TYPE_DISPLAY_DUT_FINAL_REGISTER, ConstantReport.RESULT_DATA_TYPE_FINAL_KWH);
		//resultDataTypeHashMap.put(RESULT_DATA_TYPE_DISPLAY_DUT_ONE_PULSE_DURATION, ConstantReport.RESULT_DATA_TYPE_STA_TIME);
		//resultDataTypeHashMap.put(RESULT_DATA_TYPE_DISPLAY_REFSTD_INITIAL_REGISTER, ConstantReport.RESULT_DATA_TYPE_REF_INITIAL_KWH);
		//resultDataTypeHashMap.put(RESULT_DATA_TYPE_DISPLAY_REFSTD_FINAL_REGISTER, ConstantReport.RESULT_DATA_TYPE_REF_FINAL_KWH);
		resultDataTypeHashMap.put("ResultDataKey", "RsultDataValue");
		
		resultSourceTypeHashMap.put(RESULT_SOURCE_TYPE_DISPLAY_ERROR_VALUE, ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
		resultSourceTypeHashMap.put(RESULT_SOURCE_TYPE_DISPLAY_DUT_AVERAGE, ConstantReport.RESULT_DATA_TYPE_AVG);
		resultSourceTypeHashMap.put(RESULT_SOURCE_TYPE_DISPLAY_RSM_INITIAL, ConstantReport.RESULT_DATA_TYPE_REF_INITIAL_KWH);
		resultSourceTypeHashMap.put(RESULT_SOURCE_TYPE_DISPLAY_RSM_FINAL, ConstantReport.RESULT_DATA_TYPE_REF_FINAL_KWH);
		resultSourceTypeHashMap.put(RESULT_SOURCE_TYPE_DISPLAY_RSM_DIFFERENCE, ConstantReport.RESULT_DATA_TYPE_REF_DIFF_KWH);
		resultSourceTypeHashMap.put(RESULT_SOURCE_TYPE_DISPLAY_DUT_PULSE_COUNT, ConstantReport.RESULT_DATA_TYPE_PULSE_COUNT);
		resultSourceTypeHashMap.put(RESULT_SOURCE_TYPE_DISPLAY_DUT_INITIAL_REGISTER, ConstantReport.RESULT_DATA_TYPE_INITIAL_KWH);
		resultSourceTypeHashMap.put(RESULT_SOURCE_TYPE_DISPLAY_DUT_FINAL_REGISTER, ConstantReport.RESULT_DATA_TYPE_FINAL_KWH);
		//resultDataTypeHashMap.put(RESULT_SOURCE_TYPE_DISPLAY_DUT_AUTO_INSERT_CALC_ERROR,ConstantReport.RESULT_DATA_TYPE_CONST_ERROR_VALUE);
		
		
		
		
		//resultSourceDataTypeHashMap.put(RESULT_DATA_TYPE_DISPLAY_ERROR_VALUE2, ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
		//resultSourceDataTypeHashMap.put(RESULT_SOURCE_TYPE_DISPLAY_DUT_AVERAGE, ConstantReport.RESULT_DATA_TYPE_AVG);
		//resultSourceDataTypeHashMap.put(CELL_HEADER_POSITION_HEADER_RESULT_RSM_INITIAL2, ConstantReport.RESULT_DATA_TYPE_REF_INITIAL_KWH);
		//resultSourceDataTypeHashMap.put(CELL_HEADER_POSITION_HEADER_RESULT_RSM_FINAL2, ConstantReport.RESULT_DATA_TYPE_REF_FINAL_KWH);
		//resultSourceDataTypeHashMap.put(CELL_HEADER_POSITION_HEADER_RESULT_RSM_DIFFERENCE, ConstantReport.RESULT_DATA_TYPE_REF_DIFF_KWH);
		//resultSourceDataTypeHashMap.put(RESULT_DATA_TYPE_DISPLAY_DUT_PULSE_COUNT2, ConstantReport.RESULT_DATA_TYPE_PULSE_COUNT);
		//resultSourceDataTypeHashMap.put(RESULT_DATA_TYPE_DISPLAY_DUT_INITIAL_REGISTER2, ConstantReport.RESULT_DATA_TYPE_INITIAL_KWH);
		//resultSourceDataTypeHashMap.put(RESULT_DATA_TYPE_DISPLAY_DUT_FINAL_REGISTER2, ConstantReport.RESULT_DATA_TYPE_FINAL_KWH);
		//resultSourceDataTypeHashMap.put(RESULT_SOURCE_TYPE_DISPLAY_DUT_AUTO_INSERT_CALC_ERROR,ConstantReport.RESULT_DATA_TYPE_CONST_ERROR_VALUE);
		
		
		
		POPULATE_DATA_TYPE_LIST = new ArrayList<String> (
				Arrays.asList(
						
							POPULATE_DATA_TYPE_NONE,
							POPULATE_DATA_TYPE_ONLY_HEADERS,
							POPULATE_DATA_TYPE_ALL_DUT
							
							
						));
		
		POPULATE_PARAM_PROFILE_DATA_TYPE_LIST = new ArrayList<String> (
				Arrays.asList(
						
						POPULATE_DATA_TYPE_NONE,
						POPULATE_DATA_TYPE_ONLY_HEADERS,
						POPULATE_DATA_TYPE_ALL_DUT,
						POPULATE_DATA_TYPE_ALL_DUT_AVERAGE
						
					));
		
		REPORT_META_DATATYPE_LIST = new ArrayList<String> (
				Arrays.asList(REPORT_META_DATATYPE_SERIAL_NO,					
						REPORT_META_DATATYPE_DUT_SERIAL_NO,			
					    REPORT_META_DATATYPE_RACK_POSITION_NO,
						REPORT_META_DATATYPE_DUT_TYPE ,
						REPORT_META_DATATYPE_DUT_MAKE ,
						REPORT_META_DATATYPE_DUT_MODEL_NO ,
						REPORT_META_DATATYPE_CAPACITY ,
						REPORT_META_DATATYPE_BATCH_NO ,
						REPORT_META_DATATYPE_METER_CONSTANT ,
						REPORT_META_DATATYPE_PT_RATIO ,
						REPORT_META_DATATYPE_CT_RATIO ,
						REPORT_META_DATATYPE_DUT_PAGE_STATUS,
						REPORT_META_DATATYPE_DUT_OVERALL_STATUS ,
						REPORT_META_DATATYPE_DUT_CLASS ,
						REPORT_META_DATATYPE_DUT_BASIC_CURRENT ,
						REPORT_META_DATATYPE_DUT_MAX_CURRENT ,
						REPORT_META_DATATYPE_DUT_RATED_VOLT,
						REPORT_META_DATATYPE_DUT_FREQ ,
						REPORT_META_DATATYPE_CT_TYPE,
						REPORT_META_DATATYPE_CUSTOMER_NAME,
						REPORT_META_DATATYPE_LORA_ID,
						REPORT_META_DATATYPE_EXEC_TIME_STAMP,
						REPORT_META_DATATYPE_EXEC_DATE,
						REPORT_META_DATATYPE_EXEC_TIME ,
						REPORT_META_DATATYPE_REPORT_GEN_TIME_STAMP,
						REPORT_META_DATATYPE_REPORT_GEN_DATE,
						REPORT_META_DATATYPE_REPORT_GEN_TIME,
						REPORT_META_DATATYPE_APPROVED_TIME_STAMP,
						REPORT_META_DATATYPE_APPROVED_DATE,
						REPORT_META_DATATYPE_APPROVED_TIME,
						REPORT_META_DATATYPE_TESTED_BY,
						REPORT_META_DATATYPE_WITNESSED_BY,
						REPORT_META_DATATYPE_APPROVED_BY,
						REPORT_META_DATATYPE_PAGE_NO,
						REPORT_META_DATATYPE_MAX_NO_OF_PAGES,
						REPORT_META_DATATYPE_PAGE_NO_WITH_MAX_NO_OF_PAGES,
						REPORT_META_DATATYPE_ENERGY_FLOW_MODE,
						REPORT_META_DATATYPE_EXECUTION_CT_MODE,
						REPORT_META_DATATYPE_ACTIVE_REACTIVE_ENERGY,
						REPORT_META_DATATYPE_COMPLIES
						)
				);
	
	}

	public static HashMap<String, String> getResultSourceTypeHashMap() {
		return resultSourceTypeHashMap;
	}

	public static void setResultSourceTypeHashMap(HashMap<String, String> resultSourceDataTypeHashMap) {
		ConstantReportV2.resultSourceTypeHashMap = resultSourceDataTypeHashMap;
	}

}
