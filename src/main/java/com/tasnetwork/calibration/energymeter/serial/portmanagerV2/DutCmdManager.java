package com.tasnetwork.calibration.energymeter.serial.portmanagerV2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import com.fazecast.jSerialComm.SerialPort;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantDut;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.constant.DeleteMeConstant;
import com.tasnetwork.calibration.energymeter.deployment.DutResponse;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.director.DutCmdDirectorV3;
import com.tasnetwork.calibration.energymeter.util.ErrorCodeMapping;
import com.tasnetwork.spring.orm.model.DeviceSetting;
import com.tasnetwork.spring.orm.model.DutCommand;
import com.tasnetwork.spring.orm.model.DutExecutionResult;

import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;

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

	/*
	 * public boolean dut0ComSerialStatusConnected = false;
	 * public boolean dut1ComSerialStatusConnected = false;
	 * public boolean dut2ComSerialStatusConnected = false;
	 * public boolean dut3ComSerialStatusConnected = false;
	 * public boolean dut4ComSerialStatusConnected = false;
	 * public boolean dut5ComSerialStatusConnected = false;
	 * public boolean dut6ComSerialStatusConnected = false;
	 * public boolean dut7ComSerialStatusConnected = false;
	 * public boolean dut8ComSerialStatusConnected = false;
	 * public boolean dut9ComSerialStatusConnected = false;
	 * public boolean dut10ComSerialStatusConnected = false;
	 * 
	 * public boolean dut11ComSerialStatusConnected = false;
	 * public boolean dut12ComSerialStatusConnected = false;
	 * public boolean dut13ComSerialStatusConnected = false;
	 * public boolean dut14ComSerialStatusConnected = false;
	 * public boolean dut15ComSerialStatusConnected = false;
	 * public boolean dut16ComSerialStatusConnected = false;
	 * public boolean dut17ComSerialStatusConnected = false;
	 * public boolean dut18ComSerialStatusConnected = false;
	 * public boolean dut19ComSerialStatusConnected = false;
	 * public boolean dut20ComSerialStatusConnected = false;
	 * 
	 * public boolean dut21ComSerialStatusConnected = false;
	 * public boolean dut22ComSerialStatusConnected = false;
	 * public boolean dut23ComSerialStatusConnected = false;
	 * public boolean dut24ComSerialStatusConnected = false;
	 * public boolean dut25ComSerialStatusConnected = false;
	 * public boolean dut26ComSerialStatusConnected = false;
	 * public boolean dut27ComSerialStatusConnected = false;
	 * public boolean dut28ComSerialStatusConnected = false;
	 * public boolean dut29ComSerialStatusConnected = false;
	 * public boolean dut30ComSerialStatusConnected = false;
	 * 
	 * public boolean dut31ComSerialStatusConnected = false;
	 * public boolean dut32ComSerialStatusConnected = false;
	 * public boolean dut33ComSerialStatusConnected = false;
	 * public boolean dut34ComSerialStatusConnected = false;
	 * public boolean dut35ComSerialStatusConnected = false;
	 * public boolean dut36ComSerialStatusConnected = false;
	 * public boolean dut37ComSerialStatusConnected = false;
	 * public boolean dut38ComSerialStatusConnected = false;
	 * public boolean dut39ComSerialStatusConnected = false;
	 * public boolean dut40ComSerialStatusConnected = false;
	 * 
	 * public boolean dut41ComSerialStatusConnected = false;
	 * public boolean dut42ComSerialStatusConnected = false;
	 * public boolean dut43ComSerialStatusConnected = false;
	 * public boolean dut44ComSerialStatusConnected = false;
	 * public boolean dut45ComSerialStatusConnected = false;
	 * public boolean dut46ComSerialStatusConnected = false;
	 * public boolean dut47ComSerialStatusConnected = false;
	 * public boolean dut48ComSerialStatusConnected = false;
	 */

	public ArrayList<Boolean> comSerialStatusConnectedList = new ArrayList<Boolean>(/*
																					 * Arrays.asList(
																					 * dut0ComSerialStatusConnected,
																					 * dut1ComSerialStatusConnected,
																					 * dut2ComSerialStatusConnected,
																					 * dut3ComSerialStatusConnected,
																					 * dut4ComSerialStatusConnected,
																					 * dut5ComSerialStatusConnected,
																					 * dut6ComSerialStatusConnected,
																					 * dut7ComSerialStatusConnected,
																					 * dut8ComSerialStatusConnected,
																					 * dut9ComSerialStatusConnected,
																					 * dut10ComSerialStatusConnected,
																					 * dut11ComSerialStatusConnected,
																					 * dut12ComSerialStatusConnected,
																					 * dut13ComSerialStatusConnected,
																					 * dut14ComSerialStatusConnected,
																					 * dut15ComSerialStatusConnected,
																					 * dut16ComSerialStatusConnected,
																					 * dut17ComSerialStatusConnected,
																					 * dut18ComSerialStatusConnected,
																					 * dut19ComSerialStatusConnected,
																					 * dut20ComSerialStatusConnected,
																					 * dut21ComSerialStatusConnected,
																					 * dut22ComSerialStatusConnected,
																					 * dut23ComSerialStatusConnected,
																					 * dut24ComSerialStatusConnected,
																					 * dut25ComSerialStatusConnected,
																					 * dut26ComSerialStatusConnected,
																					 * dut27ComSerialStatusConnected,
																					 * dut28ComSerialStatusConnected,
																					 * dut29ComSerialStatusConnected,
																					 * dut30ComSerialStatusConnected,
																					 * dut31ComSerialStatusConnected,
																					 * dut32ComSerialStatusConnected,
																					 * dut33ComSerialStatusConnected,
																					 * dut34ComSerialStatusConnected,
																					 * dut35ComSerialStatusConnected,
																					 * dut36ComSerialStatusConnected,
																					 * dut37ComSerialStatusConnected,
																					 * dut38ComSerialStatusConnected,
																					 * dut39ComSerialStatusConnected,
																					 * dut40ComSerialStatusConnected,
																					 * dut41ComSerialStatusConnected,
																					 * dut42ComSerialStatusConnected,
																					 * dut43ComSerialStatusConnected,
																					 * dut44ComSerialStatusConnected,
																					 * dut45ComSerialStatusConnected,
																					 * dut46ComSerialStatusConnected,
																					 * dut47ComSerialStatusConnected,
																					 * dut48ComSerialStatusConnected
																					 * 
																					 * 
																					 * )
																					 */);

	/*
	 * public SerialPortManagerDutCmd_V3 dut0_Spm = new
	 * SerialPortManagerDutCmd_V3(this);///not used, just keeping for index
	 * public SerialPortManagerDutCmd_V3 dut1_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut2_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut3_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut4_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut5_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut6_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut7_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut8_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut9_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * 
	 * public SerialPortManagerDutCmd_V3 dut10_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut11_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut12_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut13_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut14_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut15_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut16_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut17_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut18_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut19_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * 
	 * public SerialPortManagerDutCmd_V3 dut20_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut21_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut22_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut23_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut24_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut25_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut26_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut27_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut28_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut29_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * 
	 * public SerialPortManagerDutCmd_V3 dut30_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut31_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut32_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut33_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut34_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut35_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut36_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut37_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut38_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut39_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * 
	 * public SerialPortManagerDutCmd_V3 dut40_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut41_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut42_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut43_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut44_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut45_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut46_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut47_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 * public SerialPortManagerDutCmd_V3 dut48_Spm = new
	 * SerialPortManagerDutCmd_V3(this);
	 */
	// public SerialPortManagerDutCmd_V3 dut9_Spm = new
	// SerialPortManagerDutCmd_V3();

	public List<SerialPortManagerDutCmd_V3> dutSpmList = new ArrayList<SerialPortManagerDutCmd_V3>(/*
																									 * Arrays.asList(
																									 * 
																									 * dut0_Spm,///not
																									 * used, just
																									 * keeping for index
																									 * dut1_Spm,dut2_Spm
																									 * ,dut3_Spm,
																									 * dut4_Spm,dut5_Spm
																									 * ,dut6_Spm,
																									 * dut7_Spm,dut8_Spm
																									 * ,dut9_Spm,
																									 * dut10_Spm,
																									 * dut11_Spm,
																									 * dut12_Spm,
																									 * dut13_Spm,
																									 * dut14_Spm,
																									 * dut15_Spm,
																									 * dut16_Spm,
																									 * dut17_Spm,
																									 * dut18_Spm,
																									 * dut19_Spm,
																									 * dut20_Spm,
																									 * dut21_Spm,
																									 * dut22_Spm,
																									 * dut23_Spm,
																									 * dut24_Spm,
																									 * dut25_Spm,
																									 * dut26_Spm,
																									 * dut27_Spm,
																									 * dut28_Spm,
																									 * dut29_Spm,
																									 * dut30_Spm,
																									 * dut31_Spm,
																									 * dut32_Spm,
																									 * dut33_Spm,
																									 * dut34_Spm,
																									 * dut35_Spm,
																									 * dut36_Spm,
																									 * dut37_Spm,
																									 * dut38_Spm,
																									 * dut39_Spm,
																									 * dut40_Spm,
																									 * dut41_Spm,
																									 * dut42_Spm,
																									 * dut43_Spm,
																									 * dut44_Spm,
																									 * dut45_Spm,
																									 * dut46_Spm,
																									 * dut47_Spm,
																									 * dut48_Spm
																									 * 
																									 * )
																									 */);

	// DeviceDataManagerController displayDataObj = new
	// DeviceDataManagerController();

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

			// ApplicationLauncher.logger.info("PwrSrcValidateSerialCmd:
			// testD:"+serialDM_Obj.commPowerSrc.getPortDeviceMapping());

			// displayDataObj.dutCmdDisconnectPort_V2();
			// dutSpmList.get(dutPositionNo).disconnectDut();
			// setComSerialStatusConnectedList(dutPositionNo,false);

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

	/*
	 * Timer dutCommandTimer;
	 * 
	 * 
	 * public void dutExecuteCommandTrigger() {
	 * ApplicationLauncher.logger.debug("dutExecuteCommandTrigger :Entry");
	 * dutCommandTimer = new Timer();
	 * dutCommandTimer.schedule(new dutExecuteCommandTask(), 100);
	 * ApplicationLauncher.logger.debug("dutExecuteCommandTrigger : Exit");
	 * 
	 * }
	 * 
	 * class dutExecuteCommandTask extends TimerTask {
	 * public void run() {
	 * 
	 * 
	 * ApplicationLauncher.logger.
	 * debug("dutExecuteCommandTask : getDeviceMountedMap: " +
	 * ProjectExecutionController.getDeviceMountedMap());
	 * ProjectExecutionController.getDeviceMountedMap().keySet().parallelStream().
	 * forEach(lduPosition -> {
	 * try {
	 * int positionId = Integer.parseInt(lduPosition);
	 * //Map<String, Object> responseReturn = new HashMap<>();
	 * DutResponse dutResponse = new DutResponse();
	 * //responseReturn.put("status", false);
	 * //responseReturn.put("result", "NG");
	 * dutResponse.setResponseData("NG");
	 * ApplicationLauncher.logger.debug("dutExecuteCommandTask: positionId:" +
	 * positionId);
	 * boolean status = true;
	 * if(!comSerialStatusConnectedList.get(positionId-1)) {
	 * dutResponse = serialConnectDutX(positionId);
	 * status = dutResponse.getStatus();//(boolean)responseReturn.get("status");
	 * }
	 * if(status) {
	 * //status = dutSendCmd(positionId);
	 * 
	 * dutResponse = dutSendCmd(positionId);
	 * status = dutResponse.getStatus();//(boolean)responseReturn.get("status");
	 * String resultStatus, resultValue;
	 * resultValue =
	 * dutResponse.getResponseData();//(String)responseReturn.get("result");
	 * 
	 * if(status) {
	 * resultStatus = ConstantReport.RESULT_STATUS_PASS;
	 * if(resultValue.equals("")) {
	 * resultValue = ConstantReport.REPORT_POPULATE_PASS;
	 * }
	 * //
	 * 
	 * } else {
	 * resultStatus = ConstantReport.RESULT_STATUS_FAIL;
	 * resultValue = ConstantReport.REPORT_POPULATE_FAIL;
	 * }
	 * LiveTableDataManager.UpdateliveTableData(positionId, resultStatus,
	 * resultValue);
	 * } else {
	 * String responseData =
	 * dutResponse.getResponseData();//(String)responseReturn.get("result");
	 * LiveTableDataManager.UpdateliveTableData(
	 * positionId,
	 * ConstantReport.RESULT_STATUS_FAIL,
	 * responseData
	 * );
	 * }
	 * 
	 * } catch (Exception e) {
	 * ApplicationLauncher.logger.error("Error in parallel execution for position "
	 * + lduPosition, e);
	 * }
	 * });
	 * 
	 * ApplicationLauncher.logger.
	 * debug("dutExecuteCommandTask :resetting setExecuteTimeCounter to zero");
	 * ProjectExecutionController.setExecuteTimeCounter(0);
	 * Sleep(2000);
	 * //ProjectExecutionController.semLockExecutionInprogress = false;
	 * dutCommandTimer.cancel();
	 * }
	 * }
	 * 
	 * 
	 * public DutResponse serialConnectDutX(int positionId) {
	 * 
	 * String DUT_CommPortID= null;
	 * String DUTCommBaudRate = null;
	 * DutResponse dutResponse = new DutResponse();
	 * 
	 * String srcType = ConstantDut.COM_SRC_TYPE_LIST.get(positionId-1);//"DUT1";
	 * 
	 * 
	 * boolean status = false;
	 * //txtValidateDUT_CmdStatus1.clear();
	 * //Map<String,Object> responseReturn = new HashMap<String,Object>();
	 * //responseReturn.put("status", false);
	 * try{
	 * 
	 * HashMap portMap = new HashMap();
	 * portMap = commDUT1.searchForPorts();
	 * //comDutList.get(positionId-1).searchForPorts(); ;
	 * DUT_CommPortID = get_port_name(srcType);//getCurrentDUT_ComPortID1();
	 * ApplicationLauncher.logger.debug("serialConnectDutX : portMap : " + portMap);
	 * if(portMap.containsKey(DUT_CommPortID)) {
	 * DUTCommBaudRate = get_baud_rate(srcType);//getCurrentDUT_ComBaudRate1();
	 * ApplicationLauncher.logger.
	 * debug("serialConnectDutX : serial port DUTCommBaudRate : " +
	 * DUTCommBaudRate);//
	 * 
	 * dutResponse = DutX_Init(DUT_CommPortID,DUTCommBaudRate,positionId);
	 * status = dutResponse.getStatus();//(boolean)responseReturn.get("status");
	 * String responseData =
	 * dutResponse.getResponseData();//(String)responseReturn.get("result");
	 * if (!status){
	 * ApplicationLauncher.logger.
	 * debug("serialConnectDutX : serial port access failed : positionId: " +
	 * positionId + " -> " + DUT_CommPortID) ;
	 * //txtValidateDUT_CmdStatus1.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * //txtAreaMeterSerialLog.appendText("Dut1: "
	 * +ConstantApp.SERIAL_PORT_ACCESS_FAILED + "\n");
	 * } else {
	 * ApplicationLauncher.logger.
	 * debug("serialConnectDutX : serial port connected : positionId: " + positionId
	 * + " -> " + DUT_CommPortID) ;
	 * 
	 * //setPortValidationTurnedON(true);
	 * //status = dutSerialDM_Obj.lscsDUT1_UnlockCmd();
	 * 
	 * //if (!status){
	 * //txtValidateDUT_CmdStatus1.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * // txtAreaMeterSerialLog.appendText("Dut1 Connected"+ "\n");
	 * //}else{
	 * //txtValidateDUT_CmdStatus1.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * // txtAreaMeterSerialLog.appendText(ConstantApp.SERIAL_PORT_COMMAND_Success +
	 * "\n");
	 * //}
	 * //setPortValidationTurnedON(false);
	 * //DisplayDataObj.setDUT1_ReadDataFlag(false);
	 * }
	 * }else {
	 * ApplicationLauncher.logger.
	 * debug("serialConnectDutX : serial port not found : positionId: " + positionId
	 * + " -> " + DUT_CommPortID) ;
	 * String responseData = DUT_CommPortID + " not found";
	 * dutResponse.setResponseData(responseData);
	 * //responseReturn.put("result", responseData);
	 * }
	 * 
	 * //dutSerialDM_Obj.DisconnectDUT1();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.
	 * error("serialConnectDutX: Exception : positionId : " + positionId +
	 * " : "+e.getMessage());
	 * }
	 * 
	 * return dutResponse;
	 * }
	 * 
	 * 
	 * public void Sleep(int timeInMsec) {
	 * 
	 * try {
	 * Thread.sleep(timeInMsec);
	 * } catch (InterruptedException e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.
	 * error("PowerSourceDirector: Sleep2 :InterruptedException:"+ e.getMessage());
	 * }
	 * 
	 * }
	 */

}
