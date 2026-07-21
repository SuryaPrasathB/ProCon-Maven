package com.tasnetwork.calibration.conveyor;

import java.awt.Button;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.tasnetwork.calibration.conveyor.bay.calib.CalibrationBaySingleStateTestRun;
import com.tasnetwork.calibration.conveyor.bay.comm.CommunicationTestBaySingleStateTestRun;
import com.tasnetwork.calibration.conveyor.bay.ft.FunctionalTestBaySingleStateTestRun;
import com.tasnetwork.calibration.conveyor.bay.hv.HighVoltageTestBaySingleStateTestRun;
import com.tasnetwork.calibration.conveyor.bay.ir.InsulationResistanceTestBaySingleStateTestRun;
import com.tasnetwork.calibration.conveyor.bay.loading.LoadingBaySingleStateTestRun;
import com.tasnetwork.calibration.conveyor.bay.rejection.RejectionBaySingleStateTestRun;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.STA_NoLoadTestBay1SingleStateTestRun;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.STA_NoLoadTestBay2SingleStateTestRun;
import com.tasnetwork.calibration.conveyor.bay.unloading.UnloadingBaySingleStateTestRun;
import com.tasnetwork.calibration.conveyor.bay.verific.VerificationTestBaySingleStateTestRun;
import com.tasnetwork.calibration.conveyor.bay.verific_waiting.WaitngBaySingleStateTestRun;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantStateModes;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.WindowManager;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;

public class StatePlannerController implements Initializable {

	/*
	 * @FXML private TextField txtNoOfPosition;
	 * 
	 * private static TextField ref_txtNoOfPosition;
	 */

	@FXML
	public Button buttonAddRow;
	@FXML
	public Button buttonDeleteRow;
	@FXML
	public Button buttonSaveStateFlow;
	@FXML
	public Button buttonLoadStates;

	public static TableView<StateFlow> tableStatePlanner_FtBay_UI = new TableView<StateFlow>();

	@FXML
	private ComboBox<String> cmbBxSelectBayType;
	private static ComboBox<String> ref_cmbBxSelectBayType;

	@FXML
	private ComboBox<String> cmbBxSelectSingleState;
	public static ComboBox<String> ref_cmbBxSelectSingleState;

	private AtomicInteger serialNoAtomic = new AtomicInteger(1);

	@FXML
	public TableView<StateFlow> tableStatePlanner;
	@FXML
	public TableColumn<StateFlow, Integer> columnSerialNo;
	@FXML
	public TableColumn<StateFlow, String> columnPath;
	@FXML
	public TableColumn<StateFlow, String> columnState;
	@FXML
	public TableColumn<StateFlow, String> columnStateErrorCode;
	@FXML
	public TableColumn<StateFlow, String> columnSuccess;
	@FXML
	public TableColumn<StateFlow, String> columnSuccessErrorCode;
	@FXML
	public TableColumn<StateFlow, String> columnFailed;
	@FXML
	public TableColumn<StateFlow, String> columnFailedErrorCode;

	@FXML
	private ComboBox<String> cmbModeSelection; // Added ComboBox for mode selection

	private static final Map<String, Supplier<TimerTask>> singleStateTasks = new HashMap<>();

	static {
		singleStateTasks.put(ConstantConveyor.FT_BAY_DISPLAY_NAME, FunctionalTestBaySingleStateTestRun::new);
		singleStateTasks.put(ConstantConveyor.HV_BAY_DISPLAY_NAME, HighVoltageTestBaySingleStateTestRun::new);
		singleStateTasks.put(ConstantConveyor.IR_BAY_DISPLAY_NAME, InsulationResistanceTestBaySingleStateTestRun::new);
		singleStateTasks.put(ConstantConveyor.CALIBRATION_BAY_DISPLAY_NAME, CalibrationBaySingleStateTestRun::new);
		singleStateTasks.put(ConstantConveyor.COMMUNICATION_BAY_DISPLAY_NAME, CommunicationTestBaySingleStateTestRun::new);
		singleStateTasks.put(ConstantConveyor.LOADING_BAY_DISPLAY_NAME, LoadingBaySingleStateTestRun::new);
		singleStateTasks.put(ConstantConveyor.STA_NLD1_BAY_DISPLAY_NAME, STA_NoLoadTestBay1SingleStateTestRun::new);
		singleStateTasks.put(ConstantConveyor.STA_NLD2_BAY_DISPLAY_NAME, STA_NoLoadTestBay2SingleStateTestRun::new);
		singleStateTasks.put(ConstantConveyor.UNLOADING_BAY_DISPLAY_NAME, UnloadingBaySingleStateTestRun::new);
		singleStateTasks.put(ConstantConveyor.VERIFICATION_BAY_DISPLAY_NAME, VerificationTestBaySingleStateTestRun::new);
		singleStateTasks.put(ConstantConveyor.WAITING_BAY_DISPLAY_NAME, WaitngBaySingleStateTestRun::new);
		singleStateTasks.put(ConstantConveyor.REJECTION_BAY_DISPLAY_NAME, RejectionBaySingleStateTestRun::new);
	}

	private final ObservableList<String> stateNames = FXCollections.observableArrayList();

	private final ObservableList<StateFlow> stateFlowRows = FXCollections.observableArrayList();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		refAssignment();
		guiInit();
	}

	public void refAssignment() {
		ref_cmbBxSelectBayType = cmbBxSelectBayType;
		ref_cmbBxSelectSingleState = cmbBxSelectSingleState;
	}

	public void guiInit() {

		ArrayList<String> bayList = (ArrayList<String>) ConstantConveyor.getBayLookup().keySet().stream()
				.collect(Collectors.toList());
		ref_cmbBxSelectBayType.getItems().add("Select Bay");
		ref_cmbBxSelectBayType.getItems().addAll(bayList);
		ref_cmbBxSelectBayType.getSelectionModel().select(0);

		cmbModeSelection.getItems().addAll(ConstantStateModes.RUN, ConstantStateModes.STOP, ConstantStateModes.RESET,
				ConstantStateModes.BAY_BYPASS);
		cmbModeSelection.getSelectionModel().select("RUN"); // Set default selection
		cmbModeSelection.valueProperty().addListener((observable, oldValue, newValue) -> {
			ApplicationLauncher.logger.debug(newValue + " is selected for execution mode");
		});

		statePlannerGuiInit();
	}

	@FXML
	public void loadBayStateOnClick() {
		String selectedBayType = ref_cmbBxSelectBayType.getSelectionModel().getSelectedItem();
		stateNames.clear();
		tableStatePlanner.getItems().clear();
		ApplicationLauncher.logger.error("loadBayState : selectedBayType : " + selectedBayType);

		if (selectedBayType != null && !selectedBayType.equals("Select Bay")) {
			stateNames.addAll(StateRegistry.getStatesForBay(selectedBayType));
		}

		statePlannerGuiInit();
		loadBayStatesFromDatabase();

		// Populate the single state combo box
		ref_cmbBxSelectSingleState.getItems().clear(); // Clear existing items
		ref_cmbBxSelectSingleState.getItems().addAll(stateNames); // Add new state names

	}

	public void statePlannerGuiInit() {

		// ==================== STATE PLANNER
		// ==================================================================//
		ApplicationLauncher.logger.info("StatePlanner : initialize : Entry");

		columnSerialNo.setCellValueFactory(data -> data.getValue().getSerialNoProperty().asObject());
		columnPath.setCellValueFactory(data -> data.getValue().getPathProperty());
		columnState.setCellValueFactory(data -> data.getValue().getStateProperty());
		columnStateErrorCode.setCellValueFactory(data -> data.getValue().getErrorCodeProperty());
		columnSuccess.setCellValueFactory(data -> data.getValue().getIfSuccessProperty());
		columnSuccessErrorCode.setCellValueFactory(data -> data.getValue().getIfSuccessErrorCodeProperty());
		columnFailed.setCellValueFactory(data -> data.getValue().getIfFailedProperty());
		columnFailedErrorCode.setCellValueFactory(data -> data.getValue().getIfFailedErrorCodeProperty());

		// Set the columns as editable
		tableStatePlanner.setEditable(true);

		columnState.setEditable(true);
		columnSuccess.setEditable(true);

		// Set ComboBox for State and Success columns (dropdown)
		columnState.setCellFactory(ComboBoxTableCell.forTableColumn(stateNames));
		columnSuccess.setCellFactory(ComboBoxTableCell.forTableColumn(stateNames));

		// No drop-down for the Failed column, it will auto-update based on other
		// columns
		// columnFailed.setCellFactory(TextFieldTableCell.forTableColumn());

		// Populate table with initial dummy data
		stateFlowRows.addAll(

		);

		tableStatePlanner.setItems(stateFlowRows);
		tableStatePlanner.refresh();

		// Set up the table (this includes setting up ComboBox for the State column)
		setupTable();

	}

	public void loadBayStatesFromDatabase() {
		ApplicationLauncher.logger.debug("loadBayStatesFromDatabase : Entry");

		// Get the selected bay name from the combo box
		String bayName = ref_cmbBxSelectBayType.getSelectionModel().getSelectedItem();
		ApplicationLauncher.logger.debug("loadBayStatesFromDatabase : Bay Name : " + bayName);

		// Clear the TableView before populating it
		tableStatePlanner.getItems().clear();

		String bayKey = ConstantConveyor.getBayLookup().get(bayName);

		String executionMode = getExecutionMode();

		try {
			if (bayKey == null || bayKey.isEmpty() || bayKey.equals("Select Bay")) {
				ApplicationLauncher.logger.warn("loadBayStatesFromDatabase : No valid bayId selected");
				return;
			}

			// Fetch data from the database for the selected bayId
			List<StateFlow> stateFlows = MySqlServiceManager.getStateFlowService().findByBayKeyAndExecutionMode(bayKey,
					executionMode);

			// Log the number of rows retrieved
			ApplicationLauncher.logger
					.debug("loadBayStatesFromDatabase : Retrieved " + stateFlows.size() + " rows for bayId: " + bayKey);

			tableStatePlanner.getItems().addAll(stateFlows);
			OptionalInt lastSerialNo = tableStatePlanner.getItems().stream().mapToInt(e -> e.getSerialNo()).max();
			if (lastSerialNo.isPresent()) {
				getSerialNoAtomic().set(lastSerialNo.getAsInt() + 1);
			}

			if (bayKey.equals(ConstantConveyor.FT_BAY_KEY)) {
				StatePlannerController.setTableStatePlannerFtBay_UI(tableStatePlanner);
			} else {

			}

		} catch (Exception e) {
			// Handle exceptions and log errors
			ApplicationLauncher.logger.error("loadBayStatesFromDatabase : Failed to load data: " + e.getMessage(), e);
		}

		ApplicationLauncher.logger.debug("loadBayStatesFromDatabase : Exit");
	}

	@FXML
	public void cmbBxSelectBayTypeOnChange() {
		tableStatePlanner.getItems().clear();
	}

	public static String deriveStateErrorCode(String stateName) {
		if (stateName == null || !stateName.startsWith("S")) {
			return "UNKNOWN_CODE";
		}
		String[] parts = stateName.split("_");
		if (parts.length > 0 && parts[0].length() > 1) {
			String numStr = parts[0].substring(1);
			try {
				int num = Integer.parseInt(numStr);
				return String.format("STATE_CODE_%03d", num);
			} catch (NumberFormatException e) {
				return "STATE_CODE_" + numStr;
			}
		}
		return "UNKNOWN_CODE";
	}

	@FXML
	private void setupTable() {
		// Set the cell factory for the "State" column to use a ComboBox
		columnState.setCellFactory(ComboBoxTableCell.forTableColumn(stateNames));

		// Listen for changes in the "State" column and update the "StateErrorCode"
		// column
		columnState.setOnEditCommit(event -> {
			// Get the selected row
			StateFlow row = event.getRowValue();

			// Get the new state name
			String newState = event.getNewValue();

			// Update the state in the row
			row.setState(newState);

			// Map the state name dynamically to its corresponding state code
			String stateCode = deriveStateErrorCode(newState);
			row.setErrorCode(stateCode);

			// Refresh the table view to reflect the updated data
			tableStatePlanner.refresh();
		});

		// Set up other columns (e.g., "Success") as needed
		columnSuccess.setCellFactory(ComboBoxTableCell.forTableColumn(stateNames));

		columnSuccess.setOnEditCommit(event -> {
			// Get the selected row
			StateFlow row = event.getRowValue();

			// Get the new state name
			String newState = event.getNewValue();

			// Update the state in the row
			row.setIfSuccess(newState);

			// Map the state name dynamically to its corresponding state code
			String stateCode = deriveStateErrorCode(newState);
			row.setIfSuccessErrorCode(stateCode);

			// Refresh the table view to reflect the updated data
			tableStatePlanner.refresh();
		});
	}
	// ==============================================================================

	@FXML
	public void ButtonSaveStateFlowOnClick() {
		// Call the validation function
		if (!validateStateFlowRows()) {
			// If validation fails, stop further execution
			return;
		}

		// setTableStatePlannerFtBay_UI(tableStatePlanner);

		// Save rows to the database
		try {
			saveStateFlowRowsToDatabase();
			WindowManager.InformUser("State Plan", "Successfully Saved", AlertType.INFORMATION);
		} catch (Exception e) {
			e.printStackTrace();
			WindowManager.InformUser("State Plan", "Failed to Save: " + e.getMessage(), AlertType.ERROR);
		}

	}

	/**
	 * Save the rows from the tableStatePlanner to the database.
	 */
	private void saveStateFlowRowsToDatabase() {
		/*
		 * ObservableList<StateFlow> rows = tableStatePlanner.getItems();
		 * for (StateFlow row : rows) {
		 * if (row != null) {
		 * saveRowToDatabase(row);
		 * }
		 * }
		 */

		ApplicationLauncher.logger.debug(
				"saveStateFlowRowsToDatabase : tableStatePlanner : Size : " + tableStatePlanner.getItems().size());

		String bayName = ref_cmbBxSelectBayType.getSelectionModel().getSelectedItem();

		String bayKey = ConstantConveyor.getBayLookup().get(bayName);

		String executionMode = getExecutionMode();

		List<StateFlow> dbRows = MySqlServiceManager.getStateFlowService().findByBayKeyAndExecutionMode(bayKey,
				executionMode);

		// Get the current rows from the UI
		List<StateFlow> uiRows = new ArrayList<>(tableStatePlanner.getItems());

		// Identify rows to delete (present in the DB but not in the UI)
		List<StateFlow> rowsToDelete = dbRows.stream()
				.filter(dbRow -> uiRows.stream().noneMatch(uiRow -> uiRow.getId() == dbRow.getId()))
				.collect(Collectors.toList());

		// Delete rows from the database
		for (StateFlow rowToDelete : rowsToDelete) {
			MySqlServiceManager.getStateFlowService().deleteById(rowToDelete.getId());
		}

		// Save or update rows from the UI to the database
		for (StateFlow row : uiRows) {
			MySqlServiceManager.getStateFlowService().saveToDb(row);
		}
	}

	// ==============================================================================

	// ===============================================================================

	@FXML
	public void buttonDeleteRowOnClick() {
		// Get the selected row
		StateFlow selectedRow = tableStatePlanner.getSelectionModel().getSelectedItem();

		// Check if a row is selected
		if (selectedRow != null) {
			// Remove the selected row from the list
			stateFlowRows.remove(selectedRow);

			// Refresh the table view to reflect the deletion
			tableStatePlanner.refresh();
		}
	}

	@FXML
	public void buttonAddRowOnClick() {
		// Get the selected row index
		int selectedIndex = tableStatePlanner.getSelectionModel().getSelectedIndex();

		// Add a new row to the stateFlowRows list after the selected row
		String bayName = ref_cmbBxSelectBayType.getSelectionModel().getSelectedItem();
		String bayKey = ConstantConveyor.getBayLookup().get(bayName);
		String executionMode = getExecutionMode();

		StateFlow newRow = new StateFlow(
				getSerialNoAtomic().getAndIncrement(),
				"S" + (stateFlowRows.size() + 1),
				bayKey,
				executionMode,
				"Select State",
				"No State Selected",
				"Select State",
				"No State Selected",
				" ",
				" ");

		if (selectedIndex >= 0 && selectedIndex < stateFlowRows.size()) {
			// Insert the new row after the selected row
			stateFlowRows.add(selectedIndex + 1, newRow);
		} else {
			// If no row is selected or the selection is invalid, add the row to the end
			stateFlowRows.add(newRow);
		}

		// Refresh the table view to display the updated list
		tableStatePlanner.refresh();
	}

	private String getExecutionMode() {
		// Get the selected item from the ComboBox
		return cmbModeSelection.getSelectionModel().getSelectedItem();
	}

	// ==============================================================================
	@FXML
	public void runSingleStateOnClick() {
		String bayName = ref_cmbBxSelectBayType.getSelectionModel().getSelectedItem();

		Supplier<TimerTask> taskSupplier = singleStateTasks.get(bayName);
		if (taskSupplier != null) {
			new Timer().schedule(taskSupplier.get(), 100);
		}
	}

	@FXML
	void cmbBxSelectSingleStateOnChange() {

	}
	// ==============================================================================

	// ==============================================================================

	private boolean validateStateFlowRows() {
		// Get all rows from the TableView
		ObservableList<StateFlow> rows = tableStatePlanner.getItems();

		// Iterate through the rows to validate
		for (int i = 0; i < rows.size(); i++) {
			StateFlow row = rows.get(i);

			// Check if the state is "Select State"
			if ("Select State".equals(row.getState())) {
				// Prompt the user with the row number
				WindowManager.InformUser("State not selected in row " + (i + 1), "Kindly select a state",
						AlertType.INFORMATION);

				return false; // Validation failed
			}
		}
		return true; // Validation successful
	}

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep2 :InterruptedException:" + e.getMessage());
		}

	}

	public static TableView<StateFlow> getTableStatePlannerFtBay_UI() {
		return tableStatePlanner_FtBay_UI;
	}

	public static void setTableStatePlannerFtBay_UI(TableView<StateFlow> tableStatePlannerFtBay_UI) {
		StatePlannerController.tableStatePlanner_FtBay_UI = tableStatePlannerFtBay_UI;
	}

	public AtomicInteger getSerialNoAtomic() {
		return serialNoAtomic;
	}

	public void setSerialNoAtomic(AtomicInteger serialNoAtomic) {
		this.serialNoAtomic = serialNoAtomic;
	}

}
