package com.tasnetwork.calibration.conveyor.bay.verific;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasnetwork.calibration.conveyor.ClusterServer;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.MappedResult;
//import com.tasnetwork.calibration.conveyor.bay_verificationtest.S046_Stop_Execution;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.pallet.PalletTrackerController;
import com.tasnetwork.calibration.conveyor.remote.ProCalTestResultsResponse;
import com.tasnetwork.calibration.conveyor.remote.ProcalRemoteResponse;
import com.tasnetwork.calibration.conveyor.remote.ProcalRemoteSender;
import com.tasnetwork.calibration.conveyor.remote.Result;
import com.tasnetwork.calibration.conveyor.remote.TestPointStatus;
import com.tasnetwork.calibration.conveyor.remote.TestResult;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.spring.orm.model.PalletManage;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

import io.restassured.path.json.JsonPath;

public class S043_Get_Test_Point_Immediately implements VerificTestBayState  {

	BayUtils bayUtils = new BayUtils();
	String presentBayKey = "";
	String sequencePathId = "p1";

	//private String myBayKey = ConstantConveyor.VERIFICATION_BAY_KEY;
	public String getMyBayKey() {
		return myBayKey;
	}
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Verification.logger.info("S043_Get_Test_Point_Immediately : Entry");

		//BayUtils.delay(10000);

		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601 );


		ProcalRemoteSender procalRemoteSender = new ProcalRemoteSender();

		String ip_address = ConstantConveyorConfig.CONVEYOR_PROCAL_VERIFIC_IP_ADDRESS;
		String ip_port = ConstantConveyorConfig.CONVEYOR_PROCAL_VERIFIC_IP_PORT;
		String endpoint = ConstantConveyorConfig.CONVEYOR_PROCAL_VERIFIC_END_PATH;

		ClusterServer verificClusterServer = new ClusterServer(ip_address, ip_port);

		ProcalRemoteResponse myProcalRemoteResponse = procalRemoteSender.sendCommResultRefreshToProcal(verificClusterServer, endpoint);

		/*while (!(myProcalRemoteResponse.getTestPointStatus().isAllTestExecutionCompleted()) && 
				(!VerificationTestBay2.isStopProcessRequestedVerificBay()) &&
				(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)){

			VerificationTestBay.logger.info("S043_Get_Test_Point_Status : Getting test point status...");		
			BayUtils.delay(10000); // 10 sec once

			myProcalRemoteResponse = procalRemoteSender.sendCommResultRefreshToProcal(verificClusterServer, endpoint);

			ArrayList<TestResult> presentTpResult;
			presentTpResult = myProcalRemoteResponse.getTestPointStatus().getPresentTpResult();

			VerificationTestBay.logger.info("S043_Get_Test_Point_Status : presentTpResult : " + presentTpResult);	

		}*/


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
	//==========NEW LOGIC FOR RESULT UPDATION==================================================================================================================================

	/*private static final int MINIMUM_PALLET_COUNT = 4;

	private void addTestPointMeterResults(String myProcalRemoteResult) {
	    VerificationTestBay.logger.info("S043_Get_Test_Point_Status : addTestPointMeterResults : Entry");

	    if (myProcalRemoteResult == null || myProcalRemoteResult.isEmpty()) {
	        VerificationTestBay.logger.warn("Received null or empty result string");
	        return;
	    }

	    PalletTrackerController palletTracker = new PalletTrackerController();

	    try {
	        JSONObject jsonObject = new JSONObject(myProcalRemoteResult);
	        JSONArray results = jsonObject.optJSONArray("Results");
	        if (results == null) {
	            VerificationTestBay.logger.error("No 'Results' array found in JSON");
	            return;
	        }

	        presentBayKey = ConstantConveyor.VERIFICATION_BAY_KEY;
	        List<PalletManage> myPalletManageList = MySqlServiceManager.getPalletManageService()
	                .findByPresentBayKeyAndPalletActive(presentBayKey);

	        if (myPalletManageList == null || myPalletManageList.isEmpty()) {
	            VerificationTestBay.logger.warn("No pallets found for bay key: " + presentBayKey);
	            return;
	        }

	        VerificationTestBay.logger.info("S043_Get_Test_Point_Status : myPalletManageList : " + myPalletManageList);

	        // Single pass processing
	        Map<Integer, DeviceTestResults> deviceResults = processTestResults(results, myPalletManageList, palletTracker);

	        // Process summaries
	        processTestSummaries(deviceResults, myPalletManageList, palletTracker);

	    } catch (JSONException e) {
	        VerificationTestBay.logger.error("Error parsing JSON: " + e.getMessage(), e);
	    }

	    VerificationTestBay.logger.info("S043_Get_Test_Point_Status : addTestPointMeterResults : Exit");
	}

	 *//**
	 * Processes test results in a single pass and returns device status map
	 *//*
	private Map<Integer, DeviceTestResults> processTestResults(JSONArray results, 
	        List<PalletManage> pallets, PalletTrackerController palletTracker) {
	    Map<Integer, DeviceTestResults> deviceResults = new HashMap<>();

	    for (int i = 0; i < results.length(); i++) {
	        JSONObject result = results.optJSONObject(i);
	        if (!validateResult(result)) {
	            continue;
	        }

	        int deviceName = result.getInt("device_name");
	        String testStatus = result.getString("test_status");
	        String errorValue = result.optString("error_value", "N/A");
	        String testCaseName = result.getString("test_case_name");
	        String testType = determineTestType(testCaseName);

	        // Update device results
	        DeviceTestResults devResults = deviceResults.computeIfAbsent(deviceName, 
	            k -> new DeviceTestResults(true));
	        if ("F".equals(testStatus)) {
	            devResults.setAllTestsPassed(false);
	        }

	        // Map position and add result
	        MappedResult mapped = getMappedPosition(deviceName, pallets);
	        if (mapped.getMappedPosition() != -1) {
	            VerificationTestBay.logger.info("Adding result to Meter: " + deviceName);
	            palletTracker.addResultToMeter(
	                mapped.getMappedPosition(),
	                "P".equals(testStatus) ? "Pass" : "Fail",
	                errorValue,
	                getMyBayKey(),
	                mapped.getSelectedPallet(),
	                testType,
	                testCaseName
	            );
	        }
	    }

	    return deviceResults;
	}

	  *//**
	  * Processes summary results for each device
	  *//*
	private void processTestSummaries(Map<Integer, DeviceTestResults> deviceResults, 
	        List<PalletManage> pallets, PalletTrackerController palletTracker) {
	    for (Map.Entry<Integer, DeviceTestResults> entry : deviceResults.entrySet()) {
	        int deviceName = entry.getKey();
	        boolean allPass = entry.getValue().isAllTestsPassed();

	        MappedResult mapped = getMappedPosition(deviceName, pallets);
	        if (mapped.getMappedPosition() != -1) {
	            VerificationTestBay.logger.info("Adding summary for Meter: " + mapped.getMappedPosition());
	            palletTracker.addMeterResultSummary(
	                mapped.getMappedPosition(),
	                allPass ? "Pass" : "Fail",
	                allPass ? "Pass" : "Fail",
	                ConstantConveyor.VERIFICATION_RESULT_TEST_NAME,
	                ConstantConveyor.VERIFICATION_RESULT_KEY,
	                mapped.getSelectedPallet()
	            );
	        }
	    }
	}

	   *//**
	   * Determines test type based on test case name
	   *//*
	private String determineTestType(String testCaseName) {
	    return testCaseName.startsWith("LOE") ? ConstantConveyor.VERIFICATION_RESULT_KEY : "";
	}

	    *//**
	    * Validates required JSON fields
	    *//*
	private boolean validateResult(JSONObject result) {
	    if (result == null || !result.has("device_name") || !result.has("test_status") 
	            || !result.has("test_case_name")) {
	        VerificationTestBay.logger.error("Missing required JSON fields");
	        return false;
	    }
	    return true;
	}

	     *//**
	     * Maps device positions to pallet positions
	     *//*
	private MappedResult getMappedPosition(int positionNo, List<PalletManage> pallets) {
	    if (pallets.size() < MINIMUM_PALLET_COUNT) {
	        VerificationTestBay.logger.warn("Insufficient pallets: " + pallets.size());
	        return new MappedResult(-1, null);
	    }

	    int mappedPosition = -1;
	    PalletManage selectedPallet = null;

	    if (ConstantConveyor.VERIFIC_PALLET1_POSITIONS.contains(positionNo)) {
	        selectedPallet = pallets.get(0);
	        mappedPosition = mapPallet1Position(positionNo);
	    } else if (ConstantConveyor.VERIFIC_PALLET2_POSITIONS.contains(positionNo)) {
	        selectedPallet = pallets.get(1);
	        mappedPosition = mapPallet2Position(positionNo);
	    } else if (ConstantConveyor.VERIFIC_PALLET3_POSITIONS.contains(positionNo)) {
	        selectedPallet = pallets.get(2);
	        mappedPosition = mapPallet3Position(positionNo);
	    } else if (ConstantConveyor.VERIFIC_PALLET4_POSITIONS.contains(positionNo)) {
	        selectedPallet = pallets.get(3);
	        mappedPosition = positionNo - 9;
	    }

	    VerificationTestBay.logger.info("Mapping Position " + positionNo + " -> " + mappedPosition + 
	        " | Pallet: " + (selectedPallet != null ? selectedPallet.toString() : "None"));

	    return new MappedResult(mappedPosition, selectedPallet);
	}

	// Helper methods for position mapping
	private int mapPallet1Position(int pos) {
	    return switch (pos) {
	        case 22 -> 4;
	        case 23 -> 5;
	        case 24 -> 6;
	        default -> pos;
	    };
	}

	private int mapPallet2Position(int pos) {
	    return switch (pos) {
	        case 19 -> 4;
	        case 20 -> 5;
	        case 21 -> 6;
	        default -> pos - 3;
	    };
	}

	private int mapPallet3Position(int pos) {
	    return switch (pos) {
	        case 16 -> 4;
	        case 17 -> 5;
	        case 18 -> 6;
	        default -> pos - 6;
	    };
	}

	      *//**
	      * Helper class to track device test results
	      *//*
	private static class DeviceTestResults {
	    private boolean allTestsPassed;

	    DeviceTestResults(boolean initialStatus) {
	        this.allTestsPassed = initialStatus;
	    }

	    boolean isAllTestsPassed() { return allTestsPassed; }
	    void setAllTestsPassed(boolean status) { this.allTestsPassed = status; }
	}*/


	//============================================================================================================================================  

	//============================================================================================================================================  

	private void addTestPointMeterResults(String myProcalRemoteResult) {
		Verification.logger.info("S043_Get_Test_Point_Immediately : addTestPointMeterResults : Entry ");

		try {
			/*ProCalTestResultsResponse procalResult = JsonPath
			    .from(myProcalRemoteResult)
			    .getObject("", ProCalTestResultsResponse.class);*/
			
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

			ProCalTestResultsResponse procalResult = mapper.readValue(myProcalRemoteResult, ProCalTestResultsResponse.class);
			
			for(Result eachResult: procalResult.getResults()) {
				Verification.logger.info("S043_Get_Test_Point_Immediately : eachResult :" 
						+ ", Pallet:" + eachResult.getPallet_distinct_id()
						+ ", serialNo: " + eachResult.getDut_serial_no()
						+ ", lduPosition: " + eachResult.getDevice_name()
						+ ", palletPosition: " + eachResult.getPallet_rack_position_no()
						+ ", TestCase: " + eachResult.getTest_case_name()
						+ ", ErrorValue: <" + eachResult.getError_value()
						+ ">, ErrorStatus: <" + eachResult.getTest_status()
						+ ">, getRun_id: " + eachResult.getRun_id()
						);
			}
		
		}catch(Exception e) {
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

			/*// Second pass: Process test results
	        Set<String> processedTestCases = new HashSet<>(); // Track processed test cases
	        for (int i = 0; i < results.length(); i++) {
	            JSONObject result = results.getJSONObject(i);
	            int positionNo = result.getInt("device_name");
	            Integer pos = positionNo; 
	            String resultValue = result.getString("error_value");
	            String resultStatus = result.getString("test_status").equals("P") ? "Pass" : "Fail";
	            String testCaseName = result.getString("test_case_name");
	            String testType = testCaseName.startsWith("LOE") ? ConstantConveyor.VERIFICATION_RESULT_KEY "LOE" : "";

	            PalletManage selectedPallet = null;
	            MappedResult mappedResult = getMappedPosition(positionNo, pos, myPalletManageList);
	            int mappedPosition = mappedResult.getMappedPosition();
	            PalletManage selectedPallet = mappedResult.getSelectedPallet();

	            if (mappedPosition != -1) {
	                VerificationTestBay.logger.info("S043_Get_Test_Point_Status : addTestPointMeterResults : Adding result to Meter : " + positionNo);
	                palletTracker.addResultToMeter(mappedPosition, resultStatus, resultValue, getMyBayKey(), selectedPallet, testType, testCaseName);
	            } else {
	                VerificationTestBay.logger.warn("S043_Get_Test_Point_Status : addTestPointMeterResults : No matching pallet found for position: " + positionNo);
	            }
	        }*/

			// Second pass: Process test results with deduplication
			Set<String> processedTestCases = new HashSet<>(); // Track processed test cases
			for (int verificDataCount = 0; verificDataCount < results.length(); verificDataCount++) {
				JSONObject result = results.getJSONObject(verificDataCount);
				int positionNo = result.getInt("device_name");
				String testCaseName = result.getString("test_case_name");
				String uniqueKey = positionNo + "_" + testCaseName; // Unique identifier for device and test case

				// Skip if this test case for this device was already processed
				if (!processedTestCases.add(uniqueKey)) {
					Verification.logger.warn("S043_Get_Test_Point_Immediately : addTestPointMeterResults : Skipping duplicate test case: " + uniqueKey);
					continue; // Skip to next iteration
				}

				// Original second pass logic continues here
				Integer pos = positionNo; 
				String resultValue = result.getString("error_value");
				String error_min = result.getString("error_min");
				String error_max = result.getString("error_max");
				String resultStatus = result.getString("test_status").equals("P") ? "Pass" : "Fail";
				String testType = testCaseName.startsWith(ConstantConveyor.ACCURACY_ALIAS_NAME) ? ConstantConveyor.VERIFICATION_RESULT_KEY : "";

				MappedResult mappedResult = getMappedPosition(positionNo, pos, myPalletManageList);
				int mappedPosition = mappedResult.getMappedPosition();
				PalletManage selectedPallet = mappedResult.getSelectedPallet();

				if (mappedPosition != -1) {
					Verification.logger.info("S043_Get_Test_Point_Immediately : addTestPointMeterResults : Adding result to Meter : " + positionNo);
					Verification.logger.info("S043_Get_Test_Point_Immediately : addTestPointMeterResults : " + positionNo + " : Result Value : " + resultValue);
					palletTracker.addResultToPalletMeterV2(mappedPosition, resultStatus, resultValue,error_min, error_max, getMyBayKey(), selectedPallet, testType, testCaseName);
				} else {
					Verification.logger.warn("S043_Get_Test_Point_Immediately : addTestPointMeterResults : No matching pallet found for position: " + positionNo);
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

				String testType = ConstantConveyor.VERIFICATION_RESULT_KEY; //"LOE";
				String testCaseName = ConstantConveyor.SUMMARY_VERIFICATION_RESULT_TEST_NAME; //"Verification";

				MappedResult mappedResult = getMappedPosition(positionNo, pos, myPalletManageList);
				int mappedPosition = mappedResult.getMappedPosition();
				PalletManage selectedPallet = mappedResult.getSelectedPallet();

				if (mappedPosition != -1) {
					Verification.logger.info("S043_Get_Test_Point_Immediately : addTestPointMeterResults : Adding summary for Meter : " + mappedPosition);
					palletTracker.addMeterResultSummaryWithPalletDetails(mappedPosition, finalResultStatus, finalResultValue, testCaseName,  testType, selectedPallet);
					//palletTracker.addMeterResultSummary(mappedPosition, finalResultStatus, finalResultValue, "Functional Test", "FT", selectedPallet);
				} else {
					Verification.logger.warn("S043_Get_Test_Point_Immediately : addTestPointMeterResults : No matching pallet found for position: " + mappedPosition);
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

		/*if (myPalletManageList.size() >= 4) { // Ensure there are at least 4 pallets
			if (ConstantConveyor.VERIFIC_PALLET1_POSITIONS.contains(pos)) {
				selectedPallet = myPalletManageList.get(3);
				mappedPosition = (positionNo == 22) ? 4 : (positionNo == 23) ? 5 : (positionNo == 24) ? 6 : positionNo;
			} else if (ConstantConveyor.VERIFIC_PALLET2_POSITIONS.contains(pos)) {
				selectedPallet = myPalletManageList.get(2);
				mappedPosition = (positionNo == 19) ? 4 : (positionNo == 20) ? 5 : (positionNo == 21) ? 6 : positionNo - 3;
			} else if (ConstantConveyor.VERIFIC_PALLET3_POSITIONS.contains(pos)) {
				selectedPallet = myPalletManageList.get(1);
				mappedPosition = (positionNo == 16) ? 4 : (positionNo == 17) ? 5 : (positionNo == 18) ? 6 : positionNo - 6;
			} else if (ConstantConveyor.VERIFIC_PALLET4_POSITIONS.contains(pos)) {
				selectedPallet = myPalletManageList.get(0);
				mappedPosition = positionNo - 9;
			}
		} else {
			VerificationTestBay.logger.warn("getMappedPosition: Not enough pallets in myPalletManageList! Size: " + myPalletManageList.size());
		}*/

		// Linear Mapping
		if (myPalletManageList.size() >= 4) {
/*			if (ConstantConveyor.VERIFIC_PALLET1_POSITIONS.contains(pos)) {
				selectedPallet = myPalletManageList.get(0); // 4th pallet
				mappedPosition = positionNo; // 1–6 -> already 1–6
			} else if (ConstantConveyor.VERIFIC_PALLET2_POSITIONS.contains(pos)) {
				selectedPallet = myPalletManageList.get(1); // 3rd pallet
				mappedPosition = positionNo - 6; // 7–12 -> 1–6
			} else if (ConstantConveyor.VERIFIC_PALLET3_POSITIONS.contains(pos)) {
				selectedPallet = myPalletManageList.get(2); // 2nd pallet
				mappedPosition = positionNo - 12; // 13–18 -> 1–6
			} else if (ConstantConveyor.VERIFIC_PALLET4_POSITIONS.contains(pos)) {
				selectedPallet = myPalletManageList.get(3); // 1st pallet
				mappedPosition = positionNo - 18; // 19–24 -> 1–6
			}*/
			
			
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
		}
		else {
			Verification.logger.warn("getMappedPosition: Not enough pallets in myPalletManageList! Size: " + myPalletManageList.size());
		}

		Verification.logger.info("Mapping Position " + positionNo + " -> Mapped to " + mappedPosition + " | Assigned to Pallet: " + (selectedPallet != null ? selectedPallet.toString() : "None"));

		return new MappedResult(mappedPosition, selectedPallet);
	}


	/*private void addTestPointMeterResults(String myProcalRemoteResult) {
		VerificationTestBay.logger.info("S043_Get_Test_Point_Status : addTestPointMeterResults : Entry ");

		PalletTrackerController palletTracker = new PalletTrackerController();

		try {
			JSONObject jsonObject = new JSONObject(myProcalRemoteResult);
			JSONArray results = jsonObject.getJSONArray("Results");

			presentBayKey = ConstantConveyor.VERIFICATION_BAY_KEY; // STNLD1B

			// Fetch the list of pallets
			List<PalletManage> myPalletManageList = MySqlServiceManager.getPalletManageService().findByPresentBayKeyAndPalletActive(presentBayKey);

			VerificationTestBay.logger.info("S043_Get_Test_Point_Status : myPalletManageList : " + myPalletManageList);

			Map<Integer, Boolean> devicePassStatus = new HashMap<>();

			// First pass: Determine if all test cases for a device_name are "P"
			for (int i = 0; i < results.length(); i++) {
			    JSONObject result = results.getJSONObject(i);
			    int deviceName = result.getInt("device_name");
			    String testStatus = result.getString("test_status");

			    // If device is not in the map, assume it passes
			    devicePassStatus.putIfAbsent(deviceName, true);

			    // If any test case is "F", mark the device as failed
			    if (testStatus.equals("F")) {
			        devicePassStatus.put(deviceName, false);
			    }
			}

			// Second pass: Process results with updated error_value logic
			for (int i = 0; i < results.length(); i++) {
			    JSONObject result = results.getJSONObject(i);
			    int positionNo = result.getInt("device_name");
			    Integer pos = positionNo; 
			    String resultValue = result.getString("error_value");
			    String resultStatus = result.getString("test_status").equals("P") ? "Pass" : "Fail";
			    String testCaseName = result.getString("test_case_name");
			    String testType = testCaseName.startsWith("LOE") ? "LOE" : "";

			    // If all tests for a device passed, set error_value to "Pass"


			    PalletManage selectedPallet = null;
			    int mappedPosition = positionNo;

			    if (ConstantConveyor.VERIFIC_PALLET1_POSITIONS.contains(pos)) {
			        selectedPallet = myPalletManageList.get(0);
			        mappedPosition = (positionNo == 22) ? 4 : (positionNo == 23) ? 5 : (positionNo == 24) ? 6 : positionNo;
			    } else if (ConstantConveyor.VERIFIC_PALLET2_POSITIONS.contains(pos)) {
			        selectedPallet = myPalletManageList.get(1);
			        mappedPosition = (positionNo == 19) ? 4 : (positionNo == 20) ? 5 : (positionNo == 21) ? 6 : positionNo - 3;
			    } else if (ConstantConveyor.VERIFIC_PALLET3_POSITIONS.contains(pos)) {
			        selectedPallet = myPalletManageList.get(2);
			        mappedPosition = (positionNo == 16) ? 4 : (positionNo == 17) ? 5 : (positionNo == 18) ? 6 : positionNo - 6;
			    } else if (ConstantConveyor.VERIFIC_PALLET4_POSITIONS.contains(pos)) {
			        selectedPallet = myPalletManageList.get(3);
			        mappedPosition = positionNo - 9;
			    }

			    if (selectedPallet != null) {
			    	VerificationTestBay.logger.info("S043_Get_Test_Point_Status : addTestPointMeterResults : Adding result to Meter : " + positionNo);
			        palletTracker.addResultToMeter(mappedPosition, resultStatus, resultValue, getMyBayKey(), testType, testCaseName);
			    } else {
			    	VerificationTestBay.logger.warn("S043_Get_Test_Point_Status : addTestPointMeterResults : No matching pallet found for position: " + positionNo);
			    }

			    if (devicePassStatus.get(positionNo)) {
			        resultValue = "Pass";
			        resultStatus = "Pass";
			    } else {
			    	resultValue = "Fail";
			    	resultStatus = "Fail";
			    }

			    if (selectedPallet != null) {
			    	VerificationTestBay.logger.info("S043_Get_Test_Point_Status : addTestPointMeterResults : Adding result to Meter : " + positionNo);
			        palletTracker.addMeterResultSummary(mappedPosition, resultStatus, resultValue, testCaseName, testType, selectedPallet);
			    } else {
			    	VerificationTestBay.logger.warn("S043_Get_Test_Point_Status : addTestPointMeterResults : No matching pallet found for position: " + positionNo);
			    }
			}




		} catch (org.json.JSONException e) {
			VerificationTestBay.logger.error("Error parsing JSON: " + e.getMessage(), e);
		}

		VerificationTestBay.logger.info("S043_Get_Test_Point_Status : addTestPointMeterResults : Exit ");
	}*/

	// Helper method to format test case names
	private String formatTestCaseName(String testCaseName) {
		if (testCaseName.contains("0.5L")) {
			return "1.0Ib_0.5L";
		} else if (testCaseName.contains("1.0-1.0Ib")) {
			return "1.0ib";
		} else {
			return testCaseName; // Default case if no match
		}
	}

	/*private void addTestPointMeterResults(String myProcalRemoteResult) {
		VerificationTestBay.logger.info("S043_Get_Test_Point_Status : addTestPointMeterResults : Entry ");

		PalletTrackerController palletTracker = new PalletTrackerController();

		try {
			JSONObject jsonObject = new JSONObject(myProcalRemoteResult);
			JSONArray results = jsonObject.getJSONArray("Results");

			presentBayKey = ConstantConveyor.VERIFICATION_BAY_KEY; // STNLD1B

			// Fetch the list of pallets
			List<PalletManage> myPalletManageList = MySqlServiceManager.getPalletManageService().findByPresentBayKeyAndPalletActive(presentBayKey);

			VerificationTestBay.logger.info("S043_Get_Test_Point_Status : myPalletManageList : " + myPalletManageList);

			PalletManage selectedPallet = null;
		        selectedPallet = myPalletManageList.get(0);

		        VerificationTestBay.logger.info("S043_Get_Test_Point_Status : selectedPallet : 0 " + selectedPallet);

			for (int i = 0; i < results.length(); i++) {
				JSONObject result = results.getJSONObject(i);

				int positionNo = result.getInt("device_name");
				Integer pos = positionNo; // Explicitly convert to Integer
				String resultValue = result.getString("error_value");
				String resultStatus = result.getString("test_status").equals("P") ? "Pass" : "Fail";
				String testCaseName = result.getString("test_case_name"); //testCaseName = result.getString("test_case_name");
				//String formatedTestCaseName = formatTestCaseName(testCaseName);
				String testType = testCaseName.startsWith("LOE") ? "LOE" : "";

				// Determine the correct pallet based on position number
				PalletManage selectedPallet = null;
				int mappedPosition = positionNo;

				if (ConstantConveyor.VERIFIC_PALLET1_POSITIONS.contains(pos)) { //Arrays.asList(new Integer[]{1, 2, 3, 22, 23, 24})
					selectedPallet = myPalletManageList.get(0);
					mappedPosition = (positionNo == 22) ? 4 : (positionNo == 23) ? 5 : (positionNo == 24) ? 6 : positionNo;
				} else if (ConstantConveyor.VERIFIC_PALLET2_POSITIONS.contains(pos)) {
					selectedPallet = myPalletManageList.get(1);
					mappedPosition = (positionNo == 19) ? 4 : (positionNo == 20) ? 5 : (positionNo == 21) ? 6 : positionNo - 3;
				} else if (ConstantConveyor.VERIFIC_PALLET3_POSITIONS.contains(pos)) {
					selectedPallet = myPalletManageList.get(2);
					mappedPosition = (positionNo == 16) ? 4 : (positionNo == 17) ? 5 : (positionNo == 18) ? 6 : positionNo - 6;
				} else if (ConstantConveyor.VERIFIC_PALLET4_POSITIONS.contains(pos)) {
					selectedPallet = myPalletManageList.get(3);
					mappedPosition = positionNo - 9;
				}

				if (selectedPallet != null) {
					VerificationTestBay.logger.info("S043_Get_Test_Point_Status : addTestPointMeterResults : Adding result to Meter : " + positionNo);
					palletTracker.addMeterResultOnClick(mappedPosition, resultStatus, resultValue, testCaseName, testType, selectedPallet);
				} else {
					VerificationTestBay.logger.warn("S043_Get_Test_Point_Status : addTestPointMeterResults : No matching pallet found for position: " + positionNo);
				}
			}

		} catch (org.json.JSONException e) {
			VerificationTestBay.logger.error("Error parsing JSON: " + e.getMessage(), e);
		}

		VerificationTestBay.logger.info("S043_Get_Test_Point_Status : addTestPointMeterResults : Exit ");
	}

	// Helper method to format test case names
	private String formatTestCaseName(String testCaseName) {
		if (testCaseName.contains("0.5L")) {
			return "1.0Ib_0.5L";
		} else if (testCaseName.contains("1.0-1.0Ib")) {
			return "1.0ib";
		} else {
			return testCaseName; // Default case if no match
		}
	}*/

	//============================================================================================================================================  

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

/*	public String getMyBayKey() {
		return myBayKey;
	}*/

}
