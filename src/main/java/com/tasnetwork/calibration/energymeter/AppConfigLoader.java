package com.tasnetwork.calibration.energymeter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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

			String terminalFileName = ConstantAppConfigReader.getString("conveyor", "TerminalConfigFolderName");
			if (terminalFileName != null) com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig.TERMINAL_CONFIG_FILE_NAME = terminalFileName;

			String terminalFilePath = ConstantAppConfigReader.getString("conveyor", "TerminalConfigFolderPath");
			if (terminalFilePath != null) com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig.TERMINAL_CONFIG_FILE_PATH = terminalFilePath;

			String myTerminalId = ConstantAppConfigReader.getString("conveyor", "MyTerminalId");
			if (myTerminalId != null) com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig.MY_TERMINAL_ID = myTerminalId;

		} catch (Exception e) {
			e.printStackTrace();
			com.tasnetwork.calibration.energymeter.ApplicationLauncher.logger
					.error("LoadConfigProperty : Exception2: " + e.getMessage());
		}
	}
}
