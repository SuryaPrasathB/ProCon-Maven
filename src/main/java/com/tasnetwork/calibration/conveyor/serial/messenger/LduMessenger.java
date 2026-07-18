package com.tasnetwork.calibration.conveyor.serial.messenger;

import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.bay.DevSysEnergyMeter;
import com.tasnetwork.calibration.conveyor.bay.EIC_MegaOhmMeter;
import com.tasnetwork.calibration.conveyor.bay.Elmeasure_MultiMeter;
import com.tasnetwork.calibration.conveyor.bay.NewlandQRCodeScanner;
import com.tasnetwork.calibration.conveyor.bay.ir.IR_ReadResult;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmLdu;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmQrScanner;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmVoltPm;
import com.tasnetwork.calibration.conveyor.util.GUIUtils;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.DeleteMeConstant;

public class LduMessenger {

	// DeviceDataManagerController displayDataObj = new
	// DeviceDataManagerController();
	SpmLdu pwrSrcSpmObj = new SpmLdu();// null;//new DeviceDataManagerController().getSerialPortManagerPwrSrc_V2();

	public LduMessenger(SpmLdu qrScannerSerialPort) {
		this.pwrSrcSpmObj = qrScannerSerialPort;
	}

	public LduMessenger() {

	}
	// ==========================================================================================================================

	// ==========================================================================================================================

	// ====================================================================================================================
	public boolean sendVoltageCurrentStopOutputCommand() {
		ApplicationLauncher.logger.info("sendVoltageCurrentStopOutputCommand: Entry");
		// frameVoltageCurrentStopOutputCommand();
		String payLoadInHex = "";// ConstantPowerSourceBofa.CMD_STOP_VOLT_CURRENT_IN_HEX;//voltageCurrentStopOutputCmdFrame;//GUIUtils.StringToHex(voltageCurrentStopOutputCmdFrame
									// );
		ApplicationLauncher.logger.info("sendVoltageCurrentStopOutputCommand : payLoadInHex: " + payLoadInHex);

		int timeDelayInMilliSec = 0;
		String expectedData = GUIUtils.hexToAscii("");// ConstantPowerSourceBofa.ER_STARTS_WITH);
		boolean isResponseExpected = true;
		boolean status = false;
		String rxMessageTerminator = GUIUtils.hexToAscii("");// ConstantPowerSourceBofa.END_BYTE);
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);
		responseReturn = getPwrSrcSpmObj().elmeasureSendCommandProcess(payLoadInHex, timeDelayInMilliSec,
				expectedData, isResponseExpected, rxMessageTerminator, "sendVoltageCurrentStopOutputCommand");
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
	public Map<String, Object> lsLDU_SendCommandReadAccuracyData(String slaveId) {
		ApplicationLauncher.logger.info("lsLDU_SendCommandReadAccuracyData: Entry");
		boolean status = false;
		// boolean isResponseExpected = true;

		int slaveIdValue = Integer.parseInt(slaveId, 16); // Convert hex string to integer

		String slaveIdHex = String.format("%02X", slaveIdValue); // Format as 2-character hex string

		// Combine all parts into the frame (without CRC)
		String frame = slaveIdHex + Elmeasure_MultiMeter.FUNCTION_CODE + Elmeasure_MultiMeter.REGISTER_ADDRESS
				+ Elmeasure_MultiMeter.NUM_BYTES;

		// Calculate CRC for the frame
		Elmeasure_MultiMeter elmeasure_MultiMeter = new Elmeasure_MultiMeter();
		String crc = elmeasure_MultiMeter.calculateCRC(frame);

		// Combine the frame and CRC to get the final frame
		// String commandFrame = frame + crc;

		String payLoadInHex = frame + crc;// NewlandQRCodeScanner.ANALOG_TRIGGER_SETTING ;
											// //ANALOG_TRIGGER_SETTING;//startTestEndFrame ;xcvxc
		ApplicationLauncher.logger.info("lsLDU_SendCommandReadAccuracyData: payLoadInHex: " + payLoadInHex);

		/*
		 * String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address +
		 * ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;
		 * 
		 * if (address == ConstantPowerSourceBofa.LDU_INT_BROADCAST_ADDRESS) {
		 * addressStr = ConstantPowerSourceBofa.LDU_HEX_BROADCAST_ADDRESS ;
		 * }
		 */

		int expectedDataLengthInHex = Elmeasure_MultiMeter.ER_LENGTH_HEX;// slaveIdHex;//NewlandQRCodeScanner.EXPECTED_DATA_IN_HEX;
		int timeDelayInMilliSec = 0;
		String expectedDataInHex = slaveIdHex;// "06";//ConstantPowerSourceBofa.ER_LDU_STARTS_WITH + addressStr;
		String responseData = "";
		boolean isResponseExpected = true;
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);
		// SerialDM_Obj.WriteToSerialCommPwrSrc(ANALOG_TRIGGER_SETTING,ER_ANALOG_TRIGGER_SETTING);
		String responseStatus = elmeasurePanelMeterMsngrSendCommandProcessWithLength(payLoadInHex, timeDelayInMilliSec,
				expectedDataInHex, expectedDataLengthInHex, isResponseExpected, "test-T2");

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
					ApplicationLauncher.logger.info("lsLDU_SendCommandReadAccuracyData : no response expected success");
					status = true;
				}
			}
		}
		return responseReturn;
	}

	// ==================================================================================================================================

	public SpmLdu getPwrSrcSpmObj() {
		return pwrSrcSpmObj;
	}

	public void setPwrSrcSpmObj(SpmLdu pwrSrcSpmObj) {
		this.pwrSrcSpmObj = pwrSrcSpmObj;
	}

	// ==========================================================================================================================

	public String elmeasurePanelMeterMsngrSendCommandProcess(String payLoadInHex, int timeDelayInMilliSec,
			String expectedDataInHex, boolean isResponseExpected, String sourceThread) {
		ApplicationLauncher.logger.debug("elmeasurePanelMeterMsngrSendCommandProcess :Entry");
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

		// ApplicationLauncher.logger.debug("elmeasurePanelMeterMsngrSendCommandProcess
		// : expectedDataInHex: " + expectedDataInHex);
		String expectedData = "";
		if (isResponseExpected) {
			expectedData = GUIUtils.hexToAsciiV2(expectedDataInHex);
		}

		boolean status = false;
		String rxMessageTerminator = GUIUtils.hexToAscii("0D0A");// ConstantPowerSourceBofa.END_BYTE);
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);
		if (getPwrSrcSpmObj().isDeviceSerialStatusConnected()) {
			responseReturn = getPwrSrcSpmObj().elmeasureSendCommandProcess(payLoadInHex, timeDelayInMilliSec,
					expectedData, isResponseExpected, rxMessageTerminator, sourceThread);
			status = (boolean) responseReturn.get("status");
			if (status) {
				String responseData = (String) responseReturn.get("result");
				ApplicationLauncher.logger.debug("elmeasurePanelMeterMsngrSendCommandProcess : responseData <"
						+ sourceThread + ">: " + responseData);
				ApplicationLauncher.logger.debug("elmeasurePanelMeterMsngrSendCommandProcess : responseData <"
						+ sourceThread + "> hex : " + GUIUtils.asciiToHex(responseData));
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
					ApplicationLauncher.logger.debug("elmeasurePanelMeterMsngrSendCommandProcess :retryCount<"
							+ sourceThread + ">:" + retryCount);
					// int TimeOutInSec= 60;vjhvjgv
					// SerialDataLDU lduData =
					// VICI_ReadDataWithErrorResponse(ExpectedData.length(),ExpectedData,ExpectedError1Data,ExpectedError2Data,TimeOutInSec);
					// ApplicationLauncher.logger.debug("elmeasurePanelMeterMsngrSendCommandProcess
					// :Hit2");
					if (getPwrSrcSpmObj().getRxMsgQ_PwrSrc().IsResponseReceived()) {
						if (getPwrSrcSpmObj().getRxMsgQ_PwrSrc().isExpectedResponseReceived()) {
							ApplicationLauncher.logger.debug("elmeasurePanelMeterMsngrSendCommandProcess: <"
									+ sourceThread + ">:Ack Response Success");
							// String CurrentLDU_Data =lduData.getLDU_ReadSerialData();
							// ApplicationLauncher.logger.info("elmeasurePanelMeterMsngrSendCommandProcess:
							// VICI Received Data1:"+CurrentLDU_Data);
							// StripLDU_SerialData(lduData.getReceivedLength());
							responseStatus = DeleteMeConstant.SUCCESS_RESPONSE;
							getPwrSrcSpmObj().getRxMsgQ_PwrSrc().removeProcessedMsgFromQueue();
						} else if (getPwrSrcSpmObj().getRxMsgQ_PwrSrc().isExpectedErrorResponseReceived()) {
							ApplicationLauncher.logger.debug("elmeasurePanelMeterMsngrSendCommandProcess <"
									+ sourceThread + ">:Ack Response Error");
							String CurrentReadData = getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
							// ApplicationLauncher.logger.info("elmeasurePanelMeterMsngrSendCommandProcess:
							// ErrorResponse Received:"+CurrentLDU_Data);
							// ApplicationLauncher.logger.info("elmeasurePanelMeterMsngrSendCommandProcess:
							// ExpectedError1Data:"+ExpectedError1Data);
							// StripLDU_SerialData(CurrentLDU_Data.length());

							responseStatus = CurrentReadData;
							getPwrSrcSpmObj().getRxMsgQ_PwrSrc().removeProcessedMsgFromQueue();

						} else if (getPwrSrcSpmObj().getRxMsgQ_PwrSrc().isUnknownResponseRecieved()) {

							String CurrentReadData = getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
							ApplicationLauncher.logger.debug(
									"elmeasurePanelMeterMsngrSendCommandProcess : isUnknownResponseRecieved : CurrentReadData<"
											+ sourceThread + ">: " + CurrentReadData);
							responseStatus = CurrentReadData;
							getPwrSrcSpmObj().getRxMsgQ_PwrSrc().removeProcessedMsgFromQueue();
							if (responseStatus != null) { // Gopi2
								ApplicationLauncher.logger.debug(
										"elmeasurePanelMeterMsngrSendCommandProcess :Unexpected Message: dropping the message<"
												+ sourceThread + ">:" + responseStatus);
								ApplicationLauncher.logger.debug(
										"elmeasurePanelMeterMsngrSendCommandProcess :Unexpected Message: dropping the message<"
												+ sourceThread + ">:" + GUIUtils.HexToString(responseStatus));
							}

						}
					} else {
						ApplicationLauncher.logger.info("elmeasurePanelMeterMsngrSendCommandProcess : <" + sourceThread
								+ ">: Ack No Data Received");
					}

					ApplicationLauncher.logger
							.debug("elmeasurePanelMeterMsngrSendCommandProcess : <" + sourceThread + ">test1: ");

					/*
					 * if ( (
					 * !payLoadInHex.equals(ConstantPowerSourceBofa.CMD_STOP_VOLT_CURRENT_IN_HEX))
					 * &&
					 * ( !payLoadInHex.equals(ConstantPowerSourceBofa.CMD_STOP_CURRENT_IN_HEX)) ){
					 * if(BayUtils.isUserAborted()){
					 * retryCount = 0;
					 * responseStatus = DeleteMeConstant.ERROR_RESPONSE;
					 * ApplicationLauncher.logger.
					 * info("elmeasurePanelMeterMsngrSendCommandProcess V2 <"+sourceThread
					 * +">: user aborted - detected");
					 * }
					 * }else if(!getPwrSrcSpmObj().isPwrSrcSerialStatusConnected()){
					 * retryCount = 0;
					 * responseStatus = DeleteMeConstant.ERROR_RESPONSE;
					 * ApplicationLauncher.logger.
					 * debug("elmeasurePanelMeterMsngrSendCommandProcess V2 <"+sourceThread
					 * +">: serial port - disconnected");
					 * 
					 * }
					 */
				}
			} else {
				responseStatus = DeleteMeConstant.NO_RESPONSE;
				Sleep(200);
			}
		} else {
			ApplicationLauncher.logger.debug("elmeasurePanelMeterMsngrSendCommandProcess V2 <" + sourceThread
					+ ">: serial port not connected or disconnected");

		}
		// ApplicationLauncher.logger.debug("elmeasurePanelMeterMsngrSendCommandProcess
		// : test2: ");
		if (getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage() == null) {
			ApplicationLauncher.logger
					.info("elmeasurePanelMeterMsngrSendCommandProcess<" + sourceThread + ">: last read null");
		} else {
			ApplicationLauncher.logger.info("elmeasurePanelMeterMsngrSendCommandProcess: last read<" + sourceThread
					+ ">:" + getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage());
		}
		ApplicationLauncher.logger.debug("elmeasurePanelMeterMsngrSendCommandProcess <" + sourceThread + ">:Exit");
		return responseStatus;
	}
	// ==========================================================================================================================

	public String elmeasurePanelMeterMsngrSendCommandProcessWithLength(String payLoadInHex, int timeDelayInMilliSec,
			String expectedDataInHex, int expectedDataHexLength, boolean isResponseExpected, String sourceThread) {
		ApplicationLauncher.logger.debug("elmeasurePanelMeterMsngrSendCommandProcessWithLength :Entry");
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
			responseReturn = getPwrSrcSpmObj().elmeasureSendCommandProcessWithLength(payLoadInHex, timeDelayInMilliSec,
					expectedData, isResponseExpected, expectedDataHexLength, sourceThread);
			status = (boolean) responseReturn.get("status");
			if (status) {
				String responseData = (String) responseReturn.get("result");
				ApplicationLauncher.logger.debug("elmeasurePanelMeterMsngrSendCommandProcessWithLength : responseData <"
						+ sourceThread + ">: " + responseData);
				ApplicationLauncher.logger.debug("elmeasurePanelMeterMsngrSendCommandProcessWithLength : responseData <"
						+ sourceThread + "> hex : " + GUIUtils.asciiToHex(responseData));
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
					ApplicationLauncher.logger.debug("elmeasurePanelMeterMsngrSendCommandProcessWithLength :retryCount<"
							+ sourceThread + ">:" + retryCount);
					// int TimeOutInSec= 60;vjhvjgv
					// SerialDataLDU lduData =
					// VICI_ReadDataWithErrorResponse(ExpectedData.length(),ExpectedData,ExpectedError1Data,ExpectedError2Data,TimeOutInSec);
					// ApplicationLauncher.logger.debug("elmeasurePanelMeterMsngrSendCommandProcessWithLength
					// :Hit2");
					if (getPwrSrcSpmObj().getRxMsgQ_PwrSrc().IsResponseReceived()) {
						if (getPwrSrcSpmObj().getRxMsgQ_PwrSrc().isExpectedResponseReceived()) {
							ApplicationLauncher.logger.debug("elmeasurePanelMeterMsngrSendCommandProcessWithLength: <"
									+ sourceThread + ">:Ack Response Success");
							// String CurrentLDU_Data =lduData.getLDU_ReadSerialData();
							// ApplicationLauncher.logger.info("elmeasurePanelMeterMsngrSendCommandProcessWithLength:
							// VICI Received Data1:"+CurrentLDU_Data);
							// StripLDU_SerialData(lduData.getReceivedLength());
							responseStatus = DeleteMeConstant.SUCCESS_RESPONSE;
							getPwrSrcSpmObj().getRxMsgQ_PwrSrc().removeProcessedMsgFromQueue();
						} else if (getPwrSrcSpmObj().getRxMsgQ_PwrSrc().isExpectedErrorResponseReceived()) {
							ApplicationLauncher.logger.debug("elmeasurePanelMeterMsngrSendCommandProcessWithLength <"
									+ sourceThread + ">:Ack Response Error");
							String CurrentReadData = getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
							// ApplicationLauncher.logger.info("elmeasurePanelMeterMsngrSendCommandProcessWithLength:
							// ErrorResponse Received:"+CurrentLDU_Data);
							// ApplicationLauncher.logger.info("elmeasurePanelMeterMsngrSendCommandProcessWithLength:
							// ExpectedError1Data:"+ExpectedError1Data);
							// StripLDU_SerialData(CurrentLDU_Data.length());

							responseStatus = CurrentReadData;
							getPwrSrcSpmObj().getRxMsgQ_PwrSrc().removeProcessedMsgFromQueue();

						} else if (getPwrSrcSpmObj().getRxMsgQ_PwrSrc().isUnknownResponseRecieved()) {

							String CurrentReadData = getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
							ApplicationLauncher.logger.debug(
									"elmeasurePanelMeterMsngrSendCommandProcessWithLength : isUnknownResponseRecieved : CurrentReadData<"
											+ sourceThread + ">: " + CurrentReadData);
							responseStatus = CurrentReadData;
							getPwrSrcSpmObj().getRxMsgQ_PwrSrc().removeProcessedMsgFromQueue();
							if (responseStatus != null) { // Gopi2
								ApplicationLauncher.logger.debug(
										"elmeasurePanelMeterMsngrSendCommandProcessWithLength :Unexpected Message: dropping the message<"
												+ sourceThread + ">:" + responseStatus);
								ApplicationLauncher.logger.debug(
										"elmeasurePanelMeterMsngrSendCommandProcessWithLength :Unexpected Message: dropping the message<"
												+ sourceThread + ">:" + GUIUtils.HexToString(responseStatus));
							}

						}
					} else {
						ApplicationLauncher.logger.info("elmeasurePanelMeterMsngrSendCommandProcessWithLength : <"
								+ sourceThread + ">: Ack No Data Received");
					}

					ApplicationLauncher.logger.debug(
							"elmeasurePanelMeterMsngrSendCommandProcessWithLength : <" + sourceThread + ">test1: ");

					/*
					 * if ( (
					 * !payLoadInHex.equals(ConstantPowerSourceBofa.CMD_STOP_VOLT_CURRENT_IN_HEX))
					 * &&
					 * ( !payLoadInHex.equals(ConstantPowerSourceBofa.CMD_STOP_CURRENT_IN_HEX)) ){
					 * if(BayUtils.isUserAborted()){
					 * retryCount = 0;
					 * responseStatus = DeleteMeConstant.ERROR_RESPONSE;
					 * ApplicationLauncher.logger.
					 * info("elmeasurePanelMeterMsngrSendCommandProcessWithLength V2 <"+sourceThread
					 * +">: user aborted - detected");
					 * }
					 * }else if(!getPwrSrcSpmObj().isPwrSrcSerialStatusConnected()){
					 * retryCount = 0;
					 * responseStatus = DeleteMeConstant.ERROR_RESPONSE;
					 * ApplicationLauncher.logger.
					 * debug("elmeasurePanelMeterMsngrSendCommandProcessWithLength V2 <"
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
			ApplicationLauncher.logger.debug("elmeasurePanelMeterMsngrSendCommandProcessWithLength V2 <" + sourceThread
					+ ">: serial port not connected or disconnected");

		}
		// ApplicationLauncher.logger.debug("elmeasurePanelMeterMsngrSendCommandProcessWithLength
		// : test2: ");
		if (getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage() == null) {
			ApplicationLauncher.logger
					.info("elmeasurePanelMeterMsngrSendCommandProcessWithLength<" + sourceThread + ">: last read null");
		} else {
			ApplicationLauncher.logger.info("elmeasurePanelMeterMsngrSendCommandProcessWithLength: last read<"
					+ sourceThread + ">:" + getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage());
		}
		ApplicationLauncher.logger
				.debug("elmeasurePanelMeterMsngrSendCommandProcessWithLength <" + sourceThread + ">:Exit");
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
