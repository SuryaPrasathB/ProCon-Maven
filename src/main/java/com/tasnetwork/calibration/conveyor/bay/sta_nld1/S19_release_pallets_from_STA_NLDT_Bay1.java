package com.tasnetwork.calibration.conveyor.bay.sta_nld1;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.pallet.PalletTrackerController;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;

public class S19_release_pallets_from_STA_NLDT_Bay1 implements STA_NoLoadTestBay1State {

    @Override
    public BayResponse handleRequest() {

        StaNld_Bay1.logger.info("S19_release_pallets_from_STA_NLDT_Bay1 : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        BayUtils.delay(4000);

        bayResponse = releasePalletsFromStaNlt_Bay1();


        StaNld_Bay1.logger.info("S19_release_pallets_from_STA_NLDT_Bay1 : Exit");
        return bayResponse;
    }

    private BayResponse releasePalletsFromStaNlt_Bay1() {

        int noOfPalletsExited = 0;
        int maxNoOfPalletsInStaNld1 = DeviceDataManagerController.getConveyorConfigParsedKey()
                .getMaxNoOfPalletsInStaNld1();
        int postDelaySta1ExitOpenStopLatch1_InMsec = 1000
                * DeviceDataManagerController.getConveyorConfigParsedKey().getPostDelaySta1ExitOpenStopLatch1_InSec();
        int postDelaySta1ExitOpenStopLatch2_InMsec = 1000
                * DeviceDataManagerController.getConveyorConfigParsedKey().getPostDelaySta1ExitOpenStopLatch2_InSec();
        int postDelaySta1ExitCloseStopLatch1_InMsec = 1000
                * DeviceDataManagerController.getConveyorConfigParsedKey().getPostDelaySta1ExitCloseStopLatch1_InSec();
        int postDelaySta1ExitCloseStopLatch2_InMsec = 1000
                * DeviceDataManagerController.getConveyorConfigParsedKey().getPostDelaySta1ExitCloseStopLatch2_InSec();

        BayResponse bayResponse = close_StopLatch2_SCT_NLT_Bay1();


        if (bayResponse.getStatus()) {
            StaNld_Bay1.logger.info("S19_release_pallets_from_STA_NLDT_Bay1 : Stop Latch Closed");

            noOfPalletsExited = 0;
            while ((noOfPalletsExited <= maxNoOfPalletsInStaNld1)
                    && (!StaNld_Bay1.isStopProcessRequestedStaNldBay1())) {
                if (noOfPalletsExited < maxNoOfPalletsInStaNld1) {
                    BayUtils.delay(postDelaySta1ExitCloseStopLatch2_InMsec);
                    StaNld_Bay1.logger
                            .info("S19_release_pallets_from_STA_NLDT_Bay1 : open_StopLatch_SCT_NLT_Bay1 : Hit1 ");
                    bayResponse = open_StopLatch_SCT_NLT_Bay1();
                    if (bayResponse.getStatus()) {
                        BayUtils.delay(postDelaySta1ExitOpenStopLatch1_InMsec);
                        StaNld_Bay1.logger
                                .info("S19_release_pallets_from_STA_NLDT_Bay1 : close_StopLatch_SCT_NLT_Bay1 : Hit2 ");
                        bayResponse = close_StopLatch_SCT_NLT_Bay1();
                        if (bayResponse.getStatus()) {
                            BayUtils.delay(postDelaySta1ExitCloseStopLatch1_InMsec);
                            StaNld_Bay1.logger.info(
                                    "S19_release_pallets_from_STA_NLDT_Bay1 : open_StopLatch2_SCT_NLT_Bay1 : Hit3 ");
                            bayResponse = open_StopLatch2_SCT_NLT_Bay1();
                            if (bayResponse.getStatus()) {
                                StaNld_Bay1.logger.info(
                                        "S19_release_pallets_from_STA_NLDT_Bay1 : close_StopLatch2_SCT_NLT_Bay1 : Hit4 ");
                                BayUtils.delay(postDelaySta1ExitOpenStopLatch2_InMsec);
                                bayResponse = close_StopLatch2_SCT_NLT_Bay1();
                                noOfPalletsExited++;
                                StaNld_Bay1.logger.info("S19_release_pallets_from_STA_NLDT_Bay1 : noOfPalletsExited :"
                                        + noOfPalletsExited);
                                if (!bayResponse.getStatus()) {
                                    StaNld_Bay1.logger.info(
                                            "S19_release_pallets_from_STA_NLDT_Bay1 : Failed to Close Stop Latch2");
                                    bayResponse.setStatus(false);
                                    bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_018);
                                }
                            } else {
                                StaNld_Bay1.logger
                                        .info("S19_release_pallets_from_STA_NLDT_Bay1 : Failed to Open Stop Latch2");
                                bayResponse.setStatus(false);
                                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_019);
                            }
                        } else {
                            StaNld_Bay1.logger
                                    .info("S19_release_pallets_from_STA_NLDT_Bay1 : Failed to Close Stop Latch1");
                            bayResponse.setStatus(false);
                            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_007);
                        }
                    } else {
                        StaNld_Bay1.logger.info("S19_release_pallets_from_STA_NLDT_Bay1 : Failed to Open Stop Latch1");
                        bayResponse.setStatus(false);
                        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_005);
                        break;
                    }
                } else {
                    bayResponse.setStatus(true);
                    bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
                    PalletTrackerController palletTracker = new PalletTrackerController();
                    palletTracker.switchBatchToNextBay(myBayKey, ConstantConveyor.UNLOADING_BAY_KEY);
                    StaNld_Bay1.logger.info("S19_release_pallets_from_STA_NLDT_Bay1 : achieved :" + noOfPalletsExited);
                    noOfPalletsExited++;

                }
            }
        } else {
            StaNld_Bay1.logger.info("S19_release_pallets_from_STA_NLDT_Bay1 : Failed to Close Stop Latch-begin");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_018);
        }


        return bayResponse;
    }

    private BayResponse close_StopLatch_SCT_NLT_Bay1() {
        StaNld_Bay1.logger.debug("S19_release_pallets_from_STA_NLDT_Bay1 : close_StopLatch_SCT_NLT_Bay1 : Entry");

        boolean status = false;

        BayResponse bayResponse = new BayResponse();
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

            StaNld_Bay1.logger
                    .debug("S19_release_pallets_from_STA_NLDT_Bay1 : close_StopLatch_SCT_NLT_Bay1 : state : " + state);

            status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

            if (StateExecutorController.simulateSCTNLTBay1HappyPath) {
                status = true;
            }

            StaNld_Bay1.logger.debug(
                    "S19_release_pallets_from_STA_NLDT_Bay1 : close_StopLatch_SCT_NLT_Bay1 : status : " + status);

        } else {
            StaNld_Bay1.logger.debug(
                    "S19_release_pallets_from_STA_NLDT_Bay1 : close_StopLatch_SCT_NLT_Bay1 : Output port not found");
        }


        bayResponse.setStatus(status);
        bayResponse.setResponseData(state);

        StaNld_Bay1.logger
                .debug("S19_release_pallets_from_STA_NLDT_Bay1 : close_StopLatch_SCT_NLT_Bay1 : status : " + status);
        StaNld_Bay1.logger.debug("S19_release_pallets_from_STA_NLDT_Bay1 : close_StopLatch_SCT_NLT_Bay1 : Exit");
        return bayResponse;
    }

    private BayResponse open_StopLatch_SCT_NLT_Bay1() {
        StaNld_Bay1.logger.debug("S19_release_pallets_from_STA_NLDT_Bay1 : open_StopLatch_SCT_NLT_Bay1 : Entry");

        boolean status = false;

        BayResponse bayResponse = new BayResponse();
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.SCT_NLT1_PORT_NAME_STPR);

        String state = "";
        if (portInfo != null) {
            StaNld_Bay1.logger.debug("PortId    : " + portInfo.getPortId());
            StaNld_Bay1.logger.debug("ClusterId : " + portInfo.getClusterId());
            StaNld_Bay1.logger.debug("BayId     : " + portInfo.getBayId());

            String outputActive = Constant_IO_ActionMapping.OFF;
            BayUtils bayUtils = new BayUtils();

            state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                    portInfo.getBayId(),
                    portInfo.getPortId(),
                    outputActive);

            StaNld_Bay1.logger
                    .debug("S19_release_pallets_from_STA_NLDT_Bay1 : open_StopLatch_SCT_NLT_Bay1 : state : " + state);

            status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false;

            if (StateExecutorController.simulateSCTNLTBay1HappyPath) {
                status = true;
            }

            StaNld_Bay1.logger
                    .debug("S19_release_pallets_from_STA_NLDT_Bay1 : open_StopLatch_SCT_NLT_Bay1 : status : " + status);

        } else {
            StaNld_Bay1.logger.debug(
                    "S19_release_pallets_from_STA_NLDT_Bay1 : open_StopLatch_SCT_NLT_Bay1: Output port not found");
            return bayResponse;// responseReturn ;
        }


        bayResponse.setStatus(status);
        bayResponse.setResponseData(state);

        StaNld_Bay1.logger
                .debug("S19_release_pallets_from_STA_NLDT_Bay1 : open_StopLatch_SCT_NLT_Bay1 : status : " + status);
        StaNld_Bay1.logger.debug("S19_release_pallets_from_STA_NLDT_Bay1 : open_StopLatch_SCT_NLT_Bay1 : Exit");
        return bayResponse;
    }

    private BayResponse close_StopLatch2_SCT_NLT_Bay1() {
        StaNld_Bay1.logger.debug("S19_release_pallets_from_STA_NLDT_Bay1 : close_StopLatch2_SCT_NLT_Bay1 : Entry");

        boolean status = false;

        BayResponse bayResponse = new BayResponse();
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.SCT_NLT1_PORT_NAME_STPR2);

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

            StaNld_Bay1.logger
                    .debug("S19_release_pallets_from_STA_NLDT_Bay1 : close_StopLatch2_SCT_NLT_Bay1 : state : " + state);

            status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

            if (StateExecutorController.simulateSCTNLTBay1HappyPath) {
                status = true;
            }

            StaNld_Bay1.logger.debug(
                    "S19_release_pallets_from_STA_NLDT_Bay1 : close_StopLatch2_SCT_NLT_Bay1 : status : " + status);

        } else {
            StaNld_Bay1.logger.debug(
                    "S19_release_pallets_from_STA_NLDT_Bay1 : close_StopLatch2_SCT_NLT_Bay1: Output port not found");
        }


        bayResponse.setStatus(status);
        bayResponse.setResponseData(state);

        StaNld_Bay1.logger
                .debug("S19_release_pallets_from_STA_NLDT_Bay1 : close_StopLatch2_SCT_NLT_Bay1 : status : " + status);
        StaNld_Bay1.logger.debug("S19_release_pallets_from_STA_NLDT_Bay1 : close_StopLatch2_SCT_NLT_Bay1 : Exit");
        return bayResponse;
    }

    private BayResponse open_StopLatch2_SCT_NLT_Bay1() {
        StaNld_Bay1.logger.debug("S19_release_pallets_from_STA_NLDT_Bay1 : open_StopLatch2_SCT_NLT_Bay1 : Entry");

        boolean status = false;

        BayResponse bayResponse = new BayResponse();
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.SCT_NLT1_PORT_NAME_STPR2);

        String state = "";
        if (portInfo != null) {
            StaNld_Bay1.logger.debug("PortId    : " + portInfo.getPortId());
            StaNld_Bay1.logger.debug("ClusterId : " + portInfo.getClusterId());
            StaNld_Bay1.logger.debug("BayId     : " + portInfo.getBayId());

            String outputActive = Constant_IO_ActionMapping.OFF;
            BayUtils bayUtils = new BayUtils();

            state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                    portInfo.getBayId(),
                    portInfo.getPortId(),
                    outputActive);

            StaNld_Bay1.logger
                    .debug("S19_release_pallets_from_STA_NLDT_Bay1 : open_StopLatch2_SCT_NLT_Bay1 : state : " + state);

            status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false;

            if (StateExecutorController.simulateSCTNLTBay1HappyPath) {
                status = true;
            }

            StaNld_Bay1.logger.debug(
                    "S19_release_pallets_from_STA_NLDT_Bay1 : open_StopLatch2_SCT_NLT_Bay1 : status : " + status);

        } else {
            StaNld_Bay1.logger.debug(
                    "S19_release_pallets_from_STA_NLDT_Bay1 : open_StopLatch2_SCT_NLT_Bay1: Output port not found");
            return bayResponse;
        }


        bayResponse.setStatus(status);
        bayResponse.setResponseData(state);

        StaNld_Bay1.logger
                .debug("S19_release_pallets_from_STA_NLDT_Bay1 : open_StopLatch2_SCT_NLT_Bay1 : status : " + status);
        StaNld_Bay1.logger.debug("S19_release_pallets_from_STA_NLDT_Bay1 : open_StopLatch2_SCT_NLT_Bay1 : Exit");
        return bayResponse;
    }

}
