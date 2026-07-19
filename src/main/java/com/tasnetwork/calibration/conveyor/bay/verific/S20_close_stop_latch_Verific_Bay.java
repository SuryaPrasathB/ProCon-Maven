package com.tasnetwork.calibration.conveyor.bay.verific;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S20_close_stop_latch_Verific_Bay implements VerificTestBayState {

    @Override
    public BayResponse handleRequest() {
        Verification.logger.info("S20_close_stop_latch_Verific_Bay : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

        Map<String, Object> responseReturn = close_StopLatch_Verific_Bay();

        boolean closeStopLatch_Verific_Bay = (boolean) responseReturn.get("status");

        if (closeStopLatch_Verific_Bay) {
            Verification.logger.info("S20_close_stop_latch_Verific_Bay : Stop Latch Closed");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

            BayUtils.delay(3000);
        } else {
            Verification.logger.info("S20_close_stop_latch_Verific_Bay : Failed to Close Stop Latch");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_007);
        }

        Verification.logger.info("S20_close_stop_latch_Verific_Bay : Exit");
        return bayResponse;
    }

    private Map<String, Object> close_StopLatch_Verific_Bay() {
        Verification.logger.debug("S20_close_stop_latch_Verific_Bay : verificBay_StopLatch_Status : Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<>();
        responseReturn.put("status", false);

        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.VERIFIC_PORT_NAME_STPR);

        String state = "";
        if (portInfo != null) {
            Verification.logger.debug("PortId    : " + portInfo.getPortId());
            Verification.logger.debug("ClusterId : " + portInfo.getClusterId());
            Verification.logger.debug("BayId     : " + portInfo.getBayId());

            String outputActive = Constant_IO_ActionMapping.ON;
            BayUtils bayUtils = new BayUtils();

            state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                    portInfo.getBayId(),
                    portInfo.getPortId(),
                    outputActive);

            Verification.logger
                    .debug("S20_close_stop_latch_Verific_Bay : close_StopLatch_Verific_Bay : state : " + state);
            /*
             * if (simulateVerificationBayHappyPath) {
             * state = Constant_IO_ActionMapping.OFF;
             * }
             */

            status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

            if (StateExecutorController.simulateVerificBayHappyPath) {
                state = Constant_IO_ActionMapping.OPEN;
            }

            Verification.logger
                    .debug("S20_close_stop_latch_Verific_Bay : close_StopLatch_Verific_Bay : status : " + status);

        } else {
            Verification.logger.debug("S20_close_stop_latch_Verific_Bay : Output port not found");
            return responseReturn;
        }

        responseReturn.put("status", status);
        responseReturn.put("responseData", state);

        Verification.logger
                .debug("S20_close_stop_latch_Verific_Bay : verificBay_StopLatch_Status : status : " + status);
        Verification.logger.debug("S20_close_stop_latch_Verific_Bay : verificBay_StopLatch_Status : Exit");
        return responseReturn;
    }
}
