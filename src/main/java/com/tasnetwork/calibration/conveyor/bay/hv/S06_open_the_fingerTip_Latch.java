package com.tasnetwork.calibration.conveyor.bay.hv;


import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;


public class S06_open_the_fingerTip_Latch implements HvtBayState {

	/**
     * Opens the fingertip latch at the HVT Bay.
     *
     * @return BayResponse indicating success or failure.
     */
    @Override
    public BayResponse handleRequest() {
        Hv.logger.info("S06_open_the_fingerTip_Latch : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        


		Map<String,Object> responseReturn =  open_FingerTipLatch_HvtBay();	 
		boolean open_FingerTipLatch_HvtBay = (boolean)responseReturn.get("status");
	    
        
        if (open_FingerTipLatch_HvtBay) {
            Hv.logger.info("S06_open_the_fingerTip_Latch : Finger Tip Latch Opened");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        } else {
            Hv.logger.info("S06_open_the_fingerTip_Latch : Failed to Open Finger Tip Latch");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_HVT_011);
        }

        Hv.logger.info("S06_open_the_fingerTip_Latch : Exit");
        return bayResponse;
    }
    //============================================================================================================================================  

    private Map<String,Object> open_FingerTipLatch_HvtBay() {
        Hv.logger.debug("S06_open_the_fingerTip_Latch : open_FingerTipLatch_HvtBay : Entry");

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
        //============================================================================================
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.HV_PORT_NAME_FINGER_TIP);
        
        if (portInfo != null) {
            Hv.logger.debug("PortId    : " + portInfo.getPortId());
            Hv.logger.debug("ClusterId : " + portInfo.getClusterId());
            Hv.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Hv.logger.debug("S06_open_the_fingerTip_Latch : Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(), 
                                              portInfo.getBayId(), 
                                              portInfo.getPortId(),
                                              Constant_IO_ActionMapping.CLOSE);
        status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false;     
        
        if(StateExecutorController.simulateHvBayHappyPath){
        	status = true; 
        }
        
        Hv.logger.debug("S06_open_the_fingerTip_Latch : open_FingerTipLatch_HvtBay : status : " + status);
        //============================================================================================   

		responseReturn.put("status", status);
		
        Hv.logger.debug("S06_open_the_fingerTip_Latch : open_FingerTipLatch_HvtBay : Exit");
        return responseReturn;
    }
}
