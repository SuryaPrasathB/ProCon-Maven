package com.tasnetwork.calibration.conveyor.bay.verific;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S04_ensure_the_fingerTip_Latch_Closed implements VerificTestBayState {
 
    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
    	BayUtils.delay(1000);
    	
        Verification.logger.info("S04_ensure_the_fingerTip_Latch_Closed : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
     
		int try_count = 0;
	    
		while(try_count <= 3){

			Map<String,Object> responseReturn =  verificBay_FingerTipLatch_Status();	 
			String verificBay_FingerTipLatch_Status = (String)responseReturn.get("status");
		    
			if (verificBay_FingerTipLatch_Status.equals(Constant_IO_ActionMapping.OPEN)){ 
				
				Verification.logger.debug("S04_ensure_the_fingerTip_Latch_Closed : getMyBayKey(): " +getMyBayKey());
				ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setGroupedPalletsLockedImageDisplayOn(getMyBayKey(),true);
				ConveyorDataManager.getDashboardObject().getBayIndicatorManager().startTimeUpDisplay(getMyBayKey());
				
				if (ProconFeatureEnable.MOTOR_CONTROL_ENABLE) {
					BayUtils bayUtils = new BayUtils();
					responseReturn = bayUtils.set_motor_not_required(getMyBayKey());
					boolean set_motor_not_required = (boolean) responseReturn.get("status");
					if (set_motor_not_required) {
						Verification.logger.info("set_motor_not_required : Success");
						bayResponse.setStatus(true);
						bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
					} else {
						Verification.logger.info("Failed to set_motor_not_required ");
						bayResponse.setStatus(false);
						bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_026);
					} 
				}
//				bayResponse.setStatus(true);
//				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
				break;
			} else {
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_004);	
				BayUtils.delay(1000);
				try_count++;
			}
		}

        Verification.logger.info("S04_ensure_the_fingerTip_Latch_Closed : Exit");
        return bayResponse;
    }
    //============================================================================================================================================  

    private Map<String, Object> verificBay_FingerTipLatch_Status() {
        Verification.logger.debug("S04_ensure_the_fingerTip_Latch_Closed : verificBay_FingerTipLatch_Status : Entry");

        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
        IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.VERIFIC_PORT_NAME_SNSR_FINGER_TIP);
        
        if (portInfo != null) {
            Verification.logger.debug("PortId    : " + portInfo.getPortId());
            Verification.logger.debug("ClusterId : " + portInfo.getClusterId());
            Verification.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Verification.logger.debug("S04_ensure_the_fingerTip_Latch_Closed : verificBay_FingerTipLatch_Status : Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();

		String state = bayUtils.getInputDataFromBayV2(portInfo) ;
              
        state = state.equals(Constant_IO_ActionMapping.ON) ? Constant_IO_ActionMapping.OPEN : Constant_IO_ActionMapping.CLOSE;
        

		if(StateExecutorController.simulateVerificBayHappyPath){
			state = Constant_IO_ActionMapping.OPEN;
		}

        Verification.logger.debug("S04_ensure_the_fingerTip_Latch_Closed : verificBay_FingerTipLatch_Status : state : " + state);
      
		responseReturn.put("status", state);

        Verification.logger.debug("S04_ensure_the_fingerTip_Latch_Closed : verificBay_FingerTipLatch_Status : Exit");
        return responseReturn;
    }
}
