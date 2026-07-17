package com.tasnetwork.calibration.conveyor.bay.verific_waiting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.Constant_Motor_Requirement;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.bay.bookshelf.CheckForPalletAtBay;
import com.tasnetwork.calibration.conveyor.bay.verific.S19_open_stop_latch_Verific_Bay;
//import com.tasnetwork.calibration.conveyor.bay_functionaltest.FunctionalTestBay;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S02_check_for_pallets_at_Verific_Bay implements WaitingBayState {


	private BayUtils bayUtils = new BayUtils();
	private String sequencePathId = "p1";
	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_01;
	private String palletSensorPortCname =  ConstantBayPortNameMapping.VERIFIC_PORT_NAME_SNSR_PALLET ;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_WAITING_007 ;
	private boolean logEnabled = true;
	public String getMyBayKey() {
		return myBayKey;
	}

	@Override
	public BayResponse handleRequest() {
		VerificWaiting.logger.info("S02_check_for_pallets_at_Verific_Bay : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		Map<String,Object> responseReturn =  isPalletAvailableAt_VerificBay();	 
		boolean isPalletAvailableAt_VerificBay = (boolean)responseReturn.get("status");
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayMonitoringAllPalletsExistInTargetBayIndicator(getMyBayKey());
		while ( (isPalletAvailableAt_VerificBay || 
				//!ConstantConveyor.isVerificationBayPalletsCleared() ) &&
				!ConveyorDataManager.isVerific1PalletsAllCleared() ) &&
				(!ConstantConveyor.ALL_LOOP_BREAK_FLAG) ) {
			//VerificWaiting.logger.debug("S02_check_for_pallets_at_Verific_Bay : VERIFICATION_BAY_PALLETS_CLEARED : " + ConstantConveyor.isVerificationBayPalletsCleared());

			//if (ConstantConveyor.isVerificationBayPalletsCleared()) {
			//	break;
			//}

			if(logEnabled) {
				VerificWaiting.logger.info("S02_check_for_pallets_at_Verific_Bay : Pallet Available at Verific Bay");
			}
			BayUtils.delay(1000);

			responseReturn =  isPalletAvailableAt_VerificBay();	 
			isPalletAvailableAt_VerificBay = (boolean)responseReturn.get("status");
			if(logEnabled) {
				VerificWaiting.logger.debug("S02_check_for_pallets_at_Verific_Bay : ALL_LOOP_BREAK_FLAG-1: " + ConstantConveyor.ALL_LOOP_BREAK_FLAG);
				VerificWaiting.logger.debug("S02_check_for_pallets_at_Verific_Bay : isVerific1PalletsAllCleared()-1: " + ConveyorDataManager.isVerific1PalletsAllCleared());
				VerificWaiting.logger.debug("S02_check_for_pallets_at_Verific_Bay : isPalletAvailableAt_VerificBay-1: " + isPalletAvailableAt_VerificBay);
			}
			logEnabled =false;
		}

		VerificWaiting.logger.debug("S02_check_for_pallets_at_Verific_Bay : ALL_LOOP_BREAK_FLAG-2: " + ConstantConveyor.ALL_LOOP_BREAK_FLAG);
		VerificWaiting.logger.debug("S02_check_for_pallets_at_Verific_Bay : isPalletAvailableAt_VerificBay-2: " + isPalletAvailableAt_VerificBay);
		VerificWaiting.logger.debug("S02_check_for_pallets_at_Verific_Bay : isVerific1PalletsAllCleared()-2: " + ConveyorDataManager.isVerific1PalletsAllCleared());
		//if (!isPalletAvailableAt_VerificBay && ConstantConveyor.isVerificationBayPalletsCleared()) {
		if (!isPalletAvailableAt_VerificBay && ConveyorDataManager.isVerific1PalletsAllCleared()) {	
			VerificWaiting.logger.debug("S02_check_for_pallets_at_Verific_Bay : No Pallet Available at Verific Bay and existing cleared");
			ConveyorDataManager.getDashboardObject().getBayIndicatorManager().resetAllPalletsExistInTargetBayIndicator(getMyBayKey());

			/*if (ProconFeatureEnable.MOTOR_CONTROL_ENABLE) {

        		List<String> motor_requirement = Constant_Motor_Requirement.WAITING_MOTOR_REQUIRED;

        		responseReturn = bayUtils.set_motor_required(getMyBayKey(), motor_requirement);

        		boolean set_motor_required = (boolean) responseReturn.get("status");
        		if (set_motor_required) {
        			WaitingBay.logger.info("S02_check_for_pallets_at_Verific_Bay : set_motor_required : Success");
        			bayResponse.setStatus(true);
        			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        		} else {
        			WaitingBay.logger.info("S02_check_for_pallets_at_Verific_Bay : Failed to set_motor_required ");
        			bayResponse.setStatus(false);
        			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_026);
        		} 
        	}*/

			//            bayResponse.setStatus(true);
			//            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		}else  if (isPalletAvailableAt_VerificBay) {
			VerificWaiting.logger.debug("S02_check_for_pallets_at_Verific_Bay : Pallet Available");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_WAITING_007);
		}else  if (!ConveyorDataManager.isVerific1PalletsAllCleared()) {
			VerificWaiting.logger.debug("S02_check_for_pallets_at_Verific_Bay : Pallet not cleared");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_WAITING_007);
		}else {
			VerificWaiting.logger.debug("S02_check_for_pallets_at_Verific_Bay : default case");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_WAITING_007);
		}

		/*CheckForPalletAtBay bayPalletService = new CheckForPalletAtBay(FunctionalTestBay2.logger, 
				getMyBayKey(),
				getBayStateSequenceId(), 
				getPalletSensorPortCname(), 
				getFailStateErrorCode(),
				StateExecutorController.simulateFtBayHappyPath);
			bayResponse = bayPalletService.checkForPalletAtBayProcess(); */


		VerificWaiting.logger.info("S02_check_for_pallets_at_Verific_Bay : Exit");
		return bayResponse;
	}
	//============================================================================================		 

	private Map<String,Object> isPalletAvailableAt_VerificBay() {
		if(logEnabled) {
			VerificWaiting.logger.debug("isPalletAvailableAt_VerificBay : Entry");
		}
		boolean status = false;
		Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);

		//String state = "";
		//============================================================================================		 
		IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.VERIFIC_PORT_NAME_SNSR_PALLET);

		if (portInfo != null) {
			/*VerificWaiting.logger.debug("PortId    : " + portInfo.getPortId());
            VerificWaiting.logger.debug("ClusterId : " + portInfo.getClusterId());
            VerificWaiting.logger.debug("BayId     : " + portInfo.getBayId());*/
			if(logEnabled) {
				VerificWaiting.logger.debug("isPalletAvailableAt_VerificBay : getClusterId: " +portInfo.getClusterId() + " -> getBayId: " + portInfo.getBayId() + " -> getPortId: " + portInfo.getPortId() );
			}
		} else {
			if(logEnabled) {
				VerificWaiting.logger.debug("isPalletAvailableAt_VerificBay : Output port not found");
			}
			return responseReturn ;
		}

		BayUtils bayUtils = new BayUtils();

		/*String state = bayUtils.getInputDataFromBay(portInfo.getClusterId(),
                portInfo.getBayId(),
                portInfo.getPortId());*/

		String state = bayUtils.getInputDataFromBayV2(portInfo) ;
		if(logEnabled) {
			VerificWaiting.logger.debug("isPalletAvailableAt_VerificBay : state : " + state);
		}
		status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

		if(StateExecutorController.simulateWaitingBayHappyPath){
			status = true;
		}
		if(logEnabled) {
			VerificWaiting.logger.debug("isPalletAvailableAt_VerificBay : status : " + status); 
		}
		responseReturn.put("status", status);

		if(logEnabled) {
			VerificWaiting.logger.debug("isPalletAvailableAt_VerificBay : Exit");
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
