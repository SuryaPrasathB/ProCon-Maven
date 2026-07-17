package com.tasnetwork.calibration.conveyor.customer;

public class CustomerManager {
	
	private String customerName;
	private int noOfRacksOnPanel;
	private String productName;
	private String targetDevice;
	private Boolean importModeEnabled ;
	private Boolean exportModeEnabled ;
	
	public CustomerManager(String cName, String pName, String tDevice){
		
		customerName = cName;
		//noOfRacksOnPanel = RackNumber;
		productName = pName;
		targetDevice  = tDevice;
		//importModeEnabled = importMode;
		//exportModeEnabled = exportMode;
		
	}
	
	public CustomerModel initialDelivery = new CustomerModel();
	public CustomerModel latestDelivery = new CustomerModel();
	
	
/*	public Boolean getExportModeEnabled() {
		return exportModeEnabled;
	}

	public void setExportModeEnabled(Boolean value) {
		exportModeEnabled = value;
	}
	
	public Boolean getImportModeEnabled() {
		return importModeEnabled;
	}

	public void setImportModeEnabled(Boolean value) {
		importModeEnabled = value;
	}
	
	public int getNoOfRacksOnPanel() {
		return noOfRacksOnPanel;
	}

	public void setNoOfRacksOnPanel(int value) {
		noOfRacksOnPanel = value;
	}*/
	
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
