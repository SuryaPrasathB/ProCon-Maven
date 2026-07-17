package com.tasnetwork.calibration.conveyor.bay.ft;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage; // Import for ConstantBayStateManage
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor; // Import for ConstantConveyor
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus; // Import for TestInterfaceStatus

public class S13_ensure_divertor_relay_turned_on_FT_Bay implements FtBayState {

	BayUtils bayUtils = new BayUtils();
	
    // Assuming a sequence ID relevant to this operation might be needed for TestInterfaceStatus
    private String myBaySeqId = ConstantBayStateManage.FT_BAY_HP_SEQ_15; // Example, adjust if a more specific one exists

    public String getMyBayKey() {
        return myBayKey;
    }

    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        // Structured log entry for sequence start
        Ft.logger.info(String.format("[%s] : [DIVERTOR_RELAY_ON_CHECK] : [SEQUENCE_ENTRY] - Ensuring Divertor Relay is turned on.", getMyBayKey()));

        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(false); // Assume failure initially
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_100);
        
        int try_count = 0;
        Map<String,Object> responseReturn = null; // Initialize to null for the first check
        boolean isRelayTurnedOnSuccessfully = false; // To track the boolean status from ftBay_DivertorRelay_Status()
        String divertorRelayPresentState = ""; // To hold the interpreted state from the sensor

        while (try_count <= 3) { // Loop up to 4 attempts
            Ft.logger.debug(String.format("[%s] : [DIVERTOR_RELAY_ON_CHECK] : [RETRY] - Attempt %d of 4 to verify Divertor Relay status.", getMyBayKey(), try_count + 1));

            responseReturn = ftBay_DivertorRelay_Status();
            isRelayTurnedOnSuccessfully = (boolean)responseReturn.get("status"); // Retrieve the boolean status
            divertorRelayPresentState = (String)responseReturn.get("responseData"); // Retrieve the interpreted state from sensor
            
            // Assuming StateExecutorController.updateTestInterfaceStatusOnGui handles Platform.runLater() internally
            StateExecutorController.updateTestInterfaceStatusOnGui(responseReturn, ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);

            // Check if relay is turned on and the status indicates success
            if (isRelayTurnedOnSuccessfully && Constant_IO_ActionMapping.OPEN.equals(divertorRelayPresentState)) {  
                Ft.logger.info(String.format("[%s] : [DIVERTOR_RELAY_ON_CHECK] : [SUCCESS] - Divertor Relay confirmed ON. State: %s", getMyBayKey(), divertorRelayPresentState));
                bayResponse.setStatus(true);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
                break; // Exit loop on success
            } else {
                Ft.logger.warn(String.format("[%s] : [DIVERTOR_RELAY_ON_CHECK] : [MISMATCH] - Divertor Relay status is not ON. Expected: %s, Actual: %s. Retrying...", getMyBayKey(), Constant_IO_ActionMapping.OPEN, divertorRelayPresentState));
                bayResponse.setStatus(false);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_018);    
                BayUtils.delay(1000); // Blocking delay; ensure handleRequest() is called from a background thread.
                try_count++;
            }
        }

        if (!isRelayTurnedOnSuccessfully || !Constant_IO_ActionMapping.OPEN.equals(divertorRelayPresentState)) {
            // Log final failure if loop completes without success
            Ft.logger.error(String.format("[%s] : [DIVERTOR_RELAY_ON_CHECK] : [FINAL_FAILURE] - Failed to ensure Divertor Relay is turned on after %d attempts. Final state: %s. Error: %s", getMyBayKey(), try_count, divertorRelayPresentState, ConvErrorCodeMapping.ERROR_CODE_FT_018));
        }
        
        // Structured log for sequence exit
        Ft.logger.info(String.format("[%s] : [DIVERTOR_RELAY_ON_CHECK] : [SEQUENCE_EXIT] - Divertor Relay status check sequence completed. Final Status: %s", getMyBayKey(), bayResponse.getStatus()));
        return bayResponse;
    }

    //============================================================================================================================================  

    private Map<String,Object> ftBay_DivertorRelay_Status() {
        // Structured debug log for method entry
        Ft.logger.debug(String.format("[%s] : [DIVERTOR_RELAY_SENSOR_READ] : [REQUEST_ENTRY] - Reading Divertor Relay status sensor.", getMyBayKey()));

        Map<String,Object> responseReturn = new HashMap<>();
		responseReturn.put("status", false); // Default boolean status
        
        TestInterfaceStatus testInterfaceStatus = new TestInterfaceStatus();
        String rawStateFromSensor = ""; // To store the raw state from the sensor
        boolean isSuccess = false; // Local boolean status for the operation
        
        //============================================================================================
        // Note: This method is named "_Status" and seems to read a sensor.
        // However, it uses `BayUtils.getOutputPortDetails` which implies controlling an output.
        // If this is truly a sensor read for the *state* of the divertor relay, it should use an InputPort.
        // Assuming there's a sensor (input port) associated with the divertor relay's state.
        // If not, this logic might be checking the *command status* which is less reliable than actual sensor feedback.
        // For now, I will assume it's reading the state of the *output pin* itself, which is possible but not a sensor.
        // If there's a separate sensor, `ConstantBayPortNameMapping.FT_PORT_NAME_DIVERTOR_RELAY_STATUS_SENSOR` would be more appropriate.
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_DIVERTOR_RELAY);
        
        if (portInfo != null) {
            Ft.logger.debug(String.format("[%s] : [DIVERTOR_RELAY_SENSOR_READ] : [PORT_INFO] - PortId: %s, ClusterId: %s, BayId: %s", getMyBayKey(), portInfo.getPortId(), portInfo.getClusterId(), portInfo.getBayId()));
            
            // Initialize TestInterfaceStatus for GUI
            testInterfaceStatus = new TestInterfaceStatus(
                    getMyBayKey(),
                    myBaySeqId, // Use the assigned sequence ID for this state
                    ConstantConveyor.DEVICE_TYPE_CLUSTER_OUTPUT, // Still considering it an output for now based on port details call
                    "p1", // Assuming a fixed sequencePathId for this operation
                    "-",
                    portInfo.getPortId(),
                    ConstantBayPortNameMapping.FT_PORT_NAME_DIVERTOR_RELAY,
                    ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
                    "Waiting", // Initial status for GUI
                    ConstantConveyor.COMM_EXECUTION_STATUS_INP
            );
            // Add to GUI (assuming StateExecutorController.addToTestStatusGui handles Platform.runLater() internally)
            int newRecordSerialNo = StateExecutorController.addToTestStatusGui(testInterfaceStatus);
            testInterfaceStatus.setSerialNo(String.valueOf(newRecordSerialNo));

            try {
                // Attempt to read the current state of the output pin
                rawStateFromSensor = bayUtils.getInputDataFromBayV2(portInfo); 
            } catch (Exception e) {
                // Log any exceptions during input data retrieval
                Ft.logger.error(String.format("[%s] : [COMMUNICATION_ERROR] : [DIVERTOR_RELAY_SENSOR_READ] - Failed to get status from Divertor Relay port. Port: %s. Error: %s", getMyBayKey(), portInfo.getPortId(), e.getMessage()), e);
                testInterfaceStatus.setDeviceResponseStatus("Error");
                testInterfaceStatus.setDeviceResponseData("Communication Error");
                testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
                StateExecutorController.updateTestStatusGui(testInterfaceStatus); // Update GUI with error status
                responseReturn.put("status", false); // Indicate failure
                responseReturn.put("responseData", rawStateFromSensor);
                responseReturn.put("testInterfaceStatus", testInterfaceStatus);
                return responseReturn;
            }
            
            Ft.logger.debug(String.format("[%s] : [DIVERTOR_RELAY_SENSOR_READ] : [RAW_STATE] : %s", getMyBayKey(), rawStateFromSensor));
            
            // Interpret the raw state. Assuming OLD_OFF_NEW_ON indicates an "active" or "ON" state from the sensor.
            String interpretedState = rawStateFromSensor.equals(Constant_IO_ActionMapping.ON) ? Constant_IO_ActionMapping.OPEN : Constant_IO_ActionMapping.CLOSE;

            Ft.logger.debug(String.format("[%s] : [DIVERTOR_RELAY_SENSOR_READ] : [INTERPRETED_STATE] : %s", getMyBayKey(), interpretedState));

            if(StateExecutorController.simulateFtBayHappyPath){
                interpretedState = Constant_IO_ActionMapping.OPEN; // Simulate "ON" state
                Ft.logger.debug(String.format("[%s] : [SIMULATION] : Divertor Relay status overridden to ON (simulated).", getMyBayKey()));
            }
            
            // Update TestInterfaceStatus and boolean status based on the interpreted state
            if(Constant_IO_ActionMapping.OPEN.equals(interpretedState)){
                testInterfaceStatus.setDeviceResponseStatus("Success");
                testInterfaceStatus.setDeviceResponseData("Divertor Relay ON");
                isSuccess = true;
            }else{
                testInterfaceStatus.setDeviceResponseStatus("Failed");
                testInterfaceStatus.setDeviceResponseData("Divertor Relay OFF / Timeout");
                isSuccess = false;
            }

            // Check for timeout or invalid response where the raw state matches the port ID (as seen in other files)
            if(portInfo.getPortId().equals(rawStateFromSensor)){
                testInterfaceStatus.setDeviceResponseData("TimeOut");
                Ft.logger.warn(String.format("[%s] : [DIVERTOR_RELAY_SENSOR_READ] : [TIMEOUT] - Divertor Relay status read timed out or invalid response. Raw state: %s", getMyBayKey(), rawStateFromSensor));
                isSuccess = false; // A timeout implies failure
            }

            testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
            StateExecutorController.updateTestStatusGui(testInterfaceStatus); // Update GUI
            
            responseReturn.put("status", isSuccess); // Correctly return boolean status
            responseReturn.put("responseData", interpretedState); // Return the interpreted state
            responseReturn.put("testInterfaceStatus", testInterfaceStatus); // Return the TestInterfaceStatus object

        } else {
            // Log a clear error if the port information is missing
            Ft.logger.error(String.format("[%s] : [CONFIG_ERROR] : [DIVERTOR_RELAY_SENSOR_READ] - Output port not found for Divertor Relay Status: %s", getMyBayKey(), ConstantBayPortNameMapping.FT_PORT_NAME_DIVERTOR_RELAY));
            testInterfaceStatus.setBayName(getMyBayKey()); // Initialize for error reporting
            testInterfaceStatus.setDeviceResponseStatus("Failed");
            testInterfaceStatus.setDeviceResponseData("O/P port not found");
            testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
            // Add or update GUI with config error status
            StateExecutorController.addToTestStatusGui(testInterfaceStatus); 
            StateExecutorController.updateTestStatusGui(testInterfaceStatus);
            responseReturn.put("status", false); // Indicate failure due to missing config
            responseReturn.put("responseData", "CONFIG_ERROR");
            responseReturn.put("testInterfaceStatus", testInterfaceStatus);
        }
        
        // Structured debug log for method exit
        Ft.logger.debug(String.format("[%s] : [DIVERTOR_RELAY_SENSOR_READ] : [REQUEST_EXIT] - Divertor Relay status read completed. Status: %s", getMyBayKey(), isSuccess));
        return responseReturn;
    }
}
