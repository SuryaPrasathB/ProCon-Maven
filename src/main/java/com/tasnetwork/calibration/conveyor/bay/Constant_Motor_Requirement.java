package com.tasnetwork.calibration.conveyor.bay;

import java.util.Arrays;
import java.util.List;

public class Constant_Motor_Requirement {
	
	// 1 - FT, REJECTION
	// 2 - FT, HV, IR, CALIB, WAITING
	// 3 - WAITING, VERIFIC
	// 4 - LINK-VERIFIC, STA01, STA02
	// 5 - STA01
	// 6 - STA02
	// 7 - LINK-STA01, STA02
	// 8 - COMM
	// 9 - UNLOADING, LOADING 
	
	
	public static final String REQUIRED = "R";
	public static final String NOT_REQU = "NR";
	
	// 														 						   1		 2			3		 4			5		 6			7		  8  		9
	public static final List<String> FT_MOTOR_1_2_REQUIRED 			= Arrays.asList(REQUIRED, REQUIRED, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU );
	public static final List<String> FT_MOTOR_1_REQUIRED 			= Arrays.asList(REQUIRED, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU );
	public static final List<String> HV_MOTOR_REQUIRED 				= Arrays.asList(NOT_REQU, REQUIRED, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU );
	public static final List<String> IR_MOTOR_REQUIRED 				= Arrays.asList(NOT_REQU, REQUIRED, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU );
	public static final List<String> CALIB_MOTOR_REQUIRED 			= Arrays.asList(NOT_REQU, REQUIRED, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU );
	public static final List<String> WAITING_MOTOR_REQUIRED 		= Arrays.asList(NOT_REQU, REQUIRED, REQUIRED, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU );
	//public static final List<String> VERIFIC_MOTOR_STA1_REQUIRED 	= Arrays.asList(NOT_REQU, NOT_REQU, REQUIRED, REQUIRED, REQUIRED, REQUIRED, REQUIRED, REQUIRED, NOT_REQU );
	//public static final List<String> VERIFIC_MOTOR_STA1_REQUIRED 	= Arrays.asList(NOT_REQU, NOT_REQU, REQUIRED, REQUIRED, REQUIRED, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU ); // 
	/**************************************************************before modifing VERIFIC_MOTOR_STA1_REQUIRED call Gopi***********************/
	/**************************************************************before modifing VERIFIC_MOTOR_STA1_REQUIRED call Gopi***********************/
	public static final List<String> VERIFIC_MOTOR_STA1_REQUIRED 	= Arrays.asList(NOT_REQU, NOT_REQU, REQUIRED, REQUIRED, REQUIRED, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU );
	/**************************************************************before modifing VERIFIC_MOTOR_STA1_REQUIRED call Gopi***********************/
	/**************************************************************before modifing VERIFIC_MOTOR_STA1_REQUIRED call Gopi***********************/
	public static final List<String> VERIFIC_MOTOR_STA2_REQUIRED 	= Arrays.asList(NOT_REQU, NOT_REQU, REQUIRED, REQUIRED, NOT_REQU, REQUIRED, NOT_REQU, REQUIRED, NOT_REQU );
	public static final List<String> STA1_MOTOR_REQUIRED 			= Arrays.asList(NOT_REQU, NOT_REQU, NOT_REQU, REQUIRED, REQUIRED, REQUIRED, REQUIRED, REQUIRED, NOT_REQU );
	public static final List<String> STA2_MOTOR_REQUIRED 			= Arrays.asList(NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, REQUIRED, NOT_REQU, REQUIRED, NOT_REQU );
	public static final List<String> COMM_MOTOR_REQUIRED 			= Arrays.asList(NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, REQUIRED, NOT_REQU );
	public static final List<String> UNLOADING_MOTOR_REQUIRED 		= Arrays.asList(NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, REQUIRED );
	public static final List<String> LOADING_MOTOR_REQUIRED 		= Arrays.asList(NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, REQUIRED );
	public static final List<String> REJECTION_MOTOR_REQUIRED		= Arrays.asList(REQUIRED, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU, NOT_REQU );

	
}
