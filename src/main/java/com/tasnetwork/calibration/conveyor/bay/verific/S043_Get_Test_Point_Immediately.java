package com.tasnetwork.calibration.conveyor.bay.verific;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasnetwork.calibration.conveyor.ClusterServer;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.MappedResult;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.pallet.PalletTrackerController;
import com.tasnetwork.calibration.conveyor.remote.ProCalTestResultsResponse;
import com.tasnetwork.calibration.conveyor.remote.ProcalRemoteResponse;
import com.tasnetwork.calibration.conveyor.remote.ProcalRemoteSender;
import com.tasnetwork.calibration.conveyor.remote.Result;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.PalletManage;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S043_Get_Test_Point_Immediately implements VerificTestBayState {

	BayUtils bayUtils = new BayUtils();
	String presentBayKey = "";
	String sequencePathId = "p1";

	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	// ===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Verification.logger.info("S043_Get_Test_Point_Immediately : Entry");

		// BayUtils.delay(10000);

		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		ProcalRemoteSender procalRemoteSender = new ProcalRemoteSender();

		String ip_address = ConstantConveyorConfig.CONVEYOR_PROCAL_VERIFIC_IP_ADDRESS;
		String ip_port = ConstantConveyorConfig.CONVEYOR_PROCAL_VERIFIC_IP_PORT;
		String endpoint = ConstantConveyorConfig.CONVEYOR_PROCAL_VERIFIC_END_PATH;

		ClusterServer verificClusterServer = new ClusterServer(ip_address, ip_port);

		ProcalRemoteResponse myProcalRemoteResponse = procalRemoteSender
				.sendCommResultRefreshToProcal(verificClusterServer, endpoint);


		if (myProcalRemoteResponse.getTestPointStatus().isAllTestExecutionCompleted()) {
			Verification.logger.info("S043_Get_Test_Point_Immediately : Execution Success");

			String myProcalRemoteResult = procalRemoteSender.sendCommAllResultToProcal(verificClusterServer, endpoint);

			Verification.logger.info("S043_Get_Test_Point_Immediately : All results : " + myProcalRemoteResult);

			addTestPointMeterResults(myProcalRemoteResult);

			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			Verification.logger.info("S043_Get_Test_Point_Immediately : all Execution completed Failed");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_026);
		}

		Verification.logger.info("S043_Get_Test_Point_Immediately : Exit");
		return bayResponse;
	}

	// ============================================================================================================================================

	// ============================================================================================================================================

	private void addTestPointMeterResults(String myProcalRemoteResult) {
		Verification.logger.info("S043_Get_Test_Point_Immediately : addTestPointMeterResults : Entry ");

		try {
			/*
			 * ProCalTestResultsResponse procalResult = JsonPath
			 * .from(myProcalRemoteResult)
			 * .getObject("", ProCalTestResultsResponse.class);
			 */

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

			ProCalTestResultsResponse procalResult = mapper.readValue(myProcalRemoteResult,
					ProCalTestResultsResponse.class);

			for (Result eachResult : procalResult.getResults()) {
				Verification.logger.info("S043_Get_Test_Point_Immediately : eachResult :"
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
			Verification.logger.error("S043_Get_Test_Point_Immediately procalResult : Exception: " + e.getMessage());

		}

		PalletTrackerController palletTracker = new PalletTrackerController();

		try {

			JSONObject jsonObject = new JSONObject(myProcalRemoteResult);
			JSONArray results = jsonObject.getJSONArray("Results");

			presentBayKey = ConstantConveyor.VERIFICATION_BAY_KEY; // STNLD1B

			// Fetch the list of pallets
			List<PalletManage> myPalletManageList = MySqlServiceManager.getPalletManageService()
					.findByPresentBayKeyAndPalletActive(presentBayKey);

			Verification.logger.info("S043_Get_Test_Point_Immediately : myPalletManageList : " + myPalletManageList);

			Map<Integer, Boolean> devicePassStatus = new HashMap<>();

			// First pass: Determine if all test cases for a device_name are "P"
			for (int i = 0; i < results.length(); i++) {
				JSONObject result = results.getJSONObject(i);
				int deviceName = result.getInt("device_name");
				String testStatus = result.getString("test_status");

				// Assume the device passes unless a "Fail" is encountered
				devicePassStatus.putIfAbsent(deviceName, true);
				if (testStatus.equals("F")) {
					devicePassStatus.put(deviceName, false);
				}
			}

			// Second pass: Process test results with deduplication
			Set<String> processedTestCases = new HashSet<>(); // Track processed test cases
			for (int verificDataCount = 0; verificDataCount < results.length(); verificDataCount++) {
				JSONObject result = results.getJSONObject(verificDataCount);
				int positionNo = result.getInt("device_name");
				String testCaseName = result.getString("test_case_name");
				String uniqueKey = positionNo + "_" + testCaseName; // Unique identifier for device and test case

				// Skip if this test case for this device was already processed
				if (!processedTestCases.add(uniqueKey)) {
					Verification.logger.warn(
							"S043_Get_Test_Point_Immediately : addTestPointMeterResults : Skipping duplicate test case: "
									+ uniqueKey);
					continue; // Skip to next iteration
				}

				// Original second pass logic continues here
				Integer pos = positionNo;
				String resultValue = result.getString("error_value");
				String error_min = result.getString("error_min");
				String error_max = result.getString("error_max");
				String resultStatus = result.getString("test_status").equals("P") ? "Pass" : "Fail";
				String testType = testCaseName.startsWith(ConstantConveyor.ACCURACY_ALIAS_NAME)
						? ConstantConveyor.VERIFICATION_RESULT_KEY
						: "";

				MappedResult mappedResult = getMappedPosition(positionNo, pos, myPalletManageList);
				int mappedPosition = mappedResult.getMappedPosition();
				PalletManage selectedPallet = mappedResult.getSelectedPallet();

				if (mappedPosition != -1) {
					Verification.logger.info(
							"S043_Get_Test_Point_Immediately : addTestPointMeterResults : Adding result to Meter : "
									+ positionNo);
					Verification.logger.info("S043_Get_Test_Point_Immediately : addTestPointMeterResults : "
							+ positionNo + " : Result Value : " + resultValue);
					palletTracker.addResultToPalletMeterV2(mappedPosition, resultStatus, resultValue, error_min,
							error_max, getMyBayKey(), selectedPallet, testType, testCaseName);
				} else {
					Verification.logger.warn(
							"S043_Get_Test_Point_Immediately : addTestPointMeterResults : No matching pallet found for position: "
									+ positionNo);
				}

				Verification.logger.debug("S043_Get_Test_Point_Immediately : verificDataCount : " + verificDataCount);

			}

			// Third pass: Update summary once per device
			for (Map.Entry<Integer, Boolean> entry : devicePassStatus.entrySet()) {
				int positionNo = entry.getKey();
				Integer pos = positionNo;
				boolean allPass = entry.getValue();

				String finalResultValue = allPass ? "Pass" : "Fail";
				String finalResultStatus = allPass ? "Pass" : "Fail";

				String testType = ConstantConveyor.VERIFICATION_RESULT_KEY; // "LOE";
				String testCaseName = ConstantConveyor.SUMMARY_VERIFICATION_RESULT_TEST_NAME; // "Verification";

				MappedResult mappedResult = getMappedPosition(positionNo, pos, myPalletManageList);
				int mappedPosition = mappedResult.getMappedPosition();
				PalletManage selectedPallet = mappedResult.getSelectedPallet();

				if (mappedPosition != -1) {
					Verification.logger.info(
							"S043_Get_Test_Point_Immediately : addTestPointMeterResults : Adding summary for Meter : "
									+ mappedPosition);
					palletTracker.addMeterResultSummaryWithPalletDetails(mappedPosition, finalResultStatus,
							finalResultValue, testCaseName, testType, selectedPallet);
					// palletTracker.addMeterResultSummary(mappedPosition, finalResultStatus,
					// finalResultValue, "Functional Test", "FT", selectedPallet);
				} else {
					Verification.logger.warn(
							"S043_Get_Test_Point_Immediately : addTestPointMeterResults : No matching pallet found for position: "
									+ mappedPosition);
				}
			}

		} catch (org.json.JSONException e) {
			Verification.logger.error("Error parsing JSON: " + e.getMessage(), e);
		}

		Verification.logger.info("S043_Get_Test_Point_Immediately : addTestPointMeterResults : Exit ");
	}

	/**
	 * Helper function to map device positions to pallet positions.
	 */
	private MappedResult getMappedPosition(int positionNo, int pos, List<PalletManage> myPalletManageList) {
		int mappedPosition = -1;
		PalletManage selectedPallet = null;

		// Linear Mapping
		if (myPalletManageList.size() >= 4) {

			Verification.logger.debug("getMappedPosition: Hit3");
			if (ConstantConveyor.VERIFIC_PALLET1_POSITIONS.contains(pos)) {
				Verification.logger.debug("getMappedPosition: Hit4");
				selectedPallet = myPalletManageList.get(3); // Pallet 1 (at index 3 as per existing logic)
				mappedPosition = positionNo; // Positions 1-6 map directly to 1-6 for Pallet 1
			} else if (ConstantConveyor.VERIFIC_PALLET2_POSITIONS.contains(pos)) {
				Verification.logger.debug("getMappedPosition: Hit5");
				selectedPallet = myPalletManageList.get(2); // Pallet 2 (at index 2)
				mappedPosition = positionNo - 6; // Positions 7-12 map to 1-6 for Pallet 2
			} else if (ConstantConveyor.VERIFIC_PALLET3_POSITIONS.contains(pos)) {
				Verification.logger.debug("getMappedPosition: Hit6");
				selectedPallet = myPalletManageList.get(1); // Pallet 3 (at index 1)
				mappedPosition = positionNo - 12; // Positions 13-18 map to 1-6 for Pallet 3
			} else if (ConstantConveyor.VERIFIC_PALLET4_POSITIONS.contains(pos)) {
				Verification.logger.debug("getMappedPosition: Hit7");
				selectedPallet = myPalletManageList.get(0); // Pallet 4 (at index 0)
				mappedPosition = positionNo - 18; // Positions 19-24 map to 1-6 for Pallet 4
			}
			Verification.logger.debug("getMappedPosition: Hit8");
		} else {
			Verification.logger.warn(
					"getMappedPosition: Not enough pallets in myPalletManageList! Size: " + myPalletManageList.size());
		}

		Verification.logger.info("Mapping Position " + positionNo + " -> Mapped to " + mappedPosition
				+ " | Assigned to Pallet: " + (selectedPallet != null ? selectedPallet.toString() : "None"));

		return new MappedResult(mappedPosition, selectedPallet);
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

	/*
	 * public String getMyBayKey() {
	 * return myBayKey;
	 * }
	 */

}
