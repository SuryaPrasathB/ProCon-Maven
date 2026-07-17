package com.tasnetwork.calibration.conveyor.bay.hv;

public class HV_ReadResult {
    private float value;  // Removed 'final' to allow setting values
    private boolean status;  // Removed 'final' to allow setting status

    // Constructor to initialize the object
    public HV_ReadResult(float value, boolean status) {
        this.value = value;
        this.status = status;
    }

    // Getter for value
    public float getValue() {
        return value;
    }

    // Setter for value
    public void setValue(float value) {
        this.value = value;
    }

    // Getter for status
    public boolean isStatus() {
        return status;
    }

    // Setter for status
    public void setStatus(boolean status) {
        this.status = status;
    }
}

