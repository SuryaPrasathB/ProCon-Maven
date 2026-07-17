package com.tasnetwork.calibration.energymeter.constant;

import java.text.DecimalFormat;

public class ConstantPowerSourceBofa {
 
	
	
	//public static final String BOFA_VOLTAGE_RESOLUTION = "%03.01f";
	//public static final String BOFA_CURRENT_RESOLUTION = "%02.02f";
	
	public static final String LDU_METER_PULSE_PER_TURN_PULSE  = "0001" ;
	
	public static final DecimalFormat BOFA_VOLTAGE_RESOLUTION = new DecimalFormat("000.0");
	public static final DecimalFormat BOFA_CURRENT_RESOLUTION = new DecimalFormat ("00.00");
	public static final DecimalFormat BOFA_FREQ_RESOLUTION    = new DecimalFormat ("00.00");
	public static final DecimalFormat BOFA_DEGREE_RESOLUTION  = new DecimalFormat ("000.0");
	
	public static final String BASIC_VOLTAGE               = "240.0" ;  // V
	public static final String BASIC_CURRENT               = "05.00" ;	// A
	public static final String BASIC_FREQUENCY             = "50.00" ;	// A
	
	public static final String POSITIVE_PHASE_SEQUENCE     = "30" ;   
	public static final String REVERSE_PHASE_SEQUENCE      = "31" ;	  
	
	public static final String FORWARD_CURRENT_DIRECTION   = "30" ;   
	public static final String REVESRE_CURRENT_DIRECTION   = "31" ;
	
	public static final String SINE_WAVE      = "30" ;
	public static final String SUB_HARMONIC   = "31" ;
	public static final String HARMONIC       = "32" ;
	public static final String ODD_HARMONIC   = "33" ;
 	
	//========== METER TYPE =================================//
	public static final String METER_3PHASE3WIRE_WATT               = "30" ; // 
	public static final String METER_3PHASE3WIRE_60_VAR             = "31" ;
	public static final String METER_3PHASE3WIRE_90_SHIFT_PHASE_VAR = "33" ; 
	public static final String METER_3PHASE3WIRE_NATURAL_REACTIVE   = "34" ; 
	public static final String METER_3PHASE4WIRE_WATT               = "35" ;
	public static final String METER_3PHASE4WIRE_90_CROSS_PHASE_VAR = "36" ; 
	public static final String METER_3PHASE4WIRE_90_SHIFT_PHASE_VAR = "37" ;
	public static final String METER_3PHASE4WIRE_NATURAL_REACTIVE   = "38" ;
	public static final String METER_1PHASE2WIRE_WATT               = "39" ;
	
	//========== CONJUCTION PHASE/SPLIT ===================//
	public static final String PHASE_A              = "31" ; // 
	public static final String PHASE_B              = "32" ;
	public static final String PHASE_AB             = "33" ; 
	public static final String PHASE_C              = "34" ; 
	public static final String PHASE_AC             = "35" ;
	public static final String PHASE_BC             = "36" ;
	public static final String CONJUNCTION_PHASE    = "3F" ;
	
	//=============COMMAND=================================//
	public static final String PARAMETER_SETTING_CMD                   = "02" ; 
	public static final String VOLTAGE_CURRENT_OUTPUT_CMD              = "03" ;
	public static final String VOLTAGE_CURRENT_STOP_OUTPUT_CMD         = "04" ; 
	public static final String ACTUAL_OUTPUT_VALUE_READING_CMD         = "05" ; 
	public static final String FREQUENCY_READING_CMD                   = "0A" ; 
	
	public static final String SWITCH_PULSE_REF_METER_TO_PULSE_REF_CLK = "0F" ;
	public static final String CURRENT_STOPPING_COMMAND                = "0C" ;
	public static final String READ_THE_CONSTANT_OF_LIVE_REF_METER     = "0E" ;
	
	public static final String PULSE_REF_METER     = "30" ;
	public static final String PULSE_REF_CLOCK     = "31" ;
	
	public static final String START_BYTE    = "01" ;
	public static final String ADDRESS_BYTE  = "92" ;
	public static final String END_BYTE      = "17" ;
	
	public static final String POSITIVE      = "06" ;
	public static final String NEGATIVE      = "15" ;
		
	public static final String HEX_2_DIGITS_FORMAT        = "%02X";
	public static final String FLOAT_3_DIGITS_1_DECIMAL   = "%03.1f";
	public static final String FLOAT_2_DIGITS_2_DECIMAL   = "%02.1f";
	public static final String FLOAT_5_DIGITS_2_DECIMALS  = "%05.2f";
	public static final String FLOAT_8_DIGITS_2_DECIMALS  = "%08.2f";
	public static final String HEX_UPPERCASE_FORMAT       = "%X";
	
	public static final int LDU_ADDRESS_ADDITION = 65;//41 ; 
	public static final String LDU_CHECK_BYTE = "03" ;

//============== ERROR CALCULATOR COMMUNICATION ===================================
	
//=============COMMANDS=================================//
 
	//HOST:
	public static final String TEST_TURNS_AND_PULSES_CMD            = "02";  
	public static final String READ_ERRORS_CMD                      = "03"; 
	public static final String PREPARE_SEARCH_MARK_CMD              = "04";  
	public static final String BEGIN_SEARCH_MARK_CMD                = "07";  
	public static final String QUERY_MARK_SEARCH_RESULT_CMD         = "08"; 
	public static final String MARK_SEARCH_END_CMD                  = "09";  
	public static final String QUERY_ENABLE_PULSE_CMD               = "0A";  
	public static final String START_TEST_END_CMD                   = "0C";  
	public static final String STATE_QUERY_CMD                      = "0B"; 
	public static final String ENTER_DIAL_TEST_STATE_CMD            = "0F";  
	public static final String READ_DIAL_PULSES_CMD                 = "0E";  
	public static final String DIAL_TEST_STATE_EXIT_CMD             = "0C";  
	public static final String RELAY_485_CONNECT_OR_DISCONNECT_CMD  = "10";  
	public static final String PULSE_SWITCH_CMD                     = "11";  
	public static final String STOP_REFRESH_ERRORS_CMD              = "13";  
	public static final String READ_REF_METER_PULSE_NUM_CMD         = "14";  
	public static final String PULSE_INPUT_CHANNEL_SELECT_CMD       = "1B";  
	public static final String RELAY_POSITION_CONTROL_CMD           = "1C";  
	public static final String DISPLAY_DAY_ERROR_CMD                = "1F";  
	public static final String TIME_SWITCHING_ERROR_TEST_ENTRY_CMD  = "21";  
	public static final String READ_SWITCHING_TIME_CMD              = "22";  
	public static final String ERROR_TESTER_CHECK_REST_CMD          = "29";  
	public static final String DOUBLE_CIRCUIT_SWITCH_CMD            = "2A";

	//SLAVE:
	public static final String LDU_HEX_BROADCAST_ADDRESS    = "FF" ;
	public static final int LDU_INT_BROADCAST_ADDRESS    = 255 ;
	public static final int ADDRESS_SLAVE_01 = 01;
	public static final int ADDRESS_SLAVE_02 = 02;
	public static final int ADDRESS_SLAVE_03 = 03;
	public static final int ADDRESS_SLAVE_04 = 04;
	public static final int ADDRESS_SLAVE_05 = 05;
	public static final int ADDRESS_SLAVE_06 = 06;
	public static final int ADDRESS_SLAVE_07 = 07;
	public static final int ADDRESS_SLAVE_08 = 8;
	public static final int ADDRESS_SLAVE_09 = 9;
	public static final int ADDRESS_SLAVE_10 = 10;
	public static final int ADDRESS_SLAVE_11 = 11;
	public static final int ADDRESS_SLAVE_12 = 12;
	public static final int ADDRESS_SLAVE_13 = 13;
	public static final int ADDRESS_SLAVE_14 = 14;
	public static final int ADDRESS_SLAVE_15 = 15;
	public static final int ADDRESS_SLAVE_16 = 16;
	public static final int ADDRESS_SLAVE_17 = 17;
	public static final int ADDRESS_SLAVE_18 = 18;
	public static final int ADDRESS_SLAVE_19 = 19;
	public static final int ADDRESS_SLAVE_20 = 20;
	public static final int ADDRESS_SLAVE_21 = 21;
	public static final int ADDRESS_SLAVE_22 = 22;
	public static final int ADDRESS_SLAVE_23 = 23;
	public static final int ADDRESS_SLAVE_24 = 24;
	public static final int ADDRESS_SLAVE_25 = 25;
	public static final int ADDRESS_SLAVE_26 = 26;
	public static final int ADDRESS_SLAVE_27 = 27;
	public static final int ADDRESS_SLAVE_28 = 28;
	public static final int ADDRESS_SLAVE_29 = 29;
	public static final int ADDRESS_SLAVE_30 = 30;
	public static final int ADDRESS_SLAVE_31 = 31;
	public static final int ADDRESS_SLAVE_32 = 32;
	public static final int ADDRESS_SLAVE_33 = 33;
	public static final int ADDRESS_SLAVE_34 = 34;
	public static final int ADDRESS_SLAVE_35 = 35;
	public static final int ADDRESS_SLAVE_36 = 36;
	public static final int ADDRESS_SLAVE_37 = 37;
	public static final int ADDRESS_SLAVE_38 = 38;
	public static final int ADDRESS_SLAVE_39 = 39;
	public static final int ADDRESS_SLAVE_40 = 40;
	
	public static final String SLAVE_01_ADRS = "42";


	public static final String YES_MSG                           = "06";
	public static final String NO_MSG                            = "15";
	
	public static final String NOT_FOUND_MSG                     = "30";
	public static final String FOUND_MSG                         = "31";
	
	public static final String OCCUPIED_MSG                      = "30";
	public static final String IDLE_MSG                          = "31";
	
	public static final String DISCONNECT_MSG                    = "30";
	public static final String CONNECT_MSG                       = "31";
	
	public static final String SWITCHING_PULSE_DETECTED          = "30";
	public static final String NO_SWITCHING_PULSE                = "31";
	
	public static final String SWITCH_TO_CHANNEL_1_MSG           = "30"; // it is active pulse input in this application (red line)
	public static final String SWITCH_TO_CHANNEL_2_MSG           = "31"; // it is active pulse input in this application (green line)
	public static final String SWITCH_TO_CHANNEL_3_MSG           = "34"; // it is multifunction input in this application (red line)
	public static final String SWITCH_TO_CHANNEL_4_MSG           = "35"; // it is multifunction input in this application (green line)
	public static final String SWITCH_TO_CHANNEL_5_MSG           = "32"; // it is reactive pulse input in this application (red line)
	public static final String SWITCH_TO_CHANNEL_6_MSG           = "33"; // it is reactive pulse input in this application (green line)
	public static final String PULSE_INPUT_CHANNEL_1_MSG         = "30";
	public static final String PULSE_INPUT_CHANNEL_2_MSG         = "31";
	public static final String DISCONNECT_THE_VOLTAGE_RELAY_MSG  = "30";
	public static final String CONNECT_THE_VOLTAGE_RELAY_MSG     = "31";
	public static final String NONE_SWITCHING_PULSE_MSG          = "30";
	public static final String SWITCHING_PULSE_DETECTED_MSG      = "31";
	
	public static final String CIRCUIT_1_ID 					 = "31";
	public static final String CIRCUIT_2_ID                      = "32";

	public static final String DUMMY_TWO_DIGITS                  = "00";
	public static final String DUMMY_THREE_DIGITS                = "000";
	public static final String DUMMY_FOUR_DIGITS                 = "0000";
	public static final String DUMMY_FIVE_DIGITS                 = "00000";
	
	public static final String ER_STARTS_WITH = "0192" ;
	public static final String ER_LDU_STARTS_WITH = "01" ;
	//public static final String ER_FIRST_LDU_STARTS_WITH = "0142" ;
	
	public static final String ER_PARAMETER_SETTING_CMD = "" ;
	public static final String ER_VOLTAGE_CURRENT_OUTPUT_CMD = "" ;
	public static final String ER_VOLTAGE_CURRENT_STOP_OUTPUT_CMD = "" ;
	public static final String ER_ACTUAL_OUTPUT_VALUE_READING_CMD = "" ;
	public static final String ER_CURRENT_STOPPING_CMD = "" ;
	public static final String ER_READ_THE_CONSTANT_OF_LIVE_REFERENCE_METER_CMD = "" ;
	public static final String ER_SWITCH_PULSE_REF_METER_TO_PULSE_REF_CLK_CMD = "" ;

	public static final int LDU_INITIAL_ADDRESS_INDEX = 41 ;

	public static final String BOFA_LDU_WFR = "99.99999" ;;//"-99.99999" ;
	
	public static final String BOFA_CURRENT_ZERO_VALUE = "E-4";;//"1.0E-4";
	
	public static final String CMD_STOP_VOLT_CURRENT_IN_HEX = "0192049717" ;;//"-99.99999" ;
	public static final String CMD_STOP_CURRENT_IN_HEX = "01920C9F17";
	public static final String ER_CMD_STOP_VOLT_CURRENT_POSITIVE_IN_HEX = "0192069917" ;;//"-99.99999" ;

//======================================================================================
}
