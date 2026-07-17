package com.tasnetwork.calibration.energymeter.constant;

import java.util.ArrayList;
import java.util.Arrays;

import com.tasnetwork.calibration.energymeter.testprofiles.TestProfileType;

public class ConstantPowerSourceLscs {
	/* Config for serial port mapping*/
	public static final String SERIAL_PORT_POWER_SOURCE="SerialCommPowerSrc";
	public static final Integer PowerSrcDefaultBaudRate = 9600;
	
	
	public static final String CMD_PWR_SRC_SEPERATOR2 = ",";

	/*Commands for Power source*/
	
	//public static final String VOLTAGE_RESOLUTION = "%03.0f";
	
	// for Propower feed back display
	
	//public static final String CMD_PWR_SRC_READ_FREQ_HDR = "0";
	public static final String CMD_PWR_SRC_READ_FEEDBACK_ALL_DATA_HDR = "0";
	public static final String CMD_PWR_SRC_R_PHASE_READ_VOLT_HDR = "1";
	public static final String CMD_PWR_SRC_Y_PHASE_READ_VOLT_HDR = "2";
	public static final String CMD_PWR_SRC_B_PHASE_READ_VOLT_HDR = "3";
	
	public static final String CMD_PWR_SRC_R_PHASE_READ_CURRENT_HDR = "4";
	public static final String CMD_PWR_SRC_Y_PHASE_READ_CURRENT_HDR = "5";
	public static final String CMD_PWR_SRC_B_PHASE_READ_CURRENT_HDR = "6";
	
	public static final String CMD_PWR_SRC_R_PHASE_READ_PHASE_DEGREE_HDR = "7";
	public static final String CMD_PWR_SRC_Y_PHASE_READ_PHASE_DEGREE_HDR = "8";
	public static final String CMD_PWR_SRC_B_PHASE_READ_PHASE_DEGREE_HDR = "9";
	public final static int    ER_PWR_SRC_READ_FEEDBACK_ALL_PARAM_3PHASE_TERMINATOR_COUNT  = 10;
	public final static int    ER_PWR_SRC_READ_FEEDBACK_ALL_PARAM_1PHASE_TERMINATOR_COUNT  = 4;
	public static final String ER_PWR_SRC_SEPERATOR = ",";
	public static final String ER_PWR_SRC_READ_ALL_HEADER = "ALL=";
	
	// For Reference std feed back fine tune control
	public static final String CMD_PWR_SRC_R_PHASE_VOLT_INC_LEVEL1_HDR = "L";
	public static final String ER_PWR_SRC_R_PHASE_VOLT_INC_LEVEL1 = "LAK";
	
	public static final String CMD_PWR_SRC_R_PHASE_VOLT_INC_LEVEL2_HDR = "M";
	public static final String ER_PWR_SRC_R_PHASE_VOLT_INC_LEVEL2 = "MAK";
	
	public static final String CMD_PWR_SRC_R_PHASE_VOLT_INC_LEVEL3_HDR = "N";
	public static final String ER_PWR_SRC_R_PHASE_VOLT_INC_LEVEL3 = "NAK";
	
	public static final String CMD_PWR_SRC_R_PHASE_VOLT_DEC_LEVEL1_HDR = "l";// lower case of L
	public static final String ER_PWR_SRC_R_PHASE_VOLT_DEC_LEVEL1 = "lAK";
	
	public static final String CMD_PWR_SRC_R_PHASE_VOLT_DEC_LEVEL2_HDR = "m";
	public static final String ER_PWR_SRC_R_PHASE_VOLT_DEC_LEVEL2 = "mAK";
	
	public static final String CMD_PWR_SRC_R_PHASE_VOLT_DEC_LEVEL3_HDR = "n";
	public static final String ER_PWR_SRC_R_PHASE_VOLT_DEC_LEVEL3 = "nAK";
	
	public static final String CMD_PWR_SRC_R_PHASE_CURRENT_INC_LEVEL1_HDR = "L";
	public static final String ER_PWR_SRC_R_PHASE_CURRENT_INC_LEVEL1 = "LAK";
	
	public static final String CMD_PWR_SRC_R_PHASE_CURRENT_DEC_LEVEL1_HDR = "l";
	public static final String ER_PWR_SRC_R_PHASE_CURRENT_DEC_LEVEL1 = "lAK";
	
	public static final String CMD_PWR_SRC_R_PHASE_DEGREE_INC_LEVEL1_HDR = "L";
	public static final String ER_PWR_SRC_R_PHASE_DEGREE_INC_LEVEL1 = "LAK";
	
	public static final String CMD_PWR_SRC_R_PHASE_DEGREE_DEC_LEVEL1_HDR = "l";
	public static final String ER_PWR_SRC_R_PHASE_DEGREE_DEC_LEVEL1 = "lAK";
	
	
	
	public static final String CMD_PWR_SRC_Y_PHASE_VOLT_INC_LEVEL1_HDR = "L";
	public static final String ER_PWR_SRC_Y_PHASE_VOLT_INC_LEVEL1 = "LAK";
	
	public static final String CMD_PWR_SRC_Y_PHASE_VOLT_INC_LEVEL2_HDR = "M";
	public static final String ER_PWR_SRC_Y_PHASE_VOLT_INC_LEVEL2 = "MAK";
	
	public static final String CMD_PWR_SRC_Y_PHASE_VOLT_INC_LEVEL3_HDR = "N";
	public static final String ER_PWR_SRC_Y_PHASE_VOLT_INC_LEVEL3 = "NAK";
	
	public static final String CMD_PWR_SRC_Y_PHASE_VOLT_DEC_LEVEL1_HDR = "l";
	public static final String ER_PWR_SRC_Y_PHASE_VOLT_DEC_LEVEL1 = "lAK";
	
	public static final String CMD_PWR_SRC_Y_PHASE_VOLT_DEC_LEVEL2_HDR = "m";
	public static final String ER_PWR_SRC_Y_PHASE_VOLT_DEC_LEVEL2 = "mAK";
	
	public static final String CMD_PWR_SRC_Y_PHASE_VOLT_DEC_LEVEL3_HDR = "n";
	public static final String ER_PWR_SRC_Y_PHASE_VOLT_DEC_LEVEL3 = "nAK";
	
	public static final String CMD_PWR_SRC_Y_PHASE_CURRENT_INC_LEVEL1_HDR = "L";
	public static final String ER_PWR_SRC_Y_PHASE_CURRENT_INC_LEVEL1 = "LAK";
	
	public static final String CMD_PWR_SRC_Y_PHASE_CURRENT_DEC_LEVEL1_HDR = "l";
	public static final String ER_PWR_SRC_Y_PHASE_CURRENT_DEC_LEVEL1 = "lAK";
	
	public static final String CMD_PWR_SRC_Y_PHASE_DEGREE_INC_LEVEL1_HDR = "L";
	public static final String ER_PWR_SRC_Y_PHASE_DEGREE_INC_LEVEL1 = "LAK";
	
	public static final String CMD_PWR_SRC_Y_PHASE_DEGREE_DEC_LEVEL1_HDR = "l";
	public static final String ER_PWR_SRC_Y_PHASE_DEGREE_DEC_LEVEL1 = "lAK";
	
	
	public static final String CMD_PWR_SRC_B_PHASE_VOLT_INC_LEVEL1_HDR = "L";
	public static final String ER_PWR_SRC_B_PHASE_VOLT_INC_LEVEL1 = "LAK";
	
	public static final String CMD_PWR_SRC_B_PHASE_VOLT_INC_LEVEL2_HDR = "M";
	public static final String ER_PWR_SRC_B_PHASE_VOLT_INC_LEVEL2 = "MAK";
	
	public static final String CMD_PWR_SRC_B_PHASE_VOLT_INC_LEVEL3_HDR = "N";
	public static final String ER_PWR_SRC_B_PHASE_VOLT_INC_LEVEL3 = "NAK";
	
	public static final String CMD_PWR_SRC_B_PHASE_VOLT_DEC_LEVEL1_HDR = "l";
	public static final String ER_PWR_SRC_B_PHASE_VOLT_DEC_LEVEL1 = "lAK";
	
	public static final String CMD_PWR_SRC_B_PHASE_VOLT_DEC_LEVEL2_HDR = "m";
	public static final String ER_PWR_SRC_B_PHASE_VOLT_DEC_LEVEL2 = "mAK";
	
	public static final String CMD_PWR_SRC_B_PHASE_VOLT_DEC_LEVEL3_HDR = "n";
	public static final String ER_PWR_SRC_B_PHASE_VOLT_DEC_LEVEL3 = "nAK";
	
	public static final String CMD_PWR_SRC_B_PHASE_CURRENT_INC_LEVEL1_HDR = "L";
	public static final String ER_PWR_SRC_B_PHASE_CURRENT_INC_LEVEL1 = "LAK";
	
	public static final String CMD_PWR_SRC_B_PHASE_CURRENT_DEC_LEVEL1_HDR = "l";
	public static final String ER_PWR_SRC_B_PHASE_CURRENT_DEC_LEVEL1 = "lAK";
	
	public static final String CMD_PWR_SRC_B_PHASE_DEGREE_INC_LEVEL1_HDR = "L";
	public static final String ER_PWR_SRC_B_PHASE_DEGREE_INC_LEVEL1 = "LAK";
	
	public static final String CMD_PWR_SRC_B_PHASE_DEGREE_DEC_LEVEL1_HDR = "l";
	public static final String ER_PWR_SRC_B_PHASE_DEGREE_DEC_LEVEL1 = "lAK";
	
	
	
	// For ICT on/Off mode
	public static final String CMD_PWR_SRC_MAIN_ICT_MODE_ON_HDR = "KTm";
	public static final String CMD_PWR_SRC_MAIN_ICT_MODE_OFF_HDR = "KTr";
	
	public static final String ER_PWR_SRC_MAIN_ICT_ON_MODE = "iACK";
	public static final String ER_PWR_SRC_MAIN_ICT_OFF_MODE = "jACK";
	
	public static final String CMD_PWR_SRC_MAIN_CT_MODE_HDR = "CTm";
	public static final String CMD_PWR_SRC_NEUTRAL_CT_MODE_HDR = "CTn";
	public static final String CMD_PWR_SRC_ALL_CT_MODE_OFF_HDR = "CTr";
	
	public static final String CMD_PWR_SRC_MAIN_CT_MODE_HDRV2 = "M";
	public static final String CMD_PWR_SRC_NEUTRAL_CT_MODE_HDRV2 = "N";
	public static final String CMD_PWR_SRC_ALL_CT_MODE_OFF_HDRV2 = "R";
	
	public static final String ER_PWR_SRC_MAIN_CT_MODE = "mACK";
	public static final String ER_PWR_SRC_NEUTRAL_CT_MODE = "nACK";
	public static final String ER_PWR_SRC_ALL_CT_MODE_OFF = "rACK";
	
	public static final String CMD_PWR_SRC_FRQ_PREFIX = "f";
	public static final String CMD_PWR_SRC_SINGLE_PHASE_INIT_PREFIX = "A";
	public static final String CMD_PWR_SRC_SINGLE_PHASE_INIT_PREFIX_WITH_SPACE = "AA";
	public static final String CMD_PWR_SRC_SINGLE_PHASE_INIT_ER = "AACK";
	public static final String CMD_PWR_SRC_DATA_ERROR_RESPONSE = "1F";//"V1FI1F";
	public static final String CMD_PWR_SRC_SET_CREEP_ER = "I1F";
	public static final String CMD_PWR_SRC_ACK_ERROR_RESPONSE = "NAK";
	public static final String CMD_PWR_SRC_DATA_EXPECTED_RESPONSE = "sACK";
	public static final String CMD_PWR_SRC_U1_PREFIX = "v1";
	public static final String CMD_PWR_SRC_I1_PREFIX = "i1";
	public static final String CMD_PWR_SRC_PF1_PREFIX = "p1";
	
	public static final String CMD_PWR_SRC_U2_PREFIX = "v2";
	public static final String CMD_PWR_SRC_I2_PREFIX = "i2";
	public static final String CMD_PWR_SRC_PF2_PREFIX = "p2";
	
	public static final String CMD_PWR_SRC_U3_PREFIX = "v3";
	public static final String CMD_PWR_SRC_I3_PREFIX = "i3";
	public static final String CMD_PWR_SRC_PF3_PREFIX = "p3";
	
	public static final String CMD_PWR_SRC_SELECT_CUR_RELAY_PREFIX = "i4"; 
	
	
	public static final String CMD_PWR_SRC_SELECT_CUR_RELAY_50_MILLI_AMPS  = "210"; 
	public static final String CMD_PWR_SRC_SELECT_CUR_RELAY_120_MILLI_AMPS = "211"; 
	public static final String CMD_PWR_SRC_SELECT_CUR_RELAY_1_AMPS         = "212";
	public static final String CMD_PWR_SRC_SELECT_CUR_RELAY_10A            = "213"; 
	public static final String CMD_PWR_SRC_SELECT_CUR_RELAY_60_AMPS        = "214";
	public static final String CMD_PWR_SRC_SELECT_CUR_RELAY_120_AMPS       = "215";
	
	
	public static final String CMD_PWR_SRC_MASTER_HARMONICS_ENABLE = "He"; 
	public static final String CMD_PWR_SRC_MASTER_HARMONICS_DISABLE = "Hd"; 
	
	public static final String CMD_PWR_SRC_SEPERATOR = " ";
	public static final String CMD_PWR_SRC_DATA_TERMINATOR = "s";
	public static final String CMD_PWR_SRC_OFF = "E";
	//public static final String CMD_PWR_SRC_OFF_WITH_SPACE = "E ";
	public static final String CMD_PWR_SRC_OFF_ER = "EACK";
	//public static final String CMD_PWR_SRC_INIT_PASSWORD = "1.4501";//ceig
	//public static final String CMD_PWR_SRC_INIT_PASSWORD = "1.4531"; //elmeasure
	//public static final String CMD_PWR_SRC_INIT_PASSWORD = "3.6028"; //sands
	//public static final String CMD_PWR_SRC_INIT_PASSWORD = "3.50295"; // for LECS jan 2022
	public static final String CMD_PWR_SRC_INIT_PASSWORD_ER = "ok MODE:v/h";
	public static final String CMD_PWR_SRC_INIT_SET_HYPERT_MODE = "h";
	public static final String CMD_PWR_SRC_INIT_SET_DAC_MODE = "DAC";
	public static final String CMD_PWR_SRC_INIT_SET_HYPERT_MODE_ER = "hACK";
	
	
	public static final String CMD_PWR_SRC_METER_TYPE_OPTION_INIT_PASSWORD_ER = "ok 1 or 3 phase?";	
	public static final String CMD_PWR_SRC_METER_TYPE_OPTION_SELECTION_1PHASE = "1";
	public static final String CMD_PWR_SRC_METER_TYPE_OPTION_SELECTION_3PHASE  = "3";
	
	public static final String CMD_PWR_SRC_INIT_SET_APP_MODE = "v";
	public static final String CMD_PWR_SRC_INIT_SET_APP_MODE_ER = "vACK";
	
	public static final String CMD_PWR_SRC_SET = "SET";
	
	public static final String CMD_PWR_SRC_SET_ALL_CURRENT_ZERO = "I ";//"I";
	public static final String CMD_PWR_SRC_SET_ALL_CURRENT_ZERO_ER = "ZaK";

/*	public static final String CMD_PWR_SRC_U2_PREFIX = "U2,";
	public static final String CMD_PWR_SRC_I2_PREFIX = "I2,";
	public static final String CMD_PWR_SRC_PF2_PREFIX = "W2,";
	public static final String CMD_PWR_SRC_U3_PREFIX = "U3,";
	public static final String CMD_PWR_SRC_I3_PREFIX = "I3,";
	public static final String CMD_PWR_SRC_PF3_PREFIX = "W3,";*/

	//public static final String CMD_PWR_SRC_PHASE_RY_PREFIX = "PH2,";
	//public static final String CMD_PWR_SRC_PHASE_RB_PREFIX = "PH3,";
	
	public static final String CMD_PWR_SRC_PHASE_REVERSE_PREFIX = "M";
	
	
	public static final String CMD_PWR_SRC_SET_EXPECTED_RESPONSE = "=1";
	
	public static final String CMD_PWR_SRC_HAR_I1_PREFIX = "OWI1,";
	public static final String CMD_PWR_SRC_HAR_U1_PREFIX = "OWU1,";
	public static final String CMD_PWR_SRC_HAR_I2_PREFIX = "OWI2,";
	public static final String CMD_PWR_SRC_HAR_U2_PREFIX = "OWU2,";
	public static final String CMD_PWR_SRC_HAR_I3_PREFIX = "OWI3,";
	public static final String CMD_PWR_SRC_HAR_U3_PREFIX = "OWU3,";
	public static final String CMD_PWR_SRC_COMMA = ",";
	public static final String CMD_PWR_SRC_RESET = "R";
	
	
	public static final String CMD_PWR_SRC_FINE_CONTROL_V1_INC_LEVEL1 = "L";
	public static final String CMD_PWR_SRC_FINE_CONTROL_V1_INC_LEVEL2 = "M";
	public static final String CMD_PWR_SRC_FINE_CONTROL_V1_INC_LEVEL3 = "N";
	public static final String CMD_PWR_SRC_FINE_CONTROL_V1_DEC_LEVEL1 = "l";
	public static final String CMD_PWR_SRC_FINE_CONTROL_V1_DEC_LEVEL2 = "m";
	public static final String CMD_PWR_SRC_FINE_CONTROL_V1_DEC_LEVEL3 = "n";

	public static final String CMD_PWR_SRC_FINE_CONTROL_V2_INC_LEVEL1 = "P";
	public static final String CMD_PWR_SRC_FINE_CONTROL_V2_INC_LEVEL2 = "Q";
	public static final String CMD_PWR_SRC_FINE_CONTROL_V2_INC_LEVEL3 = "R";
	public static final String CMD_PWR_SRC_FINE_CONTROL_V2_DEC_LEVEL1 = "p";
	public static final String CMD_PWR_SRC_FINE_CONTROL_V2_DEC_LEVEL2 = "q";
	public static final String CMD_PWR_SRC_FINE_CONTROL_V2_DEC_LEVEL3 = "r";

	public static final String CMD_PWR_SRC_FINE_CONTROL_V3_INC_LEVEL1 = "T";
	public static final String CMD_PWR_SRC_FINE_CONTROL_V3_INC_LEVEL2 = "U";
	public static final String CMD_PWR_SRC_FINE_CONTROL_V3_INC_LEVEL3 = "V";
	public static final String CMD_PWR_SRC_FINE_CONTROL_V3_DEC_LEVEL1 = "t";
	public static final String CMD_PWR_SRC_FINE_CONTROL_V3_DEC_LEVEL2 = "u";
	public static final String CMD_PWR_SRC_FINE_CONTROL_V3_DEC_LEVEL3 = "v";

	public static final String CMD_PWR_SRC_FINE_CONTROL_I1_INC_LEVEL1 = "X";
	public static final String CMD_PWR_SRC_FINE_CONTROL_I1_DEC_LEVEL1 = "x";
	

	public static final String CMD_PWR_SRC_FINE_CONTROL_I2_INC_LEVEL1 = "Y";
	public static final String CMD_PWR_SRC_FINE_CONTROL_I2_DEC_LEVEL1 = "y";

	public static final String CMD_PWR_SRC_FINE_CONTROL_I3_INC_LEVEL1 = "Z";
	public static final String CMD_PWR_SRC_FINE_CONTROL_I3_DEC_LEVEL1 = "z";

	public static final String CMD_PWR_SRC_FINE_CONTROL_PF1_INC_LEVEL1 = "B";
	public static final String CMD_PWR_SRC_FINE_CONTROL_PF1_DEC_LEVEL1 = "b";
	public static final String CMD_PWR_SRC_FINE_CONTROL_PF1_NO_CHANGE =  "!";//"0";//ZERO

	public static final String CMD_PWR_SRC_FINE_CONTROL_PF2_INC_LEVEL1 = "G";
	public static final String CMD_PWR_SRC_FINE_CONTROL_PF2_DEC_LEVEL1 = "g";

	public static final String CMD_PWR_SRC_FINE_CONTROL_PF3_INC_LEVEL1 = "W";
	public static final String CMD_PWR_SRC_FINE_CONTROL_PF3_DEC_LEVEL1 = "w";
	
	//public static final String CMD_PWR_SRC_FINE_CONTROL_DEFAULT_MULTIPLIER =  "1";
	
	
		
	public static final String CMD_PWR_SRC_ER_FINE_CONTROL_V1_INC_LEVEL1 = "LAK";
	public static final String CMD_PWR_SRC_ER_FINE_CONTROL_V1_INC_LEVEL2 = "MAK";
	public static final String CMD_PWR_SRC_ER_FINE_CONTROL_V1_INC_LEVEL3 = "NAK";
	public static final String CMD_PWR_SRC_ER_FINE_CONTROL_V1_DEC_LEVEL1 = "lAK";
	public static final String CMD_PWR_SRC_ER_FINE_CONTROL_V1_DEC_LEVEL2 = "mAK";
	public static final String CMD_PWR_SRC_ER_FINE_CONTROL_V1_DEC_LEVEL3 = "nAK";

	public static final String CMD_PWR_SRC_ER_FINE_CONTROL_V2_INC_LEVEL1 = "PAK";
	public static final String CMD_PWR_SRC_ER_FINE_CONTROL_V2_INC_LEVEL2 = "QAK";
	public static final String CMD_PWR_SRC_ER_FINE_CONTROL_V2_INC_LEVEL3 = "RAK";
	public static final String CMD_PWR_SRC_ER_FINE_CONTROL_V2_DEC_LEVEL1 = "pAK";
	public static final String CMD_PWR_SRC_ER_FINE_CONTROL_V2_DEC_LEVEL2 = "qAK";
	public static final String CMD_PWR_SRC_ER_FINE_CONTROL_V2_DEC_LEVEL3 = "rAK";

	public static final String CMD_PWR_SRC_ER_FINE_CONTROL_V3_INC_LEVEL1 = "TAK";
	public static final String CMD_PWR_SRC_ER_FINE_CONTROL_V3_INC_LEVEL2 = "UAK";
	public static final String CMD_PWR_SRC_ER_FINE_CONTROL_V3_INC_LEVEL3 = "VAK";
	public static final String CMD_PWR_SRC_ER_FINE_CONTROL_V3_DEC_LEVEL1 = "tAK";
	public static final String CMD_PWR_SRC_ER_FINE_CONTROL_V3_DEC_LEVEL2 = "uAK";
	public static final String CMD_PWR_SRC_ER_FINE_CONTROL_V3_DEC_LEVEL3 = "vAK";

	public static final String CMD_PWR_SRC_ER_FINE_CONTROL_I1_INC_LEVEL1 = "XAK";
	public static final String CMD_PWR_SRC_ER_FINE_CONTROL_I1_DEC_LEVEL1 = "xAK";

	public static final String CMD_PWR_SRC_ER_FINE_CONTROL_I2_INC_LEVEL1 = "YAK";
	public static final String CMD_PWR_SRC_ER_FINE_CONTROL_I2_DEC_LEVEL1 = "yAK";

	public static final String CMD_PWR_SRC_ER_FINE_CONTROL_I3_INC_LEVEL1 = "ZAK";
	public static final String CMD_PWR_SRC_ER_FINE_CONTROL_I3_DEC_LEVEL1 = "zAK";

	public static final String CMD_PWR_SRC_ER_FINE_CONTROL_PF1_INC_LEVEL1 = "BAK";
	public static final String CMD_PWR_SRC_ER_FINE_CONTROL_PF1_DEC_LEVEL1 = "bAK";

	public static final String CMD_PWR_SRC_ER_FINE_CONTROL_PF2_INC_LEVEL1 = "GAK";
	public static final String CMD_PWR_SRC_ER_FINE_CONTROL_PF2_DEC_LEVEL1 = "gAK";

	public static final String CMD_PWR_SRC_ER_FINE_CONTROL_PF3_INC_LEVEL1 = "WAK";
	public static final String CMD_PWR_SRC_ER_FINE_CONTROL_PF3_DEC_LEVEL1 = "wAK";
	
	
	public static final int FINETUNE_PHASE_ANGLE_ADJUSTMENT_LEVEL1 = 1;
	public static final int FINETUNE_PHASE_ANGLE_ADJUSTMENT_LEVEL2 = 2;
	public static final int FINETUNE_PHASE_ANGLE_ADJUSTMENT_LEVEL3 = 3;
	
	public static final int FINETUNE_VOLT_ADJUSTMENT_LEVEL1 = 1;
	public static final int FINETUNE_VOLT_ADJUSTMENT_LEVEL2 = 2;
	public static final int FINETUNE_VOLT_ADJUSTMENT_LEVEL3 = 3;
	
	public static final int FINETUNE_CURRENT_ADJUSTMENT_LEVEL1 = 1;
	public static final int FINETUNE_CURRENT_ADJUSTMENT_LEVEL2 = 2;
	public static final int FINETUNE_CURRENT_ADJUSTMENT_LEVEL3 = 3;
	
	public static final String CMD_PWR_SRC_ER_FINE_CONTROL_READY = "()";
	
	public static final String VOLTAGE_RESOLUTION = "%03.01f";
	public static final String CURRENT_RESOLUTION_LOW = "%02.03f";
	public static final String CURRENT_RESOLUTION_HIGH = "%02.01f";
	public static final float CURRENT_RESOLUTION_THRESHOLD = 5.1f;//10.0f; // created error for 10.0 Amps with lscs power source
	public static final String PHASE_RESOLUTION = "%02.01f";
	public static final String ENERGY_RESOLUTION = "%.2f";
	
	public static final int POWER_SRC_3P4W_SINE_IMPORT_REACTIVE_ZPF_ANGLE = 90;
	public static final int POWER_SRC_3P4W_SINE_EXPORT_REACTIVE_ZPF_ANGLE = -90;
	public static int POWER_SRC_SINE_REACTIVE_ZPF_ANGLE = POWER_SRC_3P4W_SINE_IMPORT_REACTIVE_ZPF_ANGLE;//90;
	
	public static final int POWER_SRC_3P4W_COS_IMPORT_ACTIVE_UPF_ANGLE = 0;
	public static final int POWER_SRC_3P4W_COS_EXPORT_ACTIVE_UPF_ANGLE = -180;
	public static int POWER_SRC_COS_ACTIVE_UPF_ANGLE = POWER_SRC_3P4W_COS_IMPORT_ACTIVE_UPF_ANGLE;
	
	public static final String EXPORT_MODE = "EXPORT";
	public static final String IMPORT_MODE = "IMPORT";
	
	
	public static final String CMD_PWR_SRC_FINE_CONTROL_DEFAULT_MULTIPLIER =  "1";
	
	
	public static final String CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_1 =  "1";
	public static final String CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_2 =  "2";
	public static final String CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_3 =  "3";
	public static final String CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_4 =  "4";
	public static final String CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_5 =  "5";
	public static final String CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_6 =  "6";
	public static final String CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_7 =  "7";
	public static final String CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_8 =  "8";
	public static final String CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_9 =  "9";
	
	public static final String CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_HALF   =  "0"; // not used
	
	public static final String CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_11 =  "a";
	public static final String CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_12 =  "b";
	public static final String CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_13 =  "c";
	public static final String CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_14 =  "d";
	public static final String CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_15 =  "e";
	public static final String CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_16 =  "f";
	public static final String CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_17 =  "g";
	public static final String CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_18 =  "h";
	public static final String CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_19 =  "i";
	public static final String CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_20 =  "j";
	
	public static final ArrayList<String> POWER_RESET_TEST_TYPES = new ArrayList<>(Arrays.asList(
			TestProfileType.InfluenceFreq.toString(),
/*			TestProfileType.VoltageUnbalance.toString(),*/
			TestProfileType.PhaseReversal.toString(),
			TestProfileType.CustomTest.toString(),
			TestProfileType.InfluenceHarmonic.toString()));

}
