package com.tasnetwork.calibration.energymeter.setting;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ProcalUserModel {
	
	private final StringProperty userName;
	private final StringProperty password;
	//edited mohan
	private final StringProperty confirm_password;
	//fin
	private final StringProperty accessLevel;
	

public ProcalUserModel(String username, String password,String confirm_password, String accesslevel) {
		
		this.userName = new SimpleStringProperty(username);
		this.password = new SimpleStringProperty(password);
		this.confirm_password = new SimpleStringProperty(confirm_password);
		this.accessLevel = new SimpleStringProperty(accesslevel);

	}
	public String getUserName() {
		return userName.get();
	}

	public void setUserName(String userName) {
		this.userName.set(userName);
	}
	
	public StringProperty userNameProperty() {
		return userName;
	}

	public String getPassword() {
		return password.get();
	}

	public void setPassword(String password_input) {
		this.password.set(password_input);
	}
	
	public StringProperty passwordProperty() {
		return password;
	}
	
	public String getAccessLevel() {
		return accessLevel.get();
	}

	public void setAccessLevel(String access_level) {
		this.accessLevel.set(access_level);
	}
	
	public StringProperty accessLevelProperty() {
		return accessLevel;
	}
	//edited mohan
	public String getconfirm_password() {
		return password.get();
	}

	public void setconfirm_password(String confirm_password_input) {
		this.password.set(confirm_password_input);
	}
	
	public StringProperty confirm_passwordProperty() {
		return password;
	}
	//fin

	
	public String toStrig() {
		
		 StringBuilder toPrint = new StringBuilder();
		 toPrint.append("UserName:" + userName).append(", ");
		 toPrint.append("Password:" + password).append(", ");
		 //edited mohan
		 toPrint.append("confirm_password:" + confirm_password).append(", ");
		 //fin
		 toPrint.append("AccessLevel:" + accessLevel);
	 
	     return toPrint.toString();
	}
}
