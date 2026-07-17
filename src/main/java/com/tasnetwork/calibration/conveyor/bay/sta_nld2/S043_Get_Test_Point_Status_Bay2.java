package com.tasnetwork.calibration.conveyor.bay.sta_nld2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasnetwork.calibration.conveyor.ClusterServer;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.verific.Verification;
//import com.tasnetwork.calibration.conveyor.bay_calibration.CalibrationBay;
//import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S046_Stop_Execution;
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

public class S043_Get_Test_Point_Status_Bay2 implements STA_NoLoadTestBay2State  {

	BayUtils bayUtils = new BayUtils();
	String presentBayKey = "";
	String sequencePathId = "p1";

	//private String myBayKey = ConstantConveyor.STA_NLD2_BAY_KEY;
	public String getMyBayKey() {
		return myBayKey;
	}
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		StaNld_Bay2.logger.info("S043_Get_Test_Point_Status : Entry");

		//BayUtils.delay(10000);

		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601 );


		ProcalRemoteSender procalRemoteSender = new ProcalRemoteSender();

		String ip_address = ConstantConveyorConfig.CONVEYOR_PROCAL_STA2_IP_ADDRESS;
		String ip_port = ConstantConveyorConfig.CONVEYOR_PROCAL_STA2_IP_PORT;
		String endpoint = ConstantConveyorConfig.CONVEYOR_PROCAL_STA2_END_PATH;

		ClusterServer stdNldt2ClusterServer = new ClusterServer(ip_address, ip_port);

		ProcalRemoteResponse myProcalRemoteResponse = procalRemoteSender.sendCommResultRefreshToProcal(stdNldt2ClusterServer, endpoint);

		//VerificationTestBay.logger.info("S043_Get_Test_Point_Status : isAllTestExecutionCompleted: " +

		int i = 0;
		int delayCount = 10;
		while (!(myProcalRemoteResponse.getTestPointStatus().isAllTestExecutionCompleted()) && 
				!(StaNld_Bay2.isStopProcessRequestedStaNldBay2()) 							&& 
				//i < 60 																		&&// commented by Gopi on d0.8.5.4 06-jul-2025
				(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)){

			StaNld_Bay2.logger.info("S043_Get_Test_Point_Status : Getting test point status...");		
			//BayUtils.delay(10000); // 10 sec once
			delayCount = 10;
			while( (delayCount>0) && (!StaNld_Bay2.isStopProcessRequestedStaNldBay2()) ){
				delayCount--;
				BayUtils.delay(1000);
			}
			if(StaNld_Bay2.isStopProcessRequestedStaNldBay2()){
				StaNld_Bay2.logger.info("S043_Get_Test_Point_Status : sta2 stop requested: break");
				break;
			}
			i++;

			myProcalRemoteResponse = procalRemoteSender.sendCommResultRefreshToProcal(stdNldt2ClusterServer, endpoint);

			ArrayList<TestResult> presentTpResult;
			presentTpResult = myProcalRemoteResponse.getTestPointStatus().getPresentTpResult();

			StaNld_Bay2.logger.info("S043_Get_Test_Point_Status : presentTpResult : " + presentTpResult);	

		}
		
		StaNld_Bay2.logger.info("S043_Get_Test_Point_Status : Getting test point status: isAllTestExecutionCompleted: " + myProcalRemoteResponse.getTestPointStatus().isAllTestExecutionCompleted());	
		StaNld_Bay2.logger.info("S043_Get_Test_Point_Status : Getting test point status: isStopProcessRequestedSctNltBay2: " + StaNld_Bay2.isStopProcessRequestedStaNldBay2());	
		StaNld_Bay2.logger.info("S043_Get_Test_Point_Status : Getting test point status: ALL_LOOP_BREAK_FLAG: " + ConstantConveyor.ALL_LOOP_BREAK_FLAG);	
		StaNld_Bay2.logger.info("S043_Get_Test_Point_Status : Getting test point status: iteration count : " + i);
		if (!(StaNld_Bay2.isStopProcessRequestedStaNldBay2()) ){
		if (myProcalRemoteResponse.getTestPointStatus().isAllTestExecutionCompleted()) {
				StaNld_Bay2.logger.info("S043_Get_Test_Point_Status : Execution Success");
				int retryCount = 3;
				String myProcalRemoteResult = "";
				boolean expectedResultRecieved = false;
				String expectedContent = "\"Results\":";
				while ( (retryCount>0) && 
						(!StaNld_Bay2.isStopProcessRequestedStaNldBay2()) &&
						(!expectedResultRecieved) ){
					myProcalRemoteResult = procalRemoteSender.sendCommAllResultToProcal(stdNldt2ClusterServer, endpoint);
					retryCount--;
					if(myProcalRemoteResult.contains(expectedContent)){
						if(myProcalRemoteResult.length() > 40 ) {
							StaNld_Bay2.logger.info("S043_Get_Test_Point_Status : Expected result found");
							expectedResultRecieved = true;
						}else {
							StaNld_Bay2.logger.info("S043_Get_Test_Point_Status : length not sufficient: "+ myProcalRemoteResult);
						}
					}
				}
				StaNld_Bay2.logger.info("S043_Get_Test_Point_Status : All results : " + myProcalRemoteResult);
				if(expectedResultRecieved){
					addTestPointMeterResults(myProcalRemoteResult);
					bayResponse.setStatus(true);
					bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
				}else{
					StaNld_Bay2.logger.info("S043_Get_Test_Point_Status : Expected result not found");
					bayResponse.setStatus(false);
					bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_028);
				}
				
			} else {
				StaNld_Bay2.logger.info("S043_Get_Test_Point_Status : Execution Failed");
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_026);
			}
		}

		StaNld_Bay2.logger.info("S043_Get_Test_Point_Status : Exit");
		return bayResponse;
	}

	//============================================================================================================================================  

	private void addTestPointMeterResults(String myProcalRemoteResult) {
		StaNld_Bay2.logger.info("S043_Get_Test_Point_Status : addTestPointMeterResults : Entry ");

		PalletTrackerController palletTracker = new PalletTrackerController();

		try {
			JSONObject jsonObject = new JSONObject(myProcalRemoteResult);
			JSONArray results = jsonObject.getJSONArray("Results");

			presentBayKey = ConstantConveyor.STA_NLD2_BAY_KEY; // STNLD2B

			// Fetch the list of pallets
			List<PalletManage> myPalletManageList = MySqlServiceManager.getPalletManageService().findByPresentBayKeyAndPalletActive(presentBayKey);

			StaNld_Bay2.logger.info("S043_Get_Test_Point_Status : myPalletManageList : size: " + myPalletManageList.size());

			/*PalletManage selectedPallet = null;
		        selectedPallet = myPalletManageList.get(0);

		        STA_NoLoadTestBay2.logger.info("S043_Get_Test_Point_Status : selectedPallet : 0 " + selectedPallet);*/
			
			ProCalTestResultsResponse procalResult = new ProCalTestResultsResponse();
			try {
				/*procalResult = JsonPath
				    .from(myProcalRemoteResult)
				    .getObject("", ProCalTestResultsResponse.class);*/
				
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

				procalResult = mapper.readValue(myProcalRemoteResult, ProCalTestResultsResponse.class);
				
				for(Result eachResult: procalResult.getResults()) {
					StaNld_Bay2.logger.info("S043_Get_Test_Point_Status : eachResult :" 
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
				StaNld_Bay2.logger.error("S043_Get_Test_Point_Status procalResult : Exception: " + e.getMessage());
				
			}
			//for (int i = 0; i < results.length(); i++) {
			int positionNo = 0;
			String resultValue = "";//
			String resultStatus = "";
			String testCaseName = "";
			String dutSerialNo = "";
			String testType = "";//
			String palletDistinctId = "";
			String error_min = "";
			String error_max = "";
			for (Result eachResult: procalResult.getResults()) {
				//JSONObject result = results.getJSONObject(i);
				try {
				
					if(eachResult.getPallet_rack_position_no().isEmpty()){
						positionNo = 0;
					}else{
						positionNo = Integer.parseInt(eachResult.getPallet_rack_position_no());
					}
				//positionNo = Integer.parseInt(eachResult.getPallet_rack_position_no());
				palletDistinctId = eachResult.getPallet_distinct_id();//Integer.parseInt(eachResult.getDevice_name());//result.getInt("device_name");
				//Integer pos = positionNo; // Explicitly convert to Integer
				resultValue = eachResult.getError_value();//result.getString("error_value");
				resultStatus = eachResult.getTest_status().equals("P") ? "Pass" : "Fail";//result.getString("test_status").equals("P") ? "Pass" : "Fail";
				testCaseName = eachResult.getTest_case_name();//result.getString("test_case_name");
				dutSerialNo = eachResult.getDut_serial_no();//result.getString("dut_serial_no");
				testType = testCaseName.startsWith("NLD") ? ConstantConveyor.NLD_RESULT_KEY : ConstantConveyor.STA_RESULT_KEY; //"NLD" : "STA";
				StaNld_Bay2.logger.info("S043_Get_Test_Point_Status : testCaseName " + testCaseName + "-> testType:" + testType + ", dutSerialNo: " + dutSerialNo + " ,palletDistinctId:" + palletDistinctId);

				/*int positionNo = result.getInt("device_name");
				Integer pos = positionNo; // Explicitly convert to Integer
				String resultValue = result.getString("error_value");
				String resultStatus = result.getString("test_status").equals("P") ? "Pass" : "Fail";
				String testCaseName = result.getString("test_case_name");
				String dutSerialNo = result.getString("dut_serial_no");
				String testType = testCaseName.startsWith("NLD") ? ConstantConveyor.NLD_RESULT_KEY : ConstantConveyor.STA_RESULT_KEY; //"NLD" : "STA";
				StaNld_Bay2.logger.info("S043_Get_Test_Point_Status : testCaseName " + testCaseName + "-> testType:" + testType + ", dutSerialNo: " + dutSerialNo);
*/
				// Determine the correct pallet based on position number
//				PalletManage selectedPallet = null;
//				int mappedPosition = positionNo;

				/*if (ConstantConveyor.STA_NLD1_PALLET2_POSITIONS.contains(pos)) { //Arrays.asList(new Integer[]{1, 2, 3, 22, 23, 24})
					selectedPallet = myPalletManageList.get(0);
					mappedPosition = (positionNo == 22) ? 4 : (positionNo == 23) ? 5 : (positionNo == 24) ? 6 : positionNo;
				} else if (ConstantConveyor.STA_NLD2_PALLET2_POSITIONS.contains(pos)) {
					selectedPallet = myPalletManageList.get(1);
					mappedPosition = (positionNo == 19) ? 4 : (positionNo == 20) ? 5 : (positionNo == 21) ? 6 : positionNo - 3;
				} else if (ConstantConveyor.STA_NLD2_PALLET3_POSITIONS.contains(pos)) {
					selectedPallet = myPalletManageList.get(2);
					mappedPosition = (positionNo == 16) ? 4 : (positionNo == 17) ? 5 : (positionNo == 18) ? 6 : positionNo - 6;
				} else if (ConstantConveyor.STA_NLD2_PALLET4_POSITIONS.contains(pos)) {
					selectedPallet = myPalletManageList.get(3);
					mappedPosition = positionNo - 9;
				}*/
				
/*				if (ConstantConveyor.STA_NLD2_PALLET1_POSITIONS.contains(pos)) { //Arrays.asList(new Integer[]{1, 2, 3, 22, 23, 24})
					selectedPallet = myPalletManageList.get(3);
					mappedPosition = positionNo; // 1-6 -> already 1-6
				} else if (ConstantConveyor.STA_NLD2_PALLET2_POSITIONS.contains(pos)) {
					selectedPallet = myPalletManageList.get(2);
					mappedPosition = positionNo - 6; // 7-12 -> 1-6
				} else if (ConstantConveyor.STA_NLD2_PALLET3_POSITIONS.contains(pos)) {
					selectedPallet = myPalletManageList.get(1);
					mappedPosition = positionNo - 12; // 13-18 -> 1-6
				} else if (ConstantConveyor.STA_NLD2_PALLET4_POSITIONS.contains(pos)) {
					selectedPallet = myPalletManageList.get(0);
					mappedPosition = positionNo - 18; // 19-24 -> 1-6
				}*/

				//if (selectedPallet != null) {
					
				if(positionNo!=0){
					StaNld_Bay2.logger.info("S043_Get_Test_Point_Status : addTestPointMeterResults : Adding result to Meter : " + positionNo);
					/*palletTracker.addResultToMeter(mappedPosition, resultStatus, resultValue, getMyBayKey(), testType, testCaseName);*/
					//palletTracker.addResultToPalletMeter(mappedPosition, resultStatus, resultValue, getMyBayKey(), selectedPallet, testType, testCaseName);
					palletTracker.addResultToPalletMeterV1_1(positionNo,  dutSerialNo , resultStatus,  resultValue,  getMyBayKey(), palletDistinctId,  testType,  testCaseName,error_min, error_max  );
					
					if (testType.equals(ConstantConveyor.NLD_RESULT_KEY )) { //"NLD") {
						testCaseName = ConstantConveyor.NLD_RESULT_TEST_NAME ; //"No Load";
					} else if (testType.equals(ConstantConveyor.STA_RESULT_KEY)) { //"STA"){
						testCaseName = ConstantConveyor.STA_RESULT_TEST_NAME; //"Starting Current";
					}
					
					/*palletTracker.addMeterResultSummary(positionNo, resultStatus, resultStatus, getMyBayKey(), testType, testType);*/
					
					//palletTracker.addMeterResultSummaryWithPalletDetails(mappedPosition, resultStatus, resultStatus, testCaseName,  testType, selectedPallet);
					palletTracker.addMeterResultSummaryWithPalletDetailsV2(positionNo,dutSerialNo, resultStatus, resultStatus, testCaseName,  testType, palletDistinctId);
				}else{
					StaNld_Bay2.logger.info("S043_Get_Test_Point_Status : addTestPointMeterResults : Skipping: " + positionNo);
				}
					/*StaNld_Bay2.logger.info("S043_Get_Test_Point_Status : addTestPointMeterResults : Adding result to Meter : " + positionNo);
					palletTracker.addResultToMeter(mappedPosition, resultStatus, resultValue, getMyBayKey(), testType, testCaseName);

					if (testType == ConstantConveyor.NLD_RESULT_KEY ) { //"NLD") {
						testCaseName = ConstantConveyor.NLD_RESULT_TEST_NAME ; //"No Load";
					} else if (testType == ConstantConveyor.STA_RESULT_KEY) { //"STA"){
						testCaseName = ConstantConveyor.STA_RESULT_TEST_NAME; //"Starting Current";
					}

					palletTracker.addMeterResultSummary(positionNo, resultStatus, resultValue, getMyBayKey(), testType, testType);*/
					// palletTracker.addMeterResultSummary(mappedPosition, resultStatus, resultStatus, testType, testType, selectedPallet);
				/*} else {
					StaNld_Bay2.logger.warn("S043_Get_Test_Point_Status : addTestPointMeterResults : No matching pallet found for position: " + positionNo);
				}*/
					
				}catch(Exception e) {
					e.printStackTrace();
					StaNld_Bay2.logger.error("S043_Get_Test_Point_Status procalResult : Exception2: " + e.getMessage());
					
				}
					
			}

		} catch (org.json.JSONException e) {
			StaNld_Bay2.logger.error("Error parsing JSON: " + e.getMessage(), e);
		}

		StaNld_Bay2.logger.info("S043_Get_Test_Point_Status : addTestPointMeterResults : Exit ");
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

/*	public String getMyBayKey() {
		return myBayKey;
	}*/


}
