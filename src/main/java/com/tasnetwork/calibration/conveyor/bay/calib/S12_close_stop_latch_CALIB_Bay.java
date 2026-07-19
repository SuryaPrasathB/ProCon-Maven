package com.tasnetwork.calibration.conveyor.bay.calib;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S12_close_stop_latch_CALIB_Bay implements CalibrationBayState {

    @Override
    public BayResponse handleRequest() {
        Calib.logger.info("S12_close_stop_latch_CALIB_Bay : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        // BayUtils.delay(2000);
        BayUtils.delay(1000);
        Map<String, Object> responseReturn = close_StopLatch_CALIB_Bay();

        boolean closeStopLatch_CALIB_Bay = (boolean) responseReturn.get("status");

        if (closeStopLatch_CALIB_Bay) {
            Calib.logger.info("S12_close_stop_latch_CALIB_Bay : Stop Latch Closed");
            ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayExitStopper(getMyBayKey(),
                    false);
            BayUtils.delay(3000);

            BayUtils bayUtils = new BayUtils();
            responseReturn = bayUtils.set_motor_not_required(getMyBayKey());

            boolean set_motor_not_required = (boolean) responseReturn.get("status");

            if (set_motor_not_required) {
                Calib.logger.info("set_motor_not_required : Success");
                bayResponse.setStatus(true);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
            } else {
                Calib.logger.info("Failed to set_motor_not_required ");
                bayResponse.setStatus(false);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_026);
            }

            // bayResponse.setStatus(true);
            // bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        } else {
            Calib.logger.info("S12_close_stop_latch_CALIB_Bay : Failed to Close Stop Latch");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_009);
        }

        Calib.logger.info("S12_close_stop_latch_CALIB_Bay : Exit");
        return bayResponse;
    }

    private Map<String, Object> close_StopLatch_CALIB_Bay() {
        Calib.logger.debug("S12_close_stop_latch_CALIB_Bay : calibBay_StopLatch_Status : Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<>();
        responseReturn.put("status", false);

        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.CALIB_PORT_NAME_STPR);

        String state = "";
        if (portInfo != null) {
            Calib.logger.debug("PortId    : " + portInfo.getPortId());
            Calib.logger.debug("ClusterId : " + portInfo.getClusterId());
            Calib.logger.debug("BayId     : " + portInfo.getBayId());

            String outputActive = Constant_IO_ActionMapping.ON;
            BayUtils bayUtils = new BayUtils();

            state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                    portInfo.getBayId(),
                    portInfo.getPortId(),
                    outputActive);

            Calib.logger.debug("S12_close_stop_latch_CALIB_Bay : close_StopLatch_CALIB_Bay : state : " + state);
            /*
             * if (simulateCalibBayHappyPath) {
             * state = Constant_IO_ActionMapping.OFF;
             * }
             */

            status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

            if (StateExecutorController.simulateCalibBayHappyPath) {
                status = true;
            }

            Calib.logger.debug("S12_close_stop_latch_CALIB_Bay : close_StopLatch_CALIB_Bay : status : " + status);

        } else {
            Calib.logger.debug("S12_close_stop_latch_CALIB_Bay : Output port not found");
            return responseReturn;
        }

        responseReturn.put("status", status);
        responseReturn.put("responseData", state);

        Calib.logger.debug("S12_close_stop_latch_CALIB_Bay : calibBay_StopLatch_Status : status : " + status);

        responseReturn.put("status", status);

        Calib.logger.debug("S12_close_stop_latch_CALIB_Bay : calibBay_StopLatch_Status : Exit");
        return responseReturn;
    }
}
