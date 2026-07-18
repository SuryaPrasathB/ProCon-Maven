package com.tasnetwork.calibration.conveyor.bay.sta_nld1;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;

import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S041_Start_STA_NLDT_Bay1_Source implements STA_NoLoadTestBay1State {

	BayUtils bayUtils = new BayUtils();

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	// ===========================================================================================
	@Override
	public BayResponse handleRequest() {
		StaNld_Bay1.logger.info("S071_start_STA_NLDT_source : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		Map<String, Object> responseReturn = startSTA_NLDTSource();
		boolean startSTA_NLDTSource = (boolean) responseReturn.get("status");

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
	// ============================================================================================================================================


	// ============================================================================================================================================

	private Map<String, Object> startSTA_NLDTSource() {
		StaNld_Bay1.logger.debug("S071_turn_on_start_pin_ft_bay : startSTA_NLDTSource : Entry");

		boolean status = false;
		String state = "";
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);

		// Add Procal Source Start Here

		// responseReturn.put("status", status);
		responseReturn.put("responseData", state);
		responseReturn.put("status", status);

		StaNld_Bay1.logger.debug(
				"S071_turn_on_start_pin_ft_bay : startSTA_NLDTSource : status : " + responseReturn.get("status"));

		StaNld_Bay1.logger.debug("S071_turn_on_start_pin_ft_bay : startSTA_NLDTSource : Exit");
		return responseReturn;
	}
	// ============================================================================================================================================


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
