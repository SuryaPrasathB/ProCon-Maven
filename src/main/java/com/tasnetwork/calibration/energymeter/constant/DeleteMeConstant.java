package com.tasnetwork.calibration.energymeter.constant;

import com.tasnetwork.calibration.energymeter.util.GuiUtils;

public class DeleteMeConstant {

	public static boolean serialDisplayFeatureEnabled = true;
	
	public static final String HV_ER_TERMINATOR = "0D0A";
	//public static final String BRIDGE_ER_TERMINATOR = "0D0A";
	public static final String BRIDGE_ERR_TERMINATOR = "0D";
	public static final String LVD_ER_COM_CHECK_SUCCESS1 = "003F";//"003F";
	public static final String LVD_ER_COM_CHECK_SUCCESS2 = "3F";//"003F";
	
	public static final String HV_CMD_ERROR_RESPONSE_HDR= GuiUtils.StringToHex("VE,");
	
	
	public static  String NO_RESPONSE = "NoResponse";
	public static  String SUCCESS_RESPONSE = "Success";
	public static  String ERROR_RESPONSE = "Error";
	public static  String NO_ACCESS = " NoAccess";
	public static  String PORT_NOT_FOUND = " NotFound";
	
	
}
