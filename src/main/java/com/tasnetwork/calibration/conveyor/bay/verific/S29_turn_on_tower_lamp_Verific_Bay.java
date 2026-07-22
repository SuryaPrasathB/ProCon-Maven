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

public class S29_turn_on_tower_lamp_Verific_Bay implements VerificTestBayState {

    // ===========================================================================================
    @Override
    public BayResponse handleRequest() {
        Verification.logger.info("S29_turn_on_tower_lamp_Verific_Bay : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

        Map<String, Object> responseReturn = turn_on_tower_lamp();
        boolean turn_on_red_lamp = (boolean) responseReturn.get("status");

        if (turn_on_red_lamp) {
            Verification.logger.info("S29_turn_on_tower_lamp_Verific_Bay : turn_on_tower_lamp : Success");

            // BayUtils.delay(500);

            responseReturn = turn_off_tower_lamp();
            boolean turn_off_green_lamp = (boolean) responseReturn.get("status");

            if (turn_off_green_lamp) {
                Verification.logger.info("S29_turn_on_tower_lamp_Verific_Bay : turn_off_tower_lamp : Success");
                bayResponse.setStatus(true);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
            } else {
                Verification.logger.info("S29_turn_on_tower_lamp_Verific_Bay : Failed to turn_off_tower_lamp GREEN");
                bayResponse.setStatus(false);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_HVT_014);
            }
        } else {
            Verification.logger.info("S29_turn_on_tower_lamp_Verific_Bay : Failed to turn_on_tower_lamp RED");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_HVT_014);
        }

        // BayUtils.delay(1000);

        Verification.logger.info("S29_turn_on_tower_lamp_Verific_Bay : Exit");
        return bayResponse;
    }

    // ============================================================================================================================================

    private Map<String, Object> turn_on_tower_lamp() {
        Verification.logger.debug("S29_turn_on_tower_lamp_Verific_Bay : turn_on_tower_lamp : Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<String, Object>();
        responseReturn.put("status", false);

        // ============================================================================================
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.VERIFIC_PORT_NAME_TWR_LAMP2);

        if (portInfo != null) {
            Verification.logger.debug("PortId    : " + portInfo.getPortId());
            Verification.logger.debug("ClusterId : " + portInfo.getClusterId());
            Verification.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Verification.logger.debug("S29_turn_on_tower_lamp_Verific_Bay : Output port not found");
            return responseReturn;
        }

        BayUtils bayUtils = new BayUtils();

        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                portInfo.getBayId(),
                portInfo.getPortId(),
                Constant_IO_ActionMapping.OPEN);

        // Use CLOSE to represent turning the relay "On"

        status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

        Verification.logger.debug("S29_turn_on_tower_lamp_Verific_Bay : turn_on_tower_lamp : status : " + status);
        // ============================================================================================

        if (StateExecutorController.simulateFtBayHappyPath) {
            status = true;
        }

        responseReturn.put("status", status);

        Verification.logger.debug("S29_turn_on_tower_lamp_Verific_Bay : Exit");
        return responseReturn;
    }

    // ============================================================================================================================================

    private Map<String, Object> turn_off_tower_lamp() {
        Verification.logger.debug("S29_turn_on_tower_lamp_Verific_Bay : turn_off_tower_lamp : Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<String, Object>();
        responseReturn.put("status", false);

        // ============================================================================================
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.VERIFIC_PORT_NAME_TWR_LAMP1);

        if (portInfo != null) {
            Verification.logger.debug("PortId    : " + portInfo.getPortId());
            Verification.logger.debug("ClusterId : " + portInfo.getClusterId());
            Verification.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Verification.logger.debug("S29_turn_on_tower_lamp_Verific_Bay : Output port not found");
            return responseReturn;
        }

        BayUtils bayUtils = new BayUtils();

        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                portInfo.getBayId(),
                portInfo.getPortId(),
                Constant_IO_ActionMapping.CLOSE); // Use CLOSE to represent turning the relay "On"

        status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false;

        Verification.logger.debug("S29_turn_on_tower_lamp_Verific_Bay : turn_off_tower_lamp : status : " + status);
        // ============================================================================================

        if (StateExecutorController.simulateFtBayHappyPath) {
            status = true;
        }

        responseReturn.put("status", status);

        Verification.logger.debug("S29_turn_on_tower_lamp_Verific_Bay : Exit");
        return responseReturn;
    }
}
