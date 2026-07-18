package com.tasnetwork.calibration.conveyor.bay.sta_nld1;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S31_turn_on_Diverter_STA_Bay1 implements STA_NoLoadTestBay1State {


    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
    	StaNld_Bay1.logger.info("S13_turn_on_Diverter_STA1_Bay : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		Map<String,Object> responseReturn =  turn_on_diverter_of_STA1_Bay();	 
		boolean turn_on_diverter_of_STA1_Bay = (boolean)responseReturn.get("status");
	    
        if (turn_on_diverter_of_STA1_Bay) {
            StaNld_Bay1.logger.info("S13_turn_on_Diverter_STA1_Bay : Diverter Turned On");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        } else {
            StaNld_Bay1.logger.info("S13_turn_on_Diverter_STA1_Bay : Failed to Turn On Diverter");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_018);
        }

        StaNld_Bay1.logger.info("S13_turn_on_Diverter_STA1_Bay : Exit");
        return bayResponse;
    }

    //============================================================================================================================================  

    private Map<String,Object> turn_on_diverter_of_STA1_Bay() {
        StaNld_Bay1.logger.debug("S13_turn_on_Diverter_STA1_Bay : turn_on_diverter_of_STA1_Bay : Entry");

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
        //============================================================================================  
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.OUT_AREA_DIVERTOR);
        
        if (portInfo != null) {
            StaNld_Bay1.logger.debug("PortId    : " + portInfo.getPortId());
            StaNld_Bay1.logger.debug("ClusterId : " + portInfo.getClusterId());
            StaNld_Bay1.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            StaNld_Bay1.logger.debug("S13_turn_on_Diverter_STA1_Bay : Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(), 
                                              portInfo.getBayId(), 
                                              portInfo.getPortId(),
                                              Constant_IO_ActionMapping.OPEN); // Use CLOSE to represent turning the diverter "On"
        status = state.equals(Constant_IO_ActionMapping.ON) ? true : false; 

		if(StateExecutorController.simulateSCTNLTBay1HappyPath){
			status = true; 
		}
        
        StaNld_Bay1.logger.debug("S13_turn_on_Diverter_STA1_Bay : turn_on_diverter_of_STA1_Bay : status : " + status);
        //============================================================================================   

		responseReturn.put("status", status);
		
        StaNld_Bay1.logger.debug("S13_turn_on_Diverter_STA1_Bay : verificBay_Diverter_Status : Exit");
        return responseReturn;
    }
}