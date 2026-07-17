package com.tasnetwork.calibration.energymeter.setting;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstEULA;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class TermsConditionController implements Initializable {
    @FXML
    private TextArea txtAreaTermsDisplay;
    
   /* @FXML
    private CheckBox checkBoxAgreeTermsCondition;
    
    
    @FXML
    public CheckBox ref_checkBoxAgreeTermsCondition;*/
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		txtAreaTermsDisplay.setText(ConstEULA.TERMS_AND_CONDITIONS);
		txtAreaTermsDisplay.setEditable(false);
		txtAreaTermsDisplay.setWrapText(true);
		//ref_checkBoxAgreeTermsCondition = checkBoxAgreeTermsCondition;
		//ref_checkBoxAgreeTermsCondition.setSelected(true);
		
	}
	
	public void okayOnClick(){
		txtAreaTermsDisplay.getScene().getWindow().hide();
	}
	

}
