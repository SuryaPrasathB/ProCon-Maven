package com.tasnetwork.calibration.conveyor.bay.sta_nld2;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.pallet.PalletTrackerController;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S15_open_stop_latch_STA_NLDT_Bay2 implements STA_NoLoadTestBay2State {

	@Override
	public BayResponse handleRequest() {
		StaNld_Bay2.logger.info("S15_open_stop_latch_SCT_NLT_Bay2 : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		Map<String, Object> responseReturn = open_StopLatch_SCT_NLT_Bay2();

		boolean openStopLatch_SCT_NLT_Bay2 = (boolean) responseReturn.get("status");

		if (openStopLatch_SCT_NLT_Bay2) {
			StaNld_Bay2.logger.info("S15_open_stop_latch_SCT_NLT_Bay2 : Stop Latch Opened");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

			int palletMoveMentWaitTimeInSec = ConstantConveyorConfig.STA2_TO_UNLOADING_BAY_PALLET_MOVEMENT_WAIT_TIME_IN_SEC;// 30;
			StaNld_Bay2.logger.info("S15_open_stop_latch_SCT_NLT_Bay2 : palletMoveMentSta2ToUnloading : start:");
			
			// Log exit from current bay before the wait
			PalletTrackerController palletTracker = new PalletTrackerController();
			palletTracker.exitBatchFromPresentBay(getMyBayKey());
			while ((palletMoveMentWaitTimeInSec > 0) && (!BayUtils.isUserAborted())) {
				StaNld_Bay2.logger.info(
						"S15_open_stop_latch_SCT_NLT_Bay2 : palletMoveMentSta2ToUnloading : palletMoveMentWaitTimeInSec: "
								+ palletMoveMentWaitTimeInSec);
				BayUtils.delay(1000);
				palletMoveMentWaitTimeInSec--;
			}
			StaNld_Bay2.logger.info("S15_open_stop_latch_SCT_NLT_Bay2 : palletMoveMentSta2ToUnloading : stop:");

			if (ProconFeatureEnable.MOTOR_CONTROL_ENABLE) {
				BayUtils bayUtils = new BayUtils();
				responseReturn = bayUtils.set_motor_required(getMyBayKey());
				boolean set_motor_required = (boolean) responseReturn.get("status");
				if (set_motor_required) {
					StaNld_Bay2.logger.info("set_motor_required : Success");
					bayResponse.setStatus(true);
					bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
				} else {
					StaNld_Bay2.logger.info("Failed to set_motor_required ");
					bayResponse.setStatus(false);
					bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_026);
				}
			}

		} else {
			StaNld_Bay2.logger.info("S15_open_stop_latch_SCT_NLT_Bay2 : Failed to Open Stop Latch");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_008);
		}

		if (ProconFeatureEnable.MOTOR_CONTROL_ENABLE) {
			BayUtils bayUtils = new BayUtils();
			responseReturn = bayUtils.set_motor_not_required(getMyBayKey());
			boolean set_motor_not_required = (boolean) responseReturn.get("status");
			if (set_motor_not_required) {
				StaNld_Bay2.logger
						.info("S15_open_stop_latch_SCT_NLT_Bay2 : set_motor_not_required : Success");
				bayResponse.setStatus(true);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601); // ERROR_CODE_VERIFIC_022
			} else {
				StaNld_Bay2.logger
						.info("S15_open_stop_latch_SCT_NLT_Bay2 : Failed to set_motor_not_required ");
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_026);
			}
		}

		StaNld_Bay2.logger.info("S15_open_stop_latch_SCT_NLT_Bay2 : Exit");
		return bayResponse;
	}

	private Map<String, Object> open_StopLatch_SCT_NLT_Bay2() {
		StaNld_Bay2.logger.debug("S15_open_stop_latch_SCT_NLT_Bay2 : sctNltBay2_StopLatch_Status : Entry");

		boolean status = false;
		Map<String, Object> responseReturn = new HashMap<>();
		responseReturn.put("status", false);

		IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.SCT_NLT2_PORT_NAME_STPR);

		String state = "";

		if (portInfo != null) {
			StaNld_Bay2.logger.debug("PortId    : " + portInfo.getPortId());
			StaNld_Bay2.logger.debug("ClusterId : " + portInfo.getClusterId());
			StaNld_Bay2.logger.debug("BayId     : " + portInfo.getBayId());

			String outputActive = Constant_IO_ActionMapping.OFF;
			BayUtils bayUtils = new BayUtils();

			state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
					portInfo.getBayId(),
					portInfo.getPortId(),
					outputActive);

			StaNld_Bay2.logger
					.debug("S15_open_stop_latch_SCT_NLT_Bay2 : open_StopLatch_SCT_NLT_Bay2 : state : " + state);

			status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false;

			if (StateExecutorController.simulateSCTNLTBay2HappyPath) {
				status = true;
			}

			StaNld_Bay2.logger
					.debug("S15_open_stop_latch_SCT_NLT_Bay2 : open_StopLatch_SCT_NLT_Bay2 : status : " + status);

		} else {
			StaNld_Bay2.logger.debug("S15_open_stop_latch_SCT_NLT_Bay2 : Output port not found");
			return responseReturn;
		}

		responseReturn.put("status", status);
		responseReturn.put("responseData", state);

		StaNld_Bay2.logger.debug("S15_open_stop_latch_SCT_NLT_Bay2 : sctNltBay2_StopLatch_Status : status : " + status);
		StaNld_Bay2.logger.debug("S15_open_stop_latch_SCT_NLT_Bay2 : sctNltBay2_StopLatch_Status : Exit");
		return responseReturn;
	}
}
