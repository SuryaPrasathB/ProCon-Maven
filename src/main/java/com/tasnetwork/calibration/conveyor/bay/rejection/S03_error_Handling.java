package com.tasnetwork.calibration.conveyor.bay.rejection;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

public class S03_error_Handling  implements RejectionBayState {
	BayResponse bayResponse = new BayResponse();	

	/**
	 * Error handling state for the Rejection Bay.
	 *
	 * @return BayResponse indicating success or failure.
	 */
	@Override
	public BayResponse handleRequest() {
		ApplicationLauncher.logger.info("S03_error_Handling : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode("NO_ERROR_001");

		return bayResponse;
	}

	public S03_error_Handling(){

	}

	public S03_error_Handling(String errorCode) {

		switch (errorCode) {
		case ConvErrorCodeMapping.ERROR_CODE_REJECTION_001:
			// Add specific handling for ERROR_CODE_LOADING_001
			break;
		default:
			// Add specific handling for unknown error codes
			break;
		}

		bayResponse.setErrorCode(errorCode);
	}


}