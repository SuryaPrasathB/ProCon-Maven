package com.tasnetwork.calibration.conveyor.bay.ft;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.Constant_Motor_Requirement;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage; // Import for ConstantBayStateManage
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor; // Import for ConstantConveyor
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus; // Import for TestInterfaceStatus

public class S15_ensure_pallet_reached_HVT_Bay implements FtBayState {
	BayUtils bayUtils = new BayUtils();
	
	private String myBaySeqId = ConstantBayStateManage.FT_BAY_HP_SEQ_17; // Example, adjust if a more specific one exists

	public String getMyBayKey() {
		return myBayKey;
	}

    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        // Structured log entry for sequence start
        Ft.logger.info(String.format("[%s] : [PALLET_ARRIVAL_CHECK_HVT_BAY] : [SEQUENCE_ENTRY] - Ensuring pallet has reached HVT Bay.", getMyBayKey()));

        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(false); // Assume failure initially
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_100);
     
        int try_count = 0;
        Map<String,Object> responseReturn = null; // Initialize to null for the first check
        boolean isPalletDetected = false; // To track the boolean status from pallet_sensed_at_HVT_Bay()
        String palletSensorState = ""; // To hold the interpreted state from the sensor

        while (!ConstantConveyor.ALL_LOOP_BREAK_FLAG && !Ft.isStopProcessRequestedFtBay()) {
            Ft.logger.debug(String.format("[%s] : [PALLET_ARRIVAL_CHECK_HVT_BAY] : [RETRY] - Attempt %d to detect pallet at HVT Bay.", getMyBayKey(), try_count + 1));

            responseReturn = pallet_sensed_at_HVT_Bay();	 
            isPalletDetected = (boolean)responseReturn.get("status"); // Retrieve the boolean status
            palletSensorState = (String)responseReturn.get("responseData"); // Retrieve the interpreted state from sensor

            // Update GUI after each sensor read
            StateExecutorController.updateTestInterfaceStatusOnGui(responseReturn, ConstantConveyor.COMM_EXECUTION_STATUS_INP);

            if (isPalletDetected && Constant_IO_ActionMapping.DETECTED.equals(palletSensorState)) {  // Verify if pallet is detected
                Ft.logger.info(String.format("[%s] : [PALLET_ARRIVAL_CHECK_HVT_BAY] : [SUCCESS] - Pallet detected at HVT Bay. State: %s", getMyBayKey(), palletSensorState));
            	
            	if (ProconFeatureEnable.MOTOR_CONTROL_ENABLE) {
            		Ft.logger.info(String.format("[%s] : [PALLET_ARRIVAL_CHECK_HVT_BAY] : [Motor Off Delay 5 Sec]", getMyBayKey()));
                	
            		BayUtils.delay(5000);
    				Ft.logger.info(String.format("[%s] : [MOTOR_CONTROL] : [ENABLED] - Setting motor requirement to stop pallet at HVT Bay.", getMyBayKey()));
    				
    				 
    				List<String> motor_requirement = Constant_Motor_Requirement.FT_MOTOR_1_REQUIRED; // Assuming this motor is for stopping at HVT Bay
    				 			
    				Map<String, Object> motorResponse = new HashMap<>();
    				try {
    				    motorResponse = bayUtils.set_motor_required(getMyBayKey(), motor_requirement);
    				} catch (Exception e) {
    				    Ft.logger.error(String.format("[%s] : [MOTOR_CONTROL] : [ERROR] - Exception setting motor requirement: %s", getMyBayKey(), e.getMessage()), e);
    				    motorResponse.put("status", false); // Indicate failure
    				}

    				boolean set_motor_required = (boolean) motorResponse.getOrDefault("status", false);
    				if (set_motor_required) {
    					Ft.logger.info(String.format("[%s] : [MOTOR_CONTROL] : [SUCCESS] - Motor set to required state (stop) successfully.", getMyBayKey()));
    					bayResponse.setStatus(true);
    					bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
    				} else {
    					Ft.logger.error(String.format("[%s] : [MOTOR_CONTROL] : [FAILED] - Failed to set motor to required state (stop). Error: %s", getMyBayKey(), ConvErrorCodeMapping.ERROR_CODE_CALIB_026));
    					bayResponse.setStatus(false);
    					bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_026);
    				} 
    			} else {
    				Ft.logger.info(String.format("[%s] : [MOTOR_CONTROL] : [DISABLED] - Motor control is disabled. Assuming pallet stopped correctly.", getMyBayKey()));
    				bayResponse.setStatus(true); // If motor control is disabled, and pallet is detected, this state is successful
    				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
    			}
                // Mark GUI status check as completed after final action
                StateExecutorController.updateTestInterfaceStatusOnGui(responseReturn, ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
                break; // Exit loop on successful detection and action
            } else {
                Ft.logger.warn(String.format("[%s] : [PALLET_ARRIVAL_CHECK_HVT_BAY] : [NOT_DETECTED] - Pallet not yet detected at HVT Bay. Expected: %s, Actual: %s. Retrying...", getMyBayKey(), Constant_IO_ActionMapping.DETECTED, palletSensorState));
                bayResponse.setStatus(false);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_026);    
                BayUtils.delay(1000); // Wait for 1 second before re-checking
                try_count++;
            }
        }
        
        if (!isPalletDetected || !Constant_IO_ActionMapping.DETECTED.equals(palletSensorState)) {
            // Log final failure if loop completes without success or was interrupted
            Ft.logger.error(String.format("[%s] : [PALLET_ARRIVAL_CHECK_HVT_BAY] : [FINAL_FAILURE] - Failed to ensure pallet reached HVT Bay after multiple attempts or process stopped. Final state: %s. Error: %s", getMyBayKey(), palletSensorState, ConvErrorCodeMapping.ERROR_CODE_FT_026));
            // Ensure GUI status is updated to completed for failure case if loop exits without break
            StateExecutorController.updateTestInterfaceStatusOnGui(responseReturn, ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
        }

        // Structured log for sequence exit
        Ft.logger.info(String.format("[%s] : [PALLET_ARRIVAL_CHECK_HVT_BAY] : [SEQUENCE_EXIT] - Pallet arrival check at HVT Bay sequence completed. Final Status: %s", getMyBayKey(), bayResponse.getStatus()));
        return bayResponse;
    }
    //============================================================================================================================================  

    private Map<String,Object> pallet_sensed_at_HVT_Bay() {
        // Structured debug log for method entry
        Ft.logger.debug(String.format("[%s] : [PALLET_SENSOR_READ] : [REQUEST_ENTRY] - Reading pallet sensor status at HVT Bay.", getMyBayKey()));

        Map<String,Object> responseReturn = new HashMap<>();
		responseReturn.put("status", false); // Default boolean status
        
        TestInterfaceStatus testInterfaceStatus = new TestInterfaceStatus();
        String rawStateFromSensor = ""; // To store the raw state from the sensor
        boolean isSuccess = false; // Local boolean status for the operation
        
        //============================================================================================
        // Get port information for the pallet sensor
        IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.HV_PORT_NAME_SNSR_PALLET);
        
        if (portInfo != null) {
            Ft.logger.debug(String.format("[%s] : [PALLET_SENSOR_READ] : [PORT_INFO] - PortId: %s, ClusterId: %s, BayId: %s", getMyBayKey(), portInfo.getPortId(), portInfo.getClusterId(), portInfo.getBayId()));

            // Initialize TestInterfaceStatus for GUI
            testInterfaceStatus = new TestInterfaceStatus(
                    getMyBayKey(),
                    myBaySeqId, // Use the assigned sequence ID for this state
                    ConstantConveyor.DEVICE_TYPE_CLUSTER_INPUT, // Corrected to INPUT for sensor
                    "p1", // Assuming a fixed sequencePathId for this operation
                    "-",
                    portInfo.getPortId(),
                    ConstantBayPortNameMapping.HV_PORT_NAME_SNSR_PALLET,
                    ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
                    "Waiting", // Initial status for GUI
                    ConstantConveyor.COMM_EXECUTION_STATUS_INP
            );
            // Add to GUI (assuming StateExecutorController.addToTestStatusGui handles Platform.runLater() internally)
            int newRecordSerialNo = StateExecutorController.addToTestStatusGui(testInterfaceStatus);
            testInterfaceStatus.setSerialNo(String.valueOf(newRecordSerialNo));

            try {
                rawStateFromSensor = bayUtils.getInputDataFromBayV2(portInfo);
            } catch (Exception e) {
                Ft.logger.error(String.format("[%s] : [COMMUNICATION_ERROR] : [PALLET_SENSOR_READ] - Failed to read pallet sensor. Port: %s. Error: %s", getMyBayKey(), portInfo.getPortId(), e.getMessage()), e);
                testInterfaceStatus.setDeviceResponseStatus("Error");
                testInterfaceStatus.setDeviceResponseData("Communication Error");
                testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
                StateExecutorController.updateTestStatusGui(testInterfaceStatus);
                responseReturn.put("status", false); // Indicate failure
                responseReturn.put("responseData", rawStateFromSensor);
                responseReturn.put("testInterfaceStatus", testInterfaceStatus);
                return responseReturn;
            }
                  
            Ft.logger.debug(String.format("[%s] : [PALLET_SENSOR_READ] : [RAW_STATE] : %s", getMyBayKey(), rawStateFromSensor));

            // Interpret the raw state. Assuming OLD_OFF_NEW_ON means pallet is present.
            String interpretedState = rawStateFromSensor.equals(Constant_IO_ActionMapping.ON) ? Constant_IO_ActionMapping.DETECTED : Constant_IO_ActionMapping.NOT_DETECTED;

            if(StateExecutorController.simulateFtBayHappyPath){
				interpretedState = Constant_IO_ActionMapping.DETECTED ; // Simulate detection for happy path
				Ft.logger.debug(String.format("[%s] : [SIMULATION] : Pallet sensor status overridden to DETECTED.", getMyBayKey()));
			}
            
            // Update TestInterfaceStatus and boolean status based on the interpreted state
            if(Constant_IO_ActionMapping.DETECTED.equals(interpretedState)){
                testInterfaceStatus.setDeviceResponseStatus("Success");
                testInterfaceStatus.setDeviceResponseData("Pallet Detected");
                isSuccess = true;
            }else{
                testInterfaceStatus.setDeviceResponseStatus("Failed");
                testInterfaceStatus.setDeviceResponseData("Pallet Not Detected");
                isSuccess = false;
            }

            // Check for timeout or invalid response where the raw state matches the port ID (as seen in other files)
            if(portInfo.getPortId().equals(rawStateFromSensor)){
                testInterfaceStatus.setDeviceResponseData("TimeOut");
                Ft.logger.warn(String.format("[%s] : [PALLET_SENSOR_READ] : [TIMEOUT] - Pallet sensor read timed out or invalid response. Raw state: %s", getMyBayKey(), rawStateFromSensor));
                isSuccess = false; // A timeout implies failure
            }

            testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
            StateExecutorController.updateTestStatusGui(testInterfaceStatus); // Update GUI

            responseReturn.put("status", isSuccess); // Correctly return boolean status
            responseReturn.put("responseData", interpretedState); // Return the interpreted state
            responseReturn.put("testInterfaceStatus", testInterfaceStatus); // Include the TestInterfaceStatus object

        } else {
            // Log a clear error if the port information is missing
            Ft.logger.error(String.format("[%s] : [CONFIG_ERROR] : [PALLET_SENSOR_READ] - Input port not found for pallet sensor: %s", getMyBayKey(), ConstantBayPortNameMapping.HV_PORT_NAME_SNSR_PALLET));
            testInterfaceStatus.setBayName(getMyBayKey()); // Initialize for error reporting
            testInterfaceStatus.setDeviceResponseStatus("Failed");
            testInterfaceStatus.setDeviceResponseData("I/P port not found");
            testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
            // Add or update GUI with config error status
            StateExecutorController.addToTestStatusGui(testInterfaceStatus); 
            StateExecutorController.updateTestStatusGui(testInterfaceStatus);
            responseReturn.put("status", false); // Indicate failure due to missing config
            responseReturn.put("responseData", "CONFIG_ERROR");
            responseReturn.put("testInterfaceStatus", testInterfaceStatus);
        }
        
        // Structured debug log for method exit
        Ft.logger.debug(String.format("[%s] : [PALLET_SENSOR_READ] : [REQUEST_EXIT] - Pallet sensor read completed. Status: %s", getMyBayKey(), isSuccess));
        return responseReturn;
    }
}
