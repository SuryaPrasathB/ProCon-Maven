package com.tasnetwork.calibration.conveyor.bay.verific;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.ClusterServer;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.remote.ProcalRemoteSender;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S042_Start_Execution implements VerificTestBayState  {

	BayUtils bayUtils = new BayUtils();

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();
	public String getMyBayKey() {
		return myBayKey;
	}
	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Verification.logger.info("S042_Start_Execution : Entry");
		
		BayUtils.delay(5000);
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601 );
		
		ProcalRemoteSender procalRemoteSender = new ProcalRemoteSender();
		
		String ip_address = ConstantConveyorConfig.CONVEYOR_PROCAL_VERIFIC_IP_ADDRESS;
		String ip_port = ConstantConveyorConfig.CONVEYOR_PROCAL_VERIFIC_IP_PORT;
		String endpoint = ConstantConveyorConfig.CONVEYOR_PROCAL_VERIFIC_END_PATH;
		
		ClusterServer verificClusterServer = new ClusterServer(ip_address, ip_port);
		
		String procalServerResponse = procalRemoteSender.sendStartCommandToProcal(verificClusterServer, endpoint);

		if (procalServerResponse.equals("startTestExecuteInitiated")) {
			BayUtils.delay(1000);
			Verification.logger.info("S042_Start_Execution : Start Execution Success");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
			
			BayUtils.delay(10000);
			int loeTotalTpCount = DeviceDataManagerController.getConveyorConfigParsedKey().getTotalTestPointInVerific1();
			int completedTpCount = 0;
			ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateTpCountStatus(getMyBayKey(), completedTpCount, loeTotalTpCount);
			ConveyorDataManager.getDashboardObject().getBayIndicatorManager().startProgressBarWithTpCount(getMyBayKey());
			
		} else {
			Verification.logger.info("S042_Start_Execution : Start Execution Failed");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_025);
		}
		
		

		Verification.logger.info("S042_Start_Execution : Exit");
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
