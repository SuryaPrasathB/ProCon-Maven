package com.tasnetwork.calibration.conveyor.bay.calib;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.ClusterServer;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.bay.calib.Calib;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.pallet.CalibrationSummaryProcessor;
import com.tasnetwork.calibration.conveyor.pallet.PalletTrackerController;
import com.tasnetwork.calibration.conveyor.remote.ProcalRemoteResponse;
import com.tasnetwork.calibration.conveyor.remote.ProcalRemoteSender;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.spring.orm.model.PalletManage;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

import javafx.application.Platform;

public class S058_11_Stop_Execution implements CalibrationBayState  {

    private final CalibrationSummaryProcessor calibrationSummaryProcessor = CalibrationSummaryProcessor.getInstance();
	
	static boolean stopExecutionRequested = false;

	BayUtils bayUtils = new BayUtils();

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();
	
	String resultStatus = "";
	String resultValue = "";
	String testType = "";
	String testCaseName = "";
	
	private String myBayKey = ConstantConveyor.CALIBRATION_BAY_KEY;

	//===========================================================================================
	@Override
	public BayResponse handleRequest() {

		Calib.logger.info("S058_11_Stop_Execution : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601 );

		ProcalRemoteSender procalRemoteSender = new ProcalRemoteSender();

		String ip_address = ConstantConveyorConfig.CONVEYOR_PROCAL_CALIB_IP_ADDRESS;
		String ip_port = ConstantConveyorConfig.CONVEYOR_PROCAL_CALIB_IP_PORT;
		String endpoint = ConstantConveyorConfig.CONVEYOR_PROCAL_CALIB_END_PATH;

		ClusterServer calibClusterServer = new ClusterServer(ip_address, ip_port);

		String procalServerResponse = procalRemoteSender.sendStopCommandToProcal(calibClusterServer, endpoint);

		if (procalServerResponse.equals("testStopInitiated")) {
			BayUtils.delay(1000);
			Calib.logger.info("S058_11_Stop_Execution : Stop Execution Success");	
			bayResponse.setStatus(true);
			calibrationSummaryProcessor.processOverallCalibration();
			//updateCalibrationResult();
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			Calib.logger.info("S058_11_Stop_Execution : Stop Execution Failed");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_027);
		}
		
		updateCalibrationResult();

		ProcalRemoteResponse procalServerParamResponse = new ProcalRemoteResponse();

		while (!procalServerParamResponse.isDutCalibrationCurrentTargetSet() && 
				(!Calib.isStopProcessRequestedCalibBay()) &&
				(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)){//isVoltageStartAcknowledged()) {
			
			/*
			 *  Voltage Current Stopped - ACK
			 */
			Calib.logger.info("S058_11_Stop_Execution : Voltage Current Stop : awaiting..");
			Calib.logger.info("S058_04_Power_Source_Start_Main_CT : CALIB Power Source With Main CT Start - Awaiting");		
			BayUtils.delay(2000); // 2 sec once
			
			procalServerParamResponse = procalRemoteSender.sendParamStatusCommandToProcal(calibClusterServer, endpoint);
		}

		// WITH IR BAY LAMP TOGGLE
		if (ProconFeatureEnable.WAIT_FOR_USER_INPUT) {

			boolean toggle = false; // This flag will be used to alternate
			long startTime = System.currentTimeMillis();

			StateExecutorController.getRef_btn_CalibRemove().setDisable(false);

			Calib.logger.debug("S081_stop_FT_source : CALIB_OPTICAL_REMOVED :" + ConstantConveyor.isCALIB_OPTICAL_REMOVED());

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

				Calib.logger.debug("S081_stop_FT_source : Waiting to remove Optical Readers ");
			}
			BayUtils.delay(2000);
			ConstantConveyor.setCALIB_OPTICAL_REMOVED(false);
			StateExecutorController.getRef_btn_CalibRemove().setDisable(true);

			Platform.runLater(() -> {
				StateExecutorController.ref_tf_CALIB_prompt.clear();
			});
		}

		if (procalServerParamResponse.isDutCalibrationCurrentTargetSet()){//isVoltageStartAcknowledged()) {
			Calib.logger.info("S058_11_Stop_Execution : Voltage Current Stop : Success");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			Calib.logger.info("S058_11_Stop_Execution : Voltage Current Stop : Failed");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_019);
		}

		Calib.logger.info("S058_11_Stop_Execution : Exit");
		return bayResponse;
	}

	private void updateCalibrationResult() {
		PalletTrackerController palletTracker = new PalletTrackerController();
		String selectedBayTypeKey = getMyBayKey();
		String myPalletDistinctId = PalletTrackerController.getPresentPalletAtBayMap().get(selectedBayTypeKey);
		
		PalletManage myPalletManage = MySqlServiceManager.getPalletManageService().findFirstByPalletDistinctId(myPalletDistinctId);
		
		for (int positionNo =1; positionNo<= ConstantConveyor.MAX_DEVICES_CONNECTED; positionNo++) {
			
			resultStatus = calibrationSummaryProcessor.getOverallCalibrationResult(positionNo);
			resultValue = resultStatus;
			
			testCaseName = ConstantConveyor.SUMMARY_CALIB_RESULT_TEST_NAME;//CALIB_RESULT_TEST_NAME; //"Calibration";
			testType = ConstantConveyor.CALIBRATION_RESULT_KEY; //"CALIB";
						
			palletTracker.addMeterResultSummary(positionNo, resultStatus, resultValue, getMyBayKey(), testType, testCaseName);
		}
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

	public String getSequencePathId() {
		return sequencePathId;
	}

	public void setSequencePathId(String sequencePathId) {
		this.sequencePathId = sequencePathId;
	}

	public TestInterfaceStatus getPalletAvailableTest_I_F_Status() {
		return palletAvailableTest_I_F_Status;
	}

	public void setPalletAvailableTest_I_F_Status(TestInterfaceStatus palletAvailableTest_I_F_Status) {
		this.palletAvailableTest_I_F_Status = palletAvailableTest_I_F_Status;
	}
	
	public static boolean isStopExecutionRequested() {
		return stopExecutionRequested;
	}

	public static void setStopExecutionRequested(boolean stopExecutionRequested) {
		S058_11_Stop_Execution.stopExecutionRequested = stopExecutionRequested;
	}

	public String getMyBayKey() {
		return myBayKey;
	}

	public void setMyBayKey(String myBayKey) {
		this.myBayKey = myBayKey;
	}
}
