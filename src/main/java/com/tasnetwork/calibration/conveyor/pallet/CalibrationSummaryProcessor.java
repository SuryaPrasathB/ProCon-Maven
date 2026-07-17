package com.tasnetwork.calibration.conveyor.pallet;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.bay.calib.Calib;

public class CalibrationSummaryProcessor {
    private static final CalibrationSummaryProcessor instance = new CalibrationSummaryProcessor();
    private final Map<Integer, CalibrationResult> calibrationResults = new HashMap<>();
    private final Map<Integer, String> overallResults = new HashMap<>();

    private CalibrationSummaryProcessor() {}
    
    public static CalibrationSummaryProcessor getInstance() {
        return instance;
    }
    
    // Store Phase Calibration Result
    public synchronized void addPhaseCalibrationResult(int positionNo, String result) {
        calibrationResults.putIfAbsent(positionNo, new CalibrationResult());
        calibrationResults.get(positionNo).setPhaseResult(result);

        Calib.logger.debug("Phase Calibration Added for Meter " + positionNo + ": " + result);
        logCalibrationResults();
    }

    // Store Neutral Calibration Result
    public synchronized void addNeutralCalibrationResult(int positionNo, String result) {
        calibrationResults.putIfAbsent(positionNo, new CalibrationResult());
        calibrationResults.get(positionNo).setNeutralResult(result);

        Calib.logger.debug("Neutral Calibration Added for Meter " + positionNo + ": " + result);
        logCalibrationResults();
    }

    // Compute Overall Calibration Results
    public synchronized void processOverallCalibration() {
        Calib.logger.debug("Processing Overall Calibration...");
        logCalibrationResults();

        for (Map.Entry<Integer, CalibrationResult> entry : calibrationResults.entrySet()) {
            int positionNo = entry.getKey();
            CalibrationResult result = entry.getValue();

            String overallResult = result.getOverallResult();
            Calib.logger.debug("Processing Overall Calibration: " + positionNo + " -> overallResult:"+ overallResult);
            overallResults.put(positionNo, overallResult);

            Calib.logger.debug("Meter " + positionNo + " - " + result + ", Overall Calibration: " + overallResults);
        }
    }

    // Retrieve Overall Calibration Result for a Specific Meter
    public synchronized String getOverallCalibrationResult(int positionNo) {
        return overallResults.getOrDefault(positionNo, "Pass");
    }

    // Helper Method to Log Calibration Results
    private void logCalibrationResults() {
        Calib.logger.debug("Current Calibration Results: " + calibrationResults);
    }
}
