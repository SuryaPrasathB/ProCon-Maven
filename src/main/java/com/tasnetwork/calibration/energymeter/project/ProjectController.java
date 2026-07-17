package com.tasnetwork.calibration.energymeter.project;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
import com.tasnetwork.calibration.energymeter.database.MySQL_Interface;
import com.tasnetwork.calibration.energymeter.deployment.DeploymentTestCaseDataModel;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.testprofiles.DragContainer;
import com.tasnetwork.calibration.energymeter.testprofiles.DraggableTestNode;
import com.tasnetwork.calibration.energymeter.testprofiles.TestDataTypeEnum;
import com.tasnetwork.calibration.energymeter.testprofiles.TestProfileType;
import com.tasnetwork.calibration.energymeter.testprofiles.TestSetupCheckBoxValueFactory;
import com.tasnetwork.calibration.energymeter.testprofiles.TestSetupDataModel;
import com.tasnetwork.calibration.energymeter.testprofiles.TestSetupSummaryCheckBoxValueFactory;
import com.tasnetwork.calibration.energymeter.testprofiles.TestSetupSummaryDataModel;
import com.tasnetwork.calibration.energymeter.testreport.TestReportController;
import com.tasnetwork.calibration.energymeter.uac.UacDataModel;
import com.tasnetwork.calibration.energymeter.util.ComboBoxAutoComplete;
import com.tasnetwork.spring.orm.model.DutCommand;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ProjectController extends AnchorPane {

	
	private static int DutCommandCounter = 0;

	private static boolean childPropertySaveEnabled = true;

	@FXML AnchorPane testScriptRootPane;

	@FXML AnchorPane AnchorPaneTemplate;
	@FXML AnchorPane AnchorPaneTC;
	@FXML AnchorPane AnchorPaneSummary;
	@FXML AnchorPane AnchorInnerPaneSummary;
	@FXML AnchorPane AnchorPaneProperty;

	@FXML SplitPane testScriptSplitPane;

	@FXML ScrollPane scrollPaneTemplate;
	@FXML ScrollPane scrollPaneTC;

	@FXML VBox testScriptLeftPane;
	@FXML VBox testScriptRightPane;


	@FXML AnchorPane testScriptPropertyChildPane;

	@FXML
	private TitledPane titledSummaryPane;

	@FXML
	private TitledPane titledPaneTemplate;

	@FXML
	private TitledPane titledPaneProperty;

	@FXML
	private TitledPane titledPaneTC;

	private static String ToBeSavedProjectName;

	private static Button ref_btn_Create;
	private static Button ref_btn_Save;
	private static Button ref_btn_Delete;

	@FXML
	private TableView<TestSetupSummaryDataModel> SummaryTable;
	public static TableView<TestSetupSummaryDataModel> ref_SummaryTable;



	@FXML
	private TableColumn testCaseSelectedColumn;
	public static TableColumn ref_testCaseSelectedColumn;

	@FXML
	private TableColumn<TestSetupSummaryDataModel, String> testCaseNameColumn;
	public static TableColumn<TestSetupSummaryDataModel, String> ref_testCaseNameColumn;

	@FXML
	private TableColumn<TestSetupSummaryDataModel, String> testCaseSerialno;
	public static TableColumn<TestSetupSummaryDataModel, String> ref_testCaseSerialno;

	@FXML
	private TextField txt_FileName;



	@FXML
	private Button btn_Create;

	@FXML
	private Button btn_Save;

	@FXML
	private Button btn_Delete;

	@FXML
	private Button btn_Load;

	@FXML
	private Button btn_Load_Project;

	@FXML ComboBox<String> cmbBox_ChooseProject;
	@FXML 
	private static ComboBox<String> ref_cmbBox_ChooseProject;

	@FXML ComboBox<String> cmbBox_model_list;
	@FXML 
	private static ComboBox<String> ref_cmbBox_model_list;
	private DraggableTestNode mDragOverIcon = null;

	private EventHandler<DragEvent> mIconDragOverRoot = null;
	private EventHandler<DragEvent> mIconDragDropped = null;
	private EventHandler<DragEvent> mIconDragOverRightPane = null;
	public static ObservableList<TestSetupSummaryDataModel> SummaryData = FXCollections.observableArrayList();

	Timer LoadProjectTaskTimer;
	private static String ProjectName = null;

	static List<DraggableTestNode> NodeList = new ArrayList<DraggableTestNode>();

	private static int positionCounterId = 0;

	private static int WarmupCounter = 0;
	private static int CreepCounter = 0;
	private static int STACounter = 0;

	private static int RepeatabilityCounter = 0;
	private static int SelfHeatingCounter = 0;
	private static int ConstTestCounter = 0;
	private static int CustomRatingCounter = 0;
	private static int AccuracyCounter = 0;
	private static int InfluenceVoltCounter = 0;
	private static int InfluenceFreqCounter = 0;
	private static int InfluenceHarmonicCounter = 0;
	private static int CuttingNuetralCounter = 0;
	private static int VoltageUnbalanceCounter = 0;
	private static int PhaseReversalCounter = 0;

	private static ArrayList<String> model_id_list = null;
	private static ArrayList<String> model_list = null;

	private String TestCaseType = null;

	public static String ProjectEM_ModelType = "";
	
	public static String ProjectEM_CT_Type = "";

	private static ObservableList<Object> sequencedata = FXCollections.observableArrayList();
	private static int sequence_no = 0;
	private static ArrayList<String> TestPointsNotSaved = new ArrayList<String>();

	public ProjectController() {

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/project/Project" + ConstantApp.THEME_FXML));

		fxmlLoader.setRoot(this); 
		fxmlLoader.setController(this);

		try { 
			fxmlLoader.load();

		} catch (IOException exception) {
			exception.printStackTrace();
			ApplicationLauncher.logger.error("ProjectController: IOException:"+exception.getMessage());
			throw new RuntimeException(exception);
		}
	}

	public static String getProjectEM_ModelType(){
		return ProjectEM_ModelType;

	}



	public static void setProjectEM_ModelType(String type){

		ApplicationLauncher.logger.debug("setProjectEM_ModelType:"+type);
		ProjectEM_ModelType = type;

	}	
	
	
	public static String getProjectEM_CT_Type(){
		return ProjectEM_CT_Type;

	}



	public static void setProjectEM_CT_Type(String type){

		ApplicationLauncher.logger.debug("setProjectEM_CT_Type:"+type);
		ProjectEM_CT_Type = type;

	}

	public static void setmodel_id_list(ArrayList<String> model_id){
		model_id_list = model_id;
	}

	public static ArrayList<String> getmodel_id_list(){
		return model_id_list;
	}

	public static void setmodel_list(ArrayList<String> models){
		model_list = models;
	}

	public static ArrayList<String> getmodel_list(){
		return model_list;
	}


	@FXML
	public void initialize() {
		ApplicationLauncher.logger.info("Test script layout controller");
		ref_cmbBox_ChooseProject = cmbBox_ChooseProject;
		ref_cmbBox_model_list = cmbBox_model_list;

		mDragOverIcon = new DraggableTestNode(testScriptPropertyChildPane);

		mDragOverIcon.setVisible(false);
		mDragOverIcon.setOpacity(0.65);
		testScriptRootPane.getChildren().add(mDragOverIcon);

		String ID =  String.format("%02d", 0);
		ref_SummaryTable = SummaryTable;
		ref_testCaseNameColumn = testCaseNameColumn;
		ref_testCaseSelectedColumn = testCaseSelectedColumn;
		ref_testCaseSerialno = testCaseSerialno;
		ref_btn_Create = btn_Create;
		ref_btn_Save = btn_Save;
		ref_btn_Delete = btn_Delete;

		buildDragHandlers();
		ApplicationLauncher.logger.debug("ProjectController: DragIconType: Length :"+TestProfileType.values().length);
		//populate left pane with multiple colored icons for testing
		for (int i = 0; i < (TestProfileType.values().length); i++) {


			DraggableTestNode refTestNode = new DraggableTestNode(testScriptPropertyChildPane);
			refTestNode.setParentScriptController(this);
			addDragDetection(refTestNode);

			refTestNode.setType(TestProfileType.values()[i]);
			refTestNode.setAliasId(ID);
			ApplicationLauncher.logger.debug("ProjectController: initialize: DragIconType.values()[i]):"+TestProfileType.values()[i].toString());
			boolean IsFeatureEnabledInConfig = false;
			try{
				IsFeatureEnabledInConfig = ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.getBoolean(TestProfileType.values()[i].toString());
			}catch (Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("ProjectController: initialize: Exception :"+e.getMessage());

			}
			if(IsFeatureEnabledInConfig){
				testScriptLeftPane.getChildren().add(refTestNode);
			}
		}

		ref_SummaryTable.setEditable(true);
		ref_testCaseNameColumn.setCellValueFactory(cellData -> cellData.getValue().testDataProperty());
		ref_testCaseSelectedColumn.setCellValueFactory(new TestSetupSummaryCheckBoxValueFactory());
		ref_testCaseSerialno.setCellValueFactory(cellData -> cellData.getValue().testDataSerialNoProperty());
		try {
			LoadProjectList();
			new ComboBoxAutoComplete<String>(ref_cmbBox_ChooseProject);
			LoadModelList(ref_cmbBox_model_list);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("ProjectController: JSONException:"+e.getMessage());
		}
		ProjectAdjustScreen();
		if((ConstantApp.USER_ACCESS_LEVEL.equals(ConstantApp.TESTER_ACCESS_LEVEL)) ||
				(ConstantApp.USER_ACCESS_LEVEL.equals(ConstantApp.READONLY_ACCESS_LEVEL))){
			btn_Save.setDisable(true);
			btn_Create.setDisable(true);
			AnchorPaneTemplate.setDisable(true);
			btn_Delete.setDisable(true);

		}
		
		if(ProcalFeatureEnable.USER_ACCESS_CONTROL_ENABLED){
			applyUacSettings();
		}

	}
	public void onNodeDoubleClicked(MouseEvent event) {

		DraggableTestNode origTestNode = (DraggableTestNode) event.getSource();
		String parentContainer = origTestNode.getParent().getId();
		if (!parentContainer.equals("testScriptLeftPane")) {

			return;

		}
		System.out.println ("Handling left pane double click...");
		DraggableTestNode node = new DraggableTestNode(testScriptPropertyChildPane);
		testScriptSplitPane.setOnDragOver(mIconDragOverRoot);
		testScriptRightPane.setOnDragOver(mIconDragOverRightPane);
		testScriptRightPane.setOnDragDropped(mIconDragDropped);
		node.setType(origTestNode.getType());
		node.setTitle(origTestNode.getTitle());
		node.setColor();
		double nodeOffset = (testScriptRightPane.getChildren().size() * 60);
		node.setDragOffSet(new Point2D(450, nodeOffset));
		node.setNodeIndex(testScriptRightPane.getChildren().size());
		node.NewNode(true);
		String aliasId =  String.format("%02d", Update_counterValue(node.getTitle()));
		node.setAliasId(aliasId);
		setAliasName(node); //Added after double click integration
		node.setProjectName(getProjectName());
		positionCounterId = positionCounterId + 1; //Added after double click integration
		node.setPositionId(positionCounterId); //Added after double click integration
		System.out.println("Double clicked AliasId: " + aliasId);
		testScriptRightPane.getChildren().add(node);
		node.relocateToPoint(new Point2D(450, nodeOffset));
		SaveNode(node);


	}

	public void ProjectAdjustScreen(){

		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		double ScreenHeight = primaryScreenBounds.getHeight();
		double ScreenWidth= primaryScreenBounds.getWidth();
		double SplitPaneOffset = 76;
		double BottomStatusPaneOffset = 56;
		double HeaderOffset = 30;

		double TemplateWidthOffset = 232;
		double TC_WidthOffset = 225;
		double SummaryWidthOffset = 230;
		double LeftPaneWidthOffset = 110;
		long ScreenWidthThreshold = 1500;
		long ScreenHeightThreshold = 1000;
		ConstantApp MyPropertyObj= new ConstantApp();
		ScreenWidthThreshold = ConstantAppConfig.ScreenWidthThreshold;
		ScreenHeightThreshold = ConstantAppConfig.ScreenHeightThreshold;
		ApplicationLauncher.logger.info("ProjectAdjustScreen : ScreenWidthThreshold:"+ScreenWidthThreshold);
		ApplicationLauncher.logger.info("ProjectAdjustScreen : ScreenHeight:"+ScreenHeight);
		ApplicationLauncher.logger.info("ProjectAdjustScreen : ScreenWidth:"+ScreenWidth);

		Double TemplateHeight = ScreenHeight -BottomStatusPaneOffset;
		testScriptRootPane.setMinHeight(TemplateHeight);


		titledPaneTemplate.setMinHeight(TemplateHeight-SplitPaneOffset);
		AnchorPaneTemplate.setMinHeight(TemplateHeight-SplitPaneOffset-HeaderOffset);
		scrollPaneTemplate.setMinHeight(TemplateHeight-SplitPaneOffset-HeaderOffset);//-SplitPaneOffset-HeaderOffset);

		titledPaneTC.setMinHeight(TemplateHeight-SplitPaneOffset);
		AnchorPaneTC.setMinHeight(TemplateHeight-SplitPaneOffset-HeaderOffset);
		scrollPaneTC.setMinHeight(TemplateHeight-SplitPaneOffset-HeaderOffset);

		AnchorPaneSummary.setMinHeight(TemplateHeight-SplitPaneOffset);
		titledSummaryPane.setMinHeight(TemplateHeight-SplitPaneOffset);
		AnchorInnerPaneSummary.setMinHeight(TemplateHeight-SplitPaneOffset-HeaderOffset);
		SummaryTable.setMinHeight(TemplateHeight-SplitPaneOffset-HeaderOffset);


		AnchorPaneProperty.setMinHeight(TemplateHeight-SplitPaneOffset);
		titledPaneProperty.setMinHeight(TemplateHeight-SplitPaneOffset);
		testScriptPropertyChildPane.setMinHeight(TemplateHeight-SplitPaneOffset-HeaderOffset);


		if(ScreenWidth >ScreenWidthThreshold){
			SummaryWidthOffset = 400;
			AnchorPaneSummary.setPrefWidth(SummaryWidthOffset);
			titledSummaryPane.setPrefWidth(SummaryWidthOffset);
			AnchorInnerPaneSummary.setPrefWidth(SummaryWidthOffset);
			SummaryTable.setPrefWidth(SummaryWidthOffset);
			testCaseNameColumn.setMinWidth(350);
		}

		Double PropertyWidth = ScreenWidth-TemplateWidthOffset-TC_WidthOffset-SummaryWidthOffset-120;
		testScriptRootPane.setPrefWidth(ScreenWidth-LeftPaneWidthOffset);
		AnchorPaneProperty.setPrefWidth(PropertyWidth);
		titledPaneProperty.setPrefWidth(PropertyWidth);
		testScriptPropertyChildPane.setPrefWidth(PropertyWidth);



	}

	public static void LoadModelList(ComboBox cmbBox) throws JSONException{
		JSONObject model_data = MySQL_Controller.sp_getem_model_list();


		ArrayList<String> model_list = new ArrayList<String>();
		ArrayList<String> model_id_list = new ArrayList<String>();
		
		JSONArray models = model_data.getJSONArray("EM_models");

		//JSONArray models = new JSONArray();
		JSONObject model = new JSONObject();
		String model_id = "";
		String model_name = "";

		for(int i=0; i<models.length(); i++){
			model = (JSONObject) models.get(i);
			model_id = (String) model.get("model_id");
			model_name = (String) model.get("model_name");
			model_list.add(model_name);
			model_id_list.add(model_id);
		}

		setmodel_id_list(model_id_list);
		setmodel_list(model_list);

		ApplicationLauncher.logger.info("Energy Meter model list: " + model_list);
		cmbBox.getItems().clear();
		cmbBox.getItems().addAll(model_list);
		cmbBox.getSelectionModel().select(0);
		cmbBox.setVisible(true);

	}

	public static void UpdateNewTestPointSummaryDataToDB(String projectname, String input_testcasename,  String testType,String AliasID) {

		//ApplicationLauncher.logger.info("UpdateSummaryData : Entry ");
		//String input_testcasename = TestCase;
		//ApplicationLauncher.logger.info("UpdateNewTestPointSummaryDataToDB: sequence_no:"+sequence_no);
		//ApplicationLauncher.logger.info("UpdateNewTestPointSummaryDataToDB: SummaryData.size():"+SummaryData.size());
		Boolean data_exists = false;
		String testCaseName = "";
		for(int i =0; i< SummaryData.size(); i++){


/*			TestSetupSummaryDataModel data = SummaryData.get(i);
			String testCaseName = data.getTestData();*/
			//TestSetupSummaryDataModel data = SummaryData.get(i);
			testCaseName = SummaryData.get(i).getTestData();
			//ApplicationLauncher.logger.info("UpdateNewTestPointSummaryDataToDB: Table testCaseName: " + testCaseName + " , input_testcasename:" + input_testcasename);
			if(testCaseName.equals(input_testcasename)){
				data_exists = true;
				break;
			}
		}
		if(!data_exists){
			//ApplicationLauncher.logger.info("UpdateNewTestPointSummaryDataToDB: !data_exists: Entry :"+input_testcasename);
			//String string_sequence_no= Integer.toString(sequence_no+1);
			//SummaryData.add(new TestSetupSummaryDataModel(Integer.toString(sequence_no+1),input_testcasename,Boolean.FALSE));
			//ref_SummaryTable.setItems(SummaryData);
			//ref_SummaryTable.refresh();
			sequence_no++;
			//ApplicationLauncher.logger.info("UpdateSummaryData: sequence_no: "  + sequence_no);
			MySQL_Controller.sp_add_summary_data(projectname, input_testcasename, testType, AliasID, sequence_no);
			//ApplicationLauncher.logger.info("UpdateNewTestPointSummaryDataToDB: Exit sequence_no: "  + sequence_no);
		}

	}
	
/*	public static void UpdateSummaryDataOnGUI(String projectname, String TestCase,  String testType,String AliasID) {

		ApplicationLauncher.logger.info("UpdateSummaryDataOnGUI : Entry ");
		String input_testcasename = TestCase;
		Boolean data_exists = false;
		for(int i =0; i< SummaryData.size(); i++){


			TestSetupSummaryDataModel data = SummaryData.get(i);
			String testCaseName = data.getTestData();
			//ApplicationLauncher.logger.info("testCaseName: " + testCaseName + " input_testcasename:" + input_testcasename);
			if(testCaseName.equals(input_testcasename)){
				data_exists = true;
				break;
			}
		}
		if(!data_exists){
			ApplicationLauncher.logger.info("UpdateSummaryDataOnGUI: !data_exists: Entry ");
			//String string_sequence_no= Integer.toString(sequence_no+1);
			SummaryData.add(new TestSetupSummaryDataModel(Integer.toString(sequence_no+1),TestCase,Boolean.FALSE));
			ref_SummaryTable.setItems(SummaryData);
			sequence_no++;
			//ApplicationLauncher.logger.info("UpdateSummaryData: sequence_no: "  + sequence_no);
			//MySQL_Controller.sp_add_summary_data(projectname, TestCase, testType, AliasID, sequence_no);
		}

	}*/

	public void SetModelNameOnUI(final String ModelName){
		if (ModelName!=null){
			Platform.runLater(() -> {
				cmbBox_model_list.getSelectionModel().select(ModelName);
			});
		}else{
			
			Platform.runLater(() -> {
				cmbBox_model_list.getSelectionModel().select(0);
			});
		}
	}
	
	

	public static void refreshSummaryPaneData(String testCase, String aliasId) {
		if (testCase.equals("all")) {
			SummaryData.clear();
			sequence_no = 0;
		}
		else {
			ApplicationLauncher.logger.info("UpdateSummaryData: Deleted ");
			UpdateSummaryDB_IfDataNotExist(getProjectName());

		}

	}

	private void addDragDetection(DraggableTestNode dragIcon) {

		dragIcon.setOnDragDetected (new EventHandler <MouseEvent> () {

			@Override
			public void handle(MouseEvent event) {
				ApplicationLauncher.logger.info ("Root node -> drag detected on draggable node event recieved...");

				// set drag event handlers on their respective objects
				testScriptSplitPane.setOnDragOver(mIconDragOverRoot);
				testScriptRightPane.setOnDragOver(mIconDragOverRightPane);
				testScriptRightPane.setOnDragDropped(mIconDragDropped);

				// get a reference to the clicked DragIcon object
				DraggableTestNode origTestNode = (DraggableTestNode) event.getSource();

				//begin drag ops
				mDragOverIcon.setType(origTestNode.getType());
				mDragOverIcon.relocateToPoint(new Point2D (event.getSceneX(), event.getSceneY()));

				ClipboardContent content = new ClipboardContent();
				DragContainer container = new DragContainer();

				container.addData ("type", mDragOverIcon.getType().toString());
				container.addData ("nodeParent", origTestNode.getParent().getId());
				content.put(DragContainer.AddNode, container);

				mDragOverIcon.startDragAndDrop (TransferMode.ANY).setContent(content);
				mDragOverIcon.setVisible(true);
				mDragOverIcon.setMouseTransparent(true);
				event.consume();					
			}
		});
	}	

	private void buildDragHandlers() {
		//drag over transition to move widget form left pane to right pane
		mIconDragOverRoot = new EventHandler <DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				//ApplicationLauncher.logger.info ("Base pane -> drag over event recieved...");

				Point2D p = testScriptRightPane.sceneToLocal(event.getSceneX(), event.getSceneY());

				//turn on transfer mode and track in the right-pane's context 
				//if (and only if) the mouse cursor falls within the right pane's bounds.
				if (!testScriptRightPane.boundsInLocalProperty().get().contains(p)) {
					event.acceptTransferModes(TransferMode.ANY);
					mDragOverIcon.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
					return;
				}
				event.consume();
			}
		};

		mIconDragOverRightPane = new EventHandler <DragEvent> () {
			@Override
			public void handle(DragEvent event) {
				//ApplicationLauncher.logger.info ("Base pane -> drag over event recieved...");

				event.acceptTransferModes(TransferMode.ANY);
				mDragOverIcon.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
				event.consume();
			}
		};

		mIconDragDropped = new EventHandler <DragEvent> () {
			@Override
			public void handle(DragEvent event) {
				ApplicationLauncher.logger.info ("Right pane -> drag dropped event recieved...");

				DragContainer container = 
						(DragContainer) event.getDragboard().getContent(DragContainer.AddNode);

				Point2D dropLocation = new Point2D(event.getSceneX(), event.getSceneY());
				container.addData("scene_coords", dropLocation);

				ClipboardContent content = new ClipboardContent();
				content.put(DragContainer.AddNode, container);

				event.getDragboard().setContent(content);
				event.setDropCompleted(true);
				event.consume();
			}
		};

		testScriptRootPane.setOnDragDone(new EventHandler <DragEvent> () {

			@Override
			public void handle (DragEvent event) {
				ApplicationLauncher.logger.info ("Right pane -> drag done event recieved...");

				testScriptRightPane.removeEventHandler(DragEvent.DRAG_OVER, mIconDragOverRightPane);
				testScriptRightPane.removeEventHandler(DragEvent.DRAG_DROPPED, mIconDragDropped);
				testScriptSplitPane.removeEventHandler(DragEvent.DRAG_OVER, mIconDragOverRoot);

				mDragOverIcon.setVisible(false);

				DragContainer container = 
						(DragContainer) event.getDragboard().getContent(DragContainer.AddNode);

				if (container != null) {
					if (container.getValue("scene_coords") != null) {
						String containerType = container.getValue("type");

						DraggableTestNode node = new DraggableTestNode(testScriptPropertyChildPane);
						node.setType(TestProfileType.valueOf(containerType));
						Point2D cursorPoint = container.getValue("scene_coords");

						node.setColor();
						node.setDragOffSet(new Point2D(cursorPoint.getX(), cursorPoint.getY()));
						ApplicationLauncher.logger.info ("Drag complete for:" + node.getTitle());
						ApplicationLauncher.logger.info ("Moved the node: scene_coords:" + container.getValue("type"));

						String originalNodeParent = container.getValue("nodeParent");
						ApplicationLauncher.logger.info ("Original gesture object :" + event.getGestureSource());
						DraggableTestNode originalDragNode = (DraggableTestNode) event.getGestureSource();

						if (originalDragNode != null && originalNodeParent.equals("testScriptRightPane")) {
							ApplicationLauncher.logger.info ("Replacing original right pane draggable node " + originalDragNode.getTitle());	

							originalDragNode.setDragOffSet(new Point2D(cursorPoint.getX(), cursorPoint.getY()));
							originalDragNode.relocateToPoint(new Point2D(cursorPoint.getX(), cursorPoint.getY()));

							List<Node> sortedList = sortDraggableTestNodes(testScriptRightPane.getChildren(), cursorPoint, originalDragNode);
							ApplicationLauncher.logger.info("Drggable node sorting completed");

							testScriptRightPane.getChildren().clear();
							testScriptRightPane.getChildren().addAll(sortedList);
						}
						else {
							testScriptRightPane.getChildren().add(node);
							node.relocateToPoint(new Point2D(cursorPoint.getX(), cursorPoint.getY()));
							node.setNodeIndex(testScriptRightPane.getChildren().size());
							node.NewNode(true);
						}

						String aliasId =  String.format("%02d", Update_counterValue(node.getTitle()));
						node.setAliasId(aliasId);
						setAliasName(node);
						ApplicationLauncher.logger.info("setOnDragDone: local getProjectName: " + getProjectName());
						node.setProjectName(getProjectName());
						positionCounterId = positionCounterId + 1;
						node.setPositionId(positionCounterId);
						ApplicationLauncher.logger.info("AliasId: " + aliasId);
						addTestPoint(node.getType().toString(), aliasId);
						if (testScriptPropertyChildPane.getChildren().size() == 0) {
							node.onDragNodeClick();
						}

						SaveNode(node);


					}
				}

				container = (DragContainer) event.getDragboard().getContent(DragContainer.DragNode);
				if (container != null) {
					if (container.getValue("type") != null) {
						ApplicationLauncher.logger.info ("Moved node " + container.getValue("type"));
					}
				}
				event.consume();
			}
		});
	}

	public DraggableTestNode setAliasName(DraggableTestNode node){
		TestProfileType testtype = node.getType();
		switch (testtype) {

		case STA:
			node.setAliasName(ConstantApp.STA_ALIAS_NAME);
			break;

		case Warmup:
			node.setAliasName(ConstantApp.WARMUP_ALIAS_NAME);
			break;

		case NoLoad:
			node.setAliasName(ConstantApp.CREEP_ALIAS_NAME);
			break;

		case Accuracy:
			node.setAliasName(ConstantApp.ACCURACY_ALIAS_NAME);
			break;

		case InfluenceVolt:
			node.setAliasName(ConstantApp.VOLTAGE_ALIAS_NAME);
			break;

		case InfluenceFreq:
			node.setAliasName(ConstantApp.FREQUENCY_ALIAS_NAME);
			break;

		case InfluenceHarmonic:
			node.setAliasName(ConstantApp.HARMONIC_WITHOUT_ALIAS_NAME);
			break;

		case CuttingNuetral:
			node.setAliasName(ConstantApp.CUTNEUTRAL_ALIAS_NAME);
			break;

		case VoltageUnbalance:
			node.setAliasName(ConstantApp.VOLT_UNBALANCE_ALIAS_NAME);
			break;

		case PhaseReversal:
			node.setAliasName(ConstantApp.PHASEREVERSAL_REV_ALIAS_NAME);
			break;

		case Repeatability:
			node.setAliasName(ConstantApp.REPEATABILITY_ALIAS_NAME);
			break;

		case SelfHeating:
			node.setAliasName(ConstantApp.SELF_HEATING_ALIAS_NAME);
			break;

		case ConstantTest:
			node.setAliasName(ConstantApp.CONST_TEST_ALIAS_NAME);
			break;

		case CustomTest:
			break;
			
		case DutCommand:
			break;

		default:
			break;
		}
		return node;
	}


	private List<Node> sortDraggableTestNodes(List<Node> nodeList, Point2D newLocation, DraggableTestNode relocatingNode) {
		List<Node> retList = FXCollections.observableArrayList() ;
		ApplicationLauncher.logger.info("Total node count: " + nodeList.size());
		boolean nodeAdded = false;
		int mouseLocationOffset = 10;
		int nodeSize = 50;
		boolean ascendingOrder = true;

		int newIndexLocation = (int) ((newLocation.getY() + mouseLocationOffset) / nodeSize);
		if (newIndexLocation == 0) {
			newIndexLocation = 1;
		}
		ApplicationLauncher.logger.info("Node " + relocatingNode.getTitle() + " is moving from " + relocatingNode.getNodeIndex() + " to " + newIndexLocation);

		int relocatingNodeIndex = relocatingNode.getNodeIndex();
		if (newIndexLocation < relocatingNodeIndex) {
			ascendingOrder = false;
		}
		int relocatedNodeIndex = 0;
		int updatedNodeIndex = 0;

		for (Node node : nodeList) {
			DraggableTestNode dragNode = (DraggableTestNode)node;

			if ((ascendingOrder && dragNode.getNodeIndex() == relocatingNodeIndex) ||
					(!ascendingOrder && dragNode.getNodeIndex() == relocatedNodeIndex)) {
				ApplicationLauncher.logger.info("Skiping the drag node of old location: " + dragNode.getNodeIndex());
				continue;
			}
			if (dragNode.getNodeIndex() < newIndexLocation) {
				dragNode.setNodeIndex(++updatedNodeIndex);
				ApplicationLauncher.logger.info("Adding the lower drag node: " + dragNode + " at " + updatedNodeIndex);
				retList.add(dragNode);
			}
			else if (dragNode.getNodeIndex() >= newIndexLocation) {
				if (ascendingOrder) {
					dragNode.setNodeIndex(++updatedNodeIndex);
					ApplicationLauncher.logger.info("Adding the higher drag node: " + dragNode + " at " + updatedNodeIndex);
					retList.add(dragNode);

					if (!nodeAdded) {
						relocatingNode.setNodeIndex(++updatedNodeIndex);
						ApplicationLauncher.logger.info("Adding the inter changed drag node: " + relocatingNode + " at " + updatedNodeIndex);
						retList.add(relocatingNode);
						nodeAdded = true;
					}
				}
				else {
					if (!nodeAdded) {
						relocatingNode.setNodeIndex(++updatedNodeIndex);
						ApplicationLauncher.logger.info("Adding the inter changed drag node: " + relocatingNode + " at " + updatedNodeIndex);
						retList.add(relocatingNode);
						nodeAdded = true;
						relocatedNodeIndex = updatedNodeIndex;
					}
					dragNode.setNodeIndex(++updatedNodeIndex);
					ApplicationLauncher.logger.info("Adding the higher drag node: " + dragNode + " at " + updatedNodeIndex);
					retList.add(dragNode);
				}
			}
		}
		if (!nodeAdded) {
			if (updatedNodeIndex == 0) {
				updatedNodeIndex = nodeList.size();
			}
			relocatingNode.setNodeIndex(++updatedNodeIndex);
			ApplicationLauncher.logger.info("Adding the end drag node: " + relocatingNode + " at " + updatedNodeIndex);
			retList.add(relocatingNode);
		}
		return retList;
	}


	public int Update_counterValue(String type){
		TestCaseType = type;
		ApplicationLauncher.logger.info("Update_counterValue: type: " + type);
		switch (TestCaseType) {



		case ConstantApp.DISPLAY_TC_TITLE_STARTING_CURRENT:
			STACounter = STACounter + 1;
			return STACounter;

		case ConstantApp.DISPLAY_TC_TITLE_WARMUP:
			WarmupCounter = WarmupCounter + 1;
			return WarmupCounter;

		case ConstantApp.DISPLAY_TC_TITLE_NOLOADTEST:
			CreepCounter = CreepCounter + 1;
			return CreepCounter;

		case ConstantApp.DISPLAY_TC_TITLE_ACCURACY:
			AccuracyCounter = AccuracyCounter + 1;
			return AccuracyCounter;

		case ConstantApp.DISPLAY_TC_TITLE_INF_VOLTAGE:
			InfluenceVoltCounter = InfluenceVoltCounter + 1;
			return InfluenceVoltCounter;

		case ConstantApp.DISPLAY_TC_TITLE_INF_FREQUENCY:
			InfluenceFreqCounter = InfluenceFreqCounter + 1;
			return InfluenceFreqCounter;

		case ConstantApp.DISPLAY_TC_TITLE_INF_HARMONICS:
			InfluenceHarmonicCounter = InfluenceHarmonicCounter + 1;
			return InfluenceHarmonicCounter;

		case ConstantApp.DISPLAY_TC_TITLE_CUT_NUETRAL:
			CuttingNuetralCounter = CuttingNuetralCounter + 1;
			return CuttingNuetralCounter;

		case ConstantApp.DISPLAY_TC_TITLE_INF_VOLT_UNBALANCE:
			VoltageUnbalanceCounter = VoltageUnbalanceCounter + 1;
			return VoltageUnbalanceCounter;

		case ConstantApp.DISPLAY_TC_TITLE_PHASE_REVERSAL:
			PhaseReversalCounter = PhaseReversalCounter + 1;
			return PhaseReversalCounter;



		case ConstantApp.DISPLAY_TC_TITLE_REPEATABLITY:
			RepeatabilityCounter = RepeatabilityCounter + 1;
			return RepeatabilityCounter;

		case ConstantApp.DISPLAY_TC_TITLE_SELF_HEATING:
			SelfHeatingCounter = SelfHeatingCounter + 1;
			return SelfHeatingCounter;

		case ConstantApp.DISPLAY_TC_TITLE_CONST_TEST:
			ConstTestCounter = ConstTestCounter + 1;
			return ConstTestCounter;

		case ConstantApp.DISPLAY_TC_TITLE_CUSTOM_TEST:
			CustomRatingCounter = CustomRatingCounter + 1;
			return CustomRatingCounter;

		case ConstantApp.DISPLAY_TC_TITLE_DUT_COMMAND:
			DutCommandCounter = DutCommandCounter + 1;
			return DutCommandCounter;

		default:
			return 0;
		}
	}

	public void SetDefaultColorToAllNode(){
		List<DraggableTestNode> mNodeList = new ArrayList<DraggableTestNode>();
		mNodeList = getNodeList();
		for (int i=0;i<getNodeList().size();i++){
			mNodeList.get(i).getStyleClass().clear();
			mNodeList.get(i).getStyleClass().add("dragicon");
			mNodeList.get(i).getStyleClass().add("icon-paleblue");
		}
	}

	public static String getProjectName() {
		return ProjectName;
	}

	public void setProjectName(String Pro_name) {
		ProjectName = Pro_name;
		Stage app = ApplicationLauncher.getPrimaryStage();
		Platform.runLater(() -> {
			app.setTitle(ConstantVersion.APPLICATION_NAME +" - " + Pro_name);
		});
	}



	@FXML
	public void CreateNewProject() throws JSONException{


		ApplicationLauncher.logger.info("CreateNewProject: entry");	

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/project/SaveAsProject" + ConstantApp.THEME_FXML));
		Scene newScene;
		try {
			newScene = new Scene(loader.load());
		} catch (IOException ex) {
			// TODO: handle error
			ex.printStackTrace();
			ApplicationLauncher.logger.error("CreateNewProject: IOException:"+ex.getMessage());
			return;
		}

		Stage CreateNewStage = new Stage();

		Stage primaryStage = ApplicationLauncher.getPrimaryStage();

		CreateNewStage.setTitle("New Project");
		CreateNewStage.initModality(Modality.WINDOW_MODAL);
		CreateNewStage.initOwner(primaryStage);
		CreateNewStage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
		CreateNewStage.setScene(newScene);

		CreateNewStage.setAlwaysOnTop(true);
		CreateNewStage.showAndWait();


		String NewProjectName = loader.<SaveAsProjectController>getController().getNewProjectName();
		ApplicationLauncher.logger.info("CreateNewProject : newProjectName:"+NewProjectName);
		List<String> ExistingDBprojectlist = new ArrayList<String>(); 
		if(!(NewProjectName == null)){

			if(!(NewProjectName.equals(""))){

				Pattern pattern = Pattern.compile("[a-zA-Z0-9_-]*$");
				Matcher matcher = pattern.matcher(NewProjectName);

				if  (matcher.matches()) {
					ExistingDBprojectlist = getProjectList();
					if(!(ExistingDBprojectlist.contains(NewProjectName))){
						ApplicationLauncher.logger.info("CreateNewProject : Project Save Validation Success");

						ApplicationLauncher.logger.info("NewProjectName: "+ NewProjectName);
						setProjectName(NewProjectName);
						ResetPane();
						ResetSummaryTable();
						ResetCounters();
						NodeList.clear();
						positionCounterId = 0;
						sequence_no = 0;

						cmbBox_ChooseProject.getItems().add(NewProjectName);
						cmbBox_ChooseProject.getSelectionModel().select(NewProjectName);
						String CurrentSelectedEM_Model = loader.<SaveAsProjectController>getController().ref_cmbBoxSaveAsModel_List.getSelectionModel().getSelectedItem().toString();
						ref_cmbBox_model_list.getSelectionModel().select(CurrentSelectedEM_Model);
						SaveProjectModelToDB(NewProjectName,CurrentSelectedEM_Model);
						ApplicationLauncher.logger.info("CreateNewProject : <" + NewProjectName + "> saved");

						setProjectEM_ModelType(getModelType(NewProjectName));
						setProjectEM_CT_Type(getCT_Type(NewProjectName));
						Alert alert = new Alert(AlertType.INFORMATION , "Project: \"" + NewProjectName + "\" created", ButtonType.OK);
						Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
						stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
						alert.showAndWait();

					}
					else {
						ApplicationLauncher.logger.info("CreateNewProject : Error 101: Project already exist<" + NewProjectName + ">");
						Alert alert = new Alert(AlertType.ERROR, "Error 101:Project:<" + NewProjectName + "> already exist", ButtonType.OK);
						Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
						stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
						alert.showAndWait();
					}

				}else {

					ApplicationLauncher.logger.info("CreateNewProject : Error 101A: Project contains special characters <" + NewProjectName + ">" );
					Alert alert = new Alert(AlertType.ERROR, "Error 101A : Project:<" + NewProjectName + "> contains special characters, kindly remove them and try again\n\nSpecial characters allowed: hypen and underscore", ButtonType.OK);
					alert.showAndWait();
				}
			}else {
				ApplicationLauncher.logger.info("CreateNewProject : Error 102:Project name is Empty");
				Alert alert = new Alert(AlertType.ERROR, "Error 102:Project name is empty", ButtonType.OK);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
				alert.showAndWait();
			}
		}




	}

	public void SaveProjectModelToDB(String ProjectName,String model_name){

		int model_id = get_model_id(model_name);
		MySQL_Controller.sp_add_project_model_mapping(ProjectName, model_id);
	}

	public void ResetPane(){
		Platform.runLater(() -> {
			testScriptRightPane.getChildren().clear();
			testScriptPropertyChildPane.getChildren().clear();
			
		});
	}

	public void ResetSummaryTable(){
		SummaryTable.getItems().clear();
	}

	public void ResetCounters(){
		WarmupCounter = 0;
		CreepCounter = 0;
		STACounter = 0;
		RepeatabilityCounter = 0;
		SelfHeatingCounter = 0;
		ConstTestCounter = 0;
		AccuracyCounter = 0;
		InfluenceVoltCounter = 0;
		InfluenceFreqCounter = 0;
		InfluenceHarmonicCounter = 0;
		CuttingNuetralCounter = 0;
		VoltageUnbalanceCounter = 0;
		PhaseReversalCounter = 0;
		CustomRatingCounter=0;
		DutCommandCounter =0;
	}

	public void LoadProjectOnClick() throws JSONException{
		btn_Load.setVisible(true);
		List<String> projectlist = new ArrayList<String>(); 
		JSONObject result = MySQL_Controller.sp_getproject_list();
		JSONArray project_arr = result.getJSONArray("Projects");
		for(int i =0; i<project_arr.length(); i++ ){
			projectlist.add(project_arr.getString(i));
		}
		ApplicationLauncher.logger.info(projectlist);
		cmbBox_ChooseProject.getItems().clear();
		cmbBox_ChooseProject.getItems().addAll(projectlist);
		cmbBox_ChooseProject.getSelectionModel().select(0);
		cmbBox_ChooseProject.setVisible(true);

	}

	public List<String> getProjectList() throws JSONException{
		List<String> projectlist = new ArrayList<String>(); 
		JSONObject result = MySQL_Controller.sp_getproject_list();
		JSONArray project_arr = result.getJSONArray("Projects");
		if(result.length()>0){
			for(int i =0; i<project_arr.length(); i++ ){
				try {
					if(!project_arr.getString(i).equals("Select")) {
						projectlist.add(project_arr.getString(i));
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ApplicationLauncher.logger.error("getProjectList: Exception:"+e.getMessage());
				}
			}
		}
		return projectlist;
	}
	public void LoadProjectList() throws JSONException{
		List<String> projectlist = new ArrayList<String>(); 

		projectlist = getProjectList();
		cmbBox_ChooseProject.getItems().clear();
		cmbBox_ChooseProject.getItems().add("Select");
		cmbBox_ChooseProject.getItems().addAll(projectlist);
		cmbBox_ChooseProject.getSelectionModel().select(0);



	}

	public void LoadProjectOnChange(){
		ApplicationLauncher.setCursor(Cursor.WAIT);
		String projectname = cmbBox_ChooseProject.getSelectionModel().getSelectedItem();
		setProjectName(projectname);
		ResetPane();
		ResetSummaryTable();
		ResetCounters();
		NodeList.clear();
		positionCounterId = 0;


		try {
			FetchFromDB(projectname);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("LoadProjectOnChange: JSONException:"+e.getMessage());
		}
		ApplicationLauncher.setCursor(Cursor.DEFAULT);

	}

	public void DeleteSelectedProject(){
		String projectname = cmbBox_ChooseProject.getSelectionModel().getSelectedItem();


		try{
			if(!projectname.equals("Select")){
				boolean status = false;
				ApplicationLauncher.logger.info("project to be deleted: " + projectname);
				if (delete_project_confirmation()) {
					status = MySQL_Controller.sp_delete_project(projectname);
					status = MySQL_Controller.sp_delete_summary_data_project(projectname);
				}
				else{
					ApplicationLauncher.logger.info("DeleteSelectedProject aborted ");
				}
				if(status){
					ResetPane();
					LoadProjectList();

				}
			}
			else{
				Alert alert = new Alert(AlertType.INFORMATION , "Please select the project", ButtonType.OK);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
				alert.showAndWait();
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("DeleteSelectedProject: Exception: "+e.getMessage());
		}
	}

	public boolean delete_project_confirmation(){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
		alert.setTitle(ConstantVersion.APPLICATION_NAME +" Exit");
		String s = "Are you sure, you want to delete?";
		alert.setContentText(s);
		boolean delete_confirmation=false;
		Optional<ButtonType> result = alert.showAndWait();


		if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
			delete_confirmation=true;

		}
		else{
			delete_confirmation=false;

		}
		return delete_confirmation;
	}
	
	public static void RefreshSummaryTableWithSummaryData(){
		ref_SummaryTable.setItems(SummaryData);
		ref_SummaryTable.refresh();
	}
	
	public static void RefreshSummaryDataFromDB(){
		
		ApplicationLauncher.logger.info("RefreshSummaryDataFromDB: Entry");
		SummaryData.clear();
		sequence_no = 0;
		ApplicationLauncher.logger.info("RefreshSummaryDataFromDB: sequence_no:"+sequence_no);
		JSONObject projectdatalist = MySQL_Controller.sp_getsummary_data(getProjectName());
		ArrayList<String> testcaselist = new ArrayList<String>();
		//ApplicationLauncher.logger.info("LoadSummaryData>>>projectdatalist: " + projectdatalist);
		JSONObject jobj = new JSONObject();
		String testcase = "";
		String alias_id = "";
		String testtype = "";
		JSONArray summary_data = new JSONArray();
		try {
			summary_data = projectdatalist.getJSONArray("Summary_data");
			//ApplicationLauncher.logger.info("UpdateSummaryData: LoadSummaryDataAndUpdateDB_IfDataNotExist:"+SummaryData.toString());
			for(int i=0; i<summary_data.length(); i++){
				jobj = summary_data.getJSONObject(i);
				testcase = jobj.getString("test_case_name");
				alias_id = jobj.getString("test_alias_id");
				testtype = jobj.getString("test_type");
				//ApplicationLauncher.logger.info("DB testcase:" + testcase);
				//UpdateSummaryData(projectname, testcase, testtype,  alias_id);
				SummaryData.add(new TestSetupSummaryDataModel(Integer.toString(sequence_no+1),testcase,Boolean.FALSE));
				sequence_no++;
			}
			//RefreshSummaryTableWithSummaryData();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			ApplicationLauncher.logger.error("LoadSummaryDataAndUpdateDB_IfDataNotExist: JSONException: " + e.getMessage());
			e.printStackTrace();
		}

	}

	public static void UpdateSummaryDB_IfDataNotExist(String projectname){
		ApplicationLauncher.logger.info("UpdateSummaryDB_IfDataNotExist: Entry");
		//SummaryData.clear();
		//sequence_no = 0;
		ApplicationLauncher.logger.info("UpdateSummaryDB_IfDataNotExist: sequence_no:"+sequence_no);
		JSONObject projectdatalist = MySQL_Controller.sp_getsummary_data(projectname);
		ArrayList<String> testcaselist = new ArrayList<String>();
		//ApplicationLauncher.logger.info("LoadSummaryData>>>projectdatalist: " + projectdatalist);
		JSONObject jobj = new JSONObject();
		String testcase = "";
		String alias_id = "";
		String testtype = "";
		JSONArray summary_data = new JSONArray();
		try {
			summary_data = projectdatalist.getJSONArray("Summary_data");
			//ApplicationLauncher.logger.info("UpdateSummaryData: LoadSummaryDataAndUpdateDB_IfDataNotExist:"+SummaryData.toString());
			for(int i=0; i<summary_data.length(); i++){
				jobj = summary_data.getJSONObject(i);
				testcase = jobj.getString("test_case_name");
				alias_id = jobj.getString("test_alias_id");
				testtype = jobj.getString("test_type");
				//ApplicationLauncher.logger.info("DB testcase:" + testcase);
				UpdateNewTestPointSummaryDataToDB(projectname, testcase, testtype,  alias_id);
			}
			//RefreshSummaryTableWithSummaryData();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			ApplicationLauncher.logger.error("UpdateSummaryDB_IfDataNotExist: JSONException: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void LoadSummaryDataToGUI(){

		ApplicationLauncher.logger.info("LoadSummaryDataToGUI: Entry");
		SummaryData.clear();
		sequence_no = 0;
		
		JSONObject projectdatalist = MySQL_Controller.sp_getsummary_data(getProjectName());
		ArrayList<String> testcaselist = new ArrayList<String>();
		//ApplicationLauncher.logger.info("LoadSummaryData>>>projectdatalist: " + projectdatalist);
		JSONObject jobj = new JSONObject();
		String testcase = "";
		String alias_id = "";
		String testtype = "";
		JSONArray summary_data = new JSONArray();
		try {
			summary_data = projectdatalist.getJSONArray("Summary_data");
			for(int i=0; i<summary_data.length(); i++){
				jobj = summary_data.getJSONObject(i);
				testcase = jobj.getString("test_case_name");
				alias_id = jobj.getString("test_alias_id");
				testtype = jobj.getString("test_type");
				//ApplicationLauncher.logger.info("testcase:"+i+":" + testcase);
				//UpdateSummaryDataOnGUI(projectname, testcase, testtype,  alias_id);
				SummaryData.add(new TestSetupSummaryDataModel(Integer.toString(sequence_no+1),testcase,Boolean.FALSE));
				//ref_SummaryTable.setItems(SummaryData);
				sequence_no++;
			}
			if(summary_data.length()>0){
				//final ObservableList<TestSetupSummaryDataModel>  data = SummaryData;
				Platform.runLater(() -> {
/*					ref_SummaryTable.setItems(data);
					ref_SummaryTable.refresh();*/
					RefreshSummaryTableWithSummaryData();
				});
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			ApplicationLauncher.logger.error("LoadSummaryDataToGUI: JSONException: " + e.getMessage());
			e.printStackTrace();
		}
		ApplicationLauncher.logger.info("LoadSummaryDataToGUI: sequence_no:"+sequence_no);
	}



	public static String getModelDataFromDB(String project_name,String Key){

		//ApplicationLauncher.logger.info("getModelDataFromDB : project_name:"+project_name);
		int model_id = MySQL_Controller.sp_getProjectModel_ID(project_name);
		//ApplicationLauncher.logger.debug("getModelDataFromDB : model_id:"+model_id);
		JSONObject model_data = MySQL_Controller.sp_getem_model_data(model_id);
		//ApplicationLauncher.logger.info("getModelDataFromDB : model_data:"+model_data);
		String ModelKeyData=null;
		try {
			ModelKeyData = model_data.getString(Key);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("getModelDataFromDB : Exception:"+e.getMessage());
		}
		return ModelKeyData;
	}



	public static String getModelType(String project_name){
		int model_id = MySQL_Controller.sp_getProjectModel_ID(project_name);
		ApplicationLauncher.logger.debug("getModelType : project_name:" +project_name);
		ApplicationLauncher.logger.debug("getModelType : model_id:" +model_id);
		JSONObject model_data = MySQL_Controller.sp_getem_model_data(model_id);



		String model_type=null;
		try {
			model_type = model_data.getString("model_type");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("getModelType : JSONException:" +e.getMessage());
		}
		return model_type;
	}
	
	public static String getCT_Type(String project_name){
		int model_id = MySQL_Controller.sp_getProjectModel_ID(project_name);
		ApplicationLauncher.logger.debug("getCT_Type : project_name:" +project_name);
		ApplicationLauncher.logger.debug("getCT_Type : model_id:" +model_id);
		JSONObject model_data = MySQL_Controller.sp_getem_model_data(model_id);



		String ct_type=null;
		try {
			ct_type = model_data.getString("ct_type");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("getCT_Type : JSONException:" +e.getMessage());
		}
		ApplicationLauncher.logger.debug("getCT_Type : ct_type:" +ct_type);
		return ct_type;
	}

	public void FetchFromDB(String projectname) throws JSONException{
		JSONObject projectdatalist = MySQL_Controller.sp_getproject(projectname);
		//ApplicationLauncher.logger.debug("FetchFromDB>>>projectdatalist: " + projectdatalist);
		ApplicationLauncher.logger.debug("projectdatalist size: " + projectdatalist.getInt("No_of_nodes"));
		JSONArray project_nodes = projectdatalist.getJSONArray("Nodes");
		String ModelName = getModelDataFromDB(projectname,"model_name");
		SetModelNameOnUI(ModelName);
		setProjectEM_ModelType(getModelType(projectname));
		setProjectEM_CT_Type(getCT_Type(projectname));
		//ApplicationLauncher.logger.info("FetchFromDB: getProjectEM_ModelType: " +getProjectEM_ModelType());
		Platform.runLater(() -> {
		try {
			for(int i=0; i<projectdatalist.getInt("No_of_nodes"); i++){
				//ApplicationLauncher.logger.info("FetchFromDB Entry For Loop");
				final DraggableTestNode node = new DraggableTestNode(testScriptPropertyChildPane);
				JSONObject project_node = project_nodes.getJSONObject(i);

				node.setType(getIconType(project_node.getString("test_type")));
				node.setTitle(getDisplayName(project_node.getString("test_type")));
				node.setAliasId(project_node.getString("test_alias_id"));
				setAliasName(node);
				node.NewNode(false);
				node.SetDefaultColorToAllNode();
				node.setProjectName(getProjectName());
				//Platform.runLater(() -> {
					testScriptRightPane.getChildren().add(node);
				//});
				SaveNode(node);
				positionCounterId = positionCounterId + 1;
				IncrementAliasID(node.getType(), node.getAliasId());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.debug("FetchFromDB : Exception: " + e.getMessage());
		}
		//LoadSummaryData(projectname);
		LoadSummaryDataToGUI();
		SetDefaultColorToAllNode();
		});
	}

	public void IncrementAliasID(TestProfileType type, String aliasid){
		int alias_id_counter = Integer.parseInt(aliasid);
		switch (type) {

		case STA:
			if(STACounter < alias_id_counter){
				STACounter = alias_id_counter;
			}		
			break;

		case Warmup:
			if(WarmupCounter < alias_id_counter){
				WarmupCounter = alias_id_counter;
			}	
			break;

		case NoLoad:
			if(CreepCounter < alias_id_counter){
				CreepCounter = alias_id_counter;
			}	
			break;

		case Accuracy:
			if(AccuracyCounter < alias_id_counter){
				AccuracyCounter = alias_id_counter;
			}	
			break;

		case InfluenceVolt:
			if(InfluenceVoltCounter < alias_id_counter){
				InfluenceVoltCounter = alias_id_counter;
			}
			break;

		case InfluenceFreq:
			if(InfluenceFreqCounter < alias_id_counter){
				InfluenceFreqCounter = alias_id_counter;
			}
			break;

		case InfluenceHarmonic:
			if(InfluenceHarmonicCounter < alias_id_counter){
				InfluenceHarmonicCounter = alias_id_counter;
			}
			break;

		case CuttingNuetral:
			if(CuttingNuetralCounter < alias_id_counter){
				CuttingNuetralCounter = alias_id_counter;
			}
			break;

		case VoltageUnbalance:
			if(VoltageUnbalanceCounter < alias_id_counter){
				VoltageUnbalanceCounter = alias_id_counter;
			}
			break;

		case PhaseReversal:
			if(PhaseReversalCounter < alias_id_counter){
				PhaseReversalCounter = alias_id_counter;
			}
			break;

			/*case StdDeviation:
			if(StdDeviationCounter < alias_id_counter){
				StdDeviationCounter = alias_id_counter;
			}
		break;*/

		case Repeatability:
			if(RepeatabilityCounter < alias_id_counter){
				RepeatabilityCounter = alias_id_counter;
			}
			break;

		case SelfHeating:
			if(SelfHeatingCounter < alias_id_counter){
				SelfHeatingCounter = alias_id_counter;
			}
			break;

		case ConstantTest:
			if(ConstTestCounter < alias_id_counter){
				ConstTestCounter = alias_id_counter;
			}
			break;

		case CustomTest:
			if(CustomRatingCounter < alias_id_counter){
				CustomRatingCounter = alias_id_counter;
			}
			break;

			
		case DutCommand:
			if(DutCommandCounter < alias_id_counter){
				DutCommandCounter = alias_id_counter;
			}
			break;	

		default:
			break;
		}
	}

	public TestProfileType getIconType(String testType){
		TestCaseType = testType;
		ApplicationLauncher.logger.info("getIconType: testType: " + testType);
		switch (TestCaseType) {



		//case "STA":
		case ConstantApp.TEST_PROFILE_STA:
			return TestProfileType.STA;

		//case "Warmup":
		case	ConstantApp.TEST_PROFILE_WARMUP:
			return TestProfileType.Warmup;

		//case "NoLoad":
		case ConstantApp.TEST_PROFILE_NOLOAD:
			return TestProfileType.NoLoad;

		//case "Accuracy":
		case ConstantApp.TEST_PROFILE_ACCURACY:
			return TestProfileType.Accuracy;

		//case "InfluenceVolt":
		case ConstantApp.TEST_PROFILE_INFLUENCE_VOLT:
			return TestProfileType.InfluenceVolt;

		//case "InfluenceFreq":
		case ConstantApp.TEST_PROFILE_INFLUENCE_FREQ:
			return TestProfileType.InfluenceFreq;

		//case "InfluenceHarmonic":
		case ConstantApp.TEST_PROFILE_INFLUENCE_HARMONIC:
			return TestProfileType.InfluenceHarmonic;

		//case "CuttingNuetral":
		case ConstantApp.TEST_PROFILE_CUT_NUETRAL:
			return TestProfileType.CuttingNuetral;

		//case "VoltageUnbalance":
		case ConstantApp.TEST_PROFILE_VOLTAGE_UNBALANCE:
			return TestProfileType.VoltageUnbalance;

		//case "PhaseReversal":
		case ConstantApp.TEST_PROFILE_PHASE_REVERSAL:
			return TestProfileType.PhaseReversal;


		//case "Repeatability":
		case ConstantApp.TEST_PROFILE_REPEATABILITY:
			return TestProfileType.Repeatability;

		//case "SelfHeating":
		case ConstantApp.TEST_PROFILE_SELF_HEATING:
			return TestProfileType.SelfHeating;

		//case "ConstantTest":
		case ConstantApp.TEST_PROFILE_CONSTANT_TEST:
			return TestProfileType.ConstantTest;

		//case "CustomTest":
		case ConstantApp.TEST_PROFILE_CUSTOM_TEST:
			return TestProfileType.CustomTest;
			
		case ConstantApp.TEST_PROFILE_DUT_COMMAND:
			return TestProfileType.DutCommand;//ConstantApp.DISPLAY_TC_TITLE_DUT_COMMAND;

		default:
			return TestProfileType.DefaultType;
		}
	}

	public String getDisplayName(String testType){
		TestCaseType = testType;
		ApplicationLauncher.logger.info("getIconType: testType: " + testType);
		switch (TestCaseType) {
		//case "STA":
		case ConstantApp.TEST_PROFILE_STA:
			return ConstantApp.DISPLAY_TC_TITLE_STARTING_CURRENT;

		//case "Warmup":
		case	ConstantApp.TEST_PROFILE_WARMUP:
			return ConstantApp.DISPLAY_TC_TITLE_WARMUP;

		//case "NoLoad":
		case ConstantApp.TEST_PROFILE_NOLOAD:
			return ConstantApp.DISPLAY_TC_TITLE_NOLOADTEST;

		//case "Accuracy":
		case ConstantApp.TEST_PROFILE_ACCURACY:
			return ConstantApp.DISPLAY_TC_TITLE_ACCURACY;

		//case "InfluenceVolt":
		case ConstantApp.TEST_PROFILE_INFLUENCE_VOLT:
			return ConstantApp.DISPLAY_TC_TITLE_INF_VOLTAGE;

		//case "InfluenceFreq":
		case ConstantApp.TEST_PROFILE_INFLUENCE_FREQ:
			return ConstantApp.DISPLAY_TC_TITLE_INF_FREQUENCY;

		//case "InfluenceHarmonic":
		case ConstantApp.TEST_PROFILE_INFLUENCE_HARMONIC:
			return ConstantApp.DISPLAY_TC_TITLE_INF_HARMONICS;

		//case "CuttingNuetral":
		case ConstantApp.TEST_PROFILE_CUT_NUETRAL:
			return ConstantApp.DISPLAY_TC_TITLE_CUT_NUETRAL;

		//case "VoltageUnbalance":
		case ConstantApp.TEST_PROFILE_VOLTAGE_UNBALANCE:
			return ConstantApp.DISPLAY_TC_TITLE_INF_VOLT_UNBALANCE;

		//case "PhaseReversal":
		case ConstantApp.TEST_PROFILE_PHASE_REVERSAL:
			return ConstantApp.DISPLAY_TC_TITLE_PHASE_REVERSAL;

		//case "Repeatability":
		case ConstantApp.TEST_PROFILE_REPEATABILITY:
			return ConstantApp.DISPLAY_TC_TITLE_REPEATABLITY;

		//case "SelfHeating":
		case ConstantApp.TEST_PROFILE_SELF_HEATING:
			return ConstantApp.DISPLAY_TC_TITLE_SELF_HEATING;

		//case "ConstantTest":
		case ConstantApp.TEST_PROFILE_CONSTANT_TEST:
			return ConstantApp.DISPLAY_TC_TITLE_CONST_TEST;

		//case "CustomTest":
		case ConstantApp.TEST_PROFILE_CUSTOM_TEST:
			return ConstantApp.DISPLAY_TC_TITLE_CUSTOM_TEST;
			
		case ConstantApp.TEST_PROFILE_DUT_COMMAND:
			return ConstantApp.DISPLAY_TC_TITLE_DUT_COMMAND;

		default:
			return TestProfileType.DefaultType.toString();
		}
	}



	public static void SaveProject(){
		ApplicationLauncher.logger.info("SaveProject: entry");			
		String SelectedProject = ref_cmbBox_ChooseProject.getSelectionModel().getSelectedItem();
		//setToBeSavedProjectName(ref_cmbBox_ChooseProject.getSelectionModel().getSelectedItem());
		setToBeSavedProjectName(SelectedProject);
		List<DraggableTestNode> node_list = getNodeList();
		for(Node nodeobj : node_list){
			LoadToDB(nodeobj);

		}
		LoadEM_ModelMappingToDB();
		//setProjectEM_ModelType(getModelType(ref_cmbBox_ChooseProject.getSelectionModel().getSelectedItem()));
		setProjectEM_ModelType(getModelType(SelectedProject));
		setProjectEM_CT_Type(getCT_Type(SelectedProject));
		
		
	}

	public static  void setToBeSavedProjectName(String projectName){
		ToBeSavedProjectName = projectName;
	}

	public static  String  getToBeSavedProjectName(){
		return ToBeSavedProjectName;
	}


	public void RefreshGUI_withSavedAsProject(String SavedAsProjectName){

		setProjectName(SavedAsProjectName);
		ApplicationLauncher.logger.info("SavedAsProjectName: " + SavedAsProjectName);

		LoadProjectOnChange();
		//LoadProjectTrigger();
		cmbBox_ChooseProject.getItems().add(SavedAsProjectName);
		cmbBox_ChooseProject.getSelectionModel().select(SavedAsProjectName);

	}

	public void SaveAsProject() throws JSONException{
		ApplicationLauncher.logger.info("SaveAsClick: entry");	
		if(!ref_cmbBox_ChooseProject.getSelectionModel().getSelectedItem().equals("Select")){
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/project/SaveAsProject" + ConstantApp.THEME_FXML));
			Scene newScene;
			try {
				newScene = new Scene(loader.load());
			} catch (IOException ex) {
				// TODO: handle error
				ex.printStackTrace();
				ApplicationLauncher.logger.error("ProjectController: SaveAsProject : IOException:" +ex.getMessage());
				return;
			}

			Stage SaveAsStage = new Stage();

			Stage primaryStage = ApplicationLauncher.getPrimaryStage();

			SaveAsStage.setTitle("Save as Project");
			SaveAsStage.initModality(Modality.WINDOW_MODAL);
			SaveAsStage.initOwner(primaryStage);
			SaveAsStage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
			//https://stackoverflow.com/questions/38481914/disable-background-stage-javafx?rq=1

			SaveAsStage.setScene(newScene);
			String CurrentEM_Model = ref_cmbBox_model_list.getSelectionModel().getSelectedItem().toString();
			loader.<SaveAsProjectController>getController().ref_cmbBoxSaveAsModel_List.getSelectionModel().select(CurrentEM_Model);

			SaveAsStage.setAlwaysOnTop(true);
			SaveAsStage.showAndWait();


			String SaveAsProjectName = loader.<SaveAsProjectController>getController().getNewProjectName();
			ApplicationLauncher.logger.info("SaveAsProject : newProjectName:"+SaveAsProjectName);
			List<String> ExistingDBprojectlist = new ArrayList<String>(); 
			if(!(SaveAsProjectName == null)){
				ApplicationLauncher.logger.info("SaveAsProject : test1");

				if(!(SaveAsProjectName.equals(""))){
					ApplicationLauncher.logger.info("SaveAsProject : test2");
					ExistingDBprojectlist = getProjectList();
					if(!(ExistingDBprojectlist.contains(SaveAsProjectName))){
						ApplicationLauncher.logger.info("SaveAsProject : Project Save Validation Success");
						setToBeSavedProjectName(SaveAsProjectName);
						String CurrentProjectName = ref_cmbBox_ChooseProject.getSelectionModel().getSelectedItem();
						String CurrentSelectedSaveAsEM_Model = loader.<SaveAsProjectController>getController().ref_cmbBoxSaveAsModel_List.getSelectionModel().getSelectedItem().toString();
						ApplicationLauncher.logger.info("SaveAsProject : CurrentSelectedSaveAsEM_Model:"+CurrentSelectedSaveAsEM_Model);
						//ref_cmbBox_model_list.getSelectionModel().select(CurrentSelectedSaveAsEM_Model);
						SaveProjectAsInDB(CurrentProjectName,SaveAsProjectName,CurrentSelectedSaveAsEM_Model);

						RefreshGUI_withSavedAsProject(SaveAsProjectName);
						ApplicationLauncher.logger.info("SaveAsProject : saved");
						setProjectEM_ModelType(getModelType(SaveAsProjectName));
						setProjectEM_CT_Type(getCT_Type(SaveAsProjectName));
						Alert alert = new Alert(AlertType.INFORMATION , "Project:<" + SaveAsProjectName + "> saved", ButtonType.OK);
						Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
						stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
						alert.showAndWait();

					}
					else {
						ApplicationLauncher.logger.info("SaveAsProject : Project already exist");
						Alert alert = new Alert(AlertType.ERROR, "Project:<" + SaveAsProjectName + "> already exist", ButtonType.OK);
						Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
						stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
						alert.showAndWait();
					}
				}else {
					ApplicationLauncher.logger.info("SaveAsProject : Project name is Empty");
					Alert alert = new Alert(AlertType.ERROR, "Project name is empty", ButtonType.OK);
					Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
					alert.showAndWait();
				}
			}
		}else{
			Alert alert = new Alert(AlertType.INFORMATION , "Please select the project", ButtonType.OK);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
			alert.showAndWait();
		}

	}

	public void SaveProjectAsInDB(String CurrentProjectName,String newProjectName,String EM_Model){
		SaveAsProject(CurrentProjectName,newProjectName);
		SaveAsProjectModelMapping(CurrentProjectName,newProjectName,EM_Model);
		SaveAsAllProjectComponents(CurrentProjectName,newProjectName);
		SaveAsAllTestPointSetup(CurrentProjectName,newProjectName);
		SaveAsAllSummary(CurrentProjectName,newProjectName);
	}

	public Boolean SaveAsProject(String CurrentProjectName,String ToBeSavedProjectName){


		boolean status = MySQL_Controller.sp_getprojectSaveAs(CurrentProjectName,ToBeSavedProjectName);
		ApplicationLauncher.logger.info("SaveAsProject: status:"+status);
		return status;

	}

	public Boolean SaveAsProjectModelMapping(String CurrentProjectName,String ToBeSavedProjectName,String EM_Model_Name){

		int EM_Model_ID = MySQL_Controller.sp_getModel_ID(EM_Model_Name);
		boolean status = MySQL_Controller.sp_getproject_modelmappingSaveAs(CurrentProjectName,ToBeSavedProjectName,EM_Model_ID);
		ApplicationLauncher.logger.info("SaveAsProjectModelMapping: status:"+status);
		return status;

	}

	public Boolean SaveAsAllSummary(String CurrentProjectName,String ToBeSavedProjectName){


		boolean status = MySQL_Controller.sp_getsummary_dataSaveAs(CurrentProjectName,ToBeSavedProjectName);
		ApplicationLauncher.logger.info("SaveAsAllSummary: status:"+status);
		return status;

	}

	public Boolean SaveAsAllTestPointSetup(String CurrentProjectName,String ToBeSavedProjectName){


		boolean status = MySQL_Controller.sp_gettest_point_setupSaveAs(CurrentProjectName,ToBeSavedProjectName);
		ApplicationLauncher.logger.info("SaveAsAllTestPointSetup: status:"+status);
		return status;

	}

	public Boolean SaveAsAllProjectComponents(String CurrentProjectName,String ToBeSavedProjectName){


		boolean status = MySQL_Controller.sp_getproject_componentsSaveAs(CurrentProjectName,ToBeSavedProjectName);
		
		ApplicationLauncher.logger.info("SaveAsAllProjectComponents: status:"+status);
		List<DutCommand> dutCommandList = MySqlServiceManager.getDutCommandService().findByProjectName(CurrentProjectName);
		for(DutCommand eachDutCommand: dutCommandList) {
			eachDutCommand.setProjectName(ToBeSavedProjectName);
			eachDutCommand.setId(null);
			MySqlServiceManager.getDutCommandService().saveToDb(eachDutCommand);
		}
		return status;

	}

	public static void LoadToDB(Node nodeobj){
		ApplicationLauncher.logger.info("LoadToDB: entry");	
		String ProjectName = getToBeSavedProjectName();

		//String model_name = ref_cmbBox_model_list.getSelectionModel().getSelectedItem();
		//int model_id = get_model_id(model_name);
		String TestCase = ((DraggableTestNode) nodeobj).getTitle();
		String AliasID = ((DraggableTestNode) nodeobj).getAliasId();
		String testtype = ((DraggableTestNode) nodeobj).getType().toString();
		String PositionID = Integer.toString(((DraggableTestNode) nodeobj).getPositionId());
		//ApplicationLauncher.logger.info(ProjectName);
		MySQL_Controller.sp_add_project(ProjectName,testtype, AliasID, PositionID);//Changed by Prasanth
		//MySQL_Controller.sp_add_project_model_mapping(ProjectName, model_id);
		//ApplicationLauncher.logger.info("LoadToDB: exit");		
	}
	
	public static void LoadEM_ModelMappingToDB(){
		ApplicationLauncher.logger.info("LoadEM_ModelMappingToDB: entry");	
		String ProjectName = getToBeSavedProjectName();

		String model_name = ref_cmbBox_model_list.getSelectionModel().getSelectedItem().toString();
		ApplicationLauncher.logger.debug("LoadEM_ModelMappingToDB: ProjectName:"+ProjectName);	
		ApplicationLauncher.logger.debug("LoadEM_ModelMappingToDB: model_name:"+model_name);	
		int model_id = get_model_id(model_name);
		ApplicationLauncher.logger.debug("LoadEM_ModelMappingToDB: model_id:"+model_id);	
		MySQL_Controller.sp_add_project_model_mapping(ProjectName, model_id);
		
	}

	public void LoadProjectComponents(DraggableTestNode nodeobj){
		String projectname = nodeobj.getProjectName();
		String testcasename = nodeobj.getTitle();
		String testtype = nodeobj.getType().toString();
		String test_alias_id = nodeobj.getAliasId();
		String position_id = Integer.toString(nodeobj.getPositionId());
	}

	public static int get_model_id(String input_model_name){
		ArrayList<String> model_id_list = getmodel_id_list();
		ArrayList<String> modelNamelist = getmodel_list();
		ApplicationLauncher.logger.debug("get_model_id: model_id_list:"+model_id_list);	
		ApplicationLauncher.logger.debug("get_model_id: modelNamelist:"+modelNamelist);	
		int model_id = 0;
		for(int i=0; i < modelNamelist.size(); i++){
			if(input_model_name.equals(modelNamelist.get(i))){
				model_id = Integer.parseInt(model_id_list.get(i));
			}
		}

		return model_id;
	}
	public void SaveNode(DraggableTestNode node){
		NodeList.add(node);

	}


	public static void DeleteNode(DraggableTestNode node){
		NodeList.remove(node);

	}

	public static List<DraggableTestNode> getNodeList(){
		return NodeList;
	}

	public void SummaryMoveUpOnClick (){
		ObservableList<TestSetupSummaryDataModel> summary_data = ref_SummaryTable.getItems();
		int current_pos = 0;
		int swap_pos = 0;
		boolean swapped_flag = false;
		for (TestSetupSummaryDataModel testCaseRow : summary_data) {
			if(testCaseRow.getIsSelected()){
				swapped_flag = false;
				for(int i=0; i<summary_data.size(); i++){
					String testCaseName = summary_data.get(i).getTestData();
					String current_row = testCaseRow.getTestData();
					if(testCaseName.equals(current_row) && !swapped_flag){
						current_pos = i;
						swap_pos = i-1;
						ApplicationLauncher.logger.info("SummaryMoveUpOnClick: current_pos: " + current_pos);
						ApplicationLauncher.logger.info("SummaryMoveUpOnClick: swap_pos: " + swap_pos);
						if(current_pos != 0){
							summary_data = SwapTables(summary_data, swap_pos, current_pos);
							ref_SummaryTable.setItems(summary_data);
							swapped_flag = true;
						}
					}
				}

			}
		}
	}

	public void SummaryMoveDownOnClick(){
		ObservableList<TestSetupSummaryDataModel> summary_data = ref_SummaryTable.getItems();
		int current_pos = 0;
		int swap_pos = 0;
		boolean swapped_flag = false;
		for (TestSetupSummaryDataModel testCaseRow : summary_data) {
			if(testCaseRow.getIsSelected()){
				swapped_flag = false;
				for(int i=0; i<summary_data.size(); i++){
					String testCaseName = summary_data.get(i).getTestData();
					String current_row = testCaseRow.getTestData();
					if(testCaseName.equals(current_row) && !swapped_flag){
						current_pos = i;
						swap_pos = i+1;
						ApplicationLauncher.logger.info("SummaryMoveUpOnClick: current_pos: " + current_pos);
						ApplicationLauncher.logger.info("SummaryMoveUpOnClick: swap_pos: " + swap_pos);
						if(current_pos != summary_data.size()-1){
							summary_data = SwapTables(summary_data, swap_pos, current_pos);
							ref_SummaryTable.setItems(summary_data);
							swapped_flag = true;
						}
					}
				}

			}
		}
	}

	public ObservableList<TestSetupSummaryDataModel> SwapTables(ObservableList<TestSetupSummaryDataModel> summary_data, int swap_pos, int current_pos) {
		TestSetupSummaryDataModel current_testdata = summary_data.get(current_pos);
		TestSetupSummaryDataModel swap_testdata = summary_data.get(swap_pos);
		summary_data.set(current_pos, swap_testdata);
		summary_data.set(swap_pos, current_testdata);
		return summary_data;
	} 

	public void SaveSummaryDataOnClick(){
		ObservableList<TestSetupSummaryDataModel> summary_data = ref_SummaryTable.getItems();
		String projectname = getProjectName();
		String testdata = "";
		ArrayList<String> test_details = new ArrayList<String>();
		String test_type = "";
		String alias_id = "";
		int sequence_no = 0;
		for(int i=0; i<summary_data.size(); i++){
			//TestSetupSummaryDataModel data = summary_data.get(i);
			//String testdata = data.getTestData();
			testdata = summary_data.get(i).getTestData();
			test_details = parseTestDetails(testdata);
			test_type = test_details.get(0);
			alias_id = test_details.get(1);
			sequence_no = i+1;
			MySQL_Controller.sp_add_summary_data(projectname, testdata, test_type, alias_id, sequence_no);
		}



	}

	public ArrayList<String> parseTestDetails(String testdata){
		ArrayList<String> test_details = new ArrayList<String>();
		if(testdata.contains("-")){
			String[] split_data = testdata.split("-");
			String test_name_with_aliasid = split_data[0];
			String[] testype_alias_id = test_name_with_aliasid.split("_");
			String test_type = testype_alias_id[0];
			String alias_id = testype_alias_id[1];
			test_details.add(test_type);
			test_details.add(alias_id);
		}
		else{
			String[] testype_alias_id = testdata.split("_");
			String test_type = testype_alias_id[0];
			String alias_id = testype_alias_id[1];
			test_details.add(test_type);
			test_details.add(alias_id);
		}
		return test_details;
	}

	public void addTestPoint(String testtype, String alias_id){
		ApplicationLauncher.logger.info("addTestPoint: testtype: " + testtype);
		ApplicationLauncher.logger.info("addTestPoint: alias_id: " + alias_id);
		TestPointsNotSaved.add(testtype+alias_id);
		ApplicationLauncher.logger.info("addTestPoint: TestPointsNotSaved: " + TestPointsNotSaved);
	}

	public static void removeTestPoint(String testtype, String alias_id){
		if(TestPointsNotSaved.size() != 0){
			if(TestPointsNotSaved.contains(testtype+alias_id)){
				ApplicationLauncher.logger.info("removeTestPoint: Before:  TestPointsNotSaved: " + TestPointsNotSaved);
				TestPointsNotSaved.remove(testtype+alias_id);	
				ApplicationLauncher.logger.info("removeTestPoint: After: TestPointsNotSaved: " + TestPointsNotSaved);
			}
		}

	}

	public static boolean isAllTestPointSaved(){
		if(TestPointsNotSaved.size() == 0){
			return true;
		}
		else{
			return false;
		}
	}
	
	
	
	public void LoadProjectTrigger() {
		ApplicationLauncher.logger.info("LoadProjectTrigger Invoked:");
		LoadProjectTaskTimer = new Timer();
		LoadProjectTaskTimer.schedule(new LoadProjectTask(),100);
	}


	class LoadProjectTask extends TimerTask {
		public void run() {
			ApplicationLauncher.logger.info("LoadProjectTask : Entry");
			//ApplicationLauncher.setCursor(Cursor.WAIT);
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
			LocalDateTime DB_StartTime = LocalDateTime.now();
			ApplicationLauncher.logger.info("LoadProjectTask: DB Fetch Start Time: "+dtf.format(DB_StartTime));
			LoadProjectOnChange();
			LocalDateTime DisplayEndTime = LocalDateTime.now();
			ApplicationLauncher.logger.info("LoadProjectTask: End Time: "+dtf.format(DisplayEndTime));
			ApplicationLauncher.logger.info("LoadProjectTask: Difference Start Time  to End Time: "+ TestReportController.DiffTime(dtf.format(DB_StartTime),dtf.format(DisplayEndTime)));

			//ApplicationLauncher.setCursor(Cursor.DEFAULT);
			LoadProjectTaskTimer.cancel();
			
		}
	}
	
	private static void applyUacSettings() {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.info("ProjectController : applyUacSettings :  Entry");
		ArrayList<UacDataModel> uacSelectProfileScreenList = DeviceDataManagerController.getUacSelectProfileScreenList();
		String screenName = "";
		for (int i = 0; i < uacSelectProfileScreenList.size(); i++){

			screenName = uacSelectProfileScreenList.get(i).getScreenName();
			switch (screenName) {
				case ConstantApp.UAC_PROJECT_SCREEN:
					
					
					if(!uacSelectProfileScreenList.get(i).getExecutePossible()){
						//btn_Create.setDisable(true);
						
					}
					
					if(!uacSelectProfileScreenList.get(i).getAddPossible()){
						ref_btn_Create.setDisable(true);
						
					}
					
					if(!uacSelectProfileScreenList.get(i).getUpdatePossible()){
						//ref_vbox_testscript.setDisable(true);sdvsc
						setChildPropertySaveEnabled(false);
						ref_btn_Save.setDisable(true);
						
					}
					
					if(!uacSelectProfileScreenList.get(i).getDeletePossible()){
						ref_btn_Delete.setDisable(true);
						
					}
					break;
					
								
	
				default:
					break;
			}
			
				
				
		}
	}
	
	public static boolean isChildPropertySaveEnabled() {
		return childPropertySaveEnabled;
	}

	public static void setChildPropertySaveEnabled(boolean childPropertySaveEnabled) {
		ProjectController.childPropertySaveEnabled = childPropertySaveEnabled;
	}


}
