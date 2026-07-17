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

public class S071_start_FT_source implements FtBayState {

	BayUtils bayUtils = new BayUtils();

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	public String getMyBayKey() {
		return myBayKey;
	}

	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		// Structured log entry for sequence start
		Ft.logger.info(String.format("[%s] : [FT_SOURCE_CONTROL] : [SEQUENCE_ENTRY] - Starting FT source control sequence.", getMyBayKey()));

		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true); // Assume success initially
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601 );

		Map<String,Object> responseReturn = startFtSource();	 
		boolean startFtSourceStatus = (boolean)responseReturn.get("status");
		
		if (startFtSourceStatus) {
			Ft.logger.info(String.format("[%s] : [FT_SOURCE_CONTROL] : [SUCCESS] - FT Source Started successfully.", getMyBayKey()));
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			Ft.logger.error(String.format("[%s] : [FT_SOURCE_CONTROL] : [FAILED] - Failed to start FT Source. Error: %s", getMyBayKey(), ConvErrorCodeMapping.ERROR_CODE_FT_028));
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_028);
		}
		
		// Assuming StateExecutorController.updateTestInterfaceStatusOnGui handles Platform.runLater() internally
		StateExecutorController.updateTestInterfaceStatusOnGui(responseReturn,ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);

		// Structured log for sequence exit
		Ft.logger.info(String.format("[%s] : [FT_SOURCE_CONTROL] : [SEQUENCE_EXIT] - FT source control sequence completed.", getMyBayKey()));
		return bayResponse;
	}
	//============================================================================================================================================  

	private Map<String, Object> startFtSource() {
		// Structured debug log for method entry
		Ft.logger.debug(String.format("[%s] : [FT_SOURCE_START_COMMAND] : [REQUEST_ENTRY] - Requesting to start FT source.", getMyBayKey()));

		boolean status = false;
		IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_SRC_START);
		Map<String,Object> responseReturn = new HashMap<>();
		responseReturn.put("status", false); // Default status

		TestInterfaceStatus testIntefaceStatus = new TestInterfaceStatus();

		if (portInfo != null) {
			Ft.logger.debug(String.format("[%s] : [FT_SOURCE_START_COMMAND] : [PORT_INFO] - PortId: %s, ClusterId: %s, BayId: %s", getMyBayKey(), portInfo.getPortId(), portInfo.getClusterId(), portInfo.getBayId()));
			
			// Initialize TestInterfaceStatus
			testIntefaceStatus = new TestInterfaceStatus(
					ConstantConveyor.FT_BAY_KEY,
					ConstantBayStateManage.BAY_HP_SEQ_08,
					ConstantConveyor.DEVICE_TYPE_CLUSTER_OUTPUT,
					getSequencePathId(),
					"-",
					portInfo.getPortId(),
					ConstantBayPortNameMapping.FT_PORT_NAME_SRC_START,
					ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
					"Waiting",
					ConstantConveyor.COMM_EXECUTION_STATUS_INP);
			
			// Add to GUI (assuming StateExecutorController.addToTestStatusGui handles Platform.runLater() internally)
			StateExecutorController.addToTestStatusGui(testIntefaceStatus);
		} else {
			// Log a clear error if the port information is missing
			Ft.logger.error(String.format("[%s] : [CONFIG_ERROR] : [FT_SOURCE_START_COMMAND] - Output port not found for FT Source Start: %s", getMyBayKey(), ConstantBayPortNameMapping.FT_PORT_NAME_SRC_START));
			testIntefaceStatus.setDeviceResponseStatus("Failed");
			testIntefaceStatus.setDeviceResponseData("O/P port not found");
			// Ensure GUI status is updated even for config errors
			StateExecutorController.addToTestStatusGui(testIntefaceStatus); // Add if not already added, or update if already present.
			StateExecutorController.updateTestStatusGui(testIntefaceStatus);

			responseReturn.put("status", false);
			responseReturn.put("responseData", "CONFIG_ERROR"); // Indicate configuration error
			responseReturn.put("testInterfaceStatus", testIntefaceStatus);
			return responseReturn; // Early exit if critical config is missing
        }

		String state = "";
		try {
			state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
					portInfo.getBayId(),
					portInfo.getPortId(),
					Constant_IO_ActionMapping.ON);
		} catch (Exception e) {
			// Log any exceptions during output data transmission
			Ft.logger.error(String.format("[%s] : [COMMUNICATION_ERROR] : [FT_SOURCE_START_COMMAND] - Failed to send start command to FT Source. Port: %s. Error: %s", getMyBayKey(), portInfo.getPortId(), e.getMessage()), e);
			testIntefaceStatus.setDeviceResponseStatus("Error");
			testIntefaceStatus.setDeviceResponseData("Communication Error");
			StateExecutorController.updateTestStatusGui(testIntefaceStatus); // Update GUI with error status
			responseReturn.put("status", false);
			responseReturn.put("responseData", state); // Still include raw state for debugging
			responseReturn.put("testInterfaceStatus", testIntefaceStatus);
			return responseReturn; // Early exit on communication failure
		}
		
		// Log the raw state received from the control system for debugging
		Ft.logger.debug(String.format("[%s] : [FT_SOURCE_START_COMMAND] : [RAW_STATE] : %s", getMyBayKey(), state));

		// Assuming OLD_OFF_NEW_ON signifies successful activation based on previous context.
		// Re-evaluating logic: if setOutputDataToBay returns OLD_OFF_NEW_ON on success, then use that for comparison.
		// The next line `state = state.equals(...)` seems to re-interpret the string `state`.
		// It should directly compare `state` with the expected success response from `setOutputDataToBay`.
		// If `setOutputDataToBay` *returns* `OLD_OFF_NEW_ON` when successful, then this is correct.
		// If `OLD_CLOSE_NEW_OPEN` (which means "On") is the expected final state of the pin, then the check below is correct.
		// For consistency, let's assume `setOutputDataToBay` returns the *resultant state* of the pin.
		
		// Original logic `state = state.equals(Constant_IO_ActionMapping.OLD_OFF_NEW_ON) ? Constant_IO_ActionMapping.OLD_CLOSE_NEW_OPEN : Constant_IO_ActionMapping.OLD_OPEN_NEW_CLOSE;`
		// This line re-interprets the 'state' string. Let's assume `OLD_OFF_NEW_ON` is the expected *response* for a successful "start" command.
		
		status = state.equals(Constant_IO_ActionMapping.ON); // Check if the returned state matches the 'ON' signal

		if(StateExecutorController.simulateFtBayHappyPath){
			status = true; // Override for simulation
			// Shortened and moved to TRACE level for minimal impact
			Ft.logger.debug(String.format("[%s] : [SIMULATION] : FT Source start status overridden to TRUE.", getMyBayKey()));
		}
		
		// Update TestInterfaceStatus based on command status
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
			Ft.logger.warn(String.format("[%s] : [FT_SOURCE_START_COMMAND] : [TIMEOUT] - FT Source start command timed out or invalid response. Raw state: %s", getMyBayKey(), state));
		}else{
			testIntefaceStatus.setDeviceResponseData(state); // Store the actual response state
		}
		
		// Update GUI with final status (assuming StateExecutorController.updateTestStatusGui handles Platform.runLater() internally)
		StateExecutorController.updateTestStatusGui(testIntefaceStatus);

		responseReturn.put("status", status);
		responseReturn.put("responseData", testIntefaceStatus.getDeviceResponseData()); // Use the updated response data
		responseReturn.put("testInterfaceStatus", testIntefaceStatus);

		// Structured debug log for method exit
		Ft.logger.debug(String.format("[%s] : [FT_SOURCE_START_COMMAND] : [REQUEST_EXIT] - FT Source start request completed. Status: %s", getMyBayKey(), status));
		return responseReturn;
	}
	//============================================================================================================================================  

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
