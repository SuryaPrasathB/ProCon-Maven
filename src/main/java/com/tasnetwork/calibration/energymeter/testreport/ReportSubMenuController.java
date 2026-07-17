package com.tasnetwork.calibration.energymeter.testreport;



import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.uac.UacDataModel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;

public class ReportSubMenuController implements Initializable  {

	

	
	@FXML
    private AnchorPane reportheaderconfig_childpane;
	
	@FXML
    private AnchorPane reportexcelconfig_childpane;

	@FXML
    private AnchorPane reportfilelocation_childpane;
	
    @FXML
    private Accordion reportAccordian;
    
    @FXML
    private TitledPane fileLocationTitlePane;
    

    
	public void LoadAllChild_FXML(){
		ApplicationLauncher.logger.info("ReportSubMenuController: LoadAllChild_FXML:Entry");

   	try {

    	

    	reportheaderconfig_childpane.getChildren().add(getNodeFromFXML("/fxml/testreport/ReportHeaderConfig" + ConstantApp.THEME_FXML));
    	reportexcelconfig_childpane.getChildren().add(getNodeFromFXML("/fxml/testreport/ReportExcelConfig" + ConstantApp.THEME_FXML));
    	reportfilelocation_childpane.getChildren().add(getNodeFromFXML("/fxml/testreport/ReportFileLocation" + ConstantApp.THEME_FXML));
    	}catch(Exception e) {
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("ReportSubMenuController: LoadAllChild_FXML: Exception:" + e.getMessage());
    	}
	}
		private Parent getNodeFromFXML(String url) throws IOException{
        return FXMLLoader.load(getClass().getResource(url));
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub


		LoadAllChild_FXML();
		reportAccordian.setExpandedPane(fileLocationTitlePane);
		
		if(ProcalFeatureEnable.USER_ACCESS_CONTROL_ENABLED){
			applyUacSettings();
		}
	}
	
	private static void applyUacSettings() {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.info("ReportSubMenuController : applyUacSettings :  Entry");
		ArrayList<UacDataModel> uacSelectProfileScreenList = DeviceDataManagerController.getUacSelectProfileScreenList();
		String screenName = "";
		for (int i = 0; i < uacSelectProfileScreenList.size(); i++){

			screenName = uacSelectProfileScreenList.get(i).getScreenName();
			switch (screenName) {
				case ConstantApp.UAC_REPORT_CONFIGURATION_SCREEN:
					
					
					if(!uacSelectProfileScreenList.get(i).getExecutePossible()){
						//ref_btn_deploy.setDisable(true);
						
					}
					
					if(!uacSelectProfileScreenList.get(i).getAddPossible()){
						//ref_btn_Create.setDisable(true);
						
					}
					
					if(!uacSelectProfileScreenList.get(i).getUpdatePossible()){
						//ref_vbox_testscript.setDisable(true);sdvsc
						//setChildPropertySaveEnabled(false);
						//ref_btn_Save.setDisable(true);
						
					}
					
					if(!uacSelectProfileScreenList.get(i).getDeletePossible()){
						//ref_btn_Delete.setDisable(true);
						
					}
					break;
					
								
	
				default:
					break;
			}
			
				
				
		}
	}
}

