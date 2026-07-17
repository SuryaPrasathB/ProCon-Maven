package com.tasnetwork.calibration.energymeter.reportprofile;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.lang3.StringUtils;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.calib.LscsCalibrationConfigLoader;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdConfigLoader;
import com.tasnetwork.calibration.energymeter.constant.ConstantReportV2;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.testreport.ExcelCellValueModel;
import com.tasnetwork.calibration.energymeter.util.ErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;
import com.tasnetwork.calibration.energymeter.util.TextAreaInputDialog;
import com.tasnetwork.calibration.energymeter.util.TextFieldInputDialog;
import com.tasnetwork.spring.orm.model.OperationParam;
import com.tasnetwork.spring.orm.model.ReportProfileTestDataFilter;
import com.tasnetwork.spring.orm.model.RpPrintPosition;
import com.tasnetwork.spring.orm.service.OperationParamService;
import com.tasnetwork.spring.orm.service.OperationProcessService;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class OperationParamProfileController implements Initializable{


	OperationProcessJsonReadModel  operationProcessDataModel = DeviceDataManagerController.getReportProfileConfigParsedKey();
	static private OperationParamService reportOperationParamService = DeviceDataManagerController.getRpOperationParamService();

	@FXML private ComboBox cmbBxOperationParamProfileName;
	@FXML private Button btnLoad;
	@FXML private Button btnAddParamProfile;
	@FXML private Button btnCancel;
	@FXML private Button btnSave;
	@FXML private Button btnImport;
	@FXML private TableView<OperationParam> tvOperationParamProfile;
	@FXML private TableColumn<OperationParam, String> columnSerialNo;
	@FXML private TableColumn columnParamType;
	@FXML private TableColumn columnParamKeyName;
	@FXML private TableColumn columnParamPopulateType;



	public static ComboBox ref_cmbBxOperationParamProfileName;
	public static Button ref_btnLoad;
	public static Button ref_btnAddParamProfile;
	public static Button ref_btnCancel;
	public static Button ref_btnSave;
	public static Button ref_btnImport;
	public static TableView<OperationParam> ref_tvOperationParamProfile;
	public static TableColumn<OperationParam, String> ref_columnSerialNo;
	public static TableColumn ref_columnParamType;
	public static TableColumn<OperationParam, String> ref_columnParamKeyName;
	public static TableColumn ref_columnParamPopulateType;

	private AtomicInteger serialNo = new AtomicInteger(0) ;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		refAssignment();

		loadDefaultData();
		loadDataFromDatabase();
		loadTableViewProperty();
	}

	private void loadDataFromDatabase() {
		// TODO Auto-generated method stub
		
		ApplicationLauncher.logger.debug("loadDataFromDatabase: Entry ");
		
		String customerId = ConstantAppConfig.REPORT_PROFILE_DEFAULT_ACTIVE_CUSTOMER_ID;
		List<OperationParam> operationParamDataList = getReportOperationParamService().findByCustomerId(customerId);
		
		Set<String> paramProfileSet = new HashSet<String>();
		
		operationParamDataList.stream().forEach( e -> {
			//e.getOperationParamProfileName();
			paramProfileSet.add(e.getOperationParamProfileName());
		});
		ArrayList<String> sortedParamProfile = new ArrayList<String> (paramProfileSet);
		Collections.sort(sortedParamProfile);
		
		ref_cmbBxOperationParamProfileName.getItems().addAll(sortedParamProfile);
		ref_cmbBxOperationParamProfileName.getSelectionModel().selectFirst();
		
	}

	private void loadTableViewProperty() {

		ApplicationLauncher.logger.debug("loadTableViewProperty: Entry ");

		//getTableSerialNoProperty()

		ref_tvOperationParamProfile.setEditable(true);
		ref_columnSerialNo.setCellValueFactory( cellData -> cellData.getValue().getTableSerialNoProperty());
		ref_columnParamPopulateType.setCellValueFactory(new OperationParamPopulateTypeListComboBoxValueFactory());
		ref_columnParamType.setCellValueFactory(new OperationParamTypeListComboBoxValueFactory());

		/*		ref_columnParamKeyName.setCellValueFactory(new PropertyValueFactory<OperationParam, String>("keyParam"));
		ref_columnParamKeyName.setCellFactory(TextFieldTableCell.forTableColumn());		
		ref_columnParamKeyName.setOnEditCommit(new EventHandler<CellEditEvent<OperationParam, String>>() {
			public void handle(CellEditEvent<OperationParam, String> t) {
				OperationParam rowData = ((OperationParam) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				if(t.getNewValue() != null){
					rowData.setKeyParam(t.getNewValue());
				}

			}
		});*/




		ref_columnParamKeyName.setCellValueFactory(cellData -> cellData.getValue().getKeyParamProperty());
		ref_columnParamKeyName.setCellFactory(TextFieldTableCell.forTableColumn());		
		ref_columnParamKeyName.setOnEditCommit(new EventHandler<CellEditEvent<OperationParam, String>>() {
			public void handle(CellEditEvent<OperationParam, String> t) {
				OperationParam rowData = ((OperationParam) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				ApplicationLauncher.logger.debug("loadTableViewProperty: getNewValue: " + t.getNewValue());
				if(t.getNewValue() != null){
					rowData.setKeyParam(t.getNewValue());
				}

			}
		});


	}

	private void loadDefaultData() {
		// TODO Auto-generated method stub

		/*		RpPrintPosition dataProperty = new RpPrintPosition();

		String keyParamName1 = dataProperty.getKeyParam().getClass().getName();
		String keyParamName2 = dataProperty.getKeyParam().getClass().getSimpleName();

		ApplicationLauncher.logger.debug("OperationParamProfileController: loadDefaultData: keyParamName1: " + keyParamName1);
		ApplicationLauncher.logger.debug("OperationParamProfileController: loadDefaultData: keyParamName2: " + keyParamName2);
		 */
	}


	private void refAssignment() {
		// TODO Auto-generated method stub


		ref_cmbBxOperationParamProfileName = cmbBxOperationParamProfileName;
		ref_btnLoad = btnLoad;
		ref_btnAddParamProfile = btnAddParamProfile;
		ref_btnCancel = btnCancel;
		ref_btnSave = btnSave;
		ref_btnImport = btnImport;
		ref_tvOperationParamProfile = tvOperationParamProfile;
		ref_columnSerialNo = columnSerialNo;
		ref_columnParamType = columnParamType;
		ref_columnParamKeyName = columnParamKeyName;
		ref_columnParamPopulateType = columnParamPopulateType;

	}

	@FXML 
	public void btnAddParamProfileOnClick(){
		ApplicationLauncher.logger.debug("btnAddParamProfileOnClick: Entry ");
		Platform.runLater(() -> {

			// TextAreaInputDialog dialog = new TextAreaInputDialog();
			//TextFieldInputDialog dialog = new TextFieldInputDialog();

			/*	            dialog.setHeaderText("Enter ParamProfile Name");
	            dialog.setTitle("ParamProfile");
	            dialog.setGraphic(null);
	           // dialog.eee
	            // Show the dialog and capture the result.
	            Optional result = dialog.showAndWait();

	            // If the "Okay" button was clicked, the result will contain our String in the get() method
	            if (result.isPresent()) {
	                //System.out.println(result.get());

	                ApplicationLauncher.logger.debug("btnAddOnClick: result.get(): " + result.get());
	                ref_cmbBxOperationParamProfileName.getItems().add(result.get());
	                ref_cmbBxOperationParamProfileName.getSelectionModel().select(result.get());
	                ref_tvOperationParamProfile.getItems().clear();
	                getSerialNo().set(1);
	            }*/

			String header = "ParamProfile Name";
			String title  = "Enter ParamProfile";
			String userInputData =  GuiUtils.textFieldInputDialogDisplay(header,title);

			if (!userInputData.isEmpty()) {
				//System.out.println(result.get());

				ApplicationLauncher.logger.debug("btnAddParamProfileOnClick: userInputData: " + userInputData);
				ref_cmbBxOperationParamProfileName.getItems().add(userInputData);
				ref_cmbBxOperationParamProfileName.getSelectionModel().select(userInputData);
				ref_tvOperationParamProfile.getItems().clear();
				getSerialNo().set(0);
				//setPopulateType(ConstantReportV2.POPULATE_DATA_TYPE_ONLY_HEADERS);
				//ref_tvOperationParamProfile.getItems().clear();
			}

		});
	}

	@FXML
	public void addNewParamKeyOnClick(){
		ApplicationLauncher.logger.debug("addNewParamKeyOnClick: Entry ");
		String paramProfileName = (String) ref_cmbBxOperationParamProfileName.getSelectionModel().getSelectedItem();
		if(paramProfileName!=null){

			if(!paramProfileName.isEmpty()){
				OperationParam defaultData = new OperationParam();
				String customerId = ConstantAppConfig.REPORT_PROFILE_DEFAULT_ACTIVE_CUSTOMER_ID;
				defaultData.setTableSerialNo(String.valueOf(getSerialNo().incrementAndGet()));
				defaultData.setCustomerId(customerId);
				defaultData.setPopulateType(ConstantReportV2.POPULATE_DATA_TYPE_ALL_DUT);
				defaultData.setParamType(ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_INPUT);
				//e.setOperationParamProfileName(paramProfileName)
				ref_tvOperationParamProfile.getItems().add(defaultData);
			}else{
				ApplicationLauncher.logger.debug("btnSaveOnClick: paramProfileName is empty,Param Profile name is empty, kindly update and try again!!!  - prompted");
				//ApplicationLauncher.InformUser(ErrorCodeMapping.ERROR_CODE_6000, ErrorCodeMapping.ERROR_CODE_6000_MSG,AlertType.ERROR);
				ApplicationLauncher.InformUser("Empty Param Profile Name", "Param Profile name is empty, kindly update and try again!!!",AlertType.ERROR);

			}
		}else {
			ApplicationLauncher.logger.debug("btnSaveOnClick: Param Profile Name Null, Param Profile name is null, kindly update and try again!!! - prompted");
			//ApplicationLauncher.InformUser(ErrorCodeMapping.ERROR_CODE_6000, ErrorCodeMapping.ERROR_CODE_6000_MSG,AlertType.ERROR);
			ApplicationLauncher.InformUser("Param Profile Name Null", "Param Profile name is null, kindly update and try again!!!" ,AlertType.ERROR);

		}
	}

	public AtomicInteger getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(AtomicInteger serialNo) {
		this.serialNo = serialNo;
	}



	@FXML
	public void btnSaveOnClick(){
		ApplicationLauncher.logger.debug("btnSaveOnClick: Entry ");
		if(ref_tvOperationParamProfile.getItems().size()>0){
			String paramProfileName = (String) ref_cmbBxOperationParamProfileName.getSelectionModel().getSelectedItem();
			if(paramProfileName!=null){

				if(!paramProfileName.isEmpty()){
					String customerId = ConstantAppConfig.REPORT_PROFILE_DEFAULT_ACTIVE_CUSTOMER_ID;
/*					ref_tvOperationParamProfile.getItems().stream().forEach( e -> {
						ApplicationLauncher.logger.debug("btnSaveOnClick: Entry " + e.getTableSerialNo() + " -> key = <" + e.getKeyParam() + "> , getPopulateType -> " + e.getPopulateType() + " , paramType = "  + e.getParamType() );
						if(e.getPopulateType().equals(ConstantReportV2.POPULATE_DATA_TYPE_ONLY_HEADERS)){

							e.setPopulateOnlyHeaders(true);
							e.setPopulateAllDut(false);
						}else if(e.getPopulateType().equals(ConstantReportV2.POPULATE_DATA_TYPE_ALL_DUT)){
							e.setPopulateOnlyHeaders(false);
							e.setPopulateAllDut(true);
						}
						e.setOperationParamProfileName(paramProfileName);
						e.setCustomerId(customerId);
					});*/

					ArrayList<String> keyParamEmptySerialNoList = (ArrayList<String> )ref_tvOperationParamProfile.getItems().stream()
							.filter( e -> e.getKeyParam().isEmpty())
							.map(e -> e.getTableSerialNo())
							.collect(Collectors.toList());
					if(keyParamEmptySerialNoList.size()>0){

						ApplicationLauncher.logger.debug("btnSaveOnClick: keyParamEmptySerialNoList: " + keyParamEmptySerialNoList);
						//ApplicationLauncher.InformUser(ErrorCodeMapping.ERROR_CODE_6000, ErrorCodeMapping.ERROR_CODE_6000_MSG,AlertType.ERROR);
						ApplicationLauncher.InformUser("Empty KeyParam found", "Emtpy <Key Name> found in below serial number:\n\nSerial No list: " + StringUtils.join(keyParamEmptySerialNoList, ","),AlertType.ERROR);

					}else{

						ArrayList<OperationParam> keyParamDataList = (ArrayList<OperationParam> )ref_tvOperationParamProfile.getItems().stream()
								//.map(e -> e.getKeyParam())
								.collect(Collectors.toList());
						//Map<String, List<Integer>> dataMap = IntStream.range(0, keyParamDataList.size())
						//										.boxed()
						//										.collect(Collectors.groupingBy(keyParamDataList :: get));

						List<OperationParam> duplicateKeyNameList =  keyParamDataList.stream()
								.collect(Collectors.groupingBy(OperationParam::getKeyParam ))
								//.entrySet().stream()
								.values().stream()
								//.filter( e -> e.getValue().size() > 1)
								.filter( e -> e.size() > 1)
								.flatMap(List::stream)
								.collect(Collectors.toList());

						MultiValuedMap<String, String> duplicateKeyNameListMap = new ArrayListValuedHashMap<String,String>();
						List<String> duplicateSerialNoList = new ArrayList<String>();
						duplicateKeyNameList.stream()
						.forEach( e-> {
							ApplicationLauncher.logger.debug("btnSaveOnClick: dulicateKeyParamMap: " + e.getTableSerialNo() + " -> " + e.getKeyParam());
							duplicateKeyNameListMap.put(e.getKeyParam(),e.getTableSerialNo());
							//duplicateSerialNoList.add(e.getTableSerialNo());
						});

						if(duplicateKeyNameList.size()>0){
							ApplicationLauncher.logger.debug("btnSaveOnClick: Duplicate key Name entries found: ");

							ArrayList<String> collatedData = new ArrayList<String>();
							duplicateKeyNameListMap.keySet().stream().forEach( e -> {
								String data = "Duplicate Key Name: " + e + " , Serial No List: " + (Collection<String>)duplicateKeyNameListMap.get(e);
								collatedData.add(data);
							});

							//ApplicationLauncher.InformUser(ErrorCodeMapping.ERROR_CODE_6000, ErrorCodeMapping.ERROR_CODE_6000_MSG,AlertType.ERROR);
							ApplicationLauncher.InformUser("Empty KeyParam found", "Duplicate <Key Name> found:\n\n" + StringUtils.join(collatedData,"\n"),AlertType.ERROR);

						}else{


							ref_tvOperationParamProfile.getItems().stream().forEach( e -> {
								ApplicationLauncher.logger.debug("btnSaveOnClick: Entry " + e.getTableSerialNo() + " -> key = <" + e.getKeyParam() + "> , getPopulateType -> " + e.getPopulateType() + " , paramType = "  + e.getParamType() );
								if(e.getPopulateType().equals(ConstantReportV2.POPULATE_DATA_TYPE_ONLY_HEADERS)){

									e.setPopulateOnlyHeaders(true);
									e.setPopulateAllDut(false);
									//e.setResultTypeAverage(false);
								}else if(e.getPopulateType().equals(ConstantReportV2.POPULATE_DATA_TYPE_ALL_DUT)){
									e.setPopulateOnlyHeaders(false);
									e.setPopulateAllDut(true);
									//e.setResultTypeAverage(false);
								}else if(e.getPopulateType().equals(ConstantReportV2.POPULATE_DATA_TYPE_ALL_DUT_AVERAGE)){
									e.setPopulateOnlyHeaders(false);
									e.setPopulateAllDut(true);
									//e.setResultTypeAverage(true);
								}
								
								
								e.setOperationParamProfileName(paramProfileName);
								e.setCustomerId(customerId);
								getReportOperationParamService().saveToDb(e);
							});
							
							ApplicationLauncher.logger.info("btnSaveOnClick: Success: Saved to Database" );
							//
							ApplicationLauncher.InformUser("Success", "Saved to Database" ,AlertType.INFORMATION);


						}

						//groupByKeyParamMap.values().stream()
						//							.filter( e > e.size()>1)

						//ArrayList<OperationParam> groupByKeyParamDataList = (Map) keyParamDataList.stream()
						//		.collect(Collectors.groupingBy(OperationParam::getKeyParam ));

					}
				}
			}
		}

	}

	public static OperationParamService getReportOperationParamService() {
		return reportOperationParamService;
	}

	public static void setReportOperationParamService(OperationParamService reportOperationParamService) {
		OperationParamProfileController.reportOperationParamService = reportOperationParamService;
	}

	@FXML 
	public void cmbBxOperationParamProfileNameOnChange(){
		ApplicationLauncher.logger.debug("cmbBxOperationParamProfileNameOnChange: Entry ");
		ref_tvOperationParamProfile.getItems().clear();
		//String customerId = ConstantAppConfig.REPORT_PROFILE_DEFAULT_ACTIVE_CUSTOMER_ID;
		
		//List<OperationParam> operationParamDataList = getReportOperationParamService().findByCustomerId(customerId);
	}
	
	@FXML 
	public void btnImportOnClick(){
		ApplicationLauncher.logger.debug("btnImportOnClick: Entry ");
		
		ArrayList<String> existingParamKeyList =(ArrayList<String>) ref_tvOperationParamProfile.getItems().stream()
													.map( e ->  e.getKeyParam()).collect(Collectors.toList());
		
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Select json file location");
		FileChooser.ExtensionFilter extFilter = 
				 new FileChooser.ExtensionFilter("Json files","*.json");//  new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt");
		
		chooser.getExtensionFilters().add(extFilter);
		File file = chooser.showOpenDialog(new Stage());
		
		String fileName = file.getName();
		String filePath = file.getParent() + "\\";
		ApplicationLauncher.logger.info("btnImportOnClick: file location: "+file);
		ApplicationLauncher.logger.info("btnImportOnClick: fileName: "+fileName);
		ApplicationLauncher.logger.info("btnImportOnClick: filePath: "+filePath);
		String file_location=file.toString();
		
		
		ConstantAppConfig.REPORT_PROFILEV2_FILE_PATH = filePath;
		ConstantAppConfig.REPORT_PROFILEV2_FILE_NAME = fileName;
		
		ReportProfileOperationConfigLoader.setConfigFilePathName(ConstantAppConfig.REPORT_PROFILEV2_FILE_PATH ,ConstantAppConfig.REPORT_PROFILEV2_FILE_NAME);
		
		ReportProfileOperationConfigLoader.init3();
		
		getOperationProcessDataModel().getOperationLocalInput().forEach( e-> {
			ApplicationLauncher.logger.info("btnImportOnClick: localInput : getOperationProcessKey: "+ e.getOperationProcessKey());
			
			if(!existingParamKeyList.contains(e.getOperationProcessKey())){
				
				OperationParam newParamData = new OperationParam();
				newParamData.setKeyParam(e.getOperationProcessKey());
				if(e.isPopulateOnlyHeaders()){
					newParamData.setPopulateAllDut(false);
					newParamData.setPopulateOnlyHeaders(true);
					newParamData.setPopulateType(ConstantReportV2.POPULATE_DATA_TYPE_ONLY_HEADERS);
				}else{
					newParamData.setPopulateAllDut(true);
					newParamData.setPopulateOnlyHeaders(false);
					newParamData.setPopulateType(ConstantReportV2.POPULATE_DATA_TYPE_ALL_DUT);
				}
				newParamData.setParamType(ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_INPUT);
				newParamData.setTableSerialNo(String.valueOf(getSerialNo().incrementAndGet()));
				ref_tvOperationParamProfile.getItems().add(newParamData);
			}
		});
		
		
		getOperationProcessDataModel().getOperationLocalOutput().forEach( e-> {
			ApplicationLauncher.logger.info("btnImportOnClick: localOutput: getOperationProcessKey: "+ e.getOperationProcessKey());
			
			if(!existingParamKeyList.contains(e.getOperationProcessKey())){
				
				OperationParam newParamData = new OperationParam();
				newParamData.setKeyParam(e.getOperationProcessKey());
				if(e.isPopulateOnlyHeaders()){
					newParamData.setPopulateAllDut(false);
					newParamData.setPopulateOnlyHeaders(true);
					newParamData.setPopulateType(ConstantReportV2.POPULATE_DATA_TYPE_ONLY_HEADERS);
				}else{
					newParamData.setPopulateAllDut(true);
					newParamData.setPopulateOnlyHeaders(false);
					newParamData.setPopulateType(ConstantReportV2.POPULATE_DATA_TYPE_ALL_DUT);
				}
				newParamData.setParamType(ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_OUTPUT);
				newParamData.setTableSerialNo(String.valueOf(getSerialNo().incrementAndGet()));
				ref_tvOperationParamProfile.getItems().add(newParamData);
			}
		});
		
		
		getOperationProcessDataModel().getOperationMasterOutput().forEach( e-> {
			ApplicationLauncher.logger.info("btnImportOnClick: masterOutput: getOperationProcessKey: "+ e.getOperationProcessKey());
			
			if(!existingParamKeyList.contains(e.getOperationProcessKey())){
				
				OperationParam newParamData = new OperationParam();
				newParamData.setKeyParam(e.getOperationProcessKey());
				if(e.isPopulateOnlyHeaders()){
					newParamData.setPopulateAllDut(false);
					newParamData.setPopulateOnlyHeaders(true);
					newParamData.setPopulateType(ConstantReportV2.POPULATE_DATA_TYPE_ONLY_HEADERS);
				}else{
					newParamData.setPopulateAllDut(true);
					newParamData.setPopulateOnlyHeaders(false);
					newParamData.setPopulateType(ConstantReportV2.POPULATE_DATA_TYPE_ALL_DUT);
				}
				newParamData.setParamType(ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_MASTER_OUTPUT);
				newParamData.setTableSerialNo(String.valueOf(getSerialNo().incrementAndGet()));
				ref_tvOperationParamProfile.getItems().add(newParamData);
			}
		});
		
		//ApplicationLauncher.logger.debug("btnImportOnClick: file_location: " + file_location);
		
	}
	
	
	@FXML 
	public void btnLoadOnClick(){
		ApplicationLauncher.logger.debug("btnLoadOnClick: Entry ");
		ref_tvOperationParamProfile.getItems().clear();
		String customerId = ConstantAppConfig.REPORT_PROFILE_DEFAULT_ACTIVE_CUSTOMER_ID;
		String paramProfileName = (String) ref_cmbBxOperationParamProfileName.getSelectionModel().getSelectedItem();
		List<OperationParam> operationParamDataList = getReportOperationParamService().
								findByCustomerIdAndOperationParamProfileName(customerId,paramProfileName);
		//int maxSerialNo = 0;
		setSerialNo(new AtomicInteger(0));
		operationParamDataList.stream().forEach( e -> {
			if(e.isPopulateOnlyHeaders()){
				e.setPopulateType(ConstantReportV2.POPULATE_DATA_TYPE_ONLY_HEADERS);
			}
			
			/*else if(e.isResultTypeAverage()){
				e.setPopulateType(ConstantReportV2.POPULATE_DATA_TYPE_ALL_DUT_AVERAGE);
			}*/
			
			else if(e.isPopulateAllDut()){
				e.setPopulateType(ConstantReportV2.POPULATE_DATA_TYPE_ALL_DUT);
			}
			
			ApplicationLauncher.logger.debug("btnLoadOnClick: getPopulateType: " + e.getPopulateType());
			if(Integer.parseInt(e.getTableSerialNo()) > getSerialNo().get()){
				setSerialNo(new AtomicInteger(Integer.parseInt(e.getTableSerialNo())));
			}
			ref_tvOperationParamProfile.getItems().add(e);
		});
	}
	
	@FXML 
	public void btnDeleteOnClick(){
		ApplicationLauncher.logger.debug("btnDeleteOnClick: Entry ");
		
		int selectedIndex = ref_tvOperationParamProfile.getSelectionModel().getSelectedIndex();
		
		if(selectedIndex!=-1){
			OperationParam selectedOperationParam = ref_tvOperationParamProfile.getSelectionModel().getSelectedItem();
			
			
			try{
				
				ref_tvOperationParamProfile.getItems().remove(selectedOperationParam);
				
				int toBeRemovedId = selectedOperationParam.getId();
				
				getReportOperationParamService().removeById(toBeRemovedId);
				ApplicationLauncher.logger.debug("btnDeleteOnClick: item deleted from db: " + selectedOperationParam.getKeyParam());
				reOrderSerialNumberOperationParam();
				ref_tvOperationParamProfile.getItems().stream().forEach( e -> {
					ApplicationLauncher.logger.debug("btnDeleteOnClick: Entry " + e.getTableSerialNo() + " -> key = <" + e.getKeyParam() + "> , getPopulateType -> " + e.getPopulateType() + " , paramType = "  + e.getParamType() );
					if(e.getPopulateType().equals(ConstantReportV2.POPULATE_DATA_TYPE_ONLY_HEADERS)){

						e.setPopulateOnlyHeaders(true);
						e.setPopulateAllDut(false);
					}else if(e.getPopulateType().equals(ConstantReportV2.POPULATE_DATA_TYPE_ALL_DUT)){
						e.setPopulateOnlyHeaders(false);
						e.setPopulateAllDut(true);
					}
					//e.setOperationParamProfileName(paramProfileName);
					//e.setCustomerId(customerId);
					getReportOperationParamService().saveToDb(e);
				});
				
			}catch (Exception E){
				
				ApplicationLauncher.logger.error("btnDeleteOnClick: Exception: " + E.getMessage());
				//return false;
			}
			
		}else{
			ApplicationLauncher.logger.debug("btnDeleteOnClick: items not selected for delete");
		}
		
	}
	
	
	private void reOrderSerialNumberOperationParam() {
		// TODO Auto-generated method stub
		
		ApplicationLauncher.logger.debug("reOrderSerialNumberOperationParam: Entry");
		//List<ReportMeterMetaDataTypeSubModel> reportMeterMetaDataTypeSubModel = tableViewObj.getItems();
		///int serialNumber = 1;
		getSerialNo().set(0);
		for(int i =0 ; i <ref_tvOperationParamProfile.getItems().size(); i++){
			
			//ApplicationLauncher.logger.debug("reOrderSerialNumberTestFilter : getDataTypeKey: " + tableViewObj.getItems().get(i).getDataTypeKey());
			//ApplicationLauncher.logger.debug("reOrderSerialNumberTestFilter : serialNumber: " + serialNumber);
			ref_tvOperationParamProfile.getItems().get(i).setTableSerialNo(String.valueOf(getSerialNo().incrementAndGet()));
			//ApplicationLauncher.logger.debug("reOrderSerialNumberTestFilter : serialNumber2: " + ref_tvOperationParamProfile.getItems().get(i).getTableSerialNo());
			//serialNumber++;
		}
		
	}

	public OperationProcessJsonReadModel getOperationProcessDataModel() {
		return operationProcessDataModel;
	}

	public void setOperationProcessDataModel(OperationProcessJsonReadModel operationProcessDataModel) {
		this.operationProcessDataModel = operationProcessDataModel;
	}
}
