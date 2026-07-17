package com.tasnetwork.calibration.conveyor.AsyncHttpClient;


import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.AsyncHttpClientConfig.Builder;
import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import com.tasnetwork.calibration.conveyor.constant.ConstantProTamp;
import com.tasnetwork.calibration.conveyor.database.MySQL_Controller;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.setting.FirmwareUpgradeController;
import com.tasnetwork.calibration.energymeter.setting.LogViewController;
import com.tasnetwork.calibration.energymeter.setting.ScanDeviceController;
import com.tasnetwork.calibration.energymeter.setting.ServerSettingController;

import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;

import com.ning.http.client.Response;



public class AsyncClient  {

	volatile static boolean preRequisiteResponseStatus = false ;
/*    public static void load_saved_server_settings(){

    	JSONObject servetSetting = MySQL_Controller.sp_get_server_setting();
    	try {
			if(servetSetting.has("http_protocol")){
				ServerProperties.HTTP_Protocol=servetSetting.getString("http_protocol");
			} else{
				
				ServerProperties.HTTP_Protocol="";
				ApplicationLauncher.logger.info("load_saved_server_settings: http_protocol: Data not retrieved from DB");
			
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("load_saved_server_settings: JSONException1:"+e.getMessage());
			ServerProperties.HTTP_Protocol="";
			ApplicationLauncher.logger.info("load_saved_server_settings: http_protocol: Data not retrieved from database");
		} 	
    	
    	try {
			if(servetSetting.has("server_ip")){
				ServerProperties.PublicURL_Id = servetSetting.getString("server_ip");
			} else{
				
				ServerProperties.PublicURL_Id="";
				ApplicationLauncher.logger.info("load_saved_server_settings: server_ip: Data not retrieved from DB");
			
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("load_saved_server_settings: JSONException1:"+e.getMessage());
			ServerProperties.PublicURL_Id="";
			ApplicationLauncher.logger.info("load_saved_server_settings: server_ip: Data not retrieved from database");
		} 
    	
    	try {
			if(servetSetting.has("server_port")){
				ServerProperties.URLPort=servetSetting.getString("server_port");
			} else{
				
				ServerProperties.URLPort="";
				ApplicationLauncher.logger.info("load_saved_server_settings: server_port: Data not retrieved from DB");
			
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("load_saved_server_settings: JSONException1:"+e.getMessage());
			ServerProperties.URLPort="";
			ApplicationLauncher.logger.info("load_saved_server_settings: server_port: Data not retrieved from database");
		} 
    	
    	try {
			if(servetSetting.has("refresh_gui_freq")){
				ServerProperties.RefreshGUI_Freq = servetSetting.getInt("refresh_gui_freq");
			} else{
				
				ServerProperties.RefreshGUI_Freq =3;
				ApplicationLauncher.logger.info("load_saved_server_settings: refresh_gui_freq: Data not retrieved from DB");
			
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("load_saved_server_settings: JSONException1:"+e.getMessage());
			ServerProperties.RefreshGUI_Freq =3;
			ApplicationLauncher.logger.info("load_saved_server_settings: refresh_gui_freq: Data not retrieved from database");
		} 
    	
    }*/
	
	static ConveyorDataManager DisplayDataObj =  new ConveyorDataManager();
    
    
    public void lstamper_Scan_Available_timezones(String getMethodURL){
    	ApplicationLauncher.logger.info("lstamper_Scan_Available_timezones: Entry");
    	String TARGET_URL = ServerSettingController.getRootUrl()
    			+"/"+getMethodURL;

    	AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(5000).build());
    	Future f = null;
    	try {
    		//f = c.prepareGet("http://www.ning.com/").execute(new AsyncCompletionHandler() {
    		f = c.prepareGet(TARGET_URL).execute(new AsyncCompletionHandler<Response>() {

    			@Override
    			public Response onCompleted(Response response) throws IOException {
    				ApplicationLauncher.logger.info("lstamper_Scan_Available_timezones: onCompleted");

    				if(response.getStatusCode() == 200){
    					Gson gson = new Gson();
    					String myResp = new String();
    					myResp=response.getResponseBody().toString();
    					RestAPIResponse myCurrentAPIResponse = new RestAPIResponse();

    					myCurrentAPIResponse = gson.fromJson(myResp,RestAPIResponse.class);
    					String msgSuccess = new String();
    					msgSuccess = "200";



    					if (myCurrentAPIResponse.getStatusCode().equals(msgSuccess) ) {
    						String[] timezones;
                            timezones=myCurrentAPIResponse.getAvailableTimeZones();

                            for(int i=0;i<timezones.length;i++) {
                            	ApplicationLauncher.logger.info("lstamper_Scan_Available_timezones: timezones:" +i+":"+ timezones[i]);
                            }
                            ServerSettingController.UpdateDisplayServerTimeZoneList(timezones);

    					}

    				}
    				c.close();
    				return response;
    			}



    			@Override
    			public void onThrowable(Throwable t) {
    				ApplicationLauncher.logger.info("lstamper_Scan_Available_timezones: onThrowable:"+t.getMessage());
    				c.close();
    			}
    		});
    	} catch (Exception e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    		ApplicationLauncher.logger.error("lstamper_Scan_Available_timezones: IOException:" + e1.getMessage());
    	}
    }
    
    public void lstamper_serialStatus(String getMethodURL){
    	ApplicationLauncher.logger.info("lstamper_serialStatus: Entry");
    	String TARGET_URL = ServerSettingController.getRootUrl()
    			+"/"+getMethodURL;

    	AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(5000).build());
    	Future f = null;
    	try {
    		//f = c.prepareGet("http://www.ning.com/").execute(new AsyncCompletionHandler() {
    		f = c.prepareGet(TARGET_URL).execute(new AsyncCompletionHandler<Response>() {

    			@Override
    			public Response onCompleted(Response response) throws IOException {
    				ApplicationLauncher.logger.info("lstamper_serialStatus: onCompleted");

    				if(response.getStatusCode() == 200){
    					Gson gson = new Gson();
    					String myResp = new String();
    					myResp=response.getResponseBody().toString();
    					RestAPIResponse myCurrentAPIResponse = new RestAPIResponse();

    					myCurrentAPIResponse = gson.fromJson(myResp,RestAPIResponse.class);
    					String msgSuccess = new String();
    					msgSuccess = "200";



    					if (myCurrentAPIResponse.getStatusCode().equals(msgSuccess) ) {
    						String port_1;
                            String port_2;
                            port_1=myCurrentAPIResponse.getport1().toString();
                            port_2=myCurrentAPIResponse.getport2().toString();
                            ServerSettingController.UpdateDisplaySerialPortPort1(port_1);
                            ServerSettingController.UpdateDisplaySerialPortPort2(port_2);

    					}

    				}
    				c.close();
    				return response;
    			}



    			@Override
    			public void onThrowable(Throwable t) {
    				ApplicationLauncher.logger.info("lstamper_serialStatus: onThrowable:"+t.getMessage());
    				c.close();
    			}
    		});
    	} catch (Exception e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    		ApplicationLauncher.logger.error("lstamper_serialStatus: Exception:" + e1.getMessage());
    	}
    }
    
    public  void lstamper_getFirmwareVersion(String getMethodURL){

    	ApplicationLauncher.logger.info("lstamper_getFirmwareVersion: Entry");
    	String TARGET_URL = ServerSettingController.getRootUrl()
    			+"/"+getMethodURL;
    	ApplicationLauncher.logger.info("lstamper_getFirmwareVersion: TARGET_URL:"+TARGET_URL);

    	AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(5000).build());
    	Future f = null;
    	try {

    		f = c.prepareGet(TARGET_URL).execute(new AsyncCompletionHandler<Response>() {

    			@Override
    			public Response onCompleted(Response response) throws IOException {
    				ApplicationLauncher.logger.info("lstamper_getFirmwareVersion: onCompleted");

    				if(response.getStatusCode() == 200){
    					Gson gson = new Gson();
    					String myResp = new String();
    					myResp=response.getResponseBody().toString();
    					RestAPIResponse myCurrentAPIResponse = new RestAPIResponse();

    					myCurrentAPIResponse = gson.fromJson(myResp,RestAPIResponse.class);
    					String msgSuccess = new String();
    					msgSuccess = "200";



    					if (myCurrentAPIResponse.getStatusCode().equals(msgSuccess) ) {
    						String FirmwareVersionData;
    						FirmwareVersionData=myCurrentAPIResponse.getFirmwareVersion().toString();
    						ApplicationLauncher.logger.info("lstamper_getFirmwareVersion : FirmwareVersionData:"+FirmwareVersionData);
    						ServerSettingController.UpdateDisplayServerFirmwareVersion(FirmwareVersionData);

    					}

    				}
    				c.close();
    				return response;
    			}



    			@Override
    			public void onThrowable(Throwable t) {
    				ApplicationLauncher.logger.info("lstamper_getFirmwareVersion: onThrowable:"+t.getMessage());
    				c.close();
    			}
    		});
    	} catch (Exception e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    		ApplicationLauncher.logger.error("lstamper_getFirmwareVersion: IOException:" + e1.getMessage());
    	}



    }
    
    public  void lstamper_Scan_Available_drives(Request request){

    	ApplicationLauncher.logger.info("lstamper_Scan_Available_drives: Entry");

    	AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(30000).build());
    	Future f = null;
    	try {
     		f = c.prepareRequest(request).execute(new AsyncCompletionHandler<Response>() {

    			@Override
    			public Response onCompleted(Response response) throws IOException {
    				ApplicationLauncher.logger.info("lstamper_Scan_Available_drives: onCompleted");
    				if(response.getStatusCode() == 200){
    					Gson gson = new Gson();
    					String myResp = new String();
    					myResp=response.getResponseBody("ISO-8859-1").toString();
    					ApplicationLauncher.logger.info("lstamper_Scan_Available_drives: myResp:"+myResp);
    					RestAPIResponse myCurrentAPIResponse = new RestAPIResponse();
    					myCurrentAPIResponse = gson.fromJson(myResp,RestAPIResponse.class);
    					String msgSuccess = new String();
    					msgSuccess = "200";
    					//ApplicationLauncher.logger.info("lstamper_Scan_Available_LogFolders: test2:");


    					if (myCurrentAPIResponse.getStatusCode().equals(msgSuccess) ) {
                            
                            ApplicationLauncher.logger.debug("lstamper_Scan_Available_drives: Success");
                            String[] pi_drives;
                            pi_drives = myCurrentAPIResponse.getAvailableDrives();
                            //String[] pi_drives= new String[]{"3", "4", "5"};
                            for(int i=0;i<pi_drives.length;i++) {
                                System.out.println("lstamper_Scan_Available_drives:pi_drives:" +i+":"+ pi_drives[i]);
                            }
                            FirmwareUpgradeController.updateListOfDrives(pi_drives);
/*                            
                            try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
							//	// TODO Auto-generated catch block
								e.printStackTrace();
							}*/
                           // final String[] Log_Folders = myCurrentAPIResponse.getAvailableLogFolders();
                            //String[] pi_drives= new String[]{"3", "4", "5"};
/*                            for(int i=0;i<Log_Folders.length;i++) {
                                System.out.println("lstamper_Scan_Available_LogFolders:pi_drives:" +i+":"+ Log_Folders[i]);
                            }*/
                            //Platform.runLater(() -> {
/*                            LogViewController.updateListOfLogFolders(Log_Folders);
                            LogViewController.EnableScanLogFolderButton();
                            LogViewController.EnableCmbBxListOfLogFolder();*/
                            //});

    					}

    				}
    				
    				c.close();
    				return response;
    			}



    			@Override
    			public void onThrowable(Throwable t) {
    				ApplicationLauncher.logger.info("lstamper_Scan_Available_drives: onThrowable:"+t.getMessage());
/*    				LogViewController.EnableScanLogFolderButton();
    				LogViewController.EnableCmbBxListOfLogFolder();*/
    				c.close();
    			}
    		});
    	} catch (Exception e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    		ApplicationLauncher.logger.error("lstamper_Scan_Available_LogFolders: IOException:" + e1.getMessage());
    	}



    }
    
    
    public  void lstamper_Scan_files_in_drives(Request request){

    	ApplicationLauncher.logger.info("lstamper_Scan_files_in_drives: Entry");

    	AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(30000).build());
    	Future f = null;
    	try {
     		f = c.prepareRequest(request).execute(new AsyncCompletionHandler<Response>() {

    			@Override
    			public Response onCompleted(Response response) throws IOException {
    				ApplicationLauncher.logger.info("lstamper_Scan_files_in_drives: onCompleted");
    				if(response.getStatusCode() == 200){
    					Gson gson = new Gson();
    					String myResp = new String();
    					myResp=response.getResponseBody("ISO-8859-1").toString();
    					ApplicationLauncher.logger.info("lstamper_Scan_files_in_drives: myResp:"+myResp);
    					RestAPIResponse myCurrentAPIResponse = new RestAPIResponse();
    					myCurrentAPIResponse = gson.fromJson(myResp,RestAPIResponse.class);
    					String msgSuccess = new String();
    					msgSuccess = "200";
    					//ApplicationLauncher.logger.info("lstamper_Scan_Available_LogFolders: test2:");


    					if (myCurrentAPIResponse.getStatusCode().equals(msgSuccess) ) {
                            
                            ApplicationLauncher.logger.debug("lstamper_Scan_files_in_drives: Success");
                            String[] Available_files_in_drive;
                            Available_files_in_drive = myCurrentAPIResponse.getAvailableFilesinDrives();
                            for(int i=0;i<Available_files_in_drive.length;i++) {
                                System.out.println("lstamper_Scan_files_in_drives:Available_files_in_drive:" +i+":"+ Available_files_in_drive[i]);
                            }
                            FirmwareUpgradeController.updatefilesinselecteddrive(Available_files_in_drive);
/*                            
                            try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
							//	// TODO Auto-generated catch block
								e.printStackTrace();
							}*/
                           // final String[] Log_Folders = myCurrentAPIResponse.getAvailableLogFolders();
                            //String[] pi_drives= new String[]{"3", "4", "5"};
/*                            for(int i=0;i<Log_Folders.length;i++) {
                                System.out.println("lstamper_Scan_Available_LogFolders:pi_drives:" +i+":"+ Log_Folders[i]);
                            }*/
                            //Platform.runLater(() -> {
/*                            LogViewController.updateListOfLogFolders(Log_Folders);
                            LogViewController.EnableScanLogFolderButton();
                            LogViewController.EnableCmbBxListOfLogFolder();*/
                            //});

    					}

    				}
    				
    				c.close();
    				return response;
    			}



    			@Override
    			public void onThrowable(Throwable t) {
    				ApplicationLauncher.logger.info("lstamper_Scan_files_in_drives: onThrowable:"+t.getMessage());
/*    				LogViewController.EnableScanLogFolderButton();
    				LogViewController.EnableCmbBxListOfLogFolder();*/
    				c.close();
    			}
    		});
    	} catch (Exception e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    		ApplicationLauncher.logger.error("lstamper_Scan_files_in_drives: Exception:" + e1.getMessage());
    	}



    }
    
    
    
    public  void lstamper_validate_selected_file(Request request){

    	ApplicationLauncher.logger.info("lstamper_validate_selected_file: Entry");

    	AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(30000).build());
    	Future f = null;
    	try {
     		f = c.prepareRequest(request).execute(new AsyncCompletionHandler<Response>() {

    			@Override
    			public Response onCompleted(Response response) throws IOException {
    				ApplicationLauncher.logger.info("lstamper_validate_selected_file: onCompleted");
    				if(response.getStatusCode() == 200){
    					Gson gson = new Gson();
    					String myResp = new String();
    					myResp=response.getResponseBody("ISO-8859-1").toString();
    					ApplicationLauncher.logger.info("lstamper_validate_selected_file: myResp:"+myResp);
    					RestAPIResponse myCurrentAPIResponse = new RestAPIResponse();
    					myCurrentAPIResponse = gson.fromJson(myResp,RestAPIResponse.class);
    					String msgSuccess = new String();
    					msgSuccess = "200";
    					//ApplicationLauncher.logger.info("lstamper_Scan_Available_LogFolders: test2:");


    					if (myCurrentAPIResponse.getStatusCode().equals(msgSuccess) ) {
                            
                            ApplicationLauncher.logger.debug("lstamper_validate_selected_file: Success");
                           String Validation_Result;
                            Validation_Result = myCurrentAPIResponse.getValidationResult();
                            FirmwareUpgradeController.updateValidationResult(Validation_Result);

    					}
    					 else{
    	                       // String Validation_Result;
    	                        //Validation_Result = myCurrentAPIResponse.getValidationResult();
    						 FirmwareUpgradeController.updateValidationResult(ConvErrorCodeMapping.ERROR_CODE_405_MSG);
    	                    }

    				}
    				
    				c.close();
    				return response;
    			}



    			@Override
    			public void onThrowable(Throwable t) {
    				ApplicationLauncher.logger.info("lstamper_validate_selected_file: onThrowable:"+t.getMessage());
/*    				LogViewController.EnableScanLogFolderButton();
    				LogViewController.EnableCmbBxListOfLogFolder();*/
    				c.close();
    			}
    		});
    	} catch (Exception e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    		ApplicationLauncher.logger.error("lstamper_validate_selected_file: Exception:" + e1.getMessage());
    	}



    }
    
    public  void lstamper_DeployGUI_File(Request request){

    	ApplicationLauncher.logger.info("lstamper_DeployGUI_File: Entry");

    	AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(30000).build());
    	Future f = null;
    	try {
     		f = c.prepareRequest(request).execute(new AsyncCompletionHandler<Response>() {

    			@Override
    			public Response onCompleted(Response response) throws IOException {
    				ApplicationLauncher.logger.info("lstamper_DeployGUI_File: onCompleted");
    				if(response.getStatusCode() == 200){
    					Gson gson = new Gson();
    					String myResp = new String();
    					myResp=response.getResponseBody("ISO-8859-1").toString();
    					ApplicationLauncher.logger.info("lstamper_DeployGUI_File: myResp:"+myResp);
    					RestAPIResponse myCurrentAPIResponse = new RestAPIResponse();
    					myCurrentAPIResponse = gson.fromJson(myResp,RestAPIResponse.class);
    					String msgSuccess = new String();
    					msgSuccess = "200";
    					//ApplicationLauncher.logger.info("lstamper_Scan_Available_LogFolders: test2:");


    					if (myCurrentAPIResponse.getStatusCode().equals(msgSuccess) ) {
                            
                            ApplicationLauncher.logger.debug("lstamper_DeployGUI_File: Success");
                           String Deploy_result;

                            Deploy_result = myCurrentAPIResponse.getDeployResult();

                            FirmwareUpgradeController.updateDeployGUI_Status(Deploy_result);

    					}

    				}
    				
    				c.close();
    				return response;
    			}



    			@Override
    			public void onThrowable(Throwable t) {
    				ApplicationLauncher.logger.info("lstamper_DeployGUI_File: onThrowable:"+t.getMessage());
/*    				LogViewController.EnableScanLogFolderButton();
    				LogViewController.EnableCmbBxListOfLogFolder();*/
    				c.close();
    			}
    		});
    	} catch (Exception e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    		ApplicationLauncher.logger.error("lstamper_DeployGUI_File: Exception:" + e1.getMessage());
    	}



    }
    
    public  void lstamper_Deploy_FileV2(Request request){

    	ApplicationLauncher.logger.info("lstamper_Deploy_FileV2: Entry");

    	AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(30000).build());
    	Future f = null;
    	try {
     		f = c.prepareRequest(request).execute(new AsyncCompletionHandler<Response>() {

    			@Override
    			public Response onCompleted(Response response) throws IOException {
    				ApplicationLauncher.logger.info("lstamper_Deploy_FileV2: onCompleted");
    				if(response.getStatusCode() == 200){
    					Gson gson = new Gson();
    					String myResp = new String();
    					myResp=response.getResponseBody("ISO-8859-1").toString();
    					ApplicationLauncher.logger.info("lstamper_Deploy_FileV2: myResp:"+myResp);
    					RestAPIResponse myCurrentAPIResponse = new RestAPIResponse();
    					myCurrentAPIResponse = gson.fromJson(myResp,RestAPIResponse.class);
    					String msgSuccess = new String();
    					msgSuccess = "200";
    					//ApplicationLauncher.logger.info("lstamper_Scan_Available_LogFolders: test2:");


    					if (myCurrentAPIResponse.getStatusCode().equals(msgSuccess) ) {
                            
                            ApplicationLauncher.logger.debug("lstamper_Deploy_FileV2: Success");
                           String Deploy_result;

                            Deploy_result = myCurrentAPIResponse.getDeployResult();

                            FirmwareUpgradeController.updateDeployStatusV2(Deploy_result);

    					}

    				}
    				
    				c.close();
    				return response;
    			}



    			@Override
    			public void onThrowable(Throwable t) {
    				ApplicationLauncher.logger.info("lstamper_Deploy_FileV2: onThrowable:"+t.getMessage());
/*    				LogViewController.EnableScanLogFolderButton();
    				LogViewController.EnableCmbBxListOfLogFolder();*/
    				c.close();
    			}
    		});
    	} catch (Exception e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    		ApplicationLauncher.logger.error("lstamper_Deploy_FileV2: Exception:" + e1.getMessage());
    	}



    }
    
    public  void lstamper_Deploy_File(Request request){

    	ApplicationLauncher.logger.info("lstamper_Deploy_File: Entry");

    	AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(30000).build());
    	Future f = null;
    	try {
     		f = c.prepareRequest(request).execute(new AsyncCompletionHandler<Response>() {

    			@Override
    			public Response onCompleted(Response response) throws IOException {
    				ApplicationLauncher.logger.info("lstamper_Deploy_File: onCompleted");
    				if(response.getStatusCode() == 200){
    					Gson gson = new Gson();
    					String myResp = new String();
    					myResp=response.getResponseBody("ISO-8859-1").toString();
    					ApplicationLauncher.logger.info("lstamper_Deploy_File: myResp:"+myResp);
    					RestAPIResponse myCurrentAPIResponse = new RestAPIResponse();
    					myCurrentAPIResponse = gson.fromJson(myResp,RestAPIResponse.class);
    					String msgSuccess = new String();
    					msgSuccess = "200";
    					//ApplicationLauncher.logger.info("lstamper_Scan_Available_LogFolders: test2:");


    					if (myCurrentAPIResponse.getStatusCode().equals(msgSuccess) ) {
                            
                            ApplicationLauncher.logger.debug("lstamper_Deploy_File: Success");
                           String Deploy_result;

                            Deploy_result = myCurrentAPIResponse.getDeployResult();

                            FirmwareUpgradeController.updateDeployStatus(Deploy_result);

    					}

    				}
    				
    				c.close();
    				return response;
    			}



    			@Override
    			public void onThrowable(Throwable t) {
    				ApplicationLauncher.logger.info("lstamper_Deploy_File: onThrowable:"+t.getMessage());
/*    				LogViewController.EnableScanLogFolderButton();
    				LogViewController.EnableCmbBxListOfLogFolder();*/
    				c.close();
    			}
    		});
    	} catch (Exception e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    		ApplicationLauncher.logger.error("lstamper_Deploy_File: Exception:" + e1.getMessage());
    	}



    }

    public  void lstamper_Scan_Available_LogFoldersV2(Request request){

    	ApplicationLauncher.logger.info("lstamper_Scan_Available_LogFoldersv2: Entry");

    	AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(30000).build());
    	Future f = null;
    	try {
     		f = c.prepareRequest(request).execute(new AsyncCompletionHandler<Response>() {

    			@Override
    			public Response onCompleted(Response response) throws IOException {
    				ApplicationLauncher.logger.info("lstamper_Scan_Available_LogFolders: onCompleted");
    				if(response.getStatusCode() == 200){
    					Gson gson = new Gson();
    					String myResp = new String();
    					myResp=response.getResponseBody("ISO-8859-1").toString();
    					ApplicationLauncher.logger.info("lstamper_Scan_Available_LogFolders: myResp:"+myResp);
    					RestAPIResponse myCurrentAPIResponse = new RestAPIResponse();
    					//ApplicationLauncher.logger.info("lstamper_Scan_Available_LogFolders: test1:");
    					myCurrentAPIResponse = gson.fromJson(myResp,RestAPIResponse.class);
    					String msgSuccess = new String();
    					msgSuccess = "200";
    					//ApplicationLauncher.logger.info("lstamper_Scan_Available_LogFolders: test2:");


    					if (myCurrentAPIResponse.getStatusCode().equals(msgSuccess) ) {
                            
/*                            ApplicationLauncher.logger.debug("lstamper_Scan_Available_LogFolders: Success");
                            
                            try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
							//	// TODO Auto-generated catch block
								e.printStackTrace();
							}*/
                            final String[] Log_Folders = myCurrentAPIResponse.getAvailableLogFolders();
                            //String[] pi_drives= new String[]{"3", "4", "5"};
/*                            for(int i=0;i<Log_Folders.length;i++) {
                                System.out.println("lstamper_Scan_Available_LogFolders:pi_drives:" +i+":"+ Log_Folders[i]);
                            }*/
                            //Platform.runLater(() -> {
                            LogViewController.updateListOfLogFolders(Log_Folders);
                            LogViewController.EnableScanLogFolderButton();
                            LogViewController.EnableCmbBxListOfLogFolder();
                            //});

    					}

    				}
    				
    				c.close();
    				return response;
    			}



    			@Override
    			public void onThrowable(Throwable t) {
    				ApplicationLauncher.logger.info("lstamper_Scan_Available_LogFolders: onThrowable:"+t.getMessage());
    				LogViewController.EnableScanLogFolderButton();
    				LogViewController.EnableCmbBxListOfLogFolder();
    				c.close();
    			}
    		});
    	} catch (Exception e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    		ApplicationLauncher.logger.error("lstamper_Scan_Available_LogFolders: Exception:" + e1.getMessage());
    	}



    }
    
    
    
    public  void lstamper_Scan_Available_LogFolders(String getMethodURL){

    	ApplicationLauncher.logger.info("lstamper_Scan_Available_LogFolders: Entry");
    	String TARGET_URL = ServerSettingController.getRootUrl()
    			+"/"+getMethodURL;

    	AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(5000).build());
    	Future f = null;
    	try {
    		//ResponseHandler<String> responseHandler = new BasicResponseHandler();
    		f = c.prepareGet(TARGET_URL).execute(new AsyncCompletionHandler<Response>() {

    			@Override
    			public Response onCompleted(Response response) throws IOException {
    				ApplicationLauncher.logger.info("lstamper_Scan_Available_LogFolders: onCompleted");
    				if(response.getStatusCode() == 200){
    					Gson gson = new Gson();
    					String myResp = new String();
    					myResp=response.getResponseBody().toString();
    					ApplicationLauncher.logger.info("lstamper_Scan_Available_LogFolders: myResp:"+myResp);
    					RestAPIResponse myCurrentAPIResponse = new RestAPIResponse();
    					//ApplicationLauncher.logger.info("lstamper_Scan_Available_LogFolders: test1:");
    					myCurrentAPIResponse = gson.fromJson(myResp,RestAPIResponse.class);
    					String msgSuccess = new String();
    					msgSuccess = "200";
    					//ApplicationLauncher.logger.info("lstamper_Scan_Available_LogFolders: test2:");


    					if (myCurrentAPIResponse.getStatusCode().equals(msgSuccess) ) {
                            
/*                            ApplicationLauncher.logger.debug("lstamper_Scan_Available_LogFolders: Success");
                            
                            try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
							//	// TODO Auto-generated catch block
								e.printStackTrace();
							}*/
                            final String[] Log_Folders = myCurrentAPIResponse.getAvailableLogFolders();
                            //String[] pi_drives= new String[]{"3", "4", "5"};
/*                            for(int i=0;i<Log_Folders.length;i++) {
                                System.out.println("lstamper_Scan_Available_LogFolders:pi_drives:" +i+":"+ Log_Folders[i]);
                            }*/
                            //Platform.runLater(() -> {
                            LogViewController.updateListOfLogFolders(Log_Folders);
                            //});
                            LogViewController.EnableScanLogFolderButton();
                            LogViewController.EnableCmbBxListOfLogFolder();
    					}

    				}
    				
    				c.close();
    				return response;
    			}



    			@Override
    			public void onThrowable(Throwable t) {
    				ApplicationLauncher.logger.info("lstamper_Scan_Available_LogFolders: onThrowable:"+t.getMessage());
    				LogViewController.EnableScanLogFolderButton();
    				LogViewController.EnableCmbBxListOfLogFolder();
    				c.close();
    			}
    		});
    	} catch (Exception e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    		ApplicationLauncher.logger.error("lstamper_Scan_Available_LogFolders: Exception:" + e1.getMessage());
    	}



    }
    
    public  void ValidateCred(String getMethodURL){

    	ApplicationLauncher.logger.info("ValidateCred: Entry");
    	String TARGET_URL = ServerSettingController.getRootUrl()
    			+"/"+getMethodURL;

    	AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(5000).build());
    	Future f = null;
    	try {

    		f = c.prepareGet(TARGET_URL).execute(new AsyncCompletionHandler<Response>() {

    			@Override
    			public Response onCompleted(Response response) throws IOException {
    				ApplicationLauncher.logger.info("ValidateCred: onCompleted");
    				//ProjectExecutionController.updateserverstatus("success");
    				if(response.getStatusCode() == 200){
    					Gson gson = new Gson();
    					String myResp = new String();
    					myResp=response.getResponseBody().toString();
    					ApplicationLauncher.logger.info("ValidateCred: myResp:"+myResp);
    					RestAPIResponse myCurrentAPIResponse = new RestAPIResponse();

    					myCurrentAPIResponse = gson.fromJson(myResp,RestAPIResponse.class);
    					String msgSuccess = new String();
    					msgSuccess = "200";



    					if (myCurrentAPIResponse.getStatusCode().equals(msgSuccess) ) {
    						ApplicationLauncher.logger.info("ValidateCred: API-Response-Success");
    	                    //if(myCurrentAPIResponse.getDevice_name().equals(ModeConstant.DEVICE_NAME_LS_PROTAMP)) {
    	                    if(	StringUtils.indexOfAny(myCurrentAPIResponse.getDevice_name(),ConstantProTamp.DEVICE_NAME_LIST ) != -1){
    	                    	ApplicationLauncher.logger.info("ValidateCred: test190");
    	                    	//ProjectExecutionController.updateserverstatus("success");
    	                        //EnableProtampButton();//migrateincomplete
    	                    	//ScanDeviceController.ScanDeviceCompletedPostProcess();
    	                    	ScanDeviceController.getValidateCredResponseSuccessTaskTrigger(myCurrentAPIResponse);
    	                        //ProjectExecutionController.updateserverstatus("success");
    	                    }
    	                 // below else case Added new logic in 3 Phase ServerProperties
    					}else if (myCurrentAPIResponse.getStatusCode().equals(ServerProperties.SERVER_CONNECTION_SLAVE_COMM_FAILED) ) {
    					//else if (myCurrentAPIResponse.getStatusCode().equals(ConstantProTamp.SLAVE_COMM_FAILED) ) {
    						//if(myCurrentAPIResponse.getDevice_name().equals(ModeConstant.DEVICE_NAME_LS_PROTAMP)) {
    						if(	StringUtils.indexOfAny(myCurrentAPIResponse.getDevice_name(),ConstantProTamp.DEVICE_NAME_LIST ) != -1){
        	                    
	    						ApplicationLauncher.logger.info("ValidateCred: test192");
	    						//ProjectExecutionController.updateserverstatus(ConstantProTamp.SLAVE_COMM_FAILED);
	    						ScanDeviceController.getValidateCredResponseSlaveCommFailedTaskTrigger(myCurrentAPIResponse);
	    						//ScanDeviceController.ScanDeviceCompletedPostProcess();
	    						//ScanDeviceController.ProtampBootupDisplayVisible(true);
/*	    						try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
	    						ScanDeviceController.setPanelBootingDisplayStatus("SlaveCom Failed");
	    						ApplicationHomeController.update_labelBootupStatus("SlaveCom Failed");*/
	    						
    						}
    						
						} else if (myCurrentAPIResponse.getStatusCode().equals(ServerProperties.SERVER_CONNECTION_SLAVE_WAITING) ){
    					//else if (myCurrentAPIResponse.getStatusCode().equals(ConstantProTamp.SLAVE_WAITING) ){
    						//if(myCurrentAPIResponse.getDevice_name().equals(ModeConstant.DEVICE_NAME_LS_PROTAMP)) {
							if(	StringUtils.indexOfAny(myCurrentAPIResponse.getDevice_name(),ConstantProTamp.DEVICE_NAME_LIST ) != -1){
	    	                    
    							ApplicationLauncher.logger.info("ValidateCred: test191");
    							ScanDeviceController.getValidateCredResponseBootingTaskTrigger(myCurrentAPIResponse);
    							//ProjectExecutionController.updateserverstatus(ConstantProTamp.SLAVE_WAITING);  // Added new logic in 3 Phase
    							//ScanDeviceController.triggerBootupStatusTimerTask();
    							//ProjectExecutionController.updateserverstatus("booting");
    	                        //EnableBootingDisplay(DEVICE_NAME_LS_PROTAMP);

    	                    }
    					}

    				}else{
    					//ProjectExecutionController.updateServerStatus(ServerProperties.SERVER_CONNECTION_FAILED);
    				}
    				c.close();
    				return response;
    			}



    			@Override
    			public void onThrowable(Throwable t) {
    				ApplicationLauncher.logger.info("ValidateCred: onThrowable:"+t.getMessage());
    				//ProjectExecutionController.updateserverstatus("failure");
    				ScanDeviceController.getValidateCredResponseOnThrowTaskTrigger(t.getMessage());
    				c.close();
    			}
    		});
    	} catch (Exception e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    		ApplicationLauncher.logger.error("ValidateCred: Exception:" + e1.getMessage());
    	}



    }
    
    public  void Shutdown(String getMethodURL){

    	ApplicationLauncher.logger.info("Shutdown: Entry");
    	String TARGET_URL = ServerSettingController.getRootUrl()
    			+"/"+getMethodURL;

    	AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(5000).build());
    	Future f = null;
    	try {

    		f = c.prepareGet(TARGET_URL).execute(new AsyncCompletionHandler<Response>() {

    			@Override
    			public Response onCompleted(Response response) throws IOException {
    				ApplicationLauncher.logger.info("Shutdown: onCompleted");
    				//ProjectExecutionController.updateServerStatus(ServerProperties.SERVER_CONNECTION_SUCCESS);
    				if(response.getStatusCode() == 200){
    					Gson gson = new Gson();
    					String myResp = new String();
    					myResp=response.getResponseBody().toString();
    					RestAPIResponse myCurrentAPIResponse = new RestAPIResponse();

    					myCurrentAPIResponse = gson.fromJson(myResp,RestAPIResponse.class);
    					String msgSuccess = new String();
    					msgSuccess = "200";



    					if (myCurrentAPIResponse.getStatusCode().equals(msgSuccess) ) {
    						ApplicationLauncher.logger.info("Shutdown: API-Response-Success");

    					}

    				}
    				c.close();
    				return response;
    			}



    			@Override
    			public void onThrowable(Throwable t) {
    				ApplicationLauncher.logger.info("Shutdown: onThrowable:"+t.getMessage());
    				c.close();
    			}
    		});
    	} catch (Exception e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    		ApplicationLauncher.logger.error("Shutdown: Exception:" + e1.getMessage());
    	}



    }
    
    public void lstamper_Manual_Stop(Request request){
    	
    	ApplicationLauncher.logger.info("lstamper_Manual_Stop: Entry");

    	AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(30000).build());
    	Future f = null;
    	try {
     		f = c.prepareRequest(request).execute(new AsyncCompletionHandler<Response>() {

    			@Override
    			public Response onCompleted(Response response) throws IOException {

    				ApplicationLauncher.logger.info("lstamper_Manual_Stop: onCompleted");
    				//ProjectExecutionController.cancelMonitorServerTimer();
    				//ProjectExecutionController.MonitorResponseFromServer();
    				c.close();
    				return response;
    			}



    			@Override
    			public void onThrowable(Throwable t) {
    				ApplicationLauncher.logger.info("lstamper_Manual_Stop: onThrowable:"+t.getMessage());
    				c.close();
    			}
    		});
    	} catch (Exception e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    		ApplicationLauncher.logger.error("lstamper_Manual_Stop: Exception:" + e1.getMessage());
    	}
    	
    }
    
    
    public void lstamper_Auto_Stop(Request request){
    	
    	ApplicationLauncher.logger.info("lstamper_Auto_Stop: Entry");

    	AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(30000).build());
    	Future f = null;
    	try {
     		f = c.prepareRequest(request).execute(new AsyncCompletionHandler<Response>() {

    			@Override
    			public Response onCompleted(Response response) throws IOException {

    				ApplicationLauncher.logger.info("lstamper_Auto_Stop: onCompleted");
    				//ProjectExecutionController.cancelMonitorServerTimer();
    				//ProjectExecutionController.MonitorResponseFromServer();
    				c.close();
    				return response;
    			}



    			@Override
    			public void onThrowable(Throwable t) {
    				ApplicationLauncher.logger.info("lstamper_Auto_Stop: onThrowable:"+t.getMessage());
    				c.close();
    			}
    		});
    	} catch (Exception e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    		ApplicationLauncher.logger.error("lstamper_Auto_Stop: Exception:" + e1.getMessage());
    	}
    	
    }
    
    
    
    public void lstamper_Manual_Start(Request request){
    	
    	ApplicationLauncher.logger.info("lstamper_Manual_Start: Entry");

    	AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(30000).build());
    	Future f = null;
    	try {
     		f = c.prepareRequest(request).execute(new AsyncCompletionHandler<Response>() {

    			@Override
    			public Response onCompleted(Response response) throws IOException {

    				ApplicationLauncher.logger.info("lstamper_Manual_Start: onCompleted");
    			/*	ProjectExecutionController.ClearDataOnCurrentRow();
    				ProjectExecutionController.setExecutionInProgress(true);
    				ProjectExecutionController.MonitorResponseFromServer();*/

    				c.close();
    				return response;
    			}



    			@Override
    			public void onThrowable(Throwable t) {
    				ApplicationLauncher.logger.info("lstamper_Manual_Start: onThrowable:"+t.getMessage());
    				c.close();
    			}
    		});
    	} catch (Exception e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    		ApplicationLauncher.logger.error("lstamper_Manual_Start: Exception:" + e1.getMessage());
    	}
    	
    }
    
    
    
    public void lsTamperManual3PhaseStartWithRefStd(Request request){
    	
    	ApplicationLauncher.logger.info("lsTamperManual3PhaseStartWithRefStd: Entry");

    	AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(30000).build());
    	Future f = null;
    	try {
     		f = c.prepareRequest(request).execute(new AsyncCompletionHandler<Response>() {

    			@Override
    			public Response onCompleted(Response response) throws IOException {

    				ApplicationLauncher.logger.info("lsTamperManual3PhaseStartWithRefStd: onCompleted");
/*    				ProjectExecutionController.ClearDataOnCurrentRow();
    				ProjectExecutionController.setExecutionInProgress(true);
    				//ProjectExecutionController.MonitorResponseFromServer();dgdf
    				ProjectExecutionController.monitorResponseFromServer3PhaseWithRefStd();*/
    				
    				c.close();
    				return response;
    			}



    			@Override
    			public void onThrowable(Throwable t) {
    				ApplicationLauncher.logger.info("lsTamperManual3PhaseStartWithRefStd: onThrowable:"+t.getMessage());
    				c.close();
    			}
    		});
    	} catch (Exception e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    		ApplicationLauncher.logger.error("lsTamperManual3PhaseStartWithRefStd: Exception:" + e1.getMessage());
    	}
    	
    }
    
    
    
    
    
    public void lsTamperAuto3PhaseStartWithRefStd(Request request){
    	
    	ApplicationLauncher.logger.info("lsTamperAuto3PhaseStartWithRefStd: Entry");

    	AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(30000).build());
    	Future f = null;
    	try {
     		f = c.prepareRequest(request).execute(new AsyncCompletionHandler<Response>() {

    			@Override
    			public Response onCompleted(Response response) throws IOException {

    				ApplicationLauncher.logger.info("lsTamperAuto3PhaseStartWithRefStd: onCompleted");
/*    				ProjectExecutionController.ClearDataOnCurrentRow();
    				ProjectExecutionController.setExecutionInProgress(true);
    				//ProjectExecutionController.MonitorResponseFromServer();dgdf
    				ProjectExecutionController.monitorResponseFromServer3PhaseWithRefStd();*/
    				
    				
    				c.close();
    				return response;
    			}



    			@Override
    			public void onThrowable(Throwable t) {
    				ApplicationLauncher.logger.info("lsTamperAuto3PhaseStartWithRefStd: onThrowable:"+t.getMessage());
    				c.close();
    			}
    		});
    	} catch (Exception e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    		ApplicationLauncher.logger.error("lsTamperAuto3PhaseStartWithRefStd: Exception:" + e1.getMessage());
    	}
    	
    }
    
    public boolean lsTamperRefStdPreRequisite(Request request){
    	ApplicationLauncher.logger.info("lsTamperRefStdPreRequisite: Entry");
    	//final boolean status = false ;
    	
    	AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(40000).build());
    	Future f = null;
    	try {
     		f = c.prepareRequest(request).execute(new AsyncCompletionHandler<Response>() {

    			@Override
    			public Response onCompleted(Response response) throws IOException {

    				ApplicationLauncher.logger.info("lsTamperRefStdPreRequisite: onCompleted");
    				
    				
    				if(response.getStatusCode() == 200){
						Gson gson = new Gson();
						String myResp = new String();
						myResp=response.getResponseBody().toString();
						RestAPIResponse myCurrentAPIResponse = new RestAPIResponse();

						myCurrentAPIResponse = gson.fromJson(myResp,RestAPIResponse.class);
						String msgSuccess = new String();
						msgSuccess = "200";

						if (myCurrentAPIResponse.getStatusCode().equals(msgSuccess) ) {
							//ApplicationLauncher.logger.info("lsTamperRefStdPreRequisite :testid:"+myCurrentAPIResponse.getTestid());
							ApplicationLauncher.logger.info("lsTamperRefStdPreRequisite : getRefStdStatus(): "+myCurrentAPIResponse.getRefStdStatus());
							if(myCurrentAPIResponse.getRefStdStatus().equals("RefStdSuccess")){
								setPreRequisiteResponseStatus(true );
								
								ApplicationLauncher.logger.info("lsTamperRefStdPreRequisite : API-ResponseF3 - Ref Success");

							}else {
								
								ApplicationLauncher.logger.info("lsTamperRefStdPreRequisite : API-ResponseF3- Else");
								//ProjectExecutionController.getStatusResponseStoppedTaskTrigger(myCurrentAPIResponse);
								setPreRequisiteResponseStatus(false );
							}

						}else{
							ApplicationLauncher.logger.info("lsTamperRefStdPreRequisite : API-ResponseF3:else"+myCurrentAPIResponse.getStatusCode());
							setPreRequisiteResponseStatus(false );
						}
						
						AsyncClientManager.setResponseReceived(true);

    				}
    				//ProjectExecutionController.ClearDataOnCurrentRow();
    				//ProjectExecutionController.setExecutionInProgress(true);
    				//ProjectExecutionController.MonitorResponseFromServer();dgdf
    				//ProjectExecutionController.monitorResponseFromServer3PhaseWithRefStd();
    				c.close();
    				return response;
    			}



    			@Override
    			public void onThrowable(Throwable t) {
    				ApplicationLauncher.logger.info("lsTamperRefStdPreRequisite: onThrowable:"+t.getMessage());
    				c.close();
    			}
    		});
    	} catch (Exception e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    		ApplicationLauncher.logger.error("lsTamperRefStdPreRequisite: Exception:" + e1.getMessage());
    	}
    	
    	return preRequisiteResponseStatus ;
    }
    
    
    public void lstamper_Manual_Start50(Request request){
    	
    	ApplicationLauncher.logger.info("lstamper_Manual_Start50: Entry");

    	AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(30000).build());
    	Future f = null;
    	try {
     		f = c.prepareRequest(request).execute(new AsyncCompletionHandler<Response>() {

    			@Override
    			public Response onCompleted(Response response) throws IOException {

    				ApplicationLauncher.logger.info("lstamper_Manual_Start50: onCompleted");
    			/*	ProjectExecutionController.ClearDataOnCurrentRow();
    				ProjectExecutionController.setExecutionInProgress(true);
    				ProjectExecutionController.MonitorResponseFromServer();*/
    				
    				c.close();
    				return response;
    			}



    			@Override
    			public void onThrowable(Throwable t) {
    				ApplicationLauncher.logger.info("lstamper_Manual_Start50: onThrowable:"+t.getMessage());
    				c.close();
    			}
    		});
    	} catch (Exception e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    		ApplicationLauncher.logger.error("lstamper_Manual_Start50: Exception:" + e1.getMessage());
    	}
    	
    }
    
    public void lstamper_Manual_Start51(Request request){
    	
    	ApplicationLauncher.logger.info("lstamper_Manual_Start51: Entry");

    	AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(30000).build());
    	Future f = null;
    	try {
     		f = c.prepareRequest(request).execute(new AsyncCompletionHandler<Response>() {

    			@Override
    			public Response onCompleted(Response response) throws IOException {

    				ApplicationLauncher.logger.info("lstamper_Manual_Start51: onCompleted");
    			/*	ProjectExecutionController.ClearDataOnCurrentRow();
    				ProjectExecutionController.setExecutionInProgress(true);
    				ProjectExecutionController.MonitorResponseFromServer();*/
    				
    				c.close();
    				return response;
    			}



    			@Override
    			public void onThrowable(Throwable t) {
    				ApplicationLauncher.logger.info("lstamper_Manual_Start51: onThrowable:"+t.getMessage());
    				c.close();
    			}
    		});
    	} catch (Exception e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    		ApplicationLauncher.logger.error("lstamper_Manual_Start51: Exception:" + e1.getMessage());
    	}
    	
    }
    
    
    
    public void lstamperManageLoadResistanceBank(Request request){
    	
    	ApplicationLauncher.logger.info("lstamperManageLoadResistanceBank: Entry");

    	AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(10000).build());
    	Future f = null;
    	try {
     		f = c.prepareRequest(request).execute(new AsyncCompletionHandler<Response>() {

    			@Override
    			public Response onCompleted(Response response) throws IOException {

    				ApplicationLauncher.logger.info("lstamperManageLoadResistanceBank: onCompleted");
    				//ProjectExecutionController.ClearDataOnCurrentRow();
    				//ProjectExecutionController.setExecutionInProgress(true);
    				//ProjectExecutionController.MonitorResponseFromServer();
    				
    				c.close();
    				return response;
    			}



    			@Override
    			public void onThrowable(Throwable t) {
    				ApplicationLauncher.logger.info("lstamperManageLoadResistanceBank: onThrowable:"+t.getMessage());
    				c.close();
    			}
    		});
    	} catch (Exception e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    		ApplicationLauncher.logger.error("lstamperManageLoadResistanceBank: Exception:" + e1.getMessage());
    	}
    	
    }
    
    public void lstamper_Auto_Start(Request request){
    	
    	ApplicationLauncher.logger.info("lstamper_Auto_Start: Entry");

    	AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(30000).build());
    	Future f = null;
    	try {
     		f = c.prepareRequest(request).execute(new AsyncCompletionHandler<Response>() {

    			@Override
    			public Response onCompleted(Response response) throws IOException {

    				ApplicationLauncher.logger.info("lstamper_Auto_Start: onCompleted");
    			/*	ProjectExecutionController.ClearDataOnCurrentRow();
    				ProjectExecutionController.setExecutionInProgress(true);
    				ProjectExecutionController.MonitorResponseFromServer();*/
    				
/*    				int timeduration = DisplayDataObj.getOccuranceTimeInSec();
    				ProjectExecutionController.setExecuteTimeCounter(timeduration);
    				ProjectExecutionController.ExecuteTimerDisplay();
    				ProjectExecutionController.UI_TableRefreshTrigger(timeduration);*/
    				c.close();
    				return response;
    			}



    			@Override
    			public void onThrowable(Throwable t) {
    				ApplicationLauncher.logger.info("lstamper_Auto_Start: onThrowable:"+t.getMessage());
    				c.close();
    			}
    		});
    	} catch (Exception e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    		ApplicationLauncher.logger.error("lstamper_Auto_Start: Exception:" + e1.getMessage());
    	}
    	
    }
    
    
    
    public void lstamper_Auto_Resume(Request request){
    	
    	ApplicationLauncher.logger.info("lstamper_Auto_Resume: Entry");

    	AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(30000).build());
    	Future f = null;
    	try {
     		f = c.prepareRequest(request).execute(new AsyncCompletionHandler<Response>() {

    			@Override
    			public Response onCompleted(Response response) throws IOException {

    				ApplicationLauncher.logger.info("lstamper_Auto_Resume: onCompleted");
    				/*ProjectExecutionController.setExecutionInProgress(true);
    				ProjectExecutionController.MonitorResponseFromServer();*/
    				c.close();
    				return response;
    			}



    			@Override
    			public void onThrowable(Throwable t) {
    				ApplicationLauncher.logger.info("lstamper_Auto_Resume: onThrowable:"+t.getMessage());
    				c.close();
    			}
    		});
    	} catch (Exception e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    		ApplicationLauncher.logger.error("lstamper_Auto_Resume: Exception:" + e1.getMessage());
    	}
    	
    }
    
    
    
    public void lstamper_viewlog_from_selected_file(Request request){
    	ApplicationLauncher.logger.info("lstamper_viewlog_from_selected_file: Entry");

    	AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(5000).build());
    	Future f = null;
    	try {
     		f = c.prepareRequest(request).execute(new AsyncCompletionHandler<Response>() {

    			@Override
    			public Response onCompleted(Response response) throws IOException {

    				ApplicationLauncher.logger.info("lstamper_viewlog_from_selected_file: onCompleted");
    				
    				Gson gson = new Gson();
    				String myResp = new String();
    				myResp=response.getResponseBody().toString();
    				RestAPIResponse myCurrentAPIResponse = new RestAPIResponse();

    				myCurrentAPIResponse = gson.fromJson(myResp,RestAPIResponse.class);

    				String msgSuccess = new String();
    				msgSuccess = "200";
    				if (myCurrentAPIResponse.getStatusCode().equals(msgSuccess) ) {
    					String Log_content;
    					try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        Log_content = myCurrentAPIResponse.getviewlogcontent();
                        LogViewController.updateViewLogContentSuccess(Log_content);
                        LogViewController.EnableScanLogFolderButton();
                        LogViewController.EnableCmbBxListOfLogFolder();
                        LogViewController.EnableScanLogFilesButton();
                        LogViewController.EnableViewLogFileButton();
                        LogViewController.EnableCmbBxListOfLogFiles();
    				}
    				c.close();
    				return response;
    			}



    			@Override
    			public void onThrowable(Throwable t) {
    				ApplicationLauncher.logger.info("lstamper_viewlog_from_selected_file: onThrowable:"+t.getMessage());
                    LogViewController.EnableScanLogFolderButton();
                    LogViewController.EnableCmbBxListOfLogFolder();
                    LogViewController.EnableScanLogFilesButton();
                    LogViewController.EnableViewLogFileButton();
                    LogViewController.EnableCmbBxListOfLogFiles();
                    LogViewController.updateViewLogContentFailure();
    				c.close();
    			}
    		});
    	} catch (Exception e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    		ApplicationLauncher.logger.error("lstamper_viewlog_from_selected_file: Exception:" + e1.getMessage());
    	}
    }
    
    

    public void lstamper_Scan_files_in_logfolder(Request request){
    	ApplicationLauncher.logger.info("lstamper_set_selected_Timezone: Entry");

    	AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(5000).build());
    	Future f = null;
    	try {
     		f = c.prepareRequest(request).execute(new AsyncCompletionHandler<Response>() {

    			@Override
    			public Response onCompleted(Response response) throws IOException {

    				ApplicationLauncher.logger.info("lstamper_Scan_files_in_logfolder: onCompleted");
    				
    				Gson gson = new Gson();
    				String myResp = new String();
    				myResp=response.getResponseBody().toString();
    				RestAPIResponse myCurrentAPIResponse = new RestAPIResponse();

    				myCurrentAPIResponse = gson.fromJson(myResp,RestAPIResponse.class);

    				String msgSuccess = new String();
    				msgSuccess = "200";



    				if (myCurrentAPIResponse.getStatusCode().equals(msgSuccess) ) {

                        String[] Available_files_in_folder;
                        Available_files_in_folder = myCurrentAPIResponse.getAvailableFilesinLogFolder();
                        for(int i=0;i<Available_files_in_folder.length;i++) {
                            System.out.println("lstamper_Scan_files_in_logfolder:Available_files_in_folder:" +i+":"+ Available_files_in_folder[i]);
                        }
                        LogViewController.updatefilesinlogview(Available_files_in_folder);
        				LogViewController.EnableScanLogFolderButton();
        				LogViewController.EnableCmbBxListOfLogFolder();
        				LogViewController.EnableScanLogFilesButton();
        				LogViewController.EnableCmbBxListOfLogFiles();
        				LogViewController.EnableViewLogFileButton();
    				}
    				c.close();
    				return response;
    			}



    			@Override
    			public void onThrowable(Throwable t) {
    				ApplicationLauncher.logger.info("lstamper_Scan_files_in_logfolder: onThrowable:"+t.getMessage());
    				LogViewController.EnableScanLogFolderButton();
    				LogViewController.EnableCmbBxListOfLogFolder();
    				LogViewController.EnableScanLogFilesButton();
    				LogViewController.EnableCmbBxListOfLogFiles();
    				LogViewController.DisableViewLogFileButton();
    				c.close();
    			}
    		});
    	} catch (Exception e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    		ApplicationLauncher.logger.error("lstamper_Scan_files_in_logfolder: Exception:" + e1.getMessage());
    	}
    }

    
    public void lstamper_set_selected_Timezone(Request request){
    	ApplicationLauncher.logger.info("lstamper_set_selected_Timezone: Entry");

    	AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(5000).build());
    	Future f = null;
    	try {
     		f = c.prepareRequest(request).execute(new AsyncCompletionHandler<Response>() {

    			@Override
    			public Response onCompleted(Response response) throws IOException {

    				ApplicationLauncher.logger.info("lstamper_set_selected_Timezone: onCompleted");
    				
    				Gson gson = new Gson();
    				String myResp = new String();
    				myResp=response.getResponseBody().toString();
    				RestAPIResponse myCurrentAPIResponse = new RestAPIResponse();

    				myCurrentAPIResponse = gson.fromJson(myResp,RestAPIResponse.class);

    				String msgSuccess = new String();
    				msgSuccess = "200";



    				if (myCurrentAPIResponse.getStatusCode().equals(msgSuccess) ) {

    					ApplicationLauncher.InformUser("TimeZone Success","Updated Server TimeZone success",AlertType.INFORMATION);
    					
    				}else{
    					ApplicationLauncher.InformUser("TimeZone Failed","Updated Server TimeZone failed, Kindly retry after some time",AlertType.ERROR);
    					
    				}
    				c.close();
    				return response;
    			}



    			@Override
    			public void onThrowable(Throwable t) {
    				ApplicationLauncher.logger.info("lstamper_set_selected_Timezone: onThrowable:"+t.getMessage());
    				c.close();
    			}
    		});
    	} catch (Exception e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    		ApplicationLauncher.logger.error("lstamper_set_selected_Timezone: Exception:" + e1.getMessage());
    	}
    }

	public static boolean isPreRequisiteResponseStatus() {
		return preRequisiteResponseStatus;
	}

	public static void setPreRequisiteResponseStatus(boolean input) {
		preRequisiteResponseStatus = input;
	}
	
/*	@SuppressWarnings("unchecked")
	public  void getStatus(){  
    ApplicationLauncher.logger.info("getStatus: Entry");

	//String TARGET_URL = "http://192.168.1.100:8080/lstamper_getStatus";
	String TARGET_URL = ServerProperties.HTTP_Protocol +
						ServerProperties.PublicURL_Id+
						ServerProperties.EndURL+":"+
						ServerProperties.URLPort+"/lstamper_getStatus";
	
	AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeoutInMs(5000).build());
    Future f = null;
	try {
		//f = c.prepareGet("http://www.ning.com/").execute(new AsyncCompletionHandler() {
		f = c.prepareGet(TARGET_URL).execute(new AsyncCompletionHandler<Response>() {

		   @Override
		   public Response onCompleted(Response response) throws IOException {
		        // Do something
			   ApplicationLauncher.logger.info("getStatus: onCompleted");
			   ApplicationLauncher.logger.info("getStatus: response:"+response.getStatusCode());
			   ApplicationLauncher.logger.info("getStatus: getResponseBody:"+response.getResponseBody());
			   ApplicationLauncher.logger.info("getStatus: getContentType:"+response.getContentType());
			   if(response.getStatusCode() == 200){
			   Gson gson = new Gson();
               String myResp = new String();
			   myResp=response.getResponseBody().toString();
               RestAPIResponse myCurrentAPIResponse = new RestAPIResponse();

               myCurrentAPIResponse = gson.fromJson(myResp,RestAPIResponse.class);
              // System.out.println("API-ResponseF3: myCurrentAPIResponse:"+myCurrentAPIResponse );
               String msgSuccess = new String();
               msgSuccess = "200";



               if (myCurrentAPIResponse.getStatusCode().equals(msgSuccess) ) {
                   System.out.println("API-ResponseF3-Success");
                   System.out.println("Resp:testid:"+myCurrentAPIResponse.getTestid());
                   if(myCurrentAPIResponse.getTeststatus().equals("started")){




                   }else if(myCurrentAPIResponse.getTeststatus().equals("stopped") || myCurrentAPIResponse.getTeststatus().equals("allcompleted")){
                       System.out.println("API-ResponseF3-Success:stopped or allcompleted");



                   }

               }
               else if (myCurrentAPIResponse.getStatusCode().equals(GlobalConstant.SLAVE_WAITING) ) {
                   //updatePollingFrequency(5);
            	   System.out.println("Resp:testid2:"+myCurrentAPIResponse.getTestid());
                   System.out.println("API-ResponseF3-polling400 set to 5");

               }
               else if (myCurrentAPIResponse.getStatusCode().equals(GlobalConstant.SLAVE_TIMEOUT) ) {
                  // EnableStopButton();
                  // updatePollingFrequency(5);
                   System.out.println("API-ResponseF3-polling401 set to 5");

               }
               else{
                   System.out.println("API-ResponseF3:else"+myCurrentAPIResponse.getStatusCode());

               }
               c.close();
		       return response;
			   }else{
				   System.out.println("API-ResponseF3-else");
				   c.close();
			       return response;
			   }
		   }



		   @Override
		   public void onThrowable(Throwable t) {
			   ApplicationLauncher.logger.info("getStatus: onThrowable");
			   c.close();
		   }
   });
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		ApplicationLauncher.logger.error("getStatus: IOException:" + e1.getMessage());
	}
   try {
	Response response =  (Response) f.get();
} catch (InterruptedException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} catch (ExecutionException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}

	AsyncHttpClient c = new AsyncHttpClient (new AsyncHttpClientConfig.Builder().setRequestTimeoutInMs(3000).build());
    Future<String> f;
	try {
		f = c.prepareGet(TARGET_URL).execute(new AsyncHandler<String>() {
		    private StringBuilder builder = new StringBuilder();


		    @Override
		    public String onCompleted() throws Exception {
		         // Will be invoked once the response has been fully read or a ResponseComplete exception
		         // has been thrown.
		         return builder.toString();
		    }

		    @Override
		    public void onThrowable(Throwable t) {
		    }
		});
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}


	}*/
/*	
	@SuppressWarnings("unchecked")
	public  void getPostMethod(){  
    ApplicationLauncher.logger.info("getPostMethod: Entry");

	String TARGET_URL = "http://192.168.1.100:8080/lstamperManualStop";
	
	AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeoutInMs(5000).build());
    Future f = null;
	try {
		Request req = new RequestBuilder("POST")
                .setUrl(TARGET_URL)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addParameter("testid", "1")
                .addParameter("meterconstant", "12")
                .addParameter("nooftestpulses", "12")
                .build();
		//param1.put("testid", Test_id);
        //param1.put("meterconstant",metreconstant);
        //param1.put("nooftestpulses",nooftestpulses);
        //System.out.println("nooftestpulses:"+nooftestpulses);
		//f = c.prepareGet("http://www.ning.com/").execute(new AsyncCompletionHandler() {
		f = c.prepareRequest(req).execute(new AsyncCompletionHandler<Response>() {

		   @Override
		   public Response onCompleted(Response response) throws IOException {
		        // Do something
			   ApplicationLauncher.logger.info("getPostMethod: onCompleted");
			   ApplicationLauncher.logger.info("getPostMethod: response:"+response.getStatusCode());
			   ApplicationLauncher.logger.info("getPostMethod: getResponseBody:"+response.getResponseBody());
			   ApplicationLauncher.logger.info("getPostMethod: getContentType:"+response.getContentType());
			   
			   Gson gson = new Gson();
               String myResp = new String();
			   myResp=response.getResponseBody().toString();
               RestAPIResponse myCurrentAPIResponse = new RestAPIResponse();

               myCurrentAPIResponse = gson.fromJson(myResp,RestAPIResponse.class);
              // System.out.println("API-ResponseF3: myCurrentAPIResponse:"+myCurrentAPIResponse );
               String msgSuccess = new String();
               msgSuccess = "200";



//               if (myCurrentAPIResponse.getStatusCode().equals(msgSuccess) ) {
//                   System.out.println("getPostMethod: API-ResponseF3-Success");
//                   System.out.println("getPostMethod: Resp:testid:"+myCurrentAPIResponse.getTestid());
//                   if(myCurrentAPIResponse.getTeststatus().equals("started")){
//
//
//
//
//                   }else if(myCurrentAPIResponse.getTeststatus().equals("stopped") || myCurrentAPIResponse.getTeststatus().equals("allcompleted")){
//                       System.out.println("getPostMethod: API-ResponseF3-Success:stopped or allcompleted");
//
//
//
//                   }
//
//               }
//               else if (myCurrentAPIResponse.getStatusCode().equals(GlobalConstant.SLAVE_WAITING) ) {
//                   //updatePollingFrequency(5);
//            	   System.out.println("getPostMethod: Resp:testid2:"+myCurrentAPIResponse.getTestid());
//                   System.out.println("getPostMethod: API-ResponseF3-polling400 set to 5");
//
//               }
//               else if (myCurrentAPIResponse.getStatusCode().equals(GlobalConstant.SLAVE_TIMEOUT) ) {
//                  // EnableStopButton();
//                  // updatePollingFrequency(5);
//                   System.out.println("getPostMethod: API-ResponseF3-polling401 set to 5");
//
//               }
//               else{
//                   System.out.println("getPostMethod:API-ResponseF3:else"+myCurrentAPIResponse.getStatusCode());
//
//               }
               c.close();
		       return response;
		   }



		   @Override
		   public void onThrowable(Throwable t) {
			   ApplicationLauncher.logger.info("getPostMethod: onThrowable");
			   c.close();
		   }
   });
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	}*/
	
}
