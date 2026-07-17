package com.tasnetwork.calibration.conveyor.dashboard;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import javafx.application.Platform; // Added this import

import com.tasnetwork.calibration.conveyor.bay.calib.Calib;
import com.tasnetwork.calibration.conveyor.bay.calib.CalibrationBayReset;
import com.tasnetwork.calibration.conveyor.bay.calib.CalibrationBayStop;
import com.tasnetwork.calibration.conveyor.bay.comm.Comm;
import com.tasnetwork.calibration.conveyor.bay.comm.CommunicationTestBayReset;
import com.tasnetwork.calibration.conveyor.bay.comm.CommunicationTestBayStop;
import com.tasnetwork.calibration.conveyor.bay.ft.Ft;
import com.tasnetwork.calibration.conveyor.bay.ft.FunctionalTestBayReset;
import com.tasnetwork.calibration.conveyor.bay.ft.FunctionalTestBayStop;
import com.tasnetwork.calibration.conveyor.bay.hv.Hv;
import com.tasnetwork.calibration.conveyor.bay.hv.HighVoltageTestBayReset;
import com.tasnetwork.calibration.conveyor.bay.hv.HighVoltageTestBayStop;
import com.tasnetwork.calibration.conveyor.bay.ir.Ir;
import com.tasnetwork.calibration.conveyor.bay.ir.InsulationResistanceTestBayReset;
import com.tasnetwork.calibration.conveyor.bay.ir.InsulationResistanceTestBayStop;
import com.tasnetwork.calibration.conveyor.bay.rejection.Rejection;
import com.tasnetwork.calibration.conveyor.bay.rejection.RejectionBayReset;
import com.tasnetwork.calibration.conveyor.bay.rejection.RejectionBayStop;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.StaNld_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.STA_NoLoadTestBay1Reset;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.STA_NoLoadTestBay1Stop;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.StaNld_Bay2;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.STA_NoLoadTestBay2Reset;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.STA_NoLoadTestBay2Stop;
import com.tasnetwork.calibration.conveyor.bay.unloading.Unloading;
import com.tasnetwork.calibration.conveyor.bay.unloading.UnloadingBayReset;
import com.tasnetwork.calibration.conveyor.bay.unloading.UnloadingBayStop;
import com.tasnetwork.calibration.conveyor.bay.verific.Verification;
import com.tasnetwork.calibration.conveyor.bay.verific.VerificationTestBayReset;
import com.tasnetwork.calibration.conveyor.bay.verific.VerificationTestBayStop;
import com.tasnetwork.calibration.conveyor.bay.verific_waiting.VerificWaiting;
import com.tasnetwork.calibration.conveyor.bay.verific_waiting.WaitingBayReset;
import com.tasnetwork.calibration.conveyor.bay.verific_waiting.WaitingBayStop;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;

public class MainControlPaneController implements Initializable {

@FXML
private Button btnCalibReset;
@FXML
private Button btnCalibStart;
@FXML
private Button btnCalibStop;
@FXML
private Button btnCommTestReset;
@FXML
private Button btnCommTestStart;
@FXML
private Button btnCommTestStop;
@FXML
private Button btnFtReset;
@FXML
private Button btnFtStart;
@FXML
private Button btnFtStop;
@FXML
private Button btnHvtReset;
@FXML
private Button btnHvtStart;
@FXML
private Button btnHvtStop;
@FXML
private Button btnIrtReset;
@FXML
private Button btnIrtStart;
@FXML
private Button btnIrtStop;
@FXML
private Button btnSctNlt1Reset;
@FXML
private Button btnSctNlt1Start;
@FXML
private Button btnSctNlt1Stop;
@FXML
private Button btnSctNlt2Reset;
@FXML
private Button btnSctNlt2Start;
@FXML
private Button btnSctNlt2Stop;
@FXML
private Button btnVerificTestReset;
@FXML
private Button btnVerificTestStart;
@FXML
private Button btnVerificTestStop;

public static Button ref_btnVerificTestReset;
public static Button ref_btnVerificTestStart;
public static Button ref_btnVerificTestStop;
@FXML
private Button btnWaitingBayReset;
@FXML
private Button btnWaitingBayStart;
@FXML
private Button btnWaitingBayStop;
@FXML private Button btnRejectStart;
@FXML private Button btnRejectStop;
@FXML private Button btnRejectReset;
@FXML private Button btnUnloadingStart;
@FXML private Button btnUnloadingStop;
@FXML private Button btnUnloadingReset;

//============================================================================================================================================

Timer funtionalBayStartTaskTimer;
Timer calibrationStartTaskTimer;
Timer insResStartTaskTimer;
Timer hvtBayStartTaskTimer;
Timer verificStartTaskTimer;
Timer commStartTaskTimer;
Timer sctNlt1StartTaskTimer;
Timer sctNlt2StartTaskTimer;
Timer waitingBayStartTaskTimer;
Timer rejectionBayStartTaskTimer;
Timer unloadingBayStartTaskTimer;
Timer funtionalBayStopTaskTimer;
Timer calibrationStopTaskTimer;
Timer insResStopTaskTimer;
Timer hvtBayStopTaskTimer;
Timer verificStopTaskTimer ;
Timer commStopTaskTimer ;
Timer sctNlt2StopTaskTimer ;
Timer sctNlt1StopTaskTimer ;
Timer waitingBayStopTaskTimer;
Timer rejectionBayStopTaskTimer;
Timer unloadingBayStopTaskTimer;
Timer funtionalBayResetTaskTimer;
Timer calibrationResetTaskTimer;
Timer insResResetTaskTimer;
Timer hvtBayResetTaskTimer;
Timer verificResetTaskTimer ;
Timer commResetTaskTimer ;
Timer sctNlt1ResetTaskTimer ;
Timer sctNlt2ResetTaskTimer;
Timer waitingBayResetTaskTimer;
Timer rejectionBayResetTaskTimer;
Timer unloadingBayResetTaskTimer;

//============================================================================================================================================

@FXML
public void btnRjStartOnClick() {
    Rejection.logger.info("btnRjStartOnClick : Invoked:");
    
    Rejection.logger.info("REJECTION BAY : STARTED");
    ApplicationLauncher.logger.info("REJECTION BAY : STARTED");
    
    ConstantConveyor.ALL_LOOP_BREAK_FLAG = false;

    // F L A G S
    Rejection.setStartProcessRequestedRejectionBay(true);
    Rejection.setStopProcessCompletedRejectionBay(false);
    Rejection.setStopProcessRequestedRejectionBay(false);
    Rejection.setResetProcessCompletedRejectionBay(false);
    Rejection.setResetProcessRequestedRejectionBay(false);

    // B U T T O N I N T E R L O C K
    Platform.runLater(() -> {
        btnRejectStart.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
        btnRejectStart.setDisable(true);
        btnRejectStop.setDisable(true);
        btnRejectReset.setDisable(true);
        ApplicationLauncher.setCursor(Cursor.WAIT);
    });

    // L O G I C
    rejectionBayStartTaskTimer = new Timer();
    rejectionBayStartTaskTimer.schedule(new Rejection(), 100);
    // Sleep(500); // Removed
    // rejectionBayStartTaskTimer.cancel(); // Removed

    // Background thread to monitor process completion
    Thread waitForStartCompletion = new Thread(() -> {
        try {
            while (!Rejection.isStartProcessCompletedRejectionBay()) {
                Thread.sleep(200); // polling delay
            }

            // Once start is completed, update UI
            Platform.runLater(() -> {
                DashboardController.highlightRedBay(DashboardController.getRef_rejectionBayContainer()); // Not applicable for this bay
                btnRejectStop.setDisable(false);
                btnRejectStop.setStyle(""); // Enabled - Default
                btnRejectReset.setDisable(false);
                btnRejectReset.setStyle(""); // Enabled - Default
                ApplicationLauncher.setCursor(Cursor.DEFAULT);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Rejection.logger.warn("Start-process wait thread interrupted", e);
            e.printStackTrace(); // Added for consistency
        }
    });

    waitForStartCompletion.setDaemon(true);
    waitForStartCompletion.start();

    Rejection.logger.info("btnRjStartOnClick : Exit:");
}

@FXML
public void btnRjStopOnClick() {
    Rejection.logger.info("btnRjStopOnClick : Invoked:");
    
    Rejection.logger.info("REJECTION BAY : STOPPED");
    ApplicationLauncher.logger.info("REJECTION BAY : STOPPED");
    
    // F L A G S
    Rejection.abort_Rejection_Bay = true;
    Rejection.setStartProcessRequestedRejectionBay(false);
    Rejection.setStopProcessCompletedRejectionBay(false);
    Rejection.setStopProcessRequestedRejectionBay(true);
    Rejection.setResetProcessCompletedRejectionBay(false);
    Rejection.setResetProcessRequestedRejectionBay(false);

    // B U T T O N I N T E R L O C K
    Platform.runLater(() -> {
        btnRejectStop.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
        btnRejectStop.setDisable(true);
        btnRejectStart.setDisable(true); // Corrected: Should disable start button
        btnRejectReset.setDisable(true);
        ApplicationLauncher.setCursor(Cursor.WAIT);
    });

    // L O G I C
    rejectionBayStopTaskTimer = new Timer();
    rejectionBayStopTaskTimer.schedule(new RejectionBayStop(), 100);
    // Sleep(500); // Removed
    // rejectionBayStopTaskTimer.cancel(); // Removed

    // Wait for stop process to complete
    Thread waitForStopCompletion = new Thread(() -> {
        try {
            while (!Rejection.isStopProcessCompletedRejectionBay()) {
                Thread.sleep(200);
            }

            Platform.runLater(() -> {
                DashboardController.resetBayHighlight(DashboardController.getRef_rejectionBayContainer()); // Not applicable for this bay
                btnRejectStart.setDisable(false);
                btnRejectStart.setStyle(""); // Enabled - Default
                btnRejectReset.setDisable(false);
                btnRejectReset.setStyle(""); // Enabled - Default
                ApplicationLauncher.setCursor(Cursor.DEFAULT);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Rejection.logger.warn("Stop-process wait thread interrupted", e);
            e.printStackTrace(); // Added for consistency
        }
    });

    waitForStopCompletion.setDaemon(true);
    waitForStopCompletion.start();

    Rejection.logger.info("btnRjStopOnClick : Exit:");
}

@FXML
public void btnRjResetOnClick() {
    Rejection.logger.info("btnRjResetOnClick : Invoked:");
    
    Rejection.logger.info("REJECTION BAY : RESET");
    ApplicationLauncher.logger.info("REJECTION BAY : RESET");
    
    // F L A G S
    Rejection.setStartProcessRequestedRejectionBay(false);
    Rejection.setStopProcessCompletedRejectionBay(false);
    Rejection.setStopProcessRequestedRejectionBay(false);
    Rejection.setResetProcessCompletedRejectionBay(false);
    Rejection.setResetProcessRequestedRejectionBay(true);

    // B U T T O N I N T E R L O C K
    Platform.runLater(() -> {
        btnRejectReset.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
        btnRejectReset.setDisable(true);
        btnRejectStart.setDisable(true);
        btnRejectStop.setDisable(true);
        ApplicationLauncher.setCursor(Cursor.WAIT);
    });

    // L O G I C
    rejectionBayResetTaskTimer = new Timer();
    rejectionBayResetTaskTimer.schedule(new RejectionBayReset(), 100);
    // Sleep(500); // Removed
    // rejectionBayResetTaskTimer.cancel(); // Removed

    // Wait for reset process to complete
    Thread waitForResetCompletion = new Thread(() -> {
        try {
            while (!Rejection.isResetProcessCompletedRejectionBay()) {
                Thread.sleep(200);
            }

            Platform.runLater(() -> {
                btnRejectStart.setDisable(false);
                btnRejectStart.setStyle(""); // Enabled - Default
                btnRejectStop.setDisable(false);
                btnRejectStop.setStyle(""); // Enabled - Default
                ApplicationLauncher.setCursor(Cursor.DEFAULT);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Rejection.logger.warn("Reset-process wait thread interrupted", e);
            e.printStackTrace(); // Added for consistency
        }
    });

    waitForResetCompletion.setDaemon(true);
    waitForResetCompletion.start();

    Rejection.logger.info("btnRjResetOnClick : Exit:");
}

//============================================================================================================================================

@FXML
public void btnFtStartOnClick() {
    Ft.logger.info("btnFtStartOnClick : Invoked:");
    Ft.logger.info("FUNCTIONAL TEST BAY : STARTED");
    ApplicationLauncher.logger.info("FUNCTIONAL TEST BAY : STARTED");

    ConstantConveyor.ALL_LOOP_BREAK_FLAG = false;

    // F L A G S
    Ft.setStartProcessRequestedFtBay(true);
    Ft.setStopProcessCompletedFtBay(false);
    Ft.setStopProcessRequestedFtBay(false);
    Ft.setResetProcessCompletedFtBay(false);
    Ft.setResetProcessRequestedFtBay(false);

    // B U T T O N   I N T E R L O C K
    Platform.runLater(() -> {
        btnFtStart.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
        btnFtStart.setDisable(true);
        btnFtStop.setDisable(true);
        btnFtReset.setDisable(true);
        ApplicationLauncher.setCursor(Cursor.WAIT);
    });

    // Functional Test logic (optional)
    funtionalBayStartTaskTimer = new Timer();
    funtionalBayStartTaskTimer.schedule(new Ft(), 100);
    // Sleep(500); // Removed
    // funtionalBayStartTaskTimer.cancel(); // Removed

    // Background thread to monitor process completion
    Thread waitForStartCompletion = new Thread(() -> {
        try {
            while (!Ft.isStartProcessCompletedFtBay()) {
                Thread.sleep(200); // polling delay
            }

            // Once start is completed, update UI
            Platform.runLater(() -> {
                DashboardController.highlightGreenBay(DashboardController.getRef_bay1Container());
                btnFtStop.setDisable(false);
                btnFtStop.setStyle(""); // Enabled - Default
                btnFtReset.setDisable(false);
                btnFtReset.setStyle(""); // Enabled - Default
                ApplicationLauncher.setCursor(Cursor.DEFAULT);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Ft.logger.warn("Start-process wait thread interrupted", e);
            e.printStackTrace(); // Added for consistency
        }
    });

    waitForStartCompletion.setDaemon(true);
    waitForStartCompletion.start();

    Ft.logger.info("btnFtStartOnClick : Exit:");
}


@FXML
public void btnFtStopOnClick() {
    Ft.logger.info("btnFtStopOnClick : Invoked:");
    
    Ft.logger.info("FUNCTIONAL TEST BAY : STOPPED");
    ApplicationLauncher.logger.info("FUNCTIONAL TEST BAY : STOPPED");
    
    // F L A G S
    Ft.abort_FT_Bay = true;
    Ft.setStartProcessRequestedFtBay(false);
    Ft.setStopProcessCompletedFtBay(false);
    Ft.setStopProcessRequestedFtBay(true);
    Ft.setResetProcessCompletedFtBay(false);
    Ft.setResetProcessRequestedFtBay(false);

    // B U T T O N   I N T E R L O C K
    Platform.runLater(() -> {
        btnFtStop.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
        btnFtStop.setDisable(true);
        btnFtStart.setDisable(true);
        btnFtReset.setDisable(true);
        ApplicationLauncher.setCursor(Cursor.WAIT);
    });

    // L O G I C
    funtionalBayStopTaskTimer = new Timer();
    funtionalBayStopTaskTimer.schedule(new FunctionalTestBayStop(), 100);
    // Sleep(500); // Removed
    // funtionalBayStopTaskTimer.cancel(); // Removed

    // Wait for stop process to complete, then reset highlight
    Thread waitForStopCompletion = new Thread(() -> {
        try {
            while (!Ft.isStopProcessCompletedFtBay()) {
                Thread.sleep(200);
            }

            Platform.runLater(() -> {
                DashboardController.resetBayHighlight(DashboardController.getRef_bay1Container());
                ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopTimeUpDisplay(ConstantConveyor.FT_BAY_KEY);
                ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopProgressBarWithTime(ConstantConveyor.FT_BAY_KEY);
                btnFtStart.setDisable(false);
                btnFtStart.setStyle(""); // Enabled - Default
                btnFtReset.setDisable(false);
                btnFtReset.setStyle(""); // Enabled - Default
                ApplicationLauncher.setCursor(Cursor.DEFAULT);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Ft.logger.warn("Stop-process wait thread interrupted", e);
            e.printStackTrace(); // Added for consistency
        }
    });

    waitForStopCompletion.setDaemon(true);
    waitForStopCompletion.start();

    Ft.logger.info("btnFtStopOnClick : Exit:");
}

@FXML
public void btnFtResetOnClick() {
    Ft.logger.info("btnFtResetOnClick : Invoked:");

    Ft.logger.info("FUNCTIONAL TEST BAY : RESET");
    ApplicationLauncher.logger.info("FUNCTIONAL TEST BAY : RESET");

    // F L A G S
    Ft.setStartProcessRequestedFtBay(false);
    Ft.setStopProcessCompletedFtBay(false);
    Ft.setStopProcessRequestedFtBay(false);
    Ft.setResetProcessCompletedFtBay(false);
    Ft.setResetProcessRequestedFtBay(true);
    ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopTimeUpDisplay(ConstantConveyor.FT_BAY_KEY);
    ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopProgressBarWithTime(ConstantConveyor.FT_BAY_KEY);
    
    // B U T T O N   I N T E R L O C K
    Platform.runLater(() -> {
        btnFtReset.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
        btnFtReset.setDisable(true);
        btnFtStart.setDisable(true);
        btnFtStop.setDisable(true);
        ApplicationLauncher.setCursor(Cursor.WAIT);
    });

    // L O G I C
    funtionalBayResetTaskTimer = new Timer();
    funtionalBayResetTaskTimer.schedule(new FunctionalTestBayReset(), 100);
    // Sleep(500); // Removed
    // funtionalBayResetTaskTimer.cancel(); // Removed

    // Wait for reset process to complete, then re-enable buttons
    Thread waitForResetCompletion = new Thread(() -> {
        try {
            while (!Ft.isResetProcessCompletedFtBay()) {
                Thread.sleep(200);
            }

            Platform.runLater(() -> {
                btnFtStart.setDisable(false);
                 btnFtStart.setStyle(""); // Enabled - Default
                btnFtStop.setDisable(false);
                 btnFtStop.setStyle(""); // Enabled - Default
                ApplicationLauncher.setCursor(Cursor.DEFAULT);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Ft.logger.warn("Reset-process wait thread interrupted", e);
            e.printStackTrace(); // Added for consistency
        }
    });

    waitForResetCompletion.setDaemon(true);
    waitForResetCompletion.start();

    Ft.logger.info("btnFtResetOnClick : Exit:");
}


//============================================================================================================================================

@FXML
public void btnHvtStartOnClick() {
    Hv.logger.info("btnHvtStartOnClick : Invoked:");
    
    Hv.logger.info("HIGH VOLTAGE TEST BAY : STARTED");
    ApplicationLauncher.logger.info("HIGH VOLTAGE TEST BAY : STARTED");
    
    ConstantConveyor.ALL_LOOP_BREAK_FLAG = false;

    // F L A G S
    Hv.setStartProcessRequestedHvtBay(true);
    Hv.setStopProcessCompletedHvtBay(false);
    Hv.setStopProcessRequestedHvtBay(false);
    Hv.setResetProcessCompletedHvtBay(false);
    Hv.setResetProcessRequestedHvtBay(false);

    // B U T T O N I N T E R L O C K 
    Platform.runLater(() -> {
        btnHvtStart.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
        btnHvtStart.setDisable(true);
        btnHvtStop.setDisable(true);
        btnHvtReset.setDisable(true);
        ApplicationLauncher.setCursor(Cursor.WAIT);
    });

    // L O G I C
    hvtBayStartTaskTimer = new Timer();
    hvtBayStartTaskTimer.schedule(new Hv(), 100);
    // Sleep(500); // Removed
    // hvtBayStartTaskTimer.cancel(); // Removed

    // Background thread to monitor process completion
    Thread waitForStartCompletion = new Thread(() -> {
        try {
            while (!Hv.isStartProcessCompletedHvtBay()) {
                Thread.sleep(200); // polling delay
            }

            // Once start is completed, update UI
            Platform.runLater(() -> {
                DashboardController.highlightGreenBay(DashboardController.getRef_bay2Container());
                btnHvtStop.setDisable(false);
                btnHvtStop.setStyle(""); // Enabled - Default
                btnHvtReset.setDisable(false);
                btnHvtReset.setStyle(""); // Enabled - Default
                ApplicationLauncher.setCursor(Cursor.DEFAULT);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Hv.logger.warn("Start-process wait thread interrupted", e);
            e.printStackTrace(); // Added for consistency
        }
    });

    waitForStartCompletion.setDaemon(true);
    waitForStartCompletion.start();

    Hv.logger.info("btnHvtStartOnClick : Exit:");
}

@FXML
public void btnHvtStopOnClick() {
    Hv.logger.info("btnHvtStopOnClick : Invoked:");
    
    Hv.logger.info("HIGH VOLTAGE TEST BAY : STOPPED");
    ApplicationLauncher.logger.info("HIGH VOLTAGE TEST BAY : STOPPED");
    
    // F L A G S
    Hv.abort_HVT_Bay = true;
    Hv.setStartProcessRequestedHvtBay(false);
    Hv.setStopProcessCompletedHvtBay(false);
    Hv.setStopProcessRequestedHvtBay(true);
    Hv.logger.info("StopProcessRequestedHvtBay :" + Hv.isStopProcessRequestedHvtBay());
    Hv.setResetProcessCompletedHvtBay(false);
    Hv.setResetProcessRequestedHvtBay(false);

    // B U T T O N I N T E R L O C K
    Platform.runLater(() -> {
        btnHvtStop.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
        btnHvtStop.setDisable(true);
        btnHvtStart.setDisable(true);
        btnHvtReset.setDisable(true);
        ApplicationLauncher.setCursor(Cursor.WAIT);
    });

    // L O G I C
    hvtBayStopTaskTimer = new Timer();
    hvtBayStopTaskTimer.schedule(new HighVoltageTestBayStop(), 100);
    // Sleep(500); // Removed
    // hvtBayStopTaskTimer.cancel(); // Removed

    // Wait for stop process to complete
    Thread waitForStopCompletion = new Thread(() -> {
        try {
            while (!Hv.isStopProcessCompletedHvtBay()) {
                Thread.sleep(200);
            }

            Platform.runLater(() -> {
                DashboardController.resetBayHighlight(DashboardController.getRef_bay2Container());
                ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopTimeUpDisplay(ConstantConveyor.HV_BAY_KEY);
                ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopProgressBarWithTime(ConstantConveyor.HV_BAY_KEY);
                
                btnHvtStart.setDisable(false);
                btnHvtStart.setStyle(""); // Enabled - Default
                btnHvtReset.setDisable(false);
                btnHvtReset.setStyle(""); // Enabled - Default
                ApplicationLauncher.setCursor(Cursor.DEFAULT);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Hv.logger.warn("Stop-process wait thread interrupted", e);
            e.printStackTrace(); // Added for consistency
        }
    });

    waitForStopCompletion.setDaemon(true);
    waitForStopCompletion.start();

    Hv.logger.info("btnHvtStopOnClick : Exit:");
}

@FXML
public void btnHvtResetOnClick() {
    Hv.logger.info("btnHvtResetOnClick : Invoked:");
    
    Hv.logger.info("HIGH VOLTAGE TEST BAY : RESET");
    ApplicationLauncher.logger.info("HIGH VOLTAGE TEST BAY : RESET");
    
    // F L A G S
    Hv.setStartProcessRequestedHvtBay(false);
    Hv.setStopProcessCompletedHvtBay(false);
    Hv.setStopProcessRequestedHvtBay(false);
    Hv.setResetProcessCompletedHvtBay(false);
    Hv.setResetProcessRequestedHvtBay(true);
    ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopTimeUpDisplay(ConstantConveyor.HV_BAY_KEY);
    ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopProgressBarWithTime(ConstantConveyor.HV_BAY_KEY);
    
    // B U T T O N I N T E R L O C K
    Platform.runLater(() -> {
        btnHvtReset.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
        btnHvtReset.setDisable(true);
        btnHvtStart.setDisable(true);
        btnHvtStop.setDisable(true);
        ApplicationLauncher.setCursor(Cursor.WAIT);
    });

    // L O G I C
    hvtBayResetTaskTimer = new Timer();
    hvtBayResetTaskTimer.schedule(new HighVoltageTestBayReset(), 100);
    // Sleep(500); // Removed
    // hvtBayResetTaskTimer.cancel(); // Removed

    // Wait for reset process to complete
    Thread waitForResetCompletion = new Thread(() -> {
        try {
            while (!Hv.isResetProcessCompletedHvtBay()) {
                Thread.sleep(200);
            }

            Platform.runLater(() -> {
                btnHvtStart.setDisable(false);
                btnHvtStart.setStyle(""); // Enabled - Default
                btnHvtStop.setDisable(false);
                btnHvtStop.setStyle(""); // Enabled - Default
                ApplicationLauncher.setCursor(Cursor.DEFAULT);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Hv.logger.warn("Reset-process wait thread interrupted", e);
            e.printStackTrace(); // Added for consistency
        }
    });

    waitForResetCompletion.setDaemon(true);
    waitForResetCompletion.start();

    Hv.logger.info("btnHvtResetOnClick : Exit:");
}

//============================================================================================================================================

@FXML
public void btnIrtStartOnClick() {
    Ir.logger.info("btnIrtStartOnClick : Invoked:");
    
    Ir.logger.info("INSULATION RESISTANCE TEST BAY : STARTED");
    ApplicationLauncher.logger.info("INSULATION RESISTANCE TEST BAY : STARTED");
    
    ConstantConveyor.ALL_LOOP_BREAK_FLAG = false;

    // F L A G S
    Ir.setStartProcessRequestedIrtBay(true);
    Ir.setStopProcessCompletedIrtBay(false);
    Ir.setStopProcessRequestedIrtBay(false);
    Ir.setResetProcessCompletedIrtBay(false);
    Ir.setResetProcessRequestedIrtBay(false);

    // B U T T O N I N T E R L O C K 
    Platform.runLater(() -> {
        btnIrtStart.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
        btnIrtStart.setDisable(true);
        btnIrtStop.setDisable(true);
        btnIrtReset.setDisable(true);
        ApplicationLauncher.setCursor(Cursor.WAIT);
    });

    // L O G I C
    insResStartTaskTimer = new Timer();
    insResStartTaskTimer.schedule(new Ir(), 100);
    // Sleep(500); // Removed
    // insResStartTaskTimer.cancel(); // Removed

    // Background thread to monitor process completion
    Thread waitForStartCompletion = new Thread(() -> {
        try {
            while (!Ir.isStartProcessCompletedIrtBay()) {
                Thread.sleep(200); // polling delay
            }

            // Once start is completed, update UI
            Platform.runLater(() -> {
                DashboardController.highlightGreenBay(DashboardController.getRef_bay3Container());
                btnIrtStop.setDisable(false);
                btnIrtStop.setStyle(""); // Enabled - Default
                btnIrtReset.setDisable(false);
                btnIrtReset.setStyle(""); // Enabled - Default
                ApplicationLauncher.setCursor(Cursor.DEFAULT);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Ir.logger.warn("Start-process wait thread interrupted", e);
            e.printStackTrace(); // Added for consistency
        }
    });

    waitForStartCompletion.setDaemon(true);
    waitForStartCompletion.start();

    Ir.logger.info("btnIrtStartOnClick : Exit:");
}

@FXML
public void btnIrtStopOnClick() {
    Ir.logger.info("btnIrtStopOnClick : Invoked:");
    
    Ir.logger.info("INSULATION RESISTANCE TEST BAY : STOPPED");
    ApplicationLauncher.logger.info("INSULATION RESISTANCE TEST BAY : STOPPED");
    
    // F L A G S
    Ir.abort_IRT_Bay = true;
    Ir.setStartProcessRequestedIrtBay(false);
    Ir.setStopProcessCompletedIrtBay(false);
    Ir.setStopProcessRequestedIrtBay(true);
    Ir.setResetProcessCompletedIrtBay(false);
    Ir.setResetProcessRequestedIrtBay(false);

    // B U T T O N I N T E R L O C K
    Platform.runLater(() -> {
        btnIrtStop.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
        btnIrtStop.setDisable(true);
        btnIrtStart.setDisable(true);
        btnIrtReset.setDisable(true);
        ApplicationLauncher.setCursor(Cursor.WAIT);
    });

    // L O G I C
    insResStopTaskTimer = new Timer();
    insResStopTaskTimer.schedule(new InsulationResistanceTestBayStop(), 100);
    // Sleep(500); // Removed
    // insResStopTaskTimer.cancel(); // Removed

    // Wait for stop process to complete
    Thread waitForStopCompletion = new Thread(() -> {
        try {
            while (!Ir.isStopProcessCompletedIrtBay()) {
                Thread.sleep(200);
            }

            Platform.runLater(() -> {
                DashboardController.resetBayHighlight(DashboardController.getRef_bay3Container());
                ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopTimeUpDisplay(ConstantConveyor.IR_BAY_KEY);
                ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopProgressBarWithTime(ConstantConveyor.IR_BAY_KEY);
                
                btnIrtStart.setDisable(false);
                btnIrtStart.setStyle(""); // Enabled - Default
                btnIrtReset.setDisable(false);
                btnIrtReset.setStyle(""); // Enabled - Default
                ApplicationLauncher.setCursor(Cursor.DEFAULT);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Ir.logger.warn("Stop-process wait thread interrupted", e);
            e.printStackTrace(); // Added for consistency
        }
    });

    waitForStopCompletion.setDaemon(true);
    waitForStopCompletion.start();

    Ir.logger.info("btnIrtStopOnClick : Exit:");
}

@FXML
public void btnIrtResetOnClick() {
    Ir.logger.info("btnIrtResetOnClick : Invoked:");
    
    Ir.logger.info("INSULATION RESISTANCE TEST BAY : RESET");
    ApplicationLauncher.logger.info("INSULATION RESISTANCE TEST BAY : RESET");
    
    // F L A G S
    Ir.setStartProcessRequestedIrtBay(false);
    Ir.setStopProcessCompletedIrtBay(false);
    Ir.setStopProcessRequestedIrtBay(false);
    Ir.setResetProcessCompletedIrtBay(false);
    Ir.setResetProcessRequestedIrtBay(true);
    ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopTimeUpDisplay(ConstantConveyor.IR_BAY_KEY);
    ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopProgressBarWithTime(ConstantConveyor.IR_BAY_KEY);
    
    // B U T T O N I N T E R L O C K
    Platform.runLater(() -> {
        btnIrtReset.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
        btnIrtReset.setDisable(true);
        btnIrtStart.setDisable(true);
        btnIrtStop.setDisable(true);
        ApplicationLauncher.setCursor(Cursor.WAIT);
    });

    // L O G I C
    insResResetTaskTimer = new Timer();
    insResResetTaskTimer.schedule(new InsulationResistanceTestBayReset(), 100);
    // Sleep(500); // Removed
    // insResResetTaskTimer.cancel(); // Removed

    // Wait for reset process to complete
    Thread waitForResetCompletion = new Thread(() -> {
        try {
            while (!Ir.isResetProcessCompletedIrtBay()) {
                Thread.sleep(200);
            }

            Platform.runLater(() -> {
                btnIrtStart.setDisable(false);
                btnIrtStart.setStyle(""); // Enabled - Default
                btnIrtStop.setDisable(false);
                btnIrtStop.setStyle(""); // Enabled - Default
                ApplicationLauncher.setCursor(Cursor.DEFAULT);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Ir.logger.warn("Reset-process wait thread interrupted", e);
            e.printStackTrace(); // Added for consistency
        }
    });

    waitForResetCompletion.setDaemon(true);
    waitForResetCompletion.start();

    Ir.logger.info("btnIrtResetOnClick : Exit:");
}

//============================================================================================================================================

@FXML
public void btnCalibStartOnClick() {
    Calib.logger.info("btnCalibStartOnClick : Invoked:");
    
    Calib.logger.info("CALIBRATION BAY : STARTED");
    ApplicationLauncher.logger.info("CALIBRATION BAY : STARTED");
    
    ConstantConveyor.ALL_LOOP_BREAK_FLAG = false;

    // F L A G S
    Calib.setStartProcessRequestedCalibBay(true);
    Calib.setStopProcessCompletedCalibBay(false);
    Calib.setStopProcessRequestedCalibBay(false);
    Calib.setResetProcessCompletedCalibBay(false);
    Calib.setResetProcessRequestedCalibBay(false);

    // B U T T O N I N T E R L O C K 
    Platform.runLater(() -> {
        btnCalibStart.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
        btnCalibStart.setDisable(true);
        btnCalibStop.setDisable(true);
        btnCalibReset.setDisable(true);
        ApplicationLauncher.setCursor(Cursor.WAIT);
    });

    // L O G I C
    calibrationStartTaskTimer = new Timer();
    calibrationStartTaskTimer.schedule(new Calib(), 100);
    // Sleep(500); // Removed
    // calibrationStartTaskTimer.cancel(); // Removed

    // Background thread to monitor process completion
    Thread waitForStartCompletion = new Thread(() -> {
        try {
            while (!Calib.isStartProcessCompletedCalibBay()) {
                Thread.sleep(200); // polling delay
            }

            // Once start is completed, update UI
            Platform.runLater(() -> {
                DashboardController.highlightGreenBay(DashboardController.getRef_bay4Container());
                btnCalibStop.setDisable(false);
                btnCalibStop.setStyle(""); // Enabled - Default
                btnCalibReset.setDisable(false);
                btnCalibReset.setStyle(""); // Enabled - Default
                ApplicationLauncher.setCursor(Cursor.DEFAULT);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Calib.logger.warn("Start-process wait thread interrupted", e);
            e.printStackTrace(); // Added for consistency
        }
    });

    waitForStartCompletion.setDaemon(true);
    waitForStartCompletion.start();

    Calib.logger.info("btnCalibStartOnClick : Exit:");
}

@FXML
public void btnCalibStopOnClick() {
    Calib.logger.info("btnCalibStopOnClick : Invoked:");
    
    Calib.logger.info("CALIBRATION BAY : STOPPED");
    ApplicationLauncher.logger.info("CALIBRATION BAY : STOPPED");
    
    // F L A G S
    Calib.abort_Calib_Bay = true;
    Calib.setStartProcessRequestedCalibBay(false);
    Calib.setStopProcessCompletedCalibBay(false);
    Calib.setStopProcessRequestedCalibBay(true);
    Calib.setResetProcessCompletedCalibBay(false);
    Calib.setResetProcessRequestedCalibBay(false);

    // B U T T O N I N T E R L O C K
    Platform.runLater(() -> {
        btnCalibStop.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
        btnCalibStop.setDisable(true);
        btnCalibStart.setDisable(true);
        btnCalibReset.setDisable(true);
        ApplicationLauncher.setCursor(Cursor.WAIT);
    });

    // L O G I C
    calibrationStopTaskTimer = new Timer();
    calibrationStopTaskTimer.schedule(new CalibrationBayStop(), 100);
    // Sleep(500); // Removed
    // calibrationStopTaskTimer.cancel(); // Removed

    // Wait for stop process to complete
    Thread waitForStopCompletion = new Thread(() -> {
        try {
            while (!Calib.isStopProcessCompletedCalibBay()) {
                Thread.sleep(200);
            }

            Platform.runLater(() -> {
                DashboardController.resetBayHighlight(DashboardController.getRef_bay4Container());
                ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopTimeUpDisplay(ConstantConveyor.CALIBRATION_BAY_KEY);
                ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopProgressBarWithTime(ConstantConveyor.CALIBRATION_BAY_KEY);
                
                btnCalibStart.setDisable(false);
                btnCalibStart.setStyle(""); // Enabled - Default
                btnCalibReset.setDisable(false);
                btnCalibReset.setStyle(""); // Enabled - Default
                ApplicationLauncher.setCursor(Cursor.DEFAULT);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Calib.logger.warn("Stop-process wait thread interrupted", e);
            e.printStackTrace(); // Added for consistency
        }
    });

    waitForStopCompletion.setDaemon(true);
    waitForStopCompletion.start();

    Calib.logger.info("btnCalibStopOnClick : Exit:");
}

@FXML
public void btnCalibResetOnClick() {
    Calib.logger.info("btnCalibResetOnClick : Invoked:");
    
    Calib.logger.info("CALIBRATION BAY : RESET");
    ApplicationLauncher.logger.info("CALIBRATION BAY : RESET");
    
    // F L A G S
    Calib.setStartProcessRequestedCalibBay(false);
    Calib.setStopProcessCompletedCalibBay(false);
    Calib.setStopProcessRequestedCalibBay(false);
    Calib.setResetProcessCompletedCalibBay(false);
    Calib.setResetProcessRequestedCalibBay(true);
    ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopTimeUpDisplay(ConstantConveyor.CALIBRATION_BAY_KEY);
    ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopProgressBarWithTime(ConstantConveyor.CALIBRATION_BAY_KEY);
    
    // B U T T O N I N T E R L O C K
    Platform.runLater(() -> {
        btnCalibReset.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
        btnCalibReset.setDisable(true);
        btnCalibStart.setDisable(true);
        btnCalibStop.setDisable(true);
        ApplicationLauncher.setCursor(Cursor.WAIT);
    });

    // L O G I C
    calibrationResetTaskTimer = new Timer();
    calibrationResetTaskTimer.schedule(new CalibrationBayReset(), 100);
    // Sleep(500); // Removed
    // calibrationResetTaskTimer.cancel(); // Removed

    // Wait for reset process to complete
    Thread waitForResetCompletion = new Thread(() -> {
        try {
            while (!Calib.isResetProcessCompletedCalibBay()) {
                Thread.sleep(200);
            }

            Platform.runLater(() -> {
                btnCalibStart.setDisable(false);
                btnCalibStart.setStyle(""); // Enabled - Default
                btnCalibStop.setDisable(false);
                btnCalibStop.setStyle(""); // Enabled - Default
                ApplicationLauncher.setCursor(Cursor.DEFAULT);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Calib.logger.warn("Reset-process wait thread interrupted", e);
            e.printStackTrace(); // Added for consistency
        }
    });

    waitForResetCompletion.setDaemon(true);
    waitForResetCompletion.start();

    Calib.logger.info("btnCalibResetOnClick : Exit:");
}

//====================================================================================

@FXML
public void btnWaitingBayStartOnClick() {
    VerificWaiting.logger.info("btnWaitingBayStartOnClick : Invoked:");
    
    VerificWaiting.logger.info("WAITING BAY : STARTED");
    ApplicationLauncher.logger.info("WAITING BAY : STARTED");
    
    // F L A G S
    VerificWaiting.setStartProcessRequestedWaitingBay(true);
    VerificWaiting.setStopProcessCompletedWaitingBay(false);
    VerificWaiting.setStopProcessRequestedWaitingBay(false);
    VerificWaiting.setResetProcessCompletedWaitingBay(false);
    VerificWaiting.setResetProcessRequestedWaitingBay(false);

    // B U T T O N I N T E R L O C K 
    Platform.runLater(() -> {
        btnWaitingBayStart.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
        btnWaitingBayStart.setDisable(true);
        btnWaitingBayStop.setDisable(true);
        btnWaitingBayReset.setDisable(true);
        ApplicationLauncher.setCursor(Cursor.WAIT);
    });

    // L O G I C
    waitingBayStartTaskTimer = new Timer();
    waitingBayStartTaskTimer.schedule(new VerificWaiting(), 100);
    // Sleep(500); // Removed
    // waitingBayStartTaskTimer.cancel(); // Removed

    // Background thread to monitor process completion
    Thread waitForStartCompletion = new Thread(() -> {
        try {
            while (!VerificWaiting.isStartProcessCompletedWaitingBay()) {
                Thread.sleep(200); // polling delay
            }

            // Once start is completed, update UI
            Platform.runLater(() -> {
                DashboardController.highlightGreenBay(DashboardController.getRef_bay5Container());
                DashboardController.highlightGreenBay(DashboardController.getRef_bay6Container());
                DashboardController.highlightGreenBay(DashboardController.getRef_bay7Container());
                DashboardController.highlightGreenBay(DashboardController.getRef_bay8Container());
                btnWaitingBayStop.setDisable(false);
                btnWaitingBayStop.setStyle(""); // Enabled - Default
                btnWaitingBayReset.setDisable(false);
                btnWaitingBayReset.setStyle(""); // Enabled - Default
                ApplicationLauncher.setCursor(Cursor.DEFAULT);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            VerificWaiting.logger.warn("Start-process wait thread interrupted", e);
            e.printStackTrace(); // Added for consistency
        }
    });

    waitForStartCompletion.setDaemon(true);
    waitForStartCompletion.start();

    VerificWaiting.logger.info("btnWaitingBayStartOnClick : Exit:");
}

@FXML
public void btnWaitingBayStopOnClick() {
    VerificWaiting.logger.info("btnWaitingBayStopOnClick : Invoked:");
    
    VerificWaiting.logger.info("WAITING BAY : STOPPED");
    ApplicationLauncher.logger.info("WAITING BAY : STOPPED");
    
    // F L A G S
    VerificWaiting.abort_Waiting_Bay = true;
    VerificWaiting.setStartProcessRequestedWaitingBay(false);
    VerificWaiting.setStopProcessCompletedWaitingBay(false);
    VerificWaiting.setStopProcessRequestedWaitingBay(true);
    VerificWaiting.setResetProcessCompletedWaitingBay(false);
    VerificWaiting.setResetProcessRequestedWaitingBay(false);

    // B U T T O N I N T E R L O C K
    Platform.runLater(() -> {
        btnWaitingBayStop.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
        btnWaitingBayStop.setDisable(true);
        btnWaitingBayStart.setDisable(true);
        btnWaitingBayReset.setDisable(true);
        ApplicationLauncher.setCursor(Cursor.WAIT);
    });

    // L O G I C
    waitingBayStopTaskTimer = new Timer();
    waitingBayStopTaskTimer.schedule(new WaitingBayStop(), 100);
    // Sleep(500); // Removed
    // waitingBayStopTaskTimer.cancel(); // Removed

    // Wait for stop process to complete
    Thread waitForStopCompletion = new Thread(() -> {
        try {
            while (!VerificWaiting.isStopProcessCompletedWaitingBay()) {
                Thread.sleep(200);
            }

            Platform.runLater(() -> {
                DashboardController.resetBayHighlight(DashboardController.getRef_bay5Container());
                DashboardController.resetBayHighlight(DashboardController.getRef_bay6Container());
                DashboardController.resetBayHighlight(DashboardController.getRef_bay7Container());
                DashboardController.resetBayHighlight(DashboardController.getRef_bay8Container());
                btnWaitingBayStart.setDisable(false);
                btnWaitingBayStart.setStyle(""); // Enabled - Default
                btnWaitingBayReset.setDisable(false);
                btnWaitingBayReset.setStyle(""); // Enabled - Default
                ApplicationLauncher.setCursor(Cursor.DEFAULT);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            VerificWaiting.logger.warn("Stop-process wait thread interrupted", e);
            e.printStackTrace(); // Added for consistency
        }
    });

    waitForStopCompletion.setDaemon(true);
    waitForStopCompletion.start();

    VerificWaiting.logger.info("btnWaitingBayStopOnClick : Exit:");
}

@FXML
public void btnWaitingBayResetOnClick() {
    VerificWaiting.logger.info("btnWaitingBayResetOnClick : Invoked:");
    
    VerificWaiting.logger.info("WAITING BAY : RESET");
    ApplicationLauncher.logger.info("WAITING BAY : RESET");
    
    // F L A G S
    VerificWaiting.setStartProcessRequestedWaitingBay(false);
    VerificWaiting.setStopProcessCompletedWaitingBay(false);
    VerificWaiting.setStopProcessRequestedWaitingBay(false);
    VerificWaiting.setResetProcessCompletedWaitingBay(false);
    VerificWaiting.setResetProcessRequestedWaitingBay(true);

    // B U T T O N I N T E R L O C K
    Platform.runLater(() -> {
        btnWaitingBayReset.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
        btnWaitingBayReset.setDisable(true);
        btnWaitingBayStart.setDisable(true);
        btnWaitingBayStop.setDisable(true);
        ApplicationLauncher.setCursor(Cursor.WAIT);
    });

    // L O G I C
    waitingBayResetTaskTimer = new Timer();
    waitingBayResetTaskTimer.schedule(new WaitingBayReset(), 100);
    // Sleep(500); // Removed
    // waitingBayResetTaskTimer.cancel(); // Removed

    // Wait for reset process to complete
    Thread waitForResetCompletion = new Thread(() -> {
        try {
            while (!VerificWaiting.isResetProcessCompletedWaitingBay()) {
                Thread.sleep(200);
            }

            Platform.runLater(() -> {
                btnWaitingBayStart.setDisable(false);
                btnWaitingBayStart.setStyle(""); // Enabled - Default
                btnWaitingBayStop.setDisable(false);
                btnWaitingBayStop.setStyle(""); // Enabled - Default
                ApplicationLauncher.setCursor(Cursor.DEFAULT);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            VerificWaiting.logger.warn("Reset-process wait thread interrupted", e);
            e.printStackTrace(); // Added for consistency
        }
    });

    waitForResetCompletion.setDaemon(true);
    waitForResetCompletion.start();

    VerificWaiting.logger.info("btnWaitingBayResetOnClick : Exit:");
}

//============================================================================================================================================

@FXML
public void btnVerificTestStartOnClick() {
    Verification.logger.info("btnVerificTestStartOnClick : Invoked:");
    
    Verification.logger.info("VERIFICATION TEST BAY : STARTED");
    ApplicationLauncher.logger.info("VERIFICATION TEST BAY : STARTED");
    
    ConstantConveyor.ALL_LOOP_BREAK_FLAG = false;

    // F L A G S
    Verification.setStartProcessRequestedVerificBay(true);
    Verification.setStopProcessCompletedVerificBay(false);
    Verification.setStopProcessRequestedVerificBay(false);
    Verification.setResetProcessCompletedVerificBay(false);
    Verification.setResetProcessRequestedVerificBay(false);

    // B U T T O N I N T E R L O C K 
    Platform.runLater(() -> {
        btnVerificTestStart.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
        btnVerificTestStart.setDisable(true);
        btnVerificTestStop.setDisable(true);
        btnVerificTestReset.setDisable(true);
        ApplicationLauncher.setCursor(Cursor.WAIT);
    });

    // L O G I C
    verificStartTaskTimer = new Timer();
    verificStartTaskTimer.schedule(new Verification(), 100);
    // Sleep(500); // Removed
    // verificStartTaskTimer.cancel(); // Removed

    // Background thread to monitor process completion
    Thread waitForStartCompletion = new Thread(() -> {
        try {
            while (!Verification.isStartProcessCompletedVerificBay()) {
                Thread.sleep(200); // polling delay
            }

            // Once start is completed, update UI
            Platform.runLater(() -> {
                DashboardController.highlightGreenBay(DashboardController.getRef_bay9Container());
                DashboardController.highlightGreenBay(DashboardController.getRef_bay10Container());
                DashboardController.highlightGreenBay(DashboardController.getRef_bay11Container());
                DashboardController.highlightGreenBay(DashboardController.getRef_bay12Container());
                btnVerificTestStop.setDisable(false);
                btnVerificTestStop.setStyle(""); // Enabled - Default
                btnVerificTestReset.setDisable(false);
                btnVerificTestReset.setStyle(""); // Enabled - Default
                ApplicationLauncher.setCursor(Cursor.DEFAULT);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Verification.logger.warn("Start-process wait thread interrupted", e);
            e.printStackTrace(); // Added for consistency
        }
    });

    waitForStartCompletion.setDaemon(true);
    waitForStartCompletion.start();

    Verification.logger.info("btnVerificTestStartOnClick : Exit:");
}

@FXML
public void btnVerificTestStopOnClick() {
    Verification.logger.info("btnVerificTestStopOnClick : Invoked:");
    
    Verification.logger.info("VERIFICATION TEST BAY : STOPPED");
    ApplicationLauncher.logger.info("VERIFICATION TEST BAY : STOPPED");
    
    // F L A G S
    Verification.abort_VerificTest_Bay = true;
    Verification.setStartProcessRequestedVerificBay(false);
    Verification.setStopProcessCompletedVerificBay(false);
    Verification.setStopProcessRequestedVerificBay(true);
    Verification.setResetProcessCompletedVerificBay(false);
    Verification.setResetProcessRequestedVerificBay(false);

    // B U T T O N I N T E R L O C K
    Platform.runLater(() -> {
        btnVerificTestStop.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
        btnVerificTestStop.setDisable(true);
        btnVerificTestStart.setDisable(true);
        btnVerificTestReset.setDisable(true);
        ApplicationLauncher.setCursor(Cursor.WAIT);
    });

    // L O G I C
    verificStopTaskTimer = new Timer();
    verificStopTaskTimer.schedule(new VerificationTestBayStop(), 100);
    // Sleep(500); // Removed
    // verificStopTaskTimer.cancel(); // Removed

    // Wait for stop process to complete
    Thread waitForStopCompletion = new Thread(() -> {
        try {
            while (!Verification.isStopProcessCompletedVerificBay()) {
                Thread.sleep(200);
            }

            Platform.runLater(() -> {
                DashboardController.resetBayHighlight(DashboardController.getRef_bay9Container());
                DashboardController.resetBayHighlight(DashboardController.getRef_bay10Container());
                DashboardController.resetBayHighlight(DashboardController.getRef_bay11Container());
                DashboardController.resetBayHighlight(DashboardController.getRef_bay12Container());
                ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopTimeUpDisplay(ConstantConveyor.VERIFICATION_PP1_BAY_KEY);
                ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopProgressBarWithTpCount(ConstantConveyor.VERIFICATION_PP1_BAY_KEY);
                
                btnVerificTestStart.setDisable(false);
                btnVerificTestStart.setStyle(""); // Enabled - Default
                btnVerificTestReset.setDisable(false);
                btnVerificTestReset.setStyle(""); // Enabled - Default
                ApplicationLauncher.setCursor(Cursor.DEFAULT);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Verification.logger.warn("Stop-process wait thread interrupted", e);
            e.printStackTrace(); // Added for consistency
        }
    });

    waitForStopCompletion.setDaemon(true);
    waitForStopCompletion.start();

    Verification.logger.info("btnVerificTestStopOnClick : Exit:");
}

public void verificTestStop() {
    Verification.logger.info("verificTestStop : Invoked : from S17 Error Handling");
    
    Verification.logger.info("VERIFICATION TEST BAY : STOPPED");
    ApplicationLauncher.logger.info("VERIFICATION TEST BAY : STOPPED");
    
    // F L A G S
    Verification.abort_VerificTest_Bay = true;
    Verification.setStartProcessRequestedVerificBay(false);
    Verification.setStopProcessCompletedVerificBay(false);
    Verification.setStopProcessRequestedVerificBay(true);
    Verification.setResetProcessCompletedVerificBay(false);
    Verification.setResetProcessRequestedVerificBay(false);

    // B U T T O N I N T E R L O C K
    Platform.runLater(() -> {
        ref_btnVerificTestStop.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
        ref_btnVerificTestStop.setDisable(true);
        ref_btnVerificTestStart.setDisable(true);
        ref_btnVerificTestReset.setDisable(true);
        ApplicationLauncher.setCursor(Cursor.WAIT);
    });

    // L O G I C
    verificStopTaskTimer = new Timer();
    verificStopTaskTimer.schedule(new VerificationTestBayStop(), 100);
    // Sleep(500); // Removed
    // verificStopTaskTimer.cancel(); // Removed

    // Wait for stop process to complete
    Thread waitForStopCompletion = new Thread(() -> {
        try {
            while (!Verification.isStopProcessCompletedVerificBay()) {
                Thread.sleep(200);
            }

            Platform.runLater(() -> {
                DashboardController.resetBayHighlight(DashboardController.getRef_bay9Container());
                DashboardController.resetBayHighlight(DashboardController.getRef_bay10Container());
                DashboardController.resetBayHighlight(DashboardController.getRef_bay11Container());
                DashboardController.resetBayHighlight(DashboardController.getRef_bay12Container());
                ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopTimeUpDisplay(ConstantConveyor.VERIFICATION_PP1_BAY_KEY);
                ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopProgressBarWithTpCount(ConstantConveyor.VERIFICATION_PP1_BAY_KEY);
                
                ref_btnVerificTestStart.setDisable(false);
                ref_btnVerificTestStart.setStyle(""); // Enabled - Default
                ref_btnVerificTestReset.setDisable(false);
                ref_btnVerificTestReset.setStyle(""); // Enabled - Default
                ApplicationLauncher.setCursor(Cursor.DEFAULT);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Verification.logger.warn("Stop-process wait thread interrupted", e);
            e.printStackTrace(); // Added for consistency
        }
    });

    waitForStopCompletion.setDaemon(true);
    waitForStopCompletion.start();

    Verification.logger.info("btnVerificTestStopOnClick : Exit:");
}

@FXML
public void btnVerificTestResetOnClick() {
    Verification.logger.info("btnVerificTestResetOnClick : Invoked:");
    
    Verification.logger.info("VERIFICATION TEST BAY : RESET");
    ApplicationLauncher.logger.info("VERIFICATION TEST BAY : RESET");
    
    // F L A G S
    Verification.setStartProcessRequestedVerificBay(false);
    Verification.setStopProcessCompletedVerificBay(false);
    Verification.setStopProcessRequestedVerificBay(false);
    Verification.setResetProcessCompletedVerificBay(false);
    Verification.setResetProcessRequestedVerificBay(true);
    ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopTimeUpDisplay(ConstantConveyor.VERIFICATION_PP1_BAY_KEY);
    ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopProgressBarWithTpCount(ConstantConveyor.VERIFICATION_PP1_BAY_KEY);
    
    // B U T T O N I N T E R L O C K
    Platform.runLater(() -> {
        btnVerificTestReset.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
        btnVerificTestReset.setDisable(true);
        btnVerificTestStart.setDisable(true);
        btnVerificTestStop.setDisable(true);
        ApplicationLauncher.setCursor(Cursor.WAIT);
    });

    // L O G I C
    verificResetTaskTimer = new Timer();
    verificResetTaskTimer.schedule(new VerificationTestBayReset(), 100);
    // Sleep(500); // Removed
    // verificResetTaskTimer.cancel(); // Removed

    // Wait for reset process to complete
    Thread waitForResetCompletion = new Thread(() -> {
        try {
            while (!Verification.isResetProcessCompletedVerificBay()) {
                Thread.sleep(200);
            }

            Platform.runLater(() -> {
                btnVerificTestStart.setDisable(false);
                btnVerificTestStart.setStyle(""); // Enabled - Default
                btnVerificTestStop.setDisable(false);
                btnVerificTestStop.setStyle(""); // Enabled - Default
                ApplicationLauncher.setCursor(Cursor.DEFAULT);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Verification.logger.warn("Reset-process wait thread interrupted", e);
            e.printStackTrace(); // Added for consistency
        }
    });

    waitForResetCompletion.setDaemon(true);
    waitForResetCompletion.start();

    Verification.logger.info("btnVerificTestResetOnClick : Exit:");
}

//============================================================================================================================================

@FXML
public void btnSctNlt1StartOnClick() {
    StaNld_Bay1.logger.info("btnSctNlt1StartOnClick : Invoked:");
    
    StaNld_Bay1.logger.info("SCT NLT BAY 1 : STARTED");
    ApplicationLauncher.logger.info("SCT NLT BAY 1 : STARTED");
    
    ConstantConveyor.ALL_LOOP_BREAK_FLAG = false;

    // F L A G S
    StaNld_Bay1.setStartProcessRequestedStaNldBay1(true);
    StaNld_Bay1.setStopProcessCompletedStaNldBay1(false);
    StaNld_Bay1.setStopProcessRequestedStaNldBay1(false);
    StaNld_Bay1.setResetProcessCompletedStaNldBay1(false);
    StaNld_Bay1.setResetProcessRequestedStaNldBay1(false);

    // B U T T O N I N T E R L O C K 
    Platform.runLater(() -> {
        btnSctNlt1Start.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
        btnSctNlt1Start.setDisable(true);
        btnSctNlt1Stop.setDisable(true);
        btnSctNlt1Reset.setDisable(true);
        ApplicationLauncher.setCursor(Cursor.WAIT);
    });

    // L O G I C
    sctNlt1StartTaskTimer = new Timer();
    sctNlt1StartTaskTimer.schedule(new StaNld_Bay1(), 100);
    // Sleep(500); // Removed
    // sctNlt1StartTaskTimer.cancel(); // Removed

    // Background thread to monitor process completion
    Thread waitForStartCompletion = new Thread(() -> {
        try {
            while (!StaNld_Bay1.isStartProcessCompletedStaNldBay1()) {
                Thread.sleep(200); // polling delay
            }

            // Once start is completed, update UI
            Platform.runLater(() -> {
                DashboardController.highlightGreenBay(DashboardController.getRef_bay13Container());
                DashboardController.highlightGreenBay(DashboardController.getRef_bay14Container());
                DashboardController.highlightGreenBay(DashboardController.getRef_bay15Container());
                DashboardController.highlightGreenBay(DashboardController.getRef_bay16Container());
                btnSctNlt1Stop.setDisable(false);
                btnSctNlt1Stop.setStyle(""); // Enabled - Default
                btnSctNlt1Reset.setDisable(false);
                btnSctNlt1Reset.setStyle(""); // Enabled - Default
                ApplicationLauncher.setCursor(Cursor.DEFAULT);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            StaNld_Bay1.logger.warn("Start-process wait thread interrupted", e);
            e.printStackTrace(); // Added for consistency
        }
    });

    waitForStartCompletion.setDaemon(true);
    waitForStartCompletion.start();

    StaNld_Bay1.logger.info("btnSctNlt1StartOnClick : Exit:");
}

@FXML
public void btnSctNlt1StopOnClick() {
    StaNld_Bay1.logger.info("btnSctNlt1StopOnClick : Invoked:");
    
    StaNld_Bay1.logger.info("SCT NLT BAY 1 : STOPPED");
    ApplicationLauncher.logger.info("SCT NLT BAY 1 : STOPPED");
    
    // F L A G S
    StaNld_Bay1.abort_SCT_NLT_Bay1 = true;
    StaNld_Bay1.setStartProcessRequestedStaNldBay1(false);
    StaNld_Bay1.setStopProcessCompletedStaNldBay1(false);
    StaNld_Bay1.setStopProcessRequestedStaNldBay1(true);
    StaNld_Bay1.setResetProcessCompletedStaNldBay1(false);
    StaNld_Bay1.setResetProcessRequestedStaNldBay1(false);

    // B U T T O N I N T E R L O C K
    Platform.runLater(() -> {
        btnSctNlt1Stop.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
        btnSctNlt1Stop.setDisable(true);
        btnSctNlt1Start.setDisable(true);
        btnSctNlt1Reset.setDisable(true);
        ApplicationLauncher.setCursor(Cursor.WAIT);
    });

    // L O G I C
    sctNlt1StopTaskTimer = new Timer();
    sctNlt1StopTaskTimer.schedule(new STA_NoLoadTestBay1Stop(), 100);
    // Sleep(500); // Removed
    // sctNlt1StopTaskTimer.cancel(); // Removed

    // Wait for stop process to complete
    Thread waitForStopCompletion = new Thread(() -> {
        try {
            while (!StaNld_Bay1.isStopProcessCompletedStaNldBay1()) {
                Thread.sleep(200);
            }

            Platform.runLater(() -> {
                DashboardController.resetBayHighlight(DashboardController.getRef_bay13Container());
                DashboardController.resetBayHighlight(DashboardController.getRef_bay14Container());
                DashboardController.resetBayHighlight(DashboardController.getRef_bay15Container());
                DashboardController.resetBayHighlight(DashboardController.getRef_bay16Container());
                ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopTimeUpDisplay(ConstantConveyor.STA_NLD1_PP1_BAY_KEY);
                ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopProgressBarWithTime(ConstantConveyor.STA_NLD1_PP1_BAY_KEY);
                
                btnSctNlt1Start.setDisable(false);
                btnSctNlt1Start.setStyle(""); // Enabled - Default
                btnSctNlt1Reset.setDisable(false);
                btnSctNlt1Reset.setStyle(""); // Enabled - Default
                ApplicationLauncher.setCursor(Cursor.DEFAULT);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            StaNld_Bay1.logger.warn("Stop-process wait thread interrupted", e);
            e.printStackTrace(); // Added for consistency
        }
    });

    waitForStopCompletion.setDaemon(true);
    waitForStopCompletion.start();

    StaNld_Bay1.logger.info("btnSctNlt1StopOnClick : Exit:");
}

@FXML
public void btnSctNlt1ResetOnClick() {
    StaNld_Bay1.logger.info("btnSctNlt1ResetOnClick : Invoked:");
    
    StaNld_Bay1.logger.info("SCT NLT BAY 1 : RESET");
    ApplicationLauncher.logger.info("SCT NLT BAY 1 : RESET");
    
    // F L A G S
    StaNld_Bay1.setStartProcessRequestedStaNldBay1(false);
    StaNld_Bay1.setStopProcessCompletedStaNldBay1(false);
    StaNld_Bay1.setStopProcessRequestedStaNldBay1(false);
    StaNld_Bay1.setResetProcessCompletedStaNldBay1(false);
    StaNld_Bay1.setResetProcessRequestedStaNldBay1(true);
    ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopTimeUpDisplay(ConstantConveyor.STA_NLD1_PP1_BAY_KEY);
    ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopProgressBarWithTime(ConstantConveyor.STA_NLD1_PP1_BAY_KEY);
    
    // B U T T O N I N T E R L O C K
    Platform.runLater(() -> {
        btnSctNlt1Reset.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
        btnSctNlt1Reset.setDisable(true);
        btnSctNlt1Start.setDisable(true);
        btnSctNlt1Stop.setDisable(true);
        ApplicationLauncher.setCursor(Cursor.WAIT);
    });

    // L O G I C
    sctNlt1ResetTaskTimer = new Timer();
    sctNlt1ResetTaskTimer.schedule(new STA_NoLoadTestBay1Reset(), 100);
    // Sleep(500); // Removed
    // sctNlt1ResetTaskTimer.cancel(); // Removed

    // Wait for reset process to complete
    Thread waitForResetCompletion = new Thread(() -> {
        try {
            while (!StaNld_Bay1.isResetProcessCompletedStaNldBay1()) {
                Thread.sleep(200);
            }

            Platform.runLater(() -> {
                btnSctNlt1Start.setDisable(false);
                btnSctNlt1Start.setStyle(""); // Enabled - Default
                btnSctNlt1Stop.setDisable(false);
                btnSctNlt1Stop.setStyle(""); // Enabled - Default
                ApplicationLauncher.setCursor(Cursor.DEFAULT);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            StaNld_Bay1.logger.warn("Reset-process wait thread interrupted", e);
            e.printStackTrace(); // Added for consistency
        }
    });

    waitForResetCompletion.setDaemon(true);
    waitForResetCompletion.start();

    StaNld_Bay1.logger.info("btnSctNlt1ResetOnClick : Exit:");
}

//============================================================================================================================================

@FXML
public void btnSctNlt2StartOnClick() {
    StaNld_Bay2.logger.info("btnSctNlt2StartOnClick-X : Invoked:");
    
    StaNld_Bay2.logger.info("SCT NLT BAY 2 : STARTED");
    ApplicationLauncher.logger.info("SCT NLT BAY 2 : STARTED");
    
    ConstantConveyor.ALL_LOOP_BREAK_FLAG = false;

    // F L A G S
    StaNld_Bay2.setStartProcessRequestedStaNldBay2(true);
    StaNld_Bay2.setStopProcessCompletedStaNldBay2(false);
    StaNld_Bay2.setStopProcessRequestedStaNldBay2(false);
    StaNld_Bay2.logger.info("setStopProcessRequestedStaNldBay2 -Test3  : false");
    StaNld_Bay2.setResetProcessCompletedStaNldBay2(false);
    StaNld_Bay2.setResetProcessRequestedStaNldBay2(false);

    // B U T T O N I N T E R L O C K 
    Platform.runLater(() -> {
        btnSctNlt2Start.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
        btnSctNlt2Start.setDisable(true);
        btnSctNlt2Stop.setDisable(true);
        btnSctNlt2Reset.setDisable(true);
        ApplicationLauncher.setCursor(Cursor.WAIT);
    });

    // L O G I C
    sctNlt2StartTaskTimer = new Timer();
    sctNlt2StartTaskTimer.schedule(new StaNld_Bay2(), 100);
    // Sleep(500); // Removed
    // sctNlt2StartTaskTimer.cancel(); // Removed

    // Background thread to monitor process completion
    Thread waitForStartCompletion = new Thread(() -> {
        try {
            while (!StaNld_Bay2.isStartProcessCompletedStaNldBay2()) {
                Thread.sleep(200); // polling delay
            }

            // Once start is completed, update UI
            Platform.runLater(() -> {
                DashboardController.highlightGreenBay(DashboardController.getRef_bay17Container());
                DashboardController.highlightGreenBay(DashboardController.getRef_bay18Container());
                DashboardController.highlightGreenBay(DashboardController.getRef_bay19Container());
                DashboardController.highlightGreenBay(DashboardController.getRef_bay20Container());
                btnSctNlt2Stop.setDisable(false);
                btnSctNlt2Stop.setStyle(""); // Enabled - Default
                btnSctNlt2Reset.setDisable(false);
                btnSctNlt2Reset.setStyle(""); // Enabled - Default
                ApplicationLauncher.setCursor(Cursor.DEFAULT);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            StaNld_Bay2.logger.warn("Start-process wait thread interrupted", e);
            e.printStackTrace(); // Added for consistency
        }
    });

    waitForStartCompletion.setDaemon(true);
    waitForStartCompletion.start();

    StaNld_Bay2.logger.info("btnSctNlt2StartOnClick : Exit:");
}

@FXML
public void btnSctNlt2StopOnClick() {
    StaNld_Bay2.logger.info("btnSctNlt2StopOnClick : Invoked:");
    
    StaNld_Bay2.logger.info("SCT NLT BAY 2 : STOPPED");
    ApplicationLauncher.logger.info("SCT NLT BAY 2 : STOPPED");
    
    // F L A G S
    StaNld_Bay2.abort_SCT_NLT_Bay2 = true;
    StaNld_Bay2.setStartProcessRequestedStaNldBay2(false);
    StaNld_Bay2.setStopProcessCompletedStaNldBay2(false);
    StaNld_Bay2.setStopProcessRequestedStaNldBay2(true);
    StaNld_Bay2.logger.info("setStopProcessRequestedStaNldBay2 -Test4  : true");
    StaNld_Bay2.setResetProcessCompletedStaNldBay2(false);
    StaNld_Bay2.setResetProcessRequestedStaNldBay2(false);

    // B U T T O N I N T E R L O C K
    Platform.runLater(() -> {
        btnSctNlt2Stop.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
        btnSctNlt2Stop.setDisable(true);
        btnSctNlt2Start.setDisable(true);
        btnSctNlt2Reset.setDisable(true);
        ApplicationLauncher.setCursor(Cursor.WAIT);
    });

    // L O G I C
    sctNlt2StopTaskTimer = new Timer();
    sctNlt2StopTaskTimer.schedule(new STA_NoLoadTestBay2Stop(), 100);
    // Sleep(500); // Removed
    // sctNlt2StopTaskTimer.cancel(); // Removed
    ApplicationLauncher.logger.info("SCT NLT BAY 2 : waitForStopCompletion: Thread trigger");
    // Wait for stop process to complete
    Thread waitForStopCompletion = new Thread(() -> {
        try {
        	ApplicationLauncher.logger.info("SCT NLT BAY 2 : awaiting for isStopProcessCompletedSctNltBay2 : Entry");
            while (!StaNld_Bay2.isStopProcessCompletedStaNldBay2()) {
                Thread.sleep(200);
                ApplicationLauncher.logger.info("SCT NLT BAY 2 : still awaiting for isStopProcessCompletedSctNltBay2");
            }
            ApplicationLauncher.logger.info("SCT NLT BAY 2 : awaiting for isStopProcessCompletedSctNltBay2 : Exit");

            Platform.runLater(() -> {
                DashboardController.resetBayHighlight(DashboardController.getRef_bay17Container());
                DashboardController.resetBayHighlight(DashboardController.getRef_bay18Container());
                DashboardController.resetBayHighlight(DashboardController.getRef_bay19Container());
                DashboardController.resetBayHighlight(DashboardController.getRef_bay20Container());
                ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopTimeUpDisplay(ConstantConveyor.STA_NLD2_PP1_BAY_KEY);
                ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopProgressBarWithTime(ConstantConveyor.STA_NLD2_PP1_BAY_KEY);
                
                btnSctNlt2Start.setDisable(false);
                btnSctNlt2Start.setStyle(""); // Enabled - Default
                btnSctNlt2Reset.setDisable(false);
                btnSctNlt2Reset.setStyle(""); // Enabled - Default
                ApplicationLauncher.setCursor(Cursor.DEFAULT);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            StaNld_Bay2.logger.warn("Stop-process wait thread interrupted", e);
            e.printStackTrace(); // Added for consistency
        }
    });

    waitForStopCompletion.setDaemon(true);
    waitForStopCompletion.start();

    StaNld_Bay2.logger.info("btnSctNlt2StopOnClick : Exit:");
}

@FXML
public void btnSctNlt2ResetOnClick() {
    StaNld_Bay2.logger.info("btnSctNlt2ResetOnClick : Invoked:");
    
    StaNld_Bay2.logger.info("SCT NLT BAY 2 : RESET");
    ApplicationLauncher.logger.info("SCT NLT BAY 2 : RESET");
    
    // F L A G S
    StaNld_Bay2.setStartProcessRequestedStaNldBay2(false);
    StaNld_Bay2.setStopProcessCompletedStaNldBay2(false);
    StaNld_Bay2.setStopProcessRequestedStaNldBay2(false);
    StaNld_Bay2.logger.info("setStopProcessRequestedStaNldBay2 -Test5  : false");
    StaNld_Bay2.setResetProcessCompletedStaNldBay2(false);
    StaNld_Bay2.setResetProcessRequestedStaNldBay2(true);
    ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopTimeUpDisplay(ConstantConveyor.STA_NLD2_PP1_BAY_KEY);
    ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopProgressBarWithTime(ConstantConveyor.STA_NLD2_PP1_BAY_KEY);
    
    // B U T T O N I N T E R L O C K
    Platform.runLater(() -> {
        btnSctNlt2Reset.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
        btnSctNlt2Reset.setDisable(true);
        btnSctNlt2Start.setDisable(true);
        btnSctNlt2Stop.setDisable(true);
        ApplicationLauncher.setCursor(Cursor.WAIT);
    });

    // L O G I C
    sctNlt2ResetTaskTimer = new Timer();
    sctNlt2ResetTaskTimer.schedule(new STA_NoLoadTestBay2Reset(), 100);
    // Sleep(500); // Removed
    // sctNlt2ResetTaskTimer.cancel(); // Removed

    // Wait for reset process to complete
    Thread waitForResetCompletion = new Thread(() -> {
        try {
            while (!StaNld_Bay2.isResetProcessCompletedStaNldBay2()) {
                Thread.sleep(200);
            }

            Platform.runLater(() -> {
                btnSctNlt2Start.setDisable(false);
                btnSctNlt2Start.setStyle(""); // Enabled - Default
                btnSctNlt2Stop.setDisable(false);
                btnSctNlt2Stop.setStyle(""); // Enabled - Default
                ApplicationLauncher.setCursor(Cursor.DEFAULT);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            StaNld_Bay2.logger.warn("Reset-process wait thread interrupted", e);
            e.printStackTrace(); // Added for consistency
        }
    });

    waitForResetCompletion.setDaemon(true);
    waitForResetCompletion.start();

    StaNld_Bay2.logger.info("btnSctNlt2ResetOnClick : Exit:");
}

//============================================================================================================================================

@FXML
public void btnCommTestStartOnClick() {
    Comm.logger.info("btnCommTestStartOnClick : Invoked:");
    
    Comm.logger.info("COMMUNICATION TEST BAY : STARTED");
    ApplicationLauncher.logger.info("COMMUNICATION TEST BAY : STARTED");
    
    ConstantConveyor.ALL_LOOP_BREAK_FLAG = false;

    // F L A G S
    Comm.setStartProcessRequestedCommBay(true);
    Comm.setStopProcessCompletedCommBay(false);
    Comm.setStopProcessRequestedCommBay(false);
    Comm.setResetProcessCompletedCommBay(false);
    Comm.setResetProcessRequestedCommBay(false);

    // B U T T O N I N T E R L O C K
    Platform.runLater(() -> {
        btnCommTestStart.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
        btnCommTestStart.setDisable(true);
        btnCommTestStop.setDisable(true);
        btnCommTestReset.setDisable(true);
        ApplicationLauncher.setCursor(Cursor.WAIT);
    });

    // L O G I C
    commStartTaskTimer = new Timer();
    commStartTaskTimer.schedule(new Comm(), 100);
    // Sleep(500); // Removed
    // commStartTaskTimer.cancel(); // Removed

    // Background thread to monitor process completion
    Thread waitForStartCompletion = new Thread(() -> {
        try {
            while (!Comm.isStartProcessCompletedCommBay()) {
                Thread.sleep(200); // polling delay
            }

            // Once start is completed, update UI
            Platform.runLater(() -> {
                // DashboardController.highlightBay(DashboardController.getRef_bayXContainer()); // Not applicable for this bay
                btnCommTestStop.setDisable(false);
                btnCommTestStop.setStyle(""); // Enabled - Default
                btnCommTestReset.setDisable(false);
                btnCommTestReset.setStyle(""); // Enabled - Default
                ApplicationLauncher.setCursor(Cursor.DEFAULT);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Comm.logger.warn("Start-process wait thread interrupted", e);
            e.printStackTrace(); // Added for consistency
        }
    });

    waitForStartCompletion.setDaemon(true);
    waitForStartCompletion.start();

    Comm.logger.info("btnCommTestStartOnClick : Exit:");
}

@FXML
public void btnCommTestStopOnClick() {
    Comm.logger.info("btnCommTestStopOnClick : Invoked:");
    
    Comm.logger.info("COMMUNICATION TEST BAY : STOPPED");
    ApplicationLauncher.logger.info("COMMUNICATION TEST BAY : STOPPED");
    
    // F L A G S
    Comm.abort_CommTest_Bay = true;
    Comm.setStartProcessRequestedCommBay(false);
    Comm.setStopProcessCompletedCommBay(false);
    Comm.setStopProcessRequestedCommBay(true);
    Comm.setResetProcessCompletedCommBay(false);
    Comm.setResetProcessRequestedCommBay(false);

    // B U T T O N I N T E R L O C K
    Platform.runLater(() -> {
        btnCommTestStop.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
        btnCommTestStop.setDisable(true);
        btnCommTestStart.setDisable(true);
        btnCommTestReset.setDisable(true);
        ApplicationLauncher.setCursor(Cursor.WAIT);
    });

    // L O G I C
    commStopTaskTimer = new Timer();
    commStopTaskTimer.schedule(new CommunicationTestBayStop(), 100);
    // Sleep(500); // Removed
    // commStopTaskTimer.cancel(); // Removed

    // Wait for stop process to complete
    Thread waitForStopCompletion = new Thread(() -> {
        try {
            while (!Comm.isStopProcessCompletedCommBay()) {
                Thread.sleep(200);
            }

            Platform.runLater(() -> {
                btnCommTestStart.setDisable(false);
                btnCommTestStart.setStyle(""); // Enabled - Default
                btnCommTestReset.setDisable(false);
                btnCommTestReset.setStyle(""); // Enabled - Default
                ApplicationLauncher.setCursor(Cursor.DEFAULT);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Comm.logger.warn("Stop-process wait thread interrupted", e);
            e.printStackTrace(); // Added for consistency
        }
    });

    waitForStopCompletion.setDaemon(true);
    waitForStopCompletion.start();

    Comm.logger.info("btnCommTestStopOnClick : Exit:");
}

@FXML
public void btnCommTestResetOnClick() {
    Comm.logger.info("btnCommTestResetOnClick : Invoked:");
    
    Comm.logger.info("COMMUNICATION TEST BAY : RESET");
    ApplicationLauncher.logger.info("COMMUNICATION TEST BAY : RESET");
    
    // F L A G S
    Comm.setStartProcessRequestedCommBay(false);
    Comm.setStopProcessCompletedCommBay(false);
    Comm.setStopProcessRequestedCommBay(false);
    Comm.setResetProcessCompletedCommBay(false);
    Comm.setResetProcessRequestedCommBay(true);

    // B U T T O N I N T E R L O C K
    Platform.runLater(() -> {
        btnCommTestReset.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
        btnCommTestReset.setDisable(true);
        btnCommTestStart.setDisable(true);
        btnCommTestStop.setDisable(true);
        ApplicationLauncher.setCursor(Cursor.WAIT);
    });

    // L O G I C
    commResetTaskTimer = new Timer();
    commResetTaskTimer.schedule(new CommunicationTestBayReset(), 100);
    // Sleep(500); // Removed
    // commResetTaskTimer.cancel(); // Removed

    // Wait for reset process to complete
    Thread waitForResetCompletion = new Thread(() -> {
        try {
            while (!Comm.isResetProcessCompletedCommBay()) {
                Thread.sleep(200);
            }

            Platform.runLater(() -> {
                btnCommTestStart.setDisable(false);
                btnCommTestStart.setStyle(""); // Enabled - Default
                btnCommTestStop.setDisable(false);
                btnCommTestStop.setStyle(""); // Enabled - Default
                ApplicationLauncher.setCursor(Cursor.DEFAULT);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Comm.logger.warn("Reset-process wait thread interrupted", e);
            e.printStackTrace(); // Added for consistency
        }
    });

    waitForResetCompletion.setDaemon(true);
    waitForResetCompletion.start();

    Comm.logger.info("btnCommTestResetOnClick : Exit:");
}

//============================================================================================================================================

@FXML
public void btnUnloadingStartOnClick() {
    Unloading.logger.info("btnUnloadingStartOnClick : Invoked:");
    
    Unloading.logger.info("UNLOADING BAY : STARTED");
    ApplicationLauncher.logger.info("UNLOADING BAY : STARTED");
    
    ConstantConveyor.ALL_LOOP_BREAK_FLAG = false;

    // F L A G S
    Unloading.setStartProcessRequestedUnloadingBay(true);
    Unloading.setStopProcessCompletedUnloadingBay(false);
    Unloading.setStopProcessRequestedUnloadingBay(false);
    Unloading.setResetProcessCompletedUnloadingBay(false);
    Unloading.setResetProcessRequestedUnloadingBay(false);

    // B U T T O N I N T E R L O C K
    Platform.runLater(() -> {
        btnUnloadingStart.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
        btnUnloadingStart.setDisable(true);
        btnUnloadingStop.setDisable(true);
        btnUnloadingReset.setDisable(true);
        ApplicationLauncher.setCursor(Cursor.WAIT);
    });

    // L O G I C
    unloadingBayStartTaskTimer = new Timer();
    unloadingBayStartTaskTimer.schedule(new Unloading(), 100);
    // Sleep(500); // Removed
    // unloadingBayStartTaskTimer.cancel(); // Removed

    // Background thread to monitor process completion
    Thread waitForStartCompletion = new Thread(() -> {
        try {
            while (!Unloading.isStartProcessCompletedUnloadingBay()) {
                Thread.sleep(200); // polling delay
            }

            // Once start is completed, update UI
            Platform.runLater(() -> {
                DashboardController.highlightGreenBay(DashboardController.getRef_unloadingBayContainer()); // Not applicable for this bay
                btnUnloadingStop.setDisable(false);
                btnUnloadingStop.setStyle(""); // Enabled - Default
                btnUnloadingReset.setDisable(false);
                btnUnloadingReset.setStyle(""); // Enabled - Default
                ApplicationLauncher.setCursor(Cursor.DEFAULT);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Unloading.logger.warn("Start-process wait thread interrupted", e);
            e.printStackTrace(); // Added for consistency
        }
    });

    waitForStartCompletion.setDaemon(true);
    waitForStartCompletion.start();

    Unloading.logger.info("btnUnloadingStartOnClick : Exit:");
}

@FXML
public void btnUnloadingStopOnClick() {
    Unloading.logger.info("btnUnloadingStopOnClick : Invoked:");
    
    Unloading.logger.info("UNLOADING BAY : STOPPED");
    ApplicationLauncher.logger.info("UNLOADING BAY : STOPPED");
    
    // F L A G S
    Unloading.abort_Unloading_Bay = true;
    Unloading.setStartProcessRequestedUnloadingBay(false);
    Unloading.setStopProcessCompletedUnloadingBay(false);
    Unloading.setStopProcessRequestedUnloadingBay(true);
    Unloading.setResetProcessCompletedUnloadingBay(false);
    Unloading.setResetProcessRequestedUnloadingBay(false);

    // B U T T O N I N T E R L O C K
    Platform.runLater(() -> {
        btnUnloadingStop.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
        btnUnloadingStop.setDisable(true);
        btnUnloadingStart.setDisable(true);
        btnUnloadingReset.setDisable(true);
        ApplicationLauncher.setCursor(Cursor.WAIT);
    });

    // L O G I C
    unloadingBayStopTaskTimer = new Timer();
    unloadingBayStopTaskTimer.schedule(new UnloadingBayStop(), 100);
    // Sleep(500); // Removed
    // unloadingBayStopTaskTimer.cancel(); // Removed

    // Wait for stop process to complete
    Thread waitForStopCompletion = new Thread(() -> {
        try {
            while (!Unloading.isStopProcessCompletedUnloadingBay()) {
                Thread.sleep(200);
            }

            Platform.runLater(() -> {
            	DashboardController.resetBayHighlight(DashboardController.getRef_unloadingBayContainer());
                btnUnloadingStart.setDisable(false);
                btnUnloadingStart.setStyle(""); // Enabled - Default
                btnUnloadingReset.setDisable(false);
                btnUnloadingReset.setStyle(""); // Enabled - Default
                ApplicationLauncher.setCursor(Cursor.DEFAULT);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Unloading.logger.warn("Stop-process wait thread interrupted", e);
            e.printStackTrace(); // Added for consistency
        }
    });

    waitForStopCompletion.setDaemon(true);
    waitForStopCompletion.start();

    Unloading.logger.info("btnUnloadingStopOnClick : Exit:");
}

@FXML
public void btnUnloadingResetOnClick() {
    Unloading.logger.info("btnUnloadingResetOnClick : Invoked:");
    
    Unloading.logger.info("UNLOADING BAY : RESET");
    ApplicationLauncher.logger.info("UNLOADING BAY : RESET");
    
    // F L A G S
    Unloading.setStartProcessRequestedUnloadingBay(false);
    Unloading.setStopProcessCompletedUnloadingBay(false);
    Unloading.setStopProcessRequestedUnloadingBay(false);
    Unloading.setResetProcessCompletedUnloadingBay(false);
    Unloading.setResetProcessRequestedUnloadingBay(true);

    // B U T T O N I N T E R L O C K
    Platform.runLater(() -> {
        btnUnloadingReset.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
        btnUnloadingReset.setDisable(true);
        btnUnloadingStart.setDisable(true);
        btnUnloadingStop.setDisable(true);
        ApplicationLauncher.setCursor(Cursor.WAIT);
    });

    // L O G I C
    unloadingBayResetTaskTimer = new Timer();
    unloadingBayResetTaskTimer.schedule(new UnloadingBayReset(), 100);
    // Sleep(500); // Removed
    // unloadingBayResetTaskTimer.cancel(); // Removed

    // Wait for reset process to complete
    Thread waitForResetCompletion = new Thread(() -> {
        try {
            while (!Unloading.isResetProcessCompletedUnloadingBay()) {
                Thread.sleep(200);
            }

            Platform.runLater(() -> {
                btnUnloadingStart.setDisable(false);
                btnUnloadingStart.setStyle(""); // Enabled - Default
                btnUnloadingStop.setDisable(false);
                btnUnloadingStop.setStyle(""); // Enabled - Default
                ApplicationLauncher.setCursor(Cursor.DEFAULT);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Unloading.logger.warn("Reset-process wait thread interrupted", e);
            e.printStackTrace(); // Added for consistency
        }
    });

    waitForResetCompletion.setDaemon(true);
    waitForResetCompletion.start();

    Unloading.logger.info("btnUnloadingResetOnClick : Exit:");
}

//============================================================================================================================================

public void Sleep(int timeInMsec) {
    try {
        Thread.sleep(timeInMsec);
    } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        ApplicationLauncher.logger.error("Sleep2 :InterruptedException:"+ e.getMessage());
    }
}

@Override
public void initialize(URL location, ResourceBundle resources) {
	// TODO Auto-generated method stub
	refInit();
	guiObjectDisable();
}

private void refInit() {
	// TODO Auto-generated method stub
	ref_btnVerificTestReset = btnVerificTestReset;
	ref_btnVerificTestStart = btnVerificTestStart;
	ref_btnVerificTestStop = btnVerificTestStop;
}

private void guiObjectDisable() {
	// TODO Auto-generated method stub
	Platform.runLater(()->{
		btnCommTestStart.setDisable(true);
		btnCommTestStop.setDisable(true);
		btnCommTestReset.setDisable(true);
	});
}

}
