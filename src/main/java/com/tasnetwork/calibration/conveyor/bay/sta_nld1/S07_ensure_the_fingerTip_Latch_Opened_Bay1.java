package com.tasnetwork.calibration.conveyor.bay.sta_nld1;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S07_ensure_the_fingerTip_Latch_Opened_Bay1 implements STA_NoLoadTestBay1State {
	public String getMyBayKey() {
		return myBayKey;
	}
	/*String LOW   = "OPEN";
	String HIGH  = "CLOSE";
    String CLOSE  = "CLOSE";  
    String OPEN   = "OPEN";  */ 
    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        StaNld_Bay1.logger.info("S07_ensure_the_fingerTip_Latch_Opened : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
     
		int try_count = 0;

		while(try_count <= 3){

			Map<String,Object> responseReturn =  sctNltBay1_FingerTipLatch_Status();	 
			String sctNltBay1_FingerTipLatch_Status = (String)responseReturn.get("status");
		    
			if (sctNltBay1_FingerTipLatch_Status.equals(Constant_IO_ActionMapping.CLOSE)){  
				
				ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setGroupedPalletsLockedImageDisplayOn(getMyBayKey(),false);
				ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopTimeUpDisplay(getMyBayKey());
				
				bayResponse.setStatus(true);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
				break;
			} else {
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_012);	
				BayUtils.delay(1000);
				try_count++;
			}
		}

        StaNld_Bay1.logger.info("S07_ensure_the_fingerTip_Latch_Opened : Exit");
        return bayResponse;
    }
    //============================================================================================================================================  

    private Map<String, Object> sctNltBay1_FingerTipLatch_Status() {
        StaNld_Bay1.logger.debug("S07_ensure_the_fingerTip_Latch_Opened : sctNltBay1_FingerTipLatch_Status : Entry");

        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
        IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.SCT_NLT_BAY1_SNSR_FINGER_TIP);
        
        if (portInfo != null) {
            StaNld_Bay1.logger.debug("PortId    : " + portInfo.getPortId());
            StaNld_Bay1.logger.debug("ClusterId : " + portInfo.getClusterId());
            StaNld_Bay1.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            StaNld_Bay1.logger.debug("S07_ensure_the_fingerTip_Latch_Opened : sctNltBay1_FingerTipLatch_Status : Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        /*String state = bayUtils.getInputDataFromBay(portInfo.getClusterId(), 
                                                     portInfo.getBayId(), 
                                                     portInfo.getPortId());*/
		
		String state = bayUtils.getInputDataFromBayV2(portInfo) ;
              
        state = state.equals(Constant_IO_ActionMapping.OFF) ? Constant_IO_ActionMapping.CLOSE : Constant_IO_ActionMapping.OPEN;

        if(StateExecutorController.simulateSCTNLTBay1HappyPath){
        	state = Constant_IO_ActionMapping.OPEN; 
        }
         
        StaNld_Bay1.logger.debug("S07_ensure_the_fingerTip_Latch_Opened : sctNltBay1_FingerTipLatch_Status : state : " + state); 

		responseReturn.put("status", state);
		
        StaNld_Bay1.logger.debug("S07_ensure_the_fingerTip_Latch_Opened : sctNltBay1_FingerTipLatch_Status : Exit");
        return responseReturn;
    }
}
