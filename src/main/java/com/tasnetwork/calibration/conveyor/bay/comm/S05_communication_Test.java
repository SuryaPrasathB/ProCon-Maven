package com.tasnetwork.calibration.conveyor.bay.comm;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S05_communication_Test implements CommTestBayState {

    // ===========================================================================================
    @Override
    public BayResponse handleRequest() {
        Comm.logger.info("S05_communication_Test : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

        Map<String, Object> responseReturn = communicationTest();
        boolean communicationTest = (boolean) responseReturn.get("status");

        boolean status = communicationTest; // Call the function to perform communication test

        if (status) {
            Comm.logger.info("S05_communication_Test : Communication Test Successful");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601); // Success error code
        } else {
            Comm.logger.info("S05_communication_Test : Communication Test Failed");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_COMM_010); // Failure error code
        }

        Comm.logger.info("S05_communication_Test : Exit");
        return bayResponse;
    }

    // ============================================================================================================================================

    private Map<String, Object> communicationTest() {
        Comm.logger.debug("S05_communication_Test : Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<String, Object>();
        responseReturn.put("status", false);

        // Add logic for the communication test here
        // Set 'status' to true if the test is successful, otherwise false

        if (StateExecutorController.simulateCommBayHappyPath) {
            status = true;
        }

        Comm.logger.debug("S05_communication_Test : status : " + status);

        responseReturn.put("status", status);

        Comm.logger.debug("S05_communication_Test : Exit");
        return responseReturn;
    }
}
