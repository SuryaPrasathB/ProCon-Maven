package com.tasnetwork.calibration.conveyor.bay.ft;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S21_ensure_pallet_reached_Reject_Bay implements FtBayState {

    public String getMyBayKey() {
        return myBayKey;
    }


    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        // Structured log entry for sequence start
        Ft.logger.info(String.format("[%s] : [PALLET_REACH_CHECK] : [SEQUENCE_ENTRY] - Ensuring pallet reached Rejection Bay.", getMyBayKey()));

        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(false); // Assume failure initially
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601); // Default success code, but set to failure if pallet not detected

        int try_count = 0;
        final int MAX_RETRY_COUNT = 3;

        // Loop to retry checking if the pallet has reached the rejection bay
        Ft.logger.debug(String.format("[%s] : [PALLET_REACH_CHECK] : [RETRY_LOOP_START] - Starting retry loop to confirm pallet presence (Max retries: %d).", getMyBayKey(), MAX_RETRY_COUNT));
        while (try_count <= MAX_RETRY_COUNT) {
            Map<String,Object> responseReturn = pallet_sensed_at_Reject_Bay();
            // Ensure safe retrieval from map, defaulting to an unexpected state string
            String pallet_sensed_at_Reject_Bay_status = (String)responseReturn.getOrDefault("status", "UNEXPECTED_STATUS");

            Ft.logger.debug(String.format("[%s] : [PALLET_REACH_CHECK] : [ATTEMPT_%d] - Current pallet sensor status: %s", getMyBayKey(), (try_count + 1), pallet_sensed_at_Reject_Bay_status));

            if (pallet_sensed_at_Reject_Bay_status.equals(Constant_IO_ActionMapping.DETECTED)) {  // Verify if pallet is detected
            	ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayAllPalletsExistInNextTargetBay(getMyBayKey(),true); 
                Ft.logger.info(String.format("[%s] : [PALLET_REACH_CHECK] : [SUCCESS] - Pallet successfully detected at Rejection Bay after %d attempts.", getMyBayKey(), (try_count + 1)));
                if (ProconFeatureEnable.MOTOR_CONTROL_ENABLE) {
    				BayUtils bayUtils = new BayUtils();
    					responseReturn = bayUtils.set_motor_not_required(getMyBayKey());
    				boolean set_motor_required = (boolean) responseReturn.get("status");
    				if (set_motor_required) {
    					Ft.logger.info("set_motor_not_required : Success");
    					bayResponse.setStatus(true);
    					bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
    				} else {
    					Ft.logger.info("set_motor_not_required: Failed to set_motor_required ");
    					bayResponse.setStatus(false);
    					bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_026);
    				} 
    			}
                bayResponse.setStatus(true);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
                break; // Exit loop on success
            } else {
                Ft.logger.warn(String.format("[%s] : [PALLET_REACH_CHECK] : [NOT_DETECTED] - Pallet not yet detected at Rejection Bay. Attempt %d of %d. Retrying...", getMyBayKey(), (try_count + 1), MAX_RETRY_COUNT));
                bayResponse.setStatus(false);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_025);
                BayUtils.delay(1000); // Wait for 1 second before retrying
                try_count++;
            }
        }

        // Check if the loop completed without success
        if (!bayResponse.getStatus()) {
            Ft.logger.error(String.format("[%s] : [PALLET_REACH_CHECK] : [FAILED_AFTER_RETRIES] - Failed to detect pallet at Rejection Bay after %d attempts. Final Error Code: %s", getMyBayKey(), MAX_RETRY_COUNT, ConvErrorCodeMapping.ERROR_CODE_FT_025));
        }

        // Structured log for sequence exit
        Ft.logger.info(String.format("[%s] : [PALLET_REACH_CHECK] : [SEQUENCE_EXIT] - Pallet reach check sequence completed. Final Status: %s", getMyBayKey(), bayResponse.getStatus()));
        return bayResponse;
    }
    //============================================================================================================================================

    private Map<String, Object> pallet_sensed_at_Reject_Bay() {
        // Structured debug log for method entry
        Ft.logger.debug(String.format("[%s] : [PALLET_SENSOR_READ] : [REQUEST_ENTRY] - Reading pallet sensor status at Rejection Bay.", getMyBayKey()));

        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", "ERROR_READING_STATUS"); // Default string status for clarity in case of failure

        //============================================================================================
        IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.REJECT_PORT_NAME_SNSR_PALLET);

        if (portInfo != null) {
            // Log port information with proper formatting
            Ft.logger.debug(String.format("[%s] : [PALLET_SENSOR_READ] : [PORT_INFO] - PortId: %s, ClusterId: %s, BayId: %s", getMyBayKey(), portInfo.getPortId(), portInfo.getClusterId(), portInfo.getBayId()));

            BayUtils bayUtils = new BayUtils();
            String rawStateFromSensor = "";
            try {
                rawStateFromSensor = bayUtils.getInputDataFromBayV2(portInfo);
                Ft.logger.debug(String.format("[%s] : [PALLET_SENSOR_READ] : [RAW_STATE] : %s", getMyBayKey(), rawStateFromSensor));

                // Determine status based on raw sensor state
                // Assuming Constant_IO_ActionMapping.OLD_OFF_NEW_ON means pallet IS DETECTED
                String interpretedState = rawStateFromSensor.equals(Constant_IO_ActionMapping.ON) ?
                                          Constant_IO_ActionMapping.DETECTED :
                                          Constant_IO_ActionMapping.NOT_DETECTED;

                Ft.logger.debug(String.format("[%s] : [PALLET_SENSOR_READ] : [INTERPRETED_STATUS] : %s (Raw: %s)", getMyBayKey(), interpretedState, rawStateFromSensor));
                responseReturn.put("status", interpretedState);

            } catch (Exception e) {
                // Log communication error during input operation
                Ft.logger.error(String.format("[%s] : [COMMUNICATION_ERROR] : [PALLET_SENSOR_READ] - Failed to read pallet sensor status. Port: %s. Error: %s", getMyBayKey(), portInfo.getPortId(), e.getMessage()), e);
                // The default status "ERROR_READING_STATUS" will remain, indicating failure
            }

        } else {
            // Log a clear error if the port information is missing
            Ft.logger.error(String.format("[%s] : [CONFIG_ERROR] : [PALLET_SENSOR_READ] - Input port not found for pallet sensor: %s", getMyBayKey(), ConstantBayPortNameMapping.REJECT_PORT_NAME_SNSR_PALLET));
            // The default status "ERROR_READING_STATUS" will remain, indicating configuration failure
        }

        // Simulate happy path if enabled, overriding the actual status
        if(StateExecutorController.simulateFtBayHappyPath){
        	responseReturn.put("status", Constant_IO_ActionMapping.DETECTED); // Simulate pallet detected for a "happy path"
            Ft.logger.debug(String.format("[%s] : [SIMULATION] : Pallet sensor status overridden to DETECTED for happy path.", getMyBayKey()));
        }

        // Structured debug log for method exit
        Ft.logger.debug(String.format("[%s] : [PALLET_SENSOR_READ] : [REQUEST_EXIT] - Pallet sensor status read completed. Status: %s", getMyBayKey(), responseReturn.get("status")));
        return responseReturn;
    }
}
