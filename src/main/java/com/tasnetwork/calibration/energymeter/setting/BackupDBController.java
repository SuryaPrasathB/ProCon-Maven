package com.tasnetwork.calibration.energymeter.setting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.setting.BackupDBController.Mode;
import com.tasnetwork.calibration.energymeter.uac.UacDataModel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class BackupDBController implements Initializable {

	@FXML
	private Button btnSave;
	
	private static Button ref_btnSave;
	private static Button ref_backup_btn;
	
	@FXML
	private DatePicker datepicker_fromdate;

	@FXML
	private DatePicker datepicker_todate;

	@FXML
	private TitledPane 	titlepanebackupDB;
	@FXML
	private AnchorPane 	backup_results_childpane;

	@FXML
	private Spinner<LocalTime> spinner_from_time;

	@FXML
	private Spinner<LocalTime> spinner_to_time;

	@FXML
	private Button backup_btn;

	@FXML
	private CheckBox checkbox_purge_data;

	@FXML
	private TextField txt_backup_file_location;

	@FXML
	private Button backup_file_destination_btn;

	@FXML
	private TextField txt_sql_location;

	@FXML
	private Button sql_location_btn;

	@FXML
	private RadioButton radioBtnResults;
	@FXML
	private RadioButton radioBtnDatabase;





	public static String sql_backup_location;
	public static String sql_location;

	public void initialize(URL location, ResourceBundle resources) {



		ApplicationLauncher.logger.info("initialize");
		ref_btnSave = btnSave;
		ref_backup_btn = backup_btn;
		LoadSavedData();
		radioBtnResults.setSelected(true);
		radioBtnDatabase.setSelected(false);
		checkbox_purge_data.setDisable(false);
		initializeSpinner(LocalTime.now(), spinner_from_time);
		initializeSpinner(LocalTime.now(), spinner_to_time);
		
		if(ProcalFeatureEnable.USER_ACCESS_CONTROL_ENABLED){
			applyUacSettings();
		}

		// TODO Auto-generated method stub

	}

	public void radioBtnDatabaseOnChange(){
		if (radioBtnDatabase.isSelected()){
			checkbox_purge_data.selectedProperty().setValue(false);
			checkbox_purge_data.setDisable(true);
			datepicker_fromdate.setDisable(true);
			spinner_from_time.setDisable(true);
			datepicker_todate.setDisable(true);
			spinner_to_time.setDisable(true);
			radioBtnResults.setSelected(false);
		}else{

		}

	}

	public void radioBtnResultsOnChange(){
		if (radioBtnResults.isSelected()){
			checkbox_purge_data.setDisable(false);
			datepicker_fromdate.setDisable(false);
			spinner_from_time.setDisable(false);
			datepicker_todate.setDisable(false);
			spinner_to_time.setDisable(false);
			radioBtnDatabase.setSelected(false);
		}else{

		}
	}

	public void LoadSavedData(){

		JSONObject data = MySQL_Controller.sp_getbackup_file_location();
		try {
			if(!data.isNull("backup_folder_location")){
				String backup_file = data.getString("backup_folder_location");
				String sql_file_loc = data.getString("sql_server_location");
				sql_backup_location=backup_file;
				sql_location=sql_file_loc;
				backup_file = backup_file.replace("\\\\", "\\");
				sql_file_loc = sql_file_loc.replace("\\\\", "\\");
				txt_backup_file_location.setText(backup_file);
				txt_sql_location.setText(sql_file_loc);
			}
			else{
				txt_backup_file_location.setText("");
				txt_sql_location.setText("");	
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("BackupDBController: LoadSavedData : JSONException:" +e.getMessage());
			
		}
	}


	public void save_backup_location() throws IOException{

		String backup_file_location = txt_backup_file_location.getText();
		String sql_file_location = txt_sql_location.getText();
		backup_file_location = backup_file_location.replace("\\", "\\\\");
		sql_file_location = sql_file_location.replace("\\", "\\\\");
		MySQL_Controller.sp_add_backup_file_location(sql_backup_location, sql_location);
		InformUser("Saved Successfully", "Saved data successfully", AlertType.INFORMATION);

	}

	public void backupdata_onclick() {//throws ParseException, JSONException{

		boolean status = true;
		String Error = "";
		ApplicationLauncher.logger.info("from time: " + spinner_from_time.getValue());
		ApplicationLauncher.logger.info("to time: " + spinner_to_time.getValue());
		String fromdate = "";
		String todate="";
		if(datepicker_fromdate.getValue()!=null) {
			fromdate = datepicker_fromdate.getValue().toString() +" " + spinner_from_time.getValue();
		}
		if(datepicker_todate.getValue()!=null) {
			todate = datepicker_todate.getValue().toString() + " " + spinner_to_time.getValue();
		}

		ApplicationLauncher.logger.info("backupdata_onclick: fromdate: " + fromdate);
		ApplicationLauncher.logger.info("backupdata_onclick: todate: " + todate);



		if((!fromdate.isEmpty()) && (!todate.isEmpty())){
			String fromdateName = fromdate.replaceAll(":", "");
			String todateName = todate.replaceAll(":", "");
			fromdateName = fromdateName.replaceAll("-", "");
			fromdateName = fromdateName.replaceAll(" ", "_");
			fromdateName = fromdateName.substring(0, fromdateName.indexOf("."));
			todateName = todateName.replaceAll("-", "");
			todateName = todateName.replaceAll(" ", "_");
			todateName = todateName.substring(0, todateName.indexOf("."));
			ApplicationLauncher.logger.info("backupdata_onclick: fromdateName: " + fromdateName);
			ApplicationLauncher.logger.info("backupdata_onclick: todateName: " + todateName);
			long from_epoch = 0;
			try {
				from_epoch = calcEpoch(fromdate);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				ApplicationLauncher.logger.error("backupdata_onclick: ParseException: " + e1.getMessage());
			}
			long to_epoch = 0;
			try {
				to_epoch = calcEpoch(todate);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				ApplicationLauncher.logger.error("backupdata_onclick: ParseException: " + e1.getMessage());
			}

			String fromtime = Long.toString(from_epoch);
			String totime = Long.toString(to_epoch);

			ApplicationLauncher.logger.info("selected given dateand time initial "+fromtime );
			ApplicationLauncher.logger.info("selected given dateand time final "+totime );
			if (radioBtnResults.isSelected()){
				ApplicationLauncher.logger.info("Results radio button selected");
				try {

					sql_backup_through_cmd(fromtime,totime,fromdateName,todateName);



				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ApplicationLauncher.logger.error("backupdata_onclick : Exception3:"+e.getMessage() );
					Error = e.getMessage();
					status = false;
				}

				if(checkbox_purge_data.isSelected()){
					ApplicationLauncher.logger.info("checkbox_purge_data is selected " );
					MySQL_Controller.sp_delete_result_data(from_epoch,to_epoch);

				}
			}


		}else if (radioBtnDatabase.isSelected()){

			ApplicationLauncher.logger.info("DataBase radio button selected");
			try {
				sql_DB_Backup_through_cmd();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("backupdata_onclick : Exception4:"+e.getMessage() );
			}

		}else{
			ApplicationLauncher.logger.info("<From Date> or <To Date> should not be empty!!");
			InformUser(ConstantVersion.APPLICATION_NAME , "<From Date> or <To Date> should not be empty!!",AlertType.ERROR);

		}

	}

	public void backup_folder_selection_onclick(){
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedDirectory = 
				directoryChooser.showDialog(new Stage());

		if(selectedDirectory == null){
			ApplicationLauncher.logger.info("No Directory selected");
			txt_backup_file_location.setText("");

		}else{
			ApplicationLauncher.logger.info("showSaveDialog file location"+selectedDirectory.getAbsolutePath());
			txt_backup_file_location.setText(selectedDirectory.getAbsolutePath() + "\\");
			String save_backup_file_location = txt_backup_file_location.getText();
			sql_backup_location=save_backup_file_location;

		}
	}

	public void sql_location_onclick(){
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedDirectory = 
				directoryChooser.showDialog(new Stage());

		if(selectedDirectory == null){
			ApplicationLauncher.logger.info("No Directory selected");
			txt_sql_location.setText("");

		}else{
			ApplicationLauncher.logger.info("showSaveDialog file location"+selectedDirectory.getAbsolutePath());
			txt_sql_location.setText(selectedDirectory.getAbsolutePath());
			String save_sql_file_location = txt_sql_location.getText();
			sql_location=save_sql_file_location;

		}
	}

	public long calcEpoch(String Date_time) throws ParseException{
		long epoch = 0;
		//String str = "2014-07-04 04:05:10";   // UTC
		String str = Date_time;   // UTC

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date datenew = df.parse(str);
		epoch = datenew.getTime() /1000;

		ApplicationLauncher.logger.info(epoch);
		return epoch;
	}
	public static void sql_backup_through_cmd(String fromtime,String totime, String fromdateName,String todateName) throws Exception {
		String time_stamp="\"time_stamp>= "+fromtime+" AND time_stamp<= "+totime+"\"";
		String epoch_time="\"epoch_start_time>= "+fromtime+" AND epoch_end_time<= "+totime+"\"";
		int i=0;
		String.valueOf(i);
		String sqlURL=ConstantAppConfig.DB_URL+ConstantAppConfig.DB_NAME;
		ApplicationLauncher.logger.info("DB URL: "+ConstantAppConfig.DB_URL+ConstantAppConfig.DB_NAME);
		String DBname=(sqlURL.substring(sqlURL.lastIndexOf("/") + 1));

		String DBusername=ConstantAppConfig.DB_USERNAME;
		String DBpassword=ConstantAppConfig.DB_PASSWORD;

		ApplicationLauncher.logger.info("sql_location"+sql_location);
		ApplicationLauncher.logger.info("SQL_BACKUP_LOCATION"+sql_backup_location);
		String Selected_time = fromdateName + "-"+todateName;

		String sql_backup_filename="backup_results"+Selected_time+".sql";
		String sql_project_run_backup_filename="backup_projectrun"+Selected_time+".sql";
		String sql_backup_merged_filename="backupMerged_data"+Selected_time+".sql";
		ProcessBuilder builder1 = new ProcessBuilder(
				//C:\Program Files\MySQL\MySQL Server 5.5\bin
				"cmd.exe", "/c", "cd "+sql_location+" &&mysqldump -u"+DBusername+" -p"+DBpassword+" --no-create-info --skip-triggers "+DBname+" results --where="+time_stamp+" -r "+sql_backup_location+sql_backup_filename);
		//mysqldump -uroot -pprasanth@tas --no-create-info --skip-triggers ltcalibration results --where=
		//"time_stamp>= 1528702011 AND time_stamp<= 1528702011" -r E:\backup.sql

		builder1.redirectErrorStream(true);

		Process p = builder1.start();

		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));

		String line1;
		line1 = r.readLine();


		ProcessBuilder builder2 = new ProcessBuilder(
				//C:\Program Files\MySQL\MySQL Server 5.5\bin
				"cmd.exe", "/c", "cd "+sql_location+" &&mysqldump -u"+DBusername+" -p"+DBpassword+" --no-create-info --skip-triggers "+DBname+" project_run --where="+epoch_time+" -r "+sql_backup_location+sql_project_run_backup_filename);
		//mysqldump -uroot -pprasanth@tas --no-create-info --skip-triggers ltcalibration results --where=
		//"time_stamp>= 1528702011 AND time_stamp<= 1528702011" -r E:\backup.sql

		builder2.redirectErrorStream(true);

		Process p2 = builder2.start();

		BufferedReader r2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));

		String line2;
		line2 = r2.readLine();
		if(line1!=null){
			if(line1.contains("Warning")){
				line1="";
			}
		}
		if(line2!=null){
			if(line2.contains("Warning")){
				line2="";
			}
		}
		ApplicationLauncher.logger.info("sql_backup_through_cmd : line1: "+line1);
		ApplicationLauncher.logger.info("sql_backup_through_cmd : line2: "+line2);
		try{
			if (line1 == null && line2 == null) {  // with MySQLserver 5.5 
				ApplicationLauncher.logger.info("sql_backup_through_cmd: DB Backup finished successfully");
				InformUser(ConstantVersion.APPLICATION_NAME , "DB Backup finished successfully",AlertType.INFORMATION);

			} else if (line1.isEmpty() && line2.isEmpty()) { // with MySQLserver 5.7 
				ApplicationLauncher.logger.info("sql_backup_through_cmd: Database Backup finished successfully");
				InformUser(ConstantVersion.APPLICATION_NAME , "Database Backup finished successfully",AlertType.INFORMATION);

			}
			else{
				ApplicationLauncher.logger.info("sql_backup_through_cmd: line:"+line1+line2);
				InformUser(ConstantVersion.APPLICATION_NAME , "Database Backup :"+line1+line2,AlertType.INFORMATION);

			}
		}catch(Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("sql_backup_through_cmd: Exception:"+e.getMessage());
			InformUser(ConstantVersion.APPLICATION_NAME , "Database Backup Exception :"+line1+line2,AlertType.ERROR);
		}

		ProcessBuilder builder3 = new ProcessBuilder(
				//C:\Program Files\MySQL\MySQL Server 5.5\bin
				"cmd.exe", "/c", "cd "+"C:\\testbackup"+" &&"+"type "+sql_backup_filename+" "+sql_project_run_backup_filename+">"+sql_backup_merged_filename);
		//mysqldump -uroot -pprasanth@tas --no-create-info --skip-triggers ltcalibration results --where=
		//"time_stamp>= 1528702011 AND time_stamp<= 1528702011" -r E:\backup.sql

		builder3.redirectErrorStream(true);

		Process p3 = builder3.start();

		BufferedReader r3 = new BufferedReader(new InputStreamReader(p3.getInputStream()));


		String line3;
		line3 = r3.readLine();
		ApplicationLauncher.logger.info("line3 merge:"+line3);



	}


	public static void sql_DB_Backup_through_cmd() throws Exception {

		int i=0;
		String.valueOf(i);
		String sqlURL=ConstantAppConfig.DB_URL+ConstantAppConfig.DB_NAME;
		ApplicationLauncher.logger.info("sql_DB_Backup_through_cmd:DB URL: "+ConstantAppConfig.DB_URL+ConstantAppConfig.DB_NAME);
		String DBname=(sqlURL.substring(sqlURL.lastIndexOf("/") + 1));

		String DBusername=ConstantAppConfig.DB_USERNAME;
		String DBpassword=ConstantAppConfig.DB_PASSWORD;
		try {
			Files.createDirectories(Paths.get(sql_backup_location));
		}catch(Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("sql_DB_Backup_through_cmd: folder path creation Exception:"+e.getMessage());
			
		}


		ApplicationLauncher.logger.info("sql_DB_Backup_through_cmd:sql_location:"+sql_location);
		ApplicationLauncher.logger.info("sql_DB_Backup_through_cmd: SQL_BACKUP_LOCATION:"+sql_backup_location);
		String current_time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());;

		String sql_backup_filename="backupDB_"+ConstantAppConfig.DB_NAME+current_time+".sql";
		ProcessBuilder builder1 = new ProcessBuilder(
				//C:\Program Files\MySQL\MySQL Server 5.5\bin
				"cmd.exe", "/c", "cd "+sql_location+" &&mysqldump -u"+DBusername+" -p"+DBpassword+" --no-create-info --skip-triggers "+DBname+" -r "+sql_backup_location+sql_backup_filename);
		//mysqldump -uroot -pprasanth@tas --no-create-info --skip-triggers ltcalibration results --where=
		//"time_stamp>= 1528702011 AND time_stamp<= 1528702011" -r E:\backup.sql

		builder1.redirectErrorStream(true);

		Process p = builder1.start();

		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));

		String line1;
		line1 = r.readLine();

		if(line1!=null){
			if(line1.contains("Warning")){
				line1="";
			}
		}

		ApplicationLauncher.logger.info("sql_DB_Backup_through_cmd : line1: "+line1);
		try{
			if (line1 == null ) {  // with MySQLserver 5.5 
				ApplicationLauncher.logger.info("sql_DB_Backup_through_cmd: DB Backup finished successfully");
				InformUser(ConstantVersion.APPLICATION_NAME , "DB Backup finished successfully",AlertType.INFORMATION);

			} else if (line1.isEmpty() ) { // with MySQLserver 5.7 
				ApplicationLauncher.logger.info("sql_DB_Backup_through_cmd: Database Backup finished successfully");
				InformUser(ConstantVersion.APPLICATION_NAME , "Database Backup finished successfully",AlertType.INFORMATION);

			}
			else{
				ApplicationLauncher.logger.info("sql_DB_Backup_through_cmd: line:"+line1);
				InformUser(ConstantVersion.APPLICATION_NAME , "Database Backup :"+line1,AlertType.INFORMATION);

			}
		}catch(Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("sql_DB_Backup_through_cmd: Exception:"+e.getMessage());
			InformUser(ConstantVersion.APPLICATION_NAME , "Database Backup Exception :"+line1,AlertType.ERROR);
		}



	}


	enum Mode {

		HOURS {
			@Override
			LocalTime increment(LocalTime time, int steps) {
				return time.plusHours(steps);
			}
			@Override
			void select(Spinner<LocalTime> spinner) {
				int index = spinner.getEditor().getText().indexOf(':');
				spinner.getEditor().selectRange(0, index);
			}
		},
		MINUTES {
			@Override
			LocalTime increment(LocalTime time, int steps) {
				return time.plusMinutes(steps);
			}
			@Override
			void select(Spinner<LocalTime> spinner) {
				int hrIndex = spinner.getEditor().getText().indexOf(':');
				int minIndex = spinner.getEditor().getText().indexOf(':', hrIndex + 1);
				spinner.getEditor().selectRange(hrIndex+1, minIndex);
			}
		},
		SECONDS {
			@Override
			LocalTime increment(LocalTime time, int steps) {
				return time.plusSeconds(steps);
			}
			@Override
			void select(Spinner<LocalTime> spinner) {
				int index = spinner.getEditor().getText().lastIndexOf(':');
				spinner.getEditor().selectRange(index+1, spinner.getEditor().getText().length());
			}
		};
		abstract LocalTime increment(LocalTime time, int steps);
		abstract void select(Spinner<LocalTime> spinner);
		LocalTime decrement(LocalTime time, int steps) {
			return increment(time, -steps);
		}
	}

	// Property containing the current editing mode:

	private final ObjectProperty<Mode> mode = new SimpleObjectProperty<>(Mode.HOURS) ;

	public ObjectProperty<Mode> modeProperty() {
		return mode;
	}

	public final Mode getMode() {
		return modeProperty().get();
	}

	public final void setMode(Mode mode) {
		modeProperty().set(mode);
	}


	public void initializeSpinner(LocalTime time, Spinner<LocalTime> spinner) {
		spinner.setEditable(true);

		// Create a StringConverter for converting between the text in the
		// editor and the actual value:

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

		StringConverter<LocalTime> localTimeConverter = new StringConverter<LocalTime>() {

			@Override
			public String toString(LocalTime time) {
				return formatter.format(time);
			}

			@Override
			public LocalTime fromString(String string) {
				String[] tokens = string.split(":");
				int hours = getIntField(tokens, 0);
				int minutes = getIntField(tokens, 1) ;
				int seconds = getIntField(tokens, 2);
				int totalSeconds = (hours * 60 + minutes) * 60 + seconds ;
				return LocalTime.of((totalSeconds / 3600) % 24, (totalSeconds / 60) % 60, seconds % 60);
			}

			private int getIntField(String[] tokens, int index) {
				if (tokens.length <= index || tokens[index].isEmpty()) {
					return 0 ;
				}
				return Integer.parseInt(tokens[index]);
			}

		};

		// The textFormatter both manages the text <-> LocalTime conversion,
		// and vetoes any edits that are not valid. We just make sure we have
		// two colons and only digits in between:

		TextFormatter<LocalTime> textFormatter = new TextFormatter<LocalTime>(localTimeConverter, LocalTime.now(), c -> {
			String newText = c.getControlNewText();
			if (newText.matches("[0-9]{0,2}:[0-9]{0,2}:[0-9]{0,2}")) {
				return c ;
			}
			return null ;
		});

		// The spinner value factory defines increment and decrement by
		// delegating to the current editing mode:

		SpinnerValueFactory<LocalTime> valueFactory = new SpinnerValueFactory<LocalTime>() {
			{
				setConverter(localTimeConverter);
				setValue(time);
			}

			@Override
			public void decrement(int steps) {
				setValue(mode.get().decrement(getValue(), steps));
				mode.get().select(spinner);
			}

			@Override
			public void increment(int steps) {
				setValue(mode.get().increment(getValue(), steps));
				mode.get().select(spinner);
			}

		};

		spinner.setValueFactory(valueFactory);
		spinner.getEditor().setTextFormatter(textFormatter);

		// Update the mode when the user interacts with the editor.
		// This is a bit of a hack, e.g. calling spinner.getEditor().positionCaret()
		// could result in incorrect state. Directly observing the caretPostion
		// didn't work well though; getting that to work properly might be
		// a better approach in the long run.
		spinner.getEditor().addEventHandler(InputEvent.ANY, e -> {
			int caretPos = spinner.getEditor().getCaretPosition();
			int hrIndex = spinner.getEditor().getText().indexOf(':');
			int minIndex = spinner.getEditor().getText().indexOf(':', hrIndex + 1);
			if (caretPos <= hrIndex) {
				mode.set( Mode.HOURS );
			} else if (caretPos <= minIndex) {
				mode.set( Mode.MINUTES );
			} else {
				mode.set( Mode.SECONDS );
			}
		});

		DateTimeFormatter consoleFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
		spinner.valueProperty().addListener((obs, oldTime, newTime) -> 
		ApplicationLauncher.logger.info(consoleFormatter.format(newTime)));

		// When the mode changes, select the new portion:
		mode.addListener((obs, oldMode, newMode) -> newMode.select(spinner));

	}
	public static void InformUser(String title, String info,AlertType Alert_type){
		TextBoxDialog TextBoxDialogobj = new TextBoxDialog();
		TextBoxDialogobj.TriggerUserInfoPlatFormLater(title, info, Alert_type);
	}
	
	private static void applyUacSettings() {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.info("BackupDBController : applyUacSettings :  Entry");
		ArrayList<UacDataModel> uacSelectProfileScreenList = DeviceDataManagerController.getUacSelectProfileScreenList();
		String screenName = "";
		for (int i = 0; i < uacSelectProfileScreenList.size(); i++){

			screenName = uacSelectProfileScreenList.get(i).getScreenName();
			switch (screenName) {
				case ConstantApp.UAC_BACKUP_SETTINGS_SCREEN:
					
					
					if(!uacSelectProfileScreenList.get(i).getExecutePossible()){
						//ref_btn_deploy.setDisable(true);
						
					}
					
					if(!uacSelectProfileScreenList.get(i).getAddPossible()){
						//ref_btn_Create.setDisable(true);
						
					}
					
					if(!uacSelectProfileScreenList.get(i).getUpdatePossible()){
						//ref_vbox_testscript.setDisable(true);sdvsc
						//setChildPropertySaveEnabled(false);
						ref_btnSave.setDisable(true);
						ref_backup_btn.setDisable(true);
						
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
