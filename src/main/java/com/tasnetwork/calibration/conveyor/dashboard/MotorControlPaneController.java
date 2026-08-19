package com.tasnetwork.calibration.conveyor.dashboard;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Timer;

import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.bay.ft.Ft;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class MotorControlPaneController implements Initializable {

    private BayUtils bayUtils = new BayUtils();

    private static final boolean ON = true;
    private static final boolean OFF = false;

    // Button mappings: motor index -> on/off buttons
    private final Map<Integer, Button> motorOnButtons = new HashMap<>();
    private final Map<Integer, Button> motorOffButtons = new HashMap<>();
    private final Map<Integer, String> motorPortMappings = new HashMap<>();

    // Inject all buttons
    @FXML
    private Button btn_onMotor1, btn_offMotor1;
    @FXML
    private Button btn_onMotor2, btn_offMotor2;
    @FXML
    private Button btn_onMotor3, btn_offMotor3;
    @FXML
    private Button btn_onMotor4, btn_offMotor4;
    @FXML
    private Button btn_onMotor5, btn_offMotor5;
    @FXML
    private Button btn_onMotor6, btn_offMotor6;
    @FXML
    private Button btn_onMotor7, btn_offMotor7;
    @FXML
    private Button btn_onMotor8, btn_offMotor8;
    @FXML
    private Button btn_onMotor9, btn_offMotor9;

    Timer motorRefreshTimer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Map buttons and port keys
        setupMotorMappings();
        refreshMotorStatus();

        // Set initial button states and action handlers
        for (int i = 1; i <= 9; i++) {
            toggleButtons(i, false); // Motor OFF by default on UI
            int motorIndex = i;
            motorOnButtons.get(i).setOnAction(e -> handleMotorToggle(motorIndex, ON));
            motorOffButtons.get(i).setOnAction(e -> handleMotorToggle(motorIndex, OFF));
        }
    }

    private void setupMotorMappings() {
        // Button mapping
        motorOnButtons.put(1, btn_onMotor1);
        motorOffButtons.put(1, btn_offMotor1);
        motorOnButtons.put(2, btn_onMotor2);
        motorOffButtons.put(2, btn_offMotor2);
        motorOnButtons.put(3, btn_onMotor3);
        motorOffButtons.put(3, btn_offMotor3);
        motorOnButtons.put(4, btn_onMotor4);
        motorOffButtons.put(4, btn_offMotor4);
        motorOnButtons.put(5, btn_onMotor5);
        motorOffButtons.put(5, btn_offMotor5);
        motorOnButtons.put(6, btn_onMotor6);
        motorOffButtons.put(6, btn_offMotor6);
        motorOnButtons.put(7, btn_onMotor7);
        motorOffButtons.put(7, btn_offMotor7);
        motorOnButtons.put(8, btn_onMotor8);
        motorOffButtons.put(8, btn_offMotor8);
        motorOnButtons.put(9, btn_onMotor9);
        motorOffButtons.put(9, btn_offMotor9);

        // Port mapping
        motorPortMappings.put(1, ConstantBayPortNameMapping.MOTOR_1_CONTROL);
        motorPortMappings.put(2, ConstantBayPortNameMapping.MOTOR_2_CONTROL);
        motorPortMappings.put(3, ConstantBayPortNameMapping.MOTOR_3_CONTROL);
        motorPortMappings.put(4, ConstantBayPortNameMapping.MOTOR_4_CONTROL);
        motorPortMappings.put(5, ConstantBayPortNameMapping.MOTOR_5_CONTROL);
        motorPortMappings.put(6, ConstantBayPortNameMapping.MOTOR_6_CONTROL);
        motorPortMappings.put(7, ConstantBayPortNameMapping.MOTOR_7_CONTROL);
        motorPortMappings.put(8, ConstantBayPortNameMapping.MOTOR_8_CONTROL);
        motorPortMappings.put(9, ConstantBayPortNameMapping.MOTOR_9_CONTROL);
    }

    private void handleMotorToggle(int motorIndex, boolean turnOn) {
        String portName = motorPortMappings.get(motorIndex);

        /*
         * // Disable both buttons immediately to prevent multiple clicks
         * Platform.runLater(() -> {
         * motorOnButtons.get(motorIndex).setDisable(true);
         * motorOffButtons.get(motorIndex).setDisable(true);
         * });
         */

        new Thread(() -> {
            boolean success = controlOutput(portName, turnOn);

            if (!turnOn) {
                clearMotorRequirementsForBays(motorIndex);
            }

            Platform.runLater(() -> {
                if (success) {
                    toggleButtons(motorIndex, turnOn); // Update UI based on successful operation
                } else {
                    // If control failed, revert button state to previous or indicate error
                    // For now, re-enable both buttons to allow retry, or show an error message
                    Ft.logger.error("Failed to control motor " + motorIndex + " on port " + portName);
                    toggleButtons(motorIndex, !turnOn); // Revert to previous state if failed
                }
            });

        }).start();
    }

    /**
     * Controls the output to a specific port. This method is now called from a
     * background thread.
     *
     * @param portNameKey The key for the port name mapping.
     * @param shouldClose True to turn the motor ON (close the circuit), False to
     *                    turn it OFF (open the circuit).
     * @return true if the control operation was successful, false otherwise.
     */
    private boolean controlOutput(String portNameKey, boolean shouldClose) {
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(portNameKey);
        if (portInfo != null) {
            Ft.logger.debug("Attempting to control motor on PortId: " + portInfo.getPortId() +
                    ", ClusterId: " + portInfo.getClusterId() +
                    ", BayId: " + portInfo.getBayId() +
                    ", Action: " + (shouldClose ? "ON" : "OFF"));

            String action = shouldClose
                    ? Constant_IO_ActionMapping.ON
                    : Constant_IO_ActionMapping.OFF;

            try {
                if (ProcalFeatureEnable.MODBUS_PLC_SLAVE_MODE) {
                    bayUtils.setOutputDataToPlcBay(
                            portInfo.getClusterId(),
                            portInfo.getBayId(),
                            portInfo.getPortId(),
                            action);
                } else {
                    bayUtils.setOutputDataToBay(
                            portInfo.getClusterId(),
                            portInfo.getBayId(),
                            portInfo.getPortId(),
                            action);
                }
                Ft.logger.debug("Motor control successful for PortId: " + portInfo.getPortId());
                return true; // Operation successful
            } catch (Exception e) {
                Ft.logger.error("Error controlling motor on PortId: " + portInfo.getPortId(), e);
                return false; // Operation failed
            }
        } else {
            Ft.logger.error("PortInfo not found for portNameKey: " + portNameKey);
            return false; // Port info not found
        }
    }

    private void toggleButtons(int motorIndex, boolean motorOn) {
        // This method is now explicitly called via Platform.runLater() from background
        // thread
        // to ensure UI updates happen on the JavaFX Application Thread.
        // motorOnButtons.get(motorIndex).setDisable(motorOn);
        // motorOffButtons.get(motorIndex).setDisable(!motorOn);
    }

    // Refresh motor status and update buttons
    @FXML
    void refreshMotorStatus() {

        new Thread(() -> {
            for (int i = 1; i <= 9; i++) {
                String portKey = motorPortMappings.get(i);
                IoPortInfo ioPortInfo = BayUtils.getOutputPortDetails(portKey);

                if (ioPortInfo == null) {
                    ApplicationLauncher.logger.warn("No input port found for motor " + i);
                    continue;
                }

                String state;
                try {
                    state = bayUtils.getInputDataFromBayV2(ioPortInfo);

                    // Translate the state
                    if (state.equals(Constant_IO_ActionMapping.ON)) {
                        state = Constant_IO_ActionMapping.OPEN;
                    } else {
                        state = Constant_IO_ActionMapping.CLOSE;
                    }

                    boolean isMotorOn = state.equals(Constant_IO_ActionMapping.OPEN);
                    int motorIndex = i;
                    final boolean finalIsMotorOn = isMotorOn;

                    Platform.runLater(() -> toggleButtons(motorIndex, finalIsMotorOn));

                    ApplicationLauncher.logger.debug("Motor" + i + " status = " + (finalIsMotorOn ? "ON" : "OFF"));
                } catch (Exception e) {
                    ApplicationLauncher.logger.debug("Error reading motor " + i + " status.", e);
                }
            }
        }).start();
    }

    public void Sleep(int timeInMsec) {

        try {
            Thread.sleep(timeInMsec);
        } catch (InterruptedException e) {
            e.printStackTrace();
            ApplicationLauncher.logger.error("Sleep2 :InterruptedException:" + e.getMessage());
        }

    }

    public BayUtils getBayUtils() {
        return bayUtils;
    }

    public void setBayUtils(BayUtils bayUtils) {
        this.bayUtils = bayUtils;
    }

    private void clearMotorRequirementsForBays(int motorIndex) {
        switch (motorIndex) {
            case 1:
                bayUtils.set_motor_not_required(ConstantConveyor.FT_BAY_KEY);
                bayUtils.set_motor_not_required(ConstantConveyor.REJECTION_BAY_KEY);
                break;
            case 2:
                bayUtils.set_motor_not_required(ConstantConveyor.FT_BAY_KEY);
                bayUtils.set_motor_not_required(ConstantConveyor.HV_BAY_KEY);
                bayUtils.set_motor_not_required(ConstantConveyor.IR_BAY_KEY);
                bayUtils.set_motor_not_required(ConstantConveyor.CALIBRATION_BAY_KEY);
                bayUtils.set_motor_not_required(ConstantConveyor.WAITING_BAY_KEY);
                break;
            case 3:
                bayUtils.set_motor_not_required(ConstantConveyor.WAITING_BAY_KEY);
                bayUtils.set_motor_not_required(ConstantConveyor.VERIFICATION_BAY_KEY);
                break;
            case 4:
                bayUtils.set_motor_not_required(ConstantConveyor.VERIFICATION_BAY_KEY);
                bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD1_BAY_KEY);
                bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD2_BAY_KEY);
                break;
            case 5:
                bayUtils.set_motor_not_required(ConstantConveyor.VERIFICATION_BAY_KEY);
                bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD1_BAY_KEY);
                break;
            case 6:
                bayUtils.set_motor_not_required(ConstantConveyor.VERIFICATION_BAY_KEY);
                bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD1_BAY_KEY);
                bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD2_BAY_KEY);
                break;
            case 7:
                bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD1_BAY_KEY);
                break;
            case 8:
                bayUtils.set_motor_not_required(ConstantConveyor.VERIFICATION_BAY_KEY);
                bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD1_BAY_KEY);
                bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD2_BAY_KEY);
                bayUtils.set_motor_not_required(ConstantConveyor.COMMUNICATION_BAY_KEY);
                break;
            case 9:
                bayUtils.set_motor_not_required(ConstantConveyor.UNLOADING_BAY_KEY);
                bayUtils.set_motor_not_required(ConstantConveyor.LOADING_BAY_KEY);
                break;
            default:
                break;
        }
    }
}
