package com.tasnetwork.calibration.conveyor.bay.calib;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S051_stop_CALIB_source implements CalibrationBayState {

	BayUtils bayUtils = new BayUtils();

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	// ===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Calib.logger.info("S071_stop_CALIB_source : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		Map<String, Object> responseReturn = stopCalibSource();
		boolean stopCalibSource = (boolean) responseReturn.get("status");

		if (stopCalibSource) {
			Calib.logger.info("S071_stop_CALIB_source : CALIB Source Started");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			Calib.logger.info("S071_stop_CALIB_source : Failed to Start CALIB Source");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_015);
		}

		Calib.logger.info("S071_stop_CALIB_source : Exit");
		return bayResponse;
	}
	// ============================================================================================================================================

	private Map<String, Object> stopCalibSource() {
		Calib.logger.debug("S071_turn_on_stop_pin_ft_bay : stopCalibSource : Entry");

		boolean status = false;
		String state = "";
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);

		// Add Procal Source Stop Here

		// responseReturn.put("status", status);
		responseReturn.put("responseData", state);
		responseReturn.put("status", status);

		Calib.logger.debug("S071_turn_on_stop_pin_ft_bay : stopCalibSource : status : " + responseReturn.get("status"));

		Calib.logger.debug("S071_turn_on_stop_pin_ft_bay : stopCalibSource : Exit");
		return responseReturn;
	}
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
