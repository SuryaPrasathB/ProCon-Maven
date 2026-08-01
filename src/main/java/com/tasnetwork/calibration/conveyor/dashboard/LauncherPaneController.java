package com.tasnetwork.calibration.conveyor.dashboard;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.rejection.Rejection;
import com.tasnetwork.calibration.conveyor.bay.unloading.Unloading;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.GUIUtils;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.util.InputDialogFX;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller for the LauncherPane, responsible for handling button actions
 * to launch various ProCAL applications (both Java JARs and Python scripts).
 * This controller now uses static constants from `ConstantConveyorConfig`
 * for command locations and file names, including screen numbers for Python
 * apps.
 */
public class LauncherPaneController {

    Timer unloadingLaunchTaskTimer;
    Timer rejectionLaunchTaskTimer;
    @FXML
    private Button btn_sta1Launch;

    @FXML
    private Button btn_sta2Launch;

    @FXML
    private Button btn_verificLaunch;

    @FXML
    private Button btn_unloadingLaunch;

    @FXML
    private Button btn_rejectionLaunch;

    // --- Configuration Variables (fetched from ConstantConveyorConfig) ---
    private final String verificCmdLocation;
    private final String verificJarFile;

    private final String sta1CmdLocation;
    private final String sta1JarFile;

    private final String sta2CmdLocation;
    private final String sta2JarFile;

    private final String rejectionCmdLocation;
    private String rejectionPythonScript;

    private final String rejectionPythonExecutablePath;
    private final int rejectionScreenNumber;
    private String rejectionMonitorDisplayName = "";
    private String rejectionBayNameDisplay = "";
    private String rejectionPort = "4001";

    private String rejectionMonitorManufacturerName = "";
    private String rejectionMonitorPidNo = "";
    private String rejectionMonitorSerialNo = "";

    private final String unloadingCmdLocation;
    private String unloadingPythonScript;
    private final String unloadingPythonExecutablePath;
    private final int unloadingScreenNumber;
    private String unloadingMonitorDisplayName = "";
    private String unloadingBayNameDisplay = "";
    private String unloadingPort = "4002";

    private String unloadingMonitorManufacturerName = "";
    private String unloadingMonitorPidNo = "";
    private String unloadingMonitorSerialNo = "";

    // --- End Configuration Variables ---

    /**
     * Constructor for LauncherPaneController.
     * This is where the configuration variables are actually initialized.
     * Adding print statements here to verify values.
     */
    public LauncherPaneController() {
        // Initialize JAR app configuration variables from ConstantConveyorConfig
        verificCmdLocation = ConstantConveyorConfig.CONVEYOR_PROCAL_VERIFIC_CMD_LOCATION;
        verificJarFile = ConstantConveyorConfig.CONVEYOR_PROCAL_VERIFIC_JAR_FILE;

        sta1CmdLocation = ConstantConveyorConfig.CONVEYOR_PROCAL_STA1_CMD_LOCATION;
        sta1JarFile = ConstantConveyorConfig.CONVEYOR_PROCAL_STA1_JAR_FILE;

        sta2CmdLocation = ConstantConveyorConfig.CONVEYOR_PROCAL_STA2_CMD_LOCATION;
        sta2JarFile = ConstantConveyorConfig.CONVEYOR_PROCAL_STA2_JAR_FILE;

        // Initialize Python app configuration variables from ConstantConveyorConfig
        rejectionCmdLocation = ConstantConveyorConfig.CONVEYOR_REJECTION_CMD_LOCATION;
        rejectionPythonScript = ConstantConveyorConfig.CONVEYOR_REJECTION_PYTHON_SCRIPT;
        rejectionPythonExecutablePath = ConstantConveyorConfig.CONVEYOR_REJECTION_PYTHON_EXECUTABLE_PATH;
        rejectionScreenNumber = ConstantConveyorConfig.CONVEYOR_REJECTION_SCREEN_NUMBER - 1;

        rejectionMonitorDisplayName = ConstantConveyorConfig.CONVEYOR_REJECTION_MONITOR_DISPLAY_NAME;
        rejectionBayNameDisplay = ConstantConveyorConfig.CONVEYOR_REJECTION_BAY_DISPLAY_NAME;
        rejectionPort = ConstantConveyorConfig.CONVEYOR_REJECTION_PORT;

        rejectionMonitorManufacturerName = ConstantConveyorConfig.CONVEYOR_REJECTION_MONITOR_MANUFACTURER_NAME;
        rejectionMonitorPidNo = ConstantConveyorConfig.CONVEYOR_REJECTION_MONITOR_PID_NO;
        rejectionMonitorSerialNo = ConstantConveyorConfig.CONVEYOR_REJECTION_MONITOR_SERIAL_NO;

        unloadingCmdLocation = ConstantConveyorConfig.CONVEYOR_UNLOADING_CMD_LOCATION;
        unloadingPythonScript = ConstantConveyorConfig.CONVEYOR_UNLOADING_PYTHON_SCRIPT;
        unloadingPythonExecutablePath = ConstantConveyorConfig.CONVEYOR_UNLOADING_PYTHON_EXECUTABLE_PATH;
        unloadingScreenNumber = ConstantConveyorConfig.CONVEYOR_UNLOADING_SCREEN_NUMBER - 1;

        unloadingMonitorDisplayName = ConstantConveyorConfig.CONVEYOR_UNLOADING_MONITOR_DISPLAY_NAME;
        unloadingBayNameDisplay = ConstantConveyorConfig.CONVEYOR_UNLOADING_BAY_DISPLAY_NAME;
        unloadingPort = ConstantConveyorConfig.CONVEYOR_UNLOADING_PORT;

        unloadingMonitorManufacturerName = ConstantConveyorConfig.CONVEYOR_UNLOADING_MONITOR_MANUFACTURER_NAME;
        unloadingMonitorPidNo = ConstantConveyorConfig.CONVEYOR_UNLOADING_MONITOR_PID_NO;
        unloadingMonitorSerialNo = ConstantConveyorConfig.CONVEYOR_UNLOADING_MONITOR_SERIAL_NO;

        // --- Debugging output for configuration variables ---
        ApplicationLauncher.logger.info("--- LauncherPaneController Configuration ---");
        ApplicationLauncher.logger.info("verificCmdLocation: " + verificCmdLocation);
        ApplicationLauncher.logger.info("verificJarFile: " + verificJarFile);
        ApplicationLauncher.logger.info("sta1CmdLocation: " + sta1CmdLocation);
        ApplicationLauncher.logger.info("sta1JarFile: " + sta1JarFile);
        ApplicationLauncher.logger.info("sta2CmdLocation: " + sta2CmdLocation);
        ApplicationLauncher.logger.info("sta2JarFile: " + sta2JarFile);
        ApplicationLauncher.logger.info("rejectionCmdLocation: " + rejectionCmdLocation);
        ApplicationLauncher.logger.info("rejectionPythonScript: " + rejectionPythonScript);
        ApplicationLauncher.logger.info("rejectionPythonExecutablePath: " + rejectionPythonExecutablePath);
        ApplicationLauncher.logger.info("rejectionScreenNumber -indexed: " + rejectionScreenNumber);

        ApplicationLauncher.logger.info("rejectionMonitorDisplayName: " + rejectionMonitorDisplayName);
        ApplicationLauncher.logger.info("rejectionBayNameDisplay: " + rejectionBayNameDisplay);
        ApplicationLauncher.logger.info("rejectionPort: " + rejectionPort);

        ApplicationLauncher.logger.info("rejectionMonitorManufacturerName: " + rejectionMonitorManufacturerName);
        ApplicationLauncher.logger.info("rejectionMonitorPidNo: " + rejectionMonitorPidNo);
        ApplicationLauncher.logger.info("rejectionMonitorSerialNo: " + rejectionMonitorSerialNo);

        ApplicationLauncher.logger.info("unloadingCmdLocation: " + unloadingCmdLocation);
        ApplicationLauncher.logger.info("unloadingPythonScript: " + unloadingPythonScript);
        ApplicationLauncher.logger.info("unloadingPythonExecutablePath: " + unloadingPythonExecutablePath);
        ApplicationLauncher.logger.info("unloadingScreenNumber-indexed: " + unloadingScreenNumber);
        ApplicationLauncher.logger.info("------------------------------------------");

        ApplicationLauncher.logger.info("unloadingMonitorDisplayName: " + unloadingMonitorDisplayName);
        ApplicationLauncher.logger.info("unloadingBayNameDisplay: " + unloadingBayNameDisplay);
        ApplicationLauncher.logger.info("unloadingPort: " + unloadingPort);

        ApplicationLauncher.logger.info("unloadingMonitorManufacturerName: " + unloadingMonitorManufacturerName);
        ApplicationLauncher.logger.info("unloadingMonitorPidNo: " + unloadingMonitorPidNo);
        ApplicationLauncher.logger.info("unloadingMonitorSerialNo: " + unloadingMonitorSerialNo);
        // --- End Debugging output ---
    }

    /**
     * Initializes the controller after its root element has been completely
     * processed.
     * This method is automatically called by JavaFX.
     * No explicit initialization is needed here as constants are static final.
     */
    @FXML
    public void initialize() {
        // This method is called after the FXML elements are injected.
        // The configuration variables are already initialized in the constructor.
    }

    /**
     * Handles the action event for launching ProCAL STA1.
     * Launches the JAR using predefined constants.
     * 
     * @param event The ActionEvent triggered by the button click.
     */
    @FXML
    void launchProCALSTA1(ActionEvent event) {
        launchJar(sta1CmdLocation, sta1JarFile, btn_sta1Launch);
    }

    /**
     * Handles the action event for launching ProCAL STA2.
     * Launches the JAR using predefined constants.
     * 
     * @param event The ActionEvent triggered by the button click.
     */
    @FXML
    void launchProCALSTA2(ActionEvent event) {
        launchJar(sta2CmdLocation, sta2JarFile, btn_sta2Launch);
    }

    /**
     * Handles the action event for launching ProCAL Verific.
     * Launches the JAR using predefined constants.
     * 
     * @param event The ActionEvent triggered by the button click.
     */
    @FXML
    void launchProCALVerific(ActionEvent event) {
        launchJar(verificCmdLocation, verificJarFile, btn_verificLaunch);
    }

    /**
     * Handles the action event for launching the Rejection Screen (Python
     * application).
     * Launches the Python script using predefined constants, including the screen
     * number.
     * 
     * @param event The ActionEvent triggered by the button click.
     */
    @FXML
    void launchRejectionScreen(ActionEvent event) {
        ApplicationLauncher.logger.debug("launchRejectionScreen Invoked:");
        rejectionLaunchTaskTimer = new Timer();
        rejectionLaunchTaskTimer.schedule(new RejectionGetScreenNumberTask(), 50);
    }

    /**
     * Handles the action event for launching the Unloading Screen (Python
     * application).
     * Launches the Python script using predefined constants, including the screen
     * number.
     * 
     * @param event The ActionEvent triggered by the button click.
     */
    @FXML
    void launchUnloadingScreen(ActionEvent event) {
        ApplicationLauncher.logger.debug("launchUnloadingScreen Invoked:");
        unloadingLaunchTaskTimer = new Timer();
        unloadingLaunchTaskTimer.schedule(new UnloadingGetScreenNumberTask(), 50);
    }

    class UnloadingGetScreenNumberTask extends TimerTask {
        public void run() {

            ApplicationLauncher.logger.debug("UnloadingGetScreenNumberTask : Entry");
            String screenNumber = getUnloadingUserMonitorScreenNumber();
            ApplicationLauncher.logger.debug("UnloadingGetScreenNumberTask : screenNumber: " + screenNumber);
            if (!screenNumber.isEmpty()) {
                ApplicationLauncher.logger.debug("UnloadingGetScreenNumberTask : screenNumber: " + screenNumber);
                if (Integer.parseInt(screenNumber) == -1) {

                    ApplicationLauncher.logger.debug("UnloadingGetScreenNumberTask : launchPythonAppV3 : Hit1");
                    launchPythonAppV3(unloadingCmdLocation, unloadingPythonScript, unloadingPythonExecutablePath,
                            unloadingMonitorManufacturerName,
                            unloadingMonitorPidNo, unloadingMonitorSerialNo, unloadingBayNameDisplay, unloadingPort,
                            btn_unloadingLaunch);

                } else {
                    int screenNumberIndex = Integer.parseInt(screenNumber) - 1;
                    ApplicationLauncher.logger.debug(
                            "UnloadingGetScreenNumberTask : launchPythonApp : screenNumberIndex: " + screenNumberIndex);
                    unloadingPythonScript = ConstantConveyorConfig.CONVEYOR_UNLOADING_PYTHON_SCRIPT_SCREEN_NUMBER;
                    ApplicationLauncher.logger
                            .debug("UnloadingGetScreenNumberTask : launchPythonApp : unloadingPythonScript:"
                                    + unloadingPythonScript);

                    launchPythonAppV3_1(unloadingCmdLocation, unloadingPythonScript, unloadingPythonExecutablePath,
                            screenNumberIndex, unloadingBayNameDisplay, unloadingPort, btn_unloadingLaunch);

                }
            }

        }
    }

    class RejectionGetScreenNumberTask extends TimerTask {
        public void run() {

            ApplicationLauncher.logger.debug("RejectionGetScreenNumberTask : Entry");

            String screenNumber = getRejectionUserMonitorScreenNumber();
            ApplicationLauncher.logger.debug("RejectionGetScreenNumberTask : screenNumber: " + screenNumber);
            if (!screenNumber.isEmpty()) {
                ApplicationLauncher.logger.debug("RejectionGetScreenNumberTask : screenNumber: " + screenNumber);
                if (Integer.parseInt(screenNumber) == -1) {

                    ApplicationLauncher.logger.debug("RejectionGetScreenNumberTask : launchPythonAppV3 : Hit1");
                    launchPythonAppV3(rejectionCmdLocation, rejectionPythonScript, rejectionPythonExecutablePath,
                            rejectionMonitorManufacturerName,
                            rejectionMonitorPidNo, rejectionMonitorSerialNo, rejectionBayNameDisplay, rejectionPort,
                            btn_rejectionLaunch);

                } else {
                    int screenNumberIndex = Integer.parseInt(screenNumber) - 1;
                    ApplicationLauncher.logger
                            .debug("RejectionGetScreenNumberTask : launchPythonApp : screenNumberIndex : "
                                    + screenNumberIndex);
                    rejectionPythonScript = ConstantConveyorConfig.CONVEYOR_REJECTION_PYTHON_SCRIPT_SCREEN_NUMBER;
                    ApplicationLauncher.logger
                            .debug("RejectionGetScreenNumberTask : launchPythonApp : rejectionPythonScript:"
                                    + rejectionPythonScript);
                    // launchPythonApp(rejectionCmdLocation, rejectionPythonScript,
                    // rejectionPythonExecutablePath, (rejectionScreenNumber), btn_rejectionLaunch);
                    launchPythonAppV3_1(rejectionCmdLocation, rejectionPythonScript, rejectionPythonExecutablePath,
                            screenNumberIndex, rejectionBayNameDisplay, rejectionPort, btn_rejectionLaunch);

                }
            }

        }
    }

    private String getUnloadingUserMonitorScreenNumber() {

        String userEnteredScreenNumber = "";
        ConveyorDataManager.setUnloadingBayScreenNameUserInputReceived(false);
        ConveyorDataManager.setUnloadingBayUserEntryScreenName(-1);
        ApplicationLauncher.logger
                .debug("getUnloadingUserMonitorScreenNumber : setUnloadingBayScreenNameUserInputReceived: false");
        Platform.runLater(() -> {
            String header = "Unloading Bay : Kindly enter the screen number (1 or 2 or 3): ";
            String title = "Unloading Bay";

            InputDialogFX dialog = new InputDialogFX(title, header, InputDialogFX.MessageType.WARNING);
            dialog.resultProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null && !newVal.isEmpty()) {
                    ApplicationLauncher.logger.debug("UnloadingBay: User entered screen number: " + newVal);
                    if (GUIUtils.isNumber(newVal)) {
                        ConveyorDataManager.setUnloadingBayUserEntryScreenName(Integer.parseInt(newVal));
                    } else {
                        ApplicationLauncher.logger.debug("UnloadingBay: User entered invalid number: " + newVal);
                    }

                } else {
                    ApplicationLauncher.logger.debug("UnloadingBay: User cancelled or entered empty value");
                }
                ConveyorDataManager.setUnloadingBayScreenNameUserInputReceived(true);
            });
            dialog.show(); // This will NOT block the JavaFX thread
            ApplicationLauncher.logger
                    .debug("getUnloadingUserMonitorScreenNumber: Prompt shown, returning immediately");

        });

        ApplicationLauncher.logger.debug(
                "getUnloadingUserMonitorScreenNumber : awaiting for isUnloadingBayScreenNameUserInputReceived: Entry");
        while (!ConveyorDataManager.isUnloadingBayScreenNameUserInputReceived()
                && !Unloading.isStopProcessRequestedUnloadingBay()) {
            ApplicationLauncher.logger.debug(
                    "getUnloadingUserMonitorScreenNumber: Waiting for isUnloadingBayScreenNameUserInputReceived");
            BayUtils.delay(1000);
        }
        ApplicationLauncher.logger.debug(
                "getUnloadingUserMonitorScreenNumber : awaiting for isUnloadingBayScreenNameUserInputReceived: Exit");
        if (ConveyorDataManager.isUnloadingBayScreenNameUserInputReceived()) {
            // if(ConveyorDeviceDataManagerController.getUnloadingBayUserEntryScreenName()!=-1)
            // {
            userEnteredScreenNumber = String.valueOf(ConveyorDataManager.getUnloadingBayUserEntryScreenName());
            ApplicationLauncher.logger
                    .debug("getUnloadingUserMonitorScreenNumber : userEnteredScreenNumber: " + userEnteredScreenNumber);

            // }

        }
        return userEnteredScreenNumber;
    }

    private String getRejectionUserMonitorScreenNumber() {

        String userEnteredScreenNumber = "";
        ConveyorDataManager.setRejectionBayScreenNameUserInputReceived(false);
        ConveyorDataManager.setRejectionBayUserEntryScreenName(-1);
        ApplicationLauncher.logger
                .debug("getRejectionUserMonitorScreenNumber : setRejectionBayScreenNameUserInputReceived: false");
        Platform.runLater(() -> {
            String header = "Rejection Bay : Kindly enter the screen number (1 or 2 or 3): ";
            String title = "Rejection Bay";

            InputDialogFX dialog = new InputDialogFX(title, header, InputDialogFX.MessageType.WARNING);
            dialog.resultProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null && !newVal.isEmpty()) {
                    ApplicationLauncher.logger.debug("RejectionBay: User entered screen number: " + newVal);
                    if (GUIUtils.isNumber(newVal)) {
                        ConveyorDataManager.setRejectionBayUserEntryScreenName(Integer.parseInt(newVal));
                    } else {
                        ApplicationLauncher.logger.debug("RejectionBay: User entered invalid number: " + newVal);
                    }

                } else {
                    ApplicationLauncher.logger.debug("RejectionBay: User cancelled or entered empty value");
                }
                ConveyorDataManager.setRejectionBayScreenNameUserInputReceived(true);
            });
            dialog.show(); // This will NOT block the JavaFX thread
            ApplicationLauncher.logger
                    .debug("getRejectionUserMonitorScreenNumber: Prompt shown, returning immediately");

        });

        ApplicationLauncher.logger.debug(
                "getRejectionUserMonitorScreenNumber : awaiting for isRejectionBayScreenNameUserInputReceived: Entry");
        while (!ConveyorDataManager.isRejectionBayScreenNameUserInputReceived()
                && !Rejection.isStopProcessRequestedRejectionBay()) {
            ApplicationLauncher.logger.debug(
                    "getRejectionUserMonitorScreenNumber: Waiting for isRejectionBayScreenNameUserInputReceived");
            BayUtils.delay(1000);
        }
        ApplicationLauncher.logger.debug(
                "getRejectionUserMonitorScreenNumber : awaiting for isRejectionBayScreenNameUserInputReceived: Exit");
        if (ConveyorDataManager.isRejectionBayScreenNameUserInputReceived()) {
            // if(ConveyorDeviceDataManagerController.getRejectionBayUserEntryScreenName()!=-1)
            // {
            userEnteredScreenNumber = String.valueOf(ConveyorDataManager.getRejectionBayUserEntryScreenName());
            ApplicationLauncher.logger
                    .debug("getRejectionUserMonitorScreenNumber : userEnteredScreenNumber: " + userEnteredScreenNumber);

            // }

        }
        return userEnteredScreenNumber;
    }

    /**
     * Launches a JAR file from a specified command prompt location in a new CMD
     * window
     * and updates the button's state.
     *
     * @param cmdLocation The directory where the JAR is located and the new command
     *                    prompt should start.
     * @param jarFile     The name of the JAR file to launch.
     * @param button      The button associated with the launch action.
     */
    private void launchJar(String cmdLocation, String jarFile, Button button) {
        // Basic validation for cmdLocation and jarFile
        if (cmdLocation == null || cmdLocation.trim().isEmpty() || jarFile == null || jarFile.trim().isEmpty()) {
            ApplicationLauncher.logger.error("Cannot launch JAR: cmdLocation or jarFile is null/empty.");
            Platform.runLater(() -> updateButtonOnError(button));
            return;
        }

        button.setDisable(true);
        button.setText("LAUNCHING...");

        new Thread(() -> {
            try {
                File workingDir = new File(cmdLocation);
                if (!workingDir.exists() || !workingDir.isDirectory()) {
                    throw new IOException(
                            "The specified directory does not exist or is not a directory: " + cmdLocation);
                }

                // Get the absolute path to the java.exe that is currently running THIS
                // application
                String javaExecutablePath = System.getProperty("java.home") + File.separator + "bin" + File.separator
                        + "java.exe";

                // Ensure the constructed path actually points to an executable
                File javaExe = new File(javaExecutablePath);
                if (!javaExe.exists() || !javaExe.isFile() || !javaExe.canExecute()) {
                    throw new IOException("Cannot find or execute java.exe at: " + javaExecutablePath);
                }

                ProcessBuilder pb = new ProcessBuilder(
                        "cmd.exe",
                        "/c",
                        "start",
                        "\"\"", // Empty window title (required by 'start' for complex commands)
                        "cmd.exe",
                        "/k", // Keep the new command prompt window open
                        javaExecutablePath, // Use the absolute path to java.exe
                        "-jar",
                        jarFile);

                pb.directory(workingDir); // Set the working directory for the new process
                pb.inheritIO(); // Inherit I/O for debugging (optional)

                ApplicationLauncher.logger.info("Launching JAR: " + Arrays.toString(pb.command().toArray()));
                ApplicationLauncher.logger.info("Working Directory for JAR: " + pb.directory().getAbsolutePath());

                pb.start();

                Platform.runLater(() -> {
                    updateButtonOnSuccess(button);
                });

            } catch (IOException e) {
                ApplicationLauncher.logger.error("Error launching JAR file: " + jarFile + " from " + cmdLocation);
                e.printStackTrace();
                Platform.runLater(() -> {
                    updateButtonOnError(button);
                });
            }
        }).start();
    }

    private void launchPythonAppV3_1(String cmdLocation, String pythonScript, String pythonExecutablePath,
            int screenNumber, String bayNameDisplay, String port, Button button) {
        // Basic validation for cmdLocation and pythonScript
        if (cmdLocation == null || cmdLocation.trim().isEmpty() || pythonScript == null
                || pythonScript.trim().isEmpty()) {
            ApplicationLauncher.logger
                    .error("launchPythonAppV3_1: Cannot launch Python app: cmdLocation or pythonScript is null/empty.");
            Platform.runLater(() -> updateButtonOnError(button));
            return;
        }

        button.setDisable(true);
        button.setText("LAUNCHING...");

        new Thread(() -> {
            try {
                // Extract drive letter from cmdLocation (e.g., "D")
                String driveLetter = cmdLocation.substring(0, 1);

                // Construct the full command string for cmd.exe
                // This string will be executed by the inner cmd.exe
                String fullCommand = driveLetter + ": && cd \"" + cmdLocation + "\" && \"" +
                        (pythonExecutablePath != null && !pythonExecutablePath.trim().isEmpty() ? pythonExecutablePath
                                : "python.exe")
                        +
                        "\" \"" + pythonScript +
                        "\" --port " + port +
                        " --bay-name \"" + bayNameDisplay +
                // "\" " + String.valueOf(screenNumber);
                        "\" --screen-name \"" + screenNumber + "\"";

                ProcessBuilder pb = new ProcessBuilder(
                        "cmd.exe",
                        "/c", // Use /c to close the *outer* cmd window after 'start' finishes
                        "start",
                        "\"\"", // Empty window title for the new window opened by 'start'
                        "cmd.exe",
                        "/k", // Keep the *inner* cmd window open after the Python script runs
                        fullCommand // The entire sequence of commands to run in the inner cmd window
                );

                // IMPORTANT: Remove pb.directory() as the 'cd' command in fullCommand handles
                // directory change
                // pb.directory(workingDir); // REMOVED

                pb.inheritIO(); // Inherit I/O for debugging (optional)

                // --- Debugging output ---
                ApplicationLauncher.logger.info("launchPythonAppV3_1: Launching Python app command array: "
                        + Arrays.toString(pb.command().toArray()));
                ApplicationLauncher.logger
                        .info("launchPythonAppV3_1: Full command string passed to inner cmd.exe: " + fullCommand);
                // --- End Debugging output ---

                pb.start();

                Platform.runLater(() -> {
                    updateButtonOnSuccess(button);
                });

            } catch (IOException e) {
                ApplicationLauncher.logger.error(
                        "launchPythonAppV3_1: Error launching Python app: " + pythonScript + " from " + cmdLocation);
                e.printStackTrace();
                Platform.runLater(() -> {
                    updateButtonOnError(button);
                });
            }
        }).start();
    }

    private void launchPythonAppV3(String cmdLocation, String pythonScript, String pythonExecutablePath,
            String monitorManufacturerName,
            String monitorPidNo, String monitorSerialNo, String bayNameDisplay, String port, Button button) {
        // Basic validation for cmdLocation and pythonScript
        ApplicationLauncher.logger.info("launchPythonAppV3: Entry");
        ApplicationLauncher.logger.info("launchPythonAppV3: cmdLocation: " + cmdLocation);
        ApplicationLauncher.logger.info("launchPythonAppV3: pythonScript: " + pythonScript);
        ApplicationLauncher.logger.info("launchPythonAppV3: pythonExecutablePath: " + pythonExecutablePath);
        ApplicationLauncher.logger.info("launchPythonAppV3: monitorManufacturerName: " + monitorManufacturerName);
        ApplicationLauncher.logger.info("launchPythonAppV3: monitorPidNo: " + monitorPidNo);
        ApplicationLauncher.logger.info("launchPythonAppV3: bayNameDisplay: " + bayNameDisplay);
        ApplicationLauncher.logger.info("launchPythonAppV3: port: " + port);
        // ApplicationLauncher.logger.info("launchPythonAppV3: monitorManufacturerName:
        // " + monitorManufacturerName);
        if (cmdLocation == null || cmdLocation.trim().isEmpty() || pythonScript == null
                || pythonScript.trim().isEmpty()) {
            ApplicationLauncher.logger
                    .error("launchPythonAppV3: Cannot launch Python app: cmdLocation or pythonScript is null/empty.");
            Platform.runLater(() -> updateButtonOnError(button));
            return;
        }

        button.setDisable(true);
        button.setText("LAUNCHING...");

        new Thread(() -> {
            try {
                // Extract drive letter from cmdLocation (e.g., "D")
                String driveLetter = cmdLocation.substring(0, 1);

                String fullCommand = driveLetter + ": && cd \"" + cmdLocation + "\" && \"" +
                        (pythonExecutablePath != null && !pythonExecutablePath.trim().isEmpty() ? pythonExecutablePath
                                : "python.exe")
                        +
                        "\" \"" + pythonScript + "\" --port " + port + " --bay-name \"" + bayNameDisplay
                        + "\" --manufacturer-name \"" + monitorManufacturerName// + "\""
                // + "\" --manufacturer-name \"" + monitorManufacturerName + "\""
                        + "\" --monitor-pid-no \"" + monitorPidNo // + "\""
                        + "\" --monitor-serial-no \"" + monitorSerialNo + "\"";

                ApplicationLauncher.logger.info("launchPythonAppV3: fullCommand: <" + fullCommand + ">");

                ProcessBuilder pb = new ProcessBuilder(
                        "cmd.exe",
                        "/c", // Use /c to close the *outer* cmd window after 'start' finishes
                        "start",
                        "\"\"", // Empty window title for the new window opened by 'start'
                        "cmd.exe",
                        "/k", // Keep the *inner* cmd window open after the Python script runs
                        fullCommand // The entire sequence of commands to run in the inner cmd window
                );

                pb.inheritIO(); // Inherit I/O for debugging (optional)

                // --- Debugging output ---
                ApplicationLauncher.logger.info("launchPythonAppV3: Launching Python app command array: "
                        + Arrays.toString(pb.command().toArray()));
                ApplicationLauncher.logger
                        .info("launchPythonAppV3: Full command string passed to inner cmd.exe: " + fullCommand);
                // --- End Debugging output ---

                pb.start();

                Platform.runLater(() -> {
                    updateButtonOnSuccess(button);
                });

            } catch (IOException e) {
                ApplicationLauncher.logger.error(
                        "launchPythonAppV3: Error launching Python app: " + pythonScript + " from " + cmdLocation);
                e.printStackTrace();
                Platform.runLater(() -> {
                    updateButtonOnError(button);
                });
            }
        }).start();
    }

    /**
     * Updates the button's style and text to indicate successful launch.
     * 
     * @param button The button to update.
     */
    private void updateButtonOnSuccess(Button button) {
        button.setStyle("-fx-background-color: #4CAF50;"); // Green color
        button.setText("RUNNING");
        button.setDisable(false); // Re-enable the button after launch
    }

    /**
     * Updates the button's style and text to indicate a launch failure.
     * 
     * @param button The button to update.
     */
    private void updateButtonOnError(Button button) {
        button.setStyle("-fx-background-color: #F44336;"); // Red color for error
        button.setText("LAUNCH FAILED");
        button.setDisable(false); // Re-enable the button so the user can try again
    }
}
