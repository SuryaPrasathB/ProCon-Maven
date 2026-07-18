package com.tasnetwork.calibration.conveyor.bay;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantDutDevSys;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.database.MySQL_Controller;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.serial.director.DutDirector;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmDut;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;
import com.tasnetwork.spring.orm.model.DeviceSetting;
import com.tasnetwork.spring.orm.model.TerminalProfileSetting;

public class DevSysEnergyMeter {

	public DevSysEnergyMeter(Logger logger) {
		this.eachBaylogger = logger;
	}

	private Logger eachBaylogger = null;

	public static final String DEVUNLOCK = "646576756E6C6F636B0D";
	public static final String DEVSBL = "64657673624C50617373776F726431323334353637380D";
	public static final String DEVGCSM = "6465766763736D0D";
	public static final String DEVGSR = "6465766773720D";
	public static final String DEVGLN = "646576676C6E0D";
	public static final String DEVGPCB = "646576677063620D";
	public static final String DEVGNV256 = "646576676E763235360D";
	public static final String DEVGNV128 = "646576676E763132380D";
	public static final String DEVGNV16 = "646576676E7631360D";
	public static final String DEVGINST = "64657667696E7374720D";
	public static final String DEVSMM = "646576736D6D+2bytes(year)+0D";
	public static final String DEVSDMDT = "64657673646D64740D+1byte(md type)";
	public static final String DEVSDPWT = "64657673647077740D+1byte(power type)";
	public static final String DEVSSR = "646576737372";// 31 32 33 34 35 36 49444556303031 0D";
	public static final String DEVSSR_NEW = "64657673707372";// 31 32 33 34 35 36 49444556303031 0D";
	public static final String DEVSLN = "646576736C6E+(01010101202020203030)+10bytes+(0D)";
	public static final String DEVSPCB = "64657673706362284D4B4D33345A32353631)+(0D)";
	public static final String DEVGRTC = "646576677274630D";
	public static final String DEVSRTC = "6465767372746318030D111E010D";
	public static final String DEVBSP = "64657673627050617373776F726431323334353637380D";
	public static final String DEVMER = "6465766D65720D";
	public static final String DEVSCALL = "6465767363616C6C300D";
	public static final String DEVCALP = "64657663616C700D";
	public static final String DEVCALN = "64657663616C6E0D";
	public static final String DEVDCAL = "6465766463616C0D";
	public static final String DEVXTLOUTE = "64657678746C6F7574650D";
	public static final String DEVXTLOUTD = "64657678746C6F7574640D";
	public static final String DEVRTCCOMPS = "646576727463636F6D70732+(1byte)rtc compensation interval+(1byte)rtc comp value+0D";
	public static final String DEVRTCCOMPG = "646576727463636F6D70670D";
	public static final String DEVGETENERGY = "646576676574656E657267790D";
	public static final String DEVSIP = "646576736970323430313A383830303A306531313A3A310D";
	public static final String DEVSPORT = "64657673706F7274373030300D";
	public static final String DEVSAPN = "6465767361706E41495254454C4D4554455256360D";
	public static final String DEVRELAYON = "64657672656C61796F6E0D";
	public static final String DEVRELAYOFF = "64657672656C61796F66660D";
	public static final String DEVTAMERSS = "64657674616D657273733635343332310D";
	public static final String DEVSTNDBYMODE = "64657673746E6462796D6F64650D";
	public static final String DEVRELAYTESTSTART = "64657672656C61797465737473746172740D";
	public static final String DEVRELAYTESTSTOP = "64657672656C61797465737473746F700D";
	public static final String DEVERSCONTIMEADDR = "646576657273636F6E74696D65616464720D";
	public static final String DEVGPRSSTATE = "6465766770727373746174650D";

	public static final String expectedData = "EXPECTED_DATA";
	public static final String terminator = "TERMINATOR";

	public static final String UNLOCKED = "UnLocked";
	public static final String LOCKED = "Locked";
	public static final String START = "Start";
	public static final String PASS = "PASS=";
	public static final String FAIL = "FAIL=";
	public static final String ER_DATA_IN_HEX = "02";
	public static final String TERMINATOR = "030D0A";
	public static final String ER_SERIAL_NO_WRITE_DATA_IN_HEX = "024F4B";
	public static final String CMD_SERIAL_NO_WRITE_TERMINATOR_IN_HEX = "0D";
	// ============================================================================================================================================

	public boolean calibrationProcess(int positionNum) {
		eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : Entry");

		boolean status = false;
		String portCname = "";

		// DevSysEnergyMeter devSysEnergyMeter = new DevSysEnergyMeter();
		// TerminalProfileModel terminalProfile = new TerminalProfileModel();
		TerminalProfileSetting terminalBayProfile = new TerminalProfileSetting();
		terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService()
				.findByBayKey(ConstantConveyor.CALIBRATION_BAY_KEY);

		String deviceId = terminalBayProfile.getTerminalId() + terminalBayProfile.getClusterId() +
				terminalBayProfile.getBayId() +
				ConstantConveyor.DEVICE_TYPE_DUT +
				String.format("%02d", positionNum);
		eachBaylogger.debug("DevSysEnergyMeter : deviceId : " + deviceId);
		SpmDut spManager = null;
		if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {

			ConveyorDataManager deviceDataManager = new ConveyorDataManager();
			DeviceSetting deviceSetting = deviceDataManager.getDeviceSettingByDeviceId(deviceId);
			if (deviceSetting != null) {
				spManager = serialPortInitV2(deviceSetting);
			} else {
				eachBaylogger.debug("calibrationProcess: " + positionNum + " Failed : Com port not configured");

			}
		} else {
			try {
				JSONObject devicePortSetting = MySQL_Controller.sp_getdevice_setting_v2(deviceId);

				portCname = devicePortSetting.getString("c_name");
				eachBaylogger.debug("DevSysEnergyMeter : portCname : " + portCname);
				spManager = serialPortInit(portCname);
			} catch (JSONException e) {

				e.printStackTrace();
				eachBaylogger.error("DevSysEnergyMeter : calibrationProcess : Exception : " + e.getMessage());
			}
		}
		// String portCname = "";
		// =============
		/*
		 * if (positionNum == 1) {
		 * portCname = "CALIB_BAY_DUT1";
		 * }
		 * //=============
		 * else if (positionNum == 2) {
		 * portCname = "CALIB_BAY_DUT2";
		 * }
		 * //=============
		 * else if (positionNum == 3) {
		 * portCname = "CALIB_BAY_DUT3";
		 * }
		 * //=============
		 * else if (positionNum == 4) {
		 * portCname = "CALIB_BAY_DUT4";
		 * }
		 * //=============
		 * else if (positionNum == 5) {
		 * portCname = "CALIB_BAY_DUT5";
		 * }
		 * //=============
		 * else if (positionNum == 6) {
		 * portCname = "CALIB_BAY_DUT6";
		 * }
		 */
		// =============

		// SpmDut spManager = serialPortInit(portCname);
		if (spManager == null) {
			return status;
		}

		// 3.1 device unlock
		status = sendDeviceUnlockCommand(positionNum, spManager); // direct to energy meters

		if (status) {
			eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : Position No: " + positionNum
					+ " : sending Device Unlock Command : Success ");
			// status = devSysEnergyMeter.sendMemoryClearCommand(spManager); // clear
			// previous calibration values
		} else {
			status = false;
			eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : Position No: " + positionNum
					+ " : sending Device Unlock Command : Failed ");
			spManager.disconnectDut();
			return status;
		}

		// 3.2 change circuit to main ct.
		// pending

		// 3.2 device calibration in phase circuit
		if (status) {
			eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sending Memory Clear Command : Success ");
			status = sendDeviceCalibInPhaseCktCommand(positionNum, spManager);
		} else {
			status = false;
			eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sending Memory Clear Command : Failed ");
			spManager.disconnectDut();
			return status;
		}

		// 3.3 change circuit to neutral ct.
		// pending

		// 3.4 device calibration in neutral circuit
		if (status) {
			eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : Position No: " + positionNum
					+ " : sending Device Calib In Phase Circuit Command: Success ");
			status = sendDeviceCalibInNeutralCktCommand(positionNum, spManager);
		} else {
			status = false;
			eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : Position No: " + positionNum
					+ " : sending Device Calib In Phase Circuit Command: Failed ");
			spManager.disconnectDut();
			return status;
		}
		// =============================
		// 3.5 calibration lock
		/*
		 * if (status) {
		 * eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : Position No: "
		 * + positionNum
		 * +" : sending Device Calib In Neutral Circuit Command: Success ");
		 * status = sendCalibrationLockCommand(positionNum,spManager);
		 * } else {
		 * status = false ;
		 * eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : Position No: "
		 * + positionNum
		 * +" : sending Device Calib In Neutral Circuit Command: Failed ");
		 * spManager.disconnectDut();
		 * return status;
		 * }
		 */

		// ACCURACY - READ CURRENT
		// 4.1 read phase current
		/*
		 * if (status) {
		 * eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : Position No: "
		 * + positionNum +" : sending Device Calibration Lock Command: Success ");
		 * status = devSysEnergyMeter.sendReadPhaseCurrentCommand(spManager);
		 * } else {
		 * status = false ;
		 * eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : Position No: "
		 * + positionNum +" : sending Device Calibration Lock Command: Failed ");
		 * spManager.disconnectPwrSrc();
		 * return status;
		 * }
		 * 
		 * // 4.2 read neutral current
		 * if (status) {
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : calibrationProcess : Reading Phase Current : Success "
		 * );
		 * status = devSysEnergyMeter.sendReadNeutralCurrentCommand(spManager);
		 * } else {
		 * status = false ;
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : calibrationProcess : Reading Phase Current : Failed "
		 * );
		 * spManager.disconnectPwrSrc();
		 * return status;
		 * }
		 * 
		 * if (status) {
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : calibrationProcess : Reading Neutral Current : Success "
		 * );
		 * } else {
		 * status = false ;
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : calibrationProcess : Reading Neutral Current : Failed "
		 * );
		 * spManager.disconnectPwrSrc();
		 * return status;
		 * }
		 */

		if (status) {
			eachBaylogger.debug("DevSysEnergyMeter : ********************************************************");
			eachBaylogger.debug("DevSysEnergyMeter : ********************************************************");
			eachBaylogger.debug("DevSysEnergyMeter : ********************************************************");
			eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : Position No: " + positionNum
					+ " : sending Device Calibration Lock Command: Success ");
			eachBaylogger.debug("DevSysEnergyMeter : ********************************************************");
			eachBaylogger.debug("DevSysEnergyMeter : ********************************************************");
			eachBaylogger.debug("DevSysEnergyMeter : ********************************************************");
		} else {
			status = false;
			eachBaylogger.debug("DevSysEnergyMeter : =========================================================");
			eachBaylogger.debug("DevSysEnergyMeter : =========================================================");
			eachBaylogger.debug("DevSysEnergyMeter : =========================================================");
			eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : Position No: " + positionNum
					+ " : sending Device Calibration Lock Command: Failed ");
			eachBaylogger.debug("DevSysEnergyMeter : =========================================================");
			eachBaylogger.debug("DevSysEnergyMeter : =========================================================");
			eachBaylogger.debug("DevSysEnergyMeter : =========================================================");
			spManager.disconnectDut();
			return status;
		}
		spManager.disconnectDut();

		eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : Exit");
		return status;
	}

	// ===================

	public boolean phaseCalibrationProcess(int positionNum) {
		eachBaylogger.debug("DevSysEnergyMeter : phaseCalibrationProcess : Entry");

		boolean status = false;
		String portCname = "";

		// DevSysEnergyMeter devSysEnergyMeter = new DevSysEnergyMeter();
		// TerminalProfileModel terminalProfile = new TerminalProfileModel();
		TerminalProfileSetting terminalBayProfile = new TerminalProfileSetting();
		terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService()
				.findByBayKey(ConstantConveyor.CALIBRATION_BAY_KEY);

		String deviceId = terminalBayProfile.getTerminalId() + terminalBayProfile.getClusterId() +
				terminalBayProfile.getBayId() +
				ConstantConveyor.DEVICE_TYPE_DUT +
				String.format("%02d", positionNum);
		eachBaylogger.debug("DevSysEnergyMeter : deviceId : " + deviceId);
		SpmDut spManager = null;
		if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {

			ConveyorDataManager deviceDataManager = new ConveyorDataManager();
			DeviceSetting deviceSetting = deviceDataManager.getDeviceSettingByDeviceId(deviceId);
			if (deviceSetting != null) {
				spManager = serialPortInitV2(deviceSetting);
			} else {
				eachBaylogger.debug("phaseCalibrationProcess: " + positionNum + " Failed : Com port not configured");

			}
		} else {
			try {
				JSONObject devicePortSetting = MySQL_Controller.sp_getdevice_setting_v2(deviceId);

				portCname = devicePortSetting.getString("c_name");
				eachBaylogger.debug("DevSysEnergyMeter : portCname : " + portCname);
				spManager = serialPortInit(portCname);
			} catch (JSONException e) {

				e.printStackTrace();
				eachBaylogger.error("DevSysEnergyMeter : phaseCalibrationProcess : Exception : " + e.getMessage());
			}
		}
		// String portCname = "";
		// =============
		/*
		 * if (positionNum == 1) {
		 * portCname = "CALIB_BAY_DUT1";
		 * }
		 * //=============
		 * else if (positionNum == 2) {
		 * portCname = "CALIB_BAY_DUT2";
		 * }
		 * //=============
		 * else if (positionNum == 3) {
		 * portCname = "CALIB_BAY_DUT3";
		 * }
		 * //=============
		 * else if (positionNum == 4) {
		 * portCname = "CALIB_BAY_DUT4";
		 * }
		 * //=============
		 * else if (positionNum == 5) {
		 * portCname = "CALIB_BAY_DUT5";
		 * }
		 * //=============
		 * else if (positionNum == 6) {
		 * portCname = "CALIB_BAY_DUT6";
		 * }
		 */
		// =============

		// SpmDut spManager = serialPortInit(portCname);
		if (spManager == null) {
			return status;
		}

		// 3.1 device unlock
		status = sendDeviceUnlockCommand(positionNum, spManager); // direct to energy meters

		if (status) {
			eachBaylogger.debug("DevSysEnergyMeter : phaseCalibrationProcess : Position No: " + positionNum
					+ " : sending Device Unlock Command : Success ");
			// status = devSysEnergyMeter.sendMemoryClearCommand(spManager); // clear
			// previous calibration values
		} else {
			status = false;
			eachBaylogger.debug("DevSysEnergyMeter : phaseCalibrationProcess : Position No: " + positionNum
					+ " : sending Device Unlock Command : Failed ");
			spManager.disconnectDut();
			return status;
		}

		// 3.2 change circuit to main ct.
		// pending

		// 3.2 device calibration in phase circuit
		if (status) {
			eachBaylogger
					.debug("DevSysEnergyMeter : phaseCalibrationProcess : sending Memory Clear Command : Success ");
			status = sendDeviceCalibInPhaseCktCommand(positionNum, spManager);
		} else {
			status = false;
			eachBaylogger.debug("DevSysEnergyMeter : phaseCalibrationProcess : sending Memory Clear Command : Failed ");
			spManager.disconnectDut();
			return status;
		}

		// 3.3 change circuit to neutral ct.
		// pending

		// 3.4 device calibration in neutral circuit
		/*
		 * if (status) {
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : phaseCalibrationProcess : Position No: " +
		 * positionNum +" : sending Device Calib In Phase Circuit Command: Success ");
		 * status = sendDeviceCalibInNeutralCktCommand(positionNum,spManager);
		 * } else {
		 * status = false ;
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : phaseCalibrationProcess : Position No: " +
		 * positionNum +" : sending Device Calib In Phase Circuit Command: Failed ");
		 * spManager.disconnectDut();
		 * return status;
		 * }
		 */
		// =============================
		// 3.5 calibration lock
		/*
		 * if (status) {
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : phaseCalibrationProcess : Position No: " +
		 * positionNum +" : sending Device Calib In Neutral Circuit Command: Success ");
		 * status = sendCalibrationLockCommand(positionNum,spManager);
		 * } else {
		 * status = false ;
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : phaseCalibrationProcess : Position No: " +
		 * positionNum +" : sending Device Calib In Neutral Circuit Command: Failed ");
		 * spManager.disconnectDut();
		 * return status;
		 * }
		 */

		// ACCURACY - READ CURRENT
		// 4.1 read phase current
		/*
		 * if (status) {
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : phaseCalibrationProcess : Position No: " +
		 * positionNum +" : sending Device Calibration Lock Command: Success ");
		 * status = devSysEnergyMeter.sendReadPhaseCurrentCommand(spManager);
		 * } else {
		 * status = false ;
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : phaseCalibrationProcess : Position No: " +
		 * positionNum +" : sending Device Calibration Lock Command: Failed ");
		 * spManager.disconnectPwrSrc();
		 * return status;
		 * }
		 * 
		 * // 4.2 read neutral current
		 * if (status) {
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : phaseCalibrationProcess : Reading Phase Current : Success "
		 * );
		 * status = devSysEnergyMeter.sendReadNeutralCurrentCommand(spManager);
		 * } else {
		 * status = false ;
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : phaseCalibrationProcess : Reading Phase Current : Failed "
		 * );
		 * spManager.disconnectPwrSrc();
		 * return status;
		 * }
		 * 
		 * if (status) {
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : phaseCalibrationProcess : Reading Neutral Current : Success "
		 * );
		 * } else {
		 * status = false ;
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : phaseCalibrationProcess : Reading Neutral Current : Failed "
		 * );
		 * spManager.disconnectPwrSrc();
		 * return status;
		 * }
		 */

		if (status) {
			eachBaylogger.debug("DevSysEnergyMeter : ********************************************************");
			eachBaylogger.debug("DevSysEnergyMeter : ********************************************************");
			eachBaylogger.debug("DevSysEnergyMeter : ********************************************************");
			eachBaylogger.debug("DevSysEnergyMeter : phaseCalibrationProcess : Position No: " + positionNum
					+ " : sending Device Calib In Phase Circuit: Success ");
			eachBaylogger.debug("DevSysEnergyMeter : ********************************************************");
			eachBaylogger.debug("DevSysEnergyMeter : ********************************************************");
			eachBaylogger.debug("DevSysEnergyMeter : ********************************************************");
		} else {
			status = false;
			eachBaylogger.debug("DevSysEnergyMeter : =========================================================");
			eachBaylogger.debug("DevSysEnergyMeter : =========================================================");
			eachBaylogger.debug("DevSysEnergyMeter : =========================================================");
			eachBaylogger.debug("DevSysEnergyMeter : phaseCalibrationProcess : Position No: " + positionNum
					+ " : sending Device Calib In Phase Circuit: Failed ");
			eachBaylogger.debug("DevSysEnergyMeter : =========================================================");
			eachBaylogger.debug("DevSysEnergyMeter : =========================================================");
			eachBaylogger.debug("DevSysEnergyMeter : =========================================================");
			spManager.disconnectDut();
			return status;
		}
		spManager.disconnectDut();

		eachBaylogger.debug("DevSysEnergyMeter : phaseCalibrationProcess : Exit");
		return status;
	}

	// ================================

	public boolean neutralCalibrationProcess(int positionNum) {
		eachBaylogger.debug("DevSysEnergyMeter : neutralCalibrationProcess : Entry");

		boolean status = false;
		String portCname = "";

		// DevSysEnergyMeter devSysEnergyMeter = new DevSysEnergyMeter();
		// TerminalProfileModel terminalProfile = new TerminalProfileModel();
		TerminalProfileSetting terminalBayProfile = new TerminalProfileSetting();
		terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService()
				.findByBayKey(ConstantConveyor.CALIBRATION_BAY_KEY);

		String deviceId = terminalBayProfile.getTerminalId() + terminalBayProfile.getClusterId() +
				terminalBayProfile.getBayId() +
				ConstantConveyor.DEVICE_TYPE_DUT +
				String.format("%02d", positionNum);
		eachBaylogger.debug("DevSysEnergyMeter : deviceId : " + deviceId);
		SpmDut spManager = null;
		if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {

			ConveyorDataManager deviceDataManager = new ConveyorDataManager();
			DeviceSetting deviceSetting = deviceDataManager.getDeviceSettingByDeviceId(deviceId);
			if (deviceSetting != null) {
				spManager = serialPortInitV2(deviceSetting);
			} else {
				eachBaylogger.debug("neutralCalibrationProcess: " + positionNum + " Failed : Com port not configured");

			}
		} else {
			try {
				JSONObject devicePortSetting = MySQL_Controller.sp_getdevice_setting_v2(deviceId);

				portCname = devicePortSetting.getString("c_name");
				eachBaylogger.debug("DevSysEnergyMeter : portCname : " + portCname);
				spManager = serialPortInit(portCname);
			} catch (JSONException e) {

				e.printStackTrace();
				eachBaylogger.error("DevSysEnergyMeter : neutralCalibrationProcess : Exception : " + e.getMessage());
			}
		}
		// String portCname = "";
		// =============
		/*
		 * if (positionNum == 1) {
		 * portCname = "CALIB_BAY_DUT1";
		 * }
		 * //=============
		 * else if (positionNum == 2) {
		 * portCname = "CALIB_BAY_DUT2";
		 * }
		 * //=============
		 * else if (positionNum == 3) {
		 * portCname = "CALIB_BAY_DUT3";
		 * }
		 * //=============
		 * else if (positionNum == 4) {
		 * portCname = "CALIB_BAY_DUT4";
		 * }
		 * //=============
		 * else if (positionNum == 5) {
		 * portCname = "CALIB_BAY_DUT5";
		 * }
		 * //=============
		 * else if (positionNum == 6) {
		 * portCname = "CALIB_BAY_DUT6";
		 * }
		 */
		// =============

		// SpmDut spManager = serialPortInit(portCname);
		if (spManager == null) {
			return status;
		}

		// 3.1 device unlock
		status = sendDeviceUnlockCommand(positionNum, spManager); // direct to energy meters

		if (status) {
			eachBaylogger.debug("DevSysEnergyMeter : neutralCalibrationProcess : Position No: " + positionNum
					+ " : sending Device Unlock Command : Success ");
			// status = devSysEnergyMeter.sendMemoryClearCommand(spManager); // clear
			// previous calibration values
		} else {
			status = false;
			eachBaylogger.debug("DevSysEnergyMeter : neutralCalibrationProcess : Position No: " + positionNum
					+ " : sending Device Unlock Command : Failed ");
			spManager.disconnectDut();
			return status;
		}

		// 3.2 change circuit to main ct.
		// pending

		// 3.2 device calibration in phase circuit
		/*
		 * if (status) {
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : neutralCalibrationProcess : sending Memory Clear Command : Success "
		 * );
		 * status = sendDeviceCalibInPhaseCktCommand(positionNum,spManager);
		 * } else {
		 * status = false ;
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : neutralCalibrationProcess : sending Memory Clear Command : Failed "
		 * );
		 * spManager.disconnectDut();
		 * return status;
		 * }
		 */

		// 3.3 change circuit to neutral ct.
		// pending

		// 3.4 device calibration in neutral circuit
		if (status) {
			eachBaylogger.debug("DevSysEnergyMeter : neutralCalibrationProcess : Position No: " + positionNum
					+ " : sending Device Calib In Neutral Circuit Command: Success ");
			status = sendDeviceCalibInNeutralCktCommand(positionNum, spManager);
		} else {
			status = false;
			eachBaylogger.debug("DevSysEnergyMeter : neutralCalibrationProcess : Position No: " + positionNum
					+ " : sending Device Calib In Neutral Circuit Command: Failed ");
			spManager.disconnectDut();
			return status;
		}
		// =============================
		// 3.5 calibration lock
		/*
		 * if (status) {
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : neutralCalibrationProcess : Position No: " +
		 * positionNum +" : sending Device Calib In Neutral Circuit Command: Success ");
		 * status = sendCalibrationLockCommand(positionNum,spManager);
		 * } else {
		 * status = false ;
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : neutralCalibrationProcess : Position No: " +
		 * positionNum +" : sending Device Calib In Neutral Circuit Command: Failed ");
		 * spManager.disconnectDut();
		 * return status;
		 * }
		 */

		// ACCURACY - READ CURRENT
		// 4.1 read phase current
		/*
		 * if (status) {
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : neutralCalibrationProcess : Position No: " +
		 * positionNum +" : sending Device Calibration Lock Command: Success ");
		 * status = devSysEnergyMeter.sendReadPhaseCurrentCommand(spManager);
		 * } else {
		 * status = false ;
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : neutralCalibrationProcess : Position No: " +
		 * positionNum +" : sending Device Calibration Lock Command: Failed ");
		 * spManager.disconnectPwrSrc();
		 * return status;
		 * }
		 * 
		 * // 4.2 read neutral current
		 * if (status) {
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : neutralCalibrationProcess : Reading Phase Current : Success "
		 * );
		 * status = devSysEnergyMeter.sendReadNeutralCurrentCommand(spManager);
		 * } else {
		 * status = false ;
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : neutralCalibrationProcess : Reading Phase Current : Failed "
		 * );
		 * spManager.disconnectPwrSrc();
		 * return status;
		 * }
		 * 
		 * if (status) {
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : neutralCalibrationProcess : Reading Neutral Current : Success "
		 * );
		 * } else {
		 * status = false ;
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : neutralCalibrationProcess : Reading Neutral Current : Failed "
		 * );
		 * spManager.disconnectPwrSrc();
		 * return status;
		 * }
		 */

		if (status) {
			eachBaylogger.debug("DevSysEnergyMeter : ********************************************************");
			eachBaylogger.debug("DevSysEnergyMeter : ********************************************************");
			eachBaylogger.debug("DevSysEnergyMeter : ********************************************************");
			eachBaylogger.debug("DevSysEnergyMeter : neutralCalibrationProcess : Position No: " + positionNum
					+ " : sending Device Calib In Neutral: Success ");
			eachBaylogger.debug("DevSysEnergyMeter : ********************************************************");
			eachBaylogger.debug("DevSysEnergyMeter : ********************************************************");
			eachBaylogger.debug("DevSysEnergyMeter : ********************************************************");
		} else {
			status = false;
			eachBaylogger.debug("DevSysEnergyMeter : =========================================================");
			eachBaylogger.debug("DevSysEnergyMeter : =========================================================");
			eachBaylogger.debug("DevSysEnergyMeter : =========================================================");
			eachBaylogger.debug("DevSysEnergyMeter : neutralCalibrationProcess : Position No: " + positionNum
					+ " : sending Device Calib In Neutral: Failed ");
			eachBaylogger.debug("DevSysEnergyMeter : =========================================================");
			eachBaylogger.debug("DevSysEnergyMeter : =========================================================");
			eachBaylogger.debug("DevSysEnergyMeter : =========================================================");
			spManager.disconnectDut();
			return status;
		}
		spManager.disconnectDut();

		eachBaylogger.debug("DevSysEnergyMeter : neutralCalibrationProcess : Exit");
		return status;
	}

	// ======================================================

	public BayResponse sendReadPhaseCurrentCommandV2(SpmDut spManager) {

		eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendReadPhaseCurrentCommandV2 : Entry");
		boolean status = false;

		BayResponse bayResponse = new BayResponse();

		// String currentValue = "" ;

		/*
		 * String receivedData = "";//sendCommand(DevSysEnergyMeterCommands.DEVGINST)
		 * // receivedData =
		 * "02004B0000FFFFFFFF0000300000000000000200000002004B5F2F0031010101030D0A" ;
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : calibrationProcess : sendReadPhaseCurrentCommand : receivedData : "
		 * + receivedData);
		 */
		DutDirector pwrSrcDirector = new DutDirector(spManager);
		Map<String, Object> responseMap = pwrSrcDirector.sendCommandToDut(DevSysEnergyMeter.DEVGINST,
				DevSysEnergyMeter.ER_DATA_IN_HEX);
		if (!((boolean) responseMap.get("status"))) {
			eachBaylogger.debug("DevSysEnergyMeter : sendReadPhaseCurrentCommandV2 : Send : Failed");
			bayResponse.setErrorCode("Test-DevSys-ErrorCode-1010");
			return bayResponse;
		}
		String receivedData = (String) responseMap.get("responseData");

		eachBaylogger.debug("DevSysEnergyMeter : sendReadPhaseCurrentCommandV2 : receivedData" + receivedData);

		// Validate input string
		if (receivedData == null || receivedData.length() < 10) {
			bayResponse.setErrorCode("Test-DevSys-ErrorCode-1011");
			return bayResponse; // Minimum valid response size is 10 hex characters (5 bytes)
		}

		// Check start byte (0x02) and terminators (0x03, 0x0D, 0x0A)
		if (!receivedData.startsWith("02") || !receivedData.endsWith("030D0A")) {
			bayResponse.setErrorCode("Test-DevSys-ErrorCode-1012");
			return bayResponse; // Invalid response format
		}

		String phaseCurrent_hexStr = receivedData.substring(42, receivedData.length() - 24);
		eachBaylogger.debug(
				"DevSysEnergyMeter : sendReadPhaseCurrentCommandV2 : phaseCurrent_hexStr : " + phaseCurrent_hexStr);

		// converting hex to decimal
		int phaseCurrent_dec = 0;
		try {
			phaseCurrent_dec = Integer.parseInt(phaseCurrent_hexStr, 16);
			float phaseCurrent = (float) phaseCurrent_dec / 100;
			eachBaylogger.debug("DevSysEnergyMeter : sendReadPhaseCurrentCommandV2 : phaseCurrent : " + phaseCurrent);
			bayResponse.setStatus(true);
			bayResponse.setCurrentValue(phaseCurrent);
		} catch (NumberFormatException e) {
			eachBaylogger.debug("DevSysEnergyMeter : sendReadPhaseCurrentCommandV2 : Excception : " + e.getMessage());
		}

		// currentValue = extractAsciiData_DevSys(receivedData);
		/*
		 * if (currentValue.equals(null)) {
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : calibrationProcess : sendReadPhaseCurrentCommand : Send : Failed"
		 * );
		 * status = false;
		 * } else {
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : calibrationProcess : sendReadPhaseCurrentCommand : Send : Success"
		 * );
		 * status = true;
		 * }
		 */

		/*
		 * if (phaseCurrent>0.0f) {
		 * 
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : sendReadPhaseCurrentCommand : sendReadPhaseCurrentCommand : Send : Success"
		 * );
		 * status = true;
		 * } else {
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : sendReadPhaseCurrentCommand : sendReadPhaseCurrentCommand : Send : Failed"
		 * );
		 * status = false;
		 * }
		 */

		eachBaylogger.debug("DevSysEnergyMeter :  sendReadPhaseCurrentCommandV2 : Exit");
		return bayResponse;// status;
	}

	public boolean sendReadPhaseCurrentCommand(SpmDut spManager) {
		eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendReadPhaseCurrentCommand : Entry");
		boolean status = false;

		// String currentValue = "" ;

		/*
		 * String receivedData = "";//sendCommand(DevSysEnergyMeterCommands.DEVGINST)
		 * // receivedData =
		 * "02004B0000FFFFFFFF0000300000000000000200000002004B5F2F0031010101030D0A" ;
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : calibrationProcess : sendReadPhaseCurrentCommand : receivedData : "
		 * + receivedData);
		 */
		DutDirector pwrSrcDirector = new DutDirector(spManager);
		Map<String, Object> responseMap = pwrSrcDirector.sendCommandToDut(DevSysEnergyMeter.DEVGINST,
				DevSysEnergyMeter.ER_DATA_IN_HEX);
		if (!((boolean) responseMap.get("status"))) {
			eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendRelayOffCommand : Send : Failed");
			return status;
		}
		String receivedData = (String) responseMap.get("responseData");

		eachBaylogger
				.debug("DevSysEnergyMeter : sendReadPhaseCurrentCommand : sendReadNeutralCurrentCommand : receivedData"
						+ receivedData);

		// Validate input string
		if (receivedData == null || receivedData.length() < 10) {
			return false; // Minimum valid response size is 10 hex characters (5 bytes)
		}

		// Check start byte (0x02) and terminators (0x03, 0x0D, 0x0A)
		if (!receivedData.startsWith("02") || !receivedData.endsWith("030D0A")) {
			return false; // Invalid response format
		}

		String phaseCurrent_hexStr = receivedData.substring(42, receivedData.length() - 24);
		eachBaylogger.debug(
				"DevSysEnergyMeter : sendReadPhaseCurrentCommand : sendReadCurrentCommand : phaseCurrent_hexStr : "
						+ phaseCurrent_hexStr);

		// converting hex to decimal
		int phaseCurrent_dec = 0;
		try {
			phaseCurrent_dec = Integer.parseInt(phaseCurrent_hexStr, 16);
		} catch (NumberFormatException e) {
			eachBaylogger.debug(
					"DevSysEnergyMeter : sendReadPhaseCurrentCommand : sendReadPhaseCurrentCommand : Excception : "
							+ e.getMessage());
		}

		float phaseCurrent = (float) phaseCurrent_dec / 100;
		eachBaylogger
				.debug("DevSysEnergyMeter : sendReadPhaseCurrentCommand : sendReadPhaseCurrentCommand : phaseCurrent : "
						+ phaseCurrent);

		// currentValue = extractAsciiData_DevSys(receivedData);
		/*
		 * if (currentValue.equals(null)) {
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : calibrationProcess : sendReadPhaseCurrentCommand : Send : Failed"
		 * );
		 * status = false;
		 * } else {
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : calibrationProcess : sendReadPhaseCurrentCommand : Send : Success"
		 * );
		 * status = true;
		 * }
		 */

		/*
		 * if (phaseCurrent>0.0f) {
		 * 
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : sendReadPhaseCurrentCommand : sendReadPhaseCurrentCommand : Send : Success"
		 * );
		 * status = true;
		 * } else {
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : sendReadPhaseCurrentCommand : sendReadPhaseCurrentCommand : Send : Failed"
		 * );
		 * status = false;
		 * }
		 */

		eachBaylogger.debug("DevSysEnergyMeter : sendReadPhaseCurrentCommand : sendReadPhaseCurrentCommand : Exit");
		return status;
	}
	// ============================================================================================================================================

	public boolean sendReadNeutralCurrentCommand(SpmDut spManager) {
		eachBaylogger
				.debug("DevSysEnergyMeter : sendReadNeutralCurrentCommand : sendReadNeutralCurrentCommand : Entry");
		boolean status = false;

		String currentValue = "";
		/*
		 * String currentValue = "" ;
		 * 
		 * String receivedData = "";//sendCommand(DevSysEnergyMeterCommands.DEVGINST)
		 * // receivedData =
		 * "02004B0000FFFFFFFF0000300000000000000200000002004B5F2F0031010101030D0A" ;
		 */

		DutDirector pwrSrcDirector = new DutDirector(spManager);
		Map<String, Object> responseMap = pwrSrcDirector.sendCommandToDut(DevSysEnergyMeter.DEVGINST,
				DevSysEnergyMeter.ER_DATA_IN_HEX);
		if (!((boolean) responseMap.get("status"))) {
			eachBaylogger
					.debug("DevSysEnergyMeter : sendReadNeutralCurrentCommand : sendRelayOffCommand : Send : Failed");
			return status;
		}
		String receivedData = (String) responseMap.get("responseData");

		eachBaylogger.debug(
				"DevSysEnergyMeter : sendReadNeutralCurrentCommand : sendReadNeutralCurrentCommand : receivedData : "
						+ receivedData);

		// Validate input string
		if (receivedData == null || receivedData.length() < 10) {
			return false; // Minimum valid response size is 10 hex characters (5 bytes)
		}

		// Check start byte (0x02) and terminators (0x03, 0x0D, 0x0A)
		if (!receivedData.startsWith("02") || !receivedData.endsWith("030D0A")) {
			return false; // Invalid response format
		}

		String neutralCurrent_hexStr = receivedData.substring(46, receivedData.length() - 20);
		eachBaylogger.debug(
				"DevSysEnergyMeter : sendReadNeutralCurrentCommand : sendReadNeutralCurrentCommand : neutralCurrent_hexStr : "
						+ neutralCurrent_hexStr);

		// converting hex to decimal
		int neutralCurrent_dec = 0;
		try {
			neutralCurrent_dec = Integer.parseInt(neutralCurrent_hexStr, 16);
		} catch (NumberFormatException e) {
			eachBaylogger.debug(
					"DevSysEnergyMeter : sendReadNeutralCurrentCommand : sendReadNeutralCurrentCommand : Excception : "
							+ e.getMessage());
		}

		float neutralCurrent = (float) neutralCurrent_dec / 100;
		eachBaylogger.debug(
				"DevSysEnergyMeter : sendReadNeutralCurrentCommand : sendReadPhaseCurrentCommand : neutralCurrent : "
						+ neutralCurrent);

		/*
		 * currentValue = extractAsciiData_DevSys(receivedData);
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : sendReadNeutralCurrentCommand : sendReadNeutralCurrentCommand : extractedData : "
		 * + currentValue);
		 * 
		 * if (currentValue.equals(null)) {
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : sendReadNeutralCurrentCommand : sendReadNeutralCurrentCommand : Send : Failed"
		 * );
		 * status = false;
		 * } else {
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : sendReadNeutralCurrentCommand : sendReadNeutralCurrentCommand : Send : Success"
		 * );
		 * status = true;
		 * }
		 */

		eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendReadNeutralCurrentCommand : Exit");
		return status;
	}

	public BayResponse sendReadNeutralCurrentCommandV2(SpmDut spManager) {
		eachBaylogger.debug("DevSysEnergyMeter : sendReadNeutralCurrentCommandV2 : Entry");
		boolean status = false;
		BayResponse bayResponse = new BayResponse();
		// String currentValue = "" ;
		/*
		 * String currentValue = "" ;
		 * 
		 * String receivedData = "";//sendCommand(DevSysEnergyMeterCommands.DEVGINST)
		 * // receivedData =
		 * "02004B0000FFFFFFFF0000300000000000000200000002004B5F2F0031010101030D0A" ;
		 */

		DutDirector pwrSrcDirector = new DutDirector(spManager);
		Map<String, Object> responseMap = pwrSrcDirector.sendCommandToDut(DevSysEnergyMeter.DEVGINST,
				DevSysEnergyMeter.ER_DATA_IN_HEX);
		if (!((boolean) responseMap.get("status"))) {
			eachBaylogger.debug("DevSysEnergyMeter : sendReadNeutralCurrentCommandV2 : Send : Failed");
			// return status;
			bayResponse.setErrorCode("Test-DevSys-ErrorCode-2010");
			return bayResponse;
		}
		String receivedData = (String) responseMap.get("responseData");

		eachBaylogger.debug("DevSysEnergyMeter : sendReadNeutralCurrentCommandV2 : receivedData : " + receivedData);

		// Validate input string
		if (receivedData == null || receivedData.length() < 10) {
			// return false; // Minimum valid response size is 10 hex characters (5 bytes)
			bayResponse.setErrorCode("Test-DevSys-ErrorCode-2011");
			return bayResponse;
		}

		// Check start byte (0x02) and terminators (0x03, 0x0D, 0x0A)
		if (!receivedData.startsWith("02") || !receivedData.endsWith("030D0A")) {
			// return false; // Invalid response format
			bayResponse.setErrorCode("Test-DevSys-ErrorCode-2012");
			return bayResponse;
		}

		String neutralCurrent_hexStr = receivedData.substring(46, receivedData.length() - 20);
		eachBaylogger.debug("DevSysEnergyMeter : sendReadNeutralCurrentCommandV2 : neutralCurrent_hexStr : "
				+ neutralCurrent_hexStr);

		// converting hex to decimal
		int neutralCurrent_dec = 0;
		try {
			neutralCurrent_dec = Integer.parseInt(neutralCurrent_hexStr, 16);
			float neutralCurrent = (float) neutralCurrent_dec / 100;
			eachBaylogger
					.debug("DevSysEnergyMeter : sendReadNeutralCurrentCommandV2 : neutralCurrent : " + neutralCurrent);
			bayResponse.setStatus(true);
			bayResponse.setCurrentValue(neutralCurrent);
		} catch (NumberFormatException e) {
			eachBaylogger.debug("DevSysEnergyMeter : sendReadNeutralCurrentCommandV2 : Excception : " + e.getMessage());
		}

		/*
		 * currentValue = extractAsciiData_DevSys(receivedData);
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : sendReadNeutralCurrentCommand : sendReadNeutralCurrentCommand : extractedData : "
		 * + currentValue);
		 * 
		 * if (currentValue.equals(null)) {
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : sendReadNeutralCurrentCommand : sendReadNeutralCurrentCommand : Send : Failed"
		 * );
		 * status = false;
		 * } else {
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : sendReadNeutralCurrentCommand : sendReadNeutralCurrentCommand : Send : Success"
		 * );
		 * status = true;
		 * }
		 */

		eachBaylogger.debug("DevSysEnergyMeter : sendReadNeutralCurrentCommandV2 : Exit");
		return bayResponse;// status;
	}

	// ============================================================================================================================================

	public Map<String, Object> readSerialNumOfMeter(int meterNum, SpmDut spManager) {
		eachBaylogger.debug("readSerialNumOfMeter : Entry" + " : Position: " + meterNum);
		DutDirector pwrSrcDirector = new DutDirector(spManager);

		String meterSerialNumber = "";
		boolean status = false;
		int numOfAttempts = 3;// #Version0.5.1.4//3;

		for (int i = 0; i < numOfAttempts; i++) {
			Map<String, Object> responseMap = pwrSrcDirector.sendCommandToDut(DevSysEnergyMeter.DEVGSR,
					DevSysEnergyMeter.ER_DATA_IN_HEX);

			if (!(boolean) responseMap.get("status")) {
				eachBaylogger.debug("readSerialNumOfMeter : Send : Failed : Position: " + meterNum);
				continue;
			}

			String receivedData = (String) responseMap.get("responseData");
			eachBaylogger.debug("readSerialNumOfMeter : receivedData: " + receivedData + " : Position: " + meterNum);

			meterSerialNumber = extractAsciiData_DevSys(receivedData);
			eachBaylogger
					.debug("readSerialNumOfMeter : extractedData: " + meterSerialNumber + " :Position: " + meterNum);

			if (meterSerialNumber != null && !meterSerialNumber.isEmpty()) {
				status = true;
				eachBaylogger.debug("readSerialNumOfMeter : Send : Success :Position: " + meterNum);
				break;
			} else {
				eachBaylogger.debug("readSerialNumOfMeter : Send : Failed :Position: " + meterNum);
			}

			BayUtils.delay(100);// 200);
		}

		// eachBaylogger.debug("readSerialNumOfMeter : Exit");

		// Create a HashMap to return the results
		Map<String, Object> result = new HashMap<>();
		result.put("status", status);
		result.put("meterSerialNumber", meterSerialNumber);
		eachBaylogger.debug("readSerialNumOfMeter : Exit  :Position: " + meterNum);
		return result;
	}

	public Map<String, Object> writeSerialNumOfMeter(int meterNum, String dutSerialNo, SpmDut spManager) {
		eachBaylogger.debug("writeSerialNumOfMeter : Entry" + " : Position: " + meterNum);
		DutDirector pwrSrcDirector = new DutDirector(spManager);

		String meterSerialNumber = "";
		boolean status = false;
		int numOfAttempts = 1;// #Version0.5.1.4//3;
		String targetCommand = "";
		for (int i = 0; i < numOfAttempts; i++) {
			targetCommand = DevSysEnergyMeter.DEVSSR +
					GuiUtils.asciiToHex(
							DeviceDataManagerController.getConveyorConfigParsedKey().getDutWriteSerialNoPassword())
					+
					GuiUtils.asciiToHex(dutSerialNo) +
					DevSysEnergyMeter.CMD_SERIAL_NO_WRITE_TERMINATOR_IN_HEX;
			eachBaylogger.debug("writeSerialNumOfMeter : targetCommand-Hex   : Position: " + targetCommand);
			eachBaylogger.debug(
					"writeSerialNumOfMeter : targetCommand-Ascii : Position: " + GuiUtils.hexToAsciiV2(targetCommand));
			Map<String, Object> responseMap = pwrSrcDirector.sendCommandToDut(targetCommand,
					DevSysEnergyMeter.ER_SERIAL_NO_WRITE_DATA_IN_HEX);

			if (!(boolean) responseMap.get("status")) {
				eachBaylogger.debug("writeSerialNumOfMeter : Send : Failed : Position: " + meterNum);
				continue;
			}

			String receivedData = (String) responseMap.get("responseData");
			eachBaylogger.debug("writeSerialNumOfMeter : receivedData: " + receivedData + " : Position: " + meterNum);

			meterSerialNumber = extractAsciiData_DevSys(receivedData);
			eachBaylogger
					.debug("writeSerialNumOfMeter : extractedData: " + meterSerialNumber + " :Position: " + meterNum);

			if (meterSerialNumber != null && !meterSerialNumber.isEmpty()) {
				status = true;
				eachBaylogger.debug("writeSerialNumOfMeter : Send : Success :Position: " + meterNum);
				break;
			} else {
				eachBaylogger.debug("writeSerialNumOfMeter : Send : Failed :Position: " + meterNum);
			}

			BayUtils.delay(100);// 200);
		}

		// eachBaylogger.debug("readSerialNumOfMeter : Exit");

		// Create a HashMap to return the results
		Map<String, Object> result = new HashMap<>();
		result.put("status", status);
		// result.put("meterSerialNumber", meterSerialNumber);
		eachBaylogger.debug("writeSerialNumOfMeter : Exit  :Position: " + meterNum);
		return result;
	}

	// ============================================================================================================================================

	/*
	 * private boolean startFtSource() {
	 * 
	 * return false;
	 * }
	 */
	public boolean sendRelayOffCommand(SpmDut spManager) {
		eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendRelayOffCommand : Entry");
		boolean status = false;

		DutDirector pwrSrcDirector = new DutDirector(spManager);
		Map<String, Object> responseMap = pwrSrcDirector.sendCommandToDut(DevSysEnergyMeter.DEVRELAYOFF,
				DevSysEnergyMeter.ER_DATA_IN_HEX);
		if (!((boolean) responseMap.get("status"))) {
			eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendRelayOffCommand : Send : Failed");
			return status;
		}
		String receivedData = (String) responseMap.get("responseData");
		eachBaylogger
				.debug("DevSysEnergyMeter : calibrationProcess : sendRelayOffCommand : receivedData: " + receivedData);

		// String extractedData = extractAsciiData_DevSys(receivedData);
		// eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess :
		// sendRelayOffCommand : extractedData: " + extractedData);

		if (receivedData.equals(DevSysEnergyMeter.ER_DATA_IN_HEX + DevSysEnergyMeter.TERMINATOR)) {
			status = true;
			eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendRelayOffCommand : Send : Success");
		} else {
			status = false;
			eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendRelayOffCommand : Send : Failed");
		}

		eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendRelayOffCommand : Exit");
		return status;
	}
	// ============================================================================================

	public boolean sendRelayOnCommand(SpmDut spManager) {
		eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendRelayOnCommand : Entry");
		boolean status = false;

		DutDirector pwrSrcDirector = new DutDirector(spManager);
		Map<String, Object> responseMap = pwrSrcDirector.sendCommandToDut(DevSysEnergyMeter.DEVRELAYON,
				DevSysEnergyMeter.ER_DATA_IN_HEX);
		if (!((boolean) responseMap.get("status"))) {
			eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendRelayOnCommand : Send : Failed");
			return status;
		}
		String receivedData = (String) responseMap.get("responseData");
		eachBaylogger
				.debug("DevSysEnergyMeter : calibrationProcess : sendRelayOnCommand : receivedData" + receivedData);

		// String extractedData = extractAsciiData_DevSys(receivedData);
		// eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess :
		// sendRelayOnCommand : extractedData" + extractedData);

		if (receivedData.equals(DevSysEnergyMeter.ER_DATA_IN_HEX + DevSysEnergyMeter.TERMINATOR)) {
			status = true;
			eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendRelayOnCommand : Send : Success");
		} else {
			status = false;
			eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendRelayOnCommand : Send : Failed");
		}

		eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendRelayOnCommand : Exit");
		return status;
	}

	// ============================================================================================

	public String extractAsciiData_DevSys(String responseHex) {
		// Validate input string
		if (responseHex == null || responseHex.length() < 10) {
			return null; // Minimum valid response size is 10 hex characters (5 bytes)
		}

		// Check start byte (0x02) and terminators (0x03, 0x0D, 0x0A)
		if (!responseHex.startsWith("02") || !responseHex.endsWith("030D0A")) {
			return null; // Invalid response format
		}

		// Extract the data portion (hex string between start byte and terminators)
		String dataHex = responseHex.substring(2, responseHex.length() - 6);

		// Convert hex string to ASCII string
		StringBuilder asciiBuilder = new StringBuilder();
		for (int i = 0; i < dataHex.length(); i += 2) {
			// Convert each pair of hex characters to a byte
			int charCode = Integer.parseInt(dataHex.substring(i, i + 2), 16);
			asciiBuilder.append((char) charCode);
		}

		return asciiBuilder.toString();
	}

	// ============================================================================================================================================

	public boolean sendMemoryClearCommand(SpmDut spManager) {
		eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendMemoryClearCommand : Entry");
		boolean status = false;

		DutDirector pwrSrcDirector = new DutDirector(spManager);
		Map<String, Object> responseMap = pwrSrcDirector.sendCommandToDut(DevSysEnergyMeter.DEVMER,
				DevSysEnergyMeter.ER_DATA_IN_HEX);

		/*
		 * String receivedData = (String)responseMap.get("responseData");
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : calibrationProcess : sendMemoryClearCommand : receivedData"
		 * + receivedData);
		 * 
		 * String extractedData = extractAsciiData(receivedData);
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : calibrationProcess : sendMemoryClearCommand : extractedData"
		 * + extractedData);
		 * 
		 * if (extractedData.equals(START)) {
		 * status = true;
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : calibrationProcess : sendMemoryClearCommand : Send : Success"
		 * );
		 * } else {
		 * status = false;
		 * eachBaylogger.
		 * debug("DevSysEnergyMeter : calibrationProcess : sendMemoryClearCommand : Send : Failed"
		 * );
		 * }
		 */

		if ((boolean) responseMap.get("status")) {
			status = true;
			eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendMemoryClearCommand : Send : Success");
		}

		eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendMemoryClearCommand : Exit");
		return status;
	}
	// ============================================================================================================================================

	public boolean sendCalibrationLockCommand(int meterNum, SpmDut spManager) {
		eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendCalibrationLockCommand : Entry"
				+ " : Position: " + meterNum);
		boolean status = false;

		DutDirector pwrSrcDirector = new DutDirector(spManager);
		Map<String, Object> responseMap = pwrSrcDirector.sendCommandToDut(DevSysEnergyMeter.DEVSCALL,
				DevSysEnergyMeter.ER_DATA_IN_HEX);
		if (!((boolean) responseMap.get("status"))) {
			eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendCalibrationLockCommand : Send : Failed"
					+ " : Position: " + meterNum);
			return status;
		}
		String receivedData = (String) responseMap.get("responseData");
		eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendCalibrationLockCommand : receivedData: "
				+ receivedData + " : Position: " + meterNum);

		String extractedData = extractAsciiData(receivedData);
		eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendCalibrationLockCommand : extractedData: "
				+ extractedData + " : Position: " + meterNum);

		if (extractedData.equals("\u0000") || extractedData.isEmpty()) {
			status = true;
			eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendCalibrationLockCommand : Send : Success"
					+ " : Position: " + meterNum);
		} else {
			status = false;
			eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendCalibrationLockCommand : Send : Failed"
					+ " : Position: " + meterNum);
		}

		eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendCalibrationLockCommand : Exit"
				+ " : Position: " + meterNum);
		return status;
	}
	// ============================================================================================================================================

	public boolean sendDeviceCalibInNeutralCktCommand(int meterNum, SpmDut spManager) {
		eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendDeviceCalibInNeutralCktCommand : Entry"
				+ " : Position: " + meterNum);
		boolean status = false;

		DutDirector pwrSrcDirector = new DutDirector(spManager);
		Map<String, Object> responseMap = pwrSrcDirector.sendCommandToDut(DevSysEnergyMeter.DEVCALN,
				DevSysEnergyMeter.ER_DATA_IN_HEX);
		if (!((boolean) responseMap.get("status"))) {
			eachBaylogger
					.debug("DevSysEnergyMeter : calibrationProcess : sendDeviceCalibInNeutralCktCommand : Send : Failed"
							+ " : Position: " + meterNum);
			return status;
		}
		String receivedData = (String) responseMap.get("responseData");
		eachBaylogger
				.debug("DevSysEnergyMeter : calibrationProcess : sendDeviceCalibInNeutralCktCommand : receivedData: "
						+ receivedData + " : Position: " + meterNum);

		String extractedData = extractAsciiData(receivedData);
		eachBaylogger
				.debug("DevSysEnergyMeter : calibrationProcess : sendDeviceCalibInNeutralCktCommand : extractedData: "
						+ extractedData + " : Position: " + meterNum);

		if (extractedData.equals(START)) {
			status = true;
			eachBaylogger.debug(
					"DevSysEnergyMeter : calibrationProcess : sendDeviceCalibInNeutralCktCommand : Send : Success"
							+ " : Position: " + meterNum);
		} else {
			status = false;
			eachBaylogger
					.debug("DevSysEnergyMeter : calibrationProcess : sendDeviceCalibInNeutralCktCommand : Send : Failed"
							+ " : Position: " + meterNum);
		}

		eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendDeviceCalibInNeutralCktCommand : Exit"
				+ " : Position: " + meterNum);
		return status;
	}
	// ============================================================================================================================================

	public boolean sendDeviceCalibInPhaseCktCommand(int meterNum, SpmDut spManager) {
		eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendDeviceCalibInPhaseCktCommand : Entry"
				+ " : Position: " + meterNum);
		boolean status = false;

		DutDirector pwrSrcDirector = new DutDirector(spManager);
		Map<String, Object> responseMap = pwrSrcDirector.sendCommandToDut(DevSysEnergyMeter.DEVCALP,
				DevSysEnergyMeter.ER_DATA_IN_HEX);
		if (!((boolean) responseMap.get("status"))) {
			eachBaylogger
					.debug("DevSysEnergyMeter : calibrationProcess : sendDeviceCalibInPhaseCktCommand : Send : Failed"
							+ " : Position: " + meterNum);
			return status;
		}
		String receivedData = (String) responseMap.get("responseData");
		eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendDeviceCalibInPhaseCktCommand : receivedData: "
				+ receivedData + " : Position: " + meterNum);

		String extractedData = extractAsciiData(receivedData);
		eachBaylogger
				.debug("DevSysEnergyMeter : calibrationProcess : sendDeviceCalibInPhaseCktCommand : extractedData: "
						+ extractedData + " : Position: " + meterNum);

		if (extractedData.equals(START)) {
			status = true;
			eachBaylogger
					.debug("DevSysEnergyMeter : calibrationProcess : sendDeviceCalibInPhaseCktCommand : Send : Success"
							+ " : Position: " + meterNum);
		} else {
			status = false;
			eachBaylogger
					.debug("DevSysEnergyMeter : calibrationProcess : sendDeviceCalibInPhaseCktCommand : Send : Failed"
							+ " : Position: " + meterNum);
		}

		eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendDeviceCalibInPhaseCktCommand : Exit"
				+ " : Position: " + meterNum);
		return status;
	}

	// ============================================================================================================================================

	public boolean sendDeviceUnlockCommand(int meterNum, SpmDut spManager) {
		eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendDeviceUnlockCommand : Entry" + " : Position: "
				+ meterNum);
		boolean status = false;

		DutDirector pwrSrcDirector = new DutDirector(spManager);
		Map<String, Object> responseMap = pwrSrcDirector.sendCommandToDut(DevSysEnergyMeter.DEVUNLOCK,
				DevSysEnergyMeter.ER_DATA_IN_HEX);
		if (!((boolean) responseMap.get("status"))) {
			eachBaylogger
					.debug("DevSysEnergyMeter : calibrationProcess : sendCalibrationLockCommand : responseMap Status : "
							+ (boolean) responseMap.get("status") + " : Position: " + meterNum);
			eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendCalibrationLockCommand : Send : Failed"
					+ " : Position: " + meterNum);
			return status;
		}
		String receivedData = (String) responseMap.get("responseData");// sendCommand(DevSysEnergyMeterCommands.DEVUNLOCK);
		eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendDeviceUnlockCommand : receivedData : "
				+ receivedData + " : Position: " + meterNum);

		String extractedData = extractAsciiData(receivedData);
		eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendDeviceUnlockCommand : extractedData : "
				+ extractedData + " : Position: " + meterNum);

		// if (extractedData.equals(UNLOCKED)) { // while executing in parallel one time
		// exception observed here
		if (UNLOCKED.equals(extractedData)) {
			status = true;
			eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendDeviceUnlockCommand : Send : Success"
					+ " : Position: " + meterNum);
		} else {
			status = false;
			eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendDeviceUnlockCommand : Send : Failed"
					+ " : Position: " + meterNum);
		}

		eachBaylogger.debug(
				"DevSysEnergyMeter : calibrationProcess : sendDeviceUnlockCommand : Exit" + " : Position: " + meterNum);
		return status;
	}

	public boolean sendDefaultCalibrationCommand(SpmDut spManager) {
		eachBaylogger.debug("DevSysEnergyMeter : sendDefaultCalibrationCommand : sendRelayOffCommand : Entry");
		boolean status = false;

		DutDirector pwrSrcDirector = new DutDirector(spManager);
		Map<String, Object> responseMap = pwrSrcDirector.sendCommandToDut(DevSysEnergyMeter.DEVDCAL,
				DevSysEnergyMeter.ER_DATA_IN_HEX);
		if (!((boolean) responseMap.get("status"))) {
			eachBaylogger
					.debug("DevSysEnergyMeter : sendDefaultCalibrationCommand : sendRelayOffCommand : Send : Failed");
			return status;
		}
		String receivedData = (String) responseMap.get("responseData");
		eachBaylogger.debug("DevSysEnergyMeter : sendDefaultCalibrationCommand : sendRelayOffCommand : receivedData: "
				+ receivedData);

		// String extractedData = extractAsciiData_DevSys(receivedData);
		// eachBaylogger.debug("DevSysEnergyMeter : sendDefaultCalibrationCommand :
		// sendRelayOffCommand : extractedData: " + extractedData);

		if (receivedData.equals(DevSysEnergyMeter.ER_DATA_IN_HEX + DevSysEnergyMeter.TERMINATOR)) {
			status = true;
			eachBaylogger
					.debug("DevSysEnergyMeter : sendDefaultCalibrationCommand : sendRelayOffCommand : Send : Success");
		} else {
			status = false;
			eachBaylogger
					.debug("DevSysEnergyMeter : sendDefaultCalibrationCommand : sendRelayOffCommand : Send : Failed");
		}

		eachBaylogger.debug("DevSysEnergyMeter : sendDefaultCalibrationCommand : sendRelayOffCommand : Exit");
		return status;
	}

	// ============================================================================================================================================

	public boolean sendGsmCheckWithoutSimCommand(int meterNum, SpmDut spManager) {
		eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendGsmCheckWithoutSimCommand : Entry"
				+ " : Position: " + meterNum);
		boolean status = false;

		DutDirector pwrSrcDirector = new DutDirector(spManager);
		Map<String, Object> responseMap = pwrSrcDirector.sendCommandToDut(DevSysEnergyMeter.DEVGPRSSTATE,
				DevSysEnergyMeter.ER_DATA_IN_HEX);
		if (!((boolean) responseMap.get("status"))) {
			eachBaylogger.debug(
					"DevSysEnergyMeter : calibrationProcess : sendGsmCheckWithoutSimCommand : responseMap Status : "
							+ (boolean) responseMap.get("status") + " : Position: " + meterNum);
			eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendGsmCheckWithoutSimCommand : Send : Failed"
					+ " : Position: " + meterNum);
			return status;
		}
		String receivedData = (String) responseMap.get("responseData");// sendCommand(DevSysEnergyMeterCommands.DEVUNLOCK);
		eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendGsmCheckWithoutSimCommand : receivedData : "
				+ receivedData + " : Position: " + meterNum);

		String extractedData = extractAsciiData(receivedData);
		eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendGsmCheckWithoutSimCommand : extractedData : "
				+ extractedData + " : Position: " + meterNum);

		if (extractedData.startsWith(PASS)) {
			status = true;
			eachBaylogger
					.debug("DevSysEnergyMeter : calibrationProcess : sendGsmCheckWithoutSimCommand : Send : Success"
							+ " : Position: " + meterNum);
		} else if (extractedData.startsWith(FAIL)) {
			status = false;
			eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendGsmCheckWithoutSimCommand : Send : Failed"
					+ " : Position: " + meterNum);
		} else {
			status = false;
			eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendGsmCheckWithoutSimCommand : Send : Failed"
					+ " : Position: " + meterNum);
		}

		eachBaylogger.debug("DevSysEnergyMeter : calibrationProcess : sendGsmCheckWithoutSimCommand : Exit"
				+ " : Position: " + meterNum);
		return status;
	}

	// ============================================================================================================================================

	public String extractAsciiData(String responseHex) {
		// Validate input string
		if (responseHex == null || responseHex.length() < 10) {
			return null; // Minimum valid response size is 10 hex characters (5 bytes)
		}

		// Check start byte (0x02) and terminators (0x03, 0x0D, 0x0A)
		if (!responseHex.startsWith("02") || !responseHex.endsWith("030D0A")) {
			return null; // Invalid response format
		}

		// Extract the data portion (hex string between start byte and terminators)
		String dataHex = responseHex.substring(2, responseHex.length() - 6);

		// Convert hex string to ASCII string
		StringBuilder asciiBuilder = new StringBuilder();
		for (int i = 0; i < dataHex.length(); i += 2) {
			// Convert each pair of hex characters to a byte
			int charCode = Integer.parseInt(dataHex.substring(i, i + 2), 16);
			asciiBuilder.append((char) charCode);
		}

		return asciiBuilder.toString();
	}
	// ============================================================================================================================================

	public SpmDut serialPortInit(String portCname) {

		boolean status = false;
		String response = "";
		String commPortID = "";
		String commBaudRate = "";
		SpmDut spManager = null;
		eachBaylogger.info("serialPortInit: portCname: " + portCname);
		// ScanForSerialPorts();
		JSONObject qrPortSettingData = MySQL_Controller.sp_get_cname_port_setting(portCname);

		try {
			if (qrPortSettingData.has("port_name")) {
				commPortID = qrPortSettingData.getString("port_name");

				// cmbBxLDU_PortSelection2.setValue(saved_ldu2_setting.getString("port_name"));
			} else {
				// cmbBxLDU_PortSelection2.setValue("");
				eachBaylogger.info("serialPortInit: port_name-1: Data not retrieved from DB");

			}

		} catch (JSONException e) {

			e.printStackTrace();
			eachBaylogger.error("serialPortInit: JSONException5-2:" + e.getMessage());
			// cmbBxLDU_PortSelection2.setValue("");
			eachBaylogger.info("serialPortInit: port_name-2: Data not retrieved from database");

		}

		try {
			if (qrPortSettingData.has("baud_rate")) {
				commBaudRate = qrPortSettingData.getString("baud_rate");

				// cmbBxLDU_PortSelection2.setValue(saved_ldu2_setting.getString("port_name"));
			} else {
				// cmbBxLDU_PortSelection2.setValue("");
				eachBaylogger.info("serialPortInit: baudrate-1: Data not retrieved from DB");

			}

		} catch (JSONException e) {

			e.printStackTrace();
			eachBaylogger.error("serialPortInit: JSONException5-2:" + e.getMessage());
			// cmbBxLDU_PortSelection2.setValue("");
			eachBaylogger.info("serialPortInit: baudrate-2: Data not retrieved from database");

		}

		try {
			if ((!commBaudRate.isEmpty()) && (!commPortID.isEmpty())) {
				// SerialPortManagerPwrSrc_V2 serialPortManagerQrScanner = new
				// SerialPortManagerPwrSrc_V2("qrScn-1");
				// status =
				// serialPortManagerQrScanner.powerSourceComInitV2(commPortID,commBaudRate);
				boolean terminatorMandatory = true;
				spManager = new SpmDut(portCname, terminatorMandatory);
				eachBaylogger.debug("serialPortInit: commPortID: " + commPortID);
				eachBaylogger.debug("serialPortInit: commBaudRate: " + commBaudRate);
				status = spManager.powerSourceComInitV2(commPortID, commBaudRate);
				if (!status) {
					// status = false;
					spManager = null;
					eachBaylogger.debug("serialPortInit : Optical serial  Failed");
				} else {
					// setPortValidationTurnedON(true);
					// status = serialDM_Obj.lscsLDU1_CheckCom();
					// DisplayDataObj.pwrSrcEnableSerialMonitoring_V2();
					spManager.startSerialRxPhysical_Dut();
					spManager.enableSerialRxPhysical_DutMonitor();
					// PowerSourceDirector pwrSrcDirector = new PowerSourceDirector(spManager);
					// Map<String,Object> responseMap = pwrSrcDirector.scanQrCode();

					/*
					 * status = (boolean)responseMap.get("status");
					 * String qrData = "";
					 * try{
					 * qrData = (String)responseMap.get("responseData");
					 * eachBaylogger.debug("analogTrigger: qrData1: "+qrData);
					 * qrData = NewlandQRCodeScanner.extractScannedData((String)responseMap.get(
					 * "responseData"));
					 * eachBaylogger.debug("analogTrigger: qrData2: "+qrData);
					 * }catch(Exception e){
					 * e.printStackTrace();
					 * eachBaylogger.error("analogTrigger: Exception"+e.getMessage());
					 * }
					 */

					// DisplayDataObj.pwrSrcDisconnectPort_V2();
					// serialPortManagerQrScanner.disconnectPwrSrc();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			eachBaylogger.error("serialPortInit: Exception-X" + e.getMessage());
		}

		return spManager;
	}

	public SpmDut serialPortInitV2(DeviceSetting deviceSetting) {

		boolean status = false;
		String response = "";
		String commPortID = deviceSetting.getPortName();
		String commBaudRate = deviceSetting.getBaudRate();
		String portCname = deviceSetting.getCanName();
		SpmDut spManager = null;
		eachBaylogger.info("serialPortInitV2: commPortID: " + commPortID);
		// ScanForSerialPorts();
		// JSONObject qrPortSettingData =
		// MySQL_Controller.sp_get_cname_port_setting(portCname);

		/*
		 * try {
		 * if(qrPortSettingData.has("port_name")){
		 * commPortID = qrPortSettingData.getString("port_name");
		 * 
		 * //cmbBxLDU_PortSelection2.setValue(saved_ldu2_setting.getString("port_name"))
		 * ;
		 * } else {
		 * //cmbBxLDU_PortSelection2.setValue("");
		 * eachBaylogger.
		 * info("serialPortInitV2: port_name-1: Data not retrieved from DB");
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * eachBaylogger.error("serialPortInitV2: JSONException5-2:"+e.getMessage());
		 * //cmbBxLDU_PortSelection2.setValue("");
		 * eachBaylogger.
		 * info("serialPortInitV2: port_name-2: Data not retrieved from database");
		 * 
		 * }
		 */

		/*
		 * try {
		 * if(qrPortSettingData.has("baud_rate")){
		 * commBaudRate = qrPortSettingData.getString("baud_rate");
		 * 
		 * //cmbBxLDU_PortSelection2.setValue(saved_ldu2_setting.getString("port_name"))
		 * ;
		 * } else {
		 * //cmbBxLDU_PortSelection2.setValue("");
		 * eachBaylogger.info("serialPortInitV2: baudrate-1: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * eachBaylogger.error("serialPortInitV2: JSONException5-2:"+e.getMessage());
		 * //cmbBxLDU_PortSelection2.setValue("");
		 * eachBaylogger.
		 * info("serialPortInitV2: baudrate-2: Data not retrieved from database");
		 * 
		 * }
		 */

		try {
			if ((!commBaudRate.isEmpty()) && (!commPortID.isEmpty())) {
				// SerialPortManagerPwrSrc_V2 serialPortManagerQrScanner = new
				// SerialPortManagerPwrSrc_V2("qrScn-1");
				// status =
				// serialPortManagerQrScanner.powerSourceComInitV2(commPortID,commBaudRate);
				boolean terminatorMandatory = true;
				spManager = new SpmDut(portCname, terminatorMandatory);
				// eachBaylogger.debug("serialPortInitV2: devsys: commPortID: " + commPortID);
				eachBaylogger.debug("serialPortInitV2: devsys: commBaudRate: " + commBaudRate);
				status = spManager.powerSourceComInitV2(commPortID, commBaudRate);
				if (!status) {
					// status = false;
					spManager = null;
					eachBaylogger.debug("serialPortInitV2 :Optical serial Failed");
				} else {
					// setPortValidationTurnedON(true);
					// status = serialDM_Obj.lscsLDU1_CheckCom();
					// DisplayDataObj.pwrSrcEnableSerialMonitoring_V2();
					spManager.startSerialRxPhysical_Dut();
					spManager.enableSerialRxPhysical_DutMonitor();
					// PowerSourceDirector pwrSrcDirector = new PowerSourceDirector(spManager);
					// Map<String,Object> responseMap = pwrSrcDirector.scanQrCode();

					/*
					 * status = (boolean)responseMap.get("status");
					 * String qrData = "";
					 * try{
					 * qrData = (String)responseMap.get("responseData");
					 * eachBaylogger.debug("analogTrigger: qrData1: "+qrData);
					 * qrData = NewlandQRCodeScanner.extractScannedData((String)responseMap.get(
					 * "responseData"));
					 * eachBaylogger.debug("analogTrigger: qrData2: "+qrData);
					 * }catch(Exception e){
					 * e.printStackTrace();
					 * eachBaylogger.error("analogTrigger: Exception"+e.getMessage());
					 * }
					 */

					// DisplayDataObj.pwrSrcDisconnectPort_V2();
					// serialPortManagerQrScanner.disconnectPwrSrc();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			eachBaylogger.error("serialPortInitV2: Exception-X" + e.getMessage());
		}

		return spManager;
	}

	// ======================================================================================================================================================

}
