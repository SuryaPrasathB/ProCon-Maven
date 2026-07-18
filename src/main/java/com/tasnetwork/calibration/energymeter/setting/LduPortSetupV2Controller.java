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
import com.tasnetwork.calibration.conveyor.bay.EIC_MegaOhmMeter;
import com.tasnetwork.calibration.conveyor.bay.Elmeasure_MultiMeter;
import com.tasnetwork.calibration.conveyor.bay.configloader.Bay;
import com.tasnetwork.calibration.conveyor.bay.configloader.ClusterDetail;
import com.tasnetwork.calibration.conveyor.bay.configloader.Terminal;
import com.tasnetwork.calibration.conveyor.bay.configloader.TerminalBayConfigModel;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.constant.ConstantLdu;
import com.tasnetwork.calibration.conveyor.constant.ConstantMegaOhmPm;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.serial.director.DutDirector;
import com.tasnetwork.calibration.conveyor.serial.director.LduDirector;
import com.tasnetwork.calibration.conveyor.serial.director.MegaOhmPmDirector;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmDut;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmLdu;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmMegaOhmPm;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.WindowManager;
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

public class LduPortSetupV2Controller implements Initializable {

	Timer validateTimer;

	String deviceTypeKeyPrefix = ConstantLdu.LDU_TYPE_PREFIX;
	String deviceModelName = ConstantLdu.LDU_LSCS_MODEL;
	String deviceType = ConstantConveyor.DEVICE_TYPE_LDU;
	String deviceDefaultBaudRate = String.valueOf(ConstantLdu.LDU_DEFAULT_BAUD_RATE);

	private static TerminalBayConfigModel bayConfigModel = ConveyorDataManager.getTerminalBayConfig();
	/*
	 * private static Map<String,ArrayList<String>> clusterBayNameListMap = new
	 * HashMap<String,ArrayList<String>>();
	 * private static Map<String,String> clusterNameIdListMap = new
	 * HashMap<String,String>();
	 * private static Map<String,String> clusterBayNameIdMap = new
	 * HashMap<String,String>();
	 */
	private static Map<String, ArrayList<String>> clusterBayNamePositionListMap = new HashMap<String, ArrayList<String>>();
	private static Map<String, String> clusterBayPositionNoCnameMap = new LinkedHashMap<String, String>();
	// private static Map<String,String> clusterBayPositionNoDeviceIdMap = new
	// LinkedHashMap<String,String>();

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
	private TableColumn colDsRs485Address;

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

		refAssignment();
		// loadDataFromConfig();
		guiInit();
		loadDataFromDb();
	}

	public void refAssignment() {

		ref_tvDeviceSetting = tvDeviceSetting;
		ref_chkCmbBxModelType = chkCmbBxModelType;
		ref_chkCmbBxDeviceType = chkCmbBxDeviceType;
	}

	public void loadDataFromConfig() {

		ApplicationLauncher.logger.debug("loadDataFromConfig-Q2: Entry");
		setClusterBayPositionNoCnameMap(BayUtils.getDutClusterBayPositionNoCnameMap());
		setClusterBayNamePositionListMap(BayUtils.getDutClusterBayNamePositionListMap());
	}

	public void loadDataFromDb() {

		List<DeviceSetting> deviceSettingList = MySqlServiceManager.getDeviceSettingService()
				.findByDeviceType(getDeviceType());
		// deviceSettingList = reOrderedSerialNo(deviceSettingList);

		ref_tvDeviceSetting.getItems().addAll(FXCollections.observableArrayList(deviceSettingList));
		reOrderedSerialNo();
	}

	public void reOrderedSerialNo() {
		// List<DeviceSetting> deviceSettingList = ref_tvDeviceSetting.getItems();
		getSerialNoAtomic().set(1);
		ref_tvDeviceSetting.getItems().stream().forEachOrdered(e -> {
			e.setSerialNo(getSerialNoAtomic().getAndIncrement());
			// ApplicationLauncher.logger.debug("reOrderedSerialNo: getPositionNo : " +
			// e.getPositionNo() + " -> getcName: " + e.getcName());

		});
		ref_tvDeviceSetting.refresh();
		// ref_tvDeviceSetting.getItems().clear();
		// ref_tvDeviceSetting.getItems().addAll(deviceSettingList);
	}

	public void guiInit() {

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

		colDsRs485Address.setCellValueFactory(new DeviceSettingRs485AddressComboBoxValueFactory());

		colDsPositionNo.setCellValueFactory(new DeviceSettingPositionNoComboBoxValueFactory());

		colDsDeviceId.setCellValueFactory(data -> data.getValue().getDeviceIdProperty());

		// colDsReadData.setCellValueFactory(data ->
		// data.getValue().getSerialResponseDataProperty());
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
						textField.setEditable(false); // Disable editing

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
						textField.setEditable(false); // Disable editing

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
						textField.setEditable(false); // Disable editing

						setGraphic(textField);
						setText(null);
					}
				}
			};
		});

		colDsValidate.setCellValueFactory(new PropertyValueFactory<>("ValidateButton"));

		Callback<TableColumn<DeviceSetting, String>, TableCell<DeviceSetting, String>> cellFactory = new Callback<TableColumn<DeviceSetting, String>, TableCell<DeviceSetting, String>>() {
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

							// Bind the button's disable property to the DeviceSetting's buttonDisabled
							// property
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

	class ValidateTimerTask extends TimerTask {
		DeviceSetting deviceSetting;
		int rowIndex = 0;

		public ValidateTimerTask(DeviceSetting deviceSetting, int rowIndex) {
			this.deviceSetting = deviceSetting;
			this.rowIndex = rowIndex;
		}

		public void run() {
			WindowManager.setCursor(Cursor.WAIT);
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
				String slaveId = deviceSetting.getRs485Address();
				ApplicationLauncher.logger.debug("ldu_ValidateSerialCmd: slaveId: " + slaveId);
				SpmLdu serialPortManager = new SpmLdu(portCname);
				boolean status = serialPortManager.powerSourceComInitV2(commPortID, commBaudRate);

				if (!status) {

					deviceSetting.setSerialStatus(ConstantApp.SERIAL_PORT_ACCESS_FAILED);

				} else {

					serialPortManager.startSerialRxPhysical_Ldu();
					serialPortManager.enableSerialRxPhysical_LduMonitor();
					LduDirector pwrSrcDirector = new LduDirector(serialPortManager);
					// String slaveId=
					// ref_cmbBxLdu1DeviceAddress.getSelectionModel().getSelectedItem();//ConstantBayPortNameMapping.ERC_MEGA_OHM_METER_01_SLAVE_ID;
					ApplicationLauncher.logger.debug("ldu1_ValidateSerialCmd: slaveId: " + slaveId);
					Map<String, Object> responseMap = pwrSrcDirector.lduCheckCom(slaveId);

					status = (boolean) responseMap.get("status");
					String responseData = "";
					try {
						if (status) {
							responseData = (String) responseMap.get("responseData");
							ApplicationLauncher.logger.debug("ldu1_ValidateSerialCmd: responseData1: " + responseData);
							Elmeasure_MultiMeter elmeasure_MultiMeter = new Elmeasure_MultiMeter();
							responseData = elmeasure_MultiMeter
									.extractVoltageValueFromResponse((String) responseMap.get("responseData"));
							ApplicationLauncher.logger.debug("ldu1_ValidateSerialCmd: responseData2: " + responseData);
						} else {
							ApplicationLauncher.logger.debug("ldu1_ValidateSerialCmd: No response ");
						}
					} catch (Exception e) {
						e.printStackTrace();
						ApplicationLauncher.logger.error("ldu1_ValidateSerialCmd: Exception" + e.getMessage());
					}
					if (!status) {
						deviceSetting.setSerialStatus(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
					} else {
						deviceSetting.setSerialStatus(ConstantApp.SERIAL_PORT_COMMAND_Success);
						deviceSetting.setSerialResponseData(responseData);
					}

					serialPortManager.disconnectLdu();
				}

				// Update the table item at the specified row index
				Platform.runLater(() -> {
					// deviceSetting.setSerialResponseData("test2");
					ref_tvDeviceSetting.getItems().set(rowIndex, deviceSetting);
					deviceSetting.setButtonDisabled(false); // Re-enable the button
				});

			} catch (Exception ex1) {
				ex1.printStackTrace();
				ApplicationLauncher.logger.error("ValidateTimerTask: Exception" + ex1.getMessage());
			}

			// Reset the cursor after task completion
			WindowManager.setCursor(Cursor.DEFAULT);
		}
	}

	@FXML
	void addDeviceOnClick(ActionEvent event) {

		DeviceSetting deviceSetting = new DeviceSetting();
		deviceSetting.setModelName(getDeviceModelName());
		deviceSetting.setBaudRate(getDeviceDefaultBaudRate());
		deviceSetting.setDeviceType(getDeviceType());
		// deviceSetting.setDeviceId(deviceId);

		int deviceTypeKey = 77;
		try {
			OptionalInt maxExistingDeviceTypeKey = ref_tvDeviceSetting.getItems().stream()
					.mapToInt(e -> Integer.parseInt(e.getDeviceTypeKey().replace(getDeviceTypeKeyPrefix(), "")))
					.max();
			if (maxExistingDeviceTypeKey.isPresent()) {
				deviceTypeKey = maxExistingDeviceTypeKey.getAsInt() + 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.debug("addDeviceOnClick: Ldu: Exception: " + e.getMessage());
		}
		deviceSetting.setDeviceTypeKey(getDeviceTypeKeyPrefix() + String.format("%02d", deviceTypeKey));

		ref_tvDeviceSetting.getItems().add(deviceSetting);

		// deviceSetting.setClusterId(getClusterNameIdListMap().get(deviceSetting.getClusterName()));
		reOrderedSerialNo();
		Platform.runLater(() -> {
			int lastRowIndex = ref_tvDeviceSetting.getItems().size() - 1;
			ref_tvDeviceSetting.scrollTo(lastRowIndex);
			tvDeviceSetting.getSelectionModel().select(lastRowIndex);
		});
	}

	@FXML
	public void removeDeviceOnClick(ActionEvent event) {

		DeviceSetting deviceSetting = ref_tvDeviceSetting.getSelectionModel().getSelectedItem();
		if (deviceSetting == null) {
			ApplicationLauncher.logger.debug("removeDeviceOnClick: Dut: Kindly select an item to delete - prompted");
			WindowManager.InformUser("Item not selected", "Kindly select an item to delete", AlertType.ERROR);

		} else {
			if (deviceSetting.getId() != null) {
				MySqlServiceManager.getDeviceSettingService().removeById(deviceSetting.getId());
				ref_tvDeviceSetting.getItems().remove(deviceSetting);
				reOrderedSerialNo();
				ApplicationLauncher.logger
						.debug("removeDeviceOnClick: Dut: Selected item has been successfully deleted - prompted");
				WindowManager.InformUser("Delete Success", "Selected item has been successfully deleted",
						AlertType.ERROR);
			}
		}
	}

	@FXML
	public void saveOnClick(ActionEvent event) {

		Optional<DeviceSetting> deviceSettingWithEmptyPortOpt = ref_tvDeviceSetting.getItems().stream()
				.filter(e -> e.getPortName().isEmpty())
				.findFirst();
		if (deviceSettingWithEmptyPortOpt.isPresent()) {
			DeviceSetting deviceSetting = deviceSettingWithEmptyPortOpt.get();
			ApplicationLauncher.logger.debug("saveOnClick: Dut: Empty serial comm port name found on serial no :  "
					+ deviceSetting.getSerialNo() + " - prompted");
			WindowManager.InformUser("Port not selected",
					"Empty serial comm port name found on serial no : " + deviceSetting.getSerialNo(), AlertType.ERROR);
		} else {
			String deviceId = "";
			BayUtils bayUtils = new BayUtils();
			for (int i = 0; i < ref_tvDeviceSetting.getItems().size(); i++) {
				/*
				 * if(ref_tvDeviceSetting.getItems().get(i).getPortName().isEmpty()){
				 * 
				 * }
				 */

				if (ref_tvDeviceSetting.getItems().get(i).getDeviceId().isEmpty()) {
					// BayUtils bayUtils = new BayUtils();
					String clusterName = ref_tvDeviceSetting.getItems().get(i).getClusterName();
					String bayName = ref_tvDeviceSetting.getItems().get(i).getBayName();
					ref_tvDeviceSetting.getItems().get(i)
							.setClusterId(BayUtils.getClusterNameIdListMap().get(clusterName));
					ref_tvDeviceSetting.getItems().get(i)
							.setBayId(BayUtils.getClusterBayNameIdMap().get(clusterName + "_" + bayName));
					deviceId = bayUtils.manipulateDeviceId(ref_tvDeviceSetting.getItems().get(i));
					ref_tvDeviceSetting.getItems().get(i).setDeviceId(deviceId);
				} else {
					deviceId = bayUtils.manipulateDeviceId(ref_tvDeviceSetting.getItems().get(i));
					ref_tvDeviceSetting.getItems().get(i).setDeviceId(deviceId);
				}

				MySqlServiceManager.getDeviceSettingService().saveToDb(ref_tvDeviceSetting.getItems().get(i));
			}
			if (ref_tvDeviceSetting.getItems().size() > 0) {
				WindowManager.InformUser("Saved", "Devices saved successfully", AlertType.INFORMATION);

			}
		}
		ConveyorDataManager.loadDeviceSettingFromDb();
	}

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep :InterruptedException:" + e.getMessage());
		}

	}

	/*
	 * public static Map<String, String> getClusterBayNameIdMap() {
	 * return clusterBayNameIdMap;
	 * }
	 * 
	 * public void setClusterBayNameIdMap(Map<String, String> clusterBayNameIdMap) {
	 * this.clusterBayNameIdMap = clusterBayNameIdMap;
	 * }
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
	 * public static Map<String, String> getClusterNameIdListMap() {
	 * return clusterNameIdListMap;
	 * }
	 * 
	 * public void setClusterNameIdListMap(Map<String, String> clusterIdNameListMap)
	 * {
	 * this.clusterNameIdListMap = clusterIdNameListMap;
	 * }
	 * 
	 * public static Map<String, ArrayList<String>> getClusterBayNameListMap() {
	 * return clusterBayNameListMap;
	 * }
	 * 
	 * public void setClusterBayNameListMap(Map<String, ArrayList<String>>
	 * bayNameListMap) {
	 * this.clusterBayNameListMap = bayNameListMap;
	 * }
	 */
	public static TerminalBayConfigModel getBayConfigModel() {
		return bayConfigModel;
	}

	public static void setBayConfigModel(TerminalBayConfigModel bayConfigModel) {
		LduPortSetupV2Controller.bayConfigModel = bayConfigModel;
	}
	/*
	 * public Map<String, String> getClusterBayPositionNoDeviceIdMap() {
	 * return clusterBayPositionNoDeviceIdMap;
	 * }
	 * 
	 * public void setClusterBayPositionNoDeviceIdMap(Map<String, String>
	 * clusterBayPositionNoDeviceIdMap) {
	 * this.clusterBayPositionNoDeviceIdMap = clusterBayPositionNoDeviceIdMap;
	 * }
	 */

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
