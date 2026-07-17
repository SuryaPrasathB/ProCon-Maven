package com.tasnetwork.calibration.conveyor.bay.sta_nld1;

import java.util.HashMap;
import java.util.Map;

import javax.xml.ws.Endpoint;

import com.tasnetwork.calibration.conveyor.ClusterServer;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
//import com.tasnetwork.calibration.conveyor.bay_calibration.CalibrationBay;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.remote.ProcalRemoteResponse;
import com.tasnetwork.calibration.conveyor.remote.ProcalRemoteSender;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S044_Close_Run_Project_Bay1 implements STA_NoLoadTestBay1State  {
	static boolean STA_NLDT_TestCompleted = false;
	
	BayUtils bayUtils = new BayUtils();
	
	String paramKey = "";
	String paramValue = "";


	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		StaNld_Bay1.logger.info("S044_Close_Run_Project : Entry");
		
		BayUtils.delay(2000);
		
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601 );
		
		ProcalRemoteSender procalRemoteSender = new ProcalRemoteSender();
		
		paramKey = ConstantConveyorConfig.PARAM_PROJECT_NAME; //"projectName";
		paramValue = ConstantConveyorConfig.CONVEYOR_PROCAL_STA1_PROJECT_NAME; //"DevSysSta1";
		
		String ip_address = ConstantConveyorConfig.CONVEYOR_PROCAL_STA1_IP_ADDRESS;
		String ip_port = ConstantConveyorConfig.CONVEYOR_PROCAL_STA1_IP_PORT;
		String endpoint = ConstantConveyorConfig.CONVEYOR_PROCAL_STA1_END_PATH;
		
		ClusterServer stdNldt1ClusterServer = new ClusterServer(ip_address, ip_port);
		
		String procalServerResponse = procalRemoteSender.sendCloseRunCommandToProcal(stdNldt1ClusterServer, endpoint, paramKey, paramValue);
		
		if (procalServerResponse.equals("RunProjectClosed")) {
			BayUtils.delay(1000);
			StaNld_Bay1.logger.info("S044_Close_Run_Project : Close Run Project Success");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			StaNld_Bay1.logger.info("S044_Close_Run_Project : Close Run Project Failed");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_027);
		}
		
		setSTA_NLDT_TestCompleted(true);
		
		StaNld_Bay1.logger.info("S044_Close_Run_Project : Exit");
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
	
	public static boolean isSTA_NLDT_TestCompleted() {
		return STA_NLDT_TestCompleted;
	}

	public static void setSTA_NLDT_TestCompleted(boolean sTA_NLDT_TestCompleted) {
		STA_NLDT_TestCompleted = sTA_NLDT_TestCompleted;
	}
	
}
