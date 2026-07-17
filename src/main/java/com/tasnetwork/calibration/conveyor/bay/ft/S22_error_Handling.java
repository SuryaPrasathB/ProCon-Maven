package com.tasnetwork.calibration.conveyor.bay.ft;

import java.util.Timer;
import java.util.TimerTask; // Import TimerTask

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S22_error_Handling  implements FtBayState {

	private Timer funtionalBayStopTaskTimer;
	private BayResponse bayResponse = new BayResponse(); // Initialize here

	public String getMyBayKey() {
        return myBayKey;
    }

	@Override
	public BayResponse handleRequest() {
		// Structured log entry for sequence start
		Ft.logger.info(String.format("[%s] : [ERROR_HANDLING] : [SEQUENCE_ENTRY] - Initiating error handling sequence.", getMyBayKey()));

		bayResponse.setStatus(true); // Default to success initially
		bayResponse.setErrorCode("NO_ERROR_001"); // Default no error code

		TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();
		palletAvailableTest_I_F_Status.setBayName(ConstantConveyor.FT_BAY_KEY);
		palletAvailableTest_I_F_Status.setStateName(ConstantBayStateManage.FT_BAY_EP_SEQ_01);
		palletAvailableTest_I_F_Status.setPositionNo("pE");
		palletAvailableTest_I_F_Status.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_INP);
		palletAvailableTest_I_F_Status.setSerialStatus(ConstantConveyor.COMM_STATUS_NOT_APPLICABLE);

		int newRecordSerialNo = StateExecutorController.addToTestStatusGui(palletAvailableTest_I_F_Status);
		Ft.logger.debug(String.format("[%s] : [ERROR_HANDLING] : [GUI_STATUS_UPDATE] - Added new GUI status record with serial no: %d for state %s.", getMyBayKey(), newRecordSerialNo, palletAvailableTest_I_F_Status.getStateName()));


		// The commented-out loop seems to be for continuous error logging,
		// but the current implementation immediately updates GUI and returns.
		// If a continuous error state is intended, this loop logic needs to be re-evaluated.
		/*
		boolean status = true;
		while(status){ //bayResponse.getStatus()){
			status = bayResponse.getStatus();
			FunctionalTestBay.logger.info("S22_error_Handling : Error");
			Sleep(2000);
		}
		*/

		// Update GUI after final action
		palletAvailableTest_I_F_Status.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
		StateExecutorController.updateTestStatusGui(palletAvailableTest_I_F_Status);
		Ft.logger.debug(String.format("[%s] : [ERROR_HANDLING] : [GUI_STATUS_UPDATE] - Updating GUI status to COMPLETED for sequence: %s.", getMyBayKey(), palletAvailableTest_I_F_Status.getStateName()));

		// Structured log for sequence exit
		Ft.logger.info(String.format("[%s] : [ERROR_HANDLING] : [SEQUENCE_EXIT] - Error handling sequence completed. Final Status: %s, Error Code: %s", getMyBayKey(), bayResponse.getStatus(), bayResponse.getErrorCode()));
		return bayResponse;
	}


	public S22_error_Handling(){
        // Structured debug log for default constructor entry
        Ft.logger.debug(String.format("[%s] : [ERROR_HANDLER_CONSTRUCTOR] : [DEFAULT_INIT] - Default constructor called.", getMyBayKey()));
	}

	public S22_error_Handling(String errorCode) {
        // Structured info log for constructor entry with error code
        Ft.logger.info(String.format("[%s] : [ERROR_HANDLER_CONSTRUCTOR] : [INIT_WITH_CODE] - Initializing error handler for code: %s", getMyBayKey(), errorCode));

		switch (errorCode) {
		case ConvErrorCodeMapping.ERROR_CODE_FT_001:
		case ConvErrorCodeMapping.ERROR_CODE_FT_002:
		case ConvErrorCodeMapping.ERROR_CODE_FT_003:
		case ConvErrorCodeMapping.ERROR_CODE_FT_004:
		case ConvErrorCodeMapping.ERROR_CODE_FT_005:
		case ConvErrorCodeMapping.ERROR_CODE_FT_006:
		case ConvErrorCodeMapping.ERROR_CODE_FT_007:
		case ConvErrorCodeMapping.ERROR_CODE_FT_008:
		case ConvErrorCodeMapping.ERROR_CODE_FT_009:
		case ConvErrorCodeMapping.ERROR_CODE_FT_010:
		case ConvErrorCodeMapping.ERROR_CODE_FT_011:
		case ConvErrorCodeMapping.ERROR_CODE_FT_012:
		case ConvErrorCodeMapping.ERROR_CODE_FT_013:
		case ConvErrorCodeMapping.ERROR_CODE_FT_014:
		case ConvErrorCodeMapping.ERROR_CODE_FT_015:
		case ConvErrorCodeMapping.ERROR_CODE_FT_016:
		case ConvErrorCodeMapping.ERROR_CODE_FT_017:
		case ConvErrorCodeMapping.ERROR_CODE_FT_018:
		case ConvErrorCodeMapping.ERROR_CODE_FT_019:
		case ConvErrorCodeMapping.ERROR_CODE_FT_020:
		case ConvErrorCodeMapping.ERROR_CODE_FT_021:
		case ConvErrorCodeMapping.ERROR_CODE_FT_022:
		case ConvErrorCodeMapping.ERROR_CODE_FT_023:
		case ConvErrorCodeMapping.ERROR_CODE_FT_024:
		case ConvErrorCodeMapping.ERROR_CODE_FT_025:
		case ConvErrorCodeMapping.ERROR_CODE_FT_026:
		case ConvErrorCodeMapping.ERROR_CODE_FT_027:
		case ConvErrorCodeMapping.ERROR_CODE_FT_028:
		case ConvErrorCodeMapping.ERROR_CODE_FT_029:
		case ConvErrorCodeMapping.ERROR_CODE_FT_030:
		case ConvErrorCodeMapping.ERROR_CODE_FT_031:
		case ConvErrorCodeMapping.ERROR_CODE_FT_032:
		case ConvErrorCodeMapping.ERROR_CODE_FT_033:
		case "TEST":
		default:
            Ft.logger.debug(String.format("[%s] : [ERROR_HANDLER_CONSTRUCTOR] : [ACTION] - Handling error code %s: Scheduling FunctionalTestBayStop task.", getMyBayKey(), errorCode));
			funtionalBayStopTaskTimer = new Timer();
			funtionalBayStopTaskTimer.schedule(new FunctionalTestBayStop(),100);


            Ft.logger.debug(String.format("[%s] : [ERROR_HANDLER_CONSTRUCTOR] : [ACTION] - Task for error code %s cancelled.", getMyBayKey(), errorCode));
			break;
		}

		bayResponse.setErrorCode(errorCode);
	}

	public void Sleep(int timeInMsec) {
        Ft.logger.debug(String.format("[%s] : [UTILITY] : [SLEEP_ENTRY] - Sleeping for %d ms.", getMyBayKey(), timeInMsec));
		try {
			Thread.sleep(timeInMsec);
            Ft.logger.debug(String.format("[%s] : [UTILITY] : [SLEEP_EXIT] - Sleep completed.", getMyBayKey()));
		} catch (InterruptedException e) {
			// Structured error log for InterruptedException
			Ft.logger.error(String.format("[%s] : [UTILITY] : [SLEEP_INTERRUPTED] - Sleep interrupted: %s", getMyBayKey(), e.getMessage()), e);
			Thread.currentThread().interrupt(); // Re-interrupt the thread
		}
	}

    // Dummy FunctionalTestBayStop class for compilation, replace with actual implementation
    // This class is not provided in the original snippet but is referenced.
    // Assuming it extends TimerTask and is in the same package or imported.
    private class FunctionalTestBayStop extends TimerTask {
        @Override
        public void run() {
            Ft.logger.debug(String.format("[%s] : [FunctionalTestBayStop] : [RUN] - FunctionalTestBayStop task executed.", getMyBayKey()));
            // Actual stop logic would go here
            // Example: Ft.stopProcess();
        }
    }
}
