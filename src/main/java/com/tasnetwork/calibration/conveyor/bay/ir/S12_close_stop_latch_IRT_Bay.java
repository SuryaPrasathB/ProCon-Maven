package com.tasnetwork.calibration.conveyor.bay.ir;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S12_close_stop_latch_IRT_Bay implements IrtBayState {

	public String getMyBayKey() {
		return myBayKey;
	}
	
    @Override
    public BayResponse handleRequest() {
        Ir.logger.info("S12_close_stop_latch_IRT_Bay : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        BayUtils.delay(2000);

        Map<String, Object> responseReturn = close_StopLatch_IRT_Bay();

        boolean closeStopLatch_IRT_Bay = (boolean) responseReturn.get("status");

        if (closeStopLatch_IRT_Bay) {
        	Ir.logger.info("S12_close_stop_latch_IRT_Bay : Stop Latch Closed");
        	 ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayExitStopper(getMyBayKey(),false);
        	BayUtils bayUtils = new BayUtils();			
        	responseReturn = bayUtils.set_motor_not_required(getMyBayKey());

        	boolean set_motor_not_required = (boolean)responseReturn.get("status");  

        	if (set_motor_not_required) {
        		Ir.logger.info("set_motor_not_required : Success");
        		bayResponse.setStatus(true);
        		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        	} 
        	else {
        		Ir.logger.info("Failed to set_motor_not_required ");
        		bayResponse.setStatus(false);
        		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_026 );
        	}            

        	bayResponse.setStatus(true);
        	bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        } else {
        	Ir.logger.info("S12_close_stop_latch_IRT_Bay : Failed to Close Stop Latch");
        	bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_IRT_009);
        }

        Ir.logger.info("S12_close_stop_latch_IRT_Bay : Exit");
        return bayResponse;
    }

    private Map<String, Object> close_StopLatch_IRT_Bay() {
        Ir.logger.debug("S12_close_stop_latch_IRT_Bay : irtBay_StopLatch_Status : Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<>();
        responseReturn.put("status", false);

        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.IR_PORT_NAME_STPR);

        String state = "";
        if (portInfo != null) {
            Ir.logger.debug("PortId    : " + portInfo.getPortId());
            Ir.logger.debug("ClusterId : " + portInfo.getClusterId());
            Ir.logger.debug("BayId     : " + portInfo.getBayId());

            String outputActive = Constant_IO_ActionMapping.ON;
            BayUtils bayUtils = new BayUtils();
            
            state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                                                     portInfo.getBayId(),
                                                     portInfo.getPortId(),
                                                     outputActive);

            Ir.logger.debug("S12_close_stop_latch_IRT_Bay : close_StopLatch_IRT_Bay : state : " + state);
            /*if (simulateIrtBayHappyPath) {
                state = Constant_IO_ActionMapping.OFF;
            }*/

            status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;
            
            if(StateExecutorController.simulateIrBayHappyPath){
    			status = true; 
    		}
            
            Ir.logger.debug("S12_close_stop_latch_IRT_Bay : close_StopLatch_IRT_Bay : status : " + status);

        } else {
            Ir.logger.debug("S12_close_stop_latch_IRT_Bay : Output port not found");
            return responseReturn ;
        }

        responseReturn.put("status", status);
        responseReturn.put("responseData", state);

        Ir.logger.debug("S12_close_stop_latch_IRT_Bay : irtBay_StopLatch_Status : status : " + status);
        Ir.logger.debug("S12_close_stop_latch_IRT_Bay : irtBay_StopLatch_Status : Exit");
        return responseReturn;
    }
}

