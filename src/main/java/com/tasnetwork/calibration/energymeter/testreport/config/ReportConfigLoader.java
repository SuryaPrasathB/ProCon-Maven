package com.tasnetwork.calibration.energymeter.testreport.config;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.tasnetwork.calibration.conveyor.bay.configloader.TerminalBayConfigLoader;
import com.tasnetwork.calibration.conveyor.bay.configloader.TerminalBayConfigModel;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;
import com.tasnetwork.calibration.energymeter.custom1report.Custom1ReportConfigLoader;
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
				ApplicationLauncher.logger.error("ReportConfigLoader: getAttribute : config file: section:"+ section);
				ApplicationLauncher.InformUser("ReportConfigLoader: Error-C15","Kindly check section:"+section +" on config file",AlertType.ERROR);

				return null;
			}
			return sectionObj.get(key);
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("ReportConfigLoader: getAttribute : config file1: section:"+ section);
			ApplicationLauncher.logger.error("ReportConfigLoader: getAttribute : Exception:"+ e.getMessage());
			ApplicationLauncher.InformUser("ReportConfigLoader: Error-C151","Kindly check section:"+section +" on config file\nError:"+e.getMessage(),AlertType.ERROR);

			return null;

		}
	}
	
	public static String getString(String section, String key) {
		try{
			String retValue = (String) getAttribute(section, key);
			if (retValue == null) {
				ApplicationLauncher.logger.error("ReportConfigLoader: getString : config file: section:"+ section);
				ApplicationLauncher.logger.error("ReportConfigLoader: getString : config file: key:"+ key);
				//ApplicationLauncher.InformUser("Error-C01","Kindly check section:" +section +" and key:"+key +" on config file",AlertType.ERROR);

				return retValue;
			}else{
				return retValue;
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("ReportConfigLoader: getString : config file1: section:"+ section);
			ApplicationLauncher.logger.error("ReportConfigLoader : getString : config file1: key:"+ key);
			ApplicationLauncher.logger.error("ReportConfigLoader : getString : Exception:"+ e.getMessage());
			ApplicationLauncher.InformUser("ReportConfigLoader : Error-C161","Kindly check section:" +section +" and key:"+key +" on config file\nError:"+e.getMessage(),AlertType.ERROR);

			return null;

		}
	}
	

	

	
	

	
	
/*	private static void readJsonConfig() {
		JSONParser parser = new JSONParser();
		
		try {
			Object obj = parser.parse(new InputStreamReader(CalibConfigLoader.class.getClass().getResourceAsStream(CONFIG_FILE)));
			ApplicationLauncher.logger.debug("ReportConfigLoader :Loaded config json obj:"+obj.toString());
			properties = (JSONObject) obj;
			//properties2= (CalibrationParser)obj.toString();// new properties2(CalibrationLoader.getAttribute(ConfigFileVersion),);
			
			//ConstantApp.calibrationParsedData = new CalibrationParser(CalibrationLoader.getAttribute("ConfigFileVersion").toString());
			ApplicationLauncher.logger.debug("ReportConfigLoader : Loaded config json property:"+properties);

		} catch (Exception e) {
			//System.err.println("Error while reading from " + CONFIG_FILE);
			e.printStackTrace();
			ApplicationLauncher.logger.error("ReportConfigLoader : readJsonConfig:Exception:  " + e.getMessage());
			ApplicationLauncher.logger.error("ReportConfigLoader : Error while reading from " + CONFIG_FILE);
		}
	}*/
	
	/*private static void readJsonConfigV2() {
		JSONParser parser = new JSONParser();
		Object obj = null;
		try {
			
			ApplicationLauncher.logger.debug("ReportConfigLoader : Loaded config json CONFIG_FILE:"+ CONFIG_FILE);
			
			//filePathName = filePathName.replace("\\", "/");
			//ApplicationLauncher.logger.debug("Loaded config json filePathName2:"+ filePathName);
			
			obj = parser.parse(new InputStreamReader(ReportConfigLoader.class.getClass().getResourceAsStream(CONFIG_FILE)));
			ApplicationLauncher.logger.debug("ReportConfigLoader : Loaded config json obj:"+obj.toString());
			
		
			
			//properties = (JSONObject) obj;
			//properties2= (CalibrationParser)obj.toString();// new properties2(CalibrationLoader.getAttribute(ConfigFileVersion),);
			
			//ConstantApp.calibrationParsedData = new CalibrationParser(CalibrationLoader.getAttribute("ConfigFileVersion").toString());
			Gson gson = new Gson();
			String yourJson = obj.toString();
			DeviceDataManagerController.setReportConfigParsedData(gson.fromJson(yourJson, ReportConfigModel.class));
			
			ApplicationLauncher.logger.debug("ReportConfigLoader : Loaded config json getConfigFileVersion : "+DeviceDataManagerController.getReportConfigParsedData().getConfigFileVersion());
			ApplicationLauncher.logger.debug("ReportConfigLoader : Loaded config json getDisplayFrequency : "+DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReportDisplay().getDisplayFrequency());
			//ApplicationLauncher.logger.debug("ReportConfigLoader : Loaded config json getActualErrorValueStartCell : "+DeviceDataManagerController.reportConfigParsedData.getCalibAccuracyReport().getActualErrorValueStartCell());
/*				for(int i = 0; i < DeviceDataManagerController.reportConfigParsedData.getMainMenuList().size(); i++){
					try{
						ApplicationLauncher.logger.debug("ReportConfigLoader: Loaded config json Main List : Index :" + i + ": " +DeviceDataManagerController.reportConfigParsedData.getMainMenuList().get(i).getMenuName());
						if(DeviceDataManagerController.reportConfigParsedData.getMainMenuList().get(i).getMenuName().toString().equals(null)){
							ApplicationLauncher.logger.debug("Loaded config json Main List : Index :" + i + ": Null" );
						}
						for(int j = 0; j < DeviceDataManagerController.reportConfigParsedData.getMainMenuList().get(i).getSubMenuList().size(); j++){
							ApplicationLauncher.logger.debug("ReportConfigLoader: Loaded config json Sub List : Index :" + j + ": " +DeviceDataManagerController.reportConfigParsedData.getMainMenuList().get(i).getSubMenuList().get(j).getMenuName());
							
						}
						
					} catch (Exception e) {
						//System.err.println("Error while reading from " + CONFIG_FILE);
						//e.printStackTrace();
						ApplicationLauncher.logger.error("ReportConfigLoader: readJsonConfigV2: Exception1:  " + e.getMessage());
						ApplicationLauncher.logger.error("ReportConfigLoader: Parsing Error while reading from " + CONFIG_FILE);
					}
					//}
				}
		
		} catch (Exception e) {
			//System.err.println("Error while reading from " + CONFIG_FILE);
			e.printStackTrace();
			ApplicationLauncher.logger.error("ReportConfigLoader : readJsonConfigV2: Exception2:  " + e.getMessage());
			ApplicationLauncher.logger.error("ReportConfigLoader : Error while reading from " + CONFIG_FILE);
			
/*			try{
			
				String filePathName =CONFIG_FILE;
				//filePathName = filePathName.replace("\\\\", "\\");
				ApplicationLauncher.logger.debug("ReportConfigLoader: Loaded config json filePathName1:"+ filePathName);
			
			FileReader reader = new FileReader(filePathName);
			obj = parser.parse(reader);
			ApplicationLauncher.logger.debug("ReportConfigLoader: Loaded config json obj2:"+obj.toString());
			
			
			Gson gson = new Gson();

		}
	}*/
	
	
	private static void readJsonConfigV2() {
	    JSONParser parser = new JSONParser();
	    Object obj = null;
	    try {
/*	        InputStream is = ReportConfigLoader.class.getResourceAsStream(CONFIG_FILE);

	        if (is == null) {
	            // fallback if running outside JAR (e.g., Eclipse and resources not in classpath)
	            File file = new File("src/main/resources" + CONFIG_FILE);
	            if (file.exists()) {
	                is = new FileInputStream(file);
	            } else {
	                throw new FileNotFoundException("ReportConfigLoader: Config file not found in classpath or fallback path.");
	            }
	        }

	        Object obj = parser.parse(new InputStreamReader(is));
	        properties = (JSONObject) obj;
	        */
	    	
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
