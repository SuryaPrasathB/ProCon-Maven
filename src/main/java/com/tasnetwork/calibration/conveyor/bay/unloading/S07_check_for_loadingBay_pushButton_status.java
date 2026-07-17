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

public class S07_check_for_loadingBay_pushButton_status implements UnloadingBayState {

/*    String LOW = "PRESSED";
    String HIGH = "NOT_PRESSED";*/

    @Override
    public BayResponse handleRequest() {
        Unloading.logger.info("S07_check_for_LoadingBay_pushButton_status : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);


		Map<String,Object> responseReturn =  isLoadingBayPushButtonPressed();	 
		boolean isLoadingBayPushButtonPressed = (boolean)responseReturn.get("status");
	    
        while (!isLoadingBayPushButtonPressed &&
        		(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {
            Unloading.logger.info("S07_check_for_LoadingBay_pushButton_status : Loading Bay PushButton Not Pressed");
            BayUtils.delay(1000);
            
        	responseReturn =  isLoadingBayPushButtonPressed();	 
    	    isLoadingBayPushButtonPressed = (boolean)responseReturn.get("status");
    	    
        }

        if (isLoadingBayPushButtonPressed) {
            Unloading.logger.info("S07_check_for_LoadingBay_pushButton_status : Loading Bay PushButton Pressed");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        } else {
            Unloading.logger.info("S07_check_for_LoadingBay_pushButton_status : Loading Bay PushButton Not Pressed");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_UNLOADING_013);
        }

        Unloading.logger.info("S07_check_for_LoadingBay_pushButton_status : Exit");
        return bayResponse;
    }

    private Map<String,Object> isLoadingBayPushButtonPressed() {
        Unloading.logger.debug("S07_check_for_LoadingBay_pushButton_status : isLoadingBayPushButtonPressed : Entry");

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
        IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.LOADING_PORT_NAME_PUSH_BTN);

        if (portInfo != null) {
            Unloading.logger.debug("PortId    : " + portInfo.getPortId());
            Unloading.logger.debug("ClusterId : " + portInfo.getClusterId());
            Unloading.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Unloading.logger.debug("S07_check_for_LoadingBay_pushButton_status : isLoadingBayPushButtonPressed : Input port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        /*String state = bayUtils.getInputDataFromBay(portInfo.getClusterId(),
                portInfo.getBayId(),
                portInfo.getPortId());*/
		
		String state = bayUtils.getInputDataFromBayV2(portInfo) ;

        Unloading.logger.debug("S07_check_for_LoadingBay_pushButton_status : isLoadingBayPushButtonPressed : state : " + state);

        status = state.equals(Constant_IO_ActionMapping.ON) ? true : false; 

		if(StateExecutorController.simulateUnloadingBayHappyPath){
			status = true; 
		}

        Unloading.logger.debug("S07_check_for_LoadingBay_pushButton_status : isLoadingBayPushButtonPressed : status : " + status); 

		responseReturn.put("status", status);
		

        Unloading.logger.debug("S07_check_for_LoadingBay_pushButton_status : isLoadingBayPushButtonPressed : Exit");
        return responseReturn;
    }
}
