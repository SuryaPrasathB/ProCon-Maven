package com.tasnetwork.calibration.conveyor.bay.ft;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S0821_ensure_FT_source_stopped implements FtBayState {

/*	String LOW   = "OPEN";
	String HIGH  = "CLOSE";
	String CLOSE  = "CLOSE";  
	String OPEN   = "OPEN";
	String ON  = "On";
	String OFF  = "Off";*/

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();


	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Ft.logger.info("S082_ensure_FT_source_stopped : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		int try_count = 0;

		setSequencePathId("p1");
		setPalletAvailableTest_I_F_Status (null);
		Map<String,Object> responseReturn =  new HashMap<String,Object>();//ftBay_FingerTipLatch_Status();
		String ftSourcePresentState = "";

		while (try_count <= 3) {
			responseReturn =  ftBay_SourceStatusPin_Status();
			ftSourcePresentState = (String)responseReturn.get("responseData");
			setPalletAvailableTest_I_F_Status((TestInterfaceStatus)responseReturn.get("testInterfaceStatus"));

			if (ftSourcePresentState.equals("SRC_OFF")) {  
				Ft.logger.info("S082_ensure_FT_source_stopped : ftBay_SourceStatusPin_Status : Source OFF" );

				bayResponse.setStatus(true);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
				break;
			} else {
				Ft.logger.info("S082_ensure_FT_source_stopped : Entry");

				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_031);
				BayUtils.delay(1000);
				try_count++;
			}
		}
		StateExecutorController.updateTestInterfaceStatusOnGui(responseReturn,ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);

		Ft.logger.info("S082_ensure_FT_source_stopped : Exit");
		return bayResponse;
	}
	//============================================================================================================================================  

	private Map<String, Object> ftBay_SourceStatusPin_Status() {

		Ft.logger.debug("S082_ensure_FT_source_stopped : ftBay_SourceStatusPin_Status : Entry");

		Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
		TestInterfaceStatus testIntefaceStatus = new TestInterfaceStatus();

		String state = "";
		String status = "";
		//============================================================================================		 
		IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_SRC_STATUS_PIN); 
		// OutputPortInfo portInfo = Utils.getOutputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_RED_LED);

		if (portInfo != null) {
			Ft.logger.debug("PortId    : " + portInfo.getPortId());
			Ft.logger.debug("ClusterId : " + portInfo.getClusterId());
			Ft.logger.debug("BayId     : " + portInfo.getBayId());
			
			if(getPalletAvailableTest_I_F_Status()==null){
				
				testIntefaceStatus = new TestInterfaceStatus(
						ConstantConveyor.FT_BAY_KEY,
						ConstantBayStateManage.FT_BAY_HP_SEQ_11,
						ConstantConveyor.DEVICE_TYPE_CLUSTER_OUTPUT,
						getSequencePathId(),
						"-",
						portInfo.getPortId(),
						ConstantBayPortNameMapping.FT_PORT_NAME_SRC_START,
						ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
						"Waiting",
						ConstantConveyor.COMM_EXECUTION_STATUS_INP
						);
				
				/*testIntefaceStatus.setBayName(ConstantConveyor.FT_BAY_KEY);
				testIntefaceStatus.setStateName(ConstantBayStateManage.FT_BAY_HP_SEQ_11);
				testIntefaceStatus.setPositionNo(getSequencePathId());
				testIntefaceStatus.setcName(ConstantBayPortNameMapping.FT_PORT_NAME_SRC_STATUS_PIN);
				testIntefaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_INP);
				testIntefaceStatus.setDeviceType(ConstantConveyor.DEVICE_TYPE_CLUSTER_INPUT);
				testIntefaceStatus.setSerialStatus(ConstantConveyor.COMM_STATUS_NOT_APPLICABLE);
				testIntefaceStatus.setPortName(portInfo.getPortId());*/
				
				int newRecordSerialNo = StateExecutorController.addToTestStatusGui(testIntefaceStatus);
				testIntefaceStatus.setSerialNo(String.valueOf(newRecordSerialNo));
			}else{
				testIntefaceStatus = getPalletAvailableTest_I_F_Status();
			}
		} else {
			Ft.logger.debug("S082_ensure_FT_source_stopped : ftBay_SourceStatusPin_Status : Input port not found");
			return responseReturn ;
        }

		BayUtils bayUtils = new BayUtils();

		/*state = bayUtils.getInputDataFromBay(portInfo.getClusterId(), 
				portInfo.getBayId(), 
				portInfo.getPortId()) ;*/
		
		state = bayUtils.getInputDataFromBayV2(portInfo) ;

		state = state.equals(Constant_IO_ActionMapping.OFF) ? "SRC_OFF" : "SRC_ON";

		Ft.logger.debug("S082_ensure_FT_source_stopped : ftBay_SourceStatusPin_Status : state : " + state);
		
		if(state.equals("SRC_OFF")){
			testIntefaceStatus.setDeviceResponseStatus("Success");
			responseReturn.put("status", true);
		}else{
			testIntefaceStatus.setDeviceResponseStatus("Failed");
			responseReturn.put("status", false);
		}
		if(portInfo.getPortId().equals(state)){
			state = "TimeOut";
			testIntefaceStatus.setDeviceResponseData("TimeOut");
		}else{
			testIntefaceStatus.setDeviceResponseData(state);
		}
		
		if(StateExecutorController.simulateFtBayHappyPath){
			state = "SRC_OFF";
		}


		StateExecutorController.updateTestStatusGui(testIntefaceStatus);

		responseReturn.put("responseData", state);
		responseReturn.put("testInterfaceStatus", testIntefaceStatus);
		
		//status = state.equals(ON) ? "SRC_OFF" : "SRC_ON";
		Ft.logger.debug("S082_ensure_FT_source_stopped : ftBay_SourceStatusPin_Status : status : " + status);
		//============================================================================================	   

		responseReturn.put("status", status);
		
		Ft.logger.debug("S082_ensure_FT_source_stopped : ftBay_SourceStatusPin_Status : Exit");
		return responseReturn;
	}

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
