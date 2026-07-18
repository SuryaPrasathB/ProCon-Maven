package com.tasnetwork.calibration.conveyor.restClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
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
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

public class RestClient {

	private static final int TIMEOUT_MS = 5000;

	RequestConfig requestConfig = RequestConfig.custom()
			.setConnectTimeout(TIMEOUT_MS)
			.setSocketTimeout(TIMEOUT_MS)
			.build();

	CloseableHttpClient httpClient = HttpClients.custom()
			.setDefaultRequestConfig(requestConfig)
			.setMaxConnPerRoute(10) // Adjust as needed
			.setMaxConnTotal(20) // Adjust as needed
			.build();

	private volatile boolean responseReceived = false;
	RestApiClusterResponse restApiClusterResponseData = new RestApiClusterResponse();
	RestApiJsonBodyResponse restApiClusterResponseBodyData = new RestApiJsonBodyResponse();

	public void WaitForServerResponse(int WaitTimeInSec) {
		int SleepCounter = WaitTimeInSec;

		while ((!isResponseReceived()) && (SleepCounter > 0)) {
			Sleep(1000);
			SleepCounter--;

		}
	}

	public void setBayData(ClusterServer clusterServer, String deviceId, String bayId, String outputId,
			String outputValue) {

		ApplicationLauncher.logger.info("RestClient: setBayData: Entry : " + outputId);
		setResponseReceived(false);
		// setResponseData("");
		clearRestApiClusterResponseData();
		// String endPoint = "api/device?"+outputId+"="+outputValue;
		// String TARGET_URL = ServerSettingController.getRootUrl()
		// +"/"+endPoint;

		String endPoint = "/api/device/" + deviceId + "/bay/" + bayId + "/set";
		String TARGET_URL = clusterServer.getRootUrl() + endPoint;
		ApplicationLauncher.logger.info("RestClient: setBayData: TARGET_URL: " + TARGET_URL);

		try {
			String requestUrl = String.format("%s?%s=%s",
					TARGET_URL, outputId, outputValue);
			HttpGet request = new HttpGet(requestUrl);
			request.addHeader("Connection", "keep-alive");

			ApplicationLauncher.logger.info("RestClient: setBayData: requestUrl: " + requestUrl);

			try (CloseableHttpResponse response = httpClient.execute(request)) {
				String responseBody = EntityUtils.toString(response.getEntity());
				setResponseReceived(true);
				ApplicationLauncher.logger
						.info("RestClient: setBayData: responseBody: " + responseBody + " : " + outputId);

				Gson gson = new Gson();
				RestApiClusterResponse myCurrentAPIResponse = new RestApiClusterResponse();
				myCurrentAPIResponse = gson.fromJson(responseBody, RestApiClusterResponse.class);
				setRestApiClusterResponseData(myCurrentAPIResponse);

				RestApiJsonBodyResponse myCurrentJsonApiResponse = new RestApiJsonBodyResponse();
				// myCurrentAPIResponse = gson.fromJson(myResp,RestApiClusterResponse.class);
				myCurrentJsonApiResponse.setStatusCode("200");
				JSONParser parser = new JSONParser();
				try {
					JSONObject jsonBodyData = (JSONObject) parser.parse(responseBody);
					myCurrentJsonApiResponse.setJsonBodyResponse(jsonBodyData);
				} catch (ParseException e) {

					e.printStackTrace();
					ApplicationLauncher.logger.error("RestClient: setBayData:  ParseException : " + e.getMessage());
				}
				setRestApiClusterResponseBodyData(myCurrentJsonApiResponse);
				/*
				 * System.out.
				 * printf("Counter: %d -> Response: %s, Positive: %d (%.2f%%), Negative: %d (%.2f%%)%n"
				 * ,
				 * i, responseBody, positiveCount,
				 * (positiveCount / (double) i) * 100, negativeCount,
				 * (negativeCount / (double) i) * 100);
				 */
			}

		} catch (IOException e) {
			// negativeCount++;
			// System.err.println("Request failed: " + e.getMessage());
			ApplicationLauncher.logger
					.info("RestClient: setBayData : response failed : " + e.getMessage() + " : " + outputId);
		}
		/*
		 * Request request = new RequestBuilder("GET")
		 * .setUrl(TARGET_URL)
		 * .addQueryParameter(outputId, outputValue)
		 * //.addParameter(outputId, outputValue)
		 * //.setBody("ISO-8859-1")
		 * //.addHeader("Content-Type", "application/x-www-form-urlencoded")
		 * .build();
		 * ApplicationLauncher.logger.info("RestClient: setBayData: request getUrl: " +
		 * request.getUrl());
		 * AsyncHttpClient c = new AsyncHttpClient(new
		 * AsyncHttpClientConfig.Builder().setRequestTimeoutInMs(5000).build());
		 * Future f = null;
		 * 
		 * try {
		 * 
		 * //f = c.prepareGet(TARGET_URL).execute(new AsyncCompletionHandler<Response>()
		 * {
		 * f = c.prepareRequest(request).execute(new AsyncCompletionHandler<Response>()
		 * {
		 * 
		 * @Override
		 * public Response onCompleted(Response response) throws IOException {
		 * ApplicationLauncher.logger.info("RestClient: setData: onCompleted");
		 * setResponseReceived(true);
		 * if(response.getStatusCode() == 200){
		 * Gson gson = new Gson();
		 * String myResp = new String();
		 * myResp=response.getResponseBody().toString();
		 * 
		 * ApplicationLauncher.logger.debug("RestClient: setData : myResp:"+myResp);
		 * //setResponseData(myResp);
		 * RestApiClusterResponse myCurrentAPIResponse = new RestApiClusterResponse();
		 * myCurrentAPIResponse = gson.fromJson(myResp,RestApiClusterResponse.class);
		 * setRestApiClusterResponseData(myCurrentAPIResponse);
		 * 
		 * RestApiJsonBodyResponse myCurrentJsonApiResponse = new
		 * RestApiJsonBodyResponse();
		 * //myCurrentAPIResponse = gson.fromJson(myResp,RestApiClusterResponse.class);
		 * myCurrentJsonApiResponse.setStatusCode(String.valueOf(response.getStatusCode(
		 * )));
		 * JSONParser parser = new JSONParser();
		 * try {
		 * JSONObject jsonBodyData = (JSONObject) parser.parse(myResp);
		 * myCurrentJsonApiResponse.setJsonBodyResponse(jsonBodyData);
		 * } catch (ParseException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.error("RestClient: setData: ParseException : "+e.
		 * getMessage());
		 * }
		 * setRestApiClusterResponseBodyData(myCurrentJsonApiResponse);
		 * 
		 * 
		 * 
		 * }else{
		 * //ProjectExecutionController.updateServerStatus(ServerProperties.
		 * SERVER_CONNECTION_FAILED);
		 * ApplicationLauncher.logger.debug("RestClient: setData :Server failed");
		 * }
		 * c.close();
		 * return response;
		 * }
		 * 
		 * 
		 * 
		 * @Override
		 * public void onThrowable(Throwable t) {
		 * ApplicationLauncher.logger.info("RestClient: setData: onThrowable:"+t.
		 * getMessage());
		 * 
		 * //ScanDeviceController.getValidateCredResponseOnThrowTaskTrigger(t.getMessage
		 * ());
		 * c.close();
		 * }
		 * });
		 * } catch (IOException e1) {
		 * 
		 * e1.printStackTrace();
		 * ApplicationLauncher.logger.error("RestClient: setData : IOException:" +
		 * e1.getMessage());
		 * }
		 */
		ApplicationLauncher.logger.info("RestClient: setBayData: Exit : " + outputId);
	}

	public void getBayData(ClusterServer clusterServer, String deviceId, String bayId, String inputPortId) {
		ApplicationLauncher.logger.info("RestClient: getBayData: Entry");
		ApplicationLauncher.logger.info("RestClient: inputPortId: " + inputPortId);
		setResponseReceived(false);
		clearRestApiClusterResponseData();

		String endPoint = "/api/device/" + deviceId + "/bay/" + bayId + "/get";
		String TARGET_URL = clusterServer.getRootUrl() + endPoint;

		ApplicationLauncher.logger.info("RestClient: getBayData: TARGET_URL: " + TARGET_URL);

		try {
			String requestUrl = String.format("%s?%s=%s", TARGET_URL, inputPortId, "Status");
			HttpGet request = new HttpGet(requestUrl);
			request.addHeader("Connection", "keep-alive");

			ApplicationLauncher.logger.info("RestClient: getBayData: requestUrl: " + requestUrl);

			try (CloseableHttpResponse response = httpClient.execute(request)) {
				String responseBody = EntityUtils.toString(response.getEntity());
				ApplicationLauncher.logger.info("RestClient: responseBody: " + responseBody + " : " + inputPortId);
				setResponseReceived(true);
				RestApiJsonBodyResponse myCurrentAPIResponse = new RestApiJsonBodyResponse();
				// myCurrentAPIResponse = gson.fromJson(myResp,RestApiClusterResponse.class);
				myCurrentAPIResponse.setStatusCode("200");
				// ApplicationLauncher.logger.debug("getBayData: getStatusCode-1
				// :"+myCurrentAPIResponse.getStatusCode());
				// myCurrentAPIResponse.setStatusCode(String.valueOf(response.getStatus()));
				// ApplicationLauncher.logger.debug("getBayData: getStatusCode-1
				// :"+myCurrentAPIResponse.getStatus());
				JSONParser parser = new JSONParser();
				try {
					JSONObject jsonBodyData = (JSONObject) parser.parse(responseBody);
					myCurrentAPIResponse.setJsonBodyResponse(jsonBodyData);
				} catch (ParseException e) {

					e.printStackTrace();
					ApplicationLauncher.logger.error("RestClient: ParseException : " + e.getMessage());
				}
				// myCurrentAPIResponse.setStatus(status);
				setRestApiClusterResponseBodyData(myCurrentAPIResponse);
				// setRestApiClusterResponseBodyData();
				// ApplicationLauncher.logger.info("RestClient: responseBody: " + responseBody +
				// " : " + inputPortId);
			}
		} catch (Exception e) {
			ApplicationLauncher.logger.info("RestClient: response failed : " + e.getMessage() + " : " + inputPortId);
			RestApiClusterResponse myCurrentAPIResponse = new RestApiClusterResponse();
			myCurrentAPIResponse.setStatuscode(String.valueOf(e.getMessage()));
			setRestApiClusterResponseData(myCurrentAPIResponse);
			ApplicationLauncher.logger.debug("RestClient: :Server failed");

		}
	}

	public void sendMeterStatusUpdate(ClusterServer clusterServer, String targetDisplay, int positionNo,
			String serialNo, String status, String reason) {
		ApplicationLauncher.logger.info("RestClient: sendMeterStatusUpdate: Entry: " + targetDisplay);
		ApplicationLauncher.logger.info("RestClient: sendMeterStatusUpdate: targetDisplay: " + targetDisplay);
		ApplicationLauncher.logger.info("RestClient: sendMeterStatusUpdate: positionNo: " + positionNo);
		ApplicationLauncher.logger
				.info("RestClient: sendMeterStatusUpdate: getIpAddress: " + clusterServer.getIpAddress());
		ApplicationLauncher.logger
				.info("RestClient: sendMeterStatusUpdate: getClusterId: " + clusterServer.getClusterId());
		ApplicationLauncher.logger.info("RestClient: sendMeterStatusUpdate: getPort: " + clusterServer.getPort());
		ApplicationLauncher.logger.info("RestClient: sendMeterStatusUpdate: getRootUrl: " + clusterServer.getRootUrl());
		ApplicationLauncher.logger.info("RestClient: sendMeterStatusUpdate: hit1: " + positionNo);
		// String url = "http://127.0.0.1:5001/api/"+targetDisplay+"/" + positionNo;
		String url = clusterServer.getRootUrl() + targetDisplay + positionNo;
		ApplicationLauncher.logger.info("RestClient: sendMeterStatusUpdate: hit2: " + positionNo);
		ApplicationLauncher.logger.info("RestClient: sendMeterStatusUpdate: URL -> <" + url + ">");

		try {
			HttpPut postRequest = new HttpPut(url);
			postRequest.addHeader("Content-Type", "application/json");

			// Prepare timestamp in required format
			String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());

			// Create JSON body
			JSONObject jsonBody = new JSONObject();
			jsonBody.put("id", positionNo);
			jsonBody.put("serialNo", serialNo);
			jsonBody.put("status", status);
			jsonBody.put("reason", reason);
			jsonBody.put("timestamp", timestamp);

			StringEntity entity = new StringEntity(jsonBody.toJSONString(), "UTF-8");
			postRequest.setEntity(entity);

			ApplicationLauncher.logger
					.debug("RestClient: sendMeterStatusUpdate: Payload -> " + jsonBody.toJSONString());

			try (CloseableHttpResponse response = httpClient.execute(postRequest)) {
				int statusCode = response.getStatusLine().getStatusCode();
				String responseBody = EntityUtils.toString(response.getEntity());
				// ApplicationLauncher.logger.info("RestClient: sendMeterStatusUpdate: Response
				// -> " + responseBody);
				if (statusCode >= 200 && statusCode < 300) {
					ApplicationLauncher.logger.info("HTTP PUT Success: Status Code = " + statusCode);
					ApplicationLauncher.logger.info("Response Body: " + responseBody);
					// Optionally return true or process the response
				} else {
					ApplicationLauncher.logger.warn("HTTP PUT Failed: Status Code = " + statusCode);
					ApplicationLauncher.logger.warn("Response Body: " + responseBody);
				}
			}

		} catch (Exception e) {
			ApplicationLauncher.logger.error("RestClient: sendMeterStatusUpdate: Error -> " + e.getMessage(), e);
		}

		ApplicationLauncher.logger.info("RestClient: sendMeterStatusUpdate: Exit");
	}

	public void sendPalletWithMetersStatusUpdate(ClusterServer clusterServer, String targetDisplay, String palletQrCode,
			List<Map<String, Object>> metersData) {
		ApplicationLauncher.logger
				.info("RestClient: sendPalletWithMetersStatusUpdate: Entry for pallet: " + palletQrCode);

		try {
			// String url = "http://127.0.0.1:5001/api/unloading_meters"; // Change endpoint
			// as needed
			String url = clusterServer.getRootUrl() + targetDisplay;
			// ApplicationLauncher.logger.info("RestClient: sendMeterStatusUpdate: hit2: " +
			// positionNo);
			ApplicationLauncher.logger.info("RestClient: sendPalletWithMetersStatusUpdate: URL -> <" + url + ">");
			HttpPost postRequest = new HttpPost(url); // Or use HttpPut
			postRequest.addHeader("Content-Type", "application/json");

			JSONObject requestJson = new JSONObject();
			requestJson.put("pallet_number", palletQrCode);

			JSONArray metersArray = new JSONArray();

			for (Map<String, Object> meter : metersData) {
				JSONObject meterJson = new JSONObject();
				meterJson.put("id", meter.get("id"));
				meterJson.put("serialNo", meter.get("meterSerialNo"));
				meterJson.put("status", meter.get("overallTestResultStatus"));
				meterJson.put("reason", meter.getOrDefault("errorCode", ""));
				metersArray.add(meterJson);
			}

			requestJson.put("meters", metersArray);

			ApplicationLauncher.logger
					.info("sendPalletWithMetersStatusUpdate: Payload -> " + requestJson.toJSONString());

			StringEntity entity = new StringEntity(requestJson.toJSONString(), "UTF-8");
			postRequest.setEntity(entity);

			try (CloseableHttpResponse response = httpClient.execute(postRequest)) {
				int statusCode = response.getStatusLine().getStatusCode();
				String responseBody = EntityUtils.toString(response.getEntity());

				if (statusCode >= 200 && statusCode < 300) {
					ApplicationLauncher.logger
							.info("sendPalletWithMetersStatusUpdate: Success - Status Code = " + statusCode);
					ApplicationLauncher.logger.debug("Response Body: " + responseBody);
				} else {
					ApplicationLauncher.logger
							.warn("sendPalletWithMetersStatusUpdate: Failed - Status Code = " + statusCode);
					ApplicationLauncher.logger.warn("Response Body: " + responseBody);
				}
			}

		} catch (Exception e) {
			ApplicationLauncher.logger.error("sendPalletWithMetersStatusUpdate: Exception -> " + e.getMessage(), e);
		}

		ApplicationLauncher.logger.info("RestClient: sendPalletWithMetersStatusUpdate: Exit");
	}

	/*
	 * public void sendIdleStatusUpdate(ClusterServer clusterServer, String
	 * tailEndUrl) {
	 * ApplicationLauncher.logger.info("RestClient: sendIdleStatusUpdate: Entry");
	 * 
	 * try {
	 * String url = clusterServer.getRootUrl() + tailEndUrl;
	 * ApplicationLauncher.logger.info("RestClient: sendIdleStatusUpdate: URL -> <"
	 * + url + ">");
	 * 
	 * HttpGet getRequest = new HttpGet(url);
	 * getRequest.addHeader("Accept", "application/json");
	 * 
	 * try (CloseableHttpResponse response = httpClient.execute(getRequest)) {
	 * int statusCode = response.getStatusLine().getStatusCode();
	 * String responseBody = EntityUtils.toString(response.getEntity());
	 * 
	 * if (statusCode >= 200 && statusCode < 300) {
	 * ApplicationLauncher.logger.
	 * info("sendIdleStatusUpdate: Success - Status Code = " + statusCode);
	 * ApplicationLauncher.logger.debug("Response Body: " + responseBody);
	 * } else {
	 * ApplicationLauncher.logger.
	 * warn("sendIdleStatusUpdate: Failed - Status Code = " + statusCode);
	 * ApplicationLauncher.logger.warn("Response Body: " + responseBody);
	 * }
	 * }
	 * 
	 * } catch (Exception e) {
	 * ApplicationLauncher.logger.error("sendIdleStatusUpdate: Exception -> " +
	 * e.getMessage(), e);
	 * }
	 * 
	 * ApplicationLauncher.logger.info("RestClient: sendIdleStatusUpdate: Exit");
	 * }
	 */

	public void sendIdleStatusUpdate(ClusterServer clusterServer, String tailEndUrl) {
		ApplicationLauncher.logger.info("RestClient: sendIdleStatusUpdate: Entry");
		CloseableHttpResponse response = null;
		try {
			String url = clusterServer.getRootUrl() + tailEndUrl;
			ApplicationLauncher.logger.info("RestClient: sendIdleStatusUpdate: URL -> <" + url + ">");

			HttpGet getRequest = new HttpGet(url);
			getRequest.addHeader("Accept", "application/json");

			// try
			response = httpClient.execute(getRequest); // {
			int statusCode = response.getStatusLine().getStatusCode();
			String responseBody = EntityUtils.toString(response.getEntity());

			if (statusCode >= 200 && statusCode < 300) {
				ApplicationLauncher.logger.info("sendIdleStatusUpdate: Success - Status Code = " + statusCode);
				ApplicationLauncher.logger.debug("Response Body: " + responseBody);
			} else {
				ApplicationLauncher.logger.warn("sendIdleStatusUpdate: Failed - Status Code = " + statusCode);
				ApplicationLauncher.logger.warn("Response Body: " + responseBody);
			}
			// }

		} catch (Exception e) {
			ApplicationLauncher.logger.error("sendIdleStatusUpdate: Exception -> " + e.getMessage(), e);
		} finally {
			// Properly close resources
			try {
				if (response != null) {
					response.close();
				}
				// Don't close httpClient if using shared connection manager
				// It will be managed by the connection manager
			} catch (IOException e) {
				ApplicationLauncher.logger.error("sendIdleStatusUpdate: Error closing resources: " + e.getMessage());
			}
		}

		ApplicationLauncher.logger.info("RestClient: sendIdleStatusUpdate: Exit");
	}

	public void sendPalletNumberUpdate(ClusterServer clusterServer, String targetDisplay, String palletNumber) {
		ApplicationLauncher.logger.info("RestClient: sendPalletNumberUpdate: Entry");
		ApplicationLauncher.logger.info("RestClient: sendPalletNumberUpdate: targetDisplay: " + targetDisplay);
		ApplicationLauncher.logger.info("RestClient: sendPalletNumberUpdate: palletNumber: " + palletNumber);
		ApplicationLauncher.logger
				.info("RestClient: sendPalletNumberUpdate: getIpAddress: " + clusterServer.getIpAddress());
		ApplicationLauncher.logger
				.info("RestClient: sendPalletNumberUpdate: getRootUrl: " + clusterServer.getRootUrl());

		String url = clusterServer.getRootUrl() + targetDisplay;
		ApplicationLauncher.logger.info("RestClient: sendPalletNumberUpdate: URL -> <" + url + ">");

		try {
			HttpPut putRequest = new HttpPut(url);
			putRequest.addHeader("Content-Type", "application/json");

			// Prepare JSON body
			JSONObject jsonBody = new JSONObject();
			jsonBody.put("pallet_number", palletNumber);

			StringEntity entity = new StringEntity(jsonBody.toJSONString(), "UTF-8");
			putRequest.setEntity(entity);

			ApplicationLauncher.logger
					.debug("RestClient: sendPalletNumberUpdate: Payload -> " + jsonBody.toJSONString());

			try (CloseableHttpResponse response = httpClient.execute(putRequest)) {
				int statusCode = response.getStatusLine().getStatusCode();
				String responseBody = EntityUtils.toString(response.getEntity());

				if (statusCode >= 200 && statusCode < 300) {
					ApplicationLauncher.logger.info("HTTP PUT Success: Status Code = " + statusCode);
					ApplicationLauncher.logger.info("Response Body: " + responseBody);
				} else {
					ApplicationLauncher.logger.warn("HTTP PUT Failed: Status Code = " + statusCode);
					ApplicationLauncher.logger.warn("Response Body: " + responseBody);
				}
			}

		} catch (Exception e) {
			ApplicationLauncher.logger.error("RestClient: sendPalletNumberUpdate: Error -> " + e.getMessage(), e);
		}

		ApplicationLauncher.logger.info("RestClient: sendPalletNumberUpdate: Exit");
	}

	public boolean isResponseReceived() {
		return responseReceived;
	}

	public RestApiJsonBodyResponse getRestApiClusterResponseBodyData() {
		return restApiClusterResponseBodyData;
	}

	public void setResponseReceived(boolean responseReceived) {
		this.responseReceived = responseReceived;
	}

	public void setRestApiClusterResponseBodyData(RestApiJsonBodyResponse restApiClusterResponseBodyData) {
		this.restApiClusterResponseBodyData = restApiClusterResponseBodyData;
	}

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep :InterruptedException:" + e.getMessage());
		}

	}

	public void clearRestApiClusterResponseData() {
		RestApiClusterResponse clearCurrentAPIResponseData = new RestApiClusterResponse();
		setRestApiClusterResponseData(clearCurrentAPIResponseData);
	}

	public RestApiClusterResponse getRestApiClusterResponseData() {
		return restApiClusterResponseData;
	}

	public void setRestApiClusterResponseData(RestApiClusterResponse restApiClusterResponseData) {
		this.restApiClusterResponseData = restApiClusterResponseData;
	}

}
