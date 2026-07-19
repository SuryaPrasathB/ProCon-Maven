package com.tasnetwork.calibration.conveyor.bay.calib;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.DevSysEnergyMeter;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmDut;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.DeviceSetting;
import com.tasnetwork.spring.orm.model.TerminalProfileSetting;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S046_Turn_On_Meter_Relays implements CalibrationBayState {

	private static final int MAX_NUM_OF_METERS = 6;

	BayUtils bayUtils = new BayUtils();

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	// ===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Calib.logger.info("S046_Turn_On_Meter_Relays : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		// Map<String,Object> responseReturn = startCalibSource();
		// boolean startCalibSource = (boolean)responseReturn.get("status");

		int positionNum = 1;

		for (int i = 0; i < MAX_NUM_OF_METERS; i++) { // do it for all 6 meters
			positionNum = i + 1;

			TerminalProfileSetting terminalBayProfile = new TerminalProfileSetting();
			terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService()
					.findByBayKey(ConstantConveyor.CALIBRATION_BAY_KEY);

			String deviceId = terminalBayProfile.getTerminalId() + terminalBayProfile.getClusterId() +
					terminalBayProfile.getBayId() +
					ConstantConveyor.DEVICE_TYPE_DUT +
					String.format("%02d", positionNum);

			Calib.logger.debug("S046_Turn_On_Meter_Relays : deviceId : " + deviceId + " : Position No: " + positionNum);

			DevSysEnergyMeter devSysEnergyMeter = new DevSysEnergyMeter(Calib.logger);
			String portCname = "";

			ConveyorDataManager deviceDataManager = new ConveyorDataManager();
			DeviceSetting deviceSetting = deviceDataManager.getDeviceSettingByDeviceId(deviceId);
			portCname = deviceSetting.getCanName();

			Calib.logger
					.debug("S046_Turn_On_Meter_Relays : portCname : " + portCname + " : Position No: " + positionNum);
			Calib.logger.debug("S046_Turn_On_Meter_Relays: getPortName: " + deviceSetting.getPortName()
					+ " : Position No: " + positionNum);

			setSequencePathId("p1");
			TestInterfaceStatus testIntefaceStatus = new TestInterfaceStatus();

			testIntefaceStatus = new TestInterfaceStatus(
					ConstantConveyor.CALIBRATION_BAY_KEY,
					ConstantBayStateManage.BAY_HP_SEQ_10,
					ConstantConveyor.DEVICE_TYPE_DUT,
					getSequencePathId(),
					"" + positionNum,
					deviceSetting.getPortName(), // "-",
					portCname,
					ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
					ConstantConveyor.RELAY_ON_CMD_RESULT_TEST_NAME, // "RelayOnCmd",
					ConstantConveyor.COMM_EXECUTION_STATUS_INP);

			/*
			 * testIntefaceStatus.setBayName(ConstantConveyor.FT_BAY_KEY);
			 * testIntefaceStatus.setStateName(ConstantBayStateManage.FT_BAY_HP_SEQ_09);
			 * testIntefaceStatus.setcName(portCname);
			 * testIntefaceStatus.setDeviceType(ConstantConveyor.DEVICE_TYPE_DUT);
			 * testIntefaceStatus.setSerialStatus(ConstantConveyor.
			 * COMM_STATUS_NOT_APPLICABLE);
			 * testIntefaceStatus.setPositionNo(getSequencePathId()+ "-" + positionNum);
			 * testIntefaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_INP);
			 * testIntefaceStatus.setDeviceResponseData("Dut-SNo-ReadCmd");
			 */

			int serialNo = StateExecutorController.addToTestStatusGui(testIntefaceStatus);

			testIntefaceStatus.setSerialNo(String.valueOf(serialNo));
			// SpmDut spManager = devSysEnergyMeter.serialPortInit(portCname);

			SpmDut spManager = null;
			spManager = devSysEnergyMeter.serialPortInitV2(deviceSetting);

			if (spManager == null) {
				testIntefaceStatus.setSerialStatus(ConstantConveyor.COMM_ACCESS_FAILED);
				StateExecutorController.updateTestStatusGui(testIntefaceStatus);
				// return responseReturn;
			} else {
				testIntefaceStatus.setSerialStatus("Success");
				StateExecutorController.updateTestStatusGui(testIntefaceStatus);
				boolean status = devSysEnergyMeter.sendDeviceUnlockCommand(positionNum, spManager);
				if (status) {
					status = devSysEnergyMeter.sendRelayOnCommand(spManager); // dfgvdv status =
																				// sendDeviceUnlockCommand(positionNum,spManager);
					if (status) {
						testIntefaceStatus.setDeviceResponseStatus("Success");
						// resultStatus = ConstantReport.REPORT_POPULATE_PASS;
					} else {
						testIntefaceStatus.setDeviceResponseStatus("Failed");
						// resultStatus = ConstantReport.REPORT_POPULATE_FAIL;
					}
				} else {
					testIntefaceStatus.setDeviceResponseStatus("Failed");
					// resultStatus = ConstantReport.REPORT_POPULATE_FAIL;
				}
				spManager.disconnectDut();
			}

			testIntefaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
			StateExecutorController.updateTestStatusGui(testIntefaceStatus);

		}

		/*
		 * TestInterfaceStatus testIntefaceStatus = new TestInterfaceStatus();
		 * SpmDut spManager = null;
		 * boolean status = false;
		 * String resultStatus = "";
		 * DevSysEnergyMeter devSysEnergyMeter = new DevSysEnergyMeter();
		 * 
		 * status = devSysEnergyMeter.sendRelayOnCommand(spManager);
		 * if(status){
		 * testIntefaceStatus.setDeviceResponseStatus("Success");
		 * resultStatus = ConstantReport.REPORT_POPULATE_PASS;
		 * 
		 * bayResponse.setStatus(true);
		 * bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		 * }else{
		 * testIntefaceStatus.setDeviceResponseStatus("Failed");
		 * resultStatus = ConstantReport.REPORT_POPULATE_FAIL;
		 * 
		 * bayResponse.setStatus(false);
		 * bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_021);
		 * }
		 */

		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		Calib.logger.info("S046_Turn_On_Meter_Relays : Exit");
		return bayResponse;
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
