package com.tasnetwork.calibration.conveyor.bay.loading;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

public class S03_error_Handling  implements LoadingBayState {
	BayResponse bayResponse = new BayResponse();

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
		case ConvErrorCodeMapping.ERROR_CODE_LOADING_001:
			// Add specific handling for ERROR_CODE_LOADING_001
			break;
		case ConvErrorCodeMapping.ERROR_CODE_LOADING_002:
			// Add specific handling for ERROR_CODE_LOADING_002
			break;
		case ConvErrorCodeMapping.ERROR_CODE_LOADING_003:
			// Add specific handling for ERROR_CODE_LOADING_003
			break;
		case ConvErrorCodeMapping.ERROR_CODE_LOADING_004:
			// Add specific handling for ERROR_CODE_LOADING_004
			break;
		case ConvErrorCodeMapping.ERROR_CODE_LOADING_005:
			// Add specific handling for ERROR_CODE_LOADING_005
			break;
		default:
			// Add specific handling for unknown error codes
			break;
		}

		bayResponse.setErrorCode(errorCode);
	}


}