package com.tasnetwork.calibration.conveyor.bay.ft;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;

/**
 * State class responsible for allowing a pallet to enter the FT Bay.
 * It opens the upstream stopper (B4) to release the pallet into the bay.
 */
public class S02_let_the_pallet_to_FT_Bay implements FtBayState {

    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        // Structured log entry for sequence start
        Ft.logger.info(String.format("[%s] : [PALLET_RELEASE] : [SEQUENCE_ENTRY] - Initiating pallet release to FT Bay.", getMyBayKey()));

        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true); // Assume success initially
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601); // Default success code
   
        //=============================================================
        Map<String,Object> responseReturn = open_StopLatch_B4_FtBay();	 
		boolean openSuccess = (boolean)responseReturn.get("status");
		
        if(openSuccess) {
    		// Structured log for success
    		Ft.logger.info(String.format("[%s] : [STOPPER_OPEN] : [SUCCESS] - Stopper B4 opened successfully for pallet entry.", getMyBayKey()));
    		bayResponse.setStatus(true);
    		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
    		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayEntryStopper(getMyBayKey(),true);
    	}    
    	else{
    		// Structured log for failure with error code
    		Ft.logger.error(String.format("[%s] : [STOPPER_OPEN] : [FAILED] - Failed to open Stopper B4 for pallet entry. Error: %s", getMyBayKey(), ConvErrorCodeMapping.ERROR_CODE_FT_023));
    		bayResponse.setStatus(false);
    		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_023);
    	}
        //=============================================================

        // Structured log for sequence exit
        Ft.logger.info(String.format("[%s] : [PALLET_RELEASE] : [SEQUENCE_EXIT] - Pallet release sequence completed.", getMyBayKey()));
        return bayResponse;
    }
 
    //============================================================================================================================================

    // Open the Stop Latch at FT Bay
    private Map<String,Object> open_StopLatch_B4_FtBay() {
        // Structured debug log for method entry
        Ft.logger.debug(String.format("[%s] : [STOPlATCH_B4_OPEN] : [REQUEST_ENTRY] - Requesting to open stopper B4.", getMyBayKey()));

        boolean status = false; 
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
    	
    	IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_STPR_B4);

        if (portInfo != null) {
            // Structured debug log for port information
            Ft.logger.debug(String.format("[%s] : [STOPlATCH_B4_OPEN] : [PORT_INFO] - PortId: %s, ClusterId: %s, BayId: %s", getMyBayKey(), portInfo.getPortId(), portInfo.getClusterId(), portInfo.getBayId()));
        } else {
            // Structured error log for missing port configuration
            Ft.logger.error(String.format("[%s] : [CONFIG_ERROR] : [STOPLATCH_B4_OPEN] - Stopper output port not found: %s", getMyBayKey(), ConstantBayPortNameMapping.FT_PORT_NAME_STPR_B4));
            return responseReturn; // Return early if critical config is missing
        }

        BayUtils bayUtils = new BayUtils();
        String state = "";
        
        try {
            if(ProcalFeatureEnable.MODBUS_PLC_SLAVE_MODE){
            	state = bayUtils.setOutputDataToPlcBay(portInfo.getClusterId(),
                        portInfo.getBayId(),
                        portInfo.getPortId(),
                        Constant_IO_ActionMapping.CLOSE);
            }else{
            	state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                        portInfo.getBayId(),
                        portInfo.getPortId(),
                        Constant_IO_ActionMapping.CLOSE);
            }
        } catch (Exception e) {
            // Log any exceptions during output data transmission
            Ft.logger.error(String.format("[%s] : [COMMUNICATION_ERROR] : [STOPLATCH_B4_OPEN] - Failed to send open command to stopper. Port: %s. Error: %s", getMyBayKey(), portInfo.getPortId(), e.getMessage()), e);
            responseReturn.put("status", false);
            return responseReturn;
        }

        // Log the raw state received from the control system for debugging
        Ft.logger.debug(String.format("[%s] : [STOPLATCH_B4_OPEN] : [RAW_STATE] : %s", getMyBayKey(), state));

        // Determine status based on expected response
        status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false;
        
        if(StateExecutorController.simulateFtBayHappyPath){
			status = true;
            // Shortened and moved to TRACE level for minimal impact
			Ft.logger.debug(String.format("[%s] : [SIMULATION] : Stopper open status overridden to TRUE.", getMyBayKey()));
		}
        // Log the final resolved status
        Ft.logger.debug(String.format("[%s] : [STOPLATCH_B4_OPEN] : [FINAL_STATUS] : %s", getMyBayKey(), status));
   	
		responseReturn.put("status", status);		

        // Structured debug log for method exit
   	    Ft.logger.debug(String.format("[%s] : [STOPLATCH_B4_OPEN] : [REQUEST_EXIT] - Stopper B4 open request completed.", getMyBayKey()));
   	    return responseReturn;
   	}
 
}
