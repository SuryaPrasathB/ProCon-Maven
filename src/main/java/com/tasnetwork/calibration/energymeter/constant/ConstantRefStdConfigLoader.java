package com.tasnetwork.calibration.energymeter.constant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;

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
				ApplicationLauncher.logger.error("ConstantRefStdConfigLoader: getAttribute : config file: section:"+ section);
				ApplicationLauncher.InformUser("ConstantRefStdConfigLoader: Error-C15","Kindly check section:"+section +" on config file",AlertType.ERROR);

				return null;
			}
			return sectionObj.get(key);
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("ConstantRefStdConfigLoader: getAttribute : config file1: section:"+ section);
			ApplicationLauncher.logger.error("ConstantRefStdConfigLoader: getAttribute : Exception:"+ e.getMessage());
			ApplicationLauncher.InformUser("ConstantRefStdConfigLoader: Error-C151","Kindly check section:"+section +" on config file\nError:"+e.getMessage(),AlertType.ERROR);

			return null;

		}
	}

	public static String getString(String section, String key) {
		try{
			String retValue = (String) getAttribute(section, key);
			if (retValue == null) {
				ApplicationLauncher.logger.error("ConstantRefStdConfigLoader: getString : config file: section:"+ section);
				ApplicationLauncher.logger.error("ConstantRefStdConfigLoader: getString : config file: key:"+ key);
				//ApplicationLauncher.InformUser("Error-C01","Kindly check section:" +section +" and key:"+key +" on config file",AlertType.ERROR);

				return retValue;
			}else{
				return retValue;
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("ConstantRefStdConfigLoader: getString : config file1: section:"+ section);
			ApplicationLauncher.logger.error("ConstantRefStdConfigLoader : getString : config file1: key:"+ key);
			ApplicationLauncher.logger.error("ConstantRefStdConfigLoader : getString : Exception:"+ e.getMessage());
			ApplicationLauncher.InformUser("ConstantRefStdConfigLoader : Error-C161","Kindly check section:" +section +" and key:"+key +" on config file\nError:"+e.getMessage(),AlertType.ERROR);

			return null;

		}
	}
	
	public static Float getFloat(String section, String key) {
		try{
			Double retValue = (Double) getAttribute(section, key);
			if (retValue == null) {
				ApplicationLauncher.logger.error("ConstantRefStdConfigLoader: getFloat : config file: section:"+ section);
				ApplicationLauncher.logger.error("ConstantRefStdConfigLoader: getFloat : config file: key:"+ key);
				ApplicationLauncher.InformUser("Error-C06","Kindly check section:" +section +" and key:"+key +" on config file",AlertType.ERROR);

				return 0.0F;
			}else{
				return retValue.floatValue();
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("ConstantRefStdConfigLoader: getFloat : config file1: section:"+ section);
			ApplicationLauncher.logger.error("ConstantRefStdConfigLoader: getFloat : config file1: key:"+ key);
			ApplicationLauncher.logger.error("ConstantRefStdConfigLoader: getFloat : Exception:"+ e.getMessage());
			ApplicationLauncher.InformUser("Error-C061","Kindly check section:" +section +" and key:"+key +" on config file\nError:"+e.getMessage(),AlertType.ERROR);

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
	    
}

