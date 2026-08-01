package com.tasnetwork.calibration.conveyor.customer;

public class CustomerManager {
	
	private String customerName;
	private String productName;
	private String targetDevice;
	
	public CustomerManager(String cName, String pName, String tDevice){
		
		customerName = cName;
		productName = pName;
		targetDevice  = tDevice;
	
	}
	
	public CustomerModel initialDelivery = new CustomerModel();
	public CustomerModel latestDelivery = new CustomerModel();

	public String getTargetDevice() {
		return targetDevice;
	}

	public void setTargetDevice(String value) {
		targetDevice = value;
	}
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String value) {
		customerName = value;
	}
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String value) {
		productName = value;
	}

}
