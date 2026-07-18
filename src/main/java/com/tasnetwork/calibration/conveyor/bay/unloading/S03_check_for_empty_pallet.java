package com.tasnetwork.calibration.conveyor.bay.unloading;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.bay.rejection.Rejection;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;

public class S03_check_for_empty_pallet implements UnloadingBayState {
	private boolean logEnabled = true;

	public String getMyBayKey() {
		return myBayKey;
	}

	// ===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Unloading.logger.info("S03_check_for_empty_pallet : handleRequest : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		Map<String, Object> responseReturn = check_for_empty_pallet();
		boolean check_for_empty_pallet = (boolean) responseReturn.get("status");

		boolean status = check_for_empty_pallet;

		if (status) {
			Unloading.logger.info("S03_check_for_empty_pallet :  ");
			// Logic for success case (status is true)
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601); // Success error code
		} else {
			Unloading.logger.info("S03_check_for_empty_pallet :  ");
			// Logic for failure case (status is false)
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_UNLOADING_009); // Failure error code
		}

		Unloading.logger.info("S03_check_for_empty_pallet : handleRequest : Exit");
		return bayResponse;
	}

	// ============================================================================================================================================

	private Map<String, Object> check_for_empty_pallet() {

		Unloading.logger.debug("S03_check_for_empty_pallet : check_for_empty_pallet : Entry");

		boolean status = false;
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);

		ConveyorDataManager.getDashboardObject().getBayIndicatorManager()
				.updateBayMonitoringAllPalletsExistInBay(getMyBayKey());
		boolean isPalletAvailableAt_UnloadingBay;
		long startTime;
		boolean stableDetectionForNoPallet = false;
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager()
				.updateBayMonitoringAllPalletsExistInBay(getMyBayKey());

		int emptyPalletDetectionStableWaitTimeInMSec = 1000 * DeviceDataManagerController.getConveyorConfigParsedKey()
				.getUnloadingPalletEmptyStableDetectionWaitTime_InSec();
		while (!stableDetectionForNoPallet
				&& !ConstantConveyor.ALL_LOOP_BREAK_FLAG
				&& !Unloading.isStopProcessRequestedUnloadingBay()) {
			responseReturn = isPalletAvailableAt_UnloadingBay();
			isPalletAvailableAt_UnloadingBay = (boolean) responseReturn.get("status");

			if (!isPalletAvailableAt_UnloadingBay) {
				startTime = System.currentTimeMillis();

				while (System.currentTimeMillis() - startTime < emptyPalletDetectionStableWaitTimeInMSec) {// ConstantConveyor.STABLE_PALLET_TIME)
																											// {
					BayUtils.delay(1000); // Small delay to prevent CPU overuse
					responseReturn = isPalletAvailableAt_UnloadingBay();
					isPalletAvailableAt_UnloadingBay = (boolean) responseReturn.get("status");

					if (isPalletAvailableAt_UnloadingBay) {
						break; // Reset if detection is lost
					}
				}

				// If detection lasted for stable pallet time, confirm stability
				if (!isPalletAvailableAt_UnloadingBay) {
					stableDetectionForNoPallet = true;
				}
			} else {
				if (logEnabled) {
					Unloading.logger.info("S03_check_for_empty_pallet : pallet Available at Unloading Bay");
				}
				BayUtils.delay(1000);
			}
			logEnabled = false;
		}

		if (stableDetectionForNoPallet) {
			Unloading.logger.info("S03_check_for_empty_pallet : No Pallet detected");
			ConveyorDataManager.getDashboardObject().getBayIndicatorManager()
					.updateBayAllPalletsExistInBay(getMyBayKey(), false);
			status = true;
			// bayResponse.setStatus(true);
			// bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			Unloading.logger.info("S03_check_for_empty_pallet : Pallet detected");
			ConveyorDataManager.getDashboardObject().getBayIndicatorManager()
					.updateBayAllPalletsExistInBay(getMyBayKey(), true);
			status = false;
			// bayResponse.setStatus(false);
			// bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_UNLOADING_001);
		}

		if (StateExecutorController.simulateUnloadingBayHappyPath) {
			status = true;
		}

		Unloading.logger.debug("S03_check_for_empty_pallet :  check_for_empty_pallet : status : " + status);

		responseReturn.put("status", status);

		Unloading.logger.debug("S03_check_for_empty_pallet :  check_for_empty_pallet : Exit");
		return responseReturn;
	}

	private Map<String, Object> isPalletAvailableAt_UnloadingBay() {
		if (logEnabled) {
			Unloading.logger.debug("S03_check_for_empty_pallet : isPalletAvailableAt_UnloadingBay : Entry");
		}
		boolean status = false;
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);

		IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.UNLOADING_PORT_NAME_SNSR_PALLET);

		if (portInfo != null) {

			if (logEnabled) {
				/*
				 * Unloading.logger.debug("PortId    : " + portInfo.getPortId());
				 * Unloading.logger.debug("ClusterId : " + portInfo.getClusterId());
				 * Unloading.logger.debug("BayId     : " + portInfo.getBayId());
				 */
				Unloading.logger.debug("isPalletAvailableAt_UnloadingBay : getClusterId: " + portInfo.getClusterId()
						+ " -> getBayId: " + portInfo.getBayId() + " -> getPortId: " + portInfo.getPortId());

			}
		} else {
			if (logEnabled) {
				Unloading.logger
						.debug("S03_check_for_empty_pallet : isPalletAvailableAt_UnloadingBay :Output port not found");
			}
			return responseReturn;
		}

		BayUtils bayUtils = new BayUtils();

		/*
		 * String state = bayUtils.getInputDataFromBay(portInfo.getClusterId(),
		 * portInfo.getBayId(),
		 * portInfo.getPortId());
		 */

		String state = bayUtils.getInputDataFromBayV2(portInfo);
		if (logEnabled) {
			Unloading.logger.debug("S03_check_for_empty_pallet : isPalletAvailableAt_UnloadingBay : state : " + state);
		}
		status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

		if (StateExecutorController.simulateUnloadingBayHappyPath) {
			status = true;
		}
		if (logEnabled) {
			Unloading.logger
					.debug("S03_check_for_empty_pallet : isPalletAvailableAt_UnloadingBay : status : " + status);
		}
		responseReturn.put("status", status);
		if (logEnabled) {
			Unloading.logger.debug("S03_check_for_empty_pallet : isPalletAvailableAt_UnloadingBay : Exit");
		}
		return responseReturn;
	}
}
