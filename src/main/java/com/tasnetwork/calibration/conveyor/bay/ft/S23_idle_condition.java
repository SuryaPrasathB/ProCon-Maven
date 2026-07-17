package com.tasnetwork.calibration.conveyor.bay.ft;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

public class S23_idle_condition implements FtBayState {

    // Member variable to hold the bay key, crucial for structured logging
    //private String myBayKey = "unknownBay"; // Default or can be set via constructor/setter

    public String getMyBayKey() {
        return myBayKey;
    }

    // Setter for myBayKey if it needs to be dynamically set (e.g., from a factory)
/*    public void setMyBayKey(String myBayKey) {
        this.myBayKey = myBayKey;
    }*/

	@Override
	public BayResponse handleRequest() {
		// Structured log entry for sequence start
		Ft.logger.info(String.format("[%s] : [IDLE_CONDITION] : [SEQUENCE_ENTRY] - Entering idle condition state.", getMyBayKey()));

		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode("NO_ERROR_001");
		boolean idleComplete = false;

        

		Ft.logger.info(String.format("[%s] : [IDLE_CONDITION] : [WAIT_LOOP_ENTRY] - Waiting in Idle Condition. Initial idleComplete: %s", getMyBayKey(), idleComplete));

		while (!idleComplete && (!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {
            Ft.logger.debug(String.format("[%s] : [IDLE_CONDITION] : [LOOP_ITERATION] - Current idleComplete: %s, ALL_LOOP_BREAK_FLAG: %s", getMyBayKey(), idleComplete, ConstantConveyor.ALL_LOOP_BREAK_FLAG));

			for(int i = 0; i < 2; i ++) {
				Ft.logger.debug(String.format("[%s] : [IDLE_CONDITION] : [DELAY] - Delaying for 1 second (iteration %d/2).", getMyBayKey(), (i + 1)));
				BayUtils.delay(1000);
			}
			idleComplete = true; // Set to true after delay to exit loop
            Ft.logger.debug(String.format("[%s] : [IDLE_CONDITION] : [IDLE_COMPLETE_SET] - idleComplete set to true.", getMyBayKey()));
		}
		Ft.logger.info(String.format("[%s] : [IDLE_CONDITION] : [WAIT_LOOP_EXIT] - Exited Idle Condition waiting loop. Final idleComplete: %s", getMyBayKey(), idleComplete));

		// Check and handle start process request
		if (Ft.isStartProcessRequestedFtBay()) {
			Ft.logger.debug(String.format("[%s] : [IDLE_CONDITION] : [START_REQUESTED] - Start process requested. Setting stop process completed.", getMyBayKey()));
			Ft.setStopProcessCompletedFtBay(true);
		}

        // Check and handle stop process request
		if (Ft.isStopProcessRequestedFtBay()) {
			Ft.logger.debug(String.format("[%s] : [IDLE_CONDITION] : [STOP_REQUESTED] - Stop process requested. Setting stop process completed.", getMyBayKey()));
			Ft.setStopProcessCompletedFtBay(true);
		}

        // Check and handle reset process request
		if (Ft.isResetProcessRequestedFtBay()) {
			Ft.logger.debug(String.format("[%s] : [IDLE_CONDITION] : [RESET_REQUESTED] - Reset process requested. Setting reset process completed.", getMyBayKey()));
			Ft.setResetProcessCompletedFtBay(true);
		}
				
		// Structured log for sequence exit
		Ft.logger.info(String.format("[%s] : [IDLE_CONDITION] : [SEQUENCE_EXIT] - Idle condition state completed. Final Status: %s, Error Code: %s", getMyBayKey(), bayResponse.getStatus(), bayResponse.getErrorCode()));
		return bayResponse;
	}
}
