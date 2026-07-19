package com.tasnetwork.calibration.conveyor.bay.calib;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S29_turn_on_tower_lamp_CALIB_Bay implements CalibrationBayState {

    @Override
    public BayResponse handleRequest() {
        Calib.logger.info("S29_turn_on_tower_lamp_CALIB_Bay : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

        // Turn ON the tower lamp
        Map<String, Object> responseReturn = turn_on_tower_lamp();
        boolean turn_on_lamp = (boolean) responseReturn.get("status");

        if (turn_on_lamp) {
            Calib.logger.info("S29_turn_on_tower_lamp_CALIB_Bay : RED Tower Lamp Turned On");

            // Delay for 500ms
            // BayUtils.delay(500);

            // Turn OFF the tower lamp
            responseReturn = turn_off_tower_lamp();
            boolean turn_off_lamp = (boolean) responseReturn.get("status");

            if (turn_off_lamp) {
                Calib.logger.info("S29_turn_on_tower_lamp_CALIB_Bay : GREEN Tower Lamp Turned Off");
                bayResponse.setStatus(true);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
            } else {
                Calib.logger.info("S29_turn_on_tower_lamp_CALIB_Bay : Failed to Turn Off GREEN Tower Lamp");
                bayResponse.setStatus(false);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_017);
            }
        } else {
            Calib.logger.info("S29_turn_on_tower_lamp_CALIB_Bay : Failed to Turn On RED Tower Lamp");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_017);
        }

        Calib.logger.info("S29_turn_on_tower_lamp_CALIB_Bay : Exit");
        return bayResponse;
    }

    // =====================================================================
    // Function to turn ON the tower lamp
    // =====================================================================
    private Map<String, Object> turn_on_tower_lamp() {
        Calib.logger.debug("S29_turn_on_tower_lamp_CALIB_Bay : turn_on_tower_lamp : Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<>();
        responseReturn.put("status", false);

        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.CALIB_PORT_NAME_TWR_LAMP2);

        if (portInfo != null) {
            BayUtils bayUtils = new BayUtils();
            String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                    portInfo.getBayId(),
                    portInfo.getPortId(),
                    Constant_IO_ActionMapping.OPEN);
            status = state.equals(Constant_IO_ActionMapping.ON);
        }

        responseReturn.put("status", status);
        return responseReturn;
    }

    // =====================================================================
    // Function to turn OFF the tower lamp
    // =====================================================================
    private Map<String, Object> turn_off_tower_lamp() {
        Calib.logger.debug("S29_turn_on_tower_lamp_CALIB_Bay : turn_off_tower_lamp : Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<>();
        responseReturn.put("status", false);

        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.CALIB_PORT_NAME_TWR_LAMP1);

        if (portInfo != null) {
            BayUtils bayUtils = new BayUtils();
            String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                    portInfo.getBayId(),
                    portInfo.getPortId(),
                    Constant_IO_ActionMapping.CLOSE);
            status = state.equals(Constant_IO_ActionMapping.OFF);
        }

        responseReturn.put("status", status);
        return responseReturn;
    }
}
