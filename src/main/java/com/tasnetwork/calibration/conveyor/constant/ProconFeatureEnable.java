package com.tasnetwork.calibration.conveyor.constant;

import org.apache.commons.collections4.functors.FalsePredicate;

public class ProconFeatureEnable {

	public static boolean NEWLAND_QR_SCANNER_CONNECTED = true;
	public static boolean EIC_MEGA_OHM_PANEL_METER = true;
	public static boolean DEV_SYS_EM1_CONNECTED = true;
	public static boolean ELMEASURE_SL1300_MULTI_METER = true;
	public static boolean FT_SOURCE_CONNECTED = true;
	
	public static boolean LSCS_LDU = true;
	public static boolean CALIB_DUT_EXECUTION_PROCESS_IN_PARALLEL = false;
	public static boolean CALIB_PHASE_DUT_EXECUTION_PROCESS_IN_PARALLEL = true;
	public static boolean CALIB_NEUTRAL_DUT_EXECUTION_PROCESS_IN_PARALLEL = true;
	public static boolean FT_EXECUTION_PROCESS_IN_PARALLEL = true;
	public static boolean CALIB_QR_SCANNER_EXECUTION_PROCESS_IN_PARALLEL = true;
	public static boolean FT_QR_SCANNER_EXECUTION_PROCESS_IN_PARALLEL = true;//false
	public static boolean FT_OPTICAL_SERIAL_WRITE_EXECUTION_PROCESS_IN_PARALLEL = true;
	
	public static boolean WAIT_FOR_USER_INPUT = false;/*true;*/
	public static boolean WAIT_FOR_POWER_STABLE_IN_CALIB = false;/*true;*/
	public static boolean VERIFIC_BATCH_FAILED_USER_PROMPT_ENABLED = true;
	public static boolean MOTOR_CONTROL_ENABLE = true;
	
	public static boolean MOTOR_CONTROL_DISABLE = false; //true;
	
	// FALSE - To not take data from Spring Database
	// True - Take data from database
	public static boolean CONVEYOR_DEVICE_SETTING_SPRING_ENABLED = true; //false;
	public static boolean CONVEYOR_DASHBOARD_GUI_ENABLED = true;
}
