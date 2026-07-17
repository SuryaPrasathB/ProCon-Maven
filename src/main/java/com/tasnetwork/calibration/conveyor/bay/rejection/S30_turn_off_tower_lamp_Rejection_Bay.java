package com.tasnetwork.calibration.conveyor.bay.rejection;

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

public class S30_turn_off_tower_lamp_Rejection_Bay implements RejectionBayState {

/*    String LOW   = "OPEN";
    String HIGH  = "CLOSE";
    String CLOSE  = "Off"; //"On";
    String OPEN   = "On"; //"Off";
    String ON  = "On";
 	String OFF  = "Off";*/
    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        Rejection.logger.info("S30_turn_off_tower_lamp_Rejection_Bay : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        

        Map<String,Object> responseReturn =  turn_off_tower_lamp();	 
		boolean turn_off_divertor_relay_FT_Bay = (boolean)responseReturn.get("status");
		
        if (turn_off_divertor_relay_FT_Bay) {
            Rejection.logger.info("S30_turn_off_tower_lamp_Rejection_Bay : Tower Lamp Turned Off");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        } else {
            Rejection.logger.info("S30_turn_off_tower_lamp_Rejection_Bay : Failed to Turn Off Tower Lamp");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_017);
        }

        Rejection.logger.info("S30_turn_off_tower_lamp_Rejection_Bay : Exit");
        return bayResponse;
    }

    //============================================================================================================================================  

    private Map<String,Object> turn_off_tower_lamp() {
        Rejection.logger.debug("S30_turn_off_tower_lamp_Rejection_Bay : turn_off_tower_lamp : Entry");

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
        //============================================================================================
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.REJECT_PORT_NAME_TWR_LAMP1);
        
        if (portInfo != null) {
            Rejection.logger.debug("PortId    : " + portInfo.getPortId());
            Rejection.logger.debug("ClusterId : " + portInfo.getClusterId());
            Rejection.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Rejection.logger.debug("S30_turn_off_tower_lamp_Rejection_Bay : Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(), 
                                              portInfo.getBayId(), 
                                              portInfo.getPortId(),
                                              Constant_IO_ActionMapping.CLOSE); // Use OPEN to represent turning the relay "Off"                              

        status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false;
        Rejection.logger.debug("S30_turn_off_tower_lamp_Rejection_Bay : turn_off_tower_lamp : status : " + status);
        //============================================================================================  
        
        if(StateExecutorController.simulateFtBayHappyPath){
        	status = true; 
        }

		responseReturn.put("status", status);
		
        Rejection.logger.debug("S30_turn_off_tower_lamp_Rejection_Bay : Exit");
        return responseReturn;
    }
}

