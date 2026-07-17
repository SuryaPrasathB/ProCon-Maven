package com.tasnetwork.calibration.conveyor.bay.hv;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S09_let_the_pallet_to_IRT_Bay implements HvtBayState {

  /*  String LOW    = "OPEN";
    String HIGH   = "CLOSE";
    String CLOSE  = "Off";
    String OPEN   = "On";
    String ON  = "On";
 	String OFF  = "Off";*/
    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        Hv.logger.info("S09_let_the_pallet_to_IRT_Bay : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
   
        //=============================================================

		Map<String,Object> responseReturn =  open_StopLatch_HvtBay();	 
		boolean open_StopLatch_HvtBay = (boolean)responseReturn.get("status");
	    
        boolean openSuccess = open_StopLatch_HvtBay;
        
        if(openSuccess) {
            BayUtils.delay(2500);

    		  responseReturn =  close_StopLatch_HvtBay();	 
    		boolean close_StopLatch_HvtBay = (boolean)responseReturn.get("status");
    	    
            boolean closeSuccess = close_StopLatch_HvtBay;    
            if(closeSuccess) {
                Hv.logger.info("S09_let_the_pallet_to_IRT_Bay : Opening and Closing Stopper Success");
                bayResponse.setStatus(true);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
            }    
            else{
                Hv.logger.info("S09_let_the_pallet_to_IRT_Bay : Closing Stopper Failed");
                bayResponse.setStatus(false);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_HVT_009);  // Assuming 602 for failure
            }
        }
        else{
            Hv.logger.info("S09_let_the_pallet_to_IRT_Bay : Opening Stopper Failed");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_HVT_008);
        }
        //=============================================================

        Hv.logger.info("S09_let_the_pallet_to_IRT_Bay : Exit");
        return bayResponse;
    }
 
    //============================================================================================================================================

    private Map<String,Object> open_StopLatch_HvtBay() {
        Hv.logger.debug("S09_let_the_pallet_to_IRT_Bay : open_StopLatch_HvtBay : Entry");

        boolean status = false;
    	Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        //============================================================================================		 
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.HV_PORT_NAME_STPR);

        if (portInfo != null) {
            Hv.logger.debug("PortId    : " + portInfo.getPortId());
            Hv.logger.debug("ClusterId : " + portInfo.getClusterId());
            Hv.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Hv.logger.debug("S09_let_the_pallet_to_IRT_Bay : Stopper Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                                              portInfo.getBayId(),
                                              portInfo.getPortId(),
                                              Constant_IO_ActionMapping.CLOSE);
        status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false;
        Hv.logger.debug("S09_let_the_pallet_to_IRT_Bay : open_StopLatch_HvtBay : status : " + status);
        //============================================================================================  
        Hv.logger.debug("S09_let_the_pallet_to_IRT_Bay : open_StopLatch_HvtBay : Exit");
        return responseReturn;
    }
 
    //============================================================================================================================================

    private Map<String,Object> close_StopLatch_HvtBay() {
        Hv.logger.debug("S09_let_the_pallet_to_IRT_Bay : close_StopLatch_HvtBay : Entry");

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
        //============================================================================================		 
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.HV_PORT_NAME_STPR);

        if (portInfo != null) {
            Hv.logger.debug("PortId    : " + portInfo.getPortId());
            Hv.logger.debug("ClusterId : " + portInfo.getClusterId());
            Hv.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Hv.logger.debug("S09_let_the_pallet_to_IRT_Bay : Stopper Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                                              portInfo.getBayId(),
                                              portInfo.getPortId(),
                                              Constant_IO_ActionMapping.OPEN);
        status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;
        Hv.logger.debug("S09_let_the_pallet_to_IRT_Bay : close_StopLatch_HvtBay : status : " + status);
        //============================================================================================   

		responseReturn.put("status", status);
		
        Hv.logger.debug("S09_let_the_pallet_to_IRT_Bay : close_StopLatch_HvtBay : Exit");
        return responseReturn;
    }
    
    //============================================================================================================================================
}
