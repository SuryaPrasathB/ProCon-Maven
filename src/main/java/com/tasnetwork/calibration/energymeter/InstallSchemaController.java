package com.tasnetwork.calibration.energymeter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class InstallSchemaController implements Initializable {

	@FXML
	private TextField txt_sql_server_location;

	@FXML
	private TextField txt_sql_file_location;

	@FXML
	private Button install_schema_btn;

	public static String sql_server_location;
	public static String sql_schema_file_location;
	public static int x;
	public static String line0;
	public static String line1;
	public static boolean sqlEXE_NotExist = true;

	Timer InstallSchemaTimer;

	public void install_schema_on_click() throws IOException {

		InstallSchemaTrigger();

	}

	public void sql_server_location_onclick() {
		ApplicationLauncher.logger.info("sql_server_location_onclick: Entry");
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedDirectory = directoryChooser.showDialog(new Stage());

		if (selectedDirectory == null) {

			ApplicationLauncher.logger.info("No Directory selected");

		} else {

			ApplicationLauncher.logger.info("showSaveDialog file location" + selectedDirectory.getAbsolutePath());
			txt_sql_server_location.setText(selectedDirectory.getAbsolutePath());
			ValidateSQLServerFilePath();

		}
	}

	public void ValidateSQLServerFilePath() {
		ApplicationLauncher.logger.debug("ValidateSQLServerFilePath: Entry");

		sql_server_location = txt_sql_server_location.getText();

		String sql_exe_file = sql_server_location + "\\mysql.exe";
		ApplicationLauncher.logger.info(" sql_exe_file " + sql_exe_file);
		File f = new File(sql_exe_file);
		if (f.exists() && !f.isDirectory()) {
			ApplicationLauncher.logger.info("sql file exist :");
			sqlEXE_NotExist = false;

		} else {
			ApplicationLauncher.logger.info("sql file does not exist :");
		}

	}

	public void select_sql_file_onclick() {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Select File location");
		FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("sql files", "*.sql");
		chooser.getExtensionFilters().add(fileExtensions);
		java.io.File file = chooser.showOpenDialog(new Stage());
		ApplicationLauncher.logger.info("showOpenDialog file location" + file);

		String file_location = file.toString();
		sql_schema_file_location = file_location;
		txt_sql_file_location.setText(file_location);
		install_schema_btn.setDisable(false);

		String sql_file_check = (sql_schema_file_location.substring(sql_schema_file_location.lastIndexOf(".") + 1));
		;

		ApplicationLauncher.logger.info("sql_file_check :" + sql_file_check);

	}

	public static void InformUser(String title, String info, AlertType Alert_type) {
		// TextBoxDialog TextBoxDialogobj = new TextBoxDialog();
		// TextBoxDialogobj.TriggerUserInfoPlatFormLater(title, info, Alert_type);
	}

	public void InstallSchemaTrigger() {
		InstallSchemaTimer = new Timer();
		InstallSchemaTimer.schedule(new install_schema(), 100);// 1000);
		ApplicationLauncher.logger.info("InstallSchemaTrigger Invoked:");
	}

	class install_schema extends TimerTask {
		public void run() {
			WindowManager.setCursor(Cursor.WAIT);

			ApplicationLauncher.logger.info("install_schema: Invoked");
			try {
				install_schema_execution();
			} catch (IOException e) {

				e.printStackTrace();
				ApplicationLauncher.logger.error("install_schema : IOException: " + e.toString());
			}

			InstallSchemaTimer.cancel();
			WindowManager.setCursor(Cursor.DEFAULT);

		}
	}

	public void install_schema_execution() throws IOException {
		ValidateSQLServerFilePath();
		if (!sqlEXE_NotExist) {
			ApplicationLauncher.logger.info("sqlEXE_NotExist: : " + sqlEXE_NotExist);
			String DBusername = ConstantAppConfig.DB_USERNAME;
			String DBpassword = ConstantAppConfig.DB_PASSWORD;

			ProcessBuilder builder0 = new ProcessBuilder(
					// C:\Program Files\MySQL\MySQL Server 5.5\bin
					"cmd.exe", "/c", "cd " + sql_server_location + " &&mysql -u" + DBusername + " -p" + DBpassword
							+ " < " + sql_schema_file_location);
			// mysqldump -uroot -pprasanth@tas --no-create-info --skip-triggers
			// ltcalibration results --where=
			// "time_stamp>= 1528702011 AND time_stamp<= 1528702011" -r E:\backup.sql
			// sql_schema_file_location=C:\\Users\\nana\\Documents\\dumps\\ProCAL_MySQLDump20180805_S3.4.8_V1.7\\ProCAL-SchemaV1.7.sql
			builder0.redirectErrorStream(true);
			x = -1;
			Process p0 = builder0.start();
			try {
				x = p0.waitFor();

			} catch (InterruptedException e1) {

				e1.printStackTrace();
				ApplicationLauncher.logger.error("install_schema_execution : InterruptedException: " + e1.toString());
			}
			BufferedReader r0 = new BufferedReader(new InputStreamReader(p0.getInputStream()));

			line0 = r0.readLine();
			line1 = r0.readLine();
		}
		Platform.runLater(() -> {
			try {
				show_install_schema_result();
			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger.error("install_schema_execution: Exception: " + e.getMessage());
			}
		});
	}

	public void show_install_schema_result() {

		if (!sqlEXE_NotExist) {
			if (x == 0) {

				Alert alert = new Alert(AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("file:images/" + ConstantVersion.APP_ICON_FILENAME));
				alert.setTitle(ConstantVersion.APPLICATION_NAME);
				alert.setHeaderText("Information");
				String s = "DB Schema installation completed.\nKindly restart the application";
				alert.setContentText(s);

				alert.showAndWait();
				ApplicationLauncher.logger.info("<------------Exit " + ConstantVersion.APPLICATION_NAME
						+ " application with success code 5---------->\n");
				Platform.exit();
				System.exit(5);
			} else {

				ApplicationLauncher.logger.info("line1:" + line1);
				ApplicationLauncher.logger.info("line:" + line0);
				// InformUser(ConstantApp.APPLICATION_NAME , "Error in Installing Schema
				// :"+line0+line1,AlertType.INFORMATION);
				Alert alert = new Alert(AlertType.ERROR);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("file:images/" + ConstantVersion.APP_ICON_FILENAME));
				alert.setTitle(ConstantVersion.APPLICATION_NAME);
				alert.setHeaderText("Error");
				String s = "DB schema installation completed with below error\n" + line0 + "\n" + line1;
				alert.setContentText(s);

				alert.showAndWait();
				ApplicationLauncher.logger.info("<------------Exit " + ConstantVersion.APPLICATION_NAME
						+ " application with error code 4---------->\n");
				Platform.exit();
				System.exit(4);

			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("file:images/" + ConstantVersion.APP_ICON_FILENAME));
			alert.setTitle(ConstantVersion.APPLICATION_NAME);
			alert.setHeaderText("Error");
			String s = "mysql.exe does not exist in the specified path\nPlease provide valid path";
			alert.setContentText(s);
			alert.showAndWait();
		}

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		txt_sql_server_location.setEditable(false);
		txt_sql_server_location.setEditable(false);
		install_schema_btn.setDisable(true);
		String sql_location = ConstantAppConfig.SQL_LOCATION;
		txt_sql_server_location.setText(sql_location);
		if (!sql_location.isEmpty()) {
			sql_server_location = sql_location;
		}

	}
}
