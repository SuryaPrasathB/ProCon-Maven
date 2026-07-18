package com.tasnetwork.calibration.energymeter.setting;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.WindowManager;
import com.tasnetwork.spring.orm.model.TerminalProfileSetting;
import com.tasnetwork.spring.orm.model.BayDeviceConfig;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class BayDeviceConfigController implements Initializable {

	private AtomicInteger serialNoAtomic = new AtomicInteger(1);

	@FXML
	private Button btn_Save;

	@FXML
	private ComboBox<String> cmbBxSelectBayType;
	public static ComboBox<String> ref_cmbBxSelectBayType;

	@FXML
	private TextField txtTerminalId;
	public static TextField ref_txtTerminalId;

	@FXML
	private TextField txtTerminalName;
	public static TextField ref_txtTerminalName;

	@FXML
	public TableView<BayDeviceConfig> tvBayDeviceConfig;

	@FXML
	private TableColumn<BayDeviceConfig, String> columnBayKey;

	@FXML
	private TableColumn<BayDeviceConfig, String> columnBayName;

	@FXML
	private TableColumn columnBayDevicesActive;

	@FXML
	private TableColumn columnDutCommType;

	@FXML
	private TableColumn columnDutEnabled;

	@FXML
	private TableColumn columnLduCommType;

	@FXML
	private TableColumn columnLduEnabled;

	@FXML
	private TableColumn columnMegaOhmPmCommType;

	@FXML
	private TableColumn columnMegaOhmPmEnabled;

	@FXML
	private TableColumn columnOpticalSensorCommType;

	@FXML
	private TableColumn columnOpticalSensorEnabled;

	@FXML
	private TableColumn columnQrDutCommType;

	@FXML
	private TableColumn columnQrDutEnabled;

	@FXML
	private TableColumn columnQrPalletCommType;

	@FXML
	private TableColumn columnQrPalletEnabled;

	@FXML
	private TableColumn<BayDeviceConfig, String> columnSerialNo;

	@FXML
	private TableColumn columnVoltPmCommType;

	@FXML
	private TableColumn columnVoltPmEnabled;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		refAssignment();
		guiInit();
		loadDataFromDb();
	}

	public void loadDataFromDb() {

		List<BayDeviceConfig> bayDeviceConfigList = MySqlServiceManager.getBayDeviceConfigService().findAll();
		tvBayDeviceConfig.getItems().addAll(bayDeviceConfigList);
		OptionalInt lastSerialNo = bayDeviceConfigList.stream().mapToInt(e -> Integer.parseInt(e.getSerialNo())).max();
		if (lastSerialNo.isPresent()) {
			getSerialNoAtomic().set(lastSerialNo.getAsInt() + 1);
		}
	}

	public void guiInit() {

		ArrayList<String> bayList = (ArrayList<String>) ConstantConveyor.getBayLookup().keySet().stream()
				.collect(Collectors.toList());
		ref_cmbBxSelectBayType.getItems().add("Select Bay");
		ref_cmbBxSelectBayType.getItems().addAll(bayList);
		ref_cmbBxSelectBayType.getSelectionModel().select(0);

		tvBayDeviceConfig.setEditable(true);
		columnSerialNo.setCellValueFactory(data -> data.getValue().getSerialNoProperty());
		columnBayDevicesActive.setStyle("-fx-alignment: CENTER;");
		columnBayDevicesActive.setCellValueFactory(new BayDeviceConfigDeviceActive_CheckBoxValueFactory());
		columnBayKey.setCellValueFactory(data -> data.getValue().getBayKeyProperty());
		columnBayName.setCellValueFactory(data -> data.getValue().getBayNameProperty());

		columnDutCommType.setCellValueFactory(new BayDeviceConfigDutComTypeComboBoxValueFactory());
		columnDutEnabled.setStyle("-fx-alignment: CENTER;");
		columnDutEnabled.setCellValueFactory(new BayDeviceConfigDutEnabled_CheckBoxValueFactory());

		columnLduCommType.setCellValueFactory(new BayDeviceConfigLduComTypeComboBoxValueFactory());
		columnLduEnabled.setStyle("-fx-alignment: CENTER;");
		columnLduEnabled.setCellValueFactory(new BayDeviceConfigLduEnabled_CheckBoxValueFactory());

		columnMegaOhmPmCommType.setCellValueFactory(new BayDeviceConfigMegaOhmPmComTypeComboBoxValueFactory());
		columnMegaOhmPmEnabled.setStyle("-fx-alignment: CENTER;");
		columnMegaOhmPmEnabled.setCellValueFactory(new BayDeviceConfigMegaOhmPmEnabled_CheckBoxValueFactory());

		columnOpticalSensorCommType.setCellValueFactory(new BayDeviceConfigOpticalSensorComTypeComboBoxValueFactory());
		columnOpticalSensorEnabled.setStyle("-fx-alignment: CENTER;");
		columnOpticalSensorEnabled.setCellValueFactory(new BayDeviceConfigOpticalSensorEnabled_CheckBoxValueFactory());

		columnQrDutCommType.setCellValueFactory(new BayDeviceConfigQrDutComTypeComboBoxValueFactory());
		columnQrDutEnabled.setStyle("-fx-alignment: CENTER;");
		columnQrDutEnabled.setCellValueFactory(new BayDeviceConfigQrDutEnabled_CheckBoxValueFactory());

		columnQrPalletCommType.setCellValueFactory(new BayDeviceConfigQrPalletComTypeComboBoxValueFactory());
		columnQrPalletEnabled.setStyle("-fx-alignment: CENTER;");
		columnQrPalletEnabled.setCellValueFactory(new BayDeviceConfigQrPalletEnabled_CheckBoxValueFactory());

		columnVoltPmCommType.setCellValueFactory(new BayDeviceConfigVoltPmComTypeComboBoxValueFactory());
		columnVoltPmEnabled.setStyle("-fx-alignment: CENTER;");
		columnVoltPmEnabled.setCellValueFactory(new BayDeviceConfigVoltPmEnabled_CheckBoxValueFactory());

	}

	public void refAssignment() {

		ref_cmbBxSelectBayType = cmbBxSelectBayType;
		ref_txtTerminalId = txtTerminalId;
		ref_txtTerminalName = txtTerminalName;
	}

	@FXML
	void saveOnClick(ActionEvent event) {
		for (int i = 0; i < tvBayDeviceConfig.getItems().size(); i++) {
			MySqlServiceManager.getBayDeviceConfigService().saveToDb(tvBayDeviceConfig.getItems().get(i));
		}
		if (tvBayDeviceConfig.getItems().size() > 0) {
			WindowManager.InformUser("Saved", "Bay Device configuration saved successfully", AlertType.INFORMATION);

		}
	}

	@FXML
	void addOnClick(ActionEvent event) {
		String selectedBayName = (String) ref_cmbBxSelectBayType.getSelectionModel().getSelectedItem();
		if (!selectedBayName.equals("Select Bay")) {
			Optional<BayDeviceConfig> terminalProfileSettingOpt = tvBayDeviceConfig.getItems().stream()
					.filter(e -> e.getBayName().equals(selectedBayName))
					.findFirst();
			if (terminalProfileSettingOpt.isPresent()) {
				ApplicationLauncher.logger.debug("addOnClick: Bay Name already Exist");
				WindowManager.InformUser("Bay already Exist",
						"Bay Name already exist. Kindly try with different Bay name", AlertType.ERROR);

			} else {

				String terminalName = ref_txtTerminalName.getText();
				BayDeviceConfig bayDeviceConfig = new BayDeviceConfig();
				bayDeviceConfig.setSerialNo(String.valueOf(getSerialNoAtomic().getAndIncrement()));
				bayDeviceConfig.setBayName(selectedBayName);
				bayDeviceConfig.setBayKey(ConstantConveyor.getBayLookup().get(selectedBayName));
				bayDeviceConfig.setTerminalId(ConstantConveyorConfig.MY_TERMINAL_ID);
				bayDeviceConfig.setTerminalName(terminalName);

				tvBayDeviceConfig.getItems().add(bayDeviceConfig);
			}
		}
	}

	public AtomicInteger getSerialNoAtomic() {
		return serialNoAtomic;
	}

	public void setSerialNoAtomic(AtomicInteger serialNoAtomic) {
		this.serialNoAtomic = serialNoAtomic;
	}

}
