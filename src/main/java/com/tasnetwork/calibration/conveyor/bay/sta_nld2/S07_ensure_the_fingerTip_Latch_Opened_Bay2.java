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
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S07_ensure_the_fingerTip_Latch_Opened_Bay2 implements STA_NoLoadTestBay2State {
	
	public String getMyBayKey() {
		return myBayKey;
	}
/*	String LOW   = "OPEN";
	String HIGH  = "CLOSE";
    String CLOSE  = "CLOSE";  
    String OPEN   = "OPEN";  */ 
    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        StaNld_Bay2.logger.info("S07_ensure_the_fingerTip_Latch_Opened : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
     
		int try_count = 0;
		 
		while(try_count <= 3){

			Map<String,Object> responseReturn =  sctNltBay2_FingerTipLatch_Status();	 
			String sctNltBay2_FingerTipLatch_Status = (String)responseReturn.get("responseData");
		    
			if (sctNltBay2_FingerTipLatch_Status.equals(Constant_IO_ActionMapping.CLOSE)){  
				ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setGroupedPalletsLockedImageDisplayOn(getMyBayKey(),false);
				ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopTimeUpDisplay(getMyBayKey());
				
				bayResponse.setStatus(true);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
				break;
			} else {
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_012);	
				BayUtils.delay(1000);
				try_count++;
			}
		}

        StaNld_Bay2.logger.info("S07_ensure_the_fingerTip_Latch_Opened : Exit");
        return bayResponse;
    }
    //============================================================================================================================================  

    private Map<String, Object> sctNltBay2_FingerTipLatch_Status() {
        StaNld_Bay2.logger.debug("S07_ensure_the_fingerTip_Latch_Opened : sctNltBay2_FingerTipLatch_Status : Entry");

        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
        IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.SCT_NLT_BAY2_SNSR_FINGER_TIP );
        
        if (portInfo != null) {
            StaNld_Bay2.logger.debug("PortId    : " + portInfo.getPortId());
            StaNld_Bay2.logger.debug("ClusterId : " + portInfo.getClusterId());
            StaNld_Bay2.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            StaNld_Bay2.logger.debug("S07_ensure_the_fingerTip_Latch_Opened : sctNltBay2_FingerTipLatch_Status : Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
       /* String state = bayUtils.getInputDataFromBay(portInfo.getClusterId(), 
                                                     portInfo.getBayId(), 
                                                     portInfo.getPortId());*/
		
		String state = bayUtils.getInputDataFromBayV2(portInfo) ;
              
        state = state.equals(Constant_IO_ActionMapping.OFF) ? Constant_IO_ActionMapping.CLOSE : Constant_IO_ActionMapping.OPEN;

        if(state.equals(Constant_IO_ActionMapping.CLOSE)){
			//testIntefaceStatus.setDeviceResponseStatus("Success");
			responseReturn.put("status", true);
		}else{
			//testIntefaceStatus.setDeviceResponseStatus("Failed");
			responseReturn.put("status", false);
		}
        
        if(StateExecutorController.simulateSCTNLTBay2HappyPath){
        	state = Constant_IO_ActionMapping.OPEN; 
        }
        
		//responseReturn.put("status", state);
		responseReturn.put("responseData", state); 
		
		
        StaNld_Bay2.logger.debug("S07_ensure_the_fingerTip_Latch_Opened : sctNltBay2_FingerTipLatch_Status : state : " + state); 

		
        StaNld_Bay2.logger.debug("S07_ensure_the_fingerTip_Latch_Opened : sctNltBay2_FingerTipLatch_Status : Exit");
        return responseReturn;
    }
}
