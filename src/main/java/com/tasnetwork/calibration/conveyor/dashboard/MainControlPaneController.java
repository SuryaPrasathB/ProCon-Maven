package com.tasnetwork.calibration.conveyor.dashboard;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;

import com.tasnetwork.calibration.conveyor.bay.verific.Verification;
import com.tasnetwork.calibration.conveyor.bay.verific.VerificationTestBayStop;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.WindowManager;

import javafx.application.Platform; // Added this import
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;

public class MainControlPaneController
        implements com.tasnetwork.calibration.conveyor.dashboard.IBayUIController, Initializable {

    private static volatile MainControlPaneController instance;

    public static MainControlPaneController getInstance() {
        return instance;
    }

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
    @FXML
    private Button btnRejectStart;
    @FXML
    private Button btnRejectStop;
    @FXML
    private Button btnRejectReset;
    @FXML
    private Button btnUnloadingStart;
    @FXML
    private Button btnUnloadingStop;
    @FXML
    private Button btnUnloadingReset;

    // ============================================================================================================================================

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
    Timer verificStopTaskTimer;
    Timer commStopTaskTimer;
    Timer sctNlt2StopTaskTimer;
    Timer sctNlt1StopTaskTimer;
    Timer waitingBayStopTaskTimer;
    Timer rejectionBayStopTaskTimer;
    Timer unloadingBayStopTaskTimer;
    Timer funtionalBayResetTaskTimer;
    Timer calibrationResetTaskTimer;
    Timer insResResetTaskTimer;
    Timer hvtBayResetTaskTimer;
    Timer verificResetTaskTimer;
    Timer commResetTaskTimer;
    Timer sctNlt1ResetTaskTimer;
    Timer sctNlt2ResetTaskTimer;
    Timer waitingBayResetTaskTimer;
    Timer rejectionBayResetTaskTimer;
    Timer unloadingBayResetTaskTimer;

    // ============================================================================================================================================

    @FXML
    public void btnRjStartOnClick() {
        BayControlsManager.getInstance().handleStart(ConstantConveyor.REJECTION_BAY_KEY);
    }

    @FXML
    public void btnRjStopOnClick() {
        BayControlsManager.getInstance().handleStop(ConstantConveyor.REJECTION_BAY_KEY);
    }

    @FXML
    public void btnRjResetOnClick() {
        BayControlsManager.getInstance().handleReset(ConstantConveyor.REJECTION_BAY_KEY);
    }

    // ============================================================================================================================================

    @FXML
    public void btnFtStartOnClick() {
        BayControlsManager.getInstance().handleStart(ConstantConveyor.FT_BAY_KEY);
    }

    @FXML
    public void btnFtStopOnClick() {
        BayControlsManager.getInstance().handleStop(ConstantConveyor.FT_BAY_KEY);
    }

    @FXML
    public void btnFtResetOnClick() {
        BayControlsManager.getInstance().handleReset(ConstantConveyor.FT_BAY_KEY);
    }

    // ============================================================================================================================================

    @FXML
    public void btnHvtStartOnClick() {
        BayControlsManager.getInstance().handleStart(ConstantConveyor.HV_BAY_KEY);
    }

    @FXML
    public void btnHvtStopOnClick() {
        BayControlsManager.getInstance().handleStop(ConstantConveyor.HV_BAY_KEY);
    }

    @FXML
    public void btnHvtResetOnClick() {
        BayControlsManager.getInstance().handleReset(ConstantConveyor.HV_BAY_KEY);
    }

    // ============================================================================================================================================

    @FXML
    public void btnIrtStartOnClick() {
        BayControlsManager.getInstance().handleStart(ConstantConveyor.IR_BAY_KEY);
    }

    @FXML
    public void btnIrtStopOnClick() {
        BayControlsManager.getInstance().handleStop(ConstantConveyor.IR_BAY_KEY);
    }

    @FXML
    public void btnIrtResetOnClick() {
        BayControlsManager.getInstance().handleReset(ConstantConveyor.IR_BAY_KEY);
    }

    // ============================================================================================================================================

    @FXML
    public void btnCalibStartOnClick() {
        BayControlsManager.getInstance().handleStart(ConstantConveyor.CALIBRATION_BAY_KEY);
    }

    @FXML
    public void btnCalibStopOnClick() {
        BayControlsManager.getInstance().handleStop(ConstantConveyor.CALIBRATION_BAY_KEY);
    }

    @FXML
    public void btnCalibResetOnClick() {
        BayControlsManager.getInstance().handleReset(ConstantConveyor.CALIBRATION_BAY_KEY);
    }

    // ====================================================================================

    @FXML
    public void btnWaitingBayStartOnClick() {
        BayControlsManager.getInstance().handleStart(ConstantConveyor.WAITING_BAY_KEY);
    }

    @FXML
    public void btnWaitingBayStopOnClick() {
        BayControlsManager.getInstance().handleStop(ConstantConveyor.WAITING_BAY_KEY);
    }

    @FXML
    public void btnWaitingBayResetOnClick() {
        BayControlsManager.getInstance().handleReset(ConstantConveyor.WAITING_BAY_KEY);
    }

    // ============================================================================================================================================

    @FXML
    public void btnVerificTestStartOnClick() {
        BayControlsManager.getInstance().handleStart(ConstantConveyor.VERIFICATION_BAY_KEY);
    }

    @FXML
    public void btnVerificTestStopOnClick() {
        BayControlsManager.getInstance().handleStop(ConstantConveyor.VERIFICATION_BAY_KEY);
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
            WindowManager.setCursor(Cursor.WAIT);
        });

        // L O G I C
        verificStopTaskTimer = new Timer();
        verificStopTaskTimer.schedule(new VerificationTestBayStop(), 100);

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
                    ConveyorDataManager.getDashboardObject().getBayIndicatorManager()
                            .stopTimeUpDisplay(ConstantConveyor.VERIFICATION_PP1_BAY_KEY);
                    ConveyorDataManager.getDashboardObject().getBayIndicatorManager()
                            .stopProgressBarWithTpCount(ConstantConveyor.VERIFICATION_PP1_BAY_KEY);

                    ref_btnVerificTestStart.setDisable(false);
                    ref_btnVerificTestStart.setStyle(""); // Enabled - Default
                    ref_btnVerificTestReset.setDisable(false);
                    ref_btnVerificTestReset.setStyle(""); // Enabled - Default
                    WindowManager.setCursor(Cursor.DEFAULT);
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
        BayControlsManager.getInstance().handleReset(ConstantConveyor.VERIFICATION_BAY_KEY);
    }

    // ============================================================================================================================================

    @FXML
    public void btnSctNlt1StartOnClick() {
        BayControlsManager.getInstance().handleStart(ConstantConveyor.STA_NLD1_BAY_KEY);
    }

    @FXML
    public void btnSctNlt1StopOnClick() {
        BayControlsManager.getInstance().handleStop(ConstantConveyor.STA_NLD1_BAY_KEY);
    }

    @FXML
    public void btnSctNlt1ResetOnClick() {
        BayControlsManager.getInstance().handleReset(ConstantConveyor.STA_NLD1_BAY_KEY);
    }

    // ============================================================================================================================================

    @FXML
    public void btnSctNlt2StartOnClick() {
        BayControlsManager.getInstance().handleStart(ConstantConveyor.STA_NLD2_BAY_KEY);
    }

    @FXML
    public void btnSctNlt2StopOnClick() {
        BayControlsManager.getInstance().handleStop(ConstantConveyor.STA_NLD2_BAY_KEY);
    }

    @FXML
    public void btnSctNlt2ResetOnClick() {
        BayControlsManager.getInstance().handleReset(ConstantConveyor.STA_NLD2_BAY_KEY);
    }

    // ============================================================================================================================================

    @FXML
    public void btnCommTestStartOnClick() {
        BayControlsManager.getInstance().handleStart(ConstantConveyor.COMMUNICATION_BAY_KEY);
    }

    @FXML
    public void btnCommTestStopOnClick() {
        BayControlsManager.getInstance().handleStop(ConstantConveyor.COMMUNICATION_BAY_KEY);
    }

    @FXML
    public void btnCommTestResetOnClick() {
        BayControlsManager.getInstance().handleReset(ConstantConveyor.COMMUNICATION_BAY_KEY);
    }

    // ============================================================================================================================================

    @FXML
    public void btnUnloadingStartOnClick() {
        BayControlsManager.getInstance().handleStart(ConstantConveyor.UNLOADING_BAY_KEY);
    }

    @FXML
    public void btnUnloadingStopOnClick() {
        BayControlsManager.getInstance().handleStop(ConstantConveyor.UNLOADING_BAY_KEY);
    }

    @FXML
    public void btnUnloadingResetOnClick() {
        BayControlsManager.getInstance().handleReset(ConstantConveyor.UNLOADING_BAY_KEY);
    }

    // ============================================================================================================================================

    public void Sleep(int timeInMsec) {
        try {
            Thread.sleep(timeInMsec);
        } catch (InterruptedException e) {

            e.printStackTrace();
            ApplicationLauncher.logger.error("Sleep :InterruptedException:" + e.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BayControlsManager.getInstance().registerController(this);
        instance = this;
        refInit();
        guiObjectDisable();
    }

    private void refInit() {

        ref_btnVerificTestReset = btnVerificTestReset;
        ref_btnVerificTestStart = btnVerificTestStart;
        ref_btnVerificTestStop = btnVerificTestStop;
    }

    private void guiObjectDisable() {

        Platform.runLater(() -> {
            btnCommTestStart.setDisable(true);
            btnCommTestStop.setDisable(true);
            btnCommTestReset.setDisable(true);
        });
    }

    @Override
    public void updateBayUI(String bayKey, boolean isRunning) {
        javafx.application.Platform.runLater(() -> {
            switch (bayKey) {
                case ConstantConveyor.FT_BAY_KEY:
                    if (btnFtStart != null) {
                        if (isRunning) {
                            btnFtStart.setStyle("-fx-background-color: #FF5733;");
                            btnFtStart.setDisable(true);
                            if (btnFtStop != null) {
                                btnFtStop.setStyle("");
                                btnFtStop.setDisable(false);
                            }
                            if (btnFtReset != null) {
                                btnFtReset.setStyle("-fx-background-color: #FF5733;");
                                btnFtReset.setDisable(true);
                            }
                        } else {
                            btnFtStart.setStyle("");
                            btnFtStart.setDisable(false);
                            if (btnFtStop != null) {
                                btnFtStop.setStyle("-fx-background-color: #FF5733;");
                                btnFtStop.setDisable(true);
                            }
                            if (btnFtReset != null) {
                                btnFtReset.setStyle("");
                                btnFtReset.setDisable(false);
                            }
                        }
                    }
                    break;
                case ConstantConveyor.HV_BAY_KEY:
                    if (btnHvtStart != null) {
                        if (isRunning) {
                            btnHvtStart.setStyle("-fx-background-color: #FF5733;");
                            btnHvtStart.setDisable(true);
                            if (btnHvtStop != null) {
                                btnHvtStop.setStyle("");
                                btnHvtStop.setDisable(false);
                            }
                            if (btnHvtReset != null) {
                                btnHvtReset.setStyle("-fx-background-color: #FF5733;");
                                btnHvtReset.setDisable(true);
                            }
                        } else {
                            btnHvtStart.setStyle("");
                            btnHvtStart.setDisable(false);
                            if (btnHvtStop != null) {
                                btnHvtStop.setStyle("-fx-background-color: #FF5733;");
                                btnHvtStop.setDisable(true);
                            }
                            if (btnHvtReset != null) {
                                btnHvtReset.setStyle("");
                                btnHvtReset.setDisable(false);
                            }
                        }
                    }
                    break;
                case ConstantConveyor.IR_BAY_KEY:
                    if (btnIrtStart != null) {
                        if (isRunning) {
                            btnIrtStart.setStyle("-fx-background-color: #FF5733;");
                            btnIrtStart.setDisable(true);
                            if (btnIrtStop != null) {
                                btnIrtStop.setStyle("");
                                btnIrtStop.setDisable(false);
                            }
                            if (btnIrtReset != null) {
                                btnIrtReset.setStyle("-fx-background-color: #FF5733;");
                                btnIrtReset.setDisable(true);
                            }
                        } else {
                            btnIrtStart.setStyle("");
                            btnIrtStart.setDisable(false);
                            if (btnIrtStop != null) {
                                btnIrtStop.setStyle("-fx-background-color: #FF5733;");
                                btnIrtStop.setDisable(true);
                            }
                            if (btnIrtReset != null) {
                                btnIrtReset.setStyle("");
                                btnIrtReset.setDisable(false);
                            }
                        }
                    }
                    break;
                case ConstantConveyor.CALIBRATION_BAY_KEY:
                    if (btnCalibStart != null) {
                        if (isRunning) {
                            btnCalibStart.setStyle("-fx-background-color: #FF5733;");
                            btnCalibStart.setDisable(true);
                            if (btnCalibStop != null) {
                                btnCalibStop.setStyle("");
                                btnCalibStop.setDisable(false);
                            }
                            if (btnCalibReset != null) {
                                btnCalibReset.setStyle("-fx-background-color: #FF5733;");
                                btnCalibReset.setDisable(true);
                            }
                        } else {
                            btnCalibStart.setStyle("");
                            btnCalibStart.setDisable(false);
                            if (btnCalibStop != null) {
                                btnCalibStop.setStyle("-fx-background-color: #FF5733;");
                                btnCalibStop.setDisable(true);
                            }
                            if (btnCalibReset != null) {
                                btnCalibReset.setStyle("");
                                btnCalibReset.setDisable(false);
                            }
                        }
                    }
                    break;
                case ConstantConveyor.WAITING_BAY_KEY:
                    if (btnWaitingBayStart != null) {
                        if (isRunning) {
                            btnWaitingBayStart.setStyle("-fx-background-color: #FF5733;");
                            btnWaitingBayStart.setDisable(true);
                            if (btnWaitingBayStop != null) {
                                btnWaitingBayStop.setStyle("");
                                btnWaitingBayStop.setDisable(false);
                            }
                            if (btnWaitingBayReset != null) {
                                btnWaitingBayReset.setStyle("-fx-background-color: #FF5733;");
                                btnWaitingBayReset.setDisable(true);
                            }
                        } else {
                            btnWaitingBayStart.setStyle("");
                            btnWaitingBayStart.setDisable(false);
                            if (btnWaitingBayStop != null) {
                                btnWaitingBayStop.setStyle("-fx-background-color: #FF5733;");
                                btnWaitingBayStop.setDisable(true);
                            }
                            if (btnWaitingBayReset != null) {
                                btnWaitingBayReset.setStyle("");
                                btnWaitingBayReset.setDisable(false);
                            }
                        }
                    }
                    break;
                case ConstantConveyor.VERIFICATION_BAY_KEY:
                    if (btnVerificTestStart != null) {
                        if (isRunning) {
                            btnVerificTestStart.setStyle("-fx-background-color: #FF5733;");
                            btnVerificTestStart.setDisable(true);
                            if (btnVerificTestStop != null) {
                                btnVerificTestStop.setStyle("");
                                btnVerificTestStop.setDisable(false);
                            }
                            if (btnVerificTestReset != null) {
                                btnVerificTestReset.setStyle("-fx-background-color: #FF5733;");
                                btnVerificTestReset.setDisable(true);
                            }
                        } else {
                            btnVerificTestStart.setStyle("");
                            btnVerificTestStart.setDisable(false);
                            if (btnVerificTestStop != null) {
                                btnVerificTestStop.setStyle("-fx-background-color: #FF5733;");
                                btnVerificTestStop.setDisable(true);
                            }
                            if (btnVerificTestReset != null) {
                                btnVerificTestReset.setStyle("");
                                btnVerificTestReset.setDisable(false);
                            }
                        }
                    }
                    break;
                case ConstantConveyor.STA_NLD1_BAY_KEY:
                    if (btnSctNlt1Start != null) {
                        if (isRunning) {
                            btnSctNlt1Start.setStyle("-fx-background-color: #FF5733;");
                            btnSctNlt1Start.setDisable(true);
                            if (btnSctNlt1Stop != null) {
                                btnSctNlt1Stop.setStyle("");
                                btnSctNlt1Stop.setDisable(false);
                            }
                            if (btnSctNlt1Reset != null) {
                                btnSctNlt1Reset.setStyle("-fx-background-color: #FF5733;");
                                btnSctNlt1Reset.setDisable(true);
                            }
                        } else {
                            btnSctNlt1Start.setStyle("");
                            btnSctNlt1Start.setDisable(false);
                            if (btnSctNlt1Stop != null) {
                                btnSctNlt1Stop.setStyle("-fx-background-color: #FF5733;");
                                btnSctNlt1Stop.setDisable(true);
                            }
                            if (btnSctNlt1Reset != null) {
                                btnSctNlt1Reset.setStyle("");
                                btnSctNlt1Reset.setDisable(false);
                            }
                        }
                    }
                    break;
                case ConstantConveyor.STA_NLD2_BAY_KEY:
                    if (btnSctNlt2Start != null) {
                        if (isRunning) {
                            btnSctNlt2Start.setStyle("-fx-background-color: #FF5733;");
                            btnSctNlt2Start.setDisable(true);
                            if (btnSctNlt2Stop != null) {
                                btnSctNlt2Stop.setStyle("");
                                btnSctNlt2Stop.setDisable(false);
                            }
                            if (btnSctNlt2Reset != null) {
                                btnSctNlt2Reset.setStyle("-fx-background-color: #FF5733;");
                                btnSctNlt2Reset.setDisable(true);
                            }
                        } else {
                            btnSctNlt2Start.setStyle("");
                            btnSctNlt2Start.setDisable(false);
                            if (btnSctNlt2Stop != null) {
                                btnSctNlt2Stop.setStyle("-fx-background-color: #FF5733;");
                                btnSctNlt2Stop.setDisable(true);
                            }
                            if (btnSctNlt2Reset != null) {
                                btnSctNlt2Reset.setStyle("");
                                btnSctNlt2Reset.setDisable(false);
                            }
                        }
                    }
                    break;
                case ConstantConveyor.COMMUNICATION_BAY_KEY:
                    if (btnCommTestStart != null) {
                        if (isRunning) {
                            btnCommTestStart.setStyle("-fx-background-color: #FF5733;");
                            btnCommTestStart.setDisable(true);
                            if (btnCommTestStop != null) {
                                btnCommTestStop.setStyle("");
                                btnCommTestStop.setDisable(false);
                            }
                            if (btnCommTestReset != null) {
                                btnCommTestReset.setStyle("-fx-background-color: #FF5733;");
                                btnCommTestReset.setDisable(true);
                            }
                        } else {
                            btnCommTestStart.setStyle("");
                            btnCommTestStart.setDisable(false);
                            if (btnCommTestStop != null) {
                                btnCommTestStop.setStyle("-fx-background-color: #FF5733;");
                                btnCommTestStop.setDisable(true);
                            }
                            if (btnCommTestReset != null) {
                                btnCommTestReset.setStyle("");
                                btnCommTestReset.setDisable(false);
                            }
                        }
                    }
                    break;
                case ConstantConveyor.REJECTION_BAY_KEY:
                    if (btnRejectStart != null) {
                        if (isRunning) {
                            btnRejectStart.setStyle("-fx-background-color: #FF5733;");
                            btnRejectStart.setDisable(true);
                            if (btnRejectStop != null) {
                                btnRejectStop.setStyle("");
                                btnRejectStop.setDisable(false);
                            }
                            if (btnRejectReset != null) {
                                btnRejectReset.setStyle("-fx-background-color: #FF5733;");
                                btnRejectReset.setDisable(true);
                            }
                        } else {
                            btnRejectStart.setStyle("");
                            btnRejectStart.setDisable(false);
                            if (btnRejectStop != null) {
                                btnRejectStop.setStyle("-fx-background-color: #FF5733;");
                                btnRejectStop.setDisable(true);
                            }
                            if (btnRejectReset != null) {
                                btnRejectReset.setStyle("");
                                btnRejectReset.setDisable(false);
                            }
                        }
                    }
                    break;
                case ConstantConveyor.UNLOADING_BAY_KEY:
                    if (btnUnloadingStart != null) {
                        if (isRunning) {
                            btnUnloadingStart.setStyle("-fx-background-color: #FF5733;");
                            btnUnloadingStart.setDisable(true);
                            if (btnUnloadingStop != null) {
                                btnUnloadingStop.setStyle("");
                                btnUnloadingStop.setDisable(false);
                            }
                            if (btnUnloadingReset != null) {
                                btnUnloadingReset.setStyle("-fx-background-color: #FF5733;");
                                btnUnloadingReset.setDisable(true);
                            }
                        } else {
                            btnUnloadingStart.setStyle("");
                            btnUnloadingStart.setDisable(false);
                            if (btnUnloadingStop != null) {
                                btnUnloadingStop.setStyle("-fx-background-color: #FF5733;");
                                btnUnloadingStop.setDisable(true);
                            }
                            if (btnUnloadingReset != null) {
                                btnUnloadingReset.setStyle("");
                                btnUnloadingReset.setDisable(false);
                            }
                        }
                    }
                    break;
            }
        });
    }
}