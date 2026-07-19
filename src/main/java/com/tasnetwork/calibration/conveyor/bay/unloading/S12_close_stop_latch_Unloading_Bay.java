package com.tasnetwork.calibration.conveyor.bay.unloading;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S12_close_stop_latch_Unloading_Bay implements UnloadingBayState {

    @Override
    public BayResponse handleRequest() {
    	Unloading.logger.info("S12_close_stop_latch_Unloading_Bay : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        BayUtils.delay(2000);

        Map<String, Object> responseReturn = close_StopLatch_Unloading_Bay();

        boolean closeStopLatch_Unloading_Bay = (boolean) responseReturn.get("status");

        if (closeStopLatch_Unloading_Bay) {
            Unloading.logger.info("S12_close_stop_latch_Unloading_Bay : Stop Latch Closed");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        } else {
            Unloading.logger.info("S12_close_stop_latch_Unloading_Bay : Failed to Close Stop Latch");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_UNLOADING_007);
        }

        Unloading.logger.info("S12_close_stop_latch_Unloading_Bay : Exit");
        return bayResponse;
    }

    private Map<String, Object> close_StopLatch_Unloading_Bay() {
        Unloading.logger.debug("S12_close_stop_latch_Unloading_Bay : unloadingBay_StopLatch_Status : Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<>();
        responseReturn.put("status", false);

        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.UNLOADING_PORT_NAME_STPR);

        String state = "";
        if (portInfo != null) {
            Unloading.logger.debug("PortId    : " + portInfo.getPortId());
            Unloading.logger.debug("ClusterId : " + portInfo.getClusterId());
            Unloading.logger.debug("BayId     : " + portInfo.getBayId());

            String outputActive = Constant_IO_ActionMapping.ON;
            BayUtils bayUtils = new BayUtils();
            
            state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                                                     portInfo.getBayId(),
                                                     portInfo.getPortId(),
                                                     outputActive);

            Unloading.logger.debug("S12_close_stop_latch_Unloading_Bay : close_StopLatch_Unloading_Bay : state : " + state);
           /* if (simulateUnloadingBayHappyPath) {
                state = Constant_IO_ActionMapping.OFF;
            }*/

            status = state.equals(Constant_IO_ActionMapping.ON) ? true : false; 

    		if(StateExecutorController.simulateUnloadingBayHappyPath){
    			status = true; 
    		}


            Unloading.logger.debug("S12_close_stop_latch_Unloading_Bay : close_StopLatch_Unloading_Bay : status : " + status);

        } else {
            Unloading.logger.debug("S12_close_stop_latch_Unloading_Bay : Output port not found");
            return responseReturn ;
        }

        responseReturn.put("status", status);
        responseReturn.put("responseData", state);

        Unloading.logger.debug("S12_close_stop_latch_Unloading_Bay : unloadingBay_StopLatch_Status : status : " + status);
        Unloading.logger.debug("S12_close_stop_latch_Unloading_Bay : unloadingBay_StopLatch_Status : Exit");
        return responseReturn;
    }
}

