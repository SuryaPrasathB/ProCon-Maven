package com.tasnetwork.calibration.energymeter.constant;

import com.tasnetwork.calibration.energymeter.util.GuiUtils;

public class ConstantRefStdMte {

	public static final String SERIAL_PORT_REF_STD="SerialCommRefStd";
	public static final Integer RefStdDefaultBaudRate = 57600;
	
	public static final char CMD_TERMINATOR = (char) 0x0d; //carriage return
	
	//public static final char ER_TERMINATOR = (char) 0x0d; //carriage return
	public static final String ER_TERMINATOR = "0D";//(char) 0x0d; //carriage return
	public static final String ER_REF_STD_SEPERATOR = ",";
	
	public static final String CMD_REF_STD_VERSION_HDR = "VER2" + CMD_TERMINATOR;
	
	public static final String ER_REF_STD_VERSION_HDR = GuiUtils.StringToHex("V2.01") + ER_TERMINATOR;
	public static final int ER_REF_STD_VERSION_LENGTH = ER_REF_STD_VERSION_HDR.length();
	
	
	public final static String CMD_REF_STD_READ_CURRENT_HDR = "?1"+ CMD_TERMINATOR;
	public final static String ER_REF_STD_READ_CURRENT_HDR = GuiUtils.StringToHex("E@");
	public final static int    ER_REF_STD_READ_CURRENT_LENGTH = 30;
	
	public final static String CMD_REF_STD_READ_VOLT_PH_N_HDR = "?2"+ CMD_TERMINATOR;
	public final static String ER_REF_STD_READ_VOLT_PH_N_HDR = GuiUtils.StringToHex("EA");
	public final static int    ER_REF_STD_READ_VOLT_PH_N_LENGTH  = 30;
	
	
	public final static String CMD_REF_STD_READ_ACTIVE_POWER_HDR = "?3"+ CMD_TERMINATOR;
	public final static String ER_REF_STD_READ_ACTIVE_POWER_HDR = GuiUtils.StringToHex("EB");
	public final static int    ER_REF_STD_READ_ACTIVE_POWER_LENGTH  = 30;
	
	public final static String CMD_REF_STD_READ_APPARENT_POWER_HDR = "?5"+ CMD_TERMINATOR;
	public final static String ER_REF_STD_READ_APPARENT_POWER_HDR = GuiUtils.StringToHex("ED");
	public final static int    ER_REF_STD_READ_APPARENT_POWER_LENGTH  = 30;
	
	public final static String CMD_REF_STD_READ_PHASE_ANGLE_HDR = "?9"+ CMD_TERMINATOR;
	public final static String ER_REF_STD_READ_PHASE_ANGLE_HDR = GuiUtils.StringToHex("EH");
	public final static int    ER_REF_STD_READ_PHASE_ANGLE_LENGTH  = 32;
	
	public final static String CMD_REF_STD_READ_FREQ_HDR = "?13"+ CMD_TERMINATOR;
	public final static String ER_REF_STD_READ_FREQ_HDR = GuiUtils.StringToHex("EL");
	public final static int    ER_REF_STD_READ_FREQ_LENGTH  = 11;
	
	public final static String CMD_REF_STD_READ_ALL_PARAM = CMD_REF_STD_READ_CURRENT_HDR + CMD_REF_STD_READ_VOLT_PH_N_HDR +
			CMD_REF_STD_READ_ACTIVE_POWER_HDR + CMD_REF_STD_READ_APPARENT_POWER_HDR + CMD_REF_STD_READ_PHASE_ANGLE_HDR +
			CMD_REF_STD_READ_FREQ_HDR;
	
	public final static String ER_REF_STD_READ_ALL_PARAM = GuiUtils.StringToHex("E@");
	
	public final static int    ER_REF_STD_READ_ALL_PARAM_TERMINATOR_COUNT  = 6;
	
}
