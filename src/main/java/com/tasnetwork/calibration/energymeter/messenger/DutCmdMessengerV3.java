package com.tasnetwork.calibration.energymeter.messenger;

import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantDut;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceBofa;
import com.tasnetwork.calibration.energymeter.constant.DeleteMeConstant;
import com.tasnetwork.calibration.energymeter.deployment.DutResponse;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.serial.portmanagerV2.SerialPortManagerDutCmd_V3;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;
import com.tasnetwork.spring.orm.model.DutCommand;

public class DutCmdMessengerV3 {

	DeviceDataManagerController displayDataObj = new DeviceDataManagerController();
	SerialPortManagerDutCmd_V3 dutCmdSpmObj = new SerialPortManagerDutCmd_V3();// DeviceDataManagerController().getSerialPortManagerDutCmd_V3();

	public DutResponse dutMsngrSendCommandProcess(String payLoadInHex, int timeDelayInMilliSec,
			String expectedDataInHex, boolean isResponseExpected, String sourceThread) {
		ApplicationLauncher.logger.debug("dutMsngrSendCommandProcess :Entry");
		// boolean status=false;
		DutResponse dutResponse = new DutResponse();
		String responseStatus = DeleteMeConstant.NO_RESPONSE;
		dutResponse.setResponseData(responseStatus);
		int expectedRetryCount = 1;// 5;//10//ConstantHV_Src.HV_READ_RESPONSE_RETRY_COUNT+10;//1;

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

		getDutCmdSpmObj().getRxMsgQ_Dut().clearLastReadMessage();
		// powerSourceSendCommand(payLoadInHex,timeDelayInMilliSec);

		// ApplicationLauncher.logger.debug("dutMsngrSendCommandProcess :
		// expectedDataInHex: " + expectedDataInHex);
		String expectedData = "";
		if (isResponseExpected) {
			expectedData = GuiUtils.hexToAsciiV2(expectedDataInHex);
		}

		boolean status = false;
		String rxMessageTerminatorInHex = ConstantDut.ER_TERMINATOR;// GuiUtils.hexToAscii(ConstantPowerSourceBofa.END_BYTE);
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		if (getDutCmdSpmObj().isDutSerialStatusConnected()) {
			responseReturn = getDutCmdSpmObj().dutSendCommandProcess(payLoadInHex, timeDelayInMilliSec,
					expectedData, isResponseExpected, rxMessageTerminatorInHex, sourceThread);
			status = (boolean) responseReturn.get("status");
			if (status) {
				String responseData = (String) responseReturn.get("result");
				ApplicationLauncher.logger
						.debug("dutMsngrSendCommandProcess : responseData <" + sourceThread + ">: " + responseData);
				// ApplicationLauncher.logger.debug("dutMsngrSendCommandProcess : responseData
				// <"+sourceThread +">hex : " + GuiUtils.asciiToHex(responseData));
				// if(responseData.equals(GuiUtils.hexToAsciiV2(ConstantPowerSourceBofa.CMD_STOP_VOLT_CURRENT_IN_HEX)))
				// {
				// if(responseData.equals(GuiUtils.hexToAsciiV2(ConstantPowerSourceBofa.ER_CMD_STOP_VOLT_CURRENT_POSITIVE_IN_HEX)))
				// {
				if (responseData.equals(expectedData)) {

					status = true;
				} else {
					status = false;
				}

			}

			if (isResponseExpected) {
				while (expectedRetryCount > 0 && responseStatus.equals(DeleteMeConstant.NO_RESPONSE)) {
					expectedRetryCount--;
					// VI_SendStartCommand(CommandVI_PayLoad,SelectedPhase);
					ApplicationLauncher.logger.debug("dutMsngrSendCommandProcess :expectedRetryCount<" + sourceThread
							+ ">:" + expectedRetryCount);
					// int TimeOutInSec= 60;vjhvjgv
					// SerialDataLDU lduData =
					// VICI_ReadDataWithErrorResponse(ExpectedData.length(),ExpectedData,ExpectedError1Data,ExpectedError2Data,TimeOutInSec);
					// ApplicationLauncher.logger.debug("dutMsngrSendCommandProcess :Hit2");
					if (getDutCmdSpmObj().getRxMsgQ_Dut().IsResponseReceived()) {
						if (getDutCmdSpmObj().getRxMsgQ_Dut().isExpectedResponseReceived()) {
							ApplicationLauncher.logger
									.debug("dutMsngrSendCommandProcess: <" + sourceThread + ">:Ack Response Success");
							// String CurrentLDU_Data =lduData.getLDU_ReadSerialData();
							// ApplicationLauncher.logger.info("dutMsngrSendCommandProcess: VICI Received
							// Data1:"+CurrentLDU_Data);
							// StripLDU_SerialData(lduData.getReceivedLength());
							responseStatus = DeleteMeConstant.SUCCESS_RESPONSE;
							dutResponse.setResponseData(getDutCmdSpmObj().getRxMsgQ_Dut().getLastReadMessage());
							getDutCmdSpmObj().getRxMsgQ_Dut().removeProcessedMsgFromQueue();
							// dutResponse.setResponseData(responseStatus);
							dutResponse.setStatus(true);
						} else if (getDutCmdSpmObj().getRxMsgQ_Dut().isExpectedErrorResponseReceived()) {
							ApplicationLauncher.logger
									.debug("dutMsngrSendCommandProcess <" + sourceThread + ">:Ack Response Error");
							String CurrentReadData = getDutCmdSpmObj().getRxMsgQ_Dut().getLastReadMessage();
							// ApplicationLauncher.logger.info("dutMsngrSendCommandProcess: ErrorResponse
							// Received:"+CurrentLDU_Data);
							// ApplicationLauncher.logger.info("dutMsngrSendCommandProcess:
							// ExpectedError1Data:"+ExpectedError1Data);
							// StripLDU_SerialData(CurrentLDU_Data.length());

							responseStatus = CurrentReadData;
							dutResponse.setResponseData(responseStatus);
							dutResponse.setStatus(false);
							getDutCmdSpmObj().getRxMsgQ_Dut().removeProcessedMsgFromQueue();

						} else if (getDutCmdSpmObj().getRxMsgQ_Dut().isUnknownResponseRecieved()) {

							String CurrentReadData = getDutCmdSpmObj().getRxMsgQ_Dut().getLastReadMessage();
							ApplicationLauncher.logger
									.debug("dutMsngrSendCommandProcess : isUnknownResponseRecieved : CurrentReadData<"
											+ sourceThread + ">: " + CurrentReadData);
							responseStatus = CurrentReadData;
							dutResponse.setResponseData(responseStatus);
							dutResponse.setStatus(false);
							getDutCmdSpmObj().getRxMsgQ_Dut().removeProcessedMsgFromQueue();
							if (responseStatus != null) { // Gopi2
								ApplicationLauncher.logger
										.debug("dutMsngrSendCommandProcess :Unexpected Message: dropping the message<"
												+ sourceThread + ">:" + responseStatus);
								ApplicationLauncher.logger
										.debug("dutMsngrSendCommandProcess :Unexpected Message: dropping the message<"
												+ sourceThread + ">:" + GuiUtils.HexToString(responseStatus));
							}

						}
					} else {
						ApplicationLauncher.logger
								.info("dutMsngrSendCommandProcess : <" + sourceThread + ">: Ack No Data Received");
					}

					// ApplicationLauncher.logger.debug("dutMsngrSendCommandProcess :
					// <"+sourceThread +">test1: ");

					// if ( (
					// !payLoadInHex.equals(ConstantPowerSourceBofa.CMD_STOP_VOLT_CURRENT_IN_HEX))
					// &&
					// ( !payLoadInHex.equals(ConstantPowerSourceBofa.CMD_STOP_CURRENT_IN_HEX)) ){
					if (BayUtils.isUserAborted()) {
						expectedRetryCount = 0;
						responseStatus = DeleteMeConstant.ERROR_RESPONSE;
						dutResponse.setResponseData(responseStatus);
						dutResponse.setStatus(false);
						ApplicationLauncher.logger
								.info("dutMsngrSendCommandProcess V2 <" + sourceThread + ">: user aborted - detected");
					}
					// }else if(!getDutCmdSpmObj().isDutSerialStatusConnected()){
					// retryCount = 0;
					// responseStatus = DeleteMeConstant.ERROR_RESPONSE;
					// ApplicationLauncher.logger.debug("dutMsngrSendCommandProcess V2
					// <"+sourceThread +">: serial port - disconnected");

					// }
				}
			} else {
				responseStatus = DeleteMeConstant.NO_RESPONSE;
				dutResponse.setResponseData(responseStatus);
				dutResponse.setStatus(false);
				Sleep(200);
			}
		} else {
			ApplicationLauncher.logger.debug(
					"dutMsngrSendCommandProcess V2 <" + sourceThread + ">: serial port not connected or disconnected");

		}
		// ApplicationLauncher.logger.debug("dutMsngrSendCommandProcess : test2: ");
		if (getDutCmdSpmObj().getRxMsgQ_Dut().getLastReadMessage() == null) {
			ApplicationLauncher.logger.info("dutMsngrSendCommandProcess<" + sourceThread + ">: last read null");
		} else {
			ApplicationLauncher.logger.info("dutMsngrSendCommandProcess: last read<" + sourceThread + ">:"
					+ getDutCmdSpmObj().getRxMsgQ_Dut().getLastReadMessage());
		}
		ApplicationLauncher.logger.debug("dutMsngrSendCommandProcess <" + sourceThread + ">:Exit");
		return dutResponse;
	}

	public DutResponse dutMsngrSendCommandProcessV2(int dutPositionNo, DutCommand dutCommand, String sourceThread) {

		/*
		 * String payLoadInHex, int timeDelayInMilliSec,
		 * String expectedDataInHex,boolean isResponseExpected,String sourceThread){
		 */
		ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessV2 :Entry");
		// boolean status=false;
		DutResponse dutResponse = new DutResponse();
		String responseStatus = DeleteMeConstant.NO_RESPONSE;
		dutResponse.setResponseData(responseStatus);
		int expectedRetryCount = 1;// 5;//10//ConstantHV_Src.HV_READ_RESPONSE_RETRY_COUNT+10;//1;

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

		getDutCmdSpmObj().getRxMsgQ_Dut().clearLastReadMessage();
		// powerSourceSendCommand(payLoadInHex,timeDelayInMilliSec);

		String expectedResponse = dutCommand.getResponseExpectedData();// ConstantDut.ER_MTR_RTC_READ;
		String expectedResponseInHex = "";
		String targetCommand = dutCommand.getTargetCmd();
		/*
		 * if(isDataAppend) {
		 * targetCommand = targetCommand + appendData;
		 * }
		 */
		String targetCommandInAscii = "";

		// String timeOutStr = DisplayDataObj.getDutResponseTimeOutInSec();
		int haltTimeInSec = dutCommand.getHaltTimeInSec();
		boolean commandInHexMode = dutCommand.isTargetCmdInHex();// .isDutCommandInHexMode();
		boolean dutResponseMandatory = dutCommand.isResponseMandatory();
		String serialNoSourceType = dutCommand.getSerialNoSourceType();

		boolean dutWriteSerialNoToDutEnabled = dutCommand.isWriteSerialNoToDut();
		boolean dutReadSerialNoFromDutEnabled = dutCommand.isReadSerialNoFromDut();
		if (commandInHexMode) {
			targetCommandInAscii = GuiUtils.hexToAsciiV2(targetCommand);
			expectedResponseInHex = expectedResponse;
			// expectedResponse = GuiUtils.asciiToHex(expectedResponse);
		} else {
			targetCommandInAscii = targetCommand;
			expectedResponseInHex = GuiUtils.asciiToHex(expectedResponse);
		}

		String commandTerminator = dutCommand.getTargetCmdTerminator();// .getDutTargetCommandTerminator();
		String commandTerminatorInAscii = "";

		boolean commandTerminatorInHexMode = dutCommand.isTargetCmdTerminatorInHex();// .isDutCommandTerminatorInHexMode();

		if (commandTerminatorInHexMode) {
			commandTerminatorInAscii = GuiUtils.hexToAsciiV2(commandTerminator);
		} else {
			commandTerminatorInAscii = commandTerminator;
		}
		String dutSerialNumberInAscii = "";
		if (dutWriteSerialNoToDutEnabled) {
			dutSerialNumberInAscii = DeviceDataManagerController.getDutSerialNumberMap(dutPositionNo);
			ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessV2 : dutSerialNumberInAscii : <"
					+ dutSerialNumberInAscii + "> : position id : " + dutPositionNo);
		}

		boolean dutWriteRtcToDutEnabled = dutCommand.isWriteRtcToDut();
		boolean dutReadRtcFromDutEnabled = dutCommand.isReadRtcFromDut();

		String rtcInAscii = "";
		boolean rtcConvertTotHex = dutCommand.isRtcConvertToHex();
		if (dutWriteRtcToDutEnabled) {
			String rtcDateTimeFormat = dutCommand.getRtcDateTimeFormat();
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(rtcDateTimeFormat);
			String formattedTime = now.format(formatter);
			ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessV2 : formattedTime : <" + formattedTime
					+ "> : position id : " + dutPositionNo);
			if (rtcConvertTotHex) {
				String formatedTimeInHex = bindTwoCharToHex(formattedTime);
				ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessV2 : formatedTimeInHex : <"
						+ formatedTimeInHex + "> : position id : " + dutPositionNo);
				rtcInAscii = GuiUtils.hexToAsciiV2(formatedTimeInHex);
				ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessV2 : rtcInAscii-binding : <" + rtcInAscii
						+ "> : position id : " + dutPositionNo);
			} else {
				rtcInAscii = formattedTime;
				ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessV2 : rtcInAscii-2 : <" + rtcInAscii
						+ "> : position id : " + dutPositionNo);
			}

		}

		String absoluteCommandInAscii = targetCommandInAscii + dutSerialNumberInAscii + rtcInAscii
				+ commandTerminatorInAscii;
		String absoluteCommandInHex = GuiUtils.asciiToHex(absoluteCommandInAscii);
		ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessV2 : absoluteCommandInHex : <"
				+ absoluteCommandInHex + "> : position id : " + dutPositionNo);

		ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessV2 : absoluteCommandInAscii : "
				+ absoluteCommandInAscii + " : position id : " + dutPositionNo);
		ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessV2 : expectedResponseInHex length: "
				+ expectedResponseInHex.length() + " : position id : " + dutPositionNo);
		String expectedResponseTerminatorInHex = "";
		if (dutResponseMandatory) {

			if (dutCommand.getResponseTerminatorInHex()) {// .isDutResponseTerminatorInHexMode()) {
				expectedResponseTerminatorInHex = dutCommand.getResponseTerminator();// .getDutResponseTerminator();
			} else {
				expectedResponseTerminatorInHex = GuiUtils.asciiToHex(dutCommand.getResponseTerminator());// .getDutResponseTerminator());
			}
		}

		Map<String, Object> responseReturn = new HashMap<String, Object>();
		int timeDelayInMilliSec = 0;
		// ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessV2 :
		// isDutSerialStatusConnected: "+
		// getDutCmdSpmObj().isDutSerialStatusConnected());
		responseReturn = getDutCmdSpmObj().dutSendCommandProcessV2(absoluteCommandInHex, timeDelayInMilliSec,
				expectedResponseInHex, dutResponseMandatory, expectedResponseTerminatorInHex, sourceThread);
		boolean status = (boolean) responseReturn.get("status");
		if (status) {
			String responseData = (String) responseReturn.get("result");
			ApplicationLauncher.logger
					.debug("dutMsngrSendCommandProcessV2 : responseData <" + sourceThread + ">: " + responseData);
			// ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessV2 : responseData
			// <"+sourceThread +">hex : " + GuiUtils.asciiToHex(responseData));
			// if(responseData.equals(GuiUtils.hexToAsciiV2(ConstantPowerSourceBofa.CMD_STOP_VOLT_CURRENT_IN_HEX)))
			// {
			// if(responseData.equals(GuiUtils.hexToAsciiV2(ConstantPowerSourceBofa.ER_CMD_STOP_VOLT_CURRENT_POSITIVE_IN_HEX)))
			// {
			/*
			 * if(responseData.equals(expectedData)) {
			 * 
			 * status = true;
			 * }else {
			 * status = false;
			 * }
			 */
			// ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessV2 : Hit1");
			if (dutResponseMandatory) {
				// ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessV2 : Hit2");
				// while (expectedRetryCount>0 &&
				// responseStatus.equals(DeleteMeConstant.NO_RESPONSE)){
				// expectedRetryCount--;
				// VI_SendStartCommand(CommandVI_PayLoad,SelectedPhase);
				// ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessV2
				// :expectedRetryCount<"+sourceThread +">:" + expectedRetryCount);
				// int TimeOutInSec= 60;vjhvjgv
				// SerialDataLDU lduData =
				// VICI_ReadDataWithErrorResponse(ExpectedData.length(),ExpectedData,ExpectedError1Data,ExpectedError2Data,TimeOutInSec);
				// ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessV2 :Hit2");
				if (getDutCmdSpmObj().getRxMsgQ_Dut().IsResponseReceived()) {
					// ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessV2 : Hit3");
					if (getDutCmdSpmObj().getRxMsgQ_Dut().isExpectedResponseReceived()) {
						// ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessV2 : Hit4");
						ApplicationLauncher.logger
								.debug("dutMsngrSendCommandProcessV2: <" + sourceThread + ">:Ack Response Success");
						// String CurrentLDU_Data =lduData.getLDU_ReadSerialData();
						// ApplicationLauncher.logger.info("dutMsngrSendCommandProcessV2: VICI Received
						// Data1:"+CurrentLDU_Data);
						// StripLDU_SerialData(lduData.getReceivedLength());
						// responseStatus=DeleteMeConstant.SUCCESS_RESPONSE;
						dutResponse.setResponseData(responseData);

						if (dutReadSerialNoFromDutEnabled) {
							// ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessV2 : Hit5");
							// String dutResponseDataInHex =
							// GuiUtils.asciiToHex(dutResponse.getResponseData());
							String readSerialNoInHex = new String(GuiUtils.asciiToHex(dutResponse.getResponseData()));// dutResponse.getResponseData());
							// ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessV2 : Hit6");
							readSerialNoInHex = readSerialNoInHex.replaceFirst(expectedResponseInHex, "");// replace
																											// starting
																											// header
							// ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessV2 : Hit7");
							readSerialNoInHex = readSerialNoInHex.replaceAll(expectedResponseTerminatorInHex + "$", "");/// replace
																														/// last
																														/// terminator
							// ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessV2 : Hit8");
							String readSerialNoInAscii = GuiUtils.hexToAsciiV2(readSerialNoInHex);
							// ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessV2 : Hit9");
							ApplicationLauncher.logger
									.info("dutMsngrSendCommandProcessV2: Expected recieved: readSerialNoInAscii:<"
											+ readSerialNoInAscii + "> : position id : " + dutPositionNo);
							// responseReturn.put("result", readSerialNoInAscii);
							dutResponse.setResponseData(readSerialNoInAscii);
						}
						if (dutReadRtcFromDutEnabled) {

							String readRtcInHex = new String(GuiUtils.asciiToHex(dutResponse.getResponseData()));// dutResponse.getResponseData());
							readRtcInHex = readRtcInHex.replaceFirst(expectedResponseInHex, "");// replace starting
																								// header
							readRtcInHex = readRtcInHex.replaceAll(expectedResponseTerminatorInHex + "$", "");/// replace
																												/// last
																												/// terminator
							ApplicationLauncher.logger
									.info("dutMsngrSendCommandProcessV2: Expected recieved: readRtcInHex-binding:<"
											+ readRtcInHex + "> : position id : " + dutPositionNo);

							String readRtcInAscii = "";
							if (rtcConvertTotHex) {
								readRtcInAscii = bindHexToTwoChar(readRtcInHex);
								ApplicationLauncher.logger.info(
										"dutMsngrSendCommandProcessV2: Expected recieved: readRtcInAscii-binding:<"
												+ readRtcInAscii + "> : position id : " + dutPositionNo);

							} else {
								readRtcInAscii = GuiUtils.hexToAsciiV2(readRtcInHex);
								ApplicationLauncher.logger
										.info("dutMsngrSendCommandProcessV2: Expected recieved: readRtcInAscii-2:<"
												+ readRtcInAscii + "> : position id : " + dutPositionNo);

							}

							// responseReturn.put("result", readSerialNoInAscii);
							dutResponse.setResponseData(readRtcInAscii);

						}

						getDutCmdSpmObj().getRxMsgQ_Dut().removeProcessedMsgFromQueue();
						// dutResponse.setResponseData(responseStatus);
						dutResponse.setStatus(true);
					} else if (getDutCmdSpmObj().getRxMsgQ_Dut().isExpectedErrorResponseReceived()) {
						ApplicationLauncher.logger
								.debug("dutMsngrSendCommandProcessV2 <" + sourceThread + ">:Ack Response Error");
						String CurrentReadData = getDutCmdSpmObj().getRxMsgQ_Dut().getLastReadMessage();
						// ApplicationLauncher.logger.info("dutMsngrSendCommandProcessV2: ErrorResponse
						// Received:"+CurrentLDU_Data);
						// ApplicationLauncher.logger.info("dutMsngrSendCommandProcessV2:
						// ExpectedError1Data:"+ExpectedError1Data);
						// StripLDU_SerialData(CurrentLDU_Data.length());

						responseStatus = CurrentReadData;
						dutResponse.setResponseData(responseStatus);
						dutResponse.setStatus(false);
						getDutCmdSpmObj().getRxMsgQ_Dut().removeProcessedMsgFromQueue();

					} else if (getDutCmdSpmObj().getRxMsgQ_Dut().isUnknownResponseRecieved()) {

						String CurrentReadData = getDutCmdSpmObj().getRxMsgQ_Dut().getLastReadMessage();
						ApplicationLauncher.logger
								.debug("dutMsngrSendCommandProcessV2 : isUnknownResponseRecieved : CurrentReadData<"
										+ sourceThread + ">: " + CurrentReadData);
						responseStatus = CurrentReadData;
						dutResponse.setResponseData(responseStatus);
						dutResponse.setStatus(false);
						getDutCmdSpmObj().getRxMsgQ_Dut().removeProcessedMsgFromQueue();
						if (responseStatus != null) { // Gopi2
							ApplicationLauncher.logger
									.debug("dutMsngrSendCommandProcessV2 :Unexpected Message: dropping the message<"
											+ sourceThread + ">:" + responseStatus);
							ApplicationLauncher.logger
									.debug("dutMsngrSendCommandProcessV2 :Unexpected Message: dropping the message<"
											+ sourceThread + ">:" + GuiUtils.HexToString(responseStatus));
						}

					}
				} else {

					responseStatus = DeleteMeConstant.NO_RESPONSE;
					dutResponse.setResponseData(responseStatus);
					dutResponse.setStatus(true);
					ApplicationLauncher.logger
							.info("dutMsngrSendCommandProcessV2 : <" + sourceThread + ">: Ack No Data Received");
				}

				// ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessV2 :
				// <"+sourceThread +">test1: ");

				// if ( (
				// !payLoadInHex.equals(ConstantPowerSourceBofa.CMD_STOP_VOLT_CURRENT_IN_HEX))
				// &&
				// ( !payLoadInHex.equals(ConstantPowerSourceBofa.CMD_STOP_CURRENT_IN_HEX)) ){
				if (BayUtils.isUserAborted()) {
					expectedRetryCount = 0;
					responseStatus = DeleteMeConstant.ERROR_RESPONSE;
					dutResponse.setResponseData(responseStatus);
					dutResponse.setStatus(false);
					ApplicationLauncher.logger
							.info("dutMsngrSendCommandProcessV2 V2 <" + sourceThread + ">: user aborted - detected");
				}

				// }
			} else {
				responseStatus = DeleteMeConstant.SUCCESS_RESPONSE;
				dutResponse.setResponseData(responseStatus);
				dutResponse.setStatus(true);
				Sleep(200);
			}

		}
		// calibrationSendCommandV2(positionId,absoluteCommandInAscii);

		// ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessV2 :
		// expectedDataInHex: " + expectedDataInHex);
		/*
		 * String expectedData = "";
		 * if(isResponseExpected){
		 * expectedData = GuiUtils.hexToAsciiV2(expectedDataInHex);
		 * }
		 */

		// boolean status = false;
		/*
		 * String rxMessageTerminatorInHex =
		 * ConstantDut.ER_TERMINATOR;//GuiUtils.hexToAscii(ConstantPowerSourceBofa.
		 * END_BYTE);
		 * Map<String,Object> responseReturn = new HashMap<String,Object>();
		 * if(getDutCmdSpmObj().isDutSerialStatusConnected()){
		 * responseReturn = getDutCmdSpmObj().dutSendCommandProcessV2( payLoadInHex,
		 * timeDelayInMilliSec,
		 * expectedData,isResponseExpected, rxMessageTerminatorInHex, sourceThread);
		 * status = (boolean)responseReturn.get("status");
		 * if(status){
		 * String responseData = (String)responseReturn.get("result");
		 * ApplicationLauncher.logger.
		 * debug("dutMsngrSendCommandProcessV2 : responseData <"+sourceThread +">: " +
		 * responseData);
		 * //ApplicationLauncher.logger.
		 * debug("dutMsngrSendCommandProcessV2 : responseData <"+sourceThread +">hex : "
		 * + GuiUtils.asciiToHex(responseData));
		 * //if(responseData.equals(GuiUtils.hexToAsciiV2(ConstantPowerSourceBofa.
		 * CMD_STOP_VOLT_CURRENT_IN_HEX))) {
		 * //if(responseData.equals(GuiUtils.hexToAsciiV2(ConstantPowerSourceBofa.
		 * ER_CMD_STOP_VOLT_CURRENT_POSITIVE_IN_HEX))) {
		 * if(responseData.equals(expectedData)) {
		 * 
		 * status = true;
		 * }else {
		 * status = false;
		 * }
		 * 
		 * }
		 */

		/*
		 * }else{
		 * ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessV2 V2 <"
		 * +sourceThread +">: serial port not connected or disconnected");
		 * 
		 * }
		 */
		// ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessV2 : test2: ");
		if (getDutCmdSpmObj().getRxMsgQ_Dut().getLastReadMessage() == null) {
			ApplicationLauncher.logger.info("dutMsngrSendCommandProcessV2<" + sourceThread + ">: last read null");
		} else {
			ApplicationLauncher.logger.info("dutMsngrSendCommandProcessV2: last read<" + sourceThread + ">:"
					+ getDutCmdSpmObj().getRxMsgQ_Dut().getLastReadMessage());
		}

		int timeOut = haltTimeInSec;// Integer.parseInt(haltTimeInSecStr);
		ApplicationLauncher.logger
				.info("dutMsngrSendCommandProcessV2: timeOut Entry: " + " : position id : " + dutPositionNo);
		while ((timeOut > 0) && (!BayUtils.isUserAborted())) {
			ApplicationLauncher.logger.info("dutMsngrSendCommandProcessV2: timeOut countDown: " + timeOut
					+ " : position id : " + dutPositionNo);
			timeOut--;
			Sleep(1000);
		}
		ApplicationLauncher.logger
				.info("dutMsngrSendCommandProcessV2: timeOut Exit: " + " : position id : " + dutPositionNo);

		ApplicationLauncher.logger.debug("dutMsngrSendCommandProcessV2 <" + sourceThread + ">:Exit");
		return dutResponse;
	}

	public String bindHexToTwoChar(String hexStr) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < hexStr.length(); i += 2) {
			String hexPair = hexStr.substring(i, i + 2);
			int decimal = Integer.parseInt(hexPair, 16);
			result.append(String.format("%02d", decimal)); // Ensure 2-digit format
		}
		ApplicationLauncher.logger
				.info("dutMsngrSendCommandProcessV2: bindHexToTwoChar : dateTimeStr " + result.toString());
		return result.toString();
	}

	public String bindTwoCharToHex(String dateTimeStr) {
		String hexData = "";
		// String dateTimeStr = "250514102345"; // yyMMddHHmmss format
		for (int i = 0; i < dateTimeStr.length(); i += 2) {
			String pair = dateTimeStr.substring(i, i + 2);
			int decimal = Integer.parseInt(pair);
			hexData = hexData + String.format("%02X", decimal);
			// System.out.println("Pair: " + pair + " -> Hex: " + hex);
		}
		// }
		ApplicationLauncher.logger.info("dutMsngrSendCommandProcessV2: bindTwoCharToHex : hexData " + hexData);
		return hexData;
	}

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep2 :InterruptedException:" + e.getMessage());
		}

	}

	public SerialPortManagerDutCmd_V3 getDutCmdSpmObj() {
		return dutCmdSpmObj;
	}

	public void setDutCmdSpmObj(SerialPortManagerDutCmd_V3 DutSpmObj) {
		this.dutCmdSpmObj = DutSpmObj;
	}
}
