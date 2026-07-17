package com.tasnetwork.calibration.energymeter.deployment;



public class DutResponse {

	private boolean status = false;
	private String responseData = "";
	private boolean responseBooleanData = false;
	//private TestInterfaceStatus testInterfaceStatus = new TestInterfaceStatus();
	private String errorCode = "ERROR_100";
	private float currentValue = -1000.0f;
	private float voltValue = -2000.0f;
	//private String myPalletDistinctId = "";
	//private String myPalletQrCode = "";
	
	
	public boolean isStatus() {
		return status;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public boolean getStatus() {
		return this.status;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public float getCurrentValue() {
		return currentValue;
	}
	public void setCurrentValue(float currentValue) {
		this.currentValue = currentValue;
	}
	public String getResponseData() {
		return responseData;
	}
	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}
/*	public TestInterfaceStatus getTestInterfaceStatus() {
		return testInterfaceStatus;
	}
	public void setTestInterfaceStatus(TestInterfaceStatus testInterfaceStatus) {
		this.testInterfaceStatus = testInterfaceStatus;
	}*/
	public float getVoltValue() {
		return voltValue;
	}
	public void setVoltValue(float voltValue) {
		this.voltValue = voltValue;
	}
/*	public String getMyPalletDistinctId() {
		return myPalletDistinctId;
	}
	public void setMyPalletDistinctId(String myPalletDistinctId) {
		this.myPalletDistinctId = myPalletDistinctId;
	}
	public String getMyPalletQrCode() {
		return myPalletQrCode;
	}
	public void setMyPalletQrCode(String myPalletQrCode) {
		this.myPalletQrCode = myPalletQrCode;
	}*/
	public boolean isResponseBooleanData() {
		return responseBooleanData;
	}
	public void setResponseBooleanData(boolean responseBooleanData) {
		this.responseBooleanData = responseBooleanData;
	}
}

