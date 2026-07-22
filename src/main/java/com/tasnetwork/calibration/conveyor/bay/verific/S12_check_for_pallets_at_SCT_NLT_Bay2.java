package com.tasnetwork.calibration.conveyor.bay.verific;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S12_check_for_pallets_at_SCT_NLT_Bay2 implements VerificTestBayState {

	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_01;
	private String palletSensorPortCname = ConstantBayPortNameMapping.SCT_NLT_BAY2_SNSR_PALLET1;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_VERIFIC_014;

	@Override
	public BayResponse handleRequest() {
		Verification.logger.info("S12_check_for_pallets_at_SCT_NLT_Bay2 : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		Map<String, Object> responseReturn = new HashMap<String, Object>();
		boolean isPalletAvailableAt_SCT_NLT_Bay2 = false;

		long startTime;
		boolean stableDetectionForNoPallet = false;

		int emptyPalletDetectionStableWaitTimeInMSec = ConstantConveyor.STABLE_PALLET_TIME_IN_MSEC;// 1000*
																									// DeviceDataManagerController.getConveyorConfigParsedKey().getUnloadingPalletEmptyStableDetectionWaitTime_InSec();

		startTime = System.currentTimeMillis();
		Verification.logger.debug("S12_check_for_pallets_at_SCT_NLT_Bay2 : No pallet sensing detection: Entry");
		while ((System.currentTimeMillis() - startTime < emptyPalletDetectionStableWaitTimeInMSec)
				&& (!ConstantConveyor.ALL_LOOP_BREAK_FLAG)
				&& (!Verification.isStopProcessRequestedVerificBay())) {// ConstantConveyor.STABLE_PALLET_TIME) {
			Verification.logger.debug("S12_check_for_pallets_at_SCT_NLT_Bay2 : No pallet sensing detection: awaiting");
			BayUtils.delay(1000); // Small delay to prevent CPU overuse
			responseReturn = isPalletAvailableAt_SCT_NLT_Bay2();
			isPalletAvailableAt_SCT_NLT_Bay2 = (boolean) responseReturn.get("status");

			if (isPalletAvailableAt_SCT_NLT_Bay2) {
				Verification.logger.debug("S12_check_for_pallets_at_SCT_NLT_Bay2 : pallet sensed: breaking loop");
				break; // Reset if detection is lost
			}
		}

		// If detection lasted for stable pallet time, confirm stability
		if (!isPalletAvailableAt_SCT_NLT_Bay2) {
			stableDetectionForNoPallet = true;
		}

		if (stableDetectionForNoPallet) {
			Verification.logger.info("S12_check_for_pallets_at_SCT_NLT_Bay2 : No Pallet Available");
			ConveyorDataManager.getDashboardObject().getBayIndicatorManager()
					.updateBayAllPalletsExistInNextTargetBay(getMyBayKey(), false);

			if (ConveyorDataManager.isSta2PalletsAllCleared()) {
				Verification.logger.info("S12_check_for_pallets_at_SCT_NLT_Bay2 : STA2: Pallet all clear confirmed");
			} else {
				Verification.logger.info("S12_check_for_pallets_at_SCT_NLT_Bay2 : STA2: Pallet all NOT clear");
				int waitTimeInSec = ConstantConveyor.STABLE_PALLET_TIME_IN_MSEC / 1000;

				while ((!Verification.isStopProcessRequestedVerificBay()) &&
						(!ConveyorDataManager.isSta2PalletsAllCleared()) &&
						(waitTimeInSec > 0) &&
						!ConstantConveyor.ALL_LOOP_BREAK_FLAG) {
					Verification.logger
							.info("S12_check_for_pallets_at_SCT_NLT_Bay2 : STA2: awaiting for Pallet all clear");
					BayUtils.delay(1000);
					waitTimeInSec--;
				}
				Verification.logger.info("S12_check_for_pallets_at_SCT_NLT_Bay2 : STA2: all clear awiting exit: "
						+ ConveyorDataManager.isSta2PalletsAllCleared());
				if ((waitTimeInSec == 0) && (!ConveyorDataManager.isSta2PalletsAllCleared())) {
					Verification.logger.info(
							"S12_check_for_pallets_at_SCT_NLT_Bay2 : STA2: Timed Out: still Sta1AllPallets not Cleared : isSta2PalletsAllCleared:"
									+ ConveyorDataManager.isSta2PalletsAllCleared());
					bayResponse.setStatus(false);
					bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_014);
					Verification.logger
							.info("S12_check_for_pallets_at_SCT_NLT_Bay2 : STA2: Check for Sta1 availability");
				}

			}

			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			Verification.logger.info("S12_check_for_pallets_at_SCT_NLT_Bay2 : Pallet Available");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_014);

			BayUtils.delay(5000);
		}

		Verification.logger.info("S12_check_for_pallets_at_SCT_NLT_Bay2 : Exit");
		return bayResponse;
	}

	private Map<String, Object> isPalletAvailableAt_SCT_NLT_Bay2() {
		Verification.logger.debug("S12_check_for_pallets_at_SCT_NLT_Bay2 : Entry");

		boolean status = false;
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);

		IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.SCT_NLT_BAY2_SNSR_PALLET1);

		TestInterfaceStatus testInterfaceStatus = null;
		if (portInfo != null) {
			testInterfaceStatus = new TestInterfaceStatus(
					ConstantConveyor.VERIFICATION_BAY_KEY,
					"-",
					ConstantConveyor.DEVICE_TYPE_CLUSTER_INPUT,
					"-",
					"-",
					portInfo.getPortId(),
					ConstantBayPortNameMapping.SCT_NLT_BAY2_SNSR_PALLET1,
					ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
					"Executing",
					ConstantConveyor.COMM_EXECUTION_STATUS_INP);
			StateExecutorController.addToTestStatusGui(testInterfaceStatus);

			Verification.logger.debug("PortId    : " + portInfo.getPortId());
			Verification.logger.debug("ClusterId : " + portInfo.getClusterId());
			Verification.logger.debug("BayId     : " + portInfo.getBayId());
		} else {
			Verification.logger.debug("S12_check_for_pallets_at_SCT_NLT_Bay2 : Output port not found");
			return responseReturn;
		}

		BayUtils bayUtils = new BayUtils();

		/*
		 * String state = bayUtils.getInputDataFromBay(portInfo.getClusterId(),
		 * portInfo.getBayId(),
		 * portInfo.getPortId());
		 * if (testInterfaceStatus != null) {
		 * testInterfaceStatus.setDeviceResponseData(state);
		 * testInterfaceStatus.setTestStatus("Success");
		 * StateExecutorController.updateTestStatusGui(testInterfaceStatus);
		 * }
		 */

		String state = bayUtils.getInputDataFromBayV2(portInfo);

		Verification.logger.debug("S12_check_for_pallets_at_SCT_NLT_Bay2 : state : " + state);

		status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

		if (StateExecutorController.simulateVerificBayHappyPath) {
			status = true;
		}

		Verification.logger.debug("S12_check_for_pallets_at_SCT_NLT_Bay2 : status : " + status);

		responseReturn.put("status", status);

		Verification.logger.debug("S12_check_for_pallets_at_SCT_NLT_Bay2 : Exit");
		return responseReturn;
	}

	public String getBayStateSequenceId() {
		return bayStateSequenceId;
	}

	/*
	 * public void setBayStateSequenceId(String bayStateSequenceId) {
	 * this.bayStateSequenceId = bayStateSequenceId;
	 * }
	 */
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
