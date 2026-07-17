package com.tasnetwork.calibration.conveyor.bay.sta_nld2;

 import java.util.Map;
import java.util.concurrent.Semaphore;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.conveyor.util.SemaphoreManager;

public class S10_acquire_the_pallet_release_semaphore_Bay2 implements STA_NoLoadTestBay2State {
	
	BayResponse bayResponse = new BayResponse();
	
	public String getMyBayKey() {
		return myBayKey;
	}
	
	private static final Semaphore semaphore = SemaphoreManager.getSemaphore(); //Shared Semaphore 
	
	@Override
    public BayResponse handleRequest() {
		StaNld_Bay2.logger.info("S10_aquire_the_pallet_release_semaphore : BAY 2 : Entry");
		StaNld_Bay2.logger.info("S10_aquire_the_pallet_release_semaphore : BAY 2 : semaphore : " + semaphore.availablePermits());
		
		try {
			StaNld_Bay2.logger.info("S10_aquire_the_pallet_release_semaphore : BAY 2 : Semaphore acquiring.....");

			semaphore.acquire(); // Blocks until available

			StaNld_Bay2.logger.info("S10_aquire_the_pallet_release_semaphore : BAY 2 : Semaphore acquired successfully.");
			StaNld_Bay2.logger.info("S10_aquire_the_pallet_release_semaphore : BAY 2 : semaphore : " + semaphore.availablePermits());
			ConveyorDataManager.setSta2PalletsExitInProgress(true);
			/*
			if (ProconFeatureEnable.MOTOR_CONTROL_ENABLE) {
				BayUtils bayUtils = new BayUtils();
				Map<String, Object> responseReturn = bayUtils.set_motor_required(getMyBayKey());
				boolean set_motor_required = (boolean) responseReturn.get("status");
				if (set_motor_required) {
					StaNld_Bay2.logger.info("set_motor_required : Success");
					bayResponse.setStatus(true);
					bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
				} else {
					StaNld_Bay2.logger.info("Failed to set_motor_required ");
					bayResponse.setStatus(false);
					bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_026);
				} 
			}
			*/
			ConveyorDataManager.setSta2PalletsExitInProgress(true);
			bayResponse.setStatus(true);
	        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt(); // Restore interrupt Status
			bayResponse.setStatus(false);
	        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_023);	
	        
	        StaNld_Bay2.logger.error("S10_aquire_the_pallet_release_semaphore : BAY 2 : Semaphore acquisition interrupted.", e);
		} catch (Exception e) {
			bayResponse.setStatus(false);
	        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_014);	
	        
	        StaNld_Bay2.logger.error("S10_aquire_the_pallet_release_semaphore : BAY 2 : Exception while acquiring semaphore.", e);
		}

        StaNld_Bay2.logger.info("S10_aquire_the_pallet_release_semaphore : BAY 2 : Exit");
        return bayResponse;
	}
	
}