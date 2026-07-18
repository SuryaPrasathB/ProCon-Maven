package com.tasnetwork.calibration.conveyor.serial.messenger;

import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.bay.DevSysEnergyMeter;
import com.tasnetwork.calibration.conveyor.bay.EIC_MegaOhmMeter;
import com.tasnetwork.calibration.conveyor.bay.Elmeasure_MultiMeter;
import com.tasnetwork.calibration.conveyor.bay.NewlandQRCodeScanner;
import com.tasnetwork.calibration.conveyor.bay.ir.IR_ReadResult;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmQrScanner;
import com.tasnetwork.calibration.conveyor.util.GUIUtils;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.DeleteMeConstant;

public class QrScannerMessenger {

	// DeviceDataManagerController displayDataObj = new
	// DeviceDataManagerController();
	SpmQrScanner pwrSrcSpmObj = new SpmQrScanner();// null;//new
													// DeviceDataManagerController().getSerialPortManagerPwrSrc_V2();

	public QrScannerMessenger(SpmQrScanner qrScannerSerialPort) {
		this.pwrSrcSpmObj = qrScannerSerialPort;
	}

	public QrScannerMessenger() {

	}
	// ==========================================================================================================================

	public boolean sendAnalogTrigger() {
		ApplicationLauncher.logger.info("sendVoltageCurrentStopOutputCommand: Entry");
		// frameVoltageCurrentStopOutputCommand();
		String payLoadInHex = NewlandQRCodeScanner.ANALOG_TRIGGER_SETTING;// ConstantPowerSourceBofa.CMD_STOP_VOLT_CURRENT_IN_HEX;//voltageCurrentStopOutputCmdFrame;//GUIUtils.StringToHex(voltageCurrentStopOutputCmdFrame
																			// );
		ApplicationLauncher.logger.info("sendVoltageCurrentStopOutputCommand : payLoadInHex: " + payLoadInHex);

		int timeDelayInMilliSec = 0;
		String expectedData = GUIUtils.hexToAscii(NewlandQRCodeScanner.GOOD_READ_EXPECTED_BEGIN_DATA_IN_HEX);// ConstantPowerSourceBofa.ER_STARTS_WITH);
		boolean isResponseExpected = true;
		boolean status = false;
		String rxMessageTerminator = GUIUtils.hexToAscii(DeleteMeConstant.HV_ER_TERMINATOR);// ConstantPowerSourceBofa.END_BYTE);
		String error1ExpectedDataInHex = NewlandQRCodeScanner.NOT_GOOD_READ_EXPECTED_DATA_IN_HEX;
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);
		responseReturn = getPwrSrcSpmObj().qrSendCommandProcess(payLoadInHex, timeDelayInMilliSec,
				expectedData, error1ExpectedDataInHex, isResponseExpected, rxMessageTerminator,
				"sendVoltageCurrentStopOutputCommand");
		status = (boolean) responseReturn.get("status");
		if (status) {
			String responseData = (String) responseReturn.get("result");
			ApplicationLauncher.logger.debug("sendVoltageCurrentStopOutputCommand : responseData : " + responseData);
			ApplicationLauncher.logger
					.debug("sendVoltageCurrentStopOutputCommand : responseData hex : " + GUIUtils.asciiToHex(""));// responseData));
			// if(responseData.equals(GUIUtils.hexToAsciiV2(ConstantPowerSourceBofa.CMD_STOP_VOLT_CURRENT_IN_HEX)))
			// {
			if (responseData.equals(GUIUtils.hexToAsciiV2(""))) {// ConstantPowerSourceBofa.ER_CMD_STOP_VOLT_CURRENT_POSITIVE_IN_HEX)))
																	// {
				status = true;
			} else {
				status = false;
			}

		}

		/*
		 * String responseStatus =
		 * BofaManager.getSerialDM_Obj().bofaPowerSourceMsngrSendCommandProcess(
		 * payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
		 * if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
		 * String CurrentReadData =
		 * BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage();//"";
		 * // BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
		 * status = processResponse(voltageCurrentStopOutputCmdFrame, CurrentReadData);
		 * }
		 */

		ApplicationLauncher.logger.info("sendVoltageCurrentStopOutputCommand: Exit");
		return status;
	}
	// ==========================================================================================================================

	public Map<String, Object> sendReadCommandQrCodeScanner() {
		ApplicationLauncher.logger.info("sendReadCommandQrCodeScanner: Entry");
		boolean status = false;
		// boolean isResponseExpected = true;

		String payLoadInHex = NewlandQRCodeScanner.ANALOG_TRIGGER_SETTING; // ANALOG_TRIGGER_SETTING;//startTestEndFrame
																			// ;xcvxc
		ApplicationLauncher.logger.info("send: payLoadInHex: " + payLoadInHex);

		/*
		 * String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address +
		 * ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;
		 * 
		 * if (address == ConstantPowerSourceBofa.LDU_INT_BROADCAST_ADDRESS) {
		 * addressStr = ConstantPowerSourceBofa.LDU_HEX_BROADCAST_ADDRESS ;
		 * }
		 */

		/*
		 * 
		 * 
		 * String expectedData =
		 * GUIUtils.hexToAscii(NewlandQRCodeScanner.GOOD_READ_EXPECTED_BEGIN_DATA_IN_HEX
		 * );//ConstantPowerSourceBofa.ER_STARTS_WITH);
		 * boolean isResponseExpected = true;
		 * boolean status = false;
		 * String rxMessageTerminator =
		 * GUIUtils.hexToAscii(DeleteMeConstant.HV_ER_TERMINATOR);//
		 * ConstantPowerSourceBofa.END_BYTE);
		 * String error1ExpectedDataInHex =
		 * NewlandQRCodeScanner.NOT_GOOD_READ_EXPECTED_DATA_IN_HEX;
		 */

		String expectedDataInHex = NewlandQRCodeScanner.GOOD_READ_EXPECTED_BEGIN_DATA_IN_HEX;// NOT_GOOD_READ_EXPECTED_DATA_IN_HEX;
		String error1ExpectedDataInHex = NewlandQRCodeScanner.NOT_GOOD_READ_EXPECTED_DATA_IN_HEX;
		int timeDelayInMilliSec = 0;
		// String expectedDataInHex = "06";//ConstantPowerSourceBofa.ER_LDU_STARTS_WITH
		// + addressStr;
		String responseData = "";
		boolean isResponseExpected = true;
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);
		// SerialDM_Obj.WriteToSerialCommPwrSrc(ANALOG_TRIGGER_SETTING,ER_ANALOG_TRIGGER_SETTING);
		String responseStatus = qrMsngrSendCommandProcess(payLoadInHex, timeDelayInMilliSec, expectedDataInHex,
				error1ExpectedDataInHex, isResponseExpected, "test-T1");

		if (responseStatus.equals(DeleteMeConstant.SUCCESS_RESPONSE)) {
			// if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
			responseData = getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
			responseData = GUIUtils.asciiToHex(responseData);

			status = true;
			responseReturn.put("status", true);
			responseReturn.put("responseData", responseData);
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
		return responseReturn;
	}

	public SpmQrScanner getPwrSrcSpmObj() {
		return pwrSrcSpmObj;
	}

	public void setPwrSrcSpmObj(SpmQrScanner pwrSrcSpmObj) {
		this.pwrSrcSpmObj = pwrSrcSpmObj;
	}

	// ==========================================================================================================================

	// ==========================================================================================================================

	public String qrMsngrSendCommandProcess(String payLoadInHex, int timeDelayInMilliSec,
			String expectedDataInHex, String error1ExpectedDataInHex, boolean isResponseExpected, String sourceThread) {
		ApplicationLauncher.logger.debug("qrMsngrSendCommandProcess :Entry");
		// boolean status=false;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
			String presentTimeStamp = LocalDateTime.now().format(formatter);
			sourceThread = getPwrSrcSpmObj().getMyTraceName() + "-" + presentTimeStamp;
		} catch (Exception e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("qrMsngrSendCommandProcess :Exception:" + e.getMessage());
		}
		String responseStatus = DeleteMeConstant.NO_RESPONSE;
		int retryCount = 5;// 10//ConstantHV_Src.HV_READ_RESPONSE_RETRY_COUNT+10;//1;

		// String ExpectedError1Data =
		// "";//ConstantPrimaryVICI_Meter.VI_CMD_ERROR_RESPONSE_HDR;//+ConstantPrimaryVICI_Meter.VI_ER_TERMINATOR;

		// String ExpectedError2Data="";

		/*
		 * powerSourceSetExpectedData(expectedDataInHex);
		 * powerSourceSetExpectedError1Data(ExpectedError1Data);
		 * powerSourceSetExpectedError2Data(ExpectedError2Data);
		 * powerSourceSetRxMessageTerminator(terminatorInHex);
		 * 
		 * powerSourceResetResponseFlag();
		 */

		getPwrSrcSpmObj().getRxMsgQ_PwrSrc().clearLastReadMessage();
		// powerSourceSendCommand(payLoadInHex,timeDelayInMilliSec);

		// ApplicationLauncher.logger.debug("qrMsngrSendCommandProcess :
		// expectedDataInHex: " + expectedDataInHex);
		String expectedData = "";
		String error1ExpectedData = "";
		if (isResponseExpected) {
			expectedData = GUIUtils.hexToAsciiV2(expectedDataInHex);
			error1ExpectedData = GUIUtils.hexToAsciiV2(error1ExpectedDataInHex);
		}

		boolean status = false;
		String rxMessageTerminator = GUIUtils.hexToAscii("0D0A");// ConstantPowerSourceBofa.END_BYTE);
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);
		if (getPwrSrcSpmObj().isDeviceSerialStatusConnected()) {
			responseReturn = getPwrSrcSpmObj().qrSendCommandProcess(payLoadInHex, timeDelayInMilliSec,
					expectedData, error1ExpectedData, isResponseExpected, rxMessageTerminator, sourceThread);
			status = (boolean) responseReturn.get("status");
			if (status) {
				String responseData = (String) responseReturn.get("result");
				ApplicationLauncher.logger
						.debug("qrMsngrSendCommandProcess : responseData <" + sourceThread + ">: " + responseData);
				ApplicationLauncher.logger.debug("qrMsngrSendCommandProcess : responseData <" + sourceThread
						+ "> hex : " + GUIUtils.asciiToHex(responseData));
				// if(responseData.equals(GUIUtils.hexToAsciiV2(ConstantPowerSourceBofa.CMD_STOP_VOLT_CURRENT_IN_HEX)))
				// {
				// if(responseData.equals(GUIUtils.hexToAsciiV2(ConstantPowerSourceBofa.ER_CMD_STOP_VOLT_CURRENT_POSITIVE_IN_HEX)))
				// {
				status = true;
				// }else {
				// status = false;
				// }

			}

			if (isResponseExpected) {
				while (retryCount > 0 && responseStatus.equals(DeleteMeConstant.NO_RESPONSE)) {
					retryCount--;
					// VI_SendStartCommand(CommandVI_PayLoad,SelectedPhase);
					ApplicationLauncher.logger
							.debug("qrMsngrSendCommandProcess :retryCount<" + sourceThread + ">:" + retryCount);
					// int TimeOutInSec= 60;vjhvjgv
					// SerialDataLDU lduData =
					// VICI_ReadDataWithErrorResponse(ExpectedData.length(),ExpectedData,ExpectedError1Data,ExpectedError2Data,TimeOutInSec);
					// ApplicationLauncher.logger.debug("qrMsngrSendCommandProcess :Hit2");
					if (getPwrSrcSpmObj().getRxMsgQ_PwrSrc().IsResponseReceived()) {
						if (getPwrSrcSpmObj().getRxMsgQ_PwrSrc().isExpectedResponseReceived()) {
							ApplicationLauncher.logger
									.debug("qrMsngrSendCommandProcess: <" + sourceThread + ">:Ack Response Success");
							// String CurrentLDU_Data =lduData.getLDU_ReadSerialData();
							// ApplicationLauncher.logger.info("qrMsngrSendCommandProcess: VICI Received
							// Data1:"+CurrentLDU_Data);
							// StripLDU_SerialData(lduData.getReceivedLength());
							responseStatus = DeleteMeConstant.SUCCESS_RESPONSE;
							getPwrSrcSpmObj().getRxMsgQ_PwrSrc().removeProcessedMsgFromQueue();
						} else if (getPwrSrcSpmObj().getRxMsgQ_PwrSrc().isExpectedErrorResponseReceived()) {
							ApplicationLauncher.logger
									.debug("qrMsngrSendCommandProcess <" + sourceThread + ">:Ack Response Error");
							String CurrentReadData = getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
							// ApplicationLauncher.logger.info("qrMsngrSendCommandProcess: ErrorResponse
							// Received:"+CurrentLDU_Data);
							// ApplicationLauncher.logger.info("qrMsngrSendCommandProcess:
							// ExpectedError1Data:"+ExpectedError1Data);
							// StripLDU_SerialData(CurrentLDU_Data.length());
							responseStatus = DeleteMeConstant.SUCCESS_RESPONSE;
							// responseStatus=CurrentReadData;
							getPwrSrcSpmObj().getRxMsgQ_PwrSrc().removeProcessedMsgFromQueue();

						} else if (getPwrSrcSpmObj().getRxMsgQ_PwrSrc().isUnknownResponseRecieved()) {

							String CurrentReadData = getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
							ApplicationLauncher.logger
									.debug("qrMsngrSendCommandProcess : isUnknownResponseRecieved : CurrentReadData<"
											+ sourceThread + ">: " + CurrentReadData);
							responseStatus = CurrentReadData;
							getPwrSrcSpmObj().getRxMsgQ_PwrSrc().removeProcessedMsgFromQueue();
							if (responseStatus != null) { // Gopi2
								ApplicationLauncher.logger
										.debug("qrMsngrSendCommandProcess :Unexpected Message: dropping the message<"
												+ sourceThread + ">:" + responseStatus);
								ApplicationLauncher.logger
										.debug("qrMsngrSendCommandProcess :Unexpected Message: dropping the message<"
												+ sourceThread + ">:" + GUIUtils.HexToString(responseStatus));
							}

						}
					} else {
						ApplicationLauncher.logger
								.info("qrMsngrSendCommandProcess : <" + sourceThread + ">: Ack No Data Received");
					}

					ApplicationLauncher.logger.debug("qrMsngrSendCommandProcess : <" + sourceThread + ">test1: ");

					/*
					 * if ( (
					 * !payLoadInHex.equals(ConstantPowerSourceBofa.CMD_STOP_VOLT_CURRENT_IN_HEX))
					 * &&
					 * ( !payLoadInHex.equals(ConstantPowerSourceBofa.CMD_STOP_CURRENT_IN_HEX)) ){
					 * if(BayUtils.isUserAborted()){
					 * retryCount = 0;
					 * responseStatus = DeleteMeConstant.ERROR_RESPONSE;
					 * ApplicationLauncher.logger.info("qrMsngrSendCommandProcess V2 <"+sourceThread
					 * +">: user aborted - detected");
					 * }
					 * }else if(!getPwrSrcSpmObj().isPwrSrcSerialStatusConnected()){
					 * retryCount = 0;
					 * responseStatus = DeleteMeConstant.ERROR_RESPONSE;
					 * ApplicationLauncher.logger.debug("qrMsngrSendCommandProcess V2 <"
					 * +sourceThread +">: serial port - disconnected");
					 * 
					 * }
					 */
				}
			} else {
				responseStatus = DeleteMeConstant.NO_RESPONSE;
				Sleep(200);
			}
		} else {
			ApplicationLauncher.logger.debug(
					"qrMsngrSendCommandProcess V2 <" + sourceThread + ">: serial port not connected or disconnected");

		}
		// ApplicationLauncher.logger.debug("qrMsngrSendCommandProcess : test2: ");
		if (getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage() == null) {
			ApplicationLauncher.logger.info("qrMsngrSendCommandProcess<" + sourceThread + ">: last read null");
		} else {
			ApplicationLauncher.logger.info("qrMsngrSendCommandProcess: last read<" + sourceThread + ">:"
					+ getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage());
		}
		ApplicationLauncher.logger.debug("qrMsngrSendCommandProcess <" + sourceThread + ">:Exit");
		return responseStatus;
	}
	// ==========================================================================================================================

	public String qrMsngrSendCommandProcessWithLength(String payLoadInHex, int timeDelayInMilliSec,
			String expectedDataInHex, int expectedDataHexLength, boolean isResponseExpected, String sourceThread) {
		ApplicationLauncher.logger.debug("qrMsngrSendCommandProcessWithLength :Entry");
		// boolean status=false;

		String responseStatus = DeleteMeConstant.NO_RESPONSE;
		int retryCount = 5;// 10//ConstantHV_Src.HV_READ_RESPONSE_RETRY_COUNT+10;//1;

		// String ExpectedError1Data =
		// "";//ConstantPrimaryVICI_Meter.VI_CMD_ERROR_RESPONSE_HDR;//+ConstantPrimaryVICI_Meter.VI_ER_TERMINATOR;

		// String ExpectedError2Data="";

		/*
		 * powerSourceSetExpectedData(expectedDataInHex);
		 * powerSourceSetExpectedError1Data(ExpectedError1Data);
		 * powerSourceSetExpectedError2Data(ExpectedError2Data);
		 * powerSourceSetRxMessageTerminator(terminatorInHex);
		 * 
		 * powerSourceResetResponseFlag();
		 */

		getPwrSrcSpmObj().getRxMsgQ_PwrSrc().clearLastReadMessage();
		// powerSourceSendCommand(payLoadInHex,timeDelayInMilliSec);

		// ApplicationLauncher.logger.debug("bofaPowerSourceMsngrSendCommandProcess :
		// expectedDataInHex: " + expectedDataInHex);
		String expectedData = "";
		if (isResponseExpected) {
			expectedData = GUIUtils.hexToAsciiV2(expectedDataInHex);
		}

		boolean status = false;
		String rxMessageTerminator = GUIUtils.hexToAscii("0D0A");// ConstantPowerSourceBofa.END_BYTE);
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);
		if (getPwrSrcSpmObj().isDeviceSerialStatusConnected()) {
			responseReturn = getPwrSrcSpmObj().qrSendCommandProcessWithLength(payLoadInHex, timeDelayInMilliSec,
					expectedData, isResponseExpected, expectedDataHexLength, sourceThread);
			status = (boolean) responseReturn.get("status");
			if (status) {
				String responseData = (String) responseReturn.get("result");
				ApplicationLauncher.logger.debug(
						"qrMsngrSendCommandProcessWithLength : responseData <" + sourceThread + ">: " + responseData);
				ApplicationLauncher.logger.debug("qrMsngrSendCommandProcessWithLength : responseData <" + sourceThread
						+ "> hex : " + GUIUtils.asciiToHex(responseData));
				// if(responseData.equals(GUIUtils.hexToAsciiV2(ConstantPowerSourceBofa.CMD_STOP_VOLT_CURRENT_IN_HEX)))
				// {
				// if(responseData.equals(GUIUtils.hexToAsciiV2(ConstantPowerSourceBofa.ER_CMD_STOP_VOLT_CURRENT_POSITIVE_IN_HEX)))
				// {
				status = true;
				// }else {
				// status = false;
				// }

			}

			if (isResponseExpected) {
				while (retryCount > 0 && responseStatus.equals(DeleteMeConstant.NO_RESPONSE)) {
					retryCount--;
					// VI_SendStartCommand(CommandVI_PayLoad,SelectedPhase);
					ApplicationLauncher.logger.debug(
							"qrMsngrSendCommandProcessWithLength :retryCount<" + sourceThread + ">:" + retryCount);
					// int TimeOutInSec= 60;vjhvjgv
					// SerialDataLDU lduData =
					// VICI_ReadDataWithErrorResponse(ExpectedData.length(),ExpectedData,ExpectedError1Data,ExpectedError2Data,TimeOutInSec);
					// ApplicationLauncher.logger.debug("qrMsngrSendCommandProcessWithLength
					// :Hit2");
					if (getPwrSrcSpmObj().getRxMsgQ_PwrSrc().IsResponseReceived()) {
						if (getPwrSrcSpmObj().getRxMsgQ_PwrSrc().isExpectedResponseReceived()) {
							ApplicationLauncher.logger.debug(
									"qrMsngrSendCommandProcessWithLength: <" + sourceThread + ">:Ack Response Success");
							// String CurrentLDU_Data =lduData.getLDU_ReadSerialData();
							// ApplicationLauncher.logger.info("qrMsngrSendCommandProcessWithLength: VICI
							// Received Data1:"+CurrentLDU_Data);
							// StripLDU_SerialData(lduData.getReceivedLength());
							responseStatus = DeleteMeConstant.SUCCESS_RESPONSE;
							getPwrSrcSpmObj().getRxMsgQ_PwrSrc().removeProcessedMsgFromQueue();
						} else if (getPwrSrcSpmObj().getRxMsgQ_PwrSrc().isExpectedErrorResponseReceived()) {
							ApplicationLauncher.logger.debug(
									"qrMsngrSendCommandProcessWithLength <" + sourceThread + ">:Ack Response Error");
							String CurrentReadData = getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
							// ApplicationLauncher.logger.info("qrMsngrSendCommandProcessWithLength:
							// ErrorResponse Received:"+CurrentLDU_Data);
							// ApplicationLauncher.logger.info("qrMsngrSendCommandProcessWithLength:
							// ExpectedError1Data:"+ExpectedError1Data);
							// StripLDU_SerialData(CurrentLDU_Data.length());

							responseStatus = CurrentReadData;
							getPwrSrcSpmObj().getRxMsgQ_PwrSrc().removeProcessedMsgFromQueue();

						} else if (getPwrSrcSpmObj().getRxMsgQ_PwrSrc().isUnknownResponseRecieved()) {

							String CurrentReadData = getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
							ApplicationLauncher.logger.debug(
									"qrMsngrSendCommandProcessWithLength : isUnknownResponseRecieved : CurrentReadData<"
											+ sourceThread + ">: " + CurrentReadData);
							responseStatus = CurrentReadData;
							getPwrSrcSpmObj().getRxMsgQ_PwrSrc().removeProcessedMsgFromQueue();
							if (responseStatus != null) { // Gopi2
								ApplicationLauncher.logger.debug(
										"qrMsngrSendCommandProcessWithLength :Unexpected Message: dropping the message<"
												+ sourceThread + ">:" + responseStatus);
								ApplicationLauncher.logger.debug(
										"qrMsngrSendCommandProcessWithLength :Unexpected Message: dropping the message<"
												+ sourceThread + ">:" + GUIUtils.HexToString(responseStatus));
							}

						}
					} else {
						ApplicationLauncher.logger.info(
								"qrMsngrSendCommandProcessWithLength : <" + sourceThread + ">: Ack No Data Received");
					}

					ApplicationLauncher.logger
							.debug("qrMsngrSendCommandProcessWithLength : <" + sourceThread + ">test1: ");

					/*
					 * if ( (
					 * !payLoadInHex.equals(ConstantPowerSourceBofa.CMD_STOP_VOLT_CURRENT_IN_HEX))
					 * &&
					 * ( !payLoadInHex.equals(ConstantPowerSourceBofa.CMD_STOP_CURRENT_IN_HEX)) ){
					 * if(BayUtils.isUserAborted()){
					 * retryCount = 0;
					 * responseStatus = DeleteMeConstant.ERROR_RESPONSE;
					 * ApplicationLauncher.logger.info("qrMsngrSendCommandProcessWithLength V2 <"
					 * +sourceThread +">: user aborted - detected");
					 * }
					 * }else if(!getPwrSrcSpmObj().isPwrSrcSerialStatusConnected()){
					 * retryCount = 0;
					 * responseStatus = DeleteMeConstant.ERROR_RESPONSE;
					 * ApplicationLauncher.logger.debug("qrMsngrSendCommandProcessWithLength V2 <"
					 * +sourceThread +">: serial port - disconnected");
					 * 
					 * }
					 */
				}
			} else {
				responseStatus = DeleteMeConstant.NO_RESPONSE;
				Sleep(200);
			}
		} else {
			ApplicationLauncher.logger.debug("qrMsngrSendCommandProcessWithLength V2 <" + sourceThread
					+ ">: serial port not connected or disconnected");

		}
		// ApplicationLauncher.logger.debug("qrMsngrSendCommandProcessWithLength :
		// test2: ");
		if (getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage() == null) {
			ApplicationLauncher.logger
					.info("qrMsngrSendCommandProcessWithLength<" + sourceThread + ">: last read null");
		} else {
			ApplicationLauncher.logger.info("qrMsngrSendCommandProcessWithLength: last read<" + sourceThread + ">:"
					+ getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage());
		}
		ApplicationLauncher.logger.debug("qrMsngrSendCommandProcessWithLength <" + sourceThread + ">:Exit");
		return responseStatus;
	}

	// ==========================================================================================================================

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep2 :InterruptedException:" + e.getMessage());
		}

	}
	// ==========================================================================================================================

}
