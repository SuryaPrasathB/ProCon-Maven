package com.tasnetwork.calibration.conveyor.bay.ft;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.spring.orm.model.PalletManage;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

/**
 * State class responsible for checking the presence and stability of a pallet
 * at the FT Bay.
 * It waits for the pallet sensor to detect a pallet continuously for a
 * predefined stability duration.
 */
public class S01_check_for_pallet_at_FT_Bay implements FtBayState {

	private BayUtils bayUtils = new BayUtils();
	private String sequencePathId = "p1";
	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_01;
	private String palletSensorPortCname = ConstantBayPortNameMapping.FT_PORT_NAME_SNSR_PALLET;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_FT_001;
	private boolean logEnabled = true;
	private TestInterfaceStatus testInterfaceStatus = new TestInterfaceStatus();

	// ===========================================================================================
	@Override
	public BayResponse handleRequest() {
		// Log entry with structured format: [BAY_IDENTIFIER] : [ACTION_TYPE] :
		// [STATUS/OUTCOME]
		Ft.logger.info(String.format(
				"[%s] : [PALLET_DETECTION] : [SEQUENCE_ENTRY] - Starting pallet detection sequence.", getMyBayKey()));

		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(false);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601); // Default to a general error code
		ConveyorDataManager.getDashboardObject().removePalletFromBay(getMyBayKey());
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayMonitoringAllPalletsExistInBay(getMyBayKey());
		if (Ft.stopper_B4_FT_Bay_Status.equals("STOPPER_B4_FT_BAY_OPENED")) {

			setSequencePathId("p1");

			boolean isPalletAvailableAt_FtBay;
			long startTime;
			boolean stableDetection = false;
			// ConveyorDeviceDataManagerController.getDashboardObject().updateBayMonitoringAllPalletsExistInBay(getMyBayKey());
			while (!stableDetection && !ConstantConveyor.ALL_LOOP_BREAK_FLAG && !Ft.isStopProcessRequestedFtBay()) {
				Map<String, Object> responseReturn = isPalletAvailableAt_FtBay();
				isPalletAvailableAt_FtBay = (boolean) responseReturn.get("status");

				if (isPalletAvailableAt_FtBay) {
					// Log that a pallet has been detected, starting stable check
					if (logEnabled) {
						Ft.logger.debug(String.format(
								"[%s] : [PALLET_DETECTION] : [DETECTED] - Pallet detected, checking for stability.",
								getMyBayKey()));
					}
					startTime = System.currentTimeMillis();

					while ((System.currentTimeMillis() - startTime < ConstantConveyor.STABLE_PALLET_TIME_IN_MSEC)
							&& !Ft.isStopProcessRequestedFtBay()
							&& !ConstantConveyor.ALL_LOOP_BREAK_FLAG) {
						BayUtils.delay(1000); // Small delay to prevent CPU overuse
						responseReturn = isPalletAvailableAt_FtBay();
						isPalletAvailableAt_FtBay = (boolean) responseReturn.get("status");

						if (!isPalletAvailableAt_FtBay) {
							// Log that detection was lost before stability
							if (logEnabled) {
								Ft.logger.info(String.format(
										"[%s] : [PALLET_DETECTION] : [LOST] - Pallet detection lost during stability check. Retrying.",
										getMyBayKey()));
							}
							break; // Reset if detection is lost
						}
						logEnabled = false;
					}

					// If detection lasted for STABLE_PALLET_TIME, confirm stability
					if (isPalletAvailableAt_FtBay) {
						stableDetection = true;
					}
				} else {
					// Log that pallet is not yet available, waiting
					if (logEnabled) {
						Ft.logger.info(String.format(
								"[%s] : [PALLET_DETECTION] : [NOT_AVAILABLE] - No pallet at bay, waiting...",
								getMyBayKey()));
					}
					BayUtils.delay(1000);
				}
				logEnabled = false;
			}
			// logEnabled = true;
			if (stableDetection) {
				// Log success
				Ft.logger.info(String.format(
						"[%s] : [PALLET_DETECTION] : [AVAILABLE] - Pallet confirmed available and stable at bay.",
						getMyBayKey()));
				ConveyorDataManager.getDashboardObject().getBayIndicatorManager()
						.updateBayAllPalletsExistInBay(getMyBayKey(), true);
				bayResponse.setStatus(true);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601); // Success code
				// ConveyorDeviceDataManagerController.getDashboardObject().updateBayAllPalletsExistInBay(getMyBayKey(),true);
			} else {
				// Log error, indicating why detection failed (e.g., process stopped or never
				// detected)
				Ft.logger.error(String.format(
						"[%s] : [PALLET_DETECTION] : [FAILED] - Pallet not detected or process stopped before stable detection. Error: %s",
						getMyBayKey(), ConvErrorCodeMapping.ERROR_CODE_FT_002));
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_002); // Specific error code for this
																					// failure
			}
		} else if (Ft.stopper_B4_FT_Bay_Status.equals("STOPPER_B4_FT_BAY_CLOSED")) {
			// Log state for clarity
			Ft.logger.info(String.format(
					"[%s] : [STOPPER_STATUS] : [CLOSED] - Stopper B4 is closed. Checking for pallet.", getMyBayKey()));

			Map<String, Object> responseReturn = isPalletAvailableAt_FtBay();
			boolean isPalletAvailableAt_FtBay = (boolean) responseReturn.get("status");

			if (isPalletAvailableAt_FtBay) {
				// Log success
				Ft.logger.info(
						String.format("[%s] : [PALLET_DETECTION] : [AVAILABLE] - Pallet detected with stopper closed.",
								getMyBayKey()));
				bayResponse.setStatus(true);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
				ConveyorDataManager.getDashboardObject().getBayIndicatorManager()
						.updateBayAllPalletsExistInBay(getMyBayKey(), true);
			} else {
				// Log error
				Ft.logger.error(String.format(
						"[%s] : [PALLET_DETECTION] : [NOT_AVAILABLE] - No pallet detected with stopper closed. Error: %s",
						getMyBayKey(), ConvErrorCodeMapping.ERROR_CODE_FT_002));
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_002);
			}
		} else {
			// Handle unexpected stopper status
			Ft.logger.error(String.format(
					"[%s] : [STOPPER_STATUS] : [UNKNOWN] - Unexpected stopper B4 status: %s. Cannot proceed.",
					getMyBayKey(), Ft.stopper_B4_FT_Bay_Status));
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_001); // Or a new specific error code for
																				// unknown status
		}

		if (bayResponse.getStatus()) {
			markAsCompleteForPreviousFtBayPallet();
			Ft.logger.info("S01_check_for_pallet_at_FT_Bay: Waiting for Halt Pallet at FT-Entry");
			while (!Ft.isStopProcessRequestedFtBay() &&
					!ConstantConveyor.ALL_LOOP_BREAK_FLAG &&
					ConveyorDataManager.isHaltPalletActiveInFt()) {
				BayUtils.delay(1000);
			}

			Ft.logger.info("S01_check_for_pallet_at_FT_Bay: Waiting for Halt Pallet at FT-Exit");

		}

		Ft.logger.info(String.format("[%s] : [PALLET_DETECTION] : [SEQUENCE_EXIT] - Exiting pallet detection sequence.",
				getMyBayKey()));
		return bayResponse;
	}

	private void markAsCompleteForPreviousFtBayPallet() {

		List<PalletManage> palletManageList = MySqlServiceManager.getPalletManageService()
				.findByBayKeyAndPalletActive(getMyBayKey());
		for (PalletManage eachPalletManage : palletManageList) {
			eachPalletManage.setPalletActive(false);
			Ft.logger.debug("markAsCompleteForPreviousFtBayPallet - getPalletDistinctId: "
					+ eachPalletManage.getPalletDistinctId());
			MySqlServiceManager.getPalletManageService().saveToDb(eachPalletManage);

		}
	}

	// ============================================================================================================================================

	// ===============================================================================================

	private Map<String, Object> isPalletAvailableAt_FtBay() {
		// Log entry with structured format
		if (logEnabled) {
			Ft.logger.info(String.format("[%s] : [PALLET_SENSOR] : [READ_ENTRY] - Checking pallet sensor at FT Bay.",
					getMyBayKey()));
		}
		boolean status = false;
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);

		IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_SNSR_PALLET);
		TestInterfaceStatus testIntefaceStatus = new TestInterfaceStatus();

		if (portInfo != null) {
			if (logEnabled) {
				Ft.logger.debug(
						String.format("[%s] : [PALLET_SENSOR] : [PORT_INFO] - PortId: %s, ClusterId: %s, BayId: %s",
								getMyBayKey(), portInfo.getPortId(), portInfo.getClusterId(), portInfo.getBayId()));
			}
			if (getTestInterfaceStatus() == null) {
				testIntefaceStatus = new TestInterfaceStatus(
						ConstantConveyor.FT_BAY_KEY,
						ConstantBayStateManage.BAY_HP_SEQ_01,
						ConstantConveyor.DEVICE_TYPE_CLUSTER_INPUT,
						getSequencePathId(),
						"-",
						portInfo.getPortId(),
						ConstantBayPortNameMapping.FT_PORT_NAME_SNSR_PALLET,
						ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
						"Waiting",
						ConstantConveyor.COMM_EXECUTION_STATUS_INP);

				int newRecordSerialNo = StateExecutorController.addToTestStatusGui(testIntefaceStatus);
				testIntefaceStatus.setSerialNo(String.valueOf(newRecordSerialNo));
			} else {
				testIntefaceStatus = getTestInterfaceStatus();
			}

		} else {
			// Log a clear error if the port information is missing
			if (logEnabled) {
				Ft.logger.error(String.format(
						"[%s] : [CONFIG_ERROR] : [PALLET_SENSOR] - Output Port Not Found for pallet sensor: %s",
						getMyBayKey(), ConstantBayPortNameMapping.FT_PORT_NAME_SNSR_PALLET));
			}
			responseReturn.put("status", false); // Ensure status is false if portInfo is null
			responseReturn.put("testInterfaceStatus", testIntefaceStatus); // Return the status object even if
																			// incomplete
			return responseReturn; // Early exit if critical config is missing
		}

		BayUtils bayUtils = new BayUtils();
		String state = "";

		try {
			if (ProcalFeatureEnable.MODBUS_PLC_SLAVE_MODE) {
				state = bayUtils.getInputDataFromPlcBay(portInfo);
			} else {
				state = bayUtils.getInputDataFromBayV2(portInfo);
			}
		} catch (Exception e) {
			// Log any exceptions during input data retrieval
			Ft.logger.error(String.format(
					"[%s] : [COMMUNICATION_ERROR] : [PALLET_SENSOR] - Failed to get input data from bay. Port: %s. Error: %s",
					getMyBayKey(), portInfo.getPortId(), e.getMessage()), e);
			testIntefaceStatus.setDeviceResponseStatus("Error");
			testIntefaceStatus.setDeviceResponseData("Communication Error");
			StateExecutorController.updateTestStatusGui(testIntefaceStatus);
			responseReturn.put("status", false);
			responseReturn.put("responseData", false);
			responseReturn.put("testInterfaceStatus", testIntefaceStatus);
			return responseReturn;
		}

		// Log the raw sensor state for debugging
		if (logEnabled) {
			Ft.logger.debug(String.format("[%s] : [PALLET_SENSOR] : [RAW_STATE] : %s", getMyBayKey(), state));
		}
		status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

		if (StateExecutorController.simulateFtBayHappyPath) {
			status = true;
			Ft.logger.debug(
					String.format("[%s] : [SIMULATION] : Pallet sensor status overridden to TRUE.", getMyBayKey()));
		}

		testIntefaceStatus.setPortName(portInfo.getPortId());
		if (status) {
			testIntefaceStatus.setDeviceResponseStatus("Success");
			responseReturn.put("status", true);
		} else {
			testIntefaceStatus.setDeviceResponseStatus("Failed");
			responseReturn.put("status", false);
		}

		if (portInfo.getPortId().equals(state)) {
			state = "TimeOut";
			testIntefaceStatus.setDeviceResponseData("TimeOut");
			if (logEnabled) {
				Ft.logger.warn(String.format(
						"[%s] : [PALLET_SENSOR] : [TIMEOUT] - Pallet sensor read timed out. Raw state: %s",
						getMyBayKey(), state));
			}
		} else {
			testIntefaceStatus.setDeviceResponseData(state);
		}

		StateExecutorController.updateTestStatusGui(testIntefaceStatus);

		responseReturn.put("responseData", status);
		responseReturn.put("testInterfaceStatus", testIntefaceStatus);
		// Log the final resolved status
		if (logEnabled) {
			Ft.logger.info(String.format("[%s] : [PALLET_SENSOR] : [FINAL_STATUS] : %s", getMyBayKey(), status));
			Ft.logger.info(String.format("[%s] : [PALLET_SENSOR] : [READ_EXIT] - Exiting pallet sensor check.",
					getMyBayKey()));
		}
		return responseReturn;
	}

	// ============================================================================================

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

	public TestInterfaceStatus getTestInterfaceStatus() {
		return testInterfaceStatus;
	}

	public void setTestInterfaceStatus(TestInterfaceStatus palletAvailableTest_I_F_Status) {
		this.testInterfaceStatus = palletAvailableTest_I_F_Status;
	}

	public String getBayStateSequenceId() {
		return bayStateSequenceId;
	}

	public void setBayStateSequenceId(String bayStateSequenceId) {
		this.bayStateSequenceId = bayStateSequenceId;
	}

	public String getPalletSensorPortCname() {
		return palletSensorPortCname;
	}

	public String getFailStateErrorCode() {
		return failStateErrorCode;
	}

	public void setPalletSensorPortCname(String palletSensorPortCname) {
		this.palletSensorPortCname = palletSensorPortCname;
	}

	public void setFailStateErrorCode(String failStateErrorCode) {
		this.failStateErrorCode = failStateErrorCode;
	}

}
