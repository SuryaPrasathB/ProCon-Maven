package com.tasnetwork.calibration.conveyor.bay.sta_nld1;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S29_turn_on_tower_lamp_STA_NLDT_Bay1 implements STA_NoLoadTestBay1State {

    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        StaNld_Bay1.logger.info("S29_turn_on_tower_lamp_STA_NLDT_Bay1 : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);     

        Map<String,Object> responseReturn =  turn_on_tower_lamp();	 
		boolean turn_on_red_lamp = (boolean)responseReturn.get("status");
	    
        if (turn_on_red_lamp) {   	
        	StaNld_Bay1.logger.info("S29_turn_on_tower_lamp_STA_NLDT_Bay1 : turn_on_tower_lamp : Success");
            
            //BayUtils.delay(500);
            
            responseReturn =  turn_off_tower_lamp();	           
            boolean turn_off_green_lamp = (boolean)responseReturn.get("status");          

            if (turn_off_green_lamp) {
            	StaNld_Bay1.logger.info("S29_turn_on_tower_lamp_STA_NLDT_Bay1 : turn_off_tower_lamp : Success");
                bayResponse.setStatus(true);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
			} 
            else {
            	StaNld_Bay1.logger.info("S29_turn_on_tower_lamp_STA_NLDT_Bay1 : Failed to turn_off_tower_lamp GREEN");
                 bayResponse.setStatus(false);
                 bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_HVT_014 );
			}    
        } else {
        	StaNld_Bay1.logger.info("S29_turn_on_tower_lamp_STA_NLDT_Bay1 : Failed to turn_on_tower_lamp RED");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_HVT_014 );
        }

        StaNld_Bay1.logger.info("S29_turn_on_tower_lamp_STA_NLDT_Bay1 : Exit");
        return bayResponse;
    }

  //============================================================================================================================================  

    private Map<String,Object> turn_on_tower_lamp() {
        StaNld_Bay1.logger.debug("S29_turn_on_tower_lamp_STA_NLDT_Bay1 : turn_on_tower_lamp : Entry");

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
        //============================================================================================
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.SCT_NLT1_PORT_NAME_TWR_LAMP2);
        
        if (portInfo != null) {
            StaNld_Bay1.logger.debug("PortId    : " + portInfo.getPortId());
            StaNld_Bay1.logger.debug("ClusterId : " + portInfo.getClusterId());
            StaNld_Bay1.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            StaNld_Bay1.logger.debug("S29_turn_on_tower_lamp_STA_NLDT_Bay1 : Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(), 
                                              portInfo.getBayId(), 
                                              portInfo.getPortId(),
                                              Constant_IO_ActionMapping.OPEN); // Use CLOSE to represent turning the relay "On"

        status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;
        
        StaNld_Bay1.logger.debug("S29_turn_on_tower_lamp_STA_NLDT_Bay1 : turn_on_tower_lamp : status : " + status);
        //============================================================================================  
        
        if(StateExecutorController.simulateSCTNLTBay1HappyPath){
        	status = true; 
        }

		responseReturn.put("status", status);
		
        StaNld_Bay1.logger.debug("S29_turn_on_tower_lamp_STA_NLDT_Bay1 : Exit");
        return responseReturn;
    }
    
    private Map<String,Object> turn_off_tower_lamp() {
        StaNld_Bay1.logger.debug("S29_turn_on_tower_lamp_STA_NLDT_Bay1 : turn_off_tower_lamp : Entry");

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
        //============================================================================================
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.SCT_NLT1_PORT_NAME_TWR_LAMP1);
        
        if (portInfo != null) {
            StaNld_Bay1.logger.debug("PortId    : " + portInfo.getPortId());
            StaNld_Bay1.logger.debug("ClusterId : " + portInfo.getClusterId());
            StaNld_Bay1.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            StaNld_Bay1.logger.debug("S29_turn_on_tower_lamp_STA_NLDT_Bay1 : Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(), 
                                              portInfo.getBayId(), 
                                              portInfo.getPortId(),
                                              Constant_IO_ActionMapping.CLOSE); // Use CLOSE to represent turning the relay "On"

        status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false;
        
        StaNld_Bay1.logger.debug("S29_turn_on_tower_lamp_STA_NLDT_Bay1 : turn_off_tower_lamp : status : " + status);
        //============================================================================================  
        
        if(StateExecutorController.simulateFtBayHappyPath){
        	status = true; 
        }

		responseReturn.put("status", status);
		
        StaNld_Bay1.logger.debug("S29_turn_on_tower_lamp_STA_NLDT_Bay1 : Exit");
        return responseReturn;
    }
}

