package com.tasnetwork.calibration.energymeter.serial.portmanagerV2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fazecast.jSerialComm.SerialPort;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.deployment.DutResponse;
import com.tasnetwork.calibration.energymeter.util.ErrorCodeMapping;
import com.tasnetwork.spring.orm.model.DeviceSetting;
import com.tasnetwork.spring.orm.model.DutCommand;

public class DutCmdManager {

	public DutCmdManager() {
		// boolean comSerialStatusConnected = false;
		// SerialPortManagerDutCmd_V3 dutSpm = new SerialPortManagerDutCmd_V3(this);
		for (int i = 0; i <= 48; i++) {
			boolean comSerialStatusConnected = false;
			comSerialStatusConnectedList.add(comSerialStatusConnected);
			SerialPortManagerDutCmd_V3 dutSpm = new SerialPortManagerDutCmd_V3(this);
			dutSpmList.add(dutSpm);
		}
	}

	private boolean userAborted = false;
	private SerialPort[] scannedSerialPortList = new SerialPort[0];

	private DutCommand dutCommand = new DutCommand();

	private Map<Integer, String> portErrorMap = new HashMap<Integer, String>();

	public ArrayList<Boolean> comSerialStatusConnectedList = new ArrayList<Boolean>(
	);

	public List<SerialPortManagerDutCmd_V3> dutSpmList = new ArrayList<SerialPortManagerDutCmd_V3>(
	);

	public DutResponse dutCmdSerialPortAccessible(String deviceId, int dutPositionNo) {
		ApplicationLauncher.logger
				.debug("dutCmdSerialPortAccessible: dutPositionNo: " + dutPositionNo + " -> deviceId: " + deviceId);

		boolean status = false;
		// String dutCmdCommPortID= null;
		// String dutCmdCommBaudRate = null;
		// txtValidatedutCmdCmdStatus.clear();
		DutResponse dutResponse = new DutResponse();
		try {

			// dutCmdCommPortID = getCurrentdutCmdComPortID();
			// dutCmdCommBaudRate = getCurrentdutCmdComBaudRate();
			// status =
			// displayDataObj.pwrSrcPortAccessible_V2_1(dutCmdCommPortID,dutCmdCommBaudRate);
			// BayUtils bayUtils = new BayUtils();
			Optional<DeviceSetting> deviceSettingOpt = MySqlServiceManager.getDeviceSettingService()
					.findByDeviceIdAndPositionNo(deviceId, String.valueOf(dutPositionNo));
			// String deviceId = "010204EM01";
			if (deviceSettingOpt.isPresent()) {
				String selectedComPortId = deviceSettingOpt.get().getPortName();// "COM3";
				String selectedBaudRate = deviceSettingOpt.get().getBaudRate();// "9600";
				dutSpmList.get(dutPositionNo).init(deviceId);
				dutResponse = dutCmdPortAccessible_V2_1(dutPositionNo, selectedComPortId,
						selectedBaudRate);

				if (!dutResponse.getStatus()) {

					// txtValidatePwrSrcCmdStatus.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
					// dutResponse.setResponseData(selectedComPortId + DeleteMeConstant.NO_PORT);
				} else {

					setComSerialStatusConnectedList(dutPositionNo, true);
					dutResponse.setStatus(true);
					// displayDataObj.dutCmdEnableSerialMonitoring_V2();
					dutSpmList.get(dutPositionNo).startSerialRxPhysical_Dut();
					dutSpmList.get(dutPositionNo).enableSerialRxPhysical_DutMonitor();
					// DutCmdDirectorV3 dutCmdDirector = new DutCmdDirectorV3();
					// status = dutCmdDirector.setPowerSourceOff();

					if (!status) {
						// txtValidatePwrSrcCmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
					} else {
						// txtValidatePwrSrcCmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
					}

				}
			} else {
				dutResponse.setResponseData(deviceId + " -> " + dutPositionNo + " : db-Config-Fail");
				ApplicationLauncher.logger.debug("dutCmdSerialPortAccessible: failed for database mapping : deviceId: "
						+ deviceId + ", dutPositionNo: " + dutPositionNo);

			}

		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("dutCmdSerialPortAccessible: Exception1:" + e.getMessage());
			// ApplicationLauncher.logger.info("PwrSrcValidateSerialCmd:
			// Exception:"+e.toString());
			// txtValidatePwrSrcCmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
		}
		return dutResponse;
	}

	public void dutCmdDisconnectPort_V2(int dutPositionNo) {

		dutSpmList.get(dutPositionNo).disconnectDut();
		setComSerialStatusConnectedList(dutPositionNo, false);
	}

	public SerialPortManagerDutCmd_V3 getDutSpm(int dutPositionNo) {

		return dutSpmList.get(dutPositionNo);
	}

	public DutResponse dutCmdPortAccessible_V2_1(int dutPositionId, String selectedComPortId,
			String selectedBaudRate) {
		// ApplicationHomeController.update_left_status("DutCmd COM port access
		// validation",ConstantApp.LEFT_STATUS_DEBUG);

		// LoadCurrentSerialComSettingFromDB();
		boolean status = false;
		// setPowerSourcePortInitSuccess(false);
		// if( DisplayLDU_Init()) {

		DutResponse dutResponse = dutCmd_ComInitV2_1(dutPositionId, selectedComPortId,
				selectedBaudRate);
		if (dutResponse.getStatus()) {

			status = true;
			// setPowerSourcePortInitSuccess(true);

			ApplicationLauncher.logger.info("dutCmdPortAccessible_V2_1: Serial port accessable");

		} else {

			// setAllPortInitSuccess(false);
			ApplicationLauncher.logger.info("dutCmdPortAccessible_V2_1: " + ErrorCodeMapping.ERROR_CODE_902 + ": "
					+ ErrorCodeMapping.ERROR_CODE_902_MSG);
			// WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_900,ErrorCodeMapping.ERROR_CODE_900_MSG,AlertType.ERROR);

		}

		return dutResponse;

	}

	public DutResponse dutCmd_ComInitV2_1(int dutPositionId, String selectedComPortId,
			String selectedBaudRate) {

		/*
		 * String selectedComPortId = "com14";
		 * String selectedBaudRate = "9600";
		 */
		// boolean status =
		DutResponse dutResponse = dutSpmList.get(dutPositionId).dutComInitV2(selectedComPortId, selectedBaudRate);// HVCI_CommPortID,
																													// HVCI_CommBaudRate);
		return dutResponse;
	}

	public ArrayList<Boolean> getComSerialStatusConnectedList() {
		return comSerialStatusConnectedList;
	}

	public void setComSerialStatusConnectedList(ArrayList<Boolean> comSerialStatusConnectedList) {
		this.comSerialStatusConnectedList = comSerialStatusConnectedList;
	}

	public void setComSerialStatusConnectedList(int positionId, boolean status) {
		this.comSerialStatusConnectedList.set(positionId, status);
	}

	public boolean isComSerialStatusConnected(int positionId) {
		return this.comSerialStatusConnectedList.get(positionId);
	}

	public SerialPort[] getScannedSerialPortList() {
		return scannedSerialPortList;
	}

	public void setScannedSerialPortList(SerialPort[] scannedSerialPortList) {
		this.scannedSerialPortList = scannedSerialPortList;
	}

	public DutCommand getDutCommand() {
		return dutCommand;
	}

	public void setDutCommand(DutCommand dutCommand) {
		this.dutCommand = dutCommand;
	}

	public Map<Integer, String> getPortErrorMap() {
		return portErrorMap;
	}

	public String getPortErrorMap(int dutPositionNo) {
		return portErrorMap.getOrDefault(dutPositionNo, "");
	}

	/*
	 * public void setPortErrorMap(Map<Integer, String> portErrorMap) {
	 * this.portErrorMap = portErrorMap;
	 * }
	 */

	public void setPortErrorMap(int dutPositionNo, String portError) {
		this.portErrorMap.put(dutPositionNo, portError);
	}

	public boolean isUserAborted() {
		return userAborted;
	}

	public void setUserAborted(boolean userAborted) {
		this.userAborted = userAborted;
	}
}
