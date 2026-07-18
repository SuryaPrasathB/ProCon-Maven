package com.tasnetwork.calibration.conveyor.bay.ft;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

/**
 * State class responsible for introducing a generic 2-second delay in the FT
 * Bay sequence.
 * Typically used to allow mechanical components to settle or for pacing the
 * sequence.
 */
public class S00_DELAY_STATE_2sec implements FtBayState {

	// ===========================================================================================
	@Override
	public BayResponse handleRequest() {
		// Log entry with structured format: [BAY_IDENTIFIER] : [ACTION_TYPE] :
		// [STATUS/OUTCOME]
		Ft.logger.info(String.format("[%s] : [DELAY_STATE] : [2_SEC] - Entry", getMyBayKey()));

		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(false);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601); // Default to a general error code

		BayUtils.delay(2000);

		Ft.logger.info(String.format("[%s] : [DELAY_STATE] : [2_SEC] - Exit", getMyBayKey()));
		return bayResponse;
	}

}