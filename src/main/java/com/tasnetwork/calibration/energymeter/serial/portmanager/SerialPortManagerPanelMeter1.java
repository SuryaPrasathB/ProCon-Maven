package com.tasnetwork.calibration.energymeter.serial.portmanager;

import java.io.UnsupportedEncodingException;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceMte;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdMte;
import com.tasnetwork.calibration.energymeter.constant.DeleteMeConstant;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.deployment.MaintenanceModeExecController;
import com.tasnetwork.calibration.energymeter.device.Communicator;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

public class SerialPortManagerPanelMeter1 {
	DeviceDataManagerController DisplayDataObj =  new DeviceDataManagerController();
	
	public static Communicator commPanelMeter1= null;
	
	public SerialTxMessageQ txMsgQ_PanelMeter1 = new SerialTxMessageQ(commPanelMeter1);
	
	public SerialRxMessageQ rxMsgQ_PanelMeter1 = new SerialRxMessageQ(commPanelMeter1);
	public SerialRxPhysical rxPhysical_PanelMeter1 = new SerialRxPhysical(commPanelMeter1,rxMsgQ_PanelMeter1,false,"PanelMeter1",DeleteMeConstant.HV_ER_TERMINATOR,"");
	//public SerialRxPhysical rxPhysical_PwrSrc = new SerialRxPhysical(commPowerSrc,rxMsgQ_PwrSrc,false,"PwrSrc",DeleteMeConstant.HV_ER_TERMINATOR,"");
	
	public static boolean bInitOccured = false;
	public static boolean panelMeter1SerialStatusConnected = false;
	
	public SerialPortManagerPanelMeter1(){
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
		ApplicationLauncher.logger.debug("createObjects :Entry");
		//commPowerSrc = new Communicator(ConstantApp.SERIAL_PORT_POWER_SOURCE);
		//commRefStandard = new Communicator(ConstantApp.SERIAL_PORT_REF_STD);
/*		commLDU = new Communicator(ConstantApp.SERIAL_PORT_LDU);
		
		commBridge = new Communicator(ConstantProGEN_App.SERIAL_PORT_BRIDGE);
		commLVD = new Communicator(ConstantProGEN_App.SERIAL_PORT_PT_VOLTAGE_DIVIDER);
		commPT_Burden = new Communicator(ConstantProGEN_App.SERIAL_PORT_PT_BURDEN);
		commCT_Burden = new Communicator(ConstantProGEN_App.SERIAL_PORT_CT_BURDEN);*/
		commPanelMeter1 = new Communicator(ConstantRefStdMte.SERIAL_PORT_REF_STD);//ConstantProGEN_App.SERIAL_PORT_HVCI);
		ApplicationLauncher.logger.debug("createObjects :Exit");
		//commVICI  = new Communicator(ConstantPrimaryVICI_Meter.SERIAL_PORT_VICI);
	}
	
	public void ScanForSerialCommPort(){

		//commPowerSrc.searchForPorts();  
		commPanelMeter1.searchForPorts();  

	}
	
	//public void startSerialRxPhysical_PowerSource() {
	public void startSerialRxPhysical_PanelMeter1() {	

		rxPhysical_PanelMeter1.SerialRxPhysicalTimerStart();
		//rxPhysicalObj.setReadRxPhysicalFlag(true);
	}
	
	//public void enableSerialRxPhysical_PowerSourceMonitor() {
	public void enableSerialRxPhysical_PanelMeter1Monitor() {
		rxPhysical_PanelMeter1.setReadRxPhysicalFlag(true);
		
	}
	
	//public void disableSerialRxPhysical_PowerSourceMonitor() {
	public void disableSerialRxPhysical_PanelMeter1Monitor() {
		rxPhysical_PanelMeter1.setReadRxPhysicalFlag(false);
		

	}
	
	public void disconnectPanelMeter1(){
		ApplicationLauncher.logger.debug("disconnectPanelMeter1 :Entry");
		if(panelMeter1SerialStatusConnected){
			ApplicationLauncher.logger.debug("disconnectPanelMeter1 :Entry2");
			disableSerialRxPhysical_PanelMeter1Monitor();
			//DisplayDataObj.setPowerSrcReadData(false);
			Sleep(500);
			//disconnectPowerSourceSerialComm();
			disconnectPanelMeter1SerialComm();
			panelMeter1SerialStatusConnected=false;
			
		}
	}
	
	public void disconnectPanelMeter1SerialComm(){
		ApplicationLauncher.logger.debug("disconnectPanelMeter1SerialComm :Entry");
		commPanelMeter1.disconnect();
	}
	
	public boolean panelMeter1ComInit(String InputComm,String BaudRate) {
		ApplicationLauncher.logger.debug("panelMeter1ComInit Invoked:");
		ApplicationLauncher.logger.debug("CommInput: " + InputComm);
		ApplicationLauncher.logger.debug("BaudRate: " + BaudRate);
		try {
			panelMeter1SerialStatusConnected = panelMeter1CommSetting(InputComm,BaudRate);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("panelMeter1ComInit :UnsupportedEncodingException:"+ e.getMessage());
		}


		return panelMeter1SerialStatusConnected;
	}
	
	public boolean panelMeter1CommSetting(String CommInput, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("panelMeter1CommSetting :Entry");
		//InitSerialCommPort();
		ApplicationLauncher.logger.debug("commPanelMeter1: " +commPanelMeter1 );
		ApplicationLauncher.logger.debug("CommInput: " + CommInput);
		ApplicationLauncher.logger.debug("BaudRate: " + BaudRate);
		boolean status = SetSerialComm(commPanelMeter1,CommInput,Integer.valueOf(BaudRate),true);
		if (status){
			commPanelMeter1.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("panelMeter1CommSetting:"+CommInput+" access failed");
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
					ApplicationLauncher.logger.info("Panel Meter: PortDeviceMapping:"+ SerialPortObj.getPortDeviceMapping()+":"+ReadHexFormat);
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
	
	public void panelMeter1SendCommand(String payLoadInHex,int timeDelayInMilliSec){

		//String Data = GUIUtils.StringToHex(ConstantPrimaryVICI_Meter.VI_CMD_START)+GUIUtils.StringToHex(SelectedPhase)+CommandVI_PayLoad+GUIUtils.StringToHex(ConstantPrimaryVICI_Meter.VI_CMD_TERMINATOR);
		//String Data = ConstantPrimaryVICI_Meter.VI_CMD_START+GUIUtils.StringToHex(SelectedPhase)+CommandVI_PayLoad+ConstantPrimaryVICI_Meter.VI_CMD_TERMINATOR;
		//String Data = "";//SerialMessageHV_Src.getCmd_HV_Start(CommandVI_PayLoad,SelectedPhase);
		//WriteToSerialCommLDU(Data);
		writeHexToSerialPanelMeter1(payLoadInHex,timeDelayInMilliSec);

	}
	
	public void writeHexToSerialPanelMeter1(String Data,int timeDelayInMilliSec){
		ApplicationLauncher.logger.debug("writeHexToSerialPanelMeter1 :DataHex:"+Data);
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
					commPanelMeter1.writeStringMsgToPortInHex(eachDataInHex);
					Sleep(timeDelayInMilliSec);
					//Sleep(10);
					//Sleep(50);
					//Sleep(80);
					//Sleep(1000);//worked good for 10mA and 25mA calibration
					//Sleep(500);
					//Sleep(80);
	
	
	
				}
			}else{
				commPanelMeter1.writeStringMsgToPortInHex(Data);
			}
		}catch(Exception e){
			ApplicationLauncher.logger.error("writeHexToSerialPanelMeter1 :Exception :" + e.getMessage());
		}
		//Sleep(200);

	}
	
	public void panelMeter1SetExpectedData(String ExpectedResponse){
		ApplicationLauncher.logger.debug("panelMeter1SetExpectedData :Entry");
		Communicator SerialPortObj =commPanelMeter1;

		
		SerialPortObj.setExpectedLength(ExpectedResponse.length());
		SerialPortObj.setExpectedResult(ExpectedResponse);
		//SerialPortObj.setExpectedError1Result(ExpectedError1Data);
		//SerialPortObj.setExpectedError2Result(ExpectedError2Data);
		ApplicationLauncher.logger.debug("panelMeter1SetExpectedData: setExpectedResult:"+SerialPortObj.getExpectedResult());
		ApplicationLauncher.logger.debug("panelMeter1SetExpectedData: setExpectedLength:"+SerialPortObj.getExpectedLength());

		//SerialDataMsgHV_Manager serialDataMsgManager = new SerialDataMsgHV_Manager(SerialPortObj);
		//serialDataManager.SerialResponseTimerStart(30);
		//SerialPortObj = null;//garbagecollector
		//return serialDataMsgManager;
	}
	
	public void panelMeter1SetExpectedError1Data(String ExpectedError1Response){
		ApplicationLauncher.logger.debug("panelMeter1SetExpectedError1Data :Entry");
		Communicator SerialPortObj =commPanelMeter1;
		
		//SerialPortObj.setExpectedLength(ExpectedError1Response.length());
		SerialPortObj.setExpectedError1Result(ExpectedError1Response);
		//SerialPortObj.setExpectedError1Result(ExpectedError1Data);
		//SerialPortObj.setExpectedError2Result(ExpectedError2Data);
		ApplicationLauncher.logger.debug("panelMeter1SetExpectedError1Data: getExpectedError1Result:"+SerialPortObj.getExpectedError1Result());
		//ApplicationLauncher.logger.debug("HVCI_SetExpectedError1Data: setExpectedLength:"+SerialPortObj.getExpectedLength());

		//SerialDataMsgHV_Manager serialDataMsgManager = new SerialDataMsgHV_Manager(SerialPortObj);
		//serialDataManager.SerialResponseTimerStart(30);
		//SerialPortObj = null;//garbagecollector
		//return serialDataMsgManager;
	}
	
	public void panelMeter1SetExpectedError2Data(String ExpectedError2Response){
		ApplicationLauncher.logger.debug("panelMeter1SetExpectedError2Data :Entry");
		Communicator SerialPortObj =commPanelMeter1;
		
		//SerialPortObj.setExpectedLength(ExpectedError1Response.length());
		SerialPortObj.setExpectedError2Result(ExpectedError2Response);
		//SerialPortObj.setExpectedError1Result(ExpectedError1Data);
		//SerialPortObj.setExpectedError2Result(ExpectedError2Data);
		ApplicationLauncher.logger.debug("panelMeter1SetExpectedError2Data: getExpectedError2Result:"+SerialPortObj.getExpectedError2Result());
		//ApplicationLauncher.logger.debug("HVCI_SetExpectedError1Data: setExpectedLength:"+SerialPortObj.getExpectedLength());

		//SerialDataMsgHV_Manager serialDataMsgManager = new SerialDataMsgHV_Manager(SerialPortObj);
		//serialDataManager.SerialResponseTimerStart(30);
		//SerialPortObj = null;//garbagecollector
		//return serialDataMsgManager;
	}
	
	public void panelMeter1ResetResponseFlag() {
		rxPhysical_PanelMeter1.ClearRxPhysicalData();
		rxPhysical_PanelMeter1.clearLastDisplayedReadRxPhysicalData();
		rxMsgQ_PanelMeter1.setExpectedResponseRecieved(false);
		rxMsgQ_PanelMeter1.setErrorResponseRecieved(false);
		rxMsgQ_PanelMeter1.setUnknownResponseRecieved(false);
		rxMsgQ_PanelMeter1.setResponseRecieved(false);
		rxMsgQ_PanelMeter1.clearMsgQueue();
		
		//rxPhysical_HVCI.ClearRxPhysicalData();

	}
	
	public String panelMeter1SendCommandProcess(String payLoadInHex, int timeDelayInMilliSec, String expectedDataInHex,boolean isResponseExpected){
		ApplicationLauncher.logger.debug("panelMeter1SendCommandProcess :Entry");
		//boolean status=false;
		String responseStatus=DeleteMeConstant.NO_RESPONSE;
		int retryCount= 5;//10//ConstantHV_Src.HV_READ_RESPONSE_RETRY_COUNT+10;//1;

		//String ExpectedDataInHex = GUIUtils.StringToHex("K");//GUIUtils.StringToHex( VoltageValue)+ConstantPrimaryVICI_Meter.VI_ER_TERMINATOR;//getER_HV_StartAck
		String ExpectedError1Data = "";//ConstantPrimaryVICI_Meter.VI_CMD_ERROR_RESPONSE_HDR;//+ConstantPrimaryVICI_Meter.VI_ER_TERMINATOR;
		
		String ExpectedError2Data="";
		String terminatorInHex = GuiUtils.StringToHex("K");
		//HVCI_SetExpectedData(SerialMessageHV.getER_HV_StartVoltageValue(VoltageValue));
		panelMeter1SetExpectedData(expectedDataInHex);//SerialMessageHV_Src.getER_HV_StartAck(VoltageValue));
		panelMeter1SetExpectedError1Data(ExpectedError1Data);//SerialMessageHV_Src.ER_HV_ERROR_HEADER); 
		panelMeter1SetExpectedError2Data(ExpectedError2Data);
		panelMeter1SetRxMessageTerminator(terminatorInHex);
		
		//rxPhysical_PwrSrc.setMessageTerminatorInHex(GUIUtils.StringToHex("K"));
		panelMeter1ResetResponseFlag();

		panelMeter1SendCommand(payLoadInHex,timeDelayInMilliSec);
		if(isResponseExpected){
			while (retryCount>0 && responseStatus.equals(DeleteMeConstant.NO_RESPONSE)){
				retryCount--;
				//VI_SendStartCommand(CommandVI_PayLoad,SelectedPhase);
				//ApplicationLauncher.logger.debug("powerSourceSendCommandProcess :Hit1");
				//int TimeOutInSec= 60;vjhvjgv
				//SerialDataLDU lduData = VICI_ReadDataWithErrorResponse(ExpectedData.length(),ExpectedData,ExpectedError1Data,ExpectedError2Data,TimeOutInSec);
				//ApplicationLauncher.logger.debug("powerSourceSendCommandProcess :Hit2");
				if(rxMsgQ_PanelMeter1.IsResponseReceived()) {
					if(rxMsgQ_PanelMeter1.isExpectedResponseReceived()){
						ApplicationLauncher.logger.debug("panelMeter1SendCommandProcess :Ack Response Success");
						//String CurrentLDU_Data =lduData.getLDU_ReadSerialData();
						//ApplicationLauncher.logger.info("powerSourceSendCommandProcess: VICI Received Data1:"+CurrentLDU_Data);
						//StripLDU_SerialData(lduData.getReceivedLength());
						responseStatus=DeleteMeConstant.SUCCESS_RESPONSE;
						rxMsgQ_PanelMeter1.removeProcessedMsgFromQueue();
					}else if(rxMsgQ_PanelMeter1.isExpectedErrorResponseReceived()){
						ApplicationLauncher.logger.debug("panelMeter1SendCommandProcess :Ack Response Error");
						String CurrentReadData =rxMsgQ_PanelMeter1.getLastReadMessage();
						//ApplicationLauncher.logger.info("powerSourceSendCommandProcess: ErrorResponse Received:"+CurrentLDU_Data);
						//ApplicationLauncher.logger.info("powerSourceSendCommandProcess: ExpectedError1Data:"+ExpectedError1Data);
						//StripLDU_SerialData(CurrentLDU_Data.length());
						
						responseStatus=CurrentReadData;
						rxMsgQ_PanelMeter1.removeProcessedMsgFromQueue();
		
					}else if(rxMsgQ_PanelMeter1.isUnknownResponseRecieved()){
						
						
						String CurrentReadData =rxMsgQ_PanelMeter1.getLastReadMessage();
						responseStatus=CurrentReadData;
						rxMsgQ_PanelMeter1.removeProcessedMsgFromQueue();
						if(responseStatus!=null) { //Gopi2
							ApplicationLauncher.logger.debug("panelMeter1SendCommandProcess :Unexpected Message: dropping the message:"+responseStatus);
							ApplicationLauncher.logger.debug("panelMeter1SendCommandProcess :Unexpected Message: dropping the message:"+GuiUtils.HexToString(responseStatus));
						}
						
						
					}
				}
				else{
					ApplicationLauncher.logger.info("panelMeter1SendCommandProcess: Ack No Data Received");
				}
			}
		}
		if(rxMsgQ_PanelMeter1.getLastReadMessage()==null) {
			ApplicationLauncher.logger.info("panelMeter1SendCommandProcess: last read null");
		}else {
			ApplicationLauncher.logger.info("panelMeter1SendCommandProcess: last read:"+rxMsgQ_PanelMeter1.getLastReadMessage());
		}
		ApplicationLauncher.logger.debug("panelMeter1SendCommandProcess :Exit");
		return responseStatus;
	}

	
	private void panelMeter1SetRxMessageTerminator(String terminatorInHex) {
		// TODO Auto-generated method stub
		
		rxPhysical_PanelMeter1.setResponseMessageTerminatorInHex(terminatorInHex);
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
