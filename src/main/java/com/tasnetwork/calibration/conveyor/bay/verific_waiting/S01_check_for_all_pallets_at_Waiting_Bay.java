package com.tasnetwork.calibration.conveyor.bay.verific_waiting;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S01_check_for_all_pallets_at_Waiting_Bay implements WaitingBayState {

	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_01;
	private String palletSensorPortCname =  ConstantBayPortNameMapping.WAITING_PORT_NAME_SNSR_PALLET;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_WAITING_001;
	private boolean logEnabled = true;
	@Override
	public BayResponse handleRequest() {
		VerificWaiting.logger.info("S01_check_for_all_pallets_at_Waiting_Bay : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		//======================================================================
		ConveyorDataManager.getDashboardObject().removePalletFromBay(getMyBayKey());
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager()
				.updateBayMonitoringAllPalletsExistInBay(getMyBayKey());

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
			ConveyorDataManager.getDashboardObject().getBayIndicatorManager()
					.updateBayAllPalletsExistInBay(getMyBayKey(), true);
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			VerificWaiting.logger.info("S01_check_for_all_pallets_at_Waiting_Bay : Pallets Not Available at Waiting Bay");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_WAITING_001);
		}


		//======================================================================

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

