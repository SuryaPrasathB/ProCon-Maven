package com.tasnetwork.calibration.conveyor.serial.director;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.serial.messenger.QrScannerMessenger;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmQrScanner;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

public class QrScannerDirector {

	private SpmQrScanner serialPortManager = new SpmQrScanner();

	public QrScannerDirector(SpmQrScanner serialPortManagerQrScanner) {
		this.serialPortManager = serialPortManagerQrScanner;
	}

	public QrScannerDirector() {

	}

	public Map<String, Object> scanQrCode() {

		Map<String, Object> responseMap = new HashMap<String, Object>();

		if (ProconFeatureEnable.NEWLAND_QR_SCANNER_CONNECTED) {
			QrScannerMessenger pwrSrcBofaMessenger = new QrScannerMessenger(getSerialPortManager());
			responseMap = pwrSrcBofaMessenger.sendReadCommandQrCodeScanner();
		}
		/*
		 * }else {
		 * 
		 * }
		 */
		return responseMap;
	}

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("PowerSourceDirector: Sleep2 :InterruptedException:" + e.getMessage());
		}

	}

	public SpmQrScanner getSerialPortManager() {
		return serialPortManager;
	}

	public void setSerialPortManager(SpmQrScanner serialPortManagerQrScanner) {
		this.serialPortManager = serialPortManagerQrScanner;
	}
}
