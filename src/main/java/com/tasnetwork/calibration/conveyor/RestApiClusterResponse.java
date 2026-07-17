package com.tasnetwork.calibration.conveyor;

import org.json.simple.JSONObject;

public class RestApiClusterResponse {

	private String Message = "";
    private String statuscode = "";
    private String device = "";
    private String status = "";
    
    private String opGreen = "";
    private String opYellow = "";
    private String opRed = "";
    
    
    
    
	public String getMessage() {
		return Message;
	}
	public String getStatuscode() {
		return statuscode;
	}
	public String getDevice() {
		return device;
	}
	public String getStatus() {
		return status;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public void setStatuscode(String statuscode) {
		this.statuscode = statuscode;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOpGreen() {
		return opGreen;
	}
	public String getOpYellow() {
		return opYellow;
	}
	public String getOpRed() {
		return opRed;
	}
	public void setOpGreen(String opGreen) {
		this.opGreen = opGreen;
	}
	public void setOpYellow(String opYellow) {
		this.opYellow = opYellow;
	}
	public void setOpRed(String opRed) {
		this.opRed = opRed;
	}
	
}
