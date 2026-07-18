package com.tasnetwork.calibration.conveyor.bay;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.conveyor.bay.NewlandQRCodeScanner;
import com.tasnetwork.calibration.conveyor.bay.ir.IR_ReadResult;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.database.MySQL_Controller;
import com.tasnetwork.calibration.conveyor.serial.director.MegaOhmPmDirector;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmMegaOhmPm;
import com.tasnetwork.calibration.conveyor.util.GUIUtils;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.util.IEEE754_Format;
import com.tasnetwork.spring.orm.model.DeviceSetting;

public class EIC_MegaOhmMeter {

    public static final int ER_LENGTH_ASCII = 9;// 20; // Length of the response string without spaces
    public static final int ER_LENGTH_HEX = 18;// 20; // Length of the response string without spaces

    public static final String FUNCTION_CODE = "03"; // Function code
    public static final String REGISTER_ADDRESS = "0000"; // Start address
    public static final String NUM_BYTES = "0002"; // Number of bytes to read

    // verifyCRC(responseHex);

    // float value = extractFloatFromResponse(responseHex);

    // ========================================================================================
    public String sendCommandToPanelIrMeter(DeviceSetting deviceSetting, String slaveId) {
        ApplicationLauncher.logger.debug("sendCommandToPanelIrMeter : Entry");
        boolean status = false;

        String response = "";
        String responseData = "";
        String commPortID = deviceSetting.getPortName();
        String commBaudRate = deviceSetting.getBaudRate();
        // ScanForSerialPorts();
        /*
         * JSONObject cNamePortSettingData =
         * MySQL_Controller.sp_get_cname_port_setting(comPortCname);
         * 
         * try {
         * if(cNamePortSettingData.has("port_name")){
         * commPortID = cNamePortSettingData.getString("port_name");
         * 
         * //cmbBxLDU_PortSelection2.setValue(saved_ldu2_setting.getString("port_name"))
         * ;
         * } else {
         * //cmbBxLDU_PortSelection2.setValue("");
         * ApplicationLauncher.logger.
         * info("sendCommandToPanelIrMeter: port_name-1: Data not retrieved from DB");
         * 
         * }
         * 
         * } catch (JSONException e) {
         * 
         * e.printStackTrace();
         * ApplicationLauncher.logger.
         * error("sendCommandToPanelIrMeter: JSONException5-2:"+e.getMessage());
         * //cmbBxLDU_PortSelection2.setValue("");
         * ApplicationLauncher.logger.
         * info("sendCommandToPanelIrMeter: port_name-2: Data not retrieved from database"
         * );
         * 
         * }
         * 
         * try {
         * if(cNamePortSettingData.has("baud_rate")){
         * commBaudRate = cNamePortSettingData.getString("baud_rate");
         * 
         * //cmbBxLDU_PortSelection2.setValue(saved_ldu2_setting.getString("port_name"))
         * ;
         * } else {
         * //cmbBxLDU_PortSelection2.setValue("");
         * ApplicationLauncher.logger.
         * info("sendCommandToPanelIrMeter: baudrate-1: Data not retrieved from DB");
         * 
         * }
         * 
         * } catch (JSONException e) {
         * 
         * e.printStackTrace();
         * ApplicationLauncher.logger.
         * error("sendCommandToPanelIrMeter: JSONException5-2:"+e.getMessage());
         * //cmbBxLDU_PortSelection2.setValue("");
         * ApplicationLauncher.logger.
         * info("sendCommandToPanelIrMeter: baudrate-2: Data not retrieved from database"
         * );
         * 
         * }
         */

        // qrPortSettingData.getString(key)//getCurrentLDU_ComPortID1();
        // String commBaudRate = //getCurrentLDU_ComBaudRate1();
        try {
            if ((!commBaudRate.isEmpty()) && (!commPortID.isEmpty())) {
                SpmMegaOhmPm serialPortManagerQrScanner = new SpmMegaOhmPm("eic-slv-" + slaveId, ER_LENGTH_ASCII);
                status = serialPortManagerQrScanner.powerSourceComInitV2(commPortID, commBaudRate);
                if (!status) {
                    // status = false;
                    ApplicationLauncher.logger.debug("sendCommandToPanelIrMeter :Analog Trigger  Failed");
                } else {
                    // setPortValidationTurnedON(true);
                    // status = serialDM_Obj.lscsLDU1_CheckCom();
                    // DisplayDataObj.pwrSrcEnableSerialMonitoring_V2();
                    serialPortManagerQrScanner.startSerialRxPhysical_MegaOhmPm();
                    serialPortManagerQrScanner.enableSerialRxPhysical_MegaOhmPmMonitor();
                    MegaOhmPmDirector pwrSrcDirector = new MegaOhmPmDirector(serialPortManagerQrScanner);
                    Map<String, Object> responseMap = pwrSrcDirector.fetchMegaOhmMetrics(slaveId);

                    status = (boolean) responseMap.get("status");

                    try {
                        responseData = (String) responseMap.get("responseData");
                        ApplicationLauncher.logger.debug("sendCommandToPanelIrMeter: responseData1: " + responseData);
                        responseData = extractFloatFromResponse((String) responseMap.get("responseData"));
                        ApplicationLauncher.logger.debug("sendCommandToPanelIrMeter: responseData2: " + responseData);
                    } catch (Exception e) {
                        e.printStackTrace();
                        ApplicationLauncher.logger.error("sendCommandToPanelIrMeter: Exception" + e.getMessage());
                    }

                    // DisplayDataObj.pwrSrcDisconnectPort_V2();
                    serialPortManagerQrScanner.disconnectMegaOhmPm();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ApplicationLauncher.logger.error("sendCommandToPanelIrMeter: Exception-X" + e.getMessage());
        }
        /*
         * if(DisplayDataObj.ValidateAllComPortAccessible()){
         * boolean isResponseExpected = true;
         * status = sendReadCommandQrCodeScanner(ANALOG_TRIGGER_SETTING,
         * isResponseExpected, EXPECTED_DATA_IN_HEX); // ANALOG_TRIGGER_SETTING
         * }
         */

        // DisplayDataObj.pwrSrcDisconnectPort_V2();
        // SerialDM_Obj.DisconnectPwrSrc();

        ApplicationLauncher.logger.debug("sendCommandToPanelIrMeter : Exit");
        return responseData;
    }

    // ============================================================================================

    public IR_ReadResult readMegaOhmMeter(DeviceSetting deviceSetting, String slaveId) {
        ApplicationLauncher.logger.debug("EIC_MegaOhmMeter : readMegaOhmMeter : Entry");

        float defaultValue = 0.0f; // Default value to return
        boolean status = false; // Default status
        IR_ReadResult result = new IR_ReadResult(defaultValue, status); // Initialize result object

        try {
            // Sending Command
            // =================================================================//

            // Validate and format the slave ID
            int slaveIdValue = Integer.parseInt(slaveId, 16); // Convert hex string to integer
            if (slaveIdValue < 0 || slaveIdValue > 255) {
                ApplicationLauncher.logger.debug("EIC_MegaOhmMeter : readMegaOhmMeter : Invalid Slave ID!");
                return new IR_ReadResult(defaultValue, status);
            }
            /*
             * String slaveIdHex = String.format("%02X", slaveIdValue); // Format as
             * 2-character hex string
             * 
             * // Combine all parts into the frame (without CRC)
             * String frame = slaveIdHex + FUNCTION_CODE + REGISTER_ADDRESS + NUM_BYTES;
             * 
             * // Calculate CRC for the frame
             * String crc = calculateCRC(frame);
             * 
             * // Combine the frame and CRC to get the final frame
             * String commandFrame = frame + crc;
             */

            // Print the final frame
            // ApplicationLauncher.logger.debug("EIC_MegaOhmMeter : readMegaOhmMeter :
            // Command: " + commandFrame);
            // NewlandQRCodeScanner qrScannerObj = new NewlandQRCodeScanner();
            // EIC_MegaOhmMeter eicMegaOhmMeter = new EIC_MegaOhmMeter();
            // String responseHex =
            // sendCommandToPanelIrMeter(ConstantBayPortNameMapping.COM_PORT_EIC_MEGA,slaveId);
            String responseHex = sendCommandToPanelIrMeter(deviceSetting, slaveId);
            // Receiving Response
            // =================================================================//

            // String responseHex = ""; // Placeholder for response, replace with actual
            // `send(commandFrame)`

            // Extract the float value
            // float value = extractFloatFromResponse(responseHex);
            // ApplicationLauncher.logger.debug("EIC_MegaOhmMeter : readMegaOhmMeter :
            // Extracted Float Value: " + value);

            if (responseHex == null) {
                status = false;
                result.setValue(0); // Update the value in the result
                result.setStatus(status); // Update the status in the result
            } else {
                // If all validations pass, update the status
                status = true;
                result.setValue(Float.parseFloat(responseHex)); // Update the value in the result
                result.setStatus(status); // Update the status in the result
            }

            ApplicationLauncher.logger.debug("EIC_MegaOhmMeter : readMegaOhmMeter : responseHex " + responseHex);

        } catch (Exception e) {
            ApplicationLauncher.logger
                    .debug("EIC_MegaOhmMeter : readMegaOhmMeter : Error creating command frame: " + e.getMessage());
        }

        ApplicationLauncher.logger.debug("EIC_MegaOhmMeter : readMegaOhmMeter : Exit");
        return result;
    }

    // ============================================================================================

    // Validate response length
    public boolean validateResponseLength(String responseHex) {
        String sanitizedResponse = responseHex.replace(" ", "");
        return sanitizedResponse.length() == ER_LENGTH_HEX;
    }
    // ============================================================================================

    // Extract float from Modbus response
    public String extractFloatFromResponse(String responseHex) {
        String result = null;

        // Remove spaces and extract the 4-byte data (bytes 3 to 6)
        String sanitizedResponse = responseHex.replace(" ", "");

        // Validate response length
        if (!validateResponseLength(responseHex)) {
            ApplicationLauncher.logger.debug("EIC_MegaOhmMeter : readMegaOhmMeter : Invalid response length!");
            return null;
        }

        // Verify CRC
        if (!verifyCRC(responseHex)) {
            ApplicationLauncher.logger.debug("EIC_MegaOhmMeter : readMegaOhmMeter : CRC verification failed!");
            return null;
        }

        String dataHex = sanitizedResponse.substring(6, 14); //

        // Convert the 4-byte hex string into a float
        // int intBits = Integer.parseUnsignedInt(dataHex, 16); // Parse as unsigned
        // integer
        /// VoltageDisplayData=IEEE754_Format.hexToFloat(VoltageInHex);
        result = IEEE754_Format.hexToFloat(dataHex);
        return result; // Float.intBitsToFloat(intBits); // Convert bits to float
    }
    // ============================================================================================

    // CRC verification
    public boolean verifyCRC(String responseHex) {
        // Remove spaces and split response into data and CRC parts
        String sanitizedResponse = responseHex.replace(" ", "");
        int length = sanitizedResponse.length();

        String dataPart = sanitizedResponse.substring(0, length - 4); // Exclude last 2 CRC bytes
        String crcPart = sanitizedResponse.substring(length - 4); // Last 4 hex digits are the CRC

        // Calculate CRC for the data part
        String calculatedCRC = calculateCRC(dataPart);

        // Verify CRC (received CRC is in little-endian format)
        return calculatedCRC.equalsIgnoreCase(crcPart);
    }
    // ============================================================================================

    // CRC-16 (Modbus) calculation using String
    public String calculateCRC(String hexString) {
        int crc = 0xFFFF;

        // Iterate through each byte (2 hex characters represent 1 byte)
        for (int i = 0; i < hexString.length(); i += 2) {
            int byteValue = Integer.parseInt(hexString.substring(i, i + 2), 16); // Convert hex to integer
            crc ^= byteValue; // XOR byte into least significant byte of CRC
            for (int j = 0; j < 8; j++) {
                if ((crc & 0x0001) != 0) {
                    crc = (crc >> 1) ^ 0xA001;
                } else {
                    crc >>= 1;
                }
            }
        }

        // Modbus CRC is stored in little-endian, so swap the bytes
        int crcLow = crc & 0xFF;
        int crcHigh = (crc >> 8) & 0xFF;
        return String.format("%02X%02X", crcLow, crcHigh); // Return CRC as little-endian hex string
    }

}
