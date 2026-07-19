package com.tasnetwork.calibration.conveyor.bay.calib;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
//import com.tasnetwork.calibration.conveyor.bay_highvoltagetest.HighVoltageTestBay;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S01_check_for_pallet_at_CALIB_Bay implements CalibrationBayState {

	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_01;
	private String palletSensorPortCname = ConstantBayPortNameMapping.CALIB_PORT_NAME_SNSR_PALLET;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_CALIB_001;
	private boolean logEnabled = true;

	@Override
	public BayResponse handleRequest() {
		Calib.logger.info("S01_check_for_pallet_at_CALIB_Bay : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		boolean isPalletAvailableAt_CalibBay;
		long startTime;
		boolean stableDetection = false;

		/*
		 * // Initial delay before checking
		 * BayUtils.delay(ConstantConveyor.STABLE_PALLET_TIME);
		 */
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager()
				.updateBayMonitoringAllPalletsExistInBay(getMyBayKey());
		while (!stableDetection && !ConstantConveyor.ALL_LOOP_BREAK_FLAG && !Calib.isStopProcessRequestedCalibBay()) {
			Map<String, Object> responseReturn = isPalletAvailableAt_CalibBay();
			isPalletAvailableAt_CalibBay = (boolean) responseReturn.get("status");

			if (isPalletAvailableAt_CalibBay) {
				startTime = System.currentTimeMillis();

				while (System.currentTimeMillis() - startTime < ConstantConveyor.STABLE_PALLET_TIME_IN_MSEC) {
					BayUtils.delay(1000); // Small delay to prevent CPU overuse
					responseReturn = isPalletAvailableAt_CalibBay();
					isPalletAvailableAt_CalibBay = (boolean) responseReturn.get("status");

					if (!isPalletAvailableAt_CalibBay) {
						break; // Reset if detection is lost
					}
				}

				// If detection lasted for stable pallet time, confirm stability
				if (isPalletAvailableAt_CalibBay) {
					stableDetection = true;
				}
			} else {
				if (logEnabled) {
					Calib.logger.info("S01_check_for_pallet_at_CALIB_Bay : No pallet Available at CALIB Bay");
				}
				BayUtils.delay(1000);
			}
			logEnabled = false;
		}

		if (stableDetection) {
			Calib.logger.info("S01_check_for_pallet_at_CALIB_Bay : Pallet Available");
			ConveyorDataManager.getDashboardObject().getBayIndicatorManager()
					.updateBayAllPalletsExistInBay(getMyBayKey(), true);
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			Calib.logger.info("S01_check_for_pallet_at_CALIB_Bay : Pallet Not Available");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_001);
		}

		if (bayResponse.getStatus()) {
			BayUtils bayUtils = new BayUtils();
			bayUtils.markAsCompleteForPreviousBayPallet(getMyBayKey(), Calib.logger);
		}
		Calib.logger.info("S01_check_for_pallet_at_CALIB_Bay : Exit");
		return bayResponse;
	}

	// ================================================================================
	private Map<String, Object> isPalletAvailableAt_CalibBay() {
		if (logEnabled) {
			Calib.logger.debug("S01_check_for_pallet_at_CALIB_Bay : Entry");
		}
		boolean status = false;
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);

		IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.CALIB_PORT_NAME_SNSR_PALLET);

		if (portInfo != null) {
			// Calib.logger.debug("PortId : " + portInfo.getPortId());
			// Calib.logger.debug("ClusterId : " + portInfo.getClusterId());
			// Calib.logger.debug("BayId : " + portInfo.getBayId());
			if (logEnabled) {
				Calib.logger.debug("isPalletAvailableAt_CalibBay : getClusterId: " + portInfo.getClusterId()
						+ " -> getBayId: " + portInfo.getBayId() + " -> getPortId: " + portInfo.getPortId());
			}
		} else {
			Calib.logger.debug("S01_check_for_pallet_at_CALIB_Bay : Output port not found");
			return responseReturn;
		}

		BayUtils bayUtils = new BayUtils();

		String state = bayUtils.getInputDataFromBayV2(portInfo);
		if (logEnabled) {
			Calib.logger.debug("S01_check_for_pallet_at_CALIB_Bay : state : " + state);
		}
		status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

		/*
		 * if(StateExecutorController.simulateCalibBayHappyPath){
		 * status = true;
		 * }
		 */
		if (logEnabled) {
			Calib.logger.debug("S01_check_for_pallet_at_CALIB_Bay : status : " + status);
		}
		responseReturn.put("status", status);
		if (logEnabled) {
			Calib.logger.debug("S01_check_for_pallet_at_CALIB_Bay : Exit");
		}
		return responseReturn;
	}

	// ================================================================================
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
