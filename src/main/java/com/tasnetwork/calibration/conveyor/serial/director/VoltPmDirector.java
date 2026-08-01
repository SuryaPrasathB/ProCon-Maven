package com.tasnetwork.calibration.conveyor.serial.director;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.serial.messenger.VoltPmMessenger;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmVoltPm;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

public class VoltPmDirector {

	private SpmVoltPm serialPortManager = new SpmVoltPm();

	public VoltPmDirector(SpmVoltPm serialPortManagerQrScanner) {
		this.serialPortManager = serialPortManagerQrScanner;
	}

	public VoltPmDirector() {

	}

	// ===============================================================================================================
	public Map<String, Object> fetchVoltMeterMetrics(String slaveId) {

		Map<String, Object> responseMap = new HashMap<String, Object>();

		if (ProconFeatureEnable.ELMEASURE_SL1300_MULTI_METER) {
			VoltPmMessenger pwrSrcBofaMessenger = new VoltPmMessenger(getSerialPortManager());
			responseMap = pwrSrcBofaMessenger.sendReadCommandVoltPm(slaveId);
		}
		/*
		 * }else {
		 * 
		 * }
		 */
		return responseMap;
	}

	// =====================================================================================================================


	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("PowerSourceDirector: Sleep2 :InterruptedException:" + e.getMessage());
		}

	}

	public SpmVoltPm getSerialPortManager() {
		return serialPortManager;
	}

	public void setSerialPortManager(SpmVoltPm spManager) {
		this.serialPortManager = spManager;
	}

}
