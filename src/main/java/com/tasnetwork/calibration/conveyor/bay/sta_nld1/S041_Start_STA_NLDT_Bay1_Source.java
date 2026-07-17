package com.tasnetwork.calibration.conveyor.bay.sta_nld1;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
//import com.tasnetwork.calibration.conveyor.bay_calibration.CalibrationBay;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S041_Start_STA_NLDT_Bay1_Source implements STA_NoLoadTestBay1State{

	
	BayUtils bayUtils = new BayUtils();

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		StaNld_Bay1.logger.info("S071_start_STA_NLDT_source : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601 );

		Map<String,Object> responseReturn =  startSTA_NLDTSource();	 
		boolean startSTA_NLDTSource = (boolean)responseReturn.get("status");
		
		if (startSTA_NLDTSource) {
			StaNld_Bay1.logger.info("S071_start_STA_NLDT_source : STA_NLDT  Bay 1 Source Started");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			StaNld_Bay1.logger.info("S071_start_STA_NLDT_source : Failed to Start STA_NLDT Bay 1 Source");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_021);
		}

		StaNld_Bay1.logger.info("S071_start_STA_NLDT_source : Exit");
		return bayResponse;
	}
	//============================================================================================================================================  

	/*private Map<String,Object> startSTA_NLDTSource() {
		// TODO Auto-generated method stub
		STA_NoLoadTestBay1.logger.debug("S071_start_STA_NLDT_source : startSTA_NLDTSource : Entry");

		setSequencePathId("p1");
		setPalletAvailableTest_I_F_Status (null);
		Map<String,Object> responseReturn =  turn_on_start_pin_ft_bay();	 
		ConveyorDebugController.updateTestInterfaceStatusOnGui(responseReturn,ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);

		//responseReturn.put("status", true);
		boolean isSrcStarted_STA_NLDTBay = (boolean)responseReturn.get("status");

		if (isSrcStarted_STA_NLDTBay) {
			STA_NoLoadTestBay1.logger.debug("S071_start_STA_NLDT_source : startSTA_NLDTSource : Turned on Source Start Pin : Success");
			BayUtils.delay(500);
		} else {
			STA_NoLoadTestBay1.logger.debug("S071_start_STA_NLDT_source : startSTA_NLDTSource : Failed to turn on Source Start Pin");
			isSrcStarted_STA_NLDTBay = false;
		}

		status = turn_off_start_pin_ft_bay();
		if (status) {
	        STA_NoLoadTestBay1.logger.debug("S08_functional_Test : startSTA_NLDTSource : Turned Off Source Start Pin : Success");

		} else {
	        STA_NoLoadTestBay1.logger.debug("S08_functional_Test : startSTA_NLDTSource : Failed to turn off Source Start Pin");
	        status = false;
		}
		
		
		

		STA_NoLoadTestBay1.logger.debug("S08_functional_Test : startSTA_NLDTSource : Exit");
		return responseReturn;
	}*/
	//============================================================================================================================================  

	private Map<String, Object> startSTA_NLDTSource() {
		StaNld_Bay1.logger.debug("S071_turn_on_start_pin_ft_bay : startSTA_NLDTSource : Entry");

		boolean status = false;
		String state = "" ;
 		Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);

 
		
	//	Add Procal Source Start Here
		
		


		//responseReturn.put("status", status);
		responseReturn.put("responseData", state);
		responseReturn.put("status", status);

 
		StaNld_Bay1.logger.debug("S071_turn_on_start_pin_ft_bay : startSTA_NLDTSource : status : " + responseReturn.get("status"));
		
		StaNld_Bay1.logger.debug("S071_turn_on_start_pin_ft_bay : startSTA_NLDTSource : Exit");
		return responseReturn;
	}
	//============================================================================================================================================  


	/*private Map<String,Object> turn_off_start_pin_ft_bay() {
		STA_NoLoadTestBay1.logger.debug("S071_turn_off_start_pin_ft_bay : ftBay_StartPin_Status : Entry");

		boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
		IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.STA_NLDT_PORT_NAME_SRC_START);

		if (portInfo != null) {
			STA_NoLoadTestBay1.logger.debug("PortId    : " + portInfo.getPortId());
			STA_NoLoadTestBay1.logger.debug("ClusterId : " + portInfo.getClusterId());
			STA_NoLoadTestBay1.logger.debug("BayId     : " + portInfo.getBayId());
		} else {
			STA_NoLoadTestBay1.logger.debug("S071_turn_off_start_pin_ft_bay : Output port not found");
			return responseReturn ;
        }

		BayUtils bayUtils = new BayUtils();

		String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
				portInfo.getBayId(),
				portInfo.getPortId(),
				Constant_IO_ActionMapping.OPEN);
		status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;
		STA_NoLoadTestBay1.logger.debug("S071_turn_off_start_pin_ft_bay : ftBay_StartPin_Status : status : " + status); 

		responseReturn.put("status", status);
		
		STA_NoLoadTestBay1.logger.debug("S071_turn_off_start_pin_ft_bay : ftBay_StartPin_Status : Exit");
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
