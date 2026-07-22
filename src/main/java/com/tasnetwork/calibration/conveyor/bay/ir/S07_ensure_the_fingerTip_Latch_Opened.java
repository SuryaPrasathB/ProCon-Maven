package com.tasnetwork.calibration.conveyor.bay.ir;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S07_ensure_the_fingerTip_Latch_Opened implements IrtBayState {
	/**
     * Ensures the fingertip latch is opened at the IRT Bay.
     *
     * @return BayResponse indicating success or failure.
     */
    @Override
    public BayResponse handleRequest() {
        Ir.logger.info("S07_ensure_the_fingerTip_Latch_Opened : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
     
		int try_count = 0;
		 
		while(try_count <= 3 && !Ir.isStopProcessRequestedIrtBay() && !ConstantConveyor.ALL_LOOP_BREAK_FLAG){

			Map<String,Object> responseReturn =  irtBay_FingerTipLatch_Status();	 
			String irtBay_FingerTipLatch_Status = (String)responseReturn.get("status");
		    
			if (irtBay_FingerTipLatch_Status.equals(Constant_IO_ActionMapping.CLOSE)){  
				ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setPalletsLockedImageDisplayOn(getMyBayKey(),false);
				ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopTimeUpDisplay(getMyBayKey());
				
				bayResponse.setStatus(true);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
				break;
			} else {
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_IRT_012);	
				BayUtils.delay(1000);
				try_count++;
			}
		}

        Ir.logger.info("S07_ensure_the_fingerTip_Latch_Opened : Exit");
        return bayResponse;
    }
    //============================================================================================================================================  

    private Map<String, Object> irtBay_FingerTipLatch_Status() {
        Ir.logger.debug("S07_ensure_the_fingerTip_Latch_Opened : irtBay_FingerTipLatch_Status : Entry");

        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
        IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.IR_PORT_NAME_SNSR_FINGER_TIP);
        
        if (portInfo != null) {
            Ir.logger.debug("PortId    : " + portInfo.getPortId());
            Ir.logger.debug("ClusterId : " + portInfo.getClusterId());
            Ir.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Ir.logger.debug("S07_ensure_the_fingerTip_Latch_Opened : irtBay_FingerTipLatch_Status : Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        /*String state = bayUtils.getInputDataFromBay(portInfo.getClusterId(), 
                                                     portInfo.getBayId(), 
                                                     portInfo.getPortId());*/
		
		String state = bayUtils.getInputDataFromBayV2(portInfo) ;
              
        state = state.equals(Constant_IO_ActionMapping.OFF) ? Constant_IO_ActionMapping.CLOSE : Constant_IO_ActionMapping.OPEN;

        if(StateExecutorController.simulateIrBayHappyPath){
        	state = Constant_IO_ActionMapping.CLOSE;
        }
        

		responseReturn.put("status", state);
		
        Ir.logger.debug("S07_ensure_the_fingerTip_Latch_Opened : irtBay_FingerTipLatch_Status : state : " + state); 
		
        Ir.logger.debug("S07_ensure_the_fingerTip_Latch_Opened : irtBay_FingerTipLatch_Status : Exit");
        return responseReturn;
    }
}
