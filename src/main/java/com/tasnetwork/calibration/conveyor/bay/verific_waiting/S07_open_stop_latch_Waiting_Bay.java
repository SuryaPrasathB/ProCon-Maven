package com.tasnetwork.calibration.conveyor.bay.verific_waiting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.Constant_Motor_Requirement;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.pallet.PalletTrackerController;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S07_open_stop_latch_Waiting_Bay implements WaitingBayState {
    PalletTrackerController palletTracker = new PalletTrackerController();
    private BayUtils bayUtils = new BayUtils();

    @Override
    public BayResponse handleRequest() {
        VerificWaiting.logger.info("S07_open_stop_latch_Waiting_Bay : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

        VerificWaiting.logger.info("S07_open_stop_latch_Waiting_Bay: Waiting for Halt Pallet at Verific1Waiting-Entry");

        while ((!VerificWaiting.isStopProcessRequestedWaitingBay()) &&
                (ConveyorDataManager.isHaltPalletActiveInVerific1Waiting())) {
            BayUtils.delay(1000);
        }

        VerificWaiting.logger.info("S07_open_stop_latch_Waiting_Bay: Waiting for Halt Pallet at Verific1Waiting-Exit");

        VerificWaiting.logger
                .info("S07_open_stop_latch_Waiting_Bay: Waiting for NoEntry at Verific1Waiting-Entry for Verific1");
        while ((!VerificWaiting.isStopProcessRequestedWaitingBay()) &&
                (ConveyorDataManager.isNoEntryActiveInVerific1())) {
            BayUtils.delay(1000);
        }

        VerificWaiting.logger
                .info("S07_open_stop_latch_Waiting_Bay: Waiting for NoEntry at Verific1Waiting-Exit for Verific1");

        ConveyorDataManager.setWaitingVerific1BayPalletsAllCleared(false);
        Map<String, Object> responseReturn = open_StopLatch_Waiting_Bay();

        boolean openStopLatch_Waiting_Bay = (boolean) responseReturn.get("status");

        if (openStopLatch_Waiting_Bay) {
            VerificWaiting.logger.info("S07_open_stop_latch_Waiting_Bay : Stop Latch Opened");
            ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayExitStopper(getMyBayKey(), true);

            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

            if (ProconFeatureEnable.MOTOR_CONTROL_ENABLE) {
                BayUtils.delay(2000);
                List<String> motor_requirement = Constant_Motor_Requirement.WAITING_MOTOR_REQUIRED;

                responseReturn = bayUtils.set_motor_required(getMyBayKey(), motor_requirement);

                boolean set_motor_required = (boolean) responseReturn.get("status");
                if (set_motor_required) {
                    VerificWaiting.logger.info("S02_check_for_pallets_at_Verific_Bay : set_motor_required : Success");
                    bayResponse.setStatus(true);
                    bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
                } else {
                    VerificWaiting.logger.info("S02_check_for_pallets_at_Verific_Bay : Failed to set_motor_required ");
                    bayResponse.setStatus(false);
                    bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_026);
                }
            }

            int palletMoveMentWaitTimeInSec = ConstantConveyorConfig.WAITING_TO_VERIFIC_BAY_PALLET_MOVEMENT_WAIT_TIME_IN_SEC;// 30;
            VerificWaiting.logger.info("S07_open_stop_latch_Waiting_Bay : palletMoveMentWaiting start:");
            
            // Log exit from current bay before the wait
            palletTracker.exitBatchFromPresentBay(myBayKey);

            while ((palletMoveMentWaitTimeInSec > 0) && (!BayUtils.isUserAborted())) {
                VerificWaiting.logger
                        .info("S07_open_stop_latch_Waiting_Bay : Stop Latch Opened : palletMoveMentWaitTimeInSec: "
                                + palletMoveMentWaitTimeInSec);
                BayUtils.delay(1000);
                palletMoveMentWaitTimeInSec--;
            }
            VerificWaiting.logger.info("S07_open_stop_latch_Waiting_Bay : palletMoveMentWaiting stop:");
            
            // Log entry to next bay after the wait
            palletTracker.enterBatchToNextBay(myBayKey, ConstantConveyor.VERIFICATION_BAY_KEY);

            BayUtils.delay(1000);
        } else {
            VerificWaiting.logger.info("S07_open_stop_latch_Waiting_Bay : Failed to Open Stop Latch");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_WAITING_005);
        }

        VerificWaiting.logger.info("S07_open_stop_latch_Waiting_Bay : Exit");
        return bayResponse;
    }

    // ==============================================================================================================
    private Map<String, Object> open_StopLatch_Waiting_Bay() {
        VerificWaiting.logger.debug("S07_open_stop_latch_Waiting_Bay : waitingBay_StopLatch_Status : Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<>();
        responseReturn.put("status", false);

        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.WAITING_PORT_NAME_STPR);

        String state = "";
        if (portInfo != null) {
            VerificWaiting.logger.debug("PortId    : " + portInfo.getPortId());
            VerificWaiting.logger.debug("ClusterId : " + portInfo.getClusterId());
            VerificWaiting.logger.debug("BayId     : " + portInfo.getBayId());

            String outputActive = Constant_IO_ActionMapping.OFF;
            BayUtils bayUtils = new BayUtils();

            state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                    portInfo.getBayId(),
                    portInfo.getPortId(),
                    outputActive);

            VerificWaiting.logger
                    .debug("S07_open_stop_latch_Waiting_Bay : open_StopLatch_Waiting_Bay : state : " + state);
            /*
             * if (simulateWaitingBayHappyPath) {
             * state = Constant_IO_ActionMapping.ON;
             * }
             */

            status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false;

            if (StateExecutorController.simulateWaitingBayHappyPath) {
                status = true;
            }

            /*
             * PalletTrackerController palletTracker = new PalletTrackerController();
             * palletTracker.switchBatchToNextBay(myBayKey,
             * ConstantConveyor.VERIFICATION_BAY_KEY);
             */

            VerificWaiting.logger
                    .debug("S07_open_stop_latch_Waiting_Bay : open_StopLatch_Waiting_Bay : status : " + status);

        } else {
            VerificWaiting.logger.debug("S07_open_stop_latch_Waiting_Bay : Output port not found");
            return responseReturn;
        }

        responseReturn.put("status", status);
        responseReturn.put("responseData", state);

        VerificWaiting.logger
                .debug("S07_open_stop_latch_Waiting_Bay : waitingBay_StopLatch_Status : status : " + status);
        VerificWaiting.logger.debug("S07_open_stop_latch_Waiting_Bay : waitingBay_StopLatch_Status : Exit");
        return responseReturn;
    }
}
