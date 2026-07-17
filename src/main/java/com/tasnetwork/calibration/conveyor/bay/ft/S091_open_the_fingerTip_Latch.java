package com.tasnetwork.calibration.conveyor.bay.ft;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S091_open_the_fingerTip_Latch implements FtBayState {

	BayUtils bayUtils = new BayUtils();
	
	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	public String getMyBayKey() {
		return myBayKey;
	}

	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		// Structured log entry for sequence start, corrected class name
		Ft.logger.info(String.format("[%s] : [FINGERTIP_LATCH_OPEN] : [SEQUENCE_ENTRY] - Starting sequence to open the fingertip latch.", getMyBayKey()));

		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true); // Assume success initially
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		setSequencePathId("p1"); // Set sequence path ID for this operation
		setPalletAvailableTest_I_F_Status (null); // Resetting for current operation

		Map<String,Object> responseReturn = open_FingerTipLatch_FtBay();	 
		// Assuming StateExecutorController.updateTestInterfaceStatusOnGui handles Platform.runLater() internally
		StateExecutorController.updateTestInterfaceStatusOnGui(responseReturn,ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);

		boolean openFingerTipLatchResult = (boolean)responseReturn.get("status");

		if (openFingerTipLatchResult) {
			// Corrected log message to reflect current class
			Ft.logger.info(String.format("[%s] : [FINGERTIP_LATCH_OPEN] : [SUCCESS] - Finger Tip Latch Opened successfully.", getMyBayKey()));
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			// Corrected log message to reflect current class
			Ft.logger.error(String.format("[%s] : [FINGERTIP_LATCH_OPEN] : [FAILED] - Failed to Open Finger Tip Latch. Error: %s", getMyBayKey(), ConvErrorCodeMapping.ERROR_CODE_FT_010));
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_010);
		}

		// Structured log for sequence exit, corrected class name
		Ft.logger.info(String.format("[%s] : [FINGERTIP_LATCH_OPEN] : [SEQUENCE_EXIT] - Fingertip latch open sequence completed.", getMyBayKey()));
		return bayResponse;
	}
	//============================================================================================================================================  

	private Map<String, Object> open_FingerTipLatch_FtBay() {
		// Structured debug log for method entry, corrected class name
		Ft.logger.debug(String.format("[%s] : [FINGERTIP_LATCH_COMMAND] : [REQUEST_ENTRY] - Requesting to open the fingertip latch.", getMyBayKey()));

		boolean status = false; // Local boolean status for this operation
		Map<String,Object> responseReturn = new HashMap<>();
		responseReturn.put("status", false); // Default boolean status

		TestInterfaceStatus testIntefaceStatus = new TestInterfaceStatus();
		String state = ""; // To store the response state from the bay controller

		IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_FINGER_TIP);
		
		if (portInfo != null) {
			Ft.logger.debug(String.format("[%s] : [FINGERTIP_LATCH_COMMAND] : [PORT_INFO] - PortId: %s, ClusterId: %s, BayId: %s", getMyBayKey(), portInfo.getPortId(), portInfo.getClusterId(), portInfo.getBayId()));

			// Initialize TestInterfaceStatus for GUI
			testIntefaceStatus = new TestInterfaceStatus(
					getMyBayKey(), // Use getMyBayKey() for consistency
					ConstantBayStateManage.FT_BAY_HP_SEQ_12, // Assuming this sequence ID is correct
					ConstantConveyor.DEVICE_TYPE_CLUSTER_OUTPUT,
					getSequencePathId(),
					"-",
					portInfo.getPortId(),
					ConstantBayPortNameMapping.FT_PORT_NAME_FINGER_TIP,
					ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
					"Waiting",
					ConstantConveyor.COMM_EXECUTION_STATUS_INP
					);
			
			// Add to GUI (assuming StateExecutorController.addToTestStatusGui handles Platform.runLater() internally)
			StateExecutorController.addToTestStatusGui(testIntefaceStatus);

			try {
				// Command to open the fingertip latch
				// Assuming OLD_OPEN_NEW_CLOSE is the correct action to "open" the latch
				state = bayUtils.setOutputDataToBay(portInfo.getClusterId(), 
						portInfo.getBayId(), 
						portInfo.getPortId(),
						Constant_IO_ActionMapping.CLOSE); // Send command to open latch

				// Log the raw state received from the control system for debugging
				Ft.logger.debug(String.format("[%s] : [FINGERTIP_LATCH_COMMAND] : [RAW_STATE] : %s", getMyBayKey(), state));
				
				// Corrected logic: status should be true if the returned state matches the 'open' state.
				// Assuming Constant_IO_ActionMapping.OLD_OPEN_NEW_CLOSE is the expected successful state after command.
				status = state.equals(Constant_IO_ActionMapping.CLOSE); 
				
				Ft.logger.debug(String.format("[%s] : [FINGERTIP_LATCH_COMMAND] : [STATUS_CHECK] - Raw state: %s, Interpreted status: %s", getMyBayKey(), state, status));

				testIntefaceStatus.setPortName(portInfo.getPortId());
				
				if(status){
					testIntefaceStatus.setDeviceResponseStatus("Success");
				}else{
					testIntefaceStatus.setDeviceResponseStatus("Failed");
				}
				
				// Check for timeout or invalid response
				// This logic `portInfo.getPortId().equals(state)` seems to be a generic check for timeout.
				// If the actual state string returned matches the port ID string, it might indicate an error.
				if(portInfo.getPortId().equals(state)){ 
					testIntefaceStatus.setDeviceResponseData("TimeOut");
					Ft.logger.warn(String.format("[%s] : [FINGERTIP_LATCH_COMMAND] : [TIMEOUT] - Fingertip latch command timed out or invalid response. Raw state: %s", getMyBayKey(), state));
				}else{
					testIntefaceStatus.setDeviceResponseData(state); // Store the actual response state
				}
				
				if(StateExecutorController.simulateFtBayHappyPath){
					status = true; // Override for simulation
					testIntefaceStatus.setDeviceResponseStatus("Success"); // Ensure GUI also reflects simulation success
					testIntefaceStatus.setDeviceResponseData(Constant_IO_ActionMapping.CLOSE); // Simulate successful state
					Ft.logger.debug(String.format("[%s] : [SIMULATION] : Fingertip latch open status overridden to TRUE.", getMyBayKey()));
				}

				// Update GUI with final status (assuming StateExecutorController.updateTestStatusGui handles Platform.runLater() internally)
				StateExecutorController.updateTestStatusGui(testIntefaceStatus);

			} catch (Exception e) {
				// Log any exceptions during output data transmission
				Ft.logger.error(String.format("[%s] : [COMMUNICATION_ERROR] : [FINGERTIP_LATCH_COMMAND] - Failed to send open command to fingertip latch. Port: %s. Error: %s", getMyBayKey(), portInfo.getPortId(), e.getMessage()), e);
				testIntefaceStatus.setDeviceResponseStatus("Error");
				testIntefaceStatus.setDeviceResponseData("Communication Error");
				StateExecutorController.updateTestStatusGui(testIntefaceStatus); // Update GUI with error status
				status = false; // Mark operation as failed
			}
		} else {
			// Log a clear error if the port information is missing
			Ft.logger.error(String.format("[%s] : [CONFIG_ERROR] : [FINGERTIP_LATCH_COMMAND] - Output port not found for Finger Tip Latch: %s", getMyBayKey(), ConstantBayPortNameMapping.FT_PORT_NAME_FINGER_TIP));
			testIntefaceStatus.setDeviceResponseStatus("Failed");
			testIntefaceStatus.setDeviceResponseData("O/P port not found");
			// Ensure GUI status is updated even for config errors
			StateExecutorController.addToTestStatusGui(testIntefaceStatus); 
			StateExecutorController.updateTestStatusGui(testIntefaceStatus);
			status = false; // Mark operation as failed
        }

		responseReturn.put("status", status);
		responseReturn.put("responseData", state); // Return the actual or simulated state
		responseReturn.put("testInterfaceStatus", testIntefaceStatus);

		// Structured debug log for method exit, corrected class name
		Ft.logger.debug(String.format("[%s] : [FINGERTIP_LATCH_COMMAND] : [REQUEST_EXIT] - Fingertip latch open request completed. Status: %s, Final State: %s", getMyBayKey(), status, state));
		return responseReturn;
	}

	public String getSequencePathId() {
		return sequencePathId;
	}

	public void setSequencePathId(String sequencePathId) {
		this.sequencePathId = sequencePathId;
	}

	public TestInterfaceStatus getPalletAvailableTest_I_F_Status() {
		return palletAvailableTest_I_F_Status;
	}

	public void setPalletAvailableTest_I_F_Status(TestInterfaceStatus palletAvailableTest_I_F_Status) {
		this.palletAvailableTest_I_F_Status = palletAvailableTest_I_F_Status;
	}
}
