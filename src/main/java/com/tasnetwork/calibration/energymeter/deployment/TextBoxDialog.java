package com.tasnetwork.calibration.energymeter.deployment;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.deployment.TextBoxDialog.UserInfoPlatFormRunnable;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TextBoxDialog implements Initializable {
	Thread DialogBoxPlatFormLaterThread;
	TextDialogBoxPlatFormRunnable DialogBoxPlatFormRunnable;
	Thread InfoDialogBoxPlatFormLaterThread;
	InfoTextDialogBoxPlatFormRunnable InfoDialogBoxPlatFormRunnable;
	Thread UserInfoPlatFormLaterThread;
	UserInfoPlatFormRunnable UserInfoPlatFormRunnable;
	static boolean InitReadingFlag = false;
	static boolean FinalReadingFlag = false;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}
	
	public static boolean getInitReadingFlag(){
		return InitReadingFlag;
	}
	
	public static boolean getFinalReadingFlag(){
		return FinalReadingFlag;
	}
	
	public void TriggerInitValueTDPlatFormLater(){

		InitReadingFlag = true;
		FinalReadingFlag = false; 
		DialogBoxPlatFormRunnable = new TextDialogBoxPlatFormRunnable();
		DialogBoxPlatFormLaterThread = new Thread(DialogBoxPlatFormRunnable);
		DialogBoxPlatFormLaterThread.start();
		ApplicationLauncher.logger.info("TriggerUIResultRefreshPlatFormLater :Exit");
		
	}
	
	public void TriggerFinalValueTDPlatFormLater(){

		InitReadingFlag = false;
		FinalReadingFlag = true; 
		DialogBoxPlatFormRunnable = new TextDialogBoxPlatFormRunnable();
		DialogBoxPlatFormLaterThread = new Thread(DialogBoxPlatFormRunnable);
		DialogBoxPlatFormLaterThread.start();
		ApplicationLauncher.logger.info("TriggerUIResultRefreshPlatFormLater :Exit");
		
	}
	
	public void TriggerCutInfoPlatFormLater(){

		InfoDialogBoxPlatFormRunnable = new InfoTextDialogBoxPlatFormRunnable();
		InfoDialogBoxPlatFormLaterThread = new Thread(InfoDialogBoxPlatFormRunnable);
		InfoDialogBoxPlatFormLaterThread.start();
		ApplicationLauncher.logger.info("TriggerCutInfoPlatFormLater :Exit");
		
	}
	
	public void TriggerUserInfoPlatFormLater(String title, String info,AlertType Alert_type){

		UserInfoPlatFormRunnable = new UserInfoPlatFormRunnable(title, info, Alert_type);
		UserInfoPlatFormLaterThread = new Thread(UserInfoPlatFormRunnable);
		UserInfoPlatFormLaterThread.start();
		ApplicationLauncher.logger.info("TriggerUserInfoPlatFormLater :Exit");
		
	}
	
	class TextDialogBoxPlatFormRunnable implements Runnable{

		TextInputDialog textdialog;
		int noofdevices;
		
		public TextDialogBoxPlatFormRunnable() {

		}

		@Override
		public void run() {

				ApplicationLauncher.logger.info("TextDialogBoxPlatFormRunnable entry");
				Platform.runLater(new Runnable(){

					@Override
					public void run() {
						MeterReadingPopup();

					}
				});
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
					ApplicationLauncher.logger.error("TextDialogBoxPlatFormRunnable :InterruptedException:"+ ex.getMessage());
				} 

				ApplicationLauncher.logger.info("TextDialogBoxPlatFormRunnable exit");
				noofdevices--;

		}

	}
	
	class InfoTextDialogBoxPlatFormRunnable implements Runnable{

		TextInputDialog textdialog;
		
		public InfoTextDialogBoxPlatFormRunnable() {
			
		}

		@Override
		public void run() {
				ApplicationLauncher.logger.info("InfoTextDialogBoxPlatFormRunnable entry");
				Platform.runLater(new Runnable(){

					@Override
					public void run() {
						InformUserToCutNuetral();
					}
				});
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
					ApplicationLauncher.logger.error("InfoTextDialogBoxPlatFormRunnable :InterruptedException:"+ ex.getMessage());
				} 

				ApplicationLauncher.logger.info("TextDialogBoxPlatFormRunnable exit");
		}

	}
	
	class UserInfoPlatFormRunnable implements Runnable{

		String title;
		String info;
		AlertType Alert_type;
		
		public UserInfoPlatFormRunnable(String title_input, String user_info,AlertType inputAlerttype) {
			title = title_input;
			info = user_info;
			Alert_type =  inputAlerttype;
		}

		@Override
		public void run() {
				ApplicationLauncher.logger.info("UserInfoPlatFormRunnable entry");
				Platform.runLater(new Runnable(){

					@Override
					public void run() {
						UserInfoMsg(title, info,Alert_type);
					}
				});
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
					ApplicationLauncher.logger.error("UserInfoPlatFormRunnable :InterruptedException:"+ ex.getMessage());
				} 

				ApplicationLauncher.logger.info("UserInfoPlatFormRunnable exit");
		}

	}
	
	public void MeterReadingPopup(){
		ApplicationLauncher.logger.info("MeterReadingPopup: entry");	
		//ApplicationLauncher.logger.info("MeterReadingPopup: entry");	
		 FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/deployment/MeterReadingPopup" + ConstantApp.THEME_FXML));
		 if(ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK == 48){
			 ApplicationLauncher.logger.info("MeterReadingPopup: loading 48 position");
			 loader = new FXMLLoader(getClass().getResource("/fxml/deployment/MeterReadingPopup48" + ConstantApp.THEME_FXML));
		 }else if(ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK == 40){
			 ApplicationLauncher.logger.info("MeterReadingPopup: loading 40 position");
			 loader = new FXMLLoader(getClass().getResource("/fxml/deployment/MeterReadingPopup40" + ConstantApp.THEME_FXML));
		 }else if(ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK == 20){
			 ApplicationLauncher.logger.info("MeterReadingPopup: loading 20 position");
			 loader = new FXMLLoader(getClass().getResource("/fxml/deployment/MeterReadingPopup20" + ConstantApp.THEME_FXML));
		 }else if(ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK <= 12){
			 ApplicationLauncher.logger.info("MeterReadingPopup: loading 12 position");
			 loader = new FXMLLoader(getClass().getResource("/fxml/deployment/MeterReadingPopup12" + ConstantApp.THEME_FXML)); 
		 }
	        Scene newScene;
	        try {
	            newScene = new Scene(loader.load());
	        } catch (IOException ex) {
	            // TODO: handle error
	        	ex.printStackTrace();
				ApplicationLauncher.logger.error("MeterReadingPopup :IOException:"+ ex.getMessage());
	            return;
	        }

	        Stage SaveAsStage = new Stage();
	        //https://stackoverflow.com/questions/36071779/how-to-open-an-additional-window-in-a-javafx-fxml-app?rq=1

	        Stage primaryStage = ApplicationLauncher.getPrimaryStage();
	        

	        SaveAsStage.initModality(Modality.WINDOW_MODAL);
	        SaveAsStage.initOwner(primaryStage);
	        //https://stackoverflow.com/questions/38481914/disable-background-stage-javafx?rq=1
	        
	        SaveAsStage.setScene(newScene);
	        //Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
	        SaveAsStage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
	        SaveAsStage.setAlwaysOnTop(true);
	        SaveAsStage.setOnCloseRequest(e->e.consume());
	        if((InitReadingFlag) && (!FinalReadingFlag)){
	        	SaveAsStage.setTitle("Enter Initial Reading");
				ApplicationLauncher.logger.info("MeterReadingPopup: Initial");							
			}
			else if((!InitReadingFlag) && (FinalReadingFlag)){
				SaveAsStage.setTitle("Enter Final Reading");
				ApplicationLauncher.logger.info("MeterReadingPopup: Final");
			}
			else{
				ApplicationLauncher.logger.info("MeterReadingPopup: Else case");
			}
	        
	        SaveAsStage.showAndWait();

	}
	
	public void InformUserToCutNuetral(){

		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
		alert.setTitle("Confirmation");
		String s = "Cut the nuetral and click ok to continue the test";
		alert.setContentText(s);

		Optional<ButtonType> result = alert.showAndWait();

		if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
			DeviceDataManagerController.setcutnuetral_flag(true);
			DeviceDataManagerController.setcutnuetral_wait_flag(false);
		}
		else{
			DeviceDataManagerController.setcutnuetral_flag(false);
			DeviceDataManagerController.setcutnuetral_wait_flag(false);
		}
	}

	
	public void UserInfoMsg(String title, String info, AlertType Alert_type){
		Alert alert = new Alert(Alert_type);
		alert.setTitle(title);
		String s = info;
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
		alert.setContentText(s);

		Optional<ButtonType> result = alert.showAndWait();
	}

	
}
