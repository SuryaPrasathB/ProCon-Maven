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

public class S06_open_the_fingerTip_Latch_Bay1 implements STA_NoLoadTestBay1State {
    
    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        StaNld_Bay1.logger.info("S06_open_the_fingerTip_Latch : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		Map<String,Object> responseReturn =  open_FingerTipLatch_SctNltBay1();	 
		boolean open_FingerTipLatch_SctNltBay1 = (boolean)responseReturn.get("status");
	    
        if (open_FingerTipLatch_SctNltBay1) {
            StaNld_Bay1.logger.info("S06_open_the_fingerTip_Latch : Finger Tip Latch Opened");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        } else {
            StaNld_Bay1.logger.info("S06_open_the_fingerTip_Latch : Failed to Open Finger Tip Latch");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_011);
            return bayResponse ;
        }

        StaNld_Bay1.logger.info("S06_open_the_fingerTip_Latch : Exit");
        return bayResponse;
    }
    //============================================================================================================================================  

    private Map<String,Object> open_FingerTipLatch_SctNltBay1() {
        StaNld_Bay1.logger.debug("S06_open_the_fingerTip_Latch : open_FingerTipLatch_SctNltBay1 : Entry");

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
        //============================================================================================
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.SCT_NLT1_PORT_NAME_FINGER_TIP);
        
        if (portInfo != null) {
            StaNld_Bay1.logger.debug("PortId    : " + portInfo.getPortId());
            StaNld_Bay1.logger.debug("ClusterId : " + portInfo.getClusterId());
            StaNld_Bay1.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            StaNld_Bay1.logger.debug("S06_open_the_fingerTip_Latch : open_FingerTipLatch_SctNltBay1 : Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(), 
                                              portInfo.getBayId(), 
                                              portInfo.getPortId(),
                                              Constant_IO_ActionMapping.CLOSE);
        status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false;  
        
        if(StateExecutorController.simulateSCTNLTBay1HappyPath){
        	status = true; 
        }
         
        StaNld_Bay1.logger.debug("S06_open_the_fingerTip_Latch : open_FingerTipLatch_SctNltBay1 : status : " + status);
        //============================================================================================   

		responseReturn.put("status", status);
		
        StaNld_Bay1.logger.debug("S06_open_the_fingerTip_Latch : open_FingerTipLatch_SctNltBay1 : Exit");
        return responseReturn;
    }
}
