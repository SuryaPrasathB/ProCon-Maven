package com.tasnetwork.calibration.conveyor.bay.verific_waiting;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
//import com.tasnetwork.calibration.conveyor.bay_verificationtest.VerificationTestBay;
//import com.tasnetwork.calibration.conveyor.bay_verificationtest.VerificationTestBay2;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;


public class S05_error_Handling  implements WaitingBayState {
	BayResponse bayResponse = new BayResponse();

	@Override
	public BayResponse handleRequest() {
		VerificWaiting.logger.info("S05_error_Handling : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode("NO_ERROR_001");

		
		boolean idleComplete = false;
		
		if (VerificWaiting.isResetProcessRequestedWaitingBay()) {
			VerificWaiting.setResetProcessCompletedWaitingBay(true);
			VerificWaiting.setResetProcessRequestedWaitingBay(false);
		}

		if (VerificWaiting.isStopProcessRequestedWaitingBay()) {
			VerificWaiting.setStopProcessCompletedWaitingBay(true);
			VerificWaiting.setStopProcessRequestedWaitingBay(false);
		}


		while (!idleComplete &&
				(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {
			for(int i = 0; i < 5; i ++) {
				VerificWaiting.logger.info("S18_idle_condition : Waiting in Idle Condition");
				BayUtils.delay(1000);
			}
			idleComplete = true;

		}
		
  
		return bayResponse;
	}

	public S05_error_Handling(){

	}

	public S05_error_Handling(String errorCode) {

		switch (errorCode) {
		case ConvErrorCodeMapping.ERROR_CODE_WAITING_001:
			// Add specific handling for ERROR_CODE_WAITING_001
			break;
		case ConvErrorCodeMapping.ERROR_CODE_WAITING_002:
			// Add specific handling for ERROR_CODE_WAITING_002
			break;
		case ConvErrorCodeMapping.ERROR_CODE_WAITING_003:
			// Add specific handling for ERROR_CODE_WAITING_003
			break;
		case ConvErrorCodeMapping.ERROR_CODE_WAITING_004:
			// Add specific handling for ERROR_CODE_WAITING_004
			break;
		case ConvErrorCodeMapping.ERROR_CODE_WAITING_005:
			// Add specific handling for ERROR_CODE_WAITING_005
			break;
		case ConvErrorCodeMapping.ERROR_CODE_WAITING_006:
			// Add specific handling for ERROR_CODE_WAITING_006
			break;
		case ConvErrorCodeMapping.ERROR_CODE_WAITING_007:
			// Add specific handling for ERROR_CODE_WAITING_007
			break;
		case ConvErrorCodeMapping.ERROR_CODE_WAITING_008:
			// Add specific handling for ERROR_CODE_WAITING_008
			break;
		default:
			// Add specific handling for unknown error codes
			break;
		}

		bayResponse.setErrorCode(errorCode);
	}


}