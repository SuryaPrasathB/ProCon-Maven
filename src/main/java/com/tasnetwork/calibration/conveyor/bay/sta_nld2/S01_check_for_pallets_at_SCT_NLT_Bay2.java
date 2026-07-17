package com.tasnetwork.calibration.conveyor.bay.sta_nld2;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.bay.bookshelf.CheckForPalletAtBay;
//import com.tasnetwork.calibration.conveyor.bay.sta_nld1.StaNld_1;
//import com.tasnetwork.calibration.conveyor.bay.sta_nld1.STA_NoLoadTestBay1;
//import com.tasnetwork.calibration.conveyor.bay.sta_nld2.STA_NoLoadTestBay1;
//import com.tasnetwork.calibration.conveyor.bay_waiting.WaitingBay;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S01_check_for_pallets_at_SCT_NLT_Bay2 implements STA_NoLoadTestBay2State {

	private BayUtils bayUtils = new BayUtils();
	private String sequencePathId = "p1";
	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_01;
	private String palletSensorPortCname =  ConstantBayPortNameMapping.SCT_NLT_BAY2_SNSR_PALLET1;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_001;

	private boolean logEnabled = true;

	public String getMyBayKey() {
		return myBayKey;
	}

	@Override
	public BayResponse handleRequest() {
		StaNld_Bay2.logger.info("S01_check_for_pallets_at_SCT_NLT_Bay2 : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		StaNld_Bay2.setStopProcessRequestedStaNldBay2(false);
		StaNld_Bay2.logger.info("setStopProcessRequestedStaNldBay2 -Test1  : false");
		/*Map<String,Object> responseReturn =  isPalletAvailableAt_SCT_NLTBay2();	 
		boolean isPalletAvailableAt_SCT_NLTBay2 = (boolean)responseReturn.get("status");

		while (!isPalletAvailableAt_SCT_NLTBay2 &&
        		(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {

        	 STA_NoLoadTestBay2.logger.info("S01_check_for_pallets_at_SCT_NLT_Bay2 : No pallet Available at SCT NLT Bay2");
             BayUtils.delay(1000);

             responseReturn =  isPalletAvailableAt_SCT_NLTBay2();	 
    	   isPalletAvailableAt_SCT_NLTBay2 = (boolean)responseReturn.get("status");


        }

        if (isPalletAvailableAt_SCT_NLTBay2) {
            STA_NoLoadTestBay2.logger.info("S01_check_for_pallets_at_SCT_NLT_Bay2 : Pallet Available");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        } else {
            STA_NoLoadTestBay2.logger.info("S01_check_for_pallets_at_SCT_NLT_Bay2 : Pallet Not Available");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_001);
        }*/

		boolean isPalletAvailableAt_SCT_NLTBay2;
		long startTime;
		boolean stableDetection = false;

		while ((!stableDetection) && (!ConstantConveyor.ALL_LOOP_BREAK_FLAG)  
				&& (!StaNld_Bay2.isStopProcessRequestedStaNldBay2()) ){
			Map<String, Object> responseReturn = isPalletAvailableAt_SCT_NLTBay2();
			isPalletAvailableAt_SCT_NLTBay2 = (boolean) responseReturn.get("status");

			if (isPalletAvailableAt_SCT_NLTBay2) {
				startTime = System.currentTimeMillis();

				while (System.currentTimeMillis() - startTime < ConstantConveyor.STABLE_PALLET_TIME_IN_MSEC) {
					BayUtils.delay(1000); // Small delay to prevent CPU overuse
					responseReturn = isPalletAvailableAt_SCT_NLTBay2();
					isPalletAvailableAt_SCT_NLTBay2 = (boolean) responseReturn.get("status");

					if (!isPalletAvailableAt_SCT_NLTBay2) {
						break; // Reset if detection is lost
					}
				}

				// If detection lasted for stable pallet time, confirm stability
				if (isPalletAvailableAt_SCT_NLTBay2) {
					stableDetection = true;
				}
			} else {
				if(logEnabled) {
					StaNld_Bay2.logger.info("S01_check_for_pallets_at_SCT_NLT_Bay2 : No pallet Available at SCT NLT Bay2");
				}
				BayUtils.delay(1000);
			}
			logEnabled = false;
		}

		if (stableDetection) {
			StaNld_Bay2.logger.info("S01_check_for_pallets_at_SCT_NLT_Bay2 : Pallet Available");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
			ConveyorDataManager.setSta2PalletsAllCleared(false);
		} else {
			StaNld_Bay2.logger.info("S01_check_for_pallets_at_SCT_NLT_Bay2 : Pallet Not Available");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_001);
		}


		/*int consecutiveChecks = 0;
		int requiredChecks = 2;  // Number of times the pallet must be detected in a row

		while (consecutiveChecks < requiredChecks) {
		    WaitingBay.logger.info("S01_check_for_all_pallets_at_Waiting_Bay : Checking for pallet presence...");
		    BayUtils.delay(1000);

		    responseReturn = isPalletAvailableAt_SCT_NLTBay2();
		    boolean currentCheck = (boolean) responseReturn.get("status");

		    if (currentCheck) {
		        consecutiveChecks++;  // Increase count if pallet is detected
		        WaitingBay.logger.info("S01_check_for_all_pallets_at_Waiting_Bay : Pallet detected! Count: " + consecutiveChecks);
		    } else {
		        consecutiveChecks = 0;  // Reset count if detection fails
		        WaitingBay.logger.info("S01_check_for_all_pallets_at_Waiting_Bay : No pallet detected. Resetting check count.");
		    }
		}

		WaitingBay.logger.info("S01_check_for_all_pallets_at_Waiting_Bay : Pallet confirmed as present.");
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);*/

		/*CheckForPalletAtBay bayPalletService = new CheckForPalletAtBay(FunctionalTestBay2.logger, 
				getMyBayKey(),
				getBayStateSequenceId(), 
				getPalletSensorPortCname(), 
				getFailStateErrorCode(),
				StateExecutorController.simulateFtBayHappyPath);
		bayResponse = bayPalletService.checkForPalletAtBayProcess();*/

		StaNld_Bay2.logger.info("S01_check_for_pallets_at_SCT_NLT_Bay2 : Exit");
		return bayResponse;
	}

	// =======================================================================================================================================================

	private Map<String,Object> isPalletAvailableAt_SCT_NLTBay2() {
		if(logEnabled) {
			StaNld_Bay2.logger.debug("S01_check_for_pallets_at_SCT_NLT_Bay2 : isPalletAvailableAt_SCT_NLTBay2:  Entry");
		}
		boolean status = false;
		Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);

		IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.SCT_NLT_BAY2_SNSR_PALLET1);

		if (portInfo != null) {


			/*StaNld_Bay2.logger.debug("PortId    : " + portInfo.getPortId());
			StaNld_2.logger.debug("ClusterId : " + portInfo.getClusterId());
			StaNld_2.logger.debug("BayId     : " + portInfo.getBayId());*/
			if(logEnabled) {
				StaNld_Bay2.logger.debug("isPalletAvailableAt_SCT_NLTBay2 : getClusterId: " +portInfo.getClusterId() + " -> getBayId: " + portInfo.getBayId() + " -> getPortId: " + portInfo.getPortId() );
			}
		} else {
			if(logEnabled) {
				StaNld_Bay2.logger.debug("S01_check_for_pallets_at_SCT_NLT_Bay2 : Output port not found");
			}
			return responseReturn ;
		}

		BayUtils bayUtils = new BayUtils();

		/*String state = bayUtils.getInputDataFromBay(portInfo.getClusterId(),
				portInfo.getBayId(),
				portInfo.getPortId());*/

		String state = bayUtils.getInputDataFromBayV2(portInfo) ;
		if(logEnabled) {
			StaNld_Bay2.logger.debug("S01_check_for_pallets_at_SCT_NLT_Bay2 : state : " + state);
		}
		status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

		if(StateExecutorController.simulateSCTNLTBay2HappyPath){
			status = true; 
		}
		if(logEnabled) {
			StaNld_Bay2.logger.debug("S01_check_for_pallets_at_SCT_NLT_Bay2 : status : " + status); 
		}
		responseReturn.put("status", status);

		if(logEnabled) {
			StaNld_Bay2.logger.debug("S01_check_for_pallets_at_SCT_NLT_Bay2 : Exit");
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
