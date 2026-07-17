package com.tasnetwork.calibration.energymeter.uac;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UacDataModel {
	
	private final StringProperty serialNo;
	private final StringProperty roleName;
	private final BooleanProperty visibleEnabled;
	private final BooleanProperty executePossible;
	private final BooleanProperty addPossible;
	private final BooleanProperty updatePossible;
	private final BooleanProperty deletePossible;
	private final SimpleStringProperty screenName;
	private final SimpleStringProperty sectionName;
	private final SimpleStringProperty subSectionName;
	
	public UacDataModel (String serialNo, String screenName, String sectionName,String subSectionName,
			String roleName, boolean visibleEnabled ,boolean executePossible ,
			boolean addPossible, boolean updatePossible , boolean deletePossible ){
		
		this.serialNo = new SimpleStringProperty(serialNo);
		this.screenName = new SimpleStringProperty(screenName);
		this.sectionName = new SimpleStringProperty(sectionName);
		this.subSectionName = new SimpleStringProperty(subSectionName);
		this.roleName = new SimpleStringProperty(roleName);
		this.visibleEnabled = new SimpleBooleanProperty(visibleEnabled);
		this.executePossible = new SimpleBooleanProperty(executePossible);
		this.addPossible = new SimpleBooleanProperty(addPossible);
		this.updatePossible = new SimpleBooleanProperty(updatePossible);
		this.deletePossible = new SimpleBooleanProperty(deletePossible);
	}


	public StringProperty getSerialNoProperty() {
		return serialNo;
	}
	
	public String getSerialNo() {
		return serialNo.get();
	}
	
	public void setSerialNo(String value) {
		serialNo.set(value);;
	}
	
	
	
	public StringProperty getScreenNameProperty() {
		return screenName;
	}
	
	public String getScreenName() {
		return screenName.get();
	}
	
	public void setScreenName(String value) {
		screenName.set(value);;
	}
	
	

	public StringProperty getSectionNameProperty() {
		return sectionName;
	}
	
	public String getSectionName() {
		return sectionName.get();
	}
	
	public void setSectionName(String value) {
		sectionName.set(value);;
	}
	
	public StringProperty getSubSectionNameProperty() {
		return subSectionName;
	}
	
	public String getSubSectionName() {
		return subSectionName.get();
	}
	
	public void setSubSectionName(String value) {
		subSectionName.set(value);;
	}
	
	
	public StringProperty getRoleNameProperty() {
		return roleName;
	}
	
	public String getRoleName() {
		return roleName.get();
	}
	
	public void setRoleName(String value) {
		roleName.set(value);
	}


	public BooleanProperty getVisibleEnabledProperty() {
		return visibleEnabled;
	}
	
	public Boolean getVisibleEnabled() {
		return visibleEnabled.get();
	}
	
	public void setVisibleEnabled(boolean value) {
		visibleEnabled.set(value);
	}


	public BooleanProperty getExecutePossibleProperty() {
		return executePossible;
	}
	
	public Boolean getExecutePossible() {
		return executePossible.get();
	}
	
	public void setExecutePossible(boolean value) {
		executePossible.set(value);
	}


	public BooleanProperty getAddPossibleProperty() {
		return addPossible;
	}
	
	public Boolean getAddPossible() {
		return addPossible.get();
	}
	
	public void setAddPossible(boolean value) {
		addPossible.set(value);
	}


	public BooleanProperty getUpdatePossibleProperty() {
		return updatePossible;
	}
	
	public Boolean getUpdatePossible() {
		return updatePossible.get();
	}
	
	public void setUpdatePossible(boolean value) {
		updatePossible.set(value);
	}


	public BooleanProperty getDeletePossibleProperty() {
		return deletePossible;
	}
	
	public Boolean getDeletePossible() {
		return deletePossible.get();
	}
	
	public void setDeletePossible(boolean value) {
		deletePossible.set(value);
	}

/*	private String serialNo = "1";
	private String roleName = "";
	private boolean visibleEnabled = false;
	private boolean executePossible = false;
	private boolean addPossible = false;
	private boolean updatePossible = false;
	private boolean deletePossible = false;
	
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public boolean isVisibleEnabled() {
		return visibleEnabled;
	}
	public void setVisibleEnabled(boolean visibleEnabled) {
		this.visibleEnabled = visibleEnabled;
	}
	public boolean isExecutePossible() {
		return executePossible;
	}
	public void setExecutePossible(boolean executePossible) {
		this.executePossible = executePossible;
	}
	public boolean isAddPossible() {
		return addPossible;
	}
	public void setAddPossible(boolean addPossible) {
		this.addPossible = addPossible;
	}
	public boolean isUpdatePossible() {
		return updatePossible;
	}
	public void setUpdatePossible(boolean updatePossible) {
		this.updatePossible = updatePossible;
	}
	public boolean isDeletePossible() {
		return deletePossible;
	}
	public void setDeletePossible(boolean deletePossible) {
		this.deletePossible = deletePossible;
	}*/
}

