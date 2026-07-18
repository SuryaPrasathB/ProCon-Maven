package com.tasnetwork.calibration.conveyor.serial.director;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.bay.EIC_MegaOhmMeter;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.serial.messenger.DutMessenger;
import com.tasnetwork.calibration.conveyor.serial.messenger.MegaOhmPmMessenger;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmDut;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmMegaOhmPm;
import com.tasnetwork.calibration.conveyor.util.GUIUtils;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

public class MegaOhmPmDirector {

	private SpmMegaOhmPm serialPortManager = new SpmMegaOhmPm();

	public MegaOhmPmDirector(SpmMegaOhmPm serialPortManagerQrScanner) {
		this.serialPortManager = serialPortManagerQrScanner;
	}

	public MegaOhmPmDirector() {

	}

	// ==============================================================================================================================
	public Map<String, Object> fetchMegaOhmMetrics(String slaveId) {

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

		if (ProconFeatureEnable.EIC_MEGA_OHM_PANEL_METER) {
			MegaOhmPmMessenger pwrSrcBofaMessenger = new MegaOhmPmMessenger(getSerialPortManager());
			responseMap = pwrSrcBofaMessenger.sendReadCommandEicMegaOhmMeter(slaveId);
		}
		/*
		 * }else {
		 * 
		 * }
		 */
		return responseMap;
	}

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

	public SpmMegaOhmPm getSerialPortManager() {
		return serialPortManager;
	}

	public void setSerialPortManager(SpmMegaOhmPm spManager) {
		this.serialPortManager = spManager;
	}

}
