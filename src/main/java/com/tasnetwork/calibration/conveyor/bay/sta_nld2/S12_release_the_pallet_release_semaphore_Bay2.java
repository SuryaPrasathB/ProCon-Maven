package com.tasnetwork.calibration.conveyor.bay.sta_nld2;

import java.util.concurrent.Semaphore;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.conveyor.util.SemaphoreManager;

public class S12_release_the_pallet_release_semaphore_Bay2 implements STA_NoLoadTestBay2State {
	
	BayResponse bayResponse = new BayResponse();
	private static final Semaphore semaphore = SemaphoreManager.getSemaphore(); //Shared Semaphore 
	
	@Override
    public BayResponse handleRequest() {
		StaNld_Bay2.logger.info("S12_release_the_pallet_release_semaphore : BAY 2 : Entry");
		StaNld_Bay2.logger.info("S12_release_the_pallet_release_semaphore : BAY 2 : semaphore : " + semaphore.availablePermits());

		try {
        	if (semaphore.availablePermits() == 0) { // Check before releasing
				semaphore.release();
				StaNld_Bay2.logger.info("S12_release_the_pallet_release_semaphore : BAY 2 : Semaphore released successfully.");

                bayResponse.setStatus(true);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
			} else {
				StaNld_Bay2.logger.warn("S12_release_the_pallet_release_semaphore : BAY 2 : Attempted to release an unheld semaphore.");
                bayResponse.setStatus(false);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_024);
			}
        } catch (Exception e) {
        	bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_024);
            StaNld_Bay2.logger.error("S12_release_the_pallet_release_semaphore : BAY 2 : Exception while releasing semaphore.", e);
        }
		
		StaNld_Bay2.logger.info("S12_release_the_pallet_release_semaphore : BAY 2 : semaphore : " + semaphore.availablePermits());

        StaNld_Bay2.logger.info("S12_release_the_pallet_release_semaphore : BAY 2 : Exit");
        return bayResponse;
	}
	
}