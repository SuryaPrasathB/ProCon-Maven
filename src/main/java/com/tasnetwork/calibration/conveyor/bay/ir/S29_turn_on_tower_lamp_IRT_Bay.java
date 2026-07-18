package com.tasnetwork.calibration.conveyor.bay.ir;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S29_turn_on_tower_lamp_IRT_Bay implements IrtBayState {

    //===========================================================================================
    /**
     * Turns on the tower lamp at the IRT Bay.
     *
     * @return BayResponse indicating success or failure.
     */
    @Override
    public BayResponse handleRequest() {
        Ir.logger.info("S29_turn_on_tower_lamp1_IRT_Bay : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

        Map<String,Object> responseReturn = turn_on_tower_lamp();
        boolean turn_on_red_lamp = (boolean)responseReturn.get("status");

        if (turn_on_red_lamp) {
            Ir.logger.info("S29_turn_on_tower_lamp1_IRT_Bay : turn_on_tower_lamp : Success");

            responseReturn = turn_off_tower_lamp();
            boolean turn_off_green_lamp = (boolean)responseReturn.get("status");

            if (turn_off_green_lamp) {
                Ir.logger.info("S29_turn_on_tower_lamp1_IRT_Bay : turn_off_tower_lamp : Success");
                bayResponse.setStatus(true);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
            } else {
                Ir.logger.info("S29_turn_on_tower_lamp1_IRT_Bay : Failed to turn_off_tower_lamp GREEN");
                bayResponse.setStatus(false);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_017);
            }
        } else {
            Ir.logger.info("S29_turn_on_tower_lamp1_IRT_Bay : Failed to turn_on_tower_lamp RED");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_017);
        }

        Ir.logger.info("S29_turn_on_tower_lamp1_IRT_Bay : Exit");
        return bayResponse;
    }

    //============================================================================================================================================

    private Map<String,Object> turn_on_tower_lamp() {
        Ir.logger.debug("S29_turn_on_tower_lamp1_IRT_Bay : turn_on_tower_lamp : Entry");

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
        //============================================================================================
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.IR_PORT_NAME_TWR_LAMP2);
        
        if (portInfo != null) {
            Ir.logger.debug("PortId    : " + portInfo.getPortId());
            Ir.logger.debug("ClusterId : " + portInfo.getClusterId());
            Ir.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Ir.logger.debug("S29_turn_on_tower_lamp1_IRT_Bay : Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(), 
                                              portInfo.getBayId(), 
                                              portInfo.getPortId(),
                                              Constant_IO_ActionMapping.OPEN); // Use CLOSE to represent turning the relay "On"

        status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;
        
        Ir.logger.debug("S29_turn_on_tower_lamp1_IRT_Bay : turn_on_tower_lamp : status : " + status);
        //============================================================================================  
        
        if(StateExecutorController.simulateFtBayHappyPath){
        	status = true; 
        }

		responseReturn.put("status", status);
		
        Ir.logger.debug("S29_turn_on_tower_lamp1_IRT_Bay : Exit");
        return responseReturn;
    }
    
//============================================================================================================================================  
 
    private Map<String,Object> turn_off_tower_lamp() {
        Ir.logger.debug("S29_turn_on_tower_lamp1_IRT_Bay : turn_off_tower_lamp : Entry");

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
        //============================================================================================
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.IR_PORT_NAME_TWR_LAMP1);
        
        if (portInfo != null) {
            Ir.logger.debug("PortId    : " + portInfo.getPortId());
            Ir.logger.debug("ClusterId : " + portInfo.getClusterId());
            Ir.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Ir.logger.debug("S29_turn_on_tower_lamp1_IRT_Bay : Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(), 
                                              portInfo.getBayId(), 
                                              portInfo.getPortId(),
                                              Constant_IO_ActionMapping.CLOSE); // Use CLOSE to represent turning the relay "On"

        status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false;
        
        Ir.logger.debug("S29_turn_on_tower_lamp1_IRT_Bay : turn_off_tower_lamp : status : " + status);
        //============================================================================================  
        
        if(StateExecutorController.simulateFtBayHappyPath){
        	status = true; 
        }

		responseReturn.put("status", status);
		
        Ir.logger.debug("S29_turn_on_tower_lamp1_IRT_Bay : Exit");
        return responseReturn;
    }
}
