package com.tasnetwork.calibration.conveyor.dut_executors;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

public class DutExecutorContainerController implements Initializable{

	@FXML
	private AnchorPane ftExecutorChild;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		refInit();
		guiInit();
		dataSetupInit();
		loadAllChild_FXML();
	}

	private void dataSetupInit() {
		// TODO Auto-generated method stub
		
	}

	private void guiInit() {
		// TODO Auto-generated method stub
		
	}

	private void refInit() {
		// TODO Auto-generated method stub
		
	}

	//private void logInit() {
	// TODO Auto-generated method stub

	//}

	public void loadAllChild_FXML(){
		ApplicationLauncher.logger.info("loadAllChild_FXML :Entry");

		Platform.runLater(() -> {
			try{

				ftExecutorChild.getChildren().add(getNodeFromFXML("/fxml/conveyor/DutExecutorFt" + ConstantApp.THEME_FXML));
				//InitCounter--;


			} catch (Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("DutExecutorFt : loadAllChild_FXML:  Exception:" + e.getMessage());
			}});

/*		Platform.runLater(() -> {
			try{

				stateExecutorChild.getChildren().add(getNodeFromFXML("/fxml/conveyor/StateExecutor" + ConstantApp.THEME_FXML));
				//InitCounter--;


			} catch (Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("ConveyorDebugController : loadAllChild_FXML:  Exception:" + e.getMessage());
			}});

		Platform.runLater(() -> {
			try{

				bayTestChild.getChildren().add(getNodeFromFXML("/fxml/conveyor/BayTest" + ConstantApp.THEME_FXML));
				//InitCounter--;


			} catch (Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("ConveyorDebugController : loadAllChild_FXML:  Exception:" + e.getMessage());
			}});

		Platform.runLater(() -> {
			try{

				plcClientBayTestChild.getChildren().add(getNodeFromFXML("/fxml/conveyor/PlcClientBayTest" + ConstantApp.THEME_FXML));
				//InitCounter--;


			} catch (Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("ConveyorDebugController : loadAllChild_FXML: PlcClientBayTest:  Exception:" + e.getMessage());
			}});
		
		Platform.runLater(() -> {
			try{

				plcServerBayTestChild.getChildren().add(getNodeFromFXML("/fxml/conveyor/PlcServerBayTest" + ConstantApp.THEME_FXML));
				//InitCounter--;


			} catch (Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("ConveyorDebugController : loadAllChild_FXML: PlcServerBayTest:  Exception:" + e.getMessage());
			}});*/
	}

	private Parent getNodeFromFXML(String url) throws IOException{
		return FXMLLoader.load(getClass().getResource(url));
	}

}
