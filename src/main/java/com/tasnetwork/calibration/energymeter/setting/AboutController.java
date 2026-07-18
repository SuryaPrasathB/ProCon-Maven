package com.tasnetwork.calibration.energymeter.setting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstEULA;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;
import com.tasnetwork.calibration.energymeter.constant.ProCalCustomerConfiguration;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AboutController implements Initializable {

	@FXML
	private TextField txtFieldAppVersion;

	@FXML
	private TextField txtFieldDB_SchemaVersion;

	@FXML
	private TextField txtFieldProductName;

	@FXML
	private TextField txtDesignedBy;

	@FXML
	private TextField txtMarketedBy;

	@FXML
	private TextField txtRefStd;

	@FXML
	private TextField txtLdu;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		txtFieldProductName.setText(ConstantVersion.APPLICATION_NAME);
		txtFieldAppVersion.setText(ConstantVersion.APPLICATION_VERSION);
		txtFieldDB_SchemaVersion.setText(ConstantVersion.DB_SCHEMA_VERSION);
		hideObjects();
	}

	private void hideObjects() {

		if (ProCalCustomerConfiguration.KIGG_1PHASE_20POSITION_2022) {
			txtDesignedBy.setText("");
			txtMarketedBy.setText("");
			txtRefStd.setText("");
			txtLdu.setText("");
			txtDesignedBy.setText("");

		}
	}

	public void TermsConditionsOnClick() {

		// Alert alert = new Alert(AlertType.INFORMATION ,
		// ConstEULA.TERMS_AND_CONDITIONS, ButtonType.OK);
		// alert.showAndWait();
		ApplicationLauncher.logger.debug("TermsConditionsOnClick: entry");

		FXMLLoader loader = new FXMLLoader(
				getClass().getResource("/fxml/setting/TermsCondition" + ConstantApp.THEME_FXML));
		Scene newScene;
		try {
			newScene = new Scene(loader.load());
		} catch (IOException ex) {

			ex.printStackTrace();
			ApplicationLauncher.logger.error("TermsConditionsOnClick: IOException:" + ex.getMessage());
			return;
		}

		Stage CreateNewStage = new Stage();

		Stage primaryStage = ApplicationLauncher.getPrimaryStage();

		CreateNewStage.initModality(Modality.WINDOW_MODAL);
		CreateNewStage.initOwner(primaryStage);
		CreateNewStage.getIcons().add(new Image("file:images/" + ConstantVersion.APP_ICON_FILENAME));
		CreateNewStage.setScene(newScene);
		CreateNewStage.setTitle(ConstantVersion.APPLICATION_NAME);
		CreateNewStage.setAlwaysOnTop(true);
		CreateNewStage.showAndWait();

	}

	/*
	 * public void InformUser(String title, String info,AlertType Alert_type){
	 * TextBoxDialog TextBoxDialogobj = new TextBoxDialog();
	 * TextBoxDialogobj.TriggerUserInfoPlatFormLater(title, info, Alert_type);
	 * }
	 */

}