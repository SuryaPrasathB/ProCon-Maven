package com.tasnetwork.calibration.conveyor.bay.sta_nld1;

import java.util.Map;
import java.util.concurrent.Semaphore;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.conveyor.util.SemaphoreManager;

public class S10_acquire_the_pallet_release_semaphore_Bay1 implements STA_NoLoadTestBay1State {
	
	BayResponse bayResponse = new BayResponse();
	private static final Semaphore semaphore = SemaphoreManager.getSemaphore(); //Shared Semaphore 
	
	public String getMyBayKey() {
		return myBayKey;
	}
	
	@Override
    public BayResponse handleRequest() {
		StaNld_Bay1.logger.info("S10_aquire_the_pallet_release_semaphore : BAY 1 : Entry");
		StaNld_Bay1.logger.info("S10_aquire_the_pallet_release_semaphore : BAY 1 : semaphore : " + semaphore.availablePermits());
		
		try {
			StaNld_Bay1.logger.info("S10_aquire_the_pallet_release_semaphore : BAY 1 : Semaphore acquiring.....");

			semaphore.acquire(); //Blocks until available

			StaNld_Bay1.logger.info("S10_aquire_the_pallet_release_semaphore : BAY 1 : Semaphore acquired successfully.");
			StaNld_Bay1.logger.info("S10_aquire_the_pallet_release_semaphore : BAY 1 : semaphore : " + semaphore.availablePermits());
			
			/*if (ProconFeatureEnable.MOTOR_CONTROL_ENABLE) {
				BayUtils bayUtils = new BayUtils();
				Map<String, Object> responseReturn = bayUtils.set_motor_required(getMyBayKey());
				boolean set_motor_required = (boolean) responseReturn.get("status");
				if (set_motor_required) {
					StaNld_1.logger.info("set_motor_required : Success");
					bayResponse.setStatus(true);
					bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
				} else {
					StaNld_1.logger.info("Failed to set_motor_required ");
					bayResponse.setStatus(false);
					bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_026);
				} 
			}*/
			ConveyorDataManager.setSta1PalletsExitInProgress(true);
			bayResponse.setStatus(true);
	        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} catch (InterruptedException  e) {
			Thread.currentThread().interrupt(); // Restore interrupt Status
			bayResponse.setStatus(false);
	        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_023);	
	        
	        StaNld_Bay1.logger.error("S10_aquire_the_pallet_release_semaphore : BAY 1 : Semaphore acquisition interrupted.", e);
		} catch (Exception e) {
			bayResponse.setStatus(false);
	        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_014);	
	        
	        StaNld_Bay1.logger.error("S10_aquire_the_pallet_release_semaphore : BAY 1 : Exception while acquiring semaphore.", e);
		}


        StaNld_Bay1.logger.info("S10_aquire_the_pallet_release_semaphore : BAY 1 : Exit");
        return bayResponse;
	}
	
}
 