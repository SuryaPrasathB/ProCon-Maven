package com.tasnetwork.calibration.conveyor.bay.verific;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S051_Stop_VerificTest_Source implements VerificTestBayState {
	BayUtils bayUtils = new BayUtils();

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Verification.logger.info("S071_Stop_Verific_source : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601 );

		Map<String,Object> responseReturn =  stopCalibSource();	 
		boolean stopCalibSource = (boolean)responseReturn.get("status");
		
		if (stopCalibSource) {
			Verification.logger.info("S071_stop_Verific_source : Verific Source stopped");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			Verification.logger.info("S071_stop_Verific_source : Failed to stop Verific Source");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_024);
		}

		Verification.logger.info("S071_stop_Verific_source : Exit");
		return bayResponse;
	}
	//============================================================================================================================================  

	/*private Map<String,Object> stopCalibSource() {
		// TODO Auto-generated method stub
		VerificationTestBay.logger.debug("S071_stop_Verific_source : stopCalibSource : Entry");

		setSequencePathId("p1");
		setPalletAvailableTest_I_F_Status (null);
		Map<String,Object> responseReturn =  turn_on_stop_pin_ft_bay();	 
		ConveyorDebugController.updateTestInterfaceStatusOnGui(responseReturn,ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);

		//responseReturn.put("status", true);
		boolean isSrcstoped_CalibBay = (boolean)responseReturn.get("status");

		if (isSrcstoped_CalibBay) {
			VerificationTestBay.logger.debug("S071_stop_Verific_source : stopCalibSource : Turned on Source stop Pin : Success");
			BayUtils.delay(500);
		} else {
			VerificationTestBay.logger.debug("S071_stop_Verific_source : stopCalibSource : Failed to turn on Source stop Pin");
			isSrcstoped_CalibBay = false;
		}

		status = turn_off_stop_pin_ft_bay();
		if (status) {
	        VerificationTestBay.logger.debug("S08_functional_Test : stopCalibSource : Turned Off Source stop Pin : Success");

		} else {
	        VerificationTestBay.logger.debug("S08_functional_Test : stopCalibSource : Failed to turn off Source stop Pin");
	        status = false;
		}
		
		
		

		VerificationTestBay.logger.debug("S08_functional_Test : stopCalibSource : Exit");
		return responseReturn;
	}*/
	//============================================================================================================================================  

	private Map<String, Object> stopCalibSource() {
		Verification.logger.debug("S071_turn_on_stop_pin_ft_bay : stopCalibSource : Entry");

		boolean status = false;
		String state = "" ;
 		Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);

 
		
	//	Add Procal Source stop Here
		
		


		//responseReturn.put("status", status);
		responseReturn.put("responseData", state);
		responseReturn.put("status", status);

 
		Verification.logger.debug("S071_turn_on_stop_pin_ft_bay : stopCalibSource : status : " + responseReturn.get("status"));
		
		Verification.logger.debug("S071_turn_on_stop_pin_ft_bay : stopCalibSource : Exit");
		return responseReturn;
	}
	//============================================================================================================================================  


	/*private Map<String,Object> turn_off_stop_pin_ft_bay() {
		VerificationTestBay.logger.debug("S071_turn_off_stop_pin_ft_bay : ftBay_stopPin_Status : Entry");

		boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
		IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.Verific_PORT_NAME_SRC_stop);

		if (portInfo != null) {
			VerificationTestBay.logger.debug("PortId    : " + portInfo.getPortId());
			VerificationTestBay.logger.debug("ClusterId : " + portInfo.getClusterId());
			VerificationTestBay.logger.debug("BayId     : " + portInfo.getBayId());
		} else {
			VerificationTestBay.logger.debug("S071_turn_off_stop_pin_ft_bay : Output port not found");
			return responseReturn ;
        }

		BayUtils bayUtils = new BayUtils();

		String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
				portInfo.getBayId(),
				portInfo.getPortId(),
				Constant_IO_ActionMapping.OPEN);
		status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;
		VerificationTestBay.logger.debug("S071_turn_off_stop_pin_ft_bay : ftBay_stopPin_Status : status : " + status); 

		responseReturn.put("status", status);
		
		VerificationTestBay.logger.debug("S071_turn_off_stop_pin_ft_bay : ftBay_stopPin_Status : Exit");
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
