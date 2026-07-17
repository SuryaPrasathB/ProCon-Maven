package com.tasnetwork.calibration.conveyor.bay.hv;

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
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S08_check_for_pallet_at_IRT_Bay implements HvtBayState {

	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_01;
	private String palletSensorPortCname =  ConstantBayPortNameMapping.IR_PORT_NAME_SNSR_PALLET;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_HVT_013;
	private boolean logEnabled = true;

	public String getMyBayKey() {
		return myBayKey;
	}

	@Override
	public BayResponse handleRequest() {
		Hv.logger.info("S08_check_for_pallet_at_IRT_Bay : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayMonitoringAllPalletsExistInTargetBayIndicator(getMyBayKey());
		Map<String,Object> responseReturn =  isPalletAvailableAt_IRTBay();	 
		boolean isPalletAvailableAt_IRTBay = (boolean)responseReturn.get("status");


		while (isPalletAvailableAt_IRTBay && 
				(!Hv.isStopProcessRequestedHvtBay()) &&
				(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {
			if(logEnabled) {
				Hv.logger.info("S08_check_for_pallet_at_IRT_Bay : Pallet Available at IRT Bay");
			}
			BayUtils.delay(1000);
			responseReturn =  isPalletAvailableAt_IRTBay();	 
			isPalletAvailableAt_IRTBay = (boolean)responseReturn.get("status");
			logEnabled = false;
		}

		if (!isPalletAvailableAt_IRTBay) {
			Hv.logger.info("S08_check_for_pallet_at_IRT_Bay : No Pallet Available at IRT Bay");
			ConveyorDataManager.getDashboardObject().getBayIndicatorManager().resetAllPalletsExistInTargetBayIndicator(getMyBayKey());
			if (ProconFeatureEnable.MOTOR_CONTROL_ENABLE) {
				BayUtils bayUtils = new BayUtils();
				responseReturn = bayUtils.set_motor_required(getMyBayKey());
				boolean set_motor_required = (boolean) responseReturn.get("status");
				if (set_motor_required) {
					Hv.logger.info("set_motor_required : Success");
					bayResponse.setStatus(true);
					bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
				} else {
					Hv.logger.info("Failed to set_motor_required ");
					bayResponse.setStatus(false);
					bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_026);
				} 
			}
			//			bayResponse.setStatus(true);
			//			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_602);
		} else {
			Hv.logger.info("S08_check_for_pallet_at_IRT_Bay : Pallet Available");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_HVT_013);
		}

		/*boolean isPalletAvailableAt_IRTBay;
        long startTime;
        boolean stableDetection = false;

        // Initial delay before checking
        BayUtils.delay(ConstantConveyor.STABLE_PALLET_TIME);

        while (!stableDetection && !ConstantConveyor.ALL_LOOP_BREAK_FLAG && !HighVoltageTestBay2.isStopProcessRequestedHvtBay()) {
            Map<String, Object> responseReturn = isPalletAvailableAt_IRTBay();
            isPalletAvailableAt_IRTBay = (boolean) responseReturn.get("status");

            if (isPalletAvailableAt_IRTBay) {
                startTime = System.currentTimeMillis();

                while (System.currentTimeMillis() - startTime < ConstantConveyor.STABLE_PALLET_TIME) {
                    BayUtils.delay(1000); // Small delay to prevent CPU overuse
                    responseReturn = isPalletAvailableAt_IRTBay();
                    isPalletAvailableAt_IRTBay = (boolean) responseReturn.get("status");

                    if (!isPalletAvailableAt_IRTBay) {
                        break; // Reset if detection is lost
                    }
                }

                // If detection lasted for stable pallet time, confirm stability
                if (isPalletAvailableAt_IRTBay) {
                    stableDetection = true;
                }
            } else {
                HighVoltageTestBay.logger.info("S08_check_for_pallet_at_IRT_Bay : No pallet Available at IRT Bay");
                BayUtils.delay(1000);
            }
        }

        if (stableDetection) {
            HighVoltageTestBay.logger.info("S08_check_for_pallet_at_IRT_Bay : Pallet Available");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_602);
        } else {
            HighVoltageTestBay.logger.info("S08_check_for_pallet_at_IRT_Bay : Pallet Not Available");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_HVT_013);
        }
		 */

		/*	CheckForPalletAtBay bayPalletService = new CheckForPalletAtBay(HighVoltageTestBay.logger, 
				getMyBayKey(),
				getBayStateSequenceId(), 
				getPalletSensorPortCname(), 
				getFailStateErrorCode(),
				StateExecutorController.simulateIrBayHappyPath);
		bayResponse = bayPalletService.checkForPalletAtBayProcess();
		 */

		Hv.logger.info("S08_check_for_pallet_at_IRT_Bay : Exit");
		return bayResponse;
	}
	//============================================================================
	private Map<String,Object> isPalletAvailableAt_IRTBay() {

		if(logEnabled) {
			Hv.logger.debug("S08_check_for_pallet_at_IRT_Bay : Entry");
		}
		boolean status = false;
		Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);

		IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.IR_PORT_NAME_SNSR_PALLET);

		if (portInfo != null) {
			/*            Hv.logger.debug("PortId    : " + portInfo.getPortId());
            Hv.logger.debug("ClusterId : " + portInfo.getClusterId());
            Hv.logger.debug("BayId     : " + portInfo.getBayId());*/

			if(logEnabled) {
				Hv.logger.debug("isPalletAvailableAt_IRTBay : getClusterId: " +portInfo.getClusterId() + " -> getBayId: " + portInfo.getBayId() + " -> getPortId: " + portInfo.getPortId() );
			}
		} else {
			if(logEnabled) {
				Hv.logger.debug("S08_check_for_pallet_at_IRT_Bay : Output port not found");
			}
			return responseReturn ;
		}

		BayUtils bayUtils = new BayUtils();

		/*String state = bayUtils.getInputDataFromBay(portInfo.getClusterId(),
                portInfo.getBayId(),
                portInfo.getPortId());*/

		String state = bayUtils.getInputDataFromBayV2(portInfo) ;
		if(logEnabled) {
			Hv.logger.debug("S08_check_for_pallet_at_IRT_Bay : state : " + state);
		}
		status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

		if(StateExecutorController.simulateHvBayHappyPath){
			status = false; 
		}
		if(logEnabled) {
			Hv.logger.debug("S08_check_for_pallet_at_IRT_Bay : status : " + status); 
		}
		responseReturn.put("status", status);

		if(logEnabled) {
			Hv.logger.debug("S08_check_for_pallet_at_IRT_Bay : Exit");
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
