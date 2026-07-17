package com.tasnetwork.calibration.conveyor.bay.calib;

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

public class S07_ensure_the_fingerTip_Latch_Opened implements CalibrationBayState {
	public String getMyBayKey() {
		return myBayKey;
	}
/*	String LOW   = "OPEN";
	String HIGH  = "CLOSE";
    String CLOSE  = "CLOSE";  
    String OPEN   = "OPEN";*/   
    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        Calib.logger.info("S07_ensure_the_fingerTip_Latch_Opened : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
     
		int try_count = 0;
		 
		while(try_count <= 3 && !Calib.isStopProcessRequestedCalibBay()){
			
			Map<String,Object> responseReturn =  calibBay_FingerTipLatch_Status();	 
			String calibBay_FingerTipLatch_Status = (String)responseReturn.get("status");

			if (calibBay_FingerTipLatch_Status.equals(Constant_IO_ActionMapping.CLOSE)){  
				
				ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setPalletsLockedImageDisplayOn(getMyBayKey(),false);
				ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopTimeUpDisplay(getMyBayKey());
				
				bayResponse.setStatus(true);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
				break;
			} else {
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_012);	
				BayUtils.delay(1000);
				try_count++;
			}
		}

        Calib.logger.info("S07_ensure_the_fingerTip_Latch_Opened : Exit");
        return bayResponse;
    }
    //============================================================================================================================================  

    private Map<String, Object> calibBay_FingerTipLatch_Status() {
        Calib.logger.debug("S07_ensure_the_fingerTip_Latch_Opened : calibBay_FingerTipLatch_Status : Entry");

        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
        IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.CALIB_PORT_NAME_SNSR_FINGER_TIP);
        
        if (portInfo != null) {
            Calib.logger.debug("PortId    : " + portInfo.getPortId());
            Calib.logger.debug("ClusterId : " + portInfo.getClusterId());
            Calib.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Calib.logger.debug("S07_ensure_the_fingerTip_Latch_Opened : calibBay_FingerTipLatch_Status : Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        String state = bayUtils.getInputDataFromBayV2(portInfo);
              
        state = state.equals(Constant_IO_ActionMapping.OFF) ? Constant_IO_ActionMapping.CLOSE : Constant_IO_ActionMapping.OPEN;

        if(StateExecutorController.simulateCalibBayHappyPath){
        	state = Constant_IO_ActionMapping.CLOSE;
        }
        
        
                 
        Calib.logger.debug("S07_ensure_the_fingerTip_Latch_Opened : calibBay_FingerTipLatch_Status : state : " + state);

		responseReturn.put("status", state);
		
        Calib.logger.debug("S07_ensure_the_fingerTip_Latch_Opened : calibBay_FingerTipLatch_Status : Exit");
        return responseReturn;
    }
}
