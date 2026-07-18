package com.tasnetwork.calibration.conveyor.bay.sta_nld2;

 import java.util.concurrent.Semaphore;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.conveyor.util.SemaphoreManager;

public class S10_acquire_the_pallet_release_semaphore_Bay2 implements STA_NoLoadTestBay2State {
	
	BayResponse bayResponse = new BayResponse();
	

	
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