package com.tasnetwork.calibration.conveyor.bay.ir;

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

public class S09_let_the_pallet_to_CALIB_Bay implements IrtBayState {

 /*   String LOW    = "OPEN";
    String HIGH   = "CLOSE";
    String CLOSE  = "On";
    String OPEN   = "Off";
    String ON  = "On";
 	String OFF  = "Off";*/
    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        Ir.logger.info("S09_let_the_pallet_to_CALIB_Bay : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
   
        //=============================================================

		Map<String,Object> responseReturn =  open_StopLatch_IrtBay();	 
		boolean open_StopLatch_IrtBay = (boolean)responseReturn.get("status");
	    
        boolean openSuccess = open_StopLatch_IrtBay;
        if(openSuccess) {
            BayUtils.delay(500);

    		responseReturn =  close_StopLatch_IrtBay();	 
    		boolean close_StopLatch_IrtBay = (boolean)responseReturn.get("status");
    	    
            boolean closeSuccess = close_StopLatch_IrtBay;    
            if(closeSuccess) {
                Ir.logger.info("S09_let_the_pallet_to_CALIB_Bay : Opening and Closing Stopper Success");
                bayResponse.setStatus(true);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
            }    
            else{
                Ir.logger.info("S09_let_the_pallet_to_CALIB_Bay : Closing Stopper Failed");
                bayResponse.setStatus(false);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_IRT_009);  // Assuming 602 for failure
            }
        }
        else{
            Ir.logger.info("S09_let_the_pallet_to_CALIB_Bay : Opening Stopper Failed");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_IRT_008);
        }
        //=============================================================

        Ir.logger.info("S09_let_the_pallet_to_CALIB_Bay : Exit");
        return bayResponse;
    }
 
    //============================================================================================================================================

    private Map<String,Object> open_StopLatch_IrtBay() {
        Ir.logger.debug("S09_let_the_pallet_to_CALIB_Bay : open_StopLatch_IrtBay : Entry");

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        //============================================================================================		 
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.IR_PORT_NAME_STPR);

        if (portInfo != null) {
            Ir.logger.debug("PortId    : " + portInfo.getPortId());
            Ir.logger.debug("ClusterId : " + portInfo.getClusterId());
            Ir.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Ir.logger.debug("S09_let_the_pallet_to_CALIB_Bay : Stopper Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                                              portInfo.getBayId(),
                                              portInfo.getPortId(),
                                              Constant_IO_ActionMapping.CLOSE);
        status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false;
        
        if(StateExecutorController.simulateIrBayHappyPath){
			status = true; 
		}
        
        Ir.logger.debug("S09_let_the_pallet_to_CALIB_Bay : open_StopLatch_IrtBay : status : " + status);
        //============================================================================================  
        responseReturn.put("status", status);
        
        Ir.logger.debug("S09_let_the_pallet_to_CALIB_Bay : open_StopLatch_IrtBay : Exit");
        return responseReturn;
    }
 
    //============================================================================================================================================

    private Map<String,Object> close_StopLatch_IrtBay() {
        Ir.logger.debug("S09_let_the_pallet_to_CALIB_Bay : close_StopLatch_IrtBay : Entry");

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
        //============================================================================================		 
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.IR_PORT_NAME_STPR);

        if (portInfo != null) {
            Ir.logger.debug("PortId    : " + portInfo.getPortId());
            Ir.logger.debug("ClusterId : " + portInfo.getClusterId());
            Ir.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Ir.logger.debug("S09_let_the_pallet_to_CALIB_Bay : Stopper Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                                              portInfo.getBayId(),
                                              portInfo.getPortId(),
                                              Constant_IO_ActionMapping.OPEN);
        status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;
        Ir.logger.debug("S09_let_the_pallet_to_CALIB_Bay : close_StopLatch_IrtBay : status : " + status);
        //============================================================================================   

		responseReturn.put("status", status);
		
        Ir.logger.debug("S09_let_the_pallet_to_CALIB_Bay : close_StopLatch_IrtBay : Exit");
        return responseReturn;
    }
    
    //============================================================================================================================================
}
