package com.tasnetwork.calibration.energymeter.device;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.StringUtils;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantDut;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.calibration.energymeter.device.SerialDataDUT.calibrationMenuTaskV2;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

public class SerialDataDUT {
	
	Communicator SerialPortObj= null;
	Boolean ExpectedResponseRecieved = false;
	int lduAddress = 1;
	Timer timer;
	int responseWaitCount = 20;//initial 20, changed to 40 for lscs ldu stack
	int RetryIntervalInMsec=200;//1000;
	int numberOfReadingRequired = 1;
	int numberOfReadingObtained = 0;
	String timeStampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
	long messageTimeStamp = 0;
	ArrayList<Long> messageTimeStampList = new ArrayList<Long>(0);
	String dutReadSerialData = "";
	int lastPatternMatchingCount = 0;
	Integer ReceivedLength=0;
	private volatile String expectedResponseTerminatorInHex = "";
	private volatile boolean dutReadDataFlag = true;
	
	private static ArrayList<String> ResultStatus = new ArrayList<String>(Collections.nCopies(49,"NOP"));
	private static ArrayList<String> ErrorValue = new ArrayList<String>(Collections.nCopies(49,"NOP"));
	private static ArrayList<String> noOfPulsesCounted = new ArrayList<String>(Collections.nCopies(49,"NOP"));
	private static ArrayList<Integer> receivedReadingIndex = new ArrayList<Integer>(Collections.nCopies(49,0));
	
	public SerialDataDUT(Communicator inpSerialPortObj, int address){
		SerialPortObj = inpSerialPortObj;
		lduAddress = address;
	}
	
	
	public void SerialReponseTimerStartV2(int seconds, int noOfReadingRequired) {
		timer = new Timer();
		timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);

		responseWaitCount = seconds;
		setNumberOfReadingRequired( noOfReadingRequired);
		ApplicationLauncher.logger.info("DUT SerialReponseTimerStart :DUT_Task Triggered:");
	}

	
	public void SerialReponseTimerStartV3(int seconds, int noOfReadingRequired, String expectedRespTerminatorInHex) {
		timer = new Timer();
		timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);

		responseWaitCount = seconds;
		setNumberOfReadingRequired( noOfReadingRequired);
		setExpectedResponseTerminatorInHex(expectedRespTerminatorInHex);
		ApplicationLauncher.logger.info("DUT SerialReponseTimerStart :DUT_Task Triggered:");
	}
	public void SerialReponseTimerStart(int seconds, int noOfReadingRequired) {
		timer = new Timer();
		timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);

		responseWaitCount = seconds;
		setNumberOfReadingRequired( noOfReadingRequired);
		ApplicationLauncher.logger.info("DUT SerialReponseTimerStart :DUT_Task Triggered:");
	}
	
	public int getNumberOfReadingRequired() {
		return this.numberOfReadingRequired;
	}

	public void setNumberOfReadingRequired(int numberOfReadings) {
		ApplicationLauncher.logger.info("setNumberOfReadingRequired: numberOfReadings: "+ numberOfReadings);
		this.numberOfReadingRequired = numberOfReadings;
	}

	public int getNumberOfReadingObtained() {
		return numberOfReadingObtained;
	}

	public void setNumberOfReadingObtained(int numberOfReadingObtained) {
		this.numberOfReadingObtained = numberOfReadingObtained;
	}
	
	public void incrementNumberOfReadingObtained() {
		this.numberOfReadingObtained++;
	}

	public void setReceivedReadingIndex(int rcdIndex,int Address) {

		//ApplicationLauncher.logger.info("setReceivedReadingIndex: "+ Address +":"+rcdIndex);
		this.receivedReadingIndex.set(Address , rcdIndex);

	}
	
	class calibrationMenuTaskV2 extends TimerTask {
		public void run() {
			String CurrentSerialData = SerialPortObj.getSerialData();
			long timeStamp = 0;
			ApplicationLauncher.logger.info("calibrationMenuTaskV2: CurrentSerialData: "+CurrentSerialData);
			Integer ExpectedLength= SerialPortObj.getExpectedLength();
			ApplicationLauncher.logger.info("getExpectedLength():"+SerialPortObj.getExpectedLength());
			String ExpectedResult = SerialPortObj.getExpectedResult();
			if((CurrentSerialData.length() == ExpectedLength) )	{
				ApplicationLauncher.logger.info("calibrationMenuTaskV2: ExpectedData:"+SerialPortObj.getExpectedResult());
				//String ExpectedResult = SerialPortObj.getExpectedResult();
				if (ExpectedResult.equals("")){
					ExpectedResponseRecieved=true;
					setDUT_ReadSerialData(CurrentSerialData);
					//setLduReadSerialData(CurrentSerialData,getLduAddress());
					setReceivedLength(CurrentSerialData.length());
					ApplicationLauncher.logger.debug("calibrationMenuTaskV2 Expected Data received:Length1:"+CurrentSerialData.length()+":CurrentData:"+CurrentSerialData);
				//}else if (CurrentSerialData.contains(ExpectedResult)){
				}else if ((CurrentSerialData.startsWith(ExpectedResult)) && (CurrentSerialData.endsWith(getExpectedResponseTerminatorInHex()) )){
					ExpectedResponseRecieved=true;
					setDUT_ReadSerialData(CurrentSerialData);
					//setLduReadSerialData(CurrentSerialData,getLduAddress());
					setReceivedLength(CurrentSerialData.length());
					ApplicationLauncher.logger.debug("calibrationMenuTaskV2 Expected Data received:Length2:"+CurrentSerialData.length()+":CurrentData:"+CurrentSerialData);
				}else{
					ApplicationLauncher.logger.info("calibrationMenuTaskV2: Expected Data not received:CurrentData:"+CurrentSerialData+":ExpectedData:"+SerialPortObj.getExpectedResult());
					//if(ExpectedLength == ) {
					//SerialPortObj.StripLength(CurrentSerialData.length());
					//}
				}
			}/*else if(CurrentSerialData.length() == (ExpectedLength+2)) {
				ApplicationLauncher.logger.info("calibrationMenuTaskV2: Test2");
				
				if (CurrentSerialData.contains(ExpectedResult)){
					ExpectedResponseRecieved=true;
					setDUT_ReadSerialData(CurrentSerialData);
					//setLduReadSerialData(CurrentSerialData,getLduAddress());
					setReceivedLength(CurrentSerialData.length());
					ApplicationLauncher.logger.debug("calibrationMenuTaskV2 Expected Data received:Length3:"+CurrentSerialData.length()+":CurrentData:"+CurrentSerialData);
					ApplicationLauncher.logger.debug("calibrationMenuTaskV2 Expected Data received: CurrentData: "+GUIUtils.hexToAscii(CurrentSerialData));
				//ApplicationLauncher.logger.info("calibrationMenuTaskV2: Expected Data not received:CurrentData:"+CurrentSerialData+":ExpectedData:"+SerialPortObj.getExpectedResult());
				}
				//SerialPortObj.StripLength(CurrentSerialData.length());
				//}
			}*/else {
				if (CurrentSerialData.contains(ExpectedResult)){
					//incrementNumberOfReadingObtained();
					ApplicationLauncher.logger.debug("calibrationMenuTaskV2 Expected Data received:Length4:"+CurrentSerialData.length()+":CurrentData:"+CurrentSerialData);
					ApplicationLauncher.logger.debug("calibrationMenuTaskV2 Expected Data received:Length4a:"+CurrentSerialData.length()+":CurrentData:"+GuiUtils.hexToAscii(CurrentSerialData));
					int patternMatchingCount = StringUtils.countMatches(CurrentSerialData, ExpectedResult);
					long currentTimeEpoch = 0;
					ApplicationLauncher.logger.debug("calibrationMenuTaskV2 : lastPatternMatchingCount : "+ lastPatternMatchingCount);
					if(lastPatternMatchingCount < patternMatchingCount){
						
						
						timeStampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());;
						currentTimeEpoch = GuiUtils.calcEpoch(timeStampFormat);
						ApplicationLauncher.logger.debug("calibrationMenuTaskV2 : adding messageTimeStampList : currentTimeEpoch: "+ currentTimeEpoch);
						//setMessageTimeStamp(currentTimeEpoch);
						addMessageTimeStampList(currentTimeEpoch);
					}
					
					lastPatternMatchingCount = patternMatchingCount;
					ApplicationLauncher.logger.debug("calibrationMenuTaskV2 : patternMatchingCount : "+ patternMatchingCount);
					
					if(patternMatchingCount >= getNumberOfReadingRequired()) {
						ApplicationLauncher.logger.debug("calibrationMenuTaskV2 : patternMatchingCount matched");
						int terminatorCount = StringUtils.countMatches(CurrentSerialData, getExpectedResponseTerminatorInHex());//ConstantDut.ER_TERMINATOR);
						ApplicationLauncher.logger.debug("calibrationMenuTaskV2 : terminatorCount :" + terminatorCount);
						
						if(terminatorCount >= getNumberOfReadingRequired()) {
							
							timeStampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());;
							currentTimeEpoch = GuiUtils.calcEpoch(timeStampFormat);
							setMessageTimeStamp(currentTimeEpoch);
							ApplicationLauncher.logger.debug("calibrationMenuTaskV2 : terminatorCount matched");
							ExpectedResponseRecieved=true;
							setDUT_ReadSerialData(CurrentSerialData);
							//setLduReadSerialData(CurrentSerialData,getLduAddress());
							setReceivedLength(CurrentSerialData.length());
							ApplicationLauncher.logger.debug("calibrationMenuTaskV2 Expected Data received:Length5:"+CurrentSerialData.length()+":CurrentData:"+CurrentSerialData);
							ApplicationLauncher.logger.debug("calibrationMenuTaskV2 Expected Data received:Length5a:"+CurrentSerialData.length()+":CurrentData:"+GuiUtils.hexToAscii(CurrentSerialData));
						}
					}
				//ApplicationLauncher.logger.info("calibrationMenuTaskV2: Expected Data not received:CurrentData:"+CurrentSerialData+":ExpectedData:"+SerialPortObj.getExpectedResult());
				}
			}
			responseWaitCount --;
			if (responseWaitCount == 0 || ExpectedResponseRecieved  || ProjectExecutionController.getUserAbortedFlag()){
				ApplicationLauncher.logger.info("calibrationMenuTaskV2 : Timer Exit:");
				timer.cancel(); //Terminate the timer thread
			}
			else{
				
				renewTaskV2(lduAddress);
				/*if(DeviceDataManagerController.getDUT1_ReadDataFlag()){
					timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);nv v 
				}*/
			}

		}
	}
	
	
public void renewTaskV2(int ldu_Address){
		
		if(ProcalFeatureEnable.DUT_EXECUTION_PROCESS_IN_PARALLEL){
			if(isDutReadDataFlag()){
				ApplicationLauncher.logger.info("renewTaskV2 : dut :");
				timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
			}
		}else{
		
			switch (ldu_Address) {
	
				case ConstantDut.DUT1_ADDRESS:
					if(DeviceDataManagerController.getDUT1_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu1:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
				
					break;
					
				case ConstantDut.DUT2_ADDRESS:
					if(DeviceDataManagerController.getDUT2_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu2:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
					
				case ConstantDut.DUT3_ADDRESS:
					if(DeviceDataManagerController.getDUT3_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu3:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
					
				case ConstantDut.DUT4_ADDRESS:
					if(DeviceDataManagerController.getDUT4_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu4:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
					
				case ConstantDut.DUT5_ADDRESS:
					if(DeviceDataManagerController.getDUT5_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu5:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
					
				case ConstantDut.DUT6_ADDRESS:
					if(DeviceDataManagerController.getDUT6_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu6:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
					
				case ConstantDut.DUT7_ADDRESS:
					if(DeviceDataManagerController.getDUT7_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu7:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
					
				case ConstantDut.DUT8_ADDRESS:
					if(DeviceDataManagerController.getDUT8_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu8:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
					
				case ConstantDut.DUT9_ADDRESS:
					if(DeviceDataManagerController.getDUT9_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu9:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
					
				case ConstantDut.DUT10_ADDRESS:
					if(DeviceDataManagerController.getDUT10_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu10:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
					
				case ConstantDut.DUT11_ADDRESS:
					if(DeviceDataManagerController.getDUT11_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu11:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
					
				case ConstantDut.DUT12_ADDRESS:
					if(DeviceDataManagerController.getDUT12_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu12:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
					
				case ConstantDut.DUT13_ADDRESS:
					if(DeviceDataManagerController.getDUT13_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu13:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
					
				case ConstantDut.DUT14_ADDRESS:
					if(DeviceDataManagerController.getDUT14_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu14:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
					
				case ConstantDut.DUT15_ADDRESS:
					if(DeviceDataManagerController.getDUT15_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu15:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
					
				case ConstantDut.DUT16_ADDRESS:
					if(DeviceDataManagerController.getDUT16_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu16:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
					
				case ConstantDut.DUT17_ADDRESS:
					if(DeviceDataManagerController.getDUT17_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu17:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
					
				case ConstantDut.DUT18_ADDRESS:
					if(DeviceDataManagerController.getDUT18_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu18:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
	
				case ConstantDut.DUT19_ADDRESS:
					if(DeviceDataManagerController.getDUT19_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu19:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT20_ADDRESS:
					if(DeviceDataManagerController.getDUT20_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu20:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT21_ADDRESS:
					if(DeviceDataManagerController.getDUT21_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu21:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT22_ADDRESS:
					if(DeviceDataManagerController.getDUT22_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu22:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT23_ADDRESS:
					if(DeviceDataManagerController.getDUT23_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu23:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
	
				case ConstantDut.DUT24_ADDRESS:
					if(DeviceDataManagerController.getDUT24_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu24:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
	
				case ConstantDut.DUT25_ADDRESS:
					if(DeviceDataManagerController.getDUT25_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu25:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT26_ADDRESS:
					if(DeviceDataManagerController.getDUT26_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu26:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT27_ADDRESS:
					if(DeviceDataManagerController.getDUT27_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu27:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT28_ADDRESS:
					if(DeviceDataManagerController.getDUT28_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu28:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
	
				case ConstantDut.DUT29_ADDRESS:
					if(DeviceDataManagerController.getDUT29_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu29:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT30_ADDRESS:
					if(DeviceDataManagerController.getDUT30_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu30:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT31_ADDRESS:
					if(DeviceDataManagerController.getDUT31_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu31:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT32_ADDRESS:
					if(DeviceDataManagerController.getDUT32_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu32:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT33_ADDRESS:
					if(DeviceDataManagerController.getDUT33_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu33:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT34_ADDRESS:
					if(DeviceDataManagerController.getDUT34_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu34:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
	
				case ConstantDut.DUT35_ADDRESS:
					if(DeviceDataManagerController.getDUT35_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu35:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT36_ADDRESS:
					if(DeviceDataManagerController.getDUT36_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu36:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT37_ADDRESS:
					if(DeviceDataManagerController.getDUT37_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu37:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT38_ADDRESS:
					if(DeviceDataManagerController.getDUT38_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu38:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT39_ADDRESS:
					if(DeviceDataManagerController.getDUT39_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu39:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT40_ADDRESS:
					if(DeviceDataManagerController.getDUT40_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu40:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT41_ADDRESS:
					if(DeviceDataManagerController.getDUT41_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu41:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
	
				case ConstantDut.DUT42_ADDRESS:
					if(DeviceDataManagerController.getDUT42_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu42:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT43_ADDRESS:
					if(DeviceDataManagerController.getDUT43_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu43:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT44_ADDRESS:
					if(DeviceDataManagerController.getDUT44_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu44:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT45_ADDRESS:
					if(DeviceDataManagerController.getDUT45_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu45:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT46_ADDRESS:
					if(DeviceDataManagerController.getDUT46_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu46:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT47_ADDRESS:
					if(DeviceDataManagerController.getDUT47_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu47:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT48_ADDRESS:
					if(DeviceDataManagerController.getDUT48_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTaskV2 : ldu48:");
						timer.schedule(new calibrationMenuTaskV2(), RetryIntervalInMsec);
					}
					break;
	
				default:
					break;
			}
		}
	}


	public int getReceivedReadingIndex(int Address) {
		return this.receivedReadingIndex.get(Address);
	}
	
	class calibrationMenuTask extends TimerTask {
		public void run() {
			String CurrentSerialData = SerialPortObj.getSerialData();
			long timeStamp = 0;
			ApplicationLauncher.logger.info("calibrationMenuTask: CurrentSerialData: "+CurrentSerialData);
			Integer ExpectedLength= SerialPortObj.getExpectedLength();
			ApplicationLauncher.logger.info("getExpectedLength():"+SerialPortObj.getExpectedLength());
			String ExpectedResult = SerialPortObj.getExpectedResult();
			if((CurrentSerialData.length() == ExpectedLength) )	{
				ApplicationLauncher.logger.info("calibrationMenuTask: ExpectedData:"+SerialPortObj.getExpectedResult());
				//String ExpectedResult = SerialPortObj.getExpectedResult();
				if (ExpectedResult.equals("")){
					ExpectedResponseRecieved=true;
					setDUT_ReadSerialData(CurrentSerialData);
					//setLduReadSerialData(CurrentSerialData,getLduAddress());
					setReceivedLength(CurrentSerialData.length());
					ApplicationLauncher.logger.debug("calibrationMenuTask Expected Data received:Length1:"+CurrentSerialData.length()+":CurrentData:"+CurrentSerialData);
				}else if (CurrentSerialData.contains(ExpectedResult)){
					ExpectedResponseRecieved=true;
					setDUT_ReadSerialData(CurrentSerialData);
					//setLduReadSerialData(CurrentSerialData,getLduAddress());
					setReceivedLength(CurrentSerialData.length());
					ApplicationLauncher.logger.debug("calibrationMenuTask Expected Data received:Length2:"+CurrentSerialData.length()+":CurrentData:"+CurrentSerialData);
				}else{
					ApplicationLauncher.logger.info("calibrationMenuTask: Expected Data not received:CurrentData:"+CurrentSerialData+":ExpectedData:"+SerialPortObj.getExpectedResult());
					//if(ExpectedLength == ) {
					//SerialPortObj.StripLength(CurrentSerialData.length());
					//}
				}
			}/*else if(CurrentSerialData.length() == (ExpectedLength+2)) {
				ApplicationLauncher.logger.info("calibrationMenuTask: Test2");
				
				if (CurrentSerialData.contains(ExpectedResult)){
					ExpectedResponseRecieved=true;
					setDUT_ReadSerialData(CurrentSerialData);
					//setLduReadSerialData(CurrentSerialData,getLduAddress());
					setReceivedLength(CurrentSerialData.length());
					ApplicationLauncher.logger.debug("calibrationMenuTask Expected Data received:Length3:"+CurrentSerialData.length()+":CurrentData:"+CurrentSerialData);
					ApplicationLauncher.logger.debug("calibrationMenuTask Expected Data received: CurrentData: "+GUIUtils.hexToAscii(CurrentSerialData));
				//ApplicationLauncher.logger.info("calibrationMenuTask: Expected Data not received:CurrentData:"+CurrentSerialData+":ExpectedData:"+SerialPortObj.getExpectedResult());
				}
				//SerialPortObj.StripLength(CurrentSerialData.length());
				//}
			}*/else {
				if (CurrentSerialData.contains(ExpectedResult)){
					//incrementNumberOfReadingObtained();
					ApplicationLauncher.logger.debug("calibrationMenuTask Expected Data received:Length4:"+CurrentSerialData.length()+":CurrentData:"+CurrentSerialData);
					ApplicationLauncher.logger.debug("calibrationMenuTask Expected Data received:Length4a:"+CurrentSerialData.length()+":CurrentData:"+GuiUtils.hexToAscii(CurrentSerialData));
					int patternMatchingCount = StringUtils.countMatches(CurrentSerialData, ExpectedResult);
					long currentTimeEpoch = 0;
					ApplicationLauncher.logger.debug("calibrationMenuTask : lastPatternMatchingCount : "+ lastPatternMatchingCount);
					if(lastPatternMatchingCount < patternMatchingCount){
						
						
						timeStampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());;
						currentTimeEpoch = GuiUtils.calcEpoch(timeStampFormat);
						ApplicationLauncher.logger.debug("calibrationMenuTask : adding messageTimeStampList : currentTimeEpoch: "+ currentTimeEpoch);
						//setMessageTimeStamp(currentTimeEpoch);
						addMessageTimeStampList(currentTimeEpoch);
					}
					
					lastPatternMatchingCount = patternMatchingCount;
					ApplicationLauncher.logger.debug("calibrationMenuTask : patternMatchingCount : "+ patternMatchingCount);
					
					if(patternMatchingCount >= getNumberOfReadingRequired()) {
						ApplicationLauncher.logger.debug("calibrationMenuTask : patternMatchingCount matched");
						int terminatorCount = StringUtils.countMatches(CurrentSerialData, getExpectedResponseTerminatorInHex());//ConstantDut.ER_TERMINATOR);
						ApplicationLauncher.logger.debug("calibrationMenuTask : terminatorCount :" + terminatorCount);
						
						if(terminatorCount >= getNumberOfReadingRequired()) {
							
							timeStampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());;
							currentTimeEpoch = GuiUtils.calcEpoch(timeStampFormat);
							setMessageTimeStamp(currentTimeEpoch);
							ApplicationLauncher.logger.debug("calibrationMenuTask : terminatorCount matched");
							ExpectedResponseRecieved=true;
							setDUT_ReadSerialData(CurrentSerialData);
							//setLduReadSerialData(CurrentSerialData,getLduAddress());
							setReceivedLength(CurrentSerialData.length());
							ApplicationLauncher.logger.debug("calibrationMenuTask Expected Data received:Length5:"+CurrentSerialData.length()+":CurrentData:"+CurrentSerialData);
							ApplicationLauncher.logger.debug("calibrationMenuTask Expected Data received:Length5a:"+CurrentSerialData.length()+":CurrentData:"+GuiUtils.hexToAscii(CurrentSerialData));
						}
					}
				//ApplicationLauncher.logger.info("calibrationMenuTask: Expected Data not received:CurrentData:"+CurrentSerialData+":ExpectedData:"+SerialPortObj.getExpectedResult());
				}
			}
			responseWaitCount --;
			if (responseWaitCount == 0 || ExpectedResponseRecieved  || ProjectExecutionController.getUserAbortedFlag()){
				ApplicationLauncher.logger.info("calibrationMenuTask : Timer Exit:");
				timer.cancel(); //Terminate the timer thread
			}
			else{
				
				renewTask(lduAddress);
				/*if(DeviceDataManagerController.getDUT1_ReadDataFlag()){
					timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);nv v 
				}*/
			}

		}
	}
	
	public Boolean IsExpectedResponseReceived(){

		while((!ExpectedResponseRecieved)&&(responseWaitCount!=0)){
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT IsExpectedResponseReceived :Exception:"+ e.getMessage());
			}
		};
		return this.ExpectedResponseRecieved;
	}
	
	public void renewTask(int ldu_Address){
		
		if(ProcalFeatureEnable.DUT_EXECUTION_PROCESS_IN_PARALLEL){
			if(isDutReadDataFlag()){
				ApplicationLauncher.logger.info("renewTask : dut :");
				timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
			}
		}else{
		
			switch (ldu_Address) {
	
				case ConstantDut.DUT1_ADDRESS:
					if(DeviceDataManagerController.getDUT1_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu1:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
				
					break;
					
				case ConstantDut.DUT2_ADDRESS:
					if(DeviceDataManagerController.getDUT2_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu2:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
					
				case ConstantDut.DUT3_ADDRESS:
					if(DeviceDataManagerController.getDUT3_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu3:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
					
				case ConstantDut.DUT4_ADDRESS:
					if(DeviceDataManagerController.getDUT4_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu4:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
					
				case ConstantDut.DUT5_ADDRESS:
					if(DeviceDataManagerController.getDUT5_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu5:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
					
				case ConstantDut.DUT6_ADDRESS:
					if(DeviceDataManagerController.getDUT6_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu6:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
					
				case ConstantDut.DUT7_ADDRESS:
					if(DeviceDataManagerController.getDUT7_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu7:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
					
				case ConstantDut.DUT8_ADDRESS:
					if(DeviceDataManagerController.getDUT8_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu8:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
					
				case ConstantDut.DUT9_ADDRESS:
					if(DeviceDataManagerController.getDUT9_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu9:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
					
				case ConstantDut.DUT10_ADDRESS:
					if(DeviceDataManagerController.getDUT10_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu10:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
					
				case ConstantDut.DUT11_ADDRESS:
					if(DeviceDataManagerController.getDUT11_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu11:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
					
				case ConstantDut.DUT12_ADDRESS:
					if(DeviceDataManagerController.getDUT12_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu12:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
					
				case ConstantDut.DUT13_ADDRESS:
					if(DeviceDataManagerController.getDUT13_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu13:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
					
				case ConstantDut.DUT14_ADDRESS:
					if(DeviceDataManagerController.getDUT14_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu14:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
					
				case ConstantDut.DUT15_ADDRESS:
					if(DeviceDataManagerController.getDUT15_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu15:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
					
				case ConstantDut.DUT16_ADDRESS:
					if(DeviceDataManagerController.getDUT16_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu16:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
					
				case ConstantDut.DUT17_ADDRESS:
					if(DeviceDataManagerController.getDUT17_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu17:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
					
				case ConstantDut.DUT18_ADDRESS:
					if(DeviceDataManagerController.getDUT18_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu18:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
	
				case ConstantDut.DUT19_ADDRESS:
					if(DeviceDataManagerController.getDUT19_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu19:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT20_ADDRESS:
					if(DeviceDataManagerController.getDUT20_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu20:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT21_ADDRESS:
					if(DeviceDataManagerController.getDUT21_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu21:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT22_ADDRESS:
					if(DeviceDataManagerController.getDUT22_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu22:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT23_ADDRESS:
					if(DeviceDataManagerController.getDUT23_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu23:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
	
				case ConstantDut.DUT24_ADDRESS:
					if(DeviceDataManagerController.getDUT24_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu24:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
	
				case ConstantDut.DUT25_ADDRESS:
					if(DeviceDataManagerController.getDUT25_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu25:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT26_ADDRESS:
					if(DeviceDataManagerController.getDUT26_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu26:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT27_ADDRESS:
					if(DeviceDataManagerController.getDUT27_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu27:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT28_ADDRESS:
					if(DeviceDataManagerController.getDUT28_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu28:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
	
				case ConstantDut.DUT29_ADDRESS:
					if(DeviceDataManagerController.getDUT29_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu29:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT30_ADDRESS:
					if(DeviceDataManagerController.getDUT30_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu30:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT31_ADDRESS:
					if(DeviceDataManagerController.getDUT31_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu31:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT32_ADDRESS:
					if(DeviceDataManagerController.getDUT32_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu32:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT33_ADDRESS:
					if(DeviceDataManagerController.getDUT33_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu33:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT34_ADDRESS:
					if(DeviceDataManagerController.getDUT34_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu34:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
	
				case ConstantDut.DUT35_ADDRESS:
					if(DeviceDataManagerController.getDUT35_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu35:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT36_ADDRESS:
					if(DeviceDataManagerController.getDUT36_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu36:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT37_ADDRESS:
					if(DeviceDataManagerController.getDUT37_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu37:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT38_ADDRESS:
					if(DeviceDataManagerController.getDUT38_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu38:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT39_ADDRESS:
					if(DeviceDataManagerController.getDUT39_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu39:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT40_ADDRESS:
					if(DeviceDataManagerController.getDUT40_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu40:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT41_ADDRESS:
					if(DeviceDataManagerController.getDUT41_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu41:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
	
				case ConstantDut.DUT42_ADDRESS:
					if(DeviceDataManagerController.getDUT42_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu42:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT43_ADDRESS:
					if(DeviceDataManagerController.getDUT43_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu43:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT44_ADDRESS:
					if(DeviceDataManagerController.getDUT44_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu44:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT45_ADDRESS:
					if(DeviceDataManagerController.getDUT45_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu45:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT46_ADDRESS:
					if(DeviceDataManagerController.getDUT46_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu46:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT47_ADDRESS:
					if(DeviceDataManagerController.getDUT47_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu47:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
				case ConstantDut.DUT48_ADDRESS:
					if(DeviceDataManagerController.getDUT48_ReadDataFlag()){
						ApplicationLauncher.logger.info("renewTask : ldu48:");
						timer.schedule(new calibrationMenuTask(), RetryIntervalInMsec);
					}
					break;
	
				default:
					break;
			}
		}
	}
	
	public long getMessageTimeStamp() {
		return messageTimeStamp;
	}

	public void setMessageTimeStamp(long messageTimeStamp) {
		this.messageTimeStamp = messageTimeStamp;
	}

	public ArrayList<Long> getMessageTimeStampList() {
		return messageTimeStampList;
	}

	public void setMessageTimeStampList(ArrayList<Long> messageTimeStampList) {
		this.messageTimeStampList = messageTimeStampList;
	}
	
	public void addMessageTimeStampList(Long messageTimeStamp) {
		this.messageTimeStampList.add(messageTimeStamp);
	}

	public boolean isDutReadDataFlag() {
		return dutReadDataFlag;
	}

	public void setDutReadDataFlag(boolean dutReadData) {
		this.dutReadDataFlag = dutReadData;
	}
	
	public void setDUT_ReadSerialData(String currentSerialData) {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.debug("setLDU_ReadSerialData: Entry");
		dutReadSerialData = currentSerialData;

	}

	public  void ClearDUT_ReadSerialData() {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.debug("ClearLDU_ReadSerialData: Entry");
		dutReadSerialData = "";
		
		

	}

	public  String getDUT_ReadSerialData() {

		return dutReadSerialData;

	}
	
	public void setReceivedLength(Integer length){

		this.ReceivedLength = length;
	}

	public Integer getReceivedLength(){

		return this.ReceivedLength;
	}
	
	public String getExpectedResponseTerminatorInHex() {
		return expectedResponseTerminatorInHex;
	}

	public void setExpectedResponseTerminatorInHex(String expectedResponseTerminatorInHex) {
		this.expectedResponseTerminatorInHex = expectedResponseTerminatorInHex;
	}

}
