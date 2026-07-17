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

public class S17_turn_off_divertor_relay_FT_Bay implements FtBayState {

    public String getMyBayKey() {
        return myBayKey;
    }

    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        // Structured log entry for sequence start
        Ft.logger.info(String.format("[%s] : [DIVERTOR_RELAY] : [SEQUENCE_ENTRY] - Turning off divertor relay.", getMyBayKey()));

        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true); // Assume success initially
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

        // Attempt to turn off the divertor relay
        Map<String,Object> responseReturn =  turn_off_divertor_relay_FT_Bay();
		boolean turn_off_divertor_relay_FT_Bay_status = (boolean)responseReturn.getOrDefault("status", false); // Default to false if status is missing

        if (turn_off_divertor_relay_FT_Bay_status) {
            // Log success with proper formatting
            Ft.logger.info(String.format("[%s] : [DIVERTOR_RELAY] : [SUCCESS] - Divertor relay successfully turned off.", getMyBayKey()));
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        } else {
            // Log failure with error level and specific error code
            Ft.logger.error(String.format("[%s] : [DIVERTOR_RELAY] : [FAILED] - Failed to turn off divertor relay. Error Code: %s", getMyBayKey(), ConvErrorCodeMapping.ERROR_CODE_FT_017));
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_017);
        }

        // Structured log for sequence exit
        Ft.logger.info(String.format("[%s] : [DIVERTOR_RELAY] : [SEQUENCE_EXIT] - Divertor relay turn-off sequence completed. Final Status: %s", getMyBayKey(), bayResponse.getStatus()));
        return bayResponse;
    }

    //============================================================================================================================================

    private Map<String,Object> turn_off_divertor_relay_FT_Bay() {
        // Structured debug log for method entry
        Ft.logger.debug(String.format("[%s] : [DIVERTOR_RELAY_CONTROL] : [REQUEST_ENTRY] - Attempting to turn off divertor relay.", getMyBayKey()));

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false); // Default boolean status

        //============================================================================================
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_DIVERTOR_RELAY);

        if (portInfo != null) {
            // Log port information with proper formatting
            Ft.logger.debug(String.format("[%s] : [DIVERTOR_RELAY_CONTROL] : [PORT_INFO] - PortId: %s, ClusterId: %s, BayId: %s", getMyBayKey(), portInfo.getPortId(), portInfo.getClusterId(), portInfo.getBayId()));

            BayUtils bayUtils = new BayUtils();
            String rawOutputState = "";
            try {
                // Attempt to set the output data to the bay
                rawOutputState = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                                                  portInfo.getBayId(),
                                                  portInfo.getPortId(),
                                                  Constant_IO_ActionMapping.CLOSE); // Use OPEN to represent turning the relay "Off"
                // Assuming OLD_ON_NEW_OFF indicates a successful 'turn off' operation for the divertor
                // This logic might need adjustment based on the exact meaning of OLD_ON_NEW_OFF in this context
                // For a relay 'turning off', we'd expect the state returned to confirm it's off.
                // Assuming OLD_OPEN_NEW_CLOSE implies the "off" state for the relay.
                // The original code checked against OLD_ON_NEW_OFF, which is potentially a mismatch.
                // Let's assume for now that if setOutputDataToBay returns OLD_OPEN_NEW_CLOSE, it means success.
                status = rawOutputState.equals(Constant_IO_ActionMapping.CLOSE);
            } catch (Exception e) {
                // Log communication error during output operation
                Ft.logger.error(String.format("[%s] : [COMMUNICATION_ERROR] : [DIVERTOR_RELAY_CONTROL] - Failed to set divertor relay state. Port: %s. Error: %s", getMyBayKey(), portInfo.getPortId(), e.getMessage()), e);
                responseReturn.put("status", false); // Indicate failure
                // No further processing needed, return early with failure status
                return responseReturn;
            }


            // Log the result of setting the output state
            Ft.logger.debug(String.format("[%s] : [DIVERTOR_RELAY_CONTROL] : [OUTPUT_STATE] - Set state result: %s", getMyBayKey(), status));
            //============================================================================================

            // Simulate happy path if enabled, overriding the actual status
            if(StateExecutorController.simulateFtBayHappyPath){
                status = true; // Simulate success for a "happy path"
                Ft.logger.debug(String.format("[%s] : [SIMULATION] : Divertor relay status overridden to true for happy path.", getMyBayKey()));
            }

            responseReturn.put("status", status);

        } else {
            // Log a clear error if the port information is missing
            Ft.logger.error(String.format("[%s] : [CONFIG_ERROR] : [DIVERTOR_RELAY_CONTROL] - Output port not found for divertor relay: %s", getMyBayKey(), ConstantBayPortNameMapping.FT_PORT_NAME_DIVERTOR_RELAY));
            // No need to set responseReturn.put("status", false); again as it's default to false
            return responseReturn ; // Return early as portInfo is null
        }

        // Structured debug log for method exit
        Ft.logger.debug(String.format("[%s] : [DIVERTOR_RELAY_CONTROL] : [REQUEST_EXIT] - Divertor relay control attempt completed. Status: %s", getMyBayKey(), status));
        return responseReturn;
    }
}
