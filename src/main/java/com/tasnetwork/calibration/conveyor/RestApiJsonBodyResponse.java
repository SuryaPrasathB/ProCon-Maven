package com.tasnetwork.calibration.conveyor;

import org.json.simple.JSONObject;

public class RestApiJsonBodyResponse {

	//private String Message = "";
    private String statusCode = "";
    private String testStatus = "";
    private String status = "";
    
    private JSONObject jsonBodyResponse = new JSONObject();
    
    
	public JSONObject getJsonBodyResponse() {
		return jsonBodyResponse;
	}
	public void setJsonBodyResponse(JSONObject jsonBodyResponse) {
		this.jsonBodyResponse = jsonBodyResponse;
	}
   /* private String device = "";
    private String status = "";
    
    private String opGreen = "";
    private String opYellow = "";
    private String opRed = "";
    */
    
    
    
	/*public String getMessage() {
		return Message;
	}*/
	public String getStatusCode() {
		return statusCode;
	}
/*	public String getDevice() {
		return device;
	}
	public String getStatus() {
		return status;
	}
	public void setMessage(String message) {
		Message = message;
	}*/
	public void setStatusCode(String statuscode) {
		this.statusCode = statuscode;
	}
	/*public void setDevice(String device) {
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
	}*/
	public String getTestStatus() {
		return testStatus;
	}
	public void setTestStatus(String testStatus) {
		this.testStatus = testStatus;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
