package com.tasnetwork.calibration.conveyor.bay.sta_nld1;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasnetwork.calibration.conveyor.ClusterServer;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.pallet.PalletTrackerController;
import com.tasnetwork.calibration.conveyor.remote.ProCalTestResultsResponse;
import com.tasnetwork.calibration.conveyor.remote.ProcalRemoteResponse;
import com.tasnetwork.calibration.conveyor.remote.ProcalRemoteSender;
import com.tasnetwork.calibration.conveyor.remote.Result;
import com.tasnetwork.calibration.conveyor.remote.TestResult;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.PalletManage;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S043_Get_Test_Point_Status_Bay1 implements STA_NoLoadTestBay1State {

	BayUtils bayUtils = new BayUtils();
	String presentBayKey = "";
	String sequencePathId = "p1";

	// private String myBayKey = ConstantConveyor.STA_NLD1_BAY_KEY;

	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	// ===========================================================================================
	@Override
	public BayResponse handleRequest() {
		StaNld_Bay1.logger.info("S043_Get_Test_Point_Status : Entry");

		// BayUtils.delay(10000);

		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		ProcalRemoteSender procalRemoteSender = new ProcalRemoteSender();

		String ip_address = ConstantConveyorConfig.CONVEYOR_PROCAL_STA1_IP_ADDRESS;
		String ip_port = ConstantConveyorConfig.CONVEYOR_PROCAL_STA1_IP_PORT;
		String endpoint = ConstantConveyorConfig.CONVEYOR_PROCAL_STA1_END_PATH;

		ClusterServer stdNldt1ClusterServer = new ClusterServer(ip_address, ip_port);

		ProcalRemoteResponse myProcalRemoteResponse = procalRemoteSender
				.sendCommResultRefreshToProcal(stdNldt1ClusterServer, endpoint);

		// VerificationTestBay.logger.info("S043_Get_Test_Point_Status :
		// isAllTestExecutionCompleted: " +

		int i = 0;
		int delayCount = 10;
		while (!(myProcalRemoteResponse.getTestPointStatus().isAllTestExecutionCompleted()) &&
				!(StaNld_Bay1.isStopProcessRequestedStaNldBay1()) &&
				// i < 120 && // commented by Gopi on d0.8.5.4 06-jul-2025
				(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {

			StaNld_Bay1.logger.info("S043_Get_Test_Point_Status : Getting test point status...");
			// BayUtils.delay(10000); // 10 sec once
			delayCount = 10;
			while ((delayCount > 0) && (!StaNld_Bay1.isStopProcessRequestedStaNldBay1())) {
				delayCount--;
				BayUtils.delay(1000);
			}
			if (StaNld_Bay1.isStopProcessRequestedStaNldBay1()) {
				StaNld_Bay1.logger.info("S043_Get_Test_Point_Status : sta1 stop requested: break");
				break;
			}

			i++;

			myProcalRemoteResponse = procalRemoteSender.sendCommResultRefreshToProcal(stdNldt1ClusterServer, endpoint);

			ArrayList<TestResult> presentTpResult;
			presentTpResult = myProcalRemoteResponse.getTestPointStatus().getPresentTpResult();

			StaNld_Bay1.logger.info("S043_Get_Test_Point_Status : presentTpResult : " + presentTpResult);

		}
		StaNld_Bay1.logger.info("S043_Get_Test_Point_Status : Getting test point status: isAllTestExecutionCompleted: "
				+ myProcalRemoteResponse.getTestPointStatus().isAllTestExecutionCompleted());
		StaNld_Bay1.logger.info("S043_Get_Test_Point_Status : Getting test point status: isStopExecutionRequested: "
				+ StaNld_Bay1.isStopProcessRequestedStaNldBay1());
		StaNld_Bay1.logger.info("S043_Get_Test_Point_Status : Getting test point status: ALL_LOOP_BREAK_FLAG: "
				+ ConstantConveyor.ALL_LOOP_BREAK_FLAG);
		StaNld_Bay1.logger.info("S043_Get_Test_Point_Status : Getting test point status: iteration count : " + i);

		if (myProcalRemoteResponse.getTestPointStatus().isAllTestExecutionCompleted()) {
			StaNld_Bay1.logger.info("S043_Get_Test_Point_Status : Execution Success");
			int retryCount = 3;
			String myProcalRemoteResult = "";
			boolean expectedResultRecieved = false;
			String expectedContent = "\"Results\":";
			while ((retryCount > 0) &&
					(!StaNld_Bay1.isStopProcessRequestedStaNldBay1()) &&
					(!expectedResultRecieved)) {
				myProcalRemoteResult = procalRemoteSender.sendCommAllResultToProcal(stdNldt1ClusterServer, endpoint);
				retryCount--;
				if (myProcalRemoteResult.contains(expectedContent)) {
					if (myProcalRemoteResult.length() > 40) {
						StaNld_Bay1.logger.info("S043_Get_Test_Point_Status : Expected result found");
						expectedResultRecieved = true;
					} else {
						StaNld_Bay1.logger
								.info("S043_Get_Test_Point_Status : length not sufficient: " + myProcalRemoteResult);
					}
				}
			}
			StaNld_Bay1.logger.info("S043_Get_Test_Point_Status : All results : " + myProcalRemoteResult);
			if (expectedResultRecieved) {
				addTestPointMeterResults(myProcalRemoteResult);

				bayResponse.setStatus(true);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
			} else {
				StaNld_Bay1.logger.info("S043_Get_Test_Point_Status : Expected result not found");
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_028);
			}
		} else {
			StaNld_Bay1.logger.info("S043_Get_Test_Point_Status : Execution Failed");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_026);
		}

		StaNld_Bay1.logger.info("S043_Get_Test_Point_Status : Exit");
		return bayResponse;
	}

	// ============================================================================================================================================

	private void addTestPointMeterResults(String myProcalRemoteResult) {
		StaNld_Bay1.logger.info("S043_Get_Test_Point_Status : addTestPointMeterResults : Entry ");

		PalletTrackerController palletTracker = new PalletTrackerController();

		try {
			presentBayKey = ConstantConveyor.STA_NLD1_BAY_KEY; // STNLD1B

			// Fetch the list of pallets
			List<PalletManage> myPalletManageList = MySqlServiceManager.getPalletManageService()
					.findByPresentBayKeyAndPalletActive(presentBayKey);

			StaNld_Bay1.logger
					.info("S043_Get_Test_Point_Status : myPalletManageList : size: " + myPalletManageList.size());

			/*
			 * PalletManage selectedPallet = null;
			 * selectedPallet = myPalletManageList.get(0);
			 * 
			 * STA_NoLoadTestBay1.logger.
			 * info("S043_Get_Test_Point_Status : selectedPallet : 0 " + selectedPallet);
			 */
			ProCalTestResultsResponse procalResult = new ProCalTestResultsResponse();
			try {
				/*
				 * procalResult = JsonPath
				 * .from(myProcalRemoteResult)
				 * .getObject("", ProCalTestResultsResponse.class);
				 */

				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

				procalResult = mapper.readValue(myProcalRemoteResult, ProCalTestResultsResponse.class);

				for (Result eachResult : procalResult.getResults()) {
					StaNld_Bay1.logger.info("S043_Get_Test_Point_Status : eachResult :"
							+ ", Pallet:" + eachResult.getPallet_distinct_id()
							+ ", serialNo: " + eachResult.getDut_serial_no()
							+ ", lduPosition: " + eachResult.getDevice_name()
							+ ", palletPosition: " + eachResult.getPallet_rack_position_no()
							+ ", TestCase: " + eachResult.getTest_case_name()
							+ ", ErrorValue: <" + eachResult.getError_value()
							+ ">, ErrorStatus: <" + eachResult.getTest_status()
							+ ">, getRun_id: " + eachResult.getRun_id());
				}

			} catch (Exception e) {
				e.printStackTrace();
				StaNld_Bay1.logger.error("S043_Get_Test_Point_Status procalResult : Exception: " + e.getMessage());

			}

			// for (int i = 0; i < results.length(); i++) {

			int positionNo = 0;
			String resultValue = "";//
			String resultStatus = "";
			String testCaseName = "";
			String dutSerialNo = "";
			String testType = "";//
			String palletDistinctId = "";
			String error_min = "";
			String error_max = "";
			for (Result eachResult : procalResult.getResults()) {
				// JSONObject result = results.getJSONObject(i);
				try {
					testCaseName = eachResult.getTest_case_name();
					if (testCaseName != null && testCaseName.trim().equalsIgnoreCase("NLD_SKIP")) {
						StaNld_Bay1.logger
								.info("S043_Get_Test_Point_Status : Skipping ignored test point: " + testCaseName
										+ " for position: " + eachResult.getPallet_rack_position_no()
										+ ", serialNo: " + eachResult.getDut_serial_no());
						continue;
					}

					if (eachResult.getPallet_rack_position_no().isEmpty()) {
						positionNo = 0;
					} else {
						positionNo = Integer.parseInt(eachResult.getPallet_rack_position_no());
					}
					// positionNo = Integer.parseInt(eachResult.getPallet_rack_position_no());
					palletDistinctId = eachResult.getPallet_distinct_id();// Integer.parseInt(eachResult.getDevice_name());//result.getInt("device_name");
					// Integer pos = positionNo; // Explicitly convert to Integer
					resultValue = eachResult.getError_value();// result.getString("error_value");
					resultStatus = eachResult.getTest_status().equals("P") ? "Pass" : "Fail";// result.getString("test_status").equals("P")
																								// ? "Pass" : "Fail";
					dutSerialNo = eachResult.getDut_serial_no();// result.getString("dut_serial_no");
					testType = testCaseName.startsWith("NLD") ? ConstantConveyor.NLD_RESULT_KEY
							: ConstantConveyor.STA_RESULT_KEY; // "NLD" : "STA";
					StaNld_Bay1.logger.info("S043_Get_Test_Point_Status : testCaseName " + testCaseName + "-> testType:"
							+ testType + ", dutSerialNo: " + dutSerialNo + " ,palletDistinctId:" + palletDistinctId);

					// Determine the correct pallet based on position number
					// PalletManage selectedPallet = null;
					// int mappedPosition = positionNo;

					// if (selectedPallet != null) {
					if (positionNo != 0) {
						StaNld_Bay1.logger.info(
								"S043_Get_Test_Point_Status : addTestPointMeterResults : Adding result to Meter : "
										+ positionNo);
						// palletTracker.addResultToPalletMeter(mappedPosition, resultStatus,
						// resultValue, getMyBayKey(), selectedPallet, testType, testCaseName);
						palletTracker.addResultToPalletMeterV1_1(positionNo, dutSerialNo, resultStatus, resultValue,
								getMyBayKey(), palletDistinctId, testType, testCaseName, error_min, error_max);

						if (testType.equals(ConstantConveyor.NLD_RESULT_KEY)) { // "NLD") {
							testCaseName = ConstantConveyor.NLD_RESULT_TEST_NAME; // "No Load";
						} else if (testType.equals(ConstantConveyor.STA_RESULT_KEY)) { // "STA"){
							testCaseName = ConstantConveyor.STA_RESULT_TEST_NAME; // "Starting Current";
						}

						// palletTracker.addMeterResultSummaryWithPalletDetails(positionNo,
						// resultStatus, resultStatus, testCaseName, testType, selectedPallet);

						palletTracker.addMeterResultSummaryWithPalletDetailsV2(positionNo, dutSerialNo, resultStatus,
								resultStatus, testCaseName, testType, palletDistinctId);
					} else {
						StaNld_Bay1.logger.info(
								"S043_Get_Test_Point_Status : addTestPointMeterResults : Skipping : " + positionNo);
					}
				} catch (Exception e) {
					e.printStackTrace();
					StaNld_Bay1.logger.error("S043_Get_Test_Point_Status procalResult : Exception2: " + e.getMessage());

				}
			}

		} catch (org.json.JSONException e) {
			StaNld_Bay1.logger.error("Error parsing JSON: " + e.getMessage(), e);
		}

		StaNld_Bay1.logger.info("S043_Get_Test_Point_Status : addTestPointMeterResults : Exit ");
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
