package com.tasnetwork.calibration.energymeter.util;

import com.sun.javafx.scene.control.skin.TableViewSkin;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.constant.ConstantReportV2;
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Skin;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class GuiUtils {

	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static String FormatTimeForAvgPulses(int time_in_sec) {
		int min = time_in_sec / 60;
		int sec = time_in_sec % 60;
		if (min > 99) {
			min = 0;
		}
		String minute = String.format("%02d", min);
		String second = String.format("%02d", sec);
		String time = minute + second;
		return time;
	}

	public static String FormatPulseRate(String InputData) {
		/*
		 * int InputLength = InputData.length();
		 * int ExponentialValue = 0;
		 * int BeginningValue =0;
		 */
		long InputLength = InputData.length();
		long ExponentialValue = 0;
		long BeginningValue = 0;
		String OutputData = null;

		ApplicationLauncher.logger.info("FormatPulseRate : InputData: " + InputData);
		if (isNumber(InputData)) {
			if (InputLength == 4) {
				OutputData = InputData + "E00";
			} else if (InputLength > 4) {
				try {
					ExponentialValue = InputLength - 4;
					ApplicationLauncher.logger.debug("FormatPulseRate : ExponentialValue: " + ExponentialValue);
					// BeginningValue = Integer.parseInt(InputData)/((int) Math.pow(10,
					// ExponentialValue));
					BeginningValue = Long.parseLong(InputData) / ((long) Math.pow(10, ExponentialValue));
					ApplicationLauncher.logger.debug("FormatPulseRate : BeginningValue: " + BeginningValue);
					OutputData = BeginningValue + "E" + String.format("%02d", ExponentialValue);
					ApplicationLauncher.logger.debug("FormatPulseRate : OutputData: " + OutputData);
				} catch (Exception e) {
					ApplicationLauncher.logger.error("FormatPulseRate: Exception:" + e.getMessage());

				}
			} else if (InputLength < 4) {
				// int ValueTobeFormatted = Integer.parseInt(InputData);
				long ValueTobeFormatted = Long.parseLong(InputData);
				InputData = String.format("%04d", ValueTobeFormatted);
				OutputData = InputData + "E00";
			}
		} else {
			ApplicationLauncher.logger.info("FormatPulseRate : Not a valid data for Impulses/Unit: Current Data: <"
					+ InputData + ">:Expected data format: <3200>");
		}
		ApplicationLauncher.logger.info("FormatPulseRate : Impulses/Unit: OutputData: " + OutputData);
		return OutputData;
	}

	public static boolean isNumber(String value) {
		// ApplicationLauncher.logger.info("GUIUtils: isNumber: Entry:" );
		int size = 0;
		try {

			size = value.length();
			for (int i = 0; i < size; i++) {
				if (!Character.isDigit(value.charAt(i))) {

					return false;

				}
			}
		} catch (Exception e) {
			ApplicationLauncher.logger.error("GUIUtils: isNumber: Exception:" + e.getMessage());
			return false;
		}
		return size > 0;
	}

	public static void autoResizeColumns(TableView<?> table, Boolean IsLiveTable) {
		// Set the right policy
		table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		table.getColumns().stream().forEach((column) -> {

			javafx.scene.text.Text t = new javafx.scene.text.Text(column.getText());
			double max = t.getLayoutBounds().getWidth();
			for (int i = 0; i < table.getItems().size(); i++) {
				// cell must not be empty
				if (column.getCellData(i) != null) {
					t = new javafx.scene.text.Text(column.getCellData(i).toString());
					double calcwidth = t.getLayoutBounds().getWidth();
					// remember new max-width
					if (calcwidth > max) {
						max = calcwidth;
					}
				}
			}
			// set the new max-widht with some extra space
			if (IsLiveTable) {
				column.setPrefWidth(max + 30.0d);
			} else {
				column.setPrefWidth(max + 20.0d);
			}
		});
	}

	public static String FormatAvgPulses(String Pulses) {
		ApplicationLauncher.logger.debug("FormatAvgPulses:input Pulses: " + Pulses);
		int no_of_pulses = Integer.parseInt(Pulses);
		String pulses_in_hex = String.format("%04x", no_of_pulses).toUpperCase();

		ApplicationLauncher.logger.info("FormatAvgPulses : " + pulses_in_hex.toUpperCase());
		ApplicationLauncher.logger.debug("FormatAvgPulses:pulses_in_hex " + pulses_in_hex);
		return pulses_in_hex;
	}

	public static String FormatErrorInput(String InputErrorValue) {
		Boolean IsNegative = false;
		Boolean IsPositive = false;
		Boolean PopulatePlusSign = false;
		Boolean IsDotExist = false;
		Boolean IsDotExistInCorrectPosition = false;
		String OutputErrorValue = null;
		String regex = "\\d\\.?\\d*";
		// String regex = "^\\d*\\.\\d+|\\d+\\.\\d*$";
		// int InputLength = InputErrorValue.length();
		// ApplicationLauncher.logger.info("Test1:");

		boolean status = Validate_Error(InputErrorValue);
		if (!status) {
			return null;
		}
		if (InputErrorValue.contains("-")) {
			IsNegative = true;
			InputErrorValue = InputErrorValue.replaceFirst("-", "");
		}
		// ApplicationLauncher.logger.info("Test2:");
		if (InputErrorValue.contains("+")) {
			// ApplicationLauncher.logger.info("Test2-1:");
			IsPositive = true;
			// ApplicationLauncher.logger.info("Test2-2:");
			InputErrorValue = InputErrorValue.replaceFirst("\\+", "");
			// ApplicationLauncher.logger.info("Test2-3:");
		}

		int InputLength = InputErrorValue.length();
		// ApplicationLauncher.logger.info("test24:"+InputErrorValue);
		// ApplicationLauncher.logger.info("Test3:");
		if (InputErrorValue.contains(".")) {
			IsDotExist = true;
			if ((InputErrorValue.substring(1, 2)).equals(".")) {
				// ApplicationLauncher.logger.debug("FormatErrorInput :Dot exist in correct
				// Position");
				IsDotExistInCorrectPosition = true;
			}
			if ((InputErrorValue.substring(0, 1)).equals(".")) {
				// ApplicationLauncher.logger.debug("FormatErrorInput :Dot exist in correct
				// Position2");
				InputErrorValue = "0" + InputErrorValue;
				InputLength = InputErrorValue.length();
				IsDotExistInCorrectPosition = true;
			}
		}

		// ApplicationLauncher.logger.info("Test4:");
		if (!IsNegative && !IsPositive) {
			PopulatePlusSign = true;
		}

		// ApplicationLauncher.logger.info("Test5:"+OutputErrorValue);
		if (!IsDotExist) {

			if (InputLength < 2) {
				if (InputErrorValue.matches(regex)) {
					OutputErrorValue = InputErrorValue + ".";
					OutputErrorValue = OutputErrorValue + "00";
				}
			}
		} else {
			if (IsDotExistInCorrectPosition) {
				// ApplicationLauncher.logger.info("Test51:"+OutputErrorValue);
				if (InputLength == 4) {
					OutputErrorValue = InputErrorValue;
					// ApplicationLauncher.logger.info("Test52:"+OutputErrorValue);
				} else if (InputLength < 4) {
					// ApplicationLauncher.logger.info("Test53:"+OutputErrorValue);
					// ApplicationLauncher.logger.info("Test54:"+InputErrorValue);
					if (InputErrorValue.matches(regex)) {
						OutputErrorValue = InputErrorValue + "0";
						// ApplicationLauncher.logger.info("Test55:"+OutputErrorValue);
					}
				}

				if (InputLength > 4) {
					OutputErrorValue = InputErrorValue.substring(0, 4);
					// ApplicationLauncher.logger.info("Test56:"+OutputErrorValue);
				}

			}
		}

		// ApplicationLauncher.logger.info("Test6:"+OutputErrorValue);

		if (PopulatePlusSign || IsPositive) {
			if (!(OutputErrorValue == null)) {
				OutputErrorValue = "+" + OutputErrorValue;
			}
		} else {
			// OutputErrorValue = InputErrorValue;
		}

		// ApplicationLauncher.logger.info("Test7:"+OutputErrorValue);
		if (IsNegative) {
			if (!(OutputErrorValue == null)) {
				OutputErrorValue = "-" + OutputErrorValue;
			}
		}
		// ApplicationLauncher.logger.info("Test8:"+OutputErrorValue);
		return OutputErrorValue;

	}

	public static boolean Validate_voltage(String voltage) {
		boolean valid_status = false;
		// ApplicationLauncher.logger.info("Validate_voltage 1" );

		// if(!voltage.isEmpty()){
		// //ApplicationLauncher.logger.info("Validate_voltage 1" );
		// try{
		// float volt = Float.parseFloat(voltage);
		// String EM_CT_Type= ProjectController.getProjectEM_CT_Type();
		// if(EM_CT_Type.equals(ConstantApp.METER_CT_TYPE_LTCT)){
		// if((volt >= ConstantAppConfig.LTCT_VOLT_MIN) &&
		// (volt <= ConstantAppConfig.LTCT_VOLT_MAX)){
		// valid_status = true;
		// }else {
		// ApplicationLauncher.logger.info("Validate_voltage: LTCT voltage is not with
		// in acceptable limit. Kindly check the config file : input voltage:" +voltage
		// + ": ConfigProperty.VOLT_MIN:" + ConstantAppConfig.LTCT_VOLT_MIN + " :
		// ConfigProperty.VOLT_MAX:"+ConstantAppConfig.LTCT_VOLT_MAX);

		// }
		// }else if(EM_CT_Type.equals(ConstantApp.METER_CT_TYPE_HTCT)){
		// //ApplicationLauncher.logger.info("Validate_voltage 2" );
		// if((volt >= ConstantAppConfig.HTCT_VOLT_MIN) &&
		// (volt <= ConstantAppConfig.HTCT_VOLT_MAX)){
		// valid_status = true;
		// }else {
		// ApplicationLauncher.logger.info("Validate_voltage: LTCT voltage is not with
		// in acceptable limit. Kindly check the config file : input voltage:" +voltage
		// + ": ConfigProperty.VOLT_MIN:" + ConstantAppConfig.LTCT_VOLT_MIN + " :
		// ConfigProperty.VOLT_MAX:"+ConstantAppConfig.LTCT_VOLT_MAX);

		// }
		// }
		// }
		// catch(Exception e){
		// valid_status = false;
		// e.printStackTrace();
		// ApplicationLauncher.logger.error("Validate_voltage: Exception:"
		// +e.getMessage());
		// ApplicationLauncher.logger.info("Validate_voltage: voltage is not a valid
		// float value : input voltage:" +voltage);

		// }
		// }else{
		// ApplicationLauncher.logger.info("Validate_voltage: voltage is empty");

		// }
		return valid_status;

	}

	public static boolean Validate_current(String current) {
		boolean valid_status = false;
		// if(!current.isEmpty()){
		// try{
		// float i_current = Float.parseFloat(current);
		// String EM_CT_Type= ProjectController.getProjectEM_CT_Type();
		// if(EM_CT_Type.equals(ConstantApp.METER_CT_TYPE_LTCT)){
		// if((i_current >= ConstantAppConfig.LTCT_CURRENT_MIN) &&
		// (i_current <= ConstantAppConfig.LTCT_CURRENT_MAX)){
		// valid_status = true;
		// }else {
		// ApplicationLauncher.logger.info("Validate_current: LTCT current is not with
		// in acceptable limit. Kindly check the config file : input current:" +current
		// + ": ConfigProperty.CURRENT_MIN:" + ConstantAppConfig.LTCT_CURRENT_MIN + " :
		// ConfigProperty.CURRENT_MAX:"+ConstantAppConfig.LTCT_CURRENT_MAX);

		// }
		// }else if(EM_CT_Type.equals(ConstantApp.METER_CT_TYPE_HTCT)){
		// if((i_current >= ConstantAppConfig.HTCT_CURRENT_MIN) &&
		// (i_current <= ConstantAppConfig.HTCT_CURRENT_MAX)){
		// valid_status = true;
		// }else {
		// ApplicationLauncher.logger.info("Validate_current: HTCT current is not with
		// in acceptable limit. Kindly check the config file : input current:" +current
		// + ": ConfigProperty.CURRENT_MIN:" + ConstantAppConfig.HTCT_CURRENT_MIN + " :
		// ConfigProperty.CURRENT_MAX:"+ConstantAppConfig.HTCT_CURRENT_MAX);

		// }

		// }

		// }
		// catch(Exception e){
		// valid_status = false;
		// e.printStackTrace();
		// ApplicationLauncher.logger.error("Validate_current: Exception:"
		// +e.getMessage());
		// ApplicationLauncher.logger.info("Validate_current: current is not a valid
		// float value : input current:" +current);

		// }
		// }else {
		// ApplicationLauncher.logger.info("Validate_current: current is empty");

		// }
		return valid_status;

	}

	public static boolean Validate_phasedegree(String degree) {
		boolean valid_status = false;
		if (!degree.isEmpty()) {
			try {
				int phasedegree = Integer.parseInt(degree);
				if ((phasedegree >= ConstantAppConfig.DEGREE_MIN) &&
						(phasedegree <= ConstantAppConfig.DEGREE_MAX)) {
					valid_status = true;
				} else {
					ApplicationLauncher.logger.info(
							"Validate_phasedegree: phasedegree is not with in acceptable limit. Kindly check the config file : input phasedegree:"
									+ degree + ": ConfigProperty.DEGREE_MIN:" + ConstantAppConfig.DEGREE_MIN
									+ " : ConfigProperty.DEGREE_MAX:" + ConstantAppConfig.DEGREE_MAX);

				}
			} catch (Exception e) {
				valid_status = false;
				e.printStackTrace();
				ApplicationLauncher.logger.error("Validate_phasedegree: Exception:" + e.getMessage());
				ApplicationLauncher.logger.info(
						"Validate_phasedegree: phasedegree is not a valid float value : input phasedegree:" + degree);

			}
		} else {
			ApplicationLauncher.logger.info("Validate_phasedegree: phasedegree is empty");

		}
		return valid_status;

	}

	public static boolean Validate_PhaseLagLead(String degree) {
		ApplicationLauncher.logger.info("Validate_PhaseLagLead: PF: " + degree);
		boolean valid_status = false;
		if (!degree.isEmpty()) {
			try {
				String lag_lead_value = "";
				if (degree.equals("1")) {
					lag_lead_value = degree;
				} else {

					lag_lead_value = degree.substring(0, degree.length() - 1);
				}
				float phasedegree = Float.parseFloat(lag_lead_value);
				if ((phasedegree >= ConstantApp.PF_MIN) &&
						(phasedegree <= ConstantApp.PF_MAX)) {
					valid_status = true;
				} else if ((phasedegree == -1) && (degree.endsWith(ConstantApp.PF_LAG))) {
					valid_status = true;
				} else if ((phasedegree == -1) && (degree.endsWith(ConstantApp.PF_LEAD))) {
					valid_status = true;
				} else {
					ApplicationLauncher.logger
							.info("Validate_PhaseLagLead: pf is not with in acceptable limit.  input pf:" + degree
									+ ": MyProperty.PF_MIN:" + ConstantApp.PF_MIN + " : MyProperty.PF_MAX:"
									+ ConstantApp.PF_MAX);

				}
			} catch (Exception e) {
				valid_status = false;
				e.printStackTrace();
				ApplicationLauncher.logger.error("Validate_PhaseLagLead: Exception:" + e.getMessage());
				ApplicationLauncher.logger
						.info("Validate_PhaseLagLead: pf is not a valid float value : input pf:" + degree);

			}
		} else {
			ApplicationLauncher.logger.info("Validate_PhaseLagLead: pf is empty");

		}
		return valid_status;

	}

	public static boolean Validate_frequency(String freq) {
		boolean valid_status = false;
		if (!freq.isEmpty()) {
			try {
				float frequency = Float.parseFloat(freq);
				if ((frequency >= ConstantAppConfig.FREQUENCY_MIN) &&
						(frequency <= ConstantAppConfig.FREQUENCY_MAX)) {
					valid_status = true;
				} else {
					ApplicationLauncher.logger.info(
							"Validate_frequency: freq is not with in acceptable limit. Kindly check the config file : input freq:"
									+ freq + ": ConfigProperty.FREQUENCY_MIN:" + ConstantAppConfig.FREQUENCY_MIN
									+ " : ConfigProperty.FREQUENCY_MAX:" + ConstantAppConfig.FREQUENCY_MAX);

				}
			} catch (Exception e) {
				valid_status = false;
				e.printStackTrace();
				ApplicationLauncher.logger.error("Validate_frequency: Exception:" + e.getMessage());
				ApplicationLauncher.logger
						.info("Validate_frequency: pf is not a valid float value : input freq:" + freq);

			}
		} else {
			ApplicationLauncher.logger.info("Validate_frequency: freq is empty");

		}
		return valid_status;

	}

	public static boolean Validate_Error(String error) {
		boolean valid_status = false;
		if (!error.isEmpty()) {
			try {
				float error_value = Float.parseFloat(error);
				if ((error_value >= ConstantAppConfig.ERROR_MIN) &&
						(error_value <= ConstantAppConfig.ERROR_MAX)) {
					valid_status = true;
				} else {
					ApplicationLauncher.logger.info(
							"Validate_Error: freq is not with in acceptable limit. Kindly check the config file : input error:"
									+ error + ": ConfigProperty.ERROR_MIN:" + ConstantAppConfig.ERROR_MIN
									+ " : ConfigProperty.ERROR_MAX:" + ConstantAppConfig.ERROR_MAX);

				}
			} catch (Exception e) {
				valid_status = false;
				e.printStackTrace();
				ApplicationLauncher.logger.error("Validate_Error: Exception:" + e.getMessage());
				ApplicationLauncher.logger.info("Validate_Error: pf is not a valid float value : input error:" + error);

			}
		} else {
			ApplicationLauncher.logger.info("Validate_Error: error is empty");

		}
		return valid_status;

	}

	public static boolean is_number(String value) {
		boolean valid_status = false;
		if (!value.isEmpty()) {
			try {
				float parsed_value = Integer.parseInt(value);
				if (parsed_value >= 0) {// By Prasanth
					valid_status = true;
				}
			} catch (Exception e) {
				valid_status = false;
				e.printStackTrace();
				ApplicationLauncher.logger.error("is_number: Exception:" + e.getMessage());
			}
		}
		return valid_status;

	}

	public static boolean is_float(String value) {
		boolean valid_status = false;
		if (!value.isEmpty()) {
			try {
				float parsed_value = Float.parseFloat(value);
				// if(parsed_value >= 0){// By Prasanth
				valid_status = true;
				// }
			} catch (Exception e) {
				valid_status = false;
				e.printStackTrace();
				ApplicationLauncher.logger.error("is_float: Exception:" + e.getMessage());
			}
		}
		return valid_status;

	}

	public static boolean is_long(String value) {
		boolean valid_status = false;
		if (!value.isEmpty()) {
			try {
				long parsed_value = Long.parseLong(value);
				valid_status = true;
			} catch (Exception e) {
				valid_status = false;
				e.printStackTrace();
				ApplicationLauncher.logger.error("is_long: Exception:" + e.getMessage());
			}
		}
		return valid_status;

	}

	public static String FormatUnForDisplay(String Un) {
		// ApplicationLauncher.logger.info("FormatUnForDisplay: Un: " + Un);

		String un_display_name = Un + "U";

		return un_display_name;
	}

	public static boolean Validate_time_duration(String time_duration) {
		boolean valid_time_duration_status = false;
		if (!time_duration.isEmpty()) {
			try {
				int time = Integer.parseInt(time_duration);
				if ((time >= ConstantAppConfig.TIME_MIN) &&
						(time <= ConstantAppConfig.TIME_MAX)) {
					valid_time_duration_status = true;
				} else {
					ApplicationLauncher.logger.info(
							"Validate_time_duration: time_duration is not with in acceptable limit. Kindly check the config file : input time:"
									+ time_duration + ": ConfigProperty.TIME_MIN:" + ConstantAppConfig.TIME_MIN
									+ " : ConfigProperty.TIME_MAX:" + ConstantAppConfig.TIME_MAX);

				}
			} catch (Exception e) {
				valid_time_duration_status = false;
				e.printStackTrace();
				ApplicationLauncher.logger.error("Validate_time_duration: Exception:" + e.getMessage());
				ApplicationLauncher.logger.info(
						"Validate_time_duration: time_duration is not a valid int value : input time:" + time_duration);

			}
		} else {
			ApplicationLauncher.logger.info("Validate_time_duration: time_duration is empty");

		}
		return valid_time_duration_status;

	}

	public static String generateCommandforRefStdBNC_Constant(String bncConstant, String bncMetricIndexCommand) {
		double InputData = Float.valueOf(bncConstant);
		// double RSS_InputSetValue = (1/(InputData))*1000;
		double RSS_InputSetValue = (1 / (InputData));
		// double RSS_InputSetValue = (1/(InputData));
		ApplicationLauncher.logger.info("InputData:" + InputData);
		// ApplicationLauncher.logger.info("RSS_InputSetValue:"+RSS_InputSetValue);
		// String OutputData = TMS_FloatConversion.FloatToTMS320_Hex(InputData);
		// ApplicationLauncher.logger.info("OutputData:"+OutputData);
		String RSS_InputSetValueString = String.format("%.09f", RSS_InputSetValue);
		// String RSS_InputSetValueString = String.format ("%.011f", RSS_InputSetValue);
		ApplicationLauncher.logger.info("RSS_InputSetValueString:" + RSS_InputSetValueString);
		InputData = Float.valueOf(RSS_InputSetValueString);
		String FloatHexData = TMS_FloatConversion.FloatToTMS320_Hex(InputData);
		ApplicationLauncher.logger.info("FloatHexData:" + FloatHexData);
		String RSS_SetCommandWithOutCheckSum = bncMetricIndexCommand + FloatHexData;
		String checkSum = generateCheckSum(RSS_SetCommandWithOutCheckSum);
		ApplicationLauncher.logger.info("checkSum:" + checkSum);
		String OutputData = RSS_SetCommandWithOutCheckSum + checkSum;
		ApplicationLauncher.logger.info("OutputData:" + OutputData);

		/*
		 * RSS_SetCommandWithOutCheckSum = "A632000700000100000000";
		 * checkSum = generateCheckSum(RSS_SetCommandWithOutCheckSum);
		 * ApplicationLauncher.logger.info("checkSum:"+checkSum);
		 * OutputData = RSS_SetCommandWithOutCheckSum+checkSum;
		 * ApplicationLauncher.logger.info("OutputData:"+OutputData);
		 */
		return OutputData;
	}

	public static String generateCheckSum(String InputHexString) {

		String hex_value = new String();
		// 'hex_value' will be used to store various hex values as a string
		int x, i, checksum = 0;
		// 'x' will be used for general purpose storage of integer values
		// 'i' is used for loops
		// 'checksum' will store the final checksum
		for (i = 0; i < InputHexString.length() - 2; i = i + 2) {
			// x = (int) (InputHexString.charAt(i));
			// hex_value = (InputHexString.charAt(i));//Integer.toHexString(x);
			// x = (int) (InputHexString.charAt(i + 1));
			// hex_value = hex_value + Integer.toHexString(x);
			hex_value = Character.toString(InputHexString.charAt(i)) + Character.toString(InputHexString.charAt(i + 1));
			// Extract two characters and get their hexadecimal ASCII values
			ApplicationLauncher.logger.info("generateCheckSum: InputHexString1:" + InputHexString.charAt(i) + ""
					+ InputHexString.charAt(i + 1) + " : "
					+ hex_value);
			x = Integer.parseInt(hex_value, 16);
			ApplicationLauncher.logger.info("generateCheckSum: Int Value:" + x);
			// Convert the hex_value into int and store it
			checksum += x;
			// Add 'x' into 'checksum'
		}
		if (InputHexString.length() % 2 == 0) {
			// If number of characters is even, then repeat above loop's steps
			// one more time.
			// x = (int) (InputHexString.charAt(i));
			// hex_value = Integer.toHexString(x);
			// x = (int) (InputHexString.charAt(i + 1));
			// hex_value = hex_value + Integer.toHexString(x);
			hex_value = Character.toString(InputHexString.charAt(i)) + Character.toString(InputHexString.charAt(i + 1));

			ApplicationLauncher.logger.info("generateCheckSum: InputHexString2:" + InputHexString.charAt(i) + ""
					+ InputHexString.charAt(i + 1) + " : "
					+ hex_value);
			x = Integer.parseInt(hex_value, 16);
		} else {
			// If number of characters is odd, last 2 digits will be 00.
			x = (int) (InputHexString.charAt(i));
			hex_value = "00" + Character.toString(InputHexString.charAt(i));// Integer.toHexString(x);
			x = Integer.parseInt(hex_value, 16);
			ApplicationLauncher.logger
					.info("generateCheckSum: InputHexString3:" + InputHexString.charAt(i) + " : " + hex_value);
		}
		checksum += x;
		// Add the generated value of 'x' from the if-else case into 'checksum'
		hex_value = Integer.toHexString(checksum);
		hex_value = StringUtils.leftPad(hex_value.toUpperCase(), 4, '0');
		// hex_value= String.format("%04", hex_value);
		// Convert into hexadecimal string
		/*
		 * if (hex_value.length() > 4)
		 * {
		 * // If a carry is generated, then we wrap the carry
		 * int carry = Integer.parseInt(("" + hex_value.charAt(0)), 16);
		 * // Get the value of the carry bit
		 * hex_value = hex_value.substring(1, 5);
		 * // Remove it from the string
		 * checksum = Integer.parseInt(hex_value, 16);
		 * // Convert it into an int
		 * checksum += carry;
		 * // Add it to the checksum
		 * }
		 */
		// checksum = generateComplement(checksum);
		// Get the complement
		return hex_value;

	}

	public static String generateCheckSumWithOneByte(String InputHexString) {

		String hex_value = new String();
		// 'hex_value' will be used to store various hex values as a string
		int x, i, checksum = 0;

		for (i = 0; i < InputHexString.length() - 2; i = i + 2) {

			hex_value = Character.toString(InputHexString.charAt(i)) + Character.toString(InputHexString.charAt(i + 1));
			// Extract two characters and get their hexadecimal ASCII values
			// ApplicationLauncher.logger.info("generateCheckSumWithOneByte:
			// "+InputHexString.charAt(i) + "" + InputHexString.charAt(i + 1) + " : "
			// + hex_value);
			x = Integer.parseInt(hex_value, 16);
			// ApplicationLauncher.logger.info("generateCheckSumWithOneByte: Int Value:" + x
			// );
			// Convert the hex_value into int and store it
			checksum += x;
			// Add 'x' into 'checksum'
		}
		if (InputHexString.length() % 2 == 0) {

			hex_value = Character.toString(InputHexString.charAt(i)) + Character.toString(InputHexString.charAt(i + 1));

			// ApplicationLauncher.logger.info("generateCheckSumWithOneByte:
			// "+InputHexString.charAt(i) + "" + InputHexString.charAt(i + 1) + " : "
			// + hex_value);
			x = Integer.parseInt(hex_value, 16);
		} else {
			// If number of characters is odd, last 2 digits will be 00.
			x = (int) (InputHexString.charAt(i));
			hex_value = "00" + Character.toString(InputHexString.charAt(i));// Integer.toHexString(x);
			x = Integer.parseInt(hex_value, 16);
			// ApplicationLauncher.logger.info("generateCheckSumWithOneByte:
			// "+InputHexString.charAt(i) + " : " + hex_value);
		}
		checksum += x;
		// Add the generated value of 'x' from the if-else case into 'checksum'
		hex_value = Integer.toHexString(checksum);
		hex_value = StringUtils.leftPad(hex_value.toUpperCase(), 4, '0');
		// hex_value= String.format("%04", hex_value);
		// Convert into hexadecimal string

		// checksum = generateComplement(checksum);
		// Get the complement
		if (hex_value.length() > 2) {
			hex_value = hex_value.substring(Math.max(hex_value.length() - 2, 0));
			// ApplicationLauncher.logger.info("generateCheckSumWithOneByte: trimmed :
			// "+hex_value);
		}
		return hex_value;

	}

	public static String hexToAscii(String hexStr) {
		StringBuilder output = new StringBuilder("");

		for (int i = 0; i < hexStr.length(); i += 2) {
			String str = hexStr.substring(i, i + 2);
			output.append((char) Integer.parseInt(str, 16));
		}

		return output.toString();
	}

	public static String hexToAsciiV2(String inputHexStr) {
		// when using hexToAscii - beyond data 0x7F all converted as 0x3F which is
		// equivalent to "?"\
		// this is due to limitation of Ascii...hence need to use the Extended
		// ascii...which is UTF-8 format
		String convertedUtf8Value = "";
		// String hex = "6174656ec3a7c3a36f"; // AAA
		ByteBuffer buff = ByteBuffer.allocate(inputHexStr.length() / 2);
		for (int i = 0; i < inputHexStr.length(); i += 2) {
			buff.put((byte) Integer.parseInt(inputHexStr.substring(i, i + 2), 16));
		}
		buff.rewind();
		// Charset cs = Charset.forName("UTF-8"); //("UTF-8"); // BBB
		Charset cs = Charset.forName("ISO-8859-1");
		// Charset cs = Charset.forName("UTF-16");
		CharBuffer cb = cs.decode(buff); // BBB
		convertedUtf8Value = cb.toString(); // CCC
		// ApplicationLauncher.logger.debug("hexToString: convertedUtf8Value: "+
		// convertedUtf8Value);
		return convertedUtf8Value;
	}

	/*
	 * public static byte[] hexToString(String inputHexStr) {
	 * // when using hexToAscii - beyond data 0x7F all converted as 0x3F which is
	 * equivalent to "?"\
	 * // this is due to limitation of Ascii...hence need to use the Extended
	 * ascii...which is UTF-8 format
	 * String convertedUtf8Value = "";
	 * Charset utf8charset = Charset.forName("UTF-8");
	 * Charset iso88591charset = Charset.forName("ISO-8859-1");
	 * 
	 * ByteBuffer inputBuffer = ByteBuffer.wrap(new byte[]{(byte)0x01, (byte)0x92,
	 * (byte)0x04, (byte)0x97, (byte)0x17});//0192049717
	 * 
	 * // decode UTF-8
	 * CharBuffer data = utf8charset.decode(inputBuffer);
	 * 
	 * // encode ISO-8559-1
	 * ByteBuffer outputBuffer = iso88591charset.encode(data);
	 * byte[] outputData = outputBuffer.array();
	 * return outputData;
	 * }
	 */

	public static String HexToString(String hex) {

		StringBuilder sb = new StringBuilder();
		StringBuilder temp = new StringBuilder();

		// 49204c6f7665204a617661 split into two characters 49, 20, 4c...
		try {
			for (int i = 0; i < hex.length() - 1; i += 2) {

				// grab the hex in pairs
				String output = hex.substring(i, (i + 2));
				// convert hex to decimal
				int decimal = Integer.parseInt(output, 16);
				// convert the decimal to character
				sb.append((char) decimal);

				temp.append(decimal);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("HexToString: Exception: " + e.getMessage());
			return "";
		}
		// System.out.println("Decimal : " + temp.toString());

		return sb.toString();
	}

	public static String StringToHex(String arg) {
		// ApplicationLauncher.logger.debug("StringToHex : arg: " + arg);
		return String.format("%02x", new BigInteger(1, arg.getBytes(/* YOUR_CHARSET? */))).toUpperCase();
	}

	public static String calculateLinearInterpolation(float inpValue_X, float lastReadRmsValue_Y1,
			float lastReadCalibPointValue_X1,
			float lastReadRmsValue_Y2, float lastReadCalibPointValue_X2) {

		// y= mx+c //y = y1 + (x-x1)[(y2-y1)/(x2-x1)] // unknown is y
		float result_Y = lastReadRmsValue_Y1 +
				((inpValue_X - lastReadCalibPointValue_X1) *
						((lastReadRmsValue_Y2 - lastReadRmsValue_Y1)
								/ (lastReadCalibPointValue_X2 - lastReadCalibPointValue_X1)));
		ApplicationLauncher.logger.debug("calculateLinearInterpolation: result_Y: " + result_Y);
		String resultLongString = String.valueOf((long) Double.parseDouble(String.valueOf(result_Y)));
		ApplicationLauncher.logger.debug("calculateLinearInterpolation: resultLongString: " + resultLongString);
		return resultLongString;
	}

	public static String calculateGain(float lastReadRmsValue_Y1, float lastReadCalibPointValue_X1,
			float presentReadRmsValue_Y2, float presentReadCalibPointValue_X2) {

		// y= mx+c , m is gain , c is offset
		ApplicationLauncher.logger.debug("calculateGain: presentReadRmsValue_Y2: " + presentReadRmsValue_Y2);
		ApplicationLauncher.logger.debug("calculateGain: lastReadRmsValue_Y1: " + lastReadRmsValue_Y1);
		ApplicationLauncher.logger
				.debug("calculateGain: presentReadCalibPointValue_X2: " + presentReadCalibPointValue_X2);
		ApplicationLauncher.logger.debug("calculateGain: lastReadCalibPointValue_X1: " + lastReadCalibPointValue_X1);
		double resultGain = ((presentReadRmsValue_Y2 - lastReadRmsValue_Y1)
				/ (presentReadCalibPointValue_X2 - lastReadCalibPointValue_X1));
		ApplicationLauncher.logger.debug("calculateGain: resultGain: " + resultGain);
		String resultRoundedString = String.format("%.02f", resultGain);
		ApplicationLauncher.logger.debug("calculateGain: resultString: " + resultRoundedString);
		return resultRoundedString;
	}

	public static String calculateOffset(float lastReadRmsValue_Y1, float lastReadCalibPointValue_X1,
			double presentGain) {

		// y= mx+c , m is gain , c is offset
		ApplicationLauncher.logger.debug("calculateOffset: lastReadRmsValue_Y1: " + lastReadRmsValue_Y1);
		ApplicationLauncher.logger.debug("calculateOffset: lastReadCalibPointValue_X1: " + lastReadCalibPointValue_X1);
		ApplicationLauncher.logger.debug("calculateOffset: presentGain: " + presentGain);
		double resultOffset = lastReadRmsValue_Y1 - (lastReadCalibPointValue_X1 * presentGain);
		ApplicationLauncher.logger.debug("calculateOffset: resultOffset: " + resultOffset);
		String resultRoundedString = String.format("%.02f", resultOffset);
		ApplicationLauncher.logger.debug("calculateOffset: resultString: " + resultRoundedString);
		return resultRoundedString;
	}

	public static String generateXorCheckSum(String InputHexString) {
		String hex_value = new String();
		// 'hex_value' will be used to store various hex values as a string
		int x, i, checksum = 0;
		// 'x' will be used for general purpose storage of integer values
		// 'i' is used for loops
		// 'checksum' will store the final checksum
		for (i = 0; i < InputHexString.length() - 2; i = i + 2) {
			hex_value = Character.toString(InputHexString.charAt(i)) + Character.toString(InputHexString.charAt(i + 1));
			// Extract two characters and get their hexadecimal ASCII values
			ApplicationLauncher.logger.info("generateXorCheckSum: InputHexString1:" + InputHexString.charAt(i) + ""
					+ InputHexString.charAt(i + 1) + " : "
					+ hex_value);
			x = Integer.parseInt(hex_value, 16);
			ApplicationLauncher.logger.info("generateXorCheckSum: Int Value:" + x);
			// Convert the hex_value into int and store it
			// checksum += x;
			checksum ^= x;
			// Add 'x' into 'checksum'
		}
		if (InputHexString.length() % 2 == 0) {
			hex_value = Character.toString(InputHexString.charAt(i)) + Character.toString(InputHexString.charAt(i + 1));
			ApplicationLauncher.logger.info("generateXorCheckSum: InputHexString2:" + InputHexString.charAt(i) + ""
					+ InputHexString.charAt(i + 1) + " : "
					+ hex_value);
			x = Integer.parseInt(hex_value, 16);
		} else {
			// If number of characters is odd, last 2 digits will be 00.
			x = (int) (InputHexString.charAt(i));
			hex_value = "00" + Character.toString(InputHexString.charAt(i));// Integer.toHexString(x);
			x = Integer.parseInt(hex_value, 16);
			ApplicationLauncher.logger
					.info("generateXorCheckSum: InputHexString3:" + InputHexString.charAt(i) + " : " + hex_value);
		}
		checksum ^= x;
		// Add the generated value of 'x' from the if-else case into 'checksum'
		hex_value = Integer.toHexString(checksum);
		hex_value = StringUtils.leftPad(hex_value.toUpperCase(), 2, '0');
		return hex_value;
	}

	public static String getMax(String rPhaseData, String yPhaseData, String bPhaseData) {
		String maxValue = null;
		ApplicationLauncher.logger.info("getMax :Entry");
		ApplicationLauncher.logger.info("getMax : rPhaseData: " + rPhaseData);
		ApplicationLauncher.logger.info("getMax : yPhaseData: " + yPhaseData);
		ApplicationLauncher.logger.info("getMax : bPhaseData: " + bPhaseData);
		try {
			float rPhaseValue = Float.parseFloat(rPhaseData);
			float yPhaseValue = Float.parseFloat(yPhaseData);
			float bPhaseValue = Float.parseFloat(bPhaseData);
			List<Float> list = Arrays.asList(rPhaseValue, yPhaseValue, bPhaseValue);
			Float max = Collections.max(list);
			maxValue = String.valueOf(max);
			ApplicationLauncher.logger.info("getMax : maxValue: " + maxValue);
		} catch (Exception e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("getMax : Exception :" + e.getMessage());
		}

		return maxValue;

	}

	public static boolean validateAvgPulses(String avgPulses) {
		boolean status = false;
		ApplicationLauncher.logger.debug("FormatAvgPulses:input avgPulses: " + avgPulses);
		if (is_number(avgPulses)) {
			int avg_no_of_pulses = Integer.parseInt(avgPulses);
			if (avg_no_of_pulses >= ConstantApp.PULSE_AVERAGE_MIN_VALUE) {
				status = true;
			}
		}

		ApplicationLauncher.logger.debug("validateAvgPulses : status: " + status);
		return status;
	}

	public static long calcEpoch(String Date_time) {

		long epoch = 0;
		// String str = "2014-07-04 04:05:10"; // UTC
		String str = Date_time; // UTC

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date datenew = null;
		try {
			datenew = df.parse(str);
		} catch (ParseException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("calcEpoch: ParseException: " + e.getMessage());
		}
		epoch = datenew.getTime() / 1000;

		return epoch;
	}

	public static void calculateDegreeWithPf(String pfValue, String displayFormat) {
		// String displayFormat = ConstantApp.DISPLAY_PHASE_ANGLE_DEGREE_RESOLUTION;
		// ApplicationLauncher.logger.debug("calculateDegreeWithPf: pfValue: "+
		// pfValue);
		// String degree =
		// String.valueOf(Math.acos(Math.toDegrees(Double.parseDouble(pfValue))));
		double degree = Math.toDegrees(Math.acos(Double.parseDouble(pfValue)));
		String degreeStr = String.format(displayFormat, degree);
		// String degreeStr =
		// String.valueOf(Math.toDegrees(Math.acos(Double.parseDouble(pfValue))));
		ApplicationLauncher.logger.debug("calculateDegreeWithPf: pfValue: " + pfValue + " - > degreeStr: " + degreeStr);
	}

	public static String calculatePowerFactorWithDegree(String degreeValue, String displayFormat) {
		// String displayFormat = ConstantApp.DISPLAY_PHASE_ANGLE_DEGREE_RESOLUTION;
		// ApplicationLauncher.logger.debug("calculateDegreeWithPf: pfValue: "+
		// pfValue);
		// String degree =
		// String.valueOf(Math.acos(Math.toDegrees(Double.parseDouble(pfValue))));
		// double pfValue = Math.toDegrees(Math.acos(Double.parseDouble(degreeValue)));
		// double pfValue = Math.cos(Math.toDegrees(Double.parseDouble(degreeValue)));
		// double pfValue = Math.cos(Math.toDegrees(Double.parseDouble(degreeValue))*
		// Math.PI);
		// double pfValue = Math.acos(Math.toDegrees(Double.parseDouble(degreeValue))*
		// Math.PI);NAN
		// double pfValue = Math.cos(Math.toDegrees(Double.parseDouble(degreeValue)));
		double pfValue = Math.cos(Math.toRadians(Double.parseDouble(degreeValue)));
		// double pfValue =
		// Math.acos(Math.toRadians(Double.parseDouble(degreeValue)));NAN
		String pfValueStr = String.format(displayFormat, pfValue);
		// String degreeStr =
		// String.valueOf(Math.toDegrees(Math.acos(Double.parseDouble(pfValue))));
		ApplicationLauncher.logger.debug(
				"calculatePowerFactorWithDegree: degreeValue: " + degreeValue + " - > pfValueStr: " + pfValueStr);
		return pfValueStr;
	}

	public static String convertFloatToStringWithSetScale(float inputValue, int setScaleAfterDecimal) {
		BigDecimal bigValue = new BigDecimal(inputValue);
		// bigValue = bigValue.setScale(setScaleAfterDecimal, RoundingMode.FLOOR);
		bigValue = new BigDecimal(bigValue.toPlainString()).setScale((setScaleAfterDecimal + 1), RoundingMode.CEILING);
		// ApplicationLauncher.logger.debug("parseHexStringToFloatString: bigValue: " +
		// bigValue);
		// value = bigValue.floatValue();
		String valueStr = bigValue.toPlainString();
		valueStr = valueStr.substring(0, valueStr.length() - 1);
		// ApplicationLauncher.logger.debug("parseHexStringToFloatString: value2: " +
		// value);
		// valueStr = String.format("%.04f", value);
		// ApplicationLauncher.logger.debug("convertFloatToStringWithSetScale:
		// ######################################");
		// ApplicationLauncher.logger.debug("parseHexStringToFloatString:
		// inputHexString2: " + inputHexString);
		// ApplicationLauncher.logger.debug("convertFloatToStringWithSetScale:
		// valueStr2: " + valueStr);
		// ApplicationLauncher.logger.debug("convertFloatToStringWithSetScale:
		// =====================================");
		return valueStr;
	}

	public static String parseHexStringToFloatString(String inputHexString) {
		// ApplicationLauncher.logger.debug("parseHexStringToFloatString:
		// inputHexString: " + inputHexString);
		String exponentialParsedSign = inputHexString.substring(0, 1);
		int exponentialParsedValue = Integer.parseInt(inputHexString.substring(1, 2));
		// float exponentialParsedValue =
		// Float.parseFloat(inputHexString.substring(1,2));
		if (exponentialParsedSign.equals("1")) {
			exponentialParsedValue = -exponentialParsedValue;
		}
		// ApplicationLauncher.logger.debug("parseHexStringToFloatString:
		// exponentialParsedValue: " + exponentialParsedValue);
		String mantissaParsedSign = inputHexString.substring(2, 3);
		float mantissaParsedValue = Float.parseFloat(inputHexString.substring(3, 10));
		if (mantissaParsedSign.equals("1")) {
			mantissaParsedValue = -mantissaParsedValue;
		}
		// ApplicationLauncher.logger.debug("parseHexStringToFloatString:
		// mantissaParsedValue: " + mantissaParsedValue);

		float value = (mantissaParsedValue / 1000000) * (float) Math.pow(10, exponentialParsedValue);
		// ApplicationLauncher.logger.debug("parseHexStringToFloatString: value: " +
		// value);
		String valueStr = convertFloatToStringWithSetScale(value, 4);
		// ApplicationLauncher.logger.debug("parseHexStringToFloatString: valueStr: " +
		// valueStr);
		return valueStr;
	}

	public static String convertDegreeToMsecWithMultiplier(String degree, float multiplierValue) {

		String timeInMsec = "0";

		try {
			if (NumberUtils.isNumber(degree)) {
				int inputTime = (int) (((Float.valueOf(degree) / 360.0) * 20.0) * multiplierValue);
				timeInMsec = String.valueOf(inputTime);
				ApplicationLauncher.logger.debug(
						"convertDegreeToMsecWithMultiplier : degreeValue: " + degree + " : timeInMsec: " + timeInMsec);
			} else {
				ApplicationLauncher.logger
						.debug("convertDegreeToMsecWithMultiplier : input degree not a number: " + degree);
			}
		} catch (Exception E) {
			ApplicationLauncher.logger.error("convertDegreeToMsecWithMultiplier : Exception: " + E.getMessage());
		}

		return timeInMsec;

	}

	public static <T> void ArrayListReplaceIf(List<T> list, Predicate<? super T> pred, UnaryOperator<T> op) {
		list.replaceAll(t -> pred.test(t) ? op.apply(t) : t);
	}

	public static boolean isUserConfirmedToDelete(String title, String message) {

		// String metertype = DisplayDataObj.getDeployedEM_ModelType();
		// String message = "Are you sure to delete the filter?";

		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("file:images/" + ConstantVersion.APP_ICON_FILENAME));
		alert.setTitle(title);
		alert.setHeaderText("Confirmation");
		alert.setContentText(message);

		alert.getButtonTypes().clear();
		alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

		// Deactivate Defaultbehavior for yes-Button:
		Button yesButton = (Button) alert.getDialogPane().lookupButton(ButtonType.YES);
		yesButton.setDefaultButton(false);

		// Activate Defaultbehavior for no-Button:
		Button noButton = (Button) alert.getDialogPane().lookupButton(ButtonType.NO);
		noButton.setDefaultButton(true);

		final Optional<ButtonType> result = alert.showAndWait();
		return result.get() == ButtonType.YES;
	}

	public static String textFieldInputDialogDisplay(String header, String title) {
		ApplicationLauncher.logger.debug("textFieldInputDialogDisplay: Entry ");
		// Platform.runLater(() -> {

		// TextAreaInputDialog dialog = new TextAreaInputDialog();
		String userInputData = "";
		TextFieldInputDialog dialog = new TextFieldInputDialog();

		dialog.setHeaderText(header);// "Enter ParamProfile Name");
		dialog.setTitle(title);// "ParamProfile");
		dialog.setGraphic(null);
		// dialog.eee
		// Show the dialog and capture the result.
		Optional result = dialog.showAndWait();

		// If the "Okay" button was clicked, the result will contain our String in the
		// get() method
		if (result.isPresent()) {
			// System.out.println(result.get());

			ApplicationLauncher.logger.debug("textFieldInputDialogDisplay: result.get(): " + result.get());
			userInputData = (String) result.get();
			// ref_cmbBxOperationParamProfileName.getItems().add(result.get());
			// ref_cmbBxOperationParamProfileName.getSelectionModel().select(result.get());
			// ref_tvOperationParamProfile.getItems().clear();
			// getSerialNo().set(1);
		}
		return userInputData;
		// });
	}

	/// String testCasePreview = "REP_XX-100U-1.0-1.0Ib-1-2";
	public static String getTestPointFilterData(String testCasePreview) {

		ApplicationLauncher.logger.debug("getTestPointFilterData: testCasePreview: " + testCasePreview);

		try {
			// String testCasePreview = "STA_XX-100U-1.0-0.004Ib";
			// testCasePreview = "NLD_XX-115U";
			// if(testCasePreview.startsWith(ConstantApp.TEST_PROFILE_STA)){
			// testCasePreview = testCasePreview.replace("U-1.0-", "U-");
			// }
			String[] splittedData = testCasePreview.split(ConstantReportV2.TEST_ID_MASK);

			ApplicationLauncher.logger.debug("getTestPointFilterData: splittedData[0]:" + splittedData[0]);
			ApplicationLauncher.logger.debug("getTestPointFilterData: splittedData[1]:" + splittedData[1]);
			String testCaseFilterName = splittedData[1].substring(0,
					splittedData[1].lastIndexOf(ConstantReportV2.TEST_POINT_FILTER_SEPERATOR));
			testCaseFilterName = testCaseFilterName.substring(0,
					testCaseFilterName.lastIndexOf(ConstantReportV2.TEST_POINT_FILTER_SEPERATOR));
			testCaseFilterName = testCaseFilterName.replace(ConstantReport.EXTENSION_TYPE_CURRENT_IB, "I")
					.replace(ConstantReport.EXTENSION_TYPE_CURRENT_IMAX, "I");
			ApplicationLauncher.logger.debug("getTestPointFilterData: testCaseFilterName:" + testCaseFilterName);
			return testCaseFilterName;

		} catch (Exception e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("getTestPointFilterData : Exception:" + e.getMessage());
		}

		return "";
	}

	public static String getPfDataFromTestPoint(String testPointName) {

		ApplicationLauncher.logger.debug("getPfDataFromTestPoint: testPointName: " + testPointName);

		try {
			// String testCasePreview = "STA_XX-100U-1.0-0.004Ib";
			// testCasePreview = "NLD_XX-115U";
			// if(testCasePreview.startsWith(ConstantApp.TEST_PROFILE_STA)){
			// testCasePreview = testCasePreview.replace("U-1.0-", "U-");
			// }
			String pfData = "";
			// ApplicationLauncher.logger.debug("getPfDataFromTestPoint: testPointName1: <"
			// + ConstantApp.STA_ALIAS_NAME+ConstantReportV2.TEST_POINT_NAME_SEPERATOR+">");
			if (testPointName.startsWith(ConstantApp.STA_ALIAS_NAME + ConstantReportV2.TEST_POINT_NAME_SEPERATOR)) {
				pfData = "1.0";
			} else if (testPointName
					.contains(ConstantApp.WARMUP_ALIAS_NAME + ConstantReportV2.TEST_POINT_NAME_SEPERATOR)) {
				pfData = "1.0";
			} else {
				String[] splittedData = testPointName.split(ConstantReportV2.TEST_POINT_FILTER_SEPERATOR);
				// ApplicationLauncher.logger.debug("getPfDataFromTestPoint: testPointName:" +
				// testPointName);
				ApplicationLauncher.logger.debug("getPfDataFromTestPoint: splittedData[0]:" + splittedData[0]);
				ApplicationLauncher.logger.debug("getPfDataFromTestPoint: splittedData[1]:" + splittedData[1]);
				ApplicationLauncher.logger.debug("getPfDataFromTestPoint: splittedData[2]:" + splittedData[2]);
				pfData = splittedData[2];
			}

			if (pfData.equals("1.0")) {
				pfData = ConstantApp.PF_UPF;

			}
			return pfData;
			/*
			 * String testCaseFilterName = splittedData[1].substring(0,
			 * splittedData[1].lastIndexOf(ConstantReportV2.TEST_POINT_FILTER_SEPERATOR));
			 * testCaseFilterName = testCaseFilterName.substring(0,
			 * testCaseFilterName.lastIndexOf(ConstantReportV2.TEST_POINT_FILTER_SEPERATOR))
			 * ;
			 * testCaseFilterName =
			 * testCaseFilterName.replace(ConstantReport.EXTENSION_TYPE_CURRENT_IB,
			 * "I").replace(ConstantReport.EXTENSION_TYPE_CURRENT_IMAX, "I");
			 * ApplicationLauncher.logger.
			 * debug("getPfDataFromTestPoint: testCaseFilterName:" + testCaseFilterName);
			 * return testCaseFilterName;
			 */

		} catch (Exception e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("getPfDataFromTestPoint : Exception:" + e.getMessage());
		}

		return "";
	}

	public static Map<Boolean, String> isLicenseVerificationMatching_V1_0(long epochTimeInSec,
			String userInputSaltedKey) {
		ApplicationLauncher.logger.debug("isLicenseVerificationMatching_V1_0: Entry");
		ApplicationLauncher.logger.debug("isLicenceVerificationMatching: epochTimeInSec: " + epochTimeInSec);
		ApplicationLauncher.logger.debug("isLicenceVerificationMatching: userInputSaltedKey : " + userInputSaltedKey);

		/*
		 * int year =
		 * Instant.ofEpochSecond(epochTimeInSec).atZone(ZoneId.systemDefault()).getYear(
		 * );
		 * int month =
		 * Instant.ofEpochSecond(epochTimeInSec).atZone(ZoneId.systemDefault()).getYear(
		 * );
		 * //int date =
		 * Instant.ofEpochSecond(epochTimeInSec).atZone(ZoneId.systemDefault()).getYear(
		 * );
		 * 
		 * int hour =
		 * Instant.ofEpochSecond(epochTimeInSec).atZone(ZoneId.systemDefault()).getHour(
		 * );
		 * int minutes =
		 * Instant.ofEpochSecond(epochTimeInSec).atZone(ZoneId.systemDefault()).
		 * getMinute();
		 * int seconds =
		 * Instant.ofEpochSecond(epochTimeInSec).atZone(ZoneId.systemDefault()).
		 * getSecond();
		 * 
		 * ApplicationLauncher.logger.debug("isLicenceVerificationMatching: Time : " +
		 * year );
		 */

		userInputSaltedKey = userInputSaltedKey.replaceAll("[^0-9]", "");
		ApplicationLauncher.logger
				.debug("isLicenceVerificationMatching: userInputSaltedKey with only numbers: " + userInputSaltedKey);

		Map<Boolean, String> verificationStatusHashMap = new HashMap<Boolean, String>();
		if (userInputSaltedKey.equals(ConstantApp.USER_LICENCE_SKIP_SALTED_KEY_VALIDATION_DEFAULT_VALUE)) {
			ApplicationLauncher.logger.debug("isLicenceVerificationMatching: salted default key matching");
			verificationStatusHashMap.put(true, "");
			return verificationStatusHashMap;
			// return true;
		}
		Date date = new Date(epochTimeInSec * 1000);// 1500464550423L);
		// use correct format ('S' for milliseconds)
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyMMdd");
		dateFormatter.setTimeZone(TimeZone.getTimeZone("IST"));
		// format date
		String dateFormattedData = dateFormatter.format(date);

		SimpleDateFormat timeFormatter = new SimpleDateFormat("HHmmss");
		timeFormatter.setTimeZone(TimeZone.getTimeZone("IST"));
		// format date
		String timeFormattedData = timeFormatter.format(date);

		ApplicationLauncher.logger.debug("isLicenceVerificationMatching: dateFormattedData : " + dateFormattedData);
		ApplicationLauncher.logger.debug("isLicenceVerificationMatching: timeFormattedData : " + timeFormattedData);

		// Salt key generation Algorithm
		// Step01: inputEpochTime -> A => 1676381566
		// Step02: Take first two characters of Year and Multiply with 10,000 -> B => 20
		// * 10000 = 200000
		// Step03: Decode input epochTime in to yyMMdd in IST (yy is last two char of
		// year) -> C => 230214
		// Step04: Decode input epochTime in to HHmmss in IST -> D => 190246
		// Step05: Sum B + C + D -> X (sumOfEpochFormattedData) => Total = 620460
		// Step06: Sum each Digit Of X -> E (sumOfEachDigitOfEpochFormattedData) =>
		// SumEachDigit(620460) = 18
		// Step07: Sum each Digit Of A -> F (sumOfEachDigitInputEpochTimeInSec) =>
		// SumEachDigit(1676381566) = 49
		// Step08: Sum E - F -> Y
		// (diffOfEachDigitOfEpochFormattedAndInputEpochTimeInSec) => 18 - 49 = -31
		// Step09: Sum X + Y -> Z (sumOfEpochFormattedData +
		// diffOfEachDigitOfEpochFormattedAndInputEpochTimeInSec) => 620460 + (-31) =
		// 620429
		// Step10: Z = SaltedKeyCalculatedResult (expectedSaltedValue)

		long firstTwoYearValue = 20 * 10000; // 2,00,000

		long sumOfEpochFormattedData = firstTwoYearValue + Long.parseLong(dateFormattedData)
				+ Long.parseLong(timeFormattedData);

		ApplicationLauncher.logger
				.debug("isLicenceVerificationMatching: sumOfEpochFormattedData : " + sumOfEpochFormattedData);

		int sumOfEachDigitOfEpochFormattedData = Arrays.stream(String.valueOf(sumOfEpochFormattedData).split(""))
				.mapToInt(e -> Integer.valueOf(e))
				.sum();
		ApplicationLauncher.logger.debug("isLicenceVerificationMatching: sumOfEachDigitOfEpochFormattedData : "
				+ sumOfEachDigitOfEpochFormattedData);

		/*
		 * int sumOfEachDigitInputEpochTimeInSec =
		 * Arrays.stream(String.valueOf(epochTimeInSec).split(""))
		 * .mapToInt( e -> Integer.valueOf(e))
		 * .sum();
		 * ApplicationLauncher.logger.
		 * debug("isLicenceVerificationMatching: sumOfEachDigitInputEpochTimeInSec : " +
		 * sumOfEachDigitInputEpochTimeInSec );
		 * 
		 */

		int sumOfEachDigitInputEpochTimeInSec = Stream.of(String.valueOf(epochTimeInSec).split(""))
				.mapToInt(e -> Integer.valueOf(e))
				.sum();
		ApplicationLauncher.logger.debug("isLicenceVerificationMatching: sumOfEachDigitInputEpochTimeInSec : "
				+ sumOfEachDigitInputEpochTimeInSec);

		int diffOfEachDigitOfEpochFormattedAndInputEpochTimeInSec = sumOfEachDigitOfEpochFormattedData
				- sumOfEachDigitInputEpochTimeInSec;

		ApplicationLauncher.logger
				.debug("isLicenceVerificationMatching: diffOfEachDigitOfEpochFormattedAndInputEpochTimeInSec : "
						+ diffOfEachDigitOfEpochFormattedAndInputEpochTimeInSec);

		// String expectedSaltedValue = String.valueOf(sumOfEpochFormattedData) + "-"
		// +String.valueOf(diffOfEachDigitOfEpochFormattedAndInputEpochTimeInSec);
		long expectedSaltedValue = sumOfEpochFormattedData + diffOfEachDigitOfEpochFormattedAndInputEpochTimeInSec;
		ApplicationLauncher.logger.debug("isLicenceVerificationMatching: expectedSaltedValue : " + expectedSaltedValue);
		ApplicationLauncher.logger.debug("isLicenceVerificationMatching: userInputSaltedKey : " + userInputSaltedKey);
		if (userInputSaltedKey.equals(String.valueOf(expectedSaltedValue))) {
			ApplicationLauncher.logger.debug("isLicenceVerificationMatching: salted key matching");
			verificationStatusHashMap.put(true, "");
			return verificationStatusHashMap;
			// return true;
		} else {
			ApplicationLauncher.logger.debug("isLicenceVerificationMatching: salted key not matching");
			// return false;
			// int randomValue1 = ThreadLocalRandom.current().nextInt(5000, 8000);
			int randomValue2 = ThreadLocalRandom.current().nextInt(9000, 10000);
			String errorCode = "0x" + Long.toHexString(expectedSaltedValue).toUpperCase() + "BADC"
					+ Integer.toHexString(randomValue2).toUpperCase();
			ApplicationLauncher.logger.debug("isLicenceVerificationMatching: errorCode: " + errorCode);
			verificationStatusHashMap.put(false, errorCode);
			return verificationStatusHashMap;

		}
	}

	public static String textAreaInputDialogDisplay(String header, String title, String outputFolderPathWithFileName,
			String outputFolderPath) {
		ApplicationLauncher.logger.debug("textAreaInputDialogDisplay: Entry ");
		// Platform.runLater(() -> {

		// TextAreaInputDialog dialog = new TextAreaInputDialog();
		String userInputData = "";
		TextAreaInputDialog dialog = new TextAreaInputDialog(outputFolderPathWithFileName);

		dialog.setHeaderText(header);// "Enter ParamProfile Name");
		dialog.setTitle(title);// "ParamProfile");
		dialog.setGraphic(null);
		dialog.getEditor().setEditable(false);
		dialog.getEditor().setWrapText(true);
		// dialog.setHeight(arg0);
		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("file:images/" + ConstantVersion.APP_ICON_FILENAME));
		// dialog.eee
		// Show the dialog and capture the result.
		Optional result = dialog.showAndWait();

		// If the "Okay" button was clicked, the result will contain our String in the
		// get() method
		if (result.isPresent()) {
			// System.out.println(result.get());

			ApplicationLauncher.logger.debug("textFieldInputDialogDisplay: result.get(): " + result.get());
			userInputData = (String) result.get();
			// ref_cmbBxOperationParamProfileName.getItems().add(result.get());
			// ref_cmbBxOperationParamProfileName.getSelectionModel().select(result.get());
			// ref_tvOperationParamProfile.getItems().clear();
			// getSerialNo().set(1);
		}
		return userInputData;
		// });
	}

	public static String asciiToHex(String asciiString) {
		// ApplicationLauncher.logger.debug("asciiToHex: Entry: " );
		StringBuilder hexString = new StringBuilder();
		String hex = "";
		for (int i = 0; i < asciiString.length(); i++) {
			char c = asciiString.charAt(i);
			// String hex = Integer.toHexString((int) c);
			hex = String.format("%02X", ((int) c));
			// ApplicationLauncher.logger.debug("asciiToHex: hex: " + hex);
			hexString.append(hex);
		}
		return hexString.toString();
	}

	public static String bytesToHex(byte[] bytes) {
		// char[] hexArray = "0123456789ABCDEF".toCharArray();
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}

		return new String(hexChars);
	}

	public static String bytesToAscii(byte[] bytes) {
		String convertedHex = bytesToHex(bytes);
		String convertedAscii = hexToAsciiV2(convertedHex);
		return convertedAscii;
	}

	public String getPalletNoFromPalletDistinctId(String palletDistinctId) {

		String palletNo = "";
		try {
			if (!palletDistinctId.isEmpty()) {
				palletNo = palletDistinctId.substring(palletDistinctId.lastIndexOf('_') + 1);
				ApplicationLauncher.logger.debug("getPalletNoFromPalletDistinctId : palletDistinctId: "
						+ palletDistinctId + " -> palletNo:" + palletNo);
			}
		} catch (Exception e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("getPalletNoFromPalletDistinctId : Exception:" + e.getMessage());
		}

		return palletNo;
	}
}