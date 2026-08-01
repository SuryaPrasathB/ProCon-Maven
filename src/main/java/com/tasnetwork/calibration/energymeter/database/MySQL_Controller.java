package com.tasnetwork.calibration.energymeter.database;

import java.util.ArrayList;

import org.json.JSONObject;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.uac.UacDataModel;

public class MySQL_Controller {

	public static boolean DB_ltcreateSrcDevice(String SrcType,String ModelName,String MeterType,String SerialNo,String classValue,String Asset_ID,String DeviceActive, String UpdatedBy ) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		try {


			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_ltcreateSrcDevice( SrcType, ModelName,MeterType, SerialNo, classValue, Asset_ID, DeviceActive,UpdatedBy);


				if (SP_Response){
					ApplicationLauncher.logger.info ("Source EM Model DB create: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("Source EM Model DB create: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;

		}
		return false;

	}
	
	public static JSONObject sp_get_executed_project_results(long start_epoch_time, long end_epoch_time) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		JSONObject resultjson = new JSONObject();

		try {

			if(SQLConnect.ConnectMySQL()){

				resultjson =  SQLConnect.sp_get_executed_project_results( start_epoch_time, end_epoch_time);			  		

			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_get_executed_project_results : Exception:"+ex.getMessage());
			return resultjson;

		}
		return resultjson;

	}
	
	public static JSONObject sp_get_completed_result_data(String mctNctMode,String filterDataType1,String deploymentID) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		JSONObject result_data = new JSONObject();

		try {



			if(SQLConnect.ConnectMySQL()){
				result_data = SQLConnect.sp_get_completed_result_data(mctNctMode,filterDataType1, deploymentID);

			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_get_completed_result_data : Exception:"+ex.getMessage());
			return result_data;

		}
		return result_data;

	}

	public static boolean sp_add_project_components (String project_name, 
			String test_case_name, String test_type, String test_alias_id, 
			String test_position_id, String time_duration, 
			String creep_un,String creep_pulses, String sta_ib, 
			String sta_test_pulse_no, String std_dev_input, String std_dev_load, 
			String inf_emin, String inf_emax, String inf_pulses,String skip_reading_count,
			String inf_deviation,String testruntype, String power, String frequency, 
			String inf_voltage, String inf_voltage_unbalance_u1,
			String inf_voltage_unbalance_u2, String inf_voltage_unbalance_u3,
			String cus_u1, String cus_u2, String cus_u3, String cus_i1, String cus_i2,
			String cus_i3, String cus_ph1, String cus_ph2, String cus_ph3,
			String cus_freq , String inf_average) { //      ) {//


		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		try {


			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_ltadd_project_components( project_name,
						test_case_name, test_type, test_alias_id, test_position_id, 
						time_duration, creep_un, creep_pulses, sta_ib, sta_test_pulse_no,
						std_dev_input, std_dev_load, inf_emin, inf_emax, inf_pulses,
						skip_reading_count,inf_deviation, testruntype,  power, frequency,
						inf_voltage, inf_voltage_unbalance_u1,  inf_voltage_unbalance_u2,
						inf_voltage_unbalance_u3,
						cus_u1,  cus_u2,  cus_u3,  cus_i1,  cus_i2,
						cus_i3,  cus_ph1,  cus_ph2,  cus_ph3,
						cus_freq ,  inf_average) ;


				if (SP_Response){
					//ApplicationLauncher.logger.info ("Project Components create: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("Project Components create: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			ApplicationLauncher.logger.error("sp_add_project_components: Exception: " + ex.getMessage());
			return false;

		}
		return false;

	}
	
	public static boolean sp_update_execution_status_deploy_manage (String project_name,String deployment_id, String executionStatus,String mctModeCompletedStatus,String nctModeCompletedStatus) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();
		try {
			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_procal_update_execution_status_deploy_manage( project_name, deployment_id,	executionStatus, mctModeCompletedStatus,nctModeCompletedStatus) ;

				if (SP_Response){
					ApplicationLauncher.logger.info ("sp_update_execution_status_deploy_manage : Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("sp_update_execution_status_deploy_manage : Failure");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_update_execution_status_deploy_manage  : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}
	
	public static JSONObject sp_getdeploy_manage_active(long deployedTimeMaxSearchLimit) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		JSONObject resultjson = new JSONObject();

		try {



			if(SQLConnect.ConnectMySQL()){

				resultjson =  SQLConnect.sp_procal_getdeploy_manage_active(deployedTimeMaxSearchLimit);			  		

			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_getdeploy_manage_active : Exception:"+ex.getMessage());
			return resultjson;

		}
		return resultjson;

	}


	public static boolean sp_add_project (String project_name, String test_type, String test_alias_id, String test_position_id) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		try {


			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_ltadd_project( project_name, test_type, test_alias_id, test_position_id) ;


				if (SP_Response){
					//ApplicationLauncher.logger.info ("Project create: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("Project: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			ApplicationLauncher.logger.error("sp_add_project: Exception: " + ex.getMessage());
			return false;

		}
		return false;

	}


	public static ArrayList<String> DB_lt_getmodel_list(String EM_Model ) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();
		ArrayList<String> ModelList = new ArrayList<String>();

		try {



			if(SQLConnect.ConnectMySQL()){
				ModelList = SQLConnect.sp_lt_getmodel_list(EM_Model);



			}
		} catch (Exception ex) {
			ex.printStackTrace();
			ApplicationLauncher.logger.error("DB_lt_getmodel_list: Exception: " + ex.getMessage());
			return ModelList;

		}
		return ModelList;

	}

	public static JSONObject sp_getproject(String projectname ) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		JSONObject ProjectData = new JSONObject();

		try {



			if(SQLConnect.ConnectMySQL()){
				ProjectData = SQLConnect.sp_ltgetproject(projectname);



			}
		} catch (Exception ex) {
			ex.printStackTrace();
			ApplicationLauncher.logger.error("sp_getproject: Exception: " + ex.getMessage());
			return ProjectData;

		}
		return ProjectData;

	}

	public static boolean ValidateDB_Schema_Exist() {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();


		try {



			if(SQLConnect.ConnectMySQL()){

				return (MySQL_Interface.bDB_SchemaExist && MySQL_Interface.bDB_Connected);


			}
		} catch (Exception ex) {
			ex.printStackTrace();
			ApplicationLauncher.logger.error ("ValidateDB_Schema_Exist: Exception:"+ex.getMessage());
			return (MySQL_Interface.bDB_SchemaExist && MySQL_Interface.bDB_Connected);

		}
		return (MySQL_Interface.bDB_SchemaExist && MySQL_Interface.bDB_Connected);

	}

	public static JSONObject sp_getproject_components(String projectname, String testcase, String aliasid) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		JSONObject test_details_json = new JSONObject();

		try {



			if(SQLConnect.ConnectMySQL()){

				test_details_json = SQLConnect.sp_ltgetproject_components(projectname, testcase, aliasid);


			}
		} catch (Exception ex) {
			ex.printStackTrace();
			ApplicationLauncher.logger.error ("sp_getproject_components: Exception:"+ex.getMessage());
			return test_details_json;

		}
		return test_details_json;

	}

	public static boolean sp_getprojectSaveAs(String CurrentProjectName,String ToBeSavedProjectName) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		boolean Status = false;

		try {



			if(SQLConnect.ConnectMySQL()){

				Status = SQLConnect.sp_ltgetprojectSaveAs( CurrentProjectName, ToBeSavedProjectName);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
			ApplicationLauncher.logger.error ("sp_getprojectSaveAs: Exception:"+ex.getMessage());
			return Status;

		}
		return Status;

	}
	public static boolean sp_getproject_modelmappingSaveAs(String CurrentProjectName,String ToBeSavedProjectName, int EM_Model_ID) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		boolean Status = false;

		try {



			if(SQLConnect.ConnectMySQL()){

				Status = SQLConnect.sp_ltgetproject_modelmappingSaveAs( CurrentProjectName, ToBeSavedProjectName,EM_Model_ID);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
			ApplicationLauncher.logger.error ("sp_getproject_modelmappingSaveAs: Exception:"+ex.getMessage());
			return Status;

		}
		return Status;

	}
	public static boolean sp_getproject_componentsSaveAs(String CurrentProjectName,String ToBeSavedProjectName) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		boolean Status = false;

		try {



			if(SQLConnect.ConnectMySQL()){

				Status = SQLConnect.sp_ltgetproject_componentsSaveAs( CurrentProjectName, ToBeSavedProjectName);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
			ApplicationLauncher.logger.error ("sp_getproject_componentsSaveAs: Exception:"+ex.getMessage());
			return Status;

		}
		return Status;

	}

	public static boolean sp_gettest_point_setupSaveAs(String CurrentProjectName,String ToBeSavedProjectName) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		boolean Status = false;

		try {



			if(SQLConnect.ConnectMySQL()){

				Status = SQLConnect.sp_ltgettest_point_setupSaveAs( CurrentProjectName, ToBeSavedProjectName);

			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_gettest_point_setupSaveAs : Exception:"+ex.getMessage());
			return Status;

		}
		return Status;

	}	

	public static boolean sp_getsummary_dataSaveAs(String CurrentProjectName,String ToBeSavedProjectName) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		boolean Status = false;

		try {



			if(SQLConnect.ConnectMySQL()){

				Status = SQLConnect.sp_ltgetsummary_dataSaveAs( CurrentProjectName, ToBeSavedProjectName);

			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_getsummary_dataSaveAs : Exception:"+ex.getMessage());
			return Status;

		}
		return Status;

	}

	public static JSONObject sp_getproject_list() {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		JSONObject project_list = new JSONObject();

		try {



			if(SQLConnect.ConnectMySQL()){
				project_list = SQLConnect.sp_ltgetproject_list();



			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_getproject_list : Exception:"+ex.getMessage());
			return project_list;

		}
		return project_list;

	}



	public static boolean sp_add_test_point_setup ( String project_name, String value) {

		ApplicationLauncher.logger.debug("sp_add_test_point_setup : project_name: " + project_name);
		ApplicationLauncher.logger.debug("sp_add_test_point_setup : value: " + value);    	
		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		JSONObject SP_Error1 = new JSONObject();
		JSONObject SP_Error2 = new JSONObject();
		try {

			SP_Error1.put("error", "Unable to create the product. Kindly try after some time");
			SP_Error1.put("StatusCode", "12003");
			SP_Error2.put("error", "Unable to create. Kindly try after some time");
			SP_Error2.put("StatusCode", "12005");
			ApplicationLauncher.logger.info("success2");
			if(SQLConnect.ConnectMySQL()){
				SQLConnect.sp_ltadd_test_point_setup(  project_name, value);


			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_add_test_point_setup : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}

	public static JSONObject sp_gettest_point_setup(String projectname) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		JSONObject test_setup_data = new JSONObject();

		try {



			if(SQLConnect.ConnectMySQL()){
				test_setup_data = SQLConnect.sp_ltgettest_point_setup(projectname);



			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_gettest_point_setup : Exception:"+ex.getMessage());
			return test_setup_data;

		}
		return test_setup_data;

	}

	public static boolean sp_add_em_model (String customer_name, String model_name, String model_type, String model_class,
			String current_ib, String current_imax, String voltage_vd, 
			String no_of_pulses, String frequency,String ct_type, String ctr_ratio,
			String ptr_ratio) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		try {


			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_ltadd_em_model( customer_name, model_name, model_type, model_class,
						current_ib, current_imax, voltage_vd, no_of_pulses, frequency, ct_type,
						ctr_ratio, ptr_ratio) ;


				if (SP_Response){
					ApplicationLauncher.logger.info ("Source EM Model DB create: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("Source EM Model DB create: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_add_em_model : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}

	public static JSONObject sp_getem_model_list() {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		JSONObject em_models = new JSONObject();

		try {



			if(SQLConnect.ConnectMySQL()){
				em_models = SQLConnect.sp_ltgetem_model_list();


			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_getem_model_list : Exception:"+ex.getMessage());
			return em_models;

		}
		return em_models;

	}

	public static boolean sp_delete_em_model (String customer_name, String model_name) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		try {


			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_ltdelete_em_model( customer_name, model_name) ;


				if (SP_Response){
					ApplicationLauncher.logger.info ("Source EM Model DB Delete: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("Source EM Model DB delete: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_delete_em_model : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}

	public static boolean sp_add_device_settings (int id, String device_type, String model_name, String port_name, String baud_rate) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		try {


			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_ltadd_device_settings( id,  device_type,  model_name,  port_name,  baud_rate) ;


				if (SP_Response){
					ApplicationLauncher.logger.info ("Device Settings create: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("Device Settings create: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_add_device_settings : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}

	public static JSONObject sp_getdevice_setting(String device_type) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		JSONObject device_setting = new JSONObject();

		try {



			if(SQLConnect.ConnectMySQL()){

				device_setting = SQLConnect.sp_ltgetdevice_setting(device_type);



			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_getdevice_setting : Exception:"+ex.getMessage());
			return device_setting;

		}
		return device_setting;

	}


	public static boolean sp_add_result (String project_name, String test_case_name,
			String alias_id, String rack_id,
			String test_result, int error_id, String error_value,
			String FailureReason,String data_type,String executionMctNctMode,String energyFlowMode,
			String deploymentId,int seqNumber) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		try {


			boolean SP_Response = false;
			SP_Response = SQLConnect.sp_ltadd_result( project_name,  test_case_name, alias_id,rack_id,  
					test_result, error_id, error_value,FailureReason,data_type,executionMctNctMode,energyFlowMode, deploymentId, seqNumber) ;


			if (SP_Response){
				//ApplicationLauncher.logger.info ("Result create: Success");
				return true;
			} else {
				ApplicationLauncher.logger.info ("sp_add_result: Failure");
				return false;
			} 

		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_add_result : Exception:"+ex.getMessage());
			return false;

		}

	}



	public static JSONObject sp_getresult_testpoint_data(long fromtime, long totime, String project_name, String test_point,String data_type) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		JSONObject result_data = new JSONObject();

		try {



			if(SQLConnect.ConnectMySQL()){
				result_data = SQLConnect.sp_ltgetresult_testpoint_data(fromtime,totime, project_name,test_point,data_type);


			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_getresult_testpoint_data : Exception:"+ex.getMessage());
			return result_data;

		}
		return result_data;

	}

	public static JSONObject sp_getresult_data(long fromtime, long totime, String project_name, String data_type) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		JSONObject result_data = new JSONObject();

		try {



			if(SQLConnect.ConnectMySQL()){
				result_data = SQLConnect.sp_ltgetresult_data(fromtime,totime, project_name, data_type);

			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_getresult_data : Exception:"+ex.getMessage());
			return result_data;

		}
		return result_data;

	}

	public static JSONObject sp_getresult_projectdata(long fromtime, long totime) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		JSONObject project_list = new JSONObject();

		try {



			if(SQLConnect.ConnectMySQL()){
				project_list = SQLConnect.sp_ltgetresult_project_data(fromtime, totime);



			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_getresult_projectdata : Exception:"+ex.getMessage());
			return project_list;

		}
		return project_list;

	}

	public static boolean sp_add_project_model_mapping (String project_name, int model_id) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		try {


			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_ltadd_project_model_mapping( project_name,  model_id) ;


				if (SP_Response){
					//ApplicationLauncher.logger.info ("Project Model Mapping: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("Project Model Mapping: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_add_project_model_mapping : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}

	public static JSONObject sp_getem_model_data(int model_id) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		JSONObject model_data = new JSONObject();

		try {



			if(SQLConnect.ConnectMySQL()){
				model_data =  SQLConnect.sp_ltgetem_model_data(model_id);


			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_getem_model_data : Exception:"+ex.getMessage());
			return model_data;

		}
		return model_data;

	}

	public static int sp_getProjectModel_ID(String project_name) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();
		int modelID = 0;

		try {



			if(SQLConnect.ConnectMySQL()){
				modelID = SQLConnect.sp_ltgetProjectModel_ID(project_name);


			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_getProjectModel_ID : Exception:"+ex.getMessage());
			return modelID;

		}
		return modelID;

	}


	public static int sp_getModel_ID(String EM_ModelName) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();
		int modelID = 0;

		try {



			if(SQLConnect.ConnectMySQL()){
				modelID = SQLConnect.sp_ltgetmodel_id(EM_ModelName);


			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_getModel_ID : Exception:"+ex.getMessage());
			return modelID;

		}
		return modelID;

	}


	public static boolean sp_add_project_scheduled_time (String project_name, String sche_date, String sche_time, long time_stamp) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		try {


			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_ltadd_project_scheduled_time( project_name, sche_date, sche_time, time_stamp) ;


				if (SP_Response){
					ApplicationLauncher.logger.info ("Project Scheduled Time create: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("Project Scheduled Time create: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_add_project_scheduled_time : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}

	public static JSONObject sp_getproject_scheduled_time(long from_timestamp, long to_timestamp) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		JSONObject result = new JSONObject();

		try {



			if(SQLConnect.ConnectMySQL()){
				result =  SQLConnect.sp_ltgetproject_scheduled_time(from_timestamp, to_timestamp);

			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_getproject_scheduled_time : Exception:"+ex.getMessage());
			return result;

		}
		return result;

	}

	public static JSONObject sp_getsummary_data(String project_name) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();
		JSONObject summary_data = new JSONObject();

		try {



			if(SQLConnect.ConnectMySQL()){
				summary_data =  SQLConnect.sp_ltgetsummary_data(project_name);

			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_getsummary_data : Exception:"+ex.getMessage());
			return summary_data;

		}
		return summary_data;

	}

	public static boolean sp_add_deploy_test_cases (String lastUpdatedDeploymentID, String project_name, String test_case, String test_type, String alias_id, String sequence_no, String is_deployed) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		try {

			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_ltadd_deploy_test_cases( lastUpdatedDeploymentID, project_name, test_case, test_type, alias_id, sequence_no, is_deployed) ;


				if (SP_Response){
					//ApplicationLauncher.logger.info ("Deploy Test Cases: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("Deploy Test Cases create: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_add_deploy_test_cases : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}
	
	
//	public static boolean sp_add_deploy_test_cases_v2 (String lastUpdatedDeploymentID, String project_name, 
//			String sequence_no, String is_deployed,DeploymentTestCaseDataModel deployModel) {
//
//
//		MySQL_Interface SQLConnect  = new MySQL_Interface ();
//
//		try {
//
//			boolean SP_Response = false;
//			if(SQLConnect.ConnectMySQL()){
//				SP_Response = SQLConnect.sp_ltadd_deploy_test_cases_V2( lastUpdatedDeploymentID, project_name, sequence_no, is_deployed,
//						deployModel) ;
//
//
//				if (SP_Response){
//					//ApplicationLauncher.logger.info ("Deploy Test Cases: Success");
//					return true;
//				} else {
//					ApplicationLauncher.logger.info ("Deploy Test Cases V2 create: Failure");
//					return false;
//				} 
//			}
//		} catch (Exception ex) {	  
//			ex.printStackTrace();	  
//			ApplicationLauncher.logger.error ("sp_add_deploy_test_cases_v2 : Exception:"+ex.getMessage());
//			return false;
//
//		}
//		return false;
//
//	}

	public static JSONObject sp_getdeploy_test_cases(String project_name,String deploymentID) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();
		JSONObject test_case_list = new JSONObject();

		try {



			if(SQLConnect.ConnectMySQL()){
				test_case_list =  SQLConnect.sp_ltgetdeploy_test_cases(project_name, deploymentID);

			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_getdeploy_test_cases : Exception:"+ex.getMessage());
			return test_case_list;

		}
		return test_case_list;

	}

	public static boolean sp_add_deploy_devices (String lastUpdatedDeploymentID,String project_name,
			String device, int rack_id, 
			float ctr_ratio, float ptr_ratio,
			int meter_const,
			String is_deployed, String meterMake,String meterModelNo) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		try {

			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_ltadd_deploy_devices( lastUpdatedDeploymentID,project_name,
						device, rack_id, ctr_ratio,
						ptr_ratio, meter_const, is_deployed,meterMake,meterModelNo) ;


				if (SP_Response){
					//ApplicationLauncher.logger.info ("Deploy Devices create: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("Deploy Devices create: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_add_deploy_devices : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}

	public static JSONObject sp_getdeploy_devices(String project_name,String selectedDeployment_ID) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		JSONObject resultjson = new JSONObject();

		try {



			if(SQLConnect.ConnectMySQL()){

				resultjson =  SQLConnect.sp_ltgetdeploy_devices(project_name,selectedDeployment_ID);			  		

			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_getdeploy_devices : Exception:"+ex.getMessage());
			return resultjson;

		}
		return resultjson;

	}
	
	public static boolean sp_delete_deploy_test_cases (String project_name,String deploymentID) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		try {


			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_ltdelete_deploy_test_cases( project_name, deploymentID) ;


				if (SP_Response){
					//ApplicationLauncher.logger.info ("Deployed Test Case Delete: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("Deployed Test Case Delete: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_delete_deploy_test_cases : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}
	
	public static JSONObject sp_add_deploy_manage_v1_1 (String project_name, 
			String customer_name, String equipment_serial_no,String customer_reference_no,
			String ulr_no, String isMCT_Type, String isNCT_Type,  String isMCT_TestingCompleted,
			String isNCT_TestingCompleted,String execution_status,long deployedTime,
			long deployedTimeMaxSearchLimit,long executionCompletedTime, String testerName,String energyFlowModeSelected,String autoDeployEnabled) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();
		JSONObject resultjson = new JSONObject();
		try {

			resultjson.put("status", false);
			resultjson.put("deployment_id", "");
			resultjson.put("comments", "");
			if(SQLConnect.ConnectMySQL()){
				resultjson = SQLConnect.sp_procal_add_deploy_manage_v1_1(  project_name, 
						 customer_name,  equipment_serial_no,customer_reference_no,
						 ulr_no,  isMCT_Type,  isNCT_Type,    isMCT_TestingCompleted,
						 isNCT_TestingCompleted,execution_status, deployedTime, 
						 deployedTimeMaxSearchLimit, executionCompletedTime,  testerName ,energyFlowModeSelected, autoDeployEnabled) ;


/*				if (SP_Response){
					ApplicationLauncher.logger.info ("sp_add_deploy_manage create: Success");
					//return true;
				} else {
					ApplicationLauncher.logger.info ("sp_add_deploy_manage: create: Failure");
					//return false;
				} */
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_add_deploy_manage_v1_1 : Exception:"+ex.getMessage());
			//return false;

		}
		//return false;
		return resultjson;

	}
	
	public static JSONObject sp_add_deploy_manage(String project_name, 
			String customer_name, String equipment_serial_no,String customer_reference_no,
			String ulr_no, String isMCT_Type, String isNCT_Type,  String isMCT_TestingCompleted,
			String isNCT_TestingCompleted,String execution_status,long deployedTime,
			long deployedTimeMaxSearchLimit,long executionCompletedTime, String testerName,String energyFlowModeSelected) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();
		JSONObject resultjson = new JSONObject();
		try {

			resultjson.put("status", false);
			resultjson.put("deployment_id", "");
			resultjson.put("comments", "");
			if(SQLConnect.ConnectMySQL()){
				resultjson = SQLConnect.sp_procal_add_deploy_manage(  project_name, 
						 customer_name,  equipment_serial_no,customer_reference_no,
						 ulr_no,  isMCT_Type,  isNCT_Type,    isMCT_TestingCompleted,
						 isNCT_TestingCompleted,execution_status, deployedTime, 
						 deployedTimeMaxSearchLimit, executionCompletedTime,  testerName ,energyFlowModeSelected) ;


/*				if (SP_Response){
					ApplicationLauncher.logger.info ("sp_add_deploy_manage create: Success");
					//return true;
				} else {
					ApplicationLauncher.logger.info ("sp_add_deploy_manage: create: Failure");
					//return false;
				} */
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_add_deploy_manage : Exception:"+ex.getMessage());
			//return false;

		}
		//return false;
		return resultjson;

	}



	public static JSONObject sp_getresult_data(long fromtime, long totime, String project_name,String data_type,String deploymentID, String mctNctMode,String energyMode) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		JSONObject result_data = new JSONObject();

		try {



			if(SQLConnect.ConnectMySQL()){
				result_data = SQLConnect.sp_ltgetresult_dataV2(fromtime,totime, project_name, data_type, deploymentID,mctNctMode,energyMode);

			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_getresult_data : Exception:"+ex.getMessage());
			return result_data;

		}
		return result_data;

	}

	public static JSONObject sp_getrunning_status(String project_name) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();
		JSONObject running_status_data = new JSONObject();

		try {



			if(SQLConnect.ConnectMySQL()){
				running_status_data =  SQLConnect.sp_ltgetrunning_status(project_name);

			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_getrunning_status : Exception:"+ex.getMessage());
			return running_status_data;

		}
		return running_status_data;

	}

	public static boolean sp_add_summary_data (String project_name, String testcasename, 
			String testtype, String aliasid, int sequenceno) {

		//ApplicationLauncher.logger.info("sp_add_summary_data: sequenceno: "  + sequenceno);
		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		try {

			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_ltadd_summary_data( project_name, testcasename,
						testtype, aliasid, sequenceno) ;


				if (SP_Response){
					//ApplicationLauncher.logger.info ("Summary Data create: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("Summary Data create: Failure1");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_add_summary_data : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}


	public static boolean sp_add_harmonic_data (String project_name, String testcasename, 
			String testtype, String aliasid, int harmonicno, int harmonictimes,
			String harmonicvolt, 
			String harmoniccurrent, String harmonicphase) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();
		try {


			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_ltadd_harmonic_data( project_name, testcasename,
						testtype, aliasid, harmonicno, harmonictimes, 
						harmonicvolt, 
						harmoniccurrent, harmonicphase) ;


				if (SP_Response){
					ApplicationLauncher.logger.info ("Harmonic Data create: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("Harmonic Data create: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_add_harmonic_data : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}
	
//	public static boolean sp_add_harmonic_dataV2 (String project_name, String testcasename, 
//			String testtype, String aliasid, String harmonicsFrequency, HarmonicsDataModel harmonicsData) {
//
//
//		MySQL_Interface SQLConnect  = new MySQL_Interface ();
//		try {
//
//
//			boolean SP_Response = false;
//			if(SQLConnect.ConnectMySQL()){
//				SP_Response = SQLConnect.sp_ltadd_harmonic_dataV2( project_name, testcasename, testtype, aliasid, harmonicsFrequency, harmonicsData) ;
//
//
//				if (SP_Response){
//					ApplicationLauncher.logger.info ("HarmonicV2 Data create: Success");
//					return true;
//				} else {
//					ApplicationLauncher.logger.info ("HarmonicV2 Data create: Failure");
//					return false;
//				} 
//			}
//		} catch (Exception ex) {	  
//			ex.printStackTrace();	  
//			ApplicationLauncher.logger.error ("sp_add_harmonic_dataV2 : Exception:"+ex.getMessage());
//			return false;
//
//		}
//		return false;
//
//	}

	public static JSONObject sp_getharmonic_data(String project_name, String test_type, String alias_id) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		JSONObject resultjson = new JSONObject();

		try {



			if(SQLConnect.ConnectMySQL()){

				resultjson =  SQLConnect.sp_ltgetharmonic_data(project_name, test_type, alias_id);			  		


			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_getharmonic_data : Exception:"+ex.getMessage());
			return resultjson;

		}
		return resultjson;

	}


	public static JSONObject sp_gettp_setup_i_user_data_mapping(String project_name) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		JSONObject resultjson = new JSONObject();

		try {



			if(SQLConnect.ConnectMySQL()){

				resultjson =  SQLConnect.sp_ltgettp_setup_i_user_data_mapping(project_name);			  		

			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_gettp_setup_i_user_data_mapping : Exception:"+ex.getMessage());
			return resultjson;

		}
		return resultjson;

	}

	public static JSONObject sp_gettp_setup_pf_user_data_mapping(String project_name) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		JSONObject resultjson = new JSONObject();

		try {



			if(SQLConnect.ConnectMySQL()){

				resultjson =  SQLConnect.sp_ltgettp_setup_pf_user_data_mapping(project_name);			  		
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_gettp_setup_pf_user_data_mapping : Exception:"+ex.getMessage());
			return resultjson;

		}
		return resultjson;

	}

	public static boolean sp_add_tp_setup_i_user_data_mapping (String project_name, int i_serial_no, 
			String i_value) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		try {


			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_ltadd_tp_setup_i_user_data_mapping( project_name, i_serial_no, i_value) ;


				if (SP_Response){
					ApplicationLauncher.logger.info ("I User Data Mapping create: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("I User Data Mapping create: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_add_tp_setup_i_user_data_mapping : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}

	public static boolean sp_add_tp_setup_pf_user_data_mapping (String project_name, int pf_serial_no, 
			String pf_value) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		try {


			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_ltadd_tp_setup_pf_user_data_mapping( project_name, pf_serial_no, pf_value) ;


				if (SP_Response){
					ApplicationLauncher.logger.info ("PF User Data Mapping create: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("PF User Data Mapping create: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_add_tp_setup_pf_user_data_mapping : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}



	public static boolean sp_delete_project_node (String project_name, String test_type, String alias_id) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		try {


			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_ltdelete_project_node( project_name, test_type, alias_id ) ;


				if (SP_Response){
					ApplicationLauncher.logger.info ("Project delete node: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("Project delete node: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_delete_project_node : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}

	public static boolean sp_delete_project (String project_name) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		try {


			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_ltdelete_project( project_name ) ;


				if (SP_Response){
					ApplicationLauncher.logger.info ("Project delete : Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("Project delete : Failure");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_delete_project : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}

	public static boolean sp_delete_project_components (String project_name, String test_type, String alias_id) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		try {


			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_ltdelete_project_components( project_name, test_type, alias_id ) ;


				if (SP_Response){
					ApplicationLauncher.logger.info ("Project Components delete: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("Project Components delete: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_delete_project_components : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}

	
	public static boolean sp_delete_summary_data_project (String project_name) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		try {


			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_ltdelete_summary_data_project( project_name ) ;


				if (SP_Response){
					ApplicationLauncher.logger.info ("Summary Data Project delete: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("Summary Data Project create: Failure2");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_delete_summary_data_project : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}
	
	public static boolean sp_delete_summary_data (String project_name, String test_type, String alias_id) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		try {


			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_ltdelete_summary_data( project_name, test_type, alias_id ) ;


				if (SP_Response){
					ApplicationLauncher.logger.info ("Summary Data delete: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("Summary Data create: Failure2");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_delete_summary_data : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}

	public static boolean sp_delete_harmonic_data (String project_name, String test_type, String alias_id) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		try {


			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_ltdelete_harmonic_data( project_name, test_type, alias_id ) ;


				if (SP_Response){
					ApplicationLauncher.logger.info ("Harmonic Data Delete: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("Harmonic Data Delete: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_delete_harmonic_data : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}



	public static boolean sp_add_ref_std_const (String meter_type, String tap_name, 
			String const_value) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		try {


			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_lt_add_ref_std_const( meter_type, tap_name, const_value) ;


				if (SP_Response){
					ApplicationLauncher.logger.info ("Ref Std Const create: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("Ref Std Const create: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_add_ref_std_const : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}


	public static JSONObject sp_getref_std_const(String meter_type) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		JSONObject resultjson = new JSONObject();

		try {



			if(SQLConnect.ConnectMySQL()){

				resultjson =  SQLConnect.sp_ltgetref_std_const(meter_type);			  		

			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_getref_std_const : Exception:"+ex.getMessage());
			return resultjson;

		}
		return resultjson;

	}
	public static boolean sp_add_system_config (String property_name, String value) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();
		try {
			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_lt_add_system_config( property_name, value) ;


				if (SP_Response){
					ApplicationLauncher.logger.info ("Property create: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("Property create: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_add_system_config : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}


	public static JSONObject sp_getsystem_config() {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		JSONObject resultjson = new JSONObject();

		try {



			if(SQLConnect.ConnectMySQL()){

				resultjson =  SQLConnect.sp_ltgetsystem_config();			  		

			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_getsystem_config : Exception:"+ex.getMessage());
			return resultjson;

		}
		return resultjson;

	}

	public static boolean sp_delete_deploy_test_cases (String project_name) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		try {


			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_ltdelete_deploy_test_cases( project_name) ;


				if (SP_Response){
					//ApplicationLauncher.logger.info ("Deployed Test Case Delete: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("Deployed Test Case Delete: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_delete_deploy_test_cases : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}

	public static boolean sp_add_procal_users(String username, String password, String access_level, String created_by, 
			String date_created) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();
		try {
			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_lt_add_procal_users(username, 
						password, access_level, created_by, 
						date_created) ;


				if (SP_Response){
					ApplicationLauncher.logger.info ("User create: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("User create: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_add_procal_users : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}


	public static JSONObject sp_getprocal_users() {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		JSONObject resultjson = new JSONObject();

		try {



			if(SQLConnect.ConnectMySQL()){

				resultjson =  SQLConnect.sp_ltgetprocal_users();			  		



			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_getprocal_users : Exception:"+ex.getMessage());
			return resultjson;

		}
		return resultjson;

	}

	public static JSONObject sp_getprocal_user_access_level(String user_name, String password) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		JSONObject resultjson = new JSONObject();

		try {



			if(SQLConnect.ConnectMySQL()){

				resultjson =  SQLConnect.sp_ltgetprocal_user_access_level(user_name, password);			  		



			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_getprocal_user_access_level : Exception:"+ex.getMessage());
			return resultjson;

		}
		return resultjson;

	}

	public static boolean sp_delete_procal_users (String user_name) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		try {


			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_ltdelete_procal_users( user_name) ;


				if (SP_Response){
					ApplicationLauncher.logger.info ("User Delete: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("User Delete: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_delete_procal_users : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}

	public static boolean sp_delete_tp_setup_i_user_data_mapping (String project_name) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();
		try {


			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_ltdelete_tp_setup_i_user_data_mapping(project_name) ;


				if (SP_Response){
					ApplicationLauncher.logger.info ("I_Mapping Values Delete: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("I_Mapping Values Delete: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_delete_tp_setup_i_user_data_mapping : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}

	public static boolean sp_delete_tp_setup_pf_user_data_mapping (String project_name) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();
		try {


			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_ltdelete_tp_setup_pf_user_data_mapping(project_name) ;


				if (SP_Response){
					ApplicationLauncher.logger.info ("PF_Mapping Values Delete: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("PF_Mapping Values Delete: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_delete_tp_setup_pf_user_data_mapping : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}

	public static boolean sp_delete_result_data (long intital_time, long final_time) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		try {


			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_ltdelete_result_data( intital_time, final_time) ;


				if (SP_Response){
					ApplicationLauncher.logger.info ("Results DB Delete: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("Results DB Delete: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_delete_result_data : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}

	public static boolean sp_add_report_header_config (String selectedReportProfile,String testtype, String headertype,
			String headervalue) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();
		try {
			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_lt_add_report_header_config( selectedReportProfile,testtype, 
						headertype, headervalue) ;


				if (SP_Response){
					ApplicationLauncher.logger.info ("Report Header create: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("Report Header create: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_add_report_header_config : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}


	public static JSONObject sp_getreport_header_config(String selectedReportProfile, String test_type) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		JSONObject resultjson = new JSONObject();

		try {



			if(SQLConnect.ConnectMySQL()){
				resultjson =  SQLConnect.sp_ltgetreport_header_config(selectedReportProfile, test_type);			  		

			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_getreport_header_config : Exception:"+ex.getMessage());
			return resultjson;

		}
		return resultjson;

	}


	public static boolean sp_delete_report_header_config(String selectedReportProfile,String test_type) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();
		try {


			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_ltdelete_report_header_config(selectedReportProfile,test_type) ;


				if (SP_Response){
					ApplicationLauncher.logger.info ("Report Header Delete: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("Report Header Delete: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error("sp_delete_report_header_config : Exception:"+ex.getMessage());
			return false;

		}
		return false;
	}

	public static boolean sp_add_report_excel_config (String selectedReportProfile,String testtype, String celltype,
			String cellvalue) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();
		try {
			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_lt_add_report_excel_config( selectedReportProfile,testtype, 
						celltype, cellvalue) ;


				if (SP_Response){
					ApplicationLauncher.logger.info ("Report excel create: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("Report excel create: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_add_report_excel_config : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}

	public static JSONObject sp_getreport_excel_config(String selectedReportProfile ,String test_type) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		JSONObject resultjson = new JSONObject();

		try {



			if(SQLConnect.ConnectMySQL()){

				resultjson =  SQLConnect.sp_ltgetreport_excel_config(selectedReportProfile,test_type);			  		


			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_getreport_excel_config : Exception:"+ex.getMessage());
			return resultjson;

		}
		return resultjson;

	}

	public static boolean sp_delete_report_excel_config(String selectedReportProfile,String test_type) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();
		try {

			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_ltdelete_report_excel_config( selectedReportProfile,test_type) ;


				if (SP_Response){
					ApplicationLauncher.logger.info ("Report excel Delete: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("Report excel Delete: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_delete_report_excel_config : Exception:"+ex.getMessage());
			return false;

		}
		return false;
	}

	public static boolean sp_add_project_run (String project_name, long start_epoch_time) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();
		try {
			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_lt_add_project_run( project_name, start_epoch_time) ;


				if (SP_Response){
					//ApplicationLauncher.logger.info ("Project Run create: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("Project Run create: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_add_project_run : Exception:"+ex.getMessage());
			return false;

		}
		return false;
	}

	public static boolean sp_update_endtime_project_run (String project_name,long start_epoch_time,
			long end_epoch_time) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();
		try {
			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_lt_update_endtime_project_run( project_name, start_epoch_time,
						end_epoch_time) ;


				if (SP_Response){
					ApplicationLauncher.logger.info ("Project Run Update: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("Project Run Update: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_update_endtime_project_run : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}
	
	public static boolean sp_ltUpdateSystemConfig (String systemConfigKey, String systemConfigValue ){


		MySQL_Interface SQLConnect  = new MySQL_Interface ();
		try {
			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_lt_update_system_config( systemConfigKey, systemConfigValue) ;


				if (SP_Response){
					ApplicationLauncher.logger.info ("sp_ltUpdateSystemConfig Update: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("sp_ltUpdateSystemConfig Update: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_ltUpdateSystemConfig : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}


	public static JSONObject sp_get_project_run(long start_epoch_time, long end_epoch_time) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		JSONObject resultjson = new JSONObject();

		try {



			if(SQLConnect.ConnectMySQL()){

				resultjson =  SQLConnect.sp_ltget_project_run( start_epoch_time, end_epoch_time);			  		

			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_get_project_run : Exception:"+ex.getMessage());
			return resultjson;

		}
		return resultjson;

	}


	public static boolean sp_add_report_file_location (String selectedReportProfile,String test_type,String templ_file_loc, String save_file_loc) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();
		try {
			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_lt_add_report_file_location(selectedReportProfile, test_type,templ_file_loc, save_file_loc) ;


				if (SP_Response){
					ApplicationLauncher.logger.info ("Report File Location create: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("Report File Location  create: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_add_report_file_location : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}

	public static JSONObject sp_getreport_file_location(String selectedReportProfile,String test_type) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		JSONObject resultjson = new JSONObject();

		try {



			if(SQLConnect.ConnectMySQL()){

				resultjson =  SQLConnect.sp_ltgetreport_file_location(selectedReportProfile, test_type);			  		

			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_getreport_file_location : Exception:"+ex.getMessage());
			return resultjson;

		}
		return resultjson;

	}

	public static boolean sp_add_backup_file_location (String backup_file_loc, String sql_file_loc) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();
		try {
			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_lt_add_backup_file_location(backup_file_loc, sql_file_loc) ;


				if (SP_Response){
					ApplicationLauncher.logger.info ("Backup File Location create: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("Backup File Location  create: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_add_backup_file_location : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}

	public static JSONObject sp_getbackup_file_location() {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();
		JSONObject resultjson = new JSONObject();

		try {



			if(SQLConnect.ConnectMySQL()){

				resultjson =  SQLConnect.sp_ltgetbackup_file_location();			  		

			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_getbackup_file_location : Exception:"+ex.getMessage());
			return resultjson;

		}
		return resultjson;

	}
	
	public static JSONObject sp_get_Uac_data_by_profile(String profileName) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();
		JSONObject data = new JSONObject();

		try {



			if(SQLConnect.ConnectMySQL()){
				data =  SQLConnect.sp_get_uac_data_by_profile( profileName);

			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_get_Uac_data_by_profile : Exception:"+ex.getMessage());
			return data;

		}
		return data;

	}
	
	public static JSONObject sp_get_Uac_data_by_screen(String screenName,String screenSection, String subSection) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();
		JSONObject data = new JSONObject();

		try {



			if(SQLConnect.ConnectMySQL()){
				data =  SQLConnect.sp_get_uac_data_by_screen( screenName,screenSection, subSection);

			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_get_Uac_data_by_screen : Exception:"+ex.getMessage());
			return data;

		}
		return data;

	}
	
	public static boolean sp_add_uac_profile (UacDataModel dataElement) {


		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		try {

			boolean SP_Response = false;
			if(SQLConnect.ConnectMySQL()){
				SP_Response = SQLConnect.sp_add_uac_profile( dataElement) ;


				if (SP_Response){
					//ApplicationLauncher.logger.info ("Deploy Devices create: Success");
					return true;
				} else {
					ApplicationLauncher.logger.info ("Deploy Devices create: Failure");
					return false;
				} 
			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_add_uac_profile : Exception:"+ex.getMessage());
			return false;

		}
		return false;

	}
	
	public static boolean sp_ValidateDutAlreadyTested( String dutMeterSerialNo, String dataType) {

		MySQL_Interface SQLConnect  = new MySQL_Interface ();

		//JSONObject resultjson = new JSONObject();
		boolean status = false;
		try {

			if(SQLConnect.ConnectMySQL()){

				status =  SQLConnect.sp_validate_dut_already_tested(   dutMeterSerialNo,  dataType) ;			  		

			}
		} catch (Exception ex) {	  
			ex.printStackTrace();	  
			ApplicationLauncher.logger.error ("sp_ValidateDutAlreadyCalibrated : Exception:"+ex.getMessage());
			return status;

		}
		return status;

	}

}


