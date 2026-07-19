package com.tasnetwork.calibration.conveyor.bay.calib;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S09_let_the_pallet_to_Waiting_Bay implements CalibrationBayState {

    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        Calib.logger.info("S09_let_the_pallet_to_Waiting_Bay : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
   
        //=============================================================
        
        Map<String,Object> responseReturn =  open_StopLatch_CalibBay();	 
		boolean open_StopLatch_CalibBay = (boolean)responseReturn.get("status");

        boolean openSuccess = open_StopLatch_CalibBay;
        if(openSuccess) {
            BayUtils.delay(500);
             responseReturn =  close_StopLatch_CalibBay();	 
    		boolean close_StopLatch_CalibBay = (boolean)responseReturn.get("status");

            boolean closeSuccess = close_StopLatch_CalibBay;    
            if(closeSuccess) {
                Calib.logger.info("S09_let_the_pallet_to_Waiting_Bay : Opening and Closing Stopper Success");
                bayResponse.setStatus(true);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
            }    
            else{
                Calib.logger.info("S09_let_the_pallet_to_Waiting_Bay : Closing Stopper Failed");
                bayResponse.setStatus(false);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_009);  // Assuming 602 for failure
            }
        }
        else{
            Calib.logger.info("S09_let_the_pallet_to_Waiting_Bay : Opening Stopper Failed");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_008);
        }
        //=============================================================

        Calib.logger.info("S09_let_the_pallet_to_Waiting_Bay : Exit");
        return bayResponse;
    }
 
    //============================================================================================================================================

    private Map<String,Object> open_StopLatch_CalibBay() {
        Calib.logger.debug("S09_let_the_pallet_to_Waiting_Bay : open_StopLatch_CalibBay : Entry");

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        //============================================================================================		 
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.CALIB_PORT_NAME_STPR);

        if (portInfo != null) {
            Calib.logger.debug("PortId    : " + portInfo.getPortId());
            Calib.logger.debug("ClusterId : " + portInfo.getClusterId());
            Calib.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Calib.logger.debug("S09_let_the_pallet_to_Waiting_Bay : Stopper Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                                              portInfo.getBayId(),
                                              portInfo.getPortId(),
                                              Constant_IO_ActionMapping.CLOSE);
        status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false;
        
        if(StateExecutorController.simulateCalibBayHappyPath){
        	status = true; 
        }
         
        Calib.logger.debug("S09_let_the_pallet_to_Waiting_Bay : open_StopLatch_CalibBay : status : " + status);
        //============================================================================================  
        Calib.logger.debug("S09_let_the_pallet_to_Waiting_Bay : open_StopLatch_CalibBay : Exit");
        return responseReturn;
    }
 
    //============================================================================================================================================

    private Map<String,Object> close_StopLatch_CalibBay() {
        Calib.logger.debug("S09_let_the_pallet_to_Waiting_Bay : close_StopLatch_CalibBay : Entry");

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
        //============================================================================================		 
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.CALIB_PORT_NAME_STPR);

        if (portInfo != null) {
            Calib.logger.debug("PortId    : " + portInfo.getPortId());
            Calib.logger.debug("ClusterId : " + portInfo.getClusterId());
            Calib.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Calib.logger.debug("S09_let_the_pallet_to_Waiting_Bay : Stopper Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                                              portInfo.getBayId(),
                                              portInfo.getPortId(),
                                              Constant_IO_ActionMapping.OPEN);
        status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;
        Calib.logger.debug("S09_let_the_pallet_to_Waiting_Bay : close_StopLatch_CalibBay : status : " + status);
        //============================================================================================  

		responseReturn.put("status", status);
		
        Calib.logger.debug("S09_let_the_pallet_to_Waiting_Bay : close_StopLatch_CalibBay : Exit");
        return responseReturn;
    }
    
    //============================================================================================================================================
}
