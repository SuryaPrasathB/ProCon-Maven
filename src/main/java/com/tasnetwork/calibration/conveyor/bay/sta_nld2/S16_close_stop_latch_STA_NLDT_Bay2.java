package com.tasnetwork.calibration.conveyor.bay.sta_nld2;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
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

public class S16_close_stop_latch_STA_NLDT_Bay2 implements STA_NoLoadTestBay2State {

    @Override
    public BayResponse handleRequest() {
        StaNld_Bay2.logger.info("S16_close_stop_latch_SCT_NLT_Bay2 : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        BayUtils.delay(2000);

        Map<String, Object> responseReturn = close_StopLatch_SCT_NLT_Bay2();

        boolean closeStopLatch_SCT_NLT_Bay2 = (boolean) responseReturn.get("status");

        if (closeStopLatch_SCT_NLT_Bay2) {
            StaNld_Bay2.logger.info("S16_close_stop_latch_SCT_NLT_Bay2 : Stop Latch Closed");
            ConveyorDataManager.setSta2PalletsExitInProgress(false);
            ConveyorDataManager.setSta2PalletsAllCleared(true);
            ConveyorDataManager.getDashboardObject().removeAllPalletsFromSta2Bays();
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        } else {
            StaNld_Bay2.logger.info("S16_close_stop_latch_SCT_NLT_Bay2 : Failed to Close Stop Latch");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_009);
        }

        StaNld_Bay2.logger.info("S16_close_stop_latch_SCT_NLT_Bay2 : Exit");
        return bayResponse;
    }

    private Map<String, Object> close_StopLatch_SCT_NLT_Bay2() {
        StaNld_Bay2.logger.debug("S16_close_stop_latch_SCT_NLT_Bay2 : sctNltBay2_StopLatch_Status : Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<>();
        responseReturn.put("status", false);

        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.SCT_NLT2_PORT_NAME_STPR);

        String state = "";
        if (portInfo != null) {
            StaNld_Bay2.logger.debug("PortId    : " + portInfo.getPortId());
            StaNld_Bay2.logger.debug("ClusterId : " + portInfo.getClusterId());
            StaNld_Bay2.logger.debug("BayId     : " + portInfo.getBayId());

            String outputActive = Constant_IO_ActionMapping.ON;
            BayUtils bayUtils = new BayUtils();
            
            state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                                                     portInfo.getBayId(),
                                                     portInfo.getPortId(),
                                                     outputActive);

            StaNld_Bay2.logger.debug("S16_close_stop_latch_SCT_NLT_Bay2 : close_StopLatch_SCT_NLT_Bay2 : state : " + state);
           /* if (simulateSCTNLTBay2HappyPath) {
                state = Constant_IO_ActionMapping.OFF;
            }*/

            status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;
            PalletTrackerController palletTracker = new PalletTrackerController();
            palletTracker.switchBatchToNextBay(myBayKey, ConstantConveyor.UNLOADING_BAY_KEY);
            if(StateExecutorController.simulateSCTNLTBay2HappyPath){
            	status = true; 
            }
             

            StaNld_Bay2.logger.debug("S16_close_stop_latch_SCT_NLT_Bay2 : close_StopLatch_SCT_NLT_Bay2 : status : " + status);

        } else {
            StaNld_Bay2.logger.debug("S16_close_stop_latch_SCT_NLT_Bay2 : Output port not found");
            return responseReturn ;
        }

        responseReturn.put("status", status);
        responseReturn.put("responseData", state);

        StaNld_Bay2.logger.debug("S16_close_stop_latch_SCT_NLT_Bay2 : sctNltBay2_StopLatch_Status : status : " + status);
        StaNld_Bay2.logger.debug("S16_close_stop_latch_SCT_NLT_Bay2 : sctNltBay2_StopLatch_Status : Exit");
        return responseReturn;
    }
}

