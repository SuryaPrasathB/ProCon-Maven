package com.tasnetwork.calibration.conveyor.bay.sta_nld1;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.conveyor.ClusterServer;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.pallet.PalletTrackerController;
import com.tasnetwork.calibration.conveyor.remote.ProcalRemoteResponse;
import com.tasnetwork.calibration.conveyor.remote.ProcalRemoteSender;
import com.tasnetwork.calibration.conveyor.remote.TestResult;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.PalletManage;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;


public class S043_Get_Test_Point_Immediately_Bay1 implements STA_NoLoadTestBay1State  {

	BayUtils bayUtils = new BayUtils();
	String presentBayKey = "";
	String sequencePathId = "p1";
	
	//private String myBayKey = ConstantConveyor.STA_NLD1_BAY_KEY;

	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		StaNld_Bay1.logger.info("S043_Get_Test_Point_Immediately : Entry");

		//BayUtils.delay(10000);

		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601 );


		ProcalRemoteSender procalRemoteSender = new ProcalRemoteSender();

		String ip_address = ConstantConveyorConfig.CONVEYOR_PROCAL_STA1_IP_ADDRESS;
		String ip_port = ConstantConveyorConfig.CONVEYOR_PROCAL_STA1_IP_PORT;
		String endpoint = ConstantConveyorConfig.CONVEYOR_PROCAL_STA1_END_PATH;

		ClusterServer stdNldt1ClusterServer = new ClusterServer(ip_address, ip_port);

		ProcalRemoteResponse myProcalRemoteResponse = procalRemoteSender.sendCommResultRefreshToProcal(stdNldt1ClusterServer, endpoint);

		//VerificationTestBay.logger.info("S043_Get_Test_Point_Immediately : isAllTestExecutionCompleted: " +

		int i = 0;

		while (!(myProcalRemoteResponse.getTestPointStatus().isAllTestExecutionCompleted()) && 
				!(S046_Stop_Execution_Bay1.isStopExecutionRequested()) && i < 60 &&
        		(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)){

			StaNld_Bay1.logger.info("S043_Get_Test_Point_Immediately : Getting test point status...");		
			BayUtils.delay(10000); // 10 sec once

			i++;

			myProcalRemoteResponse = procalRemoteSender.sendCommResultRefreshToProcal(stdNldt1ClusterServer, endpoint);

			ArrayList<TestResult> presentTpResult;
			presentTpResult = myProcalRemoteResponse.getTestPointStatus().getPresentTpResult();

			StaNld_Bay1.logger.info("S043_Get_Test_Point_Immediately : presentTpResult : " + presentTpResult);	

		}

		//if (true) { //myProcalRemoteResponse.getTestPointStatus().isAllTestExecutionCompleted()) {
		if (myProcalRemoteResponse.getTestPointStatus().isAllTestExecutionCompleted()) {
			StaNld_Bay1.logger.info("S043_Get_Test_Point_Immediately : Execution Success");

			String myProcalRemoteResult = procalRemoteSender.sendCommAllResultToProcal(stdNldt1ClusterServer, endpoint);

			StaNld_Bay1.logger.info("S043_Get_Test_Point_Immediately : All results : " + myProcalRemoteResult);

			addTestPointMeterResults(myProcalRemoteResult);

			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			StaNld_Bay1.logger.info("S043_Get_Test_Point_Immediately : Execution Failed");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_026);
		}

		StaNld_Bay1.logger.info("S043_Get_Test_Point_Immediately : Exit");
		return bayResponse;
	}
	
	//============================================================================================================================================  

	private void addTestPointMeterResults(String myProcalRemoteResult) {
		StaNld_Bay1.logger.info("S043_Get_Test_Point_Immediately : addTestPointMeterResults : Entry ");

		PalletTrackerController palletTracker = new PalletTrackerController();

		try {
			JSONObject jsonObject = new JSONObject(myProcalRemoteResult);
			JSONArray results = jsonObject.getJSONArray("Results");

			presentBayKey = ConstantConveyor.STA_NLD1_BAY_KEY; // STNLD1B

			// Fetch the list of pallets
			List<PalletManage> myPalletManageList = MySqlServiceManager.getPalletManageService().findByPresentBayKeyAndPalletActive(presentBayKey);

			StaNld_Bay1.logger.info("S043_Get_Test_Point_Immediately : myPalletManageList : " + myPalletManageList);



			for (int i = 0; i < results.length(); i++) {
				JSONObject result = results.getJSONObject(i);

				int positionNo = result.getInt("device_name");
				Integer pos = positionNo; // Explicitly convert to Integer
				String resultValue = result.getString("error_value");
				String resultStatus = result.getString("test_status").equals("P") ? "Pass" : "Fail";
				String testCaseName = result.getString("test_case_name");
				String testType = testCaseName.startsWith("NLD") ? ConstantConveyor.NLD_RESULT_KEY : ConstantConveyor.STA_RESULT_KEY; //"NLD" : "STA";

				// Determine the correct pallet based on position number
				PalletManage selectedPallet = null;
				int mappedPosition = positionNo;


				if (ConstantConveyor.STA_NLD1_PALLET1_POSITIONS.contains(pos)) { //Arrays.asList(new Integer[]{1, 2, 3, 22, 23, 24})
					selectedPallet = myPalletManageList.get(3);
					mappedPosition = positionNo; // 1-6 -> already 1-6
				} else if (ConstantConveyor.STA_NLD1_PALLET2_POSITIONS.contains(pos)) {
					selectedPallet = myPalletManageList.get(2);
					mappedPosition = positionNo - 6; // 7-12 -> 1-6
				} else if (ConstantConveyor.STA_NLD1_PALLET3_POSITIONS.contains(pos)) {
					selectedPallet = myPalletManageList.get(1);
					mappedPosition = positionNo - 12; // 13-18 -> 1-6
				} else if (ConstantConveyor.STA_NLD1_PALLET4_POSITIONS.contains(pos)) {
					selectedPallet = myPalletManageList.get(0);
					mappedPosition = positionNo - 18; // 19-24 -> 1-6
				}

				if (selectedPallet != null) {
					StaNld_Bay1.logger.info("S043_Get_Test_Point_Immediately : addTestPointMeterResults : Adding result to Meter : " + positionNo);

					palletTracker.addResultToPalletMeter(mappedPosition, resultStatus, resultValue, getMyBayKey(), selectedPallet, testType, testCaseName);
					
					if (testType.equals(ConstantConveyor.NLD_RESULT_KEY )) { //"NLD") {
						testCaseName = ConstantConveyor.NLD_RESULT_TEST_NAME ; //"No Load";
					} else if (testType.equals(ConstantConveyor.STA_RESULT_KEY)) { //"STA"){
						testCaseName = ConstantConveyor.STA_RESULT_TEST_NAME; //"Starting Current";
					}
					

					palletTracker.addMeterResultSummaryWithPalletDetails(mappedPosition, resultStatus, resultStatus, testCaseName,  testType, selectedPallet);
					//palletTracker.addMeterResultSummary(mappedPosition, resultStatus, resultStatus, testType, testType, selectedPallet);
				} else {
					StaNld_Bay1.logger.warn("S043_Get_Test_Point_Immediately : addTestPointMeterResults : No matching pallet found for position: " + positionNo);
				}
			}

		} catch (JSONException e) {
			StaNld_Bay1.logger.error("Error parsing JSON: " + e.getMessage(), e);
		}

		StaNld_Bay1.logger.info("S043_Get_Test_Point_Immediately : addTestPointMeterResults : Exit ");
	}



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




}
