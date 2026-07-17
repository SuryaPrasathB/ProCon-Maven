package com.tasnetwork.calibration.conveyor.bay.verific_waiting;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S08_close_stop_latch_Waiting_Bay implements WaitingBayState {

	public String getMyBayKey() {
		return myBayKey;
	}
	
    @Override
    public BayResponse handleRequest() {
        VerificWaiting.logger.info("S08_close_stop_latch_Waiting_Bay : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        BayUtils.delay(2000);

        Map<String, Object> responseReturn = close_StopLatch_Waiting_Bay();

        boolean closeStopLatch_Waiting_Bay = (boolean) responseReturn.get("status");

        if (closeStopLatch_Waiting_Bay) {
            VerificWaiting.logger.info("S08_close_stop_latch_Waiting_Bay : Stop Latch Closed");
            ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayExitStopper(getMyBayKey(),false);
            
            if (ProconFeatureEnable.MOTOR_CONTROL_ENABLE) {
				BayUtils bayUtils = new BayUtils();
				responseReturn = bayUtils.set_motor_not_required(getMyBayKey());
				boolean set_motor_not_required = (boolean) responseReturn.get("status");
				if (set_motor_not_required) {
					VerificWaiting.logger
					.info("S04_ensure_all_pallets_reached_Verific_Bay : set_motor_not_required : Success");
					bayResponse.setStatus(true);
					bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
					ConveyorDataManager.setWaitingVerific1BayPalletsAllCleared(true);
					ConveyorDataManager.getDashboardObject().removeAllPalletsFromWaitingVerific1Bays();
				} else {
					VerificWaiting.logger
					.info("S04_ensure_all_pallets_reached_Verific_Bay : Failed to set_motor_not_required ");
					bayResponse.setStatus(false);
					bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_026);
				} 
			} else {
				bayResponse.setStatus(true);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
			}
 
        } else {
            VerificWaiting.logger.info("S08_close_stop_latch_Waiting_Bay : Failed to Close Stop Latch");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_WAITING_006);
        }

        VerificWaiting.logger.info("S08_close_stop_latch_Waiting_Bay : Exit");
        return bayResponse;
    }

    private Map<String, Object> close_StopLatch_Waiting_Bay() {
        VerificWaiting.logger.debug("S08_close_stop_latch_Waiting_Bay : waitingBay_StopLatch_Status : Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<>();
        responseReturn.put("status", false);

        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.WAITING_PORT_NAME_STPR);

        String state = "";
        if (portInfo != null) {
            VerificWaiting.logger.debug("PortId    : " + portInfo.getPortId());
            VerificWaiting.logger.debug("ClusterId : " + portInfo.getClusterId());
            VerificWaiting.logger.debug("BayId     : " + portInfo.getBayId());

            String outputActive = Constant_IO_ActionMapping.ON;
            BayUtils bayUtils = new BayUtils();
            
             state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                                                     portInfo.getBayId(),
                                                     portInfo.getPortId(),
                                                     outputActive);

            VerificWaiting.logger.debug("S08_close_stop_latch_Waiting_Bay : close_StopLatch_Waiting_Bay : state : " + state);
            /*if (simulateWaitingBayHappyPath) {
                state = Constant_IO_ActionMapping.OFF;
            }*/

            status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;
            
    		if(StateExecutorController.simulateWaitingBayHappyPath){
    			status = true;
    		}
            VerificWaiting.logger.debug("S08_close_stop_latch_Waiting_Bay : close_StopLatch_Waiting_Bay : status : " + status);

        } else {
            VerificWaiting.logger.debug("S08_close_stop_latch_Waiting_Bay : Output port not found");
            return responseReturn ;
        }

        responseReturn.put("status", status);
        responseReturn.put("responseData", state);

        VerificWaiting.logger.debug("S08_close_stop_latch_Waiting_Bay : waitingBay_StopLatch_Status : status : " + status);
        VerificWaiting.logger.debug("S08_close_stop_latch_Waiting_Bay : waitingBay_StopLatch_Status : Exit");
        return responseReturn;
    }
}

