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

public class S09_let_the_pallet_to_Unloading_Bay implements CommTestBayState {

    // ===========================================================================================
    @Override
    public BayResponse handleRequest() {
        Comm.logger.info("S09_let_the_pallet_to_Unloading_Bay : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

        // =============================================================
        Map<String, Object> responseReturn = open_StopLatch_CommBay();
        boolean open_StopLatch_CommBay = (boolean) responseReturn.get("status");

        boolean openSuccess = open_StopLatch_CommBay;
        if (openSuccess) {
            BayUtils.delay(500);
            responseReturn = close_StopLatch_CommBay();
            boolean close_StopLatch_CommBay = (boolean) responseReturn.get("status");
            boolean closeSuccess = close_StopLatch_CommBay;
            if (closeSuccess) {
                Comm.logger.info("S09_let_the_pallet_to_Unloading_Bay : Opening and Closing Stopper Success");
                bayResponse.setStatus(true);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
            } else {
                Comm.logger.info("S09_let_the_pallet_to_Unloading_Bay : Closing Stopper Failed");
                bayResponse.setStatus(false);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_COMM_009); // Assuming 602 for failure
            }
        } else {
            Comm.logger.info("S09_let_the_pallet_to_Unloading_Bay : Opening Stopper Failed");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_COMM_008);
        }
        // =============================================================

        Comm.logger.info("S09_let_the_pallet_to_Unloading_Bay : Exit");
        return bayResponse;
    }

    // ============================================================================================================================================

    private Map<String, Object> open_StopLatch_CommBay() {
        Comm.logger.debug("S09_let_the_pallet_to_Unloading_Bay : open_StopLatch_CommBay : Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<String, Object>();
        responseReturn.put("status", false);
        // ============================================================================================
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.COMM_PORT_NAME_STPR);

        if (portInfo != null) {
            Comm.logger.debug("PortId    : " + portInfo.getPortId());
            Comm.logger.debug("ClusterId : " + portInfo.getClusterId());
            Comm.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Comm.logger.debug("S09_let_the_pallet_to_Unloading_Bay : Stopper Output port not found");
            return responseReturn;
        }

        BayUtils bayUtils = new BayUtils();

        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                portInfo.getBayId(),
                portInfo.getPortId(),
                Constant_IO_ActionMapping.CLOSE);
        status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false;

        if (StateExecutorController.simulateCommBayHappyPath) {
            status = true;
        }

        Comm.logger.debug("S09_let_the_pallet_to_Unloading_Bay : open_StopLatch_CommBay : status : " + status);
        // ============================================================================================
        responseReturn.put("status", status);

        Comm.logger.debug("S09_let_the_pallet_to_Unloading_Bay : open_StopLatch_CommBay : Exit");
        return responseReturn;
    }

    // ============================================================================================================================================

    private Map<String, Object> close_StopLatch_CommBay() {
        Comm.logger.debug("S09_let_the_pallet_to_Unloading_Bay : close_StopLatch_CommBay : Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<String, Object>();
        responseReturn.put("status", false);

        // ============================================================================================
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.COMM_PORT_NAME_STPR);

        if (portInfo != null) {
            Comm.logger.debug("PortId    : " + portInfo.getPortId());
            Comm.logger.debug("ClusterId : " + portInfo.getClusterId());
            Comm.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Comm.logger.debug("S09_let_the_pallet_to_Unloading_Bay : Stopper Output port not found");
        }

        BayUtils bayUtils = new BayUtils();

        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                portInfo.getBayId(),
                portInfo.getPortId(),
                Constant_IO_ActionMapping.OPEN);
        status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;
        Comm.logger.debug("S09_let_the_pallet_to_Unloading_Bay : close_StopLatch_CommBay : status : " + status);
        // ============================================================================================

        responseReturn.put("status", status);

        Comm.logger.debug("S09_let_the_pallet_to_Unloading_Bay : close_StopLatch_CommBay : Exit");
        return responseReturn;
    }

    // ============================================================================================================================================
}
