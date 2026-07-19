package com.tasnetwork.calibration.conveyor.bay.verific;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S06_open_the_fingerTip_Latch implements VerificTestBayState {

	// ===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Verification.logger.info("S06_open_the_fingerTip_Latch : Entry");

		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		Map<String, Object> responseReturn = open_FingerTipLatch_VerificBay();
		boolean open_FingerTipLatch_VerificBay = (boolean) responseReturn.get("status");

		if (open_FingerTipLatch_VerificBay) {
			Verification.logger.info("S06_open_the_fingerTip_Latch : Finger Tip Latch Opened");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			Verification.logger.info("S06_open_the_fingerTip_Latch : Failed to Open Finger Tip Latch");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_011);
		}

		Verification.logger.info("S06_open_the_fingerTip_Latch : Exit");
		return bayResponse;
	}
	// ============================================================================================================================================

	private Map<String, Object> open_FingerTipLatch_VerificBay() {
		Verification.logger.debug("S06_open_the_fingerTip_Latch : open_FingerTipLatch_VerificBay : Entry");

		boolean status = false;
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);

		// ============================================================================================
		IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.VERIFIC_PORT_NAME_FINGER_TIP);

		if (portInfo != null) {
			Verification.logger.debug("PortId    : " + portInfo.getPortId());
			Verification.logger.debug("ClusterId : " + portInfo.getClusterId());
			Verification.logger.debug("BayId     : " + portInfo.getBayId());
		} else {
			Verification.logger
					.debug("S06_open_the_fingerTip_Latch : open_FingerTipLatch_VerificBay : Output port not found");
			return responseReturn;
		}

		BayUtils bayUtils = new BayUtils();

		String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
				portInfo.getBayId(),
				portInfo.getPortId(),
				Constant_IO_ActionMapping.CLOSE);
		status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false;

		if (status) {
			ConveyorDataManager.getDashboardObject().getBayIndicatorManager()
					.setPalletsLockedImageDisplayOn(getMyBayKey(), false);
		}

		if (StateExecutorController.simulateVerificBayHappyPath) {
			status = true;
		}

		Verification.logger.debug("S06_open_the_fingerTip_Latch : open_FingerTipLatch_VerificBay : status : " + status);
		// ============================================================================================

		responseReturn.put("status", status);

		Verification.logger.debug("S06_open_the_fingerTip_Latch : open_FingerTipLatch_VerificBay : Exit");
		return responseReturn;
	}
}
