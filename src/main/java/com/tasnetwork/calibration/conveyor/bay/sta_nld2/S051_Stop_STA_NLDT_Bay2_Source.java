package com.tasnetwork.calibration.conveyor.bay.sta_nld2;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
//import com.tasnetwork.calibration.conveyor.bay_calibration.CalibrationBay;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S051_Stop_STA_NLDT_Bay2_Source implements STA_NoLoadTestBay2State {

	BayUtils bayUtils = new BayUtils();

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	// ===========================================================================================
	@Override
	public BayResponse handleRequest() {
		StaNld_Bay2.logger.info("S071_Stop_STA_NLDT_source : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		Map<String, Object> responseReturn = StopSTA_NLDTSource();
		boolean StopSTA_NLDTSource = (boolean) responseReturn.get("status");

		if (StopSTA_NLDTSource) {
			StaNld_Bay2.logger.info("S071_Stop_STA_NLDT_source : STA_NLDT  Bay 1 Source Stopped");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			StaNld_Bay2.logger.info("S071_Stop_STA_NLDT_source : Failed to Stop STA_NLDT  Bay 1 Source");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_022);
		}

		StaNld_Bay2.logger.info("S071_Stop_STA_NLDT_source : Exit");
		return bayResponse;
	}
	// ============================================================================================================================================

	/*
	 * private Map<String,Object> StopSTA_NLDTSource() {
	 * 
	 * STA_NoLoadTestBay2.logger.
	 * debug("S071_Stop_STA_NLDT_source : StopSTA_NLDTSource : Entry");
	 * 
	 * setSequencePathId("p1");
	 * setPalletAvailableTest_I_F_Status (null);
	 * Map<String,Object> responseReturn = turn_on_Stop_pin_ft_bay();
	 * ConveyorDebugController.updateTestInterfaceStatusOnGui(responseReturn,
	 * ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
	 * 
	 * //responseReturn.put("status", true);
	 * boolean isSrcStoped_STA_NLDTBay = (boolean)responseReturn.get("status");
	 * 
	 * if (isSrcStoped_STA_NLDTBay) {
	 * STA_NoLoadTestBay2.logger.
	 * debug("S071_Stop_STA_NLDT_source : StopSTA_NLDTSource : Turned on Source Stop Pin : Success"
	 * );
	 * BayUtils.delay(500);
	 * } else {
	 * STA_NoLoadTestBay2.logger.
	 * debug("S071_Stop_STA_NLDT_source : StopSTA_NLDTSource : Failed to turn on Source Stop Pin"
	 * );
	 * isSrcStoped_STA_NLDTBay = false;
	 * }
	 * 
	 * status = turn_off_Stop_pin_ft_bay();
	 * if (status) {
	 * STA_NoLoadTestBay2.logger.
	 * debug("S08_functional_Test : StopSTA_NLDTSource : Turned Off Source Stop Pin : Success"
	 * );
	 * 
	 * } else {
	 * STA_NoLoadTestBay2.logger.
	 * debug("S08_functional_Test : StopSTA_NLDTSource : Failed to turn off Source Stop Pin"
	 * );
	 * status = false;
	 * }
	 * 
	 * 
	 * 
	 * 
	 * STA_NoLoadTestBay2.logger.
	 * debug("S08_functional_Test : StopSTA_NLDTSource : Exit");
	 * return responseReturn;
	 * }
	 */
	// ============================================================================================================================================

	private Map<String, Object> StopSTA_NLDTSource() {
		StaNld_Bay2.logger.debug("S071_turn_on_Stop_pin_ft_bay : StopSTA_NLDTSource : Entry");

		boolean status = false;
		String state = "";
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);

		// Add Procal Source Stop Here

		// responseReturn.put("status", status);
		responseReturn.put("responseData", state);
		responseReturn.put("status", status);

		StaNld_Bay2.logger
				.debug("S071_turn_on_Stop_pin_ft_bay : StopSTA_NLDTSource : status : " + responseReturn.get("status"));

		StaNld_Bay2.logger.debug("S071_turn_on_Stop_pin_ft_bay : StopSTA_NLDTSource : Exit");
		return responseReturn;
	}
	// ============================================================================================================================================

	/*
	 * private Map<String,Object> turn_off_Stop_pin_ft_bay() {
	 * STA_NoLoadTestBay2.logger.
	 * debug("S071_turn_off_Stop_pin_ft_bay : ftBay_StopPin_Status : Entry");
	 * 
	 * boolean status = false;
	 * Map<String,Object> responseReturn = new HashMap<String,Object>();
	 * responseReturn.put("status", false);
	 * 
	 * IoPortInfo portInfo =
	 * BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.
	 * STA_NLDT_PORT_NAME_SRC_Stop);
	 * 
	 * if (portInfo != null) {
	 * STA_NoLoadTestBay2.logger.debug("PortId    : " + portInfo.getPortId());
	 * STA_NoLoadTestBay2.logger.debug("ClusterId : " + portInfo.getClusterId());
	 * STA_NoLoadTestBay2.logger.debug("BayId     : " + portInfo.getBayId());
	 * } else {
	 * STA_NoLoadTestBay2.logger.
	 * debug("S071_turn_off_Stop_pin_ft_bay : Output port not found");
	 * return responseReturn ;
	 * }
	 * 
	 * BayUtils bayUtils = new BayUtils();
	 * 
	 * String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
	 * portInfo.getBayId(),
	 * portInfo.getPortId(),
	 * Constant_IO_ActionMapping.OPEN);
	 * status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;
	 * STA_NoLoadTestBay2.logger.
	 * debug("S071_turn_off_Stop_pin_ft_bay : ftBay_StopPin_Status : status : " +
	 * status);
	 * 
	 * responseReturn.put("status", status);
	 * 
	 * STA_NoLoadTestBay2.logger.
	 * debug("S071_turn_off_Stop_pin_ft_bay : ftBay_StopPin_Status : Exit");
	 * return responseReturn;
	 * }
	 */
	// ============================================================================================================================================

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
