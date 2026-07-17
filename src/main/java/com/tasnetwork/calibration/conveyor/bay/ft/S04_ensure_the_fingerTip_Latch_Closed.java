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
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S04_ensure_the_fingerTip_Latch_Closed implements FtBayState {

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();
	
	// Assuming 'myBayKey' is defined or can be derived for consistent logging
    // If FtBayState interface implies a getMyBayKey() method, this should be used.
	public String getMyBayKey() {
        return ConstantConveyor.FT_BAY_KEY; // Default for FT Bay
    }
	
	BayUtils bayUtils = new BayUtils();

	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		// Structured log entry for sequence start
		Ft.logger.info(String.format("[%s] : [FINGERTIP_STATUS_CHECK] : [SEQUENCE_ENTRY] - Ensuring fingertip latch is closed.", getMyBayKey()));

		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true); // Assume success initially
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		int try_count = 0;

		setSequencePathId("p1");
		setPalletAvailableTest_I_F_Status (null); // Resetting for current operation

		Map<String,Object> responseReturn = new HashMap<>(); // Initialize once outside the loop
		String fingerLatchPresentState = "";
		
		while(try_count <= 3){
			Ft.logger.debug(String.format("[%s] : [FINGERTIP_STATUS_CHECK] : [RETRY] - Attempt %d of 4 to verify fingertip latch status.", getMyBayKey(), try_count + 1));
			
			responseReturn = ftBay_FingerTipLatch_Status();
			fingerLatchPresentState = (String)responseReturn.get("responseData");
			setPalletAvailableTest_I_F_Status((TestInterfaceStatus)responseReturn.get("testInterfaceStatus"));

			if (Constant_IO_ActionMapping.OPEN.equals(fingerLatchPresentState)){  // Verify success
				// Log success
				Ft.logger.info(String.format("[%s] : [FINGERTIP_STATUS_CHECK] : [SUCCESS] - Finger Tip Latch confirmed closed. State: %s", getMyBayKey(), fingerLatchPresentState));

				// Assuming StateExecutorController.updateTestInterfaceStatusOnGui handles Platform.runLater() internally
				StateExecutorController.updateTestInterfaceStatusOnGui(responseReturn,ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
				ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setPalletsLockedImageDisplayOn(getMyBayKey(),true);
				ConveyorDataManager.getDashboardObject().getBayIndicatorManager().startTimeUpDisplay(getMyBayKey());
				if (ProconFeatureEnable.MOTOR_CONTROL_ENABLE) {
					Ft.logger.info(String.format("[%s] : [MOTOR_CONTROL] : [ENABLE_CHECK] - Motor control is enabled. Setting motor not required.", getMyBayKey()));
					BayUtils bayUtils = new BayUtils(); // Re-instantiate if needed, or get from class member
					Map<String,Object> motorResponse = bayUtils.set_motor_not_required(getMyBayKey());
					boolean set_motor_not_required = (boolean) motorResponse.get("status");

					if (set_motor_not_required) {
						Ft.logger.info(String.format("[%s] : [MOTOR_CONTROL] : [SUCCESS] - Motor set to not required.", getMyBayKey()));
						bayResponse.setStatus(true);
						bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
					} else {
						// Log motor control failure with error code
						Ft.logger.error(String.format("[%s] : [MOTOR_CONTROL] : [FAILED] - Failed to set motor not required. Error: %s", getMyBayKey(), ConvErrorCodeMapping.ERROR_CODE_CALIB_026));
						bayResponse.setStatus(false);
						bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_026);
					} 
				} else {
				    Ft.logger.debug(String.format("[%s] : [MOTOR_CONTROL] : [DISABLED] - Motor control is not enabled. Skipping motor settings.", getMyBayKey()));
				}
				break; // Exit loop on success
			}
			else{
				// Log failure for this attempt
				Ft.logger.warn(String.format("[%s] : [FINGERTIP_STATUS_CHECK] : [MISMATCH] - Finger Tip Latch state is not CLOSED. Expected: %s, Actual: %s. Retrying...", getMyBayKey(), Constant_IO_ActionMapping.OPEN, fingerLatchPresentState));
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_003);	
				BayUtils.delay(1000); // This delay can be blocking; ensure this sequence is called from a background thread.
				try_count++ ;
			}
		}

		if (!Constant_IO_ActionMapping.OPEN.equals(fingerLatchPresentState)) {
		    // Log final failure if loop completes without success
		    Ft.logger.error(String.format("[%s] : [FINGERTIP_STATUS_CHECK] : [FINAL_FAILURE] - Failed to ensure fingertip latch is closed after %d attempts. Final state: %s. Error: %s", getMyBayKey(), try_count, fingerLatchPresentState, ConvErrorCodeMapping.ERROR_CODE_FT_003));
		}

		// Structured log for sequence exit
		Ft.logger.info(String.format("[%s] : [FINGERTIP_STATUS_CHECK] : [SEQUENCE_EXIT] - Fingertip latch check sequence completed.", getMyBayKey()));
		return bayResponse;
	}
	//============================================================================================================================================  

	private Map<String,Object> ftBay_FingerTipLatch_Status() {
		// Structured debug log for method entry
		Ft.logger.debug(String.format("[%s] : [FINGERTIP_SENSOR_READ] : [REQUEST_ENTRY] - Reading fingertip latch sensor status.", getMyBayKey()));
		
		Map<String,Object> responseReturn = new HashMap<>();
		responseReturn.put("status", false); // Default status to false
		TestInterfaceStatus testIntefaceStatus = new TestInterfaceStatus();
		
		IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_SNSR_FINGER_TIP);

		if (portInfo != null) {
			Ft.logger.debug(String.format("[%s] : [FINGERTIP_SENSOR_READ] : [PORT_INFO] - PortId: %s, ClusterId: %s, BayId: %s", getMyBayKey(), portInfo.getPortId(), portInfo.getClusterId(), portInfo.getBayId()));
			
			if(getPalletAvailableTest_I_F_Status()==null){
				testIntefaceStatus = new TestInterfaceStatus(
						ConstantConveyor.FT_BAY_KEY,
						ConstantBayStateManage.BAY_HP_SEQ_04,
						ConstantConveyor.DEVICE_TYPE_CLUSTER_INPUT,
						getSequencePathId(),
						"-",
						portInfo.getPortId(),
						ConstantBayPortNameMapping.FT_PORT_NAME_SNSR_FINGER_TIP,
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
			Ft.logger.error(String.format("[%s] : [CONFIG_ERROR] : [FINGERTIP_SENSOR_READ] - Input port not found for fingertip latch sensor: %s", getMyBayKey(), ConstantBayPortNameMapping.FT_PORT_NAME_SNSR_FINGER_TIP));
			testIntefaceStatus.setDeviceResponseStatus("Failed");
			testIntefaceStatus.setDeviceResponseData("I/P port not found");
			StateExecutorController.addToTestStatusGui(testIntefaceStatus); // Ensure added if not present
			StateExecutorController.updateTestStatusGui(testIntefaceStatus);

			responseReturn.put("status", false);
			responseReturn.put("responseData", "CONFIG_ERROR"); // Indicate configuration error
			responseReturn.put("testInterfaceStatus", testIntefaceStatus);
			return responseReturn; // Early exit on critical config error
		}

		String state = "";
		try {
			if(ProcalFeatureEnable.MODBUS_PLC_SLAVE_MODE){ // Assuming ProcalFeatureEnable is correct here
				state = bayUtils.getInputDataFromPlcBay(portInfo) ;
			}else{
				state = bayUtils.getInputDataFromBayV2(portInfo) ;
			}
		} catch (Exception e) {
			// Log any exceptions during input data retrieval
			Ft.logger.error(String.format("[%s] : [COMMUNICATION_ERROR] : [FINGERTIP_SENSOR_READ] - Failed to get input data from fingertip latch sensor. Port: %s. Error: %s", getMyBayKey(), portInfo.getPortId(), e.getMessage()), e);
			testIntefaceStatus.setDeviceResponseStatus("Error");
			testIntefaceStatus.setDeviceResponseData("Communication Error");
			StateExecutorController.updateTestStatusGui(testIntefaceStatus); // Update GUI with error status
			responseReturn.put("status", false);
			responseReturn.put("responseData", state); // Still include raw state for debugging
			responseReturn.put("testInterfaceStatus", testIntefaceStatus);
			return responseReturn; // Early exit on communication failure
		}

		// Log the raw sensor state for debugging
		Ft.logger.debug(String.format("[%s] : [FINGERTIP_SENSOR_READ] : [RAW_STATE] : %s", getMyBayKey(), state));

		// Convert raw sensor state to meaningful action mapping constant
		// Based on previous contexts, OLD_OFF_NEW_ON seems to represent 'closed' state for active sensors.
		// Re-evaluating the logic here: if the sensor reports ON for closed, and OFF for open.
		// If `state.equals(Constant_IO_ActionMapping.OLD_OFF_NEW_ON)` means the sensor is ON (active),
		// and active implies closed, then the result should be OLD_CLOSE_NEW_OPEN.
		// If `state.equals(Constant_IO_ActionMapping.OLD_ON_NEW_OFF)` means the sensor is OFF (inactive),
		// and inactive implies open, then the result should be OLD_OPEN_NEW_CLOSE.
		String interpretedState = state.equals(Constant_IO_ActionMapping.ON) ? Constant_IO_ActionMapping.OPEN : Constant_IO_ActionMapping.CLOSE;

		if(StateExecutorController.simulateFtBayHappyPath){
			interpretedState = Constant_IO_ActionMapping.OPEN;
			// Shortened and moved to TRACE level for minimal impact
			Ft.logger.debug(String.format("[%s] : [SIMULATION] : Fingertip latch status overridden to CLOSED.", getMyBayKey()));
		}

		testIntefaceStatus.setPortName(portInfo.getPortId());
		
		if(interpretedState.equals(Constant_IO_ActionMapping.OPEN)){
			testIntefaceStatus.setDeviceResponseStatus("Success");
			responseReturn.put("status", true); // Status for responseReturn should reflect actual state
		}else{
			testIntefaceStatus.setDeviceResponseStatus("Failed");
			responseReturn.put("status", false); // Status for responseReturn should reflect actual state
		}
		
		if(portInfo.getPortId().equals(state)){ // This check seems to be for a timeout or invalid response
			testIntefaceStatus.setDeviceResponseData("TimeOut");
			Ft.logger.warn(String.format("[%s] : [FINGERTIP_SENSOR_READ] : [TIMEOUT] - Fingertip latch sensor read timed out or invalid response. Raw state: %s", getMyBayKey(), state));
		}else{
			testIntefaceStatus.setDeviceResponseData(interpretedState); // Store the interpreted state
		}

		StateExecutorController.updateTestStatusGui(testIntefaceStatus);

		responseReturn.put("responseData", interpretedState); // Return the interpreted state
		responseReturn.put("testInterfaceStatus", testIntefaceStatus);

		// Structured debug log for method exit
		Ft.logger.debug(String.format("[%s] : [FINGERTIP_SENSOR_READ] : [REQUEST_EXIT] - Fingertip latch sensor read completed. Interpreted state: %s", getMyBayKey(), interpretedState));
		return responseReturn;
	}
	//========================================================
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
