package com.tasnetwork.calibration.energymeter.director;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantDut;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
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

	/*
	 * public boolean setPowerSourceOff() {
	 * 
	 * boolean status = false;
	 * //Sleep(30000);
	 * if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
	 * 
	 * 
	 * }else if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
	 * //BofaManager.setBofaPowerSourceOff();
	 * DutMessengerV3 pwrSrcBofaMessenger = new DutMessengerV3();
	 * //status = pwrSrcBofaMessenger.sendVoltageCurrentStopOutputCommand();
	 * }else {
	 * 
	 * }
	 * return status;
	 * }
	 */

	/*
	 * public boolean setPowerSourceMctNctMode(String mctNctMode, boolean forceSet)
	 * {
	 * 
	 * boolean status = false;
	 * //Sleep(30000);
	 * if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
	 * 
	 * 
	 * }else if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
	 * //BofaManager.setBofaPowerSourceOff();
	 * DutMessengerV3 pwrSrcBofaMessenger = new DutMessengerV3();
	 * //status = pwrSrcBofaMessenger.bofaSetPowerSourceMctNctMode(mctNctMode,
	 * forceSet);
	 * }else {
	 * 
	 * }
	 * return status;
	 * }
	 */

	public DutResponse dutMsngrSendCommandProcess() {

		boolean status = false;
		// Sleep(30000);
		/*
		 * if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
		 * 
		 * 
		 * }else if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
		 */
		// BofaManager.setBofaPowerSourceOff();
		// DutCmdMessengerV3 dutMessenger = new DutCmdMessengerV3();
		String payLoadInHex = "912345670D0A";
		int timeDelayInMilliSec = 0;
		String expectedDataInHex = "91234567";
		boolean isResponseExpected = true;
		String sourceThread = "dutMsngrSendCommandProcess";
		DutResponse dutResponse = getDutMessenger().dutMsngrSendCommandProcess(payLoadInHex, timeDelayInMilliSec,
				expectedDataInHex, isResponseExpected, sourceThread);
		/*
		 * }else {
		 * 
		 * }
		 */
		return dutResponse;
	}

	public DutResponse dutMsngrSendCommandProcessV2(int dutPositionNo, DutCommand dutCommand, String sourceThread) {

		boolean status = false;
		// Sleep(30000);
		/*
		 * if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
		 * 
		 * 
		 * }else if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
		 */
		// BofaManager.setBofaPowerSourceOff();
		// DutCmdMessengerV3 dutMessenger = new DutCmdMessengerV3();
		/*
		 * String payLoadInHex="912345670D0A";
		 * int timeDelayInMilliSec = 0;
		 * String expectedDataInHex ="91234567";
		 * boolean isResponseExpected= true;
		 */
		// String sourceThread ="dutMsngrSendCommandProcessV2";
		// ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessV2-X :
		// isDutSerialStatusConnected: "+
		// getDutMessenger().getDutCmdSpmObj().isDutSerialStatusConnected());
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
