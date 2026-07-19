package com.tasnetwork.calibration.conveyor.bay.verific;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.Constant_Motor_Requirement;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S19_open_stop_latch_Verific_Bay implements VerificTestBayState {

    static int palletsPassedVerificBay = 0;

    @Override
    public BayResponse handleRequest() {
        Verification.logger.info("S19_open_stop_latch_Verific_Bay : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

        Map<String, Object> responseReturn = open_StopLatch_Verific_Bay();

        boolean openStopLatch_Verific_Bay = (boolean) responseReturn.get("status");

        if (openStopLatch_Verific_Bay) {
            Verification.logger.info("S19_open_stop_latch_Verific_Bay : Stop Latch Opened");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

            if (S044_Close_Run_Project.isVerificationTestCompleted()) {
                palletsPassedVerificBay++;
            } else {
                palletsPassedVerificBay++; // REMOVE LATER
            }

            // BayUtils.delay(2000);

            if (ProconFeatureEnable.MOTOR_CONTROL_ENABLE) {
                BayUtils.delay(2000);
                BayUtils bayUtils = new BayUtils();
                responseReturn = bayUtils.set_motor_required(getMyBayKey(),
                        Constant_Motor_Requirement.VERIFIC_MOTOR_STA1_REQUIRED);
                boolean set_motor_required = (boolean) responseReturn.get("status");
                if (set_motor_required) {
                    Verification.logger.info("set_motor_required : Success");
                    bayResponse.setStatus(true);
                    bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
                } else {
                    Verification.logger.info("Failed to set_motor_required ");
                    bayResponse.setStatus(false);
                    bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_026);
                }
            } else {
                bayResponse.setStatus(true);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
            }

            Verification.logger
                    .info("S19_open_stop_latch_Verific_Bay : palletsPassedVerificBay : " + palletsPassedVerificBay);

            Verification.logger.info("S19_open_stop_latch_Verific_Bay : Stop Latch Opened");

            BayUtils.delay(3000);
        } else {

            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_005);
        }

        Verification.logger.info("S19_open_stop_latch_Verific_Bay : Exit");
        return bayResponse;
    }

    private Map<String, Object> open_StopLatch_Verific_Bay() {
        Verification.logger.debug("S19_open_stop_latch_Verific_Bay : verificBay_StopLatch_Status : Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<>();
        responseReturn.put("status", false);

        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.VERIFIC_PORT_NAME_STPR);

        String state = "";
        if (portInfo != null) {
            Verification.logger.debug("PortId    : " + portInfo.getPortId());
            Verification.logger.debug("ClusterId : " + portInfo.getClusterId());
            Verification.logger.debug("BayId     : " + portInfo.getBayId());

            String outputActive = Constant_IO_ActionMapping.OFF;
            BayUtils bayUtils = new BayUtils();

            state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                    portInfo.getBayId(),
                    portInfo.getPortId(),
                    outputActive);

            Verification.logger
                    .debug("S19_open_stop_latch_Verific_Bay : open_StopLatch_Verific_Bay : state : " + state);

            status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false;

            if (StateExecutorController.simulateVerificBayHappyPath) {
                state = Constant_IO_ActionMapping.OPEN;
            }

            Verification.logger
                    .debug("S19_open_stop_latch_Verific_Bay : open_StopLatch_Verific_Bay : status : " + status);

        } else {
            Verification.logger.debug("S19_open_stop_latch_Verific_Bay : Output port not found");
            return responseReturn;
        }

        responseReturn.put("status", status);
        responseReturn.put("responseData", state);

        Verification.logger.debug("S19_open_stop_latch_Verific_Bay : verificBay_StopLatch_Status : status : " + status);
        Verification.logger.debug("S19_open_stop_latch_Verific_Bay : verificBay_StopLatch_Status : Exit");
        return responseReturn;
    }

    public static int getPalletsPassedVerificBay() {
        return palletsPassedVerificBay;
    }

    public static void setPalletsPassedVerificBay(int palletsPassedVerificBay) {
        S19_open_stop_latch_Verific_Bay.palletsPassedVerificBay = palletsPassedVerificBay;
    }
}
