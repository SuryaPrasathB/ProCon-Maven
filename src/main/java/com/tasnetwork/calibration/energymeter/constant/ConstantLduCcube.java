package com.tasnetwork.calibration.energymeter.constant;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

public class ConstantLduCcube {

	/* Config for serial port mapping */
	public static final String SERIAL_PORT_LDU = "SerialCommLDU";
	public static final Integer LDU_DefaultBaudRate = 9600;

	/* Commands for LDU */
	public static final String CMD_LDU_ERR_SETTING_HDR_MTR_ADRS = DecodeHextoString("29FE21");
	public static final String CMD_LDU_ERR_SETTING_HDR_MTR_CONST = DecodeHextoString("29FE2F");
	public static final String CMD_LDU_ERR_SETTING_HDR_RSS_PULSE_RATE = DecodeHextoString("25");
	public static final String CMD_LDU_ERR_SETTING_HDR_MUT_PULSE_RATE = DecodeHextoString("22");
	public static final String CMD_LDU_ERR_SETTING_HDR_AVG_CYCLE = DecodeHextoString("28");
	public static final String CMD_LDU_ERR_SETTING_HDR_ERR_H_LMT = DecodeHextoString("26");
	public static final String CMD_LDU_ERR_SETTING_HDR_ERR_L_LMT = DecodeHextoString("2A");
	public static final String CMD_LDU_ERR_SETTING_HDR_ERR_CALC_MODE = DecodeHextoString("23");
	// public static final String CMD_LDU_ERROR_RESET =
	// DecodeHextoString("2901213030");
	public static final String CMD_LDU_ERROR_RESET = DecodeHextoString("2901213030");
	public static final String CMD_LDU_READ_ERROR_DATA_HDR = DecodeHextoString("29");
	public static final String CMD_LDU_READ_ERROR_DATA_CMD_DATA = DecodeHextoString("213033");

	public static final String CMD_LDU_CREEP_SETTING_HDR_ADRS_CMD = DecodeHextoString("29FE27");
	public static final String CMD_LDU_CREEP_SETTING_HDR_PULSE_COUNT = DecodeHextoString("2C");
	public static final String CMD_LDU_CREEP_SETTING_HDR_START_CREEP = DecodeHextoString("21");

	public static final String CMD_LDU_READ_CREEP_DATA_HDR = DecodeHextoString("29");
	public static final String CMD_LDU_READ_CREEP_DATA_CMD_DATA = DecodeHextoString("213033");

	public static final String CMD_LDU_CONST_SETTING_HDR = DecodeHextoString("29");
	public static final String CMD_LDU_CONST_SETTING_ADDRESS = DecodeHextoString("FE");
	public static final String CMD_LDU_CONST_SETTING_COMMAND = DecodeHextoString("21");
	public static final String CMD_LDU_CONST_SETTING_DATA = DecodeHextoString("3035");

	public static final String CMD_LDU_READ_CONST_DATA_HDR = DecodeHextoString("29");
	public static final String CMD_LDU_READ_CONST_DATA_CMD_DATA = DecodeHextoString("213033");

	public static final String CMD_LDU_CONST_RESET_ER = "2130303106";
	/// For result comparison
	public static final String CMD_LDU_ERROR_RESET_ER = "2130303106";
	public static final int CMD_LDU_ERROR_DATA_LENGTH = 12 * 2;

	public static final String CMD_LDU_ERROR_DATA_ER = "21";
	public static final String CMD_LDU_ERROR_DATA_ER_WFR = "AAAAAA";

	public static final String CMD_LDU_CREEP_RESET_ER = "2130303106";

	public static final int CMD_LDU_CREEP_DATA_LENGTH = 9 * 2;// Maximum 10 character, but 2 may be less depends on the
																// data, so taken average 9...and there is match buffer
																// of 3 char in SerialDataLDU:RemindTask
	public static final String CMD_LDU_CREEP_DATA_ER = "21";
	public static final String CMD_LDU_CREEP_DATA_ER_WFR = "AAAA";

	public static final String CMD_LDU_STA_SETTING_HDR_ADRS_CMD = DecodeHextoString("29FE27");
	public static final int CMD_LDU_STA_DATA_LENGTH = 9 * 2;// Maximum 10 character, but 2 may be less depends on the
															// data, so taken average 9...and there is match buffer of 3
															// char in SerialDataLDU:RemindTask
	public static final String CMD_LDU_STA_DATA_ER = "21";
	public static final String CMD_LDU_STA_RESET_ER = "2130303106";

	public static final String CMD_LDU_STA_SETTING_HDR_START_STA = DecodeHextoString("21");

	public static String StringToHex(String arg) {
		return String.format("%x", new BigInteger(1, arg.getBytes(/* YOUR_CHARSET? */)));
	}

	public static String DecodeHextoString(String hexString) {
		byte[] bytes = null;
		try {
			bytes = Hex.decodeHex(hexString.toCharArray());
		} catch (DecoderException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("DecodeHextoString : exception1: " + e.toString());
		}
		String ConvertedString = null;
		try {
			ConvertedString = new String(bytes, "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {

			ApplicationLauncher.logger.error("DecodeHextoString : exception2: " + e.toString());

			e.printStackTrace();
		}
		return ConvertedString;
	}
}
