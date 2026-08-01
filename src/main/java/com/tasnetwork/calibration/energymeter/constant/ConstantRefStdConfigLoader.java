package com.tasnetwork.calibration.energymeter.constant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.WindowManager;

import javafx.scene.control.Alert.AlertType;

public class ConstantRefStdConfigLoader {

	//public static final String CONFIG_FILE = ConstantVersion.RefStdConfigFileName;//"/resources/config.json";
	public static String refStdConstantConfigFileName = ConstantAppConfig.REFSTD_CONSTANT_CONFIG_FILE_NAME;//ConstantConfig.configFilePathName;//"/resources/config.json";

	private static JSONObject properties = null;
	//private static CalibrationParser properties2 = null;

	public static void init() {
		//readJsonConfig();
		readJsonConfigV2();
	}

	public static Object getAttribute(String key) {
		try{
			Object retValue  = properties.get(key);
			if (retValue == null) {
				ApplicationLauncher.logger.error("getAttribute : config file: key:"+ key);
				WindowManager.InformUser("Error-C01","Kindly check key:"+key +" on config file",AlertType.ERROR);

				return null;
			}
			//return properties.get(key);
			return retValue;
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("getAttribute : config file1: key:"+ key);
			ApplicationLauncher.logger.error("getAttribute : Exception:"+ e.getMessage());
			WindowManager.InformUser("Error-C011","Kindly check key:"+key +" on config file\nError:"+e.getMessage(),AlertType.ERROR);

			return null;

		}
	}

	private static Object getAttribute(String section, String key) {
		try{
			JSONObject sectionObj = (JSONObject)properties.get(section);
			if (sectionObj == null) {
				ApplicationLauncher.logger.error("ConstantRefStdConfigLoader: getAttribute : config file: section:"+ section);
				WindowManager.InformUser("ConstantRefStdConfigLoader: Error-C15","Kindly check section:"+section +" on config file",AlertType.ERROR);

				return null;
			}
			return sectionObj.get(key);
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("ConstantRefStdConfigLoader: getAttribute : config file1: section:"+ section);
			ApplicationLauncher.logger.error("ConstantRefStdConfigLoader: getAttribute : Exception:"+ e.getMessage());
			WindowManager.InformUser("ConstantRefStdConfigLoader: Error-C151","Kindly check section:"+section +" on config file\nError:"+e.getMessage(),AlertType.ERROR);

			return null;

		}
	}

	public static String getString(String section, String key) {
		try{
			String retValue = (String) getAttribute(section, key);
			if (retValue == null) {
				ApplicationLauncher.logger.error("ConstantRefStdConfigLoader: getString : config file: section:"+ section);
				ApplicationLauncher.logger.error("ConstantRefStdConfigLoader: getString : config file: key:"+ key);
				//WindowManager.InformUser("Error-C01","Kindly check section:" +section +" and key:"+key +" on config file",AlertType.ERROR);

				return retValue;
			}else{
				return retValue;
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("ConstantRefStdConfigLoader: getString : config file1: section:"+ section);
			ApplicationLauncher.logger.error("ConstantRefStdConfigLoader : getString : config file1: key:"+ key);
			ApplicationLauncher.logger.error("ConstantRefStdConfigLoader : getString : Exception:"+ e.getMessage());
			WindowManager.InformUser("ConstantRefStdConfigLoader : Error-C161","Kindly check section:" +section +" and key:"+key +" on config file\nError:"+e.getMessage(),AlertType.ERROR);

			return null;

		}
	}
	
	public static Float getFloat(String section, String key) {
		try{
			Double retValue = (Double) getAttribute(section, key);
			if (retValue == null) {
				ApplicationLauncher.logger.error("ConstantRefStdConfigLoader: getFloat : config file: section:"+ section);
				ApplicationLauncher.logger.error("ConstantRefStdConfigLoader: getFloat : config file: key:"+ key);
				WindowManager.InformUser("Error-C06","Kindly check section:" +section +" and key:"+key +" on config file",AlertType.ERROR);

				return 0.0F;
			}else{
				return retValue.floatValue();
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("ConstantRefStdConfigLoader: getFloat : config file1: section:"+ section);
			ApplicationLauncher.logger.error("ConstantRefStdConfigLoader: getFloat : config file1: key:"+ key);
			ApplicationLauncher.logger.error("ConstantRefStdConfigLoader: getFloat : Exception:"+ e.getMessage());
			WindowManager.InformUser("Error-C061","Kindly check section:" +section +" and key:"+key +" on config file\nError:"+e.getMessage(),AlertType.ERROR);

			return 0.0F;
			
		}
		//return retValue.floatValue();
	}

	private static void readJsonConfigV2() {
		JSONParser parser = new JSONParser();
		Object obj = null;
		try {

			ApplicationLauncher.logger.debug("ConstantRefStdConfigLoader : Loaded config json CONFIG_FILE:"+ getRefStdConstantConfigFileName());

			//filePathName = filePathName.replace("\\", "/");
			//ApplicationLauncher.logger.debug("Loaded config json filePathName2:"+ filePathName);

			String jarPath = System.getProperty("user.dir");
			ApplicationLauncher.logger.debug("ConstantRefStdConfigLoader : jarPath: "+ jarPath);

			String externalFilePath = Paths.get(jarPath, getRefStdConstantConfigFileName()).toString();
			ApplicationLauncher.logger.debug("ConstantRefStdConfigLoader : externalFilePath: "+ externalFilePath);

			File externalFile = new File(externalFilePath);

			if (externalFile.exists()) {
				// ✅ Load from external folder "config/refstd/config.json"
				ApplicationLauncher.logger.debug("ConstantRefStdConfigLoader : Loading external config: " + externalFilePath);
				obj = parser.parse(new FileReader(externalFile));
			} else {
				// 🔹 Fallback: Load from inside JAR (resources/config/refstd/config.json)
				ApplicationLauncher.logger.debug("ConstantRefStdConfigLoader : External file not found. Loading from JAR resources...");


				InputStream inputStream = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(getRefStdConstantConfigFileName());//"config/app/app_config_elmeasure_jan2025_v1_5.json");

				if (inputStream == null) {
					throw new FileNotFoundException("ConstantRefStdConfigLoader : Config file not found in classpath!");
				}

				obj = parser.parse(new InputStreamReader(inputStream));
				//obj = parser.parse(new InputStreamReader(ConstantRefStdConfigLoader.class.getClass().getResourceAsStream(getRefStdConstantConfigFileName())));
				ApplicationLauncher.logger.debug("ConstantRefStdConfigLoader : Loaded config json obj:"+obj.toString());
			}
			properties = (JSONObject) obj;
			ApplicationLauncher.logger.debug("ConstantRefStdConfigLoader : Loaded config json property:"+properties);


			//properties = (JSONObject) obj;
			//properties2= (CalibrationParser)obj.toString();// new properties2(CalibrationLoader.getAttribute(ConfigFileVersion),);

			//ConstantApp.calibrationParsedData = new CalibrationParser(CalibrationLoader.getAttribute("ConfigFileVersion").toString());
			//Gson gson = new Gson();
			//String yourJson = obj.toString();
			//DeviceDataManagerController.setLscsCalibrationConfigParsedKey(gson.fromJson(yourJson, LscsCalibrationConfigModel.class));
			ApplicationLauncher.logger.debug("ConstantRefStdConfigLoader : Loaded ConstantRefStdConfigLoader json " );
			//ApplicationLauncher.logger.debug("ConstantRefStdConfigLoader : Loaded config json getConfigFileVersion : "+DeviceDataManagerController.getLscsCalibrationConfigParsedKey().getVoltageCalibration().get(0).getVoltagePhase());
			//ApplicationLauncher.logger.debug("ConstantRefStdConfigLoader : Loaded config json getActualErrorValueStartCell : "+DeviceDataManagerController.wordReportConfigParsedData.getCalibAccuracyReport().getActualErrorValueStartCell());


		} catch (Exception e) {
			//System.err.println("Error while reading from " + CONFIG_FILE);
			e.printStackTrace();
			ApplicationLauncher.logger.error("ConstantRefStdConfigLoader : readJsonConfigV2: Exception2:  " + e.getMessage());
			ApplicationLauncher.logger.error("ConstantRefStdConfigLoader : Error while reading from " + getRefStdConstantConfigFileName());


		}
		

	}

	
	public static String getRefStdConstantConfigFileName() {
		return refStdConstantConfigFileName;
	}

	public static void setRefStdConstantConfigFileName(String configFilePath, String configFileName) {
		//ConstantRefStdConfigLoader.refStdConstantConfigFileName  = "/resources/"+ configFileName;
		ConstantRefStdConfigLoader.refStdConstantConfigFileName  = configFilePath + configFileName;
	}
	    


	public static void loadRefStdConstantConfigProperty() {

		ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_1 = ConstantRefStdConfigLoader
				.getFloat("RefStdDeviceConstant", "RSS_HTCT_CurrentThresholdInAmpsLevel_1");
		ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_2 = ConstantRefStdConfigLoader
				.getFloat("RefStdDeviceConstant", "RSS_HTCT_CurrentThresholdInAmpsLevel_2");
		ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_3 = ConstantRefStdConfigLoader
				.getFloat("RefStdDeviceConstant", "RSS_HTCT_CurrentThresholdInAmpsLevel_3");
		ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_4 = ConstantRefStdConfigLoader
				.getFloat("RefStdDeviceConstant", "RSS_HTCT_CurrentThresholdInAmpsLevel_4");
		ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_5 = ConstantRefStdConfigLoader
				.getFloat("RefStdDeviceConstant", "RSS_HTCT_CurrentThresholdInAmpsLevel_5");
		ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_6 = ConstantRefStdConfigLoader
				.getFloat("RefStdDeviceConstant", "RSS_HTCT_CurrentThresholdInAmpsLevel_6");
		ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_7 = ConstantRefStdConfigLoader
				.getFloat("RefStdDeviceConstant", "RSS_HTCT_CurrentThresholdInAmpsLevel_7");
		ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_8 = ConstantRefStdConfigLoader
				.getFloat("RefStdDeviceConstant", "RSS_HTCT_CurrentThresholdInAmpsLevel_8");
		ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_9 = ConstantRefStdConfigLoader
				.getFloat("RefStdDeviceConstant", "RSS_HTCT_CurrentThresholdInAmpsLevel_9");
		ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_10 = ConstantRefStdConfigLoader
				.getFloat("RefStdDeviceConstant", "RSS_HTCT_CurrentThresholdInAmpsLevel_10");
		ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_11 = ConstantRefStdConfigLoader
				.getFloat("RefStdDeviceConstant", "RSS_HTCT_CurrentThresholdInAmpsLevel_11");
		ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_12 = ConstantRefStdConfigLoader
				.getFloat("RefStdDeviceConstant", "RSS_HTCT_CurrentThresholdInAmpsLevel_12");

		ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_1 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_1");
		ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_2 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_2");
		ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_3 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_3");
		ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_4 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_4");
		ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_5 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_5");
		ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_6 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_6");
		ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_7 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_7");
		ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_8 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_8");
		ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_9 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_9");
		ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_10 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_10");
		ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_11 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_11");
		ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_12 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_12");

		ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_BELOW_LEVEL_12 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_HTCT_ActivePulseConstantInImpPerWh_BelowLevel_12");

		ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_1 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_1");
		ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_2 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_2");
		ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_3 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_3");
		ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_4 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_4");
		ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_5 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_5");
		ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_6 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_6");
		ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_7 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_7");
		ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_8 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_8");
		ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_9 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_9");
		ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_10 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_10");
		ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_11 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_11");
		ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_12 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_12");

		ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_BELOW_LEVEL_12 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_HTCT_ReactivePulseConstantInImpPerWh_BelowLevel_12");

		ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_1 = ConstantRefStdConfigLoader
				.getFloat("RefStdDeviceConstant", "RSS_LTCT_CurrentThresholdInAmpsLevel_1");
		ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_2 = ConstantRefStdConfigLoader
				.getFloat("RefStdDeviceConstant", "RSS_LTCT_CurrentThresholdInAmpsLevel_2");
		ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_3 = ConstantRefStdConfigLoader
				.getFloat("RefStdDeviceConstant", "RSS_LTCT_CurrentThresholdInAmpsLevel_3");
		ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_4 = ConstantRefStdConfigLoader
				.getFloat("RefStdDeviceConstant", "RSS_LTCT_CurrentThresholdInAmpsLevel_4");
		ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_5 = ConstantRefStdConfigLoader
				.getFloat("RefStdDeviceConstant", "RSS_LTCT_CurrentThresholdInAmpsLevel_5");
		ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_6 = ConstantRefStdConfigLoader
				.getFloat("RefStdDeviceConstant", "RSS_LTCT_CurrentThresholdInAmpsLevel_6");
		ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_7 = ConstantRefStdConfigLoader
				.getFloat("RefStdDeviceConstant", "RSS_LTCT_CurrentThresholdInAmpsLevel_7");
		ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_8 = ConstantRefStdConfigLoader
				.getFloat("RefStdDeviceConstant", "RSS_LTCT_CurrentThresholdInAmpsLevel_8");
		ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_9 = ConstantRefStdConfigLoader
				.getFloat("RefStdDeviceConstant", "RSS_LTCT_CurrentThresholdInAmpsLevel_9");
		ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_10 = ConstantRefStdConfigLoader
				.getFloat("RefStdDeviceConstant", "RSS_LTCT_CurrentThresholdInAmpsLevel_10");
		ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_11 = ConstantRefStdConfigLoader
				.getFloat("RefStdDeviceConstant", "RSS_LTCT_CurrentThresholdInAmpsLevel_11");
		ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_12 = ConstantRefStdConfigLoader
				.getFloat("RefStdDeviceConstant", "RSS_LTCT_CurrentThresholdInAmpsLevel_12");

		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_1 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_1");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_2 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_2");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_3 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_3");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_4 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_4");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_5 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_5");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_6 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_6");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_7 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_7");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_8 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_8");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_9 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_9");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_10 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_10");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_11 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_11");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_12 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_12");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_BELOW_OR_EQUAL_LEVEL_12 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentBelowLevel_12");

		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_1 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_1");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_2 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_2");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_3 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_3");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_4 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_4");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_5 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_5");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_6 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_6");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_7 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_7");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_8 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_8");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_9 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_9");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_10 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_10");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_11 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_11");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_12 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_12");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_BELOW_OR_EQUAL_LEVEL_12 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentBelowLevel_12");

		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_1 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_1");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_2 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_2");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_3 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_3");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_4 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_4");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_5 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_5");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_6 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_6");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_7 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_7");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_8 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_8");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_9 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_9");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_10 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_10");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_11 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_11");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_12 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_12");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_BELOW_OR_EQUAL_LEVEL_12 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentBelowLevel_12");

		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_1 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_1");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_2 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_2");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_3 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_3");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_4 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_4");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_5 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_5");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_6 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_6");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_7 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_7");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_8 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_8");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_9 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_9");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_10 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_10");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_11 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_11");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_12 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_12");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_BELOW_OR_EQUAL_LEVEL_12 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant",
						"RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentBelowLevel_12");

		ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_1 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_1");
		ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_2 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_2");
		ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_3 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_3");
		ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_4 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_4");
		ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_5 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_5");
		ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_6 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_6");
		ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_7 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_7");
		ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_8 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_8");
		ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_9 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_9");
		ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_10 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_10");
		ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_11 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_11");
		ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_12 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_12");

		ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_BELOW_LEVEL_12 = ConstantRefStdConfigLoader
				.getString("RefStdDeviceConstant", "RSS_LTCT_ReactivePulseConstantInImpPerWh_BelowLevel_12");

	}

}

