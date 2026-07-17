package com.tasnetwork.calibration.conveyor.bay.unloading;

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

public class S06_check_for_alarm_pushButton_status implements UnloadingBayState {

 /*   String LOW = "PRESSED";
    String HIGH = "NOT_PRESSED";*/

    @Override
    public BayResponse handleRequest() {
        Unloading.logger.info("S06_check_for_alarm_pushButton_status : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);


		Map<String,Object> responseReturn =  isAlarmPushButtonPressed();	 
		boolean isAlarmPushButtonPressed = (boolean)responseReturn.get("status");
	    
        while (!isAlarmPushButtonPressed &&
        		(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {
        	  Unloading.logger.info("S06_check_for_alarm_pushButton_status : Alarm PushButton Not Pressed");
              BayUtils.delay(1000);
              
    		responseReturn =  isAlarmPushButtonPressed();	 
    		isAlarmPushButtonPressed = (boolean)responseReturn.get("status");
    	    
          
        }

        if (isAlarmPushButtonPressed) {
            Unloading.logger.info("S06_check_for_alarm_pushButton_status : Alarm PushButton Pressed");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        } else {
            Unloading.logger.info("S06_check_for_alarm_pushButton_status : Alarm PushButton Not Pressed");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_602);
        }

        Unloading.logger.info("S06_check_for_alarm_pushButton_status : Exit");
        return bayResponse;
    }

    private Map<String,Object> isAlarmPushButtonPressed() {
        Unloading.logger.debug("S06_check_for_alarm_pushButton_status : isAlarmPushButtonPressed : Entry");

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
        IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.UNLOADING_PORT_NAME_PUSH_BTN);

        if (portInfo != null) {
            Unloading.logger.debug("PortId    : " + portInfo.getPortId());
            Unloading.logger.debug("ClusterId : " + portInfo.getClusterId());
            Unloading.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Unloading.logger.debug("S06_check_for_alarm_pushButton_status : isAlarmPushButtonPressed : Input port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        /*String state = bayUtils.getInputDataFromBay(portInfo.getClusterId(),
                portInfo.getBayId(),
                portInfo.getPortId());*/
		
		String state = bayUtils.getInputDataFromBayV2(portInfo) ;

        Unloading.logger.debug("S06_check_for_alarm_pushButton_status : isAlarmPushButtonPressed : state : " + state);

        status = state.equals(Constant_IO_ActionMapping.ON) ? true : false; 

		if(StateExecutorController.simulateUnloadingBayHappyPath){
			status = true; 
		}

        Unloading.logger.debug("S06_check_for_alarm_pushButton_status : isAlarmPushButtonPressed : status : " + status); 

		responseReturn.put("status", status);
		

        Unloading.logger.debug("S06_check_for_alarm_pushButton_status : isAlarmPushButtonPressed : Exit");
        return responseReturn;
    }
}
