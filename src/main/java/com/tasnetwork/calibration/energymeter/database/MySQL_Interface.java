package com.tasnetwork.calibration.energymeter.database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.sql.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;
//import org.mindrot.jbcrypt.BCrypt;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfigReader;
import com.tasnetwork.calibration.energymeter.constant.ConstantLscsHarmonicsSourceSlave;
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;
import com.tasnetwork.calibration.energymeter.constant.Constant_Mysql;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.deployment.TextBoxDialog;
import com.tasnetwork.calibration.energymeter.testprofiles.TestProfileType;
import com.tasnetwork.calibration.energymeter.testreport.ResultDataModel;
import com.tasnetwork.calibration.energymeter.uac.UacDataModel;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;



public class MySQL_Interface {


	/*private static final String mSQL_URL = "jdbc:mysql://localhost:3306/ltcalibration";  
	private static final String mSQL_User = "root"; 
	private static final String mSQL_Password = "swam@WL13";*/
	//private static final String mSQL_URL = ConstantApp.DB_URL+ConstantAppConfig.DB_NAME; 
	private static final String mSQL_URL = ConstantAppConfig.DB_URL+ConstantAppConfig.DB_NAME+ ConstantAppConfig.DB_URL_TAIL_OPTION; 
	private static final String mSQL_User = ConstantAppConfig.DB_USERNAME;
	private static final String mSQL_Password = ConstantAppConfig.DB_PASSWORD;
	private static Connection ConnectManager  ; 
	public static boolean bDB_SchemaExist  = true;
	public static boolean bDB_Connected  = true;

	public  boolean isDbConnected() {

		try {
			if(!ConnectManager.isClosed() && ConnectManager!=null){

				return true;
			}else{
				ApplicationLauncher.logger.info("isDbConnected : DB Currently not connected");
			}
		} catch (Exception e) {
			//e.printStackTrace();
			ApplicationLauncher.logger.error("isDbConnected: Exception:" + e.getMessage());
			return false;
		}

		return false;
	}

	public boolean ConnectMySQL() {


		try {

			if(isDbConnected()){
				return true;
			} else{

				ApplicationLauncher.logger.info("Loading jdbc library..");
/*				try {
          		  
          		  Thread.sleep(2000);
          		  
	          	  } catch (InterruptedException e) {
	          		  // TODO Auto-generated catch block
	          		  e.printStackTrace();
	          	  }*/
				Class.forName("com.mysql.jdbc.Driver");
				//Class.forName("com.mysql.jdbc.driver");
				ApplicationLauncher.logger.info("Connecting to jdbc...");
				ApplicationLauncher.logger.info("ConnectMySQL: mSQL_URL: " + mSQL_URL);
				String url1 = mSQL_URL;//+"?useSSL=false&allowPublicKeyRetrieval=true";
				//ApplicationLauncher.logger.info("ConnectMySQL: url1: " + url1);
				String user = mSQL_User;
				String password = mSQL_Password;
				try{
					ConnectManager = DriverManager.getConnection(url1, user, password);
					
					if (ConnectManager != null) {
						ApplicationLauncher.logger.info("Connected to the database");
						return true;
					}
				}catch (SQLException e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("ConnectMySQL: Database connectivity failed due to below reason!!");
					ApplicationLauncher.logger.error("ConnectMySQL: Exception:" + e.getMessage());
					//ApplicationLauncher.logger.error("ConnectMySQL: System.err: " + );

					if(e.getMessage().toLowerCase().contains("unknown") && e.getMessage().toLowerCase().contains("database")){
						bDB_SchemaExist = false;
					}else{
						bDB_Connected = false;
						Alert alert = new Alert(AlertType.ERROR);
						Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
						stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
						alert.setTitle("Database connection failed");
						String s = "Database connectivity failed due to below reason!\n" + e.getMessage();
						alert.setContentText(s);

						alert.showAndWait();
					}


				}
			}


		} catch (Exception ex) {
			ex.printStackTrace();
			ApplicationLauncher.logger.error("ConnectMySQL: Exception2:" + ex.getMessage());
			return false;
		}
		return false;

	}
	
	public JSONObject sp_get_completed_result_data(String mctNctMode,String filterDataType1,String deploymentID) {


		JSONObject result_json = new JSONObject();
		JSONArray result_arr =new JSONArray();
		ApplicationLauncher.logger.debug ("sp_get_completed_result_data: data_type :"+ filterDataType1);
		ApplicationLauncher.logger.debug ("sp_get_completed_result_data: deploymentID :"+ deploymentID);

		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_get_completed_result_data(?,?,?)}");
			 
			statement.setString(1, mctNctMode);
			statement.setString(2, filterDataType1);
			//statement.setString(1, filterDataType1);
			//statement.setString(2, filterDataType2);
			//statement.setString(3, filterDataType3);
			statement.setString(3, deploymentID);
			

			boolean hadResults = statement.execute();

			int count =0;
			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();

					// process result set
					while (resultSet.next()) {


						JSONObject jobj = new JSONObject();
						jobj.put("result_id", resultSet.getString("result_id"));
						jobj.put("test_case_name", resultSet.getString("test_case_name"));
						jobj.put("alias_id", resultSet.getString("alias_id"));

						jobj.put("device_name", resultSet.getInt("device_name"));
						jobj.put("test_status", resultSet.getString("test_result"));
						jobj.put("error_value", resultSet.getString("error_value"));
						
						jobj.put("main_neutral_ct_mode", resultSet.getString("main_neutral_ct_mode"));
						jobj.put("import_export_mode", resultSet.getString("import_export_mode"));
						
						if( resultSet.getString("seq_no")!=null) {
							jobj.put("sequence_no", resultSet.getString("seq_no"));
						}else{
							jobj.put("sequence_no", "");
						}

						//ApplicationLauncher.logger.error ("sp_getresult_data :"+jobj.getString("ratio_error"));
						result_arr.put(jobj);
						count++;
					}


					hadResults = statement.getMoreResults();

					result_json.put("No_of_results", count);
					result_json.put("Results", result_arr);

				}
				statement.close();


			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_get_completed_result_data : Exception1 :"+ ex.getMessage());
				statement.close();
				//ApplicationLauncher.logger.error ("sp_ltgetresult_data :Error 201: Source EM Model Reading: Failure");

				return result_json;
			}

		} catch (Exception ex) {

			ex.printStackTrace();	
			ApplicationLauncher.logger.error("sp_get_completed_result_data : Exception2 :"+ ex.getMessage());
			//ApplicationLauncher.logger.error ("sp_ltgetresult_data :Error 202: Source EM Model Reading: Failure");

			return result_json;
		}

		return result_json;
	}

	public JSONObject sp_get_executed_project_results(long start_epoch_time, long end_epoch_time) {

		JSONObject project_run = new JSONObject();
		JSONArray project_run_arr = new JSONArray();

		try {

			CallableStatement statement = ConnectManager.prepareCall("{call sp_get_executed_project_results(?,?)}");
			statement.setLong(1, start_epoch_time);
			statement.setLong(2, end_epoch_time);

			boolean hadResults = statement.execute();

			int count = 0; 
			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();

					// process result set
					while (resultSet.next()) {


						JSONObject jobj = new JSONObject();
						//jobj.put("project_name", resultSet.getString("project_name"));
						//jobj.put("start_time", resultSet.getString("start_time"));
						jobj.put("end_time", resultSet.getString("execution_completed_time_h"));
						//jobj.put("epoch_start_time", resultSet.getString("epoch_start_time"));
						jobj.put("epoch_end_time", resultSet.getString("execution_completed_time_epoch"));
						jobj.put("deployment_id", resultSet.getString("deployment_id"));
						jobj.put("project_name", resultSet.getString("project_name"));
						jobj.put("customer_name", resultSet.getString("customer_name"));
						jobj.put("equipment_serial_no", resultSet.getString("equipment_serial_no"));
						jobj.put("mct_mode_completed", resultSet.getString("mct_mode_completed"));
						jobj.put("nct_mode_completed", resultSet.getString("nct_mode_completed"));
						//jobj.put("tested_by", resultSet.getString("tested_by"));bjhbjh
						
						if( resultSet.getString("tested_by")!=null) {
							jobj.put("tested_by", resultSet.getString("tested_by"));
						}else{
							jobj.put("tested_by", "");
						}
						
						if( resultSet.getString("customer_reference_no")!=null) {
							jobj.put("customer_reference_no", resultSet.getString("customer_reference_no"));
						}else{
							jobj.put("customer_reference_no", "");
						}
						
						if( resultSet.getString("energy_flow_mode")!=null) {
							jobj.put("energy_flow_mode", resultSet.getString("energy_flow_mode"));
						}else{
							jobj.put("energy_flow_mode", "");
						}
						
						
						project_run_arr.put(jobj);
						count++;
					}
					hadResults = statement.getMoreResults();

					project_run.put("No_of_Runs", count);
					project_run.put("Runs", project_run_arr);

				}
				statement.close();





			} catch (Exception ex) {
				ex.printStackTrace();
				ApplicationLauncher.logger.error ("sp_ltget_project_end_time : Exception1 :"+ ex.getMessage());
				statement.close();
				return project_run;
			}

		} catch (Exception ex) {

			ex.printStackTrace();
			ApplicationLauncher.logger.error ("sp_ltget_project_end_time : Exception2 :"+ ex.getMessage());
			return project_run;
		}
		return project_run;
	}
	

	public boolean sp_ltcreateSrcDevice (String SrcType,String ModelName,String MeterType,String SerialNo,String classValue,String Asset_ID,String DeviceActive,String UpdatedBy) {

		try {

			CallableStatement statement = ConnectManager.prepareCall("{call sp_ltcreateSrcDevice(?,?,?,?,?,?,?,?)}");
			statement.setString(1, SrcType); 
			statement.setString(2, ModelName); 
			statement.setString(3, MeterType); 
			statement.setString(4, SerialNo); 
			statement.setString(5, classValue); 
			statement.setString(6, Asset_ID); 
			statement.setString(7, DeviceActive); 
			statement.setString(8, UpdatedBy);	

			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {


				if (count==1){

					return true;
				} else {


					return false;
				}

			} catch (Exception ex) {
				ex.printStackTrace();
				ApplicationLauncher.logger.error("sp_ltcreateSrcDevice: Exception1:" + ex.getMessage());
				return false;

			}



		} catch (Exception ex) {
			ex.printStackTrace();
			ApplicationLauncher.logger.error("sp_ltcreateSrcDevice: Exception2:" + ex.getMessage());
			return false;
		}
	}
	
/*	public JSONObject sp_getresult_data(long fromtime, long totime, String project_name,String data_type,String deploymentID) {


		JSONObject result_json = new JSONObject();
		JSONArray result_arr =new JSONArray();
		ApplicationLauncher.logger.debug ("sp_getresult_data: fromtime :"+ fromtime);
		ApplicationLauncher.logger.debug ("sp_getresult_data: totime :"+ totime);
		ApplicationLauncher.logger.debug ("sp_getresult_data: project_name :"+ project_name);
		ApplicationLauncher.logger.debug ("sp_getresult_data: deploymentID :"+ deploymentID);

		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_getresult_data(?,?,?,?,?)}");
			statement.setLong(1, fromtime); 
			statement.setLong(2, totime);
			statement.setString(3, project_name);
			statement.setString(4, data_type);
			statement.setString(5, deploymentID);
			
dbfxdf
			boolean hadResults = statement.execute();
			int count =0;
			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();

					// process result set
					while (resultSet.next()) {


						JSONObject jobj = new JSONObject();
						jobj.put("test_case_name", resultSet.getString("test_case_name"));
						jobj.put("execution_status", resultSet.getString("execution_status"));
						jobj.put("burden_type", resultSet.getString("burden_type"));
						jobj.put("load_type", resultSet.getString("load_type"));
						if( resultSet.getString("ratio_error")!=null) {
							jobj.put("ratio_error", resultSet.getString("ratio_error"));
						}else {
							jobj.put("ratio_error","");
						}
						jobj.put("ratio_error_limit", resultSet.getString("ratio_error_limit"));
						if( resultSet.getString("phase_error")!=null) {
							jobj.put("phase_error", resultSet.getString("phase_error"));
						}else {
							jobj.put("phase_error", "");
						}
						jobj.put("phase_error_limit", resultSet.getString("phase_error_limit"));
						jobj.put("remarks", resultSet.getString("remarks"));
						jobj.put("phase_type", resultSet.getString("phase_type"));
						jobj.put("ratio_error_status", resultSet.getString("ratio_error_status"));
						jobj.put("phase_error_status", resultSet.getString("phase_error_status"));
						
						jobj.put("actual_sec_burden", resultSet.getString("actual_sec_burden"));
						jobj.put("actual_sec_pf", resultSet.getString("actual_sec_pf"));
						jobj.put("actual_pri_value", resultSet.getString("actual_pri_value"));
						jobj.put("test_result", resultSet.getString("test_result"));
						if( resultSet.getString("actual_load_percent")!=null) {
							jobj.put("actual_load_percent", resultSet.getString("actual_load_percent"));
						}else{
							jobj.put("actual_load_percent", "");
						}
						if( resultSet.getString("seq_no")!=null) {
							jobj.put("sequence_no", resultSet.getString("seq_no"));
						}else{
							jobj.put("sequence_no", "");
						}
						//ApplicationLauncher.logger.error ("sp_getresult_data :"+jobj.getString("ratio_error"));
						result_arr.put(jobj);
						count++;
					}


					hadResults = statement.getMoreResults();

					result_json.put("No_of_results", count);
					result_json.put("Results", result_arr);

				}
				statement.close();


			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_getresult_data : Exception1 :"+ ex.getMessage());
				statement.close();
				//ApplicationLauncher.logger.error ("sp_ltgetresult_data :Error 201: Source EM Model Reading: Failure");

				return result_json;
			}

		} catch (Exception ex) {

			ex.printStackTrace();	
			ApplicationLauncher.logger.error("sp_getresult_data : Exception2 :"+ ex.getMessage());
			//ApplicationLauncher.logger.error ("sp_ltgetresult_data :Error 202: Source EM Model Reading: Failure");

			return result_json;
		}

		return result_json;
	}*/
	
	public JSONObject sp_procal_getdeploy_manage_active(long deployedTimeMaxSearchLimit) {

		JSONObject resultjson = new JSONObject();
		JSONArray JsonList = new JSONArray();
		//DeploymentDataModel deployManageModel = new DeploymentDataModel("","","","","","","");
/*		ApplicationLauncher.logger.info("sp_progen_getdeploy_manage_active: customer name:"+deployManageModel.customer_nameProperty().getClass().getFields().getClass().getName());
		ApplicationLauncher.logger.info("sp_progen_getdeploy_manage_active: equipment_serial_no name:"+deployManageModel.getEquipment_serial_no().getClass().getSimpleName());*/
		
		try {

			ApplicationLauncher.logger.debug ("sp_procal_getdeploy_manage_active: deployedTimeMaxSearchLimit: " + deployedTimeMaxSearchLimit);
			CallableStatement statement = ConnectManager.prepareCall("{call sp_getdeploy_manage_active(?)}");
			statement.setLong(1, deployedTimeMaxSearchLimit); 

			boolean hadResults = statement.execute();


			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();

					// process result set
					int No_of_deployment = 0;
					while (resultSet.next()) {



						JSONObject jobj = new JSONObject ();
						jobj.put("deployment_id", resultSet.getString("deployment_id"));
						jobj.put("project_name", resultSet.getString("project_name"));
						jobj.put("customer_name", resultSet.getString("customer_name"));
						jobj.put("equipment_serial_no", resultSet.getString("equipment_serial_no"));
						jobj.put("main_ct_mode", resultSet.getString("mct_mode"));
						jobj.put("neutral_ct_mode", resultSet.getString("nct_mode"));
						jobj.put("mct_mode_completed", resultSet.getString("mct_mode_completed"));
						jobj.put("nct_mode_completed", resultSet.getString("nct_mode_completed"));
						jobj.put("customer_reference_no", resultSet.getString("customer_reference_no"));
						jobj.put("ulr_no", resultSet.getString("ulr_no"));
						jobj.put("execution_status", resultSet.getString("execution_status"));
						if(resultSet.getString("energy_flow_mode")!=null){
							jobj.put("energy_flow_mode", resultSet.getString("energy_flow_mode"));
						} else{
							//ex.printStackTrace();
							jobj.put("energy_flow_mode", ConstantApp.DEPLOYMENT_IMPORT_MODE);
							//ApplicationLauncher.logger.error ("sp_procal_getdeploy_manage_active : Exception on energy_flow_mode :"+ ex.getMessage());
							

						}
						try{
							if(resultSet.getString("auto_deploy_enabled")==null){
								jobj.put("auto_deploy_enabled", "N");
							} else{
								jobj.put("auto_deploy_enabled", resultSet.getString("auto_deploy_enabled"));
								
								
	
							}
						}catch(Exception e){
							jobj.put("auto_deploy_enabled", "N");
							ApplicationLauncher.logger.error ("sp_procal_getdeploy_manage_active : Exception2 :"+ e.getMessage());
						}
						JsonList.put(jobj);
						No_of_deployment++;
						//deploymentDataList.add(arg0)
					}
					hadResults = statement.getMoreResults();

					resultjson.put("No_of_deployment", No_of_deployment);
					resultjson.put("Deployment", JsonList);

				}
				statement.close();





			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_procal_getdeploy_manage_active : Exception1 :"+ ex.getMessage());
				statement.close();
				//ApplicationLauncher.logger.error ("sp_ltgetdeploy_devices :Error 201: Source EM Model Reading: Failure");

				return resultjson;
			}

		} catch (Exception ex) {

			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_procal_getdeploy_manage_active : Exception2 :"+ ex.getMessage());
			//ApplicationLauncher.logger.error ("sp_ltgetdeploy_devices :Error 202: Source EM Model Reading: Failure");
			return resultjson;
		}
		return resultjson;
	}
	
	
	public boolean sp_procal_update_execution_status_deploy_manage(String project_name,String deploymentId,String executionStatus,String mctModeCompletedStatus,String nctModeCompletedStatus) {

		try {

			CallableStatement statement = ConnectManager.prepareCall("{call sp_update_execution_status_deploy_manage(?,?,?,?,?)}");
			statement.setString(1, project_name); 
			statement.setString(2, deploymentId); 
			statement.setString(3, executionStatus); 
			statement.setString(4, mctModeCompletedStatus); 
			statement.setString(5, nctModeCompletedStatus); 
			

			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {

				if (count==1){
					ApplicationLauncher.logger.info("sp_procal_update_execution_status_deploy_manage: DB Success: ");
					return true;
				} else {

					ApplicationLauncher.logger.info("sp_procal_update_execution_status_deploy_manage: DB failed: ");
					return false;
				}

			} catch (Exception ex) {
				ex.printStackTrace();
				ApplicationLauncher.logger.error ("sp_procal_update_execution_status_deploy_manage : Exception1 :"+ ex.getMessage());
				return false;

			}	

		} catch (Exception ex) {
			ex.printStackTrace();
			ApplicationLauncher.logger.error ("sp_procal_update_execution_status_deploy_manage : Exception2 :"+ ex.getMessage());
			return false;
		}
	}


	public ArrayList<String> sp_lt_getmodel_list(String EM_Model) {


		ArrayList<String> ModelList = new ArrayList<String>();

		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_lt_getmodel_list(?)}");
			statement.setString(1, EM_Model); 


			boolean hadResults = statement.execute();






			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();


					while (resultSet.next()) {

						ModelList.add(resultSet.getString("model_name"));


					}

					hadResults = statement.getMoreResults();

				}
				statement.close();


			} catch (Exception ex) {
				ex.printStackTrace();
				ApplicationLauncher.logger.error("sp_lt_getmodel_list: Exception:1" + ex.getMessage());
				statement.close();
				ApplicationLauncher.logger.info ("sp_lt_getmodel_list:Error 201: Source EM Model Reading: Failure");
				return ModelList;
			}

		} catch (Exception ex) {

			ex.printStackTrace();
			ApplicationLauncher.logger.error("sp_lt_getmodel_list: Exception:2" + ex.getMessage());
			ApplicationLauncher.logger.info ("sp_lt_getmodel_list:Error 202: Source EM Model Reading: Failure");
			return ModelList;
		}

		return ModelList;
	}

	public boolean sp_ltadd_project_components ( String project_name,String test_case_name, String test_type, 
			String test_alias_id, String test_position_id, String time_duration, String creep_un,
			String creep_pulses, String sta_ib, 
			String sta_test_pulse_no, String std_dev_input, String std_dev_load, 
			String inf_emin, String inf_emax, String inf_pulses,
			String skip_reading_count, String inf_deviation,
			String testruntype, String power, String frequency, String inf_voltage,
			String inf_voltage_unbalance_u1, String inf_voltage_unbalance_u2, String inf_voltage_unbalance_u3,
			String cus_u1, String cus_u2, String cus_u3, String cus_i1, String cus_i2,
			String cus_i3, String cus_ph1, String cus_ph2, String cus_ph3,
			String cus_freq, String inf_average) {



		/*ApplicationLauncher.logger.info("inf_emin" + inf_emin);
		ApplicationLauncher.logger.info("inf_emax" + inf_emax);
		ApplicationLauncher.logger.info("inf_pulses" + inf_pulses);
		ApplicationLauncher.logger.info("skip_reading_count" + skip_reading_count);
		ApplicationLauncher.logger.info("inf_deviation" + inf_deviation);
		ApplicationLauncher.logger.info("testruntype" + testruntype);*/


		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_add_project_components(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			statement.setString(1, project_name); 
			statement.setString(2, test_case_name);
			statement.setString(3, test_type); 
			statement.setString(4, test_alias_id); 
			statement.setString(5, test_position_id); 
			statement.setString(6, time_duration); 
			statement.setString(7, creep_un);  
			statement.setString(8, creep_pulses);	
			statement.setString(9, sta_ib); 
			statement.setString(10, sta_test_pulse_no); 
			statement.setString(11, std_dev_input); 
			statement.setString(12, std_dev_load); 
			statement.setString(13, inf_emin);	
			statement.setString(14, inf_emax); 
			statement.setString(15, inf_pulses); 
			statement.setString(16, skip_reading_count); 
			statement.setString(17, inf_deviation); 
			statement.setString(18, testruntype); 
			statement.setString(19, power); 
			statement.setString(20, frequency); 
			statement.setString(21, inf_voltage); 
			statement.setString(22, inf_voltage_unbalance_u1); 
			statement.setString(23, inf_voltage_unbalance_u2); 
			statement.setString(24, inf_voltage_unbalance_u3); 
			statement.setString(25, cus_u1); 
			statement.setString(26, cus_u2); 
			statement.setString(27, cus_u3); 
			statement.setString(28, cus_i1); 
			statement.setString(29, cus_i2); 
			statement.setString(30, cus_i3); 
			statement.setString(31, cus_ph1); 
			statement.setString(32, cus_ph2); 
			statement.setString(33, cus_ph3); 
			statement.setString(34, cus_freq); 
			statement.setString(35, inf_average); 

			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {


				//ApplicationLauncher.logger.info("sp_ltadd_project_components: count:  " + count);

				if (count==1){

					//ApplicationLauncher.logger.info("sp_ltadd_project_components: DB Success");
					return true;
				} else {


					ApplicationLauncher.logger.info("sp_ltadd_project_components: DB failed");
					return false;
				}




			} catch (Exception ex) {
				ex.printStackTrace();
				ApplicationLauncher.logger.error("sp_ltadd_project_components: Exception:1" + ex.getMessage());
				return false;

			}



		} catch (Exception ex) {
			ex.printStackTrace();
			ApplicationLauncher.logger.error("sp_ltadd_project_components: Exception:2" + ex.getMessage());
			return false;
		}
	}


	public boolean sp_ltadd_project ( String project_name, String test_type, String test_alias_id, String test_position_id) {





		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_add_project(?,?,?,?)}");
			statement.setString(1, project_name); 
			statement.setString(2, test_type); 
			statement.setString(3, test_alias_id); 
			statement.setString(4, test_position_id);

			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {


				//ApplicationLauncher.logger.info("sp_ltadd_project: count: "+ count);

				if (count==1){

					//ApplicationLauncher.logger.info("sp_ltadd_project: DB Success: ");
					return true;
				} else {


					ApplicationLauncher.logger.info("sp_ltadd_project: DB failed: ");
					return false;
				}




			} catch (Exception ex) {
				ex.printStackTrace();
				ApplicationLauncher.logger.error("sp_ltadd_project: Exception1:" + ex.getMessage());
				return false;

			}



		} catch (Exception ex) {
			ex.printStackTrace();
			ApplicationLauncher.logger.error("sp_ltadd_project: Exception2:" + ex.getMessage());
			return false;
		}
	}



	public JSONObject sp_ltgetproject(String projectname) {


		JSONObject project_data = new JSONObject();
		JSONArray project_nodes = new JSONArray();
		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_getproject(?)}");
			statement.setString(1, projectname); 


			boolean hadResults = statement.execute();





			int no_of_nodes = 0;
			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();


					while (resultSet.next()) {


						JSONObject project_node = new JSONObject();
						project_node.put("test_type", resultSet.getString("test_type"));
						project_node.put("test_alias_id", resultSet.getString("test_alias_id"));
						project_node.put("test_position_id", resultSet.getString("test_position_id"));
						project_nodes.put(project_node);
						no_of_nodes++;

					}


					hadResults = statement.getMoreResults();

					project_data.put("No_of_nodes", no_of_nodes);
					project_data.put("Nodes", project_nodes);

				}
				statement.close();


			} catch (Exception ex) {
				ex.printStackTrace();
				ApplicationLauncher.logger.error("sp_ltgetproject: Exception1:" + ex.getMessage());
				statement.close();
				//ApplicationLauncher.logger.info ("sp_lt_getmodel_list:Error 201: Source EM Model Reading: Failure");
				return project_data;
			}

		} catch (Exception ex) {

			ex.printStackTrace();
			ApplicationLauncher.logger.error("sp_ltgetproject: Exception2:" + ex.getMessage());
			//ApplicationLauncher.logger.info ("sp_lt_getmodel_list:Error 202: Source EM Model Reading: Failure");
			return project_data;
		}

		return project_data;
	}

	public boolean sp_ltgettest_point_setupSaveAs ( String CurrentProjectName,String ToBeSavedProjectName) {


		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_gettest_point_setupSaveAs(?,?)}");
			statement.setString(1, CurrentProjectName); 
			statement.setString(2, ToBeSavedProjectName); 


			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();
			ApplicationLauncher.logger.info("sp_ltgettest_point_setupSaveAs: count:"+count);
			try {

				if (count==0){
					ApplicationLauncher.logger.info("sp_ltgettest_point_setupSaveAs :DB failed: ");
					return false;
				} else {


					ApplicationLauncher.logger.info("sp_ltgettest_point_setupSaveAs: DB Success: ");
					return true;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				ApplicationLauncher.logger.error("sp_ltgettest_point_setupSaveAs: Exception1:" + ex.getMessage());
				return false;

			}



		} catch (Exception ex) {
			ex.printStackTrace();
			ApplicationLauncher.logger.error("sp_ltgettest_point_setupSaveAs: Exception2:" + ex.getMessage());
			return false;
		}
	}

	public boolean sp_ltgetsummary_dataSaveAs ( String CurrentProjectName,String ToBeSavedProjectName) {


		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_getsummary_dataSaveAs(?,?)}");
			statement.setString(1, CurrentProjectName); 
			statement.setString(2, ToBeSavedProjectName); 


			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();
			ApplicationLauncher.logger.info("sp_ltgetsummary_dataSaveAs: count:"+count);
			try {

				if (count==0){
					ApplicationLauncher.logger.info("sp_ltgetsummary_dataSaveAs :DB failed: ");
					return false;
				} else {


					ApplicationLauncher.logger.info("sp_ltgetsummary_dataSaveAs: DB Success: ");
					return true;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				ApplicationLauncher.logger.error("sp_ltgetsummary_dataSaveAs: Exception1:" + ex.getMessage());
				return false;

			}



		} catch (Exception ex) {
			ex.printStackTrace();
			ApplicationLauncher.logger.error("sp_ltgetsummary_dataSaveAs: Exception2:" + ex.getMessage());
			return false;
		}
	}



	public boolean sp_ltgetproject_componentsSaveAs ( String CurrentProjectName,String ToBeSavedProjectName) {


		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_getproject_componentsSaveAs(?,?)}");
			statement.setString(1, CurrentProjectName); 
			statement.setString(2, ToBeSavedProjectName); 


			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();
			ApplicationLauncher.logger.info("sp_ltgetproject_componentsSaveAs: count:"+count);
			try {

				if (count==0){
					ApplicationLauncher.logger.info("sp_ltgetproject_componentsSaveAs:DB failed: ");
					return false;
				} else {


					ApplicationLauncher.logger.info("sp_ltgetproject_componentsSaveAs:DB Success: ");
					return true;
				}
			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error("sp_ltgetproject_componentsSaveAs : Exception1 :"+ ex.getMessage());
				return false;

			}



		} catch (Exception ex) {
			ex.printStackTrace();	
			ApplicationLauncher.logger.error("sp_ltgetproject_componentsSaveAs : Exception2 :"+ ex.getMessage());
			return false;
		}
	}

	public boolean sp_ltgetproject_modelmappingSaveAs ( String CurrentProjectName,String ToBeSavedProjectName,int EM_Model_ID) {


		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_getproject_modelmappingSaveAs(?,?,?)}");
			statement.setString(1, CurrentProjectName); 
			statement.setString(2, ToBeSavedProjectName); 
			statement.setInt(3, EM_Model_ID); 

			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();
			ApplicationLauncher.logger.info("sp_ltgetproject_modelmappingSaveAs: count:"+count);
			try {

				if (count==0){
					ApplicationLauncher.logger.info("sp_ltgetproject_modelmappingSaveAs :DB failed: ");
					return false;
				} else {


					ApplicationLauncher.logger.info("sp_ltgetproject_modelmappingSaveAs :DB Success: ");
					return true;
				}
			} catch (Exception ex) {
				ex.printStackTrace();	ApplicationLauncher.logger.error("sp_ltgetproject_modelmappingSaveAs : Exception1 :"+ ex.getMessage());
				return false;

			}



		} catch (Exception ex) {
			ex.printStackTrace();	ApplicationLauncher.logger.error("sp_ltgetproject_modelmappingSaveAs : Exception2 :"+ ex.getMessage());
			return false;
		}
	}

	public boolean sp_ltgetprojectSaveAs ( String CurrentProjectName,String ToBeSavedProjectName) {


		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_getprojectSaveAs(?,?)}");
			statement.setString(1, CurrentProjectName); 
			statement.setString(2, ToBeSavedProjectName); 


			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();
			ApplicationLauncher.logger.info("sp_ltgetprojectSaveAs: count:"+count);
			try {

				if (count==0){
					ApplicationLauncher.logger.info("sp_ltgetprojectSaveAs :DB failed: ");
					return false;
				} else {


					ApplicationLauncher.logger.info("sp_ltgetprojectSaveAs :DB Success: ");
					return true;
				}
			} catch (Exception ex) {
				ex.printStackTrace();	ApplicationLauncher.logger.error("sp_ltgetprojectSaveAs : Exception1 :"+ ex.getMessage());
				return false;

			}



		} catch (Exception ex) {
			ex.printStackTrace();	ApplicationLauncher.logger.error("sp_ltgetprojectSaveAs : Exception2 :"+ ex.getMessage());
			return false;
		}
	}



	public JSONObject sp_ltgetproject_components(String projectname, String testcase, String aliasid) {


		JSONObject test_details_json = new JSONObject();
		JSONArray json_list = new JSONArray();
		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_getproject_components(?,?,?)}");
			statement.setString(1, projectname); 
			statement.setString(2, testcase); 
			statement.setString(3, aliasid); 


			boolean hadResults = statement.execute();

			int count = 0;
			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();

					while (resultSet.next()) {

						String TestCaseType = testcase;
						ApplicationLauncher.logger.info("sp_ltgetproject_components: TestCaseType: " + TestCaseType);
						JSONObject jobj = new JSONObject();
						switch (TestCaseType) {

						//case "STA":
						case	ConstantApp.TEST_PROFILE_STA:
							jobj.put("sta_ib", resultSet.getString("sta_ib"));
							jobj.put("time_duration", resultSet.getString("time_duration"));
							jobj.put("sta_test_pulse_no", resultSet.getString("sta_test_pulse_no"));
							jobj.put("voltage", resultSet.getString("inf_voltage"));
							jobj.put("testruntype", resultSet.getString("test_run_type"));
							json_list.put(jobj);
							count++;
							break;

						//case "Warmup":
						case ConstantApp.TEST_PROFILE_WARMUP:
							jobj.put("time_duration", resultSet.getString("time_duration"));
							jobj.put("voltage", resultSet.getString("inf_voltage"));
							jobj.put("inf_pulses", resultSet.getString("inf_pulses"));
							jobj.put("inf_average", resultSet.getString("inf_average"));
							jobj.put("testruntype", resultSet.getString("test_run_type"));
							jobj.put("inf_emin", resultSet.getString("inf_emin"));
							jobj.put("inf_emax", resultSet.getString("inf_emax"));
							json_list.put(jobj);
							count++;
							break;

						//case "NoLoad":
						case ConstantApp.TEST_PROFILE_NOLOAD :
							jobj.put("creep_un", resultSet.getString("creep_un"));
							jobj.put("time_duration", resultSet.getString("time_duration"));
							jobj.put("creep_pulses", resultSet.getString("creep_pulses"));
							jobj.put("testruntype", resultSet.getString("test_run_type"));
							json_list.put(jobj);
							count++;
							break;

/*						case "Accuracy":
						case "InfluenceHarmonic":
						case "CuttingNuetral":
						case "PhaseReversal":*/
						case	ConstantApp.TEST_PROFILE_ACCURACY:
						case	ConstantApp.TEST_PROFILE_INFLUENCE_HARMONIC:
						case	ConstantApp.TEST_PROFILE_CUT_NUETRAL:
						case	ConstantApp.TEST_PROFILE_PHASE_REVERSAL:
							jobj.put("test_case_name", resultSet.getString("test_case_name"));
							jobj.put("inf_emin", resultSet.getString("inf_emin"));
							jobj.put("inf_emax", resultSet.getString("inf_emax"));
							jobj.put("inf_pulses", resultSet.getString("inf_pulses"));
							jobj.put("inf_average", resultSet.getString("inf_average"));
							jobj.put("time_duration", resultSet.getString("time_duration"));

							jobj.put("skip_reading_count", resultSet.getString("skip_reading_count"));
							jobj.put("inf_deviation", resultSet.getString("inf_deviation"));
							jobj.put("testruntype", resultSet.getString("test_run_type"));
							jobj.put("voltage", resultSet.getString("inf_voltage"));
							ApplicationLauncher.logger.debug("MYSQL_Interface: Accuracy:testruntype: "  + resultSet.getString("test_run_type"));
							json_list.put(jobj);
							count++;
							break;

						//case "InfluenceVolt":
						case ConstantApp.TEST_PROFILE_INFLUENCE_VOLT:
							jobj.put("test_case_name", resultSet.getString("test_case_name"));
							jobj.put("inf_emin", resultSet.getString("inf_emin"));
							jobj.put("inf_emax", resultSet.getString("inf_emax"));
							jobj.put("inf_pulses", resultSet.getString("inf_pulses"));
							jobj.put("inf_average", resultSet.getString("inf_average"));
							jobj.put("time_duration", resultSet.getString("time_duration"));

							jobj.put("skip_reading_count", resultSet.getString("skip_reading_count"));
							jobj.put("inf_deviation", resultSet.getString("inf_deviation"));
							jobj.put("inf_voltage", resultSet.getString("inf_voltage"));
							jobj.put("testruntype", resultSet.getString("test_run_type"));
							ApplicationLauncher.logger.debug("MYSQL_Interface: InfluenceVolt: testruntype: "  + resultSet.getString("test_run_type"));
							json_list.put(jobj);
							count++;
							break;



						//case "ConstantTest":
						case ConstantApp.TEST_PROFILE_CONSTANT_TEST:
							jobj.put("test_case_name", resultSet.getString("test_case_name"));
							jobj.put("inf_emin", resultSet.getString("inf_emin"));
							jobj.put("inf_emax", resultSet.getString("inf_emax"));
							jobj.put("inf_pulses", resultSet.getString("inf_pulses"));
							jobj.put("inf_average", resultSet.getString("inf_average"));
							jobj.put("time_duration", resultSet.getString("time_duration"));
							jobj.put("testruntype", resultSet.getString("test_run_type"));
							jobj.put("skip_reading_count", resultSet.getString("skip_reading_count"));
							jobj.put("inf_deviation", resultSet.getString("inf_deviation"));
							jobj.put("power", resultSet.getString("const_power"));
							jobj.put("energy", resultSet.getString("const_power"));
							jobj.put("voltage", resultSet.getString("inf_voltage"));

							json_list.put(jobj);
							count++;
							break;


						//case "InfluenceFreq":
						case ConstantApp.TEST_PROFILE_INFLUENCE_FREQ:
							jobj.put("test_case_name", resultSet.getString("test_case_name"));
							jobj.put("inf_emin", resultSet.getString("inf_emin"));
							jobj.put("inf_emax", resultSet.getString("inf_emax"));
							jobj.put("inf_pulses", resultSet.getString("inf_pulses"));
							jobj.put("inf_average", resultSet.getString("inf_average"));
							jobj.put("time_duration", resultSet.getString("time_duration"));
							jobj.put("skip_reading_count", resultSet.getString("skip_reading_count"));
							jobj.put("inf_deviation", resultSet.getString("inf_deviation"));
							jobj.put("frequency", resultSet.getString("frequency"));
							jobj.put("testruntype", resultSet.getString("test_run_type"));
							jobj.put("voltage", resultSet.getString("inf_voltage"));
							json_list.put(jobj);
							count++;
							break;

						//case "VoltageUnbalance":
						case ConstantApp.TEST_PROFILE_VOLTAGE_UNBALANCE:
							jobj.put("test_case_name", resultSet.getString("test_case_name"));
							jobj.put("inf_emin", resultSet.getString("inf_emin"));
							jobj.put("inf_emax", resultSet.getString("inf_emax"));
							jobj.put("inf_pulses", resultSet.getString("inf_pulses"));
							jobj.put("inf_average", resultSet.getString("inf_average"));
							jobj.put("time_duration", resultSet.getString("time_duration"));

							jobj.put("skip_reading_count", resultSet.getString("skip_reading_count"));
							jobj.put("inf_deviation", resultSet.getString("inf_deviation"));
							jobj.put("testruntype", resultSet.getString("test_run_type"));
							jobj.put("inf_voltage_unbalance_u1", resultSet.getString("inf_voltage_unbalance_u1"));
							jobj.put("inf_voltage_unbalance_u2", resultSet.getString("inf_voltage_unbalance_u2"));
							jobj.put("inf_voltage_unbalance_u3", resultSet.getString("inf_voltage_unbalance_u3"));
							json_list.put(jobj);
							count++;
							break;

						//case "CustomTest":
						case ConstantApp.TEST_PROFILE_CUSTOM_TEST:
							jobj.put("test_case_name", resultSet.getString("test_case_name"));
							jobj.put("cus_voltage_u1", resultSet.getString("cus_voltage_u1"));
							jobj.put("cus_voltage_u2", resultSet.getString("cus_voltage_u2"));
							jobj.put("cus_voltage_u3", resultSet.getString("cus_voltage_u3"));
							jobj.put("cus_current_i1", resultSet.getString("cus_current_i1"));
							jobj.put("cus_current_i2", resultSet.getString("cus_current_i2"));
							jobj.put("cus_current_i3", resultSet.getString("cus_current_i3"));
							jobj.put("cus_phase_ph1", resultSet.getString("cus_phase_ph1"));
							jobj.put("cus_phase_ph2", resultSet.getString("cus_phase_ph2"));
							jobj.put("cus_phase_ph3", resultSet.getString("cus_phase_ph3"));
							jobj.put("cus_frequency", resultSet.getString("cus_frequency"));
							jobj.put("inf_emin", resultSet.getString("inf_emin"));
							jobj.put("inf_emax", resultSet.getString("inf_emax"));
							jobj.put("inf_pulses", resultSet.getString("inf_pulses"));
							jobj.put("inf_average", resultSet.getString("inf_average"));
							jobj.put("time_duration", resultSet.getString("time_duration"));
							jobj.put("skip_reading_count", resultSet.getString("skip_reading_count"));
							jobj.put("testruntype", resultSet.getString("test_run_type"));
							json_list.put(jobj);
							count++;
							break;

						//case "Repeatability":
						case ConstantApp.TEST_PROFILE_REPEATABILITY:
							jobj.put("test_case_name", resultSet.getString("test_case_name"));
							jobj.put("inf_emin", resultSet.getString("inf_emin"));
							jobj.put("inf_emax", resultSet.getString("inf_emax"));
							jobj.put("inf_pulses", resultSet.getString("inf_pulses"));
							jobj.put("inf_average", resultSet.getString("inf_average"));
							jobj.put("time_duration", resultSet.getString("time_duration"));

							jobj.put("skip_reading_count", resultSet.getString("skip_reading_count"));
							jobj.put("inf_deviation", resultSet.getString("inf_deviation"));
							jobj.put("testruntype", resultSet.getString("test_run_type"));
							jobj.put("rep_no_of_readings", resultSet.getString("rep_no_of_readings"));
							jobj.put("voltage", resultSet.getString("inf_voltage"));
							ApplicationLauncher.logger.debug("MYSQL_Interface: Repeatability:testruntype: "  + resultSet.getString("test_run_type"));
							json_list.put(jobj);
							count++;
							break;


						//case "SelfHeating":
						case ConstantApp.TEST_PROFILE_SELF_HEATING:
							jobj.put("test_case_name", resultSet.getString("test_case_name"));
							jobj.put("inf_emin", resultSet.getString("inf_emin"));
							jobj.put("inf_emax", resultSet.getString("inf_emax"));
							jobj.put("inf_pulses", resultSet.getString("inf_pulses"));
							jobj.put("inf_average", resultSet.getString("inf_average"));
							jobj.put("time_duration", resultSet.getString("time_duration"));

							jobj.put("skip_reading_count", resultSet.getString("skip_reading_count"));
							jobj.put("inf_deviation", resultSet.getString("inf_deviation"));
							jobj.put("testruntype", resultSet.getString("test_run_type"));
							jobj.put("rep_no_of_readings", resultSet.getString("rep_no_of_readings"));
							jobj.put("voltage", resultSet.getString("inf_voltage"));
							ApplicationLauncher.logger.info("MYSQL_Interface: SelfHeating:testruntype: "  + resultSet.getString("test_run_type"));
							json_list.put(jobj);
							count++;
							break;

						default:
							break;
						}



					}

					hadResults = statement.getMoreResults();
					test_details_json.put("No_of_tests", count);
					test_details_json.put("test_details", json_list);

				}
				statement.close();


			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error("sp_ltgetproject_components : Exception1 :"+ ex.getMessage());
				statement.close();
				//ApplicationLauncher.logger.info ("sp_ltgetproject_components:Error 201: Source EM Model Reading: Failure");
				return test_details_json;
			}

		} catch (Exception ex) {

			ex.printStackTrace();	
			ApplicationLauncher.logger.error("sp_ltgetproject_components : Exception2 :"+ ex.getMessage());
			//ApplicationLauncher.logger.info ("sp_ltgetproject_components:Error 202: Source EM Model Reading: Failure");
			return test_details_json;
		}

		return test_details_json;
	}


	public JSONObject sp_ltgetproject_list() {


		JSONObject project_json = new JSONObject();
		JSONArray project_list = new JSONArray();
		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_getproject_list()}");


			boolean hadResults = statement.execute();


			int project_count = 0;
			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();

					// process result set
					while (resultSet.next()) {


						project_list.put(resultSet.getString("project_name"));
						project_count++;
					}

					hadResults = statement.getMoreResults();

					project_json.put("No_of_projects", project_count);
					project_json.put("Projects", project_list);
				}
				statement.close();


			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error("sp_ltgetproject_list : Exception1 :"+ ex.getMessage());
				statement.close();
				//ApplicationLauncher.logger.info ("sp_ltgetproject_list:Error 201: Source EM Model Reading: Failure");
				return project_json;
			}

		} catch (Exception ex) {

			ex.printStackTrace();	
			ApplicationLauncher.logger.error("sp_ltgetproject_list : Exception2 :"+ ex.getMessage());
			//ApplicationLauncher.logger.info ("sp_ltgetproject_list:Error 202: Source EM Model Reading: Failure");
			return project_json;
		}

		return project_json;
	}



	public boolean sp_ltadd_test_point_setup( String project_name,String value) {





		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_add_test_point_setup(?,?)}");
			statement.setString(1, project_name); 
			statement.setString(2, value);

			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {


				if (count==1){

					ApplicationLauncher.logger.info("sp_ltadd_test_point_setup: DB Success: ");
					return true;
				} else {


					ApplicationLauncher.logger.info("sp_ltadd_test_point_setup: DB failed: ");
					return false;
				}

			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error("sp_ltadd_test_point_setup : Exception1 :"+ ex.getMessage());
				return false;

			}



		} catch (Exception ex) {
			ex.printStackTrace();	
			ApplicationLauncher.logger.error("sp_ltadd_test_point_setup : Exception2 :"+ ex.getMessage());
			return false;
		}
	}

	public JSONObject sp_ltgettest_point_setup(String project_name) {


		ArrayList<String> test_setup_data_arr = new ArrayList<String>();
		JSONObject test_setup_data = new JSONObject();


		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_gettest_point_setup(?)}");
			statement.setString(1, project_name); 

			boolean hadResults = statement.execute();


			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();

					// process result set
					while (resultSet.next()) {
						test_setup_data_arr.add(resultSet.getString("selected_values"));
					}
					System.out.println("sp_ltgettest_point_setup: test_setup_data_arr: " + test_setup_data_arr);
					if(!test_setup_data_arr.isEmpty()){
						test_setup_data = ProcessTPData(test_setup_data_arr);
					}

					hadResults = statement.getMoreResults();

				}
				statement.close();


			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_ltgettest_point_setup : Exception1 :"+ ex.getMessage());
				statement.close();
				//ApplicationLauncher.logger.error ("sp_ltgettest_point_setup_1 :Error 201: Source EM Model Reading: Failure");
				return test_setup_data;
			}

		} catch (Exception ex) {

			ex.printStackTrace();	
			ApplicationLauncher.logger.error("sp_ltgettest_point_setup : Exception2 :"+ ex.getMessage());
			//ApplicationLauncher.logger.error ("sp_ltgettest_point_setup_1 : Error 202: Source EM Model Reading: Failure");
			return test_setup_data;
		}

		return test_setup_data;
	}

	public boolean sp_ltadd_em_model ( String customer_name, String model_name, String model_type, String model_class,
			String current_ib, String current_imax, String voltage_vd, String no_of_impluses,
			String frequency, String ct_type, String ctr_ratio, String ptr_ratio) {


		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_add_em_model(?,?,?,?,?,?,?,?,?,?,?,?)}");
			statement.setString(1, customer_name); 
			statement.setString(2, model_name); 
			statement.setString(3, model_type); 
			statement.setString(4, model_class);
			statement.setString(5, current_ib); 
			statement.setString(6, current_imax); 
			statement.setString(7, voltage_vd); 
			statement.setString(8, no_of_impluses);
			statement.setString(9, frequency);
			statement.setString(10, ctr_ratio);
			statement.setString(11, ptr_ratio);
			statement.setString(12, ct_type);
			

			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {


				if (count==1){

					ApplicationLauncher.logger.info("sp_ltadd_em_model: DB Success: ");
					return true;
				} else {


					ApplicationLauncher.logger.info("sp_ltadd_em_model: DB failed: ");
					return false;
				}




			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error("sp_ltadd_em_model : Exception1 :"+ ex.getMessage());
				return false;

			}



		} catch (Exception ex) {
			ex.printStackTrace();	
			ApplicationLauncher.logger.error("sp_ltadd_em_model : Exception2 :"+ ex.getMessage());
			return false;
		}
	}

	public JSONObject sp_ltgetem_model_list() {


		JSONObject em_model_data = new JSONObject();
		JSONArray em_models = new JSONArray();
		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_getem_model_list()}");


			boolean hadResults = statement.execute();


			int no_of_models = 0;
			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();

					// process result set
					while (resultSet.next()) {


						JSONObject model = new JSONObject();
						model.put("customer_name", resultSet.getString("customer_name"));
						model.put("model_name", resultSet.getString("model_name"));
						model.put("model_type", resultSet.getString("model_type"));
						model.put("ct_type", resultSet.getString("ct_type"));
						model.put("model_class", resultSet.getString("model_class"));
						model.put("basic_current_ib", resultSet.getString("basic_current_ib"));
						model.put("max_current_imax", resultSet.getString("max_current_imax"));
						model.put("rated_voltage_vd", resultSet.getString("rated_voltage_vd"));
						model.put("impulses_per_unit", resultSet.getString("impulses_per_unit"));
						model.put("model_id", resultSet.getString("model_id"));
						model.put("frequency", resultSet.getString("frequency"));
						model.put("ctr_ratio", resultSet.getString("ctr_ratio"));
						model.put("ptr_ratio", resultSet.getString("ptr_ratio"));
						model.put("customer_name", resultSet.getString("customer_name"));
						
						em_models.put(model);
						no_of_models++;
					}


					hadResults = statement.getMoreResults();
					em_model_data.put("No_of_models", no_of_models);
					em_model_data.put("EM_models", em_models);
				}
				statement.close();


			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error("sp_ltgetem_model_list : Exception1 :"+ ex.getMessage());
				statement.close();
				//ApplicationLauncher.logger.error ("sp_ltgetem_model_list:Error 201: Source EM Model Reading: Failure");
				return em_model_data;
			}

		} catch (Exception ex) {

			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_ltgetem_model_list : Exception2 :"+ ex.getMessage());
			//ApplicationLauncher.logger.error ("sp_ltgetem_model_list:Error 202: Source EM Model Reading: Failure");
			return em_model_data;
		}

		return em_model_data;
	}

	public boolean sp_ltdelete_em_model ( String customer_name, String model_name) {




		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_delete_em_model(?,?)}");
			statement.setString(1, customer_name); 
			statement.setString(2, model_name); 

			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {


				if (count==1){

					ApplicationLauncher.logger.info("sp_ltdelete_em_model: DB Success: ");
					return true;
				} else {


					ApplicationLauncher.logger.info("sp_ltdelete_em_model: DB failed: ");
					return false;
				}

				//statement.close();


			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_ltdelete_em_model : Exception1 :"+ ex.getMessage());
				return false;

			}



		} catch (Exception ex) {
			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_ltdelete_em_model : Exception2 :"+ ex.getMessage());
			return false;
		}
	}


	public boolean sp_ltadd_device_settings (int id, String device_type, String model_name, String port_name, String baud_rate) {



		try {

			CallableStatement statement = ConnectManager.prepareCall("{call sp_add_device_setting(?,?,?,?,?)}");
			statement.setInt(1, id); 
			statement.setString(2, device_type); 
			statement.setString(3, model_name); 
			statement.setString(4, port_name); 
			statement.setString(5, baud_rate);

			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {


				ApplicationLauncher.logger.info("sp_ltadd_device_settings : count: " + count);
				if (count==1){

					ApplicationLauncher.logger.info("sp_ltadd_device_settings: DB Success: ");
					return true;
				} else {


					ApplicationLauncher.logger.info("sp_ltadd_device_settings: DB failed: ");
					return false;
				}




			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error("sp_ltadd_device_settings : Exception1 :"+ ex.getMessage());
				return false;

			}	



		} catch (Exception ex) {
			ex.printStackTrace();	
			ApplicationLauncher.logger.error("sp_ltadd_device_settings : Exception2 :"+ ex.getMessage());
			return false;
		}
	}


	public JSONObject sp_ltgetdevice_setting(String device_type) {




		JSONObject device_setting = new JSONObject();

		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_getdevice_setting(?)}");
			statement.setString(1, device_type); 

			boolean hadResults = statement.execute();






			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();

					// process result set
					while (resultSet.next()) {


						device_setting.put("model_name", resultSet.getString("model_name"));
						device_setting.put("port_name", resultSet.getString("port_name"));
						device_setting.put("baud_rate", resultSet.getString("baud_rate"));
					}

					hadResults = statement.getMoreResults();

				}
				statement.close();


			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error("sp_ltgetdevice_setting : Exception1:"+ ex.getMessage());
				statement.close();
				//ApplicationLauncher.logger.error ("sp_ltgetdevice_setting:Error 201: Source EM Model Reading: Failure");
				return device_setting;
			}

		} catch (Exception ex) {

			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_ltgetdevice_setting : Exception2 ::"+ ex.getMessage());
			//ApplicationLauncher.logger.error ("sp_ltgetdevice_setting:Error 202: Source EM Model Reading: Failure");
			return device_setting;
		}

		return device_setting;
	}



	public boolean sp_ltadd_result (String project_name, String test_case_name, 
			String alias_id, String str_rack_id, 
			String test_result,int error_id, String error_value,String FailureReason,
			String data_type,String executionMctNctMode,String energyFlowMode, String deploymentId,int seqNumber) {

		long time_stamp = System.currentTimeMillis() / 1000L;
		int rack_id = Integer.parseInt(str_rack_id);
		if(ProcalFeatureEnable.EXPORT_MODE_ENABLED){
			if(test_case_name.contains(ConstantApp.EXPORT_MODE_ALIAS_NAME)){
				rack_id = rack_id+ConstantApp.EXPORT_MODE_DEVICE_ID_THRESHOLD;
			}
		}

		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_add_result(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			statement.setString(1, project_name); 
			statement.setString(2, test_case_name); 
			statement.setString(3, alias_id); 
			//statement.setInt(4, Integer.parseInt(rack_id)); 
			statement.setInt(4,rack_id); 
			statement.setLong(5, time_stamp); 
			statement.setString(6, test_result);
			statement.setInt(7, error_id);
			statement.setString(8, error_value);
			statement.setString(9, FailureReason);
			statement.setString(10, data_type);
			statement.setString(11, executionMctNctMode);
			statement.setString(12, energyFlowMode);
			statement.setString(13, deploymentId);
			statement.setInt(14, seqNumber);


			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {

				if (count==1){

					//ApplicationLauncher.logger.info("sp_ltadd_result: DB Success: ");
					return true;
				} else {


					ApplicationLauncher.logger.info("sp_ltadd_result: DB failed: ");
					return false;
				}




			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_ltadd_result : Exception1 :"+ ex.getMessage());
				return false;

			}	



		} catch (Exception ex) {
			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_ltadd_result : Exception2 :"+ ex.getMessage());
			return false;
		}
	}



	public JSONObject sp_ltgetresult_testpoint_data(long fromtime, long totime, String project_name,String test_point,String data_type) {


		JSONObject result_json = new JSONObject();
		JSONArray result_arr =new JSONArray();


		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_ltgetresult_testpoint_data(?,?,?,?,?)}");
			statement.setLong(1, fromtime); 
			statement.setLong(2, totime);
			statement.setString(3, project_name);
			statement.setString(4, test_point);
			statement.setString(5, data_type);

			boolean hadResults = statement.execute();


			int count =0;
			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();

					// process result set
					while (resultSet.next()) {



						JSONObject jobj = new JSONObject();
						jobj.put("test_case_name", resultSet.getString("test_case_name"));
						jobj.put("alias_id", resultSet.getString("alias_id"));
						jobj.put("device_name", resultSet.getString("device_name"));
						jobj.put("test_status", resultSet.getString("test_result"));
						jobj.put("error_value", resultSet.getString("error_value"));
						jobj.put("failure_reason", resultSet.getString("failure_reason"));

						result_arr.put(jobj);
						count++;
					}



					hadResults = statement.getMoreResults();

					result_json.put("No_of_results", count);
					result_json.put("Results", result_arr);

				}
				statement.close();


			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_ltgetresult_testpoint_data : Exception1 :"+ ex.getMessage());
				statement.close();
				//ApplicationLauncher.logger.error ("sp_ltgetresult_testpoint_data :Error 2001: sp_ltgetresult_testpoint_data: Failure");

				return result_json;
			}

		} catch (Exception ex) {

			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_ltgetresult_testpoint_data: Exception2 :"+ ex.getMessage());
			//ApplicationLauncher.logger.error ("sp_ltgetresult_testpoint_data :Error 2002: sp_ltgetresult_testpoint_data: Failure");

			return result_json;
		}

		return result_json;
	}

	
	public JSONObject sp_procal_add_deploy_manage_v1_1 (String project_name, 
			String customer_name, String equipment_serial_no, String customer_reference_no,			String ulr_no,
			String isMCT_Type, String isNCT_Type, String isMCT_TestingCompleted, String isNCT_TestingCompleted,
			String execution_status,long deployedTime,long deployedTimeMaxSearchLimit,
			long executionCompletedTime, String testerName,String energyFlowModeSelected,String autoDeployEnabled) {
		
		boolean status = false;
		String deploymentID="";
		String comments = "";
		JSONObject resultjson = new JSONObject();
		

		try {
			
			CallableStatement statement = ConnectManager.prepareCall("{call sp_procal_add_deploy_manage_v1_1(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			statement.setString(1, project_name); 
			statement.setString(2, customer_name); 
			statement.setString(3, equipment_serial_no); 
			statement.setString(4, customer_reference_no); 
			statement.setString(5, ulr_no); 
			statement.setString(6, isMCT_Type); 
			statement.setString(7, isNCT_Type); 
			statement.setString(8, isMCT_TestingCompleted);
			statement.setString(9, isNCT_TestingCompleted);
			statement.setString(10, execution_status);
			statement.setLong(11, deployedTime);
			statement.setLong(12, deployedTimeMaxSearchLimit);
			statement.setLong(13, executionCompletedTime);
			statement.setString(14, testerName);
			statement.setString(15, energyFlowModeSelected);
			statement.setString(16, autoDeployEnabled);
			
			ApplicationLauncher.logger.info("sp_procal_add_deploy_manage_v1_1: project_name : "+project_name);
			ApplicationLauncher.logger.info("sp_procal_add_deploy_manage_v1_1: customer_name : "+customer_name);
			ApplicationLauncher.logger.info("sp_procal_add_deploy_manage_v1_1: equipment_serial_no : "+equipment_serial_no);
			ApplicationLauncher.logger.info("sp_procal_add_deploy_manage_v1_1: isMCT_Type : "+isMCT_Type);
			ApplicationLauncher.logger.info("sp_procal_add_deploy_manage_v1_1: isNCT_Type : "+isNCT_Type);
			ApplicationLauncher.logger.info("sp_procal_add_deploy_manage_v1_1: customer_reference_no : "+customer_reference_no);
			ApplicationLauncher.logger.info("sp_procal_add_deploy_manage_v1_1: ulr_no : "+ulr_no);
			ApplicationLauncher.logger.info("sp_procal_add_deploy_manage_v1_1: isMCT_TestingCompleted : "+isMCT_TestingCompleted);
			ApplicationLauncher.logger.info("sp_procal_add_deploy_manage_v1_1: isNCT_TestingCompleted : "+isNCT_TestingCompleted);
			ApplicationLauncher.logger.info("sp_procal_add_deploy_manage_v1_1: execution_status : "+execution_status);
			ApplicationLauncher.logger.info("sp_procal_add_deploy_manage_v1_1: deployedTime : "+deployedTime);
			ApplicationLauncher.logger.info("sp_procal_add_deploy_manage_v1_1: deployedTimeMaxSearchLimit : "+deployedTimeMaxSearchLimit);
			ApplicationLauncher.logger.info("sp_procal_add_deploy_manage_v1_1: executionCompletedTime : "+executionCompletedTime);
			ApplicationLauncher.logger.info("sp_procal_add_deploy_manage_v1_1: energyFlowModeSelected : "+energyFlowModeSelected);
			ApplicationLauncher.logger.info("sp_procal_add_deploy_manage_v1_1: autoDeployEnabled : "+autoDeployEnabled);
			
			boolean hadResults = statement.execute();
			//statement.executeUpdate(Statement.RETURN_GENERATED_KEYS);

			int count = statement.getUpdateCount();
			ApplicationLauncher.logger.info("sp_ltadd_deploy_devices : count: "+count);
			//statement.close();
			
			try {



				if (count==1){
					ApplicationLauncher.logger.info("sp_ltadd_deploy_devices :DB Success: ");
/*					try {
						
						while (hadResults) {
							ResultSet resultSet = statement.getResultSet();

							// process result set
							//int No_of_deployment = 0;
							while (resultSet.next()) {
								ApplicationLauncher.logger.info("sp_ltadd_deploy_devices : last inserted deployment_id:"+resultSet.getString(Constant_Mysql.DEPLOYMENT_LAST_INSERTED_ID_COLUMN_NAME));
							}
							hadResults = statement.getMoreResults();
						}

					}catch(Exception e) {
						e.printStackTrace();	
						ApplicationLauncher.logger.error ("sp_procal_add_deploy_manage_v1_1 : Exception3 :"+ e.getMessage());
					}*/
					statement.close();
					status= true;
				} else {

					
					try {
						
						while (hadResults) {
							ResultSet resultSet = statement.getResultSet();

							// process result set
							//int No_of_deployment = 0;
							while (resultSet.next()) {
								deploymentID = resultSet.getString(Constant_Mysql.DEPLOYMENT_LAST_INSERTED_ID_COLUMN_NAME);
								comments = resultSet.getString(Constant_Mysql.DEPLOYMENT_LAST_UPDATED_ID_COMMENTS_COLUMN_NAME);
								ApplicationLauncher.logger.info("sp_ltadd_deploy_devices : last deployment_id:"+resultSet.getString(Constant_Mysql.DEPLOYMENT_LAST_INSERTED_ID_COLUMN_NAME));
								ApplicationLauncher.logger.info("sp_ltadd_deploy_devices : last comments:"+resultSet.getString(Constant_Mysql.DEPLOYMENT_LAST_UPDATED_ID_COMMENTS_COLUMN_NAME));
								
							}
							hadResults = statement.getMoreResults();
						}
						ApplicationLauncher.logger.info("sp_procal_add_deploy_manage_v1_1: DB Success2: ");
						status = true;
						//while (hadResults) {
						//	int resultSet = statement;
							//ApplicationLauncher.logger.info("sp_ltadd_deploy_devices : deployment_id:"+resultSet);
							//ResultSet resultSet = statement.getResultSet();
									//ResultSet resultSet = statement.getResultSet();
							//ApplicationLauncher.logger.info("sp_ltadd_deploy_devices : deployment_id:"+resultSet.getInt(1));
							//resultSet = statement.getInt(1);
							//ResultSet resultSet = statement.getResultSet();
							//ApplicationLauncher.logger.info("sp_ltadd_deploy_devices : deployment_id:"+resultSet);
							//int autoGeneratedKey = 0;

							//while (resultSet.next()) {
								//autoGeneratedKey = resultSet.getString("result");
								//ApplicationLauncher.logger.info("sp_ltadd_deploy_devices : result:"+resultSet.getString("result"));
								//autoGeneratedKey = resultSet.getInt(1);
								//ApplicationLauncher.logger.info("sp_ltadd_deploy_devices : deployment_id:"+autoGeneratedKey);
							//}
						//}
					}catch(Exception e) {
						e.printStackTrace();	
						ApplicationLauncher.logger.info("sp_procal_add_deploy_manage_v1_1: DB failed: ");
						ApplicationLauncher.logger.error ("sp_procal_add_deploy_manage_v1_1 : Exception2 :"+ e.getMessage());
					}


					
					statement.close();
					//return status;
				}
				




			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_procal_add_deploy_manage_v1_1 : Exception1 :"+ ex.getMessage());
				//statement.close();
				status= false;

			}	


			resultjson.put("status", status);
			resultjson.put("deployment_id", deploymentID);
			resultjson.put("comments", comments);
		} catch (Exception ex) {
			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_procal_add_deploy_manage_v1_1 : Exception3 :"+ ex.getMessage());

			status =  false;

		}
		return resultjson;
	}
	
	
	public JSONObject sp_procal_add_deploy_manage (String project_name, 
			String customer_name, String equipment_serial_no, String customer_reference_no,			String ulr_no,
			String isMCT_Type, String isNCT_Type, String isMCT_TestingCompleted, String isNCT_TestingCompleted,
			String execution_status,long deployedTime,long deployedTimeMaxSearchLimit,
			long executionCompletedTime, String testerName,String energyFlowModeSelected) {
		
		boolean status = false;
		String deploymentID="";
		String comments = "";
		JSONObject resultjson = new JSONObject();
		

		try {
			
			CallableStatement statement = ConnectManager.prepareCall("{call sp_add_deploy_manage(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			statement.setString(1, project_name); 
			statement.setString(2, customer_name); 
			statement.setString(3, equipment_serial_no); 
			statement.setString(4, customer_reference_no); 
			statement.setString(5, ulr_no); 
			statement.setString(6, isMCT_Type); 
			statement.setString(7, isNCT_Type); 
			statement.setString(8, isMCT_TestingCompleted);
			statement.setString(9, isNCT_TestingCompleted);
			statement.setString(10, execution_status);
			statement.setLong(11, deployedTime);
			statement.setLong(12, deployedTimeMaxSearchLimit);
			statement.setLong(13, executionCompletedTime);
			statement.setString(14, testerName);
			statement.setString(15, energyFlowModeSelected);
			//statement.setString(16, autoDeployEnabled);
			
			ApplicationLauncher.logger.info("sp_procal_add_deploy_manage: project_name : "+project_name);
			ApplicationLauncher.logger.info("sp_procal_add_deploy_manage: customer_name : "+customer_name);
			ApplicationLauncher.logger.info("sp_procal_add_deploy_manage: equipment_serial_no : "+equipment_serial_no);
			ApplicationLauncher.logger.info("sp_procal_add_deploy_manage: isMCT_Type : "+isMCT_Type);
			ApplicationLauncher.logger.info("sp_procal_add_deploy_manage: isNCT_Type : "+isNCT_Type);
			ApplicationLauncher.logger.info("sp_procal_add_deploy_manage: customer_reference_no : "+customer_reference_no);
			ApplicationLauncher.logger.info("sp_procal_add_deploy_manage: ulr_no : "+ulr_no);
			ApplicationLauncher.logger.info("sp_procal_add_deploy_manage: isMCT_TestingCompleted : "+isMCT_TestingCompleted);
			ApplicationLauncher.logger.info("sp_procal_add_deploy_manage: isNCT_TestingCompleted : "+isNCT_TestingCompleted);
			ApplicationLauncher.logger.info("sp_procal_add_deploy_manage: execution_status : "+execution_status);
			ApplicationLauncher.logger.info("sp_procal_add_deploy_manage: deployedTime : "+deployedTime);
			ApplicationLauncher.logger.info("sp_procal_add_deploy_manage: deployedTimeMaxSearchLimit : "+deployedTimeMaxSearchLimit);
			ApplicationLauncher.logger.info("sp_procal_add_deploy_manage: executionCompletedTime : "+executionCompletedTime);
			ApplicationLauncher.logger.info("sp_procal_add_deploy_manage: energyFlowModeSelected : "+energyFlowModeSelected);
			//ApplicationLauncher.logger.info("sp_procal_add_deploy_manage: autoDeployEnabled : "+autoDeployEnabled);
			
			boolean hadResults = statement.execute();
			//statement.executeUpdate(Statement.RETURN_GENERATED_KEYS);

			int count = statement.getUpdateCount();
			ApplicationLauncher.logger.info("sp_ltadd_deploy_devices : count: "+count);
			//statement.close();
			
			try {



				if (count==1){
					ApplicationLauncher.logger.info("sp_ltadd_deploy_devices :DB Success: ");
/*					try {
						
						while (hadResults) {
							ResultSet resultSet = statement.getResultSet();

							// process result set
							//int No_of_deployment = 0;
							while (resultSet.next()) {
								ApplicationLauncher.logger.info("sp_ltadd_deploy_devices : last inserted deployment_id:"+resultSet.getString(Constant_Mysql.DEPLOYMENT_LAST_INSERTED_ID_COLUMN_NAME));
							}
							hadResults = statement.getMoreResults();
						}

					}catch(Exception e) {
						e.printStackTrace();	
						ApplicationLauncher.logger.error ("sp_procal_add_deploy_manage : Exception3 :"+ e.getMessage());
					}*/
					statement.close();
					status= true;
				} else {

					
					try {
						
						while (hadResults) {
							ResultSet resultSet = statement.getResultSet();

							// process result set
							//int No_of_deployment = 0;
							while (resultSet.next()) {
								deploymentID = resultSet.getString(Constant_Mysql.DEPLOYMENT_LAST_INSERTED_ID_COLUMN_NAME);
								comments = resultSet.getString(Constant_Mysql.DEPLOYMENT_LAST_UPDATED_ID_COMMENTS_COLUMN_NAME);
								ApplicationLauncher.logger.info("sp_ltadd_deploy_devices : last deployment_id:"+resultSet.getString(Constant_Mysql.DEPLOYMENT_LAST_INSERTED_ID_COLUMN_NAME));
								ApplicationLauncher.logger.info("sp_ltadd_deploy_devices : last comments:"+resultSet.getString(Constant_Mysql.DEPLOYMENT_LAST_UPDATED_ID_COMMENTS_COLUMN_NAME));
								
							}
							hadResults = statement.getMoreResults();
						}
						ApplicationLauncher.logger.info("sp_procal_add_deploy_manage: DB Success2: ");
						status = true;
						//while (hadResults) {
						//	int resultSet = statement;
							//ApplicationLauncher.logger.info("sp_ltadd_deploy_devices : deployment_id:"+resultSet);
							//ResultSet resultSet = statement.getResultSet();
									//ResultSet resultSet = statement.getResultSet();
							//ApplicationLauncher.logger.info("sp_ltadd_deploy_devices : deployment_id:"+resultSet.getInt(1));
							//resultSet = statement.getInt(1);
							//ResultSet resultSet = statement.getResultSet();
							//ApplicationLauncher.logger.info("sp_ltadd_deploy_devices : deployment_id:"+resultSet);
							//int autoGeneratedKey = 0;

							//while (resultSet.next()) {
								//autoGeneratedKey = resultSet.getString("result");
								//ApplicationLauncher.logger.info("sp_ltadd_deploy_devices : result:"+resultSet.getString("result"));
								//autoGeneratedKey = resultSet.getInt(1);
								//ApplicationLauncher.logger.info("sp_ltadd_deploy_devices : deployment_id:"+autoGeneratedKey);
							//}
						//}
					}catch(Exception e) {
						e.printStackTrace();	
						ApplicationLauncher.logger.info("sp_procal_add_deploy_manage: DB failed: ");
						ApplicationLauncher.logger.error ("sp_procal_add_deploy_manage : Exception2 :"+ e.getMessage());
					}


					
					statement.close();
					//return status;
				}
				




			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_procal_add_deploy_manage : Exception1 :"+ ex.getMessage());
				//statement.close();
				status= false;

			}	


			resultjson.put("status", status);
			resultjson.put("deployment_id", deploymentID);
			resultjson.put("comments", comments);
		} catch (Exception ex) {
			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_procal_add_deploy_manage : Exception3 :"+ ex.getMessage());

			status =  false;

		}
		return resultjson;
	}
	
	
	
	public boolean sp_ltdelete_deploy_test_cases( String project_name,String deploymentID) {



		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_delete_deploy_test_cases(?,?)}");
			statement.setString(1, project_name);
			statement.setString(2, deploymentID);
			//statement.setString(3, testType);

			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {
			if (count==1){


					ApplicationLauncher.logger.info("sp_ltdelete_deploy_test_cases: DB Success: ");
					return true;
				} else {

				ApplicationLauncher.logger.info("sp_ltdelete_deploy_test_cases: DB failed: ");
					return false;
				}

			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_ltdelete_deploy_test_cases : Exception1 :"+ ex.getMessage());
				return false;

			}

		} catch (Exception ex) {
			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_ltdelete_deploy_test_cases : Exception2 :"+ ex.getMessage());
			return false;
		}
	}

	public JSONObject sp_ltgetresult_data(long fromtime, long totime, String project_name,String data_type) {


		JSONObject result_json = new JSONObject();
		JSONArray result_arr =new JSONArray();


		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_getresult_data(?,?,?,?)}");
			statement.setLong(1, fromtime); 
			statement.setLong(2, totime);
			statement.setString(3, project_name);
			statement.setString(4, data_type);

			boolean hadResults = statement.execute();






			int count =0;
			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();

					// process result set
					while (resultSet.next()) {


						JSONObject jobj = new JSONObject();
						jobj.put("test_case_name", resultSet.getString("test_case_name"));
						jobj.put("alias_id", resultSet.getString("alias_id"));

						jobj.put("device_name", resultSet.getInt("device_name"));
						jobj.put("test_status", resultSet.getString("test_result"));
						jobj.put("error_value", resultSet.getString("error_value"));

						result_arr.put(jobj);
						count++;
					}


					hadResults = statement.getMoreResults();

					result_json.put("No_of_results", count);
					result_json.put("Results", result_arr);

				}
				statement.close();


			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_ltgetresult_data : Exception1 :"+ ex.getMessage());
				statement.close();
				//ApplicationLauncher.logger.error ("sp_ltgetresult_data :Error 201: Source EM Model Reading: Failure");

				return result_json;
			}

		} catch (Exception ex) {

			ex.printStackTrace();	
			ApplicationLauncher.logger.error("sp_ltgetresult_data : Exception2 :"+ ex.getMessage());
			//ApplicationLauncher.logger.error ("sp_ltgetresult_data :Error 202: Source EM Model Reading: Failure");

			return result_json;
		}

		return result_json;
	}
	
	public JSONObject sp_ltgetresult_dataV2(long fromtime, long totime, String project_name,String data_type,String deploymentID,String mctNctMode,String energyMode) {


		JSONObject result_json = new JSONObject();
		JSONArray result_arr =new JSONArray();

		try {

			CallableStatement statement = ConnectManager.prepareCall("{call sp_getresult_dataV2(?,?,?,?,?,?,?)}");
			statement.setLong(1, fromtime); 
			statement.setLong(2, totime);
			statement.setString(3, project_name);
			statement.setString(4, data_type);
			statement.setString(5, deploymentID);
			statement.setString(6, mctNctMode);
			statement.setString(7, energyMode);

			boolean hadResults = statement.execute();

			int count =0;
			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();

					// process result set
					while (resultSet.next()) {

						JSONObject jobj = new JSONObject();
						jobj.put("test_case_name", resultSet.getString("test_case_name"));
						jobj.put("alias_id", resultSet.getString("alias_id"));

						jobj.put("device_name", resultSet.getInt("device_name"));
						jobj.put("test_status", resultSet.getString("test_result"));
						jobj.put("error_value", resultSet.getString("error_value"));

						result_arr.put(jobj);
						count++;
					}

					hadResults = statement.getMoreResults();

					result_json.put("No_of_results", count);
					result_json.put("Results", result_arr);

				}
				statement.close();

			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_ltgetresult_data : Exception1 :"+ ex.getMessage());
				statement.close();
				//ApplicationLauncher.logger.error ("sp_ltgetresult_data :Error 201: Source EM Model Reading: Failure");

				return result_json;
			}

		} catch (Exception ex) {

			ex.printStackTrace();	
			ApplicationLauncher.logger.error("sp_ltgetresult_data : Exception2 :"+ ex.getMessage());
			//ApplicationLauncher.logger.error ("sp_ltgetresult_data :Error 202: Source EM Model Reading: Failure");

			return result_json;
		}

		return result_json;
	}


	public JSONObject sp_ltgetresult_project_data(long fromtime, long totime) {




		JSONObject project_json = new JSONObject();
		JSONArray project_list = new JSONArray();

		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_getresult_project_data(?,?)}");
			statement.setLong(1, fromtime); 
			statement.setLong(2, totime); 

			boolean hadResults = statement.execute();





			int project_count = 0;
			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();

					// process result set
					while (resultSet.next()) {


						project_list.put(resultSet.getString("project_name"));
						project_count++;
					}

					hadResults = statement.getMoreResults();

					project_json.put("No_of_projects", project_count);
					project_json.put("Projects", project_list);
				}
				statement.close();





			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_ltgetresult_project_data : Exception1 :"+ ex.getMessage());
				statement.close();
				//ApplicationLauncher.logger.error ("sp_ltgetresult_project_data :Error 201: Source EM Model Reading: Failure");
				return project_json;
			}

		} catch (Exception ex) {

			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_ltgetresult_project_data : Exception2 :"+ ex.getMessage());
			//ApplicationLauncher.logger.error ("sp_ltgetresult_project_data :Error 202: Source EM Model Reading: Failure");
			return project_json;
		}
		return project_json;
	}

	public boolean sp_ltadd_project_model_mapping (String project_name, int model_id) {







		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_add_project_model_mapping(?,?)}");
			statement.setString(1, project_name); 
			statement.setInt(2, model_id); 

			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {



				if (count==1){


					//ApplicationLauncher.logger.info("sp_ltadd_project_model_mapping: DB Success: ");
					return true;
				} else {



					ApplicationLauncher.logger.info("sp_ltadd_project_model_mapping: DB failed: ");
					return false;
				}




			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_ltadd_project_model_mapping : Exception1 :"+ ex.getMessage());
				return false;

			}	



		} catch (Exception ex) {
			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_ltadd_project_model_mapping : Exception2 :"+ ex.getMessage());
			return false;
		}
	}

	public JSONObject sp_ltgetem_model_data(int model_id) {


		JSONObject model_data = new JSONObject();

		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_getem_model_data(?)}");
			statement.setInt(1, model_id); 

			boolean hadResults = statement.execute();






			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();

					// process result set
					while (resultSet.next()) {


						model_data.put("customer_name", resultSet.getString("customer_name"));
						//ApplicationLauncher.logger.info("sp_ltgetem_model_data: model_name:"+resultSet.getString("model_name"));
						model_data.put("model_name", resultSet.getString("model_name"));
						model_data.put("model_type", resultSet.getString("model_type"));
						model_data.put("ct_type", resultSet.getString("ct_type"));
						model_data.put("model_class", resultSet.getString("model_class"));
						model_data.put("basic_current_ib", resultSet.getString("basic_current_ib"));
						model_data.put("max_current_imax", resultSet.getString("max_current_imax"));
						model_data.put("rated_voltage_vd", resultSet.getString("rated_voltage_vd"));
						model_data.put("impulses_per_unit", resultSet.getString("impulses_per_unit"));
						model_data.put("frequency", resultSet.getString("frequency"));
						model_data.put("ctr_ratio", resultSet.getString("ctr_ratio"));
						model_data.put("ptr_ratio", resultSet.getString("ptr_ratio"));
					}

					hadResults = statement.getMoreResults();

				}
				statement.close();





			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error("sp_ltgetem_model_data : Exception1 :"+ ex.getMessage());
				statement.close();
				//ApplicationLauncher.logger.error ("sp_ltgetem_model_data:Error 201: Source EM Model Reading: Failure");
				return model_data;
			}

		} catch (Exception ex) {

			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_ltgetem_model_data : Exception2 :"+ ex.getMessage());
			//ApplicationLauncher.logger.error ("sp_ltgetem_model_data :Error 202: Source EM Model Reading: Failure");
			return model_data;
		}
		return model_data;
	}

	public int sp_ltgetProjectModel_ID(String project_name) {



		int model_ID = 0;

		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_getprojectmodel_id(?)}");
			statement.setString(1, project_name); 

			boolean hadResults = statement.execute();






			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();

					// process result set
					while (resultSet.next()) {

						model_ID = resultSet.getInt("model_id");


					}

					hadResults = statement.getMoreResults();

				}
				statement.close();





			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_ltgetProjectModel_ID : Exception1 :"+ ex.getMessage());
				statement.close();
				//ApplicationLauncher.logger.error ("sp_ltgetProjectModel_ID :Error 201: Source EM Model Reading: Failure");
				return model_ID;
			}

		} catch (Exception ex) {

			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_ltgetProjectModel_ID : Exception2 :"+ ex.getMessage());
			//ApplicationLauncher.logger.error ("sp_ltgetProjectModel_ID :Error 202: Source EM Model Reading: Failure");
			return model_ID;
		}
		return model_ID;
	}

	public int sp_ltgetmodel_id(String EM_ModelName) {



		int model_ID = 0;

		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_getmodel_id(?)}");
			statement.setString(1, EM_ModelName); 

			boolean hadResults = statement.execute();






			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();

					// process result set
					while (resultSet.next()) {


						model_ID = resultSet.getInt("model_id");


					}

					hadResults = statement.getMoreResults();

				}
				statement.close();





			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error("sp_ltgetmodel_id : Exception1 :"+ ex.getMessage());
				statement.close();
				//ApplicationLauncher.logger.error ("sp_ltgetmodel_id :Error 201: Source EM Model Reading: Failure");
				return model_ID;
			}

		} catch (Exception ex) {

			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_ltgetmodel_id : Exception2 :"+ ex.getMessage());
			//ApplicationLauncher.logger.error ("sp_ltgetmodel_id :Error 202: Source EM Model Reading: Failure");
			return model_ID;
		}
		return model_ID;


	}


	public boolean sp_ltadd_project_scheduled_time (String project_name, String sche_date, String sche_time, long time_stamp) {







		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_add_project_scheduled_time(?,?,?,?)}");
			statement.setString(1, project_name); 
			statement.setString(2, sche_date); 
			statement.setString(3, sche_time); 
			statement.setLong(4, time_stamp); 

			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {



				if (count==1){


					ApplicationLauncher.logger.info("sp_ltadd_project_scheduled_time: DB Success: ");
					return true;
				} else {



					ApplicationLauncher.logger.info("sp_ltadd_project_scheduled_time: DB failed: ");
					return false;
				}




			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_ltadd_project_scheduled_time : Exception1 :"+ ex.getMessage());
				return false;

			}	



		} catch (Exception ex) {
			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_ltadd_project_scheduled_time : Exception2 :"+ ex.getMessage());
			return false;
		}
	}


	public JSONObject sp_ltgetproject_scheduled_time(long from_timestamp, long to_timestamp) {




		JSONObject result = new JSONObject();
		JSONArray project_sche_time = new JSONArray();

		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_getproject_scheduled_time(?,?)}");
			statement.setLong(1, from_timestamp); 
			statement.setLong(2, to_timestamp); 

			boolean hadResults = statement.execute();






			int count = 0;
			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();

					// process result set
					while (resultSet.next()) {


						JSONObject jobj = new JSONObject();
						jobj.put("Project_name", resultSet.getString("project_name"));
						jobj.put("Scheduled_time", resultSet.getString("scheduled_time"));
						project_sche_time.put(jobj);
						count++;
					}

					hadResults = statement.getMoreResults();
					result.put("Project_count", count);
					result.put("Project_sche_time", project_sche_time);

				}
				statement.close();





			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_ltgetproject_scheduled_time : Exception1 :"+ ex.getMessage());
				statement.close();
				ApplicationLauncher.logger.error ("sp_ltgetproject_scheduled_time :Error 201: Source EM Model Reading: Failure");
				return result;
			}

		} catch (Exception ex) {

			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_ltgetproject_scheduled_time : Exception2 :"+ ex.getMessage());
			ApplicationLauncher.logger.error ("sp_ltgetproject_scheduled_time :Error 202: Source EM Model Reading: Failure");
			return result;
		}
		return result;
	}

	public JSONObject sp_ltgetsummary_data(String project_name) {



		JSONObject summary_data = new JSONObject();
		JSONArray testcases = new JSONArray();

		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_getsummary_data(?)}");
			statement.setString(1, project_name); 

			boolean hadResults = statement.execute();






			int count = 0; 
			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();

					// process result set
					while (resultSet.next()) {



						JSONObject jobj = new JSONObject();
						jobj.put("test_case_name", resultSet.getString("test_case_name"));
						jobj.put("test_alias_id", resultSet.getString("test_alias_id"));
						jobj.put("test_type", resultSet.getString("test_type"));
						jobj.put("sequence_no", resultSet.getString("sequence_no"));
						testcases.put(jobj);
						count++;

					}

					hadResults = statement.getMoreResults();
					summary_data.put("No_of_test_cases", count);
					summary_data.put("Summary_data", testcases);

				}
				statement.close();





			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_ltgetsummary_data : Exception1 :"+ ex.getMessage());
				statement.close();
				//ApplicationLauncher.logger.error ("sp_ltgetsummary_data :Error 201: Source EM Model Reading: Failure");
				return summary_data;
			}

		} catch (Exception ex) {

			ex.printStackTrace();	
			ApplicationLauncher.logger.error("sp_ltgetsummary_data : Exception2 :"+ ex.getMessage());
			//ApplicationLauncher.logger.error ("sp_ltgetsummary_data :Error 202: Source EM Model Reading: Failure");
			return summary_data;
		}
		return summary_data;
	}

	public boolean sp_ltadd_deploy_test_cases (String lastUpdatedDeploymentID, String project_name, String test_case, String test_type, String alias_id,String sequence_no, String is_deployed) {


		try {

			CallableStatement statement = ConnectManager.prepareCall("{call sp_add_deploy_test_cases(?,?,?,?,?,?,?)}");
			statement.setString(1, lastUpdatedDeploymentID); 
			statement.setString(2, project_name); 
			statement.setString(3, test_case); 
			statement.setString(4, test_type);
			statement.setString(5, alias_id);		
			statement.setString(6, sequence_no); 
			statement.setString(7, is_deployed);

			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {
				if (count==1){
					//ApplicationLauncher.logger.info("sp_ltadd_deploy_test_cases: DB Success: ");
					return true;
				} else {
					ApplicationLauncher.logger.info("sp_ltadd_deploy_test_cases: DB failed: ");
					return false;
				}
			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error("sp_ltadd_deploy_test_cases : Exception1 :"+ ex.getMessage());
				return false;

			}	
		} catch (Exception ex) {
			ex.printStackTrace();	
			ApplicationLauncher.logger.error("sp_ltadd_deploy_test_cases : Exception2 :"+ ex.getMessage());
			return false;
		}
	}
	
	
//	public boolean sp_ltadd_deploy_test_cases_V2 (String lastUpdatedDeploymentID, String project_name,String sequence_no, 
//			String is_deployed, DeploymentTestCaseDataModel deployModel) {
//
//
//		try {
//			int position = 1;
//			CallableStatement statement = ConnectManager.prepareCall("{call sp_add_deploy_test_cases_v2(?,?,?,?,?,?,?,?  ,?,?,?,?,?,?,?,?,?,?,?,?,?,? ,?,?,?,? ,?,?,?,?)}");
//			statement.setString(position++, lastUpdatedDeploymentID); 
//			statement.setString(position++, project_name); 
//			statement.setString(position++, sequence_no); 
//			statement.setString(position++, is_deployed);
//			statement.setString(position++, deployModel.getTestCase()); 
//			statement.setString(position++, deployModel.getTesttype());
//			statement.setString(position++, deployModel.getTestSubType());
//			statement.setString(position++, deployModel.getAliasid());
//			
//			statement.setString(position++, deployModel.getTargetFreq());
//			statement.setString(position++, deployModel.getTargetEnergy());			
//			statement.setString(position++, deployModel.getTarget_RYB_Voltage());
//			statement.setString(position++, deployModel.getTarget_RYB_Current());
//			statement.setString(position++, deployModel.getTarget_RYB_Pf());			
//			statement.setString(position++, deployModel.getTarget_R_Voltage());
//			statement.setString(position++, deployModel.getTarget_R_Current());
//			statement.setString(position++, deployModel.getTarget_R_Pf());			
//			statement.setString(position++, deployModel.getTarget_Y_Voltage());
//			statement.setString(position++, deployModel.getTarget_Y_Current());
//			statement.setString(position++, deployModel.getTarget_Y_Pf());			
//			statement.setString(position++, deployModel.getTarget_B_Voltage());
//			statement.setString(position++, deployModel.getTarget_B_Current());
//			statement.setString(position++, deployModel.getTarget_B_Pf());
//			
//		
//			
//			statement.setInt(position++, deployModel.getTestPeriodInSec());
//			statement.setInt(position++, deployModel.getWarmupPeriodInSec());
//			statement.setInt(position++, deployModel.getTargetNoOfPulses());
//			statement.setString(position++, deployModel.getRunType());
//			statement.setString(position++, deployModel.getMaxErrorAllowed());
//			statement.setString(position++, deployModel.getMinErrorAllowed());
//			statement.setInt(position++, deployModel.getReadingId());
//			statement.setInt(position++, deployModel.getTargetAverageCount());
//			
//			
//
//			boolean hadResults = statement.execute();
//
//			int count = statement.getUpdateCount();
//			statement.close();
//
//			try {
//				if (count==1){
//					//ApplicationLauncher.logger.info("sp_ltadd_deploy_test_cases: DB Success: ");
//					return true;
//				} else {
//					ApplicationLauncher.logger.info("sp_ltadd_deploy_test_cases_V2: DB failed: ");
//					return false;
//				}
//			} catch (Exception ex) {
//				ex.printStackTrace();	
//				ApplicationLauncher.logger.error("sp_ltadd_deploy_test_cases_V2 : Exception1 :"+ ex.getMessage());
//				return false;
//
//			}	
//		} catch (Exception ex) {
//			ex.printStackTrace();	
//			ApplicationLauncher.logger.error("sp_ltadd_deploy_test_cases_V2 : Exception2 :"+ ex.getMessage());
//			return false;
//		}
//	}
//	
	

	public JSONObject sp_ltgetdeploy_test_cases(String project_name,String deploymentID) {



		JSONObject testcases = new JSONObject();
		JSONArray testlist = new JSONArray();

		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_getdeploy_test_cases(?,?)}");
			statement.setString(1, deploymentID); 
			statement.setString(2, project_name); 
			
			
			boolean hadResults = statement.execute();


			int count = 0;
			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();

					// process result set
					while (resultSet.next()) {

						try{

							JSONObject jobj = new JSONObject();
							jobj.put("test_case", resultSet.getString("test_case_name"));
							jobj.put("test_type", resultSet.getString("test_type"));
							jobj.put("alias_id", resultSet.getString("alias_id"));
							jobj.put("sequence_no", resultSet.getString("sequence_no"));
							try{
								if(resultSet.getString("test_period_in_sec") == null){
									jobj.put("test_period_in_sec", "");
								}else{
									jobj.put("test_period_in_sec", resultSet.getString("test_period_in_sec"));
								}
								
								if(resultSet.getString("warmup_period_in_sec") == null){
									jobj.put("warmup_period_in_sec", "");
								}else{
									jobj.put("warmup_period_in_sec", resultSet.getString("warmup_period_in_sec"));
								}
								
								if(resultSet.getString("target_freq") == null){
									jobj.put("target_freq", "");
								}else{
									jobj.put("target_freq", resultSet.getString("target_freq"));
								}
								
								if(resultSet.getString("target_energy") == null){
									jobj.put("target_energy", "");
								}else{
									jobj.put("target_energy", resultSet.getString("target_energy"));
								}
								
								if(resultSet.getString("target_ryb_voltage") == null){
									jobj.put("target_ryb_voltage", "");
								}else{
									jobj.put("target_ryb_voltage", resultSet.getString("target_ryb_voltage"));
								}
								
								if(resultSet.getString("target_ryb_current") == null){
									jobj.put("target_ryb_current", "");
								}else{
									jobj.put("target_ryb_current", resultSet.getString("target_ryb_current"));
								}
								
								if(resultSet.getString("target_ryb_pf") == null){
									jobj.put("target_ryb_pf", "");
								}else{
									jobj.put("target_ryb_pf", resultSet.getString("target_ryb_pf"));
								}
								
								if(resultSet.getString("target_r_voltage") == null){
									jobj.put("target_r_voltage", "");
								}else{
									jobj.put("target_r_voltage", resultSet.getString("target_r_voltage"));
								}
								
								if(resultSet.getString("target_y_voltage") == null){
									jobj.put("target_y_voltage", "");
								}else{
									jobj.put("target_y_voltage", resultSet.getString("target_y_voltage"));
								}
								
								if(resultSet.getString("target_b_voltage") == null){
									jobj.put("target_b_voltage", "");
								}else{
									jobj.put("target_b_voltage", resultSet.getString("target_b_voltage"));
								}
								
								if(resultSet.getString("target_r_current") == null){
									jobj.put("target_r_current", "");
								}else{
									jobj.put("target_r_current", resultSet.getString("target_r_current"));
								}
								
								if(resultSet.getString("target_y_current") == null){
									jobj.put("target_y_current", "");
								}else{
									jobj.put("target_y_current", resultSet.getString("target_y_current"));
								}
								
								if(resultSet.getString("target_b_current") == null){
									jobj.put("target_b_current", "");
								}else{
									jobj.put("target_b_current", resultSet.getString("target_b_current"));
								}
								
								if(resultSet.getString("target_r_pf") == null){
									jobj.put("target_r_pf", "");
								}else{
									jobj.put("target_r_pf", resultSet.getString("target_r_pf"));
								}
								
								if(resultSet.getString("target_y_pf") == null){
									jobj.put("target_y_pf", "");
								}else{
									jobj.put("target_y_pf", resultSet.getString("target_y_pf"));
								}
								
								if(resultSet.getString("target_b_pf") == null){
									jobj.put("target_b_pf", "");
								}else{
									jobj.put("target_b_pf", resultSet.getString("target_b_pf"));
								}
								
								if(resultSet.getString("run_type") == null){
									jobj.put("run_type", "");
								}else{
									jobj.put("run_type", resultSet.getString("run_type"));
								}
								
								if(resultSet.getString("max_error_allowed") == null){
									jobj.put("max_error_allowed", "");
								}else{
									jobj.put("max_error_allowed", resultSet.getString("max_error_allowed"));
								}
								
								if(resultSet.getString("min_error_allowed") == null){
									jobj.put("min_error_allowed", "");
								}else{
									jobj.put("min_error_allowed", resultSet.getString("min_error_allowed"));
								}
							}catch(Exception e){
								e.printStackTrace();	
								ApplicationLauncher.logger.error("sp_ltgetdeploy_test_cases : Exception4 :"+ e.getMessage());
									
							}
							//jobj.put("inf_average", resultSet.getString("inf_average"));
							testlist.put(jobj);
							count++;
						}catch (Exception ex1) {
							ex1.printStackTrace();	
							ApplicationLauncher.logger.error("sp_ltgetdeploy_test_cases : Exception3 :"+ ex1.getMessage());
							//statement.close();
							//ApplicationLauncher.logger.error ("sp_ltgetdeploy_test_cases: Error 201: Source EM Model Reading: Failure");
							//return testcases;
						}
					}
					hadResults = statement.getMoreResults();

					testcases.put("No_of_testcases", count);
					testcases.put("Test_cases", testlist);
				}
				statement.close();





			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error("sp_ltgetdeploy_test_cases : Exception1 :"+ ex.getMessage());
				statement.close();
				//ApplicationLauncher.logger.error ("sp_ltgetdeploy_test_cases: Error 201: Source EM Model Reading: Failure");
				return testcases;
			}

		} catch (Exception ex) {

			ex.printStackTrace();	
			ApplicationLauncher.logger.error("sp_ltgetdeploy_test_cases : Exception2 :"+ ex.getMessage());
			//ApplicationLauncher.logger.error ("sp_ltgetdeploy_test_cases: Error 202: Source EM Model Reading: Failure");
			return testcases;
		}
		return testcases;
	}


	public boolean sp_ltadd_deploy_devices (String lastUpdatedDeploymentID,String project_name, 
			String device,  int rack_id, float ctr_ratio, float ptr_ratio,
			int meter_const, String is_deployed, String meterMake,String meterModelNo) {


		ApplicationLauncher.logger.debug ("sp_ltadd_deploy_devices : Entry :");
		ApplicationLauncher.logger.debug ("sp_ltadd_deploy_devices : ctr_ratio :" + ctr_ratio);
		ApplicationLauncher.logger.debug ("sp_ltadd_deploy_devices : ctr_ratio2 :" + String.valueOf(ctr_ratio));
		ApplicationLauncher.logger.debug ("sp_ltadd_deploy_devices : ptr_ratio :" + ptr_ratio);
		ApplicationLauncher.logger.debug ("sp_ltadd_deploy_devices : ptr_ratio2 :" + String.valueOf(ptr_ratio));
		//String ctRatioStr = String.valueOf(ctr_ratio);
		//String ptRatioStr = String.valueOf(ptr_ratio);
		//ApplicationLauncher.logger.debug ("sp_ltadd_deploy_devices : ctRatioStr :" + ctRatioStr);
		//ApplicationLauncher.logger.debug ("sp_ltadd_deploy_devices : ptRatioStr :" + ptRatioStr);
		try {

			//CallableStatement statement = ConnectManager.prepareCall("{call sp_add_deploy_devices(?,?,?,?,?,?,?,?,?,?)}");
			CallableStatement statement = ConnectManager.prepareCall("{call sp_add_deploy_devicesV1_1(?,?,?,?,?,?,?,?,?,?)}");
			statement.setString(1, lastUpdatedDeploymentID); 
			statement.setString(2, project_name); 
			statement.setString(3, device); 
			statement.setInt(4,rack_id); 
			//statement.setInt(5,ctr_ratio); 
			//statement.setInt(6,ptr_ratio); 
			statement.setString(5,String.valueOf(ctr_ratio)); 
			statement.setString(6,String.valueOf(ptr_ratio)); 
			//statement.setString(5,ctRatioStr); 
			//statement.setString(6,ptRatioStr); 
			statement.setInt(7,meter_const); 
			statement.setString(8, is_deployed);
			statement.setString(9, meterMake);
			statement.setString(10, meterModelNo);

			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {



				if (count==1){


					//ApplicationLauncher.logger.info("sp_ltadd_deploy_devices :DB Success: ");
					return true;
				} else {



					ApplicationLauncher.logger.info("sp_ltadd_deploy_devices: DB failed: ");
					return false;
				}




			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_ltadd_deploy_devices : Exception1 :"+ ex.getMessage());
				return false;

			}	



		} catch (Exception ex) {
			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_ltadd_deploy_devices : Exception2 :"+ ex.getMessage());
			return false;
		}
	}

	public JSONObject sp_ltgetdeploy_devices(String project_name,String selectedDeployment_ID) {

		JSONObject resultjson = new JSONObject();
		JSONArray JsonList = new JSONArray();


		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_getdeploy_devices(?,?)}");
			statement.setString(1, selectedDeployment_ID); 
			statement.setString(2, project_name); 
			
			boolean hadResults = statement.execute();


			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();

					// process result set
					int No_of_devices = 0;
					while (resultSet.next()) {



						JSONObject jobj = new JSONObject ();
						jobj.put("Device_name", resultSet.getString("device"));
						jobj.put("Rack_ID", resultSet.getInt("rack_id"));
						jobj.put("ctr_ratio", resultSet.getInt("ctr_ratio"));
						jobj.put("ptr_ratio", resultSet.getInt("ptr_ratio"));
						jobj.put("meter_const", resultSet.getInt("meter_const"));
												
						if( resultSet.getString("meter_make")!=null) {
							jobj.put("meter_make", resultSet.getString("meter_make"));
						}else{
							jobj.put("meter_make", "");
						}
						
						if( resultSet.getString("meter_model_no")!=null) {
							jobj.put("meter_model_no", resultSet.getString("meter_model_no"));
						}else{
							jobj.put("meter_model_no", "");
						}
						JsonList.put(jobj);
						No_of_devices++;
					}
					hadResults = statement.getMoreResults();

					resultjson.put("No_of_devices", No_of_devices);
					resultjson.put("Devices", JsonList);
					ApplicationLauncher.logger.debug ("sp_ltgetdeploy_devices : No_of_devices :"+ No_of_devices);

				}
				statement.close();





			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_ltgetdeploy_devices : Exception1 :"+ ex.getMessage());
				statement.close();
				//ApplicationLauncher.logger.error ("sp_ltgetdeploy_devices :Error 201: Source EM Model Reading: Failure");

				return resultjson;
			}

		} catch (Exception ex) {

			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_ltgetdeploy_devices : Exception2 :"+ ex.getMessage());
			//ApplicationLauncher.logger.error ("sp_ltgetdeploy_devices :Error 202: Source EM Model Reading: Failure");
			return resultjson;
		}
		return resultjson;
	}


	public JSONObject sp_ltgetrunning_status(String project_name) {





		JSONObject running_status_data = new JSONObject();
		JSONArray status_list = new JSONArray();

		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_getrunning_status(?)}");
			statement.setString(1, project_name); 

			boolean hadResults = statement.execute();






			int count = 0; 
			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();

					// process result set
					while (resultSet.next()) {



						JSONObject jobj = new JSONObject();
						jobj.put("test_case_name", resultSet.getString("test_case_name"));
						jobj.put("device_name", resultSet.getString("device_name"));
						jobj.put("test_status", resultSet.getString("test_result"));
						jobj.put("error_value", resultSet.getString("error_value"));
						status_list.put(jobj);
						count++;
					}
					hadResults = statement.getMoreResults();


					running_status_data.put("No_of_teststatus", count);
					running_status_data.put("Running_status", status_list);

				}
				statement.close();





			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_ltgetrunning_status : Exception1 :"+ ex.getMessage());
				statement.close();
				//ApplicationLauncher.logger.error ("sp_ltgetrunning_status: Error 201: Source EM Model Reading: Failure");
				return running_status_data;
			}

		} catch (Exception ex) {

			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_ltgetrunning_status : Exception2 :"+ ex.getMessage());
			//ApplicationLauncher.logger.error ("sp_ltgetrunning_status: Error 202: Source EM Model Reading: Failure");
			return running_status_data;
		}
		return running_status_data;
	}

	public boolean sp_ltadd_summary_data (String project_name, String testcasename,
			String testype,String aliasid,int sequenceno) {

		try {

			//ApplicationLauncher.logger.info("sp_ltadd_summary_data: sequenceno: "  + sequenceno);
			CallableStatement statement = ConnectManager.prepareCall("{call sp_add_summary_data(?,?,?,?,?)}");
			statement.setString(1, project_name); 
			statement.setString(2, testcasename); 
			statement.setString(3, testype); 
			statement.setString(4, aliasid); 
			statement.setInt(5, sequenceno); 

			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {



				if (count==1){


					//ApplicationLauncher.logger.info("sp_ltadd_summary_data: DB Success");
					return true;
				} else {



					ApplicationLauncher.logger.info("sp_ltadd_summary_data: DB failed");
					return false;
				}




			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_ltadd_summary_data : Exception1 :"+ ex.getMessage());
				return false;

			}	



		} catch (Exception ex) {
			ex.printStackTrace();
			ApplicationLauncher.logger.error("sp_ltadd_summary_data : Exception2 :"+ ex.getMessage());
			return false;
		}
	}

	public boolean sp_ltadd_harmonic_data (String project_name, String testcasename,
			String testype,String aliasid, int harmonicno, int harmonictimes, 
			String harmonicvolt,
			String harmoniccurrent, String harmonicphase) {

		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_add_harmonic_data(?,?,?,?,?,?,?,?,?)}");
			statement.setString(1, project_name); 
			statement.setString(2, testcasename); 
			statement.setString(3, testype); 
			statement.setString(4, aliasid); 
			statement.setInt(5, harmonicno); 
			statement.setInt(6, harmonictimes); 
			statement.setString(7, harmonicvolt); 
			statement.setString(8, harmoniccurrent); 
			statement.setString(9, harmonicphase); 

			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {



				if (count==1){


					ApplicationLauncher.logger.info("sp_ltadd_harmonic_data: DB Success: ");
					return true;
				} else {



					ApplicationLauncher.logger.info("sp_ltadd_harmonic_data: DB failed: ");
					return false;
				}




			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_ltadd_harmonic_data: Exception1 :"+ ex.getMessage());
				return false;

			}	



		} catch (Exception ex) {
			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_ltadd_harmonic_data : Exception2 :"+ ex.getMessage());
			return false;
		}
	}
	
	
	
	
//	public boolean sp_ltadd_harmonic_dataV2 (String project_name, String testcasename,
//			String testype,String aliasid,String harmonicsFrequency, HarmonicsDataModel harmonicsData) {
//
//		try {
//			ApplicationLauncher.logger.info("sp_ltadd_harmonic_dataV2:  Entry " );
//			
//			CallableStatement statement = ConnectManager.prepareCall("{call sp_add_harmonic_dataV2(?,?,?,?,?,?,?,?,?,?,?)}");
//			statement.setString(1, project_name); 
//			statement.setString(2, testcasename); 
//			statement.setString(3, testype); 
//			statement.setString(4, aliasid); 
//		/*	statement.setInt(5, harmonicno); 
//			statement.setInt(6, harmonictimes); 
//			statement.setString(7, harmonicvolt); 
//			statement.setString(8, harmoniccurrent); 
//			statement.setString(9, harmonicphase); */
//			
//			//statement.setString(5,null);	
//			//statement.setString(6,null);
//			ApplicationLauncher.logger.debug("sp_ltadd_harmonic_dataV2 : harmonicsData.getPhaseSelected() : "+ harmonicsData.getPhaseSelected());
//			ApplicationLauncher.logger.debug("sp_ltadd_harmonic_dataV2 : harmonicsData.getHarmonicsOrder(): " + harmonicsData.getHarmonicsOrder());
//			ApplicationLauncher.logger.debug("sp_ltadd_harmonic_dataV2 : harmonicsData.getAmplitude_V()   : " + harmonicsData.getAmplitude_V());
//			ApplicationLauncher.logger.debug("sp_ltadd_harmonic_dataV2 : harmonicsData.getAmplitude_I()   : " + harmonicsData.getAmplitude_I());
//			ApplicationLauncher.logger.debug("sp_ltadd_harmonic_dataV2 : harmonicsData.getPhaseShift_V()  : " + harmonicsData.getPhaseShift_V());
//			ApplicationLauncher.logger.debug("sp_ltadd_harmonic_dataV2 : harmonicsData.getPhaseShift_I()  : " + harmonicsData.getPhaseShift_I());
//			ApplicationLauncher.logger.debug("sp_ltadd_harmonic_dataV2 : harmonicsFrequency               : " + harmonicsFrequency);
//		
//			statement.setString(5,String.valueOf(harmonicsData.getHarmonicsOrder()));	
//			statement.setString(6,harmonicsData.getAmplitude_V());
//			statement.setString(7,harmonicsData.getAmplitude_I()); 
//			//statement.setString(7,null);
//			statement.setString(8,harmonicsData.getPhaseShift_V());
//			statement.setString(9,harmonicsData.getPhaseShift_I());
//			statement.setString(10,harmonicsData.getPhaseSelected());
//			statement.setString(11,harmonicsFrequency);
//
//			boolean hadResults = statement.execute();
//
//			int count = statement.getUpdateCount();
//			statement.close();
//
//			try {
//
//
//
//				if (count==1){
//
//
//					ApplicationLauncher.logger.info("sp_ltadd_harmonic_dataV2: DB Success: ");
//					return true;
//				} else {
//
//
//
//					ApplicationLauncher.logger.info("sp_ltadd_harmonic_dataV2: DB failed: ");
//					return false;
//				}
//
//
//
//
//			} catch (Exception ex) {
//				ex.printStackTrace();	
//				ApplicationLauncher.logger.error ("sp_ltadd_harmonic_dataV2: Exception1 :"+ ex.getMessage());
//				return false;
//
//			}	
//
//
//
//		} catch (Exception ex) {
//			ex.printStackTrace();	
//			ApplicationLauncher.logger.error ("sp_ltadd_harmonic_dataV2 : Exception2 :"+ ex.getMessage());
//			return false;
//		}
//	}

	public JSONObject sp_ltgetharmonic_data(String projectname, String testcase, String aliasid) {





		JSONObject harmonic_data = new JSONObject();
		JSONArray harmonics = new JSONArray();

		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_getharmonic_data(?,?,?)}");
			statement.setString(1, projectname); 
			statement.setString(2, testcase); 
			statement.setString(3, aliasid); 

			boolean hadResults = statement.execute();






			int count = 0; 
			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();

					// process result set
					while (resultSet.next()) {



						JSONObject jobj = new JSONObject();
						jobj.put("test_case_name", resultSet.getString("test_case_name"));
						jobj.put("test_alias_id", resultSet.getString("test_alias_id"));
						jobj.put("test_type", resultSet.getString("test_type"));
						jobj.put("harmonic_no", resultSet.getString("harmonic_no"));
						jobj.put("harmonic_times", resultSet.getString("harmonic_times"));
						jobj.put("harmonic_volt", resultSet.getString("harmonic_volt"));
						jobj.put("harmonic_current", resultSet.getString("harmonic_current"));
						jobj.put("harmonic_phase", resultSet.getString("harmonic_phase"));
						if(resultSet.getString("harmonic_volt_phase")==null){
							jobj.put("harmonic_volt_phase", "0");
						}else{
							jobj.put("harmonic_volt_phase", resultSet.getString("harmonic_volt_phase"));
						}
						if(resultSet.getString("harmonic_current_phase")==null){
							jobj.put("harmonic_current_phase", "0");
						}else{
							jobj.put("harmonic_current_phase", resultSet.getString("harmonic_current_phase"));
						}
						
						if(resultSet.getString("phase_selected")==null){
							jobj.put("phase_selected", "0");
						}else{
							jobj.put("phase_selected", resultSet.getString("phase_selected"));
						}
						
						if(resultSet.getString("harmonic_order")==null){
							jobj.put("harmonic_order", "0");
						}else{
							jobj.put("harmonic_order", resultSet.getString("harmonic_order"));
						}
						try {
							if(resultSet.getString("fund_freq")==null){
								jobj.put("fund_freq", "");
							}else{
								jobj.put("fund_freq", resultSet.getString("fund_freq"));
							}
						}catch(Exception e) {
							e.printStackTrace();
							ApplicationLauncher.logger.error("sp_ltgetharmonic_data : Exception3 :"+ e.getMessage());
							jobj.put("fund_freq", "");
						}
						
						
						harmonics.put(jobj);
						count++;
					}
					hadResults = statement.getMoreResults();


					harmonic_data.put("No_of_harmonics", count);
					harmonic_data.put("Harmonic_data", harmonics);

				}
				statement.close();





			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error("sp_ltgetharmonic_data : Exception1 :"+ ex.getMessage());
				statement.close();
				//ApplicationLauncher.logger.error ("sp_ltgetharmonic_data :Error 201: Source EM Model Reading: Failure");
				return harmonic_data;
			}

		} catch (Exception ex) {

			ex.printStackTrace();	
			ApplicationLauncher.logger.error("sp_ltgetharmonic_data : Exception2 :"+ ex.getMessage());
			//ApplicationLauncher.logger.error ("sp_ltgetharmonic_data :Error 202: Source EM Model Reading: Failure");
			return harmonic_data;
		}
		return harmonic_data;
	}

	public JSONObject sp_ltgettp_setup_i_user_data_mapping(String projectname) {




		JSONObject tp_i_mapping = new JSONObject();
		JSONArray tp_i_mapping_arr = new JSONArray();

		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_gettp_setup_i_user_data_mapping(?)}");
			statement.setString(1, projectname);

			boolean hadResults = statement.execute();






			int count = 0; 
			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();

					// process result set
					while (resultSet.next()) {



						JSONObject jobj = new JSONObject();
						jobj.put("i_serial_no", resultSet.getString("i_serial_no"));
						jobj.put("current_mapping_value", resultSet.getString("mapped_value"));
						tp_i_mapping_arr.put(jobj);
						count++;
					}
					hadResults = statement.getMoreResults();


					tp_i_mapping.put("No_of_I_mappings", count);
					tp_i_mapping.put("I_mapping_values", tp_i_mapping_arr);

				}
				statement.close();





			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_ltgettp_setup_i_user_data_mapping : Exception1 :"+ ex.getMessage());
				statement.close();
				//ApplicationLauncher.logger.error ("sp_ltgettp_setup_i_user_data_mapping :Error 201: Source EM Model Reading: Failure");
				return tp_i_mapping;
			}

		} catch (Exception ex) {

			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_ltgettp_setup_i_user_data_mapping : Exception2 :"+ ex.getMessage());
			//ApplicationLauncher.logger.error ("sp_ltgettp_setup_i_user_data_mapping :Error 202: Source EM Model Reading: Failure");
			return tp_i_mapping;
		}
		return tp_i_mapping;
	}


	public JSONObject sp_ltgettp_setup_pf_user_data_mapping(String projectname) {


		JSONObject tp_pf_mapping = new JSONObject();
		JSONArray tp_pf_mapping_arr = new JSONArray();

		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_gettp_setup_pf_user_data_mapping(?)}");
			statement.setString(1, projectname);

			boolean hadResults = statement.execute();

			int count = 0; 
			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();

					while (resultSet.next()) {


						JSONObject jobj = new JSONObject();
						jobj.put("pf_serial_no", resultSet.getString("pf_serial_no"));
						jobj.put("pf_mapping_value", resultSet.getString("mapped_value"));
						tp_pf_mapping_arr.put(jobj);
						count++;
					}
					hadResults = statement.getMoreResults();

					tp_pf_mapping.put("No_of_PF_mappings", count);
					tp_pf_mapping.put("PF_mapping_values", tp_pf_mapping_arr);

				}
				statement.close();





			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_ltgettp_setup_pf_user_data_mapping : Exception1 :"+ ex.getMessage());
				statement.close();
				//ApplicationLauncher.logger.error ("sp_ltgettp_setup_pf_user_data_mapping :Error 201: Source EM Model Reading: Failure");
				return tp_pf_mapping;
			}

		} catch (Exception ex) {

			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_ltgettp_setup_pf_user_data_mapping : Exception2 :"+ ex.getMessage());
			//ApplicationLauncher.logger.error ("sp_ltgettp_setup_pf_user_data_mapping :Error 202: Source EM Model Reading: Failure");
			return tp_pf_mapping;
		}
		return tp_pf_mapping;
	}

	public boolean sp_ltadd_tp_setup_i_user_data_mapping (String project_name, int i_serial_no,
			String mapping_value) {


		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_add_tp_setup_i_user_data_mapping(?,?,?)}");
			statement.setString(1, project_name); 
			statement.setInt(2, i_serial_no); 
			statement.setString(3, mapping_value);

			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {



				if (count==1){


					ApplicationLauncher.logger.info("sp_ltadd_tp_setup_i_user_data_mapping: DB Success: ");
					return true;
				} else {



					ApplicationLauncher.logger.info("sp_ltadd_tp_setup_i_user_data_mapping: DB failed: ");
					return false;
				}




			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error("sp_ltadd_tp_setup_i_user_data_mapping : Exception1 :"+ ex.getMessage());
				return false;

			}	



		} catch (Exception ex) {
			ex.printStackTrace();	
			ApplicationLauncher.logger.error("sp_ltadd_tp_setup_i_user_data_mapping : Exception2 :"+ ex.getMessage());
			return false;
		}
	}

	public boolean sp_ltadd_tp_setup_pf_user_data_mapping (String project_name, int pf_serial_no,
			String mapping_value) {




		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_add_tp_setup_pf_user_data_mapping(?,?,?)}");
			statement.setString(1, project_name); 
			statement.setInt(2, pf_serial_no); 
			statement.setString(3, mapping_value);

			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {


				if (count==1){

					ApplicationLauncher.logger.info("sp_ltadd_tp_setup_pf_user_data_mapping: DB Success: ");
					return true;
				} else {


					ApplicationLauncher.logger.info("sp_ltadd_tp_setup_pf_user_data_mapping: DB failed: ");
					return false;
				}



			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error("sp_ltadd_tp_setup_pf_user_data_mapping : Exception1 :"+ ex.getMessage());
				return false;

			}	



		} catch (Exception ex) {
			ex.printStackTrace();	
			ApplicationLauncher.logger.error("sp_ltadd_tp_setup_pf_user_data_mapping : Exception2 :"+ ex.getMessage());
			return false;
		}
	}



	public boolean sp_ltdelete_project_node ( String project_name, String test_type, String alais_id) {


		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_delete_project_node(?,?,?)}");
			statement.setString(1, project_name); 
			statement.setString(2, test_type); 
			statement.setString(3, alais_id); 


			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {



				if (count==1){


					ApplicationLauncher.logger.info("sp_ltdelete_project_node: DB Success: ");
					return true;
				} else {



					ApplicationLauncher.logger.info("sp_ltdelete_project_node: DB failed: ");
					return false;
				}




			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_ltdelete_project_node : Exception1 :"+ ex.getMessage());
				return false;

			}



		} catch (Exception ex) {
			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_ltdelete_project_node : Exception2 :"+ ex.getMessage());
			return false;
		}
	}

	public boolean sp_ltdelete_project( String project_name) {

		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_delete_project(?)}");
			statement.setString(1, project_name); 


			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {



				if (count>0){


					ApplicationLauncher.logger.info("sp_ltdelete_project: DB Success: ");
					return true;
				} else {



					ApplicationLauncher.logger.info("sp_ltdelete_project: DB failed: ");
					return false;
				}




			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_ltdelete_project : Exception1 :"+ ex.getMessage());
				return false;

			}



		} catch (Exception ex) {
			ex.printStackTrace();	
			ApplicationLauncher.logger.error("sp_ltdelete_project : Exception2 :"+ ex.getMessage());
			return false;
		}
	}



	public boolean sp_ltdelete_project_components ( String project_name, String test_type, String alais_id) {


		try {

			//ApplicationLauncher.logger.info("sp_ltdelete_project_components :  project_name :"+ project_name);
			//ApplicationLauncher.logger.info("sp_ltdelete_project_components :  test_type :"+ test_type);
			//ApplicationLauncher.logger.info("sp_ltdelete_project_components :  alais_id :"+ alais_id);
			CallableStatement statement = ConnectManager.prepareCall("{call sp_delete_project_components(?,?,?)}");
			statement.setString(1, project_name); 
			statement.setString(2, test_type); 
			statement.setString(3, alais_id); 


			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {



				if (count==1){


					ApplicationLauncher.logger.info("sp_ltdelete_project_components: DB Success: ");
					return true;
				} else {



					ApplicationLauncher.logger.info("sp_ltdelete_project_components: DB failed: ");
					return false;
				}




			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_ltdelete_project_components : Exception1 :"+ ex.getMessage());
				return false;

			}



		} catch (Exception ex) {
			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_ltdelete_project_components : Exception2 :"+ ex.getMessage());
			return false;
		}
	}
	
	public boolean sp_ltdelete_summary_data_project( String project_name) {



		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_delete_summary_data_project(?)}");
			statement.setString(1, project_name); 



			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {



				if (count==1){


					ApplicationLauncher.logger.debug("sp_ltdelete_summary_data_project: DB Success: ");
					return true;
				} else {



					ApplicationLauncher.logger.info("sp_ltdelete_summary_data_project: DB failed: ");
					return false;
				}




			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_ltdelete_summary_data_project : Exception1 :"+ ex.getMessage());
				return false;

			}



		} catch (Exception ex) {
			ex.printStackTrace();	
			ApplicationLauncher.logger.error("sp_ltdelete_summary_data_project : Exception2 :"+ ex.getMessage());
			return false;
		}
	}

	public boolean sp_ltdelete_summary_data( String project_name, String test_type, String alais_id) {



		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_delete_summary_data(?,?,?)}");
			statement.setString(1, project_name); 
			statement.setString(2, test_type); 
			statement.setString(3, alais_id); 


			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {



				if (count==1){


					ApplicationLauncher.logger.info("sp_ltdelete_summary_data: DB Success: ");
					return true;
				} else {



					ApplicationLauncher.logger.info("sp_ltdelete_summary_data: DB failed: ");
					return false;
				}




			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_ltdelete_summary_data : Exception1 :"+ ex.getMessage());
				return false;

			}



		} catch (Exception ex) {
			ex.printStackTrace();	
			ApplicationLauncher.logger.error("sp_ltdelete_summary_data : Exception2 :"+ ex.getMessage());
			return false;
		}
	}

	public boolean sp_ltdelete_harmonic_data( String project_name, String test_type, String alais_id) {


		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_delete_harmonic_data(?,?,?)}");
			statement.setString(1, project_name); 
			statement.setString(2, test_type); 
			statement.setString(3, alais_id); 


			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {



				if (count==1){


					ApplicationLauncher.logger.info("sp_ltdelete_harmonic_data: DB Success: ");
					return true;
				} else {



					ApplicationLauncher.logger.info("sp_ltdelete_harmonic_data: DB failed: ");
					return false;
				}




			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error("sp_ltdelete_harmonic_data : Exception1 :"+ ex.getMessage());
				return false;

			}



		} catch (Exception ex) {
			ex.printStackTrace();	
			ApplicationLauncher.logger.error("sp_ltdelete_harmonic_data : Exception2 :"+ ex.getMessage());
			return false;
		}
	}

	public boolean sp_lt_add_ref_std_const (String meter_type, String tap_name,
			String cosnt_value) {


		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_add_ref_std_const(?,?,?)}");
			statement.setString(1, meter_type); 
			statement.setString(2, tap_name); 
			statement.setString(3, cosnt_value);

			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {



				if (count==1){


					ApplicationLauncher.logger.info("sp_lt_add_ref_std_const: DB Success: ");
					return true;
				} else {



					ApplicationLauncher.logger.info("sp_lt_add_ref_std_const: DB failed: ");
					return false;
				}




			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error("sp_lt_add_ref_std_const : Exception1 :"+ ex.getMessage());
				return false;

			}	



		} catch (Exception ex) {
			ex.printStackTrace();	
			ApplicationLauncher.logger.error("sp_lt_add_ref_std_const : Exception2 :"+ ex.getMessage());
			return false;
		}
	}


	public JSONObject sp_ltgetref_std_const(String meter_type) {


		JSONObject ref_std_const = new JSONObject();
		JSONArray ref_std_const_arr = new JSONArray();

		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_getref_std_const(?)}");
			statement.setString(1, meter_type);

			boolean hadResults = statement.execute();






			int count = 0; 
			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();

					// process result set
					while (resultSet.next()) {

						JSONObject jobj = new JSONObject();
						jobj.put("tap_name", resultSet.getString("tap_name"));
						jobj.put("const_value", resultSet.getString("const_value"));
						ref_std_const_arr.put(jobj);
						count++;
					}
					hadResults = statement.getMoreResults();

					ref_std_const.put("No_of_records", count);
					ref_std_const.put("Const_Values", ref_std_const_arr);

				}
				statement.close();





			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_ltgetref_std_const : Exception1 :"+ ex.getMessage());
				statement.close();
				//ApplicationLauncher.logger.error ("sp_ltgettp_setup_i_user_data_mapping :Error 201: Source EM Model Reading: Failure");
				return ref_std_const;
			}

		} catch (Exception ex) {

			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_ltgetref_std_const : Exception2 :"+ ex.getMessage());
			//ApplicationLauncher.logger.error ("sp_ltgettp_setup_i_user_data_mapping :Error 202: Source EM Model Reading: Failure");
			return ref_std_const;
		}
		return ref_std_const;
	}


	public boolean sp_lt_add_system_config(String property_name, String value) {


		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_add_system_config(?,?)}");
			statement.setString(1, property_name); 
			statement.setString(2, value); 

			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {



				if (count==1){


					ApplicationLauncher.logger.info("sp_lt_add_system_config: DB Success: ");
					return true;
				} else {



					ApplicationLauncher.logger.info("sp_lt_add_system_config: DB failed: ");
					return false;
				}




			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_lt_add_system_config : Exception1 :"+ ex.getMessage());
				return false;

			}	



		} catch (Exception ex) {
			ex.printStackTrace();	
			ApplicationLauncher.logger.error("sp_lt_add_system_config : Exception2 :"+ ex.getMessage());
			return false;
		}
	}


	public JSONObject sp_ltgetsystem_config() {


		JSONObject properties = new JSONObject();
		JSONArray property_list = new JSONArray();

		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_getsystem_config()}");

			boolean hadResults = statement.execute();






			int count = 0; 
			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();

					// process result set
					while (resultSet.next()) {



						JSONObject jobj = new JSONObject();
						jobj.put("property", resultSet.getString("property_name"));
						jobj.put("value", resultSet.getString("property_value"));
						property_list.put(jobj);
						count++;
					}
					hadResults = statement.getMoreResults();


					properties.put("No_of_properties", count);
					properties.put("Properties", property_list);

				}
				statement.close();





			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_ltgetsystem_config : Exception1 :"+ ex.getMessage());
				statement.close();
				//ApplicationLauncher.logger.error ("sp_ltgetsystem_config :Error 201: Property Read: Failure");
				return properties;
			}

		} catch (Exception ex) {

			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_ltgetsystem_config : Exception2 :"+ ex.getMessage());
			//ApplicationLauncher.logger.error ("sp_ltgetsystem_config :Error 202: Property Read: Failure");
			return properties;
		}
		return properties;
	}

	public boolean sp_ltdelete_deploy_test_cases( String project_name) {



		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_delete_deploy_test_cases(?)}");
			statement.setString(1, project_name);


			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {



				if (count==1){


					ApplicationLauncher.logger.info("sp_ltdelete_deploy_test_cases: DB Success: ");
					return true;
				} else {



					ApplicationLauncher.logger.info("sp_ltdelete_deploy_test_cases: DB failed: ");
					return false;
				}




			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_ltdelete_deploy_test_cases : Exception1 :"+ ex.getMessage());
				return false;

			}



		} catch (Exception ex) {
			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_ltdelete_deploy_test_cases : Exception2 :"+ ex.getMessage());
			return false;
		}
	}

	public boolean sp_lt_add_procal_users(String username, String password, String access_level, String created_by, 
			String date_created) {

		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_add_procal_users(?,?,?,?,?)}");
			statement.setString(1, username); 
			statement.setString(2, password); 
			statement.setString(3, access_level); 
			statement.setString(4, created_by); 
			statement.setString(5, date_created); 

			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {



				if (count==1){


					ApplicationLauncher.logger.info("sp_lt_add_procal_users: DB Success: ");
					return true;
				} else {



					ApplicationLauncher.logger.info("sp_lt_add_procal_users: DB failed: ");
					return false;
				}




			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_lt_add_procal_users : Exception1 :"+ ex.getMessage());
				return false;

			}	



		} catch (Exception ex) {
			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_lt_add_procal_users : Exception2 :"+ ex.getMessage());
			return false;
		}
	}


	public JSONObject sp_ltgetprocal_users() {


		JSONObject properties = new JSONObject();
		JSONArray property_list = new JSONArray();

		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_getprocal_users()}");

			boolean hadResults = statement.execute();






			int count = 0; 
			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();

					// process result set
					while (resultSet.next()) {



						JSONObject jobj = new JSONObject();
						jobj.put("username", resultSet.getString("user_name"));
						jobj.put("password", resultSet.getString("login_password"));
						jobj.put("access_level", resultSet.getString("access_level"));
						property_list.put(jobj);
						count++;
					}
					hadResults = statement.getMoreResults();


					properties.put("No_of_users", count);
					properties.put("User_list", property_list);

				}
				statement.close();





			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_ltgetprocal_users : Exception1 :"+ ex.getMessage());
				statement.close();
				//ApplicationLauncher.logger.error ("sp_ltgetsystem_config :Error 201: Users Read: Failure");
				return properties;
			}

		} catch (Exception ex) {

			ex.printStackTrace();	
			ApplicationLauncher.logger.error("sp_ltgetprocal_users : Exception2 :"+ ex.getMessage());
			//ApplicationLauncher.logger.error ("sp_ltgetsystem_config :Error 202: Users Read: Failure");
			return properties;
		}
		return properties;
	}

	public JSONObject sp_ltgetprocal_user_access_level(String user_name, String password) {

		JSONObject properties = new JSONObject();
		JSONArray property_list = new JSONArray();

		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_getprocal_user_access_level(?,?)}");
			statement.setString(1, user_name);
			statement.setString(2, password);

			boolean hadResults = statement.execute();






			int count = 0; 
			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();

					// process result set
					while (resultSet.next()) {



						JSONObject jobj = new JSONObject();
						jobj.put("access_level", resultSet.getString("access_level"));
						property_list.put(jobj);
						count++;
					}
					hadResults = statement.getMoreResults();


					properties.put("No_of_users", count);
					properties.put("User_list", property_list);

				}
				statement.close();





			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error("sp_ltgetprocal_user_access_level : Exception1 :"+ ex.getMessage());
				statement.close();
				//ApplicationLauncher.logger.error ("sp_ltgetsystem_config :Error 201: Property Read: Failure");
				return properties;
			}

		} catch (Exception ex) {

			ex.printStackTrace();	
			ApplicationLauncher.logger.error("sp_ltgetprocal_user_access_level : Exception2 :"+ ex.getMessage());
			//ApplicationLauncher.logger.error ("sp_ltgetsystem_config :Error 202: Property Read: Failure");
			return properties;
		}
		return properties;
	}

	public boolean sp_ltdelete_procal_users( String user_name) {



		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_delete_procal_users(?)}");
			statement.setString(1, user_name);


			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {



				if (count==1){


					ApplicationLauncher.logger.info("sp_ltdelete_procal_users: DB Success: ");
					return true;
				} else {



					ApplicationLauncher.logger.info("sp_ltdelete_procal_users: DB failed: ");
					return false;
				}




			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_ltdelete_procal_users : Exception1 :"+ ex.getMessage());
				return false;

			}



		} catch (Exception ex) {
			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_ltdelete_procal_users : Exception2 :"+ ex.getMessage());
			return false;
		}
	}

	public boolean sp_ltdelete_tp_setup_i_user_data_mapping( String project_name) {



		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_delete_tp_setup_i_user_data_mapping(?)}");
			statement.setString(1, project_name);


			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {



				if (count==1){


					ApplicationLauncher.logger.info("sp_ltdelete_tp_setup_i_user_data_mapping: DB Success: ");
					return true;
				} else {



					ApplicationLauncher.logger.info("sp_ltdelete_tp_setup_i_user_data_mapping: DB failed: ");
					return false;
				}




			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_ltdelete_tp_setup_i_user_data_mapping : Exception1 :"+ ex.getMessage());
				return false;

			}



		} catch (Exception ex) {
			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_ltdelete_tp_setup_i_user_data_mapping : Exception2 :"+ ex.getMessage());
			return false;
		}
	}

	public boolean sp_ltdelete_tp_setup_pf_user_data_mapping( String project_name) {

		try {

			CallableStatement statement = ConnectManager.prepareCall("{call sp_delete_tp_setup_pf_user_data_mapping(?)}");
			statement.setString(1, project_name);


			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {


				if (count==1){

					ApplicationLauncher.logger.info("sp_ltdelete_tp_setup_pf_user_data_mapping: DB Success: ");
					return true;
				} else {


					ApplicationLauncher.logger.info("sp_ltdelete_tp_setup_pf_user_data_mapping: DB failed: ");
					return false;
				}




			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_ltdelete_tp_setup_pf_user_data_mapping : Exception1 :"+ ex.getMessage());
				return false;

			}



		} catch (Exception ex) {
			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_ltdelete_tp_setup_pf_user_data_mapping : Exception2 :"+ ex.getMessage());
			return false;
		}
	}

	public boolean sp_ltdelete_result_data ( long intital_time, long final_time) {





		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_delete_result_data(?,?)}");
			statement.setLong(1, intital_time); 
			statement.setLong(2, final_time); 

			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {



				if (count==1){


					ApplicationLauncher.logger.info("sp_ltdelete_result_data: DB Success: ");
					return true;
				} else {



					ApplicationLauncher.logger.info("sp_ltdelete_result_data: DB failed: ");
					return false;
				}




			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_ltdelete_result_data : Exception1 :"+ ex.getMessage());
				return false;

			}



		} catch (Exception ex) {
			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_ltdelete_result_data : Exception2 :"+ ex.getMessage());
			return false;
		}
	}

	public boolean sp_lt_add_report_header_config(String selectedReportProfile, String test_type,
			String header_type, String header_value) {


		try {

			CallableStatement statement = ConnectManager.prepareCall("{call sp_add_report_header_config(?,?,?,?)}");
			statement.setString(1, selectedReportProfile); 
			statement.setString(2, test_type); 
			statement.setString(3, header_type); 
			statement.setString(4, header_value); 

			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {



				if (count==1){


					ApplicationLauncher.logger.info("sp_lt_add_report_header_config: DB Success: ");
					return true;
				} else {



					ApplicationLauncher.logger.info("sp_lt_add_report_header_config: DB failed: ");
					return false;
				}




			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_lt_add_report_header_config : Exception1 :"+ ex.getMessage());
				return false;

			}	



		} catch (Exception ex) {
			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_lt_add_report_header_config : Exception2 :"+ ex.getMessage());
			return false;
		}
	}

	public JSONObject sp_ltgetreport_header_config(String selectedReportProfile,String test_type) {

		JSONObject report_header_config = new JSONObject();
		JSONArray report_header_config_arr = new JSONArray();

		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_getreport_header_config(?,?)}");
			statement.setString(1, selectedReportProfile);
			statement.setString(2, test_type);
			
			boolean hadResults = statement.execute();






			int count = 0; 
			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();

					// process result set
					while (resultSet.next()) {

						JSONObject jobj = new JSONObject();
						jobj.put("header_type", resultSet.getString("header_type"));
						jobj.put("header_value", resultSet.getString("header_value"));
						report_header_config_arr.put(jobj);
						count++;
					}
					hadResults = statement.getMoreResults();


					report_header_config.put("No_of_Headers", count);
					report_header_config.put("Report_Headers", report_header_config_arr);

				}
				statement.close();





			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_ltgetreport_header_config : Exception1 :"+ ex.getMessage());
				statement.close();
				//ApplicationLauncher.logger.error ("sp_ltgettp_setup_i_user_data_mapping :Error 201: Source EM Model Reading: Failure");
				return report_header_config;
			}

		} catch (Exception ex) {

			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_ltgetreport_header_config : Exception2 :"+ ex.getMessage());
			//ApplicationLauncher.logger.error ("sp_ltgettp_setup_i_user_data_mapping :Error 202: Source EM Model Reading: Failure");
			return report_header_config;
		}
		return report_header_config;
	}

	public boolean sp_ltdelete_report_header_config(String selectedReportProfile, String test_type) {


		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_delete_report_header_config(?,?)}");
			statement.setString(1, selectedReportProfile);
			statement.setString(2, test_type);

			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {



				if (count==1){


					ApplicationLauncher.logger.info("sp_ltdelete_report_header_config: DB Success: ");
					return true;
				} else {



					ApplicationLauncher.logger.info("sp_ltdelete_report_header_config: DB failed: ");
					return false;
				}




			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error("sp_ltdelete_report_header_config : Exception1 :"+ ex.getMessage());
				return false;

			}



		} catch (Exception ex) {
			ex.printStackTrace();	
			ApplicationLauncher.logger.error("sp_ltdelete_report_header_config : Exception2 :"+ ex.getMessage());
			return false;
		}
	}
	public boolean sp_lt_add_report_excel_config(String selectedReportProfile,String test_type,
			String cell_type, String cell_value) {


		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_add_report_excel_config(?,?,?,?)}");
			statement.setString(1, selectedReportProfile); 
			statement.setString(2, test_type); 
			statement.setString(3, cell_type); 
			statement.setString(4, cell_value); 

			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {



				if (count==1){


					ApplicationLauncher.logger.info("sp_lt_add_report_excel_config: DB Success: ");
					return true;
				} else {



					ApplicationLauncher.logger.info("sp_lt_add_report_excel_config: DB failed: ");
					return false;
				}




			} catch (Exception ex) {
				ex.printStackTrace();
				ApplicationLauncher.logger.error ("sp_lt_add_report_excel_config : Exception1 :"+ ex.getMessage());
				return false;

			}	



		} catch (Exception ex) {
			ex.printStackTrace();
			ApplicationLauncher.logger.error ("sp_lt_add_report_excel_config : Exception2 :"+ ex.getMessage());
			return false;
		}
	}
	public JSONObject sp_ltgetreport_excel_config(String selectedReportProfile,String test_type) {

		JSONObject report_excel_config = new JSONObject();
		JSONArray report_excel_config_arr = new JSONArray();

		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_getreport_excel_config(?,?)}");
			statement.setString(1, selectedReportProfile);
			statement.setString(2, test_type);
			boolean hadResults = statement.execute();






			int count = 0; 
			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();

					// process result set
					while (resultSet.next()) {



						JSONObject jobj = new JSONObject();
						jobj.put("cell_type", resultSet.getString("cell_type"));
						jobj.put("cell_value", resultSet.getString("cell_value"));
						report_excel_config_arr.put(jobj);
						count++;
					}
					hadResults = statement.getMoreResults();


					report_excel_config.put("No_of_Excel_Cells", count);
					report_excel_config.put("Report_Excel_Cells", report_excel_config_arr);

				}
				statement.close();





			} catch (Exception ex) {
				ex.printStackTrace();
				ApplicationLauncher.logger.error ("sp_ltgetreport_excel_config : Exception1 :"+ ex.getMessage());
				statement.close();
				//ApplicationLauncher.logger.error ("sp_ltgettp_excel_setup_i_user_data_mapping :Error 201: Source EM Model Reading: Failure");
				return report_excel_config;
			}

		} catch (Exception ex) {

			ex.printStackTrace();
			ApplicationLauncher.logger.error ("sp_ltgetreport_excel_config : Exception2 :"+ ex.getMessage());
			//ApplicationLauncher.logger.error ("sp_ltgettp_excel_setup_i_user_data_mapping :Error 202: Source EM Model Reading: Failure");
			return report_excel_config;
		}
		return report_excel_config;
	}

	public boolean sp_ltdelete_report_excel_config( String selectedReportProfile,String test_type) {



		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_delete_report_excel_config(?,?)}");
			statement.setString(1, selectedReportProfile);
			statement.setString(2, test_type);

			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {



				if (count==1){


					ApplicationLauncher.logger.info("sp_ltdelete_report_excel_config: DB Success: ");
					return true;
				} else {



					ApplicationLauncher.logger.info("sp_ltdelete_report_excel_config: DB failed: ");
					return false;
				}




			} catch (Exception ex) {
				ex.printStackTrace();
				ApplicationLauncher.logger.error ("sp_ltdelete_report_excel_config : Exception1 :"+ ex.getMessage());
				return false;

			}



		} catch (Exception ex) {
			ex.printStackTrace();
			ApplicationLauncher.logger.error ("sp_ltdelete_report_excel_config : Exception2 :"+ ex.getMessage());
			return false;
		}
	}


	public boolean sp_lt_add_project_run(String project_name,long start_epoch_time) {





		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_add_project_run(?,?)}");
			statement.setString(1, project_name); 
			statement.setLong(2, start_epoch_time); 

			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {



				if (count==1){


					//ApplicationLauncher.logger.info("sp_lt_add_project_run: DB Success: ");
					return true;
				} else {



					ApplicationLauncher.logger.info("sp_lt_add_project_run: DB failed: ");
					return false;
				}




			} catch (Exception ex) {
				ex.printStackTrace();
				ApplicationLauncher.logger.error ("sp_lt_add_project_run : Exception1 :"+ ex.getMessage());
				return false;

			}	



		} catch (Exception ex) {
			ex.printStackTrace();
			ApplicationLauncher.logger.error ("sp_lt_add_project_run : Exception2 :"+ ex.getMessage());
			return false;
		}
	}

	public boolean sp_lt_update_endtime_project_run(String project_name,long start_epoch_time, 
			long end_epoch_time) {





		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_update_endtime_project_run(?,?,?)}");
			statement.setString(1, project_name); 
			statement.setLong(2, start_epoch_time); 
			statement.setLong(3, end_epoch_time); 

			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {



				if (count==1){


					ApplicationLauncher.logger.info("sp_lt_update_endtime_project_run: DB Success: ");
					return true;
				} else {



					ApplicationLauncher.logger.info("sp_lt_update_endtime_project_run: DB failed: ");
					return false;
				}




			} catch (Exception ex) {
				ex.printStackTrace();
				ApplicationLauncher.logger.error ("sp_lt_update_endtime_project_run : Exception1 :"+ ex.getMessage());
				return false;

			}	



		} catch (Exception ex) {
			ex.printStackTrace();
			ApplicationLauncher.logger.error ("sp_lt_update_endtime_project_run : Exception2 :"+ ex.getMessage());
			return false;
		}
	}
	
	
	public boolean sp_lt_update_system_config(String systemConfigKey, String systemConfigValue ){




		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_update_system_config(?,?)}");
			statement.setString(1, systemConfigKey); 
			statement.setString(2, systemConfigValue); 
			
			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {



				if (count==1){


					ApplicationLauncher.logger.info("sp_lt_update_system_config: DB Success: ");
					return true;
				} else {



					ApplicationLauncher.logger.info("sp_lt_update_system_config: DB failed: ");
					return false;
				}




			} catch (Exception ex) {
				ex.printStackTrace();
				ApplicationLauncher.logger.error ("sp_lt_update_system_config : Exception1 :"+ ex.getMessage());
				return false;

			}	



		} catch (Exception ex) {
			ex.printStackTrace();
			ApplicationLauncher.logger.error ("sp_lt_update_system_config : Exception2 :"+ ex.getMessage());
			return false;
		}
	}


	public JSONObject sp_ltget_project_run(long start_epoch_time, long end_epoch_time) {

		JSONObject project_run = new JSONObject();
		JSONArray project_run_arr = new JSONArray();

		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_get_project_run(?,?)}");
			statement.setLong(1, start_epoch_time);
			statement.setLong(2, end_epoch_time);

			boolean hadResults = statement.execute();






			int count = 0; 
			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();

					// process result set
					while (resultSet.next()) {


						JSONObject jobj = new JSONObject();
						jobj.put("project_name", resultSet.getString("project_name"));
						jobj.put("start_time", resultSet.getString("start_time"));
						jobj.put("end_time", resultSet.getString("end_time"));
						jobj.put("epoch_start_time", resultSet.getString("epoch_start_time"));
						jobj.put("epoch_end_time", resultSet.getString("epoch_end_time"));
						project_run_arr.put(jobj);
						count++;
					}
					hadResults = statement.getMoreResults();

					project_run.put("No_of_Runs", count);
					project_run.put("Runs", project_run_arr);

				}
				statement.close();





			} catch (Exception ex) {
				ex.printStackTrace();
				ApplicationLauncher.logger.error ("sp_ltget_project_run : Exception1 :"+ ex.getMessage());
				statement.close();
				return project_run;
			}

		} catch (Exception ex) {

			ex.printStackTrace();
			ApplicationLauncher.logger.error ("sp_ltget_project_run : Exception2 :"+ ex.getMessage());
			return project_run;
		}
		return project_run;
	}

	public boolean sp_lt_add_report_file_location(String selectedReportProfile,String test_type,String templ_file_loc, String save_file_loc) {


		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_add_report_file_location(?,?,?,?)}");
			statement.setString(1, selectedReportProfile);
			statement.setString(2, test_type); 
			statement.setString(3, templ_file_loc); 
			statement.setString(4, save_file_loc); 

			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {


				if (count==1){


					ApplicationLauncher.logger.info("sp_lt_add_report_file_location: DB Success: ");
					return true;
				} else {



					ApplicationLauncher.logger.info("sp_lt_add_report_file_location: DB failed: ");
					return false;
				}




			} catch (Exception ex) {
				ex.printStackTrace();
				ApplicationLauncher.logger.error ("sp_lt_add_report_file_location : Exception1 :"+ ex.getMessage());
				return false;

			}	



		} catch (Exception ex) {
			ex.printStackTrace();
			ApplicationLauncher.logger.error ("sp_lt_add_report_file_location : Exception2 :"+ ex.getMessage());
			return false;
		}
	}

	public JSONObject sp_ltgetreport_file_location(String selectedReportProfile,String test_type) {

		JSONObject file_location = new JSONObject();

		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_getreport_file_location(?,?)}");
			statement.setString(1, selectedReportProfile);
			statement.setString(2, test_type);
			boolean hadResults = statement.execute();

			int count = 0; 
			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();

					// process result set
					while (resultSet.next()) {


						file_location.put("test_type", resultSet.getString("test_type"));
						file_location.put("template_file_location", resultSet.getString("template_file_location"));
						file_location.put("save_file_location", resultSet.getString("save_file_location"));
					}
					hadResults = statement.getMoreResults();

				}
				statement.close();





			} catch (Exception ex) {
				ex.printStackTrace();
				statement.close();
				ApplicationLauncher.logger.error ("sp_ltgetreport_file_location : Exception1 :"+ ex.getMessage());
				return file_location;
			}

		} catch (Exception ex) {

			ex.printStackTrace();
			ApplicationLauncher.logger.error ("sp_ltgetreport_file_location : Exception2 :"+ ex.getMessage());
			return file_location;
		}
		return file_location;
	}

	public JSONObject ProcessTPData(ArrayList<String> tp_setup_values){
		JSONObject test_setup_data = new JSONObject();
		JSONObject jobj1 = new JSONObject();
		JSONObject jobj2 = new JSONObject();
		JSONObject jobj3 = new JSONObject();
		JSONObject jobj4 = new JSONObject();
		String i_id = "";
		String pf_id = "";
		String first_two_chars ="";
		JSONObject imax= new JSONObject();
		JSONObject ib= new JSONObject();
		JSONObject abc= new JSONObject();
		JSONObject a_b_c= new JSONObject();
		
		try {
			for(int i=0; i < ConstantAppConfig.I_MAPPING_SIZE; i++){
				i_id = "imax_" + Integer.toString(i+1);

				jobj1.put(i_id, "F");
			}
			test_setup_data.put("imax", jobj1);

			for(int i=0; i < ConstantAppConfig.I_MAPPING_SIZE; i++){
				i_id = "ib_" + Integer.toString(i+1);
				jobj2.put(i_id, "F");
			}
			test_setup_data.put("ib", jobj2);

			for(int i=0; i < ConstantAppConfig.PF_MAPPING_SIZE; i++){
				pf_id = "abc_" + Integer.toString(i+1);
				jobj3.put(pf_id, "F");
			}


			test_setup_data.put("abc", jobj3);


			for(int i=0; i < ConstantAppConfig.PF_MAPPING_SIZE; i++){
				pf_id = "a_b_c_" + Integer.toString(i+1);
				jobj4.put(pf_id, "F");
			}

			test_setup_data.put("a_b_c", jobj4);

			for(int i=0; i<tp_setup_values.size(); i++){
				first_two_chars = tp_setup_values.get(i).substring(0, 2);
				if(first_two_chars.equals("im")){
					imax = test_setup_data.getJSONObject("imax");
					imax.put(tp_setup_values.get(i), "T");
				}
				else if(first_two_chars.equals("ib")){
					ib = test_setup_data.getJSONObject("ib");
					ib.put(tp_setup_values.get(i), "T");
				}
				else if(first_two_chars.equals("ab")){
					abc = test_setup_data.getJSONObject("abc");
					abc.put(tp_setup_values.get(i), "T");
				}
				else if(first_two_chars.equals("a_")){
					a_b_c = test_setup_data.getJSONObject("a_b_c");
					a_b_c.put(tp_setup_values.get(i), "T");
				}
				else{
					ApplicationLauncher.logger.info("ProcessTPData: Test Point data Mismatch case");

				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error ("sp_lt_add_backup_file_location : JSONException"+ e.getMessage());
		}
		return test_setup_data;
	}


	public boolean sp_lt_add_backup_file_location(String backup_file_loc, String sql_file_loc) {


		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_add_backup_file_location(?,?)}");
			statement.setString(1, backup_file_loc); 
			statement.setString(2, sql_file_loc); 


			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {


				if (count==1){


					ApplicationLauncher.logger.info("sp_lt_add_backup_file_location: DB Success: ");
					return true;
				} else {



					ApplicationLauncher.logger.info("sp_lt_add_backup_file_location: DB failed: ");
					return false;
				}




			} catch (Exception ex) {
				ex.printStackTrace();
				ApplicationLauncher.logger.error ("sp_lt_add_backup_file_location : Exception1 :"+ ex.getMessage());
				return false;

			}	



		} catch (Exception ex) {
			ex.printStackTrace();
			ApplicationLauncher.logger.error ("sp_lt_add_backup_file_location : Exception2 :"+ ex.getMessage());
			return false;
		}
	}

	public JSONObject sp_ltgetbackup_file_location() {

		JSONObject file_location = new JSONObject();

		try {


			CallableStatement statement = ConnectManager.prepareCall("{call sp_getbackup_file_location()}");


			boolean hadResults = statement.execute();






			int count = 0; 
			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();

					// process result set
					while (resultSet.next()) {



						file_location.put("backup_folder_location", resultSet.getString("backup_folder_location"));
						file_location.put("sql_server_location", resultSet.getString("sql_server_location"));
					}
					hadResults = statement.getMoreResults();

				}
				statement.close();





			} catch (Exception ex) {
				ex.printStackTrace();
				ApplicationLauncher.logger.error ("sp_ltgetbackup_file_location : Exception1 :"+ ex.getMessage());
				statement.close();

				return file_location;
			}

		} catch (Exception ex) {

			ex.printStackTrace();
			ApplicationLauncher.logger.error ("sp_ltgetbackup_file_location : Exception2 :"+ ex.getMessage());
			return file_location;
		}
		return file_location;
	}
	
	public JSONObject sp_get_uac_data_by_profile(String profileName) {

		JSONObject summary_data = new JSONObject();
		JSONArray testcases = new JSONArray();
		try {
			CallableStatement statement = ConnectManager.prepareCall("{call sp_get_uac_data_by_profile(?)}");
			statement.setString(1, profileName); 
			//statement.setString(2, subSection); 
			
			boolean hadResults = statement.execute();
			int count = 0; 
			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();
					// process result set
					while (resultSet.next()) {
						JSONObject jobj = new JSONObject();
						jobj.put("screen_name", resultSet.getString("screen_name"));
						jobj.put("screen_section", resultSet.getString("screen_section"));
						jobj.put("screen_sub_section", resultSet.getString("screen_sub_section"));
						jobj.put("profile_name", resultSet.getString("profile_name"));
						jobj.put("visible_enabled", resultSet.getString("visible_enabled"));
						jobj.put("execute_possible", resultSet.getString("execute_possible"));
						
						jobj.put("add_possible", resultSet.getString("add_possible"));
						jobj.put("update_possible", resultSet.getString("update_possible"));
						jobj.put("delete_possible", resultSet.getString("delete_possible"));
						testcases.put(jobj);
						count++;

					}

					hadResults = statement.getMoreResults();
					summary_data.put("No_of_uac", count);
					summary_data.put("Summary_data", testcases);

				}
				statement.close();
			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_get_uac_data_by_screen : Exception1 :"+ ex.getMessage());
				statement.close();
				//ApplicationLauncher.logger.error ("sp_ltgetsummary_data :Error 201: Source EM Model Reading: Failure");
				return summary_data;
			}

		} catch (Exception ex) {

			ex.printStackTrace();	
			ApplicationLauncher.logger.error("sp_ltgetsummary_data : Exception2 :"+ ex.getMessage());
			//ApplicationLauncher.logger.error ("sp_ltgetsummary_data :Error 202: Source EM Model Reading: Failure");
			return summary_data;
		}
		return summary_data;
	}
	
	public JSONObject sp_get_uac_data_by_screen(String screenName, String screenSection, String subSection) {

		JSONObject summary_data = new JSONObject();
		JSONArray testcases = new JSONArray();
		try {
			CallableStatement statement = ConnectManager.prepareCall("{call sp_get_uac_data_by_screen(?,?,?)}");
			statement.setString(1, screenName); 
			statement.setString(2, screenSection); 
			statement.setString(3, subSection); 
			
			boolean hadResults = statement.execute();
			int count = 0; 
			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();
					// process result set
					while (resultSet.next()) {
						JSONObject jobj = new JSONObject();
						jobj.put("screen_name", resultSet.getString("screen_name"));
						jobj.put("screen_section", resultSet.getString("screen_section"));
						jobj.put("screen_sub_section", resultSet.getString("screen_sub_section"));
						jobj.put("profile_name", resultSet.getString("profile_name"));
						jobj.put("visible_enabled", resultSet.getString("visible_enabled"));
						jobj.put("execute_possible", resultSet.getString("execute_possible"));
						
						jobj.put("add_possible", resultSet.getString("add_possible"));
						jobj.put("update_possible", resultSet.getString("update_possible"));
						jobj.put("delete_possible", resultSet.getString("delete_possible"));
						testcases.put(jobj);
						count++;

					}

					hadResults = statement.getMoreResults();
					summary_data.put("No_of_uac", count);
					summary_data.put("Summary_data", testcases);

				}
				statement.close();
			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_get_uac_data_by_screen : Exception1 :"+ ex.getMessage());
				statement.close();
				//ApplicationLauncher.logger.error ("sp_ltgetsummary_data :Error 201: Source EM Model Reading: Failure");
				return summary_data;
			}

		} catch (Exception ex) {

			ex.printStackTrace();	
			ApplicationLauncher.logger.error("sp_ltgetsummary_data : Exception2 :"+ ex.getMessage());
			//ApplicationLauncher.logger.error ("sp_ltgetsummary_data :Error 202: Source EM Model Reading: Failure");
			return summary_data;
		}
		return summary_data;
	}
	
	public boolean sp_add_uac_profile (UacDataModel dataElement) {


		ApplicationLauncher.logger.debug ("sp_add_uac_profile : Entry :");

		String visibleEnabled = "N";
		String executePossible = "N";
		String addPossible = "N";
		String updatePossible = "N";
		String deletePossible = "N";
		
		if(dataElement.getVisibleEnabled()){
			visibleEnabled = "Y";
		}
		
		if(dataElement.getExecutePossible()){
			executePossible = "Y";
		}
		
		if(dataElement.getAddPossible()){
			addPossible = "Y";
		}
		
		if(dataElement.getUpdatePossible()){
			updatePossible = "Y";
		}
		
		if(dataElement.getDeletePossible()){
			deletePossible = "Y";
		}
		
		String screenSection = dataElement.getSectionName();
		String screenSubSection = dataElement.getSubSectionName();
		
		try {

			CallableStatement statement = ConnectManager.prepareCall("{call sp_add_uac_profile(?,?,?,?,?,?,?,?,?,?)}");
			statement.setString(1, dataElement.getScreenName()); 
			statement.setString(2, screenSection); 
			statement.setString(3, screenSubSection); 
			statement.setString(4, dataElement.getRoleName()); 
			statement.setString(5, visibleEnabled ); 
			statement.setString(6,executePossible); 
			statement.setString(7,addPossible); 
			statement.setString(8,updatePossible); 
			statement.setString(9,deletePossible); 
			statement.setString(10, ConstantApp.USER_NAME);

			boolean hadResults = statement.execute();

			int count = statement.getUpdateCount();
			statement.close();

			try {
				if (count==1){
					//ApplicationLauncher.logger.info("sp_ltadd_deploy_devices :DB Success: ");
					return true;
				} else {
					ApplicationLauncher.logger.info("sp_add_uac_profile: DB failed: ");
					return false;
				}
			} catch (Exception ex) {
				ex.printStackTrace();	
				ApplicationLauncher.logger.error ("sp_add_uac_profile : Exception1 :"+ ex.getMessage());
				return false;

			}	

		} catch (Exception ex) {
			ex.printStackTrace();	
			ApplicationLauncher.logger.error ("sp_add_uac_profile : Exception2 :"+ ex.getMessage());
			return false;
		}
	}


	public boolean sp_validate_dut_already_tested( String dutMeterSerialNo, String dataType) {

		//ApplicationLauncher.logger.info ("sp_validate_dut_already_calibrated : deploymentId : " + deploymentId);
		ApplicationLauncher.logger.info ("sp_validate_dut_already_calibrated : dutMeterSerialNo : " + dutMeterSerialNo);
		ApplicationLauncher.logger.info ("sp_validate_dut_already_calibrated : dataType : " + dataType);
		JSONObject recordSummary = new JSONObject();
		JSONArray recordDetailsArray = new JSONArray();
		boolean foundRecord =  false;
		try {

			CallableStatement statement = ConnectManager.prepareCall("{call sp_validate_dut_already_tested(?,?)}");
			//statement.setString(1, deploymentId);
			statement.setString(1, dutMeterSerialNo);
			statement.setString(2, dataType);
			boolean hadResults = statement.execute();

			int count = 0; 
			try {

				while (hadResults) {
					ResultSet resultSet = statement.getResultSet();

					// process result set
					while (resultSet.next()) {


						JSONObject jobj = new JSONObject();
						//jobj.put("project_name", resultSet.getString("project_name"));
						//jobj.put("start_time", resultSet.getString("start_time"));
						//jobj.put("end_time", resultSet.getString("execution_completed_time_h"));
						//jobj.put("epoch_start_time", resultSet.getString("epoch_start_time"));
/*						jobj.put("epoch_end_time", resultSet.getString("execution_completed_time_epoch"));
						jobj.put("deployment_id", resultSet.getString("deployment_id"));
						jobj.put("project_name", resultSet.getString("project_name"));
						jobj.put("customer_name", resultSet.getString("customer_name"));
						jobj.put("equipment_serial_no", resultSet.getString("equipment_serial_no"));
						jobj.put("mct_mode_completed", resultSet.getString("mct_mode_completed"));
						jobj.put("nct_mode_completed", resultSet.getString("nct_mode_completed"));*/
						recordDetailsArray.put(jobj);
						count++;
					}
					hadResults = statement.getMoreResults();

					recordSummary.put("No_of_Records", count);
					recordSummary.put("RecordDetails", recordDetailsArray);

				}
				statement.close();
				
				if(count == 0){
					foundRecord = false;
					ApplicationLauncher.logger.info ("sp_validate_dut_already_calibrated : meter id record not found ");
				}else{
					foundRecord = true;
					ApplicationLauncher.logger.info ("sp_validate_dut_already_calibrated : meter id record found ");
				}




			} catch (Exception ex) {
				ex.printStackTrace();
				ApplicationLauncher.logger.error ("sp_validate_dut_already_calibrated : Exception1 :"+ ex.getMessage());
				statement.close();
				return foundRecord;
			}

		} catch (Exception ex) {

			ex.printStackTrace();
			ApplicationLauncher.logger.error ("sp_validate_dut_already_calibrated : Exception2 :"+ ex.getMessage());
			return foundRecord;
		}
		return foundRecord;
	}

}

