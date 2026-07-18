package com.tasnetwork.calibration.conveyor.AsyncHttpClient;

import java.io.IOException;
// import java.util.concurrent.ExecutionException;

// import org.apache.commons.lang3.StringUtils;
// import org.json.JSONException;
import org.json.simple.JSONObject;
//import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.ning.http.client.AsyncCompletionHandler;
// import com.ning.http.client.AsyncHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
// import com.ning.http.client.AsyncHttpClientConfig.Builder;
import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import com.tasnetwork.calibration.conveyor.ClusterServer;
import com.tasnetwork.calibration.conveyor.RestApiClusterResponse;
import com.tasnetwork.calibration.conveyor.RestApiJsonBodyResponse;
// import com.tasnetwork.calibration.conveyor.constant.ConstantProTamp;
// import com.tasnetwork.calibration.conveyor.database.MySQL_Controller;
// import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
// import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
// import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.setting.ServerSettingController;

// import javafx.application.Platform;
// import javafx.scene.control.Alert.AlertType;

import com.ning.http.client.Response;

public class AsyncConveyorClient {

	private volatile boolean responseReceived = false;
	// private static String responseData = "";

	RestApiClusterResponse restApiClusterResponseData = new RestApiClusterResponse();
	RestApiJsonBodyResponse restApiClusterResponseBodyData = new RestApiJsonBodyResponse();

	public void WaitForServerResponse(int WaitTimeInSec) {
		int SleepCounter = WaitTimeInSec;

		while ((!isResponseReceived()) && (SleepCounter > 0)) {
			Sleep(1000);
			SleepCounter--;

		}
	}

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep :InterruptedException:" + e.getMessage());
		}

	}

	public void setData(String deviceId, String bayId, String outputId, String outputValue) {

		ApplicationLauncher.logger.info("AsyncConveyorClient: setData: Entry");
		setResponseReceived(false);
		// setResponseData("");
		clearRestApiClusterResponseData();
		// String endPoint = "api/device?"+outputId+"="+outputValue;
		// String TARGET_URL = ServerSettingController.getRootUrl()
		// +"/"+endPoint;

		String endPoint = "/api/device/" + deviceId + "/bay/" + bayId + "/set";
		String TARGET_URL = ServerSettingController.getRootUrl() + endPoint;
		// ApplicationLauncher.logger.info("AsyncConveyorClient: TARGET_URL: " +
		// TARGET_URL);
		Request request = new RequestBuilder("GET")
				.setUrl(TARGET_URL)
				// .addQueryParameter(outputId, outputValue)
				.addQueryParam(outputId, outputValue)
				// .addParameter(outputId, outputValue)
				// .setBody("ISO-8859-1")
				// .addHeader("Content-Type", "application/x-www-form-urlencoded")
				.build();
		ApplicationLauncher.logger.info("AsyncConveyorClient: setData: request getUrl: " + request.getUrl());
		AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(5000).build());
		try {

			// c.prepareGet(TARGET_URL).execute(new AsyncCompletionHandler<Response>() {
			c.prepareRequest(request).execute(new AsyncCompletionHandler<Response>() {
				@Override
				public Response onCompleted(Response response) throws IOException {
					ApplicationLauncher.logger.info("AsyncConveyorClient: setData: onCompleted");
					setResponseReceived(true);
					if (response.getStatusCode() == 200) {
						Gson gson = new Gson();
						String myResp = new String();
						myResp = response.getResponseBody().toString();
						ApplicationLauncher.logger.debug("AsyncConveyorClient: setData : myResp:" + myResp);
						// setResponseData(myResp);
						RestApiClusterResponse myCurrentAPIResponse = new RestApiClusterResponse();
						myCurrentAPIResponse = gson.fromJson(myResp, RestApiClusterResponse.class);
						setRestApiClusterResponseData(myCurrentAPIResponse);
						/*
						 * RestAPIResponse myCurrentAPIResponse = new RestAPIResponse();
						 * 
						 * myCurrentAPIResponse = gson.fromJson(myResp,RestAPIResponse.class);
						 * String msgSuccess = new String();
						 * msgSuccess = "200";
						 * 
						 * 
						 * 
						 * if (myCurrentAPIResponse.getStatusCode().equals(msgSuccess) ) {
						 * ApplicationLauncher.logger.info("AsyncConveyorClient: API-Response-Success");
						 * //if(myCurrentAPIResponse.getDevice_name().equals(ModeConstant.
						 * DEVICE_NAME_LS_PROTAMP)) {
						 * if(
						 * StringUtils.indexOfAny(myCurrentAPIResponse.getDevice_name(),ConstantProTamp.
						 * DEVICE_NAME_LIST ) != -1){
						 * ApplicationLauncher.logger.info("AsyncConveyorClient: test190");
						 * ScanDeviceController.getValidateCredResponseSuccessTaskTrigger(
						 * myCurrentAPIResponse);
						 * 
						 * }
						 * // below else case Added new logic in 3 Phase ServerProperties
						 * }else if (myCurrentAPIResponse.getStatusCode().equals(ServerProperties.
						 * SERVER_CONNECTION_SLAVE_COMM_FAILED) ) {
						 * if(
						 * StringUtils.indexOfAny(myCurrentAPIResponse.getDevice_name(),ConstantProTamp.
						 * DEVICE_NAME_LIST ) != -1){
						 * 
						 * ApplicationLauncher.logger.info("AsyncConveyorClient: test192");
						 * ScanDeviceController.getValidateCredResponseSlaveCommFailedTaskTrigger(
						 * myCurrentAPIResponse);
						 * 
						 * 
						 * }
						 * 
						 * } else if (myCurrentAPIResponse.getStatusCode().equals(ServerProperties.
						 * SERVER_CONNECTION_SLAVE_WAITING) ){
						 * if(
						 * StringUtils.indexOfAny(myCurrentAPIResponse.getDevice_name(),ConstantProTamp.
						 * DEVICE_NAME_LIST ) != -1){
						 * 
						 * ApplicationLauncher.logger.info("AsyncConveyorClient: test191");
						 * ScanDeviceController.getValidateCredResponseBootingTaskTrigger(
						 * myCurrentAPIResponse);
						 * 
						 * }
						 * }
						 */

					} else {
						// ProjectExecutionController.updateServerStatus(ServerProperties.SERVER_CONNECTION_FAILED);
						ApplicationLauncher.logger.debug("AsyncConveyorClient: setData :Server failed");
					}
					c.close();
					return response;
				}

				@Override
				public void onThrowable(Throwable t) {
					ApplicationLauncher.logger.info("AsyncConveyorClient: setData: onThrowable:" + t.getMessage());

					// ScanDeviceController.getValidateCredResponseOnThrowTaskTrigger(t.getMessage());
					c.close();
				}
			});
		} catch (Exception e1) {

			e1.printStackTrace();
			ApplicationLauncher.logger.error("AsyncConveyorClient: setData : Exception:" + e1.getMessage());
		}

	}

	public void setBayData(ClusterServer clusterServer, String deviceId, String bayId, String outputId,
			String outputValue) {

		ApplicationLauncher.logger.info("AsyncConveyorClient: setBayData: Entry");
		setResponseReceived(false);
		// setResponseData("");
		clearRestApiClusterResponseData();
		// String endPoint = "api/device?"+outputId+"="+outputValue;
		// String TARGET_URL = ServerSettingController.getRootUrl()
		// +"/"+endPoint;

		String endPoint = "/api/device/" + deviceId + "/bay/" + bayId + "/set";
		String TARGET_URL = clusterServer.getRootUrl() + endPoint;
		// ApplicationLauncher.logger.info("AsyncConveyorClient: TARGET_URL: " +
		// TARGET_URL);
		Request request = new RequestBuilder("GET")
				.setUrl(TARGET_URL)
				// .addQueryParameter(outputId, outputValue)
				.addQueryParam(outputId, outputValue)
				// .addParameter(outputId, outputValue)
				// .setBody("ISO-8859-1")
				// .addHeader("Content-Type", "application/x-www-form-urlencoded")
				.build();
		ApplicationLauncher.logger.info("AsyncConveyorClient: setBayData: request getUrl: " + request.getUrl());
		AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(5000).build());
		try {

			// c.prepareGet(TARGET_URL).execute(new AsyncCompletionHandler<Response>() {
			c.prepareRequest(request).execute(new AsyncCompletionHandler<Response>() {
				@Override
				public Response onCompleted(Response response) throws IOException {
					ApplicationLauncher.logger.info("AsyncConveyorClient: setData: onCompleted");
					setResponseReceived(true);
					if (response.getStatusCode() == 200) {
						Gson gson = new Gson();
						String myResp = new String();
						myResp = response.getResponseBody().toString();

						ApplicationLauncher.logger.debug("AsyncConveyorClient: setData : myResp:" + myResp);
						// setResponseData(myResp);
						RestApiClusterResponse myCurrentAPIResponse = new RestApiClusterResponse();
						myCurrentAPIResponse = gson.fromJson(myResp, RestApiClusterResponse.class);
						setRestApiClusterResponseData(myCurrentAPIResponse);

						RestApiJsonBodyResponse myCurrentJsonApiResponse = new RestApiJsonBodyResponse();
						// myCurrentAPIResponse = gson.fromJson(myResp,RestApiClusterResponse.class);
						myCurrentJsonApiResponse.setStatusCode(String.valueOf(response.getStatusCode()));
						JSONParser parser = new JSONParser();
						try {
							JSONObject jsonBodyData = (JSONObject) parser.parse(myResp);
							myCurrentJsonApiResponse.setJsonBodyResponse(jsonBodyData);
						} catch (ParseException e) {

							e.printStackTrace();
							ApplicationLauncher.logger
									.error("AsyncConveyorClient: setData: ParseException : " + e.getMessage());
						}
						setRestApiClusterResponseBodyData(myCurrentJsonApiResponse);

					} else {
						// ProjectExecutionController.updateServerStatus(ServerProperties.SERVER_CONNECTION_FAILED);
						ApplicationLauncher.logger.debug("AsyncConveyorClient: setData :Server failed");
					}
					c.close();
					return response;
				}

				@Override
				public void onThrowable(Throwable t) {
					ApplicationLauncher.logger.info("AsyncConveyorClient: setData: onThrowable:" + t.getMessage());

					// ScanDeviceController.getValidateCredResponseOnThrowTaskTrigger(t.getMessage());
					c.close();
				}
			});
		} catch (Exception e1) {

			e1.printStackTrace();
			ApplicationLauncher.logger.error("AsyncConveyorClient: setData : Exception:" + e1.getMessage());
		}

	}
	// ============================================================================================

	/*
	 * public void setBayData( String deviceId, String bayId, String outputId,
	 * String outputValue) {
	 * 
	 * ApplicationLauncher.logger.info("AsyncConveyorClient: setData: Entry");
	 * setResponseReceived(false);
	 * setResponseData("");
	 * clearRestApiClusterResponseData();
	 * //String endPoint = "api/device?"+outputId+"="+outputValue;
	 * //String TARGET_URL = ServerSettingController.getRootUrl()
	 * // +"/"+endPoint;
	 * 
	 * String endPoint = "/api/device/" + deviceId + "/bay/" + bayId +"/set";
	 * String TARGET_URL = clusterServer.getRootUrl()+ endPoint;
	 * ApplicationLauncher.logger.info("AsyncConveyorClient: TARGET_URL: " +
	 * TARGET_URL);
	 * Request request = new RequestBuilder("GET")
	 * .setUrl(TARGET_URL)
	 * .addQueryParameter(outputId, outputValue)
	 * //.addParameter(outputId, outputValue)
	 * //.setBody("ISO-8859-1")
	 * //.addHeader("Content-Type", "application/x-www-form-urlencoded")
	 * .build();
	 * 
	 * AsyncHttpClient c = new AsyncHttpClient(new
	 * AsyncHttpClientConfig.Builder().setRequestTimeoutInMs(5000).build());
	 * * try {
	 * 
	 * //c.prepareGet(TARGET_URL).execute(new AsyncCompletionHandler<Response>()
	 * {
	 * f = c.prepareRequest(request).execute(new AsyncCompletionHandler<Response>()
	 * {
	 * 
	 * @Override
	 * public Response onCompleted(Response response) throws IOException {
	 * ApplicationLauncher.logger.info("AsyncConveyorClient: setData: onCompleted");
	 * setResponseReceived(true);
	 * if(response.getStatusCode() == 200){
	 * Gson gson = new Gson();
	 * String myResp = new String();
	 * myResp=response.getResponseBody().toString();
	 * 
	 * ApplicationLauncher.logger.debug("AsyncConveyorClient: setData : myResp:"
	 * +myResp);
	 * setResponseData(myResp);
	 * RestApiClusterResponse myCurrentAPIResponse = new RestApiClusterResponse();
	 * myCurrentAPIResponse = gson.fromJson(myResp,RestApiClusterResponse.class);
	 * setRestApiClusterResponseData(myCurrentAPIResponse);
	 * 
	 * 
	 * }else{
	 * //ProjectExecutionController.updateServerStatus(ServerProperties.
	 * SERVER_CONNECTION_FAILED);
	 * ApplicationLauncher.logger.
	 * debug("AsyncConveyorClient: setData :Server failed");
	 * }
	 * c.close();
	 * return response;
	 * }
	 * 
	 * 
	 * 
	 * @Override
	 * public void onThrowable(Throwable t) {
	 * ApplicationLauncher.logger.info("AsyncConveyorClient: setData: onThrowable:"
	 * +t.getMessage());
	 * 
	 * //ScanDeviceController.getValidateCredResponseOnThrowTaskTrigger(t.getMessage
	 * ());
	 * c.close();
	 * }
	 * });
	 * } catch (IOException e1) {
	 * 
	 * e1.printStackTrace();
	 * ApplicationLauncher.logger.
	 * error("AsyncConveyorClient: setData : IOException:" + e1.getMessage());
	 * }
	 * 
	 * }
	 */

	// ============================================================================================

	public void getBayData(ClusterServer clusterServer, String deviceId, String bayId, String inputPortId) {

		ApplicationLauncher.logger.info("AsyncConveyorClient: getBayData: Entry");
		ApplicationLauncher.logger.info("AsyncConveyorClient: inputPortId: " + inputPortId);
		setResponseReceived(false);
		// setResponseData("");
		clearRestApiClusterResponseData();
		// String endPoint = "api/device?"+outputId+"="+outputValue;
		// String TARGET_URL = ServerSettingController.getRootUrl()
		// +"/"+endPoint;

		String endPoint = "/api/device/" + deviceId + "/bay/" + bayId + "/get";
		String TARGET_URL = clusterServer.getRootUrl() + endPoint;
		// ApplicationLauncher.logger.debug("getBayData: TARGET_URL: " + TARGET_URL);
		Request request = new RequestBuilder("GET")
				.setUrl(TARGET_URL)
				// .addQueryParameter(inputPortId, "Status")
				.addQueryParam(inputPortId, "Status")
				// .addParameter(outputId, outputValue)
				// .setBody("ISO-8859-1")
				// .addHeader("Content-Type", "application/x-www-form-urlencoded")
				.build();
		ApplicationLauncher.logger.info("AsyncConveyorClient: getBayData: request getUrl: " + request.getUrl());
		AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(5000).build());
		try {

			// c.prepareGet(TARGET_URL).execute(new AsyncCompletionHandler<Response>() {
			c.prepareRequest(request).execute(new AsyncCompletionHandler<Response>() {
				@Override
				public Response onCompleted(Response response) throws IOException {
					ApplicationLauncher.logger.debug("getBayData:  onCompleted");
					setResponseReceived(true);
					if (response.getStatusCode() == 200) {
						new Gson();
						String myResp = new String();
						myResp = response.getResponseBody().toString();

						ApplicationLauncher.logger.debug("getBayData: getData : myResp:" + myResp);
						// setResponseData(myResp);
						RestApiJsonBodyResponse myCurrentAPIResponse = new RestApiJsonBodyResponse();
						// myCurrentAPIResponse = gson.fromJson(myResp,RestApiClusterResponse.class);
						myCurrentAPIResponse.setStatusCode(String.valueOf(response.getStatusCode()));
						ApplicationLauncher.logger
								.debug("getBayData: getStatusCode-1 :" + myCurrentAPIResponse.getStatusCode());
						// myCurrentAPIResponse.setStatusCode(String.valueOf(response.getStatus()));
						// ApplicationLauncher.logger.debug("getBayData: getStatusCode-1
						// :"+myCurrentAPIResponse.getStatus());
						JSONParser parser = new JSONParser();
						try {
							JSONObject jsonBodyData = (JSONObject) parser.parse(myResp);
							myCurrentAPIResponse.setJsonBodyResponse(jsonBodyData);
						} catch (ParseException e) {

							e.printStackTrace();
							ApplicationLauncher.logger.error("AsyncConveyorClient: ParseException : " + e.getMessage());
						}
						// myCurrentAPIResponse.setStatus(status);
						setRestApiClusterResponseBodyData(myCurrentAPIResponse);
						ApplicationLauncher.logger.debug("getBayData: getRestApiClusterResponseBodyData : "
								+ getRestApiClusterResponseBodyData().toString());
						ApplicationLauncher.logger
								.debug("getBayData: getStatusCode-2 :" + myCurrentAPIResponse.getStatusCode());
						ApplicationLauncher.logger.debug("getBayData: getStatus :" + myCurrentAPIResponse.getStatus());

					} else {
						// ProjectExecutionController.updateServerStatus(ServerProperties.SERVER_CONNECTION_FAILED);
						RestApiClusterResponse myCurrentAPIResponse = new RestApiClusterResponse();
						myCurrentAPIResponse.setStatuscode(String.valueOf(response.getStatusCode()));
						setRestApiClusterResponseData(myCurrentAPIResponse);
						ApplicationLauncher.logger.debug("getBayData: :Server failed");
					}
					c.close();
					return response;
				}

				@Override
				public void onThrowable(Throwable t) {
					ApplicationLauncher.logger
							.info("getBayData:  onThrowable:" + t.getMessage() + " : endPoint: " + request.getUrl());

					// ScanDeviceController.getValidateCredResponseOnThrowTaskTrigger(t.getMessage());
					c.close();
				}
			});
		} catch (Exception e1) {

			e1.printStackTrace();
			ApplicationLauncher.logger.error("getBayData: Exception:" + e1.getMessage());
		}

	}
	// ============================================================================================

	public boolean isResponseReceived() {
		return responseReceived;
	}

	/*
	 * public static String getResponseData() {
	 * return responseData;
	 * }
	 */

	public void setResponseReceived(boolean responseReceived) {
		this.responseReceived = responseReceived;
	}

	/*
	 * public static void setResponseData(String responseData) {
	 * AsyncConveyorClient.responseData = responseData;
	 * }
	 */

	public RestApiClusterResponse getRestApiClusterResponseData() {
		return restApiClusterResponseData;
	}

	public void setRestApiClusterResponseData(RestApiClusterResponse restApiClusterResponseData) {
		this.restApiClusterResponseData = restApiClusterResponseData;
	}

	public void clearRestApiClusterResponseData() {
		RestApiClusterResponse clearCurrentAPIResponseData = new RestApiClusterResponse();
		setRestApiClusterResponseData(clearCurrentAPIResponseData);
	}

	public RestApiJsonBodyResponse getRestApiClusterResponseBodyData() {
		return restApiClusterResponseBodyData;
	}

	public void setRestApiClusterResponseBodyData(RestApiJsonBodyResponse restApiClusterResponseBodyData) {
		this.restApiClusterResponseBodyData = restApiClusterResponseBodyData;
	}

}
