package com.tasnetwork.calibration.conveyor.bay.sta_nld1;


import com.tasnetwork.calibration.conveyor.ClusterServer;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;

import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.remote.ProcalRemoteSender;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S042_Start_Execution_Bay1 implements STA_NoLoadTestBay1State  {

	BayUtils bayUtils = new BayUtils();

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		StaNld_Bay1.logger.info("S042_Start_Execution : Entry");
		
		BayUtils.delay(1000);
		
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601 );
		
		ProcalRemoteSender procalRemoteSender = new ProcalRemoteSender();
		
		String ip_address = ConstantConveyorConfig.CONVEYOR_PROCAL_STA1_IP_ADDRESS;
		String ip_port = ConstantConveyorConfig.CONVEYOR_PROCAL_STA1_IP_PORT;
		String endpoint = ConstantConveyorConfig.CONVEYOR_PROCAL_STA1_END_PATH;
		
		ClusterServer stdNldt1ClusterServer = new ClusterServer(ip_address, ip_port);
		
		String procalServerResponse = procalRemoteSender.sendStartCommandToProcal(stdNldt1ClusterServer, endpoint);
		
		
		
		if (procalServerResponse.equals("startTestExecuteInitiated")) {
			BayUtils.delay(1000);
			StaNld_Bay1.logger.info("S042_Start_Execution : Start Execution Success");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
			
			BayUtils.delay(10000);
			
			int sta1TotalTpCount = DeviceDataManagerController.getConveyorConfigParsedKey().getTotalTestPointInSta1();
			int completedTpCount = 0;
			ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateTpCountStatus(getMyBayKey(), completedTpCount, sta1TotalTpCount);
			int staExecutionTime_InSec = DeviceDataManagerController.getConveyorConfigParsedKey().getSta1ExecutionTime_InSec();
			ConveyorDataManager.getDashboardObject().getBayIndicatorManager().startProgressBarWithTime(getMyBayKey(),staExecutionTime_InSec);
			
		} else {
			StaNld_Bay1.logger.info("S042_Start_Execution : Start Execution Failed");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_025);
		}
		
		

		StaNld_Bay1.logger.info("S042_Start_Execution : Exit");
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
	
	
}
