package com.tasnetwork.calibration.conveyor.remote;


import java.util.Map;

import com.tasnetwork.calibration.conveyor.ClusterServer;
import com.tasnetwork.calibration.conveyor.RestApiJsonBodyResponse;
import com.tasnetwork.calibration.conveyor.AsyncHttpClient.AsyncProcalVerifyClientManager;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

public class ProcalRemoteSender {

	public String sendStartCommandToProcal(ClusterServer clusterServer, String endPoint) {

		boolean status = false;
		String statusResponse = "";
		String procalServerResponse = "";
		AsyncProcalVerifyClientManager procalClientManager = new AsyncProcalVerifyClientManager();
		//String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
		String ipAddress = "192.168.1.169";
		String ipPort = "8085";
		ClusterServer procalVerifyServer = new ClusterServer(ipAddress,ipPort);// getServerDetails( terminalId, clusterId);
		//procalVerifyServer.setRootUrl("", ip_port);
		/*if(procalVerifyServer== null){
			
			ApplicationLauncher.logger.debug("sendStartCommandToProcal: cluster ip address details not found for terminalId:  " + terminalId + " , clusterId: " + clusterId);
			
			return null;
		}*/
		String startCommandMessage = "start";//"getTpIdResult" ;//"start";
		procalClientManager.sendCommandServer(clusterServer, endPoint, startCommandMessage);
		//ApplicationHomeController.update_left_status("Awaiting Device Response",ConstantApp.LEFT_STATUS_DEBUG);
		//Sleep(8000);
		RestApiJsonBodyResponse clusterResponseData = new RestApiJsonBodyResponse();
		ApplicationLauncher.logger.debug("sendStartCommandToProcal: Wait Time Entry");
		procalClientManager.getAsyncProcalClient().WaitForServerResponse(20);
		ApplicationLauncher.logger.debug("sendStartCommandToProcal: Wait Time Exit");
		if(procalClientManager.getAsyncProcalClient().isResponseReceived()){ //validate for server access
			//ApplicationHomeController.update_left_status("Device Connected",ConstantApp.LEFT_STATUS_DEBUG);
			//clusterResponseData = 
			//clusterResponseData = procalClientManager.getAsyncProcalClient().getRestApiClusterResponseBodyData();
			String responseData = procalClientManager.getAsyncProcalClient().getResponseData();
			//ApplicationLauncher.logger.debug("sendStartCommandToProcal: isResponseReceived : test1: responseData: " + responseData);
			procalServerResponse = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse().getMessage();
			ApplicationLauncher.logger.debug("sendStartCommandToProcal: isResponseReceived : procalServerResponse: " + procalServerResponse);
			if(procalServerResponse.equals("startTestExecuteInitiated")){
				ApplicationLauncher.logger.debug("sendStartCommandToProcal: procal start success");
			}
/*			ObjectMapper objectMapper = new ObjectMapper();
	        TestPoint testPoint = objectMapper.readValue(json, TestPoint.class);
	        System.out.println("Test Point ID: " + testPoint.getPresentTestPointId());*/
			
			//if(clusterResponseData.getStatusCode().equals(ConstantConveyor.HTTP_RESPONSE_SUCCESS)){
			//	ApplicationLauncher.logger.debug("sendStartCommandToProcal: isResponseReceived : test2: responseData: " + responseData);
				
				//clusterResponseData = procalClientManager.getAsyncConvClient().getRestApiClusterResponseBodyData();
				//ApplicationLauncher.logger.debug("sendStartCommandToProcal1: "+inputPortId+" : getDevice Data: "+ clusterResponseData.getJsonBodyResponse().get(inputPortId));
				try{
					//statusResponse = clusterResponseData.getJsonBodyResponse().get(inputPortId).toString();
					ApplicationLauncher.logger.debug("sendStartCommandToProcal: isResponseReceived : statusResponse: " + statusResponse);
				}catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("sendStartCommandToProcal1: Exception1 : "+e.getMessage());
				}
				//String responseData = procalClientManager.getAsyncConvClient().getResponseData();
				
				
				status = true;
			//}else{
			//	ApplicationLauncher.logger.debug("sendStartCommandToProcal: isResponseReceived : test3");
			//}
			

			
		}else{

			ApplicationLauncher.logger.debug("sendStartCommandToProcal1: : response failed: ");//+inputPortId);
			//statusResponse = inputPortId;
			/*Platform.runLater(()->{
				ref_txtAreaResponseDisplay.setText(ref_txtAreaResponseDisplay.getText()+"\n"+inputPortId+"-no response");
			});
			ApplicationHomeController.update_left_status("Device Connection Failed "+inputPortId+" :",ConstantApp.LEFT_STATUS_DEBUG);*/
		}
		return procalServerResponse;
	}
	
	public String sendStepRunCommandToProcal(ClusterServer clusterServer, String endPoint) {

		boolean status = false;
		String statusResponse = "";
		String procalServerResponse = "";
		AsyncProcalVerifyClientManager procalClientManager = new AsyncProcalVerifyClientManager();
		//String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
		String ipAddress = "192.168.1.169";
		String ipPort = "8085";
		ClusterServer procalVerifyServer = new ClusterServer(ipAddress,ipPort);// getServerDetails( terminalId, clusterId);
		//procalVerifyServer.setRootUrl("", ip_port);
		/*if(procalVerifyServer== null){
			
			ApplicationLauncher.logger.debug("sendStartCommandToProcal: cluster ip address details not found for terminalId:  " + terminalId + " , clusterId: " + clusterId);
			
			return null;
		}*/
		String steprunCommandMessage = "steprun";//"getTpIdResult" ;//"start";
		procalClientManager.sendCommandServer(clusterServer, endPoint, steprunCommandMessage);
		//ApplicationHomeController.update_left_status("Awaiting Device Response",ConstantApp.LEFT_STATUS_DEBUG);
		//Sleep(8000);
		RestApiJsonBodyResponse clusterResponseData = new RestApiJsonBodyResponse();
		ApplicationLauncher.logger.debug("sendStepRunCommandToProcal: Wait Time Entry");
		//procalClientManager.getAsyncProcalClient().WaitForServerResponse(20);
		ApplicationLauncher.logger.debug("sendStepRunCommandToProcal: Wait Time Exit");
		if(procalClientManager.getAsyncProcalClient().isResponseReceived()){ //validate for server access
			//ApplicationHomeController.update_left_status("Device Connected",ConstantApp.LEFT_STATUS_DEBUG);
			//clusterResponseData = 
			//clusterResponseData = procalClientManager.getAsyncProcalClient().getRestApiClusterResponseBodyData();
			String responseData = procalClientManager.getAsyncProcalClient().getResponseData();
			//ApplicationLauncher.logger.debug("sendStepRunCommandToProcal: isResponseReceived : test1: responseData: " + responseData);
			procalServerResponse = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse().getMessage();
			ApplicationLauncher.logger.debug("sendStepRunCommandToProcal: isResponseReceived : procalServerResponse: " + procalServerResponse);
			if(procalServerResponse.equals("stepRunTestExecuteInitiated")){
				ApplicationLauncher.logger.debug("sendStepRunCommandToProcal: procal steprun success");
			}
/*			ObjectMapper objectMapper = new ObjectMapper();
	        TestPoint testPoint = objectMapper.readValue(json, TestPoint.class);
	        System.out.println("Test Point ID: " + testPoint.getPresentTestPointId());*/
			
			//if(clusterResponseData.getStatusCode().equals(ConstantConveyor.HTTP_RESPONSE_SUCCESS)){
			//	ApplicationLauncher.logger.debug("sendStepRunCommandToProcal: isResponseReceived : test2: responseData: " + responseData);
				
				//clusterResponseData = procalClientManager.getAsyncConvClient().getRestApiClusterResponseBodyData();
				//ApplicationLauncher.logger.debug("sendStepRunCommandToProcal1: "+inputPortId+" : getDevice Data: "+ clusterResponseData.getJsonBodyResponse().get(inputPortId));
				try{
					//statusResponse = clusterResponseData.getJsonBodyResponse().get(inputPortId).toString();
					ApplicationLauncher.logger.debug("sendStepRunCommandToProcal: isResponseReceived : statusResponse: " + statusResponse);
				}catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("sendStepRunCommandToProcal1: Exception1 : "+e.getMessage());
				}
				//String responseData = procalClientManager.getAsyncConvClient().getResponseData();
				
				
				status = true;
			//}else{
			//	ApplicationLauncher.logger.debug("sendStepRunCommandToProcal: isResponseReceived : test3");
			//}
			

			
		}else{

			ApplicationLauncher.logger.debug("sendStepRunCommandToProcal1: : response failed: ");//+inputPortId);
			//statusResponse = inputPortId;
			/*Platform.runLater(()->{
				ref_txtAreaResponseDisplay.setText(ref_txtAreaResponseDisplay.getText()+"\n"+inputPortId+"-no response");
			});
			ApplicationHomeController.update_left_status("Device Connection Failed "+inputPortId+" :",ConstantApp.LEFT_STATUS_DEBUG);*/
		}
		return procalServerResponse;
	}
	
	public String sendSwitchToNextCommandToProcal(ClusterServer clusterServer, String endPoint) {

		boolean status = false;
		String statusResponse = "";
		String procalServerResponse = "";
		AsyncProcalVerifyClientManager procalClientManager = new AsyncProcalVerifyClientManager();
		//String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
		String ipAddress = "192.168.1.169";
		String ipPort = "8085";
		ClusterServer procalVerifyServer = new ClusterServer(ipAddress,ipPort);// getServerDetails( terminalId, clusterId);
		//procalVerifyServer.setRootUrl("", ip_port);
		/*if(procalVerifyServer== null){
			
			ApplicationLauncher.logger.debug("sendStartCommandToProcal: cluster ip address details not found for terminalId:  " + terminalId + " , clusterId: " + clusterId);
			
			return null;
		}*/
		String steprunCommandMessage = "switchToNext";//"getTpIdResult" ;//"start";
		procalClientManager.sendCommandServer(clusterServer, endPoint, steprunCommandMessage);
		//ApplicationHomeController.update_left_status("Awaiting Device Response",ConstantApp.LEFT_STATUS_DEBUG);
		//Sleep(8000);
		RestApiJsonBodyResponse clusterResponseData = new RestApiJsonBodyResponse();
		ApplicationLauncher.logger.debug("sendSwitchToNextCommandToProcal: Wait Time Entry");
		procalClientManager.getAsyncProcalClient().WaitForServerResponse(20);
		ApplicationLauncher.logger.debug("sendSwitchToNextCommandToProcal: Wait Time Exit");
		if(procalClientManager.getAsyncProcalClient().isResponseReceived()){ //validate for server access
			//ApplicationHomeController.update_left_status("Device Connected",ConstantApp.LEFT_STATUS_DEBUG);
			//clusterResponseData = 
			//clusterResponseData = procalClientManager.getAsyncProcalClient().getRestApiClusterResponseBodyData();
			String responseData = procalClientManager.getAsyncProcalClient().getResponseData();
			//ApplicationLauncher.logger.debug("sendSwitchToNextCommandToProcal: isResponseReceived : test1: responseData: " + responseData);
			procalServerResponse = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse().getMessage();
			ApplicationLauncher.logger.debug("sendSwitchToNextCommandToProcal: isResponseReceived : procalServerResponse: " + procalServerResponse);
			if(procalServerResponse.equals("startTestExecuteInitiated")){
				ApplicationLauncher.logger.debug("sendSwitchToNextCommandToProcal: procal steprun success");
			}
/*			ObjectMapper objectMapper = new ObjectMapper();
	        TestPoint testPoint = objectMapper.readValue(json, TestPoint.class);
	        System.out.println("Test Point ID: " + testPoint.getPresentTestPointId());*/
			
			//if(clusterResponseData.getStatusCode().equals(ConstantConveyor.HTTP_RESPONSE_SUCCESS)){
			//	ApplicationLauncher.logger.debug("sendSwitchToNextCommandToProcal: isResponseReceived : test2: responseData: " + responseData);
				
				//clusterResponseData = procalClientManager.getAsyncConvClient().getRestApiClusterResponseBodyData();
				//ApplicationLauncher.logger.debug("sendSwitchToNextCommandToProcal1: "+inputPortId+" : getDevice Data: "+ clusterResponseData.getJsonBodyResponse().get(inputPortId));
				try{
					//statusResponse = clusterResponseData.getJsonBodyResponse().get(inputPortId).toString();
					ApplicationLauncher.logger.debug("sendSwitchToNextCommandToProcal: isResponseReceived : statusResponse: " + statusResponse);
				}catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("sendSwitchToNextCommandToProcal1: Exception1 : "+e.getMessage());
				}
				//String responseData = procalClientManager.getAsyncConvClient().getResponseData();
				
				
				status = true;
			//}else{
			//	ApplicationLauncher.logger.debug("sendStepRunCommandToProcal: isResponseReceived : test3");
			//}
			

			
		}else{

			ApplicationLauncher.logger.debug("sendStepRunCommandToProcal1: : response failed: ");//+inputPortId);
			//statusResponse = inputPortId;
			/*Platform.runLater(()->{
				ref_txtAreaResponseDisplay.setText(ref_txtAreaResponseDisplay.getText()+"\n"+inputPortId+"-no response");
			});
			ApplicationHomeController.update_left_status("Device Connection Failed "+inputPortId+" :",ConstantApp.LEFT_STATUS_DEBUG);*/
		}
		return procalServerResponse;
	}
	
	public String sendStopCommandToProcal(ClusterServer clusterServer, String endPoint) {

		boolean status = false;
		String statusResponse = "";
		String procalServerResponse = "";
		AsyncProcalVerifyClientManager procalClientManager = new AsyncProcalVerifyClientManager();
		//String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
		String ipAddress = "192.168.1.169";
		String ipPort = "8085";
		ClusterServer procalVerifyServer = new ClusterServer(ipAddress,ipPort);// getServerDetails( terminalId, clusterId);
		//procalVerifyServer.setRootUrl("", ip_port);
		/*if(procalVerifyServer== null){
			
			ApplicationLauncher.logger.debug("sendStartCommandToProcal: cluster ip address details not found for terminalId:  " + terminalId + " , clusterId: " + clusterId);
			
			return null;
		}*/
		String stopCommandMessage = "stop";//"getTpIdResult" ;//"start";
		procalClientManager.sendCommandServer(clusterServer, endPoint, stopCommandMessage);
		//ApplicationHomeController.update_left_status("Awaiting Device Response",ConstantApp.LEFT_STATUS_DEBUG);
		//Sleep(8000);
		RestApiJsonBodyResponse clusterResponseData = new RestApiJsonBodyResponse();
		ApplicationLauncher.logger.debug("sendStopCommandToProcal: Wait Time Entry");
		procalClientManager.getAsyncProcalClient().WaitForServerResponse(20);
		ApplicationLauncher.logger.debug("sendStopCommandToProcal: Wait Time Exit");
		if(procalClientManager.getAsyncProcalClient().isResponseReceived()){ //validate for server access
			//ApplicationHomeController.update_left_status("Device Connected",ConstantApp.LEFT_STATUS_DEBUG);
			//clusterResponseData = 
			//clusterResponseData = procalClientManager.getAsyncProcalClient().getRestApiClusterResponseBodyData();
			String responseData = procalClientManager.getAsyncProcalClient().getResponseData();
			//ApplicationLauncher.logger.debug("sendStartCommandToProcal: isResponseReceived : test1: responseData: " + responseData);
			procalServerResponse = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse().getMessage();
			ApplicationLauncher.logger.debug("sendStopCommandToProcal: isResponseReceived : procalServerResponse: " + procalServerResponse);
			if(procalServerResponse.equals("testStopInitiated")){ 
				ApplicationLauncher.logger.debug("sendStopCommandToProcal: procal start success");
			}
/*			ObjectMapper objectMapper = new ObjectMapper();
	        TestPoint testPoint = objectMapper.readValue(json, TestPoint.class);
	        System.out.println("Test Point ID: " + testPoint.getPresentTestPointId());*/
			
			//if(clusterResponseData.getStatusCode().equals(ConstantConveyor.HTTP_RESPONSE_SUCCESS)){
			//	ApplicationLauncher.logger.debug("sendStartCommandToProcal: isResponseReceived : test2: responseData: " + responseData);
				
				//clusterResponseData = procalClientManager.getAsyncConvClient().getRestApiClusterResponseBodyData();
				//ApplicationLauncher.logger.debug("sendStartCommandToProcal1: "+inputPortId+" : getDevice Data: "+ clusterResponseData.getJsonBodyResponse().get(inputPortId));
				try{
					//statusResponse = clusterResponseData.getJsonBodyResponse().get(inputPortId).toString();
					ApplicationLauncher.logger.debug("sendStopCommandToProcal: isResponseReceived : statusResponse: " + statusResponse);
				}catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("sendStopCommandToProcal: Exception1 : "+e.getMessage());
				}
				//String responseData = procalClientManager.getAsyncConvClient().getResponseData();
				
				
				status = true;
			//}else{
			//	ApplicationLauncher.logger.debug("sendStartCommandToProcal: isResponseReceived : test3");
			//}
			

			
		}else{

			ApplicationLauncher.logger.debug("sendStopCommandToProcal: : response failed: ");//+inputPortId);
			//statusResponse = inputPortId;
			/*Platform.runLater(()->{
				ref_txtAreaResponseDisplay.setText(ref_txtAreaResponseDisplay.getText()+"\n"+inputPortId+"-no response");
			});
			ApplicationHomeController.update_left_status("Device Connection Failed "+inputPortId+" :",ConstantApp.LEFT_STATUS_DEBUG);*/
		}
		return procalServerResponse;
	}
	
	public ProcalRemoteResponse sendParamStatusCommandToProcal(ClusterServer clusterServer, String endPoint) {

		boolean status = false;
		String statusResponse = "";
		String procalServerResponse = "";
		AsyncProcalVerifyClientManager procalClientManager = new AsyncProcalVerifyClientManager();
		//String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
		String ipAddress = "192.168.1.169";
		String ipPort = "9091";
		ProcalRemoteResponse myProcalRemoteResponse = new ProcalRemoteResponse();
		ClusterServer procalVerifyServer = new ClusterServer(ipAddress,ipPort);// getServerDetails( terminalId, clusterId);
		//procalVerifyServer.setRootUrl("", ip_port);
		/*if(procalVerifyServer== null){
			
			ApplicationLauncher.logger.debug("sendStartCommandToProcal: cluster ip address details not found for terminalId:  " + terminalId + " , clusterId: " + clusterId);
			
			return null;
		}*/
		String steprunCommandMessage = "parameterStatus" ;//"start";
		procalClientManager.sendCommandServer(clusterServer, endPoint, steprunCommandMessage);
		//ApplicationHomeController.update_left_status("Awaiting Device Response",ConstantApp.LEFT_STATUS_DEBUG);
		//Sleep(8000);
		RestApiJsonBodyResponse clusterResponseData = new RestApiJsonBodyResponse();
		ApplicationLauncher.logger.debug("sendParamStatusCommandToProcal: Wait Time Entry");
		procalClientManager.getAsyncProcalClient().WaitForServerResponse(20);
		ApplicationLauncher.logger.debug("sendParamStatusCommandToProcal: Wait Time Exit");
		if(procalClientManager.getAsyncProcalClient().isResponseReceived()){ //validate for server access
			//ApplicationHomeController.update_left_status("Device Connected",ConstantApp.LEFT_STATUS_DEBUG);
			//clusterResponseData = 
			//clusterResponseData = procalClientManager.getAsyncProcalClient().getRestApiClusterResponseBodyData();
			String responseData = procalClientManager.getAsyncProcalClient().getResponseData();
			//ApplicationLauncher.logger.debug("sendStartCommandToProcal: isResponseReceived : test1: responseData: " + responseData);
			procalServerResponse = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse().getMessage();
			ApplicationLauncher.logger.debug("sendParamStatusCommandToProcal: isResponseReceived : procalServerResponse: " + procalServerResponse);
			myProcalRemoteResponse  = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse();
			/*if(procalServerResponse.equals("")){ RefreshResultmessage
				ApplicationLauncher.logger.debug("sendParamStatusCommandToProcal: procal start success");
			}*/
/*			ObjectMapper objectMapper = new ObjectMapper();
	        TestPoint testPoint = objectMapper.readValue(json, TestPoint.class);
	        System.out.println("Test Point ID: " + testPoint.getPresentTestPointId());*/
			
			//if(clusterResponseData.getStatusCode().equals(ConstantConveyor.HTTP_RESPONSE_SUCCESS)){
			//	ApplicationLauncher.logger.debug("sendStartCommandToProcal: isResponseReceived : test2: responseData: " + responseData);
				
				//clusterResponseData = procalClientManager.getAsyncConvClient().getRestApiClusterResponseBodyData();
				//ApplicationLauncher.logger.debug("sendStartCommandToProcal1: "+inputPortId+" : getDevice Data: "+ clusterResponseData.getJsonBodyResponse().get(inputPortId));
				try{
					//statusResponse = clusterResponseData.getJsonBodyResponse().get(inputPortId).toString();
					ApplicationLauncher.logger.debug("sendParamStatusCommandToProcal: isResponseReceived : statusResponse: " + statusResponse);
				}catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("sendParamStatusCommandToProcal: Exception1 : "+e.getMessage());
				}
				//String responseData = procalClientManager.getAsyncConvClient().getResponseData();
				
				
				status = true;
			//}else{
			//	ApplicationLauncher.logger.debug("sendStartCommandToProcal: isResponseReceived : test3");
			//}
			

			
		}else{

			ApplicationLauncher.logger.debug("sendResultRefreshCommandToProcal: : response failed: ");//+inputPortId);
			//statusResponse = inputPortId;
			/*Platform.runLater(()->{
				ref_txtAreaResponseDisplay.setText(ref_txtAreaResponseDisplay.getText()+"\n"+inputPortId+"-no response");
			});
			ApplicationHomeController.update_left_status("Device Connection Failed "+inputPortId+" :",ConstantApp.LEFT_STATUS_DEBUG);*/
		}
		return myProcalRemoteResponse;
	}
	
	public ProcalRemoteResponse sendCommResultRefreshToProcal(ClusterServer clusterServer, String endPoint) {

		boolean status = false;
		String statusResponse = "";
		String procalServerResponse = "";
		AsyncProcalVerifyClientManager procalClientManager = new AsyncProcalVerifyClientManager();
		//String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
		String ipAddress = "192.168.1.169";
		String ipPort = "8085";
		ProcalRemoteResponse myProcalRemoteResponse = new ProcalRemoteResponse();
		ClusterServer procalVerifyServer = new ClusterServer(ipAddress,ipPort);// getServerDetails( terminalId, clusterId);
		//procalVerifyServer.setRootUrl("", ip_port);
		/*if(procalVerifyServer== null){
			
			ApplicationLauncher.logger.debug("sendStartCommandToProcal: cluster ip address details not found for terminalId:  " + terminalId + " , clusterId: " + clusterId);
			
			return null;
		}*/
		String resultRefreshCommandMessage = "getTpIdResult" ;//"start";
		procalClientManager.sendCommandServer(clusterServer, endPoint, resultRefreshCommandMessage);
		//ApplicationHomeController.update_left_status("Awaiting Device Response",ConstantApp.LEFT_STATUS_DEBUG);
		//Sleep(8000);
		RestApiJsonBodyResponse clusterResponseData = new RestApiJsonBodyResponse();
		ApplicationLauncher.logger.debug("sendResultRefreshCommandToProcal: Wait Time Entry");
		procalClientManager.getAsyncProcalClient().WaitForServerResponse(20);
		ApplicationLauncher.logger.debug("sendResultRefreshCommandToProcal: Wait Time Exit");
		if(procalClientManager.getAsyncProcalClient().isResponseReceived()){ //validate for server access
			//ApplicationHomeController.update_left_status("Device Connected",ConstantApp.LEFT_STATUS_DEBUG);
			//clusterResponseData = 
			//clusterResponseData = procalClientManager.getAsyncProcalClient().getRestApiClusterResponseBodyData();
			String responseData = procalClientManager.getAsyncProcalClient().getResponseData();
			//ApplicationLauncher.logger.debug("sendStartCommandToProcal: isResponseReceived : test1: responseData: " + responseData);
			procalServerResponse = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse().getMessage();
			ApplicationLauncher.logger.debug("sendResultRefreshCommandToProcal: isResponseReceived : procalServerResponse: " + procalServerResponse);
			myProcalRemoteResponse  = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse();
			/*if(procalServerResponse.equals("")){ RefreshResultmessage
				ApplicationLauncher.logger.debug("sendResultRefreshCommandToProcal: procal start success");
			}*/
/*			ObjectMapper objectMapper = new ObjectMapper();
	        TestPoint testPoint = objectMapper.readValue(json, TestPoint.class);
	        System.out.println("Test Point ID: " + testPoint.getPresentTestPointId());*/
			
			//if(clusterResponseData.getStatusCode().equals(ConstantConveyor.HTTP_RESPONSE_SUCCESS)){
			//	ApplicationLauncher.logger.debug("sendStartCommandToProcal: isResponseReceived : test2: responseData: " + responseData);
				
				//clusterResponseData = procalClientManager.getAsyncConvClient().getRestApiClusterResponseBodyData();
				//ApplicationLauncher.logger.debug("sendStartCommandToProcal1: "+inputPortId+" : getDevice Data: "+ clusterResponseData.getJsonBodyResponse().get(inputPortId));
				try{
					//statusResponse = clusterResponseData.getJsonBodyResponse().get(inputPortId).toString();
					ApplicationLauncher.logger.debug("sendResultRefreshCommandToProcal: isResponseReceived : statusResponse: " + statusResponse);
				}catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("sendResultRefreshCommandToProcal: Exception1 : "+e.getMessage());
				}
				//String responseData = procalClientManager.getAsyncConvClient().getResponseData();
				
				
				status = true;
			//}else{
			//	ApplicationLauncher.logger.debug("sendStartCommandToProcal: isResponseReceived : test3");
			//}
			

			
		}else{

			ApplicationLauncher.logger.debug("sendResultRefreshCommandToProcal: : response failed: ");//+inputPortId);
			//statusResponse = inputPortId;
			/*Platform.runLater(()->{
				ref_txtAreaResponseDisplay.setText(ref_txtAreaResponseDisplay.getText()+"\n"+inputPortId+"-no response");
			});
			ApplicationHomeController.update_left_status("Device Connection Failed "+inputPortId+" :",ConstantApp.LEFT_STATUS_DEBUG);*/
		}
		return myProcalRemoteResponse;
	}
	
	public String sendCommAllResultToProcal(ClusterServer clusterServer, String endPoint) {

		boolean status = false;
		String statusResponse = "";
		String procalServerResponse = "";
		String responseData = "";
		AsyncProcalVerifyClientManager procalClientManager = new AsyncProcalVerifyClientManager();
		//String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
		String ipAddress = "192.168.1.169";
		String ipPort = "8085";
		ProcalRemoteResponse myProcalRemoteResponse = new ProcalRemoteResponse();
		ClusterServer procalVerifyServer = new ClusterServer(ipAddress,ipPort);// getServerDetails( terminalId, clusterId);
		//procalVerifyServer.setRootUrl("", ip_port);
		/*if(procalVerifyServer== null){
			
			ApplicationLauncher.logger.debug("sendStartCommandToProcal: cluster ip address details not found for terminalId:  " + terminalId + " , clusterId: " + clusterId);
			
			return null;
		}*/
		String resultRefreshCommandMessage = "getAllTpResult" ;//"start";
		procalClientManager.sendCommandServer(clusterServer, endPoint, resultRefreshCommandMessage);
		//ApplicationHomeController.update_left_status("Awaiting Device Response",ConstantApp.LEFT_STATUS_DEBUG);
		//Sleep(8000);
		RestApiJsonBodyResponse clusterResponseData = new RestApiJsonBodyResponse();
		ApplicationLauncher.logger.debug("sendCommAllResultToProcal: Wait Time Entry");
		procalClientManager.getAsyncProcalClient().WaitForServerResponse(20);
		ApplicationLauncher.logger.debug("sendCommAllResultToProcal: Wait Time Exit");
		if(procalClientManager.getAsyncProcalClient().isResponseReceived()){ //validate for server access
			//ApplicationHomeController.update_left_status("Device Connected",ConstantApp.LEFT_STATUS_DEBUG);
			//clusterResponseData = 
			//clusterResponseData = procalClientManager.getAsyncProcalClient().getRestApiClusterResponseBodyData();
			responseData = procalClientManager.getAsyncProcalClient().getResponseData();
			//ApplicationLauncher.logger.debug("sendStartCommandToProcal: isResponseReceived : test1: responseData: " + responseData);
			procalServerResponse = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse().getMessage();
			ApplicationLauncher.logger.debug("sendCommAllResultToProcal: isResponseReceived : responseData: " + responseData);
			ApplicationLauncher.logger.debug("sendCommAllResultToProcal: isResponseReceived : procalServerResponse: " + procalServerResponse);
			myProcalRemoteResponse  = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse();
			/*if(procalServerResponse.equals("")){ RefreshResultmessage
				ApplicationLauncher.logger.debug("sendCommAllResultToProcal: procal start success");
			}*/
/*			ObjectMapper objectMapper = new ObjectMapper();
	        TestPoint testPoint = objectMapper.readValue(json, TestPoint.class);
	        System.out.println("Test Point ID: " + testPoint.getPresentTestPointId());*/
			
			//if(clusterResponseData.getStatusCode().equals(ConstantConveyor.HTTP_RESPONSE_SUCCESS)){
			//	ApplicationLauncher.logger.debug("sendStartCommandToProcal: isResponseReceived : test2: responseData: " + responseData);
				
				//clusterResponseData = procalClientManager.getAsyncConvClient().getRestApiClusterResponseBodyData();
				//ApplicationLauncher.logger.debug("sendStartCommandToProcal1: "+inputPortId+" : getDevice Data: "+ clusterResponseData.getJsonBodyResponse().get(inputPortId));
				try{
					//statusResponse = clusterResponseData.getJsonBodyResponse().get(inputPortId).toString();
					ApplicationLauncher.logger.debug("sendCommAllResultToProcal: isResponseReceived : statusResponse: " + statusResponse);
				}catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("sendCommAllResultToProcal: Exception1 : "+e.getMessage());
				}
				//String responseData = procalClientManager.getAsyncConvClient().getResponseData();
				
				
				status = true;
			//}else{
			//	ApplicationLauncher.logger.debug("sendStartCommandToProcal: isResponseReceived : test3");
			//}
			

			
		}else{

			ApplicationLauncher.logger.debug("sendCommAllResultToProcal: : response failed: ");//+inputPortId);
			//statusResponse = inputPortId;
			/*Platform.runLater(()->{
				ref_txtAreaResponseDisplay.setText(ref_txtAreaResponseDisplay.getText()+"\n"+inputPortId+"-no response");
			});
			ApplicationHomeController.update_left_status("Device Connection Failed "+inputPortId+" :",ConstantApp.LEFT_STATUS_DEBUG);*/
		}
		return responseData;
	}

	public String sendCloseRunCommandToProcal(ClusterServer clusterServer, String endPoint, String paramKey, String paramValue) {
		
		boolean status = false;
		String statusResponse = "";
		String procalServerResponse = "";
		AsyncProcalVerifyClientManager procalClientManager = new AsyncProcalVerifyClientManager();
		//String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
		String ipAddress = "192.168.1.169";
		String ipPort = "8085";
		ProcalRemoteResponse myProcalRemoteResponse = new ProcalRemoteResponse();
		ClusterServer procalVerifyServer = new ClusterServer(ipAddress,ipPort);// getServerDetails( terminalId, clusterId);
		//procalVerifyServer.setRootUrl("", ip_port);
		/*if(procalVerifyServer== null){
			
			ApplicationLauncher.logger.debug("sendStartCommandToProcal: cluster ip address details not found for terminalId:  " + terminalId + " , clusterId: " + clusterId);
			
			return null;
		}*/
		String closeRunCommandMessage = "closeRunProject" ;
		/*String paramKey ="";
		String paramValue = "";*/
		
		// Get project name from config ==========================================================================
		
		procalClientManager.sendCommandWithParamQuery(clusterServer, endPoint, closeRunCommandMessage, paramKey, paramValue); //procalVerifyServer
		//ApplicationHomeController.update_left_status("Awaiting Device Response",ConstantApp.LEFT_STATUS_DEBUG);
		//Sleep(8000);
		RestApiJsonBodyResponse clusterResponseData = new RestApiJsonBodyResponse();
		ApplicationLauncher.logger.debug("sendCloseRunCommandToProcal: Wait Time Entry");
		procalClientManager.getAsyncProcalClient().WaitForServerResponse(20);
		ApplicationLauncher.logger.debug("sendCloseRunCommandToProcal: Wait Time Exit");
		if(procalClientManager.getAsyncProcalClient().isResponseReceived()){ //validate for server access
			//ApplicationHomeController.update_left_status("Device Connected",ConstantApp.LEFT_STATUS_DEBUG);
			//clusterResponseData = 
			//clusterResponseData = procalClientManager.getAsyncProcalClient().getRestApiClusterResponseBodyData();
			String responseData = procalClientManager.getAsyncProcalClient().getResponseData();
			//ApplicationLauncher.logger.debug("sendStartCommandToProcal: isResponseReceived : test1: responseData: " + responseData);
			procalServerResponse = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse().getMessage();
			ApplicationLauncher.logger.debug("sendCloseRunCommandToProcal: isResponseReceived : procalServerResponse: " + procalServerResponse);
			myProcalRemoteResponse  = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse();
			/*if(procalServerResponse.equals("")){ RefreshResultmessage
				ApplicationLauncher.logger.debug("sendResultRefreshCommandToProcal: procal start success");
			}*/
/*			ObjectMapper objectMapper = new ObjectMapper();
	        TestPoint testPoint = objectMapper.readValue(json, TestPoint.class);
	        System.out.println("Test Point ID: " + testPoint.getPresentTestPointId());*/
			
			//if(clusterResponseData.getStatusCode().equals(ConstantConveyor.HTTP_RESPONSE_SUCCESS)){
			//	ApplicationLauncher.logger.debug("sendStartCommandToProcal: isResponseReceived : test2: responseData: " + responseData);
				
				//clusterResponseData = procalClientManager.getAsyncConvClient().getRestApiClusterResponseBodyData();
				//ApplicationLauncher.logger.debug("sendStartCommandToProcal1: "+inputPortId+" : getDevice Data: "+ clusterResponseData.getJsonBodyResponse().get(inputPortId));
				try{
					//statusResponse = clusterResponseData.getJsonBodyResponse().get(inputPortId).toString();
					ApplicationLauncher.logger.debug("sendCloseRunCommandToProcal: isResponseReceived : statusResponse: " + statusResponse);
				}catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("sendCloseRunCommandToProcal: Exception1 : "+e.getMessage());
				}
				//String responseData = procalClientManager.getAsyncConvClient().getResponseData();
				
				
				status = true;
			//}else{
			//	ApplicationLauncher.logger.debug("sendStartCommandToProcal: isResponseReceived : test3");
			//}
			

			
		}else{

			ApplicationLauncher.logger.debug("sendCloseRunCommandToProcal: : response failed: ");//+inputPortId);
			//statusResponse = inputPortId;
			/*Platform.runLater(()->{
				ref_txtAreaResponseDisplay.setText(ref_txtAreaResponseDisplay.getText()+"\n"+inputPortId+"-no response");
			});
			ApplicationHomeController.update_left_status("Device Connection Failed "+inputPortId+" :",ConstantApp.LEFT_STATUS_DEBUG);*/
		}
		return procalServerResponse;
	}

	public String sendSelectRunCommandToProcal(ClusterServer clusterServer, String endPoint, String paramKey, String paramValue) {
		boolean status = false;
		String statusResponse = "";
		String procalServerResponse = "";
		AsyncProcalVerifyClientManager procalClientManager = new AsyncProcalVerifyClientManager();
		//String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
		String ipAddress = "192.168.1.169";
		String ipPort = "8085";
		ProcalRemoteResponse myProcalRemoteResponse = new ProcalRemoteResponse();
		ClusterServer procalVerifyServer = new ClusterServer(ipAddress,ipPort);// getServerDetails( terminalId, clusterId);
		//procalVerifyServer.setRootUrl("", ip_port);
		/*if(procalVerifyServer== null){
			
			ApplicationLauncher.logger.debug("sendStartCommandToProcal: cluster ip address details not found for terminalId:  " + terminalId + " , clusterId: " + clusterId);
			
			return null;
		}*/
		String selectRunCommandMessage = "selectRunProject" ;
		procalClientManager.sendCommandWithParamQuery(clusterServer, endPoint, selectRunCommandMessage, paramKey, paramValue);
		//ApplicationHomeController.update_left_status("Awaiting Device Response",ConstantApp.LEFT_STATUS_DEBUG);
		//Sleep(8000);
		RestApiJsonBodyResponse clusterResponseData = new RestApiJsonBodyResponse();
		ApplicationLauncher.logger.debug("sendSelectRunCommandToProcal: Wait Time Entry");
		procalClientManager.getAsyncProcalClient().WaitForServerResponse(20);
		ApplicationLauncher.logger.debug("sendSelectRunCommandToProcal: Wait Time Exit");
		if(procalClientManager.getAsyncProcalClient().isResponseReceived()){ //validate for server access
			//ApplicationHomeController.update_left_status("Device Connected",ConstantApp.LEFT_STATUS_DEBUG);
			//clusterResponseData = 
			//clusterResponseData = procalClientManager.getAsyncProcalClient().getRestApiClusterResponseBodyData();
			String responseData = procalClientManager.getAsyncProcalClient().getResponseData();
			//ApplicationLauncher.logger.debug("sendStartCommandToProcal: isResponseReceived : test1: responseData: " + responseData);
			procalServerResponse = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse().getMessage();
			ApplicationLauncher.logger.debug("sendSelectRunCommandToProcal: isResponseReceived : procalServerResponse: " + procalServerResponse);
			myProcalRemoteResponse  = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse();
			/*if(procalServerResponse.equals("")){ RefreshResultmessage
				ApplicationLauncher.logger.debug("sendResultRefreshCommandToProcal: procal start success");
			}*/
/*			ObjectMapper objectMapper = new ObjectMapper();
	        TestPoint testPoint = objectMapper.readValue(json, TestPoint.class);
	        System.out.println("Test Point ID: " + testPoint.getPresentTestPointId());*/
			
			//if(clusterResponseData.getStatusCode().equals(ConstantConveyor.HTTP_RESPONSE_SUCCESS)){
			//	ApplicationLauncher.logger.debug("sendStartCommandToProcal: isResponseReceived : test2: responseData: " + responseData);
				
				//clusterResponseData = procalClientManager.getAsyncConvClient().getRestApiClusterResponseBodyData();
				//ApplicationLauncher.logger.debug("sendStartCommandToProcal1: "+inputPortId+" : getDevice Data: "+ clusterResponseData.getJsonBodyResponse().get(inputPortId));
				try{
					//statusResponse = clusterResponseData.getJsonBodyResponse().get(inputPortId).toString();
					ApplicationLauncher.logger.debug("sendSelectRunCommandToProcal: isResponseReceived : statusResponse: " + statusResponse);
				}catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("sendSelectRunCommandToProcal: Exception1 : "+e.getMessage());
				}
				//String responseData = procalClientManager.getAsyncConvClient().getResponseData();
				
				
				status = true;
			//}else{
			//	ApplicationLauncher.logger.debug("sendStartCommandToProcal: isResponseReceived : test3");
			//}
			

			
		}else{

			ApplicationLauncher.logger.debug("sendSelectRunCommandToProcal: : response failed: ");//+inputPortId);
			//statusResponse = inputPortId;
			/*Platform.runLater(()->{
				ref_txtAreaResponseDisplay.setText(ref_txtAreaResponseDisplay.getText()+"\n"+inputPortId+"-no response");
			});
			ApplicationHomeController.update_left_status("Device Connection Failed "+inputPortId+" :",ConstantApp.LEFT_STATUS_DEBUG);*/
		}
		return procalServerResponse;
	}
	
	
	public String sendDutMeterSerialNo(ClusterServer clusterServer, String endPoint, String commandMessage, Map<Integer, String> requestBodyMap) {
		boolean status = false;
		String statusResponse = "";
		String procalServerResponse = "";
		AsyncProcalVerifyClientManager procalClientManager = new AsyncProcalVerifyClientManager();
		//String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
		String ipAddress = "192.168.1.169";
		String ipPort = "8085";
		ProcalRemoteResponse myProcalRemoteResponse = new ProcalRemoteResponse();
		ClusterServer procalVerifyServer = new ClusterServer(ipAddress,ipPort);// getServerDetails( terminalId, clusterId);
		//procalVerifyServer.setRootUrl("", ip_port);
		/*if(procalVerifyServer== null){
			
			ApplicationLauncher.logger.debug("sendStartCommandToProcal: cluster ip address details not found for terminalId:  " + terminalId + " , clusterId: " + clusterId);
			
			return null;
		}*/
		//String selectRunCommandMessage = "selectRunProject" ;
		procalClientManager.sendPostCommandServer(clusterServer, endPoint,  commandMessage, requestBodyMap);
		//ApplicationHomeController.update_left_status("Awaiting Device Response",ConstantApp.LEFT_STATUS_DEBUG);
		//Sleep(8000);
		RestApiJsonBodyResponse clusterResponseData = new RestApiJsonBodyResponse();
		ApplicationLauncher.logger.debug("sendSelectRunCommandToProcal: Wait Time Entry");
		procalClientManager.getAsyncProcalClient().WaitForServerResponse(20);
		ApplicationLauncher.logger.debug("sendSelectRunCommandToProcal: Wait Time Exit");
		if(procalClientManager.getAsyncProcalClient().isResponseReceived()){ //validate for server access
			//ApplicationHomeController.update_left_status("Device Connected",ConstantApp.LEFT_STATUS_DEBUG);
			//clusterResponseData = 
			//clusterResponseData = procalClientManager.getAsyncProcalClient().getRestApiClusterResponseBodyData();
			String responseData = procalClientManager.getAsyncProcalClient().getResponseData();
			//ApplicationLauncher.logger.debug("sendStartCommandToProcal: isResponseReceived : test1: responseData: " + responseData);
			procalServerResponse = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse().getMessage();
			ApplicationLauncher.logger.debug("sendSelectRunCommandToProcal: isResponseReceived : procalServerResponse: " + procalServerResponse);
			myProcalRemoteResponse  = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse();
			/*if(procalServerResponse.equals("")){ RefreshResultmessage
				ApplicationLauncher.logger.debug("sendResultRefreshCommandToProcal: procal start success");
			}*/
/*			ObjectMapper objectMapper = new ObjectMapper();
	        TestPoint testPoint = objectMapper.readValue(json, TestPoint.class);
	        System.out.println("Test Point ID: " + testPoint.getPresentTestPointId());*/
			
			//if(clusterResponseData.getStatusCode().equals(ConstantConveyor.HTTP_RESPONSE_SUCCESS)){
			//	ApplicationLauncher.logger.debug("sendStartCommandToProcal: isResponseReceived : test2: responseData: " + responseData);
				
				//clusterResponseData = procalClientManager.getAsyncConvClient().getRestApiClusterResponseBodyData();
				//ApplicationLauncher.logger.debug("sendStartCommandToProcal1: "+inputPortId+" : getDevice Data: "+ clusterResponseData.getJsonBodyResponse().get(inputPortId));
				try{
					//statusResponse = clusterResponseData.getJsonBodyResponse().get(inputPortId).toString();
					ApplicationLauncher.logger.debug("sendSelectRunCommandToProcal: isResponseReceived : statusResponse: " + statusResponse);
				}catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("sendSelectRunCommandToProcal: Exception1 : "+e.getMessage());
				}
				//String responseData = procalClientManager.getAsyncConvClient().getResponseData();
				
				
				status = true;
			//}else{
			//	ApplicationLauncher.logger.debug("sendStartCommandToProcal: isResponseReceived : test3");
			//}
			

			
		}else{

			ApplicationLauncher.logger.debug("sendSelectRunCommandToProcal: : response failed: ");//+inputPortId);
			//statusResponse = inputPortId;
			/*Platform.runLater(()->{
				ref_txtAreaResponseDisplay.setText(ref_txtAreaResponseDisplay.getText()+"\n"+inputPortId+"-no response");
			});
			ApplicationHomeController.update_left_status("Device Connection Failed "+inputPortId+" :",ConstantApp.LEFT_STATUS_DEBUG);*/
		}
		return procalServerResponse;
	}
	
	
	public String sendDutMeterSerialNoV2(ClusterServer clusterServer, String endPoint, String commandMessage, Map<Integer, Map<String, String>> requestBodyMap) {
		boolean status = false;
		String statusResponse = "";
		String procalServerResponse = "";
		AsyncProcalVerifyClientManager procalClientManager = new AsyncProcalVerifyClientManager();
		//String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
		String ipAddress = "192.168.1.169";
		String ipPort = "8085";
		ProcalRemoteResponse myProcalRemoteResponse = new ProcalRemoteResponse();
		ClusterServer procalVerifyServer = new ClusterServer(ipAddress,ipPort);// getServerDetails( terminalId, clusterId);
		//procalVerifyServer.setRootUrl("", ip_port);
		/*if(procalVerifyServer== null){
			
			ApplicationLauncher.logger.debug("sendStartCommandToProcal: cluster ip address details not found for terminalId:  " + terminalId + " , clusterId: " + clusterId);
			
			return null;
		}*/
		//String selectRunCommandMessage = "selectRunProject" ;
		procalClientManager.sendPostCommandServerV2(clusterServer, endPoint,  commandMessage, requestBodyMap);
		//ApplicationHomeController.update_left_status("Awaiting Device Response",ConstantApp.LEFT_STATUS_DEBUG);
		//Sleep(8000);
		RestApiJsonBodyResponse clusterResponseData = new RestApiJsonBodyResponse();
		ApplicationLauncher.logger.debug("sendSelectRunCommandToProcal: Wait Time Entry");
		procalClientManager.getAsyncProcalClient().WaitForServerResponse(20);
		ApplicationLauncher.logger.debug("sendSelectRunCommandToProcal: Wait Time Exit");
		if(procalClientManager.getAsyncProcalClient().isResponseReceived()){ //validate for server access
			//ApplicationHomeController.update_left_status("Device Connected",ConstantApp.LEFT_STATUS_DEBUG);
			//clusterResponseData = 
			//clusterResponseData = procalClientManager.getAsyncProcalClient().getRestApiClusterResponseBodyData();
			String responseData = procalClientManager.getAsyncProcalClient().getResponseData();
			//ApplicationLauncher.logger.debug("sendStartCommandToProcal: isResponseReceived : test1: responseData: " + responseData);
			procalServerResponse = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse().getMessage();
			ApplicationLauncher.logger.debug("sendSelectRunCommandToProcal: isResponseReceived : procalServerResponse: " + procalServerResponse);
			myProcalRemoteResponse  = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse();
			/*if(procalServerResponse.equals("")){ RefreshResultmessage
				ApplicationLauncher.logger.debug("sendResultRefreshCommandToProcal: procal start success");
			}*/
/*			ObjectMapper objectMapper = new ObjectMapper();
	        TestPoint testPoint = objectMapper.readValue(json, TestPoint.class);
	        System.out.println("Test Point ID: " + testPoint.getPresentTestPointId());*/
			
			//if(clusterResponseData.getStatusCode().equals(ConstantConveyor.HTTP_RESPONSE_SUCCESS)){
			//	ApplicationLauncher.logger.debug("sendStartCommandToProcal: isResponseReceived : test2: responseData: " + responseData);
				
				//clusterResponseData = procalClientManager.getAsyncConvClient().getRestApiClusterResponseBodyData();
				//ApplicationLauncher.logger.debug("sendStartCommandToProcal1: "+inputPortId+" : getDevice Data: "+ clusterResponseData.getJsonBodyResponse().get(inputPortId));
				try{
					//statusResponse = clusterResponseData.getJsonBodyResponse().get(inputPortId).toString();
					ApplicationLauncher.logger.debug("sendSelectRunCommandToProcal: isResponseReceived : statusResponse: " + statusResponse);
				}catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("sendSelectRunCommandToProcal: Exception1 : "+e.getMessage());
				}
				//String responseData = procalClientManager.getAsyncConvClient().getResponseData();
				
				
				status = true;
			//}else{
			//	ApplicationLauncher.logger.debug("sendStartCommandToProcal: isResponseReceived : test3");
			//}
			

			
		}else{

			ApplicationLauncher.logger.debug("sendSelectRunCommandToProcal: : response failed: ");//+inputPortId);
			//statusResponse = inputPortId;
			/*Platform.runLater(()->{
				ref_txtAreaResponseDisplay.setText(ref_txtAreaResponseDisplay.getText()+"\n"+inputPortId+"-no response");
			});
			ApplicationHomeController.update_left_status("Device Connection Failed "+inputPortId+" :",ConstantApp.LEFT_STATUS_DEBUG);*/
		}
		return procalServerResponse;
	}
	
	public String sendSelectProjectRunScreenConfirmationCommand(ClusterServer clusterServer, String endPoint, String commandMessage, Map<Integer, String> requestBodyMap) {
		boolean status = false;
		String statusResponse = "";
		String procalServerResponse = "";
		AsyncProcalVerifyClientManager procalClientManager = new AsyncProcalVerifyClientManager();
		String ipAddress = "192.168.1.169";
		String ipPort = "8085";
		ProcalRemoteResponse myProcalRemoteResponse = new ProcalRemoteResponse();
		ClusterServer procalVerifyServer = new ClusterServer(ipAddress,ipPort);

		String selectRunCommandMessage = "isTestRunScreenDisplayed" ;
		procalClientManager.sendPostCommandServer(clusterServer, endPoint,  commandMessage, requestBodyMap);
		RestApiJsonBodyResponse clusterResponseData = new RestApiJsonBodyResponse();
		ApplicationLauncher.logger.debug("sendSelectRunCommandToProcal: Wait Time Entry");
		procalClientManager.getAsyncProcalClient().WaitForServerResponse(20);
		ApplicationLauncher.logger.debug("sendSelectRunCommandToProcal: Wait Time Exit");
		if(procalClientManager.getAsyncProcalClient().isResponseReceived()){
			String responseData = procalClientManager.getAsyncProcalClient().getResponseData();
			procalServerResponse = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse().getMessage();
			ApplicationLauncher.logger.debug("sendSelectRunCommandToProcal: isResponseReceived : procalServerResponse: " + procalServerResponse);
			myProcalRemoteResponse  = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse();
				try{
					ApplicationLauncher.logger.debug("sendSelectRunCommandToProcal: isResponseReceived : statusResponse: " + statusResponse);
				}catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("sendSelectRunCommandToProcal: Exception1 : "+e.getMessage());
				}

				status = true;			
		}else{

			ApplicationLauncher.logger.debug("sendSelectRunCommandToProcal: : response failed: ");
		}
		return procalServerResponse;
	}
}
