package com.tasnetwork.calibration.conveyor.bay.calib;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.DevSysEnergyMeter;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
//import com.tasnetwork.calibration.conveyor.bay_functionaltest.FunctionalTestBay;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.dutprocess.ParallelTaskManager;
import com.tasnetwork.calibration.conveyor.pallet.CalibrationSummaryProcessor;
import com.tasnetwork.calibration.conveyor.pallet.PalletTrackerController;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.spring.orm.model.PalletManage;

import javafx.application.Platform;

public class S058_10_Neutral_Calibration implements CalibrationBayState {
	//private static DutManager dutManager =  new DutManager();
	/*String LOW   = "OPEN";
	String HIGH  = "CLOSE";
	String CLOSE = "On";
	String OPEN  = "Off";*/

    private final CalibrationSummaryProcessor calibrationSummaryProcessor = CalibrationSummaryProcessor.getInstance();

	String UNLOCKED       = "Unlocked";
	String LOCKED         = "Locked";
	String START          = "Start";
	String EXPECTED_DATA  = "02" ;
	String TERMINATOR     = "030D0A" ;

	String resultStatus = "";
	String resultValue = "";
	String testType = "";
	String testCaseName = "";

	private String myBayKey = ConstantConveyor.CALIBRATION_BAY_KEY;

	boolean allPass = false; 

	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Calib.logger.info("S058_10_Neutral_Calibration : Entry");

		//BayUtils.delay(15000); // 15sec wait time for current to set

		/*for(int i = 1; i <= 15; i ++) {
			CalibrationBay.logger.info("S058_10_Neutral_Calibration : Waiting for Neutral CT Current Set: " + i + "secs");
			BayUtils.delay(1000);
		}*/

		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		Map<String,Object> responseReturn =  neutralCalibrationTask();	 
		boolean neutralCalibrationTask = (boolean)responseReturn.get("status");

		boolean status = neutralCalibrationTask;  // Call the function to perform calibration test

		//boolean status = true;
		
		if (status) {
			Calib.logger.info("S058_10_Neutral_Calibration : Calibration Test Successful");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);  // Success error code

			//BayUtils.delay(60000); // 1min wait time for Calibration Process

			for(int i = 1; i <= ConstantConveyor.NEUTRAL_CALIB_WAIT_TIME; i ++) {
				Calib.logger.info("S058_10_Neutral_Calibration : Waiting for Neutral Calib: T-" + (ConstantConveyor.NEUTRAL_CALIB_WAIT_TIME-i) + "secs");
				BayUtils.delay(1000);
			}
			
			/*if (ProconFeatureEnable.PLACE_OPTICAL_READERS) {
				int i = 0;
				
				while (!ConstantConveyor.isCALIB_OPTICAL_REMOVED()) {
					i++;
					int i_final = i;
					Platform.runLater(() -> {
						StateExecutorController.ref_tf_CALIB_prompt.setText("Remove Optical Readers - " + i_final + " secs");
					});
					
					BayUtils.delay(1000);
					CalibrationBay.logger.info("S058_10_Neutral_Calibration : Waiting to remove Optical Reader ");
				}
				
				ConstantConveyor.setCALIB_OPTICAL_REMOVED(false);
				i = 0;
				Platform.runLater(() -> {
					StateExecutorController.ref_tf_CALIB_prompt.clear();
				});
			}	*/
			
			/*// WITH IR BAY LAMP TOGGLE
			if (ProconFeatureEnable.PLACE_OPTICAL_READERS) {

				boolean toggle = false; // This flag will be used to alternate
				long startTime = System.currentTimeMillis();
				
				StateExecutorController.getRef_btn_CalibRemove().setDisable(false);

				CalibrationBay.logger.debug("S081_stop_FT_source : CALIB_OPTICAL_REMOVED :" + ConstantConveyor.isCALIB_OPTICAL_REMOVED());
				
				while (!ConstantConveyor.isCALIB_OPTICAL_REMOVED()) {
					long elapsedTime = (System.currentTimeMillis() - startTime) / 1000; // in seconds

					Platform.runLater(() -> {
						StateExecutorController.ref_tf_CALIB_prompt.setText("Remove Optical Readers - " + elapsedTime + " secs");
					});

					// Alternate the tower lamp state
					if (toggle) {
						turn_on_tower_lamp1();  // Turn ON Lamp 1
						//turn_off_tower_lamp2(); // Turn OFF Lamp 2
					} else {
						turn_off_tower_lamp1(); // Turn OFF Lamp 1
						//turn_on_tower_lamp2();// Turn ON Lamp 2
					}
					
					toggle = !toggle; // Flip the flag for next iteration

					CalibrationBay.logger.debug("S081_stop_FT_source : Waiting to remove Optical Readers ");
				}
				BayUtils.delay(2000);
				ConstantConveyor.setCALIB_OPTICAL_REMOVED(false);
				StateExecutorController.getRef_btn_CalibRemove().setDisable(true);

				Platform.runLater(() -> {
					StateExecutorController.ref_tf_CALIB_prompt.clear();
				});
			}*/

		} else {
			Calib.logger.info("S058_10_Neutral_Calibration : Calibration Test Failed");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_010);  // Failure error code
		}



		Calib.logger.info("S058_10_Neutral_Calibration : Exit");
		return bayResponse;
	}

	//============================================================================================================================================  

  	private void turn_on_tower_lamp1() {

      	//============================================================================================
      	IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.IR_PORT_NAME_TWR_LAMP2);

      	BayUtils bayUtils = new BayUtils();

      	String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(), 
      			portInfo.getBayId(), 
      			portInfo.getPortId(),
      			Constant_IO_ActionMapping.OPEN); // Use CLOSE to represent turning the relay "On"
      }

      private void turn_off_tower_lamp1() {

      	//============================================================================================
      	IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.IR_PORT_NAME_TWR_LAMP2);

      	BayUtils bayUtils = new BayUtils();

      	String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(), 
      			portInfo.getBayId(), 
      			portInfo.getPortId(),
      			Constant_IO_ActionMapping.CLOSE); // Use CLOSE to represent turning the relay "On"
      }

      private void turn_on_tower_lamp2() {

      	//============================================================================================
      	IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.IR_PORT_NAME_TWR_LAMP1);

      	BayUtils bayUtils = new BayUtils();

      	String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(), 
      			portInfo.getBayId(), 
      			portInfo.getPortId(),
      			Constant_IO_ActionMapping.OPEN); // Use CLOSE to represent turning the relay "On"
      }

      private void turn_off_tower_lamp2() {

      	//============================================================================================
      	IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.IR_PORT_NAME_TWR_LAMP1);

      	BayUtils bayUtils = new BayUtils();

      	String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(), 
      			portInfo.getBayId(), 
      			portInfo.getPortId(),
      			Constant_IO_ActionMapping.CLOSE); // Use CLOSE to represent turning the relay "On"
      }
      
  	//============================================================================================================================================ 
	//============================================================================================================================================  

	public Map<String,Object> neutralCalibrationTask() {
		Calib.logger.debug("S058_10_Neutral_Calibration : neutralCalibrationTask : Entry");

		boolean status = false; 
		boolean status1 = false; 
		boolean status2 = false; 
		boolean status3 = false; 
		boolean status4 = false; 
		boolean status5 = false; 
		boolean status6 = false; 

		Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
		// 1. Start Source
		// pending

		// 2. set voltage and current to meters
		// pending

		// 3. Do calibration - send commands        


		/*boolean status1  = neutralCalibrationProcess(1); // meter 1
		 boolean status2  = neutralCalibrationProcess(2);*/
		ParallelTaskManager dutManager =  new ParallelTaskManager();
		boolean monitorAlreadyinitiated = false;
		int maxDeviceConnected = 6;
		if(ProconFeatureEnable.CALIB_DUT_EXECUTION_PROCESS_IN_PARALLEL){
			for(int positionNo =1; positionNo<= maxDeviceConnected; positionNo++){

				//if(positionNo==1 || positionNo == 6){
				dutManager.startDutCalibrationProcess(positionNo);
				if(!monitorAlreadyinitiated){
					dutManager.monitorDutControlProcessTrigger(); 
					monitorAlreadyinitiated = true;
				}
				//}
			}
			int dutWaitTimeDurationMaxInSec = 300;
			int dutWaitTimeCounter = 0;
			boolean dutAllProcessExecutionCompleted = false;

			PalletTrackerController palletTracker = new PalletTrackerController();
			String selectedBayTypeKey = getMyBayKey();
			String myPalletDistinctId = PalletTrackerController.getPresentPalletAtBayMap().get(selectedBayTypeKey);

			PalletManage myPalletManage = MySqlServiceManager.getPalletManageService().findFirstByPalletDistinctId(myPalletDistinctId);

			while ( (!ProjectExecutionController.getUserAbortedFlag()) && 
					(dutWaitTimeCounter < dutWaitTimeDurationMaxInSec) && 
					(!dutAllProcessExecutionCompleted) &&
	        		(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)){
				Sleep(1000);
				//dutWaitTimeDurationInSec--;
				dutWaitTimeCounter++;
				dutAllProcessExecutionCompleted = dutManager.isDutAllControlProcessCompleted();
				Calib.logger.debug("neutralCalibrationTask: dutWaitTimeCounter: "+ dutWaitTimeCounter + "/" + dutWaitTimeDurationMaxInSec+ " : dutAllProcessExecutionCompleted: " + dutAllProcessExecutionCompleted);

				//ApplicationHomeController.update_left_status("Creep: " + lduTimeDuration + " Sec",ConstantApp.LEFT_STATUS_DEBUG);

			}
			if(dutManager.isDutAllControlProcessCompleted()){
				//if(dutManager.isProjectExitProcess()){
				Calib.logger.debug("neutralCalibrationTask: All dut task completed");
				for(int positionNo =1; positionNo<= maxDeviceConnected; positionNo++){
					Calib.logger.debug("neutralCalibrationTask: result position Id : " + positionNo + " : "  + dutManager.getDutResultSummary(positionNo));
				}

				for (int positionNo =1; positionNo<= maxDeviceConnected; positionNo++) {
					String resultSummary = dutManager.getDutResultSummary(positionNo);
					Calib.logger.debug("S058_10_Neutral_Calibration : Result Summary : " + resultSummary);

					if (!(resultSummary.equals("P"))) { 
						allPass = false; 
						break; // No need to continue if one result is 'F'
					} else {
						allPass = true;
					}
				}

				int meterPassedCount = 0;
				int meterFailedCount = 0;

				for (int positionNo =1; positionNo<= ConstantConveyor.MAX_DEVICES_CONNECTED; positionNo++) {
					String resultSummary = dutManager.getDutResultSummary(positionNo);
					if (resultSummary.equals("P")) {
						resultStatus = "Pass";
						resultValue = "Pass";

						meterPassedCount++;
					} else {
						resultStatus = "Fail";
						resultValue = "Fail";

						meterFailedCount++;
					}

					testCaseName = ConstantConveyor.NEUTRAL_CALIB_RESULT_TEST_NAME; //"Calibration";
					testType = ConstantConveyor.CALIBRATION_RESULT_KEY; //"CALIB";
					palletTracker.addMeterResultSummary(positionNo, resultStatus, resultValue, getMyBayKey(), testType, testCaseName);
				}

				for (int positionNo = 1; positionNo <= maxDeviceConnected; positionNo++) {
					String resultSummary = dutManager.getDutResultSummary(positionNo);
					calibrationSummaryProcessor.addNeutralCalibrationResult(positionNo, resultSummary.equals("P") ? "Pass" : "Fail");
				}

				myPalletManage.setNoOfMeterPresent(meterPassedCount + meterFailedCount);
				myPalletManage.setNoOfMeterPassed(meterPassedCount);
				myPalletManage.setNoOfMeterFailed(meterFailedCount);

				MySqlServiceManager.getPalletManageService().saveToDb(myPalletManage);

				meterPassedCount = 0;
				meterFailedCount = 0;

				allPass = true;

				Calib.logger.debug("neutralCalibrationTask: allPass : " + allPass);

				responseReturn.put("status", allPass); 

				//ProjectExitProcess();
				//return status;
				//}
			}else{
				Calib.logger.debug("neutralCalibrationTask: Timed out");
			}

		}else{
			PalletTrackerController palletTracker = new PalletTrackerController();
			String selectedBayTypeKey = getMyBayKey();
			String myPalletDistinctId = PalletTrackerController.getPresentPalletAtBayMap().get(selectedBayTypeKey);

			PalletManage myPalletManage = MySqlServiceManager.getPalletManageService().findFirstByPalletDistinctId(myPalletDistinctId);

			if(ProconFeatureEnable.DEV_SYS_EM1_CONNECTED) {
				DevSysEnergyMeter devSysEnergyMeter = new DevSysEnergyMeter(Calib.logger);
				status1  = devSysEnergyMeter.neutralCalibrationProcess(1); // meter 1
				status2  = devSysEnergyMeter.neutralCalibrationProcess(2);
				status3  = devSysEnergyMeter.neutralCalibrationProcess(3);
				status4  = devSysEnergyMeter.neutralCalibrationProcess(4);
				status5  = devSysEnergyMeter.neutralCalibrationProcess(5);
				status6  = devSysEnergyMeter.neutralCalibrationProcess(6);
				Calib.logger.debug("S058_10_Neutral_Calibration : neutralCalibrationTask : Meter 1 : status : " + status1);
				Calib.logger.debug("S058_10_Neutral_Calibration : neutralCalibrationTask : Meter 2 : status : " + status2);
				Calib.logger.debug("S058_10_Neutral_Calibration : neutralCalibrationTask : Meter 3 : status : " + status3);
				Calib.logger.debug("S058_10_Neutral_Calibration : neutralCalibrationTask : Meter 4 : status : " + status4);
				Calib.logger.debug("S058_10_Neutral_Calibration : neutralCalibrationTask : Meter 5 : status : " + status5);
				Calib.logger.debug("S058_10_Neutral_Calibration : neutralCalibrationTask : Meter 6 : status : " + status6);
			}

			for(int positionNo =1; positionNo<= maxDeviceConnected; positionNo++){
				Calib.logger.debug("neutralCalibrationTask: result position Id : " + positionNo + " : "  + dutManager.getDutResultSummary(positionNo));
			}

			for (int positionNo =1; positionNo<= maxDeviceConnected; positionNo++) {
				String resultSummary = dutManager.getDutResultSummary(positionNo);
				Calib.logger.debug("S08_functional_Test: Result Summary : " + resultSummary);

				if (!(resultSummary.equals("P"))) { 
					allPass = false; 
					break; // No need to continue if one result is 'F'
				} else {
					allPass = true;
				}
			}

			int meterPassedCount = 0;
			int meterFailedCount = 0;

			for (int positionNo =1; positionNo<= ConstantConveyor.MAX_DEVICES_CONNECTED; positionNo++) {
				String resultSummary = dutManager.getDutResultSummary(positionNo);
				if (resultSummary.equals("P")) {
					resultStatus = "Pass";
					resultValue = "Pass";

					meterPassedCount++;
				} else {
					resultStatus = "Fail";
					resultValue = "Fail";

					meterFailedCount++;
				}

				testCaseName = ConstantConveyor.NEUTRAL_CALIB_RESULT_TEST_NAME; //"Calibration";
				testType = ConstantConveyor.CALIBRATION_RESULT_KEY; //"CALIB";
				palletTracker.addMeterResultSummary(positionNo, resultStatus, resultValue, getMyBayKey(), testType, testCaseName);
			}

			for (int positionNo = 1; positionNo <= maxDeviceConnected; positionNo++) {
				String resultSummary = dutManager.getDutResultSummary(positionNo);
				calibrationSummaryProcessor.addNeutralCalibrationResult(positionNo, resultSummary.equals("P") ? "Pass" : "Fail");
			}

			myPalletManage.setNoOfMeterPresent(meterPassedCount + meterFailedCount);
			myPalletManage.setNoOfMeterPassed(meterPassedCount);
			myPalletManage.setNoOfMeterFailed(meterFailedCount);

			MySqlServiceManager.getPalletManageService().saveToDb(myPalletManage);

			meterPassedCount = 0;
			meterFailedCount = 0;

			allPass = true;

			Calib.logger.debug("neutralCalibrationTask: allPass : " + allPass);

			responseReturn.put("status", allPass);

			if (status1 && status2 && status3 && status4  && status5  && status6) {
				status = true;
			} else {
				status = false;
			}

		}

		if(StateExecutorController.simulateCalibBayHappyPath){
			status = true; 
		}

		responseReturn.put("status", status); 

		Calib.logger.debug("S058_10_Neutral_Calibration : neutralCalibrationProcess : status : " + status);

		Calib.logger.debug("S058_10_Neutral_Calibration : neutralCalibrationProcess : Exit");
		return responseReturn;
	}


	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Calib.logger.error("Sleep :InterruptedException:"+ e.getMessage());
		}

	}

	public String getMyBayKey() {
		return myBayKey;
	}

	//============================================================================================================================================  


	//============================================================================================================================================  


}
