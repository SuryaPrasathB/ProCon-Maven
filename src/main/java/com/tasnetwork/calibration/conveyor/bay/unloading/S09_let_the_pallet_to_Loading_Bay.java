package com.tasnetwork.calibration.conveyor.bay.unloading;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S09_let_the_pallet_to_Loading_Bay implements  UnloadingBayState {

 
    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        Unloading.logger.info("S09_let_the_pallet_to_Loading_Bay : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
   
        //=============================================================

		Map<String,Object> responseReturn =  open_StopLatch_UnloadingBay();	 
		boolean open_StopLatch_UnloadingBay = (boolean)responseReturn.get("status");
	    
        boolean openSuccess = open_StopLatch_UnloadingBay;
        if(openSuccess) {
            BayUtils.delay(500);
            
            responseReturn = close_StopLatch_UnloadingBay();	 
            boolean close_StopLatch_UnloadingBay = (boolean)responseReturn.get("status");
     	    
            boolean closeSuccess = close_StopLatch_UnloadingBay;    

    	 
            if(closeSuccess) {
                Unloading.logger.info("S09_let_the_pallet_to_Loading_Bay : Opening and Closing Stopper Success");
                bayResponse.setStatus(true);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
            }    
            else{
                Unloading.logger.info("S09_let_the_pallet_to_Loading_Bay : Closing Stopper Failed");
                bayResponse.setStatus(false);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_UNLOADING_007);  // Assuming 602 for failure
            }
        }
        else{
            Unloading.logger.info("S09_let_the_pallet_to_Loading_Bay : Opening Stopper Failed");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_UNLOADING_006);
        }
        //=============================================================

        Unloading.logger.info("S09_let_the_pallet_to_Loading_Bay : Exit");
        return bayResponse;
    }
 
    //============================================================================================================================================

    private Map<String,Object> open_StopLatch_UnloadingBay() {
        Unloading.logger.debug("S09_let_the_pallet_to_Loading_Bay : open_StopLatch_UnloadingBay : Entry");

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        //============================================================================================		 
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.UNLOADING_PORT_NAME_STPR);

        if (portInfo != null) {
            Unloading.logger.debug("PortId    : " + portInfo.getPortId());
            Unloading.logger.debug("ClusterId : " + portInfo.getClusterId());
            Unloading.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Unloading.logger.debug("S09_let_the_pallet_to_Loading_Bay : Stopper Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                                              portInfo.getBayId(),
                                              portInfo.getPortId(),
                                              Constant_IO_ActionMapping.CLOSE);
        status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false; 

		if(StateExecutorController.simulateUnloadingBayHappyPath){
			status = true; 
		}

        Unloading.logger.debug("S09_let_the_pallet_to_Loading_Bay : open_StopLatch_UnloadingBay : status : " + status);
        //============================================================================================   

		responseReturn.put("status", status);
		
        Unloading.logger.debug("S09_let_the_pallet_to_Loading_Bay : open_StopLatch_UnloadingBay : Exit");
        return responseReturn;
    }
 
    //============================================================================================================================================

    private Map<String,Object> close_StopLatch_UnloadingBay() {
        Unloading.logger.debug("S09_let_the_pallet_to_Loading_Bay : close_StopLatch_UnloadingBay : Entry");

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
        //============================================================================================		 
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.UNLOADING_PORT_NAME_STPR);

        if (portInfo != null) {
            Unloading.logger.debug("PortId    : " + portInfo.getPortId());
            Unloading.logger.debug("ClusterId : " + portInfo.getClusterId());
            Unloading.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Unloading.logger.debug("S09_let_the_pallet_to_Loading_Bay : Stopper Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                                              portInfo.getBayId(),
                                              portInfo.getPortId(),
                                              Constant_IO_ActionMapping.OPEN);
        status = state.equals(Constant_IO_ActionMapping.ON) ? true : false; 

		if(StateExecutorController.simulateUnloadingBayHappyPath){
			status = true; 
		}

        Unloading.logger.debug("S09_let_the_pallet_to_Loading_Bay : close_StopLatch_UnloadingBay : status : " + status);
        //============================================================================================   

		responseReturn.put("status", status);
		
        Unloading.logger.debug("S09_let_the_pallet_to_Loading_Bay : close_StopLatch_UnloadingBay : Exit");
        return responseReturn;
    }
    
    //============================================================================================================================================
}
