package com.tasnetwork.calibration.conveyor.bay.verific;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.pallet.PalletTrackerController;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S221_close_stop_latch2_Verific_Bay implements VerificTestBayState {

    @Override
    public BayResponse handleRequest() {
        PalletTrackerController palletTracker = new PalletTrackerController();

        Verification.logger.info("S221_close_stop_latch2_Verific_Bay : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        BayUtils.delay(2000);

        Map<String, Object> responseReturn = close_StopLatch_Verific_Bay();

        boolean closeStopLatch_Verific_Bay = (boolean) responseReturn.get("status");

        if (closeStopLatch_Verific_Bay) {
            Verification.logger.info("S221_close_stop_latch2_Verific_Bay : Stop Latch Closed");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
            BayUtils.delay(3000);
        } else {
            Verification.logger.info("S221_close_stop_latch2_Verific_Bay : Failed to Close Stop Latch");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_020);
        }

        Verification.logger.info("S221_close_stop_latch2_Verific_Bay : getPalletsPassedVerificBay()"
                + S191_open_stop_latch_Verific_Bay.getPalletsPassedVerificBay());

        if (S191_open_stop_latch_Verific_Bay.getPalletsPassedVerificBay() == ConstantConveyor.NUM_PALLETS_VERFIC_BAY) {
            Verification.logger.info("S221_close_stop_latch2_Verific_Bay : All pallets passed out of Verific Bay");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_022);
            S191_open_stop_latch_Verific_Bay.setPalletsPassedVerificBay(0);

            // palletTracker.switchBatchToNextBay(myBayKey,
            // ConstantConveyor.STA_NLD2_BAY_KEY);

            // BayUtils.delay(15000); // Delay for Pallets to reach destination Bay
            int palletMoveMentWaitTimeInSec = ConstantConveyorConfig.VERIFIC_TO_STA2_BAY_PALLET_MOVEMENT_WAIT_TIME_IN_SEC;// 30;
            Verification.logger.info("S221_close_stop_latch2_Verific_Bay : palletMoveMentverificToSta2 : start:");

            // Log exit from current bay before the wait
            palletTracker.exitBatchFromPresentBay(myBayKey);
            while ((palletMoveMentWaitTimeInSec > 0) && (!BayUtils.isUserAborted())) {
                Verification.logger.info(
                        "S221_close_stop_latch2_Verific_Bay : palletMoveMentverificToSta2 : palletMoveMentWaitTimeInSec: "
                                + palletMoveMentWaitTimeInSec);
                BayUtils.delay(1000);
                palletMoveMentWaitTimeInSec--;
            }
            Verification.logger.info(
                    "S221_close_stop_latch2_Verific_Bay : palletMoveMentverificToSta2 : palletMoveMentWaiting stop:");

            if (ProconFeatureEnable.MOTOR_CONTROL_ENABLE) {
                BayUtils bayUtils = new BayUtils();
                responseReturn = bayUtils.set_motor_not_required(getMyBayKey());
                boolean set_motor_not_required = (boolean) responseReturn.get("status");
                if (set_motor_not_required) {
                    Verification.logger
                            .info("S221_close_stop_latch2_Verific_Bay : set_motor_not_required : Success");
                    bayResponse.setStatus(false);
                    bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_022);
                } else {
                    Verification.logger
                            .info("S221_close_stop_latch2_Verific_Bay : Failed to set_motor_not_required ");
                    bayResponse.setStatus(false);
                    bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_026);
                }
            }

            // Log entry to next bay after the wait
            palletTracker.enterBatchToNextBay(myBayKey, ConstantConveyor.STA_NLD2_BAY_KEY);
            Verification.logger.info("S22_close_stop_latch2_Verific_Bay : Pallets Cleared");
            // ConstantConveyor.VERIFICATION_BAY_PALLETS_CLEARED = true;
            ConveyorDataManager.setVerific1PalletsAllCleared(true);
        }

        Verification.logger.info("S221_close_stop_latch2_Verific_Bay : Exit");
        return bayResponse;
    }

    private Map<String, Object> close_StopLatch_Verific_Bay() {
        Verification.logger.debug("S221_close_stop_latch2_Verific_Bay : verificBay_StopLatch_Status : Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<>();
        responseReturn.put("status", false);

        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.VERIFIC_PORT_NAME_STPR2);

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
                    .debug("S221_close_stop_latch2_Verific_Bay : close_StopLatch_Verific_Bay : state : " + state);
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
                    .debug("S221_close_stop_latch2_Verific_Bay : close_StopLatch_Verific_Bay : status : " + status);

        } else {
            Verification.logger.debug("S221_close_stop_latch2_Verific_Bay : Output port not found");
            return responseReturn;
        }

        responseReturn.put("status", status);
        responseReturn.put("responseData", state);

        Verification.logger
                .debug("S221_close_stop_latch2_Verific_Bay : verificBay_StopLatch_Status : status : " + status);
        Verification.logger.debug("S221_close_stop_latch2_Verific_Bay : verificBay_StopLatch_Status : Exit");
        return responseReturn;
    }
}
