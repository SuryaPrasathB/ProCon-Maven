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
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S058_06_Current_Stop implements CalibrationBayState {

	static volatile boolean voltageStartRequest = false;  
	static volatile boolean voltageStartAcknowledged = false; 

	BayUtils bayUtils = new BayUtils();

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Calib.logger.info("S058_06_Current_Stop : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601 );

		ProcalRemoteSender procalRemoteSender = new ProcalRemoteSender();
		
		String ip_address = ConstantConveyorConfig.CONVEYOR_PROCAL_CALIB_IP_ADDRESS;
		String ip_port = ConstantConveyorConfig.CONVEYOR_PROCAL_CALIB_IP_PORT;
		String endpoint = ConstantConveyorConfig.CONVEYOR_PROCAL_CALIB_END_PATH;
		
		ClusterServer calibClusterServer = new ClusterServer(ip_address, ip_port);
		
		String procalServerResponse = procalRemoteSender.sendSwitchToNextCommandToProcal(calibClusterServer, endpoint);		
		/*
		 *  SWITCH TO NEXT TP
		 */
		if (procalServerResponse.equals("switchToNextExecuteInitiated")) {
			BayUtils.delay(1000);
			Calib.logger.info("S058_06_Current_Stop : Switch To Next Execution Success");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
			
			BayUtils.delay(10000);
		} else {
			Calib.logger.info("S058_06_Current_Stop : Switch To Next Execution Failed");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_025);
		}
		
		ProcalRemoteResponse procalServerParamResponse = new ProcalRemoteResponse();
		
		while (!procalServerParamResponse.isDutCalibrationCurrentZeroSet() && 
				!Calib.isStopProcessRequestedCalibBay() &&
				(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {//isVoltageStartAcknowledged()) {
			Calib.logger.info("S058_06_Current_Stop : Current Stop : awaiting..");
			Calib.logger.info("S058_06_Current_Stop : Getting Voltage Param status...");		
			BayUtils.delay(2000); // 2 sec once
			
			procalServerParamResponse = procalRemoteSender.sendParamStatusCommandToProcal(calibClusterServer, endpoint);
		}
		
		if (procalServerParamResponse.isDutCalibrationCurrentZeroSet()){//isVoltageStartAcknowledged()) {
			Calib.logger.info("S058_06_Current_Stop : Current Stop : Success");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			Calib.logger.info("S058_06_Current_Stop : Current Stop : Failed");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_019);
		}

		Calib.logger.info("S058_06_Current_Stop : Exit");
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

	public static boolean isVoltageStartRequest() {
		return voltageStartRequest;
	}

	public static boolean isVoltageStartAcknowledged() {
		return voltageStartAcknowledged;
	}

	public static void setVoltageStartRequest(boolean voltageStartRequest) {
		S058_06_Current_Stop.voltageStartRequest = voltageStartRequest;
	}

	public static void setVoltageStartAcknowledged(boolean voltageStartAcknowledged) {
		S058_06_Current_Stop.voltageStartAcknowledged = voltageStartAcknowledged;
	}

}

