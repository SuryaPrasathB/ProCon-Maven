package com.tasnetwork.calibration.conveyor.bay.unloading;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;


public class S10_error_Handling  implements UnloadingBayState {
	BayResponse bayResponse = new BayResponse();

	@Override
	public BayResponse handleRequest() {
		ApplicationLauncher.logger.info("S04_error_Handling : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode("NO_ERROR_001");

		return bayResponse;
	}

	public S10_error_Handling(){

	}

	public S10_error_Handling(String errorCode) {

		switch (errorCode) {
		case ConvErrorCodeMapping.ERROR_CODE_UNLOADING_001:
			// Add specific handling for ERROR_CODE_UNLOADING_001
			break;
		case ConvErrorCodeMapping.ERROR_CODE_UNLOADING_002:
			// Add specific handling for ERROR_CODE_UNLOADING_002
			break;
		case ConvErrorCodeMapping.ERROR_CODE_UNLOADING_003:
			// Add specific handling for ERROR_CODE_UNLOADING_003
			break;
		case ConvErrorCodeMapping.ERROR_CODE_UNLOADING_004:
			// Add specific handling for ERROR_CODE_UNLOADING_004
			break;
		case ConvErrorCodeMapping.ERROR_CODE_UNLOADING_005:
			// Add specific handling for ERROR_CODE_UNLOADING_005
			break;
		case ConvErrorCodeMapping.ERROR_CODE_UNLOADING_006:
			// Add specific handling for ERROR_CODE_UNLOADING_006
			break;
		case ConvErrorCodeMapping.ERROR_CODE_UNLOADING_007:
			// Add specific handling for ERROR_CODE_UNLOADING_007
			break;
		case ConvErrorCodeMapping.ERROR_CODE_UNLOADING_008:
			// Add specific handling for ERROR_CODE_UNLOADING_008
			break;
		case ConvErrorCodeMapping.ERROR_CODE_UNLOADING_009:
			// Add specific handling for ERROR_CODE_UNLOADING_009
			break;
		case ConvErrorCodeMapping.ERROR_CODE_UNLOADING_010:
			// Add specific handling for ERROR_CODE_UNLOADING_010
			break;
		case ConvErrorCodeMapping.ERROR_CODE_UNLOADING_011:
			// Add specific handling for ERROR_CODE_UNLOADING_011
			break;
		case ConvErrorCodeMapping.ERROR_CODE_UNLOADING_012:
			// Add specific handling for ERROR_CODE_UNLOADING_012
			break;
		case ConvErrorCodeMapping.ERROR_CODE_UNLOADING_013:
			// Add specific handling for ERROR_CODE_UNLOADING_013
			break;
		default:
			// Add specific handling for unknown error codes
			break;
		}

		bayResponse.setErrorCode(errorCode);
	}


}