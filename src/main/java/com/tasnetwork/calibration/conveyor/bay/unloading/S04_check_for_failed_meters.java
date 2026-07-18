package com.tasnetwork.calibration.conveyor.bay.unloading;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S04_check_for_failed_meters implements UnloadingBayState {

    // ===========================================================================================
    @Override
    public BayResponse handleRequest() {
        Unloading.logger.info("S04_check_for_failed_meters : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

        Map<String, Object> responseReturn = check_for_failed_meters();
        boolean check_for_failed_meters = (boolean) responseReturn.get("status");

        boolean status = check_for_failed_meters;

        if (status) {
            Unloading.logger.info("S04_check_for_failed_meters :  ");
            // Logic for success case (status is true)
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601); // Success error code
        } else {
            Unloading.logger.info("S04_check_for_failed_meters : ");
            // Logic for failure case (status is false)
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_UNLOADING_010); // Failure error code
        }

        Unloading.logger.info("S04_check_for_failed_meters : Exit");
        return bayResponse;
    }

    // ============================================================================================================================================

    private Map<String, Object> check_for_failed_meters() {

        Unloading.logger.debug("S04_check_for_failed_meters : check_for_failed_meters : Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<String, Object>();
        responseReturn.put("status", false);

        if (StateExecutorController.simulateUnloadingBayHappyPath) {
            status = true;
        }

        Unloading.logger.debug("S04_check_for_failed_meters : check_for_failed_meters : status : " + status);

        responseReturn.put("status", status);

        Unloading.logger.debug("S04_check_for_failed_meters : check_for_failed_meters : Exit");
        return responseReturn;
    }
}
