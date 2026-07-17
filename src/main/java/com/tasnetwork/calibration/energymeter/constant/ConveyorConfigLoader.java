package com.tasnetwork.calibration.energymeter.constant;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.reportprofile.OperationProcessJsonReadModel;

import javafx.scene.control.Alert.AlertType;

public class ConveyorConfigLoader {

		public static String conveyorConfigFileName = ConstantVersion.lscsConveyorConfigFileName;//ConstantConfig.configFilePathName;//"/resources/config.json";

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
					ApplicationLauncher.logger.error("ConveyorConfigLoader: getAttribute : config file: section:"+ section);
					ApplicationLauncher.InformUser("ConveyorConfigLoader: Error-C15","Kindly check section:"+section +" on config file",AlertType.ERROR);

					return null;
				}
				return sectionObj.get(key);
			}catch (Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("ConveyorConfigLoader: getAttribute : config file1: section:"+ section);
				ApplicationLauncher.logger.error("ConveyorConfigLoader: getAttribute : Exception:"+ e.getMessage());
				ApplicationLauncher.InformUser("ConveyorConfigLoader: Error-C151","Kindly check section:"+section +" on config file\nError:"+e.getMessage(),AlertType.ERROR);

				return null;

			}
		}

		public static String getString(String section, String key) {
			try{
				String retValue = (String) getAttribute(section, key);
				if (retValue == null) {
					ApplicationLauncher.logger.error("ConveyorConfigLoader: getString : config file: section:"+ section);
					ApplicationLauncher.logger.error("ConveyorConfigLoader: getString : config file: key:"+ key);
					//ApplicationLauncher.InformUser("Error-C01","Kindly check section:" +section +" and key:"+key +" on config file",AlertType.ERROR);

					return retValue;
				}else{
					return retValue;
				}
			}catch (Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("ConveyorConfigLoader: getString : config file1: section:"+ section);
				ApplicationLauncher.logger.error("ConveyorConfigLoader : getString : config file1: key:"+ key);
				ApplicationLauncher.logger.error("ConveyorConfigLoader : getString : Exception:"+ e.getMessage());
				ApplicationLauncher.InformUser("ConveyorConfigLoader : Error-C161","Kindly check section:" +section +" and key:"+key +" on config file\nError:"+e.getMessage(),AlertType.ERROR);

				return null;

			}
		}

		private static void readJsonConfigV2() {
			JSONParser parser = new JSONParser();
			Object obj = null;
			try {

				ApplicationLauncher.logger.debug("ConveyorConfigLoader : Loaded config json CONFIG_FILE: "+ conveyorConfigFileName);

				//filePathName = filePathName.replace("\\", "/");
				//ApplicationLauncher.logger.debug("Loaded config json filePathName2:"+ filePathName);

				//obj = parser.parse(new InputStreamReader(ConveyorConfigLoader.class.getClass().getResourceAsStream(conveyorConfigFileName)));
				//ApplicationLauncher.logger.debug("ConveyorConfigLoader : Loaded config json obj:"+obj.toString());

				
				
				
				InputStream inputStream = Thread.currentThread().getContextClassLoader()
			    .getResourceAsStream(getConveyorConfigFileName());//"config/app/app_config_elmeasure_jan2025_v1_5.json");

			if (inputStream == null) {
			    throw new FileNotFoundException("Config file not found in classpath!");
			}

		obj = parser.parse(new InputStreamReader(inputStream));
		//obj = parser.parse(new InputStreamReader(ReportProfileOperationConfigLoader.class.getClass().getResourceAsStream(configFilePathName)));
		ApplicationLauncher.logger.debug("ConveyorConfigLoader : Loaded config json obj:"+obj.toString());



		//properties = (JSONObject) obj;
		//properties2= (CalibrationParser)obj.toString();// new properties2(CalibrationLoader.getAttribute(ConfigFileVersion),);

				
				


				//properties = (JSONObject) obj;
				//properties2= (CalibrationParser)obj.toString();// new properties2(CalibrationLoader.getAttribute(ConfigFileVersion),);

				//ConstantApp.calibrationParsedData = new CalibrationParser(CalibrationLoader.getAttribute("ConfigFileVersion").toString());
				Gson gson = new Gson();
				String yourJson = obj.toString();
				DeviceDataManagerController.setConveyorConfigParsedKey(gson.fromJson(yourJson, ConveyorConfigModel.class));

				ApplicationLauncher.logger.debug("ConveyorConfigLoader : Loaded config json getConfigFileVersion : "+DeviceDataManagerController.getConveyorConfigParsedKey().isOpticalReadingInterfaceEnabled());
				//ApplicationLauncher.logger.debug("ConveyorConfigLoader : Loaded config json getActualErrorValueStartCell : "+DeviceDataManagerController.wordReportConfigParsedData.getCalibAccuracyReport().getActualErrorValueStartCell());


			} catch (Exception e) {
				//System.err.println("Error while reading from " + CONFIG_FILE);
				e.printStackTrace();
				ApplicationLauncher.logger.error("ConveyorConfigLoader : readJsonConfigV2: Exception2:  " + e.getMessage());
				ApplicationLauncher.logger.error("ConveyorConfigLoader : Error while reading from " + conveyorConfigFileName);


			}
			

		}
		
			public static String getConveyorConfigFileName() {
				return conveyorConfigFileName;
			}

			public static void setConveyorConfigFileName(String configFolderPath, String configFilePathName) {
				//ConveyorConfigLoader.lscsCalibrationFileName = "/resources/"+ configFilePathName;
				
				ConveyorConfigLoader.conveyorConfigFileName = configFolderPath	+ configFilePathName;
				ApplicationLauncher.logger.debug("setLscsCalibrationFileName : lscsCalibrationFileName: " + ConveyorConfigLoader.conveyorConfigFileName);
			}
		
	}
