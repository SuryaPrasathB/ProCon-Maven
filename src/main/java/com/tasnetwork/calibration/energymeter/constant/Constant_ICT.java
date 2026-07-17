package com.tasnetwork.calibration.energymeter.constant;

import com.tasnetwork.calibration.energymeter.util.GuiUtils;

public class Constant_ICT {

	public static final String SERIAL_PORT_ICT="SerialCommICT";
	public static final Integer ICT_DefaultBaudRate = 19200;
	
	public static final String CMD_ICT_POSITION_BROADCAST_HDR = "255";// broad cast position
	public static final String CMD_ICT_POSITION_01_HDR = "1";//position 1 
	public static final String CMD_ICT_POSITION_02_HDR = "2";//position 2
	public static final String CMD_ICT_POSITION_03_HDR = "3";//position 3
	public static final String CMD_ICT_POSITION_04_HDR = "4";//position 4
	public static final String CMD_ICT_POSITION_05_HDR = "5";//position 5
	public static final String CMD_ICT_POSITION_06_HDR = "6";//position 6
	public static final String CMD_ICT_POSITION_07_HDR = "7";//position 7
	public static final String CMD_ICT_POSITION_08_HDR = "8";//position 8
	public static final String CMD_ICT_POSITION_09_HDR = "9";//position 9
	public static final String CMD_ICT_POSITION_10_HDR = "10";//position 10
	public static final String CMD_ICT_POSITION_11_HDR = "11";//position 11 
	public static final String CMD_ICT_POSITION_12_HDR = "12";//position 12
	public static final String CMD_ICT_POSITION_13_HDR = "13";//position 13
	public static final String CMD_ICT_POSITION_14_HDR = "14";//position 14
	public static final String CMD_ICT_POSITION_15_HDR = "15";//position 15
	public static final String CMD_ICT_POSITION_16_HDR = "16";//position 16 
	public static final String CMD_ICT_POSITION_17_HDR = "17";//position 17
	public static final String CMD_ICT_POSITION_18_HDR = "18";//position 18
	public static final String CMD_ICT_POSITION_19_HDR = "19";//position 19
	public static final String CMD_ICT_POSITION_20_HDR = "20";//position 20
	
	
	public static final int ICT_MAX_POSITION = 10;//position 20
	
	public static final String ICT_STATUS_NORMAL = "0";//
	public static final String ICT_STATUS_BYPASS = "1";
	
	public static final String CMD_ICT_SEPERATOR = GuiUtils.StringToHex(",");
	
	public static final String CMD_TERMINATOR = GuiUtils.StringToHex(";");
	public static final String ER_TERMINATOR = GuiUtils.StringToHex(";");
		
	public static final String CMD_RESET_STATE_HDR = GuiUtils.StringToHex("reset");
	public static final String ER_RESET_STATE = GuiUtils.StringToHex("OK");
	
	public static final String CMD_SHORT_HDR = GuiUtils.StringToHex("short");
	public static final String ER_SHORT = GuiUtils.StringToHex("OK");
	
	public static final String CMD_INQUIRE_STATE_HDR = GuiUtils.StringToHex("state");
	public static final String ER_INQUIRE_STATE = GuiUtils.StringToHex("state");
	
	public static final String CMD_SET_ID_HDR = GuiUtils.StringToHex("setID");
	public static final String ER_SET_ID = GuiUtils.StringToHex("OK");
	
	public static final String CMD_SAVE_HDR = GuiUtils.StringToHex("save");
	public static final String ER_SAVE = GuiUtils.StringToHex("OK");
	
	public static final String CMD_INIT_HDR = GuiUtils.StringToHex("init");
	public static final String ER_INIT = GuiUtils.StringToHex("OK");
	
}
