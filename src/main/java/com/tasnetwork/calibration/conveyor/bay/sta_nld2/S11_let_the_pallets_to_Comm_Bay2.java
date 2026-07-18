package com.tasnetwork.calibration.conveyor.bay.sta_nld2;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S11_let_the_pallets_to_Comm_Bay2 implements STA_NoLoadTestBay2State {

  
    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        StaNld_Bay2.logger.info("S11_let_the_pallets_to_Comm_Bay : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
   
        //=============================================================

		Map<String,Object> responseReturn =  open_StopLatch_SctNltBay2();	 
		boolean open_StopLatch_SctNltBay2 = (boolean)responseReturn.get("status");
	    
        boolean openSuccess = open_StopLatch_SctNltBay2;
        if(openSuccess) {
            BayUtils.delay(500);

    		responseReturn =  close_StopLatch_SctNltBay2();	 
    		boolean close_StopLatch_SctNltBay2 = (boolean)responseReturn.get("status");
    	    
            boolean closeSuccess = close_StopLatch_SctNltBay2;    
            if(closeSuccess) {
                StaNld_Bay2.logger.info("S11_let_the_pallets_to_Comm_Bay : Opening and Closing Stopper Success");
                bayResponse.setStatus(true);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
            }    
            else{
                StaNld_Bay2.logger.info("S11_let_the_pallets_to_Comm_Bay : Closing Stopper Failed");
                bayResponse.setStatus(false);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_009);  // Assuming 602 for failure
            }
        }
        else{
            StaNld_Bay2.logger.info("S11_let_the_pallets_to_Comm_Bay : Opening Stopper Failed");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_008);
        }
        //=============================================================

        StaNld_Bay2.logger.info("S11_let_the_pallets_to_Comm_Bay : Exit");
        return bayResponse;
    }
 
    //============================================================================================================================================

    private Map<String,Object> open_StopLatch_SctNltBay2() {
        StaNld_Bay2.logger.debug("S11_let_the_pallets_to_Comm_Bay : open_StopLatch_SctNltBay2 : Entry");

        boolean status = false;
    	Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        //============================================================================================		 
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.SCT_NLT2_PORT_NAME_STPR);

        if (portInfo != null) {
            StaNld_Bay2.logger.debug("PortId    : " + portInfo.getPortId());
            StaNld_Bay2.logger.debug("ClusterId : " + portInfo.getClusterId());
            StaNld_Bay2.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            StaNld_Bay2.logger.debug("S11_let_the_pallets_to_Comm_Bay : open_StopLatch_SctNltBay2 : Stopper Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                                              portInfo.getBayId(),
                                              portInfo.getPortId(),
                                              Constant_IO_ActionMapping.CLOSE);
        status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false;
        
        if(StateExecutorController.simulateSCTNLTBay2HappyPath){
        	status = true; 
        }
         
        StaNld_Bay2.logger.debug("S11_let_the_pallets_to_Comm_Bay : open_StopLatch_SctNltBay2 : status : " + status);
        //============================================================================================  
        StaNld_Bay2.logger.debug("S11_let_the_pallets_to_Comm_Bay : open_StopLatch_SctNltBay2 : Exit");
        return responseReturn;
    }
 
    //============================================================================================================================================

    private Map<String,Object> close_StopLatch_SctNltBay2() {
        StaNld_Bay2.logger.debug("S11_let_the_pallets_to_Comm_Bay : close_StopLatch_SctNltBay2 : Entry");

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
        //============================================================================================		 
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.SCT_NLT2_PORT_NAME_STPR);

        if (portInfo != null) {
            StaNld_Bay2.logger.debug("PortId    : " + portInfo.getPortId());
            StaNld_Bay2.logger.debug("ClusterId : " + portInfo.getClusterId());
            StaNld_Bay2.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            StaNld_Bay2.logger.debug("S11_let_the_pallets_to_Comm_Bay : close_StopLatch_SctNltBay2 : Stopper Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                                              portInfo.getBayId(),
                                              portInfo.getPortId(),
                                              Constant_IO_ActionMapping.OPEN);
        status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;
        StaNld_Bay2.logger.debug("S11_let_the_pallets_to_Comm_Bay : close_StopLatch_SctNltBay2 : status : " + status);
        //============================================================================================   

		responseReturn.put("status", status);
		
        StaNld_Bay2.logger.debug("S11_let_the_pallets_to_Comm_Bay : close_StopLatch_SctNltBay2 : Exit");
        return responseReturn;
    }
    
    //============================================================================================================================================
}
