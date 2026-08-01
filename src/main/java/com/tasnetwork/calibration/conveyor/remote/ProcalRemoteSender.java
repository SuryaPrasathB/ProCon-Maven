package com.tasnetwork.calibration.conveyor.remote;

import java.util.Map;

import com.tasnetwork.calibration.conveyor.ClusterServer;
import com.tasnetwork.calibration.conveyor.AsyncHttpClient.AsyncProcalVerifyClientManager;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

public class ProcalRemoteSender {

	public String sendStartCommandToProcal(ClusterServer clusterServer, String endPoint) {
		String procalServerResponse = "";
		AsyncProcalVerifyClientManager procalClientManager = new AsyncProcalVerifyClientManager();

		String startCommandMessage = "start";
		procalClientManager.sendCommandServer(clusterServer, endPoint, startCommandMessage);
		ApplicationLauncher.logger.debug("sendStartCommandToProcal: Wait Time Entry");
		procalClientManager.getAsyncProcalClient().WaitForServerResponse(20);
		ApplicationLauncher.logger.debug("sendStartCommandToProcal: Wait Time Exit");
		if (procalClientManager.getAsyncProcalClient().isResponseReceived()) {
			procalClientManager.getAsyncProcalClient().getResponseData();
			procalServerResponse = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse()
					.getMessage();
			ApplicationLauncher.logger.debug(
					"sendStartCommandToProcal: isResponseReceived : procalServerResponse: " + procalServerResponse);
			if (procalServerResponse.equals("startTestExecuteInitiated")) {
				ApplicationLauncher.logger.debug("sendStartCommandToProcal: procal start success");
			}
			try {

			} catch (Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger.error("sendStartCommandToProcal1: Exception1 : " + e.getMessage());
			}

		} else {

			ApplicationLauncher.logger.debug("sendStartCommandToProcal1: : response failed: ");

		}
		return procalServerResponse;
	}

	public String sendStepRunCommandToProcal(ClusterServer clusterServer, String endPoint) {
		String procalServerResponse = "";
		AsyncProcalVerifyClientManager procalClientManager = new AsyncProcalVerifyClientManager();

		String steprunCommandMessage = "steprun";
		procalClientManager.sendCommandServer(clusterServer, endPoint, steprunCommandMessage);
		ApplicationLauncher.logger.debug("sendStepRunCommandToProcal: Wait Time Entry");
		ApplicationLauncher.logger.debug("sendStepRunCommandToProcal: Wait Time Exit");
		if (procalClientManager.getAsyncProcalClient().isResponseReceived()) {
			procalClientManager.getAsyncProcalClient().getResponseData();
			procalServerResponse = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse()
					.getMessage();
			ApplicationLauncher.logger.debug(
					"sendStepRunCommandToProcal: isResponseReceived : procalServerResponse: " + procalServerResponse);
			if (procalServerResponse.equals("stepRunTestExecuteInitiated")) {
				ApplicationLauncher.logger.debug("sendStepRunCommandToProcal: procal steprun success");
			}
			try {

			} catch (Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger.error("sendStepRunCommandToProcal1: Exception1 : " + e.getMessage());
			}

		} else {

			ApplicationLauncher.logger.debug("sendStepRunCommandToProcal1: : response failed: ");

		}
		return procalServerResponse;
	}

	public String sendSwitchToNextCommandToProcal(ClusterServer clusterServer, String endPoint) {
		String procalServerResponse = "";
		AsyncProcalVerifyClientManager procalClientManager = new AsyncProcalVerifyClientManager();

		String steprunCommandMessage = "switchToNext";
		procalClientManager.sendCommandServer(clusterServer, endPoint, steprunCommandMessage);
		ApplicationLauncher.logger.debug("sendSwitchToNextCommandToProcal: Wait Time Entry");
		procalClientManager.getAsyncProcalClient().WaitForServerResponse(20);
		ApplicationLauncher.logger.debug("sendSwitchToNextCommandToProcal: Wait Time Exit");
		if (procalClientManager.getAsyncProcalClient().isResponseReceived()) {
			procalClientManager.getAsyncProcalClient().getResponseData();
			procalServerResponse = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse()
					.getMessage();
			ApplicationLauncher.logger
					.debug("sendSwitchToNextCommandToProcal: isResponseReceived : procalServerResponse: "
							+ procalServerResponse);
			if (procalServerResponse.equals("startTestExecuteInitiated")) {
				ApplicationLauncher.logger.debug("sendSwitchToNextCommandToProcal: procal steprun success");
			}
			try {

			} catch (Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger.error("sendSwitchToNextCommandToProcal1: Exception1 : " + e.getMessage());
			}

		} else {

			ApplicationLauncher.logger.debug("sendStepRunCommandToProcal1: : response failed: ");

		}
		return procalServerResponse;
	}

	public String sendStopCommandToProcal(ClusterServer clusterServer, String endPoint) {
		String procalServerResponse = "";
		AsyncProcalVerifyClientManager procalClientManager = new AsyncProcalVerifyClientManager();

		String stopCommandMessage = "stop";
		procalClientManager.sendCommandServer(clusterServer, endPoint, stopCommandMessage);
		ApplicationLauncher.logger.debug("sendStopCommandToProcal: Wait Time Entry");
		procalClientManager.getAsyncProcalClient().WaitForServerResponse(20);
		ApplicationLauncher.logger.debug("sendStopCommandToProcal: Wait Time Exit");
		if (procalClientManager.getAsyncProcalClient().isResponseReceived()) {
			procalClientManager.getAsyncProcalClient().getResponseData();
			procalServerResponse = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse()
					.getMessage();
			ApplicationLauncher.logger.debug(
					"sendStopCommandToProcal: isResponseReceived : procalServerResponse: " + procalServerResponse);
			if (procalServerResponse.equals("testStopInitiated")) {
				ApplicationLauncher.logger.debug("sendStopCommandToProcal: procal start success");
			}
			try {

			} catch (Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger.error("sendStopCommandToProcal: Exception1 : " + e.getMessage());
			}

		} else {

			ApplicationLauncher.logger.debug("sendStopCommandToProcal: : response failed: ");

		}
		return procalServerResponse;
	}

	public ProcalRemoteResponse sendParamStatusCommandToProcal(ClusterServer clusterServer, String endPoint) {
		String procalServerResponse = "";
		AsyncProcalVerifyClientManager procalClientManager = new AsyncProcalVerifyClientManager();
		ProcalRemoteResponse myProcalRemoteResponse = new ProcalRemoteResponse();

		String steprunCommandMessage = "parameterStatus";
		procalClientManager.sendCommandServer(clusterServer, endPoint, steprunCommandMessage);
		ApplicationLauncher.logger.debug("sendParamStatusCommandToProcal: Wait Time Entry");
		procalClientManager.getAsyncProcalClient().WaitForServerResponse(20);
		ApplicationLauncher.logger.debug("sendParamStatusCommandToProcal: Wait Time Exit");
		if (procalClientManager.getAsyncProcalClient().isResponseReceived()) {
			procalClientManager.getAsyncProcalClient().getResponseData();
			procalServerResponse = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse()
					.getMessage();
			ApplicationLauncher.logger
					.debug("sendParamStatusCommandToProcal: isResponseReceived : procalServerResponse: "
							+ procalServerResponse);
			myProcalRemoteResponse = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse();
			try {

			} catch (Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger.error("sendParamStatusCommandToProcal: Exception1 : " + e.getMessage());
			}

		} else {

			ApplicationLauncher.logger.debug("sendResultRefreshCommandToProcal: : response failed: ");

		}
		return myProcalRemoteResponse;
	}

	public ProcalRemoteResponse sendCommResultRefreshToProcal(ClusterServer clusterServer, String endPoint) {
		String procalServerResponse = "";
		AsyncProcalVerifyClientManager procalClientManager = new AsyncProcalVerifyClientManager();
		ProcalRemoteResponse myProcalRemoteResponse = new ProcalRemoteResponse();

		String resultRefreshCommandMessage = "getTpIdResult";
		procalClientManager.sendCommandServer(clusterServer, endPoint, resultRefreshCommandMessage);
		ApplicationLauncher.logger.debug("sendResultRefreshCommandToProcal: Wait Time Entry");
		procalClientManager.getAsyncProcalClient().WaitForServerResponse(20);
		ApplicationLauncher.logger.debug("sendResultRefreshCommandToProcal: Wait Time Exit");
		if (procalClientManager.getAsyncProcalClient().isResponseReceived()) {
			procalClientManager.getAsyncProcalClient().getResponseData();
			procalServerResponse = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse()
					.getMessage();
			ApplicationLauncher.logger
					.debug("sendResultRefreshCommandToProcal: isResponseReceived : procalServerResponse: "
							+ procalServerResponse);
			myProcalRemoteResponse = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse();
			try {

			} catch (Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger.error("sendResultRefreshCommandToProcal: Exception1 : " + e.getMessage());
			}

		} else {

			ApplicationLauncher.logger.debug("sendResultRefreshCommandToProcal: : response failed: ");

		}
		return myProcalRemoteResponse;
	}

	public String sendCommAllResultToProcal(ClusterServer clusterServer, String endPoint) {
		String procalServerResponse = "";
		String responseData = "";
		AsyncProcalVerifyClientManager procalClientManager = new AsyncProcalVerifyClientManager();

		String resultRefreshCommandMessage = "getAllTpResult";
		procalClientManager.sendCommandServer(clusterServer, endPoint, resultRefreshCommandMessage);
		ApplicationLauncher.logger.debug("sendCommAllResultToProcal: Wait Time Entry");
		procalClientManager.getAsyncProcalClient().WaitForServerResponse(20);
		ApplicationLauncher.logger.debug("sendCommAllResultToProcal: Wait Time Exit");
		if (procalClientManager.getAsyncProcalClient().isResponseReceived()) {
			responseData = procalClientManager.getAsyncProcalClient().getResponseData();
			procalServerResponse = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse()
					.getMessage();
			ApplicationLauncher.logger
					.debug("sendCommAllResultToProcal: isResponseReceived : responseData: " + responseData);
			ApplicationLauncher.logger.debug(
					"sendCommAllResultToProcal: isResponseReceived : procalServerResponse: " + procalServerResponse);
			procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse();
			try {

			} catch (Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger.error("sendCommAllResultToProcal: Exception1 : " + e.getMessage());
			}

		} else {

			ApplicationLauncher.logger.debug("sendCommAllResultToProcal: : response failed: ");

		}
		return responseData;
	}

	public String sendCloseRunCommandToProcal(ClusterServer clusterServer, String endPoint, String paramKey,
			String paramValue) {
		String procalServerResponse = "";
		AsyncProcalVerifyClientManager procalClientManager = new AsyncProcalVerifyClientManager();

		String closeRunCommandMessage = "closeRunProject";

		procalClientManager.sendCommandWithParamQuery(clusterServer, endPoint, closeRunCommandMessage, paramKey,
				paramValue); // procalVerifyServer
		ApplicationLauncher.logger.debug("sendCloseRunCommandToProcal: Wait Time Entry");
		procalClientManager.getAsyncProcalClient().WaitForServerResponse(20);
		ApplicationLauncher.logger.debug("sendCloseRunCommandToProcal: Wait Time Exit");
		if (procalClientManager.getAsyncProcalClient().isResponseReceived()) {
			procalClientManager.getAsyncProcalClient().getResponseData();
			procalServerResponse = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse()
					.getMessage();
			ApplicationLauncher.logger.debug(
					"sendCloseRunCommandToProcal: isResponseReceived : procalServerResponse: " + procalServerResponse);
			procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse();
			try {

			} catch (Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger.error("sendCloseRunCommandToProcal: Exception1 : " + e.getMessage());
			}

		} else {

			ApplicationLauncher.logger.debug("sendCloseRunCommandToProcal: : response failed: ");

		}
		return procalServerResponse;
	}

	public String sendSelectRunCommandToProcal(ClusterServer clusterServer, String endPoint, String paramKey,
			String paramValue) {
		String procalServerResponse = "";
		AsyncProcalVerifyClientManager procalClientManager = new AsyncProcalVerifyClientManager();

		String selectRunCommandMessage = "selectRunProject";
		procalClientManager.sendCommandWithParamQuery(clusterServer, endPoint, selectRunCommandMessage, paramKey,
				paramValue);
		ApplicationLauncher.logger.debug("sendSelectRunCommandToProcal: Wait Time Entry");
		procalClientManager.getAsyncProcalClient().WaitForServerResponse(20);
		ApplicationLauncher.logger.debug("sendSelectRunCommandToProcal: Wait Time Exit");
		if (procalClientManager.getAsyncProcalClient().isResponseReceived()) {
			procalClientManager.getAsyncProcalClient().getResponseData();
			procalServerResponse = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse()
					.getMessage();
			ApplicationLauncher.logger.debug(
					"sendSelectRunCommandToProcal: isResponseReceived : procalServerResponse: " + procalServerResponse);
			procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse();
			try {

			} catch (Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger.error("sendSelectRunCommandToProcal: Exception1 : " + e.getMessage());
			}

		} else {

			ApplicationLauncher.logger.debug("sendSelectRunCommandToProcal: : response failed: ");

		}
		return procalServerResponse;
	}

	public String sendDutMeterSerialNo(ClusterServer clusterServer, String endPoint, String commandMessage,
			Map<Integer, String> requestBodyMap) {
		String procalServerResponse = "";
		AsyncProcalVerifyClientManager procalClientManager = new AsyncProcalVerifyClientManager();
		procalClientManager.sendPostCommandServer(clusterServer, endPoint, commandMessage, requestBodyMap);
		ApplicationLauncher.logger.debug("sendSelectRunCommandToProcal: Wait Time Entry");
		procalClientManager.getAsyncProcalClient().WaitForServerResponse(20);
		ApplicationLauncher.logger.debug("sendSelectRunCommandToProcal: Wait Time Exit");
		if (procalClientManager.getAsyncProcalClient().isResponseReceived()) {
			procalClientManager.getAsyncProcalClient().getResponseData();
			procalServerResponse = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse()
					.getMessage();
			ApplicationLauncher.logger.debug(
					"sendSelectRunCommandToProcal: isResponseReceived : procalServerResponse: " + procalServerResponse);
			procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse();
			try {

			} catch (Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger.error("sendSelectRunCommandToProcal: Exception1 : " + e.getMessage());
			}

		} else {

			ApplicationLauncher.logger.debug("sendSelectRunCommandToProcal: : response failed: ");

		}
		return procalServerResponse;
	}

	public String sendDutMeterSerialNoV2(ClusterServer clusterServer, String endPoint, String commandMessage,
			Map<Integer, Map<String, String>> requestBodyMap) {
		String procalServerResponse = "";
		AsyncProcalVerifyClientManager procalClientManager = new AsyncProcalVerifyClientManager();
		procalClientManager.sendPostCommandServerV2(clusterServer, endPoint, commandMessage, requestBodyMap);
		ApplicationLauncher.logger.debug("sendSelectRunCommandToProcal: Wait Time Entry");
		procalClientManager.getAsyncProcalClient().WaitForServerResponse(20);
		ApplicationLauncher.logger.debug("sendSelectRunCommandToProcal: Wait Time Exit");
		if (procalClientManager.getAsyncProcalClient().isResponseReceived()) {
			procalClientManager.getAsyncProcalClient().getResponseData();
			procalServerResponse = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse()
					.getMessage();
			ApplicationLauncher.logger.debug(
					"sendSelectRunCommandToProcal: isResponseReceived : procalServerResponse: " + procalServerResponse);
			procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse();
			try {

			} catch (Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger.error("sendSelectRunCommandToProcal: Exception1 : " + e.getMessage());
			}

		} else {

			ApplicationLauncher.logger.debug("sendSelectRunCommandToProcal: : response failed: ");

		}
		return procalServerResponse;
	}

	public String sendSelectProjectRunScreenConfirmationCommand(ClusterServer clusterServer, String endPoint,
			String commandMessage, Map<Integer, String> requestBodyMap) {
		String procalServerResponse = "";
		AsyncProcalVerifyClientManager procalClientManager = new AsyncProcalVerifyClientManager();

		procalClientManager.sendPostCommandServer(clusterServer, endPoint, commandMessage, requestBodyMap);
		ApplicationLauncher.logger.debug("sendSelectRunCommandToProcal: Wait Time Entry");
		procalClientManager.getAsyncProcalClient().WaitForServerResponse(20);
		ApplicationLauncher.logger.debug("sendSelectRunCommandToProcal: Wait Time Exit");
		if (procalClientManager.getAsyncProcalClient().isResponseReceived()) {
			procalClientManager.getAsyncProcalClient().getResponseData();
			procalServerResponse = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse()
					.getMessage();
			ApplicationLauncher.logger.debug(
					"sendSelectRunCommandToProcal: isResponseReceived : procalServerResponse: " + procalServerResponse);
			procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse();
			try {

			} catch (Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger.error("sendSelectRunCommandToProcal: Exception1 : " + e.getMessage());
			}

		} else {

			ApplicationLauncher.logger.debug("sendSelectRunCommandToProcal: : response failed: ");
		}
		return procalServerResponse;
	}
}
