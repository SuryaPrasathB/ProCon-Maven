package com.tasnetwork.calibration.conveyor.bay.ft;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

/**
 * State class responsible for waiting for the reset button at Rejection Bay.
 */
public class S19_wait_for_reset_button_ip_Rejection_Bay implements FtBayState {


    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        // Structured log entry for sequence start
        Ft.logger.info(String.format("[%s] : [RESET_BUTTON_WAIT] : [SEQUENCE_ENTRY] - Waiting for reset button press at Rejection Bay.", getMyBayKey()));

        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true); // Assume success initially
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

        Map<String,Object> responseReturn;
        boolean isResetButtonPressed = false; // Initialize to false

        // Perform initial check
        responseReturn = is_ResetButton_RejectionBay_status();
        isResetButtonPressed = (boolean)responseReturn.getOrDefault("status", false); // Default to false if status is missing

        // Loop to wait until the reset button is pressed or stop is requested
        Ft.logger.debug(String.format("[%s] : [RESET_BUTTON_WAIT] : [WAITING_LOOP_START] - Entering loop to wait for reset button. Current status: %s", getMyBayKey(), isResetButtonPressed));
        while(!isResetButtonPressed && !ConstantConveyor.ALL_LOOP_BREAK_FLAG && !Ft.isStopProcessRequestedFtBay()){ // Added Ft.isStopProcessRequestedFtBay for a more robust stop condition
            // Re-check the status of the reset button
            responseReturn = is_ResetButton_RejectionBay_status();
            isResetButtonPressed = (boolean)responseReturn.getOrDefault("status", false);

            if (!isResetButtonPressed) {
                Ft.logger.warn(String.format("[%s] : [RESET_BUTTON_WAIT] : [BUTTON_NOT_PRESSED] - Reset button at Rejection Bay is not yet pressed. Waiting...", getMyBayKey()));
                BayUtils.delay(1000); // Wait for 1 second before re-checking
            }
        }

        // After the loop, determine the final status
        if (isResetButtonPressed) {
            Ft.logger.info(String.format("[%s] : [RESET_BUTTON_WAIT] : [BUTTON_PRESSED] - Reset button at Rejection Bay successfully pressed. Proceeding.", getMyBayKey()));
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        } else {
            // This else block is reached if the loop exited due to ALL_LOOP_BREAK_FLAG or Ft.isStopProcessRequestedFtBay being true
            Ft.logger.error(String.format("[%s] : [RESET_BUTTON_WAIT] : [STOP_REQUESTED_OR_NOT_PRESSED] - Loop interrupted or reset button not pressed. Final status indicates failure. Button status: %s, ALL_LOOP_BREAK_FLAG: %s, StopProcessRequested: %s",
                                         getMyBayKey(), isResetButtonPressed, ConstantConveyor.ALL_LOOP_BREAK_FLAG, Ft.isStopProcessRequestedFtBay()));
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_021); // Indicate failure due to button not being pressed when exiting loop
        }

        // Structured log for sequence exit
        Ft.logger.info(String.format("[%s] : [RESET_BUTTON_WAIT] : [SEQUENCE_EXIT] - Wait for reset button sequence completed. Final Status: %s", getMyBayKey(), bayResponse.getStatus()));
        return bayResponse;
    }
    //============================================================================================================================================

    private Map<String,Object> is_ResetButton_RejectionBay_status() {
        // Structured debug log for method entry
        Ft.logger.debug(String.format("[%s] : [RESET_BUTTON_READ] : [REQUEST_ENTRY] - Reading reset button status at Rejection Bay.", getMyBayKey()));

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false); // Default boolean status

        //============================================================================================
        // Note: The original code uses getOutputPortDetails for a push button, which is typically an input.
        // Assuming this is a typo and it should be getInputPortDetails or there's a specific reason it's an output.
        // For a reset button, it's almost always an input. Will proceed assuming getInputPortDetails should be used.
        IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.REJECT_PORT_NAME_PUSH_BTN);

        if (portInfo != null) {
            // Log port information with proper formatting
            Ft.logger.debug(String.format("[%s] : [RESET_BUTTON_READ] : [PORT_INFO] - PortId: %s, ClusterId: %s, BayId: %s", getMyBayKey(), portInfo.getPortId(), portInfo.getClusterId(), portInfo.getBayId()));

            BayUtils bayUtils = new BayUtils();
            String rawStateFromSensor = "";
            try {
                rawStateFromSensor = bayUtils.getInputDataFromBayV2(portInfo);
                Ft.logger.debug(String.format("[%s] : [RESET_BUTTON_READ] : [RAW_STATE] : %s", getMyBayKey(), rawStateFromSensor));

                // Determine status based on raw sensor state
                // Assuming Constant_IO_ActionMapping.LOW means the button IS pressed (active low)
                status = rawStateFromSensor.equals(Constant_IO_ActionMapping.LOW); // true if button pressed

                Ft.logger.debug(String.format("[%s] : [RESET_BUTTON_READ] : [INTERPRETED_STATUS] : %s (Raw: %s)", getMyBayKey(), status ? "Pressed" : "Not Pressed", rawStateFromSensor));

            } catch (Exception e) {
                // Log communication error during input operation
                Ft.logger.error(String.format("[%s] : [COMMUNICATION_ERROR] : [RESET_BUTTON_READ] - Failed to read reset button status. Port: %s. Error: %s", getMyBayKey(), portInfo.getPortId(), e.getMessage()), e);
                // Status remains false due to error
            }

        } else {
            // Log a clear error if the port information is missing
            Ft.logger.error(String.format("[%s] : [CONFIG_ERROR] : [RESET_BUTTON_READ] - Input port not found for reset button: %s", getMyBayKey(), ConstantBayPortNameMapping.REJECT_PORT_NAME_PUSH_BTN));
            // Status remains false due to missing config
        }

        // Simulate happy path if enabled, overriding the actual status
        if(StateExecutorController.simulateFtBayHappyPath){
        	status = true; // Simulate button pressed for a "happy path"
            Ft.logger.debug(String.format("[%s] : [SIMULATION] : Reset button status overridden to PRESSED for happy path.", getMyBayKey()));
        }

		responseReturn.put("status", status);

        // Structured debug log for method exit
        Ft.logger.debug(String.format("[%s] : [RESET_BUTTON_READ] : [REQUEST_EXIT] - Reset button status read completed. Status: %s", getMyBayKey(), status));
        return responseReturn;
    }
}
