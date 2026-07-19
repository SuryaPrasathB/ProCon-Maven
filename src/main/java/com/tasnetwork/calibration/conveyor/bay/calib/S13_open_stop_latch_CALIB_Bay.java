package com.tasnetwork.calibration.conveyor.bay.calib;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.pallet.PalletTrackerController;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S13_open_stop_latch_CALIB_Bay implements CalibrationBayState {

    @Override
    public BayResponse handleRequest() {
        Calib.logger.info("S13_open_stop_latch_CALIB_Bay : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        // BayUtils.delay(2000);
        Calib.logger.info("S13_open_stop_latch_CALIB_Bay: Waiting for Halt Pallet at Calib-Entry");

        while ((!Calib.isStopProcessRequestedCalibBay()) &&
                (ConveyorDataManager.isHaltPalletActiveInCalib())) {
            BayUtils.delay(1000);
        }

        Calib.logger.info("S13_open_stop_latch_CALIB_Bay: Waiting for Halt Pallet at Calib-Exit");

        Calib.logger.info("S13_open_stop_latch_CALIB_Bay: Waiting for NoEntry at Calib-Entry for Verific1Waiting");
        while ((!Calib.isStopProcessRequestedCalibBay()) &&
                (ConveyorDataManager.isNoEntryActiveInVerific1Waiting())) {
            BayUtils.delay(1000);
        }

        Calib.logger.info("S13_open_stop_latch_CALIB_Bay: Waiting for NoEntry at Calib-Exit for Verific1Waiting");

        if (!Calib.isStopProcessRequestedCalibBay()) {
            Map<String, Object> responseReturn = open_StopLatch_CALIB_Bay();

            boolean openStopLatch_CALIB_Bay = (boolean) responseReturn.get("status");

            if (openStopLatch_CALIB_Bay) {
                ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayExitStopper(getMyBayKey(),
                        true);
                Calib.logger.info("S13_open_stop_latch_CALIB_Bay : Stop Latch Opened");
                bayResponse.setStatus(true);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

                PalletTrackerController palletTracker = new PalletTrackerController();
                palletTracker.switchPalletToNextBay(myBayKey, ConstantConveyor.WAITING_BAY_KEY);

                BayUtils.delay(1000);
            } else {
                Calib.logger.info("S13_open_stop_latch_CALIB_Bay : Failed to Open Stop Latch");
                bayResponse.setStatus(false);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_008);
            }
        }
        Calib.logger.info("S13_open_stop_latch_CALIB_Bay : Exit");
        return bayResponse;
    }

    private Map<String, Object> open_StopLatch_CALIB_Bay() {
        Calib.logger.debug("S13_open_stop_latch_CALIB_Bay : calibBay_StopLatch_Status : Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<>();
        responseReturn.put("status", false);

        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.CALIB_PORT_NAME_STPR);

        String state = "";
        if (portInfo != null) {
            Calib.logger.debug("PortId    : " + portInfo.getPortId());
            Calib.logger.debug("ClusterId : " + portInfo.getClusterId());
            Calib.logger.debug("BayId     : " + portInfo.getBayId());

            String outputActive = Constant_IO_ActionMapping.OFF;
            BayUtils bayUtils = new BayUtils();

            state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                    portInfo.getBayId(),
                    portInfo.getPortId(),
                    outputActive);

            Calib.logger.debug("S13_open_stop_latch_CALIB_Bay : open_StopLatch_CALIB_Bay : state : " + state);
            /*
             * if (simulateCalibBayHappyPath) {
             * state = Constant_IO_ActionMapping.ON;
             * }
             */

            status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false;

            if (StateExecutorController.simulateCalibBayHappyPath) {
                status = true;
            }

            Calib.logger.debug("S13_open_stop_latch_CALIB_Bay : open_StopLatch_CALIB_Bay : status : " + status);

        } else {
            Calib.logger.debug("S13_open_stop_latch_CALIB_Bay : Output port not found");
            return responseReturn;
        }

        responseReturn.put("status", status);
        responseReturn.put("responseData", state);

        Calib.logger.debug("S13_open_stop_latch_CALIB_Bay : calibBay_StopLatch_Status : status : " + status);

        responseReturn.put("status", status);

        Calib.logger.debug("S13_open_stop_latch_CALIB_Bay : calibBay_StopLatch_Status : Exit");
        return responseReturn;
    }
}
