package com.tasnetwork.calibration.conveyor;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.bay.calib.Calib;
import com.tasnetwork.calibration.conveyor.bay.calib.CalibrationBayBypass;
import com.tasnetwork.calibration.conveyor.bay.calib.CalibrationBayReset;
import com.tasnetwork.calibration.conveyor.bay.calib.CalibrationBayStop;
import com.tasnetwork.calibration.conveyor.bay.comm.Comm;
import com.tasnetwork.calibration.conveyor.bay.comm.CommBayBypass;
import com.tasnetwork.calibration.conveyor.bay.comm.CommunicationTestBayReset;
import com.tasnetwork.calibration.conveyor.bay.comm.CommunicationTestBayStop;
import com.tasnetwork.calibration.conveyor.bay.ft.Ft;
import com.tasnetwork.calibration.conveyor.bay.ft.FunctionalTestBayBypass;
import com.tasnetwork.calibration.conveyor.bay.ft.FunctionalTestBayReset;
import com.tasnetwork.calibration.conveyor.bay.ft.FunctionalTestBayStop;
import com.tasnetwork.calibration.conveyor.bay.hv.HighVoltageTestBayBypass;
import com.tasnetwork.calibration.conveyor.bay.hv.HighVoltageTestBayReset;
import com.tasnetwork.calibration.conveyor.bay.hv.HighVoltageTestBayStop;
import com.tasnetwork.calibration.conveyor.bay.hv.Hv;
import com.tasnetwork.calibration.conveyor.bay.ir.InsulationResistanceTestBayBypass;
import com.tasnetwork.calibration.conveyor.bay.ir.InsulationResistanceTestBayReset;
import com.tasnetwork.calibration.conveyor.bay.ir.InsulationResistanceTestBayStop;
import com.tasnetwork.calibration.conveyor.bay.ir.Ir;
import com.tasnetwork.calibration.conveyor.bay.rejection.Rejection;
import com.tasnetwork.calibration.conveyor.bay.rejection.RejectionBayReset;
import com.tasnetwork.calibration.conveyor.bay.rejection.RejectionBayStop;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.STA_NoLoadTestBay1Bypass;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.STA_NoLoadTestBay1Reset;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.STA_NoLoadTestBay1Stop;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.StaNld_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.STA_NoLoadTestBay2Bypass;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.STA_NoLoadTestBay2Reset;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.STA_NoLoadTestBay2Stop;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.StaNld_Bay2;
import com.tasnetwork.calibration.conveyor.bay.verific.Verification;
import com.tasnetwork.calibration.conveyor.bay.verific.VerificationTestBayBypass;
import com.tasnetwork.calibration.conveyor.bay.verific.VerificationTestBayReset;
import com.tasnetwork.calibration.conveyor.bay.verific.VerificationTestBayStop;
import com.tasnetwork.calibration.conveyor.bay.verific_waiting.VerificWaiting;
import com.tasnetwork.calibration.conveyor.bay.verific_waiting.WaitingBayReset;
import com.tasnetwork.calibration.conveyor.bay.verific_waiting.WaitingBayStop;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

//import groovyjarjarantlr4.v4.parse.ANTLRParser.v3tokenSpec_return;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class StateExecutorController implements Initializable {
	
	BayUtils bayUtils = new BayUtils();
	private final static Semaphore testStatusDisplaySemaphore = new Semaphore(1);

	public static final Boolean ON = true;
	public static final Boolean OFF  = false;
	
	public static final boolean simulateHappyPath 				= /*false;*/ true;
	public static final boolean simulateFtBayHappyPath 			= false; //true;
	public static final boolean simulateHvBayHappyPath			= false; //true;
	public static final boolean simulateIrBayHappyPath 			= false; //true;
	public static final boolean simulateCalibBayHappyPath 		= false; //true;
	public static final boolean simulateCommBayHappyPath 		= false; //true;
	public static final boolean simulateLoadingBayHappyPath		= false; //true;	
	public static final boolean simulateRejectionBayHappyPath 	= false; //true;		
	public static final boolean simulateSCTNLTBay1HappyPath 	= false; //true;
	public static final boolean simulateSCTNLTBay2HappyPath 	= false; //true;
	public static final boolean simulateUnloadingBayHappyPath 	= false; //true;
	public static final boolean simulateVerificBayHappyPath 	= false; //true;
	public static final boolean simulateWaitingBayHappyPath 	= false; //true;
	
	public static final boolean simulateHvBayResult				= true;
	public static final boolean simulateIrBayResult 			= true;
	
	@FXML private Button btnFtStart;
	@FXML private Button btnFtStop;
	@FXML private Button btnFtReset;
	@FXML private Button btnFtBayBypass;
	@FXML private Button btnHvtStart;
	@FXML private Button btnHvtStop;
	@FXML private Button btnHvtReset;
	@FXML private Button btnHvtBayBypass;
	@FXML private Button btnIrtStart;
	@FXML private Button btnIrtStop;
	@FXML private Button btnIrtReset;
	@FXML private Button btnIrtBayBypass;
	@FXML private Button btnCalibStart;
	@FXML private Button btnCalibStop;
	@FXML private Button btnCalibReset;
	@FXML private Button btnCalibBayBypass;
	@FXML private Button btnWaitingBayStart;
	@FXML private Button btnWaitingBayStop;
	@FXML private Button btnWaitingBayReset;
	@FXML private Button btnWaitingBayBypass;
	@FXML private Button btnVerificTestStart;
	@FXML private Button btnVerificTestStop;
	@FXML private Button btnVerificTestReset;
	@FXML private Button btnVerificTestBayBypass;
	@FXML private Button btnSctNlt1Start;
	@FXML private Button btnSctNlt1Stop;
	@FXML private Button btnSctNlt1Reset;
	@FXML private Button btnSctNlt1BayBypass;
	@FXML private Button btnSctNlt2Start;
	@FXML private Button btnSctNlt2Stop;
	@FXML private Button btnSctNlt2Reset;
	@FXML private Button btnSctNlt2BayBypass;
	@FXML private Button btnCommTestStart;
	@FXML private Button btnCommTestStop;
	@FXML private Button btnCommTestReset;
	@FXML private Button btnCommTestBayBypass;
	@FXML private Button btnRejectStart;
	@FXML private Button btnRejectStop;
	@FXML private Button btnRejectReset;	
	@FXML private Button btnRejectBayBypass;	
	
	// Static references
	public static Button BTN_FT_START;
	public static Button BTN_FT_STOP;
	public static Button BTN_FT_RESET;
	public static Button BTN_FT_BAY_BYPASS;
	public static Button BTN_HVT_START;
	public static Button BTN_HVT_STOP;
	public static Button BTN_HVT_RESET;
	public static Button BTN_HVT_BAY_BYPASS;
	public static Button BTN_IRT_START;
	public static Button BTN_IRT_STOP;
	public static Button BTN_IRT_RESET;
	public static Button BTN_IRT_BAY_BYPASS;
	public static Button BTN_CALIB_START;
	public static Button BTN_CALIB_STOP;
	public static Button BTN_CALIB_RESET;
	public static Button BTN_CALIB_BAY_BYPASS;
	public static Button BTN_VERIFIC_TEST_START;
	public static Button BTN_VERIFIC_TEST_STOP;
	public static Button BTN_VERIFIC_TEST_RESET;
	public static Button BTN_VERIFIC_TEST_BAY_BYPASS;
	public static Button BTN_SCT_NLT1_START;
	public static Button BTN_SCT_NLT1_STOP;
	public static Button BTN_SCT_NLT1_RESET;
	public static Button BTN_SCT_NLT1_BAY_BYPASS;
	public static Button BTN_SCT_NLT2_START;
	public static Button BTN_SCT_NLT2_STOP;
	public static Button BTN_SCT_NLT2_RESET;
	public static Button BTN_SCT_NLT2_BAY_BYPASS;
	public static Button BTN_COMM_TEST_START;
	public static Button BTN_COMM_TEST_STOP;
	public static Button BTN_COMM_TEST_RESET;
	public static Button BTN_COMM_TEST_BAY_BYPASS;
	public static Button BTN_REJECT_START;
	public static Button BTN_REJECT_STOP;
	public static Button BTN_REJECT_RESET;
	public static Button BTN_REJECT_BAY_BYPASS;
	
	// Motor 'Off' buttons
	@FXML private Button btn_offMotor1;
    @FXML private Button btn_offMotor2;
    @FXML private Button btn_offMotor3;
    @FXML private Button btn_offMotor4;
    @FXML private Button btn_offMotor5;
    @FXML private Button btn_offMotor6;
    @FXML private Button btn_offMotor7;
    @FXML private Button btn_offMotor8;
    @FXML private Button btn_offMotor9;

    // Motor 'On' buttons
    @FXML private Button btn_onMotor1;
    @FXML private Button btn_onMotor2;
    @FXML private Button btn_onMotor3;
    @FXML private Button btn_onMotor4;
    @FXML private Button btn_onMotor5;
    @FXML private Button btn_onMotor6;
    @FXML private Button btn_onMotor7;
    @FXML private Button btn_onMotor8;
    @FXML private Button btn_onMotor9;

    // Static references for 'Off' buttons
    @FXML private static Button ref_btn_offMotor1;
    @FXML private static Button ref_btn_offMotor2;
    @FXML private static Button ref_btn_offMotor3;
    @FXML private static Button ref_btn_offMotor4;
    @FXML private static Button ref_btn_offMotor5;
    @FXML private static Button ref_btn_offMotor6;
    @FXML private static Button ref_btn_offMotor7;
    @FXML private static Button ref_btn_offMotor8;
    @FXML private static Button ref_btn_offMotor9;

    // Static references for 'On' buttons
    @FXML private static Button ref_btn_onMotor1;
    @FXML private static Button ref_btn_onMotor2;
    @FXML private static Button ref_btn_onMotor3;
    @FXML private static Button ref_btn_onMotor4;
    @FXML private static Button ref_btn_onMotor5;
    @FXML private static Button ref_btn_onMotor6;
    @FXML private static Button ref_btn_onMotor7;
    @FXML private static Button ref_btn_onMotor8;
    @FXML private static Button ref_btn_onMotor9;
	
    @FXML private Button btn_CalibPlace;
    private static Button ref_btn_CalibPlace;
    
    @FXML private Button btn_CalibRemove;
    private static Button ref_btn_CalibRemove;
    
    @FXML private Button btn_CalibCurrentStable;
    private static Button ref_btn_CalibCurrentStable;
    
    @FXML private Button btn_FtPlace;
    private static Button ref_btn_FtPlace;
    
    @FXML private Button btn_FtRemove;	
    private static Button ref_btn_FtRemove;
    
    @FXML private Button btn_Ft_LDUOK;
    private static Button ref_btn_Ft_LDUOK;
    
    @FXML private Button btn_VerificDone;
    private static Button ref_btn_VerificDone;
    

	@FXML
	private TableView<TestInterfaceStatus> tvTestStatus;

	@FXML
	private TableColumn<TestInterfaceStatus, String> colTsSerialNo;

	@FXML
	private TableColumn<TestInterfaceStatus, String> colTsBayName;

	@FXML
	private TableColumn<TestInterfaceStatus, String> colTsStateName;

	@FXML
	private TableColumn<TestInterfaceStatus, String> colTsDeviceType;

	@FXML
	private TableColumn<TestInterfaceStatus, String> colTsPathNo;

	@FXML
	private TableColumn<TestInterfaceStatus, String> colTsPositionNo;

	@FXML
	private TableColumn<TestInterfaceStatus, String> colTsCname;

	@FXML
	private TableColumn<TestInterfaceStatus, String> colTsPortName;

	@FXML
	private TableColumn<TestInterfaceStatus, String> colTsSerialStatus;

	@FXML
	private TableColumn<TestInterfaceStatus, String> colTsQrResponse;

	@FXML
	private TableColumn<TestInterfaceStatus, String> colTsQrData;

	@FXML
	private TableColumn<TestInterfaceStatus, String> colTsStatus;

	@FXML
	private Button btnFilter;

    @FXML
    private TextField tf_CALIB_prompt;
    public static TextField ref_tf_CALIB_prompt;

    @FXML
    private TextField tf_FT_prompt;
    public static TextField ref_tf_FT_prompt;
    
    @FXML
    private TextField tf_VERIFIC_prompt;
    public static TextField ref_tf_VERIFIC_prompt;
    
	private static Button ref_btnFilter;

	@FXML
	private ComboBox cmbBxFilterPosition;
	private static ComboBox ref_cmbBxFilterPosition;

	private static TableView<TestInterfaceStatus> ref_tvTestStatus;
	private static TableColumn<TestInterfaceStatus, String> ref_colTsSerialNo;
	private static TableColumn<TestInterfaceStatus, String> ref_colTsBayName;
	private static TableColumn<TestInterfaceStatus, String> ref_colTsStateName;

	private static TableColumn<TestInterfaceStatus, String> ref_colTsDeviceType;
	private static TableColumn<TestInterfaceStatus, String> ref_colTsPathNo;
	private static TableColumn<TestInterfaceStatus, String> ref_colTsPositionNo;
	private static TableColumn<TestInterfaceStatus, String> ref_colTsCname;
	private static TableColumn<TestInterfaceStatus, String> ref_colTsPortName;

	private static TableColumn<TestInterfaceStatus, String> ref_colTsSerialStatus;

	private static TableColumn<TestInterfaceStatus, String> ref_colTsQrResponse;

	private static TableColumn<TestInterfaceStatus, String> ref_colTsQrData;

	private static TableColumn<TestInterfaceStatus, String> ref_colTsStatus;

	public static ArrayList<TestInterfaceStatus> allData = new ArrayList<TestInterfaceStatus>();

	static AtomicInteger serialNoTestStatusAtomic = new AtomicInteger(1);

	Timer funtionalBayStartTaskTimer;
	Timer calibrationStartTaskTimer;
	Timer insResStartTaskTimer;
	Timer hvtBayStartTaskTimer;
	Timer verificStartTaskTimer;
	Timer commStartTaskTimer;
	Timer sctNlt1StartTaskTimer;
	Timer sctNlt2StartTaskTimer;
	Timer waitingBayStartTaskTimer;
	Timer rejectionBayStartTaskTimer;

	Timer funtionalBayStopTaskTimer;
	Timer calibrationStopTaskTimer;
	Timer insResStopTaskTimer;
	Timer hvtBayStopTaskTimer;
	Timer verificStopTaskTimer ;
	Timer commStopTaskTimer ;
	Timer sctNlt2StopTaskTimer ;
	Timer sctNlt1StopTaskTimer ;
	Timer waitingBayStopTaskTimer;
	Timer rejectionBayStopTaskTimer;

	Timer funtionalBayResetTaskTimer;
	Timer calibrationResetTaskTimer;
	Timer insResResetTaskTimer;
	Timer hvtBayResetTaskTimer;
	Timer verificResetTaskTimer ; 
	Timer commResetTaskTimer ;
	Timer sctNlt1ResetTaskTimer ;
	Timer sctNlt2ResetTaskTimer;
	Timer waitingBayResetTaskTimer;
	Timer rejectionBayResetTaskTimer;
	
	Timer funtionalBayBypassTaskTimer;
	Timer hvtBayBypassTaskTimer; // Added for HV Bay Bypass
	Timer irtBayBypassTaskTimer; // Added for IR Bay Bypass
	Timer calibBayBypassTaskTimer; // Added for Calib Bay Bypass
	Timer verificTestBayBypassTaskTimer; // Added for Verification Bay Bypass
	Timer sctNlt1BayBypassTaskTimer; // Added for SCT NLT1 Bay Bypass
	Timer sctNlt2BayBypassTaskTimer; // Added for SCT NLT2 Bay Bypass
	Timer commTestBayBypassTaskTimer; // Added for Comm Test Bay Bypass
	Timer rejectBayBypassTaskTimer; // Added for Rejection Bay Bypass
	Timer waitingBayBypassTaskTimer; // Added for Waiting Bay Bypass


	//Timer btnRefreshTaskTimer;
	Timer btnFilterTaskTimer;
	Timer btnSampleDataTaskTimer;

	@FXML private Button btnAllStart;
	@FXML private Button btnAllStop;
	@FXML private Button btnAllReset;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		refInit();
		guiInit();
		dataSetupInit();
	}

	public void dataSetupInit() {
		// TODO Auto-generated method stub

	}

	public void guiInit() {
		// TODO Auto-generated method stub
		//T E S T  S T A T U S - Column Values
		ref_colTsSerialNo.setCellValueFactory(cellData -> cellData.getValue().getSerialNoProperty());
		ref_colTsBayName.setCellValueFactory(cellData -> cellData.getValue().getBayNameProperty());
		ref_colTsStateName.setCellValueFactory(cellData -> cellData.getValue().getStateNameProperty());
		ref_colTsDeviceType.setCellValueFactory(cellData -> cellData.getValue().getDeviceTypeProperty());
		ref_colTsPathNo.setCellValueFactory(cellData -> cellData.getValue().getPathNoProperty());
		ref_colTsPositionNo.setCellValueFactory(cellData -> cellData.getValue().getPositionNoProperty());
		ref_colTsCname.setCellValueFactory(cellData -> cellData.getValue().getcNameProperty());
		ref_colTsPortName.setCellValueFactory(cellData -> cellData.getValue().getPortNameProperty());
		ref_colTsSerialStatus.setCellValueFactory(cellData -> cellData.getValue().getSerialStatusProperty());
		ref_colTsQrResponse.setCellValueFactory(cellData -> cellData.getValue().getDeviceResponseStatusProperty());
		ref_colTsQrData.setCellValueFactory(cellData -> cellData.getValue().getDeviceResponseDataProperty());
		ref_colTsStatus.setCellValueFactory(cellData -> cellData.getValue().getTestStatusProperty());
		
		/*// DISABLE ALL STOP BUTTONS
		btnFtStop.setDisable(true);
		btnHvtStop.setDisable(true);
		btnIrtStop.setDisable(true);
		btnCalibStop.setDisable(true);
		btnVerificTestStop.setDisable(true);
		btnSctNlt1Stop.setDisable(true);
		btnSctNlt2Stop.setDisable(true);
		btnCommTestStop.setDisable(true);*/

		// F I L T E R  C O M B O  B O X ====================================================================
		ref_cmbBxFilterPosition.getItems().clear();
		ref_cmbBxFilterPosition.getItems().add("All");
		for (int i = 0; i <= 6; i ++) {
			ref_cmbBxFilterPosition.getItems().add(i);
		}
		
		/*ref_btn_CalibPlace  .setDisable(true);
		ref_btn_CalibRemove .setDisable(true);
		ref_btn_FtPlace     .setDisable(true); 
		ref_btn_FtRemove    .setDisable(true);*/
		
		btn_CalibPlace  		.setDisable(true);
		btn_CalibRemove 		.setDisable(true);
		btn_CalibCurrentStable 	.setDisable(true);
		btn_FtPlace     		.setDisable(true); 
		btn_FtRemove    		.setDisable(true);
		btn_Ft_LDUOK			.setDisable(true);
		btn_VerificDone			.setDisable(true);
		
	}

	public void refInit() {
		// TODO Auto-generated method stub

		ref_tvTestStatus = tvTestStatus;
		ref_colTsSerialNo = colTsSerialNo;
		ref_colTsBayName = colTsBayName;
		ref_colTsStateName = colTsStateName;
		ref_colTsDeviceType = colTsDeviceType;
		ref_colTsPathNo = colTsPathNo;
		ref_colTsPositionNo = colTsPositionNo;
		ref_colTsCname = colTsCname;
		ref_colTsPortName = colTsPortName;

		ref_colTsSerialStatus = colTsSerialStatus;
		ref_colTsQrResponse = colTsQrResponse;
		ref_colTsQrData = colTsQrData;
		ref_colTsStatus = colTsStatus;

		ref_btnFilter = btnFilter;
		
		ref_tf_CALIB_prompt = tf_CALIB_prompt;
		ref_tf_FT_prompt = tf_FT_prompt;
		ref_tf_VERIFIC_prompt = tf_VERIFIC_prompt;

		ref_cmbBxFilterPosition = cmbBxFilterPosition;
		
		ref_btn_CalibPlace  		= btn_CalibPlace 		;
		ref_btn_CalibRemove 		= btn_CalibRemove		;
		ref_btn_CalibCurrentStable  = btn_CalibCurrentStable;
		ref_btn_FtPlace     		= btn_FtPlace    		;
		ref_btn_FtRemove    		= btn_FtRemove   		;
		ref_btn_Ft_LDUOK			= btn_Ft_LDUOK			;
		ref_btn_VerificDone			= btn_VerificDone		;
		
		BTN_FT_START = btnFtStart;
	    BTN_FT_STOP = btnFtStop;
	    BTN_FT_RESET = btnFtReset;
	    BTN_FT_BAY_BYPASS = btnFtBayBypass;

	    BTN_HVT_START = btnHvtStart;
	    BTN_HVT_STOP = btnHvtStop;
	    BTN_HVT_RESET = btnHvtReset;
		BTN_HVT_BAY_BYPASS = btnHvtBayBypass; // Added static reference

	    BTN_IRT_START = btnIrtStart;
	    BTN_IRT_STOP = btnIrtStop;
	    BTN_IRT_RESET = btnIrtReset;
		BTN_IRT_BAY_BYPASS = btnIrtBayBypass; // Added static reference

	    BTN_CALIB_START = btnCalibStart;
	    BTN_CALIB_STOP = btnCalibStop;
	    BTN_CALIB_RESET = btnCalibReset;
		BTN_CALIB_BAY_BYPASS = btnCalibBayBypass; // Added static reference

	    BTN_VERIFIC_TEST_START = btnVerificTestStart;
	    BTN_VERIFIC_TEST_STOP = btnVerificTestStop;
	    BTN_VERIFIC_TEST_RESET = btnVerificTestReset;
		BTN_VERIFIC_TEST_BAY_BYPASS = btnVerificTestBayBypass; // Added static reference

	    BTN_SCT_NLT1_START = btnSctNlt1Start;
	    BTN_SCT_NLT1_STOP = btnSctNlt1Stop;
	    BTN_SCT_NLT1_RESET = btnSctNlt1Reset;
		BTN_SCT_NLT1_BAY_BYPASS = btnSctNlt1BayBypass; // Added static reference

	    BTN_SCT_NLT2_START = btnSctNlt2Start;
	    BTN_SCT_NLT2_STOP = btnSctNlt2Stop;
	    BTN_SCT_NLT2_RESET = btnSctNlt2Reset;
		BTN_SCT_NLT2_BAY_BYPASS = btnSctNlt2BayBypass; // Added static reference

	    BTN_COMM_TEST_START = btnCommTestStart;
	    BTN_COMM_TEST_STOP = btnCommTestStop;
	    BTN_COMM_TEST_RESET = btnCommTestReset;
		BTN_COMM_TEST_BAY_BYPASS = btnCommTestBayBypass; // Added static reference

		BTN_REJECT_START = btnRejectStart;
		BTN_REJECT_STOP = btnRejectStop;
		BTN_REJECT_RESET = btnRejectReset;
		BTN_REJECT_BAY_BYPASS = btnRejectBayBypass; // Added static reference
	    
	    ref_btn_offMotor1 = btn_offMotor1;
        ref_btn_offMotor2 = btn_offMotor2;
        ref_btn_offMotor3 = btn_offMotor3;
        ref_btn_offMotor4 = btn_offMotor4;
        ref_btn_offMotor5 = btn_offMotor5;
        ref_btn_offMotor6 = btn_offMotor6;
        ref_btn_offMotor7 = btn_offMotor7;
        ref_btn_offMotor8 = btn_offMotor8;
        ref_btn_offMotor9 = btn_offMotor9;

        ref_btn_onMotor1 = btn_onMotor1;
        ref_btn_onMotor2 = btn_onMotor2;
        ref_btn_onMotor3 = btn_onMotor3;
        ref_btn_onMotor4 = btn_onMotor4;
        ref_btn_onMotor5 = btn_onMotor5;
        ref_btn_onMotor6 = btn_onMotor6;
        ref_btn_onMotor7 = btn_onMotor7;
        ref_btn_onMotor8 = btn_onMotor8;
        ref_btn_onMotor9 = btn_onMotor9;
	}
	
	//============================================================================================================================================
	
	@FXML
	public void btnRjStartOnClick() {
	    Ft.logger.info("btnRjStartOnClick : Invoked:");

	    ConstantConveyor.ALL_LOOP_BREAK_FLAG = false;
	    
	    // F L A G S
	    Rejection.setStartProcessRequestedRejectionBay(true);

	    Rejection.setStopProcessCompletedRejectionBay(false);
	    Rejection.setStopProcessRequestedRejectionBay(false);

	    Rejection.setResetProcessCompletedRejectionBay(false);
	    Rejection.setResetProcessRequestedRejectionBay(false);

	    // B U T T O N  I N T E R L O C K
	    btnRejectStart.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnRejectStart.setDisable(true);

	    btnRejectStop.setStyle(""); // Enabled - Default
	    btnRejectStop.setDisable(false);

	    btnRejectReset.setStyle(""); // Enabled - Default
	    btnRejectReset.setDisable(false);

	    // L O G I C
	    ref_tvTestStatus.getItems().clear();
	    allData.clear();

	    rejectionBayStartTaskTimer = new Timer();
	    rejectionBayStartTaskTimer.schedule(new Rejection(), 100);
	    Sleep(500);
	    rejectionBayStartTaskTimer.cancel();

	    Rejection.logger.info("btnFtStartOnClick : Exit:");
	}
	
	@FXML
	public void btnRjStopOnClick() {
		Rejection.logger.info("btnRjStopOnClick : Invoked:");

	    // F L A G S
		Rejection.abort_Rejection_Bay = true;

		Rejection.setStartProcessRequestedRejectionBay(false);

		Rejection.setStopProcessCompletedRejectionBay(false);
	    Rejection.setStopProcessRequestedRejectionBay(true);

	    Rejection.setResetProcessCompletedRejectionBay(false);
	    Rejection.setResetProcessRequestedRejectionBay(false);

	    // B U T T O N  I N T E R L O C K
	    btnRejectStop.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnRejectStop.setDisable(true);

	    btnRejectStart.setStyle(""); // Enabled - Default
	    btnRejectStop.setDisable(false);

	    btnRejectReset.setStyle(""); // Enabled - Default
	    btnRejectReset.setDisable(false);
	    
	    btnRejectBayBypass.setStyle(""); // Enabled - Default
	    btnRejectBayBypass.setDisable(false);

	    // L O G I C
	    rejectionBayStopTaskTimer = new Timer();
	    rejectionBayStopTaskTimer.schedule(new RejectionBayStop(), 100);
	    Sleep(500);
	    rejectionBayStopTaskTimer.cancel();

	    Rejection.logger.info("btnRjStopOnClick : Exit:");
	}

	@FXML
	public void btnRjResetOnClick() {
		Rejection.logger.info("btnRjResetOnClick : Invoked:");

	    
	    
	    // F L A G S
	    Rejection.setStartProcessRequestedRejectionBay(false);

	    Rejection.setStopProcessCompletedRejectionBay(false);
	    Rejection.setStopProcessRequestedRejectionBay(false);

	    Rejection.setResetProcessCompletedRejectionBay(false);
	    Rejection.setResetProcessRequestedRejectionBay(true);

	    // B U T T O N  I N T E R L O C K
	    btnRejectReset.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnRejectReset.setDisable(true);

	    btnRejectStart.setStyle(""); // Enabled - Default
	    btnRejectStart.setDisable(false);

	    btnRejectStop.setStyle(""); // Enabled - Default
	    btnRejectStop.setDisable(false);

	    // L O G I C
	    rejectionBayResetTaskTimer = new Timer();
	    rejectionBayResetTaskTimer.schedule(new RejectionBayReset(), 100);
	    Sleep(500);
	    rejectionBayResetTaskTimer.cancel();

	    Rejection.logger.info("btnRjResetOnClick : Exit:");
	}

	@FXML
	public void btnRejectBayBypassOnClick() {
		Rejection.logger.info("btnRejectBayBypassOnClick : Invoked:");
	    
	    // F L A G S
		Rejection.setStartProcessRequestedRejectionBay(true);

		Rejection.setStopProcessCompletedRejectionBay(false);
		Rejection.setStopProcessRequestedRejectionBay(false);

		Rejection.setResetProcessCompletedRejectionBay(false);
		Rejection.setResetProcessRequestedRejectionBay(false);

	    // B U T T O N  I N T E R L O C K
	    btnRejectBayBypass.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnRejectBayBypass.setDisable(true);
	    
	    btnRejectReset.setStyle(""); // Enabled - Default
	    btnRejectReset.setDisable(false);

	    btnRejectStart.setStyle(""); // Enabled - Default
	    btnRejectStart.setDisable(false);

	    btnRejectStop.setStyle(""); // Enabled - Default
	    btnRejectStop.setDisable(false);

	    // L O G I C
	    rejectBayBypassTaskTimer = new Timer();
	    // Assuming a RejectionBayBypass class exists or will be created
	    rejectBayBypassTaskTimer.schedule(new TimerTask() { @Override public void run() { Rejection.logger.info("RejectionBayBypass task executed."); /* Add actual bypass logic here */ } }, 100);
	    Sleep(500);
	    rejectBayBypassTaskTimer.cancel();

	    Rejection.logger.info("btnRejectBayBypassOnClick : Exit:");
	}
	
	//============================================================================================================================================  

	@FXML
	public void btnFtStartOnClick() {
	    Ft.logger.info("btnFtStartOnClick : Invoked:");

	    ConstantConveyor.ALL_LOOP_BREAK_FLAG = false;
	    
	    // F L A G S
	    Ft.setStartProcessRequestedFtBay(true);
	    Ft.setStopProcessCompletedFtBay(false);
	    Ft.setStopProcessRequestedFtBay(false);
	    Ft.setResetProcessCompletedFtBay(false);
	    Ft.setResetProcessRequestedFtBay(false);

	    // B U T T O N  I N T E R L O C K
	    btnFtStart.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnFtStart.setDisable(true);

	    btnFtStop.setStyle(""); // Enabled - Default
	    btnFtStop.setDisable(false);

	    btnFtReset.setStyle(""); // Enabled - Default
	    btnFtReset.setDisable(false);

	    // L O G I C
	    ref_tvTestStatus.getItems().clear();
	    allData.clear();

	    funtionalBayStartTaskTimer = new Timer();
	    funtionalBayStartTaskTimer.schedule(new Ft(), 100);
	    Sleep(500);
	    funtionalBayStartTaskTimer.cancel();

	    Ft.logger.info("btnFtStartOnClick : Exit:");
	}

	@FXML
	public void btnFtStopOnClick() {
	    Ft.logger.info("btnFtStopOnClick : Invoked:");

	    
	    
	    // F L A G S
	    Ft.abort_FT_Bay = true;

	    Ft.setStartProcessRequestedFtBay(false);

	    Ft.setStopProcessCompletedFtBay(false);
	    Ft.setStopProcessRequestedFtBay(true);

	    Ft.setResetProcessCompletedFtBay(false);
	    Ft.setResetProcessRequestedFtBay(false);

	    // B U T T O N  I N T E R L O C K
	    btnFtStop.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnFtStop.setDisable(true);

	    btnFtStart.setStyle(""); // Enabled - Default
	    btnFtStart.setDisable(false);

	    btnFtReset.setStyle(""); // Enabled - Default
	    btnFtReset.setDisable(false);
	    
	    btnFtBayBypass.setStyle(""); // Enabled - Default
	    btnFtBayBypass.setDisable(false);

	    // L O G I C
	    funtionalBayStopTaskTimer = new Timer();
	    funtionalBayStopTaskTimer.schedule(new FunctionalTestBayStop(), 100);
	    Sleep(500);
	    funtionalBayStopTaskTimer.cancel();

	    Ft.logger.info("btnFtStopOnClick : Exit:");
	}

	@FXML
	public void btnFtResetOnClick() {
	    Ft.logger.info("btnFtResetOnClick : Invoked:");

	    
	    
	    // F L A G S
	    Ft.setStartProcessRequestedFtBay(false);

	    Ft.setStopProcessCompletedFtBay(false);
	    Ft.setStopProcessRequestedFtBay(false);

	    Ft.setResetProcessCompletedFtBay(false);
	    Ft.setResetProcessRequestedFtBay(true);

	    // B U T T O N  I N T E R L O C K
	    btnFtReset.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnFtReset.setDisable(true);

	    btnFtStart.setStyle(""); // Enabled - Default
	    btnFtStart.setDisable(false);

	    btnFtStop.setStyle(""); // Enabled - Default
	    btnFtStop.setDisable(false);

	    // L O G I C
	    funtionalBayResetTaskTimer = new Timer();
	    funtionalBayResetTaskTimer.schedule(new FunctionalTestBayReset(), 100);
	    Sleep(500);
	    funtionalBayResetTaskTimer.cancel();

	    Ft.logger.info("btnFtResetOnClick : Exit:");
	}
	
	@FXML
	public void btnFtBayBypassOnClick() {
	    Ft.logger.info("btnFtBayBypassOnClick : Invoked:");
	    
	    // F L A G S
	    Ft.setStartProcessRequestedFtBay(true);

	    Ft.setStopProcessCompletedFtBay(false);
	    Ft.setStopProcessRequestedFtBay(false);

	    Ft.setResetProcessCompletedFtBay(false);
	    Ft.setResetProcessRequestedFtBay(false);

	    // B U T T O N  I N T E R L O C K
	    btnFtBayBypass.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnFtBayBypass.setDisable(true);
	    
	    btnFtReset.setStyle(""); // Enabled - Default
	    btnFtReset.setDisable(false);

	    btnFtStart.setStyle(""); // Enabled - Default
	    btnFtStart.setDisable(false);

	    btnFtStop.setStyle(""); // Enabled - Default
	    btnFtStop.setDisable(false);

	    // L O G I C
	    funtionalBayBypassTaskTimer = new Timer();
	    funtionalBayBypassTaskTimer.schedule(new FunctionalTestBayBypass(), 100);
	    Sleep(500);
	    funtionalBayBypassTaskTimer.cancel();

	    Ft.logger.info("btnFtBayBypassOnClick : Exit:");
	}


	//============================================================================================================================================  

	@FXML
	public void btnHvtStartOnClick() {
		Hv.logger.info("btnHvtStartOnClick : Invoked:");

		ConstantConveyor.ALL_LOOP_BREAK_FLAG = false;
		
	    // F L A G S
	    Hv.setStartProcessRequestedHvtBay(true);

	    Hv.setStopProcessCompletedHvtBay(false);
	    Hv.setStopProcessRequestedHvtBay(false);

	    Hv.setResetProcessCompletedHvtBay(false);
	    Hv.setResetProcessRequestedHvtBay(false);

	    // B U T T O N  I N T E R L O C K
	    btnHvtStart.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnHvtStart.setDisable(true);

	    btnHvtStop.setStyle(""); // Enabled - Default
	    btnHvtStop.setDisable(false);

	    btnHvtReset.setStyle(""); // Enabled - Default
	    btnHvtReset.setDisable(false);

	    // L O G I C
	    ref_tvTestStatus.getItems().clear();
	    allData.clear();
	    
	    hvtBayStartTaskTimer = new Timer();
	    hvtBayStartTaskTimer.schedule(new Hv(), 100);
	    Sleep(500);
	    hvtBayStartTaskTimer.cancel();

	    Hv.logger.info("btnHvtStartOnClick : Exit:");
	}

	@FXML
	public void btnHvtStopOnClick() {
		Hv.logger.info("btnHvtStopOnClick : Invoked:");

		
		
	    // F L A G S
	    Hv.abort_HVT_Bay = true;

	    Hv.setStartProcessRequestedHvtBay(false);

	    Hv.setStopProcessCompletedHvtBay(false);
	    Hv.setStopProcessRequestedHvtBay(true);
	    
		Hv.logger.info("StopProcessRequestedHvtBay :" + Hv.isStopProcessRequestedHvtBay());


	    Hv.setResetProcessCompletedHvtBay(false);
	    Hv.setResetProcessRequestedHvtBay(false);

	    // B U T T O N  I N T E R L O C K
	    btnHvtStop.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnHvtStop.setDisable(true);

	    btnHvtStart.setStyle(""); // Enabled - Default
	    btnHvtStart.setDisable(false);

	    btnHvtReset.setStyle(""); // Enabled - Default
	    btnHvtReset.setDisable(false);
	    
	    btnHvtBayBypass.setStyle(""); // Enabled - Default
	    btnHvtBayBypass.setDisable(false);

	    // L O G I C
	    hvtBayStopTaskTimer = new Timer();
	    hvtBayStopTaskTimer.schedule(new HighVoltageTestBayStop(), 100);
	    Sleep(500);
	    hvtBayStopTaskTimer.cancel();

	    Hv.logger.info("btnHvtStopOnClick : Exit:");
	}

	@FXML
	public void btnHvtResetOnClick() {
		Hv.logger.info("btnHvtResetOnClick : Invoked:");

		
		
	    // F L A G S
	    Hv.setStartProcessRequestedHvtBay(false);

	    Hv.setStopProcessCompletedHvtBay(false);
	    Hv.setStopProcessRequestedHvtBay(false);

	    Hv.setResetProcessCompletedHvtBay(false);
	    Hv.setResetProcessRequestedHvtBay(true);

	    // B U T T O N  I N T E R L O C K
	    btnHvtReset.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnHvtReset.setDisable(true);

	    btnHvtStart.setStyle(""); // Enabled - Default
	    btnHvtStart.setDisable(false);

	    btnHvtStop.setStyle(""); // Enabled - Default
	    btnHvtStop.setDisable(false);

	    // L O G I C
	    hvtBayResetTaskTimer = new Timer();
	    hvtBayResetTaskTimer.schedule(new HighVoltageTestBayReset(), 100);
	    Sleep(500);
	    hvtBayResetTaskTimer.cancel();

	    Hv.logger.info("btnHvtResetOnClick : Exit:");
	}

	@FXML
	public void btnHvtBayBypassOnClick() {
		Hv.logger.info("btnHvtBayBypassOnClick : Invoked:");
	    
	    // F L A G S
	    Hv.setStartProcessRequestedHvtBay(true);

	    Hv.setStopProcessCompletedHvtBay(false);
	    Hv.setStopProcessRequestedHvtBay(false);

	    Hv.setResetProcessCompletedHvtBay(false);
	    Hv.setResetProcessRequestedHvtBay(false);

	    // B U T T O N  I N T E R L O C K
	    btnHvtBayBypass.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnHvtBayBypass.setDisable(true);
	    
	    btnHvtReset.setStyle(""); // Enabled - Default
	    btnHvtReset.setDisable(false);

	    btnHvtStart.setStyle(""); // Enabled - Default
	    btnHvtStart.setDisable(false);

	    btnHvtStop.setStyle(""); // Enabled - Default
	    btnHvtStop.setDisable(false);

	    // L O G I C
	    hvtBayBypassTaskTimer = new Timer();
	    // Assuming a HighVoltageTestBayBypass class exists or will be created
	    hvtBayBypassTaskTimer.schedule(new HighVoltageTestBayBypass(), 100);
	    Sleep(500);
	    hvtBayBypassTaskTimer.cancel();

	    Hv.logger.info("btnHvtBayBypassOnClick : Exit:");
	}


	//============================================================================================================================================  

	@FXML
	public void btnIrtStartOnClick() {
		Ir.logger.info("btnIrtStartOnClick : Invoked:");

		ConstantConveyor.ALL_LOOP_BREAK_FLAG = false;

	    // F L A G S
	    Ir.setStartProcessRequestedIrtBay(true);

	    Ir.setStopProcessCompletedIrtBay(false);
	    Ir.setStopProcessRequestedIrtBay(false);

	    Ir.setResetProcessCompletedIrtBay(false);
	    Ir.setResetProcessRequestedIrtBay(false);

	    // B U T T O N  I N T E R L O C K
	    btnIrtStart.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnIrtStart.setDisable(true);

	    btnIrtStop.setStyle(""); // Enabled - Default
	    btnIrtStop.setDisable(false);

	    btnIrtReset.setStyle(""); // Enabled - Default
	    btnIrtReset.setDisable(false);

	    // L O G I C
	    ref_tvTestStatus.getItems().clear();
	    allData.clear();
	    
	    insResStartTaskTimer = new Timer();
	    insResStartTaskTimer.schedule(new Ir(), 100);
	    Sleep(500);
	    insResStartTaskTimer.cancel();

	    Ir.logger.info("btnIrtStartOnClick : Exit:");
	}

	@FXML
	public void btnIrtStopOnClick() {
		Ir.logger.info("btnIrtStopOnClick : Invoked:");

		
		
	    // F L A G S
	    Ir.abort_IRT_Bay = true;

	    Ir.setStartProcessRequestedIrtBay(false);

	    Ir.setStopProcessCompletedIrtBay(false);
	    Ir.setStopProcessRequestedIrtBay(true);

	    Ir.setResetProcessCompletedIrtBay(false);
	    Ir.setResetProcessRequestedIrtBay(false);

	    // B U T T O N  I N T E R L O C K
	    btnIrtStop.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnIrtStop.setDisable(true);

	    btnIrtStart.setStyle(""); // Enabled - Default
	    btnIrtStart.setDisable(false);

	    btnIrtReset.setStyle(""); // Enabled - Default
	    btnIrtReset.setDisable(false);
	    
	    btnIrtBayBypass.setStyle(""); // Enabled - Default
	    btnIrtBayBypass.setDisable(false);

	    // L O G I C
	    insResStopTaskTimer = new Timer();
	    insResStopTaskTimer.schedule(new InsulationResistanceTestBayStop(), 100);
	    Sleep(500);
	    insResStopTaskTimer.cancel();

	    Ir.logger.info("btnIrtStopOnClick : Exit:");
	}

	@FXML
	public void btnIrtResetOnClick() {
		Ir.logger.info("btnIrtResetOnClick : Invoked:");
		
		
		
	    // F L A G S
	    Ir.setStartProcessRequestedIrtBay(false);

	    Ir.setStopProcessCompletedIrtBay(false);
	    Ir.setStopProcessRequestedIrtBay(false);

	    Ir.setResetProcessCompletedIrtBay(false);
	    Ir.setResetProcessRequestedIrtBay(true);

	    // B U T T O N  I N T E R L O C K
	    btnIrtReset.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnIrtReset.setDisable(true);

	    btnIrtStart.setStyle(""); // Enabled - Default
	    btnIrtStart.setDisable(false);

	    btnIrtStop.setStyle(""); // Enabled - Default
	    btnIrtStop.setDisable(false);

	    // L O G I C
	    insResResetTaskTimer = new Timer();
	    insResResetTaskTimer.schedule(new InsulationResistanceTestBayReset(), 100);
	    Sleep(500);
	    insResResetTaskTimer.cancel();

	    Ir.logger.info("btnIrtResetOnClick : Exit:");
	}

	@FXML
	public void btnIrtBayBypassOnClick() {
		Ir.logger.info("btnIrtBayBypassOnClick : Invoked:");
	    
	    // F L A G S
	    Ir.setStartProcessRequestedIrtBay(true);

	    Ir.setStopProcessCompletedIrtBay(false);
	    Ir.setStopProcessRequestedIrtBay(false);

	    Ir.setResetProcessCompletedIrtBay(false);
	    Ir.setResetProcessRequestedIrtBay(false);

	    // B U T T O N  I N T E R L O C K
	    btnIrtBayBypass.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnIrtBayBypass.setDisable(true);
	    
	    btnIrtReset.setStyle(""); // Enabled - Default
	    btnIrtReset.setDisable(false);

	    btnIrtStart.setStyle(""); // Enabled - Default
	    btnIrtStart.setDisable(false);

	    btnIrtStop.setStyle(""); // Enabled - Default
	    btnIrtStop.setDisable(false);

	    // L O G I C
	    irtBayBypassTaskTimer = new Timer();
	    // Assuming an InsulationResistanceTestBayBypass class exists or will be created
	    irtBayBypassTaskTimer.schedule(new InsulationResistanceTestBayBypass(), 100);
	    Sleep(500);
	    irtBayBypassTaskTimer.cancel();

	    Ir.logger.info("btnIrtBayBypassOnClick : Exit:");
	}
	
	//============================================================================================================================================  

	@FXML
	public void btnCalibStartOnClick() {
	    Ft.logger.info("btnCalibStartOnClick : Invoked:");

		ConstantConveyor.ALL_LOOP_BREAK_FLAG = false;

	    // F L A G S
	    Calib.setStartProcessRequestedCalibBay(true);

	    Calib.setStopProcessCompletedCalibBay(false);
	    Calib.setStopProcessRequestedCalibBay(false);

	    Calib.setResetProcessCompletedCalibBay(false);
	    Calib.setResetProcessRequestedCalibBay(false);

	    // B U T T O N  I N T E R L O C K
	    btnCalibStart.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnCalibStart.setDisable(true);

	    btnCalibStop.setStyle(""); // Enabled - Default
	    btnCalibStop.setDisable(false);

	    btnCalibReset.setStyle(""); // Enabled - Default
	    btnCalibReset.setDisable(false);

	    // L O G I C
	    ref_tvTestStatus.getItems().clear();
	    allData.clear();
	    
	    calibrationStartTaskTimer = new Timer();
	    calibrationStartTaskTimer.schedule(new Calib(), 100);
	    Sleep(500);
	    calibrationStartTaskTimer.cancel();

	    Calib.logger.info("btnCalibStartOnClick : Exit:");
	}

	@FXML
	public void btnCalibStopOnClick() {
		Calib.logger.info("btnCalibStopOnClick : Invoked:");

		

	    // F L A G S
	    Calib.abort_Calib_Bay = true;

	    Calib.setStartProcessRequestedCalibBay(false);

	    Calib.setStopProcessCompletedCalibBay(false);
	    Calib.setStopProcessRequestedCalibBay(true);

	    Calib.setResetProcessCompletedCalibBay(false);
	    Calib.setResetProcessRequestedCalibBay(false);

	    // B U T T O N  I N T E R L O C K
	    btnCalibStop.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnCalibStop.setDisable(true);

	    btnCalibStart.setStyle(""); // Enabled - Default
	    btnCalibStart.setDisable(false);

	    btnCalibReset.setStyle(""); // Enabled - Default
	    btnCalibReset.setDisable(false);
	    
	    btnCalibBayBypass.setStyle(""); // Enabled - Default
	    btnCalibBayBypass.setDisable(false);
	    
	    // L O G I C
	    calibrationStopTaskTimer = new Timer();
	    calibrationStopTaskTimer.schedule(new CalibrationBayStop(), 100);
	    Sleep(500);
	    calibrationStopTaskTimer.cancel();

	    Calib.logger.info("btnCalibStopOnClick : Exit:");
	}

	@FXML
	public void btnCalibResetOnClick() {
		Calib.logger.info("btnCalibResetOnClick : Invoked:");

		

	    // F L A G S
	    Calib.setStartProcessRequestedCalibBay(false);

	    Calib.setStopProcessCompletedCalibBay(false);
	    Calib.setStopProcessRequestedCalibBay(false);

	    Calib.setResetProcessCompletedCalibBay(false);
	    Calib.setResetProcessRequestedCalibBay(true);

	    // B U T T O N  I N T E R L O C K
	    btnCalibReset.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnCalibReset.setDisable(true);

	    btnCalibStart.setStyle(""); // Enabled - Default
	    btnCalibStart.setDisable(false);

	    btnCalibStop.setStyle(""); // Enabled - Default
	    btnCalibStop.setDisable(false);

	    // L O G I C
	    calibrationResetTaskTimer = new Timer();
	    calibrationResetTaskTimer.schedule(new CalibrationBayReset(), 100);
	    Sleep(500);
	    calibrationResetTaskTimer.cancel();

	    Calib.logger.info("btnCalibResetOnClick : Exit:");
	}

	@FXML
	public void btnCalibBayBypassOnClick() {
		Calib.logger.info("btnCalibBayBypassOnClick : Invoked:");
	    
	    // F L A G S
	    Calib.setStartProcessRequestedCalibBay(true);

	    Calib.setStopProcessCompletedCalibBay(false);
	    Calib.setStopProcessRequestedCalibBay(false);

	    Calib.setResetProcessCompletedCalibBay(false);
	    Calib.setResetProcessRequestedCalibBay(false);

	    // B U T T O N  I N T E R L O C K
	    btnCalibBayBypass.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnCalibBayBypass.setDisable(true);
	    
	    btnCalibReset.setStyle(""); // Enabled - Default
	    btnCalibReset.setDisable(false);

	    btnCalibStart.setStyle(""); // Enabled - Default
	    btnCalibStart.setDisable(false);

	    btnCalibStop.setStyle(""); // Enabled - Default
	    btnCalibStop.setDisable(false);

	    // L O G I C
	    calibBayBypassTaskTimer = new Timer();
	    // Assuming a CalibrationBayBypass class exists or will be created
	    calibBayBypassTaskTimer.schedule(new CalibrationBayBypass(), 100);
	    Sleep(500);
	    calibBayBypassTaskTimer.cancel();

	    Calib.logger.info("btnCalibBayBypassOnClick : Exit:");
	}
	
	//====================================================================================
	@FXML
	public void btnWaitingBayStartOnClick() {
		VerificWaiting.logger.info("btnWaitingBayStartOnClick : Invoked:");

	    // F L A G S
		VerificWaiting.setStartProcessRequestedWaitingBay(true);

		VerificWaiting.setStopProcessCompletedWaitingBay(false);
		VerificWaiting.setStopProcessRequestedWaitingBay(false);

		VerificWaiting.setResetProcessCompletedWaitingBay(false);
		VerificWaiting.setResetProcessRequestedWaitingBay(false);
		
		// B U T T O N  I N T E R L O C K
	    btnWaitingBayBypass.setStyle(""); // Disabled - Red
	    btnWaitingBayBypass.setDisable(false);
	    
	    btnWaitingBayReset.setStyle(""); // Enabled - Default
	    btnWaitingBayReset.setDisable(false);

	    btnWaitingBayStart.setStyle("-fx-background-color: #FF5733;"); // Enabled - Default
	    btnWaitingBayStart.setDisable(true);

	    btnWaitingBayStop.setStyle(""); // Enabled - Default
	    btnWaitingBayStop.setDisable(false);
	    
	    
	    // L O G I C
	    ref_tvTestStatus.getItems().clear();
	    allData.clear();
	    
	    
		waitingBayStartTaskTimer = new Timer();
		waitingBayStartTaskTimer.schedule(new VerificWaiting(), 100);
	    Sleep(500);
	    waitingBayStartTaskTimer.cancel();

		
		VerificWaiting.logger.info("btnWaitingBayStartOnClick : Exit:");
	}
	
	@FXML
	public void btnWaitingBayStopOnClick() {
		VerificWaiting.logger.info("btnWaitingBayStopOnClick : Invoked:");
		
		 // F L A G S
		VerificWaiting.abort_Waiting_Bay = true;

		VerificWaiting.setStartProcessRequestedWaitingBay(false);

		VerificWaiting.setStopProcessCompletedWaitingBay(false);
		VerificWaiting.setStopProcessRequestedWaitingBay(true);

		VerificWaiting.setResetProcessCompletedWaitingBay(false);
		VerificWaiting.setResetProcessRequestedWaitingBay(false);
		
		// B U T T O N  I N T E R L O C K
	    btnWaitingBayBypass.setStyle(""); // Disabled - Red
	    btnWaitingBayBypass.setDisable(false);
	    
	    btnWaitingBayReset.setStyle(""); // Enabled - Default
	    btnWaitingBayReset.setDisable(false);

	    btnWaitingBayStart.setStyle(""); // Enabled - Default
	    btnWaitingBayStart.setDisable(false);
	    
	    btnWaitingBayStop.setStyle("-fx-background-color: #FF5733;"); // Enabled - Default
	    btnWaitingBayStop.setDisable(true);
	    
	    
		waitingBayStopTaskTimer = new Timer();
		waitingBayStopTaskTimer.schedule(new WaitingBayStop(), 100);
	    Sleep(500);
	    waitingBayStopTaskTimer.cancel();

		VerificWaiting.logger.info("btnWaitingBayStopOnClick : Exit:");
	}
	
	@FXML
	public void btnWaitingBayResetOnClick() {
		VerificWaiting.logger.info("btnWaitingBayResetOnClick : Invoked:");
		
		  // F L A G S
		VerificWaiting.setStartProcessRequestedWaitingBay(false);

		VerificWaiting.setStopProcessCompletedWaitingBay(false);
		VerificWaiting.setStopProcessRequestedWaitingBay(false);

		VerificWaiting.setResetProcessCompletedWaitingBay(false);
		VerificWaiting.setResetProcessRequestedWaitingBay(true);
		
		// B U T T O N  I N T E R L O C K
	    btnWaitingBayBypass.setStyle(""); // Disabled - Red
	    btnWaitingBayBypass.setDisable(false);
	    
	    btnWaitingBayReset.setStyle("-fx-background-color: #FF5733;"); // Enabled - Default
	    btnWaitingBayReset.setDisable(true);

	    btnWaitingBayStart.setStyle(""); // Enabled - Default
	    btnWaitingBayStart.setDisable(false);

	    btnWaitingBayStop.setStyle(""); // Enabled - Default
	    btnWaitingBayStop.setDisable(false);
	    
		waitingBayResetTaskTimer = new Timer();
		waitingBayResetTaskTimer.schedule(new WaitingBayReset(), 100);
	    Sleep(500);
	    waitingBayResetTaskTimer.cancel();

		VerificWaiting.logger.info("btnWaitingBayResetOnClick : Exit:");
	}

	@FXML
	public void btnWaitingBayBypassOnClick() {
		VerificWaiting.logger.info("btnWaitingBayBypassOnClick : Invoked:");
	    
	    // F L A G S
		VerificWaiting.setStartProcessRequestedWaitingBay(true);

		VerificWaiting.setStopProcessCompletedWaitingBay(false);
		VerificWaiting.setStopProcessRequestedWaitingBay(false);

		VerificWaiting.setResetProcessCompletedWaitingBay(false);
		VerificWaiting.setResetProcessRequestedWaitingBay(false);

	    // B U T T O N  I N T E R L O C K
	    btnWaitingBayBypass.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnWaitingBayBypass.setDisable(true);
	    
	    btnWaitingBayReset.setStyle(""); // Enabled - Default
	    btnWaitingBayReset.setDisable(false);

	    btnWaitingBayStart.setStyle(""); // Enabled - Default
	    btnWaitingBayStart.setDisable(false);

	    btnWaitingBayStop.setStyle(""); // Enabled - Default
	    btnWaitingBayStop.setDisable(false);

	    // L O G I C
	    waitingBayBypassTaskTimer = new Timer();
	    // Assuming a WaitingBayBypass class exists or will be created
	    waitingBayBypassTaskTimer.schedule(new VerificWaiting(), 100);
	    Sleep(500);
	    waitingBayBypassTaskTimer.cancel();

	    VerificWaiting.logger.info("btnWaitingBayBypassOnClick : Exit:");
	}
	
	//============================================================================================================================================  

	@FXML
	public void btnVerificTestStartOnClick() {
		Verification.logger.info("btnVerificTestStartOnClick : Invoked:");

		ConstantConveyor.ALL_LOOP_BREAK_FLAG = false;

	    // F L A G S
	    Verification.setStartProcessRequestedVerificBay(true);

	    Verification.setStopProcessCompletedVerificBay(false);
	    Verification.setStopProcessRequestedVerificBay(false);

	    Verification.setResetProcessCompletedVerificBay(false);
	    Verification.setResetProcessRequestedVerificBay(false);

	    // B U T T O N  I N T E R L O C K
	    btnVerificTestStart.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnVerificTestStart.setDisable(true);

	    btnVerificTestStop.setStyle(""); // Enabled - Default
	    btnVerificTestStop.setDisable(false);

	    btnVerificTestReset.setStyle(""); // Enabled - Default
	    btnVerificTestReset.setDisable(false);

	    // L O G I C
	    ref_tvTestStatus.getItems().clear();
	    allData.clear();
	    
	    verificStartTaskTimer = new Timer();
	    verificStartTaskTimer.schedule(new Verification(), 100);
	    Sleep(500);
	    verificStartTaskTimer.cancel();

	    Verification.logger.info("btnVerificTestStartOnClick : Exit:");
	}

	@FXML
	public void btnVerificTestStopOnClick() {
		Verification.logger.info("btnVerificTestStopOnClick : Invoked:");

		

	    // F L A G S
	    Verification.abort_VerificTest_Bay = true;

	    Verification.setStartProcessRequestedVerificBay(false);

	    Verification.setStopProcessCompletedVerificBay(false);
	    Verification.setStopProcessRequestedVerificBay(true);

	    Verification.setResetProcessCompletedVerificBay(false);
	    Verification.setResetProcessRequestedVerificBay(false);

	    // B U T T O N  I N T E R L O C K
	    btnVerificTestStop.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnVerificTestStop.setDisable(true);

	    btnVerificTestStart.setStyle(""); // Enabled - Default
	    btnVerificTestStart.setDisable(false);

	    btnVerificTestReset.setStyle(""); // Enabled - Default
	    btnVerificTestReset.setDisable(false);
	    
	    btnVerificTestBayBypass.setStyle(""); // Enabled - Default
	    btnVerificTestBayBypass.setDisable(false);

	    // L O G I C
	    verificStopTaskTimer = new Timer();
	    verificStopTaskTimer.schedule(new VerificationTestBayStop(), 100);
	    Sleep(500);
	    verificStopTaskTimer.cancel();

	    Verification.logger.info("btnVerificTestStopOnClick : Exit:");
	}

	@FXML
	public void btnVerificTestResetOnClick() {
		Verification.logger.info("btnVerificTestResetOnClick : Invoked:");

		

	    // F L A G S
	    Verification.setStartProcessRequestedVerificBay(false);

	    Verification.setStopProcessCompletedVerificBay(false);
	    Verification.setStopProcessRequestedVerificBay(false);

	    Verification.setResetProcessCompletedVerificBay(false);
	    Verification.setResetProcessRequestedVerificBay(true);

	    // B U T T O N  I N T E R L O C K
	    /*btnVerificTestReset.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnVerificTestReset.setDisable(false);*/

	    btnVerificTestStart.setStyle(""); // Enabled - Default
	    btnVerificTestStart.setDisable(false);

	    btnVerificTestStop.setStyle(""); // Enabled - Default
	    btnVerificTestStop.setDisable(false);

	    // L O G I C
	    verificResetTaskTimer = new Timer();
	    verificResetTaskTimer.schedule(new VerificationTestBayReset(), 100);
	    Sleep(500);
	    verificResetTaskTimer.cancel();

	    Verification.logger.info("btnVerificTestResetOnClick : Exit:");
	}
	
	@FXML
	public void btnVerificTestBayBypassOnClick() {
		Verification.logger.info("btnVerificTestBayBypassOnClick : Invoked:");
	    
	    // F L A G S
	    Verification.setStartProcessRequestedVerificBay(true);

	    Verification.setStopProcessCompletedVerificBay(false);
	    Verification.setStopProcessRequestedVerificBay(false);

	    Verification.setResetProcessCompletedVerificBay(false);
	    Verification.setResetProcessRequestedVerificBay(false);

	    // B U T T O N  I N T E R L O C K
	    btnVerificTestBayBypass.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnVerificTestBayBypass.setDisable(true);
	    
	    btnVerificTestReset.setStyle(""); // Enabled - Default
	    btnVerificTestReset.setDisable(false);

	    btnVerificTestStart.setStyle(""); // Enabled - Default
	    btnVerificTestStart.setDisable(false);

	    btnVerificTestStop.setStyle(""); // Enabled - Default
	    btnVerificTestStop.setDisable(false);

	    // L O G I C
	    verificTestBayBypassTaskTimer = new Timer();
	    // Assuming a VerificationTestBayBypass class exists or will be created
	    verificTestBayBypassTaskTimer.schedule(new VerificationTestBayBypass(), 100);
	    Sleep(500);
	    verificTestBayBypassTaskTimer.cancel();

	    Verification.logger.info("btnVerificTestBayBypassOnClick : Exit:");
	}
	
	//============================================================================================================================================  

	@FXML
	public void btnSctNlt1StartOnClick() {
		StaNld_Bay1.logger.info("state: btnSctNlt1StartOnClick : Invoked:");

		ConstantConveyor.ALL_LOOP_BREAK_FLAG = false;

	    // F L A G S
	    StaNld_Bay1.setStartProcessRequestedStaNldBay1(true);

	    StaNld_Bay1.setStopProcessCompletedStaNldBay1(false);
	    StaNld_Bay1.setStopProcessRequestedStaNldBay1(false);

	    StaNld_Bay1.setResetProcessCompletedStaNldBay1(false);
	    StaNld_Bay1.setResetProcessRequestedStaNldBay1(false);

	    // B U T T O N  I N T E R L O C K
	    btnSctNlt1Start.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnSctNlt1Start.setDisable(true);

	    btnSctNlt1Stop.setStyle(""); // Enabled - Default
	    btnSctNlt1Stop.setDisable(false);

	    btnSctNlt1Reset.setStyle(""); // Enabled - Default
	    btnSctNlt1Reset.setDisable(false);

	    // L O G I C
	    ref_tvTestStatus.getItems().clear();
	    allData.clear();
	    
	    sctNlt1StartTaskTimer = new Timer();
	    sctNlt1StartTaskTimer.schedule(new StaNld_Bay1(), 100);
	    Sleep(500);
	    sctNlt1StartTaskTimer.cancel();

	    StaNld_Bay1.logger.info("btnSctNlt1StartOnClick : Exit:");
	}

	@FXML
	public void btnSctNlt1StopOnClick() {
		StaNld_Bay1.logger.info("btnSctNlt1StopOnClick : Invoked:");

		

	    // F L A G S
	    StaNld_Bay1.abort_SCT_NLT_Bay1 = true;

	    StaNld_Bay1.setStartProcessRequestedStaNldBay1(false);

	    StaNld_Bay1.setStopProcessCompletedStaNldBay1(false);
	    StaNld_Bay1.setStopProcessRequestedStaNldBay1(true);

	    StaNld_Bay1.setResetProcessCompletedStaNldBay1(false);
	    StaNld_Bay1.setResetProcessRequestedStaNldBay1(false);

	    // B U T T O N  I N T E R L O C K
	    btnSctNlt1Stop.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnSctNlt1Stop.setDisable(true);

	    btnSctNlt1Start.setStyle(""); // Enabled - Default
	    btnSctNlt1Start.setDisable(false);

	    btnSctNlt1Reset.setStyle(""); // Enabled - Default
	    btnSctNlt1Reset.setDisable(false);

	    btnSctNlt1BayBypass.setStyle(""); // Enabled - Default
	    btnSctNlt1BayBypass.setDisable(false);
	    
	    // L O G I C
	    sctNlt1StopTaskTimer = new Timer();
	    sctNlt1StopTaskTimer.schedule(new STA_NoLoadTestBay1Stop(), 100);
	    Sleep(500);
	    sctNlt1StopTaskTimer.cancel();

	    StaNld_Bay1.logger.info("btnSctNlt1StopOnClick : Exit:");
	}

	@FXML
	public void btnSctNlt1ResetOnClick() {
		StaNld_Bay1.logger.info("btnSctNlt1ResetOnClick : Invoked:");

		

	    // F L A G S
	    StaNld_Bay1.setStartProcessRequestedStaNldBay1(false);

	    StaNld_Bay1.setStopProcessCompletedStaNldBay1(false);
	    StaNld_Bay1.setStopProcessRequestedStaNldBay1(false);

	    StaNld_Bay1.setResetProcessCompletedStaNldBay1(false);
	    StaNld_Bay1.setResetProcessRequestedStaNldBay1(true);

	    // B U T T O N  I N T E R L O C K
	    /*btnSctNlt1Reset.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnSctNlt1Reset.setDisable(true);*/

	    btnSctNlt1Start.setStyle(""); // Enabled - Default
	    btnSctNlt1Start.setDisable(false);

	    btnSctNlt1Stop.setStyle(""); // Enabled - Default
	    btnSctNlt1Stop.setDisable(false);

	    // L O G I C
	    sctNlt1ResetTaskTimer = new Timer();
	    sctNlt1ResetTaskTimer.schedule(new STA_NoLoadTestBay1Reset(), 100);
	    Sleep(500);
	    sctNlt1ResetTaskTimer.cancel();

	    StaNld_Bay1.logger.info("btnSctNlt1ResetOnClick : Exit:");
	}

	@FXML
	public void btnSctNlt1BayBypassOnClick() {
		StaNld_Bay1.logger.info("btnSctNlt1BayBypassOnClick : Invoked:");
	    
	    // F L A G S
	    StaNld_Bay1.setStartProcessRequestedStaNldBay1(true);

	    StaNld_Bay1.setStopProcessCompletedStaNldBay1(false);
	    StaNld_Bay1.setStopProcessRequestedStaNldBay1(false);

	    StaNld_Bay1.setResetProcessCompletedStaNldBay1(false);
	    StaNld_Bay1.setResetProcessRequestedStaNldBay1(false);

	    // B U T T O N  I N T E R L O C K
	    btnSctNlt1BayBypass.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnSctNlt1BayBypass.setDisable(true);
	    
	    btnSctNlt1Reset.setStyle(""); // Enabled - Default
	    btnSctNlt1Reset.setDisable(false);

	    btnSctNlt1Start.setStyle(""); // Enabled - Default
	    btnSctNlt1Start.setDisable(false);

	    btnSctNlt1Stop.setStyle(""); // Enabled - Default
	    btnSctNlt1Stop.setDisable(false);

	    // L O G I C
	    sctNlt1BayBypassTaskTimer = new Timer();
	    // Assuming a STA_NoLoadTestBay1Bypass class exists or will be created
	    sctNlt1BayBypassTaskTimer.schedule(new STA_NoLoadTestBay1Bypass(), 100);
	    Sleep(500);
	    sctNlt1BayBypassTaskTimer.cancel();

	    StaNld_Bay1.logger.info("btnSctNlt1BayBypassOnClick : Exit:");
	}

	//============================================================================================================================================  

	@FXML
	public void btnSctNlt2StartOnClick() {
		StaNld_Bay2.logger.info("btnSctNlt2StartOnClick-Y : Invoked:");

		ConstantConveyor.ALL_LOOP_BREAK_FLAG = false;
		
	    // F L A G S
	    StaNld_Bay2.setStartProcessRequestedStaNldBay2(true);

	    StaNld_Bay2.setStopProcessCompletedStaNldBay2(false);
	    StaNld_Bay2.setStopProcessRequestedStaNldBay2(false);
	    StaNld_Bay2.logger.info("setStopProcessRequestedStaNldBay2 -Test6  : false");

	    StaNld_Bay2.setResetProcessCompletedStaNldBay2(false);
	    StaNld_Bay2.setResetProcessRequestedStaNldBay2(false);

	    // B U T T O N  I N T E R L O C K
	    btnSctNlt2Start.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnSctNlt2Start.setDisable(true);

	    btnSctNlt2Stop.setStyle(""); // Enabled - Default
	    btnSctNlt2Stop.setDisable(false);

	    btnSctNlt2Reset.setStyle(""); // Enabled - Default
	    btnSctNlt2Reset.setDisable(false);

	    // L O G I C
	    ref_tvTestStatus.getItems().clear();
	    allData.clear();
	    
	    sctNlt2StartTaskTimer = new Timer();
	    sctNlt2StartTaskTimer.schedule(new StaNld_Bay2(), 100);
	    Sleep(500);
	    sctNlt2StartTaskTimer.cancel();

	    StaNld_Bay2.logger.info("btnSctNlt2StartOnClick : Exit:");
	}

	@FXML
	public void btnSctNlt2StopOnClick() {
		StaNld_Bay2.logger.info("btnSctNlt2StopOnClick : Invoked:");

		

	    // F L A G S
	    StaNld_Bay2.abort_SCT_NLT_Bay2 = true;

	    StaNld_Bay2.setStartProcessRequestedStaNldBay2(false);

	    StaNld_Bay2.setStopProcessCompletedStaNldBay2(false);
	    StaNld_Bay2.setStopProcessRequestedStaNldBay2(true);
	    StaNld_Bay2.logger.info("setStopProcessRequestedStaNldBay2 -Test7  : true");

	    StaNld_Bay2.setResetProcessCompletedStaNldBay2(false);
	    StaNld_Bay2.setResetProcessRequestedStaNldBay2(false);

	    // B U T T O N  I N T E R L O C K
	    btnSctNlt2Stop.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnSctNlt2Stop.setDisable(true);

	    btnSctNlt2Start.setStyle(""); // Enabled - Default
	    btnSctNlt2Start.setDisable(false);

	    btnSctNlt2Reset.setStyle(""); // Enabled - Default
	    btnSctNlt2Reset.setDisable(false);
	    
	    btnSctNlt2BayBypass.setStyle(""); // Enabled - Default
	    btnSctNlt2BayBypass.setDisable(false);

	    // L O G I C
	    sctNlt2StopTaskTimer = new Timer();
	    sctNlt2StopTaskTimer.schedule(new STA_NoLoadTestBay2Stop(), 100);
	    Sleep(500);
	    sctNlt2StopTaskTimer.cancel();

	    StaNld_Bay2.logger.info("btnSctNlt2StopOnClick : Exit:");
	}

	@FXML
	public void btnSctNlt2ResetOnClick() {
		StaNld_Bay2.logger.info("btnSctNlt2ResetOnClick : Invoked:");

		

	    // F L A G S
	    StaNld_Bay2.setStartProcessRequestedStaNldBay2(false);

	    StaNld_Bay2.setStopProcessCompletedStaNldBay2(false);
	    StaNld_Bay2.setStopProcessRequestedStaNldBay2(false);
	    StaNld_Bay2.logger.info("setStopProcessRequestedStaNldBay2 -Test8  : false");

	    StaNld_Bay2.setResetProcessCompletedStaNldBay2(false);
	    StaNld_Bay2.setResetProcessRequestedStaNldBay2(true);

	    // B U T T O N  I N T E R L O C K
	    btnSctNlt2Reset.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnSctNlt2Reset.setDisable(true);

	    btnSctNlt2Start.setStyle(""); // Enabled - Default
	    btnSctNlt2Start.setDisable(false);

	    btnSctNlt2Stop.setStyle(""); // Enabled - Default
	    btnSctNlt2Stop.setDisable(false);

	    // L O G I C
	    sctNlt2ResetTaskTimer = new Timer();
	    sctNlt2ResetTaskTimer.schedule(new STA_NoLoadTestBay2Reset(), 100);
	    Sleep(500);
	    sctNlt2ResetTaskTimer.cancel();

	    StaNld_Bay2.logger.info("btnSctNlt2ResetOnClick : Exit:");
	}

	@FXML
	public void btnSctNlt2BayBypassOnClick() {
		StaNld_Bay2.logger.info("btnSctNlt2BayBypassOnClick : Invoked:");
	    
	    // F L A G S
	    StaNld_Bay2.setStartProcessRequestedStaNldBay2(true);

	    StaNld_Bay2.setStopProcessCompletedStaNldBay2(false);
	    StaNld_Bay2.setStopProcessRequestedStaNldBay2(false);
	    StaNld_Bay2.logger.info("setStopProcessRequestedStaNldBay2 -Test9  : false");

	    StaNld_Bay2.setResetProcessCompletedStaNldBay2(false);
	    StaNld_Bay2.setResetProcessRequestedStaNldBay2(false);

	    // B U T T O N  I N T E R L O C K
	    btnSctNlt2BayBypass.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnSctNlt2BayBypass.setDisable(true);
	    
	    btnSctNlt2Reset.setStyle(""); // Enabled - Default
	    btnSctNlt2Reset.setDisable(false);

	    btnSctNlt2Start.setStyle(""); // Enabled - Default
	    btnSctNlt2Start.setDisable(false);

	    btnSctNlt2Stop.setStyle(""); // Enabled - Default
	    btnSctNlt2Stop.setDisable(false);

	    // L O G I C
	    sctNlt2BayBypassTaskTimer = new Timer();
	    // Assuming a STA_NoLoadTestBay2Bypass class exists or will be created
	    sctNlt2BayBypassTaskTimer.schedule(new STA_NoLoadTestBay2Bypass(), 100);
	    Sleep(500);
	    sctNlt2BayBypassTaskTimer.cancel();

	    StaNld_Bay2.logger.info("btnSctNlt2BayBypassOnClick : Exit:");
	}

	//============================================================================================================================================  

	@FXML
	public void btnCommTestStartOnClick() {
		Comm.logger.info("btnCommTestStartOnClick : Invoked:");

		ConstantConveyor.ALL_LOOP_BREAK_FLAG = false;

	    // F L A G S
	    Comm.setStartProcessRequestedCommBay(true);

	    Comm.setStopProcessCompletedCommBay(false);
	    Comm.setStopProcessRequestedCommBay(false);

	    Comm.setResetProcessCompletedCommBay(false);
	    Comm.setResetProcessRequestedCommBay(false);

	    // B U T T O N  I N T E R L O C K
	    btnCommTestStart.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnCommTestStart.setDisable(true);

	    btnCommTestStop.setStyle(""); // Enabled - Default
	    btnCommTestStop.setDisable(false);

	    btnCommTestReset.setStyle(""); // Enabled - Default
	    btnCommTestReset.setDisable(false);

	    // L O G I C
	    ref_tvTestStatus.getItems().clear();
	    allData.clear();
	    
	    commStartTaskTimer = new Timer();
	    commStartTaskTimer.schedule(new Comm(), 100);
	    Sleep(500);
	    commStartTaskTimer.cancel();

	    Comm.logger.info("btnCommTestStartOnClick : Exit:");
	}

	@FXML
	public void btnCommTestStopOnClick() {
		Comm.logger.info("btnCommTestStopOnClick : Invoked:");

		

	    // F L A G S
	    Comm.abort_CommTest_Bay = true;

	    Comm.setStartProcessRequestedCommBay(false);

	    Comm.setStopProcessCompletedCommBay(false);
	    Comm.setStopProcessRequestedCommBay(true);

	    Comm.setResetProcessCompletedCommBay(false);
	    Comm.setResetProcessRequestedCommBay(false);

	    // B U T T O N  I N T E R L O C K
	    btnCommTestStop.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnCommTestStop.setDisable(true);

	    btnCommTestStart.setStyle(""); // Enabled - Default
	    btnCommTestStart.setDisable(false);

	    btnCommTestReset.setStyle(""); // Enabled - Default
	    btnCommTestReset.setDisable(false);
	    
	    btnCommTestBayBypass.setStyle(""); // Enabled - Default
	    btnCommTestBayBypass.setDisable(false);

	    // L O G I C
	    commStopTaskTimer = new Timer();
	    commStopTaskTimer.schedule(new CommunicationTestBayStop(), 100);
	    Sleep(500);
	    commStopTaskTimer.cancel();

	    Comm.logger.info("btnCommTestStopOnClick : Exit:");
	}

	@FXML
	public void btnCommTestResetOnClick() {
		Comm.logger.info("btnCommTestResetOnClick : Invoked:");

		

	    // F L A G S
	    Comm.setStartProcessRequestedCommBay(false);

	    Comm.setStopProcessCompletedCommBay(false);
	    Comm.setStopProcessRequestedCommBay(false);

	    Comm.setResetProcessCompletedCommBay(false);
	    Comm.setResetProcessRequestedCommBay(true);

	    // B U T T O N  I N T E R L O C K
	    btnCommTestReset.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnCommTestReset.setDisable(true);

	    btnCommTestStart.setStyle(""); // Enabled - Default
	    btnCommTestStart.setDisable(false);

	    btnCommTestStop.setStyle(""); // Enabled - Default
	    btnCommTestStop.setDisable(false);

	    // L O G I C
	    commResetTaskTimer = new Timer();
	    commResetTaskTimer.schedule(new CommunicationTestBayReset(), 100);
	    Sleep(500);
	    commResetTaskTimer.cancel();

	    Comm.logger.info("btnCommTestResetOnClick : Exit:");
	}

	@FXML
	public void btnCommTestBayBypassOnClick() {
		Comm.logger.info("btnCommTestBayBypassOnClick : Invoked:");
	    
	    // F L A G S
	    Comm.setStartProcessRequestedCommBay(true);

	    Comm.setStopProcessCompletedCommBay(false);
	    Comm.setStopProcessRequestedCommBay(false);

	    Comm.setResetProcessCompletedCommBay(false);
	    Comm.setResetProcessRequestedCommBay(false);

	    // B U T T O N  I N T E R L O C K
	    btnCommTestBayBypass.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
	    btnCommTestBayBypass.setDisable(true);
	    
	    btnCommTestReset.setStyle(""); // Enabled - Default
	    btnCommTestReset.setDisable(false);

	    btnCommTestStart.setStyle(""); // Enabled - Default
	    btnCommTestStart.setDisable(false);

	    btnCommTestStop.setStyle(""); // Enabled - Default
	    btnCommTestStop.setDisable(false);

	    // L O G I C
	    commTestBayBypassTaskTimer = new Timer();
	    // Assuming a CommunicationTestBayBypass class exists or will be created
	    commTestBayBypassTaskTimer.schedule(new CommBayBypass(), 100);
	    Sleep(500);
	    commTestBayBypassTaskTimer.cancel();

	    Comm.logger.info("btnCommTestBayBypassOnClick : Exit:");
	}
	
	//============================================================================================================================================ 

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep2 :InterruptedException:"+ e.getMessage());
		}

	}

/*	public static void updateTestStatusGui(TestInterfaceStatus testIntefaceStatus){

		ref_tvTestStatus.getItems().stream()
		.filter(e->e.getSerialNo().equals(testIntefaceStatus.getSerialNo()))
					.filter(e->e.getBayName().equals(testIntefaceStatus.getBayName()))
			.filter(e->e.getStateName().equals(testIntefaceStatus.getStateName()))
			.filter(e->e.getcName().equals(testIntefaceStatus.getcName()))
		//.filter(e->e.getPortId().equals(testIntefaceStatus.getPortId()))
		.forEach(e->{

			//ApplicationLauncher.logger.debug("updateTestStatusGui: getTestStatus: " + testIntefaceStatus.getTestStatus());
			e.setTestStatus(testIntefaceStatus.getTestStatus());
			e.setDeviceResponseStatus(testIntefaceStatus.getDeviceResponseStatus());
			e.setDeviceResponseData(testIntefaceStatus.getDeviceResponseData());
			e.setPortName(testIntefaceStatus.getPortName());
			//e.setPositionNo(testIntefaceStatus.getPositionNo());
		});


		Platform.runLater(()->{
			ref_tvTestStatus.refresh();
		});
	}*/
	//########Gopi-Parallel
/*	public static void updateTestStatusGui(TestInterfaceStatus testInterfaceStatus) {
	    Platform.runLater(() -> {
	        ref_tvTestStatus.getItems().stream()
	            .filter(e -> e.getSerialNo().equals(testInterfaceStatus.getSerialNo()))
	            .forEach(e -> {
	                e.setTestStatus(testInterfaceStatus.getTestStatus());
	                e.setDeviceResponseStatus(testInterfaceStatus.getDeviceResponseStatus());
	                e.setDeviceResponseData(testInterfaceStatus.getDeviceResponseData());
	                e.setPortName(testInterfaceStatus.getPortName());
	            });

	        ref_tvTestStatus.refresh();
	    });
	}*/
	
/*	public static void updateTestStatusGui(TestInterfaceStatus testInterfaceStatus) {
	    Platform.runLater(()->{
	    	 new ArrayList<>(ref_tvTestStatus.getItems()).stream()
	            .filter(e -> e.getSerialNo().equals(testInterfaceStatus.getSerialNo()))
	            .forEach(e -> {
	                e.setTestStatus(testInterfaceStatus.getTestStatus());
	                e.setDeviceResponseStatus(testInterfaceStatus.getDeviceResponseStatus());
	                e.setDeviceResponseData(testInterfaceStatus.getDeviceResponseData());
	                e.setPortName(testInterfaceStatus.getPortName());
	            });
	    	
	    	List<TestInterfaceStatus> snapshot;
	        synchronized (ref_tvTestStatus.getItems()) {
	            snapshot = new ArrayList<>(ref_tvTestStatus.getItems());
	        }

	        snapshot.stream()
	            .filter(e -> e.getSerialNo().equals(testInterfaceStatus.getSerialNo()))
	            .forEach(e -> {
	                e.setTestStatus(testInterfaceStatus.getTestStatus());
	                e.setDeviceResponseStatus(testInterfaceStatus.getDeviceResponseStatus());
	                e.setDeviceResponseData(testInterfaceStatus.getDeviceResponseData());
	                e.setPortName(testInterfaceStatus.getPortName());
	            });
	    	
	    	ref_tvTestStatus.refresh();
	    });
	}*/
	
	
/*	public static void updateTestStatusGui(TestInterfaceStatus testInterfaceStatus) {
		
		
	    Platform.runLater(() -> {
	        try {
	            if (testInterfaceStatus == null || testInterfaceStatus.getSerialNo() == null) {
	                ApplicationLauncher.logger.warn("updateTestStatusGui: Input testInterfaceStatus or its serialNo is null");
	                return;
	            }

	            List<TestInterfaceStatus> snapshot = FXCollections.observableArrayList(ref_tvTestStatus.getItems());

	            snapshot.stream()
	                .filter(e -> e != null && e.getSerialNo() != null &&
	                        e.getSerialNo().equals(testInterfaceStatus.getSerialNo()))
	                .forEach(e -> {
	                    try {
	                        e.setTestStatus(testInterfaceStatus.getTestStatus());
	                        e.setDeviceResponseStatus(testInterfaceStatus.getDeviceResponseStatus());
	                        e.setDeviceResponseData(testInterfaceStatus.getDeviceResponseData());
	                        e.setPortName(testInterfaceStatus.getPortName());
	                    } catch (Exception ex) {
	                        ApplicationLauncher.logger.error("updateTestStatusGui: Exception during update: " + ex.getMessage(), ex);
	                    }
	                });

	            ref_tvTestStatus.refresh();
	        } catch (Exception ex) {
	            ApplicationLauncher.logger.error("updateTestStatusGui: Outer Exception: " + ex.getMessage(), ex);
	        }
	    });
	}
*/
	
	
	public static void updateTestStatusGui(TestInterfaceStatus testInterfaceStatus) {
	    Platform.runLater(() -> {
	        try {
	            if (testInterfaceStatus == null || testInterfaceStatus.getSerialNo() == null) {
	                ApplicationLauncher.logger.warn("updateTestStatusGui: Input testInterfaceStatus or its serialNo is null");
	                return;
	            }

	            // Acquire the semaphore (wait if another thread is already executing this)
	            testStatusDisplaySemaphore.acquire();
	            
	            try {
	                ObservableList<TestInterfaceStatus> items = ref_tvTestStatus.getItems();
	                
	                // Find and update the matching item
	                for (int i = 0; i < items.size(); i++) {
	                    TestInterfaceStatus e = items.get(i);
	                    if (e != null && e.getSerialNo() != null && 
	                        e.getSerialNo().equals(testInterfaceStatus.getSerialNo())) {
	                        try {
	                            e.setTestStatus(testInterfaceStatus.getTestStatus());
	                            e.setDeviceResponseStatus(testInterfaceStatus.getDeviceResponseStatus());
	                            e.setDeviceResponseData(testInterfaceStatus.getDeviceResponseData());
	                            e.setPortName(testInterfaceStatus.getPortName());
	                        } catch (Exception ex) {
	                            ApplicationLauncher.logger.error("updateTestStatusGui: Exception during update: " + ex.getMessage(), ex);
	                        }
	                        break; // Assuming serialNo is unique
	                    }
	                }
	                
	                ref_tvTestStatus.refresh();
	            } finally {
	                // Always release the semaphore, even if an exception occurs
	                testStatusDisplaySemaphore.release();
	            }
	            
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	            ApplicationLauncher.logger.error("updateTestStatusGui: Thread was interrupted while waiting for semaphore");
	        } catch (Exception ex) {
	            ApplicationLauncher.logger.error("updateTestStatusGui: Outer Exception: " + ex.getMessage(), ex);
	        }
	    });
	}
	public static int addToTestStatusGui(TestInterfaceStatus testIntefaceStatus){
		try{
			testIntefaceStatus.setSerialNo(String.valueOf(getSerialNoTestStatusAtomic().get()));
			ref_tvTestStatus.getItems().add(testIntefaceStatus);
			Platform.runLater(()->{
				ref_tvTestStatus.refresh();
			});
			//ref_tvTestStatus.refresh();
			allData.add(testIntefaceStatus);
		} catch (Exception ex) {
            ApplicationLauncher.logger.error("updateTestStatusGui: Exception-X: " + ex.getMessage());
        }
		return getSerialNoTestStatusAtomic().getAndIncrement();
	}

	public static void updateTestInterfaceStatusOnGui(Map<String,Object> responseReturn,String testStatus){
		TestInterfaceStatus test_I_F_Status = (TestInterfaceStatus)responseReturn.get("testInterfaceStatus");
		test_I_F_Status.setTestStatus(testStatus);

		updateTestStatusGui(test_I_F_Status);

	}


	public static void updateTestInterfaceStatusOnGuiV2(BayResponse bayResponse,String testStatus){
		TestInterfaceStatus test_I_F_Status = bayResponse.getTestInterfaceStatus();
		test_I_F_Status.setTestStatus(testStatus);

		updateTestStatusGui(test_I_F_Status);

	}
	
	//============================================================================================================================================ 
	
	//============================================================================================================================================ 
	
	//A L L  S T A R T  O N  C L I C K =========================================================================================================== 

	@FXML 
	public void	btnAllStartOnClick() {
		ApplicationLauncher.logger.info("btnFtStartOnClick : Invoked:");

		//btnAllStart.setStyle("-fx-background-color: #FF5733;");
		/*btnAllStart.setDisable(true);
		btnAllStop.setDisable(false);*/

		ref_tvTestStatus.getItems().clear();
		allData.clear();
		
		funtionalBayStartTaskTimer = new Timer();
		funtionalBayStartTaskTimer.schedule(new Ft(),100);
		Sleep(500);
		funtionalBayStartTaskTimer.cancel();

		hvtBayStartTaskTimer = new Timer();
		hvtBayStartTaskTimer.schedule(new Hv(),200);
		Sleep(500);
		hvtBayStartTaskTimer.cancel();

		insResStartTaskTimer = new Timer();
		insResStartTaskTimer.schedule(new Ir(),300);
		Sleep(500);
		insResStartTaskTimer.cancel();

		
		 
		/*calibrationStartTaskTimer = new Timer();
		calibrationStartTaskTimer.schedule(new CalibrationBay2(),400);
		Sleep(500);
		calibrationStartTaskTimer.cancel();


		waitingBayStartTaskTimer = new Timer();
		waitingBayStartTaskTimer.schedule(new WaitingBay2(), 100);
	    Sleep(500);
	    waitingBayStartTaskTimer.cancel();

		verificStartTaskTimer = new Timer();
		verificStartTaskTimer.schedule(new VerificationTestBay2(),100);
		Sleep(500);
		verificStartTaskTimer.cancel();*/

		/*sctNlt1StartTaskTimer = new Timer();
		sctNlt1StartTaskTimer.schedule(new STA_NoLoadTestBay1_2(),100);
		Sleep(500);
		sctNlt1StartTaskTimer.cancel();

		sctNlt2StartTaskTimer = new Timer();
		sctNlt2StartTaskTimer.schedule(new STA_NoLoadTestBay2_2(),100);
		Sleep(500);
		sctNlt2StartTaskTimer.cancel();

		commStartTaskTimer = new Timer();
		commStartTaskTimer.schedule(new CommunicationTestBay2(),100);
		Sleep(500);
		commStartTaskTimer.cancel();*/
		
		ApplicationLauncher.logger.info("btnFtStartOnClick : EXIT:");
	}


	@FXML 
	public void	btnAllStopOnClick() {
		ApplicationLauncher.logger.info("btnAllStopOnClick : Invoked:");

		Ft.setStopProcessRequestedFtBay(true);
		Hv.abort_HVT_Bay = true;
		Ir.abort_IRT_Bay = true;
		Calib.abort_Calib_Bay = true;
		Verification.abort_VerificTest_Bay = true;
		StaNld_Bay1.abort_SCT_NLT_Bay1 = true;
		StaNld_Bay2.abort_SCT_NLT_Bay2 = true;
		Comm.abort_CommTest_Bay = true;
		//ggg add waiting  bay

		Ft.setStopProcessRequestedFtBay(true);
		Hv.setStopProcessRequestedHvtBay(true);
		Ir.setStopProcessRequestedIrtBay(true);
		Calib.setStopProcessRequestedCalibBay(true);
		Verification.setStopProcessRequestedVerificBay(true);
		StaNld_Bay1.setStopProcessRequestedStaNldBay1(true);
		StaNld_Bay2.setStopProcessRequestedStaNldBay2(true);
		StaNld_Bay2.logger.info("setStopProcessRequestedStaNldBay2 -Test10  : true");
		Comm.setStopProcessRequestedCommBay(true);
		VerificWaiting.setStopProcessRequestedWaitingBay(true);
		

		/*btnAllStart.setStyle("-fx-background-color: #FF5733;");
		btnAllStart.setDisable(false);
		btnAllStop.setDisable(true);*/

		funtionalBayStopTaskTimer = new Timer();
		funtionalBayStopTaskTimer.schedule(new FunctionalTestBayStop(),100);
		Sleep(500);
		funtionalBayStopTaskTimer.cancel();

		hvtBayStopTaskTimer = new Timer();
		hvtBayStopTaskTimer.schedule(new HighVoltageTestBayStop(),100);
		Sleep(500);
		hvtBayStopTaskTimer.cancel();

		insResStopTaskTimer = new Timer();
		insResStopTaskTimer.schedule(new InsulationResistanceTestBayStop(),100);
		Sleep(500);
		insResStopTaskTimer.cancel(); 
 
		/*calibrationStopTaskTimer = new Timer();
		calibrationStopTaskTimer.schedule(new CalibrationBayStop(),100);
		Sleep(500);
		calibrationStopTaskTimer.cancel();
		
		waitingBayStopTaskTimer = new Timer();
		waitingBayStopTaskTimer.schedule(new WaitingBayStop(), 100);
	    Sleep(500);
	    waitingBayStopTaskTimer.cancel();

		verificStopTaskTimer = new Timer();
		verificStopTaskTimer.schedule(new VerificationTestBayStop(),100);
		Sleep(500);
		verificStopTaskTimer.cancel();*/

		/*sctNlt1StopTaskTimer = new Timer();
		sctNlt1StopTaskTimer.schedule(new STA_NoLoadTestBay1Stop(),100);
		Sleep(500);
		sctNlt1StopTaskTimer.cancel();

		sctNlt2StopTaskTimer = new Timer();
		sctNlt2StopTaskTimer.schedule(new STA_NoLoadTestBay2Stop(),100);
		Sleep(500);
		sctNlt2StopTaskTimer.cancel();

		commStopTaskTimer = new Timer();
		commStopTaskTimer.schedule(new CommunicationTestBayStop(),100);
		Sleep(500);
		commStopTaskTimer.cancel();
		*/
		ApplicationLauncher.logger.info("btnAllStopOnClick : Exit:");
	}

	@FXML 
	public void	btnAllResetOnClick() {
		ApplicationLauncher.logger.info("btnAllResetOnClick : Invoked:");
		
	    ConstantConveyor.ALL_LOOP_BREAK_FLAG = false;
		 
		/*FunctionalTestBay.setResetProcessRequestedFtBay(true);
		HighVoltageTestBay2.setResetProcessRequestedHvtBay(true);
		InsulationResistanceTestBay.setResetProcessRequestedIrtBay(true);
		CalibrationBay2.setResetProcessRequestedCalibBay(true);
		VerificationTestBay2.setResetProcessRequestedVerificBay(true);
		STA_NoLoadTestBay1_2.setResetProcessRequestedSctNltBay1(true);
		STA_NoLoadTestBay2_2.setResetProcessRequestedSctNltBay2(true);
		CommunicationTestBay2.setResetProcessRequestedCommBay(true);

		funtionalBayResetTaskTimer = new Timer();
		funtionalBayResetTaskTimer.schedule(new FunctionalTestBayReset(),100);
		Sleep(500);
		funtionalBayResetTaskTimer.cancel();

		hvtBayResetTaskTimer = new Timer();
		hvtBayResetTaskTimer.schedule(new HighVoltageTestBayReset(),100);
		Sleep(500);
		hvtBayResetTaskTimer.cancel();

		insResResetTaskTimer = new Timer();
		insResResetTaskTimer.schedule(new InsulationResistanceTestBayReset(),100);
		Sleep(500);
		insResResetTaskTimer.cancel(); 

		calibrationResetTaskTimer = new Timer();
		calibrationResetTaskTimer.schedule(new CalibrationBayReset(),100);
		Sleep(500);
		calibrationResetTaskTimer.cancel();
		
		waitingBayResetTaskTimer = new Timer();
		waitingBayResetTaskTimer.schedule(new WaitingBayReset(), 100);
	    Sleep(500);
	    waitingBayResetTaskTimer.cancel();

		verificResetTaskTimer = new Timer();
		verificResetTaskTimer.schedule(new VerificationTestBayReset(),100);
		Sleep(500);
		verificResetTaskTimer.cancel();

		sctNlt1ResetTaskTimer = new Timer();
		sctNlt1ResetTaskTimer.schedule(new STA_NoLoadTestBay1Reset(),100);
		Sleep(500);
		sctNlt1ResetTaskTimer.cancel();

		sctNlt2ResetTaskTimer = new Timer();
		sctNlt2ResetTaskTimer.schedule(new STA_NoLoadTestBay2Reset(),100);
		Sleep(500);
		sctNlt2ResetTaskTimer.cancel();*/

		commResetTaskTimer = new Timer();
		commResetTaskTimer.schedule(new CommunicationTestBayReset(),100);
		Sleep(500);
		commResetTaskTimer.cancel();
		
		ApplicationLauncher.logger.info("btnAllResetOnClick : Invoked:");
	}

	//=== F I L T E R  B U T T O N  -  O N C L I C K ======================================================

	@FXML 
	public void btnFilterOnClick() {
		ApplicationLauncher.logger.debug("btnFilterOnClick Invoked:");

		Platform.runLater(()->{

			ref_btnFilter.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
		});
		btnFilterTaskTimer = new Timer();
		btnFilterTaskTimer.schedule(new FilterOnClickTimerTask(),100);
		Sleep(500);
		btnFilterTaskTimer.cancel();

	}

	class FilterOnClickTimerTask extends TimerTask {
		@Override
		public void run() {
			Platform.runLater(()->{
				Object selectedValue = ref_cmbBxFilterPosition.getValue();
				ApplicationLauncher.logger.debug("FilterOnClickTimerTask Position Number : " + selectedValue);

				List<TestInterfaceStatus> filteredDataList;
				if ("All".equals(selectedValue)) {
					filteredDataList = new ArrayList<>(allData);
				} else if (selectedValue instanceof Integer) {
					int filterPosition = (Integer) selectedValue;
					filteredDataList = allData.stream()
							.filter(item -> {
								try {
									String positionNo = item.getPositionNo();
									return positionNo == null ||positionNo == "-" || positionNo == "0" ||positionNo.isEmpty() ||
											Integer.parseInt(positionNo) == filterPosition;
								} catch (NumberFormatException | NullPointerException e) {
									return false;
								}
							})
							.collect(Collectors.toList());
				} else {
					// Handle unexpected cases
					filteredDataList = new ArrayList<>();
				}

				ref_tvTestStatus.getItems().clear();

				// Print the filtered list
				filteredDataList.forEach(item -> 
				//ApplicationLauncher.logger.error("Filtered List Item: " + item)
				ref_tvTestStatus.getItems().add(item)
						);

				filteredDataList.clear();

				ApplicationLauncher.setCursor(Cursor.DEFAULT);
				ref_btnFilter.setDisable(false);
			});
		}
	}

	//=== L O A D  S A M P L E  D A T A ======================================================

	@FXML 
	public void btnSampleDataOnClick(){
		ApplicationLauncher.logger.debug("btnSampleDataOnClick Invoked:");

		TestInterfaceStatus testIntefaceStatus = new TestInterfaceStatus();

		Random random = new Random();

		for (int j = 0; j < 15; j++) {
			int i = random.nextInt(7);
			testIntefaceStatus = new TestInterfaceStatus(
					"" + i + "BAY",
					"" + i + "Seq",
					"" + i + "DEVICE",
					"P" + i,
					"" + i,
					"-",
					"(" + i + ")",						
					ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
					"Waiting",
					ConstantConveyor.COMM_EXECUTION_STATUS_INP
					);
			allData.add(testIntefaceStatus) ;
			int newRecordSerialNo = addToTestStatusGui(testIntefaceStatus);
			testIntefaceStatus.setSerialNo(String.valueOf(newRecordSerialNo));
		}
	}
	
	//=== U S E R  F R I E N D L Y  P R O M P T S ======================================================

	
	@FXML
    void CalibPlaceOnClick() {
		ConstantConveyor.setCALIB_OPTICAL_PLACED(true);
    }

    @FXML
    void CalibRemoveOnClick() {
    	ConstantConveyor.setCALIB_OPTICAL_REMOVED(true);
    }
    
    @FXML
    void CalibCurrentStableOnClick() {
    	ConstantConveyor.setCALIB_CURRENT_STABLE(true);
    }

    @FXML
    void FtPlaceOnClick() {
    	ConstantConveyor.setFT_OPTICAL_PLACED(true);
    }

    @FXML
    void FtRemoveOnClick() {
    	ConstantConveyor.setFT_OPTICAL_REMOVED(true);
    }
    
    @FXML
    void FtLDUOKOnClick() {
    	ConstantConveyor.setFT_LDU_PLACEMENT(true);
    }

    @FXML
    void VerificDoneOnClick() {
    	ConstantConveyor.setVERIFIC_TESTING_DONE(true);
    }
    
    // M O T O R  C O N T R O L  F U N C T I O N S
    
    @FXML
    void OffMotor1OnClick() {
        controlOutput(ConstantBayPortNameMapping.MOTOR_1_CONTROL, OFF);
        bayUtils.set_motor_not_required(ConstantConveyor.FT_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.HV_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.IR_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.CALIBRATION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.COMMUNICATION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.LOADING_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.REJECTION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD1_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD2_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.WAITING_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.VERIFICATION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.UNLOADING_BAY_KEY);
    }

    @FXML
    void OffMotor2OnClick() {
        controlOutput(ConstantBayPortNameMapping.MOTOR_2_CONTROL, OFF);
        bayUtils.set_motor_not_required(ConstantConveyor.FT_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.HV_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.IR_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.CALIBRATION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.COMMUNICATION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.LOADING_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.REJECTION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD1_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD2_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.WAITING_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.VERIFICATION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.UNLOADING_BAY_KEY);
    }

    @FXML
    void OffMotor3OnClick() {
        controlOutput(ConstantBayPortNameMapping.MOTOR_3_CONTROL, OFF);
        bayUtils.set_motor_not_required(ConstantConveyor.FT_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.HV_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.IR_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.CALIBRATION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.COMMUNICATION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.LOADING_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.REJECTION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD1_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD2_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.WAITING_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.VERIFICATION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.UNLOADING_BAY_KEY);
    }

    @FXML
    void OffMotor4OnClick() {
        controlOutput(ConstantBayPortNameMapping.MOTOR_4_CONTROL, OFF);
        bayUtils.set_motor_not_required(ConstantConveyor.FT_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.HV_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.IR_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.CALIBRATION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.COMMUNICATION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.LOADING_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.REJECTION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD1_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD2_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.WAITING_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.VERIFICATION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.UNLOADING_BAY_KEY);
    }

    @FXML
    void OffMotor5OnClick() {
        controlOutput(ConstantBayPortNameMapping.MOTOR_5_CONTROL, OFF);
        bayUtils.set_motor_not_required(ConstantConveyor.FT_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.HV_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.IR_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.CALIBRATION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.COMMUNICATION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.LOADING_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.REJECTION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD1_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD2_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.WAITING_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.VERIFICATION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.UNLOADING_BAY_KEY);
    }

    @FXML
    void OffMotor6OnClick() {
        controlOutput(ConstantBayPortNameMapping.MOTOR_6_CONTROL, OFF);
        bayUtils.set_motor_not_required(ConstantConveyor.FT_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.HV_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.IR_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.CALIBRATION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.COMMUNICATION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.LOADING_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.REJECTION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD1_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD2_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.WAITING_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.VERIFICATION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.UNLOADING_BAY_KEY);
    }

    @FXML
    void OffMotor7OnClick() {
        controlOutput(ConstantBayPortNameMapping.MOTOR_7_CONTROL, OFF);
        bayUtils.set_motor_not_required(ConstantConveyor.FT_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.HV_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.IR_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.CALIBRATION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.COMMUNICATION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.LOADING_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.REJECTION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD1_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD2_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.WAITING_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.VERIFICATION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.UNLOADING_BAY_KEY);
    }

    @FXML
    void OffMotor8OnClick() {
        controlOutput(ConstantBayPortNameMapping.MOTOR_8_CONTROL, OFF);
        bayUtils.set_motor_not_required(ConstantConveyor.FT_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.HV_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.IR_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.CALIBRATION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.COMMUNICATION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.LOADING_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.REJECTION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD1_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD2_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.WAITING_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.VERIFICATION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.UNLOADING_BAY_KEY);
    }

    @FXML
    void OffMotor9OnClick() {
        controlOutput(ConstantBayPortNameMapping.MOTOR_9_CONTROL, OFF);
        bayUtils.set_motor_not_required(ConstantConveyor.FT_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.HV_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.IR_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.CALIBRATION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.COMMUNICATION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.LOADING_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.REJECTION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD1_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD2_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.WAITING_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.VERIFICATION_BAY_KEY);
        bayUtils.set_motor_not_required(ConstantConveyor.UNLOADING_BAY_KEY);
    }

    @FXML
    void OnMotor1OnClick() {
        controlOutput(ConstantBayPortNameMapping.MOTOR_1_CONTROL, ON);
    }

    @FXML
    void OnMotor2OnClick() {
        controlOutput(ConstantBayPortNameMapping.MOTOR_2_CONTROL, ON);
    }

    @FXML
    void OnMotor3OnClick() {
        controlOutput(ConstantBayPortNameMapping.MOTOR_3_CONTROL, ON);
    }

    @FXML
    void OnMotor4OnClick() {
        controlOutput(ConstantBayPortNameMapping.MOTOR_4_CONTROL, ON);
    }

    @FXML
    void OnMotor5OnClick() {
        controlOutput(ConstantBayPortNameMapping.MOTOR_5_CONTROL, ON);
    }

    @FXML
    void OnMotor6OnClick() {
        controlOutput(ConstantBayPortNameMapping.MOTOR_6_CONTROL, ON);
    }

    @FXML
    void OnMotor7OnClick() {
        controlOutput(ConstantBayPortNameMapping.MOTOR_7_CONTROL, ON);
    }

    @FXML
    void OnMotor8OnClick() {
        controlOutput(ConstantBayPortNameMapping.MOTOR_8_CONTROL, ON);
    }

    @FXML
    void OnMotor9OnClick() {
        controlOutput(ConstantBayPortNameMapping.MOTOR_9_CONTROL, ON);
    }

    
    private void controlOutput(String portNameKey, boolean shouldClose) {
		IoPortInfo portInfo = BayUtils.getOutputPortDetails(portNameKey);

		if (portInfo != null) {
			Ft.logger.debug("PortId    : " + portInfo.getPortId());
			Ft.logger.debug("ClusterId : " + portInfo.getClusterId());
			Ft.logger.debug("BayId     : " + portInfo.getBayId());

			String outputAction = shouldClose
					? Constant_IO_ActionMapping.ON   // Close = OFF->ON
							: Constant_IO_ActionMapping.OFF;  // Open  = ON->OFF

			if (ProcalFeatureEnable.MODBUS_PLC_SLAVE_MODE) {
				getBayUtils().setOutputDataToPlcBay(
						portInfo.getClusterId(),
						portInfo.getBayId(),
						portInfo.getPortId(),
						outputAction);
			} else {
				getBayUtils().setOutputDataToBay(
						portInfo.getClusterId(),
						portInfo.getBayId(),
						portInfo.getPortId(),
						outputAction);
			}
		}
	}
    // ==========================================================================================

	public static AtomicInteger getSerialNoTestStatusAtomic() {
		return serialNoTestStatusAtomic;
	}

	public void setSerialNoTestStatusAtomic(AtomicInteger serialNoTestStatusAtomic) {
		this.serialNoTestStatusAtomic = serialNoTestStatusAtomic;
	}

	public static TextField getRef_tf_CALIB_prompt() {
		return ref_tf_CALIB_prompt;
	}

	public static void setRef_tf_CALIB_prompt(TextField ref_tf_CALIB_prompt) {
		StateExecutorController.ref_tf_CALIB_prompt = ref_tf_CALIB_prompt;
	}

	public static TextField getRef_tf_FT_prompt() {
		return ref_tf_FT_prompt;
	}

	public static void setRef_tf_FT_prompt(TextField ref_tf_FT_prompt) {
		StateExecutorController.ref_tf_FT_prompt = ref_tf_FT_prompt;
	}

	public static Button getRef_btn_CalibPlace() {
		return ref_btn_CalibPlace;
	}

	public static void setRef_btn_CalibPlace(Button ref_btn_CalibPlace) {
		StateExecutorController.ref_btn_CalibPlace = ref_btn_CalibPlace;
	}

	public static Button getRef_btn_CalibRemove() {
		return ref_btn_CalibRemove;
	}

	public static void setRef_btn_CalibRemove(Button ref_btn_CalibRemove) {
		StateExecutorController.ref_btn_CalibRemove = ref_btn_CalibRemove;
	}

	public static Button getRef_btn_FtPlace() {
		return ref_btn_FtPlace;
	}

	public static void setRef_btn_FtPlace(Button ref_btn_FtPlace) {
		StateExecutorController.ref_btn_FtPlace = ref_btn_FtPlace;
	}

	public static Button getRef_btn_FtRemove() {
		return ref_btn_FtRemove;
	}

	public static void setRef_btn_FtRemove(Button ref_btn_FtRemove) {
		StateExecutorController.ref_btn_FtRemove = ref_btn_FtRemove;
	}

	public static Button getRef_btn_CalibCurrentStable() {
		return ref_btn_CalibCurrentStable;
	}

	public static void setRef_btn_CalibCurrentStable(Button ref_btn_CalibCurrentStable) {
		StateExecutorController.ref_btn_CalibCurrentStable = ref_btn_CalibCurrentStable;
	}

	public static Button getRef_btn_Ft_LDUOK() {
		return ref_btn_Ft_LDUOK;
	}

	public static void setRef_btn_Ft_LDUOK(Button ref_btn_Ft_LDUOK) {
		StateExecutorController.ref_btn_Ft_LDUOK = ref_btn_Ft_LDUOK;
	}

	public static Button getRef_btn_VerificDone() {
		return ref_btn_VerificDone;
	}

	public static void setRef_btn_VerificDone(Button ref_btn_VerificDone) {
		StateExecutorController.ref_btn_VerificDone = ref_btn_VerificDone;
	}

	public BayUtils getBayUtils() {
		return bayUtils;
	}

	public void setBayUtils(BayUtils bayUtils) {
		this.bayUtils = bayUtils;
	}



}
