package com.tasnetwork.calibration.energymeter.setting;

import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.conveyor.AsyncHttpClient.AsyncClientManager;
import com.tasnetwork.calibration.conveyor.constant.ConstantProTamp;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.WindowManager;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert.AlertType;

public class FirmwareUpgradeController implements Initializable {

	Timer ScanDrivesTaskTimer;

	Timer ScanFilesTaskTimer;

	Timer ValidateTaskTimer;

	Timer DeployTaskTimer;

	private static final ExecutorService threadPool = Executors.newCachedThreadPool();

	@FXML
	private Button btnScanDrives;

	public static Button ref_btnScanDrives;

	@FXML
	private Button btnScanFiles;

	public static Button ref_btnScanFiles;

	@FXML
	private Button btnValidate;
	public static Button ref_btnValidate;

	@FXML
	private ComboBox cmbBxListOfAvailableDrives;
	public static ComboBox ref_cmbBxListOfAvailableDrives;

	@FXML
	private ComboBox cmBxUpgradeType;
	public static ComboBox ref_cmBxUpgradeType;

	@FXML
	private ComboBox cmbBxListOfScannedFiles;
	public static ComboBox ref_cmbBxListOfScannedFiles;

	@FXML
	private Button btnDeploy;
	public static Button ref_btnDeploy;

	public static void updateListOfDrives(String[] Drives) {
		ApplicationLauncher.logger.debug("Entering activity updateListOfDrives");
		// AvailableDriveList.setAdapter(value1);
		// Spinner dropdown1 = findViewById(R.id.list_of_drives);
		// value1 = new String[]{"1", "2", "3"};

		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainContext,
		// android.R.layout.simple_spinner_dropdown_item,
		// value1);//Collections.singletonList(value1)
		// spinnerListOfDrives.setAdapter(adapter);
		Platform.runLater(() -> {
			if (Drives.length != 0) {
				ref_cmbBxListOfAvailableDrives.getItems().setAll(Drives);
				ref_cmbBxListOfAvailableDrives.getSelectionModel().select(0);
				ref_cmbBxListOfAvailableDrives.setDisable(false);
				ref_btnScanFiles.setDisable(false);
				ref_cmbBxListOfScannedFiles.setDisable(true);
				ref_btnValidate.setDisable(true);
				ref_btnDeploy.setDisable(true);
				ref_cmBxUpgradeType.setDisable(true);
				/*
				 * ref_cmbBxListOfLogFolder.setDisable(false);
				 * ref_btnViewLogFile.setDisable(true);
				 * ref_cmbBxListOfLogFiles.setDisable(true);
				 */
			}
		});
		ApplicationLauncher.logger.debug("finishing activity updateListOfDrives");
		/*
		 * if(Drives.length!=0){
		 * //UpdateUtilityScanFilesButton(true);//Button(true);
		 * }
		 */

	}

	public static void updatefilesinselecteddrive(String[] selectedfile) {
		ApplicationLauncher.logger.debug("Entering activity updatefilesinselecteddrive");

		Platform.runLater(() -> {
			if (selectedfile.length != 0) {
				ref_cmbBxListOfScannedFiles.getItems().setAll(selectedfile);
				ref_cmbBxListOfScannedFiles.getSelectionModel().select(0);
				// ref_btnScanFiles.setDisable(false);
				ref_cmbBxListOfScannedFiles.setDisable(false);
				ref_btnValidate.setDisable(false);
				ref_btnDeploy.setDisable(true);
				/*
				 * ref_cmbBxListOfLogFolder.setDisable(false);
				 * ref_btnViewLogFile.setDisable(true);
				 * ref_cmbBxListOfLogFiles.setDisable(true);
				 */
			}
		});

		ApplicationLauncher.logger.debug("finishing activity updatefilesinselecteddrive");

	}

	public static void updateValidationResult(final String validation_result) {
		ApplicationLauncher.logger.debug("Entering activity updateValidationResult");
		ConvErrorCodeMapping.Error_Msg();
		JSONObject validate_reason = ConvErrorCodeMapping.ERROR_CODE_MSG;
		String Error_msgs = null;

		// try {
		Error_msgs = ConvErrorCodeMapping.getKeyErrorCodeID(validation_result);
		ApplicationLauncher.logger.debug("Error_msgs" + Error_msgs);
		/*
		 * } catch (JSONException e) {
		 * e.printStackTrace();
		 * }
		 */
		// StringBuilder validate_reason1=new StringBuilder();
		String validate_reason1;
		if (validation_result.equals("ERROR_CODE_401")) {

			validate_reason1 = (ConvErrorCodeMapping.ERROR_CODE_401_MSG);
			// UpdateUtilityDeployButton(true);
			ref_btnDeploy.setDisable(false);

		} else {

			validate_reason1 = validation_result + " : " + Error_msgs;

		}

		WindowManager.InformUser("Validation Result", validate_reason1, AlertType.INFORMATION);
		ApplicationLauncher.logger.debug("finishing activity updateValidationResult");

	}

	public static void updateDeployStatus(final String Deploy_Result_status) {
		ApplicationLauncher.logger.info("Entering activity updateDeployStatus");
		String Deploy_msg = "Deployed successfully,required Panel reboot for new version execution";
		if (Deploy_Result_status.equals("Deploy Failed")) {
			Deploy_msg = Deploy_Result_status;
			// Deploy_msg = Deploy_Result_status + ":
			// "+ErrorCodeMapping.getKeyValue(Deploy_Result_status);
		}

		WindowManager.InformUser("Deploy Result", Deploy_msg, AlertType.INFORMATION);

		ApplicationLauncher.logger.info("finishing activity updateDeployStatus");
	}

	public static void updateDeployStatusV2(final String Deploy_Result_status) {
		ApplicationLauncher.logger.info("Entering activity updateDeployStatusV2");
		String Deploy_msg = "Deployed successfully,required Panel reboot for new version execution";
		if (!Deploy_Result_status.equals("Success")) {
			// Deploy_msg=Deploy_Result_status;
			Deploy_msg = Deploy_Result_status + ": " + ConvErrorCodeMapping.getKeyValue(Deploy_Result_status);
		}

		WindowManager.InformUser("Deploy Result", Deploy_msg, AlertType.INFORMATION);

		ApplicationLauncher.logger.info("finishing activity updateDeployStatus");
	}

	public static void updateDeployGUI_Status(final String Deploy_Result_status) {
		ApplicationLauncher.logger.info("Entering activity updateDeployGUI_Status");
		String Deploy_msg = "Deployed successfully,required Panel reboot for new version execution";
		if (!Deploy_Result_status.equals("Success")) {
			// Deploy_msg=Deploy_Result_status;
			Deploy_msg = Deploy_Result_status + ": " + ConvErrorCodeMapping.getKeyValue(Deploy_Result_status);
		}

		WindowManager.InformUser("Deploy Result", Deploy_msg, AlertType.INFORMATION);

		ApplicationLauncher.logger.info("finishing activity updateDeployGUI_Status");
	}

	public void InitTaskExecution() {
		ApplicationLauncher.logger.info("FirmwareUpgradeController :initialize: Entry");
		refAssignmentInit();
		ref_cmBxUpgradeType.getItems().addAll(ConstantProTamp.UPGRADE_UTILITY_TYPE_LIST);
		ref_cmBxUpgradeType.getSelectionModel().select(0);
		ref_btnScanFiles.setDisable(true);
		ref_cmbBxListOfScannedFiles.setDisable(true);
		ref_btnValidate.setDisable(true);
		ref_btnDeploy.setDisable(true);
		ref_cmbBxListOfAvailableDrives.setDisable(true);

	}

	public void refAssignmentInit() {
		ref_btnScanFiles = btnScanFiles;
		ref_btnValidate = btnValidate;
		ref_cmbBxListOfAvailableDrives = cmbBxListOfAvailableDrives;
		ref_cmbBxListOfScannedFiles = cmbBxListOfScannedFiles;
		ref_btnDeploy = btnDeploy;
		ref_btnScanDrives = btnScanDrives;
		ref_cmBxUpgradeType = cmBxUpgradeType;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		if (ConstantProTamp.TARGET_DEVICE_IS_WINDOWS) {
			InitTaskExecution();
		} else {
			WindowManager.setCursor(Cursor.WAIT);
			// ApplicationHomeController.DisableLeftMenuButtonsForTestRun();
			Task<Void> jfxTask = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					Platform.runLater(() -> {
						InitTaskExecution();
					});
					return null;
				}
			};

			jfxTask.setOnSucceeded(event -> {
				// ApplicationLauncher.logger.debug ("InitCounter: " + InitCounter);
				WindowManager.setCursor(Cursor.DEFAULT);
				ApplicationHomeController.EnableLeftMenuButtonsForTestRun();
			});

			jfxTask.setOnFailed(event -> {

			});
			threadPool.execute(jfxTask);
		}

	}

	public void ScanDrivesTrigger() {
		ApplicationLauncher.logger.info("ScanDrivesTrigger : Entry");
		ScanDrivesTaskTimer = new Timer();
		ScanDrivesTaskTimer.schedule(new ScanDrivesTask(), 100);
	}

	public void ScanDrives() {
		AsyncClientManager asyncClient = new AsyncClientManager();

		asyncClient.tamperScanAvailabledrives();
	}

	public void ScanFilesTrigger() {
		ApplicationLauncher.logger.info("ScanFilesTrigger : Entry");
		ScanFilesTaskTimer = new Timer();
		ScanFilesTaskTimer.schedule(new ScanFilesTask(), 100);
	}

	public void ScanFiles() {

		String SelectedDrive = ref_cmbBxListOfAvailableDrives.getSelectionModel().getSelectedItem().toString();
		;
		AsyncClientManager asyncClient = new AsyncClientManager();

		asyncClient.tamperScanfilesindrives(SelectedDrive);
	}

	public void ValidateTrigger() {
		ApplicationLauncher.logger.info("ValidateTrigger : Entry");
		ValidateTaskTimer = new Timer();
		ValidateTaskTimer.schedule(new ValidateTask(), 100);
	}

	public void ValidateFiles() {

		String SelectedFile = ref_cmbBxListOfScannedFiles.getSelectionModel().getSelectedItem().toString();
		;

		String SelectedUpgradeType = ref_cmBxUpgradeType.getSelectionModel().getSelectedItem().toString();
		ApplicationLauncher.logger.info("ValidateFiles : SelectedUpgradeType : " + SelectedUpgradeType);
		AsyncClientManager asyncClient = new AsyncClientManager();

		asyncClient.tampervalidateselectedfile(SelectedFile);
	}

	public void DeployTrigger() {
		ApplicationLauncher.logger.info("DeployTrigger : Entry");
		DeployTaskTimer = new Timer();
		DeployTaskTimer.schedule(new DeployTask(), 100);
	}

	public void DeployFiles() {

		String SelectedUpgradeType = ref_cmBxUpgradeType.getSelectionModel().getSelectedItem().toString();
		ApplicationLauncher.logger.info("DeployFiles : SelectedUpgradeType : " + SelectedUpgradeType);

		AsyncClientManager asyncClient = new AsyncClientManager();

		if (SelectedUpgradeType.equals("Firmware")) {
			// asyncClient.tamperDeployFile();
			asyncClient.tamperDeployFileV2();
		}
		// asyncClient.tamperDeployFile();
		if (SelectedUpgradeType.equals("GUI Application")) {
			asyncClient.tamperDeployGUIFile();
		}
	}

	class ScanDrivesTask extends TimerTask {

		public void run() {
			ApplicationLauncher.logger.debug("ScanDrivesTask :Entry");
			WindowManager.setCursor(Cursor.WAIT);
			ScanDrives();
			WindowManager.setCursor(Cursor.DEFAULT);

		}
	};

	class DeployTask extends TimerTask {

		public void run() {
			ApplicationLauncher.logger.debug("DeployTask :Entry");
			WindowManager.setCursor(Cursor.WAIT);
			DeployFiles();
			WindowManager.setCursor(Cursor.DEFAULT);

		}
	};

	class ValidateTask extends TimerTask {

		public void run() {
			ApplicationLauncher.logger.debug("ValidateTask :Entry");
			WindowManager.setCursor(Cursor.WAIT);
			ValidateFiles();
			WindowManager.setCursor(Cursor.DEFAULT);

		}
	};

	class ScanFilesTask extends TimerTask {

		public void run() {
			ApplicationLauncher.logger.debug("ScanFilesTask :Entry");
			WindowManager.setCursor(Cursor.WAIT);
			ScanFiles();
			WindowManager.setCursor(Cursor.DEFAULT);

		}
	};

}
