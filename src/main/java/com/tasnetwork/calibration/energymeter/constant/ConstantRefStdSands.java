package com.tasnetwork.calibration.energymeter.constant;

import com.tasnetwork.calibration.energymeter.util.GuiUtils;

public class ConstantRefStdSands {
	
	public final static String DATA_CONFIG_MODE_M1_VOLT_LT_240V = "01";	
	public final static String DATA_CONFIG_MODE_M1_VOLT_HT_63DOT5V = "02";
	public final static String DATA_CONFIG_MODE_M2_CURRENT_MAX_100A = "01";	
	public final static String DATA_CONFIG_MODE_M2_CURRENT_MAX_10A =  "02";
	public final static String DATA_CONFIG_MODE_M2_CURRENT_MAX_1A =   "03";
	public final static String DATA_CONFIG_MODE_M3_PULSE_OUTPUT_ACTIVE_ENERGY = "01";
	public final static String DATA_CONFIG_MODE_M3_PULSE_OUTPUT_REACTIVE_ENERGY = "02";
	public final static String DATA_CONFIG_MODE_M3_PULSE_OUTPUT_APPARAENT_ENERGY = "03";
	
	public final static float MODE_M1_VOLT_LEVEl1_MAX = 82.55f;	//63.5V +/- 30%
	public final static float MODE_M1_VOLT_LEVEl2_MAX = 312.0f; // 230 +/- 30%
	
	public final static float MODE_M2_CURRENT_LEVEl1_MAX = 1.0f;	//1A
	public final static float MODE_M2_CURRENT_LEVEl2_MAX = 10.0f;   // 10A	
	public final static float MODE_M2_CURRENT_LEVEl3_MAX = 120.0f;   // 120A
	
	public final static String CMD_ACCU_RESET_HDR = GuiUtils.StringToHex("$C");	
	public final static String ER_ACCU_RESET_ACK = "06";
	
	public final static String CMD_ACCU_START_HDR = GuiUtils.StringToHex("$D");	
	public final static String ER_ACCU_START_ACK = "06";
	
	public final static String CMD_ACCU_STOP_HDR = GuiUtils.StringToHex("$P");
	public final static String ER_ACCU_STOP_ACK = "06";

	public final static String CMD_GET_CONFIG_HDR = GuiUtils.StringToHex("$G");	
	public final static String ER_GET_CONFIG_HDR_ACK = "05";
	public final static String CMD_GET_CONFIG_SEC = "06";	
	public final static String ER_GET_CONFIG_SEC_ACK = GuiUtils.StringToHex("G");
	public final static int ER_GET_CONFIG_SEC_LENGTH = 10;
	public static final int GET_CONFIG_MODE_M1_INDEX_POSITION = 2;
	public static final int GET_CONFIG_MODE_M2_INDEX_POSITION = 4;
	public static final int GET_CONFIG_MODE_M3_INDEX_POSITION = 6;
	
	public final static String CMD_SET_CONFIG_DATA_HDR = GuiUtils.StringToHex("S");
	public final static String CMD_SET_CONFIG_HDR = GuiUtils.StringToHex("$S");	
	public final static String ER_SET_CONFIG_HDR_ACK = "06";
	public final static String CMD_SET_CONFIG_SEC = "05";	
	public final static String ER_SET_CONFIG_SEC_ACK = "06";
	public final static int    ER_SET_CONFIG_SEC_LENGTH = 2;
	public final static String ER_SET_CONFIG_TERT_ACK = "06"; // Tertiary acknowledgement
	public final static int    ER_SET_CONFIG_TERT_LENGTH = 2;
	
	
	public final static String CMD_READ_R_PHASE_HDR = GuiUtils.StringToHex("$R");	
	public final static String ER_READ_R_PHASE_HDR_ACK = "1E"; //byte count 30 in hex	
	public final static String CMD_READ_R_PHASE_SEC = "06";	
	public final static String ER_READ_R_PHASE_SEC_ACK = GuiUtils.StringToHex("R");
	public final static int    ER_READ_R_PHASE_SEC_LENGTH = 60;
	
	public final static String CMD_READ_Y_PHASE_HDR = GuiUtils.StringToHex("$Y");	
	public final static String ER_READ_Y_PHASE_HDR_ACK = "1E"; //byte count 30 in hex	
	public final static String CMD_READ_Y_PHASE_SEC = "06";	
	public final static String ER_READ_Y_PHASE_SEC_ACK = GuiUtils.StringToHex("Y");
	public final static int    ER_READ_Y_PHASE_SEC_LENGTH = 60;
	
	public final static String CMD_READ_B_PHASE_HDR = GuiUtils.StringToHex("$B");	
	public final static String ER_READ_B_PHASE_HDR_ACK = "1E"; //byte count 30 in hex	
	public final static String CMD_READ_B_PHASE_SEC = "06";	
	public final static String ER_READ_B_PHASE_SEC_ACK = GuiUtils.StringToHex("B");
	public final static int    ER_READ_B_PHASE_SEC_LENGTH = 60;
	
	public final static String CMD_TOTAL_POWER_INSTANT_ANGLE_HDR = GuiUtils.StringToHex("$T");	
	public final static String ER_TOTAL_POWER_INSTANT_ANGLE_HDR_ACK = "2A"; //byte count 30 in hex	
	public final static String CMD_TOTAL_POWER_INSTANT_ANGLE_SEC = "06";	
	public final static String ER_TOTAL_POWER_INSTANT_ANGLE_SEC_ACK = GuiUtils.StringToHex("T");
	public final static int    ER_TOTAL_POWER_INSTANT_ANGLE_SEC_LENGTH = 84;
	
	public final static String CMD_READ_ACCU_ENERGY_HDR = GuiUtils.StringToHex("$Q");	
	public final static String ER_READ_ACCU_ENERGY_HDR_ACK = "36"; //byte count 54 in hex	
	public final static String CMD_READ_ACCU_ENERGY_SEC = "06";	
	public final static String ER_READ_ACCU_ENERGY_SEC_ACK = GuiUtils.StringToHex("Q");
	public final static int    ER_READ_ACCU_ENERGY_SEC_LENGTH = 108;
	
	
	public final static String ER_ERROR_DATA = GuiUtils.StringToHex("$E");
	
	public static final int REF_STD_INSTANT_METRICS_VOLTAGE_INDEX_POSITION = 2;
	public static final int REF_STD_INSTANT_METRICS_CURRENT_INDEX_POSITION = 10;
	public static final int REF_STD_INSTANT_METRICS_PF_INDEX_POSITION =  18 ;
	public static final int REF_STD_INSTANT_METRICS_WATT_INDEX_POSITION = 26;
	public static final int REF_STD_INSTANT_METRICS_VAR_INDEX_POSITION  = 34;	
	public static final int REF_STD_INSTANT_METRICS_VA_INDEX_POSITION = 42;	
	public static final int REF_STD_INSTANT_METRICS_FREQ_INDEX_POSITION = 50 ;
	
	
	
	public static final int REF_STD_TOTAL_ACTIVE_POWER_INDEX_POSITION = 2;
	public static final int REF_STD_TOTAL_REACTIVE_POWER_INDEX_POSITION = 10;
	public static final int REF_STD_TOTAL_APPARENT_POWER_INDEX_POSITION =  18 ;
	public static final int REF_STD_TOTAL_PF_INDEX_POSITION =  26 ;
	public static final int REF_STD_VI_R_PHASE_ANGLE_INDEX_POSITION = 34;
	public static final int REF_STD_VI_Y_PHASE_ANGLE_INDEX_POSITION = 42;	
	public static final int REF_STD_VI_B_PHASE_ANGLE_INDEX_POSITION = 50;	
	public static final int REF_STD_VOLT_RY_PHASE_ANGLE_INDEX_POSITION = 58;
	public static final int REF_STD_VOLT_YB_PHASE_ANGLE_INDEX_POSITION = 66;	
	public static final int REF_STD_VOLT_BR_PHASE_ANGLE_INDEX_POSITION = 74;	

	
	
	//public static final int REF_STD_INSTANT_METRICS_DEGREE_INDEX_POSITION = 96;
	
	public static final String REF_STD_ACCUMULATIVE_ER = "51";
	public static final int REF_STD_ACCU_EPOCH_INDEX_POSITION = 2;
	public static final int REF_STD_ACCU_TOTAL_ACTIVE_ENERGY_INDEX_POSITION = 10;
	public static final int REF_STD_ACCU_TOTAL_REACTIVE_ENERGY_INDEX_POSITION = 18;
	public static final int REF_STD_ACCU_TOTAL_APPARENT_ENERGY_INDEX_POSITION = 26;
	public static final int REF_STD_ACCU_R_PHASE_ACTIVE_ENERGY_INDEX_POSITION = 34;
	public static final int REF_STD_ACCU_R_PHASE_REACTIVE_ENERGY_INDEX_POSITION = 42;
	public static final int REF_STD_ACCU_R_PHASE_APPARENT_ENERGY_INDEX_POSITION = 50;
	
	public static final int REF_STD_ACCU_Y_PHASE_ACTIVE_ENERGY_INDEX_POSITION = 58;
	public static final int REF_STD_ACCU_Y_PHASE_REACTIVE_ENERGY_INDEX_POSITION = 66;
	public static final int REF_STD_ACCU_Y_PHASE_APPARENT_ENERGY_INDEX_POSITION = 74;
	public static final int REF_STD_ACCU_B_PHASE_ACTIVE_ENERGY_INDEX_POSITION = 82;
	public static final int REF_STD_ACCU_B_PHASE_REACTIVE_ENERGY_INDEX_POSITION = 90;
	public static final int REF_STD_ACCU_B_PHASE_APPARENT_ENERGY_INDEX_POSITION = 98;
}
