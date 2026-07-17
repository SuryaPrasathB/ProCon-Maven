package com.tasnetwork.calibration.energymeter.reportprofile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;

import javafx.scene.control.Alert.AlertType;

public class ReportProfileOperationConfigLoader {

	
	public static String configFilePathName = ConstantVersion.operationConfigFileName;//ConstantConfig.configFilePathName;//"/resources/config.json";

	private static JSONObject properties = null;
	//private static CalibrationParser properties2 = null;

	public static void init() {
		//readJsonConfig();
		readJsonConfigV2();
	}
	
	public static void init3() {
		//readJsonConfig();
		readJsonConfigV3();
	}

	public static Object getAttribute(String key) {
		try{
			Object retValue  = properties.get(key);
			if (retValue == null) {
				ApplicationLauncher.logger.error("getAttribute : config file: key:"+ key);
				ApplicationLauncher.InformUser("Error-C01","Kindly check key:"+key +" on config file",AlertType.ERROR);

				return null;
			}
			//return properties.get(key);
			return retValue;
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("getAttribute : config file1: key:"+ key);
			ApplicationLauncher.logger.error("getAttribute : Exception:"+ e.getMessage());
			ApplicationLauncher.InformUser("Error-C011","Kindly check key:"+key +" on config file\nError:"+e.getMessage(),AlertType.ERROR);

			return null;

		}
	}

	private static Object getAttribute(String section, String key) {
		try{
			JSONObject sectionObj = (JSONObject)properties.get(section);
			if (sectionObj == null) {
				ApplicationLauncher.logger.error("ReportProfileOperationConfigLoader: getAttribute : config file: section:"+ section);
				ApplicationLauncher.InformUser("ReportProfileOperationConfigLoader: Error-C15","Kindly check section:"+section +" on config file",AlertType.ERROR);

				return null;
			}
			return sectionObj.get(key);
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("ReportProfileOperationConfigLoader: getAttribute : config file1: section:"+ section);
			ApplicationLauncher.logger.error("ReportProfileOperationConfigLoader: getAttribute : Exception:"+ e.getMessage());
			ApplicationLauncher.InformUser("ReportProfileOperationConfigLoader: Error-C151","Kindly check section:"+section +" on config file\nError:"+e.getMessage(),AlertType.ERROR);

			return null;

		}
	}

	public static String getString(String section, String key) {
		try{
			String retValue = (String) getAttribute(section, key);
			if (retValue == null) {
				ApplicationLauncher.logger.error("ReportProfileOperationConfigLoader: getString : config file: section:"+ section);
				ApplicationLauncher.logger.error("ReportProfileOperationConfigLoader: getString : config file: key:"+ key);
				//ApplicationLauncher.InformUser("Error-C01","Kindly check section:" +section +" and key:"+key +" on config file",AlertType.ERROR);

				return retValue;
			}else{
				return retValue;
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("ReportProfileOperationConfigLoader: getString : config file1: section:"+ section);
			ApplicationLauncher.logger.error("ReportProfileOperationConfigLoader : getString : config file1: key:"+ key);
			ApplicationLauncher.logger.error("ReportProfileOperationConfigLoader : getString : Exception:"+ e.getMessage());
			ApplicationLauncher.InformUser("ReportProfileOperationConfigLoader : Error-C161","Kindly check section:" +section +" and key:"+key +" on config file\nError:"+e.getMessage(),AlertType.ERROR);

			return null;

		}
	}

	private static void readJsonConfigV2() {
		JSONParser parser = new JSONParser();
		Object obj = null;
		try {

			ApplicationLauncher.logger.debug("ReportProfileOperationConfigLoader : Loaded config json CONFIG_FILE:"+ configFilePathName);

			//filePathName = filePathName.replace("\\", "/");
			//ApplicationLauncher.logger.debug("Loaded config json filePathName2:"+ filePathName);

			
			InputStream inputStream = Thread.currentThread().getContextClassLoader()
				    .getResourceAsStream(getConfigFilePathName());//"config/app/app_config_elmeasure_jan2025_v1_5.json");

				if (inputStream == null) {
				    throw new FileNotFoundException("Config file not found in classpath!");
				}

			obj = parser.parse(new InputStreamReader(inputStream));
			//obj = parser.parse(new InputStreamReader(ReportProfileOperationConfigLoader.class.getClass().getResourceAsStream(configFilePathName)));
			ApplicationLauncher.logger.debug("ReportProfileOperationConfigLoader : Loaded config json obj:"+obj.toString());



			//properties = (JSONObject) obj;
			//properties2= (CalibrationParser)obj.toString();// new properties2(CalibrationLoader.getAttribute(ConfigFileVersion),);

			//ConstantApp.calibrationParsedData = new CalibrationParser(CalibrationLoader.getAttribute("ConfigFileVersion").toString());
			Gson gson = new Gson();
			String yourJson = obj.toString();
			DeviceDataManagerController.setReportProfileConfigParsedKey(gson.fromJson(yourJson, OperationProcessJsonReadModel.class));

			ApplicationLauncher.logger.debug("ReportProfileOperationConfigLoader : Loaded config json getConfigFileVersion : "+DeviceDataManagerController.getReportProfileConfigParsedKey().getConfigFileVersion());
			//ApplicationLauncher.logger.debug("ReportProfileOperationConfigLoader : Loaded config json getActualErrorValueStartCell : "+DeviceDataManagerController.wordReportConfigParsedData.getCalibAccuracyReport().getActualErrorValueStartCell());


		} catch (Exception e) {
			//System.err.println("Error while reading from " + CONFIG_FILE);
			e.printStackTrace();
			ApplicationLauncher.logger.error("ReportProfileOperationConfigLoader : readJsonConfigV2: Exception2:  " + e.getMessage());
			ApplicationLauncher.logger.error("ReportProfileOperationConfigLoader : Error while reading from " + configFilePathName);


		}
	}
	
	
	private static void readJsonConfigV3() {
		JSONParser parser = new JSONParser();
		Object obj = null;
		try {

			ApplicationLauncher.logger.debug("ReportProfileOperationConfigLoader: readJsonConfigV3: Loaded config json CONFIG_FILE:"+ configFilePathName);

			//filePathName = filePathName.replace("\\", "/");
			//ApplicationLauncher.logger.debug("Loaded config json filePathName2:"+ filePathName);
			InputStream stream = new FileInputStream(getConfigFilePathName());  
//			obj = parser.parse(new InputStreamReader(ReportProfileOperationConfigLoader.class.getClass().getResourceAsStream(configFilePathName)));
			obj = parser.parse(new InputStreamReader(stream));
			ApplicationLauncher.logger.debug("ReportProfileOperationConfigLoader : Loaded config json obj:"+obj.toString());



			//properties = (JSONObject) obj;
			//properties2= (CalibrationParser)obj.toString();// new properties2(CalibrationLoader.getAttribute(ConfigFileVersion),);

			//ConstantApp.calibrationParsedData = new CalibrationParser(CalibrationLoader.getAttribute("ConfigFileVersion").toString());
			Gson gson = new Gson();
			String yourJson = obj.toString();
			DeviceDataManagerController.setReportProfileConfigParsedKey(gson.fromJson(yourJson, OperationProcessJsonReadModel.class));

			ApplicationLauncher.logger.debug("ReportProfileOperationConfigLoader : Loaded config json getConfigFileVersion : "+DeviceDataManagerController.getReportProfileConfigParsedKey().getConfigFileVersion());
			//ApplicationLauncher.logger.debug("ReportProfileOperationConfigLoader : Loaded config json getActualErrorValueStartCell : "+DeviceDataManagerController.wordReportConfigParsedData.getCalibAccuracyReport().getActualErrorValueStartCell());


		} catch (Exception e) {
			//System.err.println("Error while reading from " + CONFIG_FILE);
			e.printStackTrace();
			ApplicationLauncher.logger.error("ReportProfileOperationConfigLoader : readJsonConfigV2: Exception2:  " + e.getMessage());
			ApplicationLauncher.logger.error("ReportProfileOperationConfigLoader : Error while reading from " + configFilePathName);


		}
	}

	public static String getConfigFilePathName() {
		return configFilePathName;
	}

	//public static void setConfigFilePathName(String configFilePathName) {
	//	ReportProfileOperationConfigLoader.configFilePathName = "/resources/"+ configFilePathName;
	//}
	
	public static void setConfigFilePathName(String configFolderPath, String configFilePathName) {
		
		ReportProfileOperationConfigLoader.configFilePathName = configFolderPath	+ configFilePathName;
		ApplicationLauncher.logger.debug("ReportProfileOperationConfigLoader: setConfigFilePathName : configFilePathName: " + ReportProfileOperationConfigLoader.configFilePathName);
	}
}
