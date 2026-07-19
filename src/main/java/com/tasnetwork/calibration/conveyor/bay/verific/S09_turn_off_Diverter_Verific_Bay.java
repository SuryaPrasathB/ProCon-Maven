package com.tasnetwork.calibration.conveyor.bay.verific;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S09_turn_off_Diverter_Verific_Bay implements VerificTestBayState {

//===========================================================================================
    @Override
    public BayResponse handleRequest() {
        Verification.logger.info("S09_turn_off_Diverter_of_Verific_Bay : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        

        Map<String,Object> responseReturn =  turn_off_diverter_of_Verific_Bay();	 
        boolean turn_off_diverter_of_Verific_Bay = (boolean)responseReturn.get("status");
	    
        if (turn_off_diverter_of_Verific_Bay) {
        	       	
            Verification.logger.info("S09_turn_off_Diverter_of_Verific_Bay : Diverter Turned Off");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        } else {
            Verification.logger.info("S09_turn_off_Diverter_of_Verific_Bay : Failed to Turn Off Diverter");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_017);
        }

        Verification.logger.info("S09_turn_off_Diverter_of_Verific_Bay : Exit");
        return bayResponse;
    }

    //============================================================================================================================================  

    private Map<String,Object> turn_off_diverter_of_Verific_Bay() {
        Verification.logger.debug("S09_turn_off_Diverter_of_Verific_Bay : verificBay_Diverter_Status : Entry");

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
        //============================================================================================  
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.VERIFIC_PORT_NAME_DIVERTOR_RELAY);
        
        if (portInfo != null) {
            Verification.logger.debug("PortId    : " + portInfo.getPortId());
            Verification.logger.debug("ClusterId : " + portInfo.getClusterId());
            Verification.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Verification.logger.debug("S09_turn_off_Diverter_of_Verific_Bay : Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(), 
                                              portInfo.getBayId(), 
                                              portInfo.getPortId(),
                                              Constant_IO_ActionMapping.CLOSE); // Use OPEN to represent turning the diverter "Off"
        status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false; 

		if(StateExecutorController.simulateVerificBayHappyPath){
			status = true; 
		}
                           
        Verification.logger.debug("S09_turn_off_Diverter_of_Verific_Bay : verificBay_Diverter_Status : status : " + status);
        //============================================================================================   

		responseReturn.put("status", status);
		
        Verification.logger.debug("S09_turn_off_Diverter_of_Verific_Bay : verificBay_Diverter_Status : Exit");
        return responseReturn;
    }
}

