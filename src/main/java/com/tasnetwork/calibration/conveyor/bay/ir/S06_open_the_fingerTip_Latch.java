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

public class S06_open_the_fingerTip_Latch implements IrtBayState {
    
  /*  String LOW   = "OPEN";
    String HIGH  = "CLOSE";
    String CLOSE  = "On";
    String OPEN   = "Off";
    String ON  = "On";
 	String OFF  = "Off";*/
    
    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        Ir.logger.info("S06_open_the_fingerTip_Latch : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        

		Map<String,Object> responseReturn =  open_FingerTipLatch_IrtBay();	 
		boolean open_FingerTipLatch_IrtBay = (boolean)responseReturn.get("status");
	    
        if (open_FingerTipLatch_IrtBay) {
            Ir.logger.info("S06_open_the_fingerTip_Latch : Finger Tip Latch Opened");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        } else {
            Ir.logger.info("S06_open_the_fingerTip_Latch : Failed to Open Finger Tip Latch");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_IRT_011);
        }

        Ir.logger.info("S06_open_the_fingerTip_Latch : Exit");
        return bayResponse;
    }
    //============================================================================================================================================  

    private Map<String,Object> open_FingerTipLatch_IrtBay() {
        Ir.logger.debug("S06_open_the_fingerTip_Latch : open_FingerTipLatch_IrtBay : Entry");

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
        //============================================================================================
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.IR_PORT_NAME_FINGER_TIP);
        
        if (portInfo != null) {
            Ir.logger.debug("PortId    : " + portInfo.getPortId());
            Ir.logger.debug("ClusterId : " + portInfo.getClusterId());
            Ir.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Ir.logger.debug("S06_open_the_fingerTip_Latch : open_FingerTipLatch_IrtBay : Output port not found");
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
        
        Ir.logger.debug("S06_open_the_fingerTip_Latch : open_FingerTipLatch_IrtBay : status : " + status);
        //============================================================================================   

		responseReturn.put("status", status);
		
        Ir.logger.debug("S06_open_the_fingerTip_Latch : open_FingerTipLatch_IrtBay : Exit");
        return responseReturn;
    }
}
