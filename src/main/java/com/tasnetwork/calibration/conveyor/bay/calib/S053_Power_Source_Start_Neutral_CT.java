package com.tasnetwork.calibration.conveyor.bay.calib;

 

 
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S053_Power_Source_Start_Neutral_CT implements CalibrationBayState {

	static volatile boolean powerStartRequest = false;  
	static volatile boolean powerStartAcknowledged = false; 

	BayUtils bayUtils = new BayUtils();

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Calib.logger.info("S052_Power_Source_Start_Neutral_CT : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601 );

		//Map<String,Object> responseReturn =  StartCalibSource();	 
		//boolean StartCalibSource = (boolean)responseReturn.get("status");
		
		//setPowerStartRequest(true) ;
		ProjectExecutionController projectExecutionController = new ProjectExecutionController();
		projectExecutionController.initiateRatedCurrentSetOnPowerSource(ConstantReport.RESULT_EXECUTION_MODE_NEUTRAL_CT);
		
		/*
		 *  SWITCH TO NEXT TP
		 */
		
		while (!projectExecutionController.isDutCalibrationCurrentTargetSet() && !Calib.isStopProcessRequestedCalibBay()) {
			Calib.logger.info("S052_Power_Source_Start_Neutral_CT : CALIB Power Source With Neutral CT  Start - Awaiting");
			
			/*
			 *  Voltage Current Set - Ack
			 */
			
			BayUtils.delay(1000);
		}
		
		if (projectExecutionController.isDutCalibrationCurrentTargetSet() && !Calib.isStopProcessRequestedCalibBay()) {
			Calib.logger.info("S052_Power_Source_Start_Neutral_CT : Power Source With Neutral CT Start : Success");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			Calib.logger.info("S052_Power_Source_Start_Neutral_CT : Power Source With Neutral CT Start : Failed");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_024);
		}

		Calib.logger.info("S052_Power_Source_Start_Neutral_CT : Exit");
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
		S053_Power_Source_Start_Neutral_CT.powerStartRequest = powerStartRequest;
	}

	public static void setPowerStartAcknowledged(boolean powerStartAcknowledged) {
		S053_Power_Source_Start_Neutral_CT.powerStartAcknowledged = powerStartAcknowledged;
	}
 

	 

}

