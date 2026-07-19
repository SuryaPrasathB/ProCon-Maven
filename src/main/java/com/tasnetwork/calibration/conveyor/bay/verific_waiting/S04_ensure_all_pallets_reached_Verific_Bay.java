package com.tasnetwork.calibration.conveyor.bay.verific_waiting;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
//import com.tasnetwork.calibration.conveyor.bay_functionaltest.FunctionalTestBay;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S04_ensure_all_pallets_reached_Verific_Bay implements WaitingBayState {

    // ===========================================================================================
    @Override
    public BayResponse handleRequest() {
        VerificWaiting.logger.info("S04_ensure_pallet_reached_Verfic_Bay : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_501);

        // ======================================================================
        Map<String, Object> responseReturn;
        boolean isPalletAvailableAtVerificBay;
        long startTime;
        boolean stableDetection = false;

        /*
         * // Initial delay before checking
         * BayUtils.delay(3000);
         */

        while (!stableDetection && !ConstantConveyor.ALL_LOOP_BREAK_FLAG) {
            responseReturn = IsPalletAvailableAtVerificBay();
            isPalletAvailableAtVerificBay = (boolean) responseReturn.get("status");

            if (isPalletAvailableAtVerificBay) {
                startTime = System.currentTimeMillis();

                while (System.currentTimeMillis() - startTime < 3000) {
                    BayUtils.delay(1000); // Small delay to prevent CPU overuse
                    responseReturn = IsPalletAvailableAtVerificBay();
                    isPalletAvailableAtVerificBay = (boolean) responseReturn.get("status");

                    if (!isPalletAvailableAtVerificBay) {
                        break; // Reset if detection is lost
                    }
                }

                // If detection lasted for 2 seconds, confirm stability
                if (isPalletAvailableAtVerificBay) {
                    stableDetection = true;
                }
            } else {
                VerificWaiting.logger
                        .info("S04_ensure_all_pallets_reached_Verific_Bay : Not All pallets Reached Verific Bay");
                BayUtils.delay(1000);
            }
        }

        if (stableDetection) {
            VerificWaiting.logger.info("S04_ensure_all_pallets_reached_Verific_Bay : All Pallets Reached Verific Bay");

            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

        } else {
            VerificWaiting.logger.info("S04_ensure_all_pallets_reached_Verific_Bay : Pallets Not Reached Verific Bay");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_WAITING_001);
        }

        // ======================================================================

        VerificWaiting.logger.info("S04_ensure_all_pallets_reached_Verific_Bay : Exit");
        return bayResponse;
    }
    // ============================================================================================================================================

    private Map<String, Object> IsPalletAvailableAtVerificBay() {
        VerificWaiting.logger.debug("S04_ensure_pallet_reached_Verfic_Bay : IsPalletAvailableAtVerificBay : Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<String, Object>();
        responseReturn.put("status", false);

        IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.VERIFIC_PORT_NAME_SNSR_PALLET);

        if (portInfo != null) {
            VerificWaiting.logger.debug("PortId    : " + portInfo.getPortId());
            VerificWaiting.logger.debug("ClusterId : " + portInfo.getClusterId());
            VerificWaiting.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            VerificWaiting.logger.debug(
                    "S04_ensure_pallet_reached_Verfic_Bay : IsPalletAvailableAtVerificBay : Output port not found");
            return responseReturn;
        }

        BayUtils bayUtils = new BayUtils();

        /*
         * String state = bayUtils.getInputDataFromBay(portInfo.getClusterId(),
         * portInfo.getBayId(),
         * portInfo.getPortId());
         */

        String state = bayUtils.getInputDataFromBayV2(portInfo);

        status = state.equals(Constant_IO_ActionMapping.ON) ? true : false; // Constant_IO_ActionMapping.DETECTED :
                                                                            // Constant_IO_ActionMapping.NOT_DETECTED;

        if (StateExecutorController.simulateWaitingBayHappyPath) {
            state = Constant_IO_ActionMapping.DETECTED;
            status = true;
        }

        VerificWaiting.logger
                .debug("S04_ensure_pallet_reached_Verfic_Bay : IsPalletAvailableAtVerificBay : state : " + state);

        responseReturn.put("status", status);

        VerificWaiting.logger.debug("S04_ensure_pallet_reached_Verfic_Bay : IsPalletAvailableAtVerificBay : Exit");
        return responseReturn;
    }
}
