package com.tasnetwork.calibration.conveyor.bay.verific;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S14_ensure_divertor_relay_turned_on_Verific_Bay implements VerificTestBayState {

    // ===========================================================================================
    @Override
    public BayResponse handleRequest() {
        Verification.logger.info("S14_ensure_divertor_relay_turned_on_Verific_Bay : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

        int try_count = 0;

        while (try_count <= 3 && !Verification.isStopProcessRequestedVerificBay() && !ConstantConveyor.ALL_LOOP_BREAK_FLAG) {

            Map<String, Object> responseReturn = divertorRelay_Status();
            String divertorRelay_Status = (String) responseReturn.get("status");

            if (divertorRelay_Status.equals(Constant_IO_ActionMapping.OFF)) {
                bayResponse.setStatus(true);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
                break;
            } else {
                bayResponse.setStatus(false);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_016);
                BayUtils.delay(1000);
                try_count++;
            }
        }

        Verification.logger.info("S14_ensure_divertor_relay_turned_on_Verific_Bay : Exit");
        return bayResponse;
    }
    // ============================================================================================================================================

    private Map<String, Object> divertorRelay_Status() {
        Verification.logger.debug("S14_ensure_divertor_relay_turned_on_Verific_Bay : divertorRelay_Status : Entry");

        Map<String, Object> responseReturn = new HashMap<String, Object>();
        responseReturn.put("status", false);

        IoPortInfo portInfo = BayUtils
                .getOutputPortDetails(ConstantBayPortNameMapping.VERIFIC_PORT_NAME_DIVERTOR_RELAY);

        TestInterfaceStatus testInterfaceStatus = null;
        if (portInfo != null) {
            testInterfaceStatus = new TestInterfaceStatus(
                    ConstantConveyor.VERIFICATION_BAY_KEY,
                    "-",
                    ConstantConveyor.DEVICE_TYPE_CLUSTER_OUTPUT,
                    "-",
                    "-",
                    portInfo.getPortId(),
                    ConstantBayPortNameMapping.VERIFIC_PORT_NAME_DIVERTOR_RELAY,
                    ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
                    "Executing",
                    ConstantConveyor.COMM_EXECUTION_STATUS_INP);
            StateExecutorController.addToTestStatusGui(testInterfaceStatus);

            Verification.logger.debug("PortId    : " + portInfo.getPortId());
            Verification.logger.debug("ClusterId : " + portInfo.getClusterId());
            Verification.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Verification.logger.debug("S14_ensure_divertor_relay_turned_on_Verific_Bay : Output port not found");
            return responseReturn;
        }

        BayUtils bayUtils = new BayUtils();

        String state = bayUtils.getInputDataFromBayV2(portInfo);

        state = state.equals(Constant_IO_ActionMapping.ON) ? Constant_IO_ActionMapping.ON
                : Constant_IO_ActionMapping.OFF;

        if (StateExecutorController.simulateVerificBayHappyPath) {
            state = Constant_IO_ActionMapping.ON;
        }

        Verification.logger
                .debug("S14_ensure_divertor_relay_turned_on_Verific_Bay : divertorRelay_Status : state : " + state);

        responseReturn.put("status", state);

        Verification.logger.debug("S14_ensure_divertor_relay_turned_on_Verific_Bay : divertorRelay_Status : Exit");
        return responseReturn;
    }
}
