package com.tasnetwork.calibration.energymeter;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

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
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;
import com.tasnetwork.calibration.energymeter.constant.ConveyorConfigLoader;
import com.tasnetwork.calibration.energymeter.constant.ProCalCustomerConfiguration;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.custom1report.Custom1ReportConfigLoader;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
import com.tasnetwork.calibration.energymeter.database.MySQL_Interface;
import com.tasnetwork.calibration.energymeter.device.*;
import com.tasnetwork.calibration.energymeter.reportprofile.ReportProfileOperationConfigLoader;
import com.tasnetwork.calibration.energymeter.testreport.config.ReportConfigLoader;
import com.tasnetwork.calibration.energymeter.util.ErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;
import com.tasnetwork.calibration.energymeter.util.SystemUtils;
import com.tasnetwork.spring.config.ConfigLoader;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = "com.tasnetwork.spring")
@EnableJpaRepositories(basePackages = "com.tasnetwork.spring.orm.repository")
@EntityScan(basePackages = "com.tasnetwork.spring.orm.model")

@EnableJpaAuditing
@SpringBootApplication(scanBasePackages = { "com.tasnetwork.calibration", "com.tasnetwork.spring.controller" })

public class ApplicationLauncher extends Application {

	/** Application-wide Log4j logger. */
	public static Logger logger = Logger.getLogger(ConstantVersion.APPLICATION_NAME);
	private static Stage primaryStage;
	public static boolean allready_running = false;
	public static boolean reportGenerationFlag = false;

	/**
	 * Initializes system properties used by application startup and logging.
	 */
	static {

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
		System.setProperty("current.date.time", dateFormat.format(new Date()));
		String version = ConstantVersion.APPLICATION_VERSION.replace(".", "_");
		System.out.println("Application version:" + version);
		System.setProperty("Version", "V" + version);
		System.setProperty("AppName", ConstantVersion.APPLICATION_NAME);
	}

	public static ConfigurableApplicationContext springContext;

	/**
	 * Starts the Spring application context before the JavaFX UI is created.
	 *
	 * @throws Exception when Spring Boot cannot be initialized
	 */
	@Override
	public void init() throws Exception {
		String[] args = getParameters().getRaw().toArray(new String[0]);

		springContext = SpringApplication.run(ApplicationLauncher.class, args);
		springContext.getBean(ConfigLoader.class);
	}

	/**
	 * Closes the Spring application context during JavaFX shutdown.
	 *
	 * @throws Exception when the application context cannot be closed
	 */
	@Override
	public void stop() throws Exception {
		logger.info("Shutting down Spring Boot and JavaFX application...");
		springContext.close();
	}

	/**
	 * Logs uncaught exceptions and restores the application cursor when report
	 * generation fails.
	 *
	 * @param t the thread on which the exception occurred
	 * @param e the uncaught exception
	 */
	public static void handleException(Thread t, Throwable e) {
		logger.info("ApplicationLauncher : Unhandled Exception Entry: " + e);
		logger.info("ApplicationLauncher : Unhandled getname:" + t.getName());
		logger.info("ApplicationLauncher : Unhandled getMessage:" + e.getMessage());
		logger.info("ApplicationLauncher : Unhandled getStackTrace:" + e.getStackTrace());

		if (e instanceof IndexOutOfBoundsException || e instanceof ArrayIndexOutOfBoundsException) {
			boolean isUpdateCachedBoundsBug = Arrays.stream(e.getStackTrace()).anyMatch(
					s -> s.getClassName().startsWith("javafx.scene.Parent")
							&& "updateCachedBounds".equals(s.getMethodName()));

			if (isUpdateCachedBoundsBug) {
				logger.info(
						"ApplicationLauncher :Unhandled Exception : Detected an AIOBE or IOBE from the updateCachedBounds bug:");

				e.printStackTrace();
			} else {
				logger.info(
						"ApplicationLauncher :Unhandled Exception : Detected an AIOBE or IOBE that is not an updateCachedBounds bug: "
								+ e);
			}
		} else {
			logger.info("ApplicationLauncher : Unhandled Exception: " + e);
		}
		if (reportGenerationFlag) {

			reportGenerationFlag = false;
			logger.info(
					"ApplicationLauncher :Unhandled Exception : Report Generation failed with unknown error code " + e);
			WindowManager.setCursor(Cursor.DEFAULT);
			Platform.runLater(() -> {
				Alert alert = new Alert(AlertType.ERROR);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image(
						ApplicationLauncher.class.getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME)));
				alert.setTitle(ConstantVersion.APPLICATION_NAME);
				alert.setHeaderText("Report generation failed");
				String s = "Report Generation failed with unknown error code";
				alert.setContentText(s);
				alert.showAndWait();
			});
		}
		logger.info("ApplicationLauncher :Unhandled Exception : getStackTrace2:" + e.getStackTrace().toString());
		ByteArrayOutputStream out1 = new ByteArrayOutputStream();
		PrintStream out2 = new PrintStream(out1);
		e.printStackTrace(out2);
		try {
			String message = out1.toString("UTF8");
			logger.info("ApplicationLauncher :Unhandled Exception : printStackTrace1:" + message);

		} catch (UnsupportedEncodingException e1) {

			e1.printStackTrace();
		}
		logger.info("ApplicationLauncher :Unhandled Exception : tostring: " + e.toString());
		logger.info(
				"ApplicationLauncher :Unhandled Exception : getSuperclass: " + e.getClass().getSuperclass().getName());
		logger.info("ApplicationLauncher :Unhandled Exception : getName: " + e.getClass().getName());
		logger.info("ApplicationLauncher :Unhandled Exception : getSimpleName: " + e.getClass().getSimpleName());
		logger.info("ApplicationLauncher :Unhandled Exception : getCanonicalName: " + e.getClass().getCanonicalName());

	}

	/**
	 * Initializes the primary JavaFX stage, application configuration, database
	 * services, and the first application screen.
	 *
	 * @param initStage JavaFX-provided initial stage
	 * @throws Exception when startup initialization cannot be completed
	 */
	@Override
	public void start(Stage initStage) throws Exception {

		String version = ConstantVersion.APPLICATION_VERSION;

		initLogger();

		initStage = new Stage(StageStyle.DECORATED);

		logger.info("<------------" + ConstantVersion.APPLICATION_NAME + " Version " + version
				+ "----LAUNCHED---------->\n");
		initStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override

			public void handle(WindowEvent e) {

				if (WindowManager.check_alerttype_btn()) {
					logger.info("<------------ Spring App Context closing... ---------->\n");
					if (ProcalFeatureEnable.REPORT_GENERATION_V2_ENABLED) {
						ApplicationLauncher.springContext.close();
					}
					logger.info("<------------ Spring App Context closed ---------->\n");
					logger.info(
							"<------------Exiting " + ConstantVersion.APPLICATION_NAME + " application---------->\n");
					Platform.exit();
					System.exit(0);
				}

				else {
					e.consume();
				}
			}
		});
		setPrimaryStage(initStage);

		AppConfigLoader.LoadConfigProperty();
		ReportConfigLoader.setConfigFilePathName(ConstantAppConfig.REPORT_CONFIG_FILE_PATH,
				ConstantAppConfig.REPORT_CONFIG_FILE_NAME);
		ReportConfigLoader.init();
		logger.info("ApplicationLauncher : CONFIG_FILE_VERSION: " + ConstantVersion.CONFIG_FILE_VERSION);
		logger.info("ApplicationLauncher : REF_STD_MAX_OUTPUT_FREQ_IN_MEGA_HERTZ: "
				+ ConstantRefStdRadiant.REF_STD_MAX_OUTPUT_FREQ_IN_MEGA_HERTZ);

		AssertValidation.assertLicenceVerification();

		GuiUtils.FormatPulseRate("2500000000");
		GuiUtils.FormatPulseRate("125");
		SystemUtils.deleteLogFilesOlderThanNdays(ConstantAppConfig.DeleteLogFilesforX_NoOfPreviousDays, "./logs/");
		boolean dbConnected = MySQL_Controller.ValidateDB_Schema_Exist();
		if (dbConnected) {

			AppConfigLoader.LoadPropertiesFromDB();

			WindowManager.assertNoOtherInstanceRunning();

			Thread.sleep(2000);
			if ((!allready_running)) {

				ProCalCustomerConfiguration.Init();
				ConstantApp.powerSourceInit();
				ConstantReport.ConstReportInit();
				ReportConfigManager.LoadReportHeaderConfigProperty();
				ReportConfigManager.LoadReportExcelConfigProperty();
				ReportConfigManager.LoadReportFileLocationProperty();

				ConstantRefStdConfigLoader.setRefStdConstantConfigFileName(
						ConstantAppConfig.REFSTD_CONSTANT_CONFIG_FILE_PATH,
						ConstantAppConfig.REFSTD_CONSTANT_CONFIG_FILE_NAME);
				ReportProfileOperationConfigLoader.setConfigFilePathName(ConstantAppConfig.REPORT_PROFILEV2_FILE_PATH,
						ConstantAppConfig.REPORT_PROFILEV2_FILE_NAME);

				TerminalBayConfigLoader.setConfigFilePathName(ConstantConveyorConfig.TERMINAL_CONFIG_FILE_PATH,
						ConstantConveyorConfig.TERMINAL_CONFIG_FILE_NAME);
				TerminalBayConfigLoader.init();

				MySqlServiceManager.springDataInit();
				ConstantConveyor.init();

				if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {
					ConveyorDataManager.loadDeviceSettingFromDb();
				}

				ConstantRefStdConfigLoader.init();
				ConstantRefStdConfigLoader.loadRefStdConstantConfigProperty();

				ConveyorPalletTracking.resetPositionToMeterSerialNoMapToDefaultMappings();

				ReportProfileOperationConfigLoader.init();
				ConveyorConfigLoader.setConveyorConfigFileName(ConstantAppConfig.CONVEYOR_CONFIG_FILE_PATH,
						ConstantAppConfig.CONVEYOR_CONFIG_FILE_NAME);
				ConveyorConfigLoader.init();
				ApplicationLauncher.logger.info("Conveyor: getMaxNoOfDutSupported: "
						+ DeviceDataManagerController.getConveyorConfigParsedKey().getMaxDutSupported());

				ApplicationLauncher.logger.info("Conveyor: getDutHardwareIdInitialValue: "
						+ DeviceDataManagerController.getConveyorConfigParsedKey().getDutHardwareIdInitialValue());

				if (ProcalFeatureEnable.REPORT_GENERATION_V2_ENABLED) {
					DeviceDataManagerController.springDataInit();
					WindowManager.appConfigInit();
				}

				try {
					if (ConstantAppConfig.REPORT_PROFILE_CONFIG_PATH_LIST.size() > 1) {
						Custom1ReportConfigLoader.setConfigFilePathName(ConstantAppConfig.REPORT_PROFILE_PATH,
								ConstantAppConfig.REPORT_PROFILE_CONFIG_PATH_LIST.get(1));
						Custom1ReportConfigLoader.init();
					} else {
						ApplicationLauncher.logger.info("start: custome report profile not loaded!");
					}
				} catch (Exception e) {

					e.printStackTrace();
					ApplicationLauncher.logger.error("start: Exception: " + e.getMessage());

				}

				BayUtils.init();

				Thread.setDefaultUncaughtExceptionHandler(ApplicationLauncher::handleException);
				boolean systemStatus = false;
				boolean systemStatusExceptionOccured = false;
				try {
					systemStatus = SystemUtils.LoadSystemTime();

				} catch (Exception e) {

					e.printStackTrace();
					ApplicationLauncher.logger.error("start: system status Exception: " + e.getMessage());
					systemStatusExceptionOccured = true;

				}
				if (systemStatus) {

					WindowManager.SplashAndLoginDisplay();
				} else {

					if (ProcalFeatureEnable.LICENSE_FEATURE_DISPLAY_ENABLED) {

						Alert alert = new Alert(AlertType.CONFIRMATION);
						Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

						stage.getIcons().add(new Image(
								getClass().getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME)));
						alert.setTitle(ConstantVersion.APPLICATION_NAME);
						alert.setHeaderText(ErrorCodeMapping.ERROR_CODE_3002);
						alert.setContentText(ErrorCodeMapping.ERROR_CODE_3002_MSG);

						if (systemStatusExceptionOccured) {
							alert.setHeaderText(ErrorCodeMapping.ERROR_CODE_3003);
							alert.setContentText(ErrorCodeMapping.ERROR_CODE_3003_MSG);
						}

						alert.getButtonTypes().clear();
						alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
						Button yesButton = (Button) alert.getDialogPane().lookupButton(ButtonType.YES);
						yesButton.setDefaultButton(true);
						Button noButton = (Button) alert.getDialogPane().lookupButton(ButtonType.NO);
						noButton.setDefaultButton(false);

						final Optional<ButtonType> result = alert.showAndWait();
						if (result.get() == ButtonType.YES) {
							WindowManager.licenseHandlePage();
						} else {
							logger.info("<------------Exit " + ConstantVersion.APPLICATION_NAME
									+ " application with error code 00---------->\n");
							Platform.exit();
							System.exit(0);
						}

					} else {

						Alert alert = new Alert(AlertType.ERROR);
						Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

						stage.getIcons().add(new Image(
								getClass().getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME)));
						alert.setTitle(ConstantVersion.APPLICATION_NAME);
						alert.setHeaderText(ErrorCodeMapping.ERROR_CODE_3001);
						String s = ErrorCodeMapping.ERROR_CODE_3001_MSG;
						alert.setContentText(s);
						alert.showAndWait();
						logger.info("<------------Exit " + ConstantVersion.APPLICATION_NAME
								+ " application with error code 0---------->\n");
						Platform.exit();
						System.exit(0);
					}

				}

			} else {

				Alert alert = new Alert(AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

				stage.getIcons()
						.add(new Image(getClass().getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME)));
				alert.setTitle(ConstantVersion.APPLICATION_NAME);
				String s = ConstantVersion.APPLICATION_NAME + " already running";
				alert.setContentText(s);

				alert.showAndWait();
				logger.info("<------------Exit " + ConstantVersion.APPLICATION_NAME + " application---------->\n");
				Platform.exit();
				System.exit(0);

			}
		} else {
			if (!MySQL_Interface.bDB_SchemaExist) {
				WindowManager.Install_New_schema();
			} else {

				logger.info("<------------Exit " + ConstantVersion.APPLICATION_NAME
						+ " application exit with errorcode 1\n---------->\n");
				Platform.exit();
				System.exit(1);
			}
		}
	}

	/**
	 * Configures Log4j from the bundled {@code log4j.properties} resource.
	 * Displays an error dialog and terminates when logging cannot be configured.
	 */
	public static void initLogger() {
		String log4jConfigFile = "log4j.properties";
		try {
			InputStream inputStream = ApplicationLauncher.class.getClassLoader().getResourceAsStream(log4jConfigFile);
			if (inputStream != null) {
				Properties props = new Properties();
				props.load(inputStream);
				PropertyConfigurator.configure(props);
			} else {
				throw new FileNotFoundException("log4j.properties not found in classpath");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("initLogger: Exception:" + e.getMessage());
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons()
					.add(new Image(ApplicationLauncher.class
							.getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME)));
			alert.setTitle(ConstantVersion.APPLICATION_NAME);
			alert.setHeaderText("Error in log file");
			String s = "Exception while creating log file \n" + e.getMessage();
			alert.setContentText(s);

			alert.showAndWait();
			logger.info("<------------Exit " + ConstantVersion.APPLICATION_NAME
					+ " application with error code 2---------->\n");
			Platform.exit();
			System.exit(2);

		}

	}

	/**
	 * Returns the primary application window.
	 *
	 * @return the primary JavaFX stage
	 */
	public static Stage getPrimaryStage() {
		return primaryStage;
	}

	/**
	 * Stores the primary application window for use by dialogs and popup screens.
	 *
	 * @param pStage the initialized primary JavaFX stage
	 */
	public static void setPrimaryStage(Stage pStage) {
		ApplicationLauncher.primaryStage = pStage;
	}

	/**
	 * Launches the JavaFX application.
	 *
	 * @param args command-line arguments passed to the application
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
