package com.tasnetwork.calibration.energymeter.constant;

import java.util.ArrayList;
import java.util.Arrays;

import com.tasnetwork.calibration.energymeter.testprofiles.TestProfileType;

public class ConstantPowerSourceMte {
	/* Config for serial port mapping*/
	public static final String SERIAL_PORT_POWER_SOURCE="SerialCommPowerSrc";
	public static final Integer PowerSrcDefaultBaudRate = 19200;
	/*Commands for Power source*/
	


	public static final String CMD_PWR_SRC_OFF = "OFF";
	public static final String CMD_PWR_SRC_SET = "SET";
	public static final String CMD_PWR_SRC_U1_PREFIX = "U1,";
	public static final String CMD_PWR_SRC_I1_PREFIX = "I1,";
	public static final String CMD_PWR_SRC_PF1_PREFIX = "W1,";
	public static final String CMD_PWR_SRC_U2_PREFIX = "U2,";
	public static final String CMD_PWR_SRC_I2_PREFIX = "I2,";
	public static final String CMD_PWR_SRC_PF2_PREFIX = "W2,";
	public static final String CMD_PWR_SRC_U3_PREFIX = "U3,";
	public static final String CMD_PWR_SRC_I3_PREFIX = "I3,";
	public static final String CMD_PWR_SRC_PF3_PREFIX = "W3,";
	public static final String CMD_PWR_SRC_FRQ_PREFIX = "FRQ";
	public static final String CMD_PWR_SRC_PHASE_RY_PREFIX = "PH2,";
	public static final String CMD_PWR_SRC_PHASE_RB_PREFIX = "PH3,";
	public static final String CMD_PWR_SRC_SEPERATOR = ";";
	public static final String CMD_PWR_SRC_DATA_EXPECTED_RESPONSE = "=O";
	public static final String CMD_PWR_SRC_DATA_ERROR_RESPONSE = "=E";
	public static final String CMD_PWR_SRC_SET_EXPECTED_RESPONSE = "=1";
	public static final String CMD_PWR_SRC_SET_ERROR_RESPONSE = "=0";
	public static final String CMD_PWR_SRC_HAR_I1_PREFIX = "OWI1,";
	public static final String CMD_PWR_SRC_HAR_U1_PREFIX = "OWU1,";
	public static final String CMD_PWR_SRC_HAR_I2_PREFIX = "OWI2,";
	public static final String CMD_PWR_SRC_HAR_U2_PREFIX = "OWU2,";
	public static final String CMD_PWR_SRC_HAR_I3_PREFIX = "OWI3,";
	public static final String CMD_PWR_SRC_HAR_U3_PREFIX = "OWU3,";
	public static final String CMD_PWR_SRC_COMMA = ",";
	public static final String CMD_PWR_SRC_RESET = "R";
	
	public static final String VOLTAGE_RESOLUTION = "%03.02f";
	public static final String CURRENT_RESOLUTION = "%02.01f";//"%02.03f";
	public static final String PHASE_RESOLUTION = "%02.02f";
	//public static final String ENERGY_RESOLUTION = "%.2f";
	
	public static final int POWER_SRC_3P4W_SINE_IMPORT_REACTIVE_ZPF_ANGLE = 90;
	public static final int POWER_SRC_3P4W_SINE_EXPORT_REACTIVE_ZPF_ANGLE = -90;
	public static int POWER_SRC_SINE_REACTIVE_ZPF_ANGLE = POWER_SRC_3P4W_SINE_IMPORT_REACTIVE_ZPF_ANGLE;//90;
	
	public static final int POWER_SRC_3P4W_COS_IMPORT_ACTIVE_UPF_ANGLE = 0;
	public static final int POWER_SRC_3P4W_COS_EXPORT_ACTIVE_UPF_ANGLE = -180;
	public static int POWER_SRC_COS_ACTIVE_UPF_ANGLE = POWER_SRC_3P4W_COS_IMPORT_ACTIVE_UPF_ANGLE;
	
	public static final String EXPORT_MODE = "EXPORT";
	public static final String IMPORT_MODE = "IMPORT";
	
	public static final ArrayList<String> POWER_RESET_TEST_TYPES = new ArrayList<>(Arrays.asList(
			TestProfileType.InfluenceFreq.toString(),
/*			TestProfileType.VoltageUnbalance.toString(),*/
			TestProfileType.PhaseReversal.toString(),
			TestProfileType.CustomTest.toString(),
			TestProfileType.InfluenceHarmonic.toString()));
	

}
