package com.tasnetwork.calibration.conveyor.plc;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import com.tasnetwork.calibration.conveyor.BayTestController;
import com.tasnetwork.calibration.conveyor.ClusterServer;
import com.tasnetwork.calibration.conveyor.InputPortActiveCheckBoxValueFactory;
import com.tasnetwork.calibration.conveyor.InputPortTableViewRefresher;
import com.tasnetwork.calibration.conveyor.OutputPortActiveCheckBoxValueFactory;
import com.tasnetwork.calibration.conveyor.OutputPortTableViewRefresher;
import com.tasnetwork.calibration.conveyor.OutputPortUpdateBayCheckBoxValueFactory;
import com.tasnetwork.calibration.conveyor.RestApiClusterResponse;
import com.tasnetwork.calibration.conveyor.RestApiJsonBodyResponse;
import com.tasnetwork.calibration.conveyor.AsyncHttpClient.ConveyorClientManager;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.configloader.Bay;
import com.tasnetwork.calibration.conveyor.bay.configloader.ClusterDetail;
import com.tasnetwork.calibration.conveyor.bay.configloader.InputPort;
import com.tasnetwork.calibration.conveyor.bay.configloader.OutputPort;
import com.tasnetwork.calibration.conveyor.bay.configloader.Terminal;
import com.tasnetwork.calibration.conveyor.bay.configloader.TerminalBayConfigModel;
import com.tasnetwork.calibration.conveyor.bay.ft.FunctionalTestBaySingleStateTestRun;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.WindowManager;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class PlcServerBayTestController
		implements Initializable, InputPortTableViewRefresher, OutputPortTableViewRefresher {

	Timer startServerTimer;
	Timer stopServerTimer;
	Timer sendCommDataTaskTimer;
	Timer loadOnClickTimer;
	Timer baySelectionOnChangeTimer;
	Timer clusterSelectionOnChangeTimer;
	Timer funtionalBaySingleStateTaskTimer;

	ModbusTcpServer modbusTcpServer;// = new ModbusTcpServer();

	// private ModbusServer modbusServer;

	private static TerminalBayConfigModel bayConfigModel = ConveyorDataManager.getTerminalBayConfig();

	// ObservableList<InputPort> inputPortCoilDataList =
	// FXCollections.observableArrayList();

	private ObservableList<InputPort> inputPortList = FXCollections.observableArrayList();

	public static String presentClusterId = "";

	private Map<String, ArrayList<String>> clusterBayNameListMap = new HashMap<String, ArrayList<String>>();
	private Map<String, String> clusterNameIdListMap = new HashMap<String, String>();
	private Map<String, String> clusterBayNameIdMap = new HashMap<String, String>();

	/*
	 * @FXML CheckBox chkBxReadEnabledSample;
	 * public static CheckBox ref_chkBxReadEnabledSample;
	 */

	@FXML
	TextField txtReadAddressSample;
	public static TextField ref_txtReadAddressSample;

	@FXML
	TextField txtReadValueSample;
	public static TextField ref_txtReadValueSample;

	@FXML
	TextField txtReadStatusSample;
	public static TextField ref_txtReadStatusSample;

	/*
	 * @FXML CheckBox chkBxWriteEnabledSample;
	 * public static CheckBox ref_chkBxWriteEnabledSample;
	 */

	@FXML
	Button btnWriteHoldingRegister;
	public static Button ref_btnWriteHoldingRegister;

	@FXML
	TextField txtWriteAddressSample;
	public static TextField ref_txtWriteAddressSample;

	@FXML
	TextField txtWriteValueSample;
	public static TextField ref_txtWriteValueSample;

	@FXML
	TextField txtWriteStatusSample;
	public static TextField ref_txtWriteStatusSample;

	@FXML
	private Button btnLoad;

	@FXML
	private Button btnSendDataToBay;
	public static Button ref_btnSendDataToBay;

	@FXML
	private Button btnStartServer;
	public static Button ref_btnStartServer;

	@FXML
	private Button btnStopServer;
	public static Button ref_btnStopServer;

	@FXML
	TextField txtComStatus;
	public static TextField ref_txtComStatus;

	@FXML
	private TableView<OutputPort> tbViewOutputPortData;
	public static TableView<OutputPort> ref_tbViewOutputPortData;

	@FXML
	private TableColumn<OutputPort, String> tblColOutputSerialNo;
	public static TableColumn<OutputPort, String> ref_tblColOutputSerialNo;

	@FXML
	private TableColumn<OutputPort, String> tblColOutputPortStateDescription;
	public static TableColumn<OutputPort, String> ref_tblColOutputPortStateDescription;

	@FXML
	private TableColumn<OutputPort, String> tblColOutputPortName;
	public static TableColumn<OutputPort, String> ref_tblColOutputPortName;

	@FXML
	private TableColumn tblColOutputActive;
	public static TableColumn ref_tblColOutputActive;

	@FXML
	private TableColumn tblColOutputUpdateBay;
	public static TableColumn ref_tblColOutputUpdateBay;

	@FXML
	private TableView<InputPort> tbViewInputPortData;
	public static TableView<InputPort> ref_tbViewInputPortData;

	@FXML
	private TableColumn<InputPort, String> tblColInputSerialNo;
	public static TableColumn<InputPort, String> ref_tblColInputSerialNo;

	@FXML
	private TableColumn<InputPort, String> tblColInputPortStateDescription;
	public static TableColumn<InputPort, String> ref_tblColInputPortStateDescription;

	@FXML
	private TableColumn<InputPort, String> tblColInputPortName;
	public static TableColumn<InputPort, String> ref_tblColInputPortName;

	@FXML
	private TableColumn tblColInputActive;
	public static TableColumn ref_tblColInputActive;

	/*
	 * @FXML
	 * private TableColumn tblColInputReadBay;
	 * public static TableColumn ref_tblColInputReadBay;
	 */

	@FXML
	private ComboBox cmbBxBaySelection;
	public static ComboBox ref_cmbBxBaySelection;

	@FXML
	private ComboBox cmbBxClusterSelection;
	public static ComboBox ref_cmbBxClusterSelection;

	@FXML
	private TextField txtClusterIpAddress;
	public static TextField ref_txtClusterIpAddress;

	@FXML
	private TextField txtClusterPortNo;
	public static TextField ref_txtClusterPortNo;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		refInit();
		guiInit();
		dataSetupInit();
	}

	@Override
	public void refreshInputPortTable() {
		ref_tbViewInputPortData.refresh();
	}

	@Override
	public void refreshOutputPortTable() {
		ref_tbViewOutputPortData.refresh();
	}

	public void dataSetupInit() {

		// TerminalBayConfigModel bayConfigModel =
		// ConveyorDeviceDataManagerController.getBayConfigParsedKey();
		// loadDataFromConfig();

		ref_tbViewOutputPortData.getItems().clear();
		ref_tbViewInputPortData.getItems().clear();
		// ref_txtClusterIpAddress.setText("");
		// ref_txtClusterPortNo.setText("");
	}

	public void guiInit() {

		ref_tblColOutputSerialNo.setCellValueFactory(new PropertyValueFactory<OutputPort, String>("serialNo"));
		ref_tblColOutputPortName.setCellValueFactory(new PropertyValueFactory<OutputPort, String>("portName"));
		ref_tblColOutputActive
				.setCellValueFactory(new OutputPortActiveCheckBoxValueFactory(PlcServerBayTestController.this));
		ref_tblColOutputActive.setStyle("-fx-alignment: CENTER;");
		ref_tblColOutputUpdateBay.setCellValueFactory(new OutputPortUpdateBayCheckBoxValueFactory());
		ref_tblColOutputUpdateBay.setStyle("-fx-alignment: CENTER;");
		ref_tblColOutputPortStateDescription
				.setCellValueFactory(new PropertyValueFactory<OutputPort, String>("stateDescription"));

		ref_tblColInputSerialNo.setCellValueFactory(new PropertyValueFactory<InputPort, String>("serialNo"));
		ref_tblColInputPortName.setCellValueFactory(new PropertyValueFactory<InputPort, String>("portName"));
		ref_tblColInputActive.setEditable(false);
		ref_tblColInputActive
				.setCellValueFactory(new InputPortActiveCheckBoxValueFactory(PlcServerBayTestController.this));
		ref_tblColInputActive.setStyle("-fx-alignment: CENTER;");
		/*
		 * ref_tblColInputReadBay.setCellValueFactory(new
		 * InputPortReadBayCheckBoxValueFactory());
		 * ref_tblColInputReadBay.setStyle( "-fx-alignment: CENTER;");
		 */ ref_tblColInputPortStateDescription
				.setCellValueFactory(new PropertyValueFactory<InputPort, String>("stateDescription"));

		ref_btnWriteHoldingRegister.setOnAction(event -> {
			try {
				int address = Integer.parseInt(txtWriteAddressSample.getText().trim());
				int value = Integer.parseInt(txtWriteValueSample.getText().trim());
				getModbusTcpServer().writeHoldingRegister(address, value);
			} catch (NumberFormatException e) {
				ApplicationLauncher.logger.debug("â�Œ Invalid Input in Write Fields.");
			}
		});

	}

	private void refInit() {

		ref_cmbBxBaySelection = cmbBxBaySelection;
		ref_cmbBxClusterSelection = cmbBxClusterSelection;

		/*
		 * ref_cmbBxPosLoadedBaySelection = cmbBxPosLoadedBaySelection;
		 * ref_cmbBxPosLoadedClusterSelection = cmbBxPosLoadedClusterSelection;
		 * ref_cmbBxPosLoadedTerminalSelection = cmbBxPosLoadedTerminalSelection;
		 * 
		 * ref_cmbBxFilterPosition = cmbBxFilterPosition;
		 */

		ref_txtClusterIpAddress = txtClusterIpAddress;

		ref_txtClusterPortNo = txtClusterPortNo;

		ref_tbViewOutputPortData = tbViewOutputPortData;
		ref_tblColOutputSerialNo = tblColOutputSerialNo;
		ref_tblColOutputPortName = tblColOutputPortName;
		ref_tblColOutputActive = tblColOutputActive;
		ref_tblColOutputUpdateBay = tblColOutputUpdateBay;
		ref_tblColOutputPortStateDescription = tblColOutputPortStateDescription;

		ref_tbViewInputPortData = tbViewInputPortData;
		ref_tblColInputSerialNo = tblColInputSerialNo;
		ref_tblColInputPortName = tblColInputPortName;
		ref_tblColInputActive = tblColInputActive;
		// ref_tblColInputReadBay = tblColInputReadBay;
		ref_tblColInputPortStateDescription = tblColInputPortStateDescription;

		ref_btnSendDataToBay = btnSendDataToBay;
		ref_btnStartServer = btnStartServer;
		ref_txtComStatus = txtComStatus;

		// ref_chkBxReadEnabledSample = chkBxReadEnabledSample;
		ref_txtReadAddressSample = txtReadAddressSample;
		ref_txtReadValueSample = txtReadValueSample;
		ref_txtReadStatusSample = txtReadStatusSample;

		// ref_chkBxWriteEnabledSample = chkBxWriteEnabledSample;
		ref_btnWriteHoldingRegister = btnWriteHoldingRegister;
		ref_txtWriteAddressSample = txtWriteAddressSample;
		ref_txtWriteValueSample = txtWriteValueSample;
		ref_txtWriteStatusSample = txtWriteStatusSample;

	}

	public void loadDataFromConfig() {
		// B A Y T E S T ====================================================
		ApplicationLauncher.logger.debug("loadDataFromConfig: Entry");
		ref_cmbBxClusterSelection.getItems().clear();
		for (Terminal eachTerminal : getBayConfigModel().getTerminal()) {
			if (eachTerminal.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID)) {
				for (ClusterDetail eachClusterDetail : eachTerminal.getClusterDetails()) {
					ref_cmbBxClusterSelection.getItems().add(eachClusterDetail.getName());
					getClusterNameIdListMap().put(eachClusterDetail.getName(), eachClusterDetail.getClusterId());
					ArrayList<String> bayList = new ArrayList<String>();
					getClusterBayNameListMap().put(eachClusterDetail.getName(), bayList);

					for (Bay eachBay : eachClusterDetail.getBay()) {
						// ref_cmbBxBaySelection.getItems().add(eachBay.getBayName());
						bayList.add(eachBay.getBayName());
						getClusterBayNameListMap().put(eachClusterDetail.getName(), bayList);
						Map<String, String> bayNameIdMap = new HashMap<String, String>();
						bayNameIdMap.put(eachBay.getBayName(), eachBay.getBayId());
						// getClusterBayNameIdMap().put(eachClusterDetail.getName(), bayNameIdMap);
						getClusterBayNameIdMap().put(eachClusterDetail.getName() + "_" + eachBay.getBayName(),
								eachBay.getBayId());
						// ApplicationLauncher.logger.debug("loadDataFromConfig :
						// getClusterBayNameIdMap().get(clusterName)-1 :"+ getClusterBayNameIdMap());

					}
				}
			}
		}

		if (ref_cmbBxClusterSelection.getItems().size() > 0) {
			ref_cmbBxClusterSelection.getSelectionModel().select(0);

		}

		if (getClusterBayNameListMap().size() > 0) {
			if (getClusterBayNameListMap()
					.containsKey(ref_cmbBxClusterSelection.getSelectionModel().getSelectedItem().toString())) {
				ref_cmbBxBaySelection.getItems().addAll(getClusterBayNameListMap()
						.get(ref_cmbBxClusterSelection.getSelectionModel().getSelectedItem().toString()));
			}
			// ref_cmbBxBaySelection.getSelectionModel().select(0);

		}

		if (ref_cmbBxBaySelection.getItems().size() > 0) {
			ref_cmbBxBaySelection.getSelectionModel().select(0);
			String clusterName = (ref_cmbBxClusterSelection.getSelectionModel().getSelectedItem().toString());
			String bayName = (ref_cmbBxBaySelection.getSelectionModel().getSelectedItem().toString());

			String clusterId = getClusterNameIdListMap().get(clusterName);
			// ApplicationLauncher.logger.debug("loadDataFromConfig :
			// getClusterBayNameIdMap().get(clusterName)-2 :"+
			// getClusterBayNameIdMap().get(clusterName));
			// String bayId = "11";//getClusterBayNameIdMap().get(clusterName).get(bayName);

			ApplicationLauncher.logger.debug("loadDataFromConfig : clusterId :" + clusterId);
			Optional<ClusterDetail> clusterOpt = getBayConfigModel().getTerminal().stream()
					.filter(e1 -> e1.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID))
					.flatMap(terminal -> terminal.getClusterDetails().stream())
					.filter(e2 -> e2.getClusterId().equals(clusterId))
					.findFirst();

			if (clusterOpt.isPresent()) {
				ClusterDetail clusterDetail = clusterOpt.get();
				ref_txtClusterIpAddress.setText(clusterDetail.getClusterIpAddress());
				ref_txtClusterPortNo.setText(clusterDetail.getClusterPortId());
				setPresentClusterId(clusterId);

			}
			// Optional<OutputPort> outputPortOpt =

			Optional<Bay> bayOpt = getBayConfigModel().getTerminal().stream()
					.filter(e1 -> e1.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID))
					.flatMap(terminal -> terminal.getClusterDetails().stream())
					.filter(e2 -> e2.getClusterId().equals(clusterId))
					.flatMap(e3 -> e3.getBay().stream())
					.filter(e4 -> e4.getBayName().equals(bayName))
					.findFirst();

			if (bayOpt.isPresent()) {
				Bay bayDetails = bayOpt.get();
				String bayId = bayDetails.getBayId();
				ApplicationLauncher.logger.debug("loadDataFromConfig : bayId :" + bayId);

				ArrayList<OutputPort> outputPortList = (ArrayList<OutputPort>) getBayConfigModel().getTerminal()
						.stream()
						.flatMap(terminal -> terminal.getOutputPort().stream())
						.filter(p -> clusterId.equals(p.getClusterId()))
						.filter(p -> bayId.equals(p.getBayId()))
						.collect(Collectors.toList());

				if (outputPortList.size() > 0) {
					int serialNo = 1;
					for (OutputPort eachOutputPort : outputPortList) {
						eachOutputPort.setSerialNo(String.valueOf(serialNo));
						serialNo++;
					}
					ref_tbViewOutputPortData.getItems().addAll(outputPortList);

				}

				ArrayList<InputPort> inputPortList = (ArrayList<InputPort>) getBayConfigModel().getTerminal().stream()
						.flatMap(terminal -> terminal.getInputPort().stream())
						.filter(p -> clusterId.equals(p.getClusterId()))
						.filter(p -> bayId.equals(p.getBayId()))
						.collect(Collectors.toList());

				if (inputPortList.size() > 0) {
					int serialNo = 1;
					for (InputPort eachInputPort : inputPortList) {
						eachInputPort.setSerialNo(String.valueOf(serialNo));
						serialNo++;
					}
					ref_tbViewInputPortData.getItems().addAll(inputPortList);
				}
			}
		}

	}

	/*
	 * @FXML
	 * void btnLoadOnClick(ActionEvent event) {
	 * 
	 * }
	 * 
	 * @FXML
	 * void btnSendDataToBayOnClick(ActionEvent event) {
	 * 
	 * }
	 * 
	 * @FXML
	 * void cmbBxBaySelectionOnChange(ActionEvent event) {
	 * 
	 * }
	 * 
	 * @FXML
	 * void cmbBxClusterSelectionOnChange(ActionEvent event) {
	 * 
	 * }
	 */

	@FXML
	public void btnSendDataToBayOnClick() {

		ApplicationLauncher.logger.debug("btnSendDataToBayOnClick: Entry");
		sendCommDataTaskTimer = new Timer();
		sendCommDataTaskTimer.schedule(new SendDataToBayTask(), 10);

	}

	@FXML
	public void btnConnectOnClick() {
		ApplicationLauncher.logger.debug("btnConnectOnClick: Entry");
		for (int i = 0; i < 2000; i++) {
			ApplicationLauncher.logger.debug("btnConnectOnClick: Count : " + i);
			funtionalBaySingleStateTaskTimer = new Timer();
			funtionalBaySingleStateTaskTimer.schedule(new FunctionalTestBaySingleStateTestRun(), 100);

			Sleep(5000);
		}
	}

	class SendDataToBayTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				ref_btnSendDataToBay.setDisable(true);
				ref_txtComStatus.setText("Sending...");
				ref_txtReadStatusSample.setText("");
				ref_txtWriteStatusSample.setText("");
			});

			ConveyorClientManager.getInstance();

			/*
			 * if(ref_chkBxWriteGreenLed.isSelected()) {
			 * String greenLedStatus = "Off";
			 * if(ref_chkBxGreenLedData.isSelected()) {
			 * greenLedStatus = "On";
			 * }
			 */

			boolean overAllStatus = true;
			boolean deviceResponded = false;
			if (ref_tbViewOutputPortData.getItems().size() > 0) {
				String ipAddress = ref_txtClusterIpAddress.getText();
				String ipPort = ref_txtClusterPortNo.getText();
				new ClusterServer(ipAddress, ipPort, getPresentClusterId());

				for (int i = 0; i < ref_tbViewOutputPortData.getItems().size(); i++) {
					OutputPort outputPortDetails = ref_tbViewOutputPortData.getItems().get(i);
					if (outputPortDetails.isUpdateBay()) {
						String outputPortId = outputPortDetails.getPortId();
						if (outputPortDetails.isOutputActive()) {
						}
						// outputPortId = outputPortId.replace("ipt", "").replace("ip",
						// "").replace("op", "");
						outputPortId = outputPortId.replaceAll("[^0-9.]", "");
						ApplicationLauncher.logger.info("SendDataToBayTask: outputPortId: " + outputPortId);
						// deviceResponded = setOutputDataToBay(clusterServer,
						// deviceId,bayId,outputPortId, outputActive) ;
						if (GuiUtils.isNumber(outputPortId)) {
							Integer.parseInt(outputPortId);

							// deviceResponded =
							// ModbusTcpClient.modbusTcpSendWriteCoilCmd(plcCoilAddress,outputPortDetails.isOutputActive());
							if (!deviceResponded) {
								ApplicationLauncher.logger
										.info("SendDataToBayTask: outputPortId: device not responded ");
								overAllStatus = false;
							}
						} else {
							ApplicationLauncher.logger
									.info("SendDataToBayTask: invalid outputPortId: " + outputPortDetails.getPortId());
							overAllStatus = false;

							WindowManager.InformUser("Error-1010", "Kindly check key : " + outputPortDetails.getPortId()
									+ " on conveyor config file on <outputPort> section", AlertType.ERROR);
						}
					}
				}
			}

			BayResponse bayResponse = new BayResponse();
			if (ref_tbViewInputPortData.getItems().size() > 0) {
				String ipAddress = ref_txtClusterIpAddress.getText();
				String ipPort = ref_txtClusterPortNo.getText();
				String stateDesc = "";
				new ClusterServer(ipAddress, ipPort, getPresentClusterId());
				for (int i = 0; i < ref_tbViewInputPortData.getItems().size(); i++) {
					stateDesc = "";
					bayResponse = new BayResponse();
					InputPort inputPortDetails = ref_tbViewInputPortData.getItems().get(i);
					if (inputPortDetails.isReadBay()) {
						String inputPortId = inputPortDetails.getPortId();

						inputPortId = inputPortId.replaceAll("[^0-9.]", "");
						ApplicationLauncher.logger.info("SendDataToBayTask: inputPortId: " + inputPortId);

						if (GuiUtils.isNumber(inputPortId)) {
							Integer.parseInt(inputPortId);

							// bayResponse = ModbusTcpClient.modbusTcpSendReadCoilCmd(plcCoilAddress);
							if (!bayResponse.getStatus()) {
								ApplicationLauncher.logger
										.info("SendDataToBayTask: inputPortId: device not responded ");
								ref_tbViewInputPortData.getItems().get(i).setInputActive(false);
								overAllStatus = false;
							} else {
								stateDesc = ref_tbViewInputPortData.getItems().get(i).getOffStateDesc();
								ref_tbViewInputPortData.getItems().get(i)
										.setInputActive(bayResponse.isResponseBooleanData());
							}
						} else {
							ref_tbViewInputPortData.getItems().get(i).setInputActive(false);
							ApplicationLauncher.logger
									.info("SendDataToBayTask: invalid inputPortId: " + inputPortDetails.getPortId());
							overAllStatus = false;

							WindowManager.InformUser("Error-2010", "Kindly check key : " + inputPortDetails.getPortId()
									+ " on conveyor config file on <inputPort> section", AlertType.ERROR);
						}

						/*
						 * 
						 * RestApiJsonBodyResponse clusterResponseData =
						 * getInputDataFromBay(clusterServer, deviceId,bayId,inputPortId)
						 * ;//RestClient.getBayData(clusterServer, deviceId, bayId, inputPortId);
						 * ApplicationLauncher.logger.debug("SendDataToBayTask: getStatuscode : "
						 * +clusterResponseData.getStatusCode());
						 * 
						 * if(clusterResponseData.getStatusCode().equals("200")){
						 * if(clusterResponseData.getJsonBodyResponse().get(inputPortId).equals("On")){
						 * ref_tbViewInputPortData.getItems().get(i).setInputActive(true);
						 * stateDesc = ref_tbViewInputPortData.getItems().get(i).getOnStateDesc();
						 * 
						 * }else
						 * if(clusterResponseData.getJsonBodyResponse().get(inputPortId).equals("Off")){
						 * ref_tbViewInputPortData.getItems().get(i).setInputActive(false);
						 * stateDesc = ref_tbViewInputPortData.getItems().get(i).getOffStateDesc();
						 * }
						 * 
						 * 
						 * //JSONObject bodyRespinse = getJsonBodyResponse();
						 * }else {
						 * ref_tbViewInputPortData.getItems().get(i).setInputActive(false);
						 * }
						 */
						ref_tbViewInputPortData.getItems().get(i).setStateDescription(stateDesc);
						ref_tbViewInputPortData.refresh();
					}
				}
			}

			boolean overAllStatus1 = overAllStatus;
			Platform.runLater(() -> {
				ref_btnSendDataToBay.setDisable(false);
				if (overAllStatus1) {

					ref_txtComStatus.setText("WR/RD Success");

				} else {
					ref_txtComStatus.setText("WR/RD Failed");
				}

			});

			Integer.parseInt(ref_txtReadAddressSample.getText());

			/*
			 * if(ref_chkBxWriteEnabledSample.isSelected()) {
			 * int holdingWriteAddress =
			 * Integer.parseInt(ref_txtWriteAddressSample.getText());
			 * int holdingWriteValue = Integer.parseInt(ref_txtWriteValueSample.getText());
			 * BayResponse bayResponse3 =
			 * ModbusPlcDeviceData.modbusTcpSendWriteHoldingRegistersCmd(holdingWriteAddress
			 * ,holdingWriteValue);
			 * 
			 * if(!bayResponse3.getStatus()) {
			 * ApplicationLauncher.logger.
			 * info("SendDataToBayTask: WriteHoldingRegister: device not responded ");
			 * //ref_tbViewInputPortData.getItems().get(i).setInputActive(false);
			 * //overAllStatus = false;
			 * Platform.runLater(()->{
			 * ref_txtWriteStatusSample.setText("Failed");
			 * });
			 * }else {
			 * //stateDesc = ref_tbViewInputPortData.getItems().get(i).getOffStateDesc();
			 * //ref_tbViewInputPortData.getItems().get(i).setInputActive(bayResponse.
			 * isResponseBooleanData());
			 * Platform.runLater(()->{
			 * ref_txtWriteStatusSample.setText("Success");
			 * //ref_txtWriteValueSample.setText(bayResponse3.getResponseData());
			 * });
			 * }
			 * 
			 * }
			 */
		}
	}

	public RestApiJsonBodyResponse getInputDataFromBay(ClusterServer clusterServer, String deviceId, String bayId,
			String inputPortId) {
		ApplicationLauncher.logger.info("getInputDataFromBay-plc-server: Entry-failed-debug");
		String clusterId = clusterServer.getClusterId();
		ConveyorClientManager cluster1ClientManager = ConveyorClientManager.getInstance(clusterId);
		// cluster1ClientManager.getBayData(clusterServer,deviceId, bayId, inputPortId);
		String dummyOutputValue = "";
		boolean setOutput = false;
		cluster1ClientManager.messageBayData(setOutput, clusterServer, deviceId, bayId, inputPortId, dummyOutputValue);

		ApplicationHomeController.update_left_status("Awaiting Device Response", ConstantApp.LEFT_STATUS_DEBUG);
		// Sleep(8000);
		RestApiJsonBodyResponse clusterResponseData = new RestApiJsonBodyResponse();
		// clusterResponseData.setStatuscode(statuscode);
		// HashMap<Boolean,RestApiClusterResponse> returnData = new
		// HashMap<Boolean,RestApiClusterResponse>();
		boolean isResponseReceived = false;
		if (ProcalFeatureEnable.CONVEYOR_REST_CLIENT_ENABLED) {
			// cluster1ClientManager.getRestConvClient().WaitForServerResponse(8);
			isResponseReceived = cluster1ClientManager.getRestConvClient().isResponseReceived();
		} else {
			cluster1ClientManager.getAsyncConvClient().WaitForServerResponse(8);
			isResponseReceived = cluster1ClientManager.getAsyncConvClient().isResponseReceived();
		}
		if (isResponseReceived) { // validate for server access
			ApplicationHomeController.update_left_status("Device Connected", ConstantApp.LEFT_STATUS_DEBUG);

			// RestApiClusterResponse clusterResponseData = new RestApiClusterResponse();
			if (ProcalFeatureEnable.CONVEYOR_REST_CLIENT_ENABLED) {
				clusterResponseData = cluster1ClientManager.getRestConvClient().getRestApiClusterResponseBodyData();
			} else {
				clusterResponseData = cluster1ClientManager.getAsyncConvClient().getRestApiClusterResponseBodyData();
			}
			ApplicationLauncher.logger.info("getInputDataFromBay: " + inputPortId + " : getDevice Data: "
					+ clusterResponseData.getJsonBodyResponse().get(inputPortId));

			/*
			 * ApplicationLauncher.logger.info("getInputDataFromBay: "
			 * +inputPortId+" : getDevice Data: "+ clusterResponseData.getDevice());
			 * ApplicationLauncher.logger.info("getInputDataFromBay: "
			 * +inputPortId+" : getStatus Data: "+ clusterResponseData.getStatus());
			 * ApplicationLauncher.logger.info("getInputDataFromBay: "
			 * +inputPortId+" : getStatus getOpGreen: "+ clusterResponseData.getOpGreen());
			 * ApplicationLauncher.logger.info("getInputDataFromBay: "
			 * +inputPortId+" : getStatus getOpYellow: "+
			 * clusterResponseData.getOpYellow());
			 * ApplicationLauncher.logger.info("getInputDataFromBay: "
			 * +inputPortId+" : getStatus getOpRed: "+ clusterResponseData.getOpRed());
			 */

			// String responseData =
			// cluster1ClientManager.getAsyncConvClient().getResponseData();


			// ApplicationHomeController.EnableScanDeviceButton();
		} else {

			// ScanDeviceController.ScanDeviceCompletedPostProcess();
			// ApplicationHomeController.EnableScanDeviceButton();
			// ApplicationHomeController.DisableTestRunButton();
			// ApplicationHomeController.EnableLeftMenuButtonsForTestRun();
			/*
			 * Platform.runLater(()->{
			 * ref_txtAreaResponseDisplay.setText(ref_txtAreaResponseDisplay.getText()+"\n"+
			 * inputPortId+"-no response");
			 * });
			 */
			ApplicationHomeController.update_left_status("Device Connection Failed " + inputPortId + " :",
					ConstantApp.LEFT_STATUS_DEBUG);
		}
		return clusterResponseData;
	}

	public boolean setOutputDataToBay(ClusterServer clusterServer, String deviceId, String bayId, String outputPortId,
			String outputStatus) {
		ApplicationLauncher.logger.info("setOutputDataToBay-plc-server: Entry-failed-debug");
		String clusterId = clusterServer.getClusterId();
		boolean status = false;
		ConveyorClientManager cluster1ClientManager = ConveyorClientManager.getInstance(clusterId);// new
																									// ConveyorClientManager();
		// cluster1ClientManager.setBayData(clusterServer,deviceId, bayId, outputPortId,
		// outputStatus);

		// String dummyOutputValue= "";
		boolean setOutput = true;
		cluster1ClientManager.messageBayData(setOutput, clusterServer, deviceId, bayId, outputPortId, outputStatus);
		ApplicationHomeController.update_left_status("Awaiting Device Response", ConstantApp.LEFT_STATUS_DEBUG);
		// Sleep(8000);
		// cluster1ClientManager.getAsyncConvClient().WaitForServerResponse(8);
		boolean isResponseReceived = false;
		if (ProcalFeatureEnable.CONVEYOR_REST_CLIENT_ENABLED) {
			// cluster1ClientManager.getRestConvClient().WaitForServerResponse(8);
			isResponseReceived = cluster1ClientManager.getRestConvClient().isResponseReceived();
		} else {
			cluster1ClientManager.getAsyncConvClient().WaitForServerResponse(8);
			isResponseReceived = cluster1ClientManager.getAsyncConvClient().isResponseReceived();
		}
		if (isResponseReceived) {// cluster1ClientManager.getAsyncConvClient().isResponseReceived()){ //validate
									// for server access
			ApplicationHomeController.update_left_status("Device Connected", ConstantApp.LEFT_STATUS_DEBUG);

			RestApiClusterResponse clusterResponseData = new RestApiClusterResponse();
			// clusterResponseData =
			// cluster1ClientManager.getAsyncConvClient().getRestApiClusterResponseData();
			if (ProcalFeatureEnable.CONVEYOR_REST_CLIENT_ENABLED) {
				clusterResponseData = cluster1ClientManager.getRestConvClient().getRestApiClusterResponseData();
			} else {
				clusterResponseData = cluster1ClientManager.getAsyncConvClient().getRestApiClusterResponseData();
			}
			ApplicationLauncher.logger.info(
					"AsyncConveyorClient: " + outputPortId + " : getDevice Data: " + clusterResponseData.getDevice());
			ApplicationLauncher.logger.info(
					"AsyncConveyorClient: " + outputPortId + " : getStatus Data: " + clusterResponseData.getStatus());

			// String responseData =
			// cluster1ClientManager.getAsyncConvClient().getResponseData();
			/*
			 * Platform.runLater(()->{
			 * ref_txtAreaResponseDisplay.setText(responseData);
			 * });
			 */
			/*
			 * if(ref_txtAreaResponseDisplay.getText().isEmpty()) {
			 * ref_txtAreaResponseDisplay.setText(responseData);
			 * }else {
			 * ref_txtAreaResponseDisplay.setText(ref_txtAreaResponseDisplay.getText()+"\n"+
			 * responseData);
			 * }
			 */
			status = true;

			// ApplicationHomeController.EnableScanDeviceButton();
		} else {

			// ScanDeviceController.ScanDeviceCompletedPostProcess();
			// ApplicationHomeController.EnableScanDeviceButton();
			// ApplicationHomeController.DisableTestRunButton();
			// ApplicationHomeController.EnableLeftMenuButtonsForTestRun();
			/*
			 * Platform.runLater(()->{
			 * ref_txtAreaResponseDisplay.setText(ref_txtAreaResponseDisplay.getText()+"\n"+
			 * outputPortId+"-no response");
			 * });
			 */
			ApplicationHomeController.update_left_status("Device Connection Failed " + outputPortId + " :",
					ConstantApp.LEFT_STATUS_DEBUG);
			// status = null;
		}
		return status;
	}

	@FXML
	public void cmbBxClusterSelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxClusterSelectionOnChange: Entry");
		clusterSelectionOnChangeTimer = new Timer();
		clusterSelectionOnChangeTimer.schedule(new ClusterSelectionOnChangeTask(), 10);
	}

	class ClusterSelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxClusterSelection.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {
					ref_tbViewOutputPortData.getItems().clear();
					ref_tbViewInputPortData.getItems().clear();
					ref_cmbBxBaySelection.getItems().clear();
					ref_txtClusterIpAddress.setText("");
					ref_txtClusterPortNo.setText("");
					if (getClusterBayNameListMap().containsKey(selectedClusterName)) {
						ref_cmbBxBaySelection.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));
					}
					ref_cmbBxBaySelection.getSelectionModel().select(0);

				}
			});
			clusterSelectionOnChangeTimer.cancel();
		}
	}

	// BAY COMBO BOX CHANGE

	@FXML
	public void cmbBxBaySelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxBaySelectionOnChange: Entry");
		baySelectionOnChangeTimer = new Timer();
		baySelectionOnChangeTimer.schedule(new BaySelectionOnChangeTask(), 10);
	}

	class BaySelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {

				ref_cmbBxClusterSelection.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {
					ref_tbViewOutputPortData.getItems().clear();
					ref_tbViewInputPortData.getItems().clear();
					// ref_txtClusterIpAddress.setText("");
					// ref_txtClusterPortNo.setText("");
					// if(getClusterBayNameListMap().containsKey(selectedClusterName)){
					// ref_cmbBxBaySelection.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));
					// }
					// ref_cmbBxBaySelection.getSelectionModel().select(0);

				}
			});
			baySelectionOnChangeTimer.cancel();

		}
	}

	@FXML
	public void btnLoadOnClick() {

		ApplicationLauncher.logger.debug("btnLoadOnClick: Entry");
		loadOnClickTimer = new Timer();
		loadOnClickTimer.schedule(new LoadOnClickTask(), 10);

	}

	class LoadOnClickTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				ref_tbViewOutputPortData.getItems().clear();
				ref_tbViewInputPortData.getItems().clear();
				String clusterName = (ref_cmbBxClusterSelection.getSelectionModel().getSelectedItem().toString());
				String bayName = (ref_cmbBxBaySelection.getSelectionModel().getSelectedItem().toString());

				String clusterId = getClusterNameIdListMap().get(clusterName);
				// ApplicationLauncher.logger.debug("loadDataFromConfig :
				// getClusterBayNameIdMap().get(clusterName)-2 :"+
				// getClusterBayNameIdMap().get(clusterName));
				// String bayId = "11";//getClusterBayNameIdMap().get(clusterName).get(bayName);

				ApplicationLauncher.logger.debug("LoadOnClickTask : clusterId :" + clusterId);

				Optional<ClusterDetail> clusterOpt = getBayConfigModel().getTerminal().stream()
						.filter(e1 -> e1.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID))
						.flatMap(terminal -> terminal.getClusterDetails().stream())
						.filter(e2 -> e2.getClusterId().equals(clusterId))
						.findFirst();

				if (clusterOpt.isPresent()) {
					ClusterDetail clusterDetail = clusterOpt.get();
					ref_txtClusterIpAddress.setText(clusterDetail.getClusterIpAddress());
					ref_txtClusterPortNo.setText(clusterDetail.getClusterPortId());

				}

				// Optional<OutputPort> outputPortOpt =

				Optional<Bay> bayOpt = getBayConfigModel().getTerminal().stream()
						.filter(e1 -> e1.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID))
						.flatMap(terminal -> terminal.getClusterDetails().stream())
						.filter(e2 -> e2.getClusterId().equals(clusterId))
						.flatMap(e3 -> e3.getBay().stream())
						.filter(e4 -> e4.getBayName().equals(bayName))
						.findFirst();

				if (bayOpt.isPresent()) {
					Bay bayDetails = bayOpt.get();
					String bayId = bayDetails.getBayId();
					ApplicationLauncher.logger.debug("LoadOnClickTask : bayId :" + bayId);

					ArrayList<OutputPort> outputPortList = (ArrayList<OutputPort>) getBayConfigModel().getTerminal()
							.stream()
							.flatMap(terminal -> terminal.getOutputPort().stream())
							.filter(p -> clusterId.equals(p.getClusterId()))
							.filter(p -> bayId.equals(p.getBayId()))
							.collect(Collectors.toList());

					if (outputPortList.size() > 0) {
						int serialNo = 1;
						for (OutputPort eachOutputPort : outputPortList) {
							eachOutputPort.setSerialNo(String.valueOf(serialNo));
							if (eachOutputPort.isOutputActive()) {
								eachOutputPort.setStateDescription(eachOutputPort.getOnStateDesc());
								// ApplicationLauncher.logger.debug("OutputPortActiveCheckBoxValueFactory :
								// getStateDescription-1 : " + rowData.getStateDescription());
							} else {
								eachOutputPort.setStateDescription(eachOutputPort.getOffStateDesc());
								// ApplicationLauncher.logger.debug("OutputPortActiveCheckBoxValueFactory :
								// getStateDescription-12: " + rowData.getStateDescription());

							}
							// eachOutputPort.setStateDescription("DummyData");
							serialNo++;
						}
						ref_tbViewOutputPortData.getItems().addAll(outputPortList);

					}

					ArrayList<InputPort> inputPortList = (ArrayList<InputPort>) getBayConfigModel().getTerminal()
							.stream()
							.flatMap(terminal -> terminal.getInputPort().stream())
							.filter(p -> clusterId.equals(p.getClusterId()))
							.filter(p -> bayId.equals(p.getBayId()))
							.collect(Collectors.toList());

					if (inputPortList.size() > 0) {
						int serialNo = 1;
						for (InputPort eachInputPort : inputPortList) {
							eachInputPort.setSerialNo(String.valueOf(serialNo));
							if (eachInputPort.isInputActive()) {
								eachInputPort.setStateDescription(eachInputPort.getOnStateDesc());
								// ApplicationLauncher.logger.debug("OutputPortActiveCheckBoxValueFactory :
								// getStateDescription-1 : " + rowData.getStateDescription());
							} else {
								eachInputPort.setStateDescription(eachInputPort.getOffStateDesc());
								// ApplicationLauncher.logger.debug("OutputPortActiveCheckBoxValueFactory :
								// getStateDescription-12: " + rowData.getStateDescription());

							}
							serialNo++;
						}
						ref_tbViewInputPortData.getItems().addAll(inputPortList);

					}

				}
			});
			loadOnClickTimer.cancel();

		}
	}

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep2 :InterruptedException:" + e.getMessage());
		}

	}

	public Map<String, String> getClusterNameIdListMap() {
		return clusterNameIdListMap;
	}

	public void setClusterNameIdListMap(Map<String, String> clusterIdNameListMap) {
		this.clusterNameIdListMap = clusterIdNameListMap;
	}

	public Map<String, String> getClusterBayNameIdMap() {
		return clusterBayNameIdMap;
	}

	public void setClusterBayNameIdMap(Map<String, String> clusterBayNameIdMap) {
		this.clusterBayNameIdMap = clusterBayNameIdMap;
	}

	public static TerminalBayConfigModel getBayConfigModel() {
		return bayConfigModel;
	}

	public static void setBayConfigModel(TerminalBayConfigModel bayConfigModel) {
		PlcServerBayTestController.bayConfigModel = bayConfigModel;
	}

	public Map<String, ArrayList<String>> getClusterBayNameListMap() {
		return clusterBayNameListMap;
	}

	public void setClusterBayNameListMap(Map<String, ArrayList<String>> bayNameListMap) {
		this.clusterBayNameListMap = bayNameListMap;
	}

	public static String getPresentClusterId() {
		return presentClusterId;
	}

	public static void setPresentClusterId(String presentClusterId) {
		BayTestController.presentClusterId = presentClusterId;
	}

	private void updateHoldingRegisters(int[] holdingRegisters) {
		Platform.runLater(() -> {
			// int address = 301; // Example register to display
			ArrayList<Integer> serverInputHoldingRegisterAddressList = new ArrayList<>(Arrays.asList(301));

			for (int i = 0; i < holdingRegisters.length; i++) {

				if (serverInputHoldingRegisterAddressList.contains(i)) {
					ref_txtReadAddressSample.setText(String.valueOf(i));
					ref_txtReadValueSample.setText(String.valueOf(holdingRegisters[i]));
					ApplicationLauncher.logger
							.debug("ðŸ“¡ GUI Updated: Holding Register[" + i + "] = " + holdingRegisters[i]);
				}

			}
			// ref_txtReadAddressSample.setText(String.valueOf(address));
			// ref_txtReadValueSample.setText(String.valueOf(holdingRegisters[address]));

		});
	}

	// âœ… Update GUI for Coils
	private void updateCoils(boolean[] coils) {
		Platform.runLater(() -> {
			inputPortList.clear();
			for (int i = 0; i <= 32; i++) {
				InputPort inputPort = new InputPort();
				inputPort.setPortId(String.valueOf(i));
				inputPort.setPortName("Coil " + i);
				inputPort.setStateDescription(coils[i] ? "On" : "Off");
				inputPort.setInputActive(coils[i]);
				inputPort.setSerialNo(String.valueOf(i + 1));
				inputPortList.add(inputPort);
			}
			ref_tbViewInputPortData.refresh();
			ApplicationLauncher.logger.debug("ðŸ“¡ GUI Updated: Coil Data");
		});
	}

	@FXML
	public void modbusStartServerTrigger() {
		ApplicationLauncher.logger.info("modbusStartServerTrigger : Entry");
		startServerTimer = new Timer();
		startServerTimer.schedule(new ModbusStartServerTask(), 100);
	}

	class ModbusStartServerTask extends TimerTask {

		@Override
		public void run() {

			String portAddress = ref_txtClusterPortNo.getText();
			String ipAddress = ref_txtClusterIpAddress.getText();
			setModbusTcpServer(new ModbusTcpServer(ipAddress, Integer.parseInt(portAddress)));

			boolean status = getModbusTcpServer().start();

			if (status) {
				ref_txtComStatus.setText("Active");

				ref_tbViewInputPortData.setItems(inputPortList);
				getModbusTcpServer()
						.setHoldingRegisterUpdateListener(holdingRegisters -> updateHoldingRegisters(holdingRegisters));
				getModbusTcpServer().setCoilUpdateListener(coils -> updateCoils(coils));
			} else {
				ref_txtComStatus.setText("Failed");
			}

			startServerTimer.cancel();

		}

	}

	@FXML
	public void modbusStopServerTrigger() {
		ApplicationLauncher.logger.info("modbusStopServerTrigger : Entry");
		stopServerTimer = new Timer();
		stopServerTimer.schedule(new ModbusStopServerTask(), 100);
	}

	class ModbusStopServerTask extends TimerTask {

		@Override
		public void run() {

			if (getModbusTcpServer() != null) {
				// modbusServer.StopListening();
				// modbusServer.stop();
				getModbusTcpServer().stop();
				ApplicationLauncher.logger.debug("ðŸ›‘ Modbus Server Stopped.");
				ref_txtComStatus.setText("Stopped");
			}
			stopServerTimer.cancel();

		}

	}

	public ModbusTcpServer getModbusTcpServer() {
		return modbusTcpServer;
	}

	public void setModbusTcpServer(ModbusTcpServer modbusTcpServer) {
		this.modbusTcpServer = modbusTcpServer;
	}

}