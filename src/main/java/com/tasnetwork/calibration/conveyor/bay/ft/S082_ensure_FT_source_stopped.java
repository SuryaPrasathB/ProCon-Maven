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

/**
 * State class responsible for ensuring the FT source is stopped.
 */
public class S082_ensure_FT_source_stopped implements FtBayState {

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();


	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		// Structured log entry for sequence start
		Ft.logger.info(String.format("[%s] : [FT_SOURCE_STATUS_CHECK] : [SEQUENCE_ENTRY] - Ensuring FT source is stopped.", getMyBayKey()));

		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true); // Assume success initially
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		int try_count = 0;
		Map<String,Object> responseReturn = new HashMap<>(); // Initialize once outside the loop
		String ftSourcePresentState = "";

		setSequencePathId("p1"); // Set sequence path ID for this operation
		setPalletAvailableTest_I_F_Status (null); // Resetting for current operation

		while (try_count <= 3 && !Ft.isStopProcessRequestedFtBay() && !ConstantConveyor.ALL_LOOP_BREAK_FLAG) {
			Ft.logger.debug(String.format("[%s] : [FT_SOURCE_STATUS_CHECK] : [RETRY] - Attempt %d of 4 to verify FT source status.", getMyBayKey(), try_count + 1));
			
			responseReturn = ftBay_SourceStatusPin_Status();
			ftSourcePresentState = (String)responseReturn.get("responseData");
			setPalletAvailableTest_I_F_Status((TestInterfaceStatus)responseReturn.get("testInterfaceStatus"));

			if ("SRC_OFF".equals(ftSourcePresentState)) {  
				Ft.logger.info(String.format("[%s] : [FT_SOURCE_STATUS_CHECK] : [SUCCESS] - FT Source confirmed STOPPED. State: %s", getMyBayKey(), ftSourcePresentState));
				bayResponse.setStatus(true);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
				break; // Exit loop on success
			} else {
				// Corrected log message to be more specific and include bay key
				Ft.logger.warn(String.format("[%s] : [FT_SOURCE_STATUS_CHECK] : [MISMATCH] - FT Source status is not STOPPED. Expected: SRC_OFF, Actual: %s. Retrying...", getMyBayKey(), ftSourcePresentState));
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_031);
				BayUtils.delay(1000); // Blocking delay; ensure handleRequest() is called from a background thread.
				try_count++;
			}
		}

		if (!"SRC_OFF".equals(ftSourcePresentState)) {
		    // Log final failure if loop completes without success
		    Ft.logger.error(String.format("[%s] : [FT_SOURCE_STATUS_CHECK] : [FINAL_FAILURE] - Failed to ensure FT source is stopped after %d attempts. Final state: %s. Error: %s", getMyBayKey(), try_count, ftSourcePresentState, ConvErrorCodeMapping.ERROR_CODE_FT_031));
		}

		StateExecutorController.updateTestInterfaceStatusOnGui(responseReturn,ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);

		// Structured log for sequence exit
		Ft.logger.info(String.format("[%s] : [FT_SOURCE_STATUS_CHECK] : [SEQUENCE_EXIT] - FT source status check sequence completed.", getMyBayKey()));
		return bayResponse;
	}
	//============================================================================================================================================  

	private Map<String, Object> ftBay_SourceStatusPin_Status() {
		// Structured debug log for method entry
		Ft.logger.debug(String.format("[%s] : [FT_SOURCE_STATUS_PIN_READ] : [REQUEST_ENTRY] - Reading FT Source status pin.", getMyBayKey()));

		Map<String,Object> responseReturn = new HashMap<>();
		responseReturn.put("status", false); // Default status
		TestInterfaceStatus testIntefaceStatus = new TestInterfaceStatus();

		String state = ""; // Raw state read from sensor
	 
		IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_SRC_STATUS_PIN); 

		if (portInfo != null) {
			Ft.logger.debug(String.format("[%s] : [FT_SOURCE_STATUS_PIN_READ] : [PORT_INFO] - PortId: %s, ClusterId: %s, BayId: %s", getMyBayKey(), portInfo.getPortId(), portInfo.getClusterId(), portInfo.getBayId()));
			
			if(getPalletAvailableTest_I_F_Status()==null){
				// Initialize TestInterfaceStatus for GUI
				testIntefaceStatus = new TestInterfaceStatus(
						ConstantConveyor.FT_BAY_KEY,
						ConstantBayStateManage.FT_BAY_HP_SEQ_11, // Assuming this sequence ID is correct for this step
						ConstantConveyor.DEVICE_TYPE_CLUSTER_INPUT, // Corrected to INPUT since it's reading status
						getSequencePathId(),
						"-",
						portInfo.getPortId(),
						ConstantBayPortNameMapping.FT_PORT_NAME_SRC_STATUS_PIN,
						ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
						"Waiting",
						ConstantConveyor.COMM_EXECUTION_STATUS_INP
						);
				
				int newRecordSerialNo = StateExecutorController.addToTestStatusGui(testIntefaceStatus);
				testIntefaceStatus.setSerialNo(String.valueOf(newRecordSerialNo));
			}else{
				testIntefaceStatus = getPalletAvailableTest_I_F_Status();
			}
		} else {
			// Log a clear error if the port information is missing
			Ft.logger.error(String.format("[%s] : [CONFIG_ERROR] : [FT_SOURCE_STATUS_PIN_READ] - Input port not found for FT Source Status Pin: %s", getMyBayKey(), ConstantBayPortNameMapping.FT_PORT_NAME_SRC_STATUS_PIN));
			testIntefaceStatus.setDeviceResponseStatus("Failed");
			testIntefaceStatus.setDeviceResponseData("I/P port not found");
			// Ensure GUI status is updated even for config errors
			StateExecutorController.addToTestStatusGui(testIntefaceStatus); 
			StateExecutorController.updateTestStatusGui(testIntefaceStatus); 

			responseReturn.put("status", false);
			responseReturn.put("responseData", "CONFIG_ERROR"); // Indicate configuration error
			responseReturn.put("testInterfaceStatus", testIntefaceStatus);
			return responseReturn; // Early exit if critical config is missing
        }

		BayUtils bayUtils = new BayUtils();
		boolean isSuccess = false; // Local boolean status for the operation

		try {
	    	state = bayUtils.getInputDataFromBayV2(portInfo) ;
		} catch (Exception e) {
			// Log any exceptions during input data retrieval
			Ft.logger.error(String.format("[%s] : [COMMUNICATION_ERROR] : [FT_SOURCE_STATUS_PIN_READ] - Failed to get input data from FT Source Status Pin. Port: %s. Error: %s", getMyBayKey(), portInfo.getPortId(), e.getMessage()), e);
			testIntefaceStatus.setDeviceResponseStatus("Error");
			testIntefaceStatus.setDeviceResponseData("Communication Error");
			StateExecutorController.updateTestStatusGui(testIntefaceStatus); // Update GUI with error status
			responseReturn.put("status", false);
			responseReturn.put("responseData", state); // Still include raw state for debugging
			responseReturn.put("testInterfaceStatus", testIntefaceStatus);
			return responseReturn; // Early exit on communication failure
		}

		Ft.logger.debug(String.format("[%s] : [FT_SOURCE_STATUS_PIN_READ] : [RAW_STATE] : %s", getMyBayKey(), state));

		// Interpret the raw state from the sensor
		// Assuming OLD_ON_NEW_OFF means the source is inactive/off, then "SRC_OFF"
		String interpretedState = state.equals(Constant_IO_ActionMapping.OFF) ? "SRC_OFF" : "SRC_ON";

		if(StateExecutorController.simulateFtBayHappyPath){
			interpretedState = "SRC_OFF"; // Simulate successful state (source is off)
			Ft.logger.debug(String.format("[%s] : [SIMULATION] : FT Source status overridden to STOPPED.", getMyBayKey()));
		}

		// Update TestInterfaceStatus based on the interpreted state
		if("SRC_OFF".equals(interpretedState)){ // Check if the interpreted state is OFF
			testIntefaceStatus.setDeviceResponseStatus("Success");
			isSuccess = true; // Set local status to true if source is OFF
		}else{
			testIntefaceStatus.setDeviceResponseStatus("Failed");
			isSuccess = false; // Set local status to false if source is ON
		}
		
		if(portInfo.getPortId().equals(state)){ 
			testIntefaceStatus.setDeviceResponseData("TimeOut");
			Ft.logger.warn(String.format("[%s] : [FT_SOURCE_STATUS_PIN_READ] : [TIMEOUT] - FT Source status pin read timed out or invalid response. Raw state: %s", getMyBayKey(), state));
		}else{
			testIntefaceStatus.setDeviceResponseData(interpretedState); // Store the actual interpreted state
		}
		
		StateExecutorController.updateTestStatusGui(testIntefaceStatus);

		responseReturn.put("responseData", interpretedState); // Use the interpreted state in responseData
		responseReturn.put("status", isSuccess); // Return the boolean status
		responseReturn.put("testInterfaceStatus", testIntefaceStatus);
		
		// Structured debug log for method exit
		Ft.logger.debug(String.format("[%s] : [FT_SOURCE_STATUS_PIN_READ] : [REQUEST_EXIT] - FT Source status pin read completed. Status: %s, Interpreted State: %s", getMyBayKey(), isSuccess, interpretedState));
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
