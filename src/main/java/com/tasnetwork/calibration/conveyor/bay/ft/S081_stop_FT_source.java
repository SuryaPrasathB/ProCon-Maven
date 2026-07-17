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

public class S081_stop_FT_source implements FtBayState {

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	public String getMyBayKey() {
		return myBayKey;
	}

	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		// Structured log entry for sequence start
		Ft.logger.info(String.format("[%s] : [FT_SOURCE_STOP_CONTROL] : [SEQUENCE_ENTRY] - Stopping FT source control sequence.", getMyBayKey()));

		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true); // Assume success initially
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		Map<String,Object> responseReturn = stopFtSource();	 
		boolean stopFtSourceStatus = (boolean)responseReturn.get("status");

		if (stopFtSourceStatus) {
			Ft.logger.info(String.format("[%s] : [FT_SOURCE_STOP_CONTROL] : [SUCCESS] - FT Source Stopped successfully.", getMyBayKey()));
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			Ft.logger.error(String.format("[%s] : [FT_SOURCE_STOP_CONTROL] : [FAILED] - Failed to Stop FT Source. Error: %s", getMyBayKey(), ConvErrorCodeMapping.ERROR_CODE_FT_029));
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_029);
		}

		// This block involving UI updates and BayUtils.delay must be run on a background thread.
		// It is assumed that handleRequest() is invoked from such a thread.
		if (ProconFeatureEnable.WAIT_FOR_USER_INPUT) {
			Ft.logger.info(String.format("[%s] : [OPTICAL_READER_REMOVE_PROMPT] : [ACTIVE] - Waiting for user to remove optical readers.", getMyBayKey()));

			boolean toggle = false;
			long startTime = System.currentTimeMillis();
			
			Platform.runLater(() -> {
				if (StateExecutorController.getRef_btn_FtRemove() != null) {
					StateExecutorController.getRef_btn_FtRemove().setDisable(false);
					Ft.logger.debug(String.format("[%s] : [UI_UPDATE] : Enabled FtRemove button.", getMyBayKey()));
				} else {
					Ft.logger.warn(String.format("[%s] : [UI_UPDATE_WARNING] : ref_btn_FtRemove is null, cannot enable button.", getMyBayKey()));
				}
			});

			while (!ConstantConveyor.isFT_OPTICAL_REMOVED()) {
				long elapsedTime = (System.currentTimeMillis() - startTime) / 1000; // in seconds

				Platform.runLater(() -> {
					if (StateExecutorController.ref_tf_FT_prompt != null) {
						StateExecutorController.ref_tf_FT_prompt.setText("Remove Optical Readers - " + elapsedTime + " secs");
					} else {
						Ft.logger.warn(String.format("[%s] : [UI_UPDATE_WARNING] : ref_tf_FT_prompt is null, cannot update prompt text.", getMyBayKey()));
					}
				});

				// Alternate the tower lamp state (I/O operations, should be on background thread)
				if (toggle) {
					turn_on_tower_lamp1();  // Turn ON Lamp 2
					BayUtils.delay(100);
				} else {
					turn_off_tower_lamp1(); // Turn OFF Lamp 1
					BayUtils.delay(100);
				}
				toggle = !toggle; // Flip the flag for next iteration

				Ft.logger.debug(String.format("[%s] : [OPTICAL_READER_REMOVE_PROMPT] : [WAITING] - Waiting to remove Optical Readers. Elapsed: %d secs.", getMyBayKey(), elapsedTime));
			}

			BayUtils.delay(2000); // Blocking delay
			ConstantConveyor.setFT_OPTICAL_REMOVED(false); // Reset flag

			Platform.runLater(() -> {
				if (StateExecutorController.getRef_btn_FtRemove() != null) {
					StateExecutorController.getRef_btn_FtRemove().setDisable(true);
					Ft.logger.debug(String.format("[%s] : [UI_UPDATE] : Disabled FtRemove button.", getMyBayKey()));
				} else {
					Ft.logger.warn(String.format("[%s] : [UI_UPDATE_WARNING] : ref_btn_FtRemove is null, cannot disable button.", getMyBayKey()));
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
			Ft.logger.info(String.format("[%s] : [OPTICAL_READER_REMOVE_PROMPT] : [COMPLETED] - User input for removing optical readers completed.", getMyBayKey()));
		} else {
		    Ft.logger.info(String.format("[%s] : [OPTICAL_READER_REMOVE_PROMPT] : [DISABLED] - User input for removing optical readers is disabled.", getMyBayKey()));
		}

		// Structured log for sequence exit
		Ft.logger.info(String.format("[%s] : [FT_SOURCE_STOP_CONTROL] : [SEQUENCE_EXIT] - FT source stop control sequence completed.", getMyBayKey()));
		return bayResponse;
	}
	//============================================================================================================================================  

	private void turn_on_tower_lamp1() {
		Ft.logger.debug(String.format("[%s] : [TOWER_LAMP_1] : [TURN_ON_REQUEST] - Sending command to turn on Tower Lamp 1 (HV_PORT_NAME_TWR_LAMP2).", getMyBayKey()));
		IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.HV_PORT_NAME_TWR_LAMP2);

		if (portInfo == null) {
			Ft.logger.error(String.format("[%s] : [CONFIG_ERROR] : [TOWER_LAMP_1] - Port info not found for Tower Lamp 1 (HV_PORT_NAME_TWR_LAMP2).", getMyBayKey()));
			return;
		}

		// Assuming BayUtils is thread-safe or operations are on a single background thread.
		BayUtils bayUtils = new BayUtils();
		String state = "";
		try {
			state = bayUtils.setOutputDataToBay(portInfo.getClusterId(), 
					portInfo.getBayId(), 
					portInfo.getPortId(),
					Constant_IO_ActionMapping.OPEN); // Use CLOSE to represent turning the relay "On"
			Ft.logger.debug(String.format("[%s] : [TOWER_LAMP_1] : [ON_COMMAND_SENT] - Command sent. Response state: %s", getMyBayKey(), state));
		} catch (Exception e) {
			Ft.logger.error(String.format("[%s] : [COMM_ERROR] : [TOWER_LAMP_1] - Failed to send ON command to Tower Lamp 1. Port: %s. Error: %s", getMyBayKey(), portInfo.getPortId(), e.getMessage()), e);
		}
	}

	private void turn_off_tower_lamp1() {
		Ft.logger.debug(String.format("[%s] : [TOWER_LAMP_1] : [TURN_OFF_REQUEST] - Sending command to turn off Tower Lamp 1 (HV_PORT_NAME_TWR_LAMP2).", getMyBayKey()));
		IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.HV_PORT_NAME_TWR_LAMP2);

		if (portInfo == null) {
			Ft.logger.error(String.format("[%s] : [CONFIG_ERROR] : [TOWER_LAMP_1] - Port info not found for Tower Lamp 1 (HV_PORT_NAME_TWR_LAMP2).", getMyBayKey()));
			return;
		}

		BayUtils bayUtils = new BayUtils();
		String state = "";
		try {
			state = bayUtils.setOutputDataToBay(portInfo.getClusterId(), 
					portInfo.getBayId(), 
					portInfo.getPortId(),
					Constant_IO_ActionMapping.CLOSE); // Use OPEN to represent turning the relay "Off"
			Ft.logger.debug(String.format("[%s] : [TOWER_LAMP_1] : [OFF_COMMAND_SENT] - Command sent. Response state: %s", getMyBayKey(), state));
		} catch (Exception e) {
			Ft.logger.error(String.format("[%s] : [COMM_ERROR] : [TOWER_LAMP_1] - Failed to send OFF command to Tower Lamp 1. Port: %s. Error: %s", getMyBayKey(), portInfo.getPortId(), e.getMessage()), e);
		}
	}

	

	//============================================================================================================================================  

	private Map<String,Object> stopFtSource() {
		// Structured debug log for method entry
		Ft.logger.debug(String.format("[%s] : [FT_SOURCE_STOP_COMMAND] : [REQUEST_ENTRY] - Requesting to stop FT source.", getMyBayKey()));

		boolean status = false;
		IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_SRC_START); // Assuming this is the control pin for start/stop

		Map<String,Object> responseReturn = new HashMap<>();
		responseReturn.put("status", false); // Default status

		TestInterfaceStatus testIntefaceStatus = new TestInterfaceStatus();

		if (portInfo != null) {
			Ft.logger.debug(String.format("[%s] : [FT_SOURCE_STOP_COMMAND] : [PORT_INFO] - PortId: %s, ClusterId: %s, BayId: %s", getMyBayKey(), portInfo.getPortId(), portInfo.getClusterId(), portInfo.getBayId()));

			// Initialize TestInterfaceStatus
			testIntefaceStatus = new TestInterfaceStatus(
					ConstantConveyor.FT_BAY_KEY,
					ConstantBayStateManage.FT_BAY_HP_SEQ_10, // Assuming this sequence ID is for stopping FT source
					ConstantConveyor.DEVICE_TYPE_CLUSTER_OUTPUT,
					getSequencePathId(),
					"-",
					portInfo.getPortId(),
					ConstantBayPortNameMapping.FT_PORT_NAME_SRC_START,
					ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
					"Waiting",
					ConstantConveyor.COMM_EXECUTION_STATUS_INP
					);

			// Add to GUI (assuming StateExecutorController.addToTestStatusGui handles Platform.runLater() internally)
			StateExecutorController.addToTestStatusGui(testIntefaceStatus);
		} else {
			// Log a clear error if the port information is missing
			Ft.logger.error(String.format("[%s] : [CONFIG_ERROR] : [FT_SOURCE_STOP_COMMAND] - Output port not found for FT Source Start/Stop: %s", getMyBayKey(), ConstantBayPortNameMapping.FT_PORT_NAME_SRC_START));
			testIntefaceStatus.setDeviceResponseStatus("Failed");
			testIntefaceStatus.setDeviceResponseData("O/P port not found");
			// Ensure GUI status is updated even for config errors
			StateExecutorController.addToTestStatusGui(testIntefaceStatus); 
			StateExecutorController.updateTestStatusGui(testIntefaceStatus);

			responseReturn.put("status", false);
			responseReturn.put("responseData", "CONFIG_ERROR"); // Indicate configuration error
			responseReturn.put("testInterfaceStatus", testIntefaceStatus);
			return responseReturn; // Early exit if critical config is missing
		}

		BayUtils bayUtils = new BayUtils();
		String state = "";
		try {
			state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
					portInfo.getBayId(),
					portInfo.getPortId(),
					Constant_IO_ActionMapping.OFF); // Command to turn OFF/STOP
		} catch (Exception e) {
			// Log any exceptions during output data transmission
			Ft.logger.error(String.format("[%s] : [COMMUNICATION_ERROR] : [FT_SOURCE_STOP_COMMAND] - Failed to send stop command to FT Source. Port: %s. Error: %s", getMyBayKey(), portInfo.getPortId(), e.getMessage()), e);
			testIntefaceStatus.setDeviceResponseStatus("Error");
			testIntefaceStatus.setDeviceResponseData("Communication Error");
			StateExecutorController.updateTestStatusGui(testIntefaceStatus); // Update GUI with error status
			responseReturn.put("status", false);
			responseReturn.put("responseData", state); // Still include raw state for debugging
			responseReturn.put("testInterfaceStatus", testIntefaceStatus);
			return responseReturn; // Early exit on communication failure
		}

		// Log the raw state received from the control system for debugging
		Ft.logger.debug(String.format("[%s] : [FT_SOURCE_STOP_COMMAND] : [RAW_STATE] : %s", getMyBayKey(), state));

		// Assuming OLD_ON_NEW_OFF signifies successful deactivation and OLD_OPEN_NEW_CLOSE is the 'OFF' state of the pin
		// This logic appears to be interpreting the returned 'state' to match the desired final pin state.
		// If setOutputDataToBay returns the *actual resulting state* then this is correct.
		boolean actualStateMatchesExpected = state.equals(Constant_IO_ActionMapping.OFF); 
		String interpretedState = actualStateMatchesExpected ? Constant_IO_ActionMapping.CLOSE : Constant_IO_ActionMapping.OPEN;

		if(StateExecutorController.simulateFtBayHappyPath){
			interpretedState = Constant_IO_ActionMapping.CLOSE; // Simulate successful state
			Ft.logger.debug(String.format("[%s] : [SIMULATION] : FT Source stop status overridden to STOPPED.", getMyBayKey()));
		}		

		// Update TestInterfaceStatus based on command status
		if(interpretedState.equals(Constant_IO_ActionMapping.CLOSE)){ // Check if the interpreted state is OFF
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
			Ft.logger.warn(String.format("[%s] : [FT_SOURCE_STOP_COMMAND] : [TIMEOUT] - FT Source stop command timed out or invalid response. Raw state: %s", getMyBayKey(), state));
		}else{
			testIntefaceStatus.setDeviceResponseData(interpretedState); // Store the actual interpreted state
		}
		
		// Update GUI with final status (assuming StateExecutorController.updateTestStatusGui handles Platform.runLater() internally)
		StateExecutorController.updateTestStatusGui(testIntefaceStatus);

		responseReturn.put("responseData", interpretedState); // Use the interpreted state in responseData
		responseReturn.put("status", status); // Return the boolean status
		responseReturn.put("testInterfaceStatus", testIntefaceStatus);

		// Structured debug log for method exit
		Ft.logger.debug(String.format("[%s] : [FT_SOURCE_STOP_COMMAND] : [REQUEST_EXIT] - FT Source stop request completed. Status: %s, Interpreted State: %s", getMyBayKey(), status, interpretedState));
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
