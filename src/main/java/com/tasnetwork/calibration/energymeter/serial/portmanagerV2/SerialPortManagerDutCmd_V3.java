package com.tasnetwork.calibration.energymeter.serial.portmanagerV2;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.fazecast.jSerialComm.SerialPort;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantDut;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceBofa;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceLscs;
import com.tasnetwork.calibration.energymeter.constant.DeleteMeConstant;
import com.tasnetwork.calibration.energymeter.deployment.DutResponse;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

public class SerialPortManagerDutCmd_V3 {

	DeviceDataManagerController DisplayDataObj = new DeviceDataManagerController();
	private DutCmdManager parentManager;
	public CommunicatorV2 commDut = null;

	public SerialTxMessageQ_V2 txMsgQ_Dut = new SerialTxMessageQ_V2(commDut);

	public SerialRxMessageQ_V2 rxMsgQ_Dut = new SerialRxMessageQ_V2(commDut);
	public SerialRxPhysical_V2 rxPhysical_Dut = new SerialRxPhysical_V2(commDut, rxMsgQ_Dut, false, "DutV2",
			DeleteMeConstant.HV_ER_TERMINATOR, "");
	// public SerialRxPhysical rxPhysical_Dut = new
	// SerialRxPhysical(commPowerSrc,rxMsgQ_Dut,false,"Dut",DeleteMeConstant.HV_ER_TERMINATOR,"");

	public boolean bInitOccured = false;
	// public static boolean bInitOccured2 = false;
	public boolean DutSerialStatusConnected = false;

	// private static HashMap scannedSerialPortMap = new HashMap();

	public SerialPortManagerDutCmd_V3() {

	}

	public SerialPortManagerDutCmd_V3(DutCmdManager parent) {
		parentManager = parent;
		if (!bInitOccured) {
			InitSerialCommPort();
			bInitOccured = true;
			// SetBNC_OutputPortData();
			// rxMsgHV_Manager = new SerialDataMsgHV_Manager(commHVCI);
			// rxPhysicalHVCI = new SerialRxPhysical(commHVCI,rxMsgHV_Manager);
		}

	}

	public void InitSerialCommPort() {
		createObjects();
		// commPowerSrc.searchForPorts();
		// commHVCI.searchForPorts(); '
		/*
		 * if(parentManager.getScannedSerialPortList().length==0){
		 * parentManager.setScannedSerialPortList(scanForSerialCommPort());
		 * }else {
		 * commDut.setPortsAvailable(parentManager.getScannedSerialPortList());
		 * 
		 * }
		 */
	}

	private void createObjects() {
		// ApplicationLauncher.logger.debug("SerialPortManagerDut_V2: createObjects
		// :Entry");
		// commPowerSrc = new Communicator(ConstantApp.SERIAL_PORT_POWER_SOURCE);
		// commRefStandard = new Communicator(ConstantApp.SERIAL_PORT_REF_STD);
		/*
		 * commLDU = new Communicator(ConstantApp.SERIAL_PORT_LDU);
		 * 
		 * commBridge = new Communicator(ConstantProGEN_App.SERIAL_PORT_BRIDGE);
		 * commLVD = new
		 * Communicator(ConstantProGEN_App.SERIAL_PORT_PT_VOLTAGE_DIVIDER);
		 * commPT_Burden = new Communicator(ConstantProGEN_App.SERIAL_PORT_PT_BURDEN);
		 * commCT_Burden = new Communicator(ConstantProGEN_App.SERIAL_PORT_CT_BURDEN);
		 */
		commDut = new CommunicatorV2(ConstantPowerSourceLscs.SERIAL_PORT_POWER_SOURCE);// ConstantApp.SERIAL_PORT_REF_STD);//ConstantProGEN_App.SERIAL_PORT_HVCI);
		txMsgQ_Dut = new SerialTxMessageQ_V2(commDut);

		rxMsgQ_Dut = new SerialRxMessageQ_V2(commDut);
		rxPhysical_Dut = new SerialRxPhysical_V2(commDut, rxMsgQ_Dut, false, "DutV3", ConstantDut.ER_TERMINATOR, "");

		// ApplicationLauncher.logger.debug("SerialPortManagerDut_V2: createObjects
		// :Exit");
		// commVICI = new Communicator(ConstantPrimaryVICI_Meter.SERIAL_PORT_VICI);
	}

	public void init(String portInternalAliasId) {
		ApplicationLauncher.logger.debug("init: createObjects :Entry");
		ApplicationLauncher.logger.debug("createObjects: portInternalAliasId: " + portInternalAliasId);

		// commPowerSrc = new Communicator(ConstantApp.SERIAL_PORT_POWER_SOURCE);
		// commRefStandard = new Communicator(ConstantApp.SERIAL_PORT_REF_STD);
		/*
		 * commLDU = new Communicator(ConstantApp.SERIAL_PORT_LDU);
		 * 
		 * commBridge = new Communicator(ConstantProGEN_App.SERIAL_PORT_BRIDGE);
		 * commLVD = new
		 * Communicator(ConstantProGEN_App.SERIAL_PORT_PT_VOLTAGE_DIVIDER);
		 * commPT_Burden = new Communicator(ConstantProGEN_App.SERIAL_PORT_PT_BURDEN);
		 * commCT_Burden = new Communicator(ConstantProGEN_App.SERIAL_PORT_CT_BURDEN);
		 */
		commDut = new CommunicatorV2(portInternalAliasId);// ConstantApp.SERIAL_PORT_REF_STD);//ConstantProGEN_App.SERIAL_PORT_HVCI);
		txMsgQ_Dut = new SerialTxMessageQ_V2(commDut);

		rxMsgQ_Dut = new SerialRxMessageQ_V2(commDut);
		rxPhysical_Dut = new SerialRxPhysical_V2(commDut, rxMsgQ_Dut, false, portInternalAliasId,
				ConstantDut.ER_TERMINATOR, "");
		/*
		 * if(scannedSerialPortMap.isEmpty()){
		 * scannedSerialPortMap = scanForSerialCommPort();
		 * //bInitOccured2= true;
		 * }
		 */

		/*
		 * if(scannedSerialPortList.length==0){
		 * scannedSerialPortList = scanForSerialCommPort();
		 * }else {
		 * commDut.setPortsAvailable(scannedSerialPortList);
		 * 
		 * }
		 */
		if (parentManager.getScannedSerialPortList().length == 0) {
			parentManager.setScannedSerialPortList(scanForSerialCommPort());
		} else {
			commDut.setPortsAvailable(parentManager.getScannedSerialPortList());

		}

		ApplicationLauncher.logger.debug("init: createObjects :Exit");
		// commVICI = new Communicator(ConstantPrimaryVICI_Meter.SERIAL_PORT_VICI);
	}

	public SerialPort[] scanForSerialCommPort() {

		// commPowerSrc.searchForPorts();
		return commDut.searchForPorts();

	}

	public void startSerialRxPhysical_Dut() {

		rxPhysical_Dut.SerialRxPhysicalTimerStart();
		// rxPhysicalObj.setReadRxPhysicalFlag(true);
	}

	public void enableSerialRxPhysical_DutMonitor() {
		rxPhysical_Dut.setReadRxPhysicalFlag(true);

	}

	public void disableSerialRxPhysical_DutMonitor() {

		ApplicationLauncher.logger.debug("disableSerialRxPhysical_DutMonitor: " + rxPhysical_Dut.rxLabel);
		rxPhysical_Dut.setReadRxPhysicalFlag(false);

	}

	public void disconnectDut() {
		ApplicationLauncher.logger.debug("disconnectDut V2 :Entry");
		if (isDutSerialStatusConnected()) {
			// ApplicationLauncher.logger.debug("disconnectDut :DutSerialStatusConnected:" +
			// isDutSerialStatusConnected());
			disableSerialRxPhysical_DutMonitor();
			// DisplayDataObj.setDutReadDataFlag(false);
			Sleep(10);
			disconnectDutSerialComm();
			// ApplicationLauncher.logger.debug("setDutSerialStatusConnected :Hit1");
			setDutSerialStatusConnected(false);

		} else {
			ApplicationLauncher.logger.debug("disconnectDut V2 : Path2");
		}
	}

	/*
	 * public void disconnectDut(int dutPositionNo){
	 * ApplicationLauncher.logger.debug("disconnectDut V2 :Entry");
	 * if(isDutSerialStatusConnected()){
	 * ApplicationLauncher.logger.debug("disconnectDut :DutSerialStatusConnected:" +
	 * isDutSerialStatusConnected() + " -> position: " +dutPositionNo);
	 * disableSerialRxPhysical_DutMonitor();
	 * //DisplayDataObj.setDutReadDataFlag(false);
	 * Sleep(10);
	 * disconnectDutSerialComm();
	 * setDutSerialStatusConnected(false);
	 * 
	 * }else {
	 * ApplicationLauncher.logger.debug("disconnectDut V2 : Path2");
	 * }
	 * }
	 */

	public void disconnectDutSerialComm() {
		ApplicationLauncher.logger.debug("disconnectDutSerialComm V2 :Entry");
		commDut.disconnect();
	}

	public void disconnectDutSerialCommIfConnected() {
		ApplicationLauncher.logger.debug("disconnectDutSerialCommIfConnected V2 :Entry");
		try {
			if (commDut.isDeviceConnected()) {
				ApplicationLauncher.logger.debug("disconnectDutSerialCommIfConnected :Entry2:");
				disableSerialRxPhysical_DutMonitor();
				// DisplayDataObj.setDutReadDataFlag(false);
				Sleep(10);
				disconnectDutSerialComm();
				// ApplicationLauncher.logger.debug("setDutSerialStatusConnected :Hit2");
				setDutSerialStatusConnected(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("disconnectDutSerialCommIfConnected : Exception: " + e.getMessage());
		}
	}

	public DutResponse dutComInitV2(String InputComm, String BaudRate) {
		ApplicationLauncher.logger.debug("dutComInitV2 Invoked:");
		ApplicationLauncher.logger.debug("CommInput: " + InputComm);
		ApplicationLauncher.logger.debug("BaudRate: " + BaudRate);
		DutResponse dutResponse = new DutResponse();
		try {

			dutResponse = dutCommSetting(InputComm, BaudRate);
			// ApplicationLauncher.logger.debug("setDutSerialStatusConnected :Hit3:"
			// +dutResponse.getStatus()+ " , Comm -"+ InputComm);
			setDutSerialStatusConnected(dutResponse.getStatus());
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("dutComInit :UnsupportedEncodingException:" + e.getMessage());
		}

		return dutResponse;// isDutSerialStatusConnected();
	}

	public DutResponse dutCommSetting(String CommInput, String BaudRate) throws UnsupportedEncodingException {
		ApplicationLauncher.logger.debug("dutCommSetting :Entry");
		// InitSerialCommPort();
		ApplicationLauncher.logger.debug("dutComm: " + commDut);
		ApplicationLauncher.logger.debug("CommInput: " + CommInput);
		ApplicationLauncher.logger.debug("BaudRate: " + BaudRate);
		boolean status = false;
		DutResponse dutResponse = setSerialCommV2(commDut, CommInput, Integer.valueOf(BaudRate), true);

		if (dutResponse.getStatus()) {
			// commPowerSource.setFlowControlMode();
			// commRefStandard.SetFlowControlModeV2();
		} else {

			ApplicationLauncher.logger.info("dutCommSetting:" + CommInput + " access failed");
		}
		return dutResponse;

	}

	public DutResponse setSerialCommV2(CommunicatorV2 SerialPortObj, String SerialPort_ID, Integer BaudRate,
			Boolean ReadHexFormat) {
		ApplicationLauncher.logger.debug("setSerialCommV2 :Entry");
		DutResponse dutResponse = new DutResponse();
		boolean status = false;
		try {
			ApplicationLauncher.logger.debug("setSerialCommV2 : test1");
			int retryCount = 3;
			boolean isDevicePortExist = false;
			while ((retryCount > 0) && (!isDevicePortExist)) {
				isDevicePortExist = SerialPortObj.isDevicePortExist(SerialPort_ID);
				if (!isDevicePortExist) {
					ApplicationLauncher.logger.debug("setSerialCommV2 : Serial port not found : " + SerialPort_ID);
					// dutResponse.setResponseData(SerialPort_ID + "-" +DeleteMeConstant.NO_PORT);
					// return dutResponse;
					Sleep(100);
				}
				retryCount--;
			}

			if (!isDevicePortExist) {
				ApplicationLauncher.logger.debug("setSerialCommV2 : Serial port not found2 : " + SerialPort_ID);
				dutResponse.setResponseData(SerialPort_ID + "-" + DeleteMeConstant.PORT_NOT_FOUND);
				return dutResponse;
			}

			if (!SerialPortObj.isDeviceConnected()) {
				ApplicationLauncher.logger.debug("setSerialCommV2 : test1A");
				SerialPortObj.assignSerialPort(SerialPort_ID);
			}
			// SerialPortObj.setFlowControlMode();
			SerialPortObj.connect(SerialPort_ID);
			ApplicationLauncher.logger.debug("setSerialCommV2 : test2");
			if (SerialPortObj.isDeviceConnected()) {
				ApplicationLauncher.logger.debug("setSerialCommV2 : test3");
				// if (SerialPortObj.initIOStream() == true){
				ApplicationLauncher.logger.debug("setSerialCommV2 : test4");
				SerialPortObj.serialPortConfig(BaudRate);
				ApplicationLauncher.logger.debug("setSerialCommV2 : test5");
				// SerialPortObj.setPortDeviceMapping(SerialPort_ID);
				ApplicationLauncher.logger.info("setSerialCommV2: PortDeviceMapping:"
						+ SerialPortObj.getPortDeviceMapping() + ":" + ReadHexFormat);
				ApplicationLauncher.logger.debug("setSerialCommV2 : test6");
				SerialPortObj.initListener();
				ApplicationLauncher.logger.debug("setSerialCommV2 : test7");
				// SerialPortObj.setDataReadFormatInHex(ReadHexFormat);
				// SerialPortObj.setDataReadFormatInHex(false);
				ApplicationLauncher.logger.debug("setSerialCommV2 : test8");
				status = true;
				dutResponse.setStatus(true);
				return dutResponse;
				// }
			} else {
				dutResponse.setResponseData(SerialPort_ID + "-" + DeleteMeConstant.NO_ACCESS);
				dutResponse.setStatus(false);
				ApplicationLauncher.logger.debug("setSerialCommV2 : device com port not connected: " + SerialPort_ID);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("SetSerialComm: Exception: " + e.getMessage());

		}

		return dutResponse;
	}

	public void dutSendCommand(String payLoadInHex, int timeDelayInMilliSec) {
		ApplicationLauncher.logger.debug("dutSendCommand V2: Entry");
		// `ApplicationLauncher.logger.debug("dutSendCommand V2: payLoadInHex: " +
		// payLoadInHex);
		// String Data =
		// GUIUtils.StringToHex(ConstantPrimaryVICI_Meter.VI_CMD_START)+GUIUtils.StringToHex(SelectedPhase)+CommandVI_PayLoad+GUIUtils.StringToHex(ConstantPrimaryVICI_Meter.VI_CMD_TERMINATOR);
		// String Data =
		// ConstantPrimaryVICI_Meter.VI_CMD_START+GUIUtils.StringToHex(SelectedPhase)+CommandVI_PayLoad+ConstantPrimaryVICI_Meter.VI_CMD_TERMINATOR;
		// String Data =
		// "";//SerialMessageHV_Src.getCmd_HV_Start(CommandVI_PayLoad,SelectedPhase);
		// WriteToSerialCommLDU(Data);
		writeStringToDut(payLoadInHex, timeDelayInMilliSec);

	}

	public void dutSendCommandV2_1(String payLoadInHex, int timeDelayInMilliSec, String sourceThread) {
		ApplicationLauncher.logger.debug("dutSendCommand V2_1: Entry");
		// `ApplicationLauncher.logger.debug("dutSendCommand V2: payLoadInHex: " +
		// payLoadInHex);
		// String Data =
		// GUIUtils.StringToHex(ConstantPrimaryVICI_Meter.VI_CMD_START)+GUIUtils.StringToHex(SelectedPhase)+CommandVI_PayLoad+GUIUtils.StringToHex(ConstantPrimaryVICI_Meter.VI_CMD_TERMINATOR);
		// String Data =
		// ConstantPrimaryVICI_Meter.VI_CMD_START+GUIUtils.StringToHex(SelectedPhase)+CommandVI_PayLoad+ConstantPrimaryVICI_Meter.VI_CMD_TERMINATOR;
		// String Data =
		// "";//SerialMessageHV_Src.getCmd_HV_Start(CommandVI_PayLoad,SelectedPhase);
		// WriteToSerialCommLDU(Data);
		writeStringToDutV2(payLoadInHex, timeDelayInMilliSec, sourceThread);

	}

	public void writeStringToDut(String Data, int timeDelayInMilliSec) {
		// ApplicationLauncher.logger.debug("writeStringToDut V2 :DataHex:"+Data);
		try {
			/*
			 * if(ProcalFeatureEnable.MAINTENANCE_MODE_ENABLED) {
			 * if(MaintenanceModeExecController.isSerialDisplayProcess()) {
			 * MaintenanceModeExecController.serialDataDisplayUpdate("hvc-Tx-Hex:<"+Data+">"
			 * );
			 * MaintenanceModeExecController.serialDataDisplayUpdate("hvc-Tx-Str:<"+GuiUtils
			 * .HexToString(Data)+">");
			 * }
			 * }
			 */

			if (timeDelayInMilliSec != 0) {
				/*
				 * String eachDataInHex = "";
				 * for(int i = 0; i < (Data.length()-1); i+=2){
				 * //ApplicationLauncher.logger.debug("lscsLDU_SendCeigSettingMethod : index :"
				 * + i +": " + String.valueOf(Data.charAt(i)));
				 * //SerialPortObj.writeStringMsgToPort(String.valueOf(Data.charAt(i)));
				 * eachDataInHex = Data.substring(i,i+2);
				 * //ApplicationLauncher.logger.
				 * debug("writeHexToSerialPowerSource :eachDataInHex:"+eachDataInHex);
				 * commRefStandard.writeStringMsgToPortInHex(eachDataInHex);
				 * Sleep(timeDelayInMilliSec);
				 * //Sleep(10);
				 * //Sleep(50);
				 * //Sleep(80);
				 * //Sleep(1000);//worked good for 10mA and 25mA calibration
				 * 
				 * //Sleep(80);
				 * 
				 * 
				 * 
				 * }
				 */
			} else {
				// commRefStandard.writeStringMsgToPortInHex(Data);
				ApplicationLauncher.logger.debug("writeStringToDut : Data: " + Data);
				// String myStr = GuiUtils.hexToAscii(Data);
				// ConstantPowerSourceBofa.ER_STARTS_WITH
				// String myStr =
				// GuiUtils.hexToAscii(ConstantPowerSourceBofa.ER_STARTS_WITH)+"Test22"+
				// GuiUtils.hexToAscii(ConstantPowerSourceBofa.END_BYTE)
				// ;//GuiUtils.hexToAscii(Data);
				// String myStr =
				// GuiUtils.hexToString(ConstantPowerSourceBofa.ER_STARTS_WITH)+"Test22"+
				// GuiUtils.hexToAscii(ConstantPowerSourceBofa.END_BYTE)
				// ;//GuiUtils.hexToAscii(Data);
				String myStr = GuiUtils.hexToAsciiV2(Data);
				// byte[] myStr = GuiUtils.hexToString(Data);

				// ApplicationLauncher.logger.debug("writeStringToDut : myStr: " + myStr);
				// ApplicationLauncher.logger.debug("writeStringToDut : asciiToHex: " +
				// GuiUtils.asciiToHex(myStr));

				// ApplicationLauncher.logger.debug("writeStringToDut : StringToHex: " +
				// GuiUtils.StringToHex(myStr));
				// ApplicationLauncher.logger.debug("writeStringToDut : StringToHex: " +
				// GuiUtils.StringToHex(myStr));

				commDut.writeStringMsgToPortV1(myStr);
				// commPowerSource.writeStringMsgToPortV2(myStr);
			}
		} catch (Exception e) {
			ApplicationLauncher.logger.error("writeStringToDut V2 :Exception :" + e.getMessage());
		}
		// Sleep(200);

	}

	public void writeStringToDutV2(String Data, int timeDelayInMilliSec, String sourceThread) {
		// ApplicationLauncher.logger.debug("writeStringToDut V2 :DataHex:"+Data);
		try {

			if (timeDelayInMilliSec != 0) {
				/*
				 * String eachDataInHex = "";
				 * for(int i = 0; i < (Data.length()-1); i+=2){
				 * //ApplicationLauncher.logger.debug("lscsLDU_SendCeigSettingMethod : index :"
				 * + i +": " + String.valueOf(Data.charAt(i)));
				 * //SerialPortObj.writeStringMsgToPort(String.valueOf(Data.charAt(i)));
				 * eachDataInHex = Data.substring(i,i+2);
				 * //ApplicationLauncher.logger.
				 * debug("writeHexToSerialPowerSource :eachDataInHex:"+eachDataInHex);
				 * commRefStandard.writeStringMsgToPortInHex(eachDataInHex);
				 * Sleep(timeDelayInMilliSec);
				 * //Sleep(10);
				 * //Sleep(50);
				 * //Sleep(80);
				 * //Sleep(1000);//worked good for 10mA and 25mA calibration
				 * 
				 * //Sleep(80);
				 * 
				 * 
				 * 
				 * }
				 */
			} else {
				// ApplicationLauncher.logger.debug("writeStringToDutV2 : threadTimeStamp: " +
				// threadTimeStamp);
				ApplicationLauncher.logger.debug("writeStringToDutV2 : Data<" + sourceThread + ">: " + Data);
				String myStr = GuiUtils.hexToAsciiV2(Data);

				// ApplicationLauncher.logger.debug("writeStringToDut : myStr: " + myStr);
				// ApplicationLauncher.logger.debug("writeStringToDut : asciiToHex: " +
				// GuiUtils.asciiToHex(myStr));

				commDut.writeStringMsgToPortV1(myStr);
				// commPowerSource.writeStringMsgToPortV2(myStr);
			}
		} catch (Exception e) {
			ApplicationLauncher.logger.error("writeStringToDutV2-1 :Exception :" + e.getMessage());
		}
		// Sleep(200);

	}

	public void dutSetExpectedData(String ExpectedResponse) {
		ApplicationLauncher.logger.debug("dutSetExpectedDataV2 :Entry");
		CommunicatorV2 SerialPortObj = commDut;

		SerialPortObj.setExpectedLength(ExpectedResponse.length());
		SerialPortObj.setExpectedResult(ExpectedResponse);
		// rxMsgQ_Dut.getSerialPortObj().setExpectedResult(ExpectedResponse);
		// ApplicationLauncher.logger.debug("dutSetExpectedDataV2
		// :commDut.getDeviceType(): " +commDut.getDeviceType());
		// ApplicationLauncher.logger.debug("dutSetExpectedDataV2 :getExpectedResult: "
		// +rxMsgQ_Dut.getSerialPortObj().getExpectedResult());

		// SerialPortObj.setExpectedError1Result(ExpectedError1Data);
		// SerialPortObj.setExpectedError2Result(ExpectedError2Data);
		// ApplicationLauncher.logger.debug("dutSetExpectedDataV2:
		// setExpectedResult:"+SerialPortObj.getExpectedResult());
		// ApplicationLauncher.logger.debug("dutSetExpectedDataV2:
		// setExpectedLength:"+SerialPortObj.getExpectedLength());

		// SerialDataMsgHV_Manager serialDataMsgManager = new
		// SerialDataMsgHV_Manager(SerialPortObj);
		// serialDataManager.SerialResponseTimerStart(30);
		// SerialPortObj = null;//garbagecollector
		// return serialDataMsgManager;
	}

	public void dutSetExpectedError1Data(String ExpectedError1Response) {
		// ApplicationLauncher.logger.debug("dutSetExpectedError1Data :Entry");
		CommunicatorV2 SerialPortObj = commDut;

		// SerialPortObj.setExpectedLength(ExpectedError1Response.length());
		SerialPortObj.setExpectedError1Result(ExpectedError1Response);
		// SerialPortObj.setExpectedError1Result(ExpectedError1Data);
		// SerialPortObj.setExpectedError2Result(ExpectedError2Data);
		// ApplicationLauncher.logger.debug("dutSetExpectedError1Data:
		// getExpectedError1Result:"+SerialPortObj.getExpectedError1Result());
		// ApplicationLauncher.logger.debug("HVCI_SetExpectedError1Data:
		// setExpectedLength:"+SerialPortObj.getExpectedLength());

		// SerialDataMsgHV_Manager serialDataMsgManager = new
		// SerialDataMsgHV_Manager(SerialPortObj);
		// serialDataManager.SerialResponseTimerStart(30);
		// SerialPortObj = null;//garbagecollector
		// return serialDataMsgManager;
	}

	public void dutSetExpectedError2Data(String ExpectedError2Response) {
		// ApplicationLauncher.logger.debug("dutSetExpectedError2Data :Entry");
		CommunicatorV2 SerialPortObj = commDut;

		// SerialPortObj.setExpectedLength(ExpectedError1Response.length());
		SerialPortObj.setExpectedError2Result(ExpectedError2Response);
		// SerialPortObj.setExpectedError1Result(ExpectedError1Data);
		// SerialPortObj.setExpectedError2Result(ExpectedError2Data);
		// ApplicationLauncher.logger.debug("dutSetExpectedError2Data:
		// getExpectedError2Result:"+SerialPortObj.getExpectedError2Result());
		// ApplicationLauncher.logger.debug("HVCI_SetExpectedError1Data:
		// setExpectedLength:"+SerialPortObj.getExpectedLength());

		// SerialDataMsgHV_Manager serialDataMsgManager = new
		// SerialDataMsgHV_Manager(SerialPortObj);
		// serialDataManager.SerialResponseTimerStart(30);
		// SerialPortObj = null;//garbagecollector
		// return serialDataMsgManager;
	}

	public void dutResetResponseFlag() {
		rxPhysical_Dut.ClearRxPhysicalData();
		rxPhysical_Dut.clearLastDisplayedReadRxPhysicalData();
		rxMsgQ_Dut.setExpectedResponseRecieved(false);
		rxMsgQ_Dut.setErrorResponseRecieved(false);
		rxMsgQ_Dut.setUnknownResponseRecieved(false);
		rxMsgQ_Dut.setResponseRecieved(false);
		rxMsgQ_Dut.clearMsgQueue();

		// rxPhysical_HVCI.ClearRxPhysicalData();

	}

	public Map<String, Object> dutSendCommandProcess(String payLoadInHex, int timeDelayInMilliSec, String expectedData,
			boolean isResponseExpected, String rxMessageTerminatorInHex, String sourceThread) {
		ApplicationLauncher.logger.debug("dutSendCommandProcess <" + sourceThread + ">:Entry");
		boolean status = false;
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		String responseData = DeleteMeConstant.NO_RESPONSE;
		int retryCount = 3;// 10//ConstantHV_Src.HV_READ_RESPONSE_RETRY_COUNT+10;//1;

		String ExpectedError1Data = "";// ConstantPrimaryVICI_Meter.VI_CMD_ERROR_RESPONSE_HDR;//+ConstantPrimaryVICI_Meter.VI_ER_TERMINATOR;

		String ExpectedError2Data = "";
		dutSetExpectedData(expectedData);// SerialMessageHV_Src.getER_HV_StartAck(VoltageValue));
		dutSetExpectedError1Data(ExpectedError1Data);// SerialMessageHV_Src.ER_HV_ERROR_HEADER);
		dutSetExpectedError2Data(ExpectedError2Data);
		ApplicationLauncher.logger.debug("dutSendCommandProcess :commDut.getExpectedResult(): "
				+ rxMsgQ_Dut.getSerialPortObj().getExpectedResult());

		// dutSetRxMessageTerminator(terminatorInHex);
		dutSetRxMessageTerminator(GuiUtils.hexToAsciiV2(rxMessageTerminatorInHex));
		// dutSetRxMessageTerminator(rxMessageTerminatorInHex);
		clearLastMessage();
		// rxPhysical_Dut.setMessageTerminatorInHex(GUIUtils.StringToHex("K"));
		dutResetResponseFlag();
		if (isDutSerialStatusConnected()) {
			dutSendCommandV2_1(payLoadInHex, timeDelayInMilliSec, sourceThread);

			if (isResponseExpected) {
				while (retryCount > 0 && responseData.equals(DeleteMeConstant.NO_RESPONSE)) {
					retryCount--;

					if (rxMsgQ_Dut.IsResponseReceived()) {
						if (rxMsgQ_Dut.isExpectedResponseReceived()) {
							ApplicationLauncher.logger
									.debug("dutSendCommandProcess <" + sourceThread + ">:Ack Response Success");

							String CurrentReadData = new String(rxMsgQ_Dut.getLastReadMessage());
							responseData = CurrentReadData;
							rxMsgQ_Dut.removeProcessedMsgFromQueue();
							status = true;
						} else if (rxMsgQ_Dut.isExpectedErrorResponseReceived()) {
							ApplicationLauncher.logger
									.debug("dutSendCommandProcess <" + sourceThread + ">:Ack Response Error");
							String CurrentReadData = new String(rxMsgQ_Dut.getLastReadMessage());

							responseData = CurrentReadData;
							rxMsgQ_Dut.removeProcessedMsgFromQueue();

						} else if (rxMsgQ_Dut.isUnknownResponseRecieved()) {

							String CurrentReadData = new String(rxMsgQ_Dut.getLastReadMessage());
							responseData = CurrentReadData;
							rxMsgQ_Dut.removeProcessedMsgFromQueue();
							if (responseData != null) { // Gopi2
								ApplicationLauncher.logger.debug("dutSendCommandProcess :Unexpected Message<"
										+ sourceThread + ">: dropping the message:" + responseData);
								// ApplicationLauncher.logger.debug("dutSendCommandProcess :Unexpected Message:
								// dropping the message:"+GuiUtils.HexToString(responseData));
							}

						}
					} else {
						ApplicationLauncher.logger
								.debug("dutSendCommandProcess <" + sourceThread + ">: Ack No Data Received");
					}
					if ((!payLoadInHex.equals(ConstantPowerSourceBofa.CMD_STOP_VOLT_CURRENT_IN_HEX)) &&
							(!payLoadInHex.equals(ConstantPowerSourceBofa.CMD_STOP_CURRENT_IN_HEX))) {
						if (BayUtils.isUserAborted()) {
							retryCount = 0;
							responseData = DeleteMeConstant.ERROR_RESPONSE;
							ApplicationLauncher.logger
									.debug("dutSendCommandProcess <" + sourceThread + ">: user aborted - detected");
						} else if (!isDutSerialStatusConnected()) {
							retryCount = 0;
							responseData = DeleteMeConstant.ERROR_RESPONSE;
							ApplicationLauncher.logger
									.debug("dutSendCommandProcess <" + sourceThread + ">: serial port - disconnected");

						}
					}
				}
			}
		} else {
			ApplicationLauncher.logger
					.debug("dutSendCommandProcess-X <" + sourceThread + "> serial port not connected or disconnected");
		}
		if (rxMsgQ_Dut.getLastReadMessage() == null) {
			ApplicationLauncher.logger.debug("dutSendCommandProcess <" + sourceThread + ">: last read null");
		} else {
			// ApplicationLauncher.logger.info("dutSendCommandProcess: last
			// read:"+rxMsgQ_Dut.getLastReadMessage());
		}
		responseReturn.put("status", status);
		responseReturn.put("result", responseData);
		ApplicationLauncher.logger.debug("dutSendCommandProcess <" + sourceThread + ">:Exit");

		// return responseStatus;

		return responseReturn;
	}

	public Map<String, Object> dutSendCommandProcessV2(String payLoadInHex, int timeDelayInMilliSec,
			String expectedDataInHex,
			boolean isResponseExpected, String rxMessageTerminatorInHex, String sourceThread) {
		ApplicationLauncher.logger.debug("dutSendCommandProcess V2 <" + sourceThread + ">:Entry");
		boolean status = false;
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		String responseData = DeleteMeConstant.NO_RESPONSE;
		int retryCount = 3;// 10//ConstantHV_Src.HV_READ_RESPONSE_RETRY_COUNT+10;//1;

		String ExpectedError1Data = "";// ConstantPrimaryVICI_Meter.VI_CMD_ERROR_RESPONSE_HDR;//+ConstantPrimaryVICI_Meter.VI_ER_TERMINATOR;

		String ExpectedError2Data = "";
		dutSetExpectedData(GuiUtils.hexToAsciiV2(expectedDataInHex));// SerialMessageHV_Src.getER_HV_StartAck(VoltageValue));
		dutSetExpectedError1Data(ExpectedError1Data);// SerialMessageHV_Src.ER_HV_ERROR_HEADER);
		dutSetExpectedError2Data(ExpectedError2Data);
		ApplicationLauncher.logger.debug("dutSendCommandProcess :commDut.getExpectedResult(): "
				+ rxMsgQ_Dut.getSerialPortObj().getExpectedResult());

		// dutSetRxMessageTerminator(terminatorInHex);
		dutSetRxMessageTerminator(GuiUtils.hexToAsciiV2(rxMessageTerminatorInHex));
		// dutSetRxMessageTerminator(rxMessageTerminatorInHex);
		clearLastMessage();
		// rxPhysical_Dut.setMessageTerminatorInHex(GUIUtils.StringToHex("K"));
		dutResetResponseFlag();
		if (isDutSerialStatusConnected()) {
			dutSendCommandV2_1(payLoadInHex, timeDelayInMilliSec, sourceThread);

			if (isResponseExpected) {
				while (retryCount > 0 && responseData.equals(DeleteMeConstant.NO_RESPONSE)) {
					retryCount--;
					ApplicationLauncher.logger.debug("dutSendCommandProcess V2: Hit0");
					ApplicationLauncher.logger.debug("dutSendCommandProcess V2: IsResponseReceived:"
							+ rxMsgQ_Dut.IsResponseReceived() + " : " + sourceThread);
					ApplicationLauncher.logger.debug("dutSendCommandProcess V2: isExpectedResponseReceived:"
							+ rxMsgQ_Dut.isExpectedResponseReceived() + " : " + sourceThread);
					if (rxMsgQ_Dut.IsResponseReceived()) {
						ApplicationLauncher.logger.debug("dutSendCommandProcess V2: Hit1");
						if (rxMsgQ_Dut.isExpectedResponseReceived()) {
							ApplicationLauncher.logger.debug("dutSendCommandProcess V2: Hit2");
							ApplicationLauncher.logger
									.debug("dutSendCommandProcess V2 <" + sourceThread + ">:Ack Response Success");

							String CurrentReadData = new String(rxMsgQ_Dut.getLastReadMessage());
							responseData = CurrentReadData;
							rxMsgQ_Dut.removeProcessedMsgFromQueue();
							status = true;
						} else if (rxMsgQ_Dut.isExpectedErrorResponseReceived()) {
							ApplicationLauncher.logger.debug("dutSendCommandProcess V2: Hit3");
							ApplicationLauncher.logger
									.debug("dutSendCommandProcess V2 <" + sourceThread + ">:Ack Response Error");
							String CurrentReadData = new String(rxMsgQ_Dut.getLastReadMessage());

							responseData = CurrentReadData;
							rxMsgQ_Dut.removeProcessedMsgFromQueue();

						} else if (rxMsgQ_Dut.isUnknownResponseRecieved()) {

							ApplicationLauncher.logger.debug("dutSendCommandProcess V2: Hit4");
							String CurrentReadData = new String(rxMsgQ_Dut.getLastReadMessage());
							responseData = CurrentReadData;
							rxMsgQ_Dut.removeProcessedMsgFromQueue();
							if (responseData != null) { // Gopi2
								ApplicationLauncher.logger.debug("dutSendCommandProcess V2 :Unexpected Message<"
										+ sourceThread + ">: dropping the message:" + responseData);
								// ApplicationLauncher.logger.debug("dutSendCommandProcess :Unexpected Message:
								// dropping the message:"+GuiUtils.HexToString(responseData));
							}

						}
					} else {

						ApplicationLauncher.logger
								.debug("dutSendCommandProcess V2 <" + sourceThread + ">: Ack No Data Received");
					}
					if (parentManager.isUserAborted()) {
						// responseData = DeleteMeConstant.NO_RESPONSE;
						retryCount = 0;
						ApplicationLauncher.logger
								.debug("dutSendCommandProcess V2 <" + sourceThread + ">: user aborted ");
					} else {
						Sleep(200);
					}

					/*
					 * if ( (
					 * !payLoadInHex.equals(ConstantPowerSourceBofa.CMD_STOP_VOLT_CURRENT_IN_HEX))
					 * &&
					 * ( !payLoadInHex.equals(ConstantPowerSourceBofa.CMD_STOP_CURRENT_IN_HEX)) ){
					 * if(BayUtils.isUserAborted()){
					 * retryCount = 0;
					 * responseData = DeleteMeConstant.ERROR_RESPONSE;
					 * ApplicationLauncher.logger.debug("dutSendCommandProcess V2 <"+sourceThread
					 * +">: user aborted - detected");
					 * }else if(!isDutSerialStatusConnected()){
					 * retryCount = 0;
					 * responseData = DeleteMeConstant.ERROR_RESPONSE;
					 * ApplicationLauncher.logger.debug("dutSendCommandProcess V2 <"+sourceThread
					 * +">: serial port - disconnected");
					 * 
					 * }
					 * }
					 */
				}
				ApplicationLauncher.logger.debug("dutSendCommandProcess V2: Hit5");
			} else {
				responseData = DeleteMeConstant.SUCCESS_RESPONSE;
				status = true;
				// dutResponse.setResponseData(responseStatus);
				// dutResponse.setStatus(true);
				// Sleep(200);
			}
		} else {
			ApplicationLauncher.logger.debug(
					"dutSendCommandProcess V2-Y <" + sourceThread + "> serial port not connected or disconnected");
		}
		if (rxMsgQ_Dut.getLastReadMessage() == null) {
			ApplicationLauncher.logger.debug("dutSendCommandProcess V2 <" + sourceThread + ">: last read null");
		} else {
			// ApplicationLauncher.logger.info("dutSendCommandProcess: last
			// read:"+rxMsgQ_Dut.getLastReadMessage());
		}
		responseReturn.put("status", status);
		responseReturn.put("result", responseData);
		ApplicationLauncher.logger.debug("dutSendCommandProcess V2 <" + sourceThread + ">:Exit");

		// return responseStatus;

		return responseReturn;
	}

	public void dutSetRxMessageTerminator(String terminatorInHex) {

		rxPhysical_Dut.setResponseMessageTerminator(terminatorInHex);
	}

	public void clearLastMessage() {
		rxMsgQ_Dut.clearLastMessage();
	}

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep2 :InterruptedException:" + e.getMessage());
		}

	}

	public SerialRxMessageQ_V2 getRxMsgQ_Dut() {
		return rxMsgQ_Dut;
	}

	public void setRxMsgQ_Dut(SerialRxMessageQ_V2 rxMsgQ_Dut) {
		this.rxMsgQ_Dut = rxMsgQ_Dut;
	}

	public boolean isDutSerialStatusConnected() {
		return DutSerialStatusConnected;
	}

	public void setDutSerialStatusConnected(boolean DutSerialStatus) {
		// ApplicationLauncher.logger.debug("setDutSerialStatusConnected:"+
		// DutSerialStatus);
		this.DutSerialStatusConnected = DutSerialStatus;

	}

	/*
	 * public HashMap getScannedSerialPortMap() {
	 * return scannedSerialPortMap;
	 * }
	 * 
	 * public void setScannedSerialPortMap(HashMap scannedSerialPortMap) {
	 * this.scannedSerialPortMap = scannedSerialPortMap;
	 * }
	 */

}
