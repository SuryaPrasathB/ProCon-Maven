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

public class S052_Power_Source_Start_Main_CT implements CalibrationBayState {

	static volatile boolean powerStartRequest = false;  
	static volatile boolean powerStartAcknowledged = false; 

	BayUtils bayUtils = new BayUtils();

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Calib.logger.info("S052_Power_Source_Start_Main_CT : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601 );

		//Map<String,Object> responseReturn =  StartCalibSource();	 
		//boolean StartCalibSource = (boolean)responseReturn.get("status");
		
	//	setPowerStartRequest(true) ;
		ProjectExecutionController projectExecutionController = new ProjectExecutionController();
		projectExecutionController.initiateRatedCurrentSetOnPowerSource(ConstantReport.RESULT_EXECUTION_MODE_MAIN_CT);
		
		/*
		 *  SWITCH TO NEXT TP
		 *  CMD
		 */
		
		
		while (!projectExecutionController.isDutCalibrationCurrentTargetSet() && !Calib.isStopProcessRequestedCalibBay()) {
			Calib.logger.info("S052_Power_Source_Start_Main_CT : CALIB Power Source With Main CT Start - Awaiting");		
			
			/*
			 *  Voltage Current Set - Ack
			 */
			
			BayUtils.delay(1000);
		}
		
		if (projectExecutionController.isDutCalibrationCurrentTargetSet() && !Calib.isStopProcessRequestedCalibBay()) {
			Calib.logger.info("S052_Power_Source_Start_Main_CT : Power Source With Neutral Main CT Start : Success");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			Calib.logger.info("S052_Power_Source_Start_Main_CT : Power Source With Neutral Main CT Start : Failed");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_023);
		}

		Calib.logger.info("S052_Power_Source_Start_Main_CT : Exit");
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
		S052_Power_Source_Start_Main_CT.powerStartRequest = powerStartRequest;
	}

	public static void setPowerStartAcknowledged(boolean powerStartAcknowledged) {
		S052_Power_Source_Start_Main_CT.powerStartAcknowledged = powerStartAcknowledged;
	}
 

	 

}

