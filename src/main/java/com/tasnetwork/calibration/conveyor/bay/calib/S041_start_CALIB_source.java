package com.tasnetwork.calibration.conveyor.bay.calib;

import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Source;

import org.springframework.cglib.transform.impl.AddDelegateTransformer;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
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

public class S041_start_CALIB_source implements CalibrationBayState {

	BayUtils bayUtils = new BayUtils();

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	// ===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Calib.logger.info("S071_start_CALIB_source : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		Map<String, Object> responseReturn = startCalibSource();
		boolean startCalibSource = (boolean) responseReturn.get("status");

		if (startCalibSource) {
			Calib.logger.info("S071_start_CALIB_source : CALIB Source Started");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			Calib.logger.info("S071_start_CALIB_source : Failed to Start CALIB Source");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_015);
		}

		Calib.logger.info("S071_start_CALIB_source : Exit");
		return bayResponse;
	}
	// ============================================================================================================================================

	/*
	 * private Map<String,Object> startCalibSource() {
	 * 
	 * CalibrationBay.logger.
	 * debug("S071_start_CALIB_source : startCalibSource : Entry");
	 * 
	 * setSequencePathId("p1");
	 * setPalletAvailableTest_I_F_Status (null);
	 * Map<String,Object> responseReturn = turn_on_start_pin_ft_bay();
	 * ConveyorDebugController.updateTestInterfaceStatusOnGui(responseReturn,
	 * ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
	 * 
	 * //responseReturn.put("status", true);
	 * boolean isSrcStarted_CalibBay = (boolean)responseReturn.get("status");
	 * 
	 * if (isSrcStarted_CalibBay) {
	 * CalibrationBay.logger.
	 * debug("S071_start_CALIB_source : startCalibSource : Turned on Source Start Pin : Success"
	 * );
	 * BayUtils.delay(500);
	 * } else {
	 * CalibrationBay.logger.
	 * debug("S071_start_CALIB_source : startCalibSource : Failed to turn on Source Start Pin"
	 * );
	 * isSrcStarted_CalibBay = false;
	 * }
	 * 
	 * status = turn_off_start_pin_ft_bay();
	 * if (status) {
	 * CalibrationBay.logger.
	 * debug("S08_functional_Test : startCalibSource : Turned Off Source Start Pin : Success"
	 * );
	 * 
	 * } else {
	 * CalibrationBay.logger.
	 * debug("S08_functional_Test : startCalibSource : Failed to turn off Source Start Pin"
	 * );
	 * status = false;
	 * }
	 * 
	 * 
	 * 
	 * 
	 * CalibrationBay.logger.debug("S08_functional_Test : startCalibSource : Exit");
	 * return responseReturn;
	 * }
	 */
	// ============================================================================================================================================

	private Map<String, Object> startCalibSource() {
		Calib.logger.debug("S071_turn_on_start_pin_ft_bay : startCalibSource : Entry");

		boolean status = false;
		String state = "";
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);

		// Add Procal Source Start Here

		// responseReturn.put("status", status);
		responseReturn.put("responseData", state);
		responseReturn.put("status", status);

		Calib.logger
				.debug("S071_turn_on_start_pin_ft_bay : startCalibSource : status : " + responseReturn.get("status"));

		Calib.logger.debug("S071_turn_on_start_pin_ft_bay : startCalibSource : Exit");
		return responseReturn;
	}
	// ============================================================================================================================================

	/*
	 * private Map<String,Object> turn_off_start_pin_ft_bay() {
	 * CalibrationBay.logger.
	 * debug("S071_turn_off_start_pin_ft_bay : ftBay_StartPin_Status : Entry");
	 * 
	 * boolean status = false;
	 * Map<String,Object> responseReturn = new HashMap<String,Object>();
	 * responseReturn.put("status", false);
	 * 
	 * IoPortInfo portInfo =
	 * BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.
	 * CALIB_PORT_NAME_SRC_START);
	 * 
	 * if (portInfo != null) {
	 * CalibrationBay.logger.debug("PortId    : " + portInfo.getPortId());
	 * CalibrationBay.logger.debug("ClusterId : " + portInfo.getClusterId());
	 * CalibrationBay.logger.debug("BayId     : " + portInfo.getBayId());
	 * } else {
	 * CalibrationBay.logger.
	 * debug("S071_turn_off_start_pin_ft_bay : Output port not found");
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
	 * CalibrationBay.logger.
	 * debug("S071_turn_off_start_pin_ft_bay : ftBay_StartPin_Status : status : " +
	 * status);
	 * 
	 * responseReturn.put("status", status);
	 * 
	 * CalibrationBay.logger.
	 * debug("S071_turn_off_start_pin_ft_bay : ftBay_StartPin_Status : Exit");
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
