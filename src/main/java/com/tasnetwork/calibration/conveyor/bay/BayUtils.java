package com.tasnetwork.calibration.conveyor.bay;

import java.util.List;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
// import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
// import org.codehaus.groovy.transform.stc.StaticTypeCheckingSupport.FloatArrayStaticTypesHelper;

import com.tasnetwork.calibration.conveyor.ClusterServer;
// import com.tasnetwork.calibration.conveyor.RestApiClusterResponse;
import com.tasnetwork.calibration.conveyor.RestApiJsonBodyResponse;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.AsyncHttpClient.ConveyorClientManager;
import com.tasnetwork.calibration.conveyor.AsyncHttpClient.ConveyorClientManager.ClusterUtils;
// import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
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
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
// import com.tasnetwork.calibration.conveyor.dashboard.MeterStatus;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorCluster;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
// import com.tasnetwork.calibration.conveyor.bay.ft.Ft;
// import com.tasnetwork.calibration.conveyor.bay.hv.Hv;
// import com.tasnetwork.calibration.conveyor.bay.unloading.Unloading;
import com.tasnetwork.calibration.conveyor.plc.ModbusRequestProcessor;
import com.tasnetwork.calibration.conveyor.plc.ModbusTcpClient;
// import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
// import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
//import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;
import com.tasnetwork.spring.orm.model.ConveyorOutputMetrics;
import com.tasnetwork.spring.orm.model.ConveyorOutputMetricsSummary;
import com.tasnetwork.spring.orm.model.DeviceSetting;
// import com.tasnetwork.spring.orm.model.MeterResultSummary;
import com.tasnetwork.spring.orm.model.PalletManage;
import com.tasnetwork.spring.orm.model.PalletMeter;
import com.tasnetwork.spring.orm.model.PalletMeterArchivedResults;
import com.tasnetwork.spring.orm.model.PalletMeterResults;
// import com.tasnetwork.spring.orm.service.ConveyorOutputMetricsService;
// import com.tasnetwork.spring.orm.service.ConveyorOutputMetricsSummaryService;
import com.tasnetwork.calibration.conveyor.plc.ModbusTcpClientManager;

// import javafx.application.Platform;
// import javafx.scene.control.Alert.AlertType;

public class BayUtils {

	private static boolean userAborted = false;

	public static boolean isUserAborted() {
		return userAborted;
	}

	public static void setUserAborted(boolean userAborted) {
		BayUtils.userAborted = userAborted;
	}

	private static Map<String, IoPortInfo> inputIoPortInfoMap = new LinkedHashMap<String, IoPortInfo>();
	private static Map<String, IoPortInfo> outputIoPortInfoMap = new LinkedHashMap<String, IoPortInfo>();
	private static final ConcurrentHashMap<String, String> lastInputStatusMap = new ConcurrentHashMap<>();

	private static List<String> unloadingBayPalletDistinctIdList = new ArrayList<String>();
	private static List<String> rejectionBayPalletDistinctIdList = new ArrayList<String>();

	private static ModbusTcpClientManager modbusTcpClientManager = new ModbusTcpClientManager();
	private static final long MAX_TIMEOUT = 5000;
	private static TerminalBayConfigModel bayConfigModel = ConveyorDataManager.getTerminalBayConfig();

	public static volatile boolean ftBayMessageSenderSemLocked = false;
	private static Map<String, ArrayList<String>> clusterBayNameListMap = new HashMap<String, ArrayList<String>>();
	private static Map<String, String> clusterNameIdListMap = new LinkedHashMap<String, String>();
	private static Map<String, String> clusterBayNameIdMap = new LinkedHashMap<String, String>();

	private static Map<String, Map<String, ArrayList<String>>> filteredClusterBayNamePositionListMap = new LinkedHashMap<String, Map<String, ArrayList<String>>>();
	private static Map<String, Map<String, String>> filteredClusterBayPositionNoCnameMap = new LinkedHashMap<String, Map<String, String>>();
	private static Map<String, Map<String, String>> filteredClusterBayPositionNoDeviceIdMap = new LinkedHashMap<String, Map<String, String>>();
	private static Map<String, Map<String, ArrayList<String>>> filteredClusterBayPositionNoAddressListMap = new LinkedHashMap<String, Map<String, ArrayList<String>>>();

	private static Map<String, ArrayList<String>> qrClusterBayNamePositionListMap = new HashMap<String, ArrayList<String>>();
	private static Map<String, String> qrClusterBayPositionNoCnameMap = new LinkedHashMap<String, String>();
	private static Map<String, String> qrClusterBayPositionNoDeviceIdMap = new LinkedHashMap<String, String>();

	private static Map<String, ArrayList<String>> dutClusterBayNamePositionListMap = new HashMap<String, ArrayList<String>>();
	private static Map<String, String> dutClusterBayPositionNoCnameMap = new LinkedHashMap<String, String>();
	private static Map<String, String> dutClusterBayPositionNoDeviceIdMap = new LinkedHashMap<String, String>();

	private static Map<String, ArrayList<String>> megaOhmMeterClusterBayNamePositionListMap = new HashMap<String, ArrayList<String>>();
	private static Map<String, String> megaOhmMeterClusterBayPositionNoCnameMap = new LinkedHashMap<String, String>();
	private static Map<String, String> megaOhmMeterClusterBayPositionNoDeviceIdMap = new LinkedHashMap<String, String>();
	private static Map<String, ArrayList<String>> megaOhmMeterClusterBayPositionNoAddressListMap = new LinkedHashMap<String, ArrayList<String>>();

	private static Map<String, ArrayList<String>> voltMeterClusterBayNamePositionListMap = new HashMap<String, ArrayList<String>>();
	private static Map<String, String> voltMeterClusterBayPositionNoCnameMap = new LinkedHashMap<String, String>();
	private static Map<String, String> voltMeterClusterBayPositionNoDeviceIdMap = new LinkedHashMap<String, String>();
	private static Map<String, ArrayList<String>> voltMeterClusterBayPositionNoAddressListMap = new LinkedHashMap<String, ArrayList<String>>();

	private static Map<String, ArrayList<String>> lduClusterBayNamePositionListMap = new HashMap<String, ArrayList<String>>();
	private static Map<String, String> lduClusterBayPositionNoCnameMap = new LinkedHashMap<String, String>();
	private static Map<String, String> lduClusterBayPositionNoDeviceIdMap = new LinkedHashMap<String, String>();
	private static Map<String, ArrayList<String>> lduClusterBayPositionNoAddressListMap = new LinkedHashMap<String, ArrayList<String>>();

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
				deviceSetting.getDeviceType() +
				String.format("%02d", Integer.parseInt(deviceSetting.getPositionNo()));
		ApplicationLauncher.logger.debug("manipulateDeviceId : deviceId : " + deviceId);
		return deviceId;

	}

	public ClusterServer getServerDetails(String terminalId, String clusterId) {

		ClusterServer clusterServer = null;

		// String clusterIpAddress = "";
		// String clusterPortNo = "";
		Optional<ClusterDetail> clusterOpt = getBayConfigModel().getTerminal().stream()
				.filter(e1 -> e1.getTerminalId().equals(terminalId))
				.flatMap(terminal -> terminal.getClusterDetails().stream())
				.filter(e2 -> e2.getClusterId().equals(clusterId))
				.findFirst();

		if (clusterOpt.isPresent()) {
			// ClusterDetail clusterDetail = clusterOpt.get();
			// clusterIpAddress = clusterOpt.get().getClusterIpAddress();
			// clusterPortNo = clusterOpt.get().getClusterPortId();
			clusterServer = new ClusterServer(clusterOpt.get().getClusterIpAddress(),
					clusterOpt.get().getClusterPortId(), clusterId);
		}

		return clusterServer;
	}

	// ========================================================================
	public static void delay(long delayTimeInMsec) {
		long startTime = System.currentTimeMillis();

		while ((System.currentTimeMillis() - startTime) < delayTimeInMsec) {
			if ((System.currentTimeMillis() - startTime) >= MAX_TIMEOUT) {
				break;
			}

			try {
				Thread.sleep(1); // Sleep briefly to reduce CPU usage
			} catch (InterruptedException e) {
				// DO NOT restore interrupted status here (Thread.currentThread().interrupt())
				// as it causes infinite tight CPU loops in callers that do not check for it.
				// Just break the delay loop to allow the caller to evaluate stop flags.
				break;
			}
		}
	}

	// ===============================================================================================================================
	public static IoPortInfo getOutputPortDetails(String searchPortName) {

		if (getOutputIoPortInfoMap().containsKey(searchPortName)) {
			return getOutputIoPortInfoMap().get(searchPortName);
		}

		Optional<OutputPort> outputPortOpt = ConveyorDataManager.getTerminalBayConfig().getTerminal().stream()
				.filter(e -> e.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID))
				.flatMap(terminal -> terminal.getOutputPort().stream())
				.filter(p -> searchPortName.equals(p.getPortName()))
				.findFirst();

		if (outputPortOpt.isPresent()) {
			OutputPort outputPortDetails = outputPortOpt.get();
			ModbusTcpClient.logger
					.debug("getOutputPortDetails : getClusterId: " + outputPortDetails.getClusterId() + " -> getBayId: "
							+ outputPortDetails.getBayId() + " -> getPortId: " + outputPortDetails.getPortId());
			// ModbusTcpClient.logger.debug("getOutputPortDetails : getClusterId : " +
			// outputPortDetails.getClusterId());
			// ModbusTcpClient.logger.debug("getOutputPortDetails : getBayId : " +
			// outputPortDetails.getBayId());
			IoPortInfo outputPortInfo = new IoPortInfo(outputPortDetails.getPortId(), outputPortDetails.getClusterId(),
					outputPortDetails.getBayId());
			getOutputIoPortInfoMap().put(searchPortName, outputPortInfo);

			// return new IoPortInfo(outputPortDetails.getPortId(),
			// outputPortDetails.getClusterId(), outputPortDetails.getBayId());

			// Return all three values wrapped in an OutputPortInfo object
			return outputPortInfo;
		}

		return null; // or handle the case if outputPort is not found
	}

	public static IoPortInfo getOutputPortDetailsWithPortNamePrefixAndPositionNo(String terminalId, String clusterId,
			String bayId,
			int positionNo,
			String searchPrefixPortName) {

		ApplicationLauncher.logger.debug("getOutputPortDetailsWithPortNamePrefixAndPositionNo : inp terminalId     : "
				+ Integer.parseInt(terminalId));
		ApplicationLauncher.logger.debug("getOutputPortDetailsWithPortNamePrefixAndPositionNo : inp clusterId     : "
				+ Integer.parseInt(clusterId));
		ApplicationLauncher.logger.debug(
				"getOutputPortDetailsWithPortNamePrefixAndPositionNo : inp bayId         : " + Integer.parseInt(bayId));
		ApplicationLauncher.logger
				.debug("getOutputPortDetailsWithPortNamePrefixAndPositionNo : inp positionNo    : " + positionNo);

		Optional<OutputPort> outputPortOpt = ConveyorDataManager.getTerminalBayConfig().getTerminal().stream()
				// .filter(e-> e.getTerminalId().equals(terminalId))
				.filter(e -> Integer.parseInt(e.getTerminalId()) == Integer.parseInt(terminalId))
				.flatMap(terminal -> terminal.getOutputPort().stream())
				.filter(p -> Integer.parseInt(p.getClusterId()) == Integer.parseInt(clusterId))
				.filter(p1 -> Integer.parseInt(p1.getBayId()) == Integer.parseInt(bayId))
				.filter(p2 -> p2.getPortName().contains(searchPrefixPortName))
				.filter(p3 -> Integer.parseInt(p3.getPositionId()) == positionNo)
				.findFirst();

		if (outputPortOpt.isPresent()) {
			OutputPort outputPortDetails = outputPortOpt.get();
			ApplicationLauncher.logger.debug("getOutputPortDetailsWithPortNamePrefixAndPositionNo : getPortId    : "
					+ outputPortDetails.getPortId());
			ApplicationLauncher.logger.debug("getOutputPortDetailsWithPortNamePrefixAndPositionNo : getClusterId : "
					+ outputPortDetails.getClusterId());
			ApplicationLauncher.logger.debug("getOutputPortDetailsWithPortNamePrefixAndPositionNo : getBayId     : "
					+ outputPortDetails.getBayId());

			// Return all three values wrapped in an OutputPortInfo object
			return new IoPortInfo(outputPortDetails.getPortId(), outputPortDetails.getClusterId(),
					outputPortDetails.getBayId());
		}

		return null; // or handle the case if outputPort is not found
	}

	public static IoPortInfo getInputPortDetailsWithPortNamePrefixAndPositionNo(String terminalId, String clusterId,
			String bayId,
			int positionNo, String searchPrefixPortName) {
		ApplicationLauncher.logger.debug("getInputPortDetailsWithPortNamePrefixAndPositionNo : inp terminalId     : "
				+ Integer.parseInt(terminalId));
		ApplicationLauncher.logger.debug("getInputPortDetailsWithPortNamePrefixAndPositionNo : inp clusterId     : "
				+ Integer.parseInt(clusterId));
		ApplicationLauncher.logger.debug(
				"getInputPortDetailsWithPortNamePrefixAndPositionNo : inp bayId         : " + Integer.parseInt(bayId));
		ApplicationLauncher.logger
				.debug("getInputPortDetailsWithPortNamePrefixAndPositionNo : inp positionNo    : " + positionNo);

		Optional<InputPort> inputPortOpt = ConveyorDataManager.getTerminalBayConfig().getTerminal().stream()
				// .filter(e-> e.getTerminalId().equals(terminalId))
				.filter(e -> Integer.parseInt(e.getTerminalId()) == Integer.parseInt(terminalId))
				.flatMap(terminal -> terminal.getInputPort().stream())
				.filter(p -> Integer.parseInt(p.getClusterId()) == Integer.parseInt(clusterId))
				.filter(p1 -> Integer.parseInt(p1.getBayId()) == Integer.parseInt(bayId))
				.filter(p2 -> p2.getPortName().contains(searchPrefixPortName))
				.filter(p3 -> Integer.parseInt(p3.getPositionId()) == positionNo)
				.findFirst();

		if (inputPortOpt.isPresent()) {
			InputPort inputPortDetails = inputPortOpt.get();
			ApplicationLauncher.logger.debug("getInputPortDetailsWithPortNamePrefixAndPositionNo : getPortId    : "
					+ inputPortDetails.getPortId());
			ApplicationLauncher.logger.debug("getInputPortDetailsWithPortNamePrefixAndPositionNo : getClusterId : "
					+ inputPortDetails.getClusterId());
			ApplicationLauncher.logger.debug("getInputPortDetailsWithPortNamePrefixAndPositionNo : getBayId     : "
					+ inputPortDetails.getBayId());

			// Return all three values wrapped in an OutputPortInfo object
			return new IoPortInfo(inputPortDetails.getPortId(), inputPortDetails.getClusterId(),
					inputPortDetails.getBayId());
		}

		return null; // or handle the case if outputPort is not found
	}

	public static IoPortInfo getInputPortDetailsWithPositionNo(String terminalId, String clusterId,
			String bayId,
			int positionNo) {
		ApplicationLauncher.logger
				.debug("getInputPortDetailsWithPositionNo : inp terminalId     : " + Integer.parseInt(terminalId));
		ApplicationLauncher.logger
				.debug("getInputPortDetailsWithPositionNo : inp clusterId     : " + Integer.parseInt(clusterId));
		ApplicationLauncher.logger
				.debug("getInputPortDetailsWithPositionNo : inp bayId         : " + Integer.parseInt(bayId));
		ApplicationLauncher.logger.debug("getInputPortDetailsWithPositionNo : inp positionNo    : " + positionNo);

		Optional<InputPort> inputPortOpt = ConveyorDataManager.getTerminalBayConfig().getTerminal().stream()
				// .filter(e-> e.getTerminalId().equals(terminalId))
				.filter(e -> Integer.parseInt(e.getTerminalId()) == Integer.parseInt(terminalId))
				.flatMap(terminal -> terminal.getInputPort().stream())
				.filter(p -> Integer.parseInt(p.getClusterId()) == Integer.parseInt(clusterId))
				.filter(p1 -> Integer.parseInt(p1.getBayId()) == Integer.parseInt(bayId))
				.filter(p2 -> Integer.parseInt(p2.getPositionId()) == positionNo)
				.findFirst();

		if (inputPortOpt.isPresent()) {
			InputPort inputPortDetails = inputPortOpt.get();
			ApplicationLauncher.logger
					.debug("getInputPortDetailsWithPositionNo : getPortId    : " + inputPortDetails.getPortId());
			ApplicationLauncher.logger
					.debug("getInputPortDetailsWithPositionNo : getClusterId : " + inputPortDetails.getClusterId());
			ApplicationLauncher.logger
					.debug("getInputPortDetailsWithPositionNo : getBayId     : " + inputPortDetails.getBayId());

			// Return all three values wrapped in an OutputPortInfo object
			return new IoPortInfo(inputPortDetails.getPortId(), inputPortDetails.getClusterId(),
					inputPortDetails.getBayId());
		}

		return null; // or handle the case if outputPort is not found
	}

	public static IoPortInfo getInputPortDetails(String searchPortName) {

		if (getInputIoPortInfoMap().containsKey(searchPortName)) {
			return getInputIoPortInfoMap().get(searchPortName);
		}
		Optional<InputPort> inputPortOpt = ConveyorDataManager.getTerminalBayConfig().getTerminal().stream()
				.filter(e -> e.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID))
				.flatMap(terminal -> terminal.getInputPort().stream())
				.filter(p -> searchPortName.equals(p.getPortName()))
				.findFirst();

		if (inputPortOpt.isPresent()) {
			InputPort inputPortDetails = inputPortOpt.get();

			IoPortInfo ioPortInfo = new IoPortInfo(inputPortDetails.getPortId(), inputPortDetails.getClusterId(),
					inputPortDetails.getBayId());

			getInputIoPortInfoMap().put(searchPortName, ioPortInfo);

			return ioPortInfo;
		}

		return null; // or handle the case if outputPort is not found
	}

	public boolean setInitiatePulseCounterOnCluster(String clusterId, String bayId, String inputPortId,
			String outputStatus) {
		ApplicationLauncher.logger.info("setInitiatePulseCounterOnCluster-bay-utils: Entry-failed-debug");
		ApplicationLauncher.logger.debug("setInitiatePulseCounterOnCluster: inputPortId: " + inputPortId);
		Boolean status = false;
		String statusResponse = "";
		ConveyorClientManager cluster1ClientManager = ConveyorClientManager.getInstance(clusterId);
		String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
		ClusterServer clusterServer = getServerDetails(terminalId, clusterId);

		if (clusterServer == null) {
			ApplicationLauncher.logger
					.debug("setInitiatePulseCounterOnCluster: cluster IP address details not found for terminalId: "
							+ terminalId + " , clusterId: " + clusterId);
			return status;
		}

		boolean setOutput = true;
		cluster1ClientManager.messageBayData(setOutput, clusterServer, clusterId, bayId, inputPortId, outputStatus);

		ApplicationLauncher.logger.debug("setInitiatePulseCounterOnCluster: Wait Time Entry");
		boolean isResponseReceived = false;

		if (ProcalFeatureEnable.CONVEYOR_REST_CLIENT_ENABLED) {
			// cluster1ClientManager.getRestConvClient().WaitForServerResponse(12);
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
					ApplicationLauncher.logger.debug("setInitiatePulseCounterOnCluster: " + inputPortId
							+ " : statusResponse Data: " + statusResponse);
				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger
							.error("setInitiatePulseCounterOnCluster: Exception1 : " + e.getMessage());
				}
			} else {
				ApplicationLauncher.logger.debug("setInitiatePulseCounterOnCluster: isResponseReceived : test3");
			}
		} else {
			ApplicationLauncher.logger.debug("setInitiatePulseCounterOnCluster: response failed : " + inputPortId);
		}

		return status;
	}

	public String getInputDataFromBayV2(IoPortInfo io_portInfo) {

		String statusResponse = "";
		if (ProcalFeatureEnable.MODBUS_PLC_SLAVE_MODE) {
			statusResponse = getInputDataFromPlcBay(io_portInfo);
		} else {
			ApplicationLauncher.logger.info("getInputDataFromBayV2: Entry-failed-debug");
			statusResponse = getInputDataFromBayStm32(io_portInfo);
		}

		return statusResponse;
	}

	public String getInputDataFromBayStm32(IoPortInfo io_portInfo) {
		ApplicationLauncher.logger.info("getInputDataFromBayStm32-bay utils: Entry-failed-debug");
		String clusterId = io_portInfo.getClusterId();
		String bayId = io_portInfo.getBayId();
		String inputPortId = io_portInfo.getPortId();
		ApplicationLauncher.logger.debug("getInputDataFromBayV2: Entry: " + inputPortId);
		String statusResponse = "";
		ConveyorClientManager cluster1ClientManager = ConveyorClientManager.getInstance(clusterId);
		String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
		ClusterServer clusterServer = getServerDetails(terminalId, clusterId);

		if (clusterServer == null) {
			ApplicationLauncher.logger
					.debug("getInputDataFromBayV2: cluster ip address details not found for terminalId: " + terminalId
							+ " , clusterId: " + clusterId);
			return null;
		}

		String dummyOutputValue = "";
		boolean setOutput = false;
		cluster1ClientManager.messageBayData(setOutput, clusterServer, clusterId, bayId, inputPortId, dummyOutputValue);

		ApplicationLauncher.logger.debug("getInputDataFromBayV2: Wait Time Entry: " + inputPortId);
		boolean isResponseReceived = false;

		if (ProcalFeatureEnable.CONVEYOR_REST_CLIENT_ENABLED) {
			// cluster1ClientManager.getRestConvClient().WaitForServerResponse(8);
			isResponseReceived = cluster1ClientManager.getRestConvClient().isResponseReceived();
		} else {
			cluster1ClientManager.getAsyncConvClient().WaitForServerResponse(8);
			isResponseReceived = cluster1ClientManager.getAsyncConvClient().isResponseReceived();
		}

		ApplicationLauncher.logger.debug("getInputDataFromBayV2: Wait Time Exit: " + inputPortId);

		if (isResponseReceived) {
			RestApiJsonBodyResponse clusterResponseData;
			if (ProcalFeatureEnable.CONVEYOR_REST_CLIENT_ENABLED) {
				clusterResponseData = cluster1ClientManager.getRestConvClient().getRestApiClusterResponseBodyData();
			} else {
				clusterResponseData = cluster1ClientManager.getAsyncConvClient().getRestApiClusterResponseBodyData();
			}

			ApplicationLauncher.logger.debug("getInputDataFromBayV2: isResponseReceived : test1 : " + inputPortId);
			if (clusterResponseData.getStatusCode().equals(ConstantConveyor.HTTP_RESPONSE_SUCCESS)) {
				ApplicationLauncher.logger.debug("getInputDataFromBayV2: isResponseReceived : test2: " + inputPortId);
				try {
					ApplicationLauncher.logger
							.debug("getInputDataFromBayV2: isResponseReceived : inputPortId: " + inputPortId);
					statusResponse = clusterResponseData.getJsonBodyResponse().get(inputPortId).toString();
					ApplicationLauncher.logger
							.debug("getInputDataFromBayV2: isResponseReceived : statusResponse: " + statusResponse);
				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger.error("getInputDataFromBayV2-1: Exception1 : " + e.getMessage()
							+ " : inputPortId: " + inputPortId);
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

	public String setOutputDataToPlcBayV2(String clusterId, String bayId, String outputPortId, String outputStatus) {
		// ModbusTcpClient.logger.debug("setOutputDataToPlcBay: Entry ");
		AtomicReference<String> statusResponse = new AtomicReference<>("");
		outputStatus = Constant_IO_ActionMapping.OFF.equalsIgnoreCase(outputStatus) ? Constant_IO_ActionMapping.ON
				: Constant_IO_ActionMapping.OFF;
		String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
		ClusterServer clusterServer = getServerDetails(terminalId, clusterId);

		if (clusterServer == null) {
			ModbusTcpClient.logger
					.debug("setOutputDataToPlcBayV2: cluster IP address details not found for terminalId: " + terminalId
							+ " , clusterId: " + clusterId);
			return null;
		}

		ModbusTcpClient.logger
				.debug("setOutputDataToPlcBayV2: Processing cluster: " + clusterId + " : outputId : " + outputPortId);
		boolean modbusPlcConnected = getModbusTcpClientManager().ensureModbusConnection(clusterServer);

		if (modbusPlcConnected) {
			String formattedOutputPortId = outputPortId.replaceAll("[^0-9.]", "");
			if (GuiUtils.isNumber(formattedOutputPortId)) {
				int plcCoilAddress = Integer.parseInt(formattedOutputPortId);
				boolean outputValue = Constant_IO_ActionMapping.OFF.equals(outputStatus);
				CountDownLatch latch = new CountDownLatch(1);
				String outputStatusFinal = outputStatus;
				ModbusRequestProcessor.addRequest(clusterServer.getClusterId(), () -> {
					try {
						BayResponse bayResponse = getModbusTcpClientManager().modbusWriteCoil(clusterServer,
								plcCoilAddress, outputValue);

						if (!bayResponse.getStatus()) {
							ModbusTcpClient.logger.info(
									"setOutputDataToPlcBayV2: outputPortId: device not responded : " + outputPortId);
							statusResponse.set(outputStatusFinal.equalsIgnoreCase(Constant_IO_ActionMapping.OFF)
									? Constant_IO_ActionMapping.ON
									: Constant_IO_ActionMapping.OFF);
						} else {
							ModbusTcpClient.logger.info("setOutputDataToPlcBayV2: success : outputPortId: "
									+ outputPortId + " : " + bayResponse.isResponseBooleanData());
							statusResponse
									.set(outputValue ? Constant_IO_ActionMapping.OFF : Constant_IO_ActionMapping.ON);
						}
					} finally {
						latch.countDown();
					}
				});

				try {
					latch.await(); // Wait until the task completes
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			} else {
				statusResponse.set(outputPortId);
				ModbusTcpClient.logger.info("setOutputDataToPlcBayV2: invalid outputPortId: " + outputPortId);
			}
		} else {
			statusResponse.set(outputPortId);
			ModbusTcpClient.logger
					.info("setOutputDataToPlcBayV2: modbus failed to connect : outputPortId: " + outputPortId);
		}

		ModbusTcpClient.logger.debug("setOutputDataToPlcBayV2: Exit ");
		return statusResponse.get();
	}

	public String setOutputWordToPlcBay(String clusterId, String bayId, String outputPortId, int outputValue) {
		// ModbusTcpClient.logger.debug("setOutputWordToPlcBay: Entry ");
		ModbusTcpClient.logger.debug("setOutputWordToPlcBay: clusterId: " + clusterId + " -> bayId :" + bayId
				+ " -> outputPortId: " + outputPortId);

		String statusResponse = "";

		String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
		String formattedOutputPortId = outputPortId;
		ClusterServer clusterServer = getServerDetails(terminalId, clusterId);
		// ModbusTcpClient.logger.debug("setOutputWordToPlcBay: Test1 ");
		if (clusterServer == null) {
			ModbusTcpClient.logger.debug("setOutputWordToPlcBay: cluster ip address details not found for terminalId: "
					+ terminalId + " , clusterId: " + clusterId);
			return null;
		}
		boolean modbusPlcConnected = false;

		int retryCount = 10;
		boolean messageProcessed = false;

		Object clusterLock = ClusterUtils.getClusterLock(clusterId); // Get lock for this cluster

		while ((retryCount != 0) && (!messageProcessed) && (!BayUtils.isUserAborted())) {
			retryCount--;
			ModbusTcpClient.logger.debug("setOutputWordToPlcBay : retryCount: " + retryCount + " : " + outputPortId);

			synchronized (clusterLock) { // Lock based on clusterId
				ModbusTcpClient.logger.debug(
						"setOutputWordToPlcBay: Processing cluster: " + clusterId + " : outputId : " + outputPortId);

				// Process the message

				modbusPlcConnected = getModbusTcpClientManager().ensureModbusConnection(clusterServer);// isConnectionSetupGood(clusterServer.getIpAddress(),
																										// clusterServer.getPort());

				if (modbusPlcConnected) {

					formattedOutputPortId = outputPortId.replaceAll("[^0-9.]", "");
					// ModbusTcpClient.logger.info("SendDataToBayTask: inputPortId: "+inputPortId);

					if (GuiUtils.isNumber(formattedOutputPortId)) {
						int plcCoilAddress = -1;
						plcCoilAddress = Integer.parseInt(formattedOutputPortId);

						/*
						 * if(Constant_IO_ActionMapping.OLD_OFF_NEW_ON.equals(outputValue)){
						 * outputValue = true;
						 * }
						 */

						BayResponse bayResponse = new BayResponse();
						bayResponse = getModbusTcpClientManager().modbusWriteHoldingRegister(clusterServer,
								plcCoilAddress, outputValue);
						if (!bayResponse.getStatus()) {
							ModbusTcpClient.logger.info(
									"setOutputWordToPlcBay: outputPortId: device not responded : " + outputPortId);

							statusResponse = Constant_IO_ActionMapping.OFF; // Constant_IO_ActionMapping.OLD_ON_NEW_OFF.equalsIgnoreCase(outputStatus)
																			// ?
																			// Constant_IO_ActionMapping.OLD_ON_NEW_OFF
																			// :
																			// Constant_IO_ActionMapping.OLD_OFF_NEW_ON;
						} else {
							ModbusTcpClient.logger.info("setOutputWordToPlcBay: success : outputPortId:  "
									+ outputPortId + " : " + bayResponse.isResponseBooleanData());

							statusResponse = Constant_IO_ActionMapping.OFF; // Constant_IO_ActionMapping.OLD_ON_NEW_OFF.equalsIgnoreCase(outputStatus)
																			// ?
																			// Constant_IO_ActionMapping.OLD_ON_NEW_OFF
																			// :
																			// Constant_IO_ActionMapping.OLD_OFF_NEW_ON;

						}
					} else {
						statusResponse = outputPortId;
						ModbusTcpClient.logger.info("setOutputWordToPlcBay: invalid outputPortId: " + outputPortId);

					}

				} else {
					statusResponse = outputPortId;
					ModbusTcpClient.logger
							.info("setOutputWordToPlcBay: modbus failed to connect :  outputPortId: " + outputPortId);
				}

				messageProcessed = true;

				ModbusTcpClient.logger.debug("setOutputWordToPlcBay: Completed processing for cluster: " + clusterId
						+ " : outputId : " + outputPortId);
			}
		}

		ModbusTcpClient.logger.debug("setOutputWordToPlcBay: Exit ");
		return statusResponse;
	}

	public String setOutputDataToPlcBay(String clusterId, String bayId, String outputPortId, String outputStatus) {
		// ModbusTcpClient.logger.debug("setOutputDataToPlcBay: Entry ");
		ModbusTcpClient.logger.debug("setOutputDataToPlcBay: clusterId: " + clusterId + " -> bayId :" + bayId
				+ " -> outputPortId: " + outputPortId);

		String statusResponse = "";

		//// outputStatus =
		//// Constant_IO_ActionMapping.OLD_ON_NEW_OFF.equalsIgnoreCase(outputStatus) ?
		//// Constant_IO_ActionMapping.OLD_OFF_NEW_ON :
		//// Constant_IO_ActionMapping.OLD_ON_NEW_OFF;

		// ConveyorClientManager cluster1ClientManager =
		// ConveyorClientManager.getInstance(clusterId);
		// RestApiJsonBodyResponse clusterResponseData = new RestApiJsonBodyResponse();
		String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
		String formattedOutputPortId = outputPortId;
		ClusterServer clusterServer = getServerDetails(terminalId, clusterId);
		// ModbusTcpClient.logger.debug("setOutputDataToPlcBay: Test1 ");
		if (clusterServer == null) {
			ModbusTcpClient.logger.debug("setOutputDataToPlcBay: cluster ip address details not found for terminalId: "
					+ terminalId + " , clusterId: " + clusterId);
			return null;
		}
		boolean modbusPlcConnected = false;
		/*
		 * ApplicationLauncher.logger.debug("setOutputDataToPlcBay: Test2 ");
		 * if(!getModbusTcpClientManager().getModbusTcpServerConnectedMap().containsKey(
		 * clusterServer.getIpAddress())){
		 * 
		 * BayResponse bayResponse =
		 * getModbusTcpClientManager().modbusConnect(clusterServer.getIpAddress(),
		 * clusterServer.getPort());
		 * if(bayResponse.getStatus()) {
		 * ApplicationLauncher.logger.debug("setOutputDataToPlcBay: modbus connected : "
		 * + outputPortId);
		 * modbusPlcConnected = true;
		 * }
		 * }else {
		 * if(!getModbusTcpClientManager().getModbusTcpServerConnectedMap().get(
		 * clusterServer.getIpAddress())){
		 * BayResponse bayResponse =
		 * getModbusTcpClientManager().modbusConnect(clusterServer.getIpAddress(),
		 * clusterServer.getPort());
		 * if(bayResponse.getStatus()) {
		 * ApplicationLauncher.logger.
		 * debug("setOutputDataToPlcBay: modbus connected-B : " + outputPortId);
		 * modbusPlcConnected = true;
		 * }
		 * }else{
		 * ApplicationLauncher.logger.
		 * debug("setOutputDataToPlcBay: modbus already connected-C : " + outputPortId);
		 * modbusPlcConnected = true;
		 * }
		 * 
		 * }
		 */

		int retryCount = 10;
		boolean messageProcessed = false;

		// ModbusTcpClient.logger.debug("setOutputDataToPlcBay : clusterId : " +
		// clusterId);

		Object clusterLock = ClusterUtils.getClusterLock(clusterId); // Get lock for this cluster

		while ((retryCount != 0) && (!messageProcessed) && (!BayUtils.isUserAborted())) {
			retryCount--;
			ModbusTcpClient.logger.debug("setOutputDataToPlcBay : retryCount: " + retryCount + " : " + outputPortId);

			synchronized (clusterLock) { // Lock based on clusterId
				ModbusTcpClient.logger.debug(
						"setOutputDataToPlcBay: Processing cluster: " + clusterId + " : outputId : " + outputPortId);

				// Process the message

				modbusPlcConnected = getModbusTcpClientManager().ensureModbusConnection(clusterServer);// isConnectionSetupGood(clusterServer.getIpAddress(),
																										// clusterServer.getPort());

				if (modbusPlcConnected) {
					// ModbusTcpClient.logger.debug("setOutputDataToPlcBay: Test3 ");

					formattedOutputPortId = outputPortId.replaceAll("[^0-9.]", "");

					if (GuiUtils.isNumber(formattedOutputPortId)) {
						int plcCoilAddress = -1;
						plcCoilAddress = Integer.parseInt(formattedOutputPortId);
						boolean outputValue = false;
						if (Constant_IO_ActionMapping.ON.equals(outputStatus)) {
							outputValue = true;
						}
						BayResponse bayResponse = new BayResponse();
						bayResponse = getModbusTcpClientManager().modbusWriteCoil(clusterServer, plcCoilAddress,
								outputValue);// .getModbusTcpClient().modbusTcpSendWriteCoilCmd(plcCoilAddress,outputValue);
						if (!bayResponse.getStatus()) {
							ModbusTcpClient.logger.info(
									"setOutputDataToPlcBay: outputPortId: device not responded : " + outputPortId);
							statusResponse = Constant_IO_ActionMapping.OFF.equalsIgnoreCase(outputStatus)
									? Constant_IO_ActionMapping.OFF
									: Constant_IO_ActionMapping.ON;

						} else {
							ModbusTcpClient.logger.info("setOutputDataToPlcBay: success : outputPortId:  "
									+ outputPortId + " : " + bayResponse.isResponseBooleanData());

							statusResponse = Constant_IO_ActionMapping.OFF.equalsIgnoreCase(outputStatus)
									? Constant_IO_ActionMapping.OFF
									: Constant_IO_ActionMapping.ON;

						}
					} else {

						statusResponse = outputPortId;
						ModbusTcpClient.logger.info("setOutputDataToPlcBay: invalid outputPortId: " + outputPortId);

					}
				} else {
					statusResponse = outputPortId;
					ModbusTcpClient.logger
							.info("setOutputDataToPlcBay: modbus failed to connect :  outputPortId: " + outputPortId);
				}

				messageProcessed = true;

				ModbusTcpClient.logger.debug("setOutputDataToPlcBay: Completed processing for cluster: " + clusterId
						+ " : outputId : " + outputPortId);
			}
		}

		// ModbusTcpClient.logger.debug("setOutputDataToPlcBay: Exit ");
		return statusResponse;
	}

	public String getInputDataFromPlcBay(IoPortInfo io_portInfo) {

		String clusterId = io_portInfo.getClusterId();
		String bayId = io_portInfo.getBayId();
		String inputPortId = io_portInfo.getPortId();

		String statusResponse = "";

		String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
		ClusterServer clusterServer = getServerDetails(terminalId, clusterId);
		boolean modbusPlcConnected = false;
		if (clusterServer == null) {
			ModbusTcpClient.logger.debug("getInputDataFromPlcBay: cluster ip address details not found for terminalId: "
					+ terminalId + " , clusterId: " + clusterId);
			return null;
		}

		int retryCount = 10;
		boolean messageProcessed = false;

		Object clusterLock = ClusterUtils.getClusterLock(clusterId); // Get lock for this cluster


		while ((retryCount != 0) && (!messageProcessed) && (!BayUtils.isUserAborted())) {
			retryCount--;

			synchronized (clusterLock) { // Lock based on clusterId

				// Process the message
				modbusPlcConnected = getModbusTcpClientManager().ensureModbusConnection(clusterServer);

				if (modbusPlcConnected) {

					inputPortId = inputPortId.replaceAll("[^0-9.]", "");

					if (GuiUtils.isNumber(inputPortId)) {
						int plcCoilAddress = -1;
						plcCoilAddress = Integer.parseInt(inputPortId);

						BayResponse bayResponse = getModbusTcpClientManager().modbusReadCoil(clusterServer,
								plcCoilAddress);// .modbusTcpSendReadCoilCmd(plcCoilAddress);
						if (!bayResponse.getStatus()) {
							ModbusTcpClient.logger.info("getInputDataFromPlcBay: inputPortId: device not responded ");
							statusResponse = inputPortId;
						} else {
							statusResponse = bayResponse.getResponseData();
							String mapKey = clusterId + "-" + io_portInfo.getPortId();
							String lastStatus = lastInputStatusMap.get(mapKey);
							if (lastStatus == null || !lastStatus.equals(statusResponse)) {
								ModbusTcpClient.logger.debug("getInputDataFromPlcBay: clusterId: " + clusterId + " -> bayId :" + bayId + " -> inputPortId: " + io_portInfo.getPortId());
								ModbusTcpClient.logger.info("getInputDataFromPlcBay: inputPortId:  " + io_portInfo.getPortId() + " : " + bayResponse.isResponseBooleanData());
								ModbusTcpClient.logger.info("getInputDataFromPlcBay: inputPortId:  " + statusResponse + " : " + statusResponse);
								ModbusTcpClient.logger.debug("getInputDataFromPlcBay: Completed processing for cluster: " + clusterId + " : inputPortId : " + io_portInfo.getPortId());
								lastInputStatusMap.put(mapKey, statusResponse);
							}
						}
					} else {
						statusResponse = inputPortId;
						ModbusTcpClient.logger
								.info("getInputDataFromPlcBay: invalid inputPortId: " + io_portInfo.getPortId());
					}
				} else {
					statusResponse = inputPortId;
					ModbusTcpClient.logger
							.info("getInputDataFromPlcBay: modbus failed to connect :  outputPortId: " + inputPortId);
				}

				messageProcessed = true;
			}
		}

		return statusResponse;
	}

	public String getInputDataFromPlcBayV2(IoPortInfo io_portInfo) {
		String clusterId = io_portInfo.getClusterId();
		io_portInfo.getBayId();
		String inputPortId = io_portInfo.getPortId();
		ModbusTcpClient.logger.debug("getInputDataFromPlcBay: Entry: " + inputPortId);

		String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
		ClusterServer clusterServer = getServerDetails(terminalId, clusterId);
		if (clusterServer == null) {
			ModbusTcpClient.logger.debug("getInputDataFromPlcBay: Cluster IP address details not found for terminalId: "
					+ terminalId + " , clusterId: " + clusterId);
			return null;
		}

		boolean modbusPlcConnected = getModbusTcpClientManager().ensureModbusConnection(clusterServer);
		if (!modbusPlcConnected) {
			ModbusTcpClient.logger
					.info("getInputDataFromPlcBay: Modbus failed to connect : inputPortId: " + inputPortId);
			return inputPortId;
		}

		AtomicReference<String> statusResponse = new AtomicReference<>(inputPortId);
		inputPortId = inputPortId.replaceAll("[^0-9]", "");

		if (GuiUtils.isNumber(inputPortId)) {
			int plcCoilAddress = Integer.parseInt(inputPortId);
			String serverKey = clusterServer.getIpAddress() + ":" + clusterServer.getPort();

			CompletableFuture<Void> future = new CompletableFuture<>();
			String inputPortIdFinal = inputPortId;
			ModbusRequestProcessor.addRequest(serverKey, () -> {
				BayResponse bayResponse = getModbusTcpClientManager().modbusReadCoil(clusterServer, plcCoilAddress);

				if (!bayResponse.getStatus()) {
					ModbusTcpClient.logger.info("getInputDataFromPlcBay: inputPortId: device not responded");
					statusResponse.set(inputPortIdFinal);
				} else {
					ModbusTcpClient.logger.info("getInputDataFromPlcBay: inputPortId: " + io_portInfo.getPortId()
							+ " : " + bayResponse.isResponseBooleanData());
					String response = bayResponse.getResponseData();
					statusResponse
							.set(Constant_IO_ActionMapping.OFF.equalsIgnoreCase(response) ? Constant_IO_ActionMapping.ON
									: Constant_IO_ActionMapping.OFF);
					ModbusTcpClient.logger.info("getInputDataFromPlcBay: inputPortId: " + statusResponse.get());
				}
				future.complete(null);
			});

			// Wait for the async request to complete
			try {
				future.get();
			} catch (Exception e) {
				ModbusTcpClient.logger.error("getInputDataFromPlcBay: Error waiting for Modbus request", e);
			}
		} else {
			ModbusTcpClient.logger.info("getInputDataFromPlcBay: invalid inputPortId: " + io_portInfo.getPortId());
		}

		ModbusTcpClient.logger.debug("getInputDataFromPlcBay: Completed processing for cluster: " + clusterId
				+ " : inputPortId : " + io_portInfo.getPortId());
		return statusResponse.get();
	}

	public String getPulseCounterStatusFromBay(String clusterId, String bayId, String inputPortId) {

		ApplicationLauncher.logger.info("getPulseCounterStatusFromBay-bay-utils: Entry-failed-debug");
		ApplicationLauncher.logger.debug("getPulseCounterStatusFromBay: inputPortId: " + inputPortId);
		String pulseCounter = "";
		String statusResponse = "";
		String testStatus = "";
		ConveyorClientManager cluster1ClientManager = ConveyorClientManager.getInstance(clusterId);
		String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
		ClusterServer clusterServer = getServerDetails(terminalId, clusterId);

		if (clusterServer == null) {
			ApplicationLauncher.logger
					.debug("getPulseCounterStatusFromBay: cluster IP address details not found for terminalId: "
							+ terminalId + " , clusterId: " + clusterId);
			return null;
		}

		boolean setOutput = false;
		String dummyOutputValue = "";
		cluster1ClientManager.messageBayData(setOutput, clusterServer, clusterId, bayId, inputPortId, dummyOutputValue);

		ApplicationLauncher.logger.debug("getPulseCounterStatusFromBay: Wait Time Entry");
		boolean isResponseReceived = false;

		if (ProcalFeatureEnable.CONVEYOR_REST_CLIENT_ENABLED) {
			// cluster1ClientManager.getRestConvClient().WaitForServerResponse(8);
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

	// ========================================================================================

	public String setOutputDataToBay(String clusterId, String bayId, String outputPortId, String outputStatus) {
		ApplicationLauncher.logger.info("setOutputDataToBay - bayutils: Entry-failed-debug");
		String statusResponse = "";
		if (ProcalFeatureEnable.MODBUS_PLC_SLAVE_MODE) {
			statusResponse = setOutputDataToPlcBay(clusterId, bayId, outputPortId, outputStatus);
		} else {
			statusResponse = setOutputDataToBayStm32(clusterId, bayId, outputPortId, outputStatus);
		}

		return statusResponse;
	}

	public String setOutputDataToBayStm32(String clusterId, String bayId, String outputPortId, String outputStatus) {
		ApplicationLauncher.logger.info("setOutputDataToBayStm32 - bayutils: Entry-failed-debug");
		String statusResponse = "";
		ConveyorClientManager cluster1ClientManager = ConveyorClientManager.getInstance(clusterId);
		RestApiJsonBodyResponse clusterResponseData = new RestApiJsonBodyResponse();
		String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
		ClusterServer clusterServer = getServerDetails(terminalId, clusterId);

		if (clusterServer == null) {
			ApplicationLauncher.logger.debug("setOutputDataToBay: cluster ip address details not found for terminalId: "
					+ terminalId + " , clusterId: " + clusterId);
			return null;
		}

		boolean setOutput = true;
		cluster1ClientManager.messageBayData(setOutput, clusterServer, clusterId, bayId, outputPortId, outputStatus);

		boolean isResponseReceived = false;
		if (ProcalFeatureEnable.CONVEYOR_REST_CLIENT_ENABLED) {
			// cluster1ClientManager.getRestConvClient().WaitForServerResponse(8);
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

			if (clusterResponseData != null
					&& clusterResponseData.getStatusCode().equals(ConstantConveyor.HTTP_RESPONSE_SUCCESS)) {
				try {
					if (clusterResponseData.getJsonBodyResponse() != null) {
						statusResponse = clusterResponseData.getJsonBodyResponse().get(outputPortId).toString();
					} else {
						ApplicationLauncher.logger.error(
								"setOutputDataToBay: JsonBodyResponse is null for outputPortId: " + outputPortId);
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

	public static void init() {

		ApplicationLauncher.logger.debug("BayUtils: init: loadDataFromConfig: Entry");
		// ref_cmbBxDut1ClusterId.getItems().clear();

		// ref_cmbBxDutClusterId1;
		// ref_cmbBxDutBayId1;

		// =======================================

		for (Terminal eachTerminal : getBayConfigModel().getTerminal()) {
			if (eachTerminal.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID)) {
				for (ClusterDetail eachClusterDetail : eachTerminal.getClusterDetails()) {

					getClusterNameIdListMap().put(eachClusterDetail.getName(), eachClusterDetail.getClusterId());
					ArrayList<String> bayList = new ArrayList<String>();
					getClusterBayNameListMap().put(eachClusterDetail.getName(), bayList);

					for (Bay eachBay : eachClusterDetail.getBay()) {

						// ref_cmbBxBaySelection.getItems().add(eachBay.getBayName());
						bayList.add(eachBay.getBayName());
						getClusterBayNameListMap().put(eachClusterDetail.getName(), bayList);
						Map<String, String> bayNameIdMap = new HashMap<String, String>();
						bayNameIdMap.put(eachBay.getBayName(), eachBay.getBayId());
						getClusterBayNameIdMap().put(eachClusterDetail.getName() + "_" + eachBay.getBayName(),
								eachBay.getBayId());
						// getClusterBayNameIdMap().put(eachClusterDetail.getName(), bayNameIdMap);
						// ApplicationLauncher.logger.debug("loadDataFromConfig :
						// getClusterBayNameIdMap().get(clusterName)-1 :"+ getClusterBayNameIdMap());

					}
				}

			}

		}

		for (Terminal eachTerminal : getBayConfigModel().getTerminal()) {
			String clusterId = "";
			String bayId = "";
			if (eachTerminal.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID)) {
				for (ClusterDetail eachClusterDetail : eachTerminal.getClusterDetails()) {
					clusterId = getClusterNameIdListMap().get(eachClusterDetail.getName());
					for (Bay eachBay : eachClusterDetail.getBay()) {
						bayId = getClusterBayNameIdMap().get(eachClusterDetail.getName() + "_" + eachBay.getBayName());
						ArrayList<String> qrPositionNoList = new ArrayList<String>();
						for (QrScanner eachQrDevice : eachTerminal.getQrScanner()) {
							if ((eachQrDevice.getClusterId().equals(clusterId))
									&& (eachQrDevice.getBayId().equals(bayId))) {
								// getClusterBayNamePositionListMap().put
								qrPositionNoList.add(eachQrDevice.getPositionId());
								getQrClusterBayPositionNoCnameMap().put(
										eachClusterDetail.getName() + "_" + eachBay.getBayName() + "_"
												+ eachQrDevice.getPositionId(),
										eachQrDevice.getPortName());
								getQrClusterBayPositionNoDeviceIdMap().put(
										eachClusterDetail.getName() + "_" + eachBay.getBayName() + "_"
												+ eachQrDevice.getPositionId(),
										eachQrDevice.getDeviceId());

							}

						}
						if (qrPositionNoList.size() > 0) {
							getQrClusterBayNamePositionListMap()
									.put(eachClusterDetail.getName() + "_" + eachBay.getBayName(), qrPositionNoList);
						}

						/////////////////////////////////////

						ArrayList<String> dutPositionNoList = new ArrayList<String>();
						for (DutDevice eachDutDevice : eachTerminal.getDutDevice()) {
							if ((eachDutDevice.getClusterId().equals(clusterId))
									&& (eachDutDevice.getBayId().equals(bayId))) {
								// getClusterBayNamePositionListMap().put
								dutPositionNoList.add(eachDutDevice.getPositionId());
								getDutClusterBayPositionNoCnameMap().put(
										eachClusterDetail.getName() + "_" + eachBay.getBayName() + "_"
												+ eachDutDevice.getPositionId(),
										eachDutDevice.getPortName());
								getDutClusterBayPositionNoDeviceIdMap().put(
										eachClusterDetail.getName() + "_" + eachBay.getBayName() + "_"
												+ eachDutDevice.getPositionId(),
										eachDutDevice.getDeviceId());

							}

						}
						if (dutPositionNoList.size() > 0) {
							getDutClusterBayNamePositionListMap()
									.put(eachClusterDetail.getName() + "_" + eachBay.getBayName(), dutPositionNoList);
						}
						//////////////////////////////////////////////////

						/////////////////////////////////////

						ArrayList<String> megaOhmPositionNoList = new ArrayList<String>();
						ArrayList<String> megaOhmAddressList = new ArrayList<String>();
						for (MegaOhmMeter eachMegaOhmMeterDevice : eachTerminal.getMegaOhmMeter()) {
							if ((eachMegaOhmMeterDevice.getClusterId().equals(clusterId))
									&& (eachMegaOhmMeterDevice.getBayId().equals(bayId))) {
								// getClusterBayNamePositionListMap().put
								megaOhmPositionNoList.add(eachMegaOhmMeterDevice.getPositionId());
								getMegaOhmMeterClusterBayPositionNoCnameMap().put(
										eachClusterDetail.getName() + "_" + eachBay.getBayName() + "_"
												+ eachMegaOhmMeterDevice.getPositionId(),
										eachMegaOhmMeterDevice.getPortName());
								getMegaOhmMeterClusterBayPositionNoDeviceIdMap().put(
										eachClusterDetail.getName() + "_" + eachBay.getBayName() + "_"
												+ eachMegaOhmMeterDevice.getPositionId(),
										eachMegaOhmMeterDevice.getDeviceId());
								ApplicationLauncher.logger.debug(
										"loadDataFromConfig-M : getDeviceId :" + eachMegaOhmMeterDevice.getDeviceId());
								if (eachMegaOhmMeterDevice.isRs485Enabled()) {
									megaOhmAddressList = eachMegaOhmMeterDevice.getRs485DeviceIdList();
									ApplicationLauncher.logger.debug(
											"loadDataFromConfig-M : addressList :" + megaOhmAddressList.toString());
								}

								if (megaOhmAddressList.size() > 0) {
									String formattedPositionId = String.format("%02d",
											Integer.parseInt(eachMegaOhmMeterDevice.getPositionId()));
									getMegaOhmMeterClusterBayPositionNoAddressListMap().put(eachClusterDetail.getName()
											+ "_" + eachBay.getBayName() + "_" + formattedPositionId,
											megaOhmAddressList);
								}

							}

						}
						if (megaOhmPositionNoList.size() > 0) {
							getMegaOhmMeterClusterBayNamePositionListMap().put(
									eachClusterDetail.getName() + "_" + eachBay.getBayName(), megaOhmPositionNoList);
						}

						//////////////////////////////////////////////////

						/////////////////////////////////////

						ArrayList<String> voltPositionNoList = new ArrayList<String>();
						ArrayList<String> voltAddressList = new ArrayList<String>();
						for (VoltMeter eachVoltMeterDevice : eachTerminal.getVoltMeter()) {
							if ((eachVoltMeterDevice.getClusterId().equals(clusterId))
									&& (eachVoltMeterDevice.getBayId().equals(bayId))) {
								// getClusterBayNamePositionListMap().put
								voltPositionNoList.add(eachVoltMeterDevice.getPositionId());
								getVoltMeterClusterBayPositionNoCnameMap().put(
										eachClusterDetail.getName() + "_" + eachBay.getBayName() + "_"
												+ eachVoltMeterDevice.getPositionId(),
										eachVoltMeterDevice.getPortName());
								getVoltMeterClusterBayPositionNoDeviceIdMap().put(
										eachClusterDetail.getName() + "_" + eachBay.getBayName() + "_"
												+ eachVoltMeterDevice.getPositionId(),
										eachVoltMeterDevice.getDeviceId());
								ApplicationLauncher.logger.debug(
										"loadDataFromConfig-V : getDeviceId :" + eachVoltMeterDevice.getDeviceId());
								if (eachVoltMeterDevice.isRs485Enabled()) {
									voltAddressList = eachVoltMeterDevice.getRs485DeviceIdList();
									ApplicationLauncher.logger
											.debug("loadDataFromConfig-V : addressList :" + voltAddressList.toString());
								}

								if (voltAddressList.size() > 0) {
									String formattedPositionId = String.format("%02d",
											Integer.parseInt(eachVoltMeterDevice.getPositionId()));
									getVoltMeterClusterBayPositionNoAddressListMap().put(eachClusterDetail.getName()
											+ "_" + eachBay.getBayName() + "_" + formattedPositionId, voltAddressList);
								}

							}

						}
						if (voltPositionNoList.size() > 0) {
							getVoltMeterClusterBayNamePositionListMap()
									.put(eachClusterDetail.getName() + "_" + eachBay.getBayName(), voltPositionNoList);
						}
						//////////////////////////////////////////////////

						/////////////////////////////////////

						ArrayList<String> lduPositionNoList = new ArrayList<String>();
						ArrayList<String> lduAddressList = new ArrayList<String>();
						for (Ldu eachLduDevice : eachTerminal.getLdu()) {
							if ((eachLduDevice.getClusterId().equals(clusterId))
									&& (eachLduDevice.getBayId().equals(bayId))) {
								// getClusterBayNamePositionListMap().put
								lduPositionNoList.add(eachLduDevice.getPositionId());
								getLduClusterBayPositionNoCnameMap().put(
										eachClusterDetail.getName() + "_" + eachBay.getBayName() + "_"
												+ eachLduDevice.getPositionId(),
										eachLduDevice.getPortName());
								getLduClusterBayPositionNoDeviceIdMap().put(
										eachClusterDetail.getName() + "_" + eachBay.getBayName() + "_"
												+ eachLduDevice.getPositionId(),
										eachLduDevice.getDeviceId());
								ApplicationLauncher.logger
										.debug("loadDataFromConfig-L : getDeviceId :" + eachLduDevice.getDeviceId());
								if (eachLduDevice.isRs485Enabled()) {
									lduAddressList = eachLduDevice.getRs485DeviceIdList();
									ApplicationLauncher.logger
											.debug("loadDataFromConfig-L : addressList :" + lduAddressList.toString());
								}

								if (lduAddressList.size() > 0) {
									String formattedPositionId = String.format("%02d",
											Integer.parseInt(eachLduDevice.getPositionId()));
									getLduClusterBayPositionNoAddressListMap().put(eachClusterDetail.getName() + "_"
											+ eachBay.getBayName() + "_" + formattedPositionId, lduAddressList);
								}

							}

						}
						if (lduPositionNoList.size() > 0) {
							getLduClusterBayNamePositionListMap()
									.put(eachClusterDetail.getName() + "_" + eachBay.getBayName(), lduPositionNoList);
						}
						//////////////////////////////////////////////////

					}

				}

			}
		}

		/*
		 * getQrClusterBayPositionNoCnameMap().entrySet().forEach((e)->{
		 * ApplicationLauncher.logger.
		 * debug("loadDataFromConfig-2: getQrClusterBayPositionNoCnameMap: key : " +
		 * e.getKey() + " -> " + e.getValue());
		 * });
		 * 
		 * getQrClusterBayPositionNoDeviceIdMap().entrySet().forEach((e)->{
		 * ApplicationLauncher.logger.
		 * debug("loadDataFromConfig-Qr2: getQrClusterBayPositionNoDeviceIdMap: key : "
		 * + e.getKey() + " -> " + e.getValue());
		 * });
		 * 
		 * 
		 * getDutClusterBayPositionNoCnameMap().entrySet().forEach((e)->{
		 * ApplicationLauncher.logger.
		 * debug("loadDataFromConfig-2: getDutClusterBayPositionNoCnameMap: key : " +
		 * e.getKey() + " -> " + e.getValue());
		 * });
		 * 
		 * getDutClusterBayPositionNoDeviceIdMap().entrySet().forEach((e)->{
		 * ApplicationLauncher.logger.
		 * debug("loadDataFromConfig-Dut2: getDutClusterBayPositionNoDeviceIdMap: key : "
		 * + e.getKey() + " -> " + e.getValue());
		 * });
		 */

		getMegaOhmMeterClusterBayNamePositionListMap().entrySet().forEach((e) -> {
			ApplicationLauncher.logger
					.debug("loadDataFromConfig-OhmMeter: getMegaOhmMeterClusterBayNamePositionListMap: key : "
							+ e.getKey() + " -> " + e.getValue());
		});
		getFilteredClusterBayNamePositionListMap().put(ConstantConveyor.DEVICE_TYPE_QR_SCANNER,
				getQrClusterBayNamePositionListMap());
		getFilteredClusterBayPositionNoCnameMap().put(ConstantConveyor.DEVICE_TYPE_QR_SCANNER,
				getQrClusterBayPositionNoCnameMap());
		getFilteredClusterBayPositionNoDeviceIdMap().put(ConstantConveyor.DEVICE_TYPE_QR_SCANNER,
				getQrClusterBayPositionNoDeviceIdMap());

		getFilteredClusterBayNamePositionListMap().put(ConstantConveyor.DEVICE_TYPE_DUT,
				getDutClusterBayNamePositionListMap());
		getFilteredClusterBayPositionNoCnameMap().put(ConstantConveyor.DEVICE_TYPE_DUT,
				getDutClusterBayPositionNoCnameMap());
		getFilteredClusterBayPositionNoDeviceIdMap().put(ConstantConveyor.DEVICE_TYPE_DUT,
				getDutClusterBayPositionNoDeviceIdMap());

		getFilteredClusterBayNamePositionListMap().put(ConstantConveyor.DEVICE_TYPE_OHM_METER,
				getMegaOhmMeterClusterBayNamePositionListMap());
		getFilteredClusterBayPositionNoCnameMap().put(ConstantConveyor.DEVICE_TYPE_OHM_METER,
				getMegaOhmMeterClusterBayPositionNoCnameMap());
		getFilteredClusterBayPositionNoDeviceIdMap().put(ConstantConveyor.DEVICE_TYPE_OHM_METER,
				getMegaOhmMeterClusterBayPositionNoDeviceIdMap());
		getFilteredClusterBayPositionNoAddressListMap().put(ConstantConveyor.DEVICE_TYPE_OHM_METER,
				getMegaOhmMeterClusterBayPositionNoAddressListMap());

		getFilteredClusterBayNamePositionListMap().put(ConstantConveyor.DEVICE_TYPE_VOLT_METER,
				getVoltMeterClusterBayNamePositionListMap());
		getFilteredClusterBayPositionNoCnameMap().put(ConstantConveyor.DEVICE_TYPE_VOLT_METER,
				getVoltMeterClusterBayPositionNoCnameMap());
		getFilteredClusterBayPositionNoDeviceIdMap().put(ConstantConveyor.DEVICE_TYPE_VOLT_METER,
				getVoltMeterClusterBayPositionNoDeviceIdMap());
		getFilteredClusterBayPositionNoAddressListMap().put(ConstantConveyor.DEVICE_TYPE_VOLT_METER,
				getVoltMeterClusterBayPositionNoAddressListMap());

		getFilteredClusterBayNamePositionListMap().put(ConstantConveyor.DEVICE_TYPE_LDU,
				getLduClusterBayNamePositionListMap());
		getFilteredClusterBayPositionNoCnameMap().put(ConstantConveyor.DEVICE_TYPE_LDU,
				getLduClusterBayPositionNoCnameMap());
		getFilteredClusterBayPositionNoDeviceIdMap().put(ConstantConveyor.DEVICE_TYPE_LDU,
				getLduClusterBayPositionNoDeviceIdMap());
		getFilteredClusterBayPositionNoAddressListMap().put(ConstantConveyor.DEVICE_TYPE_LDU,
				getLduClusterBayPositionNoAddressListMap());

		getFilteredClusterBayPositionNoAddressListMap().entrySet().forEach((e) -> {
			ApplicationLauncher.logger
					.debug("loadDataFromConfig-OhmMeter: getFilteredClusterBayPositionNoAddressListMap: key : "
							+ e.getKey() + " -> " + e.getValue());
		});

	}

	// ========================================================================================
	public List<PalletManage> fetchPalletsByBayState(String bayKey) {

		List<PalletManage> palletManageList = new ArrayList<PalletManage>();
		int noOfPalletsInBay = 1;
		if (bayKey.equals(ConstantConveyor.VERIFICATION_BAY_KEY)) {
			noOfPalletsInBay = DeviceDataManagerController.getConveyorConfigParsedKey().getMaxNoOfPalletsInVerific1();
			ApplicationLauncher.logger.debug("fetchPalletsByBayState: verific");
		} else if (bayKey.equals(ConstantConveyor.STA_NLD1_BAY_KEY)) {
			ApplicationLauncher.logger.debug("fetchPalletsByBayState: sta1");
			noOfPalletsInBay = DeviceDataManagerController.getConveyorConfigParsedKey().getMaxNoOfPalletsInStaNld1();
		} else if (bayKey.equals(ConstantConveyor.STA_NLD2_BAY_KEY)) {
			ApplicationLauncher.logger.debug("fetchPalletsByBayState: sta2");
			noOfPalletsInBay = DeviceDataManagerController.getConveyorConfigParsedKey().getMaxNoOfPalletsInStaNld2();
		} else if (bayKey.equals(ConstantConveyor.WAITING_BAY_KEY)) {
			ApplicationLauncher.logger.debug("fetchPalletsByBayState: waiting bay");
			noOfPalletsInBay = DeviceDataManagerController.getConveyorConfigParsedKey()
					.getMaxNoOfPalletsInWaitingVerific1();
		}
		ApplicationLauncher.logger.debug("fetchPalletsByBayState: noOfPalletsInBay : " + noOfPalletsInBay);
		palletManageList = MySqlServiceManager.getPalletManageService().findTopXActiveByPresentBayKey(bayKey,
				noOfPalletsInBay);
		for (PalletManage eachPalletManage : palletManageList) {
			ApplicationLauncher.logger
					.debug("fetchPalletsByBayState: getPalletDistinctId:    " + eachPalletManage.getPalletDistinctId());
		}
		return palletManageList;
	}

	public List<PalletManage> fetchTillLastWeekPalletsByBayState(String bayKey) {

		List<PalletManage> palletManageList = new ArrayList<PalletManage>();
		int noOfPalletsInBay = 1;
		if (bayKey.equals(ConstantConveyor.VERIFICATION_BAY_KEY)) {
			noOfPalletsInBay = DeviceDataManagerController.getConveyorConfigParsedKey().getMaxNoOfPalletsInVerific1();
			ApplicationLauncher.logger.debug("fetchPalletsByBayState: verific");
		} else if (bayKey.equals(ConstantConveyor.STA_NLD1_BAY_KEY)) {
			ApplicationLauncher.logger.debug("fetchPalletsByBayState: sta1");
			noOfPalletsInBay = DeviceDataManagerController.getConveyorConfigParsedKey().getMaxNoOfPalletsInStaNld1();
		} else if (bayKey.equals(ConstantConveyor.STA_NLD2_BAY_KEY)) {
			ApplicationLauncher.logger.debug("fetchPalletsByBayState: sta2");
			noOfPalletsInBay = DeviceDataManagerController.getConveyorConfigParsedKey().getMaxNoOfPalletsInStaNld2();
		} else if (bayKey.equals(ConstantConveyor.WAITING_BAY_KEY)) {
			ApplicationLauncher.logger.debug("fetchPalletsByBayState: waiting bay");
			noOfPalletsInBay = DeviceDataManagerController.getConveyorConfigParsedKey()
					.getMaxNoOfPalletsInWaitingVerific1();
		}
		ApplicationLauncher.logger.debug("fetchPalletsByBayState: noOfPalletsInBay : " + noOfPalletsInBay);
		palletManageList = MySqlServiceManager.getPalletManageService().findLastWeekTopXActiveByPresentBayKey(bayKey,
				noOfPalletsInBay);
		for (PalletManage eachPalletManage : palletManageList) {
			ApplicationLauncher.logger
					.debug("fetchPalletsByBayState: getPalletDistinctId:    " + eachPalletManage.getPalletDistinctId());
		}
		return palletManageList;
	}
	// GENERATE MODBUS WORD FOR MOTOR REQUIREMENT

	public static int generateModBusWord(List<String> values) {

		// Pad with "NR" at the end if size < 16 (LSB side padding)
		int paddingNeeded = 16 - values.size();
		List<String> paddedList = new ArrayList<>(values);

		for (int i = 0; i < paddingNeeded; i++) {
			paddedList.add("NR");
		}

		System.out.println("paddedList : " + paddedList);

		int value = 0;
		for (int i = 0; i < 16; i++) {
			if (paddedList.get(i).equals("R")) {
				value |= (1 << i); // LSB at index 0, MSB at index 15
			}
		}

		// Format to 16-bit binary string
		String binaryString = String.format("%16s", Integer.toBinaryString(value)).replace(' ', '0');
		System.out.println("Binary value: " + binaryString);

		int word = Integer.parseInt(binaryString, 2);
		System.out.println("Word value: " + word);

		return word;
	}

	// ========================================================================================

	public Map<String, Object> set_motor_required(String bayKey) {

		boolean status = false;
		String state = "";
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);
		// ============================================================================================
		String address = getBayAddress(bayKey);

		IoPortInfo portInfo = BayUtils.getOutputPortDetails(address);

		if (!ProconFeatureEnable.MOTOR_CONTROL_DISABLE) {
			if (portInfo != null) {
				ApplicationLauncher.logger.debug("set_motor_required: ClusterId :" + portInfo.getClusterId()
						+ "-> BayId: " + portInfo.getBayId() + "-> PortId: " + portInfo.getPortId());
				// ApplicationLauncher.logger.debug("ClusterId : " + portInfo.getClusterId());
				// ApplicationLauncher.logger.debug("BayId : " + portInfo.getBayId());

				List<String> motor_requirement = getMotorRequirement(bayKey);
				int outputValue = BayUtils.generateModBusWord(motor_requirement);

				if (ProcalFeatureEnable.MODBUS_PLC_SLAVE_MODE) {
					state = setOutputWordToPlcBay(portInfo.getClusterId(), portInfo.getBayId(), portInfo.getPortId(),
							outputValue);
				} else {
					state = setOutputWordToPlcBay(portInfo.getClusterId(), portInfo.getBayId(), portInfo.getPortId(),
							outputValue);
				}

				ApplicationLauncher.logger.debug("Bay Utils : set_motor_required : state : " + state);

				status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false;

				ApplicationLauncher.logger.debug("Bay Utils : set_motor_required : status : " + status);

			} else {
				ApplicationLauncher.logger.debug("Bay Utils : Output port not found");
			}
		}
		if (StateExecutorController.simulateFtBayHappyPath) {
			status = true;
		}

		state = Constant_IO_ActionMapping.OFF;
		status = true;

		responseReturn.put("status", status);
		responseReturn.put("responseData", state);

		ApplicationLauncher.logger.debug("Bay Utils : set_motor_not_required : status : " + status);
		// ============================================================================================
		ApplicationLauncher.logger.debug("Bay Utils : set_motor_not_required : Exit");
		// return status;
		return responseReturn;
	}

	// ========================================================================================

	public Map<String, Object> set_motor_required(String bayKey, List<String> motor_requirement) {

		boolean status = false;
		String state = "";
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);
		// ============================================================================================
		String address = getBayAddress(bayKey);

		IoPortInfo portInfo = BayUtils.getOutputPortDetails(address);

		if (!ProconFeatureEnable.MOTOR_CONTROL_DISABLE) {
			if (portInfo != null) {
				/*
				 * ApplicationLauncher.logger.debug("PortId    : " + portInfo.getPortId());
				 * ApplicationLauncher.logger.debug("ClusterId : " + portInfo.getClusterId());
				 * ApplicationLauncher.logger.debug("BayId     : " + portInfo.getBayId());
				 */
				ApplicationLauncher.logger.debug("set_motor_required With Req: ClusterId :" + portInfo.getClusterId()
						+ "-> BayId: " + portInfo.getBayId() + "-> PortId: " + portInfo.getPortId());

				// List<String> motor_requirement = getMotorRequirement(bayKey);
				int outputValue = BayUtils.generateModBusWord(motor_requirement);

				if (ProcalFeatureEnable.MODBUS_PLC_SLAVE_MODE) {
					state = setOutputWordToPlcBay(portInfo.getClusterId(), portInfo.getBayId(), portInfo.getPortId(),
							outputValue);
				} else {
					state = setOutputWordToPlcBay(portInfo.getClusterId(), portInfo.getBayId(), portInfo.getPortId(),
							outputValue);
				}

				ApplicationLauncher.logger.debug("Bay Utils : set_motor_required : state : " + state);

				status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false;

				ApplicationLauncher.logger.debug("Bay Utils : set_motor_required : status : " + status);

			} else {
				ApplicationLauncher.logger.debug("Bay Utils : Output port not found");
			}
		}
		if (StateExecutorController.simulateFtBayHappyPath) {
			status = true;
		}

		state = Constant_IO_ActionMapping.OFF;
		status = true;

		responseReturn.put("status", status);
		responseReturn.put("responseData", state);

		ApplicationLauncher.logger.debug("Bay Utils : set_motor_not_required : status : " + status);
		// ============================================================================================
		ApplicationLauncher.logger.debug("Bay Utils : set_motor_not_required : Exit");
		// return status;
		return responseReturn;
	}

	// ========================================================================================

	public Map<String, Object> set_motor_not_required(String bayKey) {

		boolean status = false;
		String state = "";
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);
		// ============================================================================================

		String address = getBayAddress(bayKey);

		IoPortInfo portInfo = BayUtils.getOutputPortDetails(address);

		if (!ProconFeatureEnable.MOTOR_CONTROL_DISABLE) {
			if (portInfo != null) {
				// ApplicationLauncher.logger.debug("PortId : " + portInfo.getPortId());
				// ApplicationLauncher.logger.debug("ClusterId : " + portInfo.getClusterId());
				// ApplicationLauncher.logger.debug("BayId : " + portInfo.getBayId());

				ApplicationLauncher.logger.debug("set_motor_not_required: ClusterId :" + portInfo.getClusterId()
						+ "-> BayId: " + portInfo.getBayId() + "-> PortId: " + portInfo.getPortId());

				int outputValue = 0;

				if (ProcalFeatureEnable.MODBUS_PLC_SLAVE_MODE) {
					state = setOutputWordToPlcBay(portInfo.getClusterId(), portInfo.getBayId(), portInfo.getPortId(),
							outputValue);
				} else {
					state = setOutputWordToPlcBay(portInfo.getClusterId(), portInfo.getBayId(), portInfo.getPortId(),
							outputValue);
				}

				ApplicationLauncher.logger.debug("Bay Utils : set_motor_not_required : state : " + state);

				status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false;

				ApplicationLauncher.logger.debug("Bay Utils : set_motor_not_required : status : " + status);

			} else {
				ApplicationLauncher.logger.debug("Bay Utils : Output port not found");
			}
		}
		if (StateExecutorController.simulateFtBayHappyPath) {
			status = true;
		}

		state = Constant_IO_ActionMapping.OFF;
		status = true;

		responseReturn.put("status", status);
		responseReturn.put("responseData", state);

		ApplicationLauncher.logger.debug("Bay Utils : set_motor_not_required : status : " + status);
		// ============================================================================================
		ApplicationLauncher.logger.debug("Bay Utils : set_motor_not_required : Exit");
		// return status;
		return responseReturn;
	}

	// ========================================================================================

	public String getBayAddress(String bayKey) {
		switch (bayKey) {
			case ConstantConveyor.FT_BAY_KEY:
				return ConstantBayPortNameMapping.FT_PORT_NAME_ADDRESS;
			case ConstantConveyor.HV_BAY_KEY:
				return ConstantBayPortNameMapping.HV_PORT_NAME_ADDRESS;
			case ConstantConveyor.IR_BAY_KEY:
				return ConstantBayPortNameMapping.IR_PORT_NAME_ADDRESS;
			case ConstantConveyor.CALIBRATION_BAY_KEY:
				return ConstantBayPortNameMapping.CALIB_PORT_NAME_ADDRESS;
			case ConstantConveyor.COMMUNICATION_BAY_KEY:
				return ConstantBayPortNameMapping.COMM_PORT_NAME_ADDRESS;
			case ConstantConveyor.LOADING_BAY_KEY:
				return ConstantBayPortNameMapping.LOADING_PORT_NAME_ADDRESS;
			case ConstantConveyor.REJECTION_BAY_KEY:
				return ConstantBayPortNameMapping.REJECTION_PORT_NAME_ADDRESS;
			case ConstantConveyor.STA_NLD1_BAY_KEY:
				return ConstantBayPortNameMapping.STA1_PORT_NAME_ADDRESS;
			case ConstantConveyor.STA_NLD2_BAY_KEY:
				return ConstantBayPortNameMapping.STA2_PORT_NAME_ADDRESS;
			case ConstantConveyor.WAITING_BAY_KEY:
				return ConstantBayPortNameMapping.WAITING_PORT_NAME_ADDRESS;
			case ConstantConveyor.VERIFICATION_BAY_KEY:
				return ConstantBayPortNameMapping.VERIFIC_PORT_NAME_ADDRESS;
			case ConstantConveyor.UNLOADING_BAY_KEY:
				return ConstantBayPortNameMapping.UNLOADING_PORT_NAME_ADDRESS;
			default:
				return null; // or throw an IllegalArgumentException if preferred
		}
	}

	// ========================================================================================

	public List<String> getMotorRequirement(String bayKey) {
		switch (bayKey) {
			/*
			 * case ConstantConveyor.FT_BAY_KEY:
			 * return Constant_Motor_Requirement.ft
			 */
			case ConstantConveyor.HV_BAY_KEY:
				return Constant_Motor_Requirement.HV_MOTOR_REQUIRED;
			case ConstantConveyor.IR_BAY_KEY:
				return Constant_Motor_Requirement.IR_MOTOR_REQUIRED;
			case ConstantConveyor.CALIBRATION_BAY_KEY:
				return Constant_Motor_Requirement.CALIB_MOTOR_REQUIRED;
			case ConstantConveyor.COMMUNICATION_BAY_KEY:
				return Constant_Motor_Requirement.COMM_MOTOR_REQUIRED;
			case ConstantConveyor.LOADING_BAY_KEY:
				return Constant_Motor_Requirement.LOADING_MOTOR_REQUIRED;
			case ConstantConveyor.REJECTION_BAY_KEY:
				return Constant_Motor_Requirement.REJECTION_MOTOR_REQUIRED;
			case ConstantConveyor.STA_NLD1_BAY_KEY:
				return Constant_Motor_Requirement.STA1_MOTOR_REQUIRED;
			case ConstantConveyor.STA_NLD2_BAY_KEY:
				return Constant_Motor_Requirement.STA2_MOTOR_REQUIRED;
			case ConstantConveyor.WAITING_BAY_KEY:
				return Constant_Motor_Requirement.WAITING_MOTOR_REQUIRED;
			case ConstantConveyor.UNLOADING_BAY_KEY:
				return Constant_Motor_Requirement.UNLOADING_MOTOR_REQUIRED;
			default:
				throw new IllegalArgumentException("Invalid bay key: " + bayKey);
		}
	}

	// ========================================================================================

	public static TerminalBayConfigModel getBayConfigModel() {
		return bayConfigModel;
	}
	// ========================================================================================

	public static void setBayConfigModel(TerminalBayConfigModel bayConfigModel) {
		BayUtils.bayConfigModel = bayConfigModel;
	}

	// ========================================================================================
	/*
	 * public static Map<String, ArrayList<String>> getClusterBayNameListMap() {
	 * return clusterBayNameListMap;
	 * }
	 * public static Map<String, String> getClusterNameIdListMap() {
	 * return clusterNameIdListMap;
	 * }
	 * public static Map<String, String> getClusterBayNameIdMap() {
	 * return clusterBayNameIdMap;
	 * }
	 * public static Map<String, ArrayList<String>>
	 * getClusterBayNamePositionListMap() {
	 * return clusterBayNamePositionListMap;
	 * }
	 * public static Map<String, String> getClusterBayPositionNoCnameMap() {
	 * return clusterBayPositionNoCnameMap;
	 * }
	 * public static Map<String, String> getClusterBayPositionNoDeviceIdMap() {
	 * return clusterBayPositionNoDeviceIdMap;
	 * }
	 * public static Map<String, ArrayList<String>>
	 * getClusterBayPositionNoAddressListMap() {
	 * return clusterBayPositionNoAddressListMap;
	 * }
	 * public static void setClusterBayNameListMap(Map<String, ArrayList<String>>
	 * clusterBayNameListMap) {
	 * BayUtils.clusterBayNameListMap = clusterBayNameListMap;
	 * }
	 * public static void setClusterNameIdListMap(Map<String, String>
	 * clusterNameIdListMap) {
	 * BayUtils.clusterNameIdListMap = clusterNameIdListMap;
	 * }
	 * public static void setClusterBayNameIdMap(Map<String, String>
	 * clusterBayNameIdMap) {
	 * BayUtils.clusterBayNameIdMap = clusterBayNameIdMap;
	 * }
	 * public static void setClusterBayNamePositionListMap(Map<String,
	 * ArrayList<String>> clusterBayNamePositionListMap) {
	 * BayUtils.clusterBayNamePositionListMap = clusterBayNamePositionListMap;
	 * }
	 * public static void setClusterBayPositionNoCnameMap(Map<String, String>
	 * clusterBayPositionNoCnameMap) {
	 * BayUtils.clusterBayPositionNoCnameMap = clusterBayPositionNoCnameMap;
	 * }
	 * public static void setClusterBayPositionNoDeviceIdMap(Map<String, String>
	 * clusterBayPositionNoDeviceIdMap) {
	 * BayUtils.clusterBayPositionNoDeviceIdMap = clusterBayPositionNoDeviceIdMap;
	 * }
	 * public static void setClusterBayPositionNoAddressListMap(
	 * Map<String, ArrayList<String>> clusterBayPositionNoAddressListMap) {
	 * BayUtils.clusterBayPositionNoAddressListMap =
	 * clusterBayPositionNoAddressListMap;
	 * }
	 */
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
		BayUtils.clusterBayNameListMap = clusterBayNameListMap;
	}

	public static void setClusterNameIdListMap(Map<String, String> clusterNameIdListMap) {
		BayUtils.clusterNameIdListMap = clusterNameIdListMap;
	}

	public static void setClusterBayNameIdMap(Map<String, String> clusterBayNameIdMap) {
		BayUtils.clusterBayNameIdMap = clusterBayNameIdMap;
	}

	public static void setQrClusterBayNamePositionListMap(
			Map<String, ArrayList<String>> qrClusterBayNamePositionListMap) {
		BayUtils.qrClusterBayNamePositionListMap = qrClusterBayNamePositionListMap;
	}

	public static void setQrClusterBayPositionNoCnameMap(Map<String, String> qrClusterBayPositionNoCnameMap) {
		BayUtils.qrClusterBayPositionNoCnameMap = qrClusterBayPositionNoCnameMap;
	}

	public static void setQrClusterBayPositionNoDeviceIdMap(Map<String, String> qrClusterBayPositionNoDeviceIdMap) {
		BayUtils.qrClusterBayPositionNoDeviceIdMap = qrClusterBayPositionNoDeviceIdMap;
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
		BayUtils.dutClusterBayNamePositionListMap = dutClusterBayNamePositionListMap;
	}

	public static void setDutClusterBayPositionNoCnameMap(Map<String, String> dutClusterBayPositionNoCnameMap) {
		BayUtils.dutClusterBayPositionNoCnameMap = dutClusterBayPositionNoCnameMap;
	}

	public static void setDutClusterBayPositionNoDeviceIdMap(Map<String, String> dutClusterBayPositionNoDeviceIdMap) {
		BayUtils.dutClusterBayPositionNoDeviceIdMap = dutClusterBayPositionNoDeviceIdMap;
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
		BayUtils.filteredClusterBayNamePositionListMap = filteredClusterBayNamePositionListMap;
	}

	public static void setFilteredClusterBayPositionNoCnameMap(
			Map<String, Map<String, String>> filteredClusterBayPositionNoCnameMap) {
		BayUtils.filteredClusterBayPositionNoCnameMap = filteredClusterBayPositionNoCnameMap;
	}

	public static void setFilteredClusterBayPositionNoDeviceIdMap(
			Map<String, Map<String, String>> filteredClusterBayPositionNoDeviceIdMap) {
		BayUtils.filteredClusterBayPositionNoDeviceIdMap = filteredClusterBayPositionNoDeviceIdMap;
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
		BayUtils.megaOhmMeterClusterBayNamePositionListMap = megaOhmMeterClusterBayNamePositionListMap;
	}

	public static void setMegaOhmMeterClusterBayPositionNoCnameMap(
			Map<String, String> megaOhmMeterClusterBayPositionNoCnameMap) {
		BayUtils.megaOhmMeterClusterBayPositionNoCnameMap = megaOhmMeterClusterBayPositionNoCnameMap;
	}

	public static void setMegaOhmMeterClusterBayPositionNoDeviceIdMap(
			Map<String, String> megaOhmMeterClusterBayPositionNoDeviceIdMap) {
		BayUtils.megaOhmMeterClusterBayPositionNoDeviceIdMap = megaOhmMeterClusterBayPositionNoDeviceIdMap;
	}

	public static void setVoltMeterClusterBayNamePositionListMap(
			Map<String, ArrayList<String>> voltMeterClusterBayNamePositionListMap) {
		BayUtils.voltMeterClusterBayNamePositionListMap = voltMeterClusterBayNamePositionListMap;
	}

	public static void setVoltMeterClusterBayPositionNoCnameMap(
			Map<String, String> voltMeterClusterBayPositionNoCnameMap) {
		BayUtils.voltMeterClusterBayPositionNoCnameMap = voltMeterClusterBayPositionNoCnameMap;
	}

	public static void setVoltMeterClusterBayPositionNoDeviceIdMap(
			Map<String, String> voltMeterClusterBayPositionNoDeviceIdMap) {
		BayUtils.voltMeterClusterBayPositionNoDeviceIdMap = voltMeterClusterBayPositionNoDeviceIdMap;
	}

	public static void setLduClusterBayNamePositionListMap(
			Map<String, ArrayList<String>> lduClusterBayNamePositionListMap) {
		BayUtils.lduClusterBayNamePositionListMap = lduClusterBayNamePositionListMap;
	}

	public static void setLduClusterBayPositionNoCnameMap(Map<String, String> lduClusterBayPositionNoCnameMap) {
		BayUtils.lduClusterBayPositionNoCnameMap = lduClusterBayPositionNoCnameMap;
	}

	public static void setLduClusterBayPositionNoDeviceIdMap(Map<String, String> lduClusterBayPositionNoDeviceIdMap) {
		BayUtils.lduClusterBayPositionNoDeviceIdMap = lduClusterBayPositionNoDeviceIdMap;
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
		BayUtils.megaOhmMeterClusterBayPositionNoAddressListMap = megaOhmMeterClusterBayPositionNoAddressListMap;
	}

	public void setVoltMeterClusterBayPositionNoAddressListMap(
			Map<String, ArrayList<String>> voltMeterClusterBayPositionNoAddressListMap) {
		BayUtils.voltMeterClusterBayPositionNoAddressListMap = voltMeterClusterBayPositionNoAddressListMap;
	}

	public void setLduClusterBayPositionNoAddressListMap(
			Map<String, ArrayList<String>> lduMeterClusterBayPositionNoAddressListMap) {
		BayUtils.lduClusterBayPositionNoAddressListMap = lduMeterClusterBayPositionNoAddressListMap;
	}

	public static Map<String, Map<String, ArrayList<String>>> getFilteredClusterBayPositionNoAddressListMap() {
		return filteredClusterBayPositionNoAddressListMap;
	}

	public static void setFilteredClusterBayPositionNoAddressListMap(
			Map<String, Map<String, ArrayList<String>>> filteredClusterBayPositionNoAddressListMap) {
		BayUtils.filteredClusterBayPositionNoAddressListMap = filteredClusterBayPositionNoAddressListMap;
	}

	public static ModbusTcpClientManager getModbusTcpClientManager() {
		return modbusTcpClientManager;
	}

	public static void setModbusTcpClientManager(ModbusTcpClientManager modbusTcpClientManager) {
		BayUtils.modbusTcpClientManager = modbusTcpClientManager;
	}

	// public void computeMeterOverAllStatus(PalletManage myPalletManage ){
	public void computeMeterOverAllStatus(String palletDistinctId) {
		ApplicationLauncher.logger.debug("BayUtils: computeMeterOverAllStatus: Entry");

		Optional<PalletManage> myPalletManageOpt = MySqlServiceManager.getPalletManageService()
				.findByPalletDistinctId(palletDistinctId);
		if (myPalletManageOpt.isPresent()) {
			PalletManage myPalletManage = myPalletManageOpt.get();
			//// ********************************************************************
			// this logic need to be modified additiona validation all test case completed
			//// .s since verification has lot of test
			// **********************************************************************
			String meterOverAllStatus = ConstantReport.REPORT_POPULATE_WFR;

			Map<String, Boolean> meterTestTypeResultTemplate = new HashMap<String, Boolean>();
			for (String eachTestType : ConstantConveyor.CONV_TEST_TYPE_ALIAS_LIST) {
				if (!ConstantConveyor.INACTIVE_TEST_TYPE_ALIAS_LIST.contains(eachTestType)) {
					meterTestTypeResultTemplate.put(eachTestType, false);
				}
			}
			ApplicationLauncher.logger.debug("BayUtils: computeMeterOverAllStatus: getPalletDistinctId: "
					+ myPalletManage.getPalletDistinctId());
			ApplicationLauncher.logger.debug(
					"BayUtils: computeMeterOverAllStatus: meterTestTypeResultTemplate: " + meterTestTypeResultTemplate);
			Set<PalletMeter> palletMeterSetList = myPalletManage.getPalletMeterList();
			List<PalletMeter> sortedPalletMeterList = palletMeterSetList.stream()
					.sorted(Comparator.comparingInt(PalletMeter::getRackPositionNo)).collect(Collectors.toList());
			for (int i = 0; i < sortedPalletMeterList.size(); i++) {
				// meterResultSummary = ref_tvMeterResultSummary.getItems().get(i);
				meterOverAllStatus = ConstantReport.REPORT_POPULATE_WFR;

				String presentMeterSerialNo = sortedPalletMeterList.get(i).getMeterSerialNo();
				// String palletDistinctId = myPalletManage.getPalletDistinctId();

				PalletMeter palletMeter = sortedPalletMeterList.get(i);//// MySqlServiceManager.getPalletMeterService().findByMeterSerialNoAndPalletDistinctId(presentMeterSerialNo,palletDistinctId);
				palletMeter.getRackPositionNo();
				if (palletMeter != null) {

					Map<String, Boolean> meterTestTypeResultPresent = new HashMap<String, Boolean>(
							meterTestTypeResultTemplate);

					/*
					 * palletMeter.getPalletMeterResultsList().stream().forEach(e->{
					 * if(palletMeter.getPalletMeterResultsList().contains(o)) {
					 * 
					 * }
					 * meterTestTypePresent.put(e.getTestType(), true);
					 * });
					 */
					ApplicationLauncher.logger
							.debug("BayUtils: computeMeterOverAllStatus: meterTestTypeResultPresent-1 : "
									+ meterTestTypeResultPresent);
					palletMeter.getPalletMeterResultsList().stream()
							// .filter(e->e.getResultActive())
							.forEach(e -> {
								ApplicationLauncher.logger
										.debug("BayUtils: computeMeterOverAllStatus: getPalletMeterResultsList : "
												+ e.getMeterSerialNo() + " : " + e.getTestType());

								if (meterTestTypeResultPresent.containsKey(e.getTestType())) {
									meterTestTypeResultPresent.put(e.getTestType(), true);
								} else {
									ApplicationLauncher.logger.debug(
											"BayUtils: computeMeterOverAllStatus: skipping  : Test type validation: "
													+ e.getTestType());
								}
							});

					ApplicationLauncher.logger
							.debug("BayUtils: computeMeterOverAllStatus: meterTestTypeResultPresent-2 : "
									+ meterTestTypeResultPresent);
					ApplicationLauncher.logger.debug(
							"BayUtils: computeMeterOverAllStatus: presentMeterSerialNo : " + presentMeterSerialNo);

					/*
					 * meterTestTypeResultPresent.entrySet().stream().forEach(e->{
					 * ApplicationLauncher.logger.
					 * debug("BayUtils: computeMeterOverAllStatus: meterTestTypeResultPresent : key: "
					 * + e.getKey() + " -> " + e.getValue());
					 * //meterTestTypePresent.put(e.getTestType(), true);
					 * });
					 */

					StringBuilder nonExecutedResultMessage = new StringBuilder();
					meterTestTypeResultPresent.entrySet().stream().forEach(e -> {
						ApplicationLauncher.logger
								.debug("BayUtils: computeMeterOverAllStatus: meterTestTypeResultPresent : key: "
										+ e.getKey() + " -> " + e.getValue());
						// meterTestTypePresent.put(e.getTestType(), true);
						if (!e.getValue()) {
							if (nonExecutedResultMessage.length() > 0) {
								nonExecutedResultMessage.append(", ");
							}
							nonExecutedResultMessage.append(e.getKey()).append(":TBD");

						}
					});
					ApplicationLauncher.logger.debug("BayUtils: computeMeterOverAllStatus: nonExecutedResultMessage : "
							+ nonExecutedResultMessage);
					Boolean allTestTypeCompleted = meterTestTypeResultPresent.entrySet().stream()
							.allMatch(e -> e.getValue().equals(true)); // this logic need to be modified for all testing
																		// completed
					ApplicationLauncher.logger.debug(
							"BayUtils: computeMeterOverAllStatus: allTestTypeCompleted : " + allTestTypeCompleted);
					if (allTestTypeCompleted) {

						palletMeter.getPalletMeterResultsList().stream().forEachOrdered(e -> {
							ApplicationLauncher.logger.debug(
									"BayUtils: Result : bay: " + e.getBayStateKey() + ", TestType : " + e.getTestType()
											+ ", Position: " + e.getRackPositionNo() + ", ResultStatus: <"
											+ e.getResultStatus() + ">, ResultValue: <" + e.getResultValue() + ">");
						});
						/*
						 * boolean failedResultFound =
						 * palletMeter.getPalletMeterResultsList().stream().filter(e->e.getMeterSerialNo
						 * ().equals(presentMeterSerialNo))
						 * .anyMatch(e->e.getResultStatus().equals(ConstantReport.REPORT_POPULATE_FAIL))
						 * ;
						 * ApplicationLauncher.logger.
						 * debug("BayUtils: computeMeterOverAllStatus: presentMeterSerialNo: " +
						 * presentMeterSerialNo + " : failedResultFound : " + failedResultFound);
						 * boolean wfrResultFound =
						 * palletMeter.getPalletMeterResultsList().stream().filter(e->e.getMeterSerialNo
						 * ().equals(presentMeterSerialNo))
						 * .anyMatch(e->e.getResultStatus().equals(ConstantReport.REPORT_POPULATE_WFR));
						 * ApplicationLauncher.logger.
						 * debug("BayUtils: computeMeterOverAllStatus: presentMeterSerialNo: " +
						 * presentMeterSerialNo + " : wfrResultFound : " + wfrResultFound);
						 */

						String failureReason = "";
						boolean batchFailedResultFound = palletMeter.getPalletMeterResultsList().stream()
								.filter(e -> e.getMeterSerialNo().equals(presentMeterSerialNo))
								.anyMatch(e -> e.getResultValue()
										.contains(ConstantConveyor.REASON_RESULT_BATCH_FAILED_DISPLAY_HEADER));
						ApplicationLauncher.logger.debug("BayUtils: computeMeterOverAllStatus: presentMeterSerialNo: "
								+ presentMeterSerialNo + " : batchFailedResultFound status: " + batchFailedResultFound);

						/*
						 * if(batchFailedResultFound) {
						 * ApplicationLauncher.logger.
						 * debug("BayUtils: computeMeterOverAllStatus: batch failed found");
						 * String batchFailedTestTypesCsv =
						 * palletMeter.getPalletMeterResultsList().stream()
						 * .filter(e -> e.getMeterSerialNo().equals(presentMeterSerialNo))
						 * .filter(e -> e.getResultStatus().equals(ConstantReport.REPORT_POPULATE_FAIL))
						 * .filter(e -> e.getResultValue().contains(ConstantConveyor.
						 * REASON_RESULT_BATCH_FAILED_DISPLAY_HEADER))
						 * .map(e -> e.getTestType() + ":" +
						 * ConstantConveyor.REASON_RESULT_BATCH_FAILED_DISPLAY_HEADER )
						 * .distinct() // optional, if you want unique test types
						 * .collect(Collectors.joining(","));
						 * //failedTestTypesCsv = "Failed: " + failedTestTypesCsv;
						 * batchFailedTestTypesCsv = ConstantConveyor.REASON_RESULT_FAILED_DISPLAY +
						 * batchFailedTestTypesCsv;
						 * ApplicationLauncher.logger.
						 * debug("BayUtils: computeMeterOverAllStatus: batch failedResultFound : presentMeterSerialNo: "
						 * + presentMeterSerialNo + " : batchFailedTestTypesCsv : " +
						 * batchFailedTestTypesCsv);
						 * failureReason = batchFailedTestTypesCsv;// + "\n" + wfrResultFoundCsv;
						 * 
						 * if(!failureReason.isEmpty()){// removing last newline char
						 * if (failureReason.endsWith("\n")) {// removing last newline char
						 * failureReason = failureReason.substring(0, failureReason.length() - 1);
						 * }
						 * }
						 * ApplicationLauncher.logger.
						 * debug("BayUtils: computeMeterOverAllStatus: batch : presentMeterSerialNo: " +
						 * presentMeterSerialNo + " : failureReason : " + failureReason);
						 * palletMeter.setErrorCode(failureReason);
						 * 
						 * meterOverAllStatus = ConstantReport.REPORT_POPULATE_FAIL;
						 * }else {
						 */

						ApplicationLauncher.logger.debug("BayUtils: computeMeterOverAllStatus: batch failed NOT found");
						boolean failedResultFound = palletMeter.getPalletMeterResultsList().stream()
								.filter(e -> e.getMeterSerialNo().equals(presentMeterSerialNo))
								.anyMatch(e -> ((e.getResultStatus().equals(ConstantReport.REPORT_POPULATE_FAIL))
										&& (!e.getResultValue().contains(
												ConstantConveyor.REASON_RESULT_BATCH_FAILED_DISPLAY_HEADER))));
						ApplicationLauncher.logger.debug("BayUtils: computeMeterOverAllStatus: presentMeterSerialNo: "
								+ presentMeterSerialNo + " : failedResultFound status : " + failedResultFound);
						boolean wfrResultFound = palletMeter.getPalletMeterResultsList().stream()
								.filter(e -> e.getMeterSerialNo().equals(presentMeterSerialNo))
								.anyMatch(e -> e.getResultStatus().equals(ConstantReport.REPORT_POPULATE_WFR));
						ApplicationLauncher.logger.debug("BayUtils: computeMeterOverAllStatus: presentMeterSerialNo: "
								+ presentMeterSerialNo + " : wfrResultFound status : " + wfrResultFound);
						if (failedResultFound || wfrResultFound || batchFailedResultFound) {
							ApplicationLauncher.logger.debug(
									"BayUtils: computeMeterOverAllStatus: failedResultFound : presentMeterSerialNo: "
											+ presentMeterSerialNo + " : meterOverAllStatus : " + meterOverAllStatus);

							String batchFailedTestTypesCsv = "";
							if (batchFailedResultFound) {
								ApplicationLauncher.logger
										.debug("BayUtils: computeMeterOverAllStatus: batch failed found");
								batchFailedTestTypesCsv = palletMeter.getPalletMeterResultsList().stream()
										.filter(e -> e.getMeterSerialNo().equals(presentMeterSerialNo))
										.filter(e -> e.getResultStatus().equals(ConstantReport.REPORT_POPULATE_FAIL))
										.filter(e -> e.getResultValue()
												.contains(ConstantConveyor.REASON_RESULT_BATCH_FAILED_DISPLAY_HEADER))
										.map(e -> e.getTestType())
										.distinct() // optional, if you want unique test types
										.collect(Collectors.joining(","));
								// failedTestTypesCsv = "Failed: " + failedTestTypesCsv;
								batchFailedTestTypesCsv = ConstantConveyor.REASON_RESULT_BATCH_FAILED_DISPLAY_HEADER
										+ ":" + batchFailedTestTypesCsv;// ConstantConveyor.REASON_RESULT_FAILED_DISPLAY
																		// + batchFailedTestTypesCsv;
								ApplicationLauncher.logger.debug(
										"BayUtils: computeMeterOverAllStatus: batch failedResultFound : presentMeterSerialNo: "
												+ presentMeterSerialNo + " : batchFailedTestTypesCsv : "
												+ batchFailedTestTypesCsv);
								/*
								 * failureReason = batchFailedTestTypesCsv;// + "\n" + wfrResultFoundCsv;
								 * 
								 * if(!failureReason.isEmpty()){// removing last newline char
								 * if (failureReason.endsWith("\n")) {// removing last newline char
								 * failureReason = failureReason.substring(0, failureReason.length() - 1);
								 * }
								 * }
								 */

							}

							String failedTestTypesCsv = "";
							if (failedResultFound) {
								failedTestTypesCsv = palletMeter.getPalletMeterResultsList().stream()
										.filter(e -> e.getMeterSerialNo().equals(presentMeterSerialNo))
										.filter(e -> ((e.getResultStatus().equals(ConstantReport.REPORT_POPULATE_FAIL))
												&& (!e.getResultValue().contains(
														ConstantConveyor.REASON_RESULT_BATCH_FAILED_DISPLAY_HEADER))))
										.map(e -> e.getTestType())
										.distinct() // optional, if you want unique test types
										.collect(Collectors.joining(","));
								// failedTestTypesCsv = "Failed: " + failedTestTypesCsv;
								failedTestTypesCsv = ConstantConveyor.REASON_RESULT_FAILED_DISPLAY + failedTestTypesCsv;
								ApplicationLauncher.logger.debug(
										"BayUtils: computeMeterOverAllStatus: failedResultFound : presentMeterSerialNo: "
												+ presentMeterSerialNo + " : failedTestTypesCsv : "
												+ failedTestTypesCsv);

							}

							String wfrResultFoundCsv = "";
							if (wfrResultFound) {
								wfrResultFoundCsv = palletMeter.getPalletMeterResultsList().stream()
										.filter(e -> e.getMeterSerialNo().equals(presentMeterSerialNo))
										.filter(e -> e.getResultStatus().equals(ConstantReport.REPORT_POPULATE_WFR))
										.map(e -> e.getTestType())
										.distinct() // optional, if you want unique test types
										.collect(Collectors.joining(","));
								// wfrResultFoundCsv = "WFR: " + wfrResultFoundCsv;
								wfrResultFoundCsv = ConstantConveyor.REASON_RESULT_WFR_DISPLAY + wfrResultFoundCsv;
								ApplicationLauncher.logger.debug(
										"BayUtils: computeMeterOverAllStatus: wfrResultFound : presentMeterSerialNo: "
												+ presentMeterSerialNo + " : wfrResultFoundCsv : " + wfrResultFoundCsv);
							}

							failureReason = batchFailedTestTypesCsv + "\n" + failedTestTypesCsv + "\n"
									+ wfrResultFoundCsv;
							/*
							 * if(!failureReason.isEmpty()){// removing last newline char
							 * failureReason = failureReason.replaceFirst("(?s)[\\r\\n]+\\z", "");
							 * }
							 */
							/*
							 * if(!failureReason.isEmpty()){// removing last newline char
							 * if (failureReason.endsWith("\n")) {// removing last newline char
							 * failureReason = failureReason.substring(0, failureReason.length() - 1);
							 * }
							 * }
							 */
							// failureReason = failureReason.replaceAll("[\\r\\n]+$", "");
							failureReason = failureReason.replaceAll("^[\\r\\n]+|[\\r\\n]+$", "");

							ApplicationLauncher.logger
									.debug("BayUtils: computeMeterOverAllStatus: Combined : presentMeterSerialNo: "
											+ presentMeterSerialNo + " : failureReason : " + failureReason);
							palletMeter.setErrorCode(failureReason);

							meterOverAllStatus = ConstantReport.REPORT_POPULATE_FAIL;
						} else {
							ApplicationLauncher.logger.debug(
									"BayUtils: computeMeterOverAllStatus: failedResultNotFound : Pass hit : presentMeterSerialNo: "
											+ presentMeterSerialNo + " : meterOverAllStatus : " + meterOverAllStatus);

							meterOverAllStatus = ConstantReport.REPORT_POPULATE_PASS;
							palletMeter.setErrorCode("");
						}
						// }
					} else {
						ApplicationLauncher.logger
								.debug("BayUtils: one or few result results missing: nonExecutedResultMessage: "
										+ nonExecutedResultMessage.toString());
						meterOverAllStatus = ConstantReport.REPORT_POPULATE_FAIL;
						palletMeter.setErrorCode(nonExecutedResultMessage.toString());
					}

				}

				// meterResultSummary.setOverAllStatus(meterOverAllStatus);
				// ref_tvMeterResultSummary.getItems().set(i, meterResultSummary);
				ApplicationLauncher.logger.debug("BayUtils: computeMeterOverAllStatus: presentMeterSerialNo: "
						+ presentMeterSerialNo + " : meterOverAllStatus : " + meterOverAllStatus);
				palletMeter.setOverAllTestResultStatus(meterOverAllStatus);
				MySqlServiceManager.getPalletMeterService().save(palletMeter);
			}

		} else {
			ApplicationLauncher.logger
					.debug("BayUtils: computeMeterOverAllStatus: palletDistinctId NOT found:" + palletDistinctId);
		}

		ApplicationLauncher.logger.debug("BayUtils: computeMeterOverAllStatus: Exit");

	}

	public void archiveExistingResultInDb(String myBayKey, String palletDistinctId) {

		ApplicationLauncher.logger.debug("BayUtils: archiveExistingResultInDb: Entry");
		ApplicationLauncher.logger.debug("BayUtils: archiveExistingResultInDb: myBayKey: " + myBayKey
				+ ", eachPalletDistinctId: " + palletDistinctId);
		Optional<PalletManage> myPalletManageOpt = MySqlServiceManager.getPalletManageService()
				.findByPalletDistinctId(palletDistinctId);
		if (myPalletManageOpt.isPresent()) {
			// ApplicationLauncher.logger.debug("BayUtils: archiveExistingResultInDb: Hit");
			PalletManage myPalletManage = myPalletManageOpt.get();
			Set<PalletMeter> palletMeterSetList = myPalletManage.getPalletMeterList();
			List<PalletMeter> sortedPalletMeterList = palletMeterSetList.stream()
					.sorted(Comparator.comparingInt(PalletMeter::getRackPositionNo)).collect(Collectors.toList());
			for (PalletMeter eachPalletMeter : sortedPalletMeterList) {
				// ApplicationLauncher.logger.debug("BayUtils: archiveExistingResultInDb:
				// Hit2");
				// eachPalletMeter.getPalletMeterResultsList().stream().filter(e->e.getBayStateKey().equals(myBayKey))
				/*
				 * eachPalletMeter.getPalletMeterResultsList().removeIf(result -> {
				 * if (result.getBayStateKey().equals(myBayKey)) {
				 * result.setPalletMeter(null); // break reference to parent
				 * return true; // mark for removal
				 * }
				 * return false;
				 * });
				 */

				archiveResultsForBayKey(eachPalletMeter, myBayKey);
				eachPalletMeter.getPalletMeterResultsList().stream()
						.filter(e -> e.getBayStateKey().equals(myBayKey))
						.collect(Collectors.toList());
			}

		} else {
			ApplicationLauncher.logger
					.debug("BayUtils: archiveExistingResultInDb: palletDistinctId NOT found:" + palletDistinctId);

		}
		ApplicationLauncher.logger.debug("BayUtils: archiveExistingResultInDb: Exit");
	}

	public void archiveResultsForBayKey(PalletMeter palletMeter, String bayKey) {

		ApplicationLauncher.logger.debug("BayUtils: archiveResultsForBayKey: Entry");
		Set<PalletMeterResults> resultsToArchive = palletMeter.getPalletMeterResultsList().stream()
				.filter(r -> r.getBayStateKey().equals(bayKey))
				.collect(Collectors.toSet());

		for (PalletMeterResults result : resultsToArchive) {
			// Copy to archive object
			PalletMeterArchivedResults archive = new PalletMeterArchivedResults();
			archive.setBayStateKey(result.getBayStateKey());
			archive.setRackPositionNo(result.getRackPositionNo());
			archive.setTestType(result.getTestType());
			archive.setTestCaseName(result.getTestCaseName());
			archive.setResultStatus(result.getResultStatus());
			archive.setResultValue(result.getResultValue());
			archive.setPermissibleLowerLimit(result.getPermissibleLowerLimit());
			archive.setPermissibleUpperLimit(result.getPermissibleUpperLimit());
			archive.setMeterSerialNo(result.getMeterSerialNo());
			archive.setPalletBatchNo(result.getPalletBatchNo());
			archive.setPalletQrId(result.getPalletQrId());
			archive.setPalletDistinctId(result.getPalletDistinctId());
			archive.setSerialNo(result.getSerialNo());

			archive.setOriginalCreatedAt(result.getCreatedAt());
			archive.setOriginalUpdatedAt(result.getUpdatedAt());
			// Save to archive table
			MySqlServiceManager.getPalletMeterArchivedResultsService().save(archive);
		}

		// Remove from original list and break the link
		palletMeter.getPalletMeterResultsList().removeIf(r -> {
			boolean match = r.getBayStateKey().equals(bayKey);
			if (match)
				r.setPalletMeter(null);
			return match;
		});

		// Save updated PalletMeter
		MySqlServiceManager.getPalletMeterService().save(palletMeter);
		ApplicationLauncher.logger.debug("BayUtils: archiveResultsForBayKey: Exit");
	}

	public boolean updateConveyorMetrics(

			String bayKey,
			String palletDistinctId,
			String palletQrId,
			// String customerName,
			// List<Map<String, Object>> metersToUpdate
			ArrayList<PalletMeter> palletMeterList) {

		boolean allUpdatesInitiatedSuccessfully = true;
		try {
			if (isInMidnightBufferZone()) {

				int bufferTimeWaitInSec = 11;
				ApplicationLauncher.logger
						.debug("BayUtils: updateConveyorMetrics: isInMidnightBufferZone: Entry : bufferTimeWaitInSec: "
								+ bufferTimeWaitInSec);
				while ((bufferTimeWaitInSec > 0)
						&& (!BayUtils.isUserAborted())) {

					bufferTimeWaitInSec--;
					delay(1000);
					ApplicationLauncher.logger.debug(
							"BayUtils: updateConveyorMetrics: isInMidnightBufferZone: waiting : bufferTimeWaitInSec: "
									+ bufferTimeWaitInSec);

				}
				ApplicationLauncher.logger.debug("BayUtils: updateConveyorMetrics: isInMidnightBufferZone: Exit");

			}

		} catch (Exception e) {
			ApplicationLauncher.logger
					.error("BayUtils: Exception :updateConveyorMetrics: isInMidnightBufferZone:" + e.getMessage());

		}

		String customerName = ConveyorDataManager.getTerminalBayConfig().getCustomerName();// DeviceDataManagerController.getConveyorConfigParsedKey().getCustomerName();//."DevSys";
																							// // Fixed customerName

		ConveyorOutputMetrics metrics = new ConveyorOutputMetrics();

		// int currentPalletTotalMeters = metersToUpdate.size();
		int currentPalletTotalMeters = palletMeterList.size();
		int currentPalletPassedMeters = 0;
		int currentPalletFailedMeters = 0;
		Integer positionNo = 0;
		String serialNo = "";
		String status = "";
		String reason = "";

		if (bayKey.contains(ConstantConveyor.UNLOADING_BAY_KEY)) {
			// for (Map<String, Object> meterData : metersToUpdate) {
			for (PalletMeter eachpalletMeter : palletMeterList) {
				try {
					positionNo = eachpalletMeter.getRackPositionNo();// (Integer)meterData.get("rackPositionNo");
					serialNo = eachpalletMeter.getMeterSerialNo();// (String) meterData.get("meterSerialNo");
					status = eachpalletMeter.getOverAllTestResultStatus();// (String)
																			// meterData.get("overallTestResultStatus");
					reason = eachpalletMeter.getErrorCode();// (String) meterData.get("errorCode");
					ApplicationLauncher.logger.debug("updateConveyorMetrics: Unloading: positionNo: " + positionNo
							+ " status :<" + status + ">");

					if ("PASS".equalsIgnoreCase(status)) {
						currentPalletPassedMeters++;
						ApplicationLauncher.logger.debug("updateConveyorMetrics: Unloading: positionNo: " + positionNo
								+ " currentPalletPassedMeters hit1");
					} else if ("FAIL".equalsIgnoreCase(status)) {
						currentPalletFailedMeters++;
						ApplicationLauncher.logger.debug("updateConveyorMetrics: Unloading: positionNo: " + positionNo
								+ " currentPalletFailedMeters hit2");
					} else {
						currentPalletFailedMeters++;
						ApplicationLauncher.logger.debug("updateConveyorMetrics: Unloading: positionNo: " + positionNo
								+ " currentPalletFailedMeters hit3");
					}

					/*
					 * if (positionNo != null) {
					 * //sendunloadingMeterUpdate(positionNo, serialNo, status, reason);
					 * //BayUtils.delay(20000);
					 * //sendPalletWithMetersStatusUpdate(palletQrCode, metersToUpdate);
					 * //MeterStatus meterStatus = status.equals("Pass") ? MeterStatus.PASSED :
					 * MeterStatus.FAILED;
					 * MeterStatus meterStatus = status.equals(ConstantReport.REPORT_POPULATE_PASS)
					 * ? MeterStatus.PASSED : MeterStatus.FAILED;
					 * if(status.equals(ConstantReport.REPORT_POPULATE_PASS)) {
					 * meterStatus = MeterStatus.PASSED;
					 * ApplicationLauncher.logger.
					 * debug("updateConveyorMetrics: Unloading: positionNo: "+ positionNo
					 * +" Pass hit1");
					 * }else if(status.equals(ConstantReport.REPORT_POPULATE_FAIL)) {
					 * meterStatus = MeterStatus.FAILED;
					 * ApplicationLauncher.logger.
					 * debug("updateConveyorMetrics: Unloading: positionNo: "+ positionNo
					 * +" Fail hit2");
					 * }else if(status.equals(ConstantReport.REPORT_POPULATE_WFR)) {
					 * ApplicationLauncher.logger.
					 * debug("updateConveyorMetrics: Unloading: positionNo: "+ positionNo
					 * +" WFR hit3");
					 * meterStatus = MeterStatus.FAILED;
					 * }else {
					 * ApplicationLauncher.logger.
					 * debug("updateConveyorMetrics: Unloading: positionNo: "+ positionNo
					 * +" others hit4");
					 * meterStatus = MeterStatus.FAILED;
					 * }
					 * //ConveyorDeviceDataManagerController.getDashboardObject().
					 * updatePalletMeterStatusByBayAndPosition(
					 * // getMyBayKey(), positionNo, meterStatus, reason);
					 * ConveyorDataManager.getDashboardObject().
					 * updatePalletMeterStatusByBayAndPositionWithSerialNo(bayKey, positionNo,
					 * serialNo, meterStatus,
					 * reason.replace(ConstantConveyor.REASON_RESULT_FAILED_DISPLAY,
					 * "").replace("\n", ""));//.replace(ConstantConveyor.REASON_RESULT_WFR_DISPLAY,
					 * ""));
					 * } else {
					 * ApplicationLauncher.logger.
					 * debug("batchUpdateMeters: Unloading: Meter ID is null, skipping update for: "
					 * + serialNo);
					 * allUpdatesInitiatedSuccessfully = false;
					 * }
					 */
				} catch (Exception e) {
					ApplicationLauncher.logger
							.error("batchUpdateMeters: Unloading: Error initiating update for meter data: "
									+ eachpalletMeter.getMeterSerialNo() + ". Exception: " + e.getMessage(), e);
					allUpdatesInitiatedSuccessfully = false;
				}
			}

		} else if (bayKey.contains(ConstantConveyor.REJECTION_BAY_KEY)) {

			ApplicationLauncher.logger
					.debug("updateConveyorMetrics: palletMeterList.size(): " + palletMeterList.size());
			// for (Map<String, Object> meterData : metersToUpdate) {
			for (PalletMeter eachpalletMeter : palletMeterList) {
				try {
					positionNo = eachpalletMeter.getRackPositionNo();// (Integer) meterData.get("rackPositionNo");
					serialNo = eachpalletMeter.getMeterSerialNo();// (String) meterData.get("meterSerialNo");
					status = eachpalletMeter.getOverAllTestResultStatus();// (String)
																			// meterData.get("overallTestResultStatus");
					reason = eachpalletMeter.getErrorCode();// (String) meterData.get("errorCode");
					ApplicationLauncher.logger.debug("updateConveyorMetrics: Rejection: positionNo: " + positionNo
							+ " ,serialNo: " + serialNo + " , status: <" + status + "> , reason: " + reason);
					if (ConstantReport.REPORT_POPULATE_PASS.equalsIgnoreCase(status)) {
						currentPalletPassedMeters++;
						ApplicationLauncher.logger
								.debug("updateConveyorMetrics: Rejection: positionNo : " + positionNo + ": Pass hit1");
					} else if (ConstantReport.REPORT_POPULATE_FAIL.equalsIgnoreCase(status)) {
						currentPalletFailedMeters++;
						ApplicationLauncher.logger
								.debug("updateConveyorMetrics: Rejection: positionNo : " + positionNo + ": Fail hit2");
					} else if (ConstantReport.REPORT_POPULATE_WFR.equalsIgnoreCase(status)) {
						currentPalletPassedMeters++;
						ApplicationLauncher.logger
								.debug("updateConveyorMetrics: Rejection: positionNo : " + positionNo + ": WFR hit3");
					} else {
						currentPalletPassedMeters++;
						ApplicationLauncher.logger.debug(
								"updateConveyorMetrics: Rejection: positionNo : " + positionNo + ": Others hit4");
					}

					/*
					 * if (positionNo != null) {
					 * 
					 * MeterStatus meterStatus = status.equals(ConstantReport.REPORT_POPULATE_PASS)
					 * ? MeterStatus.PASSED : MeterStatus.FAILED;
					 * if(status.equals(ConstantReport.REPORT_POPULATE_PASS)) {
					 * meterStatus = MeterStatus.PASSED;
					 * ApplicationLauncher.logger.
					 * debug("updateConveyorMetrics: Rejection: positionNo: "+ positionNo
					 * +" Pass hit1");
					 * }else if(status.equals(ConstantReport.REPORT_POPULATE_FAIL)) {
					 * meterStatus = MeterStatus.FAILED;
					 * ApplicationLauncher.logger.
					 * debug("updateConveyorMetrics: Rejection: positionNo: "+ positionNo
					 * +" Fail hit2");
					 * }else if(status.equals(ConstantReport.REPORT_POPULATE_WFR)) {
					 * ApplicationLauncher.logger.
					 * debug("updateConveyorMetrics: Rejection: positionNo: "+ positionNo
					 * +" WFR hit3");
					 * meterStatus = MeterStatus.IDLE;
					 * }else {
					 * ApplicationLauncher.logger.
					 * debug("updateConveyorMetrics: Rejection: positionNo: "+ positionNo
					 * +" others hit4");
					 * meterStatus = MeterStatus.IDLE;
					 * }
					 * ConveyorDataManager.getDashboardObject().
					 * updatePalletMeterStatusByBayAndPositionWithSerialNo(
					 * bayKey, positionNo, serialNo, meterStatus, reason);
					 * } else {
					 * ApplicationLauncher.logger.
					 * error("updateConveyorMetrics: Rejection: Meter ID is null, skipping update for: "
					 * + serialNo);
					 * allUpdatesInitiatedSuccessfully = false;
					 * }
					 */
				} catch (Exception e) {
					ApplicationLauncher.logger
							.error("updateConveyorMetrics: Rejection: Error initiating update for meter data: "
									+ eachpalletMeter.getMeterSerialNo() + ". Exception: " + e.getMessage(), e);
					allUpdatesInitiatedSuccessfully = false;
				}
			}

		}

		// After processing all meters for the current pallet, update the daily
		// ConveyorOutputMetrics

		metrics.setTotalNoOfMeters(currentPalletTotalMeters);
		metrics.setPassedMeters(currentPalletPassedMeters);
		metrics.setFailedMeters(currentPalletFailedMeters);

		// updatedAt will be set by the service automatically before saving

		// Calculate Average Hourly Output
		// The createdAt field in 'metrics' will be the start of the day (00:00:00)
		// The current time 'now' represents the end of the active period for average
		// calculation

		// double calculatedAverage = metrics.getTotalNoOfMeters() / hoursElapsed;
		// ApplicationLauncher.logger.debug("batchUpdateMeters : calculatedAverage: " +
		// calculatedAverage);
		// metrics.setAverageHourlyOutput(calculatedAverage);
		metrics.setBayType(bayKey);
		metrics.setPalletDistinctId(palletDistinctId);
		metrics.setPalletQrId(palletQrId);
		metrics.setCustomerName(customerName);
		metrics.setUserName(DeviceDataManagerController.getUserName());
		metrics.setLocationName(ConveyorDataManager.getTerminalBayConfig().getLocationName());
		metrics.setPlantName(ConveyorDataManager.getTerminalBayConfig().getPlantName());
		metrics.setDepartmentName(ConveyorDataManager.getTerminalBayConfig().getDepartmentName());
		metrics.setLineNo(ConveyorDataManager.getTerminalBayConfig().getLineNo());

		try {
			boolean recordAlreadyExist = false;
			try {
				Optional<ConveyorOutputMetrics> conveyorOutputMetricsOpt = MySqlServiceManager
						.getConveyorOutputMetricsService().findByPalletDistinctId(palletDistinctId);
				if (conveyorOutputMetricsOpt.isPresent()) {
					recordAlreadyExist = true;
					if (recordAlreadyExist) {
						metrics.setId(conveyorOutputMetricsOpt.get().getId());
						metrics.setDateH(conveyorOutputMetricsOpt.get().getDateH());
						// metrics.setCreatedAt(conveyorOutputMetricsOpt.get().getCreatedAt());
						ApplicationLauncher.logger.info(
								"ConveyorOutputMetrics record already exist for palletDistinctId: " + palletDistinctId);
					}
				}

			} catch (Exception e) {
				ApplicationLauncher.logger
						.error("ConveyorOutputMetrics : Exception: recordAlreadyExist : " + e.getMessage());
				allUpdatesInitiatedSuccessfully = false;
			}

			MySqlServiceManager.getConveyorOutputMetricsService().saveToDb(metrics);
			// ApplicationLauncher.logger.info("ConveyorOutputMetrics (Daily) updated
			// successfully for customer: " + customerName + ", bay: " + bayKey + " for
			// today. Avg Hourly Output: " + String.format("%.2f", calculatedAverage));
			/*
			 * Platform.runLater(()->{
			 * ConveyorDataManager.getDashboardObject().refreshMetricsTable();
			 * });
			 */
		} catch (Exception e) {
			ApplicationLauncher.logger.error("Error saving ConveyorOutputMetrics (Daily): " + e.getMessage(), e);
			allUpdatesInitiatedSuccessfully = false;
		}

		// updateSummaryMetrics( metrics, bayType, currentPalletTotalMeters,
		// currentPalletPassedMeters, currentPalletFailedMeters);
		updateSummaryMetrics(bayKey, palletDistinctId);
		/*
		 * Platform.runLater(()->{
		 * ConveyorDataManager.getDashboardObject().refreshMetricsTable();
		 * });
		 */
		return allUpdatesInitiatedSuccessfully;
	}

	public void updateSummaryMetrics(

			ConveyorOutputMetrics metrics,
			// String customerName,
			String bayType,
			int currentPalletTotalMeters,
			int currentPalletPassedMeters,
			int currentPalletFailedMeters) {

		ApplicationLauncher.logger.debug("BayUtils: updateSummaryMetrics : Entry: bayType" + bayType);
		// String customerName =
		// DeviceDataManagerController.getConveyorConfigParsedKey().getCustomerName();//."DevSys";
		// // Fixed customerName
		String customerName = ConveyorDataManager.getTerminalBayConfig().getCustomerName();
		// SUMMARY - UPDATE
		try {
			Date metricsCreatedAt = metrics.getCreatedAt();

			Optional<ConveyorOutputMetricsSummary> summaryOptional = MySqlServiceManager
					.getConveyorOutputMetricsSummaryService()
					.findByCustomerNameBayTypeAndCurrentDate(customerName, bayType);

			ConveyorOutputMetricsSummary summary = summaryOptional.orElseGet(() -> {
				ConveyorOutputMetricsSummary s = new ConveyorOutputMetricsSummary();
				s.setCustomerName(customerName);
				s.setBayType(bayType);
				s.setCreatedAt(metricsCreatedAt);
				s.setPalletOutput(0);
				s.setTotalNoOfMeters(0);
				s.setPassedMeters(0);
				s.setFailedMeters(0);
				s.setAverageHourlyOutput(0.0);
				return s;
			});
			Date now = new Date();
			long diffInMillis = 0;
			try {
				diffInMillis = now.getTime() - summary.getCreatedAt().getTime(); // Time elapsed since start of day
				ApplicationLauncher.logger.debug(
						"BayUtils: updateSummaryMetrics : diffInMillis: " + diffInMillis + " : bayType" + bayType);
			} catch (Exception e) {
				ApplicationLauncher.logger.error("BayUtils: updateSummaryMetrics : Exception: diffInMillis: "
						+ e.getMessage() + " : bayType" + bayType);
				e.printStackTrace();
				if (e instanceof InterruptedException) {
					Thread.currentThread().interrupt(); // Restore interrupt status
				}
			}

			double hoursElapsed = (double) diffInMillis / (1000 * 60 * 60); // Convert milliseconds to hours
			ApplicationLauncher.logger.debug("BayUtils: updateSummaryMetrics : summary : hoursElapsed: " + hoursElapsed
					+ " : bayType" + bayType);
			// To avoid division by zero or inflated "per hour" numbers for very short
			// durations
			// If the elapsed time is less than an hour, we'll consider it 1 hour for
			// averaging purposes.
			// This gives a more realistic "rate" from the beginning of operation.
			if (hoursElapsed < 1.0) {
				hoursElapsed = 1.0;
			}

			double calculatedSummaryAverage = (summary.getTotalNoOfMeters() + currentPalletTotalMeters) / hoursElapsed;
			ApplicationLauncher.logger.debug("BayUtils: updateSummaryMetrics : summary : hoursElapsed: " + hoursElapsed
					+ " : bayType" + bayType);
			ApplicationLauncher.logger.debug("BayUtils: updateSummaryMetrics : summary : TotalNoOfMeters: "
					+ summary.getTotalNoOfMeters() + " : bayType" + bayType);
			ApplicationLauncher.logger.debug("BayUtils: updateSummaryMetrics : summary : calculatedSummaryAverage: "
					+ calculatedSummaryAverage + " : bayType" + bayType);

			summaryOptional.ifPresent(existing -> summary.setId(existing.getId())); // Keep ID if exists
			summary.setUpdatedAt(now);
			summary.setPalletOutput(summary.getPalletOutput() + 1);
			summary.setTotalNoOfMeters(summary.getTotalNoOfMeters() + currentPalletTotalMeters);
			summary.setPassedMeters(summary.getPassedMeters() + currentPalletPassedMeters);
			summary.setFailedMeters(summary.getFailedMeters() + currentPalletFailedMeters);
			summary.setAverageHourlyOutput(calculatedSummaryAverage);

			summary.setCustomerName(customerName);
			summary.setLocationName(ConveyorDataManager.getTerminalBayConfig().getLocationName());
			summary.setPlantName(ConveyorDataManager.getTerminalBayConfig().getPlantName());
			summary.setDepartmentName(ConveyorDataManager.getTerminalBayConfig().getDepartmentName());
			summary.setLineNo(ConveyorDataManager.getTerminalBayConfig().getLineNo());

			MySqlServiceManager.getConveyorOutputMetricsSummaryService().saveToDb(summary);

			ApplicationLauncher.logger.info("BayUtils: ConveyorOutputMetricsSummary upserted for customer: "
					+ customerName + ", bay: " + bayType);
		} catch (Exception e) {
			ApplicationLauncher.logger.error("BayUtils: Exception: Error saving ConveyorOutputMetricsSummary: "
					+ e.getMessage() + " : bayType" + bayType);
		}
	}

	public static List<String> getUnloadingBayPalletDistinctIdList() {
		return unloadingBayPalletDistinctIdList;
	}

	public static List<String> getRejectionBayPalletDistinctIdList() {
		return rejectionBayPalletDistinctIdList;
	}

	public static void setUnloadingBayPalletDistinctIdList(List<String> unloadingBayPalletDistinctIdList) {
		BayUtils.unloadingBayPalletDistinctIdList = unloadingBayPalletDistinctIdList;
	}

	public static void setRejectionBayPalletDistinctIdList(List<String> rejectionBayPalletDistinctIdList) {
		BayUtils.rejectionBayPalletDistinctIdList = rejectionBayPalletDistinctIdList;
	}

	public static Map<String, IoPortInfo> getInputIoPortInfoMap() {
		return inputIoPortInfoMap;
	}

	public static void setInputIoPortInfoMap(Map<String, IoPortInfo> inputIoPortInfoMap) {
		BayUtils.inputIoPortInfoMap = inputIoPortInfoMap;
	}

	public static Map<String, IoPortInfo> getOutputIoPortInfoMap() {
		return outputIoPortInfoMap;
	}

	public static void setOutputIoPortInfoMap(Map<String, IoPortInfo> outputIoPortInfoMap) {
		BayUtils.outputIoPortInfoMap = outputIoPortInfoMap;
	}

	public void updateSummaryMetrics(String bayKey, String palletDistinctId) {
		// Find the specific metric that triggered this update
		ConveyorOutputMetrics triggerMetric = MySqlServiceManager.getConveyorOutputMetricsService()
				.findByPalletDistinctId(palletDistinctId)
				.orElseThrow(() -> new RuntimeException("Metric not found with palletDistinctId: " + palletDistinctId));

		// Validate the bayKey matches the metric's bayType

		ApplicationLauncher.logger.info("updateSummaryMetrics: bayKey: " + bayKey);
		ApplicationLauncher.logger
				.info("updateSummaryMetrics: triggerMetric.getBayType(): " + triggerMetric.getBayType());
		if (!bayKey.equals(triggerMetric.getBayType())) {
			throw new IllegalArgumentException("bayKey does not match the bayType in the metric record");
		}

		// Get date boundaries for aggregation
		Date metricTargetDate = triggerMetric.getDateH();// getDateWithoutTime(triggerMetric.getCreatedAt());
		// Date startOfDay = getStartOfDay(metricDate);
		// Date endOfDay = getEndOfDay(metricDate);

		// Find all metrics for this bayKey on the same day
		List<ConveyorOutputMetrics> dailyMetrics = MySqlServiceManager.getConveyorOutputMetricsService()
				.findAllByBayTypeAndDateH(
						bayKey,
						metricTargetDate);

		// Calculate aggregated values with percentages
		AggregatedMetrics aggregated = calculateAggregatedMetrics(dailyMetrics);

		// Find existing summary or create new one
		ConveyorOutputMetricsSummary summary = MySqlServiceManager.getConveyorOutputMetricsSummaryService()
				.findByBayTypeAndDateH(
						bayKey,
						metricTargetDate)
				.orElseGet(() -> createNewSummary(triggerMetric));

		// Update all summary fields including percentages
		updateSummaryFields(summary, aggregated);

		MySqlServiceManager.getConveyorOutputMetricsSummaryService().saveToDb(summary);
	}

	private AggregatedMetrics calculateAggregatedMetrics(List<ConveyorOutputMetrics> metrics) {
		AggregatedMetrics result = new AggregatedMetrics();

		// Calculate basic metrics
		result.totalMeters = metrics.stream().mapToInt(ConveyorOutputMetrics::getTotalNoOfMeters).sum();
		result.passedMeters = metrics.stream().mapToInt(ConveyorOutputMetrics::getPassedMeters).sum();
		result.failedMeters = metrics.stream().mapToInt(ConveyorOutputMetrics::getFailedMeters).sum();
		result.palletOutput = metrics.stream().mapToInt(ConveyorOutputMetrics::getPalletOutput).sum();

		// Calculate operational hours
		double operationalHours = calculateOperationalHours(metrics);
		ApplicationLauncher.logger.info("calculateAggregatedMetrics: operationalHours: " + operationalHours);
		ApplicationLauncher.logger.info("calculateAggregatedMetrics: totalMeters: " + result.totalMeters);

		// Calculate average hourly output with 3 decimal precision
		result.averageHourlyOutput = operationalHours > 0
				? roundToTwoDecimals((double) result.totalMeters / operationalHours)
				: 0.0;

		// Calculate pass/fail percentages with 2 decimal precision
		if (result.totalMeters > 0) {
			result.passedPercentage = roundToTwoDecimals((result.passedMeters * 100.0) / result.totalMeters);
			result.failedPercentage = roundToTwoDecimals((result.failedMeters * 100.0) / result.totalMeters);
		} else {
			result.passedPercentage = 0.0;
			result.failedPercentage = 0.0;
		}
		ApplicationLauncher.logger.info("calculateAggregatedMetrics: passedPercentage: " + result.passedPercentage);
		ApplicationLauncher.logger.info("calculateAggregatedMetrics: failedPercentage: " + result.failedPercentage);
		return result;
	}

	private double roundToTwoDecimals(double value) {
		return BigDecimal.valueOf(value)
				.setScale(2, RoundingMode.HALF_UP)
				.doubleValue();
	}

	private ConveyorOutputMetricsSummary createNewSummary(ConveyorOutputMetrics metric) {
		ConveyorOutputMetricsSummary newSummary = new ConveyorOutputMetricsSummary();
		newSummary.setCustomerName(metric.getCustomerName());
		newSummary.setBayType(metric.getBayType());
		newSummary.setLocationName(metric.getLocationName());
		newSummary.setDepartmentName(metric.getDepartmentName());
		newSummary.setPlantName(metric.getPlantName());
		newSummary.setLineNo(metric.getLineNo());

		// Set both timestamp and date fields
		// newSummary.setCreatedAt(new Date()); // Current timestamp
		newSummary.setDateH(metric.getDateH()); // Date only

		return newSummary;
	}

	private void updateSummaryFields(ConveyorOutputMetricsSummary summary, AggregatedMetrics aggregated) {
		summary.setTotalNoOfMeters(aggregated.totalMeters);
		summary.setPassedMeters(aggregated.passedMeters);
		summary.setFailedMeters(aggregated.failedMeters);
		summary.setPalletOutput(aggregated.palletOutput);
		summary.setAverageHourlyOutput(aggregated.averageHourlyOutput);
		summary.setPassedPercentage(aggregated.passedPercentage);
		summary.setFailedPercentage(aggregated.failedPercentage);
		// summary.setUpdatedAt(new Date());
	}

	// Date utility methods (unchanged from previous implementation)
	/*
	 * private Date getDateWithoutTime(Date date) {
	 * Calendar calendar = Calendar.getInstance();
	 * calendar.setTime(date);
	 * calendar.set(Calendar.HOUR_OF_DAY, 0);
	 * calendar.set(Calendar.MINUTE, 0);
	 * calendar.set(Calendar.SECOND, 0);
	 * calendar.set(Calendar.MILLISECOND, 0);
	 * return calendar.getTime();
	 * }
	 */


	/*
	 * private long calculateOperationalHours(List<ConveyorOutputMetrics> metrics) {
	 * if (metrics.isEmpty()) return 0;
	 * if (metrics.size() == 1) return 1;
	 * 
	 * Date first = metrics.get(0).getCreatedAt();
	 * Date last = metrics.get(metrics.size() - 1).getCreatedAt();
	 * long diffHours = (last.getTime() - first.getTime()) / (60 * 60 * 1000);
	 * return Math.max(diffHours, 1);
	 * }
	 */

	private double calculateOperationalHours(List<ConveyorOutputMetrics> metrics) {
		if (metrics.isEmpty())
			return 0.0;
		if (metrics.size() == 1)
			return 1.0;

		Date first = metrics.get(0).getCreatedAt();
		Date last = metrics.get(metrics.size() - 1).getCreatedAt();

		// Convert milliseconds to hours with decimal precision
		double diffHours = (last.getTime() - first.getTime()) / (60.0 * 60.0 * 1000.0);

		// Return at least 1.0 hour but with possible decimal values
		return Math.max(diffHours, 1.0);
	}

	private static class AggregatedMetrics {
		int totalMeters;
		int passedMeters;
		int failedMeters;
		int palletOutput;
		double averageHourlyOutput;
		double passedPercentage;
		double failedPercentage;
	}

	private boolean isInMidnightBufferZone() {
		LocalTime now = LocalTime.now();
		return now.isAfter(LocalTime.of(23, 59, 55)) || now.isBefore(LocalTime.of(0, 0, 5));
	}

	public void markAsCompleteForPreviousBayPallet(String bayKey, Logger eachLogger) {

		List<PalletManage> palletManageList = MySqlServiceManager.getPalletManageService()
				.findByBayKeyAndPalletActive(bayKey);
		if (palletManageList.size() > 1) {
			palletManageList.stream().map(e -> e.getPalletDistinctId())
					.collect(Collectors.toList());
			Optional<PalletManage> palletManageOpt = MySqlServiceManager.getPalletManageService()
					.findTopByBayKeyAndPalletActive(bayKey);
			if (palletManageOpt.isPresent()) {
				String topPalletDistinctId = palletManageOpt.get().getPalletDistinctId();
				for (PalletManage eachPalletManage : palletManageList) {
					if (!eachPalletManage.getPalletDistinctId().equals(topPalletDistinctId)) {
						eachPalletManage.setPalletActive(false);
						eachLogger.debug("markAsCompleteForPreviousHvBayPallet - getPalletDistinctId: "
								+ eachPalletManage.getPalletDistinctId() + " , bayKey: " + bayKey);
						MySqlServiceManager.getPalletManageService().saveToDb(eachPalletManage);
					}
				}
			}

		}

	}

}
