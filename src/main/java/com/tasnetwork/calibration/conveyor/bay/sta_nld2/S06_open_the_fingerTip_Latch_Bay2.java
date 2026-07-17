package com.tasnetwork.calibration.conveyor.bay.sta_nld2;

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

public class S06_open_the_fingerTip_Latch_Bay2 implements STA_NoLoadTestBay2State {
    
/*    String LOW   = "OPEN";
    String HIGH  = "CLOSE";
    String CLOSE  = "On";
    String OPEN   = "Off";
    String ON  = "On";
 	String OFF  = "Off";*/
    
    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        StaNld_Bay2.logger.info("S06_open_the_fingerTip_Latch : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		Map<String,Object> responseReturn =  open_FingerTipLatch_SctNlyBay2();	 
		boolean open_FingerTipLatch_SctNlyBay2 = (boolean)responseReturn.get("status");
	    
        if (open_FingerTipLatch_SctNlyBay2) {
            StaNld_Bay2.logger.info("S06_open_the_fingerTip_Latch : Finger Tip Latch Opened");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        } else {
            StaNld_Bay2.logger.info("S06_open_the_fingerTip_Latch : Failed to Open Finger Tip Latch");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_011);
        }

        StaNld_Bay2.logger.info("S06_open_the_fingerTip_Latch : Exit");
        return bayResponse;
    }
    //============================================================================================================================================  

    private Map<String,Object> open_FingerTipLatch_SctNlyBay2() {
        StaNld_Bay2.logger.debug("S06_open_the_fingerTip_Latch : open_FingerTipLatch_SctNlyBay2 : Entry");

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
        //============================================================================================
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.SCT_NLT2_PORT_NAME_FINGER_TIP);
        
        if (portInfo != null) {
            StaNld_Bay2.logger.debug("PortId    : " + portInfo.getPortId());
            StaNld_Bay2.logger.debug("ClusterId : " + portInfo.getClusterId());
            StaNld_Bay2.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            StaNld_Bay2.logger.debug("S06_open_the_fingerTip_Latch : open_FingerTipLatch_SctNlyBay2 : Output port not found");
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
         
        StaNld_Bay2.logger.debug("S06_open_the_fingerTip_Latch : open_FingerTipLatch_SctNlyBay2 : status : " + status);
        //============================================================================================   

		responseReturn.put("status", status);
		
        StaNld_Bay2.logger.debug("S06_open_the_fingerTip_Latch : open_FingerTipLatch_SctNlyBay2 : Exit");
        return responseReturn;
    }
}
