package com.tasnetwork.calibration.conveyor.customer;

public class CustomerModel {
	
//	private String customerName;
	private String deliveredDate;
	private String buildVersion;
	private String databaseSchemaVersion;
	private String configFileVersion;
	
	private int noOfRacksOnPanel=0;
	private Boolean importModeEnabled = false;
	private Boolean exportModeEnabled = false;
/*	private String latestDeliveryDate;
	private String latestDeliveryBuildVersion;
	private String latestDeliveryDbSchemaVersion;
	private String latestDeliveryConfigFileVersion;*/
	
	
/*	CustomerModel(String bVersion,	String dbSchemaVersion, String configVersion){
		buildVersion = bVersion;
		databaseSchemaVersion =dbSchemaVersion;
		configFileVersion =configVersion;
	}*/
/*	CustomerModel(String cName){
		customerName = cName;
	}*/

/*	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String value) {
		customerName = value;
	}*/
	
	public Boolean getExportModeEnabled() {
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
	}

	public String getBuildVersion() {
		return buildVersion;
	}

	public void setBuildVersion(String value) {
		buildVersion = value;
	}
	

	
	public String getDeliveredDate() {
		return deliveredDate;
	}

	public void setDeliveredDate(String value) {
		deliveredDate = value;
	}

	public String getDbSchemaVersion() {
		return databaseSchemaVersion;
	}

	public void setDbSchemaVersion(String value) {
		databaseSchemaVersion = value;
	}

	
	public String getConfigFileVersion() {
		return configFileVersion;
	}

	public void setConfigFileVersion(String value) {
		configFileVersion = value;
	}

	

}
