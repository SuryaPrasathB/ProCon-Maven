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
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

/**
 * State class responsible for issuing the command to close the fingertip latch
 * in the FT Bay.
 * It triggers the output port to close the latch to secure the pallet.
 */
public class S03_close_the_fingerTip_Latch implements FtBayState {

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	BayUtils bayUtils = new BayUtils();

	// ===========================================================================================
	@Override
	public BayResponse handleRequest() {
		// Structured log entry for sequence start
		Ft.logger.info(String.format(
				"[%s] : [FINGERTIP_CLOSE] : [SEQUENCE_ENTRY] - Initiating close fingertip latch sequence.",
				getMyBayKey()));

		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true); // Assume success initially
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601); // Default success code

		BayUtils.delay(1000); // This delay can be blocking; ensure this sequence is called from a background
								// thread.

		setSequencePathId("p1");
		setPalletAvailableTest_I_F_Status(null); // Resetting for current operation

		Map<String, Object> responseReturn = close_FingerTipLatch_FtBay();
		// Assuming StateExecutorController.updateTestInterfaceStatusOnGui handles
		// Platform.runLater() internally
		StateExecutorController.updateTestInterfaceStatusOnGui(responseReturn,
				ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);

		boolean closeFingerTipLatch_FtBay = (boolean) responseReturn.get("status");

		if (closeFingerTipLatch_FtBay) {
			// Structured log for success
			Ft.logger.info(String.format("[%s] : [FINGERTIP_CLOSE] : [SUCCESS] - Finger Tip Latch closed successfully.",
					getMyBayKey()));
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
			// ConveyorDeviceDataManagerController.getDashboardObject().updateBayEntryStopper(getMyBayKey(),false);
		} else {
			// Structured log for failure with error code
			Ft.logger.error(
					String.format("[%s] : [FINGERTIP_CLOSE] : [FAILED] - Failed to close Finger Tip Latch. Error: %s",
							getMyBayKey(), ConvErrorCodeMapping.ERROR_CODE_FT_027));
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_027);
		}

		// Structured log for sequence exit
		Ft.logger.info(
				String.format("[%s] : [FINGERTIP_CLOSE] : [SEQUENCE_EXIT] - Finger Tip Latch close sequence completed.",
						getMyBayKey()));
		return bayResponse;
	}
	// ============================================================================================================================================

	private Map<String, Object> close_FingerTipLatch_FtBay() {
		// Structured debug log for method entry
		Ft.logger.debug(String.format(
				"[%s] : [FINGERTIP_LATCH_COMMAND] : [REQUEST_ENTRY] - Requesting to close fingertip latch.",
				getMyBayKey()));

		boolean status = false;
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);

		IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_FINGER_TIP);
		TestInterfaceStatus testIntefaceStatus = new TestInterfaceStatus();

		if (portInfo != null) {
			Ft.logger.debug(String.format(
					"[%s] : [FINGERTIP_LATCH_COMMAND] : [PORT_INFO] - PortId: %s, ClusterId: %s, BayId: %s",
					getMyBayKey(), portInfo.getPortId(), portInfo.getClusterId(), portInfo.getBayId()));

			// Initialize TestInterfaceStatus
			testIntefaceStatus = new TestInterfaceStatus(
					ConstantConveyor.FT_BAY_KEY,
					ConstantBayStateManage.BAY_HP_SEQ_03,
					ConstantConveyor.DEVICE_TYPE_CLUSTER_OUTPUT,
					getSequencePathId(),
					"-",
					portInfo.getPortId(),
					ConstantBayPortNameMapping.FT_PORT_NAME_FINGER_TIP,
					ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
					"Waiting",
					ConstantConveyor.COMM_EXECUTION_STATUS_INP);

			// Add to GUI
			StateExecutorController.addToTestStatusGui(testIntefaceStatus);

			String state = "";
			String outputActive = Constant_IO_ActionMapping.OPEN;

			try {
				if (ProcalFeatureEnable.MODBUS_PLC_SLAVE_MODE) {
					state = getBayUtils().setOutputDataToPlcBay(portInfo.getClusterId(),
							portInfo.getBayId(),
							portInfo.getPortId(),
							outputActive);
				} else {
					state = getBayUtils().setOutputDataToBay(portInfo.getClusterId(),
							portInfo.getBayId(),
							portInfo.getPortId(), outputActive);
				}
			} catch (Exception e) {
				// Log any exceptions during output data transmission
				Ft.logger.error(String.format(
						"[%s] : [COMMUNICATION_ERROR] : [FINGERTIP_LATCH_COMMAND] - Failed to send close command to fingertip latch. Port: %s. Error: %s",
						getMyBayKey(), portInfo.getPortId(), e.getMessage()), e);
				testIntefaceStatus.setDeviceResponseStatus("Error");
				testIntefaceStatus.setDeviceResponseData("Communication Error");
				StateExecutorController.updateTestStatusGui(testIntefaceStatus); // Update GUI with error status
				responseReturn.put("status", false);
				responseReturn.put("responseData", state); // Still include raw state for debugging
				responseReturn.put("testInterfaceStatus", testIntefaceStatus);
				return responseReturn; // Early exit on communication failure
			}

			// Log the raw state received from the control system for debugging
			Ft.logger.debug(String.format("[%s] : [FINGERTIP_LATCH_COMMAND] : [RAW_STATE] : %s", getMyBayKey(), state));

			// Determine status based on expected response
			status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

			// Update TestInterfaceStatus based on command status
			testIntefaceStatus.setPortName(portInfo.getPortId());
			if (status) {
				testIntefaceStatus.setDeviceResponseStatus("Success");
			} else {
				testIntefaceStatus.setDeviceResponseStatus("Failed");
			}

			if (portInfo.getPortId().equals(state)) {
				testIntefaceStatus.setDeviceResponseData("TimeOut");
				Ft.logger.warn(String.format(
						"[%s] : [FINGERTIP_LATCH_COMMAND] : [TIMEOUT] - Fingertip latch command timed out. Raw state: %s",
						getMyBayKey(), state));
			} else {
				testIntefaceStatus.setDeviceResponseData(state);
			}

		} else {
			// Log a clear error if the port information is missing
			Ft.logger.error(String.format(
					"[%s] : [CONFIG_ERROR] : [FINGERTIP_LATCH_COMMAND] - Output port not found for fingertip latch: %s",
					getMyBayKey(), ConstantBayPortNameMapping.FT_PORT_NAME_FINGER_TIP));
			testIntefaceStatus.setDeviceResponseStatus("Failed");
			testIntefaceStatus.setDeviceResponseData("O/P port not found");
			// Ensure GUI status is updated even for config errors
			StateExecutorController.addToTestStatusGui(testIntefaceStatus); // Add if not already added, or update if
																			// already present.
			StateExecutorController.updateTestStatusGui(testIntefaceStatus);

			responseReturn.put("status", false); // Ensure status is false if portInfo is null
			responseReturn.put("responseData", "CONFIG_ERROR"); // Indicate configuration error in response data
			responseReturn.put("testInterfaceStatus", testIntefaceStatus);
			return responseReturn; // Early exit if critical config is missing
		}

		if (StateExecutorController.simulateFtBayHappyPath) {
			status = true;
			// Shortened and moved to TRACE level for minimal impact
			Ft.logger.debug(String.format("[%s] : [SIMULATION] : Fingertip latch close status overridden to TRUE.",
					getMyBayKey()));
		}

		// Update GUI with final status (assuming
		// StateExecutorController.updateTestStatusGui handles Platform.runLater()
		// internally)
		StateExecutorController.updateTestStatusGui(testIntefaceStatus);

		responseReturn.put("status", status);
		responseReturn.put("responseData", testIntefaceStatus.getDeviceResponseData()); // Use the updated response data
		responseReturn.put("testInterfaceStatus", testIntefaceStatus);

		// Structured debug log for method exit
		Ft.logger.debug(String.format(
				"[%s] : [FINGERTIP_LATCH_COMMAND] : [REQUEST_EXIT] - Fingertip latch close request completed. Status: %s",
				getMyBayKey(), status));
		return responseReturn;
	}

	public BayUtils getBayUtils() {
		return bayUtils;
	}

	public void setBayUtils(BayUtils bayUtils) {
		this.bayUtils = bayUtils;
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
