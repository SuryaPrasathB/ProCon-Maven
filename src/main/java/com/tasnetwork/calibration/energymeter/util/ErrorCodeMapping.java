package com.tasnetwork.calibration.energymeter.util;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.TimerTask;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

import javafx.fxml.Initializable;

public class ErrorCodeMapping implements Initializable {

	public static JSONObject ERROR_CODE_MSG = new JSONObject();

	public static final String ERROR_CODE_004 = "Error_Code_004";
	public static final String ERROR_CODE_004_MSG = "Power Source no response received";

	public static final String ERROR_CODE_100 = "Error_Code_100";
	public static final String ERROR_CODE_100_MSG = "StabilityValidation:PowerSource: Unable to set the Parameter on Power Source";

	public static final String ERROR_CODE_100A = "Error_Code_100A";
	public static final String ERROR_CODE_100A_MSG = "StabilityValidation:PowerSource: Unable to set the Parameter on Power Source";

	public static final String ERROR_CODE_100B = "Error_Code_100B";
	public static final String ERROR_CODE_100B_MSG = "StabilityValidation:PowerSource: Unable to set the Parameter on Power Source";

	public static final String ERROR_CODE_101 = "Error_Code_101";
	public static final String ERROR_CODE_101_MSG = " StabilityValidation:PowerSource: Unable to set RY Phase angle during Phase Reverse sequence Reset";

	public static final String ERROR_CODE_102 = "Error_Code_102";
	public static final String ERROR_CODE_102_MSG = "StabilityValidation:PowerSource: Unable to set RB Phase angle during Phase Reverse sequence Reset";

	public static final String ERROR_CODE_103 = "Error_Code_103";
	public static final String ERROR_CODE_103_MSG = "StabilityValidation:PowerSource: Failed on SET Command";

	public static final String ERROR_CODE_103A = "Error_Code_103A";
	public static final String ERROR_CODE_103A_MSG = "StabilityValidation:PowerSource: Failed on SET Command for single phase";

	public static final String ERROR_CODE_104A = "Error_Code_104A";
	public static final String ERROR_CODE_104A_MSG = "StabilityValidation:PowerSource: Failed on Frequency SET Command";

	public static final String ERROR_CODE_104B = "Error_Code_104B";
	public static final String ERROR_CODE_104B_MSG = "StabilityValidation:PowerSource: Single Phase Failed on Frequency SET Command";

	public static final String ERROR_CODE_104C = "Error_Code_104C";
	public static final String ERROR_CODE_104C_MSG = "StabilityValidation:PowerSource: Phase init acknowledgement failed";

	public static final String ERROR_CODE_105 = "Error_Code_105";
	public static final String ERROR_CODE_105_MSG = "Timeout :StabilityValidation:PowerSource: Timeout";

	public static final String ERROR_CODE_106 = "Error_Code_106";
	public static final String ERROR_CODE_106_MSG = "StabilityValidation:PowerSource: Failed while setting RB phase sequence";

	public static final String ERROR_CODE_106A = "Error_Code_106A";
	public static final String ERROR_CODE_106A_MSG = "StabilityValidation:PowerSource: Failed while setting RY phase sequence";

	public static final String ERROR_CODE_111 = "Error_Code_111";
	public static final String ERROR_CODE_111_MSG = "R Phase Voltage: StabilityValidation:PowerSource: Failed while setting R Phase Voltage";

	public static final String ERROR_CODE_111A = "Error_Code_111A";
	public static final String ERROR_CODE_111A_MSG = "R Phase Voltage: StabilityValidation:PowerSource: Single Phase: Failed while setting R Phase Voltage";

	public static final String ERROR_CODE_112 = "Error_Code_112";
	public static final String ERROR_CODE_112_MSG = "R Phase Current: StabilityValidation:PowerSource: Failed while setting R Phase Current";

	public static final String ERROR_CODE_112A = "Error_Code_112A";
	public static final String ERROR_CODE_112A_MSG = "R Phase Current: StabilityValidation:PowerSource:Single Phase: Failed while setting R Phase Current";

	public static final String ERROR_CODE_113 = "Error_Code_113";
	public static final String ERROR_CODE_113_MSG = "R Phase Degree: StabilityValidation:PowerSource: Failed while setting R Phase Degree";

	public static final String ERROR_CODE_1XX = "Error_Code_1XX";
	public static final String ERROR_CODE_1XX_MSG = "StabilityValidation:PowerSource: Failed on PhaseRevPowerOn set value";

	public static final String ERROR_CODE_1XXA = "Error_Code_1XXA";
	public static final String ERROR_CODE_1XXA_MSG = "StabilityValidation:PowerSource: Failed on PhaseRevPowerOn set value for single phase";

	public static final String ERROR_CODE_121 = "Error_Code_121";
	public static final String ERROR_CODE_121_MSG = "Y Phase Voltage: StabilityValidation:PowerSource: Failed while setting Y Phase Voltage";

	public static final String ERROR_CODE_122 = "Error_Code_122";
	public static final String ERROR_CODE_122_MSG = "Y Phase Current: StabilityValidation:PowerSource: Failed while setting Y Phase Current";

	public static final String ERROR_CODE_123 = "Error_Code_123";
	public static final String ERROR_CODE_123_MSG = "Y Phase Degree: StabilityValidation:PowerSource: Failed while setting Y Phase Degree";

	public static final String ERROR_CODE_131 = "Error_Code_131";
	public static final String ERROR_CODE_131_MSG = "B Phase Voltage: StabilityValidation:PowerSource: Failed while setting B Phase Voltage";

	public static final String ERROR_CODE_132 = "Error_Code_132";
	public static final String ERROR_CODE_132_MSG = "B Phase Current: StabilityValidation:PowerSource: Failed while setting B Phase Current";

	public static final String ERROR_CODE_133 = "Error_Code_133";
	public static final String ERROR_CODE_133_MSG = "B Phase Degree: StabilityValidation:PowerSource: Failed while setting B Phase Degree";

	public static final String ERROR_CODE_134 = "Error_Code_134";
	public static final String ERROR_CODE_134_MSG = "Power source : Calibration - Voltage mode set failed";

	public static final String ERROR_CODE_211 = "Error_Code_211";
	public static final String ERROR_CODE_211_MSG = "R Phase Voltage : StabilityValidation:RefStd: Feedback parameter from Reference Standard failed";

	public static final String ERROR_CODE_212 = "Error_Code_212";
	public static final String ERROR_CODE_212_MSG = "R Phase Current :StabilityValidation:RefStd: Feedback parameter from Reference Standard failed";

	public static final String ERROR_CODE_213 = "Error_Code_213";
	public static final String ERROR_CODE_213_MSG = "R Phase Degree :StabilityValidation:RefStd: Feedback parameter from Reference Standard failed";

	public static final String ERROR_CODE_214 = "Error_Code_214";
	public static final String ERROR_CODE_214_MSG = "Phase Frequency :StabilityValidation:RefStd: Feedback parameter from Reference Standard failed";

	public static final String ERROR_CODE_221 = "Error_Code_221";
	public static final String ERROR_CODE_221_MSG = "Y Phase Voltage :StabilityValidation:RefStd: Feedback parameter from Reference Standard failed";

	public static final String ERROR_CODE_222 = "Error_Code_222";
	public static final String ERROR_CODE_222_MSG = "Y Phase Current :StabilityValidation:RefStd: Feedback parameter from Reference Standard failed";

	public static final String ERROR_CODE_223 = "Error_Code_223";
	public static final String ERROR_CODE_223_MSG = "Y Phase Degree :StabilityValidation:RefStd: Feedback parameter from Reference Standard failed";

	public static final String ERROR_CODE_224 = "Error_Code_224";
	public static final String ERROR_CODE_224_MSG = "Phase Frequency :StabilityValidation:RefStd: Feedback parameter from Reference Standard failed";

	public static final String ERROR_CODE_231 = "Error_Code_231";
	public static final String ERROR_CODE_231_MSG = "B Phase Voltage :StabilityValidation:RefStd: Feedback parameter from Reference Standard failed";

	public static final String ERROR_CODE_232 = "Error_Code_232";
	public static final String ERROR_CODE_232_MSG = "B Phase Current :StabilityValidation:RefStd: Feedback parameter from Reference Standard failed";

	public static final String ERROR_CODE_233 = "Error_Code_233";
	public static final String ERROR_CODE_233_MSG = "B Phase Degree :StabilityValidation:RefStd: Feedback parameter from Reference Standard failed";

	public static final String ERROR_CODE_234 = "Error_Code_234";
	public static final String ERROR_CODE_234_MSG = "Phase Frequency :StabilityValidation:RefStd: Feedback parameter from Reference Standard failed";

	public static final String ERROR_CODE_301 = "Error_Code_301";
	public static final String ERROR_CODE_301_MSG = "Not Executed: Due to continuous Power source or Ref standard failure";

	public static final String ERROR_CODE_1001 = "Error_Code_1001";
	public static final String ERROR_CODE_1001_MSG = "Off Failed :StabilityValidation:PowerSource: Unable to turn off the power through command";

	public static final String ERROR_CODE_1002 = "Error_Code_1002";
	public static final String ERROR_CODE_1002_MSG = "Power source : Unable to MCT-NCT mode through command";

	public static final String ERROR_CODE_1003 = "Error_Code_1003";
	public static final String ERROR_CODE_1003_MSG = "Power source : Unable to set feedback control-phase angle adjustment through command";

	public static final String ERROR_CODE_1004 = "Error_Code_1004";
	public static final String ERROR_CODE_1004_MSG = "Power source : Unable to set feedback control-voltage adjustment through command";

	public static final String ERROR_CODE_1005 = "Error_Code_1005";
	public static final String ERROR_CODE_1005_MSG = "Power source : Unable to set feedback control-current adjustment through command";

	public static final String ERROR_CODE_2041 = "Error_Code_2041";
	public static final String ERROR_CODE_2041_MSG = "Phase Frequency :StabilityValidation:RefStd: Feedback parameter from Reference Standard failed";

	public static final String ERROR_CODE_2040 = "Error_Code_2040";
	public static final String ERROR_CODE_2040_MSG = "Phase Frequency :StabilityValidation:RefStd: Feedback parameter from Reference Standard failed";

	public static final String ERROR_CODE_2042 = "Error_Code_2042";
	public static final String ERROR_CODE_2042_MSG = "Phase Frequency :StabilityValidation:RefStd: Feedback parameter from Reference Standard failed";

	public static final String ERROR_CODE_3000 = "Error_Code_3000";
	public static final String ERROR_CODE_3000_MSG = "User aborted";

	public static final String ERROR_CODE_3001 = "Error_Code_3001";
	public static final String ERROR_CODE_3001_MSG = "Error loading the library files";

	public static final String ERROR_CODE_3002 = "Error_Code_3002";
	public static final String ERROR_CODE_3002_MSG = "License expired. Kindly contact supplier to renew licence!!\n\nif license already procured kindly click yes to continue";

	public static final String ERROR_CODE_3003 = "Error_Code_3003";
	public static final String ERROR_CODE_3003_MSG = "License corrupted. Kindly click <Yes> to proceed with license registration or contact supplier!!";

	public static final String ERROR_CODE_5001 = "Error_Code_5001";
	public static final String ERROR_CODE_5001_MSG = "Black listed meter id found";

	public static final String ERROR_CODE_5002 = "Error_Code_5002";
	public static final String ERROR_CODE_5002_MSG = "Meter Id already tested";

	public static final String ERROR_CODE_5003 = "Error_Code_5003";
	public static final String ERROR_CODE_5003_MSG = "Empty Meter id found";

	public static final String ERROR_CODE_5004 = "Error_Code_5004";
	public static final String ERROR_CODE_5004_MSG = "Unknown Error during execution";

	public static final String ERROR_CODE_900 = "Error_Code_900";
	public static final String ERROR_CODE_900_MSG = "Unable to access Power Source Serial Port. Kindly check the setting and configure appropriately";

	public static final String ERROR_CODE_901 = "Error_Code_901";
	public static final String ERROR_CODE_901_MSG = "Unable to access RefStd Serial Port. Kindly check the setting and configure appropriately";

	public static final String ERROR_CODE_902 = "Error_Code_902";
	public static final String ERROR_CODE_902_MSG = "Unable to access DUT Serial Port. Kindly check the setting and configure appropriately";

	public static final String ERROR_CODE_6000 = "Error_Code_6000";
	public static final String ERROR_CODE_6000_MSG = "Cell Start Data input contains unaccepted value , kindly rephrase!!";

	public static final String ERROR_CODE_6001 = "Error_Code_6001";
	public static final String ERROR_CODE_6001_MSG = "TestFilter Cell Header Data input contains unaccepted value , kindly rephrase!!";

	public static final String ERROR_CODE_6002 = "Error_Code_6002";
	public static final String ERROR_CODE_6002_MSG = "MeterMetaData Cell Start Data input contains unaccepted value , kindly rephrase!!";

	public static final String ERROR_CODE_6003 = "Error_Code_6003";
	public static final String ERROR_CODE_6003_MSG = "MeterMetaData Cell Header Data input contains unaccepted value , kindly rephrase!!";

	public static final String ERROR_CODE_6004 = "Error_Code_6004";
	public static final String ERROR_CODE_6004_MSG = "No Check box selected\n\nAtleast one check box should be selected for population of data.\n\nKindly select and try again!";

	public static final String ERROR_CODE_6005 = "Error_Code_6005";
	public static final String ERROR_CODE_6005_MSG = "Same Output selected\n\n<Output Data> and <Compared Result status> selection output are same. Kindly ensure different outputs are selected\n\nSelected output: ";

	public static final String ERROR_CODE_6006 = "Error_Code_6006";
	public static final String ERROR_CODE_6006_MSG = "Input Process list table is empty or insufficient data to process operation.\n\nKindly add the input process list and try again!";

	public static final String ERROR_CODE_6007 = "Error_Code_6007";
	public static final String ERROR_CODE_6007_MSG = "Same value entered on <Allowed Upper Limit> and <Allowed Lower Limit> field.\n\nKindly enter different value and try again!";

	public static final String ERROR_CODE_6008 = "Error_Code_6008";
	public static final String ERROR_CODE_6008_MSG = "<Allowed Lower Limit> field is empty.\n\nKindly enter valid value and try again!";

	public static final String ERROR_CODE_6009 = "Error_Code_6009";
	public static final String ERROR_CODE_6009_MSG = "<Allowed Upper Limit> field is empty.\n\nKindly enter valid value and try again!";

	public static final String ERROR_CODE_6010 = "Error_Code_6010";
	public static final String ERROR_CODE_6010_MSG = "Input Process list table is empty or insufficient data to process operation.\n\nKindly add the input process list and try again!";

	public static final String ERROR_CODE_6011 = "Error_Code_6011";
	public static final String ERROR_CODE_6011_MSG = "Both <Operation> and <Master Output> fields are invalid.\n\nKindly select valid input and try again!";

	public static final String ERROR_CODE_6012 = "Error_Code_6012";
	public static final String ERROR_CODE_6012_MSG = "Input Process list table is empty or insufficient data to process operation.\n\nKindly add the input process list and try again!";

	public static final String ERROR_CODE_6013 = "Error_Code_6013";
	public static final String ERROR_CODE_6013_MSG = "Test filter: Cell Starting position field is empty.\nKindly update data and try again!\n\nEmpty field : ";

	public static final String ERROR_CODE_6014 = "Error_Code_6014";
	public static final String ERROR_CODE_6014_MSG = "Test filter: Cell Header position field is empty.\nKindly update data and try again!\n\nEmpty field : ";

	public static final String ERROR_CODE_6015 = "Error_Code_6015";
	public static final String ERROR_CODE_6015_MSG = "Test filter: Duplicate Cell data found in Cell Start position.\nKindly update data and try again!";

	public static final String ERROR_CODE_6016 = "Error_Code_6016";
	public static final String ERROR_CODE_6016_MSG = "Test filter: Duplicate Cell data found in Cell Header position.\nKindly update data and try again!";

	public static final String ERROR_CODE_6017 = "Error_Code_6017";
	public static final String ERROR_CODE_6017_MSG = "Meter meta data: Cell Starting position field is empty.\nKindly update data and try again!\n\nEmpty field : ";

	public static final String ERROR_CODE_6018 = "Error_Code_6018";
	public static final String ERROR_CODE_6018_MSG = "Meter meta data: Cell Header position field is empty.\nKindly update data and try again!\n\nEmpty field : ";

	public static final String ERROR_CODE_6019 = "Error_Code_6019";
	public static final String ERROR_CODE_6019_MSG = "Meter meta data: Duplicate Cell data found in Cell Start position.\nKindly update data and try again!";

	public static final String ERROR_CODE_6020 = "Error_Code_6020";
	public static final String ERROR_CODE_6020_MSG = "Meter meta data: Duplicate Cell data found in Cell Header position.\nKindly update data and try again!";

	public static final String ERROR_CODE_6021 = "Error_Code_6021";
	public static final String ERROR_CODE_6021_MSG = "Entered <Allowed Upper Limit> value is lesser than <Allowed Lower Limit> value.\n\nKindly enter different value and try again!";

	public static final String ERROR_CODE_6022 = "Error_Code_6022";
	public static final String ERROR_CODE_6022_MSG = "Meter meta data: Duplicate Cell data found in Start Cell and Header Cell position.\nKindly update data and try again!";

	public static final String ERROR_CODE_6023 = "Error_Code_6023";
	public static final String ERROR_CODE_6023_MSG = "Test filter data: Duplicate Cell data found in Start Cell and Header Cell position.\nKindly update data and try again!";

	public static final String ERROR_CODE_6024 = "Error_Code_6024";
	public static final String ERROR_CODE_6024_MSG = "Test filter data: <Custom Test Name> field is empty\nKindly update data and try again!";

	public static final String ERROR_CODE_6025 = "Error_Code_6025";
	public static final String ERROR_CODE_6025_MSG = "Test filter data: <Post Operation value> field is empty\nKindly update data and try again!";

	public static final String ERROR_CODE_6026 = "Error_Code_6026";
	public static final String ERROR_CODE_6026_MSG = "Test filter data: <Input Process List> contains same <Master Output> selected\nKindly update data and try again!";

	public static final String ERROR_CODE_6027 = "Error_Code_6027";
	public static final String ERROR_CODE_6027_MSG = "Test filter data: <Input Process List> contains same <Local Output Data> selected\nKindly update data and try again!";

	public static final String ERROR_CODE_6028 = "Error_Code_6028";
	public static final String ERROR_CODE_6028_MSG = "Meter meta data: Page Name is empty.\nKindly update data and try again!";

	public static final String ERROR_CODE_6029 = "Error_Code_6029";
	public static final String ERROR_CODE_6029_MSG = "Meter meta data: Meta Data - Items not selected\n Kindly select an item and try again!";

	public static final String ERROR_CODE_6030 = "Error_Code_6030";
	public static final String ERROR_CODE_6030_MSG = "Test Filter data: - Items not selected\n Kindly select an item and try again!";

	public static final String ERROR_CODE_6031 = "Error_Code_6031";
	public static final String ERROR_CODE_6031_MSG = "Test filter: Test Filter name already exist.\nKindly update data and try again!\n\nFilter Name value: ";

	public static final String ERROR_CODE_6032 = "Error_Code_6032";
	public static final String ERROR_CODE_6032_MSG = "Test filter : Test Type Data,  Voltage percentage is not in acceptable limit\nKindly update data and try again!";

	public static final String ERROR_CODE_6033 = "Error_Code_6033";
	public static final String ERROR_CODE_6033_MSG = "Test filter : Test Type Data,  Voltage percentage field is empty\nKindly update data and try again!";

	public static final String ERROR_CODE_6034 = "Error_Code_6034";
	public static final String ERROR_CODE_6034_MSG = "Test filter : Test Type Data,  Current percentage for Ib is not in acceptable limit\nKindly update data and try again!";

	public static final String ERROR_CODE_6035 = "Error_Code_6035";
	public static final String ERROR_CODE_6035_MSG = "Test filter : Test Type Data,  Current percentage for Imax is not in acceptable limit\nKindly update data and try again!";

	public static final String ERROR_CODE_6036 = "Error_Code_6036";
	public static final String ERROR_CODE_6036_MSG = "Test filter : Test Type Data,  Current percentage field is empty\nKindly update data and try again!";

	public static final String ERROR_CODE_6037 = "Error_Code_6037";
	public static final String ERROR_CODE_6037_MSG = "Test filter : Test Type Data,  PF value is not in acceptable limit\nKindly update data and try again!";

	public static final String ERROR_CODE_6038 = "Error_Code_6038";
	public static final String ERROR_CODE_6038_MSG = "Test filter : Test Type Data,  PF value field is empty\nKindly update data and try again!";

	public static final String ERROR_CODE_6039 = "Error_Code_6039";
	public static final String ERROR_CODE_6039_MSG = "Test filter : Test Type Data,  Frequency value is not in acceptable limit\nKindly update data and try again!";

	public static final String ERROR_CODE_6040 = "Error_Code_6040";
	public static final String ERROR_CODE_6040_MSG = "Test filter : Test Type Data,  Frequency value field is empty\nKindly update data and try again!";

	public static final String ERROR_CODE_6041 = "Error_Code_6041";
	public static final String ERROR_CODE_6041_MSG = "Test filter : Test Type Data,  Iteration starting reading value is not in acceptable limit\nKindly update data and try again!";

	public static final String ERROR_CODE_6042 = "Error_Code_6042";
	public static final String ERROR_CODE_6042_MSG = "Test filter : Test Type Data,  Iteration starting reading value field is empty\nKindly update data and try again!";

	public static final String ERROR_CODE_6043 = "Error_Code_6043";
	public static final String ERROR_CODE_6043_MSG = "Test filter : Test Type Data,  Active Energy value is not in acceptable limit\nKindly update data and try again!";

	public static final String ERROR_CODE_6044 = "Error_Code_6044";
	public static final String ERROR_CODE_6044_MSG = "Test filter : Test Type Data,  Reactive Energy value is not in acceptable limit\nKindly update data and try again!";

	public static final String ERROR_CODE_6045 = "Error_Code_6045";
	public static final String ERROR_CODE_6045_MSG = "Test filter : Test Type Data,  Apparent Energy value is not in acceptable limit\nKindly update data and try again!";

	public static final String ERROR_CODE_6046 = "Error_Code_6046";
	public static final String ERROR_CODE_6046_MSG = "Test filter : Test Type Data,  Energy value field is empty\nKindly update data and try again!";

	public static final String ERROR_CODE_6047 = "Error_Code_6047";
	public static final String ERROR_CODE_6047_MSG = "Test filter data: For Post processing division method,  <Post Operation input> value should not be zero\n\nKindly update data and try again!";

	public static final String ERROR_CODE_6048 = "Error_Code_6048";
	public static final String ERROR_CODE_6048_MSG = "Test filter : Test Type Data,  Iteration ending reading value is not in acceptable limit\nKindly update data and try again!";

	public static final String ERROR_CODE_6049 = "Error_Code_6049";
	public static final String ERROR_CODE_6049_MSG = "Test filter : Test Type Data,  Iteration starting reading value field is greater than ending reading value\nKindly update data and try again!";

	public static final String ERROR_CODE_6050 = "Error_Code_6050";
	public static final String ERROR_CODE_6050_MSG = "Test filter: Test Filter name is empty.\nKindly update data and try again!";

	public static final String ERROR_CODE_7001 = "Error_Code_7001";
	public static final String ERROR_CODE_7001_MSG = "Voltage Percentage is lesser than minimum acceptable limit\n\nKindly update project data setup or configuration and try again!";

	public static final String ERROR_CODE_7002 = "Error_Code_7002";
	public static final String ERROR_CODE_7002_MSG = "Voltage Percentage is greater than maximum acceptable limit\n\nKindly update project data setup or configuration and try again!";

	public static final String ERROR_CODE_7003 = "Error_Code_7003";
	public static final String ERROR_CODE_7003_MSG = "Voltage target value is lesser than minimum acceptable limit\n\nKindly update project data setup or configuration and try again!";

	public static final String ERROR_CODE_7004 = "Error_Code_7004";
	public static final String ERROR_CODE_7004_MSG = "Voltage target value is greater than maximum acceptable limit\n\nKindly update project data setup or configuration and try again!";

	public static final String ERROR_CODE_7005 = "Error_Code_7005";
	public static final String ERROR_CODE_7005_MSG = "R Phase Voltage target value is lesser than minimum acceptable limit\n\nKindly update project data setup or configuration and try again!";

	public static final String ERROR_CODE_7006 = "Error_Code_7006";
	public static final String ERROR_CODE_7006_MSG = "R Phase Voltage target value is greater than maximum acceptable limit\n\nKindly update project data setup or configuration and try again!";

	public static final String ERROR_CODE_7007 = "Error_Code_7007";
	public static final String ERROR_CODE_7007_MSG = "B Phase Voltage target value is lesser than minimum acceptable limit\n\nKindly update project data setup or configuration and try again!";

	public static final String ERROR_CODE_7008 = "Error_Code_7008";
	public static final String ERROR_CODE_7008_MSG = "B Phase Voltage target value is greater than maximum acceptable limit\n\nKindly update project data setup or configuration and try again!";

	public static final String ERROR_CODE_7009 = "Error_Code_7009";
	public static final String ERROR_CODE_7009_MSG = "Y Phase Voltage target value is lesser than minimum acceptable limit\n\nKindly update project data setup or configuration and try again!";

	public static final String ERROR_CODE_7010 = "Error_Code_7010";
	public static final String ERROR_CODE_7010_MSG = "Y Phase Voltage target value is greater than maximum acceptable limit\n\nKindly update project data setup or configuration and try again!";

	public static final String ERROR_CODE_7011 = "Error_Code_7011";
	public static final String ERROR_CODE_7011_MSG = "B Phase Voltage target value is greater than maximum acceptable limit of combination of Vrated and Percentage\n\nKindly update project data setup or configuration and try again!";

	public static final String ERROR_CODE_7012 = "Error_Code_7012";
	public static final String ERROR_CODE_7012_MSG = "Y Phase Voltage target value is greater than maximum acceptable limit of combination of Vrated and Percentage\n\nKindly update project data setup or configuration and try again!";

	public static final String ERROR_CODE_7013 = "Error_Code_7013";
	public static final String ERROR_CODE_7013_MSG = "R Phase Voltage target value is greater than maximum acceptable limit of combination of Vrated and Percentage\n\nKindly update project data setup or configuration and try again!";

	public static final String ERROR_CODE_7101 = "Error_Code_7101";
	public static final String ERROR_CODE_7101_MSG = "Current Ib Percentage is greater than maximum acceptable limit\n\nKindly update project data setup or configuration and try again!";

	public static final String ERROR_CODE_7102 = "Error_Code_7102";
	public static final String ERROR_CODE_7102_MSG = "Current Ib target value is lesser than minimum acceptable limit\n\nKindly update project data setup or configuration and try again!";

	public static final String ERROR_CODE_7103 = "Error_Code_7103";
	public static final String ERROR_CODE_7103_MSG = "Current Ib target value is greater than maximum acceptable limit\n\nKindly update project data setup or configuration and try again!";

	public static final String ERROR_CODE_7104 = "Error_Code_7104";
	public static final String ERROR_CODE_7104_MSG = "Current Imax Percentage is greater than maximum acceptable limit\n\nKindly update project data setup or configuration and try again!";

	public static final String ERROR_CODE_7105 = "Error_Code_7105";
	public static final String ERROR_CODE_7105_MSG = "Current Imax target value is lesser than minimum acceptable limit\n\nKindly update project data setup or configuration and try again!";

	public static final String ERROR_CODE_7106 = "Error_Code_7106";
	public static final String ERROR_CODE_7106_MSG = "Current Imax target value is greater than maximum acceptable limit\n\nKindly update project data setup or configuration and try again!";

	public static final String ERROR_CODE_7107 = "Error_Code_7107";
	public static final String ERROR_CODE_7107_MSG = "R Phase Current target value is lesser than minimum acceptable limit\n\nKindly update project data setup or configuration and try again!";

	public static final String ERROR_CODE_7108 = "Error_Code_7108";
	public static final String ERROR_CODE_7108_MSG = "R Phase Current target value is greater than maximum acceptable limit\n\nKindly update project data setup or configuration and try again!";

	public static final String ERROR_CODE_7109 = "Error_Code_7109";
	public static final String ERROR_CODE_7109_MSG = "B Phase Current target value is lesser than minimum acceptable limit\n\nKindly update project data setup or configuration and try again!";

	public static final String ERROR_CODE_7110 = "Error_Code_7110";
	public static final String ERROR_CODE_7110_MSG = "B Phase Current target value is greater than maximum acceptable limit\n\nKindly update project data setup or configuration and try again!";

	public static final String ERROR_CODE_7111 = "Error_Code_7111";
	public static final String ERROR_CODE_7111_MSG = "Y Phase Current target value is lesser than minimum acceptable limit\n\nKindly update project data setup or configuration and try again!";

	public static final String ERROR_CODE_7112 = "Error_Code_7112";
	public static final String ERROR_CODE_7112_MSG = "Y Phase Current target value is greater than maximum acceptable limit\n\nKindly update project data setup or configuration and try again!";

	public static final String ERROR_CODE_7113 = "Error_Code_7113";
	public static final String ERROR_CODE_7113_MSG = "B Phase Current target value is greater than maximum acceptable limit of combination of DUT Imax and Percentage\n\nKindly update project data setup or configuration and try again!";

	public static final String ERROR_CODE_7114 = "Error_Code_7114";
	public static final String ERROR_CODE_7114_MSG = "Y Phase Current target value is greater than maximum acceptable limit of combination of DUT Imax and Percentage\n\nKindly update project data setup or configuration and try again!";

	public static final String ERROR_CODE_7115 = "Error_Code_7115";
	public static final String ERROR_CODE_7115_MSG = "R Phase Current target value is greater than maximum acceptable limit of combination of DUT Imax and Percentage\n\nKindly update project data setup or configuration and try again!";

	public static void Error_Msg() {
		try {
			ERROR_CODE_MSG = new JSONObject();

			ERROR_CODE_MSG.append(ERROR_CODE_004, ERROR_CODE_004_MSG);

			ERROR_CODE_MSG.append(ERROR_CODE_105, ERROR_CODE_105_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_102, ERROR_CODE_102_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_101, ERROR_CODE_101_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_1001, ERROR_CODE_1001_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_103, ERROR_CODE_103_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_1XX, ERROR_CODE_1XX_MSG);

			ERROR_CODE_MSG.append(ERROR_CODE_131, ERROR_CODE_131_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_132, ERROR_CODE_132_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_133, ERROR_CODE_133_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_134, ERROR_CODE_134_MSG);

			ERROR_CODE_MSG.append(ERROR_CODE_123, ERROR_CODE_123_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_122, ERROR_CODE_122_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_121, ERROR_CODE_121_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_113, ERROR_CODE_113_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_112, ERROR_CODE_112_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_111, ERROR_CODE_111_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_104A, ERROR_CODE_104A_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_106, ERROR_CODE_106_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_106A, ERROR_CODE_106A_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_100, ERROR_CODE_100_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_100A, ERROR_CODE_100A_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_100B, ERROR_CODE_100B_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_213, ERROR_CODE_213_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_212, ERROR_CODE_212_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_211, ERROR_CODE_211_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_223, ERROR_CODE_223_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_222, ERROR_CODE_222_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_221, ERROR_CODE_221_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_233, ERROR_CODE_233_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_232, ERROR_CODE_232_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_231, ERROR_CODE_231_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_214, ERROR_CODE_214_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_224, ERROR_CODE_224_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_234, ERROR_CODE_234_MSG);

			ERROR_CODE_MSG.append(ERROR_CODE_900, ERROR_CODE_900_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_901, ERROR_CODE_901_MSG);

			ERROR_CODE_MSG.append(ERROR_CODE_2041, ERROR_CODE_2041_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_2040, ERROR_CODE_2040_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_2042, ERROR_CODE_2042_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_301, ERROR_CODE_301_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_3000, ERROR_CODE_3000_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_103A, ERROR_CODE_103A_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_1XXA, ERROR_CODE_1XXA_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_111A, ERROR_CODE_111A_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_112A, ERROR_CODE_112A_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_104B, ERROR_CODE_104B_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_104C, ERROR_CODE_104C_MSG);

			ERROR_CODE_MSG.append(ERROR_CODE_1002, ERROR_CODE_1002_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_1003, ERROR_CODE_1003_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_1004, ERROR_CODE_1004_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_1005, ERROR_CODE_1005_MSG);

			ERROR_CODE_MSG.append(ERROR_CODE_3001, ERROR_CODE_3001_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_3002, ERROR_CODE_3002_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_3003, ERROR_CODE_3003_MSG);

			ERROR_CODE_MSG.append(ERROR_CODE_5001, ERROR_CODE_5001_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_5002, ERROR_CODE_5002_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_5003, ERROR_CODE_5003_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_5004, ERROR_CODE_5004_MSG);

			ERROR_CODE_MSG.append(ERROR_CODE_6000, ERROR_CODE_6000_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6001, ERROR_CODE_6001_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6002, ERROR_CODE_6002_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6003, ERROR_CODE_6003_MSG);

			ERROR_CODE_MSG.append(ERROR_CODE_6004, ERROR_CODE_6004_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6005, ERROR_CODE_6005_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6006, ERROR_CODE_6006_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6007, ERROR_CODE_6007_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6008, ERROR_CODE_6008_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6009, ERROR_CODE_6009_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6010, ERROR_CODE_6010_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6011, ERROR_CODE_6011_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6012, ERROR_CODE_6012_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6013, ERROR_CODE_6013_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6014, ERROR_CODE_6014_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6015, ERROR_CODE_6015_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6016, ERROR_CODE_6016_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6017, ERROR_CODE_6017_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6018, ERROR_CODE_6018_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6019, ERROR_CODE_6019_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6020, ERROR_CODE_6020_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6021, ERROR_CODE_6021_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6022, ERROR_CODE_6022_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6023, ERROR_CODE_6023_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6024, ERROR_CODE_6024_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6025, ERROR_CODE_6025_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6026, ERROR_CODE_6026_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6027, ERROR_CODE_6027_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6028, ERROR_CODE_6028_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6029, ERROR_CODE_6029_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6030, ERROR_CODE_6030_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6031, ERROR_CODE_6031_MSG);

			ERROR_CODE_MSG.append(ERROR_CODE_6032, ERROR_CODE_6032_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6033, ERROR_CODE_6033_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6034, ERROR_CODE_6034_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6035, ERROR_CODE_6035_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6036, ERROR_CODE_6036_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6037, ERROR_CODE_6037_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6038, ERROR_CODE_6038_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6039, ERROR_CODE_6039_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6040, ERROR_CODE_6040_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6041, ERROR_CODE_6041_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6042, ERROR_CODE_6042_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6043, ERROR_CODE_6043_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6044, ERROR_CODE_6044_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6045, ERROR_CODE_6045_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6046, ERROR_CODE_6046_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6047, ERROR_CODE_6047_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6048, ERROR_CODE_6048_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6049, ERROR_CODE_6049_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_6050, ERROR_CODE_6050_MSG);

			ERROR_CODE_MSG.append(ERROR_CODE_7001, ERROR_CODE_7001_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_7002, ERROR_CODE_7002_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_7003, ERROR_CODE_7003_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_7004, ERROR_CODE_7004_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_7005, ERROR_CODE_7005_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_7006, ERROR_CODE_7006_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_7007, ERROR_CODE_7007_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_7008, ERROR_CODE_7008_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_7009, ERROR_CODE_7009_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_7010, ERROR_CODE_7010_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_7011, ERROR_CODE_7011_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_7012, ERROR_CODE_7012_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_7013, ERROR_CODE_7013_MSG);

			ERROR_CODE_MSG.append(ERROR_CODE_7101, ERROR_CODE_7101_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_7102, ERROR_CODE_7102_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_7103, ERROR_CODE_7103_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_7104, ERROR_CODE_7104_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_7105, ERROR_CODE_7105_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_7106, ERROR_CODE_7106_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_7107, ERROR_CODE_7107_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_7108, ERROR_CODE_7108_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_7109, ERROR_CODE_7109_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_7110, ERROR_CODE_7110_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_7111, ERROR_CODE_7111_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_7112, ERROR_CODE_7112_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_7113, ERROR_CODE_7113_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_7114, ERROR_CODE_7114_MSG);
			ERROR_CODE_MSG.append(ERROR_CODE_7115, ERROR_CODE_7115_MSG);

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("Error_Msg : JSONException: " + e.getMessage());
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		Error_Msg();

	}

	public static JSONObject getERROR_CODE_MSG() {
		Error_Msg();

		return ERROR_CODE_MSG;
	}

	public static String getErrorCodeID(String responseStatus) {
		// String responseStatus = "56452C3232320D0A";
		// responseStatus = responseStatus.replace(ConstantHV_Src.HV_ER_TERMINATOR, "");
		// responseStatus =
		// responseStatus.replace(ConstantHV_Src.HV_CMD_ERROR_RESPONSE_HDR, "");
		// responseStatus =
		// responseStatus.replace(ConstantCI_Src.CI_CMD_ERROR_RESPONSE_HDR, "");
		// ApplicationLauncher.logger.info("Assert_ErrorCodeMap:
		// responseStatus1:"+responseStatus);
		byte[] bytes = DatatypeConverter.parseHexBinary(responseStatus);// Hex.decodeHex(responseStatus);
		responseStatus = new String(bytes, StandardCharsets.UTF_8);
		// ApplicationLauncher.logger.info("Assert_ErrorCodeMap:
		// responseStatus2:"+responseStatus);
		String ErrorMsgID = "Error_Code_" + responseStatus;
		return ErrorMsgID;
	}

	public static String getErrorMsgDescription(String ErrorMsgID) {
		// TP_ID_MapInit();

		try {
			// String AliasID_StrippedTestPoint_Name =
			// inputTestPoint_Name.replaceAll("_.*?-", "-");
			// ApplicationLauncher.logger.debug("TP_ID_MapInit : inputTestPoint_Name: " +
			// inputTestPoint_Name);
			// ApplicationLauncher.logger.debug("TP_ID_MapInit :
			// AliasID_StrippedTestPoint_Name: " + AliasID_StrippedTestPoint_Name);
			if (ERROR_CODE_MSG.has(ErrorMsgID)) {
				ApplicationLauncher.logger
						.debug("getErrorMsgDescription : ERROR_CODE_MSG: " + ERROR_CODE_MSG.getString(ErrorMsgID));
				return ERROR_CODE_MSG.getString(ErrorMsgID);
			}
		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("getErrorMsgDescription : JSONException: " + e.getMessage());
		}
		return null;
	}

}
