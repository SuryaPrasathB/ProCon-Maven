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

/**
 * State class responsible for turning ON Tower Lamp (Green) and turning OFF Tower Lamp (Red) at the FT Bay.
 */
public class S301_turn_off_tower_lamp1_FT_Bay implements FtBayState {
	
	BayUtils bayUtils = new BayUtils();

	
    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        // Structured log entry for sequence start
        Ft.logger.info(String.format("[%s] : [TOWER_LAMP_CONTROL] : [SEQUENCE_ENTRY] - Turning ON Tower Lamp (Green) and turning OFF Tower Lamp (Red).", getMyBayKey()));

        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true); // Assume success initially
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

        // First, attempt to turn ON the green tower lamp (TWR_LAMP1)
        Map<String,Object> responseReturn_turnOnGreen =  turn_on_tower_lamp_green();
		boolean turn_on_green_lamp_status = (boolean)responseReturn_turnOnGreen.getOrDefault("status", false);

        if (turn_on_green_lamp_status) {
        	Ft.logger.info(String.format("[%s] : [TOWER_LAMP_CONTROL] : [GREEN_LAMP_SUCCESS] - Tower Lamp (Green) successfully turned ON.", getMyBayKey()));

            // Now, attempt to turn OFF the red tower lamp (TWR_LAMP2)
            Map<String,Object> responseReturn_turnOffRed =  turn_off_tower_lamp_red();
            boolean turn_off_red_lamp_status = (boolean)responseReturn_turnOffRed.getOrDefault("status", false);

            if (turn_off_red_lamp_status) {
            	Ft.logger.info(String.format("[%s] : [TOWER_LAMP_CONTROL] : [RED_LAMP_SUCCESS] - Tower Lamp (Red) successfully turned OFF.", getMyBayKey()));
                bayResponse.setStatus(true);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
			}
            else {
            	Ft.logger.error(String.format("[%s] : [TOWER_LAMP_CONTROL] : [RED_LAMP_FAILED] - Failed to turn OFF Tower Lamp (Red). Error Code: %s", getMyBayKey(), ConvErrorCodeMapping.ERROR_CODE_HVT_014));
                 bayResponse.setStatus(false);
                 bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_HVT_014);
			}
        } else {
        	Ft.logger.error(String.format("[%s] : [TOWER_LAMP_CONTROL] : [GREEN_LAMP_FAILED] - Failed to turn ON Tower Lamp (Green). Error Code: %s", getMyBayKey(), ConvErrorCodeMapping.ERROR_CODE_HVT_014));
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_HVT_014);
        }

        Ft.logger.debug(String.format("[%s] : [TOWER_LAMP_CONTROL] : [DELAY] - Delaying for 1000 ms before sequence exit.", getMyBayKey()));
        BayUtils.delay(1000); // Delay before exiting the state

        // Structured log for sequence exit
        Ft.logger.info(String.format("[%s] : [TOWER_LAMP_CONTROL] : [SEQUENCE_EXIT] - Tower lamp control sequence completed. Final Status: %s", getMyBayKey(), bayResponse.getStatus()));
        return bayResponse;
    }

    //============================================================================================================================================

    private Map<String,Object> turn_off_tower_lamp_red() {
        // Structured debug log for method entry
        Ft.logger.debug(String.format("[%s] : [TOWER_LAMP_RED_OFF] : [REQUEST_ENTRY] - Attempting to turn OFF Tower Lamp (Red).", getMyBayKey()));

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false); // Default boolean status

        //============================================================================================
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_TWR_LAMP2);
        Ft.logger.debug(String.format("[%s] : [TOWER_LAMP_RED_OFF] : [PORT_CONFIG] - Target port: %s", getMyBayKey(), ConstantBayPortNameMapping.FT_PORT_NAME_TWR_LAMP2));

        String rawOutputState = "";
        if (portInfo != null) {
            Ft.logger.debug(String.format("[%s] : [TOWER_LAMP_RED_OFF] : [PORT_INFO] - PortId: %s, ClusterId: %s, BayId: %s", getMyBayKey(), portInfo.getPortId(), portInfo.getClusterId(), portInfo.getBayId()));

            String outputInactive = Constant_IO_ActionMapping.CLOSE;
            Ft.logger.debug(String.format("[%s] : [TOWER_LAMP_RED_OFF] : [SET_OUTPUT] - Setting output to: %s for port: %s", getMyBayKey(), outputInactive, portInfo.getPortId()));

            try {
                rawOutputState = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                                                  portInfo.getBayId(),
                                                  portInfo.getPortId(),
                                                  outputInactive);
                Ft.logger.debug(String.format("[%s] : [TOWER_LAMP_RED_OFF] : [RAW_OUTPUT_STATE] : %s", getMyBayKey(), rawOutputState));

                status = rawOutputState.equals(Constant_IO_ActionMapping.OFF);

            } catch (Exception e) {
                Ft.logger.error(String.format("[%s] : [COMMUNICATION_ERROR] : [TOWER_LAMP_RED_OFF] - Failed to set Tower Lamp (Red) state. Port: %s. Error: %s", getMyBayKey(), portInfo.getPortId(), e.getMessage()), e);
                status = false; // Indicate failure
            }

        } else {
            Ft.logger.error(String.format("[%s] : [CONFIG_ERROR] : [TOWER_LAMP_RED_OFF] - Output port not found for Tower Lamp (Red): %s", getMyBayKey(), ConstantBayPortNameMapping.FT_PORT_NAME_TWR_LAMP2));
        }

        // Apply simulation override if enabled
        if(StateExecutorController.simulateFtBayHappyPath){
        	status = true; // Simulate success for a "happy path"
            Ft.logger.debug(String.format("[%s] : [SIMULATION] : Tower Lamp (Red) status overridden to OFF (true) for happy path.", getMyBayKey()));
        }

		responseReturn.put("status", status);

        // Structured debug log for method exit
        Ft.logger.debug(String.format("[%s] : [TOWER_LAMP_RED_OFF] : [REQUEST_EXIT] - Tower Lamp (Red) operation completed. Status: %s", getMyBayKey(), status));
        return responseReturn;
    }

  //============================================================================================================================================

    private Map<String,Object> turn_on_tower_lamp_green() {
        // Structured debug log for method entry
        Ft.logger.debug(String.format("[%s] : [TOWER_LAMP_GREEN_ON] : [REQUEST_ENTRY] - Attempting to turn ON Tower Lamp (Green).", getMyBayKey()));

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false); // Default boolean status

        //============================================================================================
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_TWR_LAMP1);
        Ft.logger.debug(String.format("[%s] : [TOWER_LAMP_GREEN_ON] : [PORT_CONFIG] - Target port: %s", getMyBayKey(), ConstantBayPortNameMapping.FT_PORT_NAME_TWR_LAMP1));

        String rawOutputState = "";
        if (portInfo != null) {
            Ft.logger.debug(String.format("[%s] : [TOWER_LAMP_GREEN_ON] : [PORT_INFO] - PortId: %s, ClusterId: %s, BayId: %s", getMyBayKey(), portInfo.getPortId(), portInfo.getClusterId(), portInfo.getBayId()));

            String outputActive = Constant_IO_ActionMapping.OPEN;
            Ft.logger.debug(String.format("[%s] : [TOWER_LAMP_GREEN_ON] : [SET_OUTPUT] - Setting output to: %s for port: %s", getMyBayKey(), outputActive, portInfo.getPortId()));

            try {
                rawOutputState = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                                                  portInfo.getBayId(),
                                                  portInfo.getPortId(),
                                                  outputActive);
                Ft.logger.debug(String.format("[%s] : [TOWER_LAMP_GREEN_ON] : [RAW_OUTPUT_STATE] : %s", getMyBayKey(), rawOutputState));

                status = rawOutputState.equals(Constant_IO_ActionMapping.ON);

            } catch (Exception e) {
                Ft.logger.error(String.format("[%s] : [COMMUNICATION_ERROR] : [TOWER_LAMP_GREEN_ON] - Failed to set Tower Lamp (Green) state. Port: %s. Error: %s", getMyBayKey(), portInfo.getPortId(), e.getMessage()), e);
                status = false; // Indicate failure
            }

        } else {
            Ft.logger.error(String.format("[%s] : [CONFIG_ERROR] : [TOWER_LAMP_GREEN_ON] - Output port not found for Tower Lamp (Green): %s", getMyBayKey(), ConstantBayPortNameMapping.FT_PORT_NAME_TWR_LAMP1));
        }

        // Apply simulation override if enabled
        if(StateExecutorController.simulateFtBayHappyPath){
        	status = true; // Simulate success for a "happy path"
            Ft.logger.debug(String.format("[%s] : [SIMULATION] : Tower Lamp (Green) status overridden to ON (true) for happy path.", getMyBayKey()));
        }

		responseReturn.put("status", status);

        // Structured debug log for method exit
        Ft.logger.debug(String.format("[%s] : [TOWER_LAMP_GREEN_ON] : [REQUEST_EXIT] - Tower Lamp (Green) operation completed. Status: %s", getMyBayKey(), status));
        return responseReturn;
    }
}
