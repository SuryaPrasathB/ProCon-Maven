package com.tasnetwork.calibration.energymeter;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.effect.DropShadow;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.util.Duration;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.paint.Color;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;
import com.tasnetwork.calibration.energymeter.constant.ConveyorConfigLoader;
import com.tasnetwork.calibration.energymeter.constant.ProCalCustomerConfiguration;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.custom1report.Custom1ReportConfigLoader;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
import com.tasnetwork.calibration.energymeter.database.MySQL_Interface;
import com.tasnetwork.calibration.energymeter.deployment.TextBoxDialog;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.reportprofile.ReportProfileOperationConfigLoader;
import com.tasnetwork.calibration.energymeter.testreport.config.ReportConfigLoader;
import com.tasnetwork.calibration.energymeter.util.ErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;
import com.tasnetwork.spring.orm.model.AppConfig;
import com.tasnetwork.calibration.conveyor.ConveyorPalletTracking;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.configloader.TerminalBayConfigLoader;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdConfigLoader;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdRadiant;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;

public class WindowManager {

	private static VBox splashLayout;
	private static boolean SplashFadeOut = false;
	private static final int SPLASH_WIDTH = 676;
	private static final int SPLASH_HEIGHT = 227;

	public static void SplashInit() {

		// File file = new File("@../../images/procal-splash.png");
		// ImageView splash = new ImageView(new Image(file.toURI().toString()));
		// ImageView splash = new ImageView(new
		// Image("file:images/"+ConstantVersion.SPLASH_FILENAME));
		ImageView splash = new ImageView(new Image(com.tasnetwork.calibration.energymeter.ApplicationLauncher.class
				.getResourceAsStream("/images/" + ConstantVersion.SPLASH_FILENAME)));

		splashLayout = new VBox();
		splashLayout.getChildren().addAll(splash);
		splashLayout.setStyle(
				"-fx-padding: 5; -fx-background-color: lightblue; -fx-border-width:5; -fx-border-color: linear-gradient(to bottom, lightblue, derive(lightblue, 50%));");
		splashLayout.setEffect(new DropShadow());
	}

	public static void showSplash(Stage splashStage) {
		SplashInit();
		Scene splashScene = new Scene(splashLayout);
		splashStage.initStyle(StageStyle.UNDECORATED);
		final Rectangle2D bounds = Screen.getPrimary().getBounds();
		splashStage.setScene(splashScene);
		splashStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH / 2);
		splashStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT / 2);
		splashStage.getIcons().add(new Image(com.tasnetwork.calibration.energymeter.ApplicationLauncher.class
				.getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME)));

		splashStage.centerOnScreen();
		splashStage.show();
		splashStage.toFront();
		FadeTransition fadeSplash = new FadeTransition(Duration.seconds(3.0), splashLayout);
		fadeSplash.setFromValue(0.0);
		fadeSplash.setToValue(1.0);
		SplashFadeOut = false;
		fadeSplash.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				ApplicationLauncher.logger
						.info("ApplicationLauncher : showSplash Entry0");
				if (SplashFadeOut) {
					ApplicationLauncher.logger
							.info("ApplicationLauncher : showSplash Entry1");
					splashStage.hide();
					LoginPage();
				} else {
					try {
						ApplicationLauncher.logger
								.info("ApplicationLauncher : showSplash Entry2");
						Thread.sleep(2000);
						fadeSplash.setFromValue(1.0);
						fadeSplash.setToValue(0.0);
						fadeSplash.playFromStart();
						ApplicationLauncher.logger
								.info("ApplicationLauncher : showSplash Entry3");
						SplashFadeOut = true;
					} catch (InterruptedException e) {

						e.printStackTrace();
					}
				}
			}
		});
		fadeSplash.play();

	}

	public static void start(Stage initStage) throws Exception {

		String version = ConstantVersion.APPLICATION_VERSION;

		ApplicationLauncher.initLogger();

		initStage = new Stage(StageStyle.DECORATED);

		ApplicationLauncher.logger.info("<------------"
				+ ConstantVersion.APPLICATION_NAME + " Version " + version + "----LAUNCHED---------->\n");
		initStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override

			public void handle(WindowEvent e) {

				if (check_alerttype_btn()) {
					ApplicationLauncher.logger
							.info("<------------ Spring App Context closing... ---------->\n");
					if (ProcalFeatureEnable.REPORT_GENERATION_V2_ENABLED) {
						// DeviceDataManagerController.getSpringAppCtx().close();
						ApplicationLauncher.springContext.close();
					}
					ApplicationLauncher.logger
							.info("<------------ Spring App Context closed ---------->\n");
					// ctx.close();
					ApplicationLauncher.logger.info(
							"<------------Exiting " + ConstantVersion.APPLICATION_NAME + " application---------->\n");
					Platform.exit();
					System.exit(0);
				}

				else {
					e.consume();
				}
			}
		});
		ApplicationLauncher.setPrimaryStage(initStage);

		AppConfigLoader.LoadConfigProperty();
		ReportConfigLoader.setConfigFilePathName(ConstantAppConfig.REPORT_CONFIG_FILE_PATH,
				ConstantAppConfig.REPORT_CONFIG_FILE_NAME);
		ReportConfigLoader.init();
		ApplicationLauncher.logger
				.info("ApplicationLauncher : CONFIG_FILE_VERSION: " + ConstantVersion.CONFIG_FILE_VERSION);
		ApplicationLauncher.logger
				.info("ApplicationLauncher : REF_STD_MAX_OUTPUT_FREQ_IN_MEGA_HERTZ: "
						+ ConstantRefStdRadiant.REF_STD_MAX_OUTPUT_FREQ_IN_MEGA_HERTZ);

		AssertValidation.assertLicenceVerification();

		GuiUtils.FormatPulseRate("2500000000");
		GuiUtils.FormatPulseRate("125");
		com.tasnetwork.calibration.energymeter.util.SystemUtils
				.deleteLogFilesOlderThanNdays(ConstantAppConfig.DeleteLogFilesforX_NoOfPreviousDays, "./logs/");
		// AssertValidation.AssertLagLead();
		boolean dbConnected = MySQL_Controller.ValidateDB_Schema_Exist();
		if (dbConnected) {

			AppConfigLoader.LoadPropertiesFromDB();

			assertNoOtherInstanceRunning();

			Thread.sleep(2000);
			if ((!ApplicationLauncher.allready_running)) {

				// ProcalFeatureEnable.Init();
				ProCalCustomerConfiguration.Init();
				ConstantApp.powerSourceInit();
				ConstantReport.ConstReportInit();
				// ApplicationLauncher.logger.debug("ApplicationLauncher: Test1");
				ReportConfigManager.LoadReportHeaderConfigProperty();
				// ApplicationLauncher.logger.debug("ApplicationLauncher: Test2");
				ReportConfigManager.LoadReportExcelConfigProperty();
				// ApplicationLauncher.logger.debug("ApplicationLauncher: Test3");
				ReportConfigManager.LoadReportFileLocationProperty();
				// ApplicationLauncher.logger.debug("ApplicationLauncher: Test4");

				// LscsCalibrationConfigLoader.setLscsCalibrationFileName(ConstantAppConfig.LSCS_POWER_SOURCE_CALIBRATION_FILE_PATH
				// ,ConstantAppConfig.LSCS_POWER_SOURCE_CALIBRATION_FILE_NAME);
				// ApplicationLauncher.logger.debug("ApplicationLauncher: Test5");
				ConstantRefStdConfigLoader.setRefStdConstantConfigFileName(
						ConstantAppConfig.REFSTD_CONSTANT_CONFIG_FILE_PATH,
						ConstantAppConfig.REFSTD_CONSTANT_CONFIG_FILE_NAME);
				// ApplicationLauncher.logger.debug("ApplicationLauncher: Test6");
				ReportProfileOperationConfigLoader.setConfigFilePathName(ConstantAppConfig.REPORT_PROFILEV2_FILE_PATH,
						ConstantAppConfig.REPORT_PROFILEV2_FILE_NAME);

				TerminalBayConfigLoader.setConfigFilePathName(ConstantConveyorConfig.TERMINAL_CONFIG_FILE_PATH,
						ConstantConveyorConfig.TERMINAL_CONFIG_FILE_NAME);
				TerminalBayConfigLoader.init();
				// String configFilePath = "src\\resources\\DevSysTerminalConfigV1_1.json";
				// DeviceDataManagerController.getMyConveyorTree().init(configFilePath);
				MySqlServiceManager.springDataInit();
				ConstantConveyor.init();

				if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {
					ConveyorDataManager.loadDeviceSettingFromDb();
				}

				ConstantRefStdConfigLoader.init();
				// ApplicationLauncher.logger.debug("ApplicationLauncher: Test7");
				com.tasnetwork.calibration.energymeter.constant.ConstantRefStdConfigLoader
						.loadRefStdConstantConfigProperty();
				// ApplicationLauncher.logger.debug("ApplicationLauncher: Test8");
				// LscsCalibrationConfigLoader.init();
				ConveyorPalletTracking.resetPositionToMeterSerialNoMapToDefaultMappings();

				ReportProfileOperationConfigLoader.init();
				// if(ProcalFeatureEnable.CONVEYOR_FEATURE_ENABLED) {
				ConveyorConfigLoader.setConveyorConfigFileName(ConstantAppConfig.CONVEYOR_CONFIG_FILE_PATH,
						ConstantAppConfig.CONVEYOR_CONFIG_FILE_NAME);
				ConveyorConfigLoader.init();
				ApplicationLauncher.logger
						.info("Conveyor: getMaxNoOfDutSupported: "
								+ DeviceDataManagerController.getConveyorConfigParsedKey().getMaxDutSupported());

				ApplicationLauncher.logger
						.info("Conveyor: getDutHardwareIdInitialValue: " + DeviceDataManagerController
								.getConveyorConfigParsedKey().getDutHardwareIdInitialValue());

				// }
				if (ProcalFeatureEnable.REPORT_GENERATION_V2_ENABLED) {
					DeviceDataManagerController.springDataInit();
					appConfigInit();
				} else {
					// DeviceDataManagerController.getSpringAppCtx().close();
					// JavaFXSpringApp.springContext.close();
				}

				try {
					if (ConstantAppConfig.REPORT_PROFILE_CONFIG_PATH_LIST.size() > 1) {
						Custom1ReportConfigLoader.setConfigFilePathName(ConstantAppConfig.REPORT_PROFILE_PATH,
								ConstantAppConfig.REPORT_PROFILE_CONFIG_PATH_LIST.get(1));
						Custom1ReportConfigLoader.init();
					} else {
						ApplicationLauncher.logger
								.info("start: custome report profile not loaded!");
					}
				} catch (Exception e) {

					e.printStackTrace();
					ApplicationLauncher.logger
							.error("start: Exception: " + e.getMessage());

				}

				BayUtils.init();
				/* BayUtils_Cluster2.init(); */
				// AssertValidation.assertGetTargetVoltageRms();
				// AssertValidation.assertGetTargetCurrentRms();
				// AssertValidation.assertCalculateDegreeWithPf();
				// ConstantFeatureEnable.Init();
				// ConstantReport.ConstReportInit();
				// AssertValidation.dutCommandTesting();
				// AssertValidation.assertGenerateNextNewHardwareIds();
				// AssertValidation.fetchPalletsByBayState();
				Thread.setDefaultUncaughtExceptionHandler(ApplicationLauncher::handleException);
				boolean systemStatus = false;
				boolean systemStatusExceptionOccured = false;
				try {
					systemStatus = com.tasnetwork.calibration.energymeter.util.SystemUtils.LoadSystemTime();

				} catch (Exception e) {

					e.printStackTrace();
					ApplicationLauncher.logger
							.error("start: system status Exception: " + e.getMessage());
					systemStatusExceptionOccured = true;

				}
				if (systemStatus) {

					SplashAndLoginDisplay();
					// LoginPage();
				} else {

					if (ProcalFeatureEnable.LICENSE_FEATURE_DISPLAY_ENABLED) {

						Alert alert = new Alert(AlertType.CONFIRMATION);
						Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
						// stage.getIcons().add(new
						// Image(com.tasnetwork.calibration.energymeter.ApplicationLauncher.class.getResourceAsStream("/images/"
						// + ConstantVersion.APP_ICON_FILENAME));
						stage.getIcons().add(new Image(com.tasnetwork.calibration.energymeter.ApplicationLauncher.class
								.getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME)));
						alert.setTitle(ConstantVersion.APPLICATION_NAME);
						alert.setHeaderText(ErrorCodeMapping.ERROR_CODE_3002);
						alert.setContentText(ErrorCodeMapping.ERROR_CODE_3002_MSG);

						if (systemStatusExceptionOccured) {
							alert.setHeaderText(ErrorCodeMapping.ERROR_CODE_3003);
							alert.setContentText(ErrorCodeMapping.ERROR_CODE_3003_MSG);
						}

						alert.getButtonTypes().clear();
						alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

						// Deactivate Defaultbehavior for yes-Button:
						Button yesButton = (Button) alert.getDialogPane().lookupButton(ButtonType.YES);
						yesButton.setDefaultButton(true);

						// Activate Defaultbehavior for no-Button:
						Button noButton = (Button) alert.getDialogPane().lookupButton(ButtonType.NO);
						noButton.setDefaultButton(false);

						final Optional<ButtonType> result = alert.showAndWait();
						if (result.get() == ButtonType.YES) {
							licenseHandlePage();
						} else {
							ApplicationLauncher.logger
									.info("<------------Exit " + ConstantVersion.APPLICATION_NAME
											+ " application with error code 00---------->\n");
							Platform.exit();
							System.exit(0);
						}

					} else {

						Alert alert = new Alert(AlertType.ERROR);
						Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
						// stage.getIcons().add(new
						// Image(com.tasnetwork.calibration.energymeter.ApplicationLauncher.class.getResourceAsStream("/images/"
						// + ConstantVersion.APP_ICON_FILENAME));
						stage.getIcons().add(new Image(com.tasnetwork.calibration.energymeter.ApplicationLauncher.class
								.getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME)));
						alert.setTitle(ConstantVersion.APPLICATION_NAME);
						alert.setHeaderText(ErrorCodeMapping.ERROR_CODE_3001);
						String s = ErrorCodeMapping.ERROR_CODE_3001_MSG;
						alert.setContentText(s);
						alert.showAndWait();
						ApplicationLauncher.logger.info("<------------Exit "
								+ ConstantVersion.APPLICATION_NAME + " application with error code 0---------->\n");
						Platform.exit();
						System.exit(0);
					}

				}

			} else {

				Alert alert = new Alert(AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				// stage.getIcons().add(new
				// Image(com.tasnetwork.calibration.energymeter.ApplicationLauncher.class.getResourceAsStream("/images/"
				// + ConstantVersion.APP_ICON_FILENAME));
				stage.getIcons().add(new Image(com.tasnetwork.calibration.energymeter.ApplicationLauncher.class
						.getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME)));
				alert.setTitle(ConstantVersion.APPLICATION_NAME);
				String s = ConstantVersion.APPLICATION_NAME + " already running";
				alert.setContentText(s);

				alert.showAndWait();
				ApplicationLauncher.logger
						.info("<------------Exit " + ConstantVersion.APPLICATION_NAME + " application---------->\n");
				Platform.exit();
				System.exit(0);

			}
		} else {
			if (!MySQL_Interface.bDB_SchemaExist) {
				Install_New_schema();
			} else {

				ApplicationLauncher.logger.info("<------------Exit "
						+ ConstantVersion.APPLICATION_NAME + " application exit with errorcode 1\n---------->\n");
				Platform.exit();
				System.exit(1);
			}
		}
	}

	public static void appConfigInit() {

		String customerId = "0";
		String propertyName = "ReportTestExecutionFilterByEndDate";
		Optional<AppConfig> appConfigDataOpt = DeviceDataManagerController.getAppConfigService()
				.findFirstByCustomerIdAndPropertyName(customerId, propertyName);
		if (appConfigDataOpt.isPresent()) {
			AppConfig appConfigData = appConfigDataOpt.get();
			String result = appConfigData.getPropertyValue();
			if ((result.equalsIgnoreCase("yes")) || (result.equalsIgnoreCase("y"))) {
				// ConstantAppConfig.REPORT_TEST_EXECUTION_FILTER_BY_END_DATE = true;
			}

		}

	}

	public static void setCursor(Cursor value) {
		ApplicationLauncher.logger.info("ApplicationLauncher : setCursor: Entry: Value: " + value);
		ApplicationLauncher.getPrimaryStage().getScene().setCursor(value);

	}

	public static void InformUser(String title, String info, AlertType Alert_type) {
		TextBoxDialog TextBoxDialogobj = new TextBoxDialog();
		TextBoxDialogobj.TriggerUserInfoPlatFormLater(title, info, Alert_type);
	}

	public static boolean check_alerttype_btn() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		// stage.getIcons().add(new
		// Image(com.tasnetwork.calibration.energymeter.ApplicationLauncher.class.getResourceAsStream("/images/"
		// + ConstantVersion.APP_ICON_FILENAME));
		stage.getIcons().add(new Image(com.tasnetwork.calibration.energymeter.ApplicationLauncher.class
				.getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME)));
		alert.setTitle(ConstantVersion.APPLICATION_NAME + " Exit");
		String s = "Are you sure, you want to exit?";
		// if(ProjectExecutionController.getExecutionInProgress()){
		// s = "Test Execution is still in progress. Kindly stop execution or wait until
		// execution is completed";
		// alert = new Alert(AlertType.INFORMATION);
		// stage = (Stage) alert.getDialogPane().getScene().getWindow();
		// stage.getIcons().add(new
		// Image(com.tasnetwork.calibration.energymeter.ApplicationLauncher.class.getResourceAsStream("/images/"
		// + ConstantVersion.APP_ICON_FILENAME)));
		// alert.setTitle(ConstantVersion.APPLICATION_NAME +" Exit");
		// }
		alert.setContentText(s);
		boolean check_test_run = false;
		Optional<ButtonType> result = alert.showAndWait();

		if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
			check_test_run = true;
		}
		/*
		 * else{
		 * check_test_run=false;
		 * 
		 * }
		 */
		return check_test_run;
	}

	public static void assertNoOtherInstanceRunning() {

		new Thread(() -> {
			try {

				new ServerSocket(ConstantAppConfig.APP_INSTANCE_SERVER_PORT).accept();

			} catch (IOException e) {

				ApplicationLauncher.allready_running = true;
				ApplicationLauncher.logger
						.error("assertNoOtherInstanceRunning: APP_INSTANCE_SERVER_PORT: "
								+ ConstantAppConfig.APP_INSTANCE_SERVER_PORT);
				ApplicationLauncher.logger
						.error("assertNoOtherInstanceRunning: IOException: " + e.getMessage());

			}
		}).start();
	}

	public static void Install_New_schema() {

		ApplicationLauncher.logger
				.info("Install_New_schema: Entry");
		Alert alert = new Alert(AlertType.ERROR);
		Stage stage1 = (Stage) alert.getDialogPane().getScene().getWindow();
		stage1.getIcons().add(new Image(com.tasnetwork.calibration.energymeter.ApplicationLauncher.class
				.getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME)));
		alert.setTitle("Database connection failed");

		String sqlURL = ConstantAppConfig.DB_URL + ConstantAppConfig.DB_NAME;
		String DBname = (sqlURL.substring(sqlURL.lastIndexOf("/") + 1));
		alert.setContentText("Database schema <" + DBname + "> does not exist\nDo you want to install schema?");
		alert.getButtonTypes().clear();
		alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

		Optional<ButtonType> result = alert.showAndWait();

		if ((result.isPresent()) && (result.get() == ButtonType.YES)) {
			ApplicationLauncher.logger
					.info("Install_New_schema :button ok pressed!!");
			Parent root = null;
			Stage stage = ApplicationLauncher.getPrimaryStage();

			try {

				root = FXMLLoader.load(com.tasnetwork.calibration.energymeter.ApplicationLauncher.class
						.getResource("/fxml/main/Install_schema" + ConstantApp.THEME_FXML));
			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger
						.error("Install_New_schema : Exception:" + e.getMessage());
			}

			Scene scene = new Scene(root);
			stage.setScene(scene);
			scene.setFill(Color.TRANSPARENT);
			stage.setTitle(ConstantVersion.APPLICATION_NAME + " - " + ConstantVersion.APPLICATION_VERSION);
			stage.show();

		} else {
			ApplicationLauncher.logger
					.info("Install_New_schema :button <No> pressed!!");
		}

	}

	public static void SplashAndLoginDisplay() {

		Stage stage1 = new Stage();

		showSplash(stage1);

	}

	public static void LoginPage() {

		Parent root = null;
		Stage stage = ApplicationLauncher.getPrimaryStage();

		try {
			root = FXMLLoader.load(com.tasnetwork.calibration.energymeter.ApplicationLauncher.class
					.getResource("/fxml/main/LoginPage" + ConstantApp.THEME_FXML));
		} catch (IOException e) {

			e.printStackTrace();
			ApplicationLauncher.logger
					.error("LoginPage: IOException: " + e.getMessage());
		}
		// showSplash(stage);
		stage.getIcons().add(new Image(com.tasnetwork.calibration.energymeter.ApplicationLauncher.class
				.getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME)));
		stage.setMinWidth(300);
		stage.setMinHeight(200);
		// stage.hide();
		// stage.initStyle(StageStyle.DECORATED);
		// stage.setIconified(true);

		Scene scene = new Scene(root);
		// stage.setScene(scene);
		scene.setFill(Color.TRANSPARENT);
		stage.setTitle(ConstantVersion.APPLICATION_NAME + " - " + ConstantVersion.APPLICATION_VERSION);
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
		// stage.setAlwaysOnTop(true);
		stage.toFront();

	}

	public static void licenseHandlePage() {

		Parent root = null;
		Stage stage = ApplicationLauncher.getPrimaryStage();

		try {
			// root =
			// FXMLLoader.load(com.tasnetwork.calibration.energymeter.ApplicationLauncher.class.getResource("/fxml/main/LicenseHandle"
			// + ConstantApp.THEME_FXML));
			root = FXMLLoader.load(com.tasnetwork.calibration.energymeter.ApplicationLauncher.class
					.getResource("/fxml/main/LicenseHandle" + ConstantApp.THEME_FXML));
		} catch (IOException e) {

			e.printStackTrace();
			ApplicationLauncher.logger
					.error("licenseHandlePage: IOException: " + e.getMessage());
		}
		// showSplash(stage);
		stage.getIcons().add(new Image(com.tasnetwork.calibration.energymeter.ApplicationLauncher.class
				.getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME)));
		stage.setMinWidth(300);
		stage.setMinHeight(200);
		// stage.hide();
		// stage.initStyle(StageStyle.DECORATED);
		// stage.setIconified(true);

		Scene scene = new Scene(root);
		// stage.setScene(scene);
		scene.setFill(Color.TRANSPARENT);
		stage.setTitle(ConstantVersion.APPLICATION_NAME + " - " + ConstantVersion.APPLICATION_VERSION);
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
		// stage.setAlwaysOnTop(true);
		stage.toFront();

	}

	/*
	 * public static void licenseHandlePage(){
	 * com.tasnetwork.calibration.energymeter.ApplicationLauncher.com.tasnetwork.
	 * calibration.energymeter.ApplicationLauncher.logger.
	 * info("licenseHandlePage: Entry");
	 * //com.tasnetwork.calibration.energymeter.ApplicationLauncher.com.tasnetwork.
	 * calibration.energymeter.ApplicationLauncher.logger.
	 * info("MeterReadingPopup: entry");
	 * FXMLLoader loader = new
	 * FXMLLoader(com.tasnetwork.calibration.energymeter.ApplicationLauncher.class.
	 * getResource("/fxml/main/LicenseHandle" + ConstantApp.THEME_FXML));
	 * 
	 * Scene newScene;
	 * try {
	 * newScene = new Scene(loader.load());
	 * } catch (IOException ex) {
	 * 
	 * ex.printStackTrace();
	 * ApplicationLauncher.logger.
	 * error("licenseHandlePage :IOException:"+ ex.getMessage());
	 * return;
	 * }
	 * 
	 * Stage licenseHandleStage = new Stage();
	 * //https://stackoverflow.com/questions/36071779/how-to-open-an-additional-
	 * window-in-a-javafx-fxml-app?rq=1
	 * 
	 * Stage primaryStage =
	 * ApplicationLauncher.ApplicationLauncher.getPrimaryStage();
	 * 
	 * licenseHandleStage.setTitle(ConstantVersion.APPLICATION_NAME +
	 * " - "+ConstantVersion.APPLICATION_VERSION);
	 * licenseHandleStage.initModality(Modality.NONE);
	 * licenseHandleStage.initOwner(primaryStage);
	 * //https://stackoverflow.com/questions/38481914/disable-background-stage-
	 * javafx?rq=1
	 * 
	 * licenseHandleStage.setScene(newScene);
	 * //Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
	 * licenseHandleStage.getIcons().add(new
	 * Image(com.tasnetwork.calibration.energymeter.ApplicationLauncher.class.
	 * getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME));
	 * licenseHandleStage.setAlwaysOnTop(true);
	 * licenseHandleStage.setOnCloseRequest(e->e.consume());
	 * licenseHandleStage.showAndWait();
	 * 
	 * }
	 * 
	 */

	public static void CustomDialogInit() {

		Alert alert = new Alert(AlertType.WARNING);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(com.tasnetwork.calibration.energymeter.ApplicationLauncher.class
				.getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME)));
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText("This is a Custom Confirmation Dialog");
		alert.setContentText("We override the style classes of the dialog");

		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(
				com.tasnetwork.calibration.energymeter.ApplicationLauncher.class.getResource("/resources/myDialogs.css")
						.toExternalForm());
		dialogPane.getStyleClass().add("/resources/myDialog");

		alert.show();
	}

}
