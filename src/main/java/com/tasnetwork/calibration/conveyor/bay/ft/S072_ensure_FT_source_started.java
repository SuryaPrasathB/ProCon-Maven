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
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

import javafx.application.Platform;

public class S072_ensure_FT_source_started implements FtBayState {

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();
	
	public String getMyBayKey() {
		return myBayKey;
	}

	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		// Structured log entry for sequence start
		Ft.logger.info(String.format("[%s] : [FT_SOURCE_STATUS_CHECK] : [SEQUENCE_ENTRY] - Ensuring FT source is started.", getMyBayKey()));

		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true); // Assume success initially
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		int try_count = 0;
		Map<String,Object> responseReturn = new HashMap<>(); // Initialize once outside the loop
		String ftSourcePresentState = "";

		setSequencePathId("p1"); // Set sequence path ID for this operation
		setPalletAvailableTest_I_F_Status (null); // Resetting for current operation

		while (try_count <= 3) {
			Ft.logger.debug(String.format("[%s] : [FT_SOURCE_STATUS_CHECK] : [RETRY] - Attempt %d of 4 to verify FT source status.", getMyBayKey(), try_count + 1));
			
			responseReturn = ftBay_SourceStatusPin_Status();
			ftSourcePresentState = (String)responseReturn.get("responseData");
			setPalletAvailableTest_I_F_Status((TestInterfaceStatus)responseReturn.get("testInterfaceStatus"));

			if (Constant_IO_ActionMapping.OPEN.equals(ftSourcePresentState)) {  // Verify success
				Ft.logger.info(String.format("[%s] : [FT_SOURCE_STATUS_CHECK] : [SUCCESS] - FT Source confirmed started. State: %s", getMyBayKey(), ftSourcePresentState));
				bayResponse.setStatus(true);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
				break; // Exit loop on success
			} else {
				Ft.logger.warn(String.format("[%s] : [FT_SOURCE_STATUS_CHECK] : [MISMATCH] - FT Source status is not STARTED. Expected: %s, Actual: %s. Retrying...", getMyBayKey(), Constant_IO_ActionMapping.OPEN, ftSourcePresentState));
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_030);
				BayUtils.delay(1000); // Blocking delay; ensure handleRequest() is called from a background thread.
				try_count++;
			}
		}

		if (!Constant_IO_ActionMapping.OPEN.equals(ftSourcePresentState)) {
		    // Log final failure if loop completes without success
		    Ft.logger.error(String.format("[%s] : [FT_SOURCE_STATUS_CHECK] : [FINAL_FAILURE] - Failed to ensure FT source is started after %d attempts. Final state: %s. Error: %s", getMyBayKey(), try_count, ftSourcePresentState, ConvErrorCodeMapping.ERROR_CODE_FT_030));
		}

		// Assuming StateExecutorController.updateTestInterfaceStatusOnGui handles Platform.runLater() internally
		StateExecutorController.updateTestInterfaceStatusOnGui(responseReturn,ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
		
		// This entire block involving UI updates and BayUtils.delay must be run on a background thread.
		// It is assumed that handleRequest() is invoked from such a thread.
		if (ProconFeatureEnable.WAIT_FOR_USER_INPUT) {
			Ft.logger.info(String.format("[%s] : [LDU_SENSOR_PROMPT] : [ACTIVE] - Waiting for user confirmation for LDU sensors.", getMyBayKey()));

			boolean toggle = false;
			long startTime = System.currentTimeMillis();

			Platform.runLater(() -> {
				if (StateExecutorController.getRef_btn_Ft_LDUOK() != null) {
					StateExecutorController.getRef_btn_Ft_LDUOK().setDisable(false);
					Ft.logger.debug(String.format("[%s] : [UI_UPDATE] : Enabled Ft_LDUOK button.", getMyBayKey()));
				} else {
					Ft.logger.warn(String.format("[%s] : [UI_UPDATE_WARNING] : ref_btn_Ft_LDUOK is null, cannot enable button.", getMyBayKey()));
				}
			});

			while (!ConstantConveyor.isFT_LDU_PLACEMENT()) {
				long elapsedTime = (System.currentTimeMillis() - startTime) / 1000; // in seconds

				Platform.runLater(() -> {
					if (StateExecutorController.ref_tf_FT_prompt != null) {
						StateExecutorController.ref_tf_FT_prompt.setText("LDU Sensors OK ? - " + elapsedTime + " secs");
					} else {
						Ft.logger.warn(String.format("[%s] : [UI_UPDATE_WARNING] : ref_tf_FT_prompt is null, cannot update prompt text.", getMyBayKey()));
					}
				});

				// Alternate the tower lamp state (I/O operations, should be on background thread)
				if (toggle) {
					turn_on_tower_lamp2();
					BayUtils.delay(100);
				} else {
					turn_off_tower_lamp2();
					BayUtils.delay(100);
				}
				toggle = !toggle; // Flip the flag for next iteration

				Ft.logger.debug(String.format("[%s] : [LDU_SENSOR_PROMPT] : [WAITING] - Waiting for LDU SENSOR ADJUSTMENT. Elapsed: %d secs.", getMyBayKey(), elapsedTime));
			}

			BayUtils.delay(2000); // Blocking delay
			ConstantConveyor.setFT_LDU_PLACEMENT(false); // Reset flag

			Platform.runLater(() -> {
				if (StateExecutorController.getRef_btn_Ft_LDUOK() != null) {
					StateExecutorController.getRef_btn_Ft_LDUOK().setDisable(true);
					Ft.logger.debug(String.format("[%s] : [UI_UPDATE] : Disabled Ft_LDUOK button.", getMyBayKey()));
				} else {
					Ft.logger.warn(String.format("[%s] : [UI_UPDATE_WARNING] : ref_btn_Ft_LDUOK is null, cannot disable button.", getMyBayKey()));
				}
			});

			Platform.runLater(() -> {
				if (StateExecutorController.ref_tf_FT_prompt != null) {
					StateExecutorController.ref_tf_FT_prompt.clear();
					Ft.logger.debug(String.format("[%s] : [UI_UPDATE] : Cleared FT prompt text.", getMyBayKey()));
				} else {
					Ft.logger.warn(String.format("[%s] : [UI_UPDATE_WARNING] : ref_tf_FT_prompt is null, cannot clear prompt text.", getMyBayKey()));
				}
			});
			Ft.logger.info(String.format("[%s] : [LDU_SENSOR_PROMPT] : [COMPLETED] - User confirmation for LDU sensors received.", getMyBayKey()));
		} else {
		    Ft.logger.info(String.format("[%s] : [LDU_SENSOR_PROMPT] : [DISABLED] - User input for LDU sensors is disabled.", getMyBayKey()));
		}


		// Structured log for sequence exit
		Ft.logger.info(String.format("[%s] : [FT_SOURCE_STATUS_CHECK] : [SEQUENCE_EXIT] - FT source status check sequence completed.", getMyBayKey()));
		return bayResponse;
	}
	//============================================================================================================================================  

	private Map<String, Object> ftBay_SourceStatusPin_Status() {
		// Structured debug log for method entry
		Ft.logger.debug(String.format("[%s] : [FT_SOURCE_STATUS_PIN] : [REQUEST_ENTRY] - Reading FT Source status pin.", getMyBayKey()));

		Map<String,Object> responseReturn = new HashMap<>();
		responseReturn.put("status", false); // Default status
		TestInterfaceStatus testIntefaceStatus = new TestInterfaceStatus();

		IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_SRC_STATUS_PIN); 

		if (portInfo != null) {
			Ft.logger.debug(String.format("[%s] : [FT_SOURCE_STATUS_PIN] : [PORT_INFO] - PortId: %s, ClusterId: %s, BayId: %s", getMyBayKey(), portInfo.getPortId(), portInfo.getClusterId(), portInfo.getBayId()));
			
			if(getPalletAvailableTest_I_F_Status()==null){
				// Initialize TestInterfaceStatus
				testIntefaceStatus = new TestInterfaceStatus(
						ConstantConveyor.FT_BAY_KEY,
						ConstantBayStateManage.BAY_HP_SEQ_08,
						ConstantConveyor.DEVICE_TYPE_CLUSTER_INPUT,
						getSequencePathId(),
						"-",
						portInfo.getPortId(),
						ConstantBayPortNameMapping.FT_PORT_NAME_SRC_STATUS_PIN,
						ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
						"Waiting",
						ConstantConveyor.COMM_EXECUTION_STATUS_INP);
				
				int newRecordSerialNo = StateExecutorController.addToTestStatusGui(testIntefaceStatus);
				testIntefaceStatus.setSerialNo(String.valueOf(newRecordSerialNo));
			}else{
				testIntefaceStatus = getPalletAvailableTest_I_F_Status();
			}
		} else {
			// Log a clear error if the port information is missing
			Ft.logger.error(String.format("[%s] : [CONFIG_ERROR] : [FT_SOURCE_STATUS_PIN] - Input port not found for FT Source Status Pin: %s", getMyBayKey(), ConstantBayPortNameMapping.FT_PORT_NAME_SRC_STATUS_PIN));
			testIntefaceStatus.setDeviceResponseStatus("Failed");
			testIntefaceStatus.setDeviceResponseData("I/P port not found");
			// Ensure GUI status is updated even for config errors
			StateExecutorController.addToTestStatusGui(testIntefaceStatus); // Add if not already added, or update if already present.
			StateExecutorController.updateTestStatusGui(testIntefaceStatus); // Corrected typo here
			responseReturn.put("status", false);
			responseReturn.put("responseData", "CONFIG_ERROR"); // Indicate configuration error
			responseReturn.put("testInterfaceStatus", testIntefaceStatus);
			return responseReturn; // Early exit if critical config is missing
        }

		BayUtils bayUtils = new BayUtils();
		String state = "";
		boolean status = false; // Local status variable

		try {
			state = bayUtils.getInputDataFromBayV2(portInfo);
		} catch (Exception e) {
			// Log any exceptions during input data retrieval
			Ft.logger.error(String.format("[%s] : [COMMUNICATION_ERROR] : [FT_SOURCE_STATUS_PIN] - Failed to get input data from FT Source Status Pin. Port: %s. Error: %s", getMyBayKey(), portInfo.getPortId(), e.getMessage()), e);
			testIntefaceStatus.setDeviceResponseStatus("Error");
			testIntefaceStatus.setDeviceResponseData("Communication Error");
			StateExecutorController.updateTestStatusGui(testIntefaceStatus); // Update GUI with error status
			responseReturn.put("status", false);
			responseReturn.put("responseData", state); // Still include raw state for debugging
			responseReturn.put("testInterfaceStatus", testIntefaceStatus);
			return responseReturn; // Early exit on communication failure
		}
		
		// Log the raw sensor state for debugging
		Ft.logger.debug(String.format("[%s] : [FT_SOURCE_STATUS_PIN] : [RAW_STATE] : %s", getMyBayKey(), state));

		// Interpret the raw state from the sensor
		// If OLD_OFF_NEW_ON means the source is active/on, then interpretedState should be OLD_CLOSE_NEW_OPEN (representing "closed circuit" or "active")
		String interpretedState = state.equals(Constant_IO_ActionMapping.ON) ? Constant_IO_ActionMapping.OPEN : Constant_IO_ActionMapping.CLOSE;

		if(StateExecutorController.simulateFtBayHappyPath){
			interpretedState = Constant_IO_ActionMapping.OPEN; // Simulate successful state
			// Shortened and moved to TRACE level for minimal impact
			Ft.logger.debug(String.format("[%s] : [SIMULATION] : FT Source status overridden to STARTED.", getMyBayKey()));
		}

		// Update TestInterfaceStatus based on the interpreted state
		if(interpretedState.equals(Constant_IO_ActionMapping.OPEN)){
			testIntefaceStatus.setDeviceResponseStatus("Success");
			status = true; // Set local status
		}else{
			testIntefaceStatus.setDeviceResponseStatus("Failed");
			status = false; // Set local status
		}
		
		// Check for timeout or invalid response
		// This logic `portInfo.getPortId().equals(state)` seems to be a generic check for timeout.
		// If the actual state string returned matches the port ID string, it might indicate an error.
		if(portInfo.getPortId().equals(state)){ 
			testIntefaceStatus.setDeviceResponseData("TimeOut");
			Ft.logger.warn(String.format("[%s] : [FT_SOURCE_STATUS_PIN] : [TIMEOUT] - FT Source status pin read timed out or invalid response. Raw state: %s", getMyBayKey(), state));
		}else{
			testIntefaceStatus.setDeviceResponseData(interpretedState); // Store the actual response state
		}
		
		// Update GUI with final status (assuming StateExecutorController.updateTestStatusGui handles Platform.runLater() internally)
		StateExecutorController.updateTestStatusGui(testIntefaceStatus);

		responseReturn.put("status", status); // Return the boolean status
		responseReturn.put("responseData", interpretedState); // Use the interpreted state in responseData
		responseReturn.put("testInterfaceStatus", testIntefaceStatus);

		// Structured debug log for method exit
		Ft.logger.debug(String.format("[%s] : [FT_SOURCE_STATUS_PIN] : [REQUEST_EXIT] - FT Source status pin read completed. Status: %s, Interpreted State: %s", getMyBayKey(), status, interpretedState));
		return responseReturn;
	}
	
	//============================================================================================================================================  

    private void turn_on_tower_lamp2() {
		Ft.logger.debug(String.format("[%s] : [TOWER_LAMP_2] : [TURN_ON_REQUEST] - Sending command to turn on Tower Lamp 2 (HV_PORT_NAME_TWR_LAMP1).", getMyBayKey()));
    	IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.HV_PORT_NAME_TWR_LAMP1);

		if (portInfo == null) {
            Ft.logger.error(String.format("[%s] : [CONFIG_ERROR] : [TOWER_LAMP_2] - Port info not found for Tower Lamp 2 (HV_PORT_NAME_TWR_LAMP1).", getMyBayKey()));
            return;
        }

    	BayUtils bayUtils = new BayUtils();
		String state = "";
		try {
			state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
					portInfo.getBayId(),
					portInfo.getPortId(),
					Constant_IO_ActionMapping.OPEN); // Use CLOSE for ON
			Ft.logger.debug(String.format("[%s] : [TOWER_LAMP_2] : [ON_COMMAND_SENT] - Command sent. Response state: %s", getMyBayKey(), state));
		} catch (Exception e) {
			Ft.logger.error(String.format("[%s] : [COMM_ERROR] : [TOWER_LAMP_2] - Failed to send ON command to Tower Lamp 2. Port: %s. Error: %s", getMyBayKey(), portInfo.getPortId(), e.getMessage()), e);
		}
    }

    private void turn_off_tower_lamp2() {
		Ft.logger.debug(String.format("[%s] : [TOWER_LAMP_2] : [TURN_OFF_REQUEST] - Sending command to turn off Tower Lamp 2 (HV_PORT_NAME_TWR_LAMP1).", getMyBayKey()));
    	IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.HV_PORT_NAME_TWR_LAMP1);

		if (portInfo == null) {
            Ft.logger.error(String.format("[%s] : [CONFIG_ERROR] : [TOWER_LAMP_2] - Port info not found for Tower Lamp 2 (HV_PORT_NAME_TWR_LAMP1).", getMyBayKey()));
            return;
        }

    	BayUtils bayUtils = new BayUtils();
		String state = "";
		try {
			state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
					portInfo.getBayId(),
					portInfo.getPortId(),
					Constant_IO_ActionMapping.CLOSE); // Use OPEN for OFF
			Ft.logger.debug(String.format("[%s] : [TOWER_LAMP_2] : [OFF_COMMAND_SENT] - Command sent. Response state: %s", getMyBayKey(), state));
		} catch (Exception e) {
			Ft.logger.error(String.format("[%s] : [COMM_ERROR] : [TOWER_LAMP_2] - Failed to send OFF command to Tower Lamp 2. Port: %s. Error: %s", getMyBayKey(), portInfo.getPortId(), e.getMessage()), e);
		}
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
