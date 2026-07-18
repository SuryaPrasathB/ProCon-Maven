package com.tasnetwork.calibration.conveyor.bay.hv;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S051_stop_HV_source implements HvtBayState {

	/*
	 * String LOW = "START";
	 * String HIGH = "STOP";
	 * String CLOSE = "Off";
	 * String OPEN = "On";
	 * String ON = "On";
	 * String OFF = "Off";
	 */
	// ===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Hv.logger.info("S051_stop_HV_source : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		Map<String, Object> responseReturn = turn_on_stop_pin_hv_bay();
		boolean turn_on_stop_pin_hv_bay = (boolean) responseReturn.get("status");

		if (turn_on_stop_pin_hv_bay) {
			BayUtils.delay(500);

			responseReturn = turn_off_stop_pin_hv_bay();
			boolean turn_off_stop_pin_hv_bay = (boolean) responseReturn.get("status");

			if (turn_off_stop_pin_hv_bay) {
				Hv.logger.info("S051_stop_HV_source : HV Source Stopped : Success ");
				bayResponse.setStatus(true);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
			} else {
				Hv.logger.info("S051_stop_HV_source : Failed to Turn Off Stop Pin HV Source");
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_HVT_016);
			}

		} else {
			Hv.logger.info("S051_stop_HV_source : Failed to Turn On Stop Pin HV Source");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_HVT_016);
		}

		Hv.logger.info("S051_stop_HV_source : Exit");
		return bayResponse;
	}
	// ============================================================================================================================================

	/*
	 * private Map<String, Object> stopHvSource() {
	 * 
	 * HighVoltageTestBay.logger.
	 * debug("S12_turn_on_start_pin_hv_bay : stopHvSource : Entry");
	 * 
	 * boolean status = false;
	 * Map<String,Object> responseReturn = new HashMap<String,Object>();
	 * responseReturn.put("status", false);
	 * 
	 * status = turn_on_stop_pin_hv_bay();
	 * 
	 * if (status) {
	 * HighVoltageTestBay.logger.
	 * debug("S12_turn_on_start_pin_hv_bay : stopHvSource : Turned on Source Stop Pin : Success"
	 * );
	 * BayUtils.delay(500);
	 * } else {
	 * HighVoltageTestBay.logger.
	 * debug("S12_turn_on_start_pin_hv_bay : stopHvSource : Failed to turn on Source Stop Pin"
	 * );
	 * status = false;
	 * }
	 * status = true;
	 * status = turn_off_stop_pin_hv_bay();
	 * if (status) {
	 * HighVoltageTestBay.logger.
	 * debug("S12_turn_on_start_pin_hv_bay : stopHvSource : Turned Off Source Stop Pin : Success"
	 * );
	 * 
	 * } else {
	 * HighVoltageTestBay.logger.
	 * debug("S12_turn_on_start_pin_hv_bay : stopHvSource : Failed to turn off Source Stop Pin"
	 * );
	 * status = false;
	 * }
	 * 
	 * HighVoltageTestBay.logger.
	 * debug("S12_turn_on_start_pin_hv_bay : stopHvSource : Exit");
	 * return responseReturn;
	 * }
	 */
	// ============================================================================================================================================

	private Map<String, Object> turn_on_stop_pin_hv_bay() {
		Hv.logger.debug("S12_turn_on_stop_pin_hv_bay : turn_on_stop_pin_hv_bay : Entry");

		boolean status = false;
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);

		IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.HV_PORT_NAME_SRC_STOP);

		if (portInfo != null) {
			Hv.logger.debug("PortId    : " + portInfo.getPortId());
			Hv.logger.debug("ClusterId : " + portInfo.getClusterId());
			Hv.logger.debug("BayId     : " + portInfo.getBayId());
		} else {
			Hv.logger.debug("S12_turn_on_stop_pin_hv_bay : turn_on_stop_pin_hv_bay : Output port not found");
			return responseReturn;
		}

		BayUtils bayUtils = new BayUtils();

		String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
				portInfo.getBayId(),
				portInfo.getPortId(),
				Constant_IO_ActionMapping.ON);
		status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

		if (StateExecutorController.simulateHvBayHappyPath) {
			status = true;
		}

		Hv.logger.debug("S12_turn_on_stop_pin_hv_bay : turn_on_stop_pin_hv_bay : status : " + status);

		responseReturn.put("status", status);

		Hv.logger.debug("S12_turn_on_stop_pin_hv_bay : turn_on_stop_pin_hv_bay : Exit");
		return responseReturn;
	}
	// ============================================================================================================================================

	private Map<String, Object> turn_off_stop_pin_hv_bay() {
		Hv.logger.debug("S12_turn_off_stop_pin_hv_bay : turn_off_stop_pin_hv_bay : Entry");

		boolean status = false;
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);

		IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.HV_PORT_NAME_SRC_STOP);

		if (portInfo != null) {
			Hv.logger.debug("PortId    : " + portInfo.getPortId());
			Hv.logger.debug("ClusterId : " + portInfo.getClusterId());
			Hv.logger.debug("BayId     : " + portInfo.getBayId());
		} else {
			Hv.logger.debug("S12_turn_off_stop_pin_hv_bay : turn_off_stop_pin_hv_bay : Output port not found");
			return responseReturn;
		}

		BayUtils bayUtils = new BayUtils();

		String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
				portInfo.getBayId(),
				portInfo.getPortId(),
				Constant_IO_ActionMapping.OFF);
		status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false;

		if (StateExecutorController.simulateHvBayHappyPath) {
			status = true;
		}

		Hv.logger.debug("S12_turn_off_stop_pin_hv_bay : turn_off_stop_pin_hv_bay : status : " + status);

		responseReturn.put("status", status);

		Hv.logger.debug("S12_turn_off_stop_pin_hv_bay : turn_off_stop_pin_hv_bay : Exit");
		return responseReturn;
	}
	// ============================================================================================================================================

}
