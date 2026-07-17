package com.tasnetwork.calibration.energymeter.constant;

public class ConstantLscsHarmonicsSourceSlave {
	
	public static final int TOTAL_NO_OF_ORDER_HARMONICS_SUPPORTED = 21; //32 ;
	public static final int NUM_OF_ORDERS_ALLOWED_IN_ONE_TEST_CASE = 3 ;
	public static final int TOTAL_NO_OF_SIGNALS = 6 ;
	
	public static final int BOFA_MAXIMUM_ALLOWED_HARM_VOLTAGE_CONTENT = 40 ;
	public static final int BOFA_MAXIMUM_ALLOWED_HARM_CURRENT_CONTENT = 40 ;

	public static final String SERIAL_PORT_HARMONICS_SLAVE= "SerialCommHarmonicsSlave";
	public static final Integer harmonicsSourceDefaultBaudRate = 9600;
	
	public static final String HEADER_VOLTAGE = "V";
	public static final String HEADER_CURRENT = "I";
	public static final String CMD_SEPERATOR  = ","; 
	
	public static final String HEADER_FREQUENCY  = "FQ";
	
	public static final String CMD_PWR_HRM_SRC_SEPERATOR = ",";
	public static final String CMD_PWR_HRM_SRC_SLAVE_DISABLE_ALL_HARMONICS_HDR = "HE,0,0,0,0,0,0,0";
	public static final String CMD_PWR_HRM_SRC_SLAVE_ENABLE_HARMONICS_HDR = 	 "HE,1,";//1,0,0,0,0,0";        //byte no. to alter -> 6,8,10,12,14
	public static final String CMD_PWR_HRM_SRC_SLAVE_TRANSMISION_END_HDR  = 	 "TE";
	
	public static final String ER_PWR_HRM_SRC_SLAVE_DISABLE_ALL_HARMONICS  = "d";//hdAcK
	public static final String ER_PWR_HRM_SRC_SLAVE_ENABLE_ALL_HARMONICS   = "e";//heAck
	public static final String ER_PWR_HRM_SRC_SLAVE_SLAVE_TRANSMISION_END  = "TE"; 
	public static final String ACKNOWLEDGMENT_KEYWORD  = "ACK"; 
	
	public static final String CMD_PWR_SRC_HRM_ACK_ERROR_RESPONSE = "INVALIDINPUT";

	
	public static final String ER_PWR_HRM_SRC_SLAVE_FOR_V1_DATA  = "1";     //V1ACK
	public static final String ER_PWR_HRM_SRC_SLAVE_FOR_V2_DATA  = "2";
	public static final String ER_PWR_HRM_SRC_SLAVE_FOR_V3_DATA  = "3";
	
	public static final String ER_PWR_HRM_SRC_SLAVE_FOR_I1_DATA  = "1";     
	public static final String ER_PWR_HRM_SRC_SLAVE_FOR_I2_DATA  = "2";
	public static final String ER_PWR_HRM_SRC_SLAVE_FOR_I3_DATA  = "3";
	
	
	public static final String R_PHASE = "R";           
	public static final String Y_PHASE = "Y";
	public static final String B_PHASE = "B";
	
	public static final String HEADER_HARMONICS = "h";  
	
	public static final String HARM_IN_V_AND_I = "H";       
	public static final String HARM_IN_ONLY_V  = "V"; 
	public static final String HARM_IN_ONLY_I  = "I";  
	
	public static final String HYPHEN  = "-"; 
	
	public static final String ZERO  = "0"; 
	
}
