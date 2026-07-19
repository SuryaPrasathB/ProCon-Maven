package com.tasnetwork.calibration.conveyor.bay.verific;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.pallet.PalletTrackerController;
import com.tasnetwork.calibration.conveyor.remote.ProCalTestResultsResponse;
import com.tasnetwork.calibration.conveyor.remote.ProcalRemoteResponse;
import com.tasnetwork.calibration.conveyor.remote.ProcalRemoteSender;
import com.tasnetwork.calibration.conveyor.remote.Result;
import com.tasnetwork.calibration.conveyor.remote.TestPointStatus;
import com.tasnetwork.calibration.conveyor.remote.TestResult;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.WindowManager;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.util.YesNoDialogFX;
import com.tasnetwork.spring.orm.model.PalletManage;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

// JavaFX Imports for UI Alert
import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;

public class S043_Get_Test_Point_Status implements VerificTestBayState {

	// --- Simulation Flags ---
	// Set this to "NONE", "CIRCUIT_OPEN", or "CONTIGUOUS_BATCH" for simulation
	// testing
	private static final String SIMULATION_MODE = "NONE"; // Set to "NONE" for normal operation
	private static final String MODE_NONE = "NONE";
	private static final String MODE_CIRCUIT_OPEN = "CIRCUIT_OPEN";
	private static final String MODE_CONTIGUOUS_BATCH = "CONTIGUOUS_BATCH";

	private boolean userAbortedPalletRelease = false;
	private final boolean PROCESS_RESULT_WITH_DUT_SERIAL_NO_MAPPING_ENABLED = true;
	private int contiguousFaultCounter = 0; // Counter for contiguous batch simulation
	private int simulationLoopCounter = 0; // Counter for total simulation loops to control completion

	BayUtils bayUtils = new BayUtils();
	String presentBayKey = "";
	String sequencePathId = "p1";

	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	private static class MockProcalRemoteResponse extends ProcalRemoteResponse {
		private TestPointStatus internalSimulatedStatus;

		// Constructor for our mock, taking the TestPointStatus to return
		public MockProcalRemoteResponse(TestPointStatus statusToReturn) {

			super();
			this.internalSimulatedStatus = statusToReturn;
		}

		@Override
		public TestPointStatus getTestPointStatus() {
			return internalSimulatedStatus; // Return our controlled simulated status
		}

	}

	// ===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Verification.logger.info("S043_Get_Test_Point_Status : Entry");
		Verification.logger.info("S043_Get_Test_Point_Status : Simulation Mode: " + SIMULATION_MODE);

		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true); // Default to true, will be set to false on failure
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601); // Default success error code

		ProcalRemoteSender procalRemoteSender = new ProcalRemoteSender();

		String ip_address = ConstantConveyorConfig.CONVEYOR_PROCAL_VERIFIC_IP_ADDRESS;
		String ip_port = ConstantConveyorConfig.CONVEYOR_PROCAL_VERIFIC_IP_PORT;
		String endpoint = ConstantConveyorConfig.CONVEYOR_PROCAL_VERIFIC_END_PATH;

		ClusterServer verificClusterServer = new ClusterServer(ip_address, ip_port);

		ProcalRemoteResponse myProcalRemoteResponse; // This will be the actual or mock response

		// Flag to track if the loop exited due to current circuit open detected
		boolean batchFailedRelayOpenOneTime = false;
		boolean batchFailedRelayOpenContiguous = false;
		// --- Initializing myProcalRemoteResponse before the loop ---
		if (!SIMULATION_MODE.equals(MODE_NONE)) {
			// Create a dummy TestPointStatus for initial mock response
			TestPointStatus initialSimulatedStatus = new TestPointStatus();
			initialSimulatedStatus.setAllTestExecutionCompleted(false); // Assume not completed initially
			initialSimulatedStatus.setCurrentCircuitOpenDetectedInBatch(SIMULATION_MODE.equals(MODE_CIRCUIT_OPEN)); // Set
																													// based
																													// on
																													// mode
			initialSimulatedStatus.setFaultDetectedInContiguousBatch(false); // Reset for initial

			myProcalRemoteResponse = new MockProcalRemoteResponse(initialSimulatedStatus);
			Verification.logger
					.info("S043_Get_Test_Point_Status : Initializing myProcalRemoteResponse in simulation mode.");
		} else {
			// In normal mode, get the actual response from Procal
			myProcalRemoteResponse = procalRemoteSender.sendCommResultRefreshToProcal(verificClusterServer, endpoint);
			Verification.logger.info("S043_Get_Test_Point_Status : getPresentTestPointId-1 : "
					+ myProcalRemoteResponse.getTestPointStatus().getPresentTestPointId());
			Verification.logger.info("S043_Get_Test_Point_Status : getCompletedTestPointCount-1 : "
					+ myProcalRemoteResponse.getTestPointStatus().getCompletedTestPointCount());
			Verification.logger.info("S043_Get_Test_Point_Status : getTotalTestPointCount-1 : "
					+ myProcalRemoteResponse.getTestPointStatus().getTotalTestPointCount());
			Verification.logger.info("S043_Get_Test_Point_Status : getTotalTestingTimeInSec-1 : "
					+ myProcalRemoteResponse.getTestPointStatus().getTotalTestingTimeInSec());

		}

		String message = "Current circuit open detected";
		int delayCount = 10;
		// Loop until all tests completed, stop process requested, or a critical batch
		// failure is detected
		while (!(myProcalRemoteResponse.getTestPointStatus().isAllTestExecutionCompleted()) &&
				(!Verification.isStopProcessRequestedVerificBay()) &&
				(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {

			Verification.logger.info("S043_Get_Test_Point_Status : Getting test point status...");
			// BayUtils.delay(10000); // Wait for 10 seconds before polling again
			delayCount = 10;
			while ((delayCount > 0) && (!Verification.isStopProcessRequestedVerificBay())) {
				delayCount--;
				BayUtils.delay(1000);
			}
			if (Verification.isStopProcessRequestedVerificBay()) {
				Verification.logger.info("S043_Get_Test_Point_Status : verific1 stop requested: break");
				break;
			}

			// --- Generate/Update myProcalRemoteResponse for current loop iteration ---
			if (!SIMULATION_MODE.equals(MODE_NONE)) {
				simulationLoopCounter++; // Increment simulation loop counter

				TestPointStatus simulatedStatus = new TestPointStatus();
				simulatedStatus.setCurrentCircuitOpenDetectedInBatch(false); // Reset for current iteration
				simulatedStatus.setFaultDetectedInContiguousBatch(false); // Reset for current iteration

				// Simulate completion after a certain number of loops
				if (simulationLoopCounter >= 3 && SIMULATION_MODE.equals(MODE_NONE)) { // Only simulate completion in
																						// NONE for real scenario
					simulatedStatus.setAllTestExecutionCompleted(true);
					Verification.logger
							.info("S043_Get_Test_Point_Status : Simulating all test execution completed (Loop: "
									+ simulationLoopCounter + ").");
				} else if (simulationLoopCounter >= 3 && !SIMULATION_MODE.equals(MODE_NONE)) { // for simulation, make
																								// it complete to exit
																								// loop
					simulatedStatus.setAllTestExecutionCompleted(true);
					Verification.logger.info(
							"S043_Get_Test_Point_Status : Simulating all test execution completed for simulation mode (Loop: "
									+ simulationLoopCounter + ").");
				} else {
					simulatedStatus.setAllTestExecutionCompleted(false); // Keep loop running
				}

				if (SIMULATION_MODE.equals(MODE_CIRCUIT_OPEN)) {
					// This case should ideally be handled by the initial check, but ensures it's
					// set
					simulatedStatus.setCurrentCircuitOpenDetectedInBatch(true);
					Verification.logger
							.info("S043_Get_Test_Point_Status : Simulating CIRCUIT_OPEN detection inside loop.");
				} else if (SIMULATION_MODE.equals(MODE_CONTIGUOUS_BATCH)) {
					contiguousFaultCounter++;
					if (contiguousFaultCounter >= 2) { // Simulate after 2 polling attempts
						simulatedStatus.setFaultDetectedInContiguousBatch(true);
						Verification.logger.info(
								"S043_Get_Test_Point_Status : Simulating CONTIGUOUS_BATCH fault detection (Count: "
										+ contiguousFaultCounter + ").");
					}
				}
				myProcalRemoteResponse = new MockProcalRemoteResponse(simulatedStatus); // Update mock response for this
																						// iteration
			} else {
				// Normal operation: send actual request to Procal
				myProcalRemoteResponse = procalRemoteSender.sendCommResultRefreshToProcal(verificClusterServer,
						endpoint);
			}

			// Check for current circuit open detected in batch - this is a critical failure
			// that stops the process
			if (myProcalRemoteResponse.getTestPointStatus().isCurrentCircuitOpenDetectedInBatch()) {
				Verification.logger.error(
						"S043_Get_Test_Point_Status : Current circuit open detected in batch. Setting batchFailedRelayOpen flag and exiting loop.");
				batchFailedRelayOpenOneTime = true;
				message = "Current circuit open detected";
				releasePalletToNextBayForBatchFailed(message);

				break; // Exit the while loop
			}

			// Check for contiguous batch fault - this prompts the user with a UI alert and
			// waits for interaction
			if (myProcalRemoteResponse.getTestPointStatus().isFaultDetectedInContiguousBatch()) {
				Verification.logger.warn(
						"S043_Get_Test_Point_Status : ALERT: Two batches continuous failure detected. Showing UI prompt.");
				batchFailedRelayOpenContiguous = true;

				/*
				 * boolean isPalletToBeReleasedToNextBay=
				 * releasePalletToNextBayForBatchFailed();
				 * 
				 * if(isPalletToBeReleasedToNextBay) {
				 * 
				 * }
				 */
				message = "Current circuit open detected in continous batch";
				releasePalletToNextBayForBatchFailed(message);

				// The loop continues regardless of start command success/failure

				break;
			}

			ArrayList<TestResult> presentTpResult;
			presentTpResult = myProcalRemoteResponse.getTestPointStatus().getPresentTpResult();
			Verification.logger.info("S043_Get_Test_Point_Status : getPresentTestPointId : "
					+ myProcalRemoteResponse.getTestPointStatus().getPresentTestPointId());
			Verification.logger.info("S043_Get_Test_Point_Status : getCompletedTestPointCount : "
					+ myProcalRemoteResponse.getTestPointStatus().getCompletedTestPointCount());
			Verification.logger.info("S043_Get_Test_Point_Status : getTotalTestPointCount : "
					+ myProcalRemoteResponse.getTestPointStatus().getTotalTestPointCount());
			Verification.logger.info("S043_Get_Test_Point_Status : getTotalTestingTimeInSec : "
					+ myProcalRemoteResponse.getTestPointStatus().getTotalTestingTimeInSec());
			Verification.logger.info("S043_Get_Test_Point_Status : presentTpResult : " + presentTpResult);
			// int totalTestPoint = presentTpResult.stream().filter(e->e.getPositionId() !=
			// ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID)
			// .map(mapper)

		}

		// After the loop, determine the final response based on why the loop exited
		if (batchFailedRelayOpenOneTime || batchFailedRelayOpenContiguous) {
			Verification.logger
					.info("S043_Get_Test_Point_Status : batchFailedRelayOpenOneTime: " + batchFailedRelayOpenOneTime);
			Verification.logger.info(
					"S043_Get_Test_Point_Status : batchFailedRelayOpenContiguous: " + batchFailedRelayOpenContiguous);

			// Call addTestPointMeterResults with the batch failure flag
			addTestPointMeterResults(null, true); // myProcalRemoteResult is not needed for this path
			/*
			 * boolean isPalletToBeReleased= releaseMeterForBatchFailed();
			 * 
			 * if(isPalletToBeReleased) {
			 * 
			 * }
			 */

			if (batchFailedRelayOpenContiguous) {
				Verification.logger.info(
						"S043_Get_Test_Point_Status : Execution Failed due to BATCH FAILED - Relay Open - multiple time");
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_031); // Using existing failure code
			} else if (batchFailedRelayOpenOneTime) {
				Verification.logger.info(
						"S043_Get_Test_Point_Status : Execution Failed due to BATCH FAILED - Relay Open - one time");
				bayResponse.setStatus(false);

				if (userAbortedPalletRelease) {
					bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_033); // Using existing failure
																							// code
					userAbortedPalletRelease = false;
				} else {
					bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_030); // Using existing failure
																							// code
				}
			}
		} else if (myProcalRemoteResponse.getTestPointStatus().isAllTestExecutionCompleted()) {
			Verification.logger.info("S043_Get_Test_Point_Status : Execution Success");

			// Get all results only if execution was successful
			String myProcalRemoteResult = "";
			boolean expectedResultRecieved = false;
			if (SIMULATION_MODE.equals(MODE_NONE)) {

				int retryCount = 3;
				// String myProcalRemoteResult = "";

				String expectedContent = "\"Results\":";
				while ((retryCount > 0) &&
						(!Verification.isStopProcessRequestedVerificBay()) &&
						(!expectedResultRecieved)) {
					myProcalRemoteResult = procalRemoteSender.sendCommAllResultToProcal(verificClusterServer, endpoint);
					retryCount--;
					if (myProcalRemoteResult.contains(expectedContent)) {
						if (myProcalRemoteResult.length() > 40) {
							Verification.logger.info("S043_Get_Test_Point_Status : Expected result found");
							expectedResultRecieved = true;
						} else {
							Verification.logger.info(
									"S043_Get_Test_Point_Status : length not sufficient: " + myProcalRemoteResult);
						}
					}
				}

			} else {
				// In simulation, return a dummy JSON array for all results, for testing
				// processing
				myProcalRemoteResult = "{\"Results\":[]}"; // You can populate this with dummy pass/fail results as
															// needed for testing
				Verification.logger
						.info("S043_Get_Test_Point_Status : Simulating empty JSON results for all results call.");
			}
			Verification.logger.info("S043_Get_Test_Point_Status : All results : " + myProcalRemoteResult);

			// Process and add the normal test results
			if (expectedResultRecieved) {
				if (PROCESS_RESULT_WITH_DUT_SERIAL_NO_MAPPING_ENABLED) {
					Verification.logger
							.info("S043_Get_Test_Point_Status : executing result processing with dutSerialNo");

					addTestPointMeterResults(myProcalRemoteResult, false);

				} else {
					Verification.logger
							.info("S043_Get_Test_Point_Status : executing result processing with lduPositionMapping");
					addTestPointMeterResultsWithLduPosition(myProcalRemoteResult, false);
				}

				bayResponse.setStatus(true);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

			} else {
				Verification.logger.info("S043_Get_Test_Point_Status : Expected result not found");
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_029);
			}
		} else {
			Verification.logger.info(
					"S043_Get_Test_Point_Status : Execution Failed (e.g., due to stop process request or ALL_LOOP_BREAK_FLAG being set)");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_026);
		}

		Verification.logger.info("S043_Get_Test_Point_Status : Exit");
		return bayResponse;
	}

	private boolean releasePalletToNextBayForBatchFailed(String message) {
		boolean releaseMeterForNextBay = false;
		Verification.logger.debug("releasePalletToNextBayForBatchFailed : message: " + message);
		if (ProconFeatureEnable.VERIFIC_BATCH_FAILED_USER_PROMPT_ENABLED) {

			ConveyorDataManager.setVerific1BatchFailedPromptUserInputReceived(false);
			ConveyorDataManager.setVerific1BatchFailedReleasePalletRequested(false);
			// Verification.logger.debug("releasePalletToNextBayForBatchFailed :
			// setCALIB_CURRENT_STABLE: false");
			Platform.runLater(() -> {
				String header = "Verification-1 Bay : " + message + "\n\nDo you want to release the pallet from bay?";
				String title = "Verific1 Bay";

				YesNoDialogFX dialog = new YesNoDialogFX(title, header, YesNoDialogFX.MessageType.WARNING);
				dialog.resultProperty().addListener((obs, oldVal, newVal) -> {
					if (Boolean.TRUE.equals(newVal)) {
						ConveyorDataManager.setVerific1BatchFailedReleasePalletRequested(true);
						Verification.logger
								.debug("releasePalletToNextBayForBatchFailed: release pallet prompt user hit: YES");
					} else {
						ConveyorDataManager.setVerific1BatchFailedReleasePalletRequested(false);
						Verification.logger
								.debug("releasePalletToNextBayForBatchFailed: release pallet prompt user hit: NO");
						ApplicationLauncher.logger
								.debug("releasePalletToNextBayForBatchFailed: ERROR_CODE_VERIFIC_032 :"
										+ ConvErrorCodeMapping.ERROR_CODE_VERIFIC_032_MSG + " : Prompted");
						WindowManager.InformUser(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_032_MSG,
								ConvErrorCodeMapping.ERROR_CODE_VERIFIC_032_MSG, AlertType.ERROR);
						userAbortedPalletRelease = true;
					}
					ConveyorDataManager.setVerific1BatchFailedPromptUserInputReceived(true);
					Verification.logger.debug(
							"releasePalletToNextBayForBatchFailed: setVerific1BatchFailedPromptUserInputReceived: true");
				});
				dialog.show(); // This will NOT block the JavaFX thread
				// Calib .logger.debug("Prompt shown, returning immediately");

			});

			// long startTime = System.currentTimeMillis();
			Verification.logger
					.debug("releasePalletToNextBayForBatchFailed : awaiting for user input for power stable: Entry");
			while (!ConveyorDataManager.isVerific1BatchFailedPromptUserInputReceived()

					&& !Verification.isStopProcessRequestedVerificBay()) {

				BayUtils.delay(1000);
			}

			if (Verification.isStopProcessRequestedVerificBay()) {
				releaseMeterForNextBay = false;
				Verification.logger
						.debug("releasePalletToNextBayForBatchFailed : stop requested: releaseMeterForNextBay: "
								+ releaseMeterForNextBay);
			} else {
				releaseMeterForNextBay = ConveyorDataManager.isVerific1BatchFailedReleasePalletRequested();
				Verification.logger.debug(
						"releasePalletToNextBayForBatchFailed :  releaseMeterForNextBay: " + releaseMeterForNextBay);

			}

			Verification.logger
					.debug("releasePalletToNextBayForBatchFailed : awaiting for user input for power stable: Exit");

		}

		return releaseMeterForNextBay;

	}

	/**
	 * This method adds test point meter results to the pallet tracker.
	 * It can handle two scenarios: normal processing of Procal results or
	 * a specific batch failure (relay open) where all positions are marked as
	 * failed.
	 *
	 * @param myProcalRemoteResult   The JSON string containing test results from
	 *                               Procal. Null if isBatchFailedRelayOpen is true.
	 * @param isBatchFailedRelayOpen A boolean flag indicating if the call is for a
	 *                               batch failure due to relay open.
	 */
	private void addTestPointMeterResults(String myProcalRemoteResult, boolean isBatchFailedRelayOpen) {
		Verification.logger.info("S043_Get_Test_Point_Status : addTestPointMeterResults : Entry ");

		PalletTrackerController palletTracker = new PalletTrackerController();

		try {
			presentBayKey = ConstantConveyor.VERIFICATION_BAY_KEY;
			// Fetch the list of pallets for the current bay
			List<PalletManage> myPalletManageList = MySqlServiceManager.getPalletManageService()
					.findByPresentBayKeyAndPalletActive(presentBayKey);

			if (myPalletManageList == null || myPalletManageList.isEmpty()) {
				Verification.logger.warn("S043_Get_Test_Point_Status : No active pallets found for bay key: "
						+ presentBayKey + ". Cannot add results.");
				// If no pallets are found, we cannot proceed with result addition, even for
				// batch fail.
				// This indicates a setup issue rather than a test result issue.
				return;
			}

			// Handle BATCH FAILED - Relay Open scenario: mark all 24 positions as failed
			if (isBatchFailedRelayOpen) {
				Verification.logger.info(
						"S043_Get_Test_Point_Status : Handling BATCH FAILED - Relay Open scenario for all 24 positions.");
				String batchFailMessage = ConstantConveyor.REASON_RESULT_BATCH_FAILED;// "BATCH FAILED - Relay Open in
																						// VERIFIC";
				String testType = ConstantConveyor.VERIFICATION_RESULT_KEY; // Using existing constant for test type
				String testCaseName = ConstantConveyor.SUMMARY_VERIFICATION_RESULT_TEST_NAME; // Using existing constant
																								// for summary test case
																								// name

				// Iterate through all 24 logical device positions (1 to 24)
				for (int devicePos = 1; devicePos <= 24; devicePos++) {
					// Map the device position to the correct pallet and its mapped position
					MappedResult mappedResult = getMappedPosition(devicePos, devicePos, myPalletManageList);
					int mappedPosition = mappedResult.getMappedPosition();
					PalletManage selectedPallet = mappedResult.getSelectedPallet();

					if (mappedPosition != -1 && selectedPallet != null) {
						Verification.logger.info("S043_Get_Test_Point_Status : Marking position " + devicePos
								+ " (mapped to " + mappedPosition + " on pallet " + selectedPallet.getPalletQrId()
								+ ") as BATCH FAILED.");

						// Add individual test point result for this failure
						palletTracker.addResultToPalletMeterV2(
								mappedPosition,
								"Fail", // Result status
								batchFailMessage, // Result value (error_value)
								batchFailMessage, // error_min
								batchFailMessage, // error_max
								getMyBayKey(),
								selectedPallet,
								testType,
								testCaseName + "_BatchFail" // Append to test case name for clarity
						);

						// Add summary result for the device
						palletTracker.addMeterResultSummaryWithPalletDetails(
								mappedPosition,
								"Fail", // Final result status
								batchFailMessage, // Final result value
								testCaseName, // Summary test case name
								testType, // Summary test type
								selectedPallet);
					} else {
						Verification.logger.warn("S043_Get_Test_Point_Status : Could not map position " + devicePos
								+ " for batch failure. Skipping result marking for this position.");
					}
				}
				return; // Exit the method as batch failure has been handled
			}

			// ==============================================================================
			// Normal test result processing (only if not a batch failed scenario)
			// ==============================================================================
			if (myProcalRemoteResult == null || myProcalRemoteResult.isEmpty()) {
				Verification.logger.warn(
						"S043_Get_Test_Point_Status : Normal processing: Received null or empty result string. Nothing to process.");
				return;
			}

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
					Verification.logger.info("S043_Get_Test_Point_Status : eachResult :"
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
				Verification.logger.error("S043_Get_Test_Point_Status procalResult : Exception: " + e.getMessage());

			}

			// JSONObject jsonObject = new JSONObject(myProcalRemoteResult);
			// JSONArray results = jsonObject.getJSONArray("Results");

			Verification.logger
					.info("S043_Get_Test_Point_Status : myPalletManageList : size:" + myPalletManageList.size());

			// Map to track if all tests for a given device passed
			Map<Integer, Boolean> devicePassStatus = new HashMap<>();

			// First pass: Determine overall pass/fail status for each device based on all
			// its test cases
			int lduPositionNumber = 0;
			String testStatus = "";
			Map<Integer, DeviceInfo> deviceInfoMap = new HashMap<>();
			for (Result eachResult : procalResult.getResults()) {
				try {
					// for (int i = 0; i < results.length(); i++) {
					// JSONObject result = results.getJSONObject(i);
					lduPositionNumber = Integer.parseInt(eachResult.getDevice_name());// result.getInt("device_name");
					testStatus = eachResult.getTest_status();// result.getString("test_status");

					// Store device info (serial, pallet ID) from any result
					if (!deviceInfoMap.containsKey(lduPositionNumber)) {
						DeviceInfo info = new DeviceInfo();
						info.dutSerialNo = eachResult.getDut_serial_no();
						info.palletDistinctId = eachResult.getPallet_distinct_id();
						info.palletRackPosition = eachResult.getPallet_rack_position_no();
						deviceInfoMap.put(lduPositionNumber, info);
					}
					// Assume the device passes initially, then set to false if any test fails

					devicePassStatus.putIfAbsent(lduPositionNumber, true);
					if (testStatus.contains("F") || testStatus.contains("N")) {
						devicePassStatus.put(lduPositionNumber, false);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Verification.logger
							.error("S043_Get_Test_Point_Status procalResult : Exception-for-1: " + e.getMessage());

				}
			}

			Verification.logger.info("S043_Get_Test_Point_Status : devicePassStatusMap: " + devicePassStatus);

			// ConstantReport.REPORT_POPULATE_FAIL
			// validate mandatory testpoint present if not add failed result

			List<String> mandatoryTestPointList = DeviceDataManagerController.getConveyorConfigParsedKey()
					.getReportPrintDetailedTestPointWhiteList();

			procalResult.getResults().stream()
					.collect(Collectors.groupingBy(r -> {
						try {
							return Integer.parseInt(r.getDevice_name());
						} catch (NumberFormatException e) {
							return -1; // Invalid device, will be filtered out
						}
					}));

			// Validate each device
			for (Map.Entry<Integer, Boolean> entry : devicePassStatus.entrySet()) {
				int lduPosition = entry.getKey();
				DeviceInfo deviceInfo = deviceInfoMap.get(lduPosition);
				/*
				 * List<Result> deviceResults = resultsByDevice.getOrDefault(lduPosition,
				 * Collections.emptyList());
				 * 
				 * // Get all test names for this device
				 * Set<String> deviceTestNames = deviceResults.stream()
				 * .map(Result::getTest_case_name)
				 * .collect(Collectors.toSet());
				 */

				// Get existing test names for this device
				Set<String> deviceTestNames = procalResult.getResults().stream()
						.filter(r -> String.valueOf(lduPosition).equals(r.getDevice_name()))
						.map(Result::getTest_case_name)
						.collect(Collectors.toSet());

				// Check against whitelist patterns
				for (String whiteListPattern : mandatoryTestPointList) {
					// Replace XX with regex pattern to match any 2 digits
					String regexPattern = whiteListPattern.replace("XX", "\\d{2}");

					// Check if any test matches this pattern
					boolean patternMatched = deviceTestNames.stream()
							.anyMatch(testName -> testName.matches(regexPattern));

					if (!patternMatched) {
						// Mark device as failed for missing this required test
						devicePassStatus.put(lduPosition, false);
						// Verification.logger.warn("Device at LDU position {} missing required test
						// pattern: {}",
						// lduPosition, whiteListPattern);

						// Optional: Add the missing test to results with "N" status
						Result missingTest = new Result();
						missingTest.setDevice_name(String.valueOf(lduPosition));
						missingTest.setTest_case_name(whiteListPattern.replace("XX", "00")); // Or specific number
						missingTest.setTest_status(ConstantReport.RESULT_STATUS_UNDEFINED); // Not present
						// missingTest.setDut_serial_no(deviceResults.get(index));
						missingTest.setDut_serial_no(deviceInfo.dutSerialNo);
						missingTest.setPallet_distinct_id(deviceInfo.palletDistinctId);
						missingTest.setPallet_rack_position_no(deviceInfo.palletRackPosition);
						// Add to results
						procalResult.getResults().add(missingTest);

						Verification.logger.debug("Added missing test for device lduPosition: " + lduPosition

								+ ", palletDistinctId: " + deviceInfo.palletDistinctId
								+ ", palletRackPosition: " + deviceInfo.palletRackPosition
								+ ", dutSerialNo: " + deviceInfo.dutSerialNo
								+ ", TestCase: " + whiteListPattern);

					}
				}
			}

			// Second pass: Process individual test results and add them to the pallet
			// tracker
			int lduPositionNo = 0;
			int palletRackpositionNo = 0;
			String resultValue = "";//
			String resultStatus = "";
			String testCaseName = "";
			String dutSerialNo = "";
			String testType = "";//
			String palletDistinctId = "";
			String uniqueKey = "";
			String error_min = "";
			String error_max = "";
			Set<String> processedTestCases = new HashSet<>(); // Track processed test cases to avoid duplicates
			// for (int verificDataCount = 0; verificDataCount < results.length();
			// verificDataCount++) {
			// for (int verificDataCount = 0; verificDataCount <
			// procalResult.getResults().size(); verificDataCount++) {
			for (Result eachResult : procalResult.getResults()) {
				// JSONObject result = results.getJSONObject(verificDataCount);
				try {

					lduPositionNo = Integer.parseInt(eachResult.getDevice_name());// result.getInt("device_name");

					if (eachResult.getPallet_rack_position_no().isEmpty()) {
						palletRackpositionNo = 0;
					} else {
						palletRackpositionNo = Integer.parseInt(eachResult.getPallet_rack_position_no());
					}
					// palletRackpositionNo =
					// Integer.parseInt(eachResult.getPallet_rack_position_no());
					palletDistinctId = eachResult.getPallet_distinct_id();
					testCaseName = eachResult.getTest_case_name();// result.getString("test_case_name");
					uniqueKey = lduPositionNo + "_" + testCaseName; // Unique identifier for device and test case
					dutSerialNo = eachResult.getDut_serial_no();
					// Skip if this test case for this device was already processed (to handle
					// potential duplicates in Procal response)
					if (!processedTestCases.add(uniqueKey)) {
						Verification.logger.warn(
								"S043_Get_Test_Point_Status : addTestPointMeterResults : Skipping duplicate test case: "
										+ uniqueKey);
						continue; // Skip to next iteration
					}

					// Integer pos = positionNo; // Use Integer for contains method
					resultValue = eachResult.getError_value();// result.getString("error_value");
					error_min = eachResult.getError_min();// result.getString("error_min");
					error_max = eachResult.getError_max();// result.getString("error_max");
					resultStatus = eachResult.getTest_status().equals("P") ? "Pass" : "Fail";// result.getString("test_status").equals("P")
																								// ? "Pass" : "Fail";
					// Determine test type based on constant configuration (e.g., "LOE" if starts
					// with "ACCURACY_ALIAS_NAME")
					testType = testCaseName.startsWith(ConstantConveyor.ACCURACY_ALIAS_NAME)
							? ConstantConveyor.VERIFICATION_RESULT_KEY
							: "";

					// MappedResult mappedResult = getMappedPosition(lduPositionNo, pos,
					// myPalletManageList);
					// int mappedPosition = mappedResult.getMappedPosition();
					// PalletManage selectedPallet = mappedResult.getSelectedPallet();
					if (palletRackpositionNo != 0) {
						// if (mappedPosition != -1 && selectedPallet != null) {
						// Verification.logger.info("S043_Get_Test_Point_Status :
						// addTestPointMeterResults : Adding result for Meter: " + lduPositionNo + "
						// (Mapped: " + palletRackpositionNo + ")");
						Verification.logger.info("S043_Get_Test_Point_Status : lduPositionNo : " + lduPositionNo +
								" , palletDistinctId: " + palletDistinctId +
								" , testType: " + testType +
								" , dutSerialNo: " + dutSerialNo +
								" , palletRackpositionNo: " + palletRackpositionNo +
								" , testCaseName: " + testCaseName +
								" ,Result Value : <" + resultValue +
								"> ,resultStatus : <" + resultStatus +
								"> , Error Min: " + error_min +
								", Error Max: " + error_max);
						// palletTracker.addResultToPalletMeterV2(mappedPosition, resultStatus,
						// resultValue, error_min, error_max, getMyBayKey(), selectedPallet, testType,
						// testCaseName);

						palletTracker.addResultToPalletMeterV1_1(palletRackpositionNo, dutSerialNo, resultStatus,
								resultValue, getMyBayKey(), palletDistinctId, testType, testCaseName, error_min,
								error_max);
					} else {
						Verification.logger.info("S043_Get_Test_Point_Status : palletRackpositionNo : skipping : "
								+ palletRackpositionNo);
					}

					/*
					 * } else {
					 * Verification.logger.
					 * warn("S043_Get_Test_Point_Status : addTestPointMeterResults : No matching pallet found for position: "
					 * + lduPositionNo + ". Skipping result addition.");
					 * }
					 */
				} catch (Exception e) {
					e.printStackTrace();
					Verification.logger
							.error("S043_Get_Test_Point_Status procalResult : Exception-for-2: " + e.getMessage());

				}
				// Verification.logger.debug("S043_Get_Test_Point_Status : verificDataCount : "
				// + verificDataCount);
			}

			Verification.logger.info("S043_Get_Test_Point_Status : processedTestCasesMap: " + processedTestCases);

			// Third pass: Update summary once per device based on overall pass/fail status

			for (Map.Entry<Integer, Boolean> entry : devicePassStatus.entrySet()) {
				try {
					int lduPositionNoKey = entry.getKey();
					// Integer pos = lduPositionNoKey;
					boolean allPass = entry.getValue();

					String finalResultValue = allPass ? "Pass" : "Fail";
					String finalResultStatus = allPass ? "Pass" : "Fail";

					testType = ConstantConveyor.VERIFICATION_RESULT_KEY;
					testCaseName = ConstantConveyor.SUMMARY_VERIFICATION_RESULT_TEST_NAME;

					/*
					 * MappedResult mappedResult = getMappedPosition(lduPositionNoKey, pos,
					 * myPalletManageList);
					 * int mappedPosition = mappedResult.getMappedPosition();
					 * PalletManage selectedPallet = mappedResult.getSelectedPallet();
					 */
					// int lduPositionNoKey
					Optional<Result> procalResultOpt = procalResult.getResults().stream()
							.filter(e -> e.getDevice_name().equals(String.valueOf(lduPositionNoKey)))
							.findFirst();

					if (procalResultOpt.isPresent()) {
						Result myProcalResult = procalResultOpt.get();
						palletDistinctId = myProcalResult.getPallet_distinct_id();
						palletRackpositionNo = Integer.parseInt(myProcalResult.getPallet_rack_position_no());
						dutSerialNo = myProcalResult.getDut_serial_no();
						Verification.logger.info("S043_Get_Test_Point_Status : lduPositionNoKey : " + lduPositionNoKey
								+ " -> dutSerialNo :" + dutSerialNo + " -> palletRackpositionNo : "
								+ palletRackpositionNo + " -> finalResultValue : <" + finalResultValue
								+ "> , finalResultValue : <" + finalResultValue + "> , palletDistinctId: "
								+ palletDistinctId);

						// Verification.logger.info("S043_Get_Test_Point_Status :
						// addTestPointMeterResults : Adding summary for Meter : " +
						// palletRackpositionNo + " (Pallet: " + palletDistinctId + ")");
						// palletTracker.addMeterResultSummaryWithPalletDetails(mappedPosition,
						// finalResultStatus, finalResultValue, testCaseName, testType, selectedPallet);
						palletTracker.addMeterResultSummaryWithPalletDetailsV2(palletRackpositionNo, dutSerialNo,
								finalResultStatus, finalResultValue, testCaseName, testType, palletDistinctId);

					} else {
						Verification.logger.warn(
								"S043_Get_Test_Point_Status : addTestPointMeterResults : No matching pallet found for lduPositionNo: "
										+ lduPositionNoKey + ". Skipping summary addition.");
					}

				} catch (Exception e) {
					e.printStackTrace();
					Verification.logger
							.error("S043_Get_Test_Point_Status procalResult : Exception-for-3: " + e.getMessage());

				}
			}

			/*
			 * } catch (org.json.JSONException e) {
			 * Verification.logger.error("Error parsing JSON in normal processing: " +
			 * e.getMessage(), e);
			 */
		} catch (Exception e) {
			Verification.logger.error(
					"S043_Get_Test_Point_Status: Exception: An unexpected error occurred in addTestPointMeterResults: "
							+ e.getMessage());
		}

		Verification.logger.info("S043_Get_Test_Point_Status : addTestPointMeterResults : Exit ");
	}

	// this function get result with mapped ldu position
	private void addTestPointMeterResultsWithLduPosition(String myProcalRemoteResult, boolean isBatchFailedRelayOpen) {
		Verification.logger.info("S043_Get_Test_Point_Status : addTestPointMeterResultsWithLduPosition : Entry ");

		PalletTrackerController palletTracker = new PalletTrackerController();

		try {
			presentBayKey = ConstantConveyor.VERIFICATION_BAY_KEY;
			// Fetch the list of pallets for the current bay
			List<PalletManage> myPalletManageList = MySqlServiceManager.getPalletManageService()
					.findByPresentBayKeyAndPalletActive(presentBayKey);

			if (myPalletManageList == null || myPalletManageList.isEmpty()) {
				Verification.logger.warn("S043_Get_Test_Point_Status : No active pallets found for bay key: "
						+ presentBayKey + ". Cannot add results.");
				// If no pallets are found, we cannot proceed with result addition, even for
				// batch fail.
				// This indicates a setup issue rather than a test result issue.
				return;
			}

			// Handle BATCH FAILED - Relay Open scenario: mark all 24 positions as failed
			if (isBatchFailedRelayOpen) {
				Verification.logger.info(
						"S043_Get_Test_Point_Status : Handling BATCH FAILED - Relay Open scenario for all 24 positions.");
				String batchFailMessage = ConstantConveyor.REASON_RESULT_BATCH_FAILED;// "BATCH FAILED - Relay Open in
																						// VERIFIC";
				String testType = ConstantConveyor.VERIFICATION_RESULT_KEY; // Using existing constant for test type
				String testCaseName = ConstantConveyor.SUMMARY_VERIFICATION_RESULT_TEST_NAME; // Using existing constant
																								// for summary test case
																								// name

				// Iterate through all 24 logical device positions (1 to 24)
				for (int devicePos = 1; devicePos <= 24; devicePos++) {
					// Map the device position to the correct pallet and its mapped position
					MappedResult mappedResult = getMappedPosition(devicePos, devicePos, myPalletManageList);
					int mappedPosition = mappedResult.getMappedPosition();
					PalletManage selectedPallet = mappedResult.getSelectedPallet();

					if (mappedPosition != -1 && selectedPallet != null) {
						Verification.logger.info("S043_Get_Test_Point_Status : Marking position " + devicePos
								+ " (mapped to " + mappedPosition + " on pallet " + selectedPallet.getPalletQrId()
								+ ") as BATCH FAILED.");

						// Add individual test point result for this failure
						palletTracker.addResultToPalletMeterV2(
								mappedPosition,
								"Fail", // Result status
								batchFailMessage, // Result value (error_value)
								batchFailMessage, // error_min
								batchFailMessage, // error_max
								getMyBayKey(),
								selectedPallet,
								testType,
								testCaseName + "_BatchFail" // Append to test case name for clarity
						);

						// Add summary result for the device
						palletTracker.addMeterResultSummaryWithPalletDetails(
								mappedPosition,
								"Fail", // Final result status
								batchFailMessage, // Final result value
								testCaseName, // Summary test case name
								testType, // Summary test type
								selectedPallet);
					} else {
						Verification.logger.warn("S043_Get_Test_Point_Status : Could not map position " + devicePos
								+ " for batch failure. Skipping result marking for this position.");
					}
				}
				return; // Exit the method as batch failure has been handled
			}

			// ==============================================================================
			// Normal test result processing (only if not a batch failed scenario)
			// ==============================================================================
			if (myProcalRemoteResult == null || myProcalRemoteResult.isEmpty()) {
				Verification.logger.warn(
						"S043_Get_Test_Point_Status : Normal processing: Received null or empty result string. Nothing to process.");
				return;
			}

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
					Verification.logger.info("S043_Get_Test_Point_Status : eachResult :"
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
				Verification.logger.error("S043_Get_Test_Point_Status procalResult : Exception: " + e.getMessage());

			}

			JSONObject jsonObject = new JSONObject(myProcalRemoteResult);
			JSONArray results = jsonObject.getJSONArray("Results");

			Verification.logger
					.info("S043_Get_Test_Point_Status : myPalletManageList : size:" + myPalletManageList.size());

			// Map to track if all tests for a given device passed
			Map<Integer, Boolean> devicePassStatus = new HashMap<>();

			// First pass: Determine overall pass/fail status for each device based on all
			// its test cases
			for (int i = 0; i < results.length(); i++) {
				JSONObject result = results.getJSONObject(i);
				int deviceName = result.getInt("device_name");
				String testStatus = result.getString("test_status");

				// Assume the device passes initially, then set to false if any test fails
				devicePassStatus.putIfAbsent(deviceName, true);
				if (testStatus.contains("F") || testStatus.contains("N")) {
					devicePassStatus.put(deviceName, false);
				}
			}

			// Second pass: Process individual test results and add them to the pallet
			// tracker
			Set<String> processedTestCases = new HashSet<>(); // Track processed test cases to avoid duplicates
			for (int verificDataCount = 0; verificDataCount < results.length(); verificDataCount++) {
				JSONObject result = results.getJSONObject(verificDataCount);
				int positionNo = result.getInt("device_name");
				String testCaseName = result.getString("test_case_name");
				String uniqueKey = positionNo + "_" + testCaseName; // Unique identifier for device and test case

				// Skip if this test case for this device was already processed (to handle
				// potential duplicates in Procal response)
				if (!processedTestCases.add(uniqueKey)) {
					Verification.logger.warn(
							"S043_Get_Test_Point_Status : addTestPointMeterResultsWithLduPosition : Skipping duplicate test case: "
									+ uniqueKey);
					continue; // Skip to next iteration
				}

				Integer pos = positionNo; // Use Integer for contains method
				String resultValue = result.getString("error_value");
				String error_min = result.getString("error_min");
				String error_max = result.getString("error_max");
				String resultStatus = result.getString("test_status").equals("P") ? "Pass" : "Fail";
				// Determine test type based on constant configuration (e.g., "LOE" if starts
				// with "ACCURACY_ALIAS_NAME")
				String testType = testCaseName.startsWith(ConstantConveyor.ACCURACY_ALIAS_NAME)
						? ConstantConveyor.VERIFICATION_RESULT_KEY
						: "";

				MappedResult mappedResult = getMappedPosition(positionNo, pos, myPalletManageList);
				int mappedPosition = mappedResult.getMappedPosition();
				PalletManage selectedPallet = mappedResult.getSelectedPallet();

				if (mappedPosition != -1 && selectedPallet != null) {
					Verification.logger.info(
							"S043_Get_Test_Point_Status : addTestPointMeterResultsWithLduPosition : Adding result for Meter: "
									+ positionNo + " (Mapped: " + mappedPosition + ")");
					Verification.logger.info("S043_Get_Test_Point_Status : addTestPointMeterResultsWithLduPosition : "
							+ positionNo + " : Result Value : " + resultValue + ", Error Min: " + error_min
							+ ", Error Max: " + error_max);
					palletTracker.addResultToPalletMeterV2(mappedPosition, resultStatus, resultValue, error_min,
							error_max, getMyBayKey(), selectedPallet, testType, testCaseName);
				} else {
					Verification.logger.warn(
							"S043_Get_Test_Point_Status : addTestPointMeterResultsWithLduPosition : No matching pallet found for position: "
									+ positionNo + ". Skipping result addition.");
				}

				Verification.logger.debug("S043_Get_Test_Point_Status : verificDataCount : " + verificDataCount);
			}

			// Third pass: Update summary once per device based on overall pass/fail status
			for (Map.Entry<Integer, Boolean> entry : devicePassStatus.entrySet()) {
				int positionNo = entry.getKey();
				Integer pos = positionNo;
				boolean allPass = entry.getValue();

				String finalResultValue = allPass ? "Pass" : "Fail";
				String finalResultStatus = allPass ? "Pass" : "Fail";

				String testType = ConstantConveyor.VERIFICATION_RESULT_KEY;
				String testCaseName = ConstantConveyor.SUMMARY_VERIFICATION_RESULT_TEST_NAME;

				MappedResult mappedResult = getMappedPosition(positionNo, pos, myPalletManageList);
				int mappedPosition = mappedResult.getMappedPosition();
				PalletManage selectedPallet = mappedResult.getSelectedPallet();

				if (mappedPosition != -1 && selectedPallet != null) {
					Verification.logger.info(
							"S043_Get_Test_Point_Status : addTestPointMeterResultsWithLduPosition : Adding summary for Meter : "
									+ mappedPosition + " (Pallet: " + selectedPallet.getPalletQrId() + ")");
					palletTracker.addMeterResultSummaryWithPalletDetails(mappedPosition, finalResultStatus,
							finalResultValue, testCaseName, testType, selectedPallet);
				} else {
					Verification.logger.warn(
							"S043_Get_Test_Point_Status : addTestPointMeterResultsWithLduPosition : No matching pallet found for position: "
									+ mappedPosition + ". Skipping summary addition.");
				}
			}

		} catch (org.json.JSONException e) {
			Verification.logger
					.error("addTestPointMeterResultsWithLduPosition: Error parsing JSON in normal processing: "
							+ e.getMessage(), e);
		} catch (Exception e) {
			Verification.logger.error(
					"addTestPointMeterResultsWithLduPosition: An unexpected error occurred in addTestPointMeterResultsWithLduPosition: "
							+ e.getMessage(),
					e);
		}

		Verification.logger.info("S043_Get_Test_Point_Status : addTestPointMeterResultsWithLduPosition : Exit ");
	}

	/**
	 * Helper function to map device positions to pallet positions.
	 * This method assumes myPalletManageList contains the pallets in a specific
	 * order
	 * where index 0 is for Pallet 4, index 1 for Pallet 3, index 2 for Pallet 2,
	 * and index 3 for Pallet 1.
	 *
	 * @param positionNo         The raw device position number (1-24).
	 * @param pos                Integer representation of positionNo, used for
	 *                           `contains` checks.
	 * @param myPalletManageList A list of PalletManage objects representing active
	 *                           pallets in the bay.
	 * @return A MappedResult object containing the mapped position and the selected
	 *         PalletManage object.
	 */
	private MappedResult getMappedPosition(int positionNo, int pos, List<PalletManage> myPalletManageList) {
		Verification.logger.debug("getMappedPosition: Entry");
		int mappedPosition = -1;
		PalletManage selectedPallet = new PalletManage();
		;
		Verification.logger.debug("getMappedPosition: Hit1");
		MappedResult mappedResult = new MappedResult();
		Verification.logger.debug("getMappedPosition: Hit2");
		try {
			// Ensure there are at least 4 pallets available for mapping based on the logic
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
				Verification.logger.warn("getMappedPosition: Not enough pallets in myPalletManageList! Size: "
						+ myPalletManageList.size() + ". Expected at least 4 for correct mapping.");
			}

			// Log the mapping details
			Verification.logger.info("getMappedPosition: Mapping Position " + positionNo + " -> Mapped to "
					+ mappedPosition + " | Assigned to Pallet: "
					+ (selectedPallet != null ? selectedPallet.getPalletQrId() : "None"));
			mappedResult = new MappedResult(mappedPosition, selectedPallet);
			Verification.logger.debug("getMappedPosition: Hit9");
		} catch (Exception e) {
			e.printStackTrace();
			Verification.logger.error("S043_Get_Test_Point_Status getMappedPosition : Exception: " + e.getMessage());

		}
		// return new MappedResult(mappedPosition, selectedPallet);
		return mappedResult;

	}

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

	private class DeviceInfo {
		String dutSerialNo;
		String palletDistinctId;
		String palletRackPosition;
	}

}