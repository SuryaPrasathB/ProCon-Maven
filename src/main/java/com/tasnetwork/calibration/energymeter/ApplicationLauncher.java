package com.tasnetwork.calibration.energymeter;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;
import com.tasnetwork.spring.config.ConfigLoader;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

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

	private static long appStartTimeMs;
	private static long springStartTimeMs;
	private static long springDurationMs;
	private static long javafxInitDurationMs;

	private static String[] savedArgs = new String[0];

	/**
	 * Saves parameters before JavaFX UI is created.
	 *
	 * @throws Exception when initialization setup fails
	 */
	@Override
	public void init() throws Exception {
		if (getParameters() != null && getParameters().getRaw() != null) {
			savedArgs = getParameters().getRaw().toArray(new String[0]);
		}
	}

	/**
	 * Boots the Spring Application Context. Called asynchronously during Splash
	 * Screen background initialization.
	 */
	public static void bootSpringContext() {
		springStartTimeMs = System.currentTimeMillis();
		springContext = SpringApplication.run(ApplicationLauncher.class, savedArgs);
		springDurationMs = System.currentTimeMillis() - springStartTimeMs;
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
		if (springContext != null) {
			springContext.close();
		}
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
		WindowManager.start(initStage);
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

	public static long getAppStartTimeMs() {
		return appStartTimeMs;
	}

	/**
	 * Launches the JavaFX application.
	 *
	 * @param args command-line arguments passed to the application
	 */
	public static void main(String[] args) {
		appStartTimeMs = System.currentTimeMillis();
		launch(args);
	}
}