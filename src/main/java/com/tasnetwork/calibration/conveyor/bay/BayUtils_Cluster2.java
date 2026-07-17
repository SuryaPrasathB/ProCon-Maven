/*package com.tasnetwork.calibration.conveyor.bay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.tasnetwork.calibration.conveyor.ClusterServer;
import com.tasnetwork.calibration.conveyor.RestApiClusterResponse;
import com.tasnetwork.calibration.conveyor.RestApiJsonBodyResponse;
import com.tasnetwork.calibration.conveyor.AsyncHttpClient.ConveyorClientManager;
import com.tasnetwork.calibration.conveyor.AsyncHttpClient.ConveyorClientManagerCluster2;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.bay.configloader.Bay;
import com.tasnetwork.calibration.conveyor.bay.configloader.ClusterDetail;
import com.tasnetwork.calibration.conveyor.bay.configloader.DutDevice;
import com.tasnetwork.calibration.conveyor.bay.configloader.InputPort;
import com.tasnetwork.calibration.conveyor.bay.configloader.Ldu;
import com.tasnetwork.calibration.conveyor.bay.configloader.MegaOhmMeter;
import com.tasnetwork.calibration.conveyor.bay.configloader.OutputPort;
import com.tasnetwork.calibration.conveyor.bay.configloader.QrScanner;
import com.tasnetwork.calibration.conveyor.bay.configloader.Terminal;
import com.tasnetwork.calibration.conveyor.bay.configloader.TerminalBayConfigModel;
import com.tasnetwork.calibration.conveyor.bay.configloader.VoltMeter;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorCluster;
import com.tasnetwork.calibration.conveyor.device.ConveyorDeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.spring.orm.model.DeviceSetting;

import javafx.application.Platform;

public class BayUtils_Cluster2 {
	private static final long MAX_TIMEOUT = 5000;
	private static TerminalBayConfigModel  bayConfigModel = ConveyorDeviceDataManagerController.getBayConfigParsedKey();

	public static volatile boolean   ftBayMessageSenderSemLocked = false;
	private static Map<String,ArrayList<String>> clusterBayNameListMap = new HashMap<String,ArrayList<String>>();
	private static Map<String,String> clusterNameIdListMap = new LinkedHashMap<String,String>();
	private static Map<String,String> clusterBayNameIdMap = new LinkedHashMap<String,String>();


	private static Map<String,Map<String,ArrayList<String>>> filteredClusterBayNamePositionListMap = new LinkedHashMap<String,Map<String,ArrayList<String>>>();
	private static Map<String,Map<String,String>> filteredClusterBayPositionNoCnameMap = new LinkedHashMap<String,Map<String,String>>();
	private static Map<String,Map<String,String>> filteredClusterBayPositionNoDeviceIdMap = new LinkedHashMap<String,Map<String,String>>();
	private static Map<String,Map<String,ArrayList<String>>> filteredClusterBayPositionNoAddressListMap = new LinkedHashMap<String,Map<String,ArrayList<String>>>();

	private static Map<String,ArrayList<String>> qrClusterBayNamePositionListMap = new HashMap<String,ArrayList<String>>();
	private static Map<String,String> qrClusterBayPositionNoCnameMap = new LinkedHashMap<String,String>();
	private static Map<String,String> qrClusterBayPositionNoDeviceIdMap = new LinkedHashMap<String,String>();

	private static Map<String,ArrayList<String>> dutClusterBayNamePositionListMap = new HashMap<String,ArrayList<String>>();
	private static Map<String,String> dutClusterBayPositionNoCnameMap = new LinkedHashMap<String,String>();
	private static Map<String,String> dutClusterBayPositionNoDeviceIdMap = new LinkedHashMap<String,String>();


	private static Map<String,ArrayList<String>> megaOhmMeterClusterBayNamePositionListMap = new HashMap<String,ArrayList<String>>();
	private static Map<String,String> megaOhmMeterClusterBayPositionNoCnameMap = new LinkedHashMap<String,String>();
	private static Map<String,String> megaOhmMeterClusterBayPositionNoDeviceIdMap = new LinkedHashMap<String,String>();
	private static Map<String,ArrayList<String>> megaOhmMeterClusterBayPositionNoAddressListMap = new LinkedHashMap<String,ArrayList<String>>();

	private static Map<String,ArrayList<String>> voltMeterClusterBayNamePositionListMap = new HashMap<String,ArrayList<String>>();
	private static Map<String,String> voltMeterClusterBayPositionNoCnameMap = new LinkedHashMap<String,String>();
	private static Map<String,String> voltMeterClusterBayPositionNoDeviceIdMap = new LinkedHashMap<String,String>();
	private static Map<String,ArrayList<String>> voltMeterClusterBayPositionNoAddressListMap = new LinkedHashMap<String,ArrayList<String>>();


	private static Map<String,ArrayList<String>> lduClusterBayNamePositionListMap = new HashMap<String,ArrayList<String>>();
	private static Map<String,String> lduClusterBayPositionNoCnameMap = new LinkedHashMap<String,String>();
	private static Map<String,String> lduClusterBayPositionNoDeviceIdMap = new LinkedHashMap<String,String>();
	private static Map<String,ArrayList<String>> lduClusterBayPositionNoAddressListMap = new LinkedHashMap<String,ArrayList<String>>();

	private static final ConcurrentHashMap<String, Object> bayLocks = new ConcurrentHashMap<>();

    public static Object getBayLock(String bayId) {
        return bayLocks.computeIfAbsent(bayId, k -> new Object());
    }

	public String manipulateDeviceId(DeviceSetting deviceSetting) {

		ApplicationLauncher.logger.debug("manipulateDeviceId : getClusterId : " + deviceSetting.getClusterId());
		ApplicationLauncher.logger.debug("manipulateDeviceId : getBayId : " + deviceSetting.getBayId());
		ApplicationLauncher.logger.debug("manipulateDeviceId : getDeviceType : " + deviceSetting.getDeviceType());
		ApplicationLauncher.logger.debug("manipulateDeviceId : getPositionNo : " + deviceSetting.getPositionNo());
		String deviceId = String.format("%02d", Integer.parseInt(ConstantConveyorConfig.MY_TERMINAL_ID)) +
				String.format("%02d", Integer.parseInt(deviceSetting.getClusterId())) + 
				String.format("%02d", Integer.parseInt(deviceSetting.getBayId())) +
				deviceSetting.getDeviceType()+ 
				String.format("%02d", Integer.parseInt(deviceSetting.getPositionNo()));
		ApplicationLauncher.logger.debug("manipulateDeviceId : deviceId : " + deviceId);
		return deviceId;

	}
	public ClusterServer getServerDetails(String terminalId, String clusterId) {

		ClusterServer clusterServer = null;

		//String clusterIpAddress = "";
		//String clusterPortNo = "";
		Optional<ClusterDetail> clusterOpt = 		getBayConfigModel().getTerminal().stream()
				.filter(e1->e1.getTerminalId().equals(terminalId))
				.flatMap(terminal -> terminal.getClusterDetails().stream())
				.filter(e2->e2.getClusterId().equals(clusterId))
				.findFirst();

		if (clusterOpt.isPresent()) {
			//ClusterDetail clusterDetail = clusterOpt.get();
			//clusterIpAddress = clusterOpt.get().getClusterIpAddress();
			//clusterPortNo = clusterOpt.get().getClusterPortId();
			clusterServer = new  ClusterServer(clusterOpt.get().getClusterIpAddress(),clusterOpt.get().getClusterPortId(),clusterId);
		}


		return clusterServer;
	}
	//========================================================================	
	public static void delay(long delayTime) {
		long startTime = System.currentTimeMillis();

		while ((System.currentTimeMillis() - startTime) < delayTime) {
			if ((System.currentTimeMillis() - startTime) >= MAX_TIMEOUT) {
				break;
			}

			try {
				Thread.sleep(1); // Sleep briefly to reduce CPU usage
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt(); // Restore interrupted status
				break;
			}
		}
	}
	//===============================================================================================================================	 
	public static IoPortInfo getOutputPortDetails(String searchPortName) {
		Optional<OutputPort> outputPortOpt = ConveyorDeviceDataManagerController.getBayConfigParsedKey().getTerminal().stream()
				.filter(e-> e.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID))
				.flatMap(terminal -> terminal.getOutputPort().stream())
				.filter(p -> searchPortName.equals(p.getPortName()))
				.findFirst();

		if (outputPortOpt.isPresent()) {
			OutputPort outputPortDetails = outputPortOpt.get();
			ApplicationLauncher.logger.debug("getOutputPortDetails : getPortId    : " + outputPortDetails.getPortId());
			ApplicationLauncher.logger.debug("getOutputPortDetails : getClusterId : " + outputPortDetails.getClusterId());
			ApplicationLauncher.logger.debug("getOutputPortDetails : getBayId     : " + outputPortDetails.getBayId());

			// Return all three values wrapped in an OutputPortInfo object
			return new IoPortInfo(outputPortDetails.getPortId(), outputPortDetails.getClusterId(), outputPortDetails.getBayId());
		}

		return null; // or handle the case if outputPort is not found
	}
		
	public QrScanner getQrPortDetails(String searchPortName ) {

		QrScanner outputPortDetails = new QrScanner();
		Optional<QrScanner> outputPortOpt = DeviceDataManagerController.getBayConfigParsedKey().getTerminal().stream()
				.filter(e-> e.getTerminalId().equals(ConstantConfig.MY_TERMINAL_ID))
				.flatMap(terminal -> terminal.getQrScanner().stream())
				.filter(p -> searchPortName.equals(p.getPortName()))
				.findFirst();


		if (outputPortOpt.isPresent()) {
			outputPortDetails = outputPortOpt.get();
			ApplicationLauncher.logger.debug("getOutputPortDetails : getPortId    : " + outputPortDetails.getPortId());
			ApplicationLauncher.logger.debug("getOutputPortDetails : getClusterId : " + outputPortDetails.getClusterId());
			ApplicationLauncher.logger.debug("getOutputPortDetails : getBayId     : " + outputPortDetails.getBayId());

			// Return all three values wrapped in an OutputPortInfo object
			return new IoPortInfo(outputPortDetails.getPortId(), outputPortDetails.getClusterId(), outputPortDetails.getBayId());
		}

		return null; // or handle the case if outputPort is not found
	}

	public static IoPortInfo getInputPortDetailsWithPositionNo(String terminalId,String clusterId, 
			String bayId,
			int positionNo) {
		ApplicationLauncher.logger.debug("getInputPortDetailsWithPositionNo : inp terminalId     : " + Integer.parseInt(terminalId));
		ApplicationLauncher.logger.debug("getInputPortDetailsWithPositionNo : inp clusterId     : " + Integer.parseInt(clusterId));
		ApplicationLauncher.logger.debug("getInputPortDetailsWithPositionNo : inp bayId         : " + Integer.parseInt(bayId));
		ApplicationLauncher.logger.debug("getInputPortDetailsWithPositionNo : inp positionNo    : " + positionNo);
				
		DeviceDataManagerController.getBayConfigParsedKey().getTerminal().stream()
		//.filter(e-> e.getTerminalId().equals("01"))
		//.filter(e-> Integer.parseInt(e.getTerminalId())==Integer.parseInt("01"))
		.flatMap(terminal -> terminal.getInputPort().stream())
		//.filter(p -> Integer.parseInt(p.getClusterId())== Integer.parseInt("01"))
		//.filter(p -> Integer.parseInt(p.getBayId())== Integer.parseInt("01"))
		//.filter(p -> Integer.parseInt(p.getPositionId())==(5))
		.forEach(e->{
			ApplicationLauncher.logger.debug("getInputPortDetailsWithPositionNo: getClusterId    : " + e.getClusterId());
			ApplicationLauncher.logger.debug("getInputPortDetailsWithPositionNo: getBayId    : " + e.getBayId());
			ApplicationLauncher.logger.debug("getInputPortDetailsWithPositionNo: cName    : " + e.getPortName());
			ApplicationLauncher.logger.debug("getInputPortDetailsWithPositionNo: getPositionId    : " + e.getPositionId());
		});
		DeviceDataManagerController.getBayConfigParsedKey().getTerminal().stream()
		//.filter(e-> e.getTerminalId().equals("01"))
		.filter(e-> Integer.parseInt(e.getTerminalId())==Integer.parseInt("01"))
		.flatMap(terminal -> terminal.getInputPort().stream())
		.filter(p -> Integer.parseInt(p.getClusterId())== Integer.parseInt("01"))
		.filter(p -> Integer.parseInt(p.getBayId())== Integer.parseInt("01"))
		.filter(p -> Integer.parseInt(p.getPositionId())==(5))
		.forEach(e->{
			ApplicationLauncher.logger.debug("getInputPortDetailsWithPositionNo2: getClusterId    : " + e.getClusterId());
			ApplicationLauncher.logger.debug("getInputPortDetailsWithPositionNo2: getBayId    : " + e.getBayId());
			ApplicationLauncher.logger.debug("getInputPortDetailsWithPositionNo2: cName    : " + e.getPortName());
			ApplicationLauncher.logger.debug("getInputPortDetailsWithPositionNo2: getPositionId    : " + e.getPositionId());
		});
		Optional<InputPort> inputPortOpt = ConveyorDeviceDataManagerController.getBayConfigParsedKey().getTerminal().stream()
				//.filter(e-> e.getTerminalId().equals(terminalId))
				.filter(e-> Integer.parseInt(e.getTerminalId())==Integer.parseInt(terminalId))
				.flatMap(terminal -> terminal.getInputPort().stream())
				.filter(p -> Integer.parseInt(p.getClusterId())==Integer.parseInt(clusterId))
				.filter(p1 -> Integer.parseInt(p1.getBayId())==Integer.parseInt(bayId))
				.filter(p2 -> Integer.parseInt(p2.getPositionId())== positionNo)
				.findFirst();

						.filter(p -> p.getClusterId().equals(clusterId))
				.filter(p -> p.getBayId().equals(bayId))
				.filter(p -> p.getPositionId().equals(String.valueOf(positionNo)))
				.findFirst();

		if (inputPortOpt.isPresent()) {
			InputPort inputPortDetails = inputPortOpt.get();
			ApplicationLauncher.logger.debug("getInputPortDetailsWithPositionNo : getPortId    : " + inputPortDetails.getPortId());
			ApplicationLauncher.logger.debug("getInputPortDetailsWithPositionNo : getClusterId : " + inputPortDetails.getClusterId());
			ApplicationLauncher.logger.debug("getInputPortDetailsWithPositionNo : getBayId     : " + inputPortDetails.getBayId());

			// Return all three values wrapped in an OutputPortInfo object
			return new IoPortInfo(inputPortDetails.getPortId(), inputPortDetails.getClusterId(), inputPortDetails.getBayId());
		}

		return null; // or handle the case if outputPort is not found
	}

	public static IoPortInfo getInputPortDetails(String searchPortName) {
		Optional<InputPort> inputPortOpt = ConveyorDeviceDataManagerController.getBayConfigParsedKey().getTerminal().stream()
				.filter(e-> e.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID))
				.flatMap(terminal -> terminal.getInputPort().stream())
				.filter(p -> searchPortName.equals(p.getPortName()))
				.findFirst();

		if (inputPortOpt.isPresent()) {
			InputPort inputPortDetails = inputPortOpt.get();
			ApplicationLauncher.logger.debug("getInputPortDetails : getPortId    : " + inputPortDetails.getPortId());
			ApplicationLauncher.logger.debug("getInputPortDetails : getClusterId : " + inputPortDetails.getClusterId());
			ApplicationLauncher.logger.debug("getInputPortDetails : getBayId     : " + inputPortDetails.getBayId());

			// Return all three values wrapped in an OutputPortInfo object
			return new IoPortInfo(inputPortDetails.getPortId(), inputPortDetails.getClusterId(), inputPortDetails.getBayId());
		}

		return null; // or handle the case if outputPort is not found
	}

		public static ArrayList<String> getInputPortDetails(String searchPortName) {
		Optional<InputPort> inputPortOpt = DeviceDataManagerController.getBayConfigParsedKey().getTerminal().stream()
				.filter(e-> e.getTerminalId().equals(ConstantConfig.MY_TERMINAL_ID))
				.flatMap(terminal -> terminal.getInputPort().stream())
				.filter(p -> searchPortName.equals(p.getPortName()))
				.findFirst();

		if (inputPortOpt.isPresent()) {
			InputPort inputPortDetails = inputPortOpt.get();
			ApplicationLauncher.logger.debug("getInputPortDetails : getPortId    : " + inputPortDetails.getPortId());
			ApplicationLauncher.logger.debug("getInputPortDetails : getClusterId : " + inputPortDetails.getClusterId());
			ApplicationLauncher.logger.debug("getInputPortDetails : getBayId     : " + inputPortDetails.getBayId());

			// Return all three values wrapped in an OutputPortInfo object
			return new IoPortInfo(inputPortDetails.getPortId(), inputPortDetails.getClusterId(), inputPortDetails.getBayId());
		}

		return null; // or handle the case if outputPort is not found
	}


	//===============================================================================================================================

	public boolean setOutputDataOnCluster(String deviceId, String bayId,String outputPortId, String outputStatus) {  

		boolean status = false;
		AsyncConveyorClientManager cluster1ClientManager = new AsyncConveyorClientManager();
		cluster1ClientManager.setData(deviceId, bayId,  outputPortId, outputStatus);
		ApplicationHomeController.update_left_status("Awaiting Device Response",ConstantApp.LEFT_STATUS_DEBUG);
		//Sleep(8000);
		cluster1ClientManager.getAsyncConvClient().WaitForServerResponse(8);
		if(cluster1ClientManager.getAsyncConvClient().isResponseReceived()){ //validate for server access
			ApplicationHomeController.update_left_status("Device Connected",ConstantApp.LEFT_STATUS_DEBUG);

			RestApiClusterResponse clusterResponseData = new RestApiClusterResponse();
			clusterResponseData = cluster1ClientManager.getAsyncConvClient().getRestApiClusterResponseData();
			ApplicationLauncher.logger.info("AsyncConveyorClient: "+outputPortId+" : getDevice Data: "+ clusterResponseData.getDevice());
			ApplicationLauncher.logger.info("AsyncConveyorClient: "+outputPortId+" : getStatus Data: "+ clusterResponseData.getStatus());


			String responseData = cluster1ClientManager.getAsyncConvClient().getResponseData();

			status = true;

		}else{

			ApplicationLauncher.logger.debug("AsyncConveyorClient: Device Connection Failed: "+outputPortId);
			ApplicationHomeController.update_left_status("Device Connection Failed "+outputPortId+" :",ConstantApp.LEFT_STATUS_DEBUG);
		}
		return status;
	}


		public String  setInitiatePulseCounterOnCluster(String deviceId, String bayId,String outputPortId, String outputStatus) {

		//boolean status = false;
		//AsyncConveyorClientManager cluster1ClientManager = new AsyncConveyorClientManager();
		String returnResponse = setOutputDataToBay(deviceId, bayId,  outputPortId, outputStatus);
		ApplicationHomeController.update_left_status("Awaiting Device Response",ConstantApp.LEFT_STATUS_DEBUG);
		//Sleep(8000);
		cluster1ClientManager.getAsyncConvClient().WaitForServerResponse(8);
		if(cluster1ClientManager.getAsyncConvClient().isResponseReceived()){ //validate for server access
			ApplicationHomeController.update_left_status("Device Connected",ConstantApp.LEFT_STATUS_DEBUG);

			RestApiClusterResponse clusterResponseData = new RestApiClusterResponse();
			clusterResponseData = cluster1ClientManager.getAsyncConvClient().getRestApiClusterResponseData();
			ApplicationLauncher.logger.info("setInitiatePulseCounterOnCluster: "+outputPortId+" : getDevice Data: "+ clusterResponseData.getDevice());
			ApplicationLauncher.logger.info("setInitiatePulseCounterOnCluster: "+outputPortId+" : getStatus Data: "+ clusterResponseData.getStatus());


			String responseData = cluster1ClientManager.getAsyncConvClient().getResponseData();

			status = true;

		}else{

			ApplicationLauncher.logger.debug("setInitiatePulseCounterOnCluster: Device Connection Failed: "+outputPortId);
			ApplicationHomeController.update_left_status("Device Connection Failed "+outputPortId+" :",ConstantApp.LEFT_STATUS_DEBUG);
		}
		return returnResponse;
	}

	
	public boolean setInitiatePulseCounterOnCluster(String clusterId, String bayId, String inputPortId, String outputStatus) {
	    ApplicationLauncher.logger.debug("setInitiatePulseCounterOnCluster: inputPortId: " + inputPortId);
	    Boolean status = false;
	    String statusResponse = "";
	    ConveyorClientManager cluster1ClientManager = ConveyorClientManager.getInstance();
	    String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
	    ClusterServer clusterServer = getServerDetails(terminalId, clusterId);

	    if (clusterServer == null) {
	        ApplicationLauncher.logger.debug("setInitiatePulseCounterOnCluster: cluster IP address details not found for terminalId: " + terminalId + " , clusterId: " + clusterId);
	        return status;
	    }

	    boolean setOutput = true;
	    cluster1ClientManager.messageBayData(setOutput, clusterServer, clusterId, bayId, inputPortId, outputStatus);

	    ApplicationLauncher.logger.debug("setInitiatePulseCounterOnCluster: Wait Time Entry");
	    boolean isResponseReceived = false;

	    if (ProcalFeatureEnable.CONVEYOR_REST_CLIENT_ENABLED) {
	        cluster1ClientManager.getRestConvClient().WaitForServerResponse(12);
	        isResponseReceived = cluster1ClientManager.getRestConvClient().isResponseReceived();
	    } else {
	        cluster1ClientManager.getAsyncConvClient().WaitForServerResponse(12);
	        isResponseReceived = cluster1ClientManager.getAsyncConvClient().isResponseReceived();
	    }

	    ApplicationLauncher.logger.debug("setInitiatePulseCounterOnCluster: Wait Time Exit");

	    if (isResponseReceived) {
	        RestApiJsonBodyResponse clusterResponseData;
	        if (ProcalFeatureEnable.CONVEYOR_REST_CLIENT_ENABLED) {
	            clusterResponseData = cluster1ClientManager.getRestConvClient().getRestApiClusterResponseBodyData();
	        } else {
	            clusterResponseData = cluster1ClientManager.getAsyncConvClient().getRestApiClusterResponseBodyData();
	        }

	        ApplicationLauncher.logger.debug("setInitiatePulseCounterOnCluster: isResponseReceived : test1");
	        if (clusterResponseData.getStatusCode().equals(ConstantConveyor.HTTP_RESPONSE_SUCCESS)) {
	            status = true;
	            ApplicationLauncher.logger.debug("setInitiatePulseCounterOnCluster: isResponseReceived : test2");
	            try {
	                statusResponse = clusterResponseData.getJsonBodyResponse().get(inputPortId).toString();
	                ApplicationLauncher.logger.debug("setInitiatePulseCounterOnCluster: " + inputPortId + " : statusResponse Data: " + statusResponse);
	            } catch (Exception e) {
	                e.printStackTrace();
	                ApplicationLauncher.logger.error("setInitiatePulseCounterOnCluster: Exception1 : " + e.getMessage());
	            }
	        } else {
	            ApplicationLauncher.logger.debug("setInitiatePulseCounterOnCluster: isResponseReceived : test3");
	        }
	    } else {
	        ApplicationLauncher.logger.debug("setInitiatePulseCounterOnCluster: response failed : " + inputPortId);
	    }

	    return status;
	}


	public boolean setInitiatePulseCounterOnCluster(String clusterId, String bayId,String inputPortId, String outputStatus) {
		ApplicationLauncher.logger.debug("setInitiatePulseCounterOnCluster: inputPortId: " + inputPortId);
		Boolean status = null;
		String statusResponse = "";
		ConveyorClientManager cluster1ClientManager = ConveyorClientManager.getInstance();
		RestApiJsonBodyResponse clusterResponseData = new RestApiJsonBodyResponse();
		String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
		ClusterServer clusterServer = getServerDetails( terminalId, clusterId);
		if(clusterServer== null){

			ApplicationLauncher.logger.debug("setInitiatePulseCounterOnCluster: cluster ip address details not found for terminalId:  " + terminalId + " , clusterId: " + clusterId);

			return status;
		}
		status =false;
		//cluster1ClientManager.setBayData(clusterServer,clusterId, bayId,  inputPortId, outputStatus);
		boolean setOutput = true;
		cluster1ClientManager.messageBayData(setOutput,clusterServer,clusterId, bayId,  inputPortId,outputStatus);
		cluster1ClientManager.getAsyncConvClient().WaitForServerResponse(12);//8);
		if(cluster1ClientManager.getAsyncConvClient().isResponseReceived()){ //validate for server access


			//RestApiClusterResponse clusterResponseData = new RestApiClusterResponse();
			clusterResponseData = cluster1ClientManager.getAsyncConvClient().getRestApiClusterResponseBodyData();
			//ApplicationLauncher.logger.debug("setOutputDataToBay: "+outputPortId+" : getDevice Data: "+ clusterResponseData.getDevice());
			//ApplicationLauncher.logger.debug("setOutputDataToBay: "+outputPortId+" : getStatus Data: "+ clusterResponseData.getStatus());

			if(clusterResponseData.getStatusCode().equals(ConstantConveyor.HTTP_RESPONSE_SUCCESS)){
				status = true;
				try{
					statusResponse = clusterResponseData.getJsonBodyResponse().get(inputPortId).toString();
					ApplicationLauncher.logger.debug("setInitiatePulseCounterOnCluster: "+inputPortId+" : statusResponse Data: "+ statusResponse);
				}catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("setInitiatePulseCounterOnCluster: Exception1 : "+e.getMessage());
				}
			}
			//String responseData = cluster1ClientManager.getAsyncConvClient().getResponseData();




		}else{
			ApplicationLauncher.logger.debug("setInitiatePulseCounterOnCluster: response failed : "+inputPortId);

		}
		return status;
		boolean status = false;
		AsyncConveyorClientManager cluster1ClientManager = new AsyncConveyorClientManager();
		cluster1ClientManager.setData(deviceId, bayId,  outputPortId, outputStatus);
		ApplicationHomeController.update_left_status("Awaiting Device Response",ConstantApp.LEFT_STATUS_DEBUG);
		//Sleep(8000);
		cluster1ClientManager.getAsyncConvClient().WaitForServerResponse(8);
		if(cluster1ClientManager.getAsyncConvClient().isResponseReceived()){ //validate for server access
			ApplicationHomeController.update_left_status("Device Connected",ConstantApp.LEFT_STATUS_DEBUG);

			RestApiClusterResponse clusterResponseData = new RestApiClusterResponse();
			clusterResponseData = cluster1ClientManager.getAsyncConvClient().getRestApiClusterResponseData();
			ApplicationLauncher.logger.info("setInitiatePulseCounterOnCluster: "+outputPortId+" : getDevice Data: "+ clusterResponseData.getDevice());
			ApplicationLauncher.logger.info("setInitiatePulseCounterOnCluster: "+outputPortId+" : getStatus Data: "+ clusterResponseData.getStatus());


			String responseData = cluster1ClientManager.getAsyncConvClient().getResponseData();

			status = true;

		}else{

			ApplicationLauncher.logger.debug("setInitiatePulseCounterOnCluster: Device Connection Failed: "+outputPortId);
			ApplicationHomeController.update_left_status("Device Connection Failed "+outputPortId+" :",ConstantApp.LEFT_STATUS_DEBUG);
		}
		return status;
	}


	//========================================================================================

		public static String getOutputDataOnCluster(String deviceId, String bayId,String outputPortId) {

		String state = "XX";
		AsyncConveyorClientManager cluster1ClientManager = new AsyncConveyorClientManager();
		vnbvghv
		//cluster1ClientManager.setData(deviceId, bayId,  outputPortId, outputStatus);
		ApplicationHomeController.update_left_status("Awaiting Device Response",ConstantApp.LEFT_STATUS_DEBUG);
		//Sleep(8000);
		cluster1ClientManager.getAsyncConvClient().WaitForServerResponse(8);
		if(cluster1ClientManager.getAsyncConvClient().isResponseReceived()){ //validate for server access
			ApplicationHomeController.update_left_status("Device Connected",ConstantApp.LEFT_STATUS_DEBUG);

			RestApiClusterResponse clusterResponseData = new RestApiClusterResponse();
			clusterResponseData = cluster1ClientManager.getAsyncConvClient().getRestApiClusterResponseData();
			ApplicationLauncher.logger.info("AsyncConveyorClient: "+outputPortId+" : getDevice Data: "+ clusterResponseData.getDevice());
			ApplicationLauncher.logger.info("AsyncConveyorClient: "+outputPortId+" : getStatus Data: "+ clusterResponseData.getStatus());				

			String responseData = cluster1ClientManager.getAsyncConvClient().getResponseData();		 
			//status = true;

		}else{		 
			ApplicationLauncher.logger.debug("AsyncConveyorClient: Device Connection Failed: "+outputPortId);				
			ApplicationHomeController.update_left_status("Device Connection Failed "+outputPortId+" :",ConstantApp.LEFT_STATUS_DEBUG);
		}
		return state;
	}

	//========================================================================================

	public String getInputDataFromBay(String clusterId, String bayId, String inputPortId) {
	    String statusResponse = "";
	    ConveyorClientManager cluster1ClientManager = ConveyorClientManager.getInstance();
	    String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
	    ClusterServer clusterServer = getServerDetails(terminalId, clusterId);

	    if (clusterServer == null) {
	        ApplicationLauncher.logger.debug("getInputDataFromBay: cluster IP address details not found for terminalId: " + terminalId + " , clusterId: " + clusterId);
	        return null;
	    }

	    String dummyOutputValue = "";
	    boolean setOutput = false;
	    cluster1ClientManager.messageBayData(setOutput, clusterServer, clusterId, bayId, inputPortId, dummyOutputValue);

	    ApplicationLauncher.logger.debug("getInputDataFromBay: Wait Time Entry");
	    boolean isResponseReceived = false;

	    if (ProcalFeatureEnable.CONVEYOR_REST_CLIENT_ENABLED) {
	        cluster1ClientManager.getRestConvClient().WaitForServerResponse(8);
	        isResponseReceived = cluster1ClientManager.getRestConvClient().isResponseReceived();
	    } else {
	        cluster1ClientManager.getAsyncConvClient().WaitForServerResponse(8);
	        isResponseReceived = cluster1ClientManager.getAsyncConvClient().isResponseReceived();
	    }

	    ApplicationLauncher.logger.debug("getInputDataFromBay: Wait Time Exit");

	    if (isResponseReceived) {
	        RestApiJsonBodyResponse clusterResponseData;
	        if (ProcalFeatureEnable.CONVEYOR_REST_CLIENT_ENABLED) {
	            clusterResponseData = cluster1ClientManager.getRestConvClient().getRestApiClusterResponseBodyData();
	        } else {
	            clusterResponseData = cluster1ClientManager.getAsyncConvClient().getRestApiClusterResponseBodyData();
	        }

	        ApplicationLauncher.logger.debug("getInputDataFromBay: isResponseReceived : test1");
	        if (clusterResponseData.getStatusCode().equals(ConstantConveyor.HTTP_RESPONSE_SUCCESS)) {
	            ApplicationLauncher.logger.debug("getInputDataFromBay: isResponseReceived : test2");
	            try {
	                statusResponse = clusterResponseData.getJsonBodyResponse().get(inputPortId).toString();
	                ApplicationLauncher.logger.debug("getInputDataFromBay: isResponseReceived : statusResponse: " + statusResponse);
	            } catch (Exception e) {
	                e.printStackTrace();
	                ApplicationLauncher.logger.error("getInputDataFromBay: Exception1 : " + e.getMessage());
	            }
	        } else {
	            ApplicationLauncher.logger.debug("getInputDataFromBay: isResponseReceived : test3");
	        }
	    } else {
	        ApplicationLauncher.logger.debug("getInputDataFromBay: response failed: " + inputPortId);
	        statusResponse = inputPortId;
	    }

	    return statusResponse;
	}


	public String getInputDataFromBay(String clusterId, String bayId,String inputPortId) {

		boolean status = false;
		String statusResponse = "";
		ConveyorClientManager cluster1ClientManager = ConveyorClientManager.getInstance();
		String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
		ClusterServer clusterServer = getServerDetails( terminalId, clusterId);
		if(clusterServer== null){

			ApplicationLauncher.logger.debug("getInputDataFromBay: cluster ip address details not found for terminalId:  " + terminalId + " , clusterId: " + clusterId);

			return null;
		}
		//cluster1ClientManager.getBayData(clusterServer,clusterId, bayId,  inputPortId);
		String dummyOutputValue= "";
		boolean setOutput = false;
		cluster1ClientManager.messageBayData(setOutput,clusterServer,clusterId, bayId,  inputPortId,dummyOutputValue);

		//ApplicationHomeController.update_left_status("Awaiting Device Response",ConstantApp.LEFT_STATUS_DEBUG);
		//Sleep(8000);
		RestApiJsonBodyResponse clusterResponseData = new RestApiJsonBodyResponse();
		ApplicationLauncher.logger.debug("getInputDataFromBay: Wait Time Entry");
		cluster1ClientManager.getAsyncConvClient().WaitForServerResponse(8);
		ApplicationLauncher.logger.debug("getInputDataFromBay: Wait Time Exit");
		if(cluster1ClientManager.getAsyncConvClient().isResponseReceived()){ //validate for server access
			//ApplicationHomeController.update_left_status("Device Connected",ConstantApp.LEFT_STATUS_DEBUG);
			//clusterResponseData = 
			clusterResponseData = cluster1ClientManager.getAsyncConvClient().getRestApiClusterResponseBodyData();
			ApplicationLauncher.logger.debug("getInputDataFromBay: isResponseReceived : test1");
			if(clusterResponseData.getStatusCode().equals(ConstantConveyor.HTTP_RESPONSE_SUCCESS)){
				ApplicationLauncher.logger.debug("getInputDataFromBay: isResponseReceived : test2");
				//clusterResponseData = cluster1ClientManager.getAsyncConvClient().getRestApiClusterResponseBodyData();
				ApplicationLauncher.logger.debug("getInputDataFromBay1: "+inputPortId+" : getDevice Data: "+ clusterResponseData.getJsonBodyResponse().get(inputPortId));
				try{
					statusResponse = clusterResponseData.getJsonBodyResponse().get(inputPortId).toString();
					ApplicationLauncher.logger.debug("getInputDataFromBay: isResponseReceived : statusResponse: " + statusResponse);
				}catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("getInputDataFromBay1: Exception1 : "+e.getMessage());
				}
				//String responseData = cluster1ClientManager.getAsyncConvClient().getResponseData();


				status = true;
			}else{
				ApplicationLauncher.logger.debug("getInputDataFromBay: isResponseReceived : test3");
			}



		}else{

			ApplicationLauncher.logger.debug("getInputDataFromBay1: : response failed: "+inputPortId);
			statusResponse = inputPortId;
			Platform.runLater(()->{
				ref_txtAreaResponseDisplay.setText(ref_txtAreaResponseDisplay.getText()+"\n"+inputPortId+"-no response");
			});
			ApplicationHomeController.update_left_status("Device Connection Failed "+inputPortId+" :",ConstantApp.LEFT_STATUS_DEBUG);
		}
		return statusResponse;
	}

	public String getInputDataFromBayV2(IoPortInfo io_portInfo) {
		
		String clusterId = io_portInfo.getClusterId();
		String bayId = io_portInfo.getBayId();
		String inputPortId = io_portInfo.getPortId();
		ApplicationLauncher.logger.debug("getInputDataFromBayV2: Entry: " + inputPortId);
		String statusResponse = "";
		ConveyorClientManagerCluster2 cluster2ClientManager = ConveyorClientManagerCluster2.getInstance();
		String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
		ClusterServer clusterServer = getServerDetails(terminalId, clusterId);

		if (clusterServer == null) {
			ApplicationLauncher.logger.debug("getInputDataFromBayV2: cluster ip address details not found for terminalId: " + terminalId + " , clusterId: " + clusterId);
			return null;
		}

		String dummyOutputValue = "";
		boolean setOutput = false;
		cluster2ClientManager.messageBayData(setOutput, clusterServer, clusterId, bayId, inputPortId, dummyOutputValue);

		ApplicationLauncher.logger.debug("getInputDataFromBayV2: Wait Time Entry: " + inputPortId);
		boolean isResponseReceived = false;

		if (ProcalFeatureEnable.CONVEYOR_REST_CLIENT_ENABLED) {
			//cluster1ClientManager.getRestConvClient().WaitForServerResponse(8);
			isResponseReceived = cluster2ClientManager.getRestConvClient().isResponseReceived();
		} else {
			cluster2ClientManager.getAsyncConvClient().WaitForServerResponse(8);
			isResponseReceived = cluster2ClientManager.getAsyncConvClient().isResponseReceived();
		}

		ApplicationLauncher.logger.debug("getInputDataFromBayV2: Wait Time Exit: " + inputPortId);

		if (isResponseReceived) {
			RestApiJsonBodyResponse clusterResponseData;
			if (ProcalFeatureEnable.CONVEYOR_REST_CLIENT_ENABLED) {
				clusterResponseData = cluster2ClientManager.getRestConvClient().getRestApiClusterResponseBodyData();
			} else {
				clusterResponseData = cluster2ClientManager.getAsyncConvClient().getRestApiClusterResponseBodyData();
			}

			ApplicationLauncher.logger.debug("getInputDataFromBayV2: isResponseReceived : test1 : " + inputPortId);
			if (clusterResponseData.getStatusCode().equals(ConstantConveyor.HTTP_RESPONSE_SUCCESS)) {
				ApplicationLauncher.logger.debug("getInputDataFromBayV2: isResponseReceived : test2: " + inputPortId);
				try {
					ApplicationLauncher.logger.debug("getInputDataFromBayV2: isResponseReceived : inputPortId: " + inputPortId);
					statusResponse = clusterResponseData.getJsonBodyResponse().get(inputPortId).toString();
					ApplicationLauncher.logger.debug("getInputDataFromBayV2: isResponseReceived : statusResponse: " + statusResponse);
				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger.error("getInputDataFromBayV2-1: Exception1 : " + e.getMessage() + " : inputPortId: " + inputPortId);
				}
			} else {
				ApplicationLauncher.logger.debug("getInputDataFromBayV2: isResponseReceived : test3");
			}
		} else {
			ApplicationLauncher.logger.debug("getInputDataFromBayV2-1: response failed: " + inputPortId);
			statusResponse = inputPortId;
		}

		return statusResponse;
	}



	public String getInputDataFromBayV2(IoPortInfo io_portInfo) {


		String clusterId = io_portInfo.getClusterId();
		String bayId = io_portInfo.getBayId();
		String inputPortId = io_portInfo.getPortId();
		boolean status = false;
		String statusResponse = "";
		ConveyorClientManager cluster1ClientManager = ConveyorClientManager.getInstance();
		String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
		ClusterServer clusterServer = getServerDetails( terminalId, clusterId);
		if(clusterServer== null){

			ApplicationLauncher.logger.debug("getInputDataFromBayV2: cluster ip address details not found for terminalId:  " + terminalId + " , clusterId: " + clusterId);

			return null;
		}
		//cluster1ClientManager.getBayData(clusterServer,clusterId, bayId,  inputPortId);
		String dummyOutputValue= "";
		boolean setOutput = false;
		cluster1ClientManager.messageBayData(setOutput,clusterServer,clusterId, bayId,  inputPortId,dummyOutputValue);
		//ApplicationHomeController.update_left_status("Awaiting Device Response",ConstantApp.LEFT_STATUS_DEBUG);
		//Sleep(8000);
		RestApiJsonBodyResponse clusterResponseData = new RestApiJsonBodyResponse();
		ApplicationLauncher.logger.debug("getInputDataFromBayV2: Wait Time Entry");
		cluster1ClientManager.getAsyncConvClient().WaitForServerResponse(8);
		ApplicationLauncher.logger.debug("getInputDataFromBayV2: Wait Time Exit");
		if(cluster1ClientManager.getAsyncConvClient().isResponseReceived()){ //validate for server access
			//ApplicationHomeController.update_left_status("Device Connected",ConstantApp.LEFT_STATUS_DEBUG);
			//clusterResponseData = 
			clusterResponseData = cluster1ClientManager.getAsyncConvClient().getRestApiClusterResponseBodyData();
			ApplicationLauncher.logger.debug("getInputDataFromBayV2: isResponseReceived : test1");
			if(clusterResponseData.getStatusCode().equals(ConstantConveyor.HTTP_RESPONSE_SUCCESS)){
				ApplicationLauncher.logger.debug("getInputDataFromBayV2: isResponseReceived : test2");
				//clusterResponseData = cluster1ClientManager.getAsyncConvClient().getRestApiClusterResponseBodyData();
				ApplicationLauncher.logger.debug("getInputDataFromBayV2-1: "+inputPortId+" : getDevice Data: "+ clusterResponseData.getJsonBodyResponse().get(inputPortId));
				try{
					statusResponse = clusterResponseData.getJsonBodyResponse().get(inputPortId).toString();
					ApplicationLauncher.logger.debug("getInputDataFromBayV2: isResponseReceived : statusResponse: " + statusResponse);
				}catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("getInputDataFromBayV2-1: Exception1 : "+e.getMessage());
				}
				//String responseData = cluster1ClientManager.getAsyncConvClient().getResponseData();


				status = true;
			}else{
				ApplicationLauncher.logger.debug("getInputDataFromBayV2: isResponseReceived : test3");
			}



		}else{

			ApplicationLauncher.logger.debug("getInputDataFromBayV2-1: : response failed: "+inputPortId);
			statusResponse = inputPortId;
			Platform.runLater(()->{
				ref_txtAreaResponseDisplay.setText(ref_txtAreaResponseDisplay.getText()+"\n"+inputPortId+"-no response");
			});
			ApplicationHomeController.update_left_status("Device Connection Failed "+inputPortId+" :",ConstantApp.LEFT_STATUS_DEBUG);
		}
		return statusResponse;
	}
	
	public String getPulseCounterStatusFromBay(String clusterId, String bayId, String inputPortId) {
	    ApplicationLauncher.logger.debug("getPulseCounterStatusFromBay: inputPortId: " + inputPortId);
	    String pulseCounter = "";
	    String statusResponse = "";
	    String testStatus = "";
	    ConveyorClientManager cluster1ClientManager = ConveyorClientManager.getInstance();
	    String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
	    ClusterServer clusterServer = getServerDetails(terminalId, clusterId);

	    if (clusterServer == null) {
	        ApplicationLauncher.logger.debug("getPulseCounterStatusFromBay: cluster IP address details not found for terminalId: " + terminalId + " , clusterId: " + clusterId);
	        return null;
	    }

	    boolean setOutput = false;
	    String dummyOutputValue = "";
	    cluster1ClientManager.messageBayData(setOutput, clusterServer, clusterId, bayId, inputPortId, dummyOutputValue);
	    
	    ApplicationLauncher.logger.debug("getPulseCounterStatusFromBay: Wait Time Entry");
	    boolean isResponseReceived = false;

	    if (ProcalFeatureEnable.CONVEYOR_REST_CLIENT_ENABLED) {
	        cluster1ClientManager.getRestConvClient().WaitForServerResponse(8);
	        isResponseReceived = cluster1ClientManager.getRestConvClient().isResponseReceived();
	    } else {
	        cluster1ClientManager.getAsyncConvClient().WaitForServerResponse(8);
	        isResponseReceived = cluster1ClientManager.getAsyncConvClient().isResponseReceived();
	    }

	    ApplicationLauncher.logger.debug("getPulseCounterStatusFromBay: Wait Time Exit");

	    if (isResponseReceived) {
	        RestApiJsonBodyResponse clusterResponseData;
	        if (ProcalFeatureEnable.CONVEYOR_REST_CLIENT_ENABLED) {
	            clusterResponseData = cluster1ClientManager.getRestConvClient().getRestApiClusterResponseBodyData();
	        } else {
	            clusterResponseData = cluster1ClientManager.getAsyncConvClient().getRestApiClusterResponseBodyData();
	        }

	        ApplicationLauncher.logger.debug("getPulseCounterStatusFromBay: isResponseReceived : test1");
	        if (clusterResponseData.getStatusCode().equals(ConstantConveyor.HTTP_RESPONSE_SUCCESS)) {
	            ApplicationLauncher.logger.debug("getPulseCounterStatusFromBay: isResponseReceived : test2");
	            try {
	                statusResponse = clusterResponseData.getJsonBodyResponse().get(inputPortId).toString();
	                testStatus = clusterResponseData.getJsonBodyResponse().get("status").toString();
	                ApplicationLauncher.logger.debug("getPulseCounterStatusFromBay: statusResponse: " + statusResponse);
	                ApplicationLauncher.logger.debug("getPulseCounterStatusFromBay: testStatus: " + testStatus);

	                if (testStatus.equals(ConstantConveyorCluster.TEST_STATUS_COMPLETED)) {
	                    pulseCounter = "6666";
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	                ApplicationLauncher.logger.error("getPulseCounterStatusFromBay: Exception1 : " + e.getMessage());
	            }
	        } else {
	            ApplicationLauncher.logger.debug("getPulseCounterStatusFromBay: isResponseReceived : test3");
	        }
	    } else {
	        ApplicationLauncher.logger.debug("getPulseCounterStatusFromBay: response failed: " + inputPortId);
	    }

	    return pulseCounter;
	}

	public String getPulseCounterStatusFromBay(String clusterId, String bayId,String inputPortId) {

		boolean status = false;
		String statusResponse = "";
		String pulseCounter = "";
		String testStatus = "";
		ConveyorClientManager cluster1ClientManager = ConveyorClientManager.getInstance();
		String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
		ClusterServer clusterServer = getServerDetails( terminalId, clusterId);
		if(clusterServer== null){

			ApplicationLauncher.logger.debug("getPulseCounterStatusFromBay: cluster ip address details not found for terminalId:  " + terminalId + " , clusterId: " + clusterId);

			return null;
		}
		//cluster1ClientManager.getBayData(clusterServer,clusterId, bayId,  inputPortId);
		String dummyOutputValue= "";
		boolean setOutput = false;
		cluster1ClientManager.messageBayData(setOutput,clusterServer,clusterId, bayId,  inputPortId,dummyOutputValue);
		//ApplicationHomeController.update_left_status("Awaiting Device Response",ConstantApp.LEFT_STATUS_DEBUG);
		//Sleep(8000);
		RestApiJsonBodyResponse clusterResponseData = new RestApiJsonBodyResponse();
		cluster1ClientManager.getAsyncConvClient().WaitForServerResponse(8);
		ApplicationLauncher.logger.debug("getPulseCounterStatusFromBay: "+inputPortId+" : isResponseReceived:  "+ cluster1ClientManager.getAsyncConvClient().isResponseReceived());
		if(cluster1ClientManager.getAsyncConvClient().isResponseReceived()){ //validate for server access
			//ApplicationHomeController.update_left_status("Device Connected",ConstantApp.LEFT_STATUS_DEBUG);
			ApplicationLauncher.logger.debug("getPulseCounterStatusFromBay: test1");
			clusterResponseData = cluster1ClientManager.getAsyncConvClient().getRestApiClusterResponseBodyData();
			if(clusterResponseData.getStatusCode().equals(ConstantConveyor.HTTP_RESPONSE_SUCCESS)){
				ApplicationLauncher.logger.debug("getPulseCounterStatusFromBay: test2");
				//clusterResponseData = cluster1ClientManager.getAsyncConvClient().getRestApiClusterResponseBodyData();
				ApplicationLauncher.logger.debug("getPulseCounterStatusFromBay: "+inputPortId+" : getDevice Data: "+ clusterResponseData.getJsonBodyResponse().get(inputPortId));
				try{
					statusResponse = clusterResponseData.getJsonBodyResponse().get(inputPortId).toString();
					ApplicationLauncher.logger.debug("getPulseCounterStatusFromBay: statusResponse: " + statusResponse);

					testStatus = clusterResponseData.getJsonBodyResponse().get("status").toString();//clusterResponseData.getStatus();
					ApplicationLauncher.logger.debug("getPulseCounterStatusFromBay: testStatus: " + testStatus);
					ApplicationLauncher.logger.debug("getPulseCounterStatusFromBay: TEST_STATUS_COMPLETED: " + ConstantConveyorCluster.TEST_STATUS_COMPLETED);
					if(testStatus.equals(ConstantConveyorCluster.TEST_STATUS_COMPLETED)) {
						//pulseCounter = clusterResponseData.getJsonBodyResponse().get(inputPortId+ConstantConveyorCluster.FT_PULSE_COUNTER_POST_FIX).toString();
						pulseCounter = "6666";
					}
				}catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("getPulseCounterStatusFromBay: Exception1 : "+e.getMessage());
				}
				//String responseData = cluster1ClientManager.getAsyncConvClient().getResponseData();


				status = true;
			}else{
				ApplicationLauncher.logger.debug("getPulseCounterStatusFromBay: test3");
			}



		}else{

			ApplicationLauncher.logger.debug("getPulseCounterStatusFromBay: : response failed: "+inputPortId);
			Platform.runLater(()->{
				ref_txtAreaResponseDisplay.setText(ref_txtAreaResponseDisplay.getText()+"\n"+inputPortId+"-no response");
			});
			ApplicationHomeController.update_left_status("Device Connection Failed "+inputPortId+" :",ConstantApp.LEFT_STATUS_DEBUG);
		}
		return pulseCounter;
	}

	//========================================================================================

	public String setOutputDataToBay(String clusterId, String bayId, String outputPortId, String outputStatus) {
		String statusResponse = "";
		ConveyorClientManager cluster1ClientManager = ConveyorClientManager.getInstance();
		RestApiJsonBodyResponse clusterResponseData = new RestApiJsonBodyResponse();
		String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
		ClusterServer clusterServer = getServerDetails(terminalId, clusterId);

		if (clusterServer == null) {
			ApplicationLauncher.logger.debug("setOutputDataToBay: cluster ip address details not found for terminalId: " + terminalId + " , clusterId: " + clusterId);
			return null;
		}

		boolean setOutput = true;
		cluster1ClientManager.messageBayData(setOutput, clusterServer, clusterId, bayId, outputPortId, outputStatus);

		boolean isResponseReceived = false;
		if (ProcalFeatureEnable.CONVEYOR_REST_CLIENT_ENABLED) {
			cluster1ClientManager.getRestConvClient().WaitForServerResponse(8);
			isResponseReceived = cluster1ClientManager.getRestConvClient().isResponseReceived();
		} else {
			cluster1ClientManager.getAsyncConvClient().WaitForServerResponse(8);
			isResponseReceived = cluster1ClientManager.getAsyncConvClient().isResponseReceived();
		}

		if (isResponseReceived) {
			if (ProcalFeatureEnable.CONVEYOR_REST_CLIENT_ENABLED) {
				clusterResponseData = cluster1ClientManager.getRestConvClient().getRestApiClusterResponseBodyData();
			} else {
				clusterResponseData = cluster1ClientManager.getAsyncConvClient().getRestApiClusterResponseBodyData();
			}

			if (clusterResponseData != null && clusterResponseData.getStatusCode().equals(ConstantConveyor.HTTP_RESPONSE_SUCCESS)) {
				try {
					if (clusterResponseData.getJsonBodyResponse() != null) {
						statusResponse = clusterResponseData.getJsonBodyResponse().get(outputPortId).toString();
					} else {
						ApplicationLauncher.logger.error("setOutputDataToBay: JsonBodyResponse is null for outputPortId: " + outputPortId);
					}
				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger.error("setOutputDataToBay: Exception1 : " + e.getMessage());
				}
			}
		} else {
			ApplicationLauncher.logger.debug("setOutputDataToBay: response failed : " + outputPortId);
		}

		return statusResponse;
	}


	
	public String  setOutputDataToBay(String clusterId, String bayId,String outputPortId, String outputStatus) {

		boolean status = false;
		String statusResponse = "";
		ConveyorClientManager cluster1ClientManager = ConveyorClientManager.getInstance();
		RestApiJsonBodyResponse clusterResponseData = new RestApiJsonBodyResponse();
		String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
		ClusterServer clusterServer = getServerDetails( terminalId, clusterId);
		if(clusterServer== null){

			ApplicationLauncher.logger.debug("setOutputDataToBay: cluster ip address details not found for terminalId:  " + terminalId + " , clusterId: " + clusterId);

			return null;
		}
		//cluster1ClientManager.setBayData(clusterServer,clusterId, bayId,  outputPortId, outputStatus);
		boolean setOutput = true;
		cluster1ClientManager.messageBayData(setOutput,clusterServer,clusterId, bayId,  outputPortId,outputStatus);
		cluster1ClientManager.getAsyncConvClient().WaitForServerResponse(8);
		if(cluster1ClientManager.getAsyncConvClient().isResponseReceived()){ //validate for server access


			//RestApiClusterResponse clusterResponseData = new RestApiClusterResponse();
			clusterResponseData = cluster1ClientManager.getAsyncConvClient().getRestApiClusterResponseBodyData();
			//ApplicationLauncher.logger.debug("setOutputDataToBay: "+outputPortId+" : getDevice Data: "+ clusterResponseData.getDevice());
			//ApplicationLauncher.logger.debug("setOutputDataToBay: "+outputPortId+" : getStatus Data: "+ clusterResponseData.getStatus());

			if(clusterResponseData.getStatusCode().equals(ConstantConveyor.HTTP_RESPONSE_SUCCESS)){
				status = true;
				try{
					statusResponse = clusterResponseData.getJsonBodyResponse().get(outputPortId).toString();
				}catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("setOutputDataToBay: Exception1 : "+e.getMessage());
				}
			}
			//String responseData = cluster1ClientManager.getAsyncConvClient().getResponseData();




		}else{
			ApplicationLauncher.logger.debug("setOutputDataToBay: response failed : "+outputPortId);

		}
		return statusResponse;
	}

	public static void init() {
		// TODO Auto-generated method stub

		ApplicationLauncher.logger.debug("BayUtils: init: loadDataFromConfig: Entry");
		//ref_cmbBxDut1ClusterId.getItems().clear();

		// ref_cmbBxDutClusterId1;
		//ref_cmbBxDutBayId1;

		//=======================================	 


		for(Terminal eachTerminal: getBayConfigModel().getTerminal()){
			if(eachTerminal.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID)){
				for(ClusterDetail eachClusterDetail: eachTerminal.getClusterDetails()){


					getClusterNameIdListMap().put(eachClusterDetail.getName(), eachClusterDetail.getClusterId());
					ArrayList<String> bayList = new ArrayList<String>();
					getClusterBayNameListMap().put(eachClusterDetail.getName(), bayList);


					for (Bay eachBay: eachClusterDetail.getBay()){

						//ref_cmbBxBaySelection.getItems().add(eachBay.getBayName());
						bayList.add(eachBay.getBayName());
						getClusterBayNameListMap().put(eachClusterDetail.getName(), bayList);
						Map<String,String> bayNameIdMap = new HashMap<String,String>();
						bayNameIdMap.put(eachBay.getBayName(), eachBay.getBayId());
						getClusterBayNameIdMap().put(eachClusterDetail.getName()+"_"+eachBay.getBayName(), eachBay.getBayId());
						//getClusterBayNameIdMap().put(eachClusterDetail.getName(), bayNameIdMap);
						//ApplicationLauncher.logger.debug("loadDataFromConfig : getClusterBayNameIdMap().get(clusterName)-1 :"+ getClusterBayNameIdMap());

					}
				}


			}

		}

		for(Terminal eachTerminal: getBayConfigModel().getTerminal()){
			String clusterId = "";
			String bayId = "";
			if(eachTerminal.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID)){
				for(ClusterDetail eachClusterDetail: eachTerminal.getClusterDetails()){
					clusterId = getClusterNameIdListMap().get(eachClusterDetail.getName());
					for (Bay eachBay: eachClusterDetail.getBay()){
						bayId = getClusterBayNameIdMap().get(eachClusterDetail.getName()+"_"+eachBay.getBayName());
						ArrayList<String> qrPositionNoList = new ArrayList<String>();
						for(QrScanner eachQrDevice: eachTerminal.getQrScanner()){
							if( (eachQrDevice.getClusterId().equals(clusterId)) && (eachQrDevice.getBayId().equals(bayId)) ){
								//getClusterBayNamePositionListMap().put
								qrPositionNoList.add(eachQrDevice.getPositionId());
								getQrClusterBayPositionNoCnameMap().put(
										eachClusterDetail.getName()+"_"+eachBay.getBayName()+"_"+eachQrDevice.getPositionId(), 
										eachQrDevice.getPortName());
								getQrClusterBayPositionNoDeviceIdMap().put(
										eachClusterDetail.getName()+"_"+eachBay.getBayName()+"_"+eachQrDevice.getPositionId(), 
										eachQrDevice.getDeviceId());

							}

						}
						if(qrPositionNoList.size()>0) {
							getQrClusterBayNamePositionListMap().put(eachClusterDetail.getName()+"_"+eachBay.getBayName(),qrPositionNoList);
						}


						/////////////////////////////////////



						ArrayList<String> dutPositionNoList = new ArrayList<String>();
						for(DutDevice eachDutDevice: eachTerminal.getDutDevice()){
							if( (eachDutDevice.getClusterId().equals(clusterId)) && (eachDutDevice.getBayId().equals(bayId)) ){
								//getClusterBayNamePositionListMap().put
								dutPositionNoList.add(eachDutDevice.getPositionId());
								getDutClusterBayPositionNoCnameMap().put(
										eachClusterDetail.getName()+"_"+eachBay.getBayName()+"_"+eachDutDevice.getPositionId(), 
										eachDutDevice.getPortName());
								getDutClusterBayPositionNoDeviceIdMap().put(
										eachClusterDetail.getName()+"_"+eachBay.getBayName()+"_"+eachDutDevice.getPositionId(), 
										eachDutDevice.getDeviceId());

							}

						}
						if(dutPositionNoList.size()>0) {
							getDutClusterBayNamePositionListMap().put(eachClusterDetail.getName()+"_"+eachBay.getBayName(),dutPositionNoList);
						}
						//////////////////////////////////////////////////


						/////////////////////////////////////



						ArrayList<String> megaOhmPositionNoList = new ArrayList<String>();
						ArrayList<String> megaOhmAddressList = new ArrayList<String>();
						for(MegaOhmMeter eachMegaOhmMeterDevice: eachTerminal.getMegaOhmMeter()){
							if( (eachMegaOhmMeterDevice.getClusterId().equals(clusterId)) && (eachMegaOhmMeterDevice.getBayId().equals(bayId)) ){
								//getClusterBayNamePositionListMap().put
								megaOhmPositionNoList.add(eachMegaOhmMeterDevice.getPositionId());
								getMegaOhmMeterClusterBayPositionNoCnameMap().put(
										eachClusterDetail.getName()+"_"+eachBay.getBayName()+"_"+eachMegaOhmMeterDevice.getPositionId(), 
										eachMegaOhmMeterDevice.getPortName());
								getMegaOhmMeterClusterBayPositionNoDeviceIdMap().put(
										eachClusterDetail.getName()+"_"+eachBay.getBayName()+"_"+eachMegaOhmMeterDevice.getPositionId(), 
										eachMegaOhmMeterDevice.getDeviceId());
								ApplicationLauncher.logger.debug("loadDataFromConfig-M : getDeviceId :"+ eachMegaOhmMeterDevice.getDeviceId());
								if(eachMegaOhmMeterDevice.isRs485Enabled()) {
									megaOhmAddressList = eachMegaOhmMeterDevice.getRs485DeviceIdList();
									ApplicationLauncher.logger.debug("loadDataFromConfig-M : addressList :"+ megaOhmAddressList.toString());
								}

								if(megaOhmAddressList.size()>0) {
									String formattedPositionId = String.format("%02d", Integer.parseInt(eachMegaOhmMeterDevice.getPositionId()));
									getMegaOhmMeterClusterBayPositionNoAddressListMap().put(eachClusterDetail.getName()+"_"+eachBay.getBayName()+"_"+formattedPositionId,megaOhmAddressList);
								}

							}

						}
						if(megaOhmPositionNoList.size()>0) {
							getMegaOhmMeterClusterBayNamePositionListMap().put(eachClusterDetail.getName()+"_"+eachBay.getBayName(),megaOhmPositionNoList);
						}

						//////////////////////////////////////////////////

						/////////////////////////////////////



						ArrayList<String> voltPositionNoList = new ArrayList<String>();
						ArrayList<String> voltAddressList = new ArrayList<String>();
						for(VoltMeter eachVoltMeterDevice: eachTerminal.getVoltMeter()){
							if( (eachVoltMeterDevice.getClusterId().equals(clusterId)) && (eachVoltMeterDevice.getBayId().equals(bayId)) ){
								//getClusterBayNamePositionListMap().put
								voltPositionNoList.add(eachVoltMeterDevice.getPositionId());
								getVoltMeterClusterBayPositionNoCnameMap().put(
										eachClusterDetail.getName()+"_"+eachBay.getBayName()+"_"+eachVoltMeterDevice.getPositionId(), 
										eachVoltMeterDevice.getPortName());
								getVoltMeterClusterBayPositionNoDeviceIdMap().put(
										eachClusterDetail.getName()+"_"+eachBay.getBayName()+"_"+eachVoltMeterDevice.getPositionId(), 
										eachVoltMeterDevice.getDeviceId());
								ApplicationLauncher.logger.debug("loadDataFromConfig-V : getDeviceId :"+ eachVoltMeterDevice.getDeviceId());
								if(eachVoltMeterDevice.isRs485Enabled()) {
									voltAddressList = eachVoltMeterDevice.getRs485DeviceIdList();
									ApplicationLauncher.logger.debug("loadDataFromConfig-V : addressList :"+ voltAddressList.toString());
								}

								if(voltAddressList.size()>0) {
									String formattedPositionId = String.format("%02d", Integer.parseInt(eachVoltMeterDevice.getPositionId()));
									getVoltMeterClusterBayPositionNoAddressListMap().put(eachClusterDetail.getName()+"_"+eachBay.getBayName()+"_"+formattedPositionId,voltAddressList);
								}

							}

						}
						if(voltPositionNoList.size()>0) {
							getVoltMeterClusterBayNamePositionListMap().put(eachClusterDetail.getName()+"_"+eachBay.getBayName(),voltPositionNoList);
						}
						//////////////////////////////////////////////////


						/////////////////////////////////////



						ArrayList<String> lduPositionNoList = new ArrayList<String>();
						ArrayList<String> lduAddressList = new ArrayList<String>();
						for(Ldu eachLduDevice: eachTerminal.getLdu()){
							if( (eachLduDevice.getClusterId().equals(clusterId)) && (eachLduDevice.getBayId().equals(bayId)) ){
								//getClusterBayNamePositionListMap().put
								lduPositionNoList.add(eachLduDevice.getPositionId());
								getLduClusterBayPositionNoCnameMap().put(
										eachClusterDetail.getName()+"_"+eachBay.getBayName()+"_"+eachLduDevice.getPositionId(), 
										eachLduDevice.getPortName());
								getLduClusterBayPositionNoDeviceIdMap().put(
										eachClusterDetail.getName()+"_"+eachBay.getBayName()+"_"+eachLduDevice.getPositionId(), 
										eachLduDevice.getDeviceId());
								ApplicationLauncher.logger.debug("loadDataFromConfig-L : getDeviceId :"+ eachLduDevice.getDeviceId());
								if(eachLduDevice.isRs485Enabled()) {
									lduAddressList = eachLduDevice.getRs485DeviceIdList();
									ApplicationLauncher.logger.debug("loadDataFromConfig-L : addressList :"+ lduAddressList.toString());
								}

								if(lduAddressList.size()>0) {
									String formattedPositionId = String.format("%02d", Integer.parseInt(eachLduDevice.getPositionId()));
									getLduClusterBayPositionNoAddressListMap().put(eachClusterDetail.getName()+"_"+eachBay.getBayName()+"_"+formattedPositionId,lduAddressList);
								}

							}

						}
						if(lduPositionNoList.size()>0) {
							getLduClusterBayNamePositionListMap().put(eachClusterDetail.getName()+"_"+eachBay.getBayName(),lduPositionNoList);
						}
						//////////////////////////////////////////////////

					}

				}

			}
		}

				getQrClusterBayPositionNoCnameMap().entrySet().forEach((e)->{
			ApplicationLauncher.logger.debug("loadDataFromConfig-2: getQrClusterBayPositionNoCnameMap: key : " + e.getKey() + " -> " + e.getValue());
		});

		getQrClusterBayPositionNoDeviceIdMap().entrySet().forEach((e)->{
			ApplicationLauncher.logger.debug("loadDataFromConfig-Qr2: getQrClusterBayPositionNoDeviceIdMap: key : " + e.getKey() + " -> " + e.getValue());
		});


		getDutClusterBayPositionNoCnameMap().entrySet().forEach((e)->{
			ApplicationLauncher.logger.debug("loadDataFromConfig-2: getDutClusterBayPositionNoCnameMap: key : " + e.getKey() + " -> " + e.getValue());
		});

		getDutClusterBayPositionNoDeviceIdMap().entrySet().forEach((e)->{
			ApplicationLauncher.logger.debug("loadDataFromConfig-Dut2: getDutClusterBayPositionNoDeviceIdMap: key : " + e.getKey() + " -> " + e.getValue());
		});

		getMegaOhmMeterClusterBayNamePositionListMap().entrySet().forEach((e)->{
			ApplicationLauncher.logger.debug("loadDataFromConfig-OhmMeter: getMegaOhmMeterClusterBayNamePositionListMap: key : " + e.getKey() + " -> " + e.getValue());
		});
		getFilteredClusterBayNamePositionListMap().put(ConstantConveyor.DEVICE_TYPE_QR_SCANNER,getQrClusterBayNamePositionListMap());
		getFilteredClusterBayPositionNoCnameMap().put(ConstantConveyor.DEVICE_TYPE_QR_SCANNER,getQrClusterBayPositionNoCnameMap());
		getFilteredClusterBayPositionNoDeviceIdMap().put(ConstantConveyor.DEVICE_TYPE_QR_SCANNER,getQrClusterBayPositionNoDeviceIdMap());

		getFilteredClusterBayNamePositionListMap().put(ConstantConveyor.DEVICE_TYPE_DUT,getDutClusterBayNamePositionListMap());		
		getFilteredClusterBayPositionNoCnameMap().put(ConstantConveyor.DEVICE_TYPE_DUT,getDutClusterBayPositionNoCnameMap());		
		getFilteredClusterBayPositionNoDeviceIdMap().put(ConstantConveyor.DEVICE_TYPE_DUT,getDutClusterBayPositionNoDeviceIdMap());

		getFilteredClusterBayNamePositionListMap().put(ConstantConveyor.DEVICE_TYPE_OHM_METER,getMegaOhmMeterClusterBayNamePositionListMap());		
		getFilteredClusterBayPositionNoCnameMap().put(ConstantConveyor.DEVICE_TYPE_OHM_METER,getMegaOhmMeterClusterBayPositionNoCnameMap());		
		getFilteredClusterBayPositionNoDeviceIdMap().put(ConstantConveyor.DEVICE_TYPE_OHM_METER,getMegaOhmMeterClusterBayPositionNoDeviceIdMap());
		getFilteredClusterBayPositionNoAddressListMap().put(ConstantConveyor.DEVICE_TYPE_OHM_METER,getMegaOhmMeterClusterBayPositionNoAddressListMap());


		getFilteredClusterBayNamePositionListMap().put(ConstantConveyor.DEVICE_TYPE_VOLT_METER,getVoltMeterClusterBayNamePositionListMap());		
		getFilteredClusterBayPositionNoCnameMap().put(ConstantConveyor.DEVICE_TYPE_VOLT_METER,getVoltMeterClusterBayPositionNoCnameMap());		
		getFilteredClusterBayPositionNoDeviceIdMap().put(ConstantConveyor.DEVICE_TYPE_VOLT_METER,getVoltMeterClusterBayPositionNoDeviceIdMap());
		getFilteredClusterBayPositionNoAddressListMap().put(ConstantConveyor.DEVICE_TYPE_VOLT_METER,getVoltMeterClusterBayPositionNoAddressListMap());

		getFilteredClusterBayNamePositionListMap().put(ConstantConveyor.DEVICE_TYPE_LDU,getLduClusterBayNamePositionListMap());		
		getFilteredClusterBayPositionNoCnameMap().put(ConstantConveyor.DEVICE_TYPE_LDU,getLduClusterBayPositionNoCnameMap());		
		getFilteredClusterBayPositionNoDeviceIdMap().put(ConstantConveyor.DEVICE_TYPE_LDU,getLduClusterBayPositionNoDeviceIdMap());
		getFilteredClusterBayPositionNoAddressListMap().put(ConstantConveyor.DEVICE_TYPE_LDU,getLduClusterBayPositionNoAddressListMap());



		getFilteredClusterBayPositionNoAddressListMap().entrySet().forEach((e)->{
			ApplicationLauncher.logger.debug("loadDataFromConfig-OhmMeter: getFilteredClusterBayPositionNoAddressListMap: key : " + e.getKey() + " -> " + e.getValue());
		});

	}

	//========================================================================================


	public static TerminalBayConfigModel getBayConfigModel() {
		return bayConfigModel;
	}
	//========================================================================================

	public static void setBayConfigModel(TerminalBayConfigModel bayConfigModel) {
		BayUtils_Cluster2.bayConfigModel = bayConfigModel;
	}
	//========================================================================================
		public static Map<String, ArrayList<String>> getClusterBayNameListMap() {
		return clusterBayNameListMap;
	}
	public static Map<String, String> getClusterNameIdListMap() {
		return clusterNameIdListMap;
	}
	public static Map<String, String> getClusterBayNameIdMap() {
		return clusterBayNameIdMap;
	}
	public static Map<String, ArrayList<String>> getClusterBayNamePositionListMap() {
		return clusterBayNamePositionListMap;
	}
	public static Map<String, String> getClusterBayPositionNoCnameMap() {
		return clusterBayPositionNoCnameMap;
	}
	public static Map<String, String> getClusterBayPositionNoDeviceIdMap() {
		return clusterBayPositionNoDeviceIdMap;
	}
	public static Map<String, ArrayList<String>> getClusterBayPositionNoAddressListMap() {
		return clusterBayPositionNoAddressListMap;
	}
	public static void setClusterBayNameListMap(Map<String, ArrayList<String>> clusterBayNameListMap) {
		BayUtils.clusterBayNameListMap = clusterBayNameListMap;
	}
	public static void setClusterNameIdListMap(Map<String, String> clusterNameIdListMap) {
		BayUtils.clusterNameIdListMap = clusterNameIdListMap;
	}
	public static void setClusterBayNameIdMap(Map<String, String> clusterBayNameIdMap) {
		BayUtils.clusterBayNameIdMap = clusterBayNameIdMap;
	}
	public static void setClusterBayNamePositionListMap(Map<String, ArrayList<String>> clusterBayNamePositionListMap) {
		BayUtils.clusterBayNamePositionListMap = clusterBayNamePositionListMap;
	}
	public static void setClusterBayPositionNoCnameMap(Map<String, String> clusterBayPositionNoCnameMap) {
		BayUtils.clusterBayPositionNoCnameMap = clusterBayPositionNoCnameMap;
	}
	public static void setClusterBayPositionNoDeviceIdMap(Map<String, String> clusterBayPositionNoDeviceIdMap) {
		BayUtils.clusterBayPositionNoDeviceIdMap = clusterBayPositionNoDeviceIdMap;
	}
	public static void setClusterBayPositionNoAddressListMap(
			Map<String, ArrayList<String>> clusterBayPositionNoAddressListMap) {
		BayUtils.clusterBayPositionNoAddressListMap = clusterBayPositionNoAddressListMap;
	}
	public static Map<String, ArrayList<String>> getClusterBayNameListMap() {
		return clusterBayNameListMap;
	}
	public static Map<String, String> getClusterNameIdListMap() {
		return clusterNameIdListMap;
	}
	public static Map<String, String> getClusterBayNameIdMap() {
		return clusterBayNameIdMap;
	}
	public static Map<String, ArrayList<String>> getQrClusterBayNamePositionListMap() {
		return qrClusterBayNamePositionListMap;
	}
	public static Map<String, String> getQrClusterBayPositionNoCnameMap() {
		return qrClusterBayPositionNoCnameMap;
	}
	public static Map<String, String> getQrClusterBayPositionNoDeviceIdMap() {
		return qrClusterBayPositionNoDeviceIdMap;
	}
	public static void setClusterBayNameListMap(Map<String, ArrayList<String>> clusterBayNameListMap) {
		BayUtils_Cluster2.clusterBayNameListMap = clusterBayNameListMap;
	}
	public static void setClusterNameIdListMap(Map<String, String> clusterNameIdListMap) {
		BayUtils_Cluster2.clusterNameIdListMap = clusterNameIdListMap;
	}
	public static void setClusterBayNameIdMap(Map<String, String> clusterBayNameIdMap) {
		BayUtils_Cluster2.clusterBayNameIdMap = clusterBayNameIdMap;
	}
	public static void setQrClusterBayNamePositionListMap(Map<String, ArrayList<String>> qrClusterBayNamePositionListMap) {
		BayUtils_Cluster2.qrClusterBayNamePositionListMap = qrClusterBayNamePositionListMap;
	}
	public static void setQrClusterBayPositionNoCnameMap(Map<String, String> qrClusterBayPositionNoCnameMap) {
		BayUtils_Cluster2.qrClusterBayPositionNoCnameMap = qrClusterBayPositionNoCnameMap;
	}
	public static void setQrClusterBayPositionNoDeviceIdMap(Map<String, String> qrClusterBayPositionNoDeviceIdMap) {
		BayUtils_Cluster2.qrClusterBayPositionNoDeviceIdMap = qrClusterBayPositionNoDeviceIdMap;
	}
	public static Map<String, ArrayList<String>> getDutClusterBayNamePositionListMap() {
		return dutClusterBayNamePositionListMap;
	}
	public static Map<String, String> getDutClusterBayPositionNoCnameMap() {
		return dutClusterBayPositionNoCnameMap;
	}
	public static Map<String, String> getDutClusterBayPositionNoDeviceIdMap() {
		return dutClusterBayPositionNoDeviceIdMap;
	}
	public static void setDutClusterBayNamePositionListMap(
			Map<String, ArrayList<String>> dutClusterBayNamePositionListMap) {
		BayUtils_Cluster2.dutClusterBayNamePositionListMap = dutClusterBayNamePositionListMap;
	}
	public static void setDutClusterBayPositionNoCnameMap(Map<String, String> dutClusterBayPositionNoCnameMap) {
		BayUtils_Cluster2.dutClusterBayPositionNoCnameMap = dutClusterBayPositionNoCnameMap;
	}
	public static void setDutClusterBayPositionNoDeviceIdMap(Map<String, String> dutClusterBayPositionNoDeviceIdMap) {
		BayUtils_Cluster2.dutClusterBayPositionNoDeviceIdMap = dutClusterBayPositionNoDeviceIdMap;
	}
	public static Map<String, Map<String, ArrayList<String>>> getFilteredClusterBayNamePositionListMap() {
		return filteredClusterBayNamePositionListMap;
	}
	public static Map<String, Map<String, String>> getFilteredClusterBayPositionNoCnameMap() {
		return filteredClusterBayPositionNoCnameMap;
	}
	public static Map<String, Map<String, String>> getFilteredClusterBayPositionNoDeviceIdMap() {
		return filteredClusterBayPositionNoDeviceIdMap;
	}
	public static void setFilteredClusterBayNamePositionListMap(
			Map<String, Map<String, ArrayList<String>>> filteredClusterBayNamePositionListMap) {
		BayUtils_Cluster2.filteredClusterBayNamePositionListMap = filteredClusterBayNamePositionListMap;
	}
	public static void setFilteredClusterBayPositionNoCnameMap(
			Map<String, Map<String, String>> filteredClusterBayPositionNoCnameMap) {
		BayUtils_Cluster2.filteredClusterBayPositionNoCnameMap = filteredClusterBayPositionNoCnameMap;
	}
	public static void setFilteredClusterBayPositionNoDeviceIdMap(
			Map<String, Map<String, String>> filteredClusterBayPositionNoDeviceIdMap) {
		BayUtils_Cluster2.filteredClusterBayPositionNoDeviceIdMap = filteredClusterBayPositionNoDeviceIdMap;
	}
	public static Map<String, ArrayList<String>> getMegaOhmMeterClusterBayNamePositionListMap() {
		return megaOhmMeterClusterBayNamePositionListMap;
	}
	public static Map<String, String> getMegaOhmMeterClusterBayPositionNoCnameMap() {
		return megaOhmMeterClusterBayPositionNoCnameMap;
	}
	public static Map<String, String> getMegaOhmMeterClusterBayPositionNoDeviceIdMap() {
		return megaOhmMeterClusterBayPositionNoDeviceIdMap;
	}
	public static Map<String, ArrayList<String>> getVoltMeterClusterBayNamePositionListMap() {
		return voltMeterClusterBayNamePositionListMap;
	}
	public static Map<String, String> getVoltMeterClusterBayPositionNoCnameMap() {
		return voltMeterClusterBayPositionNoCnameMap;
	}
	public static Map<String, String> getVoltMeterClusterBayPositionNoDeviceIdMap() {
		return voltMeterClusterBayPositionNoDeviceIdMap;
	}
	public static Map<String, ArrayList<String>> getLduClusterBayNamePositionListMap() {
		return lduClusterBayNamePositionListMap;
	}
	public static Map<String, String> getLduClusterBayPositionNoCnameMap() {
		return lduClusterBayPositionNoCnameMap;
	}
	public static Map<String, String> getLduClusterBayPositionNoDeviceIdMap() {
		return lduClusterBayPositionNoDeviceIdMap;
	}
	public static void setMegaOhmMeterClusterBayNamePositionListMap(
			Map<String, ArrayList<String>> megaOhmMeterClusterBayNamePositionListMap) {
		BayUtils_Cluster2.megaOhmMeterClusterBayNamePositionListMap = megaOhmMeterClusterBayNamePositionListMap;
	}
	public static void setMegaOhmMeterClusterBayPositionNoCnameMap(
			Map<String, String> megaOhmMeterClusterBayPositionNoCnameMap) {
		BayUtils_Cluster2.megaOhmMeterClusterBayPositionNoCnameMap = megaOhmMeterClusterBayPositionNoCnameMap;
	}
	public static void setMegaOhmMeterClusterBayPositionNoDeviceIdMap(
			Map<String, String> megaOhmMeterClusterBayPositionNoDeviceIdMap) {
		BayUtils_Cluster2.megaOhmMeterClusterBayPositionNoDeviceIdMap = megaOhmMeterClusterBayPositionNoDeviceIdMap;
	}
	public static void setVoltMeterClusterBayNamePositionListMap(
			Map<String, ArrayList<String>> voltMeterClusterBayNamePositionListMap) {
		BayUtils_Cluster2.voltMeterClusterBayNamePositionListMap = voltMeterClusterBayNamePositionListMap;
	}
	public static void setVoltMeterClusterBayPositionNoCnameMap(Map<String, String> voltMeterClusterBayPositionNoCnameMap) {
		BayUtils_Cluster2.voltMeterClusterBayPositionNoCnameMap = voltMeterClusterBayPositionNoCnameMap;
	}
	public static void setVoltMeterClusterBayPositionNoDeviceIdMap(
			Map<String, String> voltMeterClusterBayPositionNoDeviceIdMap) {
		BayUtils_Cluster2.voltMeterClusterBayPositionNoDeviceIdMap = voltMeterClusterBayPositionNoDeviceIdMap;
	}
	public static void setLduClusterBayNamePositionListMap(
			Map<String, ArrayList<String>> lduClusterBayNamePositionListMap) {
		BayUtils_Cluster2.lduClusterBayNamePositionListMap = lduClusterBayNamePositionListMap;
	}
	public static void setLduClusterBayPositionNoCnameMap(Map<String, String> lduClusterBayPositionNoCnameMap) {
		BayUtils_Cluster2.lduClusterBayPositionNoCnameMap = lduClusterBayPositionNoCnameMap;
	}
	public static void setLduClusterBayPositionNoDeviceIdMap(Map<String, String> lduClusterBayPositionNoDeviceIdMap) {
		BayUtils_Cluster2.lduClusterBayPositionNoDeviceIdMap = lduClusterBayPositionNoDeviceIdMap;
	}
	public static Map<String, ArrayList<String>> getMegaOhmMeterClusterBayPositionNoAddressListMap() {
		return megaOhmMeterClusterBayPositionNoAddressListMap;
	}
	public static Map<String, ArrayList<String>> getVoltMeterClusterBayPositionNoAddressListMap() {
		return voltMeterClusterBayPositionNoAddressListMap;
	}
	public static Map<String, ArrayList<String>> getLduClusterBayPositionNoAddressListMap() {
		return lduClusterBayPositionNoAddressListMap;
	}
	public void setMegaOhmMeterClusterBayPositionNoAddressListMap(
			Map<String, ArrayList<String>> megaOhmMeterClusterBayPositionNoAddressListMap) {
		this.megaOhmMeterClusterBayPositionNoAddressListMap = megaOhmMeterClusterBayPositionNoAddressListMap;
	}
	public void setVoltMeterClusterBayPositionNoAddressListMap(
			Map<String, ArrayList<String>> voltMeterClusterBayPositionNoAddressListMap) {
		this.voltMeterClusterBayPositionNoAddressListMap = voltMeterClusterBayPositionNoAddressListMap;
	}
	public void setLduClusterBayPositionNoAddressListMap(
			Map<String, ArrayList<String>> lduMeterClusterBayPositionNoAddressListMap) {
		this.lduClusterBayPositionNoAddressListMap = lduMeterClusterBayPositionNoAddressListMap;
	}
	public static Map<String, Map<String, ArrayList<String>>> getFilteredClusterBayPositionNoAddressListMap() {
		return filteredClusterBayPositionNoAddressListMap;
	}
	public static void setFilteredClusterBayPositionNoAddressListMap(
			Map<String, Map<String, ArrayList<String>>> filteredClusterBayPositionNoAddressListMap) {
		BayUtils_Cluster2.filteredClusterBayPositionNoAddressListMap = filteredClusterBayPositionNoAddressListMap;
	}



}
*/