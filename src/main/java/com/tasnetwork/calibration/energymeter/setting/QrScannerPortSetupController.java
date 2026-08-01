package com.tasnetwork.calibration.energymeter.setting;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.conveyor.bay.NewlandQRCodeScanner;
import com.tasnetwork.calibration.conveyor.bay.configloader.Bay;
import com.tasnetwork.calibration.conveyor.bay.configloader.ClusterDetail;
import com.tasnetwork.calibration.conveyor.bay.configloader.QrScanner;
import com.tasnetwork.calibration.conveyor.bay.configloader.Terminal;
import com.tasnetwork.calibration.conveyor.bay.configloader.TerminalBayConfigModel;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.constant.ConstantQrScanner;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.database.MySQL_Controller;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.serial.director.QrScannerDirector;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmQrScanner;
import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.WindowManager;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.spring.orm.model.DeviceSetting;

import gnu.io.CommPortIdentifier;
import javafx.application.Platform;
//import SerialPort.Communicator;
//import application.Communicator;
//import SerialPort.KeybindingController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class QrScannerPortSetupController implements Initializable {

	private static TerminalBayConfigModel bayConfigModel = ConveyorDataManager.getTerminalBayConfig();
	private Map<String, ArrayList<String>> clusterBayNameListMap = new HashMap<String, ArrayList<String>>();
	private Map<String, String> clusterNameIdListMap = new HashMap<String, String>();
	private Map<String, String> clusterBayNameIdMap = new HashMap<String, String>();
	private Map<String, ArrayList<String>> clusterBayNamePositionListMap = new HashMap<String, ArrayList<String>>();
	private Map<String, String> clusterBayPositionNoCnameMap = new LinkedHashMap<String, String>();
	private Map<String, String> clusterBayPositionNoDeviceIdMap = new LinkedHashMap<String, String>();

	Timer qr1ClusterSelectionOnChangeTimer;
	Timer qr2ClusterSelectionOnChangeTimer;
	Timer qr3ClusterSelectionOnChangeTimer;
	Timer qr4ClusterSelectionOnChangeTimer;
	Timer qr5ClusterSelectionOnChangeTimer;
	Timer qr6ClusterSelectionOnChangeTimer;
	Timer qr7ClusterSelectionOnChangeTimer;
	Timer qr8ClusterSelectionOnChangeTimer;
	Timer qr9ClusterSelectionOnChangeTimer;
	Timer qr10ClusterSelectionOnChangeTimer;
	Timer qr11ClusterSelectionOnChangeTimer;
	Timer qr12ClusterSelectionOnChangeTimer;
	Timer qr13ClusterSelectionOnChangeTimer;

	Timer qr1BaySelectionOnChangeTimer;
	Timer qr2BaySelectionOnChangeTimer;
	Timer qr3BaySelectionOnChangeTimer;
	Timer qr4BaySelectionOnChangeTimer;
	Timer qr5BaySelectionOnChangeTimer;
	Timer qr6BaySelectionOnChangeTimer;
	Timer qr7BaySelectionOnChangeTimer;
	Timer qr8BaySelectionOnChangeTimer;
	Timer qr9BaySelectionOnChangeTimer;
	Timer qr10BaySelectionOnChangeTimer;
	Timer qr11BaySelectionOnChangeTimer;
	Timer qr12BaySelectionOnChangeTimer;
	Timer qr13BaySelectionOnChangeTimer;

	Timer qr1PositionIdOnChangeTimer;
	Timer qr2PositionIdOnChangeTimer;
	Timer qr3PositionIdOnChangeTimer;
	Timer qr4PositionIdOnChangeTimer;
	Timer qr5PositionIdOnChangeTimer;
	Timer qr6PositionIdOnChangeTimer;
	Timer qr7PositionIdOnChangeTimer;
	Timer qr8PositionIdOnChangeTimer;
	Timer qr9PositionIdOnChangeTimer;
	Timer qr10PositionIdOnChangeTimer;
	Timer qr11PositionIdOnChangeTimer;
	Timer qr12PositionIdOnChangeTimer;
	Timer qr13PositionIdOnChangeTimer;
	/*
	 * @FXML
	 * private ComboBox<Integer> cmbBxPowerSrcBaudRate;
	 * 
	 * @FXML
	 * private ComboBox<Integer> cmbBxRefStdBaudRate;
	 */

	@FXML
	private Button btn_Save;
	

	// @FXML
	// private Button btnValidatePwrSrcCmd;
	// private static Button ref_btnValidatePwrSrcCmd;

	// @FXML
	// private Button btnValidateRefStdCmd;
	// private static Button ref_btnValidateRefStdCmd;

	@FXML
	private ComboBox<String> cmbBxQr1ClusterId;
	@FXML
	private ComboBox<String> cmbBxQr2ClusterId;
	@FXML
	private ComboBox<String> cmbBxQr3ClusterId;
	@FXML
	private ComboBox<String> cmbBxQr4ClusterId;
	@FXML
	private ComboBox<String> cmbBxQr5ClusterId;
	@FXML
	private ComboBox<String> cmbBxQr6ClusterId;

	@FXML
	private ComboBox<String> cmbBxQr7ClusterId;
	@FXML
	private ComboBox<String> cmbBxQr8ClusterId;
	@FXML
	private ComboBox<String> cmbBxQr9ClusterId;
	@FXML
	private ComboBox<String> cmbBxQr10ClusterId;
	@FXML
	private ComboBox<String> cmbBxQr11ClusterId;
	@FXML
	private ComboBox<String> cmbBxQr12ClusterId;
	@FXML
	private ComboBox<String> cmbBxQr13ClusterId;

	@FXML
	private ComboBox<String> cmbBxQr1BayId;
	@FXML
	private ComboBox<String> cmbBxQr2BayId;
	@FXML
	private ComboBox<String> cmbBxQr3BayId;
	@FXML
	private ComboBox<String> cmbBxQr4BayId;
	@FXML
	private ComboBox<String> cmbBxQr5BayId;
	@FXML
	private ComboBox<String> cmbBxQr6BayId;
	@FXML
	private ComboBox<String> cmbBxQr7BayId;
	@FXML
	private ComboBox<String> cmbBxQr8BayId;
	@FXML
	private ComboBox<String> cmbBxQr9BayId;
	@FXML
	private ComboBox<String> cmbBxQr10BayId;
	@FXML
	private ComboBox<String> cmbBxQr11BayId;
	@FXML
	private ComboBox<String> cmbBxQr12BayId;
	@FXML
	private ComboBox<String> cmbBxQr13BayId;

	@FXML
	private ComboBox<String> cmbBxQr1PositionId;
	@FXML
	private ComboBox<String> cmbBxQr2PositionId;
	@FXML
	private ComboBox<String> cmbBxQr3PositionId;
	@FXML
	private ComboBox<String> cmbBxQr4PositionId;
	@FXML
	private ComboBox<String> cmbBxQr5PositionId;
	@FXML
	private ComboBox<String> cmbBxQr6PositionId;
	@FXML
	private ComboBox<String> cmbBxQr7PositionId;
	@FXML
	private ComboBox<String> cmbBxQr8PositionId;
	@FXML
	private ComboBox<String> cmbBxQr9PositionId;
	@FXML
	private ComboBox<String> cmbBxQr10PositionId;
	@FXML
	private ComboBox<String> cmbBxQr11PositionId;
	@FXML
	private ComboBox<String> cmbBxQr12PositionId;
	@FXML
	private ComboBox<String> cmbBxQr13PositionId;

	private static ComboBox<String> ref_cmbBxQr1ClusterId;
	private static ComboBox<String> ref_cmbBxQr2ClusterId;
	private static ComboBox<String> ref_cmbBxQr3ClusterId;
	private static ComboBox<String> ref_cmbBxQr4ClusterId;
	private static ComboBox<String> ref_cmbBxQr5ClusterId;
	private static ComboBox<String> ref_cmbBxQr6ClusterId;
	private static ComboBox<String> ref_cmbBxQr7ClusterId;
	private static ComboBox<String> ref_cmbBxQr8ClusterId;
	private static ComboBox<String> ref_cmbBxQr9ClusterId;
	private static ComboBox<String> ref_cmbBxQr10ClusterId;
	private static ComboBox<String> ref_cmbBxQr11ClusterId;
	private static ComboBox<String> ref_cmbBxQr12ClusterId;
	private static ComboBox<String> ref_cmbBxQr13ClusterId;

	private static ComboBox<String> ref_cmbBxQr1BayId;
	private static ComboBox<String> ref_cmbBxQr2BayId;
	private static ComboBox<String> ref_cmbBxQr3BayId;
	private static ComboBox<String> ref_cmbBxQr4BayId;
	private static ComboBox<String> ref_cmbBxQr5BayId;
	private static ComboBox<String> ref_cmbBxQr6BayId;
	private static ComboBox<String> ref_cmbBxQr7BayId;
	private static ComboBox<String> ref_cmbBxQr8BayId;
	private static ComboBox<String> ref_cmbBxQr9BayId;
	private static ComboBox<String> ref_cmbBxQr10BayId;
	private static ComboBox<String> ref_cmbBxQr11BayId;
	private static ComboBox<String> ref_cmbBxQr12BayId;
	private static ComboBox<String> ref_cmbBxQr13BayId;

	private static ComboBox<String> ref_cmbBxQr1PositionId;
	private static ComboBox<String> ref_cmbBxQr2PositionId;
	private static ComboBox<String> ref_cmbBxQr3PositionId;
	private static ComboBox<String> ref_cmbBxQr4PositionId;
	private static ComboBox<String> ref_cmbBxQr5PositionId;
	private static ComboBox<String> ref_cmbBxQr6PositionId;
	private static ComboBox<String> ref_cmbBxQr7PositionId;
	private static ComboBox<String> ref_cmbBxQr8PositionId;
	private static ComboBox<String> ref_cmbBxQr9PositionId;
	private static ComboBox<String> ref_cmbBxQr10PositionId;
	private static ComboBox<String> ref_cmbBxQr11PositionId;
	private static ComboBox<String> ref_cmbBxQr12PositionId;
	private static ComboBox<String> ref_cmbBxQr13PositionId;

	@FXML
	private TextField txtQr1Cname;
	@FXML
	private TextField txtQr2Cname;
	@FXML
	private TextField txtQr3Cname;
	@FXML
	private TextField txtQr4Cname;
	@FXML
	private TextField txtQr5Cname;
	@FXML
	private TextField txtQr6Cname;
	@FXML
	private TextField txtQr7Cname;
	@FXML
	private TextField txtQr8Cname;
	@FXML
	private TextField txtQr9Cname;
	@FXML
	private TextField txtQr10Cname;
	@FXML
	private TextField txtQr11Cname;
	@FXML
	private TextField txtQr12Cname;
	@FXML
	private TextField txtQr13Cname;

	private static TextField ref_txtQr1Cname;
	private static TextField ref_txtQr2Cname;
	private static TextField ref_txtQr3Cname;
	private static TextField ref_txtQr4Cname;
	private static TextField ref_txtQr5Cname;
	private static TextField ref_txtQr6Cname;
	private static TextField ref_txtQr7Cname;
	private static TextField ref_txtQr8Cname;
	private static TextField ref_txtQr9Cname;
	private static TextField ref_txtQr10Cname;
	private static TextField ref_txtQr11Cname;
	private static TextField ref_txtQr12Cname;
	private static TextField ref_txtQr13Cname;

	@FXML
	private ComboBox<Integer> cmbBxQr1_BaudRate;
	@FXML
	private ComboBox<Integer> cmbBxQr2_BaudRate;
	@FXML
	private ComboBox<Integer> cmbBxQr3_BaudRate;
	@FXML
	private ComboBox<Integer> cmbBxQr4_BaudRate;
	@FXML
	private ComboBox<Integer> cmbBxQr5_BaudRate;
	@FXML
	private ComboBox<Integer> cmbBxQr6_BaudRate;

	@FXML
	private ComboBox<Integer> cmbBxQr7_BaudRate;
	@FXML
	private ComboBox<Integer> cmbBxQr8_BaudRate;
	@FXML
	private ComboBox<Integer> cmbBxQr9_BaudRate;
	@FXML
	private ComboBox<Integer> cmbBxQr10_BaudRate;
	@FXML
	private ComboBox<Integer> cmbBxQr11_BaudRate;
	@FXML
	private ComboBox<Integer> cmbBxQr12_BaudRate;
	@FXML
	private ComboBox<Integer> cmbBxQr13_BaudRate;

	static private ComboBox<Integer> ref_cmbBxQr1_BaudRate;
	static private ComboBox<Integer> ref_cmbBxQr2_BaudRate;
	static private ComboBox<Integer> ref_cmbBxQr3_BaudRate;
	static private ComboBox<Integer> ref_cmbBxQr4_BaudRate;
	static private ComboBox<Integer> ref_cmbBxQr5_BaudRate;
	static private ComboBox<Integer> ref_cmbBxQr6_BaudRate;

	static private ComboBox<Integer> ref_cmbBxQr7_BaudRate;
	static private ComboBox<Integer> ref_cmbBxQr8_BaudRate;
	static private ComboBox<Integer> ref_cmbBxQr9_BaudRate;
	static private ComboBox<Integer> ref_cmbBxQr10_BaudRate;
	static private ComboBox<Integer> ref_cmbBxQr11_BaudRate;
	static private ComboBox<Integer> ref_cmbBxQr12_BaudRate;
	static private ComboBox<Integer> ref_cmbBxQr13_BaudRate;

	/*
	 * @FXML private ComboBox<Integer> cmbBxLDU_BaudRate3;
	 * 
	 * @FXML private ComboBox<Integer> cmbBxLDU_BaudRate4;
	 * 
	 * @FXML private ComboBox<Integer> cmbBxLDU_BaudRate5;
	 * 
	 * @FXML private ComboBox<Integer> cmbBxLDU_BaudRate6;
	 * 
	 * @FXML private ComboBox<Integer> cmbBxLDU_BaudRate7;
	 */

	@FXML
	private ComboBox<String> cmbBxPowerSrcPortSelection;
	@FXML
	private ComboBox<String> cmbBxRefStdPortSelection;

	@FXML
	private ComboBox<String> cmbBxQr1_PortSelection;
	@FXML
	private ComboBox<String> cmbBxQr2_PortSelection;
	@FXML
	private ComboBox<String> cmbBxQr3_PortSelection;
	@FXML
	private ComboBox<String> cmbBxQr4_PortSelection;
	@FXML
	private ComboBox<String> cmbBxQr5_PortSelection;
	@FXML
	private ComboBox<String> cmbBxQr6_PortSelection;

	@FXML
	private ComboBox<String> cmbBxQr7_PortSelection;
	@FXML
	private ComboBox<String> cmbBxQr8_PortSelection;
	@FXML
	private ComboBox<String> cmbBxQr9_PortSelection;
	@FXML
	private ComboBox<String> cmbBxQr10_PortSelection;
	@FXML
	private ComboBox<String> cmbBxQr11_PortSelection;
	@FXML
	private ComboBox<String> cmbBxQr12_PortSelection;
	@FXML
	private ComboBox<String> cmbBxQr13_PortSelection;

	static private ComboBox<String> ref_cmbBxQr1_PortSelection;
	static private ComboBox<String> ref_cmbBxQr2_PortSelection;
	static private ComboBox<String> ref_cmbBxQr3_PortSelection;
	static private ComboBox<String> ref_cmbBxQr4_PortSelection;
	static private ComboBox<String> ref_cmbBxQr5_PortSelection;
	static private ComboBox<String> ref_cmbBxQr6_PortSelection;

	static private ComboBox<String> ref_cmbBxQr7_PortSelection;
	static private ComboBox<String> ref_cmbBxQr8_PortSelection;
	static private ComboBox<String> ref_cmbBxQr9_PortSelection;
	static private ComboBox<String> ref_cmbBxQr10_PortSelection;
	static private ComboBox<String> ref_cmbBxQr11_PortSelection;
	static private ComboBox<String> ref_cmbBxQr12_PortSelection;
	static private ComboBox<String> ref_cmbBxQr13_PortSelection;

	/*
	 * @FXML
	 * private ComboBox<String> cmbBxPowerSource_ModelName;
	 * 
	 * @FXML
	 * private ComboBox<String> cmbBxReferanceStd_ModelName;
	 */

	@FXML
	private ComboBox<String> cmbBxQr1_ModelName;
	@FXML
	private ComboBox<String> cmbBxQr2_ModelName;
	@FXML
	private ComboBox<String> cmbBxQr3_ModelName;
	@FXML
	private ComboBox<String> cmbBxQr4_ModelName;
	@FXML
	private ComboBox<String> cmbBxQr5_ModelName;
	@FXML
	private ComboBox<String> cmbBxQr6_ModelName;

	@FXML
	private ComboBox<String> cmbBxQr7_ModelName;
	@FXML
	private ComboBox<String> cmbBxQr8_ModelName;
	@FXML
	private ComboBox<String> cmbBxQr9_ModelName;
	@FXML
	private ComboBox<String> cmbBxQr10_ModelName;
	@FXML
	private ComboBox<String> cmbBxQr11_ModelName;
	@FXML
	private ComboBox<String> cmbBxQr12_ModelName;
	@FXML
	private ComboBox<String> cmbBxQr13_ModelName;

	static private ComboBox<String> ref_cmbBxQr1_ModelName;
	static private ComboBox<String> ref_cmbBxQr2_ModelName;
	static private ComboBox<String> ref_cmbBxQr3_ModelName;
	static private ComboBox<String> ref_cmbBxQr4_ModelName;
	static private ComboBox<String> ref_cmbBxQr5_ModelName;
	static private ComboBox<String> ref_cmbBxQr6_ModelName;

	static private ComboBox<String> ref_cmbBxQr7_ModelName;
	static private ComboBox<String> ref_cmbBxQr8_ModelName;
	static private ComboBox<String> ref_cmbBxQr9_ModelName;
	static private ComboBox<String> ref_cmbBxQr10_ModelName;
	static private ComboBox<String> ref_cmbBxQr11_ModelName;
	static private ComboBox<String> ref_cmbBxQr12_ModelName;
	static private ComboBox<String> ref_cmbBxQr13_ModelName;

	@FXML
	private TextField txtValidatePwrSrcCmdStatus;
	@FXML
	private TextField txtValidateRefStdCmdStatus;

	@FXML
	private TextField txtQr1ReadData;
	@FXML
	private TextField txtQr2ReadData;
	@FXML
	private TextField txtQr3ReadData;
	@FXML
	private TextField txtQr4ReadData;
	@FXML
	private TextField txtQr5ReadData;
	@FXML
	private TextField txtQr6ReadData;
	@FXML
	private TextField txtQr7ReadData;
	@FXML
	private TextField txtQr8ReadData;
	@FXML
	private TextField txtQr9ReadData;
	@FXML
	private TextField txtQr10ReadData;
	@FXML
	private TextField txtQr11ReadData;
	@FXML
	private TextField txtQr12ReadData;
	@FXML
	private TextField txtQr13ReadData;

	static private TextField ref_txtQr1ReadData;
	static private TextField ref_txtQr2ReadData;
	static private TextField ref_txtQr3ReadData;
	static private TextField ref_txtQr4ReadData;
	static private TextField ref_txtQr5ReadData;
	static private TextField ref_txtQr6ReadData;

	@FXML
	private TextField txtValidateQr1_CmdStatus;
	@FXML
	private TextField txtValidateQr2_CmdStatus;
	@FXML
	private TextField txtValidateQr3_CmdStatus;
	@FXML
	private TextField txtValidateQr4_CmdStatus;
	@FXML
	private TextField txtValidateQr5_CmdStatus;
	@FXML
	private TextField txtValidateQr6_CmdStatus;

	@FXML
	private TextField txtValidateQr7_CmdStatus;
	@FXML
	private TextField txtValidateQr8_CmdStatus;
	@FXML
	private TextField txtValidateQr9_CmdStatus;
	@FXML
	private TextField txtValidateQr10_CmdStatus;
	@FXML
	private TextField txtValidateQr11_CmdStatus;
	@FXML
	private TextField txtValidateQr12_CmdStatus;
	@FXML
	private TextField txtValidateQr13_CmdStatus;

	static private TextField ref_txtValidateQr1_CmdStatus;
	static private TextField ref_txtValidateQr2_CmdStatus;
	static private TextField ref_txtValidateQr3_CmdStatus;
	static private TextField ref_txtValidateQr4_CmdStatus;
	static private TextField ref_txtValidateQr5_CmdStatus;
	static private TextField ref_txtValidateQr6_CmdStatus;

	@FXML
	private Button btnValidatePwrSrcCmd;
	@FXML
	private Button btnValidateRefStdCmd;

	@FXML
	private Button btnValidateQr1_Cmd;
	@FXML
	private Button btnValidateQr2_Cmd;
	@FXML
	private Button btnValidateQr3_Cmd;
	@FXML
	private Button btnValidateQr4_Cmd;
	@FXML
	private Button btnValidateQr5_Cmd;
	@FXML
	private Button btnValidateQr6_Cmd;
	@FXML
	private Button btnValidateQr7_Cmd;
	@FXML
	private Button btnValidateQr8_Cmd;
	@FXML
	private Button btnValidateQr9_Cmd;
	@FXML
	private Button btnValidateQr10_Cmd;
	@FXML
	private Button btnValidateQr11_Cmd;
	@FXML
	private Button btnValidateQr12_Cmd;
	@FXML
	private Button btnValidateQr13_Cmd;

	private static Button ref_btnValidateQr1_Cmd;
	private static Button ref_btnValidateQr2_Cmd;
	private static Button ref_btnValidateQr3_Cmd;
	private static Button ref_btnValidateQr4_Cmd;
	private static Button ref_btnValidateQr5_Cmd;
	private static Button ref_btnValidateQr6_Cmd;

	Timer PwrSrcValidateTimer;
	Timer RefStdValidateTimer;
	Timer qr1_ValidateTimer;
	Timer qr2_ValidateTimer;
	Timer qr3_ValidateTimer;
	Timer qr4_ValidateTimer;
	Timer qr5_ValidateTimer;
	Timer qr6_ValidateTimer;
	Timer LDU7_ValidateTimer;
	Timer LDU8_ValidateTimer;
	Timer LDU9_ValidateTimer;
	Timer LDU10_ValidateTimer;

	Timer LDU11_ValidateTimer;
	Timer LDU12_ValidateTimer;
	Timer LDU13_ValidateTimer;
	Timer LDU14_ValidateTimer;
	Timer LDU15_ValidateTimer;
	Timer LDU16_ValidateTimer;
	Timer LDU17_ValidateTimer;
	Timer LDU18_ValidateTimer;
	Timer LDU19_ValidateTimer;
	Timer LDU20_ValidateTimer;

	Timer LDU21_ValidateTimer;
	Timer LDU22_ValidateTimer;
	Timer LDU23_ValidateTimer;
	Timer LDU24_ValidateTimer;
	Timer LDU25_ValidateTimer;
	Timer LDU26_ValidateTimer;
	Timer LDU27_ValidateTimer;
	Timer LDU28_ValidateTimer;
	Timer LDU29_ValidateTimer;
	Timer LDU30_ValidateTimer;

	Timer LDU31_ValidateTimer;
	Timer LDU32_ValidateTimer;
	Timer LDU33_ValidateTimer;
	Timer LDU34_ValidateTimer;
	Timer LDU35_ValidateTimer;
	Timer LDU36_ValidateTimer;
	Timer LDU37_ValidateTimer;
	Timer LDU38_ValidateTimer;
	Timer LDU39_ValidateTimer;
	Timer LDU40_ValidateTimer;

	Timer LDU41_ValidateTimer;
	Timer LDU42_ValidateTimer;
	Timer LDU43_ValidateTimer;
	Timer LDU44_ValidateTimer;
	Timer LDU45_ValidateTimer;
	Timer LDU46_ValidateTimer;
	Timer LDU47_ValidateTimer;
	Timer LDU48_ValidateTimer;

	private static boolean PortValidationTurnedON = false;

	Timer UI_DisplayTimer = new Timer();
	UI_DisplayTimerTask UI_DisplayTimerTaskObj;

	MyRunnable myRunnable;
	Thread myRunnableThread;

	// public SerialDataManager serialDM_Obj = new SerialDataManager();
	ConveyorDataManager DisplayDataObj = new ConveyorDataManager();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		ref_assignment();
		/*
		 * Platform.runLater(() -> {
		 * enableBusyLoadingScreen();
		 * });
		 */
		initializeComPorts();

		UI_DisplayTimerTaskObj = new UI_DisplayTimerTask(txtValidateRefStdCmdStatus);
		myRunnable = new MyRunnable(txtValidateRefStdCmdStatus);
		disableGuiObjects();
		// SerialDataManager.setSkipCurrentTP_Execution(false);
		// disableBusyLoadingScreen();
		// if(ProcalFeatureEnable.USER_ACCESS_CONTROL_ENABLED){
		applyUacSettings();
		// }
	}

	private static void applyUacSettings() {

		ApplicationLauncher.logger.info("QrScannerPortSetupController : applyUacSettings :  Entry");
	}

	public void disableGuiObjects() {

	}

	public void ref_assignment() {
		ref_btnValidateQr1_Cmd = btnValidateQr1_Cmd;
		ref_btnValidateQr2_Cmd = btnValidateQr2_Cmd;
		ref_btnValidateQr3_Cmd = btnValidateQr3_Cmd;
		ref_btnValidateQr4_Cmd = btnValidateQr4_Cmd;
		ref_btnValidateQr5_Cmd = btnValidateQr5_Cmd;
		ref_btnValidateQr6_Cmd = btnValidateQr6_Cmd;

		ref_cmbBxQr1ClusterId = cmbBxQr1ClusterId;
		ref_cmbBxQr2ClusterId = cmbBxQr2ClusterId;
		ref_cmbBxQr3ClusterId = cmbBxQr3ClusterId;
		ref_cmbBxQr4ClusterId = cmbBxQr4ClusterId;
		ref_cmbBxQr5ClusterId = cmbBxQr5ClusterId;
		ref_cmbBxQr6ClusterId = cmbBxQr6ClusterId;
		ref_cmbBxQr7ClusterId = cmbBxQr7ClusterId;
		ref_cmbBxQr8ClusterId = cmbBxQr8ClusterId;
		ref_cmbBxQr9ClusterId = cmbBxQr9ClusterId;
		ref_cmbBxQr10ClusterId = cmbBxQr10ClusterId;
		ref_cmbBxQr11ClusterId = cmbBxQr11ClusterId;
		ref_cmbBxQr12ClusterId = cmbBxQr12ClusterId;
		ref_cmbBxQr13ClusterId = cmbBxQr13ClusterId;

		ref_cmbBxQr1BayId = cmbBxQr1BayId;
		ref_cmbBxQr2BayId = cmbBxQr2BayId;
		ref_cmbBxQr3BayId = cmbBxQr3BayId;
		ref_cmbBxQr4BayId = cmbBxQr4BayId;
		ref_cmbBxQr5BayId = cmbBxQr5BayId;
		ref_cmbBxQr6BayId = cmbBxQr6BayId;
		ref_cmbBxQr7BayId = cmbBxQr7BayId;
		ref_cmbBxQr8BayId = cmbBxQr8BayId;
		ref_cmbBxQr9BayId = cmbBxQr9BayId;
		ref_cmbBxQr10BayId = cmbBxQr10BayId;
		ref_cmbBxQr11BayId = cmbBxQr11BayId;
		ref_cmbBxQr12BayId = cmbBxQr12BayId;
		ref_cmbBxQr13BayId = cmbBxQr13BayId;

		ref_cmbBxQr1PositionId = cmbBxQr1PositionId;
		ref_cmbBxQr2PositionId = cmbBxQr2PositionId;
		ref_cmbBxQr3PositionId = cmbBxQr3PositionId;
		ref_cmbBxQr4PositionId = cmbBxQr4PositionId;
		ref_cmbBxQr5PositionId = cmbBxQr5PositionId;
		ref_cmbBxQr6PositionId = cmbBxQr6PositionId;
		ref_cmbBxQr7PositionId = cmbBxQr7PositionId;
		ref_cmbBxQr8PositionId = cmbBxQr8PositionId;
		ref_cmbBxQr9PositionId = cmbBxQr9PositionId;
		ref_cmbBxQr10PositionId = cmbBxQr10PositionId;
		ref_cmbBxQr11PositionId = cmbBxQr11PositionId;
		ref_cmbBxQr12PositionId = cmbBxQr12PositionId;
		ref_cmbBxQr13PositionId = cmbBxQr13PositionId;

		ref_txtQr1Cname = txtQr1Cname;
		ref_txtQr2Cname = txtQr2Cname;
		ref_txtQr3Cname = txtQr3Cname;
		ref_txtQr4Cname = txtQr4Cname;
		ref_txtQr5Cname = txtQr5Cname;
		ref_txtQr6Cname = txtQr6Cname;
		ref_txtQr7Cname = txtQr7Cname;
		ref_txtQr8Cname = txtQr8Cname;
		ref_txtQr9Cname = txtQr9Cname;
		ref_txtQr10Cname = txtQr10Cname;
		ref_txtQr11Cname = txtQr11Cname;
		ref_txtQr12Cname = txtQr12Cname;
		ref_txtQr13Cname = txtQr13Cname;

		ref_txtQr1ReadData = txtQr1ReadData;
		ref_txtQr2ReadData = txtQr2ReadData;
		ref_txtQr3ReadData = txtQr3ReadData;
		ref_txtQr4ReadData = txtQr4ReadData;
		ref_txtQr5ReadData = txtQr5ReadData;
		ref_txtQr6ReadData = txtQr6ReadData;

		ref_cmbBxQr1_PortSelection = cmbBxQr1_PortSelection;
		ref_cmbBxQr2_PortSelection = cmbBxQr2_PortSelection;
		ref_cmbBxQr3_PortSelection = cmbBxQr3_PortSelection;
		ref_cmbBxQr4_PortSelection = cmbBxQr4_PortSelection;
		ref_cmbBxQr5_PortSelection = cmbBxQr5_PortSelection;
		ref_cmbBxQr6_PortSelection = cmbBxQr6_PortSelection;
		ref_cmbBxQr7_PortSelection = cmbBxQr7_PortSelection;
		ref_cmbBxQr8_PortSelection = cmbBxQr8_PortSelection;
		ref_cmbBxQr9_PortSelection = cmbBxQr9_PortSelection;
		ref_cmbBxQr10_PortSelection = cmbBxQr10_PortSelection;
		ref_cmbBxQr11_PortSelection = cmbBxQr11_PortSelection;
		ref_cmbBxQr12_PortSelection = cmbBxQr12_PortSelection;
		ref_cmbBxQr13_PortSelection = cmbBxQr13_PortSelection;

		ref_cmbBxQr1_ModelName = cmbBxQr1_ModelName;
		ref_cmbBxQr2_ModelName = cmbBxQr2_ModelName;
		ref_cmbBxQr3_ModelName = cmbBxQr3_ModelName;
		ref_cmbBxQr4_ModelName = cmbBxQr4_ModelName;
		ref_cmbBxQr5_ModelName = cmbBxQr5_ModelName;
		ref_cmbBxQr6_ModelName = cmbBxQr6_ModelName;
		ref_cmbBxQr7_ModelName = cmbBxQr7_ModelName;
		ref_cmbBxQr8_ModelName = cmbBxQr8_ModelName;
		ref_cmbBxQr9_ModelName = cmbBxQr9_ModelName;
		ref_cmbBxQr10_ModelName = cmbBxQr10_ModelName;
		ref_cmbBxQr11_ModelName = cmbBxQr11_ModelName;
		ref_cmbBxQr12_ModelName = cmbBxQr12_ModelName;
		ref_cmbBxQr13_ModelName = cmbBxQr13_ModelName;

		ref_txtValidateQr1_CmdStatus = txtValidateQr1_CmdStatus;
		ref_txtValidateQr2_CmdStatus = txtValidateQr2_CmdStatus;
		ref_txtValidateQr3_CmdStatus = txtValidateQr3_CmdStatus;
		ref_txtValidateQr4_CmdStatus = txtValidateQr4_CmdStatus;
		ref_txtValidateQr5_CmdStatus = txtValidateQr5_CmdStatus;
		ref_txtValidateQr6_CmdStatus = txtValidateQr6_CmdStatus;

		ref_cmbBxQr1_BaudRate = cmbBxQr1_BaudRate;
		ref_cmbBxQr2_BaudRate = cmbBxQr2_BaudRate;
		ref_cmbBxQr3_BaudRate = cmbBxQr3_BaudRate;
		ref_cmbBxQr4_BaudRate = cmbBxQr4_BaudRate;
		ref_cmbBxQr5_BaudRate = cmbBxQr5_BaudRate;
		ref_cmbBxQr6_BaudRate = cmbBxQr6_BaudRate;
		ref_cmbBxQr7_BaudRate = cmbBxQr7_BaudRate;
		ref_cmbBxQr8_BaudRate = cmbBxQr8_BaudRate;
		ref_cmbBxQr9_BaudRate = cmbBxQr9_BaudRate;
		ref_cmbBxQr10_BaudRate = cmbBxQr10_BaudRate;
		ref_cmbBxQr11_BaudRate = cmbBxQr11_BaudRate;
		ref_cmbBxQr12_BaudRate = cmbBxQr12_BaudRate;
		ref_cmbBxQr13_BaudRate = cmbBxQr13_BaudRate;

	}

	public void initializeComPorts() {

		setupComPortsBaudRate();

		loadAvailableComPorts();

		// updatePowerSourceModel();
		// updateReferenceMeterModel();
		updateLDUModel();
		loadDataFromConfig();
		// loadQrScannerName();
		load_saved_device_settings();
	}

	public void loadDataFromConfig() {

		ApplicationLauncher.logger.debug("loadDataFromConfig-Q: Entry");
		// ref_cmbBxDut1ClusterId.getItems().clear();

		// ref_cmbBxDutClusterId1;
		// ref_cmbBxDutBayId1;

		// =======================================
		ref_cmbBxQr1ClusterId.getItems().clear();
		ref_cmbBxQr2ClusterId.getItems().clear();
		ref_cmbBxQr3ClusterId.getItems().clear();
		ref_cmbBxQr4ClusterId.getItems().clear();
		ref_cmbBxQr5ClusterId.getItems().clear();
		ref_cmbBxQr6ClusterId.getItems().clear();

		ref_cmbBxQr7ClusterId.getItems().clear();
		ref_cmbBxQr8ClusterId.getItems().clear();
		ref_cmbBxQr9ClusterId.getItems().clear();
		ref_cmbBxQr10ClusterId.getItems().clear();
		ref_cmbBxQr11ClusterId.getItems().clear();
		ref_cmbBxQr12ClusterId.getItems().clear();
		ref_cmbBxQr13ClusterId.getItems().clear();

		for (Terminal eachTerminal : getBayConfigModel().getTerminal()) {
			if (eachTerminal.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID)) {
				for (ClusterDetail eachClusterDetail : eachTerminal.getClusterDetails()) {
					ref_cmbBxQr1ClusterId.getItems().add(eachClusterDetail.getName());
					ref_cmbBxQr2ClusterId.getItems().add(eachClusterDetail.getName());
					ref_cmbBxQr3ClusterId.getItems().add(eachClusterDetail.getName());
					ref_cmbBxQr4ClusterId.getItems().add(eachClusterDetail.getName());
					ref_cmbBxQr5ClusterId.getItems().add(eachClusterDetail.getName());
					ref_cmbBxQr6ClusterId.getItems().add(eachClusterDetail.getName());
					ref_cmbBxQr7ClusterId.getItems().add(eachClusterDetail.getName());
					ref_cmbBxQr8ClusterId.getItems().add(eachClusterDetail.getName());
					ref_cmbBxQr9ClusterId.getItems().add(eachClusterDetail.getName());
					ref_cmbBxQr10ClusterId.getItems().add(eachClusterDetail.getName());
					ref_cmbBxQr11ClusterId.getItems().add(eachClusterDetail.getName());
					ref_cmbBxQr12ClusterId.getItems().add(eachClusterDetail.getName());
					ref_cmbBxQr13ClusterId.getItems().add(eachClusterDetail.getName());

					getClusterNameIdListMap().put(eachClusterDetail.getName(), eachClusterDetail.getClusterId());
					ArrayList<String> bayList = new ArrayList<String>();
					getClusterBayNameListMap().put(eachClusterDetail.getName(), bayList);

					for (Bay eachBay : eachClusterDetail.getBay()) {

						// ref_cmbBxBaySelection.getItems().add(eachBay.getBayName());
						bayList.add(eachBay.getBayName());
						getClusterBayNameListMap().put(eachClusterDetail.getName(), bayList);
						Map<String, String> bayNameIdMap = new HashMap<String, String>();
						bayNameIdMap.put(eachBay.getBayName(), eachBay.getBayId());
						getClusterBayNameIdMap().put(eachClusterDetail.getName() + "_" + eachBay.getBayName(),
								eachBay.getBayId());
						// getClusterBayNameIdMap().put(eachClusterDetail.getName(), bayNameIdMap);
						// ApplicationLauncher.logger.debug("loadDataFromConfig :
						// getClusterBayNameIdMap().get(clusterName)-1 :"+ getClusterBayNameIdMap());

					}
				}

			}

		}

		for (Terminal eachTerminal : getBayConfigModel().getTerminal()) {
			String clusterId = "";
			String bayId = "";
			if (eachTerminal.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID)) {
				for (ClusterDetail eachClusterDetail : eachTerminal.getClusterDetails()) {
					clusterId = getClusterNameIdListMap().get(eachClusterDetail.getName());
					for (Bay eachBay : eachClusterDetail.getBay()) {
						bayId = getClusterBayNameIdMap().get(eachClusterDetail.getName() + "_" + eachBay.getBayName());
						ArrayList<String> positionNoList = new ArrayList<String>();
						for (QrScanner eachQrDevice : eachTerminal.getQrScanner()) {
							if ((eachQrDevice.getClusterId().equals(clusterId))
									&& (eachQrDevice.getBayId().equals(bayId))) {
								// getClusterBayNamePositionListMap().put
								positionNoList.add(eachQrDevice.getPositionId());
								getClusterBayPositionNoCnameMap().put(
										eachClusterDetail.getName() + "_" + eachBay.getBayName() + "_"
												+ eachQrDevice.getPositionId(),
										eachQrDevice.getPortName());
								getClusterBayPositionNoDeviceIdMap().put(
										eachClusterDetail.getName() + "_" + eachBay.getBayName() + "_"
												+ eachQrDevice.getPositionId(),
										eachQrDevice.getDeviceId());

							}

						}
						if (positionNoList.size() > 0) {
							getClusterBayNamePositionListMap()
									.put(eachClusterDetail.getName() + "_" + eachBay.getBayName(), positionNoList);
						}

					}

				}

			}
		}

		getClusterBayPositionNoCnameMap().entrySet().forEach((e) -> {
			ApplicationLauncher.logger.debug("loadDataFromConfig-1: getClusterBayPositionNoCnameMap: key : "
					+ e.getKey() + " -> " + e.getValue());
		});

		getClusterBayPositionNoDeviceIdMap().entrySet().forEach((e) -> {
			ApplicationLauncher.logger.debug("loadDataFromConfig-Qr1: getClusterBayPositionNoDeviceIdMap: key : "
					+ e.getKey() + " -> " + e.getValue());
		});

		if (ref_cmbBxQr1ClusterId.getItems().size() > 0) {
			ref_cmbBxQr1ClusterId.getSelectionModel().select(0);
			ref_cmbBxQr2ClusterId.getSelectionModel().select(0);
			ref_cmbBxQr3ClusterId.getSelectionModel().select(0);
			ref_cmbBxQr4ClusterId.getSelectionModel().select(0);
			ref_cmbBxQr5ClusterId.getSelectionModel().select(0);
			ref_cmbBxQr6ClusterId.getSelectionModel().select(0);
			ref_cmbBxQr7ClusterId.getSelectionModel().select(0);
			ref_cmbBxQr8ClusterId.getSelectionModel().select(0);
			ref_cmbBxQr9ClusterId.getSelectionModel().select(0);
			ref_cmbBxQr10ClusterId.getSelectionModel().select(0);
			ref_cmbBxQr11ClusterId.getSelectionModel().select(0);
			ref_cmbBxQr12ClusterId.getSelectionModel().select(0);
			ref_cmbBxQr13ClusterId.getSelectionModel().select(0);

		}

		if (getClusterBayNameListMap().size() > 0) {
			if (getClusterBayNameListMap()
					.containsKey(ref_cmbBxQr1ClusterId.getSelectionModel().getSelectedItem().toString())) {
				ref_cmbBxQr1BayId.getItems().addAll(getClusterBayNameListMap()
						.get(ref_cmbBxQr1ClusterId.getSelectionModel().getSelectedItem().toString()));
				ref_cmbBxQr2BayId.getItems().addAll(getClusterBayNameListMap()
						.get(ref_cmbBxQr2ClusterId.getSelectionModel().getSelectedItem().toString()));
				ref_cmbBxQr3BayId.getItems().addAll(getClusterBayNameListMap()
						.get(ref_cmbBxQr3ClusterId.getSelectionModel().getSelectedItem().toString()));
				ref_cmbBxQr4BayId.getItems().addAll(getClusterBayNameListMap()
						.get(ref_cmbBxQr4ClusterId.getSelectionModel().getSelectedItem().toString()));
				ref_cmbBxQr5BayId.getItems().addAll(getClusterBayNameListMap()
						.get(ref_cmbBxQr5ClusterId.getSelectionModel().getSelectedItem().toString()));
				ref_cmbBxQr6BayId.getItems().addAll(getClusterBayNameListMap()
						.get(ref_cmbBxQr6ClusterId.getSelectionModel().getSelectedItem().toString()));
				ref_cmbBxQr7BayId.getItems().addAll(getClusterBayNameListMap()
						.get(ref_cmbBxQr3ClusterId.getSelectionModel().getSelectedItem().toString()));
				ref_cmbBxQr8BayId.getItems().addAll(getClusterBayNameListMap()
						.get(ref_cmbBxQr4ClusterId.getSelectionModel().getSelectedItem().toString()));
				ref_cmbBxQr9BayId.getItems().addAll(getClusterBayNameListMap()
						.get(ref_cmbBxQr5ClusterId.getSelectionModel().getSelectedItem().toString()));
				ref_cmbBxQr10BayId.getItems().addAll(getClusterBayNameListMap()
						.get(ref_cmbBxQr6ClusterId.getSelectionModel().getSelectedItem().toString()));
				ref_cmbBxQr11BayId.getItems().addAll(getClusterBayNameListMap()
						.get(ref_cmbBxQr1ClusterId.getSelectionModel().getSelectedItem().toString()));
				ref_cmbBxQr12BayId.getItems().addAll(getClusterBayNameListMap()
						.get(ref_cmbBxQr2ClusterId.getSelectionModel().getSelectedItem().toString()));
				ref_cmbBxQr13BayId.getItems().addAll(getClusterBayNameListMap()
						.get(ref_cmbBxQr3ClusterId.getSelectionModel().getSelectedItem().toString()));

			}
			// ref_cmbBxBaySelection.getSelectionModel().select(0);

		}
		if (ref_cmbBxQr1BayId.getItems().size() > 0) {

			ref_cmbBxQr1BayId.getSelectionModel().select(0);
			ref_cmbBxQr2BayId.getSelectionModel().select(0);
			ref_cmbBxQr3BayId.getSelectionModel().select(0);
			ref_cmbBxQr4BayId.getSelectionModel().select(0);
			ref_cmbBxQr5BayId.getSelectionModel().select(0);
			ref_cmbBxQr6BayId.getSelectionModel().select(0);

			ref_cmbBxQr7BayId.getSelectionModel().select(0);
			ref_cmbBxQr8BayId.getSelectionModel().select(0);
			ref_cmbBxQr9BayId.getSelectionModel().select(0);
			ref_cmbBxQr10BayId.getSelectionModel().select(0);
			ref_cmbBxQr11BayId.getSelectionModel().select(0);
			ref_cmbBxQr12BayId.getSelectionModel().select(0);
			ref_cmbBxQr13BayId.getSelectionModel().select(0);
			String clusterName = (ref_cmbBxQr1ClusterId.getSelectionModel().getSelectedItem().toString());
			String bayName = (ref_cmbBxQr1BayId.getSelectionModel().getSelectedItem().toString());

			String clusterId = getClusterNameIdListMap().get(clusterName);
			String bayId = getClusterBayNameIdMap().get(clusterName + "_" + bayName);
			// ApplicationLauncher.logger.debug("loadDataFromConfig :
			// getClusterBayNameIdMap().get(clusterName)-2 :"+
			// getClusterBayNameIdMap().get(clusterName));
			// String bayId = "11";//getClusterBayNameIdMap().get(clusterName).get(bayName);

			ApplicationLauncher.logger.debug("loadDataFromConfig : clusterId :" + clusterId);
			ApplicationLauncher.logger.debug("loadDataFromConfig : bayId :" + bayId);

			ArrayList<QrScanner> qrDeviceList = (ArrayList<QrScanner>) getBayConfigModel().getTerminal().stream()
					.filter(e1 -> e1.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID))
					.flatMap(terminal -> terminal.getQrScanner().stream())
					.filter(e2 -> e2.getBayId().equals(bayId))
					.collect(Collectors.toList());
			ArrayList<String> cNameList = new ArrayList<String>();
			for (QrScanner eachQrDevice : qrDeviceList) {

				ref_cmbBxQr1PositionId.getItems().add(eachQrDevice.getPositionId());
				ref_cmbBxQr2PositionId.getItems().add(eachQrDevice.getPositionId());
				ref_cmbBxQr3PositionId.getItems().add(eachQrDevice.getPositionId());
				ref_cmbBxQr4PositionId.getItems().add(eachQrDevice.getPositionId());
				ref_cmbBxQr5PositionId.getItems().add(eachQrDevice.getPositionId());
				ref_cmbBxQr6PositionId.getItems().add(eachQrDevice.getPositionId());

				ref_cmbBxQr7PositionId.getItems().add(eachQrDevice.getPositionId());
				ref_cmbBxQr8PositionId.getItems().add(eachQrDevice.getPositionId());
				ref_cmbBxQr9PositionId.getItems().add(eachQrDevice.getPositionId());
				ref_cmbBxQr10PositionId.getItems().add(eachQrDevice.getPositionId());
				ref_cmbBxQr11PositionId.getItems().add(eachQrDevice.getPositionId());
				ref_cmbBxQr12PositionId.getItems().add(eachQrDevice.getPositionId());
				ref_cmbBxQr13PositionId.getItems().add(eachQrDevice.getPositionId());

				cNameList.add(eachQrDevice.getPortName());
			}
			// ref_cmbBxQrCname1.getItems().addAll(cNameList);

			if (ref_cmbBxQr1PositionId.getItems().size() > 0) {
				ref_cmbBxQr1PositionId.getSelectionModel().select(0);
				ref_cmbBxQr2PositionId.getSelectionModel().select(0);
				ref_cmbBxQr3PositionId.getSelectionModel().select(0);
				ref_cmbBxQr4PositionId.getSelectionModel().select(0);
				ref_cmbBxQr5PositionId.getSelectionModel().select(0);
				ref_cmbBxQr6PositionId.getSelectionModel().select(0);
				ref_cmbBxQr7PositionId.getSelectionModel().select(0);
				ref_cmbBxQr8PositionId.getSelectionModel().select(0);
				ref_cmbBxQr9PositionId.getSelectionModel().select(0);
				ref_cmbBxQr10PositionId.getSelectionModel().select(0);
				ref_cmbBxQr11PositionId.getSelectionModel().select(0);
				ref_cmbBxQr12PositionId.getSelectionModel().select(0);
				ref_cmbBxQr13PositionId.getSelectionModel().select(0);
				// ref_cmbBxQrCname1.getSelectionModel().select(0);
				// ref_txtQrCname1.setText(arg0);
			}
			if (cNameList.size() > 0) {
				ref_txtQr1Cname.setText(cNameList.get(0));
				ref_txtQr2Cname.setText(cNameList.get(0));
				ref_txtQr3Cname.setText(cNameList.get(0));
				ref_txtQr4Cname.setText(cNameList.get(0));
				ref_txtQr5Cname.setText(cNameList.get(0));
				ref_txtQr6Cname.setText(cNameList.get(0));
				ref_txtQr7Cname.setText(cNameList.get(0));
				ref_txtQr8Cname.setText(cNameList.get(0));
				ref_txtQr9Cname.setText(cNameList.get(0));
				ref_txtQr10Cname.setText(cNameList.get(0));
				ref_txtQr11Cname.setText(cNameList.get(0));
				ref_txtQr12Cname.setText(cNameList.get(0));
				ref_txtQr13Cname.setText(cNameList.get(0));
			}

		}
	}

	public void loadQrScannerName() {

		ArrayList<String> ModelList = new ArrayList<String>();
		String ModelName = "";
		// ConstantApp MyPropertyObj= new ConstantApp();
		// ModelName = ConstantQrScanner.QR_SCANNER_MODEL;

		List<QrScanner> qrScannerList = ConveyorDataManager.getTerminalBayConfig().getTerminal().stream()
				.filter(e -> e.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID))
				.flatMap(terminal -> terminal.getQrScanner().stream())
				.collect(Collectors.toList());
		// .filter(p -> searchPortName.equals(p.getPortName()))
		// .findFirst();

		for (QrScanner eachScanner : qrScannerList) {
			ModelList.add(eachScanner.getPortName());
		}

		ref_txtQr1Cname.setText("");// .getItems().clear();
		ref_txtQr2Cname.setText("");// .getItems().clear();
		ref_txtQr3Cname.setText("");
		ref_txtQr4Cname.setText("");
		ref_txtQr5Cname.setText("");
		ref_txtQr6Cname.setText("");
		ref_txtQr7Cname.setText("");
		ref_txtQr8Cname.setText("");
		ref_txtQr9Cname.setText("");
		ref_txtQr10Cname.setText("");
		ref_txtQr11Cname.setText("");
		ref_txtQr12Cname.setText("");
		ref_txtQr13Cname.setText("");
		if (ModelList.size() > 0) {
			/*
			 * ref_txtQr1Cname.getItems().addAll(ModelList);
			 * ref_txtQr1Cname.getSelectionModel().select(0);
			 * 
			 * ref_txtQr2Cname.getItems().addAll(ModelList);
			 * ref_txtQr2Cname.getSelectionModel().select(0);
			 */
		}

	}

	public void guiQrRefresh(
			int qrNo,
			String modelKey,
			Object clusterIdObj,
			Object bayIdObj,
			Object positionIdObj,
			TextField c_nameObj,
			Object portNameObj,
			Object baudRateObj) {

		JSONObject saved_ldu_setting = MySQL_Controller.sp_getdevice_setting(modelKey);

		String selectedClusterName = "";

		ComboBox<String> clusterId = (ComboBox<String>) clusterIdObj;
		ComboBox<String> bayId = (ComboBox<String>) bayIdObj;
		ComboBox<String> positionId = (ComboBox<String>) positionIdObj;
		TextField c_name = (TextField) c_nameObj;
		ComboBox<String> portName = (ComboBox<String>) portNameObj;
		ComboBox<Integer> baudRate = (ComboBox<Integer>) baudRateObj;

		try {
			if (saved_ldu_setting.has("cluster_name")) {
				// ref_cmbBxDutCname1.setValue(saved_ldu1_setting.getString("c_name"));
				selectedClusterName = saved_ldu_setting.getString("cluster_name");
				clusterId.getSelectionModel().select(selectedClusterName);
				bayId.getItems().clear();
				bayId.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));

			} else {
				// ref_cmbBxQrCname1.setValue("");
				clusterId.getSelectionModel().select("No-ClusterId" + qrNo);
				ApplicationLauncher.logger.debug("load_saved_device_settings: qr" + qrNo
						+ " cluster_name  Selection: Data not retrieved from DB");

			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error(
					"load_saved_device_settings: qr" + qrNo + " cluster_name: JSONException-1:" + e.getMessage());
			// ref_cmbBxQrCname1.setValue("");
			clusterId.getSelectionModel().select("Cluster-Ex" + qrNo);
			ApplicationLauncher.logger
					.info("load_saved_device_settings: qr" + qrNo + " cluster_name:  Data not retrieved from database");

		}
		if (selectedClusterName != null) {
			String selectedBayName = "";

			try {
				if (saved_ldu_setting.has("bay_name")) {
					// ref_cmbBxQrCname1.setValue(saved_ldu1_setting.getString("c_name"));
					selectedBayName = saved_ldu_setting.getString("bay_name");
					bayId.getSelectionModel().select(selectedBayName);
					selectedClusterName = (String) clusterId.getSelectionModel().getSelectedItem();
					positionId.getItems().clear();
					positionId.getItems().addAll(
							getClusterBayNamePositionListMap().get(selectedClusterName + "_" + selectedBayName));

				} else {
					// ref_cmbBxQrCname1.setValue("");
					bayId.getSelectionModel().select("No-BayId" + qrNo);
					ApplicationLauncher.logger.debug("load_saved_device_settings: qr" + qrNo
							+ " bay_name  Selection: Data not retrieved from DB");

				}

			} catch (JSONException e) {

				e.printStackTrace();
				ApplicationLauncher.logger.error(
						"load_saved_device_settings: qr" + qrNo + " bay_name: JSONException-1:" + e.getMessage());
				// ref_cmbBxQrCname1.setValue("");
				bayId.getSelectionModel().select("Bay-Ex1");
				ApplicationLauncher.logger
						.info("load_saved_device_settings: qr" + qrNo + " bay_name:  Data not retrieved from database");

			}
			if (selectedBayName != null) {
				String selectedPositionNo = "";

				try {
					if (saved_ldu_setting.has("position_no")) {
						// ref_cmbBxQrCname1.setValue(saved_ldu1_setting.getString("c_name"));
						selectedPositionNo = saved_ldu_setting.getString("position_no");
						positionId.getSelectionModel().select(selectedPositionNo);
						/*
						 * selectedClusterName =
						 * (String)ref_cmbBxQr1ClusterId.getSelectionModel().getSelectedItem();
						 * ref_cmbBxQr1PositionId.getItems().clear();
						 * ref_cmbBxQr1PositionId.getItems().addAll(getClusterBayNamePositionListMap().
						 * get(selectedClusterName + "_"+selectedBayName));
						 */

					} else {
						// ref_cmbBxQrCname1.setValue("");
						positionId.getSelectionModel().select("No-PositionId" + qrNo);
						ApplicationLauncher.logger.debug("load_saved_device_settings: qr" + qrNo
								+ " position_no  Selection: Data not retrieved from DB");

					}

				} catch (JSONException e) {

					e.printStackTrace();
					ApplicationLauncher.logger.error("load_saved_device_settings: qr" + qrNo
							+ " position_no: JSONException-1:" + e.getMessage());
					// ref_cmbBxQrCname1.setValue("");
					positionId.getSelectionModel().select("PositionId-Ex" + qrNo);
					ApplicationLauncher.logger.info("load_saved_device_settings: qr" + qrNo
							+ " position_no:  Data not retrieved from database");

				}
				if (selectedPositionNo != null) {
					try {
						if (saved_ldu_setting.has("c_name")) {
							c_name.setText(saved_ldu_setting.getString("c_name"));
						} else {
							c_name.setText("");
							ApplicationLauncher.logger.debug("load_saved_device_settings: qr Port name" + qrNo
									+ " Selection: Data not retrieved from DB");

						}

					} catch (JSONException e) {

						e.printStackTrace();
						ApplicationLauncher.logger.error("load_saved_device_settings: qr_port_name" + qrNo
								+ ": JSONException-1:" + e.getMessage());
						c_name.setText("");
						ApplicationLauncher.logger.info("load_saved_device_settings: qr_port_name" + qrNo
								+ ":  Data not retrieved from database");

					}

					try {
						if (saved_ldu_setting.has("port_name")) {
							portName.setValue(saved_ldu_setting.getString("port_name"));
						} else {
							portName.setValue("");
							ApplicationLauncher.logger.info("load_saved_device_settings: LDU" + qrNo
									+ "_PortSelection: Data not retrieved from DB");

						}

					} catch (JSONException e) {

						e.printStackTrace();
						ApplicationLauncher.logger
								.error("load_saved_device_settings: JSONException5-1:" + e.getMessage());
						portName.setValue("");
						ApplicationLauncher.logger.info("load_saved_device_settings: LDU" + qrNo
								+ "_PortSelection: Data not retrieved from database");

					}

					try {
						if (saved_ldu_setting.has("baud_rate")) {
							baudRate.setValue(Integer.parseInt(saved_ldu_setting.getString("baud_rate")));
						} else {
							baudRate.setValue(9600);
							ApplicationLauncher.logger
									.info("load_saved_device_settings: LDU_BaudRate: Data not retrieved from DB");

						}
					} catch (JSONException e) {

						e.printStackTrace();
						ApplicationLauncher.logger
								.error("load_saved_device_settings: JSONException6-1:" + e.getMessage());
						baudRate.setValue(9600);
						ApplicationLauncher.logger
								.info("load_saved_device_settings: LDU_BaudRate: Data not retrieved from database");

					}
				}
			}
		}

	}

	public void guiRefreshQrV2(
			int qrNo,
			String modelKey,
			Object clusterIdObj,
			Object bayIdObj,
			Object positionIdObj,
			TextField c_nameObj,
			Object portNameObj,
			Object baudRateObj) {

		DeviceSetting savedQrDeviceSetting = MySqlServiceManager.getDeviceSettingService()
				.findFirstByDeviceTypeKey(modelKey);

		// JSONObject saved_ldu_setting =
		// MySQL_Controller.sp_getdevice_setting(modelKey);
		if (savedQrDeviceSetting != null) {
			String selectedClusterName = "";

			ComboBox<String> clusterId = (ComboBox<String>) clusterIdObj;
			ComboBox<String> bayId = (ComboBox<String>) bayIdObj;
			ComboBox<String> positionId = (ComboBox<String>) positionIdObj;
			TextField c_name = (TextField) c_nameObj;
			ComboBox<String> portName = (ComboBox<String>) portNameObj;
			ComboBox<Integer> baudRate = (ComboBox<Integer>) baudRateObj;

			try {
				// if(saved_ldu_setting.has("cluster_name")){
				// ref_cmbBxDutCname1.setValue(saved_ldu1_setting.getString("c_name"));
				selectedClusterName = savedQrDeviceSetting.getClusterName();// saved_ldu_setting.getString("cluster_name");
				clusterId.getSelectionModel().select(selectedClusterName);
				bayId.getItems().clear();
				bayId.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));

				/*
				 * } else {
				 * //ref_cmbBxQrCname1.setValue("");
				 * clusterId.getSelectionModel().select("No-ClusterId"+ qrNo);
				 * ApplicationLauncher.logger.debug("guiRefreshQrV2: qr"+ qrNo +
				 * " cluster_name  Selection: Data not retrieved from DB");
				 * 
				 * }
				 */

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger
						.error("guiRefreshQrV2: qr" + qrNo + " cluster_name: Exception-1:" + e.getMessage());
				// ref_cmbBxQrCname1.setValue("");
				clusterId.getSelectionModel().select("Cluster-Ex" + qrNo);
				ApplicationLauncher.logger
						.info("guiRefreshQrV2: qr" + qrNo + " cluster_name:  Data not retrieved from database");

			}

			String selectedBayName = "";

			try {
				// if(saved_ldu_setting.has("bay_name")){
				// ref_cmbBxQrCname1.setValue(saved_ldu1_setting.getString("c_name"));
				selectedBayName = savedQrDeviceSetting.getBayName();// saved_ldu_setting.getString("bay_name");
				ApplicationLauncher.logger.debug("guiRefreshQrV2: selectedBayName: " + selectedBayName);
				ApplicationLauncher.logger.debug("guiRefreshQrV2: selectedClusterName: " + selectedClusterName);
				bayId.getSelectionModel().select(selectedBayName);
				selectedClusterName = (String) clusterId.getSelectionModel().getSelectedItem();
				positionId.getItems().clear();
				positionId.getItems()
						.addAll(getClusterBayNamePositionListMap().get(selectedClusterName + "_" + selectedBayName));

				/*
				 * } else {
				 * //ref_cmbBxQrCname1.setValue("");
				 * bayId.getSelectionModel().select("No-BayId"+ qrNo);
				 * ApplicationLauncher.logger.debug("guiRefreshQrV2: qr"+ qrNo +
				 * " bay_name  Selection: Data not retrieved from DB");
				 * 
				 * }
				 */

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger
						.error("guiRefreshQrV2: qr" + qrNo + " bay_name: Exception-1:" + e.getMessage());
				// ref_cmbBxQrCname1.setValue("");
				bayId.getSelectionModel().select("Bay-Ex1");
				ApplicationLauncher.logger
						.info("guiRefreshQrV2: qr" + qrNo + " bay_name:  Data not retrieved from database");

			}

			String selectedPositionNo = "";

			try {
				// if(saved_ldu_setting.has("position_no")){
				// ref_cmbBxQrCname1.setValue(saved_ldu1_setting.getString("c_name"));
				selectedPositionNo = savedQrDeviceSetting.getPositionNo();// saved_ldu_setting.getString("position_no");
				positionId.getSelectionModel().select(selectedPositionNo);

				/*
				 * if(getClusterBayPositionNoAddressListMap().containsKey(selectedClusterName+
				 * "_"+selectedBayName + "_"+ selectedPositionNo)) {
				 * ref_cmbBxLdu1DeviceAddress.getItems().addAll(
				 * getClusterBayPositionNoAddressListMap().get(selectedClusterName+"_"+
				 * selectedBayName + "_"+ selectedPositionNo));
				 * ref_cmbBxLdu1DeviceAddress.getSelectionModel().select(0);
				 * }
				 */

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger
						.error("guiRefreshQrV2: qr" + qrNo + " position_no: Exception-1:" + e.getMessage());
				// ref_cmbBxQrCname1.setValue("");
				positionId.getSelectionModel().select("PositionId-Ex" + qrNo);
				ApplicationLauncher.logger
						.info("guiRefreshQrV2: qr" + qrNo + " position_no:  Data not retrieved from database");

			}

			try {
				// if(saved_ldu_setting.has("c_name")){
				c_name.setText(savedQrDeviceSetting.getCanName());// saved_ldu_setting.getString("c_name"));
				/*
				 * } else {
				 * c_name.setText("");
				 * ApplicationLauncher.logger.debug("guiRefreshQrV2: qr Port name"+ qrNo +
				 * " Selection: Data not retrieved from DB");
				 * 
				 * }
				 */

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger
						.error("guiRefreshQrV2: qr_port_name" + qrNo + ": Exception-1:" + e.getMessage());
				c_name.setText("");
				ApplicationLauncher.logger
						.info("guiRefreshQrV2: qr_port_name" + qrNo + ":  Data not retrieved from database");

			}

			try {
				// if(saved_ldu_setting.has("port_name")){
				portName.setValue(savedQrDeviceSetting.getPortName());// saved_ldu_setting.getString("port_name"));
				/*
				 * } else {
				 * portName.setValue("");
				 * ApplicationLauncher.logger.info("guiRefreshQrV2: LDU"+ qrNo +
				 * "_PortSelection: Data not retrieved from DB");
				 * 
				 * }
				 */

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger.error("guiRefreshQrV2: JSONException5-1:" + e.getMessage());
				portName.setValue("");
				ApplicationLauncher.logger
						.info("guiRefreshQrV2: Qr: " + qrNo + "_PortSelection: Data not retrieved from database");

			}

			try {
				// if(saved_ldu_setting.has("baud_rate")){
				baudRate.setValue(Integer.parseInt(savedQrDeviceSetting.getBaudRate()));// saved_ldu_setting.getString("baud_rate")));
				/*
				 * } else {
				 * baudRate.setValue(9600);
				 * ApplicationLauncher.logger.
				 * info("guiRefreshQrV2: LDU_BaudRate: Data not retrieved from DB");
				 * 
				 * }
				 */
			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger.error("guiRefreshQrV2: JSONException6-1:" + e.getMessage());
				baudRate.setValue(9600);
				ApplicationLauncher.logger.info("guiRefreshQrV2: Qr_BaudRate: Data not retrieved from database");

			}
		}

	}

	public void load_saved_device_settings() {

		// JSONObject saved_pwr_setting =
		// MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_POWER_SOURCE);
		// JSONObject saved_ref_setting =
		// MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_REF_STD);
		// JSONObject saved_ldu1_setting =
		// MySQL_Controller.sp_getdevice_setting(ConstantQrScanner.QR_SCANNER_TYPE_NEWLAND_1);
		// JSONObject saved_ldu2_setting =
		// MySQL_Controller.sp_getdevice_setting(ConstantQrScanner.QR_SCANNER_TYPE_NEWLAND_2);

		ArrayList<String> modelKeyList = new ArrayList<String>();
		modelKeyList.add(ConstantQrScanner.QR_SCANNER_TYPE_NEWLAND_1);
		modelKeyList.add(ConstantQrScanner.QR_SCANNER_TYPE_NEWLAND_2);
		modelKeyList.add(ConstantQrScanner.QR_SCANNER_TYPE_NEWLAND_3);
		modelKeyList.add(ConstantQrScanner.QR_SCANNER_TYPE_NEWLAND_4);
		modelKeyList.add(ConstantQrScanner.QR_SCANNER_TYPE_NEWLAND_5);
		modelKeyList.add(ConstantQrScanner.QR_SCANNER_TYPE_NEWLAND_6);

		modelKeyList.add(ConstantQrScanner.QR_SCANNER_TYPE_NEWLAND_7);
		modelKeyList.add(ConstantQrScanner.QR_SCANNER_TYPE_NEWLAND_8);
		modelKeyList.add(ConstantQrScanner.QR_SCANNER_TYPE_NEWLAND_9);
		modelKeyList.add(ConstantQrScanner.QR_SCANNER_TYPE_NEWLAND_10);

		modelKeyList.add(ConstantQrScanner.QR_SCANNER_TYPE_NEWLAND_11);
		modelKeyList.add(ConstantQrScanner.QR_SCANNER_TYPE_NEWLAND_12);
		modelKeyList.add(ConstantQrScanner.QR_SCANNER_TYPE_NEWLAND_13);

		ArrayList<Object> clusterIdList = new ArrayList<Object>();
		clusterIdList.add(ref_cmbBxQr1ClusterId);
		clusterIdList.add(ref_cmbBxQr2ClusterId);
		clusterIdList.add(ref_cmbBxQr3ClusterId);
		clusterIdList.add(ref_cmbBxQr4ClusterId);
		clusterIdList.add(ref_cmbBxQr5ClusterId);
		clusterIdList.add(ref_cmbBxQr6ClusterId);
		clusterIdList.add(ref_cmbBxQr7ClusterId);
		clusterIdList.add(ref_cmbBxQr8ClusterId);
		clusterIdList.add(ref_cmbBxQr9ClusterId);
		clusterIdList.add(ref_cmbBxQr10ClusterId);
		clusterIdList.add(ref_cmbBxQr11ClusterId);
		clusterIdList.add(ref_cmbBxQr12ClusterId);
		clusterIdList.add(ref_cmbBxQr13ClusterId);

		ArrayList<Object> bayIdList = new ArrayList<Object>();
		bayIdList.add(ref_cmbBxQr1BayId);
		bayIdList.add(ref_cmbBxQr2BayId);
		bayIdList.add(ref_cmbBxQr3BayId);
		bayIdList.add(ref_cmbBxQr4BayId);
		bayIdList.add(ref_cmbBxQr5BayId);
		bayIdList.add(ref_cmbBxQr6BayId);
		bayIdList.add(ref_cmbBxQr7BayId);
		bayIdList.add(ref_cmbBxQr8BayId);
		bayIdList.add(ref_cmbBxQr9BayId);
		bayIdList.add(ref_cmbBxQr10BayId);
		bayIdList.add(ref_cmbBxQr11BayId);
		bayIdList.add(ref_cmbBxQr12BayId);
		bayIdList.add(ref_cmbBxQr13BayId);

		ArrayList<Object> positionIdList = new ArrayList<Object>();
		positionIdList.add(ref_cmbBxQr1PositionId);
		positionIdList.add(ref_cmbBxQr2PositionId);
		positionIdList.add(ref_cmbBxQr3PositionId);
		positionIdList.add(ref_cmbBxQr4PositionId);
		positionIdList.add(ref_cmbBxQr5PositionId);
		positionIdList.add(ref_cmbBxQr6PositionId);
		positionIdList.add(ref_cmbBxQr7PositionId);
		positionIdList.add(ref_cmbBxQr8PositionId);
		positionIdList.add(ref_cmbBxQr9PositionId);
		positionIdList.add(ref_cmbBxQr10PositionId);
		positionIdList.add(ref_cmbBxQr11PositionId);
		positionIdList.add(ref_cmbBxQr12PositionId);
		positionIdList.add(ref_cmbBxQr13PositionId);

		ArrayList<TextField> CnameList = new ArrayList<TextField>();
		CnameList.add(ref_txtQr1Cname);
		CnameList.add(ref_txtQr2Cname);
		CnameList.add(ref_txtQr3Cname);
		CnameList.add(ref_txtQr4Cname);
		CnameList.add(ref_txtQr5Cname);
		CnameList.add(ref_txtQr6Cname);
		CnameList.add(ref_txtQr7Cname);
		CnameList.add(ref_txtQr8Cname);
		CnameList.add(ref_txtQr9Cname);
		CnameList.add(ref_txtQr10Cname);
		CnameList.add(ref_txtQr11Cname);
		CnameList.add(ref_txtQr12Cname);
		CnameList.add(ref_txtQr13Cname);

		ArrayList<Object> portNameList = new ArrayList<Object>();
		portNameList.add(ref_cmbBxQr1_PortSelection);
		portNameList.add(ref_cmbBxQr2_PortSelection);
		portNameList.add(ref_cmbBxQr3_PortSelection);
		portNameList.add(ref_cmbBxQr4_PortSelection);
		portNameList.add(ref_cmbBxQr5_PortSelection);
		portNameList.add(ref_cmbBxQr6_PortSelection);
		portNameList.add(ref_cmbBxQr7_PortSelection);
		portNameList.add(ref_cmbBxQr8_PortSelection);
		portNameList.add(ref_cmbBxQr9_PortSelection);
		portNameList.add(ref_cmbBxQr10_PortSelection);
		portNameList.add(ref_cmbBxQr11_PortSelection);
		portNameList.add(ref_cmbBxQr12_PortSelection);
		portNameList.add(ref_cmbBxQr13_PortSelection);

		ArrayList<Object> baudRateList = new ArrayList<Object>();
		baudRateList.add(ref_cmbBxQr1_BaudRate);
		baudRateList.add(ref_cmbBxQr2_BaudRate);
		baudRateList.add(ref_cmbBxQr3_BaudRate);
		baudRateList.add(ref_cmbBxQr4_BaudRate);
		baudRateList.add(ref_cmbBxQr5_BaudRate);
		baudRateList.add(ref_cmbBxQr6_BaudRate);
		baudRateList.add(ref_cmbBxQr7_BaudRate);
		baudRateList.add(ref_cmbBxQr8_BaudRate);
		baudRateList.add(ref_cmbBxQr9_BaudRate);
		baudRateList.add(ref_cmbBxQr10_BaudRate);
		baudRateList.add(ref_cmbBxQr11_BaudRate);
		baudRateList.add(ref_cmbBxQr12_BaudRate);
		baudRateList.add(ref_cmbBxQr13_BaudRate);

		/*
		 * for(int i = 0; i < modelKeyList.size(); i++) {
		 * guiQrRefresh(i + 1, modelKeyList.get(i),clusterIdList.get(i),
		 * bayIdList.get(i), positionIdList.get(i), CnameList.get(i),
		 * portNameList.get(i), baudRateList.get(i));
		 * }
		 */

		for (int i = 0; i < modelKeyList.size(); i++) {
			if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {
				guiRefreshQrV2(i + 1, modelKeyList.get(i), clusterIdList.get(i), bayIdList.get(i),
						positionIdList.get(i), CnameList.get(i), portNameList.get(i), baudRateList.get(i));

			} else {
				guiQrRefresh(i + 1, modelKeyList.get(i), clusterIdList.get(i), bayIdList.get(i), positionIdList.get(i),
						CnameList.get(i), portNameList.get(i), baudRateList.get(i));

			}
		}

		String selectedClusterName = "";
		String selectedBayName = "";
		String selectedPositionNo = "";

	}

	public static JSONObject get_device_settings(String InputSourceType) {

		JSONObject saved_pwr_setting = MySQL_Controller.sp_getdevice_setting(InputSourceType);
		return saved_pwr_setting;
	}

	public void setupComPortsBaudRate() {

		/*
		 * cmbBxPowerSrcBaudRate.getItems().clear();
		 * cmbBxPowerSrcBaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
		 * if(ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
		 * cmbBxPowerSrcBaudRate.getSelectionModel().select(ConstantMtePowerSource.
		 * PowerSrcDefaultBaudRate);
		 * }else if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
		 * cmbBxPowerSrcBaudRate.getSelectionModel().select(ConstantLscsPowerSource.
		 * PowerSrcDefaultBaudRate);
		 * }
		 * cmbBxRefStdBaudRate.getItems().clear();
		 * cmbBxRefStdBaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxRefStdBaudRate.getSelectionModel().select(ConstantRadiantRefStd.
		 * RefStdDefaultBaudRate);
		 */
		ref_cmbBxQr1_BaudRate.getItems().clear();
		ref_cmbBxQr1_BaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
		ref_cmbBxQr1_BaudRate.getSelectionModel().select(ConstantQrScanner.QR_SCANNER_DEFAULT_BAUD_RATE);

		ref_cmbBxQr2_BaudRate.getItems().clear();
		ref_cmbBxQr2_BaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
		ref_cmbBxQr2_BaudRate.getSelectionModel().select(ConstantQrScanner.QR_SCANNER_DEFAULT_BAUD_RATE);

		ref_cmbBxQr3_BaudRate.getItems().clear();
		ref_cmbBxQr3_BaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
		ref_cmbBxQr3_BaudRate.getSelectionModel().select(ConstantQrScanner.QR_SCANNER_DEFAULT_BAUD_RATE);

		ref_cmbBxQr4_BaudRate.getItems().clear();
		ref_cmbBxQr4_BaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
		ref_cmbBxQr4_BaudRate.getSelectionModel().select(ConstantQrScanner.QR_SCANNER_DEFAULT_BAUD_RATE);

		ref_cmbBxQr5_BaudRate.getItems().clear();
		ref_cmbBxQr5_BaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
		ref_cmbBxQr5_BaudRate.getSelectionModel().select(ConstantQrScanner.QR_SCANNER_DEFAULT_BAUD_RATE);

		ref_cmbBxQr6_BaudRate.getItems().clear();
		ref_cmbBxQr6_BaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
		ref_cmbBxQr6_BaudRate.getSelectionModel().select(ConstantQrScanner.QR_SCANNER_DEFAULT_BAUD_RATE);

		ref_cmbBxQr7_BaudRate.getItems().clear();
		ref_cmbBxQr7_BaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
		ref_cmbBxQr7_BaudRate.getSelectionModel().select(ConstantQrScanner.QR_SCANNER_DEFAULT_BAUD_RATE);

		ref_cmbBxQr8_BaudRate.getItems().clear();
		ref_cmbBxQr8_BaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
		ref_cmbBxQr8_BaudRate.getSelectionModel().select(ConstantQrScanner.QR_SCANNER_DEFAULT_BAUD_RATE);

		ref_cmbBxQr9_BaudRate.getItems().clear();
		ref_cmbBxQr9_BaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
		ref_cmbBxQr9_BaudRate.getSelectionModel().select(ConstantQrScanner.QR_SCANNER_DEFAULT_BAUD_RATE);

		ref_cmbBxQr10_BaudRate.getItems().clear();
		ref_cmbBxQr10_BaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
		ref_cmbBxQr10_BaudRate.getSelectionModel().select(ConstantQrScanner.QR_SCANNER_DEFAULT_BAUD_RATE);

		ref_cmbBxQr11_BaudRate.getItems().clear();
		ref_cmbBxQr11_BaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
		ref_cmbBxQr11_BaudRate.getSelectionModel().select(ConstantQrScanner.QR_SCANNER_DEFAULT_BAUD_RATE);

		ref_cmbBxQr12_BaudRate.getItems().clear();
		ref_cmbBxQr12_BaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
		ref_cmbBxQr12_BaudRate.getSelectionModel().select(ConstantQrScanner.QR_SCANNER_DEFAULT_BAUD_RATE);

		ref_cmbBxQr13_BaudRate.getItems().clear();
		ref_cmbBxQr13_BaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
		ref_cmbBxQr13_BaudRate.getSelectionModel().select(ConstantQrScanner.QR_SCANNER_DEFAULT_BAUD_RATE);

	}

	public static boolean getPortValidationTurnedON() {
		return PortValidationTurnedON;
	}

	public static void setPortValidationTurnedON(boolean status) {
		PortValidationTurnedON = status;

	}

	public void loadAvailableComPorts() {

		// Stub for testing

		ArrayList<String> ModelList = new ArrayList<String>();
		String ModelName = "";
		ConstantApp MyPropertyObj = new ConstantApp();
		ModelName = ConstantConveyorConfig.REFSTD;
		ModelList.add("Com1");
		ModelList.add("Com2");
		ModelList.add("Com3");

		scanSerialPortAndUpdateDisplay();// dramesh
	}

	/*
	 * public void updatePowerSourceModel() {
	 * ArrayList<String> ModelList = new ArrayList<String>();
	 * String ModelName = "";
	 * ConstantApp MyPropertyObj= new ConstantApp();
	 * ModelName = ConstantConfig.POWERSRC;
	 * ModelList.add(ModelName);
	 * cmbBxPowerSource_ModelName.getItems().clear();
	 * cmbBxPowerSource_ModelName.getItems().addAll(ModelList);
	 * cmbBxPowerSource_ModelName.getSelectionModel().select(0);
	 * }
	 * 
	 * public void updateReferenceMeterModel() {
	 * ArrayList<String> ModelList = new ArrayList<String>();
	 * String ModelName = "";
	 * ConstantApp MyPropertyObj= new ConstantApp();
	 * ModelName = ConstantConfig.REFSTD;
	 * ModelList.add(ModelName);
	 * cmbBxReferanceStd_ModelName.getItems().clear();
	 * cmbBxReferanceStd_ModelName.getItems().addAll(ModelList);
	 * cmbBxReferanceStd_ModelName.getSelectionModel().select(0);
	 * }
	 */

	public void updateLDUModel() {
		ArrayList<String> ModelList = new ArrayList<String>();
		String ModelName = "";
		ConstantApp MyPropertyObj = new ConstantApp();
		ModelName = ConstantQrScanner.QR_SCANNER_MODEL;
		ModelList.add(ModelName);

		// ref_cmbBxQrScannerName.get

		ref_cmbBxQr1_ModelName.getItems().clear();
		ref_cmbBxQr1_ModelName.getItems().addAll(ModelList);
		ref_cmbBxQr1_ModelName.getSelectionModel().select(0);

		ref_cmbBxQr2_ModelName.getItems().clear();
		ref_cmbBxQr2_ModelName.getItems().addAll(ModelList);
		ref_cmbBxQr2_ModelName.getSelectionModel().select(0);

		ref_cmbBxQr3_ModelName.getItems().clear();
		ref_cmbBxQr3_ModelName.getItems().addAll(ModelList);
		ref_cmbBxQr3_ModelName.getSelectionModel().select(0);

		ref_cmbBxQr4_ModelName.getItems().clear();
		ref_cmbBxQr4_ModelName.getItems().addAll(ModelList);
		ref_cmbBxQr4_ModelName.getSelectionModel().select(0);

		ref_cmbBxQr5_ModelName.getItems().clear();
		ref_cmbBxQr5_ModelName.getItems().addAll(ModelList);
		ref_cmbBxQr5_ModelName.getSelectionModel().select(0);

		ref_cmbBxQr6_ModelName.getItems().clear();
		ref_cmbBxQr6_ModelName.getItems().addAll(ModelList);
		ref_cmbBxQr6_ModelName.getSelectionModel().select(0);

		ref_cmbBxQr7_ModelName.getItems().clear();
		ref_cmbBxQr7_ModelName.getItems().addAll(ModelList);
		ref_cmbBxQr7_ModelName.getSelectionModel().select(0);

		ref_cmbBxQr8_ModelName.getItems().clear();
		ref_cmbBxQr8_ModelName.getItems().addAll(ModelList);
		ref_cmbBxQr8_ModelName.getSelectionModel().select(0);

		ref_cmbBxQr9_ModelName.getItems().clear();
		ref_cmbBxQr9_ModelName.getItems().addAll(ModelList);
		ref_cmbBxQr9_ModelName.getSelectionModel().select(0);

		ref_cmbBxQr10_ModelName.getItems().clear();
		ref_cmbBxQr10_ModelName.getItems().addAll(ModelList);
		ref_cmbBxQr10_ModelName.getSelectionModel().select(0);

		ref_cmbBxQr11_ModelName.getItems().clear();
		ref_cmbBxQr11_ModelName.getItems().addAll(ModelList);
		ref_cmbBxQr11_ModelName.getSelectionModel().select(0);

		ref_cmbBxQr12_ModelName.getItems().clear();
		ref_cmbBxQr12_ModelName.getItems().addAll(ModelList);
		ref_cmbBxQr12_ModelName.getSelectionModel().select(0);

		ref_cmbBxQr13_ModelName.getItems().clear();
		ref_cmbBxQr13_ModelName.getItems().addAll(ModelList);
		ref_cmbBxQr13_ModelName.getSelectionModel().select(0);

	}

	public void scanSerialPortAndUpdateDisplay() {
        new Thread(() -> {
            java.util.List<String> availablePorts = new java.util.ArrayList<>();
            java.util.Enumeration sysPorts = CommPortIdentifier.getPortIdentifiers();
            while (sysPorts.hasMoreElements()) {
                CommPortIdentifier curPort = (CommPortIdentifier) sysPorts.nextElement();
                if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                    availablePorts.add(curPort.getName());
                }
            }
            
            javafx.application.Platform.runLater(() -> {
                // Mock enumeration to inject ports into original UI code
                java.util.Enumeration ports = java.util.Collections.enumeration(availablePorts);


		/*
		 * cmbBxPowerSrcPortSelection.getItems().clear();
		 * cmbBxRefStdPortSelection.getItems().clear();
		 */
		ref_cmbBxQr1_PortSelection.getItems().clear();
		ref_cmbBxQr2_PortSelection.getItems().clear();
		ref_cmbBxQr3_PortSelection.getItems().clear();
		ref_cmbBxQr4_PortSelection.getItems().clear();
		ref_cmbBxQr5_PortSelection.getItems().clear();
		ref_cmbBxQr6_PortSelection.getItems().clear();

		ref_cmbBxQr7_PortSelection.getItems().clear();
		ref_cmbBxQr8_PortSelection.getItems().clear();
		ref_cmbBxQr9_PortSelection.getItems().clear();
		ref_cmbBxQr10_PortSelection.getItems().clear();

		ref_cmbBxQr11_PortSelection.getItems().clear();
		ref_cmbBxQr12_PortSelection.getItems().clear();
		ref_cmbBxQr13_PortSelection.getItems().clear();

		// Enumeration ports is now provided above

		while (ports.hasMoreElements()) {
			String curPortName = (String) ports.nextElement();

			if (true) {
				/*
				 * cmbBxPowerSrcPortSelection.getItems().add(curPortName);
				 * cmbBxRefStdPortSelection.getItems().add(curPortName);
				 */
				ref_cmbBxQr1_PortSelection.getItems().add(curPortName);
				ref_cmbBxQr2_PortSelection.getItems().add(curPortName);
				ref_cmbBxQr3_PortSelection.getItems().add(curPortName);
				ref_cmbBxQr4_PortSelection.getItems().add(curPortName);
				ref_cmbBxQr5_PortSelection.getItems().add(curPortName);
				ref_cmbBxQr6_PortSelection.getItems().add(curPortName);
				ref_cmbBxQr7_PortSelection.getItems().add(curPortName);
				ref_cmbBxQr8_PortSelection.getItems().add(curPortName);
				ref_cmbBxQr9_PortSelection.getItems().add(curPortName);
				ref_cmbBxQr10_PortSelection.getItems().add(curPortName);
				ref_cmbBxQr11_PortSelection.getItems().add(curPortName);
				ref_cmbBxQr12_PortSelection.getItems().add(curPortName);
				ref_cmbBxQr13_PortSelection.getItems().add(curPortName);

			}
		}

		/*
		 * try {
		 * cmbBxPowerSrcPortSelection.getSelectionModel().select(0);
		 * } catch(Exception e) {
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception1:"+e.getMessage());
		 * }
		 * try {
		 * cmbBxRefStdPortSelection.getSelectionModel().select(0);
		 * } catch(Exception e) {
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception2:"+e.getMessage());
		 * }
		 */

		try {
			ref_cmbBxQr1_PortSelection.getSelectionModel().select(0);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-1:" + e.getMessage());
		}

		try {
			ref_cmbBxQr2_PortSelection.getSelectionModel().select(0);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-2:" + e.getMessage());
		}
		try {
			ref_cmbBxQr3_PortSelection.getSelectionModel().select(0);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-3:" + e.getMessage());
		}
		try {
			ref_cmbBxQr4_PortSelection.getSelectionModel().select(0);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-4:" + e.getMessage());
		}
		try {
			ref_cmbBxQr5_PortSelection.getSelectionModel().select(0);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-5:" + e.getMessage());
		}
		try {
			ref_cmbBxQr6_PortSelection.getSelectionModel().select(0);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-6:" + e.getMessage());
		}
		try {
			ref_cmbBxQr7_PortSelection.getSelectionModel().select(0);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-7:" + e.getMessage());
		}

		try {
			ref_cmbBxQr8_PortSelection.getSelectionModel().select(0);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-8:" + e.getMessage());
		}

		try {
			ref_cmbBxQr9_PortSelection.getSelectionModel().select(0);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-9:" + e.getMessage());
		}
		try {
			ref_cmbBxQr10_PortSelection.getSelectionModel().select(0);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-10:" + e.getMessage());
		}
		try {
			ref_cmbBxQr11_PortSelection.getSelectionModel().select(0);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-11:" + e.getMessage());
		}

		try {
			ref_cmbBxQr12_PortSelection.getSelectionModel().select(0);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-12:" + e.getMessage());
		}

		try {
			ref_cmbBxQr13_PortSelection.getSelectionModel().select(0);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-13:" + e.getMessage());
		}

            });
        }).start();
}

	public void SaveOnClick() {
		String pwr_type = ConstantApp.SOURCE_TYPE_POWER_SOURCE;

		String device1_type_key = ConstantQrScanner.QR_SCANNER_TYPE_NEWLAND_1;
		String device2_type = ConstantQrScanner.QR_SCANNER_TYPE_NEWLAND_2;
		String device3_type = ConstantQrScanner.QR_SCANNER_TYPE_NEWLAND_3;
		String device4_type = ConstantQrScanner.QR_SCANNER_TYPE_NEWLAND_4;
		String device5_type = ConstantQrScanner.QR_SCANNER_TYPE_NEWLAND_5;
		String device6_type = ConstantQrScanner.QR_SCANNER_TYPE_NEWLAND_6;

		String device7_type = ConstantQrScanner.QR_SCANNER_TYPE_NEWLAND_7;
		String device8_type = ConstantQrScanner.QR_SCANNER_TYPE_NEWLAND_8;
		String device9_type = ConstantQrScanner.QR_SCANNER_TYPE_NEWLAND_9;
		String device10_type = ConstantQrScanner.QR_SCANNER_TYPE_NEWLAND_10;

		String device11_type = ConstantQrScanner.QR_SCANNER_TYPE_NEWLAND_11;
		String device12_type = ConstantQrScanner.QR_SCANNER_TYPE_NEWLAND_12;
		String device13_type = ConstantQrScanner.QR_SCANNER_TYPE_NEWLAND_13;

		String device1_model_name = ref_cmbBxQr1_ModelName.getSelectionModel().getSelectedItem();
		String device2_model_name = ref_cmbBxQr2_ModelName.getSelectionModel().getSelectedItem();
		String device3_model_name = ref_cmbBxQr3_ModelName.getSelectionModel().getSelectedItem();
		String device4_model_name = ref_cmbBxQr4_ModelName.getSelectionModel().getSelectedItem();
		String device5_model_name = ref_cmbBxQr5_ModelName.getSelectionModel().getSelectedItem();
		String device6_model_name = ref_cmbBxQr6_ModelName.getSelectionModel().getSelectedItem();

		String device7_model_name = ref_cmbBxQr7_ModelName.getSelectionModel().getSelectedItem();
		String device8_model_name = ref_cmbBxQr8_ModelName.getSelectionModel().getSelectedItem();
		String device9_model_name = ref_cmbBxQr9_ModelName.getSelectionModel().getSelectedItem();
		String device10_model_name = ref_cmbBxQr10_ModelName.getSelectionModel().getSelectedItem();

		String device11_model_name = ref_cmbBxQr11_ModelName.getSelectionModel().getSelectedItem();
		String device12_model_name = ref_cmbBxQr12_ModelName.getSelectionModel().getSelectedItem();
		String device13_model_name = ref_cmbBxQr13_ModelName.getSelectionModel().getSelectedItem();

		String device1_port_name = ref_cmbBxQr1_PortSelection.getSelectionModel().getSelectedItem();
		String device2_port_name = ref_cmbBxQr2_PortSelection.getSelectionModel().getSelectedItem();
		String device3_port_name = ref_cmbBxQr3_PortSelection.getSelectionModel().getSelectedItem();
		String device4_port_name = ref_cmbBxQr4_PortSelection.getSelectionModel().getSelectedItem();
		String device5_port_name = ref_cmbBxQr5_PortSelection.getSelectionModel().getSelectedItem();
		String device6_port_name = ref_cmbBxQr6_PortSelection.getSelectionModel().getSelectedItem();

		String device7_port_name = ref_cmbBxQr7_PortSelection.getSelectionModel().getSelectedItem();
		String device8_port_name = ref_cmbBxQr8_PortSelection.getSelectionModel().getSelectedItem();
		String device9_port_name = ref_cmbBxQr9_PortSelection.getSelectionModel().getSelectedItem();
		String device10_port_name = ref_cmbBxQr10_PortSelection.getSelectionModel().getSelectedItem();

		String device11_port_name = ref_cmbBxQr11_PortSelection.getSelectionModel().getSelectedItem();
		String device12_port_name = ref_cmbBxQr12_PortSelection.getSelectionModel().getSelectedItem();
		String device13_port_name = ref_cmbBxQr13_PortSelection.getSelectionModel().getSelectedItem();
	
		String device1_baud_rate = ref_cmbBxQr1_BaudRate.getSelectionModel().getSelectedItem().toString();
		String device2_baud_rate = ref_cmbBxQr2_BaudRate.getSelectionModel().getSelectedItem().toString();
		String device3_baud_rate = ref_cmbBxQr3_BaudRate.getSelectionModel().getSelectedItem().toString();
		String device4_baud_rate = ref_cmbBxQr4_BaudRate.getSelectionModel().getSelectedItem().toString();
		String device5_baud_rate = ref_cmbBxQr5_BaudRate.getSelectionModel().getSelectedItem().toString();
		String device6_baud_rate = ref_cmbBxQr6_BaudRate.getSelectionModel().getSelectedItem().toString();

		String device7_baud_rate = ref_cmbBxQr7_BaudRate.getSelectionModel().getSelectedItem().toString();
		String device8_baud_rate = ref_cmbBxQr8_BaudRate.getSelectionModel().getSelectedItem().toString();
		String device9_baud_rate = ref_cmbBxQr9_BaudRate.getSelectionModel().getSelectedItem().toString();
		String device10_baud_rate = ref_cmbBxQr10_BaudRate.getSelectionModel().getSelectedItem().toString();
		String device11_baud_rate = ref_cmbBxQr11_BaudRate.getSelectionModel().getSelectedItem().toString();
		String device12_baud_rate = ref_cmbBxQr12_BaudRate.getSelectionModel().getSelectedItem().toString();
		String device13_baud_rate = ref_cmbBxQr13_BaudRate.getSelectionModel().getSelectedItem().toString();
	
		String device1ClusterName = (String) ref_cmbBxQr1ClusterId.getSelectionModel().getSelectedItem();
		String device2ClusterName = (String) ref_cmbBxQr2ClusterId.getSelectionModel().getSelectedItem();
		String device3ClusterName = (String) ref_cmbBxQr3ClusterId.getSelectionModel().getSelectedItem();
		String device4ClusterName = (String) ref_cmbBxQr4ClusterId.getSelectionModel().getSelectedItem();
		String device5ClusterName = (String) ref_cmbBxQr5ClusterId.getSelectionModel().getSelectedItem();
		String device6ClusterName = (String) ref_cmbBxQr6ClusterId.getSelectionModel().getSelectedItem();
		String device7ClusterName = (String) ref_cmbBxQr7ClusterId.getSelectionModel().getSelectedItem();
		String device8ClusterName = (String) ref_cmbBxQr8ClusterId.getSelectionModel().getSelectedItem();
		String device9ClusterName = (String) ref_cmbBxQr9ClusterId.getSelectionModel().getSelectedItem();
		String device10ClusterName = (String) ref_cmbBxQr10ClusterId.getSelectionModel().getSelectedItem();
		String device11ClusterName = (String) ref_cmbBxQr11ClusterId.getSelectionModel().getSelectedItem();
		String device12ClusterName = (String) ref_cmbBxQr12ClusterId.getSelectionModel().getSelectedItem();
		String device13ClusterName = (String) ref_cmbBxQr13ClusterId.getSelectionModel().getSelectedItem();

		String device1BayName = (String) ref_cmbBxQr1BayId.getSelectionModel().getSelectedItem();
		String device2BayName = (String) ref_cmbBxQr2BayId.getSelectionModel().getSelectedItem();
		String device3BayName = (String) ref_cmbBxQr3BayId.getSelectionModel().getSelectedItem();
		String device4BayName = (String) ref_cmbBxQr4BayId.getSelectionModel().getSelectedItem();
		String device5BayName = (String) ref_cmbBxQr5BayId.getSelectionModel().getSelectedItem();
		String device6BayName = (String) ref_cmbBxQr6BayId.getSelectionModel().getSelectedItem();

		String device7BayName = (String) ref_cmbBxQr7BayId.getSelectionModel().getSelectedItem();
		String device8BayName = (String) ref_cmbBxQr8BayId.getSelectionModel().getSelectedItem();
		String device9BayName = (String) ref_cmbBxQr9BayId.getSelectionModel().getSelectedItem();
		String device10BayName = (String) ref_cmbBxQr10BayId.getSelectionModel().getSelectedItem();
		String device11BayName = (String) ref_cmbBxQr11BayId.getSelectionModel().getSelectedItem();
		String device12BayName = (String) ref_cmbBxQr12BayId.getSelectionModel().getSelectedItem();
		String device13BayName = (String) ref_cmbBxQr13BayId.getSelectionModel().getSelectedItem();

		String device1PositionNo = (String) ref_cmbBxQr1PositionId.getSelectionModel().getSelectedItem();
		String device2PositionNo = (String) ref_cmbBxQr2PositionId.getSelectionModel().getSelectedItem();
		String device3PositionNo = (String) ref_cmbBxQr3PositionId.getSelectionModel().getSelectedItem();
		String device4PositionNo = (String) ref_cmbBxQr4PositionId.getSelectionModel().getSelectedItem();
		String device5PositionNo = (String) ref_cmbBxQr5PositionId.getSelectionModel().getSelectedItem();
		String device6PositionNo = (String) ref_cmbBxQr6PositionId.getSelectionModel().getSelectedItem();

		String device7PositionNo = (String) ref_cmbBxQr7PositionId.getSelectionModel().getSelectedItem();
		String device8PositionNo = (String) ref_cmbBxQr8PositionId.getSelectionModel().getSelectedItem();
		String device9PositionNo = (String) ref_cmbBxQr9PositionId.getSelectionModel().getSelectedItem();
		String device10PositionNo = (String) ref_cmbBxQr10PositionId.getSelectionModel().getSelectedItem();
		String device11PositionNo = (String) ref_cmbBxQr11PositionId.getSelectionModel().getSelectedItem();
		String device12PositionNo = (String) ref_cmbBxQr12PositionId.getSelectionModel().getSelectedItem();
		String device13PositionNo = (String) ref_cmbBxQr13PositionId.getSelectionModel().getSelectedItem();

		String device1Cname = ref_txtQr1Cname.getText();// getSelectionModel().getSelectedItem();
		String device2Cname = ref_txtQr2Cname.getText();// getSelectionModel().getSelectedItem();
		String device3Cname = ref_txtQr3Cname.getText();
		String device4Cname = ref_txtQr4Cname.getText();
		String device5Cname = ref_txtQr5Cname.getText();
		String device6Cname = ref_txtQr6Cname.getText();
		String device7Cname = ref_txtQr7Cname.getText();
		String device8Cname = ref_txtQr7Cname.getText();
		String device9Cname = ref_txtQr7Cname.getText();
		String device10Cname = ref_txtQr10Cname.getText();
		String device11Cname = ref_txtQr11Cname.getText();
		String device12Cname = ref_txtQr12Cname.getText();
		String device13Cname = ref_txtQr13Cname.getText();

		String device1Id = getClusterBayPositionNoDeviceIdMap().get(
				device1ClusterName + "_" + device1BayName + "_" + device1PositionNo);// ref_txtDut1Cname.getText();//ref_cmbBxDutCname1.getSelectionModel().getSelectedItem();
		String device2Id = getClusterBayPositionNoDeviceIdMap().get(
				device2ClusterName + "_" + device2BayName + "_" + device2PositionNo);
		String device3Id = getClusterBayPositionNoDeviceIdMap().get(
				device3ClusterName + "_" + device3BayName + "_" + device3PositionNo);// ref_txtDut1Cname.getText();//ref_cmbBxDutCname1.getSelectionModel().getSelectedItem();
		String device4Id = getClusterBayPositionNoDeviceIdMap().get(
				device4ClusterName + "_" + device4BayName + "_" + device4PositionNo);
		String device5Id = getClusterBayPositionNoDeviceIdMap().get(
				device5ClusterName + "_" + device5BayName + "_" + device5PositionNo);// ref_txtDut1Cname.getText();//ref_cmbBxDutCname1.getSelectionModel().getSelectedItem();
		String device6Id = getClusterBayPositionNoDeviceIdMap().get(
				device6ClusterName + "_" + device6BayName + "_" + device6PositionNo);

		String device7Id = getClusterBayPositionNoDeviceIdMap().get(
				device7ClusterName + "_" + device7BayName + "_" + device7PositionNo);
		String device8Id = getClusterBayPositionNoDeviceIdMap().get(
				device8ClusterName + "_" + device8BayName + "_" + device8PositionNo);
		String device9Id = getClusterBayPositionNoDeviceIdMap().get(
				device9ClusterName + "_" + device9BayName + "_" + device9PositionNo);
		String device10Id = getClusterBayPositionNoDeviceIdMap().get(
				device10ClusterName + "_" + device10BayName + "_" + device10PositionNo);
		String device11Id = getClusterBayPositionNoDeviceIdMap().get(
				device11ClusterName + "_" + device11BayName + "_" + device11PositionNo);
		String device12Id = getClusterBayPositionNoDeviceIdMap().get(
				device12ClusterName + "_" + device12BayName + "_" + device12PositionNo);
		String device13Id = getClusterBayPositionNoDeviceIdMap().get(
				device13ClusterName + "_" + device13BayName + "_" + device13PositionNo);

		String clusterId = "";
		String bayId = "";
		String deviceType = ConstantConveyor.DEVICE_TYPE_QR_SCANNER;
		if ((!device1ClusterName.startsWith("No-")) &&
				(!device1BayName.startsWith("No-")) &&
				(!device1PositionNo.startsWith("No-"))) {
			if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {
				clusterId = getClusterNameIdListMap().get(device1ClusterName);
				bayId = getClusterBayNameIdMap().get(device1ClusterName + "_" + device1BayName);
				DeviceSetting deviceSetting = new DeviceSetting(device1_type_key, device1_model_name, device1_port_name,
						device1_baud_rate,
						clusterId, device1ClusterName, bayId, device1BayName, device1PositionNo, device1Cname,
						device1Id, deviceType);

				MySqlServiceManager.getDeviceSettingService().saveToDb(deviceSetting);
			} else {
				MySQL_Controller.sp_add_device_settings_v2(device1_type_key, device1_model_name, device1_port_name,
						device1_baud_rate,
						device1ClusterName, device1BayName, device1PositionNo, device1Cname, device1Id);
			}
		}

		if ((!device2ClusterName.startsWith("No-")) &&
				(!device2BayName.startsWith("No-")) &&
				(!device2PositionNo.startsWith("No-"))) {
			if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {
				clusterId = getClusterNameIdListMap().get(device2ClusterName);
				bayId = getClusterBayNameIdMap().get(device2ClusterName + "_" + device2BayName);
				DeviceSetting deviceSetting = new DeviceSetting(device2_type, device2_model_name, device2_port_name,
						device2_baud_rate,
						clusterId, device2ClusterName, bayId, device2BayName, device2PositionNo, device2Cname,
						device2Id, deviceType);

				MySqlServiceManager.getDeviceSettingService().saveToDb(deviceSetting);
			} else {
				MySQL_Controller.sp_add_device_settings_v2(device2_type, device2_model_name, device2_port_name,
						device2_baud_rate,
						device2ClusterName, device2BayName, device2PositionNo, device2Cname, device2Id);
			}
		}

		if ((!device3ClusterName.startsWith("No-")) &&
				(!device3BayName.startsWith("No-")) &&
				(!device3PositionNo.startsWith("No-"))) {
			if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {
				clusterId = getClusterNameIdListMap().get(device3ClusterName);
				bayId = getClusterBayNameIdMap().get(device3ClusterName + "_" + device3BayName);
				DeviceSetting deviceSetting = new DeviceSetting(device3_type, device3_model_name, device3_port_name,
						device3_baud_rate,
						clusterId, device3ClusterName, bayId, device3BayName, device3PositionNo, device3Cname,
						device3Id, deviceType);

				MySqlServiceManager.getDeviceSettingService().saveToDb(deviceSetting);
			} else {
				MySQL_Controller.sp_add_device_settings_v2(device3_type, device3_model_name, device3_port_name,
						device3_baud_rate,
						device3ClusterName, device3BayName, device3PositionNo, device3Cname, device3Id);
			}
		}

		if ((!device4ClusterName.startsWith("No-")) &&
				(!device4BayName.startsWith("No-")) &&
				(!device4PositionNo.startsWith("No-"))) {
			if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {
				clusterId = getClusterNameIdListMap().get(device4ClusterName);
				bayId = getClusterBayNameIdMap().get(device4ClusterName + "_" + device4BayName);
				DeviceSetting deviceSetting = new DeviceSetting(device4_type, device4_model_name, device4_port_name,
						device4_baud_rate,
						clusterId, device4ClusterName, bayId, device4BayName, device4PositionNo, device4Cname,
						device4Id, deviceType);

				MySqlServiceManager.getDeviceSettingService().saveToDb(deviceSetting);
			} else {
				MySQL_Controller.sp_add_device_settings_v2(device4_type, device4_model_name, device4_port_name,
						device4_baud_rate,
						device4ClusterName, device4BayName, device4PositionNo, device4Cname, device4Id);
			}
		}

		if ((!device5ClusterName.startsWith("No-")) &&
				(!device5BayName.startsWith("No-")) &&
				(!device5PositionNo.startsWith("No-"))) {
			if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {
				clusterId = getClusterNameIdListMap().get(device5ClusterName);
				bayId = getClusterBayNameIdMap().get(device5ClusterName + "_" + device5BayName);
				DeviceSetting deviceSetting = new DeviceSetting(device5_type, device5_model_name, device5_port_name,
						device5_baud_rate,
						clusterId, device5ClusterName, bayId, device5BayName, device5PositionNo, device5Cname,
						device5Id, deviceType);

				MySqlServiceManager.getDeviceSettingService().saveToDb(deviceSetting);
			} else {
				MySQL_Controller.sp_add_device_settings_v2(device5_type, device5_model_name, device5_port_name,
						device5_baud_rate,
						device5ClusterName, device5BayName, device5PositionNo, device5Cname, device5Id);
			}
		}

		if ((!device6ClusterName.startsWith("No-")) &&
				(!device6BayName.startsWith("No-")) &&
				(!device6PositionNo.startsWith("No-"))) {
			if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {
				clusterId = getClusterNameIdListMap().get(device6ClusterName);
				bayId = getClusterBayNameIdMap().get(device6ClusterName + "_" + device6BayName);
				DeviceSetting deviceSetting = new DeviceSetting(device6_type, device6_model_name, device6_port_name,
						device6_baud_rate,
						clusterId, device6ClusterName, bayId, device6BayName, device6PositionNo, device6Cname,
						device6Id, deviceType);

				MySqlServiceManager.getDeviceSettingService().saveToDb(deviceSetting);
			} else {
				MySQL_Controller.sp_add_device_settings_v2(device6_type, device6_model_name, device6_port_name,
						device6_baud_rate,
						device6ClusterName, device6BayName, device6PositionNo, device6Cname, device6Id);
			}
		}

		if ((!device7ClusterName.startsWith("No-")) &&
				(!device7BayName.startsWith("No-")) &&
				(!device7PositionNo.startsWith("No-"))) {
			if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {
				clusterId = getClusterNameIdListMap().get(device7ClusterName);
				bayId = getClusterBayNameIdMap().get(device7ClusterName + "_" + device7BayName);
				DeviceSetting deviceSetting = new DeviceSetting(device7_type, device7_model_name, device7_port_name,
						device7_baud_rate,
						clusterId, device7ClusterName, bayId, device7BayName, device7PositionNo, device7Cname,
						device7Id, deviceType);

				MySqlServiceManager.getDeviceSettingService().saveToDb(deviceSetting);
			} else {
				MySQL_Controller.sp_add_device_settings_v2(device7_type, device7_model_name, device7_port_name,
						device7_baud_rate,
						device7ClusterName, device7BayName, device7PositionNo, device7Cname, device7Id);
			}
		}

		if ((!device8ClusterName.startsWith("No-")) &&
				(!device8BayName.startsWith("No-")) &&
				(!device8PositionNo.startsWith("No-"))) {
			if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {
				clusterId = getClusterNameIdListMap().get(device8ClusterName);
				bayId = getClusterBayNameIdMap().get(device8ClusterName + "_" + device8BayName);
				DeviceSetting deviceSetting = new DeviceSetting(device8_type, device8_model_name, device8_port_name,
						device8_baud_rate,
						clusterId, device8ClusterName, bayId, device8BayName, device8PositionNo, device8Cname,
						device8Id, deviceType);

				MySqlServiceManager.getDeviceSettingService().saveToDb(deviceSetting);
			} else {
				MySQL_Controller.sp_add_device_settings_v2(device8_type, device8_model_name, device8_port_name,
						device8_baud_rate,
						device8ClusterName, device8BayName, device8PositionNo, device8Cname, device8Id);
			}
		}

		if ((!device9ClusterName.startsWith("No-")) &&
				(!device9BayName.startsWith("No-")) &&
				(!device9PositionNo.startsWith("No-"))) {
			if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {
				clusterId = getClusterNameIdListMap().get(device9ClusterName);
				bayId = getClusterBayNameIdMap().get(device9ClusterName + "_" + device9BayName);
				DeviceSetting deviceSetting = new DeviceSetting(device9_type, device9_model_name, device9_port_name,
						device9_baud_rate,
						clusterId, device9ClusterName, bayId, device9BayName, device9PositionNo, device9Cname,
						device9Id, deviceType);

				MySqlServiceManager.getDeviceSettingService().saveToDb(deviceSetting);
			} else {
				MySQL_Controller.sp_add_device_settings_v2(device9_type, device9_model_name, device9_port_name,
						device9_baud_rate,
						device9ClusterName, device9BayName, device9PositionNo, device9Cname, device9Id);
			}
		}

		if ((!device10ClusterName.startsWith("No-")) &&
				(!device10BayName.startsWith("No-")) &&
				(!device10PositionNo.startsWith("No-"))) {
			if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {
				clusterId = getClusterNameIdListMap().get(device10ClusterName);
				bayId = getClusterBayNameIdMap().get(device10ClusterName + "_" + device10BayName);
				DeviceSetting deviceSetting = new DeviceSetting(device10_type, device10_model_name, device10_port_name,
						device10_baud_rate,
						clusterId, device10ClusterName, bayId, device10BayName, device10PositionNo, device10Cname,
						device10Id, deviceType);

				MySqlServiceManager.getDeviceSettingService().saveToDb(deviceSetting);
			} else {
				MySQL_Controller.sp_add_device_settings_v2(device10_type, device10_model_name, device10_port_name,
						device10_baud_rate,
						device10ClusterName, device10BayName, device10PositionNo, device10Cname, device10Id);
			}
		}

		if ((!device11ClusterName.startsWith("No-")) &&
				(!device11BayName.startsWith("No-")) &&
				(!device11PositionNo.startsWith("No-"))) {
			if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {
				clusterId = getClusterNameIdListMap().get(device11ClusterName);
				bayId = getClusterBayNameIdMap().get(device11ClusterName + "_" + device11BayName);
				DeviceSetting deviceSetting = new DeviceSetting(device11_type, device11_model_name, device11_port_name,
						device11_baud_rate,
						clusterId, device11ClusterName, bayId, device11BayName, device11PositionNo, device11Cname,
						device11Id, deviceType);

				MySqlServiceManager.getDeviceSettingService().saveToDb(deviceSetting);
			} else {
				MySQL_Controller.sp_add_device_settings_v2(device11_type, device11_model_name, device11_port_name,
						device11_baud_rate,
						device11ClusterName, device11BayName, device11PositionNo, device11Cname, device11Id);
			}
		}

		if ((!device12ClusterName.startsWith("No-")) &&
				(!device12BayName.startsWith("No-")) &&
				(!device12PositionNo.startsWith("No-"))) {
			if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {
				clusterId = getClusterNameIdListMap().get(device12ClusterName);
				bayId = getClusterBayNameIdMap().get(device12ClusterName + "_" + device12BayName);
				DeviceSetting deviceSetting = new DeviceSetting(device12_type, device12_model_name, device12_port_name,
						device12_baud_rate,
						clusterId, device12ClusterName, bayId, device12BayName, device12PositionNo, device12Cname,
						device12Id, deviceType);

				MySqlServiceManager.getDeviceSettingService().saveToDb(deviceSetting);
			} else {
				MySQL_Controller.sp_add_device_settings_v2(device12_type, device12_model_name, device12_port_name,
						device12_baud_rate,
						device12ClusterName, device12BayName, device12PositionNo, device12Cname, device12Id);
			}
		}

		if ((!device13ClusterName.startsWith("No-")) &&
				(!device13BayName.startsWith("No-")) &&
				(!device13PositionNo.startsWith("No-"))) {
			if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {
				clusterId = getClusterNameIdListMap().get(device13ClusterName);
				bayId = getClusterBayNameIdMap().get(device13ClusterName + "_" + device13BayName);
				DeviceSetting deviceSetting = new DeviceSetting(device13_type, device13_model_name, device13_port_name,
						device13_baud_rate,
						clusterId, device13ClusterName, bayId, device13BayName, device13PositionNo, device13Cname,
						device13Id, deviceType);

				MySqlServiceManager.getDeviceSettingService().saveToDb(deviceSetting);
			} else {
				MySQL_Controller.sp_add_device_settings_v2(device13_type, device13_model_name, device13_port_name,
						device13_baud_rate,
						device13ClusterName, device13BayName, device13PositionNo, device13Cname, device13Id);
			}
		}

		if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {
			ConveyorDataManager.loadDeviceSettingFromDb();
		}

		WindowManager.InformUser("Saved Successfully", "Saved data successfully", AlertType.INFORMATION);

	}

	public void RefStdValidateSerialCmd() {

		String RefStdCommPortID = null;
		String RefStdCommBaudRate = null;

	}

	class MyRunnable implements Runnable {

		double count;
		TextField l_txtValidateRefStdCmdStatus;

		public MyRunnable(TextField ValidateRefStdCmdStatus) {
			count = 0;
			l_txtValidateRefStdCmdStatus = ValidateRefStdCmdStatus;
		}

		@Override
		public void run() {
			for (int i = 0; i <= count; i++) {

				final double update_i = count;

				ApplicationLauncher.logger.info("RefStdValidateSerialCmd:runLater1:test2");
				l_txtValidateRefStdCmdStatus.setText("Sending CMD" + update_i);
				ApplicationLauncher.logger.info("RefStdValidateSerialCmd:runLater1:test3");

				// Update JavaFX UI with runLater() in UI thread
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						for (int j = 0; j <= update_i; j++) {
							ApplicationLauncher.logger.info("RefStdValidateSerialCmd:runLater2:test2");
							ApplicationLauncher.logger.info("RefStdValidateSerialCmd:runLater2:test3");
							ApplicationLauncher.logger.info("RefStdValidateSerialCmd:runLater2:SleepEntry");
							ApplicationLauncher.logger.info("RefStdValidateSerialCmd:runLater2:Exception");

							ApplicationLauncher.logger.info("RefStdValidateSerialCmd:runLater2:NextForloop");

						}
					}
				});

			}
		}

	}

	class UI_DisplayTimerTask extends TimerTask {

		double count = 10;
		TextField l_txtValidateRefStdCmdStatus;

		public UI_DisplayTimerTask(TextField ValidateRefStdCmdStatus) {

			l_txtValidateRefStdCmdStatus = ValidateRefStdCmdStatus;

		}

		@Override
		public void run() {
			for (int i = 0; i < count; i++) {
				ApplicationLauncher.logger.info("RefStdValidateSerialCmd:test2");
				l_txtValidateRefStdCmdStatus.setText("Sending CMD" + i);
				ApplicationLauncher.logger.info("RefStdValidateSerialCmd:test3");

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {

					e.printStackTrace();
					ApplicationLauncher.logger.error("UI_DisplayTimerTask: InterruptedException: " + e.getMessage());
				}
			}

			UI_DisplayTimer.cancel();

		}

	}

	public void qr1_ValidateSerialCmd() {
		ApplicationLauncher.logger.debug("qr1_ValidateSerialCmd : Entry");

		String LDU_CommPortID = null;
		String LDUCommBaudRate = null;
		ref_txtValidateQr1_CmdStatus.clear();
		ref_txtQr1ReadData.clear();
		try {
			// serialDM_Obj.commLDU1.searchForPorts();
			LDU_CommPortID = getCurrentLDU_ComPortID1();
			LDUCommBaudRate = getCurrentLDU_ComBaudRate1();
			// boolean status =
			// DisplayDataObj.pwrSrcPortAccessible_V2_1(LDU_CommPortID,LDUCommBaudRate);//false;//serialDM_Obj.LDU1_Init(LDU_CommPortID,LDUCommBaudRate);
			String portCname = ref_txtQr1Cname.getText();
			SpmQrScanner serialPortManagerQrScanner = new SpmQrScanner(portCname);
			boolean status = serialPortManagerQrScanner.powerSourceComInitV2(LDU_CommPortID, LDUCommBaudRate);
			if (!status) {

				ref_txtValidateQr1_CmdStatus.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);

			} else {
				setPortValidationTurnedON(true);
				// status = serialDM_Obj.lscsLDU1_CheckCom();
				// DisplayDataObj.pwrSrcEnableSerialMonitoring_V2();
				serialPortManagerQrScanner.startSerialRxPhysical_PwrSrc();
				serialPortManagerQrScanner.enableSerialRxPhysical_QrScannerMonitor();
				QrScannerDirector pwrSrcDirector = new QrScannerDirector(serialPortManagerQrScanner);
				Map<String, Object> responseMap = pwrSrcDirector.scanQrCode();

				status = (boolean) responseMap.get("status");
				String qrData = "";
				try {
					if (status) {
						qrData = (String) responseMap.get("responseData");
						ApplicationLauncher.logger.debug("qr1_ValidateSerialCmd: qrData1: " + qrData);
						qrData = NewlandQRCodeScanner.extractScannedData((String) responseMap.get("responseData"));
						ApplicationLauncher.logger.debug("qr1_ValidateSerialCmd: qrData2: " + qrData);
					} else {
						ApplicationLauncher.logger.debug("qr1_ValidateSerialCmd: No response ");
					}
				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger.error("qr1_ValidateSerialCmd: Exception" + e.getMessage());
				}
				if (!status) {
					ref_txtValidateQr1_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
				} else {
					ref_txtValidateQr1_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
					ref_txtQr1ReadData.setText(qrData);
				}
				setPortValidationTurnedON(false);
				// DisplayDataObj.pwrSrcDisconnectPort_V2();
				serialPortManagerQrScanner.disconnectQrScanner();
				// DisplayDataObj.setLDU1_ReadDataFlag(false);
			}
			// serialDM_Obj.DisconnectLDU1();
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("qr1_ValidateSerialCmd: Exception" + e.getMessage());
		}

		ApplicationLauncher.logger.debug("qr1_ValidateSerialCmd : Exit");

	}

	public void qr2_ValidateSerialCmd() {
		ApplicationLauncher.logger.debug("qr2_ValidateSerialCmd : Entry");

		String LDU_CommPortID = null;
		String LDUCommBaudRate = null;
		ref_txtValidateQr2_CmdStatus.clear();
		ref_txtQr2ReadData.clear();
		try {
			// serialDM_Obj.commLDU1.searchForPorts();
			LDU_CommPortID = getCurrentLDU_ComPortID2();
			LDUCommBaudRate = getCurrentLDU_ComBaudRate2();
			String portCname = ref_txtQr2Cname.getText();
			SpmQrScanner serialPortManagerQrScanner = new SpmQrScanner(portCname);
			boolean status = serialPortManagerQrScanner.powerSourceComInitV2(LDU_CommPortID, LDUCommBaudRate);
			if (!status) {

				ref_txtValidateQr2_CmdStatus.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);

			} else {
				setPortValidationTurnedON(true);

				serialPortManagerQrScanner.startSerialRxPhysical_PwrSrc();
				serialPortManagerQrScanner.enableSerialRxPhysical_QrScannerMonitor();
				QrScannerDirector pwrSrcDirector = new QrScannerDirector(serialPortManagerQrScanner);
				Map<String, Object> responseMap = pwrSrcDirector.scanQrCode();

				status = (boolean) responseMap.get("status");
				String qrData = "";
				try {
					if (status) {
						qrData = (String) responseMap.get("responseData");
						ApplicationLauncher.logger.debug("qr2_ValidateSerialCmd: qrData1: " + qrData);
						qrData = NewlandQRCodeScanner.extractScannedData((String) responseMap.get("responseData"));
						ApplicationLauncher.logger.debug("qr2_ValidateSerialCmd: qrData2: " + qrData);
					} else {
						ApplicationLauncher.logger.debug("qr2_ValidateSerialCmd: No response ");
					}
				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger.error("qr2_ValidateSerialCmd: Exception" + e.getMessage());
				}
				if (!status) {
					ref_txtValidateQr2_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
				} else {
					ref_txtValidateQr2_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
					ref_txtQr2ReadData.setText(qrData);
				}
				setPortValidationTurnedON(false);
				serialPortManagerQrScanner.disconnectQrScanner();

			}
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("qr2_ValidateSerialCmd: Exception" + e.getMessage());
		}

		ApplicationLauncher.logger.debug("qr2_ValidateSerialCmd : Exit");

	}

	public void qr3_ValidateSerialCmd() {
		ApplicationLauncher.logger.debug("qr3_ValidateSerialCmd : Entry");

		String LDU_CommPortID = null;
		String LDUCommBaudRate = null;
		ref_txtValidateQr3_CmdStatus.clear();
		ref_txtQr3ReadData.clear();
		try {
			// serialDM_Obj.commLDU1.searchForPorts();
			LDU_CommPortID = getCurrentLDU_ComPortID3();
			LDUCommBaudRate = getCurrentLDU_ComBaudRate3();
			String portCname = ref_txtQr3Cname.getText();
			SpmQrScanner serialPortManagerQrScanner = new SpmQrScanner(portCname);
			boolean status = serialPortManagerQrScanner.powerSourceComInitV2(LDU_CommPortID, LDUCommBaudRate);
			if (!status) {

				ref_txtValidateQr3_CmdStatus.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);

			} else {
				setPortValidationTurnedON(true);

				serialPortManagerQrScanner.startSerialRxPhysical_PwrSrc();
				serialPortManagerQrScanner.enableSerialRxPhysical_QrScannerMonitor();
				QrScannerDirector pwrSrcDirector = new QrScannerDirector(serialPortManagerQrScanner);
				Map<String, Object> responseMap = pwrSrcDirector.scanQrCode();

				status = (boolean) responseMap.get("status");
				String qrData = "";
				try {
					if (status) {
						qrData = (String) responseMap.get("responseData");
						ApplicationLauncher.logger.debug("qr3_ValidateSerialCmd: qrData1: " + qrData);
						qrData = NewlandQRCodeScanner.extractScannedData((String) responseMap.get("responseData"));
						ApplicationLauncher.logger.debug("qr3_ValidateSerialCmd: qrData2: " + qrData);
					} else {
						ApplicationLauncher.logger.debug("qr3_ValidateSerialCmd: No response ");
					}
				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger.error("qr3_ValidateSerialCmd: Exception" + e.getMessage());
				}
				if (!status) {
					ref_txtValidateQr3_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
				} else {
					ref_txtValidateQr3_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
					ref_txtQr3ReadData.setText(qrData);
				}
				setPortValidationTurnedON(false);
				serialPortManagerQrScanner.disconnectQrScanner();

			}
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("qr3_ValidateSerialCmd: Exception" + e.getMessage());
		}

		ApplicationLauncher.logger.debug("qr3_ValidateSerialCmd : Exit");

	}

	public void qr4_ValidateSerialCmd() {
		ApplicationLauncher.logger.debug("qr4_ValidateSerialCmd : Entry");

		String LDU_CommPortID = null;
		String LDUCommBaudRate = null;
		ref_txtValidateQr4_CmdStatus.clear();
		ref_txtQr4ReadData.clear();
		try {
			// serialDM_Obj.commLDU1.searchForPorts();
			LDU_CommPortID = getCurrentLDU_ComPortID4();
			LDUCommBaudRate = getCurrentLDU_ComBaudRate4();
			String portCname = ref_txtQr4Cname.getText();
			SpmQrScanner serialPortManagerQrScanner = new SpmQrScanner(portCname);
			boolean status = serialPortManagerQrScanner.powerSourceComInitV2(LDU_CommPortID, LDUCommBaudRate);
			if (!status) {

				ref_txtValidateQr4_CmdStatus.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);

			} else {
				setPortValidationTurnedON(true);

				serialPortManagerQrScanner.startSerialRxPhysical_PwrSrc();
				serialPortManagerQrScanner.enableSerialRxPhysical_QrScannerMonitor();
				QrScannerDirector pwrSrcDirector = new QrScannerDirector(serialPortManagerQrScanner);
				Map<String, Object> responseMap = pwrSrcDirector.scanQrCode();

				status = (boolean) responseMap.get("status");
				String qrData = "";
				try {
					if (status) {
						qrData = (String) responseMap.get("responseData");
						ApplicationLauncher.logger.debug("qr4_ValidateSerialCmd: qrData1: " + qrData);
						qrData = NewlandQRCodeScanner.extractScannedData((String) responseMap.get("responseData"));
						ApplicationLauncher.logger.debug("qr4_ValidateSerialCmd: qrData4: " + qrData);
					} else {
						ApplicationLauncher.logger.debug("qr4_ValidateSerialCmd: No response ");
					}
				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger.error("qr4_ValidateSerialCmd: Exception" + e.getMessage());
				}
				if (!status) {
					ref_txtValidateQr4_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
				} else {
					ref_txtValidateQr4_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
					ref_txtQr4ReadData.setText(qrData);
				}
				setPortValidationTurnedON(false);
				serialPortManagerQrScanner.disconnectQrScanner();

			}
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("qr4_ValidateSerialCmd: Exception" + e.getMessage());
		}

		ApplicationLauncher.logger.debug("qr4_ValidateSerialCmd : Exit");

	}

	public void qr5_ValidateSerialCmd() {
		ApplicationLauncher.logger.debug("qr5_ValidateSerialCmd : Entry");

		String LDU_CommPortID = null;
		String LDUCommBaudRate = null;
		ref_txtValidateQr5_CmdStatus.clear();
		ref_txtQr5ReadData.clear();
		try {
			// serialDM_Obj.commLDU1.searchForPorts();
			LDU_CommPortID = getCurrentLDU_ComPortID5();
			LDUCommBaudRate = getCurrentLDU_ComBaudRate5();
			String portCname = ref_txtQr5Cname.getText();
			SpmQrScanner serialPortManagerQrScanner = new SpmQrScanner(portCname);
			boolean status = serialPortManagerQrScanner.powerSourceComInitV2(LDU_CommPortID, LDUCommBaudRate);
			if (!status) {

				ref_txtValidateQr5_CmdStatus.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);

			} else {
				setPortValidationTurnedON(true);

				serialPortManagerQrScanner.startSerialRxPhysical_PwrSrc();
				serialPortManagerQrScanner.enableSerialRxPhysical_QrScannerMonitor();
				QrScannerDirector pwrSrcDirector = new QrScannerDirector(serialPortManagerQrScanner);
				Map<String, Object> responseMap = pwrSrcDirector.scanQrCode();

				status = (boolean) responseMap.get("status");
				String qrData = "";
				try {
					if (status) {
						qrData = (String) responseMap.get("responseData");
						ApplicationLauncher.logger.debug("qr5_ValidateSerialCmd: qrData1: " + qrData);
						qrData = NewlandQRCodeScanner.extractScannedData((String) responseMap.get("responseData"));
						ApplicationLauncher.logger.debug("qr5_ValidateSerialCmd: qrData2: " + qrData);
					} else {
						ApplicationLauncher.logger.debug("qr5_ValidateSerialCmd: No response ");
					}
				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger.error("qr5_ValidateSerialCmd: Exception" + e.getMessage());
				}
				if (!status) {
					ref_txtValidateQr5_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
				} else {
					ref_txtValidateQr5_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
					ref_txtQr5ReadData.setText(qrData);
				}
				setPortValidationTurnedON(false);
				serialPortManagerQrScanner.disconnectQrScanner();

			}
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("qr5_ValidateSerialCmd: Exception" + e.getMessage());
		}

		ApplicationLauncher.logger.debug("qr5_ValidateSerialCmd : Exit");

	}

	public void qr6_ValidateSerialCmd() {
		ApplicationLauncher.logger.debug("qr6_ValidateSerialCmd : Entry");

		String LDU_CommPortID = null;
		String LDUCommBaudRate = null;
		ref_txtValidateQr6_CmdStatus.clear();
		ref_txtQr6ReadData.clear();
		try {
			// serialDM_Obj.commLDU1.searchForPorts();
			LDU_CommPortID = getCurrentLDU_ComPortID6();
			LDUCommBaudRate = getCurrentLDU_ComBaudRate6();
			String portCname = ref_txtQr6Cname.getText();
			SpmQrScanner serialPortManagerQrScanner = new SpmQrScanner(portCname);
			boolean status = serialPortManagerQrScanner.powerSourceComInitV2(LDU_CommPortID, LDUCommBaudRate);
			if (!status) {

				ref_txtValidateQr6_CmdStatus.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);

			} else {
				setPortValidationTurnedON(true);

				serialPortManagerQrScanner.startSerialRxPhysical_PwrSrc();
				serialPortManagerQrScanner.enableSerialRxPhysical_QrScannerMonitor();
				QrScannerDirector pwrSrcDirector = new QrScannerDirector(serialPortManagerQrScanner);
				Map<String, Object> responseMap = pwrSrcDirector.scanQrCode();

				status = (boolean) responseMap.get("status");
				String qrData = "";
				try {
					if (status) {
						qrData = (String) responseMap.get("responseData");
						ApplicationLauncher.logger.debug("qr6_ValidateSerialCmd: qrData1: " + qrData);
						qrData = NewlandQRCodeScanner.extractScannedData((String) responseMap.get("responseData"));
						ApplicationLauncher.logger.debug("qr6_ValidateSerialCmd: qrData2: " + qrData);
					} else {
						ApplicationLauncher.logger.debug("qr6_ValidateSerialCmd: No response ");
					}
				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger.error("qr6_ValidateSerialCmd: Exception" + e.getMessage());
				}
				if (!status) {
					ref_txtValidateQr6_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
				} else {
					ref_txtValidateQr6_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
					ref_txtQr6ReadData.setText(qrData);
				}
				setPortValidationTurnedON(false);
				serialPortManagerQrScanner.disconnectQrScanner();

			}
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("qr6_ValidateSerialCmd: Exception" + e.getMessage());
		}

		ApplicationLauncher.logger.debug("qr6_ValidateSerialCmd : Exit");

	}

	private String getCurrentLDU_ComBaudRate1() {

		return ref_cmbBxQr1_BaudRate.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentLDU_ComPortID1() {

		return ref_cmbBxQr1_PortSelection.getSelectionModel().getSelectedItem();
	}

	private String getCurrentLDU_ComBaudRate2() {

		return ref_cmbBxQr2_BaudRate.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentLDU_ComPortID2() {

		return ref_cmbBxQr2_PortSelection.getSelectionModel().getSelectedItem();
	}

	private String getCurrentLDU_ComBaudRate3() {

		return ref_cmbBxQr3_BaudRate.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentLDU_ComPortID3() {

		return ref_cmbBxQr3_PortSelection.getSelectionModel().getSelectedItem();
	}

	private String getCurrentLDU_ComBaudRate4() {

		return ref_cmbBxQr4_BaudRate.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentLDU_ComPortID4() {

		return ref_cmbBxQr4_PortSelection.getSelectionModel().getSelectedItem();
	}

	private String getCurrentLDU_ComBaudRate5() {

		return ref_cmbBxQr5_BaudRate.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentLDU_ComPortID5() {

		return ref_cmbBxQr5_PortSelection.getSelectionModel().getSelectedItem();
	}

	private String getCurrentLDU_ComBaudRate6() {

		return ref_cmbBxQr6_BaudRate.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentLDU_ComPortID6() {

		return ref_cmbBxQr6_PortSelection.getSelectionModel().getSelectedItem();
	}

	public void PwrSrcValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("PwrSrcValidateSerialCmdTrigger: Invoked:");
		PwrSrcValidateTimer = new Timer();
		PwrSrcValidateTimer.schedule(new PwrSrcValidateTimerTask(), 100);// 1000);

	}

	public void RefStdValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("RefStdValidateSerialCmdTrigger: Invoked:");
		RefStdValidateTimer = new Timer();
		RefStdValidateTimer.schedule(new RefStdValidateTimerTask(), 100);// 1000);

	}

	@FXML
	public void qr1_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("qr1_ValidateSerialCmdTrigger: Invoked:");
		qr1_ValidateTimer = new Timer();
		qr1_ValidateTimer.schedule(new Qr1_ValidateTimerTask(), 100);// 1000);

	}

	@FXML
	public void qr2_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("qr2_ValidateSerialCmdTrigger: Invoked:");
		qr2_ValidateTimer = new Timer();
		qr2_ValidateTimer.schedule(new Qr2_ValidateTimerTask(), 200);// 2000);
	}

	@FXML
	public void qr3_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("qr3_ValidateSerialCmdTrigger: Invoked:");
		qr3_ValidateTimer = new Timer();
		qr3_ValidateTimer.schedule(new Qr3_ValidateTimerTask(), 200);
		// LDU3_ValidateTimer.schedule(new Qr3_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void qr4_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("qr4_ValidateSerialCmdTrigger: Invoked:");
		qr4_ValidateTimer = new Timer();
		qr4_ValidateTimer.schedule(new Qr4_ValidateTimerTask(), 200);
		// LDU4_ValidateTimer.schedule(new Qr4_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void qr5_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("qr5_ValidateSerialCmdTrigger: Invoked:");
		qr5_ValidateTimer = new Timer();
		qr5_ValidateTimer.schedule(new Qr5_ValidateTimerTask(), 200);
		// LDU5_ValidateTimer.schedule(new Qr5_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void qr6_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("qr6_ValidateSerialCmdTrigger: Invoked:");
		qr6_ValidateTimer = new Timer();
		qr6_ValidateTimer.schedule(new Qr6_ValidateTimerTask(), 200);
		// LDU6_ValidateTimer.schedule(new Qr6_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void qr7_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("qr7_ValidateSerialCmdTrigger: Invoked:");
		LDU7_ValidateTimer = new Timer();
		// LDU7_ValidateTimer.schedule(new LDU7_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void qr8_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("qr8_ValidateSerialCmdTrigger: Invoked:");
		LDU8_ValidateTimer = new Timer();
		// LDU8_ValidateTimer.schedule(new LDU8_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void qr9_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("qr9_ValidateSerialCmdTrigger: Invoked:");
		LDU9_ValidateTimer = new Timer();
		// LDU9_ValidateTimer.schedule(new LDU9_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void qr10_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("qr10_ValidateSerialCmdTrigger: Invoked:");
		LDU10_ValidateTimer = new Timer();
		// LDU10_ValidateTimer.schedule(new LDU10_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void qr11_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("qr11_ValidateSerialCmdTrigger: Invoked:");
		LDU11_ValidateTimer = new Timer();
		// LDU11_ValidateTimer.schedule(new LDU11_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void qr12_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("qr12_ValidateSerialCmdTrigger: Invoked:");
		LDU12_ValidateTimer = new Timer();
		// LDU12_ValidateTimer.schedule(new LDU12_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void qr13_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("qr13_ValidateSerialCmdTrigger: Invoked:");
		LDU13_ValidateTimer = new Timer();
		// LDU13_ValidateTimer.schedule(new LDU13_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU14_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU14_ValidateSerialCmdTrigger: Invoked:");
		LDU14_ValidateTimer = new Timer();
		// LDU14_ValidateTimer.schedule(new LDU14_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU15_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU15_ValidateSerialCmdTrigger: Invoked:");
		LDU15_ValidateTimer = new Timer();
		// LDU15_ValidateTimer.schedule(new LDU15_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU16_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU16_ValidateSerialCmdTrigger: Invoked:");
		LDU16_ValidateTimer = new Timer();
		// LDU16_ValidateTimer.schedule(new LDU16_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU17_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU17_ValidateSerialCmdTrigger: Invoked:");
		LDU17_ValidateTimer = new Timer();
		// LDU17_ValidateTimer.schedule(new LDU17_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU18_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU18_ValidateSerialCmdTrigger: Invoked:");
		LDU18_ValidateTimer = new Timer();
		// LDU18_ValidateTimer.schedule(new LDU18_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU19_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU19_ValidateSerialCmdTrigger: Invoked:");
		LDU19_ValidateTimer = new Timer();
		// LDU19_ValidateTimer.schedule(new LDU19_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU20_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU20_ValidateSerialCmdTrigger: Invoked:");
		LDU20_ValidateTimer = new Timer();
		// LDU20_ValidateTimer.schedule(new LDU20_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU21_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU21_ValidateSerialCmdTrigger: Invoked:");
		LDU21_ValidateTimer = new Timer();
		// LDU21_ValidateTimer.schedule(new LDU21_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU22_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU22_ValidateSerialCmdTrigger: Invoked:");
		LDU22_ValidateTimer = new Timer();
		// LDU22_ValidateTimer.schedule(new LDU22_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU23_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU23_ValidateSerialCmdTrigger: Invoked:");
		LDU23_ValidateTimer = new Timer();
		// LDU23_ValidateTimer.schedule(new LDU23_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU24_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU24_ValidateSerialCmdTrigger: Invoked:");
		LDU24_ValidateTimer = new Timer();
		// LDU24_ValidateTimer.schedule(new LDU24_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU25_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU25_ValidateSerialCmdTrigger: Invoked:");
		LDU25_ValidateTimer = new Timer();
		// LDU25_ValidateTimer.schedule(new LDU25_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU26_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU26_ValidateSerialCmdTrigger: Invoked:");
		LDU26_ValidateTimer = new Timer();
		// LDU26_ValidateTimer.schedule(new LDU26_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU27_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU27_ValidateSerialCmdTrigger: Invoked:");
		LDU27_ValidateTimer = new Timer();
		// LDU27_ValidateTimer.schedule(new LDU27_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU28_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU28_ValidateSerialCmdTrigger: Invoked:");
		LDU28_ValidateTimer = new Timer();
		// LDU28_ValidateTimer.schedule(new LDU28_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU29_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU29_ValidateSerialCmdTrigger: Invoked:");
		LDU29_ValidateTimer = new Timer();
		// LDU29_ValidateTimer.schedule(new LDU29_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU30_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU30_ValidateSerialCmdTrigger: Invoked:");
		LDU30_ValidateTimer = new Timer();
		// LDU30_ValidateTimer.schedule(new LDU30_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU31_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU31_ValidateSerialCmdTrigger: Invoked:");
		LDU31_ValidateTimer = new Timer();
		// LDU31_ValidateTimer.schedule(new LDU31_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU32_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU32_ValidateSerialCmdTrigger: Invoked:");
		LDU32_ValidateTimer = new Timer();
		// LDU32_ValidateTimer.schedule(new LDU32_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU33_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU33_ValidateSerialCmdTrigger: Invoked:");
		LDU33_ValidateTimer = new Timer();
		// LDU33_ValidateTimer.schedule(new LDU33_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU34_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU34_ValidateSerialCmdTrigger: Invoked:");
		LDU34_ValidateTimer = new Timer();
		// LDU34_ValidateTimer.schedule(new LDU34_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU35_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU35_ValidateSerialCmdTrigger: Invoked:");
		LDU35_ValidateTimer = new Timer();
		// LDU35_ValidateTimer.schedule(new LDU35_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU36_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU36_ValidateSerialCmdTrigger: Invoked:");
		LDU36_ValidateTimer = new Timer();
		// LDU36_ValidateTimer.schedule(new LDU36_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU37_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU37_ValidateSerialCmdTrigger: Invoked:");
		LDU37_ValidateTimer = new Timer();
		// LDU37_ValidateTimer.schedule(new LDU37_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU38_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU38_ValidateSerialCmdTrigger: Invoked:");
		LDU38_ValidateTimer = new Timer();
		// LDU38_ValidateTimer.schedule(new LDU38_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU39_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU39_ValidateSerialCmdTrigger: Invoked:");
		LDU39_ValidateTimer = new Timer();
		// LDU39_ValidateTimer.schedule(new LDU39_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU40_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU40_ValidateSerialCmdTrigger: Invoked:");
		LDU40_ValidateTimer = new Timer();
		// LDU40_ValidateTimer.schedule(new LDU40_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU41_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU41_ValidateSerialCmdTrigger: Invoked:");
		LDU41_ValidateTimer = new Timer();
		// LDU41_ValidateTimer.schedule(new LDU41_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU42_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU42_ValidateSerialCmdTrigger: Invoked:");
		LDU42_ValidateTimer = new Timer();
		// LDU42_ValidateTimer.schedule(new LDU42_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU43_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU43_ValidateSerialCmdTrigger: Invoked:");
		LDU43_ValidateTimer = new Timer();
		// LDU43_ValidateTimer.schedule(new LDU43_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU44_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU44_ValidateSerialCmdTrigger: Invoked:");
		LDU44_ValidateTimer = new Timer();
		// LDU44_ValidateTimer.schedule(new LDU44_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU45_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU45_ValidateSerialCmdTrigger: Invoked:");
		LDU45_ValidateTimer = new Timer();
		// LDU45_ValidateTimer.schedule(new LDU45_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU46_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU46_ValidateSerialCmdTrigger: Invoked:");
		LDU46_ValidateTimer = new Timer();
		// LDU46_ValidateTimer.schedule(new LDU46_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU47_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU47_ValidateSerialCmdTrigger: Invoked:");
		LDU47_ValidateTimer = new Timer();
		// LDU47_ValidateTimer.schedule(new LDU47_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU48_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU48_ValidateSerialCmdTrigger: Invoked:");
		LDU48_ValidateTimer = new Timer();
		// LDU48_ValidateTimer.schedule(new LDU48_ValidateTimerTask(),100);// 1000);
	}

	class PwrSrcValidateTimerTask extends TimerTask {
		public void run() {
			btnValidatePwrSrcCmd.setDisable(true);
			WindowManager.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("PwrSrcValidateTimerTask: WAIT");
			try {
				// PwrSrcValidateSerialCmd();
			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger.error("PwrSrcValidateTimerTask: Exception:" + e.getMessage());
			}
			PwrSrcValidateTimer.cancel();
			WindowManager.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("PwrSrcValidateTimerTask: DEFAULT");
			btnValidatePwrSrcCmd.setDisable(false);
		}
	}

	class RefStdValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateRefStdCmd.setDisable(true);
			WindowManager.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("RefStdValidateTimerTask: WAIT");
			try {

				RefStdValidateSerialCmd();
			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger.error("RefStdValidateTimerTask: Exception:" + e.getMessage());
			}
			RefStdValidateTimer.cancel();
			WindowManager.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("RefStdValidateTimerTask: DEFAULT");
			btnValidateRefStdCmd.setDisable(false);
		}
	}

	class Qr1_ValidateTimerTask extends TimerTask {
		public void run() {
			ref_btnValidateQr1_Cmd.setDisable(true);
			WindowManager.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("Qr1_ValidateTimerTask: WAIT");
			try {
				/*
				 * if(ProcalFeatureEnable.CCUBE_LDU_CONNECTED){
				 * LDU_ValidateSerialCmd();
				 * } else if (ProcalFeatureEnable.LSCS_LDU_CONNECTED){
				 * lscsLDU1_ValidateSerialCmd();
				 * }
				 */
				qr1_ValidateSerialCmd();

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger.error("Qr1_ValidateTimerTask: Exception:" + e.getMessage());
			}
			qr1_ValidateTimer.cancel();
			WindowManager.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("Qr1_ValidateTimerTask: DEFAULT");
			ref_btnValidateQr1_Cmd.setDisable(false);
		}
	}

	class Qr2_ValidateTimerTask extends TimerTask {
		public void run() {
			ref_btnValidateQr2_Cmd.setDisable(true);
			WindowManager.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("Qr2_ValidateTimerTask: WAIT");
			try {
				qr2_ValidateSerialCmd();

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger.error("Qr2_ValidateTimerTask: Exception:" + e.getMessage());
			}
			qr2_ValidateTimer.cancel();
			WindowManager.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("Qr2_ValidateTimerTask: DEFAULT");
			ref_btnValidateQr2_Cmd.setDisable(false);
		}
	}

	class Qr3_ValidateTimerTask extends TimerTask {
		public void run() {
			ref_btnValidateQr3_Cmd.setDisable(true);
			WindowManager.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("Qr3_ValidateTimerTask: WAIT");
			try {
				qr3_ValidateSerialCmd();

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger.error("Qr3_ValidateTimerTask: Exception:" + e.getMessage());
			}
			qr3_ValidateTimer.cancel();
			WindowManager.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("Qr3_ValidateTimerTask: DEFAULT");
			ref_btnValidateQr3_Cmd.setDisable(false);
		}
	}

	class Qr4_ValidateTimerTask extends TimerTask {
		public void run() {
			ref_btnValidateQr4_Cmd.setDisable(true);
			WindowManager.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("Qr4_ValidateTimerTask: WAIT");
			try {
				qr4_ValidateSerialCmd();

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger.error("Qr4_ValidateTimerTask: Exception:" + e.getMessage());
			}
			qr4_ValidateTimer.cancel();
			WindowManager.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("Qr4_ValidateTimerTask: DEFAULT");
			ref_btnValidateQr4_Cmd.setDisable(false);
		}
	}

	class Qr5_ValidateTimerTask extends TimerTask {
		public void run() {
			ref_btnValidateQr5_Cmd.setDisable(true);
			WindowManager.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("Qr5_ValidateTimerTask: WAIT");
			try {
				qr5_ValidateSerialCmd();

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger.error("Qr5_ValidateTimerTask: Exception:" + e.getMessage());
			}
			qr5_ValidateTimer.cancel();
			WindowManager.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("Qr5_ValidateTimerTask: DEFAULT");
			ref_btnValidateQr5_Cmd.setDisable(false);
		}
	}

	class Qr6_ValidateTimerTask extends TimerTask {
		public void run() {
			ref_btnValidateQr6_Cmd.setDisable(true);
			WindowManager.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("Qr6_ValidateTimerTask: WAIT");
			try {
				qr6_ValidateSerialCmd();

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger.error("Qr6_ValidateTimerTask: Exception:" + e.getMessage());
			}
			qr6_ValidateTimer.cancel();
			WindowManager.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("Qr6_ValidateTimerTask: DEFAULT");
			ref_btnValidateQr6_Cmd.setDisable(false);
		}
	}

	public void enableBusyLoadingScreen() {// long time_in_seconds) {
		ApplicationLauncher.logger.info("SystemSettingController: enableBusyLoadingScreen: entry");

		// FXMLLoader loader = new FXMLLoader(
		// getClass().getResource("/fxml/setting/ScanDevice" + ConstantApp.THEME_FXML));
		Parent nodeFromFXML = null;
		try {
			nodeFromFXML = getNodeFromFXML("/fxml/setting/BusyLoading" + ConstantApp.THEME_FXML);
			ApplicationHomeController.displayBusyLoadingScreen(nodeFromFXML);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger
					.error("SystemSettingController: enableBusyLoadingScreen: Exception: " + e.getMessage());
		}
	}

	public void disableBusyLoadingScreen() {
		BusyLoadingController.removeBusyLoadingScreenOverlay();
	}

	private Parent getNodeFromFXML(String url) throws IOException {
		return FXMLLoader.load(getClass().getResource(url));
	}

	@FXML
	public void cmbBxQr1BaySelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr1BaySelectionOnChange: Entry");
		qr1BaySelectionOnChangeTimer = new Timer();
		qr1BaySelectionOnChangeTimer.schedule(new Qr1BaySelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr3BaySelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr3BaySelectionOnChange: Entry");
		qr3BaySelectionOnChangeTimer = new Timer();
		qr3BaySelectionOnChangeTimer.schedule(new Qr3BaySelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr4BaySelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr4BaySelectionOnChange: Entry");
		qr4BaySelectionOnChangeTimer = new Timer();
		qr4BaySelectionOnChangeTimer.schedule(new Qr4BaySelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr5BaySelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr5BaySelectionOnChange: Entry");
		qr5BaySelectionOnChangeTimer = new Timer();
		qr5BaySelectionOnChangeTimer.schedule(new Qr5BaySelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr6BaySelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr6BaySelectionOnChange: Entry");
		qr6BaySelectionOnChangeTimer = new Timer();
		qr6BaySelectionOnChangeTimer.schedule(new Qr6BaySelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr7BaySelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr7BaySelectionOnChange: Entry");
		qr7BaySelectionOnChangeTimer = new Timer();
		qr7BaySelectionOnChangeTimer.schedule(new Qr7BaySelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr8BaySelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr8BaySelectionOnChange: Entry");
		qr8BaySelectionOnChangeTimer = new Timer();
		qr8BaySelectionOnChangeTimer.schedule(new Qr8BaySelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr9BaySelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr9BaySelectionOnChange: Entry");
		qr9BaySelectionOnChangeTimer = new Timer();
		qr9BaySelectionOnChangeTimer.schedule(new Qr9BaySelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr10BaySelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr10BaySelectionOnChange: Entry");
		qr10BaySelectionOnChangeTimer = new Timer();
		qr10BaySelectionOnChangeTimer.schedule(new Qr10BaySelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr11BaySelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr11BaySelectionOnChange: Entry");
		qr11BaySelectionOnChangeTimer = new Timer();
		qr11BaySelectionOnChangeTimer.schedule(new Qr11BaySelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr12BaySelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr12BaySelectionOnChange: Entry");
		qr12BaySelectionOnChangeTimer = new Timer();
		qr12BaySelectionOnChangeTimer.schedule(new Qr12BaySelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr13BaySelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr13BaySelectionOnChange: Entry");
		qr13BaySelectionOnChangeTimer = new Timer();
		qr13BaySelectionOnChangeTimer.schedule(new Qr13BaySelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr1ClusterSelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr1ClusterSelectionOnChange: Entry");
		qr1ClusterSelectionOnChangeTimer = new Timer();
		qr1ClusterSelectionOnChangeTimer.schedule(new Qr1ClusterSelectionOnChangeTask(), 10);
	}

	// ref_cmbBxQrClusterId1;
	// ref_cmbBxQrBayId1;

	class Qr1ClusterSelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxQr1ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {
					/*
					 * ref_tbViewOutputPortData.getItems().clear();
					 * ref_tbViewInputPortData.getItems().clear();
					 */
					ref_cmbBxQr1BayId.getItems().clear();
					/*
					 * ref_txtClusterIpAddress.setText("");
					 * ref_txtClusterPortNo.setText("");
					 */
					if (getClusterBayNameListMap().containsKey(selectedClusterName)) {
						ref_cmbBxQr1BayId.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));
					}
					ref_cmbBxQr1BayId.getSelectionModel().select(0);

				}
			});
			qr1ClusterSelectionOnChangeTimer.cancel();

		}
	}

	class Qr1BaySelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {

				String selectedClusterName = (String) ref_cmbBxQr1ClusterId.getSelectionModel().getSelectedItem();
				String selectedBayName = (String) ref_cmbBxQr1BayId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxQr1PositionId.getItems().clear();
					if (getClusterBayNamePositionListMap().containsKey(selectedClusterName + "_" + selectedBayName)) {
						ref_cmbBxQr1PositionId.getItems().addAll(
								getClusterBayNamePositionListMap().get(selectedClusterName + "_" + selectedBayName));
						ref_cmbBxQr1PositionId.getSelectionModel().select(0);
						String selectionPositionNo = (String) ref_cmbBxQr1PositionId.getSelectionModel()
								.getSelectedItem();
						if (getClusterBayPositionNoCnameMap()
								.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo)) {
							ref_txtQr1Cname.setText(getClusterBayPositionNoCnameMap()
									.get(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo));
						}
					} else {
						ref_txtQr1Cname.setText("");
					}
				}
			});
			qr1BaySelectionOnChangeTimer.cancel();
		}
	}

	class Qr3BaySelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {

				String selectedClusterName = (String) ref_cmbBxQr3ClusterId.getSelectionModel().getSelectedItem();
				String selectedBayName = (String) ref_cmbBxQr3BayId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxQr3PositionId.getItems().clear();
					if (getClusterBayNamePositionListMap().containsKey(selectedClusterName + "_" + selectedBayName)) {
						ref_cmbBxQr3PositionId.getItems().addAll(
								getClusterBayNamePositionListMap().get(selectedClusterName + "_" + selectedBayName));
						ref_cmbBxQr3PositionId.getSelectionModel().select(0);
						String selectionPositionNo = (String) ref_cmbBxQr3PositionId.getSelectionModel()
								.getSelectedItem();
						if (getClusterBayPositionNoCnameMap()
								.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo)) {
							ref_txtQr3Cname.setText(getClusterBayPositionNoCnameMap()
									.get(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo));
						}
					} else {
						ref_txtQr3Cname.setText("");
					}
				}
			});
			qr3BaySelectionOnChangeTimer.cancel();
		}
	}

	class Qr4BaySelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {

				String selectedClusterName = (String) ref_cmbBxQr4ClusterId.getSelectionModel().getSelectedItem();
				String selectedBayName = (String) ref_cmbBxQr4BayId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxQr4PositionId.getItems().clear();
					if (getClusterBayNamePositionListMap().containsKey(selectedClusterName + "_" + selectedBayName)) {
						ref_cmbBxQr4PositionId.getItems().addAll(
								getClusterBayNamePositionListMap().get(selectedClusterName + "_" + selectedBayName));
						ref_cmbBxQr4PositionId.getSelectionModel().select(0);
						String selectionPositionNo = (String) ref_cmbBxQr4PositionId.getSelectionModel()
								.getSelectedItem();
						if (getClusterBayPositionNoCnameMap()
								.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo)) {
							ref_txtQr4Cname.setText(getClusterBayPositionNoCnameMap()
									.get(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo));
						}
					} else {
						ref_txtQr4Cname.setText("");
					}
				}
			});
			qr4BaySelectionOnChangeTimer.cancel();
		}
	}

	class Qr5BaySelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {

				String selectedClusterName = (String) ref_cmbBxQr5ClusterId.getSelectionModel().getSelectedItem();
				String selectedBayName = (String) ref_cmbBxQr5BayId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxQr5PositionId.getItems().clear();
					if (getClusterBayNamePositionListMap().containsKey(selectedClusterName + "_" + selectedBayName)) {
						ref_cmbBxQr5PositionId.getItems().addAll(
								getClusterBayNamePositionListMap().get(selectedClusterName + "_" + selectedBayName));
						ref_cmbBxQr5PositionId.getSelectionModel().select(0);
						String selectionPositionNo = (String) ref_cmbBxQr5PositionId.getSelectionModel()
								.getSelectedItem();
						if (getClusterBayPositionNoCnameMap()
								.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo)) {
							ref_txtQr5Cname.setText(getClusterBayPositionNoCnameMap()
									.get(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo));
						}
					} else {
						ref_txtQr5Cname.setText("");
					}
				}
			});
			qr5BaySelectionOnChangeTimer.cancel();
		}
	}

	class Qr6BaySelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {

				String selectedClusterName = (String) ref_cmbBxQr6ClusterId.getSelectionModel().getSelectedItem();
				String selectedBayName = (String) ref_cmbBxQr6BayId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxQr6PositionId.getItems().clear();
					if (getClusterBayNamePositionListMap().containsKey(selectedClusterName + "_" + selectedBayName)) {
						ref_cmbBxQr6PositionId.getItems().addAll(
								getClusterBayNamePositionListMap().get(selectedClusterName + "_" + selectedBayName));
						ref_cmbBxQr6PositionId.getSelectionModel().select(0);
						String selectionPositionNo = (String) ref_cmbBxQr6PositionId.getSelectionModel()
								.getSelectedItem();
						if (getClusterBayPositionNoCnameMap()
								.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo)) {
							ref_txtQr6Cname.setText(getClusterBayPositionNoCnameMap()
									.get(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo));
						}
					} else {
						ref_txtQr6Cname.setText("");
					}
				}
			});
			qr6BaySelectionOnChangeTimer.cancel();
		}
	}

	class Qr7BaySelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {

				String selectedClusterName = (String) ref_cmbBxQr7ClusterId.getSelectionModel().getSelectedItem();
				String selectedBayName = (String) ref_cmbBxQr7BayId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxQr7PositionId.getItems().clear();
					if (getClusterBayNamePositionListMap().containsKey(selectedClusterName + "_" + selectedBayName)) {
						ref_cmbBxQr7PositionId.getItems().addAll(
								getClusterBayNamePositionListMap().get(selectedClusterName + "_" + selectedBayName));
						ref_cmbBxQr7PositionId.getSelectionModel().select(0);
						String selectionPositionNo = (String) ref_cmbBxQr7PositionId.getSelectionModel()
								.getSelectedItem();
						if (getClusterBayPositionNoCnameMap()
								.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo)) {
							ref_txtQr7Cname.setText(getClusterBayPositionNoCnameMap()
									.get(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo));
						}
					} else {
						ref_txtQr7Cname.setText("");
					}
				}
			});
			qr7BaySelectionOnChangeTimer.cancel();
		}
	}

	class Qr8BaySelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {

				String selectedClusterName = (String) ref_cmbBxQr8ClusterId.getSelectionModel().getSelectedItem();
				String selectedBayName = (String) ref_cmbBxQr8BayId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxQr8PositionId.getItems().clear();
					if (getClusterBayNamePositionListMap().containsKey(selectedClusterName + "_" + selectedBayName)) {
						ref_cmbBxQr8PositionId.getItems().addAll(
								getClusterBayNamePositionListMap().get(selectedClusterName + "_" + selectedBayName));
						ref_cmbBxQr8PositionId.getSelectionModel().select(0);
						String selectionPositionNo = (String) ref_cmbBxQr8PositionId.getSelectionModel()
								.getSelectedItem();
						if (getClusterBayPositionNoCnameMap()
								.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo)) {
							ref_txtQr8Cname.setText(getClusterBayPositionNoCnameMap()
									.get(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo));
						}
					} else {
						ref_txtQr8Cname.setText("");
					}
				}
			});
			qr8BaySelectionOnChangeTimer.cancel();
		}
	}

	class Qr9BaySelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {

				String selectedClusterName = (String) ref_cmbBxQr9ClusterId.getSelectionModel().getSelectedItem();
				String selectedBayName = (String) ref_cmbBxQr9BayId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxQr9PositionId.getItems().clear();
					if (getClusterBayNamePositionListMap().containsKey(selectedClusterName + "_" + selectedBayName)) {
						ref_cmbBxQr9PositionId.getItems().addAll(
								getClusterBayNamePositionListMap().get(selectedClusterName + "_" + selectedBayName));
						ref_cmbBxQr9PositionId.getSelectionModel().select(0);
						String selectionPositionNo = (String) ref_cmbBxQr9PositionId.getSelectionModel()
								.getSelectedItem();
						if (getClusterBayPositionNoCnameMap()
								.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo)) {
							ref_txtQr9Cname.setText(getClusterBayPositionNoCnameMap()
									.get(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo));
						}
					} else {
						ref_txtQr9Cname.setText("");
					}
				}
			});
			qr9BaySelectionOnChangeTimer.cancel();
		}
	}

	class Qr10BaySelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {

				String selectedClusterName = (String) ref_cmbBxQr10ClusterId.getSelectionModel().getSelectedItem();
				String selectedBayName = (String) ref_cmbBxQr10BayId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxQr10PositionId.getItems().clear();
					if (getClusterBayNamePositionListMap().containsKey(selectedClusterName + "_" + selectedBayName)) {
						ref_cmbBxQr10PositionId.getItems().addAll(
								getClusterBayNamePositionListMap().get(selectedClusterName + "_" + selectedBayName));
						ref_cmbBxQr10PositionId.getSelectionModel().select(0);
						String selectionPositionNo = (String) ref_cmbBxQr10PositionId.getSelectionModel()
								.getSelectedItem();
						if (getClusterBayPositionNoCnameMap()
								.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo)) {
							ref_txtQr10Cname.setText(getClusterBayPositionNoCnameMap()
									.get(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo));
						}
					} else {
						ref_txtQr10Cname.setText("");
					}
				}
			});
			qr10BaySelectionOnChangeTimer.cancel();
		}
	}

	class Qr11BaySelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {

				String selectedClusterName = (String) ref_cmbBxQr11ClusterId.getSelectionModel().getSelectedItem();
				String selectedBayName = (String) ref_cmbBxQr11BayId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxQr11PositionId.getItems().clear();
					if (getClusterBayNamePositionListMap().containsKey(selectedClusterName + "_" + selectedBayName)) {
						ref_cmbBxQr11PositionId.getItems().addAll(
								getClusterBayNamePositionListMap().get(selectedClusterName + "_" + selectedBayName));
						ref_cmbBxQr11PositionId.getSelectionModel().select(0);
						String selectionPositionNo = (String) ref_cmbBxQr11PositionId.getSelectionModel()
								.getSelectedItem();
						if (getClusterBayPositionNoCnameMap()
								.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo)) {
							ref_txtQr11Cname.setText(getClusterBayPositionNoCnameMap()
									.get(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo));
						}
					} else {
						ref_txtQr11Cname.setText("");
					}
				}
			});
			qr11BaySelectionOnChangeTimer.cancel();
		}
	}

	class Qr12BaySelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {

				String selectedClusterName = (String) ref_cmbBxQr12ClusterId.getSelectionModel().getSelectedItem();
				String selectedBayName = (String) ref_cmbBxQr12BayId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxQr12PositionId.getItems().clear();
					if (getClusterBayNamePositionListMap().containsKey(selectedClusterName + "_" + selectedBayName)) {
						ref_cmbBxQr12PositionId.getItems().addAll(
								getClusterBayNamePositionListMap().get(selectedClusterName + "_" + selectedBayName));
						ref_cmbBxQr12PositionId.getSelectionModel().select(0);
						String selectionPositionNo = (String) ref_cmbBxQr12PositionId.getSelectionModel()
								.getSelectedItem();
						if (getClusterBayPositionNoCnameMap()
								.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo)) {
							ref_txtQr12Cname.setText(getClusterBayPositionNoCnameMap()
									.get(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo));
						}
					} else {
						ref_txtQr12Cname.setText("");
					}
				}
			});
			qr12BaySelectionOnChangeTimer.cancel();
		}
	}

	class Qr13BaySelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {

				String selectedClusterName = (String) ref_cmbBxQr13ClusterId.getSelectionModel().getSelectedItem();
				String selectedBayName = (String) ref_cmbBxQr13BayId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxQr13PositionId.getItems().clear();
					if (getClusterBayNamePositionListMap().containsKey(selectedClusterName + "_" + selectedBayName)) {
						ref_cmbBxQr13PositionId.getItems().addAll(
								getClusterBayNamePositionListMap().get(selectedClusterName + "_" + selectedBayName));
						ref_cmbBxQr13PositionId.getSelectionModel().select(0);
						String selectionPositionNo = (String) ref_cmbBxQr13PositionId.getSelectionModel()
								.getSelectedItem();
						if (getClusterBayPositionNoCnameMap()
								.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo)) {
							ref_txtQr13Cname.setText(getClusterBayPositionNoCnameMap()
									.get(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo));
						}
					} else {
						ref_txtQr13Cname.setText("");
					}
				}
			});
			qr13BaySelectionOnChangeTimer.cancel();
		}
	}

	@FXML
	public void cmbBxQr1PositionIdOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr1PositionIdOnChange: Entry");
		qr1PositionIdOnChangeTimer = new Timer();
		qr1PositionIdOnChangeTimer.schedule(new Qr1PositionIdOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr3PositionIdOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr3PositionIdOnChange: Entry");
		qr3PositionIdOnChangeTimer = new Timer();
		qr3PositionIdOnChangeTimer.schedule(new Qr3PositionIdOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr4PositionIdOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr4PositionIdOnChange: Entry");
		qr4PositionIdOnChangeTimer = new Timer();
		qr4PositionIdOnChangeTimer.schedule(new Qr4PositionIdOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr5PositionIdOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr5PositionIdOnChange: Entry");
		qr5PositionIdOnChangeTimer = new Timer();
		qr5PositionIdOnChangeTimer.schedule(new Qr5PositionIdOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr6PositionIdOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr6PositionIdOnChange: Entry");
		qr6PositionIdOnChangeTimer = new Timer();
		qr6PositionIdOnChangeTimer.schedule(new Qr6PositionIdOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr7PositionIdOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr7PositionIdOnChange: Entry");
		qr7PositionIdOnChangeTimer = new Timer();
		qr7PositionIdOnChangeTimer.schedule(new Qr7PositionIdOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr8PositionIdOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr8PositionIdOnChange: Entry");
		qr8PositionIdOnChangeTimer = new Timer();
		qr8PositionIdOnChangeTimer.schedule(new Qr8PositionIdOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr9PositionIdOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr9PositionIdOnChange: Entry");
		qr9PositionIdOnChangeTimer = new Timer();
		qr9PositionIdOnChangeTimer.schedule(new Qr9PositionIdOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr10PositionIdOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr10PositionIdOnChange: Entry");
		qr10PositionIdOnChangeTimer = new Timer();
		qr10PositionIdOnChangeTimer.schedule(new Qr10PositionIdOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr11PositionIdOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr11PositionIdOnChange: Entry");
		qr11PositionIdOnChangeTimer = new Timer();
		qr11PositionIdOnChangeTimer.schedule(new Qr11PositionIdOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr12PositionIdOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr12PositionIdOnChange: Entry");
		qr12PositionIdOnChangeTimer = new Timer();
		qr12PositionIdOnChangeTimer.schedule(new Qr12PositionIdOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr13PositionIdOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr13PositionIdOnChange: Entry");
		qr13PositionIdOnChangeTimer = new Timer();
		qr13PositionIdOnChangeTimer.schedule(new Qr13PositionIdOnChangeTask(), 10);
	}

	// ref_cmbBxQrClusterId1;
	// ref_cmbBxQrBayId1;

	class Qr1PositionIdOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxQr1ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					String selectedBayName = (String) ref_cmbBxQr1BayId.getSelectionModel().getSelectedItem();
					String selectedPositionNo = (String) ref_cmbBxQr1PositionId.getSelectionModel().getSelectedItem();
					if (getClusterBayPositionNoCnameMap()
							.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo)) {
						ref_txtQr1Cname.setText(getClusterBayPositionNoCnameMap()
								.get(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo));
					}

				}
			});
			qr1PositionIdOnChangeTimer.cancel();

		}
	}

	class Qr3PositionIdOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxQr3ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					String selectedBayName = (String) ref_cmbBxQr3BayId.getSelectionModel().getSelectedItem();
					String selectedPositionNo = (String) ref_cmbBxQr3PositionId.getSelectionModel().getSelectedItem();
					if (getClusterBayPositionNoCnameMap()
							.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo)) {
						ref_txtQr3Cname.setText(getClusterBayPositionNoCnameMap()
								.get(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo));
					}

				}
			});
			qr3PositionIdOnChangeTimer.cancel();

		}
	}

	class Qr4PositionIdOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxQr4ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					String selectedBayName = (String) ref_cmbBxQr4BayId.getSelectionModel().getSelectedItem();
					String selectedPositionNo = (String) ref_cmbBxQr4PositionId.getSelectionModel().getSelectedItem();
					if (getClusterBayPositionNoCnameMap()
							.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo)) {
						ref_txtQr4Cname.setText(getClusterBayPositionNoCnameMap()
								.get(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo));
					}

				}
			});
			qr4PositionIdOnChangeTimer.cancel();

		}
	}

	class Qr5PositionIdOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxQr5ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					String selectedBayName = (String) ref_cmbBxQr5BayId.getSelectionModel().getSelectedItem();
					String selectedPositionNo = (String) ref_cmbBxQr5PositionId.getSelectionModel().getSelectedItem();
					if (getClusterBayPositionNoCnameMap()
							.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo)) {
						ref_txtQr5Cname.setText(getClusterBayPositionNoCnameMap()
								.get(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo));
					}

				}
			});
			qr5PositionIdOnChangeTimer.cancel();

		}
	}

	class Qr6PositionIdOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxQr6ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					String selectedBayName = (String) ref_cmbBxQr6BayId.getSelectionModel().getSelectedItem();
					String selectedPositionNo = (String) ref_cmbBxQr6PositionId.getSelectionModel().getSelectedItem();
					if (getClusterBayPositionNoCnameMap()
							.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo)) {
						ref_txtQr6Cname.setText(getClusterBayPositionNoCnameMap()
								.get(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo));
					}

				}
			});
			qr6PositionIdOnChangeTimer.cancel();

		}
	}

	class Qr7PositionIdOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxQr7ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					String selectedBayName = (String) ref_cmbBxQr7BayId.getSelectionModel().getSelectedItem();
					String selectedPositionNo = (String) ref_cmbBxQr7PositionId.getSelectionModel().getSelectedItem();
					if (getClusterBayPositionNoCnameMap()
							.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo)) {
						ref_txtQr7Cname.setText(getClusterBayPositionNoCnameMap()
								.get(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo));
					}

				}
			});
			qr7PositionIdOnChangeTimer.cancel();

		}
	}

	class Qr8PositionIdOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxQr8ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					String selectedBayName = (String) ref_cmbBxQr8BayId.getSelectionModel().getSelectedItem();
					String selectedPositionNo = (String) ref_cmbBxQr8PositionId.getSelectionModel().getSelectedItem();
					if (getClusterBayPositionNoCnameMap()
							.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo)) {
						ref_txtQr8Cname.setText(getClusterBayPositionNoCnameMap()
								.get(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo));
					}

				}
			});
			qr8PositionIdOnChangeTimer.cancel();

		}
	}

	class Qr9PositionIdOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxQr9ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					String selectedBayName = (String) ref_cmbBxQr9BayId.getSelectionModel().getSelectedItem();
					String selectedPositionNo = (String) ref_cmbBxQr9PositionId.getSelectionModel().getSelectedItem();
					if (getClusterBayPositionNoCnameMap()
							.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo)) {
						ref_txtQr9Cname.setText(getClusterBayPositionNoCnameMap()
								.get(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo));
					}

				}
			});
			qr9PositionIdOnChangeTimer.cancel();

		}
	}

	class Qr10PositionIdOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxQr10ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					String selectedBayName = (String) ref_cmbBxQr10BayId.getSelectionModel().getSelectedItem();
					String selectedPositionNo = (String) ref_cmbBxQr10PositionId.getSelectionModel().getSelectedItem();
					if (getClusterBayPositionNoCnameMap()
							.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo)) {
						ref_txtQr10Cname.setText(getClusterBayPositionNoCnameMap()
								.get(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo));
					}

				}
			});
			qr10PositionIdOnChangeTimer.cancel();

		}
	}

	class Qr11PositionIdOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxQr11ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					String selectedBayName = (String) ref_cmbBxQr11BayId.getSelectionModel().getSelectedItem();
					String selectedPositionNo = (String) ref_cmbBxQr11PositionId.getSelectionModel().getSelectedItem();
					if (getClusterBayPositionNoCnameMap()
							.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo)) {
						ref_txtQr11Cname.setText(getClusterBayPositionNoCnameMap()
								.get(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo));
					}

				}
			});
			qr11PositionIdOnChangeTimer.cancel();

		}
	}

	class Qr12PositionIdOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxQr12ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					String selectedBayName = (String) ref_cmbBxQr12BayId.getSelectionModel().getSelectedItem();
					String selectedPositionNo = (String) ref_cmbBxQr12PositionId.getSelectionModel().getSelectedItem();
					if (getClusterBayPositionNoCnameMap()
							.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo)) {
						ref_txtQr12Cname.setText(getClusterBayPositionNoCnameMap()
								.get(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo));
					}

				}
			});
			qr12PositionIdOnChangeTimer.cancel();

		}
	}

	class Qr13PositionIdOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxQr13ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					String selectedBayName = (String) ref_cmbBxQr13BayId.getSelectionModel().getSelectedItem();
					String selectedPositionNo = (String) ref_cmbBxQr13PositionId.getSelectionModel().getSelectedItem();
					if (getClusterBayPositionNoCnameMap()
							.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo)) {
						ref_txtQr13Cname.setText(getClusterBayPositionNoCnameMap()
								.get(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo));
					}

				}
			});
			qr13PositionIdOnChangeTimer.cancel();

		}
	}

	@FXML
	public void cmbBxQr2BaySelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr2BaySelectionOnChange: Entry");
		qr2BaySelectionOnChangeTimer = new Timer();
		qr2BaySelectionOnChangeTimer.schedule(new Qr2BaySelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr2ClusterSelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr2ClusterSelectionOnChange: Entry");
		qr2ClusterSelectionOnChangeTimer = new Timer();
		qr2ClusterSelectionOnChangeTimer.schedule(new Qr2ClusterSelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr3ClusterSelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr3ClusterSelectionOnChange: Entry");
		qr3ClusterSelectionOnChangeTimer = new Timer();
		qr3ClusterSelectionOnChangeTimer.schedule(new Qr3ClusterSelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr4ClusterSelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr4ClusterSelectionOnChange: Entry");
		qr4ClusterSelectionOnChangeTimer = new Timer();
		qr4ClusterSelectionOnChangeTimer.schedule(new Qr4ClusterSelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr5ClusterSelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr5ClusterSelectionOnChange: Entry");
		qr5ClusterSelectionOnChangeTimer = new Timer();
		qr5ClusterSelectionOnChangeTimer.schedule(new Qr5ClusterSelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr6ClusterSelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr6ClusterSelectionOnChange: Entry");
		qr6ClusterSelectionOnChangeTimer = new Timer();
		qr6ClusterSelectionOnChangeTimer.schedule(new Qr6ClusterSelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr7ClusterSelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr7ClusterSelectionOnChange: Entry");
		qr7ClusterSelectionOnChangeTimer = new Timer();
		qr7ClusterSelectionOnChangeTimer.schedule(new Qr7ClusterSelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr8ClusterSelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr8ClusterSelectionOnChange: Entry");
		qr8ClusterSelectionOnChangeTimer = new Timer();
		qr8ClusterSelectionOnChangeTimer.schedule(new Qr8ClusterSelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr9ClusterSelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr9ClusterSelectionOnChange: Entry");
		qr9ClusterSelectionOnChangeTimer = new Timer();
		qr9ClusterSelectionOnChangeTimer.schedule(new Qr9ClusterSelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr10ClusterSelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr10ClusterSelectionOnChange: Entry");
		qr10ClusterSelectionOnChangeTimer = new Timer();
		qr10ClusterSelectionOnChangeTimer.schedule(new Qr10ClusterSelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr11ClusterSelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr11ClusterSelectionOnChange: Entry");
		qr11ClusterSelectionOnChangeTimer = new Timer();
		qr11ClusterSelectionOnChangeTimer.schedule(new Qr11ClusterSelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr12ClusterSelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr12ClusterSelectionOnChange: Entry");
		qr12ClusterSelectionOnChangeTimer = new Timer();
		qr12ClusterSelectionOnChangeTimer.schedule(new Qr12ClusterSelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxQr13ClusterSelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr13ClusterSelectionOnChange: Entry");
		qr13ClusterSelectionOnChangeTimer = new Timer();
		qr13ClusterSelectionOnChangeTimer.schedule(new Qr13ClusterSelectionOnChangeTask(), 10);
	}

	// ref_cmbBxQrClusterId1;
	// ref_cmbBxQrBayId1;

	class Qr2ClusterSelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxQr2ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxQr2BayId.getItems().clear();
					if (getClusterBayNameListMap().containsKey(selectedClusterName)) {
						ref_cmbBxQr2BayId.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));
					}
					ref_cmbBxQr2BayId.getSelectionModel().select(0);

				}
			});
			qr2ClusterSelectionOnChangeTimer.cancel();
		}
	}

	class Qr3ClusterSelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxQr3ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxQr3BayId.getItems().clear();
					if (getClusterBayNameListMap().containsKey(selectedClusterName)) {
						ref_cmbBxQr3BayId.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));
					}
					ref_cmbBxQr3BayId.getSelectionModel().select(0);

				}
			});
			qr3ClusterSelectionOnChangeTimer.cancel();
		}
	}

	class Qr4ClusterSelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxQr4ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxQr4BayId.getItems().clear();
					if (getClusterBayNameListMap().containsKey(selectedClusterName)) {
						ref_cmbBxQr4BayId.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));
					}
					ref_cmbBxQr4BayId.getSelectionModel().select(0);

				}
			});
			qr4ClusterSelectionOnChangeTimer.cancel();
		}
	}

	class Qr5ClusterSelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxQr5ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxQr5BayId.getItems().clear();
					if (getClusterBayNameListMap().containsKey(selectedClusterName)) {
						ref_cmbBxQr5BayId.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));
					}
					ref_cmbBxQr5BayId.getSelectionModel().select(0);

				}
			});
			qr5ClusterSelectionOnChangeTimer.cancel();
		}
	}

	class Qr6ClusterSelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxQr6ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxQr6BayId.getItems().clear();
					if (getClusterBayNameListMap().containsKey(selectedClusterName)) {
						ref_cmbBxQr6BayId.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));
					}
					ref_cmbBxQr6BayId.getSelectionModel().select(0);

				}
			});
			qr6ClusterSelectionOnChangeTimer.cancel();
		}
	}

	class Qr7ClusterSelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxQr7ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxQr7BayId.getItems().clear();
					if (getClusterBayNameListMap().containsKey(selectedClusterName)) {
						ref_cmbBxQr7BayId.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));
					}
					ref_cmbBxQr7BayId.getSelectionModel().select(0);

				}
			});
			qr7ClusterSelectionOnChangeTimer.cancel();
		}
	}

	class Qr8ClusterSelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxQr8ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxQr8BayId.getItems().clear();
					if (getClusterBayNameListMap().containsKey(selectedClusterName)) {
						ref_cmbBxQr8BayId.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));
					}
					ref_cmbBxQr8BayId.getSelectionModel().select(0);

				}
			});
			qr8ClusterSelectionOnChangeTimer.cancel();
		}
	}

	class Qr9ClusterSelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxQr9ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxQr9BayId.getItems().clear();
					if (getClusterBayNameListMap().containsKey(selectedClusterName)) {
						ref_cmbBxQr9BayId.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));
					}
					ref_cmbBxQr9BayId.getSelectionModel().select(0);

				}
			});
			qr9ClusterSelectionOnChangeTimer.cancel();
		}
	}

	class Qr10ClusterSelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxQr10ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxQr10BayId.getItems().clear();
					if (getClusterBayNameListMap().containsKey(selectedClusterName)) {
						ref_cmbBxQr10BayId.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));
					}
					ref_cmbBxQr10BayId.getSelectionModel().select(0);

				}
			});
			qr10ClusterSelectionOnChangeTimer.cancel();
		}
	}

	class Qr11ClusterSelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxQr11ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxQr11BayId.getItems().clear();
					if (getClusterBayNameListMap().containsKey(selectedClusterName)) {
						ref_cmbBxQr11BayId.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));
					}
					ref_cmbBxQr11BayId.getSelectionModel().select(0);

				}
			});
			qr11ClusterSelectionOnChangeTimer.cancel();
		}
	}

	class Qr12ClusterSelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxQr12ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxQr12BayId.getItems().clear();
					if (getClusterBayNameListMap().containsKey(selectedClusterName)) {
						ref_cmbBxQr12BayId.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));
					}
					ref_cmbBxQr12BayId.getSelectionModel().select(0);

				}
			});
			qr12ClusterSelectionOnChangeTimer.cancel();
		}
	}

	class Qr13ClusterSelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxQr13ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxQr13BayId.getItems().clear();
					if (getClusterBayNameListMap().containsKey(selectedClusterName)) {
						ref_cmbBxQr13BayId.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));
					}
					ref_cmbBxQr13BayId.getSelectionModel().select(0);

				}
			});
			qr13ClusterSelectionOnChangeTimer.cancel();
		}
	}

	class Qr2BaySelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {

				String selectedClusterName = (String) ref_cmbBxQr2ClusterId.getSelectionModel().getSelectedItem();
				String selectedBayName = (String) ref_cmbBxQr2BayId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {
					// ref_tbViewOutputPortData.getItems().clear();
					// ref_tbViewInputPortData.getItems().clear();
					ref_cmbBxQr2PositionId.getItems().clear();
					if (getClusterBayNamePositionListMap().containsKey(selectedClusterName + "_" + selectedBayName)) {
						ref_cmbBxQr2PositionId.getItems().addAll(
								getClusterBayNamePositionListMap().get(selectedClusterName + "_" + selectedBayName));
						ref_cmbBxQr2PositionId.getSelectionModel().select(0);
						String selectionPositionNo = (String) ref_cmbBxQr2PositionId.getSelectionModel()
								.getSelectedItem();
						if (getClusterBayPositionNoCnameMap()
								.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo)) {
							ref_txtQr2Cname.setText(getClusterBayPositionNoCnameMap()
									.get(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo));
						}
					} else {
						ref_txtQr2Cname.setText("");
					}
					// if(getClusterBayNameListMap().containsKey(selectedClusterName)){
					// ref_cmbBxQrPositionId1.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));
					// }
					// ref_cmbBxQrPositionId1.getSelectionModel().select(0);

				}
			});
			qr2BaySelectionOnChangeTimer.cancel();

		}
	}

	@FXML
	public void cmbBxQr2PositionIdOnChange() {
		ApplicationLauncher.logger.debug("cmbBxQr2PositionIdOnChange: Entry");
		qr2PositionIdOnChangeTimer = new Timer();
		qr2PositionIdOnChangeTimer.schedule(new Qr2PositionIdOnChangeTask(), 10);
	}

	// ref_cmbBxQrClusterId1;
	// ref_cmbBxQrBayId1;

	class Qr2PositionIdOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxQr2ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {
					/*
					 * ref_tbViewOutputPortData.getItems().clear();
					 * ref_tbViewInputPortData.getItems().clear();
					 */
					// ref_cmbBxQrBayId1.getItems().clear();
					/*
					 * ref_txtClusterIpAddress.setText("");
					 * ref_txtClusterPortNo.setText("");
					 */
					String selectedBayName = (String) ref_cmbBxQr2BayId.getSelectionModel().getSelectedItem();
					String selectedPositionNo = (String) ref_cmbBxQr2PositionId.getSelectionModel().getSelectedItem();
					if (getClusterBayPositionNoCnameMap()
							.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo)) {
						ref_txtQr2Cname.setText(getClusterBayPositionNoCnameMap()
								.get(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo));
					}

				}
			});
			qr2PositionIdOnChangeTimer.cancel();

		}
	}

	public Map<String, String> getClusterBayNameIdMap() {
		return clusterBayNameIdMap;
	}

	public void setClusterBayNameIdMap(Map<String, String> clusterBayNameIdMap) {
		this.clusterBayNameIdMap = clusterBayNameIdMap;
	}

	public Map<String, String> getClusterBayPositionNoCnameMap() {
		return clusterBayPositionNoCnameMap;
	}

	public void setClusterBayPositionNoCnameMap(Map<String, String> clusterBayPositionNoCnameMap) {
		this.clusterBayPositionNoCnameMap = clusterBayPositionNoCnameMap;
	}

	public Map<String, ArrayList<String>> getClusterBayNamePositionListMap() {
		return clusterBayNamePositionListMap;
	}

	public void setClusterBayNamePositionListMap(Map<String, ArrayList<String>> clusterBayNamePositionListMap) {
		this.clusterBayNamePositionListMap = clusterBayNamePositionListMap;
	}

	public Map<String, String> getClusterNameIdListMap() {
		return clusterNameIdListMap;
	}

	public void setClusterNameIdListMap(Map<String, String> clusterIdNameListMap) {
		this.clusterNameIdListMap = clusterIdNameListMap;
	}

	public Map<String, ArrayList<String>> getClusterBayNameListMap() {
		return clusterBayNameListMap;
	}

	public void setClusterBayNameListMap(Map<String, ArrayList<String>> bayNameListMap) {
		this.clusterBayNameListMap = bayNameListMap;
	}

	public static TerminalBayConfigModel getBayConfigModel() {
		return bayConfigModel;
	}

	public static void setBayConfigModel(TerminalBayConfigModel bayConfigModel) {
		QrScannerPortSetupController.bayConfigModel = bayConfigModel;
	}

	public Map<String, String> getClusterBayPositionNoDeviceIdMap() {
		return clusterBayPositionNoDeviceIdMap;
	}

	public void setClusterBayPositionNoDeviceIdMap(Map<String, String> clusterBayPositionNoDeviceIdMap) {
		this.clusterBayPositionNoDeviceIdMap = clusterBayPositionNoDeviceIdMap;
	}

}
