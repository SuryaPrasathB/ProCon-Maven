package com.tasnetwork.calibration.conveyor.bay.loading;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S02_let_pallet_outside_loading_bay implements LoadingBayState {

    /**
     * Lets the pallet move outside the Loading Bay.
     *
     * @return BayResponse indicating success or failure.
     */
    @Override
    public BayResponse handleRequest() {
        Loading.logger.info("S02_let_pallet_outside_loading_bay : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
   
        //=============================================================

		Map<String,Object> responseReturn =  open_StopLatch_LoadingBay();	 
		boolean open_StopLatch_LoadingBay = (boolean)responseReturn.get("status");
	    
        boolean openSuccess = open_StopLatch_LoadingBay;
        if(openSuccess) {
            BayUtils.delay(500);

    	 responseReturn =  close_StopLatch_LoadingBay();	 
    		boolean close_StopLatch_LoadingBay = (boolean)responseReturn.get("status");
    	    
            boolean closeSuccess = close_StopLatch_LoadingBay;    
            if(closeSuccess) {
                Loading.logger.info("S02_let_pallet_outside_loading_bay : Opening and Closing Stopper Success");
                bayResponse.setStatus(true);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
            }    
            else{
                Loading.logger.info("S02_let_pallet_outside_loading_bay : Closing Stopper Failed");
                bayResponse.setStatus(false);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_LOADING_005);  // Assuming 102 for failure
            }
        }
        else{
            Loading.logger.info("S02_let_pallet_outside_loading_bay : Opening Stopper Failed");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_LOADING_004);
        }
        //=============================================================

        Loading.logger.info("S02_let_pallet_outside_loading_bay : Exit");
        return bayResponse;
    }
 
    //============================================================================================================================================

    private Map<String,Object> open_StopLatch_LoadingBay() {
        Loading.logger.debug("S02_let_pallet_outside_loading_bay : open_StopLatch_LoadingBay : Entry");

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        //============================================================================================		 
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.LOADING_PORT_NAME_STPR);

        if (portInfo != null) {
            Loading.logger.debug("PortId    : " + portInfo.getPortId());
            Loading.logger.debug("ClusterId : " + portInfo.getClusterId());
            Loading.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Loading.logger.debug("S02_let_pallet_outside_loading_bay : Stopper Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                                              portInfo.getBayId(),
                                              portInfo.getPortId(),
                                              Constant_IO_ActionMapping.CLOSE);
        status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;
        
		if(StateExecutorController.simulateLoadingBayHappyPath){
			status = true; 
		}
 
        Loading.logger.debug("S02_let_pallet_outside_loading_bay : open_StopLatch_LoadingBay : status : " + status);
        //============================================================================================  
        Loading.logger.debug("S02_let_pallet_outside_loading_bay : open_StopLatch_LoadingBay : Exit");
        return responseReturn;
    }
 
    //============================================================================================================================================

    private Map<String,Object> close_StopLatch_LoadingBay() {
        Loading.logger.debug("S02_let_pallet_outside_loading_bay : close_StopLatch_LoadingBay : Entry");

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
        //============================================================================================		 
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.LOADING_PORT_NAME_STPR);

        if (portInfo != null) {
            Loading.logger.debug("PortId    : " + portInfo.getPortId());
            Loading.logger.debug("ClusterId : " + portInfo.getClusterId());
            Loading.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Loading.logger.debug("S02_let_pallet_outside_loading_bay : Stopper Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                                              portInfo.getBayId(),
                                              portInfo.getPortId(),
                                              Constant_IO_ActionMapping.OPEN);
        status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;
        
		if(StateExecutorController.simulateLoadingBayHappyPath){
			status = true; 
		}
 
        Loading.logger.debug("S02_let_pallet_outside_loading_bay : close_StopLatch_LoadingBay : status : " + status);
        //============================================================================================   

		responseReturn.put("status", status);
		
        Loading.logger.debug("S02_let_pallet_outside_loading_bay : close_StopLatch_LoadingBay : Exit");
        return responseReturn;
    }
    
    //============================================================================================================================================
}

