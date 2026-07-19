package com.tasnetwork.calibration.conveyor.bay;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.database.MySQL_Controller;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.serial.director.QrScannerDirector;
import com.tasnetwork.calibration.conveyor.serial.messenger.QrScannerMessenger;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmQrScanner;
import com.tasnetwork.calibration.conveyor.util.GUIUtils;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.DeleteMeConstant;
import com.tasnetwork.spring.orm.model.DeviceSetting;
import com.tasnetwork.spring.orm.model.TerminalProfileSetting;

public class NewlandQRCodeScanner {

	// public static SerialDataManager SerialDM_Obj = new SerialDataManager();
	// static DeviceDataManagerController DisplayDataObj = new
	// DeviceDataManagerController();
	static QrScannerMessenger pwrSrcBofaMessenger = new QrScannerMessenger();

	TerminalProfileSetting terminalBayProfile = new TerminalProfileSetting();

	public NewlandQRCodeScanner(TerminalProfileSetting myTerminalProfile) {
		terminalBayProfile = myTerminalProfile;
	}

	public NewlandQRCodeScanner() {

	}

	public static final String GOOD_READ_EXPECTED_BEGIN_DATA_IN_HEX = "51722D";// "Qr-"
	public static final String NOT_GOOD_READ_EXPECTED_DATA_IN_HEX = "4E470D0A";// "06";
	public static final String NOT_GOOD_READ_EXPECTED_DATA_IN_ASCII = "NG";
	public static final String QR_METER_SERIAL_NUMBER_SCAN_FAILED = "SN:N/A";

	public static final String ANALOG_TRIGGER_SETTING = "015404";// "1B31";

	public static final String TRIGGER_STOP_SETTINGS = "0130";

	public static final String AUTOMATIC_READING_SETTINGS = "1B32";
	public static final String CONTINUOUS_READING_SETTINGS = "1B33";

	public static final String ENABLE_COMMAND_PROGRAMMING = "6E6C7330303036303130";
	public static final String DISABLE_COMMAND_PROGRAMMING = "6E6C7330303036303030";

	public static final String ENABLE_ALL_BAR_CODES = "30303031303230";
	public static final String DISABLE_ALL_BAR_CODES = "30303031303130";
	public static final String ENABLE_2D_BAR_CODES = "30303031303630";
	public static final String DISABLE_2D_BAR_CODES = "30303031303530";
	public static final String ENABLE_1D_BAR_CODES = "30303031303430";

	public static final String DISABLE_1D_BAR_CODES = "30303031303330";
	public static final String SAVE_AS_USER_DEFAULT = "30303031313530";

	public static final String RS232 = "31313030303030";
	public static final String BAUD_RATE_9600_BPS = "30313030303330";
	public static final String BAUD_RATE_115200_BPS = "30313030303830";
	public static final String RS232_NO_VERIFY = "30313031303030";
	public static final String RS232_8_DIGITS = "30313033303330";
	public static final String RS232_ONE_STOP_BIT = "30313032303030";

	public static final String GOOD_READ_BEEP_ENABLE = "30323033303130";
	public static final String GOOD_READ_BEEP_DISABLE = "30323033303030";

	// =================================================================
	public String scan_QR_code(int positionNum) {
		ApplicationLauncher.logger.debug("scan_QR_code : Entry");
		String portCname = "";
		String receivedData = "";
		if (positionNum == 1) {
			receivedData = "TestQr1";
		} else if (positionNum == 2) {
			// portInfo =
			// BayUtils.getInputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_LDU_SCANNER2_IP
			// );
			receivedData = "TestQr2";
		} else if (positionNum == 3) {
			// portInfo =
			// BayUtils.getInputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_LDU_SCANNER3_IP
			// );
			receivedData = "TestQr3";
		} else if (positionNum == 4) {
			// portInfo =
			// BayUtils.getInputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_LDU_SCANNER4_IP
			// );
			receivedData = "TestQr4";
		} else if (positionNum == 5) {
			// portInfo =
			// BayUtils.getInputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_LDU_SCANNER5_IP
			// );
			receivedData = "TestQr5";
		} else if (positionNum == 6) {
			// portInfo =
			// BayUtils.getInputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_LDU_SCANNER6_IP
			// );
			receivedData = "TestQr6";
		}

		/*
		 * if (!receivedData.isEmpty()) {
		 * return receivedData ;
		 * }
		 */

		// DevSysEnergyMeter devSysEnergyMeter = new DevSysEnergyMeter();
		// TerminalProfileModel terminalProfile = new TerminalProfileModel();
		String deviceId = getTerminalBayProfile().getTerminalId() + getTerminalBayProfile().getClusterId() +
				getTerminalBayProfile().getBayId() +
				ConstantConveyor.DEVICE_TYPE_QR_SCANNER +
				String.format("%02d", positionNum);

		ApplicationLauncher.logger.debug("scan_QR_code : deviceId : " + deviceId);
		if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {

			ConveyorDataManager deviceDataManager = new ConveyorDataManager();
			DeviceSetting deviceSetting = deviceDataManager.getDeviceSettingByDeviceId(deviceId);
			if (deviceSetting != null) {
				receivedData = analogTriggerV2(deviceSetting);
			} else {
				ApplicationLauncher.logger.debug("scan_QR_code: " + positionNum + " Failed : Com port not configured");
				receivedData = "COM_PORT_NOT_CONFIGURED";
			}
		} else {
			try {
				JSONObject devicePortSetting = MySQL_Controller.sp_getdevice_setting_v2(deviceId);

				portCname = devicePortSetting.getString("c_name");
				ApplicationLauncher.logger.debug("scan_QR_code : portCname : " + portCname);
			} catch (JSONException e) {

				e.printStackTrace();
				ApplicationLauncher.logger.error("scan_QR_code : Exception : " + e.getMessage());
			}
			// String scannerID = "";
			ApplicationLauncher.logger.debug("scan_QR_code : positionNum : " + positionNum);
			ApplicationLauncher.logger.debug("scan_QR_code : portCname   : " + portCname);

			// boolean status = false;
			receivedData = analogTrigger(portCname); // ANALOG_TRIGGER_SETTING
		}
		ApplicationLauncher.logger.debug("scan_QR_code: scannedData: " + receivedData);

		// Convert received data to a readable string
		// String scannedData = extractScannedData(receivedData);
		// ApplicationLauncher.logger.debug("scan_QR_code: scannedData" + scannedData);

		if (receivedData == null) {
			ApplicationLauncher.logger.debug("scan_QR_code: " + positionNum + " Failed : No QR Code Available");
			receivedData = "SCNR_NW";
		} else if (receivedData.equals("06")) {
			// status = false;
			ApplicationLauncher.logger.debug("scan_QR_code: " + positionNum + " Failed : No QR Code Available");
			receivedData = "NO_QR_CODE_AVAILABLE";
		}

		ApplicationLauncher.logger.debug("scan_QR_code : " + portCname + " : Exit");
		return receivedData;
	}

	// ============================================================================================

	public String analogTrigger(String scannerID) {
		ApplicationLauncher.logger.debug("analogTrigger : Entry");
		boolean status = false;

		// String response = "";
		String qrData = "";
		String commPortID = "";
		String commBaudRate = "";
		// ScanForSerialPorts();
		JSONObject qrPortSettingData = MySQL_Controller.sp_get_cname_port_setting(scannerID);

		try {
			if (qrPortSettingData.has("port_name")) {
				commPortID = qrPortSettingData.getString("port_name");

				// cmbBxLDU_PortSelection2.setValue(saved_ldu2_setting.getString("port_name"));
			} else {
				// cmbBxLDU_PortSelection2.setValue("");
				ApplicationLauncher.logger.info("analogTrigger: port_name-1: Data not retrieved from DB");

			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("analogTrigger: JSONException5-2:" + e.getMessage());
			// cmbBxLDU_PortSelection2.setValue("");
			ApplicationLauncher.logger.info("analogTrigger: port_name-2: Data not retrieved from database");

		}

		try {
			if (qrPortSettingData.has("baud_rate")) {
				commBaudRate = qrPortSettingData.getString("baud_rate");

				// cmbBxLDU_PortSelection2.setValue(saved_ldu2_setting.getString("port_name"));
			} else {
				// cmbBxLDU_PortSelection2.setValue("");
				ApplicationLauncher.logger.info("analogTrigger: baudrate-1: Data not retrieved from DB");

			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("analogTrigger: JSONException5-2:" + e.getMessage());
			// cmbBxLDU_PortSelection2.setValue("");
			ApplicationLauncher.logger.info("analogTrigger: baudrate-2: Data not retrieved from database");

		}
		// qrPortSettingData.getString(key)//getCurrentLDU_ComPortID1();
		// String commBaudRate = //getCurrentLDU_ComBaudRate1();
		try {
			if ((!commBaudRate.isEmpty()) && (!commPortID.isEmpty())) {
				SpmQrScanner serialPortManagerQrScanner = new SpmQrScanner("qrScn-1");
				status = serialPortManagerQrScanner.powerSourceComInitV2(commPortID, commBaudRate);
				if (!status) {
					// status = false;
					ApplicationLauncher.logger.debug("analogTrigger :Analog Trigger  Failed");
				} else {
					// setPortValidationTurnedON(true);
					// status = serialDM_Obj.lscsLDU1_CheckCom();
					// DisplayDataObj.pwrSrcEnableSerialMonitoring_V2();
					serialPortManagerQrScanner.startSerialRxPhysical_PwrSrc();
					serialPortManagerQrScanner.enableSerialRxPhysical_QrScannerMonitor();
					QrScannerDirector pwrSrcDirector = new QrScannerDirector(serialPortManagerQrScanner);
					Map<String, Object> responseMap = pwrSrcDirector.scanQrCode();

					status = (boolean) responseMap.get("status");

					try {
						if (status) {
							qrData = (String) responseMap.get("responseData");
							ApplicationLauncher.logger.debug("analogTrigger: qrData1: " + qrData);
							qrData = NewlandQRCodeScanner.extractScannedData((String) responseMap.get("responseData"));
							ApplicationLauncher.logger.debug("analogTrigger: qrData2: " + qrData);

						} else {
							ApplicationLauncher.logger.debug("analogTrigger: No response ");
						}
					} catch (Exception e) {
						e.printStackTrace();
						ApplicationLauncher.logger.error("analogTrigger: Exception" + e.getMessage());
					}

					// DisplayDataObj.pwrSrcDisconnectPort_V2();
					serialPortManagerQrScanner.disconnectQrScanner();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("analogTrigger: Exception-X" + e.getMessage());
		}
		/*
		 * if(DisplayDataObj.ValidateAllComPortAccessible()){
		 * boolean isResponseExpected = true;
		 * status = sendReadCommandQrCodeScanner(ANALOG_TRIGGER_SETTING,
		 * isResponseExpected, EXPECTED_DATA_IN_HEX); // ANALOG_TRIGGER_SETTING
		 * }
		 */

		// DisplayDataObj.pwrSrcDisconnectPort_V2();
		// SerialDM_Obj.DisconnectPwrSrc();

		ApplicationLauncher.logger.debug("analogTrigger : Exit");
		return qrData;
	}

	public String analogTriggerV2(DeviceSetting deviceSetting) {
		ApplicationLauncher.logger.debug("analogTriggerV2 : Entry");
		boolean status = false;

		// String response = "";
		String qrData = "";
		String commPortID = "";
		String commBaudRate = "";
		String positionNo = deviceSetting.getPositionNo();

		commPortID = deviceSetting.getPortName();
		commBaudRate = deviceSetting.getBaudRate();
		String parentClassName = super.getClass().getSuperclass().getSimpleName();
		ApplicationLauncher.logger.debug("analogTriggerV2 : parentClassName: " + parentClassName);
		try {
			if ((!commBaudRate.isEmpty()) && (!commPortID.isEmpty())) {
				SpmQrScanner serialPortManagerQrScanner = new SpmQrScanner("qrScn-" + positionNo);
				status = serialPortManagerQrScanner.powerSourceComInitV2(commPortID, commBaudRate);
				if (!status) {
					// status = false;
					ApplicationLauncher.logger.debug("analogTriggerV2 :Analog Trigger  Failed");
				} else {
					// setPortValidationTurnedON(true);
					// status = serialDM_Obj.lscsLDU1_CheckCom();
					// DisplayDataObj.pwrSrcEnableSerialMonitoring_V2();
					serialPortManagerQrScanner.startSerialRxPhysical_PwrSrc();
					serialPortManagerQrScanner.enableSerialRxPhysical_QrScannerMonitor();
					QrScannerDirector pwrSrcDirector = new QrScannerDirector(serialPortManagerQrScanner);
					Map<String, Object> responseMap = pwrSrcDirector.scanQrCode();

					status = (boolean) responseMap.get("status");

					int retry_count = 3;
					try {
						for (int i = 0; i < retry_count; i++) {

							if (status) {
								qrData = (String) responseMap.get("responseData");
								ApplicationLauncher.logger.debug("analogTriggerV2: qrData1: " + qrData);
								qrData = NewlandQRCodeScanner
										.extractScannedData((String) responseMap.get("responseData"));
								ApplicationLauncher.logger
										.debug("analogTriggerV2: qrData2: <" + qrData + "> : Index : " + i);

								if (!qrData.contains("NG")) {
									break;
								}
							} else {
								ApplicationLauncher.logger.debug("analogTriggerV2: No response ");
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						ApplicationLauncher.logger.error("analogTriggerV2: Exception" + e.getMessage());
					}

					// DisplayDataObj.pwrSrcDisconnectPort_V2();
					serialPortManagerQrScanner.disconnectQrScanner();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("analogTriggerV2: Exception-X" + e.getMessage());
		}
		/*
		 * if(DisplayDataObj.ValidateAllComPortAccessible()){
		 * boolean isResponseExpected = true;
		 * status = sendReadCommandQrCodeScanner(ANALOG_TRIGGER_SETTING,
		 * isResponseExpected, EXPECTED_DATA_IN_HEX); // ANALOG_TRIGGER_SETTING
		 * }
		 */

		// DisplayDataObj.pwrSrcDisconnectPort_V2();
		// SerialDM_Obj.DisconnectPwrSrc();

		ApplicationLauncher.logger.debug("analogTriggerV2 : Exit");
		return qrData;
	}

	boolean sendReadCommandQrCodeScanner(String command, boolean isResponseExpected, String expectedDataInHex) {

		String payLoadInHex = command; // ANALOG_TRIGGER_SETTING;//startTestEndFrame ;xcvxc
		ApplicationLauncher.logger.info("send: payLoadInHex: " + payLoadInHex);

		int timeDelayInMilliSec = 0;
		// String expectedDataInHex = "06";//ConstantPowerSourceBofa.ER_LDU_STARTS_WITH
		// + addressStr;
		String responseData = "";
		String error1ExpectedDataInHex = NOT_GOOD_READ_EXPECTED_DATA_IN_HEX;
		// SerialDM_Obj.WriteToSerialCommPwrSrc(ANALOG_TRIGGER_SETTING,ER_ANALOG_TRIGGER_SETTING);
		String responseStatus = pwrSrcBofaMessenger.qrMsngrSendCommandProcess(payLoadInHex, timeDelayInMilliSec,
				expectedDataInHex, error1ExpectedDataInHex, isResponseExpected, "test-T1");

		if (responseStatus.equals(DeleteMeConstant.SUCCESS_RESPONSE)) {
			// if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
			responseData = pwrSrcBofaMessenger.getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
			responseData = GUIUtils.asciiToHex(responseData);
		} else {
			if (!isResponseExpected) {
				if (responseStatus.equals(DeleteMeConstant.NO_RESPONSE)) {
					ApplicationLauncher.logger
							.info("sendDataToBofaAfterSemaPhoreAcquired : no response expected success");
				}
			}
		}
		return false;
	}
	// ============================================================================================

	public static String extractScannedData(String hexData) {
		ApplicationLauncher.logger.info("extractScannedData: Entry");
		ApplicationLauncher.logger.info("extractScannedData: hexData : " + hexData);

		if (hexData == null || hexData.isEmpty()) {
			ApplicationLauncher.logger.info("extractScannedData: Invalid input");
			return null; // Invalid input
		}

		String[] hexBytes = hexData.trim().split("(?<=\\G..)"); // Split every 2 characters

		if (hexData.equals(NOT_GOOD_READ_EXPECTED_DATA_IN_HEX)) {
			return GUIUtils
					.hexToAsciiV2(NOT_GOOD_READ_EXPECTED_DATA_IN_HEX.replace(DeleteMeConstant.HV_ER_TERMINATOR, ""));
		}

		if (hexBytes.length < 4) {
			ApplicationLauncher.logger.info("extractScannedData: Not enough data for a valid terminator");
			return null; // Not enough data
		}

		String result = GUIUtils.hexToAsciiV2(hexData.replace(GOOD_READ_EXPECTED_BEGIN_DATA_IN_HEX, "")); // new
																											// String(scannedBytes,
																											// StandardCharsets.UTF_8);
		ApplicationLauncher.logger.info("extractScannedData: Resulting string : " + result);
		// result= result.replace("", "");cscd

		ApplicationLauncher.logger.info("extractScannedData: Exit");
		return result;
	}

	public TerminalProfileSetting getTerminalBayProfile() {
		return terminalBayProfile;
	}

	public void setTerminalBayProfile(TerminalProfileSetting terminalProfile) {
		this.terminalBayProfile = terminalProfile;
	}

	// ============================================================================================
}
