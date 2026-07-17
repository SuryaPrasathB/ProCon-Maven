package com.tasnetwork.calibration.energymeter.constant;

import java.util.ArrayList;
import java.util.Arrays;

public class ConstantRefStdRadiant {

	/* Config for serial port mapping*/

	public static final String SERIAL_PORT_REF_STD="SerialCommRefStd";
	public static final Integer RefStdDefaultBaudRate = 57600;
	
	//public static final double REF_STD_MAX_OUTPUT_FREQ_IN_MEGA_HERTZ = 1.905;/// for radiant maximum is 2.1 MHz
	//public static final double REF_STD_MAX_OUTPUT_FREQ_IN_MEGA_HERTZ = 0.1905;// more error observed while testing in reactive
	//public static final double REF_STD_MAX_OUTPUT_FREQ_IN_MEGA_HERTZ = 0.5;//not working while testing in reactive 
	//public static final double REF_STD_MAX_OUTPUT_FREQ_IN_MEGA_HERTZ = 0.25;// more error observed while testing in reactive
	//public static final double REF_STD_MAX_OUTPUT_FREQ_IN_MEGA_HERTZ = 0.001905;// results at 20190619_045555___0.001905Mhz 
	//public static final double REF_STD_MAX_OUTPUT_FREQ_IN_MEGA_HERTZ = 0.0001905;// results at 20190619_054641___0.0001905Mhz
	//public static final double REF_STD_MAX_OUTPUT_FREQ_IN_MEGA_HERTZ = 0.01905;//worked good for  Active HTCT //failed at few test point , result at 20190618_204900___0.01905Mhz 
	//public static final double REF_STD_MAX_OUTPUT_FREQ_IN_MEGA_HERTZ = 0.010; //for reactive failed results at 20190620_115108___0.010Mhz-Reactive
	//public static final double REF_STD_MAX_OUTPUT_FREQ_IN_MEGA_HERTZ = 0.001905;// for reactive failed results at 20190620_121848___0.001905Mhz-Reactive
	//public static final double REF_STD_MAX_OUTPUT_FREQ_IN_MEGA_HERTZ = 0.0001905;//  output freq= 191 Hz
	//public static final double REF_STD_MAX_OUTPUT_FREQ_IN_MEGA_HERTZ = 0.000900;//  output freq= 902 Hz
	public static final double REF_STD_MAX_OUTPUT_FREQ_IN_MEGA_HERTZ = 1.905; // tested for NSoft
	
	/*Commands for Ref Standard*/
	public final static String REF_STD_R_PHASE_RESPONSE_HEADER = "B60D00540E1D2EA4";
	public final static String REF_STD_Y_PHASE_RESPONSE_HEADER = "C60D00540E1D2EA4";
	public final static String REF_STD_B_PHASE_RESPONSE_HEADER = "D60D00540E1D2EA4";
	public final static String REF_STD_PHASE_WITHOUT_RESPONSE_HEADER = "0D00540E1D2EA4";
	public final static String UNKNOWN_PHASE_FILLER = "0E";
	public final static String REF_STD_PHASE_WITHOUT_RESPONSE_HEADER2 = "0E1D2EA4";
	public final static String UNKNOWN_PHASE_FILLER1 = "0E0E0E0E";
	public final static String CMD_REF_STD_READ_R_PHASE = ("B60D0008005400000000FFFF031D");
	public static final String CMD_REF_STD_READ_Y_PHASE = ("C60D0008005400000000FFFF032D");
	public static final String CMD_REF_STD_READ_B_PHASE = ("D60D0008005400000000FFFF033D");
	public static final String CMD_REF_STD_NOP_CMD = ("A600000000A6");
	public static final Integer CMD_REF_STD_READ_PHASE_DATA_EXPECTED_LENGTH = 180;
	public static final Integer CMD_REF_STD_NOP_EXPECTED_LENGTH = 4*2;
	public static final String  CMD_REF_STD_NOP_ER = "A30000A3";
	
	/* commands for Ref Std Voltage Relay */
	
	public static final String CMD_REF_STD_VOLT_RELAY120 = ("A60B000307000100BC");
	public static final Integer CMD_REF_STD_VOLT_RELAY_EXPECTED_LENGTH = 15*2;
	public static final String  CMD_REF_STD_VOLT_RELAY120_ER = "A60B00090709090709090709090105";
	
	public static final String CMD_REF_STD_VOLT_RELAY240 = ("A60B000307000200BD");
	public static final String  CMD_REF_STD_VOLT_RELAY240_ER = "A60B000907090A07090A07090A0108";
	
	public static final String CMD_REF_STD_VOLT_RELAY480 = ("A60B000307000400BF");
	public static final String  CMD_REF_STD_VOLT_RELAY480_ER = "A60B000907090C07090C07090C010E";
	
	public static final String CMD_REF_STD_UNLOCK_VOLT_RELAY = ("A60B000304000000B8");
	public static final String  CMD_REF_STD_UNLOCK_VOLT_RELAY_ER = "A60B00092441092441092441090204";
	/* commands for Ref Std Current Relay */
	public static final String CMD_REF_STD_CURRENT_RELAY_0_0328 = ("A60B00037008080134");
	public static final Integer CMD_REF_STD_CURRENT_RELAY_EXPECTED_LENGTH = 15*2;
	public static final String  CMD_REF_STD_CURRENT_RELAY_0_0328_120V_ER = "";
	public static final String  CMD_REF_STD_CURRENT_RELAY_0_0328_240V_ER = "";
	public static final String  CMD_REF_STD_CURRENT_RELAY_0_0328_480V_ER = "";
	
	public static final String CMD_REF_STD_CURRENT_RELAY_0_0656 = ("A60B0003704008016C");
	public static final String  CMD_REF_STD_CURRENT_RELAY_0_0656_120V_ER = "";
	public static final String  CMD_REF_STD_CURRENT_RELAY_0_0656_240V_ER = "";
	public static final String  CMD_REF_STD_CURRENT_RELAY_0_0656_480V_ER = "";
	
	public static final String CMD_REF_STD_CURRENT_RELAY_0_1312 = ("A60B0003701008013C");
	public static final String  CMD_REF_STD_CURRENT_RELAY_0_1312_120V_ER = "";
	public static final String  CMD_REF_STD_CURRENT_RELAY_0_1312_240V_ER = "";
	public static final String  CMD_REF_STD_CURRENT_RELAY_0_1312_480V_ER = "";
	
	public static final String CMD_REF_STD_CURRENT_RELAY_0_2624 = ("A60B000370800801AC");
	public static final String  CMD_REF_STD_CURRENT_RELAY_0_2624_120V_ER = "";
	public static final String  CMD_REF_STD_CURRENT_RELAY_0_2624_240V_ER = "";
	public static final String  CMD_REF_STD_CURRENT_RELAY_0_2624_480V_ER = "";
	
	public static final String CMD_REF_STD_CURRENT_RELAY_0_5248 = ("A60B0003702008014C");
	public static final String  CMD_REF_STD_CURRENT_RELAY_0_5248_120V_ER = "";
	public static final String  CMD_REF_STD_CURRENT_RELAY_0_5248_240V_ER = "";
	public static final String  CMD_REF_STD_CURRENT_RELAY_0_5248_480V_ER = "";
	
	public static final String CMD_REF_STD_CURRENT_RELAY_1_0496 = ("A60B000370801001B4");
	public static final String  CMD_REF_STD_CURRENT_RELAY_1_0496_120V_ER = "";
	public static final String  CMD_REF_STD_CURRENT_RELAY_1_0496_240V_ER = "";
	public static final String  CMD_REF_STD_CURRENT_RELAY_1_0496_480V_ER = "";
	
	public static final String CMD_REF_STD_CURRENT_RELAY_2_0992 = ("A60B00037020100154");
	public static final String  CMD_REF_STD_CURRENT_RELAY_2_0992_120V_ER = "";
	public static final String  CMD_REF_STD_CURRENT_RELAY_2_0992_240V_ER = "";
	public static final String  CMD_REF_STD_CURRENT_RELAY_2_0992_480V_ER = "";
	
	public static final String CMD_REF_STD_CURRENT_RELAY_4_1984 = ("A60B000370802001C4");
	public static final String  CMD_REF_STD_CURRENT_RELAY_4_1984_120V_ER = "";
	public static final String  CMD_REF_STD_CURRENT_RELAY_4_1984_240V_ER = "";
	public static final String  CMD_REF_STD_CURRENT_RELAY_4_1984_480V_ER = "";

	public static final String CMD_REF_STD_CURRENT_RELAY_8_3968 = ("A60B00037020200164");
	public static final String  CMD_REF_STD_CURRENT_RELAY_8_3968_120V_ER = "";
	public static final String  CMD_REF_STD_CURRENT_RELAY_8_3968_240V_ER = "";
	public static final String  CMD_REF_STD_CURRENT_RELAY_8_3968_480V_ER = "";
	
	public static final String CMD_REF_STD_CURRENT_RELAY_16_7936 = ("A60B000370804001E4");
	public static final String  CMD_REF_STD_CURRENT_RELAY_16_7936_120V_ER = "";
	public static final String  CMD_REF_STD_CURRENT_RELAY_16_7936_240V_ER = "";
	public static final String  CMD_REF_STD_CURRENT_RELAY_16_7936_480V_ER = "";
	
	public static final String CMD_REF_STD_CURRENT_RELAY_33_5872 = ("A60B00037020400184");
	public static final String  CMD_REF_STD_CURRENT_RELAY_33_5872_120V_ER = "";
	public static final String  CMD_REF_STD_CURRENT_RELAY_33_5872_240V_ER = "";
	public static final String  CMD_REF_STD_CURRENT_RELAY_33_5872_480V_ER = "";
	
	public static final String CMD_REF_STD_CURRENT_RELAY_67_1744 = ("A60B00037080800224");
	public static final String  CMD_REF_STD_CURRENT_RELAY_67_1744_120V_ER = "";
	public static final String  CMD_REF_STD_CURRENT_RELAY_67_1744_240V_ER = "";
	public static final String  CMD_REF_STD_CURRENT_RELAY_67_1744_480V_ER = "";
	
	public static final String CMD_REF_STD_CURRENT_RELAY_200 = ("A60B000370208001C4");
	public static final String  CMD_REF_STD_CURRENT_RELAY_200_120V_ER = "";
	public static final String  CMD_REF_STD_CURRENT_RELAY_200_240V_ER = "";
	public static final String  CMD_REF_STD_CURRENT_RELAY_200_480V_ER = "";
	
	public static final String CMD_REF_STD_UNLOCK_CURRENT_RELAY = ("A60B000340000000F4");
	public static final String  CMD_REF_STD_UNLOCK_CURRENT_RELAY_ER = "";
	
	
	public static final String CMD_REF_STD_ACC_RESET_SETTING = ("A60700011000BE");
	public static final String CMD_REF_STD_ACC_RESET_SETTING_ER = "A30700AA";
	public static final String CMD_REF_STD_ACC_START = ("A608000301000000B2");
	public static final String CMD_REF_STD_ACC_START_ER = "A30800AB";
	
	
	public static final String CMD_REF_STD_READ_WATT_HOUR_READING = ("A62F0002000000D7");
	public static final String CMD_REF_STD_READ_VARH_READING = ("A62F0002000100D8");
	public static final String CMD_REF_STD_READ_METER_READING_ER = "A62F0014";
	public static final int CMD_REF_STD_READ_METER_READING_ER_LENGTH = 26*2; 
	
	public static final String CMD_REF_STD_ACC_STOP = ("A609000000AF"); 
	
	public static final String CMD_REF_STD_BNC_OUTPUT_PORT1_ACTIVE =   "A61D000302000300CB";
	public static final String CMD_REF_STD_BNC_OUTPUT_PORT1_REACTIVE = "A61D000302010300CC";
	public static final String CMD_REF_STD_BNC_OUTPUT_PORT2_ACTIVE =   "A61D000305000300CE";
	public static final String CMD_REF_STD_BNC_OUTPUT_PORT2_REACTIVE = "A61D000305010300CF";
	public static final String CMD_REF_STD_BNC_OUTPUT_PORT3_ACTIVE =   "A61D000306000300CF";
	public static final String CMD_REF_STD_BNC_OUTPUT_PORT3_REACTIVE = "A61D000306010300D0";
	public static final String CMD_REF_STD_BNC_OUTPUT_ER = "AC1D000201CC0198";
	public static final String CMD_REF_STD_ACC_STOP_ER = "A30900AC";
	
	public static final String CMD_REF_STD_BNC_OUTPUT_2XFAMILY_ER = "AC1D0002000000CB";
	
/*	public static final String CMD_REF_STD_BNC_OUTPUT_2XFAMILY_PORT1_ACTIVE =   "A61D0002020000C7"; // YET TO BE TESTED
	public static final String CMD_REF_STD_BNC_OUTPUT_2XFAMILY_PORT1_REACTIVE = "A61D0002020100C8"; // YET TO BE TESTED
	public static final String CMD_REF_STD_BNC_OUTPUT_2XFAMILY_PORT2_ACTIVE =   "A61D0002050000CA"; // YET TO BE TESTED
	public static final String CMD_REF_STD_BNC_OUTPUT_2XFAMILY_PORT2_REACTIVE = "A61D0002050100CB"; // YET TO BE TESTED
	public static final String CMD_REF_STD_BNC_OUTPUT_2XFAMILY_PORT3_ACTIVE =   "A61D0002060000CB"; // YET TO BE TESTED
	public static final String CMD_REF_STD_BNC_OUTPUT_2XFAMILY_PORT3_REACTIVE = "A61D0002060100CC"; // YET TO BE TESTED

	
	public static final String CMD_REF_STD_READ_2XFAMILY_WATT_HOUR_READING = ("A61600062000000004"); // YET TO BE TESTED
	public static final String CMD_REF_STD_READ_2XFAMILY_VARH_READING =      ("A61600062000000004"); // YET TO BE TESTED
	public static final String CMD_REF_STD_READ_2XFAMILY_METER_READING_ER = "A6160020";// YET TO BE TESTED
	public static final int    CMD_REF_STD_READ_2XFAMILY_METER_READING_ER_LENGTH = 38*2;  // YET TO BE TESTED
	public static final String REF_STD_2XFAMILY_ACCUMULATIVE_ER = "A6160020"; // YET TO BE TESTED
*/	
	public static final ArrayList<String> REF_STD_TAP_NAMES = new ArrayList<>(Arrays.asList("Upto32mA", "Upto65mA",
			"Upto131mA","Upto262mA","Upto524mA","Upto1A","Upto2A","Upto4A","Upto8A",
			"Upto16A","Upto33A","Upto67A","Upto200A"));
	
	public static boolean REF_STD_RELAY_SETTING = true;
	
	public static final int REF_STD_FEEDBACK_CONTINUOUS_FAILURE_COUNTER = 5;
	
	public static final int REF_STD_INSTANT_METRICS_VOLTAGE_POSITION = 48;
	public static final int REF_STD_INSTANT_METRICS_CURRENT_POSITION = 56;
	public static final int REF_STD_INSTANT_METRICS_WATT_POSITION = 64;
	public static final int REF_STD_INSTANT_METRICS_VA_POSITION = 72;
	public static final int REF_STD_INSTANT_METRICS_VAR_POSITION = 80;
	public static final int REF_STD_INSTANT_METRICS_FREQ_POSITION = 88;
	public static final int REF_STD_INSTANT_METRICS_DEGREE_POSITION = 96;
	public static final int REF_STD_INSTANT_METRICS_PF_POSITION = 104;
	public static final String REF_STD_ACCUMULATIVE_ER = "A62F0014";
	
	public static final int REF_STD_ACCUMULATIVE_R_PHASE_POSITION = 0;
	public static final int REF_STD_ACCUMULATIVE_Y_PHASE_POSITION = 8;
	public static final int REF_STD_ACCUMULATIVE_B_PHASE_POSITION = 16;
	
	//Command to set the BNC Constant in RSS device
	public final static String CMD_REF_STD_BNC_CONSTANT_WH_SET =   "A6320007000000";
	public final static String CMD_REF_STD_BNC_CONSTANT_VARH_SET = "A6320007000100";
	public static final String CMD_REF_STD_BNC_CONSTANT_ER = "A33200D5";
	
	//public static final float RSS_CURRENT_THRESHOLD_FOR_BNC_CONFIGURE_CONSTANT = 2.0f;

}
