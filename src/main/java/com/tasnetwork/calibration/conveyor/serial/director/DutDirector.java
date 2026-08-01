package com.tasnetwork.calibration.conveyor.serial.director;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.serial.messenger.DutMessenger;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmDut;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

public class DutDirector {

	private SpmDut serialPortManager = new SpmDut();

	public DutDirector(SpmDut serialPortManagerQrScanner) {
		this.serialPortManager = serialPortManagerQrScanner;
	}

	public DutDirector() {

	}

	public Map<String, Object> fetchDutSerialNumber() {

		Map<String, Object> responseMap = new HashMap<String, Object>();

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

		Map<String, Object> responseMap = new HashMap<String, Object>();

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
