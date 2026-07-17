package com.tasnetwork.calibration.conveyor.bay.sta_nld1;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.Constant_Motor_Requirement;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
//import com.tasnetwork.calibration.conveyor.bay_verificationtest.S05_verification_Test;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S16_open_stop_latch_STA_NLDT_Bay1 implements STA_NoLoadTestBay1State {

	static int palletsPassedSTA_NLDT_Bay1 = 0;
	
	public String getMyBayKey() {
		return myBayKey;
	}
	
    @Override
    public BayResponse handleRequest() {
    	StaNld_Bay1.logger.info("S16_open_stop_latch_SCT_NLT_Bay1 : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        BayUtils.delay(2000);
        
        Map<String, Object> responseReturn;


        

        responseReturn = open_StopLatch_SCT_NLT_Bay1();

        boolean openStopLatch_SCT_NLT_Bay1 = (boolean) responseReturn.get("status");

        if (openStopLatch_SCT_NLT_Bay1) {
        	StaNld_Bay1.logger.info("S16_open_stop_latch_SCT_NLT_Bay1 : Stop Latch Opened");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
            
            BayUtils.delay(3000);
            
            if (ProconFeatureEnable.MOTOR_CONTROL_ENABLE) {
    			BayUtils bayUtils = new BayUtils();
    			responseReturn = bayUtils.set_motor_required(getMyBayKey(), Constant_Motor_Requirement.STA1_MOTOR_REQUIRED);
    			boolean set_motor_required = (boolean) responseReturn.get("status");
    			if (set_motor_required) {
    				StaNld_Bay1.logger.info("set_motor_required : Success");
    				bayResponse.setStatus(true);
    				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
    			} else {
    				StaNld_Bay1.logger.info("Failed to set_motor_required ");
    				bayResponse.setStatus(false);
    				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_026);
    			} 
    		} else {
    			bayResponse.setStatus(true);
    			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
    		}
            StaNld_Bay1.logger.info("S16_open_stop_latch_SCT_NLT_Bay1 : hit1");
            if(S044_Close_Run_Project_Bay1.isSTA_NLDT_TestCompleted()) {  
            	palletsPassedSTA_NLDT_Bay1++ ;
            	StaNld_Bay1.logger.info("S16_open_stop_latch_SCT_NLT_Bay1 : increment : hit2");
            } else {
            	palletsPassedSTA_NLDT_Bay1++ ; // REMOVE LATER
            	StaNld_Bay1.logger.info("S16_open_stop_latch_SCT_NLT_Bay1 : increment2 : hit2");
            }
                        
            //BayUtils.delay(1000);
        } else {
        	StaNld_Bay1.logger.info("S16_open_stop_latch_SCT_NLT_Bay1 : Failed to Open Stop Latch");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_005);
        }

        StaNld_Bay1.logger.info("S16_open_stop_latch_SCT_NLT_Bay1 : Exit");
        return bayResponse;
    }

    private Map<String, Object> open_StopLatch_SCT_NLT_Bay1() {
    	StaNld_Bay1.logger.debug("S16_open_stop_latch_SCT_NLT_Bay1 : sctNltBay1_StopLatch_Status : Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<>();
        responseReturn.put("status", false);

        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.SCT_NLT1_PORT_NAME_STPR);

        String state = "";
        if (portInfo != null) {
        	StaNld_Bay1.logger.debug("PortId    : " + portInfo.getPortId());
        	StaNld_Bay1.logger.debug("ClusterId : " + portInfo.getClusterId());
        	StaNld_Bay1.logger.debug("BayId     : " + portInfo.getBayId());

            String outputActive = Constant_IO_ActionMapping.OFF;
            BayUtils bayUtils = new BayUtils();
            
             state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                                                     portInfo.getBayId(),
                                                     portInfo.getPortId(),
                                                     outputActive);

            StaNld_Bay1.logger.debug("S16_open_stop_latch_SCT_NLT_Bay1 : open_StopLatch_SCT_NLT_Bay1 : state : " + state);
            /*if (simulateSCTNLTBay1HappyPath) {
                state = Constant_IO_ActionMapping.ON;
            }*/

            status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false;
            
            if(StateExecutorController.simulateSCTNLTBay1HappyPath){
            	status = true; 
            }
             
            StaNld_Bay1.logger.debug("S16_open_stop_latch_SCT_NLT_Bay1 : open_StopLatch_SCT_NLT_Bay1 : status : " + status);

        } else {
        	StaNld_Bay1.logger.debug("S16_open_stop_latch_SCT_NLT_Bay1 : Output port not found");
        	return responseReturn ;
        }

        responseReturn.put("status", status);
        responseReturn.put("responseData", state);

        StaNld_Bay1.logger.debug("S16_open_stop_latch_SCT_NLT_Bay1 : sctNltBay1_StopLatch_Status : status : " + status);
        StaNld_Bay1.logger.debug("S16_open_stop_latch_SCT_NLT_Bay1 : sctNltBay1_StopLatch_Status : Exit");
        return responseReturn;
    }

	public static int getPalletsPassedSTA_NLDT_Bay1() {
		return palletsPassedSTA_NLDT_Bay1;
	}

	public static void setPalletsPassedSTA_NLDT_Bay1(int palletsPassedSTA_NLDT_Bay1) {
		S16_open_stop_latch_STA_NLDT_Bay1.palletsPassedSTA_NLDT_Bay1 = palletsPassedSTA_NLDT_Bay1;
	}
}

