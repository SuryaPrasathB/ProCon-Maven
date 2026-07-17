package com.tasnetwork.calibration.conveyor;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PositionLoadedModel {
	
	


	StringProperty serialNo = new SimpleStringProperty();
	StringProperty positionNo = new SimpleStringProperty();
	StringProperty cName = new SimpleStringProperty("");
	
	BooleanProperty positionSelected = new SimpleBooleanProperty();
	
	PositionLoadedModel(){
		
	}
	PositionLoadedModel(String sNO,String pNO, String cName){
		this.serialNo =  new SimpleStringProperty(sNO);
		this.positionNo =  new SimpleStringProperty(pNO);
		this.cName =  new SimpleStringProperty(cName);
	}
	
	public StringProperty getSerialNoProperty () {
		return serialNo;
	}

	public void setSerialNoProperty(SimpleStringProperty serialNo) {
		this.serialNo = serialNo;
	}
	
	public void setSerialNo(String serialNo) {
		this.serialNo.setValue( serialNo);
	}
	
	public String getSerialNo() {
		return this.serialNo.getValue();
	}

	public StringProperty getPositionNoProperty () {
		return positionNo;
	}

	public void setPositionNoProperty(SimpleStringProperty positionNo) {
		this.positionNo = positionNo;
	}
	
	public String getPositionNo () {
		return positionNo.getValue();
	}

	public void setPositionNo(String positionNo) {
		this.positionNo.setValue(positionNo);
	}
	
	public StringProperty getCNameProperty () {
		return cName;
	}
	
	public void setCNameProperty(SimpleStringProperty cName) {
		this.cName = cName;
	}
	
	public String getCName () {
		return cName.getValue();
	}
	
	public void setCName(String cName) {
		this.cName.setValue(cName);
	}
	
/*	
	String serialNo = "";
	String positionNo = "";
	
	PositionLoadedModel(String sNO,String pNO){
		this.serialNo =  (sNO);
		this.positionNo =  (pNO);
	}*/

	public BooleanProperty getPositionSelectedProperty() {
		return positionSelected;
	}

	public void setPositionSelectedProperty(BooleanProperty positionSelected) {
		this.positionSelected = positionSelected;
	}
	
	public Boolean isPositionSelected() {
		return positionSelected.getValue();
	}

	public void setPositionSelected(Boolean positionSelected) {
		this.positionSelected.setValue(positionSelected);
	}
	public StringProperty getcNameProperty() {
		return cName;
	}
	public void setcNameProperty(StringProperty cName) {
		this.cName = cName;
	}
	
	public String getcName() {
		return cName.getValue();
	}
	public void setcName(String cName) {
		this.cName.setValue(cName);
	}

}
