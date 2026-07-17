package com.tasnetwork.calibration.conveyor.AsyncHttpClient;

import java.util.Map;

import com.tasnetwork.calibration.conveyor.ClusterServer;

public class AsyncProcalVerifyClientManager {

	 
    private AsyncProcalVerifyClient asyncProcalClient = new AsyncProcalVerifyClient();
    
    private static boolean responseReceived = false;
    private static String responseData = "";
    
/*	public  void WaitForServerResponse(int WaitTimeInSec){
		int SleepCounter = WaitTimeInSec;

		while ((!ServerProperties.getServerStatus()) && (SleepCounter > 0)){
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

	}*/
    
    
/*    public void setData(String deviceId, String bayId,String outputId,String outputValue){
    	//setResponseReceived(false);
    	getAsyncProcalClient().setData( deviceId,  bayId, outputId, outputValue);
    }
    
    public void setBayData(ClusterServer clusterServer,String deviceId, String bayId,String outputId,String outputValue){
    	//setResponseReceived(false);
    	getAsyncProcalClient().setBayData(clusterServer, deviceId,  bayId, outputId, outputValue);
    }*/
    
/*    public void setBayData(String deviceId, String bayId,String outputId,String outputValue){
    	//setResponseReceived(false);
    	getAsyncConvClient().setBayData( deviceId,  bayId, outputId, outputValue);
    }*/
    
    public void sendCommand(ClusterServer clusterServer, String commandMessage){
    	//setResponseReceived(false);
    	getAsyncProcalClient().sendCommand(clusterServer, 		 commandMessage);
    }
    
    public void sendCommandServer(ClusterServer clusterServer, String endPoint, String commandMessage){
    	//setResponseReceived(false);
    	getAsyncProcalClient().sendCommandServer(clusterServer, commandMessage, endPoint);
    }
    
    public void sendCommandWithParamQuery(ClusterServer clusterServer, String endPoint, String commandMessage, String paramKey, String paramValue){
    	//setResponseReceived(false);
    	getAsyncProcalClient().sendCommandWithParamQuery(clusterServer, endPoint, commandMessage, paramKey, paramValue);
    }
    
    public void sendPostCommandServer(ClusterServer clusterServer, String endPoint, String commandMessage, Map<Integer, String> requestBodyMap){
    	//setResponseReceived(false);
    	getAsyncProcalClient().sendPostCommandServer( clusterServer,  endPoint,  commandMessage,  requestBodyMap);
    }
    
    public void sendPostCommandServerV2(ClusterServer clusterServer, String endPoint, String commandMessage, Map<Integer, Map<String, String>> requestBodyMap){
    	//setResponseReceived(false);
    	getAsyncProcalClient().sendPostCommandServerV2( clusterServer,  endPoint,  commandMessage,  requestBodyMap);
    }

	public  static boolean isResponseReceived() {
		return responseReceived;
	}

	public static void setResponseReceived(boolean respReceived) {
		responseReceived = respReceived;
	}

	public AsyncProcalVerifyClient getAsyncProcalClient() {
		return asyncProcalClient;
	}

	public void setAsyncProcalClient(AsyncProcalVerifyClient asyncConvClient) {
		this.asyncProcalClient = asyncConvClient;
	}
}
