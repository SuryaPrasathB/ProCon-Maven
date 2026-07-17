package com.tasnetwork.calibration.conveyor.plc;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.tasnetwork.calibration.conveyor.BayTestController;
import com.tasnetwork.calibration.conveyor.ClusterServer;
import com.tasnetwork.calibration.conveyor.InputPortActiveCheckBoxValueFactory;
import com.tasnetwork.calibration.conveyor.InputPortReadBayCheckBoxValueFactory;
import com.tasnetwork.calibration.conveyor.InputPortTableViewRefresher;
import com.tasnetwork.calibration.conveyor.OutputPortActiveCheckBoxValueFactory;
import com.tasnetwork.calibration.conveyor.OutputPortTableViewRefresher;
import com.tasnetwork.calibration.conveyor.OutputPortUpdateBayCheckBoxValueFactory;
import com.tasnetwork.calibration.conveyor.RestApiClusterResponse;
import com.tasnetwork.calibration.conveyor.RestApiJsonBodyResponse;
import com.tasnetwork.calibration.conveyor.AsyncHttpClient.ConveyorClientManager;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
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
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.Cursor;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

public class PlcClientBayTestController implements Initializable,InputPortTableViewRefresher,OutputPortTableViewRefresher  {
	
	Timer connectTimer;
	Timer disconnectTimer;
	Timer sendCommDataTaskTimer;
	Timer loadOnClickTimer;
	Timer baySelectionOnChangeTimer;
	Timer clusterSelectionOnChangeTimer;
	Timer funtionalBaySingleStateTaskTimer;
	
/*	Map<String,Boolean> modbusTcpServerConnectedMap = new HashMap<String, Boolean>();
	Map<String,ModbusTcpClient> modbusTcpServerObjectMap = new HashMap<String, ModbusTcpClient>();
	
	ModbusTcpClient modbusTcpClient = new ModbusTcpClient();*/
	
	private static TerminalBayConfigModel  bayConfigModel = ConveyorDataManager.getTerminalBayConfig();
	
	public static String presentClusterId = "";

	private Map<String,ArrayList<String>> clusterBayNameListMap = new HashMap<String,ArrayList<String>>();
	private Map<String,String> clusterNameIdListMap = new HashMap<String,String>();
	private Map<String,String> clusterBayNameIdMap = new HashMap<String,String>();
	
	
	
	@FXML CheckBox chkBxReadEnabledSample;
	public static  CheckBox ref_chkBxReadEnabledSample;
	
	@FXML TextField txtReadAddressSample;
	public static TextField ref_txtReadAddressSample;
	
	@FXML TextField txtReadValueSample;
	public static TextField ref_txtReadValueSample;
	
	@FXML TextField txtReadStatusSample;
	public static TextField ref_txtReadStatusSample;
	
	
	@FXML CheckBox chkBxWriteEnabledSample;
	public static  CheckBox ref_chkBxWriteEnabledSample;
	
	@FXML TextField txtWriteAddressSample;
	public static TextField ref_txtWriteAddressSample;
	
	@FXML TextField txtWriteValueSample;
	public static TextField ref_txtWriteValueSample;
	
	@FXML TextField txtWriteStatusSample;
	public static TextField ref_txtWriteStatusSample;
	
	  @FXML
	    private Button btnLoad;


	    
		@FXML
		private Button btnSendDataToBay;
		public static Button ref_btnSendDataToBay;
		
	    @FXML
	    private Button btnConnect;
	    public static Button ref_btnConnect;
	    
	    @FXML TextField txtComStatus;
	    public static TextField ref_txtComStatus;

		@FXML
		private TableView<OutputPort> tbViewOutputPortData;
		public static TableView<OutputPort> ref_tbViewOutputPortData;


		@FXML
		private TableColumn<OutputPort,String> tblColOutputSerialNo;
		public static TableColumn<OutputPort,String> ref_tblColOutputSerialNo;

		@FXML
		private TableColumn<OutputPort,String> tblColOutputPortStateDescription;
		public static TableColumn<OutputPort,String> ref_tblColOutputPortStateDescription;




		@FXML
		private TableColumn<OutputPort,String> tblColOutputPortName;
		public static TableColumn<OutputPort,String> ref_tblColOutputPortName;
		
		@FXML
		private TableColumn<OutputPort,String> tblColOutputPortId;
		public static TableColumn<OutputPort,String> ref_tblColOutputPortId;

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
		private TableColumn<InputPort,String> tblColInputSerialNo;
		public static TableColumn<InputPort,String> ref_tblColInputSerialNo;

		@FXML
		private TableColumn<InputPort,String> tblColInputPortStateDescription;
		public static TableColumn<InputPort,String> ref_tblColInputPortStateDescription;



		@FXML
		private TableColumn<InputPort,String> tblColInputPortName;
		public static TableColumn<InputPort,String> ref_tblColInputPortName;
		
		@FXML
		private TableColumn<InputPort,String> tblColInputPortId;
		public static TableColumn<InputPort,String> ref_tblColInputPortId;

		@FXML
		private TableColumn tblColInputActive;
		public static TableColumn ref_tblColInputActive;

		@FXML
		private TableColumn tblColInputReadBay;
		public static TableColumn ref_tblColInputReadBay;



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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		//TerminalBayConfigModel  bayConfigModel = ConveyorDeviceDataManagerController.getBayConfigParsedKey();
		loadDataFromConfig();

		ref_tbViewOutputPortData.getItems().clear();
		ref_tbViewInputPortData.getItems().clear();
		ref_txtClusterIpAddress.setText("");
		ref_txtClusterPortNo.setText("");
	}
	
	private static PlcClientBayTestController instance;

	public PlcClientBayTestController() {
	    instance = this;
	}

	public static PlcClientBayTestController getInstance() {
	    return instance;
	}

	public void guiInit() {
		// TODO Auto-generated method stub
		ref_tblColOutputSerialNo.setCellValueFactory(new PropertyValueFactory<OutputPort, String>("serialNo"));
		ref_tblColOutputPortName.setCellValueFactory(new PropertyValueFactory<OutputPort, String>("portName"));
		ref_tblColOutputPortId.setCellValueFactory(new PropertyValueFactory<OutputPort, String>("portId"));
		ref_tblColOutputActive.setCellValueFactory(new OutputPortActiveCheckBoxValueFactory(PlcClientBayTestController.getInstance()));
		ref_tblColOutputActive.setStyle( "-fx-alignment: CENTER;");
		ref_tblColOutputUpdateBay.setCellValueFactory(new OutputPortUpdateBayCheckBoxValueFactory());
		ref_tblColOutputUpdateBay.setStyle( "-fx-alignment: CENTER;");
		ref_tblColOutputPortStateDescription.setCellValueFactory(new PropertyValueFactory<OutputPort, String>("stateDescription"));


		ref_tblColInputSerialNo.setCellValueFactory(new PropertyValueFactory<InputPort, String>("serialNo"));
		ref_tblColInputPortName.setCellValueFactory(new PropertyValueFactory<InputPort, String>("portName"));
		ref_tblColInputPortId.setCellValueFactory(new PropertyValueFactory<InputPort, String>("portId"));
		ref_tblColInputActive.setEditable(false);
		ref_tblColInputActive.setCellValueFactory(new InputPortActiveCheckBoxValueFactory(PlcClientBayTestController.getInstance()));
		ref_tblColInputActive.setStyle( "-fx-alignment: CENTER;");
		ref_tblColInputReadBay.setCellValueFactory(new InputPortReadBayCheckBoxValueFactory());
		ref_tblColInputReadBay.setStyle( "-fx-alignment: CENTER;");
		ref_tblColInputPortStateDescription.setCellValueFactory(new PropertyValueFactory<InputPort, String>("stateDescription"));


	}
	
	

	private void refInit() {
		// TODO Auto-generated method stub
		ref_cmbBxBaySelection= cmbBxBaySelection;
		ref_cmbBxClusterSelection = cmbBxClusterSelection;

/*		ref_cmbBxPosLoadedBaySelection = cmbBxPosLoadedBaySelection;
		ref_cmbBxPosLoadedClusterSelection = cmbBxPosLoadedClusterSelection;
		ref_cmbBxPosLoadedTerminalSelection = cmbBxPosLoadedTerminalSelection;

		ref_cmbBxFilterPosition = cmbBxFilterPosition;*/


		ref_txtClusterIpAddress = txtClusterIpAddress;

		ref_txtClusterPortNo = txtClusterPortNo;



		ref_tbViewOutputPortData = tbViewOutputPortData;		
		ref_tblColOutputSerialNo = tblColOutputSerialNo;		
		ref_tblColOutputPortName = tblColOutputPortName;	
		ref_tblColOutputPortId = tblColOutputPortId;
		ref_tblColOutputActive =tblColOutputActive;		
		ref_tblColOutputUpdateBay = tblColOutputUpdateBay;
		ref_tblColOutputPortStateDescription = tblColOutputPortStateDescription;


		ref_tbViewInputPortData = tbViewInputPortData;		
		ref_tblColInputSerialNo = tblColInputSerialNo;		
		ref_tblColInputPortName = tblColInputPortName;	
		ref_tblColInputPortId = tblColInputPortId;
		ref_tblColInputActive =tblColInputActive;		
		ref_tblColInputReadBay = tblColInputReadBay;
		ref_tblColInputPortStateDescription = tblColInputPortStateDescription;
		
		ref_btnSendDataToBay = btnSendDataToBay;
		ref_btnConnect = btnConnect;
		ref_txtComStatus = txtComStatus;
		
		ref_chkBxReadEnabledSample = chkBxReadEnabledSample;
		ref_txtReadAddressSample = txtReadAddressSample;
		ref_txtReadValueSample = txtReadValueSample;
		ref_txtReadStatusSample = txtReadStatusSample;

		
		ref_chkBxWriteEnabledSample = chkBxWriteEnabledSample;
		ref_txtWriteAddressSample = txtWriteAddressSample;
		ref_txtWriteValueSample = txtWriteValueSample;
		ref_txtWriteStatusSample = txtWriteStatusSample;

	}
	
	public void loadDataFromConfig() {			
		// B A Y  T E S T ====================================================
		ApplicationLauncher.logger.debug("loadDataFromConfig: Entry");
		ref_cmbBxClusterSelection.getItems().clear();
		for(Terminal eachTerminal: getBayConfigModel().getTerminal()){
			if(eachTerminal.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID)){
				for(ClusterDetail eachClusterDetail: eachTerminal.getClusterDetails()){
					ref_cmbBxClusterSelection.getItems().add(eachClusterDetail.getName());
					getClusterNameIdListMap().put(eachClusterDetail.getName(), eachClusterDetail.getClusterId());
					ArrayList<String> bayList = new ArrayList<String>();
					getClusterBayNameListMap().put(eachClusterDetail.getName(), bayList);


					for (Bay eachBay: eachClusterDetail.getBay()){
						//ref_cmbBxBaySelection.getItems().add(eachBay.getBayName());
						bayList.add(eachBay.getBayName());
						getClusterBayNameListMap().put(eachClusterDetail.getName(), bayList);
						Map<String,String> bayNameIdMap = new HashMap<String,String>();
						bayNameIdMap.put(eachBay.getBayName(), eachBay.getBayId());
						//getClusterBayNameIdMap().put(eachClusterDetail.getName(), bayNameIdMap);
						getClusterBayNameIdMap().put(eachClusterDetail.getName()+"_"+eachBay.getBayName(), eachBay.getBayId());
						//ApplicationLauncher.logger.debug("loadDataFromConfig : getClusterBayNameIdMap().get(clusterName)-1 :"+ getClusterBayNameIdMap());

					}
				}
			}
		}

		if(ref_cmbBxClusterSelection.getItems().size()>0){
			ref_cmbBxClusterSelection.getSelectionModel().select(0);

		}

		if(getClusterBayNameListMap().size()>0){
			if(getClusterBayNameListMap().containsKey(ref_cmbBxClusterSelection.getSelectionModel().getSelectedItem().toString())){
				ref_cmbBxBaySelection.getItems().addAll(getClusterBayNameListMap().get(ref_cmbBxClusterSelection.getSelectionModel().getSelectedItem().toString()));
			}
			//ref_cmbBxBaySelection.getSelectionModel().select(0);

		}

		if(ref_cmbBxBaySelection.getItems().size()>0){
			ref_cmbBxBaySelection.getSelectionModel().select(0);
			String clusterName = (ref_cmbBxClusterSelection.getSelectionModel().getSelectedItem().toString());
			String bayName = (ref_cmbBxBaySelection.getSelectionModel().getSelectedItem().toString());

			String clusterId = getClusterNameIdListMap().get(clusterName);
			//ApplicationLauncher.logger.debug("loadDataFromConfig : getClusterBayNameIdMap().get(clusterName)-2 :"+ getClusterBayNameIdMap().get(clusterName));
			//String bayId = "11";//getClusterBayNameIdMap().get(clusterName).get(bayName);

			ApplicationLauncher.logger.debug("loadDataFromConfig : clusterId :"+ clusterId);
			String clusterIpAddress = "";
			String clusterPortNo = "";

			Optional<ClusterDetail> clusterOpt = 		getBayConfigModel().getTerminal().stream()
					.filter(e1->e1.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID))
					.flatMap(terminal -> terminal.getClusterDetails().stream())
					.filter(e2->e2.getClusterId().equals(clusterId))
					.findFirst();

			if (clusterOpt.isPresent()) {
				ClusterDetail clusterDetail = clusterOpt.get();
				ref_txtClusterIpAddress.setText(clusterDetail.getClusterIpAddress());
				ref_txtClusterPortNo.setText(clusterDetail.getClusterPortId());
				setPresentClusterId(clusterId);

			}
			//Optional<OutputPort> outputPortOpt = 

			Optional<Bay> bayOpt = 		getBayConfigModel().getTerminal().stream()
					.filter(e1->e1.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID))
					.flatMap(terminal -> terminal.getClusterDetails().stream())
					.filter(e2->e2.getClusterId().equals(clusterId))
					.flatMap(e3 -> e3.getBay().stream())
					.filter(e4->e4.getBayName().equals(bayName))
					.findFirst();

			if (bayOpt.isPresent()) {
				Bay bayDetails = bayOpt.get();
				String bayId = bayDetails.getBayId();
				ApplicationLauncher.logger.debug("loadDataFromConfig : bayId :"+ bayId);

				ArrayList<OutputPort> outputPortList = (ArrayList<OutputPort>) getBayConfigModel().getTerminal().stream()
						.flatMap(terminal -> terminal.getOutputPort().stream())
						.filter(p -> clusterId.equals(p.getClusterId()))
						.filter(p -> bayId.equals(p.getBayId()))
						.collect(Collectors.toList());

				if (outputPortList.size()>0) {
					int serialNo = 1;
					for(OutputPort eachOutputPort : outputPortList) {
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

				if (inputPortList.size()>0) {
					int serialNo = 1;
					for(InputPort eachInputPort : inputPortList) {
						eachInputPort.setSerialNo(String.valueOf(serialNo));
						serialNo++; 
					}
					ref_tbViewInputPortData.getItems().addAll(inputPortList);
				}
			}
		}



	}

	
	
/*	@FXML
    void btnLoadOnClick(ActionEvent event) {

    }

    @FXML
    void btnSendDataToBayOnClick(ActionEvent event) {

    }

    @FXML
    void cmbBxBaySelectionOnChange(ActionEvent event) {

    }

    @FXML
    void cmbBxClusterSelectionOnChange(ActionEvent event) {

    }*/
    
    @FXML 
	public void  btnSendDataToBayOnClick() {

		ApplicationLauncher.logger.debug("btnSendDataToBayOnClick: Entry");
		sendCommDataTaskTimer = new Timer();
		//sendCommDataTaskTimer.schedule(new SendDataToBayTask(),10);
		sendCommDataTaskTimer.schedule(new SendDataToBayTaskInParallel(),10);

	}
    
    @FXML 
	public void btnConnectOnClick() {
    	ApplicationLauncher.logger.debug("btnConnectOnClick: Entry");
    	for (int i = 0; i < 2000; i++) {
    		ApplicationLauncher.logger.debug("btnConnectOnClick: Count : " + i);
    		funtionalBaySingleStateTaskTimer = new Timer();
			funtionalBaySingleStateTaskTimer.schedule(new FunctionalTestBaySingleStateTestRun(), 100);
			Sleep(500);
			funtionalBaySingleStateTaskTimer.cancel();
			
			Sleep(5000);
    	}
    }
    
    
    class SendDataToBayTaskInParallel extends TimerTask {
        public void run() {
            Platform.runLater(() -> {
                ref_btnSendDataToBay.setDisable(true);
                ref_txtComStatus.setText("Sending...");
            });

            ConveyorClientManager cluster1ClientManager = ConveyorClientManager.getInstance();
            String ipAddress = ref_txtClusterIpAddress.getText();
            String ipPort = ref_txtClusterPortNo.getText();
            ClusterServer clusterServer = new ClusterServer(ipAddress, ipPort, getPresentClusterId());
            String serverKey = clusterServer.getIpAddress() + ":" + clusterServer.getPort();

            AtomicInteger pendingRequests = new AtomicInteger(0);
            AtomicBoolean hasFailure = new AtomicBoolean(false);

            // Output Port Processing (Writing Coils)
            List<OutputPort> outputPorts = ref_tbViewOutputPortData.getItems().stream()
                    .filter(OutputPort::isUpdateBay)
                    .collect(Collectors.toList());

            pendingRequests.addAndGet(outputPorts.size());

            outputPorts.parallelStream().forEach(outputPortDetails -> {
                String outputPortId = outputPortDetails.getPortId().replaceAll("[^0-9]", "");
                if (GuiUtils.isNumber(outputPortId)) {
                    int plcCoilAddress = Integer.parseInt(outputPortId);
                    boolean writeValue = outputPortDetails.isOutputActive();

                    ModbusRequestProcessor.addRequest(serverKey, () -> {
                        BayResponse response = BayUtils.getModbusTcpClientManager()
                                .modbusWriteCoil(clusterServer, plcCoilAddress, writeValue);

                        if (!response.getStatus()) hasFailure.set(true);

                        if (pendingRequests.decrementAndGet() == 0) updateComStatus(hasFailure);
                    });

                } else {
                    ApplicationLauncher.InformUser(
                            "Error-1010",
                            "Invalid outputPortId: " + outputPortDetails.getPortId(),
                            AlertType.ERROR);
                    hasFailure.set(true);
                    if (pendingRequests.decrementAndGet() == 0) updateComStatus(hasFailure);
                }
            });

            // Input Port Processing (Reading Coils)
            List<InputPort> inputPorts = ref_tbViewInputPortData.getItems().stream()
                    .filter(InputPort::isReadBay)
                    .collect(Collectors.toList());

            pendingRequests.addAndGet(inputPorts.size());

            inputPorts.parallelStream().forEach(inputPortDetails -> {
                String inputPortId = inputPortDetails.getPortId().replaceAll("[^0-9]", "");
                if (GuiUtils.isNumber(inputPortId)) {
                    int plcCoilAddress = Integer.parseInt(inputPortId);

                    ModbusRequestProcessor.addRequest(serverKey, () -> {
                        BayResponse bayResponse = BayUtils.getModbusTcpClientManager()
                                .modbusReadCoil(clusterServer, plcCoilAddress);

                        Platform.runLater(() -> {
                            inputPortDetails.setInputActive(bayResponse.isResponseBooleanData());
                            ref_tbViewInputPortData.refresh();
                        });

                        if (!bayResponse.getStatus()) hasFailure.set(true);
                        if (pendingRequests.decrementAndGet() == 0) updateComStatus(hasFailure);
                    });

                } else {
                    Platform.runLater(() -> {
                        ApplicationLauncher.InformUser(
                                "Error-2010",
                                "Invalid inputPortId: " + inputPortDetails.getPortId(),
                                AlertType.ERROR);
                        ref_tbViewInputPortData.refresh();
                    });

                    hasFailure.set(true);
                    if (pendingRequests.decrementAndGet() == 0) updateComStatus(hasFailure);
                }
            });

            // Read Holding Register
            if (ref_chkBxReadEnabledSample.isSelected()) {
                int holdingReadAddress = Integer.parseInt(ref_txtReadAddressSample.getText());
                pendingRequests.incrementAndGet();

                ModbusRequestProcessor.addRequest(serverKey, () -> {
                    BayResponse response = BayUtils.getModbusTcpClientManager().modbusReadHoldingRegister(clusterServer, holdingReadAddress);

                    Platform.runLater(() -> {
                        ref_txtReadStatusSample.setText(response.getStatus() ? "Success" : "Failed");
                        if (response.getStatus()) ref_txtReadValueSample.setText(response.getResponseData());
                    });

                    if (!response.getStatus()) hasFailure.set(true);
                    if (pendingRequests.decrementAndGet() == 0) updateComStatus(hasFailure);
                });
            }

            // Write Holding Register
            if (ref_chkBxWriteEnabledSample.isSelected()) {
                int holdingWriteAddress = Integer.parseInt(ref_txtWriteAddressSample.getText());
                int holdingWriteValue = Integer.parseInt(ref_txtWriteValueSample.getText());
                pendingRequests.incrementAndGet();

                ModbusRequestProcessor.addRequest(serverKey, () -> {
                    BayResponse response = BayUtils.getModbusTcpClientManager()
                            .modbusWriteHoldingRegister(clusterServer, holdingWriteAddress, holdingWriteValue);

                    Platform.runLater(() -> ref_txtWriteStatusSample.setText(response.getStatus() ? "Success" : "Failed"));

                    if (!response.getStatus()) hasFailure.set(true);
                    if (pendingRequests.decrementAndGet() == 0) updateComStatus(hasFailure);
                });
            }

            if((outputPorts.size()==0) && (inputPorts.size() ==0) &&
            		(!ref_chkBxReadEnabledSample.isSelected()) &&
            		(!ref_chkBxWriteEnabledSample.isSelected())
            		) {
            	Platform.runLater(() -> {
            		ref_btnSendDataToBay.setDisable(false);
            		ref_txtComStatus.setText("");
            	});
            }
        }

        private void updateComStatus(AtomicBoolean hasFailure) {
            Platform.runLater(() -> {
                ref_btnSendDataToBay.setDisable(false);
                ref_txtComStatus.setText(hasFailure.get() ? "WR/RD Failed" : "WR/RD Success");
            });
        }
    }

	class SendDataToBayTask extends TimerTask {
		public void run() {
			Platform.runLater(()->{
				ref_btnSendDataToBay.setDisable(true);
				ref_txtComStatus.setText("Sending...");
				ref_txtReadStatusSample.setText("");
				ref_txtWriteStatusSample.setText("");
			});

			ConveyorClientManager cluster1ClientManager = ConveyorClientManager.getInstance();
			/*			Platform.runLater(()->{
				ref_txtAreaResponseDisplay.clear();
			});*/

			/*if(ref_chkBxWriteGreenLed.isSelected()) {
				String greenLedStatus = "Off";
				if(ref_chkBxGreenLedData.isSelected()) {
					greenLedStatus = "On";
				}*/

			String deviceId = "1"; 
			String bayId = "1";
			boolean overAllStatus = true;
			boolean deviceResponded = false;
			//int plcCoilAddress = -1;
			String ipAddress = ref_txtClusterIpAddress.getText();
			String ipPort = ref_txtClusterPortNo.getText();
			ClusterServer clusterServer = new ClusterServer(ipAddress,ipPort ,getPresentClusterId());
			String serverKey = clusterServer.getIpAddress() + ":" + clusterServer.getPort();
			if(ref_tbViewOutputPortData.getItems().size()>0) {
				//String ipAddress = ref_txtClusterIpAddress.getText();
				//String ipPort = ref_txtClusterPortNo.getText();
				//ClusterServer clusterServer = new ClusterServer(ipAddress,ipPort ,getPresentClusterId());
/*				BayResponse bayResponse = new BayResponse();
				for(int i =0; i< ref_tbViewOutputPortData.getItems().size(); i++) {
					plcCoilAddress = -1;
					bayResponse = new BayResponse();
					OutputPort outputPortDetails =  ref_tbViewOutputPortData.getItems().get(i);
					if(outputPortDetails.isUpdateBay()) {
						String outputPortId = outputPortDetails.getPortId();						
						String outputActive = "Off";
						if(outputPortDetails.isOutputActive()) {
							outputActive = "On";
						}
						//outputPortId = outputPortId.replace("ipt", "").replace("ip", "").replace("op", "");
						outputPortId = outputPortId.replaceAll("[^0-9.]", "");
						ApplicationLauncher.logger.info("SendDataToBayTask: outputPortId: "+outputPortId);
						//deviceResponded = setOutputDataToBay(clusterServer, deviceId,bayId,outputPortId, outputActive) ;
						if(GuiUtils.isNumber(outputPortId)) {
							plcCoilAddress = Integer.parseInt(outputPortId);
							

							bayResponse = BayUtils.getModbusTcpClientManager().modbusWriteCoil(clusterServer, plcCoilAddress, outputPortDetails.isOutputActive());
							if(!bayResponse.getStatus()) {
								ApplicationLauncher.logger.info("SendDataToBayTask: outputPortId: device not responded ");
								overAllStatus = false;
							}
						}else {
							ApplicationLauncher.logger.info("SendDataToBayTask: invalid outputPortId: "+outputPortDetails.getPortId());
							overAllStatus = false;
							
							ApplicationLauncher.InformUser("Error-1010","Kindly check key : "+outputPortDetails.getPortId() +" on conveyor config file on <outputPort> section",AlertType.ERROR);
						}
					}
				}*/
				
				List<OutputPort> outputPorts = ref_tbViewOutputPortData.getItems().stream()
			            .filter(OutputPort::isUpdateBay) // Filter only ports that need updating
			            .collect(Collectors.toList());
				
				outputPorts.parallelStream().forEach(outputPortDetails -> {
			       // int plcCoilAddress = -1;
			        String outputPortId = outputPortDetails.getPortId().replaceAll("[^0-9]", "");

			        ApplicationLauncher.logger.info("SendDataToBayTask: outputPortId: " + outputPortId);

			        if (GuiUtils.isNumber(outputPortId)) {
			           int  plcCoilAddress = Integer.parseInt(outputPortId);
			            //int plcCoilAddress2 = plcCoilAddress1;
			            boolean writeValue = outputPortDetails.isOutputActive();

			            // Enqueue the Modbus write request for this specific server
			            ModbusRequestProcessor.addRequest(serverKey, () -> {
			                BayResponse response = BayUtils.getModbusTcpClientManager()
			                    .modbusWriteCoil(clusterServer, plcCoilAddress, writeValue);

			                if (!response.getStatus()) {
			                    ApplicationLauncher.logger.info("SendDataToBayTask: outputPortId: device not responded ");
			                }
			            });

			        } else {
			            ApplicationLauncher.logger.info("SendDataToBayTask: invalid outputPortId: " + outputPortDetails.getPortId());
			            ApplicationLauncher.InformUser(
			                "Error-1010",
			                "Kindly check key : " + outputPortDetails.getPortId() + " on conveyor config file on <outputPort> section",
			                AlertType.ERROR
			            );
			        }
			    });
				
			}

			
			//BayResponse bayResponse = new BayResponse();
			int baseAddress = 0;
			if(ref_tbViewInputPortData.getItems().size()>0) {
				
				ref_tbViewInputPortData.getItems().parallelStream()
		        .filter(InputPort::isReadBay) // Filter only ports that need reading
		        .forEach(inputPortDetails -> {
		            AtomicReference<String> stateDesc = new AtomicReference<>("");

		            String inputPortId = inputPortDetails.getPortId().replaceAll("[^0-9]", "");
		            ApplicationLauncher.logger.info("SendDataToBayTask: inputPortId: " + inputPortId);

		            if (GuiUtils.isNumber(inputPortId)) {
		                final int plcCoilAddress = Integer.parseInt(inputPortId); // ✅ Use 'final' to avoid lambda issues

		                // Enqueue the Modbus read request for this specific server
		                ModbusRequestProcessor.addRequest(serverKey, () -> {
		                    BayResponse bayResponse = BayUtils.getModbusTcpClientManager()
		                            .modbusReadCoil(clusterServer, plcCoilAddress);

		                    Platform.runLater(() -> {
		                        if (!bayResponse.getStatus()) {
		                            ApplicationLauncher.logger.info("SendDataToBayTask: inputPortId: device not responded ");
		                            inputPortDetails.setInputActive(false);
		                            stateDesc.set(inputPortDetails.getOffStateDesc());
		                        } else {
		                            boolean coilState = bayResponse.isResponseBooleanData();
		                            inputPortDetails.setInputActive(coilState);
		                            stateDesc.set(coilState ? inputPortDetails.getOnStateDesc() : inputPortDetails.getOffStateDesc());
		                            ApplicationLauncher.logger.info("SendDataToBayTask: inputPortId: " + inputPortDetails.getPortId() + " : value : " + coilState);
		                        }

		                        inputPortDetails.setStateDescription(stateDesc.get());
		                        ref_tbViewInputPortData.refresh();
		                    });
		                });

		            } else {
		                Platform.runLater(() -> {
		                    inputPortDetails.setInputActive(false);
		                    stateDesc.set(inputPortDetails.getOffStateDesc());
		                    inputPortDetails.setStateDescription(stateDesc.get());
		                    ref_tbViewInputPortData.refresh();

		                    ApplicationLauncher.logger.info("SendDataToBayTask: invalid inputPortId: " + inputPortDetails.getPortId());
		                    ApplicationLauncher.InformUser(
		                            "Error-2010",
		                            "Kindly check key : " + inputPortDetails.getPortId() + " on conveyor config file on <inputPort> section",
		                            AlertType.ERROR
		                    );
		                });
		            }
		        });
				//String ipAddress = ref_txtClusterIpAddress.getText();
				//String ipPort = ref_txtClusterPortNo.getText();
				/*String stateDesc = "";
				//ClusterServer clusterServer = new ClusterServer(ipAddress,ipPort ,getPresentClusterId());
				for(int i =0; i< ref_tbViewInputPortData.getItems().size(); i++) {
					stateDesc = "";
					bayResponse = new BayResponse();
					plcCoilAddress = -1;
					InputPort inputPortDetails =  ref_tbViewInputPortData.getItems().get(i);
					if(inputPortDetails.isReadBay()) {
						String inputPortId = inputPortDetails.getPortId();	
						
						inputPortId = inputPortId.replaceAll("[^0-9.]", "");
						ApplicationLauncher.logger.info("SendDataToBayTask: inputPortId: "+inputPortId);
						
						if(GuiUtils.isNumber(inputPortId)) {
							plcCoilAddress = Integer.parseInt(inputPortId);
							
							bayResponse = BayUtils.getModbusTcpClientManager().modbusReadCoil(clusterServer, plcCoilAddress);
							
							if(!bayResponse.getStatus()) {
								ApplicationLauncher.logger.info("SendDataToBayTask: inputPortId: device not responded ");
								ref_tbViewInputPortData.getItems().get(i).setInputActive(false);
								stateDesc = ref_tbViewInputPortData.getItems().get(i).getOffStateDesc();
								overAllStatus = false;
							}else {
								//stateDesc = ref_tbViewInputPortData.getItems().get(i).getOffStateDesc();
								ref_tbViewInputPortData.getItems().get(i).setInputActive(bayResponse.isResponseBooleanData());
								ApplicationLauncher.logger.info("SendDataToBayTask: inputPortId:  " + inputPortDetails.getPortId() + " : value : "  + bayResponse.isResponseBooleanData());
								if(bayResponse.isResponseBooleanData()) {
									stateDesc = ref_tbViewInputPortData.getItems().get(i).getOnStateDesc();
								}else {
									stateDesc = ref_tbViewInputPortData.getItems().get(i).getOffStateDesc();
								}
							}
						}else {
							ref_tbViewInputPortData.getItems().get(i).setInputActive(false);
							stateDesc = ref_tbViewInputPortData.getItems().get(i).getOffStateDesc();
							ApplicationLauncher.logger.info("SendDataToBayTask: invalid inputPortId: "+inputPortDetails.getPortId());
							overAllStatus = false;
							
							ApplicationLauncher.InformUser("Error-2010","Kindly check key : "+inputPortDetails.getPortId() +" on conveyor config file on <inputPort> section",AlertType.ERROR);
						}
						

						ref_tbViewInputPortData.getItems().get(i).setStateDescription(stateDesc);
						ref_tbViewInputPortData.refresh();
					}
				}*/
			}

			sendCommDataTaskTimer.cancel();
			boolean overAllStatus1= overAllStatus;
			Platform.runLater(()->{
				ref_btnSendDataToBay.setDisable(false);
				if(overAllStatus1) {
					
					ref_txtComStatus.setText("WR/RD Success");
					
				}else {
					ref_txtComStatus.setText("WR/RD Failed");
				}
				
			});
			
			
			
			
			if(ref_chkBxReadEnabledSample.isSelected()) {
				int holdingReadAddress = Integer.parseInt(ref_txtReadAddressSample.getText());
				
				BayResponse bayResponse2 = BayUtils.getModbusTcpClientManager().modbusReadHoldingRegister(clusterServer, holdingReadAddress);
				//	bayResponse2 = 	BayUtils.getModbusTcpClientManager().getModbusTcpClient().modbusTcpSendReadHoldingRegistersCmd(holdingReadAddress);
				
				if(!bayResponse2.getStatus()) {
					ApplicationLauncher.logger.info("SendDataToBayTask: ReadHoldingRegister: device not responded ");
					//ref_tbViewInputPortData.getItems().get(i).setInputActive(false);
					//overAllStatus = false;
					Platform.runLater(()->{
						ref_txtReadStatusSample.setText("Failed");
					});
				}else {
					//stateDesc = ref_tbViewInputPortData.getItems().get(i).getOffStateDesc();
					//ref_tbViewInputPortData.getItems().get(i).setInputActive(bayResponse.isResponseBooleanData());
					Platform.runLater(()->{
						ref_txtReadStatusSample.setText("Success");
						ref_txtReadValueSample.setText(bayResponse2.getResponseData());
					});
				}
				
			}
			
			
			if(ref_chkBxWriteEnabledSample.isSelected()) {
				int holdingWriteAddress = Integer.parseInt(ref_txtWriteAddressSample.getText());
				int holdingWriteValue = Integer.parseInt(ref_txtWriteValueSample.getText());
				
				BayResponse bayResponse3 = BayUtils.getModbusTcpClientManager().modbusWriteHoldingRegister(clusterServer, holdingWriteAddress, holdingWriteValue);
						//bayResponse3 =  BayUtils.getModbusTcpClientManager().getModbusTcpClient().modbusTcpSendWriteHoldingRegistersCmd(holdingWriteAddress,holdingWriteValue);
				
				if(!bayResponse3.getStatus()) {
					ApplicationLauncher.logger.info("SendDataToBayTask: WriteHoldingRegister: device not responded ");
					//ref_tbViewInputPortData.getItems().get(i).setInputActive(false);
					//overAllStatus = false;
					Platform.runLater(()->{
						ref_txtWriteStatusSample.setText("Failed");
					});
				}else {
					//stateDesc = ref_tbViewInputPortData.getItems().get(i).getOffStateDesc();
					//ref_tbViewInputPortData.getItems().get(i).setInputActive(bayResponse.isResponseBooleanData());
					Platform.runLater(()->{
						ref_txtWriteStatusSample.setText("Success");
						//ref_txtWriteValueSample.setText(bayResponse3.getResponseData());
					});
				}
				
			}
		}
	}	
	
	

	public RestApiJsonBodyResponse getInputDataFromBay(ClusterServer clusterServer,String deviceId, String bayId,String inputPortId) {
		ApplicationLauncher.logger.info("getInputDataFromBay -plc-client: Entry-failed-debug");
		String clusterId = clusterServer.getClusterId(); 
		boolean status = false;
		ConveyorClientManager cluster1ClientManager = ConveyorClientManager.getInstance(clusterId);
		//cluster1ClientManager.getBayData(clusterServer,deviceId, bayId,  inputPortId);
		String dummyOutputValue= "";
		boolean setOutput = false;
		cluster1ClientManager.messageBayData(setOutput,clusterServer,deviceId, bayId,  inputPortId,dummyOutputValue);
		
		ApplicationHomeController.update_left_status("Awaiting Device Response",ConstantApp.LEFT_STATUS_DEBUG);
		//Sleep(8000);
		RestApiJsonBodyResponse clusterResponseData = new RestApiJsonBodyResponse();
		//clusterResponseData.setStatuscode(statuscode);
		//HashMap<Boolean,RestApiClusterResponse> returnData = new HashMap<Boolean,RestApiClusterResponse>();
		boolean isResponseReceived = false;
		if(ProcalFeatureEnable.CONVEYOR_REST_CLIENT_ENABLED){
			//cluster1ClientManager.getRestConvClient().WaitForServerResponse(8);
			isResponseReceived = cluster1ClientManager.getRestConvClient().isResponseReceived();
		}else{
			cluster1ClientManager.getAsyncConvClient().WaitForServerResponse(8);
			isResponseReceived = cluster1ClientManager.getAsyncConvClient().isResponseReceived();
		}
		if(isResponseReceived){ //validate for server access
			ApplicationHomeController.update_left_status("Device Connected",ConstantApp.LEFT_STATUS_DEBUG);

			//RestApiClusterResponse clusterResponseData = new RestApiClusterResponse();
			if(ProcalFeatureEnable.CONVEYOR_REST_CLIENT_ENABLED){
				clusterResponseData = cluster1ClientManager.getRestConvClient().getRestApiClusterResponseBodyData();
			}else{
				clusterResponseData = cluster1ClientManager.getAsyncConvClient().getRestApiClusterResponseBodyData();
			}
			ApplicationLauncher.logger.info("getInputDataFromBay: "+inputPortId+" : getDevice Data: "+ clusterResponseData.getJsonBodyResponse().get(inputPortId));

			/*ApplicationLauncher.logger.info("getInputDataFromBay: "+inputPortId+" : getDevice Data: "+ clusterResponseData.getDevice());
			ApplicationLauncher.logger.info("getInputDataFromBay: "+inputPortId+" : getStatus Data: "+ clusterResponseData.getStatus());
			ApplicationLauncher.logger.info("getInputDataFromBay: "+inputPortId+" : getStatus getOpGreen: "+ clusterResponseData.getOpGreen());
			ApplicationLauncher.logger.info("getInputDataFromBay: "+inputPortId+" : getStatus getOpYellow: "+ clusterResponseData.getOpYellow());
			ApplicationLauncher.logger.info("getInputDataFromBay: "+inputPortId+" : getStatus getOpRed: "+ clusterResponseData.getOpRed());*/


			//String responseData = cluster1ClientManager.getAsyncConvClient().getResponseData();

			/*Platform.runLater(()->{
				ref_txtAreaResponseDisplay.setText(responseData);
			});*/
			/*			if(ref_txtAreaResponseDisplay.getText().isEmpty()) {
				ref_txtAreaResponseDisplay.setText(responseData);
			}else {
				ref_txtAreaResponseDisplay.setText(ref_txtAreaResponseDisplay.getText()+"\n"+responseData);
			}*/
			status = true;

			//ApplicationHomeController.EnableScanDeviceButton();
		}else{

			//ScanDeviceController.ScanDeviceCompletedPostProcess();
			//ApplicationHomeController.EnableScanDeviceButton();
			//ApplicationHomeController.DisableTestRunButton();
			//ApplicationHomeController.EnableLeftMenuButtonsForTestRun();
/*			Platform.runLater(()->{
				ref_txtAreaResponseDisplay.setText(ref_txtAreaResponseDisplay.getText()+"\n"+inputPortId+"-no response");
			});*/
			ApplicationHomeController.update_left_status("Device Connection Failed "+inputPortId+" :",ConstantApp.LEFT_STATUS_DEBUG);
		}
		return clusterResponseData;
	}

	
	public boolean setOutputDataToBay(ClusterServer clusterServer,String deviceId, String bayId,String outputPortId, String outputStatus) {
		ApplicationLauncher.logger.info("setOutputDataToBay-plc client: Entry-failed-debug");
		String clusterId = clusterServer.getClusterId(); 
		boolean status = false;
		ConveyorClientManager cluster1ClientManager = ConveyorClientManager.getInstance(clusterId);//new ConveyorClientManager();
		//cluster1ClientManager.setBayData(clusterServer,deviceId, bayId,  outputPortId, outputStatus);
		
		//String dummyOutputValue= "";
		boolean setOutput = true;
		cluster1ClientManager.messageBayData(setOutput,clusterServer,deviceId, bayId,  outputPortId,outputStatus);
		ApplicationHomeController.update_left_status("Awaiting Device Response",ConstantApp.LEFT_STATUS_DEBUG);
		//Sleep(8000);
		//cluster1ClientManager.getAsyncConvClient().WaitForServerResponse(8);
		boolean isResponseReceived = false;
		if(ProcalFeatureEnable.CONVEYOR_REST_CLIENT_ENABLED){
			//cluster1ClientManager.getRestConvClient().WaitForServerResponse(8);
			isResponseReceived = cluster1ClientManager.getRestConvClient().isResponseReceived();
		}else{
			cluster1ClientManager.getAsyncConvClient().WaitForServerResponse(8);
			isResponseReceived = cluster1ClientManager.getAsyncConvClient().isResponseReceived();
		}
		if(isResponseReceived){//cluster1ClientManager.getAsyncConvClient().isResponseReceived()){ //validate for server access
			ApplicationHomeController.update_left_status("Device Connected",ConstantApp.LEFT_STATUS_DEBUG);

			RestApiClusterResponse clusterResponseData = new RestApiClusterResponse();
			//clusterResponseData = cluster1ClientManager.getAsyncConvClient().getRestApiClusterResponseData();
			if(ProcalFeatureEnable.CONVEYOR_REST_CLIENT_ENABLED){
				clusterResponseData = cluster1ClientManager.getRestConvClient().getRestApiClusterResponseData();
			}else{
				clusterResponseData = cluster1ClientManager.getAsyncConvClient().getRestApiClusterResponseData();
			}
			ApplicationLauncher.logger.info("AsyncConveyorClient: "+outputPortId+" : getDevice Data: "+ clusterResponseData.getDevice());
			ApplicationLauncher.logger.info("AsyncConveyorClient: "+outputPortId+" : getStatus Data: "+ clusterResponseData.getStatus());


			//String responseData = cluster1ClientManager.getAsyncConvClient().getResponseData();
			/*Platform.runLater(()->{
				ref_txtAreaResponseDisplay.setText(responseData);
			});*/
			/*if(ref_txtAreaResponseDisplay.getText().isEmpty()) {
				ref_txtAreaResponseDisplay.setText(responseData);
			}else {
				ref_txtAreaResponseDisplay.setText(ref_txtAreaResponseDisplay.getText()+"\n"+responseData);
			}*/
			status = true;

			//ApplicationHomeController.EnableScanDeviceButton();
		}else{

			//ScanDeviceController.ScanDeviceCompletedPostProcess();
			//ApplicationHomeController.EnableScanDeviceButton();
			//ApplicationHomeController.DisableTestRunButton();
			//ApplicationHomeController.EnableLeftMenuButtonsForTestRun();
/*			Platform.runLater(()->{
				ref_txtAreaResponseDisplay.setText(ref_txtAreaResponseDisplay.getText()+"\n"+outputPortId+"-no response");
			});*/
			ApplicationHomeController.update_left_status("Device Connection Failed "+outputPortId+" :",ConstantApp.LEFT_STATUS_DEBUG);
			//status = null;
		}
		return status;
	}
	

	@FXML
	public void cmbBxClusterSelectionOnChange(){
		ApplicationLauncher.logger.debug("cmbBxClusterSelectionOnChange: Entry");
		clusterSelectionOnChangeTimer = new Timer();
		clusterSelectionOnChangeTimer.schedule(new ClusterSelectionOnChangeTask(),10);
	}

	class ClusterSelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String)ref_cmbBxClusterSelection.getSelectionModel().getSelectedItem();
				if(getClusterBayNameListMap().size()>0){
					ref_tbViewOutputPortData.getItems().clear();
					ref_tbViewInputPortData.getItems().clear();
					ref_cmbBxBaySelection.getItems().clear();
					ref_txtClusterIpAddress.setText("");
					ref_txtClusterPortNo.setText("");
					if(getClusterBayNameListMap().containsKey(selectedClusterName)){
						ref_cmbBxBaySelection.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));
					}
					ref_cmbBxBaySelection.getSelectionModel().select(0);

				}
				ref_txtComStatus.setText("");
			});
			clusterSelectionOnChangeTimer.cancel();
		}
	}

	//BAY COMBO BOX CHANGE

	@FXML
	public void cmbBxBaySelectionOnChange(){
		ApplicationLauncher.logger.debug("cmbBxBaySelectionOnChange: Entry");
		baySelectionOnChangeTimer = new Timer();
		baySelectionOnChangeTimer.schedule(new BaySelectionOnChangeTask(),10);
	}


	class BaySelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(()->{

				String selectedClusterName = (String)ref_cmbBxClusterSelection.getSelectionModel().getSelectedItem();
				if(getClusterBayNameListMap().size()>0){
					ref_tbViewOutputPortData.getItems().clear();
					ref_tbViewInputPortData.getItems().clear();
					//ref_txtClusterIpAddress.setText("");
					//ref_txtClusterPortNo.setText("");
					//if(getClusterBayNameListMap().containsKey(selectedClusterName)){
					//	ref_cmbBxBaySelection.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));
					//}
					//ref_cmbBxBaySelection.getSelectionModel().select(0);

				}
			});
			baySelectionOnChangeTimer.cancel();




		}
	}




	@FXML
	public void btnLoadOnClick(){

		ApplicationLauncher.logger.debug("btnLoadOnClick: Entry");
		loadOnClickTimer = new Timer();
		loadOnClickTimer.schedule(new LoadOnClickTask(),10);

	}


	class LoadOnClickTask extends TimerTask {
		public void run() {
			Platform.runLater(()->{
				ref_tbViewOutputPortData.getItems().clear();
				ref_tbViewInputPortData.getItems().clear();
				ref_txtComStatus.setText("");
				String clusterName = (ref_cmbBxClusterSelection.getSelectionModel().getSelectedItem().toString());
				String bayName = (ref_cmbBxBaySelection.getSelectionModel().getSelectedItem().toString());

				String clusterId = getClusterNameIdListMap().get(clusterName);
				//ApplicationLauncher.logger.debug("loadDataFromConfig : getClusterBayNameIdMap().get(clusterName)-2 :"+ getClusterBayNameIdMap().get(clusterName));
				//String bayId = "11";//getClusterBayNameIdMap().get(clusterName).get(bayName);

				ApplicationLauncher.logger.debug("LoadOnClickTask : clusterId :"+ clusterId);


				//String clusterIpAddress = "";
				//String clusterPortNo = "";

				Optional<ClusterDetail> clusterOpt = 		getBayConfigModel().getTerminal().stream()
						.filter(e1->e1.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID))
						.flatMap(terminal -> terminal.getClusterDetails().stream())
						.filter(e2->e2.getClusterId().equals(clusterId))
						.findFirst();

				if (clusterOpt.isPresent()) {
					ClusterDetail clusterDetail = clusterOpt.get();
					ApplicationLauncher.logger.info("LoadOnClickTask : clusterDetail().getClusterIpAddress(): " + clusterDetail.getClusterIpAddress());
					ApplicationLauncher.logger.info("LoadOnClickTask : clusterDetail().getClusterPortId(): " + clusterDetail.getClusterPortId());
					//ApplicationLauncher.logger.info("LoadOnClickTask : already connected status: " + BayUtils.getModbusTcpClientManager().getModbusTcpServerConnectedMap().get(clusterDetail.getClusterIpAddress()));
					
					//ref_txtClusterIpAddress.setText(clusterDetail.getClusterIpAddress());
					//ref_txtClusterPortNo.setText(clusterDetail.getClusterPortId());
					
/*					if(BayUtils.getModbusTcpClientManager().getModbusTcpServerConnectedMap().containsKey(clusterDetail.getClusterIpAddress())){
						ApplicationLauncher.logger.info("LoadOnClickTask : clusterDetail().getClusterIpAddress(): " + clusterDetail.getClusterIpAddress());
						ApplicationLauncher.logger.info("LoadOnClickTask : already connected status: " + BayUtils.getModbusTcpClientManager().getModbusTcpServerConnectedMap().get(clusterDetail.getClusterIpAddress()));
						
						if(BayUtils.getModbusTcpClientManager().getModbusTcpServerConnectedMap().get(clusterDetail.getClusterIpAddress())) {
							ref_txtComStatus.setText("Connected");
						}else {
							ref_txtComStatus.setText("Disconnected");
						}
					}else {
						ref_txtComStatus.setText("");
					}*/
					
					//String ipAddress = clusterDetail.getClusterIpAddress();
	               // String portNo = clusterDetail.getClusterPortId();
	                String serverKey = clusterDetail.getClusterIpAddress() + ":" + clusterDetail.getClusterPortId();  // Unique identifier for IP + Port
	                //String clusterIpAddress = clusterDetail.getClusterIpAddress();
	                //int clusterPortNo = Integer.parseInt(clusterDetail.getClusterPortId());

	                Platform.runLater(() -> ref_txtClusterIpAddress.setText(clusterDetail.getClusterIpAddress()));
	                Platform.runLater(() -> ref_txtClusterPortNo.setText(clusterDetail.getClusterPortId()));

	                ApplicationLauncher.logger.info("LoadOnClickTask: getModbusTcpServerStatus : " + BayUtils.getModbusTcpClientManager()
                    .getModbusTcpServerStatus());
	                // Use modbusTcpServerStatus to check connection status
	                boolean isConnected = BayUtils.getModbusTcpClientManager()
	                        .getModbusTcpServerStatus().getOrDefault(serverKey, false);

	                if (isConnected) {
	                	Platform.runLater(() -> ref_txtComStatus.setText("Connected"));
	                	ref_txtComStatus.setStyle("-fx-text-fill: green;");
	                    ApplicationLauncher.logger.info("LoadOnClickTask: Connected to " + clusterDetail.getClusterIpAddress());
	                } else {
	                	Platform.runLater(() -> ref_txtComStatus.setText("Disconnected"));
	                	ref_txtComStatus.setStyle("-fx-text-fill: red;");
	                    ApplicationLauncher.logger.info("LoadOnClickTask: Not connected to " + clusterDetail.getClusterIpAddress());
	                }

				}




				//Optional<OutputPort> outputPortOpt = 

				Optional<Bay> bayOpt = 		getBayConfigModel().getTerminal().stream()
						.filter(e1->e1.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID))
						.flatMap(terminal -> terminal.getClusterDetails().stream())
						.filter(e2->e2.getClusterId().equals(clusterId))
						.flatMap(e3 -> e3.getBay().stream())
						.filter(e4->e4.getBayName().equals(bayName))
						.findFirst();

				if (bayOpt.isPresent()) {
					Bay bayDetails = bayOpt.get();
					String bayId = bayDetails.getBayId();
					ApplicationLauncher.logger.debug("LoadOnClickTask : bayId :"+ bayId);

					ArrayList<OutputPort> outputPortList = (ArrayList<OutputPort>) getBayConfigModel().getTerminal().stream()
							.flatMap(terminal -> terminal.getOutputPort().stream())
							.filter(p -> clusterId.equals(p.getClusterId()))
							.filter(p -> bayId.equals(p.getBayId()))
							.map(OutputPort::clone)
							.collect(Collectors.toList());
					
					//ArrayList<OutputPort> outputPortListClone = (ArrayList<OutputPort>) outputPortList.clone();

					if (outputPortList.size()>0) {
						int serialNo = 1;
						for(OutputPort eachOutputPort : outputPortList) {
							eachOutputPort.setSerialNo(String.valueOf(serialNo));
							if(eachOutputPort.isOutputActive()) {
								eachOutputPort.setStateDescription(eachOutputPort.getOnStateDesc());
								//ApplicationLauncher.logger.debug("OutputPortActiveCheckBoxValueFactory : getStateDescription-1 : " + rowData.getStateDescription());
							}else {
								eachOutputPort.setStateDescription(eachOutputPort.getOffStateDesc());
								//ApplicationLauncher.logger.debug("OutputPortActiveCheckBoxValueFactory : getStateDescription-12: " + rowData.getStateDescription());

							}
							//eachOutputPort.setStateDescription("DummyData");
							serialNo++; 
						}
						ref_tbViewOutputPortData.getItems().addAll(outputPortList); 

					}

					ArrayList<InputPort> inputPortList = (ArrayList<InputPort>) getBayConfigModel().getTerminal().stream()
							.flatMap(terminal -> terminal.getInputPort().stream())
							.filter(p -> clusterId.equals(p.getClusterId()))
							.filter(p -> bayId.equals(p.getBayId()))
							.map(InputPort::clone)
							.collect(Collectors.toList());

					if (inputPortList.size()>0) {
						int serialNo = 1;
						for(InputPort eachInputPort : inputPortList) {
							eachInputPort.setSerialNo(String.valueOf(serialNo));
							if(eachInputPort.isInputActive()) {
								eachInputPort.setStateDescription(eachInputPort.getOnStateDesc());
								//ApplicationLauncher.logger.debug("OutputPortActiveCheckBoxValueFactory : getStateDescription-1 : " + rowData.getStateDescription());
							}else {
								eachInputPort.setStateDescription(eachInputPort.getOffStateDesc());
								//ApplicationLauncher.logger.debug("OutputPortActiveCheckBoxValueFactory : getStateDescription-12: " + rowData.getStateDescription());

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
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep2 :InterruptedException:"+ e.getMessage());
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
		PlcClientBayTestController.bayConfigModel = bayConfigModel;
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
	
	
	@FXML
	public void modbusConnectTrigger(){
		ApplicationLauncher.logger.info("modbusConnectTrigger : Entry");
		connectTimer = new Timer();
		connectTimer.schedule(new modbusConnectTask(), 100);
	}
	
	
	class modbusConnectTask extends TimerTask{


		@Override
		public void run() {
			Platform.runLater(() -> {
			//ref_tbViewOutputPortData.getItems().clear();
		    //ref_tbViewInputPortData.getItems().clear();
			ref_btnConnect.setDisable(true); // Disable connect button
	       
		    ref_txtComStatus.setText("");

		    String clusterName = ref_cmbBxClusterSelection.getSelectionModel().getSelectedItem().toString();
		    String bayName = ref_cmbBxBaySelection.getSelectionModel().getSelectedItem().toString();
		    String clusterId = getClusterNameIdListMap().get(clusterName);

		    ApplicationLauncher.logger.debug("LoadOnClickTask : clusterId :" + clusterId);

		    Optional<ClusterDetail> clusterOpt = getBayConfigModel().getTerminal().stream()
		            .filter(e1 -> e1.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID))
		            .flatMap(terminal -> terminal.getClusterDetails().stream())
		            .filter(e2 -> e2.getClusterId().equals(clusterId))
		            .findFirst();

		    if (clusterOpt.isPresent()) {
		        ClusterDetail clusterDetail = clusterOpt.get();
		        String clusterIpAddress = clusterDetail.getClusterIpAddress();
		        int clusterPortNo = Integer.parseInt(clusterDetail.getClusterPortId());
		        String key = clusterIpAddress + ":" + clusterPortNo;
		        ref_txtClusterIpAddress.setText(clusterIpAddress);
		        ref_txtClusterPortNo.setText(clusterDetail.getClusterPortId());

		        // Use modbusTcpServerStatus to check connection status
		        boolean isConnected = BayUtils.getModbusTcpClientManager()
		                .getModbusTcpServerStatus().getOrDefault(key, false);

		        if (isConnected) {
                    ref_txtComStatus.setText("Connected");
                    ref_txtComStatus.setStyle("-fx-text-fill: green;");
                    ApplicationLauncher.logger.info("modbusConnectTask: Already connected to " + clusterIpAddress);
                    ref_btnConnect.setDisable(false);
                } else {
                    // Establish the connection
                    /*ApplicationLauncher.logger.info("modbusConnectTask: Establishing connection to " + key);
                    BayResponse response = BayUtils.getModbusTcpClientManager()
                            .modbusConnect(clusterIpAddress, clusterPortNo);

                    if (response.getStatus()) {
                        ref_txtComStatus.setText("Connected");
                        ApplicationLauncher.logger.info("modbusConnectTask: Connection successful - " + response.getResponseData());
                    } else {
                        ref_txtComStatus.setText("Connection Failed");
                        ApplicationLauncher.logger.error("modbusConnectTask: Connection failed - " + response.getResponseData());
                    }*/
                	
                	ApplicationLauncher.logger.info("modbusConnectTask: Establishing connection to " + key);
                	 ref_btnConnect.setCursor(Cursor.WAIT); // Show busy cursor
                    // Start a new thread for connection to avoid UI freeze
                    new Thread(() -> {
                        BayResponse response = BayUtils.getModbusTcpClientManager()
                                .modbusConnect(clusterIpAddress, clusterPortNo);

                        Platform.runLater(() -> {
                            if (response.getStatus()) {
                                ref_txtComStatus.setText("Connected");
                                ref_txtComStatus.setStyle("-fx-text-fill: green;");
                                ApplicationLauncher.logger.info("modbusConnectTask: Connection successful - " + response.getResponseData());
                            } else {
                                ref_txtComStatus.setText("Connection Failed");
                                ref_txtComStatus.setStyle("-fx-text-fill: red;");
                                ApplicationLauncher.logger.error("modbusConnectTask: Connection failed - " + response.getResponseData());
                            }

                            // Reset cursor and button state
                            ref_btnConnect.setDisable(false);
                            ref_btnConnect.setCursor(Cursor.DEFAULT);
                        });
                    }).start();
                }
		    }else{
		    	ref_btnConnect.setDisable(false);
		    }
			
			});
			
			connectTimer.cancel();
			//}

		}
	}
	
	@FXML
	public void modbusDisconnectTrigger(){
		ApplicationLauncher.logger.info("modbusDisconnectTrigger : Entry");
		disconnectTimer = new Timer();
		disconnectTimer.schedule(new modbusDisconnectTask(), 100);
	}
	
	


	class modbusDisconnectTask extends TimerTask{


		@Override
		public void run() {
			//Platform.runLater(() -> {
		       try {
		            // Get IP and Port from UI fields
		            String ipAddress = ref_txtClusterIpAddress.getText();
		            int port = Integer.parseInt(ref_txtClusterPortNo.getText());
		            String key = ipAddress + ":" + port;

		            ApplicationLauncher.logger.info("modbusDisconnectTask : Attempting to disconnect from " + key);

		            // Check if the connection exists
		            if (!BayUtils.getModbusTcpClientManager().getModbusTcpServerStatus().getOrDefault(key, false)) {
		                ApplicationLauncher.logger.warn("modbusDisconnectTask : No active connection found for " + key);
		                Platform.runLater(() -> ref_txtComStatus.setText("Not Connected"));
		                disconnectTimer.cancel();
		                return;
		            }

		            // Perform disconnect
		            BayUtils.getModbusTcpClientManager().modbusDisconnect(ipAddress, port);

		            // Update UI
		            Platform.runLater(() -> ref_txtComStatus.setText("Disconnected"));

		            ApplicationLauncher.logger.info("modbusDisconnectTask : Successfully disconnected from " + key);
		        } catch (NumberFormatException e) {
		            ApplicationLauncher.logger.error("modbusDisconnectTask : Invalid port number format");
		            Platform.runLater(() -> ref_txtComStatus.setText("Invalid Port"));
		        } catch (Exception e) {
		            ApplicationLauncher.logger.error("modbusDisconnectTask : Disconnection error: " + e.getMessage());
		            Platform.runLater(() -> ref_txtComStatus.setText("Disconnection Error"));
		        }
/*				status=stop_confirmation();
				if(status){
					StopOnClickSuccess();
				}*/
				disconnectTimer.cancel();
			//});
		}

	}

/*

	public ModbusTcpClient getModbusTcpClient() {
		return modbusTcpClient;
	}

	public void setModbusTcpClient(ModbusTcpClient modbusTcpClient) {
		this.modbusTcpClient = modbusTcpClient;
	}
	
	public Map<String, Boolean> getModbusTcpServerConnectedMap() {
		return modbusTcpServerConnectedMap;
	}

	public void setModbusTcpServerConnectedMap(Map<String, Boolean> modbusTcpServerConnectionStatusMap) {
		this.modbusTcpServerConnectedMap = modbusTcpServerConnectionStatusMap;
	}

	public Map<String, ModbusTcpClient> getModbusTcpServerObjectMap() {
		return modbusTcpServerObjectMap;
	}

	public void setModbusTcpServerObjectMap(Map<String, ModbusTcpClient> modbusTcpServerObjectMap) {
		this.modbusTcpServerObjectMap = modbusTcpServerObjectMap;
	}*/




}