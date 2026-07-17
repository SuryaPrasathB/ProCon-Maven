package com.tasnetwork.calibration.conveyor.bay.verific;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
//import com.tasnetwork.calibration.conveyor.bay_calibration.CalibrationBay;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S041_Load_Project_From_DB implements VerificTestBayState  {

	BayUtils bayUtils = new BayUtils();

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Verification.logger.info("S041_Load_Project_From_DB : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601 );
		
		/*ProjectExecutionController projectExecutionController = new ProjectExecutionController();
		projectExecutionController.initiateLoadProjectFromDB();
		
		while (!projectExecutionController.isProjectLoadedFromDB()){//isVoltageStartAcknowledged()) {
			CalibrationBay.logger.info("S041_Load_Project_From_DB : Project Load : awaiting..");		
			BayUtils.delay(1000);
		}		
		
		if (projectExecutionController.isProjectLoadedFromDB()) {
			VerificationTestBay.logger.info("S041_Load_Project_From_DB : Project Load Success");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			VerificationTestBay.logger.info("S041_Load_Project_From_DB : Project Load Failed");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.);
		}*/

		Verification.logger.info("S041_Load_Project_From_DB : Exit");
		return bayResponse;
	}
	//============================================================================================================================================  

	/*private Map<String,Object> startCalibSource() {
		// TODO Auto-generated method stub
		VerificationTestBay.logger.debug("S041_Load_Project_From_DB : startCalibSource : Entry");

		setSequencePathId("p1");
		setPalletAvailableTest_I_F_Status (null);
		Map<String,Object> responseReturn =  turn_on_start_pin_ft_bay();	 
		ConveyorDebugController.updateTestInterfaceStatusOnGui(responseReturn,ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);

		//responseReturn.put("status", true);
		boolean isSrcStarted_CalibBay = (boolean)responseReturn.get("status");

		if (isSrcStarted_CalibBay) {
			VerificationTestBay.logger.debug("S041_Load_Project_From_DB : startCalibSource : Turned on Source Start Pin : Success");
			BayUtils.delay(500);
		} else {
			VerificationTestBay.logger.debug("S041_Load_Project_From_DB : startCalibSource : Failed to turn on Source Start Pin");
			isSrcStarted_CalibBay = false;
		}

		status = turn_off_start_pin_ft_bay();
		if (status) {
	        VerificationTestBay.logger.debug("S08_functional_Test : startCalibSource : Turned Off Source Start Pin : Success");

		} else {
	        VerificationTestBay.logger.debug("S08_functional_Test : startCalibSource : Failed to turn off Source Start Pin");
	        status = false;
		}
		
		
		

		VerificationTestBay.logger.debug("S08_functional_Test : startCalibSource : Exit");
		return responseReturn;
	}*/
	//============================================================================================================================================  

	private Map<String, Object> startCalibSource() {
		Verification.logger.debug("S071_turn_on_start_pin_ft_bay : startCalibSource : Entry");

		boolean status = false;
		String state = "" ;
 		Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);

 
		
	//	Add Procal Source Start Here
		
		


		//responseReturn.put("status", status);
		responseReturn.put("responseData", state);
		responseReturn.put("status", status);

 
		Verification.logger.debug("S071_turn_on_start_pin_ft_bay : startCalibSource : status : " + responseReturn.get("status"));
		
		Verification.logger.debug("S071_turn_on_start_pin_ft_bay : startCalibSource : Exit");
		return responseReturn;
	}
	//============================================================================================================================================  


	/*private Map<String,Object> turn_off_start_pin_ft_bay() {
		VerificationTestBay.logger.debug("S071_turn_off_start_pin_ft_bay : ftBay_StartPin_Status : Entry");

		boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
		IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.Verific_PORT_NAME_SRC_START);

		if (portInfo != null) {
			VerificationTestBay.logger.debug("PortId    : " + portInfo.getPortId());
			VerificationTestBay.logger.debug("ClusterId : " + portInfo.getClusterId());
			VerificationTestBay.logger.debug("BayId     : " + portInfo.getBayId());
		} else {
			VerificationTestBay.logger.debug("S071_turn_off_start_pin_ft_bay : Output port not found");
			return responseReturn ;
        }

		BayUtils bayUtils = new BayUtils();

		String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
				portInfo.getBayId(),
				portInfo.getPortId(),
				Constant_IO_ActionMapping.OPEN);
		status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;
		VerificationTestBay.logger.debug("S071_turn_off_start_pin_ft_bay : ftBay_StartPin_Status : status : " + status); 

		responseReturn.put("status", status);
		
		VerificationTestBay.logger.debug("S071_turn_off_start_pin_ft_bay : ftBay_StartPin_Status : Exit");
		return responseReturn;
	}*/
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
