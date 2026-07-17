package com.tasnetwork.calibration.conveyor.serial.messenger;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.bay.DevSysEnergyMeter;
import com.tasnetwork.calibration.conveyor.bay.EIC_MegaOhmMeter;
import com.tasnetwork.calibration.conveyor.bay.Elmeasure_MultiMeter;
import com.tasnetwork.calibration.conveyor.bay.NewlandQRCodeScanner;
import com.tasnetwork.calibration.conveyor.bay.ir.IR_ReadResult;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmMegaOhmPm;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmQrScanner;
import com.tasnetwork.calibration.conveyor.util.GUIUtils;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.DeleteMeConstant;



public class MegaOhmPmMessenger {

	//DeviceDataManagerController displayDataObj =  new DeviceDataManagerController();
	SpmMegaOhmPm pwrSrcSpmObj =  new SpmMegaOhmPm();//null;//new DeviceDataManagerController().getSerialPortManagerPwrSrc_V2();
	
	public MegaOhmPmMessenger(SpmMegaOhmPm qrScannerSerialPort){
		this.pwrSrcSpmObj = qrScannerSerialPort;
	}
	
	public MegaOhmPmMessenger(){
		
	}
	//==========================================================================================================================	



	
	
	
    //==========================================================================================================================	
  	public Map<String,Object> sendReadCommandEicMegaOhmMeter(String slaveId){
  		ApplicationLauncher.logger.info("sendReadCommandEicMegaOhmMeter: Entry"); 
  		boolean status = false;
  		//boolean isResponseExpected = true;
  		
  		 int slaveIdValue = Integer.parseInt(slaveId, 16); // Convert hex string to integer
          
           String slaveIdHex = String.format("%02X", slaveIdValue); // Format as 2-character hex string

           // Combine all parts into the frame (without CRC)
           String frame = slaveIdHex + EIC_MegaOhmMeter.FUNCTION_CODE + EIC_MegaOhmMeter.REGISTER_ADDRESS + EIC_MegaOhmMeter.NUM_BYTES;

           // Calculate CRC for the frame
           EIC_MegaOhmMeter eic_MegaOhmMeter = new EIC_MegaOhmMeter();
           String crc = eic_MegaOhmMeter.calculateCRC(frame);

           // Combine the frame and CRC to get the final frame
         //  String commandFrame = frame + crc;
  		
  		String payLoadInHex = frame + crc;//NewlandQRCodeScanner.ANALOG_TRIGGER_SETTING ; //ANALOG_TRIGGER_SETTING;//startTestEndFrame ;xcvxc
  		ApplicationLauncher.logger.info("sendReadCommandEicMegaOhmMeter: payLoadInHex: " + payLoadInHex  );
  		
  /*		String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;

  		if (address == ConstantPowerSourceBofa.LDU_INT_BROADCAST_ADDRESS) {
  			 addressStr = ConstantPowerSourceBofa.LDU_HEX_BROADCAST_ADDRESS ;
  		 }*/
  		
  		int expectedDataLengthInHex = EIC_MegaOhmMeter.ER_LENGTH_ASCII;//slaveIdHex;//NewlandQRCodeScanner.EXPECTED_DATA_IN_HEX;
  		int timeDelayInMilliSec = 0;
  		String expectedDataInHex = slaveIdHex;//"06";//ConstantPowerSourceBofa.ER_LDU_STARTS_WITH + addressStr;
  		String responseData = "";
  		boolean isResponseExpected = true;
  		Map<String,Object> responseReturn = new HashMap<String,Object>();
  		responseReturn.put("status", false);
  		//SerialDM_Obj.WriteToSerialCommPwrSrc(ANALOG_TRIGGER_SETTING,ER_ANALOG_TRIGGER_SETTING);
  		String responseStatus = eicPanelMeterMsngrSendCommandProcessWithLength(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,expectedDataLengthInHex,isResponseExpected,"test-T2");
  		
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
  					ApplicationLauncher.logger.info("sendReadCommandEicMegaOhmMeter : no response expected success");
  					status = true;
  				}
  			}
  		}
  		return responseReturn;
  	}
  	
  //==================================================================================================================================


  	
  //==================================================================================================================================

	
	public SpmMegaOhmPm getPwrSrcSpmObj() {
		return pwrSrcSpmObj;
	}
	public void setPwrSrcSpmObj(SpmMegaOhmPm pwrSrcSpmObj) {
		this.pwrSrcSpmObj = pwrSrcSpmObj;
	}

	//==========================================================================================================================	

	//==========================================================================================================================	

	
	public String eicPanelMeterMsngrSendCommandProcess(String payLoadInHex, int timeDelayInMilliSec, 
			String expectedDataInHex,boolean isResponseExpected,String sourceThread){
		ApplicationLauncher.logger.debug("eicPanelMeterMsngrSendCommandProcess :Entry");
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
		
 
		
		//ApplicationLauncher.logger.debug("eicPanelMeterMsngrSendCommandProcess : expectedDataInHex: " + expectedDataInHex);
		String expectedData = "";
		if(isResponseExpected){
			expectedData = GUIUtils.hexToAsciiV2(expectedDataInHex);
		}
		
		boolean status = false;
		String rxMessageTerminator = GUIUtils.hexToAscii("0D0A");//ConstantPowerSourceBofa.END_BYTE);
		Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
		if(getPwrSrcSpmObj().isDeviceSerialStatusConnected()){
			responseReturn = getPwrSrcSpmObj().eicSendCommandProcess( payLoadInHex, timeDelayInMilliSec,
					expectedData,isResponseExpected, rxMessageTerminator, sourceThread);
			status = (boolean)responseReturn.get("status");
			if(status){
				String responseData = (String)responseReturn.get("result");
				ApplicationLauncher.logger.debug("eicPanelMeterMsngrSendCommandProcess : responseData <"+sourceThread +">: " + responseData);
				ApplicationLauncher.logger.debug("eicPanelMeterMsngrSendCommandProcess : responseData <"+sourceThread +"> hex : " + GUIUtils.asciiToHex(responseData));
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
					ApplicationLauncher.logger.debug("eicPanelMeterMsngrSendCommandProcess :retryCount<"+sourceThread +">:" + retryCount);
					//int TimeOutInSec= 60;vjhvjgv
					//SerialDataLDU lduData = VICI_ReadDataWithErrorResponse(ExpectedData.length(),ExpectedData,ExpectedError1Data,ExpectedError2Data,TimeOutInSec);
					//ApplicationLauncher.logger.debug("eicPanelMeterMsngrSendCommandProcess :Hit2");
					if(getPwrSrcSpmObj().getRxMsgQ_PwrSrc().IsResponseReceived()) {
						if(getPwrSrcSpmObj().getRxMsgQ_PwrSrc().isExpectedResponseReceived()){
							ApplicationLauncher.logger.debug("eicPanelMeterMsngrSendCommandProcess: <"+sourceThread +">:Ack Response Success");
							//String CurrentLDU_Data =lduData.getLDU_ReadSerialData();
							//ApplicationLauncher.logger.info("eicPanelMeterMsngrSendCommandProcess: VICI Received Data1:"+CurrentLDU_Data);
							//StripLDU_SerialData(lduData.getReceivedLength());
							responseStatus=DeleteMeConstant.SUCCESS_RESPONSE;
							getPwrSrcSpmObj().getRxMsgQ_PwrSrc().removeProcessedMsgFromQueue();
						}else if(getPwrSrcSpmObj().getRxMsgQ_PwrSrc().isExpectedErrorResponseReceived()){
							ApplicationLauncher.logger.debug("eicPanelMeterMsngrSendCommandProcess <"+sourceThread +">:Ack Response Error");
							String CurrentReadData =getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
							//ApplicationLauncher.logger.info("eicPanelMeterMsngrSendCommandProcess: ErrorResponse Received:"+CurrentLDU_Data);
							//ApplicationLauncher.logger.info("eicPanelMeterMsngrSendCommandProcess: ExpectedError1Data:"+ExpectedError1Data);
							//StripLDU_SerialData(CurrentLDU_Data.length());
							
							responseStatus=CurrentReadData;
							getPwrSrcSpmObj().getRxMsgQ_PwrSrc().removeProcessedMsgFromQueue();
			
						}else if(getPwrSrcSpmObj().getRxMsgQ_PwrSrc().isUnknownResponseRecieved()){
							
							
							String CurrentReadData =getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
							ApplicationLauncher.logger.debug("eicPanelMeterMsngrSendCommandProcess : isUnknownResponseRecieved : CurrentReadData<"+sourceThread +">: "+ CurrentReadData);
							responseStatus=CurrentReadData;
							getPwrSrcSpmObj().getRxMsgQ_PwrSrc().removeProcessedMsgFromQueue();
							if(responseStatus!=null) { //Gopi2
								ApplicationLauncher.logger.debug("eicPanelMeterMsngrSendCommandProcess :Unexpected Message: dropping the message<"+sourceThread +">:"+responseStatus);
								ApplicationLauncher.logger.debug("eicPanelMeterMsngrSendCommandProcess :Unexpected Message: dropping the message<"+sourceThread +">:"+GUIUtils.HexToString(responseStatus));
							}
							
						}
					}
					else{
						ApplicationLauncher.logger.info("eicPanelMeterMsngrSendCommandProcess : <"+sourceThread +">: Ack No Data Received");
					}
					
					
					ApplicationLauncher.logger.debug("eicPanelMeterMsngrSendCommandProcess : <"+sourceThread +">test1: ");
					
/*					if ( ( !payLoadInHex.equals(ConstantPowerSourceBofa.CMD_STOP_VOLT_CURRENT_IN_HEX)) && 
						     ( !payLoadInHex.equals(ConstantPowerSourceBofa.CMD_STOP_CURRENT_IN_HEX)) ){
							if(ProjectExecutionController.getUserAbortedFlag()){
								retryCount = 0;
								responseStatus = DeleteMeConstant.ERROR_RESPONSE;
								ApplicationLauncher.logger.info("eicPanelMeterMsngrSendCommandProcess V2 <"+sourceThread +">: user aborted - detected");
							}
						}else if(!getPwrSrcSpmObj().isPwrSrcSerialStatusConnected()){
							retryCount = 0;
							responseStatus = DeleteMeConstant.ERROR_RESPONSE;
							ApplicationLauncher.logger.debug("eicPanelMeterMsngrSendCommandProcess V2 <"+sourceThread +">: serial port - disconnected");
						
						}*/
				}
			}else{
				responseStatus=DeleteMeConstant.NO_RESPONSE;
				Sleep(200);
			}
		}else{
			ApplicationLauncher.logger.debug("eicPanelMeterMsngrSendCommandProcess V2 <"+sourceThread +">: serial port not connected or disconnected");
			
		}
		//ApplicationLauncher.logger.debug("eicPanelMeterMsngrSendCommandProcess : test2: ");
		if(getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage()==null) {
			ApplicationLauncher.logger.info("eicPanelMeterMsngrSendCommandProcess<"+sourceThread +">: last read null");
		}else {
			ApplicationLauncher.logger.info("eicPanelMeterMsngrSendCommandProcess: last read<"+sourceThread +">:"+getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage());
		}
		ApplicationLauncher.logger.debug("eicPanelMeterMsngrSendCommandProcess <"+sourceThread +">:Exit");
		return responseStatus;
	}
	//==========================================================================================================================	

	public String eicPanelMeterMsngrSendCommandProcessWithLength(String payLoadInHex, int timeDelayInMilliSec, 
			String expectedDataInHex,int expectedDataHexLength,boolean isResponseExpected,String sourceThread){
		ApplicationLauncher.logger.debug("eicPanelMeterMsngrSendCommandProcessWithLength :Entry");
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
		
		
		
		
		
	
		
		
		//ApplicationLauncher.logger.debug("bofaPowerSourceMsngrSendCommandProcess : expectedDataInHex: " + expectedDataInHex);
		String expectedData = "";
		if(isResponseExpected){
			expectedData = GUIUtils.hexToAsciiV2(expectedDataInHex);
		}
		
		boolean status = false;
		String rxMessageTerminator = GUIUtils.hexToAscii("0D0A");//ConstantPowerSourceBofa.END_BYTE);
		Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
		if(getPwrSrcSpmObj().isDeviceSerialStatusConnected()){
			responseReturn = getPwrSrcSpmObj().eicSendCommandProcessWithLength( payLoadInHex, timeDelayInMilliSec,
					expectedData,isResponseExpected, expectedDataHexLength, sourceThread);
			status = (boolean)responseReturn.get("status");
			if(status){
				String responseData = (String)responseReturn.get("result");
				ApplicationLauncher.logger.debug("eicPanelMeterMsngrSendCommandProcessWithLength : responseData <"+sourceThread +">: " + responseData);
				ApplicationLauncher.logger.debug("eicPanelMeterMsngrSendCommandProcessWithLength : responseData <"+sourceThread +"> hex : " + GUIUtils.asciiToHex(responseData));
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
					ApplicationLauncher.logger.debug("eicPanelMeterMsngrSendCommandProcessWithLength :retryCount<"+sourceThread +">:" + retryCount);
					//int TimeOutInSec= 60;vjhvjgv
					//SerialDataLDU lduData = VICI_ReadDataWithErrorResponse(ExpectedData.length(),ExpectedData,ExpectedError1Data,ExpectedError2Data,TimeOutInSec);
					//ApplicationLauncher.logger.debug("eicPanelMeterMsngrSendCommandProcessWithLength :Hit2");
					if(getPwrSrcSpmObj().getRxMsgQ_PwrSrc().IsResponseReceived()) {
						if(getPwrSrcSpmObj().getRxMsgQ_PwrSrc().isExpectedResponseReceived()){
							ApplicationLauncher.logger.debug("eicPanelMeterMsngrSendCommandProcessWithLength: <"+sourceThread +">:Ack Response Success");
							//String CurrentLDU_Data =lduData.getLDU_ReadSerialData();
							//ApplicationLauncher.logger.info("eicPanelMeterMsngrSendCommandProcessWithLength: VICI Received Data1:"+CurrentLDU_Data);
							//StripLDU_SerialData(lduData.getReceivedLength());
							responseStatus=DeleteMeConstant.SUCCESS_RESPONSE;
							getPwrSrcSpmObj().getRxMsgQ_PwrSrc().removeProcessedMsgFromQueue();
						}else if(getPwrSrcSpmObj().getRxMsgQ_PwrSrc().isExpectedErrorResponseReceived()){
							ApplicationLauncher.logger.debug("eicPanelMeterMsngrSendCommandProcessWithLength <"+sourceThread +">:Ack Response Error");
							String CurrentReadData =getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
							//ApplicationLauncher.logger.info("eicPanelMeterMsngrSendCommandProcessWithLength: ErrorResponse Received:"+CurrentLDU_Data);
							//ApplicationLauncher.logger.info("eicPanelMeterMsngrSendCommandProcessWithLength: ExpectedError1Data:"+ExpectedError1Data);
							//StripLDU_SerialData(CurrentLDU_Data.length());
							
							responseStatus=CurrentReadData;
							getPwrSrcSpmObj().getRxMsgQ_PwrSrc().removeProcessedMsgFromQueue();
			
						}else if(getPwrSrcSpmObj().getRxMsgQ_PwrSrc().isUnknownResponseRecieved()){
							
							
							String CurrentReadData =getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
							ApplicationLauncher.logger.debug("eicPanelMeterMsngrSendCommandProcessWithLength : isUnknownResponseRecieved : CurrentReadData<"+sourceThread +">: "+ CurrentReadData);
							responseStatus=CurrentReadData;
							getPwrSrcSpmObj().getRxMsgQ_PwrSrc().removeProcessedMsgFromQueue();
							if(responseStatus!=null) { //Gopi2
								ApplicationLauncher.logger.debug("eicPanelMeterMsngrSendCommandProcessWithLength :Unexpected Message: dropping the message<"+sourceThread +">:"+responseStatus);
								ApplicationLauncher.logger.debug("eicPanelMeterMsngrSendCommandProcessWithLength :Unexpected Message: dropping the message<"+sourceThread +">:"+GUIUtils.HexToString(responseStatus));
							}
							
						}
					}
					else{
						ApplicationLauncher.logger.info("eicPanelMeterMsngrSendCommandProcessWithLength : <"+sourceThread +">: Ack No Data Received");
					}
					
					
					ApplicationLauncher.logger.debug("eicPanelMeterMsngrSendCommandProcessWithLength : <"+sourceThread +">test1: ");
					
/*					if ( ( !payLoadInHex.equals(ConstantPowerSourceBofa.CMD_STOP_VOLT_CURRENT_IN_HEX)) && 
						     ( !payLoadInHex.equals(ConstantPowerSourceBofa.CMD_STOP_CURRENT_IN_HEX)) ){
							if(ProjectExecutionController.getUserAbortedFlag()){
								retryCount = 0;
								responseStatus = DeleteMeConstant.ERROR_RESPONSE;
								ApplicationLauncher.logger.info("eicPanelMeterMsngrSendCommandProcessWithLength V2 <"+sourceThread +">: user aborted - detected");
							}
						}else if(!getPwrSrcSpmObj().isPwrSrcSerialStatusConnected()){
							retryCount = 0;
							responseStatus = DeleteMeConstant.ERROR_RESPONSE;
							ApplicationLauncher.logger.debug("eicPanelMeterMsngrSendCommandProcessWithLength V2 <"+sourceThread +">: serial port - disconnected");
						
						}*/
				}
			}else{
				responseStatus=DeleteMeConstant.NO_RESPONSE;
				Sleep(200);
			}
		}else{
			ApplicationLauncher.logger.debug("eicPanelMeterMsngrSendCommandProcessWithLength V2 <"+sourceThread +">: serial port not connected or disconnected");
			
		}
		//ApplicationLauncher.logger.debug("eicPanelMeterMsngrSendCommandProcessWithLength : test2: ");
		if(getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage()==null) {
			ApplicationLauncher.logger.info("eicPanelMeterMsngrSendCommandProcessWithLength<"+sourceThread +">: last read null");
		}else {
			ApplicationLauncher.logger.info("eicPanelMeterMsngrSendCommandProcessWithLength: last read<"+sourceThread +">:"+getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage());
		}
		ApplicationLauncher.logger.debug("eicPanelMeterMsngrSendCommandProcessWithLength <"+sourceThread +">:Exit");
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
