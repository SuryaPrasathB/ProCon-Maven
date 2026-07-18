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

/**
 * State class responsible for releasing the pallet to the HVT Bay.
 */
public class S14_let_the_pallet_to_HVT_Bay implements FtBayState {
	BayUtils bayUtils = new BayUtils();
	
    private String myBaySeqId = ConstantBayStateManage.FT_BAY_HP_SEQ_16; // Example, adjust if a more specific one exists
    
    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        // Structured log entry for sequence start
        Ft.logger.info(String.format("[%s] : [PALLET_RELEASE_HVT_BAY] : [SEQUENCE_ENTRY] - Releasing pallet to HVT Bay.", getMyBayKey()));

        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true); // Assume success initially
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

        //=============================================================
        Map<String,Object> responseReturnOpen = open_StopLatch_FtBay();	 
		boolean openStopLatchSuccess = (boolean)responseReturnOpen.get("status");
        
        // Update GUI for the open operation
        StateExecutorController.updateTestInterfaceStatusOnGui(responseReturnOpen, ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);

        if(openStopLatchSuccess) {
            Ft.logger.info(String.format("[%s] : [PALLET_RELEASE_HVT_BAY] : [STOPPER_OPENED] - Stopper opened successfully. Delaying for pallet to pass.", getMyBayKey()));
            BayUtils.delay(4000); // Blocking delay, ensure handleRequest() is called from a background thread.

            Map<String,Object> responseReturnClose = close_StopLatch_FtBay();	 
    		boolean closeStopLatchSuccess = (boolean)responseReturnClose.get("status");
            
            // Update GUI for the close operation
            StateExecutorController.updateTestInterfaceStatusOnGui(responseReturnClose, ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);

        	if(closeStopLatchSuccess) {
        		Ft.logger.info(String.format("[%s] : [PALLET_RELEASE_HVT_BAY] : [STOPPER_CYCLE_SUCCESS] - Stopper opened and closed successfully.", getMyBayKey()));
        		bayResponse.setStatus(true);
        		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        	} else {
        		Ft.logger.error(String.format("[%s] : [PALLET_RELEASE_HVT_BAY] : [STOPPER_CLOSE_FAILED] - Failed to close stopper. Error: %s", getMyBayKey(), ConvErrorCodeMapping.ERROR_CODE_FT_023));
        		bayResponse.setStatus(false);
        		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_023); 
        	}
        } else {
        	Ft.logger.error(String.format("[%s] : [PALLET_RELEASE_HVT_BAY] : [STOPPER_OPEN_FAILED] - Failed to open stopper. Error: %s", getMyBayKey(), ConvErrorCodeMapping.ERROR_CODE_FT_007));
        	bayResponse.setStatus(false);
        	bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_007);
        }
        //=============================================================

        // Structured log for sequence exit
        Ft.logger.info(String.format("[%s] : [PALLET_RELEASE_HVT_BAY] : [SEQUENCE_EXIT] - Pallet release to HVT Bay sequence completed. Final Status: %s", getMyBayKey(), bayResponse.getStatus()));
        return bayResponse;
    }

    //============================================================================================================================================

    private Map<String,Object> open_StopLatch_FtBay() {
        // Structured debug log for method entry
        Ft.logger.debug(String.format("[%s] : [STOPPER_OPEN_COMMAND] : [REQUEST_ENTRY] - Sending command to open stopper latch.", getMyBayKey()));

        boolean status = false; // Local boolean status for this operation
        Map<String,Object> responseReturn = new HashMap<>();
		responseReturn.put("status", false); // Default boolean status
        
        TestInterfaceStatus testInterfaceStatus = new TestInterfaceStatus();
        String state = ""; // To store the response state from the bay controller
        
        //============================================================================================		 
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_STPR_AT);

        if (portInfo != null) {
            Ft.logger.debug(String.format("[%s] : [STOPPER_OPEN_COMMAND] : [PORT_INFO] - PortId: %s, ClusterId: %s, BayId: %s", getMyBayKey(), portInfo.getPortId(), portInfo.getClusterId(), portInfo.getBayId()));

            // Initialize TestInterfaceStatus for GUI
            testInterfaceStatus = new TestInterfaceStatus(
                    getMyBayKey(),
                    myBaySeqId, // Use the assigned sequence ID for this state
                    ConstantConveyor.DEVICE_TYPE_CLUSTER_OUTPUT,
                    "p1", // Assuming a fixed sequencePathId for this operation
                    "-",
                    portInfo.getPortId(),
                    ConstantBayPortNameMapping.FT_PORT_NAME_STPR_AT,
                    ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
                    "Waiting", // Initial status for GUI
                    ConstantConveyor.COMM_EXECUTION_STATUS_INP
            );

            int newRecordSerialNo = StateExecutorController.addToTestStatusGui(testInterfaceStatus);
            testInterfaceStatus.setSerialNo(String.valueOf(newRecordSerialNo));

            try {
                // Send command to open the stopper
                state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                                                portInfo.getBayId(),
                                                portInfo.getPortId(),
                                                Constant_IO_ActionMapping.CLOSE);
                
                // Log the raw state received from the control system for debugging
                Ft.logger.debug(String.format("[%s] : [STOPPER_OPEN_COMMAND] : [RAW_STATE] : %s", getMyBayKey(), state));

                // Determine if the operation was successful based on the returned state
                status = state.equals(Constant_IO_ActionMapping.CLOSE); 

                Ft.logger.debug(String.format("[%s] : [STOPPER_OPEN_COMMAND] : [STATUS_CHECK] - Raw state: %s, Interpreted status: %s", getMyBayKey(), state, status));
                
                if (status) {
                    testInterfaceStatus.setDeviceResponseStatus("Success");
                    testInterfaceStatus.setDeviceResponseData("Stopper Opened");
                } else {
                    testInterfaceStatus.setDeviceResponseStatus("Failed");
                    testInterfaceStatus.setDeviceResponseData(state.isEmpty() ? "No Response" : state);
                }

                // Check for timeout or invalid response where the raw state matches the port ID (as seen in other files)
                if(portInfo.getPortId().equals(state)){
                    testInterfaceStatus.setDeviceResponseData("TimeOut");
                    Ft.logger.warn(String.format("[%s] : [STOPPER_OPEN_COMMAND] : [TIMEOUT] - Stopper open command timed out or invalid response. Raw state: %s", getMyBayKey(), state));
                    status = false; // A timeout implies failure
                }

                // Simulate happy path if enabled
                if(StateExecutorController.simulateFtBayHappyPath){
                    status = true; 
                    testInterfaceStatus.setDeviceResponseStatus("Success"); // Ensure GUI reflects simulation success
                    testInterfaceStatus.setDeviceResponseData("Simulated Success - Stopper Opened");
                    Ft.logger.debug(String.format("[%s] : [SIMULATION] : Stopper Open status overridden to TRUE.", getMyBayKey()));
                }

                testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
                StateExecutorController.updateTestStatusGui(testInterfaceStatus); // Update GUI
                
            } catch (Exception e) {
                // Log any exceptions during output data transmission
                Ft.logger.error(String.format("[%s] : [COMMUNICATION_ERROR] : [STOPPER_OPEN_COMMAND] - Failed to send OPEN command to Stopper. Port: %s. Error: %s", getMyBayKey(), portInfo.getPortId(), e.getMessage()), e);
                testInterfaceStatus.setDeviceResponseStatus("Error");
                testInterfaceStatus.setDeviceResponseData("Communication Error");
                testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
                StateExecutorController.updateTestStatusGui(testInterfaceStatus); // Update GUI with error status
                status = false; // Mark operation as failed
            }
        } else {
            // Log a clear error if the port information is missing
            Ft.logger.error(String.format("[%s] : [CONFIG_ERROR] : [STOPPER_OPEN_COMMAND] - Output port not found for Stopper: %s", getMyBayKey(), ConstantBayPortNameMapping.FT_PORT_NAME_STPR_AT));
            testInterfaceStatus.setBayName(getMyBayKey()); // Initialize for error reporting
            testInterfaceStatus.setDeviceResponseStatus("Failed");
            testInterfaceStatus.setDeviceResponseData("O/P port not found");
            testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
            // Add or update GUI with config error status
            StateExecutorController.addToTestStatusGui(testInterfaceStatus); 
            StateExecutorController.updateTestStatusGui(testInterfaceStatus);
            status = false; // Mark operation as failed
        }
        
		responseReturn.put("status", status);
        responseReturn.put("responseData", state); // Include the state/response data
        responseReturn.put("testInterfaceStatus", testInterfaceStatus); // Include the TestInterfaceStatus object

        // Structured debug log for method exit
        Ft.logger.debug(String.format("[%s] : [STOPPER_OPEN_COMMAND] : [REQUEST_EXIT] - Stopper Open request completed. Status: %s, Final State: %s", getMyBayKey(), status, state));
        return responseReturn;
    }
 
    //============================================================================================================================================

    private Map<String,Object> close_StopLatch_FtBay() {
        // Structured debug log for method entry
        Ft.logger.debug(String.format("[%s] : [STOPPER_CLOSE_COMMAND] : [REQUEST_ENTRY] - Sending command to close stopper latch.", getMyBayKey()));

        boolean status = false; // Local boolean status for this operation
        Map<String,Object> responseReturn = new HashMap<>();
		responseReturn.put("status", false); // Default boolean status
        
        TestInterfaceStatus testInterfaceStatus = new TestInterfaceStatus();
        String state = ""; // To store the response state from the bay controller

        //============================================================================================		 
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_STPR_AT);

        if (portInfo != null) {
            Ft.logger.debug(String.format("[%s] : [STOPPER_CLOSE_COMMAND] : [PORT_INFO] - PortId: %s, ClusterId: %s, BayId: %s", getMyBayKey(), portInfo.getPortId(), portInfo.getClusterId(), portInfo.getBayId()));

            // Initialize TestInterfaceStatus for GUI
            testInterfaceStatus = new TestInterfaceStatus(
                    getMyBayKey(),
                    myBaySeqId, // Use the assigned sequence ID for this state
                    ConstantConveyor.DEVICE_TYPE_CLUSTER_OUTPUT,
                    "p1", // Assuming a fixed sequencePathId for this operation
                    "-",
                    portInfo.getPortId(),
                    ConstantBayPortNameMapping.FT_PORT_NAME_STPR_AT,
                    ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
                    "Waiting", // Initial status for GUI
                    ConstantConveyor.COMM_EXECUTION_STATUS_INP
            );
            // Add to GUI (assuming StateExecutorController.addToTestStatusGui handles Platform.runLater() internally)
            int newRecordSerialNo = StateExecutorController.addToTestStatusGui(testInterfaceStatus);
            testInterfaceStatus.setSerialNo(String.valueOf(newRecordSerialNo));

            try {
                // Send command to close the stopper
                state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                                                portInfo.getBayId(),
                                                portInfo.getPortId(),
                                                Constant_IO_ActionMapping.OPEN);
                
                // Log the raw state received from the control system for debugging
                Ft.logger.debug(String.format("[%s] : [STOPPER_CLOSE_COMMAND] : [RAW_STATE] : %s", getMyBayKey(), state));

                // Determine if the operation was successful based on the returned state
                status = state.equals(Constant_IO_ActionMapping.OPEN); 
                
                Ft.logger.debug(String.format("[%s] : [STOPPER_CLOSE_COMMAND] : [STATUS_CHECK] - Raw state: %s, Interpreted status: %s", getMyBayKey(), state, status));
                
                if (status) {
                    testInterfaceStatus.setDeviceResponseStatus("Success");
                    testInterfaceStatus.setDeviceResponseData("Stopper Closed");
                } else {
                    testInterfaceStatus.setDeviceResponseStatus("Failed");
                    testInterfaceStatus.setDeviceResponseData(state.isEmpty() ? "No Response" : state);
                }

                // Check for timeout or invalid response where the raw state matches the port ID (as seen in other files)
                if(portInfo.getPortId().equals(state)){
                    testInterfaceStatus.setDeviceResponseData("TimeOut");
                    Ft.logger.warn(String.format("[%s] : [STOPPER_CLOSE_COMMAND] : [TIMEOUT] - Stopper close command timed out or invalid response. Raw state: %s", getMyBayKey(), state));
                    status = false; // A timeout implies failure
                }

                // Simulate happy path if enabled
                if(StateExecutorController.simulateFtBayHappyPath){
                    status = true; 
                    testInterfaceStatus.setDeviceResponseStatus("Success"); // Ensure GUI reflects simulation success
                    testInterfaceStatus.setDeviceResponseData("Simulated Success - Stopper Closed");
                    Ft.logger.debug(String.format("[%s] : [SIMULATION] : Stopper Close status overridden to TRUE.", getMyBayKey()));
                }

                testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
                StateExecutorController.updateTestStatusGui(testInterfaceStatus); // Update GUI
                
            } catch (Exception e) {
                // Log any exceptions during output data transmission
                Ft.logger.error(String.format("[%s] : [COMMUNICATION_ERROR] : [STOPPER_CLOSE_COMMAND] - Failed to send CLOSE command to Stopper. Port: %s. Error: %s", getMyBayKey(), portInfo.getPortId(), e.getMessage()), e);
                testInterfaceStatus.setDeviceResponseStatus("Error");
                testInterfaceStatus.setDeviceResponseData("Communication Error");
                testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
                StateExecutorController.updateTestStatusGui(testInterfaceStatus); // Update GUI with error status
                status = false; // Mark operation as failed
            }
        } else {
            // Log a clear error if the port information is missing
            Ft.logger.error(String.format("[%s] : [CONFIG_ERROR] : [STOPPER_CLOSE_COMMAND] - Output port not found for Stopper: %s", getMyBayKey(), ConstantBayPortNameMapping.FT_PORT_NAME_STPR_AT));
            testInterfaceStatus.setBayName(getMyBayKey()); // Initialize for error reporting
            testInterfaceStatus.setDeviceResponseStatus("Failed");
            testInterfaceStatus.setDeviceResponseData("O/P port not found");
            testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
            // Add or update GUI with config error status
            StateExecutorController.addToTestStatusGui(testInterfaceStatus); 
            StateExecutorController.updateTestStatusGui(testInterfaceStatus);
            status = false; // Mark operation as failed
        }
        
		responseReturn.put("status", status);
        responseReturn.put("responseData", state); // Include the state/response data
        responseReturn.put("testInterfaceStatus", testInterfaceStatus); // Include the TestInterfaceStatus object

        // Structured debug log for method exit
        Ft.logger.debug(String.format("[%s] : [STOPPER_CLOSE_COMMAND] : [REQUEST_EXIT] - Stopper Close request completed. Status: %s, Final State: %s", getMyBayKey(), status, state));
        return responseReturn;
    }
}
