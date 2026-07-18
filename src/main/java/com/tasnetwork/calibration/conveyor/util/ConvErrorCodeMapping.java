package com.tasnetwork.calibration.conveyor.util;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.TimerTask;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import com.tasnetwork.calibration.conveyor.bay.verific.S16_check_for_pallets_at_both_bays_in_loop;
import com.tasnetwork.calibration.conveyor.bay.ft.S12_turn_on_divertor_relay_FT_Bay;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

import javafx.fxml.Initializable;

public class ConvErrorCodeMapping implements Initializable {

    public static JSONObject ERROR_CODE_MSG = new JSONObject();

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

    public static final String ERROR_CODE_401 = " ";
    public static final String ERROR_CODE_401_MSG = "Validation Success, file shall be deployed now";

    public static final String ERROR_CODE_402 = "ERROR_CODE_402";
    public static final String ERROR_CODE_402_MSG = "Files mismatching or not available with respect to Json";

    public static final String ERROR_CODE_403 = "ERROR_CODE_403";
    public static final String ERROR_CODE_403_MSG = "Total number of files mismatching with respect to Json";

    public static final String ERROR_CODE_404 = "ERROR_CODE_404";
    public static final String ERROR_CODE_404_MSG = "Json file is missing in the selected file";

    public static final String ERROR_CODE_405 = "ERROR_CODE_405";
    public static final String ERROR_CODE_405_MSG = "Validation Failed";

    public static final String ERROR_CODE_406 = "ERROR_CODE_406";
    public static final String ERROR_CODE_406_MSG = "Invalid File";

    public static final String ERROR_CODE_501 = "ERROR_CODE_501";
    public static final String ERROR_CODE_501_MSG = "Invalid Device ID. \\n\\nKindly check Reference Standard device is powered up. if not kindly power up and also restart the protamp panel\\n\\n      or\n\nKindly check Reference Standard device connection to the Panel\n\n      or \n\nCheck PC/Laptop is connected to Protamp wifi network";

    public static final String ERROR_CODE_502 = "ERROR_CODE_502";
    public static final String ERROR_CODE_502_MSG = "Setting Pulse Mode Failed ";

    public static final String ERROR_CODE_503 = "ERROR_CODE_503";
    public static final String ERROR_CODE_503_MSG = "Setting Target Pulse Count Failed ";

    public static final String ERROR_CODE_504 = "ERROR_CODE_504";
    public static final String ERROR_CODE_504_MSG = "Setting Meter Constant Failed";

    public static final String ERROR_CODE_505 = "ERROR_CODE_505";
    public static final String ERROR_CODE_505_MSG = "Setting Input Mode Failed";

    public static final String ERROR_CODE_506 = "ERROR_CODE_506";
    public static final String ERROR_CODE_506_MSG = "Setting Reference Standard Constant Failed";

    public static final String ERROR_CODE_507 = "ERROR_CODE_507";
    public static final String ERROR_CODE_507_MSG = "Setting Impulses Output Status to Active Power Failed ";

    public static final String ERROR_CODE_508 = "ERROR_CODE_508";
    public static final String ERROR_CODE_508_MSG = " ";

    public static final String ERROR_CODE_509 = "ERROR_CODE_509";
    public static final String ERROR_CODE_509_MSG = "Setting Reference Standard No of Samples Failed";

    public static final String ERROR_CODE_900 = "Error_Code_900";
    public static final String ERROR_CODE_900_MSG = "Unable to access Power Source Serial Port. Kindly check the setting and configure appropriately";

    public static final String ERROR_CODE_999 = "ERROR_CODE_999";
    public static final String ERROR_CODE_999_MSG = "USER ABORTED";

    // ======================== CONVEYOR =========================================//
    public static final String ERROR_CODE_601 = "ERROR_CODE_601";
    public static final String ERROR_CODE_601_MSG = "No_Error";

    public static final String ERROR_CODE_602 = "ERROR_CODE_602";
    public static final String ERROR_CODE_602_MSG = "XYZ Error";

    // ======================== FUNCTIONAL TEST BAY
    // =========================================//

    public static final String ERROR_CODE_FT_001 = "ERROR_CODE_FT_001";
    public static final String ERROR_CODE_FT_001_MSG = "FT Bay Pallet Sensing Not Working";

    public static final String ERROR_CODE_FT_002 = "ERROR_CODE_FT_002";
    public static final String ERROR_CODE_FT_002_MSG = "No Pallet Available at FT Bay";

    public static final String ERROR_CODE_FT_003 = "ERROR_CODE_FT_003";
    public static final String ERROR_CODE_FT_003_MSG = "FT Bay Finger Tip Not Closed";

    public static final String ERROR_CODE_FT_004 = "ERROR_CODE_FT_004";
    public static final String ERROR_CODE_FT_004_MSG = "No Pallet Available before FT Bay";

    public static final String ERROR_CODE_FT_005 = "ERROR_CODE_FT_005";
    public static final String ERROR_CODE_FT_005_MSG = "FT Bay Stopper Not Working";

    public static final String ERROR_CODE_FT_006 = "ERROR_CODE_FT_006";
    public static final String ERROR_CODE_FT_006_MSG = "QR Code Scanning of Pallet Failed";

    public static final String ERROR_CODE_FT_007 = "ERROR_CODE_FT_007";
    public static final String ERROR_CODE_FT_007_MSG = "FT Bay Stopper Not Closed";

    public static final String ERROR_CODE_FT_008 = "ERROR_CODE_FT_008";
    public static final String ERROR_CODE_FT_008_MSG = "Meters Presence Validation Failed";

    public static final String ERROR_CODE_FT_009 = "ERROR_CODE_FT_009";
    public static final String ERROR_CODE_FT_009_MSG = "No Pallet Sensed at Reject Bay";

    public static final String ERROR_CODE_FT_010 = "ERROR_CODE_FT_010";
    public static final String ERROR_CODE_FT_010_MSG = "Functional Test Failed";

    public static final String ERROR_CODE_FT_011 = "ERROR_CODE_FT_011";
    public static final String ERROR_CODE_FT_011_MSG = "Reject Bay Stopper Not Working";

    public static final String ERROR_CODE_FT_012 = "ERROR_CODE_FT_012";
    public static final String ERROR_CODE_FT_012_MSG = "Reject Bay Stopper Not Closed";

    public static final String ERROR_CODE_FT_013 = "ERROR_CODE_FT_013";
    public static final String ERROR_CODE_FT_013_MSG = "FT Bay Finger Tip Not Opened";

    public static final String ERROR_CODE_FT_014 = "ERROR_CODE_FT_014";
    public static final String ERROR_CODE_FT_014_MSG = "Pallet Available at HVT Bay";

    public static final String ERROR_CODE_FT_015 = "ERROR_CODE_FT_015";
    public static final String ERROR_CODE_FT_015_MSG = "HVT Bay Stopper Not Working";

    public static final String ERROR_CODE_FT_016 = "ERROR_CODE_FT_016";
    public static final String ERROR_CODE_FT_016_MSG = "HVT Bay Stopper Not Closed";

    public static final String ERROR_CODE_FT_017 = "ERROR_CODE_FT_017";
    public static final String ERROR_CODE_FT_017_MSG = "Failed to turn on FT Bay divertor relay";

    public static final String ERROR_CODE_FT_018 = "ERROR_CODE_FT_018";
    public static final String ERROR_CODE_FT_018_MSG = "FT Bay divertor relay not turned ON";

    public static final String ERROR_CODE_FT_019 = "ERROR_CODE_FT_019";
    public static final String ERROR_CODE_FT_019_MSG = "Failed to turn off FT Bay divertor relay";

    public static final String ERROR_CODE_FT_020 = "ERROR_CODE_FT_020";
    public static final String ERROR_CODE_FT_020_MSG = "FT Bay divertor relay not turned OFF";

    public static final String ERROR_CODE_FT_021 = "ERROR_CODE_FT_021";
    public static final String ERROR_CODE_FT_021_MSG = "Rejection Bay Reset Timeout";

    public static final String ERROR_CODE_FT_022 = "ERROR_CODE_FT_022";
    public static final String ERROR_CODE_FT_022_MSG = "QR Code Scanning of Meters Failed";

    public static final String ERROR_CODE_FT_023 = "ERROR_CODE_FT_023";
    public static final String ERROR_CODE_FT_023_MSG = "Ft Bay Stopper Not Opened";

    public static final String ERROR_CODE_FT_024 = "ERROR_CODE_FT_024";
    public static final String ERROR_CODE_FT_024_MSG = "FT Bay Pallet Sensing Not Working";

    public static final String ERROR_CODE_FT_025 = "ERROR_CODE_FT_025";
    public static final String ERROR_CODE_FT_025_MSG = "Pallet Not Reached Reject Bay";

    public static final String ERROR_CODE_FT_026 = "ERROR_CODE_FT_026";
    public static final String ERROR_CODE_FT_026_MSG = "Pallet Not Reached HVT Bay";

    public static final String ERROR_CODE_FT_027 = "ERROR_CODE_FT_027";
    public static final String ERROR_CODE_FT_027_MSG = "Failed to close FT Bay Finger Tip";

    public static final String ERROR_CODE_FT_028 = "ERROR_CODE_FT_028";
    public static final String ERROR_CODE_FT_028_MSG = "Failed to Start FT Source";

    public static final String ERROR_CODE_FT_029 = "ERROR_CODE_FT_029";
    public static final String ERROR_CODE_FT_029_MSG = "Failed to Stop FT Source";

    public static final String ERROR_CODE_FT_030 = "ERROR_CODE_FT_030";
    public static final String ERROR_CODE_FT_030_MSG = "FT Source Not Started";

    public static final String ERROR_CODE_FT_031 = "ERROR_CODE_FT_031";
    public static final String ERROR_CODE_FT_031_MSG = "FT Source Not Stopped";

    public static final String ERROR_CODE_FT_032 = "ERROR_CODE_FT_032";
    public static final String ERROR_CODE_FT_032_MSG = "Failed to Close Stopper Before FT Bay";

    public static final String ERROR_CODE_FT_033 = "ERROR_CODE_FT_033";
    public static final String ERROR_CODE_FT_033_MSG = "FT Bay Communication Time Out";

    public static final String ERROR_CODE_FT_034 = "ERROR_CODE_FT_034";
    public static final String ERROR_CODE_FT_034_MSG = "Stopper Before FT Bay Closed";

    public static final String ERROR_CODE_FT_035 = "ERROR_CODE_FT_035";
    public static final String ERROR_CODE_FT_035_MSG = "Failed to Read Stopper Before FT Bay";

    public static final String ERROR_CODE_FT_036 = "ERROR_CODE_FT_036";
    public static final String ERROR_CODE_FT_036_MSG = "Stopper Before FT Bay Opened";

    // =============== HVT Bay
    // =================================================================//

    public static final String ERROR_CODE_HVT_001 = "ERROR_CODE_HVT_001";
    public static final String ERROR_CODE_HVT_001_MSG = "HVT Bay Pallet Sensing Not Working";

    public static final String ERROR_CODE_HVT_002 = "ERROR_CODE_HVT_002";
    public static final String ERROR_CODE_HVT_002_MSG = "No Pallet Available at HVT Bay";

    public static final String ERROR_CODE_HVT_003 = "ERROR_CODE_HVT_003";
    public static final String ERROR_CODE_HVT_003_MSG = "Failed to Close HVT Bay Finger Tip";

    public static final String ERROR_CODE_HVT_004 = "ERROR_CODE_HVT_004";
    public static final String ERROR_CODE_HVT_004_MSG = "HVT Bay Finger Tip Not Closed";

    public static final String ERROR_CODE_HVT_005 = "ERROR_CODE_HVT_005";
    public static final String ERROR_CODE_HVT_005_MSG = "Failed to open HVT Bay Stopper";

    public static final String ERROR_CODE_HVT_006 = "ERROR_CODE_HVT_006";
    public static final String ERROR_CODE_HVT_006_MSG = "QR Code Scanning of Pallet Failed";

    public static final String ERROR_CODE_HVT_007 = "ERROR_CODE_HVT_007";
    public static final String ERROR_CODE_HVT_007_MSG = "Failed to close HVT Bay Stopper";

    public static final String ERROR_CODE_HVT_008 = "ERROR_CODE_HVT_008";
    public static final String ERROR_CODE_HVT_008_MSG = "HVT Bay Stopper Not Opened";

    public static final String ERROR_CODE_HVT_009 = "ERROR_CODE_HVT_009";
    public static final String ERROR_CODE_HVT_009_MSG = "HVT Bay Stopper Not Closed";

    public static final String ERROR_CODE_HVT_010 = "ERROR_CODE_HVT_010";
    public static final String ERROR_CODE_HVT_010_MSG = "High Voltage Test Failed";

    public static final String ERROR_CODE_HVT_011 = "ERROR_CODE_HVT_011";
    public static final String ERROR_CODE_HVT_011_MSG = "Failed to open HVT Bay Finger Tip";

    public static final String ERROR_CODE_HVT_012 = "ERROR_CODE_HVT_012";
    public static final String ERROR_CODE_HVT_012_MSG = "HVT Bay Finger Tip Not Opened";

    public static final String ERROR_CODE_HVT_013 = "ERROR_CODE_HVT_013";
    public static final String ERROR_CODE_HVT_013_MSG = "Pallet Available at IRT Bay";

    public static final String ERROR_CODE_HVT_014 = "ERROR_CODE_HVT_014";
    public static final String ERROR_CODE_HVT_014_MSG = "Failed to Start HVT Source";

    public static final String ERROR_CODE_HVT_015 = "ERROR_CODE_HVT_015";
    public static final String ERROR_CODE_HVT_015_MSG = "Failed to Stop HVT Source";

    public static final String ERROR_CODE_HVT_016 = "ERROR_CODE_HVT_016";
    public static final String ERROR_CODE_HVT_016_MSG = "HVT Source Not Started";

    public static final String ERROR_CODE_HVT_017 = "ERROR_CODE_HVT_017";
    public static final String ERROR_CODE_HVT_017_MSG = "HVT Source Not Stopped";

    // ========= IRT Bay

    public static final String ERROR_CODE_IRT_001 = "ERROR_CODE_IRT_001";
    public static final String ERROR_CODE_IRT_001_MSG = "IRT Bay Pallet Sensing Not Working";

    public static final String ERROR_CODE_IRT_002 = "ERROR_CODE_IRT_002";
    public static final String ERROR_CODE_IRT_002_MSG = "No Pallet Available at IRT Bay";

    public static final String ERROR_CODE_IRT_003 = "ERROR_CODE_IRT_003";
    public static final String ERROR_CODE_IRT_003_MSG = "Failed to Close IRT Bay Finger Tip";

    public static final String ERROR_CODE_IRT_004 = "ERROR_CODE_IRT_004";
    public static final String ERROR_CODE_IRT_004_MSG = "IRT Bay Finger Tip Not Closed";

    public static final String ERROR_CODE_IRT_005 = "ERROR_CODE_IRT_005";
    public static final String ERROR_CODE_IRT_005_MSG = "Failed to open IRT Bay Stopper";

    public static final String ERROR_CODE_IRT_006 = "ERROR_CODE_IRT_006";
    public static final String ERROR_CODE_IRT_006_MSG = "QR Code Scanning of Pallet Failed";

    public static final String ERROR_CODE_IRT_007 = "ERROR_CODE_IRT_007";
    public static final String ERROR_CODE_IRT_007_MSG = "Failed to close IRT Bay Stopper";

    public static final String ERROR_CODE_IRT_008 = "ERROR_CODE_IRT_008";
    public static final String ERROR_CODE_IRT_008_MSG = "IRT Bay Stopper Not Opened";

    public static final String ERROR_CODE_IRT_009 = "ERROR_CODE_IRT_009";
    public static final String ERROR_CODE_IRT_009_MSG = "IRT Bay Stopper Not Closed";

    public static final String ERROR_CODE_IRT_010 = "ERROR_CODE_IRT_010";
    public static final String ERROR_CODE_IRT_010_MSG = "Insulation Resistance Test Failed";

    public static final String ERROR_CODE_IRT_011 = "ERROR_CODE_IRT_011";
    public static final String ERROR_CODE_IRT_011_MSG = "Failed to open IRT Bay Finger Tip";

    public static final String ERROR_CODE_IRT_012 = "ERROR_CODE_IRT_012";
    public static final String ERROR_CODE_IRT_012_MSG = "IRT Bay Finger Tip Not Opened";

    public static final String ERROR_CODE_IRT_013 = "ERROR_CODE_IRT_013";
    public static final String ERROR_CODE_IRT_013_MSG = "Pallet Available at CALIB Bay";

    // ========== CALIB Bay

    public static final String ERROR_CODE_CALIB_001 = "ERROR_CODE_CALIB_001";
    public static final String ERROR_CODE_CALIB_001_MSG = "CALIB Bay Pallet Sensing Not Working";

    public static final String ERROR_CODE_CALIB_002 = "ERROR_CODE_CALIB_002";
    public static final String ERROR_CODE_CALIB_002_MSG = "No Pallet Available at CALIB Bay";

    public static final String ERROR_CODE_CALIB_003 = "ERROR_CODE_CALIB_003";
    public static final String ERROR_CODE_CALIB_003_MSG = "Failed to Close CALIB Bay Finger Tip";

    public static final String ERROR_CODE_CALIB_004 = "ERROR_CODE_CALIB_004";
    public static final String ERROR_CODE_CALIB_004_MSG = "CALIB Bay Finger Tip Not Closed";

    public static final String ERROR_CODE_CALIB_005 = "ERROR_CODE_CALIB_005";
    public static final String ERROR_CODE_CALIB_005_MSG = "Failed to open CALIB Bay Stopper";

    public static final String ERROR_CODE_CALIB_006 = "ERROR_CODE_CALIB_006";
    public static final String ERROR_CODE_CALIB_006_MSG = "QR Code Scanning of Pallet Failed";

    public static final String ERROR_CODE_CALIB_007 = "ERROR_CODE_CALIB_007";
    public static final String ERROR_CODE_CALIB_007_MSG = "Failed to close CALIB Bay Stopper";

    public static final String ERROR_CODE_CALIB_008 = "ERROR_CODE_CALIB_008";
    public static final String ERROR_CODE_CALIB_008_MSG = "CALIB Bay Stopper Not Opened";

    public static final String ERROR_CODE_CALIB_009 = "ERROR_CODE_CALIB_009";
    public static final String ERROR_CODE_CALIB_009_MSG = "CALIB Bay Stopper Not Closed";

    public static final String ERROR_CODE_CALIB_010 = "ERROR_CODE_CALIB_010";
    public static final String ERROR_CODE_CALIB_010_MSG = "Calibration Test Failed";

    public static final String ERROR_CODE_CALIB_011 = "ERROR_CODE_CALIB_011";
    public static final String ERROR_CODE_CALIB_011_MSG = "Failed to open CALIB Bay Finger Tip";

    public static final String ERROR_CODE_CALIB_012 = "ERROR_CODE_CALIB_012";
    public static final String ERROR_CODE_CALIB_012_MSG = "CALIB Bay Finger Tip Not Opened";

    public static final String ERROR_CODE_CALIB_013 = "ERROR_CODE_CALIB_013";
    public static final String ERROR_CODE_CALIB_013_MSG = "Pallet Available at Waiting Bay";

    public static final String ERROR_CODE_CALIB_014 = "ERROR_CODE_CALIB_014";
    public static final String ERROR_CODE_CALIB_014_MSG = "Waiting Bay Pallet Sensing Not Working";

    public static final String ERROR_CODE_CALIB_015 = "ERROR_CODE_CALIB_015";
    public static final String ERROR_CODE_CALIB_015_MSG = "Failed to Start Calibration Source";

    public static final String ERROR_CODE_CALIB_016 = "ERROR_CODE_CALIB_016";
    public static final String ERROR_CODE_CALIB_016_MSG = "Failed to Turn On Main CT";

    public static final String ERROR_CODE_CALIB_017 = "ERROR_CODE_CALIB_017";
    public static final String ERROR_CODE_CALIB_017_MSG = "Failed to Turn On Neutral CT";

    public static final String ERROR_CODE_CALIB_018 = "ERROR_CODE_CALIB_018";
    public static final String ERROR_CODE_CALIB_018_MSG = "Failed to Reset Main CT & Neutral CT";

    public static final String ERROR_CODE_CALIB_019 = "ERROR_CODE_CALIB_019";
    public static final String ERROR_CODE_CALIB_019_MSG = "Voltage Start Failed";

    public static final String ERROR_CODE_CALIB_020 = "ERROR_CODE_CALIB_020";
    public static final String ERROR_CODE_CALIB_020_MSG = "Power Stop Failed";

    public static final String ERROR_CODE_CALIB_021 = "ERROR_CODE_CALIB_021";
    public static final String ERROR_CODE_CALIB_021_MSG = "Failed to Turn ON Meter Relays ";

    public static final String ERROR_CODE_CALIB_022 = "ERROR_CODE_CALIB_022";
    public static final String ERROR_CODE_CALIB_022_MSG = "Power Source Start Failed";

    public static final String ERROR_CODE_CALIB_023 = "ERROR_CODE_CALIB_023";
    public static final String ERROR_CODE_CALIB_023_MSG = "Power Source with Main CT Start Failed";

    public static final String ERROR_CODE_CALIB_024 = "ERROR_CODE_CALIB_024";
    public static final String ERROR_CODE_CALIB_024_MSG = "Power Source with Neutral CT Start Failed";

    public static final String ERROR_CODE_CALIB_025 = "ERROR_CODE_CALIB_025";
    public static final String ERROR_CODE_CALIB_025_MSG = "Current Stop Failed";

    public static final String ERROR_CODE_CALIB_026 = "ERROR_CODE_CALIB_026";
    public static final String ERROR_CODE_CALIB_026_MSG = "Failed to Make Main CT";

    public static final String ERROR_CODE_CALIB_027 = "ERROR_CODE_CALIB_027";
    public static final String ERROR_CODE_CALIB_027_MSG = "Failed to Make Neutral CT";

    // ======== VERIFICATION BAY ====

    public static final String ERROR_CODE_VERIFIC_001 = "ERROR_CODE_VERIFIC_001";
    public static final String ERROR_CODE_VERIFIC_001_MSG = "VERIFIC Bay Pallet Sensing Not Working";

    public static final String ERROR_CODE_VERIFIC_002 = "ERROR_CODE_VERIFIC_002";
    public static final String ERROR_CODE_VERIFIC_002_MSG = "No Pallet Available at VERIFIC Bay";

    public static final String ERROR_CODE_VERIFIC_003 = "ERROR_CODE_VERIFIC_003";
    public static final String ERROR_CODE_VERIFIC_003_MSG = "Failed to Close VERIFIC Bay Finger Tip";

    public static final String ERROR_CODE_VERIFIC_004 = "ERROR_CODE_VERIFIC_004";
    public static final String ERROR_CODE_VERIFIC_004_MSG = "VERIFIC Bay Finger Tip Not Closed";

    public static final String ERROR_CODE_VERIFIC_005 = "ERROR_CODE_VERIFIC_005";
    public static final String ERROR_CODE_VERIFIC_005_MSG = "Failed to open VERIFIC Bay Stopper";

    public static final String ERROR_CODE_VERIFIC_006 = "ERROR_CODE_VERIFIC_006";
    public static final String ERROR_CODE_VERIFIC_006_MSG = "QR Code Scanning of Pallet Failed";

    public static final String ERROR_CODE_VERIFIC_007 = "ERROR_CODE_VERIFIC_007";
    public static final String ERROR_CODE_VERIFIC_007_MSG = "Failed to close VERIFIC Bay Stopper";

    public static final String ERROR_CODE_VERIFIC_008 = "ERROR_CODE_VERIFIC_008";
    public static final String ERROR_CODE_VERIFIC_008_MSG = "VERIFIC Bay Stopper Not Opened";

    public static final String ERROR_CODE_VERIFIC_009 = "ERROR_CODE_VERIFIC_009";
    public static final String ERROR_CODE_VERIFIC_009_MSG = "VERIFIC Bay Stopper Not Closed";

    public static final String ERROR_CODE_VERIFIC_010 = "ERROR_CODE_VERIFIC_010";
    public static final String ERROR_CODE_VERIFIC_010_MSG = "Verification Test Failed";

    public static final String ERROR_CODE_VERIFIC_011 = "ERROR_CODE_VERIFIC_011";
    public static final String ERROR_CODE_VERIFIC_011_MSG = "Failed to open VERIFIC Bay Finger Tip";

    public static final String ERROR_CODE_VERIFIC_012 = "ERROR_CODE_VERIFIC_012";
    public static final String ERROR_CODE_VERIFIC_012_MSG = "VERIFIC Bay Finger Tip Not Opened";

    public static final String ERROR_CODE_VERIFIC_013 = "ERROR_CODE_VERIFIC_013";
    public static final String ERROR_CODE_VERIFIC_013_MSG = "Pallet Available at SCT_NLT Bay 1";

    public static final String ERROR_CODE_VERIFIC_014 = "ERROR_CODE_VERIFIC_014";
    public static final String ERROR_CODE_VERIFIC_014_MSG = "Pallet Available at SCT_NLT Bay 2";

    public static final String ERROR_CODE_VERIFIC_015 = "ERROR_CODE_VERIFIC_015";
    public static final String ERROR_CODE_VERIFIC_015_MSG = "Failed to turn on VERIFIC bay diverter relay";

    public static final String ERROR_CODE_VERIFIC_016 = "ERROR_CODE_VERIFIC_016";
    public static final String ERROR_CODE_VERIFIC_016_MSG = "VERIFIC Bay diverter relay not turned on";

    public static final String ERROR_CODE_VERIFIC_017 = "ERROR_CODE_VERIFIC_017";
    public static final String ERROR_CODE_VERIFIC_017_MSG = "Failed to turn off VERIFIC bay diverter relay";

    public static final String ERROR_CODE_VERIFIC_018 = "ERROR_CODE_VERIFIC_018";
    public static final String ERROR_CODE_VERIFIC_018_MSG = "VERIFIC Bay diverter relay not turned off";

    public static final String ERROR_CODE_VERIFIC_019 = "ERROR_CODE_VERIFIC_019";
    public static final String ERROR_CODE_VERIFIC_019_MSG = "Checking for pallets at both_bays timeout";

    public static final String ERROR_CODE_VERIFIC_020 = "ERROR_CODE_VERIFIC_020";
    public static final String ERROR_CODE_VERIFIC_020_MSG = "Failed to Close VERIFIC Bay Finger Tip";

    public static final String ERROR_CODE_VERIFIC_021 = "ERROR_CODE_VERIFIC_021";
    public static final String ERROR_CODE_VERIFIC_021_MSG = "Failed to open VERIFIC Bay Stopper";

    public static final String ERROR_CODE_VERIFIC_022 = "ERROR_CODE_VERIFIC_022";
    public static final String ERROR_CODE_VERIFIC_022_MSG = "All Pallets Passed Out of Verific Bay";

    public static final String ERROR_CODE_VERIFIC_023 = "ERROR_CODE_VERIFIC_023";
    public static final String ERROR_CODE_VERIFIC_023_MSG = "Failed to Start Verific Test Source ";

    public static final String ERROR_CODE_VERIFIC_024 = "ERROR_CODE_VERIFIC_024";
    public static final String ERROR_CODE_VERIFIC_024_MSG = "Failed to Stop Verific Test Source ";

    public static final String ERROR_CODE_VERIFIC_025 = "ERROR_CODE_VERIFIC_025";
    public static final String ERROR_CODE_VERIFIC_025_MSG = "Verification Test Start Execution Failed ";

    public static final String ERROR_CODE_VERIFIC_026 = "ERROR_CODE_VERIFIC_026";
    public static final String ERROR_CODE_VERIFIC_026_MSG = " Failed to Get Test Result ";

    public static final String ERROR_CODE_VERIFIC_027 = "ERROR_CODE_VERIFIC_027";
    public static final String ERROR_CODE_VERIFIC_027_MSG = "Verification Test Stop Execution Failed ";

    public static final String ERROR_CODE_VERIFIC_028 = "ERROR_CODE_VERIFIC_028";
    public static final String ERROR_CODE_VERIFIC_028_MSG = "Verification update dut serial no procal - Failed ";

    public static final String ERROR_CODE_VERIFIC_029 = "ERROR_CODE_VERIFIC_029";
    public static final String ERROR_CODE_VERIFIC_029_MSG = "Verification Failed to Get Complete deatailed Result ";

    public static final String ERROR_CODE_VERIFIC_030 = "ERROR_CODE_VERIFIC_030";
    public static final String ERROR_CODE_VERIFIC_030_MSG = "Verification failed due to Current circuit open - on time";

    public static final String ERROR_CODE_VERIFIC_031 = "ERROR_CODE_VERIFIC_031";
    public static final String ERROR_CODE_VERIFIC_031_MSG = "Verification failed due to Current circuit open - multiple time";

    public static final String ERROR_CODE_VERIFIC_032 = "ERROR_CODE_VERIFIC_032";
    public static final String ERROR_CODE_VERIFIC_032_MSG = "Kindly stop the bay to troubleshoot the issue. Once the issue is resolved, kindly start the bay to resume the testing";

    public static final String ERROR_CODE_VERIFIC_033 = "ERROR_CODE_VERIFIC_033";
    public static final String ERROR_CODE_VERIFIC_033_MSG = "User aborted pallet release from bay";

    // ================================== SCT_NLT BAY1
    // =======================================

    public static final String ERROR_CODE_SCT_NLT_BAY1_001 = "ERROR_CODE_SCT_NLT_BAY1_001";
    public static final String ERROR_CODE_SCT_NLT_BAY1_001_MSG = "SCT_NLT Bay 1 Pallet Sensing Not Working";

    public static final String ERROR_CODE_SCT_NLT_BAY1_002 = "ERROR_CODE_SCT_NLT_BAY1_002";
    public static final String ERROR_CODE_SCT_NLT_BAY1_002_MSG = "No Pallet Available at SCT_NLT Bay 1";

    public static final String ERROR_CODE_SCT_NLT_BAY1_003 = "ERROR_CODE_SCT_NLT_BAY1_003";
    public static final String ERROR_CODE_SCT_NLT_BAY1_003_MSG = "Failed to Close SCT_NLT Bay 1 Finger Tip";

    public static final String ERROR_CODE_SCT_NLT_BAY1_004 = "ERROR_CODE_SCT_NLT_BAY1_004";
    public static final String ERROR_CODE_SCT_NLT_BAY1_004_MSG = "SCT_NLT Bay 1 Finger Tip Not Closed";

    public static final String ERROR_CODE_SCT_NLT_BAY1_005 = "ERROR_CODE_SCT_NLT_BAY1_005";
    public static final String ERROR_CODE_SCT_NLT_BAY1_005_MSG = "Failed to open SCT_NLT Bay 1 Stopper";

    public static final String ERROR_CODE_SCT_NLT_BAY1_006 = "ERROR_CODE_SCT_NLT_BAY1_006";
    public static final String ERROR_CODE_SCT_NLT_BAY1_006_MSG = "QR Code Scanning of Pallet Failed";

    public static final String ERROR_CODE_SCT_NLT_BAY1_007 = "ERROR_CODE_SCT_NLT_BAY1_007";
    public static final String ERROR_CODE_SCT_NLT_BAY1_007_MSG = "Failed to close SCT_NLT Bay 1 Stopper";

    public static final String ERROR_CODE_SCT_NLT_BAY1_008 = "ERROR_CODE_SCT_NLT_BAY1_008";
    public static final String ERROR_CODE_SCT_NLT_BAY1_008_MSG = "SCT_NLT Bay 1 Stopper Not Opened";

    public static final String ERROR_CODE_SCT_NLT_BAY1_009 = "ERROR_CODE_SCT_NLT_BAY1_009";
    public static final String ERROR_CODE_SCT_NLT_BAY1_009_MSG = "SCT_NLT Bay 1 Stopper Not Closed";

    public static final String ERROR_CODE_SCT_NLT_BAY1_010 = "ERROR_CODE_SCT_NLT_BAY1_010";
    public static final String ERROR_CODE_SCT_NLT_BAY1_010_MSG = "SCT_NLT Test Failed";

    public static final String ERROR_CODE_SCT_NLT_BAY1_011 = "ERROR_CODE_SCT_NLT_BAY1_011";
    public static final String ERROR_CODE_SCT_NLT_BAY1_011_MSG = "Failed to open SCT_NLT Bay 1 Finger Tip";

    public static final String ERROR_CODE_SCT_NLT_BAY1_012 = "ERROR_CODE_SCT_NLT_BAY1_012";
    public static final String ERROR_CODE_SCT_NLT_BAY1_012_MSG = "SCT_NLT Bay 1 Finger Tip Not Opened";

    public static final String ERROR_CODE_SCT_NLT_BAY1_013 = "ERROR_CODE_SCT_NLT_BAY1_013";
    public static final String ERROR_CODE_SCT_NLT_BAY1_013_MSG = "Pallet Available at SCT_NLT Bay 1";

    public static final String ERROR_CODE_SCT_NLT_BAY1_014 = "ERROR_CODE_SCT_NLT_BAY1_014";
    public static final String ERROR_CODE_SCT_NLT_BAY1_014_MSG = "Pallet Available at Out Area 1";

    public static final String ERROR_CODE_SCT_NLT_BAY1_015 = "ERROR_CODE_SCT_NLT_BAY1_015";
    public static final String ERROR_CODE_SCT_NLT_BAY1_015_MSG = "Pallet Available at Out Area 2";

    public static final String ERROR_CODE_SCT_NLT_BAY1_016 = "ERROR_CODE_SCT_NLT_BAY1_016";
    public static final String ERROR_CODE_SCT_NLT_BAY1_016_MSG = "Failed to aquire Pallet release semaphore";

    public static final String ERROR_CODE_SCT_NLT_BAY1_017 = "ERROR_CODE_SCT_NLT_BAY1_017";
    public static final String ERROR_CODE_SCT_NLT_BAY1_017_MSG = "Failed to release Pallet release semaphore";

    public static final String ERROR_CODE_SCT_NLT_BAY1_018 = "ERROR_CODE_SCT_NLT_BAY1_018";
    public static final String ERROR_CODE_SCT_NLT_BAY1_018_MSG = "Failed to close SCT_NLT Bay 1 Stopper 2 ";

    public static final String ERROR_CODE_SCT_NLT_BAY1_019 = "ERROR_CODE_SCT_NLT_BAY1_019";
    public static final String ERROR_CODE_SCT_NLT_BAY1_019_MSG = "Failed to open SCT_NLT Bay 1 Stopper 2 ";

    public static final String ERROR_CODE_SCT_NLT_BAY1_020 = "ERROR_CODE_SCT_NLT_BAY1_020";
    public static final String ERROR_CODE_SCT_NLT_BAY1_020_MSG = "All Pallets Passed Out of STD NLDT Bay 1";

    public static final String ERROR_CODE_SCT_NLT_BAY1_021 = "ERROR_CODE_SCT_NLT_BAY1_021";
    public static final String ERROR_CODE_SCT_NLT_BAY1_021_MSG = "Failed to Start STD NLDT Bay 1 Source";

    public static final String ERROR_CODE_SCT_NLT_BAY1_022 = "ERROR_CODE_SCT_NLT_BAY1_022";
    public static final String ERROR_CODE_SCT_NLT_BAY1_022_MSG = "Failed to Stop STD NLDT Bay 1 Source";

    public static final String ERROR_CODE_SCT_NLT_BAY1_023 = "ERROR_CODE_SCT_NLT_BAY1_023";
    public static final String ERROR_CODE_SCT_NLT_BAY1_023_MSG = "Semaphore acquisition interrupted";

    public static final String ERROR_CODE_SCT_NLT_BAY1_024 = "ERROR_CODE_SCT_NLT_BAY1_024";
    public static final String ERROR_CODE_SCT_NLT_BAY1_024_MSG = "Semaphore release interrupted";

    public static final String ERROR_CODE_SCT_NLT_BAY1_026 = "ERROR_CODE_SCT_NLT_BAY1_026";
    public static final String ERROR_CODE_SCT_NLT_BAY1_026_MSG = " Failed to Get Test Result ";

    public static final String ERROR_CODE_SCT_NLT_BAY1_027 = "ERROR_CODE_SCT_NLT_BAY1_027";
    public static final String ERROR_CODE_SCT_NLT_BAY1_027_MSG = " Failed to stop the motor";

    public static final String ERROR_CODE_SCT_NLT_BAY1_028 = "ERROR_CODE_SCT_NLT_BAY1_028";
    public static final String ERROR_CODE_SCT_NLT_BAY1_028_MSG = " Failed to Get Complete deatailed Result ";

    // =================== SCT_NLT BAY2 ===================

    public static final String ERROR_CODE_SCT_NLT_BAY2_001 = "ERROR_CODE_SCT_NLT_BAY2_001";
    public static final String ERROR_CODE_SCT_NLT_BAY2_001_MSG = "SCT_NLT Bay 2 Pallet Sensing Not Working";

    public static final String ERROR_CODE_SCT_NLT_BAY2_002 = "ERROR_CODE_SCT_NLT_BAY2_002";
    public static final String ERROR_CODE_SCT_NLT_BAY2_002_MSG = "No Pallet Available at SCT_NLT Bay 2";

    public static final String ERROR_CODE_SCT_NLT_BAY2_003 = "ERROR_CODE_SCT_NLT_BAY2_003";
    public static final String ERROR_CODE_SCT_NLT_BAY2_003_MSG = "Failed to Close SCT_NLT Bay 2 Finger Tip";

    public static final String ERROR_CODE_SCT_NLT_BAY2_004 = "ERROR_CODE_SCT_NLT_BAY2_004";
    public static final String ERROR_CODE_SCT_NLT_BAY2_004_MSG = "SCT_NLT Bay 2 Finger Tip Not Closed";

    public static final String ERROR_CODE_SCT_NLT_BAY2_005 = "ERROR_CODE_SCT_NLT_BAY2_005";
    public static final String ERROR_CODE_SCT_NLT_BAY2_005_MSG = "Failed to open SCT_NLT Bay 2 Stopper";

    public static final String ERROR_CODE_SCT_NLT_BAY2_006 = "ERROR_CODE_SCT_NLT_BAY2_006";
    public static final String ERROR_CODE_SCT_NLT_BAY2_006_MSG = "QR Code Scanning of Pallet Failed";

    public static final String ERROR_CODE_SCT_NLT_BAY2_007 = "ERROR_CODE_SCT_NLT_BAY2_007";
    public static final String ERROR_CODE_SCT_NLT_BAY2_007_MSG = "Failed to close SCT_NLT Bay 2 Stopper";

    public static final String ERROR_CODE_SCT_NLT_BAY2_008 = "ERROR_CODE_SCT_NLT_BAY2_008";
    public static final String ERROR_CODE_SCT_NLT_BAY2_008_MSG = "SCT_NLT Bay 2 Stopper Not Opened";

    public static final String ERROR_CODE_SCT_NLT_BAY2_009 = "ERROR_CODE_SCT_NLT_BAY2_009";
    public static final String ERROR_CODE_SCT_NLT_BAY2_009_MSG = "SCT_NLT Bay 2 Stopper Not Closed";

    public static final String ERROR_CODE_SCT_NLT_BAY2_010 = "ERROR_CODE_SCT_NLT_BAY2_010";
    public static final String ERROR_CODE_SCT_NLT_BAY2_010_MSG = "SCT_NLT Test Failed";

    public static final String ERROR_CODE_SCT_NLT_BAY2_011 = "ERROR_CODE_SCT_NLT_BAY2_011";
    public static final String ERROR_CODE_SCT_NLT_BAY2_011_MSG = "Failed to open SCT_NLT Bay 2 Finger Tip";

    public static final String ERROR_CODE_SCT_NLT_BAY2_012 = "ERROR_CODE_SCT_NLT_BAY2_012";
    public static final String ERROR_CODE_SCT_NLT_BAY2_012_MSG = "SCT_NLT Bay 2 Finger Tip Not Opened";

    public static final String ERROR_CODE_SCT_NLT_BAY2_013 = "ERROR_CODE_SCT_NLT_BAY2_013";
    public static final String ERROR_CODE_SCT_NLT_BAY2_013_MSG = "Pallet Available at SCT_NLT Bay 1";

    public static final String ERROR_CODE_SCT_NLT_BAY2_014 = "ERROR_CODE_SCT_NLT_BAY2_014";
    public static final String ERROR_CODE_SCT_NLT_BAY2_014_MSG = "Pallet Available at Out Area 1";

    public static final String ERROR_CODE_SCT_NLT_BAY2_015 = "ERROR_CODE_SCT_NLT_BAY2_015";
    public static final String ERROR_CODE_SCT_NLT_BAY2_015_MSG = "Pallet Available at Out Area 2";

    public static final String ERROR_CODE_SCT_NLT_BAY2_016 = "ERROR_CODE_SCT_NLT_BAY2_016";
    public static final String ERROR_CODE_SCT_NLT_BAY2_016_MSG = "Failed to aquire Pallet release semaphore";

    public static final String ERROR_CODE_SCT_NLT_BAY2_017 = "ERROR_CODE_SCT_NLT_BAY2_017";
    public static final String ERROR_CODE_SCT_NLT_BAY2_017_MSG = "Failed to release Pallet release semaphore";

    public static final String ERROR_CODE_SCT_NLT_BAY2_021 = "ERROR_CODE_SCT_NLT_BAY2_021";
    public static final String ERROR_CODE_SCT_NLT_BAY2_021_MSG = "Failed to Start STD NLDT Bay 2 Source";

    public static final String ERROR_CODE_SCT_NLT_BAY2_022 = "ERROR_CODE_SCT_NLT_BAY2_022";
    public static final String ERROR_CODE_SCT_NLT_BAY2_022_MSG = "Failed to Stop STD NLDT Bay 2 Source";

    public static final String ERROR_CODE_SCT_NLT_BAY2_023 = "ERROR_CODE_SCT_NLT_BAY2_023";
    public static final String ERROR_CODE_SCT_NLT_BAY2_023_MSG = "Semaphore acquisition interrupted";

    public static final String ERROR_CODE_SCT_NLT_BAY2_024 = "ERROR_CODE_SCT_NLT_BAY2_024";
    public static final String ERROR_CODE_SCT_NLT_BAY2_024_MSG = "Semaphore release interrupted";

    public static final String ERROR_CODE_SCT_NLT_BAY2_026 = "ERROR_CODE_SCT_NLT_BAY2_026";
    public static final String ERROR_CODE_SCT_NLT_BAY2_026_MSG = " Failed to Get Test Result ";

    public static final String ERROR_CODE_SCT_NLT_BAY2_028 = "ERROR_CODE_SCT_NLT_BAY2_028";
    public static final String ERROR_CODE_SCT_NLT_BAY2_028_MSG = " Failed to Get Complete deatailed Result ";

    // ========= COMMUNICATION BAY ===========================//

    public static final String ERROR_CODE_COMM_001 = "ERROR_CODE_COMM_001";
    public static final String ERROR_CODE_COMM_001_MSG = "COMM Bay Pallet Sensing Not Working";

    public static final String ERROR_CODE_COMM_002 = "ERROR_CODE_COMM_002";
    public static final String ERROR_CODE_COMM_002_MSG = "No Pallet Available at COMM Bay";

    public static final String ERROR_CODE_COMM_003 = "ERROR_CODE_COMM_003";
    public static final String ERROR_CODE_COMM_003_MSG = "Failed to Close COMM Bay Finger Tip";

    public static final String ERROR_CODE_COMM_004 = "ERROR_CODE_COMM_004";
    public static final String ERROR_CODE_COMM_004_MSG = "COMM Bay Finger Tip Not Closed";

    public static final String ERROR_CODE_COMM_005 = "ERROR_CODE_COMM_005";
    public static final String ERROR_CODE_COMM_005_MSG = "Failed to open COMM Bay Stopper";

    public static final String ERROR_CODE_COMM_006 = "ERROR_CODE_COMM_006";
    public static final String ERROR_CODE_COMM_006_MSG = "QR Code Scanning of Pallet Failed";

    public static final String ERROR_CODE_COMM_007 = "ERROR_CODE_COMM_007";
    public static final String ERROR_CODE_COMM_007_MSG = "Failed to close COMM Bay Stopper";

    public static final String ERROR_CODE_COMM_008 = "ERROR_CODE_COMM_008";
    public static final String ERROR_CODE_COMM_008_MSG = "COMM Bay Stopper Not Opened";

    public static final String ERROR_CODE_COMM_009 = "ERROR_CODE_COMM_009";
    public static final String ERROR_CODE_COMM_009_MSG = "COMM Bay Stopper Not Closed";

    public static final String ERROR_CODE_COMM_010 = "ERROR_CODE_COMM_010";
    public static final String ERROR_CODE_COMM_010_MSG = "COMM Test Failed";

    public static final String ERROR_CODE_COMM_011 = "ERROR_CODE_COMM_011";
    public static final String ERROR_CODE_COMM_011_MSG = "Failed to open COMM Bay Finger Tip";

    public static final String ERROR_CODE_COMM_012 = "ERROR_CODE_COMM_012";
    public static final String ERROR_CODE_COMM_012_MSG = "COMM Bay Finger Tip Not Opened";

    public static final String ERROR_CODE_COMM_013 = "ERROR_CODE_COMM_013";
    public static final String ERROR_CODE_COMM_013_MSG = "Pallet Available at Unloading Bay 2";

    // ============== UNLOADING BAY

    public static final String ERROR_CODE_UNLOADING_001 = "ERROR_CODE_UNLOADING_001";
    public static final String ERROR_CODE_UNLOADING_001_MSG = "UNLOADING Bay Pallet Sensing Not Working";

    public static final String ERROR_CODE_UNLOADING_002 = "ERROR_CODE_UNLOADING_002";
    public static final String ERROR_CODE_UNLOADING_002_MSG = "No Pallet Available at UNLOADING Bay";

    public static final String ERROR_CODE_UNLOADING_003 = "ERROR_CODE_UNLOADING_003";
    public static final String ERROR_CODE_UNLOADING_003_MSG = "QR Code Scanning of Pallet Failed";

    public static final String ERROR_CODE_UNLOADING_004 = "ERROR_CODE_UNLOADING_004";
    public static final String ERROR_CODE_UNLOADING_004_MSG = "Failed to open UNLOADING Bay Stopper";

    public static final String ERROR_CODE_UNLOADING_005 = "ERROR_CODE_UNLOADING_005";
    public static final String ERROR_CODE_UNLOADING_005_MSG = "Failed to close UNLOADING Bay Stopper";

    public static final String ERROR_CODE_UNLOADING_006 = "ERROR_CODE_UNLOADING_006";
    public static final String ERROR_CODE_UNLOADING_006_MSG = "UNLOADING Bay Stopper Not Opened";

    public static final String ERROR_CODE_UNLOADING_007 = "ERROR_CODE_UNLOADING_007";
    public static final String ERROR_CODE_UNLOADING_007_MSG = "UNLOADING Bay Stopper Not Closed";

    public static final String ERROR_CODE_UNLOADING_008 = "ERROR_CODE_UNLOADING_008";
    public static final String ERROR_CODE_UNLOADING_008_MSG = "Pallet Available at Loading Bay";

    public static final String ERROR_CODE_UNLOADING_009 = "ERROR_CODE_UNLOADING_009";
    public static final String ERROR_CODE_UNLOADING_009_MSG = "Empty pallet detected";

    public static final String ERROR_CODE_UNLOADING_010 = "ERROR_CODE_UNLOADING_010";
    public static final String ERROR_CODE_UNLOADING_010_MSG = "Failed meters detected";

    public static final String ERROR_CODE_UNLOADING_011 = "ERROR_CODE_UNLOADING_011";
    public static final String ERROR_CODE_UNLOADING_011_MSG = "Failed to sense alarm push button status";

    public static final String ERROR_CODE_UNLOADING_012 = "ERROR_CODE_UNLOADING_012";
    public static final String ERROR_CODE_UNLOADING_012_MSG = "Failed to indicate status of meters";

    public static final String ERROR_CODE_UNLOADING_013 = "ERROR_CODE_UNLOADING_013";
    public static final String ERROR_CODE_UNLOADING_013_MSG = "Failed to sense loading bay push button status";

    // ================== LOADING BAY ==============

    public static final String ERROR_CODE_LOADING_001 = "ERROR_CODE_LOADING_001";
    public static final String ERROR_CODE_LOADING_001_MSG = "LOADING Bay Push Button Sensing Not Working";

    public static final String ERROR_CODE_LOADING_002 = "ERROR_CODE_LOADING_002";
    public static final String ERROR_CODE_LOADING_002_MSG = "Failed to open LOADING Bay Stopper";

    public static final String ERROR_CODE_LOADING_003 = "ERROR_CODE_LOADING_003";
    public static final String ERROR_CODE_LOADING_003_MSG = "Failed to close LOADING Bay Stopper";

    public static final String ERROR_CODE_LOADING_004 = "ERROR_CODE_LOADING_004";
    public static final String ERROR_CODE_LOADING_004_MSG = "LOADING Bay Stopper Not Opened";

    public static final String ERROR_CODE_LOADING_005 = "ERROR_CODE_LOADING_005";
    public static final String ERROR_CODE_LOADING_005_MSG = "LOADING Bay Stopper Not Closed";

    // =================== REJECTION BAY =============

    public static final String ERROR_CODE_REJECTION_001 = "ERROR_CODE_REJECTION_001";
    public static final String ERROR_CODE_REJECTION_001_MSG = "REJECTION Bay Pallet Sensing Not Working";

    // =================== WAITING BAY =============

    public static final String ERROR_CODE_WAITING_001 = "ERROR_CODE_WAITING_001";
    public static final String ERROR_CODE_WAITING_001_MSG = "Waiting Bay Pallet Sensing Not Working";

    public static final String ERROR_CODE_WAITING_002 = "ERROR_CODE_WAITING_002";
    public static final String ERROR_CODE_WAITING_002_MSG = "No Pallet Available at Waiting Bay";

    public static final String ERROR_CODE_WAITING_003 = "ERROR_CODE_WAITING_003";
    public static final String ERROR_CODE_WAITING_003_MSG = "Failed to open Waiting Bay Stopper";

    public static final String ERROR_CODE_WAITING_004 = "ERROR_CODE_WAITING_004";
    public static final String ERROR_CODE_WAITING_004_MSG = "Failed to close Waiting Bay Stopper";

    public static final String ERROR_CODE_WAITING_005 = "ERROR_CODE_WAITING_005";
    public static final String ERROR_CODE_WAITING_005_MSG = "Waiting Bay Stopper Not Opened";

    public static final String ERROR_CODE_WAITING_006 = "ERROR_CODE_WAITING_006";
    public static final String ERROR_CODE_WAITING_006_MSG = "Waiting Bay Stopper Not Closed";

    public static final String ERROR_CODE_WAITING_007 = "ERROR_CODE_WAITING_007";
    public static final String ERROR_CODE_WAITING_007_MSG = "Pallet Available at Calibration Bay";

    public static final String ERROR_CODE_WAITING_008 = "ERROR_CODE_WAITING_008";
    public static final String ERROR_CODE_WAITING_008_MSG = "Calibration Bay Pallet Sensing Not Working";

    public static final String ERROR_CODE_WAITING_009 = "ERROR_CODE_WAITING_009";
    public static final String ERROR_CODE_WAITING_009_MSG = "Pallets Not Reached Verific Bay";

    // ===========

    public static final String ERROR_CODE_5001 = "Error_Code_5001";
    public static final String ERROR_CODE_5001_MSG = "Black listed meter id found";

    public static final String ERROR_CODE_5002 = "Error_Code_5002";
    public static final String ERROR_CODE_5002_MSG = "Meter Id already calibrated";

    public static final String ERROR_CODE_5003 = "Error_Code_5003";
    public static final String ERROR_CODE_5003_MSG = "Empty Meter id or error in reading meter id found";

    public static void Error_Msg() {
        try {
            ERROR_CODE_MSG = new JSONObject();

            ERROR_CODE_MSG.put(ERROR_CODE_105, ERROR_CODE_105_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_102, ERROR_CODE_102_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_101, ERROR_CODE_101_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_1001, ERROR_CODE_1001_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_103, ERROR_CODE_103_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_1XX, ERROR_CODE_1XX_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_133, ERROR_CODE_133_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_132, ERROR_CODE_132_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_131, ERROR_CODE_131_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_123, ERROR_CODE_123_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_122, ERROR_CODE_122_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_121, ERROR_CODE_121_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_113, ERROR_CODE_113_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_112, ERROR_CODE_112_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_111, ERROR_CODE_111_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_104A, ERROR_CODE_104A_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_106, ERROR_CODE_106_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_106A, ERROR_CODE_106A_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_100, ERROR_CODE_100_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_100A, ERROR_CODE_100A_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_100B, ERROR_CODE_100B_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_213, ERROR_CODE_213_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_212, ERROR_CODE_212_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_211, ERROR_CODE_211_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_223, ERROR_CODE_223_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_222, ERROR_CODE_222_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_221, ERROR_CODE_221_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_233, ERROR_CODE_233_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_232, ERROR_CODE_232_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_231, ERROR_CODE_231_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_214, ERROR_CODE_214_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_224, ERROR_CODE_224_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_234, ERROR_CODE_234_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_2041, ERROR_CODE_2041_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_2040, ERROR_CODE_2040_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_2042, ERROR_CODE_2042_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_301, ERROR_CODE_301_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_3000, ERROR_CODE_3000_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_103A, ERROR_CODE_103A_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_1XXA, ERROR_CODE_1XXA_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_111A, ERROR_CODE_111A_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_112A, ERROR_CODE_112A_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_104B, ERROR_CODE_104B_MSG);

            ERROR_CODE_MSG.put(ERROR_CODE_401, ERROR_CODE_401_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_402, ERROR_CODE_402_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_403, ERROR_CODE_403_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_404, ERROR_CODE_404_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_405, ERROR_CODE_405_MSG);
            ERROR_CODE_MSG.put(ERROR_CODE_406, ERROR_CODE_406_MSG);

        } catch (JSONException e) {

            e.printStackTrace();
            ApplicationLauncher.logger.error("Error_Msg : JSONException: " + e.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Error_Msg();

    }

    public static String getKeyErrorCodeID(String InputErrorMsg) {

        // InputErrorMsg = String.format("%03d", Integer.parseInt(InputErrorMsg));
        JSONObject testPointID_Map = new JSONObject();
        testPointID_Map = ERROR_CODE_MSG;
        Iterator iter = testPointID_Map.keys();
        String valueTestPointName = "";
        String keyTestPointID = null;
        while (iter.hasNext()) {
            valueTestPointName = (String) iter.next();
            keyTestPointID = null;
            try {
                keyTestPointID = testPointID_Map.getString(valueTestPointName);
                if (keyTestPointID.equals(InputErrorMsg)) {
                    return valueTestPointName;
                }

            } catch (JSONException e) {

                e.printStackTrace();
            }

        }
        return null;
    }

    public static String getKeyValue(String inputTestPoint_Name) {
        // TP_ID_MapInit();

        try {
            // String AliasID_StrippedTestPoint_Name =
            // inputTestPoint_Name.replaceAll("_.*?-", "-");
            ApplicationLauncher.logger.debug("TP_ID_MapInit : inputTestPoint_Name: " + inputTestPoint_Name);
            ApplicationLauncher.logger.debug("TP_ID_MapInit : AliasID_StrippedTestPoint_Name: " + inputTestPoint_Name);
            if (ERROR_CODE_MSG.has(inputTestPoint_Name)) {
                ApplicationLauncher.logger
                        .debug("TP_ID_MapInit : TP_Name: " + ERROR_CODE_MSG.getString(inputTestPoint_Name));
                return ERROR_CODE_MSG.getString(inputTestPoint_Name);
            }
        } catch (JSONException e) {

            e.printStackTrace();
            ApplicationLauncher.logger.error("getKeyValue : JSONException: " + e.getMessage());
        }
        return null;
    }

    public static JSONObject getERROR_CODE_MSG() {
        Error_Msg();

        return ERROR_CODE_MSG;
    }

}
