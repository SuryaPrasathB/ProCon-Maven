package com.tasnetwork.calibration.conveyor.pallet;

public class CalibrationResult {
    private String phaseResult = "Fail";
    private String neutralResult = "Fail";

    public void setPhaseResult(String result) {
        this.phaseResult = result;
    }

    public void setNeutralResult(String result) {
        this.neutralResult = result;
    }

    public String getPhaseResult() {
        return phaseResult;
    }

    public String getNeutralResult() {
        return neutralResult;
    }

    public String getOverallResult() {
        return (phaseResult.equals("Pass") && neutralResult.equals("Pass")) ? "Pass" : "Fail";
    }

    @Override
    public String toString() {
        return "{Phase=" + phaseResult + ", Neutral=" + neutralResult + "}";
    }
}

