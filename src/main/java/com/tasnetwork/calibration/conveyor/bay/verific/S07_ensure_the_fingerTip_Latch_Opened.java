package com.tasnetwork.calibration.conveyor.bay.verific;

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

public class S07_ensure_the_fingerTip_Latch_Opened implements VerificTestBayState {
	

	public String getMyBayKey() {
		return myBayKey;
	}
/*	String LOW   = "OPEN";
	String HIGH  = "CLOSE";
    String CLOSE  = "CLOSE";  
    String OPEN   = "OPEN";   */
    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        Verification.logger.info("S07_ensure_the_fingerTip_Latch_Opened : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
     
		int try_count = 0;
		 
		while(try_count <= 3){

			Map<String,Object> responseReturn =  verificBay_FingerTipLatch_Status();	 
			String verificBay_FingerTipLatch_Status = (String)responseReturn.get("status");
			
			if (verificBay_FingerTipLatch_Status.equals(Constant_IO_ActionMapping.CLOSE)){  
				bayResponse.setStatus(true);
				Verification.logger.debug("S07_ensure_the_fingerTip_Latch_Opened : getMyBayKey(): " +getMyBayKey());
				ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setGroupedPalletsLockedImageDisplayOn(getMyBayKey(),false);
				ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopTimeUpDisplay(getMyBayKey());
				
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
				ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayMonitoringAllPalletsExistInTargetBayIndicator(getMyBayKey());
				
				break;
			} else {
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_012);	
				BayUtils.delay(1000);
				try_count++;
			}
		}

        Verification.logger.info("S07_ensure_the_fingerTip_Latch_Opened : Exit");
        return bayResponse;
    }
    //============================================================================================================================================  

    private Map<String, Object> verificBay_FingerTipLatch_Status() {
        Verification.logger.debug("S07_ensure_the_fingerTip_Latch_Opened : verificBay_FingerTipLatch_Status : Entry");

        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
        IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.VERIFIC_PORT_NAME_SNSR_FINGER_TIP);
        
        if (portInfo != null) {
            Verification.logger.debug("PortId    : " + portInfo.getPortId());
            Verification.logger.debug("ClusterId : " + portInfo.getClusterId());
            Verification.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Verification.logger.debug("S07_ensure_the_fingerTip_Latch_Opened : verificBay_FingerTipLatch_Status : Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        /*String state = bayUtils.getInputDataFromBay(portInfo.getClusterId(), 
                                                     portInfo.getBayId(), 
                                                     portInfo.getPortId());*/
		
		String state = bayUtils.getInputDataFromBayV2(portInfo) ;
              
        state = state.equals(Constant_IO_ActionMapping.OFF) ? Constant_IO_ActionMapping.CLOSE : Constant_IO_ActionMapping.OPEN;
        
		if(StateExecutorController.simulateVerificBayHappyPath){
			state = Constant_IO_ActionMapping.CLOSE;
		}

        Verification.logger.debug("S07_ensure_the_fingerTip_Latch_Opened : verificBay_FingerTipLatch_Status : state : " + state);
        
		responseReturn.put("status", state);

        Verification.logger.debug("S07_ensure_the_fingerTip_Latch_Opened : verificBay_FingerTipLatch_Status : Exit");
        return responseReturn;
    }
}
