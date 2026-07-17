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
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.spring.orm.model.PalletManage;

import javafx.application.Platform;

public class S058_05_Phase_Calibration implements CalibrationBayState {
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
		Calib.logger.info("S058_05_Phase_Calibration : Entry");

		//BayUtils.delay(15000); // 15sec wait time for current to set
		
		/*for(int i = 1; i <= 15; i ++) {
			CalibrationBay.logger.info("S058_05_Phase_Calibration : Waiting for Main CT Current Set: " + i + "secs");
			BayUtils.delay(1000);
		}*/

		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		
		Calib.logger.info("S058_05_Phase_Calibration : Current Stable Check : Entry");
		// WITH IR BAY LAMP TOGGLE
		if (ProconFeatureEnable.WAIT_FOR_USER_INPUT) {

			boolean toggle = false; // This flag will be used to alternate
			long startTime = System.currentTimeMillis();

			StateExecutorController.getRef_btn_CalibCurrentStable().setDisable(false);

			while (!ConstantConveyor.isCALIB_CURRENT_STABLE()) {
				long elapsedTime = (System.currentTimeMillis() - startTime) / 1000; // in seconds

				Platform.runLater(() -> {
					StateExecutorController.ref_tf_CALIB_prompt.setText("Is Current Stable ? - " + elapsedTime + " secs");
				});

				// Alternate the tower lamp state
				if (toggle) {
					turn_on_tower_lamp2();  // Turn ON Lamp 1
					//turn_off_tower_lamp2(); // Turn OFF Lamp 2
				} else {
					turn_off_tower_lamp2(); // Turn OFF Lamp 1
					//turn_on_tower_lamp2();// Turn ON Lamp 2
				}

				toggle = !toggle; // Flip the flag for next iteration

				Calib.logger.debug("S058_05_Phase_Calibration : Waiting for Stable Current ");
			}

			BayUtils.delay(2000);
			ConstantConveyor.setCALIB_CURRENT_STABLE(false);
			StateExecutorController.getRef_btn_CalibCurrentStable().setDisable(true);

			Platform.runLater(() -> {
				StateExecutorController.ref_tf_CALIB_prompt.clear();
			});
		}

		Map<String,Object> responseReturn =  phasephaseCalibrationTask();	 
		boolean phaseCalibrationTask = (boolean)responseReturn.get("status");

		boolean status = phaseCalibrationTask;  // Call the function to perform calibration test

		if (status) {
			Calib.logger.info("S058_05_Phase_Calibration : Calibration Test Successful");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);  // Success error code

			//BayUtils.delay(60000); // 1min wait time for Calibration Process

			for(int i = 1; i <= ConstantConveyor.PHASE_CALIB_WAIT_TIME; i ++) {
				Calib.logger.info("S058_05_Phase_Calibration : Waiting for Phase Calib: T-" + (ConstantConveyor.PHASE_CALIB_WAIT_TIME-i) + "secs");
				BayUtils.delay(1000);
			}

		} else {
			Calib.logger.info("S058_05_Phase_Calibration : Calibration Test Failed");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_010);  // Failure error code
		}



		Calib.logger.info("S058_05_Phase_Calibration : Exit");
		return bayResponse;
	}

	//============================================================================================================================================  

	public Map<String,Object> phasephaseCalibrationTask() {
		Calib.logger.debug("S058_05_Phase_Calibration : phasephaseCalibrationTask : Entry");

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


		/*boolean status1  = phaseCalibrationProcess(1); // meter 1
		 boolean status2  = phaseCalibrationProcess(2);*/
		ParallelTaskManager dutManager =  new ParallelTaskManager();
		boolean monitorAlreadyinitiated = false;
		int maxDeviceConnected = 6;
		if(ProconFeatureEnable.CALIB_DUT_EXECUTION_PROCESS_IN_PARALLEL){
			for(int positionNo =1; positionNo<= maxDeviceConnected; positionNo++){

				//if(positionNo==1 || positionNo == 6){
				dutManager.startDutCalibrationProcess(positionNo);
				//dutManager.startDutPhaseCalibrationProcess(positionNo);
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
			ApplicationLauncher.logger.debug("phasephaseCalibrationTask: selectedBayTypeKey: "+ selectedBayTypeKey);
			ApplicationLauncher.logger.debug("phasephaseCalibrationTask: myPalletDistinctId: "+ myPalletDistinctId);
			PalletManage myPalletManage = MySqlServiceManager.getPalletManageService().findFirstByPalletDistinctId(myPalletDistinctId);
			if(myPalletManage==null){
				ApplicationLauncher.logger.debug("phasephaseCalibrationTask: myPalletManage: is null ");
			}
			while ( (!ProjectExecutionController.getUserAbortedFlag()) && 
					(dutWaitTimeCounter < dutWaitTimeDurationMaxInSec) && 
					(!dutAllProcessExecutionCompleted) &&
	        		(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)){
				Sleep(1000);
				//dutWaitTimeDurationInSec--;
				dutWaitTimeCounter++;
				dutAllProcessExecutionCompleted = dutManager.isDutAllControlProcessCompleted();
				Calib.logger.debug("phasephaseCalibrationTask: dutWaitTimeCounter: "+ dutWaitTimeCounter + "/" + dutWaitTimeDurationMaxInSec+ " : dutAllProcessExecutionCompleted: " + dutAllProcessExecutionCompleted);

				//ApplicationHomeController.update_left_status("Creep: " + lduTimeDuration + " Sec",ConstantApp.LEFT_STATUS_DEBUG);

			}
			if(dutManager.isDutAllControlProcessCompleted()){
				//if(dutManager.isProjectExitProcess()){
				Calib.logger.debug("phasephaseCalibrationTask: All dut task completed");
				for(int positionNo =1; positionNo<= maxDeviceConnected; positionNo++){
					Calib.logger.debug("phasephaseCalibrationTask: result position Id : " + positionNo + " : "  + dutManager.getDutResultSummary(positionNo));
				}

				for (int positionNo =1; positionNo<= maxDeviceConnected; positionNo++) {
					String resultSummary = dutManager.getDutResultSummary(positionNo);
					Calib.logger.debug("S058_05_Phase_Calibration: Result Summary : " + resultSummary);

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
					testCaseName = ConstantConveyor.PHASE_CALIB_RESULT_TEST_NAME; //"Calibration";
					testType = ConstantConveyor.CALIBRATION_RESULT_KEY; //"CALIB";
					palletTracker.addMeterResultSummary(positionNo, resultStatus, resultValue, getMyBayKey(), testType, testCaseName);
				}

				for (int positionNo = 1; positionNo <= maxDeviceConnected; positionNo++) {
				    String resultSummary = dutManager.getDutResultSummary(positionNo);
				    calibrationSummaryProcessor.addPhaseCalibrationResult(positionNo, resultSummary.equals("P") ? "Pass" : "Fail");
				}
				
				myPalletManage.setNoOfMeterPresent(meterPassedCount + meterFailedCount);
				myPalletManage.setNoOfMeterPassed(meterPassedCount);
				myPalletManage.setNoOfMeterFailed(meterFailedCount);
				
				MySqlServiceManager.getPalletManageService().saveToDb(myPalletManage);
				
				meterPassedCount = 0;
				meterFailedCount = 0;
				
				Calib.logger.debug("phasephaseCalibrationTask: allPass : " + allPass);

				responseReturn.put("status", allPass); 

				status = allPass;
				//ProjectExitProcess();
				//return status;
				//}
			}else{
				Calib.logger.debug("phasephaseCalibrationTask: Timed out");
			}

		}else{
			PalletTrackerController palletTracker = new PalletTrackerController();
			String selectedBayTypeKey = getMyBayKey();
			String myPalletDistinctId = PalletTrackerController.getPresentPalletAtBayMap().get(selectedBayTypeKey);
			
			PalletManage myPalletManage = MySqlServiceManager.getPalletManageService().findFirstByPalletDistinctId(myPalletDistinctId);

			if(ProconFeatureEnable.DEV_SYS_EM1_CONNECTED) {
				DevSysEnergyMeter devSysEnergyMeter = new DevSysEnergyMeter(Calib.logger);
				status1  = devSysEnergyMeter.phaseCalibrationProcess(1); // meter 1
				status2  = devSysEnergyMeter.phaseCalibrationProcess(2);
				status3  = devSysEnergyMeter.phaseCalibrationProcess(3);
				status4  = devSysEnergyMeter.phaseCalibrationProcess(4);
				status5  = devSysEnergyMeter.phaseCalibrationProcess(5);
				status6  = devSysEnergyMeter.phaseCalibrationProcess(6);
				Calib.logger.debug("S058_05_Phase_Calibration : phaseCalibrationTask : Meter 1 : status : " + status1);
				Calib.logger.debug("S058_05_Phase_Calibration : phaseCalibrationTask : Meter 2 : status : " + status2);
				Calib.logger.debug("S058_05_Phase_Calibration : phaseCalibrationTask : Meter 3 : status : " + status3);
				Calib.logger.debug("S058_05_Phase_Calibration : phaseCalibrationTask : Meter 4 : status : " + status4);
				Calib.logger.debug("S058_05_Phase_Calibration : phaseCalibrationTask : Meter 5 : status : " + status5);
				Calib.logger.debug("S058_05_Phase_Calibration : phaseCalibrationTask : Meter 6 : status : " + status6);
			}

			for(int positionNo =1; positionNo<= maxDeviceConnected; positionNo++){
				Calib.logger.debug("phaseCalibrationTask: result position Id : " + positionNo + " : "  + dutManager.getDutResultSummary(positionNo));
			}

			for (int positionNo =1; positionNo<= maxDeviceConnected; positionNo++) {
				String resultSummary = dutManager.getDutResultSummary(positionNo);
				Calib.logger.debug("phaseCalibrationTask: Result Summary : " + resultSummary);

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
				testCaseName = ConstantConveyor.PHASE_CALIB_RESULT_TEST_NAME; //"Calibration";
				testType = ConstantConveyor.CALIBRATION_RESULT_KEY; //"CALIB";
				palletTracker.addMeterResultSummary(positionNo, resultStatus, resultValue, getMyBayKey(), testType, testCaseName);
			}

			for (int positionNo = 1; positionNo <= maxDeviceConnected; positionNo++) {
			    String resultSummary = dutManager.getDutResultSummary(positionNo);
			    calibrationSummaryProcessor.addPhaseCalibrationResult(positionNo, resultSummary.equals("P") ? "Pass" : "Fail");
			}
			
			myPalletManage.setNoOfMeterPresent(meterPassedCount + meterFailedCount);
			myPalletManage.setNoOfMeterPassed(meterPassedCount);
			myPalletManage.setNoOfMeterFailed(meterFailedCount);
			
			MySqlServiceManager.getPalletManageService().saveToDb(myPalletManage);
			
			meterPassedCount = 0;
			meterFailedCount = 0;
			
			Calib.logger.debug("phaseCalibrationTask: allPass : " + allPass);

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

		Calib.logger.debug("S058_05_Phase_Calibration : phaseCalibrationProcess : status : " + status);

		Calib.logger.debug("S058_05_Phase_Calibration : phaseCalibrationProcess : Exit");
		return responseReturn;
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
