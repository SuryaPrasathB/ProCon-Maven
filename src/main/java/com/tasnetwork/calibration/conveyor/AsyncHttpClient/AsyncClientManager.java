package com.tasnetwork.calibration.conveyor.AsyncHttpClient;

import java.util.ArrayList;
// import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.ning.http.client.Param;
import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import com.tasnetwork.calibration.conveyor.constant.ConstantProTamp;
import com.tasnetwork.calibration.conveyor.database.MySQL_Controller;
// import com.tasnetwork.calibration.conveyor.util.GUIUtils;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.setting.ServerSettingController;

public class AsyncClientManager {

	private AsyncClient asyncClient = new AsyncClient();

	private static boolean responseReceived = false;

	/* public static String rootUrl; */

	public void getServerTimeZoneList() {

		asyncClient.lstamper_Scan_Available_timezones("lstamperGetTimeZoneList");
	}

	public void getTamperGetStatus() {

		// asyncClient.lstamper_getStatus("lstamper_getStatus");
	}

	public void getTamperGetStatus3PhaseWithRefStd() {

		// asyncClient.lstamper_getStatus("lsTamper3PhaseWithRefStdGetStatus");
	}

	public void getServerSerialStatus() {
		asyncClient.lstamper_serialStatus("lstamperSerialStatus");
	}

	public void getServerFirmwareVersion() {

		asyncClient.lstamper_getFirmwareVersion("lstamperGetFirmwareVersion");
	}

	public void ValidateCredential() {

		asyncClient.ValidateCred("ValidateCred");
	}

	public void ShutDownServer() {

		asyncClient.Shutdown("Shutdown");
	}

	public void tamperScanServerLogFolders() {

		asyncClient.lstamper_Scan_Available_LogFolders("lstamperLogFolder");
	}

	public void tamperScanServerLogFoldersV2() {
		String TARGET_URL = ServerSettingController.getRootUrl()
				+ "/lstamperLogFolder";
		Request req = new RequestBuilder("GET")
				.setUrl(TARGET_URL)
				.setBody("ISO-8859-1")
				.addHeader("Content-Type", "application/x-www-form-urlencoded")
				.build();
		asyncClient.lstamper_Scan_Available_LogFoldersV2(req);
	}

	public void tamperScanAvailabledrives() {
		String TARGET_URL = ServerSettingController.getRootUrl()
				+ "/lstamperAvailableDrives";
		Request req = new RequestBuilder("GET")
				.setUrl(TARGET_URL)
				.setBody("ISO-8859-1")
				.addHeader("Content-Type", "application/x-www-form-urlencoded")
				.build();
		asyncClient.lstamper_Scan_Available_drives(req);
	}

	public void tamperScanfilesindrives(String SelectedDrive) {
		String TARGET_URL = ServerSettingController.getRootUrl()
				+ "/lstamperZipFilesinSelectedDrives";

		List<Param> params = new ArrayList<>();
		params.add(new Param("drive", SelectedDrive));
		Request req = new RequestBuilder("POST")
				.setUrl(TARGET_URL)
				.setBody("ISO-8859-1")
				.addHeader("Content-Type", "application/x-www-form-urlencoded")
				// .addParameter("drive", SelectedDrive)
				.setFormParams(params)
				.build();
		asyncClient.lstamper_Scan_files_in_drives(req);
	}

	public void tampervalidateselectedfile(String SelectedFile) {
		String TARGET_URL = ServerSettingController.getRootUrl()
				+ "/lstamperValidateZipFile";
		List<Param> params = new ArrayList<>();
		params.add(new Param("ZipFile", SelectedFile));
		Request req = new RequestBuilder("POST")
				.setUrl(TARGET_URL)
				.setBody("ISO-8859-1")
				.addHeader("Content-Type", "application/x-www-form-urlencoded")
				// .addParameter("ZipFile", SelectedFile)
				.setFormParams(params)
				.build();
		asyncClient.lstamper_validate_selected_file(req);
	}

	public void tamperDeployFile() {
		String TARGET_URL = ServerSettingController.getRootUrl()
				+ "/lstamperDeployLauncher";
		// String TARGET_URL = ServerSettingController.getRootUrl()
		// +"/lstamperDeployGUILauncher";
		Request req = new RequestBuilder("GET")
				.setUrl(TARGET_URL)
				.setBody("ISO-8859-1")
				.addHeader("Content-Type", "application/x-www-form-urlencoded")
				.build();
		asyncClient.lstamper_Deploy_File(req);
	}

	public void tamperDeployFileV2() {
		String TARGET_URL = ServerSettingController.getRootUrl()
				+ "/lstamperDeployLauncherV2";
		// String TARGET_URL = ServerSettingController.getRootUrl()
		// +"/lstamperDeployGUILauncher";
		Request req = new RequestBuilder("GET")
				.setUrl(TARGET_URL)
				.setBody("ISO-8859-1")
				.addHeader("Content-Type", "application/x-www-form-urlencoded")
				.build();
		asyncClient.lstamper_Deploy_FileV2(req);
	}

	public void tamperDeployGUIFile() {
		String TARGET_URL = ServerSettingController.getRootUrl()
				+ "/lstamperDeployGUILauncher";
		// String TARGET_URL = ServerSettingController.getRootUrl()
		// +"/lstamperDeployGUILauncher";
		Request req = new RequestBuilder("GET")
				.setUrl(TARGET_URL)
				.setBody("ISO-8859-1")
				.addHeader("Content-Type", "application/x-www-form-urlencoded")
				.build();
		asyncClient.lstamper_DeployGUI_File(req);
	}

	public static void load_saved_server_settings() {

		JSONObject serverSetting = MySQL_Controller.sp_get_server_setting();
		try {
			if (serverSetting.has("http_protocol")) {
				ServerProperties.HTTP_Protocol = serverSetting.getString("http_protocol");
			} else {

				ServerProperties.HTTP_Protocol = "";
				ApplicationLauncher.logger
						.info("load_saved_server_settings: http_protocol: Data not retrieved from DB");

			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("load_saved_server_settings: JSONException1:" + e.getMessage());
			ServerProperties.HTTP_Protocol = "";
			ApplicationLauncher.logger
					.info("load_saved_server_settings: http_protocol: Data not retrieved from database");
		}

		try {
			if (serverSetting.has("server_ip")) {
				ServerProperties.PublicURL_Id = serverSetting.getString("server_ip");
			} else {

				ServerProperties.PublicURL_Id = "";
				ApplicationLauncher.logger.info("load_saved_server_settings: server_ip: Data not retrieved from DB");

			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("load_saved_server_settings: JSONException1:" + e.getMessage());
			ServerProperties.PublicURL_Id = "";
			ApplicationLauncher.logger.info("load_saved_server_settings: server_ip: Data not retrieved from database");
		}

		try {
			if (serverSetting.has("server_port")) {
				ServerProperties.URLPort = serverSetting.getString("server_port");
			} else {

				ServerProperties.URLPort = "";
				ApplicationLauncher.logger.info("load_saved_server_settings: server_port: Data not retrieved from DB");

			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("load_saved_server_settings: JSONException1:" + e.getMessage());
			ServerProperties.URLPort = "";
			ApplicationLauncher.logger
					.info("load_saved_server_settings: server_port: Data not retrieved from database");
		}

		try {
			if (serverSetting.has("refresh_gui_freq")) {
				ServerProperties.pollingFrequencyInSec = serverSetting.getInt("refresh_gui_freq");
			} else {

				ServerProperties.pollingFrequencyInSec = 3;
				ApplicationLauncher.logger
						.info("load_saved_server_settings: refresh_gui_freq: Data not retrieved from DB");

			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("load_saved_server_settings: JSONException1:" + e.getMessage());
			ServerProperties.pollingFrequencyInSec = 3;
			ApplicationLauncher.logger
					.info("load_saved_server_settings: refresh_gui_freq: Data not retrieved from database");
		}

	}
	/*
	 * public static String getRootUrl(){
	 * return rootUrl;
	 * }
	 * 
	 * public static void setRootUrl(){
	 * rootUrl = ServerProperties.HTTP_Protocol +
	 * ServerProperties.PublicURL_Id+
	 * ServerProperties.EndURL+":"+
	 * ServerProperties.URLPort;
	 * 
	 * }
	 */

	public void setTimeZoneOnServer(String SelectedTimeZone) {
		String TARGET_URL = ServerSettingController.getRootUrl()
				+ "/lstamperConnecttoTimezone";
		List<Param> params = new ArrayList<>();
		params.add(new Param("timezone", SelectedTimeZone));
		Request req = new RequestBuilder("POST")
				.setUrl(TARGET_URL)
				.addHeader("Content-Type", "application/x-www-form-urlencoded")
				// .addParameter("timezone", SelectedTimeZone)
				.setFormParams(params)
				.build();
		asyncClient.lstamper_set_selected_Timezone(req);
	}

	public void tamperViewLogFromSelectedFile(String logfilename) {
		String TARGET_URL = ServerSettingController.getRootUrl()
				+ "/lstamperFetchLogs";
		List<Param> params = new ArrayList<>();
		params.add(new Param("getlogfilename", logfilename));
		Request req = new RequestBuilder("POST")
				.setUrl(TARGET_URL)
				.addHeader("Content-Type", "application/x-www-form-urlencoded")
				// .addParameter("getlogfilename", logfilename)
				.setFormParams(params)
				.build();
		asyncClient.lstamper_viewlog_from_selected_file(req);
	}

	public void tamperScanFilesInLogFolder(String logDateFolder) {
		String TARGET_URL = ServerSettingController.getRootUrl()
				+ "/lstamperFindLogFile";
		// For application/x-www-form-urlencoded
		List<Param> params = new ArrayList<>();
		params.add(new Param("logdatefolder", logDateFolder));
		Request req = new RequestBuilder("POST")
				.setUrl(TARGET_URL)
				.addHeader("Content-Type", "application/x-www-form-urlencoded")
				// .addParameter("logdatefolder", logDateFolder)
				.setFormParams(params)
				.build();
		asyncClient.lstamper_Scan_files_in_logfolder(req);
	}

	public void tamperManualStop(String tp_ID, String metreconstant, String nooftestpulses) {

		ApplicationLauncher.logger.debug("tamperManualStop:Entry");
		String TestPointID = tp_ID;// String.format(ConstantProTamp.TEST_POINT_ID_FORMAT, tp_ID);
		// String TestPointID = "000";
		/*
		 * if(ConstantProTamp.PROTAMP_SINGLE_PHASE){
		 * TestPointID = TestPointID.substring(1);
		 * }
		 */
		TestPointID = new Integer(TestPointID).toString();
		String TARGET_URL = ServerSettingController.getRootUrl()
				+ "/lstamperManualStop";
		List<Param> formParams = new ArrayList<>();
		formParams.add(new Param("testid", TestPointID));
		formParams.add(new Param("meterconstant", metreconstant));
		formParams.add(new Param("nooftestpulses", nooftestpulses));
		Request req = new RequestBuilder("POST")
				.setUrl(TARGET_URL)
				.addHeader("Content-Type", "application/x-www-form-urlencoded")
				/*
				 * .addParameter("testid", TestPointID)
				 * .addParameter("meterconstant", metreconstant)
				 * .addParameter("nooftestpulses", nooftestpulses)
				 */
				.setFormParams(formParams)
				.build();
		asyncClient.lstamper_Manual_Stop(req);
	}

	public void tamperAutoStop() {
		ApplicationLauncher.logger.debug("tamperAutoStop :Entry");
		String TARGET_URL = ServerSettingController.getRootUrl()
				+ "/lstamperAutoStop";
		String TestPointID = "000";
		if (ConstantProTamp.PROTAMP_SINGLE_PHASE) {
			TestPointID = TestPointID.substring(1);
		}
		List<Param> params = new ArrayList<>();
		params.add(new Param("testid", TestPointID));
		Request req = new RequestBuilder("POST")
				.setUrl(TARGET_URL)
				.addHeader("Content-Type", "application/x-www-form-urlencoded")
				// .addParameter("testid", TestPointID)
				.setFormParams(params)
				.build();
		asyncClient.lstamper_Auto_Stop(req);
	}

	public void tamperManualStart3PhaseWithRefStd(String tp_ID, String meterconstant,
			String nooftestpulses, String refStdConstant,
			String noOfReadingSamples) {
		ApplicationLauncher.logger.debug("tamperManualStart3PhaseWithRefStd :Entry");
		String testPointID = tp_ID;// String.format("%02d", tp_ID);
		/*
		 * if(ConstantProTamp.PROTAMP_SINGLE_PHASE){
		 * testPointID = testPointID.substring(1);
		 * ApplicationLauncher.logger.
		 * info("tamperManualStart :ManualMode: testPointID: "+ testPointID);
		 * }
		 */
		testPointID = new Integer(testPointID).toString(); // remove the LeadingZero

		String TARGET_URL = ServerSettingController.getRootUrl()
				+ "/lsTamper3PhaseManualStartWithRefStd";
		List<Param> formParams = new ArrayList<>();
		formParams.add(new Param("testid", testPointID));
		formParams.add(new Param("meterconstant", meterconstant));
		formParams.add(new Param("nooftestpulses", nooftestpulses));
		formParams.add(new Param("refStdConstant", refStdConstant));
		formParams.add(new Param("noOfReadingSamples", noOfReadingSamples));
		Request req = new RequestBuilder("POST")
				.setUrl(TARGET_URL)
				.addHeader("Content-Type", "application/x-www-form-urlencoded")
				// .addHeader("Content-Type", "application/json")
				// .addHeader("content-type", "application/json")
				/*
				 * .addParameter("testid", testPointID)
				 * .addParameter("meterconstant", meterconstant)
				 * .addParameter("nooftestpulses", nooftestpulses)
				 * .addParameter("refStdConstant", refStdConstant)
				 * .addParameter("noOfReadingSamples", noOfReadingSamples)
				 */
				/*
				 * .addParameter("primaryCommand", "*idn?\r\n")
				 * .addParameter("expectedResponse", nooftestpulses)
				 * .addParameter("failureResponse", nooftestpulses)
				 * .addParameter("failBackCommand", nooftestpulses)
				 */
				.setFormParams(formParams)
				.build();
		asyncClient.lsTamperManual3PhaseStartWithRefStd(req);
	}

	public void tamperManualStart3PhaseWithRefStdAndLoadResistance(String tp_ID, String meterconstant,
			String nooftestpulses, String refStdConstant,
			String noOfReadingSamples,
			String rPhaseTestSeqResistanceLoadIndex,
			String yPhaseTestSeqResistanceLoadIndex,
			String bPhaseTestSeqResistanceLoadIndex) {
		ApplicationLauncher.logger.debug("tamperManualStart3PhaseWithRefStd :Entry");
		String testPointID = tp_ID;// String.format("%02d", tp_ID);
		/*
		 * if(ConstantProTamp.PROTAMP_SINGLE_PHASE){
		 * testPointID = testPointID.substring(1);
		 * ApplicationLauncher.logger.
		 * info("tamperManualStart :ManualMode: testPointID: "+ testPointID);
		 * }
		 */
		testPointID = new Integer(testPointID).toString(); // remove the LeadingZero

		String TARGET_URL = ServerSettingController.getRootUrl()
				+ "/lsTamper3PhaseManualStartWithRefStdAndLoad";

		List<Param> formParams = new ArrayList<>();
		formParams.add(new Param("testid", testPointID));
		formParams.add(new Param("meterconstant", meterconstant));
		formParams.add(new Param("nooftestpulses", nooftestpulses));
		formParams.add(new Param("refStdConstant", refStdConstant));
		formParams.add(new Param("noOfReadingSamples", noOfReadingSamples));
		formParams.add(new Param("r_ManageResistanceIndex", rPhaseTestSeqResistanceLoadIndex));
		formParams.add(new Param("y_ManageResistanceIndex", yPhaseTestSeqResistanceLoadIndex));
		formParams.add(new Param("b_ManageResistanceIndex", bPhaseTestSeqResistanceLoadIndex));

		Request req = new RequestBuilder("POST")
				.setUrl(TARGET_URL)
				.addHeader("Content-Type", "application/x-www-form-urlencoded")
				// .addHeader("Content-Type", "application/json")
				// .addHeader("content-type", "application/json")
				/*
				 * .addParameter("testid", testPointID)
				 * .addParameter("meterconstant", meterconstant)
				 * .addParameter("nooftestpulses", nooftestpulses)
				 * .addParameter("refStdConstant", refStdConstant)
				 * .addParameter("noOfReadingSamples", noOfReadingSamples)
				 * .addParameter("r_ManageResistanceIndex", rPhaseTestSeqResistanceLoadIndex)
				 * .addParameter("y_ManageResistanceIndex", yPhaseTestSeqResistanceLoadIndex)
				 * .addParameter("b_ManageResistanceIndex", bPhaseTestSeqResistanceLoadIndex)
				 */
				/*
				 * .addParameter("primaryCommand", "*idn?\r\n")
				 * .addParameter("expectedResponse", nooftestpulses)
				 * .addParameter("failureResponse", nooftestpulses)
				 * .addParameter("failBackCommand", nooftestpulses)
				 */

				.setFormParams(formParams)
				.build();
		asyncClient.lsTamperManual3PhaseStartWithRefStd(req);
	}

	public void tamperAutoStart3PhaseWithRefStd(String occur_time_in_sec, String restore_time_in_sec,
			String nooftestpulses, String noOfReadingSamples,
			String final_testcase_selected_list_string) {
		ApplicationLauncher.logger.debug("tamperAutoStart3PhaseWithRefStd :Entry");
		// String testPointID = tp_ID;//String.format("%02d", tp_ID);
		/*
		 * if(ConstantProTamp.PROTAMP_SINGLE_PHASE){
		 * testPointID = testPointID.substring(1);
		 * ApplicationLauncher.logger.
		 * info("tamperManualStart :ManualMode: testPointID: "+ testPointID);
		 * }
		 */
		// testPointID = new Integer(testPointID).toString(); // remove the LeadingZero

		String TARGET_URL = ServerSettingController.getRootUrl()
				+ "/lsTamper3PhaseAutoStartWithRefStd";

		List<Param> formParams = new ArrayList<>();
		formParams.add(new Param("occtime", occur_time_in_sec));
		formParams.add(new Param("restime", restore_time_in_sec));
		formParams.add(new Param("nooftestpulses", nooftestpulses));
		formParams.add(new Param("noOfReadingSamples", noOfReadingSamples));
		formParams.add(new Param("userselectedTClist", final_testcase_selected_list_string));
		Request req = new RequestBuilder("POST")
				.setUrl(TARGET_URL)
				.addHeader("Content-Type", "application/x-www-form-urlencoded")
				// .addHeader("Content-Type", "application/json")
				// .addHeader("content-type", "application/json")
				/*
				 * .addParameter("testid", testPointID)
				 * .addParameter("meterconstant", meterconstant)
				 * 
				 */
				/*
				 * .addParameter("primaryCommand", "*idn?\r\n")
				 * .addParameter("expectedResponse", nooftestpulses)
				 * .addParameter("failureResponse", nooftestpulses)
				 * .addParameter("failBackCommand", nooftestpulses)
				 */
				/*
				 * .addParameter("occtime", occur_time_in_sec)
				 * .addParameter("restime", restore_time_in_sec)
				 * .addParameter("nooftestpulses", nooftestpulses)
				 * .addParameter("noOfReadingSamples", noOfReadingSamples)
				 * .addParameter("userselectedTClist", final_testcase_selected_list_string)
				 */
				.setFormParams(formParams)
				.build();
		asyncClient.lsTamperAuto3PhaseStartWithRefStd(req);
	}

	public void tamperAutoStart3PhaseWithRefStdAndLoad(String occur_time_in_sec, String restore_time_in_sec,
			String nooftestpulses, String noOfReadingSamples,
			String final_testcase_selected_list_string,
			String rPhaseTestSeqSelectedLoadIndexListInStr,
			String yPhaseTestSeqSelectedLoadIndexListInStr,
			String bPhaseTestSeqSelectedLoadIndexListInStr,
			String isAllLoadResistanceSame) {
		ApplicationLauncher.logger.debug("tamperAutoStart3PhaseWithRefStdAndLoad :Entry");
		// String testPointID = tp_ID;//String.format("%02d", tp_ID);
		/*
		 * if(ConstantProTamp.PROTAMP_SINGLE_PHASE){
		 * testPointID = testPointID.substring(1);
		 * ApplicationLauncher.logger.
		 * info("tamperManualStart :ManualMode: testPointID: "+ testPointID);
		 * }
		 */
		// testPointID = new Integer(testPointID).toString(); // remove the LeadingZero

		String TARGET_URL = ServerSettingController.getRootUrl()
				+ "/lsTamper3PhaseAutoStartWithRefStdAndLoad";

		List<Param> formParams = new ArrayList<>();
		formParams.add(new Param("occtime", occur_time_in_sec));
		formParams.add(new Param("restime", restore_time_in_sec));
		formParams.add(new Param("nooftestpulses", nooftestpulses));
		formParams.add(new Param("noOfReadingSamples", noOfReadingSamples));
		formParams.add(new Param("userselectedTClist", final_testcase_selected_list_string));
		formParams.add(new Param("r_ManageResistanceIndexList", rPhaseTestSeqSelectedLoadIndexListInStr));
		formParams.add(new Param("y_ManageResistanceIndexList", yPhaseTestSeqSelectedLoadIndexListInStr));
		formParams.add(new Param("b_ManageResistanceIndexList", bPhaseTestSeqSelectedLoadIndexListInStr));
		formParams.add(new Param("allResistanceLoadSame", isAllLoadResistanceSame));
		Request req = new RequestBuilder("POST")
				.setUrl(TARGET_URL)
				.addHeader("Content-Type", "application/x-www-form-urlencoded")
				// .addHeader("Content-Type", "application/json")
				// .addHeader("content-type", "application/json")
				/*
				 * .addParameter("testid", testPointID)
				 * .addParameter("meterconstant", meterconstant)
				 * 
				 */
				/*
				 * .addParameter("primaryCommand", "*idn?\r\n")
				 * .addParameter("expectedResponse", nooftestpulses)
				 * .addParameter("failureResponse", nooftestpulses)
				 * .addParameter("failBackCommand", nooftestpulses)
				 */
				/*
				 * .addParameter("occtime", occur_time_in_sec)
				 * .addParameter("restime", restore_time_in_sec)
				 * .addParameter("nooftestpulses", nooftestpulses)
				 * .addParameter("noOfReadingSamples", noOfReadingSamples)
				 * .addParameter("userselectedTClist", final_testcase_selected_list_string)
				 * 
				 * .addParameter("r_ManageResistanceIndexList",
				 * rPhaseTestSeqSelectedLoadIndexListInStr)
				 * .addParameter("y_ManageResistanceIndexList",
				 * yPhaseTestSeqSelectedLoadIndexListInStr)
				 * .addParameter("b_ManageResistanceIndexList",
				 * bPhaseTestSeqSelectedLoadIndexListInStr)
				 * .addParameter("allResistanceLoadSame", isAllLoadResistanceSame)
				 */
				.setFormParams(formParams)
				.build();
		asyncClient.lsTamperAuto3PhaseStartWithRefStd(req);
	}

	public boolean tamperRefStdSetPrerequisite(String primaryCommand, String primaryExpectedResponse,
			String secondaryCommand, String secondaryExpectedResponse) {
		boolean status = false;

		String TARGET_URL = ServerSettingController.getRootUrl()
				+ "/tamperRefStdSetPrerequisite";

		List<Param> formParams = new ArrayList<>();
		formParams.add(new Param("primaryCommand", primaryCommand));
		formParams.add(new Param("primaryExpectedResponse", primaryExpectedResponse));
		formParams.add(new Param("secondaryCommand", secondaryCommand));
		formParams.add(new Param("secondaryExpectedResponse", secondaryExpectedResponse));
		Request req = new RequestBuilder("POST")
				.setUrl(TARGET_URL)
				.addHeader("Content-Type", "application/x-www-form-urlencoded")
				// .addHeader("Content-Type", "application/json")
				// .addHeader("content-type", "application/json")
				/*
				 * .addParameter("primaryCommand", primaryCommand)
				 * .addParameter("primaryExpectedResponse", primaryExpectedResponse)
				 * .addParameter("secondaryCommand", secondaryCommand)
				 * .addParameter("secondaryExpectedResponse", secondaryExpectedResponse)
				 */
				.setFormParams(formParams)
				.build();
		setResponseReceived(false);
		status = asyncClient.lsTamperRefStdPreRequisite(req);
		status = waitForPrerequisiteResponse();
		return status;

	}

	private boolean waitForPrerequisiteResponse() {

		int retryCount = 5;

		while ((retryCount > 0) && !isResponseReceived()) {
			ApplicationLauncher.logger.debug("waitForPrerequisiteResponse : waiting retry count :" + retryCount);
			Sleep(1000);
			retryCount = retryCount - 1;
		}
		ApplicationLauncher.logger.debug("waitForPrerequisiteResponse : waiting retry exit :");
		return isResponseReceived();
	}

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep :InterruptedException:" + e.getMessage());
		}

	}

	public void tamperManualStart(String tp_ID, String meterconstant, String nooftestpulses) {
		ApplicationLauncher.logger.debug("tamperManualStart :Entry");
		String testPointID = tp_ID;// String.format("%02d", tp_ID);
		/*
		 * if(ConstantProTamp.PROTAMP_SINGLE_PHASE){
		 * testPointID = testPointID.substring(1);
		 * ApplicationLauncher.logger.
		 * info("tamperManualStart :ManualMode: testPointID: "+ testPointID);
		 * }
		 */
		testPointID = new Integer(testPointID).toString(); // remove the LeadingZero
		String TARGET_URL = ServerSettingController.getRootUrl()
				+ "/lstamperManualStart";

		List<Param> formParams = new ArrayList<>();
		formParams.add(new Param("testid", testPointID));
		formParams.add(new Param("meterconstant", meterconstant));
		formParams.add(new Param("nooftestpulses", nooftestpulses));
		Request req = new RequestBuilder("POST")
				.setUrl(TARGET_URL)
				.addHeader("Content-Type", "application/x-www-form-urlencoded")
				/*
				 * .addParameter("testid", testPointID)
				 * .addParameter("meterconstant", meterconstant)
				 * .addParameter("nooftestpulses", nooftestpulses)
				 */
				.setFormParams(formParams)
				.build();
		asyncClient.lstamper_Manual_Start(req);
	}

	public void tamperManualStart50(String tp_ID, String oncycle_time_in_sec, String offcycle_time_in_sec,
			String no_of_cycle, String meterconstant, String nooftestpulses) {

		ApplicationLauncher.logger.debug("tamperManualStart50 : Entry");
		String testPointID = tp_ID;// String.format("%02d", tp_ID);
		if (ConstantProTamp.PROTAMP_SINGLE_PHASE) {
			testPointID = testPointID.substring(1);
			ApplicationLauncher.logger.info("tamperManualStart :ManualMode: testPointID :" + testPointID);
		}
		String TARGET_URL = ServerSettingController.getRootUrl()
				+ "/lstamperManualStart50";

		List<Param> formParams = new ArrayList<>();
		formParams.add(new Param("testid", testPointID));
		formParams.add(new Param("oncycle", oncycle_time_in_sec));
		formParams.add(new Param("offcycle", offcycle_time_in_sec));
		formParams.add(new Param("noofcycles", no_of_cycle));
		formParams.add(new Param("meterconstant", meterconstant));
		formParams.add(new Param("nooftestpulses", nooftestpulses));
		Request req = new RequestBuilder("POST")
				.setUrl(TARGET_URL)
				.addHeader("Content-Type", "application/x-www-form-urlencoded")
				/*
				 * .addParameter("testid", testPointID)
				 * .addParameter("oncycle", oncycle_time_in_sec)
				 * .addParameter("offcycle", offcycle_time_in_sec)
				 * .addParameter("noofcycles", no_of_cycle)
				 * .addParameter("meterconstant", meterconstant)
				 * .addParameter("nooftestpulses", nooftestpulses)
				 */
				.setFormParams(formParams)
				.build();
		asyncClient.lstamper_Manual_Start50(req);
	}

	public void tamperManualStart51(String tp_ID, String oncycle_time_in_sec, String offcycle_time_in_sec,
			String no_of_cycle, String meterconstant, String nooftestpulses) {

		ApplicationLauncher.logger.debug("tamperManualStart51 : Entry");
		String testPointID = tp_ID;// String.format("%02d", tp_ID);
		if (ConstantProTamp.PROTAMP_SINGLE_PHASE) {
			testPointID = testPointID.substring(1);
			ApplicationLauncher.logger.info("tamperManualStart :ManualMode: testPointID: " + testPointID);
		}
		String TARGET_URL = ServerSettingController.getRootUrl()
				+ "/lstamperManualStart51";

		List<Param> formParams = new ArrayList<>();
		formParams.add(new Param("testid", testPointID));
		formParams.add(new Param("oncycle", oncycle_time_in_sec));
		formParams.add(new Param("offcycle", offcycle_time_in_sec));
		formParams.add(new Param("noofcycles", no_of_cycle));
		formParams.add(new Param("meterconstant", meterconstant));
		formParams.add(new Param("nooftestpulses", nooftestpulses));
		Request req = new RequestBuilder("POST")
				.setUrl(TARGET_URL)
				.addHeader("Content-Type", "application/x-www-form-urlencoded")
				/*
				 * .addParameter("testid", testPointID)
				 * .addParameter("oncycle", oncycle_time_in_sec)
				 * .addParameter("offcycle", offcycle_time_in_sec)
				 * .addParameter("noofcycles", no_of_cycle)
				 * .addParameter("meterconstant", meterconstant)
				 * .addParameter("nooftestpulses", nooftestpulses)
				 */
				.setFormParams(formParams)
				.build();
		asyncClient.lstamper_Manual_Start51(req);
	}

	public void tamper3PhaseManageLoadResistanceBank(String tp_ID, String rPhaseManageResistanceIndex,
			String yPhaseManageResistanceIndex,
			String bPhaseManageResistanceIndex) {

		ApplicationLauncher.logger.debug("tamper3PhaseManageLoadResistanceBank : Entry");
		String testPointID = tp_ID;// String.format("%02d", tp_ID);
		if (ConstantProTamp.PROTAMP_SINGLE_PHASE) {
			testPointID = testPointID.substring(1);
			ApplicationLauncher.logger
					.info("tamper3PhaseManageLoadResistanceBank :ManualMode: testPointID :" + testPointID);
		}
		ApplicationLauncher.logger.debug("tamper3PhaseManageLoadResistanceBank : testPointID: " + testPointID);
		String TARGET_URL = ServerSettingController.getRootUrl()
				+ "/lstamperManageLoadResistance";

		List<Param> formParams = new ArrayList<>();
		formParams.add(new Param("testid", testPointID));
		formParams.add(new Param("r_ManageResistanceIndex", rPhaseManageResistanceIndex));
		formParams.add(new Param("y_ManageResistanceIndex", yPhaseManageResistanceIndex));
		formParams.add(new Param("b_ManageResistanceIndex", bPhaseManageResistanceIndex));
		Request req = new RequestBuilder("POST")
				.setUrl(TARGET_URL)
				.addHeader("Content-Type", "application/x-www-form-urlencoded")
				/*
				 * .addParameter("testid", testPointID)
				 * .addParameter("r_ManageResistanceIndex", rPhaseManageResistanceIndex)
				 * .addParameter("y_ManageResistanceIndex", yPhaseManageResistanceIndex)
				 * .addParameter("b_ManageResistanceIndex", bPhaseManageResistanceIndex)
				 */
				.setFormParams(formParams)
				.build();
		asyncClient.lstamperManageLoadResistanceBank(req);
	}

	public void tamperAutoStart(String oncycle_time_in_sec, String offcycle_time_in_sec,
			String no_of_cycle, String occur_time_in_sec, String restore_time_in_sec, String frequency,
			String meterconstant, String nooftestpulses, String final_testcase_selected_list_string) {

		ApplicationLauncher.logger.debug("tamperAutoStart : Entry");
		String TARGET_URL = ServerSettingController.getRootUrl()
				+ "/lstamperAutoStart";

		List<Param> formParams = new ArrayList<>();
		formParams.add(new Param("oncycle", oncycle_time_in_sec));
		formParams.add(new Param("offcycle", offcycle_time_in_sec));
		formParams.add(new Param("noofcycles", no_of_cycle));
		formParams.add(new Param("occtime", occur_time_in_sec));
		formParams.add(new Param("restime", restore_time_in_sec));
		formParams.add(new Param("freq", frequency));
		formParams.add(new Param("meterconstant", meterconstant));
		formParams.add(new Param("nooftestpulses", nooftestpulses));
		formParams.add(new Param("userselectedTClist", final_testcase_selected_list_string));
		Request req = new RequestBuilder("POST")
				.setUrl(TARGET_URL)
				.addHeader("Content-Type", "application/x-www-form-urlencoded")
				/*
				 * .addParameter("oncycle", oncycle_time_in_sec)
				 * .addParameter("offcycle", offcycle_time_in_sec)
				 * .addParameter("noofcycles", no_of_cycle)
				 * .addParameter("occtime", occur_time_in_sec)
				 * .addParameter("restime", restore_time_in_sec)
				 * .addParameter("freq", frequency)
				 * .addParameter("meterconstant", meterconstant)
				 * .addParameter("nooftestpulses", nooftestpulses)
				 * .addParameter("userselectedTClist", final_testcase_selected_list_string)
				 */
				.setFormParams(formParams)
				.build();

		asyncClient.lstamper_Auto_Start(req);
	}

	public void tamperAutoResume(String oncycle_time_in_sec, String offcycle_time_in_sec,
			String no_of_cycle, String occur_time_in_sec, String restore_time_in_sec, String frequency,
			String meterconstant, String nooftestpulses, String final_testcase_selected_list_string) {

		ApplicationLauncher.logger.debug("tamperAutoResume : Entry");
		String TARGET_URL = ServerSettingController.getRootUrl()
				+ "/lstamperAutoResume";

		List<Param> formParams = new ArrayList<>();
		formParams.add(new Param("oncycle", oncycle_time_in_sec));
		formParams.add(new Param("offcycle", offcycle_time_in_sec));
		formParams.add(new Param("noofcycles", no_of_cycle));
		formParams.add(new Param("occtime", occur_time_in_sec));
		formParams.add(new Param("restime", restore_time_in_sec));
		formParams.add(new Param("freq", frequency));
		formParams.add(new Param("meterconstant", meterconstant));
		formParams.add(new Param("nooftestpulses", nooftestpulses));
		formParams.add(new Param("userselectedTClist", final_testcase_selected_list_string));
		Request req = new RequestBuilder("POST")
				.setUrl(TARGET_URL)
				.addHeader("Content-Type", "application/x-www-form-urlencoded")
				/*
				 * .addParameter("oncycle", oncycle_time_in_sec)
				 * .addParameter("offcycle", offcycle_time_in_sec)
				 * .addParameter("noofcycles", no_of_cycle)
				 * .addParameter("occtime", occur_time_in_sec)
				 * .addParameter("restime", restore_time_in_sec)
				 * .addParameter("freq", frequency)
				 * .addParameter("meterconstant", meterconstant)
				 * .addParameter("nooftestpulses", nooftestpulses)
				 * .addParameter("userselectedTClist", final_testcase_selected_list_string)
				 */
				.setFormParams(formParams)
				.build();

		asyncClient.lstamper_Auto_Resume(req);
	}

	public static boolean isResponseReceived() {
		return responseReceived;
	}

	public static void setResponseReceived(boolean responseReceived) {
		AsyncClientManager.responseReceived = responseReceived;
	}
}
