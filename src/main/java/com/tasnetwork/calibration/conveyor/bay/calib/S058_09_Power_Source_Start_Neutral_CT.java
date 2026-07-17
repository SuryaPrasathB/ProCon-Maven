package com.tasnetwork.calibration.conveyor.bay.calib;




import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Source;

import org.omg.CORBA.FloatSeqHelper;
import org.springframework.cglib.transform.impl.AddDelegateTransformer;

import com.tasnetwork.calibration.conveyor.ClusterServer;
import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.remote.ProcalRemoteResponse;
import com.tasnetwork.calibration.conveyor.remote.ProcalRemoteSender;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S058_09_Power_Source_Start_Neutral_CT implements CalibrationBayState {

	static volatile boolean powerStartRequest = false;  
	static volatile boolean powerStartAcknowledged = false; 

	BayUtils bayUtils = new BayUtils();

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Calib.logger.info("S058_09_Power_Source_Start_Neutral_CT : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601 );

		ProcalRemoteSender procalRemoteSender = new ProcalRemoteSender();

		String ip_address = ConstantConveyorConfig.CONVEYOR_PROCAL_CALIB_IP_ADDRESS;
		String ip_port = ConstantConveyorConfig.CONVEYOR_PROCAL_CALIB_IP_PORT;
		String endpoint = ConstantConveyorConfig.CONVEYOR_PROCAL_CALIB_END_PATH;

		ClusterServer calibClusterServer = new ClusterServer(ip_address, ip_port);

		String procalServerResponse = procalRemoteSender.sendSwitchToNextCommandToProcal(calibClusterServer, endpoint);

		if (procalServerResponse.equals("switchToNextExecuteInitiated")) {
			BayUtils.delay(1000);
			Calib.logger.info("S058_09_Power_Source_Start_Neutral_CT : Switch To Next Execution Success");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

			BayUtils.delay(10000);
		} else {
			Calib.logger.info("S058_09_Power_Source_Start_Neutral_CT : Switch To Next Execution Failed");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_025);
		}

		ProcalRemoteResponse procalServerParamResponse = new ProcalRemoteResponse();

		while (!procalServerParamResponse.isDutCalibrationCurrentTargetSet() && 
				!Calib.isStopProcessRequestedCalibBay() &&
				(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {
			Calib.logger.info("S058_09_Power_Source_Start_Neutral_CT : Current Start : awaiting..");
			Calib.logger.info("S058_09_Power_Source_Start_Neutral_CT : CALIB Power Source With Neutral CT Start - Awaiting");		
			BayUtils.delay(2000); // 2 sec once

			procalServerParamResponse = procalRemoteSender.sendParamStatusCommandToProcal(calibClusterServer, endpoint);
		}

		if (procalServerParamResponse.isDutCalibrationCurrentTargetSet()) {
			Calib.logger.info("S058_09_Power_Source_Start_Neutral_CT : Power Source in Neutral CT Start : Success");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			Calib.logger.info("S058_09_Power_Source_Start_Neutral_CT : Power Source in Neutral CT Start : Failed");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_023);
		}

		Calib.logger.info("S058_09_Power_Source_Start_Neutral_CT : Exit");
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

	public static boolean isPowerStartRequest() {
		return powerStartRequest;
	}

	public static boolean isPowerStartAcknowledged() {
		return powerStartAcknowledged;
	}

	public static void setPowerStartRequest(boolean powerStartRequest) {
		S058_09_Power_Source_Start_Neutral_CT.powerStartRequest = powerStartRequest;
	}

	public static void setPowerStartAcknowledged(boolean powerStartAcknowledged) {
		S058_09_Power_Source_Start_Neutral_CT.powerStartAcknowledged = powerStartAcknowledged;
	}




}

