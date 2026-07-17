package com.tasnetwork.calibration.conveyor.bay.unloading;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.bay.bookshelf.CheckForPalletAtBay;
import com.tasnetwork.calibration.conveyor.bay.calib.Calib;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S01_check_for_pallet_at_Unloading_Bay implements UnloadingBayState {

	private BayUtils bayUtils = new BayUtils();
	private String sequencePathId = "p1";
	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_01 ;
	private String palletSensorPortCname =  ConstantBayPortNameMapping.UNLOADING_PORT_NAME_SNSR_PALLET  ;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_UNLOADING_001 ;
	private boolean logEnabled = true;
	public String getMyBayKey() {
		return myBayKey;
	}


	@Override
	public BayResponse handleRequest() {
		Unloading.logger.info("S01_check_for_pallet_at_Unloading_Bay : Entry");
		BayResponse bayResponse = new BayResponse();

		/*bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		Map<String,Object> responseReturn =  isPalletAvailableAt_UnloadingBay();	 
		boolean isPalletAvailableAt_UnloadingBay = (boolean)responseReturn.get("status");

		while (!isPalletAvailableAt_UnloadingBay &&
        		(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {

			responseReturn =  isPalletAvailableAt_UnloadingBay();	 
			isPalletAvailableAt_UnloadingBay = (boolean)responseReturn.get("status");

			UnloadingBay.logger.info("S01_check_for_pallet_at_Unloading_Bay : No pallet Available at Unloading Bay");
			BayUtils.delay(1000);
		}

		if (isPalletAvailableAt_UnloadingBay) {
			UnloadingBay.logger.info("S01_check_for_pallet_at_Unloading_Bay : Pallet Available");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			UnloadingBay.logger.info("S01_check_for_pallet_at_Unloading_Bay : Pallet Not Available");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_UNLOADING_001 );
		}*/

		boolean isPalletAvailableAt_UnloadingBay;
		long startTime;
		boolean stableDetection = false;
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayMonitoringAllPalletsExistInBay(getMyBayKey());
		while (!stableDetection && 
				!ConstantConveyor.ALL_LOOP_BREAK_FLAG &&
				!Unloading.isStopProcessRequestedUnloadingBay()
				) {
			Map<String, Object> responseReturn = isPalletAvailableAt_UnloadingBay();
			isPalletAvailableAt_UnloadingBay = (boolean) responseReturn.get("status");

			if (isPalletAvailableAt_UnloadingBay) {
				startTime = System.currentTimeMillis();

				while (System.currentTimeMillis() - startTime < ConstantConveyor.STABLE_PALLET_TIME_IN_MSEC) {
					BayUtils.delay(1000); // Small delay to prevent CPU overuse
					responseReturn = isPalletAvailableAt_UnloadingBay();
					isPalletAvailableAt_UnloadingBay = (boolean) responseReturn.get("status");

					if (!isPalletAvailableAt_UnloadingBay) {
						break; // Reset if detection is lost
					}
				}

				// If detection lasted for stable pallet time, confirm stability
				if (isPalletAvailableAt_UnloadingBay) {
					stableDetection = true;
				}
			} else {
				if(logEnabled) {
					Unloading.logger.info("S01_check_for_pallet_at_Unloading_Bay : No pallet Available at Unloading Bay");
				}
				BayUtils.delay(1000);
			}
			//logEnabled = false;
		}

		if (stableDetection) {
			Unloading.logger.info("S01_check_for_pallet_at_Unloading_Bay : Pallet Available");
			ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayAllPalletsExistInBay(getMyBayKey(),true);
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			Unloading.logger.info("S01_check_for_pallet_at_Unloading_Bay : Pallet Not Available");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_UNLOADING_001);
		}


		/*CheckForPalletAtBay bayPalletService = new CheckForPalletAtBay(FunctionalTestBay2.logger, 
				getMyBayKey(),
				getBayStateSequenceId(), 
				getPalletSensorPortCname(), 
				getFailStateErrorCode(),
				StateExecutorController.simulateFtBayHappyPath);
			bayResponse = bayPalletService.checkForPalletAtBayProcess();*/


		Unloading.logger.info("S01_check_for_pallet_at_Unloading_Bay : Exit");
		return bayResponse;
	}

	private Map<String,Object> isPalletAvailableAt_UnloadingBay() {
		if(logEnabled) {
			Unloading.logger.debug("S01_check_for_pallet_at_Unloading_Bay : isPalletAvailableAt_UnloadingBay : Entry");
		}
		boolean status = false;
		Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);

		IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.UNLOADING_PORT_NAME_SNSR_PALLET);

		if (portInfo != null) {
			if(logEnabled) {
				/*			Unloading.logger.debug("PortId    : " + portInfo.getPortId());
			Unloading.logger.debug("ClusterId : " + portInfo.getClusterId());
			Unloading.logger.debug("BayId     : " + portInfo.getBayId());*/
				Unloading.logger.debug("isPalletAvailableAt_UnloadingBay : getClusterId: " +portInfo.getClusterId() + " -> getBayId: " + portInfo.getBayId() + " -> getPortId: " + portInfo.getPortId() );

			}
		} else {
			if(logEnabled) {
				Unloading.logger.debug("S01_check_for_pallet_at_Unloading_Bay : isPalletAvailableAt_UnloadingBay :Output port not found");
			}
			return responseReturn ;
		}

		BayUtils bayUtils = new BayUtils();

		/*String state = bayUtils.getInputDataFromBay(portInfo.getClusterId(),
				portInfo.getBayId(),
				portInfo.getPortId());*/

		String state = bayUtils.getInputDataFromBayV2(portInfo) ;
		if(logEnabled) {
			Unloading.logger.debug("S01_check_for_pallet_at_Unloading_Bay : isPalletAvailableAt_UnloadingBay : state : " + state);
		}
		status = state.equals(Constant_IO_ActionMapping.ON) ? true : false; 

		if(StateExecutorController.simulateUnloadingBayHappyPath){
			status = true; 
		}
		if(logEnabled) {
			Unloading.logger.debug("S01_check_for_pallet_at_Unloading_Bay : isPalletAvailableAt_UnloadingBay : status : " + status);
		}
		responseReturn.put("status", status);
		if(logEnabled) {
			Unloading.logger.debug("S01_check_for_pallet_at_Unloading_Bay : isPalletAvailableAt_UnloadingBay : Exit");
		}
		return responseReturn;
	}


	public String getBayStateSequenceId() {
		return bayStateSequenceId;
	}


	/*	public void setBayStateSequenceId(String bayStateSequenceId) {
		this.bayStateSequenceId = bayStateSequenceId;
	}
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