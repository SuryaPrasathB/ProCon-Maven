package com.tasnetwork.calibration.energymeter.custom1report;

import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.WindowManager;
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;

import javafx.scene.control.Alert.AlertType;

public class Custom1ReportConfigLoader {

	
	public static String configFilePathName = ConstantVersion.custom1ReportConfigFileName;//ConstantConfig.configFilePathName;//"/resources/config.json";

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
				ApplicationLauncher.logger.error("Custom1ReportConfigLoader: getAttribute : config file: section:"+ section);
				WindowManager.InformUser("Custom1ReportConfigLoader: Error-C15","Kindly check section:"+section +" on config file",AlertType.ERROR);

				return null;
			}
			return sectionObj.get(key);
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("Custom1ReportConfigLoader: getAttribute : config file1: section:"+ section);
			ApplicationLauncher.logger.error("Custom1ReportConfigLoader: getAttribute : Exception:"+ e.getMessage());
			WindowManager.InformUser("Custom1ReportConfigLoader: Error-C151","Kindly check section:"+section +" on config file\nError:"+e.getMessage(),AlertType.ERROR);

			return null;

		}
	}

	public static String getString(String section, String key) {
		try{
			String retValue = (String) getAttribute(section, key);
			if (retValue == null) {
				ApplicationLauncher.logger.error("Custom1ReportConfigLoader: getString : config file: section:"+ section);
				ApplicationLauncher.logger.error("Custom1ReportConfigLoader: getString : config file: key:"+ key);
				//WindowManager.InformUser("Error-C01","Kindly check section:" +section +" and key:"+key +" on config file",AlertType.ERROR);

				return retValue;
			}else{
				return retValue;
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("Custom1ReportConfigLoader: getString : config file1: section:"+ section);
			ApplicationLauncher.logger.error("Custom1ReportConfigLoader : getString : config file1: key:"+ key);
			ApplicationLauncher.logger.error("Custom1ReportConfigLoader : getString : Exception:"+ e.getMessage());
			WindowManager.InformUser("Custom1ReportConfigLoader : Error-C161","Kindly check section:" +section +" and key:"+key +" on config file\nError:"+e.getMessage(),AlertType.ERROR);

			return null;

		}
	}

	private static void readJsonConfigV2() {
		JSONParser parser = new JSONParser();
		Object obj = null;
		try {

			ApplicationLauncher.logger.debug("Custom1ReportConfigLoader : Loaded config json CONFIG_FILE:"+ configFilePathName);

			//filePathName = filePathName.replace("\\", "/");
			//ApplicationLauncher.logger.debug("Loaded config json filePathName2:"+ filePathName);

			obj = parser.parse(new InputStreamReader(Custom1ReportConfigLoader.class.getClass().getResourceAsStream(configFilePathName)));
			//ApplicationLauncher.logger.debug("Custom1ReportConfigLoader : Loaded config json obj:"+obj.toString());



			//properties = (JSONObject) obj;
			//properties2= (CalibrationParser)obj.toString();// new properties2(CalibrationLoader.getAttribute(ConfigFileVersion),);

			//ConstantApp.calibrationParsedData = new CalibrationParser(CalibrationLoader.getAttribute("ConfigFileVersion").toString());
			Gson gson = new Gson();
			String yourJson = obj.toString();
			DeviceDataManagerController.setCustom1ReportConfigParsedKey(gson.fromJson(yourJson, Custom1ReportConfigModel.class));

			ApplicationLauncher.logger.debug("Custom1ReportConfigLoader : Loaded config json getConfigFileVersion : "+DeviceDataManagerController.getCustom1ReportConfigParsedKey().getConfigFileVersion());
			//ApplicationLauncher.logger.debug("Custom1ReportConfigLoader : Loaded config json getActualErrorValueStartCell : "+DeviceDataManagerController.wordReportConfigParsedData.getCalibAccuracyReport().getActualErrorValueStartCell());


		} catch (Exception e) {
			//System.err.println("Error while reading from " + CONFIG_FILE);
			e.printStackTrace();
			ApplicationLauncher.logger.error("Custom1ReportConfigLoader : readJsonConfigV2: Exception2:  " + e.getMessage());
			ApplicationLauncher.logger.error("Custom1ReportConfigLoader : Error while reading from " + configFilePathName);


		}
	}

	public static String getConfigFilePathName() {
		return configFilePathName;
	}

	//public static void setConfigFilePathName(String configFilePathName) {
	//	Custom1ReportConfigLoader.configFilePathName = "/resources/"+ configFilePathName;
	//}
	
	public static void setConfigFilePathName(String configFolderPath, String configFilePathName) {
		
		Custom1ReportConfigLoader.configFilePathName = configFolderPath	+ configFilePathName;
		ApplicationLauncher.logger.debug("Custom1ReportConfigLoader: setConfigFilePathName : configFilePathName: " + Custom1ReportConfigLoader.configFilePathName);
	}
	
}
