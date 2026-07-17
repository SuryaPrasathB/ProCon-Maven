package com.tasnetwork.calibration.conveyor.bay.calib;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.ClusterServer;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.calib.Calib;
//import com.tasnetwork.calibration.conveyor.bay_verificationtest.VerificationTestBay;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.remote.ProcalRemoteResponse;
import com.tasnetwork.calibration.conveyor.remote.ProcalRemoteSender;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S058_01_Step_Run_Execution implements CalibrationBayState  {

	BayUtils bayUtils = new BayUtils();

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Calib.logger.info("S058_01_Step_Run_Execution : Entry");

		BayUtils.delay(8000);
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601 );

		ProcalRemoteSender procalRemoteSender = new ProcalRemoteSender();

		String ip_address = ConstantConveyorConfig.CONVEYOR_PROCAL_CALIB_IP_ADDRESS;
		String ip_port = ConstantConveyorConfig.CONVEYOR_PROCAL_CALIB_IP_PORT;
		String endpoint = ConstantConveyorConfig.CONVEYOR_PROCAL_CALIB_END_PATH;

		ClusterServer calibClusterServer = new ClusterServer(ip_address, ip_port);

		String procalServerResponse = procalRemoteSender.sendStepRunCommandToProcal(calibClusterServer, endpoint);

		/*
		 * TODO
		 * Select 1st index
		 * Steprun
		 */

		if (procalServerResponse.equals("stepRunTestExecuteInitiated")) {
			BayUtils.delay(1000);
			Calib.logger.info("S058_01_Step_Run_Execution : Step Run Execution Success");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

			BayUtils.delay(10000);
		} else {
			Calib.logger.info("S058_01_Step_Run_Execution : Step Run Execution Failed");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_025);
		}

		ProcalRemoteResponse procalServerParamResponse = new ProcalRemoteResponse();
		
		long startTime = System.currentTimeMillis();
		long timeoutMillis = 2 * 60 * 1000; // 2 minutes in milliseconds

		while ((!procalServerParamResponse.isDutCalibrationVoltageTargetSet()) && 
				(!Calib.isStopProcessRequestedCalibBay()) &&
				(!ConstantConveyor.ALL_LOOP_BREAK_FLAG ) &&
				(System.currentTimeMillis() - startTime < timeoutMillis)){//isVoltageStartAcknowledged()) {
			Calib.logger.info("S058_01_Step_Run_Execution : Voltage  Start : awaiting..");	
			Calib.logger.info("S058_01_Step_Run_Execution : Getting Voltage Param status...");
			BayUtils.delay(2000); // 2 sec once
			
			long creepTimeSec = (System.currentTimeMillis() - startTime) / 1000;
		    Calib.logger.info("S058_01_Step_Run_Execution : Elapsed creep time in loop = " + creepTimeSec + " seconds");

			procalServerParamResponse = procalRemoteSender.sendParamStatusCommandToProcal(calibClusterServer, endpoint);				
		}

		if (procalServerParamResponse.isDutCalibrationVoltageTargetSet()){//isVoltageStartAcknowledged()) {
			Calib.logger.info("S058_01_Step_Run_Execution : Voltage  Start : Success");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			Calib.logger.info("S058_01_Step_Run_Execution : Voltage Start : Failed");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_019);
		}


		Calib.logger.info("S058_01_Step_Run_Execution : Exit");
		return bayResponse;
	}

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


}
