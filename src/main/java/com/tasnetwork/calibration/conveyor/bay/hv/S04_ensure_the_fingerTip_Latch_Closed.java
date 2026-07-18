package com.tasnetwork.calibration.conveyor.bay.hv;

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
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S04_ensure_the_fingerTip_Latch_Closed implements HvtBayState {

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	/**
	 * Ensures that the fingertip latch is successfully closed at the HVT Bay.
	 *
	 * @return BayResponse indicating success or failure.
	 */
	@Override
	public BayResponse handleRequest() {
		Hv.logger.info("S04_ensure_the_fingerTip_Latch_Closed : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		int try_count = 0;

		setSequencePathId("p1");
		setPalletAvailableTest_I_F_Status(null);

		Map<String, Object> responseReturn = new HashMap<String, Object>();// ftBay_FingerTipLatch_Status();
		String fingerLatchPresentState = "";// (String)responseReturn.get("responseData");

		while (try_count <= 3) {

			responseReturn = hvtBay_FingerTipLatch_Status();
			// String hvtBay_FingerTipLatch_Status = (String)responseReturn.get("status");
			fingerLatchPresentState = (String) responseReturn.get("responseData");

			setPalletAvailableTest_I_F_Status((TestInterfaceStatus) responseReturn.get("testInterfaceStatus"));

			if (fingerLatchPresentState.equals(Constant_IO_ActionMapping.OPEN)) {

				StateExecutorController.updateTestInterfaceStatusOnGui(responseReturn,
						ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
				ConveyorDataManager.getDashboardObject().getBayIndicatorManager()
						.setPalletsLockedImageDisplayOn(getMyBayKey(), true);
				ConveyorDataManager.getDashboardObject().getBayIndicatorManager().startTimeUpDisplay(getMyBayKey());

				if (ProconFeatureEnable.MOTOR_CONTROL_ENABLE) {
					BayUtils bayUtils = new BayUtils();
					responseReturn = bayUtils.set_motor_not_required(getMyBayKey());
					boolean set_motor_not_required = (boolean) responseReturn.get("status");
					if (set_motor_not_required) {
						Hv.logger.info("set_motor_not_required : Success");
						bayResponse.setStatus(true);
						bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
					} else {
						Hv.logger.info("Failed to set_motor_not_required ");
						bayResponse.setStatus(false);
						bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_026);
					}
				}
				// bayResponse.setStatus(true);
				// bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
				break;
			} else {
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_HVT_004);
				BayUtils.delay(1000);
				try_count++;
			}
		}

		Hv.logger.info("S04_ensure_the_fingerTip_Latch_Closed : Exit");
		return bayResponse;
	}
	// ============================================================================================================================================

	private Map<String, Object> hvtBay_FingerTipLatch_Status() {
		Hv.logger.debug("S04_ensure_the_fingerTip_Latch_Closed : hvtBay_FingerTipLatch_Status : Entry");

		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);

		TestInterfaceStatus testIntefaceStatus = new TestInterfaceStatus();

		IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.HV_PORT_NAME_SNSR_FINGER_TIP);

		if (portInfo != null) {
			Hv.logger.debug("PortId    : " + portInfo.getPortId());
			Hv.logger.debug("ClusterId : " + portInfo.getClusterId());
			Hv.logger.debug("BayId     : " + portInfo.getBayId());

			if (getPalletAvailableTest_I_F_Status() == null) {

				testIntefaceStatus = new TestInterfaceStatus(
						ConstantConveyor.HV_BAY_KEY,
						ConstantBayStateManage.BAY_HP_SEQ_04,
						ConstantConveyor.DEVICE_TYPE_CLUSTER_INPUT,
						getSequencePathId(),
						"-",
						portInfo.getPortId(),
						ConstantBayPortNameMapping.HV_PORT_NAME_SNSR_FINGER_TIP,
						ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
						"Waiting",
						ConstantConveyor.COMM_EXECUTION_STATUS_INP);

				int newRecordSerialNo = StateExecutorController.addToTestStatusGui(testIntefaceStatus);
				testIntefaceStatus.setSerialNo(String.valueOf(newRecordSerialNo));
			} else {
				testIntefaceStatus = getPalletAvailableTest_I_F_Status();
			}
		} else {
			Hv.logger.debug(
					"S04_ensure_the_fingerTip_Latch_Closed : hvtBay_FingerTipLatch_Status : Output port not found");
			return responseReturn;
		}

		BayUtils bayUtils = new BayUtils();

		String state = bayUtils.getInputDataFromBayV2(portInfo);

		state = state.equals(Constant_IO_ActionMapping.ON) ? Constant_IO_ActionMapping.OPEN
				: Constant_IO_ActionMapping.CLOSE;

		if (state.equals(Constant_IO_ActionMapping.OPEN)) {
			testIntefaceStatus.setDeviceResponseStatus("Success");
			responseReturn.put("status", true);
		} else {
			testIntefaceStatus.setDeviceResponseStatus("Failed");
			responseReturn.put("status", false);
		}
		if (portInfo.getPortId().equals(state)) {
			state = "TimeOut";
			testIntefaceStatus.setDeviceResponseData("TimeOut");
		} else {
			testIntefaceStatus.setDeviceResponseData(state);
		}

		if (StateExecutorController.simulateHvBayHappyPath) {
			state = Constant_IO_ActionMapping.OPEN;
		}

		StateExecutorController.updateTestStatusGui(testIntefaceStatus);

		responseReturn.put("status", state);
		responseReturn.put("responseData", state);
		responseReturn.put("testInterfaceStatus", testIntefaceStatus);

		Hv.logger.debug("S04_ensure_the_fingerTip_Latch_Closed : hvtBay_FingerTipLatch_Status : state : " + state);
		Hv.logger.debug("S04_ensure_the_fingerTip_Latch_Closed : hvtBay_FingerTipLatch_Status : Exit");
		return responseReturn;
	}

	public String getSequencePathId() {
		return sequencePathId;
	}

	public TestInterfaceStatus getPalletAvailableTest_I_F_Status() {
		return palletAvailableTest_I_F_Status;
	}

	public void setSequencePathId(String sequencePathId) {
		this.sequencePathId = sequencePathId;
	}

	public void setPalletAvailableTest_I_F_Status(TestInterfaceStatus palletAvailableTest_I_F_Status) {
		this.palletAvailableTest_I_F_Status = palletAvailableTest_I_F_Status;
	}
}
