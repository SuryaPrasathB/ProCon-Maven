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

public class S28_turn_off_motor_Verific_Bay implements VerificTestBayState {

    // ===========================================================================================
    @Override
    public BayResponse handleRequest() {
        Verification.logger.info("S28_turn_off_motor_Verific_Bay : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

        Map<String, Object> responseReturn = turn_off();
        boolean turn_off = (boolean) responseReturn.get("status");

        if (turn_off) {
            Verification.logger.info("S28_turn_off_motor_Verific_Bay : Motor Turned Off");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        } else {
            Verification.logger.info("S28_turn_off_motor_Verific_Bay : Failed to Turn Off Motor");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_017);
        }

        Verification.logger.info("S28_turn_off_motor_Verific_Bay : Exit");
        return bayResponse;
    }

    // ============================================================================================================================================

    private Map<String, Object> turn_off() {
        Verification.logger.debug("S28_turn_off_motor_Verific_Bay : turn_off : Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<String, Object>();
        responseReturn.put("status", false);

        // ============================================================================================
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.VERIFIC_PORT_NAME_MOTOR_CTRL);

        if (portInfo != null) {
            Verification.logger.debug("PortId    : " + portInfo.getPortId());
            Verification.logger.debug("ClusterId : " + portInfo.getClusterId());
            Verification.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Verification.logger.debug("S28_turn_off_motor_Verific_Bay : Output port not found");
            return responseReturn;
        }

        BayUtils bayUtils = new BayUtils();

        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                portInfo.getBayId(),
                portInfo.getPortId(),
                Constant_IO_ActionMapping.CLOSE); // Use OPEN to represent turning the relay "Off"

        status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false;
        Verification.logger.debug("S28_turn_off_motor_Verific_Bay : turn_off : status : " + status);
        // ============================================================================================

        if (StateExecutorController.simulateSCTNLTBay1HappyPath) {
            status = true;
        }

        responseReturn.put("status", status);

        return responseReturn;
    }
}
