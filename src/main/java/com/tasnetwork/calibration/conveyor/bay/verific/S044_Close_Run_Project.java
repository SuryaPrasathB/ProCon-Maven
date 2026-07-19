package com.tasnetwork.calibration.conveyor.bay.verific;

import com.tasnetwork.calibration.conveyor.ClusterServer;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.remote.ProcalRemoteSender;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S044_Close_Run_Project implements VerificTestBayState  {
	static boolean verificationTestCompleted = false;
	
	BayUtils bayUtils = new BayUtils();
	
	String paramKey = "";
	String paramValue = "";
	

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();
	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Verification.logger.info("S044_Close_Run_Project : Entry");
		
		BayUtils.delay(2000);
		
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601 );
		
		ProcalRemoteSender procalRemoteSender = new ProcalRemoteSender();
		
		paramKey = ConstantConveyorConfig.PARAM_PROJECT_NAME; //"projectName";
		paramValue = ConstantConveyorConfig.CONVEYOR_PROCAL_VERIFIC_PROJECT_NAME;//"DevSysVerific01";
		
		String ip_address = ConstantConveyorConfig.CONVEYOR_PROCAL_VERIFIC_IP_ADDRESS;
		String ip_port = ConstantConveyorConfig.CONVEYOR_PROCAL_VERIFIC_IP_PORT;
		String endpoint = ConstantConveyorConfig.CONVEYOR_PROCAL_VERIFIC_END_PATH;
		
		ClusterServer verificClusterServer = new ClusterServer(ip_address, ip_port);
		
		String procalServerResponse = procalRemoteSender.sendCloseRunCommandToProcal(verificClusterServer, endpoint, paramKey, paramValue);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopProgressBarWithTpCount(getMyBayKey());
		if (procalServerResponse.equals("RunProjectClosed")) {
			BayUtils.delay(1000);
			Verification.logger.info("S044_Close_Run_Project : Close Run Project Success");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			Verification.logger.info("S044_Close_Run_Project : Close Run Project Failed");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_027);
		}
		
		setVerificationTestCompleted(true);
		
		Verification.logger.info("S044_Close_Run_Project : Exit");
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

	public static boolean isVerificationTestCompleted() {
		return verificationTestCompleted;
	}

	public static void setVerificationTestCompleted(boolean verificationTestCompleted) {
		S044_Close_Run_Project.verificationTestCompleted = verificationTestCompleted;
	}
	
	
}
