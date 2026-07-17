package com.tasnetwork.calibration.conveyor.bay.verific;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.bay.bookshelf.CheckForPalletAtBay;
import com.tasnetwork.calibration.conveyor.bay.unloading.Unloading;
//import com.tasnetwork.calibration.conveyor.bay_waiting.WaitingBay;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;

public class S01_check_for_pallets_at_Verific_Bay implements VerificTestBayState {

	private BayUtils bayUtils = new BayUtils();
	private String sequencePathId = "p1";
	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_01;
	private String palletSensorPortCname =  ConstantBayPortNameMapping.VERIFIC_PORT_NAME_SNSR_PALLET;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_VERIFIC_001;

	private boolean logEnabled = true;
	public String getMyBayKey() {
		return myBayKey;
	}

	@Override
	public BayResponse handleRequest() {
		Verification.logger.info("S01_check_for_pallets_at_Verific_Bay : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);


		/*Map<String,Object> responseReturn =  isPalletAvailableAt_VerificBay();	 
		boolean isPalletAvailableAt_VerificBay = (boolean)responseReturn.get("status");

		BayUtils.delay(1000);

		responseReturn =  isPalletAvailableAt_VerificBay();	 
		isPalletAvailableAt_VerificBay = (boolean)responseReturn.get("status");


		while (!isPalletAvailableAt_VerificBay &&
				(!ConstantConveyor.ALL_LOOP_BREAK_FLAG))  {
			VerificationTestBay.logger.info("S01_check_for_pallets_at_Verific_Bay : No pallet Available at Verification Bay");
			BayUtils.delay(1000);

			responseReturn =  isPalletAvailableAt_VerificBay();	 
			isPalletAvailableAt_VerificBay = (boolean)responseReturn.get("status");

			BayUtils.delay(1000);

			responseReturn =  isPalletAvailableAt_VerificBay();	 
			isPalletAvailableAt_VerificBay = (boolean)responseReturn.get("status");

			ProjectExecutionController.getUserAbortedFlag();
		}

		if (isPalletAvailableAt_VerificBay) {
			VerificationTestBay.logger.info("S01_check_for_pallets_at_Verific_Bay : Pallet Available");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			VerificationTestBay.logger.info("S01_check_for_pallets_at_Verific_Bay : Pallet Not Available");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_001);
		}*/

		boolean isPalletAvailableAt_VerificBay;
		long startTime;
		boolean stableDetection = false;

		Map<String, Object> responseReturn = isPalletAvailableAt_VerificBay();
		isPalletAvailableAt_VerificBay = (boolean) responseReturn.get("status");

		/*BayUtils.delay(1000); // Initial delay*/

		// If the pallet is not available, check again
		if (!isPalletAvailableAt_VerificBay) {
			responseReturn = isPalletAvailableAt_VerificBay();
			isPalletAvailableAt_VerificBay = (boolean) responseReturn.get("status");
		}

		while ((!stableDetection) && (!ConstantConveyor.ALL_LOOP_BREAK_FLAG) && (!Verification.isStopProcessRequestedVerificBay())){
			if (isPalletAvailableAt_VerificBay) {
				startTime = System.currentTimeMillis();

				while (System.currentTimeMillis() - startTime < ConstantConveyor.STABLE_PALLET_TIME_IN_MSEC) {
					BayUtils.delay(1000); // Delay to prevent overloading the CPU

					responseReturn = isPalletAvailableAt_VerificBay();
					isPalletAvailableAt_VerificBay = (boolean) responseReturn.get("status");

					if (!isPalletAvailableAt_VerificBay) {
						break; // Break if pallet is no longer available
					}
				}

				if (isPalletAvailableAt_VerificBay) {
					stableDetection = true; // Mark as stable
				}
			} else {
				if(logEnabled) {
					Verification.logger.info("S01_check_for_pallets_at_Verific_Bay : No pallet Available at Verification Bay");
				}
				BayUtils.delay(1000); // Check again after some delay

				responseReturn = isPalletAvailableAt_VerificBay();
				isPalletAvailableAt_VerificBay = (boolean) responseReturn.get("status");
			}
			logEnabled = true;
		}

		if (stableDetection) {
			Verification.logger.info("S01_check_for_pallets_at_Verific_Bay : Pallet Available");
			Verification.logger.info("S01_check_for_pallets_at_Verific_Bay : Sleep");

			//ConstantConveyor.VERIFICATION_BAY_PALLETS_CLEARED = false;
			ConveyorDataManager.setVerific1PalletsAllCleared(false);

			BayUtils.delay(1000);
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			Verification.logger.info("S01_check_for_pallets_at_Verific_Bay : Pallet Not Available");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_001);
		}


		/*int consecutiveChecks = 0;
		int requiredChecks = 2;  // Number of times the pallet must be detected in a row

		while (consecutiveChecks < requiredChecks) {
		    WaitingBay.logger.info("S01_check_for_all_pallets_at_Waiting_Bay : Checking for pallet presence...");
		    BayUtils.delay(2000);

		    responseReturn = isPalletAvailableAt_VerificBay();
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

		Verification.logger.info("S01_check_for_pallets_at_Verific_Bay : Exit");
		return bayResponse;
	}

	//===========================================================================================

	private Map<String,Object> isPalletAvailableAt_VerificBay() {

		if(logEnabled) {
			Verification.logger.debug("S01_check_for_pallets_at_Verific_Bay : Entry");
		}
		boolean status = false;
		Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);

		IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.VERIFIC_PORT_NAME_SNSR_PALLET);

		if (portInfo != null) {


			if(logEnabled) {
				/*Verification.logger.debug("PortId    : " + portInfo.getPortId());
				Verification.logger.debug("ClusterId : " + portInfo.getClusterId());
				Verification.logger.debug("BayId     : " + portInfo.getBayId());*/
				Verification.logger.debug("isPalletAvailableAt_VerificBay : getClusterId: " +portInfo.getClusterId() + " -> getBayId: " + portInfo.getBayId() + " -> getPortId: " + portInfo.getPortId() );

			}
		} else {
			if(logEnabled) {
				Verification.logger.debug("S01_check_for_pallets_at_Verific_Bay : Output port not found");
			}
			return responseReturn ;
		}

		BayUtils bayUtils = new BayUtils();

		/*String state = bayUtils.getInputDataFromBay(portInfo.getClusterId(),
                portInfo.getBayId(),
                portInfo.getPortId());*/

		String state = bayUtils.getInputDataFromBayV2(portInfo) ;
		if(logEnabled) {
			Verification.logger.debug("S01_check_for_pallets_at_Verific_Bay : state : " + state);
		}
		status = state.equals(Constant_IO_ActionMapping.ON) ? true : false; 

		if(StateExecutorController.simulateVerificBayHappyPath){
			status = true; 
		}
		if(logEnabled) {
			Verification.logger.debug("S01_check_for_pallets_at_Verific_Bay : status : " + status); 
		}
		responseReturn.put("status", status);

		if(logEnabled) {
			Verification.logger.debug("S01_check_for_pallets_at_Verific_Bay : Exit");
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
