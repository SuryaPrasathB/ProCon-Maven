package com.tasnetwork.calibration.conveyor.bay.verific_waiting;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.bay.bookshelf.CheckForPalletAtBay;
import com.tasnetwork.calibration.conveyor.bay.verific.Verification;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S01_check_for_all_pallets_at_Waiting_Bay implements WaitingBayState {

	private BayUtils bayUtils = new BayUtils();
	private String sequencePathId = "p1";
	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_01;
	private String palletSensorPortCname =  ConstantBayPortNameMapping.WAITING_PORT_NAME_SNSR_PALLET;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_WAITING_001;
	private boolean logEnabled = true;

	public String getMyBayKey() {
		return myBayKey;
	}

	@Override
	public BayResponse handleRequest() {
		VerificWaiting.logger.info("S01_check_for_all_pallets_at_Waiting_Bay : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		//======================================================================

		Map<String, Object> responseReturn;
		boolean isPalletAvailableAt_WaitingBay;
		long startTime;
		boolean stableDetection = false;

		/*// Initial delay before checking
        BayUtils.delay(3000);*/

		while (!stableDetection && !ConstantConveyor.ALL_LOOP_BREAK_FLAG 
				&& VerificWaiting.isStartProcessRequestedWaitingBay()) {
			if(logEnabled) {
				VerificWaiting.logger.info("S01_check_for_all_pallets_at_Waiting_Bay : Not All pallets Available at Waiting Bay");
			}
			responseReturn = isPalletAvailableAt_WaitingBay();
			isPalletAvailableAt_WaitingBay = (boolean) responseReturn.get("status");

			if (isPalletAvailableAt_WaitingBay) {
				startTime = System.currentTimeMillis();

				while (System.currentTimeMillis() - startTime < 3000) {
					BayUtils.delay(1000); // Small delay to prevent CPU overuse
					responseReturn = isPalletAvailableAt_WaitingBay();
					isPalletAvailableAt_WaitingBay = (boolean) responseReturn.get("status");

					if (!isPalletAvailableAt_WaitingBay) {
						break; // Reset if detection is lost
					}
				}

				// If detection lasted for 2 seconds, confirm stability
				if (isPalletAvailableAt_WaitingBay) {
					stableDetection = true;
				}
			} else {
				if(logEnabled) {
					VerificWaiting.logger.info("S01_check_for_all_pallets_at_Waiting_Bay : Not All pallets Available at Waiting Bay");
				}
				BayUtils.delay(1000);
			}
			logEnabled = false;
		}

		if (stableDetection) {
			VerificWaiting.logger.info("S01_check_for_all_pallets_at_Waiting_Bay : All Pallets Available at Waiting Bay");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			VerificWaiting.logger.info("S01_check_for_all_pallets_at_Waiting_Bay : Pallets Not Available at Waiting Bay");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_WAITING_001);
		}


		//======================================================================

		/*Map<String,Object> responseReturn =  isPalletAvailableAt_WaitingBay();	 
		boolean isPalletAvailableAt_WaitingBay = (boolean)responseReturn.get("status");

		while (!isPalletAvailableAt_WaitingBay &&
				(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {

			WaitingBay.logger.info("S01_check_for_all_pallets_at_Waiting_Bay : No pallet Available at Waiting Bay");
			BayUtils.delay(1000);

			responseReturn =  isPalletAvailableAt_WaitingBay();	 ///
			isPalletAvailableAt_WaitingBay = (boolean)responseReturn.get("status");///


		}

        if (isPalletAvailableAt_WaitingBay) {
            WaitingBay.logger.info("S01_check_for_all_pallets_at_Waiting_Bay : Pallet Available");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        } else {
            WaitingBay.logger.info("S01_check_for_all_pallets_at_Waiting_Bay : Pallet Not Available");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_WAITING_001);
        }*/
		//======================================================================

		/*		int consecutiveChecks = 0;
		int requiredChecks = 2;  // Number of times the pallet must be detected in a row

		while (consecutiveChecks < requiredChecks) {
		    WaitingBay.logger.info("S01_check_for_all_pallets_at_Waiting_Bay : Checking for pallet presence...");
		    BayUtils.delay(1000);

		    responseReturn = isPalletAvailableAt_WaitingBay();
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
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		 */
		/*CheckForPalletAtBay bayPalletService = new CheckForPalletAtBay(FunctionalTestBay2.logger, 
				getMyBayKey(),
				getBayStateSequenceId(), 
				getPalletSensorPortCname(), 
				getFailStateErrorCode(),
				StateExecutorController.simulateFtBayHappyPath);
			bayResponse = bayPalletService.checkForPalletAtBayProcess();*/

		VerificWaiting.logger.info("S01_check_for_all_pallets_at_Waiting_Bay : Exit");
		return bayResponse;
	}

	// =======================================================================================================================================================

	private Map<String,Object> isPalletAvailableAt_WaitingBay() {

		if(logEnabled) {
			VerificWaiting.logger.debug("isPalletAvailableAt_WaitingBay : Entry");
		}
		boolean status = false;
		Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);

		IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.WAITING_PORT_NAME_SNSR_PALLET);

		if (portInfo != null) {
			if(logEnabled) {
				/*VerificWaiting.logger.debug("PortId    : " + portInfo.getPortId());
				VerificWaiting.logger.debug("ClusterId : " + portInfo.getClusterId());
				VerificWaiting.logger.debug("BayId     : " + portInfo.getBayId());*/
				VerificWaiting.logger.debug("isPalletAvailableAt_WaitingBay : getClusterId: " +portInfo.getClusterId() + " -> getBayId: " + portInfo.getBayId() + " -> getPortId: " + portInfo.getPortId() );

			}
		} else {
			if(logEnabled) {
				VerificWaiting.logger.debug("isPalletAvailableAt_WaitingBay : Output port not found");
			}
			return responseReturn ;
		}

		BayUtils bayUtils = new BayUtils();

		/*String state = bayUtils.getInputDataFromBay(portInfo.getClusterId(),
                portInfo.getBayId(),
                portInfo.getPortId());*/

		String state = bayUtils.getInputDataFromBayV2(portInfo) ;
		if(logEnabled) {
			VerificWaiting.logger.debug("isPalletAvailableAt_WaitingBay : state : " + state);
		}
		status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

		if(StateExecutorController.simulateWaitingBayHappyPath){
			status = true;
		}
		if(logEnabled) {
			VerificWaiting.logger.debug("isPalletAvailableAt_WaitingBay : status : " + status); 
		}
		responseReturn.put("status", status);

		if(logEnabled) {
			VerificWaiting.logger.debug("isPalletAvailableAt_WaitingBay : Exit");
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

