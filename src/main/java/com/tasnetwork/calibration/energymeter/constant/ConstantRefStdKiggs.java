package com.tasnetwork.calibration.energymeter.constant;

import java.util.ArrayList;
import java.util.Arrays;

public class ConstantRefStdKiggs {

	//CH: 01 -- 09 channel (01: UU1 02:UU2 03:UU3 04: Ia 05: Ib 06: Ic 07:Ua 08:Ub 09:Uc)
	
	public final static int VOLTAGE_TAP_DIVISOR = 100;
	public final static int CURRENT_TAP_DIVISOR = 1000000;
	
	public final static long RSS_PREDEFINED_CONSTANT_DATA1 = 3600000;
	public final static long RSS_PREDEFINED_CONSTANT_DATA2 = 60000;
	
	public final static ArrayList<Float> CURRENT_TERMINAL_TAP_VALUE_LIST = new ArrayList<Float>
						(Arrays.asList(100f,50f,25f,10f,5f,2.5f,1.25f,1f,0.5f,0.25f,0.1f,0.05f,0.025f,0.0125f,0.00624f));
	
	public final static ArrayList<String> CURRENT_TERMINAL_TAP_STRING_LIST = new ArrayList<String>
		(Arrays.asList(	"0100000000",
						"0050000000",
						"0025000000",
						"0010000000",
						"0005000000",
						"0002500000",
						"0001250000",
						"0001000000",
						"0000500000",
						"0000250000",
						"0000100000",
						"0000050000",
						"0000025000",
						"0000012500",
						"0000006250"));
	
	public final static String CURRENT_TERMINAL_TAP_AUTO_MODE_CURRENT_TAP = "0000000000";
	
	public final static String CH_R_PHASE_VOLTAGE = "01";
	public final static String CH_R_PHASE_CURRENT = "04";
	public final static String CH_R_PHASE = "11";
	
	public final static String CMD_MEASURED_VOLT_CURRENT_HEADER = "A30103";	
	public final static String CMD_MEASURED_VOLT_CURRENT_PREFIX= "F6";	
	public static final String ER_MEASURED_VOLT_CURRENT_HEADER = "A301";
	public static final Integer ER_MEASURED_VOLT_CURRENT_DATA_EXPECTED_LENGTH = 17*2;
	
	
	public final static String CMD_GENERAL_REQUEST_HEADER = "A30102A0A0";	
	public static final Integer CMD_REF_STD_GENERAL_REQUEST_DATA_EXPECTED_LENGTH = 180;
	
	
	public final static String CMD_MEASURED_DEGREE_PHASE_HEADER = "A30103";	
	public final static String CMD_MEASURED_DEGREE_PHASE_PREFIX= "F5";	
	public static final String ER_MEASURED_DEGREE_PHASE_HEADER = "A301";
	public static final Integer ER_MEASURED_DEGREE_PHASE_DATA_EXPECTED_LENGTH = 11*2;
	
	public final static String CMD_MEASURED_POWER_FACTOR_HEADER = "A30103";	
	public final static String CMD_MEASURED_POWER_FACTOR_PREFIX= "F4";	
	public static final String ER_MEASURED_POWER_FACTOR_HEADER = "A30108F4";
	public static final Integer ER_MEASURED_POWER_FACTOR_DATA_EXPECTED_LENGTH = 11*2;
	
	public final static String CMD_MEASURED_APPARENT_POWER_HEADER = "A30103";	
	public final static String CMD_MEASURED_APPARENT_POWER_PREFIX= "F3";	
	public static final String ER_MEASURED_APPARENT_POWER_HEADER = "A30108F3";
	public static final Integer ER_MEASURED_APPARENT_POWER_DATA_EXPECTED_LENGTH = 11*2;
	
	public final static String CMD_MEASURED_REACTIVE_POWER_HEADER = "A30103";	
	public final static String CMD_MEASURED_REACTIVE_POWER_PREFIX= "F2";	
	public static final String ER_MEASURED_REACTIVE_POWER_HEADER = "A30108F2";
	public static final Integer ER_MEASURED_REACTIVE_POWER_DATA_EXPECTED_LENGTH = 11*2;
	
	
	public final static String CMD_MEASURED_ACTIVE_POWER_HEADER = "A30103";	
	public final static String CMD_MEASURED_ACTIVE_POWER_PREFIX= "F1";	
	public static final String ER_MEASURED_ACTIVE_POWER_HEADER = "A30108F1";
	public static final Integer ER_MEASURED_ACTIVE_POWER_DATA_EXPECTED_LENGTH = 11*2;
	
	public final static String CMD_MEASURED_FREQUENCY_HEADER = "A30102F0";	
	public final static String CMD_MEASURED_FREQUENCY_PREFIX= "F0";	
	public static final String ER_MEASURED_FREQUENCY_HEADER = "A30107F0";
	public static final Integer ER_MEASURED_FREQUENCY_DATA_EXPECTED_LENGTH = 10*2;
	
	//ToDo	
	
	
	
	public final static String CMD_BNC_PORT_OUTPUT_HEADER = "A30104";	//No response from the RSM for this message
	public final static String CMD_BNC_PORT_OUTPUT_PREFIX = "A5";	
	public final static String CMD_BNC_PORT_DATA1_ACTIVE_POWER = "00";	
	public final static String CMD_BNC_PORT_DATA1_REACTIVE_POWER  = "01";	
	public final static String CMD_BNC_PORT_DATA2_AUTOMATIC_CONSTANT  = "00";	
	
	
	
	public final static String CMD_ENERGY_ACCU_SETTING_MODE_HEADER = "A30105";	//No response from the RSM for this message
	public final static String CMD_ACCU_SETTING_MODE_PREFIX = "C0";	
	public final static String CMD_ACCU_SETTING_MODE_DATA1_SINGLE_PHASE_ACTIVE_POWER = "00";
	public final static String CMD_ACCU_SETTING_MODE_DATA1_SINGLE_PHASE_REACTIVE_POWER = "10";
	public final static String CMD_ACCU_SETTING_MODE_DATA1_SINGLE_ACTIVE_ENERGY = "13";	
	public final static String CMD_ACCU_SETTING_MODE_DATA1_BASIC_MEASURE = "27";	
		
	public final static String CMD_ACCU_SETTING_MODE_DATA2_FUNDAMENTAL_WAVE = "01";
	public final static String CMD_ACCU_SETTING_MODE_DATA3_COLLECTION_TIME = "01";
	//public static final String ER_ENERGY_ACCU_SETTING_MODE_HEADER = "";//No response from the RSM
	//public static final Integer ER_ENERGY_ACCU_SETTING_MODE_EXPECTED_LENGTH = 10*2;
	
	
	public final static String CMD_ENERGY_ACCU_CONTROL_HEADER = "A30103";	//No response from the RSM for this message
	public final static String CMD_ENERGY_ACCU_CONTROL_PREFIX = "CB";	
	public final static String CMD_ENERGY_ACCU_CONTROL_DATA1_START = "00";	
	public final static String CMD_ENERGY_ACCU_CONTROL_DATA1_STOP = "01";	
	
	
	public final static String CMD_ENERGY_ACCU_READ_HEADER = "A30102FCFC";	
	public static final String ER_ENERGY_ACCU_READ_HEADER = "A30114";//19
	public static final Integer ER_ENERGY_ACCU_READ_EXPECTED_LENGTH = 23*2;	
	
	public final static String CMD_CURRENT_RANGE_SETTING_HEADER = "A30108";	//No response from the RSM for this message
	public final static String CMD_CURRENT_RANGE_SETTING_PREFIX = "C2";	
	public final static String CMD_CURRENT_RANGE_SETTING_DATA1_AUTO = "00";
	public final static String CMD_CURRENT_RANGE_SETTING_DATA1_MANUAL = "01";
	
	
	public final static String CMD_REFSTD_METER_WORKING_MODE_HEADER = "A30103E401E5";
	public static final String ER_REFSTD_METER_WORKING_MODE_HEADER = "A3010DE4";//19
	public static final Integer ER_REFSTD_METER_WORKING_MODE_EXPECTED_LENGTH = 16*2;	
	
}
