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
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S01_check_for_pallet_at_HVT_Bay implements HvtBayState {

	private String sequencePathId = "p1";
	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_01;
	private String palletSensorPortCname = ConstantBayPortNameMapping.HV_PORT_NAME_SNSR_PALLET;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_HVT_001;

	private TestInterfaceStatus testInterfaceStatus = new TestInterfaceStatus();
	private boolean logEnabled = true;

	/**
	 * Checks if a pallet is available at the High Voltage Test (HVT) Bay.
	 *
	 * @return BayResponse indicating success or failure.
	 */
	@Override
	public BayResponse handleRequest() {
		Hv.logger.info("S01_check_for_pallet_at_HVT_Bay : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		boolean isPalletAvailableAt_HVTBay;
		long startTime;
		boolean stableDetection = false;
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager()
				.updateBayMonitoringAllPalletsExistInBay(getMyBayKey());
		while (!stableDetection && !ConstantConveyor.ALL_LOOP_BREAK_FLAG && !Hv.isStopProcessRequestedHvtBay()) {
			Map<String, Object> responseReturn = isPalletAvailableAt_HVTBay();
			isPalletAvailableAt_HVTBay = (boolean) responseReturn.get("status");

			if (isPalletAvailableAt_HVTBay) {
				startTime = System.currentTimeMillis();

				while (System.currentTimeMillis() - startTime < ConstantConveyor.STABLE_PALLET_TIME_IN_MSEC) {
					BayUtils.delay(1000); // Small delay to prevent CPU overuse
					responseReturn = isPalletAvailableAt_HVTBay();
					isPalletAvailableAt_HVTBay = (boolean) responseReturn.get("status");

					if (!isPalletAvailableAt_HVTBay) {
						break; // Reset if detection is lost
					}
				}

				// If detection lasted for stable pallet time, confirm stability
				if (isPalletAvailableAt_HVTBay) {
					stableDetection = true;
				}
			} else {
				if (logEnabled) {
					Hv.logger.info("S01_check_for_pallet_at_HVT_Bay : No pallet Available at HVT Bay");
				}
				BayUtils.delay(1000);
			}
			logEnabled = false;
		}

		if (stableDetection) {
			Hv.logger.info("S01_check_for_pallet_at_HVT_Bay : Pallet Available");
			ConveyorDataManager.getDashboardObject().getBayIndicatorManager()
					.updateBayAllPalletsExistInBay(getMyBayKey(), true);
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			Hv.logger.info("S01_check_for_pallet_at_HVT_Bay : Pallet Not Available");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_HVT_001);
		}

		if (bayResponse.getStatus()) {
			BayUtils bayUtils = new BayUtils();
			bayUtils.markAsCompleteForPreviousBayPallet(getMyBayKey(), Hv.logger);
			Hv.logger.info("S01_check_for_pallet_at_HVT_Bay: Waiting for Halt Pallet at Hv-Entry");

			while ((!Hv.isStopProcessRequestedHvtBay()) &&
					(ConveyorDataManager.isHaltPalletActiveInHv())) {
				BayUtils.delay(1000);
			}

			Hv.logger.info("S01_check_for_pallet_at_HVT_Bay: Waiting for Halt Pallet at Hv-Exit");

		}

		Hv.logger.info("S01_check_for_pallet_at_HVT_Bay : Exit");
		return bayResponse;
	}

	// =====================================================================================================
	private Map<String, Object> isPalletAvailableAt_HVTBay() {
		if (logEnabled) {
			Hv.logger.debug("S01_check_for_pallet_at_HVT_Bay : Entry");
		}
		boolean status = false;

		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);

		IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.HV_PORT_NAME_SNSR_PALLET);

		TestInterfaceStatus testIntefaceStatus = new TestInterfaceStatus();

		if (portInfo != null) {
			if (logEnabled) {
				Hv.logger.debug("isPalletAvailableAt_HVTBay : getClusterId: " + portInfo.getClusterId()
						+ " -> getBayId: " + portInfo.getBayId() + " -> getPortId: " + portInfo.getPortId());

			}
			if (getTestInterfaceStatus() == null) {

				testIntefaceStatus = new TestInterfaceStatus(
						ConstantConveyor.HV_BAY_KEY,
						ConstantBayStateManage.BAY_HP_SEQ_01,
						ConstantConveyor.DEVICE_TYPE_CLUSTER_INPUT,
						getSequencePathId(),
						"-",
						portInfo.getPortId(),
						ConstantBayPortNameMapping.HV_PORT_NAME_SNSR_PALLET,
						ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
						"Waiting",
						ConstantConveyor.COMM_EXECUTION_STATUS_INP);

				int newRecordSerialNo = StateExecutorController.addToTestStatusGui(testIntefaceStatus);
				testIntefaceStatus.setSerialNo(String.valueOf(newRecordSerialNo));
			} else {
				testIntefaceStatus = getTestInterfaceStatus();
			}
		} else {
			if (logEnabled) {
				Hv.logger.debug("S01_check_for_pallet_at_HVT_Bay : Output port not found");
			}
			return responseReturn;
		}

		BayUtils bayUtils = new BayUtils();

		String state = bayUtils.getInputDataFromBayV2(portInfo);
		if (logEnabled) {
			Hv.logger.debug("S01_check_for_pallet_at_HVT_Bay : state : " + state);
		}
		status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

		if (StateExecutorController.simulateHvBayHappyPath) {
			status = true;
		}
		if (logEnabled) {
			Hv.logger.debug("S01_check_for_pallet_at_HVT_Bay : status : " + status);
		}
		responseReturn.put("status", status);

		if (logEnabled) {
			Hv.logger.debug("S01_check_for_pallet_at_HVT_Bay : Exit");
		}
		return responseReturn;
	}

	public String getBayStateSequenceId() {
		return bayStateSequenceId;
	}

	public void setBayStateSequenceId(String bayStateSequenceId) {
		this.bayStateSequenceId = bayStateSequenceId;
	}

	public String getPalletSensorPortCname() {
		return palletSensorPortCname;
	}

	public String getFailStateErrorCode() {
		return failStateErrorCode;
	}

	public void setPalletSensorPortCname(String palletSensorPortCname) {
		this.palletSensorPortCname = palletSensorPortCname;
	}

	public void setFailStateErrorCode(String failStateErrorCode) {
		this.failStateErrorCode = failStateErrorCode;
	}

	public TestInterfaceStatus getTestInterfaceStatus() {
		return testInterfaceStatus;
	}

	public void setTestInterfaceStatus(TestInterfaceStatus testInterfaceStatus) {
		this.testInterfaceStatus = testInterfaceStatus;
	}

	public String getSequencePathId() {
		return sequencePathId;
	}

	public void setSequencePathId(String sequencePathId) {
		this.sequencePathId = sequencePathId;
	}
}
