package com.tasnetwork.calibration.conveyor.bay;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantQrScanner;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.database.MySQL_Controller;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.serial.director.QrScannerDirector;
import com.tasnetwork.calibration.conveyor.serial.messenger.QrScannerMessenger;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmQrScanner;
import com.tasnetwork.calibration.conveyor.util.GUIUtils;
import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
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

	// ============================================================================================

	private boolean qR_code_scanner_init() {
		ApplicationLauncher.logger.debug("qR_code_scanner_init" + "Entry");

		boolean status = false;

		String[] scannerID = new String[ConstantBayPortNameMapping.NUM_OF_QR_CODE_SCANNERS_TERM_1];
		String baudRate = "";
		String verificType = "";
		String dataBits = "";
		String numOfStopBits = "";

		// 1. RS232 COMM SETTING
		for (int i = 0; i < ConstantBayPortNameMapping.NUM_OF_QR_CODE_SCANNERS_TERM_1; i++) {
			status = configRS232Comm(scannerID[i], baudRate, verificType, dataBits, numOfStopBits);
			if (!status) {
				ApplicationLauncher.logger
						.debug("qR_code_scanner_init: configRS232Comm for : " + scannerID[i] + " Failed");
			}
		}

		// 2. ENABLE_COMMAND_PROGRAMMING
		for (int i = 0; i < ConstantBayPortNameMapping.NUM_OF_QR_CODE_SCANNERS_TERM_1; i++) {
			status = enableCommandProgramming(scannerID[i]);
			if (!status) {
				ApplicationLauncher.logger
						.debug("qR_code_scanner_init: configRS232Comm for : " + scannerID[i] + " Failed");
			}
		}

		// 3. ENABLE ANALOG TRIGGER MODE
		for (int i = 0; i < ConstantBayPortNameMapping.NUM_OF_QR_CODE_SCANNERS_TERM_1; i++) {
			String receivedData = analogTrigger(scannerID[i]);

			if (receivedData == null || receivedData.isEmpty()) {
				return false;
			} // Invalid input

			if (receivedData.length() == 1) {
				return false;
			} // Not enough data

			// Verify the first and last bytes
			if (!(receivedData.equalsIgnoreCase("06"))) {
				return false; // Invalid format
			}

			if (!status) {
				ApplicationLauncher.logger
						.debug("qR_code_scanner_init: analog Trigger for : " + scannerID[i] + " Failed");
			}
		}

		ApplicationLauncher.logger.debug("qR_code_scanner_init" + "Exit");
		return status;
	}

	// ============================================================================================

	/*
	 * public String scan_QR_code(String scannerID){
	 * ApplicationLauncher.logger.debug("scan_QR_code : "+ scannerID + "Entry");
	 * 
	 * //boolean status = false;
	 * String receivedData = analogTrigger(scannerID); // ANALOG_TRIGGER_SETTING
	 * 
	 * // Convert received data to a readable string
	 * String scannedData = extractScannedData(receivedData);
	 * ApplicationLauncher.logger.debug("scan_QR_code: scannedData: " +
	 * scannedData);
	 * 
	 * if (scannedData.equals("06")) {
	 * // status = false;
	 * ApplicationLauncher.logger.debug("scan_QR_code: " + scannerID +
	 * "Failed : No QR Code Available");
	 * scannedData = "NO_QR_CODE_AVAILABLE" ;
	 * }
	 * else{
	 * ApplicationLauncher.logger.debug("scan_QR_code: " + scannerID +
	 * "Failed : No QR Code Available");
	 * scannedData = "SCNR_NW" ;
	 * }
	 * 
	 * 
	 * ApplicationLauncher.logger.debug("scan_QR_code : "+ scannerID + " : Exit");
	 * return scannedData;
	 * }
	 */

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

	// ============================================================================================

	private boolean triggerStop(String scannerID) {
		ApplicationLauncher.logger.debug("triggerStop : Entry");
		boolean status = false;

		boolean isResponseExpected = true;
		status = sendReadCommandQrCodeScanner(TRIGGER_STOP_SETTINGS, isResponseExpected,
				NOT_GOOD_READ_EXPECTED_DATA_IN_HEX); // TRIGGER_STOP_SETTINGS

		if (!status) {
			status = false;
			ApplicationLauncher.logger.debug("triggerStop : Trigger Stop Failed");
		}

		ApplicationLauncher.logger.debug("triggerStop : Exit");
		return status;
	}

	// ============================================================================================

	private boolean enableCommandProgramming(String scannerID) {
		ApplicationLauncher.logger.debug("enableCommandProgramming : Entry");
		boolean status = false;

		boolean isResponseExpected = true;
		status = sendReadCommandQrCodeScanner(ENABLE_COMMAND_PROGRAMMING, isResponseExpected,
				NOT_GOOD_READ_EXPECTED_DATA_IN_HEX); // ENABLE_COMMAND_PROGRAMMING

		if (!status) {
			status = false;
			ApplicationLauncher.logger.debug("enableCommandProgramming : Failed to enable Command Programming ");
		}

		ApplicationLauncher.logger.debug("enableCommandProgramming : Exit");
		return status;
	}
	// ============================================================================================

	private boolean disableCommandProgramming(String scannerID) {
		ApplicationLauncher.logger.debug("disableCommandProgramming : Entry");
		boolean status = false;

		boolean isResponseExpected = true;
		status = sendReadCommandQrCodeScanner(DISABLE_COMMAND_PROGRAMMING, isResponseExpected,
				NOT_GOOD_READ_EXPECTED_DATA_IN_HEX); // DISABLE_COMMAND_PROGRAMMING

		if (!status) {
			status = false;
			ApplicationLauncher.logger.debug("disableCommandProgramming : Failed to disable Command Programming ");
		}

		ApplicationLauncher.logger.debug("disableCommandProgramming : Exit");
		return status;
	}

	// ============================================================================================

	private boolean enable1dBarCode(String scannerID) {
		ApplicationLauncher.logger.debug("enable1dBarCode : Entry");
		boolean status = false;

		boolean isResponseExpected = true;
		status = sendReadCommandQrCodeScanner(ENABLE_1D_BAR_CODES, isResponseExpected,
				NOT_GOOD_READ_EXPECTED_DATA_IN_HEX); // ENABLE_1D_BAR_CODES

		if (!status) {
			status = false;
			ApplicationLauncher.logger.debug("enable1dBarCode : Failed to enable 1D bar code");
		}

		ApplicationLauncher.logger.debug("enable1dBarCode : Exit");
		return status;
	}
	// ============================================================================================

	private boolean disable1dBarCode(String scannerID) {
		ApplicationLauncher.logger.debug("disable1dBarCode : Entry");
		boolean status = false;

		boolean isResponseExpected = true;
		status = sendReadCommandQrCodeScanner(DISABLE_1D_BAR_CODES, isResponseExpected,
				NOT_GOOD_READ_EXPECTED_DATA_IN_HEX); // DISABLE_1D_BAR_CODES

		if (!status) {
			status = false;
			ApplicationLauncher.logger.debug("enable1dBarCode : Failed to disable 1D bar code");
		}

		ApplicationLauncher.logger.debug("disable1dBarCode : Exit");
		return status;
	}

	// ============================================================================================

	private boolean enable2dBarCode(String scannerID) {
		ApplicationLauncher.logger.debug("enable2dBarCode : Entry");
		boolean status = false;

		boolean isResponseExpected = true;
		status = sendReadCommandQrCodeScanner(ENABLE_2D_BAR_CODES, isResponseExpected,
				NOT_GOOD_READ_EXPECTED_DATA_IN_HEX); // ENABLE_2D_BAR_CODES

		if (!status) {
			status = false;
			ApplicationLauncher.logger.debug("enable2dBarCode : Failed to enable 1D bar code");
		}

		ApplicationLauncher.logger.debug("enable2dBarCode : Exit");
		return status;
	}
	// ============================================================================================

	private boolean disable2dBarCode(String scannerID) {
		ApplicationLauncher.logger.debug("disable2dBarCode : Entry");
		boolean status = false;

		boolean isResponseExpected = true;
		status = sendReadCommandQrCodeScanner(DISABLE_2D_BAR_CODES, isResponseExpected,
				NOT_GOOD_READ_EXPECTED_DATA_IN_HEX); // DISABLE_2D_BAR_CODES

		if (!status) {
			status = false;
			ApplicationLauncher.logger.debug("disable2dBarCode : Failed to disable 1D bar code");
		}

		ApplicationLauncher.logger.debug("disable2dBarCode : Exit");
		return status;
	}

	// ============================================================================================
	private boolean enableAllBarCode(String scannerID) {
		ApplicationLauncher.logger.debug("enableAllBarCode : Entry");
		boolean status = false;

		boolean isResponseExpected = true;
		status = sendReadCommandQrCodeScanner(GOOD_READ_BEEP_ENABLE, isResponseExpected,
				NOT_GOOD_READ_EXPECTED_DATA_IN_HEX); // GOOD_READ_BEEP_ENABLE

		if (!status) {
			status = false;
			ApplicationLauncher.logger.debug("enableAllBarCode : Failed to enable 1D bar code");
		}

		ApplicationLauncher.logger.debug("enableAllBarCode : Exit");
		return status;

	}
	// ============================================================================================

	private boolean disableAllBarCode(String scannerID) {
		ApplicationLauncher.logger.debug("disableAllBarCode : Entry");
		boolean status = false;

		boolean isResponseExpected = true;
		status = sendReadCommandQrCodeScanner(GOOD_READ_BEEP_DISABLE, isResponseExpected,
				NOT_GOOD_READ_EXPECTED_DATA_IN_HEX); // GOOD_READ_BEEP_DISABLE

		if (!status) {
			status = false;
			ApplicationLauncher.logger.debug("disableAllBarCode : Failed to disable 1D bar code");
		}

		ApplicationLauncher.logger.debug("disableAllBarCode : Exit");
		return status;

	}

	// ============================================================================================
	private boolean enableGoodReadBeep(String scannerID) {
		ApplicationLauncher.logger.debug("enableGoodReadBeep : Entry");
		boolean status = false;

		boolean isResponseExpected = true;
		status = sendReadCommandQrCodeScanner(ENABLE_ALL_BAR_CODES, isResponseExpected,
				NOT_GOOD_READ_EXPECTED_DATA_IN_HEX); // ENABLE_ALL_BAR_CODES

		if (!status) {
			status = false;
			ApplicationLauncher.logger.debug("enableGoodReadBeep : Failed to enable good read beep");
		}

		ApplicationLauncher.logger.debug("enableGoodReadBeep : Exit");
		return status;

	}
	// ============================================================================================

	private boolean disableGoodReadBeep(String scannerID) {
		ApplicationLauncher.logger.debug("disableGoodReadBeep : Entry");
		boolean status = false;

		boolean isResponseExpected = true;
		status = sendReadCommandQrCodeScanner(DISABLE_ALL_BAR_CODES, isResponseExpected,
				NOT_GOOD_READ_EXPECTED_DATA_IN_HEX); // DISABLE_ALL_BAR_CODES

		if (!status) {
			status = false;
			ApplicationLauncher.logger.debug("disableGoodReadBeep : Failed to disable good read beep");
		}

		ApplicationLauncher.logger.debug("disableGoodReadBeep : Exit");
		return status;

	}
	// ============================================================================================

	private boolean saveAsUserDefault(String scannerID) {
		ApplicationLauncher.logger.debug("saveAsUserDefault : Entry");
		boolean status = false;

		boolean isResponseExpected = true;
		status = sendReadCommandQrCodeScanner(SAVE_AS_USER_DEFAULT, isResponseExpected,
				NOT_GOOD_READ_EXPECTED_DATA_IN_HEX); // SAVE_AS_USER_DEFAULT

		if (!status) {
			status = false;
			ApplicationLauncher.logger.debug("saveAsUserDefault : Failed to save As user default");
		}

		ApplicationLauncher.logger.debug("saveAsUserDefault : Exit");
		return status;

	}

	// ============================================================================================

	private boolean configRS232Comm(String scannerID, String baudRate, String verificType, String dataBits,
			String numOfStopBits) {
		ApplicationLauncher.logger.debug("configRS232Comm : Entry");

		boolean status = false;

		boolean isResponseExpected = true;
		status = sendReadCommandQrCodeScanner(BAUD_RATE_9600_BPS, isResponseExpected,
				NOT_GOOD_READ_EXPECTED_DATA_IN_HEX); // BAUD_RATE_9600_BPS

		if (status) {
			BayUtils.delay(10);
			isResponseExpected = true;
			status = sendReadCommandQrCodeScanner(RS232_NO_VERIFY, isResponseExpected,
					NOT_GOOD_READ_EXPECTED_DATA_IN_HEX); // RS232_NO_VERIFY
		} else {
			status = false;
			ApplicationLauncher.logger.debug("configRS232Comm : Failed to set baud rate");
		}

		if (status) {
			BayUtils.delay(10);
			isResponseExpected = true;
			status = sendReadCommandQrCodeScanner(RS232_8_DIGITS, isResponseExpected,
					NOT_GOOD_READ_EXPECTED_DATA_IN_HEX); // RS232_8_DIGITS
		} else {
			status = false;
			ApplicationLauncher.logger.debug("configRS232Comm : Failed to set verification type");
		}

		if (status) {
			BayUtils.delay(10);
			isResponseExpected = true;
			status = sendReadCommandQrCodeScanner(RS232_ONE_STOP_BIT, isResponseExpected,
					NOT_GOOD_READ_EXPECTED_DATA_IN_HEX); // RS232_ONE_STOP_BIT
		} else {
			status = false;
			ApplicationLauncher.logger.debug("configRS232Comm : Failed to set num of data bits");
		}

		if (!status) {
			status = false;
			ApplicationLauncher.logger.debug("configRS232Comm : Failed to set num of stop bits");
		}

		ApplicationLauncher.logger.debug("configRS232Comm : Exit");
		return status;
	}
	// ============================================================================================

	boolean sendReadCommandQrCodeScanner(String command, boolean isResponseExpected, String expectedDataInHex) {

		boolean status = false;
		// boolean isResponseExpected = true;

		String payLoadInHex = command; // ANALOG_TRIGGER_SETTING;//startTestEndFrame ;xcvxc
		ApplicationLauncher.logger.info("send: payLoadInHex: " + payLoadInHex);

		/*
		 * String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address +
		 * ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;
		 * 
		 * if (address == ConstantPowerSourceBofa.LDU_INT_BROADCAST_ADDRESS) {
		 * addressStr = ConstantPowerSourceBofa.LDU_HEX_BROADCAST_ADDRESS ;
		 * }
		 */

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

			status = true;
			// status = processResponse(readTheConstantOfLiveReferenceMeterCmdFrame,
			// CurrentReadData);
		} else {
			if (!isResponseExpected) {
				if (responseStatus.equals(DeleteMeConstant.NO_RESPONSE)) {
					ApplicationLauncher.logger
							.info("sendDataToBofaAfterSemaPhoreAcquired : no response expected success");
					status = true;
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

		// Split into hex bytes
		// String[] hexBytes = hexData.trim().split("(?<=\\G..)"); // Split every 2
		// characters
		String[] hexBytes = hexData.trim().split("(?<=\\G..)"); // Split every 2 characters
		// ApplicationLauncher.logger.info("extractScannedData: hexBytes : " +
		// Arrays.toString(hexBytes));

		// Handle the case where only the start byte '06' is received
		// if (hexBytes.length == 1 &&
		// hexBytes[0].equalsIgnoreCase(NOT_GOOD_READ_EXPECTED_DATA_IN_HEX)){//"06")) {
		/*
		 * if (hexBytes.length == 1 &&
		 * hexBytes[0].equalsIgnoreCase(NOT_GOOD_READ_EXPECTED_DATA_IN_HEX)){//"06")) {
		 * ApplicationLauncher.logger.
		 * info("extractScannedData: Only start byte '06' received. Returning '06' directly."
		 * );
		 * return NOT_GOOD_READ_EXPECTED_DATA_IN_HEX; // Return just the start byte if
		 * that's the only data
		 * }
		 */

		if (hexData.equals(NOT_GOOD_READ_EXPECTED_DATA_IN_HEX)) {
			return GUIUtils
					.hexToAsciiV2(NOT_GOOD_READ_EXPECTED_DATA_IN_HEX.replace(DeleteMeConstant.HV_ER_TERMINATOR, ""));
		}

		if (hexBytes.length < 4) {
			ApplicationLauncher.logger.info("extractScannedData: Not enough data for a valid terminator");
			return null; // Not enough data
		}

		// Verify terminators
		/*
		 * if (!hexBytes[0].equalsIgnoreCase(NOT_GOOD_READ_EXPECTED_DATA_IN_HEX)||
		 * //"06") ||
		 * !hexBytes[hexBytes.length - 2].equalsIgnoreCase("0D") ||
		 * !hexBytes[hexBytes.length - 1].equalsIgnoreCase("0A")) {
		 * ApplicationLauncher.logger.
		 * info("extractScannedData: Invalid format or terminator");
		 * return null; // Invalid format
		 * }
		 */

		// Calculate data length and extract data bytes
		/*
		 * int dataLength = hexBytes.length -
		 * ((GOOD_READ_EXPECTED_BEGIN_DATA_IN_HEX.length()/2) + 2); // Exclude 06 (start
		 * byte) and 0D 0A (terminators)
		 * ApplicationLauncher.logger.info("extractScannedData: Data length : " +
		 * dataLength);
		 * 
		 * byte[] scannedBytes = new byte[dataLength];
		 * for (int i = 0; i < dataLength; i++) {
		 * try {
		 * // scannedBytes[i] = (byte) Integer.parseInt(hexBytes[i + 1], 16); // Start
		 * after 06
		 * scannedBytes[i] = (byte) Integer.parseInt(hexBytes[i], 16); // Start after 06
		 * } catch (NumberFormatException e) {
		 * ApplicationLauncher.logger.
		 * error("extractScannedData: Invalid hex byte at index " + (i + 1) + " : " +
		 * hexBytes[i + 1], e);
		 * return null; // Invalid data
		 * }
		 * }
		 */

		// ApplicationLauncher.logger.info("extractScannedData: Extracted bytes : " +
		// Arrays.toString(scannedBytes));

		// Convert bytes to string
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

	/*
	 * public void ScanForSerialPorts(){
	 * ApplicationLauncher.logger.info("ScanForSerialPorts: Entry");
	 * ApplicationHomeController.update_left_status("Scanning serial ports"
	 * ,ConstantApp.LEFT_STATUS_DEBUG);
	 * SerialDM_Obj.ScanForSerialCommPort();
	 * }
	 */

}
