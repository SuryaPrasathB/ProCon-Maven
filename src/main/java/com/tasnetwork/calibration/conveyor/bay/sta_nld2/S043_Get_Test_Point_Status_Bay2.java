package com.tasnetwork.calibration.conveyor.bay.sta_nld2;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

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

public class S043_Get_Test_Point_Status_Bay2 implements STA_NoLoadTestBay2State  {

	BayUtils bayUtils = new BayUtils();
	String presentBayKey = "";
	String sequencePathId = "p1";


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
			jsonObject.getJSONArray("Results");

			presentBayKey = ConstantConveyor.STA_NLD2_BAY_KEY; // STNLD2B

			// Fetch the list of pallets
			List<PalletManage> myPalletManageList = MySqlServiceManager.getPalletManageService().findByPresentBayKeyAndPalletActive(presentBayKey);

			StaNld_Bay2.logger.info("S043_Get_Test_Point_Status : myPalletManageList : size: " + myPalletManageList.size());


			
			ProCalTestResultsResponse procalResult = new ProCalTestResultsResponse();
			try {

				
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

				try {
				
					if(eachResult.getPallet_rack_position_no().isEmpty()){
						positionNo = 0;
					}else{
						positionNo = Integer.parseInt(eachResult.getPallet_rack_position_no());
					}
				resultValue = eachResult.getError_value();
				resultStatus = eachResult.getTest_status().equals("P") ? "Pass" : "Fail";
				testCaseName = eachResult.getTest_case_name();
				dutSerialNo = eachResult.getDut_serial_no();
				testType = testCaseName.startsWith("NLD") ? ConstantConveyor.NLD_RESULT_KEY : ConstantConveyor.STA_RESULT_KEY; //"NLD" : "STA";
				StaNld_Bay2.logger.info("S043_Get_Test_Point_Status : testCaseName " + testCaseName + "-> testType:" + testType + ", dutSerialNo: " + dutSerialNo + " ,palletDistinctId:" + palletDistinctId);


					
				if(positionNo!=0){
					StaNld_Bay2.logger.info("S043_Get_Test_Point_Status : addTestPointMeterResults : Adding result to Meter : " + positionNo);

					palletTracker.addResultToPalletMeterV1_1(positionNo,  dutSerialNo , resultStatus,  resultValue,  getMyBayKey(), palletDistinctId,  testType,  testCaseName,error_min, error_max  );
					
					if (testType.equals(ConstantConveyor.NLD_RESULT_KEY )) { //"NLD") {
						testCaseName = ConstantConveyor.NLD_RESULT_TEST_NAME ; //"No Load";
					} else if (testType.equals(ConstantConveyor.STA_RESULT_KEY)) { //"STA"){
						testCaseName = ConstantConveyor.STA_RESULT_TEST_NAME; //"Starting Current";
					}
					

					palletTracker.addMeterResultSummaryWithPalletDetailsV2(positionNo,dutSerialNo, resultStatus, resultStatus, testCaseName,  testType, palletDistinctId);
				}else{
					StaNld_Bay2.logger.info("S043_Get_Test_Point_Status : addTestPointMeterResults : Skipping: " + positionNo);
				}

					
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




}
