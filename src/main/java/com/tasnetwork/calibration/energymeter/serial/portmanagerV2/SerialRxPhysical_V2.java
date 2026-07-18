package com.tasnetwork.calibration.energymeter.serial.portmanagerV2;

import java.util.Timer;
import java.util.TimerTask;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.DeleteMeConstant;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

public class SerialRxPhysical_V2 {

	Timer rxPhysicalTimer;
	int TaskIntervalInMsec = 200;// 1000;//500;//200;

	// SerialRxMessageQ rxMessageQ = new SerialRxMessageQ();
	Object productType = new Object();

	CommunicatorV2 SerialPortObj = null;
	// SerialDataMsgHV_Manager productType= new
	// SerialDataMsgHV_Manager(SerialPortObj);
	// Boolean ExpectedResponseRecieved = false;
	// Boolean ErrorResponseRecieved = false;
	// static String ReadRxPhysicalData = "";//LDU_ReadSerialData = "";
	Integer RxPhysicalDataLength = 0;// ReceivedLength=0;
	String ReadRxPhysicalData = "";
	String lastDisplayedReadRxPhysicalData = "";
	String rxDisplayData = "";
	boolean ReadRxPhysicalFlag = false;
	boolean comCheckEnabled = false;

	/*
	 * String messageTerminator = ConstantHV_Src.HV_ER_TERMINATOR;
	 * String messageErrorTerminator = ConstantBridge.BRIDGE_ERR_TERMINATOR;
	 * String comCheckSuccess1 = ConstantLVD.LVD_ER_COM_CHECK_SUCCESS1;
	 * String comCheckSuccess2 = ConstantLVD.LVD_ER_COM_CHECK_SUCCESS2;
	 */

	String responseMessageTerminator = DeleteMeConstant.HV_ER_TERMINATOR;
	String messageErrorTerminator = DeleteMeConstant.BRIDGE_ERR_TERMINATOR;
	String comCheckSuccess1 = DeleteMeConstant.LVD_ER_COM_CHECK_SUCCESS1;
	String comCheckSuccess2 = DeleteMeConstant.LVD_ER_COM_CHECK_SUCCESS2;
	public String rxLabel = "";

	public boolean isReadRxPhysicalFlag() {
		return ReadRxPhysicalFlag;
	}

	public void setReadRxPhysicalFlag(boolean readRxPhysicalFlag) {
		ReadRxPhysicalFlag = readRxPhysicalFlag;
	}

	public SerialRxPhysical_V2(CommunicatorV2 inpSerialPortObj, Object inputProductType, boolean comCheckStatus,
			String inpLabel, String responseTerminator, String errorTerminator) {// ,String inputComCheckSuccess){
		SerialPortObj = inpSerialPortObj;
		// rxMessageQ.productType = inputProductType;
		productType = inputProductType;
		comCheckEnabled = comCheckStatus;
		rxLabel = inpLabel;
		responseMessageTerminator = responseTerminator;
		messageErrorTerminator = errorTerminator;
		// comCheckSuccess = inputComCheckSuccess;
	}

	/*
	 * SerialRxPhysical(Object inputProductType, boolean comCheckStatus){
	 * 
	 * //rxMessageQ.productType = inputProductType;
	 * productType = inputProductType;
	 * 
	 * if(productType instanceof SerialRxMessageQ ) {
	 * SerialRxMessageQ DataMsgHV = (SerialRxMessageQ)productType;
	 * SerialPortObj = DataMsgHV.getSerialPortObj();
	 * }
	 * comCheckEnabled = comCheckStatus;
	 * }
	 */

	public void SerialRxPhysicalTimerStart() {
		rxPhysicalTimer = new Timer();
		rxPhysicalTimer.schedule(new SerialRxPhysicalTask(), TaskIntervalInMsec);
		// rxMessageQ.responseWaitCount = seconds;
		ApplicationLauncher.logger.info("SerialRxTimerStart_V2 : Triggered: " + rxLabel);
	}

	/*
	 * public Boolean IsExpectedErrorResponseReceived(){
	 * return this.ErrorResponseRecieved;
	 * 
	 * }
	 */

	public void ClearRxPhysicalData() {
		ApplicationLauncher.logger.info("ClearRxPhysicalData : Entry:");
		SerialPortObj.clearSerialData();
		ReadRxPhysicalData = "";

	}

	public void StripRxPhysicalData(int dataLength) {
		SerialPortObj.stripLength(dataLength);
		ReadRxPhysicalData = "";

	}

	public boolean isComCheckEnabled() {
		return comCheckEnabled;
	}

	public void setComCheckEnabled(boolean comCheckEnabled) {
		this.comCheckEnabled = comCheckEnabled;
	}

	public void setRxPhysicalDataLength(Integer length) {// setReceivedLength(Integer length){

		this.RxPhysicalDataLength = length;
	}

	public Integer getRxPhysicalDataLength() {// getReceivedLength(){

		return this.RxPhysicalDataLength;
	}

	public void ResetRxPhysicalDataLength() {// getReceivedLength(){

		this.RxPhysicalDataLength = 0;
	}

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep :InterruptedException:" + e.getMessage());
		}

	}

	public void AddToProductMessageQ(String inputData) {
		// ApplicationLauncher.logger.info("AddToProductMessageQ: Entry");

		if (productType instanceof SerialRxMessageQ_V2) {
			SerialRxMessageQ_V2 DataMsgHV_Manager = (SerialRxMessageQ_V2) productType;
			DataMsgHV_Manager.addMsgQueue(inputData);
			/*
			 * if(MaintenanceModeExecController.isSerialDisplayProcess()) {
			 * //if(ProjectManualModeExecController.isProGenDemoExecution()) {
			 * if(ProcalFeatureEnable.MAINTENANCE_MODE_ENABLED) {
			 * MaintenanceModeExecController.serialDataDisplayUpdate(rxLabel+"-Rx-Hex:<"+
			 * inputData+">");
			 * MaintenanceModeExecController.serialDataDisplayUpdate(rxLabel+"-Rx-Str:<"+
			 * GuiUtils.HexToString(inputData)+">");
			 * }
			 * }
			 */
			StripRxPhysicalData(inputData.length());
		}
	}

	public void clearLastDisplayedReadRxPhysicalData() {
		lastDisplayedReadRxPhysicalData = "";
	}

	class SerialRxPhysicalTask extends TimerTask {
		public void run() {
			ReadRxPhysicalData = SerialPortObj.getSerialData();
			// ApplicationLauncher.logger.debug("ReadRxPhysicalData V2: "+ rxLabel+":
			// "+SerialPortObj.getDeviceType());

			// ApplicationLauncher.logger.debug("ReadRxPhysicalData V2: "+ rxLabel+":
			// "+ReadRxPhysicalData);
			// ApplicationLauncher.logger.debug("ReadRxPhysicalData V2: "+ rxLabel+": Hex:
			// "+GuiUtils.asciiToHex(ReadRxPhysicalData));
			// ApplicationLauncher.logger.debug("ReadRxPhysicalData:
			// responseMessageTerminator:"+responseMessageTerminator);
			if (ReadRxPhysicalData.endsWith(responseMessageTerminator)) {
				Sleep(300);
				ReadRxPhysicalData = SerialPortObj.getSerialData();
				if (ReadRxPhysicalData.endsWith(responseMessageTerminator)) {
					String[] checkForMultipleMessage = ReadRxPhysicalData.split(responseMessageTerminator);

					// ApplicationLauncher.logger.debug("AssertSplitMultipleMessage: Size:
					// "+checkForMultipleMessage.length);
					for (int i = 0; i < checkForMultipleMessage.length; i++) {
						// ApplicationLauncher.logger.debug("AssertSplitMultipleMessage: data:
						// "+checkForMultipleMessage[i]+responseMessageTerminatorInHex);
						AddToProductMessageQ(checkForMultipleMessage[i] + responseMessageTerminator);
					}

				}
			}

			if (!messageErrorTerminator.equals("")) {
				if (ReadRxPhysicalData.endsWith(messageErrorTerminator)) {
					Sleep(300);
					ReadRxPhysicalData = SerialPortObj.getSerialData();
					if (ReadRxPhysicalData.endsWith(messageErrorTerminator)) {
						// String[] checkForMultipleMessage =
						// ReadRxPhysicalData.split(messageErrorTerminator);

						// ApplicationLauncher.logger.debug("AssertSplitMultipleMessage: Size:
						// "+checkForMultipleMessage.length);
						// for(int i =0; i< checkForMultipleMessage.length; i++) {
						// ApplicationLauncher.logger.debug("AssertSplitMultipleMessage: data:
						// "+checkForMultipleMessage[i]+messageErrorTerminator);
						ApplicationLauncher.logger.debug("With messageErrorTerminator: " + ReadRxPhysicalData);
						if (!(ReadRxPhysicalData.equals(messageErrorTerminator))) {
							// ReadRxPhysicalData= ReadRxPhysicalData.replace(messageErrorTerminator, "");
							AddToProductMessageQ(ReadRxPhysicalData);
							// ClearRxPhysicalData();
						}
						// }

					}
				}
			}
			if (isComCheckEnabled()) {
				// ApplicationLauncher.logger.info("isComCheckEnabled: Entry:"+rxLabel);
				// ApplicationLauncher.logger.info("isComCheckEnabled: ReadRxPhysicalData:"
				// +rxLabel +":"+ ReadRxPhysicalData);
				if (ReadRxPhysicalData.endsWith(comCheckSuccess1)) {
					Sleep(300);
					ReadRxPhysicalData = SerialPortObj.getSerialData();
					if (ReadRxPhysicalData.endsWith(comCheckSuccess1)) {
						ApplicationLauncher.logger.debug("AssertSplitMultipleMessage1: Command Check success");
						AddToProductMessageQ(ReadRxPhysicalData);
					}
				} else if (ReadRxPhysicalData.endsWith(comCheckSuccess2)) {
					Sleep(300);
					ReadRxPhysicalData = SerialPortObj.getSerialData();
					if (ReadRxPhysicalData.endsWith(comCheckSuccess2)) {
						ApplicationLauncher.logger.debug("AssertSplitMultipleMessage2: Command Check success");
						AddToProductMessageQ(ReadRxPhysicalData);
					}
				}
			}
			if (isReadRxPhysicalFlag()) {
				rxPhysicalTimer.schedule(new SerialRxPhysicalTask(), TaskIntervalInMsec);
				// ApplicationLauncher.logger.debug("SerialRxPhysicalTask: Renewed");
			} else {
				ApplicationLauncher.logger.debug("SerialRxPhysicalTask: Stopped :" + rxLabel);
			}
		}

	}

	public String getResponseMessageTerminator() {
		return responseMessageTerminator;
	}

	public void setResponseMessageTerminator(String messageTerminator) {
		this.responseMessageTerminator = messageTerminator;
	}

}
