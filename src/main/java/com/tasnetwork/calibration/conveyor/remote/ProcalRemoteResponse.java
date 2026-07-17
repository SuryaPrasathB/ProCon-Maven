package com.tasnetwork.calibration.conveyor.remote;

import org.json.JSONObject;

public class ProcalRemoteResponse {
	private String message;
	
	private TestPointStatus testPointStatus = new TestPointStatus();
	
	private boolean dutCalibrationVoltageTargetSet   = false;
    private boolean dutCalibrationCurrentTargetSet   = false; 
	private boolean dutCalibrationCurrentZeroSet     = false; 
	private boolean dutCalibrationVoltCurrentSetZero = false;

	JSONObject result = new JSONObject();
	
	public ProcalRemoteResponse (){
		 
	}
    public ProcalRemoteResponse (String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    
    public void setMessage(String msg) {
        message = msg;
    }
	public TestPointStatus getTestPointStatus() {
		return testPointStatus;
	}
	public void setTestPointStatus(TestPointStatus testPointStatus) {
		this.testPointStatus = testPointStatus;
	}
	public boolean isDutCalibrationVoltageTargetSet() {
		return dutCalibrationVoltageTargetSet;
	}
	public void setDutCalibrationVoltageTargetSet(boolean dutCalibrationVoltageTargetSet) {
		this.dutCalibrationVoltageTargetSet = dutCalibrationVoltageTargetSet;
	}
	public boolean isDutCalibrationCurrentTargetSet() {
		return dutCalibrationCurrentTargetSet;
	}
	public void setDutCalibrationCurrentTargetSet(boolean dutCalibrationCurrentTargetSet) {
		this.dutCalibrationCurrentTargetSet = dutCalibrationCurrentTargetSet;
	}
	public boolean isDutCalibrationCurrentZeroSet() {
		return dutCalibrationCurrentZeroSet;
	}
	public void setDutCalibrationCurrentZeroSet(boolean dutCalibrationCurrentZeroSet) {
		this.dutCalibrationCurrentZeroSet = dutCalibrationCurrentZeroSet;
	}
	public boolean isDutCalibrationVoltCurrentSetZero() {
		return dutCalibrationVoltCurrentSetZero;
	}
	public void setDutCalibrationVoltCurrentSetZero(boolean dutCalibrationVoltCurrentSetZero) {
		this.dutCalibrationVoltCurrentSetZero = dutCalibrationVoltCurrentSetZero;
	}
	public JSONObject getResult() {
		return result;
	}
	public void setResult(JSONObject result) {
		this.result = result;
	}

}
