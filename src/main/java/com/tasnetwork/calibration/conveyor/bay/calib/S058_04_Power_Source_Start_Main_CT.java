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

public class S058_04_Power_Source_Start_Main_CT implements CalibrationBayState {

	static volatile boolean powerStartRequest = false;  
	static volatile boolean powerStartAcknowledged = false; 

	BayUtils bayUtils = new BayUtils();

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Calib.logger.info("S058_04_Power_Source_Start_Main_CT : Entry");
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
			Calib.logger.info("S058_04_Power_Source_Start_Main_CT : Switch To Next Execution Success");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
			
			BayUtils.delay(10000);
		} else {
			Calib.logger.info("S058_04_Power_Source_Start_Main_CT : Switch To Next Execution Failed");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_025);
		}
		
		ProcalRemoteResponse procalServerParamResponse = new ProcalRemoteResponse();
		
		long startTime = System.currentTimeMillis();
		long timeoutMillis = 2 * 60 * 1000; // 2 minutes in milliseconds

		while (!procalServerParamResponse.isDutCalibrationCurrentTargetSet() && 
		       !Calib.isStopProcessRequestedCalibBay() &&
		       !ConstantConveyor.ALL_LOOP_BREAK_FLAG &&
		       (System.currentTimeMillis() - startTime < timeoutMillis)) {

		    Calib.logger.info("S058_04_Power_Source_Start_Main_CT : Current Start : awaiting..");	
		    Calib.logger.info("S058_04_Power_Source_Start_Main_CT : Getting Current Param status...");		
		    BayUtils.delay(2000); // 2 sec once

		    long creepTimeSec = (System.currentTimeMillis() - startTime) / 1000;
		    Calib.logger.info("S058_04_Power_Source_Start_Main_CT : Elapsed creep time in loop = " + creepTimeSec + " seconds");

		    procalServerParamResponse = procalRemoteSender.sendParamStatusCommandToProcal(calibClusterServer, endpoint);
		}

		if (procalServerParamResponse.isDutCalibrationCurrentTargetSet()) {
		    Calib.logger.info("S058_04_Power_Source_Start_Main_CT : Power Source in Main CT Start : Success");
		    bayResponse.setStatus(true);
		    bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
		    Calib.logger.info("S058_04_Power_Source_Start_Main_CT : Power Source in Main CT Start : Failed (timeout or stop condition)");
		    bayResponse.setStatus(false);
		    bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_023);
		}

		Calib.logger.info("S058_04_Power_Source_Start_Main_CT : Exit");
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
		S058_04_Power_Source_Start_Main_CT.powerStartRequest = powerStartRequest;
	}

	public static void setPowerStartAcknowledged(boolean powerStartAcknowledged) {
		S058_04_Power_Source_Start_Main_CT.powerStartAcknowledged = powerStartAcknowledged;
	}
 

	 

}

