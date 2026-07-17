package com.tasnetwork.calibration.conveyor.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//import com.mysql.fabric.xmlrpc.base.Array;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

public class ConstantConveyor {
	
	public static final String PALLET_METER_SERIAL_NO_KEY = "meterSerialNo";//
	public static final String PALLET_DISTINCT_ID_KEY = "palletDistinctId";//
	public static final String PALLET_RACK_POSITION_NO_KEY = "palletRackPositionNo";//
	public static final boolean CONVEYOR_CALIB_FEATURE_ENABLED = true;//
	
	public static boolean CALIB_SANGYONG_SOURCE_CONNECTED = true;
	public static boolean ALL_LOOP_BREAK_FLAG = false;
	
	//public static boolean VERIFICATION_BAY_PALLETS_CLEARED = true;

	
	public static final String REASON_RESULT_APPENDER = " - ";
	public static final String REASON_RESULT_FAILED_DISPLAY  = "Failed: " ;
	public static final String REASON_RESULT_BATCH_FAILED_DISPLAY_HEADER  = "BATCH FAILED" ;
	public static final String REASON_RESULT_WFR_DISPLAY  = "WFR: " ;
	public static final String REASON_RESULT_BATCH_REASON = "Relay Open in VERIFIC";
	public static final String REASON_RESULT_BATCH_FAILED = REASON_RESULT_BATCH_FAILED_DISPLAY_HEADER + REASON_RESULT_APPENDER +REASON_RESULT_BATCH_REASON;
	
	public static final String CHANNEL_KEY_FT_DB  = "FT-dB" ;
	public static final String CHANNEL_KEY_ADD_METER_RESULT_DB  = "Meter-result-dB" ;
	public static final String CHANNEL_KEY_DB_TO_GUI_REFRESH  = "dB-gui-refresh" ;
	
	
	public static final int STABLE_PALLET_TIME_IN_MSEC = 3000;
	
	public static final int FT_OPTICAL_DELAY_TIME = 30; // in seconds
	//public static final int PHASE_CALIB_WAIT_TIME = 60; // in seconds
	public static final int PHASE_CALIB_WAIT_TIME = 55; // in seconds // reduced 5 seconds for optimisation - updated by Gopi version d0.8.5.7   -  07-Jul-2025
	//public static final int NEUTRAL_CALIB_WAIT_TIME = 45; // in seconds
	public static final int NEUTRAL_CALIB_WAIT_TIME = 35; // in seconds // reduced 10 seconds for optimisation -  updated by Gopi version d0.8.5.7   -  07-Jul-2025
	
	public static boolean FT_OPTICAL_PLACED 	= false;
	public static boolean FT_OPTICAL_REMOVED 	= false;
	public static boolean FT_LDU_PLACEMENT 		= false;
	public static boolean CALIB_OPTICAL_PLACED 	= false; 
	public static boolean CALIB_OPTICAL_REMOVED = false;
	public static boolean CALIB_CURRENT_STABLE 	= false;
	public static boolean VERIFIC_TESTING_DONE 	= false;
	
	public static final String CONV_RESULT_FILTER_ALL = "All";
	public static final String CONV_RESULT_FILTER_ALL_PASSED = "All Passed";
	public static final String CONV_RESULT_FILTER_CALIB_PASSED = "Calib Passed";
	public static final String CONV_RESULT_FILTER_LOE_PASSED = "LOE Passed";
	public static final String CONV_RESULT_FILTER_ANY_ONE_FAILED = "Any One Failed";
	public static final String CONV_RESULT_FILTER_NUNMERIC_SERIAL_NUMBER = "SerialWithOnlyNumbers";
	
	public static final String EXECUTION_STATUS_RERUN = "Re-Run";
	public static final String EXECUTION_STATUS_COMPLETED = "Completed";
	public static final String EXECUTION_STATUS_TIMED_OUT = "Time out";
	public static final String EXECUTION_STATUS_NOT_STARTED = "Not Started";
	public static final String EXECUTION_STATUS_INPROGRESS = "InProgress";
	public static final String EXECUTION_STATUS_ABORTED = "Aborted";
	public static final String EXECUTION_STATUS_SKIPPED = "Skipped";
	public static final String EXECUTION_STATUS_NOT_EXECUTED = "Not Executed";
	public static final String EXECUTION_STATUS_STARTED = "Started";
	
	public static final String ER_START_CMD = "CSTART" + GuiUtils.hexToAscii("0D0A");
	public static final String CMD_TEST_COMPLETED = "TEST_COMPLETED" ;
	public static final String CMD_START_CONVEYOR = "START_CONVEYOR" ;
	public static final String CMD_STOP_CONVEYOR = "STOP_CONVEYOR" ;

	public static final String HTTP_RESPONSE_SUCCESS = "200";
	
	public static final int MAX_DEVICES_CONNECTED = 6;
	 
	public static final String DEVICE_TYPE_QR_SCANNER = "QR";
	public static final String DEVICE_TYPE_DUT= "EM";
	public static final String DEVICE_TYPE_OHM_METER = "OM";
	public static final String DEVICE_TYPE_VOLT_METER = "VM";
	public static final String DEVICE_TYPE_LDU = "LD";
	public static final String DEVICE_TYPE_CLUSTER = "ZN";
	public static final String DEVICE_TYPE_CLUSTER_INPUT = "CL-IP";
	public static final String DEVICE_TYPE_CLUSTER_OUTPUT = "CL-OP";
	public static final String COMMON_ID_FOR_ALL_POSITION = "00";
	
	public static final String COMM_STATUS_NOT_APPLICABLE = "-NA-";
	public static final String COMM_EXECUTION_STATUS_COMPLETED = "Completed";
	public static final String COMM_EXECUTION_STATUS_INP = "In-Progress";
	public static final String COMM_EXECUTION_STATUS_PENDING = "Pending";
	
	public static final String COMM_ACCESS_FAILED = "Access Failed";
	
	
	public static final String FT_ALIAS_NAME = "FT";
	public static final String HV_ALIAS_NAME = "HV";
	public static final String IR_ALIAS_NAME = "IR";
	public static final String CALIB_ALIAS_NAME = "CALIB";
	
	public static final String ACCURACY_ALIAS_NAME = "LOE";
/*	public static final String ACCURACY1_ALIAS_NAME = "LOE_0.5ib";
	public static final String ACCURACY2_ALIAS_NAME = "LOE_1.0ib";
	public static final String ACCURACY3_ALIAS_NAME = "LOE_1.0Imax";
	public static final String ACCURACY4_ALIAS_NAME = "LOE_1.0Ib_0.5L";
	public static final String ACCURACY5_ALIAS_NAME = "LOE_1.0Ib_0.8C";*/
	public static final String CREEP_ALIAS_NAME = "NLD";
	public static final String STA_ALIAS_NAME = "STA";
	public static final String COMM_ALIAS_NAME = "COMM";
	
	public static final ArrayList<String> CONV_TEST_TYPE_ALIAS_LIST = new ArrayList<String>(Arrays.asList(
			FT_ALIAS_NAME,
			HV_ALIAS_NAME,
			IR_ALIAS_NAME,
			CALIB_ALIAS_NAME,
			ACCURACY_ALIAS_NAME,
			CREEP_ALIAS_NAME,
			STA_ALIAS_NAME,
			COMM_ALIAS_NAME
			));
	
	public static final ArrayList<String> INACTIVE_TEST_TYPE_ALIAS_LIST = new ArrayList<String>(Arrays.asList(

			COMM_ALIAS_NAME,
			CALIB_ALIAS_NAME
			));
	
	public static final String FT_BAY_KEY = "FTB";
	public static final String HV_BAY_KEY = "HVB";
	public static final String IR_BAY_KEY = "IRB";
	public static final String CALIBRATION_BAY_KEY = "CALB";
	public static final String COMMUNICATION_BAY_KEY = "COMB";
	public static final String LOADING_BAY_KEY = "LOADB";
	public static final String REJECTION_BAY_KEY = "RJTB";
	public static final String STA_NLD1_BAY_KEY = "STNLD1B";
	public static final String STA_NLD1_PP1_BAY_KEY = "STNLD1B-PP1";
	public static final String STA_NLD1_PP2_BAY_KEY = "STNLD1B-PP2";
	public static final String STA_NLD1_PP3_BAY_KEY = "STNLD1B-PP3";
	public static final String STA_NLD1_PP4_BAY_KEY = "STNLD1B-PP4";
	
	public static final String STA_NLD2_BAY_KEY = "STNLD2B";
	public static final String STA_NLD2_PP1_BAY_KEY = "STNLD2B-PP1";
	public static final String STA_NLD2_PP2_BAY_KEY = "STNLD2B-PP2";
	public static final String STA_NLD2_PP3_BAY_KEY = "STNLD2B-PP3";
	public static final String STA_NLD2_PP4_BAY_KEY = "STNLD2B-PP4";
	public static final String WAITING_BAY_KEY = "WTNGB";
	public static final String WAITING_PP1_BAY_KEY = "WTNGB-PP1";// pallete position-1
	public static final String WAITING_PP2_BAY_KEY = "WTNGB-PP2";// pallete position-1
	public static final String WAITING_PP3_BAY_KEY = "WTNGB-PP3";// pallete position-1
	public static final String WAITING_PP4_BAY_KEY = "WTNGB-PP4";// pallete position-1
	public static final String VERIFICATION_BAY_KEY = "VERIFICB";
	public static final String VERIFICATION_PP1_BAY_KEY = "VERIFICB-PP1";
	public static final String VERIFICATION_PP2_BAY_KEY = "VERIFICB-PP2";
	public static final String VERIFICATION_PP3_BAY_KEY = "VERIFICB-PP3";
	public static final String VERIFICATION_PP4_BAY_KEY = "VERIFICB-PP4";
	public static final String UNLOADING_BAY_KEY = "UNLDB";
	
	public static final String FT_BAY_DISPLAY_NAME = "Functional Bay";
	public static final String HV_BAY_DISPLAY_NAME = "High Voltage Bay";
	public static final String IR_BAY_DISPLAY_NAME = "IR Bay";
	public static final String CALIBRATION_BAY_DISPLAY_NAME = "Calibration Bay";
	public static final String COMMUNICATION_BAY_DISPLAY_NAME = "Communication Bay";
	public static final String LOADING_BAY_DISPLAY_NAME = "Loading Bay";
	public static final String REJECTION_BAY_DISPLAY_NAME = "Rejection Bay";
	public static final String STA_NLD1_BAY_DISPLAY_NAME = "STA NLD Bay 1";
	public static final String STA_NLD2_BAY_DISPLAY_NAME = "STA NLD Bay 2";
	public static final String WAITING_BAY_DISPLAY_NAME = "Waiting Bay";
	public static final String VERIFICATION_BAY_DISPLAY_NAME = "Verification Bay";
	public static final String UNLOADING_BAY_DISPLAY_NAME = "Unloading Bay";
	
	
	public static final List<String> GROUPED_BAY_LIST = Arrays.asList(
			
			WAITING_BAY_KEY,
			VERIFICATION_BAY_KEY, 
			STA_NLD1_BAY_KEY, 
			STA_NLD2_BAY_KEY
			
			);
	
	public static final List<String> ENTRY_BAY_LIST = Arrays.asList(
			
			FT_BAY_KEY
			
			);
	
	public static final List<String> EXIT_BAY_LIST = Arrays.asList(
			
			REJECTION_BAY_KEY,
			COMMUNICATION_BAY_KEY,
			UNLOADING_BAY_KEY
			
	);
	
	
	public static final String FT_RESULT_KEY = "FT";
	public static final String HV_RESULT_KEY = "HV";
	public static final String IR_RESULT_KEY = "IR";
	public static final String CALIBRATION_RESULT_KEY = "CALIB";
	public static final String VERIFICATION_RESULT_KEY = "LOE";
	public static final String NLD_RESULT_KEY = "NLD";
	public static final String STA_RESULT_KEY = "STA";
	public static final String COMMUNICATION_RESULT_KEY = "COMM";
	
	
	public static final List<String> RESULT_REPORT_TEST_TYPE_ORDER_LIST = Arrays.asList(FT_RESULT_KEY,HV_RESULT_KEY, 
			IR_RESULT_KEY, CALIBRATION_RESULT_KEY,
			VERIFICATION_RESULT_KEY,
			NLD_RESULT_KEY,
			STA_RESULT_KEY,
			COMMUNICATION_RESULT_KEY
			
			);
	
	public static final String FT_RESULT_TEST_NAME = "Functional Test";
	public static final String HV_RESULT_TEST_NAME = "High Voltage Sensing";
	public static final String IR_RESULT_TEST_NAME = "Insulation Resistance";
	//public static final String CALIB_RESULT_TEST_NAME = "Calibration";
	public static final String PHASE_CALIB_RESULT_TEST_NAME = "Phase Calibration";
	public static final String NEUTRAL_CALIB_RESULT_TEST_NAME = "Neutral Calibration";
	public static final String SUMMARY_CALIB_RESULT_TEST_NAME = "Calibration";
	public static final String SUMMARY_VERIFICATION_RESULT_TEST_NAME = "Verification";
	public static final String NLD_RESULT_TEST_NAME = "No Load";
	public static final String STA_RESULT_TEST_NAME = "Starting Current";
	public static final String COMMUNICATION_RESULT_TEST_NAME = "Communication";
	
	public static final ArrayList<String> TEST_NAME_SUMMARY_LIST = new ArrayList<String>(Arrays.asList(
				FT_RESULT_TEST_NAME,
				HV_RESULT_TEST_NAME,
				IR_RESULT_TEST_NAME,
				//CALIB_RESULT_TEST_NAME ,
				//PHASE_CALIB_RESULT_TEST_NAME ,
				//NEUTRAL_CALIB_RESULT_TEST_NAME ,
				SUMMARY_CALIB_RESULT_TEST_NAME,
				SUMMARY_VERIFICATION_RESULT_TEST_NAME ,
				NLD_RESULT_TEST_NAME ,
				STA_RESULT_TEST_NAME,
				COMMUNICATION_RESULT_TEST_NAME
	
			)
			);
	
	public static final String LED_PULSE_CHECK_RESULT_TEST_NAME = "LED Pulse Check";
	//public static final String READ_PHASE_CURRENT_RESULT_TEST_NAME = "ReadPhaseCurrent";
	//public static final String READ_NEUTRAL_CURRENT_RESULT_TEST_NAME = "ReadNeutralCurrent";
	public static final String RELAY_ON_READ_PHASE_CURRENT_RESULT_TEST_NAME = "RelayOnReadPhaseCurrent";
	public static final String RELAY_ON_READ_NEUTRAL_CURRENT_RESULT_TEST_NAME = "RelayOnReadNeutralCurrent";
	public static final String RELAY_OFF_READ_PHASE_CURRENT_RESULT_TEST_NAME = "RelayOffReadPhaseCurrent";
	public static final String RELAY_OFF_READ_NEUTRAL_CURRENT_RESULT_TEST_NAME = "RelayOffReadNeutralCurrent";
	public static final String RELAY_ON_CMD_RESULT_TEST_NAME = "RelayOnCmd";
	public static final String RELAY_OFF_CMD_RESULT_TEST_NAME = "RelayOffCmd";
	public static final String GSM_CHECK_WITHOUT_SIM = "GSMCheck_WithoutSIM";
	//public static final String DUT_SERIAL_NO_READ_CMD_RESULT_TEST_NAME = "Dut-SNo-ReadCmd";
	public static final String DUT_OPTICAL_DEFAULT_CALIBRATION_CMD_RESULT_TEST_NAME = "Dut-Opt-DfltCalb";
	public static final String DUT_OPTICAL_SERIAL_NO_READ_CMD_RESULT_TEST_NAME = "Dut-OptSNo-ReadCmd";
	public static final String DUT_OPTICAL_SERIAL_NO_SET_CMD_RESULT_TEST_NAME = "Dut-OptSNo-SetCmd";
	public static final String DUT_OPTICAL_HARDWARE_ID_NO_SET_CMD_RESULT_TEST_NAME = "Dut-OptHwNo-SetCmd";
	public static final String DUT_QR_SERIAL_NO_READ_CMD_RESULT_TEST_NAME = "Dut-QrSNo-ReadCmd";
	
	public static final String DEFAULT_DUT_HARDWARE_ID = "YZ";
	
	
	 //int[][] customOrder = {
	public static int[][] LDU_PLACEMENT_ORDER = {
			{3, 1}, {3, 2}, {3, 3}, {3, 4}, {3, 5}, {3, 6}, // Pallet 3
			{2, 1}, {2, 2}, {2, 3}, {2, 4}, {2, 5}, {2, 6}, // Pallet 2
			{1, 1}, {1, 2}, {1, 3}, {1, 4}, {1, 5}, {1, 6}, // Pallet 1
			{0, 1}, {0, 2}, {0, 3}, {0, 4}, {0, 5}, {0, 6}  // Pallet 0
	};
	 
//	public static final ArrayList<Integer> VERIFIC_PALLET1_POSITIONS  = new ArrayList<>(Arrays.asList(1,2,3,22,23,24));
//	public static final ArrayList<Integer> VERIFIC_PALLET2_POSITIONS  = new ArrayList<>(Arrays.asList(4,5,6,19,20,21));
//	public static final ArrayList<Integer> VERIFIC_PALLET3_POSITIONS  = new ArrayList<>(Arrays.asList(7,8,9,16,17,18));
//	public static final ArrayList<Integer> VERIFIC_PALLET4_POSITIONS  = new ArrayList<>(Arrays.asList(10,11,12,13,14,15));
	
	public static final ArrayList<Integer> VERIFIC_PALLET1_POSITIONS  = new ArrayList<>(Arrays.asList(1,2,3,4,5,6));
	public static final ArrayList<Integer> VERIFIC_PALLET2_POSITIONS  = new ArrayList<>(Arrays.asList(7,8,9,10,11,12));
	public static final ArrayList<Integer> VERIFIC_PALLET3_POSITIONS  = new ArrayList<>(Arrays.asList(13,14,15,16,17,18));
	public static final ArrayList<Integer> VERIFIC_PALLET4_POSITIONS  = new ArrayList<>(Arrays.asList(19,20,21,22,23,24));
	
//	public static final ArrayList<Integer> STA_NLD1_PALLET1_POSITIONS = new ArrayList<>(Arrays.asList(1,2,3,22,23,24));
//	public static final ArrayList<Integer> STA_NLD1_PALLET2_POSITIONS = new ArrayList<>(Arrays.asList(4,5,6,19,20,21));
//	public static final ArrayList<Integer> STA_NLD1_PALLET3_POSITIONS = new ArrayList<>(Arrays.asList(7,8,9,16,17,18));
//	public static final ArrayList<Integer> STA_NLD1_PALLET4_POSITIONS = new ArrayList<>(Arrays.asList(10,11,12,13,14,15));
	
	public static final ArrayList<Integer> STA_NLD1_PALLET1_POSITIONS  = new ArrayList<>(Arrays.asList(1,2,3,4,5,6));
	public static final ArrayList<Integer> STA_NLD1_PALLET2_POSITIONS  = new ArrayList<>(Arrays.asList(7,8,9,10,11,12));
	public static final ArrayList<Integer> STA_NLD1_PALLET3_POSITIONS  = new ArrayList<>(Arrays.asList(13,14,15,16,17,18));
	public static final ArrayList<Integer> STA_NLD1_PALLET4_POSITIONS  = new ArrayList<>(Arrays.asList(19,20,21,22,23,24));
	
//	public static final ArrayList<Integer> STA_NLD2_PALLET1_POSITIONS = new ArrayList<>(Arrays.asList(1,2,3,22,23,24));
//	public static final ArrayList<Integer> STA_NLD2_PALLET2_POSITIONS = new ArrayList<>(Arrays.asList(4,5,6,19,20,21));
//	public static final ArrayList<Integer> STA_NLD2_PALLET3_POSITIONS = new ArrayList<>(Arrays.asList(7,8,9,16,17,18));
//	public static final ArrayList<Integer> STA_NLD2_PALLET4_POSITIONS = new ArrayList<>(Arrays.asList(10,11,12,13,14,15));
	
	public static final ArrayList<Integer> STA_NLD2_PALLET1_POSITIONS  = new ArrayList<>(Arrays.asList(1,2,3,4,5,6));
	public static final ArrayList<Integer> STA_NLD2_PALLET2_POSITIONS  = new ArrayList<>(Arrays.asList(7,8,9,10,11,12));
	public static final ArrayList<Integer> STA_NLD2_PALLET3_POSITIONS  = new ArrayList<>(Arrays.asList(13,14,15,16,17,18));
	public static final ArrayList<Integer> STA_NLD2_PALLET4_POSITIONS  = new ArrayList<>(Arrays.asList(19,20,21,22,23,24));
	
	
	public static final int NUM_PALLETS_VERFIC_BAY = 4;
	public static final int NUM_PALLETS_STA_NLD1_BAY1 = 4;
	public static final int NUM_PALLETS_STA_NLD2_BAY2 = 4;
	
	
	
	public static final String DEVICE_COM_TYPE_SERIAL = "Serial";
	public static final String DEVICE_COM_TYPE_IP_INTERFACE = "IpNet";
	public static final ArrayList<String> DEVICE_COM_TYPE_LIST = new ArrayList<String>(Arrays.asList(
			DEVICE_COM_TYPE_SERIAL,
			DEVICE_COM_TYPE_IP_INTERFACE)
		);
	
	
	public static final ArrayList<String> WAITING_BAY_GROUP_LIST = new ArrayList<String>(Arrays.asList(
			WAITING_PP1_BAY_KEY,
			WAITING_PP2_BAY_KEY,
			WAITING_PP3_BAY_KEY,
			WAITING_PP4_BAY_KEY )
		);
	
/*	public static final ArrayList<String> WAITING_BAY_GROUP_QUEUE_LIST = new ArrayList<String>(Arrays.asList(
			WAITING_PP4_BAY_KEY,
			WAITING_PP3_BAY_KEY,
			WAITING_PP2_BAY_KEY,
			WAITING_PP1_BAY_KEY )
		);*/
	
	public static final ArrayList<String> VERIFIC_BAY_GROUP_LIST = new ArrayList<String>(Arrays.asList(
			VERIFICATION_PP1_BAY_KEY,
			VERIFICATION_PP2_BAY_KEY,
			VERIFICATION_PP3_BAY_KEY,
			VERIFICATION_PP4_BAY_KEY )
		);
	
	public static final ArrayList<String> STA_NLD1_BAY_KEY_GROUP_LIST = new ArrayList<String>(Arrays.asList(
			STA_NLD1_PP1_BAY_KEY,
			STA_NLD1_PP2_BAY_KEY,
			STA_NLD1_PP3_BAY_KEY,
			STA_NLD1_PP4_BAY_KEY )
		);

	public static final ArrayList<String> STA_NLD2_BAY_KEY_GROUP_LIST = new ArrayList<String>(Arrays.asList(
			STA_NLD2_PP1_BAY_KEY,
			STA_NLD2_PP2_BAY_KEY,
			STA_NLD2_PP3_BAY_KEY,
			STA_NLD2_PP4_BAY_KEY )
		);
	
	public static final ArrayList<String> STATE_SEQUENCE_LIST = new ArrayList<String>(Arrays.asList(
			LOADING_BAY_KEY,
			FT_BAY_KEY,
			HV_BAY_KEY,
			IR_BAY_KEY,
			CALIBRATION_BAY_KEY ,
			WAITING_BAY_KEY,
			VERIFICATION_BAY_KEY,
			STA_NLD1_BAY_KEY,
			STA_NLD2_BAY_KEY,
			//COMMUNICATION_BAY_KEY,
			UNLOADING_BAY_KEY,
			REJECTION_BAY_KEY )
		);
	
	
	public static final ArrayList<String> ALL_BAY_LIST = new ArrayList<String>(Arrays.asList(
			LOADING_BAY_KEY,
			FT_BAY_KEY,
			HV_BAY_KEY,
			IR_BAY_KEY,
			CALIBRATION_BAY_KEY ,
			WAITING_PP1_BAY_KEY,
			WAITING_PP2_BAY_KEY,
			WAITING_PP3_BAY_KEY,
			WAITING_PP4_BAY_KEY,
			VERIFICATION_PP1_BAY_KEY,
			VERIFICATION_PP2_BAY_KEY,
			VERIFICATION_PP3_BAY_KEY,
			VERIFICATION_PP4_BAY_KEY,
			STA_NLD1_PP1_BAY_KEY,
			STA_NLD1_PP2_BAY_KEY,
			STA_NLD1_PP3_BAY_KEY,
			STA_NLD1_PP4_BAY_KEY,
			STA_NLD2_PP1_BAY_KEY,
			STA_NLD2_PP2_BAY_KEY,
			STA_NLD2_PP3_BAY_KEY,
			STA_NLD2_PP4_BAY_KEY,
			//COMMUNICATION_BAY_KEY,
			UNLOADING_BAY_KEY,
			REJECTION_BAY_KEY )
		);
	
	private static Map <String, String> bayLookup = new LinkedHashMap <String, String>();
	
	public static final ArrayList<String> DEVICE_TYPE_LIST = new ArrayList<String>(Arrays.asList(
			DEVICE_TYPE_QR_SCANNER,
			DEVICE_TYPE_DUT,
			DEVICE_TYPE_OHM_METER,
			DEVICE_TYPE_VOLT_METER)
		);
	
	public static void init(){
		bayLookup.put(FT_BAY_DISPLAY_NAME, FT_BAY_KEY );
		bayLookup.put(HV_BAY_DISPLAY_NAME, HV_BAY_KEY );
		bayLookup.put(IR_BAY_DISPLAY_NAME, IR_BAY_KEY );
		bayLookup.put(CALIBRATION_BAY_DISPLAY_NAME,CALIBRATION_BAY_KEY  );
		bayLookup.put(COMMUNICATION_BAY_DISPLAY_NAME, COMMUNICATION_BAY_KEY );
		bayLookup.put(LOADING_BAY_DISPLAY_NAME, LOADING_BAY_KEY );
		bayLookup.put(REJECTION_BAY_DISPLAY_NAME, REJECTION_BAY_KEY );
		bayLookup.put(STA_NLD1_BAY_DISPLAY_NAME, STA_NLD1_BAY_KEY );
		bayLookup.put(STA_NLD2_BAY_DISPLAY_NAME, STA_NLD2_BAY_KEY );
		bayLookup.put(WAITING_BAY_DISPLAY_NAME,  WAITING_BAY_KEY);
		bayLookup.put(VERIFICATION_BAY_DISPLAY_NAME,VERIFICATION_BAY_KEY );
		bayLookup.put(UNLOADING_BAY_DISPLAY_NAME, UNLOADING_BAY_KEY );
		
		LDU_PLACEMENT_ORDER = new int[][] {
	        {3, 1}, {3, 2}, {3, 3}, {3, 4}, {3, 5}, {3, 6}, // Pallet 3
	        {2, 1}, {2, 2}, {2, 3}, {2, 4}, {2, 5}, {2, 6}, // Pallet 2
	        {1, 1}, {1, 2}, {1, 3}, {1, 4}, {1, 5}, {1, 6}, // Pallet 1
	        {0, 1}, {0, 2}, {0, 3}, {0, 4}, {0, 5}, {0, 6}  // Pallet 0
	    };
	}

	public static Map<String, String> getBayLookup() {
		return bayLookup;
	}

	public static void setBayLookup(Map<String, String> bayLookup) {
		ConstantConveyor.bayLookup = bayLookup;
	}
	
	public static boolean isFT_OPTICAL_PLACED() {
		return FT_OPTICAL_PLACED;
	}

	public static void setFT_OPTICAL_PLACED(boolean fT_OPTICAL_PLACED) {
		FT_OPTICAL_PLACED = fT_OPTICAL_PLACED;
	}

	public static boolean isFT_OPTICAL_REMOVED() {
		return FT_OPTICAL_REMOVED;
	}

	public static void setFT_OPTICAL_REMOVED(boolean fT_OPTICAL_REMOVED) {
		FT_OPTICAL_REMOVED = fT_OPTICAL_REMOVED;
	}

	public static boolean isCALIB_OPTICAL_PLACED() {
		return CALIB_OPTICAL_PLACED;
	}

	public static void setCALIB_OPTICAL_PLACED(boolean cALIB_OPTICAL_PLACED) {
		CALIB_OPTICAL_PLACED = cALIB_OPTICAL_PLACED;
	}

	public static boolean isCALIB_OPTICAL_REMOVED() {
		return CALIB_OPTICAL_REMOVED;
	}

	public static void setCALIB_OPTICAL_REMOVED(boolean cALIB_OPTICAL_REMOVED) {
		CALIB_OPTICAL_REMOVED = cALIB_OPTICAL_REMOVED;
	}

	public static boolean isCALIB_CURRENT_STABLE() {
		return CALIB_CURRENT_STABLE;
	}

	public static void setCALIB_CURRENT_STABLE(boolean cALIB_CURRENT_STABLE) {
		CALIB_CURRENT_STABLE = cALIB_CURRENT_STABLE;
	}

	public static boolean isFT_LDU_PLACEMENT() {
		return FT_LDU_PLACEMENT;
	}

	public static void setFT_LDU_PLACEMENT(boolean fT_LDU_PLACEMENT) {
		FT_LDU_PLACEMENT = fT_LDU_PLACEMENT;
	}

	public static boolean isVERIFIC_TESTING_DONE() {
		return VERIFIC_TESTING_DONE;
	}

	public static void setVERIFIC_TESTING_DONE(boolean vERIFIC_TESTING_DONE) {
		VERIFIC_TESTING_DONE = vERIFIC_TESTING_DONE;
	}

/*	public static boolean isVerificationBayPalletsCleared() {
		return VERIFICATION_BAY_PALLETS_CLEARED;
	}	*/
}
