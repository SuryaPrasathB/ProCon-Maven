package com.tasnetwork.calibration.energymeter.setting;

import java.net.URL;
import java.util.ResourceBundle;

import com.tasnetwork.calibration.energymeter.constant.ConstEULA;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

public class TermsConditionController implements Initializable {
	@FXML
	private TextArea txtAreaTermsDisplay;

	/*
	 * @FXML
	 * private CheckBox checkBoxAgreeTermsCondition;
	 * 
	 * 
	 * @FXML
	 * public CheckBox ref_checkBoxAgreeTermsCondition;
	 */

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		txtAreaTermsDisplay.setText(ConstEULA.TERMS_AND_CONDITIONS);
		txtAreaTermsDisplay.setEditable(false);
		txtAreaTermsDisplay.setWrapText(true);
		// ref_checkBoxAgreeTermsCondition = checkBoxAgreeTermsCondition;
		// ref_checkBoxAgreeTermsCondition.setSelected(true);

	}

	public void okayOnClick() {
		txtAreaTermsDisplay.getScene().getWindow().hide();
	}

}
