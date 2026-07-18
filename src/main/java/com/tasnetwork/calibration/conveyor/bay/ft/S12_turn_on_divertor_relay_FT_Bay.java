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
 * State class responsible for turning on the divertor relay for FT Bay.
 */
public class S12_turn_on_divertor_relay_FT_Bay implements FtBayState {


    private String myBaySeqId = ConstantBayStateManage.FT_BAY_HP_SEQ_14; 

    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        // Structured log entry for sequence start
        Ft.logger.info(String.format("[%s] : [DIVERTOR_RELAY_ON] : [SEQUENCE_ENTRY] - Turning on Divertor Relay.", getMyBayKey()));

        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true); // Assume success initially
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);     

        Map<String,Object> responseReturn = turn_on_divertor_relay_FT_Bay();	 
		boolean turn_on_divertor_relay_status = (boolean)responseReturn.get("status");
        
        StateExecutorController.updateTestInterfaceStatusOnGui(responseReturn, ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);

        if (turn_on_divertor_relay_status) {
            Ft.logger.info(String.format("[%s] : [DIVERTOR_RELAY_ON] : [SUCCESS] - Divertor Relay Turned On successfully.", getMyBayKey()));
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        } else {
            Ft.logger.error(String.format("[%s] : [DIVERTOR_RELAY_ON] : [FAILED] - Failed to Turn On Divertor Relay. Error: %s", getMyBayKey(), ConvErrorCodeMapping.ERROR_CODE_FT_017));
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_017);
        }

        // Structured log for sequence exit
        Ft.logger.info(String.format("[%s] : [DIVERTOR_RELAY_ON] : [SEQUENCE_EXIT] - Divertor Relay sequence completed. Final Status: %s", getMyBayKey(), bayResponse.getStatus()));
        return bayResponse;
    }

    //============================================================================================================================================  

    private Map<String,Object> turn_on_divertor_relay_FT_Bay() {
        // Structured debug log for method entry
        Ft.logger.debug(String.format("[%s] : [DIVERTOR_RELAY_COMMAND] : [REQUEST_ENTRY] - Sending command to turn on Divertor Relay.", getMyBayKey()));

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<>();
		responseReturn.put("status", false); // Default status

        TestInterfaceStatus testInterfaceStatus = new TestInterfaceStatus();
        String state = ""; // To store the response state from the bay controller
        
        // Get port information for the divertor relay
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_DIVERTOR_RELAY);
        
        if (portInfo != null) {
            Ft.logger.debug(String.format("[%s] : [DIVERTOR_RELAY_COMMAND] : [PORT_INFO] - PortId: %s, ClusterId: %s, BayId: %s", getMyBayKey(), portInfo.getPortId(), portInfo.getClusterId(), portInfo.getBayId()));

            // Initialize TestInterfaceStatus for GUI
            testInterfaceStatus = new TestInterfaceStatus(
                    getMyBayKey(),
                    myBaySeqId, // Use the assigned sequence ID for this state
                    ConstantConveyor.DEVICE_TYPE_CLUSTER_OUTPUT,
                    "p1", // Assuming a fixed sequencePathId for this operation within its scope
                    "-",
                    portInfo.getPortId(),
                    ConstantBayPortNameMapping.FT_PORT_NAME_DIVERTOR_RELAY,
                    ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
                    "Waiting", // Initial status for GUI
                    ConstantConveyor.COMM_EXECUTION_STATUS_INP
            );

            int newRecordSerialNo = StateExecutorController.addToTestStatusGui(testInterfaceStatus);
            testInterfaceStatus.setSerialNo(String.valueOf(newRecordSerialNo));

            try {
            	BayUtils bayUtils = new BayUtils();
                // Send command to turn ON the divertor relay
                state = bayUtils.setOutputDataToBay(portInfo.getClusterId(), 
                                              portInfo.getBayId(), 
                                              portInfo.getPortId(),
                                              Constant_IO_ActionMapping.OPEN); 

                // Log the raw state received from the control system for debugging
                Ft.logger.debug(String.format("[%s] : [DIVERTOR_RELAY_COMMAND] : [RAW_STATE] : %s", getMyBayKey(), state));

                status = state.equals(Constant_IO_ActionMapping.OPEN); 

                Ft.logger.debug(String.format("[%s] : [DIVERTOR_RELAY_COMMAND] : [STATUS_CHECK] - Raw state: %s, Interpreted status: %s", getMyBayKey(), state, status));
                
                if (status) {
                    testInterfaceStatus.setDeviceResponseStatus("Success");
                    testInterfaceStatus.setDeviceResponseData("Divertor Relay ON");
                } else {
                    testInterfaceStatus.setDeviceResponseStatus("Failed");
                    testInterfaceStatus.setDeviceResponseData(state.isEmpty() ? "No Response" : state);
                }
                
                // Simulate happy path if enabled
                if(StateExecutorController.simulateFtBayHappyPath){
                    status = true; 
                    testInterfaceStatus.setDeviceResponseStatus("Success"); // Ensure GUI reflects simulation success
                    testInterfaceStatus.setDeviceResponseData("Simulated Success - Divertor Relay ON");
                    Ft.logger.debug(String.format("[%s] : [SIMULATION] : Divertor Relay ON status overridden to TRUE.", getMyBayKey()));
                }

                testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
                StateExecutorController.updateTestStatusGui(testInterfaceStatus); // Update GUI

            } catch (Exception e) {
                // Log any exceptions during output data transmission
                Ft.logger.error(String.format("[%s] : [COMMUNICATION_ERROR] : [DIVERTOR_RELAY_COMMAND] - Failed to send ON command to Divertor Relay. Port: %s. Error: %s", getMyBayKey(), portInfo.getPortId(), e.getMessage()), e);
                testInterfaceStatus.setDeviceResponseStatus("Error");
                testInterfaceStatus.setDeviceResponseData("Communication Error");
                testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
                StateExecutorController.updateTestStatusGui(testInterfaceStatus); // Update GUI with error status
                status = false; // Mark operation as failed
            }
        } else {
            // Log a clear error if the port information is missing
            Ft.logger.error(String.format("[%s] : [CONFIG_ERROR] : [DIVERTOR_RELAY_COMMAND] - Output port not found for Divertor Relay: %s", getMyBayKey(), ConstantBayPortNameMapping.FT_PORT_NAME_DIVERTOR_RELAY));
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
        responseReturn.put("testInterfaceStatus", testInterfaceStatus); 
        
        // Structured debug log for method exit
        Ft.logger.debug(String.format("[%s] : [DIVERTOR_RELAY_COMMAND] : [REQUEST_EXIT] - Divertor Relay ON request completed. Status: %s, Final State: %s", getMyBayKey(), status, state));
        return responseReturn;
    }
}
