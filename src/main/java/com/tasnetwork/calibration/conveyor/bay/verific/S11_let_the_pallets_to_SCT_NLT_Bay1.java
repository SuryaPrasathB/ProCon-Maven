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

public class S11_let_the_pallets_to_SCT_NLT_Bay1 implements VerificTestBayState {

    // ===========================================================================================
    @Override
    public BayResponse handleRequest() {
        Verification.logger.info("S11_let_the_pallets_to_SCT_NLT_Bay1 : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

        // =============================================================

        Map<String, Object> responseReturn = open_StopLatch_VerificBay();
        boolean open_StopLatch_VerificBay = (boolean) responseReturn.get("status");

        boolean openSuccess = open_StopLatch_VerificBay;
        if (openSuccess) {
            BayUtils.delay(500);
            responseReturn = close_StopLatch_VerificBay();
            boolean close_StopLatch_VerificBay = (boolean) responseReturn.get("status");

            boolean closeSuccess = close_StopLatch_VerificBay;
            if (closeSuccess) {
                Verification.logger.info("S11_let_the_pallets_to_SCT_NLT_Bay1 : Opening and Closing Stopper Success");
                bayResponse.setStatus(true);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
            } else {
                Verification.logger.info("S11_let_the_pallets_to_SCT_NLT_Bay1 : Closing Stopper Failed");
                bayResponse.setStatus(false);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_009); // Assuming 602 for failure
            }
        } else {
            Verification.logger.info("S11_let_the_pallets_to_SCT_NLT_Bay1 : Opening Stopper Failed");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_008);
        }
        // =============================================================

        Verification.logger.info("S11_let_the_pallets_to_SCT_NLT_Bay1 : Exit");
        return bayResponse;
    }

    // ============================================================================================================================================

    private Map<String, Object> open_StopLatch_VerificBay() {
        Verification.logger.debug("S11_let_the_pallets_to_SCT_NLT_Bay1 : open_StopLatch_VerificBay : Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<String, Object>();
        responseReturn.put("status", false);

        // ============================================================================================
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.VERIFIC_PORT_NAME_STPR);

        if (portInfo != null) {
            Verification.logger.debug("PortId    : " + portInfo.getPortId());
            Verification.logger.debug("ClusterId : " + portInfo.getClusterId());
            Verification.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Verification.logger.debug("S11_let_the_pallets_to_SCT_NLT_Bay1 : Stopper Output port not found");
            return responseReturn;
        }

        BayUtils bayUtils = new BayUtils();

        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                portInfo.getBayId(),
                portInfo.getPortId(),
                Constant_IO_ActionMapping.CLOSE);

        status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false;

        if (StateExecutorController.simulateVerificBayHappyPath) {
            status = true;
        }

        Verification.logger
                .debug("S11_let_the_pallets_to_SCT_NLT_Bay1 : open_StopLatch_VerificBay : status : " + status);
        // ============================================================================================

        responseReturn.put("status", status);

        Verification.logger.debug("S11_let_the_pallets_to_SCT_NLT_Bay1 : open_StopLatch_VerificBay : Exit");
        return responseReturn;
    }

    // ============================================================================================================================================

    private Map<String, Object> close_StopLatch_VerificBay() {
        Verification.logger.debug("S11_let_the_pallets_to_SCT_NLT_Bay1 : close_StopLatch_VerificBay : Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<String, Object>();
        responseReturn.put("status", false);

        // ============================================================================================
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.VERIFIC_PORT_NAME_STPR);

        if (portInfo != null) {
            Verification.logger.debug("PortId    : " + portInfo.getPortId());
            Verification.logger.debug("ClusterId : " + portInfo.getClusterId());
            Verification.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Verification.logger.debug("S11_let_the_pallets_to_SCT_NLT_Bay1 : Stopper Output port not found");
            return responseReturn;
        }

        BayUtils bayUtils = new BayUtils();

        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                portInfo.getBayId(),
                portInfo.getPortId(),
                Constant_IO_ActionMapping.OPEN);
        status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

        if (StateExecutorController.simulateVerificBayHappyPath) {
            status = true;
        }

        Verification.logger
                .debug("S11_let_the_pallets_to_SCT_NLT_Bay1 : close_StopLatch_VerificBay : status : " + status);
        // ============================================================================================

        responseReturn.put("status", status);

        Verification.logger.debug("S11_let_the_pallets_to_SCT_NLT_Bay1 : close_StopLatch_VerificBay : Exit");
        return responseReturn;
    }

    // ============================================================================================================================================
}
