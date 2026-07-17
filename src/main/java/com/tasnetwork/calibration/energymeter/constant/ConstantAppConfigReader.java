package com.tasnetwork.calibration.energymeter.constant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.custom1report.Custom1ReportConfigLoader;
import com.tasnetwork.calibration.marked_for_delete.PrintStyleModel;

import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;

public class ConstantAppConfigReader {
    
	//public static final String CONFIG_FILE = ConstantAppConfig.configFilePathName;//"/resources/config.json";
	public static String configFilePathName = ConstantAppConfig.configFilePathName;//"/resources/config.json";
	
	private static JSONObject properties = null;
	
	public static void init() {
		readJsonConfig();
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
	
	public static String getString(String section, String key) {
		try{
			String retValue = (String) getAttribute(section, key);
			if (retValue == null) {
				ApplicationLauncher.logger.error("getString : config file: section:"+ section);
				ApplicationLauncher.logger.error("getString : config file: key:"+ key);
				//ApplicationLauncher.InformUser("Error-C01","Kindly check section:" +section +" and key:"+key +" on config file",AlertType.ERROR);

				return retValue;
			}else{
				return retValue;
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("getString : config file1: section:"+ section);
			ApplicationLauncher.logger.error("getString : config file1: key:"+ key);
			ApplicationLauncher.logger.error("getString : Exception:"+ e.getMessage());
			ApplicationLauncher.InformUser("Error-C161","Kindly check section:" +section +" and key:"+key +" on config file\nError:"+e.getMessage(),AlertType.ERROR);

			return null;

		}
	}
	
	public static Integer getInt(String section, String key) {
		try{
			Long retValue = (Long) getAttribute(section, key);
			if (retValue == null) {
				ApplicationLauncher.logger.error("getInt : config file: section:"+ section);
				ApplicationLauncher.logger.error("getInt : config file: key:"+ key);
				ApplicationLauncher.InformUser("Error-C02","Kindly check section:" +section +" and key:"+key +" on config file",AlertType.ERROR);

				return 0;
			}
			return retValue.intValue();
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("getInt : config file1: section:"+ section);
			ApplicationLauncher.logger.error("getInt : config file1: key:"+ key);
			ApplicationLauncher.logger.error("getInt : Exception:"+ e.getMessage());
			ApplicationLauncher.InformUser("Error-C021","Kindly check section:" +section +" and key:"+key +" on config file\nError:"+e.getMessage(),AlertType.ERROR);

			return null;

		}
	}
	
	
	public static PrintStyleModel getPrintStyleModel(String section, String key) {
		try{
			PrintStyleModel retValue = (PrintStyleModel) getAttribute(section, key);
			if (retValue == null) {
				ApplicationLauncher.logger.error("getPrintStyleModel : config file: section:"+ section);
				ApplicationLauncher.logger.error("getPrintStyleModel : config file: key:"+ key);
				ApplicationLauncher.InformUser("Error-C02A","Kindly check section:" +section +" and key:"+key +" on config file",AlertType.ERROR);

				return null;
			}
			return retValue;
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("getPrintStyleModel : config file1: section:"+ section);
			ApplicationLauncher.logger.error("getPrintStyleModel : config file1: key:"+ key);
			ApplicationLauncher.logger.error("getPrintStyleModel : Exception:"+ e.getMessage());
			ApplicationLauncher.InformUser("Error-C021A","Kindly check section:" +section +" and key:"+key +" on config file\nError:"+e.getMessage(),AlertType.ERROR);

			return null;

		}
	}
	
	public static Long getLong(String section, String key) {
		try{
			Long retValue = (Long) getAttribute(section, key);
			if (retValue == null) {
				ApplicationLauncher.logger.error("getLong : config file: section:"+ section);
				ApplicationLauncher.logger.error("getLong : config file: key:"+ key);
				ApplicationLauncher.InformUser("Error-C03","Kindly check section:" +section +" and key:"+key +" on config file",AlertType.ERROR);

				return retValue;
			}
			return retValue;
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("getLong : config file1: section:"+ section);
			ApplicationLauncher.logger.error("getLong : config file1: key:"+ key);
			ApplicationLauncher.logger.error("getLong : Exception:"+ e.getMessage());
			ApplicationLauncher.InformUser("Error-C031","Kindly check section:" +section +" and key:"+key +" on config file\nError:"+e.getMessage(),AlertType.ERROR);

			return null;

		}
	}
	public static Double getDouble(String section, String key) {
		try{
			Double retValue = (Double) getAttribute(section, key);
			if (retValue == null) {
				ApplicationLauncher.logger.error("getDouble : config file: section:"+ section);
				ApplicationLauncher.logger.error("getDouble : config file: key:"+ key);
				ApplicationLauncher.InformUser("Error-C04","Kindly check section:" +section +" and key:"+key +" on config file",AlertType.ERROR);

				return retValue;
			}
			return retValue;
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("getDouble : config file1: section:"+ section);
			ApplicationLauncher.logger.error("getDouble : config file1: key:"+ key);
			ApplicationLauncher.logger.error("getDouble : Exception:"+ e.getMessage());
			ApplicationLauncher.InformUser("Error-C041","Kindly check section:" +section +" and key:"+key +" on config file\nError:"+e.getMessage(),AlertType.ERROR);

			return null;

		}
	}
	
	public static boolean getBoolean(String section, String key) {
		boolean retValue = false;
		try{
			retValue = (boolean) getAttribute(section, key);
			return retValue;
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("getBoolean : config file: section:"+ section);
			ApplicationLauncher.logger.error("getBoolean : config file: key:"+ key);
			ApplicationLauncher.logger.error("PropertyReader : getBoolean Exception:"+ e.getMessage());
			ApplicationLauncher.InformUser("Error-C05","Kindly check section:" +section +" and key:"+key +" on config file\nError:"+e.getMessage(),AlertType.ERROR);

			
		}
		/*} catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("PropertyReader : getBoolean Exception :"+e.getMessage());
		}*/
		return retValue;
	}
	
	
	public static ArrayList<Integer> getIntegerList(String section, String key) {
		ArrayList<Integer> retValue = new ArrayList<Integer>();
		try{
			retValue = (ArrayList<Integer>) getAttribute(section, key);
			return retValue;
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("getIntegerList : config file: section:"+ section);
			ApplicationLauncher.logger.error("getIntegerList : config file: key:"+ key);
			ApplicationLauncher.logger.error("PropertyReader : getIntegerList Exception:"+ e.getMessage());
			ApplicationLauncher.InformUser("Error-C205","Kindly check section:" +section +" and key:"+key +" on config file\nError:"+e.getMessage(),AlertType.ERROR);

			
		}
		/*} catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("PropertyReader : getBoolean Exception :"+e.getMessage());
		}*/
		return retValue;
	}
	
	
	public static ArrayList<String> getStringList(String section, String key) {
		ArrayList<String> retValue = new ArrayList<String>();
		try{
			retValue = (ArrayList<String>) getAttribute(section, key);
			return retValue;
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("getStringList : config file: section:"+ section);
			ApplicationLauncher.logger.error("getStringList : config file: key:"+ key);
			ApplicationLauncher.logger.error("PropertyReader : getStringList Exception:"+ e.getMessage());
			ApplicationLauncher.InformUser("Error-C206","Kindly check section:" +section +" and key:"+key +" on config file\nError:"+e.getMessage(),AlertType.ERROR);

			
		}
		/*} catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("PropertyReader : getBoolean Exception :"+e.getMessage());
		}*/
		return retValue;
	}
	
	public static Float getFloat(String section, String key) {
		try{
			Double retValue = (Double) getAttribute(section, key);
			if (retValue == null) {
				ApplicationLauncher.logger.error("getFloat : config file: section:"+ section);
				ApplicationLauncher.logger.error("getFloat : config file: key:"+ key);
				ApplicationLauncher.InformUser("Error-C06","Kindly check section:" +section +" and key:"+key +" on config file",AlertType.ERROR);

				return 0.0F;
			}else{
				return retValue.floatValue();
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("getFloat : config file1: section:"+ section);
			ApplicationLauncher.logger.error("getFloat : config file1: key:"+ key);
			ApplicationLauncher.logger.error("getFloat : Exception:"+ e.getMessage());
			ApplicationLauncher.InformUser("Error-C061","Kindly check section:" +section +" and key:"+key +" on config file\nError:"+e.getMessage(),AlertType.ERROR);

			return 0.0F;
			
		}
		//return retValue.floatValue();
	}
	
	public static JSONArray getJsonArray(String section) {
		try{
			JSONArray retValue = (JSONArray) properties.get(section);
			if (retValue == null) {
				ApplicationLauncher.logger.error("getJsonArray : config file: section:"+ section);
				ApplicationLauncher.InformUser("Error-C07","Kindly check section:"+section +" on config file",AlertType.ERROR);
				
				return retValue;
			}
			return retValue;
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("getJsonArray : config file1: section:"+ section);
			ApplicationLauncher.logger.error("getJsonArray : Exception:"+ e.getMessage());
			ApplicationLauncher.InformUser("Error-C071","Kindly check section:"+section +" on config file\nError:"+e.getMessage(),AlertType.ERROR);
			return null;

		}
	}
	public static JSONArray getJsonArray(String section, String key) {
		try{
			JSONArray retValue = (JSONArray) getAttribute(section, key);
			if (retValue == null) {
				ApplicationLauncher.logger.error("getJsonArray : config file: section:"+ section);
				ApplicationLauncher.InformUser("Error-C08","Kindly check section:"+section +" on config file\nError",AlertType.ERROR);
				
				return retValue;
			}
			return retValue;
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("getJsonArray : config file1: section:"+ section);
			ApplicationLauncher.logger.error("getJsonArray : Exception:"+ e.getMessage());
			ApplicationLauncher.InformUser("Error-C081","Kindly check section:"+section +" on config file\nError:"+e.getMessage(),AlertType.ERROR);
			return null;

		}
	}
	
	public static Object[] getObjectArray(String section) {
		try{
			JSONArray retValue = (JSONArray) properties.get(section);
			if (retValue == null) {
				ApplicationLauncher.logger.error("getObjectArray : config file: section:"+ section);
				ApplicationLauncher.InformUser("Error-C09","Kindly check section:"+section +" on config file",AlertType.ERROR);

				return null;
			}
			return retValue.toArray();
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("getObjectArray : config file1: section:"+ section);
			ApplicationLauncher.logger.error("getObjectArray : Exception:"+ e.getMessage());
			ApplicationLauncher.InformUser("Error-C091","Kindly check section:"+section +" on config file\nError:"+e.getMessage(),AlertType.ERROR);


			return null;

		}
	}
	public static Object[] getObjectArray(String section, String key) {
		try{
			JSONArray retValue = (JSONArray) getAttribute(section, key);
			if (retValue == null) {
				ApplicationLauncher.logger.error("getObjectArray : config file: key:"+ key);
				ApplicationLauncher.InformUser("Error-C10","Kindly check key:"+key +" on config file",AlertType.ERROR);
				return null;
				
			}
			return retValue.toArray();
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("getObjectArray : config file1: key:"+ key);
			ApplicationLauncher.logger.error("getObjectArray : Exception:"+ e.getMessage());
			ApplicationLauncher.InformUser("Error-C101","Kindly check key:"+key +" on config file\nError:"+e.getMessage(),AlertType.ERROR);

			return null;

		}
	}
	
	public static List<?> getList(String section) {
		try{
			JSONArray retValue = (JSONArray) properties.get(section);
			if (retValue == null) {
				ApplicationLauncher.logger.error("getList : config file: section:"+ section);
				ApplicationLauncher.InformUser("Error-C11","Kindly check section:"+section +" on config file",AlertType.ERROR);

				return null;
			}
			return retValue.subList(0, retValue.size()-1);
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("getList : config file1: section:"+ section);
			ApplicationLauncher.logger.error("getList : Exception:"+ e.getMessage());
			ApplicationLauncher.InformUser("Error-C111","Kindly check section:"+section +" on config file\nError:"+e.getMessage(),AlertType.ERROR);


			return null;

		}
	}
	
	public static List<?> getList(String section, String key) {
		try{
			JSONArray retValue = (JSONArray) getAttribute(section, key);
			if (retValue == null) {
				ApplicationLauncher.logger.error("getList : config file: section:"+ section);
				ApplicationLauncher.InformUser("Error-C12","Kindly check section: "+section + " , key: " + key +" on config file",AlertType.ERROR);

				return null;
			}
			return retValue.subList(0, retValue.size());
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("getList : config file1: section:"+ section);
			ApplicationLauncher.logger.error("getList : Exception:"+ e.getMessage());
			ApplicationLauncher.InformUser("Error-C121","Kindly check section:"+section +" on config file\nError:"+e.getMessage(),AlertType.ERROR);


			return null;

		}
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getMap(String section) {
		try{
			Map<String, Object> retValue = (Map<String, Object>) properties.get(section);
			if (retValue == null) {
				ApplicationLauncher.logger.error("getMap : config file: section:"+ section);
				ApplicationLauncher.InformUser("Error-C13","Kindly check section:"+section +" on config file",AlertType.ERROR);

				return null;
			}
			return retValue;
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("getMap : config file1: section:"+ section);
			ApplicationLauncher.logger.error("getMap : Exception:"+ e.getMessage());
			ApplicationLauncher.InformUser("Error-C131","Kindly check section:"+section +" on config file\nError:"+e.getMessage(),AlertType.ERROR);


			return null;

		}
	}
	public static JSONObject getJsonObject(String section) {
		try{
			JSONObject retValue = (JSONObject)properties.get(section);
			if (retValue == null) {
				ApplicationLauncher.logger.error("getJsonObject : config file: section:"+ section);
				ApplicationLauncher.InformUser("Error-C14","Kindly check section:"+section +" on config file",AlertType.ERROR);

				return null;
			}
			return retValue;
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("getJsonObject : config file1: section:"+ section);
			ApplicationLauncher.logger.error("getJsonObject : Exception:"+ e.getMessage());
			ApplicationLauncher.InformUser("Error-C141","Kindly check section:"+section +" on config file\nError:"+e.getMessage(),AlertType.ERROR);

			return null;

		}
	}
	
	
/*	public static PrintStyleModel getPrintStyleModelObject(String section, String key) {
		try{
			PrintStyleModel retValue = (PrintStyleModel)properties.get(section);
			if (retValue == null) {
				ApplicationLauncher.logger.error("getJsonObject : config file: section:"+ section);
				ApplicationLauncher.InformUser("Error-C14A","Kindly check section:"+section +" on config file",AlertType.ERROR);

				return null;
			}
			return retValue.get;
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("getJsonObject : config file1: section:"+ section);
			ApplicationLauncher.logger.error("getJsonObject : Exception:"+ e.getMessage());
			ApplicationLauncher.InformUser("Error-C141A","Kindly check section:"+section +" on config file\nError:"+e.getMessage(),AlertType.ERROR);

			return null;

		}
	}*/
	
	private static Object getAttribute(String section, String key) {
		try{
			JSONObject sectionObj = (JSONObject)properties.get(section);
			if (sectionObj == null) {
				ApplicationLauncher.logger.error("getAttribute : config file: section:"+ section);
				ApplicationLauncher.InformUser("Error-C15","Kindly check section:"+section +" on config file",AlertType.ERROR);

				return null;
			}
			return sectionObj.get(key);
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("getAttribute : config file1: section:"+ section);
			ApplicationLauncher.logger.error("getAttribute : Exception:"+ e.getMessage());
			ApplicationLauncher.InformUser("Error-C151","Kindly check section:"+section +" on config file\nError:"+e.getMessage(),AlertType.ERROR);

			return null;

		}
	}
	
	

	
	
	private static void readJsonConfig() {
		JSONParser parser = new JSONParser();
		Object obj = null;
		try {
			
			
			String jarPath = System.getProperty("user.dir");
			ApplicationLauncher.logger.debug("ConstantAppConfigReader : jarPath: "+ jarPath);

			String externalFilePath = Paths.get(jarPath, getAppConfigFilePathName()).toString();
			ApplicationLauncher.logger.debug("ConstantAppConfigReader : externalFilePath: "+ externalFilePath);

			File externalFile = new File(externalFilePath);

			if (externalFile.exists()) {
				// ✅ Load from external folder "config/refstd/config.json"
				ApplicationLauncher.logger.debug("ConstantAppConfigReader: Loading external config: " + externalFilePath);
				obj = parser.parse(new FileReader(externalFile));
			} else {
				// 🔹 Fallback: Load from inside JAR (resources/config/refstd/config.json)
				ApplicationLauncher.logger.debug("ConstantAppConfigReader : External file not found. Loading from JAR resources...");

			//Object obj = parser.parse(new InputStreamReader(ConstantAppConfigReader.class.getClass().getResourceAsStream(getAppConfigFilePathName())));
				InputStream inputStream = Thread.currentThread().getContextClassLoader()
				    .getResourceAsStream(getAppConfigFilePathName());//"config/app/app_config_elmeasure_jan2025_v1_5.json");

				if (inputStream == null) {
				    throw new FileNotFoundException("ConstantAppConfigReader : Config file not found in classpath!");
				}

			//Object obj = parser.parse(new InputStreamReader(inputStream));
				 obj = parser.parse(new InputStreamReader(inputStream));
				 
			}
			properties = (JSONObject) obj;
			ApplicationLauncher.logger.debug("ConstantAppConfigReader: Loaded config json property:"+properties);

		} catch (Exception e) {
			//System.err.println("Error while reading from " + CONFIG_FILE);
			e.printStackTrace();
			ApplicationLauncher.logger.error("ConstantAppConfigReader: readJsonConfig:Exception:  " + e.getMessage());
			ApplicationLauncher.logger.error("ConstantAppConfigReader :Error while reading from " + getAppConfigFilePathName());
		}
	}
	
	public static String getAppConfigFilePathName() {
		return configFilePathName;
	}

	public static void setAppConfigFilePathName(String configFilePathName) {
		ApplicationLauncher.logger.debug("setAppConfigFilePathName : ConstantMasterConfig.CONFIG_FILE_PATH  :"+ConstantMasterConfig.CONFIG_FILE_PATH);
		//ConstantAppConfigReader.configFilePathName = "/resources/"+ configFilePathName;
		ConstantAppConfigReader.configFilePathName = ConstantMasterConfig.CONFIG_FILE_PATH + configFilePathName;
		//ConstantAppConfigReader.configFilePathName = "/resources/Config/" + configFilePathName;
	}
	    
}
