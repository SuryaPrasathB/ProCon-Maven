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
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;


public class S27_open_stop_latch_b4_FT_Bay implements FtBayState {

	public String getMyBayKey() {
		return myBayKey;
	}

	//boolean simulateFtBayHappyPath = true; // This is a class variable, but typically accessed statically.
	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	BayUtils bayUtils = new BayUtils();
	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		// Structured log entry for sequence start
		Ft.logger.info(String.format("[%s] : [STOP_LATCH_B4_CONTROL] : [SEQUENCE_ENTRY] - Attempting to open stop latch B4 at FT Bay.", getMyBayKey()));

		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		// If delay is needed, add log here
		// BayUtils.delay(500);

		setSequencePathId("p1"); // Set the sequence path ID
		setPalletAvailableTest_I_F_Status(null); // Reset TestInterfaceStatus, presumably for a fresh start

		Map<String, Object> responseReturn = open_StopLatch_B4_FtBay();
		// Update GUI after the operation
		StateExecutorController.updateTestInterfaceStatusOnGui(responseReturn, ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
		Ft.logger.debug(String.format("[%s] : [STOP_LATCH_B4_CONTROL] : [GUI_UPDATE] - Updated GUI status after attempting to open stop latch B4.", getMyBayKey()));

		boolean openStopLatch_B4_FtBay_status = (boolean) responseReturn.getOrDefault("status", false); // Get status safely

		if (openStopLatch_B4_FtBay_status) {
			Ft.logger.info(String.format("[%s] : [STOP_LATCH_B4_CONTROL] : [SUCCESS] - Stop Latch B4 successfully opened.", getMyBayKey()));
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
			ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayEntryStopper(getMyBayKey(),true);
			Ft.logger.debug(String.format("[%s] : [STOP_LATCH_B4_CONTROL] : [DELAY] - Delaying for 1000 ms after latch operation.", getMyBayKey()));
			BayUtils.delay(1000);
		} else {
			Ft.logger.error(String.format("[%s] : [STOP_LATCH_B4_CONTROL] : [FAILED] - Failed to open stop latch B4. Error Code: %s", getMyBayKey(), ConvErrorCodeMapping.ERROR_CODE_FT_027));
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_027);
		}

		// Structured log for sequence exit
		Ft.logger.info(String.format("[%s] : [STOP_LATCH_B4_CONTROL] : [SEQUENCE_EXIT] - Open stop latch B4 sequence completed. Final Status: %s", getMyBayKey(), bayResponse.getStatus()));
		return bayResponse;
	}
	//============================================================================================================================================

	private Map<String, Object> open_StopLatch_B4_FtBay() {
		// Structured debug log for method entry
		Ft.logger.debug(String.format("[%s] : [STOP_LATCH_B4_OPERATION] : [REQUEST_ENTRY] - Attempting to operate stop latch B4.", getMyBayKey()));

		boolean status = false;
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false); // Default boolean status

		//============================================================================================
		IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_STPR_B4);

		TestInterfaceStatus testInterfaceStatus = new TestInterfaceStatus();

		testInterfaceStatus.setBayName(ConstantConveyor.FT_BAY_KEY);
		testInterfaceStatus.setStateName(ConstantBayStateManage.BAY_HP_SEQ_00); // Consider updating to a more specific state if available for S27
		testInterfaceStatus.setcName(ConstantBayPortNameMapping.FT_PORT_NAME_STPR_B4);
		testInterfaceStatus.setDeviceType(ConstantConveyor.DEVICE_TYPE_CLUSTER_OUTPUT);
		testInterfaceStatus.setSerialStatus(ConstantConveyor.COMM_STATUS_NOT_APPLICABLE);
		testInterfaceStatus.setPositionNo(getSequencePathId());
		testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_INP);

		// Add to GUI and log
		int newRecordSerialNo = StateExecutorController.addToTestStatusGui(testInterfaceStatus);
		Ft.logger.debug(String.format("[%s] : [STOP_LATCH_B4_OPERATION] : [GUI_STATUS_INIT] - Added new GUI status record with serial no: %d for state %s, cName: %s.", getMyBayKey(), newRecordSerialNo, testInterfaceStatus.getStateName(), testInterfaceStatus.getcName()));

		String rawOutputState = "";
		if (portInfo != null) {
			Ft.logger.debug(String.format("[%s] : [STOP_LATCH_B4_OPERATION] : [PORT_INFO] - PortId: %s, ClusterId: %s, BayId: %s", getMyBayKey(), portInfo.getPortId(), portInfo.getClusterId(), portInfo.getBayId()));
			testInterfaceStatus.setPortName(portInfo.getPortId()); // Set port name for GUI

			String outputActive = Constant_IO_ActionMapping.OFF; // Assuming OLD_ON_NEW_OFF means 'open' or 'deactivate'
			Ft.logger.debug(String.format("[%s] : [STOP_LATCH_B4_OPERATION] : [SET_OUTPUT] - Attempting to set output to %s for port %s.", getMyBayKey(), outputActive, portInfo.getPortId()));

			try {
				rawOutputState = getBayUtils().setOutputDataToBay(portInfo.getClusterId(),
						portInfo.getBayId(),
						portInfo.getPortId(), outputActive);
				Ft.logger.debug(String.format("[%s] : [STOP_LATCH_B4_OPERATION] : [RAW_OUTPUT_STATE] : %s", getMyBayKey(), rawOutputState));

				// Check if the operation was successful based on the returned state
				// Assuming success if the returned state matches the desired active state
				status = rawOutputState.equals(Constant_IO_ActionMapping.OFF);

			} catch (Exception e) {
				Ft.logger.error(String.format("[%s] : [COMMUNICATION_ERROR] : [STOP_LATCH_B4_OPERATION] - Failed to set stop latch B4 state. Port: %s. Error: %s", getMyBayKey(), portInfo.getPortId(), e.getMessage()), e);
				status = false; // Indicate failure
				testInterfaceStatus.setDeviceResponseStatus("Error");
				testInterfaceStatus.setDeviceResponseData("Communication Error");
			}

			// Apply simulation override if enabled (this should override actual status)
			if (StateExecutorController.simulateFtBayHappyPath) {
				status = true; // Simulate success
				rawOutputState = Constant_IO_ActionMapping.OFF; // Simulate the expected successful state
				Ft.logger.debug(String.format("[%s] : [SIMULATION] : Stop latch B4 status overridden to SUCCESS for happy path. Raw state simulated: %s", getMyBayKey(), rawOutputState));
			}

			Ft.logger.debug(String.format("[%s] : [STOP_LATCH_B4_OPERATION] : [FINAL_STATUS] : %s", getMyBayKey(), status));

			// Update GUI status based on the operation's outcome
			if (status) {
				testInterfaceStatus.setDeviceResponseStatus("Success");
				testInterfaceStatus.setDeviceResponseData("Latch B4 Opened");
			} else {
				// If it failed due to communication error, it's already set. Otherwise, set general failure.
				if (!testInterfaceStatus.getDeviceResponseStatus().equals("Error")) {
					testInterfaceStatus.setDeviceResponseStatus("Failed");
					testInterfaceStatus.setDeviceResponseData("Latch B4 Not Opened");
				}
			}
			// The original logic `if (portInfo.getPortId().equals(state))` seems incorrect for checking timeout.
			// Assuming `state` contains the actual response from `setOutputDataToBay`.
			// Instead, `state` should reflect whether the operation was successful.
			// Removed the old timeout check, as `setOutputDataToBay` should handle its own response.

			testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED); // Mark status check as completed
			StateExecutorController.updateTestStatusGui(testInterfaceStatus);
			Ft.logger.debug(String.format("[%s] : [STOP_LATCH_B4_OPERATION] : [GUI_STATUS_COMPLETED] - Updated GUI with final status: %s for cName: %s.", getMyBayKey(), testInterfaceStatus.getDeviceResponseStatus(), testInterfaceStatus.getcName()));

		} else {
			// Log a clear error if the port information is missing
			Ft.logger.error(String.format("[%s] : [CONFIG_ERROR] : [STOP_LATCH_B4_OPERATION] - Output port not found for stop latch B4: %s", getMyBayKey(), ConstantBayPortNameMapping.FT_PORT_NAME_STPR_B4));
			testInterfaceStatus.setDeviceResponseStatus("Failed");
			testInterfaceStatus.setDeviceResponseData("O/P port not found");
			testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
			StateExecutorController.updateTestStatusGui(testInterfaceStatus); // Update GUI for config error
			responseReturn.put("status", false); // Indicate failure due to missing config
			responseReturn.put("responseData", "CONFIG_ERROR");
			responseReturn.put("testInterfaceStatus", testInterfaceStatus);
			return responseReturn; // Return early as portInfo is null
		}

		responseReturn.put("status", status);
		responseReturn.put("responseData", rawOutputState);
		responseReturn.put("testInterfaceStatus", testInterfaceStatus);


		// Structured debug log for method exit
		Ft.logger.debug(String.format("[%s] : [STOP_LATCH_B4_OPERATION] : [REQUEST_EXIT] - Stop latch B4 operation completed. Final Status: %s", getMyBayKey(), status));
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
