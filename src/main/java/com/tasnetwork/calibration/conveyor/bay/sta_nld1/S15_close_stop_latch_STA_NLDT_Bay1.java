package com.tasnetwork.calibration.conveyor.bay.sta_nld1;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S15_close_stop_latch_STA_NLDT_Bay1 implements STA_NoLoadTestBay1State {

    @Override
    public BayResponse handleRequest() {
        StaNld_Bay1.logger.info("S15_close_stop_latch_SCT_NLT_Bay1 : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        BayUtils.delay(3000);

        Map<String, Object> responseReturn = close_StopLatch_SCT_NLT_Bay1();

        boolean closeStopLatch_SCT_NLT_Bay1 = (boolean) responseReturn.get("status");

        if (closeStopLatch_SCT_NLT_Bay1) {
            StaNld_Bay1.logger.info("S15_close_stop_latch_SCT_NLT_Bay1 : Stop Latch Closed");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        } else {
            StaNld_Bay1.logger.info("S15_close_stop_latch_SCT_NLT_Bay1 : Failed to Close Stop Latch");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_007);
        }

        StaNld_Bay1.logger.info("S15_close_stop_latch_SCT_NLT_Bay1 : Exit");
        return bayResponse;
    }

    private Map<String, Object> close_StopLatch_SCT_NLT_Bay1() {
        StaNld_Bay1.logger.debug("S15_close_stop_latch_SCT_NLT_Bay1 : sctNltBay1_StopLatch_Status : Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<>();
        responseReturn.put("status", false);

        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.SCT_NLT1_PORT_NAME_STPR);

        String state = "";
        if (portInfo != null) {
            StaNld_Bay1.logger.debug("PortId    : " + portInfo.getPortId());
            StaNld_Bay1.logger.debug("ClusterId : " + portInfo.getClusterId());
            StaNld_Bay1.logger.debug("BayId     : " + portInfo.getBayId());

            String outputInactive = Constant_IO_ActionMapping.ON;
            
            BayUtils bayUtils = new BayUtils();
            
            state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                                                     portInfo.getBayId(),
                                                     portInfo.getPortId(),
                                                     outputInactive);

            StaNld_Bay1.logger.debug("S15_close_stop_latch_SCT_NLT_Bay1 : close_StopLatch_SCT_NLT_Bay1 : state : " + state);
            /*if (simulateSCTNLTBay1HappyPath) {
                state = Constant_IO_ActionMapping.OFF;
            }*/

            status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

            if(StateExecutorController.simulateSCTNLTBay1HappyPath){
            	status = true; 
            }
             
            StaNld_Bay1.logger.debug("S15_close_stop_latch_SCT_NLT_Bay1 : close_StopLatch_SCT_NLT_Bay1 : status : " + status);

        } else {
            StaNld_Bay1.logger.debug("S15_close_stop_latch_SCT_NLT_Bay1 : Output port not found");
        }

        responseReturn.put("status", status);
        responseReturn.put("responseData", state);

        StaNld_Bay1.logger.debug("S15_close_stop_latch_SCT_NLT_Bay1 : sctNltBay1_StopLatch_Status : status : " + status);
        StaNld_Bay1.logger.debug("S15_close_stop_latch_SCT_NLT_Bay1 : sctNltBay1_StopLatch_Status : Exit");
        return responseReturn;
    }
}

