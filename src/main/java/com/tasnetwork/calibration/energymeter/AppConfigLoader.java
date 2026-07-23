package com.tasnetwork.calibration.energymeter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfigReader;
import com.tasnetwork.calibration.energymeter.constant.ConstantMasterConfig;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
import com.tasnetwork.spring.config.ConfigLoader;
import com.tasnetwork.spring.config.MasterConfig;

public class AppConfigLoader {
	public static void LoadPropertiesFromDB() {
		com.tasnetwork.calibration.energymeter.ApplicationLauncher.logger.debug("LoadPropertiesFromDB :Entry");
		ConstantAppConfig.SECOND_VALIDATION_RETRY_COUNT = ConstantAppConfigReader.getInt("StablizationValidation",
				"DefaultRefStandardValidationRetryCount");
		ConstantAppConfig.SECOND_VALIDATION_WAIT_TIME = ConstantAppConfigReader.getInt("StablizationValidation",
				"DefaultRefStandardValidationWaitTimeInSec");
		ConstantAppConfig.VOLT_ACCEPTED_PERCENTAGE = ConstantAppConfigReader.getInt("AcceptedErrorPercentage",
				ConstantApp.VOLTAGE_ACCEPTED_PERCENTAGE_KEY);
		ConstantAppConfig.CURRENT_ACCEPTED_PERCENTAGE = ConstantAppConfigReader.getInt("AcceptedErrorPercentage",
				ConstantApp.CURRENT_ACCEPTED_PERCENTAGE_KEY);
		ConstantAppConfig.DEGREE_ACCEPTED_PERCENTAGE = ConstantAppConfigReader.getInt("AcceptedErrorPercentage",
				ConstantApp.PHASE_ACCEPTED_PERCENTAGE_KEY);
		ConstantAppConfig.FREQUENCY_ACCEPTED_PERCENTAGE = ConstantAppConfigReader.getFloat("AcceptedErrorPercentage",
				ConstantApp.FREQUENCY_ACCEPTED_PERCENTAGE_KEY);
		ConstantAppConfig.HAR_VOLT_ACCEPTED_PERCENTAGE = ConstantAppConfigReader.getInt("AcceptedErrorPercentage",
				ConstantApp.HAR_VOLTAGE_ACCEPTED_PERCENTAGE_KEY);
		ConstantAppConfig.HAR_CURRENT_ACCEPTED_PERCENTAGE = ConstantAppConfigReader.getInt("AcceptedErrorPercentage",
				ConstantApp.HAR_CURRENT_ACCEPTED_PERCENTAGE_KEY);

		JSONObject system_properties = MySQL_Controller.sp_getsystem_config();
		try {
			if (system_properties.length() > 0) {
				JSONArray properties_arr = system_properties.getJSONArray("Properties");
				com.tasnetwork.calibration.energymeter.ApplicationLauncher.logger
						.debug("LoadPropertiesFromDB :properties_arr" + properties_arr.length());
				if (properties_arr.length() == ConstantApp.TOTAL_NO_OF_SYSTEM_CONFIG_KEY) {
					JSONObject jobj = new JSONObject();
					for (int i = 0; i < properties_arr.length(); i++) {
						jobj = properties_arr.getJSONObject(i);
						String property_name = jobj.getString("property");
						String property_value = jobj.getString("value");
						setCorrespondingProperty(property_name, property_value);
						com.tasnetwork.calibration.energymeter.ApplicationLauncher.logger
								.debug("LoadPropertiesFromDB :property_name:" + property_name);
						com.tasnetwork.calibration.energymeter.ApplicationLauncher.logger
								.debug("LoadPropertiesFromDB :property_value:" + property_value);

					}
				}
			}

		} catch (JSONException e) {
			com.tasnetwork.calibration.energymeter.ApplicationLauncher.logger
					.error("LoadPropertiesFromDB :property_value: JSONException" + e.getMessage());

			e.printStackTrace();
		}
	}

	public static void setCorrespondingProperty(String property_name, String property_value) {
		switch (property_name) {

			case ConstantApp.VOLTAGE_ACCEPTED_PERCENTAGE_KEY:
				ConstantAppConfig.VOLT_ACCEPTED_PERCENTAGE = Integer.parseInt(property_value);
				break;

			case ConstantApp.CURRENT_ACCEPTED_PERCENTAGE_KEY:
				ConstantAppConfig.CURRENT_ACCEPTED_PERCENTAGE = Integer.parseInt(property_value);
				break;

			case ConstantApp.PHASE_ACCEPTED_PERCENTAGE_KEY:
				ConstantAppConfig.DEGREE_ACCEPTED_PERCENTAGE = Integer.parseInt(property_value);
				com.tasnetwork.calibration.energymeter.ApplicationLauncher.logger
						.info("setCorrespondingProperty : DEGREE_ACCEPTED_PERCENTAGE:"
								+ ConstantAppConfig.DEGREE_ACCEPTED_PERCENTAGE);
				break;

			case ConstantApp.FREQUENCY_ACCEPTED_PERCENTAGE_KEY:
				ConstantAppConfig.FREQUENCY_ACCEPTED_PERCENTAGE = Float.parseFloat(property_value);
				break;

			case ConstantApp.HAR_VOLTAGE_ACCEPTED_PERCENTAGE_KEY:
				ConstantAppConfig.HAR_VOLT_ACCEPTED_PERCENTAGE = Integer.parseInt(property_value);
				break;

			case ConstantApp.HAR_CURRENT_ACCEPTED_PERCENTAGE_KEY:
				ConstantAppConfig.HAR_CURRENT_ACCEPTED_PERCENTAGE = Integer.parseInt(property_value);
				break;

			case ConstantApp.SECOND_VALIDATION_RETRY_COUNT_KEY:
				ConstantAppConfig.SECOND_VALIDATION_RETRY_COUNT = Integer.parseInt(property_value);
				break;

			case ConstantApp.SECOND_VALIDATION_WAIT_TIME_IN_SEC_KEY:
				ConstantAppConfig.SECOND_VALIDATION_WAIT_TIME = Integer.parseInt(property_value);
				break;

			default:
				break;
		}
	}

	public static void LoadConfigProperty() {

		ConfigLoader configLoader = ApplicationLauncher.springContext.getBean(ConfigLoader.class);
		MasterConfig masterConfig = configLoader.getMasterConfig();
		ApplicationLauncher.logger
				.info("LoadConfigProperty: getConfigFileVersion: " + masterConfig.getConfigFileVersion());
		try {
			ConstantMasterConfig.APP_CONFIG_CUSTOMER_FILE_NAME = masterConfig.getCustomerDetails()
					.getAppConfigFileName();
			ApplicationLauncher.logger.info("LoadConfigProperty : APP_CONFIG_CUSTOMER_FILE_NAME: "
					+ ConstantMasterConfig.APP_CONFIG_CUSTOMER_FILE_NAME);

			ConstantMasterConfig.CONFIG_FILE_PATH = masterConfig.getCustomerDetails().getConfigFilePath();
			ApplicationLauncher.logger
					.info("LoadConfigProperty : CONFIG_FILE_PATH: " + ConstantMasterConfig.CONFIG_FILE_PATH);

			ConstantAppConfigReader.setAppConfigFilePathName(ConstantMasterConfig.APP_CONFIG_CUSTOMER_FILE_NAME);
			ConstantAppConfigReader.init(); 

			ConstantAppConfig.DB_URL = ConstantAppConfigReader.getString("database", "url");
			ConstantAppConfig.DB_URL_TAIL_OPTION = ConstantAppConfigReader.getString("database", "urlTailOption");
			ConstantAppConfig.DB_USERNAME = ConstantAppConfigReader.getString("database", "username");
			ConstantAppConfig.DB_PASSWORD = ConstantAppConfigReader.getString("database", "password");
			ConstantAppConfig.DB_NAME = ConstantAppConfigReader.getString("database", "db_name");
			ConstantAppConfig.SQL_LOCATION = ConstantAppConfigReader.getString("database", "DefaultMySQL_InstalledLocation");
			ConstantAppConfig.SQL_BACKUP_LOCATION = ConstantAppConfigReader.getString("database", "DefaultBackupLocation");
			
			Integer totalReportProfile = ConstantAppConfigReader.getInt("Report", "TotalReportProfile");
			if (totalReportProfile != null && totalReportProfile > 0) {
				ConstantAppConfig.REPORT_PROFILE_LIST.clear();
				for (int i = 1; i <= totalReportProfile; i++) {
					String reportProfile = ConstantAppConfigReader.getString("Report", ("ReportProfile" + i));
					if (reportProfile != null) {
						ConstantAppConfig.REPORT_PROFILE_LIST.add(reportProfile);
					}
				}
			}
			
			ApplicationLauncher.logger.info("LoadConfigProperty : Database config loaded successfully.");
			
			String pSrc = ConstantAppConfigReader.getString("app", "PowerSrc");
			if (pSrc != null) ConstantAppConfig.POWERSRC = pSrc;
			String rStd = ConstantAppConfigReader.getString("app", "RefStd");
			if (rStd != null) ConstantAppConfig.REFSTD = rStd;
			String ldu = ConstantAppConfigReader.getString("app", "LDU");
			if (ldu != null) ConstantAppConfig.LDU = ldu;
			String ict = ConstantAppConfigReader.getString("app", "ICT");
			if (ict != null) ConstantAppConfig.ICT = ict;
			String dut = ConstantAppConfigReader.getString("app", "DUT");
			if (dut != null) ConstantAppConfig.DUT = dut;
			String hSrc = ConstantAppConfigReader.getString("app", "HarmonicsSrc");
			if (hSrc != null) ConstantAppConfig.HARMONICS_SRC = hSrc;
			
			Long sw = ConstantAppConfigReader.getLong("app", "ScreenWidthThreshold");
			if (sw != null) ConstantAppConfig.ScreenWidthThreshold = sw;
			Long sh = ConstantAppConfigReader.getLong("app", "ScreenHeightThreshold");
			if (sh != null) ConstantAppConfig.ScreenHeightThreshold = sh;
			Long dlf = ConstantAppConfigReader.getLong("app", "HoldLogFilesforX_NoOfPreviousDays");
			if (dlf != null) ConstantAppConfig.DeleteLogFilesforX_NoOfPreviousDays = dlf;

			String refStdFileName = ConstantAppConfigReader.getString("ConfigFileName", "RefStdConstantConfigFileName");
			if (refStdFileName != null) ConstantAppConfig.REFSTD_CONSTANT_CONFIG_FILE_NAME = refStdFileName;

			String refStdFilePath = ConstantAppConfigReader.getString("ConfigFilePathLocation", "RefStdConstantConfigFolderPath");
			if (refStdFilePath != null) ConstantAppConfig.REFSTD_CONSTANT_CONFIG_FILE_PATH = refStdFilePath;

			String reportFileName = ConstantAppConfigReader.getString("ConfigFileName", "ReportConfigFileName");
			if (reportFileName != null) ConstantAppConfig.REPORT_CONFIG_FILE_NAME = reportFileName;

			String reportFilePath = ConstantAppConfigReader.getString("ConfigFilePathLocation", "ReportConfigFolderPath");
			if (reportFilePath != null) ConstantAppConfig.REPORT_CONFIG_FILE_PATH = reportFilePath;

			String reportProfileFileName = ConstantAppConfigReader.getString("ConfigFileName", "ReportProfileV2FileName");
			if (reportProfileFileName != null) ConstantAppConfig.REPORT_PROFILEV2_FILE_NAME = reportProfileFileName;

			String reportProfileFilePath = ConstantAppConfigReader.getString("ConfigFilePathLocation", "ReportProfileFolderPath");
			if (reportProfileFilePath != null) ConstantAppConfig.REPORT_PROFILEV2_FILE_PATH = reportProfileFilePath;

			String conveyorFileName = ConstantAppConfigReader.getString("ConfigFileName", "ConveyorProfileConfigFileName");
			if (conveyorFileName != null) ConstantAppConfig.CONVEYOR_CONFIG_FILE_NAME = conveyorFileName;

			String conveyorFilePath = ConstantAppConfigReader.getString("ConfigFilePathLocation", "ConveyorProfileFolderPath");
			if (conveyorFilePath != null) ConstantAppConfig.CONVEYOR_CONFIG_FILE_PATH = conveyorFilePath;

			String calibFileName = ConstantAppConfigReader.getString("ConfigFileName", "ProPowerCalibrationFileName");
			if (calibFileName != null) ConstantAppConfig.LSCS_POWER_SOURCE_CALIBRATION_FILE_NAME = calibFileName;

			String calibFilePath = ConstantAppConfigReader.getString("ConfigFilePathLocation", "ProPowerCalibrationFolderPath");
			if (calibFilePath != null) ConstantAppConfig.LSCS_POWER_SOURCE_CALIBRATION_FILE_PATH = calibFilePath;

			// --- Conveyor General Config ---
			String terminalFileName = ConstantAppConfigReader.getString("conveyor", "TerminalConfigFolderName");
			if (terminalFileName != null) ConstantConveyorConfig.TERMINAL_CONFIG_FILE_NAME = terminalFileName;

			String terminalFilePath = ConstantAppConfigReader.getString("conveyor", "TerminalConfigFolderPath");
			if (terminalFilePath != null) ConstantConveyorConfig.TERMINAL_CONFIG_FILE_PATH = terminalFilePath;

			String myTerminalId = ConstantAppConfigReader.getString("conveyor", "MyTerminalId");
			if (myTerminalId != null) ConstantConveyorConfig.MY_TERMINAL_ID = myTerminalId;

			Boolean conveyorDebugEnabled = ConstantAppConfigReader.getBoolean("conveyor", "ConveyorDebugScreenDisplayEnabled");
			ConstantConveyorConfig.CONVEYOR_DEBUG_SCREEN_DISPLAY_ENABLED = conveyorDebugEnabled;

			Integer palletManageDays = ConstantAppConfigReader.getInt("conveyor", "PalletManageRecentNoOfDaysDisplay");
			if (palletManageDays != null && palletManageDays > 0) ConstantConveyorConfig.PALLET_MANAGE_RECENT_NO_OF_DAYS_DISPLAY = palletManageDays;

			Integer waitingToVerificSec = ConstantAppConfigReader.getInt("conveyor", "WaitingToVerificBayPalletMovementWaitTimeInSec");
			if (waitingToVerificSec != null && waitingToVerificSec > 0) ConstantConveyorConfig.WAITING_TO_VERIFIC_BAY_PALLET_MOVEMENT_WAIT_TIME_IN_SEC = waitingToVerificSec;

			Integer verificToSta1Sec = ConstantAppConfigReader.getInt("conveyor", "VerificToSta1BayPalletMovementWaitTimeInSec");
			if (verificToSta1Sec != null && verificToSta1Sec > 0) ConstantConveyorConfig.VERIFIC_TO_STA1_BAY_PALLET_MOVEMENT_WAIT_TIME_IN_SEC = verificToSta1Sec;

			Integer verificToSta2Sec = ConstantAppConfigReader.getInt("conveyor", "VerificToSta2BayPalletMovementWaitTimeInSec");
			if (verificToSta2Sec != null && verificToSta2Sec > 0) ConstantConveyorConfig.VERIFIC_TO_STA2_BAY_PALLET_MOVEMENT_WAIT_TIME_IN_SEC = verificToSta2Sec;

			Integer sta1ToUnloadingSec = ConstantAppConfigReader.getInt("conveyor", "Sta1ToUnloadingBayPalletMovementWaitTimeInSec");
			if (sta1ToUnloadingSec != null && sta1ToUnloadingSec > 0) ConstantConveyorConfig.STA1_TO_UNLOADING_BAY_PALLET_MOVEMENT_WAIT_TIME_IN_SEC = sta1ToUnloadingSec;

			Integer sta2ToUnloadingSec = ConstantAppConfigReader.getInt("conveyor", "Sta2ToUnloadingBayPalletMovementWaitTimeInSec");
			if (sta2ToUnloadingSec != null && sta2ToUnloadingSec > 0) ConstantConveyorConfig.STA2_TO_UNLOADING_BAY_PALLET_MOVEMENT_WAIT_TIME_IN_SEC = sta2ToUnloadingSec;

			// --- Conveyor ProCAL Calib Config ---
			String calibIp = ConstantAppConfigReader.getString("conveyor_procal_calib", "IpAddress");
			if (calibIp != null) ConstantConveyorConfig.CONVEYOR_PROCAL_CALIB_IP_ADDRESS = calibIp;
			String calibPort = ConstantAppConfigReader.getString("conveyor_procal_calib", "IpPort");
			if (calibPort != null) ConstantConveyorConfig.CONVEYOR_PROCAL_CALIB_IP_PORT = calibPort;
			String calibEndPath = ConstantAppConfigReader.getString("conveyor_procal_calib", "targetEndPath");
			if (calibEndPath != null) ConstantConveyorConfig.CONVEYOR_PROCAL_CALIB_END_PATH = calibEndPath;
			String calibProjName = ConstantAppConfigReader.getString("conveyor_procal_calib", "ProjectName");
			if (calibProjName != null) ConstantConveyorConfig.CONVEYOR_PROCAL_CALIB_PROJECT_NAME = calibProjName;

			// --- Conveyor ProCAL Verific Config ---
			String verificIp = ConstantAppConfigReader.getString("conveyor_procal_verific", "IpAddress");
			if (verificIp != null) ConstantConveyorConfig.CONVEYOR_PROCAL_VERIFIC_IP_ADDRESS = verificIp;
			String verificPort = ConstantAppConfigReader.getString("conveyor_procal_verific", "IpPort");
			if (verificPort != null) ConstantConveyorConfig.CONVEYOR_PROCAL_VERIFIC_IP_PORT = verificPort;
			String verificEndPath = ConstantAppConfigReader.getString("conveyor_procal_verific", "targetEndPath");
			if (verificEndPath != null) ConstantConveyorConfig.CONVEYOR_PROCAL_VERIFIC_END_PATH = verificEndPath;
			String verificCmdLoc = ConstantAppConfigReader.getString("conveyor_procal_verific", "CmdLocation");
			if (verificCmdLoc != null) ConstantConveyorConfig.CONVEYOR_PROCAL_VERIFIC_CMD_LOCATION = verificCmdLoc;
			String verificJar = ConstantAppConfigReader.getString("conveyor_procal_verific", "JarFile");
			if (verificJar != null) ConstantConveyorConfig.CONVEYOR_PROCAL_VERIFIC_JAR_FILE = verificJar;
			String verificProjName = ConstantAppConfigReader.getString("conveyor_procal_verific", "ProjectName");
			if (verificProjName != null) ConstantConveyorConfig.CONVEYOR_PROCAL_VERIFIC_PROJECT_NAME = verificProjName;

			// --- Conveyor ProCAL STA1 Config ---
			String sta1Ip = ConstantAppConfigReader.getString("conveyor_procal_sta1", "IpAddress");
			if (sta1Ip != null) ConstantConveyorConfig.CONVEYOR_PROCAL_STA1_IP_ADDRESS = sta1Ip;
			String sta1Port = ConstantAppConfigReader.getString("conveyor_procal_sta1", "IpPort");
			if (sta1Port != null) ConstantConveyorConfig.CONVEYOR_PROCAL_STA1_IP_PORT = sta1Port;
			String sta1EndPath = ConstantAppConfigReader.getString("conveyor_procal_sta1", "targetEndPath");
			if (sta1EndPath != null) ConstantConveyorConfig.CONVEYOR_PROCAL_STA1_END_PATH = sta1EndPath;
			String sta1CmdLoc = ConstantAppConfigReader.getString("conveyor_procal_sta1", "CmdLocation");
			if (sta1CmdLoc != null) ConstantConveyorConfig.CONVEYOR_PROCAL_STA1_CMD_LOCATION = sta1CmdLoc;
			String sta1Jar = ConstantAppConfigReader.getString("conveyor_procal_sta1", "JarFile");
			if (sta1Jar != null) ConstantConveyorConfig.CONVEYOR_PROCAL_STA1_JAR_FILE = sta1Jar;
			String sta1ProjName = ConstantAppConfigReader.getString("conveyor_procal_sta1", "ProjectName");
			if (sta1ProjName != null) ConstantConveyorConfig.CONVEYOR_PROCAL_STA1_PROJECT_NAME = sta1ProjName;

			// --- Conveyor ProCAL STA2 Config ---
			String sta2Ip = ConstantAppConfigReader.getString("conveyor_procal_sta2", "IpAddress");
			if (sta2Ip != null) ConstantConveyorConfig.CONVEYOR_PROCAL_STA2_IP_ADDRESS = sta2Ip;
			String sta2Port = ConstantAppConfigReader.getString("conveyor_procal_sta2", "IpPort");
			if (sta2Port != null) ConstantConveyorConfig.CONVEYOR_PROCAL_STA2_IP_PORT = sta2Port;
			String sta2EndPath = ConstantAppConfigReader.getString("conveyor_procal_sta2", "targetEndPath");
			if (sta2EndPath != null) ConstantConveyorConfig.CONVEYOR_PROCAL_STA2_END_PATH = sta2EndPath;
			String sta2CmdLoc = ConstantAppConfigReader.getString("conveyor_procal_sta2", "CmdLocation");
			if (sta2CmdLoc != null) ConstantConveyorConfig.CONVEYOR_PROCAL_STA2_CMD_LOCATION = sta2CmdLoc;
			String sta2Jar = ConstantAppConfigReader.getString("conveyor_procal_sta2", "JarFile");
			if (sta2Jar != null) ConstantConveyorConfig.CONVEYOR_PROCAL_STA2_JAR_FILE = sta2Jar;
			String sta2ProjName = ConstantAppConfigReader.getString("conveyor_procal_sta2", "ProjectName");
			if (sta2ProjName != null) ConstantConveyorConfig.CONVEYOR_PROCAL_STA2_PROJECT_NAME = sta2ProjName;

			// --- Conveyor Rejection Python Config ---
			String rejCmdLoc = ConstantAppConfigReader.getString("conveyor_python_rejection", "CmdLocation");
			if (rejCmdLoc != null) ConstantConveyorConfig.CONVEYOR_REJECTION_CMD_LOCATION = rejCmdLoc;
			String rejPyScript = ConstantAppConfigReader.getString("conveyor_python_rejection", "PythonScript");
			if (rejPyScript != null) ConstantConveyorConfig.CONVEYOR_REJECTION_PYTHON_SCRIPT = rejPyScript;
			String rejPyScriptScreen = ConstantAppConfigReader.getString("conveyor_python_rejection", "PythonScriptScreenNumber");
			if (rejPyScriptScreen != null) ConstantConveyorConfig.CONVEYOR_REJECTION_PYTHON_SCRIPT_SCREEN_NUMBER = rejPyScriptScreen;
			String rejPyExe = ConstantAppConfigReader.getString("conveyor_python_rejection", "PythonExecutablePath");
			if (rejPyExe != null) ConstantConveyorConfig.CONVEYOR_REJECTION_PYTHON_EXECUTABLE_PATH = rejPyExe;
			String rejMonDisp = ConstantAppConfigReader.getString("conveyor_python_rejection", "MonitorDisplayName");
			if (rejMonDisp != null) ConstantConveyorConfig.CONVEYOR_REJECTION_MONITOR_DISPLAY_NAME = rejMonDisp;
			String rejBayDisp = ConstantAppConfigReader.getString("conveyor_python_rejection", "BayNameDisplay");
			if (rejBayDisp != null) ConstantConveyorConfig.CONVEYOR_REJECTION_BAY_DISPLAY_NAME = rejBayDisp;
			String rejPort = ConstantAppConfigReader.getString("conveyor_python_rejection", "port");
			if (rejPort != null) ConstantConveyorConfig.CONVEYOR_REJECTION_PORT = rejPort;
			String rejManu = ConstantAppConfigReader.getString("conveyor_python_rejection", "MonitorManufacturerName");
			if (rejManu != null) ConstantConveyorConfig.CONVEYOR_REJECTION_MONITOR_MANUFACTURER_NAME = rejManu;
			String rejPid = ConstantAppConfigReader.getString("conveyor_python_rejection", "MonitorPidNo");
			if (rejPid != null) ConstantConveyorConfig.CONVEYOR_REJECTION_MONITOR_PID_NO = rejPid;
			String rejSerial = ConstantAppConfigReader.getString("conveyor_python_rejection", "MonitorSerialNo");
			if (rejSerial != null) ConstantConveyorConfig.CONVEYOR_REJECTION_MONITOR_SERIAL_NO = rejSerial;
			Integer rejScreenNum = ConstantAppConfigReader.getInt("conveyor_python_rejection", "ScreenNumber");
			if (rejScreenNum != null && rejScreenNum > 0) ConstantConveyorConfig.CONVEYOR_REJECTION_SCREEN_NUMBER = rejScreenNum;
			String rejClusterId = ConstantAppConfigReader.getString("conveyor_python_rejection", "RejectionBayClusterId");
			if (rejClusterId != null) ConstantConveyorConfig.CONVEYOR_REJECTION_CLUSTER_ID = rejClusterId;
			String rejTailEnd = ConstantAppConfigReader.getString("conveyor_python_rejection", "RejectionServerTailEnd");
			if (rejTailEnd != null) ConstantConveyorConfig.REST_API_TAIL_END_REJECTION_DISPLAY = rejTailEnd;
			String rejBatchTailEnd = ConstantAppConfigReader.getString("conveyor_python_rejection", "RejectionServerBatchUpdateTailEnd");
			if (rejBatchTailEnd != null) ConstantConveyorConfig.REST_API_BATCH_UPDATE_TAIL_END_REJECTION_DISPLAY = rejBatchTailEnd;
			String rejIdleTailEnd = ConstantAppConfigReader.getString("conveyor_python_rejection", "RejectionServerIdleDisplayTailEnd");
			if (rejIdleTailEnd != null) ConstantConveyorConfig.REST_API_IDLE_DISPLAY_TAIL_END_REJECTION_DISPLAY = rejIdleTailEnd;

			// --- Conveyor Unloading Python Config ---
			String unlCmdLoc = ConstantAppConfigReader.getString("conveyor_python_unloading", "CmdLocation");
			if (unlCmdLoc != null) ConstantConveyorConfig.CONVEYOR_UNLOADING_CMD_LOCATION = unlCmdLoc;
			String unlPyScript = ConstantAppConfigReader.getString("conveyor_python_unloading", "PythonScript");
			if (unlPyScript != null) ConstantConveyorConfig.CONVEYOR_UNLOADING_PYTHON_SCRIPT = unlPyScript;
			String unlPyScriptScreen = ConstantAppConfigReader.getString("conveyor_python_unloading", "PythonScriptScreenNumber");
			if (unlPyScriptScreen != null) ConstantConveyorConfig.CONVEYOR_UNLOADING_PYTHON_SCRIPT_SCREEN_NUMBER = unlPyScriptScreen;
			String unlPyExe = ConstantAppConfigReader.getString("conveyor_python_unloading", "PythonExecutablePath");
			if (unlPyExe != null) ConstantConveyorConfig.CONVEYOR_UNLOADING_PYTHON_EXECUTABLE_PATH = unlPyExe;
			String unlMonDisp = ConstantAppConfigReader.getString("conveyor_python_unloading", "MonitorDisplayName");
			if (unlMonDisp != null) ConstantConveyorConfig.CONVEYOR_UNLOADING_MONITOR_DISPLAY_NAME = unlMonDisp;
			String unlBayDisp = ConstantAppConfigReader.getString("conveyor_python_unloading", "BayNameDisplay");
			if (unlBayDisp != null) ConstantConveyorConfig.CONVEYOR_UNLOADING_BAY_DISPLAY_NAME = unlBayDisp;
			String unlPort = ConstantAppConfigReader.getString("conveyor_python_unloading", "port");
			if (unlPort != null) ConstantConveyorConfig.CONVEYOR_UNLOADING_PORT = unlPort;
			String unlManu = ConstantAppConfigReader.getString("conveyor_python_unloading", "MonitorManufacturerName");
			if (unlManu != null) ConstantConveyorConfig.CONVEYOR_UNLOADING_MONITOR_MANUFACTURER_NAME = unlManu;
			String unlPid = ConstantAppConfigReader.getString("conveyor_python_unloading", "MonitorPidNo");
			if (unlPid != null) ConstantConveyorConfig.CONVEYOR_UNLOADING_MONITOR_PID_NO = unlPid;
			String unlSerial = ConstantAppConfigReader.getString("conveyor_python_unloading", "MonitorSerialNo");
			if (unlSerial != null) ConstantConveyorConfig.CONVEYOR_UNLOADING_MONITOR_SERIAL_NO = unlSerial;
			Integer unlScreenNum = ConstantAppConfigReader.getInt("conveyor_python_unloading", "ScreenNumber");
			if (unlScreenNum != null && unlScreenNum > 0) ConstantConveyorConfig.CONVEYOR_UNLOADING_SCREEN_NUMBER = unlScreenNum;
			String unlClusterId = ConstantAppConfigReader.getString("conveyor_python_unloading", "UnloadingBayClusterId");
			if (unlClusterId != null) ConstantConveyorConfig.CONVEYOR_UNLOADING_CLUSTER_ID = unlClusterId;
			String unlTailEnd = ConstantAppConfigReader.getString("conveyor_python_unloading", "UnloadingServerTailEnd");
			if (unlTailEnd != null) ConstantConveyorConfig.REST_API_TAIL_END_UNLOADING_DISPLAY = unlTailEnd;
			String unlBatchTailEnd = ConstantAppConfigReader.getString("conveyor_python_unloading", "UnloadingServerBatchUpdateTailEnd");
			if (unlBatchTailEnd != null) ConstantConveyorConfig.REST_API_BATCH_UPDATE_TAIL_END_UNLOADING_DISPLAY = unlBatchTailEnd;
			String unlIdleTailEnd = ConstantAppConfigReader.getString("conveyor_python_unloading", "RejectionServerIdleDisplayTailEnd");
			if (unlIdleTailEnd != null) ConstantConveyorConfig.REST_API_IDLE_DISPLAY_TAIL_END_UNLOADING_DISPLAY = unlIdleTailEnd;

		} catch (Exception e) {
			e.printStackTrace();
			com.tasnetwork.calibration.energymeter.ApplicationLauncher.logger
					.error("LoadConfigProperty : Exception2: " + e.getMessage());
		}
	}
}
