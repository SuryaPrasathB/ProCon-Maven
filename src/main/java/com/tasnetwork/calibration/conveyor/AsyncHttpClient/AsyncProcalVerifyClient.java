package com.tasnetwork.calibration.conveyor.AsyncHttpClient;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Future;

import javax.xml.ws.Endpoint;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.Response;
import com.tasnetwork.calibration.conveyor.ClusterServer;
import com.tasnetwork.calibration.conveyor.RestApiClusterResponse;
import com.tasnetwork.calibration.conveyor.RestApiJsonBodyResponse;
import com.tasnetwork.calibration.conveyor.remote.ProcalRemoteResponse;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.setting.ServerSettingController;

public class AsyncProcalVerifyClient {
	private volatile boolean responseReceived = false;
    private String responseData = "";
    
    //RestApiClusterResponse restApiClusterResponseData = new RestApiClusterResponse();
    
    ProcalRemoteResponse procalVerifyRemoteResponse = new ProcalRemoteResponse ();
    //RestApiJsonBodyResponse restApiClusterResponseBodyData = new RestApiJsonBodyResponse();
    
    
    public  void WaitForServerResponse(int WaitTimeInSec){
		int SleepCounter = WaitTimeInSec;

		while ((!isResponseReceived()) && (SleepCounter > 0)){
			Sleep(1000);
			SleepCounter --;

		}
	}
	
	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep :InterruptedException:"+ e.getMessage());
		}

	}

	
	//============================================================================================		 

/*	public void setBayData( String deviceId, String bayId, String outputId, String outputValue) {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.info("AsyncProcalVerifyClient: setData: Entry");
		setResponseReceived(false);
		setResponseData("");
		clearRestApiClusterResponseData();
		//String endPoint = "api/device?"+outputId+"="+outputValue;
    	//String TARGET_URL = ServerSettingController.getRootUrl()
    	//		+"/"+endPoint;
    	
    	String endPoint = "/api/device/" + deviceId + "/bay/" + bayId +"/set";
    	String TARGET_URL = clusterServer.getRootUrl()+ endPoint;
    	ApplicationLauncher.logger.info("AsyncProcalVerifyClient: TARGET_URL: " + TARGET_URL);
    	Request request = new RequestBuilder("GET")
				.setUrl(TARGET_URL)
				.addQueryParameter(outputId, outputValue)
				//.addParameter(outputId, outputValue)
				//.setBody("ISO-8859-1")
				//.addHeader("Content-Type", "application/x-www-form-urlencoded")
				.build();

    	AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeoutInMs(5000).build());
    	Future f = null;
    	try {

    		//f = c.prepareGet(TARGET_URL).execute(new AsyncCompletionHandler<Response>() {
    		f = c.prepareRequest(request).execute(new AsyncCompletionHandler<Response>() {
    			@Override
    			public Response onCompleted(Response response) throws IOException {
    				ApplicationLauncher.logger.info("AsyncProcalVerifyClient: setData: onCompleted");
    				setResponseReceived(true);
    				if(response.getStatusCode() == 200){
    					Gson gson = new Gson();
    					String myResp = new String();
    					myResp=response.getResponseBody().toString();
    					
    					ApplicationLauncher.logger.debug("AsyncProcalVerifyClient: setData : myResp:"+myResp);
    					setResponseData(myResp);
    					RestApiClusterResponse myCurrentAPIResponse = new RestApiClusterResponse();
    					myCurrentAPIResponse = gson.fromJson(myResp,RestApiClusterResponse.class);
    					setRestApiClusterResponseData(myCurrentAPIResponse);
    					

    				}else{
    					//ProjectExecutionController.updateServerStatus(ServerProperties.SERVER_CONNECTION_FAILED);
    					ApplicationLauncher.logger.debug("AsyncProcalVerifyClient: setData :Server failed");
    				}
    				c.close();
    				return response;
    			}



    			@Override
    			public void onThrowable(Throwable t) {
    				ApplicationLauncher.logger.info("AsyncProcalVerifyClient: setData: onThrowable:"+t.getMessage());
    				
    				//ScanDeviceController.getValidateCredResponseOnThrowTaskTrigger(t.getMessage());
    				c.close();
    			}
    		});
    	} catch (IOException e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    		ApplicationLauncher.logger.error("AsyncProcalVerifyClient: setData : IOException:" + e1.getMessage());
    	}

	}*/
	
	
	//============================================================================================		 

	
	
	
	public void sendCommand(ClusterServer clusterServer, 		String commandMessage) {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.info("AsyncProcalVerifyClient: sendCommand:: Entry");
		ApplicationLauncher.logger.info("AsyncProcalVerifyClient: commandMessage: " + commandMessage);
		setResponseReceived(false);
		setResponseData("");
		//clearRestApiClusterResponseData();
		//String endPoint = "api/device?"+outputId+"="+outputValue;
    	//String TARGET_URL = ServerSettingController.getRootUrl()
    	//		+"/"+endPoint;
    	
    	String endPoint = "/procal/verific/" + commandMessage;// + "/bay/" + bayId +"/get";
    	String TARGET_URL = clusterServer.getRootUrl()+ endPoint;
    	//ApplicationLauncher.logger.debug("sendCommand: TARGET_URL: " + TARGET_URL);
    	Request request = new RequestBuilder("GET")
				.setUrl(TARGET_URL)
				//.addQueryParameter(inputPortId, "Status")
				//.addParameter(outputId, outputValue)
				//.setBody("ISO-8859-1")
				//.addHeader("Content-Type", "application/x-www-form-urlencoded")
				.build();
    	ApplicationLauncher.logger.info("AsyncProcalVerifyClient: sendCommand: request getUrl: " + request.getUrl());
    	AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(5000).build());
    	Future f = null;
    	try {

    		//f = c.prepareGet(TARGET_URL).execute(new AsyncCompletionHandler<Response>() {
    		f = c.prepareRequest(request).execute(new AsyncCompletionHandler<Response>() {
    			@Override
    			public Response onCompleted(Response response) throws IOException {
    				ApplicationLauncher.logger.debug("sendCommand:  onCompleted");
    				setResponseReceived(true);
    				if(response.getStatusCode() == 200){
    					Gson gson = new Gson();
    					String myResp = new String();
    					myResp=response.getResponseBody().toString();
    					
    					ApplicationLauncher.logger.debug("sendCommand: getData : myResp:"+myResp);
    					setResponseData(myResp);
    					
    					//RestApiJsonBodyResponse myCurrentAPIResponse = new RestApiJsonBodyResponse();
    					//myCurrentAPIResponse = gson.fromJson(myResp,RestApiClusterResponse.class);
    					//myCurrentAPIResponse.setStatusCode(String.valueOf(response.getStatusCode()));
    					//ApplicationLauncher.logger.debug("sendCommand: getStatusCode-1 :"+myCurrentAPIResponse.getStatusCode());
    					//myCurrentAPIResponse.setStatusCode(String.valueOf(response.getStatus()));
    					//ApplicationLauncher.logger.debug("sendCommand: getStatusCode-1 :"+myCurrentAPIResponse.getStatus());
    					JSONParser parser = new JSONParser();
    					try {
							JSONObject jsonBodyData = (JSONObject) parser.parse(myResp);
							
							ApplicationLauncher.logger.debug("sendCommand: response : jsonBodyData :"+jsonBodyData);
							ProcalRemoteResponse myProcalRemoteResponse = gson.fromJson(jsonBodyData.toJSONString(), ProcalRemoteResponse.class);
							
							
							
							ApplicationLauncher.logger.debug("sendCommand: myProcalRemoteResponse: " + myProcalRemoteResponse.getMessage());
							setProcalVerifyRemoteResponse(myProcalRemoteResponse);
							//myCurrentAPIResponse.setJsonBodyResponse(jsonBodyData);
							//myProcalRemoteResponse
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							ApplicationLauncher.logger.error("AsyncProcalVerifyClient: ParseException : "+e.getMessage());
						}
    					//myCurrentAPIResponse.setStatus(status);
    					//setRestApiClusterResponseBodyData(myCurrentAPIResponse);
    					//ApplicationLauncher.logger.debug("sendCommand: getRestApiClusterResponseBodyData : "+getRestApiClusterResponseBodyData().toString());
    					//ApplicationLauncher.logger.debug("sendCommand: getStatusCode-2 :"+myCurrentAPIResponse.getStatusCode());
    					//ApplicationLauncher.logger.debug("sendCommand: getStatus :"+myCurrentAPIResponse.getStatus());
    					

    				}else{
    					//ProjectExecutionController.updateServerStatus(ServerProperties.SERVER_CONNECTION_FAILED);
    					//RestApiClusterResponse myCurrentAPIResponse = new RestApiClusterResponse();
    					//myCurrentAPIResponse.setStatuscode(String.valueOf(response.getStatusCode()));
    					//setRestApiClusterResponseData(myCurrentAPIResponse);
    					ApplicationLauncher.logger.debug("sendCommand: :Server failed");
    				}
    				//c.close();
    				new Thread(() -> {
	                    try {
	                        c.close();
	                    } catch (Exception e) {
	                        ApplicationLauncher.logger.error("sendCommand : Error closing client: " + e.getMessage());
	                    }
	                }).start();
    				return response;
    			}



    			@Override
    			public void onThrowable(Throwable t) {
    				ApplicationLauncher.logger.info("sendCommand:  onThrowable:"+t.getMessage() + " : endPoint: " + request.getUrl());
    				
    				//ScanDeviceController.getValidateCredResponseOnThrowTaskTrigger(t.getMessage());
    				//c.close();
    				new Thread(() -> {
	                    try {
	                        c.close();
	                    } catch (Exception e) {
	                        ApplicationLauncher.logger.error("sendCommand: Error closing client in onThrowable: " + e.getMessage());
	                    }
	                }).start();
    				
    			}
    		});
    	} catch (Exception e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    		ApplicationLauncher.logger.error("sendCommand: Exception:" + e1.getMessage());
    	}

	}
	//============================================================================================		
	
		public void sendCommandServer(ClusterServer clusterServer, String commandMessage, String endPoint) {
			// TODO Auto-generated method stub
			ApplicationLauncher.logger.info("AsyncProcalVerifyClient: sendCommand:: Entry");
			ApplicationLauncher.logger.info("AsyncProcalVerifyClient: commandMessage: " + commandMessage);
			setResponseReceived(false);
			setResponseData("");
			//clearRestApiClusterResponseData();
			//String endPoint = "api/device?"+outputId+"="+outputValue;
	    	//String TARGET_URL = ServerSettingController.getRootUrl()
	    	//		+"/"+endPoint;
	    	
	    	String tailPoint = "/procal/" + endPoint + "/" + commandMessage;// + "/bay/" + bayId +"/get";
	    	String TARGET_URL = clusterServer.getRootUrl()+ tailPoint;
	    	//ApplicationLauncher.logger.debug("sendCommand: TARGET_URL: " + TARGET_URL);
	    	Request request = new RequestBuilder("GET")
					.setUrl(TARGET_URL)
					//.addQueryParameter(inputPortId, "Status")
					//.addParameter(outputId, outputValue)
					//.setBody("ISO-8859-1")
					//.addHeader("Content-Type", "application/x-www-form-urlencoded")
					.build();
	    	ApplicationLauncher.logger.info("AsyncProcalVerifyClient: sendCommand: request getUrl: " + request.getUrl());
	    	AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(5000).build());
	    	Future f = null;
	    	try {

	    		//f = c.prepareGet(TARGET_URL).execute(new AsyncCompletionHandler<Response>() {
	    		f = c.prepareRequest(request).execute(new AsyncCompletionHandler<Response>() {
	    			@Override
	    			public Response onCompleted(Response response) throws IOException {
	    				ApplicationLauncher.logger.debug("sendCommand:  onCompleted");
	    				setResponseReceived(true);
	    				ApplicationLauncher.logger.debug("sendCommand: getData : response.getStatusCode():"+response.getStatusCode());
	    				if(response.getStatusCode() == 200){
	    					
	    					Gson gson = new Gson();
	    					String myResp = new String();
	    					myResp=response.getResponseBody().toString();
	    					
	    					ApplicationLauncher.logger.debug("sendCommand: getData : myResp:"+myResp);
	    					setResponseData(myResp);
	    					
	    					//RestApiJsonBodyResponse myCurrentAPIResponse = new RestApiJsonBodyResponse();
	    					//myCurrentAPIResponse = gson.fromJson(myResp,RestApiClusterResponse.class);
	    					//myCurrentAPIResponse.setStatusCode(String.valueOf(response.getStatusCode()));
	    					//ApplicationLauncher.logger.debug("sendCommand: getStatusCode-1 :"+myCurrentAPIResponse.getStatusCode());
	    					//myCurrentAPIResponse.setStatusCode(String.valueOf(response.getStatus()));
	    					//ApplicationLauncher.logger.debug("sendCommand: getStatusCode-1 :"+myCurrentAPIResponse.getStatus());
	    					JSONParser parser = new JSONParser();
	    					try {
								JSONObject jsonBodyData = (JSONObject) parser.parse(myResp);
								
								ApplicationLauncher.logger.debug("sendCommand: response : jsonBodyData :"+jsonBodyData);
								ProcalRemoteResponse myProcalRemoteResponse = gson.fromJson(jsonBodyData.toJSONString(), ProcalRemoteResponse.class);
								
								
								
								ApplicationLauncher.logger.debug("sendCommand: myProcalRemoteResponse: " + myProcalRemoteResponse.getMessage());
								setProcalVerifyRemoteResponse(myProcalRemoteResponse);
								//myCurrentAPIResponse.setJsonBodyResponse(jsonBodyData);
								//myProcalRemoteResponse
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								ApplicationLauncher.logger.error("AsyncProcalVerifyClient: ParseException : "+e.getMessage());
							}
	    					//myCurrentAPIResponse.setStatus(status);
	    					//setRestApiClusterResponseBodyData(myCurrentAPIResponse);
	    					//ApplicationLauncher.logger.debug("sendCommand: getRestApiClusterResponseBodyData : "+getRestApiClusterResponseBodyData().toString());
	    					//ApplicationLauncher.logger.debug("sendCommand: getStatusCode-2 :"+myCurrentAPIResponse.getStatusCode());
	    					//ApplicationLauncher.logger.debug("sendCommand: getStatus :"+myCurrentAPIResponse.getStatus());
	    					

	    				}else{
	    					//ProjectExecutionController.updateServerStatus(ServerProperties.SERVER_CONNECTION_FAILED);
	    					//RestApiClusterResponse myCurrentAPIResponse = new RestApiClusterResponse();
	    					//myCurrentAPIResponse.setStatuscode(String.valueOf(response.getStatusCode()));
	    					//setRestApiClusterResponseData(myCurrentAPIResponse);
	    					ApplicationLauncher.logger.debug("sendCommand: :Server failed");
	    				}
	    				//c.close();
	    				new Thread(() -> {
		                    try {
		                        c.close();
		                    } catch (Exception e) {
		                        ApplicationLauncher.logger.error("sendCommand : Error closing client: " + e.getMessage());
		                    }
		                }).start();
	    				return response;
	    			}



	    			@Override
	    			public void onThrowable(Throwable t) {
	    				ApplicationLauncher.logger.info("sendCommand:  onThrowable:"+t.getMessage() + " : endPoint: " + request.getUrl());
	    				
	    				//ScanDeviceController.getValidateCredResponseOnThrowTaskTrigger(t.getMessage());
	    				//c.close();
	    				new Thread(() -> {
		                    try {
		                        c.close();
		                    } catch (Exception e) {
		                        ApplicationLauncher.logger.error("sendCommand: Error closing client in onThrowable: " + e.getMessage());
		                    }
		                }).start();
	    			}
	    		});
	    	} catch (Exception e1) {
	    		// TODO Auto-generated catch block
	    		e1.printStackTrace();
	    		ApplicationLauncher.logger.error("sendCommand: Exception:" + e1.getMessage());
	    	}

		}
		//============================================================================================
		
		
		public void sendPostCommandServer(ClusterServer clusterServer, String endPoint, String commandMessage, Map<Integer, String> requestBodyMap) {
		    ApplicationLauncher.logger.info("AsyncProcalVerifyClient: sendPostCommandServer:: Entry");

		    String tailPoint = "/procal/" + endPoint + "/" + commandMessage;
		    String TARGET_URL = clusterServer.getRootUrl() + tailPoint;

		    ApplicationLauncher.logger.info("sendPostCommandServer: TARGET_URL = " + TARGET_URL);
		    setResponseReceived(false);
		    setResponseData("");

		    Gson gson = new Gson();
		    String jsonRequest = gson.toJson(requestBodyMap); // convert Map to JSON string

		    Request request = new RequestBuilder("POST")
		        .setUrl(TARGET_URL)
		        .setHeader("Content-Type", "application/json")
		        .setBody(jsonRequest)
		        .build();

		    AsyncHttpClient client = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(5000).build());

		    try {
		        client.prepareRequest(request).execute(new AsyncCompletionHandler<Response>() {
		            @Override
		            public Response onCompleted(Response response) throws IOException {
		                ApplicationLauncher.logger.debug("sendPostCommandServer: onCompleted with status: " + response.getStatusCode());
		                if (response.getStatusCode() == 200) {
		                    setResponseReceived(true);
		                    String body = response.getResponseBody();
		                    setResponseData(body);

		                    JSONObject jsonResponse;
							try {
								jsonResponse = (JSONObject) new JSONParser().parse(body);
								ProcalRemoteResponse myProcalRemoteResponse = gson.fromJson(jsonResponse.toJSONString(), ProcalRemoteResponse.class);
			                    setProcalVerifyRemoteResponse(myProcalRemoteResponse);
			                    ApplicationLauncher.logger.debug("sendPostCommandServer: Response Message: " + myProcalRemoteResponse.getMessage());
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								ApplicationLauncher.logger.error("sendPostCommandServer: : ParseException " + e.getMessage());
							}
		                    

		                    
		                }
		                //client.close();
		                new Thread(() -> {
		                    try {
		                        client.close();
		                    } catch (Exception e) {
		                        ApplicationLauncher.logger.error("sendPostCommandServer : Error closing client: " + e.getMessage());
		                    }
		                }).start();
		                return response;
		            }

		            @Override
		            public void onThrowable(Throwable t) {
		                ApplicationLauncher.logger.error("sendPostCommandServer: onThrowable: " + t.getMessage());
		                //client.close();
		                new Thread(() -> {
		                    try {
		                        client.close();
		                    } catch (Exception e) {
		                        ApplicationLauncher.logger.error("sendPostCommandServer: Error closing client in onThrowable: " + e.getMessage());
		                    }
		                }).start();
		            }
		        });
		    } catch (Exception e) {
		        e.printStackTrace();
		        ApplicationLauncher.logger.error("sendPostCommandServer: Exception: " + e.getMessage());
		    }
		}
		
		
		public void sendPostCommandServerV2(ClusterServer clusterServer, String endPoint, String commandMessage, Map<Integer, Map<String, String>> requestBodyMap) {
		    ApplicationLauncher.logger.info("AsyncProcalVerifyClient: sendPostCommandServerV2:: Entry");

		    String tailPoint = "/procal/" + endPoint + "/" + commandMessage;
		    String TARGET_URL = clusterServer.getRootUrl() + tailPoint;

		    ApplicationLauncher.logger.info("sendPostCommandServerV2: TARGET_URL = " + TARGET_URL);
		    setResponseReceived(false);
		    setResponseData("");

		    Gson gson = new Gson();
		    String jsonRequest = gson.toJson(requestBodyMap); // convert Map to JSON string

		    Request request = new RequestBuilder("POST")
		        .setUrl(TARGET_URL)
		        .setHeader("Content-Type", "application/json")
		        .setBody(jsonRequest)
		        .build();

		    AsyncHttpClient client = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(5000).build());

		    try {
		        client.prepareRequest(request).execute(new AsyncCompletionHandler<Response>() {
		            @Override
		            public Response onCompleted(Response response) throws IOException {
		                ApplicationLauncher.logger.debug("sendPostCommandServerV2: onCompleted with status: " + response.getStatusCode());
		                if (response.getStatusCode() == 200) {
		                    setResponseReceived(true);
		                    String body = response.getResponseBody();
		                    setResponseData(body);

		                    JSONObject jsonResponse;
							try {
								jsonResponse = (JSONObject) new JSONParser().parse(body);
								ProcalRemoteResponse myProcalRemoteResponse = gson.fromJson(jsonResponse.toJSONString(), ProcalRemoteResponse.class);
			                    setProcalVerifyRemoteResponse(myProcalRemoteResponse);
			                    ApplicationLauncher.logger.debug("sendPostCommandServerV2: Response Message: " + myProcalRemoteResponse.getMessage());
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								ApplicationLauncher.logger.error("sendPostCommandServerV2: : ParseException " + e.getMessage());
							}
		                    

		                    
		                }
		                //client.close();
		                new Thread(() -> {
		                    try {
		                        client.close();
		                    } catch (Exception e) {
		                        ApplicationLauncher.logger.error("sendPostCommandServerV2: Error closing client: " + e.getMessage());
		                    }
		                }).start();
		                return response;
		            }

		            @Override
		            public void onThrowable(Throwable t) {
		                ApplicationLauncher.logger.error("sendPostCommandServerV2: onThrowable: " + t.getMessage());
		                //client.close();
		                new Thread(() -> {
		                    try {
		                        client.close();
		                    } catch (Exception e) {
		                        ApplicationLauncher.logger.error("sendPostCommandServerV2: Error closing client in onThrowable: " + e.getMessage());
		                    }
		                }).start();
		            }
		        });
		    } catch (Exception e) {
		        e.printStackTrace();
		        ApplicationLauncher.logger.error("sendPostCommandServerV2: Exception: " + e.getMessage());
		    }
		}
	
	public void sendCommandWithParamQuery(ClusterServer clusterServer, String endPoint, String commandMessage, String paramKey, String paramValue) {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.info("AsyncProcalVerifyClient: sendCommand:: Entry");
		ApplicationLauncher.logger.info("AsyncProcalVerifyClient: commandMessage: " + commandMessage);
		ApplicationLauncher.logger.info("AsyncProcalVerifyClient: paramKey: " + paramKey);
		ApplicationLauncher.logger.info("AsyncProcalVerifyClient: paramValue: " + paramValue);
		
		setResponseReceived(false);
		setResponseData("");
		//clearRestApiClusterResponseData();
		//String endPoint = "api/device?"+outputId+"="+outputValue;
    	//String TARGET_URL = ServerSettingController.getRootUrl()
    	//		+"/"+endPoint;
    	
    	String tailPoint = "/procal/" + endPoint + "/" + commandMessage;// + "/bay/" + bayId +"/get";
    	String TARGET_URL = clusterServer.getRootUrl()+ tailPoint;
    	//ApplicationLauncher.logger.debug("sendCommand: TARGET_URL: " + TARGET_URL);
    	Request request = new RequestBuilder("GET")
				.setUrl(TARGET_URL)
				//.addQueryParameter(paramKey, paramValue)
				.addQueryParam(paramKey, paramValue)
				//.addQueryParameter(inputPortId, "Status")
				//.addParameter(outputId, outputValue)
				//.setBody("ISO-8859-1")
				//.addHeader("Content-Type", "application/x-www-form-urlencoded")
				.build();
    	ApplicationLauncher.logger.info("AsyncProcalVerifyClient: sendCommand: request getUrl: " + request.getUrl());
    	AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(5000).build());
    	Future f = null;
    	try {

    		//f = c.prepareGet(TARGET_URL).execute(new AsyncCompletionHandler<Response>() {
    		f = c.prepareRequest(request).execute(new AsyncCompletionHandler<Response>() {
    			@Override
    			public Response onCompleted(Response response) throws IOException {
    				ApplicationLauncher.logger.debug("sendCommand:  onCompleted");
    				setResponseReceived(true);
    				if(response.getStatusCode() == 200){
    					Gson gson = new Gson();
    					String myResp = new String();
    					myResp=response.getResponseBody().toString();
    					
    					ApplicationLauncher.logger.debug("sendCommand: getData : myResp:"+myResp);
    					setResponseData(myResp);
    					
    					//RestApiJsonBodyResponse myCurrentAPIResponse = new RestApiJsonBodyResponse();
    					//myCurrentAPIResponse = gson.fromJson(myResp,RestApiClusterResponse.class);
    					//myCurrentAPIResponse.setStatusCode(String.valueOf(response.getStatusCode()));
    					//ApplicationLauncher.logger.debug("sendCommand: getStatusCode-1 :"+myCurrentAPIResponse.getStatusCode());
    					//myCurrentAPIResponse.setStatusCode(String.valueOf(response.getStatus()));
    					//ApplicationLauncher.logger.debug("sendCommand: getStatusCode-1 :"+myCurrentAPIResponse.getStatus());
    					JSONParser parser = new JSONParser();
    					try {
							JSONObject jsonBodyData = (JSONObject) parser.parse(myResp);
							
							ApplicationLauncher.logger.debug("sendCommand: response : jsonBodyData :"+jsonBodyData);
							ProcalRemoteResponse myProcalRemoteResponse = gson.fromJson(jsonBodyData.toJSONString(), ProcalRemoteResponse.class);
							
							
							
							ApplicationLauncher.logger.debug("sendCommand: myProcalRemoteResponse: " + myProcalRemoteResponse.getMessage());
							setProcalVerifyRemoteResponse(myProcalRemoteResponse);
							//myCurrentAPIResponse.setJsonBodyResponse(jsonBodyData);
							//myProcalRemoteResponse
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							ApplicationLauncher.logger.error("AsyncProcalVerifyClient: ParseException : "+e.getMessage());
						}
    					//myCurrentAPIResponse.setStatus(status);
    					//setRestApiClusterResponseBodyData(myCurrentAPIResponse);
    					//ApplicationLauncher.logger.debug("sendCommand: getRestApiClusterResponseBodyData : "+getRestApiClusterResponseBodyData().toString());
    					//ApplicationLauncher.logger.debug("sendCommand: getStatusCode-2 :"+myCurrentAPIResponse.getStatusCode());
    					//ApplicationLauncher.logger.debug("sendCommand: getStatus :"+myCurrentAPIResponse.getStatus());
    					

    				}else{
    					//ProjectExecutionController.updateServerStatus(ServerProperties.SERVER_CONNECTION_FAILED);
    					//RestApiClusterResponse myCurrentAPIResponse = new RestApiClusterResponse();
    					//myCurrentAPIResponse.setStatuscode(String.valueOf(response.getStatusCode()));
    					//setRestApiClusterResponseData(myCurrentAPIResponse);
    					ApplicationLauncher.logger.debug("sendCommand: :Server failed");
    				}
    				//c.close();
    				new Thread(() -> {
    				    try {
    				        c.close();
    				    } catch (Exception e) {
    				        ApplicationLauncher.logger.error("Error closing client: " + e.getMessage());
    				    }
    				}).start();
    				return response;
    			}



    			@Override
    			public void onThrowable(Throwable t) {
    				ApplicationLauncher.logger.info("sendCommand:  onThrowable:"+t.getMessage() + " : endPoint: " + request.getUrl());
    				
    				//ScanDeviceController.getValidateCredResponseOnThrowTaskTrigger(t.getMessage());
    				//c.close();
    				new Thread(() -> {
    				    try {
    				        c.close();
    				    } catch (Exception e) {
    				        ApplicationLauncher.logger.error("Error closing client in onThrowable: " + e.getMessage());
    				    }
    				}).start();
    			}
    		});
    	} catch (Exception e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    		ApplicationLauncher.logger.error("sendCommand: Exception:" + e1.getMessage());
    	}

	}
	
	//============================================================================================	

	public boolean isResponseReceived() {
		return responseReceived;
	}

	public  String getResponseData() {
		return responseData;
	}

	public void setResponseReceived(boolean responseReceived) {
		this.responseReceived = responseReceived;
	}

	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}

/*	public RestApiClusterResponse getRestApiClusterResponseData() {
		return restApiClusterResponseData;
	}

	public void setRestApiClusterResponseData(RestApiClusterResponse restApiClusterResponseData) {
		this.restApiClusterResponseData = restApiClusterResponseData;
	}
	public void clearRestApiClusterResponseData() {
		RestApiClusterResponse clearCurrentAPIResponseData = new RestApiClusterResponse();
		setRestApiClusterResponseData(clearCurrentAPIResponseData);
	}*/

/*	public RestApiJsonBodyResponse getRestApiClusterResponseBodyData() {
		return restApiClusterResponseBodyData;
	}

	public void setRestApiClusterResponseBodyData(RestApiJsonBodyResponse restApiClusterResponseBodyData) {
		this.restApiClusterResponseBodyData = restApiClusterResponseBodyData;
	}*/

	public ProcalRemoteResponse getProcalVerifyRemoteResponse() {
		return procalVerifyRemoteResponse;
	}

	public void setProcalVerifyRemoteResponse(ProcalRemoteResponse procalVerifyRemoteResponse) {
		this.procalVerifyRemoteResponse = procalVerifyRemoteResponse;
	}




	
}
