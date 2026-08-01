package com.tasnetwork.calibration.energymeter.reportprofile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.lang3.math.NumberUtils;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.WindowManager;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.constant.ConstantReportV2;
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.testprofiles.TestProfileType;
import com.tasnetwork.calibration.energymeter.testreport.ExcelCellValueModel;
import com.tasnetwork.calibration.energymeter.testreport.ReportMeterMetaDataTypeSubModel;
import com.tasnetwork.calibration.energymeter.uac.UacDataModel;
import com.tasnetwork.calibration.energymeter.util.ErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;
import com.tasnetwork.spring.orm.model.OperationParam;
import com.tasnetwork.spring.orm.model.OperationProcess;
import com.tasnetwork.spring.orm.model.ReportProfileManage;
import com.tasnetwork.spring.orm.model.ReportProfileMeterMetaDataFilter;
import com.tasnetwork.spring.orm.model.ReportProfileTestDataFilter;
import com.tasnetwork.spring.orm.model.RpPrintPosition;
import com.tasnetwork.spring.orm.service.OperationParamService;
import com.tasnetwork.spring.orm.service.ReportProfileManageService;
import com.tasnetwork.spring.orm.service.ReportProfileMeterMetaDataFilterService;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;

public class ReportProfileConfigController implements Initializable {

	// Timer guiLaunchTimer;
	// static DeviceDataManagerController dataManagerObj = new
	// DeviceDataManagerController();

	private boolean REPLICATE_FEATURE_DISABLED = true;

	// private boolean CURRENT_USER_ENTRY_IN_PERCENTAGE = true;
	static private OperationParamService reportOperationParamService = DeviceDataManagerController
			.getRpOperationParamService();
	static private ReportProfileMeterMetaDataFilterService reportProfileMeterMetaDataFilterService = DeviceDataManagerController
			.getReportProfileMeterMetaDataFilterService();
	static private ReportProfileManageService reportProfileManageService = DeviceDataManagerController
			.getReportProfileManageService();
	private OperationProcessJsonReadModel operationProcessDataModel = DeviceDataManagerController
			.getReportProfileConfigParsedKey();
	private ArrayList<OperationParam> operationParameterProfileDataList = new ArrayList<OperationParam>();

	private final String RPS_SUB_NORMAL = "Normal";
	private final String RPS_SUB_REVERSE = "Reverse";

	private ArrayList<String> REV_PHASE_SEQ_SUB_LIST = new ArrayList<String>(
			Arrays.asList(RPS_SUB_NORMAL, RPS_SUB_REVERSE));

	HashMap<String, Integer> operationProcessDbIdHashMap = new LinkedHashMap<String, Integer>();
	HashMap<String, Integer> rpPrintDbIdHashMap = new LinkedHashMap<String, Integer>();

	private boolean insertDataMode = false;
	private volatile boolean testFilterEditMode = false;

	private MultiValuedMap<String, String> groupProfileNameHashMap = new ArrayListValuedHashMap<String, String>();

	private Set<String> metaDataPageNameList = new HashSet<String>();

	private int REPLICATE_DATA_COUNT = 10;

	private int REPORT_MAX_PAGE_SUPPORTED = 4;
	String ALL_PAGES = "All Pages";
	String SELECT_DISPLAY = "Select";
	String SAMPLE_GROUP = "GROUP1";

	Pattern excelRowColUserEntryPattern = Pattern.compile("[a-zA-Z]+\\d+");

	private int OPERATION_MASTER_OUTPUT_STATUS_MAX_COUNT = 10;

	private String DUT_KEY = ConstantAppConfig.DUT_DISPLAY_KEY;// ConstantReport.DUT_KEY;

	private String BASE_TEMPLATE1 = "Base Template1";
	private String BASE_TEMPLATE2 = "Base Template2";
	private String BASE_TEMPLATE3 = "Base Template3";


	String ERROR_RESULT_STATUS_NOT_ALLOWED_TAIL_END = "Register";

	// private String NONE_DISPLAYED = "None";

	private ArrayList<ReportMeterMetaDataTypeSubModel> meterMetaDataList = new ArrayList<ReportMeterMetaDataTypeSubModel>();
	private HashMap<String, ReportProfileMeterMetaDataModel> reportProfileMeterMetaDataHashMap = new LinkedHashMap<String, ReportProfileMeterMetaDataModel>();

	// private ArrayList<ExcelCellValueModel> testfilterCellPositionDataList = new
	// ArrayList<ExcelCellValueModel>();
	private HashMap<String, String> testFilterDataCellPositionHashMap = new LinkedHashMap<String, String>();
// 
	private HashMap<String, String> lastSavedMeterMetaDataCellPosition = new LinkedHashMap<String, String>();
	private HashMap<String, String> lastSavedTestFilterDataCellPositionHashMap = new LinkedHashMap<String, String>();

	private List<ReportProfileManage> activeReportProfileDatabaseList = new ArrayList<ReportProfileManage>();

	private ArrayList<String> OPERATION_INPUT_HEADERS_ONLY_DATA_TYPE_LIST = new ArrayList<String>();// (Arrays.asList(ConstantReportV2.RESULT_DATA_TYPE_DISPLAY_REFSTD_INITIAL_REGISTER,
	// ConstantReportV2.RESULT_DATA_TYPE_DISPLAY_REFSTD_FINAL_REGISTER));

	ArrayList<String> REPORT_METER_DATA_TYPE_KEY_LIST = ConstantReportV2.REPORT_META_DATATYPE_LIST;

	private ArrayList<String> BASE_TEMPLATE_LIST = new ArrayList<String>(
			Arrays.asList(BASE_TEMPLATE1, BASE_TEMPLATE2, BASE_TEMPLATE3));

	private ArrayList<String> BASE_TEMPLATE_POPULATE_TYPE_LIST = new ArrayList<String>(Arrays
			.asList(ConstantReport.REPORT_DATA_POPULATE_VERTICAL, ConstantReport.REPORT_DATA_POPULATE_HORIZONTAL));

	ArrayList<String> MEASURED_DATA_HEADER_KEY_LIST = new ArrayList<String>(Arrays.asList(
			"Target Voltage", "Target Current", "Target PF", "Target Power",
			"RSM Actual Voltage", "RSM Actual Current", "RSM Actual PF", "RSM Actual Power",
			DUT_KEY + " Actual Voltage", DUT_KEY + " Actual Current", DUT_KEY + " Actual PF",
			DUT_KEY + " Actual Power"));

	private String VOLTAGE_PERCENT_PLACE_HOLDER = "%";
	private String VOLTAGE_UNIT_TERMNATOR = VOLTAGE_PERCENT_PLACE_HOLDER + ConstantReport.EXTENSION_TYPE_VOLTAGE_U;

	// ArrayList<String> OPERATION_METHOD_LIST = new
	// ArrayList<String>(Arrays.asList(ConstantReportV2.NONE_DISPLAYED,"Maximum","Minimum","Difference","Multiply","Average","ErrorPercentage"));

	// ArrayList<String> POST_OPERATION_METHOD_LIST = new
	// ArrayList<String>(Arrays.asList("+","-","*","/"));
	/*
	 * ArrayList<String> POST_OPERATION_METHOD_LIST = new
	 * ArrayList<String>(Arrays.asList("Add","Subtract","Multiply","Divide"));
	 */

	// private String OPERATION_MASTER_OUTPUT_HEADER_DISPLAY_STATUS_PREFIX =
	// "MasterOutputStatus";

	private String OPERATION_INPUT_DATA_HEADER_KEY_PREFIX = "OperLocalInData";
	private String OPERATION_LOCAL_OUTPUT_DATA_HEADER_KEY_PREFIX = "OperLocalOutData";
	private String OPERATION_LOCAL_OUTPUT_DATA_HEADER_DISPLAY_PREFIX = "LocalOutputSetData";

	private String OPERATION_LOCAL_OUTPUT_STATUS_HEADER_DISPLAY_PREFIX = "LocalOutputSetStatus";

	private String OPERATION_MASTER_OUTPUT_DATA_HEADER_KEY_PREFIX = "OperMasterOutputData";
	private String OPERATION_MASTER_OUTPUT_DATA_HEADER_DISPLAY_PREFIX = "MasterOutputSetData";

	private String OPERATION_MASTER_OUTPUT_STATUS_HEADER_KEY_PREFIX = "OperMasterOutputStatus";
	private String OPERATION_MASTER_OUTPUT_STATUS_HEADER_DISPLAY_PREFIX = "MasterOutputSetStatus";

	String REPLICATE_RESULT_KEY_PREFIX = "Replicate Result Value";

	ArrayList<String> cellStartPositionReplicateHeader = new ArrayList<String>(
			Arrays.asList(REPLICATE_RESULT_KEY_PREFIX + "01",
					REPLICATE_RESULT_KEY_PREFIX + "02",
					REPLICATE_RESULT_KEY_PREFIX + "03",
					REPLICATE_RESULT_KEY_PREFIX + "04",
					REPLICATE_RESULT_KEY_PREFIX + "05",
					REPLICATE_RESULT_KEY_PREFIX + "06",
					REPLICATE_RESULT_KEY_PREFIX + "07",
					REPLICATE_RESULT_KEY_PREFIX + "08",
					REPLICATE_RESULT_KEY_PREFIX + "09",
					REPLICATE_RESULT_KEY_PREFIX + "10"));

	ArrayList<String> cellStartPositionReplicateCell = new ArrayList<String>(Arrays.asList("",
			"", "", "", "",
			"", "", "", "",
			""));
	public static ArrayList<String> presentInputProcessList = new ArrayList<String>();

	private ObservableList<ExcelCellValueModel> cellStartPositionPageCellValues = FXCollections.observableArrayList();
	private ObservableList<RpPrintPosition> cellStartPositionPageCellValues2 = FXCollections.observableArrayList();

	private ObservableList<ReportMeterMetaDataTypeSubModel> selectedMeterData = FXCollections.observableArrayList();

	private ObservableList<ReportMeterMetaDataTypeSubModel> measuredData = FXCollections.observableArrayList();

	@FXML
	private TableColumn<RpPrintPosition, String> colTestFilterCellStartPositionPageHeader;

	@FXML
	private TableColumn<RpPrintPosition, String> colTestFilterCellStartPositionPageCellValue;

	@FXML
	private TableColumn<ExcelCellValueModel, String> colMeterMetaDataCellHeaderPositionPageHeader;

	@FXML
	private TableColumn<ExcelCellValueModel, String> colMeterMetaDataCellHeaderPositionPageCellValue;

	@FXML
	private TableColumn<ExcelCellValueModel, String> colMeterMetaDataCellStartPositionPageHeader;

	@FXML
	private TableColumn<ExcelCellValueModel, String> colMeterMetaDataCellStartPositionPageCellValue;

	@FXML
	private TableColumn<RpPrintPosition, String> colTestFilterCellHeaderPositionPageHeader;

	@FXML
	private TableColumn<RpPrintPosition, String> colTestFilterCellHeaderPositionPageTitleValue;

	@FXML
	private TableColumn<RpPrintPosition, String> colTestFilterCellHeaderPositionPageCellValue;

	@FXML
	private TableColumn<ReportMeterMetaDataTypeSubModel, String> colSelectedMeterDataSerialNo;
	@FXML
	private TableColumn<ReportMeterMetaDataTypeSubModel, String> colSelectedMeterDataType;
	@FXML
	private TableColumn colSelectedMeterPopulateData;
	@FXML
	private TableColumn colSelectedMeterPopulateForEachDut;
	@FXML
	private TableColumn colPopulateDataSelection;

	@FXML
	private TableColumn<ReportMeterMetaDataTypeSubModel, String> colMeasuredDataSerialNo;
	@FXML
	private TableColumn<ReportMeterMetaDataTypeSubModel, String> colMeasuredDataKey;
	@FXML
	private TableColumn colMeasuredDataPopulateData;
	// @FXML private TableColumn colDummyDisplay;

	@FXML
	private ComboBox<String> cmbBxTestType;

	@FXML
	private ComboBox<String> cmbBxTestTypeSub;

	@FXML
	private ComboBox<String> cmBxPageNumber;

	// @FXML
	// private TextField txt_value;

	// @FXML
	// private TextField txt_reference_value;

	// @FXML
	// private TextField txt_reference_extension;

	@FXML
	private Label lblPageName;
	@FXML
	private Label lblPageNumber;
	@FXML
	private Label lblMaxDutDisplayPerPage;

	// @FXML
	// private ComboBox<String> cmbBox_extension;

	// @FXML private RadioButton rdBtnPopulateVertical ;
	// @FXML private RadioButton rdBtnPopulateHorizontal ;
	@FXML
	private CheckBox chkBxEnableFilter;

	// @FXML private CheckBox chkBxRefStdInitialRegister ;
	// @FXML private CheckBox chkBxRefStdFinalRegister ;
	// @FXML private CheckBox chkBxDutPulseCount ;
	// @FXML private CheckBox chkBxDutInitialRegister ;
	// @FXML private CheckBox chkBxDutFinalRegister ;
	// @FXML private CheckBox chkBxErrorResultValue ;
	@FXML
	private CheckBox chkBxPopulateDutErrorResultStatus;
	// @FXML private TextField txtTestTypeAlias ;
	@FXML
	private TextField txtVoltPercentFilterUserEntry;
	@FXML
	private TextField txtCurrentPercentFilterUserEntry;
	@FXML
	private TextField txtPfFilterUserEntry;
	@FXML
	private TextField txtFreqFilterUserEntry;
	@FXML
	private TextField txtEnergyFilterUserEntry;
	// @FXML private TextField txtRefVoltPercentFilterUserEntry ;
	// @FXML private TextField txtRefCurrentPercentFilterUserEntry ;
	// @FXML private TextField txtRefFreqFilterUserEntry ;
	@FXML
	private TextField txtVoltPercentFilterData;
	@FXML
	private TextField txtCurrentPercentFilterData;
	@FXML
	private TextField txtPfFilterData;
	@FXML
	private TextField txtFreqFilterData;
	@FXML
	private TextField txtEnergyFilterData;
	// @FXML private TextField txtRefVoltPercentFilterData ;
	// @FXML private TextField txtRefCurrentPercentFilterData ;
	// @FXML private TextField txtRefFreqFilterData ;
	// @FXML private TextField txtErrorValueCellStartPosition ;
	// @FXML private TextField txtErrorStatusCellStartPosition ;
	@FXML
	private TextField txtTestFilterPageNumber;
	@FXML
	private TextField txtMeterMetaDataPageNumber;
	@FXML
	private TextField txtMeterMetaDataPageName;
	@FXML
	private Label lbl_MeterProfileMetaDataPageName;

	@FXML
	private TextField txtIterationReadingStartingId;
	@FXML
	private TextField txtIterationReadingEndingId;
	@FXML
	private ComboBox cmbBxVoltPercentFilterUserEntry;
	@FXML
	private ComboBox cmbBxCurrentPercentFilterUserEntry;
	@FXML
	private ComboBox cmbBxPfFilterUserEntry;
	@FXML
	private ComboBox cmbBxFreqFilterUserEntry;
	@FXML
	private ComboBox cmbBxEnergyFilterUserEntry;
	// @FXML private ComboBox cmbBxRefVoltPercentFilterUserEntry ;
	// @FXML private ComboBox cmbBxRefCurrentPercentFilterUserEntry ;
	// @FXML private ComboBox cmbBxRefFreqFilterUserEntry ;
	@FXML
	private ComboBox cmbBxExecutionResultType;
	@FXML
	private ComboBox cmbBxOperationSourceParamType;

	@FXML
	private TextField txtHeader3Value;
	// @FXML private TextField txtHeader3CellPosition;
	@FXML
	private CheckBox chkBxPopulateHeader3;
	@FXML
	private CheckBox chkBxHeader3VoltageFilter;
	@FXML
	private CheckBox chkBxHeader3CurrentFilter;
	@FXML
	private CheckBox chkBxHeader3PfFilter;
	@FXML
	private CheckBox chkBxHeader3FreqFilter;
	@FXML
	private CheckBox chkBxHeader3EnergyFilter;
	@FXML
	private CheckBox chkBxHeader3IterationIdFilter;
	@FXML
	private CheckBox chkBxHeader3CustomAllowed;

	@FXML
	private TextField txt_IterationIdHeaderPrefix;
	@FXML
	private CheckBox chkBx_IterationIdHeaderPrefix;

	@FXML
	private CheckBox chkBxPopulateHeader1;
	@FXML
	private CheckBox chkBxPopulateHeader2;
	// @FXML private CheckBox chkBxPopulateVertical ;
	// @FXML private CheckBox chkBxOperationInput ;
	// @FXML private CheckBox chkBxOperationOutput ;
	@FXML
	private CheckBox chkBxReplicateData;
	// @FXML private TextField txtHeader1CellPosition;
	// @FXML private TextField txtHeader2CellPosition;
	@FXML
	private TextField txtHeader1Value;
	@FXML
	private TextField txtHeader2Value;
	@FXML
	private ComboBox cmbBxReplicateCountUserEntry;
	@FXML
	private ComboBox cmbBxOperationInputUserEntry;
	// @FXML private ComboBox cmbBxOperationOutputUserEntry ;
	@FXML
	private ComboBox cmbBxOperationCriteriaInputData;
	@FXML
	private ComboBox cmbBxOperationCriteriaLocalOutputData;
	@FXML
	private ComboBox cmbBxOperationCriteriaProcessData;
	@FXML
	private TableView<RpPrintPosition> tvTestFilterCellStartPosition;
	@FXML
	private TableView<RpPrintPosition> tvTestFilterCellHeaderPosition;

	@FXML
	private TableView<ExcelCellValueModel> tvMeterMetaDataCellStartPosition;
	@FXML
	private TableView<ExcelCellValueModel> tvMeterMetaDataCellHeaderPosition;

	@FXML
	private TextField txtFilterComments;
	@FXML
	private TableView<ReportMeterMetaDataTypeSubModel> tvSelectedMeterDataType;
	@FXML
	private TableView<ReportMeterMetaDataTypeSubModel> tvSelectedTestFilterMeasuredData;

	@FXML
	private TableView<ReportMeterMetaDataTypeSubModel> tvMeterMetaDataList;
	@FXML
	private TableView<ReportProfileTestDataFilter> tvTestFilterDataList;

	/*
	 * colSelectedMeterDataType
	 * colSelectedMeterPopulateData
	 * colSelectedMeterPopulateForEachDut
	 */

	// @FXML private TableView<String> tvInputProcessList;

	@FXML
	private ListView<String> listViewInputProcessList;

	// @FXML private TableView tvOperationProcessList;
	// @FXML private TableView tvOutputProcessList;

	@FXML
	private TitledPane titledPanePopulateData;
	@FXML
	private TitledPane titledPaneTestTypeData;
	@FXML
	private TitledPane titledPaneOperation;
	@FXML
	private TitledPane titledPaneCellPosition;

	@FXML
	private RadioButton rdBtnOperationNone;
	@FXML
	private RadioButton rdBtnOperationInput;
	@FXML
	private RadioButton rdBtnOperationOutput;

	@FXML
	private TextField txtFilterName;
	@FXML
	private TextField txtAllowedUpperLimit;
	@FXML
	private TextField txtAllowedLowerLimit;
	@FXML
	private CheckBox chkBxCompareWithLimits;
	@FXML
	private CheckBox chkBxPopulateLocalOutputData;
	@FXML
	private CheckBox chkBxPopulateUpperLimitData;
	@FXML
	private CheckBox chkBxPopulateLowerLimitData;
	@FXML
	private CheckBox chkBxPopulateComparedLocalResultStatus;
	@FXML
	private CheckBox chkBxPopulateComparedMasterResultStatus;

	@FXML
	private ComboBox cmbBxOperationComparedLocalResultStatusOutput;
	@FXML
	private ComboBox cmbBxOperationComparedMasterResultStatusOutput;

	@FXML
	private TitledPane titledPaneCellHeaderPosition;
	@FXML
	private CheckBox chkBxDiscardRackPositionInDutSerialNumber;
	@FXML
	private CheckBox chkBxAppendMeterSerialNoAndRackPositionListDisplay;

	@FXML
	private CheckBox chkBxMergeUpperLowerLimit;
	@FXML
	private ComboBox cmbBxOperationCriteriaMasterOutputData;
	@FXML
	private CheckBox chkBxPopulateMasterOutputData;
	// @FXML private CheckBox chkBxOperationActive;
	@FXML
	private ComboBox cmbBxBaseTemplate;
	@FXML
	private ComboBox cmbBxMeterMetaDataPopulateType;
	@FXML
	private ComboBox cmbBxTestFilterDataPopulateType;

	@FXML
	private TitledPane titledPaneMeasuredData;
	@FXML
	private Accordion accordTestFilter;

	@FXML
	private Button btnInputAdd;
	@FXML
	private Button btnInputDelete;
	@FXML
	private TitledPane titledPaneMeterDataCellPosition;
	@FXML
	private TitledPane titledPaneMeterDataCellHeaderPosition;

	@FXML
	private TableColumn<ReportMeterMetaDataTypeSubModel, String> colMeterMetaDataListSerialNo;
	@FXML
	private TableColumn<ReportMeterMetaDataTypeSubModel, Integer> colMeterMetaDataListPageNo;
	@FXML
	private TableColumn<ReportMeterMetaDataTypeSubModel, String> colMeterMetaDataListMeterDataType;
	@FXML
	private TableColumn colMeterMetaDataListPopulateOnlyHeader;
	@FXML
	private TableColumn colMeterMetaDataListPopulateForEachDut;
	@FXML
	private TableColumn<ReportMeterMetaDataTypeSubModel, String> colMeterMetaDataListCellPosition;

	@FXML
	private TableColumn<ReportProfileTestDataFilter, String> colTestFilterListSerialNo;
	@FXML
	private TableColumn<ReportProfileTestDataFilter, String> colTestFilterListPageNo;
	@FXML
	private TableColumn<ReportProfileTestDataFilter, String> colTestFilterListFilterName;
	@FXML
	private TableColumn colTestFilterListFilterActive;
	@FXML
	private TableColumn<ReportProfileTestDataFilter, String> colTestFilterListTestType;
	@FXML
	private TableColumn<ReportProfileTestDataFilter, String> colTestFilterListFilterPreview;
	// @FXML private TableColumn<ReportProfileTestDataFilter, String>
	// colTestFilterListFilterUnit;
	@FXML
	private TableColumn<ReportProfileTestDataFilter, String> colTestFilterListIterationId;
	@FXML
	private TableColumn<ReportProfileTestDataFilter, String> colTestFilterListOperationMode;
	@FXML
	private TableColumn<ReportProfileTestDataFilter, String> colTestFilterListExecutionResultType;
	// @FXML private TableColumn<ReportProfileTestDataFilter, String>
	// colTestFilterListRsmDataType;
	@FXML
	private TableColumn<ReportProfileTestDataFilter, String> colTestFilterListResultDataType;
	@FXML
	private TableColumn<ReportProfileTestDataFilter, String> colTestFilterListResultValueCellPosition;
	@FXML
	private TableColumn<ReportProfileTestDataFilter, String> colTestFilterListResultStatusCellPosition;
	@FXML
	private TableColumn<ReportProfileTestDataFilter, String> colTestFilterListHeader1CellPosition;
	@FXML
	private TableColumn<ReportProfileTestDataFilter, String> colTestFilterListHeader2CellPosition;
	@FXML
	private TableColumn<ReportProfileTestDataFilter, String> colTestFilterListHeader3CellPosition;

	@FXML
	private TableColumn<ReportProfileTestDataFilter, String> colTestFilterListTestTypeAlias;
	@FXML
	private TableColumn<ReportProfileTestDataFilter, String> colTestFilterListUpperLimitCellPosition;
	@FXML
	private TableColumn<ReportProfileTestDataFilter, String> colTestFilterListLowerLimitCellPosition;

	@FXML
	private Label lblCustomTestName;
	@FXML
	private TextField txtCustomTestNameUserEntry;
	@FXML
	private TextField txtCustomTestNameData;

	@FXML
	private TableColumn<ReportProfileTestDataFilter, String> colTestFilterListNonDisplayedDataSet;
	@FXML
	private TableColumn colTestFilterListMergeUpperLowerLimits;
	// @FXML private TableColumn<ReportProfileTestDataFilter, String>
	// colTestFilterListMergedLimitCellPosition;
	@FXML
	private TableColumn colTestFilterListReplicateData;
	@FXML
	private TableColumn colTestFilterListReplicateDataCellPosition;
	@FXML
	private TableColumn<ReportProfileTestDataFilter, String> colTestFilterListOperationMethod;
	@FXML
	private TableColumn colTestFilterListInputProcessDataList;

	@FXML
	private TitledPane titledPaneExecutionMode;
	@FXML
	private RadioButton rdBtnMainCt;
	@FXML
	private RadioButton rdBtnNeutralCt;
	@FXML
	private RadioButton rdBtnImportMode;
	@FXML
	private RadioButton rdBtnExportMode;

	@FXML
	private CheckBox chkBxPostOperationActive;
	@FXML
	private TextField txtPostOperationValue;
	@FXML
	private ComboBox cmbBxPostOperationMethod;

	@FXML
	private Button btnReportProfileSave;
	@FXML
	private Button btnReportProfileLoad;
	@FXML
	private Button btnReportProfileAdd;
	@FXML
	private Button btnReportProfileDelete;
	@FXML
	private Button btnReportProfileEdit;

	@FXML
	private TitledPane titledPaneTestFilterData;
	@FXML
	private TitledPane titledPaneMeterProfileMetaData;

	@FXML
	private Tab tabMeterProfileDataList;
	@FXML
	private Tab tabTestFilterDataList;

	@FXML
	private ComboBox cmbBxReportProfileGroup;
	@FXML
	private ComboBox cmbBxMetaDataPageName;
	@FXML
	private CheckBox chkBxMeterProfileMetaDataPageActive;
	@FXML
	private CheckBox chkBxMeterProfileMetaDataListPageActive;

	@FXML
	private CheckBox chkBxDutMetaDataApplyForAllPages;
	@FXML
	private CheckBox chkBxDutMetaDataClubPageNoAndMaxNoOfPages;
	@FXML
	private CheckBox chkBxSplitDutDisplayInToMultiplePage;
	@FXML
	private TextField txtMaxDutDisplayPerPage;
	@FXML
	private TextField txtTemplateFileNameWithPath;
	@FXML
	private TextField txtOutputPath;

	@FXML
	private Button btnAddGroupProfile;
	@FXML
	private Button btnSaveGroupProfile;
	@FXML
	private GridPane gridPaneControl;

	@FXML
	private ComboBox cmbBxParameterProfileName;

	@FXML
	private ComboBox cmbBxResultPrintStyleName;
	@FXML
	private ComboBox cmbBxGenericHeaderPrintStyleName;
	@FXML
	private ComboBox cmbBxTableHeaderPrintStyleName;

	@FXML
	private CheckBox chkBxPopulateHeaderTestPeriod;
	@FXML
	private CheckBox chkBxPopulateHeaderWarmupPeriod;
	@FXML
	private CheckBox chkBxPopulateHeaderActualVoltage;
	@FXML
	private CheckBox chkBxPopulateHeaderActualCurrent;
	@FXML
	private CheckBox chkBxPopulateHeaderActualPf;
	@FXML
	private CheckBox chkBxPopulateHeaderActualFreq;
	@FXML
	private CheckBox chkBxPopulateHeaderActualEnergy;
	@FXML
	private Label lblReplicateCount;

	@FXML
	private CheckBox chkBxPopulateRsmInitial;
	@FXML
	private CheckBox chkBxPopulateRsmFinal;
	@FXML
	private CheckBox chkBxPopulateRsmDifference;
	@FXML
	private CheckBox chkBxPopulateDutInitial;
	@FXML
	private CheckBox chkBxPopulateDutFinal;
	@FXML
	private CheckBox chkBxPopulateDutDifference;
	@FXML
	private CheckBox chkBxPopulateDutPulseCount;
	@FXML
	private CheckBox chkBxPopulateDutCalcErrorPercentage;
	@FXML
	private CheckBox chkBxPopulateDutOnePulsePeriod;

	@FXML
	private CheckBox chkBxPopulateDutAverageValue;
	@FXML
	private CheckBox chkBxPopulateDutAverageStatus;
	@FXML
	private CheckBox chkBxPopulateHeader4;
	@FXML
	private CheckBox chkBxPopulateHeader5;
	@FXML
	private TitledPane titledPaneDutResultPopulateType;
	@FXML
	private TextField txtHeader4Value;
	@FXML
	private TextField txtHeader5Value;

	// public static RadioButton ref_rdBtnPopulateVertical;
	// public static RadioButton ref_rdBtnPopulateHorizontal;
	public static CheckBox ref_chkBxEnableFilter;
	// public static CheckBox ref_chkBxRefStdInitialRegister;
	// public static CheckBox ref_chkBxRefStdFinalRegister;
	// public static CheckBox ref_chkBxDutPulseCount;
	// public static CheckBox ref_chkBxDutInitialRegister;
	// public static CheckBox ref_chkBxDutFinalRegister;
	// public static CheckBox ref_chkBxErrorResultValue;
	public static ComboBox ref_cmBxPageNumber;
	public static CheckBox ref_chkBxPopulateDutErrorResultStatus;
	// public static TextField ref_txtTestTypeAlias;
	public static TextField ref_txtVoltPercentFilterUserEntry;
	public static TextField ref_txtCurrentPercentFilterUserEntry;
	public static TextField ref_txtPfFilterUserEntry;
	public static TextField ref_txtFreqFilterUserEntry;
	public static TextField ref_txtEnergyFilterUserEntry;
	// public static TextField ref_txtRefVoltPercentFilterUserEntry;
	// public static TextField ref_txtRefCurrentPercentFilterUserEntry;
	// public static TextField ref_txtRefFreqFilterUserEntry;
	public static TextField ref_txtVoltPercentFilterData;
	public static TextField ref_txtCurrentPercentFilterData;
	public static TextField ref_txtPfFilterData;
	public static TextField ref_txtFreqFilterData;
	public static TextField ref_txtEnergyFilterData;
	// public static TextField ref_txtRefVoltPercentFilterData;
	// public static TextField ref_txtRefCurrentPercentFilterData;
	// public static TextField ref_txtRefFreqFilterData;
	// public static TextField ref_txtErrorValueCellStartPosition;
	// public static TextField ref_txtErrorStatusCellStartPosition;
	public static TextField ref_txtTestFilterPageNumber;
	public static TextField ref_txtMeterMetaDataPageNumber;
	public static TextField ref_txtMeterMetaDataPageName;
	public static Label ref_lbl_MeterProfileMetaDataPageName;
	public static TextField ref_txtIterationReadingStartingId;
	public static TextField ref_txtIterationReadingEndingId;
	public static ComboBox ref_cmbBxVoltPercentFilterUserEntry;
	public static ComboBox ref_cmbBxCurrentPercentFilterUserEntry;
	public static ComboBox ref_cmbBxPfFilterUserEntry;
	public static ComboBox ref_cmbBxFreqFilterUserEntry;
	public static ComboBox ref_cmbBxEnergyFilterUserEntry;
	// public static ComboBox ref_cmbBxRefVoltPercentFilterUserEntry;
	// public static ComboBox ref_cmbBxRefCurrentPercentFilterUserEntry;
	// public static ComboBox ref_cmbBxRefFreqFilterUserEntry;
	public static ComboBox ref_cmbBxExecutionResultType;
	public static ComboBox ref_cmbBxOperationSourceParamType;

	public static TextField ref_txtHeader3Value;
	// public static TextField ref_txtHeader3CellPosition;
	public static CheckBox ref_chkBxPopulateHeader3;

	public static CheckBox ref_chkBxHeader3VoltageFilter;
	public static CheckBox ref_chkBxHeader3CurrentFilter;
	public static CheckBox ref_chkBxHeader3PfFilter;
	public static CheckBox ref_chkBxHeader3FreqFilter;
	public static CheckBox ref_chkBxHeader3EnergyFilter;
	public static CheckBox ref_chkBxHeader3IterationIdFilter;
	public static CheckBox ref_chkBxHeader3CustomAllowed;

	public static TextField ref_txt_IterationIdHeaderPrefix;
	public static CheckBox ref_chkBx_IterationIdHeaderPrefix;

	public static CheckBox ref_chkBxPopulateHeader1;
	public static CheckBox ref_chkBxPopulateHeader2;
	// public static CheckBox ref_chkBxPopulateVertical ;
	// public static CheckBox ref_chkBxOperationInput ;
	// public static CheckBox ref_chkBxOperationOutput ;
	public static CheckBox ref_chkBxReplicateData;
	// public static TextField ref_txtHeader1CellPosition;
	// public static TextField ref_txtHeader2CellPosition;
	public static TextField ref_txtHeader1Value;
	public static TextField ref_txtHeader2Value;
	public static ComboBox ref_cmbBxReplicateCountUserEntry;
	public static ComboBox ref_cmbBxOperationInputUserEntry;
	// public static ComboBox ref_cmbBxOperationOutputUserEntry ;
	public static ComboBox ref_cmbBxOperationCriteriaInputData;
	public static ComboBox ref_cmbBxOperationCriteriaLocalOutputData;
	public static ComboBox ref_cmbBxOperationCriteriaProcessData;
	public static TableView<RpPrintPosition> ref_tvTestFilterCellStartPosition;
	public static TableView<RpPrintPosition> ref_tvTestFilterCellHeaderPosition;

	public static TableView<ExcelCellValueModel> ref_tvMeterMetaDataCellStartPosition;
	public static TableView<ExcelCellValueModel> ref_tvMeterMetaDataCellHeaderPosition;

	public static TableView<ReportMeterMetaDataTypeSubModel> ref_tvSelectedMeterDataType;
	public static TableView<ReportMeterMetaDataTypeSubModel> ref_tvSelectedTestFilterMeasuredData;

	public static TableView<ReportMeterMetaDataTypeSubModel> ref_tvMeterMetaDataList;
	public static TableView<ReportProfileTestDataFilter> ref_tvTestFilterDataList;

	// public static TableView<String> ref_tvInputProcessList;
	public static ListView<String> ref_listViewInputProcessList;

	public static Button ref_btnInputAdd;
	public static Button ref_btnInputDelete;

	// public static TableView ref_tvOperationProcessList;
	// public static TableView ref_tvOutputProcessList;

	public static TitledPane ref_titledPanePopulateData;
	public static TitledPane ref_titledPaneTestTypeData;
	public static TitledPane ref_titledPaneOperation;
	public static TitledPane ref_titledPaneCellPosition;

	public static RadioButton ref_rdBtnOperationNone;
	public static RadioButton ref_rdBtnOperationInput;
	public static RadioButton ref_rdBtnOperationOutput;

	public static ComboBox ref_cmbBxTestType;
	public static ComboBox ref_cmbBxTestTypeSub;

	public static TextField ref_txtFilterName;
	public static TextField ref_txtAllowedUpperLimit;
	public static TextField ref_txtAllowedLowerLimit;
	public static CheckBox ref_chkBxPopulateLocalOutputData;
	public static CheckBox ref_chkBxPopulateUpperLimitData;
	public static CheckBox ref_chkBxPopulateLowerLimitData;
	public static CheckBox ref_chkBxCompareWithLimits;
	public static CheckBox ref_chkBxPopulateComparedLocalResultStatus;
	public static CheckBox ref_chkBxPopulateComparedMasterResultStatus;
	public static ComboBox ref_cmbBxOperationComparedLocalResultStatusOutput;
	public static ComboBox ref_cmbBxOperationComparedMasterResultStatusOutput;

	public static TitledPane ref_titledPaneCellHeaderPosition;
	public static TextField ref_txtFilterComments;

	public static CheckBox ref_chkBxDiscardRackPositionInDutSerialNumber;
	public static CheckBox ref_chkBxAppendMeterSerialNoAndRackPositionListDisplay;
	public static CheckBox ref_chkBxMergeUpperLowerLimit;
	public static ComboBox ref_cmbBxOperationCriteriaMasterOutputData;
	public static CheckBox ref_chkBxPopulateMasterOutputData;
	// public static CheckBox ref_chkBxOperationActive;
	public static ComboBox ref_cmbBxBaseTemplate;
	public static ComboBox ref_cmbBxMeterMetaDataPopulateType;
	public static ComboBox ref_cmbBxTestFilterDataPopulateType;

	public static TitledPane ref_titledPaneMeasuredData;
	public static Accordion ref_accordTestFilter;

	public static TitledPane ref_titledPaneMeterDataCellPosition;

	public static TableColumn<RpPrintPosition, String> ref_colTestFilterCellStartPositionPageHeader;
	public static TableColumn<RpPrintPosition, String> ref_colTestFilterCellStartPositionPageCellValue;

	public static TableColumn<RpPrintPosition, String> ref_colTestFilterCellHeaderPositionPageHeader;
	public static TableColumn<RpPrintPosition, String> ref_colTestFilterCellHeaderPositionPageTitleValue;

	public static TableColumn<RpPrintPosition, String> ref_colTestFilterCellHeaderPositionPageCellValue;

	public static TableColumn<ExcelCellValueModel, String> ref_colMeterMetaDataCellStartPositionPageHeader;
	public static TableColumn<ExcelCellValueModel, String> ref_colMeterMetaDataCellStartPositionPageCellValue;

	public static TableColumn<ExcelCellValueModel, String> ref_colMeterMetaDataCellHeaderPositionPageHeader;
	public static TableColumn<ExcelCellValueModel, String> ref_colMeterMetaDataCellHeaderPositionPageCellValue;

	public static TableColumn<ReportMeterMetaDataTypeSubModel, String> ref_colSelectedMeterDataSerialNo;
	public static TableColumn<ReportMeterMetaDataTypeSubModel, String> ref_colSelectedMeterDataType;
	public static TableColumn ref_colSelectedMeterPopulateData;
	public static TableColumn ref_colSelectedMeterPopulateForEachDut;
	public static TableColumn ref_colPopulateDataSelection;

	public static TableColumn<ReportMeterMetaDataTypeSubModel, String> ref_colMeasuredDataSerialNo;
	public static TableColumn<ReportMeterMetaDataTypeSubModel, String> ref_colMeasuredDataKey;
	public static TableColumn ref_colMeasuredDataPopulateData;

	public static TitledPane ref_titledPaneMeterDataCellHeaderPosition;

	public static TableColumn<ReportMeterMetaDataTypeSubModel, String> ref_colMeterMetaDataListSerialNo;
	public static TableColumn<ReportMeterMetaDataTypeSubModel, Integer> ref_colMeterMetaDataListPageNo;
	public static TableColumn<ReportMeterMetaDataTypeSubModel, String> ref_colMeterMetaDataListMeterDataType;
	public static TableColumn ref_colMeterMetaDataListPopulateOnlyHeader;
	public static TableColumn ref_colMeterMetaDataListPopulateForEachDut;
	public static TableColumn<ReportMeterMetaDataTypeSubModel, String> ref_colMeterMetaDataListCellPosition;

	public static Label ref_lblCustomTestName;
	public static TextField ref_txtCustomTestNameUserEntry;
	public static TextField ref_txtCustomTestNameData;

	public static TableColumn<ReportProfileTestDataFilter, String> ref_colTestFilterListSerialNo;
	public static TableColumn<ReportProfileTestDataFilter, String> ref_colTestFilterListPageNo;
	public static TableColumn<ReportProfileTestDataFilter, String> ref_colTestFilterListFilterName;
	public static TableColumn ref_colTestFilterListFilterActive;
	public static TableColumn<ReportProfileTestDataFilter, String> ref_colTestFilterListTestType;
	public static TableColumn<ReportProfileTestDataFilter, String> ref_colTestFilterListFilterPreview;
	// public static TableColumn<ReportProfileTestDataFilter, String>
	// ref_colTestFilterListFilterUnit;
	public static TableColumn<ReportProfileTestDataFilter, String> ref_colTestFilterListIterationId;
	public static TableColumn<ReportProfileTestDataFilter, String> ref_colTestFilterListOperationMode;
	public static TableColumn<ReportProfileTestDataFilter, String> ref_colTestFilterListExecutionResultType;
	// public static TableColumn<ReportProfileTestDataFilter, String>
	// ref_colTestFilterListRsmDataType;
	public static TableColumn<ReportProfileTestDataFilter, String> ref_colTestFilterListResultDataType;
	public static TableColumn<ReportProfileTestDataFilter, String> ref_colTestFilterListResultValueCellPosition;
	public static TableColumn<ReportProfileTestDataFilter, String> ref_colTestFilterListResultStatusCellPosition;
	public static TableColumn<ReportProfileTestDataFilter, String> ref_colTestFilterListHeader1CellPosition;
	public static TableColumn<ReportProfileTestDataFilter, String> ref_colTestFilterListHeader2CellPosition;
	public static TableColumn<ReportProfileTestDataFilter, String> ref_colTestFilterListHeader3CellPosition;

	public static TableColumn<ReportProfileTestDataFilter, String> ref_colTestFilterListTestTypeAlias;
	public static TableColumn<ReportProfileTestDataFilter, String> ref_colTestFilterListUpperLimitCellPosition;
	public static TableColumn<ReportProfileTestDataFilter, String> ref_colTestFilterListLowerLimitCellPosition;

	public static TableColumn<ReportProfileTestDataFilter, String> ref_colTestFilterListNonDisplayedDataSet;
	public static TableColumn ref_colTestFilterListMergeUpperLowerLimits;
	// public static TableColumn<ReportProfileTestDataFilter, String>
	// ref_colTestFilterListMergedLimitCellPosition;
	public static TableColumn ref_colTestFilterListReplicateData;
	public static TableColumn ref_colTestFilterListReplicateDataCellPosition;
	public static TableColumn<ReportProfileTestDataFilter, String> ref_colTestFilterListOperationMethod;
	public static TableColumn ref_colTestFilterListInputProcessDataList;
	// public static TableColumn ref_colDummyDisplay;

	public static TitledPane ref_titledPaneExecutionMode;
	public static RadioButton ref_rdBtnMainCt;
	public static RadioButton ref_rdBtnNeutralCt;
	public static RadioButton ref_rdBtnImportMode;
	public static RadioButton ref_rdBtnExportMode;

	public static CheckBox ref_chkBxPostOperationActive;
	public static TextField ref_txtPostOperationValue;
	public static ComboBox ref_cmbBxPostOperationMethod;

	public static Button ref_btnReportProfileSave;
	public static Button ref_btnReportProfileLoad;
	public static Button ref_btnReportProfileAdd;
	public static Button ref_btnReportProfileDelete;
	public static Button ref_btnReportProfileEdit;

	public static TitledPane ref_titledPaneTestFilterData;
	public static TitledPane ref_titledPaneMeterProfileMetaData;

	public static Tab ref_tabMeterProfileDataList;
	public static Tab ref_tabTestFilterDataList;

	public static ComboBox ref_cmbBxReportProfileGroup;

	public static ComboBox ref_cmbBxMetaDataPageName;

	public static CheckBox ref_chkBxMeterProfileMetaDataPageActive;
	public static CheckBox ref_chkBxMeterProfileMetaDataListPageActive;

	public static CheckBox ref_chkBxDutMetaDataApplyForAllPages;
	public static CheckBox ref_chkBxDutMetaDataClubPageNoAndMaxNoOfPages;
	public static CheckBox ref_chkBxSplitDutDisplayInToMultiplePage;
	public static TextField ref_txtMaxDutDisplayPerPage;
	public static TextField ref_txtTemplateFileNameWithPath;
	public static TextField ref_txtOutputPath;

	public static Button ref_btnAddGroupProfile;
	public static Button ref_btnSaveGroupProfile;

	public static GridPane ref_gridPaneControl;

	public static ComboBox ref_cmbBxParameterProfileName;

	public static Label ref_lblPageName;
	public static Label ref_lblPageNumber;
	public static Label ref_lblMaxDutDisplayPerPage;

	public static ComboBox ref_cmbBxResultPrintStyleName;
	public static ComboBox ref_cmbBxGenericHeaderPrintStyleName;
	public static ComboBox ref_cmbBxTableHeaderPrintStyleName;

	public static CheckBox ref_chkBxPopulateHeaderTestPeriod;
	public static CheckBox ref_chkBxPopulateHeaderWarmupPeriod;
	public static CheckBox ref_chkBxPopulateHeaderActualVoltage;
	public static CheckBox ref_chkBxPopulateHeaderActualCurrent;
	public static CheckBox ref_chkBxPopulateHeaderActualPf;
	public static CheckBox ref_chkBxPopulateHeaderActualFreq;
	public static CheckBox ref_chkBxPopulateHeaderActualEnergy;

	public static Label ref_lblReplicateCount;

	private static CheckBox ref_chkBxPopulateRsmInitial;
	private static CheckBox ref_chkBxPopulateRsmFinal;
	private static CheckBox ref_chkBxPopulateRsmDifference;
	private static CheckBox ref_chkBxPopulateDutInitial;
	private static CheckBox ref_chkBxPopulateDutFinal;
	private static CheckBox ref_chkBxPopulateDutDifference;
	private static CheckBox ref_chkBxPopulateDutPulseCount;
	private static CheckBox ref_chkBxPopulateDutCalcErrorPercentage;
	private static CheckBox ref_chkBxPopulateDutOnePulsePeriod;

	private static CheckBox ref_chkBxPopulateDutAverageValue;
	private static CheckBox ref_chkBxPopulateDutAverageStatus;
	private static CheckBox ref_chkBxPopulateHeader4;
	private static CheckBox ref_chkBxPopulateHeader5;

	private static TextField ref_txtHeader4Value;
	private static TextField ref_txtHeader5Value;

	// private static TitledPane ref_titledPaneDutResultPopulateType;

	// private HashMap<String,String> resultDataTypeHashMap = new
	// LinkedHashMap<String,String>();

	private BidiMap<String, String> operationInputDataHashBiMap = new DualHashBidiMap<String, String>();
	private BidiMap<String, String> operationLocalOutputDataHashBiMap = new DualHashBidiMap<String, String>();
	private BidiMap<String, String> operationMasterOutputDataHashBiMap = new DualHashBidiMap<String, String>();
	private BidiMap<String, String> operationMasterOutputStatusHashBiMap = new DualHashBidiMap<String, String>();
	// private HashMap<String,String> operationInputHashBiMap = new
	// LinkedHashMap<String,String>();
	// private HashMap<String,String> operationOutputHashBiMap = new
	// LinkedHashMap<String,String>();

	/*
	 * @FXML
	 * private ListView<String> listview_voltage;
	 * 
	 * @FXML
	 * private ListView<String> listview_current;
	 * 
	 * @FXML
	 * private ListView<String> listview_phase;
	 * 
	 * @FXML
	 * private ListView<String> listview_frequency;
	 */

	@FXML
	private TitledPane titledpane_freq_harm;

	// @FXML
	// private Button btn_add;

	// @FXML
	// private Button btn_delete;

	@FXML
	private ComboBox<String> cmbBxReportProfile;

	static ComboBox<String> ref_cmbBxReportProfile;

	private String selectedReportProfile = "";

	public int Max_Size = 0;

	public String headerSeperator = " ";

	@FXML
	private Button btnSave;

	private static ReportProfileManage reportProfileManageModel = new ReportProfileManage();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		refAssignment();
		dataInit();
		guiInit();

		readActiveReportProfileManageFromDataBase();
		loadReportProfileList();
		// LoadTestTypes();
		// LoadParameters();
		// loadExtensions();
		// UPF_listener();
		// txt_reference_extension.setEditable(false);
		displayOperationData();
		if (ProcalFeatureEnable.USER_ACCESS_CONTROL_ENABLED) {
			applyUacSettings();
		}
		hideGuiObjects();

		// setupDataForReportProfileManage();
		// readReportProfileManageFromDataBase();

	}

	private void setupDataForReportProfileManage() {

		ApplicationLauncher.logger.debug("setupDataForReportProfileManage : Entry");
		String baseTemplateMetaDataPopulateType = ref_cmbBxMeterMetaDataPopulateType.getSelectionModel()
				.getSelectedItem().toString();
		String baseTemplateName = ref_cmbBxBaseTemplate.getSelectionModel().getSelectedItem().toString();
		String reportGroupId = "";
		String reportGroupName = ref_cmbBxReportProfileGroup.getSelectionModel().getSelectedItem().toString();
		String reportProfileId = "";
		String reportProfileName = ref_cmbBxReportProfile.getSelectionModel().getSelectedItem().toString();
		// String filterName = ref_txtMeterMetaDataPageName.getText();
		boolean dutMainCtSelected = ref_rdBtnMainCt.isSelected();
		String baseTemplateTestFilterDataPopulateType = ref_cmbBxTestFilterDataPopulateType.getSelectionModel()
				.getSelectedItem().toString();
		boolean dutMetaDataApplyForAllPages = ref_chkBxDutMetaDataApplyForAllPages.isSelected();
		boolean dutMetaDataClubPageNoAndMaxNoOfPages = ref_chkBxDutMetaDataClubPageNoAndMaxNoOfPages.isSelected();
		boolean dutNeutralCtSelected = ref_rdBtnNeutralCt.isSelected();
		boolean importModeSelected = ref_rdBtnImportMode.isSelected();
		boolean exportModeSelected = ref_rdBtnExportMode.isSelected();
		Integer maxDutDisplayPerPage = Integer.parseInt(ref_txtMaxDutDisplayPerPage.getText());
		String outputFolderPath = ref_txtOutputPath.getText();
		boolean profileActive = true;
		boolean splitDutDisplayInToMultiplePage = ref_chkBxSplitDutDisplayInToMultiplePage.isSelected();
		String templateFileNameWithPath = ref_txtTemplateFileNameWithPath.getText();

		String parameterProfileName = ref_cmbBxParameterProfileName.getSelectionModel().getSelectedItem().toString();

		String printStyleResult = ref_cmbBxResultPrintStyleName.getSelectionModel().getSelectedItem().toString();
		String printStyleGenericHeader = ref_cmbBxGenericHeaderPrintStyleName.getSelectionModel().getSelectedItem()
				.toString();
		String printStyleTableHeader = ref_cmbBxTableHeaderPrintStyleName.getSelectionModel().getSelectedItem()
				.toString();
		// ApplicationLauncher.logger.debug("setupDataForReportProfileManage :
		// exportModeSelected: " + exportModeSelected);
		// ApplicationLauncher.logger.debug("setupDataForReportProfileManage :
		// importModeSelected: " + importModeSelected);

		ReportProfileManage reportProfileManageData = new ReportProfileManage();

		reportProfileManageData.setAppendDutSerialNoAndRackPosition(false);
		reportProfileManageData.setBaseTemplateMetaDataPopulateType(baseTemplateMetaDataPopulateType);
		reportProfileManageData.setBaseTemplateName(baseTemplateName);
		reportProfileManageData.setBaseTemplateTestFilterDataPopulateType(baseTemplateTestFilterDataPopulateType);
		reportProfileManageData.setDutMainCtSelected(dutMainCtSelected);
		reportProfileManageData.setDutMetaDataApplyForAllPages(dutMetaDataApplyForAllPages);
		reportProfileManageData.setDutMetaDataClubPageNoAndMaxNoOfPages(dutMetaDataClubPageNoAndMaxNoOfPages);
		reportProfileManageData.setDutNeutralCtSelected(dutNeutralCtSelected);
		reportProfileManageData.setExportModeSelected(exportModeSelected);
		reportProfileManageData.setImportModeSelected(importModeSelected);
		reportProfileManageData.setMaxDutDisplayPerPage(maxDutDisplayPerPage);
		reportProfileManageData.setOutputFolderPath(outputFolderPath);
		reportProfileManageData.setProfileActive(profileActive);
		reportProfileManageData.setReportGroupId(reportGroupId);
		reportProfileManageData.setReportGroupName(reportGroupName);
		reportProfileManageData.setReportProfileId(reportProfileId);
		reportProfileManageData.setReportProfileName(reportProfileName);
		reportProfileManageData.setSplitDutDisplayInToMultiplePage(splitDutDisplayInToMultiplePage);
		reportProfileManageData.setTemplateFileNameWithPath(templateFileNameWithPath);

		reportProfileManageData.setPrintStyleResult(printStyleResult);
		reportProfileManageData.setPrintStyleGenericHeader(printStyleGenericHeader);
		reportProfileManageData.setPrintStyleTableHeader(printStyleTableHeader);

		reportProfileManageData.setParameterProfileName(parameterProfileName);
		setReportProfileManageModel(reportProfileManageData);

	}

	private void displayOperationData() {

		ApplicationLauncher.logger.debug("displayOperationData : Entry");
		// OperationProcessJsonReadModel operationProcessDataModel =
		// DeviceDataManagerController.getReportProfileConfigParsedKey();

		/*
		 * for(OperationProcessDataJsonRead operationProcessData :
		 * operationProcessDataModel.getOperationMasterOutput()){
		 * ApplicationLauncher.logger.
		 * debug("displayOperationData : getOperationProcessKey: " +
		 * operationProcessData.getOperationProcessKey());
		 * ApplicationLauncher.logger.
		 * debug("displayOperationData : getPopulateOnlyHeaders: " +
		 * operationProcessData.getPopulateOnlyHeaders());
		 * ApplicationLauncher.logger.debug("displayOperationData : getUpperLimit: " +
		 * operationProcessData.getUpperLimit());
		 * ApplicationLauncher.logger.debug("displayOperationData : getLowerLimit: " +
		 * operationProcessData.getLowerLimit());
		 * ApplicationLauncher.logger.debug("displayOperationData : getResultValue: " +
		 * operationProcessData.getResultValue());
		 * ApplicationLauncher.logger.debug("displayOperationData : getComparedStatus: "
		 * + operationProcessData.getComparedStatus());
		 * ApplicationLauncher.logger.
		 * debug("displayOperationData : =========================== ");
		 * }
		 */

		for (OperationProcessDataJsonRead operationProcessData : operationProcessDataModel.getOperationMasterOutput()) {
			ApplicationLauncher.logger.debug("getOperationMasterOutput : getOperationProcessKey: "
					+ operationProcessData.getOperationProcessKey());
			ApplicationLauncher.logger.debug("getOperationMasterOutput : getPopulateOnlyHeaders: "
					+ operationProcessData.isPopulateOnlyHeaders());

			/*
			 * ApplicationLauncher.logger.debug("displayOperationData : getUpperLimit: " +
			 * operationProcessData.getUpperLimit());
			 * ApplicationLauncher.logger.debug("displayOperationData : getLowerLimit: " +
			 * operationProcessData.getLowerLimit());
			 * ApplicationLauncher.logger.debug("displayOperationData : getResultValue: " +
			 * operationProcessData.getResultValue());
			 * ApplicationLauncher.logger.debug("displayOperationData : getComparedStatus: "
			 * + operationProcessData.getComparedStatus());
			 */

			ApplicationLauncher.logger.debug("getOperationMasterOutput : =========================== ");
		}

		for (OperationProcessDataJsonRead operationProcessData : operationProcessDataModel.getOperationLocalOutput()) {
			ApplicationLauncher.logger.debug("getOperationLocalOutput : getOperationProcessKey: "
					+ operationProcessData.getOperationProcessKey());
			ApplicationLauncher.logger.debug("getOperationLocalOutput : getPopulateOnlyHeaders: "
					+ operationProcessData.isPopulateOnlyHeaders());

			/*
			 * ApplicationLauncher.logger.debug("displayOperationData : getUpperLimit: " +
			 * operationProcessData.getUpperLimit());
			 * ApplicationLauncher.logger.debug("displayOperationData : getLowerLimit: " +
			 * operationProcessData.getLowerLimit());
			 * ApplicationLauncher.logger.debug("displayOperationData : getResultValue: " +
			 * operationProcessData.getResultValue());
			 * ApplicationLauncher.logger.debug("displayOperationData : getComparedStatus: "
			 * + operationProcessData.getComparedStatus());
			 */

			ApplicationLauncher.logger.debug("getOperationLocalOutput : =========================== ");
		}

		for (OperationProcessDataJsonRead operationProcessData : operationProcessDataModel.getOperationLocalInput()) {
			ApplicationLauncher.logger.debug("getOperationLocalInput : getOperationProcessKey: "
					+ operationProcessData.getOperationProcessKey());
			ApplicationLauncher.logger.debug(
					"getOperationLocalInput : getPopulateOnlyHeaders: " + operationProcessData.isPopulateOnlyHeaders());

			/*
			 * ApplicationLauncher.logger.debug("displayOperationData : getUpperLimit: " +
			 * operationProcessData.getUpperLimit());
			 * ApplicationLauncher.logger.debug("displayOperationData : getLowerLimit: " +
			 * operationProcessData.getLowerLimit());
			 * ApplicationLauncher.logger.debug("displayOperationData : getResultValue: " +
			 * operationProcessData.getResultValue());
			 * ApplicationLauncher.logger.debug("displayOperationData : getComparedStatus: "
			 * + operationProcessData.getComparedStatus());
			 */

			ApplicationLauncher.logger.debug("getOperationLocalInput : =========================== ");
		}

	}

	private void hideGuiObjects() {

		ref_accordTestFilter.getPanes().remove(ref_titledPaneMeasuredData);
		ref_colSelectedMeterPopulateData.setVisible(false);
		ref_colSelectedMeterPopulateForEachDut.setVisible(false);
		ref_chkBxAppendMeterSerialNoAndRackPositionListDisplay.setVisible(false);
		ref_chkBxDutMetaDataApplyForAllPages.setVisible(false);
		ref_chkBxDutMetaDataClubPageNoAndMaxNoOfPages.setVisible(false);
		ref_chkBxMeterProfileMetaDataListPageActive.setVisible(false);
		ref_cmBxPageNumber.setVisible(false);
		ref_titledPaneExecutionMode.setVisible(false);

		ref_lblPageName.setVisible(false);
		ref_lblPageNumber.setVisible(false);
		ref_cmbBxMetaDataPageName.setVisible(false);
		ref_cmbBxExecutionResultType.setVisible(false);
		ref_cmbBxExecutionResultType.setVisible(false);
		ref_chkBxPopulateDutOnePulsePeriod.setVisible(false);
		ref_chkBxHeader3IterationIdFilter.setVisible(false);
		// ref_chkBx_IterationIdHeaderPrefix.setVisible(false);
		ref_colTestFilterListIterationId.setVisible(false);
		ref_colTestFilterListReplicateData.setVisible(false);
		ref_txtMeterMetaDataPageName.setVisible(false);
		ref_lbl_MeterProfileMetaDataPageName.setVisible(false);
		if (REPLICATE_FEATURE_DISABLED) {
			ref_lblReplicateCount.setVisible(false);
			ref_cmbBxReplicateCountUserEntry.setVisible(false);
			ref_chkBxReplicateData.setVisible(false);
			ref_colTestFilterListReplicateDataCellPosition.setVisible(false);

			// ref_chkBxPopulateRsmInitial.setVisible(false);
			// ref_chkBxPopulateRsmFinal.setVisible(false);
			// ref_chkBxPopulateDutInitial.setVisible(false);
			// ref_chkBxPopulateDutFinal.setVisible(false);
			// ref_chkBxPopulateDutDifference.setVisible(false);
			// ref_chkBxPopulateDutPulseCount.setVisible(false);
			// ref_chkBxPopulateDutCalcErrorPercentage.setVisible(false);

			// ref_chkBxPopulateDutAverage.setVisible(false);
			// ref_chkBxPopulateHeader4.setVisible(false);
			// ref_chkBxPopulateHeader5.setVisible(false);

		}

	}

	private void dataInit() {

		ApplicationLauncher.logger.debug("dataInit : Entry");

		OperationProcessJsonReadModel operationProcessJsonReadModel = DeviceDataManagerController
				.getReportProfileConfigParsedKey();
		/*
		 * resultDataTypeHashMap.put(ConstantReportV2.
		 * RESULT_DATA_TYPE_DISPLAY_ERROR_VALUE,
		 * ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
		 * resultDataTypeHashMap.put(ConstantReportV2.
		 * RESULT_DATA_TYPE_DISPLAY_DUT_PULSE_COUNT,
		 * ConstantReport.RESULT_DATA_TYPE_PULSE_COUNT);
		 * resultDataTypeHashMap.put(ConstantReportV2.
		 * RESULT_DATA_TYPE_DISPLAY_DUT_INITIAL_REGISTER,
		 * ConstantReport.RESULT_DATA_TYPE_INITIAL_KWH);
		 * resultDataTypeHashMap.put(ConstantReportV2.
		 * RESULT_DATA_TYPE_DISPLAY_DUT_FINAL_REGISTER,
		 * ConstantReport.RESULT_DATA_TYPE_FINAL_KWH);
		 * resultDataTypeHashMap.put(ConstantReportV2.
		 * RESULT_DATA_TYPE_DISPLAY_REFSTD_INITIAL_REGISTER,
		 * ConstantReport.RESULT_DATA_TYPE_REF_INITIAL_KWH);
		 * resultDataTypeHashMap.put(ConstantReportV2.
		 * RESULT_DATA_TYPE_DISPLAY_REFSTD_FINAL_REGISTER,
		 * ConstantReport.RESULT_DATA_TYPE_REF_FINAL_KWH);
		 * 
		 */
		// resultDataTypeHashMap.put(RESULT_DATA_TYPE_DISPLAY_OPERATION,
		// ConstantReport.RESULT_DATA_TYPE_OPERATION_OUTPUT);

		/*
		 * for(int i= 1; i <= OPERATION_INPUT_DATA_MAX_COUNT ; i++){
		 * operationInputDataHashBiMap.put(OPERATION_INPUT_DATA_HEADER_KEY_PREFIX +
		 * String.format("%02d", i), OPERATION_INPUT_DATA_HEADER_DISPLAY_PREFIX +
		 * String.format("%02d",i));
		 * 
		 * }
		 */

		/*
		 * for(int i= 1; i <= OPERATION_LOCAL_OUTPUT_DATA_MAX_COUNT ; i++){
		 * operationLocalOutputDataHashBiMap.put(
		 * OPERATION_LOCAL_OUTPUT_DATA_HEADER_KEY_PREFIX + String.format("%02d",i),
		 * OPERATION_LOCAL_OUTPUT_DATA_HEADER_DISPLAY_PREFIX + String.format("%02d",i));
		 * 
		 * }
		 */

		for (int i = 1; i <= operationProcessJsonReadModel.getOperationLocalInput().size(); i++) {
			operationInputDataHashBiMap.put(OPERATION_INPUT_DATA_HEADER_KEY_PREFIX + String.format("%02d", i),
					operationProcessJsonReadModel.getOperationLocalInput().get(i - 1).getOperationProcessKey());
		}

		for (int i = 1; i <= operationProcessJsonReadModel.getOperationLocalOutput().size(); i++) {
			operationLocalOutputDataHashBiMap.put(
					OPERATION_LOCAL_OUTPUT_DATA_HEADER_KEY_PREFIX + String.format("%02d", i),
					operationProcessJsonReadModel.getOperationLocalOutput().get(i - 1).getOperationProcessKey());

		}

		// for(int i= 1; i <= OPERATION_LOCAL_OUTPUT_STATUS_MAX_COUNT ; i++){
		// operationLocalOutputStatusHashBiMap.put(OPERATION_LOCAL_OUTPUT_STATUS_HEADER_KEY_PREFIX
		// + String.format("%02d",i),
		// OPERATION_LOCAL_OUTPUT_STATUS_HEADER_DISPLAY_PREFIX +
		// String.format("%02d",i));

		// }

		// cmbBxOperationComparedResultStatusOutput

		/*
		 * for(int i= 1; i <= OPERATION_MASTER_OUTPUT_STATUS_MAX_COUNT ; i++){
		 * operationMasterOutputDataHashBiMap.put(
		 * OPERATION_MASTER_OUTPUT_DATA_HEADER_KEY_PREFIX + String.format("%02d",i),
		 * OPERATION_MASTER_OUTPUT_DATA_HEADER_DISPLAY_PREFIX +
		 * String.format("%02d",i));
		 * 
		 * }
		 */

		for (int i = 1; i <= operationProcessJsonReadModel.getOperationMasterOutput().size(); i++) {
			operationMasterOutputDataHashBiMap.put(
					OPERATION_MASTER_OUTPUT_DATA_HEADER_KEY_PREFIX + String.format("%02d", i),
					operationProcessJsonReadModel.getOperationMasterOutput().get(i - 1).getOperationProcessKey());

		}

		for (int i = 1; i <= OPERATION_MASTER_OUTPUT_STATUS_MAX_COUNT; i++) {
			operationMasterOutputStatusHashBiMap.put(
					OPERATION_MASTER_OUTPUT_STATUS_HEADER_KEY_PREFIX + String.format("%02d", i),
					OPERATION_MASTER_OUTPUT_STATUS_HEADER_DISPLAY_PREFIX + String.format("%02d", i));

		}

		ApplicationLauncher.logger
				.debug("dataInit: OPERATION_OUTPUT_HEADER_KEY_PREFIX2: " + operationLocalOutputDataHashBiMap
						.get(OPERATION_LOCAL_OUTPUT_DATA_HEADER_KEY_PREFIX + String.valueOf(2)));
		ConstantReport.REPORT_TEST_TYPES.add(ConstantReport.REPORT_TEST_TYPES_NONE);
		// Map<String,String> value = (String)
		// operationOutputHashBiMap.forEach((k,v)->(v.equals(OPERATION_OUTPUT_HEADER_DISPLAY_PREFIX
		// + String.valueOf(3))){return v}});
		// Map<String,String> filteredMap =
		// operationOutputHashBiMap.entrySet().stream().filter(k->k.getValue().equals(OPERATION_OUTPUT_HEADER_DISPLAY_PREFIX
		// + String.valueOf(3))).collect(Collectors.toMap(Map.Entry::getKey,
		// Map.Entry::getValue));
		// ApplicationLauncher.logger.debug("dataInit:
		// OPERATION_OUTPUT_HEADER_DISPLAY_PREFIX3: values: " + filteredMap.values());
		// ApplicationLauncher.logger.debug("dataInit:
		// OPERATION_OUTPUT_HEADER_DISPLAY_PREFIX3: keySet: " + filteredMap.keySet());

	}

	private void guiInit() {

		loadTestTypes();
		loadPopulateDataTypes();
		loadExtensions();

		guiPropertySettings();
		guiCommonDefaultSettings();
		guiTestFilterDefaultSettings();
		guiMeterMetaDataDefaultSettings();
	}

	private void guiCommonDefaultSettings() {
		ApplicationLauncher.logger.debug("guiCommonDefaultSettings: Entry");

		ref_btnSaveGroupProfile.setDisable(true);
		ref_rdBtnMainCt.setSelected(true);
		ref_rdBtnNeutralCt.setSelected(false);
		ref_rdBtnImportMode.setSelected(true);
		ref_rdBtnExportMode.setSelected(false);

		ref_cmbBxBaseTemplate.getItems().addAll(BASE_TEMPLATE_LIST);
		ref_cmbBxBaseTemplate.getSelectionModel().select(0);

		for (int i = 1; i <= REPORT_MAX_PAGE_SUPPORTED; i++) {
			ref_cmBxPageNumber.getItems().add(i);

		}

		ref_cmBxPageNumber.getItems().add(ALL_PAGES);
		ref_cmBxPageNumber.getSelectionModel().select(0);

		ref_titledPaneMeterProfileMetaData.setDisable(true);
		ref_titledPaneTestFilterData.setDisable(true);

		// ref_cmbBxReportProfileGroup.getItems().add(SELECT_DISPLAY);
		// ref_cmbBxReportProfileGroup.getItems().add(SAMPLE_GROUP);
		// ref_cmbBxReportProfileGroup.getSelectionModel().select(0);

		ref_chkBxMeterProfileMetaDataListPageActive.setSelected(false);

		ref_cmbBxResultPrintStyleName.getSelectionModel().selectFirst();
		ref_cmbBxGenericHeaderPrintStyleName.getSelectionModel().selectFirst();
		ref_cmbBxTableHeaderPrintStyleName.getSelectionModel().selectFirst();

	}

	private void guiMeterMetaDataDefaultSettings() {
		ApplicationLauncher.logger.debug("guiMeterMetaDataDefaultSettings: Entry");

		ref_cmbBxMeterMetaDataPopulateType.getItems().clear();
		ref_tvSelectedMeterDataType.getItems().clear();
		ref_tvSelectedTestFilterMeasuredData.getItems().clear();
		clearLastSavedMeterMetaDataCellPosition();

		ref_cmbBxMeterMetaDataPopulateType.getItems().addAll(BASE_TEMPLATE_POPULATE_TYPE_LIST);
		ref_cmbBxMeterMetaDataPopulateType.getSelectionModel().select(0);

		for (int i = 0; i < MEASURED_DATA_HEADER_KEY_LIST.size(); i++) {
			measuredData.add(new ReportMeterMetaDataTypeSubModel(String.valueOf((i + 1)),
					MEASURED_DATA_HEADER_KEY_LIST.get(i), false, false, false, "", "", ""));// ,false));
		}

		// measuredData.add(new ReportFilterDataTypeModel("1","Target Voltage",false,
		// false));

		for (int i = 0; i < REPORT_METER_DATA_TYPE_KEY_LIST.size(); i++) {
			selectedMeterData.add(new ReportMeterMetaDataTypeSubModel(String.valueOf(i + 1),
					REPORT_METER_DATA_TYPE_KEY_LIST.get(i), false, false, false, "", "", ""));// ,false));
		}

		ref_tvSelectedMeterDataType.setItems(selectedMeterData);

		ref_tvSelectedTestFilterMeasuredData.setItems(measuredData);

		if (getMetaDataPageNameList().size() == 0) {
			ref_cmbBxMetaDataPageName.getItems().clear();
			ref_cmbBxMetaDataPageName.getItems().add(SELECT_DISPLAY);
			ref_cmbBxMetaDataPageName.getSelectionModel().select(0);
		}

		ref_chkBxMeterProfileMetaDataPageActive.setSelected(true);

		ref_tvMeterMetaDataCellStartPosition.getItems().clear();
		ref_tvMeterMetaDataCellHeaderPosition.getItems().clear();
	}

	private void guiTestFilterDefaultSettings() {
		ApplicationLauncher.logger.debug("guiTestFilterDefaultSettings: Entry");

		// ref_rdBtnPopulateVertical.setSelected(true);
		// ref_rdBtnPopulateHorizontal.setSelected(false);

		ref_txtPostOperationValue.setDisable(true);
		ref_cmbBxPostOperationMethod.setDisable(true);
		ref_txtPostOperationValue.setText("");
		ref_chkBxPostOperationActive.setSelected(false);

		ref_lblCustomTestName.setVisible(false);
		ref_txtCustomTestNameUserEntry.setVisible(false);
		ref_txtCustomTestNameData.setVisible(false);
		ref_rdBtnOperationNone.setSelected(true);
		ref_rdBtnOperationInput.setSelected(false);
		ref_rdBtnOperationOutput.setSelected(false);
		ref_titledPaneOperation.setDisable(true);
		ref_txtTestFilterPageNumber.setText("1");
		ref_txtMeterMetaDataPageNumber.setText("1");
		ref_chkBxHeader3VoltageFilter.setDisable(true);
		ref_chkBxHeader3CurrentFilter.setDisable(true);
		ref_chkBxHeader3PfFilter.setDisable(true);
		ref_chkBxHeader3FreqFilter.setDisable(true);
		ref_chkBxHeader3EnergyFilter.setDisable(true);
		// ref_chkBxHeader3IterationIdFilter.setDisable(true);
		ref_chkBx_IterationIdHeaderPrefix.setDisable(true);
		ref_chkBxHeader3CustomAllowed.setDisable(true);
		ref_cmbBxReplicateCountUserEntry.setDisable(true);

		ref_chkBxPopulateRsmInitial.setSelected(false);
		ref_chkBxPopulateRsmFinal.setSelected(false);
		ref_chkBxPopulateRsmDifference.setSelected(false);
		ref_chkBxPopulateDutInitial.setSelected(false);
		ref_chkBxPopulateDutFinal.setSelected(false);
		ref_chkBxPopulateDutDifference.setSelected(false);
		ref_chkBxPopulateDutPulseCount.setSelected(false);
		ref_chkBxPopulateDutCalcErrorPercentage.setSelected(false);
		ref_chkBxPopulateDutOnePulsePeriod.setSelected(false);
		ref_chkBxPopulateDutAverageValue.setSelected(false);
		ref_chkBxPopulateDutAverageStatus.setSelected(false);
		ref_chkBxPopulateHeader4.setSelected(false);
		ref_chkBxPopulateHeader5.setSelected(false);
		// ref_titledPaneDutResultPopulateType.setExpanded(true);

		ref_chkBxPopulateHeader1.setSelected(false);
		ref_chkBxPopulateHeader2.setSelected(false);
		ref_chkBxPopulateHeader3.setSelected(false);
		ref_chkBxPopulateHeader4.setSelected(false);
		ref_chkBxPopulateHeader5.setSelected(false);

		ref_chkBxPopulateRsmInitial.setSelected(false);
		ref_chkBxPopulateRsmFinal.setSelected(false);
		ref_chkBxPopulateRsmDifference.setSelected(false);
		ref_chkBxPopulateHeaderTestPeriod.setSelected(false);
		ref_chkBxPopulateHeaderWarmupPeriod.setSelected(false);
		ref_chkBxPopulateHeaderActualVoltage.setSelected(false);
		ref_chkBxPopulateHeaderActualCurrent.setSelected(false);
		ref_chkBxPopulateHeaderActualPf.setSelected(false);
		ref_chkBxPopulateHeaderActualFreq.setSelected(false);
		ref_chkBxPopulateHeaderActualEnergy.setSelected(false);
		ref_chkBxPopulateDutInitial.setSelected(false);
		ref_chkBxPopulateDutFinal.setSelected(false);
		ref_chkBxPopulateDutDifference.setSelected(false);
		ref_chkBxPopulateDutPulseCount.setSelected(false);
		ref_chkBxPopulateDutCalcErrorPercentage.setSelected(false);
		ref_chkBxPopulateDutOnePulsePeriod.setSelected(false);
		ref_chkBxPopulateDutAverageValue.setSelected(false);
		ref_chkBxPopulateDutAverageStatus.setSelected(false);

		ref_cmbBxOperationCriteriaLocalOutputData.setDisable(true);
		ref_chkBxPopulateLocalOutputData.setDisable(true);
		ref_chkBxMergeUpperLowerLimit.setDisable(true);

		ref_txtAllowedUpperLimit.setText("");
		ref_txtAllowedLowerLimit.setText("");

		disableCurrentFilterGui();
		disablePfFilterGui();
		disableFreqFilterGui();
		disableEnergyFilterGui();
		disableIterationFilterGui();
		ref_cmbBxReplicateCountUserEntry.getItems().clear();
		ref_cmbBxOperationInputUserEntry.getItems().clear();
		ref_cmbBxOperationCriteriaInputData.getItems().clear();
		ref_cmbBxOperationCriteriaLocalOutputData.getItems().clear();
		ref_cmbBxOperationComparedLocalResultStatusOutput.getItems().clear();
		ref_cmbBxOperationComparedMasterResultStatusOutput.getItems().clear();
		ref_cmbBxOperationCriteriaMasterOutputData.getItems().clear();
		ref_cmbBxOperationCriteriaProcessData.getItems().clear();
		ref_cmbBxTestFilterDataPopulateType.getItems().clear();
		ref_cmbBxPostOperationMethod.getItems().clear();
		ref_tvTestFilterCellStartPosition.getItems().clear();
		ref_tvTestFilterCellHeaderPosition.getItems().clear();
		clearLastSavedTestFilterDataCellPositionHashMap();

		for (int i = 1; i <= REPLICATE_DATA_COUNT; i++) {
			ref_cmbBxReplicateCountUserEntry.getItems().add(String.valueOf(i));
		}

		ref_cmbBxReplicateCountUserEntry.getSelectionModel().select(0);

		ArrayList<String> replicateHeader = new ArrayList<String>();
		ArrayList<String> replicateCell = new ArrayList<String>();

		// replicateHeader.add(CELL_START_POSITION_HEADER_RESULT_DATA_KEY);
		// replicateCell.add("");
		// loadTable(ref_tvCellStartPosition, replicateHeader, replicateCell,
		// cellStartPositionPageCellValues);

		/*
		 * operationInputHashBiMap.entrySet().forEach(k->
		 * ref_cmbBxOperationInputUserEntry.getItems().add(k.getValue()));
		 * ref_cmbBxOperationInputUserEntry.getSelectionModel().select(0);
		 * 
		 * operationOutputHashBiMap.entrySet().forEach(k->
		 * ref_cmbBxOperationOutputUserEntry.getItems().add(k.getValue()));
		 * ref_cmbBxOperationOutputUserEntry.getSelectionModel().select(0);
		 */

		ArrayList<String> operationUserEntryLocalInput = (ArrayList<String>) getOperationParameterProfileDataList()
				.stream()
				.filter(e -> e.getParamType().equals(ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_INPUT))
				// .filter( e-> e.isPopulateOnlyHeaders() == true)
				.map(e -> e.getKeyParam())
				.collect(Collectors.toList());
		// operationInputDataHashBiMap.entrySet().forEach(k->operationUserEntry.add(k.getValue()));
		Collections.sort(operationUserEntryLocalInput);
		ref_cmbBxOperationInputUserEntry.getItems().addAll(operationUserEntryLocalInput);
		ref_cmbBxOperationInputUserEntry.getSelectionModel().select(0);
		ref_cmbBxOperationCriteriaInputData.getItems().addAll(operationUserEntryLocalInput);
		ref_cmbBxOperationCriteriaInputData.getSelectionModel().select(0);

		/// operationUserEntry.clear();
		// operationLocalOutputDataHashBiMap.entrySet().forEach(k->operationUserEntry.add(k.getValue()));
		ArrayList<String> operationUserEntryLocalOutput = (ArrayList<String>) getOperationParameterProfileDataList()
				.stream()
				.filter(e -> e.getParamType().equals(ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_OUTPUT))
				// .filter( e-> e.isPopulateOnlyHeaders() == true)
				.map(e -> e.getKeyParam())
				.collect(Collectors.toList());

		Collections.sort(operationUserEntryLocalOutput);
		// ref_cmbBxOperationOutputUserEntry.getItems().addAll(operationUserEntry);
		// ref_cmbBxOperationOutputUserEntry.getSelectionModel().select(0);
		ref_cmbBxOperationCriteriaLocalOutputData.getItems().addAll(operationUserEntryLocalOutput);
		ref_cmbBxOperationCriteriaLocalOutputData.getSelectionModel().select(0);

		ref_cmbBxOperationCriteriaInputData.getItems().addAll(operationUserEntryLocalOutput);

		ArrayList<String> operationUserEntry = new ArrayList<String>();

		operationUserEntry.clear();
		// operationLocalOutputStatusHashBiMap.entrySet().forEach(k->operationUserEntry.add(k.getValue()));
		// Collections.sort(operationUserEntry);
		// ref_cmbBxOperationComparedResultStatusOutput.getItems().addAll(operationUserEntry);
		ref_cmbBxOperationComparedLocalResultStatusOutput.getItems()
				.add(ConstantReportV2.POPULATE_LOCAL_OUTPUT_STATUS_KEY);
		ref_cmbBxOperationComparedLocalResultStatusOutput.getSelectionModel().select(0);

		ref_cmbBxOperationComparedMasterResultStatusOutput.getItems()
				.add(ConstantReportV2.POPULATE_MASTER_OUTPUT_STATUS_KEY);
		ref_cmbBxOperationComparedMasterResultStatusOutput.getSelectionModel().select(0);

		// operationUserEntry.clear();

		// operationMasterOutputDataHashBiMap.entrySet().forEach(k->operationUserEntry.add(k.getValue()));

		ArrayList<String> operationUserEntryMasterOutput = (ArrayList<String>) getOperationParameterProfileDataList()
				.stream()
				.filter(e -> e.getParamType().equals(ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_MASTER_OUTPUT))
				// .filter( e-> e.isPopulateOnlyHeaders() == true)
				.map(e -> e.getKeyParam())
				.collect(Collectors.toList());
		Collections.sort(operationUserEntryMasterOutput);
		ref_cmbBxOperationCriteriaMasterOutputData.getItems().add(ConstantReportV2.NONE_DISPLAYED);
		ref_cmbBxOperationCriteriaMasterOutputData.getItems().addAll(operationUserEntryMasterOutput);
		ref_cmbBxOperationCriteriaMasterOutputData.getSelectionModel().select(0);

		ref_cmbBxOperationCriteriaInputData.getItems().addAll(operationUserEntryMasterOutput);

		operationUserEntry.clear();
		// operationUserEntry.add(NONE_DISPLAYED);
		operationUserEntry.addAll(ConstantReportV2.OPERATION_METHOD_LIST);

		ref_cmbBxOperationCriteriaProcessData.getItems().addAll(operationUserEntry);
		ref_cmbBxOperationCriteriaProcessData.getSelectionModel().select(0);

		ref_cmbBxTestFilterDataPopulateType.getItems().addAll(BASE_TEMPLATE_POPULATE_TYPE_LIST);
		ref_cmbBxTestFilterDataPopulateType.getSelectionModel().select(0);

		ref_cmbBxPostOperationMethod.getItems().addAll(ConstantReportV2.POST_OPERATION_METHOD_LIST);
		ref_cmbBxPostOperationMethod.getSelectionModel().select(0);
		/*
		 * ref_cmbBxBaseTemplate.getItems().addAll(BASE_TEMPLATE_LIST);
		 * ref_cmbBxBaseTemplate.getSelectionModel().select(0);
		 * 
		 * ref_cmbBxMeterMetaDataPopulateType.getItems().addAll(
		 * BASE_TEMPLATE_POPULATE_TYPE_LIST);
		 * ref_cmbBxMeterMetaDataPopulateType.getSelectionModel().select(0);
		 * 
		 * 
		 * ref_cmbBxTestFilterDataPopulateType.getItems().addAll(
		 * BASE_TEMPLATE_POPULATE_TYPE_LIST);
		 * ref_cmbBxTestFilterDataPopulateType.getSelectionModel().select(0);
		 */

		/*
		 * for(int i= 1; i <= REPORT_MAX_PAGE_SUPPORTED ; i++){
		 * ref_cmBxPageNumber.getItems().add(i);
		 * 
		 * }
		 * 
		 * ref_cmBxPageNumber.getItems().add(ALL_PAGES);
		 * ref_cmBxPageNumber.getSelectionModel().select(0);
		 */
		// ReportFilterDataTypeModel measuredDataList = new
		// ReportFilterDataTypeModel("1","Target Voltage",false, false);

		/*
		 * for(int i = 0; i < MEASURED_DATA_HEADER_KEY_LIST.size();i ++){
		 * measuredData.add(new ReportMeterMetaDataTypeSubModel(String.valueOf((i+1)),
		 * MEASURED_DATA_HEADER_KEY_LIST.get(i),false,false, false,"",""));
		 * }
		 * 
		 * //measuredData.add(new ReportFilterDataTypeModel("1","Target Voltage",false,
		 * false));
		 * 
		 * 
		 * for(int i = 0; i < REPORT_METER_DATA_TYPE_KEY_LIST.size();i ++){
		 * selectedMeterData.add(new
		 * ReportMeterMetaDataTypeSubModel(String.valueOf(i+1),
		 * REPORT_METER_DATA_TYPE_KEY_LIST.get(i),false,false, false,"",""));
		 * }
		 * 
		 * 
		 * ref_tvSelectedMeterDataType.setItems(selectedMeterData);
		 * 
		 * ref_tvSelectedTestFilterMeasuredData.setItems(measuredData);
		 */

		//

		// listViewInputProcessList.getItems().add("Test1");
		// listViewInputProcessList.getItems().add("Test2");
		// loadTable(ref_tvCellHeaderPosition, getCellStartPositionHeader(),
		// getCellStartPositionCell(),
		// cellStartPositionPageCellValues);

	}

	private void guiPropertySettings() {

		ApplicationLauncher.logger.debug("guiPropertySettings: Entry");

		ref_txtTemplateFileNameWithPath.setEditable(false);
		ref_txtOutputPath.setEditable(false);

		ref_lblMaxDutDisplayPerPage.setText("Max " + DUT_KEY + " display per page");// Max Dut Display Per Page
		ref_chkBxSplitDutDisplayInToMultiplePage.setText("Split " + DUT_KEY + " display in to multiple page");// Split
																												// Dut
																												// Display
																												// In to
																												// Multiple
																												// Page

		// ref_txtTemplateFileNameWithPath.setDisable(true);
		// ref_txtOutputPath.setDisable(true);

		ref_colMeterMetaDataListPopulateForEachDut.setText(
				ref_colMeterMetaDataListPopulateForEachDut.getText().replace("DUT", ConstantAppConfig.DUT_DISPLAY_KEY));
		ref_colSelectedMeterPopulateForEachDut.setText(
				ref_colSelectedMeterPopulateForEachDut.getText().replace("DUT", ConstantAppConfig.DUT_DISPLAY_KEY));

		ref_chkBxPopulateDutInitial
				.setText(ref_chkBxPopulateDutInitial.getText().replace("DUT", ConstantAppConfig.DUT_DISPLAY_KEY));
		ref_chkBxPopulateDutFinal
				.setText(ref_chkBxPopulateDutFinal.getText().replace("DUT", ConstantAppConfig.DUT_DISPLAY_KEY));
		ref_chkBxPopulateDutDifference
				.setText(ref_chkBxPopulateDutDifference.getText().replace("DUT", ConstantAppConfig.DUT_DISPLAY_KEY));
		ref_chkBxPopulateDutPulseCount
				.setText(ref_chkBxPopulateDutPulseCount.getText().replace("DUT", ConstantAppConfig.DUT_DISPLAY_KEY));
		ref_chkBxPopulateDutCalcErrorPercentage.setText(
				ref_chkBxPopulateDutCalcErrorPercentage.getText().replace("DUT", ConstantAppConfig.DUT_DISPLAY_KEY));
		ref_chkBxPopulateDutOnePulsePeriod.setText(
				ref_chkBxPopulateDutOnePulsePeriod.getText().replace("DUT", ConstantAppConfig.DUT_DISPLAY_KEY));
		ref_chkBxPopulateDutAverageValue
				.setText(ref_chkBxPopulateDutAverageValue.getText().replace("DUT", ConstantAppConfig.DUT_DISPLAY_KEY));
		ref_chkBxPopulateDutAverageStatus
				.setText(ref_chkBxPopulateDutAverageStatus.getText().replace("DUT", ConstantAppConfig.DUT_DISPLAY_KEY));
		// ref_titledPaneDutResultPopulateType.setText(ref_titledPaneDutResultPopulateType.getText().replace("DUT",ConstantAppConfig.DUT_DISPLAY_KEY));
		ref_chkBxPopulateDutErrorResultStatus.setText(
				ref_chkBxPopulateDutErrorResultStatus.getText().replace("DUT", ConstantAppConfig.DUT_DISPLAY_KEY));

		/*
		 * ref_txtCustomTestNameUserEntry.textProperty().addListener(new
		 * ChangeListener<String>() {
		 * 
		 * @Override
		 * public void changed(ObservableValue<? extends String> observable, String
		 * oldValue,
		 * String newValue) {
		 * if (!newValue.matches("\\w*")) {
		 * 
		 * ref_txtCustomTestNameUserEntry.setText(newValue.replaceAll("[^\\w]", ""));
		 * }
		 * }
		 * });
		 */

		ref_txtIterationReadingStartingId.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue,
					String newValue) {
				if (!newValue.matches("\\d*")) {

					ref_txtIterationReadingStartingId.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		ref_txtIterationReadingEndingId.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue,
					String newValue) {
				if (!newValue.matches("\\d*")) {

					ref_txtIterationReadingEndingId.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		ref_txtTestFilterPageNumber.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue,
					String newValue) {
				if (!newValue.matches("\\d*")) {

					ref_txtTestFilterPageNumber.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		ref_txtMeterMetaDataPageNumber.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue,
					String newValue) {
				if (!newValue.matches("\\d*")) {

					ref_txtMeterMetaDataPageNumber.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		ref_txtCurrentPercentFilterUserEntry.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue,
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {

					// ref_txtCurrentPercentFilterUserEntry.setText(newValue.replaceAll("[^\\d||.]",
					// ""));
					ref_txtCurrentPercentFilterUserEntry.setText(oldValue);
				}
			}
		});

		ref_txtPfFilterUserEntry.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue,
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					ref_txtPfFilterUserEntry.setText(oldValue);
				}
			}
		});

		/*
		 * ref_cmbBxPfFilterUserEntry.textProperty().addListener(new
		 * ChangeListener<String>() {
		 * 
		 * @Override
		 * public void changed(ObservableValue<? extends String> observable, String
		 * oldValue,
		 * String newValue) {
		 * if (!newValue.matches("\\d*(\\.\\d*)?")) {
		 * ref_txtPfFilterUserEntry.setText(oldValue);
		 * }
		 * }
		 * });
		 */

		ref_txtFreqFilterUserEntry.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue,
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					ref_txtFreqFilterUserEntry.setText(oldValue);
				}
			}
		});

		ref_txtEnergyFilterUserEntry.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue,
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					ref_txtEnergyFilterUserEntry.setText(oldValue);
				}
			}
		});

		ref_txtFilterName.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue,
					String newValue) {
				if (!newValue.matches("([a-zA-Z0-9_])*")) {
					ref_txtFilterName.setText(newValue.replaceAll("[^a-zA-Z0-9_]", ""));
				}
			}
		});

		/*
		 * ref_txtErrorValueCellStartPosition.textProperty().addListener(new
		 * ChangeListener<String>() {
		 * 
		 * @Override
		 * public void changed(ObservableValue<? extends String> observable, String
		 * oldValue,
		 * String newValue) {
		 * if (!newValue.matches("\\d\\sa-zA-Z*")) {
		 * ref_txtErrorValueCellStartPosition.setText(newValue.replaceAll(
		 * "[^\\d\\sa-zA-Z]", ""));
		 * }
		 * }
		 * });
		 * 
		 * ref_txtErrorStatusCellStartPosition.textProperty().addListener(new
		 * ChangeListener<String>() {
		 * 
		 * @Override
		 * public void changed(ObservableValue<? extends String> observable, String
		 * oldValue,
		 * String newValue) {
		 * if (!newValue.matches("\\d\\sa-zA-Z*")) {
		 * ref_txtErrorStatusCellStartPosition.setText(newValue.replaceAll(
		 * "[^\\d\\sa-zA-Z]", ""));
		 * }
		 * }
		 * });
		 */

		/*
		 * ref_txtVoltPercentFilterUserEntry.textProperty().addListener(new
		 * ChangeListener<String>() {
		 * 
		 * @Override
		 * public void changed(ObservableValue<? extends String> observable, String
		 * oldValue,
		 * String newValue) {
		 * if (!newValue.matches("\\d\\sa-zA-Z||:*")) {
		 * ref_txtVoltPercentFilterUserEntry.setText(newValue.replaceAll(
		 * "[^\\d\\sa-zA-Z||:]", ""));
		 * }
		 * }
		 * });
		 */

		ref_txtVoltPercentFilterUserEntry.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue,
					String newValue) {
				if (!newValue.matches("\\d*")) {

					ref_txtVoltPercentFilterUserEntry.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		ref_txtVoltPercentFilterUserEntry.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				if (newPropertyValue) {
					// System.out.println("Textfield on focus");
				} else {
					// System.out.println("Textfield out focus");
					String selectedUnit = ref_cmbBxVoltPercentFilterUserEntry.getSelectionModel().getSelectedItem()
							.toString();

					if (selectedUnit.equals(ConstantReport.EXTENSION_TYPE_VOLTAGE_U)) {
						ref_txtVoltPercentFilterData.setText(ref_txtVoltPercentFilterUserEntry.getText() +
								selectedUnit);
					} else {
						/*
						 * ref_txtVoltPercentFilterData.setText( selectedUnit +
						 * ref_txtVoltPercentFilterUserEntry.getText()+
						 * ConstantReport.EXTENSION_TYPE_VOLTAGE_U);
						 */
						ref_txtVoltPercentFilterData.setText(selectedUnit.replace(VOLTAGE_PERCENT_PLACE_HOLDER,
								ref_txtVoltPercentFilterUserEntry.getText()));
					}
					if (ref_chkBxHeader3VoltageFilter.isSelected()) {
						chkBxHeader3VoltageFilterOnChange();
					}
				}
			}
		});

		ref_txtCurrentPercentFilterUserEntry.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				if (newPropertyValue) {
					// System.out.println("Textfield on focus");
				} else {
					// System.out.println("Textfield out focus");
					ref_txtCurrentPercentFilterData.setText(ref_txtCurrentPercentFilterUserEntry.getText() +
							ref_cmbBxCurrentPercentFilterUserEntry.getSelectionModel().getSelectedItem().toString());
					if (ref_chkBxHeader3CurrentFilter.isSelected()) {
						chkBxHeader3CurrentFilterOnChange();
					}
				}
			}
		});

		// Pattern negativeDecimalPattern =
		// Pattern.compile("(-|\\+)?\\d+((\\.{1}\\d+)?)");

		/*
		 * ref_txtAllowedUpperLimit.textProperty().addListener(new
		 * ChangeListener<String>() {
		 * 
		 * @Override
		 * public void changed(ObservableValue<? extends String> observable, String
		 * oldValue,
		 * String newValue) {
		 * if (!newValue.matches("(-|\\+)?\\d+((\\.{1}\\d+)?)")) {
		 * ref_txtAllowedUpperLimit.setText(oldValue);
		 * }
		 * }
		 * });
		 */

		// Pattern validDoubleText = Pattern.compile("-?((\\d*)|(\\d+\\.\\d*))");
		Pattern validDoubleText = Pattern.compile("[-+]?((\\d*)|(\\d+\\.\\d*))");

		TextFormatter<Double> textFormatterUpperLimit = new TextFormatter<Double>(new DoubleStringConverter(), -0.25,
				change -> {
					String newText = change.getControlNewText();
					if (validDoubleText.matcher(newText).matches()) {
						return change;
					} else
						return null;
				});

		ref_txtAllowedUpperLimit.setTextFormatter(textFormatterUpperLimit);

		textFormatterUpperLimit.valueProperty().addListener((obs, oldValue, newValue) -> {
			// System.out.println("
			ApplicationLauncher.logger
					.debug("guiPropertySettings: textFormatterUpperLimit: New double value: " + newValue);
		});

		TextFormatter<Double> textFormatterLowerLimit = new TextFormatter<Double>(new DoubleStringConverter(), +0.25,
				change -> {
					String newText = change.getControlNewText();
					if (validDoubleText.matcher(newText).matches()) {
						return change;
					} else
						return null;
				});

		ref_txtAllowedLowerLimit.setTextFormatter(textFormatterLowerLimit);

		textFormatterLowerLimit.valueProperty().addListener((obs, oldValue, newValue) -> {
			// System.out.println("
			ApplicationLauncher.logger
					.debug("guiPropertySettings: textFormatterLowerLimit: New double value: " + newValue);
		});

		TextFormatter<Double> textFormatterPostOperation = new TextFormatter<Double>(new DoubleStringConverter(),
				+1000.0,
				change -> {
					String newText = change.getControlNewText();
					if (validDoubleText.matcher(newText).matches()) {
						return change;
					} else
						return null;
				});

		ref_txtPostOperationValue.setTextFormatter(textFormatterPostOperation);

		textFormatterPostOperation.valueProperty().addListener((obs, oldValue, newValue) -> {
			// System.out.println("
			ApplicationLauncher.logger
					.debug("guiPropertySettings: textFormatterPostOperation: New double value: " + newValue);
		});

		ref_txtPfFilterUserEntry.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				if (newPropertyValue) {
					// System.out.println("Textfield on focus");
				} else {
					// System.out.println("Textfield out focus");
					if (ref_cmbBxPfFilterUserEntry.getSelectionModel().getSelectedItem().toString()
							.equals(ConstantApp.PF_UPF)) {
						// ref_txtPfFilterData.setText(String.format("%.01f",Float.parseFloat(ref_txtPfFilterUserEntry.getText())));
						ref_txtPfFilterData.setText(ref_txtPfFilterUserEntry.getText());
					} else {
						/*
						 * if(ref_txtPfFilterUserEntry.getText().length()==1){
						 * ref_txtPfFilterData.setText(String.format("%.01f",Float.parseFloat(
						 * ref_txtPfFilterUserEntry.getText()))+
						 * ref_cmbBxPfFilterUserEntry.getSelectionModel().getSelectedItem().toString());
						 * }else{
						 */
						if (ref_txtPfFilterUserEntry.getText().equals("1.0")) {
							ref_txtPfFilterData.setText(ref_txtPfFilterUserEntry.getText());
						} else {
							ref_txtPfFilterData.setText(ref_txtPfFilterUserEntry.getText() +
									ref_cmbBxPfFilterUserEntry.getSelectionModel().getSelectedItem().toString());
						}
						// }
					}
					if (ref_chkBxHeader3PfFilter.isSelected()) {
						chkBxHeader3PfFilterOnChange();
					}
				}
			}
		});

		ref_txtFreqFilterUserEntry.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				if (newPropertyValue) {
					// System.out.println("Textfield on focus");
				} else {
					// System.out.println("Textfield out focus");
					ref_txtFreqFilterData.setText(ref_txtFreqFilterUserEntry.getText());
					// ref_cmbBxFreqFilterUserEntry.getSelectionModel().getSelectedItem().toString());
					if (ref_chkBxHeader3FreqFilter.isSelected()) {
						chkBxHeader3FreqFilterOnChange();
					}
				}
			}
		});

		ref_txtEnergyFilterUserEntry.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				if (newPropertyValue) {
					// System.out.println("Textfield on focus");
				} else {
					// System.out.println("Textfield out focus");
					ref_txtEnergyFilterData.setText(ref_txtEnergyFilterUserEntry.getText());
					// ref_cmbBxEnergyFilterUserEntry.getSelectionModel().getSelectedItem().toString());

					if (ref_chkBxHeader3EnergyFilter.isSelected()) {
						chkBxHeader3EnergyFilterOnChange();
					}
				}
			}
		});

		ref_txtIterationReadingStartingId.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				if (newPropertyValue) {
					// System.out.println("Textfield on focus");
				} else {
					// System.out.println("Textfield out focus");
					// ref_txtEnergyFilterData.setText(ref_txtEnergyFilterUserEntry.getText());
					// ref_cmbBxEnergyFilterUserEntry.getSelectionModel().getSelectedItem().toString());

					// if(ref_chkBxHeader3IterationIdFilter.isSelected()){
					if (ref_chkBx_IterationIdHeaderPrefix.isSelected()) {
						chkBxHeader3IterationIdFilterOnChange();
					}
				}
			}
		});

		/*
		 * ref_txt_IterationIdHeaderPrefix.focusedProperty().addListener(new
		 * ChangeListener<Boolean>() {
		 * 
		 * @Override
		 * public void changed(ObservableValue<? extends Boolean> arg0, Boolean
		 * oldPropertyValue, Boolean newPropertyValue)
		 * {
		 * if (newPropertyValue)
		 * {
		 * //System.out.println("Textfield on focus");
		 * }
		 * else
		 * {
		 * //System.out.println("Textfield out focus");
		 * //ref_txtEnergyFilterData.setText(ref_txtEnergyFilterUserEntry.getText());
		 * //ref_cmbBxEnergyFilterUserEntry.getSelectionModel().getSelectedItem().
		 * toString());
		 * 
		 * if(ref_chkBxHeader3IterationIdFilter.isSelected()){
		 * if(ref_chkBx_IterationIdHeaderPrefix.isSelected()){
		 * chkBxHeader3IterationIdFilterOnChange();
		 * }
		 * }
		 * }
		 * }
		 * });
		 */

		ref_txtCustomTestNameUserEntry.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				if (newPropertyValue) {
					// System.out.println("Textfield on focus");
				} else {
					// System.out.println("Textfield out focus");
					// ref_txtEnergyFilterData.setText(ref_txtEnergyFilterUserEntry.getText());
					// ref_cmbBxEnergyFilterUserEntry.getSelectionModel().getSelectedItem().toString());
					String customTestNameUserEntry = ref_txtCustomTestNameUserEntry.getText();
					if (!customTestNameUserEntry.isEmpty()) {
						if (!customTestNameUserEntry.startsWith(ConstantApp.CUSTOM_TEST_ALIAS_NAME)) {
							customTestNameUserEntry = ConstantApp.CUSTOM_TEST_ALIAS_NAME + customTestNameUserEntry;
						}
					}
					ref_txtCustomTestNameData.setText(customTestNameUserEntry);
				}
			}
		});

		loadTableViewProperty();

		/*
		 * ref_txtVoltPercentFilterUserEntry.textProperty().addListener(new
		 * ChangeListener<String>() {
		 * 
		 * @Override
		 * public void changed(ObservableValue<? extends String> observable, String
		 * oldValue,
		 * String newValue) {
		 * if (!newValue.matches("\\d\\sa-zA-Z(\\:\\d*)?*")) {
		 * ref_txtVoltPercentFilterUserEntry.setText(newValue.replaceAll(
		 * "[^\\d\\sa-zA-Z(\\:\\d*)?*]", ""));
		 * //ref_txtVoltPercentFilterUserEntry.setText(oldValue);
		 * }
		 * }
		 * });
		 */
	}

	private void loadTableViewProperty() {

		ApplicationLauncher.logger.debug("loadTableViewProperty: Entry");

		ref_tvTestFilterCellStartPosition.setEditable(true);

		/*
		 * ref_colTestFilterCellStartPositionPageHeader.setCellValueFactory(cellData ->
		 * cellData.getValue().headerProperty());
		 * ref_colTestFilterCellStartPositionPageCellValue.setCellValueFactory(new
		 * PropertyValueFactory<ExcelCellValueModel, String>("cell_value"));
		 * ref_colTestFilterCellStartPositionPageCellValue.setCellFactory(
		 * TextFieldTableCell.forTableColumn());
		 * ref_colTestFilterCellStartPositionPageCellValue.setOnEditCommit(new
		 * EventHandler<CellEditEvent<ExcelCellValueModel, String>>() {
		 * public void handle(CellEditEvent<ExcelCellValueModel, String> t) {
		 * ExcelCellValueModel rowData = ((ExcelCellValueModel)
		 * t.getTableView().getItems().get(t.getTablePosition().getRow()));
		 * 
		 * 
		 * Matcher excelRowColMatcher =
		 * excelRowColUserEntryPattern.matcher(t.getNewValue());
		 * if(excelRowColMatcher.matches()){
		 * rowData.setcell_value(t.getNewValue().toUpperCase());
		 * //ref_tvTestFilterCellStartPosition.refresh();
		 * ApplicationLauncher.logger.
		 * debug("loadTableViewProperty : TestFilter: Cell Start: t.getNewValue() :" +
		 * t.getNewValue().toUpperCase());
		 * }else{
		 * 
		 * rowData.setcell_value("");
		 * ApplicationLauncher.logger.
		 * debug("loadTableViewProperty : TestFilter: Cell Start invalid data: t.getNewValue() :"
		 * + t.getNewValue());
		 * ApplicationLauncher.logger.info("loadTableViewProperty : ERROR_CODE_6000: "+
		 * ErrorCodeMapping.ERROR_CODE_6000_MSG +" - prompted!");
		 * WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6000,
		 * ErrorCodeMapping.ERROR_CODE_6000_MSG,AlertType.ERROR);
		 * 
		 * ref_tvTestFilterCellStartPosition.refresh();
		 * 
		 * }
		 * //ref_tvTestFilterCellStartPosition.refresh();
		 * }
		 * });
		 * 
		 * 
		 * ref_tvTestFilterCellHeaderPosition.setEditable(true);
		 * 
		 * 
		 * ref_colTestFilterCellHeaderPositionPageHeader.setCellValueFactory(cellData ->
		 * cellData.getValue().headerProperty());
		 * ref_colTestFilterCellHeaderPositionPageCellValue.setCellValueFactory(new
		 * PropertyValueFactory<ExcelCellValueModel, String>("cell_value"));
		 * ref_colTestFilterCellHeaderPositionPageCellValue.setCellFactory(
		 * TextFieldTableCell.forTableColumn());
		 * ref_colTestFilterCellHeaderPositionPageCellValue.setOnEditCommit(new
		 * EventHandler<CellEditEvent<ExcelCellValueModel, String>>() {
		 * public void handle(CellEditEvent<ExcelCellValueModel, String> t) {
		 * ExcelCellValueModel rowData = ((ExcelCellValueModel)
		 * t.getTableView().getItems().get(t.getTablePosition().getRow()));
		 * 
		 * 
		 * Matcher excelRowColMatcher =
		 * excelRowColUserEntryPattern.matcher(t.getNewValue());
		 * if(excelRowColMatcher.matches()){
		 * rowData.setcell_value(t.getNewValue().toUpperCase());
		 * ApplicationLauncher.logger.
		 * debug("loadTableViewProperty : TestFilter: Cell Header: t.getNewValue() :" +
		 * t.getNewValue().toUpperCase());
		 * }else{
		 * 
		 * rowData.setcell_value("");
		 * ApplicationLauncher.logger.
		 * debug("loadTableViewProperty : TestFilter: Cell Header invalid data: t.getNewValue() :"
		 * + t.getNewValue());
		 * ApplicationLauncher.logger.info("loadTableViewProperty : ERROR_CODE_6001: "+
		 * ErrorCodeMapping.ERROR_CODE_6001_MSG +" - prompted!");
		 * WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6001,
		 * ErrorCodeMapping.ERROR_CODE_6001_MSG,AlertType.ERROR);
		 * 
		 * 
		 * 
		 * //ApplicationLauncher.logger.
		 * info("loadTableViewProperty : ErrorCode - R8000: TestFilter Cell Header Data input contains unaccepted value , kindly rephrase - prompted!"
		 * );
		 * //WindowManager.InformUser("ErrorCode - R8000",
		 * "TestFilter Cell Header Data input contains unaccepted value , kindly rephrase!!"
		 * ,AlertType.ERROR);
		 * 
		 * ref_tvTestFilterCellHeaderPosition.refresh();
		 * 
		 * }
		 * }
		 * });
		 */

		// getKeyParamProperty
		// ref_colTestFilterCellHeaderPositionPageHeader.setCellValueFactory(cellData ->
		// cellData.getValue().getKeyParamProperty());
		ref_colTestFilterCellStartPositionPageHeader
				.setCellValueFactory(new PropertyValueFactory<RpPrintPosition, String>("keyParam"));
		ref_colTestFilterCellStartPositionPageCellValue
				.setCellValueFactory(new PropertyValueFactory<RpPrintPosition, String>("cellPosition"));
		ref_colTestFilterCellStartPositionPageCellValue.setCellFactory(TextFieldTableCell.forTableColumn());
		ref_colTestFilterCellStartPositionPageCellValue
				.setOnEditCommit(new EventHandler<CellEditEvent<RpPrintPosition, String>>() {
					public void handle(CellEditEvent<RpPrintPosition, String> t) {
						RpPrintPosition rowData = ((RpPrintPosition) t.getTableView().getItems()
								.get(t.getTablePosition().getRow()));

						Matcher excelRowColMatcher = excelRowColUserEntryPattern.matcher(t.getNewValue());
						if (excelRowColMatcher.matches()) {
							rowData.setCellPosition(t.getNewValue().toUpperCase());
							// ref_tvTestFilterCellStartPosition.refresh();
							ApplicationLauncher.logger
									.debug("loadTableViewProperty : TestFilter: Cell Start: t.getNewValue() :"
											+ t.getNewValue().toUpperCase());
						} else {

							rowData.setCellPosition("");
							ApplicationLauncher.logger.debug(
									"loadTableViewProperty : TestFilter: Cell Start invalid data: t.getNewValue() :"
											+ t.getNewValue());
							ApplicationLauncher.logger.info("loadTableViewProperty : ERROR_CODE_6000: "
									+ ErrorCodeMapping.ERROR_CODE_6000_MSG + " - prompted!");
							WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6000,
									ErrorCodeMapping.ERROR_CODE_6000_MSG, AlertType.ERROR);

							// ref_tvTestFilterCellStartPosition.refresh();

						}
						ref_tvTestFilterCellStartPosition.refresh();
					}
				});

		ref_tvTestFilterCellHeaderPosition.setEditable(true);

		ref_colTestFilterCellHeaderPositionPageHeader
				.setCellValueFactory(new PropertyValueFactory<RpPrintPosition, String>("keyParam"));
		ref_colTestFilterCellHeaderPositionPageTitleValue
				.setCellValueFactory(new PropertyValueFactory<RpPrintPosition, String>("headerValue"));
		ref_colTestFilterCellHeaderPositionPageCellValue
				.setCellValueFactory(new PropertyValueFactory<RpPrintPosition, String>("cellPosition"));
		ref_colTestFilterCellHeaderPositionPageCellValue.setCellFactory(TextFieldTableCell.forTableColumn());
		ref_colTestFilterCellHeaderPositionPageCellValue
				.setOnEditCommit(new EventHandler<CellEditEvent<RpPrintPosition, String>>() {
					public void handle(CellEditEvent<RpPrintPosition, String> t) {
						RpPrintPosition rowData = ((RpPrintPosition) t.getTableView().getItems()
								.get(t.getTablePosition().getRow()));

						Matcher excelRowColMatcher = excelRowColUserEntryPattern.matcher(t.getNewValue());
						if (excelRowColMatcher.matches()) {
							rowData.setCellPosition(t.getNewValue().toUpperCase());
							ApplicationLauncher.logger
									.debug("loadTableViewProperty : TestFilter: Cell Header: t.getNewValue() :"
											+ t.getNewValue().toUpperCase());
						} else {

							rowData.setCellPosition("");
							ApplicationLauncher.logger.debug(
									"loadTableViewProperty : TestFilter: Cell Header invalid data: t.getNewValue() :"
											+ t.getNewValue());
							ApplicationLauncher.logger.info("loadTableViewProperty : ERROR_CODE_6001: "
									+ ErrorCodeMapping.ERROR_CODE_6001_MSG + " - prompted!");
							WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6001,
									ErrorCodeMapping.ERROR_CODE_6001_MSG, AlertType.ERROR);

							// ApplicationLauncher.logger.info("loadTableViewProperty : ErrorCode - R8000:
							// TestFilter Cell Header Data input contains unaccepted value , kindly rephrase
							// - prompted!");
							// WindowManager.InformUser("ErrorCode - R8000", "TestFilter Cell Header Data
							// input contains unaccepted value , kindly rephrase!!",AlertType.ERROR);

							// ref_tvTestFilterCellHeaderPosition.refresh();

						}
						ref_tvTestFilterCellHeaderPosition.refresh();
					}
				});

		ref_tvMeterMetaDataCellStartPosition.setEditable(true);

		ref_colMeterMetaDataCellStartPositionPageHeader
				.setCellValueFactory(cellData -> cellData.getValue().headerProperty());
		ref_colMeterMetaDataCellStartPositionPageCellValue
				.setCellValueFactory(new PropertyValueFactory<ExcelCellValueModel, String>("cell_value"));
		ref_colMeterMetaDataCellStartPositionPageCellValue.setCellFactory(TextFieldTableCell.forTableColumn());
		ref_colMeterMetaDataCellStartPositionPageCellValue
				.setOnEditCommit(new EventHandler<CellEditEvent<ExcelCellValueModel, String>>() {
					public void handle(CellEditEvent<ExcelCellValueModel, String> t) {
						ExcelCellValueModel rowData = ((ExcelCellValueModel) t.getTableView().getItems()
								.get(t.getTablePosition().getRow()));
						/*
						 * if(t.getNewValue() != null){
						 * rowData.setcell_value(t.getNewValue().toUpperCase());
						 * ApplicationLauncher.logger.
						 * debug("loadTableViewProperty : MeterMetaData: Cell Start: t.getNewValue() :"
						 * + t.getNewValue().toUpperCase());
						 * }
						 */

						Matcher excelRowColMatcher = excelRowColUserEntryPattern.matcher(t.getNewValue());
						if (excelRowColMatcher.matches()) {
							rowData.setcell_value(t.getNewValue().toUpperCase());
							ApplicationLauncher.logger
									.debug("loadTableViewProperty : MeterMetaData: Cell Start: t.getNewValue() :"
											+ t.getNewValue().toUpperCase());
						} else {

							rowData.setcell_value("");
							ApplicationLauncher.logger.debug(
									"loadTableViewProperty : MeterMetaData: Cell Start invalid data: t.getNewValue() :"
											+ t.getNewValue());
							ApplicationLauncher.logger.info("loadTableViewProperty : ERROR_CODE_6002: "
									+ ErrorCodeMapping.ERROR_CODE_6002_MSG + " - prompted!");
							WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6002,
									ErrorCodeMapping.ERROR_CODE_6002_MSG, AlertType.ERROR);

							// ApplicationLauncher.logger.info("loadTableViewProperty : ErrorCode - R8000:
							// MeterMetaData Cell Start Data input contains unaccepted value , kindly
							// rephrase - prompted!");
							// WindowManager.InformUser("ErrorCode - R8000", "MeterMetaData Cell Start Data
							// input contains unaccepted value , kindly rephrase!!",AlertType.ERROR);

							ref_tvMeterMetaDataCellStartPosition.refresh();

						}
					}
				});

		ref_tvMeterMetaDataCellHeaderPosition.setEditable(true);

		ref_colMeterMetaDataCellHeaderPositionPageHeader
				.setCellValueFactory(cellData -> cellData.getValue().headerProperty());
		ref_colMeterMetaDataCellHeaderPositionPageCellValue
				.setCellValueFactory(new PropertyValueFactory<ExcelCellValueModel, String>("cell_value"));
		ref_colMeterMetaDataCellHeaderPositionPageCellValue.setCellFactory(TextFieldTableCell.forTableColumn());

		// Matcher excelRowColMatcher = null;
		ref_colMeterMetaDataCellHeaderPositionPageCellValue
				.setOnEditCommit(new EventHandler<CellEditEvent<ExcelCellValueModel, String>>() {
					public void handle(CellEditEvent<ExcelCellValueModel, String> t) {
						ExcelCellValueModel rowData = ((ExcelCellValueModel) t.getTableView().getItems()
								.get(t.getTablePosition().getRow()));
						if (t.getNewValue() != null) {
							Matcher excelRowColMatcher = excelRowColUserEntryPattern.matcher(t.getNewValue());
							if (excelRowColMatcher.matches()) {
								rowData.setcell_value(t.getNewValue().toUpperCase());
								ApplicationLauncher.logger
										.debug("loadTableViewProperty : MeterMetaData: Cell Header: t.getNewValue() :"
												+ t.getNewValue().toUpperCase());
							} else {

								rowData.setcell_value("");
								ApplicationLauncher.logger.debug(
										"loadTableViewProperty : MeterMetaData: Cell Header invalid data: t.getNewValue() :"
												+ t.getNewValue());
								ApplicationLauncher.logger.info("loadTableViewProperty : ERROR_CODE_6003: "
										+ ErrorCodeMapping.ERROR_CODE_6003_MSG + " - prompted!");
								WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6003,
										ErrorCodeMapping.ERROR_CODE_6003_MSG, AlertType.ERROR);

								// ApplicationLauncher.logger.info("loadTableViewProperty : ErrorCode - R8000:
								// MeterMetaData Cell Header Data input contains unaccepted value , kindly
								// rephrase - prompted!");
								// WindowManager.InformUser("ErrorCode - R8000", "MeterMetaData Cell Header Data
								// input contains unaccepted value , kindly rephrase!!",AlertType.ERROR);

								ref_tvMeterMetaDataCellHeaderPosition.refresh();

							}
						}
					}
				});

		// ref_colSelectedMeterDataSerialNo.setCellValueFactory(cellData ->
		// cellData.getValue().getSerialNoProperty());
		ref_colSelectedMeterDataSerialNo
				.setCellValueFactory(cellData -> cellData.getValue().getPsuedoSerialNoProperty());
		ref_colSelectedMeterDataType.setCellValueFactory(cellData -> cellData.getValue().getDataTypeKeyProperty());
		ref_colSelectedMeterPopulateData.setCellValueFactory(new ReportFilterPopulateDataCheckBoxValueFactory());
		ref_colSelectedMeterPopulateData.setStyle("-fx-alignment: CENTER;");
		ref_colSelectedMeterPopulateForEachDut
				.setCellValueFactory(new ReportFilterPopulateEachDutCheckBoxValueFactory());
		ref_colSelectedMeterPopulateForEachDut.setStyle("-fx-alignment: CENTER;");

		ref_colPopulateDataSelection.setCellValueFactory(new MeterDataPopulateTypeListComboBoxValueFactory());

		// ref_colSelectedMeterPopulateForEachDut.setAlignment(Pos.CENTER);

		ref_colMeasuredDataSerialNo.setCellValueFactory(cellData -> cellData.getValue().getSerialNoProperty());
		ref_colMeasuredDataKey.setCellValueFactory(cellData -> cellData.getValue().getDataTypeKeyProperty());
		ref_colMeasuredDataPopulateData.setCellValueFactory(new ReportFilterPopulateDataCheckBoxValueFactory());
		ref_colMeasuredDataPopulateData.setStyle("-fx-alignment: CENTER;");
		// ref_colDummyDisplay.setCellValueFactory(new
		// ReportFilterPopulateEachDutCheckBoxValueFactory());
		// ReportFilterDataTypeModel selectedMeterDataList = new
		// ReportFilterDataTypeModel("1","CTR Ratio",false, false);

		ref_colMeterMetaDataListSerialNo.setCellValueFactory(cellData -> cellData.getValue().getSerialNoProperty());
		ref_colMeterMetaDataListPageNo
				.setCellValueFactory(cellData -> cellData.getValue().getPageNumberProperty().asObject());
		ref_colMeterMetaDataListMeterDataType
				.setCellValueFactory(cellData -> cellData.getValue().getDataTypeKeyProperty());
		ref_colMeterMetaDataListCellPosition
				.setCellValueFactory(cellData -> cellData.getValue().getDataCellPositionProperty());
		ref_colMeterMetaDataListPopulateOnlyHeader
				.setCellValueFactory(new ReportFilterPopulateHeadersOnlyDataDisplayCheckBoxValueFactory());
		ref_colMeterMetaDataListPopulateOnlyHeader.setStyle("-fx-alignment: CENTER;");
		ref_colMeterMetaDataListPopulateForEachDut
				.setCellValueFactory(new ReportFilterPopulateEachDutDisplayCheckBoxValueFactory());
		ref_colMeterMetaDataListPopulateForEachDut.setStyle("-fx-alignment: CENTER;");

		ref_colTestFilterListFilterActive
				.setCellValueFactory(new ReportTestFilterListFilterActiveCheckBoxValueFactory());
		ref_colTestFilterListFilterActive.setStyle("-fx-alignment: CENTER;");

		ref_colTestFilterListMergeUpperLowerLimits
				.setCellValueFactory(new ReportTestFilterListMergedLimitsCheckBoxValueFactory());
		ref_colTestFilterListMergeUpperLowerLimits.setStyle("-fx-alignment: CENTER;");
		////// ref_colTestFilterListMergedLimitCellPosition.setCellValueFactory(cellData
		////// -> cellData.getValue().getResultLowerLimitCellPositionProperty());
		ref_colTestFilterListReplicateData.setCellValueFactory(new ReportTestFilterListReplicateCheckBoxValueFactory());
		ref_colTestFilterListReplicateData.setStyle("-fx-alignment: CENTER;");
		ref_colTestFilterListReplicateDataCellPosition
				.setCellValueFactory(new ReportTestFilterListReplicateResultCellPositionListComboBoxValueFactory());
		ref_colTestFilterListInputProcessDataList
				.setCellValueFactory(new ReportTestFilterListInputProcessListComboBoxValueFactory());

		// serialnoColumn.setCellValueFactory(new
		// PropertyValueFactory<DeploymentDataModel, String>("serialno"));

		ref_colTestFilterListSerialNo
				.setCellValueFactory(new PropertyValueFactory<ReportProfileTestDataFilter, String>("tableSerialNo"));// cellData
																														// ->
																														// cellData.getValue().getSerialNoProperty());
		ref_colTestFilterListPageNo
				.setCellValueFactory(new PropertyValueFactory<ReportProfileTestDataFilter, String>("pageNumber"));// cellData
																													// ->
																													// cellData.getValue().getPageNumberProperty());
		ref_colTestFilterListFilterName
				.setCellValueFactory(new PropertyValueFactory<ReportProfileTestDataFilter, String>("testFilterName"));// cellData
																														// ->
																														// cellData.getValue().getTestFilterNameProperty());
		ref_colTestFilterListTestType
				.setCellValueFactory(new PropertyValueFactory<ReportProfileTestDataFilter, String>("testTypeSelected"));// cellData
																														// ->
																														// cellData.getValue().getTestTypeSelectedProperty());
		ref_colTestFilterListIterationId.setCellValueFactory(
				new PropertyValueFactory<ReportProfileTestDataFilter, String>("iterationReadingIdUserEntry"));// cellData
																												// ->
																												// cellData.getValue().getIterationReadingIdUserEntryProperty());
		ref_colTestFilterListOperationMode
				.setCellValueFactory(new PropertyValueFactory<ReportProfileTestDataFilter, String>("operationMode"));// cellData
																														// ->
																														// cellData.getValue().getOperationModeProperty());
		ref_colTestFilterListExecutionResultType.setCellValueFactory(
				new PropertyValueFactory<ReportProfileTestDataFilter, String>("testExecutionResultTypeSelected"));// cellData
																													// ->
																													// cellData.getValue().getTestExecutionResultTypeSelectedProperty());
		ref_colTestFilterListResultDataType
				.setCellValueFactory(new PropertyValueFactory<ReportProfileTestDataFilter, String>("resultDataType"));// cellData
																														// ->
																														// cellData.getValue().getResultDataTypeCellPositionProperty());
		ref_colTestFilterListResultValueCellPosition.setCellValueFactory(
				new PropertyValueFactory<ReportProfileTestDataFilter, String>("resultValueCellPosition"));// cellData ->
																											// cellData.getValue().getResultValueCellPositionProperty());
		ref_colTestFilterListResultStatusCellPosition.setCellValueFactory(
				new PropertyValueFactory<ReportProfileTestDataFilter, String>("resultStatusCellPosition"));// cellData
																											// ->
																											// cellData.getValue().getResultStatusCellPositionProperty());
		ref_colTestFilterListHeader1CellPosition.setCellValueFactory(
				new PropertyValueFactory<ReportProfileTestDataFilter, String>("header1_CellPosition"));// cellData ->
																										// cellData.getValue().getHeader1_CellPositionProperty());
		ref_colTestFilterListHeader2CellPosition.setCellValueFactory(
				new PropertyValueFactory<ReportProfileTestDataFilter, String>("header2_CellPosition"));// cellData ->
																										// cellData.getValue().getHeader2_CellPositionProperty());
		ref_colTestFilterListHeader3CellPosition.setCellValueFactory(
				new PropertyValueFactory<ReportProfileTestDataFilter, String>("header3_CellPosition"));// cellData ->
																										// cellData.getValue().getHeader3_CellPositionProperty());
		ref_colTestFilterListTestTypeAlias
				.setCellValueFactory(new PropertyValueFactory<ReportProfileTestDataFilter, String>("testTypeAlias"));// cellData
																														// ->
																														// cellData.getValue().getTestTypeAliasProperty());
		ref_colTestFilterListUpperLimitCellPosition.setCellValueFactory(
				new PropertyValueFactory<ReportProfileTestDataFilter, String>("resultUpperLimitCellPosition"));// cellData
																												// ->
																												// cellData.getValue().getResultUpperLimitCellPositionProperty());
		ref_colTestFilterListLowerLimitCellPosition.setCellValueFactory(
				new PropertyValueFactory<ReportProfileTestDataFilter, String>("resultLowerLimitCellPosition"));// cellData
																												// ->
																												// cellData.getValue().getResultLowerLimitCellPositionProperty());

		ref_colTestFilterListNonDisplayedDataSet.setCellValueFactory(
				new PropertyValueFactory<ReportProfileTestDataFilter, String>("nonDisplayedDataSet"));// cellData ->
																										// cellData.getValue().getNonDisplayedDataSetProperty());
		ref_colTestFilterListOperationMethod.setCellValueFactory(
				new PropertyValueFactory<ReportProfileTestDataFilter, String>("operationProcessMethod"));// cellData ->
																											// cellData.getValue().getOperationProcessMethodProperty());
		ref_colTestFilterListFilterPreview
				.setCellValueFactory(new PropertyValueFactory<ReportProfileTestDataFilter, String>("filterPreview"));// cellData
																														// ->
																														// cellData.getValue().getFilterPreviewProperty());

		ApplicationLauncher.logger.debug("loadTableViewProperty: Exit");
	}

	private void refAssignment() {

		ref_cmbBxReportProfile = cmbBxReportProfile;
		// ref_rdBtnPopulateVertical = rdBtnPopulateVertical;
		// ref_rdBtnPopulateHorizontal = rdBtnPopulateHorizontal;
		ref_chkBxEnableFilter = chkBxEnableFilter;
		// ref_chkBxRefStdInitialRegister = chkBxRefStdInitialRegister;
		// ref_chkBxRefStdFinalRegister = chkBxRefStdFinalRegister;
		// ref_chkBxDutPulseCount = chkBxDutPulseCount;
		// ref_chkBxDutInitialRegister = chkBxDutInitialRegister;
		// ref_chkBxDutFinalRegister = chkBxDutFinalRegister;
		// ref_chkBxErrorResultValue = chkBxErrorResultValue;
		ref_chkBxPopulateDutErrorResultStatus = chkBxPopulateDutErrorResultStatus;
		// ref_txtTestTypeAlias = txtTestTypeAlias;
		ref_txtVoltPercentFilterUserEntry = txtVoltPercentFilterUserEntry;
		ref_txtCurrentPercentFilterUserEntry = txtCurrentPercentFilterUserEntry;
		ref_txtPfFilterUserEntry = txtPfFilterUserEntry;
		ref_txtFreqFilterUserEntry = txtFreqFilterUserEntry;
		ref_txtEnergyFilterUserEntry = txtEnergyFilterUserEntry;
		// ref_txtRefVoltPercentFilterUserEntry = txtRefVoltPercentFilterUserEntry;
		// ref_txtRefCurrentPercentFilterUserEntry =
		// txtRefCurrentPercentFilterUserEntry;
		// ref_txtRefFreqFilterUserEntry = txtRefFreqFilterUserEntry;
		ref_txtVoltPercentFilterData = txtVoltPercentFilterData;
		ref_txtCurrentPercentFilterData = txtCurrentPercentFilterData;
		ref_txtPfFilterData = txtPfFilterData;
		ref_txtFreqFilterData = txtFreqFilterData;
		ref_txtEnergyFilterData = txtEnergyFilterData;
		// ref_txtRefVoltPercentFilterData = txtRefVoltPercentFilterData;
		// ref_txtRefCurrentPercentFilterData = txtRefCurrentPercentFilterData;
		// ref_txtRefFreqFilterData = txtRefFreqFilterData;
		// ref_txtErrorValueCellStartPosition = txtErrorValueCellStartPosition;
		// ref_txtErrorStatusCellStartPosition = txtErrorStatusCellStartPosition;
		ref_txtTestFilterPageNumber = txtTestFilterPageNumber;
		ref_txtMeterMetaDataPageNumber = txtMeterMetaDataPageNumber;
		ref_txtMeterMetaDataPageName = txtMeterMetaDataPageName;
		ref_lbl_MeterProfileMetaDataPageName = lbl_MeterProfileMetaDataPageName;
		ref_txtIterationReadingStartingId = txtIterationReadingStartingId;
		ref_txtIterationReadingEndingId = txtIterationReadingEndingId;
		ref_cmbBxVoltPercentFilterUserEntry = cmbBxVoltPercentFilterUserEntry;
		ref_cmbBxCurrentPercentFilterUserEntry = cmbBxCurrentPercentFilterUserEntry;
		ref_cmbBxPfFilterUserEntry = cmbBxPfFilterUserEntry;
		ref_cmbBxFreqFilterUserEntry = cmbBxFreqFilterUserEntry;
		ref_cmbBxEnergyFilterUserEntry = cmbBxEnergyFilterUserEntry;
		// ref_cmbBxRefVoltPercentFilterUserEntry = cmbBxRefVoltPercentFilterUserEntry;
		// ref_cmbBxRefCurrentPercentFilterUserEntry =
		// cmbBxRefCurrentPercentFilterUserEntry;
		// ref_cmbBxRefFreqFilterUserEntry = cmbBxRefFreqFilterUserEntry;
		ref_cmbBxExecutionResultType = cmbBxExecutionResultType;
		ref_cmbBxOperationSourceParamType = cmbBxOperationSourceParamType;
		ref_txtHeader3Value = txtHeader3Value;
		// ref_txtHeader3CellPosition = txtHeader3CellPosition;
		ref_chkBxPopulateHeader3 = chkBxPopulateHeader3;
		ref_chkBxHeader3VoltageFilter = chkBxHeader3VoltageFilter;
		ref_chkBxHeader3CurrentFilter = chkBxHeader3CurrentFilter;
		ref_chkBxHeader3PfFilter = chkBxHeader3PfFilter;
		ref_chkBxHeader3FreqFilter = chkBxHeader3FreqFilter;
		ref_chkBxHeader3EnergyFilter = chkBxHeader3EnergyFilter;
		ref_chkBxHeader3IterationIdFilter = chkBxHeader3IterationIdFilter;
		ref_chkBxHeader3CustomAllowed = chkBxHeader3CustomAllowed;
		ref_txt_IterationIdHeaderPrefix = txt_IterationIdHeaderPrefix;
		ref_chkBx_IterationIdHeaderPrefix = chkBx_IterationIdHeaderPrefix;

		ref_chkBxPopulateHeader1 = chkBxPopulateHeader1;
		ref_chkBxPopulateHeader2 = chkBxPopulateHeader2;
		// ref_chkBxPopulateVertical = chkBxPopulateVertical;
		// ref_chkBxOperationInput = chkBxOperationInput;
		// ref_chkBxOperationOutput = chkBxOperationOutput ;
		ref_chkBxReplicateData = chkBxReplicateData;
		// ref_txtHeader1CellPosition = txtHeader1CellPosition;
		// ref_txtHeader2CellPosition = txtHeader2CellPosition;
		ref_txtHeader1Value = txtHeader1Value;
		ref_txtHeader2Value = txtHeader2Value;
		ref_cmbBxReplicateCountUserEntry = cmbBxReplicateCountUserEntry;
		ref_cmbBxOperationInputUserEntry = cmbBxOperationInputUserEntry;
		// ref_cmbBxOperationOutputUserEntry = cmbBxOperationOutputUserEntry ;
		ref_cmbBxOperationCriteriaInputData = cmbBxOperationCriteriaInputData;
		ref_cmbBxOperationCriteriaLocalOutputData = cmbBxOperationCriteriaLocalOutputData;
		ref_cmbBxOperationCriteriaProcessData = cmbBxOperationCriteriaProcessData;
		ref_tvTestFilterCellStartPosition = tvTestFilterCellStartPosition;
		ref_tvTestFilterCellHeaderPosition = tvTestFilterCellHeaderPosition;

		ref_tvMeterMetaDataCellStartPosition = tvMeterMetaDataCellStartPosition;
		ref_tvMeterMetaDataCellHeaderPosition = tvMeterMetaDataCellHeaderPosition;
		// ref_tvInputProcessList = tvInputProcessList;
		ref_listViewInputProcessList = listViewInputProcessList;
		// ref_tvOperationProcessList = tvOperationProcessList;
		// ref_tvOutputProcessList = tvOutputProcessList;

		ref_titledPanePopulateData = titledPanePopulateData;
		ref_titledPaneTestTypeData = titledPaneTestTypeData;
		ref_titledPaneOperation = titledPaneOperation;
		ref_titledPaneCellPosition = titledPaneCellPosition;
		ref_colTestFilterCellStartPositionPageHeader = colTestFilterCellStartPositionPageHeader;
		ref_colTestFilterCellStartPositionPageCellValue = colTestFilterCellStartPositionPageCellValue;
		ref_colTestFilterCellHeaderPositionPageHeader = colTestFilterCellHeaderPositionPageHeader;
		ref_colTestFilterCellHeaderPositionPageTitleValue = colTestFilterCellHeaderPositionPageTitleValue;
		ref_colTestFilterCellHeaderPositionPageCellValue = colTestFilterCellHeaderPositionPageCellValue;

		ref_colMeterMetaDataCellStartPositionPageHeader = colMeterMetaDataCellStartPositionPageHeader;
		ref_colMeterMetaDataCellStartPositionPageCellValue = colMeterMetaDataCellStartPositionPageCellValue;
		ref_colMeterMetaDataCellHeaderPositionPageHeader = colMeterMetaDataCellHeaderPositionPageHeader;
		ref_colMeterMetaDataCellHeaderPositionPageCellValue = colMeterMetaDataCellHeaderPositionPageCellValue;

		ref_rdBtnOperationNone = rdBtnOperationNone;
		ref_rdBtnOperationInput = rdBtnOperationInput;
		ref_rdBtnOperationOutput = rdBtnOperationOutput;

		ref_cmbBxTestType = cmbBxTestType;
		ref_cmbBxTestTypeSub = cmbBxTestTypeSub;

		ref_txtFilterName = txtFilterName;
		ref_txtAllowedUpperLimit = txtAllowedUpperLimit;
		ref_txtAllowedLowerLimit = txtAllowedLowerLimit;
		ref_chkBxPopulateLocalOutputData = chkBxPopulateLocalOutputData;
		ref_chkBxPopulateUpperLimitData = chkBxPopulateUpperLimitData;
		ref_chkBxPopulateLowerLimitData = chkBxPopulateLowerLimitData;
		ref_chkBxCompareWithLimits = chkBxCompareWithLimits;
		ref_chkBxPopulateComparedLocalResultStatus = chkBxPopulateComparedLocalResultStatus;
		ref_chkBxPopulateComparedMasterResultStatus = chkBxPopulateComparedMasterResultStatus;
		ref_cmbBxOperationComparedLocalResultStatusOutput = cmbBxOperationComparedLocalResultStatusOutput;
		ref_cmbBxOperationComparedMasterResultStatusOutput = cmbBxOperationComparedMasterResultStatusOutput;
		ref_titledPaneCellHeaderPosition = titledPaneCellHeaderPosition;
		ref_txtFilterComments = txtFilterComments;
		ref_chkBxDiscardRackPositionInDutSerialNumber = chkBxDiscardRackPositionInDutSerialNumber;
		ref_chkBxAppendMeterSerialNoAndRackPositionListDisplay = chkBxAppendMeterSerialNoAndRackPositionListDisplay;

		ref_chkBxMergeUpperLowerLimit = chkBxMergeUpperLowerLimit;
		ref_cmbBxOperationCriteriaMasterOutputData = cmbBxOperationCriteriaMasterOutputData;
		ref_chkBxPopulateMasterOutputData = chkBxPopulateMasterOutputData;
		// ref_chkBxOperationActive = chkBxOperationActive;
		ref_cmbBxBaseTemplate = cmbBxBaseTemplate;
		ref_cmbBxMeterMetaDataPopulateType = cmbBxMeterMetaDataPopulateType;
		ref_cmbBxTestFilterDataPopulateType = cmbBxTestFilterDataPopulateType;

		ref_tvSelectedMeterDataType = tvSelectedMeterDataType;
		;
		ref_tvSelectedTestFilterMeasuredData = tvSelectedTestFilterMeasuredData;

		ref_colSelectedMeterDataSerialNo = colSelectedMeterDataSerialNo;
		ref_colSelectedMeterDataType = colSelectedMeterDataType;
		ref_colSelectedMeterPopulateData = colSelectedMeterPopulateData;
		ref_colSelectedMeterPopulateForEachDut = colSelectedMeterPopulateForEachDut;
		ref_colPopulateDataSelection = colPopulateDataSelection;

		ref_colMeasuredDataSerialNo = colMeasuredDataSerialNo;
		ref_colMeasuredDataKey = colMeasuredDataKey;
		ref_colMeasuredDataPopulateData = colMeasuredDataPopulateData;

		ref_titledPaneMeasuredData = titledPaneMeasuredData;
		ref_accordTestFilter = accordTestFilter;

		ref_btnInputAdd = btnInputAdd;
		ref_btnInputDelete = btnInputDelete;
		ref_titledPaneMeterDataCellPosition = titledPaneMeterDataCellPosition;
		ref_titledPaneMeterDataCellHeaderPosition = titledPaneMeterDataCellHeaderPosition;
		ref_cmBxPageNumber = cmBxPageNumber;
		// ref_colDummyDisplay = colDummyDisplay;

		ref_tvMeterMetaDataList = tvMeterMetaDataList;

		ref_colMeterMetaDataListSerialNo = colMeterMetaDataListSerialNo;
		ref_colMeterMetaDataListPageNo = colMeterMetaDataListPageNo;
		ref_colMeterMetaDataListMeterDataType = colMeterMetaDataListMeterDataType;
		ref_colMeterMetaDataListPopulateOnlyHeader = colMeterMetaDataListPopulateOnlyHeader;
		ref_colMeterMetaDataListPopulateForEachDut = colMeterMetaDataListPopulateForEachDut;
		ref_colMeterMetaDataListCellPosition = colMeterMetaDataListCellPosition;

		ref_lblCustomTestName = lblCustomTestName;
		ref_txtCustomTestNameUserEntry = txtCustomTestNameUserEntry;
		ref_txtCustomTestNameData = txtCustomTestNameData;

		ref_colTestFilterListSerialNo = colTestFilterListSerialNo;
		ref_colTestFilterListPageNo = colTestFilterListPageNo;
		ref_colTestFilterListFilterName = colTestFilterListFilterName;
		ref_colTestFilterListFilterActive = colTestFilterListFilterActive;
		ref_colTestFilterListTestType = colTestFilterListTestType;
		ref_colTestFilterListFilterPreview = colTestFilterListFilterPreview;
		// ref_colTestFilterListFilterUnit = colTestFilterListFilterUnit;
		ref_colTestFilterListIterationId = colTestFilterListIterationId;
		ref_colTestFilterListOperationMode = colTestFilterListOperationMode;
		ref_colTestFilterListExecutionResultType = colTestFilterListExecutionResultType;
		// ref_colTestFilterListRsmDataType = colTestFilterListRsmDataType;
		ref_colTestFilterListResultDataType = colTestFilterListResultDataType;
		ref_colTestFilterListResultValueCellPosition = colTestFilterListResultValueCellPosition;
		ref_colTestFilterListResultStatusCellPosition = colTestFilterListResultStatusCellPosition;
		ref_colTestFilterListHeader1CellPosition = colTestFilterListHeader1CellPosition;
		ref_colTestFilterListHeader2CellPosition = colTestFilterListHeader2CellPosition;
		ref_colTestFilterListHeader3CellPosition = colTestFilterListHeader3CellPosition;

		ref_tvTestFilterDataList = tvTestFilterDataList;

		ref_colTestFilterListTestTypeAlias = colTestFilterListTestTypeAlias;
		ref_colTestFilterListUpperLimitCellPosition = colTestFilterListUpperLimitCellPosition;
		ref_colTestFilterListLowerLimitCellPosition = colTestFilterListLowerLimitCellPosition;

		ref_colTestFilterListNonDisplayedDataSet = colTestFilterListNonDisplayedDataSet;
		ref_colTestFilterListMergeUpperLowerLimits = colTestFilterListMergeUpperLowerLimits;
		// ref_colTestFilterListMergedLimitCellPosition =
		// colTestFilterListMergedLimitCellPosition;
		ref_colTestFilterListReplicateData = colTestFilterListReplicateData;
		ref_colTestFilterListReplicateDataCellPosition = colTestFilterListReplicateDataCellPosition;
		ref_colTestFilterListOperationMethod = colTestFilterListOperationMethod;
		ref_colTestFilterListInputProcessDataList = colTestFilterListInputProcessDataList;

		ref_titledPaneExecutionMode = titledPaneExecutionMode;
		ref_rdBtnMainCt = rdBtnMainCt;
		ref_rdBtnNeutralCt = rdBtnNeutralCt;
		ref_rdBtnImportMode = rdBtnImportMode;
		ref_rdBtnExportMode = rdBtnExportMode;

		ref_chkBxPostOperationActive = chkBxPostOperationActive;
		ref_txtPostOperationValue = txtPostOperationValue;
		ref_cmbBxPostOperationMethod = cmbBxPostOperationMethod;

		ref_btnReportProfileSave = btnReportProfileSave;
		ref_btnReportProfileLoad = btnReportProfileLoad;
		ref_btnReportProfileAdd = btnReportProfileAdd;
		ref_btnReportProfileDelete = btnReportProfileDelete;
		ref_btnReportProfileEdit = btnReportProfileEdit;

		ref_titledPaneTestFilterData = titledPaneTestFilterData;
		ref_titledPaneMeterProfileMetaData = titledPaneMeterProfileMetaData;

		ref_tabMeterProfileDataList = tabMeterProfileDataList;
		ref_tabTestFilterDataList = tabTestFilterDataList;
		ref_cmbBxReportProfileGroup = cmbBxReportProfileGroup;
		ref_cmbBxMetaDataPageName = cmbBxMetaDataPageName;

		ref_chkBxMeterProfileMetaDataPageActive = chkBxMeterProfileMetaDataPageActive;
		ref_chkBxMeterProfileMetaDataListPageActive = chkBxMeterProfileMetaDataListPageActive;

		ref_chkBxDutMetaDataApplyForAllPages = chkBxDutMetaDataApplyForAllPages;
		ref_chkBxDutMetaDataClubPageNoAndMaxNoOfPages = chkBxDutMetaDataClubPageNoAndMaxNoOfPages;
		ref_chkBxSplitDutDisplayInToMultiplePage = chkBxSplitDutDisplayInToMultiplePage;
		ref_txtMaxDutDisplayPerPage = txtMaxDutDisplayPerPage;
		ref_txtTemplateFileNameWithPath = txtTemplateFileNameWithPath;
		ref_txtOutputPath = txtOutputPath;

		ref_btnAddGroupProfile = btnAddGroupProfile;
		ref_btnSaveGroupProfile = btnSaveGroupProfile;
		ref_gridPaneControl = gridPaneControl;

		ref_cmbBxParameterProfileName = cmbBxParameterProfileName;

		ref_lblPageName = lblPageName;
		ref_lblPageNumber = lblPageNumber;
		ref_lblMaxDutDisplayPerPage = lblMaxDutDisplayPerPage;

		ref_cmbBxResultPrintStyleName = cmbBxResultPrintStyleName;
		ref_cmbBxGenericHeaderPrintStyleName = cmbBxGenericHeaderPrintStyleName;
		ref_cmbBxTableHeaderPrintStyleName = cmbBxTableHeaderPrintStyleName;

		ref_chkBxPopulateHeaderTestPeriod = chkBxPopulateHeaderTestPeriod;
		ref_chkBxPopulateHeaderWarmupPeriod = chkBxPopulateHeaderWarmupPeriod;
		ref_chkBxPopulateHeaderActualVoltage = chkBxPopulateHeaderActualVoltage;
		ref_chkBxPopulateHeaderActualCurrent = chkBxPopulateHeaderActualCurrent;
		ref_chkBxPopulateHeaderActualPf = chkBxPopulateHeaderActualPf;
		ref_chkBxPopulateHeaderActualFreq = chkBxPopulateHeaderActualFreq;
		ref_chkBxPopulateHeaderActualEnergy = chkBxPopulateHeaderActualEnergy;
		ref_lblReplicateCount = lblReplicateCount;

		ref_chkBxPopulateRsmInitial = chkBxPopulateRsmInitial;
		ref_chkBxPopulateRsmFinal = chkBxPopulateRsmFinal;
		ref_chkBxPopulateRsmDifference = chkBxPopulateRsmDifference;
		ref_chkBxPopulateDutInitial = chkBxPopulateDutInitial;
		ref_chkBxPopulateDutFinal = chkBxPopulateDutFinal;
		ref_chkBxPopulateDutDifference = chkBxPopulateDutDifference;
		ref_chkBxPopulateDutPulseCount = chkBxPopulateDutPulseCount;
		ref_chkBxPopulateDutCalcErrorPercentage = chkBxPopulateDutCalcErrorPercentage;
		ref_chkBxPopulateDutOnePulsePeriod = chkBxPopulateDutOnePulsePeriod;

		ref_chkBxPopulateDutAverageValue = chkBxPopulateDutAverageValue;
		ref_chkBxPopulateDutAverageStatus = chkBxPopulateDutAverageStatus;
		ref_chkBxPopulateHeader4 = chkBxPopulateHeader4;
		ref_chkBxPopulateHeader5 = chkBxPopulateHeader5;
		ref_chkBxPopulateDutOnePulsePeriod = chkBxPopulateDutOnePulsePeriod;
		// ref_titledPaneDutResultPopulateType = titledPaneDutResultPopulateType;

		ref_txtHeader4Value = txtHeader4Value;
		ref_txtHeader5Value = txtHeader5Value;

	}

	private static void applyUacSettings() {

		ApplicationLauncher.logger.info("ReportHeaderConfigController : applyUacSettings :  Entry");
		ArrayList<UacDataModel> uacSelectProfileScreenList = DeviceDataManagerController
				.getUacSelectProfileScreenList();
		String screenName = "";
		for (int i = 0; i < uacSelectProfileScreenList.size(); i++) {

			screenName = uacSelectProfileScreenList.get(i).getScreenName();
			switch (screenName) {
				case ConstantApp.UAC_REPORT_PROFILE_CONFIG_SCREEN:

					if (!uacSelectProfileScreenList.get(i).getVisibleEnabled()) {
						ref_btnAddGroupProfile.setVisible(false);
						ref_btnSaveGroupProfile.setVisible(false);
					}

					if (!uacSelectProfileScreenList.get(i).getExecutePossible()) {
						// ref_btn_deploy.setDisable(true);

					}

					if (!uacSelectProfileScreenList.get(i).getAddPossible()) {
						// ref_btn_Create.setDisable(true);

					}

					if (!uacSelectProfileScreenList.get(i).getUpdatePossible()) {
						// ref_btnSave.setDisable(true);

					}

					if (!uacSelectProfileScreenList.get(i).getDeletePossible()) {
						// ref_btn_Delete.setDisable(true);

					}
					break;

				default:
					break;
			}

		}
	}

	public void loadReportProfileList() {

		boolean loadDefault = false;// true;

		if (loadDefault) {

			ref_cmbBxReportProfileGroup.getItems().add(SELECT_DISPLAY);
			ref_cmbBxReportProfileGroup.getItems().add(SAMPLE_GROUP);
			// ref_cmbBxReportProfileGroup.getSelectionModel().select(0);
			ref_cmbBxReportProfileGroup.getSelectionModel().selectLast();
			Set<String> hSet = new HashSet<String>(ConstantAppConfig.REPORT_PROFILE_LIST);
			hSet.addAll(ConstantAppConfig.REPORT_PROFILE_LIST);

			ref_cmbBxReportProfile.getItems().clear();
			ref_cmbBxReportProfile.getItems().addAll(hSet);
			// ref_cmbBxReportProfile.getSelectionModel().select(0);
			ref_cmbBxReportProfile.getSelectionModel().select(ConstantAppConfig.DefaultReportProfileDisplay);
			setSelectedReportProfile(ref_cmbBxReportProfile.getSelectionModel().getSelectedItem().toString());
			setupDataForReportProfileManage();
			// LoadSavedData();
		} else {

			MultiValuedMap<String, String> groupNameHashMap = new ArrayListValuedHashMap<String, String>();

			getActiveReportProfileDatabaseList().stream().forEach(e -> {
				ApplicationLauncher.logger.info("loadReportProfileList: groupNameList : " + e.getReportGroupName());
				// if(groupNameHashMap.containsKey(e.getReportGroupName())){

				// }else{
				groupNameHashMap.put(e.getReportGroupName(), e.getReportProfileName());
				// }
				// groupNameSetList.add(e.getReportGroupName());
			});

			// ref_cmbBxReportProfileGroup.getItems().addAll(groupNameSetList);
			// String lastProfileGroupName = "";
			groupNameHashMap.keySet().stream().forEach(e -> {

				ref_cmbBxReportProfileGroup.getItems().add(e);
				// lastProfileGroupName = e;
				// groupNameHashMap

			});

			ref_cmbBxReportProfileGroup.getSelectionModel().selectLast();
			String lastProfileGroupName = (String) ref_cmbBxReportProfileGroup.getSelectionModel().getSelectedItem();// .toString();

			ApplicationLauncher.logger.info("loadReportProfileList: lastProfileGroupName : " + lastProfileGroupName);
			ApplicationLauncher.logger.info("loadReportProfileList: groupNameHashMap : " + groupNameHashMap);

			// String profileNameList = groupNameHashMap.entries().stream().filter(e ->
			// e.getKey().equals(lastProfileGroupName)).findFirst().get().getValue();

			Collection<String> reportFileNameList = groupNameHashMap.get(lastProfileGroupName);
			reportFileNameList.stream().forEach(value -> {

				ref_cmbBxReportProfile.getItems().add(value);
			});

			ref_cmbBxReportProfile.getSelectionModel().selectLast();
			setGroupProfileNameHashMap(groupNameHashMap);
		}

	}

	public String getSelectedReportProfile() {
		return selectedReportProfile;
	}

	public void setSelectedReportProfile(String selectedReportProfile) {
		this.selectedReportProfile = selectedReportProfile;
	}

	public void loadTestTypes() {
		ApplicationLauncher.logger.info("LoadTestTypes : Entry");

		ref_cmbBxTestType.getItems().clear();
		ref_cmbBxTestType.getItems().addAll(ConstantReport.REPORT_TEST_TYPES_DISPLAY);
		ref_cmbBxTestType.getItems().add(ConstantApp.DISPLAY_TC_TITLE_CUSTOM_TEST);
		// ref_cmbBxTestType.getItems().add(ConstantReport.REPORT_TEST_TYPES_NONE);
		ref_cmbBxTestType.getSelectionModel().select(0);

	}

	public void loadPopulateDataTypes() {
		ApplicationLauncher.logger.info("loadPopulateDataTypes : Entry");

		ArrayList<String> dataTypeList = new ArrayList<String>();

		ConstantReportV2.getResultDataTypeHashMap().keySet().forEach(k -> dataTypeList.add(k));

		ref_cmbBxExecutionResultType.getItems().clear();
		ref_cmbBxExecutionResultType.getItems().addAll(dataTypeList);
		ref_cmbBxExecutionResultType.getSelectionModel().select(0);

		// ref_cmbBxOperationSourceParamType.getItems().add(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_AVERAGE);
		// ref_cmbBxOperationSourceParamType.getItems().add(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_ERROR_VALUE);

		// ref_cmbBxOperationSourceParamType.getItems().add(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_AUTO_INSERT_CALC_ERROR);

		dataTypeList.clear();

		ConstantReportV2.getResultSourceTypeHashMap().keySet().forEach(k -> dataTypeList.add(k));

		ref_cmbBxOperationSourceParamType.getItems().clear();
		ref_cmbBxOperationSourceParamType.getItems().addAll(dataTypeList);

		ref_cmbBxOperationSourceParamType.getSelectionModel().selectFirst();

		ref_cmbBxTestTypeSub.setDisable(true);
		ref_cmbBxTestTypeSub.getItems().clear();
		ref_cmbBxTestTypeSub.getItems().addAll(REV_PHASE_SEQ_SUB_LIST);
		ref_cmbBxTestTypeSub.getSelectionModel().selectFirst();

		String customerId = ConstantAppConfig.REPORT_PROFILE_DEFAULT_ACTIVE_CUSTOMER_ID;
		List<OperationParam> operationParamDataList = getReportOperationParamService().findByCustomerId(customerId);

		Set<String> paramProfileSet = new HashSet<String>();

		operationParamDataList.stream().forEach(e -> {
			// e.getOperationParamProfileName();
			paramProfileSet.add(e.getOperationParamProfileName());
		});
		ArrayList<String> sortedParamProfile = new ArrayList<String>(paramProfileSet);
		Collections.sort(sortedParamProfile);

		ref_cmbBxParameterProfileName.getItems().setAll(sortedParamProfile);
		ref_cmbBxParameterProfileName.getSelectionModel().selectLast();
		ref_cmbBxParameterProfileName.setDisable(true);
		ref_cmbBxBaseTemplate.setDisable(true);

		ref_cmbBxResultPrintStyleName.getItems().clear();
		ref_cmbBxGenericHeaderPrintStyleName.getItems().clear();
		ref_cmbBxTableHeaderPrintStyleName.getItems().clear();

		ArrayList<String> printStyleList = (ArrayList<String>) ConstantAppConfig.PRINT_STYLE_LIST.stream()
				.map(e -> e.getReportPrintStyleName())
				.collect(Collectors.toList());

		ref_cmbBxResultPrintStyleName.getItems().add(ConstantReportV2.REPORT_PRINT_SELECT_RESULT_STYLE);
		ref_cmbBxResultPrintStyleName.getItems().addAll(printStyleList);
		ref_cmbBxGenericHeaderPrintStyleName.getItems().add(ConstantReportV2.REPORT_PRINT_SELECT_GENERIC_HEADER_STYLE);
		ref_cmbBxGenericHeaderPrintStyleName.getItems().addAll(printStyleList);
		ref_cmbBxTableHeaderPrintStyleName.getItems().add(ConstantReportV2.REPORT_PRINT_SELECT_TABLE_HEADER_STYLE);
		ref_cmbBxTableHeaderPrintStyleName.getItems().addAll(printStyleList);

		ref_cmbBxResultPrintStyleName.getSelectionModel().selectFirst();
		ref_cmbBxGenericHeaderPrintStyleName.getSelectionModel().selectFirst();
		ref_cmbBxTableHeaderPrintStyleName.getSelectionModel().selectFirst();

	}

	public void LoadParameters() {
		ApplicationLauncher.logger.info("LoadParameters : Entry");
		ArrayList<String> parameters = new ArrayList<String>();
		// String test_type = cmbBox_testtype.getSelectionModel().getSelectedItem();
		int TestTypeDisplayIndex = ref_cmbBxTestType.getSelectionModel().getSelectedIndex();
		String test_type = ConstantReport.REPORT_TEST_TYPES.get(TestTypeDisplayIndex);
		if (test_type.equals(TestProfileType.InfluenceFreq.toString())) {
			parameters.add(ConstantReport.PARAMETER_TYPE_VOLTAGE);
			parameters.add(ConstantReport.PARAMETER_TYPE_CURRENT);
			parameters.add(ConstantReport.PARAMETER_TYPE_PF);
			parameters.add(ConstantReport.PARAMETER_TYPE_FREQUENCY);
			titledpane_freq_harm.setText(ConstantReport.PARAMETER_TYPE_FREQUENCY);
		} else if (test_type.equals(TestProfileType.NoLoad.toString())) {
			parameters.add(ConstantReport.PARAMETER_TYPE_VOLTAGE);
		} else if (test_type.equals(TestProfileType.STA.toString())) {
			parameters.add(ConstantReport.PARAMETER_TYPE_VOLTAGE);
			parameters.add(ConstantReport.PARAMETER_TYPE_CURRENT);
		}
		// else if(test_type.equals("UnbalancedLoad")){
		else if (test_type.equals(ConstantApp.TEST_PROFILE_UNBALANCED_LOAD)) {
			parameters.add(ConstantReport.PARAMETER_TYPE_CURRENT);
			parameters.add(ConstantReport.PARAMETER_TYPE_PF);
		} else if (test_type.equals(TestProfileType.InfluenceHarmonic.toString())) {
			parameters.add(ConstantReport.PARAMETER_TYPE_VOLTAGE);
			parameters.add(ConstantReport.PARAMETER_TYPE_CURRENT);
			parameters.add(ConstantReport.PARAMETER_TYPE_PF);
			parameters.add(ConstantReport.PARAMETER_TYPE_HARMONICS);
			titledpane_freq_harm.setText(ConstantReport.PARAMETER_TYPE_HARMONICS);
		} else {
			parameters.add(ConstantReport.PARAMETER_TYPE_VOLTAGE);
			parameters.add(ConstantReport.PARAMETER_TYPE_CURRENT);
			parameters.add(ConstantReport.PARAMETER_TYPE_PF);
		}

		cmBxPageNumber.getItems().clear();
		cmBxPageNumber.getItems().addAll(parameters);
		cmBxPageNumber.getSelectionModel().select(0);
		ClearAllListView();
		LoadReferenceValue();
		LoadSavedData(test_type);
		// if((test_type.equals("UnbalancedLoad")) &&
		// (listview_voltage.getItems().isEmpty())){
		/*
		 * if((test_type.equals(ConstantApp.TEST_PROFILE_UNBALANCED_LOAD)) &&
		 * (listview_voltage.getItems().isEmpty())){
		 * LoadDefaultVoltages();
		 * }
		 */
	}

	public void LoadDefaultVoltages() {
		// listview_voltage.getItems().addAll(ConstantReport.UNBALANCED_LOAD_TEMPL_VOLTAGES);
	}

	public void ClearAllListView() {
		/*
		 * listview_voltage.getItems().clear();
		 * listview_current.getItems().clear();
		 * listview_phase.getItems().clear();
		 * listview_frequency.getItems().clear();
		 */
	}

	public void loadExtensions() {
		ApplicationLauncher.logger.info("loadExtensions : Entry");
		ArrayList<String> units = new ArrayList<String>();
		units.add(ConstantReport.EXTENSION_TYPE_PHASE_UPF);
		units.add(ConstantReport.EXTENSION_TYPE_PHASE_L);
		units.add(ConstantReport.EXTENSION_TYPE_PHASE_C);

		ref_cmbBxPfFilterUserEntry.getItems().clear();
		ref_cmbBxPfFilterUserEntry.getItems().addAll(units);

		units.clear();

		units.add(ConstantReport.EXTENSION_TYPE_CURRENT_IB);
		units.add(ConstantReport.EXTENSION_TYPE_CURRENT_IMAX);

		ref_cmbBxCurrentPercentFilterUserEntry.getItems().clear();
		ref_cmbBxCurrentPercentFilterUserEntry.getItems().addAll(units);

		// ref_cmbBxRefCurrentPercentFilterUserEntry.getItems().clear();
		// ref_cmbBxRefCurrentPercentFilterUserEntry.getItems().addAll(units);

		units.clear();

		units.add(ConstantReport.EXTENSION_TYPE_VOLTAGE_U);
		String voltUnitTerminator = ConstantApp.VOLTAGE_PHASE_SPLITTER + VOLTAGE_UNIT_TERMNATOR;
		units.add(ConstantApp.FIRST_PHASE_DISPLAY_NAME + voltUnitTerminator);
		units.add(ConstantApp.SECOND_PHASE_DISPLAY_NAME + voltUnitTerminator);
		units.add(ConstantApp.THIRD_PHASE_DISPLAY_NAME + voltUnitTerminator);

		units.add(ConstantApp.FIRST_PHASE_DISPLAY_NAME + ConstantApp.SECOND_PHASE_DISPLAY_NAME + voltUnitTerminator);
		units.add(ConstantApp.SECOND_PHASE_DISPLAY_NAME + ConstantApp.THIRD_PHASE_DISPLAY_NAME + voltUnitTerminator);
		units.add(ConstantApp.FIRST_PHASE_DISPLAY_NAME + ConstantApp.THIRD_PHASE_DISPLAY_NAME + voltUnitTerminator);

		ref_cmbBxVoltPercentFilterUserEntry.getItems().clear();
		ref_cmbBxVoltPercentFilterUserEntry.getItems().addAll(units);

		// ref_cmbBxRefVoltPercentFilterUserEntry.getItems().clear();
		// ref_cmbBxRefVoltPercentFilterUserEntry.getItems().addAll(units);

		units.clear();

		units.add(ConstantReport.EXTENSION_TYPE_ENERGY_ACTIVE);
		units.add(ConstantReport.EXTENSION_TYPE_ENERGY_REACTIVE);
		units.add(ConstantReport.EXTENSION_TYPE_ENERGY_APPARENT);

		ref_cmbBxEnergyFilterUserEntry.getItems().clear();
		ref_cmbBxEnergyFilterUserEntry.getItems().addAll(units);

		units.clear();

		units.add(ConstantApp.FREQ_UNIT);

		ref_cmbBxFreqFilterUserEntry.getItems().clear();
		ref_cmbBxFreqFilterUserEntry.getItems().addAll(units);

		Platform.runLater(() -> {
			ref_cmbBxPfFilterUserEntry.getSelectionModel().select(0);
			ref_cmbBxCurrentPercentFilterUserEntry.getSelectionModel().select(0);
			// ref_cmbBxRefCurrentPercentFilterUserEntry.getSelectionModel().select(0);
			ref_cmbBxVoltPercentFilterUserEntry.getSelectionModel().select(0);
			// ref_cmbBxRefVoltPercentFilterUserEntry.getSelectionModel().select(0);
			ref_cmbBxEnergyFilterUserEntry.getSelectionModel().select(0);
			ref_cmbBxFreqFilterUserEntry.getSelectionModel().select(0);
			ref_txtPfFilterUserEntry.setText("1.0");
			ref_txtPfFilterUserEntry.setDisable(true);
			ref_txtPfFilterData.setText("1.0");
		});

		/*
		 * if(!cmBxPageNumber.getSelectionModel().isEmpty()){
		 * String parameter = cmBxPageNumber.getSelectionModel().getSelectedItem();
		 * if(parameter.equals(ConstantReport.PARAMETER_TYPE_VOLTAGE)){
		 * units.add(ConstantReport.EXTENSION_TYPE_VOLTAGE_U);
		 * }
		 * else if(parameter.equals(ConstantReport.PARAMETER_TYPE_CURRENT)){
		 * units.add(ConstantReport.EXTENSION_TYPE_CURRENT_IB);
		 * units.add(ConstantReport.EXTENSION_TYPE_CURRENT_IMAX);
		 * }
		 * else if(parameter.equals(ConstantReport.PARAMETER_TYPE_PF)){
		 * units.add(ConstantReport.EXTENSION_TYPE_PHASE_UPF);
		 * units.add(ConstantReport.EXTENSION_TYPE_PHASE_L);
		 * units.add(ConstantReport.EXTENSION_TYPE_PHASE_C);
		 * }
		 * else{
		 * units.add("");
		 * }
		 * 
		 * if(units.isEmpty()){
		 * //cmbBox_extension.setDisable(true);
		 * //cmbBox_extension.getItems().clear();
		 * }
		 * else{
		 * cmbBox_extension.getItems().clear();
		 * cmbBox_extension.getItems().addAll(units);
		 * cmbBox_extension.getSelectionModel().select(0);
		 * }
		 * //extension_on_click();
		 * //LoadMaxSize(parameter);
		 * }
		 */
	}

	public void LoadMaxSize(String parameter) {
		int TestTypeDisplayIndex = ref_cmbBxTestType.getSelectionModel().getSelectedIndex();
		String test_type = ConstantReport.REPORT_TEST_TYPES.get(TestTypeDisplayIndex);
		switch (test_type) {
			// case "InfluenceFreq":
			case ConstantApp.TEST_PROFILE_INFLUENCE_FREQ:
				if (parameter.equals(ConstantReport.PARAMETER_TYPE_VOLTAGE)) {
					Max_Size = 1;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_CURRENT)) {
					Max_Size = 50;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_PF)) {
					Max_Size = 50;
				} else {
					Max_Size = 50;
				}
				break;

			// case "InfluenceVolt":
			case ConstantApp.TEST_PROFILE_INFLUENCE_VOLT:
				if (parameter.equals(ConstantReport.PARAMETER_TYPE_VOLTAGE)) {
					Max_Size = 50;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_CURRENT)) {
					Max_Size = 50;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_PF)) {
					Max_Size = 50;
				}
				break;

			// case "VoltageUnbalance":
			case ConstantApp.TEST_PROFILE_VOLTAGE_UNBALANCE:
				if (parameter.equals(ConstantReport.PARAMETER_TYPE_VOLTAGE)) {
					Max_Size = 7;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_CURRENT)) {
					Max_Size = 1;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_PF)) {
					Max_Size = 1;
				}
				break;

			// case "SelfHeating":
			case ConstantApp.TEST_PROFILE_SELF_HEATING:
				if (parameter.equals(ConstantReport.PARAMETER_TYPE_VOLTAGE)) {
					Max_Size = 1;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_CURRENT)) {
					Max_Size = 1;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_PF)) {
					Max_Size = 50;
				}
				break;

			// case "Accuracy":
			case ConstantApp.TEST_PROFILE_ACCURACY:
				if (parameter.equals(ConstantReport.PARAMETER_TYPE_VOLTAGE)) {
					Max_Size = 1;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_CURRENT)) {
					Max_Size = 50;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_PF)) {
					Max_Size = 50;
				}
				break;

			// case "InfluenceHarmonic":
			case ConstantApp.TEST_PROFILE_INFLUENCE_HARMONIC:
				if (parameter.equals(ConstantReport.PARAMETER_TYPE_VOLTAGE)) {
					Max_Size = 1;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_CURRENT)) {
					Max_Size = 50;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_PF)) {
					Max_Size = 1;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_HARMONICS)) {
					Max_Size = 2;
				}
				break;

			// case "NoLoad":
			case ConstantApp.TEST_PROFILE_NOLOAD:
				if (parameter.equals(ConstantReport.PARAMETER_TYPE_VOLTAGE)) {
					Max_Size = 1;
				}
				break;

			// case "STA":
			case ConstantApp.TEST_PROFILE_STA:
				if (parameter.equals(ConstantReport.PARAMETER_TYPE_VOLTAGE)) {
					Max_Size = 1;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_CURRENT)) {
					Max_Size = 1;
				}
				break;

			// case "ConstantTest":
			// case "PhaseReversal":
			case ConstantApp.TEST_PROFILE_CONSTANT_TEST:
			case ConstantApp.TEST_PROFILE_PHASE_REVERSAL:
				if (parameter.equals(ConstantReport.PARAMETER_TYPE_VOLTAGE)) {
					Max_Size = 1;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_CURRENT)) {
					Max_Size = 1;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_PF)) {
					Max_Size = 1;
				}
				break;

			// case "Repeatability":
			case ConstantApp.TEST_PROFILE_REPEATABILITY:
				if (parameter.equals(ConstantReport.PARAMETER_TYPE_VOLTAGE)) {
					Max_Size = 1;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_CURRENT)) {
					Max_Size = 2;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_PF)) {
					Max_Size = 1;
				}
				break;

			// case "UnbalancedLoad":
			case ConstantApp.TEST_PROFILE_UNBALANCED_LOAD:
				if (parameter.equals(ConstantReport.PARAMETER_TYPE_CURRENT)) {
					Max_Size = 50;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_PF)) {
					Max_Size = 50;
				}
				break;

			default:
				break;
		}
	}

	public void LoadReferenceValue() {

	}

	public void LoadSavedData(String test_type) {
		ApplicationLauncher.logger.info("LoadSavedData : test_type: " + test_type);
	}

	public void LoadValuesToList(String header_type, String header_value) {
		ApplicationLauncher.logger.info("LoadValuesToList : " + header_type + "-" + header_value);
	}

	public void AddValue(ListView<String> listview) {
	}

	public boolean checkdataexists(String value, ListView<String> listview) {
		boolean dataexists = false;
		ObservableList<String> existing_data = listview.getItems();
		if (existing_data.size() != 0) {
			for (int i = 0; i < existing_data.size(); i++) {
				String data = existing_data.get(i);
				if (value.equals(data)) {
					dataexists = true;
					break;
				} else {
					dataexists = false;
				}
			}
		} else {
			dataexists = false;
		}
		return dataexists;
	}

	public void DeleteValue(ListView<String> listview) {
		ObservableList<String> values = listview.getItems();
		String del_value = listview.getSelectionModel().getSelectedItem();
		ApplicationLauncher.logger.info("DeleteValue : " + del_value);
		values.remove(del_value);
	}

	public ObservableList<String> getValuesFromList(ListView<String> listview) {
		ObservableList<String> values = listview.getItems();
		ApplicationLauncher.logger.info("getValuesFromList : " + values);
		return values;
	}


	@FXML
	public void reportHeaderSaveOnClick() {
		ApplicationLauncher.logger.debug("reportHeaderSaveOnClick : Entry");

		// WindowManager.InformUser("Saved Successfully", "Saved data successfully",
		// AlertType.INFORMATION);

		if (ref_tabMeterProfileDataList.isSelected()) {
			if (ref_tvMeterMetaDataList.getItems().size() > 0) {
				saveMeterProfileDataListToDataBase();
				readActiveReportProfileManageFromDataBase();

				WindowManager.InformUser("Meta Data saved", "Meta Data save success", AlertType.INFORMATION);
			} else {
				ApplicationLauncher.logger.debug("reportHeaderSaveOnClick : No items to save to DB");
			}

		} else if (ref_tabTestFilterDataList.isSelected()) {
			if (ref_tvTestFilterDataList.getItems().size() > 0) {
				saveTestDataListToDataBase();
				// readActiveReportProfileManageFromDataBase();
				WindowManager.InformUser("Test Filter Data saved", "Test Filter Data save success",
						AlertType.INFORMATION);

				// saveMeterProfileDataListToDataBase();
				// readActiveReportProfileManageFromDataBase();
			} else {
				ApplicationLauncher.logger.debug("reportHeaderSaveOnClick : No items to save to DB");
			}
		}

		ref_btnReportProfileSave.setDisable(true);
	}

	private void saveMeterProfileDataListToDataBase() {

		ApplicationLauncher.logger.debug("saveMeterProfileDataListToDataBase : Entry");

		// readReportProfileManageFromDataBase();
		// readMeterDataFromDataBase();

		ArrayList<ReportProfileMeterMetaDataFilter> meterMetaDataDatabaseList = convertMeterProfileDataDisplayModelToDatabaseModel();
		if (meterMetaDataDatabaseList.size() > 0) {
			getReportProfileManageModel().clearDutMetaDataList();
		}
		meterMetaDataDatabaseList.stream().forEach(e -> {
			ApplicationLauncher.logger
					.debug("saveMeterProfileDataListToDataBase : saving to DB : getFilterName: " + e.getFilterName());
			ApplicationLauncher.logger.debug(
					"saveMeterProfileDataListToDataBase : saving to DB : getMeterDataType: " + e.getMeterDataType());
			getReportProfileManageModel().addDutMetaData(e);

		});

		String customerId = ConstantAppConfig.REPORT_PROFILE_DEFAULT_ACTIVE_CUSTOMER_ID;
		getReportProfileManageModel().setCustomerId(customerId);

		// ApplicationLauncher.logger.debug("saveMeterProfileDataListToDataBase :
		// exportModeSelected: " +
		// getReportProfileManageModel().isExportModeSelected());
		// ApplicationLauncher.logger.debug("saveMeterProfileDataListToDataBase :
		// importModeSelected: " +
		// getReportProfileManageModel().isImportModeSelected());

		updateReportProfileDataFromGui();

		int getSavedId = getReportProfileManageService().saveToDb(getReportProfileManageModel());
		ApplicationLauncher.logger.debug("saveMeterProfileDataListToDataBase : getSavedId: " + getSavedId);
		setReportProfileManageModel(getReportProfileManageService().findById(getSavedId));

		// uiyi
		// List<ReportProfileMeterMetaDataFilter> metaDataList =
		// getReportProfileMeterMetaDataFilterService().findByReportProfileManageId(getSavedId);//
		// getReportProfileManageModel().getDutMetaDataList();
		// getReportProfileManageModel().setDutMetaDataList(metaDataList);
		ApplicationLauncher.logger.debug("saveMeterProfileDataListToDataBase : getReportProfileName: "
				+ getReportProfileManageModel().getReportProfileName());
		ApplicationLauncher.logger.debug("saveMeterProfileDataListToDataBase : getDutMetaDataList().size()2: "
				+ getReportProfileManageModel().getDutMetaDataList().size());
		// setReportProfileManageModel();
		ApplicationLauncher.logger.debug("saveMeterProfileDataListToDataBase : Exit");
	}

	private void updateReportProfileDataFromGui() {

		ApplicationLauncher.logger.debug("updateReportProfileDataFromGui : Entry");

		try {

			getReportProfileManageModel().setOutputFolderPath(ref_txtOutputPath.getText());
			getReportProfileManageModel().setTemplateFileNameWithPath(ref_txtTemplateFileNameWithPath.getText());
			getReportProfileManageModel()
					.setSplitDutDisplayInToMultiplePage(ref_chkBxSplitDutDisplayInToMultiplePage.isSelected());
			getReportProfileManageModel().setExportModeSelected(ref_rdBtnExportMode.isSelected());
			getReportProfileManageModel().setImportModeSelected(ref_rdBtnImportMode.isSelected());
			getReportProfileManageModel().setDutMainCtSelected(ref_rdBtnMainCt.isSelected());
			getReportProfileManageModel().setDutNeutralCtSelected(ref_rdBtnNeutralCt.isSelected());
			getReportProfileManageModel()
					.setMaxDutDisplayPerPage(Integer.parseInt(ref_txtMaxDutDisplayPerPage.getText()));
			getReportProfileManageModel().setPrintStyleResult(
					ref_cmbBxResultPrintStyleName.getSelectionModel().getSelectedItem().toString());
			getReportProfileManageModel().setPrintStyleGenericHeader(
					ref_cmbBxGenericHeaderPrintStyleName.getSelectionModel().getSelectedItem().toString());
			getReportProfileManageModel().setPrintStyleTableHeader(
					ref_cmbBxTableHeaderPrintStyleName.getSelectionModel().getSelectedItem().toString());

		} catch (Exception E) {

			ApplicationLauncher.logger.error("updateReportProfileDataFromGui: Exception: " + E.getMessage());
			// return false;
		}

	}

	public void saveTestDataListToDataBase() {
		ApplicationLauncher.logger.debug("saveTestDataListToDataBase : Entry");

		List<ReportProfileTestDataFilter> testFilterDatabaseList = ref_tvTestFilterDataList.getItems();// convertTestFilterDisplayModelToDatabaseModel();
		if (testFilterDatabaseList.size() > 0) {
			getReportProfileManageModel().clearTestDataFilterList();
		}
		testFilterDatabaseList.stream().forEach(e -> {
			ApplicationLauncher.logger
					.debug("saveTestDataListToDataBase : saving to DB : getTestFilterName: " + e.getTestFilterName());
			ApplicationLauncher.logger
					.debug("saveTestDataListToDataBase : saving to DB : getPageNumber: " + e.getPageNumber());

			e.getOperationProcessDataList().stream().forEach(e1 -> {
				ApplicationLauncher.logger.debug("saveTestDataListToDataBase : saving to DB : getId: " + e1.getId());
			});
			getReportProfileManageModel().addTestDataFilter(e);

		});

		String customerId = ConstantAppConfig.REPORT_PROFILE_DEFAULT_ACTIVE_CUSTOMER_ID;
		getReportProfileManageModel().setCustomerId(customerId);

		// ApplicationLauncher.logger.debug("saveMeterProfileDataListToDataBase :
		// exportModeSelected: " +
		// getReportProfileManageModel().isExportModeSelected());
		// ApplicationLauncher.logger.debug("saveMeterProfileDataListToDataBase :
		// importModeSelected: " +
		// getReportProfileManageModel().isImportModeSelected());

		updateReportProfileDataFromGui();

		int getSavedId = getReportProfileManageService().saveToDb(getReportProfileManageModel());
		ApplicationLauncher.logger.debug("saveTestDataListToDataBase : getSavedId: " + getSavedId);
		setReportProfileManageModel(getReportProfileManageService().findById(getSavedId));
		// updateOperationProcessDatabaseId();

		// ApplicationLauncher.logger.debug("saveMeterProfileDataListToDataBase :
		// getReportProfileName: " +
		// getReportProfileManageModel().getReportProfileName());
		// ApplicationLauncher.logger.debug("saveMeterProfileDataListToDataBase :
		// getDutMetaDataList().size()2: " +
		// getReportProfileManageModel().getDutMetaDataList().size());
		// btnReportProfileLoadOnClick();
		// readActiveReportProfileManageFromDataBase();
		// updateSelectedReportProfileData();
		// updateCommonGuiData();
		// publishDataOnMeterProfileMetaDataList();
		// publishDataOnTestFilterDataList();

		/*
		 * for(int dbIndex = 0; dbIndex <
		 * getReportProfileManageModel().getReportProfileTestDataFilterList().size() ;
		 * dbIndex++){
		 * ApplicationLauncher.logger.
		 * debug("saveTestDataListToDataBase : db getTestFilterName: " +
		 * getReportProfileManageModel().getReportProfileTestDataFilterList().get(
		 * dbIndex).getTestFilterName());
		 * String dbFilterName =
		 * getReportProfileManageModel().getReportProfileTestDataFilterList().get(
		 * dbIndex).getTestFilterName();
		 * List<OperationProcess> opProcessDataList =
		 * getReportProfileManageModel().getReportProfileTestDataFilterList().get(
		 * dbIndex).getOperationProcessDataList();
		 * for(int dbProcessIndex = 0; dbProcessIndex < opProcessDataList.size();
		 * dbProcessIndex++){
		 * 
		 * 
		 * 
		 * String dbProcessKey =
		 * opProcessDataList.get(dbProcessIndex).getOperationProcessKey();
		 * ApplicationLauncher.logger.
		 * debug("saveTestDataListToDataBase : dbProcessKey: " + dbProcessKey);
		 * 
		 * 
		 * for(int displayIndex = 0; displayIndex <
		 * ref_tvTestFilterDataList.getItems().size() ; displayIndex++){
		 * ApplicationLauncher.logger.
		 * debug("saveTestDataListToDataBase : display getTestFilterName: " +
		 * ref_tvTestFilterDataList.getItems().get(displayIndex).getTestFilterName());
		 * String displayFilterName =
		 * ref_tvTestFilterDataList.getItems().get(displayIndex).getTestFilterName();
		 * if(dbFilterName.equals(displayFilterName)){
		 * List<OperationProcess> displayOpProcessDataList =
		 * ref_tvTestFilterDataList.getItems().get(displayIndex).
		 * getOperationProcessDataList();
		 * 
		 * for(int displayProcessIndex = 0; displayProcessIndex <
		 * displayOpProcessDataList.size(); displayProcessIndex++){
		 * 
		 * 
		 * 
		 * String displayProcessKey =
		 * displayOpProcessDataList.get(displayProcessIndex).getOperationProcessKey();
		 * ApplicationLauncher.logger.
		 * debug("saveTestDataListToDataBase : displayProcessKey: " +
		 * displayProcessKey);
		 * if(dbProcessKey.equals(displayProcessKey)){
		 * if(ref_tvTestFilterDataList.getItems().get(displayIndex).
		 * getOperationProcessDataList().get(displayProcessIndex).getId() == null){
		 * int idValue =
		 * getReportProfileManageModel().getReportProfileTestDataFilterList().get(
		 * dbIndex).getOperationProcessDataList().get(dbProcessIndex).getId();
		 * ApplicationLauncher.logger.debug("saveTestDataListToDataBase : idValue: " +
		 * idValue);
		 * ref_tvTestFilterDataList.getItems().get(displayIndex).
		 * getOperationProcessDataList().get(displayProcessIndex).setId(idValue);
		 * }
		 * }
		 * 
		 * }
		 * }
		 * 
		 * }
		 * 
		 * }
		 * 
		 * }
		 */
		ApplicationLauncher.logger.debug("saveTestDataListToDataBase : Exit");
	}

	private void readActiveReportProfileManageFromDataBase() {

		ApplicationLauncher.logger.debug("readActiveReportProfileManageFromDataBase : Entry");

		// List<ReportProfileMeterMetaDataFilter> meterMetaDataDatabaseList =
		// dataManagerObj.getReportProfileMeterMetaDataFilterService().findAll();
		List<ReportProfileManage> reportProfileActiveDatabaseList = getReportProfileManageService().findAll();// findAllOrderByTableSerialNoAsc();

		List<String> reportProfileGroupNameFilterList = ConstantAppConfig.REPORT_PROFILE_DEFAULT_ACTIVE_GROUP_NAME_LIST;
		String reportProfileDefaultActiveCustomerId = ConstantAppConfig.REPORT_PROFILE_DEFAULT_ACTIVE_CUSTOMER_ID;

		// reportProfileActiveDatabaseList =
		// getReportProfileManageService().findActiveCustomerByReportGroupNameList(reportProfileDefaultActiveCustomerId,reportProfileGroupNameFilterList);//findAllOrderByTableSerialNoAsc();

		if (reportProfileGroupNameFilterList.contains(ConstantReportV2.REPORT_GROUP_IMPORT_ALL)) {
			ApplicationLauncher.logger
					.debug("ReportProfileConfigController: readActiveReportProfileManageFromDataBase : Importing All");

			reportProfileActiveDatabaseList = getReportProfileManageService()
					.findActiveProfileAndCustomerId(reportProfileDefaultActiveCustomerId);// findAllOrderByTableSerialNoAsc();
		} else {
			ApplicationLauncher.logger.debug(
					"ReportProfileConfigController: readActiveReportProfileManageFromDataBase : Importing filtered group");
			reportProfileActiveDatabaseList = getReportProfileManageService().findActiveCustomerByReportGroupNameList(
					reportProfileDefaultActiveCustomerId, reportProfileGroupNameFilterList);// findAllOrderByTableSerialNoAsc();
		}

		reportProfileActiveDatabaseList.stream().forEach(e -> {
			ApplicationLauncher.logger
					.debug("readActiveReportProfileManageFromDataBase Active : Reading from DB : getId: " + e.getId());
			ApplicationLauncher.logger
					.debug("readActiveReportProfileManageFromDataBase Active : Reading from DB : isProfileActive: "
							+ e.isProfileActive());
			ApplicationLauncher.logger
					.debug("readActiveReportProfileManageFromDataBase Active : Reading from DB : getReportGroupId: "
							+ e.getReportGroupId());
			ApplicationLauncher.logger
					.debug("readActiveReportProfileManageFromDataBase Active : Reading from DB : getReportGroupName: "
							+ e.getReportGroupName());
			ApplicationLauncher.logger
					.debug("readActiveReportProfileManageFromDataBase Active : Reading from DB : getReportProfileName: "
							+ e.getReportProfileName());

			ApplicationLauncher.logger.debug(
					"readActiveReportProfileManageFromDataBase : findActiveCustomerByReportGroupNameList: Reading from DB : 000000000000000000000000 ");
		});

		setActiveReportProfileDatabaseList(reportProfileActiveDatabaseList);
		ApplicationLauncher.logger.debug("readActiveReportProfileManageFromDataBase : Exit");
	}

	private ArrayList<ReportProfileMeterMetaDataFilter> convertMeterProfileDataDisplayModelToDatabaseModel() {

		ApplicationLauncher.logger.debug("convertMeterProfileDataDisplayModelToDatabaseModel : Entry");
		ObservableList<ReportMeterMetaDataTypeSubModel> meterMetaDataDisplayList = ref_tvMeterMetaDataList.getItems();
		ArrayList<ReportProfileMeterMetaDataFilter> meterMetaDataDatabaseList = new ArrayList<ReportProfileMeterMetaDataFilter>();
		ReportProfileMeterMetaDataFilter meterMetaDataDatabase = new ReportProfileMeterMetaDataFilter();
		String baseTemplateMetaDataPopulateType = ref_cmbBxMeterMetaDataPopulateType.getSelectionModel()
				.getSelectedItem().toString();
		String baseTemplateName = ref_cmbBxBaseTemplate.getSelectionModel().getSelectedItem().toString();
		String reportGroupId = "";
		String reportGroupName = ref_cmbBxReportProfileGroup.getSelectionModel().getSelectedItem().toString();
		String reportProfileId = "";
		String reportProfileName = ref_cmbBxReportProfile.getSelectionModel().getSelectedItem().toString();
		String filterName = ref_txtMeterMetaDataPageName.getText();// "";

		boolean appendDutSerialAndRackPosition = ref_chkBxDiscardRackPositionInDutSerialNumber.isSelected();// true;
		boolean filterActive = ref_chkBxMeterProfileMetaDataPageActive.isSelected();// true;
		// for(ReportMeterMetaDataTypeSubModel eachMeterMetaData :
		// getMeterMetaDataList()){//.stream().forEach(e -> {

		List<ReportMeterMetaDataTypeSubModel> populatedMetaDataList = ref_tvMeterMetaDataList.getItems();
		for (ReportMeterMetaDataTypeSubModel eachMeterMetaData : populatedMetaDataList) {
			meterMetaDataDatabase = new ReportProfileMeterMetaDataFilter();
			meterMetaDataDatabase.setPageNumber(Integer.parseInt(eachMeterMetaData.getPageNumber()));
			meterMetaDataDatabase.setDiscardRackPositionInDutSerialNumber(
					eachMeterMetaData.isDiscardRackPositionInDutSerialNumber());// eachMeterMetaData.
																				// appendDutSerialAndRackPosition);
			meterMetaDataDatabase.setBaseTemplateMetaDataPopulateType(baseTemplateMetaDataPopulateType);
			// meterMetaDataDatabase.setBaseTemplateName(baseTemplateName);
			meterMetaDataDatabase.setFilterActive(eachMeterMetaData.isFilterActive());
			meterMetaDataDatabase.setFilterName(filterName);
			// meterMetaDataDatabase.setReportGroupId(reportGroupId);
			// meterMetaDataDatabase.setReportGroupName(reportGroupName);
			// meterMetaDataDatabase.setReportProfileId(reportProfileId);
			// meterMetaDataDatabase.setReportProfileName(reportProfileName);
			meterMetaDataDatabase.setTableSerialNo(eachMeterMetaData.getSerialNo());
			meterMetaDataDatabase.setMeterDataType(eachMeterMetaData.getDataTypeKey());
			meterMetaDataDatabase.setPopulateOnlyOnHeader(eachMeterMetaData.getPopulateOnlyHeader());
			meterMetaDataDatabase.setPopulateForEachDut(eachMeterMetaData.getPopulateDataForEachDut());
			meterMetaDataDatabase.setCellPosition(eachMeterMetaData.getDataCellPosition());

			ApplicationLauncher.logger.debug("convertMeterProfileDataDisplayModelToDatabaseModel : getDataTypeKey(): "
					+ eachMeterMetaData.getDataTypeKey());

			meterMetaDataDatabaseList.add(meterMetaDataDatabase);
		}

		return meterMetaDataDatabaseList;
	}

	@FXML
	public void btnReportProfileAddOnClick() {
		ApplicationLauncher.logger.debug("btnReportProfileAddOnClick : Entry");

		if (ref_tabMeterProfileDataList.isSelected()) {

			guiMeterMetaDataDefaultSettings();
			ref_titledPaneMeterProfileMetaData.setExpanded(true);
			ref_titledPaneMeterProfileMetaData.setDisable(false);
			ref_titledPaneTestFilterData.setDisable(true);

			// setInsertDataMode(true);
			// ref_gridPaneControl.setDisable(true);

		} else if (ref_tabTestFilterDataList.isSelected()) {

			guiTestFilterDefaultSettings();
			getPageNumberPrompt();
			ref_titledPaneTestFilterData.setExpanded(true);
			ref_titledPaneTestFilterData.setDisable(false);
			ref_titledPaneMeterProfileMetaData.setDisable(true);
			ref_txtFilterName.setDisable(false);
		}

		setInsertDataMode(true);
		ref_gridPaneControl.setDisable(true);
		ref_btnReportProfileSave.setDisable(false);
		ref_tabMeterProfileDataList.setDisable(true);
		ref_tabTestFilterDataList.setDisable(true);
	}

	public void getPageNumberPrompt() {
		ApplicationLauncher.logger.debug("getPageNumberPrompt: Entry");

		Platform.runLater(() -> {
			String title = "Page Number";
			String header = "Enter page number";

			String userInputData = GuiUtils.textFieldInputDialogDisplay(header, title);

			if (userInputData.isEmpty()) {
				ref_txtTestFilterPageNumber.setText("1");
			} else if (GuiUtils.isNumber(userInputData)) {
				ref_txtTestFilterPageNumber.setText(userInputData);
			} else {
				ref_txtTestFilterPageNumber.setText("1");
			}
		});
	}

	public void SaveToDB(String test_type, String header_type, ObservableList<String> values) {
		ApplicationLauncher.logger.info("SaveToDB: values : " + values);
		/*
		 * if(values.size() != 0){
		 * for(int i=0; i<values.size(); i++){
		 * MySQL_Controller.sp_add_report_header_config(getSelectedReportProfile(),
		 * test_type, header_type, values.get(i));
		 * }
		 * ApplicationLauncher.LoadReportHeaderConfigProperty();
		 * ApplicationLauncher.LoadReportExcelConfigProperty();
		 * }
		 */

	}

	/*
	 * @FXML
	 * public void rdBtnPopulateVerticalOnChange(){
	 * if(ref_rdBtnPopulateVertical.isSelected()){
	 * ref_rdBtnPopulateHorizontal.setSelected(false);
	 * }else{
	 * ref_rdBtnPopulateVertical.setSelected(true);
	 * }
	 * 
	 * }
	 * 
	 * @FXML
	 * public void rdBtnPopulateHorizontalOnChange(){
	 * if(ref_rdBtnPopulateHorizontal.isSelected()){
	 * ref_rdBtnPopulateVertical.setSelected(false);
	 * }else{
	 * ref_rdBtnPopulateHorizontal.setSelected(true);
	 * }
	 * }
	 */

	@FXML
	public void cmBxPageNumberOnChange() {
		ApplicationLauncher.logger.debug("cmBxPageNumberOnChange: Entry");

	}

	/*
	 * public HashMap<String, String> getResultDataTypeHashMap() {
	 * return resultDataTypeHashMap;
	 * }
	 * 
	 * public void setResultDataTypeHashMap(HashMap<String, String>
	 * resultDataTypeHashMap) {
	 * this.resultDataTypeHashMap = resultDataTypeHashMap;
	 * }
	 */

	@FXML
	// public void cmbBxPopulateDataTypeOnChange(){
	public void cmbBxExecutionResultTypeOnChange() {
		ApplicationLauncher.logger.debug("cmbBxExecutionResultTypeOnChange: Entry");
		String selectedData = ref_cmbBxExecutionResultType.getSelectionModel().getSelectedItem().toString();
		if (selectedData.endsWith(ERROR_RESULT_STATUS_NOT_ALLOWED_TAIL_END)) {
			ref_chkBxPopulateDutErrorResultStatus.setSelected(false);
			ref_chkBxPopulateDutErrorResultStatus.setDisable(true);
		} else {
			ref_chkBxPopulateDutErrorResultStatus.setDisable(false);
		}

	}

	@FXML
	public void cmbBxOperationSourceParamTypeOnChange() {
		ApplicationLauncher.logger.debug("cmbBxOperationSourceParamTypeOnChange: Entry");
		String selectedData = ref_cmbBxOperationSourceParamType.getSelectionModel().getSelectedItem().toString();
		if (selectedData.endsWith(ERROR_RESULT_STATUS_NOT_ALLOWED_TAIL_END)) {
			ref_chkBxPopulateDutErrorResultStatus.setSelected(false);
			ref_chkBxPopulateDutErrorResultStatus.setDisable(true);
		} else {
			ref_chkBxPopulateDutErrorResultStatus.setDisable(false);
		}

	}

	@FXML
	public void chkBxPopulateHeader3OnChange() {

		if (ref_chkBxPopulateHeader3.isSelected()) {
			ref_chkBxHeader3VoltageFilter.setDisable(false);
			ref_chkBxHeader3CurrentFilter.setDisable(false);
			ref_chkBxHeader3PfFilter.setDisable(false);
			ref_chkBxHeader3FreqFilter.setDisable(false);
			ref_chkBxHeader3EnergyFilter.setDisable(false);
			// ref_chkBxHeader3IterationIdFilter.setDisable(false);
			// ref_chkBx_IterationIdHeaderPrefix.setDisable(false);
			ref_chkBxHeader3CustomAllowed.setDisable(false);
			// ref_txtHeader3CellPosition.setDisable(false);
		} else {
			ref_chkBxHeader3VoltageFilter.setDisable(true);
			ref_chkBxHeader3CurrentFilter.setDisable(true);
			ref_chkBxHeader3PfFilter.setDisable(true);
			ref_chkBxHeader3FreqFilter.setDisable(true);
			ref_chkBxHeader3EnergyFilter.setDisable(true);
			// ref_chkBxHeader3IterationIdFilter.setDisable(true);
			// ref_chkBx_IterationIdHeaderPrefix.setDisable(true);
			ref_chkBxHeader3CustomAllowed.setDisable(true);
			ref_txtHeader3Value.setText("");
			// ref_txtHeader3CellPosition.setDisable(true);
		}

	}

	@FXML
	public void cmbBxVoltPercentFilterUserEntryOnChange() {

		/*
		 * String value =
		 * ref_cmbBxVoltPercentFilterUserEntry.getSelectionModel().getSelectedItem().
		 * toString();
		 * ref_txtVoltPercentFilterData.setText(ref_txtVoltPercentFilterUserEntry.
		 * getText()+value);
		 */
		String selectedUnit = ref_cmbBxVoltPercentFilterUserEntry.getSelectionModel().getSelectedItem().toString();

		if (selectedUnit.equals(ConstantReport.EXTENSION_TYPE_VOLTAGE_U)) {
			ref_txtVoltPercentFilterData.setText(ref_txtVoltPercentFilterUserEntry.getText() +
					selectedUnit);
		} else {
			// ref_txtVoltPercentFilterData.setText( selectedUnit +
			// ref_txtVoltPercentFilterUserEntry.getText()+
			// ConstantReport.EXTENSION_TYPE_VOLTAGE_U);
			ref_txtVoltPercentFilterData.setText(
					selectedUnit.replace(VOLTAGE_PERCENT_PLACE_HOLDER, ref_txtVoltPercentFilterUserEntry.getText()));

		}
		if (ref_chkBxHeader3VoltageFilter.isSelected()) {
			chkBxHeader3VoltageFilterOnChange();
		}
	}

	@FXML
	public void cmbBxCurrentPercentFilterUserEntryOnChange() {

		String currentUnit = ref_cmbBxCurrentPercentFilterUserEntry.getSelectionModel().getSelectedItem().toString();
		String currentValue = ref_txtCurrentPercentFilterUserEntry.getText();
		/*
		 * float currentInFloat =
		 * Float.parseFloat(currentValue)/ConstantApp.CURRENT_PERCENT_CONVERTION_FACTOR;
		 * if(CURRENT_USER_ENTRY_IN_PERCENTAGE ){
		 * currentValue = String.valueOf(currentInFloat);
		 * }
		 */

		ref_txtCurrentPercentFilterData.setText(currentValue + currentUnit);

		if (ref_chkBxHeader3CurrentFilter.isSelected()) {
			chkBxHeader3CurrentFilterOnChange();
		}
	}

	@FXML
	public void cmbBxPfFilterUserEntryOnChange() {

		String value = ref_cmbBxPfFilterUserEntry.getSelectionModel().getSelectedItem().toString();

		/*
		 * if(ref_txtPfFilterUserEntry.getText().length()==1){
		 * ref_txtPfFilterData.setText(String.format("%.01f",Float.parseFloat(
		 * ref_txtPfFilterUserEntry.getText()))+value);
		 * }else {
		 */
		// ref_txtPfFilterData.setText(ref_txtPfFilterUserEntry.getText()+value);;l'l
		// }

		if (value.equals(ConstantApp.PF_UPF)) {
			ref_txtPfFilterUserEntry.setText("1.0");
			ref_txtPfFilterUserEntry.setDisable(true);
			ref_txtPfFilterData.setText(ref_txtPfFilterUserEntry.getText());
		} else {
			ref_txtPfFilterUserEntry.setDisable(false);
			if (ref_txtPfFilterUserEntry.getText().equals("1.0")) {
				ref_txtPfFilterUserEntry.setText("");
			}
			ref_txtPfFilterData.setText(ref_txtPfFilterUserEntry.getText() + value);
		}

		if (ref_chkBxHeader3PfFilter.isSelected()) {
			chkBxHeader3PfFilterOnChange();
		}
	}

	@FXML
	public void cmbBxFreqFilterUserEntryOnChange() {

		String value = ref_cmbBxFreqFilterUserEntry.getSelectionModel().getSelectedItem().toString();
		ref_txtFreqFilterData.setText(ref_txtFreqFilterUserEntry.getText() + value);
		if (ref_chkBxHeader3FreqFilter.isSelected()) {
			chkBxHeader3FreqFilterOnChange();
		}
	}

	@FXML
	public void cmbBxEnergyFilterUserEntryOnChange() {

		// String value =
		// ref_cmbBxEnergyFilterUserEntry.getSelectionModel().getSelectedItem().toString();
		ref_txtEnergyFilterData.setText(ref_txtEnergyFilterUserEntry.getText());
		if (ref_chkBxHeader3EnergyFilter.isSelected()) {
			chkBxHeader3EnergyFilterOnChange();
		}
	}

	public String getConcatHeader3Value() {
		String headerValue = "";
		String unitValue = "";
		if (ref_chkBxHeader3VoltageFilter.isSelected()) {
			if (!ref_txtVoltPercentFilterUserEntry.getText().isEmpty()) {
				headerValue = headerValue + ref_txtVoltPercentFilterData.getText();
			}
		}

		if (ref_chkBxHeader3CurrentFilter.isSelected()) {
			if (!ref_txtCurrentPercentFilterUserEntry.getText().isEmpty()) {
				if (headerValue.isEmpty()) {
					headerValue = ref_txtCurrentPercentFilterData.getText();
				} else {
					headerValue = headerValue + headerSeperator + ref_txtCurrentPercentFilterData.getText();
				}
			}
		}

		if (ref_chkBxHeader3PfFilter.isSelected()) {
			if (!ref_txtPfFilterUserEntry.getText().isEmpty()) {
				unitValue = ref_txtPfFilterData.getText();
				// if(unitValue.equals("1.0")){
				if (Float.parseFloat(unitValue) == 1.0f) {
					unitValue = ConstantApp.PF_UPF;
				}
				if (headerValue.isEmpty()) {
					headerValue = unitValue;
				} else {
					headerValue = headerValue + headerSeperator + unitValue;
				}
			}
		}

		if (ref_chkBxHeader3FreqFilter.isSelected()) {
			if (!ref_txtFreqFilterUserEntry.getText().isEmpty()) {
				unitValue = ref_cmbBxFreqFilterUserEntry.getSelectionModel().getSelectedItem().toString();
				if (headerValue.isEmpty()) {
					headerValue = ref_txtFreqFilterData.getText() + unitValue;
				} else {
					headerValue = headerValue + headerSeperator + ref_txtFreqFilterData.getText() + unitValue;
				}
			}
		}

		if (ref_chkBxHeader3EnergyFilter.isSelected()) {
			if (!ref_txtEnergyFilterUserEntry.getText().isEmpty()) {
				unitValue = ref_cmbBxEnergyFilterUserEntry.getSelectionModel().getSelectedItem().toString();
				if (headerValue.isEmpty()) {
					headerValue = ref_txtEnergyFilterData.getText() + unitValue;
				} else {
					headerValue = headerValue + headerSeperator + ref_txtEnergyFilterData.getText() + unitValue;
				}
			}
		}

		// if(ref_chkBxHeader3IterationIdFilter.isSelected()){
		if (ref_chkBx_IterationIdHeaderPrefix.isSelected()) {

			if (ConstantReportV2.REPEAT_START_TO_END_FEATURE_ENABLED) {
				if (!ref_txtIterationReadingStartingId.getText().isEmpty()) {
					String prefixIterationHeader = "";
					if (ref_chkBx_IterationIdHeaderPrefix.isSelected()) {
						prefixIterationHeader = ref_txt_IterationIdHeaderPrefix.getText();
					}
					if (headerValue.isEmpty()) {
						headerValue = prefixIterationHeader + ref_txtIterationReadingStartingId.getText();
					} else {
						headerValue = headerValue + headerSeperator + prefixIterationHeader
								+ ref_txtIterationReadingStartingId.getText();
					}
				}
			} else {

				if (!ref_txtIterationReadingStartingId.getText().isEmpty()) {
					String prefixIterationHeader = "";
					if (ref_chkBx_IterationIdHeaderPrefix.isSelected()) {
						prefixIterationHeader = ref_txt_IterationIdHeaderPrefix.getText();
					}
					if (headerValue.isEmpty()) {
						headerValue = prefixIterationHeader + ref_txtIterationReadingStartingId.getText();
					} else {
						headerValue = headerValue + headerSeperator + prefixIterationHeader
								+ ref_txtIterationReadingStartingId.getText();
					}
				}
			}
		}

		ApplicationLauncher.logger.debug("getConcatHeader3Value: headerValue: " + headerValue);

		return headerValue;
	}

	@FXML
	public void chkBxHeader3VoltageFilterOnChange() {
		// if(!isTestFilterEditMode()){
		ref_txtHeader3Value.setText(getConcatHeader3Value());
		// }

	}

	@FXML
	public void chkBxHeader3CurrentFilterOnChange() {
		// if(!isTestFilterEditMode()){
		ref_txtHeader3Value.setText(getConcatHeader3Value());
		// }
	}

	@FXML
	public void chkBxHeader3PfFilterOnChange() {
		// if(!isTestFilterEditMode()){
		ref_txtHeader3Value.setText(getConcatHeader3Value());
		// }
	}

	@FXML
	public void chkBxHeader3FreqFilterOnChange() {
		// if(!isTestFilterEditMode()){
		ref_txtHeader3Value.setText(getConcatHeader3Value());
		// }
	}

	@FXML
	public void chkBxHeader3EnergyFilterOnChange() {
		// if(!isTestFilterEditMode()){
		ref_txtHeader3Value.setText(getConcatHeader3Value());
		// }
	}

	@FXML
	public void chkBxHeader3IterationIdFilterOnChange() {
		// if(!isTestFilterEditMode()){
		ref_txtHeader3Value.setText(getConcatHeader3Value());
		// }
	}

	@FXML
	public void chkBxHeader3CustomAllowedOnChange() {

		if (ref_chkBxHeader3CustomAllowed.isSelected()) {

			ref_txtHeader3Value.setDisable(false);
		} else {
			ref_txtHeader3Value.setDisable(true);
		}
	}

	@FXML
	public void chkBx_IterationIdHeaderPrefixOnChange() {
		if (ref_chkBx_IterationIdHeaderPrefix.isSelected()) {

			ref_txt_IterationIdHeaderPrefix.setDisable(false);
			// chkBxHeader3IterationIdFilterOnChange();
		} else {
			ref_txt_IterationIdHeaderPrefix.setDisable(true);
		}
		// chkBxHeader3IterationIdFilterOnChange();
	}

	@FXML
	public void inputProcessAddClick() {
		ApplicationLauncher.logger.debug("inputProcessAddClick: Entry");

		String newValueToBeAdded = ref_cmbBxOperationCriteriaInputData.getSelectionModel().getSelectedItem().toString();
		ApplicationLauncher.logger.debug("inputProcessAddClick: value: " + newValueToBeAdded);
		HashSet<String> exisingTableViewDataList = new HashSet<String>();
		ref_listViewInputProcessList.getItems().forEach(k -> exisingTableViewDataList.add(k.toString()));
		// exisingTableViewDataList.add(newValueToBeAdded);
		// List<String> sortedDataList = new
		// ArrayList<String>(exisingTableViewDataList);
		// Collections.sort(sortedDataList);
		// ref_listViewInputProcessList.getItems().clear();
		// ref_listViewInputProcessList.getItems().setAll(exisingTableViewDataList);
		if (!exisingTableViewDataList.contains(newValueToBeAdded)) {
			ref_listViewInputProcessList.getItems().add(newValueToBeAdded);
		}
		ref_listViewInputProcessList.refresh();

	}

	@FXML
	public void inputProcessRemoveClick() {
		ApplicationLauncher.logger.debug("inputProcessRemoveClick: Entry");
		String valueToBeRemoved = ref_cmbBxOperationCriteriaInputData.getSelectionModel().getSelectedItem().toString();
		String userSelectedValueInTable = ref_listViewInputProcessList.getSelectionModel().getSelectedItem();
		if (userSelectedValueInTable != null) {
			valueToBeRemoved = userSelectedValueInTable;
		}
		ApplicationLauncher.logger.debug("inputProcessAddClick: userSelectedValueInTable: " + userSelectedValueInTable);
		ApplicationLauncher.logger.debug("inputProcessAddClick: valueToBeRemoved: " + valueToBeRemoved);
		HashSet<String> exisingTableViewDataList = new HashSet<String>();
		ref_listViewInputProcessList.getItems().forEach(k -> exisingTableViewDataList.add(k.toString()));
		if (exisingTableViewDataList.size() > 0) {
			if (exisingTableViewDataList.contains(valueToBeRemoved)) {
				ApplicationLauncher.logger.debug("inputProcessAddClick: removing value: " + valueToBeRemoved);
				exisingTableViewDataList.remove(valueToBeRemoved);
				List<String> sortedDataList = new ArrayList<String>(exisingTableViewDataList);
				Collections.sort(sortedDataList);
				ref_listViewInputProcessList.getItems().clear();
				ref_listViewInputProcessList.getItems().setAll(sortedDataList);
				ref_listViewInputProcessList.refresh();
			}
		}

	}

	/*
	 * @FXML
	 * public void operationProcessAddClick(){
	 * ApplicationLauncher.logger.debug("operationProcessAddClick: Entry");
	 * 
	 * 
	 * }
	 * 
	 * 
	 * @FXML
	 * public void operationProcessRemoveClick(){
	 * ApplicationLauncher.logger.debug("operationProcessRemoveClick: Entry");
	 * 
	 * 
	 * }
	 */

	/*
	 * @FXML
	 * public void outputProcessAddClick(){
	 * ApplicationLauncher.logger.debug("outputProcessAddClick: Entry");
	 * 
	 * 
	 * }
	 * 
	 * 
	 * @FXML
	 * public void outputProcessRemoveClick(){
	 * ApplicationLauncher.logger.debug("outputProcessRemoveClick: Entry");
	 * 
	 * 
	 * }
	 * 
	 * 
	 * @FXML
	 * public void chkBxPopulateVerticalOnChange(){
	 * ApplicationLauncher.logger.debug("chkBxPopulateVerticalOnChange: Entry");
	 * 
	 * 
	 * }
	 */

	@FXML
	public void chkBxReplicateDataOnChange() {
		ApplicationLauncher.logger.debug("chkBxReplicateDataOnChange: Entry");

		if (ref_chkBxReplicateData.isSelected()) {
			ref_cmbBxReplicateCountUserEntry.setDisable(false);
		} else {
			ref_cmbBxReplicateCountUserEntry.setDisable(true);
		}

	}

	/*
	 * @FXML
	 * public void chkBxOperationInputOnChange(){
	 * ApplicationLauncher.logger.debug("chkBxOperationInputOnChange: Entry");
	 * if(ref_chkBxOperationInput.isSelected()){
	 * ref_cmbBxOperationInputUserEntry.setDisable(false);
	 * }else{
	 * ref_cmbBxOperationInputUserEntry.setDisable(true);
	 * }
	 * 
	 * }
	 */

	/*
	 * @FXML
	 * public void chkBxOperationOutputOnChange(){
	 * ApplicationLauncher.logger.debug("chkBxOperationOutputOnChange: Entry");
	 * 
	 * if(ref_chkBxOperationOutput.isSelected()){
	 * ref_cmbBxOperationOutputUserEntry.setDisable(false);
	 * }else{
	 * ref_cmbBxOperationOutputUserEntry.setDisable(true);
	 * }
	 * 
	 * }
	 */

	@FXML
	public void chkBxPopulateHeader1OnChange() {
		ApplicationLauncher.logger.debug("chkBxPopulateHeader1OnChange: Entry");

		if (ref_chkBxPopulateHeader1.isSelected()) {
			ref_txtHeader1Value.setDisable(false);
		} else {
			ref_txtHeader1Value.setDisable(true);
			ref_txtHeader1Value.setText("");
		}

	}

	@FXML
	public void chkBxPopulateHeader2OnChange() {
		ApplicationLauncher.logger.debug("chkBxPopulateHeader2OnChange: Entry");
		if (ref_chkBxPopulateHeader2.isSelected()) {
			ref_txtHeader2Value.setDisable(false);
		} else {
			ref_txtHeader2Value.setDisable(true);
			ref_txtHeader2Value.setText("");
		}

	}

	@FXML
	public void chkBxPopulateHeader4OnChange() {
		ApplicationLauncher.logger.debug("chkBxPopulateHeader4OnChange: Entry");

		if (ref_chkBxPopulateHeader4.isSelected()) {
			ref_txtHeader4Value.setDisable(false);
		} else {
			ref_txtHeader4Value.setDisable(true);
			ref_txtHeader4Value.setText("");
		}

	}

	@FXML
	public void chkBxPopulateHeader5OnChange() {
		ApplicationLauncher.logger.debug("chkBxPopulateHeader5OnChange: Entry");

		if (ref_chkBxPopulateHeader5.isSelected()) {
			ref_txtHeader5Value.setDisable(false);
		} else {
			ref_txtHeader5Value.setDisable(true);
			ref_txtHeader5Value.setText("");
		}

	}

	@FXML
	public void cmbBxReplicateCountUserEntryOnChange() {
		ApplicationLauncher.logger.debug("cmbBxReplicateCountUserEntryOnChange: Entry");

	}

	@FXML
	public void cmbBxOperationInputUserEntryOnChange() {
		ApplicationLauncher.logger.debug("cmbBxOperationInputUserEntryOnChange: Entry");

	}

	/*
	 * @FXML
	 * public void cmbBxOperationOutputUserEntryOnChange(){
	 * ApplicationLauncher.logger.
	 * debug("cmbBxOperationOutputUserEntryOnChange: Entry");
	 * 
	 * 
	 * }
	 */

	@FXML
	public void cmbBxBaseTemplateOnChange() {
		ApplicationLauncher.logger.debug("cmbBxBaseTemplateOnChange: Entry");

		String selectedValue = ref_cmbBxBaseTemplate.getSelectionModel().getSelectedItem().toString();
		if (selectedValue.equals(BASE_TEMPLATE1)) {
			ref_cmbBxMeterMetaDataPopulateType.getSelectionModel().select(ConstantReport.REPORT_DATA_POPULATE_VERTICAL);
			ref_cmbBxTestFilterDataPopulateType.getSelectionModel()
					.select(ConstantReport.REPORT_DATA_POPULATE_VERTICAL);
		} else if (selectedValue.equals(BASE_TEMPLATE2)) {
			ref_cmbBxMeterMetaDataPopulateType.getSelectionModel()
					.select(ConstantReport.REPORT_DATA_POPULATE_HORIZONTAL);
			ref_cmbBxTestFilterDataPopulateType.getSelectionModel()
					.select(ConstantReport.REPORT_DATA_POPULATE_HORIZONTAL);

		} else if (selectedValue.equals(BASE_TEMPLATE3)) {
			ref_cmbBxMeterMetaDataPopulateType.getSelectionModel().select(ConstantReport.REPORT_DATA_POPULATE_VERTICAL);
			ref_cmbBxTestFilterDataPopulateType.getSelectionModel()
					.select(ConstantReport.REPORT_DATA_POPULATE_HORIZONTAL);

		}

	}

	@FXML
	public void btnPopulateDataNextOnClick() {
		ApplicationLauncher.logger.debug("btnPopulateDataNextOnClick: Entry");

		// titledPanePopulateData.setExpanded(false);
		ref_titledPaneTestTypeData.setExpanded(true);

	}

	/*
	 * public static float = 120.0F;
	 * public static float = 400.0F;
	 * public static float = 0.0F;
	 * public static float = 1.0F;
	 * public static float TP_USER_ENTRY_PF_ALLOWED_MIN = 0.0F;
	 * 
	 * public static float = 10.0F;
	 * public static float TP_USER_ENTRY_ACTIVE_ENERGY_KWH_ALLOWED_MIN_ABOVE = 0.0F;
	 * public static float TP_USER_ENTRY_REACTIVE_ENERGY_KVARH_ALLOWED_MAX = 15.0F;
	 * public static float TP_USER_ENTRY_REACTIVE_ENERGY_KVARH_ALLOWED_MIN_ABOVE =
	 * 0.0F;
	 * public static float TP_USER_ENTRY_APPARENT_ENERGY_KVAH_ALLOWED_MAX = 12.0F;
	 * public static float TP_USER_ENTRY_APPARENT_ENERGY_KVAH_ALLOWED_MIN_ABOVE =
	 * 0.0F;
	 * 
	 * public static int = 30;
	 * public static int TP_USER_ENTRY_ITERATION_ALLOWED_MIN = 1;
	 */

	public boolean validateUserEntryDataInRange() {
		ApplicationLauncher.logger.debug("validateUserEntryDataInRange: Entry");

		float userEntryData = 0.0f;
		float maxAllowed = 0.0f;
		float minAllowed = 0.0f;
		float minAllowedAbove = 0.0f;

		try {

			if (ref_rdBtnOperationOutput.isSelected()) {
				if (ref_chkBxPostOperationActive.isSelected()) {

					String selectedPostOperation = ref_cmbBxPostOperationMethod.getSelectionModel().getSelectedItem()
							.toString();
					if (selectedPostOperation.equals(ConstantReportV2.POST_OPERATION_METHOD_DIVIDE)) {
						String userInputPostProcessingValue = ref_txtPostOperationValue.getText();
						if (Float.parseFloat(userInputPostProcessingValue) == 0.0f) {
							ApplicationLauncher.logger.info("validateUserEntryDataInRange : ERROR_CODE_6047: "
									+ ErrorCodeMapping.ERROR_CODE_6047 + " - prompted!");
							WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6047,
									ErrorCodeMapping.ERROR_CODE_6047_MSG, AlertType.ERROR);
							return false;
						}

					}

				}
			}

			if (!ref_txtVoltPercentFilterUserEntry.isDisabled()) {
				if (!ref_txtVoltPercentFilterUserEntry.getText().isEmpty()) {
					userEntryData = NumberUtils.toFloat(ref_txtVoltPercentFilterUserEntry.getText());
					maxAllowed = ConstantAppConfig.TP_USER_ENTRY_VOLT_PERCENT_ALLOWED_MAX;
					minAllowed = ConstantAppConfig.TP_USER_ENTRY_VOLT_PERCENT_ALLOWED_MIN;

					if ((userEntryData > maxAllowed) || (userEntryData < minAllowed)) {

						ApplicationLauncher.logger.info("validateUserEntryDataInRange : ERROR_CODE_6032: "
								+ ErrorCodeMapping.ERROR_CODE_6032_MSG + "\n\nMax Allowed: " + maxAllowed
								+ "\nMin Allowed: " + minAllowed + " - prompted!");
						WindowManager.InformUser(
								ErrorCodeMapping.ERROR_CODE_6032, ErrorCodeMapping.ERROR_CODE_6032_MSG
										+ "\n\nMax Allowed: " + maxAllowed + "\nMin Allowed: " + minAllowed,
								AlertType.ERROR);
						return false;
					}
				} else {
					ApplicationLauncher.logger.info("validateUserEntryDataInRange : ERROR_CODE_6033: "
							+ ErrorCodeMapping.ERROR_CODE_6033_MSG + " - prompted!");
					WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6033, ErrorCodeMapping.ERROR_CODE_6033_MSG,
							AlertType.ERROR);
					return false;
				}
			}

			if (!ref_txtCurrentPercentFilterUserEntry.isDisabled()) {
				if (!ref_txtCurrentPercentFilterUserEntry.getText().isEmpty()) {
					userEntryData = NumberUtils.toFloat(ref_txtCurrentPercentFilterUserEntry.getText());
					String selectedCurrentParam = ref_cmbBxCurrentPercentFilterUserEntry.getSelectionModel()
							.getSelectedItem().toString();
					if (selectedCurrentParam.equals(ConstantReport.EXTENSION_TYPE_CURRENT_IB)) {
						maxAllowed = ConstantAppConfig.TP_USER_ENTRY_CURRENT_Ib_PERCENT_ALLOWED_MAX
								/ ConstantApp.CURRENT_PERCENT_CONVERTION_FACTOR;
					} else if (selectedCurrentParam.equals(ConstantReport.EXTENSION_TYPE_CURRENT_IMAX)) {
						maxAllowed = ConstantAppConfig.TP_USER_ENTRY_CURRENT_IMAX_PERCENT_ALLOWED_MAX
								/ ConstantApp.CURRENT_PERCENT_CONVERTION_FACTOR;
					} else {
						// maxAllowed = ConstantAppConfig.TP_USER_ENTRY_CURRENT_Ib_PERCENT_ALLOWED_MAX;
					}
					minAllowedAbove = ConstantAppConfig.TP_USER_ENTRY_CURRENT_PERCENT_ALLOWED_MIN_ABOVE / 100;

					if ((userEntryData > maxAllowed) || (userEntryData <= minAllowedAbove)) {
						if (selectedCurrentParam.equals(ConstantReport.EXTENSION_TYPE_CURRENT_IB)) {
							ApplicationLauncher.logger.info("validateUserEntryDataInRange : ERROR_CODE_6034: "
									+ ErrorCodeMapping.ERROR_CODE_6034_MSG + "\n\nMax allowed: " + maxAllowed
									+ "\nMin allowed above: " + minAllowedAbove + " - prompted!");
							WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6034,
									ErrorCodeMapping.ERROR_CODE_6034_MSG + "\n\nMax allowed: " + maxAllowed
											+ "\nMin allowed above: " + minAllowedAbove,
									AlertType.ERROR);
						} else if (selectedCurrentParam.equals(ConstantReport.EXTENSION_TYPE_CURRENT_IMAX)) {
							ApplicationLauncher.logger.info("validateUserEntryDataInRange : ERROR_CODE_6035: "
									+ ErrorCodeMapping.ERROR_CODE_6035_MSG + "\n\nMax allowed: " + maxAllowed
									+ "\nMin allowed above: " + minAllowedAbove + " - prompted!");
							WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6035,
									ErrorCodeMapping.ERROR_CODE_6035_MSG + "\n\nMax allowed: " + maxAllowed
											+ "\nMin allowed above: " + minAllowedAbove,
									AlertType.ERROR);

						}
						return false;
					}
				} else {
					ApplicationLauncher.logger.info("validateUserEntryDataInRange : ERROR_CODE_6036: "
							+ ErrorCodeMapping.ERROR_CODE_6036_MSG + " - prompted!");
					WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6036, ErrorCodeMapping.ERROR_CODE_6036_MSG,
							AlertType.ERROR);
					return false;

				}
			}

			if (!ref_txtPfFilterUserEntry.isDisabled()) {
				if (!ref_txtPfFilterUserEntry.getText().isEmpty()) {
					userEntryData = NumberUtils.toFloat(ref_txtPfFilterUserEntry.getText());
					maxAllowed = ConstantAppConfig.TP_USER_ENTRY_PF_ALLOWED_MAX;
					minAllowed = ConstantAppConfig.TP_USER_ENTRY_PF_ALLOWED_MIN;

					if ((userEntryData > maxAllowed) || (userEntryData < minAllowed)) {

						ApplicationLauncher.logger.info("validateUserEntryDataInRange : ERROR_CODE_6037: "
								+ ErrorCodeMapping.ERROR_CODE_6037_MSG + "\n\nMax Allowed: " + maxAllowed
								+ "\nMin Allowed: " + minAllowed + " - prompted!");
						WindowManager.InformUser(
								ErrorCodeMapping.ERROR_CODE_6037, ErrorCodeMapping.ERROR_CODE_6037_MSG
										+ "\n\nMax Allowed: " + maxAllowed + "\nMin Allowed: " + minAllowed,
								AlertType.ERROR);
						return false;
					}
					/*
					 * String selectedPfType =
					 * ref_cmbBxPfFilterUserEntry.getSelectionModel().getSelectedItem().toString();
					 * if((selectedPfType.equals(ConstantApp.PF_UPF)) && (userEntryData == 1.0f) ){
					 * 
					 * }
					 */
				} else {
					ApplicationLauncher.logger.info("validateUserEntryDataInRange : ERROR_CODE_6038: "
							+ ErrorCodeMapping.ERROR_CODE_6038_MSG + " - prompted!");
					WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6038, ErrorCodeMapping.ERROR_CODE_6038_MSG,
							AlertType.ERROR);
					return false;
				}
			}

			if (!ref_txtFreqFilterUserEntry.isDisabled()) {
				if (!ref_txtFreqFilterUserEntry.getText().isEmpty()) {
					userEntryData = NumberUtils.toFloat(ref_txtFreqFilterUserEntry.getText());
					maxAllowed = ConstantAppConfig.FREQUENCY_MAX;
					minAllowed = ConstantAppConfig.FREQUENCY_MIN;

					if ((userEntryData > maxAllowed) || (userEntryData < minAllowed)) {

						ApplicationLauncher.logger.info("validateUserEntryDataInRange : ERROR_CODE_6039: "
								+ ErrorCodeMapping.ERROR_CODE_6039_MSG + "\n\nMax Allowed: " + maxAllowed
								+ "\nMin Allowed: " + minAllowed + " - prompted!");
						WindowManager.InformUser(
								ErrorCodeMapping.ERROR_CODE_6039, ErrorCodeMapping.ERROR_CODE_6039_MSG
										+ "\n\nMax Allowed: " + maxAllowed + "\nMin Allowed: " + minAllowed,
								AlertType.ERROR);
						return false;
					}
				} else {
					ApplicationLauncher.logger.info("validateUserEntryDataInRange : ERROR_CODE_6040: "
							+ ErrorCodeMapping.ERROR_CODE_6040_MSG + " - prompted!");
					WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6040, ErrorCodeMapping.ERROR_CODE_6040_MSG,
							AlertType.ERROR);
					return false;
				}
			}

			if (!ref_txtIterationReadingStartingId.isDisabled()) {
				if (!ref_txtIterationReadingStartingId.getText().isEmpty()) {
					userEntryData = NumberUtils.toFloat(ref_txtIterationReadingStartingId.getText());
					maxAllowed = ConstantAppConfig.TP_USER_ENTRY_ITERATION_ALLOWED_MAX;
					minAllowed = ConstantAppConfig.TP_USER_ENTRY_ITERATION_ALLOWED_MIN;

					if ((userEntryData > maxAllowed) || (userEntryData < minAllowed)) {

						ApplicationLauncher.logger.info("validateUserEntryDataInRange : ERROR_CODE_6041: "
								+ ErrorCodeMapping.ERROR_CODE_6041_MSG + "\n\nMax Allowed: " + maxAllowed
								+ "\nMin Allowed: " + minAllowed + " - prompted!");
						WindowManager.InformUser(
								ErrorCodeMapping.ERROR_CODE_6041, ErrorCodeMapping.ERROR_CODE_6041_MSG
										+ "\n\nMax Allowed: " + maxAllowed + "\nMin Allowed: " + minAllowed,
								AlertType.ERROR);
						return false;
					}
				} else {
					ApplicationLauncher.logger.info("validateUserEntryDataInRange : ERROR_CODE_6042: "
							+ ErrorCodeMapping.ERROR_CODE_6042_MSG + " - prompted!");
					WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6042, ErrorCodeMapping.ERROR_CODE_6042_MSG,
							AlertType.ERROR);
					return false;
				}
			}

			if (!ref_txtIterationReadingEndingId.isDisabled()) {
				if (!ref_txtIterationReadingEndingId.getText().isEmpty()) {
					userEntryData = NumberUtils.toFloat(ref_txtIterationReadingEndingId.getText());
					maxAllowed = ConstantAppConfig.TP_USER_ENTRY_ITERATION_ALLOWED_MAX;
					minAllowed = ConstantAppConfig.TP_USER_ENTRY_ITERATION_ALLOWED_MIN;

					if ((userEntryData > maxAllowed) || (userEntryData < minAllowed)) {

						ApplicationLauncher.logger.info("validateUserEntryDataInRange : ERROR_CODE_6048: "
								+ ErrorCodeMapping.ERROR_CODE_6048_MSG + "\n\nMax Allowed: " + maxAllowed
								+ "\nMin Allowed: " + minAllowed + " - prompted!");
						WindowManager.InformUser(
								ErrorCodeMapping.ERROR_CODE_6048, ErrorCodeMapping.ERROR_CODE_6048_MSG
										+ "\n\nMax Allowed: " + maxAllowed + "\nMin Allowed: " + minAllowed,
								AlertType.ERROR);
						return false;
					}
				} /*
					 * else{ilu
					 * ApplicationLauncher.logger.
					 * info("validateUserEntryDataInRange : ERROR_CODE_6042: "+
					 * ErrorCodeMapping.ERROR_CODE_6042_MSG +" - prompted!");
					 * WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6042,
					 * ErrorCodeMapping.ERROR_CODE_6042_MSG ,AlertType.ERROR);
					 * return false;
					 * }
					 */
			}

			if (!ref_txtIterationReadingEndingId.isDisabled()) {
				if (!ref_txtIterationReadingEndingId.getText().isEmpty()) {
					float userEntryRepeatStartingId = NumberUtils.toFloat(ref_txtIterationReadingStartingId.getText());
					float userEntryRepeatEndingId = NumberUtils.toFloat(ref_txtIterationReadingEndingId.getText());
					if (userEntryRepeatEndingId != 0.0f) {
						if (userEntryRepeatStartingId > userEntryRepeatEndingId) {
							ApplicationLauncher.logger.info("validateUserEntryDataInRange : ERROR_CODE_6049: "
									+ ErrorCodeMapping.ERROR_CODE_6049_MSG + " - prompted!");
							WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6049,
									ErrorCodeMapping.ERROR_CODE_6049_MSG, AlertType.ERROR);
							return false;
						}
					}
				}

			}

			if (!ref_txtEnergyFilterUserEntry.isDisabled()) {
				if (!ref_txtEnergyFilterUserEntry.getText().isEmpty()) {
					userEntryData = NumberUtils.toFloat(ref_txtEnergyFilterUserEntry.getText());
					String selectedCurrentParam = ref_cmbBxEnergyFilterUserEntry.getSelectionModel().getSelectedItem()
							.toString();
					if (selectedCurrentParam.equals(ConstantReport.EXTENSION_TYPE_ENERGY_ACTIVE)) {
						maxAllowed = ConstantAppConfig.TP_USER_ENTRY_ACTIVE_ENERGY_KWH_ALLOWED_MAX;
						minAllowedAbove = ConstantAppConfig.TP_USER_ENTRY_ACTIVE_ENERGY_KWH_ALLOWED_MIN_ABOVE;
					} else if (selectedCurrentParam.equals(ConstantReport.EXTENSION_TYPE_ENERGY_REACTIVE)) {
						maxAllowed = ConstantAppConfig.TP_USER_ENTRY_REACTIVE_ENERGY_KVARH_ALLOWED_MAX;
						minAllowedAbove = ConstantAppConfig.TP_USER_ENTRY_REACTIVE_ENERGY_KVARH_ALLOWED_MIN_ABOVE;
					} else if (selectedCurrentParam.equals(ConstantReport.EXTENSION_TYPE_ENERGY_APPARENT)) {
						maxAllowed = ConstantAppConfig.TP_USER_ENTRY_APPARENT_ENERGY_KVAH_ALLOWED_MAX;
						minAllowedAbove = ConstantAppConfig.TP_USER_ENTRY_APPARENT_ENERGY_KVAH_ALLOWED_MIN_ABOVE;
					}

					if ((userEntryData > maxAllowed) || (userEntryData <= minAllowedAbove)) {
						if (selectedCurrentParam.equals(ConstantReport.EXTENSION_TYPE_ENERGY_ACTIVE)) {
							ApplicationLauncher.logger.info("validateUserEntryDataInRange : ERROR_CODE_6043: "
									+ ErrorCodeMapping.ERROR_CODE_6043_MSG + "\n\nMax Allowed: " + maxAllowed
									+ "\nMin allowed above: " + minAllowedAbove + " - prompted!");
							WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6043,
									ErrorCodeMapping.ERROR_CODE_6043_MSG + "\n\nMax allowed: " + maxAllowed
											+ "\nMin allowed above: " + minAllowedAbove,
									AlertType.ERROR);
						} else if (selectedCurrentParam.equals(ConstantReport.EXTENSION_TYPE_ENERGY_REACTIVE)) {
							ApplicationLauncher.logger.info("validateUserEntryDataInRange : ERROR_CODE_6044: "
									+ ErrorCodeMapping.ERROR_CODE_6044_MSG + "\n\nMax Allowed: " + maxAllowed
									+ "\nMin allowed above: " + minAllowedAbove + " - prompted!");
							WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6044,
									ErrorCodeMapping.ERROR_CODE_6044_MSG + "\n\nMax allowed: " + maxAllowed
											+ "\nMin allowed above: " + minAllowedAbove,
									AlertType.ERROR);

						} else if (selectedCurrentParam.equals(ConstantReport.EXTENSION_TYPE_ENERGY_APPARENT)) {
							ApplicationLauncher.logger.info("validateUserEntryDataInRange : ERROR_CODE_6045: "
									+ ErrorCodeMapping.ERROR_CODE_6045_MSG + "\n\nMax Allowed: " + maxAllowed
									+ "\nMin allowed above: " + minAllowedAbove + " - prompted!");
							WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6045,
									ErrorCodeMapping.ERROR_CODE_6045_MSG + "\n\nMax allowed: " + maxAllowed
											+ "\nMin allowed above: " + minAllowedAbove,
									AlertType.ERROR);

						}
						return false;
					}
				} else {
					ApplicationLauncher.logger.info("validateUserEntryDataInRange : ERROR_CODE_6046: "
							+ ErrorCodeMapping.ERROR_CODE_6046_MSG + " - prompted!");
					WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6046, ErrorCodeMapping.ERROR_CODE_6046_MSG,
							AlertType.ERROR);
					return false;

				}
			}

		} catch (Exception E) {

			ApplicationLauncher.logger.error("validateUserEntryDataInRange: Exception: " + E.getMessage());
			return false;
		}

		return true;
	}

	@FXML
	public void btnTestTypeDataNextOnClick() {
		ApplicationLauncher.logger.debug("btnTestTypeDataNextOnClick: Entry");

		// cellPositionTitledPaneDisplayDataProcess();
		// String voltPercentFilterUserEntry =
		// ref_txtVoltPercentFilterUserEntry.getText();

		try {
			String selectedTestTypeKey = ref_cmbBxTestType.getSelectionModel().getSelectedItem().toString();
			if (selectedTestTypeKey.equals(ConstantApp.DISPLAY_TC_TITLE_CUSTOM_TEST)) {
				if (ref_txtCustomTestNameData.getText().isEmpty()) {
					ApplicationLauncher.logger.info("btnTestTypeDataNextOnClick : ERROR_CODE_6024: "
							+ ErrorCodeMapping.ERROR_CODE_6024_MSG + " - prompted!");
					WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6024, ErrorCodeMapping.ERROR_CODE_6024_MSG,
							AlertType.ERROR);
					return;
				}

			}

			boolean status = validateUserEntryDataInRange();

			if (!status) {
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("btnTestTypeDataNextOnClick: Exception: " + e.getMessage());
		}

		cellPositionTitledPaneDisplayDataProcess();

		if (ref_rdBtnOperationOutput.isSelected()) {
			ref_titledPaneOperation.setExpanded(true);
		} else {
			ref_titledPaneCellPosition.setExpanded(true);
		}

	}

	public void cellPositionTitledPaneDisplayDataProcess() {
		ApplicationLauncher.logger.debug("cellPositionTitledPaneDisplayDataProcess: Entry");

		ArrayList<String> cellStartHeaderList = new ArrayList<String>();
		// Set<String> cellStartHeaderList = new HashSet<String>();
		ArrayList<String> cellStartPositionCellList = new ArrayList<String>();

		ArrayList<String> cellHeaderPositionHeaderList = new ArrayList<String>();
		ArrayList<String> cellHeaderPositionCellList = new ArrayList<String>();
		/*
		 * if(ref_rdBtnOperationOutput.isSelected()){
		 * ref_titledPaneOperation.setExpanded(true);
		 * }else{
		 * ref_titledPaneCellPosition.setExpanded(true);
		 * }
		 */

		getTestFilterDataCellPositionHashMap().clear();

		ArrayList<String> executionResultTypeHeadersOnlyList = new ArrayList<String>();// (Arrays.asList(ConstantReportV2.RESULT_DATA_TYPE_DISPLAY_REFSTD_INITIAL_REGISTER,
		// ConstantReportV2.RESULT_DATA_TYPE_DISPLAY_REFSTD_FINAL_REGISTER));

		/*
		 * ArrayList<String> localOutputHeadersOnlyList = (ArrayList<String>)
		 * getOperationProcessDataModel().getOperationLocalOutput().stream()
		 * .filter(e -> e.isPopulateOnlyHeaders() == true)
		 * .map(e -> e.getOperationProcessKey())
		 * .collect(Collectors.toList());
		 * 
		 * ArrayList<String> masterOutputHeadersOnlyList = (ArrayList<String>)
		 * getOperationProcessDataModel().getOperationMasterOutput().stream()
		 * .filter(e -> e.isPopulateOnlyHeaders() == true)
		 * .map(e -> e.getOperationProcessKey())
		 * .collect(Collectors.toList());
		 */

		ArrayList<String> localOutputHeadersOnlyList = (ArrayList<String>) getOperationParameterProfileDataList()
				.stream()
				.filter(e -> e.getParamType().equals(ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_OUTPUT))
				.filter(e -> e.isPopulateOnlyHeaders() == true)
				.map(e -> e.getKeyParam())
				.collect(Collectors.toList());
		// .filter( e - > e)
		// .filter(e -> e.getPopulateOnlyHeaders() == true)
		// .map(e -> e.getOperationProcessKey())
		// .collect(Collectors.toList());

		ArrayList<String> masterOutputHeadersOnlyList = (ArrayList<String>) getOperationParameterProfileDataList()
				.stream()
				.filter(e -> e.getParamType().equals(ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_MASTER_OUTPUT))
				.filter(e -> e.isPopulateOnlyHeaders() == true)
				.map(e -> e.getKeyParam())
				.collect(Collectors.toList());

		if (localOutputHeadersOnlyList.size() > 0) {
			executionResultTypeHeadersOnlyList.addAll(localOutputHeadersOnlyList);
		}

		if (masterOutputHeadersOnlyList.size() > 0) {
			executionResultTypeHeadersOnlyList.addAll(masterOutputHeadersOnlyList);
		}
		// getMeterMetaDataList().clear();cvbgv

		String filterUserEntry = ref_txtVoltPercentFilterUserEntry.getText()
				+ ref_txtCurrentPercentFilterUserEntry.getText() +
				ref_txtPfFilterUserEntry.getText() + ref_txtFreqFilterUserEntry.getText()
				+ ref_txtEnergyFilterUserEntry.getText() +
				ref_txtIterationReadingStartingId.getText();

		if (ConstantReportV2.REPEAT_START_TO_END_FEATURE_ENABLED) {
			filterUserEntry = ref_txtVoltPercentFilterUserEntry.getText()
					+ ref_txtCurrentPercentFilterUserEntry.getText() +
					ref_txtPfFilterUserEntry.getText() + ref_txtFreqFilterUserEntry.getText()
					+ ref_txtEnergyFilterUserEntry.getText() +
					ConstantReportV2.TEST_POINT_FILTER_SEPERATOR + ref_txtIterationReadingStartingId.getText()
					+ ConstantReportV2.TEST_POINT_FILTER_SEPERATOR + ref_txtIterationReadingEndingId.getText();
			ApplicationLauncher.logger
					.debug("cellPositionTitledPaneDisplayDataProcess: filterUserEntry: " + filterUserEntry);
		}

		String selectedTestType = ref_cmbBxTestType.getSelectionModel().getSelectedItem().toString();

		String selectedExecutionResultType = ref_cmbBxExecutionResultType.getSelectionModel().getSelectedItem()
				.toString();

		// String customTestNameData = ref_txtCustomTestNameData.getText();
		if (!filterUserEntry.isEmpty()) {
			ApplicationLauncher.logger.debug("cellPositionTitledPaneDisplayDataProcess: Result value hit1");
			if (!executionResultTypeHeadersOnlyList.contains(selectedExecutionResultType)) {
				if (selectedTestType.equals(ConstantApp.DISPLAY_TC_TITLE_REPEATABLITY)) {
					String startIdStr = ref_txtIterationReadingStartingId.getText();
					String endIdStr = ref_txtIterationReadingEndingId.getText();
					int repeatStartingId = NumberUtils.toInt(startIdStr, 0);
					int repeatEndingId = NumberUtils.toInt(endIdStr, 0);

					if (repeatEndingId > repeatStartingId) {
						for (int repeatId = repeatStartingId; repeatId <= repeatEndingId; repeatId++) {
							cellStartHeaderList
									.add(ConstantReportV2.CELL_START_POSITION_HEADER_REPEAT_RESULT_DATA_PREFIX_KEY
											+ String.valueOf(repeatId));
							cellStartPositionCellList.add("");
						}
					} else {
						cellStartHeaderList
								.add(ConstantReportV2.CELL_START_POSITION_HEADER_REPEAT_RESULT_DATA_PREFIX_KEY
										+ String.valueOf(repeatStartingId));
						cellStartPositionCellList.add("");
					}

				} else if (selectedTestType.equals(ConstantApp.DISPLAY_TC_TITLE_SELF_HEATING)) {

					String startIdStr = ref_txtIterationReadingStartingId.getText();
					String endIdStr = ref_txtIterationReadingEndingId.getText();
					int repeatStartingId = NumberUtils.toInt(startIdStr, 0);
					int repeatEndingId = NumberUtils.toInt(endIdStr, 0);

					if (repeatEndingId > repeatStartingId) {
						for (int repeatId = repeatStartingId; repeatId <= repeatEndingId; repeatId++) {
							cellStartHeaderList
									.add(ConstantReportV2.CELL_START_POSITION_HEADER_SELF_HEAT_RESULT_DATA_PREFIX_KEY
											+ String.valueOf(repeatId));
							cellStartPositionCellList.add("");
						}
					} else {
						cellStartHeaderList
								.add(ConstantReportV2.CELL_START_POSITION_HEADER_SELF_HEAT_RESULT_DATA_PREFIX_KEY
										+ String.valueOf(repeatStartingId));
						cellStartPositionCellList.add("");
					}

				} else {
					if (ref_rdBtnOperationOutput.isSelected()) {
						if (!ref_chkBxPopulateLocalOutputData.isDisabled()) {
							// cellStartHeaderList.add(ConstantReportV2.CELL_START_POSITION_HEADER_RESULT_DATA_KEY);

							// commented in version 4.2.0.3.0.9
							// cellStartHeaderList.add(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_ERROR_VALUE);
							// cellStartPositionCellList.add("");
						}
					} else {
						// added logic in version s.4.2.3.0.4
						// cellStartHeaderList.add(ConstantReportV2.CELL_START_POSITION_HEADER_RESULT_DATA_KEY);//
						// Commented in version s.4.2.3.0.7
						// cellStartPositionCellList.add("");
					}

				}
			}
		} else if (selectedTestType.equals(ConstantApp.DISPLAY_TC_TITLE_CUSTOM_TEST)) {
			ApplicationLauncher.logger.debug("cellPositionTitledPaneDisplayDataProcess: Result value hit2");
			/*
			 * if(!executionResultTypeHeadersOnlyList.contains(selectedExecutionResultType))
			 * {
			 * //cellStartHeaderList.add(ConstantReportV2.
			 * CELL_START_POSITION_HEADER_RESULT_DATA_KEY);
			 * cellStartHeaderList.add(ConstantReportV2.RESULT_DATA_TYPE_DISPLAY_ERROR_VALUE
			 * );
			 * cellStartPositionCellList.add("");
			 * }
			 */

		}

		if (ref_chkBxPopulateDutCalcErrorPercentage.isSelected()) {

			cellStartHeaderList.add(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_ERROR_VALUE);
			cellStartPositionCellList.add("");

		}

		if (ref_chkBxPopulateDutErrorResultStatus.isSelected()) {
			if (!filterUserEntry.isEmpty()) {
				ApplicationLauncher.logger.debug("cellPositionTitledPaneDisplayDataProcess: Result Status hit3");
				cellStartHeaderList.add(ConstantReportV2.RESULT_SOURCE_TYPE_RESULT_STATUS_KEY);
				cellStartPositionCellList.add("");
			} else if (selectedTestType.equals(ConstantApp.DISPLAY_TC_TITLE_CUSTOM_TEST)) {

				ApplicationLauncher.logger.debug("cellPositionTitledPaneDisplayDataProcess: Result Status hit4");
				cellStartHeaderList.add(ConstantReportV2.RESULT_SOURCE_TYPE_RESULT_STATUS_KEY);
				cellStartPositionCellList.add("");
			}

		}

		if (ref_chkBxPopulateDutPulseCount.isSelected()) {

			cellStartHeaderList.add(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_PULSE_COUNT);
			cellStartPositionCellList.add("");

		}

		if (ref_chkBxPopulateDutInitial.isSelected()) {

			cellStartHeaderList.add(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_INITIAL_REGISTER);
			cellStartPositionCellList.add("");

		}

		if (ref_chkBxPopulateDutFinal.isSelected()) {

			cellStartHeaderList.add(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_FINAL_REGISTER);
			cellStartPositionCellList.add("");

		}

		if (ref_chkBxPopulateDutDifference.isSelected()) {

			cellStartHeaderList.add(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_DIFFERENCE);
			cellStartPositionCellList.add("");

		}

		if (ref_chkBxPopulateDutOnePulsePeriod.isSelected()) {

			cellStartHeaderList.add(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_ONE_PULSE_DURATION);
			cellStartPositionCellList.add("");

		}

		if (ref_chkBxPopulateDutAverageValue.isSelected()) {
			if (selectedTestType.equals(ConstantApp.DISPLAY_TC_TITLE_REPEATABLITY)) {
				cellStartHeaderList.add(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_REPEAT_AVERAGE_VALUE);
				cellStartPositionCellList.add("");
			} else if (selectedTestType.equals(ConstantApp.DISPLAY_TC_TITLE_SELF_HEATING)) {
				cellStartHeaderList.add(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_SELF_HEAT_AVERAGE_VALUE);
				cellStartPositionCellList.add("");
			} else {
				cellStartHeaderList.add(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_AVERAGE_VALUE);
				cellStartPositionCellList.add("");
			}

		}

		if (ref_chkBxPopulateDutAverageStatus.isSelected()) {
			if (selectedTestType.equals(ConstantApp.DISPLAY_TC_TITLE_REPEATABLITY)) {
				cellStartHeaderList.add(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_REPEAT_AVERAGE_STATUS);
				cellStartPositionCellList.add("");
			} else if (selectedTestType.equals(ConstantApp.DISPLAY_TC_TITLE_SELF_HEATING)) {
				cellStartHeaderList.add(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_SELF_HEAT_AVERAGE_STATUS);
				cellStartPositionCellList.add("");
			} else {
				cellStartHeaderList.add(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_AVERAGE_STATUS);
				cellStartPositionCellList.add("");
			}

		}

		if (ref_chkBxReplicateData.isSelected()) {

			// getCellStartPositionHeader().stream().forEach(k->replicateHeader.add(k));
			// getCellStartPositionCell().stream().forEach(k->replicateCell.add(k));

			/*
			 * cellStartHeaderList.add(CELL_START_POSITION_HEADER_RESULT_DATA_KEY);
			 * cellStartPositionCellList.add("");
			 * 
			 * if(ref_chkBxErrorResultStatus.isSelected()){
			 * cellStartHeaderList.add(CELL_START_POSITION_HEADER_RESULT_STATUS_KEY);
			 * cellStartPositionCellList.add("");
			 * 
			 * }
			 */

			int replicateSelectedCount = Integer
					.parseInt(ref_cmbBxReplicateCountUserEntry.getSelectionModel().getSelectedItem().toString());
			for (int i = 0; i < replicateSelectedCount; i++) {
				cellStartHeaderList.add(cellStartPositionReplicateHeader.get(i));
				cellStartPositionCellList.add(cellStartPositionReplicateCell.get(i));
			}

			// loadTable(ref_tvCellStartPosition, cellStartHeaderList,
			// cellStartPositionCellList,
			// cellStartPositionPageCellValues);

		}

		String headerKey = "";

		if (selectedTestType.equals(ConstantApp.DISPLAY_TC_TITLE_REPEATABLITY)) {
			String startIdStr = ref_txtIterationReadingStartingId.getText();
			String endIdStr = ref_txtIterationReadingEndingId.getText();
			int repeatStartingId = NumberUtils.toInt(startIdStr, 0);
			int repeatEndingId = NumberUtils.toInt(endIdStr, 0);
			if (ref_chkBx_IterationIdHeaderPrefix.isSelected()) {
				if (repeatEndingId > repeatStartingId) {
					for (int repeatId = repeatStartingId; repeatId <= repeatEndingId; repeatId++) {
						cellHeaderPositionHeaderList
								.add(ConstantReportV2.CELL_HEADER_POSITION_HEADER_REPEAT_RESULT_DATA_PREFIX_KEY
										+ String.valueOf(repeatId));
						cellHeaderPositionCellList.add("");
					}
				} else {
					cellHeaderPositionHeaderList
							.add(ConstantReportV2.CELL_HEADER_POSITION_HEADER_REPEAT_RESULT_DATA_PREFIX_KEY
									+ String.valueOf(repeatStartingId));
					cellHeaderPositionCellList.add("");
				}
			}

		} else if (selectedTestType.equals(ConstantApp.DISPLAY_TC_TITLE_SELF_HEATING)) {

			String startIdStr = ref_txtIterationReadingStartingId.getText();
			String endIdStr = ref_txtIterationReadingEndingId.getText();
			int repeatStartingId = NumberUtils.toInt(startIdStr, 0);
			int repeatEndingId = NumberUtils.toInt(endIdStr, 0);

			if (repeatEndingId > repeatStartingId) {
				for (int repeatId = repeatStartingId; repeatId <= repeatEndingId; repeatId++) {
					cellHeaderPositionHeaderList
							.add(ConstantReportV2.CELL_HEADER_POSITION_HEADER_SELF_HEAT_RESULT_DATA_PREFIX_KEY
									+ String.valueOf(repeatId));
					cellHeaderPositionCellList.add("");
				}
			} else {
				cellHeaderPositionHeaderList
						.add(ConstantReportV2.CELL_HEADER_POSITION_HEADER_SELF_HEAT_RESULT_DATA_PREFIX_KEY
								+ String.valueOf(repeatStartingId));
				cellHeaderPositionCellList.add("");
			}

		}

		if (executionResultTypeHeadersOnlyList.contains(selectedExecutionResultType)) {

			/*
			 * if(selectedExecutionResultType.equals(ConstantReportV2.
			 * RESULT_DATA_TYPE_DISPLAY_REFSTD_INITIAL_REGISTER)){
			 * cellHeaderPositionHeaderList.add(ConstantReportV2.
			 * CELL_HEADER_POSITION_HEADER_RESULT_RSM_INITIAL);
			 * cellHeaderPositionCellList.add("");
			 * }else if(selectedExecutionResultType.equals(ConstantReportV2.
			 * RESULT_DATA_TYPE_DISPLAY_REFSTD_FINAL_REGISTER)){
			 * cellHeaderPositionHeaderList.add(ConstantReportV2.
			 * CELL_HEADER_POSITION_HEADER_RESULT_RSM_FINAL);
			 * cellHeaderPositionCellList.add("");
			 * }
			 */
		}

		if (ref_chkBxPopulateHeader1.isSelected()) {
			String header1Value = ref_txtHeader1Value.getText();

			if (!header1Value.isEmpty()) {

				cellHeaderPositionHeaderList.add(ConstantReportV2.POPULATE_HEADER1_KEY);
				cellHeaderPositionCellList.add("");

			}
		}

		if (ref_chkBxPopulateHeader2.isSelected()) {
			String header2Value = ref_txtHeader2Value.getText();
			if (!header2Value.isEmpty()) {

				cellHeaderPositionHeaderList.add(ConstantReportV2.POPULATE_HEADER2_KEY);
				cellHeaderPositionCellList.add("");

			}
		}

		if (ref_chkBxPopulateHeader3.isSelected()) {
			String header3Value = ref_txtHeader3Value.getText();
			if (!header3Value.isEmpty()) {

				cellHeaderPositionHeaderList.add(ConstantReportV2.POPULATE_HEADER3_KEY);
				cellHeaderPositionCellList.add("");

			}
		}

		if (ref_chkBxPopulateHeader4.isSelected()) {
			String header4Value = ref_txtHeader4Value.getText();// " ";//ref_txtHeader3Value.getText();
			if (!header4Value.isEmpty()) {

				cellHeaderPositionHeaderList.add(ConstantReportV2.POPULATE_HEADER4_KEY);
				cellHeaderPositionCellList.add("");

			}
		}

		if (ref_chkBxPopulateHeader5.isSelected()) {
			String header5Value = ref_txtHeader5Value.getText();// " ";//ref_txtHeader3Value.getText();
			if (!header5Value.isEmpty()) {

				cellHeaderPositionHeaderList.add(ConstantReportV2.POPULATE_HEADER5_KEY);
				cellHeaderPositionCellList.add("");

			}
		}

		if (ref_chkBxPopulateHeaderTestPeriod.isSelected()) {

			cellHeaderPositionHeaderList.add(ConstantReportV2.POPULATE_HEADER_KEY_TEST_PERIOD_IN_MINUTES);
			cellHeaderPositionCellList.add("");

		}

		if (ref_chkBxPopulateHeaderWarmupPeriod.isSelected()) {

			cellHeaderPositionHeaderList.add(ConstantReportV2.POPULATE_HEADER_KEY_WARMUP_PERIOD_IN_MINUTES);
			cellHeaderPositionCellList.add("");

		}

		if (ref_chkBxPopulateHeaderActualVoltage.isSelected()) {

			cellHeaderPositionHeaderList.add(ConstantReportV2.POPULATE_HEADER_KEY_TARGET_VOLTAGE);
			cellHeaderPositionCellList.add("");

		}

		if (ref_chkBxPopulateHeaderActualCurrent.isSelected()) {

			cellHeaderPositionHeaderList.add(ConstantReportV2.POPULATE_HEADER_KEY_TARGET_CURRENT);
			cellHeaderPositionCellList.add("");

		}

		if (ref_chkBxPopulateHeaderActualPf.isSelected()) {

			cellHeaderPositionHeaderList.add(ConstantReportV2.POPULATE_HEADER_KEY_TARGET_PF);
			cellHeaderPositionCellList.add("");

		}

		if (ref_chkBxPopulateHeaderActualFreq.isSelected()) {

			cellHeaderPositionHeaderList.add(ConstantReportV2.POPULATE_HEADER_KEY_TARGET_FREQ);
			cellHeaderPositionCellList.add("");

		}

		if (ref_chkBxPopulateHeaderActualEnergy.isSelected()) {

			cellHeaderPositionHeaderList.add(ConstantReportV2.POPULATE_HEADER_KEY_TARGET_ENERGY);
			cellHeaderPositionCellList.add("");

		}

		if (ref_chkBxPopulateRsmInitial.isSelected()) {
			cellHeaderPositionHeaderList.add(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_RSM_INITIAL);
			cellHeaderPositionCellList.add("");
		}

		if (ref_chkBxPopulateRsmFinal.isSelected()) {
			cellHeaderPositionHeaderList.add(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_RSM_FINAL);
			cellHeaderPositionCellList.add("");
		}

		if (ref_chkBxPopulateRsmDifference.isSelected()) {

			cellHeaderPositionHeaderList.add(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_RSM_DIFFERENCE);
			cellHeaderPositionCellList.add("");

		}

		if (ref_rdBtnOperationOutput.isSelected()) {

			String selectedMasterOutputData = ref_cmbBxOperationCriteriaMasterOutputData.getSelectionModel()
					.getSelectedItem().toString();
			// String selectedOperation =
			// ref_cmbBxOperationCriteriaProcessData.getSelectionModel().getSelectedItem().toString();

			if (ref_chkBxPopulateLocalOutputData.isSelected()) {
				headerKey = ref_cmbBxOperationCriteriaLocalOutputData.getSelectionModel().getSelectedItem().toString();
				if (executionResultTypeHeadersOnlyList.contains(headerKey)) {
					cellHeaderPositionHeaderList.add(headerKey);
					cellHeaderPositionCellList.add("");
				} else {
					cellStartHeaderList.add(headerKey);
					cellStartPositionCellList.add("");
				}
			}

			if (ref_chkBxPopulateMasterOutputData.isSelected()) {
				if (!selectedMasterOutputData.equals(ConstantReportV2.NONE_DISPLAYED)) {
					headerKey = ref_cmbBxOperationCriteriaMasterOutputData.getSelectionModel().getSelectedItem()
							.toString();
					if (executionResultTypeHeadersOnlyList.contains(headerKey)) {
						cellHeaderPositionHeaderList.add(headerKey);
						cellHeaderPositionCellList.add("");
					} else {
						cellStartHeaderList.add(headerKey);
						cellStartPositionCellList.add("");
					}
				}
			}

			/*
			 * if(ref_chkBxPopulateLocalOutputData.isSelected()){
			 * cellStartHeaderList.add(OPERATION_POPULATE_LOCAL_OUTPUT_DATA_KEY);
			 * cellStartPositionCellList.add("");
			 * }
			 */
			String upperLimitValue = ref_txtAllowedUpperLimit.getText();
			String lowerLimitValue = ref_txtAllowedLowerLimit.getText();

			if (ref_chkBxPopulateUpperLimitData.isSelected()) {
				if (ref_chkBxMergeUpperLowerLimit.isSelected()) {

					// if(ref_chkBxPopulateMasterOutputData.isSelected()){iu
					if (!ref_chkBxPopulateMasterOutputData.isDisabled()) {
						// if( (!upperLimitValue.isEmpty()) && (!lowerLimitValue.isEmpty()) ){

						cellHeaderPositionHeaderList.add(ConstantReportV2.POPULATE_MASTER_MERGED_LIMIT_KEY);
						cellHeaderPositionCellList.add("");

						// }

						// }else if(ref_chkBxPopulateLocalOutputData.isSelected()){uipoi
					} else if (!ref_chkBxPopulateLocalOutputData.isDisabled()) {
						if ((!upperLimitValue.isEmpty()) && (!lowerLimitValue.isEmpty())) {

							cellHeaderPositionHeaderList.add(ConstantReportV2.POPULATE_LOCAL_MERGED_LIMIT_KEY);
							cellHeaderPositionCellList.add("");

						}
					}

				} else {
					// String upperLimitValue = ref_txtAllowedUpperLimit.getText();
					// if(ref_chkBxPopulateMasterOutputData.isSelected()){uioi
					if (!ref_chkBxPopulateMasterOutputData.isDisabled()) {

						// if(!upperLimitValue.isEmpty()){

						cellHeaderPositionHeaderList.add(ConstantReportV2.POPULATE_MASTER_UPPER_LIMIT_KEY);
						cellHeaderPositionCellList.add("");

						// }

						// }else if(ref_chkBxPopulateLocalOutputData.isSelected()){ipoi
					} else if (!ref_chkBxPopulateLocalOutputData.isDisabled()) {
						if (!upperLimitValue.isEmpty()) {

							cellHeaderPositionHeaderList.add(ConstantReportV2.POPULATE_LOCAL_UPPER_LIMIT_KEY);
							cellHeaderPositionCellList.add("");

						}
					}
				}
			}

			if (ref_chkBxPopulateLowerLimitData.isSelected()) {
				// String lowerLimitValue = ref_txtAllowedLowerLimit.getText();
				// if(ref_chkBxPopulateMasterOutputData.isSelected()){iuoiuo
				if (!ref_chkBxPopulateMasterOutputData.isDisabled()) {

					// if(!lowerLimitValue.isEmpty()){

					cellHeaderPositionHeaderList.add(ConstantReportV2.POPULATE_MASTER_LOWER_LIMIT_KEY);
					cellHeaderPositionCellList.add("");

					// }
					// }else if(ref_chkBxPopulateLocalOutputData.isSelected()){jk;lok
				} else if (!ref_chkBxPopulateLocalOutputData.isDisabled()) {
					if (!lowerLimitValue.isEmpty()) {

						cellHeaderPositionHeaderList.add(ConstantReportV2.POPULATE_LOCAL_LOWER_LIMIT_KEY);
						cellHeaderPositionCellList.add("");

					}
				}
			}

			if (ref_chkBxPopulateComparedLocalResultStatus.isSelected()) {
				// if(ref_chkBxPopulateMasterOutputData.isSelected()){fhgf
				/*
				 * if(!ref_chkBxPopulateMasterOutputData.isDisabled()){
				 * headerKey = ref_cmbBxOperationCriteriaMasterOutputData.getSelectionModel().
				 * getSelectedItem().toString();
				 * headerKey =
				 * headerKey.replace(OPERATION_MASTER_OUTPUT_DATA_HEADER_DISPLAY_PREFIX,
				 * OPERATION_MASTER_OUTPUT_STATUS_HEADER_DISPLAY_PREFIX);
				 * cellStartHeaderList.add(headerKey);
				 * cellStartPositionCellList.add("");
				 * 
				 * //}else if(ref_chkBxPopulateLocalOutputData.isSelected()){fhfg
				 * }else
				 */ if (!ref_chkBxPopulateLocalOutputData.isDisabled()) {
					headerKey = ref_cmbBxOperationComparedLocalResultStatusOutput.getSelectionModel().getSelectedItem()
							.toString();

					cellStartHeaderList.add(headerKey);
					cellStartPositionCellList.add("");
				}
			}

			if (ref_chkBxPopulateComparedMasterResultStatus.isSelected()) {
				// if(ref_chkBxPopulateMasterOutputData.isSelected()){fhgf
				if (!ref_chkBxPopulateMasterOutputData.isDisabled()) {
					headerKey = ref_cmbBxOperationComparedMasterResultStatusOutput.getSelectionModel().getSelectedItem()
							.toString();
					cellStartHeaderList.add(headerKey);
					cellStartPositionCellList.add("");

					// }else if(ref_chkBxPopulateLocalOutputData.isSelected()){fhfg
				}
			}

		}

		for (int i = 0; i < cellStartHeaderList.size(); i++) {
			updateTestFilterDataCellPositionHashMap(cellStartHeaderList.get(i), cellStartPositionCellList.get(i));
		}

		for (int i = 0; i < cellHeaderPositionHeaderList.size(); i++) {
			updateTestFilterDataCellPositionHashMap(cellHeaderPositionHeaderList.get(i),
					cellHeaderPositionCellList.get(i));
		}
		// ApplicationLauncher.logger.debug("cellPositionTitledPaneDisplayDataProcess :
		// cellHeaderPositionHeaderList1: " + cellHeaderPositionHeaderList);
		// ApplicationLauncher.logger.debug("cellPositionTitledPaneDisplayDataProcess :
		// cellHeaderPositionCellList1: " + cellHeaderPositionCellList);

		getTestFilterDataCellPositionHashMap().forEach((key, value) -> {
			if (getLastSavedTestFilterDataCellPositionHashMap().containsKey(key)) {
				// String cellHeader = getLastSavedTestFilterDataCellPosition().get(key);
				ApplicationLauncher.logger.debug("cellPositionTitledPaneDisplayDataProcess : key: " + key);
				ApplicationLauncher.logger.debug("cellPositionTitledPaneDisplayDataProcess : Value: " + value);
				String lastSavedCellPosition = getLastSavedTestFilterDataCellPositionHashMap().get(key);
				updateTestFilterDataCellPositionHashMap(key, lastSavedCellPosition);
				int foundKeyIndex = 0;
				if (cellStartHeaderList.contains(key)) {
					ApplicationLauncher.logger.debug("cellPositionTitledPaneDisplayDataProcess : Hit4");
					foundKeyIndex = cellStartHeaderList.indexOf(key);
					cellStartPositionCellList.set(foundKeyIndex, lastSavedCellPosition);
				} else if (cellHeaderPositionHeaderList.contains(key)) {
					ApplicationLauncher.logger.debug("cellPositionTitledPaneDisplayDataProcess : Hit5");
					foundKeyIndex = cellHeaderPositionHeaderList.indexOf(key);
					cellHeaderPositionCellList.set(foundKeyIndex, lastSavedCellPosition);
				}
				// ApplicationLauncher.logger.debug("btnMeterDataDisplayNextOnClick: Hit1 ");
				/*
				 * String cellPositionValue = "";
				 * cellPositionValue = getLastSavedMeterMetaDataCellPosition().get(key);
				 * if(k.getPopulateDataForEachDut()){
				 * int index = cellStartHeaderList.indexOf(k.getDataTypeKey());
				 * cellStartPositionCellList.set( index,cellPositionValue);
				 * }else if(k.isPopulateOnlyHeader()){
				 * int index = cellHeaderPositionHeaderList.indexOf(k.getDataTypeKey());
				 * cellHeaderPositionCellList.set( index,cellPositionValue);
				 * }
				 */
			}

		});

		// ApplicationLauncher.logger.debug("cellPositionTitledPaneDisplayDataProcess :
		// cellStartHeaderList2: " + cellStartHeaderList);
		// ApplicationLauncher.logger.debug("cellPositionTitledPaneDisplayDataProcess :
		// cellStartPositionCellList2: " + cellStartPositionCellList);
		// ApplicationLauncher.logger.debug("cellPositionTitledPaneDisplayDataProcess :
		// cellHeaderPositionHeaderList2: " + cellHeaderPositionHeaderList);
		// ApplicationLauncher.logger.debug("cellPositionTitledPaneDisplayDataProcess :
		// cellHeaderPositionCellList2: " + cellHeaderPositionCellList);
		String inpDataOwner = ConstantReportV2.RP_DATA_OWNER_TEST_DATA_FILTER;

		// String inpDataType = ConstantReportV2.RP_PRINT_TYPE_ALL;
		boolean populateAllData = true;
		boolean populateOnlyHeaders = false;
		if (cellStartHeaderList.size() > 0) {
			ApplicationLauncher.logger
					.debug("cellPositionTitledPaneDisplayDataProcess : cellStartHeaderList3: " + cellStartHeaderList);
			ApplicationLauncher.logger.debug("cellPositionTitledPaneDisplayDataProcess : cellStartPositionCellList3: "
					+ cellStartPositionCellList);

			loadTableV2(ref_tvTestFilterCellStartPosition, cellStartHeaderList, cellStartPositionCellList,
					cellStartPositionPageCellValues2, inpDataOwner, populateAllData, populateOnlyHeaders);// inpDataType);

		} else {
			ref_tvTestFilterCellStartPosition.getItems().clear();
			ref_tvTestFilterCellStartPosition.refresh();
		}
		// ApplicationLauncher.logger.debug("cellPositionTitledPaneDisplayDataProcess :
		// getTestFilterDataCellPositionHashMap3: " +
		// getTestFilterDataCellPositionHashMap());

		// inpDataType = ConstantReportV2.RP_PRINT_TYPE_HEADER_ONLY;
		populateAllData = false;
		populateOnlyHeaders = true;
		if (cellHeaderPositionHeaderList.size() > 0) {
			// ref_titledPaneCellHeaderPosition.setVisible(true);
			loadTableV2(ref_tvTestFilterCellHeaderPosition, cellHeaderPositionHeaderList, cellHeaderPositionCellList,
					cellStartPositionPageCellValues2, inpDataOwner, populateAllData, populateOnlyHeaders);// inpDataType);

		} else {
			ref_tvTestFilterCellHeaderPosition.getItems().clear();
			ref_tvTestFilterCellHeaderPosition.refresh();
			// ref_titledPaneCellHeaderPosition.setVisible(false);
		}

		// ApplicationLauncher.logger.debug("cellPositionTitledPaneDisplayDataProcess :
		// getTestFilterDataCellPositionHashMap4: " +
		// getTestFilterDataCellPositionHashMap());

		ApplicationLauncher.logger
				.debug("cellPositionTitledPaneDisplayDataProcess : getTestFilterDataCellPositionHashMap4: "
						+ getTestFilterDataCellPositionHashMap());

		/*
		 * getTestFilterDataCellPositionHashMap().forEach(k-> {
		 * //ApplicationLauncher.logger.debug("btnMeterDataDisplayNextOnClick: key: " +
		 * k.getDataTypeKey());
		 * //ApplicationLauncher.logger.debug("btnMeterDataDisplayNextOnClick: value:" +
		 * getLastSavedMeterMetaDataCellPosition().get(k.getDataTypeKey()));
		 * String cellPositionValue = "";
		 * if(getLastSavedMeterMetaDataCellPosition().containsKey(k.getDataTypeKey())){
		 * //ApplicationLauncher.logger.debug("btnMeterDataDisplayNextOnClick: Hit1 ");
		 * cellPositionValue =
		 * getLastSavedMeterMetaDataCellPosition().get(k.getDataTypeKey());
		 * if(k.getPopulateDataForEachDut()){
		 * int index = cellStartHeaderList.indexOf(k.getDataTypeKey());
		 * cellStartPositionCellList.set( index,cellPositionValue);
		 * }else if(k.isPopulateOnlyHeader()){
		 * int index = cellHeaderPositionHeaderList.indexOf(k.getDataTypeKey());
		 * cellHeaderPositionCellList.set( index,cellPositionValue);
		 * }
		 * }
		 * 
		 * });
		 */
		ApplicationLauncher.logger.debug("cellPositionTitledPaneDisplayDataProcess: Exit");
	}

	public boolean validateUserSelectedAtLeastOnePopulateInOperation() {

		ApplicationLauncher.logger.debug("validateUserSelectedAtLeastOnePopulateInOperation: Entry");
		// boolean validCheckBoxNotSelected = false;
		if ((ref_chkBxPopulateMasterOutputData.isSelected()) && (!ref_chkBxPopulateMasterOutputData.isDisabled())) {

			// ApplicationLauncher.logger.info("validateUserSelectedAtLeastOnePopulateInOperation:
			// " + "No Check box selected1: Atleast one check box should be selected for
			// population of data.\n\nKindly select and try again! - Prompted");
			// WindowManager.InformUser("No Check box selected","Atleast one check box
			// should be selected for population of data.\n\nKindly select and try again!"
			// ,AlertType.ERROR);
			// if(!validCheckBoxNotSelected){
			ApplicationLauncher.logger
					.debug("validateUserSelectedAtLeastOnePopulateInOperation: PopulateMasterOutputData Good");
			// validCheckBoxNotSelected = true;
			// }
			// return false;
			return true;
		}

		if ((ref_chkBxPopulateLocalOutputData.isSelected()) && (!ref_chkBxPopulateLocalOutputData.isDisabled())) {

			// ApplicationLauncher.logger.info("validateUserSelectedAtLeastOnePopulateInOperation:
			// " + "No Check box selected2: Atleast one check box should be selected for
			// population of data.\n\nKindly select and try again! - Prompted");
			// WindowManager.InformUser("No Check box selected","Atleast one check box
			// should be selected for population of data.\n\nKindly select and try again!"
			// ,AlertType.ERROR);
			// return false;
			// if(!validCheckBoxNotSelected){
			ApplicationLauncher.logger
					.debug("validateUserSelectedAtLeastOnePopulateInOperation: PopulateLocalOutputData Good");
			// validCheckBoxNotSelected = true;
			// }
			return true;
		}

		if ((ref_chkBxPopulateUpperLimitData.isSelected()) && (!ref_chkBxPopulateUpperLimitData.isDisabled())) {

			// ApplicationLauncher.logger.info("validateUserSelectedAtLeastOnePopulateInOperation:
			// " + "No Check box selected3: Atleast one check box should be selected for
			// population of data.\n\nKindly select and try again! - Prompted");
			// WindowManager.InformUser("No Check box selected","Atleast one check box
			// should be selected for population of data.\n\nKindly select and try again!"
			// ,AlertType.ERROR);
			// return false;
			// if(validCheckBoxNotSelected){
			ApplicationLauncher.logger
					.debug("validateUserSelectedAtLeastOnePopulateInOperation: PopulateUpperLimitData Good");
			// validCheckBoxNotSelected = false;
			// }
			return true;
		}

		if ((ref_chkBxPopulateLowerLimitData.isSelected()) && (!ref_chkBxPopulateLowerLimitData.isDisabled())) {

			// ApplicationLauncher.logger.info("validateUserSelectedAtLeastOnePopulateInOperation:
			// " + "No Check box selected4: Atleast one check box should be selected for
			// population of data.\n\nKindly select and try again! - Prompted");
			// WindowManager.InformUser("No Check box selected","Atleast one check box
			// should be selected for population of data.\n\nKindly select and try again!"
			// ,AlertType.ERROR);
			// return false;
			// if(validCheckBoxNotSelected){
			ApplicationLauncher.logger
					.debug("validateUserSelectedAtLeastOnePopulateInOperation: PopulateUpperLimitData Good");
			// validCheckBoxNotSelected = false;
			// }
			return true;
		}

		if ((ref_chkBxPopulateComparedLocalResultStatus.isSelected())
				&& (!ref_chkBxPopulateComparedLocalResultStatus.isDisabled())) {

			// ApplicationLauncher.logger.info("validateUserSelectedAtLeastOnePopulateInOperation:
			// " + "No Check box selected5: Atleast one check box should be selected for
			// population of data.\n\nKindly select and try again! - Prompted");
			// WindowManager.InformUser("No Check box selected","Atleast one check box
			// should be selected for population of data.\n\nKindly select and try again!"
			// ,AlertType.ERROR);
			// return false;
			// if(validCheckBoxNotSelected){
			ApplicationLauncher.logger
					.debug("validateUserSelectedAtLeastOnePopulateInOperation: PopulateComparedResultStatus Good");
			// validCheckBoxNotSelected = false;
			// }
			return true;
		}

		if ((ref_chkBxPopulateComparedMasterResultStatus.isSelected())
				&& (!ref_chkBxPopulateComparedMasterResultStatus.isDisabled())) {

			// ApplicationLauncher.logger.info("validateUserSelectedAtLeastOnePopulateInOperation:
			// " + "No Check box selected1: Atleast one check box should be selected for
			// population of data.\n\nKindly select and try again! - Prompted");
			// WindowManager.InformUser("No Check box selected","Atleast one check box
			// should be selected for population of data.\n\nKindly select and try again!"
			// ,AlertType.ERROR);
			// if(!validCheckBoxNotSelected){
			ApplicationLauncher.logger.debug(
					"validateUserSelectedAtLeastOnePopulateInOperation: PopulateMasterStatusOutputData for Average Type Good");
			// validCheckBoxNotSelected = true;
			// }
			// return false;
			return true;
		}

		// if(validCheckBoxNotSelected){
		ApplicationLauncher.logger.info("validateUserSelectedAtLeastOnePopulateInOperation : ERROR_CODE_6004: "
				+ ErrorCodeMapping.ERROR_CODE_6004_MSG + " - prompted!");
		WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6004, ErrorCodeMapping.ERROR_CODE_6004_MSG,
				AlertType.ERROR);
		// ApplicationLauncher.logger.info("validateUserSelectedAtLeastOnePopulateInOperation:
		// " + "ErrorCode_R2400: No Check box selected\n\nAtleast one check box should
		// be selected for population of data.\n\nKindly select and try again! -
		// Prompted");
		// WindowManager.InformUser("ErrorCode_R2400","No Check box selected\n\nAtleast
		// one check box should be selected for population of data.\n\nKindly select and
		// try again!" ,AlertType.ERROR);

		// }

		ApplicationLauncher.logger.debug("validateUserSelectedAtLeastOnePopulateInOperation: Exit");
		return false;// validCheckBoxNotSelected;//true;
	}

	public boolean validateForCompareLimitsGuiInOperation() {

		ApplicationLauncher.logger.debug("validateForCompareLimitsGuiInOperation: Entry");

		// String selectedMasterOutputValue =
		// ref_cmbBxOperationCriteriaMasterOutputData.getSelectionModel().getSelectedItem().toString();
		String selectedOperation = ref_cmbBxOperationCriteriaProcessData.getSelectionModel().getSelectedItem()
				.toString();
		List<String> inputProcessDataList = ref_listViewInputProcessList.getItems();
		String selectedMasterOutputData = ref_cmbBxOperationCriteriaMasterOutputData.getSelectionModel()
				.getSelectedItem().toString();
		String selectedLocalOutputData = ref_cmbBxOperationCriteriaLocalOutputData.getSelectionModel().getSelectedItem()
				.toString();
		String selectedComparedResultStatus = ref_cmbBxOperationComparedLocalResultStatusOutput.getSelectionModel()
				.getSelectedItem().toString();
		if (selectedLocalOutputData.equals(selectedComparedResultStatus)) {
			ApplicationLauncher.logger.info("validateForCompareLimitsGuiInOperation : ERROR_CODE_6005: "
					+ ErrorCodeMapping.ERROR_CODE_6005_MSG + selectedComparedResultStatus + " - prompted!");
			WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6005,
					ErrorCodeMapping.ERROR_CODE_6005_MSG + selectedComparedResultStatus, AlertType.ERROR);

			// ApplicationLauncher.logger.info("validateForCompareLimitsGuiInOperation: " +
			// "Same Output selected: <Output Data> and <Compared Result status> selection
			// output are same. Kindly ensure different outputs are selected\n\nSelected
			// output: " + selectedComparedResultStatus+" - Prompted");
			// WindowManager.InformUser("Same Output selected","<Output Data> and <Compared
			// Result status> selection output are same. Kindly ensure different outputs are
			// selected\n\nSelected output: " +
			// selectedComparedResultStatus,AlertType.ERROR);

		} else {
			if (selectedOperation.equals(ConstantReportV2.NONE_DISPLAYED)) {
				ApplicationLauncher.logger.debug("validateForCompareLimitsGuiInOperation: test1");
				if (!selectedMasterOutputData.equals(ConstantReportV2.NONE_DISPLAYED)) {
					boolean status = validateUserSelectedAtLeastOnePopulateInOperation();
					if (status) {
						ApplicationLauncher.logger.debug("validateForCompareLimitsGuiInOperation: Success1");
						return true;

					}
				}
			} else {
				if (!ref_txtAllowedUpperLimit.getText().isEmpty()) {
					// ApplicationLauncher.logger.debug("validateForCompareLimitsGuiInOperation:
					// test1");
					if (!ref_txtAllowedLowerLimit.getText().isEmpty()) {
						// ApplicationLauncher.logger.debug("validateForCompareLimitsGuiInOperation:
						// test2");
						float allowedUpperLimit = Float.parseFloat(txtAllowedUpperLimit.getText());
						float allowedLowerLimit = Float.parseFloat(txtAllowedLowerLimit.getText());
						// if(allowedUpperLimit != allowedLowerLimit){
						if ((allowedUpperLimit > allowedLowerLimit) || (allowedUpperLimit == allowedLowerLimit)) {
							if (selectedOperation.equals(ConstantReportV2.NONE_DISPLAYED)) {
								ApplicationLauncher.logger.debug("validateForCompareLimitsGuiInOperation: testX");
							} else {// if((!selectedOperation.equals(NONE_DISPLAYED))){
								if ((inputProcessDataList.size() > 0)) {
									// ref_titledPaneCellPosition.setExpanded(true);

									ApplicationLauncher.logger
											.debug("validateForCompareLimitsGuiInOperation: ValidateCheckBox test1");
									boolean status = validateUserSelectedAtLeastOnePopulateInOperation();
									if (status) {
										ApplicationLauncher.logger
												.debug("validateForCompareLimitsGuiInOperation: Success2");
										return true;
										// ref_titledPaneCellPosition.setExpanded(true);
									}
								} else {

									ApplicationLauncher.logger
											.info("validateForCompareLimitsGuiInOperation : ERROR_CODE_6006: "
													+ ErrorCodeMapping.ERROR_CODE_6006_MSG + " - prompted!");
									WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6006,
											ErrorCodeMapping.ERROR_CODE_6006_MSG, AlertType.ERROR);

									// ApplicationLauncher.logger.info("validateForCompareLimitsGuiInOperation: " +
									// "ErrorCode_R2401: Input Process list table is empty or insufficient data to
									// process operation.\n\nKindly add the input process list and try again! -
									// Prompted");
									// WindowManager.InformUser("ErrorCode_R2401","Input Process list table is empty
									// or insufficient data to process operation.\n\nKindly add the input process
									// list and try again!" ,AlertType.ERROR);

								}
							}
						} else {
							ApplicationLauncher.logger.debug(
									"validateForCompareLimitsGuiInOperation: allowedUpperLimit: " + allowedUpperLimit);
							ApplicationLauncher.logger.debug(
									"validateForCompareLimitsGuiInOperation: allowedLowerLimit: " + allowedLowerLimit);
							ApplicationLauncher.logger.info("validateForCompareLimitsGuiInOperation : ERROR_CODE_6021: "
									+ ErrorCodeMapping.ERROR_CODE_6021_MSG + "\n\nAllowedUpperLimit = "
									+ allowedUpperLimit + "\nAllowedLowerLimit = " + allowedLowerLimit
									+ " - prompted!");
							WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6021,
									ErrorCodeMapping.ERROR_CODE_6021_MSG + "\n\nAllowedUpperLimit = "
											+ allowedUpperLimit + "\nAllowedLowerLimit = " + allowedLowerLimit,
									AlertType.ERROR);

						}
						/*
						 * }else{
						 * 
						 * ApplicationLauncher.logger.
						 * info("validateForCompareLimitsGuiInOperation : ERROR_CODE_6007: "+
						 * ErrorCodeMapping.ERROR_CODE_6007_MSG +" - prompted!");
						 * WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6007,
						 * ErrorCodeMapping.ERROR_CODE_6007_MSG ,AlertType.ERROR);
						 * 
						 * //ApplicationLauncher.logger.info("validateForCompareLimitsGuiInOperation: "
						 * +
						 * "ErrorCode_R2402: Same value entered on <Allowed Upper Limit> and <Allowed Lower Limit> field.\n\nKindly enter different value and try again! - Prompted"
						 * );
						 * //WindowManager.InformUser(
						 * "ErrorCode_R2402","Same value entered on <Allowed Upper Limit> and <Allowed Lower Limit> field.\n\nKindly enter different value and try again!"
						 * ,AlertType.ERROR);
						 * 
						 * }
						 */
					} else {

						ApplicationLauncher.logger.info("validateForCompareLimitsGuiInOperation : ERROR_CODE_6008: "
								+ ErrorCodeMapping.ERROR_CODE_6008_MSG + " - prompted!");
						WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6008, ErrorCodeMapping.ERROR_CODE_6008_MSG,
								AlertType.ERROR);

						// ApplicationLauncher.logger.info("validateForCompareLimitsGuiInOperation: " +
						// "ErrorCode_R2403: <Allowed Lower Limit> field is empty.\n\nKindly enter valid
						// value and try again! - Prompted");
						// WindowManager.InformUser("ErrorCode_R2403","<Allowed Lower Limit> field is
						// empty.\n\nKindly enter valid value and try again!" ,AlertType.ERROR);

					}

				} else {

					ApplicationLauncher.logger.info("validateForCompareLimitsGuiInOperation : ERROR_CODE_6009: "
							+ ErrorCodeMapping.ERROR_CODE_6009_MSG + " - prompted!");
					WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6009, ErrorCodeMapping.ERROR_CODE_6009_MSG,
							AlertType.ERROR);

					// ApplicationLauncher.logger.info("validateForCompareLimitsGuiInOperation: " +
					// "ErrorCode_R2404: <Allowed Upper Limit> field is empty.\n\nKindly enter valid
					// value and try again! - Prompted");
					// WindowManager.InformUser("ErrorCode_R2404","<Allowed Upper Limit> field is
					// empty.\n\nKindly enter valid value and try again!" ,AlertType.ERROR);

				}
			}

		}

		ApplicationLauncher.logger.debug("validateForCompareLimitsGuiInOperation: Exit");
		return false;
	}

	@FXML
	public void btnOperationNextOnClick() {
		ApplicationLauncher.logger.debug("btnOperationNextOnClick: Entry");

		boolean status = false;
		cellPositionTitledPaneDisplayDataProcess();

		String selectedMasterOutputValue = ref_cmbBxOperationCriteriaMasterOutputData.getSelectionModel()
				.getSelectedItem().toString();
		String selectedLocalOutputValue = ref_cmbBxOperationCriteriaLocalOutputData.getSelectionModel()
				.getSelectedItem().toString();
		String selectedOperation = ref_cmbBxOperationCriteriaProcessData.getSelectionModel().getSelectedItem()
				.toString();

		List<String> inputProcessDataList = ref_listViewInputProcessList.getItems();
		ApplicationLauncher.logger.debug("btnOperationNextOnClick: selectedOperation: " + selectedOperation);
		ApplicationLauncher.logger
				.debug("btnOperationNextOnClick: selectedMasterOutputValue: " + selectedMasterOutputValue);
		// if (!selectedOperation.equals(NONE_DISPLAYED)) {

		if (ref_chkBxPostOperationActive.isSelected()) {
			if (ref_txtPostOperationValue.getText().isEmpty()) {
				ApplicationLauncher.logger.info("btnOperationNextOnClick : ERROR_CODE_6025: "
						+ ErrorCodeMapping.ERROR_CODE_6025_MSG + " - prompted!");
				WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6025, ErrorCodeMapping.ERROR_CODE_6025_MSG,
						AlertType.ERROR);
				return;
			}

		}
		if (!selectedMasterOutputValue.equals(ConstantReportV2.NONE_DISPLAYED)) {
			if (inputProcessDataList.contains(selectedMasterOutputValue)) {

				ApplicationLauncher.logger
						.info("btnOperationNextOnClick : ERROR_CODE_6026: " + ErrorCodeMapping.ERROR_CODE_6026_MSG
								+ "\n\nMaster Output: " + selectedMasterOutputValue + " - prompted!");
				WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6026,
						ErrorCodeMapping.ERROR_CODE_6026_MSG + "\n\nMaster Output: " + selectedMasterOutputValue,
						AlertType.ERROR);
				return;

			}
		}

		if (!selectedOperation.equals(ConstantReportV2.NONE_DISPLAYED)) {
			if (inputProcessDataList.contains(selectedLocalOutputValue)) {

				ApplicationLauncher.logger
						.info("btnOperationNextOnClick : ERROR_CODE_6027: " + ErrorCodeMapping.ERROR_CODE_6027_MSG
								+ "\n\nLocal Output Data: " + selectedLocalOutputValue + " - prompted!");
				WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6027,
						ErrorCodeMapping.ERROR_CODE_6027_MSG + "\n\nLocal Output Data: " + selectedLocalOutputValue,
						AlertType.ERROR);
				return;

			}
		}

		if (ref_chkBxPostOperationActive.isSelected()) {

			String selectedPostOperation = ref_cmbBxPostOperationMethod.getSelectionModel().getSelectedItem()
					.toString();
			if (selectedPostOperation.equals(ConstantReportV2.POST_OPERATION_METHOD_DIVIDE)) {
				String userInputPostProcessingValue = ref_txtPostOperationValue.getText();
				if (Float.parseFloat(userInputPostProcessingValue) == 0.0f) {
					ApplicationLauncher.logger.info("btnOperationNextOnClick : ERROR_CODE_6047: "
							+ ErrorCodeMapping.ERROR_CODE_6047 + " - prompted!");
					WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6047, ErrorCodeMapping.ERROR_CODE_6047_MSG,
							AlertType.ERROR);
					return;
				}

			}

		}

		if ((!selectedOperation.equals(ConstantReportV2.NONE_DISPLAYED))
				&& (!selectedMasterOutputValue.equals(ConstantReportV2.NONE_DISPLAYED))) {
			ApplicationLauncher.logger.debug("btnOperationNextOnClick: Hit1");
			// List<String> inputProcessDataList = ref_listViewInputProcessList.getItems();
			// if(inputProcessDataList.size()>0){

			if (ref_chkBxCompareWithLimits.isSelected()) {
				ApplicationLauncher.logger.debug("btnOperationNextOnClick: CompareLimitsGuiInOperation test1");
				status = validateForCompareLimitsGuiInOperation();
				if (status) {
					ApplicationLauncher.logger.debug("btnOperationNextOnClick: Success1");
					ref_titledPaneCellPosition.setExpanded(true);
				}
			} else {
				// ref_titledPaneCellPosition.setExpanded(true);
				// if(selectedOperation.equals(NONE_DISPLAYED)){

				// }else {// if((!selectedOperation.equals(NONE_DISPLAYED))){
				if ((inputProcessDataList.size() > 1)) {
					/*
					 * if(!ref_chkBxPopulateLocalOutputData.isSelected()){
					 * ApplicationLauncher.logger.info("btnOperationNextOnClick: " +
					 * "No Check box selected2: Atleast one check box should be selected for population of data.\n\nKindly select and try again! - Prompted"
					 * );
					 * WindowManager.InformUser("No Check box selected"
					 * ,"Atleast one check box should be selected for population of data.\n\nKindly select and try again!"
					 * ,AlertType.ERROR);
					 * 
					 * }else{
					 * ref_titledPaneCellPosition.setExpanded(true);
					 * }
					 */

					ApplicationLauncher.logger.debug("btnOperationNextOnClick: ValidateCheckBox test2");

					status = validateUserSelectedAtLeastOnePopulateInOperation();
					if (status) {
						status = isOutputProcessPropertyMatching();
						if (status) {
							ApplicationLauncher.logger.debug("btnOperationNextOnClick: Success2");
							ref_titledPaneCellPosition.setExpanded(true);
						}
					}

					// ref_titledPaneCellPosition.setExpanded(true);
				} else {
					ApplicationLauncher.logger.info("btnOperationNextOnClick : ERROR_CODE_6010: "
							+ ErrorCodeMapping.ERROR_CODE_6010_MSG + " - prompted!");
					WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6010, ErrorCodeMapping.ERROR_CODE_6010_MSG,
							AlertType.ERROR);

					// ApplicationLauncher.logger.info("btnOperationNextOnClick: " +
					// "ErrorCode_R2405: Input Process list table is empty or insufficient data to
					// process operation.\n\nKindly add the input process list and try again! -
					// Prompted");
					// WindowManager.InformUser("ErrorCode_R2405","Input Process list table is empty
					// or insufficient data to process operation.\n\nKindly add the input process
					// list and try again!" ,AlertType.ERROR);

				}
				// }
			}
			/*
			 * }else{gvn
			 * ApplicationLauncher.logger.info("btnOperationNextOnClick: " +
			 * "Input Process list empty: Input Process list table is empty.\n\nKindly add the input process list and try again! - Prompted"
			 * );
			 * WindowManager.InformUser("Input Process list empty"
			 * ,"Input Process list table is empty.\n\nKindly add the input process list and try again!"
			 * ,AlertType.ERROR);
			 * 
			 * }
			 */
		} else if ((selectedOperation.equals(ConstantReportV2.NONE_DISPLAYED))
				&& (selectedMasterOutputValue.equals(ConstantReportV2.NONE_DISPLAYED))) {
			ApplicationLauncher.logger.debug("btnOperationNextOnClick: Hit2");

			ApplicationLauncher.logger.info("btnOperationNextOnClick : ERROR_CODE_6011: "
					+ ErrorCodeMapping.ERROR_CODE_6011_MSG + " - prompted!");
			WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6011, ErrorCodeMapping.ERROR_CODE_6011_MSG,
					AlertType.ERROR);

			// ApplicationLauncher.logger.info("btnOperationNextOnClick: " +
			// "ErrorCode_R2406: Both <Operation> and <Master Output> fields are
			// invalid.\n\nKindly select valid input and try again! - Prompted");
			// WindowManager.InformUser("ErrorCode_R2406","Both <Operation> and <Master
			// Output> fields are invalid.\n\nKindly select valid input and try again!"
			// ,AlertType.ERROR);

		} else if ((selectedOperation.equals(ConstantReportV2.NONE_DISPLAYED))
				&& (!selectedMasterOutputValue.equals(ConstantReportV2.NONE_DISPLAYED))) {

			/*
			 * if(!ref_chkBxPopulateMasterOutputData.isSelected()){
			 * if(!ref_chkBxPopulateUpperLimitData.isSelected()){
			 * if(!ref_chkBxPopulateComparedResultStatus.isSelected()){
			 * ApplicationLauncher.logger.info("btnOperationNextOnClick: " +
			 * "No Check box selected: Atleast one check box should be selected for population of data.\n\nKindly select and try again! - Prompted"
			 * );
			 * WindowManager.InformUser("No Check box selected"
			 * ,"Atleast one check box should be selected for population of data.\n\nKindly select and try again!"
			 * ,AlertType.ERROR);
			 * 
			 * }else{
			 * ref_titledPaneCellPosition.setExpanded(true);
			 * }
			 * }else{
			 * ref_titledPaneCellPosition.setExpanded(true);
			 * }
			 * }else{
			 * ref_titledPaneCellPosition.setExpanded(true);
			 * }
			 */

			ApplicationLauncher.logger.debug("btnOperationNextOnClick: Hit3");
			if (ref_chkBxCompareWithLimits.isSelected()) {
				ApplicationLauncher.logger.debug("btnOperationNextOnClick: CompareLimitsGuiInOperation test3");
				status = validateForCompareLimitsGuiInOperation();
				if (status) {
					ApplicationLauncher.logger.debug("btnOperationNextOnClick: Success3");
					ref_titledPaneCellPosition.setExpanded(true);
				}
			} else {
				ApplicationLauncher.logger.debug("btnOperationNextOnClick: ValidateCheckBox test4");
				status = validateUserSelectedAtLeastOnePopulateInOperation();
				if (status) {
					status = isOutputProcessPropertyMatching();
					if (status) {
						ApplicationLauncher.logger.debug("btnOperationNextOnClick: Success4");
						ref_titledPaneCellPosition.setExpanded(true);
					}
				}
			}
			// ApplicationLauncher.logger.info("btnOperationNextOnClick: " + "Invalid
			// options selected: Both <Operation> and <Master Output> fields are
			// invalid.\n\nKindly select valid input and try again! - Prompted");
			// WindowManager.InformUser("Invalid options selected","Both <Operation> and
			// <Master Output> fields are invalid.\n\nKindly select valid input and try
			// again!" ,AlertType.ERROR);

		} else if ((!selectedOperation.equals(ConstantReportV2.NONE_DISPLAYED))
				&& (selectedMasterOutputValue.equals(ConstantReportV2.NONE_DISPLAYED))) {

			// ApplicationLauncher.logger.info("btnOperationNextOnClick: " + "Input Process
			// list empty2: Input Process list table is empty.\n\nKindly add the input
			// process list and try again! - Prompted");
			// WindowManager.InformUser("Input Process list empty","Input Process list table
			// is empty.\n\nKindly add the input process list and try again!"
			// ,AlertType.ERROR);
			ApplicationLauncher.logger.debug("btnOperationNextOnClick: Hit4");
			if (ref_chkBxCompareWithLimits.isSelected()) {
				ApplicationLauncher.logger.debug("btnOperationNextOnClick: CompareLimitsGuiInOperation test5");
				status = validateForCompareLimitsGuiInOperation();
				if (status) {
					ApplicationLauncher.logger.debug("btnOperationNextOnClick: Success5");
					ref_titledPaneCellPosition.setExpanded(true);
				}
			} else {
				ApplicationLauncher.logger.debug("btnOperationNextOnClick: ValidateCheckBox test6");

				if ((inputProcessDataList.size() > 0)) {
					// ref_titledPaneCellPosition.setExpanded(true);

					ApplicationLauncher.logger.debug("btnOperationNextOnClick: ValidateCheckBox test6A");
					status = validateUserSelectedAtLeastOnePopulateInOperation();
					if (status) {
						status = isOutputProcessPropertyMatching();
						if (status) {
							// ApplicationLauncher.logger.debug("validateForCompareLimitsGuiInOperation:
							// Success6");
							ApplicationLauncher.logger.debug("btnOperationNextOnClick: Success6");
							ref_titledPaneCellPosition.setExpanded(true);
							// ref_titledPaneCellPosition.setExpanded(true);
						}
					}
				} else {

					ApplicationLauncher.logger.info("btnOperationNextOnClick : ERROR_CODE_6012: "
							+ ErrorCodeMapping.ERROR_CODE_6012_MSG + " - prompted!");
					WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6012, ErrorCodeMapping.ERROR_CODE_6012_MSG,
							AlertType.ERROR);

					// ApplicationLauncher.logger.info("validateForCompareLimitsGuiInOperation: " +
					// "ErrorCode_R2407: Input Process list table is empty or insufficient data to
					// process operation.\n\nKindly add the input process list and try again! -
					// Prompted");
					// WindowManager.InformUser("ErrorCode_R2407","Input Process list table is empty
					// or insufficient data to process operation.\n\nKindly add the input process
					// list and try again!" ,AlertType.ERROR);

				}

				/*
				 * status = validateUserSelectedAtLeastOnePopulateInOperation ();
				 * if(status){
				 * ApplicationLauncher.logger.debug("btnOperationNextOnClick: Success6");
				 * ref_titledPaneCellPosition.setExpanded(true);
				 * }
				 */
			}

		}

	}

	private boolean isOutputProcessPropertyMatching() {

		ApplicationLauncher.logger.debug("isOutputProcessPropertyMatching: Entry");

		String selectedMasterOutputValue = ref_cmbBxOperationCriteriaMasterOutputData.getSelectionModel()
				.getSelectedItem().toString();
		String selectedLocalOutputValue = ref_cmbBxOperationCriteriaLocalOutputData.getSelectionModel()
				.getSelectedItem().toString();
		String selectedOperation = ref_cmbBxOperationCriteriaProcessData.getSelectionModel().getSelectedItem()
				.toString();
		List<String> inputProcessDataList = ref_listViewInputProcessList.getItems();
		ApplicationLauncher.logger
				.debug("isOutputProcessPropertyMatching: inputProcessDataList: " + inputProcessDataList);
		// boolean anyInputIsHeaderOnly =
		// for (String eachInputSelected : inputProcessDataList){
		/*
		 * ArrayList<String> localInputHeadersOnlyList = (ArrayList<String>)
		 * getOperationProcessDataModel().getOperationLocalInput().stream()
		 * //.filter( e -> e.getOperationProcessKey().equals(eachInputSelected))
		 * .filter(e -> e.isPopulateOnlyHeaders() == true)
		 * .map(e -> e.getOperationProcessKey())
		 * .collect(Collectors.toList());
		 * 
		 * ArrayList<String> localOutputHeadersOnlyList = (ArrayList<String>)
		 * getOperationProcessDataModel().getOperationLocalOutput().stream()
		 * .filter(e -> e.isPopulateOnlyHeaders() == true)
		 * .map(e -> e.getOperationProcessKey())
		 * .collect(Collectors.toList());
		 * 
		 * ArrayList<String> masterOutputHeadersOnlyList = (ArrayList<String>)
		 * getOperationProcessDataModel().getOperationMasterOutput().stream()
		 * .filter(e -> e.isPopulateOnlyHeaders() == true)
		 * .map(e -> e.getOperationProcessKey())
		 * .collect(Collectors.toList());
		 */

		ArrayList<String> localInputHeadersOnlyList = (ArrayList<String>) getOperationParameterProfileDataList()
				.stream()
				.filter(e -> e.getParamType().equals(ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_INPUT))
				.filter(e -> e.isPopulateOnlyHeaders() == true)
				.map(e -> e.getKeyParam())
				.collect(Collectors.toList());

		ArrayList<String> localOutputHeadersOnlyList = (ArrayList<String>) getOperationParameterProfileDataList()
				.stream()
				.filter(e -> e.getParamType().equals(ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_OUTPUT))
				.filter(e -> e.isPopulateOnlyHeaders() == true)
				.map(e -> e.getKeyParam())
				.collect(Collectors.toList());

		ArrayList<String> masterOutputHeadersOnlyList = (ArrayList<String>) getOperationParameterProfileDataList()
				.stream()
				.filter(e -> e.getParamType().equals(ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_MASTER_OUTPUT))
				.filter(e -> e.isPopulateOnlyHeaders() == true)
				.map(e -> e.getKeyParam())
				.collect(Collectors.toList());

		// for (String eachInputHeaderOnlyKey: localInputHeadersOnlyList){
		int counter = 0;
		for (String eachInputSelected : inputProcessDataList) {
			ApplicationLauncher.logger
					.debug("isOutputProcessPropertyMatching: eachInputSelected: " + eachInputSelected);
			boolean alreadyCounted = false;
			if (localInputHeadersOnlyList.contains(eachInputSelected)) {
				counter++;
				alreadyCounted = true;
				ApplicationLauncher.logger.debug("isOutputProcessPropertyMatching: hit1");
			}
			if (localOutputHeadersOnlyList.contains(eachInputSelected)) {
				ApplicationLauncher.logger.debug("isOutputProcessPropertyMatching: hit2");
				if (!alreadyCounted) {
					ApplicationLauncher.logger.debug("isOutputProcessPropertyMatching: hit2A");
					counter++;
				}
			}
			if (masterOutputHeadersOnlyList.contains(eachInputSelected)) {
				ApplicationLauncher.logger.debug("isOutputProcessPropertyMatching: hit3");
				if (!alreadyCounted) {
					ApplicationLauncher.logger.debug("isOutputProcessPropertyMatching: hit3A");
					counter++;
				}
			}
		}

		if (counter == inputProcessDataList.size()) {

			if (selectedMasterOutputValue.equals(ConstantReportV2.OPERATION_PROCESS_MODE_NONE)) {
				if (localOutputHeadersOnlyList.contains(selectedLocalOutputValue)) {
					ApplicationLauncher.logger
							.debug("isOutputProcessPropertyMatching: all selected data are matching with Headers Only");
					return true;
				} else {
					ApplicationLauncher.logger.debug(
							"isOutputProcessPropertyMatching: Operation process: Local Output data not marked Headers Only. Kindly check the property");
				}
			} else {
				if (masterOutputHeadersOnlyList.contains(selectedMasterOutputValue)) {
					if (localOutputHeadersOnlyList.contains(selectedLocalOutputValue)) {
						ApplicationLauncher.logger
								.debug("isOutputProcessPropertyMatching2: all data are matching with Headers Only");
						return true;
					} else {
						ApplicationLauncher.logger.debug(
								"isOutputProcessPropertyMatching2: Operation process: Local Output data not marked Headers Only. Kindly check the property");
					}
				} else {

					/*
					 * if(selectedOperation.equals(ConstantReportV2.OPERATION_PROCESS_MODE_NONE)){
					 * if(ref_listViewInputProcessList.getItems().size()>0){
					 * String selectedLocalInputValue =
					 * ref_listViewInputProcessList.getItems().get(0);
					 * 
					 * //ref_cmbBxOperationCriteriaInputData.getSelectionModel().getSelectedItem().
					 * toString();
					 * //String paramProfileName =
					 * 
					 * ApplicationLauncher.logger.
					 * debug("isOutputProcessPropertyMatching: selectedLocalInputValue: " +
					 * selectedLocalInputValue);
					 * ApplicationLauncher.logger.
					 * debug("isOutputProcessPropertyMatching: selectedMasterOutputValue: " +
					 * selectedMasterOutputValue);
					 * 
					 * getOperationParameterProfileDataList().stream()
					 * .forEachOrdered( e-> {
					 * ApplicationLauncher.logger.debug("isOutputProcessPropertyMatching: key: " +
					 * e.getKeyParam() + " -> " + e.getPopulateType());
					 * });
					 * 
					 * boolean localInputAverageType =
					 * getOperationParameterProfileDataList().stream()
					 * .filter(e -> e.getKeyParam().equals(selectedLocalInputValue))
					 * .anyMatch(e -> e.getPopulateType().equals(ConstantReportV2.
					 * POPULATE_DATA_TYPE_ALL_DUT_AVERAGE));
					 * 
					 * boolean masterInputAverageType =
					 * getOperationParameterProfileDataList().stream()
					 * .filter(e -> e.getKeyParam().equals(selectedMasterOutputValue))
					 * .anyMatch(e -> e.getPopulateType().equals(ConstantReportV2.
					 * POPULATE_DATA_TYPE_ALL_DUT_AVERAGE));
					 * 
					 * if(localInputAverageType && masterInputAverageType){
					 * return true;
					 * }else{
					 * ApplicationLauncher.logger.
					 * debug("isOutputProcessPropertyMatching: Operation process: Input Process is empty. Kindly check the property"
					 * );
					 * }
					 * }else{
					 * 
					 * ApplicationLauncher.logger.
					 * debug("isOutputProcessPropertyMatching: Operation process: Master Output data not marked Headers Only. Kindly check the property"
					 * );
					 * 
					 * }
					 * 
					 * }
					 */

					// else{

					ApplicationLauncher.logger.debug(
							"isOutputProcessPropertyMatching: Operation process: Master Output data not marked Headers Only. Kindly check the property");

					// }
				}
			}

		} else {

			if (selectedOperation.equals(ConstantReportV2.OPERATION_PROCESS_MODE_NONE)) {
				if (ref_listViewInputProcessList.getItems().size() > 0) {
					String selectedLocalInputValue = ref_listViewInputProcessList.getItems().get(0);

					// ref_cmbBxOperationCriteriaInputData.getSelectionModel().getSelectedItem().toString();
					// String paramProfileName =

					ApplicationLauncher.logger.debug(
							"isOutputProcessPropertyMatching: selectedLocalInputValue: " + selectedLocalInputValue);
					ApplicationLauncher.logger.debug(
							"isOutputProcessPropertyMatching: selectedMasterOutputValue: " + selectedMasterOutputValue);
					/*
					 * getOperationParameterProfileDataList().stream()
					 * .forEachOrdered( e-> {
					 * ApplicationLauncher.logger.debug("isOutputProcessPropertyMatching: key: " +
					 * e.getKeyParam() + " -> " + e.getPopulateType());
					 * });
					 */

					/*
					 * boolean localInputAverageType =
					 * getOperationParameterProfileDataList().stream()
					 * .filter(e -> e.getKeyParam().equals(selectedLocalInputValue))
					 * .anyMatch(e -> e.getPopulateType().equals(ConstantReportV2.
					 * POPULATE_DATA_TYPE_ALL_DUT_AVERAGE));
					 * 
					 * boolean masterInputAverageType =
					 * getOperationParameterProfileDataList().stream()
					 * .filter(e -> e.getKeyParam().equals(selectedMasterOutputValue))
					 * .anyMatch(e -> e.getPopulateType().equals(ConstantReportV2.
					 * POPULATE_DATA_TYPE_ALL_DUT_AVERAGE));
					 * 
					 * if(localInputAverageType && masterInputAverageType){
					 */
					return true;
					// }else{
					// ApplicationLauncher.logger.debug("isOutputProcessPropertyMatching: Operation
					// process: Input selected or Master output is not Average Type. Kindly check
					// the property");
					// }
				} else {

					ApplicationLauncher.logger.debug(
							"isOutputProcessPropertyMatching: Operation process: Master Output data not marked Headers Only. Kindly check the property");

				}

			} else {
				ApplicationLauncher.logger.debug(
						"isOutputProcessPropertyMatching: Operation process: input process list, all data are not marked Headers Only. Kindly check the property");
				return true;
			}
		}

		return false;
	}

	public void updateOperationProcessDatabaseId() {
		ApplicationLauncher.logger.debug("updateOperationProcessDatabaseId: Entry");
		for (int dbIndex = 0; dbIndex < getReportProfileManageModel().getReportProfileTestDataFilterList()
				.size(); dbIndex++) {
			ApplicationLauncher.logger
					.debug("updateOperationProcessDatabaseId : db getTestFilterName: " + getReportProfileManageModel()
							.getReportProfileTestDataFilterList().get(dbIndex).getTestFilterName());
			String dbFilterName = getReportProfileManageModel().getReportProfileTestDataFilterList().get(dbIndex)
					.getTestFilterName();
			List<OperationProcess> opProcessDataList = getReportProfileManageModel()
					.getReportProfileTestDataFilterList().get(dbIndex).getOperationProcessDataList();
			for (int dbProcessIndex = 0; dbProcessIndex < opProcessDataList.size(); dbProcessIndex++) {

				String dbProcessKey = opProcessDataList.get(dbProcessIndex).getOperationProcessKey();
				ApplicationLauncher.logger.debug("updateOperationProcessDatabaseId : dbProcessKey: " + dbProcessKey);

				for (int displayIndex = 0; displayIndex < ref_tvTestFilterDataList.getItems().size(); displayIndex++) {
					ApplicationLauncher.logger.debug("updateOperationProcessDatabaseId : display getTestFilterName: "
							+ ref_tvTestFilterDataList.getItems().get(displayIndex).getTestFilterName());
					String displayFilterName = ref_tvTestFilterDataList.getItems().get(displayIndex)
							.getTestFilterName();
					if (dbFilterName.equals(displayFilterName)) {
						List<OperationProcess> displayOpProcessDataList = ref_tvTestFilterDataList.getItems()
								.get(displayIndex).getOperationProcessDataList();

						for (int displayProcessIndex = 0; displayProcessIndex < displayOpProcessDataList
								.size(); displayProcessIndex++) {

							String displayProcessKey = displayOpProcessDataList.get(displayProcessIndex)
									.getOperationProcessKey();
							ApplicationLauncher.logger.debug(
									"updateOperationProcessDatabaseId : displayProcessKey: " + displayProcessKey);
							if (dbProcessKey.equals(displayProcessKey)) {
								if (ref_tvTestFilterDataList.getItems().get(displayIndex).getOperationProcessDataList()
										.get(displayProcessIndex).getId() == null) {
									int idValue = getReportProfileManageModel().getReportProfileTestDataFilterList()
											.get(dbIndex).getOperationProcessDataList().get(dbProcessIndex).getId();
									ApplicationLauncher.logger
											.debug("updateOperationProcessDatabaseId : idValue: " + idValue);
									ref_tvTestFilterDataList.getItems().get(displayIndex).getOperationProcessDataList()
											.get(displayProcessIndex).setId(idValue);
								}
							}

						}
					}

				}

			}

		}

	}

	@FXML
	public void btnTestFilterDataSaveOnClick() {
		ApplicationLauncher.logger.debug("btnTestFilterDataSaveOnClick: Entry");

		boolean status = validateTestFilterCellDataEntry();
		if (status) {

			getLastSavedTestFilterDataCellPositionHashMap().clear();
			getTestFilterDataCellPositionHashMap().forEach((key, value) -> {
				updateLastSavedTestFilterDataCellPositionHashMap(key, value);
				ApplicationLauncher.logger
						.debug("btnTestFilterDataSaveOnClick: updateLastSavedTestFilterDataCellPositionHashMap: key:"
								+ key + " -> " + value);
			});

			ReportProfileTestDataFilter testFilterData = fetchTestFilterGuiData();
			updateTableViewTestFilterDataList(testFilterData);
			// updateOperationProcessDatabaseId();

			setInsertDataMode(false);
			ref_gridPaneControl.setDisable(false);
			ref_titledPaneTestFilterData.setDisable(true);
			ref_cmbBxTestType.getSelectionModel().select(ConstantReportV2.NONE_DISPLAYED);
			ref_tabMeterProfileDataList.setDisable(false);
			ref_tabTestFilterDataList.setDisable(false);
		}

	}

	private void updateTableViewTestFilterDataList(ReportProfileTestDataFilter testFilterData) {

		ApplicationLauncher.logger.debug("updateTableViewTestFilterDataList: Entry");
		if (isInsertDataMode()) {

			ref_tvTestFilterDataList.getItems().add(testFilterData);
		} else {
			int selectedIndex = ref_tvTestFilterDataList.getSelectionModel().getSelectedIndex();
			ref_tvTestFilterDataList.getItems().set(selectedIndex, testFilterData);

		}

		// GUIUtils.autoResizeColumns(ref_tvTestFilterDataList,false);
		ref_tvTestFilterDataList.autosize();
		reOrderSerialNumberTestFilter(ref_tvTestFilterDataList);
		// ref_tvTestFilterDataList.size
	}

	private ReportProfileTestDataFilter fetchTestFilterGuiData() {

		ApplicationLauncher.logger.debug("fetchTestFilterGuiData: Entry");

		ReportProfileTestDataFilter testFilterDataFetchGromGui = new ReportProfileTestDataFilter();
		if (!isInsertDataMode()) {
			ApplicationLauncher.logger.debug("fetchTestFilterGuiData: Edit Mode");
			testFilterDataFetchGromGui = ref_tvTestFilterDataList.getSelectionModel().getSelectedItem();
			// ApplicationLauncher.logger.debug("fetchTestFilterGuiData: test1: size: " +
			// testFilterDataFetchGromGui.getOperationProcessDataList().size());

			/*
			 * for(int i=0; i <
			 * testFilterDataFetchGromGui.getOperationProcessDataList().size();i++){
			 * OperationProcess operationProcessKey =
			 * testFilterDataFetchGromGui.getOperationProcessDataList().get(i);
			 * ApplicationLauncher.logger.
			 * debug("fetchTestFilterGuiData: test1: getOperationProcessKey: " +
			 * operationProcessKey.getOperationProcessKey());
			 * ApplicationLauncher.logger.
			 * debug("fetchTestFilterGuiData: test1 :getOperationProcessDataType: " +
			 * operationProcessKey.getOperationProcessDataType());
			 * 
			 * }
			 */
		}
		// testFilterData.setSerialNo("1");
		testFilterDataFetchGromGui.setTableSerialNo("1");
		// testFilterDataFetchGromGui.setId(id);
		testFilterDataFetchGromGui.setFilterActive(ref_chkBxEnableFilter.isSelected());
		testFilterDataFetchGromGui.setPopulateResultStatus(ref_chkBxPopulateDutErrorResultStatus.isSelected());
		testFilterDataFetchGromGui.setReplicateData(ref_chkBxReplicateData.isSelected());
		/*
		 * testFilterDataFetchGromGui.setPopulateHeader1(ref_chkBxPopulateHeader1.
		 * isSelected());
		 * testFilterDataFetchGromGui.setPopulateHeader2(ref_chkBxPopulateHeader2.
		 * isSelected());
		 * testFilterDataFetchGromGui.setPopulateHeader3(ref_chkBxPopulateHeader3.
		 * isSelected());
		 * 
		 * testFilterDataFetchGromGui.setPopulateHeaderTestPeriodInMinutes(
		 * ref_chkBxPopulateHeaderTestPeriod.isSelected());
		 * testFilterDataFetchGromGui.setPopulateHeaderWarmupPeriodInMinutes(
		 * ref_chkBxPopulateHeaderWarmupPeriod.isSelected());
		 * testFilterDataFetchGromGui.setPopulateHeaderActualVoltage(
		 * ref_chkBxPopulateHeaderActualVoltage.isSelected());
		 * testFilterDataFetchGromGui.setPopulateHeaderActualCurrent(
		 * ref_chkBxPopulateHeaderActualCurrent.isSelected());
		 * testFilterDataFetchGromGui.setPopulateHeaderActualPf(
		 * ref_chkBxPopulateHeaderActualPf.isSelected());
		 * testFilterDataFetchGromGui.setPopulateHeaderActualFreq(
		 * ref_chkBxPopulateHeaderActualFreq.isSelected());
		 * testFilterDataFetchGromGui.setPopulateHeaderActualEnergy(
		 * ref_chkBxPopulateHeaderActualEnergy.isSelected());
		 */

		testFilterDataFetchGromGui.setOperationNoneSelected(ref_rdBtnOperationNone.isSelected());
		testFilterDataFetchGromGui.setOperationInputSelected(ref_rdBtnOperationInput.isSelected());
		testFilterDataFetchGromGui.setOperationOutputSelected(ref_rdBtnOperationOutput.isSelected());
		testFilterDataFetchGromGui.processOperationMode();
		testFilterDataFetchGromGui.setPageNumber(ref_txtTestFilterPageNumber.getText());
		testFilterDataFetchGromGui.setTestFilterDataPopulateType(
				ref_cmbBxTestFilterDataPopulateType.getSelectionModel().getSelectedItem().toString());
		// String
		// headeref_cmbBxExecutionResultType.getSelectionModel().getSelectedItem().toString();
		testFilterDataFetchGromGui.setTestExecutionResultTypeSelected(
				ref_cmbBxExecutionResultType.getSelectionModel().getSelectedItem().toString());
		testFilterDataFetchGromGui.setOperationSourceResultTypeSelected(
				ref_cmbBxOperationSourceParamType.getSelectionModel().getSelectedItem().toString());
		testFilterDataFetchGromGui
				.setReportBaseTemplate(ref_cmbBxBaseTemplate.getSelectionModel().getSelectedItem().toString());
		/*
		 * String testTypeSelected =
		 * ref_cmbBxTestType.getSelectionModel().getSelectedItem().toString();
		 * String testTypeAlias =
		 * ConstantReport.TEST_TYPE_ALIAS_HASH_MAP.get(testTypeSelected) ;
		 * testFilterData.setTestTypeSelected(ref_cmbBxTestType.getSelectionModel().
		 * getSelectedItem().toString());
		 */
		testFilterDataFetchGromGui.setTestFilterName(ref_txtFilterName.getText());
		testFilterDataFetchGromGui.setHeader1_Value(ref_txtHeader1Value.getText());
		testFilterDataFetchGromGui.setHeader2_Value(ref_txtHeader2Value.getText());
		testFilterDataFetchGromGui.setHeader3_Value(ref_txtHeader3Value.getText());

		testFilterDataFetchGromGui.setHeader4_Value(ref_txtHeader4Value.getText());
		testFilterDataFetchGromGui.setHeader5_Value(ref_txtHeader5Value.getText());

		testFilterDataFetchGromGui.setVoltPercentFilterUserEntry(ref_txtVoltPercentFilterUserEntry.getText());
		testFilterDataFetchGromGui.setCurrentPercentFilterUserEntry(ref_txtCurrentPercentFilterUserEntry.getText());
		testFilterDataFetchGromGui.setPfFilterUserEntry(ref_txtPfFilterUserEntry.getText());
		testFilterDataFetchGromGui.setFreqFilterUserEntry(ref_txtFreqFilterUserEntry.getText());
		testFilterDataFetchGromGui.setEnergyFilterUserEntry(ref_txtEnergyFilterUserEntry.getText());
		testFilterDataFetchGromGui.setIterationReadingStartIdUserEntry(ref_txtIterationReadingStartingId.getText());
		testFilterDataFetchGromGui.setIterationReadingEndIdUserEntry(ref_txtIterationReadingEndingId.getText());

		testFilterDataFetchGromGui.setVoltFilterUnitValue(
				ref_cmbBxVoltPercentFilterUserEntry.getSelectionModel().getSelectedItem().toString());
		testFilterDataFetchGromGui.setCurrentFilterUnitValue(
				ref_cmbBxCurrentPercentFilterUserEntry.getSelectionModel().getSelectedItem().toString());
		testFilterDataFetchGromGui
				.setPfFilterUnitValue(ref_cmbBxPfFilterUserEntry.getSelectionModel().getSelectedItem().toString());
		testFilterDataFetchGromGui
				.setFreqFilterUnitValue(ref_cmbBxFreqFilterUserEntry.getSelectionModel().getSelectedItem().toString());
		testFilterDataFetchGromGui.setEnergyFilterUnitValue(
				ref_cmbBxEnergyFilterUserEntry.getSelectionModel().getSelectedItem().toString());

		testFilterDataFetchGromGui.setVoltPercentFilterData(ref_txtVoltPercentFilterData.getText());
		testFilterDataFetchGromGui.setCurrentPercentFilterData(ref_txtCurrentPercentFilterData.getText());
		testFilterDataFetchGromGui.setPfFilterData(ref_txtPfFilterData.getText());
		testFilterDataFetchGromGui.setFreqFilterData(ref_txtFreqFilterData.getText());
		testFilterDataFetchGromGui.setEnergyFilterData(ref_txtEnergyFilterData.getText());
		testFilterDataFetchGromGui.setIterationReadingPrefixValue(ref_txt_IterationIdHeaderPrefix.getText());

		testFilterDataFetchGromGui.setHeader3_VoltFilterSelected(ref_chkBxHeader3VoltageFilter.isSelected());
		testFilterDataFetchGromGui.setHeader3_CurrentFilterSelected(ref_chkBxHeader3CurrentFilter.isSelected());
		testFilterDataFetchGromGui.setHeader3_PfFilterSelected(ref_chkBxHeader3PfFilter.isSelected());
		testFilterDataFetchGromGui.setHeader3_FreqFilterSelected(ref_chkBxHeader3FreqFilter.isSelected());
		testFilterDataFetchGromGui.setHeader3_EnergyFilterSelected(ref_chkBxHeader3EnergyFilter.isSelected());
		// testFilterDataFetchGromGui.setHeader3_IterationReadingIdSelected(ref_chkBxHeader3IterationIdFilter.isSelected());
		testFilterDataFetchGromGui
				.setHeader3_IterationReadingIdSelected(ref_chkBx_IterationIdHeaderPrefix.isSelected());
		testFilterDataFetchGromGui.setHeader3_CustomSelected(ref_chkBxHeader3CustomAllowed.isSelected());
		testFilterDataFetchGromGui.setPopulateIterationHeaderSelected(ref_chkBx_IterationIdHeaderPrefix.isSelected());

		testFilterDataFetchGromGui.setReplicateCountValue(
				ref_cmbBxReplicateCountUserEntry.getSelectionModel().getSelectedItem().toString());
		testFilterDataFetchGromGui.setOperationInputKey(
				ref_cmbBxOperationInputUserEntry.getSelectionModel().getSelectedItem().toString());
		testFilterDataFetchGromGui.setUserCommentValue(ref_txtFilterComments.getText());
		testFilterDataFetchGromGui.setCustomTestNameUserEntry(ref_txtCustomTestNameUserEntry.getText());
		testFilterDataFetchGromGui.setCustomTestNameData(ref_txtCustomTestNameData.getText());

		testFilterDataFetchGromGui.setOperationCompareLimitsSelected(ref_chkBxCompareWithLimits.isSelected());
		testFilterDataFetchGromGui.setOperationMergeLimitsSelected(ref_chkBxMergeUpperLowerLimit.isSelected());
		// Collection<String> operationProcessInputKeyList =
		// ref_listViewInputProcessList.getItems();
		ArrayList<String> tempList = new ArrayList<String>();
		// operationProcessInputKeyList.toArray(tempList);
		// testFilterData.setOperationProcessInputKeyList(tempList);

		// ApplicationLauncher.logger.debug("fetchTestFilterGuiData:
		// getOperationProcessInputKeyList: " +
		// testFilterData.getOperationProcessInputKeyList());

		// testFilterData.setOperationProcessMethod(ref_cmbBxOperationCriteriaProcessData.getSelectionModel().getSelectedItem().toString());

		// testFilterData.setOperationProcessMasterOutputKey(ref_cmbBxOperationCriteriaMasterOutputData.getSelectionModel().getSelectedItem().toString());

		// ApplicationLauncher.logger.debug("fetchTestFilterGuiData: test1A: size: " +
		// testFilterDataFetchGromGui.getOperationProcessDataList().size());

		testFilterDataFetchGromGui.setOperationProcessLocalOutputKey(
				ref_cmbBxOperationCriteriaLocalOutputData.getSelectionModel().getSelectedItem().toString());
		testFilterDataFetchGromGui.setOperationProcessLocalResult("");
		testFilterDataFetchGromGui.setOperationProcessLocalComparedStatus(
				ref_cmbBxOperationComparedLocalResultStatusOutput.getSelectionModel().getSelectedItem().toString());

		testFilterDataFetchGromGui.setOperationProcessMasterComparedStatus(
				ref_cmbBxOperationComparedMasterResultStatusOutput.getSelectionModel().getSelectedItem().toString());
		// testFilterDataFetchGromGui.setOperationProcessMasterComparedStatus(ConstantReportV2.POPULATE_MASTER_OUTPUT_STATUS_KEY);
		testFilterDataFetchGromGui.setOperationProcessLocalUpperLimit(ref_txtAllowedUpperLimit.getText());
		testFilterDataFetchGromGui.setOperationProcessLocalLowerLimit(ref_txtAllowedLowerLimit.getText());

		ApplicationLauncher.logger.debug("fetchTestFilterGuiData: ref_chkBxPopulateMasterOutputData: "
				+ ref_chkBxPopulateMasterOutputData.isSelected());
		testFilterDataFetchGromGui.setPopulateOperationMasterOutput(ref_chkBxPopulateMasterOutputData.isSelected());
		testFilterDataFetchGromGui.setPopulateOperationLocalOutput(ref_chkBxPopulateLocalOutputData.isSelected());
		testFilterDataFetchGromGui.setPopulateOperationUpperLimit(ref_chkBxPopulateUpperLimitData.isSelected());
		testFilterDataFetchGromGui.setPopulateOperationLowerLimit(ref_chkBxPopulateLowerLimitData.isSelected());
		testFilterDataFetchGromGui
				.setPopulateOperationComparedLocalStatus(ref_chkBxPopulateComparedLocalResultStatus.isSelected());

		ApplicationLauncher.logger.debug("fetchTestFilterGuiData: ref_chkBxPopulateComparedMasterResultStatus: "
				+ ref_chkBxPopulateComparedMasterResultStatus.isSelected());
		testFilterDataFetchGromGui
				.setPopulateOperationComparedMasterStatus(ref_chkBxPopulateComparedMasterResultStatus.isSelected());

		Collection<RpPrintPosition> testDataCellList = ref_tvTestFilterCellStartPosition.getItems();
		ArrayList<RpPrintPosition> testFilterDataCellList = new ArrayList<RpPrintPosition>(testDataCellList);
		// testFilterData.setTestFilterDataCellList(tempListExcelModelList);
		// ApplicationLauncher.logger.debug("fetchTestFilterGuiData:
		// getTestFilterDataCellList: " + testFilterData.getTestFilterDataCellList());

		Collection<RpPrintPosition> hDataCellList = ref_tvTestFilterCellHeaderPosition.getItems();
		ArrayList<RpPrintPosition> headerDataCellList = new ArrayList<RpPrintPosition>(hDataCellList);
		// tempListExcelModelList = new ArrayList<RpPrintPosition> (headerDataCellList);
		// testFilterData.setHeaderDataCellList(tempListExcelModelList);
		// ApplicationLauncher.logger.debug("fetchTestFilterGuiData:
		// getHeaderDataCellList: " + testFilterData.getHeaderDataCellList());

		ApplicationLauncher.logger
				.debug("fetchTestFilterGuiData: TEST_TYPE_ALIAS_HASH_MAP: " + ConstantReport.TEST_TYPE_ALIAS_HASH_MAP);
		String selectedTestTypeKey = ref_cmbBxTestType.getSelectionModel().getSelectedItem().toString();

		testFilterDataFetchGromGui.setTestTypeSelected(selectedTestTypeKey);

		String selectedSubTestType = "";
		if (!ref_cmbBxTestTypeSub.isDisabled()) {
			selectedSubTestType = ref_cmbBxTestTypeSub.getSelectionModel().getSelectedItem().toString();
		}
		testFilterDataFetchGromGui.setSubTestTypeSelected(selectedSubTestType);
		try {

			ApplicationLauncher.logger.debug("fetchTestFilterGuiData: selectedTestTypeKey: " + selectedTestTypeKey);
			ApplicationLauncher.logger.debug("fetchTestFilterGuiData: TEST_TYPE_ALIAS_HASH_MAP2: "
					+ ConstantReport.TEST_TYPE_ALIAS_HASH_MAP.get(selectedTestTypeKey));
			// ArrayList<String> selectedTestTypeAlias =
			// Collection<String> selectedTestTypeAlias =
			// ConstantReport.TEST_TYPE_ALIAS_HASH_MAP.get(selectedTestTypeKey);
			// testFilterData.setTestTypeAlias(selectedTestTypeAlias.iterator().next());
			String selectedTestTypeAlias = ConstantReport.TEST_TYPE_ALIAS_HASH_MAP.get(selectedTestTypeKey);

			if (selectedTestTypeKey.equals(ConstantApp.DISPLAY_TC_TITLE_PHASE_REVERSAL)) {

				String selectedTestTypeSub = ref_cmbBxTestTypeSub.getSelectionModel().getSelectedItem().toString();
				if (selectedTestTypeSub.equals(RPS_SUB_NORMAL)) {
					selectedTestTypeAlias = ConstantApp.PHASEREVERSAL_NORMAL_ALIAS_NAME;
				} else if (selectedTestTypeSub.equals(RPS_SUB_REVERSE)) {
					selectedTestTypeAlias = ConstantApp.PHASEREVERSAL_REV_ALIAS_NAME;
				}
			}

			testFilterDataFetchGromGui.setTestTypeAlias(selectedTestTypeAlias);
		} catch (Exception e) {
			e.printStackTrace();
			testFilterDataFetchGromGui.setTestTypeAlias("");
			ApplicationLauncher.logger.error("fetchTestFilterGuiData: Exception: " + e.getMessage());
		}
		ApplicationLauncher.logger
				.debug("fetchTestFilterGuiData: getTestTypeAlias: " + testFilterDataFetchGromGui.getTestTypeAlias());

		String operationCriteriaMasterOutputDataSelectedKey = ref_cmbBxOperationCriteriaMasterOutputData
				.getSelectionModel().getSelectedItem().toString();
		// ApplicationLauncher.logger.debug("fetchTestFilterGuiData: test1B: size: " +
		// testFilterDataFetchGromGui.getOperationProcessDataList().size());

		OperationProcess operationProcessMasterOutputData = new OperationProcess();
		// OperationProcessDataHashMapModel operationProcessDataHashMapMasterOutData =
		// new OperationProcessDataHashMapModel();

		/*
		 * testFilterData.setOperationProcessMasterOutputKey(
		 * operationCriteriaMasterOutputDataSelectedKey);
		 * 
		 * testFilterData.setOperationProcessMasterResult("");
		 * testFilterData.setOperationProcessMasterComparedStatus(
		 * ref_cmbBxOperationComparedResultStatusOutput.getSelectionModel().
		 * getSelectedItem().toString());;
		 * testFilterData.setOperationProcessMasterUpperLimit(ref_txtAllowedUpperLimit.
		 * getText());
		 * testFilterData.setOperationProcessMasterLowerLimit(ref_txtAllowedLowerLimit.
		 * getText());
		 */
		// #DebugOperationProcess
		operationProcessMasterOutputData.setOperationProcessKey(operationCriteriaMasterOutputDataSelectedKey);
		operationProcessMasterOutputData.setResultValue("");
		operationProcessMasterOutputData.setLocalComparedStatus(
				ref_cmbBxOperationComparedLocalResultStatusOutput.getSelectionModel().getSelectedItem().toString());
		operationProcessMasterOutputData.setMasterComparedStatus(
				ref_cmbBxOperationComparedMasterResultStatusOutput.getSelectionModel().getSelectedItem().toString());
		operationProcessMasterOutputData.setUpperLimit(ref_txtAllowedUpperLimit.getText());
		operationProcessMasterOutputData.setLowerLimit(ref_txtAllowedLowerLimit.getText());
		operationProcessMasterOutputData
				.setOperationProcessDataType(ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_MASTER_OUTPUT);
		operationProcessMasterOutputData.setPopulateOnlyHeaders(false);
		// operationProcessDataHashMapMasterOutData.addOperationProcessData(operationCriteriaMasterOutputDataSelectedKey,operationProcessMasterOutpuData);

		String upperLimitCellPosition = "";
		String lowerLimitCellPosition = "";
		String resultValueCellPosition = "";
		String resultStatusCellPosition = "";
		String headerKey = "";
		if (ref_rdBtnOperationOutput.isSelected()) {
			// if(ref_chkBxPopulateMasterOutputData.isSelected()){
			if (!ref_chkBxPopulateMasterOutputData.isDisabled()) {

				if (OPERATION_INPUT_HEADERS_ONLY_DATA_TYPE_LIST
						.contains(testFilterDataFetchGromGui.getTestExecutionResultTypeSelected())) {
					// testFilterData.setOperationProcessMasterOutputOnlyHeader(true);
					// #DebugOperationProcess
					operationProcessMasterOutputData.setPopulateOnlyHeaders(true);
				}

				if (ref_chkBxPopulateMasterOutputData.isSelected()) {
					testFilterDataFetchGromGui.setResultDataType(OPERATION_MASTER_OUTPUT_DATA_HEADER_DISPLAY_PREFIX);

				} else {
					if (ref_chkBxPopulateUpperLimitData.isSelected()) {
						testFilterDataFetchGromGui
								.setResultDataType(OPERATION_MASTER_OUTPUT_DATA_HEADER_DISPLAY_PREFIX);
					} else if (ref_chkBxPopulateLowerLimitData.isSelected()) {
						testFilterDataFetchGromGui
								.setResultDataType(OPERATION_MASTER_OUTPUT_DATA_HEADER_DISPLAY_PREFIX);
					} else if (ref_chkBxPopulateComparedLocalResultStatus.isSelected()) {
						testFilterDataFetchGromGui
								.setResultDataType(OPERATION_MASTER_OUTPUT_STATUS_HEADER_DISPLAY_PREFIX);
					} else {
						testFilterDataFetchGromGui
								.setResultDataType(OPERATION_MASTER_OUTPUT_DATA_HEADER_DISPLAY_PREFIX);
					}
				}

				resultValueCellPosition = "";
				resultStatusCellPosition = "";
				for (int i = 0; i < testFilterDataCellList.size(); i++) {
					// headerKey = testFilterData.getTestFilterDataCellList().get(i).getheader();
					headerKey = testFilterDataCellList.get(i).getKeyParam();
					if (headerKey.startsWith(OPERATION_MASTER_OUTPUT_DATA_HEADER_DISPLAY_PREFIX)) {
						// resultValueCellPosition =
						// testFilterData.getTestFilterDataCellList().get(i).getcell_value();
						resultValueCellPosition = testFilterDataCellList.get(i).getCellPosition();
					} else if (headerKey.startsWith(OPERATION_MASTER_OUTPUT_STATUS_HEADER_DISPLAY_PREFIX)) {
						// resultStatusCellPosition =
						// testFilterData.getTestFilterDataCellList().get(i).getcell_value();
						resultStatusCellPosition = testFilterDataCellList.get(i).getCellPosition();
					}
				}
				// ApplicationLauncher.logger.debug("fetchTestFilterGuiData: test1C: size: " +
				// testFilterDataFetchGromGui.getOperationProcessDataList().size());

				if (!resultValueCellPosition.isEmpty()) {
					testFilterDataFetchGromGui.setResultValueCellPosition(resultValueCellPosition);
				}
				if (!resultStatusCellPosition.isEmpty()) {
					testFilterDataFetchGromGui.setResultStatusCellPosition(resultStatusCellPosition);
				}

				upperLimitCellPosition = "";
				lowerLimitCellPosition = "";

				for (int i = 0; i < headerDataCellList.size(); i++) {
					// headerKey = testFilterData.getHeaderDataCellList().get(i).getheader();
					headerKey = headerDataCellList.get(i).getKeyParam();
					if (headerKey.equals(ConstantReportV2.POPULATE_MASTER_UPPER_LIMIT_KEY)) {
						// upperLimitCellPosition =
						// testFilterData.getHeaderDataCellList().get(i).getcell_value();
						upperLimitCellPosition = headerDataCellList.get(i).getCellPosition();
					} else if (headerKey.equals(ConstantReportV2.POPULATE_MASTER_LOWER_LIMIT_KEY)) {
						// lowerLimitCellPosition =
						// testFilterData.getHeaderDataCellList().get(i).getcell_value();
						lowerLimitCellPosition = headerDataCellList.get(i).getCellPosition();
					} else if (headerKey.equals(ConstantReportV2.POPULATE_MASTER_MERGED_LIMIT_KEY)) {
						// upperLimitCellPosition =
						// testFilterData.getHeaderDataCellList().get(i).getcell_value();
						upperLimitCellPosition = headerDataCellList.get(i).getCellPosition();
					}
				}
				if (!upperLimitCellPosition.isEmpty()) {
					testFilterDataFetchGromGui.setResultUpperLimitCellPosition(upperLimitCellPosition);
				}
				if (!lowerLimitCellPosition.isEmpty()) {
					testFilterDataFetchGromGui.setResultLowerLimitCellPosition(lowerLimitCellPosition);
				}
				// ApplicationLauncher.logger.debug("fetchTestFilterGuiData: test1D: size: " +
				// testFilterDataFetchGromGui.getOperationProcessDataList().size());

				// }else if(ref_chkBxPopulateLocalOutputData.isSelected()){
			} else if (!ref_chkBxPopulateLocalOutputData.isDisabled()) {
				/*
				 * if(ref_chkBxPopulateLocalOutputData.isSelected()){
				 * testFilterData.setResultDataTypeCellPosition(
				 * OPERATION_LOCAL_OUTPUT_DATA_HEADER_DISPLAY_PREFIX);
				 * }
				 */

				if (OPERATION_INPUT_HEADERS_ONLY_DATA_TYPE_LIST
						.contains(testFilterDataFetchGromGui.getTestExecutionResultTypeSelected())) {
					testFilterDataFetchGromGui.setOperationProcessLocalOutputOnlyHeader(true);
				}

				if (ref_chkBxPopulateLocalOutputData.isSelected()) {
					testFilterDataFetchGromGui.setResultDataType(OPERATION_LOCAL_OUTPUT_DATA_HEADER_DISPLAY_PREFIX);

				} else {
					if (ref_chkBxPopulateUpperLimitData.isSelected()) {
						testFilterDataFetchGromGui.setResultDataType(OPERATION_LOCAL_OUTPUT_DATA_HEADER_DISPLAY_PREFIX);
					} else if (ref_chkBxPopulateLowerLimitData.isSelected()) {
						testFilterDataFetchGromGui.setResultDataType(OPERATION_LOCAL_OUTPUT_DATA_HEADER_DISPLAY_PREFIX);
					} else if (ref_chkBxPopulateComparedLocalResultStatus.isSelected()) {
						testFilterDataFetchGromGui
								.setResultDataType(OPERATION_LOCAL_OUTPUT_STATUS_HEADER_DISPLAY_PREFIX);
					} else {
						testFilterDataFetchGromGui.setResultDataType(OPERATION_LOCAL_OUTPUT_DATA_HEADER_DISPLAY_PREFIX);
					}
				}
				resultValueCellPosition = "";
				resultStatusCellPosition = "";
				for (int i = 0; i < testFilterDataCellList.size(); i++) {
					// headerKey = testFilterData.getTestFilterDataCellList().get(i).getheader();
					headerKey = testFilterDataCellList.get(i).getKeyParam();
					if (headerKey.startsWith(OPERATION_LOCAL_OUTPUT_DATA_HEADER_DISPLAY_PREFIX)) {
						// resultValueCellPosition =
						// testFilterData.getTestFilterDataCellList().get(i).getcell_value();
						resultValueCellPosition = testFilterDataCellList.get(i).getCellPosition();
					} else if (headerKey.startsWith(OPERATION_LOCAL_OUTPUT_STATUS_HEADER_DISPLAY_PREFIX)) {
						// resultStatusCellPosition =
						// testFilterData.getTestFilterDataCellList().get(i).getcell_value();
						resultStatusCellPosition = testFilterDataCellList.get(i).getCellPosition();
					}
				}
				if (!resultValueCellPosition.isEmpty()) {
					testFilterDataFetchGromGui.setResultValueCellPosition(resultValueCellPosition);
				}
				if (!resultStatusCellPosition.isEmpty()) {
					testFilterDataFetchGromGui.setResultStatusCellPosition(resultStatusCellPosition);
				}

				upperLimitCellPosition = "";
				lowerLimitCellPosition = "";
				for (int i = 0; i < headerDataCellList.size(); i++) {
					// headerKey = testFilterData.getHeaderDataCellList().get(i).getheader();
					headerKey = headerDataCellList.get(i).getKeyParam();
					if (headerKey.equals(ConstantReportV2.POPULATE_LOCAL_UPPER_LIMIT_KEY)) {
						// upperLimitCellPosition =
						// testFilterData.getHeaderDataCellList().get(i).getcell_value();
						upperLimitCellPosition = headerDataCellList.get(i).getCellPosition();
					} else if (headerKey.equals(ConstantReportV2.POPULATE_LOCAL_LOWER_LIMIT_KEY)) {
						// lowerLimitCellPosition =
						// testFilterData.getHeaderDataCellList().get(i).getcell_value();
						lowerLimitCellPosition = headerDataCellList.get(i).getCellPosition();
					} else if (headerKey.equals(ConstantReportV2.POPULATE_LOCAL_MERGED_LIMIT_KEY)) {
						// upperLimitCellPosition =
						// testFilterData.getHeaderDataCellList().get(i).getcell_value();
						upperLimitCellPosition = headerDataCellList.get(i).getCellPosition();
					}
				}
				if (!upperLimitCellPosition.isEmpty()) {
					testFilterDataFetchGromGui.setResultUpperLimitCellPosition(upperLimitCellPosition);
				}
				if (!lowerLimitCellPosition.isEmpty()) {
					testFilterDataFetchGromGui.setResultLowerLimitCellPosition(lowerLimitCellPosition);
				}

				// ApplicationLauncher.logger.debug("fetchTestFilterGuiData: test1E: size: " +
				// testFilterDataFetchGromGui.getOperationProcessDataList().size());

			} /*
				 * else{
				 * testFilterData.setResultDataTypeCellPosition("Error");
				 * for(int i =0 ; i< testFilterData.getTestFilterDataCellList().size(); i++){
				 * headerKey = testFilterData.getTestFilterDataCellList().get(i).getheader();
				 * if(headerKey.equals(CELL_START_POSITION_HEADER_RESULT_DATA_KEY)){
				 * resultValueCellPosition =
				 * testFilterData.getTestFilterDataCellList().get(i).getcell_value();
				 * }else if(headerKey.equals(CELL_START_POSITION_HEADER_RESULT_STATUS_KEY)){
				 * resultStatusCellPosition =
				 * testFilterData.getTestFilterDataCellList().get(i).getcell_value();
				 * }
				 * }
				 * 
				 * 
				 * testFilterData.setResultValueCellPosition(resultValueCellPosition);
				 * testFilterData.setResultStatusCellPosition(resultStatusCellPosition);
				 * }
				 */
		} else {
			testFilterDataFetchGromGui.setResultDataType("Error");
			for (int i = 0; i < testFilterDataCellList.size(); i++) {
				// headerKey = testFilterData.getTestFilterDataCellList().get(i).getheader();
				headerKey = testFilterDataCellList.get(i).getKeyParam();
				/*
				 * if(headerKey.equals(ConstantReportV2.
				 * CELL_START_POSITION_HEADER_RESULT_DATA_KEY)){
				 * //resultValueCellPosition =
				 * testFilterData.getTestFilterDataCellList().get(i).getcell_value();
				 * resultValueCellPosition = testFilterDataCellList.get(i).getCellPosition();
				 * }else
				 */

				if (headerKey.equals(ConstantReportV2.RESULT_SOURCE_TYPE_RESULT_STATUS_KEY)) {
					// resultStatusCellPosition =
					// testFilterData.getTestFilterDataCellList().get(i).getcell_value();
					resultStatusCellPosition = testFilterDataCellList.get(i).getCellPosition();
				} /*
					 * else if(headerKey.equals(CELL_HEADER_POSITION_HEADER_RESULT_RSM_INITIAL)){
					 * //resultStatusCellPosition =
					 * testFilterData.getTestFilterDataCellList().get(i).getcell_value();
					 * resultValueCellPosition = testFilterDataCellList.get(i).getCellPosition();
					 * }else if(headerKey.equals(CELL_HEADER_POSITION_HEADER_RESULT_RSM_FINAL)){
					 * //resultStatusCellPosition =
					 * testFilterData.getTestFilterDataCellList().get(i).getcell_value();
					 * resultValueCellPosition = testFilterDataCellList.get(i).getCellPosition();
					 * }
					 */

			}

			testFilterDataFetchGromGui.setResultValueCellPosition(resultValueCellPosition);
			testFilterDataFetchGromGui.setResultStatusCellPosition(resultStatusCellPosition);
		}
		// ApplicationLauncher.logger.debug("fetchTestFilterGuiData: test1F: size: " +
		// testFilterDataFetchGromGui.getOperationProcessDataList().size());

		operationProcessMasterOutputData.setAddedToInputProcess(false);
		// testFilterData.setOperationProcessMasterOutputDataSet(operationProcessMasterOutpuData);
		if (isInsertDataMode()) {
			testFilterDataFetchGromGui.addOperationProcessDataList(operationProcessMasterOutputData);
		} else {
			// ApplicationLauncher.logger.debug("fetchTestFilterGuiData: test2: size: " +
			// testFilterDataFetchGromGui.getOperationProcessDataList().size());
			/*
			 * for(int j=0; j <
			 * testFilterDataFetchGromGui.getOperationProcessDataList().size();j++){
			 * OperationProcess operationProcessKey =
			 * testFilterDataFetchGromGui.getOperationProcessDataList().get(j);
			 * ApplicationLauncher.logger.
			 * debug("fetchTestFilterGuiData: test2: getOperationProcessKey: " +
			 * operationProcessKey.getOperationProcessKey());
			 * if(operationProcessKey.getOperationProcessDataType()!=null){
			 * ApplicationLauncher.logger.
			 * debug("fetchTestFilterGuiData: test2: getOperationProcessDataType: " +
			 * operationProcessKey.getOperationProcessDataType());
			 * if(operationProcessKey.getOperationProcessDataType().equals(ConstantReportV2.
			 * OPERATION_PROCESS_DATA_TYPE_MASTER_OUTPUT)){
			 * ApplicationLauncher.logger.
			 * debug("fetchTestFilterGuiData: test2: getOperationProcessDataType: removed");
			 * testFilterDataFetchGromGui.getOperationProcessDataList().remove(j);
			 * }
			 * }
			 * }
			 */

			testFilterDataFetchGromGui.getOperationProcessDataList().removeIf(e -> e.getOperationProcessDataType()
					.equals(ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_MASTER_OUTPUT));

			testFilterDataFetchGromGui.addOperationProcessDataList(operationProcessMasterOutputData);
		}

		String header1CellPosition = "";
		String header2CellPosition = "";
		String header3CellPosition = "";
		String headerTestPeriodInMinutesCellPosition = "";
		String headerWarmupPeriodInMinutesCellPosition = "";
		String headerActualVoltCellPosition = "";
		String headerActualCurrentCellPosition = "";
		String headerActualPfCellPosition = "";
		String headerActualFreqCellPosition = "";
		String headerActualEnergyCellPosition = "";

		for (int i = 0; i < headerDataCellList.size(); i++) {
			// headerKey = testFilterData.getHeaderDataCellList().get(i).getheader();
			headerKey = headerDataCellList.get(i).getKeyParam();
			if (headerKey.equals(ConstantReportV2.POPULATE_HEADER1_KEY)) {
				// header1CellPosition =
				// testFilterData.getHeaderDataCellList().get(i).getcell_value();
				header1CellPosition = headerDataCellList.get(i).getCellPosition();
			} else if (headerKey.equals(ConstantReportV2.POPULATE_HEADER2_KEY)) {
				// header2CellPosition =
				// testFilterData.getHeaderDataCellList().get(i).getcell_value();
				header2CellPosition = headerDataCellList.get(i).getCellPosition();
			} else if (headerKey.equals(ConstantReportV2.POPULATE_HEADER3_KEY)) {
				// header3CellPosition =
				// testFilterData.getHeaderDataCellList().get(i).getcell_value();
				header3CellPosition = headerDataCellList.get(i).getCellPosition();
			}
			/*
			 * else if(headerKey.equals(ConstantReportV2.
			 * CELL_HEADER_POSITION_HEADER_RESULT_RSM_INITIAL)){
			 * resultValueCellPosition = headerDataCellList.get(i).getCellPosition();
			 * testFilterDataFetchGromGui.setResultValueCellPosition(resultValueCellPosition
			 * );
			 * 
			 * }else if(headerKey.equals(ConstantReportV2.
			 * CELL_HEADER_POSITION_HEADER_RESULT_RSM_FINAL)){
			 * resultValueCellPosition = headerDataCellList.get(i).getCellPosition();
			 * testFilterDataFetchGromGui.setResultValueCellPosition(resultValueCellPosition
			 * );
			 * }
			 */

			else if (headerKey.equals(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_RSM_INITIAL)) {
				resultValueCellPosition = headerDataCellList.get(i).getCellPosition();
				testFilterDataFetchGromGui.setResultValueCellPosition(resultValueCellPosition);

			} else if (headerKey.equals(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_RSM_FINAL)) {
				resultValueCellPosition = headerDataCellList.get(i).getCellPosition();
				testFilterDataFetchGromGui.setResultValueCellPosition(resultValueCellPosition);
			} else if (headerKey.equals(ConstantReportV2.POPULATE_HEADER_KEY_TEST_PERIOD_IN_MINUTES)) {
				headerTestPeriodInMinutesCellPosition = headerDataCellList.get(i).getCellPosition();
			} else if (headerKey.equals(ConstantReportV2.POPULATE_HEADER_KEY_WARMUP_PERIOD_IN_MINUTES)) {
				headerWarmupPeriodInMinutesCellPosition = headerDataCellList.get(i).getCellPosition();
			} else if (headerKey.equals(ConstantReportV2.POPULATE_HEADER_KEY_TARGET_VOLTAGE)) {
				headerActualVoltCellPosition = headerDataCellList.get(i).getCellPosition();
			} else if (headerKey.equals(ConstantReportV2.POPULATE_HEADER_KEY_TARGET_CURRENT)) {
				headerActualCurrentCellPosition = headerDataCellList.get(i).getCellPosition();
			} else if (headerKey.equals(ConstantReportV2.POPULATE_HEADER_KEY_TARGET_PF)) {
				headerActualPfCellPosition = headerDataCellList.get(i).getCellPosition();
			} else if (headerKey.equals(ConstantReportV2.POPULATE_HEADER_KEY_TARGET_FREQ)) {
				headerActualFreqCellPosition = headerDataCellList.get(i).getCellPosition();
			} else if (headerKey.equals(ConstantReportV2.POPULATE_HEADER_KEY_TARGET_ENERGY)) {
				headerActualEnergyCellPosition = headerDataCellList.get(i).getCellPosition();
			}

		}
		testFilterDataFetchGromGui.setHeader1_CellPosition(header1CellPosition);
		testFilterDataFetchGromGui.setHeader2_CellPosition(header2CellPosition);
		testFilterDataFetchGromGui.setHeader3_CellPosition(header3CellPosition);
		testFilterDataFetchGromGui.setHeaderTestPeriodInMinutesCellPosition(headerTestPeriodInMinutesCellPosition);
		testFilterDataFetchGromGui.setHeaderWarmUpPeriodInMinutesCellPosition(headerWarmupPeriodInMinutesCellPosition);
		testFilterDataFetchGromGui.setHeaderActualVoltageCellPosition(headerActualVoltCellPosition);
		testFilterDataFetchGromGui.setHeaderActualCurrentCellPosition(headerActualCurrentCellPosition);
		testFilterDataFetchGromGui.setHeaderActualPfCellPosition(headerActualPfCellPosition);
		testFilterDataFetchGromGui.setHeaderActualFreqCellPosition(headerActualFreqCellPosition);
		testFilterDataFetchGromGui.setHeaderActualEnergyCellPosition(headerActualEnergyCellPosition);

		String nonDisplayedDataSet = "";
		testFilterDataFetchGromGui.setOperationProcessMethod(ConstantReportV2.NONE_DISPLAYED);
		if (ref_rdBtnOperationInput.isSelected()) {
			nonDisplayedDataSet = ref_cmbBxOperationInputUserEntry.getSelectionModel().getSelectedItem().toString();

		} else if (ref_rdBtnOperationOutput.isSelected()) {

			if (!operationCriteriaMasterOutputDataSelectedKey.equals(ConstantReportV2.NONE_DISPLAYED)) {
				if (!ref_chkBxPopulateMasterOutputData.isSelected()) {
					if (!chkBxPopulateComparedLocalResultStatus.isDisabled()) {
						if (!chkBxPopulateComparedLocalResultStatus.isSelected()) {
							nonDisplayedDataSet = ref_cmbBxOperationCriteriaMasterOutputData.getSelectionModel()
									.getSelectedItem().toString();
						}
					} else {
						nonDisplayedDataSet = ref_cmbBxOperationCriteriaMasterOutputData.getSelectionModel()
								.getSelectedItem().toString();
					}
				}
			} else {
				if (!ref_chkBxPopulateLocalOutputData.isSelected()) {
					if (!chkBxPopulateComparedLocalResultStatus.isDisabled()) {
						if (!chkBxPopulateComparedLocalResultStatus.isSelected()) {
							nonDisplayedDataSet = ref_cmbBxOperationCriteriaLocalOutputData.getSelectionModel()
									.getSelectedItem().toString();
						}
					} else {
						nonDisplayedDataSet = ref_cmbBxOperationCriteriaLocalOutputData.getSelectionModel()
								.getSelectedItem().toString();
					}
				}

			}

			testFilterDataFetchGromGui.setOperationProcessMethod(
					ref_cmbBxOperationCriteriaProcessData.getSelectionModel().getSelectedItem().toString());

			// Collection<String> operationProcessInputKeyList =
			// ref_listViewInputProcessList.getItems();
			// tempList = new ArrayList<String> (operationProcessInputKeyList);
			// testFilterData.setOperationProcessInputKeyList(tempList);

			Collection<String> addedKeyList = ref_listViewInputProcessList.getItems();
			tempList = new ArrayList<String>(addedKeyList);
			testFilterDataFetchGromGui.setOperationProcessInputKeyList(tempList);
			List<OperationProcess> operationProcessInputKeyList = new ArrayList<OperationProcess>();
			// testFilterDataFetchGromGui.clearOperationProcessDataListAddedToInputProcess();
			// ApplicationLauncher.logger.debug("fetchTestFilterGuiData: test3: size: " +
			// testFilterDataFetchGromGui.getOperationProcessDataList().size());

			/*
			 * for(int i=0; i <
			 * testFilterDataFetchGromGui.getOperationProcessDataList().size();i++){
			 * OperationProcess operationProcessKey =
			 * testFilterDataFetchGromGui.getOperationProcessDataList().get(i);
			 * ApplicationLauncher.logger.debug("fetchTestFilterGuiData: test3X: size: " +
			 * testFilterDataFetchGromGui.getOperationProcessDataList().size());
			 * ApplicationLauncher.logger.
			 * debug("fetchTestFilterGuiData: test3: getOperationProcessKey: " +
			 * operationProcessKey.getOperationProcessKey());
			 * if(operationProcessKey.isAddedToInputProcess()){
			 * ApplicationLauncher.logger.debug("fetchTestFilterGuiData: test3: removed");
			 * testFilterDataFetchGromGui.getOperationProcessDataList().remove(i);
			 * }
			 * ApplicationLauncher.logger.debug("fetchTestFilterGuiData: test3: end");
			 * }
			 */

			testFilterDataFetchGromGui.getOperationProcessDataList().removeIf(e -> e.isAddedToInputProcess() == true);

			for (int i = 0; i < tempList.size(); i++) {
				OperationProcess operationProcessInputKey = new OperationProcess();
				// ApplicationLauncher.logger.debug("fetchTestFilterGuiData: tempList1 key: " +
				// tempList.get(i));
				operationProcessInputKey.setOperationProcessKey(tempList.get(i));
				operationProcessInputKey.setAddedToInputProcess(true);
				operationProcessInputKey.setPopulateOnlyHeaders(false);
				operationProcessInputKey
						.setOperationProcessDataType(ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_INPUT);
				testFilterDataFetchGromGui.addOperationProcessDataList(operationProcessInputKey);
			}

			/*
			 * addedKeyList.stream()
			 * .forEach(e -> {
			 * OperationProcess operationProcessInputKey = new OperationProcess();
			 * operationProcessInputKey.setOperationProcessKey(e);
			 * operationProcessInputKey.setAddedToInputProcess(true);
			 * operationProcessInputKeyList.add(operationProcessInputKey);
			 * //testFilterData.addOperationProcessDataList(operationProcessInputKey);
			 * });
			 */

			// operationProcessInputKeyList.stream()
			// .forEach(e -> testFilterData.addOperationProcessDataList(e) );

		} // else{
			// testFilterData.setOperationProcessMethod(ConstantReportV2.NONE_DISPLAYED);
			// }
		testFilterDataFetchGromGui.setNonDisplayedDataSet(nonDisplayedDataSet);

		if (ref_chkBxReplicateData.isSelected()) {
			ArrayList<String> replicateResultCellPositionList = new ArrayList<String>();
			for (int i = 0; i < testFilterDataCellList.size(); i++) {
				// headerKey = testFilterData.getTestFilterDataCellList().get(i).getheader();
				headerKey = testFilterDataCellList.get(i).getKeyParam();
				if (headerKey.startsWith(REPLICATE_RESULT_KEY_PREFIX)) {
					// replicateResultCellPositionList.add(testFilterData.getTestFilterDataCellList().get(i).getcell_value());
					replicateResultCellPositionList.add(testFilterDataCellList.get(i).getCellPosition());
				}
			}
			testFilterDataFetchGromGui.setReplicateResultCellPositionList(replicateResultCellPositionList);
		}

		boolean operationInputOnlyHeader = false;
		if (OPERATION_INPUT_HEADERS_ONLY_DATA_TYPE_LIST
				.contains(testFilterDataFetchGromGui.getTestExecutionResultTypeSelected())) {
			operationInputOnlyHeader = true;
		}

		testFilterDataFetchGromGui.setOperationInputOnlyHeader(operationInputOnlyHeader);
		testFilterDataFetchGromGui.setOperationProcessPostActive(ref_chkBxPostOperationActive.isSelected());
		testFilterDataFetchGromGui.setOperationProcessPostMethod(
				ref_cmbBxPostOperationMethod.getSelectionModel().getSelectedItem().toString());
		testFilterDataFetchGromGui.setOperationProcessPostInputValue(ref_txtPostOperationValue.getText());
		String filterPreview = testFilterDataFetchGromGui.getTestTypeAlias()
				+ ConstantReportV2.TEST_POINT_NAME_SEPERATOR +
				// "XX" +
				ConstantReportV2.TEST_ID_MASK + ConstantReportV2.TEST_POINT_FILTER_SEPERATOR +
				ref_txtVoltPercentFilterData.getText();
		if (ref_rdBtnOperationOutput.isSelected()) {
			filterPreview = "NA";
		} else {
			if (!txtPfFilterUserEntry.getText().isEmpty()) {
				filterPreview = filterPreview + ConstantReportV2.TEST_POINT_FILTER_SEPERATOR
						+ ref_txtPfFilterData.getText();
			}
			if (!txtCurrentPercentFilterUserEntry.getText().isEmpty()) {
				filterPreview = filterPreview + ConstantReportV2.TEST_POINT_FILTER_SEPERATOR
						+ ref_txtCurrentPercentFilterData.getText();
			}
			if (!ref_txtFreqFilterData.getText().isEmpty()) {
				filterPreview = filterPreview + ConstantReportV2.TEST_POINT_FILTER_SEPERATOR
						+ ref_txtFreqFilterData.getText();
			}
			if (!ref_txtEnergyFilterData.getText().isEmpty()) {
				filterPreview = filterPreview + ConstantReportV2.TEST_POINT_FILTER_SEPERATOR
						+ ref_txtEnergyFilterData.getText();
			}

			if ((selectedTestTypeKey.equals(ConstantApp.DISPLAY_TC_TITLE_REPEATABLITY))
					|| (selectedTestTypeKey.equals(ConstantApp.DISPLAY_TC_TITLE_SELF_HEATING))) {
				if (!ref_txtIterationReadingStartingId.getText().isEmpty()) {
					filterPreview = filterPreview + ConstantReportV2.TEST_POINT_FILTER_SEPERATOR
							+ ref_txtIterationReadingStartingId.getText();
				}

				if (!ref_txtIterationReadingEndingId.getText().isEmpty()) {
					if (ConstantReportV2.REPEAT_START_TO_END_FEATURE_ENABLED) {
						filterPreview = filterPreview + ConstantReportV2.TEST_POINT_FILTER_SEPERATOR
								+ ref_txtIterationReadingEndingId.getText();
						ApplicationLauncher.logger
								.debug("fetchTestFilterGuiData: ref_txtIterationReadingEndingId: filterPreview: "
										+ filterPreview);
					}

				}
			} else if (selectedTestTypeKey.equals(ConstantApp.DISPLAY_TC_TITLE_CUSTOM_TEST)) {
				filterPreview = testFilterDataFetchGromGui.getTestTypeAlias()
						+ ConstantReportV2.TEST_POINT_NAME_SEPERATOR +
						ref_txtCustomTestNameUserEntry.getText() + // ConstantReportV2.TEST_POINT_NAME_SEPERATOR +
						ConstantReportV2.TEST_POINT_NAME_SEPERATOR +
						ConstantReportV2.TEST_ID_MASK;// +
				// ConstantReportV2.TEST_POINT_FILTER_SEPERATOR + "1.0";
			}
		}
		testFilterDataFetchGromGui.setFilterPreview(filterPreview);
		ApplicationLauncher.logger.debug("fetchTestFilterGuiData: filterPreview: " + filterPreview);
		testFilterDataFetchGromGui.clearRpPrintPositionList();
		for (RpPrintPosition eachPrintPositionLocatorData : ref_tvTestFilterCellStartPosition.getItems()) {
			testFilterDataFetchGromGui.addRpPrintPositionList(eachPrintPositionLocatorData);
		}
		for (RpPrintPosition eachPrintPositionLocatorData : ref_tvTestFilterCellHeaderPosition.getItems()) {
			testFilterDataFetchGromGui.addRpPrintPositionList(eachPrintPositionLocatorData);
		}
		// if(ref_rdBtnOperationOutput.isSelected()){
		/*
		 * Collection<String> operationProcessInputKeyList =
		 * ref_listViewInputProcessList.getItems();
		 * tempList = new ArrayList<String> (operationProcessInputKeyList);
		 * testFilterData.setOperationProcessInputKeyList(tempList);
		 */
		/*
		 * if(testFilterData.getOperationProcessInputKeyList().size()>0){
		 * setPresentInputProcessList(testFilterData.getOperationProcessInputKeyList());
		 * }else{
		 * clearPresentInputProcessList();
		 * }
		 */
		// }
		/*
		 * else{
		 * clearPresentInputProcessList();
		 * }
		 */

		/*
		 * testFilterData.setResultValueCellPosition(ref_ .getText());
		 * testFilterData.setResultStatusCellPosition(ref_ .getText());
		 * testFilterData.setResultDataTypeCellPosition(ref_ .getText());
		 * testFilterData.setResultUpperLimitCellPosition(ref_ .getText());
		 * testFilterData.setResultLowerLimitCellPosition(ref_ .getText());
		 */
		// getReportProfileTestFilterDataHashMap().cxvcx
		for (int i = 0; i < testFilterDataFetchGromGui.getOperationProcessDataList().size(); i++) {
			String opProcessKey = testFilterDataFetchGromGui.getOperationProcessDataList().get(i)
					.getOperationProcessKey();
			if (getOperationProcessDbIdHashMap().containsKey(opProcessKey)) {
				int dbIdValue = getOperationProcessDbIdHashMap().get(opProcessKey);
				ApplicationLauncher.logger
						.debug("fetchTestFilterGuiData: opProcessKey :" + opProcessKey + " -> " + dbIdValue);
				testFilterDataFetchGromGui.getOperationProcessDataList().get(i).setId(dbIdValue);
			}
		}

		for (int i = 0; i < testFilterDataFetchGromGui.getRpPrintPositionList().size(); i++) {
			if (testFilterDataFetchGromGui.getRpPrintPositionList().get(i).getDataOwner()
					.equals(ConstantReportV2.RP_DATA_OWNER_TEST_DATA_FILTER)) {
				String rpPrintKey = testFilterDataFetchGromGui.getRpPrintPositionList().get(i).getKeyParam();
				if (getRpPrintDbIdHashMap().containsKey(rpPrintKey)) {
					int dbIdValue = getRpPrintDbIdHashMap().get(rpPrintKey);
					ApplicationLauncher.logger
							.debug("fetchTestFilterGuiData: rpPrintKey :" + rpPrintKey + " -> " + dbIdValue);
					testFilterDataFetchGromGui.getRpPrintPositionList().get(i).setId(dbIdValue);
				}
			}
		}

		return testFilterDataFetchGromGui;
	}

	@FXML
	public void btnMeterMetaDataCancelOnClick() {
		ApplicationLauncher.logger.debug("btnMeterMetaDataCancelOnClick: Entry");
		setInsertDataMode(false);
		ref_gridPaneControl.setDisable(false);
		ref_titledPaneMeterProfileMetaData.setDisable(true);
		ref_tabMeterProfileDataList.setDisable(false);
		ref_tabTestFilterDataList.setDisable(false);
	}

	@FXML
	public void btnTestFilterDataCancelOnClick() {
		ApplicationLauncher.logger.debug("btnTestFilterDataCancelOnClick: Entry");
		setInsertDataMode(false);
		ref_gridPaneControl.setDisable(false);
		ref_titledPaneTestFilterData.setDisable(true);

		ref_cmbBxTestType.getSelectionModel().select(ConstantReportV2.NONE_DISPLAYED);
		ref_tabMeterProfileDataList.setDisable(false);
		ref_tabTestFilterDataList.setDisable(false);
		/*
		 * ref_rdBtnOperationInput.setSelected(false);
		 * ref_rdBtnOperationOutput.setSelected(false);
		 * ref_rdBtnOperationNone.setSelected(true);
		 * chkBxEnableFilter.setSelected(false);
		 * chkBxErrorResultStatus.setSelected(false);
		 * chkBxReplicateData.setSelected(false);
		 * chkBxPopulateHeader1.setSelected(false);
		 * chkBxPopulateHeader2.setSelected(false);
		 * chkBxPopulateHeader3.setSelected(false);
		 */
	}

	@FXML
	public void btnMeterMetaDataSaveOnClick() {
		ApplicationLauncher.logger.debug("btnMeterMetaDataSaveOnClick: Entry");
		boolean status = validateMeterMetaDataCellDataEntry();
		// String pageNumber = "";
		String pageName = "";
		if (status) {
			// ApplicationLauncher.logger.debug("btnMeterMetaDataSaveOnClick: test1");
			ReportProfileMeterMetaDataModel reportProfileMeterMetaData = new ReportProfileMeterMetaDataModel();
			String pageNumber = ref_txtMeterMetaDataPageNumber.getText();
			pageName = ref_txtMeterMetaDataPageName.getText();
			boolean filterActive = ref_chkBxMeterProfileMetaDataPageActive.isSelected();
			boolean discardRackPositionInDutSerialNumber = ref_chkBxDiscardRackPositionInDutSerialNumber.isSelected();
			reportProfileMeterMetaData.setPageNumber(pageNumber);
			reportProfileMeterMetaData.setPageName(pageName);
			reportProfileMeterMetaData.setMeterMetaDataPopulateType(
					ref_cmbBxMeterMetaDataPopulateType.getSelectionModel().getSelectedItem().toString());
			// reportProfileMeterMetaData.setReportBaseTemplate(ref_cmbBxBaseTemplate.getSelectionModel().getSelectedItem().toString());
			// reportProfileMeterMetaData.setAppendMeterSerialNoAndRackPosition(ref_chkBxDiscardRackPositionInDutSerialNumber.isSelected());
			ApplicationLauncher.logger.debug("btnMeterMetaDataSaveOnClick: discardRackPositionInDutSerialNumber: "
					+ discardRackPositionInDutSerialNumber);

			// ref_tvMeterMetaDataList.getItems().clear();
			// ref_tvMeterMetaDataList.getItems().setAll(getMeterMetaDataList());
			getMeterMetaDataList().stream()
					.filter(e -> e.getPageNumber().equals(pageNumber))
					.forEach(e -> {
						// ApplicationLauncher.logger.debug("btnMeterMetaDataSaveOnClick:
						// getPageNumber1: " + e.getPageNumber());
						// ApplicationLauncher.logger.debug("btnMeterMetaDataSaveOnClick:
						// getDataTypeKey1: " + e.getDataTypeKey());
						e.setFilterActive(filterActive);
						e.setDiscardRackPositionInDutSerialNumber(discardRackPositionInDutSerialNumber);
					});

			if (isInsertDataMode()) {
				ref_tvMeterMetaDataList.getItems().addAll(getMeterMetaDataList());
			} else {
				ref_tvMeterMetaDataList.getItems().removeIf(e -> e.getPageNumber().equals(pageNumber));
				ref_tvMeterMetaDataList.getItems().addAll(getMeterMetaDataList());
			}

			/*
			 * for(int i =0; i< getMeterMetaDataList().size(); i++){
			 * ApplicationLauncher.logger.
			 * debug("btnMeterMetaDataSaveOnClick: getPageNumber0: " +
			 * getMeterMetaDataList().get(i).getPageNumber());
			 * ApplicationLauncher.logger.
			 * debug("btnMeterMetaDataSaveOnClick: getDataTypeKey0: " +
			 * getMeterMetaDataList().get(i).getDataTypeKey());
			 * ApplicationLauncher.logger.
			 * debug("btnMeterMetaDataSaveOnClick: isFilterActive0: " +
			 * getMeterMetaDataList().get(i).isFilterActive());
			 * ApplicationLauncher.logger.
			 * debug("btnMeterMetaDataSaveOnClick: isDiscardRackPositionInDutSerialNumber0: "
			 * + getMeterMetaDataList().get(i).isDiscardRackPositionInDutSerialNumber());
			 * 
			 * 
			 * }
			 * 
			 * getMeterMetaDataList().stream()
			 * .filter(e -> e.getPageNumber().equals(pageNumber))
			 * .forEach(e -> {
			 * ApplicationLauncher.logger.
			 * debug("btnMeterMetaDataSaveOnClick: getPageNumber1: " + e.getPageNumber());
			 * ApplicationLauncher.logger.
			 * debug("btnMeterMetaDataSaveOnClick: getDataTypeKey1: " + e.getDataTypeKey());
			 * e.setFilterActive(filterActive);
			 * e.setDiscardRackPositionInDutSerialNumber(
			 * discardRackPositionInDutSerialNumber);
			 * }); ;
			 * for(int i =0; i< getMeterMetaDataList().size(); i++){
			 * ApplicationLauncher.logger.
			 * debug("btnMeterMetaDataSaveOnClick: getPageNumber2: " +
			 * getMeterMetaDataList().get(i).getPageNumber());
			 * ApplicationLauncher.logger.
			 * debug("btnMeterMetaDataSaveOnClick: getDataTypeKey2: " +
			 * getMeterMetaDataList().get(i).getDataTypeKey());
			 * ApplicationLauncher.logger.
			 * debug("btnMeterMetaDataSaveOnClick: isFilterActive2: " +
			 * getMeterMetaDataList().get(i).isFilterActive());
			 * ApplicationLauncher.logger.
			 * debug("btnMeterMetaDataSaveOnClick: isDiscardRackPositionInDutSerialNumber2: "
			 * + getMeterMetaDataList().get(i).isDiscardRackPositionInDutSerialNumber());
			 * 
			 * 
			 * }
			 */

			reportProfileMeterMetaData.setReportProfileMeterMetaData(getMeterMetaDataList());

			ref_tvMeterMetaDataList.refresh();
			List<ReportMeterMetaDataTypeSubModel> data1 = ref_tvMeterMetaDataList.getItems();
			// ref_tvMeterMetaDataList
			/*
			 * for(int i =0; i< data1.size(); i++){
			 * ApplicationLauncher.logger.
			 * debug("btnMeterMetaDataSaveOnClick: getPageNumber3: " +
			 * data1.get(i).getPageNumber());
			 * ApplicationLauncher.logger.
			 * debug("btnMeterMetaDataSaveOnClick: getDataTypeKey3: " +
			 * data1.get(i).getDataTypeKey());
			 * ApplicationLauncher.logger.
			 * debug("btnMeterMetaDataSaveOnClick: isFilterActive3: " +
			 * data1.get(i).isFilterActive());
			 * ApplicationLauncher.logger.
			 * debug("btnMeterMetaDataSaveOnClick: isDiscardRackPositionInDutSerialNumber3: "
			 * + data1.get(i).isDiscardRackPositionInDutSerialNumber());
			 * 
			 * 
			 * }
			 */

			addReportProfileMeterMetaDataHashMap(pageNumber, reportProfileMeterMetaData);
			if (ref_chkBxDiscardRackPositionInDutSerialNumber.isSelected()) {
				ref_chkBxAppendMeterSerialNoAndRackPositionListDisplay.setSelected(true);
			} else {
				ref_chkBxAppendMeterSerialNoAndRackPositionListDisplay.setSelected(false);
			}

			ObservableList existingPageNameList = ref_cmbBxMetaDataPageName.getItems();

			if (!existingPageNameList.contains(pageName)) {
				ref_cmbBxMetaDataPageName.getItems().add(pageName);
				ref_cmbBxMetaDataPageName.getSelectionModel().select(pageName);
			}

			getLastSavedMeterMetaDataCellPosition().clear();
			getMeterMetaDataList().forEach(k -> {
				updateLastSavedMeterMetaDataCellPosition(k.getDataTypeKey(), k.getDataCellPosition());
				// ApplicationLauncher.logger.debug("displayMeterMetaDataList: " +
				// k.getDataTypeKey());
			});

			// reOrderSerialNumber(ref_tvSelectedMeterDataType);
			ArrayList<ReportMeterMetaDataTypeSubModel> sortedTableData = (ArrayList<ReportMeterMetaDataTypeSubModel>) ref_tvMeterMetaDataList
					.getItems().stream()
					.sorted(Comparator.comparingInt(ReportMeterMetaDataTypeSubModel::getPageNumberInt))
					.collect(Collectors.toList());

			ref_tvMeterMetaDataList.getItems().clear();
			ref_tvMeterMetaDataList.getItems().setAll(sortedTableData);

			reOrderSerialNumber(ref_tvMeterMetaDataList);

			setInsertDataMode(false);
			ref_gridPaneControl.setDisable(false);
			ref_titledPaneMeterProfileMetaData.setDisable(true);
			ref_tabMeterProfileDataList.setDisable(false);
			ref_tabTestFilterDataList.setDisable(false);
		}
	}

	/*
	 * private void displayMeterMetaDataList() {
	 * 
	 * ApplicationLauncher.logger.debug("displayMeterMetaDataList: Entry");
	 * //for(ReportFilterDataTypeModel meterMetaLocalDataList :
	 * getMeterMetaDataList()){
	 * 
	 * //}
	 * getMeterMetaDataList().forEach(k->
	 * {ApplicationLauncher.logger.debug("displayMeterMetaDataList: " +
	 * k.getDataTypeKey());});
	 * }
	 */

	private boolean validateTestFilterCellDataEntry() {

		ApplicationLauncher.logger.debug("validateTestFilterCellDataEntry: Entry");

		if (isInsertDataMode()) {
			List<String> existingTestFilterNameList = new ArrayList<String>();
			ref_tvTestFilterDataList.getItems().stream()
					.forEach(e -> existingTestFilterNameList.add(e.getTestFilterName()));

			if (existingTestFilterNameList.size() > 0) {
				String presentTestFilterName = ref_txtFilterName.getText();
				if (!presentTestFilterName.isEmpty()) {
					if (existingTestFilterNameList.contains(presentTestFilterName)) {
						ApplicationLauncher.logger.debug(
								"validateTestFilterCellDataEntry: presentTestFilterName already exist in the list: "
										+ presentTestFilterName);
						ApplicationLauncher.logger.info("validateTestFilterCellDataEntry : ERROR_CODE_6031: "
								+ ErrorCodeMapping.ERROR_CODE_6031_MSG + presentTestFilterName + " - prompted!");
						WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6031,
								ErrorCodeMapping.ERROR_CODE_6031_MSG + presentTestFilterName, AlertType.ERROR);

						return false;
					}
				} else {

					ApplicationLauncher.logger.debug("validateTestFilterCellDataEntry: Filter Name is empty");
					ApplicationLauncher.logger.info("validateTestFilterCellDataEntry : ERROR_CODE_6050: "
							+ ErrorCodeMapping.ERROR_CODE_6050_MSG + " - prompted!");
					WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6050, ErrorCodeMapping.ERROR_CODE_6050_MSG,
							AlertType.ERROR);

					return false;
				}
			} else {
				String presentTestFilterName = ref_txtFilterName.getText();
				if (presentTestFilterName.isEmpty()) {
					ApplicationLauncher.logger.debug("validateTestFilterCellDataEntry 2: Filter Name is empty");
					ApplicationLauncher.logger.info("validateTestFilterCellDataEntry 2 : ERROR_CODE_6050: "
							+ ErrorCodeMapping.ERROR_CODE_6050_MSG + " - prompted!");
					WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6050, ErrorCodeMapping.ERROR_CODE_6050_MSG,
							AlertType.ERROR);

				}
			}

		}

		boolean status = validateUserEntryDataInRange();

		if (!status) {
			return false;
		}

		ArrayList<String> cellDataList = new ArrayList<String>();
		String readCellData = "";// ref_tvTestFilterCellStartPosition.getItems().get(i).getcell_value();
		String readDataHeaderKey = "";
		String alreadyFoundHeader = "";
		ArrayList<String> cellStartPositionList = new ArrayList<String>();
		ArrayList<String> cellStartKeyList = new ArrayList<String>();
		for (int i = 0; i < ref_tvTestFilterCellStartPosition.getItems().size(); i++) {
			// readCellData =
			// ref_tvTestFilterCellStartPosition.getItems().get(i).getcell_value();
			// readDataHeaderKey =
			// ref_tvTestFilterCellStartPosition.getItems().get(i).getheader();
			readCellData = ref_tvTestFilterCellStartPosition.getItems().get(i).getCellPosition();
			readDataHeaderKey = ref_tvTestFilterCellStartPosition.getItems().get(i).getKeyParam();
			if (readCellData.isEmpty()) {
				ApplicationLauncher.logger
						.debug("validateTestFilterCellDataEntry: ref_tvTestFilterCellStartPosition.getItems().get(" + i
								+ ").getheader() : " + readDataHeaderKey + " is empty");
				ApplicationLauncher.logger.info("validateTestFilterCellDataEntry : ERROR_CODE_6013: "
						+ ErrorCodeMapping.ERROR_CODE_6013_MSG + readDataHeaderKey + " - prompted!");
				WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6013,
						ErrorCodeMapping.ERROR_CODE_6013_MSG + readDataHeaderKey, AlertType.ERROR);

				return false;
			} else if (cellDataList.contains(readCellData)) {
				ApplicationLauncher.logger.info(
						"validateTestFilterCellDataEntry : existing index: " + cellDataList.indexOf(readCellData));
				// alreadyFoundHeader =
				// ref_tvTestFilterCellStartPosition.getItems().get(cellDataList.indexOf(readCellData)).getheader();
				alreadyFoundHeader = ref_tvTestFilterCellStartPosition.getItems()
						.get(cellDataList.indexOf(readCellData)).getKeyParam();
				ApplicationLauncher.logger.info("validateTestFilterCellDataEntry : ERROR_CODE_6015: "
						+ ErrorCodeMapping.ERROR_CODE_6015_MSG + "\n\nData Type1 : " + alreadyFoundHeader
						+ "\nData Type2 : " + readDataHeaderKey + "\nCell Value : " + readCellData + " - prompted!");
				WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6015,
						ErrorCodeMapping.ERROR_CODE_6015_MSG + "\n\nData Type1 : " + alreadyFoundHeader
								+ "\nData Type2 : " + readDataHeaderKey + "\nCell Value : " + readCellData,
						AlertType.ERROR);

				return false;
			} else {
				// cellDataList.add(ref_tvTestFilterCellStartPosition.getItems().get(i).getcell_value());
				cellDataList.add(readCellData);
				cellStartPositionList.add(readCellData);
				cellStartKeyList.add(readDataHeaderKey);
			}

			/*
			 * for(int j= 0; j< getMeterMetaDataList().size(); j++){
			 * //ApplicationLauncher.logger.
			 * debug("validateMeterMetaDataCellDataEntry : Start readDataHeaderKey2: " +
			 * readDataHeaderKey);
			 * //ApplicationLauncher.logger.
			 * debug("validateMeterMetaDataCellDataEntry : Start getMeterMetaDataList().get(j): "
			 * + getMeterMetaDataList().get(j).getDataTypeKey()+ " ," + j);
			 * if(getMeterMetaDataList().get(j).getDataTypeKey().equals(readDataHeaderKey)){
			 * //ApplicationLauncher.logger.
			 * debug("validateMeterMetaDataCellDataEntry : Start Hit1");
			 * getMeterMetaDataList().get(j).setDataCellPosition(readCellData);
			 * }
			 * 
			 * }
			 */

			if (getTestFilterDataCellPositionHashMap().containsKey(readDataHeaderKey)) {

				updateTestFilterDataCellPositionHashMap(readDataHeaderKey, readCellData);

			}

		}

		// ApplicationLauncher.logger.debug("btnTestFilterDataSaveOnClick:
		// getTestFilterDataCellPositionHashMap1: " +
		// getTestFilterDataCellPositionHashMap());

		cellDataList.clear();
		if (ref_titledPaneCellHeaderPosition.isVisible()) {
			for (int i = 0; i < ref_tvTestFilterCellHeaderPosition.getItems().size(); i++) {

				// readCellData =
				// ref_tvTestFilterCellHeaderPosition.getItems().get(i).getcell_value();
				// readDataHeaderKey =
				// ref_tvTestFilterCellHeaderPosition.getItems().get(i).getheader();
				readCellData = ref_tvTestFilterCellHeaderPosition.getItems().get(i).getCellPosition();
				readDataHeaderKey = ref_tvTestFilterCellHeaderPosition.getItems().get(i).getKeyParam();
				if (readCellData.isEmpty()) {
					ApplicationLauncher.logger
							.debug("validateTestFilterCellDataEntry: ref_tvTestFilterCellHeaderPosition.getItems().get("
									+ i + ").getheader() : " + readDataHeaderKey + " is empty");
					ApplicationLauncher.logger.info("validateTestFilterCellDataEntry : ERROR_CODE_6014: "
							+ ErrorCodeMapping.ERROR_CODE_6014_MSG + readDataHeaderKey + " - prompted!");
					WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6014,
							ErrorCodeMapping.ERROR_CODE_6014_MSG + readDataHeaderKey, AlertType.ERROR);

					return false;
				} else if (cellDataList.contains(readCellData)) {
					ApplicationLauncher.logger.info(
							"validateTestFilterCellDataEntry : existing index: " + cellDataList.indexOf(readCellData));
					// alreadyFoundHeader =
					// ref_tvTestFilterCellHeaderPosition.getItems().get(cellDataList.indexOf(readCellData)).getheader();
					alreadyFoundHeader = ref_tvTestFilterCellHeaderPosition.getItems()
							.get(cellDataList.indexOf(readCellData)).getKeyParam();
					ApplicationLauncher.logger.info(
							"validateTestFilterCellDataEntry : ERROR_CODE_6016: " + ErrorCodeMapping.ERROR_CODE_6016_MSG
									+ "\n\nHeader Type1 : " + alreadyFoundHeader + "\nHeader Type2 : "
									+ readDataHeaderKey + "\nCell Value : " + readCellData + " - prompted!");
					WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6016,
							ErrorCodeMapping.ERROR_CODE_6016_MSG + "\n\nHeader Type1 : " + alreadyFoundHeader
									+ "\nHeader Type2 : " + readDataHeaderKey + "\nCell Value : " + readCellData,
							AlertType.ERROR);

					return false;
				} else {
					if (cellStartPositionList.contains(readCellData)) {
						int indexOfFoundCellData = cellStartPositionList.indexOf(readCellData);
						alreadyFoundHeader = cellStartKeyList.get(indexOfFoundCellData);
						ApplicationLauncher.logger.info("validateMeterMetaDataCellDataEntry : ERROR_CODE_6023: "
								+ ErrorCodeMapping.ERROR_CODE_6023_MSG + "\n\nCell Start Key : " + alreadyFoundHeader
								+ "\nCell Header Key : " + readDataHeaderKey + "\nCell Value : " + readCellData
								+ " - prompted!");
						WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6023,
								ErrorCodeMapping.ERROR_CODE_6023_MSG + "\n\nCell Start Key : " + alreadyFoundHeader
										+ "\nCell Header Key : " + readDataHeaderKey + "\nCell Value : " + readCellData,
								AlertType.ERROR);
						return false;
					} else {
						// cellDataList.add(ref_tvTestFilterCellHeaderPosition.getItems().get(i).getcell_value());
						cellDataList.add(readCellData);
					}
				}

				/*
				 * for(int j= 0; j< getMeterMetaDataList().size(); j++){
				 * ApplicationLauncher.logger.
				 * debug("validateMeterMetaDataCellDataEntry : Header getMeterMetaDataList().get(j): "
				 * + getMeterMetaDataList().get(j).getDataTypeKey()+ " ," + j);
				 * 
				 * if(getMeterMetaDataList().get(j).getDataTypeKey().equals(readDataHeaderKey)){
				 * //ApplicationLauncher.logger.
				 * debug("validateMeterMetaDataCellDataEntry : Header Hit1");
				 * getMeterMetaDataList().get(j).setDataCellPosition(readCellData);
				 * }
				 * 
				 * }
				 */

				if (getTestFilterDataCellPositionHashMap().containsKey(readDataHeaderKey)) {

					updateTestFilterDataCellPositionHashMap(readDataHeaderKey, readCellData);

				}
			}
		}

		// ApplicationLauncher.logger.debug("btnTestFilterDataSaveOnClick:
		// getTestFilterDataCellPositionHashMap2: " +
		// getTestFilterDataCellPositionHashMap());

		ApplicationLauncher.logger.debug("validateTestFilterCellDataEntry: Exit");
		return true;
	}

	private boolean validateMeterMetaDataCellDataEntry() {

		ApplicationLauncher.logger.debug("validateMeterMetaDataCellDataEntry: Entry");
		// displayMeterMetaDataList();
		ArrayList<String> cellDataList = new ArrayList<String>();
		String readCellData = "";
		String readDataHeaderKey = "";
		String alreadyFoundHeader = "";
		ArrayList<String> cellStartPositionList = new ArrayList<String>();
		ArrayList<String> cellStartKeyList = new ArrayList<String>();

		String pageName = ref_txtMeterMetaDataPageName.getText();
		if (pageName.isEmpty()) {
			ApplicationLauncher.logger.debug("validateMeterMetaDataCellDataEntry: Page Name is empty");
			ApplicationLauncher.logger.info("validateMeterMetaDataCellDataEntry : ERROR_CODE_6028: "
					+ ErrorCodeMapping.ERROR_CODE_6028_MSG + " - prompted!");
			WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6028, ErrorCodeMapping.ERROR_CODE_6028_MSG,
					AlertType.ERROR);

			return false;
		}
		for (int i = 0; i < ref_tvMeterMetaDataCellStartPosition.getItems().size(); i++) {
			readCellData = ref_tvMeterMetaDataCellStartPosition.getItems().get(i).getcell_value();
			readDataHeaderKey = ref_tvMeterMetaDataCellStartPosition.getItems().get(i).getheader();
			if (readCellData.isEmpty()) {
				ApplicationLauncher.logger.debug(
						"validateMeterMetaDataCellDataEntry: ref_tvMeterMetaDataCellStartPosition.getItems().get(" + i
								+ ").getheader() : " + readDataHeaderKey + " is empty");
				ApplicationLauncher.logger.info("validateMeterMetaDataCellDataEntry : ERROR_CODE_6017: "
						+ ErrorCodeMapping.ERROR_CODE_6017_MSG + readDataHeaderKey + " - prompted!");
				WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6017,
						ErrorCodeMapping.ERROR_CODE_6017_MSG + readDataHeaderKey, AlertType.ERROR);

				return false;
			} else if (cellDataList.contains(readCellData)) {
				ApplicationLauncher.logger.info(
						"validateMeterMetaDataCellDataEntry : existing index: " + cellDataList.indexOf(readCellData));
				alreadyFoundHeader = ref_tvMeterMetaDataCellStartPosition.getItems()
						.get(cellDataList.indexOf(readCellData)).getheader();
				ApplicationLauncher.logger.info("validateMeterMetaDataCellDataEntry : ERROR_CODE_6019: "
						+ ErrorCodeMapping.ERROR_CODE_6019_MSG + "\n\nData Type1 : " + alreadyFoundHeader
						+ "\nData Type2 : " + readDataHeaderKey + "\nCell Value : " + readCellData + " - prompted!");
				WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6019,
						ErrorCodeMapping.ERROR_CODE_6019_MSG + "\n\nData Type1 : " + alreadyFoundHeader
								+ "\nData Type2 : " + readDataHeaderKey + "\nCell Value : " + readCellData,
						AlertType.ERROR);

				return false;
			} else {
				// cellDataList.add(ref_tvMeterMetaDataCellStartPosition.getItems().get(i).getcell_value());
				cellDataList.add(readCellData);
				cellStartPositionList.add(readCellData);
				cellStartKeyList.add(readDataHeaderKey);
			}
			for (int j = 0; j < getMeterMetaDataList().size(); j++) {
				// ApplicationLauncher.logger.debug("validateMeterMetaDataCellDataEntry : Start
				// readDataHeaderKey2: " + readDataHeaderKey);
				// ApplicationLauncher.logger.debug("validateMeterMetaDataCellDataEntry : Start
				// getMeterMetaDataList().get(j): " +
				// getMeterMetaDataList().get(j).getDataTypeKey()+ " ," + j);
				if (getMeterMetaDataList().get(j).getDataTypeKey().equals(readDataHeaderKey)) {
					// ApplicationLauncher.logger.debug("validateMeterMetaDataCellDataEntry : Start
					// Hit1");
					getMeterMetaDataList().get(j).setDataCellPosition(readCellData);
				}

			}
		}
		// displayMeterMetaDataList();
		cellDataList.clear();
		if (ref_titledPaneMeterDataCellHeaderPosition.isVisible()) {
			for (int i = 0; i < ref_tvMeterMetaDataCellHeaderPosition.getItems().size(); i++) {
				readCellData = ref_tvMeterMetaDataCellHeaderPosition.getItems().get(i).getcell_value();
				readDataHeaderKey = ref_tvMeterMetaDataCellHeaderPosition.getItems().get(i).getheader();
				if (readCellData.isEmpty()) {
					ApplicationLauncher.logger.debug(
							"validateMeterMetaDataCellDataEntry: ref_tvMeterMetaDataCellHeaderPosition.getItems().get("
									+ i + ").getheader() : " + readDataHeaderKey + " is empty");
					ApplicationLauncher.logger.info("validateMeterMetaDataCellDataEntry : ERROR_CODE_6018: "
							+ ErrorCodeMapping.ERROR_CODE_6018_MSG + readDataHeaderKey + " - prompted!");
					WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6018,
							ErrorCodeMapping.ERROR_CODE_6018_MSG + readDataHeaderKey, AlertType.ERROR);

					return false;
				} else if (cellDataList.contains(readCellData)) {
					ApplicationLauncher.logger.info("validateMeterMetaDataCellDataEntry : existing index: "
							+ cellDataList.indexOf(readCellData));
					alreadyFoundHeader = ref_tvMeterMetaDataCellHeaderPosition.getItems()
							.get(cellDataList.indexOf(readCellData)).getheader();
					ApplicationLauncher.logger.info("validateMeterMetaDataCellDataEntry : ERROR_CODE_6020: "
							+ ErrorCodeMapping.ERROR_CODE_6020_MSG + "\n\nHeader Type1 : " + alreadyFoundHeader
							+ "\nHeader Type2 : " + readDataHeaderKey + "\nCell Value : " + readCellData
							+ " - prompted!");
					WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6020,
							ErrorCodeMapping.ERROR_CODE_6020_MSG + "\n\nHeader Type1 : " + alreadyFoundHeader
									+ "\nHeader Type2 : " + readDataHeaderKey + "\nCell Value : " + readCellData,
							AlertType.ERROR);

					return false;
				} else {
					if (cellStartPositionList.contains(readCellData)) {
						int indexOfFoundCellData = cellStartPositionList.indexOf(readCellData);
						alreadyFoundHeader = cellStartKeyList.get(indexOfFoundCellData);
						ApplicationLauncher.logger.info("validateMeterMetaDataCellDataEntry : ERROR_CODE_6022: "
								+ ErrorCodeMapping.ERROR_CODE_6022_MSG + "\n\nCell Start Key : " + alreadyFoundHeader
								+ "\nCell Header Key : " + readDataHeaderKey + "\nCell Value : " + readCellData
								+ " - prompted!");
						WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6022,
								ErrorCodeMapping.ERROR_CODE_6022_MSG + "\n\nCell Start Key : " + alreadyFoundHeader
										+ "\nCell Header Key : " + readDataHeaderKey + "\nCell Value : " + readCellData,
								AlertType.ERROR);
						return false;
					} else {
						// cellDataList.add(ref_tvMeterMetaDataCellHeaderPosition.getItems().get(i).getcell_value());
						cellDataList.add(readCellData);

					}
				}

				for (int j = 0; j < getMeterMetaDataList().size(); j++) {
					ApplicationLauncher.logger
							.debug("validateMeterMetaDataCellDataEntry : Header getMeterMetaDataList().get(j): "
									+ getMeterMetaDataList().get(j).getDataTypeKey() + " ," + j);

					if (getMeterMetaDataList().get(j).getDataTypeKey().equals(readDataHeaderKey)) {
						// ApplicationLauncher.logger.debug("validateMeterMetaDataCellDataEntry : Header
						// Hit1");
						getMeterMetaDataList().get(j).setDataCellPosition(readCellData);
					}

				}

			}
		}
		ApplicationLauncher.logger.debug("validateMeterMetaDataCellDataEntry: Exit");
		// displayMeterMetaDataList();
		return true;
	}

	public void rdBtnOperationNoneEditMode() {
		ApplicationLauncher.logger.debug("rdBtnOperationNoneEditMode: Entry");
		if (ref_rdBtnOperationNone.isSelected()) {
			// ref_cmbBxOperationOutputUserEntry.setDisable(true);
			ref_cmbBxOperationInputUserEntry.setDisable(true);
			ref_rdBtnOperationInput.setSelected(false);
			ref_rdBtnOperationOutput.setSelected(false);
			ref_titledPaneOperation.setDisable(true);
			ref_cmbBxExecutionResultType.setDisable(false);
			ref_cmbBxOperationSourceParamType.setDisable(false);
			ref_cmbBxTestType.setDisable(false);
			ref_chkBxReplicateData.setDisable(false);
			ref_cmbBxReplicateCountUserEntry.setDisable(false);
			// ref_cmbBxTestType.getItems().remove(ConstantReportV2.NONE_DISPLAYED);
			// ref_cmbBxTestType.getSelectionModel().select(0);
			// ref_cmbBxTestType.getItems().remove(ConstantReportV2.RESULT_DATA_TYPE_DISPLAY_OPERATION);
			// ref_cmbBxExecutionResultType.getSelectionModel().select(0);
			enablePrimaryFilterGuiObjects();
		} else {
			// ref_cmbBxOperationOutputUserEntry.setDisable(true);
			ref_rdBtnOperationNone.setSelected(true);

		}
	}

	@FXML
	public void rdBtnOperationNoneOnChange() {
		ApplicationLauncher.logger.debug("rdBtnOperationNoneOnChange: Entry");
		if (ref_rdBtnOperationNone.isSelected()) {
			// ref_cmbBxOperationOutputUserEntry.setDisable(true);
			ref_cmbBxOperationInputUserEntry.setDisable(true);
			ref_rdBtnOperationInput.setSelected(false);
			ref_rdBtnOperationOutput.setSelected(false);
			ref_titledPaneOperation.setDisable(true);
			ref_cmbBxExecutionResultType.setDisable(false);
			ref_cmbBxOperationSourceParamType.setDisable(false);
			ref_cmbBxTestType.setDisable(false);
			ref_chkBxReplicateData.setDisable(false);
			ref_cmbBxReplicateCountUserEntry.setDisable(false);
			ref_cmbBxTestType.getItems().remove(ConstantReportV2.NONE_DISPLAYED);
			ref_cmbBxTestType.getSelectionModel().select(0);
			ref_cmbBxTestType.getItems().remove(ConstantReportV2.RESULT_DATA_TYPE_DISPLAY_OPERATION);
			ref_cmbBxExecutionResultType.getSelectionModel().select(0);
			ref_cmbBxOperationSourceParamType.getSelectionModel().select(0);
			enablePrimaryFilterGuiObjects();
		} else {
			// ref_cmbBxOperationOutputUserEntry.setDisable(true);
			ref_rdBtnOperationNone.setSelected(true);

		}
	}

	public void rdBtnOperationInputEditMode() {
		ApplicationLauncher.logger.debug("rdBtnOperationInputEditMode: Entry");
		if (ref_rdBtnOperationInput.isSelected()) {
			ref_cmbBxOperationInputUserEntry.setDisable(false);
			// ref_cmbBxOperationOutputUserEntry.setDisable(true);
			// ref_rdBtnOperationOutput.setSelected(false);
			// ref_rdBtnOperationNone.setSelected(false);
			ref_titledPaneOperation.setDisable(true);
			ref_cmbBxExecutionResultType.setDisable(false);
			ref_cmbBxOperationSourceParamType.setDisable(false);
			ref_cmbBxTestType.setDisable(false);
			ref_chkBxReplicateData.setDisable(false);
			if (ref_chkBxReplicateData.isSelected()) {
				ref_cmbBxReplicateCountUserEntry.setDisable(false);
			} else {
				ref_cmbBxReplicateCountUserEntry.setDisable(true);
			}
			// ref_cmbBxTestType.getItems().remove(ConstantReportV2.NONE_DISPLAYED);
			// ref_cmbBxTestType.getSelectionModel().select(0);
			// ref_cmbBxTestType.getItems().remove(ConstantReportV2.RESULT_DATA_TYPE_DISPLAY_OPERATION);
			// ref_cmbBxExecutionResultType.getSelectionModel().select(0);
			enablePrimaryFilterGuiObjects();
		} else {
			ref_cmbBxOperationInputUserEntry.setDisable(true);
			ref_rdBtnOperationInput.setSelected(true);
		}
	}

	@FXML
	public void rdBtnOperationInputOnChange() {
		ApplicationLauncher.logger.debug("rdBtnOperationInputOnChange: Entry");
		if (ref_rdBtnOperationInput.isSelected()) {
			ref_cmbBxOperationInputUserEntry.setDisable(false);
			// ref_cmbBxOperationOutputUserEntry.setDisable(true);
			ref_rdBtnOperationOutput.setSelected(false);
			ref_rdBtnOperationNone.setSelected(false);
			ref_titledPaneOperation.setDisable(true);
			ref_cmbBxExecutionResultType.setDisable(false);
			ref_cmbBxOperationSourceParamType.setDisable(false);
			ref_cmbBxTestType.setDisable(false);
			ref_chkBxReplicateData.setDisable(false);
			if (ref_chkBxReplicateData.isSelected()) {
				ref_cmbBxReplicateCountUserEntry.setDisable(false);
			} else {
				ref_cmbBxReplicateCountUserEntry.setDisable(true);
			}
			ref_cmbBxTestType.getItems().remove(ConstantReportV2.NONE_DISPLAYED);
			ref_cmbBxTestType.getSelectionModel().select(0);
			ref_cmbBxTestType.getItems().remove(ConstantReportV2.RESULT_DATA_TYPE_DISPLAY_OPERATION);
			ref_cmbBxExecutionResultType.getSelectionModel().select(0);
			ref_cmbBxOperationSourceParamType.getSelectionModel().select(0);
			enablePrimaryFilterGuiObjects();
		} else {
			ref_cmbBxOperationInputUserEntry.setDisable(true);
			ref_rdBtnOperationInput.setSelected(true);
		}
	}

	public void rdBtnOperationOutputEditMode() {
		ApplicationLauncher.logger.debug("rdBtnOperationOutputEditMode: Entry");
		// if(!isTestFilterEditMode()){
		if (ref_rdBtnOperationOutput.isSelected()) {
			// ref_cmbBxOperationOutputUserEntry.setDisable(false);
			ref_cmbBxOperationInputUserEntry.setDisable(true);
			// ref_rdBtnOperationInput.setSelected(false);
			// ref_rdBtnOperationNone.setSelected(false);
			ref_titledPaneOperation.setDisable(false);
			// ref_cmbBxTestType.getItems().add(ConstantReportV2.RESULT_DATA_TYPE_DISPLAY_OPERATION);
			// ref_cmbBxExecutionResultType.getSelectionModel().select(ConstantReportV2.RESULT_DATA_TYPE_DISPLAY_OPERATION);
			// ref_cmbBxExecutionResultType.setDisable(true);
			ref_cmbBxTestType.getItems().add(ConstantReportV2.NONE_DISPLAYED);
			ref_cmbBxTestType.getSelectionModel().select(ConstantReportV2.NONE_DISPLAYED);

			ref_cmbBxTestType.setDisable(true);
			ref_chkBxReplicateData.setDisable(true);
			ref_chkBxReplicateData.setSelected(false);
			ref_cmbBxReplicateCountUserEntry.setDisable(true);
			// disablePrimaryFilterGuiObjects();

		} else {
			// ref_cmbBxOperationOutputUserEntry.setDisable(true);
			ref_rdBtnOperationOutput.setSelected(true);
			// ref_chkBxReplicateData.setDisable(false);
		}
		// }
	}

	@FXML
	public void rdBtnOperationOutputOnChange() {
		ApplicationLauncher.logger.debug("rdBtnOperationOutputOnChange: Entry");
		// if(!isTestFilterEditMode()){
		if (ref_rdBtnOperationOutput.isSelected()) {
			// ref_cmbBxOperationOutputUserEntry.setDisable(false);
			ref_cmbBxOperationInputUserEntry.setDisable(true);
			ref_rdBtnOperationInput.setSelected(false);
			ref_rdBtnOperationNone.setSelected(false);
			ref_titledPaneOperation.setDisable(false);
			ref_cmbBxTestType.getItems().add(ConstantReportV2.RESULT_DATA_TYPE_DISPLAY_OPERATION);
			ref_cmbBxExecutionResultType.getSelectionModel()
					.select(ConstantReportV2.RESULT_DATA_TYPE_DISPLAY_OPERATION);
			ref_cmbBxExecutionResultType.setDisable(true);

			ref_cmbBxOperationSourceParamType.getSelectionModel()
					.select(ConstantReportV2.RESULT_DATA_TYPE_DISPLAY_OPERATION);
			ref_cmbBxOperationSourceParamType.setDisable(true);
			ref_cmbBxTestType.getItems().add(ConstantReportV2.NONE_DISPLAYED);
			ref_cmbBxTestType.getSelectionModel().select(ConstantReportV2.NONE_DISPLAYED);

			ref_cmbBxTestType.setDisable(true);
			ref_chkBxReplicateData.setDisable(true);
			ref_chkBxReplicateData.setSelected(false);
			ref_cmbBxReplicateCountUserEntry.setDisable(true);
			disablePrimaryFilterGuiObjects();

		} else {
			// ref_cmbBxOperationOutputUserEntry.setDisable(true);
			ref_rdBtnOperationOutput.setSelected(true);
			// ref_chkBxReplicateData.setDisable(false);
		}
		// }
	}

	private void disablePrimaryFilterGuiObjects() {

		ApplicationLauncher.logger.debug("disablePrimaryFilterGuiObjects : Entry");
		if (!isTestFilterEditMode()) {
			ref_txtVoltPercentFilterUserEntry.setDisable(true);
			ref_txtCurrentPercentFilterUserEntry.setDisable(true);
			ref_txtPfFilterUserEntry.setDisable(true);
			ref_txtFreqFilterUserEntry.setDisable(true);
			ref_txtEnergyFilterUserEntry.setDisable(true);
			ref_txtIterationReadingStartingId.setDisable(true);
			ref_txtIterationReadingEndingId.setDisable(true);

			// ref_chkBx_IterationIdHeaderPrefix.setDisable(true);
			ref_cmbBxVoltPercentFilterUserEntry.setDisable(true);
			ref_cmbBxCurrentPercentFilterUserEntry.setDisable(true);
			ref_cmbBxPfFilterUserEntry.setDisable(true);
			ref_cmbBxFreqFilterUserEntry.setDisable(true);
			ref_cmbBxEnergyFilterUserEntry.setDisable(true);

			ref_txtVoltPercentFilterUserEntry.setText("");
			ref_txtCurrentPercentFilterUserEntry.setText("");
			ref_txtPfFilterUserEntry.setText("");
			ref_txtFreqFilterUserEntry.setText("");
			ref_txtEnergyFilterUserEntry.setText("");

			ref_txtVoltPercentFilterData.setText("");
			ref_txtCurrentPercentFilterData.setText("");
			ref_txtPfFilterData.setText("");
			ref_txtFreqFilterData.setText("");
			ref_txtEnergyFilterData.setText("");

			// if(!ref_chkBxPopulateHeader3.isSelected()){
			// if(!isTestFilterEditMode()){
			ref_txtHeader3Value.setText("");
			ref_chkBxHeader3CustomAllowed.setSelected(false);
			ref_txtIterationReadingStartingId.setText("");
			ref_txtIterationReadingEndingId.setText("");
			ref_chkBx_IterationIdHeaderPrefix.setSelected(false);
			ref_txt_IterationIdHeaderPrefix.setDisable(true);
			ref_txt_IterationIdHeaderPrefix.setText("");
			// }

			ref_chkBxHeader3VoltageFilter.setSelected(false);
			ref_chkBxHeader3CurrentFilter.setSelected(false);
			ref_chkBxHeader3PfFilter.setSelected(false);
			ref_chkBxHeader3FreqFilter.setSelected(false);
			ref_chkBxHeader3EnergyFilter.setSelected(false);
			// ref_chkBxHeader3IterationIdFilter.setSelected(false);
			ref_chkBx_IterationIdHeaderPrefix.setSelected(false);

			ref_chkBxHeader3VoltageFilter.setDisable(true);
			ref_chkBxHeader3CurrentFilter.setDisable(true);
			ref_chkBxHeader3PfFilter.setDisable(true);
			ref_chkBxHeader3FreqFilter.setDisable(true);
			ref_chkBxHeader3EnergyFilter.setDisable(true);
			// ref_chkBxHeader3IterationIdFilter.setDisable(true);
			ref_chkBx_IterationIdHeaderPrefix.setDisable(true);
			ref_chkBxHeader3CustomAllowed.setDisable(true);

		}
	}

	private void enableVoltageFilterGuiObjects() {

		ApplicationLauncher.logger.debug("enableVoltageFilterGuiObjects : Entry");

		if (ref_chkBxPopulateHeader3.isSelected()) {
			ref_chkBxHeader3VoltageFilter.setDisable(false);
		} else {
			ref_chkBxHeader3VoltageFilter.setDisable(true);
		}

		ref_txtVoltPercentFilterUserEntry.setDisable(false);
		ref_cmbBxVoltPercentFilterUserEntry.setDisable(false);

	}

	private void disableVoltageFilterGuiObjects() {

		ApplicationLauncher.logger.debug("disableVoltageFilterGuiObjects : Entry");

		ref_txtVoltPercentFilterUserEntry.setDisable(true);
		ref_cmbBxVoltPercentFilterUserEntry.setDisable(true);

		ref_chkBxHeader3VoltageFilter.setDisable(true);

	}

	private void disableCurrentFilterGuiObjects() {

		ApplicationLauncher.logger.debug("disableCurrentFilterGuiObjects : Entry");

		ref_txtCurrentPercentFilterUserEntry.setDisable(true);
		ref_cmbBxCurrentPercentFilterUserEntry.setDisable(true);

		ref_chkBxHeader3CurrentFilter.setDisable(true);

	}

	private void disablePfFilterGuiObjects() {

		ApplicationLauncher.logger.debug("disableCurrentFilterGuiObjects : Entry");

		ref_txtPfFilterUserEntry.setDisable(true);
		ref_cmbBxPfFilterUserEntry.setDisable(true);

		ref_chkBxHeader3PfFilter.setDisable(true);

	}

	private void disableHeader3WhenTestTypeNoneSelectedGuiObjects() {

		ApplicationLauncher.logger.debug("disableHeader3WhenTestTypeNoneSelectedGuiObjects : Entry");

		ref_chkBxHeader3VoltageFilter.setDisable(true);
		ref_chkBxHeader3CurrentFilter.setDisable(true);
		ref_chkBxHeader3PfFilter.setDisable(true);
		ref_chkBxHeader3FreqFilter.setDisable(true);
		ref_chkBxHeader3EnergyFilter.setDisable(true);
		// ref_chkBxHeader3IterationIdFilter.setDisable(true);
		ref_chkBx_IterationIdHeaderPrefix.setDisable(true);

	}

	private void enablePrimaryFilterGuiObjects() {

		ApplicationLauncher.logger.debug("enablePrimaryFilterGuiObjects : Entry");
		if (ref_chkBxPopulateHeader3.isSelected()) {
			ref_txtVoltPercentFilterUserEntry.setDisable(false);
			ref_cmbBxVoltPercentFilterUserEntry.setDisable(false);
			ref_chkBxHeader3VoltageFilter.setDisable(false);
			ref_chkBxHeader3CustomAllowed.setDisable(false);
		} else {
			// ref_txtVoltPercentFilterUserEntry.setDisable(true);
			// ref_cmbBxVoltPercentFilterUserEntry.setDisable(true);
			ref_chkBxHeader3VoltageFilter.setDisable(true);
			ref_chkBxHeader3CustomAllowed.setDisable(true);
		}

	}

	public void loadTable(TableView<ExcelCellValueModel> table, ArrayList<String> headers, ArrayList<String> values,
			ObservableList<ExcelCellValueModel> table_data) {
		table_data.clear();
		ApplicationLauncher.logger.info("loadTable : table: " + table.getId());
		ApplicationLauncher.logger.info("loadTable : headers: " + headers);
		ApplicationLauncher.logger.info("loadTable : values: " + values);
		if (headers.size() == values.size()) {
			ApplicationLauncher.logger.info("loadTable : Match ");
			for (int i = 0; i < headers.size(); i++) {
				table_data.add(new ExcelCellValueModel(headers.get(i), values.get(i)));
			}
			table.getItems().setAll(table_data);
			// ApplicationLauncher.logger.info("loadTable : updated: ");
			// table.getItems().addAll(table_data);
		} else {
			ApplicationLauncher.logger.info("loadTable : Mismatch ");
			for (int i = 0; i < headers.size(); i++) {
				table_data.add(new ExcelCellValueModel(headers.get(i), ""));
			}
			table.getItems().setAll(table_data);
		}
		// ApplicationLauncher.logger.info("loadTable : Exit");
	}

	public void loadTableV2(TableView<RpPrintPosition> table, ArrayList<String> headers, ArrayList<String> values,
			ObservableList<RpPrintPosition> table_data, String inpDataOwner, boolean populateAllData,
			boolean populateOnlyHeaders) {// String inpDataType){
		table_data.clear();
		ApplicationLauncher.logger.info("loadTableV2 : table: " + table.getId());
		ApplicationLauncher.logger.info("loadTableV2 : headers: " + headers);
		ApplicationLauncher.logger.info("loadTableV2 : values: " + values);
		RpPrintPosition rpPrintPosition = new RpPrintPosition();
		if (headers.size() == values.size()) {
			ApplicationLauncher.logger.info("loadTableV2 : Match ");

			for (int i = 0; i < headers.size(); i++) {
				// table_data.add(new RpPrintPosition(headers.get(i),
				// values.get(i),inpDataOwner,inpDataType));
				rpPrintPosition = new RpPrintPosition();
				rpPrintPosition.setKeyParam(headers.get(i));
				if (headers.get(i).equals(ConstantReportV2.POPULATE_HEADER1_KEY)) {
					rpPrintPosition.setHeaderValue(ref_txtHeader1Value.getText());
				} else if (headers.get(i).equals(ConstantReportV2.POPULATE_HEADER2_KEY)) {
					rpPrintPosition.setHeaderValue(ref_txtHeader2Value.getText());
				} else if (headers.get(i).equals(ConstantReportV2.POPULATE_HEADER3_KEY)) {
					rpPrintPosition.setHeaderValue(ref_txtHeader3Value.getText());
				} else if (headers.get(i).equals(ConstantReportV2.POPULATE_HEADER4_KEY)) {
					rpPrintPosition.setHeaderValue(ref_txtHeader4Value.getText());
				} else if (headers.get(i).equals(ConstantReportV2.POPULATE_HEADER5_KEY)) {
					rpPrintPosition.setHeaderValue(ref_txtHeader5Value.getText());
				}
				rpPrintPosition.setCellPosition(values.get(i));
				rpPrintPosition.setDataOwner(inpDataOwner);
				// rpPrintPosition.setDataType(inpDataType);
				rpPrintPosition.setPopulateAllData(populateAllData);
				rpPrintPosition.setPopulateOnlyHeaders(populateOnlyHeaders);
				table_data.add(rpPrintPosition);
			}
			table.getItems().setAll(table_data);
			// ApplicationLauncher.logger.info("loadTable : updated: ");
			// table.getItems().addAll(table_data);
		} else {
			ApplicationLauncher.logger.info("loadTableV2 : Mismatch ");
			for (int i = 0; i < headers.size(); i++) {
				// table_data.add(new RpPrintPosition(headers.get(i),
				// "",inpDataOwner,inpDataType));
				rpPrintPosition = new RpPrintPosition();
				rpPrintPosition.setKeyParam(headers.get(i));
				rpPrintPosition.setCellPosition("");
				rpPrintPosition.setDataOwner(inpDataOwner);
				// rpPrintPosition.setDataType(inpDataType);
				rpPrintPosition.setPopulateAllData(populateAllData);
				rpPrintPosition.setPopulateOnlyHeaders(populateOnlyHeaders);
				table_data.add(rpPrintPosition);
			}
			table.getItems().setAll(table_data);
		}
		table.refresh();
		// ApplicationLauncher.logger.info("loadTable : Exit");
	}

	@FXML
	public void chkBxPopulateLocalOutputDataOnChange() {
		ApplicationLauncher.logger.debug("chkBxPopulateOutputDataOnChange: Entry");

	}

	@FXML
	public void chkBxCompareWithLimitsOnChange() {
		ApplicationLauncher.logger.debug("chkBxCompareWithLimitsOnChange: Entry");
		// String selectedValue =
		// ref_cmbBxOperationCriteriaMasterOutputData.getSelectionModel().getSelectedItem().toString();
		populateGuiEnableDisableLogic();

	}

	@FXML
	public void chkBxPopulateUpperLimitDataOnChange() {
		ApplicationLauncher.logger.debug("chkBxPopulateUpperLimitDataOnChange: Entry");

	}

	@FXML
	public void chkBxPopulateLowerLimitDataOnChange() {
		ApplicationLauncher.logger.debug("chkBxPopulateLowerLimitDataOnChange: Entry");

	}

	@FXML
	public void chkBxPopulateComparedLocalResultStatusOnChange() {
		ApplicationLauncher.logger.debug("chkBxPopulateComparedResultStatusOnChange: Entry");

	}

	@FXML
	public void chkBxPopulateComparedMasterResultStatusOnChange() {
		ApplicationLauncher.logger.debug("chkBxPopulateComparedMasterResultStatusOnChange: Entry");

	}

	@FXML
	public void chkBxPopulateOutputDataOnChange() {
		ApplicationLauncher.logger.debug("chkBxPopulateOutputDataOnChange: Entry");

	}

	@FXML
	public void cmbBxOperationCriteriaProcessDataOnChange() {
		ApplicationLauncher.logger.debug("cmbBxOperationCriteriaProcessDataOnChange: Entry");
		populateGuiEnableDisableLogic();

	}

	@FXML
	public void chkBxMergeUpperLowerLimitOnChange() {
		ApplicationLauncher.logger.debug("chkBxMergeUpperLowerLimitOnChange: Entry");
		populateGuiEnableDisableLogic();
	}

	@FXML
	public void cmbBxOperationCriteriaMasterOutputDataOnChange() {
		ApplicationLauncher.logger.debug("cmbBxOperationCriteriaMasterOutputDataOnChange: Entry");
		populateGuiEnableDisableLogic();
	}

	@FXML
	public void cmbBxTestTypeOnChange() {
		ApplicationLauncher.logger.debug("cmbBxTestTypeOnChange: Entry");
		String selectedTestType = ref_cmbBxTestType.getSelectionModel().getSelectedItem().toString();
		if (selectedTestType.equals(ConstantApp.DISPLAY_TC_TITLE_CUSTOM_TEST)) {
			ref_lblCustomTestName.setVisible(true);
			ref_txtCustomTestNameUserEntry.setVisible(true);
			ref_txtCustomTestNameData.setVisible(true);
			disablePrimaryFilterGuiObjects();
		} else {
			ref_lblCustomTestName.setVisible(false);
			ref_txtCustomTestNameUserEntry.setVisible(false);
			ref_txtCustomTestNameData.setVisible(false);
			enablePrimaryFilterGuiObjects();
		}
		enableVoltageFilterGuiObjects();
		enablePfFilterGui();
		enableCurrentFilterGui();
		disableFreqFilterGui();
		disableEnergyFilterGui();
		disableIterationFilterGui();

		if (selectedTestType.equals(ConstantApp.DISPLAY_TC_TITLE_INF_FREQUENCY)) {
			enableFreqFilterGui();

		} else if (selectedTestType.equals(ConstantApp.DISPLAY_TC_TITLE_CONST_TEST)) {

			enableEnergyFilterGui();

		} else if (selectedTestType.equals(ConstantApp.DISPLAY_TC_TITLE_REPEATABLITY)) {

			enableIterationFilterGui();

		} else if (selectedTestType.equals(ConstantApp.DISPLAY_TC_TITLE_SELF_HEATING)) {

			enableIterationFilterGui();

		} else if (selectedTestType.equals(ConstantApp.DISPLAY_TC_TITLE_NOLOADTEST)) {

			disablePfFilterGui();
			disableCurrentFilterGui();
		} else if (selectedTestType.equals(ConstantApp.DISPLAY_TC_TITLE_CUSTOM_TEST)) {
			disableVoltageFilterGuiObjects();
			disablePfFilterGui();
			disableCurrentFilterGui();
		}

		if (selectedTestType.equals(ConstantApp.DISPLAY_TC_TITLE_PHASE_REVERSAL)) {
			ref_cmbBxTestTypeSub.setDisable(false);
			ref_cmbBxTestTypeSub.getItems().clear();
			ref_cmbBxTestTypeSub.getItems().addAll(REV_PHASE_SEQ_SUB_LIST);
			ref_cmbBxTestTypeSub.getSelectionModel().selectFirst();

		} else {
			ref_cmbBxTestTypeSub.setDisable(true);
		}

		String selectedPfType = ref_cmbBxPfFilterUserEntry.getSelectionModel().getSelectedItem().toString();
		if (selectedPfType.equals(ConstantApp.PF_UPF)) {
			ref_txtPfFilterUserEntry.setText("1.0");
			ref_txtPfFilterUserEntry.setDisable(true);
			ref_txtPfFilterData.setText("1.0");
		}
	}

	public void enableCurrentFilterGui() {
		ApplicationLauncher.logger.debug("enableCurrentFilterGui: Entry");
		if (ref_chkBxPopulateHeader3.isSelected()) {
			ref_chkBxHeader3CurrentFilter.setDisable(false);
		}
		ref_txtCurrentPercentFilterUserEntry.setDisable(false);
		ref_cmbBxCurrentPercentFilterUserEntry.setDisable(false);
	}

	public void disableCurrentFilterGui() {
		ApplicationLauncher.logger.debug("disableCurrentFilterGui: Entry");
		ref_chkBxHeader3CurrentFilter.setDisable(true);
		ref_txtCurrentPercentFilterUserEntry.setDisable(true);
		ref_cmbBxCurrentPercentFilterUserEntry.setDisable(true);

		ref_chkBxHeader3CurrentFilter.setSelected(false);
		ref_txtCurrentPercentFilterUserEntry.setText("");
		ref_txtCurrentPercentFilterData.setText("");
	}

	public void enablePfFilterGui() {
		ApplicationLauncher.logger.debug("enablePfFilterGui: Entry");
		if (ref_chkBxPopulateHeader3.isSelected()) {
			ref_chkBxHeader3PfFilter.setDisable(false);
			ref_txtPfFilterUserEntry.setDisable(false);
			ref_cmbBxPfFilterUserEntry.setDisable(false);
		}

		ref_txtPfFilterUserEntry.setDisable(false);
		ref_cmbBxPfFilterUserEntry.setDisable(false);
	}

	public void disablePfFilterGui() {
		ApplicationLauncher.logger.debug("disablePfFilterGui: Entry");
		ref_chkBxHeader3PfFilter.setDisable(true);
		ref_txtPfFilterUserEntry.setDisable(true);
		ref_cmbBxPfFilterUserEntry.setDisable(true);

		ref_chkBxHeader3PfFilter.setSelected(false);
		ref_txtPfFilterUserEntry.setText("");
		ref_txtPfFilterData.setText("");
	}

	public void enableFreqFilterGui() {
		ApplicationLauncher.logger.debug("enableFreqFilterGui: Entry");
		if (ref_chkBxPopulateHeader3.isSelected()) {
			ref_chkBxHeader3FreqFilter.setDisable(false);
		}
		ref_txtFreqFilterUserEntry.setDisable(false);
		ref_cmbBxFreqFilterUserEntry.setDisable(false);
		// ref_txtFreqFilterData.setDisable(false);
	}

	public void disableFreqFilterGui() {
		ApplicationLauncher.logger.debug("disableFreqFilterGui: Entry");
		ref_chkBxHeader3FreqFilter.setDisable(true);
		ref_txtFreqFilterUserEntry.setDisable(true);
		ref_cmbBxFreqFilterUserEntry.setDisable(true);
		// ref_txtFreqFilterData.setDisable(true);

		ref_chkBxHeader3FreqFilter.setSelected(false);
		ref_txtFreqFilterUserEntry.setText("");
		ref_txtFreqFilterData.setText("");
	}

	public void enableEnergyFilterGui() {
		ApplicationLauncher.logger.debug("enableEnergyFilterGui: Entry");
		if (ref_chkBxPopulateHeader3.isSelected()) {
			ref_chkBxHeader3EnergyFilter.setDisable(false);
		}
		ref_txtEnergyFilterUserEntry.setDisable(false);
		ref_cmbBxEnergyFilterUserEntry.setDisable(false);
		// ref_txtEnergyFilterData.setDisable(false);
	}

	public void disableEnergyFilterGui() {
		ApplicationLauncher.logger.debug("disableEnergyFilterGui: Entry");
		ref_chkBxHeader3EnergyFilter.setDisable(true);
		ref_txtEnergyFilterUserEntry.setDisable(true);
		ref_cmbBxEnergyFilterUserEntry.setDisable(true);
		// ref_txtEnergyFilterData.setDisable(true);

		ref_chkBxHeader3EnergyFilter.setSelected(false);
		ref_txtEnergyFilterUserEntry.setText("");
		ref_txtEnergyFilterData.setText("");
	}

	public void enableIterationFilterGui() {
		ApplicationLauncher.logger.debug("enableIterationFilterGui: Entry");
		// if(ref_chkBxPopulateHeader3.isSelected()){
		// ref_chkBxHeader3IterationIdFilter.setDisable(false);
		ref_chkBx_IterationIdHeaderPrefix.setDisable(false);
		// }
		ref_txtIterationReadingStartingId.setDisable(false);
		ref_txtIterationReadingEndingId.setDisable(false);
		ref_chkBx_IterationIdHeaderPrefix.setDisable(false);
		ref_txt_IterationIdHeaderPrefix.setDisable(false);
	}

	public void disableIterationFilterGui() {
		ApplicationLauncher.logger.debug("disableIterationFilterGui: Entry");
		// ref_chkBxHeader3IterationIdFilter.setDisable(true);
		ref_chkBx_IterationIdHeaderPrefix.setDisable(true);
		ref_txtIterationReadingStartingId.setDisable(true);
		ref_txtIterationReadingEndingId.setDisable(true);
		// ref_chkBx_IterationIdHeaderPrefix.setDisable(true);
		ref_txt_IterationIdHeaderPrefix.setDisable(true);

		// ref_chkBxHeader3IterationIdFilter.setSelected(false);
		ref_chkBx_IterationIdHeaderPrefix.setSelected(false);
		ref_txtIterationReadingStartingId.setText("");
		ref_txtIterationReadingEndingId.setText("");
		ref_chkBx_IterationIdHeaderPrefix.setSelected(false);
		ref_txt_IterationIdHeaderPrefix.setText("");
	}

	@FXML
	public void btnMeterDataDisplayNextOnClick() {
		ApplicationLauncher.logger.debug("btnMeterDataDisplayNextOnClick: Entry");

		ArrayList<String> cellStartHeaderList = new ArrayList<String>();
		ArrayList<String> cellStartPositionCellList = new ArrayList<String>();

		ArrayList<String> cellHeaderPositionHeaderList = new ArrayList<String>();
		ArrayList<String> cellHeaderPositionCellList = new ArrayList<String>();

		String headerKey = "";
		String pageNumber = ref_txtMeterMetaDataPageNumber.getText();
		boolean discardRackPositionInDutSerialNumber = ref_chkBxDiscardRackPositionInDutSerialNumber.isSelected();
		ReportMeterMetaDataTypeSubModel meterMetaData = new ReportMeterMetaDataTypeSubModel("", "", false, false, false,
				"", pageNumber, "");// ,discardRackPositionInDutSerialNumber);
		getMeterMetaDataList().clear();
		int serialNo = 1;
		if (ref_tvMeterMetaDataList.getItems().size() != 0) {

			serialNo = ref_tvMeterMetaDataList.getItems().size() + 1;
		}
		for (int i = 0; i < ref_tvSelectedMeterDataType.getItems().size(); i++) {
			// ApplicationLauncher.logger.debug("btnMeterDataDisplayNextOnClick:
			// getDataTypeKey : " +
			// ref_tvSelectedMeterDataType.getItems().get(i).getDataTypeKey());
			// ApplicationLauncher.logger.debug("btnMeterDataDisplayNextOnClick:
			// getPopulateData : " +
			// ref_tvSelectedMeterDataType.getItems().get(i).getPopulateData());
			// ApplicationLauncher.logger.debug("btnMeterDataDisplayNextOnClick:
			// getPopulateDataForEachDut : " +
			// ref_tvSelectedMeterDataType.getItems().get(i).getPopulateDataForEachDut());
			meterMetaData = new ReportMeterMetaDataTypeSubModel("", "", false, false, false, "", pageNumber, "");/// ,discardRackPositionInDutSerialNumber);
			// if(ref_tvSelectedMeterDataType.getItems().get(i).getPopulateData()){
			if (!ref_tvSelectedMeterDataType.getItems().get(i).getPopulateDataSelection()
					.equals(ConstantReportV2.NONE_DISPLAYED)) {
				meterMetaData.setPopulateData(true);
				// ApplicationLauncher.logger.debug("btnMeterDataDisplayNextOnClick: Hit1");
				headerKey = ref_tvSelectedMeterDataType.getItems().get(i).getDataTypeKey();
				ApplicationLauncher.logger.debug("btnMeterDataDisplayNextOnClick: headerKey: " + headerKey);
				// if(ref_tvSelectedMeterDataType.getItems().get(i).getPopulateDataForEachDut()){
				if (ref_tvSelectedMeterDataType.getItems().get(i).getPopulateDataSelection()
						.equals(ConstantReportV2.POPULATE_DATA_TYPE_ALL_DUT)) {
					// ApplicationLauncher.logger.debug("btnMeterDataDisplayNextOnClick: Hit2");
					// headerKey = ref_tvSelectedMeterDataType.getItems().get(i).getDataTypeKey();
					cellStartHeaderList.add(headerKey);
					cellStartPositionCellList.add("");
					meterMetaData.setPopulateDataForEachDut(true);

				} else {
					// ApplicationLauncher.logger.debug("btnMeterDataDisplayNextOnClick: Hit3");
					// headerKey = ref_tvSelectedMeterDataType.getItems().get(i).getDataTypeKey();
					cellHeaderPositionHeaderList.add(headerKey);
					cellHeaderPositionCellList.add("");
					meterMetaData.setPopulateOnlyHeader(true);
				}
				// ApplicationLauncher.logger.debug("btnMeterDataDisplayNextOnClick: Hit4");
				// ApplicationLauncher.logger.debug("btnMeterDataDisplayNextOnClick: headerKey:
				// " + headerKey);
				meterMetaData.setDataTypeKey(headerKey);
				meterMetaData.setSerialNo(String.valueOf(serialNo));
				getMeterMetaDataList().add(meterMetaData);
				serialNo++;
			}

		}
		// displayMeterMetaDataList();

		getMeterMetaDataList().forEach(k -> {
			ApplicationLauncher.logger.debug("btnMeterDataDisplayNextOnClick: key: " + k.getDataTypeKey());
			// ApplicationLauncher.logger.debug("btnMeterDataDisplayNextOnClick: value:" +
			// getLastSavedMeterMetaDataCellPosition().get(k.getDataTypeKey()));
			String cellPositionValue = "";
			if (getLastSavedMeterMetaDataCellPosition().containsKey(k.getDataTypeKey())) {
				// ApplicationLauncher.logger.debug("btnMeterDataDisplayNextOnClick: Hit1 ");
				cellPositionValue = getLastSavedMeterMetaDataCellPosition().get(k.getDataTypeKey());
				if (k.getPopulateDataForEachDut()) {
					int index = cellStartHeaderList.indexOf(k.getDataTypeKey());
					cellStartPositionCellList.set(index, cellPositionValue);
				} else if (k.isPopulateOnlyHeader()) {
					int index = cellHeaderPositionHeaderList.indexOf(k.getDataTypeKey());
					cellHeaderPositionCellList.set(index, cellPositionValue);
				}
			}

		});

		if (cellStartHeaderList.size() > 0) {
			loadTable(ref_tvMeterMetaDataCellStartPosition, cellStartHeaderList, cellStartPositionCellList,
					cellStartPositionPageCellValues);
		} else {
			ref_tvMeterMetaDataCellStartPosition.getItems().clear();
			ref_tvMeterMetaDataCellStartPosition.refresh();
		}

		if (cellHeaderPositionHeaderList.size() > 0) {
			// ref_titledPaneMeterDataCellHeaderPosition.setVisible(true);
			loadTable(ref_tvMeterMetaDataCellHeaderPosition, cellHeaderPositionHeaderList, cellHeaderPositionCellList,
					cellStartPositionPageCellValues);
		} else {
			ref_tvMeterMetaDataCellHeaderPosition.getItems().clear();
			ref_tvMeterMetaDataCellHeaderPosition.refresh();
			// ref_titledPaneMeterDataCellHeaderPosition.setVisible(false);
		}

		ref_titledPaneMeterDataCellPosition.setExpanded(true);
	}

	public void populateGuiEnableDisableLogic() {
		ApplicationLauncher.logger.debug("populateGuiEnableDisableLogic: Entry ");
		/*
		 * if(ref_chkBxCompareWithLimits.isSelected()){
		 * if(selectedValue.equals(NONE_DISPLAYED)){
		 * ref_txtAllowedUpperLimit.setDisable(false);
		 * ref_txtAllowedLowerLimit.setDisable(false);
		 * ref_cmbBxOperationComparedResultStatusOutput.setDisable(false);
		 * ref_chkBxPopulateUpperLimitData.setDisable(false);
		 * ref_chkBxPopulateLowerLimitData.setDisable(false);
		 * ref_chkBxPopulateComparedResultStatus.setDisable(false);
		 * }
		 * }else{
		 * if(selectedValue.equals(NONE_DISPLAYED)){
		 * ref_txtAllowedUpperLimit.setDisable(true);
		 * ref_txtAllowedLowerLimit.setDisable(true);
		 * ref_cmbBxOperationComparedResultStatusOutput.setDisable(true);
		 * ref_chkBxPopulateUpperLimitData.setDisable(true);
		 * ref_chkBxPopulateLowerLimitData.setDisable(true);
		 * ref_chkBxPopulateComparedResultStatus.setDisable(true);
		 * }
		 * }
		 */
		/*
		 * String selectedMasterOutputValue =
		 * ref_cmbBxOperationCriteriaMasterOutputData.getSelectionModel().
		 * getSelectedItem().toString();
		 * String selectedOperation =
		 * cmbBxOperationCriteriaProcessData.getSelectionModel().getSelectedItem().
		 * toString();
		 * 
		 * if(selectedMasterOutputValue.equals(NONE_DISPLAYED)){
		 * ref_chkBxPopulateMasterOutputData.setDisable(true);
		 * ref_chkBxPopulateLocalOutputData.setDisable(false);
		 * ref_cmbBxOperationCriteriaLocalOutputData.setDisable(false);
		 * ref_txtAllowedUpperLimit.setDisable(false);
		 * ref_txtAllowedLowerLimit.setDisable(false);
		 * ref_cmbBxOperationComparedResultStatusOutput.setDisable(false);
		 * if(ref_chkBxCompareWithLimits.isSelected()){
		 * ref_txtAllowedUpperLimit.setDisable(false);
		 * ref_txtAllowedLowerLimit.setDisable(false);
		 * ref_cmbBxOperationComparedResultStatusOutput.setDisable(false);
		 * ref_chkBxPopulateUpperLimitData.setDisable(false);
		 * ref_chkBxPopulateLowerLimitData.setDisable(false);
		 * ref_chkBxPopulateComparedResultStatus.setDisable(false);
		 * }else{
		 * ref_txtAllowedUpperLimit.setDisable(true);
		 * ref_txtAllowedLowerLimit.setDisable(true);
		 * ref_cmbBxOperationComparedResultStatusOutput.setDisable(true);
		 * ref_chkBxPopulateUpperLimitData.setDisable(true);
		 * ref_chkBxPopulateLowerLimitData.setDisable(true);
		 * ref_chkBxPopulateComparedResultStatus.setDisable(true);
		 * }
		 * }else{
		 * ref_chkBxPopulateMasterOutputData.setDisable(false);
		 * ref_chkBxPopulateLocalOutputData.setDisable(true);
		 * ref_cmbBxOperationCriteriaLocalOutputData.setDisable(true);
		 * ref_txtAllowedUpperLimit.setDisable(true);
		 * ref_txtAllowedLowerLimit.setDisable(true);
		 * ref_cmbBxOperationComparedResultStatusOutput.setDisable(true);
		 * if(ref_chkBxCompareWithLimits.isSelected()){
		 * ref_txtAllowedUpperLimit.setDisable(false);
		 * ref_txtAllowedLowerLimit.setDisable(false);
		 * ref_cmbBxOperationComparedResultStatusOutput.setDisable(false);
		 * ref_chkBxPopulateUpperLimitData.setDisable(false);
		 * ref_chkBxPopulateLowerLimitData.setDisable(false);
		 * ref_chkBxPopulateComparedResultStatus.setDisable(false);
		 * }else{
		 * ref_txtAllowedUpperLimit.setDisable(true);
		 * ref_txtAllowedLowerLimit.setDisable(true);
		 * ref_cmbBxOperationComparedResultStatusOutput.setDisable(true);
		 * ref_chkBxPopulateUpperLimitData.setDisable(true);
		 * ref_chkBxPopulateLowerLimitData.setDisable(true);
		 * ref_chkBxPopulateComparedResultStatus.setDisable(true);
		 * }
		 * }
		 */

		if (!isTestFilterEditMode()) {
			String selectedMasterOutputValue = (String) ref_cmbBxOperationCriteriaMasterOutputData.getSelectionModel()
					.getSelectedItem();
			String selectedOperation = (String) ref_cmbBxOperationCriteriaProcessData.getSelectionModel()
					.getSelectedItem();
			// if(selectedOperation.equals(null)){
			// fdds
			// }
			/*
			 * if(selectedMasterOutputValue.equals(NONE_DISPLAYED)){
			 * ref_chkBxPopulateMasterOutputData.setDisable(true);
			 * ref_chkBxPopulateLocalOutputData.setDisable(false);
			 * ref_cmbBxOperationCriteriaLocalOutputData.setDisable(false);
			 * ref_txtAllowedUpperLimit.setDisable(false);
			 * ref_txtAllowedLowerLimit.setDisable(false);
			 * ref_cmbBxOperationComparedResultStatusOutput.setDisable(false);
			 */
			if (ref_chkBxCompareWithLimits.isSelected()) {

				ref_chkBxPopulateUpperLimitData.setDisable(false);
				ref_chkBxPopulateLowerLimitData.setDisable(false);
				ref_chkBxPopulateComparedLocalResultStatus.setDisable(false);
				ref_chkBxMergeUpperLowerLimit.setDisable(false);
				if (selectedOperation != null) {
					if (selectedOperation.equals(ConstantReportV2.NONE_DISPLAYED)) {

						ref_btnInputAdd.setDisable(true);
						ref_btnInputDelete.setDisable(true);

						/// ref_listViewInputProcessList.getItems().clear();
						if (selectedMasterOutputValue != null) {
							if (selectedMasterOutputValue.equals(ConstantReportV2.NONE_DISPLAYED)) {

								ref_chkBxPopulateMasterOutputData.setDisable(true);
								ref_chkBxPopulateMasterOutputData.setSelected(false);
								ref_chkBxPopulateLocalOutputData.setDisable(false);
								ref_cmbBxOperationCriteriaLocalOutputData.setDisable(false);
								ref_txtAllowedUpperLimit.setDisable(false);
								ref_txtAllowedLowerLimit.setDisable(false);
								// ref_cmbBxOperationComparedLocalResultStatusOutput.setDisable(false);
								// ref_cmbBxOperationComparedMasterResultStatusOutput.setDisable(true);
								ref_chkBxPopulateComparedMasterResultStatus.setDisable(true);
							} else {
								ref_chkBxPopulateMasterOutputData.setDisable(false);
								ref_chkBxPopulateLocalOutputData.setDisable(true);
								ref_chkBxPopulateLocalOutputData.setSelected(false);
								ref_cmbBxOperationCriteriaLocalOutputData.setDisable(true);
								ref_txtAllowedUpperLimit.setDisable(true);
								ref_txtAllowedLowerLimit.setDisable(true);
								// ref_cmbBxOperationComparedLocalResultStatusOutput.setDisable(true);
								// ref_cmbBxOperationComparedMasterResultStatusOutput.setDisable(false);
								ref_chkBxPopulateComparedMasterResultStatus.setDisable(false);

								ref_cmbBxOperationComparedLocalResultStatusOutput.setDisable(true);

								ref_chkBxPopulateComparedLocalResultStatus.setDisable(true);
								ref_chkBxPopulateComparedLocalResultStatus.setSelected(false);
							}
						}

						// ref_chkBxPopulateLocalOutputData.setDisable(false);
						/*
						 * ref_cmbBxOperationCriteriaLocalOutputData.setDisable(false);
						 * ref_txtAllowedUpperLimit.setDisable(false);
						 * ref_txtAllowedLowerLimit.setDisable(false);
						 * ref_cmbBxOperationComparedResultStatusOutput.setDisable(false);
						 */
					} else {
						ref_chkBxPopulateMasterOutputData.setDisable(true);
						ref_chkBxPopulateMasterOutputData.setSelected(false);
						ref_chkBxPopulateLocalOutputData.setDisable(false);
						ref_cmbBxOperationCriteriaLocalOutputData.setDisable(false);
						ref_txtAllowedUpperLimit.setDisable(false);
						ref_txtAllowedLowerLimit.setDisable(false);
						ref_cmbBxOperationComparedLocalResultStatusOutput.setDisable(false);

						ref_chkBxPopulateComparedMasterResultStatus.setDisable(true);
						ref_chkBxPopulateComparedMasterResultStatus.setSelected(false);

						ref_btnInputAdd.setDisable(false);
						ref_btnInputDelete.setDisable(false);

					}
				}
				if (ref_chkBxMergeUpperLowerLimit.isSelected()) {
					ref_chkBxPopulateLowerLimitData.setDisable(true);
					ref_chkBxPopulateLowerLimitData.setSelected(false);
				} else {
					ref_chkBxPopulateLowerLimitData.setDisable(false);
				}
			} else { // if(!ref_chkBxCompareWithLimits.isSelected()){

				ref_chkBxPopulateUpperLimitData.setDisable(true);
				ref_chkBxPopulateUpperLimitData.setSelected(false);
				ref_chkBxPopulateLowerLimitData.setDisable(true);
				ref_chkBxPopulateLowerLimitData.setSelected(false);
				ref_chkBxPopulateComparedLocalResultStatus.setDisable(true);
				ref_chkBxPopulateComparedLocalResultStatus.setSelected(false);

				// ref_chkBxPopulateComparedMasterResultStatus.setDisable(true);
				// ref_chkBxPopulateComparedMasterResultStatus.setSelected(false);
				// ref_cmbBxOperationCriteriaLocalOutputData.setDisable(true);
				ref_txtAllowedUpperLimit.setDisable(true);
				ref_txtAllowedLowerLimit.setDisable(true);
				ref_cmbBxOperationComparedLocalResultStatusOutput.setDisable(true);
				ref_chkBxMergeUpperLowerLimit.setDisable(true);
				ref_chkBxMergeUpperLowerLimit.setSelected(false);
				if (selectedOperation != null) {
					if (selectedOperation.equals(ConstantReportV2.NONE_DISPLAYED)) {

						// ref_btnInputAdd.setDisable(true);//commented in Procal version s4.2.0.3.0.2
						// ref_btnInputDelete.setDisable(true);//commented in Procal version
						// s4.2.0.3.0.2
						// ref_listViewInputProcessList.getItems().clear();
						if (selectedMasterOutputValue != null) {
							if (selectedMasterOutputValue.equals(ConstantReportV2.NONE_DISPLAYED)) {
								ref_chkBxPopulateComparedMasterResultStatus.setDisable(true);
								ref_chkBxPopulateComparedMasterResultStatus.setSelected(false);
								ref_chkBxPopulateMasterOutputData.setDisable(true);
								ref_chkBxPopulateMasterOutputData.setSelected(false);
								ref_chkBxPopulateLocalOutputData.setDisable(false);
								// ref_chkBxPopulateMasterOutputData.setDisable(false);
								ref_cmbBxOperationCriteriaLocalOutputData.setDisable(false);

								/*
								 * ref_txtAllowedUpperLimit.setDisable(false);
								 * ref_txtAllowedLowerLimit.setDisable(false);
								 * ref_cmbBxOperationComparedResultStatusOutput.setDisable(false);
								 */
							} else {
								ref_chkBxPopulateMasterOutputData.setDisable(false);
								// ref_chkBxPopulateMasterOutputData.setSelected(false);
								ref_chkBxPopulateLocalOutputData.setDisable(true);
								ref_chkBxPopulateLocalOutputData.setSelected(false);
								// ref_chkBxPopulateMasterOutputData.setDisable(true);

								ref_cmbBxOperationCriteriaLocalOutputData.setDisable(true);
								ApplicationLauncher.logger.debug("populateGuiEnableDisableLogic: Hit1");
								if (ref_listViewInputProcessList.getItems().size() > 0) {
									ApplicationLauncher.logger.debug("populateGuiEnableDisableLogic: Hit2");
									List<String> selectedLocalInputValueList = ref_listViewInputProcessList.getItems();// .get(0);
									// String selectedLocalInputValue =
									// ref_cmbBxOperationCriteriaInputData.getSelectionModel().getSelectedItem().toString();

									/*
									 * boolean localInputAverageType =
									 * getOperationParameterProfileDataList().stream()
									 * .filter(e -> e.getKeyParam().equals(selectedLocalInputValue))
									 * .anyMatch(e -> e.getPopulateType().equals(ConstantReportV2.
									 * POPULATE_DATA_TYPE_ALL_DUT_AVERAGE));
									 * 
									 * boolean masterInputAverageType =
									 * getOperationParameterProfileDataList().stream()
									 * .filter(e -> e.getKeyParam().equals(selectedMasterOutputValue))
									 * .anyMatch(e -> e.getPopulateType().equals(ConstantReportV2.
									 * POPULATE_DATA_TYPE_ALL_DUT_AVERAGE));
									 * 
									 * if(localInputAverageType && masterInputAverageType){uiouyo
									 * ref_chkBxPopulateComparedMasterResultStatus.setDisable(false);
									 * }else{
									 * ref_chkBxPopulateComparedMasterResultStatus.setDisable(true);
									 * ref_chkBxPopulateComparedMasterResultStatus.setSelected(false);
									 * //ApplicationLauncher.logger.
									 * debug("isOutputProcessPropertyMatching: Operation process: Local Input and Master Output data type not matching with Average. Kindly check the property"
									 * );
									 * }
									 */
									// ref_chkBxPopulateComparedMasterResultStatus.setDisable(true);
									// ref_chkBxPopulateComparedMasterResultStatus.setSelected(false);
									boolean inputKeywithOnlyHeadersIdentified = false;
									for (String eachSelectedInputKey : selectedLocalInputValueList) {
										String selectedLocalInputValue = eachSelectedInputKey;
										boolean localInputHeadersTypeFound = getOperationParameterProfileDataList()
												.stream()
												.filter(e -> e.getKeyParam().equals(selectedLocalInputValue))
												.anyMatch(e -> e.getPopulateType()
														.equals(ConstantReportV2.POPULATE_DATA_TYPE_ONLY_HEADERS));
										if (localInputHeadersTypeFound) {
											inputKeywithOnlyHeadersIdentified = true;
											break;
										}
									}
									if (!inputKeywithOnlyHeadersIdentified) {
										ref_chkBxPopulateComparedMasterResultStatus.setDisable(false);
									}
								} else {
									ref_chkBxPopulateComparedMasterResultStatus.setDisable(true);
									ref_chkBxPopulateComparedMasterResultStatus.setSelected(false);
									ref_chkBxPopulateMasterOutputData.setSelected(false);
								}

								/*
								 * ref_txtAllowedUpperLimit.setDisable(true);
								 * ref_txtAllowedLowerLimit.setDisable(true);
								 * ref_cmbBxOperationComparedResultStatusOutput.setDisable(true);
								 */
							}
						}

					} else {
						ref_chkBxPopulateMasterOutputData.setDisable(true);
						ref_chkBxPopulateMasterOutputData.setSelected(false);
						ref_chkBxPopulateLocalOutputData.setDisable(false);
						ref_cmbBxOperationCriteriaLocalOutputData.setDisable(false);

						ref_btnInputAdd.setDisable(false);
						ref_btnInputDelete.setDisable(false);
						/*
						 * ref_txtAllowedUpperLimit.setDisable(false);
						 * ref_txtAllowedLowerLimit.setDisable(false);
						 * ref_cmbBxOperationComparedResultStatusOutput.setDisable(false);
						 */

					}
				}

			}
		}

	}

	public ArrayList<ReportMeterMetaDataTypeSubModel> getMeterMetaDataList() {
		return meterMetaDataList;
	}

	public void setMeterMetaDataList(ArrayList<ReportMeterMetaDataTypeSubModel> meterMetaDataList) {
		this.meterMetaDataList = meterMetaDataList;
	}

	public HashMap<String, ReportProfileMeterMetaDataModel> getReportProfileMeterMetaDataHashMap() {
		return reportProfileMeterMetaDataHashMap;
	}

	public void addReportProfileMeterMetaDataHashMap(String pageNo, ReportProfileMeterMetaDataModel modelData) {
		reportProfileMeterMetaDataHashMap.put(pageNo, modelData);
	}

	public void setReportProfileMeterMetaDataHashMap(
			HashMap<String, ReportProfileMeterMetaDataModel> reportProfileMeterMetaDataModelList) {
		this.reportProfileMeterMetaDataHashMap = reportProfileMeterMetaDataModelList;
	}

	public HashMap<String, String> getLastSavedMeterMetaDataCellPosition() {
		return lastSavedMeterMetaDataCellPosition;
	}

	public void updateLastSavedMeterMetaDataCellPosition(String dataKey, String cellPosition) {
		lastSavedMeterMetaDataCellPosition.put(dataKey, cellPosition);
	}

	public void setLastSavedMeterMetaDataCellPosition(HashMap<String, String> lastSavedMeterMetaDataCellPosition) {
		this.lastSavedMeterMetaDataCellPosition = lastSavedMeterMetaDataCellPosition;
	}

	public void clearLastSavedMeterMetaDataCellPosition() {
		this.lastSavedMeterMetaDataCellPosition.clear();
	}

	/*
	 * public MultiValuedMap<String, ReportProfileTestFilterDataModel>
	 * getReportProfileTestFilterDataHashMap() {
	 * return reportProfileTestFilterDataHashMap;
	 * }
	 * 
	 * public void updateReportProfileTestFilterDataHashMap(
	 * String pageNo, ReportProfileTestFilterDataModel
	 * reportProfileTestFilterDataHashMap) {
	 * this.reportProfileTestFilterDataHashMap.put(pageNo,
	 * reportProfileTestFilterDataHashMap);
	 * }
	 * 
	 * public void setReportProfileTestFilterDataHashMap(
	 * MultiValuedMap<String, ReportProfileTestFilterDataModel>
	 * reportProfileTestFilterDataHashMap) {
	 * this.reportProfileTestFilterDataHashMap = reportProfileTestFilterDataHashMap;
	 * }
	 */
	/*
	 * public ArrayList<ExcelCellValueModel> getTestfilterCellPositionDataList() {
	 * return testfilterCellPositionDataList;
	 * }
	 * 
	 * public void updateTestfilterCellPositionDataList(ExcelCellValueModel
	 * testfilterCellPositionData) {
	 * this.testfilterCellPositionDataList.add(testfilterCellPositionData);
	 * }
	 * 
	 * public void setTestfilterCellPositionDataList(ArrayList<ExcelCellValueModel>
	 * testfilterCellPositionDataList) {
	 * this.testfilterCellPositionDataList = testfilterCellPositionDataList;
	 * }
	 */

	public void clearLastSavedTestFilterDataCellPositionHashMap() {
		lastSavedTestFilterDataCellPositionHashMap.clear();
	}

	public HashMap<String, String> getLastSavedTestFilterDataCellPositionHashMap() {
		return lastSavedTestFilterDataCellPositionHashMap;
	}

	public void updateLastSavedTestFilterDataCellPositionHashMap(String dataKey, String cellPosition) {
		lastSavedTestFilterDataCellPositionHashMap.put(dataKey, cellPosition);
	}

	public void setLastSavedTestFilterDataCellPositionHashMap(
			HashMap<String, String> lastSavedTestFilterDataCellPosition) {
		this.lastSavedTestFilterDataCellPositionHashMap = lastSavedTestFilterDataCellPosition;
	}

	public HashMap<String, String> getTestFilterDataCellPositionHashMap() {
		return testFilterDataCellPositionHashMap;
	}

	public void updateTestFilterDataCellPositionHashMap(String dataKey, String cellPosition) {
		testFilterDataCellPositionHashMap.put(dataKey, cellPosition);
	}

	public void setTestFilterDataCellPositionHashMap(HashMap<String, String> testFilterDataCell) {
		this.testFilterDataCellPositionHashMap = testFilterDataCell;
	}

	public static ArrayList<String> getPresentInputProcessList() {
		return presentInputProcessList;
	}

	public static void setPresentInputProcessList(ArrayList<String> presentInputProcessList) {
		ReportProfileConfigController.presentInputProcessList = presentInputProcessList;
	}

	public static void clearPresentInputProcessList() {
		ReportProfileConfigController.presentInputProcessList.clear();
	}

	@FXML
	public void cmbBxOperationCriteriaLocalOutputDataOnChange() {
		ApplicationLauncher.logger.debug("cmbBxOperationCriteriaLocalOutputDataOnChange: Entry");
		/*
		 * String selectedLocalOutputData =
		 * (String)ref_cmbBxOperationCriteriaLocalOutputData.getSelectionModel().
		 * getSelectedItem();
		 * if(selectedLocalOutputData != null){
		 * String localOutputStatus = selectedLocalOutputData.replace(
		 * OPERATION_LOCAL_OUTPUT_DATA_HEADER_DISPLAY_PREFIX,
		 * OPERATION_LOCAL_OUTPUT_STATUS_HEADER_DISPLAY_PREFIX);
		 * ref_cmbBxOperationComparedLocalResultStatusOutput.getSelectionModel().select(
		 * localOutputStatus);
		 * }
		 */

	}

	@FXML
	public void rdBtnMainCtOnChange() {
		ApplicationLauncher.logger.debug("rdBtnMainCtOnChange: Entry");
		if (ref_rdBtnMainCt.isSelected()) {
			ref_rdBtnNeutralCt.setSelected(false);
		} else {
			// ref_cmbBxOperationOutputUserEntry.setDisable(true);
			ref_rdBtnMainCt.setSelected(true);

		}
	}

	@FXML
	public void rdBtnNeutralCtOnChange() {
		ApplicationLauncher.logger.debug("rdBtnNeutralCtOnChange: Entry");
		if (ref_rdBtnNeutralCt.isSelected()) {
			ref_rdBtnMainCt.setSelected(false);
		} else {

			ref_rdBtnNeutralCt.setSelected(true);

		}
	}

	@FXML
	public void rdBtnImportModeOnChange() {
		ApplicationLauncher.logger.debug("rdBtnImportModeOnChange: Entry");
		if (ref_rdBtnImportMode.isSelected()) {
			ref_rdBtnExportMode.setSelected(false);
		} else {

			ref_rdBtnImportMode.setSelected(true);

		}
	}

	@FXML
	public void rdBtnExportModeOnChange() {
		ApplicationLauncher.logger.debug("rdBtnExportModeOnChange: Entry");
		if (ref_rdBtnExportMode.isSelected()) {
			ref_rdBtnImportMode.setSelected(false);
		} else {

			ref_rdBtnExportMode.setSelected(true);

		}
	}

	@FXML
	public void chkBxPostOperationActiveOnChange() {
		ApplicationLauncher.logger.debug("chkBxPostOperationActiveOnChange: Entry");
		if (ref_chkBxPostOperationActive.isSelected()) {
			ref_cmbBxPostOperationMethod.setDisable(false);
			ref_txtPostOperationValue.setDisable(false);
		} else {
			ref_cmbBxPostOperationMethod.setDisable(true);
			ref_txtPostOperationValue.setDisable(true);
		}
	}

	public static ReportProfileMeterMetaDataFilterService getReportProfileMeterMetaDataFilterService() {
		return reportProfileMeterMetaDataFilterService;
	}

	public static void setReportProfileMeterMetaDataFilterService(
			ReportProfileMeterMetaDataFilterService reportProfileMeterMetaDataFilterService) {
		ReportProfileConfigController.reportProfileMeterMetaDataFilterService = reportProfileMeterMetaDataFilterService;
	}

	public static ReportProfileManageService getReportProfileManageService() {
		return reportProfileManageService;
	}

	public static void setReportProfileManageService(ReportProfileManageService reportProfileManageService) {
		ReportProfileConfigController.reportProfileManageService = reportProfileManageService;
	}

	public ReportProfileManage getReportProfileManageModel() {
		return reportProfileManageModel;
	}

	public void setReportProfileManageModel(ReportProfileManage reportProfileManageModel) {
		ReportProfileConfigController.reportProfileManageModel = reportProfileManageModel;
	}

	public List<ReportProfileManage> getActiveReportProfileDatabaseList() {
		return activeReportProfileDatabaseList;
	}

	public void setActiveReportProfileDatabaseList(List<ReportProfileManage> activeReportProfileDatabaseList) {
		this.activeReportProfileDatabaseList = activeReportProfileDatabaseList;
	}

	public MultiValuedMap<String, String> getGroupProfileNameHashMap() {
		return groupProfileNameHashMap;
	}

	public void setGroupProfileNameHashMap(MultiValuedMap<String, String> groupProfileNameHashMap) {
		this.groupProfileNameHashMap = groupProfileNameHashMap;
	}

	@FXML
	public void cmbBxReportProfileGroupOnchange() {
		ApplicationLauncher.logger.debug("cmbBxReportProfileGroupOnchange: Entry");

		try {
			String lastSelectedGroupName = ref_cmbBxReportProfileGroup.getSelectionModel().getSelectedItem().toString();

			ApplicationLauncher.logger
					.info("cmbBxReportProfileGroupOnchange: lastSelectedGroupName : " + lastSelectedGroupName);
			// ApplicationLauncher.logger.info("loadReportProfileList: groupNameHashMap : "
			// +groupNameHashMap);

			// String profileNameList = groupNameHashMap.entries().stream().filter(e ->
			// e.getKey().equals(lastProfileGroupName)).findFirst().get().getValue();

			Collection<String> reportFileNameList = getGroupProfileNameHashMap().get(lastSelectedGroupName);

			ref_cmbBxReportProfile.getItems().clear();
			reportFileNameList.stream().forEach(value -> {

				ref_cmbBxReportProfile.getItems().add(value);
			});

			ref_cmbBxReportProfile.getSelectionModel().selectLast();
		} catch (Exception e) {
			ApplicationLauncher.logger.error("cmbBxReportProfileGroupOnchange: Exception : " + e.getMessage());
		}

	}

	@FXML
	public void btnReportProfileLoadOnClick() {
		ApplicationLauncher.logger.debug("btnReportProfileLoadOnClick: Entry");

		updateSelectedReportProfileData();
		updateCommonGuiData();
		publishDataOnMeterProfileMetaDataList();
		publishDataOnTestFilterDataList();

	}

	private void updateCommonGuiData() {

		ApplicationLauncher.logger.debug("updateCommonGuiData: Entry");
		ref_txtTemplateFileNameWithPath.setText(getReportProfileManageModel().getTemplateFileNameWithPath());
		ref_txtOutputPath.setText(getReportProfileManageModel().getOutputFolderPath());
		ref_txtMaxDutDisplayPerPage.setText(String.valueOf(getReportProfileManageModel().getMaxDutDisplayPerPage()));
		ref_chkBxSplitDutDisplayInToMultiplePage
				.setSelected(getReportProfileManageModel().isSplitDutDisplayInToMultiplePage());

		String paramProfileName = getReportProfileManageModel().getParameterProfileName();
		ref_rdBtnMainCt.setSelected(getReportProfileManageModel().isDutMainCtSelected());
		ref_rdBtnNeutralCt.setSelected(getReportProfileManageModel().isDutNeutralCtSelected());
		ref_rdBtnImportMode.setSelected(getReportProfileManageModel().isImportModeSelected());
		ref_rdBtnExportMode.setSelected(getReportProfileManageModel().isExportModeSelected());
		ref_chkBxAppendMeterSerialNoAndRackPositionListDisplay
				.setSelected(getReportProfileManageModel().isAppendDutSerialNoAndRackPosition());
		ref_chkBxDutMetaDataApplyForAllPages.setSelected(getReportProfileManageModel().isDutMetaDataApplyForAllPages());
		ref_chkBxDutMetaDataClubPageNoAndMaxNoOfPages
				.setSelected(getReportProfileManageModel().isDutMetaDataClubPageNoAndMaxNoOfPages());
		ref_cmbBxParameterProfileName.getSelectionModel().select(paramProfileName);

		ref_cmbBxResultPrintStyleName.getSelectionModel().select(getReportProfileManageModel().getPrintStyleResult());
		ref_cmbBxGenericHeaderPrintStyleName.getSelectionModel()
				.select(getReportProfileManageModel().getPrintStyleGenericHeader());
		ref_cmbBxTableHeaderPrintStyleName.getSelectionModel()
				.select(getReportProfileManageModel().getPrintStyleTableHeader());

		loadParameterProfileDataFromDataBase(paramProfileName);
	}

	public void loadParameterProfileDataFromDataBase(String paramProfileName) {
		ApplicationLauncher.logger.debug("loadParameterProfileDataFromDataBase: Entry ");
		// ref_tvOperationParamProfile.getItems().clear();
		String customerId = ConstantAppConfig.REPORT_PROFILE_DEFAULT_ACTIVE_CUSTOMER_ID;
		// String paramProfileName = (String)
		// ref_cmbBxOperationParamProfileName.getSelectionModel().getSelectedItem();
		List<OperationParam> operationParamDataList = getReportOperationParamService()
				.findByCustomerIdAndOperationParamProfileName(customerId, paramProfileName);
		// int maxSerialNo = 0;
		// setSerialNo(new AtomicInteger(0));

		ArrayList<OperationParam> operationParamProfileDataList = new ArrayList<OperationParam>();
		operationParamDataList.stream().forEach(e -> {
			if (e.isPopulateOnlyHeaders()) {
				e.setPopulateType(ConstantReportV2.POPULATE_DATA_TYPE_ONLY_HEADERS);
			}
			/*
			 * else if(e.isResultTypeAverage()){
			 * e.setPopulateType(ConstantReportV2.POPULATE_DATA_TYPE_ALL_DUT_AVERAGE);
			 * }
			 */
			else if (e.isPopulateAllDut()) {
				e.setPopulateType(ConstantReportV2.POPULATE_DATA_TYPE_ALL_DUT);
			}

			ApplicationLauncher.logger
					.debug("loadParameterProfileDataFromDataBase: getPopulateType: " + e.getPopulateType());
			// if(Integer.parseInt(e.getTableSerialNo()) > getSerialNo().get()){
			// setSerialNo(new AtomicInteger(Integer.parseInt(e.getTableSerialNo())));
			// }
			operationParamProfileDataList.add(e);
		});

		setOperationParameterProfileDataList(operationParamProfileDataList);

	}

	private void updateSelectedReportProfileData() {

		ApplicationLauncher.logger.debug("updateSelectedReportProfileData: Entry");

		String selectedGroupName = ref_cmbBxReportProfileGroup.getSelectionModel().getSelectedItem().toString();
		String selectedProfileName = ref_cmbBxReportProfile.getSelectionModel().getSelectedItem().toString();
		String selectedBaseTemplate = ref_cmbBxBaseTemplate.getSelectionModel().getSelectedItem().toString();

		ReportProfileManage reportProfileManageData = getActiveReportProfileDatabaseList().stream()
				.filter(e -> e.getReportGroupName().equals(selectedGroupName))
				.filter(e -> e.getReportProfileName().equals(selectedProfileName))
				.filter(e -> e.getBaseTemplateName().equals(selectedBaseTemplate))
				.findFirst()
				.get();
		// .collect(Collectors.toList());

		// ApplicationLauncher.logger.debug("updateSelectedReportProfileData:
		// reportProfileManageData.size(): " + reportProfileManageData.size());

		// if(reportProfileManageData.size()>0){

		// ApplicationLauncher.logger.debug("updateSelectedReportProfileData:
		// reportProfileManageData.get(0).getId(): " +
		// reportProfileManageData.get(0).getId());
		setReportProfileManageModel(reportProfileManageData);// .get(0));

		ApplicationLauncher.logger
				.debug("updateSelectedReportProfileData: getId: " + getReportProfileManageModel().getId());
		ApplicationLauncher.logger.debug("updateSelectedReportProfileData: getReportGroupName: "
				+ getReportProfileManageModel().getReportGroupName());
		ApplicationLauncher.logger.debug("updateSelectedReportProfileData: getReportProfileName: "
				+ getReportProfileManageModel().getReportProfileName());
		ApplicationLauncher.logger.debug("updateSelectedReportProfileData: getDutMetaDataList().size(): "
				+ getReportProfileManageModel().getDutMetaDataList().size());

		// }
	}

	private void publishDataOnTestFilterDataList() {

		ApplicationLauncher.logger.debug("publishDataOnTestFilterDataList: Entry");
		// convertDatabaseModelToMeterProfileDataDisplayModel();
		// ReportProfileMeterMetaDataModel meterMetaDataDisplayModel =
		// convertDatabaseModelToMeterProfileDataDisplayModel();
		// refreshGuiTestFilterDataList(meterMetaDataDisplayModel);
		ref_tvTestFilterDataList.getItems().clear();
		for (int i = 0; i < getReportProfileManageModel().getReportProfileTestDataFilterList().size(); i++) {
			List<RpPrintPosition> rpPrintPositionDataList = getReportProfileManageModel()
					.getReportProfileTestDataFilterList().get(i).getRpPrintPositionList();
			for (RpPrintPosition eachRpPrintPosition : rpPrintPositionDataList) {
				if (eachRpPrintPosition.getKeyParam().equals(ConstantReportV2.POPULATE_HEADER1_KEY)) {
					getReportProfileManageModel().getReportProfileTestDataFilterList().get(i)
							.setHeader1_CellPosition(eachRpPrintPosition.getCellPosition());
				} else if (eachRpPrintPosition.getKeyParam().equals(ConstantReportV2.POPULATE_HEADER2_KEY)) {
					getReportProfileManageModel().getReportProfileTestDataFilterList().get(i)
							.setHeader2_CellPosition(eachRpPrintPosition.getCellPosition());
				} else if (eachRpPrintPosition.getKeyParam().equals(ConstantReportV2.POPULATE_HEADER3_KEY)) {
					getReportProfileManageModel().getReportProfileTestDataFilterList().get(i)
							.setHeader3_CellPosition(eachRpPrintPosition.getCellPosition());
				}
				/*
				 * else if(eachRpPrintPosition.getKeyParam().equals(ConstantReportV2.
				 * CELL_START_POSITION_HEADER_RESULT_DATA_KEY)){
				 * getReportProfileManageModel().getReportProfileTestDataFilterList().get(i).
				 * setResultValueCellPosition(eachRpPrintPosition.getCellPosition());
				 * }
				 */

				else if (eachRpPrintPosition.getKeyParam()
						.equals(ConstantReportV2.RESULT_SOURCE_TYPE_RESULT_STATUS_KEY)) {
					getReportProfileManageModel().getReportProfileTestDataFilterList().get(i)
							.setResultStatusCellPosition(eachRpPrintPosition.getCellPosition());
				} else if (eachRpPrintPosition.getKeyParam().equals(ConstantReportV2.POPULATE_LOCAL_LOWER_LIMIT_KEY)) {
					getReportProfileManageModel().getReportProfileTestDataFilterList().get(i)
							.setResultLowerLimitCellPosition(eachRpPrintPosition.getCellPosition());
				} else if (eachRpPrintPosition.getKeyParam().equals(ConstantReportV2.POPULATE_LOCAL_UPPER_LIMIT_KEY)) {
					getReportProfileManageModel().getReportProfileTestDataFilterList().get(i)
							.setResultUpperLimitCellPosition(eachRpPrintPosition.getCellPosition());
				} else if (eachRpPrintPosition.getKeyParam().equals(ConstantReportV2.POPULATE_MASTER_LOWER_LIMIT_KEY)) {
					getReportProfileManageModel().getReportProfileTestDataFilterList().get(i)
							.setResultLowerLimitCellPosition(eachRpPrintPosition.getCellPosition());
				} else if (eachRpPrintPosition.getKeyParam().equals(ConstantReportV2.POPULATE_MASTER_UPPER_LIMIT_KEY)) {
					getReportProfileManageModel().getReportProfileTestDataFilterList().get(i)
							.setResultUpperLimitCellPosition(eachRpPrintPosition.getCellPosition());
				} else if (eachRpPrintPosition.getKeyParam().startsWith(REPLICATE_RESULT_KEY_PREFIX)) {
					getReportProfileManageModel().getReportProfileTestDataFilterList().get(i)
							.getReplicateResultCellPositionList().add(eachRpPrintPosition.getCellPosition());
				}

				/*
				 * else if (eachRpPrintPosition.getKeyParam().startsWith(ConstantReportV2.
				 * CELL_HEADER_POSITION_HEADER_RESULT_RSM_INITIAL)){
				 * getReportProfileManageModel().getReportProfileTestDataFilterList().get(i).
				 * setResultValueCellPosition(eachRpPrintPosition.getCellPosition());
				 * }else if (eachRpPrintPosition.getKeyParam().startsWith(ConstantReportV2.
				 * CELL_HEADER_POSITION_HEADER_RESULT_RSM_FINAL)){
				 * getReportProfileManageModel().getReportProfileTestDataFilterList().get(i).
				 * setResultValueCellPosition(eachRpPrintPosition.getCellPosition());
				 * }
				 */

				else if (eachRpPrintPosition.getKeyParam()
						.startsWith(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_RSM_INITIAL)) {
					getReportProfileManageModel().getReportProfileTestDataFilterList().get(i)
							.setResultValueCellPosition(eachRpPrintPosition.getCellPosition());
				} else if (eachRpPrintPosition.getKeyParam()
						.startsWith(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_RSM_FINAL)) {
					getReportProfileManageModel().getReportProfileTestDataFilterList().get(i)
							.setResultValueCellPosition(eachRpPrintPosition.getCellPosition());
				} else if (eachRpPrintPosition.getKeyParam()
						.equals(ConstantReportV2.POPULATE_HEADER_KEY_TEST_PERIOD_IN_MINUTES)) {
					getReportProfileManageModel().getReportProfileTestDataFilterList().get(i)
							.setHeaderTestPeriodInMinutesCellPosition(eachRpPrintPosition.getCellPosition());
				} else if (eachRpPrintPosition.getKeyParam()
						.equals(ConstantReportV2.POPULATE_HEADER_KEY_WARMUP_PERIOD_IN_MINUTES)) {
					getReportProfileManageModel().getReportProfileTestDataFilterList().get(i)
							.setHeaderWarmUpPeriodInMinutesCellPosition(eachRpPrintPosition.getCellPosition());
				} else if (eachRpPrintPosition.getKeyParam()
						.equals(ConstantReportV2.POPULATE_HEADER_KEY_TARGET_VOLTAGE)) {
					getReportProfileManageModel().getReportProfileTestDataFilterList().get(i)
							.setHeaderActualVoltageCellPosition(eachRpPrintPosition.getCellPosition());
				} else if (eachRpPrintPosition.getKeyParam()
						.equals(ConstantReportV2.POPULATE_HEADER_KEY_TARGET_CURRENT)) {
					getReportProfileManageModel().getReportProfileTestDataFilterList().get(i)
							.setHeaderActualCurrentCellPosition(eachRpPrintPosition.getCellPosition());
				} else if (eachRpPrintPosition.getKeyParam().equals(ConstantReportV2.POPULATE_HEADER_KEY_TARGET_PF)) {
					getReportProfileManageModel().getReportProfileTestDataFilterList().get(i)
							.setHeaderActualPfCellPosition(eachRpPrintPosition.getCellPosition());
				} else if (eachRpPrintPosition.getKeyParam().equals(ConstantReportV2.POPULATE_HEADER_KEY_TARGET_FREQ)) {
					getReportProfileManageModel().getReportProfileTestDataFilterList().get(i)
							.setHeaderActualFreqCellPosition(eachRpPrintPosition.getCellPosition());
				} else if (eachRpPrintPosition.getKeyParam()
						.equals(ConstantReportV2.POPULATE_HEADER_KEY_TARGET_ENERGY)) {
					getReportProfileManageModel().getReportProfileTestDataFilterList().get(i)
							.setHeaderActualEnergyCellPosition(eachRpPrintPosition.getCellPosition());
				} else {
					getReportProfileManageModel().getReportProfileTestDataFilterList().get(i)
							.setResultValueCellPosition(eachRpPrintPosition.getCellPosition());
				}

				// getReportProfileManageModel().getReportProfileTestDataFilterList().get(i).setResultDataTypeCellPosition(resultDataTypeCellPosition);
			}

			List<OperationProcess> rpOperationProcessList = getReportProfileManageModel()
					.getReportProfileTestDataFilterList().get(i).getOperationProcessDataList();
			ArrayList<String> inputProcessDataList = new ArrayList<String>();
			rpOperationProcessList.stream()
					.filter(e -> e.isAddedToInputProcess() == true)
					.forEach(e -> inputProcessDataList.add(e.getOperationProcessKey()));
			getReportProfileManageModel().getReportProfileTestDataFilterList().get(i)
					.setOperationProcessInputKeyList(inputProcessDataList);

		}

		/*
		 * ArrayList<String> cellStartPositionHeader = new
		 * ArrayList<String>(Arrays.asList("Result Value","Result Status"));
		 * ArrayList<String> cellStartPositionCell = new
		 * ArrayList<String>(Arrays.asList("A1","B22"));
		 */

		ref_tvTestFilterDataList.getItems().addAll(getReportProfileManageModel().getReportProfileTestDataFilterList());

	}

	private ReportProfileMeterMetaDataModel convertDatabaseModelToMeterProfileDataDisplayModel() {

		ApplicationLauncher.logger.debug("convertDatabaseModelToMeterProfileDataDisplayModel : Entry");
		ReportProfileMeterMetaDataModel meterMetaDataDisplay = new ReportProfileMeterMetaDataModel();// new
																										// ref_tvMeterMetaDataList.getItems();
		List<ReportProfileMeterMetaDataFilter> meterMetaDatabaseList = getReportProfileManageModel()
				.getDutMetaDataList();// new ArrayList<ReportProfileMeterMetaDataFilter>();
		// ReportMeterMetaDataTypeSubModel meterMetaDataDisplayModel = null;//new
		// ReportMeterMetaDataTypeSubModel();
		// String baseTemplateMetaDataPopulateType =
		// ref_cmbBxMeterMetaDataPopulateType.getSelectionModel().getSelectedItem().toString();
		// String baseTemplateName =
		// ref_cmbBxBaseTemplate.getSelectionModel().getSelectedItem().toString();
		// String reportGroupId = "";
		// String reportGroupName =
		// ref_cmbBxReportProfileGroup.getSelectionModel().getSelectedItem().toString();
		// String reportProfileId = "";
		// String reportProfileName =
		// ref_cmbBxReportProfile.getSelectionModel().getSelectedItem().toString();
		// String filterName = ref_txtMeterMetaDataPageName.getText();//"";

		// boolean appendDutSerialAndRackPosition =
		// ref_chkBxDiscardRackPositionInDutSerialNumber.isSelected();//true;
		// boolean filterActive =
		// ref_chkBxMeterProfileMetaDataPageActive.isSelected();//true;

		String sNo = "";
		String dataTypeKey = "";
		boolean populateData = false;
		boolean populateDataForEachDut = false;
		boolean populateOnlyHeader = false;
		boolean discardRackPositionInDutSerialNumber = false;
		boolean filterActive = false;
		String dataCellPosition = "";
		String pageNumber = "";
		String pageName = "";
		String populateDataSelection = ConstantReportV2.NONE_DISPLAYED;
		clearMetaDataPageNameList();
		// ReportProfileMeterMetaDataModel reportProfileMeterMetaData = new
		// ReportProfileMeterMetaDataModel();
		ArrayList<ReportMeterMetaDataTypeSubModel> reportProfileMeterMetaDataSubModelList = new ArrayList<ReportMeterMetaDataTypeSubModel>();
		for (ReportProfileMeterMetaDataFilter eachMeterMetaDataBase : meterMetaDatabaseList) {// .stream().forEach(e ->
																								// {
			// meterMetaDataDisplayModel = new ReportProfileMeterMetaDataFilter();

			meterMetaDataDisplay.setAppendMeterSerialNoAndRackPosition(false);// eachMeterMetaDataBase.isAppendDutSerialAndRackPosition());
			meterMetaDataDisplay
					.setMeterMetaDataPopulateType(eachMeterMetaDataBase.getBaseTemplateMetaDataPopulateType());
			meterMetaDataDisplay.setPageName(eachMeterMetaDataBase.getFilterName());
			pageNumber = String.valueOf(eachMeterMetaDataBase.getPageNumber());
			meterMetaDataDisplay.setPageNumber(pageNumber);
			dataTypeKey = eachMeterMetaDataBase.getMeterDataType();
			// populateData = eachMeterMetaDataBase.isPopulateOnlyOnHeader();
			populateDataForEachDut = eachMeterMetaDataBase.isPopulateForEachDut();
			populateOnlyHeader = eachMeterMetaDataBase.isPopulateOnlyOnHeader();
			sNo = eachMeterMetaDataBase.getTableSerialNo();
			pageName = eachMeterMetaDataBase.getFilterName();
			addMetaDataPageNameList(pageName);
			if ((populateDataForEachDut == true) || (populateOnlyHeader == true)) {
				populateData = true;
			} else {
				populateData = false;
			}
			dataCellPosition = eachMeterMetaDataBase.getCellPosition();
			discardRackPositionInDutSerialNumber = eachMeterMetaDataBase.isDiscardRackPositionInDutSerialNumber();
			filterActive = eachMeterMetaDataBase.isFilterActive();
			ReportMeterMetaDataTypeSubModel reportProfileMeterMetaDataSubModel = new ReportMeterMetaDataTypeSubModel(
					sNo,
					dataTypeKey, populateData, populateDataForEachDut, populateOnlyHeader,
					dataCellPosition, pageNumber, pageName);// ,discardRackPositionInDutSerialNumber);
			reportProfileMeterMetaDataSubModel
					.setDiscardRackPositionInDutSerialNumber(discardRackPositionInDutSerialNumber);
			reportProfileMeterMetaDataSubModel.setFilterActive(filterActive);
			if (populateOnlyHeader) {
				populateDataSelection = ConstantReportV2.POPULATE_DATA_TYPE_ONLY_HEADERS;
				reportProfileMeterMetaDataSubModel.setPopulateDataSelection(populateDataSelection);
			}
			if (populateDataForEachDut) {
				populateDataSelection = ConstantReportV2.POPULATE_DATA_TYPE_ALL_DUT;
				reportProfileMeterMetaDataSubModel.setPopulateDataSelection(populateDataSelection);
			}
			reportProfileMeterMetaDataSubModelList.add(reportProfileMeterMetaDataSubModel);
			// reportProfileMeterMetaData.setReportProfileMeterMetaData();
			// ReportMeterMetaDataTypeSubModel reportProfileMeterMetaData1 = new
			// ReportMeterMetaDataTypeSubModel(pageNumber, pageNumber, populateOnlyHeader,
			// populateOnlyHeader, populateOnlyHeader, pageNumber, pageNumber);
			// reportProfileMeterMetaData.setReportBaseTemplate(baseTemplateName);//eachMeterMetaDataBase.getBaseTemplateName());
			// reportProfileMeterMetaData.setReportProfileMeterMetaData(reportProfileMeterMetaData);
			// reportProfileMeterMetaData.setReportProfileMeterMetaData(eachMeterMetaDataBase);
			/*
			 * meterMetaDataDatabase.setPageNumber(Integer.parseInt(eachMeterMetaData.
			 * getPageNumber()));
			 * meterMetaDataDatabase.setAppendDutSerialAndRackPosition(
			 * appendDutSerialAndRackPosition);//eachMeterMetaData.
			 * appendDutSerialAndRackPosition);
			 * meterMetaDataDatabase.setBaseTemplateMetaDataPopulateType(
			 * baseTemplateMetaDataPopulateType);
			 * meterMetaDataDatabase.setBaseTemplateName(baseTemplateName);
			 * meterMetaDataDatabase.setFilterActive(filterActive);
			 * meterMetaDataDatabase.setFilterName(filterName);
			 * meterMetaDataDatabase.setReportGroupId(reportGroupId);
			 * meterMetaDataDatabase.setReportGroupName(reportGroupName);
			 * meterMetaDataDatabase.setReportProfileId(reportProfileId);
			 * meterMetaDataDatabase.setReportProfileName(reportProfileName);
			 * meterMetaDataDatabase.setTableSerialNo(eachMeterMetaData.getSerialNo());
			 * meterMetaDataDatabase.setMeterDataType(eachMeterMetaData.getDataTypeKey());
			 * meterMetaDataDatabase.setPopulateOnlyOnHeader(eachMeterMetaData.
			 * getPopulateOnlyHeader());
			 * meterMetaDataDatabase.setPopulateForEachDut(eachMeterMetaData.
			 * getPopulateDataForEachDut());
			 * meterMetaDataDatabase.setCellPosition(eachMeterMetaData.getDataCellPosition()
			 * );
			 * 
			 * 
			 * 
			 * ApplicationLauncher.logger.
			 * debug("convertDatabaseModelToMeterProfileDataDisplayModel : getDataTypeKey(): "
			 * + eachMeterMetaData.getDataTypeKey());
			 */
			// meterMetaDataDisplayList.add(reportProfileMeterMetaData);
		}

		meterMetaDataDisplay.setReportProfileMeterMetaData(reportProfileMeterMetaDataSubModelList);

		return meterMetaDataDisplay;
	}

	private void publishDataOnMeterProfileMetaDataList() {

		ApplicationLauncher.logger.debug("publishDataOnMeterProfileMetaDataList: Entry");
		ReportProfileMeterMetaDataModel meterMetaDataDisplayModel = convertDatabaseModelToMeterProfileDataDisplayModel();
		refreshGuiMeterProfileDataList(meterMetaDataDisplayModel);
	}

	public void refreshGuiMeterProfileDataList(ReportProfileMeterMetaDataModel reportProfileMeterMetaData) {
		ApplicationLauncher.logger.debug("refreshGuiMeterProfileDataList: Entry");
		boolean status = true;// validateMeterMetaDataCellDataEntry();
		String pageNumber = "";
		String pageName = "";
		if (status) {
			// ApplicationLauncher.logger.debug("btnMeterMetaDataSaveOnClick: test1");
			// ReportProfileMeterMetaDataModel reportProfileMeterMetaData = new
			// ReportProfileMeterMetaDataModel();
			// pageNumber = ref_txtMeterMetaDataPageNumber.getText();
			// pageName = ref_txtMeterMetaDataPageName.getText();
			// reportProfileMeterMetaData.setPageNumber(pageNumber);
			// reportProfileMeterMetaData.setPageName(pageName);
			// reportProfileMeterMetaData.setMeterMetaDataPopulateType(ref_cmbBxMeterMetaDataPopulateType.getSelectionModel().getSelectedItem().toString());
			// reportProfileMeterMetaData.setReportBaseTemplate(ref_cmbBxBaseTemplate.getSelectionModel().getSelectedItem().toString());
			// reportProfileMeterMetaData.setAppendMeterSerialNoAndRackPosition(ref_chkBxDiscardRackPositionInDutSerialNumber.isSelected());
			try {
				ref_cmbBxMetaDataPageName.getItems().clear();
				ref_cmbBxMetaDataPageName.getItems().addAll(getMetaDataPageNameList());
				ref_cmbBxMetaDataPageName.getSelectionModel().selectLast();
				String selectedMetaDataPageName = ref_cmbBxMetaDataPageName.getSelectionModel().getSelectedItem()
						.toString();
				ref_tvMeterMetaDataList.getItems().clear();
				// displayMeterMetaDataList();

				ArrayList<ReportMeterMetaDataTypeSubModel> filteredReportProfileMetaDataList = (ArrayList<ReportMeterMetaDataTypeSubModel>) reportProfileMeterMetaData
						.getReportProfileMeterMetaData()
						.stream()
						.filter(e -> e.getPageName().equals(selectedMetaDataPageName))
						.sorted(Comparator.comparingInt(ReportMeterMetaDataTypeSubModel::getPageNumberInt))
						// .map(e -> e.setDiscardRackPositionInDutSerialNumber(true))
						.collect(Collectors.toList());

				// filteredReportProfileMetaDataList.stream().forEach( e -> {

				// e.setDiscardRackPositionInDutSerialNumber(reportProfileMeterMetaData.getAppendMeterSerialNoAndRackPosition());

				// });

				ref_tvMeterMetaDataList.getItems().setAll(filteredReportProfileMetaDataList);
				// reportProfileMeterMetaData.setReportProfileMeterMetaData(reportProfileMeterMetaData.getReportProfileMeterMetaData());

				reOrderSerialNumber(ref_tvMeterMetaDataList);
				ref_tvMeterMetaDataList.refresh();

			} catch (Exception e) {
				ApplicationLauncher.logger.error("refreshGuiMeterProfileDataList: Exception : " + e.getMessage());
			}
			/*
			 * addReportProfileMeterMetaDataHashMap(pageNumber,reportProfileMeterMetaData );
			 * if(ref_chkBxDiscardRackPositionInDutSerialNumber.isSelected()){
			 * ref_chkBxAppendMeterSerialNoAndRackPositionListDisplay.setSelected(true);
			 * }else{
			 * ref_chkBxAppendMeterSerialNoAndRackPositionListDisplay.setSelected(false);
			 * }
			 * 
			 * ObservableList existingPageNameList = ref_cmbBxMetaDataPageName.getItems();
			 * 
			 * if(!existingPageNameList.contains(pageName)){
			 * ref_cmbBxMetaDataPageName.getItems().add(pageName);
			 * ref_cmbBxMetaDataPageName.getSelectionModel().select(pageName);
			 * }
			 * 
			 * getLastSavedMeterMetaDataCellPosition().clear();
			 * getMeterMetaDataList().forEach(k-> {
			 * updateLastSavedMeterMetaDataCellPosition(k.getDataTypeKey(),k.
			 * getDataCellPosition());
			 * //ApplicationLauncher.logger.debug("displayMeterMetaDataList: " +
			 * k.getDataTypeKey());
			 * });
			 */

		}
	}

	@FXML
	public void btnAddGroupProfileOnClick() {
		ApplicationLauncher.logger.debug("btnAddGroupProfileOnClick: Entry");
		// hgjgf
		ref_cmbBxReportProfileGroup.setEditable(true);
		ref_cmbBxReportProfile.setEditable(true);
		ref_btnAddGroupProfile.setDisable(true);
		ref_btnSaveGroupProfile.setDisable(false);

		ref_btnReportProfileLoad.setDisable(true);
		ref_gridPaneControl.setDisable(true);
		ref_cmbBxParameterProfileName.setDisable(false);
	}

	@FXML
	public void btnSaveGroupProfileOnClick() {
		ApplicationLauncher.logger.debug("btnSaveGroupProfileOnClick: Entry");

		setupDataForReportProfileManage();
		saveMeterProfileDataListToDataBase();
		ref_cmbBxReportProfileGroup.setEditable(false);
		ref_cmbBxReportProfile.setEditable(false);
		ref_btnAddGroupProfile.setDisable(false);
		ref_btnSaveGroupProfile.setDisable(true);

		ref_btnReportProfileLoad.setDisable(false);
		ref_gridPaneControl.setDisable(false);
		ref_cmbBxParameterProfileName.setDisable(true);
	}

	public Set<String> getMetaDataPageNameList() {
		return metaDataPageNameList;
	}

	public void clearMetaDataPageNameList() {
		metaDataPageNameList.clear();
	}

	public void addMetaDataPageNameList(String inputData) {
		metaDataPageNameList.add(inputData);
	}

	public void setMetaDataPageNameList(Set<String> metaDataPageNameList) {
		this.metaDataPageNameList = metaDataPageNameList;
	}

	public boolean isInsertDataMode() {
		return insertDataMode;
	}

	public void setInsertDataMode(boolean insertDataMode) {
		this.insertDataMode = insertDataMode;
	}

	@FXML
	public void btnReportProfileDeleteOnClick() {
		ApplicationLauncher.logger.debug("btnReportProfileDeleteOnClick : Entry");
		if (ref_tabMeterProfileDataList.isSelected()) {
			int selectedIndex = ref_tvMeterMetaDataList.getSelectionModel().getSelectedIndex();
			if (selectedIndex != -1) {
				Platform.runLater(() -> {
					ReportMeterMetaDataTypeSubModel reportMeterMetaDataTypeSubModelData = ref_tvMeterMetaDataList
							.getSelectionModel().getSelectedItem();
					String message = "Are you sure to delete the filter?";
					String title = "Delete Filter";
					if (GuiUtils.isUserConfirmedToDelete(title, message)) {
						ref_tvMeterMetaDataList.getItems().remove(reportMeterMetaDataTypeSubModelData);
						reOrderSerialNumber(ref_tvMeterMetaDataList);
					}
				});
			} else {
				ApplicationLauncher.logger.info("btnReportProfileDeleteOnClick : ERROR_CODE_6029: "
						+ ErrorCodeMapping.ERROR_CODE_6029_MSG + " - prompted!");
				WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6029, ErrorCodeMapping.ERROR_CODE_6029_MSG,
						AlertType.WARNING);

			}
		} else if (ref_tabTestFilterDataList.isSelected()) {
			int selectedIndex = ref_tvTestFilterDataList.getSelectionModel().getSelectedIndex();
			if (selectedIndex != -1) {
				Platform.runLater(() -> {
					ReportProfileTestDataFilter reportProfileTestDataFilterData = ref_tvTestFilterDataList
							.getSelectionModel().getSelectedItem();
					String message = "Are you sure to delete the filter?";
					String title = "Delete Filter";
					if (GuiUtils.isUserConfirmedToDelete(title, message)) {
						ref_tvTestFilterDataList.getItems().remove(reportProfileTestDataFilterData);
						reOrderSerialNumberTestFilter(ref_tvTestFilterDataList);
					}
				});
			} else {
				ApplicationLauncher.logger.info("btnReportProfileDeleteOnClick : ERROR_CODE_6030: "
						+ ErrorCodeMapping.ERROR_CODE_6030_MSG + " - prompted!");
				WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6030, ErrorCodeMapping.ERROR_CODE_6030_MSG,
						AlertType.WARNING);

			}
		}
		ref_btnReportProfileSave.setDisable(false);
	}

	@FXML
	public void btnReportProfileEditOnClick() {
		ApplicationLauncher.logger.debug("btnReportProfileEditOnClick : Entry");

		if (ref_tabMeterProfileDataList.isSelected()) {

			// List<ReportMeterMetaDataTypeSubModel> reportMeterMetaDataTypeSubModel =
			// ref_tvMeterMetaDataList.getItems();
			// String selectedPageNumber = "1";

			int selectedIndex = ref_tvMeterMetaDataList.getSelectionModel().getSelectedIndex();
			if (selectedIndex != -1) {
				guiMeterMetaDataDefaultSettings();
				// ArrayList<ReportMeterMetaDataTypeSubModel> selectedMestaDataList =

				guiRefreshSelectedMetaDataList();
				ref_titledPaneMeterProfileMetaData.setExpanded(true);
				ref_titledPaneMeterProfileMetaData.setDisable(false);
				ref_titledPaneTestFilterData.setDisable(true);
				// ref_txtFilterName.setDisable(true);
				setInsertDataMode(false);

				ref_gridPaneControl.setDisable(true);

				ref_btnReportProfileSave.setDisable(false);
				ref_tabTestFilterDataList.setDisable(true);
				ref_tabMeterProfileDataList.setDisable(true);
			} else {
				// ApplicationLauncher.logger.debug("btnReportProfileEditOnClick : TestFilter:
				// Cell Start invalid data: t.getNewValue() :" + t.getNewValue());
				ApplicationLauncher.logger.info("btnReportProfileEditOnClick : ERROR_CODE_6029: "
						+ ErrorCodeMapping.ERROR_CODE_6029_MSG + " - prompted!");
				WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6029, ErrorCodeMapping.ERROR_CODE_6029_MSG,
						AlertType.WARNING);

			}

		} else if (ref_tabTestFilterDataList.isSelected()) {
			// guiTestFilterDefaultSettings();

			// List<ReportMeterMetaDataTypeSubModel> reportMeterMetaDataTypeSubModel =
			// ref_tvMeterMetaDataList.getItems();
			// String selectedPageNumber = "1";

			int selectedIndex = ref_tvTestFilterDataList.getSelectionModel().getSelectedIndex();
			if (selectedIndex != -1) {
				setTestFilterEditMode(true);
				guiTestFilterDefaultSettings();
				guiRefreshSelectedTestFilterList();

				ref_titledPaneTestFilterData.setExpanded(true);
				ref_titledPaneTestFilterData.setDisable(false);
				ref_titledPaneMeterProfileMetaData.setDisable(true);
				ref_titledPaneCellPosition.setExpanded(true);
				ref_txtFilterName.setDisable(true);

				setInsertDataMode(false);
				ref_gridPaneControl.setDisable(true);
				setTestFilterEditMode(false);
				guiObjectEnableDisable();
				ref_btnReportProfileSave.setDisable(false);
				ref_tabTestFilterDataList.setDisable(true);
				ref_tabMeterProfileDataList.setDisable(true);
			} else {
				// ApplicationLauncher.logger.debug("btnReportProfileEditOnClick : TestFilter:
				// Cell Start invalid data: t.getNewValue() :" + t.getNewValue());
				ApplicationLauncher.logger.info("btnReportProfileEditOnClick : ERROR_CODE_6030: "
						+ ErrorCodeMapping.ERROR_CODE_6030_MSG + " - prompted!");
				WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_6030, ErrorCodeMapping.ERROR_CODE_6030_MSG,
						AlertType.WARNING);

			}

		}

		/*
		 * setInsertDataMode(false);
		 * ref_gridPaneControl.setDisable(true);
		 */
		// ref_btnReportProfileSave.setDisable(false);
		// ref_tabMeterProfileDataList.setDisable(true);
		// ref_tabTestFilterDataList.setDisable(true);

	}

	private void guiObjectEnableDisable() {

		ApplicationLauncher.logger.debug("guiObjectEnableDisable : Entry");

		Sleep(200);

		chkBxPopulateHeader1OnChange();
		chkBxPopulateHeader2OnChange();
		chkBxPopulateHeader3OnChange();
		chkBxPopulateHeader4OnChange();
		chkBxPopulateHeader5OnChange();
		chkBxReplicateDataOnChange();
		if (ref_rdBtnOperationNone.isSelected()) {
			// rdBtnOperationNoneOnChange();
			rdBtnOperationNoneEditMode();
		} else if (ref_rdBtnOperationInput.isSelected()) {
			// rdBtnOperationInputOnChange();
			rdBtnOperationInputEditMode();
		} else if (ref_rdBtnOperationOutput.isSelected()) {
			// rdBtnOperationOutputOnChange();
			rdBtnOperationOutputEditMode();
		}
		// cmbBxTestTypeOnChange();
		String selectedTestType = ref_cmbBxTestType.getSelectionModel().getSelectedItem().toString();
		if (selectedTestType.equals(ConstantReportV2.NONE_DISPLAYED)) {
			disableVoltageFilterGuiObjects();
			disableCurrentFilterGuiObjects();
			disablePfFilterGuiObjects();
			disableHeader3WhenTestTypeNoneSelectedGuiObjects();
		}
		populateGuiEnableDisableLogic();
	}

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep :InterruptedException:" + e.getMessage());
		}

	}

	private void guiRefreshSelectedMetaDataList() {

		ApplicationLauncher.logger.debug("guiRefreshSelectedMetaDataList : Entry");

		List<ReportMeterMetaDataTypeSubModel> reportMeterMetaDataTypeSubModel = new ArrayList<ReportMeterMetaDataTypeSubModel>(
				ref_tvMeterMetaDataList.getItems());
		// String selectedPageNumber = "1";

		int selectedIndex = ref_tvMeterMetaDataList.getSelectionModel().getSelectedIndex();
		if (selectedIndex != -1) {
			ApplicationLauncher.logger.debug("guiRefreshSelectedMetaDataList : selectedIndex: " + selectedIndex);
			// String selectedPageNumber =
			// ref_tvMeterMetaDataList.getSelectionModel().getSelectedItem().getPageNumber();
			ReportMeterMetaDataTypeSubModel reportMeterMetaDataTypeSubModelData = ref_tvMeterMetaDataList
					.getSelectionModel().getSelectedItem();
			String selectedPageNumber = reportMeterMetaDataTypeSubModelData.getPageNumber();
			ref_txtMeterMetaDataPageNumber.setText(selectedPageNumber);
			ref_chkBxDiscardRackPositionInDutSerialNumber
					.setSelected(reportMeterMetaDataTypeSubModelData.isDiscardRackPositionInDutSerialNumber());
			ref_chkBxMeterProfileMetaDataPageActive.setSelected(reportMeterMetaDataTypeSubModelData.isFilterActive());
			List<ReportMeterMetaDataTypeSubModel> filteredReportMeterMetaDataTypeSubModel = reportMeterMetaDataTypeSubModel
					.stream()
					.filter(e -> e.getPageNumber().equals(selectedPageNumber))
					.collect(Collectors.toList());

			// filteredReportMeterMetaDataTypeSubModel =
			// (List<ReportMeterMetaDataTypeSubModel>) new
			// ArrayList<ReportMeterMetaDataTypeSubModel>(filteredReportMeterMetaDataTypeSubModel).clone();
			Set<String> alreadyAddedList = new HashSet<String>();

			// updatedReportMeterMetaDataTypeSubModel = reportMeterMetaDataTypeSubModel;

			List<ReportMeterMetaDataTypeSubModel> updatedReportMeterMetaDataTypeSubModel = new ArrayList<ReportMeterMetaDataTypeSubModel>(
					ref_tvSelectedMeterDataType.getItems());

			// updatedReportMeterMetaDataTypeSubModel =
			// (List<ReportMeterMetaDataTypeSubModel>) new
			// ArrayList<ReportMeterMetaDataTypeSubModel>(updatedReportMeterMetaDataTypeSubModel).clone();
			filteredReportMeterMetaDataTypeSubModel.stream()
					.forEach(element -> {

						GuiUtils.ArrayListReplaceIf(updatedReportMeterMetaDataTypeSubModel,
								e -> element.getDataTypeKey().equals(e.getDataTypeKey()), e -> element);
					});

			ref_tvSelectedMeterDataType.getItems().clear();
			ref_tvSelectedMeterDataType.getItems().addAll(updatedReportMeterMetaDataTypeSubModel);
			reOrderPsuedoSerialNumber(ref_tvSelectedMeterDataType);

			clearLastSavedMeterMetaDataCellPosition();

			filteredReportMeterMetaDataTypeSubModel.stream()
					.forEach(e -> {

						updateLastSavedMeterMetaDataCellPosition(e.getDataTypeKey(), e.getDataCellPosition());
					});

			// ApplicationLauncher.logger.debug("guiRefreshSelectedMetaDataList :
			// filteredReportMeterMetaDataTypeSubModel: " +
			// filteredReportMeterMetaDataTypeSubModel);
		}

		btnMeterDataDisplayNextOnClick();
		// return null;
	}

	private void guiRefreshSelectedTestFilterList() {

		ApplicationLauncher.logger.debug("guiRefreshSelectedTestFilterList : Entry");

		ReportProfileTestDataFilter testFilterSelectedData = ref_tvTestFilterDataList.getSelectionModel()
				.getSelectedItem();

		// ReportProfileTestDataFilter testFilterData = new
		// ReportProfileTestDataFilter();
		// testFilterData.setSerialNo("1");
		// testFilterSelectedData.setTableSerialNo("1");
		ref_cmbBxTestType.getSelectionModel().select(testFilterSelectedData.getTestTypeSelected());
		ref_cmbBxTestTypeSub.getSelectionModel().select(testFilterSelectedData.getSubTestTypeSelected());
		ref_rdBtnOperationNone.setSelected(testFilterSelectedData.isOperationNoneSelected());
		ref_rdBtnOperationInput.setSelected(testFilterSelectedData.isOperationInputSelected());
		ref_rdBtnOperationOutput.setSelected(testFilterSelectedData.isOperationOutputSelected());

		// selectedTestTypeKey);

		ref_chkBxEnableFilter.setSelected(testFilterSelectedData.isFilterActive());
		ref_chkBxPopulateDutErrorResultStatus.setSelected(testFilterSelectedData.isPopulateResultStatus());
		ref_chkBxReplicateData.setSelected(testFilterSelectedData.isReplicateData());
		/*
		 * ref_chkBxPopulateHeader1.setSelected(testFilterSelectedData.isPopulateHeader1
		 * ());
		 * ref_chkBxPopulateHeader2.setSelected(testFilterSelectedData.isPopulateHeader2
		 * ());
		 * ref_chkBxPopulateHeader3.setSelected(testFilterSelectedData.isPopulateHeader3
		 * ());
		 */
		// ref_chkBxPopulateHeader4.setSelected(testFilterSelectedData.isPopulateHeader4());
		// ref_chkBxPopulateHeader5.setSelected(testFilterSelectedData.isPopulateHeader5());
		/*
		 * ref_chkBxPopulateHeaderTestPeriod.setSelected(testFilterSelectedData.
		 * isPopulateHeaderTestPeriodInMinutes());
		 * ref_chkBxPopulateHeaderWarmupPeriod.setSelected(testFilterSelectedData.
		 * isPopulateHeaderWarmupPeriodInMinutes());
		 * ref_chkBxPopulateHeaderActualVoltage.setSelected(testFilterSelectedData.
		 * isPopulateHeaderActualVoltage());
		 * ref_chkBxPopulateHeaderActualCurrent.setSelected(testFilterSelectedData.
		 * isPopulateHeaderActualCurrent());
		 * ref_chkBxPopulateHeaderActualPf.setSelected(testFilterSelectedData.
		 * isPopulateHeaderActualPf());
		 * ref_chkBxPopulateHeaderActualFreq.setSelected(testFilterSelectedData.
		 * isPopulateHeaderActualFreq());
		 * ref_chkBxPopulateHeaderActualEnergy.setSelected(testFilterSelectedData.
		 * isPopulateHeaderActualEnergy());
		 */
		// testFilterSelectedData.processOperationMode();
		ref_txtTestFilterPageNumber.setText(testFilterSelectedData.getPageNumber());
		ref_cmbBxTestFilterDataPopulateType.getSelectionModel()
				.select(testFilterSelectedData.getTestFilterDataPopulateType());// .getSelectedItem().toString());

		ref_cmbBxExecutionResultType.getSelectionModel()
				.select(testFilterSelectedData.getTestExecutionResultTypeSelected());// .getSelectedItem().toString());
		ref_cmbBxOperationSourceParamType.getSelectionModel()
				.select(testFilterSelectedData.getOperationSourceResultTypeSelected());
		ref_cmbBxBaseTemplate.getSelectionModel().select(testFilterSelectedData.getReportBaseTemplate());// .getSelectedItem().toString());

		ref_txtFilterName.setText(testFilterSelectedData.getTestFilterName());
		ref_txtHeader1Value.setText(testFilterSelectedData.getHeader1_Value());
		ref_txtHeader2Value.setText(testFilterSelectedData.getHeader2_Value());
		ref_txtHeader3Value.setText(testFilterSelectedData.getHeader3_Value());
		ref_txtHeader4Value.setText(testFilterSelectedData.getHeader4_Value());
		ref_txtHeader5Value.setText(testFilterSelectedData.getHeader5_Value());

		ApplicationLauncher.logger.debug(
				"guiRefreshSelectedTestFilterList : getHeader3_Value: " + testFilterSelectedData.getHeader3_Value());
		ref_txtVoltPercentFilterUserEntry.setText(testFilterSelectedData.getVoltPercentFilterUserEntry());
		ref_txtCurrentPercentFilterUserEntry.setText(testFilterSelectedData.getCurrentPercentFilterUserEntry());
		ref_txtPfFilterUserEntry.setText(testFilterSelectedData.getPfFilterUserEntry());
		ref_txtFreqFilterUserEntry.setText(testFilterSelectedData.getFreqFilterUserEntry());
		// ApplicationLauncher.logger.debug("guiRefreshSelectedTestFilterList :
		// getEnergyFilterUserEntry: " +
		// testFilterSelectedData.getEnergyFilterUserEntry());
		ref_txtEnergyFilterUserEntry.setText(testFilterSelectedData.getEnergyFilterUserEntry());
		ref_txtIterationReadingStartingId.setText(testFilterSelectedData.getIterationReadingStartIdUserEntry());
		ref_txtIterationReadingEndingId.setText(testFilterSelectedData.getIterationReadingEndIdUserEntry());
		ref_cmbBxVoltPercentFilterUserEntry.getSelectionModel().select(testFilterSelectedData.getVoltFilterUnitValue());// .getSelectedItem().toString());
		ref_cmbBxCurrentPercentFilterUserEntry.getSelectionModel()
				.select(testFilterSelectedData.getCurrentFilterUnitValue());// .getSelectedItem().toString());
		ref_cmbBxPfFilterUserEntry.getSelectionModel().select(testFilterSelectedData.getPfFilterUnitValue());// .getSelectedItem().toString());
		ref_cmbBxFreqFilterUserEntry.getSelectionModel().select(testFilterSelectedData.getFreqFilterUnitValue());// .getSelectedItem().toString());
		ref_cmbBxEnergyFilterUserEntry.getSelectionModel().select(testFilterSelectedData.getEnergyFilterUnitValue());// .getSelectedItem().toString());

		ref_txtVoltPercentFilterData.setText(testFilterSelectedData.getVoltPercentFilterData());
		ref_txtCurrentPercentFilterData.setText(testFilterSelectedData.getCurrentPercentFilterData());
		ref_txtPfFilterData.setText(testFilterSelectedData.getPfFilterData());
		if (testFilterSelectedData.getPfFilterUnitValue().equals(ConstantApp.PF_UPF)) {
			ref_txtPfFilterUserEntry.setDisable(true);
		}

		ref_txtFreqFilterData.setText(testFilterSelectedData.getFreqFilterData());
		ref_txtEnergyFilterData.setText(testFilterSelectedData.getEnergyFilterData());
		ref_txt_IterationIdHeaderPrefix.setText(testFilterSelectedData.getIterationReadingPrefixValue());

		ref_chkBxHeader3VoltageFilter.setSelected(testFilterSelectedData.isHeader3_VoltFilterSelected());
		ref_chkBxHeader3CurrentFilter.setSelected(testFilterSelectedData.isHeader3_CurrentFilterSelected());
		ref_chkBxHeader3PfFilter.setSelected(testFilterSelectedData.isHeader3_PfFilterSelected());
		ref_chkBxHeader3FreqFilter.setSelected(testFilterSelectedData.isHeader3_FreqFilterSelected());
		ref_chkBxHeader3EnergyFilter.setSelected(testFilterSelectedData.isHeader3_EnergyFilterSelected());
		// ref_chkBxHeader3IterationIdFilter.setSelected(testFilterSelectedData.isHeader3_IterationReadingIdSelected());
		ref_chkBx_IterationIdHeaderPrefix.setSelected(testFilterSelectedData.isHeader3_IterationReadingIdSelected());
		ref_chkBxHeader3CustomAllowed.setSelected(testFilterSelectedData.isHeader3_CustomSelected());
		ApplicationLauncher.logger.debug("guiRefreshSelectedTestFilterList : isHeader3_CustomSelected: "
				+ testFilterSelectedData.isHeader3_CustomSelected());
		ref_chkBx_IterationIdHeaderPrefix.setSelected(testFilterSelectedData.isPopulateIterationHeaderSelected());
		if (testFilterSelectedData.isPopulateIterationHeaderSelected()) {
			ref_txt_IterationIdHeaderPrefix.setDisable(false);
		} else {
			ref_txt_IterationIdHeaderPrefix.setDisable(true);
		}

		ref_cmbBxReplicateCountUserEntry.getSelectionModel().select(testFilterSelectedData.getReplicateCountValue());// .getSelectedItem().toString());
		ref_cmbBxOperationInputUserEntry.getSelectionModel().select(testFilterSelectedData.getOperationInputKey());// .getSelectedItem().toString());
		ref_txtFilterComments.setText(testFilterSelectedData.getUserCommentValue());
		ref_txtCustomTestNameUserEntry.setText(testFilterSelectedData.getCustomTestNameUserEntry());
		ref_txtCustomTestNameData.setText(testFilterSelectedData.getCustomTestNameData());

		ref_chkBxCompareWithLimits.setSelected(testFilterSelectedData.isOperationCompareLimitsSelected());
		ref_chkBxMergeUpperLowerLimit.setSelected(testFilterSelectedData.isOperationMergeLimitsSelected());

		ArrayList<String> tempList = new ArrayList<String>();

		if (testFilterSelectedData.isHeader3_CustomSelected()) {
			ref_txtHeader3Value.setDisable(false);
		}

		// ApplicationLauncher.logger.debug("fetchTestFilterGuiData:
		// getOperationProcessInputKeyList: " +
		// testFilterSelectedData.getOperationProcessInputKeyList());

		// testFilterData.setOperationProcessMethod(ref_cmbBxOperationCriteriaProcessData.getSelectionModel().getSelectedItem().toString());

		// testFilterData.setOperationProcessMasterOutputKey(ref_cmbBxOperationCriteriaMasterOutputData.getSelectionModel().getSelectedItem().toString());

		ref_cmbBxOperationCriteriaLocalOutputData.getSelectionModel()
				.select(testFilterSelectedData.getOperationProcessLocalOutputKey());// .getSelectedItem().toString());
		// testFilterSelectedData.setOperationProcessLocalResult("");
		ref_cmbBxOperationComparedLocalResultStatusOutput.getSelectionModel()
				.select(testFilterSelectedData.getOperationProcessLocalComparedStatus());// .getSelectedItem().toString());
		String data = testFilterSelectedData.getOperationProcessMasterComparedStatus();
		if (data == null) {
			testFilterSelectedData
					.setOperationProcessMasterComparedStatus(ConstantReportV2.POPULATE_MASTER_OUTPUT_STATUS_KEY);
		} else {
			ref_cmbBxOperationComparedMasterResultStatusOutput.getSelectionModel()
					.select(testFilterSelectedData.getOperationProcessMasterComparedStatus());
		}
		ref_txtAllowedUpperLimit.setText(testFilterSelectedData.getOperationProcessLocalUpperLimit());
		ref_txtAllowedLowerLimit.setText(testFilterSelectedData.getOperationProcessLocalLowerLimit());

		// ApplicationLauncher.logger.debug("guiRefreshSelectedTestFilterList :
		// guiRefreshSelectedTestFilterList: " +
		// testFilterSelectedData.isPopulateOperationMasterOutput());
		ref_chkBxPopulateMasterOutputData.setSelected(testFilterSelectedData.isPopulateOperationMasterOutput());
		ref_chkBxPopulateLocalOutputData.setSelected(testFilterSelectedData.isPopulateOperationLocalOutput());
		ref_chkBxPopulateUpperLimitData.setSelected(testFilterSelectedData.isPopulateOperationUpperLimit());
		ref_chkBxPopulateLowerLimitData.setSelected(testFilterSelectedData.isPopulateOperationLowerLimit());
		ref_chkBxPopulateComparedLocalResultStatus
				.setSelected(testFilterSelectedData.isPopulateOperationComparedLocalStatus());
		ref_chkBxPopulateComparedMasterResultStatus
				.setSelected(testFilterSelectedData.isPopulateOperationComparedMasterStatus());
		// Collection<RpPrintPosition> testFilterDataCellList =
		// ref_tvTestFilterCellStartPosition.getItems();
		// ArrayList<RpPrintPosition> tempListExcelModelList = new
		// ArrayList<RpPrintPosition> (testFilterDataCellList);
		// testFilterSelectedData.setTestFilterDataCellList(tempListExcelModelList);
		// ApplicationLauncher.logger.debug("fetchTestFilterGuiData:
		// getTestFilterDataCellList: " +
		// testFilterSelectedData.getTestFilterDataCellList());

		// Collection<RpPrintPosition> headerDataCellList =
		// ref_tvTestFilterCellHeaderPosition.getItems();
		// tempListExcelModelList = new ArrayList<RpPrintPosition> (headerDataCellList);
		// testFilterSelectedData.setHeaderDataCellList(tempListExcelModelList);
		// ApplicationLauncher.logger.debug("fetchTestFilterGuiData:
		// getHeaderDataCellList: " + testFilterSelectedData.getHeaderDataCellList());

		// ApplicationLauncher.logger.debug("fetchTestFilterGuiData:
		// TEST_TYPE_ALIAS_HASH_MAP: " + ConstantReport.TEST_TYPE_ALIAS_HASH_MAP);
		// String selectedTestTypeKey =
		// ref_cmbBxTestType.getSelectionModel().getSelectedItem().toString();

		// ref_cmbBxTestType.getSelectionModel().select(testFilterSelectedData.getTestTypeSelected());//selectedTestTypeKey);

		/*
		 * try{
		 * 
		 * ApplicationLauncher.logger.
		 * debug("fetchTestFilterGuiData: selectedTestTypeKey: " + selectedTestTypeKey);
		 * ApplicationLauncher.logger.
		 * debug("fetchTestFilterGuiData: TEST_TYPE_ALIAS_HASH_MAP2: " +
		 * ConstantReport.TEST_TYPE_ALIAS_HASH_MAP.get(selectedTestTypeKey));
		 * //ArrayList<String> selectedTestTypeAlias =
		 * //Collection<String> selectedTestTypeAlias =
		 * ConstantReport.TEST_TYPE_ALIAS_HASH_MAP.get(selectedTestTypeKey);
		 * //testFilterData.setTestTypeAlias(selectedTestTypeAlias.iterator().next());
		 * String selectedTestTypeAlias =
		 * ConstantReport.TEST_TYPE_ALIAS_HASH_MAP.get(selectedTestTypeKey);
		 * testFilterSelectedData.setTestTypeAlias(selectedTestTypeAlias);
		 * } catch (Exception e) {
		 * e.printStackTrace();
		 * testFilterSelectedData.setTestTypeAlias("");
		 * ApplicationLauncher.logger.error("fetchTestFilterGuiData: Exception: " +
		 * e.getMessage());
		 * }
		 * ApplicationLauncher.logger.debug("fetchTestFilterGuiData: getTestTypeAlias: "
		 * + testFilterSelectedData.getTestTypeAlias());
		 */

		// String operationCriteriaMasterOutputDataSelectedKey =
		// ref_cmbBxOperationCriteriaMasterOutputData.getSelectionModel().getSelectedItem().toString();

		// OperationProcess operationProcessMasterOutputData = new OperationProcess();

		// #DebugOperationProcess
		// operationProcessMasterOutputData.setOperationProcessKey(operationCriteriaMasterOutputDataSelectedKey);
		// operationProcessMasterOutputData.setResultValue("");
		// operationProcessMasterOutputData.setComparedStatus(ref_cmbBxOperationComparedResultStatusOutput.getSelectionModel().getSelectedItem().toString());
		// operationProcessMasterOutputData.setUpperLimit(ref_txtAllowedUpperLimit.getText());
		// operationProcessMasterOutputData.setLowerLimit(ref_txtAllowedLowerLimit.getText());
		// operationProcessMasterOutputData.setOperationProcessDataType(ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_MASTER_OUTPUT);
		// operationProcessMasterOutputData.setPopulateOnlyHeaders(false);
		// operationProcessDataHashMapMasterOutData.addOperationProcessData(operationCriteriaMasterOutputDataSelectedKey,operationProcessMasterOutpuData);

		if (testFilterSelectedData.getOperationProcessDataList().size() > 0) {

			ref_cmbBxOperationCriteriaMasterOutputData.getSelectionModel()
					.select(testFilterSelectedData.getOperationProcessDataList().get(0).getOperationProcessKey());
			ref_cmbBxOperationComparedLocalResultStatusOutput.getSelectionModel()
					.select(testFilterSelectedData.getOperationProcessDataList().get(0).getLocalComparedStatus());
			ref_cmbBxOperationComparedMasterResultStatusOutput.getSelectionModel()
					.select(testFilterSelectedData.getOperationProcessDataList().get(0).getMasterComparedStatus());
			ref_txtAllowedUpperLimit
					.setText(testFilterSelectedData.getOperationProcessDataList().get(0).getUpperLimit());
			ref_txtAllowedLowerLimit
					.setText(testFilterSelectedData.getOperationProcessDataList().get(0).getLowerLimit());

		}

		String keyParam = "";

		/*
		 * ref_chkBxPopulateRsmInitial.setSelected(false);
		 * ref_chkBxPopulateRsmFinal.setSelected(false);
		 * ref_chkBxPopulateDutInitial.setSelected(false);
		 * ref_chkBxPopulateDutFinal.setSelected(false);
		 * ref_chkBxPopulateDutDifference.setSelected(false);
		 * ref_chkBxPopulateDutPulseCount.setSelected(false);
		 * ref_chkBxPopulateDutCalcErrorPercentage.setSelected(false);
		 * ref_chkBxPopulateDutOnePulsePeriod.setSelected(false);
		 * ref_chkBxPopulateDutAverage.setSelected(false);
		 * ref_chkBxPopulateHeader4.setSelected(false);
		 * ref_chkBxPopulateHeader5.setSelected(false);
		 */

		ref_chkBxPopulateRsmInitial.setSelected(false);
		ref_chkBxPopulateRsmFinal.setSelected(false);
		ref_chkBxPopulateRsmDifference.setSelected(false);
		ref_chkBxPopulateHeaderTestPeriod.setSelected(false);
		ref_chkBxPopulateHeaderWarmupPeriod.setSelected(false);
		ref_chkBxPopulateHeaderActualVoltage.setSelected(false);
		ref_chkBxPopulateHeaderActualCurrent.setSelected(false);
		ref_chkBxPopulateHeaderActualPf.setSelected(false);
		ref_chkBxPopulateHeaderActualFreq.setSelected(false);
		ref_chkBxPopulateHeaderActualEnergy.setSelected(false);
		ref_chkBxPopulateHeader1.setSelected(false);
		ref_chkBxPopulateHeader2.setSelected(false);
		ref_chkBxPopulateHeader3.setSelected(false);
		ref_chkBxPopulateHeader4.setSelected(false);
		ref_chkBxPopulateHeader5.setSelected(false);
		ref_chkBxPopulateDutInitial.setSelected(false);
		ref_chkBxPopulateDutFinal.setSelected(false);
		ref_chkBxPopulateDutDifference.setSelected(false);
		ref_chkBxPopulateDutPulseCount.setSelected(false);
		ref_chkBxPopulateDutCalcErrorPercentage.setSelected(false);
		ref_chkBxPopulateDutOnePulsePeriod.setSelected(false);
		ref_chkBxPopulateDutAverageValue.setSelected(false);
		ref_chkBxPopulateDutAverageStatus.setSelected(false);

		for (int i = 0; i < testFilterSelectedData.getRpPrintPositionList().size(); i++) {
			RpPrintPosition rpPrintData = testFilterSelectedData.getRpPrintPositionList().get(i);
			if (rpPrintData.getDataOwner().equals(ConstantReportV2.RP_DATA_OWNER_TEST_DATA_FILTER)) {
				keyParam = rpPrintData.getKeyParam();
				ApplicationLauncher.logger.info("guiRefreshSelectedTestFilterList: keyParam: " + keyParam);
				switch (keyParam) {
					/*
					 * case ConstantReportV2.CELL_START_POSITION_HEADER_RESULT_DATA_KEY:
					 * rpPrintPositionList.setPopulateResultValue(true);
					 * break;
					 * 
					 * case ConstantReportV2.POPULATE_LOCAL_OUTPUT_STATUS_KEY:
					 * rpPrintPositionList.setPopulateLocalResultStatus(true);
					 * break;
					 * 
					 * case ConstantReportV2.POPULATE_MASTER_OUTPUT_STATUS_KEY:
					 * rpPrintPositionList.setPopulateMasterResultStatus(true);
					 * break;
					 * 
					 * case ConstantReportV2.:
					 * rpPrintPositionList.setPopulateResultValue(true);
					 * break;
					 * case ConstantReportV2.RESULT_DATA_TYPE_DISPLAY_DUT_FINAL_REGISTER:
					 * rpPrintPositionList.setPopulateResultValue(true);
					 * break;
					 * 
					 * case ConstantReportV2.CELL_START_POSITION_HEADER_RESULT_STATUS_KEY:
					 * rpPrintPositionList.setPopulateLocalResultStatus(true);
					 * break;
					 * 
					 * case ConstantReportV2.CELL_HEADER_POSITION_HEADER_RESULT_RSM_INITIAL:
					 * //rpPrintPositionList.setPopulateResultValue(true);
					 * break;
					 * 
					 * case ConstantReportV2.CELL_HEADER_POSITION_HEADER_RESULT_RSM_FINAL:
					 * //rpPrintPositionList.setPopulateResultValue(true);
					 * break;
					 */

					case ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_RSM_INITIAL:
						ref_chkBxPopulateRsmInitial.setSelected(true);
						break;

					case ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_RSM_FINAL:
						ref_chkBxPopulateRsmFinal.setSelected(true);
						break;

					case ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_RSM_DIFFERENCE:
						ref_chkBxPopulateRsmDifference.setSelected(true);
						break;

					case ConstantReportV2.POPULATE_HEADER_KEY_TEST_PERIOD_IN_MINUTES:
						ref_chkBxPopulateHeaderTestPeriod.setSelected(true);
						break;

					case ConstantReportV2.POPULATE_HEADER_KEY_WARMUP_PERIOD_IN_MINUTES:
						ref_chkBxPopulateHeaderWarmupPeriod.setSelected(true);
						break;

					case ConstantReportV2.POPULATE_HEADER_KEY_TARGET_VOLTAGE:
						ref_chkBxPopulateHeaderActualVoltage.setSelected(true);
						break;

					case ConstantReportV2.POPULATE_HEADER_KEY_TARGET_CURRENT:
						ref_chkBxPopulateHeaderActualCurrent.setSelected(true);
						break;

					case ConstantReportV2.POPULATE_HEADER_KEY_TARGET_PF:
						ref_chkBxPopulateHeaderActualPf.setSelected(true);
						break;

					case ConstantReportV2.POPULATE_HEADER_KEY_TARGET_FREQ:
						ref_chkBxPopulateHeaderActualFreq.setSelected(true);
						break;

					case ConstantReportV2.POPULATE_HEADER_KEY_TARGET_ENERGY:
						ref_chkBxPopulateHeaderActualEnergy.setSelected(true);
						break;

					case ConstantReportV2.POPULATE_HEADER1_KEY:
						ref_chkBxPopulateHeader1.setSelected(true);
						break;

					case ConstantReportV2.POPULATE_HEADER2_KEY:
						ref_chkBxPopulateHeader2.setSelected(true);
						break;

					case ConstantReportV2.POPULATE_HEADER3_KEY:
						ref_chkBxPopulateHeader3.setSelected(true);
						break;

					case ConstantReportV2.POPULATE_HEADER4_KEY:
						ref_chkBxPopulateHeader4.setSelected(true);
						break;

					case ConstantReportV2.POPULATE_HEADER5_KEY:
						ref_chkBxPopulateHeader5.setSelected(true);
						break;

					/*
					 * case ConstantReportV2.POPULATE_HEADER1_KEY:
					 * rpPrintPositionList.setPopulateHeader1(true);
					 * break;
					 * 
					 * case ConstantReportV2.POPULATE_HEADER2_KEY:
					 * rpPrintPositionList.setPopulateHeader2(true);
					 * break;
					 * 
					 * case ConstantReportV2.POPULATE_HEADER3_KEY:
					 * rpPrintPositionList.setPopulateHeader3(true);
					 * break;
					 * 
					 */

					/*
					 * 
					 * case ConstantReportV2.POPULATE_HEADER_KEY_TEST_PERIOD_IN_MINUTES:
					 * rpPrintPositionList.setPopulateHeaderTestPeriodInMinutes(true);
					 * break;
					 * case ConstantReportV2.POPULATE_HEADER_KEY_WARMUP_PERIOD_IN_MINUTES:
					 * rpPrintPositionList.setPopulateHeaderWarmupPeriodInMinutes(true);
					 * break;
					 * case ConstantReportV2.POPULATE_HEADER_KEY_ACTUAL_VOLTAGE:
					 * rpPrintPositionList.setPopulateHeaderActualVoltage(true);
					 * break;
					 * case ConstantReportV2.POPULATE_HEADER_KEY_ACTUAL_CURRENT:
					 * rpPrintPositionList.setPopulateHeaderActualCurrent(true);
					 * break;
					 * case ConstantReportV2.POPULATE_HEADER_KEY_ACTUAL_PF:
					 * rpPrintPositionList.setPopulateHeaderActualPf(true);
					 * break;
					 * case ConstantReportV2.POPULATE_HEADER_KEY_ACTUAL_FREQ:
					 * rpPrintPositionList.setPopulateHeaderActualFreq(true);
					 * break;
					 * case ConstantReportV2.POPULATE_HEADER_KEY_ACTUAL_ENERGY:
					 * rpPrintPositionList.setPopulateHeaderActualEnergy(true);
					 * break;
					 * 
					 * case ConstantReportV2.POPULATE_LOCAL_UPPER_LIMIT_KEY:
					 * rpPrintPositionList.setPopulateHeaderUpperLimit(true);
					 * break;
					 * 
					 * case ConstantReportV2.POPULATE_LOCAL_LOWER_LIMIT_KEY:
					 * rpPrintPositionList.setPopulateHeaderLowerLimit(true);
					 * break;
					 * 
					 * case ConstantReportV2.POPULATE_LOCAL_MERGED_LIMIT_KEY:
					 * rpPrintPositionList.setPopulateHeaderMergedLimit(true);
					 * break;
					 * 
					 * 
					 * case ConstantReportV2.POPULATE_MASTER_UPPER_LIMIT_KEY:
					 * rpPrintPositionList.setPopulateHeaderUpperLimit(true);
					 * break;
					 * 
					 * case ConstantReportV2.POPULATE_MASTER_LOWER_LIMIT_KEY:
					 * rpPrintPositionList.setPopulateHeaderLowerLimit(true);
					 * break;
					 * case ConstantReportV2.POPULATE_MASTER_MERGED_LIMIT_KEY:
					 * rpPrintPositionList.setPopulateHeaderMergedLimit(true);
					 * break;
					 */

					default:
						break;
				}

				if (keyParam.equals(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_INITIAL_REGISTER)) {
					ref_chkBxPopulateDutInitial.setSelected(true);
				} else if (keyParam.equals(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_FINAL_REGISTER)) {
					ref_chkBxPopulateDutFinal.setSelected(true);
				} else if (keyParam.equals(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_DIFFERENCE)) {
					ref_chkBxPopulateDutDifference.setSelected(true);
				} else if (keyParam.equals(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_PULSE_COUNT)) {
					ref_chkBxPopulateDutPulseCount.setSelected(true);
				} else if (keyParam.equals(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_ERROR_VALUE)) {
					ref_chkBxPopulateDutCalcErrorPercentage.setSelected(true);
				} else if (keyParam.equals(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_ONE_PULSE_DURATION)) {
					ref_chkBxPopulateDutOnePulsePeriod.setSelected(true);
				} else if (keyParam.equals(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_AVERAGE_VALUE)) {
					ref_chkBxPopulateDutAverageValue.setSelected(true);
				} else if (keyParam.equals(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_AVERAGE_STATUS)) {
					ref_chkBxPopulateDutAverageStatus.setSelected(true);
				} else if (keyParam.equals(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_REPEAT_AVERAGE_VALUE)) {
					ref_chkBxPopulateDutAverageValue.setSelected(true);
				} else if (keyParam.equals(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_REPEAT_AVERAGE_STATUS)) {
					ref_chkBxPopulateDutAverageStatus.setSelected(true);
				} else if (keyParam.equals(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_SELF_HEAT_AVERAGE_VALUE)) {
					ref_chkBxPopulateDutAverageValue.setSelected(true);
				} else if (keyParam.equals(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_SELF_HEAT_AVERAGE_STATUS)) {
					ref_chkBxPopulateDutAverageStatus.setSelected(true);
				}
			}
		}

		clearOperationProcessDbIdHashMap();
		clearRpPrintDbIdHashMap();
		for (int i = 0; i < testFilterSelectedData.getOperationProcessDataList().size(); i++) {
			OperationProcess opProData = testFilterSelectedData.getOperationProcessDataList().get(i);

			if (opProData.getId() != null) {
				// operationProcessDbIdHashMap.put(opProData.getOperationProcessKey(),
				// opProData.getId());
				addOperationProcessDbIdHashMap(opProData.getOperationProcessKey(), opProData.getId());
			}
		}

		for (int i = 0; i < testFilterSelectedData.getRpPrintPositionList().size(); i++) {
			RpPrintPosition rpPrintData = testFilterSelectedData.getRpPrintPositionList().get(i);
			if (rpPrintData.getDataOwner().equals(ConstantReportV2.RP_DATA_OWNER_TEST_DATA_FILTER)) {
				if (rpPrintData.getId() != null) {
					// operationProcessDbIdHashMap.put(opProData.getOperationProcessKey(),
					// opProData.getId());
					addRpPrintDbIdHashMap(rpPrintData.getKeyParam(), rpPrintData.getId());
				}
			}
		}

		/*
		 * String upperLimitCellPosition = "";
		 * String lowerLimitCellPosition = "";
		 * String resultValueCellPosition = "";
		 * String resultStatusCellPosition = "";
		 * String headerKey = "";
		 * if(ref_rdBtnOperationOutput.isSelected()){
		 * //if(ref_chkBxPopulateMasterOutputData.setSelected()){
		 * if(!ref_chkBxPopulateMasterOutputData.isDisabled()){
		 * 
		 * if(OPERATION_INPUT_HEADERS_ONLY_DATA_TYPE_LIST.contains(
		 * testFilterSelectedData.getTestExecutionResultTypeSelected())){
		 * //testFilterData.setOperationProcessMasterOutputOnlyHeader(true);
		 * // #DebugOperationProcess
		 * operationProcessMasterOutputData.setPopulateOnlyHeaders(true);
		 * }
		 * 
		 * if(ref_chkBxPopulateMasterOutputData.setSelected()){
		 * testFilterSelectedData.setResultDataType(
		 * OPERATION_MASTER_OUTPUT_DATA_HEADER_DISPLAY_PREFIX);
		 * 
		 * }else{
		 * if(ref_chkBxPopulateUpperLimitData.setSelected()){
		 * testFilterSelectedData.setResultDataType(
		 * OPERATION_MASTER_OUTPUT_DATA_HEADER_DISPLAY_PREFIX);
		 * }else if(ref_chkBxPopulateLowerLimitData.setSelected()){
		 * testFilterSelectedData.setResultDataType(
		 * OPERATION_MASTER_OUTPUT_DATA_HEADER_DISPLAY_PREFIX);
		 * }else if(ref_chkBxPopulateComparedResultStatus.setSelected()){
		 * testFilterSelectedData.setResultDataType(
		 * OPERATION_MASTER_OUTPUT_STATUS_HEADER_DISPLAY_PREFIX);
		 * }else{
		 * testFilterSelectedData.setResultDataType(
		 * OPERATION_MASTER_OUTPUT_DATA_HEADER_DISPLAY_PREFIX);
		 * }
		 * }
		 * 
		 * resultValueCellPosition = "";
		 * resultStatusCellPosition = "";
		 * for(int i =0 ; i< testFilterSelectedData.getTestFilterDataCellList().size();
		 * i++){
		 * //headerKey = testFilterData.getTestFilterDataCellList().get(i).getheader();
		 * headerKey =
		 * testFilterSelectedData.getTestFilterDataCellList().get(i).getKeyParam();
		 * if(headerKey.startsWith(OPERATION_MASTER_OUTPUT_DATA_HEADER_DISPLAY_PREFIX)){
		 * //resultValueCellPosition =
		 * testFilterData.getTestFilterDataCellList().get(i).getcell_value();
		 * resultValueCellPosition =
		 * testFilterSelectedData.getTestFilterDataCellList().get(i).getCellPosition();
		 * }else
		 * if(headerKey.startsWith(OPERATION_MASTER_OUTPUT_STATUS_HEADER_DISPLAY_PREFIX)
		 * ){
		 * //resultStatusCellPosition =
		 * testFilterData.getTestFilterDataCellList().get(i).getcell_value();
		 * resultStatusCellPosition =
		 * testFilterSelectedData.getTestFilterDataCellList().get(i).getCellPosition();
		 * }
		 * }
		 * if(!resultValueCellPosition.isEmpty()){
		 * testFilterSelectedData.setResultValueCellPosition(resultValueCellPosition);
		 * }
		 * if(!resultStatusCellPosition.isEmpty()){
		 * testFilterSelectedData.setResultStatusCellPosition(resultStatusCellPosition);
		 * }
		 * 
		 * upperLimitCellPosition = "";
		 * lowerLimitCellPosition = "";
		 * 
		 * for(int i =0 ; i< testFilterSelectedData.getHeaderDataCellList().size();
		 * i++){
		 * //headerKey = testFilterData.getHeaderDataCellList().get(i).getheader();
		 * headerKey =
		 * testFilterSelectedData.getHeaderDataCellList().get(i).getKeyParam();
		 * if(headerKey.equals(OPERATION_POPULATE_MASTER_UPPER_LIMIT_KEY)){
		 * //upperLimitCellPosition =
		 * testFilterData.getHeaderDataCellList().get(i).getcell_value();
		 * upperLimitCellPosition =
		 * testFilterSelectedData.getHeaderDataCellList().get(i).getCellPosition();
		 * }else if(headerKey.equals(OPERATION_POPULATE_MASTER_LOWER_LIMIT_KEY)){
		 * //lowerLimitCellPosition =
		 * testFilterData.getHeaderDataCellList().get(i).getcell_value();
		 * lowerLimitCellPosition =
		 * testFilterSelectedData.getHeaderDataCellList().get(i).getCellPosition();
		 * }else if(headerKey.equals(OPERATION_POPULATE_MASTER_MERGED_LIMIT_KEY)){
		 * //upperLimitCellPosition =
		 * testFilterData.getHeaderDataCellList().get(i).getcell_value();
		 * upperLimitCellPosition =
		 * testFilterSelectedData.getHeaderDataCellList().get(i).getCellPosition();
		 * }
		 * }
		 * if(!upperLimitCellPosition.isEmpty()){
		 * testFilterSelectedData.setResultUpperLimitCellPosition(upperLimitCellPosition
		 * );
		 * }
		 * if(!lowerLimitCellPosition.isEmpty()){
		 * testFilterSelectedData.setResultLowerLimitCellPosition(lowerLimitCellPosition
		 * );
		 * }
		 * //}else if(ref_chkBxPopulateLocalOutputData.setSelected()){
		 * }else if(!ref_chkBxPopulateLocalOutputData.isDisabled()){
		 * 
		 * 
		 * if(OPERATION_INPUT_HEADERS_ONLY_DATA_TYPE_LIST.contains(
		 * testFilterSelectedData.getTestExecutionResultTypeSelected())){
		 * testFilterSelectedData.setOperationProcessLocalOutputOnlyHeader(true);
		 * }
		 * 
		 * if(ref_chkBxPopulateLocalOutputData.setSelected()){
		 * testFilterSelectedData.setResultDataType(
		 * OPERATION_LOCAL_OUTPUT_DATA_HEADER_DISPLAY_PREFIX);
		 * 
		 * }else{
		 * if(ref_chkBxPopulateUpperLimitData.setSelected()){
		 * testFilterSelectedData.setResultDataType(
		 * OPERATION_LOCAL_OUTPUT_DATA_HEADER_DISPLAY_PREFIX);
		 * }else if(ref_chkBxPopulateLowerLimitData.setSelected()){
		 * testFilterSelectedData.setResultDataType(
		 * OPERATION_LOCAL_OUTPUT_DATA_HEADER_DISPLAY_PREFIX);
		 * }else if(ref_chkBxPopulateComparedResultStatus.setSelected()){
		 * testFilterSelectedData.setResultDataType(
		 * OPERATION_LOCAL_OUTPUT_STATUS_HEADER_DISPLAY_PREFIX);
		 * }else{
		 * testFilterSelectedData.setResultDataType(
		 * OPERATION_LOCAL_OUTPUT_DATA_HEADER_DISPLAY_PREFIX);
		 * }
		 * }
		 * resultValueCellPosition = "";
		 * resultStatusCellPosition = "";
		 * for(int i =0 ; i< testFilterSelectedData.getTestFilterDataCellList().size();
		 * i++){
		 * //headerKey = testFilterData.getTestFilterDataCellList().get(i).getheader();
		 * headerKey =
		 * testFilterSelectedData.getTestFilterDataCellList().get(i).getKeyParam();
		 * if(headerKey.startsWith(OPERATION_LOCAL_OUTPUT_DATA_HEADER_DISPLAY_PREFIX)){
		 * //resultValueCellPosition =
		 * testFilterData.getTestFilterDataCellList().get(i).getcell_value();
		 * resultValueCellPosition =
		 * testFilterSelectedData.getTestFilterDataCellList().get(i).getCellPosition();
		 * }else
		 * if(headerKey.startsWith(OPERATION_LOCAL_OUTPUT_STATUS_HEADER_DISPLAY_PREFIX))
		 * {
		 * //resultStatusCellPosition =
		 * testFilterData.getTestFilterDataCellList().get(i).getcell_value();
		 * resultStatusCellPosition =
		 * testFilterSelectedData.getTestFilterDataCellList().get(i).getCellPosition();
		 * }
		 * }
		 * if(!resultValueCellPosition.isEmpty()){
		 * testFilterSelectedData.setResultValueCellPosition(resultValueCellPosition);
		 * }
		 * if(!resultStatusCellPosition.isEmpty()){
		 * testFilterSelectedData.setResultStatusCellPosition(resultStatusCellPosition);
		 * }
		 * 
		 * upperLimitCellPosition = "";
		 * lowerLimitCellPosition = "";
		 * for(int i =0 ; i< testFilterSelectedData.getHeaderDataCellList().size();
		 * i++){
		 * //headerKey = testFilterData.getHeaderDataCellList().get(i).getheader();
		 * headerKey =
		 * testFilterSelectedData.getHeaderDataCellList().get(i).getKeyParam();
		 * if(headerKey.equals(OPERATION_POPULATE_LOCAL_UPPER_LIMIT_KEY)){
		 * //upperLimitCellPosition =
		 * testFilterData.getHeaderDataCellList().get(i).getcell_value();
		 * upperLimitCellPosition =
		 * testFilterSelectedData.getHeaderDataCellList().get(i).getCellPosition();
		 * }else if(headerKey.equals(OPERATION_POPULATE_LOCAL_LOWER_LIMIT_KEY)){
		 * //lowerLimitCellPosition =
		 * testFilterData.getHeaderDataCellList().get(i).getcell_value();
		 * lowerLimitCellPosition =
		 * testFilterSelectedData.getHeaderDataCellList().get(i).getCellPosition();
		 * }else if(headerKey.equals(OPERATION_POPULATE_LOCAL_MERGED_LIMIT_KEY)){
		 * //upperLimitCellPosition =
		 * testFilterData.getHeaderDataCellList().get(i).getcell_value();
		 * upperLimitCellPosition =
		 * testFilterSelectedData.getHeaderDataCellList().get(i).getCellPosition();
		 * }
		 * }
		 * if(!upperLimitCellPosition.isEmpty()){
		 * testFilterSelectedData.setResultUpperLimitCellPosition(upperLimitCellPosition
		 * );
		 * }
		 * if(!lowerLimitCellPosition.isEmpty()){
		 * testFilterSelectedData.setResultLowerLimitCellPosition(lowerLimitCellPosition
		 * );
		 * }
		 * }
		 * }else{
		 * testFilterSelectedData.setResultDataType("Error");
		 * for(int i =0 ; i< testFilterSelectedData.getTestFilterDataCellList().size();
		 * i++){
		 * //headerKey = testFilterData.getTestFilterDataCellList().get(i).getheader();
		 * headerKey =
		 * testFilterSelectedData.getTestFilterDataCellList().get(i).getKeyParam();
		 * if(headerKey.equals(CELL_START_POSITION_HEADER_RESULT_DATA_KEY)){
		 * //resultValueCellPosition =
		 * testFilterData.getTestFilterDataCellList().get(i).getcell_value();
		 * resultValueCellPosition =
		 * testFilterSelectedData.getTestFilterDataCellList().get(i).getCellPosition();
		 * }else if(headerKey.equals(CELL_START_POSITION_HEADER_RESULT_STATUS_KEY)){
		 * //resultStatusCellPosition =
		 * testFilterData.getTestFilterDataCellList().get(i).getcell_value();
		 * resultStatusCellPosition =
		 * testFilterSelectedData.getTestFilterDataCellList().get(i).getCellPosition();
		 * }
		 * }
		 * 
		 * 
		 * testFilterSelectedData.setResultValueCellPosition(resultValueCellPosition);
		 * testFilterSelectedData.setResultStatusCellPosition(resultStatusCellPosition);
		 * }
		 */

		// testFilterSelectedData.addOperationProcessDataList(operationProcessMasterOutputData);

		/*
		 * String header1CellPosition = "";
		 * String header2CellPosition = "";
		 * String header3CellPosition = "";
		 * 
		 * for(int i =0 ; i< testFilterSelectedData.getHeaderDataCellList().size();
		 * i++){
		 * //headerKey = testFilterData.getHeaderDataCellList().get(i).getheader();
		 * headerKey =
		 * testFilterSelectedData.getHeaderDataCellList().get(i).getKeyParam();
		 * if(headerKey.equals(POPULATE_HEADER1_KEY)){
		 * //header1CellPosition =
		 * testFilterData.getHeaderDataCellList().get(i).getcell_value();
		 * header1CellPosition =
		 * testFilterSelectedData.getHeaderDataCellList().get(i).getCellPosition();
		 * }else if(headerKey.equals(POPULATE_HEADER2_KEY)){
		 * //header2CellPosition =
		 * testFilterData.getHeaderDataCellList().get(i).getcell_value();
		 * header2CellPosition =
		 * testFilterSelectedData.getHeaderDataCellList().get(i).getCellPosition();
		 * }else if(headerKey.equals(POPULATE_HEADER3_KEY)){
		 * //header3CellPosition =
		 * testFilterData.getHeaderDataCellList().get(i).getcell_value();
		 * header3CellPosition =
		 * testFilterSelectedData.getHeaderDataCellList().get(i).getCellPosition();
		 * }
		 * }
		 */
		/*
		 * testFilterSelectedData.setHeader1_CellPosition(header1CellPosition);
		 * testFilterSelectedData.setHeader2_CellPosition(header2CellPosition);
		 * testFilterSelectedData.setHeader3_CellPosition(header3CellPosition);
		 * String nonDisplayedDataSet = "";
		 * testFilterSelectedData.setOperationProcessMethod(ConstantReportV2.
		 * NONE_DISPLAYED);
		 */
		/*
		 * if(ref_rdBtnOperationInput.setSelected()){
		 * nonDisplayedDataSet =
		 * ref_cmbBxOperationInputUserEntry.getSelectionModel().getSelectedItem().
		 * toString();
		 * 
		 * 
		 * }else if(ref_rdBtnOperationOutput.setSelected()){
		 */

		/*
		 * if(!operationCriteriaMasterOutputDataSelectedKey.equals(ConstantReportV2.
		 * NONE_DISPLAYED)){
		 * if(!ref_chkBxPopulateMasterOutputData.setSelected()){
		 * if(!chkBxPopulateComparedResultStatus.isDisabled()){
		 * if(!chkBxPopulateComparedResultStatus.setSelected()){
		 * nonDisplayedDataSet =
		 * ref_cmbBxOperationCriteriaMasterOutputData.getSelectionModel().
		 * getSelectedItem().toString();
		 * }
		 * }else{
		 * nonDisplayedDataSet =
		 * ref_cmbBxOperationCriteriaMasterOutputData.getSelectionModel().
		 * getSelectedItem().toString();
		 * }
		 * }
		 * }else {
		 * if(!ref_chkBxPopulateLocalOutputData.setSelected()){
		 * if(!chkBxPopulateComparedResultStatus.isDisabled()){
		 * if(!chkBxPopulateComparedResultStatus.setSelected()){
		 * nonDisplayedDataSet =
		 * ref_cmbBxOperationCriteriaLocalOutputData.getSelectionModel().getSelectedItem
		 * ().toString();
		 * }
		 * }else{
		 * nonDisplayedDataSet =
		 * ref_cmbBxOperationCriteriaLocalOutputData.getSelectionModel().getSelectedItem
		 * ().toString();
		 * }
		 * }
		 * 
		 * }
		 */

		/*
		 * testFilterSelectedData.setOperationProcessMethod(
		 * ref_cmbBxOperationCriteriaProcessData.getSelectionModel().getSelectedItem().
		 * toString());
		 * 
		 * 
		 * Collection<String> operationProcessInputKeyList =
		 * ref_listViewInputProcessList.getItems();
		 * tempList = new ArrayList<String> (operationProcessInputKeyList);
		 * testFilterSelectedData.setOperationProcessInputKeyList(tempList);
		 */
		// }//else{
		// testFilterData.setOperationProcessMethod(ConstantReportV2.NONE_DISPLAYED);
		// }
		/*
		 * testFilterSelectedData.setNonDisplayedDataSet(nonDisplayedDataSet);
		 * 
		 * if(ref_chkBxReplicateData.setSelected()){
		 * ArrayList<String> replicateResultCellPositionList = new ArrayList<String> ();
		 * for(int i =0 ; i< testFilterSelectedData.getTestFilterDataCellList().size();
		 * i++){
		 * //headerKey = testFilterData.getTestFilterDataCellList().get(i).getheader();
		 * headerKey =
		 * testFilterSelectedData.getTestFilterDataCellList().get(i).getKeyParam();
		 * if(headerKey.startsWith(REPLICATE_RESULT_KEY_PREFIX)){
		 * //replicateResultCellPositionList.add(testFilterData.
		 * getTestFilterDataCellList().get(i).getcell_value());
		 * replicateResultCellPositionList.add(testFilterSelectedData.
		 * getTestFilterDataCellList().get(i).getCellPosition());
		 * }
		 * }
		 * testFilterSelectedData.setReplicateResultCellPositionList(
		 * replicateResultCellPositionList);
		 * }
		 */

		/*
		 * boolean operationInputOnlyHeader = false;
		 * if(OPERATION_INPUT_HEADERS_ONLY_DATA_TYPE_LIST.contains(
		 * testFilterSelectedData.getTestExecutionResultTypeSelected())){
		 * operationInputOnlyHeader = true;
		 * }
		 */

		// testFilterSelectedData.setOperationInputOnlyHeader(operationInputOnlyHeader);

		ref_chkBxPostOperationActive.setSelected(testFilterSelectedData.isOperationProcessPostActive());
		ref_cmbBxPostOperationMethod.getSelectionModel().select(testFilterSelectedData.getOperationProcessPostMethod());// .getSelectedItem().toString());
		ref_txtPostOperationValue.setText(testFilterSelectedData.getOperationProcessPostInputValue());
		if (testFilterSelectedData.isHeader3_IterationReadingIdSelected()) {
			// ref_chkBxHeader3IterationIdFilter.setSelected(true);
			ref_chkBx_IterationIdHeaderPrefix.setSelected(true);
		} else {
			// ref_chkBxHeader3IterationIdFilter.setSelected(false);
			ref_chkBx_IterationIdHeaderPrefix.setSelected(false);
		}

		if (testFilterSelectedData.isPopulateIterationHeaderSelected()) {
			ref_chkBx_IterationIdHeaderPrefix.setSelected(true);
		} else {
			ref_chkBx_IterationIdHeaderPrefix.setSelected(false);
		}

		ref_txtIterationReadingStartingId.setText(testFilterSelectedData.getIterationReadingStartIdUserEntry());
		ref_txtIterationReadingEndingId.setText(testFilterSelectedData.getIterationReadingEndIdUserEntry());
		ref_txt_IterationIdHeaderPrefix.setText(testFilterSelectedData.getIterationReadingPrefixValue());

		/*
		 * String filterPreview =
		 * testFilterSelectedData.getTestTypeAlias()+ConstantReportV2.
		 * TEST_POINT_NAME_SEPERATOR +
		 * "XX" +ConstantReportV2.TEST_POINT_FILTER_SEPERATOR +
		 * ref_txtVoltPercentFilterData.getText();
		 * 
		 * if(!txtPfFilterUserEntry.getText().isEmpty()){
		 * filterPreview = filterPreview + ConstantReportV2.TEST_POINT_FILTER_SEPERATOR
		 * + ref_txtPfFilterData.getText();
		 * }
		 * if(!txtCurrentPercentFilterUserEntry.getText().isEmpty()){
		 * filterPreview = filterPreview + ConstantReportV2.TEST_POINT_FILTER_SEPERATOR
		 * +ref_txtCurrentPercentFilterData.getText();
		 * }
		 * if(!ref_txtFreqFilterData.getText().isEmpty()){
		 * filterPreview = filterPreview +
		 * ConstantReportV2.TEST_POINT_FILTER_SEPERATOR+ref_txtFreqFilterData.getText();
		 * }
		 * if(!ref_txtEnergyFilterData.getText().isEmpty()){
		 * filterPreview = filterPreview +
		 * ConstantReportV2.TEST_POINT_FILTER_SEPERATOR+ref_txtEnergyFilterData.getText(
		 * );
		 * }
		 * if(!ref_txtIterationReadingId.getText().isEmpty()){
		 * filterPreview = filterPreview +
		 * ConstantReportV2.TEST_POINT_FILTER_SEPERATOR+ref_txtIterationReadingId.
		 * getText();
		 * }
		 * testFilterSelectedData.setFilterPreview(filterPreview);
		 * ApplicationLauncher.logger.debug("fetchTestFilterGuiData: filterPreview: " +
		 * filterPreview);
		 * 
		 */

		List<RpPrintPosition> rpPrintPositionPrintHeaderList = testFilterSelectedData.getRpPrintPositionList().stream()
				.filter(e -> e.getDataOwner().equals(ConstantReportV2.RP_DATA_OWNER_TEST_DATA_FILTER))
				// .filter(e ->
				// e.getDataType().equals(ConstantReportV2.RP_PRINT_TYPE_HEADER_ONLY))
				.filter(e -> e.isPopulateOnlyHeaders() == true)
				.collect(Collectors.toList());

		for (int i = 0; i < rpPrintPositionPrintHeaderList.size(); i++) {
			if (rpPrintPositionPrintHeaderList.get(i).getKeyParam().equals(ConstantReportV2.POPULATE_HEADER1_KEY)) {
				// ApplicationLauncher.logger.debug("guiRefreshSelectedTestFilterList : Header1
				// Hit1: " + ref_txtHeader1Value.getText());
				if (!ref_txtHeader1Value.getText().isEmpty()) {
					// ApplicationLauncher.logger.debug("guiRefreshSelectedTestFilterList : Header1
					// Hit1: " );
					rpPrintPositionPrintHeaderList.get(i).setHeaderValue(ref_txtHeader1Value.getText());
				}
			} else if (rpPrintPositionPrintHeaderList.get(i).getKeyParam()
					.equals(ConstantReportV2.POPULATE_HEADER2_KEY)) {
				if (!ref_txtHeader2Value.getText().isEmpty()) {
					rpPrintPositionPrintHeaderList.get(i).setHeaderValue(ref_txtHeader2Value.getText());
				}
			} else if (rpPrintPositionPrintHeaderList.get(i).getKeyParam()
					.equals(ConstantReportV2.POPULATE_HEADER3_KEY)) {
				if (!ref_txtHeader3Value.getText().isEmpty()) {
					rpPrintPositionPrintHeaderList.get(i).setHeaderValue(ref_txtHeader3Value.getText());
				}
			} else if (rpPrintPositionPrintHeaderList.get(i).getKeyParam()
					.equals(ConstantReportV2.POPULATE_HEADER4_KEY)) {
				if (!ref_txtHeader4Value.getText().isEmpty()) {
					rpPrintPositionPrintHeaderList.get(i).setHeaderValue(ref_txtHeader4Value.getText());
				}
			} else if (rpPrintPositionPrintHeaderList.get(i).getKeyParam()
					.equals(ConstantReportV2.POPULATE_HEADER5_KEY)) {
				if (!ref_txtHeader5Value.getText().isEmpty()) {
					rpPrintPositionPrintHeaderList.get(i).setHeaderValue(ref_txtHeader5Value.getText());
				}
			}
		}

		// ref_tvTestFilterCellHeaderPosition.getItems().clear();
		ref_tvTestFilterCellHeaderPosition.getItems().addAll(rpPrintPositionPrintHeaderList);

		List<RpPrintPosition> rpPrintPositionPrintAllList = testFilterSelectedData.getRpPrintPositionList().stream()
				.filter(e -> e.getDataOwner().equals(ConstantReportV2.RP_DATA_OWNER_TEST_DATA_FILTER))
				// .filter(e -> e.getDataType().equals(ConstantReportV2.RP_PRINT_TYPE_ALL))
				.filter(e -> e.isPopulateAllData() == true)
				.collect(Collectors.toList());

		// ref_tvTestFilterCellStartPosition.getItems().clear();
		ref_tvTestFilterCellStartPosition.getItems().addAll(rpPrintPositionPrintAllList);

		clearLastSavedTestFilterDataCellPositionHashMap();

		ref_tvTestFilterCellHeaderPosition.getItems().stream()
				.forEach(e -> {

					updateLastSavedTestFilterDataCellPositionHashMap(e.getKeyParam(), e.getCellPosition());
				});

		ref_tvTestFilterCellStartPosition.getItems().stream()
				.forEach(e -> {

					updateLastSavedTestFilterDataCellPositionHashMap(e.getKeyParam(), e.getCellPosition());
				});

		// ref_titledPaneTestTypeData.setExpanded(true);
		// return null;

		if (testFilterSelectedData.isOperationOutputSelected()) {
			rdBtnOperationOutputOnChange();

			ref_listViewInputProcessList.getItems().clear();
			ref_listViewInputProcessList.getItems().addAll(testFilterSelectedData.getOperationProcessInputKeyList());

			String masterOutputData = "";
			try {
				masterOutputData = testFilterSelectedData.getOperationProcessDataList().stream()
						.filter(e -> e.getOperationProcessDataType()
								.equals(ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_MASTER_OUTPUT))
						.findFirst()
						.get()
						.getOperationProcessKey();

			} catch (Exception e) {
				ApplicationLauncher.logger.error("guiRefreshSelectedTestFilterList: Exception1: " + e.getMessage());
			}

			ref_cmbBxOperationCriteriaProcessData.getSelectionModel()
					.select(testFilterSelectedData.getOperationProcessMethod());
			ref_cmbBxPostOperationMethod.getSelectionModel()
					.select(testFilterSelectedData.getOperationProcessPostMethod());
			ref_cmbBxOperationCriteriaMasterOutputData.getSelectionModel().select(masterOutputData);
			ref_cmbBxOperationCriteriaLocalOutputData.getSelectionModel()
					.select(testFilterSelectedData.getOperationProcessLocalOutputKey());
			ref_cmbBxOperationComparedLocalResultStatusOutput.getSelectionModel()
					.select(testFilterSelectedData.getOperationProcessLocalComparedStatus());
			ref_cmbBxOperationComparedMasterResultStatusOutput.getSelectionModel()
					.select(testFilterSelectedData.getOperationProcessMasterComparedStatus());

			ref_chkBxCompareWithLimits.setSelected(testFilterSelectedData.isOperationCompareLimitsSelected());
			ref_chkBxMergeUpperLowerLimit.setSelected(testFilterSelectedData.isOperationMergeLimitsSelected());
			ref_chkBxPostOperationActive.setSelected(testFilterSelectedData.isOperationProcessPostActive());
			// ApplicationLauncher.logger.debug("guiRefreshSelectedTestFilterList :
			// guiRefreshSelectedTestFilterList2: " +
			// testFilterSelectedData.isPopulateOperationMasterOutput());
			ref_chkBxPopulateMasterOutputData.setSelected(testFilterSelectedData.isPopulateOperationMasterOutput());
			ref_chkBxPopulateLocalOutputData.setSelected(testFilterSelectedData.isPopulateOperationLocalOutput());
			ref_chkBxPopulateUpperLimitData.setSelected(testFilterSelectedData.isPopulateOperationUpperLimit());
			ref_chkBxPopulateLowerLimitData.setSelected(testFilterSelectedData.isPopulateOperationLowerLimit());
			ref_chkBxPopulateComparedLocalResultStatus
					.setSelected(testFilterSelectedData.isPopulateOperationComparedLocalStatus());
			ref_chkBxPopulateComparedMasterResultStatus
					.setSelected(testFilterSelectedData.isPopulateOperationComparedMasterStatus());

			ref_txtAllowedLowerLimit.setText(testFilterSelectedData.getOperationProcessLocalLowerLimit());
			ref_txtAllowedUpperLimit.setText(testFilterSelectedData.getOperationProcessLocalUpperLimit());
			ref_txtPostOperationValue.setText(testFilterSelectedData.getOperationProcessPostInputValue());
			if (testFilterSelectedData.isOperationProcessPostActive()) {
				ref_cmbBxPostOperationMethod.setDisable(false);
				ref_txtPostOperationValue.setDisable(false);
			} else {
				ref_cmbBxPostOperationMethod.setDisable(true);
				ref_txtPostOperationValue.setDisable(true);
			}

			/*
			 * if(ref_listViewInputProcessList.getItems().size()>0){
			 * String selectedLocalInputValue =
			 * ref_listViewInputProcessList.getItems().get(0);
			 * String selectedMasterOutputValue =
			 * ref_cmbBxOperationCriteriaMasterOutputData.getSelectionModel().
			 * getSelectedItem().toString();
			 * 
			 * boolean localInputAverageType =
			 * getOperationParameterProfileDataList().stream()
			 * .filter(e -> e.getKeyParam().equals(selectedLocalInputValue))
			 * .anyMatch(e -> e.getPopulateType().equals(ConstantReportV2.
			 * POPULATE_DATA_TYPE_ALL_DUT_AVERAGE));
			 * 
			 * boolean masterInputAverageType =
			 * getOperationParameterProfileDataList().stream()
			 * .filter(e -> e.getKeyParam().equals(selectedMasterOutputValue))
			 * .anyMatch(e -> e.getPopulateType().equals(ConstantReportV2.
			 * POPULATE_DATA_TYPE_ALL_DUT_AVERAGE));
			 * 
			 * if(localInputAverageType && masterInputAverageType){
			 * ref_chkBxPopulateComparedMasterResultStatus.setDisable(false);
			 * }else{
			 * ref_chkBxPopulateComparedMasterResultStatus.setDisable(true);
			 * ref_chkBxPopulateComparedMasterResultStatus.setSelected(false);
			 * //ApplicationLauncher.logger.
			 * debug("isOutputProcessPropertyMatching: Operation process: Local Input and Master Output data type not matching with Average. Kindly check the property"
			 * );
			 * }
			 * }
			 */

		}

		ApplicationLauncher.logger.debug("guiRefreshSelectedTestFilterList : getOperationProcessDbIdHashMap: "
				+ getOperationProcessDbIdHashMap());
		ApplicationLauncher.logger
				.debug("guiRefreshSelectedTestFilterList : getRpPrintDbIdHashMap: " + getRpPrintDbIdHashMap());

	}

	private void reOrderSerialNumber(TableView<ReportMeterMetaDataTypeSubModel> tableViewObj) {

		// List<ReportMeterMetaDataTypeSubModel> reportMeterMetaDataTypeSubModel =
		// tableViewObj.getItems();
		int serialNumber = 1;

		for (int i = 0; i < tableViewObj.getItems().size(); i++) {

			ApplicationLauncher.logger
					.debug("reOrderSerialNumber : getDataTypeKey: " + tableViewObj.getItems().get(i).getDataTypeKey());
			ApplicationLauncher.logger.debug("reOrderSerialNumber : serialNumber: " + serialNumber);
			ApplicationLauncher.logger
					.debug("reOrderSerialNumber : getPageNumber: " + tableViewObj.getItems().get(i).getPageNumber());
			tableViewObj.getItems().get(i).setSerialNo(String.valueOf(serialNumber));
			// ApplicationLauncher.logger.debug("reOrderSerialNumber : serialNumber2: " +
			// tableViewObj.getItems().get(i).getSerialNo());
			serialNumber++;
		}

	}

	private void reOrderPsuedoSerialNumber(TableView<ReportMeterMetaDataTypeSubModel> tableViewObj) {

		// List<ReportMeterMetaDataTypeSubModel> reportMeterMetaDataTypeSubModel =
		// tableViewObj.getItems();
		int serialNumber = 1;

		for (int i = 0; i < tableViewObj.getItems().size(); i++) {

			ApplicationLauncher.logger
					.debug("reOrderSerialNumber : getDataTypeKey: " + tableViewObj.getItems().get(i).getDataTypeKey());
			ApplicationLauncher.logger.debug("reOrderSerialNumber : serialNumber: " + serialNumber);
			tableViewObj.getItems().get(i).setPsuedoSerialNo(String.valueOf(serialNumber));
			ApplicationLauncher.logger
					.debug("reOrderSerialNumber : serialNumber2: " + tableViewObj.getItems().get(i).getSerialNo());
			serialNumber++;
		}

	}

	private void reOrderSerialNumberTestFilter(TableView<ReportProfileTestDataFilter> tableViewObj) {

		// List<ReportMeterMetaDataTypeSubModel> reportMeterMetaDataTypeSubModel =
		// tableViewObj.getItems();
		int serialNumber = 1;

		for (int i = 0; i < tableViewObj.getItems().size(); i++) {

			// ApplicationLauncher.logger.debug("reOrderSerialNumberTestFilter :
			// getDataTypeKey: " + tableViewObj.getItems().get(i).getDataTypeKey());
			// ApplicationLauncher.logger.debug("reOrderSerialNumberTestFilter :
			// serialNumber: " + serialNumber);
			tableViewObj.getItems().get(i).setTableSerialNo(String.valueOf(serialNumber));
			// ApplicationLauncher.logger.debug("reOrderSerialNumberTestFilter :
			// serialNumber2: " + tableViewObj.getItems().get(i).getTableSerialNo());
			serialNumber++;
		}

	}

	public OperationProcessJsonReadModel getOperationProcessDataModel() {
		return operationProcessDataModel;
	}

	public void setOperationProcessDataModel(OperationProcessJsonReadModel operationProcessDataModel) {
		this.operationProcessDataModel = operationProcessDataModel;
	}

	@FXML
	public void launchOperationDataSetProfile_GUI_Click() {
		ApplicationLauncher.logger.info("launchOperationDataSetProfile_GUI_Click: Entry");

		Platform.runLater(() -> {

			// guiLaunchTimer = new Timer();
			// guiLaunchTimer.schedule(new StartRunTaskClick(), 50);
			launchOperationParamProfileGui();
		});

	}

	/*
	 * class StartRunTaskClick extends TimerTask {
	 * public void run() {
	 * 
	 * Platform.runLater(() -> {
	 * launchOperationParamProfileGui();
	 * });
	 * guiLaunchTimer.cancel();
	 * 
	 * 
	 * }
	 * }
	 */

	public void launchOperationParamProfileGui() {
		ApplicationLauncher.logger.info("launchOperationParamProfileGui: entry");
		// ApplicationLauncher.logger.info("MeterReadingPopup: entry");
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource("/fxml/reportprofile/OperationParamProfile" + ConstantApp.THEME_FXML));

		// ApplicationLauncher.logger.info("MeterReadingPopup: loading 48 position");
		// loader = new
		// FXMLLoader(getClass().getResource("/fxml/deployment/MeterReadingPopup48" +
		// ConstantApp.THEME_FXML));

		Scene newScene;
		try {
			newScene = new Scene(loader.load());
		} catch (IOException ex) {

			ex.printStackTrace();
			ApplicationLauncher.logger.error("launchOperationParamProfileGui :IOException:" + ex.getMessage());
			return;
		}

		Stage SaveAsStage = new Stage();
		// https://stackoverflow.com/questions/36071779/how-to-open-an-additional-window-in-a-javafx-fxml-app?rq=1

		Stage primaryStage = ApplicationLauncher.getPrimaryStage();

		// SaveAsStage.initModality(Modality.WINDOW_MODAL);
		SaveAsStage.initModality(Modality.APPLICATION_MODAL);
		SaveAsStage.initOwner(primaryStage);
		// https://stackoverflow.com/questions/38481914/disable-background-stage-javafx?rq=1

		SaveAsStage.setScene(newScene);
		// Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		SaveAsStage.getIcons().add(new Image("file:images/" + ConstantVersion.APP_ICON_FILENAME));
		SaveAsStage.setAlwaysOnTop(false);
		// SaveAsStage.setOnCloseRequest(e->e.consume());
		SaveAsStage.setTitle("Operation DataSet Profile");

		try {
			SaveAsStage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("launchOperationParamProfileGui: Exception :" + e.getMessage());

		}

	}

	public static OperationParamService getReportOperationParamService() {
		return reportOperationParamService;
	}

	public static void setReportOperationParamService(OperationParamService reportOperationParamService) {
		ReportProfileConfigController.reportOperationParamService = reportOperationParamService;
	}

	public ArrayList<OperationParam> getOperationParameterProfileDataList() {
		return operationParameterProfileDataList;
	}

	public void setOperationParameterProfileDataList(ArrayList<OperationParam> operationParameterProfileDataList) {
		this.operationParameterProfileDataList = operationParameterProfileDataList;
	}

	@FXML
	public void btnTemplatePathSelectOnClick() {
		ApplicationLauncher.logger.debug("btnTemplatePathSelectOnClick: Entry");
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Select report template file location");
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files", "*.xlsx", "*.xls");// new
																													// FileChooser.ExtensionFilter("TEXT
																													// files
																													// (*.txt)",
																													// "*.txt");

		chooser.getExtensionFilters().add(extFilter);
		File file = chooser.showOpenDialog(new Stage());

		String fileName = file.getName();
		String filePath = file.getParent() + "\\";
		ApplicationLauncher.logger.debug("btnTemplatePathSelectOnClick: file location: " + file);
		ApplicationLauncher.logger.debug("btnTemplatePathSelectOnClick: fileName: " + fileName);
		ApplicationLauncher.logger.debug("btnTemplatePathSelectOnClick: filePath: " + filePath);
		String file_location = file.toString();

		ref_txtTemplateFileNameWithPath.setText(file_location);

	}

	@FXML
	public void btnOutputPathSelectOnClick() {
		ApplicationLauncher.logger.debug("btnOutputPathSelectOnClick: Entry");

		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedDirectory = directoryChooser.showDialog(new Stage());

		if (selectedDirectory == null) {

			ApplicationLauncher.logger.debug("btnOutputPathSelectOnClick: No Directory selected");
			ref_txtOutputPath.setText("");
		} else {

			ApplicationLauncher.logger.debug(
					"btnOutputPathSelectOnClick: showSaveDialog file location: " + selectedDirectory.getAbsolutePath());
			ref_txtOutputPath.setText(selectedDirectory.getAbsolutePath() + "\\");
		}
	}

	public boolean isTestFilterEditMode() {
		return testFilterEditMode;
	}

	public void setTestFilterEditMode(boolean testFilterEditMode) {
		this.testFilterEditMode = testFilterEditMode;
	}

	public HashMap<String, Integer> getOperationProcessDbIdHashMap() {
		return operationProcessDbIdHashMap;
	}

	public void addOperationProcessDbIdHashMap(String processKey, int dbIdValue) {
		this.operationProcessDbIdHashMap.put(processKey, dbIdValue);
	}

	public void clearOperationProcessDbIdHashMap() {
		this.operationProcessDbIdHashMap.clear();
		;
	}

	public void setOperationProcessDbIdHashMap(HashMap<String, Integer> operationProcessDbIdHashMap) {
		this.operationProcessDbIdHashMap = operationProcessDbIdHashMap;
	}

	public HashMap<String, Integer> getRpPrintDbIdHashMap() {
		return rpPrintDbIdHashMap;
	}

	public void addRpPrintDbIdHashMap(String processKey, int dbIdValue) {
		this.rpPrintDbIdHashMap.put(processKey, dbIdValue);
	}

	public void clearRpPrintDbIdHashMap() {
		this.rpPrintDbIdHashMap.clear();
		;
	}

	public void setRpPrintDbIdHashMap(HashMap<String, Integer> operationProcessDbIdHashMap) {
		this.rpPrintDbIdHashMap = operationProcessDbIdHashMap;
	}

}
