package com.tasnetwork.calibration.conveyor.serial.portmanager;

import java.util.Timer;
import java.util.TimerTask;

import com.tasnetwork.calibration.conveyor.util.GUIUtils;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.DeleteMeConstant;

public class SerialRxPhysical {

	Timer rxPhysicalTimer;
	int TaskIntervalInMsec = 200;// 1000;//500;//200;

	// SerialRxMessageQ rxMessageQ = new SerialRxMessageQ();
	Object productType = new Object();

	Communicator SerialPortObj = null;
	// SerialDataMsgHV_Manager productType= new
	// SerialDataMsgHV_Manager(SerialPortObj);
	// Boolean ExpectedResponseRecieved = false;
	// Boolean ErrorResponseRecieved = false;
	// static String ReadRxPhysicalData = "";//LDU_ReadSerialData = "";
	Integer RxPhysicalDataLength = 0;// ReceivedLength=0;
	String ReadRxPhysicalData = "";
	String lastDisplayedReadRxPhysicalData = "";
	String rxDisplayData = "";
	volatile boolean ReadRxPhysicalFlag = false;
	boolean comCheckEnabled = false;
	Integer expectedResponseDataInAsciiLength = 1;
	boolean validateByRxDataLength = false;

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
	String rxLabel = "";
	private boolean terminatorMandatory = false;
	int terminatorOptionalCheckCounter = 0;

	public boolean isReadRxPhysicalFlag() {
		return ReadRxPhysicalFlag;
	}

	public void setReadRxPhysicalFlag(boolean readRxPhysicalFlag) {
		ReadRxPhysicalFlag = readRxPhysicalFlag;
	}

	public SerialRxPhysical(Communicator inpSerialPortObj, Object inputProductType, boolean comCheckStatus,
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

	public SerialRxPhysical(Communicator inpSerialPortObj, Object inputProductType, boolean comCheckStatus,
			String inpLabel, boolean valByDataLength, int expectedDataLengthInHex, String errorTerminator) {// ,String
																											// inputComCheckSuccess){
		SerialPortObj = inpSerialPortObj;
		// rxMessageQ.productType = inputProductType;
		productType = inputProductType;
		comCheckEnabled = comCheckStatus;
		rxLabel = inpLabel;
		// responseMessageTerminator = responseTerminator;
		// messageErrorTerminator = errorTerminator;
		validateByRxDataLength = valByDataLength;
		expectedResponseDataInAsciiLength = expectedDataLengthInHex;
		messageErrorTerminator = errorTerminator;
		ApplicationLauncher.logger.debug("SerialRxPhysical_V2 : expectedDataLengthInHex: " + expectedDataLengthInHex);
		ApplicationLauncher.logger
				.debug("SerialRxPhysical_V2 : expectedResponseDataInAsciiLength: " + expectedResponseDataInAsciiLength);
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

	// =========================================================================================================================================

	public void AddToProductMessageQ(String inputData) {
		// ApplicationLauncher.logger.info("AddToProductMessageQ: Entry");

		if (productType instanceof SerialRxMessageQ) {
			SerialRxMessageQ DataMsgHV_Manager = (SerialRxMessageQ) productType;
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
	// =========================================================================================================================================

	class SerialRxPhysicalTask extends TimerTask {
		public void run() {
			ReadRxPhysicalData = SerialPortObj.getSerialData();
			/*
			 * if(MaintenanceModeExecController.isSerialDisplayProcess()) {
			 * //if(ProjectManualModeExecController.isProGenDemoExecution()) {
			 * if(ProcalFeatureEnable.MAINTENANCE_MODE_ENABLED) {
			 * if( (!ReadRxPhysicalData.isEmpty()) &&
			 * (lastDisplayedReadRxPhysicalData!=ReadRxPhysicalData)){
			 * 
			 * if(ReadRxPhysicalData.startsWith(lastDisplayedReadRxPhysicalData)){
			 * rxDisplayData =
			 * ReadRxPhysicalData.replaceFirst(lastDisplayedReadRxPhysicalData, "");
			 * //ApplicationLauncher.logger.
			 * debug("AssertSplitMultipleMessage: rxDisplayData1: "+rxDisplayData);
			 * 
			 * }else{
			 * rxDisplayData = ReadRxPhysicalData;
			 * //ApplicationLauncher.logger.
			 * debug("AssertSplitMultipleMessage: rxDisplayData2: "+rxDisplayData);
			 * }
			 * MaintenanceModeExecController.updateRxSerialMessageDisplay(rxDisplayData);
			 * lastDisplayedReadRxPhysicalData = ReadRxPhysicalData;
			 * }
			 * }
			 * }
			 */

			/*
			 * ApplicationLauncher.logger.debug("ReadRxPhysicalData V2: "+
			 * rxLabel+": "+ReadRxPhysicalData);
			 * ApplicationLauncher.logger.debug("ReadRxPhysicalData V2: "+
			 * rxLabel+": Hex: "+GUIUtils.asciiToHex(ReadRxPhysicalData));
			 * ApplicationLauncher.logger.debug("ReadRxPhysicalData V2: "+
			 * rxLabel+": expectedResponseDataInAsciiLength: "
			 * +expectedResponseDataInAsciiLength);
			 * ApplicationLauncher.logger.debug("ReadRxPhysicalData V2: "+
			 * rxLabel+": ReadRxPhysicalData.length(): "+ReadRxPhysicalData.length());
			 * ApplicationLauncher.logger.debug("ReadRxPhysicalData V2: "+
			 * rxLabel+": terminatorMandatory: "+ terminatorMandatory);
			 */
			if (ReadRxPhysicalData.endsWith(responseMessageTerminator)) {
				ApplicationLauncher.logger.debug("ReadRxPhysicalData V2: " + rxLabel + ": hit1 ");

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
			} else if (!messageErrorTerminator.equals("")) {
				ApplicationLauncher.logger.debug("ReadRxPhysicalData V2: " + rxLabel + ": hit2 ");
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
			} else if ((!terminatorMandatory) && (expectedResponseDataInAsciiLength == ReadRxPhysicalData.length())) {
				ApplicationLauncher.logger.debug("ReadRxPhysicalData V2: " + rxLabel + ": hit3 ");
				Sleep(300);
				terminatorOptionalCheckCounter++;
				if (terminatorOptionalCheckCounter == 3) {
					ApplicationLauncher.logger.debug("SerialRxPhysical_V2 counter hit: " + ReadRxPhysicalData);
					AddToProductMessageQ(ReadRxPhysicalData);
					terminatorOptionalCheckCounter = 0;
				}
			} /*
				 * else if (!terminatorMandatory){
				 * ApplicationLauncher.logger.debug("ReadRxPhysicalData V2: "+
				 * rxLabel+": hit4 ");
				 * Sleep(300);
				 * terminatorOptionalCheckCounter++;
				 * if(terminatorOptionalCheckCounter==3){
				 * ApplicationLauncher.logger.debug("SerialRxPhysical_V2 counter hit: "+
				 * ReadRxPhysicalData);
				 * AddToProductMessageQ(ReadRxPhysicalData);
				 * terminatorOptionalCheckCounter=0;
				 * }
				 * }
				 */
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
				ApplicationLauncher.logger.debug(
						"SerialRxPhysicalTask: Stopped :" + rxLabel + " : Port: " + SerialPortObj.getSystemPortName());
			}
		}

	}
	// =========================================================================================================================================

	public String getResponseMessageTerminator() {
		return responseMessageTerminator;
	}

	public void setResponseMessageTerminator(String messageTerminator) {
		this.responseMessageTerminator = messageTerminator;
	}

	// =========================================================================================================================================
	public boolean isTerminatorMandatory() {
		return terminatorMandatory;
	}

	public void setTerminatorMandatory(boolean terminatorMandatory) {
		this.terminatorMandatory = terminatorMandatory;
	}

}
