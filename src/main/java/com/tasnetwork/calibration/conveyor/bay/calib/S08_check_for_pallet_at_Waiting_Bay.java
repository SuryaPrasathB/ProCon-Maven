package com.tasnetwork.calibration.conveyor.bay.calib;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.bay.bookshelf.CheckForPalletAtBay;
import com.tasnetwork.calibration.conveyor.bay.verific_waiting.VerificWaiting;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S08_check_for_pallet_at_Waiting_Bay implements CalibrationBayState {

	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_01;
	private String palletSensorPortCname =  ConstantBayPortNameMapping.WAITING_PORT_NAME_SNSR_PALLET ;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_CALIB_014 ;
	private boolean logEnabled = true;
	public String getMyBayKey() {
		return myBayKey;
	}

	@Override
	public BayResponse handleRequest() {
		Calib.logger.info("S08_check_for_pallet_at_Waiting_Bay : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayMonitoringAllPalletsExistInTargetBayIndicator(getMyBayKey());
		Map<String,Object> responseReturn =  isPalletAvailableAt_WaitingBay();	 
		boolean isPalletAvailableAt_WaitingBay = (boolean)responseReturn.get("status");


		//while (isPalletAvailableAt_WaitingBay && 
		while ( (isPalletAvailableAt_WaitingBay || !ConveyorDataManager.isWaitingVerific1BayPalletsAllCleared() ) &&
				(!Calib.isStopProcessRequestedCalibBay()) &&
				(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {
			if(logEnabled) {
				Calib.logger.info("S08_check_for_pallet_at_Waiting_Bay : Pallet Available at Waiting Bay");
			}
			BayUtils.delay(1000);

			responseReturn =  isPalletAvailableAt_WaitingBay();	 
			isPalletAvailableAt_WaitingBay = (boolean)responseReturn.get("status");
			if(logEnabled) {
				Calib.logger.debug("S08_check_for_pallet_at_Waiting_Bay : ALL_LOOP_BREAK_FLAG-1: " + ConstantConveyor.ALL_LOOP_BREAK_FLAG);
				Calib.logger.debug("S08_check_for_pallet_at_Waiting_Bay : isVerific1WaitingBayPalletsAllCleared-1: " + ConveyorDataManager.isWaitingVerific1BayPalletsAllCleared());
				Calib.logger.debug("S08_check_for_pallet_at_Waiting_Bay : isPalletAvailableAt_WaitingBay-1: " + isPalletAvailableAt_WaitingBay);
			}
			logEnabled= false;
		}

		Calib.logger.debug("S08_check_for_pallet_at_Waiting_Bay : ALL_LOOP_BREAK_FLAG-2: " + ConstantConveyor.ALL_LOOP_BREAK_FLAG);
		Calib.logger.debug("S08_check_for_pallet_at_Waiting_Bay : isVerific1WaitingBayPalletsAllCleared-2: " + ConveyorDataManager.isWaitingVerific1BayPalletsAllCleared());
		Calib.logger.debug("S08_check_for_pallet_at_Waiting_Bay : isPalletAvailableAt_WaitingBay-2: " + isPalletAvailableAt_WaitingBay);
		//if (!isPalletAvailableAt_WaitingBay) {
		if (!isPalletAvailableAt_WaitingBay && ConveyorDataManager.isWaitingVerific1BayPalletsAllCleared()) {
			Calib.logger.debug("S08_check_for_pallet_at_Waiting_Bay : No Pallet Available at Waiting Bay and existing cleared");
			// Calib.logger.info("S08_check_for_pallet_at_Waiting_Bay : No Pallet Available");
			ConveyorDataManager.getDashboardObject().getBayIndicatorManager().resetAllPalletsExistInTargetBayIndicator(getMyBayKey());
			if (ProconFeatureEnable.MOTOR_CONTROL_ENABLE) {
				BayUtils bayUtils = new BayUtils();
				responseReturn = bayUtils.set_motor_required(getMyBayKey());
				boolean set_motor_required = (boolean) responseReturn.get("status");
				if (set_motor_required) {
					Calib.logger.info("set_motor_required : Success");
					bayResponse.setStatus(true);
					bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
				} else {
					Calib.logger.info("Failed to set_motor_required ");
					bayResponse.setStatus(false);
					bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_026);
				} 
			}
			//			bayResponse.setStatus(true);
			//            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			Calib.logger.info("S08_check_for_pallet_at_Waiting_Bay : Pallet Available");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_014);
		}

		/*boolean isPalletAvailableAt_WaitingBay;
        long startTime;
        boolean stableDetection = false;

        // Initial delay before checking
        BayUtils.delay(ConstantConveyor.STABLE_PALLET_TIME);

        while (!stableDetection && !ConstantConveyor.ALL_LOOP_BREAK_FLAG && !CalibrationBay2.isStopProcessRequestedCalibBay()) {
            Map<String, Object> responseReturn = isPalletAvailableAt_WaitingBay();
            isPalletAvailableAt_WaitingBay = (boolean) responseReturn.get("status");

            if (isPalletAvailableAt_WaitingBay) {
                startTime = System.currentTimeMillis();

                while (System.currentTimeMillis() - startTime < ConstantConveyor.STABLE_PALLET_TIME) {
                    BayUtils.delay(1000); // Small delay to prevent CPU overuse
                    responseReturn = isPalletAvailableAt_WaitingBay();
                    isPalletAvailableAt_WaitingBay = (boolean) responseReturn.get("status");

                    if (!isPalletAvailableAt_WaitingBay) {
                        break; // Reset if detection is lost
                    }
                }

                // If detection lasted for stable pallet time, confirm stability
                if (isPalletAvailableAt_WaitingBay) {
                    stableDetection = true;
                }
            } else {
                CalibrationBay.logger.info("S08_check_for_pallet_at_Waiting_Bay : No Pallet Available");
                BayUtils.delay(1000);
            }
        }

        if (stableDetection) {
            CalibrationBay.logger.info("S08_check_for_pallet_at_Waiting_Bay : Pallet Available");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        } else {
            CalibrationBay.logger.info("S08_check_for_pallet_at_Waiting_Bay : Pallet Not Available");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_014);
        }*/


		/*CheckForPalletAtBay bayPalletService = new CheckForPalletAtBay(CalibrationBay.logger, 
				getMyBayKey(),
				getBayStateSequenceId(), 
				getPalletSensorPortCname(), 
				getFailStateErrorCode(),
				StateExecutorController.simulateCalibBayHappyPath);
		bayResponse = bayPalletService.checkForPalletAtBayProcess(); */

		Calib.logger.info("S08_check_for_pallet_at_Waiting_Bay : Exit");
		return bayResponse;
	}

	private Map<String,Object> isPalletAvailableAt_WaitingBay() {

		if(logEnabled) {
			Calib.logger.debug("S08_check_for_pallet_at_Waiting_Bay : isPalletAvailableAt_WaitingBay : Entry");
		}
		boolean status = false;
		Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);

		IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.WAITING_PORT_NAME_SNSR_PALLET);

		if (portInfo != null) {
			/*Calib.logger.debug("PortId    : " + portInfo.getPortId());
            Calib.logger.debug("ClusterId : " + portInfo.getClusterId());
            Calib.logger.debug("BayId     : " + portInfo.getBayId());*/
			if(logEnabled) {
				Calib.logger.debug("isPalletAvailableAt_WaitingBay : getClusterId: " +portInfo.getClusterId() + " -> getBayId: " + portInfo.getBayId() + " -> getPortId: " + portInfo.getPortId() );
			}
		} else {
			if(logEnabled) {
				Calib.logger.debug("S08_check_for_pallet_at_Waiting_Bay : isPalletAvailableAt_WaitingBay : Output port not found");
			}
			return responseReturn ;
		}

		BayUtils bayUtils = new BayUtils();

		String state = bayUtils.getInputDataFromBayV2(portInfo);
		if(logEnabled) {
			Calib.logger.debug("S08_check_for_pallet_at_Waiting_Bay : isPalletAvailableAt_WaitingBay : state : " + state);
		}
		status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

		/*if(StateExecutorController.simulateCalibBayHappyPath){
        	status = true; 
        }*/
		if(logEnabled) {
			Calib.logger.debug("S08_check_for_pallet_at_Waiting_Bay : isPalletAvailableAt_WaitingBay : status : " + status);
		}
		responseReturn.put("status", status);

		if(logEnabled) {
			Calib.logger.debug("S08_check_for_pallet_at_Waiting_Bay : isPalletAvailableAt_WaitingBay : Exit");
		}
		return responseReturn;
	}


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
