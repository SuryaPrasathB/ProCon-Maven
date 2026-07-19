package com.tasnetwork.calibration.conveyor.bay.verific_waiting;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S03_let_the_pallet_to_Verific_Bay implements WaitingBayState {
	// ===========================================================================================
	@Override
	public BayResponse handleRequest() {
		VerificWaiting.logger.info("S03_let_the_pallet_to_Verific_Bay : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		// =============================================================

		Map<String, Object> responseReturn = open_StopLatch_WaitingBay();
		boolean open_StopLatch_WaitingBay = (boolean) responseReturn.get("status");

		boolean openSuccess = open_StopLatch_WaitingBay;
		if (openSuccess) {

			BayUtils.delay(20000); // Time to allow pallets to move into bay

			responseReturn = close_StopLatch_WaitingBay();
			boolean close_StopLatch_WaitingBay = (boolean) responseReturn.get("status");

			boolean closeSuccess = close_StopLatch_WaitingBay;
			if (closeSuccess) {
				VerificWaiting.logger.info("S03_let_the_pallet_to_Verific_Bay : Opening and Closing Stopper Success");
				if (ProconFeatureEnable.MOTOR_CONTROL_ENABLE) {
					BayUtils bayUtils = new BayUtils();
					responseReturn = bayUtils.set_motor_not_required(getMyBayKey());
					boolean set_motor_not_required = (boolean) responseReturn.get("status");
					if (set_motor_not_required) {
						VerificWaiting.logger
								.info("S04_ensure_all_pallets_reached_Verific_Bay : set_motor_not_required : Success");
						bayResponse.setStatus(true);
						bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
					} else {
						VerificWaiting.logger
								.info("S04_ensure_all_pallets_reached_Verific_Bay : Failed to set_motor_not_required ");
						bayResponse.setStatus(false);
						bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_026);
					}
				} else {
					bayResponse.setStatus(true);
					bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
				}
			} else {
				VerificWaiting.logger.info("S03_let_the_pallet_to_Verific_Bay : Closing Stopper Failed");
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_WAITING_006); // Assuming 602 for failure
			}
		} else {
			VerificWaiting.logger.info("S03_let_the_pallet_to_Verific_Bay : Opening Stopper Failed");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_WAITING_005);
		}

		// =============================================================

		VerificWaiting.logger.info("S03_let_the_pallet_to_Verific_Bay : Exit");
		return bayResponse;
	}

	// ============================================================================================================================================

	private Map<String, Object> open_StopLatch_WaitingBay() {
		VerificWaiting.logger.debug("S03_let_the_pallet_to_Verific_Bay : open_StopLatch_WaitingBay : Entry");

		boolean status = false;
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);
		// ============================================================================================
		IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.WAITING_PORT_NAME_STPR);

		if (portInfo != null) {
			VerificWaiting.logger.debug("PortId    : " + portInfo.getPortId());
			VerificWaiting.logger.debug("ClusterId : " + portInfo.getClusterId());
			VerificWaiting.logger.debug("BayId     : " + portInfo.getBayId());
		} else {
			VerificWaiting.logger.debug("S03_let_the_pallet_to_Verific_Bay : Stopper Output port not found");
			return responseReturn;
		}

		BayUtils bayUtils = new BayUtils();

		String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
				portInfo.getBayId(),
				portInfo.getPortId(),
				Constant_IO_ActionMapping.CLOSE);
		status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false;

		if (StateExecutorController.simulateWaitingBayHappyPath) {
			status = true;
		}

		VerificWaiting.logger
				.debug("S03_let_the_pallet_to_Verific_Bay : open_StopLatch_WaitingBay : status : " + status);
		// ============================================================================================
		responseReturn.put("status", status);

		VerificWaiting.logger.debug("S03_let_the_pallet_to_Verific_Bay : open_StopLatch_WaitingBay : Exit");
		return responseReturn;
	}

	// ============================================================================================================================================

	private Map<String, Object> close_StopLatch_WaitingBay() {
		VerificWaiting.logger.debug("S03_let_the_pallet_to_Verific_Bay : close_StopLatch_WaitingBay : Entry");

		boolean status = false;
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);

		// ============================================================================================
		IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.WAITING_PORT_NAME_STPR);

		if (portInfo != null) {
			VerificWaiting.logger.debug("PortId    : " + portInfo.getPortId());
			VerificWaiting.logger.debug("ClusterId : " + portInfo.getClusterId());
			VerificWaiting.logger.debug("BayId     : " + portInfo.getBayId());
		} else {
			VerificWaiting.logger.debug("S03_let_the_pallet_to_Verific_Bay : Stopper Output port not found");
			return responseReturn;
		}

		BayUtils bayUtils = new BayUtils();

		String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
				portInfo.getBayId(),
				portInfo.getPortId(),
				Constant_IO_ActionMapping.OPEN);
		status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

		if (StateExecutorController.simulateWaitingBayHappyPath) {
			status = true;
		}

		VerificWaiting.logger
				.debug("S03_let_the_pallet_to_Verific_Bay : close_StopLatch_WaitingBay : status : " + status);
		// ============================================================================================

		responseReturn.put("status", status);

		VerificWaiting.logger.debug("S03_let_the_pallet_to_Verific_Bay : close_StopLatch_WaitingBay : Exit");
		return responseReturn;
	}

	// ============================================================================================================================================
}
