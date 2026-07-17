package com.tasnetwork.calibration.conveyor.bay.calib;

 

 
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Source;

import org.omg.CORBA.FloatSeqHelper;
import org.springframework.cglib.transform.impl.AddDelegateTransformer;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S051_Power_Source_Stop implements CalibrationBayState {

	static volatile boolean powerStopRequest = false;  
	static volatile boolean powerStopAcknowledged = false; 

	BayUtils bayUtils = new BayUtils();

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Calib.logger.info("S051_Power_Source_Stop : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601 );

		//Map<String,Object> responseReturn =  StopCalibSource();	 
		//boolean StopCalibSource = (boolean)responseReturn.get("status");
		
		//setPowerStopRequest(true) ;
		ProjectExecutionController projectExecutionController = new ProjectExecutionController();
		projectExecutionController.initiateStopRequestOnPowerSource();
		
		while (!projectExecutionController.isDutCalibrationVoltCurrentSetZero() && !Calib.isStopProcessRequestedCalibBay()) {
			Calib.logger.info("S051_Power_Source_Stop : CALIB Power Source Stop - Awaiting");		
			BayUtils.delay(1000);
		}
		
		if (projectExecutionController.isDutCalibrationVoltCurrentSetZero()) {
			Calib.logger.info("S051_Power_Source_Stop : Power Source Stop : Success");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			Calib.logger.info("S051_Power_Source_Stop : Power Source Stop : Failed");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_020);
		}

		Calib.logger.info("S051_Power_Stop_Source : Exit");
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
	public static boolean isPowerStopRequest() {
		return powerStopRequest;
	}
	public static boolean isPowerStopAcknowledged() {
		return powerStopAcknowledged;
	}
	public static void setPowerStopRequest(boolean powerStopRequest) {
		S051_Power_Source_Stop.powerStopRequest = powerStopRequest;
	}
	public static void setPowerStopAcknowledged(boolean powerStopAcknowledged) {
		S051_Power_Source_Stop.powerStopAcknowledged = powerStopAcknowledged;
	}

	 

}

