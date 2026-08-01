package com.tasnetwork.calibration.energymeter.testreport.config;


import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.WindowManager;
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;

import javafx.scene.control.Alert.AlertType;

public class ReportConfigLoader {
	public static String CONFIG_FILE = ConstantVersion.reportConfigFileName;//ConstantConfig.configFilePathName;//"/resources/config.json";
	
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
				ApplicationLauncher.logger.error("ReportConfigLoader: getAttribute : config file: section:"+ section);
				WindowManager.InformUser("ReportConfigLoader: Error-C15","Kindly check section:"+section +" on config file",AlertType.ERROR);

				return null;
			}
			return sectionObj.get(key);
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("ReportConfigLoader: getAttribute : config file1: section:"+ section);
			ApplicationLauncher.logger.error("ReportConfigLoader: getAttribute : Exception:"+ e.getMessage());
			WindowManager.InformUser("ReportConfigLoader: Error-C151","Kindly check section:"+section +" on config file\nError:"+e.getMessage(),AlertType.ERROR);

			return null;

		}
	}
	
	public static String getString(String section, String key) {
		try{
			String retValue = (String) getAttribute(section, key);
			if (retValue == null) {
				ApplicationLauncher.logger.error("ReportConfigLoader: getString : config file: section:"+ section);
				ApplicationLauncher.logger.error("ReportConfigLoader: getString : config file: key:"+ key);
				//WindowManager.InformUser("Error-C01","Kindly check section:" +section +" and key:"+key +" on config file",AlertType.ERROR);

				return retValue;
			}else{
				return retValue;
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("ReportConfigLoader: getString : config file1: section:"+ section);
			ApplicationLauncher.logger.error("ReportConfigLoader : getString : config file1: key:"+ key);
			ApplicationLauncher.logger.error("ReportConfigLoader : getString : Exception:"+ e.getMessage());
			WindowManager.InformUser("ReportConfigLoader : Error-C161","Kindly check section:" +section +" and key:"+key +" on config file\nError:"+e.getMessage(),AlertType.ERROR);

			return null;

		}
	}
	
	private static void readJsonConfigV2() {
	    JSONParser parser = new JSONParser();
	    Object obj = null;
	    try {

	    	
			InputStream inputStream = Thread.currentThread().getContextClassLoader()
				    .getResourceAsStream(getConfigFilePathName());//"config/app/app_config_elmeasure_jan2025_v1_5.json");

				if (inputStream == null) {
				    throw new FileNotFoundException("Config file not found in classpath!");
				}

			obj = parser.parse(new InputStreamReader(inputStream));
			//obj = parser.parse(new InputStreamReader(ReportProfileOperationConfigLoader.class.getClass().getResourceAsStream(configFilePathName)));
			ApplicationLauncher.logger.debug("ReportConfigLoader : Loaded config json obj:"+obj.toString());


	        
	        Gson gson = new Gson();
			String yourJson = obj.toString();
			DeviceDataManagerController.setReportConfigParsedData(gson.fromJson(yourJson, ReportConfigModel.class));
	        ApplicationLauncher.logger.debug("ReportConfigLoader: Loaded config JSON property: " + properties);

	    } catch (Exception e) {
	        e.printStackTrace();
	        ApplicationLauncher.logger.error("ReportConfigLoader: readJsonConfig:Exception: " + e.getMessage());
	        ApplicationLauncher.logger.error("ReportConfigLoader: Error while reading from " + CONFIG_FILE);
	    }
	}
	
	public static String getConfigFilePathName() {
		return ReportConfigLoader.CONFIG_FILE;//configFilePathName;
	}
	
	public static void setConfigFilePathName(String configFolderPath, String configFilePathName) {
		
		ReportConfigLoader.CONFIG_FILE = configFolderPath	+ configFilePathName;
		ApplicationLauncher.logger.debug("ReportConfigLoader: setConfigFilePathName : CONFIG_FILE: " + ReportConfigLoader.CONFIG_FILE);
	}
}
