package com.tasnetwork.calibration.conveyor.bay.sta_nld1;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.Constant_Motor_Requirement;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;

import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S27_turn_on_motor_STA_NLDT_Bay1 implements STA_NoLoadTestBay1State {

	// ===========================================================================================
	@Override
	public BayResponse handleRequest() {
		StaNld_Bay1.logger.info("S27_turn_on_motor_STA_NLDT_Bay1 : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		Map<String, Object> responseReturn = turn_on();
		boolean turn_on_divertor_relay_FT_Bay = (boolean) responseReturn.get("status");

		if (turn_on_divertor_relay_FT_Bay) {
			StaNld_Bay1.logger.info("S27_turn_on_motor_STA_NLDT_Bay1 : Motor Turned On");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			StaNld_Bay1.logger.info("S27_turn_on_motor_STA_NLDT_Bay1 : Failed to Turn On Motor");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_017);
		}

		StaNld_Bay1.logger.info("S27_turn_on_motor_STA_NLDT_Bay1 : Exit");
		return bayResponse;
	}

	// ============================================================================================================================================

	private Map<String, Object> turn_on() {
		StaNld_Bay1.logger.debug("S27_turn_on_motor_STA_NLDT_Bay1 : turn_on : Entry");

		boolean status = false;
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);
		BayResponse bayResponse = new BayResponse();

		if (ProconFeatureEnable.MOTOR_CONTROL_ENABLE) {
			BayUtils.delay(2000);
			BayUtils bayUtils = new BayUtils();
			responseReturn = bayUtils.set_motor_required(getMyBayKey(), Constant_Motor_Requirement.STA1_MOTOR_REQUIRED);
			boolean set_motor_required = (boolean) responseReturn.get("status");
			if (set_motor_required) {
				StaNld_Bay1.logger.info("S27_turn_on_motor_STA_NLDT_Bay1: set_motor_required : Success");
				bayResponse.setStatus(true);
				status = true;
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
			} else {
				StaNld_Bay1.logger.info("S27_turn_on_motor_STA_NLDT_Bay1: Failed to set_motor_required ");
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_026);
			}
		} else {

			// ============================================================================================
			IoPortInfo portInfo = BayUtils
					.getOutputPortDetails(ConstantBayPortNameMapping.SCT_NLT1_PORT_NAME_MOTOR_CTRL);

			if (portInfo != null) {
				StaNld_Bay1.logger.debug("PortId    : " + portInfo.getPortId());
				StaNld_Bay1.logger.debug("ClusterId : " + portInfo.getClusterId());
				StaNld_Bay1.logger.debug("BayId     : " + portInfo.getBayId());
			} else {
				StaNld_Bay1.logger.debug(
						"S27_turn_on_motor_STA_NLDT_Bay1: S27_turn_on_motor_STA_NLDT_Bay1 : Output port not found");
			}

			BayUtils bayUtils = new BayUtils();

			String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
					portInfo.getBayId(),
					portInfo.getPortId(),
					Constant_IO_ActionMapping.OPEN);

			// Use CLOSE to represent turning the relay "On"

			status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

			StaNld_Bay1.logger.debug("S27_turn_on_motor_STA_NLDT_Bay1 : turn_on : status : " + status);
			// ============================================================================================

		}

		if (StateExecutorController.simulateSCTNLTBay1HappyPath) {
			status = true;
		}

		responseReturn.put("status", status);

		StaNld_Bay1.logger.debug("S27_turn_on_motor_STA_NLDT_Bay1 : Exit");
		return responseReturn;
	}
}
