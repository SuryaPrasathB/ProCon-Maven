package com.tasnetwork.calibration.energymeter.constant;

import com.tasnetwork.calibration.energymeter.util.GuiUtils;

public class ConstantRefStdKre {

	
	public final static String INVALID_ENERGY_DATA = "-1.#IND";
	public final static String CURRENT_LOW_THRESHOLD = "1.0";
	public final static String CURRENT_MAX = "120.0";
	public final static String CMD_TERMINATOR = GuiUtils.StringToHex("/!");
	public final static String CMD_RESPONSE_HDR = GuiUtils.StringToHex("KRE://");
	public final static String CMD_READ_SETTING_HDR = GuiUtils.StringToHex("KRE://READ_SET,");	
	public final static String CMD_SEPERATOR = GuiUtils.StringToHex(",");
	public final static String ER_READ_SETTING_ACK = GuiUtils.StringToHex("KRE://CMC=");
	
	public final static String ER_TERMINATOR = GuiUtils.StringToHex("/!");
	public final static String ER_REF_STD_SEPERATOR = GuiUtils.StringToHex(",");
	
	public final static String CMD_READ_BASIC_DATA_HDR = GuiUtils.StringToHex("KRE://READ_BD,");	
	public final static String ER_READ_BASIC_DATA_ACK = GuiUtils.StringToHex("KRE://FREQ=");
	
	public final static String CMD_WRITE_SETTING_HDR = GuiUtils.StringToHex("KRE://");	
	public final static String ER_WRITE_SETTING_ACK = GuiUtils.StringToHex("KRE://CMC=");
	
	public final static String CMD_ENERGY_ACC_START_HDR = GuiUtils.StringToHex("KRE://ENERGY_ACC_START,");	
	public final static String ER_ENERGY_ACC_START_ACK = GuiUtils.StringToHex("KRE://ENERGY_ACC_START");
	
	public final static int    ER_REF_STD_READ_ALL_PARAM_TERMINATOR_COUNT  = 1;
	
	public final static String RPHASE_PHASE_ANGLE_PARSED_DATA_HEADER = "a1=";
	public final static String YPHASE_PHASE_ANGLE_PARSED_DATA_HEADER = "a2=";
	public final static String BPHASE_PHASE_ANGLE_PARSED_DATA_HEADER = "a3=";
	
	public final static String FREQ_PARSED_DATA_HEADER = "FREQ=";
	public final static String RPHASE_VOLTAGE_PARSED_DATA_HEADER = "U1=";
	public final static String RPHASE_CURRENT_PARSED_DATA_HEADER = "I1=";
	public final static String RPHASE_PF_PARSED_DATA_HEADER = "COS1=";
	public final static String RPHASE_ACTIVE_POWER_PARSED_DATA_HEADER = "P1=";
	public final static String RPHASE_REACTIVE_POWER_PARSED_DATA_HEADER = "Q1=";
	public final static String RPHASE_APPARENT_POWER_PARSED_DATA_HEADER = "S1=";
	
	public final static String RPHASE_ACTIVE_ENERGY_PARSED_DATA_HEADER = "EP1=";
	public final static String RPHASE_REACTIVE_ENERGY_PARSED_DATA_HEADER = "EQ1=";
	public final static String RPHASE_APPARENT_ENERGY_PARSED_DATA_HEADER = "ES1=";
	
	public final static String YPHASE_VOLTAGE_PARSED_DATA_HEADER = "U2=";
	public final static String YPHASE_CURRENT_PARSED_DATA_HEADER = "I2=";
	public final static String YPHASE_PF_PARSED_DATA_HEADER = "COS2=";
	public final static String YPHASE_ACTIVE_POWER_PARSED_DATA_HEADER = "P2=";
	public final static String YPHASE_REACTIVE_POWER_PARSED_DATA_HEADER = "Q2=";
	public final static String YPHASE_APPARENT_POWER_PARSED_DATA_HEADER = "S2=";
	
	public final static String YPHASE_ACTIVE_ENERGY_PARSED_DATA_HEADER = "EP2=";
	public final static String YPHASE_REACTIVE_ENERGY_PARSED_DATA_HEADER = "EQ2=";
	public final static String YPHASE_APPARENT_ENERGY_PARSED_DATA_HEADER = "ES2=";
	
	public final static String BPHASE_VOLTAGE_PARSED_DATA_HEADER = "U3=";
	public final static String BPHASE_CURRENT_PARSED_DATA_HEADER = "I3=";
	public final static String BPHASE_PF_PARSED_DATA_HEADER = "COS3=";
	public final static String BPHASE_ACTIVE_POWER_PARSED_DATA_HEADER = "P3=";
	public final static String BPHASE_REACTIVE_POWER_PARSED_DATA_HEADER = "Q3=";
	public final static String BPHASE_APPARENT_POWER_PARSED_DATA_HEADER = "S3=";
	
	public final static String BPHASE_ACTIVE_ENERGY_PARSED_DATA_HEADER = "EP3=";
	public final static String BPHASE_REACTIVE_ENERGY_PARSED_DATA_HEADER = "EQ3=";
	public final static String BPHASE_APPARENT_ENERGY_PARSED_DATA_HEADER = "ES3=";
	
	
	public final static String SETTING_WIRING_MODE_DATA_HEADER = "CMC=";
	public final static String SETTING_ISTALL_PARSED_DATA_HEADER = "ISTALL=";
	public final static String SETTING_OUTPUT1_PULSE_CONSTANT_PARSED_DATA_HEADER = "COUT_1=";
	public final static String SETTING_OUTPUT2_PULSE_CONSTANT_PARSED_DATA_HEADER = "COUT_2=";
	public final static String SETTING_INPUT1_PULSE_CONSTANT_PARSED_DATA_HEADER = "CIN_1=";
	public final static String SETTING_INPUT2_PULSE_CONSTANT_PARSED_DATA_HEADER = "CIN_2=";
	public final static String SETTING_INPUT3_PULSE_CONSTANT_PARSED_DATA_HEADER = "CIN_3=";
	public final static String SETTING_INPUT4_PULSE_CONSTANT_PARSED_DATA_HEADER = "CIN_4=";
	public final static String SETTING_OUTPUT1_PULSE_TYPE_PARSED_DATA_HEADER = "PW_1=";
	public final static String SETTING_OUTPUT2_PULSE_TYPE_PARSED_DATA_HEADER = "PW_2=";
	
	public final static String SETTING_INPUT1_PULSE_TYPE_PARSED_DATA_HEADER = "PW_IN1=";
	public final static String SETTING_INPUT2_PULSE_TYPE_PARSED_DATA_HEADER = "PW_IN2=";
	public final static String SETTING_INPUT3_PULSE_TYPE_PARSED_DATA_HEADER = "PW_IN3=";
	public final static String SETTING_INPUT4_PULSE_TYPE_PARSED_DATA_HEADER = "PW_IN4=";
	
	public final static String SETTING_ERROR_TEST_WAY_PARSED_DATA_HEADER = "ERR_WAY=";
	
	public final static String SETTING_ERROR_PULSE_N_PARSED_DATA_HEADER = "ERR_PULSE_N=";
	public final static String SETTING_ERROR_PULSE_T_PARSED_DATA_HEADER = "ERR_PULSE_T=";
	
	public final static String SETTING_SERIAL_NO_PARSED_DATA_HEADER = "SN=";
	public final static String SETTING_FIRMWARE_VERSION_PARSED_DATA_HEADER = "EV=";
	
	public final static String SETTING_DLL_VERSION_PARSED_DATA_HEADER = "DV=";
	public final static String SETTING_DATE_AND_TIME_PARSED_DATA_HEADER = "DATETIME=";
	
	
	//*********************data types*************
	
	public final static String SETTING_DATA_WIRING_MODE_3P4W = GuiUtils.StringToHex("00");
	public final static String SETTING_DATA_WIRING_MODE_3P3W = GuiUtils.StringToHex("10");
	
	public final static String SETTING_DATA_OUTPUT_PULSE_TYPE_ACTIVE = GuiUtils.StringToHex("0");
	public final static String SETTING_DATA_OUTPUT_PULSE_TYPE_REACTIVE = GuiUtils.StringToHex("1");
	public final static String SETTING_DATA_OUTPUT_PULSE_TYPE_APPARENT = GuiUtils.StringToHex("2");
	
}
