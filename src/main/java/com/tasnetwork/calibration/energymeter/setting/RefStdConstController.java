package com.tasnetwork.calibration.energymeter.setting;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.WindowManager;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdRadiant;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
import com.tasnetwork.calibration.energymeter.deployment.TextBoxDialog;
import com.tasnetwork.calibration.energymeter.testprofiles.TestCaseData;
import com.tasnetwork.calibration.energymeter.uac.UacAddPossibleCheckBoxValueFactory;
import com.tasnetwork.calibration.energymeter.uac.UacDataModel;
import com.tasnetwork.calibration.energymeter.uac.UacDeletePossibleCheckBoxValueFactory;
import com.tasnetwork.calibration.energymeter.uac.UacExecutePossibleCheckBoxValueFactory;
import com.tasnetwork.calibration.energymeter.uac.UacUpdatePossibleCheckBoxValueFactory;
import com.tasnetwork.calibration.energymeter.uac.UacVisibleEnabledCheckBoxValueFactory;
import com.tasnetwork.calibration.energymeter.util.EditCell;
import com.tasnetwork.calibration.energymeter.util.EditCellRefStdPulseConstVoltageAndCurrent;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;
import com.tasnetwork.calibration.energymeter.util.MyFloatStringConverter;
import com.tasnetwork.calibration.energymeter.util.MyIntegerStringConverter;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;
import javafx.scene.control.Button;

public class RefStdConstController implements Initializable {

	private final float MINIMUM_ACCEPTED_CURRENT = 0.001f;
	private final float MAXIMUM_ACCEPTED_CURRENT = 120.0f;

	private final float MINIMUM_ACCEPTED_VOLTAGE = 30.0f;
	private final float MAXIMUM_ACCEPTED_VOLTAGE = 480.0f;

	private boolean voltageAndCurrentTapSelected = false;

	// private boolean validateTableViewEditWithComboBoxType = true;// = false;

	Timer modelSaveNextTimer;

	@FXML
	private ComboBox<String> cmbBox_metertype;

	@FXML
	private TextField txt_32_mA;

	@FXML
	private TextField txt_65_mA;

	@FXML
	private TextField txt_131_mA;

	@FXML
	private TextField txt_262_mA;

	@FXML
	private TextField txt_524_mA;

	@FXML
	private TextField txt_1_A;

	@FXML
	private TextField txt_2_A;

	@FXML
	private TextField txt_4_A;

	@FXML
	private TextField txt_8_A;

	@FXML
	private TextField txt_16_A;

	@FXML
	private TextField txt_33_A;

	@FXML
	private TextField txt_67_A;

	@FXML
	private TextField txt_200_A;

	public ArrayList<TextField> list_of_txt_fields = new ArrayList<TextField>();

	/*
	 * @FXML private TableColumn<RefStdPulseConstantModel, String>
	 * tableColCurrentTapSerialNo;
	 * 
	 * @FXML private TableColumn<RefStdPulseConstantModel, String>
	 * tableColCurrentTapValue;
	 * //@FXML private TableColumn tableColCurrentTapOperation;
	 * 
	 * @FXML private TableColumn<RefStdPulseConstantModel, String>
	 * tableColCurrentTapOperation;
	 * //@FXML private TableColumn<RefStdPulseConstantModel, String>
	 * tableColCurrentTapConstantV1_InWh;
	 * 
	 * @FXML private TableColumn<RefStdPulseConstantModel, Integer>
	 * tableColCurrentTapConstantV1_InWh;
	 */

	@FXML
	private TableColumn<RefStdPulseConstantModel, String> tableColVoltageTapSerialNo;
	// @FXML private TableColumn tableColVoltageTapSerialNo;
	@FXML
	private TableColumn<RefStdPulseConstantModel, String> tableColVoltageCurrentTapValue;
	// @FXML private TableColumn tableColVoltageCurrentTapValue;
	// @FXML private TableColumn tableColVoltageTapOperation;
	@FXML
	private TableColumn<RefStdPulseConstantModel, String> tableColVoltageTapOperation;
	@FXML
	private TableColumn<RefStdPulseConstantModel, Integer> tableColVoltageTapConstantV1_InWh;
	@FXML
	private TableColumn<RefStdPulseConstantModel, Integer> tableColVoltageTapConstantV2_InWh;
	@FXML
	private TableColumn<RefStdPulseConstantModel, Integer> tableColVoltageTapConstantV3_InWh;
	@FXML
	private TableColumn<RefStdPulseConstantModel, Integer> tableColVoltageTapConstantV4_InWh;
	@FXML
	private TableColumn<RefStdPulseConstantModel, Integer> tableColVoltageTapConstantV5_InWh;

	@FXML
	private TableView<RefStdPulseConstantModel> tableViewCurrentMappingOnly;
	public static TableView<RefStdPulseConstantModel> ref_tableViewCurrentMappingOnly;

	// @FXML private TableView<RefStdPulseConstantModel>
	// tableViewVoltageAndCurrentMapping;
	// public static TableView<RefStdPulseConstantModel>
	// ref_tableViewVoltageAndCurrentMapping;

	@FXML
	private TitledPane titledPaneModelSection;
	private static TitledPane ref_titledPaneModelSection;

	@FXML
	private TitledPane titledPaneLookupTable;
	private static TitledPane ref_titledPaneLookupTable;

	@FXML
	private TabPane tabPaneTapMapping;
	private static TabPane ref_tabPaneTapMapping;

	@FXML
	private ComboBox cmBxRefStdModel;
	@FXML
	private RadioButton rdBtnCurrentMappingOnly;
	@FXML
	private RadioButton rdBtnVoltageAndCurrentMapping;
	@FXML
	private RadioButton rdbtnMilliAmps;
	@FXML
	private RadioButton rdbtnAmps;
	@FXML
	private RadioButton rdbtnMilliVolts;
	@FXML
	private RadioButton rdbtnVolts;

	@FXML
	private TextField txtNoOfCurrentTap;
	@FXML
	private TextField txtNoOfVoltageTap;
	@FXML
	private TextField txtCurrentValue;
	@FXML
	private TextField txtVoltageValue;

	@FXML
	private TitledPane titledPaneCurrentTap;
	@FXML
	private TitledPane titledPaneVoltageTap;

	@FXML
	private ListView<String> listViewCurrentTap;
	@FXML
	private ListView<String> listViewVoltageTap;

	@FXML
	private Button btnModelSectionNext;
	// @FXML private Button btnCurrentTapLookupSave;
	@FXML
	private Button btnCurrentAndVoltageTapLookupSave;

	@FXML
	private Label labelMeterType;
	@FXML
	private ComboBox cmbBoxMetertype;

	// @FXML private Tab tabCurrentMapping;
	@FXML
	private Tab tabVoltageAndCurrentMapping;

	// @FXML private TableView tableViewCurrentMapping;
	// @FXML private TableView tableViewVoltageAndCurrentMapping;
	@FXML
	private TableView<RefStdPulseConstantModel> tableViewVoltageAndCurrentMapping;

	@FXML
	private TableColumn tableColVoltageTapV1_Hdr;
	@FXML
	private TableColumn tableColVoltageTapV2_Hdr;
	@FXML
	private TableColumn tableColVoltageTapV3_Hdr;
	@FXML
	private TableColumn tableColVoltageTapV4_Hdr;
	@FXML
	private TableColumn tableColVoltageTapV5_Hdr;

	private static ComboBox ref_cmBxRefStdModel;
	private static RadioButton ref_rdBtnCurrentMappingOnly;
	private static RadioButton ref_rdBtnVoltageAndCurrentMapping;

	private static RadioButton ref_rdbtnMilliAmps;
	private static RadioButton ref_rdbtnAmps;
	private static RadioButton ref_rdbtnMilliVolts;
	private static RadioButton ref_rdbtnVolts;

	private static TextField ref_txtNoOfCurrentTap;
	private static TextField ref_txtNoOfVoltageTap;
	private static TextField ref_txtCurrentValue;
	private static TextField ref_txtVoltageValue;

	private static TitledPane ref_titledPaneCurrentTap;
	private static TitledPane ref_titledPaneVoltageTap;

	private static ListView<String> ref_listViewCurrentTap;
	private static ListView<String> ref_listViewVoltageTap;

	private static Button ref_btnModelSectionNext;
	// private static Button ref_btnCurrentTapLookupSave;
	private static Button ref_btnCurrentAndVoltageTapLookupSave;

	private static Label ref_labelMeterType;
	private static ComboBox ref_cmbBoxMetertype;

	// private static Tab ref_tabCurrentMapping;
	private static Tab ref_tabVoltageAndCurrentMapping;

	// private static TableView ref_tableViewCurrentMapping;
	// private static TableView ref_tableViewVoltageAndCurrentMapping;
	private static TableView<RefStdPulseConstantModel> ref_tableViewVoltageAndCurrentMapping;

	private static TableColumn ref_tableColVoltageTapV1_Hdr;
	private static TableColumn ref_tableColVoltageTapV2_Hdr;
	private static TableColumn ref_tableColVoltageTapV3_Hdr;
	private static TableColumn ref_tableColVoltageTapV4_Hdr;
	private static TableColumn ref_tableColVoltageTapV5_Hdr;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		refAssignment();

		list_of_txt_fields.add(txt_32_mA);
		list_of_txt_fields.add(txt_65_mA);
		list_of_txt_fields.add(txt_131_mA);
		list_of_txt_fields.add(txt_262_mA);
		list_of_txt_fields.add(txt_524_mA);
		list_of_txt_fields.add(txt_1_A);
		list_of_txt_fields.add(txt_2_A);
		list_of_txt_fields.add(txt_4_A);
		list_of_txt_fields.add(txt_8_A);
		list_of_txt_fields.add(txt_16_A);
		list_of_txt_fields.add(txt_33_A);
		list_of_txt_fields.add(txt_67_A);
		list_of_txt_fields.add(txt_200_A);

		cmbBox_metertype.getItems().clear();
		cmbBox_metertype.getItems().addAll(ConstantApp.MeterType);
		cmbBox_metertype.getSelectionModel().select(0);

		guiObjectInit();
		hideGuiObjects();
		// setupCurrentLookupTableView();
		setupCurrentAndVoltageLookupTableView();
		// testDummyDataOnCurrentTable();

	}

	@SuppressWarnings("unchecked")
	public static void editFocusedCellOnTableViewVoltageAndCurrentMapping() {
		ApplicationLauncher.logger.debug("editFocusedCellOnTableViewVoltageAndCurrentMapping: Entry");
		final TablePosition<RefStdPulseConstantModel, ?> focusedCell = ref_tableViewVoltageAndCurrentMapping
				.focusModelProperty().get().focusedCellProperty().get();
		if (focusedCell.getTableColumn().isEditable()) {
			ApplicationLauncher.logger.debug("editFocusedCellOnTableViewVoltageAndCurrentMapping: Edit Hit");
			ref_tableViewVoltageAndCurrentMapping.edit(focusedCell.getRow(), focusedCell.getTableColumn());
		}
	}

	private TableColumn<RefStdPulseConstantModel, ?> getTableColumnOnTableViewVoltageAndCurrentMapping(
			final TableColumn<RefStdPulseConstantModel, ?> column, int offset) {
		ApplicationLauncher.logger.debug("getTableColumnOnTableViewVoltageAndCurrentMapping: Entry");
		int columnIndex = ref_tableViewVoltageAndCurrentMapping.getVisibleLeafIndex(column);
		int newColumnIndex = columnIndex + offset;
		return ref_tableViewVoltageAndCurrentMapping.getVisibleLeafColumn(newColumnIndex);
	}

	@SuppressWarnings("unchecked")
	private void selectPreviousOnTableViewVoltageAndCurrentMapping() {
		ApplicationLauncher.logger.debug("selectPreviousOnTableViewVoltageAndCurrentMapping: Entry");
		if (ref_tableViewVoltageAndCurrentMapping.getSelectionModel().isCellSelectionEnabled()) {
			// in cell selection mode, we have to wrap around, going from
			// right-to-left, and then wrapping to the end of the previous line
			TablePosition<RefStdPulseConstantModel, ?> pos = ref_tableViewVoltageAndCurrentMapping.getFocusModel()
					.getFocusedCell();
			if (pos.getColumn() - 1 >= 0) {
				// go to previous row
				ref_tableViewVoltageAndCurrentMapping.getSelectionModel().select(pos.getRow(),
						getTableColumnOnTableViewVoltageAndCurrentMapping(pos.getTableColumn(), -1));
			} else if (pos.getRow() < ref_tableViewVoltageAndCurrentMapping.getItems().size()) {
				// wrap to end of previous row
				ref_tableViewVoltageAndCurrentMapping.getSelectionModel().select(pos.getRow() - 1,
						ref_tableViewVoltageAndCurrentMapping.getVisibleLeafColumn(
								ref_tableViewVoltageAndCurrentMapping.getVisibleLeafColumns().size() - 1));
			}
		} else {
			int focusIndex = ref_tableViewVoltageAndCurrentMapping.getFocusModel().getFocusedIndex();
			if (focusIndex == -1) {
				ref_tableViewVoltageAndCurrentMapping.getSelectionModel()
						.select(ref_tableViewVoltageAndCurrentMapping.getItems().size() - 1);
			} else if (focusIndex > 0) {
				ref_tableViewVoltageAndCurrentMapping.getSelectionModel().select(focusIndex - 1);
			}
		}
	}

	private void selectAboveCellOnTableViewVoltageAndCurrentMapping() {
		ApplicationLauncher.logger.debug("selectAboveCellOnTableViewVoltageAndCurrentMapping: Entry");
		ref_tableViewVoltageAndCurrentMapping.getSelectionModel().getTableView().getSelectionModel().selectAboveCell();
	}

	private void selectBelowCellOnTableViewVoltageAndCurrentMapping() {
		ApplicationLauncher.logger.debug("selectBelowCellOnTableViewVoltageAndCurrentMapping: Entry");
		ref_tableViewVoltageAndCurrentMapping.getSelectionModel().getTableView().getSelectionModel().selectBelowCell();
	}

	@SuppressWarnings("unchecked")
	private void setupCurrentAndVoltageLookupTableView() {

		ref_tableViewVoltageAndCurrentMapping.setEditable(true);

		ref_tableViewVoltageAndCurrentMapping.getSelectionModel().cellSelectionEnabledProperty().set(true);
		EditCellRefStdPulseConstVoltageAndCurrent.setPresentActiveTable(ref_tableViewVoltageAndCurrentMapping);
		// when character or numbers pressed it will start edit in editable
		// fields

		ref_tableViewVoltageAndCurrentMapping.setOnKeyPressed(event -> {
			if (event.getCode().isLetterKey() || event.getCode().isDigitKey()) {
				ApplicationLauncher.logger.debug("setupCurrentAndVoltageLookupTableView: editFocusedCell");
				editFocusedCellOnTableViewVoltageAndCurrentMapping();
			} else if (event.getCode() == KeyCode.RIGHT
					|| ((event.getCode() == KeyCode.TAB) && (!event.isShiftDown()))) {
				ApplicationLauncher.logger.debug("setupCurrentAndVoltageLookupTableView: Tab");
				ref_tableViewVoltageAndCurrentMapping.getSelectionModel().selectNext();
				editFocusedCellOnTableViewVoltageAndCurrentMapping();
				event.consume();
				// } else if ( (event.getCode() == KeyCode.LEFT ) || ( (event.getCode() == (
				// KeyCode.SHIFT )) || (event.getCode() == KeyCode.TAB) ) ) {
			} else if ((event.getCode() == KeyCode.LEFT) || (event.getCode() == KeyCode.TAB && (event.isShiftDown()))) {
				// work around due to
				// TableView.getSelectionModel().selectPrevious() due to a bug
				// stopping it from working on
				// the first column in the last row of the table
				ApplicationLauncher.logger.debug("setupCurrentAndVoltageLookupTableView: Left");
				selectPreviousOnTableViewVoltageAndCurrentMapping();
				editFocusedCellOnTableViewVoltageAndCurrentMapping();
				event.consume();
			} else if (event.getCode() == KeyCode.UP) {
				ApplicationLauncher.logger.debug("setupCurrentAndVoltageLookupTableView: UP");
				selectAboveCellOnTableViewVoltageAndCurrentMapping();
				// ref_tableViewVoltageAndCurrentMapping.getSelectionModel().getTableView().getSelectionModel().selectAboveCell();
				editFocusedCellOnTableViewVoltageAndCurrentMapping();
				event.consume();
			} else if (event.getCode() == KeyCode.DOWN) {
				ApplicationLauncher.logger.debug("setupCurrentAndVoltageLookupTableView: Down");
				selectBelowCellOnTableViewVoltageAndCurrentMapping();
				// ref_tableViewVoltageAndCurrentMapping.getSelectionModel().getTableView().getSelectionModel().selectBelowCell();
				editFocusedCellOnTableViewVoltageAndCurrentMapping();
				event.consume();
			}
		});

		/*
		 * Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn,
		 * TableCell>() {
		 * public TableCell call(TableColumn p) {
		 * return new EditingCell();
		 * }
		 * };
		 */

		/*
		 * Callback<TableColumn<RefStdPulseConstantModel, String>,
		 * TableCell<RefStdPulseConstantModel, String>> cellFactory = new
		 * Callback<TableColumn<RefStdPulseConstantModel, String>,
		 * TableCell<RefStdPulseConstantModel, String>>() {
		 * 
		 * @Override
		 * public TableCell<RefStdPulseConstantModel, String>
		 * call(TableColumn<RefStdPulseConstantModel, String> paramTableColumn) {
		 * return new EditCell<RefStdPulseConstantModel, String>( new
		 * DefaultStringConverter()) {
		 * 
		 * @Override
		 * public void updateItem(String s, boolean b) {
		 * super.updateItem(s, b);
		 * 
		 * if (! isEmpty()) {
		 * RefStdPulseConstantModel item = getTableView().getItems().get(getIndex());
		 * // Test for disable condition
		 * if (item != null){// && !item.isDummy()) {
		 * setEditable(false);
		 * //getStylesheets().add("/styles/ParameterTableView.css");
		 * } else {
		 * setEditable(true);
		 * //setStyle("");
		 * }
		 * }
		 * }
		 * };
		 * }
		 * };
		 */

		tableColVoltageTapSerialNo.setCellValueFactory(cellData -> cellData.getValue().getSerialNoProperty());
		/*
		 * tableColVoltageTapSerialNo.setCellValueFactory(new
		 * PropertyValueFactory<RefStdPulseConstantModel, String>("serialNo"));
		 * //tableColVoltageTapSerialNo.setCellFactory(cellFactory);
		 * tableColVoltageTapSerialNo.setCellFactory(EditCell.<RefStdPulseConstantModel,
		 * String>forTableColumn( new DefaultStringConverter()));
		 * tableColVoltageTapSerialNo.setOnEditCommit(new
		 * EventHandler<CellEditEvent<RefStdPulseConstantModel, String>>() {
		 * 
		 * @Override
		 * public void handle(CellEditEvent<RefStdPulseConstantModel, String> t) {
		 * ((RefStdPulseConstantModel) t.getTableView().getItems()
		 * .get(t.getTablePosition().getRow()))
		 * .setSerialNo(t.getNewValue());
		 * }
		 * });
		 */

		tableColVoltageCurrentTapValue
				.setCellValueFactory(cellData -> cellData.getValue().getCurrentTapValueProperty());
		/*
		 * tableColVoltageCurrentTapValue.setCellValueFactory(new
		 * PropertyValueFactory<RefStdPulseConstantModel, String>("currentTapValue"));
		 * //tableColVoltageCurrentTapValue.setCellFactory(cellFactory);
		 * tableColVoltageCurrentTapValue.setCellFactory(EditCell.<
		 * RefStdPulseConstantModel, String>forTableColumn( new
		 * DefaultStringConverter()));
		 * 
		 * tableColVoltageCurrentTapValue.setOnEditCommit(new
		 * EventHandler<CellEditEvent<RefStdPulseConstantModel, String>>() {
		 * 
		 * @Override
		 * public void handle(CellEditEvent<RefStdPulseConstantModel, String> t) {
		 * ((RefStdPulseConstantModel) t.getTableView().getItems()
		 * .get(t.getTablePosition().getRow()))
		 * .setCurrentTapValue(t.getNewValue());
		 * }
		 * });
		 */

		// tableColVoltageTapOperation.setCellValueFactory(new
		// RefStdOperationVoltageComboBoxValueFactory());
		tableColVoltageTapOperation.setCellValueFactory(cellData -> cellData.getValue().getOperationProperty());

		tableColVoltageTapConstantV1_InWh
				.setCellValueFactory(new PropertyValueFactory<RefStdPulseConstantModel, Integer>("constantInV1_Wh"));
		tableColVoltageTapConstantV1_InWh.setCellFactory(EditCellRefStdPulseConstVoltageAndCurrent
				.<RefStdPulseConstantModel, Integer>forTableColumn(new MyIntegerStringConverter()));
		tableColVoltageTapConstantV1_InWh
				.setOnEditCommit(new EventHandler<CellEditEvent<RefStdPulseConstantModel, Integer>>() {
					public void handle(CellEditEvent<RefStdPulseConstantModel, Integer> t) {

						ApplicationLauncher.logger.info("setupCurrentAndVoltageLookupTableView : Onchange1");
						RefStdPulseConstantModel rowData = ((RefStdPulseConstantModel) t.getTableView().getItems()
								.get(t.getTablePosition().getRow()));
						/*
						 * if(t. == KeyCode.TAB){
						 * 
						 * }
						 */
						try {
							rowData.setConstantInV1_Wh(t.getNewValue());
						} catch (Exception e) {

							e.printStackTrace();
							ApplicationLauncher.logger
									.error("setupCurrentAndVoltageLookupTableView: Pulse constant1: Exception: "
											+ e.getMessage());
							WindowManager.InformUser("Incorrect decimal Value",
									"Kindly enter valid input decimal pulse constant1", AlertType.ERROR);
							rowData.setConstantInV1_Wh(t.getOldValue());
							ref_tableViewVoltageAndCurrentMapping.refresh();
							// setGraphic(null);
							// setContentDisplay(ContentDisplay.TEXT_ONLY);
						}
					}
				});

		tableColVoltageTapConstantV2_InWh
				.setCellValueFactory(new PropertyValueFactory<RefStdPulseConstantModel, Integer>("constantInV2_Wh"));
		tableColVoltageTapConstantV2_InWh.setCellFactory(EditCellRefStdPulseConstVoltageAndCurrent
				.<RefStdPulseConstantModel, Integer>forTableColumn(new MyIntegerStringConverter()));
		tableColVoltageTapConstantV2_InWh
				.setOnEditCommit(new EventHandler<CellEditEvent<RefStdPulseConstantModel, Integer>>() {
					public void handle(CellEditEvent<RefStdPulseConstantModel, Integer> t) {

						ApplicationLauncher.logger.info("setupCurrentAndVoltageLookupTableView : Onchange2");
						RefStdPulseConstantModel rowData = ((RefStdPulseConstantModel) t.getTableView().getItems()
								.get(t.getTablePosition().getRow()));
						try {
							rowData.setConstantInV2_Wh(t.getNewValue());
						} catch (Exception e) {

							e.printStackTrace();
							ApplicationLauncher.logger
									.error("setupCurrentAndVoltageLookupTableView: Pulse constant2: Exception: "
											+ e.getMessage());
							WindowManager.InformUser("Incorrect decimal Value",
									"Kindly enter valid input decimal pulse constant2", AlertType.ERROR);
							rowData.setConstantInV2_Wh(t.getOldValue());
							ref_tableViewVoltageAndCurrentMapping.refresh();
						}
					}
				});

		tableColVoltageTapConstantV3_InWh
				.setCellValueFactory(new PropertyValueFactory<RefStdPulseConstantModel, Integer>("constantInV3_Wh"));
		tableColVoltageTapConstantV3_InWh.setCellFactory(EditCellRefStdPulseConstVoltageAndCurrent
				.<RefStdPulseConstantModel, Integer>forTableColumn(new MyIntegerStringConverter()));
		tableColVoltageTapConstantV3_InWh
				.setOnEditCommit(new EventHandler<CellEditEvent<RefStdPulseConstantModel, Integer>>() {
					public void handle(CellEditEvent<RefStdPulseConstantModel, Integer> t) {

						ApplicationLauncher.logger.info("setupCurrentAndVoltageLookupTableView : Onchange3");
						RefStdPulseConstantModel rowData = ((RefStdPulseConstantModel) t.getTableView().getItems()
								.get(t.getTablePosition().getRow()));
						try {
							rowData.setConstantInV3_Wh(t.getNewValue());
						} catch (Exception e) {

							e.printStackTrace();
							ApplicationLauncher.logger
									.error("setupCurrentAndVoltageLookupTableView: Pulse constant3: Exception: "
											+ e.getMessage());
							WindowManager.InformUser("Incorrect decimal Value",
									"Kindly enter valid input decimal pulse constant3", AlertType.ERROR);
							rowData.setConstantInV3_Wh(t.getOldValue());
							ref_tableViewVoltageAndCurrentMapping.refresh();
						}
					}
				});

		tableColVoltageTapConstantV4_InWh
				.setCellValueFactory(new PropertyValueFactory<RefStdPulseConstantModel, Integer>("constantInV4_Wh"));
		tableColVoltageTapConstantV4_InWh.setCellFactory(EditCellRefStdPulseConstVoltageAndCurrent
				.<RefStdPulseConstantModel, Integer>forTableColumn(new MyIntegerStringConverter()));
		tableColVoltageTapConstantV4_InWh
				.setOnEditCommit(new EventHandler<CellEditEvent<RefStdPulseConstantModel, Integer>>() {
					public void handle(CellEditEvent<RefStdPulseConstantModel, Integer> t) {

						ApplicationLauncher.logger.info("setupCurrentAndVoltageLookupTableView : Onchange4");
						RefStdPulseConstantModel rowData = ((RefStdPulseConstantModel) t.getTableView().getItems()
								.get(t.getTablePosition().getRow()));
						try {
							rowData.setConstantInV4_Wh(t.getNewValue());
						} catch (Exception e) {

							e.printStackTrace();
							ApplicationLauncher.logger
									.error("setupCurrentAndVoltageLookupTableView: Pulse constant4: Exception: "
											+ e.getMessage());
							WindowManager.InformUser("Incorrect decimal Value",
									"Kindly enter valid input decimal pulse constant4", AlertType.ERROR);
							rowData.setConstantInV4_Wh(t.getOldValue());
							ref_tableViewVoltageAndCurrentMapping.refresh();
						}
					}
				});

		tableColVoltageTapConstantV5_InWh
				.setCellValueFactory(new PropertyValueFactory<RefStdPulseConstantModel, Integer>("constantInV5_Wh"));
		tableColVoltageTapConstantV5_InWh.setCellFactory(EditCellRefStdPulseConstVoltageAndCurrent
				.<RefStdPulseConstantModel, Integer>forTableColumn(new MyIntegerStringConverter()));
		tableColVoltageTapConstantV5_InWh
				.setOnEditCommit(new EventHandler<CellEditEvent<RefStdPulseConstantModel, Integer>>() {
					public void handle(CellEditEvent<RefStdPulseConstantModel, Integer> t) {

						ApplicationLauncher.logger.info("setupCurrentAndVoltageLookupTableView : Onchange5");
						RefStdPulseConstantModel rowData = ((RefStdPulseConstantModel) t.getTableView().getItems()
								.get(t.getTablePosition().getRow()));
						try {
							rowData.setConstantInV5_Wh(t.getNewValue());
						} catch (Exception e) {

							e.printStackTrace();
							ApplicationLauncher.logger
									.error("setupCurrentAndVoltageLookupTableView: Pulse constant5: Exception: "
											+ e.getMessage());
							WindowManager.InformUser("Incorrect decimal Value",
									"Kindly enter valid input decimal pulse constant5", AlertType.ERROR);
							rowData.setConstantInV5_Wh(t.getOldValue());
							ref_tableViewVoltageAndCurrentMapping.refresh();
						}
					}
				});

	}

	private void testDummyDataOnCurrentTable() {
		try {

			RefStdPulseConstantModel dataModel = new RefStdPulseConstantModel();
			dataModel.setSerialNo("1");
			dataModel.setCurrentTapValue("TapValue");
			dataModel.setOperation(ConstantApp.REF_STD_OPERATION_CURRENT_ABOVE_OR_EQUAL);
			// dataModel.setConstantInV1_Wh("V1-Hr");
			dataModel.setConstantInV1_Wh(1);
			ref_tableViewCurrentMappingOnly.getItems().add(dataModel);
			RefStdPulseConstantModel dataModel2 = new RefStdPulseConstantModel();
			dataModel2.setSerialNo("1");
			dataModel2.setCurrentTapValue("TapValue");
			/*
			 * dataModel2.setConstantInV1_Wh("V1-Hr");
			 * dataModel2.setConstantInV2_Wh("V2-Hr");
			 * dataModel2.setConstantInV3_Wh("V3-Hr");
			 * dataModel2.setConstantInV4_Wh("V4-Hr");
			 * dataModel2.setConstantInV5_Wh("V5-Hr");
			 */
			dataModel2.setConstantInV1_Wh(1);
			dataModel2.setConstantInV2_Wh(2);
			dataModel2.setConstantInV3_Wh(3);
			dataModel2.setConstantInV4_Wh(4);
			dataModel2.setConstantInV5_Wh(5);
			dataModel2.setOperation(ConstantApp.REF_STD_OPERATION_VOLTAGE_ABOVE_OR_EQUAL);
			ref_tableViewVoltageAndCurrentMapping.getItems().add(dataModel2);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("testDummyDataOnCurrentTable: Exception: " + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public static void editFocusedCellOnTableViewCurrentMapping() {
		ApplicationLauncher.logger.debug("editFocusedCellOnTableViewCurrentMapping: Entry");
		final TablePosition<RefStdPulseConstantModel, ?> focusedCell = ref_tableViewCurrentMappingOnly
				.focusModelProperty().get().focusedCellProperty().get();
		if (focusedCell.getTableColumn().isEditable()) {
			ApplicationLauncher.logger.debug("editFocusedCellOnTableViewCurrentMapping: Edit Hit");
			ref_tableViewCurrentMappingOnly.edit(focusedCell.getRow(), focusedCell.getTableColumn());
		}
	}

	private TableColumn<RefStdPulseConstantModel, ?> getTableColumnOnTableViewCurrentMapping(
			final TableColumn<RefStdPulseConstantModel, ?> column, int offset) {
		ApplicationLauncher.logger.debug("getTableColumnOnTableViewCurrentMapping: Entry");
		int columnIndex = ref_tableViewCurrentMappingOnly.getVisibleLeafIndex(column);
		int newColumnIndex = columnIndex + offset;
		return ref_tableViewCurrentMappingOnly.getVisibleLeafColumn(newColumnIndex);
	}

	@SuppressWarnings("unchecked")
	private void selectPreviousOnTableViewCurrentMapping() {
		ApplicationLauncher.logger.debug("selectPreviousOnTableViewCurrentMapping: Entry");
		if (ref_tableViewCurrentMappingOnly.getSelectionModel().isCellSelectionEnabled()) {
			// in cell selection mode, we have to wrap around, going from
			// right-to-left, and then wrapping to the end of the previous line
			TablePosition<RefStdPulseConstantModel, ?> pos = ref_tableViewCurrentMappingOnly.getFocusModel()
					.getFocusedCell();
			if (pos.getColumn() - 1 >= 0) {
				// go to previous row
				ref_tableViewCurrentMappingOnly.getSelectionModel().select(pos.getRow(),
						getTableColumnOnTableViewCurrentMapping(pos.getTableColumn(), -1));
			} else if (pos.getRow() < ref_tableViewCurrentMappingOnly.getItems().size()) {
				// wrap to end of previous row
				ref_tableViewCurrentMappingOnly.getSelectionModel().select(pos.getRow() - 1,
						ref_tableViewCurrentMappingOnly.getVisibleLeafColumn(
								ref_tableViewCurrentMappingOnly.getVisibleLeafColumns().size() - 1));
			}
		} else {
			int focusIndex = ref_tableViewCurrentMappingOnly.getFocusModel().getFocusedIndex();
			if (focusIndex == -1) {
				ref_tableViewCurrentMappingOnly.getSelectionModel()
						.select(ref_tableViewCurrentMappingOnly.getItems().size() - 1);
			} else if (focusIndex > 0) {
				ref_tableViewCurrentMappingOnly.getSelectionModel().select(focusIndex - 1);
			}
		}
	}

	private void selectAboveCellOnTableViewCurrentMapping() {
		ApplicationLauncher.logger.debug("selectAboveCellOnTableViewCurrentMapping: Entry");
		ref_tableViewCurrentMappingOnly.getSelectionModel().getTableView().getSelectionModel().selectAboveCell();
	}

	private void selectBelowCellOnTableViewCurrentMapping() {
		ApplicationLauncher.logger.debug("selectBelowCellOnTableViewCurrentMapping: Entry");
		ref_tableViewCurrentMappingOnly.getSelectionModel().getTableView().getSelectionModel().selectBelowCell();
	}

	/*
	 * public void setupCurrentLookupTableView(){
	 * //ref_tableViewUac.setEditable(true);
	 * 
	 * ref_tableViewCurrentMappingOnly.setEditable(true);
	 * ref_tableViewCurrentMappingOnly.getSelectionModel().
	 * cellSelectionEnabledProperty().set(true);
	 * 
	 * ref_tableViewCurrentMappingOnly.setOnKeyPressed(event -> {
	 * if (event.getCode().isLetterKey() || event.getCode().isDigitKey()) {
	 * ApplicationLauncher.logger.
	 * debug("setupCurrentLookupTableView: editFocusedCell");
	 * editFocusedCellOnTableViewCurrentMapping();
	 * } else if (event.getCode() == KeyCode.RIGHT
	 * || ((event.getCode() == KeyCode.TAB) && (!event.isShiftDown()) )) {
	 * ApplicationLauncher.logger.debug("setupCurrentLookupTableView: Tab");
	 * ref_tableViewCurrentMappingOnly.getSelectionModel().selectNext();
	 * editFocusedCellOnTableViewCurrentMapping();
	 * event.consume();
	 * //} else if ( (event.getCode() == KeyCode.LEFT ) || ( (event.getCode() == (
	 * KeyCode.SHIFT )) || (event.getCode() == KeyCode.TAB) ) ) {
	 * } else if ( (event.getCode() == KeyCode.LEFT ) || ( event.getCode() ==
	 * KeyCode.TAB && (event.isShiftDown()) ) ) {
	 * // work around due to
	 * // TableView.getSelectionModel().selectPrevious() due to a bug
	 * // stopping it from working on
	 * // the first column in the last row of the table
	 * ApplicationLauncher.logger.debug("setupCurrentLookupTableView: Left");
	 * selectPreviousOnTableViewCurrentMapping();
	 * editFocusedCellOnTableViewCurrentMapping();
	 * event.consume();
	 * } else if (event.getCode() == KeyCode.UP) {
	 * ApplicationLauncher.logger.debug("setupCurrentLookupTableView: UP");
	 * selectAboveCellOnTableViewCurrentMapping();
	 * //ref_tableViewVoltageAndCurrentMapping.getSelectionModel().getTableView().
	 * getSelectionModel().selectAboveCell();
	 * editFocusedCellOnTableViewCurrentMapping();
	 * event.consume();
	 * } else if (event.getCode() == KeyCode.DOWN) {
	 * ApplicationLauncher.logger.debug("setupCurrentLookupTableView: Down");
	 * selectBelowCellOnTableViewCurrentMapping();
	 * //ref_tableViewVoltageAndCurrentMapping.getSelectionModel().getTableView().
	 * getSelectionModel().selectBelowCell();
	 * editFocusedCellOnTableViewCurrentMapping();
	 * event.consume();
	 * }
	 * });
	 * 
	 * tableColCurrentTapSerialNo.setCellValueFactory(cellData ->
	 * cellData.getValue().getSerialNoProperty());
	 * tableColCurrentTapValue.setCellValueFactory(cellData ->
	 * cellData.getValue().getCurrentTapValueProperty());
	 * //tableColCurrentTapOperation.setCellValueFactory(new
	 * RefStdOperationCurrentComboBoxValueFactory());
	 * 
	 * if(validateTableViewEditWithComboBoxType){
	 * tableColCurrentTapOperation.setCellValueFactory(new
	 * RefStdOperationCurrentComboBoxValueFactory());
	 * }else{
	 * tableColCurrentTapOperation.setCellValueFactory(cellData ->
	 * cellData.getValue().getOperationProperty());
	 * //}
	 * tableColCurrentTapConstantV1_InWh.setCellValueFactory(new
	 * PropertyValueFactory<RefStdPulseConstantModel, Integer>("constantInV1_Wh"));
	 * tableColCurrentTapConstantV1_InWh.setCellFactory(
	 * EditCellRefStdPulseConstCurrentOnly.<RefStdPulseConstantModel,
	 * Integer>forTableColumn(new MyIntegerStringConverter()));
	 * tableColCurrentTapConstantV1_InWh.setOnEditCommit(new
	 * EventHandler<CellEditEvent<RefStdPulseConstantModel, Integer>>() {
	 * public void handle(CellEditEvent<RefStdPulseConstantModel, Integer> t) {
	 * 
	 * ApplicationLauncher.logger.info("setupCurrentLookupTableView : Onchange");
	 * RefStdPulseConstantModel rowData = ((RefStdPulseConstantModel)
	 * t.getTableView().getItems().get(t.getTablePosition().getRow()));
	 * try{
	 * rowData.setConstantInV1_Wh(t.getNewValue());
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.
	 * error("setupCurrentLookupTableView: Pulse constant: Exception: " +
	 * e.getMessage());
	 * WindowManager.InformUser("Incorrect decimal Value",
	 * "Kindly enter valid input decimal pulse constant" ,AlertType.ERROR);
	 * rowData.setConstantInV1_Wh(t.getOldValue());
	 * ref_tableViewCurrentMappingOnly.refresh();
	 * }
	 * }
	 * });
	 * 
	 * 
	 * //refreshData();
	 * 
	 * }
	 */

	private void hideGuiObjects() {

		Platform.runLater(() -> {
			ref_rdbtnMilliAmps.setVisible(false);
			ref_rdbtnAmps.setVisible(false);
			ref_rdbtnMilliVolts.setVisible(false);
			ref_rdbtnVolts.setVisible(false);
			tableColVoltageTapConstantV2_InWh.setVisible(false);
			tableColVoltageTapConstantV3_InWh.setVisible(false);
			tableColVoltageTapConstantV4_InWh.setVisible(false);
			tableColVoltageTapConstantV5_InWh.setVisible(false);

			ref_tableColVoltageTapV2_Hdr.setVisible(false);
			ref_tableColVoltageTapV3_Hdr.setVisible(false);
			ref_tableColVoltageTapV4_Hdr.setVisible(false);
			ref_tableColVoltageTapV5_Hdr.setVisible(false);

			ref_rdBtnCurrentMappingOnly.setVisible(false);
			ref_rdBtnVoltageAndCurrentMapping.setVisible(false);

			// ref_tabCurrentMapping.setVisible(false);
		});
	}

	private void guiObjectInit() {

		Platform.runLater(() -> {
			ref_titledPaneModelSection.setExpanded(true);
			ref_rdbtnMilliAmps.setSelected(false);
			ref_rdbtnAmps.setSelected(true);
			ref_rdbtnMilliVolts.setSelected(false);
			ref_rdbtnVolts.setSelected(true);
			ref_rdBtnCurrentMappingOnly.setSelected(true);
			ref_rdBtnVoltageAndCurrentMapping.setSelected(true);
			ref_titledPaneCurrentTap.setDisable(false);
			ref_titledPaneVoltageTap.setDisable(false);
			ref_txtNoOfCurrentTap.setText("3");
			ref_txtNoOfVoltageTap.setText("4");
			if (ProcalFeatureEnable.KRE_REFSTD_CONNECTED) {
				ref_cmBxRefStdModel.getItems().add(ConstantApp.REF_STD_TYPE_KRE_9XXX);
				ref_cmBxRefStdModel.getSelectionModel().select(0);
			} else {
				ref_cmBxRefStdModel.getItems().add("Select");
				ref_cmBxRefStdModel.getSelectionModel().select(0);
			}

			ref_cmbBoxMetertype.getItems().addAll(ConstantApp.REF_STD_METER_PULSE_TYPE_LIST);
			ref_cmbBoxMetertype.getSelectionModel().select(0);

			// ref_tabCurrentMapping.setDisable(false);
			// ref_tabVoltageAndCurrentMapping.setDisable(true);
			ref_tabPaneTapMapping.getSelectionModel().select(0);

			ref_listViewCurrentTap.getItems().add("10");
			ref_listViewCurrentTap.getItems().add("20");
			ref_listViewCurrentTap.getItems().add("0.02");

			ref_listViewVoltageTap.getItems().add("120");
			ref_listViewVoltageTap.getItems().add("60");
			ref_listViewVoltageTap.getItems().add("240");
			ref_listViewVoltageTap.getItems().add("480");

			// ref_btnCurrentTapLookupSave.setVisible(true);
			// ref_btnCurrentAndVoltageTapLookupSave.setVisible(false);
		});

		setVoltageAndCurrentTapSelected(true);
		// setVoltageAndCurrentTapSelected(false);
	}

	private void refAssignment() {

		ref_cmBxRefStdModel = cmBxRefStdModel;
		ref_rdBtnCurrentMappingOnly = rdBtnCurrentMappingOnly;
		ref_rdBtnVoltageAndCurrentMapping = rdBtnVoltageAndCurrentMapping;
		ref_rdbtnMilliAmps = rdbtnMilliAmps;
		ref_rdbtnAmps = rdbtnAmps;
		ref_rdbtnMilliVolts = rdbtnMilliVolts;
		ref_rdbtnVolts = rdbtnVolts;
		ref_txtNoOfCurrentTap = txtNoOfCurrentTap;
		ref_txtNoOfVoltageTap = txtNoOfVoltageTap;
		ref_txtCurrentValue = txtCurrentValue;
		ref_txtVoltageValue = txtVoltageValue;

		ref_titledPaneCurrentTap = titledPaneCurrentTap;
		ref_titledPaneVoltageTap = titledPaneVoltageTap;

		ref_listViewCurrentTap = listViewCurrentTap;
		ref_listViewVoltageTap = listViewVoltageTap;

		ref_btnModelSectionNext = btnModelSectionNext;
		ref_btnCurrentAndVoltageTapLookupSave = btnCurrentAndVoltageTapLookupSave;
		// ref_btnCurrentTapLookupSave = btnCurrentTapLookupSave;

		ref_labelMeterType = labelMeterType;
		ref_cmbBoxMetertype = cmbBoxMetertype;

		// ref_tabCurrentMapping = tabCurrentMapping;
		ref_tabVoltageAndCurrentMapping = tabVoltageAndCurrentMapping;

		// ref_tableViewCurrentMapping = tableViewCurrentMapping;
		ref_tableViewCurrentMappingOnly = tableViewCurrentMappingOnly;
		ref_tableViewVoltageAndCurrentMapping = tableViewVoltageAndCurrentMapping;

		/*
		 * ref_tableColumnV1 = tableColumnV1;
		 * ref_tableColumnV2 = tableColumnV2;
		 * ref_tableColumnV3 = tableColumnV3;
		 * ref_tableColumnV4 = tableColumnV4;
		 * ref_tableColumnV5 = tableColumnV5;
		 */

		ref_tableColVoltageTapV1_Hdr = tableColVoltageTapV1_Hdr;
		ref_tableColVoltageTapV2_Hdr = tableColVoltageTapV2_Hdr;
		ref_tableColVoltageTapV3_Hdr = tableColVoltageTapV3_Hdr;
		ref_tableColVoltageTapV4_Hdr = tableColVoltageTapV4_Hdr;
		ref_tableColVoltageTapV5_Hdr = tableColVoltageTapV5_Hdr;

		ref_titledPaneModelSection = titledPaneModelSection;
		ref_titledPaneLookupTable = titledPaneLookupTable;
		ref_tabPaneTapMapping = tabPaneTapMapping;

	}

	public void MeterTypeOnChange() {
		ApplicationLauncher.logger.info("MeterTypeOnChange:  Refresh");
		LoadConstValues();
	}

	public void LoadConstValues() {
		String meter_type = cmbBox_metertype.getValue();
		JSONObject result = MySQL_Controller.sp_getref_std_const(meter_type);
		try {
			JSONArray const_values_arr = result.getJSONArray("Const_Values");
			if (!const_values_arr.isNull(0)) {
				JSONObject jobj = new JSONObject();
				String tap_name = "";
				String const_value = "";
				int tap = 0;
				for (int i = 0; i < const_values_arr.length(); i++) {
					jobj = const_values_arr.getJSONObject(i);
					tap_name = jobj.getString("tap_name");
					const_value = jobj.getString("const_value");
					tap = ConstantRefStdRadiant.REF_STD_TAP_NAMES.indexOf(tap_name);
					TextField txt_field = list_of_txt_fields.get(tap);
					txt_field.setText(const_value);
				}
			} else {
				for (int i = 0; i < list_of_txt_fields.size(); i++) {
					TextField txt_field = list_of_txt_fields.get(i);
					txt_field.clear();
				}
			}
		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("LoadConstValues: JSONException:" + e.getMessage());
		}
	}

	public ArrayList<String> getConstValues() {
		ArrayList<String> const_Values = new ArrayList<String>();
		for (int i = 0; i < list_of_txt_fields.size(); i++) {
			TextField txt_field = list_of_txt_fields.get(i);
			if (!txt_field.getText().isEmpty()) {
				const_Values.add(txt_field.getText());
			}
		}
		return const_Values;
	}

	public void SaveRefStdConstOnClick() {
		ArrayList<String> const_Values = getConstValues();
		int sizeof_constvalues = const_Values.size();
		int expected_size = list_of_txt_fields.size();
		String meter_type = cmbBox_metertype.getValue();
		if (sizeof_constvalues == expected_size) {
			for (int i = 0; i < const_Values.size(); i++) {
				if (GuiUtils.is_long(const_Values.get(i))) {
					String tap_name = ConstantRefStdRadiant.REF_STD_TAP_NAMES.get(i);
					MySQL_Controller.sp_add_ref_std_const(meter_type, tap_name,
							const_Values.get(i));
				}
			}
		}
		InformUser("Save success", "Data saved successfully", AlertType.INFORMATION);
	}

	public static void InformUser(String title, String info, AlertType Alert_type) {
		TextBoxDialog TextBoxDialogobj = new TextBoxDialog();
		TextBoxDialogobj.TriggerUserInfoPlatFormLater(title, info, Alert_type);
	}

	@FXML
	public void appendCurrentValueToList() {

		String value = ref_txtCurrentValue.getText();
		if ((GuiUtils.is_float(value)) && (!value.isEmpty())) {
			if (isCurrentDataInAcceptableRange(value)) {
				if (ref_rdbtnMilliAmps.isSelected()) {
					value = value + " mA";
				}
				appendCurrentValue(value);
			} else {
				ApplicationLauncher.logger.debug("appendCurrentValueToList:  value1: " + value);
				ApplicationLauncher.logger
						.debug("appendCurrentValueToList:  MINIMUM_ACCEPTED_CURRENT: " + MINIMUM_ACCEPTED_CURRENT);
				ApplicationLauncher.logger
						.debug("appendCurrentValueToList:  MAXIMUM_ACCEPTED_CURRENT: " + MAXIMUM_ACCEPTED_CURRENT);
				WindowManager.InformUser("Error", "Kindly enter valid current value between " + MINIMUM_ACCEPTED_CURRENT
						+ " and " + MAXIMUM_ACCEPTED_CURRENT, AlertType.ERROR);
			}
		} else {
			ApplicationLauncher.logger.debug("appendCurrentValueToList:  value2: " + value);
			WindowManager.InformUser("Error", "Kindly enter valid current value", AlertType.ERROR);
		}

	}

	private boolean isCurrentDataInAcceptableRange(String value) {

		float currentValue = Float.parseFloat(value);
		if (ref_rdbtnMilliAmps.isSelected()) {
			currentValue = currentValue / 1000;
		}

		ApplicationLauncher.logger.debug("isCurrentDataInAcceptableRange:  currentValue: " + currentValue);
		if ((currentValue >= MINIMUM_ACCEPTED_CURRENT) && (currentValue <= MAXIMUM_ACCEPTED_CURRENT)) {
			ApplicationLauncher.logger.debug("isCurrentDataInAcceptableRange:  currentValue: in range");
			return true;
		}

		ApplicationLauncher.logger.debug("isCurrentDataInAcceptableRange:  currentValue: out of range");
		return false;
	}

	@FXML
	public void removeCurrentValueFromList() {
		// ObservableList<String> current = ref_listViewCurrentTap.getItems();
		if (ref_listViewCurrentTap.getItems().size() > 0) {
			String rem_current = ref_listViewCurrentTap.getSelectionModel().getSelectedItem();
			ApplicationLauncher.logger.debug("removeCurrentValueFromList:  rem_current: " + rem_current);
			// current.remove(rem_current);
			Platform.runLater(() -> {
				ref_listViewCurrentTap.getItems().remove(rem_current);
			});
		}
	}

	public void appendCurrentValue(String value) {

		ObservableList<String> current = ref_listViewCurrentTap.getItems();

		if (!checkCurrentDataexists(value)) {
			Platform.runLater(() -> {
				ref_listViewCurrentTap.getItems().add(value);
				ref_txtCurrentValue.clear();
				ref_txtCurrentValue.requestFocus();
			});
		}

	}

	public static boolean checkCurrentDataexists(String value) {
		boolean dataexists = false;
		ObservableList<String> existing_data = ref_listViewCurrentTap.getItems();
		if (existing_data.size() != 0) {
			String data = "";
			for (int i = 0; i < existing_data.size(); i++) {
				data = existing_data.get(i);
				if (value.equals(data)) {
					dataexists = true;
					break;
				} else {
					dataexists = false;
				}
			}
		} else {
			dataexists = false;
		}
		return dataexists;
	}

	@FXML
	public void rdbtnMilliAmpsOnChange() {

		if (ref_rdbtnMilliAmps.isSelected()) {
			ref_rdbtnAmps.setSelected(false);
		} else {
			ref_rdbtnMilliAmps.setSelected(true);
		}

	}

	@FXML
	public void rdbtnAmpsOnChange() {

		if (ref_rdbtnAmps.isSelected()) {
			ref_rdbtnMilliAmps.setSelected(false);
		} else {
			ref_rdbtnAmps.setSelected(true);
		}

	}

	@FXML
	public void rdbtnMilliVoltsOnChange() {

		if (ref_rdbtnMilliVolts.isSelected()) {
			ref_rdbtnVolts.setSelected(false);
		} else {
			ref_rdbtnMilliVolts.setSelected(true);
		}

	}

	@FXML
	public void rdbtnVoltsOnChange() {

		if (ref_rdbtnVolts.isSelected()) {
			ref_rdbtnMilliVolts.setSelected(false);
		} else {
			ref_rdbtnVolts.setSelected(true);
		}

	}

	@FXML
	public void rdBtnCurrentMappingOnlyOnChange() {

		if (ref_rdBtnCurrentMappingOnly.isSelected()) {
			ref_rdBtnVoltageAndCurrentMapping.setSelected(false);
			ref_titledPaneCurrentTap.setDisable(false);
			ref_titledPaneVoltageTap.setDisable(true);
			setVoltageAndCurrentTapSelected(false);
		} else {
			ref_rdBtnCurrentMappingOnly.setSelected(true);
		}

	}

	@FXML
	public void rdBtnVoltageAndCurrentMappingOnChange() {

		if (ref_rdBtnVoltageAndCurrentMapping.isSelected()) {
			ref_rdBtnCurrentMappingOnly.setSelected(false);
			ref_titledPaneCurrentTap.setDisable(false);
			ref_titledPaneVoltageTap.setDisable(false);
			setVoltageAndCurrentTapSelected(true);
		} else {
			ref_rdBtnVoltageAndCurrentMapping.setSelected(true);
		}

	}

	@FXML
	public void MoveUpCurrentOnClick() {
		ObservableList<String> currentList_data = ref_listViewCurrentTap.getItems();
		int current_pos = 0;
		int swap_pos = 0;
		boolean swapped_flag = false;
		String selected_value = "";
		String i_value = "";
		String current_row = "";
		for (String testCaseRow : currentList_data) {
			selected_value = ref_listViewCurrentTap.getSelectionModel().getSelectedItem();
			ApplicationLauncher.logger.info("MoveUpCurrentOnClick: selected_value: " + selected_value);

			if (testCaseRow.equals(selected_value)) {
				for (int i = 0; i < currentList_data.size(); i++) {
					i_value = currentList_data.get(i);
					current_row = testCaseRow;
					ApplicationLauncher.logger.info("MoveUpCurrentOnClick: i_value: " + i_value);
					ApplicationLauncher.logger.info("MoveUpCurrentOnClick: current_row: " + current_row);

					if (i_value.equals(current_row) && !swapped_flag) {
						current_pos = i;
						swap_pos = i - 1;
						ApplicationLauncher.logger.info("MoveUpCurrentOnClick: current_pos: " + current_pos);
						ApplicationLauncher.logger.info("MoveUpCurrentOnClick: swap_pos: " + swap_pos);
						if (current_pos != 0) {
							currentList_data = SwapValues(currentList_data, swap_pos, current_pos);
							ref_listViewCurrentTap.setItems(currentList_data);
							ref_listViewCurrentTap.getSelectionModel().select(swap_pos);
							swapped_flag = true;
						}
					}
				}

			}
		}
	}

	@FXML
	public void MoveDownCurrentOnClick() {
		ObservableList<String> currentList_data = ref_listViewCurrentTap.getItems();
		int current_pos = 0;
		int swap_pos = 0;
		boolean swapped_flag = false;
		String selected_value = "";
		String i_value = "";
		String current_row = "";
		for (String testCaseRow : currentList_data) {
			selected_value = ref_listViewCurrentTap.getSelectionModel().getSelectedItem();
			if (testCaseRow.equals(selected_value)) {
				for (int i = 0; i < currentList_data.size(); i++) {
					i_value = currentList_data.get(i);
					current_row = testCaseRow;
					ApplicationLauncher.logger.info("MoveDownCurrentOnClick: i_value: " + i_value);
					ApplicationLauncher.logger.info("MoveDownCurrentOnClick: current_row: " + current_row);

					if (i_value.equals(current_row) && !swapped_flag) {
						current_pos = i;
						swap_pos = i + 1;
						ApplicationLauncher.logger.info("MoveDownCurrentOnClick: current_pos: " + current_pos);
						ApplicationLauncher.logger.info("MoveDownCurrentOnClick: swap_pos: " + swap_pos);
						if (current_pos != currentList_data.size() - 1) {
							currentList_data = SwapValues(currentList_data, swap_pos, current_pos);
							ref_listViewCurrentTap.setItems(currentList_data);
							ref_listViewCurrentTap.getSelectionModel().select(swap_pos);
							swapped_flag = true;
						}
					}
				}

			}
		}
	}

	public ObservableList<String> SwapValues(ObservableList<String> currentList_data, int swap_pos, int current_pos) {
		String current_value = currentList_data.get(current_pos);
		String swap_value = currentList_data.get(swap_pos);
		currentList_data.set(current_pos, swap_value);
		currentList_data.set(swap_pos, current_value);
		return currentList_data;
	}

	@FXML
	public void appendVoltageValueToList() {

		String value = ref_txtVoltageValue.getText();
		if ((GuiUtils.is_float(value)) && (!value.isEmpty())) {
			if (isVoltageDataInAcceptableRange(value)) {
				if (ref_rdbtnMilliAmps.isSelected()) {
					value = value + " mA";
				}
				appendVoltageValue(value);
			} else {
				ApplicationLauncher.logger.debug("appendVoltageValueToList:  value1: " + value);
				ApplicationLauncher.logger
						.debug("appendVoltageValueToList:  MINIMUM_ACCEPTED_VOLTAGE: " + MINIMUM_ACCEPTED_VOLTAGE);
				ApplicationLauncher.logger
						.debug("appendVoltageValueToList:  MAXIMUM_ACCEPTED_VOLTAGE: " + MAXIMUM_ACCEPTED_VOLTAGE);
				WindowManager.InformUser("Error", "Kindly enter valid voltage value between " + MINIMUM_ACCEPTED_VOLTAGE
						+ " and " + MAXIMUM_ACCEPTED_VOLTAGE, AlertType.ERROR);
			}
		} else {
			ApplicationLauncher.logger.debug("appendVoltageValueToList:  value2: " + value);
			WindowManager.InformUser("Error", "Kindly enter valid voltage value", AlertType.ERROR);
		}

	}

	private boolean isVoltageDataInAcceptableRange(String value) {

		float voltageValue = Float.parseFloat(value);
		if (ref_rdbtnMilliAmps.isSelected()) {
			voltageValue = voltageValue / 1000;
		}

		ApplicationLauncher.logger.debug("isVoltageDataInAcceptableRange:  voltageValue: " + voltageValue);
		if ((voltageValue >= MINIMUM_ACCEPTED_VOLTAGE) && (voltageValue <= MAXIMUM_ACCEPTED_VOLTAGE)) {
			ApplicationLauncher.logger.debug("isVoltageDataInAcceptableRange:  voltageValue: in range");
			return true;
		}

		ApplicationLauncher.logger.debug("isVoltageDataInAcceptableRange:  voltageValue: out of range");
		return false;
	}

	@FXML
	public void removeVoltageValueFromList() {
		// ObservableList<String> voltage = ref_listViewVoltageTap.getItems();
		if (ref_listViewVoltageTap.getItems().size() > 0) {
			String rem_voltage = ref_listViewVoltageTap.getSelectionModel().getSelectedItem();
			ApplicationLauncher.logger.debug("removeVoltageValueFromList:  rem_voltage: " + rem_voltage);
			// voltage.remove(rem_voltage);
			Platform.runLater(() -> {
				ref_listViewVoltageTap.getItems().remove(rem_voltage);
			});
		}
	}

	public void appendVoltageValue(String value) {

		ObservableList<String> voltage = ref_listViewVoltageTap.getItems();

		if (!checkVoltageDataexists(value)) {
			Platform.runLater(() -> {
				ref_listViewVoltageTap.getItems().add(value);
				ref_txtVoltageValue.clear();
				ref_txtVoltageValue.requestFocus();
			});
		}

	}

	public static boolean checkVoltageDataexists(String value) {
		boolean dataexists = false;
		ObservableList<String> existing_data = ref_listViewVoltageTap.getItems();
		if (existing_data.size() != 0) {
			String data = "";
			for (int i = 0; i < existing_data.size(); i++) {
				data = existing_data.get(i);
				if (value.equals(data)) {
					dataexists = true;
					break;
				} else {
					dataexists = false;
				}
			}
		} else {
			dataexists = false;
		}
		return dataexists;
	}

	@FXML
	public void MoveUpVoltageOnClick() {
		ObservableList<String> voltageList_data = ref_listViewVoltageTap.getItems();
		int voltage_pos = 0;
		int swap_pos = 0;
		boolean swapped_flag = false;
		String selected_value = "";
		String i_value = "";
		String voltage_row = "";
		for (String testCaseRow : voltageList_data) {
			selected_value = ref_listViewVoltageTap.getSelectionModel().getSelectedItem();
			ApplicationLauncher.logger.info("MoveUpVoltageOnClick: selected_value: " + selected_value);

			if (testCaseRow.equals(selected_value)) {
				for (int i = 0; i < voltageList_data.size(); i++) {
					i_value = voltageList_data.get(i);
					voltage_row = testCaseRow;
					ApplicationLauncher.logger.info("MoveUpVoltageOnClick: i_value: " + i_value);
					ApplicationLauncher.logger.info("MoveUpVoltageOnClick: voltage_row: " + voltage_row);

					if (i_value.equals(voltage_row) && !swapped_flag) {
						voltage_pos = i;
						swap_pos = i - 1;
						ApplicationLauncher.logger.info("MoveUpVoltageOnClick: voltage_pos: " + voltage_pos);
						ApplicationLauncher.logger.info("MoveUpVoltageOnClick: swap_pos: " + swap_pos);
						if (voltage_pos != 0) {
							voltageList_data = SwapValues(voltageList_data, swap_pos, voltage_pos);
							ref_listViewVoltageTap.setItems(voltageList_data);
							ref_listViewVoltageTap.getSelectionModel().select(swap_pos);
							swapped_flag = true;
						}
					}
				}

			}
		}
	}

	@FXML
	public void MoveDownVoltageOnClick() {
		ObservableList<String> voltageList_data = ref_listViewVoltageTap.getItems();
		int voltage_pos = 0;
		int swap_pos = 0;
		boolean swapped_flag = false;
		String selected_value = "";
		String i_value = "";
		String voltage_row = "";
		for (String testCaseRow : voltageList_data) {
			selected_value = ref_listViewVoltageTap.getSelectionModel().getSelectedItem();
			if (testCaseRow.equals(selected_value)) {
				for (int i = 0; i < voltageList_data.size(); i++) {
					i_value = voltageList_data.get(i);
					voltage_row = testCaseRow;
					ApplicationLauncher.logger.info("MoveDownVoltageOnClick: i_value: " + i_value);
					ApplicationLauncher.logger.info("MoveDownVoltageOnClick: voltage_row: " + voltage_row);

					if (i_value.equals(voltage_row) && !swapped_flag) {
						voltage_pos = i;
						swap_pos = i + 1;
						ApplicationLauncher.logger.info("MoveDownVoltageOnClick: voltage_pos: " + voltage_pos);
						ApplicationLauncher.logger.info("MoveDownVoltageOnClick: swap_pos: " + swap_pos);
						if (voltage_pos != voltageList_data.size() - 1) {
							voltageList_data = SwapValues(voltageList_data, swap_pos, voltage_pos);
							ref_listViewVoltageTap.setItems(voltageList_data);
							ref_listViewVoltageTap.getSelectionModel().select(swap_pos);
							swapped_flag = true;
						}
					}
				}

			}
		}
	}

	public void modelSectionNextTask() {
		ApplicationLauncher.logger.debug("modelSectionNextTask : Entry");
		boolean status = false;

		// if(ref_rdBtnVoltageAndCurrentMapping.isSelected()){
		status = validateVoltageTapCount();
		/*
		 * }else{
		 * status = true;
		 * }
		 */
		if (status) {
			status = validateCurrentTapCount();
			if (status) {
				Platform.runLater(() -> {
					ref_titledPaneLookupTable.setExpanded(true);
					if (isVoltageAndCurrentTapSelected()) {
						// ref_tabCurrentMapping.setDisable(true);
						// ref_tabVoltageAndCurrentMapping.setDisable(false);
						ref_tabPaneTapMapping.getSelectionModel().select(1);
						// ref_btnCurrentTapLookupSave.setVisible(false);
						// ref_btnCurrentAndVoltageTapLookupSave.setVisible(true);
						populateDataForVoltageAndCurrentTapLookupTable();
					} else {
						// ref_tabCurrentMapping.setDisable(false);
						// ref_tabVoltageAndCurrentMapping.setDisable(true);
						ref_tabPaneTapMapping.getSelectionModel().select(0);
						// ref_btnCurrentTapLookupSave.setVisible(true);
						// ref_btnCurrentAndVoltageTapLookupSave.setVisible(false);
						populateDataForCurrentTapLookupTable();
					}
				});

			}
		}

	}

	private void populateDataForVoltageAndCurrentTapLookupTable() {

		ApplicationLauncher.logger.debug("populateDataForVoltageAndCurrentTapLookupTable : CurrentTapSort");
		String lowValueResolutionStrFormat = "%.03f";
		String highValueResolutionDfFormat = "#.###";// "%.1f";
		// boolean decendingOrder = true;//true;
		boolean voltageDescendingOrder = true;// false;
		boolean currentDescendingOrder = true;// false;
		ArrayList<String> sortedCurrentTapData = sortTapData(ref_listViewCurrentTap, lowValueResolutionStrFormat,
				highValueResolutionDfFormat, currentDescendingOrder);
		ApplicationLauncher.logger.debug("populateDataForVoltageAndCurrentTapLookupTable : VoltageTapSort");
		lowValueResolutionStrFormat = "%.03f";
		highValueResolutionDfFormat = "#.###";// "%.1f";
		// decendingOrder = true;//false;
		ArrayList<String> sortedVoltageTapData = sortTapData(ref_listViewVoltageTap, lowValueResolutionStrFormat,
				highValueResolutionDfFormat, voltageDescendingOrder);
		ArrayList<RefStdPulseConstantModel> pulseConstantCurrentData = new ArrayList<RefStdPulseConstantModel>();

		tableColVoltageTapConstantV2_InWh.setVisible(false);
		tableColVoltageTapConstantV3_InWh.setVisible(false);
		tableColVoltageTapConstantV4_InWh.setVisible(false);
		tableColVoltageTapConstantV5_InWh.setVisible(false);
		ref_tableColVoltageTapV2_Hdr.setVisible(false);
		ref_tableColVoltageTapV3_Hdr.setVisible(false);
		ref_tableColVoltageTapV4_Hdr.setVisible(false);
		ref_tableColVoltageTapV5_Hdr.setVisible(false);

		for (int i = 0; i < sortedVoltageTapData.size(); i++) {
			RefStdPulseConstantModel pulseConstantData = new RefStdPulseConstantModel();
			// pulseConstantData.setSerialNo("1");
			// pulseConstantData.setConstantInV1_Wh(2);
			String operationValue = sortedVoltageTapData.get(i);
			if (voltageDescendingOrder) {
				ApplicationLauncher.logger
						.debug("populateDataForVoltageAndCurrentTapLookupTable : voltageDescendingOrder");
				if (i < (sortedVoltageTapData.size() - 1)) {
					operationValue = sortedVoltageTapData.get(i + 1) + " v < V <= " + sortedVoltageTapData.get(i)
							+ " v";
				} else {
					// operationValue = " V >= " + sortedVoltageTapData.get(i) + " v";
					operationValue = "0 v <= V <= " + sortedVoltageTapData.get(i) + " v";
				}
			} else {
				ApplicationLauncher.logger
						.debug("populateDataForVoltageAndCurrentTapLookupTable : voltageAscendingOrder");
				if (i == 0) {
					operationValue = "0 v <= V <= " + sortedVoltageTapData.get(i) + " v";
					// operationValue = sortedVoltageTapData.get(i+1) + " v < V <= " +
					// sortedVoltageTapData.get(i) + " v";
				} else {// if(i < (sortedVoltageTapData.size())){
						// operationValue = " V >= " + sortedVoltageTapData.get(i) + " v";
						// operationValue = "0 v <= V <= " + sortedVoltageTapData.get(i) + " v";
					operationValue = sortedVoltageTapData.get(i - 1) + " v < V <= " + sortedVoltageTapData.get(i)
							+ " v";
				}
			}

			switch (i) {
				case 0:
					tableColVoltageTapConstantV1_InWh.setText(operationValue);
					ref_tableColVoltageTapV1_Hdr.setText(sortedVoltageTapData.get(i) + " Volt");
					// pulseConstantData.setConstantInV1_Wh(operationValue);
					break;

				case 1:
					tableColVoltageTapConstantV2_InWh.setText(operationValue);
					ref_tableColVoltageTapV2_Hdr.setText(sortedVoltageTapData.get(i) + " Volt");
					tableColVoltageTapConstantV2_InWh.setVisible(true);
					ref_tableColVoltageTapV2_Hdr.setVisible(true);
					break;

				case 2:
					tableColVoltageTapConstantV3_InWh.setText(operationValue);
					ref_tableColVoltageTapV3_Hdr.setText(sortedVoltageTapData.get(i) + " Volt");
					tableColVoltageTapConstantV3_InWh.setVisible(true);
					ref_tableColVoltageTapV3_Hdr.setVisible(true);
					break;

				case 3:
					tableColVoltageTapConstantV4_InWh.setText(operationValue);
					ref_tableColVoltageTapV4_Hdr.setText(sortedVoltageTapData.get(i) + " Volt");
					tableColVoltageTapConstantV4_InWh.setVisible(true);
					ref_tableColVoltageTapV4_Hdr.setVisible(true);
					break;

				case 4:
					tableColVoltageTapConstantV5_InWh.setText(operationValue);
					ref_tableColVoltageTapV5_Hdr.setText(sortedVoltageTapData.get(i) + " Volt");
					tableColVoltageTapConstantV5_InWh.setVisible(true);
					ref_tableColVoltageTapV5_Hdr.setVisible(true);
					break;

				default:
					ApplicationLauncher.logger.debug("populateDataForVoltageAndCurrentTapLookupTable : Index: " + i);
					break;

			}
		}

		for (int i = 0; i < sortedCurrentTapData.size(); i++) {
			RefStdPulseConstantModel pulseConstantData = new RefStdPulseConstantModel();
			// pulseConstantData.setSerialNo(sortedCurrentTapData.get(i));
			pulseConstantData.setSerialNo(String.valueOf(i + 1));
			pulseConstantData.setConstantInV1_Wh(ConstantApp.DEFAULT_PULSE_CONSTANT);
			String operationValue = sortedCurrentTapData.get(i);
			if (currentDescendingOrder) {
				ApplicationLauncher.logger
						.debug("populateDataForVoltageAndCurrentTapLookupTable : currentDescendingOrder");

				if (i < (sortedCurrentTapData.size() - 1)) {
					operationValue = sortedCurrentTapData.get(i + 1) + " A < I <= " + sortedCurrentTapData.get(i)
							+ " A";
				} else {
					operationValue = "0 A <= I <= " + sortedCurrentTapData.get(i) + " A";
				}
			} else {
				ApplicationLauncher.logger
						.debug("populateDataForVoltageAndCurrentTapLookupTable : currentAscendingOrder");
				if (i == 0) {
					operationValue = "0 A <= I <= " + sortedCurrentTapData.get(i) + " A";

				} else {
					operationValue = sortedCurrentTapData.get(i - 1) + " A < I <= " + sortedCurrentTapData.get(i)
							+ " A";
				}
			}
			pulseConstantData.setCurrentTapValue(sortedCurrentTapData.get(i));// operationValue);
			pulseConstantData.setOperation(operationValue);// ConstantApp.REF_STD_OPERATION_VOLTAGE_ABOVE_OR_EQUAL);
			pulseConstantCurrentData.add(pulseConstantData);
			// tableColVoltageTapConstantV1_InWh.setText("Test1");
		}

		ref_tableViewVoltageAndCurrentMapping.getItems().clear();
		ref_tableViewVoltageAndCurrentMapping.getItems().addAll(pulseConstantCurrentData);
	}

	private void populateDataForCurrentTapLookupTable() {

		ApplicationLauncher.logger.debug("populateDataForCurrentTapLookupTable : CurrentTapSort");
		String lowValueResolutionStrFormat = "%.03f";
		String highValueResolutionDfFormat = "#.###";
		// boolean decendingOrder = false;//true;
		boolean currentDescendingOrder = true;// false;
		ArrayList<String> sortedCurrentTapData = sortTapData(ref_listViewCurrentTap, lowValueResolutionStrFormat,
				highValueResolutionDfFormat, currentDescendingOrder);
		ArrayList<RefStdPulseConstantModel> pulseConstantCurrentData = new ArrayList<RefStdPulseConstantModel>();
		// boolean currentDescendingOrder = true;//false;
		for (int i = 0; i < sortedCurrentTapData.size(); i++) {
			RefStdPulseConstantModel pulseConstantData = new RefStdPulseConstantModel();
			pulseConstantData.setSerialNo(String.valueOf(i + 1));// sortedCurrentTapData.get(i));
			pulseConstantData.setConstantInV1_Wh(ConstantApp.DEFAULT_PULSE_CONSTANT);
			String operationValue = sortedCurrentTapData.get(i);
			/*
			 * if(i < (sortedCurrentTapData.size()-1)){
			 * operationValue = sortedCurrentTapData.get(i+1) + " A < I <= " +
			 * sortedCurrentTapData.get(i) + " A";
			 * }else{
			 * //operationValue = " I >= " + sortedCurrentTapData.get(i) + " A";;
			 * operationValue = "0 A <= I <= " + sortedCurrentTapData.get(i) + " A";
			 * }
			 */
			if (currentDescendingOrder) {
				ApplicationLauncher.logger.debug("populateDataForCurrentTapLookupTable : currentDescendingOrder");

				if (i < (sortedCurrentTapData.size() - 1)) {
					operationValue = sortedCurrentTapData.get(i + 1) + " A < I <= " + sortedCurrentTapData.get(i)
							+ " A";
				} else {
					operationValue = "0 A <= I <= " + sortedCurrentTapData.get(i) + " A";
				}
			} else {
				ApplicationLauncher.logger.debug("populateDataForCurrentTapLookupTable : currentAscendingOrder");
				if (i == 0) {
					operationValue = "0 A <= I <= " + sortedCurrentTapData.get(i) + " A";

				} else {
					operationValue = sortedCurrentTapData.get(i - 1) + " A < I <= " + sortedCurrentTapData.get(i)
							+ " A";
				}
			}
			pulseConstantData.setCurrentTapValue(sortedCurrentTapData.get(i));
			pulseConstantData.setOperation(operationValue);// ConstantApp.REF_STD_OPERATION_CURRENT_ABOVE_OR_EQUAL);
			pulseConstantCurrentData.add(pulseConstantData);
		}
		ref_tableViewCurrentMappingOnly.getItems().clear();
		ref_tableViewCurrentMappingOnly.getItems().addAll(pulseConstantCurrentData);
	}

	private ArrayList<String> sortTapData(ListView<String> ref_listViewCurrentTap2, String lowValueResolutionFormat,
			String highValueResolutionFormat, boolean decendingOrder) {
		// String lowValueResolutionFormat = "%.03f";
		// String highValueResolutionFormat = "%.1f";
		ArrayList<Float> sortedTapDataFloat = new ArrayList<Float>();
		for (int i = 0; i < ref_listViewCurrentTap2.getItems().size(); i++) {
			sortedTapDataFloat.add(Float.parseFloat(ref_listViewCurrentTap2.getItems().get(i)));
		}

		if (sortedTapDataFloat.size() > 0) {
			Collections.sort(sortedTapDataFloat, new Comparator<Float>() {
				@Override
				public int compare(Float o1, Float o2) {
					return Float.compare(o1.floatValue(), o2.floatValue());
				}
			});
			// sortedCurrentTapData = Arrays.sort(sortedCurrentTapData,
			// Collections.reverseOrder());
			if (decendingOrder) {
				Collections.sort(sortedTapDataFloat, Collections.reverseOrder());
			}

		}
		ApplicationLauncher.logger.debug("sortTapData : sortedTapDataFloat: " + sortedTapDataFloat.toString());

		ArrayList<String> sortedTapDataStr = new ArrayList<String>();

		for (int i = 0; i < ref_listViewCurrentTap2.getItems().size(); i++) {

			if (sortedTapDataFloat.get(i) < 1.0) {
				sortedTapDataStr.add(String.format(lowValueResolutionFormat, (sortedTapDataFloat.get(i))));
				/*
				 * DecimalFormat df = new DecimalFormat("#.###");
				 * String formattedValue = df.format(sortedTapDataFloat.get(i));
				 * ApplicationLauncher.logger.debug("sortTapData : formattedValue: " +
				 * formattedValue);
				 * sortedTapDataStr.add(formattedValue);
				 */
			} else {
				// sortedTapDataStr.add(String.format(highValueResolutionFormat,(sortedTapDataFloat.get(i))));
				DecimalFormat df = new DecimalFormat(highValueResolutionFormat);// "#.###");
				String formattedValue = df.format(sortedTapDataFloat.get(i));
				sortedTapDataStr.add(formattedValue);
			}
		}

		ApplicationLauncher.logger.debug("sortTapData : sortedTapDataStr: " + sortedTapDataStr);

		return sortedTapDataStr;
	}

	private boolean validateVoltageTapCount() {

		ApplicationLauncher.logger.debug("validateVoltageTapCount : Entry");
		String voltageTapCountStr = ref_txtNoOfVoltageTap.getText();
		if (GuiUtils.is_number(voltageTapCountStr)) {
			int voltageTapCount = Integer.parseInt(voltageTapCountStr);
			if (voltageTapCount > 0) {
				int userAddedVoltageTapListCount = ref_listViewVoltageTap.getItems().size();

				if (voltageTapCount == userAddedVoltageTapListCount) {
					return true;
				} else {
					ApplicationLauncher.logger.debug("validateVoltageTapCount:  voltageTapCount: " + voltageTapCount);
					ApplicationLauncher.logger.debug(
							"validateVoltageTapCount:  userAddedVoltageTapListCount: " + userAddedVoltageTapListCount);
					WindowManager.InformUser("Error",
							"Voltage tap list count mismatching with <No of Voltage Tap> value", AlertType.ERROR);
				}
			} else {
				ApplicationLauncher.logger.debug("validateVoltageTapCount:  voltageTapCount2: " + voltageTapCount);
				// ApplicationLauncher.logger.debug("validateVoltageTapCount:
				// userAddedVoltageTapListCount: " + userAddedVoltageTapListCount );
				WindowManager.InformUser("Error", "Kindly update <No of Voltage Tap> field with non zero",
						AlertType.ERROR);
			}
		} else {
			ApplicationLauncher.logger.debug("validateVoltageTapCount:  voltageTapCountStr: " + voltageTapCountStr);
			WindowManager.InformUser("Error", "Kindly enter valid number in <No of Voltage Tap> field",
					AlertType.ERROR);
		}

		return false;
	}

	private boolean validateCurrentTapCount() {

		ApplicationLauncher.logger.debug("validateCurrentTapCount : Entry");
		String currentTapCountStr = ref_txtNoOfCurrentTap.getText();
		if (GuiUtils.is_number(currentTapCountStr)) {
			int currentTapCount = Integer.parseInt(currentTapCountStr);
			if (currentTapCount > 0) {
				int userAddedCurrentTapListCount = ref_listViewCurrentTap.getItems().size();
				if (currentTapCount == userAddedCurrentTapListCount) {
					return true;
				} else {
					ApplicationLauncher.logger.debug("validateCurrentTapCount:  currentTapCount: " + currentTapCount);
					ApplicationLauncher.logger.debug(
							"validateCurrentTapCount:  userAddedCurrentTapListCount: " + userAddedCurrentTapListCount);
					WindowManager.InformUser("Error",
							"Current tap list count mismatching with <No of Current Tap> value", AlertType.ERROR);
				}
			} else {
				ApplicationLauncher.logger.debug("validateCurrentTapCount:  currentTapCount2: " + currentTapCount);
				// ApplicationLauncher.logger.debug("validateCurrentTapCount:
				// userAddedCurrentTapListCount: " + userAddedCurrentTapListCount );
				WindowManager.InformUser("Error", "Kindly update <No of Current Tap> field with non zero",
						AlertType.ERROR);
			}
		} else {
			ApplicationLauncher.logger.debug("validateCurrentTapCount:  currentTapCountStr: " + currentTapCountStr);
			WindowManager.InformUser("Error", "Kindly enter valid number in <No of Current Tap> field",
					AlertType.ERROR);
		}

		return false;
	}

	@FXML
	public void btnModelSectionNextOnClick() {

		ApplicationLauncher.logger.debug("btnModelSectionNextOnClick : Entry");
		modelSaveNextTimer = new Timer();
		modelSaveNextTimer.schedule(new modelSectionNextTaskTrigger(), 100);

	}

	class modelSectionNextTaskTrigger extends TimerTask {

		@Override
		public void run() {
			// Platform.runLater(() -> {
			modelSectionNextTask();
			modelSaveNextTimer.cancel();
			// });
		}

	}

	public boolean isVoltageAndCurrentTapSelected() {
		return voltageAndCurrentTapSelected;
	}

	public void setVoltageAndCurrentTapSelected(boolean voltageAndCurrentTapSelected) {
		this.voltageAndCurrentTapSelected = voltageAndCurrentTapSelected;
	}

	class EditingCell extends TableCell<RefStdPulseConstantModel, String> {

		private TextField textField;

		public EditingCell() {
		}

		@Override
		public void startEdit() {
			ApplicationLauncher.logger.debug("EditingCell : startEdit");
			if (!isEmpty()) {
				super.startEdit();
				if (textField == null) {
					createTextField();
				}

				setGraphic(textField);
				setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
				// textField.selectAll();
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						textField.requestFocus();
						textField.selectAll();
					}
				});
			}
		}

		@Override
		public void cancelEdit() {
			super.cancelEdit();
			ApplicationLauncher.logger.debug("EditingCell : cancelEdit");
			setText((String) getItem());
			setGraphic(null);
			setContentDisplay(ContentDisplay.TEXT_ONLY);
		}

		@Override
		public void updateItem(String item, boolean empty) {
			super.updateItem(item, empty);
			ApplicationLauncher.logger.debug("EditingCell : updateItem");
			if (empty) {
				setText(null);
				setGraphic(null);
			} else {
				if (isEditing()) {
					if (textField != null) {
						textField.setText(getString());
					}
					setGraphic(textField);
					setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
				} else {
					setText(getString());
					setContentDisplay(ContentDisplay.TEXT_ONLY);
				}
			}
		}

		private void createTextField() {
			ApplicationLauncher.logger.debug("EditingCell : createTextField");
			textField = new TextField(getString());
			textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()
					* 2);

			textField.focusedProperty().addListener(
					new ChangeListener<Boolean>() {
						@Override
						public void changed(
								ObservableValue<? extends Boolean> arg0,
								Boolean arg1, Boolean arg2) {
							if (!arg2) {
								commitEdit(textField.getText());
							}
						}
					});

			textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent t) {
					if (t.getCode() == KeyCode.ENTER) {
						commitEdit(textField.getText());
					} else if (t.getCode() == KeyCode.ESCAPE) {
						cancelEdit();
					} else if (t.getCode() == KeyCode.TAB) {
						commitEdit(textField.getText());
						TableColumn nextColumn = getNextColumn(!t.isShiftDown());
						if (nextColumn != null) {
							getTableView().edit(getTableRow().getIndex(),
									nextColumn);
						}

					}
				}

			});
		}

		private String getString() {
			ApplicationLauncher.logger.debug("EditingCell : getString");
			return getItem() == null ? "" : getItem().toString();
		}

		private TableColumn<RefStdPulseConstantModel, ?> getNextColumn(boolean forward) {
			ApplicationLauncher.logger.debug("EditingCell : getNextColumn");
			List<TableColumn<RefStdPulseConstantModel, ?>> columns = new ArrayList<>();
			for (TableColumn<RefStdPulseConstantModel, ?> column : getTableView().getColumns()) {
				columns.addAll(getLeaves(column));
			}
			// There is no other column that supports editing.
			if (columns.size() < 2) {// 2) {
				ApplicationLauncher.logger.debug("EditingCell : getNextColumn: Exit1");
				return null;
			}
			int currentIndex = columns.indexOf(getTableColumn());
			int nextIndex = currentIndex;
			if (forward) {
				nextIndex++;
				if (nextIndex > columns.size() - 1) {
					nextIndex = 0;
				}
			} else {
				nextIndex--;
				if (nextIndex < 0) {
					nextIndex = columns.size() - 1;
				}
			}
			ApplicationLauncher.logger.debug("EditingCell : getNextColumn: Exit2");
			return columns.get(nextIndex);
		}

		private List<TableColumn<RefStdPulseConstantModel, ?>> getLeaves(
				TableColumn<RefStdPulseConstantModel, ?> root) {
			ApplicationLauncher.logger.debug("EditingCell : getLeaves");
			List<TableColumn<RefStdPulseConstantModel, ?>> columns = new ArrayList<>();
			if (root.getColumns().isEmpty()) {
				// We only want the leaves that are editable.
				if (root.isEditable()) {
					columns.add(root);
				}
				return columns;
			} else {
				for (TableColumn<RefStdPulseConstantModel, ?> column : root.getColumns()) {
					columns.addAll(getLeaves(column));
				}
				return columns;
			}
		}
	}

}
