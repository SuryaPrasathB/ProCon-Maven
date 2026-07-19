package com.tasnetwork.calibration.conveyor.bay.verific;

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

public class S08_check_for_pallets_at_SCT_NLT_Bay1 implements VerificTestBayState {

	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_01;
	private String palletSensorPortCname = ConstantBayPortNameMapping.SCT_NLT_BAY1_SNSR_PALLET1;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_VERIFIC_013;

	@Override
	public BayResponse handleRequest() {
		Verification.logger.info("S08_check_for_pallets_at_SCT_NLT_Bay1 : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		// Map<String,Object> responseReturn = isPalletAvailableAt_SCT_NLT_Bay1();
		// boolean isPalletAvailableAt_SCT_NLT_Bay1 =
		// (boolean)responseReturn.get("status");

		Map<String, Object> responseReturn = new HashMap<String, Object>();// isPalletAvailableAt_SCT_NLT_Bay1();
		boolean isPalletAvailableAt_SCT_NLT_Bay1 = false;// (boolean)responseReturn.get("status");

		long startTime;
		boolean stableDetectionForNoPallet = false;

		int emptyPalletDetectionStableWaitTimeInMSec = ConstantConveyor.STABLE_PALLET_TIME_IN_MSEC;// 1000*
																									// DeviceDataManagerController.getConveyorConfigParsedKey().getUnloadingPalletEmptyStableDetectionWaitTime_InSec();

		startTime = System.currentTimeMillis();
		Verification.logger.debug("S08_check_for_pallets_at_SCT_NLT_Bay1 : No pallet sensing detection: Entry");
		while ((System.currentTimeMillis() - startTime < emptyPalletDetectionStableWaitTimeInMSec)
				&& (!ConstantConveyor.ALL_LOOP_BREAK_FLAG)
				&& (!Verification.isStopProcessRequestedVerificBay())) {// ConstantConveyor.STABLE_PALLET_TIME) {
			Verification.logger.debug("S08_check_for_pallets_at_SCT_NLT_Bay1 : No pallet sensing detection: awaiting");
			BayUtils.delay(1000); // Small delay to prevent CPU overuse
			responseReturn = isPalletAvailableAt_SCT_NLT_Bay1();
			isPalletAvailableAt_SCT_NLT_Bay1 = (boolean) responseReturn.get("status");

			if (isPalletAvailableAt_SCT_NLT_Bay1) {
				Verification.logger.debug("S08_check_for_pallets_at_SCT_NLT_Bay1 : pallet sensed: breaking loop");
				break; // Reset if detection is lost
			}
		}

		// If detection lasted for stable pallet time, confirm stability
		if (!isPalletAvailableAt_SCT_NLT_Bay1) {
			stableDetectionForNoPallet = true;
		}

		// if (!isPalletAvailableAt_SCT_NLT_Bay1) {
		if (stableDetectionForNoPallet) {
			Verification.logger.info("S08_check_for_pallets_at_SCT_NLT_Bay1 : No Pallet Available");
			ConveyorDataManager.getDashboardObject().getBayIndicatorManager()
					.updateBayAllPalletsExistInNextTargetBay(getMyBayKey(), false);

			if (ConveyorDataManager.isSta1PalletsAllCleared()) {
				Verification.logger.info("S08_check_for_pallets_at_SCT_NLT_Bay1 : STA1: Pallet all clear confirmed");
			} else {
				Verification.logger.info("S08_check_for_pallets_at_SCT_NLT_Bay1 : STA1: Pallet all NOT clear");
				int waitTimeInSec = ConstantConveyor.STABLE_PALLET_TIME_IN_MSEC / 1000;
				while ((!Verification.isStopProcessRequestedVerificBay()) &&
						(!ConveyorDataManager.isSta1PalletsAllCleared()) &&
						(waitTimeInSec > 0) &&
						!ConstantConveyor.ALL_LOOP_BREAK_FLAG) {
					Verification.logger
							.info("S08_check_for_pallets_at_SCT_NLT_Bay1 : STA1: awaiting for Pallet all clear");
					BayUtils.delay(1000);
					waitTimeInSec--;
				}
				Verification.logger.info("S08_check_for_pallets_at_SCT_NLT_Bay1 : STA1: all clear awiting exit: "
						+ ConveyorDataManager.isSta1PalletsAllCleared());

				if ((waitTimeInSec == 0) && (!ConveyorDataManager.isSta1PalletsAllCleared())) {
					Verification.logger.info(
							"S08_check_for_pallets_at_SCT_NLT_Bay1 : STA1: Timed Out: still Sta1AllPallets not Cleared : isSta1PalletsAllCleared:"
									+ ConveyorDataManager.isSta1PalletsAllCleared());
					bayResponse.setStatus(false);
					bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_013);
					Verification.logger
							.info("S08_check_for_pallets_at_SCT_NLT_Bay1 : STA1: Check for Sta2 availability");
				}
			}
			/*
			 * if (ProconFeatureEnable.MOTOR_CONTROL_ENABLE) {
			 * BayUtils bayUtils = new BayUtils();
			 * 
			 * 
			 * 
			 * responseReturn = bayUtils.set_motor_required(getMyBayKey(),
			 * Constant_Motor_Requirement.VERIFIC_MOTOR_STA1_REQUIRED);
			 * boolean set_motor_required = (boolean) responseReturn.get("status");
			 * if (set_motor_required) {
			 * VerificationTestBay.logger.info("set_motor_required : Success");
			 * bayResponse.setStatus(true);
			 * bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
			 * } else {
			 * VerificationTestBay.logger.info("Failed to set_motor_required ");
			 * bayResponse.setStatus(false);
			 * bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_026);
			 * }
			 * }
			 */
			// bayResponse.setStatus(true);
			// bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			Verification.logger.info("S08_check_for_pallets_at_SCT_NLT_Bay1 : Pallet  Available");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_013);

			BayUtils.delay(5000);
		}

		Verification.logger.info("S08_check_for_pallets_at_SCT_NLT_Bay1 : Exit");
		return bayResponse;
	}

	private Map<String, Object> isPalletAvailableAt_SCT_NLT_Bay1() {
		Verification.logger.debug("S08_check_for_pallets_at_SCT_NLT_Bay1 : Entry");

		boolean status = false;
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);

		IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.SCT_NLT_BAY1_SNSR_PALLET1);

		if (portInfo != null) {
			Verification.logger.debug("PortId    : " + portInfo.getPortId());
			Verification.logger.debug("ClusterId : " + portInfo.getClusterId());
			Verification.logger.debug("BayId     : " + portInfo.getBayId());
		} else {
			Verification.logger.debug("S08_check_for_pallets_at_SCT_NLT_Bay1 : Output port not found");
			return responseReturn;
		}

		BayUtils bayUtils = new BayUtils();

		/*
		 * String state = bayUtils.getInputDataFromBay(portInfo.getClusterId(),
		 * portInfo.getBayId(),
		 * portInfo.getPortId());
		 */

		String state = bayUtils.getInputDataFromBayV2(portInfo);

		Verification.logger.debug("S08_check_for_pallets_at_SCT_NLT_Bay1 : state : " + state);

		status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

		if (StateExecutorController.simulateVerificBayHappyPath) {
			status = true;
		}

		Verification.logger.debug("S08_check_for_pallets_at_SCT_NLT_Bay1 : status : " + status);

		responseReturn.put("status", status);

		Verification.logger.debug("S08_check_for_pallets_at_SCT_NLT_Bay1 : Exit");
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
