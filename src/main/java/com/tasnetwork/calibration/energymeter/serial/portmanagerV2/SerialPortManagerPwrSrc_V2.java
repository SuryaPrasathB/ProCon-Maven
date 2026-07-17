package com.tasnetwork.calibration.energymeter.serial.portmanagerV2;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceBofa;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceLscs;
import com.tasnetwork.calibration.energymeter.constant.DeleteMeConstant;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

public class SerialPortManagerPwrSrc_V2 {

	DeviceDataManagerController DisplayDataObj =  new DeviceDataManagerController();
	
	public static CommunicatorV2 commPowerSource= null;
	
	public SerialTxMessageQ_V2 txMsgQ_PwrSrc = new SerialTxMessageQ_V2(commPowerSource);
	
	public SerialRxMessageQ_V2 rxMsgQ_PwrSrc = new SerialRxMessageQ_V2(commPowerSource);
	public SerialRxPhysical_V2 rxPhysical_PwrSrc = new SerialRxPhysical_V2(commPowerSource,rxMsgQ_PwrSrc,false,"PwrSrcV2",DeleteMeConstant.HV_ER_TERMINATOR,"");
	//public SerialRxPhysical rxPhysical_PwrSrc = new SerialRxPhysical(commPowerSrc,rxMsgQ_PwrSrc,false,"PwrSrc",DeleteMeConstant.HV_ER_TERMINATOR,"");
	
	public static boolean bInitOccured = false;
	public volatile static boolean pwrSrcSerialStatusConnected = false;
	
	public SerialPortManagerPwrSrc_V2(){
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
		ApplicationLauncher.logger.debug("SerialPortManagerPwrSrc_V2: createObjects :Entry");
		//commPowerSrc = new Communicator(ConstantApp.SERIAL_PORT_POWER_SOURCE);
		//commRefStandard = new Communicator(ConstantApp.SERIAL_PORT_REF_STD);
/*		commLDU = new Communicator(ConstantApp.SERIAL_PORT_LDU);
		
		commBridge = new Communicator(ConstantProGEN_App.SERIAL_PORT_BRIDGE);
		commLVD = new Communicator(ConstantProGEN_App.SERIAL_PORT_PT_VOLTAGE_DIVIDER);
		commPT_Burden = new Communicator(ConstantProGEN_App.SERIAL_PORT_PT_BURDEN);
		commCT_Burden = new Communicator(ConstantProGEN_App.SERIAL_PORT_CT_BURDEN);*/
		commPowerSource = new CommunicatorV2(ConstantPowerSourceLscs.SERIAL_PORT_POWER_SOURCE);//ConstantApp.SERIAL_PORT_REF_STD);//ConstantProGEN_App.SERIAL_PORT_HVCI);
		txMsgQ_PwrSrc = new SerialTxMessageQ_V2(commPowerSource);
		
		rxMsgQ_PwrSrc = new SerialRxMessageQ_V2(commPowerSource);
		rxPhysical_PwrSrc = new SerialRxPhysical_V2(commPowerSource,rxMsgQ_PwrSrc,false,"PwrSrcV2",DeleteMeConstant.HV_ER_TERMINATOR,"");
		
		ApplicationLauncher.logger.debug("SerialPortManagerPwrSrc_V2: createObjects :Exit");
		//commVICI  = new Communicator(ConstantPrimaryVICI_Meter.SERIAL_PORT_VICI);
	}
	
	
	
	
	public void scanForSerialCommPort(){

		//commPowerSrc.searchForPorts();  
		commPowerSource.searchForPorts();  

	}
	
	public void startSerialRxPhysical_PwrSrc() {
		

		rxPhysical_PwrSrc.SerialRxPhysicalTimerStart();
		//rxPhysicalObj.setReadRxPhysicalFlag(true);
	}
	
	public void enableSerialRxPhysical_PwrSrcMonitor() {
		rxPhysical_PwrSrc.setReadRxPhysicalFlag(true);
		
	}
	
	public void disableSerialRxPhysical_PwrSrcMonitor() {
		rxPhysical_PwrSrc.setReadRxPhysicalFlag(false);
		

	}
	
	public void disconnectPwrSrc(){
		ApplicationLauncher.logger.debug("disconnectPwrSrc V2 :Entry");
		if(isPwrSrcSerialStatusConnected()){
			ApplicationLauncher.logger.debug("disconnectPwrSrc :PwrSrcSerialStatusConnected:" + isPwrSrcSerialStatusConnected());
			disableSerialRxPhysical_PwrSrcMonitor();
			//DisplayDataObj.setPwrSrcReadDataFlag(false);  
			Sleep(10);
			disconnectPwrSrcSerialComm();
			setPwrSrcSerialStatusConnected(false);
			
		}else {
			ApplicationLauncher.logger.debug("disconnectPwrSrc V2 : Path2");
		}
	}
	
	public void disconnectPwrSrcSerialComm(){
		ApplicationLauncher.logger.debug("disconnectPwrSrcSerialComm V2 :Entry");
		commPowerSource.disconnect();
	}
	
	
	
	public void disconnectPwrSrcSerialCommIfConnected(){
		ApplicationLauncher.logger.debug("disconnectPwrSrcSerialCommIfConnected V2 :Entry");
		try {
		if(commPowerSource.isDeviceConnected()){
			ApplicationLauncher.logger.debug("disconnectPwrSrcSerialCommIfConnected :Entry2:");
			disableSerialRxPhysical_PwrSrcMonitor();
			//DisplayDataObj.setPwrSrcReadDataFlag(false);  
			Sleep(10);
			disconnectPwrSrcSerialComm();
			setPwrSrcSerialStatusConnected(false);
		}
		}catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("disconnectPwrSrcSerialCommIfConnected : Exception: " + e.getMessage());
		}
	}
	
	public boolean powerSourceComInitV2(String InputComm,String BaudRate) {
		ApplicationLauncher.logger.debug("powerSourceComInitV2 Invoked:");
		ApplicationLauncher.logger.debug("CommInput: " + InputComm);
		ApplicationLauncher.logger.debug("BaudRate: " + BaudRate);
		try {
			setPwrSrcSerialStatusConnected(powerSourceCommSetting(InputComm,BaudRate));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("powerSourceComInit :UnsupportedEncodingException:"+ e.getMessage());
		}


		return isPwrSrcSerialStatusConnected();
	}
	
	public boolean powerSourceCommSetting(String CommInput, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("powerSourceCommSetting :Entry");
		//InitSerialCommPort();
		ApplicationLauncher.logger.debug("powerSourceComm: " +commPowerSource );
		ApplicationLauncher.logger.debug("CommInput: " + CommInput);
		ApplicationLauncher.logger.debug("BaudRate: " + BaudRate);
		boolean status = setSerialCommV2(commPowerSource,CommInput,Integer.valueOf(BaudRate),true);
		
		if (status){
			//commPowerSource.setFlowControlMode();
			//commRefStandard.SetFlowControlModeV2();
		} else {

			ApplicationLauncher.logger.info("powerSourceCommSetting:"+CommInput+" access failed");
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
			//SerialPortObj.setFlowControlMode();
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
	
	public void powerSourceSendCommand(String payLoadInHex,int timeDelayInMilliSec){
		ApplicationLauncher.logger.debug("powerSourceSendCommand V2: Entry");
		//`ApplicationLauncher.logger.debug("powerSourceSendCommand V2: payLoadInHex: " + payLoadInHex);
		//String Data = GUIUtils.StringToHex(ConstantPrimaryVICI_Meter.VI_CMD_START)+GUIUtils.StringToHex(SelectedPhase)+CommandVI_PayLoad+GUIUtils.StringToHex(ConstantPrimaryVICI_Meter.VI_CMD_TERMINATOR);
		//String Data = ConstantPrimaryVICI_Meter.VI_CMD_START+GUIUtils.StringToHex(SelectedPhase)+CommandVI_PayLoad+ConstantPrimaryVICI_Meter.VI_CMD_TERMINATOR;
		//String Data = "";//SerialMessageHV_Src.getCmd_HV_Start(CommandVI_PayLoad,SelectedPhase);
		//WriteToSerialCommLDU(Data);
		writeStringToPwrSrc(payLoadInHex,timeDelayInMilliSec);

	}
	
	public void powerSourceSendCommandV2_1(String payLoadInHex,int timeDelayInMilliSec,String sourceThread){
		ApplicationLauncher.logger.debug("powerSourceSendCommand V2_1: Entry");
		//`ApplicationLauncher.logger.debug("powerSourceSendCommand V2: payLoadInHex: " + payLoadInHex);
		//String Data = GUIUtils.StringToHex(ConstantPrimaryVICI_Meter.VI_CMD_START)+GUIUtils.StringToHex(SelectedPhase)+CommandVI_PayLoad+GUIUtils.StringToHex(ConstantPrimaryVICI_Meter.VI_CMD_TERMINATOR);
		//String Data = ConstantPrimaryVICI_Meter.VI_CMD_START+GUIUtils.StringToHex(SelectedPhase)+CommandVI_PayLoad+ConstantPrimaryVICI_Meter.VI_CMD_TERMINATOR;
		//String Data = "";//SerialMessageHV_Src.getCmd_HV_Start(CommandVI_PayLoad,SelectedPhase);
		//WriteToSerialCommLDU(Data);
		writeStringToPwrSrcV2(payLoadInHex,timeDelayInMilliSec, sourceThread);

	}
	
	
	
	
	public void writeStringToPwrSrc(String Data,int timeDelayInMilliSec){
		//ApplicationLauncher.logger.debug("writeStringToPwrSrc V2 :DataHex:"+Data);
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
				ApplicationLauncher.logger.debug("writeStringToPwrSrc : Data: "  + Data);
				//String myStr = GuiUtils.hexToAscii(Data);
				//ConstantPowerSourceBofa.ER_STARTS_WITH
				//String myStr = GuiUtils.hexToAscii(ConstantPowerSourceBofa.ER_STARTS_WITH)+"Test22"+ GuiUtils.hexToAscii(ConstantPowerSourceBofa.END_BYTE) ;//GuiUtils.hexToAscii(Data);
				//String myStr = GuiUtils.hexToString(ConstantPowerSourceBofa.ER_STARTS_WITH)+"Test22"+ GuiUtils.hexToAscii(ConstantPowerSourceBofa.END_BYTE) ;//GuiUtils.hexToAscii(Data);
				String myStr = GuiUtils.hexToAsciiV2(Data);
				//byte[] myStr = GuiUtils.hexToString(Data);
				
				
				
				//ApplicationLauncher.logger.debug("writeStringToPwrSrc : myStr: "  + myStr);
				//ApplicationLauncher.logger.debug("writeStringToPwrSrc : asciiToHex: "  + GuiUtils.asciiToHex(myStr));
				
				
				
				
				//ApplicationLauncher.logger.debug("writeStringToPwrSrc : StringToHex: "  + GuiUtils.StringToHex(myStr));
				//ApplicationLauncher.logger.debug("writeStringToPwrSrc : StringToHex: "  + GuiUtils.StringToHex(myStr));
				
				commPowerSource.writeStringMsgToPortV1(myStr);
				//commPowerSource.writeStringMsgToPortV2(myStr);
			}
		}catch(Exception e){
			ApplicationLauncher.logger.error("writeStringToPwrSrc V2 :Exception :" + e.getMessage());
		}
		//Sleep(200);

	}
	
	
	public void writeStringToPwrSrcV2(String Data,int timeDelayInMilliSec, String sourceThread ){
		//ApplicationLauncher.logger.debug("writeStringToPwrSrc V2 :DataHex:"+Data);
		try{

			
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
				//ApplicationLauncher.logger.debug("writeStringToPwrSrcV2 : threadTimeStamp: "  + threadTimeStamp);
				ApplicationLauncher.logger.debug("writeStringToPwrSrcV2 : Data<"+sourceThread +">: "  + Data);
				String myStr = GuiUtils.hexToAsciiV2(Data);
				
				//ApplicationLauncher.logger.debug("writeStringToPwrSrc : myStr: "  + myStr);
				//ApplicationLauncher.logger.debug("writeStringToPwrSrc : asciiToHex: "  + GuiUtils.asciiToHex(myStr));
				
				
				commPowerSource.writeStringMsgToPortV1(myStr);
				//commPowerSource.writeStringMsgToPortV2(myStr);
			}
		}catch(Exception e){
			ApplicationLauncher.logger.error("writeStringToPwrSrcV2-1 :Exception :" + e.getMessage());
		}
		//Sleep(200);

	}
	
	public void powerSourceSetExpectedData(String ExpectedResponse){
		ApplicationLauncher.logger.debug("powerSourceSetExpectedDataV2 :Entry");
		CommunicatorV2 SerialPortObj =commPowerSource;

		
		SerialPortObj.setExpectedLength(ExpectedResponse.length());
		SerialPortObj.setExpectedResult(ExpectedResponse);
		//SerialPortObj.setExpectedError1Result(ExpectedError1Data);
		//SerialPortObj.setExpectedError2Result(ExpectedError2Data);
		//ApplicationLauncher.logger.debug("powerSourceSetExpectedDataV2: setExpectedResult:"+SerialPortObj.getExpectedResult());
		//ApplicationLauncher.logger.debug("powerSourceSetExpectedDataV2: setExpectedLength:"+SerialPortObj.getExpectedLength());

		//SerialDataMsgHV_Manager serialDataMsgManager = new SerialDataMsgHV_Manager(SerialPortObj);
		//serialDataManager.SerialResponseTimerStart(30);
		//SerialPortObj = null;//garbagecollector
		//return serialDataMsgManager;
	}
	
	public void powerSourceSetExpectedError1Data(String ExpectedError1Response){
		//ApplicationLauncher.logger.debug("powerSourceSetExpectedError1Data :Entry");
		CommunicatorV2 SerialPortObj =commPowerSource;
		
		//SerialPortObj.setExpectedLength(ExpectedError1Response.length());
		SerialPortObj.setExpectedError1Result(ExpectedError1Response);
		//SerialPortObj.setExpectedError1Result(ExpectedError1Data);
		//SerialPortObj.setExpectedError2Result(ExpectedError2Data);
		//ApplicationLauncher.logger.debug("powerSourceSetExpectedError1Data: getExpectedError1Result:"+SerialPortObj.getExpectedError1Result());
		//ApplicationLauncher.logger.debug("HVCI_SetExpectedError1Data: setExpectedLength:"+SerialPortObj.getExpectedLength());

		//SerialDataMsgHV_Manager serialDataMsgManager = new SerialDataMsgHV_Manager(SerialPortObj);
		//serialDataManager.SerialResponseTimerStart(30);
		//SerialPortObj = null;//garbagecollector
		//return serialDataMsgManager;
	}
	
	public void powerSourceSetExpectedError2Data(String ExpectedError2Response){
		//ApplicationLauncher.logger.debug("powerSourceSetExpectedError2Data :Entry");
		CommunicatorV2 SerialPortObj =commPowerSource;
		
		//SerialPortObj.setExpectedLength(ExpectedError1Response.length());
		SerialPortObj.setExpectedError2Result(ExpectedError2Response);
		//SerialPortObj.setExpectedError1Result(ExpectedError1Data);
		//SerialPortObj.setExpectedError2Result(ExpectedError2Data);
		//ApplicationLauncher.logger.debug("powerSourceSetExpectedError2Data: getExpectedError2Result:"+SerialPortObj.getExpectedError2Result());
		//ApplicationLauncher.logger.debug("HVCI_SetExpectedError1Data: setExpectedLength:"+SerialPortObj.getExpectedLength());

		//SerialDataMsgHV_Manager serialDataMsgManager = new SerialDataMsgHV_Manager(SerialPortObj);
		//serialDataManager.SerialResponseTimerStart(30);
		//SerialPortObj = null;//garbagecollector
		//return serialDataMsgManager;
	}
	
	public void powerSourceResetResponseFlag() {
		rxPhysical_PwrSrc.ClearRxPhysicalData();
		rxPhysical_PwrSrc.clearLastDisplayedReadRxPhysicalData();
		rxMsgQ_PwrSrc.setExpectedResponseRecieved(false);
		rxMsgQ_PwrSrc.setErrorResponseRecieved(false);
		rxMsgQ_PwrSrc.setUnknownResponseRecieved(false);
		rxMsgQ_PwrSrc.setResponseRecieved(false);
		rxMsgQ_PwrSrc.clearMsgQueue();
		
		//rxPhysical_HVCI.ClearRxPhysicalData();

	}
	

	public Map<String,Object> powerSourceSendCommandProcess(String payLoadInHex, int timeDelayInMilliSec, String expectedData,
			boolean isResponseExpected, String rxMessageTerminatorInHex,String sourceThread){
		ApplicationLauncher.logger.debug("powerSourceSendCommandProcess V2 <"+sourceThread +">:Entry");
		boolean status=false;
		Map<String,Object> responseReturn = new HashMap<String,Object>();
		String responseData=DeleteMeConstant.NO_RESPONSE;
		int retryCount= 5;//10//ConstantHV_Src.HV_READ_RESPONSE_RETRY_COUNT+10;//1;

		String ExpectedError1Data = "";//ConstantPrimaryVICI_Meter.VI_CMD_ERROR_RESPONSE_HDR;//+ConstantPrimaryVICI_Meter.VI_ER_TERMINATOR;
		
		String ExpectedError2Data="";
		powerSourceSetExpectedData(expectedData);//SerialMessageHV_Src.getER_HV_StartAck(VoltageValue));
		powerSourceSetExpectedError1Data(ExpectedError1Data);//SerialMessageHV_Src.ER_HV_ERROR_HEADER); 
		powerSourceSetExpectedError2Data(ExpectedError2Data);
		//powerSourceSetRxMessageTerminator(terminatorInHex);
		powerSourceSetRxMessageTerminator(rxMessageTerminatorInHex);
		clearLastMessage();
		//rxPhysical_PwrSrc.setMessageTerminatorInHex(GUIUtils.StringToHex("K"));
		powerSourceResetResponseFlag();
		if(isPwrSrcSerialStatusConnected()){
			powerSourceSendCommandV2_1(payLoadInHex,timeDelayInMilliSec,sourceThread);
			if(isResponseExpected){
				while (retryCount>0 && responseData.equals(DeleteMeConstant.NO_RESPONSE)){
					retryCount--;
	
					if(rxMsgQ_PwrSrc.IsResponseReceived()) {
						if(rxMsgQ_PwrSrc.isExpectedResponseReceived()){
							ApplicationLauncher.logger.debug("powerSourceSendCommandProcess V2 <"+sourceThread +">:Ack Response Success");
	
							String CurrentReadData = new String (rxMsgQ_PwrSrc.getLastReadMessage());
							responseData=CurrentReadData;
							rxMsgQ_PwrSrc.removeProcessedMsgFromQueue();
							status = true;
						}else if(rxMsgQ_PwrSrc.isExpectedErrorResponseReceived()){
							ApplicationLauncher.logger.debug("powerSourceSendCommandProcess V2 <"+sourceThread +">:Ack Response Error");
							String CurrentReadData =new String (rxMsgQ_PwrSrc.getLastReadMessage());
						
							responseData=CurrentReadData;
							rxMsgQ_PwrSrc.removeProcessedMsgFromQueue();
			
						}else if(rxMsgQ_PwrSrc.isUnknownResponseRecieved()){
							
							
							String CurrentReadData =new String (rxMsgQ_PwrSrc.getLastReadMessage());
							responseData=CurrentReadData;
							rxMsgQ_PwrSrc.removeProcessedMsgFromQueue();
							if(responseData!=null) { //Gopi2
								ApplicationLauncher.logger.debug("powerSourceSendCommandProcess V2 :Unexpected Message<"+sourceThread +">: dropping the message:"+responseData);
								//ApplicationLauncher.logger.debug("powerSourceSendCommandProcess :Unexpected Message: dropping the message:"+GuiUtils.HexToString(responseData));
							}
							
							
						}
					}
					else{
						ApplicationLauncher.logger.debug("powerSourceSendCommandProcess V2 <"+sourceThread +">: Ack No Data Received");
					}
					if ( ( !payLoadInHex.equals(ConstantPowerSourceBofa.CMD_STOP_VOLT_CURRENT_IN_HEX)) && 
					     ( !payLoadInHex.equals(ConstantPowerSourceBofa.CMD_STOP_CURRENT_IN_HEX)) ){
						if(ProjectExecutionController.getUserAbortedFlag()){
							retryCount = 0;
							responseData = DeleteMeConstant.ERROR_RESPONSE;
							ApplicationLauncher.logger.debug("powerSourceSendCommandProcess V2 <"+sourceThread +">: user aborted - detected");
						}else if(!isPwrSrcSerialStatusConnected()){
							retryCount = 0;
							responseData = DeleteMeConstant.ERROR_RESPONSE;
							ApplicationLauncher.logger.debug("powerSourceSendCommandProcess V2 <"+sourceThread +">: serial port - disconnected");
						
						}
					}
				}
			}
		}else{
			ApplicationLauncher.logger.debug("powerSourceSendCommandProcess V2 <"+sourceThread +"> serial port not connected or disconnected");
		}
		if(rxMsgQ_PwrSrc.getLastReadMessage()==null) {
			ApplicationLauncher.logger.debug("powerSourceSendCommandProcess V2 <"+sourceThread +">: last read null");
		}else {
			//ApplicationLauncher.logger.info("powerSourceSendCommandProcess: last read:"+rxMsgQ_PwrSrc.getLastReadMessage());
		}
		responseReturn.put("status", status);
		responseReturn.put("result", responseData);
		ApplicationLauncher.logger.debug("powerSourceSendCommandProcess V2 <"+sourceThread +">:Exit");
		
		//return responseStatus;
		
		return responseReturn;
	}

	
	public void powerSourceSetRxMessageTerminator(String terminatorInHex) {
		// TODO Auto-generated method stub
		
		rxPhysical_PwrSrc.setResponseMessageTerminator(terminatorInHex);
	}
	
	
	public void clearLastMessage() {
		rxMsgQ_PwrSrc.clearLastMessage();
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

	public SerialRxMessageQ_V2 getRxMsgQ_PwrSrc() {
		return rxMsgQ_PwrSrc;
	}

	public void setRxMsgQ_PwrSrc(SerialRxMessageQ_V2 rxMsgQ_PwrSrc) {
		this.rxMsgQ_PwrSrc = rxMsgQ_PwrSrc;
	}

	public static boolean isPwrSrcSerialStatusConnected() {
		return pwrSrcSerialStatusConnected;
	}

	public static void setPwrSrcSerialStatusConnected(boolean pwrSrcSerialStatus) {
		//ApplicationLauncher.logger.debug("setPwrSrcSerialStatusConnected:"+ pwrSrcSerialStatus);
		SerialPortManagerPwrSrc_V2.pwrSrcSerialStatusConnected = pwrSrcSerialStatus;
		
	}
	
		
}

