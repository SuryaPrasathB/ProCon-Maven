package com.tasnetwork.calibration.conveyor.bay;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.conveyor.bay.hv.HV_ReadResult;
import com.tasnetwork.calibration.conveyor.bay.ir.IR_ReadResult;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.database.MySQL_Controller;
import com.tasnetwork.calibration.conveyor.serial.director.VoltPmDirector;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmVoltPm;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.util.IEEE754_Format;
import com.tasnetwork.spring.orm.model.DeviceSetting;

public class Elmeasure_MultiMeter {

    public static final int ER_LENGTH_ASCII = 9;//20; // Length of the response string without spaces
    public static final int ER_LENGTH_HEX   = 18;//20; // Length of the response string without spaces

    public static final String FUNCTION_CODE        = "03";   // Function code
    public static final String REGISTER_ADDRESS     = "008E"; // Start address
    public static final String NUM_BYTES            = "0002"; // Number of bytes to read

    public static final int DATA_START_BYTE  = 7;
    public static final int DATA_END_BYTE    = 14;
    
    //========================================================================================
    public String sendCommandToPanelHvMeter(DeviceSetting deviceSetting, String slaveId){
		ApplicationLauncher.logger.debug("sendCommandToPanelHvMeter : Entry");
		boolean status =  false;
	
		String response = "";
		String commPortID = deviceSetting.getPortName();
		String commBaudRate = deviceSetting.getBaudRate();
		//ScanForSerialPorts();
/*		JSONObject cNamePortSettingData = MySQL_Controller.sp_get_cname_port_setting(comPortCname);
		
		try {
			if(cNamePortSettingData.has("port_name")){
				commPortID = cNamePortSettingData.getString("port_name");
			
        	//cmbBxLDU_PortSelection2.setValue(saved_ldu2_setting.getString("port_name")); 	
			} else {
				//cmbBxLDU_PortSelection2.setValue("");
				ApplicationLauncher.logger.info("sendCommandToPanelHvMeter: port_name-1: Data not retrieved from DB");
			
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("sendCommandToPanelHvMeter: JSONException5-2:"+e.getMessage());
			//cmbBxLDU_PortSelection2.setValue("");
			ApplicationLauncher.logger.info("sendCommandToPanelHvMeter: port_name-2: Data not retrieved from database");
		
		} 
		
		try {
			if(cNamePortSettingData.has("baud_rate")){
				commBaudRate = cNamePortSettingData.getString("baud_rate");
			
			} else {
				ApplicationLauncher.logger.info("sendCommandToPanelHvMeter: baudrate-1: Data not retrieved from DB");
			
			}

		} catch (JSONException e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("sendCommandToPanelHvMeter: JSONException5-2:"+e.getMessage());
			ApplicationLauncher.logger.info("sendCommandToPanelHvMeter: baudrate-2: Data not retrieved from database");
		
		}*/ 
		
		try{
			ApplicationLauncher.logger.debug("sendCommandToPanelHvMeter : try ");

			if( (!commBaudRate.isEmpty()) && (!commPortID.isEmpty())){
				SpmVoltPm serialPortManagerQrScanner = new SpmVoltPm("Elm-slv-"+slaveId,ER_LENGTH_ASCII);
		    	status = serialPortManagerQrScanner.powerSourceComInitV2(commPortID,commBaudRate);
		    	if (!status) {
					ApplicationLauncher.logger.debug("sendCommandToPanelHvMeter : Analog Trigger  Failed");
				}else {
		    		serialPortManagerQrScanner.startSerialRxPhysical_VoltPm();
		    		serialPortManagerQrScanner.enableSerialRxPhysical_VoltPmMonitor(); 
		    		VoltPmDirector pwrSrcDirector = new VoltPmDirector(serialPortManagerQrScanner);
					// Map<String,Object>  responseMap = pwrSrcDirector.fetchMegaOhmMetrics(slaveId);
					Map<String,Object>  responseMap = pwrSrcDirector.fetchVoltMeterMetrics(slaveId);

					
					
					status = (boolean)responseMap.get("status");
					String responseData =  "";
					try{
						responseData = (String)responseMap.get("responseData");  
						ApplicationLauncher.logger.debug("sendCommandToPanelHvMeter: responseData1: "+responseData);
					 	responseData = extractVoltageValueFromResponse((String)responseMap.get("responseData"));
						//	responseData = extractFloatFromResponse((String)responseMap.get("responseData"));
						ApplicationLauncher.logger.debug("sendCommandToPanelHvMeter: responseData2: "+responseData);
						response =responseData;
			    	}catch(Exception e){
			    		e.printStackTrace();
			    		ApplicationLauncher.logger.error("sendCommandToPanelHvMeter: Exception : "+e.getMessage());
			    	}
		    		
		    		serialPortManagerQrScanner.disconnectVoltPm();
				}
			}
		}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("sendCommandToPanelHvMeter: Exception-X " +e.getMessage());
    	}

		ApplicationLauncher.logger.debug("sendCommandToPanelHvMeter : Exit");
		return response;
	}

//============================================================================================		 
    
    public String extractVoltageValueFromResponse(String inputString) {
        // Validate response length
        if (!validateResponseLength(inputString)) {
            ApplicationLauncher.logger.debug("Elmeasure_VoltMeter : readVoltMeter : Invalid response length!");
            return null ;
        }

        // Verify CRC
        if (!verifyCRC(inputString)) {
            ApplicationLauncher.logger.debug("Elmeasure_VoltMeter : readVoltMeter : CRC verification failed!");
            return null ;
        }
        
    	String voltageString = inputString.substring((DATA_START_BYTE - 1 ), DATA_END_BYTE);
        ApplicationLauncher.logger.debug("Elmeasure_VoltMeter : readVoltMeter : voltageString : " + voltageString);

    	
     // Split the hex data into two halves
        String firstHalf  = voltageString.substring(0, 4); // First 2 bytes
        String secondHalf = voltageString.substring(4);    // Last 2 bytes

        // Reverse the halves and combine them
        String reversedHexData = secondHalf + firstHalf;
        
        voltageString = IEEE754_Format.hexToFloat(reversedHexData);

		return voltageString;
	}
//=====================================================================================
	public HV_ReadResult readElmeasureVoltMeter(DeviceSetting deviceSetting, String slaveId) {
		
        
        ApplicationLauncher.logger.debug("Elmeasure_VoltMeter : readVoltMeter : Entry");
        
        float defaultValue = 0.0f; // Default value to return
        boolean status = false;   // Default status
        HV_ReadResult result = new HV_ReadResult(defaultValue, status);  // Initialize result object

        try {
        	ApplicationLauncher.logger.debug("Elmeasure_VoltMeter : Slave Id :" + slaveId);
            // Sending Command =================================================================//

            // Validate and format the slave ID
            int slaveIdValue = Integer.parseInt(slaveId, 16); // Convert hex string to integer
        	ApplicationLauncher.logger.debug("Elmeasure_VoltMeter : Slave Id :" + slaveIdValue);

            if (slaveIdValue < 0 || slaveIdValue > 255) {
                ApplicationLauncher.logger.debug("Elmeasure_VoltMeter : readVoltMeter : Invalid Slave ID!");
                return new HV_ReadResult(defaultValue, status);
            }

        	String responseHex = sendCommandToPanelHvMeter(deviceSetting,slaveId);
        	
            // Receiving Response =================================================================//

            //String responseHex = ""; // Placeholder for response, replace with actual `send(commandFrame)`
        	if (responseHex == null) {
                ApplicationLauncher.logger.debug("Elmeasure_VoltMeter : responseHex: null ");
        		status = false;
                result.setValue(0);        // Update the value in the result
                result.setStatus(status);                              // Update the status in the result
			} else {
			    // If all validations pass, update the status
	            status = true;
	            result.setValue(Float.parseFloat(responseHex));        // Update the value in the result
	            result.setStatus(status);                              // Update the status in the result
			}
        	
        } catch (Exception e) {
            ApplicationLauncher.logger.debug("Elmeasure_VoltMeter : readVoltMeter Exception : " + e.getMessage());
        }

        ApplicationLauncher.logger.debug("Elmeasure_VoltMeter : readVoltMeter : Exit");
        return result ;
    }

   	//============================================================================================		 

    
	 // Validate response length
    public boolean validateResponseLength(String responseHex) {
        String sanitizedResponse = responseHex.replace(" ", "");
        return sanitizedResponse.length() == ER_LENGTH_HEX;
    }
    
    
   	//============================================================================================		 

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
   	//============================================================================================		 

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
	
   	//============================================================================================		 

    
    

    
    
    
}


