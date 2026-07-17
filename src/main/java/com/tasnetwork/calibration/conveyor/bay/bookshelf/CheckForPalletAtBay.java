package com.tasnetwork.calibration.conveyor.bay.bookshelf;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.bay.calib.Calib;
import com.tasnetwork.calibration.conveyor.bay.ft.Ft;
import com.tasnetwork.calibration.conveyor.bay.hv.Hv;
import com.tasnetwork.calibration.conveyor.bay.ir.Ir;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.StaNld_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.StaNld_Bay2;
import com.tasnetwork.calibration.conveyor.bay.verific.Verification;
import com.tasnetwork.calibration.conveyor.bay.verific_waiting.VerificWaiting;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;



public class CheckForPalletAtBay {


	private boolean simulateBayHappyPath = false;
	private Logger eachBaylogger = null; 
	private String bayKey = "";

	private String failPathErrorCode = ConvErrorCodeMapping.ERROR_CODE_602;
	private String stateManageSeqNo = "";//ConstantBayStateManage.FT_BAY_HP_SEQ_01,
	private String palletSensorPortName = "";//ConstantBayPortNameMapping.FT_PORT_NAME_SNSR_PALLET,

	private String flowPathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	public CheckForPalletAtBay(Logger logger, String bayKey,String stateManageSeqNo, 
			String palletSensorPortName, String failPathErrorCode, boolean simulateHpPath ){
		this.eachBaylogger = logger;
		this.bayKey = bayKey;
		this.stateManageSeqNo = stateManageSeqNo;
		this.palletSensorPortName = palletSensorPortName;
		this.failPathErrorCode = failPathErrorCode;
		this.simulateBayHappyPath = simulateHpPath;
	}

	public BayResponse checkForPalletAtBayProcess() {
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(false);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		//		if (FunctionalTestBay2.stopper_B4_FT_Bay_Status.equals("STOPPER_B4_FT_BAY_OPENED")) {  	

		setFlowPathId("p1");
		setPalletAvailableTest_I_F_Status (null);

		bayResponse =  isPalletAvailableAtBay();	 
		StateExecutorController.updateTestInterfaceStatusOnGuiV2(bayResponse,ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
		boolean isPalletAvailableAt_Bay = bayResponse.getStatus();//(boolean)responseReturn.get("status");		

		while ( (!isPalletAvailableAt_Bay) &&
				(!Ft.isStopProcessRequestedFtBay()) &&
				(!Hv.isStopProcessRequestedHvtBay()) && 
				(!Ir.isStopProcessRequestedIrtBay()) &&
				(!Calib.isStopProcessRequestedCalibBay()) &&
				(!VerificWaiting.isStopProcessRequestedWaitingBay()) &&
				(!Verification.isStopProcessRequestedVerificBay()) &&
				(!StaNld_Bay1.isStopProcessRequestedStaNldBay1()) &&
				(!StaNld_Bay2.isStopProcessRequestedStaNldBay2()) &&
				(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {      // add timeout logic

			eachBaylogger.info("checkForPalletAtBayProcess : No pallet Available at Bay : " + getBayKey());
			eachBaylogger.info("checkForPalletAtBayProcess : isStopProcessRequestedFtBay : "  + Ft.isStopProcessRequestedFtBay());
			eachBaylogger.info("checkForPalletAtBayProcess : isStopProcessRequestedHvtBay : "  + Hv.isStopProcessRequestedHvtBay());
			BayUtils.delay(1000);

			//responseReturn =  isPalletAvailableAt_FtBay();	 
			//isPalletAvailableAt_FtBay = (boolean)responseReturn.get("status");

			bayResponse =  isPalletAvailableAtBay();	
			setPalletAvailableTest_I_F_Status(bayResponse.getTestInterfaceStatus());
			isPalletAvailableAt_Bay = bayResponse.getStatus();
		}

		if (isPalletAvailableAt_Bay) {
			eachBaylogger.info("checkForPalletAtBayProcess : Pallet Available"  + " : " +getBayKey());
			//bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {  
			eachBaylogger.info("checkForPalletAtBayProcess : No Pallet Available at Bay"  + " : " +getBayKey());
			//bayResponse.setStatus(false);
			bayResponse.setErrorCode(getFailPathErrorCode());
		}			

		//		} 

		/*		else if (FunctionalTestBay2.stopper_B4_FT_Bay_Status.equals("STOPPER_B4_FT_BAY_CLOSED")) {

			Map<String,Object> responseReturn =  isPalletAvailableAt_FtBay();	 
			boolean isPalletAvailableAt_FtBay = (boolean)responseReturn.get("status");	

			if (isPalletAvailableAt_FtBay) {
				eachBaylogger.info("checkForPalletAtBayProcess : Pallet Available");
				bayResponse.setStatus(true);
				bayResponse.setErrorCode(ErrorCodeMapping.ERROR_CODE_601);
			} else {
				eachBaylogger.info("checkForPalletAtBayProcess : No Pallet Available at FT Bay");
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ErrorCodeMapping.ERROR_CODE_FT_034);
			}		
		}*/

		eachBaylogger.info("checkForPalletAtBayProcess : Exit");
		return bayResponse;
	}

	private BayResponse isPalletAvailableAtBay() {
		eachBaylogger.debug("isPalletAvailableAtBay : Entry"  + " : " +getBayKey());

		boolean status = false;

		//Map<String,Object> responseReturn = new HashMap<String,Object>();
		//responseReturn.put("status", false);
		BayResponse bayResponse = new BayResponse();
		//String state = "";
		//============================================================================================		 
		IoPortInfo portInfo = BayUtils.getInputPortDetails(getPalletSensorPortName()); 
		// OutputPortInfo portInfo = Utils.getOutputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_RED_LED);
		TestInterfaceStatus testIntefaceStatus = new TestInterfaceStatus();
		//int newRecordSerialNo = 0;
		if (portInfo != null) {
			eachBaylogger.debug("PortId    : " + portInfo.getPortId());
			eachBaylogger.debug("ClusterId : " + portInfo.getClusterId());
			eachBaylogger.debug("BayId     : " + portInfo.getBayId());

			if(getPalletAvailableTest_I_F_Status()==null){

				testIntefaceStatus = new TestInterfaceStatus(
						getBayKey(),
						getStateManageSeqNo(),
						ConstantConveyor.DEVICE_TYPE_CLUSTER_INPUT,
						getFlowPathId(),
						"-",
						portInfo.getPortId(),
						getPalletSensorPortName(),						
						ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
						"Waiting",
						ConstantConveyor.COMM_EXECUTION_STATUS_INP
						);


				int newRecordSerialNo = StateExecutorController.addToTestStatusGui(testIntefaceStatus);
				testIntefaceStatus.setSerialNo(String.valueOf(newRecordSerialNo));
			}else{
				testIntefaceStatus = getPalletAvailableTest_I_F_Status();
			}

		} else {
			eachBaylogger.debug("isPalletAvailableAtBay : Output port not found"  + " : " +getBayKey());
		}



		BayUtils bayUtils = new BayUtils();
		/*BayUtils_Cluster2 bayUtils = new BayUtils_Cluster2();*/


		/*		String state = bayUtils.getInputDataFromBay(portInfo.getClusterId(), 
				portInfo.getBayId(), 
				portInfo.getPortId()) ;*/

		String state = bayUtils.getInputDataFromBayV2(portInfo) ;



		eachBaylogger.debug("isPalletAvailableAtBay : state : " + state + " : " +getBayKey());

		status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

		if(simulateBayHappyPath){
			status = true;
		}

		testIntefaceStatus.setPortName(portInfo.getPortId());
		//testIntefaceStatus.setPositionNo(getSequencePathId());
		if(status){
			testIntefaceStatus.setDeviceResponseStatus("Success");
			//responseReturn.put("status", true);
			bayResponse.setStatus(true);
		}else{
			testIntefaceStatus.setDeviceResponseStatus("Failed");
			//responseReturn.put("status", false);
			bayResponse.setStatus(false);
		}

		if(portInfo.getPortId().equals(state)){
			if(simulateBayHappyPath){
				testIntefaceStatus.setDeviceResponseData(state);
			}else {
				state = "TimeOut";
				testIntefaceStatus.setDeviceResponseData("TimeOut");
			}
		}else{
			testIntefaceStatus.setDeviceResponseData(state);
		}


		//testIntefaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_INP);
		StateExecutorController.updateTestStatusGui(testIntefaceStatus);

		//responseReturn.put("responseData", status);
		bayResponse.setResponseData(state);
		//responseReturn.put("recordId", newRecordSerialNo);
		//responseReturn.put("testInterfaceStatus", testIntefaceStatus);
		bayResponse.setTestInterfaceStatus(testIntefaceStatus);
		eachBaylogger.debug("isPalletAvailableAtBay : status : " + status + " : " +getBayKey());
		//============================================================================================	  
		eachBaylogger.debug("isPalletAvailableAtBay : Exit" + " : " +getBayKey());
		//return status;
		//return responseReturn;
		return bayResponse;
	}

	public String getFlowPathId() {
		return flowPathId;
	}

	public void setFlowPathId(String sequencePathId) {
		this.flowPathId = sequencePathId;
	}

	public TestInterfaceStatus getPalletAvailableTest_I_F_Status() {
		return palletAvailableTest_I_F_Status;
	}

	public void setPalletAvailableTest_I_F_Status(TestInterfaceStatus palletAvailableTest_I_F_Status) {
		this.palletAvailableTest_I_F_Status = palletAvailableTest_I_F_Status;
	}

	public String getFailPathErrorCode() {
		return failPathErrorCode;
	}

	public String getStateManageSeqNo() {
		return stateManageSeqNo;
	}

	public String getPalletSensorPortName() {
		return palletSensorPortName;
	}

	public void setFailPathErrorCode(String failPathErrorCode) {
		this.failPathErrorCode = failPathErrorCode;
	}

	public void setStateManageSeqNo(String stateManageSeqNo) {
		this.stateManageSeqNo = stateManageSeqNo;
	}

	public void setPalletSensorPortName(String palletSensorPortName) {
		this.palletSensorPortName = palletSensorPortName;
	}

	public Logger getEachBaylogger() {
		return eachBaylogger;
	}

	public String getBayKey() {
		return bayKey;
	}

	public void setEachBaylogger(Logger eachBaylogger) {
		this.eachBaylogger = eachBaylogger;
	}

	public void setBayKey(String bayKey) {
		this.bayKey = bayKey;
	}

}
