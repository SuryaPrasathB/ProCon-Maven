package com.tasnetwork.calibration.conveyor.serial.director;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.bay.EIC_MegaOhmMeter;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.serial.messenger.DutMessenger;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmDut;
import com.tasnetwork.calibration.conveyor.util.GUIUtils;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

public class DutDirector {

	private SpmDut serialPortManager = new SpmDut();

	public DutDirector(SpmDut serialPortManagerQrScanner) {
		this.serialPortManager = serialPortManagerQrScanner;
	}

	public DutDirector() {

	}

	public Map<String, Object> fetchDutSerialNumber() {

		boolean status = false;
		Map<String, Object> responseMap = new HashMap<String, Object>();
		// Sleep(30000);
		/*
		 * if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
		 * 
		 * 
		 * }else if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
		 */
		// BofaManager.setBofaPowerSourceOff();
		// PowerSourceBofaMessenger pwrSrcBofaMessenger = new
		// PowerSourceBofaMessenger();
		// status = pwrSrcBofaMessenger.sendVoltageCurrentStopOutputCommand();

		if (ProconFeatureEnable.DEV_SYS_EM1_CONNECTED) {
			DutMessenger pwrSrcBofaMessenger = new DutMessenger(getSerialPortManager());
			responseMap = pwrSrcBofaMessenger.sendReadSerialNumberCommandToDut();
		}
		/*
		 * }else {
		 * 
		 * }
		 */
		return responseMap;
	}

	public Map<String, Object> sendCommandToDut(String payLoadInHex, String expectedDataInHex) {

		boolean status = false;
		Map<String, Object> responseMap = new HashMap<String, Object>();
		// Sleep(30000);
		/*
		 * if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
		 * 
		 * 
		 * }else if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
		 */
		// BofaManager.setBofaPowerSourceOff();
		// PowerSourceBofaMessenger pwrSrcBofaMessenger = new
		// PowerSourceBofaMessenger();
		// status = pwrSrcBofaMessenger.sendVoltageCurrentStopOutputCommand();

		if (ProconFeatureEnable.DEV_SYS_EM1_CONNECTED) {
			DutMessenger pwrSrcBofaMessenger = new DutMessenger(getSerialPortManager());
			responseMap = pwrSrcBofaMessenger.sendCommandToDut(payLoadInHex, expectedDataInHex);
		}
		/*
		 * }else {
		 * 
		 * }
		 */
		return responseMap;
	}
	// ==============================================================================================================================

	// =====================================================================================================================
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
	 * PowerSourceBofaMessenger pwrSrcBofaMessenger = new
	 * PowerSourceBofaMessenger();
	 * status = pwrSrcBofaMessenger.bofaSetPowerSourceMctNctMode(mctNctMode,
	 * forceSet);
	 * }else {
	 * 
	 * }
	 * return status;
	 * }
	 */

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("PowerSourceDirector: Sleep2 :InterruptedException:" + e.getMessage());
		}

	}

	public SpmDut getSerialPortManager() {
		return serialPortManager;
	}

	public void setSerialPortManager(SpmDut serialPortManagerQrScanner) {
		this.serialPortManager = serialPortManagerQrScanner;
	}

}
