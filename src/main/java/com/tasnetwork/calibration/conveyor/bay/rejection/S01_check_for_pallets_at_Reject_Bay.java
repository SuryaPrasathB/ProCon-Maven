package com.tasnetwork.calibration.conveyor.bay.rejection;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
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

public class S01_check_for_pallets_at_Reject_Bay implements RejectionBayState {

	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_01 ;
	private String palletSensorPortCname =  ConstantBayPortNameMapping.REJECT_PORT_NAME_SNSR_PALLET ;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_REJECTION_001 ;


	private boolean logEnabled = true;
	/**
	 * Checks for the presence of a pallet at the Rejection Bay.
	 *
	 * @return BayResponse indicating success or failure.
	 */
	@Override
	public BayResponse handleRequest() {
		Rejection.logger.info("S01_check_for_pallets_at_Reject_Bay : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_REJECTION_001);

		/*Map<String,Object> responseReturn =  isPalletAvailableAt_RejectBay();	 
		boolean isPalletAvailableAt_RejectBay = (boolean)responseReturn.get("status");


        while (!isPalletAvailableAt_RejectBay &&
        		(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {     
        	   RejectionBay.logger.info("S01_check_for_pallets_at_Reject_Bay : No pallet Available at Waiting Bay");
               BayUtils.delay(1000);

    		responseReturn =  isPalletAvailableAt_RejectBay();	 
    		  isPalletAvailableAt_RejectBay = (boolean)responseReturn.get("status");


        }

        if (isPalletAvailableAt_RejectBay) {
            RejectionBay.logger.info("S01_check_for_pallets_at_Reject_Bay : Pallet Available");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_602);
        } else {
            RejectionBay.logger.info("S01_check_for_pallets_at_Reject_Bay : Pallet Not Available");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_REJECTION_001);
        }*/

		boolean isPalletAvailableAt_RejectBay;
		long startTime;
		boolean stableDetection = false;
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayMonitoringAllPalletsExistInBay(getMyBayKey());

		while (!stableDetection 
				&& !Rejection.isStopProcessRequestedRejectionBay()
				&& !ConstantConveyor.ALL_LOOP_BREAK_FLAG) {
			Map<String, Object> responseReturn = isPalletAvailableAt_RejectBay();
			isPalletAvailableAt_RejectBay = (boolean) responseReturn.get("status");

			if (isPalletAvailableAt_RejectBay) {
				startTime = System.currentTimeMillis();

				while (System.currentTimeMillis() - startTime < ConstantConveyor.STABLE_PALLET_TIME_IN_MSEC) {
					BayUtils.delay(1000); // Small delay to prevent CPU overuse
					responseReturn = isPalletAvailableAt_RejectBay();
					isPalletAvailableAt_RejectBay = (boolean) responseReturn.get("status");

					if (!isPalletAvailableAt_RejectBay) {
						break; // Reset if detection is lost
					}
				}

				// If detection lasted for stable pallet time, confirm stability
				if (isPalletAvailableAt_RejectBay) {
					stableDetection = true;
				}
			} else {
				if(logEnabled) {
					Rejection.logger.info("S01_check_for_pallets_at_Reject_Bay : No Pallet Available");
				}
				BayUtils.delay(1000);
			}
			//logEnabled = false;
		}

		if (stableDetection) {
			Rejection.logger.info("S01_check_for_pallets_at_Reject_Bay : Pallet Available");
			ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayAllPalletsExistInBay(getMyBayKey(),true);
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_602);
		} else {
			Rejection.logger.info("S01_check_for_pallets_at_Reject_Bay : Pallet Not Available");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_REJECTION_001);
		}


		/*CheckForPalletAtBay bayPalletService = new CheckForPalletAtBay(HighVoltageTestBay.logger, 
				getMyBayKey(),
				getBayStateSequenceId(),//ConstantBayStateManage.BAY_HP_SEQ_01, 
				getPalletSensorPortCname(), 
				getFailStateErrorCode(),
				simulateIrtBayHappyPath);
		bayResponse = bayPalletService.checkForPalletAtBayProcess(); 

        RejectionBay.logger.info("S01_check_for_pallets_at_Reject_Bay : Exit");*/
		return bayResponse;
	}

	private Map<String,Object> isPalletAvailableAt_RejectBay() {
		if(logEnabled) {
			Rejection.logger.debug("S01_check_for_pallets_at_Reject_Bay : Entry");
		}
		boolean status = false;
		Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
		IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.REJECT_PORT_NAME_SNSR_PALLET);

		if (portInfo != null) {
			if(logEnabled) {
				/* Rejection.logger.debug("PortId    : " + portInfo.getPortId());
            Rejection.logger.debug("ClusterId : " + portInfo.getClusterId());
            Rejection.logger.debug("BayId     : " + portInfo.getBayId());*/
				Rejection.logger.debug("isPalletAvailableAt_RejectBay : getClusterId: " +portInfo.getClusterId() + " -> getBayId: " + portInfo.getBayId() + " -> getPortId: " + portInfo.getPortId() );

			}
		} else {
			if(logEnabled) {
				Rejection.logger.debug("S01_check_for_pallets_at_Waiting_Bay : Output port not found");
			}
			return responseReturn ;
		}

		BayUtils bayUtils = new BayUtils();

		/*String state = bayUtils.getInputDataFromBay(portInfo.getClusterId(),
                portInfo.getBayId(),
                portInfo.getPortId());*/

		String state = bayUtils.getInputDataFromBayV2(portInfo) ;
		if(logEnabled) {
			Rejection.logger.debug("S01_check_for_pallets_at_Reject_Bay : state : " + state);
		}
		status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

		if(StateExecutorController.simulateRejectionBayHappyPath){
			status = true; 
		}
		if(logEnabled) {
			Rejection.logger.debug("S01_check_for_pallets_at_Reject_Bay : status : " + status); 
		}
		responseReturn.put("status", status);

		if(logEnabled) {
			Rejection.logger.debug("S01_check_for_pallets_at_Reject_Bay : Exit");
		}
		return responseReturn;
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

	public String getBayStateSequenceId() {
		return bayStateSequenceId;
	}

	public void setBayStateSequenceId(String bayStateSequenceId) {
		this.bayStateSequenceId = bayStateSequenceId;
	}
}
