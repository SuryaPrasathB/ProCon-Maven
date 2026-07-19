package com.tasnetwork.calibration.conveyor.bay.calib;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

import javafx.application.Platform;

public class S04_ensure_the_fingerTip_Latch_Closed implements CalibrationBayState {
	
	BayUtils bayUtils = new BayUtils();
	
    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        Calib.logger.info("S04_ensure_the_fingerTip_Latch_Closed : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
 
		int try_count = 0;
		 
		while(try_count <= 3 && !Calib.isStopProcessRequestedCalibBay()){
			
			Map<String,Object> responseReturn =  calibBay_FingerTipLatch_Status();	 
			String calibBay_FingerTipLatch_Status = (String)responseReturn.get("status");

			if (calibBay_FingerTipLatch_Status.equals(Constant_IO_ActionMapping.OPEN)){  
				ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setPalletsLockedImageDisplayOn(getMyBayKey(),true);
				ConveyorDataManager.getDashboardObject().getBayIndicatorManager().startTimeUpDisplay(getMyBayKey());
				
				if (ProconFeatureEnable.MOTOR_CONTROL_ENABLE) {
					BayUtils bayUtils = new BayUtils();			
					responseReturn = bayUtils.set_motor_not_required(getMyBayKey());
					
					boolean set_motor_not_required = (boolean) responseReturn.get("status");
					if (set_motor_not_required) {
						Calib.logger.info("set_motor_not_required : Success");
						bayResponse.setStatus(true);
						bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
					} else {
						Calib.logger.info("Failed to set_motor_not_required ");
						bayResponse.setStatus(false);
						bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_026);
					} 
				}
				//bayResponse.setStatus(true);
				//bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
				break;
			} else {
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_004);	
				BayUtils.delay(1000);
				try_count++;
			}
		}
		
		// WITH IR BAY LAMP TOGGLE
		if (ProconFeatureEnable.WAIT_FOR_USER_INPUT) {

			boolean toggle = false; // This flag will be used to alternate
			long startTime = System.currentTimeMillis();
			
            StateExecutorController.getRef_btn_CalibPlace().setDisable(false);


			while (!ConstantConveyor.isCALIB_OPTICAL_PLACED() && !Calib.isStopProcessRequestedCalibBay()) {
				long elapsedTime = (System.currentTimeMillis() - startTime) / 1000; // in seconds

				Platform.runLater(() -> {
					StateExecutorController.ref_tf_CALIB_prompt.setText("Place Optical Readers - " + elapsedTime + " secs");
				});

				// Alternate the tower lamp state
				if (toggle) {
					turn_on_tower_lamp1();  // Turn ON Lamp 1
					BayUtils.delay(100);
					//turn_off_tower_lamp2(); // Turn OFF Lamp 2
				} else {
					turn_off_tower_lamp1(); // Turn OFF Lamp 1.
					BayUtils.delay(100);
					//turn_on_tower_lamp2();// Turn ON Lamp 2
				}
				
				toggle = !toggle; // Flip the flag for next iteration

				Calib.logger.debug("S081_stop_FT_source : Waiting to Place Optical Readers ");
			}

			BayUtils.delay(2000);
			ConstantConveyor.setCALIB_OPTICAL_PLACED(false);
			StateExecutorController.getRef_btn_CalibPlace().setDisable(true);

			Platform.runLater(() -> {
				StateExecutorController.ref_tf_CALIB_prompt.clear();
			});
		}
		
		Calib.logger.info("S04_ensure_the_fingerTip_Latch_Closed : Exit");
        return bayResponse;
    }
    
  //============================================================================================================================================  

  	private void turn_on_tower_lamp1() {

      	//============================================================================================
      	IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.IR_PORT_NAME_TWR_LAMP2);

      	BayUtils bayUtils = new BayUtils();

      	bayUtils.setOutputDataToBay(portInfo.getClusterId(), 
      			portInfo.getBayId(), 
      			portInfo.getPortId(),
      			Constant_IO_ActionMapping.OPEN);
      }

      private void turn_off_tower_lamp1() {

      	//============================================================================================
      	IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.IR_PORT_NAME_TWR_LAMP2);

      	BayUtils bayUtils = new BayUtils();

      	bayUtils.setOutputDataToBay(portInfo.getClusterId(), 
      			portInfo.getBayId(), 
      			portInfo.getPortId(),
      			Constant_IO_ActionMapping.CLOSE); // Use CLOSE to represent turning the relay "On"
      }

    //============================================================================================================================================  

    private Map<String, Object> calibBay_FingerTipLatch_Status() {
        Calib.logger.debug("S04_ensure_the_fingerTip_Latch_Closed : calibBay_FingerTipLatch_Status : Entry");
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
        IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.CALIB_PORT_NAME_SNSR_FINGER_TIP);
        
        if (portInfo != null) {
            Calib.logger.debug("PortId    : " + portInfo.getPortId());
            Calib.logger.debug("ClusterId : " + portInfo.getClusterId());
            Calib.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Calib.logger.debug("S04_ensure_the_fingerTip_Latch_Closed : calibBay_FingerTipLatch_Status : Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        String state = bayUtils.getInputDataFromBayV2(portInfo);
              
        state = state.equals(Constant_IO_ActionMapping.ON) ? Constant_IO_ActionMapping.OPEN : Constant_IO_ActionMapping.CLOSE;

        if(StateExecutorController.simulateCalibBayHappyPath){
        	state = Constant_IO_ActionMapping.OPEN; 
        }
         
        Calib.logger.debug("S04_ensure_the_fingerTip_Latch_Closed : calibBay_FingerTipLatch_Status : state : " + state);
        
        responseReturn.put("status", state);
        
        Calib.logger.debug("S04_ensure_the_fingerTip_Latch_Closed : calibBay_FingerTipLatch_Status : Exit");
        return responseReturn;
    }
}
