package com.tasnetwork.calibration.energymeter.setting;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.WindowManager;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
import com.tasnetwork.calibration.energymeter.testprofiles.TestCaseData;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class AdminPageController implements Initializable {
	Timer UAC_Timer;

	@FXML
	private Button btnUserAccessControl;

	private static Button ref_btnUserAccessControl;

	@FXML
	private ComboBox<String> cmbBxAccessLevel;

	@FXML
	private TextField txt_username;

	@FXML
	private TextField filterField;

	@FXML
	private TextField txt_password;
	// edited mohan
	@FXML
	private TextField txt_confirm_password;
	// fin

	@FXML
	private TableView<ProcalUserModel> procal_user_list_table;
	@FXML
	private TableColumn<ProcalUserModel, String> usernameColumn;
	@FXML
	private TableColumn<ProcalUserModel, String> accesslevelColumn;

	private ObservableList<ProcalUserModel> username_list = FXCollections.observableArrayList();

	public AdminPageController() {
		RefreshUserList();

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ref_btnUserAccessControl = btnUserAccessControl;
		updateAccessLevel();

		usernameColumn.setCellValueFactory(cellData -> cellData.getValue().userNameProperty());
		accesslevelColumn.setCellValueFactory(cellData -> cellData.getValue().accessLevelProperty());

		// 1. Wrap the ObservableList in a FilteredList (initially display all data).
		FilteredList<ProcalUserModel> filteredData = new FilteredList<>(username_list, user -> true);

		// 2. Set the filter Predicate whenever the filter changes.
		filterField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(energyMeterModelFilterData -> {
				// If filter text is empty, display all persons.
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				// Compare first name and last name of every person with filter text.
				String lowerCaseFilter = newValue.toLowerCase();

				if (energyMeterModelFilterData.getUserName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true; // Filter matches first name.
				} else if (energyMeterModelFilterData.getAccessLevel().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true; // Filter matches last name.
				}
				return false; // Does not match.
			});
		});

		// 3. Wrap the FilteredList in a SortedList.
		SortedList<ProcalUserModel> sortedData = new SortedList<>(filteredData);

		// 4. Bind the SortedList comparator to the TableView comparator.
		// Otherwise, sorting the TableView would have no effect.
		sortedData.comparatorProperty().bind(procal_user_list_table.comparatorProperty());

		// 5. Add sorted (and filtered) data to the table.
		procal_user_list_table.setItems(sortedData);

		procal_user_list_table.getSelectionModel().selectedItemProperty()
				.addListener((obs, oldSelection, newSelection) -> {
					if (newSelection != null) {
						// tableview2.getSelectionModel().clearSelection();
						// TablePosition<EM_Model, ?> pos =
						// customer_EM_Model_Table.getFocusModel().getFocusedCell();
						// ApplicationLauncher.logger.info("Checkbox value set:"+newValue + "
						// Row="+pos.getRow());
						ApplicationLauncher.logger.info("New row selected: " + newSelection.getUserName() + ":"
								+ newSelection.getAccessLevel());
						loadUserDetails(newSelection);
					}
				});

		if (!ProcalFeatureEnable.USER_ACCESS_CONTROL_ENABLED) {
			ref_btnUserAccessControl.setVisible(false);
		}

	}

	public void loadUserDetails(ProcalUserModel newSelection) {

		txt_username.setText(newSelection.getUserName());
		txt_password.setText(newSelection.getPassword());
		cmbBxAccessLevel.getSelectionModel().select(newSelection.getAccessLevel());
	}

	public void updateAccessLevel() {
		cmbBxAccessLevel.getItems().clear();
		cmbBxAccessLevel.getItems().addAll(ConstantApp.ACCESS_LEVEL);
		cmbBxAccessLevel.getSelectionModel().select(0);
	}

	public void onAddUserClick() {
		username_list.add(new ProcalUserModel("", "", "", "Read Only"));
		int row = username_list.size() - 1;

		procal_user_list_table.requestFocus();
		procal_user_list_table.getSelectionModel().select(row);
		procal_user_list_table.getFocusModel().focus(row);
	}

	public void onRemoveUserClick() {
		int row = procal_user_list_table.getSelectionModel().getSelectedIndex();

		ProcalUserModel user = (ProcalUserModel) procal_user_list_table.getSelectionModel().getSelectedItem();
		MySQL_Controller.sp_delete_procal_users(user.getUserName());
		RefreshUserList();
	}

	public void onResetClick() {
		txt_username.setText("");
		txt_password.setText("");
		// edited mohan
		txt_confirm_password.setText("");
		// fin
		cmbBxAccessLevel.getSelectionModel().select(0);
	}

	// edited mohan
	public void onSaveUserClick() {
		int row = procal_user_list_table.getSelectionModel().getSelectedIndex();

		String user_name = txt_username.getText();
		String password = txt_password.getText();
		String confirm_password = txt_confirm_password.getText();
		String access_level = cmbBxAccessLevel.getSelectionModel().getSelectedItem();
		String created_by = "";
		String date_created = "";
		boolean status = CheckDataIsNotEmpty(user_name,
				password, confirm_password, access_level);
		if (status) {
			if (check_correct_password(password, confirm_password)) {

				MySQL_Controller.sp_add_procal_users(user_name,
						password, access_level, created_by, date_created);

				RefreshUserList();
				procal_user_list_table.getSelectionModel().select(row);
				procal_user_list_table.getFocusModel().focus(row);
				WindowManager.InformUser("Save success", "Data saved successfully", AlertType.INFORMATION);
			} else {
				ApplicationLauncher.logger.info("Re-enter the password correctly");
				WindowManager.InformUser("password incorrect", "Password not matching", AlertType.ERROR);
			}
		}

		else {
			ApplicationLauncher.logger.info("onEmModelSaveClick:  Failure");
			WindowManager.InformUser("Empty field", "Required fields are empty", AlertType.ERROR);
		}

	}

	// edited mohan
	public boolean CheckDataIsNotEmpty(String user_name,
			String password, String confirm_password, String access_level) {
		boolean validation_status = false;
		validation_status = !user_name.isEmpty();
		if (validation_status) {
			ApplicationLauncher.logger.info("CheckDataISNotEmpty:  user_name: Success");
			validation_status = !password.isEmpty();
			if (validation_status) {
				validation_status = !confirm_password.isEmpty();
			}
		}

		return validation_status;
	}

	// fin
	public void RefreshUserList() {
		username_list.clear();
		JSONObject Userdata = MySQL_Controller.sp_getprocal_users();
		ApplicationLauncher.logger.info("UserList: " + Userdata);
		JSONArray userlist = new JSONArray();
		try {
			userlist = Userdata.getJSONArray("User_list");
		} catch (JSONException e1) {

			e1.printStackTrace();
			ApplicationLauncher.logger.error("RefreshUserList: JSONException1: " + e1.getMessage());
		}
		for (int i = 0; i < userlist.length(); i++) {
			String user_name = "";
			String password = "";
			// edited mohan
			String confirm_password = "";
			String access_level = "";
			try {
				JSONObject model = (JSONObject) userlist.get(i);
				user_name = model.getString("username");
				password = model.getString("password");
				confirm_password = password;
				// edited mohan
				// confirm_password= model.getString("confirm_password");
				//
				access_level = model.getString("access_level");
			} catch (JSONException e) {

				e.printStackTrace();
				ApplicationLauncher.logger.error("RefreshUserList: JSONException2: " + e.getMessage());
			}
			username_list.add(new ProcalUserModel(user_name,
					password, confirm_password, access_level));
		}

	}

	/*
	 * public void InformUser(String title, String info,AlertType Alert_type){
	 * TextBoxDialog TextBoxDialogobj = new TextBoxDialog();
	 * TextBoxDialogobj.TriggerUserInfoPlatFormLater(title, info,Alert_type);
	 * }
	 */
	// edited mohan
	public boolean check_correct_password(String password, String confirm_password) {
		boolean password_status = false;
		if (password.equals(confirm_password)) {

			password_status = true;
		} else {
			password_status = false;
		}
		return password_status;
	}
	// fin

	@FXML
	public void btnUserAccessControlOnClick() {

		ApplicationLauncher.logger.info("btnUserAccessControlOnClick :Entry");
		UAC_Timer = new Timer();
		UAC_Timer.schedule(new UserAccessControlDisplayTrigger(), 100);
	}

	class UserAccessControlDisplayTrigger extends TimerTask {

		@Override
		public void run() {

			ApplicationLauncher.logger.debug("UserAccessControlDisplayTrigger : Run Entry");
			Platform.runLater(() -> {
				try {

					UserAccessControlDisplay();

				} catch (JSONException e) {

					e.printStackTrace();
					ApplicationLauncher.logger
							.error("UserAccessControlDisplayTrigger: JSONException: " + e.getMessage());
				}
			});

			UAC_Timer.cancel();
		}

	}

	public void UserAccessControlDisplay() throws JSONException {
		ApplicationLauncher.logger.debug("UserAccessControlDisplay : Entry");

		// ApplicationLauncher.logger.info("loadModbusPlcGUI: entry");
		// ApplicationHomeController.setModbusPlcGuiDisplayed(true);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/setting/UAC" + ConstantApp.THEME_FXML));
		Scene newScene;
		try {
			newScene = new Scene(loader.load());
		} catch (IOException ex) {

			ex.printStackTrace();
			ApplicationLauncher.logger.error("UserAccessControlDisplay: IOException:" + ex.getMessage());
			return;
		}

		Stage displayStage = new Stage();
		displayStage.initModality(Modality.APPLICATION_MODAL);
		// https://stackoverflow.com/questions/38481914/disable-background-stage-javafx?rq=1
		displayStage.getIcons().add(new Image("file:images/" + ConstantVersion.APP_ICON_FILENAME));
		displayStage.setScene(newScene);
		displayStage.setTitle(ConstantVersion.APPLICATION_NAME + " - UAC");
		displayStage.setResizable(false);
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		int width = 860;// 1083;//413+10;
		int height = 520;// 649+35;//849+35;//314+35;
		/*
		 * InstantMetricsStage.setX(primaryScreenBounds.getMinX() +
		 * primaryScreenBounds.getWidth() - width);
		 * InstantMetricsStage.setY(primaryScreenBounds.getMinY() +
		 * primaryScreenBounds.getHeight() - height);
		 */
		displayStage.setWidth(width);
		displayStage.setHeight(height);

		displayStage.setAlwaysOnTop(false);
		displayStage.show();
		// displayStage.toBack();

		/*
		 * displayStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		 * public void handle(WindowEvent we) {
		 * we.consume();
		 * }
		 * 
		 * });
		 */

	}

}
