package com.tasnetwork.calibration.conveyor.bay.comm;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S04_ensure_the_fingerTip_Latch_Closed implements CommTestBayState {

	// ===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Comm.logger.info("S04_ensure_the_fingerTip_Latch_Closed : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		int try_count = 0;

		while (try_count <= 3) {

			Map<String, Object> responseReturn = commBay_FingerTipLatch_Status();
			String commBay_FingerTipLatch_Status = (String) responseReturn.get("status");

			if (commBay_FingerTipLatch_Status.equals(Constant_IO_ActionMapping.OPEN)) {

				if (ProconFeatureEnable.MOTOR_CONTROL_ENABLE) {
					BayUtils bayUtils = new BayUtils();
					responseReturn = bayUtils.set_motor_not_required(getMyBayKey());
					boolean set_motor_not_required = (boolean) responseReturn.get("status");
					if (set_motor_not_required) {
						Comm.logger.info("set_motor_not_required : Success");
						bayResponse.setStatus(true);
						bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
					} else {
						Comm.logger.info("Failed to set_motor_not_required ");
						bayResponse.setStatus(false);
						bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_026);
					}
				}
				// bayResponse.setStatus(true);
				// bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
				break;
			} else {
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_COMM_004);
				BayUtils.delay(1000);
				try_count++;
			}
		}

		Comm.logger.info("S04_ensure_the_fingerTip_Latch_Closed : Exit");
		return bayResponse;
	}
	// ============================================================================================================================================

	private Map<String, Object> commBay_FingerTipLatch_Status() {
		Comm.logger.debug("S04_ensure_the_fingerTip_Latch_Closed : commBay_FingerTipLatch_Status : Entry");

		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);

		IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.COMM_PORT_NAME_SNSR_FINGER_TIP);

		TestInterfaceStatus testInterfaceStatus = null;
		if (portInfo != null) {
			testInterfaceStatus = new TestInterfaceStatus(
					ConstantConveyor.COMMUNICATION_BAY_KEY,
					"-",
					ConstantConveyor.DEVICE_TYPE_CLUSTER_INPUT,
					"-",
					"-",
					portInfo.getPortId(),
					ConstantBayPortNameMapping.COMM_PORT_NAME_SNSR_FINGER_TIP,
					ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
					"Executing",
					ConstantConveyor.COMM_EXECUTION_STATUS_INP);
			StateExecutorController.addToTestStatusGui(testInterfaceStatus);

			Comm.logger.debug("PortId    : " + portInfo.getPortId());
			Comm.logger.debug("ClusterId : " + portInfo.getClusterId());
			Comm.logger.debug("BayId     : " + portInfo.getBayId());
		} else {
			Comm.logger.debug(
					"S04_ensure_the_fingerTip_Latch_Closed : commBay_FingerTipLatch_Status : Output port not found");
			return responseReturn;
		}

		BayUtils bayUtils = new BayUtils();

		/*
		 * String state = bayUtils.getInputDataFromBay(portInfo.getClusterId(),
		 * portInfo.getBayId(),
		 * portInfo.getPortId());
		 * if (testInterfaceStatus != null) {
		 * testInterfaceStatus.setDeviceResponseData(state);
		 * testInterfaceStatus.setTestStatus("Success");
		 * StateExecutorController.updateTestStatusGui(testInterfaceStatus);
		 * }
		 * 
		 */

		String state = bayUtils.getInputDataFromBayV2(portInfo);

		state = state.equals(Constant_IO_ActionMapping.ON) ? Constant_IO_ActionMapping.OPEN
				: Constant_IO_ActionMapping.CLOSE;

		if (StateExecutorController.simulateCommBayHappyPath) {
			state = Constant_IO_ActionMapping.OPEN;
		}

		Comm.logger.debug("S04_ensure_the_fingerTip_Latch_Closed : commBay_FingerTipLatch_Status : state : " + state);

		responseReturn.put("status", state);

		Comm.logger.debug("S04_ensure_the_fingerTip_Latch_Closed : commBay_FingerTipLatch_Status : Exit");
		return responseReturn;
	}
}
