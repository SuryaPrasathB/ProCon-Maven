package com.tasnetwork.calibration.conveyor.serial.messenger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.bay.DevSysEnergyMeter;
import com.tasnetwork.calibration.conveyor.bay.EIC_MegaOhmMeter;
import com.tasnetwork.calibration.conveyor.bay.Elmeasure_MultiMeter;
import com.tasnetwork.calibration.conveyor.bay.NewlandQRCodeScanner;
import com.tasnetwork.calibration.conveyor.bay.ir.IR_ReadResult;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmDut;
import com.tasnetwork.calibration.conveyor.util.GUIUtils;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.DeleteMeConstant;



public class DutMessenger {

	//DeviceDataManagerController displayDataObj =  new DeviceDataManagerController();
	SpmDut pwrSrcSpmObj =  new ConveyorDataManager().getSerialPortManagerPwrSrc_V2();
	
	public DutMessenger(SpmDut qrScannerSerialPort){
		this.pwrSrcSpmObj = qrScannerSerialPort;
	}
	
	public DutMessenger(){
		
	}
	//==========================================================================================================================	


//==========================================================================================================================	

	
	public Map<String,Object> sendReadSerialNumberCommandToDut(){
		ApplicationLauncher.logger.info("sendReadSerialNumberCommandToDut: Entry"); 
		boolean status = false;
		//boolean isResponseExpected = true;
		
		String payLoadInHex = DevSysEnergyMeter.DEVGSR ; //ANALOG_TRIGGER_SETTING;//startTestEndFrame ;xcvxc
		ApplicationLauncher.logger.info("send: payLoadInHex: " + payLoadInHex  );
		
/*		String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;

		if (address == ConstantPowerSourceBofa.LDU_INT_BROADCAST_ADDRESS) {
			 addressStr = ConstantPowerSourceBofa.LDU_HEX_BROADCAST_ADDRESS ;
		 }*/
		
		String expectedDataInHex = DevSysEnergyMeter.ER_DATA_IN_HEX;
		Map<String,Object> responseReturn = sendCommandToDut(payLoadInHex,expectedDataInHex);
		/*int timeDelayInMilliSec = 0;
		//String expectedDataInHex = "06";//ConstantPowerSourceBofa.ER_LDU_STARTS_WITH + addressStr;
		String responseData = "";
		boolean isResponseExpected = true;
		Map<String,Object> responseReturn = new HashMap<String,Object>();
		//SerialDM_Obj.WriteToSerialCommPwrSrc(ANALOG_TRIGGER_SETTING,ER_ANALOG_TRIGGER_SETTING);
		String responseStatus = dutMsngrSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected,"test-T1");
		
		if (responseStatus.equals(DeleteMeConstant.SUCCESS_RESPONSE)) {
			//if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
				responseData = getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
				responseData = GUIUtils.asciiToHex(responseData);
			
			status = true;
			responseReturn.put("status", true);
			responseReturn.put("responseData", responseData);
			//status = processResponse(readTheConstantOfLiveReferenceMeterCmdFrame, CurrentReadData);
		}else {
			if(!isResponseExpected){
				if(responseStatus.equals(DeleteMeConstant.NO_RESPONSE)){
					ApplicationLauncher.logger.info("sendReadSerialNumberCommandToDut : no response expected success");
					status = true;
				}
			}
		}*/
		return responseReturn;
	}
	
	
	public Map<String,Object> sendCommandToDut(String payLoadInHex, String expectedDataInHex){
		ApplicationLauncher.logger.info("sendCommandToDut: Entry"); 
		boolean status = false;
		//boolean isResponseExpected = true;
		
		//String payLoadInHex = DevSysEnergyMeter.DEVGSR ; //ANALOG_TRIGGER_SETTING;//startTestEndFrame ;xcvxc
		ApplicationLauncher.logger.info("sendCommandToDut: payLoadInHex: " + payLoadInHex  );
		
/*		String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;

		if (address == ConstantPowerSourceBofa.LDU_INT_BROADCAST_ADDRESS) {
			 addressStr = ConstantPowerSourceBofa.LDU_HEX_BROADCAST_ADDRESS ;
		 }*/
		
		//String expectedDataInHex = DevSysEnergyMeter.EXPECTED_DATA_IN_HEX;
		int timeDelayInMilliSec = 0;
		//String expectedDataInHex = "06";//ConstantPowerSourceBofa.ER_LDU_STARTS_WITH + addressStr;
		String responseData = "";
		boolean isResponseExpected = true;
		if (payLoadInHex.equals(DevSysEnergyMeter.DEVMER)) { 
			isResponseExpected = false;
		}
		Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
		//SerialDM_Obj.WriteToSerialCommPwrSrc(ANALOG_TRIGGER_SETTING,ER_ANALOG_TRIGGER_SETTING);
		
		String responseStatus = dutMsngrSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected,"test-T1");
		
		if (responseStatus.equals(DeleteMeConstant.SUCCESS_RESPONSE)) {
			//if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
				responseData = getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
				responseData = GUIUtils.asciiToHex(responseData);
			
			status = true;
			responseReturn.put("status", true);
			responseReturn.put("responseData", responseData);
			//status = processResponse(readTheConstantOfLiveReferenceMeterCmdFrame, CurrentReadData);
		}else {
			if(!isResponseExpected){
				if(responseStatus.equals(DeleteMeConstant.NO_RESPONSE)){
					ApplicationLauncher.logger.info("sendCommandToDut : no response expected success");
					status = true;
					responseReturn.put("status", true);
					//responseReturn.put("responseData", responseData);
				}
			}
		}
		return responseReturn;
	}
	
	

	

  	
  //==================================================================================================================================


  	
  //==================================================================================================================================

	
	public SpmDut getPwrSrcSpmObj() {
		return pwrSrcSpmObj;
	}
	public void setPwrSrcSpmObj(SpmDut pwrSrcSpmObj) {
		this.pwrSrcSpmObj = pwrSrcSpmObj;
	}

	//==========================================================================================================================	

	public boolean bofaSetPowerSourceMctNctMode(String mctNctMode, boolean forceSet) {
		ApplicationLauncher.logger.debug("bofaSetPowerSourceMctNctMode :Entry");
		ApplicationLauncher.logger.info("bofaSetPowerSourceMctNctMode : mctNctMode: " +mctNctMode);
		boolean status = false;
		String circuit = "";
		/*int address    = ConstantPowerSourceBofa.ADDRESS_SLAVE_01;

		if(mctNctMode.equals(ConstantReport.RESULT_EXECUTION_MODE_MAIN_CT)) {
			circuit = ConstantPowerSourceLscs.CMD_PWR_SRC_MAIN_CT_MODE_HDR;
		} else if(mctNctMode.equals(ConstantReport.RESULT_EXECUTION_MODE_NEUTRAL_CT)) {
			circuit = ConstantPowerSourceLscs.CMD_PWR_SRC_NEUTRAL_CT_MODE_HDR;
		}else if(mctNctMode.equals(ConstantReport.RESULT_EXECUTION_MODE_MCT_NCT_OFF)) {
			circuit = ConstantPowerSourceLscs.CMD_PWR_SRC_ALL_CT_MODE_OFF_HDR;
		}else {
			ApplicationLauncher.logger.info("bofaSetPowerSourceMctNctMode :invalid mode data: mctNctMode: "+mctNctMode);
			return status;
		}*/
		int retryCount = 3;
		status = false;
		while ((retryCount>0) && (!status) ){
			ApplicationLauncher.logger.debug("bofaSetPowerSourceMctNctMode : Bofa : retryCount: "+retryCount);
			//status = Data_LduBofa.bofaSendSwitchCtCircuit(address, circuit);
			retryCount--;
			Sleep(300);
		}
		ApplicationLauncher.logger.debug("bofaSetPowerSourceMctNctMode : Bofa : status: "+status);
		ApplicationLauncher.logger.debug("bofaSetPowerSourceMctNctMode : Bofa : retry Exit: ");




		if(status){
			//setPowerSrcOnFlag(false);
			//setPowerSrcTurnedOnStatus(false);
			ApplicationLauncher.logger.info("***************************************");
			ApplicationLauncher.logger.info("bofaSetPowerSourceMctNctMode: Mode set success");
			ApplicationLauncher.logger.info("***************************************");
		}else{
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("bofaSetPowerSourceMctNctMode: Mode set Failed");
			//FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_1002);
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
		}

		ApplicationLauncher.logger.debug("bofaSetPowerSourceMctNctMode: Exit");
		return status;


	}
	//==========================================================================================================================	

	
	public String dutMsngrSendCommandProcess(String payLoadInHex, int timeDelayInMilliSec, 
			String expectedDataInHex,boolean isResponseExpected,String sourceThread){
		ApplicationLauncher.logger.debug("dutMsngrSendCommandProcess :Entry");
		//boolean status=false;
		try{
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
	        String presentTimeStamp = LocalDateTime.now().format(formatter);
			sourceThread = getPwrSrcSpmObj().getMyTraceName() + "-" + presentTimeStamp;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("dutMsngrSendCommandProcess :Exception:"+ e.getMessage());
		}
		ApplicationLauncher.logger.debug("dutMsngrSendCommandProcess :sourceThread:" +sourceThread);
		String responseStatus=DeleteMeConstant.NO_RESPONSE;
		int retryCount= 1;//#Version0.5.1.4//5;//10//ConstantHV_Src.HV_READ_RESPONSE_RETRY_COUNT+10;//1;

		//String ExpectedError1Data = "";//ConstantPrimaryVICI_Meter.VI_CMD_ERROR_RESPONSE_HDR;//+ConstantPrimaryVICI_Meter.VI_ER_TERMINATOR;
		
		//String ExpectedError2Data="";
		
		
		
		/*powerSourceSetExpectedData(expectedDataInHex);
		powerSourceSetExpectedError1Data(ExpectedError1Data); 
		powerSourceSetExpectedError2Data(ExpectedError2Data);
		powerSourceSetRxMessageTerminator(terminatorInHex);
		
		powerSourceResetResponseFlag();*/
		
		getPwrSrcSpmObj().getRxMsgQ_PwrSrc().clearLastReadMessage();
		//powerSourceSendCommand(payLoadInHex,timeDelayInMilliSec);
		
 
		
		//ApplicationLauncher.logger.debug("dutMsngrSendCommandProcess : expectedDataInHex: " + expectedDataInHex);
		String expectedData = "";
		if(isResponseExpected){
			expectedData = GUIUtils.hexToAsciiV2(expectedDataInHex);
		}
		
		boolean status = false;
		String rxMessageTerminator = GUIUtils.hexToAscii("0D0A");//ConstantPowerSourceBofa.END_BYTE);
		Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
		if(getPwrSrcSpmObj().isDeviceSerialStatusConnected()){
			responseReturn = getPwrSrcSpmObj().dutSendCommandProcess( payLoadInHex, timeDelayInMilliSec,
					expectedData,isResponseExpected, rxMessageTerminator, sourceThread);
			status = (boolean)responseReturn.get("status");
			if(status){
				String responseData = (String)responseReturn.get("result");
				ApplicationLauncher.logger.debug("dutMsngrSendCommandProcess : responseData <"+sourceThread +">: " + responseData);
				ApplicationLauncher.logger.debug("dutMsngrSendCommandProcess : responseData <"+sourceThread +"> hex : " + GUIUtils.asciiToHex(responseData));
				//if(responseData.equals(GUIUtils.hexToAsciiV2(ConstantPowerSourceBofa.CMD_STOP_VOLT_CURRENT_IN_HEX))) {
				//if(responseData.equals(GUIUtils.hexToAsciiV2(ConstantPowerSourceBofa.ER_CMD_STOP_VOLT_CURRENT_POSITIVE_IN_HEX))) {
					status = true;
				//}else {
				//	status = false;
				//}
	
			}	
			
			
			if(isResponseExpected){
				while (retryCount>0 && responseStatus.equals(DeleteMeConstant.NO_RESPONSE)){
					retryCount--;
					//VI_SendStartCommand(CommandVI_PayLoad,SelectedPhase);
					ApplicationLauncher.logger.debug("dutMsngrSendCommandProcess :retryCount<"+sourceThread +">:" + retryCount);
					//int TimeOutInSec= 60;vjhvjgv
					//SerialDataLDU lduData = VICI_ReadDataWithErrorResponse(ExpectedData.length(),ExpectedData,ExpectedError1Data,ExpectedError2Data,TimeOutInSec);
					//ApplicationLauncher.logger.debug("dutMsngrSendCommandProcess :Hit2");
					if(getPwrSrcSpmObj().getRxMsgQ_PwrSrc().IsResponseReceived()) {
						if(getPwrSrcSpmObj().getRxMsgQ_PwrSrc().isExpectedResponseReceived()){
							ApplicationLauncher.logger.debug("dutMsngrSendCommandProcess: <"+sourceThread +">:Ack Response Success");
							//String CurrentLDU_Data =lduData.getLDU_ReadSerialData();
							//ApplicationLauncher.logger.info("dutMsngrSendCommandProcess: VICI Received Data1:"+CurrentLDU_Data);
							//StripLDU_SerialData(lduData.getReceivedLength());
							responseStatus=DeleteMeConstant.SUCCESS_RESPONSE;
							getPwrSrcSpmObj().getRxMsgQ_PwrSrc().removeProcessedMsgFromQueue();
						}else if(getPwrSrcSpmObj().getRxMsgQ_PwrSrc().isExpectedErrorResponseReceived()){
							ApplicationLauncher.logger.debug("dutMsngrSendCommandProcess <"+sourceThread +">:Ack Response Error");
							String CurrentReadData =getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
							//ApplicationLauncher.logger.info("dutMsngrSendCommandProcess: ErrorResponse Received:"+CurrentLDU_Data);
							//ApplicationLauncher.logger.info("dutMsngrSendCommandProcess: ExpectedError1Data:"+ExpectedError1Data);
							//StripLDU_SerialData(CurrentLDU_Data.length());
							
							responseStatus=CurrentReadData;
							getPwrSrcSpmObj().getRxMsgQ_PwrSrc().removeProcessedMsgFromQueue();
			
						}else if(getPwrSrcSpmObj().getRxMsgQ_PwrSrc().isUnknownResponseRecieved()){
							
							
							String CurrentReadData =getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
							ApplicationLauncher.logger.debug("dutMsngrSendCommandProcess : isUnknownResponseRecieved : CurrentReadData<"+sourceThread +">: "+ CurrentReadData);
							responseStatus=CurrentReadData;
							getPwrSrcSpmObj().getRxMsgQ_PwrSrc().removeProcessedMsgFromQueue();
							if(responseStatus!=null) { //Gopi2
								ApplicationLauncher.logger.debug("dutMsngrSendCommandProcess :Unexpected Message: dropping the message<"+sourceThread +">:"+responseStatus);
								ApplicationLauncher.logger.debug("dutMsngrSendCommandProcess :Unexpected Message: dropping the message<"+sourceThread +">:"+GUIUtils.HexToString(responseStatus));
							}
							
						}
					}
					else{
						ApplicationLauncher.logger.info("dutMsngrSendCommandProcess : <"+sourceThread +">: Ack No Data Received");
					}
					
					
					ApplicationLauncher.logger.debug("dutMsngrSendCommandProcess : <"+sourceThread +">test1: ");
					
/*					if ( ( !payLoadInHex.equals(ConstantPowerSourceBofa.CMD_STOP_VOLT_CURRENT_IN_HEX)) && 
						     ( !payLoadInHex.equals(ConstantPowerSourceBofa.CMD_STOP_CURRENT_IN_HEX)) ){
							if(ProjectExecutionController.getUserAbortedFlag()){
								retryCount = 0;
								responseStatus = DeleteMeConstant.ERROR_RESPONSE;
								ApplicationLauncher.logger.info("dutMsngrSendCommandProcess V2 <"+sourceThread +">: user aborted - detected");
							}
						}else if(!getPwrSrcSpmObj().isPwrSrcSerialStatusConnected()){
							retryCount = 0;
							responseStatus = DeleteMeConstant.ERROR_RESPONSE;
							ApplicationLauncher.logger.debug("dutMsngrSendCommandProcess V2 <"+sourceThread +">: serial port - disconnected");
						
						}*/
				}
			}else{
				responseStatus=DeleteMeConstant.NO_RESPONSE;
				Sleep(50);//200);
			}
		}else{
			ApplicationLauncher.logger.debug("dutMsngrSendCommandProcess V2 <"+sourceThread +">: serial port not connected or disconnected");
			
		}
		//ApplicationLauncher.logger.debug("dutMsngrSendCommandProcess : test2: ");
		if(getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage()==null) {
			ApplicationLauncher.logger.info("dutMsngrSendCommandProcess<"+sourceThread +">: last read null");
		}else {
			ApplicationLauncher.logger.info("dutMsngrSendCommandProcess: last read<"+sourceThread +">:"+getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage());
		}
		ApplicationLauncher.logger.debug("dutMsngrSendCommandProcess <"+sourceThread +">:Exit");
		return responseStatus;
	}
	//==========================================================================================================================	

	public String dutMsngrSendCommandProcessWithLength(String payLoadInHex, int timeDelayInMilliSec, 
			String expectedDataInHex,int expectedDataHexLength,boolean isResponseExpected,String sourceThread){
		ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessWithLength :Entry");
		//boolean status=false;

		String responseStatus=DeleteMeConstant.NO_RESPONSE;
		int retryCount= 5;//10//ConstantHV_Src.HV_READ_RESPONSE_RETRY_COUNT+10;//1;

		//String ExpectedError1Data = "";//ConstantPrimaryVICI_Meter.VI_CMD_ERROR_RESPONSE_HDR;//+ConstantPrimaryVICI_Meter.VI_ER_TERMINATOR;
		
		//String ExpectedError2Data="";
		
		
		
		/*powerSourceSetExpectedData(expectedDataInHex);
		powerSourceSetExpectedError1Data(ExpectedError1Data); 
		powerSourceSetExpectedError2Data(ExpectedError2Data);
		powerSourceSetRxMessageTerminator(terminatorInHex);
		
		powerSourceResetResponseFlag();*/
		
		getPwrSrcSpmObj().getRxMsgQ_PwrSrc().clearLastReadMessage();
		//powerSourceSendCommand(payLoadInHex,timeDelayInMilliSec);
		
		
		
		
		
	
		
		
		//ApplicationLauncher.logger.debug("dutMsngrSendCommandProcess : expectedDataInHex: " + expectedDataInHex);
		String expectedData = "";
		if(isResponseExpected){
			expectedData = GUIUtils.hexToAsciiV2(expectedDataInHex);
		}
		
		boolean status = false;
		String rxMessageTerminator = GUIUtils.hexToAscii("0D0A");//ConstantPowerSourceBofa.END_BYTE);
		Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
		if(getPwrSrcSpmObj().isDeviceSerialStatusConnected()){
			responseReturn = getPwrSrcSpmObj().dutSendCommandProcessWithLength( payLoadInHex, timeDelayInMilliSec,
					expectedData,isResponseExpected, expectedDataHexLength, sourceThread);
			status = (boolean)responseReturn.get("status");
			if(status){
				String responseData = (String)responseReturn.get("result");
				ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessWithLength : responseData <"+sourceThread +">: " + responseData);
				ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessWithLength : responseData <"+sourceThread +"> hex : " + GUIUtils.asciiToHex(responseData));
				//if(responseData.equals(GUIUtils.hexToAsciiV2(ConstantPowerSourceBofa.CMD_STOP_VOLT_CURRENT_IN_HEX))) {
				//if(responseData.equals(GUIUtils.hexToAsciiV2(ConstantPowerSourceBofa.ER_CMD_STOP_VOLT_CURRENT_POSITIVE_IN_HEX))) {
					status = true;
				//}else {
				//	status = false;
				//}
	
			}	
			
			
			if(isResponseExpected){
				while (retryCount>0 && responseStatus.equals(DeleteMeConstant.NO_RESPONSE)){
					retryCount--;
					//VI_SendStartCommand(CommandVI_PayLoad,SelectedPhase);
					ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessWithLength :retryCount<"+sourceThread +">:" + retryCount);
					//int TimeOutInSec= 60;vjhvjgv
					//SerialDataLDU lduData = VICI_ReadDataWithErrorResponse(ExpectedData.length(),ExpectedData,ExpectedError1Data,ExpectedError2Data,TimeOutInSec);
					//ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessWithLength :Hit2");
					if(getPwrSrcSpmObj().getRxMsgQ_PwrSrc().IsResponseReceived()) {
						if(getPwrSrcSpmObj().getRxMsgQ_PwrSrc().isExpectedResponseReceived()){
							ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessWithLength: <"+sourceThread +">:Ack Response Success");
							//String CurrentLDU_Data =lduData.getLDU_ReadSerialData();
							//ApplicationLauncher.logger.info("dutMsngrSendCommandProcessWithLength: VICI Received Data1:"+CurrentLDU_Data);
							//StripLDU_SerialData(lduData.getReceivedLength());
							responseStatus=DeleteMeConstant.SUCCESS_RESPONSE;
							getPwrSrcSpmObj().getRxMsgQ_PwrSrc().removeProcessedMsgFromQueue();
						}else if(getPwrSrcSpmObj().getRxMsgQ_PwrSrc().isExpectedErrorResponseReceived()){
							ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessWithLength <"+sourceThread +">:Ack Response Error");
							String CurrentReadData =getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
							//ApplicationLauncher.logger.info("dutMsngrSendCommandProcessWithLength: ErrorResponse Received:"+CurrentLDU_Data);
							//ApplicationLauncher.logger.info("dutMsngrSendCommandProcessWithLength: ExpectedError1Data:"+ExpectedError1Data);
							//StripLDU_SerialData(CurrentLDU_Data.length());
							
							responseStatus=CurrentReadData;
							getPwrSrcSpmObj().getRxMsgQ_PwrSrc().removeProcessedMsgFromQueue();
			
						}else if(getPwrSrcSpmObj().getRxMsgQ_PwrSrc().isUnknownResponseRecieved()){
							
							
							String CurrentReadData =getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
							ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessWithLength : isUnknownResponseRecieved : CurrentReadData<"+sourceThread +">: "+ CurrentReadData);
							responseStatus=CurrentReadData;
							getPwrSrcSpmObj().getRxMsgQ_PwrSrc().removeProcessedMsgFromQueue();
							if(responseStatus!=null) { //Gopi2
								ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessWithLength :Unexpected Message: dropping the message<"+sourceThread +">:"+responseStatus);
								ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessWithLength :Unexpected Message: dropping the message<"+sourceThread +">:"+GUIUtils.HexToString(responseStatus));
							}
							
						}
					}
					else{
						ApplicationLauncher.logger.info("dutMsngrSendCommandProcessWithLength : <"+sourceThread +">: Ack No Data Received");
					}
					
					
					ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessWithLength : <"+sourceThread +">test1: ");
					
/*					if ( ( !payLoadInHex.equals(ConstantPowerSourceBofa.CMD_STOP_VOLT_CURRENT_IN_HEX)) && 
						     ( !payLoadInHex.equals(ConstantPowerSourceBofa.CMD_STOP_CURRENT_IN_HEX)) ){
							if(ProjectExecutionController.getUserAbortedFlag()){
								retryCount = 0;
								responseStatus = DeleteMeConstant.ERROR_RESPONSE;
								ApplicationLauncher.logger.info("dutMsngrSendCommandProcessWithLength V2 <"+sourceThread +">: user aborted - detected");
							}
						}else if(!getPwrSrcSpmObj().isPwrSrcSerialStatusConnected()){
							retryCount = 0;
							responseStatus = DeleteMeConstant.ERROR_RESPONSE;
							ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessWithLength V2 <"+sourceThread +">: serial port - disconnected");
						
						}*/
				}
			}else{
				responseStatus=DeleteMeConstant.NO_RESPONSE;
				Sleep(200);
			}
		}else{
			ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessWithLength V2 <"+sourceThread +">: serial port not connected or disconnected");
			
		}
		//ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessWithLength : test2: ");
		if(getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage()==null) {
			ApplicationLauncher.logger.info("dutMsngrSendCommandProcessWithLength<"+sourceThread +">: last read null");
		}else {
			ApplicationLauncher.logger.info("dutMsngrSendCommandProcessWithLength: last read<"+sourceThread +">:"+getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage());
		}
		ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessWithLength <"+sourceThread +">:Exit");
		return responseStatus;
	}
	
	//==========================================================================================================================	



	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep2 :InterruptedException:"+ e.getMessage());
		}

	}
	//==========================================================================================================================	

	
}
