package com.tasnetwork.calibration.conveyor.bay.comm;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S15_close_stop_latch2_COMM_Bay implements CommTestBayState {

    @Override
    public BayResponse handleRequest() {
        Comm.logger.info("S15_close_stop_latch2_COMM_Bay : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        BayUtils.delay(2000);

        Map<String, Object> responseReturn = close_StopLatch_COMM_Bay();

        boolean closeStopLatch_COMM_Bay = (boolean) responseReturn.get("status");

        if (closeStopLatch_COMM_Bay) {
            Comm.logger.info("S15_close_stop_latch2_COMM_Bay : Stop Latch Closed");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
            // BayUtils.delay(1000);
        } else {
            Comm.logger.info("S15_close_stop_latch2_COMM_Bay : Failed to Close Stop Latch");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_COMM_009);
        }

        Comm.logger.info("S15_close_stop_latch2_COMM_Bay : Exit");
        return bayResponse;
    }

    private Map<String, Object> close_StopLatch_COMM_Bay() {
        Comm.logger.debug("S15_close_stop_latch2_COMM_Bay : commBay_StopLatch_Status : Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<>();
        responseReturn.put("status", false);

        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.COMM_PORT_NAME_STPR2);

        String state = "";

        if (portInfo != null) {
            Comm.logger.debug("PortId    : " + portInfo.getPortId());
            Comm.logger.debug("ClusterId : " + portInfo.getClusterId());
            Comm.logger.debug("BayId     : " + portInfo.getBayId());

            String outputActive = Constant_IO_ActionMapping.ON;
            BayUtils bayUtils = new BayUtils();

            state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                    portInfo.getBayId(),
                    portInfo.getPortId(),
                    outputActive);

            Comm.logger.debug("S15_close_stop_latch2_COMM_Bay : close_StopLatch_COMM_Bay : state : " + state);
            /*
             * if (simulateCommBayHappyPath) {
             * state = Constant_IO_ActionMapping.OFF;
             * }
             */

            status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

            if (StateExecutorController.simulateCommBayHappyPath) {
                status = true;
            }

            Comm.logger.debug("S15_close_stop_latch2_COMM_Bay : close_StopLatch_COMM_Bay : status : " + status);

        } else {
            Comm.logger.debug("S15_close_stop_latch2_COMM_Bay : Output port not found");
            return responseReturn;
        }

        responseReturn.put("status", status);
        responseReturn.put("responseData", state);

        Comm.logger.debug("S15_close_stop_latch2_COMM_Bay : commBay_StopLatch_Status : status : " + status);
        Comm.logger.debug("S15_close_stop_latch2_COMM_Bay : commBay_StopLatch_Status : Exit");
        return responseReturn;
    }
}
