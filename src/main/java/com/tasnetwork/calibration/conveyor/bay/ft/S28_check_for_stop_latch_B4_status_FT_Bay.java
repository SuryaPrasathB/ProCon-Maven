package com.tasnetwork.calibration.conveyor.bay.ft;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController; // Keep if actually used, otherwise remove
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S28_check_for_stop_latch_B4_status_FT_Bay implements FtBayState {

	public String getMyBayKey() {
		return myBayKey;
	}

    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        // Structured log entry for sequence start
        Ft.logger.info(String.format("[%s] : [STOP_LATCH_B4_STATUS_CHECK] : [SEQUENCE_ENTRY] - Checking status of Stop Latch B4 at FT Bay.", getMyBayKey()));

        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true); // Assume success initially
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

        Ft.logger.debug(String.format("[%s] : [STOP_LATCH_B4_STATUS_CHECK] : [DELAY] - Delaying for 2000 ms before checking latch status.", getMyBayKey()));
        BayUtils.delay(2000);

        Map<String,Object> responseReturn =  stop_latch_B4_status_FT_Bay();
        // Get status safely, default to an error string if not found
        String stop_latch_B4_status = (String)responseReturn.getOrDefault("status", "ERROR_STATUS_RETRIEVAL");

        Ft.logger.debug(String.format("[%s] : [STOP_LATCH_B4_STATUS_CHECK] : [READ_STATUS] - Stop Latch B4 reported status: %s", getMyBayKey(), stop_latch_B4_status));

        if (stop_latch_B4_status.equals("STOPPER_OPENED")){
            Ft.stopper_B4_FT_Bay_Status = "STOPPER_B4_FT_BAY_OPENED" ;
            Ft.logger.info(String.format("[%s] : [STOP_LATCH_B4_STATUS_CHECK] : [STATUS_CONFIRMED] - Stop Latch B4 confirmed OPEN. Global status set to: %s", getMyBayKey(), Ft.stopper_B4_FT_Bay_Status));
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
            ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayEntryStopper(getMyBayKey(),true);
        }
        else if (stop_latch_B4_status.equals("STOPPER_CLOSED")){
            Ft.stopper_B4_FT_Bay_Status = "STOPPER_B4_FT_BAY_CLOSED" ;
            Ft.logger.warn(String.format("[%s] : [STOP_LATCH_B4_STATUS_CHECK] : [STATUS_MISMATCH] - Stop Latch B4 reported CLOSED. Expected OPEN. Global status set to: %s. Error Code: %s", getMyBayKey(), Ft.stopper_B4_FT_Bay_Status, ConvErrorCodeMapping.ERROR_CODE_FT_034));
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_034);
            ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayEntryStopper(getMyBayKey(),false);
        }
        else{
            Ft.logger.error(String.format("[%s] : [STOP_LATCH_B4_STATUS_CHECK] : [READ_FAILED] - Failed to read Stop Latch B4 status or unexpected status received: %s. Error Code: %s", getMyBayKey(), stop_latch_B4_status, ConvErrorCodeMapping.ERROR_CODE_FT_035));
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_035);
        }

        // Structured log for sequence exit
        Ft.logger.info(String.format("[%s] : [STOP_LATCH_B4_STATUS_CHECK] : [SEQUENCE_EXIT] - Stop Latch B4 status check sequence completed. Final Status: %s, Error Code: %s", getMyBayKey(), bayResponse.getStatus(), bayResponse.getErrorCode()));
        return bayResponse;
    }
    //============================================================================================================================================

    private Map<String,Object> stop_latch_B4_status_FT_Bay() {
        // Structured debug log for method entry
        Ft.logger.debug(String.format("[%s] : [STOP_LATCH_B4_SENSOR_READ] : [REQUEST_ENTRY] - Reading Stop Latch B4 sensor status at FT Bay.", getMyBayKey()));

        Map<String,Object> responseReturn = new HashMap<String,Object>();
        responseReturn.put("status", "ERROR_READING_SENSOR"); // Default string status for clarity in case of failure

        //============================================================================================
        // Corrected: Stop latch status is typically read from an INPUT port (sensor), not an Output port.
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_STPR_B4);

        String interpretedState = "UNKNOWN"; // Default interpreted state

        if (portInfo != null) {
            // Log port information with proper formatting
            Ft.logger.debug(String.format("[%s] : [STOP_LATCH_B4_SENSOR_READ] : [PORT_INFO] - PortId: %s, ClusterId: %s, BayId: %s", getMyBayKey(), portInfo.getPortId(), portInfo.getClusterId(), portInfo.getBayId()));

            BayUtils bayUtils = new BayUtils();
            String rawStateFromSensor = "";
            try {
                rawStateFromSensor = bayUtils.getInputDataFromBayV2(portInfo) ;
                Ft.logger.debug(String.format("[%s] : [STOP_LATCH_B4_SENSOR_READ] : [RAW_STATE] : %s", getMyBayKey(), rawStateFromSensor));

                // Interpret the raw state based on Constant_IO_ActionMapping
                // Assuming OLD_OFF_NEW_ON means the stopper is activated/closed (sensor is ON)
                // If it's not OLD_OFF_NEW_ON, it means it's inactive/open (sensor is OFF)
                interpretedState = rawStateFromSensor.equals(Constant_IO_ActionMapping.ON) ? "STOPPER_CLOSED" : "STOPPER_OPENED";
                Ft.logger.debug(String.format("[%s] : [STOP_LATCH_B4_SENSOR_READ] : [INTERPRETED_STATE] : %s (Raw: %s)", getMyBayKey(), interpretedState, rawStateFromSensor));
                responseReturn.put("status", interpretedState);

            } catch (Exception e) {
                // Log communication error during input operation
                Ft.logger.error(String.format("[%s] : [COMMUNICATION_ERROR] : [STOP_LATCH_B4_SENSOR_READ] - Failed to read Stop Latch B4 status. Port: %s. Error: %s", getMyBayKey(), portInfo.getPortId(), e.getMessage()), e);
                // The default status "ERROR_READING_SENSOR" will remain, indicating failure
            }

        } else {
            // Log a clear error if the port information is missing
            Ft.logger.error(String.format("[%s] : [CONFIG_ERROR] : [STOP_LATCH_B4_SENSOR_READ] - Input port not found for Stop Latch B4: %s", getMyBayKey(), ConstantBayPortNameMapping.FT_PORT_NAME_STPR_B4));
            // The default status "ERROR_READING_SENSOR" will remain, indicating configuration failure
        }

        // Add simulation override if StateExecutorController.simulateFtBayHappyPath is a static boolean
        // Assuming for this check, a "happy path" means the stopper is OPENED
        if (StateExecutorController.simulateFtBayHappyPath) {
            interpretedState = "STOPPER_OPENED"; // Simulate the desired state
            responseReturn.put("status", interpretedState);
            Ft.logger.debug(String.format("[%s] : [SIMULATION] : Stop Latch B4 status overridden to '%s' for happy path.", getMyBayKey(), interpretedState));
        }

        // Structured debug log for method exit
        Ft.logger.debug(String.format("[%s] : [STOP_LATCH_B4_SENSOR_READ] : [REQUEST_EXIT] - Stop Latch B4 status read completed. Status: %s", getMyBayKey(), responseReturn.get("status")));
        return responseReturn;
    }
}
