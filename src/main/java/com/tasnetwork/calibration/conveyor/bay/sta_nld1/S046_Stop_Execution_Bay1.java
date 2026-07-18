package com.tasnetwork.calibration.conveyor.bay.sta_nld1;

import com.tasnetwork.calibration.conveyor.ClusterServer;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.remote.ProcalRemoteSender;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S046_Stop_Execution_Bay1 implements STA_NoLoadTestBay1State  {
	
	static boolean stopExecutionRequested = false;
	
	BayUtils bayUtils = new BayUtils();

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		StaNld_Bay1.logger.info("S046_Stop_Execution : Entry");
		
		setStopExecutionRequested(true);
		
		BayUtils.delay(1000);
		
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601 );
		
		ProcalRemoteSender procalRemoteSender = new ProcalRemoteSender();
		
		String ip_address = ConstantConveyorConfig.CONVEYOR_PROCAL_STA1_IP_ADDRESS;
		String ip_port = ConstantConveyorConfig.CONVEYOR_PROCAL_STA1_IP_PORT;
		String endpoint = ConstantConveyorConfig.CONVEYOR_PROCAL_STA1_END_PATH;
		
		ClusterServer stdNldt1ClusterServer = new ClusterServer(ip_address, ip_port);
		
		String procalServerResponse = procalRemoteSender.sendStopCommandToProcal(stdNldt1ClusterServer, endpoint);
		
		if (procalServerResponse.equals("testStopInitiated")) {
			BayUtils.delay(1000);
			StaNld_Bay1.logger.info("S046_Stop_Execution : Stop Execution Success");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
			
			BayUtils.delay(10000);
		} else {
			StaNld_Bay1.logger.info("S046_Stop_Execution : Stop Execution Failed");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_025);
		}
		
		

		StaNld_Bay1.logger.info("S046_Stop_Execution : Exit");
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
		S046_Stop_Execution_Bay1.stopExecutionRequested = stopExecutionRequested;
	}
	
	
	
	
}
