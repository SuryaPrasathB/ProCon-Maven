package com.tasnetwork.calibration.energymeter.setting;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.WindowManager;
import com.tasnetwork.calibration.energymeter.util.CheckComboBoxTableCell;
import com.tasnetwork.spring.orm.model.TerminalProfileSetting;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;

public class TerminalProfileSettingController implements Initializable {

	private int TOTAL_NO_OF_DUT_POSITIONS = 6;
	@FXML
	private ComboBox<String> cmbBxSelectBayType;
	private static ComboBox<String> ref_cmbBxSelectBayType;

	@FXML
	private TextField txtTerminalName;
	private static TextField ref_txtTerminalName;
	@FXML
	public TableView<TerminalProfileSetting> tvTerminalProfile;
	@FXML
	public TableColumn<TerminalProfileSetting, String> columnSerialNo;
	@FXML
	public TableColumn columnBayActive;

	@FXML
	public TableColumn<TerminalProfileSetting, String> columnBayKey;
	@FXML
	public TableColumn<TerminalProfileSetting, String> columnBayName;

	@FXML
	public TableColumn<TerminalProfileSetting, String> columnClusterName;
	@FXML
	public TableColumn<TerminalProfileSetting, String> columnClusterId;
	@FXML
	public TableColumn<TerminalProfileSetting, String> columnBayId;
	@FXML
	public TableColumn<TerminalProfileSetting, String> columnTotalNoOfPosition;
	@FXML
	public TableColumn<TerminalProfileSetting, String> columnPositionId;

	@FXML
	public TableColumn<TerminalProfileSetting, String> columnPositionToBeSkipped;

	private AtomicInteger serialNoAtomic = new AtomicInteger(1);

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		refAssignment();
		guiInit();
		loadDataFromDb();
	}

	public void loadDataFromDb() {

		List<TerminalProfileSetting> terminalProfileSettingList = MySqlServiceManager.getTerminalProfileSettingService()
				.findAll();
		tvTerminalProfile.getItems().addAll(terminalProfileSettingList);
		OptionalInt lastSerialNo = terminalProfileSettingList.stream().mapToInt(e -> Integer.parseInt(e.getSerialNo()))
				.max();
		if (lastSerialNo.isPresent()) {
			getSerialNoAtomic().set(lastSerialNo.getAsInt() + 1);
		}
	}

	public void refAssignment() {

		// ref_txtNoOfPosition = txtNoOfPosition;
		ref_cmbBxSelectBayType = cmbBxSelectBayType;
		ref_txtTerminalName = txtTerminalName;
		// ref_cmbBxSelectSingleState = cmbBxSelectSingleState;
	}

	public void guiInit() {

		ArrayList<String> bayList = (ArrayList<String>) ConstantConveyor.getBayLookup().keySet().stream()
				.collect(Collectors.toList());
		ref_cmbBxSelectBayType.getItems().add("Select Bay");
		ref_cmbBxSelectBayType.getItems().addAll(bayList);
		ref_cmbBxSelectBayType.getSelectionModel().select(0);

		tvTerminalProfile.setEditable(true);
		columnSerialNo.setCellValueFactory(data -> data.getValue().getSerialNoProperty());
		columnBayActive.setStyle("-fx-alignment: CENTER;");
		columnBayActive.setCellValueFactory(new TerminalProfileBayActive_CheckBoxValueFactory());
		columnBayKey.setCellValueFactory(data -> data.getValue().getBayKeyProperty());
		// columnBayName.setCellValueFactory(data ->
		// data.getValue().getBayNameProperty());

		columnBayName.setCellValueFactory(data -> data.getValue().getBayNameProperty());
		columnBayName.setCellFactory(TextFieldTableCell.forTableColumn());
		columnBayName.setOnEditCommit(new EventHandler<CellEditEvent<TerminalProfileSetting, String>>() {
			public void handle(CellEditEvent<TerminalProfileSetting, String> t) {
				TerminalProfileSetting rowData = ((TerminalProfileSetting) t.getTableView().getItems()
						.get(t.getTablePosition().getRow()));
				if (t.getNewValue() != null) {
					rowData.setBayName(t.getNewValue());
				}
				tvTerminalProfile.refresh();
			}
		});

		columnClusterName.setCellValueFactory(data -> data.getValue().getClusterNameProperty());
		columnClusterName.setCellFactory(TextFieldTableCell.forTableColumn());
		columnClusterName.setOnEditCommit(new EventHandler<CellEditEvent<TerminalProfileSetting, String>>() {
			public void handle(CellEditEvent<TerminalProfileSetting, String> t) {
				TerminalProfileSetting rowData = ((TerminalProfileSetting) t.getTableView().getItems()
						.get(t.getTablePosition().getRow()));
				if (t.getNewValue() != null) {
					rowData.setClusterName(t.getNewValue());
				}
				tvTerminalProfile.refresh();
			}
		});

		columnClusterId.setCellValueFactory(data -> data.getValue().getClusterIdProperty());
		columnBayId.setCellValueFactory(data -> data.getValue().getBayIdProperty());
		columnTotalNoOfPosition.setCellValueFactory(data -> data.getValue().getTotalNoOfPositionsProperty());
		columnPositionId.setCellValueFactory(data -> data.getValue().getPositionIdAsListProperty());
		columnPositionToBeSkipped.setCellValueFactory(data -> data.getValue().getPositionToBeSkippedAsListProperty());

		// columnClusterId.setEditable(true);
		columnClusterId.setCellFactory(TextFieldTableCell.forTableColumn());
		columnClusterId.setOnEditCommit(new EventHandler<CellEditEvent<TerminalProfileSetting, String>>() {
			public void handle(CellEditEvent<TerminalProfileSetting, String> t) {
				TerminalProfileSetting rowData = ((TerminalProfileSetting) t.getTableView().getItems()
						.get(t.getTablePosition().getRow()));
				if (t.getNewValue() != null) {
					rowData.setClusterId(t.getNewValue());
				}
				tvTerminalProfile.refresh();
			}
		});

		columnBayId.setCellFactory(TextFieldTableCell.forTableColumn());
		columnBayId.setOnEditCommit(new EventHandler<CellEditEvent<TerminalProfileSetting, String>>() {
			public void handle(CellEditEvent<TerminalProfileSetting, String> t) {
				TerminalProfileSetting rowData = ((TerminalProfileSetting) t.getTableView().getItems()
						.get(t.getTablePosition().getRow()));
				if (t.getNewValue() != null) {
					rowData.setBayId(t.getNewValue());
				}
				tvTerminalProfile.refresh();
			}
		});

		columnTotalNoOfPosition.setCellFactory(TextFieldTableCell.forTableColumn());
		columnTotalNoOfPosition.setOnEditCommit(new EventHandler<CellEditEvent<TerminalProfileSetting, String>>() {
			public void handle(CellEditEvent<TerminalProfileSetting, String> t) {
				TerminalProfileSetting rowData = ((TerminalProfileSetting) t.getTableView().getItems()
						.get(t.getTablePosition().getRow()));
				if ((t.getNewValue() != null) && (!t.getNewValue().isEmpty())) {
					rowData.setTotalNoOfPositions(t.getNewValue());
					ArrayList<String> positionIdList = new ArrayList<String>();
					for (int i = 0; i < Integer.parseInt(t.getNewValue()); i++) {
						positionIdList.add(String.valueOf(i + 1));
					}
					ObservableList<String> options1 = FXCollections.observableArrayList(positionIdList);
					columnPositionId.setCellFactory(CheckComboBoxTableCell.forTableColumn(options1));
					columnPositionToBeSkipped.setCellFactory(CheckComboBoxTableCell.forTableColumn(options1));
					tvTerminalProfile.refresh();
				}

			}
		});

		ArrayList<String> positionIdList = new ArrayList<String>();
		for (int i = 0; i < TOTAL_NO_OF_DUT_POSITIONS; i++) {
			positionIdList.add(String.valueOf(i + 1));
		}
		ObservableList<String> options1 = FXCollections.observableArrayList(positionIdList);
		columnPositionId.setCellFactory(CheckComboBoxTableCell.forTableColumn(options1));

		columnPositionId.addEventHandler(TableColumn.editCommitEvent(), event -> {
			TerminalProfileSetting rowData = ((TerminalProfileSetting) event.getTableView().getItems()
					.get(event.getTablePosition().getRow()));
			String newValue = (String) event.getNewValue();
			ApplicationLauncher.logger.debug("columnPositionId: value: " + newValue);
			rowData.setPositionIdAsList(newValue);
		});

		ArrayList<String> positionToBeSkippedIdList = new ArrayList<String>();
		for (int i = 0; i < TOTAL_NO_OF_DUT_POSITIONS; i++) {
			positionToBeSkippedIdList.add(String.valueOf(i + 1));
		}
		ObservableList<String> options2 = FXCollections.observableArrayList(positionToBeSkippedIdList);
		columnPositionToBeSkipped.setCellFactory(CheckComboBoxTableCell.forTableColumn(options2));

		columnPositionToBeSkipped.addEventHandler(TableColumn.editCommitEvent(), event -> {
			TerminalProfileSetting rowData = ((TerminalProfileSetting) event.getTableView().getItems()
					.get(event.getTablePosition().getRow()));
			String newValue = (String) event.getNewValue();
			ApplicationLauncher.logger.debug("columnPositionToBeSkipped value: " + newValue);
			rowData.setPositionTobeSkippedAsList(newValue);
		});
	}

	@FXML
	public void saveOnClick() {
		for (int i = 0; i < tvTerminalProfile.getItems().size(); i++) {
			MySqlServiceManager.getTerminalProfileSettingService().saveToDb(tvTerminalProfile.getItems().get(i));
		}
		if (tvTerminalProfile.getItems().size() > 0) {
			WindowManager.InformUser("Saved", "Terminal profile saved successfully", AlertType.INFORMATION);

		}
	}

	@FXML
	public void addOnClick() {
		String selectedBayName = (String) ref_cmbBxSelectBayType.getSelectionModel().getSelectedItem();
		if (!selectedBayName.equals("Select Bay")) {
			Optional<TerminalProfileSetting> terminalProfileSettingOpt = tvTerminalProfile.getItems().stream()
					.filter(e -> e.getBayName().equals(selectedBayName))
					.findFirst();
			if (terminalProfileSettingOpt.isPresent()) {
				ApplicationLauncher.logger.debug("addOnClick: Bay Name already Exist");
				WindowManager.InformUser("Bay already Exist",
						"Bay Name already exist. Kindly try with different Bay name", AlertType.ERROR);

			} else {

				String terminalName = ref_txtTerminalName.getText();
				TerminalProfileSetting terminalProfileSetting = new TerminalProfileSetting();
				terminalProfileSetting.setSerialNo(String.valueOf(getSerialNoAtomic().getAndIncrement()));
				terminalProfileSetting.setBayName(selectedBayName);
				terminalProfileSetting.setBayKey(ConstantConveyor.getBayLookup().get(selectedBayName));
				terminalProfileSetting.setTerminalId(ConstantConveyorConfig.MY_TERMINAL_ID);
				terminalProfileSetting.setTerminalName(terminalName);
				terminalProfileSetting.setTotalNoOfPositions(String.valueOf(TOTAL_NO_OF_DUT_POSITIONS));
				tvTerminalProfile.getItems().add(terminalProfileSetting);
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
