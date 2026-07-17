package com.tasnetwork.calibration.conveyor.bay.ir;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
//import com.tasnetwork.calibration.conveyor.bay_highvoltagetest.HighVoltageTestBay;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S04_ensure_the_fingerTip_Latch_Closed implements IrtBayState {
	
/*	String LOW   = "OPEN";
	String HIGH  = "CLOSE";
    String CLOSE  = "CLOSE";  
    String OPEN   = "OPEN"; */  
	
	public String getMyBayKey() {
		return myBayKey;
	}
	
    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        Ir.logger.info("S04_ensure_the_fingerTip_Latch_Closed : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        
        /*for(int i = 0; i < 60; i ++) {
        	InsulationResistanceTestBay.logger.info("S11_idle_condition : Waiting IR for 1 min" + myBayKey);
			BayUtils.delay(1000);
		}*/
     
		int try_count = 0;
		 
		while(try_count <= 3){

			Map<String,Object> responseReturn =  irtBay_FingerTipLatch_Status();	 
			String irtBay_FingerTipLatch_Status = (String)responseReturn.get("status");
		    
			if (irtBay_FingerTipLatch_Status.equals(Constant_IO_ActionMapping.OPEN)){  
				ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setPalletsLockedImageDisplayOn(getMyBayKey(),true);
				ConveyorDataManager.getDashboardObject().getBayIndicatorManager().startTimeUpDisplay(getMyBayKey());
				
				if (ProconFeatureEnable.MOTOR_CONTROL_ENABLE) {
					BayUtils bayUtils = new BayUtils();
					responseReturn = bayUtils.set_motor_not_required(getMyBayKey());
					boolean set_motor_not_required = (boolean) responseReturn.get("status");
					if (set_motor_not_required) {
						Ir.logger.info("set_motor_not_required : Success");
						
						//workaround added delay for the S03_close_the_fingerTip_Latch - IR finger - #Gopi-09-06-2025 
						
				        BayUtils.delay(10000);
						
						bayResponse.setStatus(true);
						bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
					} else {
						Ir.logger.info("Failed to set_motor_not_required ");
						bayResponse.setStatus(false);
						bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_026);
					} 
				}
//				bayResponse.setStatus(true);
//				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
				break;
			} else {
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_IRT_004);	
				BayUtils.delay(1000);
				try_count++;
			}
		}

        Ir.logger.info("S04_ensure_the_fingerTip_Latch_Closed : Exit");
        return bayResponse;
    }
    //============================================================================================================================================  

    private Map<String, Object> irtBay_FingerTipLatch_Status() {
        Ir.logger.debug("S04_ensure_the_fingerTip_Latch_Closed : irtBay_FingerTipLatch_Status : Entry");

        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
        IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.IR_PORT_NAME_SNSR_FINGER_TIP);
        
        if (portInfo != null) {
            Ir.logger.debug("PortId    : " + portInfo.getPortId());
            Ir.logger.debug("ClusterId : " + portInfo.getClusterId());
            Ir.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Ir.logger.debug("S04_ensure_the_fingerTip_Latch_Closed : irtBay_FingerTipLatch_Status : Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        /*String state = bayUtils.getInputDataFromBay(portInfo.getClusterId(), 
                                                     portInfo.getBayId(), 
                                                     portInfo.getPortId());*/
		
		String state = bayUtils.getInputDataFromBayV2(portInfo) ;
              
        state = state.equals(Constant_IO_ActionMapping.ON) ? Constant_IO_ActionMapping.OPEN : Constant_IO_ActionMapping.CLOSE;

        if(StateExecutorController.simulateCommBayHappyPath){
        	state = Constant_IO_ActionMapping.OPEN;
        }
        
        Ir.logger.debug("S04_ensure_the_fingerTip_Latch_Closed : irtBay_FingerTipLatch_Status : state : " + state); 

		responseReturn.put("status", state);
		
		
        Ir.logger.debug("S04_ensure_the_fingerTip_Latch_Closed : irtBay_FingerTipLatch_Status : Exit");
        return responseReturn;
    }
}
