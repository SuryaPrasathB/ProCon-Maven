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

public class S054_Current_Stop implements CalibrationBayState {

	static volatile boolean currentStopRequest = false;  
	static volatile boolean currentStopAcknowledged = false; 

	BayUtils bayUtils = new BayUtils();

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Calib.logger.info("S054_Current_Stop : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601 );

		//Map<String,Object> responseReturn =  StopCalibSource();	 
		//boolean StopCalibSource = (boolean)responseReturn.get("status");
		
		//setCurrentStopRequest(true) ;
		ProjectExecutionController projectExecutionController = new ProjectExecutionController();
		projectExecutionController.initiateZeroCurrentSetOnPowerSource();
		
		/*
		 *  SWITCH TO NEXT TP
		 */
		
		while (!projectExecutionController.isDutCalibrationCurrentZeroSet() && !Calib.isStopProcessRequestedCalibBay()) {
			Calib.logger.info("S054_Current_Stop : CALIB Current Source Stop - Awaiting");		
			
			/*
			 * Current Stop - Ack
			 */
			BayUtils.delay(1000);
		}
		
		if (projectExecutionController.isDutCalibrationCurrentZeroSet()) {
			Calib.logger.info("S054_Current_Stop : Current Source Stop : Success");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			Calib.logger.info("S054_Current_Stop : Current Source Stop : Failed");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_025);
		}

		Calib.logger.info("S054_Current_Stop : Exit");
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
	public static boolean isCurrentStopRequest() {
		return currentStopRequest;
	}
	public static boolean isCurrentStopAcknowledged() {
		return currentStopAcknowledged;
	}
	public static void setCurrentStopRequest(boolean currentStopRequest) {
		S054_Current_Stop.currentStopRequest = currentStopRequest;
	}
	public static void setCurrentStopAcknowledged(boolean currentStopAcknowledged) {
		S054_Current_Stop.currentStopAcknowledged = currentStopAcknowledged;
	}

	 

}

