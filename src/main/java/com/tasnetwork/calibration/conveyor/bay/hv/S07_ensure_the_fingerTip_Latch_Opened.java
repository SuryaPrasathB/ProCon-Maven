package com.tasnetwork.calibration.conveyor.bay.hv;

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

public class S07_ensure_the_fingerTip_Latch_Opened implements HvtBayState {
	public String getMyBayKey() {
		return myBayKey;
	}
  /*  String LOW   = "OPEN";
    String HIGH  = "CLOSE";
    String CLOSE  = "On";
    String OPEN   = "Off";
    String ON  = "On";
 	String OFF  = "Off";    */
    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        Hv.logger.info("S07_ensure_the_fingerTip_Latch_Opened : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
     
		int try_count = 0;
		 
		while(try_count <= 3){

			Map<String,Object> responseReturn =  hvtBay_FingerTipLatch_Status();	 
			String hvtBay_FingerTipLatch_Status = (String)responseReturn.get("status");
		    
			if (hvtBay_FingerTipLatch_Status.equals(Constant_IO_ActionMapping.CLOSE)){  
				
				ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setPalletsLockedImageDisplayOn(getMyBayKey(),false);
				ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopTimeUpDisplay(getMyBayKey());
				
				bayResponse.setStatus(true);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
				break;
			} else {
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_HVT_012);	
				BayUtils.delay(1000);
				try_count++;
			}
		}

        Hv.logger.info("S07_ensure_the_fingerTip_Latch_Opened : Exit");
        return bayResponse;
    }
    //============================================================================================================================================  

    private Map<String, Object> hvtBay_FingerTipLatch_Status() {
        Hv.logger.debug("S07_ensure_the_fingerTip_Latch_Opened : hvtBay_FingerTipLatch_Status : Entry");

        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
        IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.HV_PORT_NAME_SNSR_FINGER_TIP );
        
        if (portInfo != null) {
            Hv.logger.debug("PortId    : " + portInfo.getPortId());
            Hv.logger.debug("ClusterId : " + portInfo.getClusterId());
            Hv.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Hv.logger.debug("S07_ensure_the_fingerTip_Latch_Opened : hvtBay_FingerTipLatch_Status : Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        /*String state = bayUtils.getInputDataFromBay(portInfo.getClusterId(), 
                                                     portInfo.getBayId(), 
                                                     portInfo.getPortId());*/
		
		String state = bayUtils.getInputDataFromBayV2(portInfo) ;
              
        state = state.equals(Constant_IO_ActionMapping.OFF) ? Constant_IO_ActionMapping.CLOSE : Constant_IO_ActionMapping.OPEN;

        if(StateExecutorController.simulateHvBayHappyPath){
        	state = Constant_IO_ActionMapping.CLOSE;
        }
        
		responseReturn.put("status", state);
		
        Hv.logger.debug("S07_ensure_the_fingerTip_Latch_Opened : hvtBay_FingerTipLatch_Status : state : " + state); 
		
        Hv.logger.debug("S07_ensure_the_fingerTip_Latch_Opened : hvtBay_FingerTipLatch_Status : Exit");
        return responseReturn;
    }
}