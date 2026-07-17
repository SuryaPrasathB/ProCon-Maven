package com.tasnetwork.calibration.conveyor.bay.sta_nld1;

import java.util.concurrent.Semaphore;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.conveyor.util.SemaphoreManager;

public class S12_release_the_pallet_release_semaphore_Bay1 implements STA_NoLoadTestBay1State {
	
	BayResponse bayResponse = new BayResponse();
	private static final Semaphore semaphore = SemaphoreManager.getSemaphore(); //Shared Semaphore 
		
	@Override
    public BayResponse handleRequest() {
		StaNld_Bay1.logger.info("S12_release_the_pallet_release_semaphore : BAY 1 : Entry");
		StaNld_Bay1.logger.info("S12_release_the_pallet_release_semaphore : BAY 1 : semaphore : " + semaphore.availablePermits());
		
        try {
        	if (semaphore.availablePermits() == 0) { // Check before releasing
				semaphore.release();
				StaNld_Bay1.logger.info("S12_release_the_pallet_release_semaphore : BAY 1 : Semaphore released successfully.");

                bayResponse.setStatus(true);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
			} else {
				StaNld_Bay1.logger.warn("S12_release_the_pallet_release_semaphore : BAY 1 : Attempted to release an unheld semaphore.");
                bayResponse.setStatus(false);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_024);
			}
        } catch (Exception e) {
        	bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_024);
            StaNld_Bay1.logger.error("S12_release_the_pallet_release_semaphore : BAY 1 : Exception while releasing semaphore.", e);
        }
        
		StaNld_Bay1.logger.info("S12_release_the_pallet_release_semaphore : BAY 1 : semaphore : " + semaphore.availablePermits());

        StaNld_Bay1.logger.info("S12_release_the_pallet_release_semaphore : BAY 1 : Exit");
        return bayResponse;
	}
	
}
 