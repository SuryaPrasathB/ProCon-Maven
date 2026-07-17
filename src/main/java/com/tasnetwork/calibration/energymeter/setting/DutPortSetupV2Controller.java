package com.tasnetwork.calibration.energymeter.setting;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.controlsfx.control.CheckComboBox;

import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.configloader.Bay;
import com.tasnetwork.calibration.conveyor.bay.configloader.ClusterDetail;
import com.tasnetwork.calibration.conveyor.bay.configloader.Terminal;
import com.tasnetwork.calibration.conveyor.bay.configloader.TerminalBayConfigModel;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.constant.ConstantDutDevSys;
import com.tasnetwork.calibration.conveyor.constant.ConstantQrScanner;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.serial.director.DutDirector;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmDut;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.spring.orm.model.BayDeviceConfig;
import com.tasnetwork.spring.orm.model.DeviceSetting;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

public class DutPortSetupV2Controller implements Initializable {

	Timer validateTimer;
	
	String deviceTypeKeyPrefix = ConstantDutDevSys.DUT_EM_TYPE_DEVSYS_PREFIX ;
	String deviceModelName = ConstantDutDevSys.DUT_EM_MODEL;
	String deviceType = ConstantConveyor.DEVICE_TYPE_DUT;
	String deviceDefaultBaudRate = String.valueOf(ConstantDutDevSys.DUT_DEFAULT_BAUD_RATE);

	private static TerminalBayConfigModel  bayConfigModel = ConveyorDataManager.getTerminalBayConfig();
/*	private static Map<String,ArrayList<String>> clusterBayNameListMap = new HashMap<String,ArrayList<String>>();
	private static Map<String,String> clusterNameIdListMap = new HashMap<String,String>();
	private static Map<String,String> clusterBayNameIdMap = new HashMap<String,String>();*/
	private static Map<String,ArrayList<String>> clusterBayNamePositionListMap = new HashMap<String,ArrayList<String>>();
	private static Map<String,String> clusterBayPositionNoCnameMap = new LinkedHashMap<String,String>();
//	private static Map<String,String> clusterBayPositionNoDeviceIdMap = new LinkedHashMap<String,String>();

	@FXML
	private CheckComboBox<String> chkCmbBxModelType;
	static private CheckComboBox<String> ref_chkCmbBxModelType;
	
	@FXML
	private CheckComboBox<String> chkCmbBxDeviceType;
	static private CheckComboBox<String> ref_chkCmbBxDeviceType;
	
	@FXML
	private TableColumn<DeviceSetting, String> colDsCname;

	@FXML
	private TableColumn<DeviceSetting, Integer> colDsSerialNo;

	@FXML
	private TableColumn<DeviceSetting, String> colDsResponseData;

	@FXML
	private TableColumn<DeviceSetting, String> colDsStatus;


	@FXML
	private TableColumn colDsPositionNo;

	@FXML
	private TableColumn colDsBayName;

	@FXML
	private TableColumn colDsClusterName;


	@FXML
	private TableColumn<DeviceSetting, String> colDsDeviceId;

	@FXML
	private TableColumn colDsBaudRate;

	@FXML
	private TableColumn colDsPortName;


	@FXML
	private TableColumn colDsValidate;

	@FXML
	private TableView<DeviceSetting> tvDeviceSetting;
	
	private static TableView<DeviceSetting> ref_tvDeviceSetting;
	
	private AtomicInteger serialNoAtomic = new AtomicInteger(1);


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		refAssignment();
		//loadDataFromConfig();		
		guiInit();
		loadDataFromDb();
	}
	
	public void refAssignment() {
		// TODO Auto-generated method stub
		ref_tvDeviceSetting = tvDeviceSetting;
		ref_chkCmbBxModelType = chkCmbBxModelType;
		ref_chkCmbBxDeviceType = chkCmbBxDeviceType;
	}

	public void loadDataFromConfig() {
		// TODO Auto-generated method stub

		ApplicationLauncher.logger.debug("loadDataFromConfig-Q2: Entry");
		setClusterBayPositionNoCnameMap(BayUtils.getDutClusterBayPositionNoCnameMap());
		setClusterBayNamePositionListMap(BayUtils.getDutClusterBayNamePositionListMap());
	}
/*		//ref_cmbBxDut1ClusterId.getItems().clear();

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
						ArrayList<String> positionNoList = new ArrayList<String>();
						for(QrScanner eachQrDevice: eachTerminal.getQrScanner()){
							if( (eachQrDevice.getClusterId().equals(clusterId)) && (eachQrDevice.getBayId().equals(bayId)) ){
								//getClusterBayNamePositionListMap().put
								positionNoList.add(eachQrDevice.getPositionId());
								getClusterBayPositionNoCnameMap().put(
										eachClusterDetail.getName()+"_"+eachBay.getBayName()+"_"+eachQrDevice.getPositionId(), 
										eachQrDevice.getPortName());
								getClusterBayPositionNoDeviceIdMap().put(
										eachClusterDetail.getName()+"_"+eachBay.getBayName()+"_"+eachQrDevice.getPositionId(), 
										eachQrDevice.getDeviceId());

							}

						}
						if(positionNoList.size()>0) {
							getClusterBayNamePositionListMap().put(eachClusterDetail.getName()+"_"+eachBay.getBayName(),positionNoList);
						}


					}

				}

			}
		}





	}
*/
	public void loadDataFromDb() {
		// TODO Auto-generated method stub
		List<DeviceSetting> deviceSettingList = MySqlServiceManager.getDeviceSettingService().findByDeviceType(getDeviceType());
		//deviceSettingList = reOrderedSerialNo(deviceSettingList);
		
		ref_tvDeviceSetting.getItems().addAll(FXCollections.observableArrayList(deviceSettingList));
		reOrderedSerialNo();
		//FXCollections.observableArrayList(products)
	}
	
	public void reOrderedSerialNo() {
		//List<DeviceSetting> deviceSettingList = ref_tvDeviceSetting.getItems();
		getSerialNoAtomic().set(1);
		ref_tvDeviceSetting.getItems().stream().forEachOrdered(e->{
			e.setSerialNo(getSerialNoAtomic().getAndIncrement());
			//ApplicationLauncher.logger.debug("reOrderedSerialNo: getPositionNo : " + e.getPositionNo() + " -> getcName: " + e.getcName());
			
		});
		ref_tvDeviceSetting.refresh();
		//ref_tvDeviceSetting.getItems().clear();
		//ref_tvDeviceSetting.getItems().addAll(deviceSettingList);
	}

	public void guiInit() {
		// TODO Auto-generated method stub
		
		ref_chkCmbBxModelType.getItems().clear();
		ref_chkCmbBxModelType.getItems().add(deviceModelName);
		ref_chkCmbBxModelType.getCheckModel().checkAll();
		
		
		ref_chkCmbBxDeviceType.getItems().clear();
		ref_chkCmbBxDeviceType.getItems().add(deviceType);
		ref_chkCmbBxDeviceType.getCheckModel().checkAll();
		
		ref_tvDeviceSetting.setEditable(true);
			    
		colDsSerialNo.setCellValueFactory(data -> data.getValue().getSerialNoProperty().asObject());		 
		colDsBaudRate.setCellValueFactory(new DeviceSettingBaudRateComboBoxValueFactory());	
		colDsPortName.setCellValueFactory(new DeviceSettingPortNameComboBoxValueFactory());	
		colDsResponseData.setCellValueFactory(data -> data.getValue().getSerialResponseDataProperty());		    
		

		colDsClusterName.setCellValueFactory(new DeviceSettingClusterNameComboBoxValueFactory());

		colDsBayName.setCellValueFactory(new DeviceSettingBayNameComboBoxValueFactory());	



		colDsPositionNo.setCellValueFactory(new DeviceSettingPositionNoComboBoxValueFactory());	


		colDsDeviceId.setCellValueFactory(data -> data.getValue().getDeviceIdProperty());	

		//colDsReadData.setCellValueFactory(data -> data.getValue().getSerialResponseDataProperty());
		colDsResponseData.setCellValueFactory(data -> data.getValue().getSerialResponseDataProperty());	
		colDsResponseData.setCellFactory(column -> {
		    return new TableCell<DeviceSetting, String>() {
		        private final TextField textField = new TextField();

		        @Override
		        protected void updateItem(String item, boolean empty) {
		            super.updateItem(item, empty);
		            if (empty) {
		                setGraphic(null);
		                setText(null);
		            } else {
		                textField.setText(item);
		                textField.setEditable(false);  // Disable editing

		                setGraphic(textField);
		                setText(null);
		            }
		        }
		    };
		});
		
		colDsCname.setCellValueFactory(data -> data.getValue().getcNameProperty());
		colDsCname.setCellFactory(column -> {
		    return new TableCell<DeviceSetting, String>() {
		        private final TextField textField = new TextField();

		        @Override
		        protected void updateItem(String item, boolean empty) {
		            super.updateItem(item, empty);
		            if (empty) {
		                setGraphic(null);
		                setText(null);
		            } else {
		                textField.setText(item);
		                textField.setEditable(false);  // Disable editing

		                setGraphic(textField);
		                setText(null);
		            }
		        }
		    };
		});
		
		colDsStatus.setCellValueFactory(data -> data.getValue().getSerialStatusProperty());	
		colDsStatus.setCellFactory(column -> {
		    return new TableCell<DeviceSetting, String>() {
		        private final TextField textField = new TextField();

		        @Override
		        protected void updateItem(String item, boolean empty) {
		            super.updateItem(item, empty);
		            if (empty) {
		                setGraphic(null);
		                setText(null);
		            } else {
		                textField.setText(item);
		                textField.setEditable(false);  // Disable editing

		                setGraphic(textField);
		                setText(null);
		            }
		        }
		    };
		});
		//colDsReadData.setCellFactory(TextFieldTableCell.forTableColumn());
		//colDsReadData.setOnEditCommit(new EventHandler<CellEditEvent<DeviceSetting, String>>() {
		//	public void handle(CellEditEvent<DeviceSetting, String> t) {
		//ApplicationLauncher.logger.info("loadSerialNo: setSerialno: Entry");
		//DeviceSetting rowData = ((DeviceSetting) t.getTableView().getItems().get(t.getTablePosition().getRow()));

		//rowData.setSerialno(t.getNewValue());

		/*				String serial_no = t.getNewValue();
				if(!ProcalFeatureEnable.EXPORT_MODE_ENABLED){
					rowData.setSerialno(t.getNewValue());
				}else if(ProcalFeatureEnable.EXPORT_MODE_ENABLED){


					if((!serial_no.toUpperCase().contains(ConstantApp.EXPORT_MODE_ALIAS_NAME)) &&
							(!serial_no.toUpperCase().contains(ConstantApp.EXPORT_MODE_ALIAS_NAME.trim())) ){
						rowData.setSerialno(t.getNewValue());
					}else{
						ApplicationLauncher.logger.info("DeploymentManagerController : loadSerialNo: handle: serial_no:"+serial_no+". Modified data input not accepted prompt-ErrorCode - U001");
						ApplicationLauncher.InformUser("ErrorCode - U001", "Modified data input contains unaccepted value <"+ConstantApp.EXPORT_MODE_ALIAS_NAME+">  , kindly rephrase!!",AlertType.ERROR);

						devicesDataTable.refresh();
					}
				}*/

		//	}
		//});
		//colDsValidate.setCellValueFactory(new DeviceSettingValidateButtonFactory());	
		/*	    colDsValidate.setCellFactory(DeviceSettingValidateButtonFactory.<DeviceSetting>forTableColumn("Validate", (DeviceSetting e) -> {
	    	//ref_tvDeviceSetting.getItems().remove(p);

	    	//validateSerialCmdTrigger(e);
	    	ApplicationLauncher.logger.info("validateSerialCmdTrigger: Invoked:");
	    	Timer validateTimer1 = new Timer();
	    	validateTimer1.schedule(new ValidateTimerTask(e),10);
			Sleep(20);
			validateTimer1.cancel();
	        return e;
	    }));*/

		colDsValidate.setCellValueFactory(new PropertyValueFactory<>("ValidateButton"));

		Callback<TableColumn<DeviceSetting, String>, TableCell<DeviceSetting, String>> cellFactory
		= new Callback<TableColumn<DeviceSetting, String>, TableCell<DeviceSetting, String>>() {
			@Override
			public TableCell<DeviceSetting, String> call(final TableColumn<DeviceSetting, String> param) {
				final TableCell<DeviceSetting, String> cell = new TableCell<DeviceSetting, String>() {
					final Button myButton = new Button("Validate");

					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
							setText(null);
						} else {
							DeviceSetting deviceSetting = getTableView().getItems().get(getIndex());

							// Bind the button's disable property to the DeviceSetting's buttonDisabled property
							myButton.disableProperty().bind(deviceSetting.buttonDisabledProperty());

							myButton.setOnAction(event -> {
								int rowIndex = getIndex();
								ApplicationLauncher.logger.debug("colDsValidate OnClick: rowIndex : " + rowIndex);

								// Disable the button for the specific row
								deviceSetting.setButtonDisabled(true);

								Timer validateTimer1 = new Timer();
								validateTimer1.schedule(new ValidateTimerTask(deviceSetting, rowIndex), 10);
							});
							myButton.setPrefWidth(150); 
							setGraphic(myButton);
							setText(null);
						}
					}
				};
				return cell;
			}
		};

		colDsValidate.setCellFactory(cellFactory);


	}

	/*	void refreshTable() {
	    final List<DeviceSetting> items = ref_tvDeviceSetting.getItems();
	    if( items == null || items.size() == 0) return;

	    final DeviceSetting item = ref_tvDeviceSetting.getItems().get(0);
	    items.remove(0);
	    Platform.runLater(new Runnable(){
	        @Override
	        public void run() {
	            items.add(0, item);
	        }
	    });
	 }*/

	/*	public void  validateSerialCmdTrigger(DeviceSetting deviceSetting){
    	ApplicationLauncher.logger.info("validateSerialCmdTrigger: Invoked:");
    	validateTimer = new Timer();
    	//validateTimer.schedule(new ValidateTimerTask(myButton,deviceSetting),200);
    	//LDU3_ValidateTimer.schedule(new Qr3_ValidateTimerTask(),100);// 1000);
    }*/

	/*	class ValidateTimerTask extends TimerTask {
		DeviceSetting deviceSetting;
		Button myButton;
		int rowIndex =0;
		//DeviceSettingValidateButtonFactory deviceSettingValidateButtonFactory;
		public ValidateTimerTask(Button button,DeviceSetting deviceSetting,int rowIndex){
			this.deviceSetting = deviceSetting;
			this.myButton = button;
			this.rowIndex = rowIndex;

		}
		public void run() {

			ApplicationLauncher.setCursor(Cursor.WAIT);


	    	String commPortID= "";
	    	String commBaudRate = "";
	    	deviceSetting.setSerialStatus("InProgress");
	    	deviceSetting.setSerialResponseData("test1");


	    	try{
	    		ref_tvDeviceSetting.getItems().set(rowIndex, deviceSetting);
		    	//serialDM_Obj.commLDU1.searchForPorts(); 
		    	commPortID = deviceSetting.getPortName();
		    	commBaudRate = deviceSetting.getBaudRate();
		    	String portCname = deviceSetting.getcName();
		    	SpmQrScanner serialPortManagerQrScanner = new SpmQrScanner(portCname);
		    	boolean status = serialPortManagerQrScanner.powerSourceComInitV2(commPortID,commBaudRate);
		    	if (!status){

		    		deviceSetting.setSerialStatus(ConstantApp.SERIAL_PORT_ACCESS_FAILED);


		    	} else {

			    		Platform.runLater(()->{
			    			myButton.setDisable(true);
			    		});	
		    		serialPortManagerQrScanner.startSerialRxPhysical_PwrSrc();
		    		serialPortManagerQrScanner.enableSerialRxPhysical_QrScannerMonitor(); 
		    		QrScannerDirector pwrSrcDirector = new QrScannerDirector(serialPortManagerQrScanner);
					Map<String,Object>  responseMap = pwrSrcDirector.scanQrCode();

					status = (boolean)responseMap.get("status");
					String qrData =  "";
					try{
						if(status) {
							qrData = (String)responseMap.get("responseData");  
							ApplicationLauncher.logger.debug("qr3_ValidateSerialCmd: qrData1: "+qrData);
							qrData = NewlandQRCodeScanner.extractScannedData((String)responseMap.get("responseData"));
							ApplicationLauncher.logger.debug("qr3_ValidateSerialCmd: qrData2: "+qrData);
						}else {
							ApplicationLauncher.logger.debug("qr3_ValidateSerialCmd: No response ");
						}
			    	}catch(Exception ex){
			    		ex.printStackTrace();
			    		ApplicationLauncher.logger.error("qr3_ValidateSerialCmd: Exception"+ex.getMessage());
			    	}
		    		if (!status){
		    			deviceSetting.setSerialStatus(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
		    		}else{
		    			deviceSetting.setSerialStatus(ConstantApp.SERIAL_PORT_COMMAND_Success);
		    			deviceSetting.setSerialResponseData(qrData);
		    		}

		    		deviceSetting.setSerialResponseData("test2");
		    		ref_tvDeviceSetting.getItems().set(rowIndex, deviceSetting);	
		    		myButton.setDisable(false);


		    		serialPortManagerQrScanner.disconnectQrScanner();

		    	}
	    	}catch(Exception ex1){
	    		ex1.printStackTrace();
	    		ApplicationLauncher.logger.error("qr3_ValidateSerialCmd: Exception"+ex1.getMessage());
	    	}
	    	//deviceSetting.setSerialResponseData("Test1");
	    	//deviceSetting.setSerialStatus("StatusTest2");
	    	ApplicationLauncher.setCursor(Cursor.DEFAULT);


	    		ref_tvDeviceSetting.getItems().set(rowIndex, deviceSetting);
	    		myButton.setDisable(false);


		}
	}*/

/*	class ValidateTimerTask extends TimerTask {
		DeviceSetting deviceSetting;
		Button myButton;
		int rowIndex = 0;

		public ValidateTimerTask(Button button, DeviceSetting deviceSetting, int rowIndex) {
			this.deviceSetting = deviceSetting;
			this.myButton = button;
			this.rowIndex = rowIndex;
		}

		public void run() {
			// Set the cursor to WAIT when the task starts
			ApplicationLauncher.setCursor(Cursor.WAIT);

			String commPortID = "";
			String commBaudRate = "";
			deviceSetting.setSerialStatus("InProgress");
			deviceSetting.setSerialResponseData("test1");

			try {
				// Update table item at the specified row index
				ref_tvDeviceSetting.getItems().set(rowIndex, deviceSetting);

				commPortID = deviceSetting.getPortName();
				commBaudRate = deviceSetting.getBaudRate();
				String portCname = deviceSetting.getcName();
				SpmQrScanner serialPortManagerQrScanner = new SpmQrScanner(portCname);
				boolean status = serialPortManagerQrScanner.powerSourceComInitV2(commPortID, commBaudRate);

				if (!status) {
					deviceSetting.setSerialStatus(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
				} else {
					// Disable the button immediately
					Platform.runLater(() -> myButton.setDisable(true));

					serialPortManagerQrScanner.startSerialRxPhysical_PwrSrc();
					serialPortManagerQrScanner.enableSerialRxPhysical_QrScannerMonitor();
					QrScannerDirector pwrSrcDirector = new QrScannerDirector(serialPortManagerQrScanner);
					Map<String, Object> responseMap = pwrSrcDirector.scanQrCode();

					status = (boolean) responseMap.get("status");
					String qrData = "";
					try {
						if (status) {
							qrData = (String) responseMap.get("responseData");
							qrData = NewlandQRCodeScanner.extractScannedData(qrData);
						} else {
							ApplicationLauncher.logger.debug("qr3_ValidateSerialCmd: No response ");
						}
					} catch (Exception ex) {
						ex.printStackTrace();
						ApplicationLauncher.logger.error("qr3_ValidateSerialCmd: Exception" + ex.getMessage());
					}

					if (!status) {
						deviceSetting.setSerialStatus(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
					} else {
						deviceSetting.setSerialStatus(ConstantApp.SERIAL_PORT_COMMAND_Success);
						deviceSetting.setSerialResponseData(qrData);
					}
				}

				// Update the table item and re-enable the button after the task
				Platform.runLater(() -> {
					ref_tvDeviceSetting.getItems().set(rowIndex, deviceSetting);
					myButton.setDisable(false); // Re-enable the button
				});

				// Disconnect the QR Scanner after completion
				serialPortManagerQrScanner.disconnectQrScanner();

			} catch (Exception ex1) {
				ex1.printStackTrace();
				ApplicationLauncher.logger.error("qr3_ValidateSerialCmd: Exception" + ex1.getMessage());
			}

			// Reset cursor after task completion
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
		}
	}*/
	
	
	class ValidateTimerTask extends TimerTask {
	    DeviceSetting deviceSetting;
	    int rowIndex = 0;

	    public ValidateTimerTask(DeviceSetting deviceSetting, int rowIndex) {
	        this.deviceSetting = deviceSetting;
	        this.rowIndex = rowIndex;
	    }

	    public void run() {
	        ApplicationLauncher.setCursor(Cursor.WAIT);
	        String commPortID = "";
			String commBaudRate = "";
			
	        try {
	        	deviceSetting.setSerialStatus("InProgress");
		    	deviceSetting.setSerialResponseData("");
		    	Platform.runLater(() -> {
		    		ref_tvDeviceSetting.getItems().set(rowIndex, deviceSetting);
		    	});
	            commPortID = deviceSetting.getPortName();
	            commBaudRate = deviceSetting.getBaudRate();
	            String portCname = deviceSetting.getCanName();
	            boolean terminatorMandatory = true;
				SpmDut spManager = new SpmDut(portCname,terminatorMandatory);
				boolean status = spManager.powerSourceComInitV2(commPortID,commBaudRate);
				if (!status){

					deviceSetting.setSerialStatus(ConstantApp.SERIAL_PORT_ACCESS_FAILED);

				} else {
					
					spManager.startSerialRxPhysical_Dut();
					spManager.enableSerialRxPhysical_DutMonitor(); 

					DutDirector pwrSrcDirector = new DutDirector(spManager);
					Map<String,Object>  responseMap = pwrSrcDirector.fetchDutSerialNumber();

					status = (boolean)responseMap.get("status");
					String responseFromDut =  "";
					try{
						responseFromDut = (String)responseMap.get("responseData");  
						ApplicationLauncher.logger.debug("dut3_ValidateSerialCmd: responseFromDut: "+responseFromDut);
						//qrData = NewlandQRCodeScanner.extractScannedData((String)responseMap.get("responseData"));
						//ApplicationLauncher.logger.debug("dut3_ValidateSerialCmd: responseFromDut: "+responseFromDut);
					}catch(Exception e){
						e.printStackTrace();
						ApplicationLauncher.logger.error("dut3_ValidateSerialCmd: Exception"+e.getMessage());
					}
					 if (!status) {
		                    deviceSetting.setSerialStatus(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
		                } else {
		                    deviceSetting.setSerialStatus(ConstantApp.SERIAL_PORT_COMMAND_Success);
		                    deviceSetting.setSerialResponseData(responseFromDut);
		                }
					
					spManager.disconnectDut();
				}
	    

	            // Update the table item at the specified row index
	            Platform.runLater(() -> {
	            	//deviceSetting.setSerialResponseData("test2");
	                ref_tvDeviceSetting.getItems().set(rowIndex, deviceSetting);
	                deviceSetting.setButtonDisabled(false);  // Re-enable the button
	            });

	           

	        } catch (Exception ex1) {
	            ex1.printStackTrace();
	            ApplicationLauncher.logger.error("ValidateTimerTask: Exception" + ex1.getMessage());
	        }

	        // Reset the cursor after task completion
	        ApplicationLauncher.setCursor(Cursor.DEFAULT);
	    }
	}





	@FXML
	void addDeviceOnClick(ActionEvent event) {


		

		DeviceSetting deviceSetting = new DeviceSetting();
		deviceSetting.setModelName(getDeviceModelName());
		deviceSetting.setBaudRate(getDeviceDefaultBaudRate());
		deviceSetting.setDeviceType(getDeviceType());
		//deviceSetting.setDeviceId(deviceId);
		int deviceTypeKey = 88;
		try {
			OptionalInt maxExistingDeviceTypeKey = ref_tvDeviceSetting.getItems().stream()
					.mapToInt(e->Integer.parseInt(e.getDeviceTypeKey().replace(getDeviceTypeKeyPrefix(), "")))
					.max();
			if(maxExistingDeviceTypeKey.isPresent()) {
				deviceTypeKey = maxExistingDeviceTypeKey.getAsInt()+1;
			}
		}catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.debug("addDeviceOnClick: Dut: Exception: " + e.getMessage());
		}
		deviceSetting.setDeviceTypeKey(getDeviceTypeKeyPrefix() + String.format("%02d", deviceTypeKey));

		ref_tvDeviceSetting.getItems().add(deviceSetting);

		//deviceSetting.setClusterId(getClusterNameIdListMap().get(deviceSetting.getClusterName()));
		reOrderedSerialNo();
		Platform.runLater(()->{
			int lastRowIndex = ref_tvDeviceSetting.getItems().size()-1;
			ref_tvDeviceSetting.scrollTo(lastRowIndex);
			tvDeviceSetting.getSelectionModel().select(lastRowIndex);
		});
	}

	
	@FXML
	public void removeDeviceOnClick(ActionEvent event) {
	
		DeviceSetting deviceSetting = ref_tvDeviceSetting.getSelectionModel().getSelectedItem();
		if(deviceSetting==null) {
			ApplicationLauncher.logger.debug("removeDeviceOnClick: Dut: Kindly select an item to delete - prompted");
			ApplicationLauncher.InformUser("Item not selected","Kindly select an item to delete",AlertType.ERROR);
		
		}else {
			if(deviceSetting.getId()!=null) {
				MySqlServiceManager.getDeviceSettingService().removeById(deviceSetting.getId());
				ref_tvDeviceSetting.getItems().remove(deviceSetting);
				reOrderedSerialNo();
				ApplicationLauncher.logger.debug("removeDeviceOnClick: Dut: Selected item has been successfully deleted - prompted");
				ApplicationLauncher.InformUser("Delete Success","Selected item has been successfully deleted",AlertType.ERROR);
			}
		}
	}
	@FXML
	public void saveOnClick(ActionEvent event) {

		Optional<DeviceSetting> deviceSettingWithEmptyPortOpt = ref_tvDeviceSetting.getItems().stream()
															.filter(e->e.getPortName().isEmpty())
															.findFirst();
		if(deviceSettingWithEmptyPortOpt.isPresent()) {
			DeviceSetting deviceSetting = deviceSettingWithEmptyPortOpt.get();
			ApplicationLauncher.logger.debug("saveOnClick: Dut: Empty serial comm port name found on serial no :  " + deviceSetting.getSerialNo() + " - prompted");
			ApplicationLauncher.InformUser("Port not selected","Empty serial comm port name found on serial no : " + deviceSetting.getSerialNo(), AlertType.ERROR);
		}else {
			String deviceId = "";
			BayUtils bayUtils = new BayUtils();
			for(int i=0; i< ref_tvDeviceSetting.getItems().size();i++) {
/*				if(ref_tvDeviceSetting.getItems().get(i).getPortName().isEmpty()){
					
				}*/
				
				if(ref_tvDeviceSetting.getItems().get(i).getDeviceId().isEmpty()){
					//BayUtils bayUtils = new BayUtils();
					String clusterName = ref_tvDeviceSetting.getItems().get(i).getClusterName();
					String bayName = ref_tvDeviceSetting.getItems().get(i).getBayName();
					ref_tvDeviceSetting.getItems().get(i).setClusterId(BayUtils.getClusterNameIdListMap().get(clusterName));
					ref_tvDeviceSetting.getItems().get(i).setBayId(BayUtils.getClusterBayNameIdMap().get(clusterName+"_"+bayName));
					deviceId = bayUtils.manipulateDeviceId(ref_tvDeviceSetting.getItems().get(i));
					ref_tvDeviceSetting.getItems().get(i).setDeviceId(deviceId);
				}else {
					deviceId = bayUtils.manipulateDeviceId(ref_tvDeviceSetting.getItems().get(i));
					ref_tvDeviceSetting.getItems().get(i).setDeviceId(deviceId);
				}
				
				MySqlServiceManager.getDeviceSettingService().saveToDb(ref_tvDeviceSetting.getItems().get(i));
			}
			if(ref_tvDeviceSetting.getItems().size()>0) {
				ApplicationLauncher.InformUser("Saved","Devices saved successfully" ,AlertType.INFORMATION);
	
			}
		}
		ConveyorDataManager.loadDeviceSettingFromDb();
	}



	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep :InterruptedException:"+ e.getMessage());
		}

	}
/*
	public static Map<String, String> getClusterBayNameIdMap() {
		return clusterBayNameIdMap;
	}

	public void setClusterBayNameIdMap(Map<String, String> clusterBayNameIdMap) {
		this.clusterBayNameIdMap = clusterBayNameIdMap;
	}
*/
	public static Map<String, String> getClusterBayPositionNoCnameMap() {
		return clusterBayPositionNoCnameMap;
	}

	public void setClusterBayPositionNoCnameMap(Map<String, String> clusterBayPositionNoCnameMap) {
		this.clusterBayPositionNoCnameMap = clusterBayPositionNoCnameMap;
	}

	public static Map<String, ArrayList<String>> getClusterBayNamePositionListMap() {
		return clusterBayNamePositionListMap;
	}

	public void setClusterBayNamePositionListMap(Map<String, ArrayList<String>> clusterBayNamePositionListMap) {
		this.clusterBayNamePositionListMap = clusterBayNamePositionListMap;
	}
/*
	public static Map<String, String> getClusterNameIdListMap() {
		return clusterNameIdListMap;
	}

	public void setClusterNameIdListMap(Map<String, String> clusterIdNameListMap) {
		this.clusterNameIdListMap = clusterIdNameListMap;
	}

	public static Map<String, ArrayList<String>> getClusterBayNameListMap() {
		return clusterBayNameListMap;
	}

	public void setClusterBayNameListMap(Map<String, ArrayList<String>> bayNameListMap) {
		this.clusterBayNameListMap = bayNameListMap;
	}
*/
	public static TerminalBayConfigModel getBayConfigModel() {
		return bayConfigModel;
	}

	public static void setBayConfigModel(TerminalBayConfigModel bayConfigModel) {
		DutPortSetupV2Controller.bayConfigModel = bayConfigModel;
	}
/*
	public Map<String, String> getClusterBayPositionNoDeviceIdMap() {
		return clusterBayPositionNoDeviceIdMap;
	}

	public void setClusterBayPositionNoDeviceIdMap(Map<String, String> clusterBayPositionNoDeviceIdMap) {
		this.clusterBayPositionNoDeviceIdMap = clusterBayPositionNoDeviceIdMap;
	}*/

	public String getDeviceTypeKeyPrefix() {
		return deviceTypeKeyPrefix;
	}

	public void setDeviceTypeKeyPrefix(String deviceTypePrefix) {
		this.deviceTypeKeyPrefix = deviceTypePrefix;
	}

	public String getDeviceModelName() {
		return deviceModelName;
	}

	public String getDeviceDefaultBaudRate() {
		return deviceDefaultBaudRate;
	}

	public void setDeviceModelName(String deviceModelName) {
		this.deviceModelName = deviceModelName;
	}

	public void setDeviceDefaultBaudRate(String deviceDefaultBaudRate) {
		this.deviceDefaultBaudRate = deviceDefaultBaudRate;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public AtomicInteger getSerialNoAtomic() {
		return serialNoAtomic;
	}

	public void setSerialNoAtomic(AtomicInteger serialNoAtomic) {
		this.serialNoAtomic = serialNoAtomic;
	}

}
