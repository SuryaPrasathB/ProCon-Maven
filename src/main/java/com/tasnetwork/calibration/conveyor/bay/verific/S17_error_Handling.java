package com.tasnetwork.calibration.conveyor.bay.verific;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.dashboard.MainControlPaneController;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;


public class S17_error_Handling  implements VerificTestBayState {
	BayResponse bayResponse = new BayResponse();

	@Override
	public BayResponse handleRequest() {
		ApplicationLauncher.logger.info("S17_error_Handling : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode("NO_ERROR_001");

		return bayResponse;
	}

	public S17_error_Handling() {

	}

	public S17_error_Handling(String errorCode) {

	    switch (errorCode) {
	        case ConvErrorCodeMapping.ERROR_CODE_VERIFIC_001:
	            // Add specific handling for ERROR_CODE_VERIFIC_001
	            break;
	        case ConvErrorCodeMapping.ERROR_CODE_VERIFIC_002:
	            // Add specific handling for ERROR_CODE_VERIFIC_002
	            break;
	        case ConvErrorCodeMapping.ERROR_CODE_VERIFIC_003:
	            // Add specific handling for ERROR_CODE_VERIFIC_003
	            break;
	        case ConvErrorCodeMapping.ERROR_CODE_VERIFIC_004:
	            // Add specific handling for ERROR_CODE_VERIFIC_004
	            break;
	        case ConvErrorCodeMapping.ERROR_CODE_VERIFIC_005:
	            // Add specific handling for ERROR_CODE_VERIFIC_005
	            break;
	        case ConvErrorCodeMapping.ERROR_CODE_VERIFIC_006:
	            // Add specific handling for ERROR_CODE_VERIFIC_006
	            break;
	        case ConvErrorCodeMapping.ERROR_CODE_VERIFIC_007:
	            // Add specific handling for ERROR_CODE_VERIFIC_007
	            break;
	        case ConvErrorCodeMapping.ERROR_CODE_VERIFIC_008:
	            // Add specific handling for ERROR_CODE_VERIFIC_008
	            break;
	        case ConvErrorCodeMapping.ERROR_CODE_VERIFIC_009:
	            // Add specific handling for ERROR_CODE_VERIFIC_009
	            break;
	        case ConvErrorCodeMapping.ERROR_CODE_VERIFIC_010:
	            // Add specific handling for ERROR_CODE_VERIFIC_010
	            break;
	        case ConvErrorCodeMapping.ERROR_CODE_VERIFIC_011:
	            // Add specific handling for ERROR_CODE_VERIFIC_011
	            break;
	        case ConvErrorCodeMapping.ERROR_CODE_VERIFIC_012:
	            // Add specific handling for ERROR_CODE_VERIFIC_012
	            break;
	        case ConvErrorCodeMapping.ERROR_CODE_VERIFIC_013:
	            // Add specific handling for ERROR_CODE_VERIFIC_013
	            break;
	        case ConvErrorCodeMapping.ERROR_CODE_VERIFIC_014:
	            // Add specific handling for ERROR_CODE_VERIFIC_014
	            break;
	        case ConvErrorCodeMapping.ERROR_CODE_VERIFIC_015:
	            // Add specific handling for ERROR_CODE_VERIFIC_015
	            break;
	        case ConvErrorCodeMapping.ERROR_CODE_VERIFIC_016:
	            // Add specific handling for ERROR_CODE_VERIFIC_016
	            break;
	        case ConvErrorCodeMapping.ERROR_CODE_VERIFIC_017:
	            // Add specific handling for ERROR_CODE_VERIFIC_017
	            break;
	        case ConvErrorCodeMapping.ERROR_CODE_VERIFIC_018:
	            // Add specific handling for ERROR_CODE_VERIFIC_018
	            break;
	        case ConvErrorCodeMapping.ERROR_CODE_VERIFIC_019:
	            // Add specific handling for ERROR_CODE_VERIFIC_019
	            break;
	        case ConvErrorCodeMapping.ERROR_CODE_VERIFIC_033 :
	        	MainControlPaneController mainControlPaneController = new MainControlPaneController();
	    		mainControlPaneController.verificTestStop();
	        default:
	        	
	            break;
	    }

	    bayResponse.setErrorCode(errorCode);
	}

}