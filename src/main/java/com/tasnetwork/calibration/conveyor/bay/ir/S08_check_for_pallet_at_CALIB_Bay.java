package com.tasnetwork.calibration.conveyor.bay.ir;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.bay.bookshelf.CheckForPalletAtBay;
import com.tasnetwork.calibration.conveyor.bay.hv.Hv;
//import com.tasnetwork.calibration.conveyor.bay_verificationtest.VerificationTestBay2;
//import com.tasnetwork.calibration.conveyor.bay_waiting.WaitingBay;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S08_check_for_pallet_at_CALIB_Bay implements IrtBayState {


	private BayUtils bayUtils = new BayUtils();
	private String sequencePathId = "p1";
	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_01;
	private String palletSensorPortCname =  ConstantBayPortNameMapping.CALIB_PORT_NAME_SNSR_PALLET;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_IRT_013;
	private boolean logEnabled = true;
	public String getMyBayKey() {
		return myBayKey;
	}

	@Override
	public BayResponse handleRequest() {
		Ir.logger.info("S08_check_for_pallet_at_CALIB_Bay : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayMonitoringAllPalletsExistInTargetBayIndicator(getMyBayKey());
		Map<String,Object> responseReturn =  isPalletAvailableAt_CalibBay();	 
		boolean isPalletAvailableAt_CalibBay = (boolean)responseReturn.get("status");

		while (isPalletAvailableAt_CalibBay && 
				(!Ir.isStopProcessRequestedIrtBay()) &&
				(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {     	
			if(logEnabled) {
				Ir.logger.info("S08_check_for_pallet_at_CALIB_Bay : Pallet Available at CALIB Bay");
			}
			BayUtils.delay(1000);

			responseReturn =  isPalletAvailableAt_CalibBay();	 
			isPalletAvailableAt_CalibBay = (boolean)responseReturn.get("status");
			logEnabled = false;
		}

		if (!isPalletAvailableAt_CalibBay) {
			Ir.logger.info("S08_check_for_pallet_at_CALIB_Bay : No Pallet Available");
			ConveyorDataManager.getDashboardObject().getBayIndicatorManager().resetAllPalletsExistInTargetBayIndicator(getMyBayKey());
			// delay required to avoid over run to calib exit
			BayUtils.delay(5000);
			if (ProconFeatureEnable.MOTOR_CONTROL_ENABLE) {
				BayUtils bayUtils = new BayUtils();
				responseReturn = bayUtils.set_motor_required(getMyBayKey());
				boolean set_motor_required = (boolean) responseReturn.get("status");
				if (set_motor_required) {
					Ir.logger.info("set_motor_required : Success");
					bayResponse.setStatus(true);
					bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
				} else {
					Ir.logger.info("Failed to set_motor_required ");
					bayResponse.setStatus(false);
					bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_026);
				} 
			}
			//			bayResponse.setStatus(true);
			//			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			Ir.logger.info("S08_check_for_pallet_at_CALIB_Bay : Pallet Not Available");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_IRT_013);
		}

		/*boolean isPalletAvailableAt_CalibBay;
		long startTime;
		boolean stableDetection = false;

		// Initial delay before checking
		BayUtils.delay(ConstantConveyor.STABLE_PALLET_TIME);

		while (!stableDetection && !ConstantConveyor.ALL_LOOP_BREAK_FLAG && !InsulationResistanceTestBay.isStopProcessRequestedIrtBay()) {
		    Map<String, Object> responseReturn = isPalletAvailableAt_CalibBay();
		    isPalletAvailableAt_CalibBay = (boolean) responseReturn.get("status");

		    if (isPalletAvailableAt_CalibBay) {
		        startTime = System.currentTimeMillis();

		        while (System.currentTimeMillis() - startTime < ConstantConveyor.STABLE_PALLET_TIME) {
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
		        InsulationResistanceTestBay.logger.info("S08_check_for_pallet_at_CALIB_Bay : No Pallet Available");
		        BayUtils.delay(1000);
		    }
		}

		if (stableDetection) {
		    InsulationResistanceTestBay.logger.info("S08_check_for_pallet_at_CALIB_Bay : Pallet Available");
		    bayResponse.setStatus(true);
		    bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
		    InsulationResistanceTestBay.logger.info("S08_check_for_pallet_at_CALIB_Bay : Pallet Not Available");
		    bayResponse.setStatus(false);
		    bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_IRT_013);
		}*/


		/*	int consecutiveChecks = 0;
		int requiredChecks = 2;  // Number of times the pallet must be detected in a row

		while (consecutiveChecks < requiredChecks) {
			InsulationResistanceTestBay.logger.info("S08_check_for_pallet_at_CALIB_Bay : Checking for pallet presence...");
		    BayUtils.delay(1000);

		    responseReturn = isPalletAvailableAt_CalibBay();
		    boolean currentCheck = (boolean) responseReturn.get("status");

		    if (!currentCheck) {
		        consecutiveChecks++;  // Increase count if no pallet is detected
		        InsulationResistanceTestBay.logger.info("S08_check_for_pallet_at_CALIB_Bay : No pallet detected! Count: " + consecutiveChecks);
		    } else {
		        //consecutiveChecks = 0;  // Reset count if detection passes
		        InsulationResistanceTestBay.logger.info("S08_check_for_pallet_at_CALIB_Bay : Pallet detected. Resetting check count.");
		    }
		}

		InsulationResistanceTestBay.logger.info("S08_check_for_pallet_at_CALIB_Bay : No Pallet confirmed as present.");
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		 */
		/*CheckForPalletAtBay bayPalletService = new CheckForPalletAtBay(InsulationResistanceTestBay.logger, 
				getMyBayKey(),
				getBayStateSequenceId(), 
				getPalletSensorPortCname(), 
				getFailStateErrorCode(),
				StateExecutorController.simulateCalibBayHappyPath);
			bayResponse = bayPalletService.checkForPalletAtBayProcess();*/


		Ir.logger.info("S08_check_for_pallet_at_CALIB_Bay : Exit");
		return bayResponse;
	}
	//======================================================================
	private Map<String,Object> isPalletAvailableAt_CalibBay() {
		if(logEnabled) {
			Ir.logger.debug("S08_check_for_pallet_at_CALIB_Bay : isPalletAvailableAt_CalibBay : Entry");
		}
		boolean status = false;
		Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);

		IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.CALIB_PORT_NAME_SNSR_PALLET);

		if (portInfo != null) {
			/*	Ir.logger.debug("PortId    : " + portInfo.getPortId());
			Ir.logger.debug("ClusterId : " + portInfo.getClusterId());
			Ir.logger.debug("BayId     : " + portInfo.getBayId());*/

			if(logEnabled) {
				Ir.logger.debug("isPalletAvailableAt_CalibBay : getClusterId: " +portInfo.getClusterId() + " -> getBayId: " + portInfo.getBayId() + " -> getPortId: " + portInfo.getPortId() );
			}
		} else {
			if(logEnabled) {
				Ir.logger.debug("S08_check_for_pallet_at_CALIB_Bay : isPalletAvailableAt_CalibBay : Output port not found");
			}
			return responseReturn ;
		}

		BayUtils bayUtils = new BayUtils();

		/*String state = bayUtils.getInputDataFromBay(portInfo.getClusterId(),
				portInfo.getBayId(),
				portInfo.getPortId());*/

		String state = bayUtils.getInputDataFromBayV2(portInfo) ;
		if(logEnabled) {
			Ir.logger.debug("S08_check_for_pallet_at_CALIB_Bay : isPalletAvailableAt_CalibBay : state : " + state);
		}
		status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

		if(StateExecutorController.simulateIrBayHappyPath){
			status = false; 
		}
		if(logEnabled) {
			Ir.logger.debug("S08_check_for_pallet_at_CALIB_Bay : isPalletAvailableAt_CalibBay : status : " + status); 
		}
		responseReturn.put("status", status);

		if(logEnabled) {
			Ir.logger.debug("S08_check_for_pallet_at_CALIB_Bay : isPalletAvailableAt_CalibBay : Exit");
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
