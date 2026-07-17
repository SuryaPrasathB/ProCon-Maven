package com.tasnetwork.calibration.energymeter.serial.portmanager;

import java.io.UnsupportedEncodingException;
import java.util.TimerTask;

import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceBofa;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceMte;
import com.tasnetwork.calibration.energymeter.constant.DeleteMeConstant;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.deployment.MaintenanceModeExecController;
import com.tasnetwork.calibration.energymeter.device.Communicator;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

public class SerialPortManagerPwrSrc {
	
	DeviceDataManagerController DisplayDataObj =  new DeviceDataManagerController();
	
	public static Communicator commPowerSrc= null;
	
	public SerialTxMessageQ txMsgQ_PwrSrc = new SerialTxMessageQ(commPowerSrc);
	
	public SerialRxMessageQ rxMsgQ_PwrSrc = new SerialRxMessageQ(commPowerSrc);
	public SerialRxPhysical rxPhysical_PwrSrc = new SerialRxPhysical(commPowerSrc,rxMsgQ_PwrSrc,false,"PwrSrc",DeleteMeConstant.HV_ER_TERMINATOR,"");
	//public SerialRxPhysical rxPhysical_PwrSrc = new SerialRxPhysical(commPowerSrc,rxMsgQ_PwrSrc,false,"PwrSrc",DeleteMeConstant.HV_ER_TERMINATOR,"");
	
	public static boolean bInitOccured = false;
	public static volatile boolean powerSourceSerialStatusConnected = false;
	
	public SerialPortManagerPwrSrc(){
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
		ScanForSerialCommPort();
	}
	
	private void createObjects()
	{
		ApplicationLauncher.logger.debug("SerialPortManagerPwrSrc: createObjects :Entry");
		//commPowerSrc = new Communicator(ConstantApp.SERIAL_PORT_POWER_SOURCE);
		//commRefStandard = new Communicator(ConstantApp.SERIAL_PORT_REF_STD);
/*		commLDU = new Communicator(ConstantApp.SERIAL_PORT_LDU);
		
		commBridge = new Communicator(ConstantProGEN_App.SERIAL_PORT_BRIDGE);
		commLVD = new Communicator(ConstantProGEN_App.SERIAL_PORT_PT_VOLTAGE_DIVIDER);
		commPT_Burden = new Communicator(ConstantProGEN_App.SERIAL_PORT_PT_BURDEN);
		commCT_Burden = new Communicator(ConstantProGEN_App.SERIAL_PORT_CT_BURDEN);*/
		commPowerSrc = new Communicator(ConstantPowerSourceMte.SERIAL_PORT_POWER_SOURCE);//ConstantProGEN_App.SERIAL_PORT_HVCI);
		ApplicationLauncher.logger.debug("createObjects :Exit");
		//commVICI  = new Communicator(ConstantPrimaryVICI_Meter.SERIAL_PORT_VICI);
	}
	
	public void ScanForSerialCommPort(){

		//commPowerSrc.searchForPorts();  
		commPowerSrc.searchForPorts();  

	}
	
	public void startSerialRxPhysical_PowerSource() {
		

		rxPhysical_PwrSrc.SerialRxPhysicalTimerStart();
		//rxPhysicalObj.setReadRxPhysicalFlag(true);
	}
	
	public void enableSerialRxPhysical_PowerSourceMonitor() {
		ApplicationLauncher.logger.debug("enableSerialRxPhysical_PowerSourceMonitor :Entry");
		rxPhysical_PwrSrc.setReadRxPhysicalFlag(true);
		
	}
	
	public void disableSerialRxPhysical_PowerSourceMonitor() {
		rxPhysical_PwrSrc.setReadRxPhysicalFlag(false);
		

	}
	
	public void disconnectPowerSource(){
		ApplicationLauncher.logger.debug("disconnectPowerSource :Entry");
		if(powerSourceSerialStatusConnected){
			ApplicationLauncher.logger.debug("disconnectPowerSource :Entry2");
			disableSerialRxPhysical_PowerSourceMonitor();
			DisplayDataObj.setPowerSrcReadData(false);
			Sleep(500);
			disconnectPowerSourceSerialComm();
			powerSourceSerialStatusConnected=false;
			
		}
	}
	
	public void disconnectPowerSourceSerialComm(){
		ApplicationLauncher.logger.debug("disconnectPowerSourceSerialComm :Entry");
		commPowerSrc.disconnect();
	}
	
	public boolean powerSourceComInit(String InputComm,String BaudRate) {
		ApplicationLauncher.logger.debug("powerSourceComInit Invoked:");
		ApplicationLauncher.logger.debug("CommInput: " + InputComm);
		ApplicationLauncher.logger.debug("BaudRate: " + BaudRate);
		try {
			powerSourceSerialStatusConnected = powerSourceCommSetting(InputComm,BaudRate);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("powerSourceComInit :UnsupportedEncodingException:"+ e.getMessage());
		}


		return powerSourceSerialStatusConnected;
	}
	
	public boolean powerSourceCommSetting(String CommInput, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("powerSourceCommSetting :Entry");
		//InitSerialCommPort();
		ApplicationLauncher.logger.debug("commPowerSrc: " +commPowerSrc );
		ApplicationLauncher.logger.debug("CommInput: " + CommInput);
		ApplicationLauncher.logger.debug("BaudRate: " + BaudRate);
		boolean status = SetSerialComm(commPowerSrc,CommInput,Integer.valueOf(BaudRate),true);
		if (status){
			commPowerSrc.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("powerSourceCommSetting:"+CommInput+" access failed");
		}
		return status;


	} 
	
	public boolean SetSerialComm(Communicator SerialPortObj, String SerialPort_ID, Integer BaudRate,Boolean ReadHexFormat){
		ApplicationLauncher.logger.debug("SetSerialComm :Entry");
		boolean status=false;
		try {
			ApplicationLauncher.logger.debug("SetSerialComm : test1");
			SerialPortObj.connect(SerialPort_ID);
			ApplicationLauncher.logger.debug("SetSerialComm : test2");
			if(SerialPortObj.getDeviceConnected()==true){
				ApplicationLauncher.logger.debug("SetSerialComm : test3");
				if (SerialPortObj.initIOStream() == true){
					ApplicationLauncher.logger.debug("SetSerialComm : test4");
					SerialPortObj.SerialPortConfig(BaudRate);
					ApplicationLauncher.logger.debug("SetSerialComm : test5");
					//SerialPortObj.setPortDeviceMapping(SerialPort_ID);
					ApplicationLauncher.logger.info("PwrSrc: PortDeviceMapping:"+ SerialPortObj.getPortDeviceMapping()+":"+ReadHexFormat);
					ApplicationLauncher.logger.debug("SetSerialComm : test6");
					SerialPortObj.initListener();
					ApplicationLauncher.logger.debug("SetSerialComm : test7");
					SerialPortObj.setDataReadFormatInHex(ReadHexFormat);
					ApplicationLauncher.logger.debug("SetSerialComm : test8");
					status = true;
					return status;
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("SetSerialComm: Exception: " + e.getMessage());
			

		}
		
		return status;
	}
	
	public void powerSourceSendCommand(String payLoadInHex,int timeDelayInMilliSec){

		//String Data = GUIUtils.StringToHex(ConstantPrimaryVICI_Meter.VI_CMD_START)+GUIUtils.StringToHex(SelectedPhase)+CommandVI_PayLoad+GUIUtils.StringToHex(ConstantPrimaryVICI_Meter.VI_CMD_TERMINATOR);
		//String Data = ConstantPrimaryVICI_Meter.VI_CMD_START+GUIUtils.StringToHex(SelectedPhase)+CommandVI_PayLoad+ConstantPrimaryVICI_Meter.VI_CMD_TERMINATOR;
		//String Data = "";//SerialMessageHV_Src.getCmd_HV_Start(CommandVI_PayLoad,SelectedPhase);
		//WriteToSerialCommLDU(Data);
		writeHexToSerialPowerSource(payLoadInHex,timeDelayInMilliSec);

	}
	
	public void writeHexToSerialPowerSource(String Data,int timeDelayInMilliSec){
		ApplicationLauncher.logger.debug("writeHexToSerialPowerSource :DataHex:"+Data);
		try{
			if(ProcalFeatureEnable.MAINTENANCE_MODE_ENABLED) {
				if(MaintenanceModeExecController.isSerialDisplayProcess()) {				
					MaintenanceModeExecController.serialDataDisplayUpdate("hvc-Tx-Hex:<"+Data+">");
					MaintenanceModeExecController.serialDataDisplayUpdate("hvc-Tx-Str:<"+GuiUtils.HexToString(Data)+">");
				}
			}
			
			if(timeDelayInMilliSec!=0){
				String eachDataInHex = "";
				for(int i = 0; i < (Data.length()-1); i+=2){
					//ApplicationLauncher.logger.debug("lscsLDU_SendCeigSettingMethod : index :" + i +": " + String.valueOf(Data.charAt(i)));
					//SerialPortObj.writeStringMsgToPort(String.valueOf(Data.charAt(i)));
					eachDataInHex = Data.substring(i,i+2);
					//ApplicationLauncher.logger.debug("writeHexToSerialPowerSource :eachDataInHex:"+eachDataInHex);
					commPowerSrc.writeStringMsgToPortInHex(eachDataInHex);
					Sleep(timeDelayInMilliSec);
					//Sleep(10);
					//Sleep(50);
					//Sleep(80);
					//Sleep(1000);//worked good for 10mA and 25mA calibration
					//Sleep(500);
					//Sleep(80);
	
	
	
				}
			}else{
				commPowerSrc.writeStringMsgToPortInHex(Data);
			}
		}catch(Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("writeHexToSerialPowerSource :Exception :" + e.getMessage());
		}
		//Sleep(200);

	}
	
	public void powerSourceSetExpectedData(String ExpectedResponse){
		//ApplicationLauncher.logger.debug("HVCI_SetExpectedData :Entry");
		Communicator SerialPortObj =commPowerSrc;

		
		SerialPortObj.setExpectedLength(ExpectedResponse.length());
		SerialPortObj.setExpectedResult(ExpectedResponse);
		//SerialPortObj.setExpectedError1Result(ExpectedError1Data);
		//SerialPortObj.setExpectedError2Result(ExpectedError2Data);
		//ApplicationLauncher.logger.debug("HVCI_SetExpectedData: setExpectedResult:"+SerialPortObj.getExpectedResult());
		//ApplicationLauncher.logger.debug("HVCI_SetExpectedData: setExpectedLength:"+SerialPortObj.getExpectedLength());

		//SerialDataMsgHV_Manager serialDataMsgManager = new SerialDataMsgHV_Manager(SerialPortObj);
		//serialDataManager.SerialResponseTimerStart(30);
		//SerialPortObj = null;//garbagecollector
		//return serialDataMsgManager;
	}
	
	public void powerSourceSetExpectedError1Data(String ExpectedError1Response){
		//ApplicationLauncher.logger.debug("HVCI_SetExpectedError1Data :Entry");
		Communicator SerialPortObj =commPowerSrc;
		
		//SerialPortObj.setExpectedLength(ExpectedError1Response.length());
		SerialPortObj.setExpectedError1Result(ExpectedError1Response);
		//SerialPortObj.setExpectedError1Result(ExpectedError1Data);
		//SerialPortObj.setExpectedError2Result(ExpectedError2Data);
		//ApplicationLauncher.logger.debug("HVCI_SetExpectedError1Data: getExpectedError1Result:"+SerialPortObj.getExpectedError1Result());
		//ApplicationLauncher.logger.debug("HVCI_SetExpectedError1Data: setExpectedLength:"+SerialPortObj.getExpectedLength());

		//SerialDataMsgHV_Manager serialDataMsgManager = new SerialDataMsgHV_Manager(SerialPortObj);
		//serialDataManager.SerialResponseTimerStart(30);
		//SerialPortObj = null;//garbagecollector
		//return serialDataMsgManager;
	}
	
	public void powerSourceSetExpectedError2Data(String ExpectedError2Response){
		//ApplicationLauncher.logger.debug("HVCI_SetExpectedError2Data :Entry");
		Communicator SerialPortObj =commPowerSrc;
		
		//SerialPortObj.setExpectedLength(ExpectedError1Response.length());
		SerialPortObj.setExpectedError2Result(ExpectedError2Response);
		//SerialPortObj.setExpectedError1Result(ExpectedError1Data);
		//SerialPortObj.setExpectedError2Result(ExpectedError2Data);
		//ApplicationLauncher.logger.debug("HVCI_SetExpectedError2Data: getExpectedError2Result:"+SerialPortObj.getExpectedError2Result());
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
	
	public String powerSourceSendCommandProcess(String payLoadInHex, int timeDelayInMilliSec, String expectedDataInHex,boolean isResponseExpected){
		ApplicationLauncher.logger.debug("powerSourceSendCommandProcess :Entry");
		//boolean status=false;
		String responseStatus=DeleteMeConstant.NO_RESPONSE;
		int retryCount= 5;//10//ConstantHV_Src.HV_READ_RESPONSE_RETRY_COUNT+10;//1;

		//String ExpectedDataInHex = GUIUtils.StringToHex("K");//GUIUtils.StringToHex( VoltageValue)+ConstantPrimaryVICI_Meter.VI_ER_TERMINATOR;//getER_HV_StartAck
		String ExpectedError1Data = "";//ConstantPrimaryVICI_Meter.VI_CMD_ERROR_RESPONSE_HDR;//+ConstantPrimaryVICI_Meter.VI_ER_TERMINATOR;
		
		String ExpectedError2Data="";
		String terminatorInHex = GuiUtils.StringToHex("K");
		//HVCI_SetExpectedData(SerialMessageHV.getER_HV_StartVoltageValue(VoltageValue));
		powerSourceSetExpectedData(expectedDataInHex);//SerialMessageHV_Src.getER_HV_StartAck(VoltageValue));
		powerSourceSetExpectedError1Data(ExpectedError1Data);//SerialMessageHV_Src.ER_HV_ERROR_HEADER); 
		powerSourceSetExpectedError2Data(ExpectedError2Data);
		powerSourceSetRxMessageTerminator(terminatorInHex);
		
		//rxPhysical_PwrSrc.setMessageTerminatorInHex(GUIUtils.StringToHex("K"));
		powerSourceResetResponseFlag();

		powerSourceSendCommand(payLoadInHex,timeDelayInMilliSec);
		if(isResponseExpected){
			while (retryCount>0 && responseStatus.equals(DeleteMeConstant.NO_RESPONSE)){
				retryCount--;
				//VI_SendStartCommand(CommandVI_PayLoad,SelectedPhase);
				//ApplicationLauncher.logger.debug("powerSourceSendCommandProcess :Hit1");
				//int TimeOutInSec= 60;vjhvjgv
				//SerialDataLDU lduData = VICI_ReadDataWithErrorResponse(ExpectedData.length(),ExpectedData,ExpectedError1Data,ExpectedError2Data,TimeOutInSec);
				//ApplicationLauncher.logger.debug("powerSourceSendCommandProcess :Hit2");
				if(rxMsgQ_PwrSrc.IsResponseReceived()) {
					if(rxMsgQ_PwrSrc.isExpectedResponseReceived()){
						ApplicationLauncher.logger.debug("powerSourceSendCommandProcess :Ack Response Success");
						//String CurrentLDU_Data =lduData.getLDU_ReadSerialData();
						//ApplicationLauncher.logger.info("powerSourceSendCommandProcess: VICI Received Data1:"+CurrentLDU_Data);
						//StripLDU_SerialData(lduData.getReceivedLength());
						responseStatus=DeleteMeConstant.SUCCESS_RESPONSE;
						rxMsgQ_PwrSrc.removeProcessedMsgFromQueue();
					}else if(rxMsgQ_PwrSrc.isExpectedErrorResponseReceived()){
						ApplicationLauncher.logger.debug("powerSourceSendCommandProcess :Ack Response Error");
						String CurrentReadData =rxMsgQ_PwrSrc.getLastReadMessage();
						//ApplicationLauncher.logger.info("powerSourceSendCommandProcess: ErrorResponse Received:"+CurrentLDU_Data);
						//ApplicationLauncher.logger.info("powerSourceSendCommandProcess: ExpectedError1Data:"+ExpectedError1Data);
						//StripLDU_SerialData(CurrentLDU_Data.length());
						
						responseStatus=CurrentReadData;
						rxMsgQ_PwrSrc.removeProcessedMsgFromQueue();
		
					}else if(rxMsgQ_PwrSrc.isUnknownResponseRecieved()){
						
						
						String CurrentReadData =rxMsgQ_PwrSrc.getLastReadMessage();
						responseStatus=CurrentReadData;
						rxMsgQ_PwrSrc.removeProcessedMsgFromQueue();
						if(responseStatus!=null) { //Gopi2
							ApplicationLauncher.logger.debug("powerSourceSendCommandProcess :Unexpected Message: dropping the message:"+responseStatus);
							ApplicationLauncher.logger.debug("powerSourceSendCommandProcess :Unexpected Message: dropping the message:"+GuiUtils.HexToString(responseStatus));
						}
						
						
					}
				}
				else{
					ApplicationLauncher.logger.info("powerSourceSendCommandProcess: Ack No Data Received");
				}
			}
		}
		if(rxMsgQ_PwrSrc.getLastReadMessage()==null) {
			ApplicationLauncher.logger.info("powerSourceSendCommandProcess: last read null");
		}else {
			ApplicationLauncher.logger.info("powerSourceSendCommandProcess: last read:"+rxMsgQ_PwrSrc.getLastReadMessage());
		}
		ApplicationLauncher.logger.debug("powerSourceSendCommandProcess :Exit");
		return responseStatus;
	}

	public String bofaPowerSourceSendCommandProcess(String payLoadInHex, int timeDelayInMilliSec, String expectedDataInHex,boolean isResponseExpected){
		ApplicationLauncher.logger.debug("bofaPowerSourceSendCommandProcess :Entry");
		//boolean status=false;

		String responseStatus=DeleteMeConstant.NO_RESPONSE;
		int retryCount= 5;//10//ConstantHV_Src.HV_READ_RESPONSE_RETRY_COUNT+10;//1;

		//String ExpectedDataInHex = GUIUtils.StringToHex("K");//GUIUtils.StringToHex( VoltageValue)+ConstantPrimaryVICI_Meter.VI_ER_TERMINATOR;//getER_HV_StartAck
		String ExpectedError1Data = "";//ConstantPrimaryVICI_Meter.VI_CMD_ERROR_RESPONSE_HDR;//+ConstantPrimaryVICI_Meter.VI_ER_TERMINATOR;
		
		String ExpectedError2Data="";
		String terminatorInHex = ConstantPowerSourceBofa.END_BYTE;//GuiUtils.StringToHex("K");
		//HVCI_SetExpectedData(SerialMessageHV.getER_HV_StartVoltageValue(VoltageValue));
		powerSourceSetExpectedData(expectedDataInHex);//SerialMessageHV_Src.getER_HV_StartAck(VoltageValue));
		powerSourceSetExpectedError1Data(ExpectedError1Data);//SerialMessageHV_Src.ER_HV_ERROR_HEADER); 
		powerSourceSetExpectedError2Data(ExpectedError2Data);
		powerSourceSetRxMessageTerminator(terminatorInHex);
		
		//rxPhysical_PwrSrc.setMessageTerminatorInHex(GUIUtils.StringToHex("K"));
		powerSourceResetResponseFlag();
		rxMsgQ_PwrSrc.clearLastReadMessage();
		powerSourceSendCommand(payLoadInHex,timeDelayInMilliSec);
		if(isResponseExpected){
			while (retryCount>0 && responseStatus.equals(DeleteMeConstant.NO_RESPONSE)){
				retryCount--;
				//VI_SendStartCommand(CommandVI_PayLoad,SelectedPhase);
				ApplicationLauncher.logger.debug("bofaPowerSourceSendCommandProcess :retryCount:" + retryCount);
				//int TimeOutInSec= 60;vjhvjgv
				//SerialDataLDU lduData = VICI_ReadDataWithErrorResponse(ExpectedData.length(),ExpectedData,ExpectedError1Data,ExpectedError2Data,TimeOutInSec);
				//ApplicationLauncher.logger.debug("bofaPowerSourceSendCommandProcess :Hit2");
				if(rxMsgQ_PwrSrc.IsResponseReceived()) {
					if(rxMsgQ_PwrSrc.isExpectedResponseReceived()){
						ApplicationLauncher.logger.debug("bofaPowerSourceSendCommandProcess :Ack Response Success");
						//String CurrentLDU_Data =lduData.getLDU_ReadSerialData();
						//ApplicationLauncher.logger.info("bofaPowerSourceSendCommandProcess: VICI Received Data1:"+CurrentLDU_Data);
						//StripLDU_SerialData(lduData.getReceivedLength());
						responseStatus=DeleteMeConstant.SUCCESS_RESPONSE;
						rxMsgQ_PwrSrc.removeProcessedMsgFromQueue();
					}else if(rxMsgQ_PwrSrc.isExpectedErrorResponseReceived()){
						ApplicationLauncher.logger.debug("bofaPowerSourceSendCommandProcess :Ack Response Error");
						String CurrentReadData =rxMsgQ_PwrSrc.getLastReadMessage();
						//ApplicationLauncher.logger.info("bofaPowerSourceSendCommandProcess: ErrorResponse Received:"+CurrentLDU_Data);
						//ApplicationLauncher.logger.info("bofaPowerSourceSendCommandProcess: ExpectedError1Data:"+ExpectedError1Data);
						//StripLDU_SerialData(CurrentLDU_Data.length());
						
						responseStatus=CurrentReadData;
						rxMsgQ_PwrSrc.removeProcessedMsgFromQueue();
		
					}else if(rxMsgQ_PwrSrc.isUnknownResponseRecieved()){
						
						
						String CurrentReadData =rxMsgQ_PwrSrc.getLastReadMessage();
						ApplicationLauncher.logger.debug("bofaPowerSourceSendCommandProcess : isUnknownResponseRecieved : CurrentReadData: "+ CurrentReadData);
						responseStatus=CurrentReadData;
						rxMsgQ_PwrSrc.removeProcessedMsgFromQueue();
						if(responseStatus!=null) { //Gopi2
							ApplicationLauncher.logger.debug("bofaPowerSourceSendCommandProcess :Unexpected Message: dropping the message:"+responseStatus);
							ApplicationLauncher.logger.debug("bofaPowerSourceSendCommandProcess :Unexpected Message: dropping the message:"+GuiUtils.HexToString(responseStatus));
						}
						
					}
				}
				else{
					ApplicationLauncher.logger.info("bofaPowerSourceSendCommandProcess: Ack No Data Received");
				}
				
				ApplicationLauncher.logger.debug("bofaPowerSourceSendCommandProcess : test1: ");
			}
		}else{
			responseStatus=DeleteMeConstant.NO_RESPONSE;
			Sleep(200);
		}
		//ApplicationLauncher.logger.debug("bofaPowerSourceSendCommandProcess : test2: ");
		if(rxMsgQ_PwrSrc.getLastReadMessage()==null) {
			ApplicationLauncher.logger.info("bofaPowerSourceSendCommandProcess: last read null");
		}else {
			ApplicationLauncher.logger.info("bofaPowerSourceSendCommandProcess: last read:"+rxMsgQ_PwrSrc.getLastReadMessage());
		}
		ApplicationLauncher.logger.debug("bofaPowerSourceSendCommandProcess :Exit");
		return responseStatus;
	}


	
	public void powerSourceSetRxMessageTerminator(String terminatorInHex) {
		// TODO Auto-generated method stub
		
		rxPhysical_PwrSrc.setResponseMessageTerminatorInHex(terminatorInHex);
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
	
	public SerialRxMessageQ getRxMsgQ_PwrSrc() {
		return rxMsgQ_PwrSrc;
	}

	public void setRxMsgQ_PwrSrc(SerialRxMessageQ rxMsgQ_PwrSrc) {
		this.rxMsgQ_PwrSrc = rxMsgQ_PwrSrc;
	}
	
		
}

