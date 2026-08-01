package com.tasnetwork.calibration.energymeter.director;

import java.util.Timer;

import com.tasnetwork.calibration.energymeter.deployment.DutResponse;
import com.tasnetwork.calibration.energymeter.messenger.DutCmdMessengerV3;
import com.tasnetwork.calibration.energymeter.serial.portmanagerV2.SerialPortManagerDutCmd_V3;
import com.tasnetwork.spring.orm.model.DutCommand;

public class DutCmdDirectorV3 {

	DutCmdMessengerV3 dutMessenger = new DutCmdMessengerV3();
	Timer dutCommandTimer;

	public DutCmdDirectorV3(SerialPortManagerDutCmd_V3 dutSpm) {
		// dutMessenger
		dutMessenger.setDutCmdSpmObj(dutSpm);

	}

	public DutResponse dutMsngrSendCommandProcess() {

		boolean status = false;

		String payLoadInHex = "912345670D0A";
		int timeDelayInMilliSec = 0;
		String expectedDataInHex = "91234567";
		boolean isResponseExpected = true;
		String sourceThread = "dutMsngrSendCommandProcess";
		DutResponse dutResponse = getDutMessenger().dutMsngrSendCommandProcess(payLoadInHex, timeDelayInMilliSec,
				expectedDataInHex, isResponseExpected, sourceThread);

		return dutResponse;
	}

	public DutResponse dutMsngrSendCommandProcessV2(int dutPositionNo, DutCommand dutCommand, String sourceThread) {

		boolean status = false;

		DutResponse dutResponse = getDutMessenger().dutMsngrSendCommandProcessV2(dutPositionNo, dutCommand,
				sourceThread);
		/*
		 * }else {
		 * 
		 * }
		 */
		return dutResponse;
	}

	public DutCmdMessengerV3 getDutMessenger() {
		return dutMessenger;
	}

	public void setDutMessenger(DutCmdMessengerV3 dutMessenger) {
		this.dutMessenger = dutMessenger;
	}

	/*
	 * public void dutExecuteCommandTrigger() {
	 * ApplicationLauncher.logger.debug("dutExecuteCommandTrigger :Entry");
	 * dutCommandTimer = new Timer();
	 * dutCommandTimer.schedule(new dutExecuteCommandTask(), 100);
	 * ApplicationLauncher.logger.debug("dutExecuteCommandTrigger : Exit");
	 * 
	 * }
	 */

}
