package com.tasnetwork.calibration.energymeter.serial.portmanagerV2;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdKre;
import com.tasnetwork.calibration.energymeter.constant.DeleteMeConstant;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.deployment.DisplayInstantMetricsController;
import com.tasnetwork.calibration.energymeter.deployment.MaintenanceModeExecController;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.device.SerialDataRefStdKre;
import com.tasnetwork.calibration.energymeter.message.RefStdKreMessage;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

public class SerialPortManagerRefStd_V2 {

	DeviceDataManagerController DisplayDataObj =  new DeviceDataManagerController();
	
	public static CommunicatorV2 commRefStandard= null;
	
	public SerialTxMessageQ_V2 txMsgQ_RefStd = new SerialTxMessageQ_V2(commRefStandard);
	
	public SerialRxMessageQ_V2 rxMsgQ_RefStd = new SerialRxMessageQ_V2(commRefStandard);
	public SerialRxPhysical_V2 rxPhysical_RefStd = new SerialRxPhysical_V2(commRefStandard,rxMsgQ_RefStd,false,"RefStd",DeleteMeConstant.HV_ER_TERMINATOR,"");
	//public SerialRxPhysical rxPhysical_PwrSrc = new SerialRxPhysical(commPowerSrc,rxMsgQ_PwrSrc,false,"PwrSrc",DeleteMeConstant.HV_ER_TERMINATOR,"");
	
	public static boolean bInitOccured = false;
	public static boolean refStdSerialStatusConnected = false;
	
	public SerialPortManagerRefStd_V2(){
		if(!bInitOccured){
			InitSerialCommPort();
			bInitOccured= true;
			//SetBNC_OutputPortData();
			//rxMsgHV_Manager = new SerialDataMsgHV_Manager(commHVCI);
			//rxPhysicalHVCI = new SerialRxPhysical(commHVCI,rxMsgHV_Manager);
		}

	}
	
	public void InitSerialCommPort(){
		createObjects();
		//commPowerSrc.searchForPorts();  
		//commHVCI.searchForPorts();  
		scanForSerialCommPort();
	}
	
	private void createObjects()
	{
		ApplicationLauncher.logger.debug("SerialPortManagerRefStd: createObjects :Entry");
		//commPowerSrc = new Communicator(ConstantApp.SERIAL_PORT_POWER_SOURCE);
		//commRefStandard = new Communicator(ConstantApp.SERIAL_PORT_REF_STD);
/*		commLDU = new Communicator(ConstantApp.SERIAL_PORT_LDU);
		
		commBridge = new Communicator(ConstantProGEN_App.SERIAL_PORT_BRIDGE);
		commLVD = new Communicator(ConstantProGEN_App.SERIAL_PORT_PT_VOLTAGE_DIVIDER);
		commPT_Burden = new Communicator(ConstantProGEN_App.SERIAL_PORT_PT_BURDEN);
		commCT_Burden = new Communicator(ConstantProGEN_App.SERIAL_PORT_CT_BURDEN);*/
		commRefStandard = new CommunicatorV2(ConstantApp.SERIAL_PORT_REF_STD);//ConstantProGEN_App.SERIAL_PORT_HVCI);
		txMsgQ_RefStd = new SerialTxMessageQ_V2(commRefStandard);
		
		rxMsgQ_RefStd = new SerialRxMessageQ_V2(commRefStandard);
		rxPhysical_RefStd = new SerialRxPhysical_V2(commRefStandard,rxMsgQ_RefStd,false,"RefStdV2",DeleteMeConstant.HV_ER_TERMINATOR,"");
		
		ApplicationLauncher.logger.debug("createObjects :Exit");
		//commVICI  = new Communicator(ConstantPrimaryVICI_Meter.SERIAL_PORT_VICI);
	}
	
	
	
	
	public void scanForSerialCommPort(){

		//commPowerSrc.searchForPorts();  
		commRefStandard.searchForPorts();  

	}
	
	public void startSerialRxPhysical_RefStd() {
		

		rxPhysical_RefStd.SerialRxPhysicalTimerStart();
		//rxPhysicalObj.setReadRxPhysicalFlag(true);
	}
	
	public void enableSerialRxPhysical_RefStdMonitor() {
		rxPhysical_RefStd.setReadRxPhysicalFlag(true);
		
	}
	
	public void disableSerialRxPhysical_RefStdMonitor() {
		rxPhysical_RefStd.setReadRxPhysicalFlag(false);
		

	}
	
	public void disconnectRefStd(){
		ApplicationLauncher.logger.debug("disconnectRefStd V2 :Entry");
		if(refStdSerialStatusConnected){
			ApplicationLauncher.logger.debug("disconnectRefStd :refStdSerialStatusConnected:" + refStdSerialStatusConnected);
			disableSerialRxPhysical_RefStdMonitor();
			DisplayDataObj.setRefStdReadDataFlag(false);  
			//Sleep(10);
			//Sleep(1500);jjjj
			disconnectRefStdSerialComm();
			refStdSerialStatusConnected=false;
			
		}else {
			ApplicationLauncher.logger.debug("disconnectRefStd V2 : Path2");
		}
	}
	
	public void disconnectRefStdSerialComm(){
		ApplicationLauncher.logger.debug("disconnectRefStdSerialComm V2 :Entry");
		commRefStandard.disconnect();
	}
	
	
	
	public void disconnectRefStdSerialCommIfConnected(){
		ApplicationLauncher.logger.debug("disconnectRefStdSerialCommIfConnected V2 :Entry");
		try {
		if(commRefStandard.isDeviceConnected()){
			ApplicationLauncher.logger.debug("disconnectRefStdSerialCommIfConnected :Entry2:");
			disableSerialRxPhysical_RefStdMonitor();
			DisplayDataObj.setRefStdReadDataFlag(false);  
			Sleep(10);
			disconnectRefStdSerialComm();
			refStdSerialStatusConnected=false;
		}
		}catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("disconnectRefStdSerialCommIfConnected : Exception: " + e.getMessage());
		}
	}
	
	public boolean refStdComInitV2(String InputComm,String BaudRate) {
		ApplicationLauncher.logger.debug("refStdComInitV2 Invoked:");
		ApplicationLauncher.logger.debug("CommInput: " + InputComm);
		ApplicationLauncher.logger.debug("BaudRate: " + BaudRate);
		try {
			refStdSerialStatusConnected = refStdCommSetting(InputComm,BaudRate);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("refStdComInit :UnsupportedEncodingException:"+ e.getMessage());
		}


		return refStdSerialStatusConnected;
	}
	
	public boolean refStdCommSetting(String CommInput, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("refStdCommSetting :Entry");
		//InitSerialCommPort();
		ApplicationLauncher.logger.debug("refStdComm: " +commRefStandard );
		ApplicationLauncher.logger.debug("CommInput: " + CommInput);
		ApplicationLauncher.logger.debug("BaudRate: " + BaudRate);
		boolean status = setSerialCommV2(commRefStandard,CommInput,Integer.valueOf(BaudRate),true);
		
		if (status){
			//commRefStandard.SetFlowControlMode();
			//commRefStandard.SetFlowControlModeV2();
		} else {

			ApplicationLauncher.logger.info("refStdCommSetting:"+CommInput+" access failed");
		}
		return status;


	} 
	
	public boolean setSerialCommV2(CommunicatorV2 SerialPortObj, String SerialPort_ID, Integer BaudRate,Boolean ReadHexFormat){
		ApplicationLauncher.logger.debug("setSerialCommV2 :Entry");
		boolean status=false;
		try {
			ApplicationLauncher.logger.debug("setSerialCommV2 : test1");
			if(!SerialPortObj.isDevicePortExist(SerialPort_ID)) {
				ApplicationLauncher.logger.debug("setSerialCommV2 : Serial port not found : " + SerialPort_ID);
				return status;
			}
			if(!SerialPortObj.isDeviceConnected()){
				ApplicationLauncher.logger.debug("setSerialCommV2 : test1A");
				SerialPortObj.assignSerialPort(SerialPort_ID); 
			}
			SerialPortObj.connect(SerialPort_ID);
			ApplicationLauncher.logger.debug("setSerialCommV2 : test2");
			if(SerialPortObj.isDeviceConnected()==true){
				ApplicationLauncher.logger.debug("setSerialCommV2 : test3");
				//if (SerialPortObj.initIOStream() == true){
					ApplicationLauncher.logger.debug("setSerialCommV2 : test4");
					SerialPortObj.serialPortConfig(BaudRate);
					ApplicationLauncher.logger.debug("setSerialCommV2 : test5");
					//SerialPortObj.setPortDeviceMapping(SerialPort_ID);
					ApplicationLauncher.logger.info("setSerialCommV2: PortDeviceMapping:"+ SerialPortObj.getPortDeviceMapping()+":"+ReadHexFormat);
					ApplicationLauncher.logger.debug("setSerialCommV2 : test6");
					SerialPortObj.initListener();
					ApplicationLauncher.logger.debug("setSerialCommV2 : test7");
					//SerialPortObj.setDataReadFormatInHex(ReadHexFormat);
					//SerialPortObj.setDataReadFormatInHex(false);
					ApplicationLauncher.logger.debug("setSerialCommV2 : test8");
					status = true;
					return status;
				//}
			}else {
				ApplicationLauncher.logger.debug("setSerialCommV2 : device com port " + SerialPort_ID + " not connected");
			}
		}catch(Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("SetSerialComm: Exception: " + e.getMessage());
			

		}
		
		return status;
	}
	
	public void refStdSendCommand(String payLoadInHex,int timeDelayInMilliSec){
		ApplicationLauncher.logger.debug("refStdSendCommand V2: Entry");
		//`ApplicationLauncher.logger.debug("refStdSendCommand V2: payLoadInHex: " + payLoadInHex);
		//String Data = GUIUtils.StringToHex(ConstantPrimaryVICI_Meter.VI_CMD_START)+GUIUtils.StringToHex(SelectedPhase)+CommandVI_PayLoad+GUIUtils.StringToHex(ConstantPrimaryVICI_Meter.VI_CMD_TERMINATOR);
		//String Data = ConstantPrimaryVICI_Meter.VI_CMD_START+GUIUtils.StringToHex(SelectedPhase)+CommandVI_PayLoad+ConstantPrimaryVICI_Meter.VI_CMD_TERMINATOR;
		//String Data = "";//SerialMessageHV_Src.getCmd_HV_Start(CommandVI_PayLoad,SelectedPhase);
		//WriteToSerialCommLDU(Data);
		writeStringToRefStd(payLoadInHex,timeDelayInMilliSec);

	}
	
	public void writeStringToRefStd(String Data,int timeDelayInMilliSec){
		//ApplicationLauncher.logger.debug("writeStringToRefStd V2 :DataHex:"+Data);
		try{
/*			if(ProcalFeatureEnable.MAINTENANCE_MODE_ENABLED) {
				if(MaintenanceModeExecController.isSerialDisplayProcess()) {				
					MaintenanceModeExecController.serialDataDisplayUpdate("hvc-Tx-Hex:<"+Data+">");
					MaintenanceModeExecController.serialDataDisplayUpdate("hvc-Tx-Str:<"+GuiUtils.HexToString(Data)+">");
				}
			}*/
			
			if(timeDelayInMilliSec!=0){
/*				String eachDataInHex = "";
				for(int i = 0; i < (Data.length()-1); i+=2){
					//ApplicationLauncher.logger.debug("lscsLDU_SendCeigSettingMethod : index :" + i +": " + String.valueOf(Data.charAt(i)));
					//SerialPortObj.writeStringMsgToPort(String.valueOf(Data.charAt(i)));
					eachDataInHex = Data.substring(i,i+2);
					//ApplicationLauncher.logger.debug("writeHexToSerialPowerSource :eachDataInHex:"+eachDataInHex);
					commRefStandard.writeStringMsgToPortInHex(eachDataInHex);
					Sleep(timeDelayInMilliSec);
					//Sleep(10);
					//Sleep(50);
					//Sleep(80);
					//Sleep(1000);//worked good for 10mA and 25mA calibration
					//Sleep(500);
					//Sleep(80);
	
	
	
				}*/
			}else{
				//commRefStandard.writeStringMsgToPortInHex(Data);
				//ApplicationLauncher.logger.debug("writeStringToRefStd : Data: "  + Data);
				String myStr = GuiUtils.hexToAscii(Data);
				ApplicationLauncher.logger.debug("writeStringToRefStd : Entry: " + myStr);
				commRefStandard.writeStringMsgToPortV1(myStr);
			}
		}catch(Exception e){
			ApplicationLauncher.logger.error("writeStringToRefStd V2 :Exception :" + e.getMessage());
		}
		//Sleep(200);

	}
	
	public void refStdSetExpectedData(String ExpectedResponse){
		ApplicationLauncher.logger.debug("refStdSetExpectedData :Entry");
		CommunicatorV2 SerialPortObj =commRefStandard;

		
		SerialPortObj.setExpectedLength(ExpectedResponse.length());
		SerialPortObj.setExpectedResult(ExpectedResponse);
		//SerialPortObj.setExpectedError1Result(ExpectedError1Data);
		//SerialPortObj.setExpectedError2Result(ExpectedError2Data);
		//ApplicationLauncher.logger.debug("refStdSetExpectedData: setExpectedResult:"+SerialPortObj.getExpectedResult());
		//ApplicationLauncher.logger.debug("refStdSetExpectedData: setExpectedLength:"+SerialPortObj.getExpectedLength());

		//SerialDataMsgHV_Manager serialDataMsgManager = new SerialDataMsgHV_Manager(SerialPortObj);
		//serialDataManager.SerialResponseTimerStart(30);
		//SerialPortObj = null;//garbagecollector
		//return serialDataMsgManager;
	}
	
	public void refStdSetExpectedError1Data(String ExpectedError1Response){
		//ApplicationLauncher.logger.debug("refStdSetExpectedError1Data :Entry");
		CommunicatorV2 SerialPortObj =commRefStandard;
		
		//SerialPortObj.setExpectedLength(ExpectedError1Response.length());
		SerialPortObj.setExpectedError1Result(ExpectedError1Response);
		//SerialPortObj.setExpectedError1Result(ExpectedError1Data);
		//SerialPortObj.setExpectedError2Result(ExpectedError2Data);
		//ApplicationLauncher.logger.debug("refStdSetExpectedError1Data: getExpectedError1Result:"+SerialPortObj.getExpectedError1Result());
		//ApplicationLauncher.logger.debug("HVCI_SetExpectedError1Data: setExpectedLength:"+SerialPortObj.getExpectedLength());

		//SerialDataMsgHV_Manager serialDataMsgManager = new SerialDataMsgHV_Manager(SerialPortObj);
		//serialDataManager.SerialResponseTimerStart(30);
		//SerialPortObj = null;//garbagecollector
		//return serialDataMsgManager;
	}
	
	public void refStdSetExpectedError2Data(String ExpectedError2Response){
		//ApplicationLauncher.logger.debug("refStdSetExpectedError2Data :Entry");
		CommunicatorV2 SerialPortObj =commRefStandard;
		
		//SerialPortObj.setExpectedLength(ExpectedError1Response.length());
		SerialPortObj.setExpectedError2Result(ExpectedError2Response);
		//SerialPortObj.setExpectedError1Result(ExpectedError1Data);
		//SerialPortObj.setExpectedError2Result(ExpectedError2Data);
		//ApplicationLauncher.logger.debug("refStdSetExpectedError2Data: getExpectedError2Result:"+SerialPortObj.getExpectedError2Result());
		//ApplicationLauncher.logger.debug("HVCI_SetExpectedError1Data: setExpectedLength:"+SerialPortObj.getExpectedLength());

		//SerialDataMsgHV_Manager serialDataMsgManager = new SerialDataMsgHV_Manager(SerialPortObj);
		//serialDataManager.SerialResponseTimerStart(30);
		//SerialPortObj = null;//garbagecollector
		//return serialDataMsgManager;
	}
	
	public void refStdResetResponseFlag() {
		rxPhysical_RefStd.ClearRxPhysicalData();
		rxPhysical_RefStd.clearLastDisplayedReadRxPhysicalData();
		rxMsgQ_RefStd.setExpectedResponseRecieved(false);
		rxMsgQ_RefStd.setErrorResponseRecieved(false);
		rxMsgQ_RefStd.setUnknownResponseRecieved(false);
		rxMsgQ_RefStd.setResponseRecieved(false);
		rxMsgQ_RefStd.clearMsgQueue();
		
		//rxPhysical_HVCI.ClearRxPhysicalData();

	}
	
	public Map<String,Object> refStdSendCommandProcess(String payLoadInHex, int timeDelayInMilliSec, String expectedDataInHex,boolean isResponseExpected, String rxMessageTerminatorInHex){
		ApplicationLauncher.logger.debug("refStdSendCommandProcess :Entry");
		boolean status=false;
		Map<String,Object> responseReturn = new HashMap<String,Object>();
		//responseReturn.put("status", false);
		//responseReturn.put("result", "");
		String responseData=DeleteMeConstant.NO_RESPONSE;
		int retryCount= 5;//10//ConstantHV_Src.HV_READ_RESPONSE_RETRY_COUNT+10;//1;

		//String ExpectedDataInHex = GUIUtils.StringToHex("K");//GUIUtils.StringToHex( VoltageValue)+ConstantPrimaryVICI_Meter.VI_ER_TERMINATOR;//getER_HV_StartAck
		String ExpectedError1Data = "";//ConstantPrimaryVICI_Meter.VI_CMD_ERROR_RESPONSE_HDR;//+ConstantPrimaryVICI_Meter.VI_ER_TERMINATOR;
		
		String ExpectedError2Data="";
		//String terminatorInHex = GuiUtils.StringToHex("K");
		//HVCI_SetExpectedData(SerialMessageHV.getER_HV_StartVoltageValue(VoltageValue));
		refStdSetExpectedData(expectedDataInHex);//SerialMessageHV_Src.getER_HV_StartAck(VoltageValue));
		refStdSetExpectedError1Data(ExpectedError1Data);//SerialMessageHV_Src.ER_HV_ERROR_HEADER); 
		refStdSetExpectedError2Data(ExpectedError2Data);
		//powerSourceSetRxMessageTerminator(terminatorInHex);
		refStdSetRxMessageTerminator(rxMessageTerminatorInHex);
		clearLastMessage();
		//rxPhysical_PwrSrc.setMessageTerminatorInHex(GUIUtils.StringToHex("K"));
		refStdResetResponseFlag();

		refStdSendCommand(payLoadInHex,timeDelayInMilliSec);
		if(isResponseExpected){
			while ((retryCount>0) && (responseData.equals(DeleteMeConstant.NO_RESPONSE) && 
					(!ProjectExecutionController.getUserAbortedFlag()))){
				retryCount--;
				//VI_SendStartCommand(CommandVI_PayLoad,SelectedPhase);
				//ApplicationLauncher.logger.debug("powerSourceSendCommandProcess :Hit1");
				//int TimeOutInSec= 60;vjhvjgv
				//SerialDataLDU lduData = VICI_ReadDataWithErrorResponse(ExpectedData.length(),ExpectedData,ExpectedError1Data,ExpectedError2Data,TimeOutInSec);
				//ApplicationLauncher.logger.debug("powerSourceSendCommandProcess :Hit2");
				if(rxMsgQ_RefStd.IsResponseReceived()) {
					if(rxMsgQ_RefStd.isExpectedResponseReceived()){
						ApplicationLauncher.logger.debug("refStdSendCommandProcessV2 :Ack Response Success");
						//String CurrentLDU_Data =lduData.getLDU_ReadSerialData();
						//ApplicationLauncher.logger.info("powerSourceSendCommandProcess: VICI Received Data1:"+CurrentLDU_Data);
						//StripLDU_SerialData(lduData.getReceivedLength());
						//responseStatus=DeleteMeConstant.SUCCESS_RESPONSE;
						String CurrentReadData =rxMsgQ_RefStd.getLastReadMessage();
						responseData=CurrentReadData;
						rxMsgQ_RefStd.removeProcessedMsgFromQueue();
						status = true;
					}else if(rxMsgQ_RefStd.isExpectedErrorResponseReceived()){
						ApplicationLauncher.logger.debug("refStdSendCommandProcess :Ack Response Error");
						String CurrentReadData =rxMsgQ_RefStd.getLastReadMessage();
						//ApplicationLauncher.logger.info("powerSourceSendCommandProcess: ErrorResponse Received:"+CurrentLDU_Data);
						//ApplicationLauncher.logger.info("powerSourceSendCommandProcess: ExpectedError1Data:"+ExpectedError1Data);
						//StripLDU_SerialData(CurrentLDU_Data.length());
						
						responseData=CurrentReadData;
						rxMsgQ_RefStd.removeProcessedMsgFromQueue();
		
					}else if(rxMsgQ_RefStd.isUnknownResponseRecieved()){
						
						
						String CurrentReadData =rxMsgQ_RefStd.getLastReadMessage();
						responseData=CurrentReadData;
						rxMsgQ_RefStd.removeProcessedMsgFromQueue();
						if(responseData!=null) { //Gopi2
							ApplicationLauncher.logger.debug("refStdSendCommandProcess :Unexpected Message: dropping the message:"+responseData);
							//ApplicationLauncher.logger.debug("refStdSendCommandProcess :Unexpected Message: dropping the message:"+GuiUtils.HexToString(responseData));
						}
						
						
					}
				}
				else{
					ApplicationLauncher.logger.info("refStdSendCommandProcess: Ack No Data Received");
				}
			}
		}
		if(rxMsgQ_RefStd.getLastReadMessage()==null) {
			ApplicationLauncher.logger.info("refStdSendCommandProcess: last read null");
		}else {
			//ApplicationLauncher.logger.info("refStdSendCommandProcess: last read:"+rxMsgQ_RefStd.getLastReadMessage());
		}
		ApplicationLauncher.logger.debug("refStdSendCommandProcess :Exit");
		responseReturn.put("status", status);
		responseReturn.put("result", responseData);
		//return responseStatus;
		
		return responseReturn;
	}

	
	public void refStdSetRxMessageTerminator(String terminatorInHex) {
		// TODO Auto-generated method stub
		
		rxPhysical_RefStd.setResponseMessageTerminator(terminatorInHex);
	}
	
	
	public void clearLastMessage() {
		rxMsgQ_RefStd.clearLastMessage();
	}
	

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep2 :InterruptedException:"+ e.getMessage());
		}

	}
	
		
}

