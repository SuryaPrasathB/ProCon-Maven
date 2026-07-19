package com.tasnetwork.calibration.conveyor.bay.verific;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S05_verification_Test implements VerificTestBayState {

    // ===========================================================================================
    @Override
    public BayResponse handleRequest() {

        Verification.logger.info("S05_verification_Test : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

        Map<String, Object> responseReturn = verificationTest();
        boolean verificationTest = (boolean) responseReturn.get("status");

        boolean status = verificationTest; // Call the function to perform verification test

        if (status) {
            Verification.logger.info("S05_verification_Test : Verification Test Successful");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601); // Success error code
        } else {
            Verification.logger.info("S05_verification_Test : Verification Test Failed");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_010); // Failure error code
        }

        // setVerificationTestCompleted(true);

        Verification.logger.info("S05_verification_Test : Exit");
        return bayResponse;
    }

    // ============================================================================================================================================

    private Map<String, Object> verificationTest() {
        Verification.logger.debug("S05_verification_Test : Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<String, Object>();
        responseReturn.put("status", false);

        // Add logic for the verification test here
        // Set 'status' to true if the test is successful, otherwise false

        // 1. Start Source
        // 2. Run Test Cases
        // 3. Start Source

        if (StateExecutorController.simulateVerificBayHappyPath) {
            status = true;
        }

        Verification.logger.debug("S05_verification_Test : status : " + status);

        responseReturn.put("status", status);

        Verification.logger.debug("S05_verification_Test : Exit");
        return responseReturn;
    }

}
