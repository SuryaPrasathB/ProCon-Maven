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
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S03_close_the_fingerTip_Latch implements CalibrationBayState {
    
/*    String LOW   = "OPEN";
    String HIGH  = "CLOSE";
    String CLOSE  = "On";
    String OPEN   = "Off";
    String ON  = "On";
 	String OFF  = "Off";*/
    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        Calib.logger.info("S03_close_the_fingerTip_Latch : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        
        Map<String,Object> responseReturn =  close_FingerTipLatch_CalibBay();	 

		boolean close_FingerTipLatch_CalibBay = (boolean)responseReturn.get("status");
        
        if (close_FingerTipLatch_CalibBay) {
            Calib.logger.info("S03_close_the_fingerTip_Latch : Finger Tip Latch Closed");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        } else {
            Calib.logger.info("S03_close_the_fingerTip_Latch : Failed to Close Finger Tip Latch");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_003);
        }

        Calib.logger.info("S03_close_the_fingerTip_Latch : Exit");
        return bayResponse;
    }
    //============================================================================================================================================  

    private Map<String,Object> close_FingerTipLatch_CalibBay() {
        Calib.logger.debug("S03_close_the_fingerTip_Latch : close_FingerTipLatch_CalibBay : Entry");

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
        //============================================================================================
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.CALIB_PORT_NAME_FINGER_TIP);
        
        if (portInfo != null) {
            Calib.logger.debug("PortId    : " + portInfo.getPortId());
            Calib.logger.debug("ClusterId : " + portInfo.getClusterId());
            Calib.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Calib.logger.debug("S03_close_the_fingerTip_Latch : close_FingerTipLatch_CalibBay : Output port not found");
            return responseReturn ;
        }
		
        BayUtils bayUtils = new BayUtils();
        
        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(), 
                                              portInfo.getBayId(), 
                                              portInfo.getPortId(),
                                              Constant_IO_ActionMapping.OPEN);
        
        status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;
        
        if(StateExecutorController.simulateCalibBayHappyPath){
        	status = true; 
        }
         
        Calib.logger.debug("S03_close_the_fingerTip_Latch : close_FingerTipLatch_CalibBay : status : " + status);
        //============================================================================================  
       

        responseReturn.put("status", status);
        
        Calib.logger.debug("S03_close_the_fingerTip_Latch : close_FingerTipLatch_CalibBay : Exit");
        return responseReturn;
    }
}
