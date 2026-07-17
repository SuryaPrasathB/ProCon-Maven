package com.tasnetwork.calibration.conveyor.bay.calib;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.ClusterServer;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.calib.Calib;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.remote.ProcalRemoteSender;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S050_Stop_Execution implements CalibrationBayState  {

	static boolean stopExecutionRequested = false;

	BayUtils bayUtils = new BayUtils();

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Calib.logger.info("S050_Stop_Execution : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601 );

		ProcalRemoteSender procalRemoteSender = new ProcalRemoteSender();

		String ip_address = ConstantConveyorConfig.CONVEYOR_PROCAL_CALIB_IP_ADDRESS;
		String ip_port = ConstantConveyorConfig.CONVEYOR_PROCAL_CALIB_IP_PORT;
		String endpoint = ConstantConveyorConfig.CONVEYOR_PROCAL_CALIB_END_PATH;

		ClusterServer verificClusterServer = new ClusterServer(ip_address, ip_port);

		String procalServerResponse = procalRemoteSender.sendStopCommandToProcal(verificClusterServer, endpoint);

		if (procalServerResponse.equals("testStopInitiated")) {
			BayUtils.delay(1000);
			Calib.logger.info("S050_Stop_Execution : Stop Execution Success");	
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			Calib.logger.info("S050_Stop_Execution : Stop Execution Failed");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_027);
		}

		ProjectExecutionController projectExecutionController = new ProjectExecutionController();

		while (!projectExecutionController.isDutCalibrationVoltageTargetSet() && !Calib.isStopProcessRequestedCalibBay()){//isVoltageStartAcknowledged()) {
			
			/*
			 *  Voltage Current Stopped - ACK
			 */
			
			Calib.logger.info("S041_Voltage_Start : Voltage  Start : awaiting..");		
			BayUtils.delay(1000);
		}
		
		if (projectExecutionController.isDutCalibrationVoltageTargetSet() && !Calib.isStopProcessRequestedCalibBay()){//isVoltageStartAcknowledged()) {
			Calib.logger.info("S041_Voltage_Start : Voltage  Start : Success");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			Calib.logger.info("S041_Voltage_Start : Voltage Start : Failed");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_019);
		}

		Calib.logger.info("S050_Stop_Execution : Exit");
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
	
	public static boolean isStopExecutionRequested() {
		return stopExecutionRequested;
	}

	public static void setStopExecutionRequested(boolean stopExecutionRequested) {
		S050_Stop_Execution.stopExecutionRequested = stopExecutionRequested;
	}
}
