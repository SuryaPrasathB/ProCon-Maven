package com.tasnetwork.calibration.conveyor.AsyncHttpClient;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import com.tasnetwork.calibration.conveyor.ClusterServer;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.constant.ConstantProTamp;
import com.tasnetwork.calibration.conveyor.database.MySQL_Controller;
import com.tasnetwork.calibration.conveyor.restClient.RestClient;
import com.tasnetwork.calibration.conveyor.util.GUIUtils;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;

public class ConveyorClientManager {


	// private static boolean messengerLocked = false;
	private AsyncConveyorClient asyncConvClient = new AsyncConveyorClient();

	private RestClient restConvClient = new RestClient();

	private static boolean responseReceived = false;
	private static String responseData = "";

	private static ConveyorClientManager instance;
	
	private static final ConcurrentHashMap<String, ConveyorClientManager> instances = new ConcurrentHashMap<>();
	

	private ConveyorClientManager() {
		// Private constructor to prevent direct instantiation
	}
	
/*	public static synchronized ConveyorClientManager getInstance(String clusterId) {
		return instances.computeIfAbsent(clusterId, k -> new ConveyorClientManager());
	}*/
	
	public static ConveyorClientManager getInstance(String clusterId) {
		return instances.computeIfAbsent(clusterId, k -> new ConveyorClientManager());
	}

	public static synchronized ConveyorClientManager getInstance() {
		if (instance == null) {
			instance = new ConveyorClientManager();
		}
		return instance;
	}
	
	public static class ClusterUtils {
	    private static final ConcurrentHashMap<String, Object> clusterLocks = new ConcurrentHashMap<>();
	    //private static volatile ConcurrentHashMap<String, Object> clusterLocks = new ConcurrentHashMap<>();
	    public static Object getClusterLock(String clusterId) {
	        return clusterLocks.computeIfAbsent(clusterId, k -> new Object());
	    }
	}
	
	public static class Cluster2Utils {
	    private static final ConcurrentHashMap<String, Object> cluster2Locks = new ConcurrentHashMap<>();
	    //private static volatile ConcurrentHashMap<String, Object> clusterLocks = new ConcurrentHashMap<>();
	    public static Object getClusterLock(String clusterId) {
	        return cluster2Locks.computeIfAbsent(clusterId, k -> new Object());
	    }
	}

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


	public void setData(String deviceId, String bayId,String outputId,String outputValue){
		//setResponseReceived(false);
		getAsyncConvClient().setData( deviceId,  bayId, outputId, outputValue);
	}

	/*public void getData(String deviceId, String bayId,String outputId,String outputValue){
		//setResponseReceived(false);
		getAsyncConvClient().getData( deviceId,  bayId, outputId, outputValue);
	}*/

	/*    public void setBayData(String deviceId, String bayId,String outputId,String outputValue){
	//setResponseReceived(false);
	getAsyncConvClient().setBayData( deviceId,  bayId, outputId, outputValue);
	}*/

	/*    public void setBayData(ClusterServer clusterServer,String deviceId, String bayId,String outputId,String outputValue){
    	//setResponseReceived(false);
    	getAsyncConvClient().setBayData(clusterServer, deviceId,  bayId, outputId, outputValue);
    }



    public void getBayData(ClusterServer clusterServer,String deviceId, String bayId,String outputId){
    	//setResponseReceived(false);
    	getAsyncConvClient().getBayData(clusterServer, deviceId,  bayId, outputId);
    }*/


	//public synchronized  void messageBayData(boolean setType, ClusterServer clusterServer,String deviceId, String bayId,String outputId,String outputValue) {
	// =========================		
	// For Each Cluster
	// =========================
	public void messageBayData(boolean setType, ClusterServer clusterServer, String deviceId, String bayId, String outputId, String outputValue) {
	    ApplicationLauncher.logger.debug("messageBayData : Entry");

	    int retryCount = 10;
	    boolean messageProcessed = false;
	    String clusterId = clusterServer.getClusterId();   // Get the cluster ID
	    
	    ApplicationLauncher.logger.debug("messageBayData : clusterId : " + clusterId);

	    Object clusterLock = ClusterUtils.getClusterLock(clusterId);  // Get lock for this cluster
	    
	    ApplicationLauncher.logger.debug("messageBayData : clusterLock : " + clusterLock);

	    while ((retryCount != 0) && (!messageProcessed))  && (!ProjectExecutionController.getUserAbortedFlag())) {
	        retryCount--;
	        ApplicationLauncher.logger.debug("messageBayData : retryCount: " + retryCount + " : " + outputId);

	        synchronized (clusterLock) {  // Lock based on clusterId
	            ApplicationLauncher.logger.debug("messageBayData: Processing cluster: " + clusterId + " : outputId : " + outputId);

	            // Process the message
	            if (setType) {
	                if (ProcalFeatureEnable.CONVEYOR_REST_CLIENT_ENABLED) {
	                    getRestConvClient().setBayData(clusterServer, deviceId, bayId, outputId, outputValue);
	                } else {
	                    getAsyncConvClient().setBayData(clusterServer, deviceId, bayId, outputId, outputValue);
	                }
	            } else {
	                if (ProcalFeatureEnable.CONVEYOR_REST_CLIENT_ENABLED) {
	                    getRestConvClient().getBayData(clusterServer, deviceId, bayId, outputId);
	                } else {
	                    getAsyncConvClient().getBayData(clusterServer, deviceId, bayId, outputId);
	                }
	            }

	            messageProcessed = true;
	            try {
	                Thread.sleep(500);  // Prevent flooding the cluster
	            } catch (InterruptedException e) {
	                Thread.currentThread().interrupt();
	            }

	            ApplicationLauncher.logger.debug("messageBayData: Completed processing for cluster: " + clusterId + " : outputId : " + outputId);
	        }
	    }
	}
	
	// =========================
	// For Each Bay
	// =========================
	/*public void messageBayData(boolean setType, ClusterServer clusterServer, String deviceId, String bayId, String outputId, String outputValue) {
	    ApplicationLauncher.logger.debug("messageBayData : Entry");
	    int retryCount = 10;
	    boolean messageProcessed = false;
	    
	    Object bayLock = BayUtils.getBayLock(bayId); // Get the lock for this bayId

	    while ((retryCount != 0) && (!messageProcessed) && (!ProjectExecutionController.getUserAbortedFlag())) {
	        retryCount--;
	        ApplicationLauncher.logger.debug("messageBayData : retryCount: " + retryCount + " : " + outputId);

	        synchronized (bayLock) {  // Synchronize on specific bayId lock
	            ApplicationLauncher.logger.debug("messageBayData: : Processing bayId: " + bayId);

	            if (setType) {
	                if (ProcalFeatureEnable.CONVEYOR_REST_CLIENT_ENABLED) {
	                    getRestConvClient().setBayData(clusterServer, deviceId, bayId, outputId, outputValue);
	                } else {
	                    getAsyncConvClient().setBayData(clusterServer, deviceId, bayId, outputId, outputValue);
	                }
	            } else {
	                if (ProcalFeatureEnable.CONVEYOR_REST_CLIENT_ENABLED) {
	                    getRestConvClient().getBayData(clusterServer, deviceId, bayId, outputId);
	                } else {
	                    getAsyncConvClient().getBayData(clusterServer, deviceId, bayId, outputId);
	                }
	            }

	            messageProcessed = true;
	            try {
	                Thread.sleep(500); // Adjust sleep time as needed
	            } catch (InterruptedException e) {
	                Thread.currentThread().interrupt();
	            }

	            ApplicationLauncher.logger.debug("messageBayData: : Completed processing for bayId: " + bayId);
	        }
	    }
	}*/
	
	/*public  void messageBayData(boolean setType, ClusterServer clusterServer,String deviceId, String bayId,String outputId,String outputValue) {
		ApplicationLauncher.logger.debug("messageBayData : Entry");
		int retryCount =10;
		boolean messageProcessed = false;
		while( (retryCount!=0) && (!messageProcessed) && (!ProjectExecutionController.getUserAbortedFlag())){
			retryCount--;
			ApplicationLauncher.logger.debug("messageBayData : retryCount: " + retryCount + " : " + outputId);

			while( (BayUtils.ftBayMessageSenderSemLocked) ){//&& (!ProjectExecutionController.getUserAbortedFlag())){
				ApplicationLauncher.logger.debug("messageBayData: : awaiting for ftBayMessageSenderSemLocked1 :" + outputId);
				if(ProjectExecutionController.getUserAbortedFlag()){
					ApplicationLauncher.logger.debug("messageBayData break:");
					break;
				}else{
					Sleep(70);
				}

			}
			ApplicationLauncher.logger.debug("messageBayData: : awaiting Exit for ftBayMessageSenderSemLocked2 :" + outputId);
			//synchronized(this) {
			if(!BayUtils.ftBayMessageSenderSemLocked){
				BayUtils.ftBayMessageSenderSemLocked = true;
				ApplicationLauncher.logger.debug("messageBayData: : ftBayMessageSenderSemLocked : locked: " + outputId );
				if(setType) {
					if(ProcalFeatureEnable.CONVEYOR_REST_CLIENT_ENABLED) {
						getRestConvClient().setBayData(clusterServer, deviceId,  bayId, outputId, outputValue);
					}else {
						getAsyncConvClient().setBayData(clusterServer, deviceId,  bayId, outputId, outputValue);
					}
				}else {
					if(ProcalFeatureEnable.CONVEYOR_REST_CLIENT_ENABLED) {
						getRestConvClient().getBayData(clusterServer, deviceId,  bayId, outputId);
					}else {
						getAsyncConvClient().getBayData(clusterServer, deviceId,  bayId, outputId);
					}
				}
				messageProcessed =true;
				Sleep(500);   // RECENT CHANGES - (100)  :  NOT WORKING AT (3000)
				BayUtils.ftBayMessageSenderSemLocked = false;
				ApplicationLauncher.logger.debug("messageBayData: : ftBayMessageSenderSemLocked : unlocked : " +outputId );
				//}
			}else{
				ApplicationLauncher.logger.debug("messageBayData: :  still locked ftBayMessageSenderSemLocked :" + outputId);
			}
		}


	}*/
	
	
	

	public  static boolean isResponseReceived() {
		return responseReceived;
	}

	public static void setResponseReceived(boolean respReceived) {
		responseReceived = respReceived;
	}

	public  AsyncConveyorClient getAsyncConvClient() {
		return asyncConvClient;
	}

	public void setAsyncConvClient(AsyncConveyorClient asyncConvClient) {
		this.asyncConvClient = asyncConvClient;
	}



	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep2 :InterruptedException:"+ e.getMessage());
		}

	}

	public RestClient getRestConvClient() {
		return restConvClient;
	}

	public void setRestConvClient(RestClient restConvClient) {
		this.restConvClient = restConvClient;
	}
}
