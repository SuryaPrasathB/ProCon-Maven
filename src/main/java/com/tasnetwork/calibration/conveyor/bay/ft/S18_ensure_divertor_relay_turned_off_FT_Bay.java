package com.tasnetwork.calibration.conveyor.bay.ft;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S18_ensure_divertor_relay_turned_off_FT_Bay implements FtBayState {

    public String getMyBayKey() {
        return myBayKey;
    }

    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        // Structured log entry for sequence start
        Ft.logger.info(String.format("[%s] : [DIVERTOR_RELAY_CHECK] : [SEQUENCE_ENTRY] - Ensuring divertor relay is off.", getMyBayKey()));

        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(false); // Assume failure initially
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_100);

        int try_count = 0;
        final int MAX_RETRY_COUNT = 3;

        // Loop to retry checking the divertor relay status
        Ft.logger.debug(String.format("[%s] : [DIVERTOR_RELAY_CHECK] : [RETRY_LOOP_START] - Starting retry loop to confirm relay is off (Max retries: %d).", getMyBayKey(), MAX_RETRY_COUNT));
        while (try_count <= MAX_RETRY_COUNT) {
    		Map<String,Object> responseReturn =  ftBay_DivertorRelay_Status();
    		// Ensure safe retrieval from map, defaulting to an unexpected state string
    		String ftBay_DivertorRelay_CurrentStatus = (String)responseReturn.getOrDefault("status", "UNEXPECTED_STATUS");

            Ft.logger.debug(String.format("[%s] : [DIVERTOR_RELAY_CHECK] : [ATTEMPT_%d] - Current relay status: %s", getMyBayKey(), (try_count + 1), ftBay_DivertorRelay_CurrentStatus));

            if (ftBay_DivertorRelay_CurrentStatus.equals(Constant_IO_ActionMapping.CLOSE)) {  // Check if relay is turned off
                Ft.logger.info(String.format("[%s] : [DIVERTOR_RELAY_CHECK] : [SUCCESS] - Divertor relay confirmed to be off after %d attempts.", getMyBayKey(), (try_count + 1)));
                bayResponse.setStatus(true);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
                break; // Exit loop on success
            } else {
                Ft.logger.warn(String.format("[%s] : [DIVERTOR_RELAY_CHECK] : [NOT_OFF] - Divertor relay is still ON. Attempt %d of %d. Retrying...", getMyBayKey(), (try_count + 1), MAX_RETRY_COUNT));
                bayResponse.setStatus(false);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_020);
                BayUtils.delay(1000); // Wait for 1 second before retrying
                try_count++;
            }
        }

        // Check if the loop completed without success
        if (!bayResponse.getStatus()) {
            Ft.logger.error(String.format("[%s] : [DIVERTOR_RELAY_CHECK] : [FAILED_AFTER_RETRIES] - Failed to confirm divertor relay is off after %d attempts. Final Error Code: %s", getMyBayKey(), MAX_RETRY_COUNT, ConvErrorCodeMapping.ERROR_CODE_FT_020));
        }

        // Structured log for sequence exit
        Ft.logger.info(String.format("[%s] : [DIVERTOR_RELAY_CHECK] : [SEQUENCE_EXIT] - Divertor relay check sequence completed. Final Status: %s", getMyBayKey(), bayResponse.getStatus()));
        return bayResponse;
    }

    //============================================================================================================================================

    private Map<String, Object> ftBay_DivertorRelay_Status() {
        // Structured debug log for method entry
        Ft.logger.debug(String.format("[%s] : [DIVERTOR_RELAY_READ] : [REQUEST_ENTRY] - Reading divertor relay status.", getMyBayKey()));

        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", "ERROR_READING_STATUS"); // Default string status for clarity in case of failure

        //============================================================================================
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_DIVERTOR_RELAY);

        if (portInfo != null) {
            // Log port information with proper formatting
            Ft.logger.debug(String.format("[%s] : [DIVERTOR_RELAY_READ] : [PORT_INFO] - PortId: %s, ClusterId: %s, BayId: %s", getMyBayKey(), portInfo.getPortId(), portInfo.getClusterId(), portInfo.getBayId()));

            BayUtils bayUtils = new BayUtils();
            String rawStateFromSensor = "";
            try {
                // Read the actual state from the bay's output port
                rawStateFromSensor = bayUtils.getInputDataFromBayV2(portInfo);
                Ft.logger.debug(String.format("[%s] : [DIVERTOR_RELAY_READ] : [RAW_STATE] : %s", getMyBayKey(), rawStateFromSensor));

                // Interpret the raw state based on Constant_IO_ActionMapping
                // Assuming OLD_ON_NEW_OFF indicates "ON" (relay active/closed)
                // And OLD_OPEN_NEW_CLOSE indicates "OFF" (relay inactive/open)
                String interpretedState = rawStateFromSensor.equals(Constant_IO_ActionMapping.OFF) ?
                                          Constant_IO_ActionMapping.OPEN : // If sensor is ON, assume relay is CLOSED/active
                                          Constant_IO_ActionMapping.CLOSE;  // If sensor is OFF, assume relay is OPEN/inactive

                Ft.logger.debug(String.format("[%s] : [DIVERTOR_RELAY_READ] : [INTERPRETED_STATE] : %s", getMyBayKey(), interpretedState));
                responseReturn.put("status", interpretedState);

            } catch (Exception e) {
                // Log communication error during input operation
                Ft.logger.error(String.format("[%s] : [COMMUNICATION_ERROR] : [DIVERTOR_RELAY_READ] - Failed to read divertor relay state. Port: %s. Error: %s", getMyBayKey(), portInfo.getPortId(), e.getMessage()), e);
                // The default status "ERROR_READING_STATUS" will remain, indicating failure
            }

        } else {
            // Log a clear error if the port information is missing
            Ft.logger.error(String.format("[%s] : [CONFIG_ERROR] : [DIVERTOR_RELAY_READ] - Output port not found for divertor relay: %s", getMyBayKey(), ConstantBayPortNameMapping.FT_PORT_NAME_DIVERTOR_RELAY));
            // The default status "ERROR_READING_STATUS" will remain, indicating configuration failure
        }

        // Simulate happy path if enabled, overriding the actual status
        if(StateExecutorController.simulateFtBayHappyPath){
        	responseReturn.put("status", Constant_IO_ActionMapping.CLOSE); // Simulate "OFF" state for happy path
            Ft.logger.debug(String.format("[%s] : [SIMULATION] : Divertor relay status overridden to OFF for happy path.", getMyBayKey()));
        }

        // Structured debug log for method exit
        Ft.logger.debug(String.format("[%s] : [DIVERTOR_RELAY_READ] : [REQUEST_EXIT] - Divertor relay status read completed. Status: %s", getMyBayKey(), responseReturn.get("status")));
        return responseReturn;
    }
}
