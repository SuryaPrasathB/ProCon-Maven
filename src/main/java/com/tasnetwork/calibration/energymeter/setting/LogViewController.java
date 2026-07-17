package com.tasnetwork.calibration.energymeter.setting;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import com.tasnetwork.calibration.conveyor.AsyncHttpClient.AsyncClientManager;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

public class LogViewController implements Initializable {
	
	
	
	@FXML
	private ComboBox cmbBxListOfLogFolder;
	public static ComboBox ref_cmbBxListOfLogFolder;
	
	
	
	@FXML
	private ComboBox cmbBxListOfLogFiles;
	public static ComboBox ref_cmbBxListOfLogFiles;
	
	@FXML
	private TextArea txtAreaLogContent;
	public static TextArea ref_txtAreaLogContent;
	
	@FXML
	private Button btnScanLogFolder;
	public static Button ref_btnScanLogFolder;
	
	@FXML
	private Button btnScanLogFiles;
	public static Button ref_btnScanLogFiles;
	
	
	
	@FXML
	private Button btnViewLogFile;
	public static Button ref_btnViewLogFile;
	
	@FXML
	private TitledPane titledPaneLogFileView;
	public static TitledPane ref_titledPaneLogFileView;
	
	Timer ScanLogFolderTaskTimer;
	Timer scanLogFilesTaskTimer;
	Timer viewLogFileTaskTimer;
	
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		ref_cmbBxListOfLogFolder = cmbBxListOfLogFolder;
		ref_cmbBxListOfLogFiles = cmbBxListOfLogFiles;
		ref_txtAreaLogContent = txtAreaLogContent;
		ref_btnScanLogFolder = btnScanLogFolder;
		ref_btnScanLogFiles = btnScanLogFiles;
		ref_btnViewLogFile = btnViewLogFile;
		ref_titledPaneLogFileView = titledPaneLogFileView;
		ref_btnScanLogFiles.setDisable(true);
		ref_cmbBxListOfLogFolder.setDisable(true);
		ref_btnViewLogFile.setDisable(true);
		ref_cmbBxListOfLogFiles.setDisable(true);

	}
	
	
    public static  void updateListOfLogFolders(String[] logfolders){
    	ApplicationLauncher.logger.info("updateListOfLogFolders: Entry");
    	
    	
    	Platform.runLater(() -> {
    		if(logfolders.length!=0){
    			ref_cmbBxListOfLogFolder.getItems().setAll(logfolders);
    			ref_cmbBxListOfLogFolder.getSelectionModel().select(0);
    			ref_btnScanLogFiles.setDisable(false);
    			ref_cmbBxListOfLogFolder.setDisable(false);
    			ref_btnViewLogFile.setDisable(true);
    			ref_cmbBxListOfLogFiles.setDisable(true);
    		}
		});
    	ApplicationLauncher.logger.info("updateListOfLogFolders: Exit");
/*        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainContext, android.R.layout.simple_spinner_dropdown_item, logfolders);//Collections.singletonList(value1)
        spinnerListOfLogFolders.setAdapter(adapter);
        //System.out.println("finishing activity updateListOfLogFolders");
        if(logfolders.length!=0){
            UpdateLogs_scanlogfilesButton(true);//Button(true);
        }*/

    }
    
    public static  void updatefilesinlogview(String[] logfiles){
    	
    	ApplicationLauncher.logger.info("updatefilesinlogview: Entry");
    	
    	
    	Platform.runLater(() -> {
    		if(logfiles.length!=0){
    			ref_cmbBxListOfLogFiles.getItems().setAll(logfiles);
    			ref_cmbBxListOfLogFiles.getSelectionModel().select(0);
    			ref_btnViewLogFile.setDisable(false);
    			ref_cmbBxListOfLogFiles.setDisable(false);
    		}
		});   	
    	
/*        System.out.println("Entering activity updatefilesinlogview");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainContext, android.R.layout.simple_spinner_dropdown_item, logfiles);//Collections.singletonList(value1)
        spinnerListOfFilesinLogFolder.setAdapter(adapter);
       // System.out.println("finishing activity updatefilesinlogview");
        if(logfiles.length!=0){
            UpdateLogs_viewlogsButton(true);//Button(true);
        }*/


    }
    
/*    public static void UpdateLogs_viewlogsButton(boolean status){
        btn_view_logs.setEnabled(status);
    }
    
    public static void UpdateLogs_scanlogfilesButton(boolean status){
        btn_scan_logfiles.setEnabled(status);
    }*/
    
    public static  void updateViewLogContentSuccess(String logview_content){
    	final String logContent = logview_content.replace("|","\n");
    	Platform.runLater(() -> {
    		ref_titledPaneLogFileView.setText(ref_cmbBxListOfLogFiles.getSelectionModel().getSelectedItem().toString());
	    	ref_txtAreaLogContent.setText(logContent);
	    	//ApplicationLauncher.logger.info("updateViewLogContent: logContent:"+logContent);
	    	ApplicationLauncher.InformUser("Success","Log content retrieval success",AlertType.INFORMATION);
    	});
       /* AlertDialog.Builder builder= new AlertDialog.Builder(MainContext);
        //TextView textView = (TextView) builder.findViewById(android.R.id.message);
        //textView.setTextSize(40);
        TextView myMsg = new TextView(MainContext);
        myMsg.setTextSize(20);
        myMsg.setText(logview_content);
        builder.setTitle("Logview Content")
                .setMessage(logview_content)
               // .setCustomTitle(myMsg)

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.out.println("logview_content"+logview_content);
                    }
                });
        AlertDialog alter = builder.create();


        alter.show();
        alter.getWindow().getAttributes();

        TextView textView = (TextView) alter.findViewById(android.R.id.message);
        textView.setTextSize(10);
        System.out.println("finishing activity updateViewLogContent");*/
    }
    
    public static  void updateViewLogContentFailure(){
    	//final String logContent = logview_content.replace("|","\n");
    	Platform.runLater(() -> {
    		ref_titledPaneLogFileView.setText("");
	    	ref_txtAreaLogContent.clear();
	    	ApplicationLauncher.logger.info("updateViewLogContentFailure: Error-808: Unable to retrieve the logs. kindly retry");
	    	ApplicationLauncher.InformUser("Error - 808","Error-808: Unable to retrieve the logs. kindly retry",AlertType.ERROR);
    	});

    }
	
	@FXML
    public void ScanLogFolderTaskTrigger() {
		ApplicationLauncher.logger.info("ScanLogFolderTaskTrigger : Entry");
		DisableScanLogFolderButton();
		DisableCmbBxListOfLogFolder();
		ScanLogFolderTaskTimer = new Timer();
		ScanLogFolderTaskTimer.schedule(new ScanLogFolderTask(), 100);
	}
		
	
	public void ScanLogFolder(){
		AsyncClientManager asycnClient = new AsyncClientManager();
        asycnClient.tamperScanServerLogFoldersV2();
        
	}
	
	public static void DisableScanLogFolderButton(){
		ref_btnScanLogFolder.setDisable(true);
	}
	
	public static void EnableScanLogFolderButton(){
		ref_btnScanLogFolder.setDisable(false);
	}
	
	
	
	public static void DisableCmbBxListOfLogFolder(){
		ref_cmbBxListOfLogFolder.setDisable(true);
	}
	
	public static void EnableCmbBxListOfLogFolder(){
		ref_cmbBxListOfLogFolder.setDisable(false);
	}
	
	public static void DisableCmbBxListOfLogFiles(){
		ref_cmbBxListOfLogFiles.setDisable(true);
	}
	
	public static void EnableCmbBxListOfLogFiles(){
		ref_cmbBxListOfLogFiles.setDisable(false);
	}
	
	
	
	public static void DisableScanLogFilesButton(){
		ref_btnScanLogFiles.setDisable(true);
	}
	
	public static void EnableScanLogFilesButton(){
		ref_btnScanLogFiles.setDisable(false);
	}
	
	
	
	public static void DisableViewLogFileButton(){
		ref_btnViewLogFile.setDisable(true);
	}
	
	public static void EnableViewLogFileButton(){
		ref_btnViewLogFile.setDisable(false);
	}
	
	@FXML
	public void scanLogFilesTaskTrigger() {
		ApplicationLauncher.logger.info("scanLogFilesTaskTrigger : Entry");
		DisableScanLogFolderButton();
		DisableCmbBxListOfLogFolder();
		DisableScanLogFilesButton();
		DisableCmbBxListOfLogFiles();
		DisableViewLogFileButton();
		scanLogFilesTaskTimer = new Timer();
		scanLogFilesTaskTimer.schedule(new scanLogFilesTask(), 100);
	}
	
	public void scanLogFiles(){
		
		String SelectedFolderName = ref_cmbBxListOfLogFolder.getSelectionModel().getSelectedItem().toString();
		AsyncClientManager asycnClient = new AsyncClientManager();
        asycnClient.tamperScanFilesInLogFolder(SelectedFolderName);
        
	}
	
	
	@FXML
	public void viewLogFileTaskTrigger() {
		ApplicationLauncher.logger.info("viewLogFileTaskTrigger : Entry");
		DisableScanLogFolderButton();
		DisableCmbBxListOfLogFolder();
		DisableScanLogFilesButton();
		DisableCmbBxListOfLogFiles();
		DisableViewLogFileButton();
		
		viewLogFileTaskTimer = new Timer();
		viewLogFileTaskTimer.schedule(new viewLogFileTask(), 100);
	}
	
	public void viewLogFile(){
		
		String SelectedFileName = ref_cmbBxListOfLogFiles.getSelectionModel().getSelectedItem().toString();
		AsyncClientManager asycnClient = new AsyncClientManager();
        asycnClient.tamperViewLogFromSelectedFile(SelectedFileName);
	}
	
	class ScanLogFolderTask extends TimerTask {


		public void run() {
			ApplicationLauncher.logger.info("ScanLogFolderTask: Entry");
			
			ScanLogFolder();
			
			ScanLogFolderTaskTimer.cancel();
		}
	};
	
	class scanLogFilesTask extends TimerTask {


		public void run() {
			ApplicationLauncher.logger.info("scanLogFilesTask: Entry");
			
			scanLogFiles();
			
			scanLogFilesTaskTimer.cancel();
		}
	};
	
	
	
	class viewLogFileTask extends TimerTask {


		public void run() {
			ApplicationLauncher.logger.info("viewLogFileTask: Entry");
			
			viewLogFile();
			
			viewLogFileTaskTimer.cancel();
		}
	};

	

}
