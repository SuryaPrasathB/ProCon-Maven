package com.tasnetwork.calibration.energymeter.setting;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.bay.NewlandQRCodeScanner;
import com.tasnetwork.calibration.conveyor.bay.configloader.Bay;
import com.tasnetwork.calibration.conveyor.bay.configloader.ClusterDetail;
import com.tasnetwork.calibration.conveyor.bay.configloader.QrScanner;
import com.tasnetwork.calibration.conveyor.bay.configloader.Terminal;
import com.tasnetwork.calibration.conveyor.bay.configloader.TerminalBayConfigModel;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
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
import javafx.application.Application;
import javafx.application.Platform;
//import SerialPort.Communicator;
//import application.Communicator;
//import SerialPort.KeybindingController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

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
	private static Button ref_btn_Save;

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
	static private TextField ref_txtQr7ReadData;
	static private TextField ref_txtQr8ReadData;
	static private TextField ref_txtQr9ReadData;
	static private TextField ref_txtQr10ReadData;
	static private TextField ref_txtQr11ReadData;
	static private TextField ref_txtQr12ReadData;
	static private TextField ref_txtQr13ReadData;

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
	static private TextField ref_txtValidateQr7_CmdStatus;
	static private TextField ref_txtValidateQr8_CmdStatus;
	static private TextField ref_txtValidateQr9_CmdStatus;
	static private TextField ref_txtValidateQr10_CmdStatus;
	static private TextField ref_txtValidateQr11_CmdStatus;
	static private TextField ref_txtValidateQr12_CmdStatus;
	static private TextField ref_txtValidateQr13_CmdStatus;

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
	private static Button ref_btnValidateQr7_Cmd;
	private static Button ref_btnValidateQr8_Cmd;
	private static Button ref_btnValidateQr9_Cmd;
	private static Button ref_btnValidateQr10_Cmd;
	private static Button ref_btnValidateQr11_Cmd;
	private static Button ref_btnValidateQr12_Cmd;
	private static Button ref_btnValidateQr13_Cmd;

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

	private static HashMap FXML_PortMap = new HashMap();

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
		/*
		 * ArrayList<UacDataModel> uacSelectProfileScreenList =
		 * DeviceDataManagerController.getUacSelectProfileScreenList();
		 * String screenName = "";
		 * for (int i = 0; i < uacSelectProfileScreenList.size(); i++){
		 * 
		 * screenName = uacSelectProfileScreenList.get(i).getScreenName();
		 * switch (screenName) {
		 * case ConstantApp.UAC_DEVICE_SETTINGS_SCREEN:
		 * 
		 * 
		 * if(!uacSelectProfileScreenList.get(i).getExecutePossible()){
		 * //ref_btn_deploy.setDisable(true);
		 * //ref_btnValidatePwrSrcCmd.setDisable(true);
		 * //ref_btnValidateRefStdCmd.setDisable(true);
		 * ref_btnValidateLDU_Cmd1.setDisable(true);
		 * ref_btnValidateLDU_Cmd2.setDisable(true);
		 * ref_btnValidateLDU_Cmd3.setDisable(true);
		 * ref_btnValidateLDU_Cmd4.setDisable(true);
		 * ref_btnValidateLDU_Cmd5.setDisable(true);
		 * ref_btnValidateLDU_Cmd6.setDisable(true);
		 * ref_btnValidateLDU_Cmd7.setDisable(true);
		 * ref_btnValidateLDU_Cmd8.setDisable(true);
		 * ref_btnValidateLDU_Cmd9.setDisable(true);
		 * ref_btnValidateLDU_Cmd10.setDisable(true);
		 * ref_btnValidateLDU_Cmd11.setDisable(true);
		 * ref_btnValidateLDU_Cmd12.setDisable(true);
		 * ref_btnValidateLDU_Cmd13.setDisable(true);
		 * ref_btnValidateLDU_Cmd14.setDisable(true);
		 * ref_btnValidateLDU_Cmd15.setDisable(true);
		 * ref_btnValidateLDU_Cmd16.setDisable(true);
		 * ref_btnValidateLDU_Cmd17.setDisable(true);
		 * ref_btnValidateLDU_Cmd18.setDisable(true);
		 * ref_btnValidateLDU_Cmd19.setDisable(true);
		 * ref_btnValidateLDU_Cmd20.setDisable(true);
		 * ref_btnValidateLDU_Cmd21.setDisable(true);
		 * ref_btnValidateLDU_Cmd22.setDisable(true);
		 * ref_btnValidateLDU_Cmd23.setDisable(true);
		 * ref_btnValidateLDU_Cmd24.setDisable(true);
		 * ref_btnValidateLDU_Cmd25.setDisable(true);
		 * ref_btnValidateLDU_Cmd26.setDisable(true);
		 * ref_btnValidateLDU_Cmd27.setDisable(true);
		 * ref_btnValidateLDU_Cmd28.setDisable(true);
		 * ref_btnValidateLDU_Cmd29.setDisable(true);
		 * ref_btnValidateLDU_Cmd30.setDisable(true);
		 * ref_btnValidateLDU_Cmd31.setDisable(true);
		 * ref_btnValidateLDU_Cmd32.setDisable(true);
		 * ref_btnValidateLDU_Cmd33.setDisable(true);
		 * ref_btnValidateLDU_Cmd34.setDisable(true);
		 * ref_btnValidateLDU_Cmd35.setDisable(true);
		 * ref_btnValidateLDU_Cmd36.setDisable(true);
		 * ref_btnValidateLDU_Cmd37.setDisable(true);
		 * ref_btnValidateLDU_Cmd38.setDisable(true);
		 * ref_btnValidateLDU_Cmd39.setDisable(true);
		 * ref_btnValidateLDU_Cmd40.setDisable(true);
		 * ref_btnValidateLDU_Cmd41.setDisable(true);
		 * ref_btnValidateLDU_Cmd42.setDisable(true);
		 * ref_btnValidateLDU_Cmd43.setDisable(true);
		 * ref_btnValidateLDU_Cmd44.setDisable(true);
		 * ref_btnValidateLDU_Cmd45.setDisable(true);
		 * ref_btnValidateLDU_Cmd46.setDisable(true);
		 * ref_btnValidateLDU_Cmd47.setDisable(true);
		 * ref_btnValidateLDU_Cmd48.setDisable(true);
		 * 
		 * }
		 * 
		 * if(!uacSelectProfileScreenList.get(i).getAddPossible()){
		 * //ref_btn_Create.setDisable(true);
		 * 
		 * }
		 * 
		 * if(!uacSelectProfileScreenList.get(i).getUpdatePossible()){
		 * //ref_vbox_testscript.setDisable(true);sdvsc
		 * //setChildPropertySaveEnabled(false);
		 * ref_btn_Save.setDisable(true);
		 * 
		 * 
		 * }
		 * 
		 * if(!uacSelectProfileScreenList.get(i).getDeletePossible()){
		 * //ref_btn_Delete.setDisable(true);
		 * 
		 * }
		 * break;
		 * 
		 * 
		 * 
		 * default:
		 * break;
		 * }
		 * 
		 * 
		 * 
		 * }
		 */
	}

	public void disableGuiObjects() {

		/*
		 * for(int i = (ConstantConfig.TOTAL_NO_OF_SUPPORTED_RACK +1); i <=
		 * ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK_MAX_POSITION; i++){
		 * switch (i){
		 * 
		 * case 1:
		 * ref_btnValidateLDU_Cmd1.setDisable(true);
		 * break;
		 * case 2:
		 * ref_btnValidateLDU_Cmd2.setDisable(true);
		 * break;
		 * case 3:
		 * ref_btnValidateLDU_Cmd3.setDisable(true);
		 * break;
		 * case 4:
		 * ref_btnValidateLDU_Cmd4.setDisable(true);
		 * break;
		 * case 5:
		 * ref_btnValidateLDU_Cmd5.setDisable(true);
		 * break;
		 * case 6:
		 * ref_btnValidateLDU_Cmd6.setDisable(true);
		 * break;
		 * case 7:
		 * ref_btnValidateLDU_Cmd7.setDisable(true);
		 * break;
		 * case 8:
		 * ref_btnValidateLDU_Cmd8.setDisable(true);
		 * break;
		 * case 9:
		 * ref_btnValidateLDU_Cmd9.setDisable(true);
		 * break;
		 * case 10:
		 * ref_btnValidateLDU_Cmd10.setDisable(true);
		 * break;
		 * 
		 * case 11:
		 * ref_btnValidateLDU_Cmd11.setDisable(true);
		 * break;
		 * case 12:
		 * ref_btnValidateLDU_Cmd12.setDisable(true);
		 * break;
		 * case 13:
		 * ref_btnValidateLDU_Cmd13.setDisable(true);
		 * break;
		 * case 14:
		 * ref_btnValidateLDU_Cmd14.setDisable(true);
		 * break;
		 * case 15:
		 * ref_btnValidateLDU_Cmd15.setDisable(true);
		 * break;
		 * case 16:
		 * ref_btnValidateLDU_Cmd16.setDisable(true);
		 * break;
		 * case 17:
		 * ref_btnValidateLDU_Cmd17.setDisable(true);
		 * break;
		 * case 18:
		 * ref_btnValidateLDU_Cmd18.setDisable(true);
		 * break;
		 * case 19:
		 * ref_btnValidateLDU_Cmd19.setDisable(true);
		 * break;
		 * case 20:
		 * ref_btnValidateLDU_Cmd20.setDisable(true);
		 * break;
		 * 
		 * case 21:
		 * ref_btnValidateLDU_Cmd21.setDisable(true);
		 * break;
		 * case 22:
		 * ref_btnValidateLDU_Cmd22.setDisable(true);
		 * break;
		 * case 23:
		 * ref_btnValidateLDU_Cmd23.setDisable(true);
		 * break;
		 * case 24:
		 * ref_btnValidateLDU_Cmd24.setDisable(true);
		 * break;
		 * case 25:
		 * ref_btnValidateLDU_Cmd25.setDisable(true);
		 * break;
		 * case 26:
		 * ref_btnValidateLDU_Cmd26.setDisable(true);
		 * break;
		 * case 27:
		 * ref_btnValidateLDU_Cmd27.setDisable(true);
		 * break;
		 * case 28:
		 * ref_btnValidateLDU_Cmd28.setDisable(true);
		 * break;
		 * case 29:
		 * ref_btnValidateLDU_Cmd29.setDisable(true);
		 * break;
		 * case 30:
		 * ref_btnValidateLDU_Cmd30.setDisable(true);
		 * break;
		 * 
		 * case 31:
		 * ref_btnValidateLDU_Cmd31.setDisable(true);
		 * break;
		 * case 32:
		 * ref_btnValidateLDU_Cmd32.setDisable(true);
		 * break;
		 * case 33:
		 * ref_btnValidateLDU_Cmd33.setDisable(true);
		 * break;
		 * case 34:
		 * ref_btnValidateLDU_Cmd34.setDisable(true);
		 * break;
		 * case 35:
		 * ref_btnValidateLDU_Cmd35.setDisable(true);
		 * break;
		 * case 36:
		 * ref_btnValidateLDU_Cmd36.setDisable(true);
		 * break;
		 * case 37:
		 * ref_btnValidateLDU_Cmd37.setDisable(true);
		 * break;
		 * case 38:
		 * ref_btnValidateLDU_Cmd38.setDisable(true);
		 * break;
		 * case 39:
		 * ref_btnValidateLDU_Cmd39.setDisable(true);
		 * break;
		 * case 40:
		 * ref_btnValidateLDU_Cmd40.setDisable(true);
		 * break;
		 * 
		 * default:
		 * break;
		 * }
		 * 
		 * }
		 */

	}

	public void ref_assignment() {
		ref_btnValidateQr1_Cmd = btnValidateQr1_Cmd;
		ref_btnValidateQr2_Cmd = btnValidateQr2_Cmd;
		ref_btnValidateQr3_Cmd = btnValidateQr3_Cmd;
		ref_btnValidateQr4_Cmd = btnValidateQr4_Cmd;
		ref_btnValidateQr5_Cmd = btnValidateQr5_Cmd;
		ref_btnValidateQr6_Cmd = btnValidateQr6_Cmd;
		ref_btnValidateQr7_Cmd = btnValidateQr7_Cmd;
		ref_btnValidateQr8_Cmd = btnValidateQr8_Cmd;
		ref_btnValidateQr9_Cmd = btnValidateQr9_Cmd;
		ref_btnValidateQr10_Cmd = btnValidateQr10_Cmd;
		ref_btnValidateQr11_Cmd = btnValidateQr11_Cmd;
		ref_btnValidateQr12_Cmd = btnValidateQr12_Cmd;
		ref_btnValidateQr13_Cmd = btnValidateQr13_Cmd;

		ref_btn_Save = btn_Save;

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
		ref_txtQr7ReadData = txtQr7ReadData;
		ref_txtQr8ReadData = txtQr8ReadData;
		ref_txtQr9ReadData = txtQr9ReadData;
		ref_txtQr10ReadData = txtQr10ReadData;
		ref_txtQr11ReadData = txtQr11ReadData;
		ref_txtQr12ReadData = txtQr12ReadData;
		ref_txtQr13ReadData = txtQr13ReadData;

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
		ref_txtValidateQr7_CmdStatus = txtValidateQr7_CmdStatus;
		ref_txtValidateQr8_CmdStatus = txtValidateQr8_CmdStatus;
		ref_txtValidateQr9_CmdStatus = txtValidateQr9_CmdStatus;
		ref_txtValidateQr10_CmdStatus = txtValidateQr10_CmdStatus;
		ref_txtValidateQr11_CmdStatus = txtValidateQr11_CmdStatus;
		ref_txtValidateQr12_CmdStatus = txtValidateQr12_CmdStatus;
		ref_txtValidateQr13_CmdStatus = txtValidateQr13_CmdStatus;

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

		// ref_btnValidatePwrSrcCmd = btnValidatePwrSrcCmd;
		// ref_btnValidateRefStdCmd = btnValidateRefStdCmd;
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

		Enumeration ports = CommPortIdentifier.getPortIdentifiers();

		while (ports.hasMoreElements()) {
			CommPortIdentifier curPort = (CommPortIdentifier) ports.nextElement();

			if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				/*
				 * cmbBxPowerSrcPortSelection.getItems().add(curPort.getName());
				 * cmbBxRefStdPortSelection.getItems().add(curPort.getName());
				 */
				ref_cmbBxQr1_PortSelection.getItems().add(curPort.getName());
				ref_cmbBxQr2_PortSelection.getItems().add(curPort.getName());
				ref_cmbBxQr3_PortSelection.getItems().add(curPort.getName());
				ref_cmbBxQr4_PortSelection.getItems().add(curPort.getName());
				ref_cmbBxQr5_PortSelection.getItems().add(curPort.getName());
				ref_cmbBxQr6_PortSelection.getItems().add(curPort.getName());
				ref_cmbBxQr7_PortSelection.getItems().add(curPort.getName());
				ref_cmbBxQr8_PortSelection.getItems().add(curPort.getName());
				ref_cmbBxQr9_PortSelection.getItems().add(curPort.getName());
				ref_cmbBxQr10_PortSelection.getItems().add(curPort.getName());
				ref_cmbBxQr11_PortSelection.getItems().add(curPort.getName());
				ref_cmbBxQr12_PortSelection.getItems().add(curPort.getName());
				ref_cmbBxQr13_PortSelection.getItems().add(curPort.getName());

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
		/*
		 * try{
		 * cmbBxLDU_PortSelection14.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-14:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection15.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-15:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection16.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-16:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection17.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-17:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection18.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-18:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection19.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-19:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection20.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-20:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection21.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-21:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection22.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-22:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection23.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-23:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection24.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-24:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection25.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-25:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection26.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-26:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection27.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-27:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection28.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-28:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection29.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-29:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection30.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-30:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection31.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-31:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection32.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-32:"+e.getMessage());
		 * }
		 * 
		 * try{
		 * cmbBxLDU_PortSelection33.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-33:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection34.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-34:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection35.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-35:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection36.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-36:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection37.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-37:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection38.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-38:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection39.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-39:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection40.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-40:"+e.getMessage());
		 * }
		 * 
		 * if((ConstantConfig.TOTAL_NO_OF_SUPPORTED_RACK > 40) &&
		 * (ConstantConfig.TOTAL_NO_OF_SUPPORTED_RACK ==
		 * ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK_MAX_POSITION)){
		 * 
		 * try{
		 * cmbBxLDU_PortSelection41.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-41:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection42.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-42:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection43.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-43:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection44.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-44:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection45.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-45:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection46.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-46:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection47.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-47:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection48.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-48:"+e.getMessage());
		 * }
		 * }
		 */

	}

	public void SaveOnClick() {
		String pwr_type = ConstantApp.SOURCE_TYPE_POWER_SOURCE;
		/*
		 * String pwr_model_name =
		 * cmbBxPowerSource_ModelName.getSelectionModel().getSelectedItem();
		 * String pwr_port_name =
		 * cmbBxPowerSrcPortSelection.getSelectionModel().getSelectedItem();
		 * String pwr_baud_rate =
		 * cmbBxPowerSrcBaudRate.getSelectionModel().getSelectedItem().toString();
		 * String ref_type = ConstantApp.SOURCE_TYPE_REF_STD;
		 * String ref_model_name =
		 * cmbBxReferanceStd_ModelName.getSelectionModel().getSelectedItem();
		 * String ref_port_name =
		 * cmbBxRefStdPortSelection.getSelectionModel().getSelectedItem();
		 * String ref_baud_rate =
		 * cmbBxRefStdBaudRate.getSelectionModel().getSelectedItem().toString();
		 */
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
		/*
		 * String ldu3_type = ConstantApp.SOURCE_TYPE_LDU3;
		 * String ldu4_type = ConstantApp.SOURCE_TYPE_LDU4;
		 * String ldu5_type = ConstantApp.SOURCE_TYPE_LDU5;
		 * String ldu6_type = ConstantApp.SOURCE_TYPE_LDU6;
		 * String ldu7_type = ConstantApp.SOURCE_TYPE_LDU7;
		 * 
		 * String ldu8_type = ConstantApp.SOURCE_TYPE_LDU8;
		 * String ldu9_type = ConstantApp.SOURCE_TYPE_LDU9;
		 * String ldu10_type = ConstantApp.SOURCE_TYPE_LDU10;
		 * String ldu11_type = ConstantApp.SOURCE_TYPE_LDU11;
		 * String ldu12_type = ConstantApp.SOURCE_TYPE_LDU12;
		 * String ldu13_type = ConstantApp.SOURCE_TYPE_LDU13;
		 * String ldu14_type = ConstantApp.SOURCE_TYPE_LDU14;
		 * String ldu15_type = ConstantApp.SOURCE_TYPE_LDU15;
		 * String ldu16_type = ConstantApp.SOURCE_TYPE_LDU16;
		 * String ldu17_type = ConstantApp.SOURCE_TYPE_LDU17;
		 * String ldu18_type = ConstantApp.SOURCE_TYPE_LDU18;
		 * String ldu19_type = ConstantApp.SOURCE_TYPE_LDU19;
		 * String ldu20_type = ConstantApp.SOURCE_TYPE_LDU20;
		 * String ldu21_type = ConstantApp.SOURCE_TYPE_LDU21;
		 * String ldu22_type = ConstantApp.SOURCE_TYPE_LDU22;
		 * String ldu23_type = ConstantApp.SOURCE_TYPE_LDU23;
		 * String ldu24_type = ConstantApp.SOURCE_TYPE_LDU24;
		 * String ldu25_type = ConstantApp.SOURCE_TYPE_LDU25;
		 * String ldu26_type = ConstantApp.SOURCE_TYPE_LDU26;
		 * String ldu27_type = ConstantApp.SOURCE_TYPE_LDU27;
		 * String ldu28_type = ConstantApp.SOURCE_TYPE_LDU28;
		 * String ldu29_type = ConstantApp.SOURCE_TYPE_LDU29;
		 * String ldu30_type = ConstantApp.SOURCE_TYPE_LDU30;
		 * String ldu31_type = ConstantApp.SOURCE_TYPE_LDU31;
		 * String ldu32_type = ConstantApp.SOURCE_TYPE_LDU32;
		 * String ldu33_type = ConstantApp.SOURCE_TYPE_LDU33;
		 * String ldu34_type = ConstantApp.SOURCE_TYPE_LDU34;
		 * String ldu35_type = ConstantApp.SOURCE_TYPE_LDU35;
		 * String ldu36_type = ConstantApp.SOURCE_TYPE_LDU36;
		 * String ldu37_type = ConstantApp.SOURCE_TYPE_LDU37;
		 * String ldu38_type = ConstantApp.SOURCE_TYPE_LDU38;
		 * String ldu39_type = ConstantApp.SOURCE_TYPE_LDU39;
		 * String ldu40_type = ConstantApp.SOURCE_TYPE_LDU40;
		 * String ldu41_type = ConstantApp.SOURCE_TYPE_LDU41;
		 * String ldu42_type = ConstantApp.SOURCE_TYPE_LDU42;
		 * String ldu43_type = ConstantApp.SOURCE_TYPE_LDU43;
		 * String ldu44_type = ConstantApp.SOURCE_TYPE_LDU44;
		 * String ldu45_type = ConstantApp.SOURCE_TYPE_LDU45;
		 * String ldu46_type = ConstantApp.SOURCE_TYPE_LDU46;
		 * String ldu47_type = ConstantApp.SOURCE_TYPE_LDU47;
		 * String ldu48_type = ConstantApp.SOURCE_TYPE_LDU48;
		 */

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

		/*
		 * String ldu3_model_name =
		 * cmbBxLDU_ModelName3.getSelectionModel().getSelectedItem();
		 * String ldu4_model_name =
		 * cmbBxLDU_ModelName4.getSelectionModel().getSelectedItem();
		 * String ldu5_model_name =
		 * cmbBxLDU_ModelName5.getSelectionModel().getSelectedItem();
		 * String ldu6_model_name =
		 * cmbBxLDU_ModelName6.getSelectionModel().getSelectedItem();
		 * String ldu7_model_name =
		 * cmbBxLDU_ModelName7.getSelectionModel().getSelectedItem();
		 * 
		 * String ldu8_model_name =
		 * cmbBxLDU_ModelName8.getSelectionModel().getSelectedItem();
		 * String ldu9_model_name =
		 * cmbBxLDU_ModelName9.getSelectionModel().getSelectedItem();
		 * String ldu10_model_name =
		 * cmbBxLDU_ModelName10.getSelectionModel().getSelectedItem();
		 * String ldu11_model_name =
		 * cmbBxLDU_ModelName11.getSelectionModel().getSelectedItem();
		 * String ldu12_model_name =
		 * cmbBxLDU_ModelName12.getSelectionModel().getSelectedItem();
		 * String ldu13_model_name =
		 * cmbBxLDU_ModelName13.getSelectionModel().getSelectedItem();
		 * String ldu14_model_name =
		 * cmbBxLDU_ModelName14.getSelectionModel().getSelectedItem();
		 * String ldu15_model_name =
		 * cmbBxLDU_ModelName15.getSelectionModel().getSelectedItem();
		 * String ldu16_model_name =
		 * cmbBxLDU_ModelName16.getSelectionModel().getSelectedItem();
		 * String ldu17_model_name =
		 * cmbBxLDU_ModelName17.getSelectionModel().getSelectedItem();
		 * String ldu18_model_name =
		 * cmbBxLDU_ModelName18.getSelectionModel().getSelectedItem();
		 * String ldu19_model_name =
		 * cmbBxLDU_ModelName19.getSelectionModel().getSelectedItem();
		 * String ldu20_model_name =
		 * cmbBxLDU_ModelName20.getSelectionModel().getSelectedItem();
		 * String ldu21_model_name =
		 * cmbBxLDU_ModelName21.getSelectionModel().getSelectedItem();
		 * String ldu22_model_name =
		 * cmbBxLDU_ModelName22.getSelectionModel().getSelectedItem();
		 * String ldu23_model_name =
		 * cmbBxLDU_ModelName23.getSelectionModel().getSelectedItem();
		 * String ldu24_model_name =
		 * cmbBxLDU_ModelName24.getSelectionModel().getSelectedItem();
		 * String ldu25_model_name =
		 * cmbBxLDU_ModelName25.getSelectionModel().getSelectedItem();
		 * String ldu26_model_name =
		 * cmbBxLDU_ModelName26.getSelectionModel().getSelectedItem();
		 * String ldu27_model_name =
		 * cmbBxLDU_ModelName27.getSelectionModel().getSelectedItem();
		 * String ldu28_model_name =
		 * cmbBxLDU_ModelName28.getSelectionModel().getSelectedItem();
		 * String ldu29_model_name =
		 * cmbBxLDU_ModelName29.getSelectionModel().getSelectedItem();
		 * String ldu30_model_name =
		 * cmbBxLDU_ModelName30.getSelectionModel().getSelectedItem();
		 * String ldu31_model_name =
		 * cmbBxLDU_ModelName31.getSelectionModel().getSelectedItem();
		 * String ldu32_model_name =
		 * cmbBxLDU_ModelName32.getSelectionModel().getSelectedItem();
		 * String ldu33_model_name =
		 * cmbBxLDU_ModelName33.getSelectionModel().getSelectedItem();
		 * String ldu34_model_name =
		 * cmbBxLDU_ModelName34.getSelectionModel().getSelectedItem();
		 * String ldu35_model_name =
		 * cmbBxLDU_ModelName35.getSelectionModel().getSelectedItem();
		 * String ldu36_model_name =
		 * cmbBxLDU_ModelName36.getSelectionModel().getSelectedItem();
		 * String ldu37_model_name =
		 * cmbBxLDU_ModelName37.getSelectionModel().getSelectedItem();
		 * String ldu38_model_name =
		 * cmbBxLDU_ModelName38.getSelectionModel().getSelectedItem();
		 * String ldu39_model_name =
		 * cmbBxLDU_ModelName39.getSelectionModel().getSelectedItem();
		 * String ldu40_model_name =
		 * cmbBxLDU_ModelName40.getSelectionModel().getSelectedItem();
		 */

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
		/*
		 * String ldu_port_name3 =
		 * cmbBxLDU_PortSelection3.getSelectionModel().getSelectedItem();
		 * String ldu_port_name4 =
		 * cmbBxLDU_PortSelection4.getSelectionModel().getSelectedItem();
		 * String ldu_port_name5 =
		 * cmbBxLDU_PortSelection5.getSelectionModel().getSelectedItem();
		 * String ldu_port_name6 =
		 * cmbBxLDU_PortSelection6.getSelectionModel().getSelectedItem();
		 * String ldu_port_name7 =
		 * cmbBxLDU_PortSelection7.getSelectionModel().getSelectedItem();
		 * 
		 * String ldu_port_name8 =
		 * cmbBxLDU_PortSelection8.getSelectionModel().getSelectedItem();
		 * String ldu_port_name9 =
		 * cmbBxLDU_PortSelection9.getSelectionModel().getSelectedItem();
		 * String ldu_port_name10 =
		 * cmbBxLDU_PortSelection10.getSelectionModel().getSelectedItem();
		 * String ldu_port_name11 =
		 * cmbBxLDU_PortSelection11.getSelectionModel().getSelectedItem();
		 * String ldu_port_name12 =
		 * cmbBxLDU_PortSelection12.getSelectionModel().getSelectedItem();
		 * String ldu_port_name13 =
		 * cmbBxLDU_PortSelection13.getSelectionModel().getSelectedItem();
		 * String ldu_port_name14 =
		 * cmbBxLDU_PortSelection14.getSelectionModel().getSelectedItem();
		 * String ldu_port_name15 =
		 * cmbBxLDU_PortSelection15.getSelectionModel().getSelectedItem();
		 * String ldu_port_name16 =
		 * cmbBxLDU_PortSelection16.getSelectionModel().getSelectedItem();
		 * String ldu_port_name17 =
		 * cmbBxLDU_PortSelection17.getSelectionModel().getSelectedItem();
		 * String ldu_port_name18 =
		 * cmbBxLDU_PortSelection18.getSelectionModel().getSelectedItem();
		 * String ldu_port_name19 =
		 * cmbBxLDU_PortSelection19.getSelectionModel().getSelectedItem();
		 * String ldu_port_name20 =
		 * cmbBxLDU_PortSelection20.getSelectionModel().getSelectedItem();
		 * String ldu_port_name21 =
		 * cmbBxLDU_PortSelection21.getSelectionModel().getSelectedItem();
		 * String ldu_port_name22 =
		 * cmbBxLDU_PortSelection22.getSelectionModel().getSelectedItem();
		 * String ldu_port_name23 =
		 * cmbBxLDU_PortSelection23.getSelectionModel().getSelectedItem();
		 * String ldu_port_name24 =
		 * cmbBxLDU_PortSelection24.getSelectionModel().getSelectedItem();
		 * String ldu_port_name25 =
		 * cmbBxLDU_PortSelection25.getSelectionModel().getSelectedItem();
		 * String ldu_port_name26 =
		 * cmbBxLDU_PortSelection26.getSelectionModel().getSelectedItem();
		 * String ldu_port_name27 =
		 * cmbBxLDU_PortSelection27.getSelectionModel().getSelectedItem();
		 * String ldu_port_name28 =
		 * cmbBxLDU_PortSelection28.getSelectionModel().getSelectedItem();
		 * String ldu_port_name29 =
		 * cmbBxLDU_PortSelection29.getSelectionModel().getSelectedItem();
		 * String ldu_port_name30 =
		 * cmbBxLDU_PortSelection30.getSelectionModel().getSelectedItem();
		 * String ldu_port_name31 =
		 * cmbBxLDU_PortSelection31.getSelectionModel().getSelectedItem();
		 * String ldu_port_name32 =
		 * cmbBxLDU_PortSelection32.getSelectionModel().getSelectedItem();
		 * String ldu_port_name33 =
		 * cmbBxLDU_PortSelection33.getSelectionModel().getSelectedItem();
		 * String ldu_port_name34 =
		 * cmbBxLDU_PortSelection34.getSelectionModel().getSelectedItem();
		 * String ldu_port_name35 =
		 * cmbBxLDU_PortSelection35.getSelectionModel().getSelectedItem();
		 * String ldu_port_name36 =
		 * cmbBxLDU_PortSelection36.getSelectionModel().getSelectedItem();
		 * String ldu_port_name37 =
		 * cmbBxLDU_PortSelection37.getSelectionModel().getSelectedItem();
		 * String ldu_port_name38 =
		 * cmbBxLDU_PortSelection38.getSelectionModel().getSelectedItem();
		 * String ldu_port_name39 =
		 * cmbBxLDU_PortSelection39.getSelectionModel().getSelectedItem();
		 * String ldu_port_name40 =
		 * cmbBxLDU_PortSelection40.getSelectionModel().getSelectedItem();
		 */

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
		/*
		 * String ldu3_baud_rate =
		 * cmbBxLDU_BaudRate3.getSelectionModel().getSelectedItem().toString();
		 * String ldu4_baud_rate =
		 * cmbBxLDU_BaudRate4.getSelectionModel().getSelectedItem().toString();
		 * String ldu5_baud_rate =
		 * cmbBxLDU_BaudRate5.getSelectionModel().getSelectedItem().toString();
		 * String ldu6_baud_rate =
		 * cmbBxLDU_BaudRate6.getSelectionModel().getSelectedItem().toString();
		 * String ldu7_baud_rate =
		 * cmbBxLDU_BaudRate7.getSelectionModel().getSelectedItem().toString();
		 * 
		 * String ldu8_baud_rate =
		 * cmbBxLDU_BaudRate8.getSelectionModel().getSelectedItem().toString();
		 * String ldu9_baud_rate =
		 * cmbBxLDU_BaudRate9.getSelectionModel().getSelectedItem().toString();
		 * String ldu10_baud_rate =
		 * cmbBxLDU_BaudRate10.getSelectionModel().getSelectedItem().toString();
		 * String ldu11_baud_rate =
		 * cmbBxLDU_BaudRate11.getSelectionModel().getSelectedItem().toString();
		 * String ldu12_baud_rate =
		 * cmbBxLDU_BaudRate12.getSelectionModel().getSelectedItem().toString();
		 * String ldu13_baud_rate =
		 * cmbBxLDU_BaudRate13.getSelectionModel().getSelectedItem().toString();
		 * String ldu14_baud_rate =
		 * cmbBxLDU_BaudRate14.getSelectionModel().getSelectedItem().toString();
		 * String ldu15_baud_rate =
		 * cmbBxLDU_BaudRate15.getSelectionModel().getSelectedItem().toString();
		 * String ldu16_baud_rate =
		 * cmbBxLDU_BaudRate16.getSelectionModel().getSelectedItem().toString();
		 * String ldu17_baud_rate =
		 * cmbBxLDU_BaudRate17.getSelectionModel().getSelectedItem().toString();
		 * String ldu18_baud_rate =
		 * cmbBxLDU_BaudRate18.getSelectionModel().getSelectedItem().toString();
		 * String ldu19_baud_rate =
		 * cmbBxLDU_BaudRate19.getSelectionModel().getSelectedItem().toString();
		 * String ldu20_baud_rate =
		 * cmbBxLDU_BaudRate20.getSelectionModel().getSelectedItem().toString();
		 * String ldu21_baud_rate =
		 * cmbBxLDU_BaudRate21.getSelectionModel().getSelectedItem().toString();
		 * String ldu22_baud_rate =
		 * cmbBxLDU_BaudRate22.getSelectionModel().getSelectedItem().toString();
		 * String ldu23_baud_rate =
		 * cmbBxLDU_BaudRate23.getSelectionModel().getSelectedItem().toString();
		 * String ldu24_baud_rate =
		 * cmbBxLDU_BaudRate24.getSelectionModel().getSelectedItem().toString();
		 * String ldu25_baud_rate =
		 * cmbBxLDU_BaudRate25.getSelectionModel().getSelectedItem().toString();
		 * String ldu26_baud_rate =
		 * cmbBxLDU_BaudRate26.getSelectionModel().getSelectedItem().toString();
		 * String ldu27_baud_rate =
		 * cmbBxLDU_BaudRate27.getSelectionModel().getSelectedItem().toString();
		 * String ldu28_baud_rate =
		 * cmbBxLDU_BaudRate28.getSelectionModel().getSelectedItem().toString();
		 * String ldu29_baud_rate =
		 * cmbBxLDU_BaudRate29.getSelectionModel().getSelectedItem().toString();
		 * String ldu30_baud_rate =
		 * cmbBxLDU_BaudRate30.getSelectionModel().getSelectedItem().toString();
		 * String ldu31_baud_rate =
		 * cmbBxLDU_BaudRate31.getSelectionModel().getSelectedItem().toString();
		 * String ldu32_baud_rate =
		 * cmbBxLDU_BaudRate32.getSelectionModel().getSelectedItem().toString();
		 * String ldu33_baud_rate =
		 * cmbBxLDU_BaudRate33.getSelectionModel().getSelectedItem().toString();
		 * String ldu34_baud_rate =
		 * cmbBxLDU_BaudRate34.getSelectionModel().getSelectedItem().toString();
		 * String ldu35_baud_rate =
		 * cmbBxLDU_BaudRate35.getSelectionModel().getSelectedItem().toString();
		 * String ldu36_baud_rate =
		 * cmbBxLDU_BaudRate36.getSelectionModel().getSelectedItem().toString();
		 * String ldu37_baud_rate =
		 * cmbBxLDU_BaudRate37.getSelectionModel().getSelectedItem().toString();
		 * String ldu38_baud_rate =
		 * cmbBxLDU_BaudRate38.getSelectionModel().getSelectedItem().toString();
		 * String ldu39_baud_rate =
		 * cmbBxLDU_BaudRate39.getSelectionModel().getSelectedItem().toString();
		 * String ldu40_baud_rate =
		 * cmbBxLDU_BaudRate40.getSelectionModel().getSelectedItem().toString();
		 */

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
		// MySQL_Controller.sp_add_device_settings(1, pwr_type, pwr_model_name,
		// pwr_port_name, pwr_baud_rate);
		// MySQL_Controller.sp_add_device_settings(2, ref_type, ref_model_name,
		// ref_port_name, ref_baud_rate);

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

		// MySQL_Controller.sp_add_device_settings(3, ldu1_type, ldu1_model_name,
		// ldu_port_name1, ldu1_baud_rate,qrPortName1);
		// MySQL_Controller.sp_add_device_settings(4, ldu2_type, ldu2_model_name,
		// ldu_port_name2, ldu2_baud_rate,qrPortName2);

		/*
		 * MySQL_Controller.sp_add_device_settings(5, ldu3_type, ldu3_model_name,
		 * ldu_port_name3, ldu3_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(6, ldu4_type, ldu4_model_name,
		 * ldu_port_name4, ldu4_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(7, ldu5_type, ldu5_model_name,
		 * ldu_port_name5, ldu5_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(8, ldu6_type, ldu6_model_name,
		 * ldu_port_name6, ldu6_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(9, ldu7_type, ldu7_model_name,
		 * ldu_port_name7, ldu7_baud_rate);
		 * 
		 * MySQL_Controller.sp_add_device_settings(10, ldu8_type, ldu8_model_name,
		 * ldu_port_name8, ldu8_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(11, ldu9_type, ldu9_model_name,
		 * ldu_port_name9, ldu9_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(12, ldu10_type, ldu10_model_name,
		 * ldu_port_name10, ldu10_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(13, ldu11_type, ldu11_model_name,
		 * ldu_port_name11, ldu11_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(14, ldu12_type, ldu12_model_name,
		 * ldu_port_name12, ldu12_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(15, ldu13_type, ldu13_model_name,
		 * ldu_port_name13, ldu13_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(16, ldu14_type, ldu14_model_name,
		 * ldu_port_name14, ldu14_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(17, ldu15_type, ldu15_model_name,
		 * ldu_port_name15, ldu15_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(18, ldu16_type, ldu16_model_name,
		 * ldu_port_name16, ldu16_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(19, ldu17_type, ldu17_model_name,
		 * ldu_port_name17, ldu17_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(20, ldu18_type, ldu18_model_name,
		 * ldu_port_name18, ldu18_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(21, ldu19_type, ldu19_model_name,
		 * ldu_port_name19, ldu19_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(22, ldu20_type, ldu20_model_name,
		 * ldu_port_name20, ldu20_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(23, ldu21_type, ldu21_model_name,
		 * ldu_port_name21, ldu21_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(24, ldu22_type, ldu22_model_name,
		 * ldu_port_name22, ldu22_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(25, ldu23_type, ldu23_model_name,
		 * ldu_port_name23, ldu23_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(26, ldu24_type, ldu24_model_name,
		 * ldu_port_name24, ldu24_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(27, ldu25_type, ldu25_model_name,
		 * ldu_port_name25, ldu25_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(28, ldu26_type, ldu26_model_name,
		 * ldu_port_name26, ldu26_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(29, ldu27_type, ldu27_model_name,
		 * ldu_port_name27, ldu27_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(30, ldu28_type, ldu28_model_name,
		 * ldu_port_name28, ldu28_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(31, ldu29_type, ldu29_model_name,
		 * ldu_port_name29, ldu29_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(32, ldu30_type, ldu30_model_name,
		 * ldu_port_name30, ldu30_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(33, ldu31_type, ldu31_model_name,
		 * ldu_port_name31, ldu31_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(34, ldu32_type, ldu32_model_name,
		 * ldu_port_name32, ldu32_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(35, ldu33_type, ldu33_model_name,
		 * ldu_port_name33, ldu33_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(36, ldu34_type, ldu34_model_name,
		 * ldu_port_name34, ldu34_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(37, ldu35_type, ldu35_model_name,
		 * ldu_port_name35, ldu35_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(38, ldu36_type, ldu36_model_name,
		 * ldu_port_name36, ldu36_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(39, ldu37_type, ldu37_model_name,
		 * ldu_port_name37, ldu37_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(40, ldu38_type, ldu38_model_name,
		 * ldu_port_name38, ldu38_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(41, ldu39_type, ldu39_model_name,
		 * ldu_port_name39, ldu39_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(42, ldu40_type, ldu40_model_name,
		 * ldu_port_name40, ldu40_baud_rate);
		 */

		/*
		 * if((ConstantConfig.TOTAL_NO_OF_SUPPORTED_RACK > 40) &&
		 * (ConstantConfig.TOTAL_NO_OF_SUPPORTED_RACK ==
		 * ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK_MAX_POSITION)){
		 * 
		 * String ldu41_model_name =
		 * cmbBxLDU_ModelName41.getSelectionModel().getSelectedItem();
		 * String ldu42_model_name =
		 * cmbBxLDU_ModelName42.getSelectionModel().getSelectedItem();
		 * String ldu43_model_name =
		 * cmbBxLDU_ModelName43.getSelectionModel().getSelectedItem();
		 * String ldu44_model_name =
		 * cmbBxLDU_ModelName44.getSelectionModel().getSelectedItem();
		 * String ldu45_model_name =
		 * cmbBxLDU_ModelName45.getSelectionModel().getSelectedItem();
		 * String ldu46_model_name =
		 * cmbBxLDU_ModelName46.getSelectionModel().getSelectedItem();
		 * String ldu47_model_name =
		 * cmbBxLDU_ModelName47.getSelectionModel().getSelectedItem();
		 * String ldu48_model_name =
		 * cmbBxLDU_ModelName48.getSelectionModel().getSelectedItem();
		 * 
		 * String ldu_port_name41 =
		 * cmbBxLDU_PortSelection41.getSelectionModel().getSelectedItem();
		 * String ldu_port_name42 =
		 * cmbBxLDU_PortSelection42.getSelectionModel().getSelectedItem();
		 * String ldu_port_name43 =
		 * cmbBxLDU_PortSelection43.getSelectionModel().getSelectedItem();
		 * String ldu_port_name44 =
		 * cmbBxLDU_PortSelection44.getSelectionModel().getSelectedItem();
		 * String ldu_port_name45 =
		 * cmbBxLDU_PortSelection45.getSelectionModel().getSelectedItem();
		 * String ldu_port_name46 =
		 * cmbBxLDU_PortSelection46.getSelectionModel().getSelectedItem();
		 * String ldu_port_name47 =
		 * cmbBxLDU_PortSelection47.getSelectionModel().getSelectedItem();
		 * String ldu_port_name48 =
		 * cmbBxLDU_PortSelection48.getSelectionModel().getSelectedItem();
		 * 
		 * String ldu41_baud_rate =
		 * cmbBxLDU_BaudRate41.getSelectionModel().getSelectedItem().toString();
		 * String ldu42_baud_rate =
		 * cmbBxLDU_BaudRate42.getSelectionModel().getSelectedItem().toString();
		 * String ldu43_baud_rate =
		 * cmbBxLDU_BaudRate43.getSelectionModel().getSelectedItem().toString();
		 * String ldu44_baud_rate =
		 * cmbBxLDU_BaudRate44.getSelectionModel().getSelectedItem().toString();
		 * String ldu45_baud_rate =
		 * cmbBxLDU_BaudRate45.getSelectionModel().getSelectedItem().toString();
		 * String ldu46_baud_rate =
		 * cmbBxLDU_BaudRate46.getSelectionModel().getSelectedItem().toString();
		 * String ldu47_baud_rate =
		 * cmbBxLDU_BaudRate47.getSelectionModel().getSelectedItem().toString();
		 * String ldu48_baud_rate =
		 * cmbBxLDU_BaudRate48.getSelectionModel().getSelectedItem().toString();
		 * 
		 * MySQL_Controller.sp_add_device_settings(43, ldu41_type, ldu41_model_name,
		 * ldu_port_name41, ldu41_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(44, ldu42_type, ldu42_model_name,
		 * ldu_port_name42, ldu42_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(45, ldu43_type, ldu43_model_name,
		 * ldu_port_name43, ldu43_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(46, ldu44_type, ldu44_model_name,
		 * ldu_port_name44, ldu44_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(47, ldu45_type, ldu45_model_name,
		 * ldu_port_name45, ldu45_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(48, ldu46_type, ldu46_model_name,
		 * ldu_port_name46, ldu46_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(49, ldu47_type, ldu47_model_name,
		 * ldu_port_name47, ldu47_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(50, ldu48_type, ldu48_model_name,
		 * ldu_port_name48, ldu48_baud_rate);
		 * 
		 * }
		 */

		WindowManager.InformUser("Saved Successfully", "Saved data successfully", AlertType.INFORMATION);

	}

	/*
	 * public void PwrSrcValidateSerialCmd(){
	 * 
	 * String PowerSrcCommPortID= null;
	 * String PwrSrcCommBaudRate = null;
	 * txtValidatePwrSrcCmdStatus.clear();
	 * try{
	 * serialDM_Obj.commPowerSrc.searchForPorts();
	 * //PowerSrcCommPortID = getCurrentPwrSrcComPortID();
	 * //PwrSrcCommBaudRate = getCurrentPwrSrcComBaudRate();
	 * boolean status =
	 * serialDM_Obj.pwrSrc_CommInit(PowerSrcCommPortID,PwrSrcCommBaudRate);
	 * 
	 * if (!status){
	 * 
	 * txtValidatePwrSrcCmdStatus.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * status = serialDM_Obj.SetPowerSourceOff();
	 * if (!status){
	 * txtValidatePwrSrcCmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidatePwrSrcCmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * 
	 * }
	 * ApplicationLauncher.logger.info("PwrSrcValidateSerialCmd: testD:"
	 * +serialDM_Obj.commPowerSrc.getPortDeviceMapping());
	 * serialDM_Obj.DisconnectPwrSrc();
	 * 
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("PwrSrcValidateSerialCmd: Exception1:"+e.
	 * getMessage());
	 * //ApplicationLauncher.logger.info("PwrSrcValidateSerialCmd: Exception:"+e.
	 * toString());
	 * txtValidatePwrSrcCmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }
	 * }
	 */

	public void RefStdValidateSerialCmd() {

		String RefStdCommPortID = null;
		String RefStdCommBaudRate = null;
		/*
		 * txtValidateRefStdCmdStatus.clear();
		 * try{
		 * serialDM_Obj.commRefStandard.searchForPorts();
		 * RefStdCommPortID = getCurrentRefStdComPortID();
		 * RefStdCommBaudRate = getCurrentRefStdComBaudRate();
		 * boolean status =
		 * serialDM_Obj.RefStdComInit(RefStdCommPortID,RefStdCommBaudRate);
		 * if (!status){
		 * 
		 * txtValidateRefStdCmdStatus.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
		 * 
		 * } else {
		 * 
		 * status = serialDM_Obj.mteRefStd_ValidateVersionCMD();
		 * if (!status){
		 * txtValidateRefStdCmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
		 * }else{
		 * txtValidateRefStdCmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
		 * }
		 * 
		 * }
		 * serialDM_Obj.DisconnectRefStd();
		 * }catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.error("RefStdValidateSerialCmd: Exception"+e.
		 * getMessage());
		 * }
		 */

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

	/*
	 * public void LDU_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus1.clear();
	 * try{
	 * serialDM_Obj.commLDU1.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID1();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate1();
	 * boolean status = serialDM_Obj.LDU1_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus1.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-1.00");
	 * DisplayDataObj.set_Error_max("+1.00");
	 * DisplayDataObj.setNoOfPulses("10");
	 * 
	 * DisplayDataObj.setLDU1_ReadDataFlag(true);
	 * status = serialDM_Obj.LDU_ResetSetting();
	 * if (!status){
	 * txtValidateLDU_CmdStatus1.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus1.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU1_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU1();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 */

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

	/*
	 * public void lscsLDU2_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus2.clear();
	 * try{
	 * serialDM_Obj.commLDU2.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID2();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate2();
	 * boolean status = serialDM_Obj.LDU2_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus2.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * 
	 * status = serialDM_Obj.lscsLDU2_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus2.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus2.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU2_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU2();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU2_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * public void lscsLDU3_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus3.clear();
	 * try{
	 * serialDM_Obj.commLDU3.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID3();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate3();
	 * boolean status = serialDM_Obj.LDU3_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus3.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * 
	 * status = serialDM_Obj.lscsLDU3_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus3.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus3.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU3_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU3();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU3_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * public void lscsLDU4_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus4.clear();
	 * try{
	 * serialDM_Obj.commLDU4.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID4();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate4();
	 * boolean status = serialDM_Obj.LDU4_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus4.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * 
	 * status = serialDM_Obj.lscsLDU4_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus4.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus4.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU4_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU4();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU4_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * public void lscsLDU5_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus5.clear();
	 * try{
	 * serialDM_Obj.commLDU5.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID5();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate5();
	 * boolean status = serialDM_Obj.LDU5_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus5.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * 
	 * status = serialDM_Obj.lscsLDU5_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus5.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus5.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU5_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU5();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU5_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * public void lscsLDU6_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus6.clear();
	 * try{
	 * serialDM_Obj.commLDU6.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID6();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate6();
	 * boolean status = serialDM_Obj.LDU6_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus6.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-6.00");
	 * DisplayDataObj.set_Error_max("+6.00");
	 * DisplayDataObj.setNoOfPulses("60");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU6_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus6.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus6.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU6_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU6();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU6_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * public void lscsLDU7_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus7.clear();
	 * try{
	 * serialDM_Obj.commLDU7.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID7();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate7();
	 * boolean status = serialDM_Obj.LDU7_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus7.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-7.00");
	 * DisplayDataObj.set_Error_max("+7.00");
	 * DisplayDataObj.setNoOfPulses("70");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU7_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus7.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus7.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU7_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU7();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU7_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * public void lscsLDU8_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus8.clear();
	 * try{
	 * serialDM_Obj.commLDU8.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID8();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate8();
	 * boolean status = serialDM_Obj.LDU8_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus8.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-8.00");
	 * DisplayDataObj.set_Error_max("+8.00");
	 * DisplayDataObj.setNoOfPulses("80");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU8_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus8.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus8.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU8_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU8();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU8_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU9_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus9.clear();
	 * try{
	 * serialDM_Obj.commLDU9.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID9();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate9();
	 * boolean status = serialDM_Obj.LDU9_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus9.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-9.00");
	 * DisplayDataObj.set_Error_max("+9.00");
	 * DisplayDataObj.setNoOfPulses("90");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU9_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus9.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus9.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU9_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU9();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU9_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU10_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus10.clear();
	 * try{
	 * serialDM_Obj.commLDU10.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID10();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate10();
	 * boolean status = serialDM_Obj.LDU10_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus10.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-10.00");
	 * DisplayDataObj.set_Error_max("+10.00");
	 * DisplayDataObj.setNoOfPulses("100");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU10_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus10.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus10.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU10_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU10();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU10_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU11_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus11.clear();
	 * try{
	 * serialDM_Obj.commLDU11.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID11();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate11();
	 * boolean status = serialDM_Obj.LDU11_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus11.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-11.00");
	 * DisplayDataObj.set_Error_max("+11.00");
	 * DisplayDataObj.setNoOfPulses("110");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU11_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus11.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus11.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU11_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU11();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU11_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU12_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus12.clear();
	 * try{
	 * serialDM_Obj.commLDU12.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID12();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate12();
	 * boolean status = serialDM_Obj.LDU12_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus12.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-12.00");
	 * DisplayDataObj.set_Error_max("+12.00");
	 * DisplayDataObj.setNoOfPulses("120");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU12_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus12.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus12.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU12_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU12();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU12_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU13_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus13.clear();
	 * try{
	 * serialDM_Obj.commLDU13.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID13();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate13();
	 * boolean status = serialDM_Obj.LDU13_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus13.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-13.00");
	 * DisplayDataObj.set_Error_max("+13.00");
	 * DisplayDataObj.setNoOfPulses("130");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU13_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus13.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus13.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU13_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU13();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU13_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU14_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus14.clear();
	 * try{
	 * serialDM_Obj.commLDU14.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID14();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate14();
	 * boolean status = serialDM_Obj.LDU14_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus14.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-14.00");
	 * DisplayDataObj.set_Error_max("+14.00");
	 * DisplayDataObj.setNoOfPulses("140");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU14_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus14.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus14.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU14_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU14();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU14_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU15_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus15.clear();
	 * try{
	 * serialDM_Obj.commLDU15.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID15();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate15();
	 * boolean status = serialDM_Obj.LDU15_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus15.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-15.00");
	 * DisplayDataObj.set_Error_max("+15.00");
	 * DisplayDataObj.setNoOfPulses("150");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU15_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus15.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus15.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU15_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU15();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU15_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU16_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus16.clear();
	 * try{
	 * serialDM_Obj.commLDU16.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID16();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate16();
	 * boolean status = serialDM_Obj.LDU16_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus16.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-16.00");
	 * DisplayDataObj.set_Error_max("+16.00");
	 * DisplayDataObj.setNoOfPulses("160");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU16_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus16.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus16.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU16_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU16();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU16_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU17_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus17.clear();
	 * try{
	 * serialDM_Obj.commLDU17.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID17();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate17();
	 * boolean status = serialDM_Obj.LDU17_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus17.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-17.00");
	 * DisplayDataObj.set_Error_max("+17.00");
	 * DisplayDataObj.setNoOfPulses("170");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU17_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus17.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus17.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU17_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU17();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU17_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU18_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus18.clear();
	 * try{
	 * serialDM_Obj.commLDU18.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID18();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate18();
	 * boolean status = serialDM_Obj.LDU18_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus18.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-18.00");
	 * DisplayDataObj.set_Error_max("+18.00");
	 * DisplayDataObj.setNoOfPulses("180");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU18_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus18.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus18.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU18_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU18();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU18_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU19_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus19.clear();
	 * try{
	 * serialDM_Obj.commLDU19.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID19();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate19();
	 * boolean status = serialDM_Obj.LDU19_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus19.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-19.00");
	 * DisplayDataObj.set_Error_max("+19.00");
	 * DisplayDataObj.setNoOfPulses("190");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU19_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus19.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus19.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU19_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU19();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU19_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU20_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus20.clear();
	 * try{
	 * serialDM_Obj.commLDU20.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID20();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate20();
	 * boolean status = serialDM_Obj.LDU20_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus20.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-20.00");
	 * DisplayDataObj.set_Error_max("+20.00");
	 * DisplayDataObj.setNoOfPulses("200");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU20_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus20.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus20.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU20_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU20();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU20_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * }
	 */

	/*
	 * private String getCurrentPwrSrcComBaudRate() {
	 * 
	 * return
	 * cmbBxPowerSrcBaudRate.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentPwrSrcComPortID() {
	 * 
	 * 
	 * return cmbBxPowerSrcPortSelection.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentRefStdComBaudRate() {
	 * 
	 * return cmbBxRefStdBaudRate.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentRefStdComPortID() {
	 * 
	 * 
	 * return cmbBxRefStdPortSelection.getSelectionModel().getSelectedItem();
	 * }
	 */

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

	private String getCurrentLDU_ComBaudRate7() {

		return ref_cmbBxQr7_BaudRate.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentLDU_ComPortID7() {

		return ref_cmbBxQr7_PortSelection.getSelectionModel().getSelectedItem();
	}

	private String getCurrentLDU_ComBaudRate8() {

		return ref_cmbBxQr8_BaudRate.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentLDU_ComPortID8() {

		return ref_cmbBxQr8_PortSelection.getSelectionModel().getSelectedItem();
	}

	private String getCurrentLDU_ComBaudRate9() {

		return ref_cmbBxQr9_BaudRate.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentLDU_ComPortID9() {

		return ref_cmbBxQr9_PortSelection.getSelectionModel().getSelectedItem();
	}

	private String getCurrentLDU_ComBaudRate10() {

		return ref_cmbBxQr10_BaudRate.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentLDU_ComPortID10() {

		return ref_cmbBxQr10_PortSelection.getSelectionModel().getSelectedItem();
	}

	private String getCurrentLDU_ComBaudRate11() {

		return ref_cmbBxQr11_BaudRate.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentLDU_ComPortID11() {

		return ref_cmbBxQr11_PortSelection.getSelectionModel().getSelectedItem();
	}

	private String getCurrentLDU_ComBaudRate12() {

		return ref_cmbBxQr12_BaudRate.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentLDU_ComPortID12() {

		return ref_cmbBxQr12_PortSelection.getSelectionModel().getSelectedItem();
	}

	private String getCurrentLDU_ComBaudRate13() {

		return ref_cmbBxQr13_BaudRate.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentLDU_ComPortID13() {

		return ref_cmbBxQr13_PortSelection.getSelectionModel().getSelectedItem();
	}

	/*
	 * private String getCurrentLDU_ComBaudRate14() {
	 * 
	 * return cmbBxLDU_BaudRate14.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID14() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection14.getSelectionModel().getSelectedItem();
	 * }
	 * private String getCurrentLDU_ComBaudRate15() {
	 * 
	 * return cmbBxLDU_BaudRate15.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID15() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection15.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * 
	 * private String getCurrentLDU_ComBaudRate16() {
	 * 
	 * return cmbBxLDU_BaudRate16.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID16() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection16.getSelectionModel().getSelectedItem();
	 * }
	 * private String getCurrentLDU_ComBaudRate17() {
	 * 
	 * return cmbBxLDU_BaudRate17.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID17() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection17.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate18() {
	 * 
	 * return cmbBxLDU_BaudRate18.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID18() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection18.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate19() {
	 * 
	 * return cmbBxLDU_BaudRate19.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID19() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection19.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * 
	 * private String getCurrentLDU_ComBaudRate20() {
	 * 
	 * return cmbBxLDU_BaudRate20.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID20() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection20.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate21() {
	 * 
	 * return cmbBxLDU_BaudRate21.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID21() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection21.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * 
	 * private String getCurrentLDU_ComBaudRate22() {
	 * 
	 * return cmbBxLDU_BaudRate22.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID22() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection22.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate23() {
	 * 
	 * return cmbBxLDU_BaudRate23.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID23() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection23.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * 
	 * private String getCurrentLDU_ComBaudRate24() {
	 * 
	 * return cmbBxLDU_BaudRate24.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID24() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection24.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate25() {
	 * 
	 * return cmbBxLDU_BaudRate25.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID25() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection25.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate26() {
	 * 
	 * return cmbBxLDU_BaudRate26.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID26() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection26.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate27() {
	 * 
	 * return cmbBxLDU_BaudRate27.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID27() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection27.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate28() {
	 * 
	 * return cmbBxLDU_BaudRate28.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID28() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection28.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate29() {
	 * 
	 * return cmbBxLDU_BaudRate29.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID29() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection29.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate30() {
	 * 
	 * return cmbBxLDU_BaudRate30.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID30() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection30.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * 
	 * private String getCurrentLDU_ComBaudRate31() {
	 * 
	 * return cmbBxLDU_BaudRate31.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID31() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection31.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * 
	 * private String getCurrentLDU_ComBaudRate32() {
	 * 
	 * return cmbBxLDU_BaudRate32.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID32() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection32.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate33() {
	 * 
	 * return cmbBxLDU_BaudRate33.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID33() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection33.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * 
	 * private String getCurrentLDU_ComBaudRate34() {
	 * 
	 * return cmbBxLDU_BaudRate34.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID34() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection34.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate35() {
	 * 
	 * return cmbBxLDU_BaudRate35.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID35() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection35.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate36() {
	 * 
	 * return cmbBxLDU_BaudRate36.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID36() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection36.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate37() {
	 * 
	 * return cmbBxLDU_BaudRate37.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID37() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection37.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate38() {
	 * 
	 * return cmbBxLDU_BaudRate38.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID38() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection38.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate39() {
	 * 
	 * return cmbBxLDU_BaudRate39.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID39() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection39.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate40() {
	 * 
	 * return cmbBxLDU_BaudRate40.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID40() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection40.getSelectionModel().getSelectedItem();
	 * }
	 * private String getCurrentLDU_ComBaudRate41() {
	 * 
	 * return cmbBxLDU_BaudRate41.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID41() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection41.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate42() {
	 * 
	 * return cmbBxLDU_BaudRate42.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID42() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection42.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate43() {
	 * 
	 * return cmbBxLDU_BaudRate43.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID43() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection43.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate44() {
	 * 
	 * return cmbBxLDU_BaudRate44.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID44() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection44.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * 
	 * private String getCurrentLDU_ComBaudRate45() {
	 * 
	 * return cmbBxLDU_BaudRate45.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID45() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection45.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate46() {
	 * 
	 * return cmbBxLDU_BaudRate46.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID46() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection46.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate47() {
	 * 
	 * return cmbBxLDU_BaudRate47.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID47() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection47.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * 
	 * private String getCurrentLDU_ComBaudRate48() {
	 * 
	 * return cmbBxLDU_BaudRate48.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID48() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection48.getSelectionModel().getSelectedItem();
	 * }
	 */
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

	/*
	 * class LDU3_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd3.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU3_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU3_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU3_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU3_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU3_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd3.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU4_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd4.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU4_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU4_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU4_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU4_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU4_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd4.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU5_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd5.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU5_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU5_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU5_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU5_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU5_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd5.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU6_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd6.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU6_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU6_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU6_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU6_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU6_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd6.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU7_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd7.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU7_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU7_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU7_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU7_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU7_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd7.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU8_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd8.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU8_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU8_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU8_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU8_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU8_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd8.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU9_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd9.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU9_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU9_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU9_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU9_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU9_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd9.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU10_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd10.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU10_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU10_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU10_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU10_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU10_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd10.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU11_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd11.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU11_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU11_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU11_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU11_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU11_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd11.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU12_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd12.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU12_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU12_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU12_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU12_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU12_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd12.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU13_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd13.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU13_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU13_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU13_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU13_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU13_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd13.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU14_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd14.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU14_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU14_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU14_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU14_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU14_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd14.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU15_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd15.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU15_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU15_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU15_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU15_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU15_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd15.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU16_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd16.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU16_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU16_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU16_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU16_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU16_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd16.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU17_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd17.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU17_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU17_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU17_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU17_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU17_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd17.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU18_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd18.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU18_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU18_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU18_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU18_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU18_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd18.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU19_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd19.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU19_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU19_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU19_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU19_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU19_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd19.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU20_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd20.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU20_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU20_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU20_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU20_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU20_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd20.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU21_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd21.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU21_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU21_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU21_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU21_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU21_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd21.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU22_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd22.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU22_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU22_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU22_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU22_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU22_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd22.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU23_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd23.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU23_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU23_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU23_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU23_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU23_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd23.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU24_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd24.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU24_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU24_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU24_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU24_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU24_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd24.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU25_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd25.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU25_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU25_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU25_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU25_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU25_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd25.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU26_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd26.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU26_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU26_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU26_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU26_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU26_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd26.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU27_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd27.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU27_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU27_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU27_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU27_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU27_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd27.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU28_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd28.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU28_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU28_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU28_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU28_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU28_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd28.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU29_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd29.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU29_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU29_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU29_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU29_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU29_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd29.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU30_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd30.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU30_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU30_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU30_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU30_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU30_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd30.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU31_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd31.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU31_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU31_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU31_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU31_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU31_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd31.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU32_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd32.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU32_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU32_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU32_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU32_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU32_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd32.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU33_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd33.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU33_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU33_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU33_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU33_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU33_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd33.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU34_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd34.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU34_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU34_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU34_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU34_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU34_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd34.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU35_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd35.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU35_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU35_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU35_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU35_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU35_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd35.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU36_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd36.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU36_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU36_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU36_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU36_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU36_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd36.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU37_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd37.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU37_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU37_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU37_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU37_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU37_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd37.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU38_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd38.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU38_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU38_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU38_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU38_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU38_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd38.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU39_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd39.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU39_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU39_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU39_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU39_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU39_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd39.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU40_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd40.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU40_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU40_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU40_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU40_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU40_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd40.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU41_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd41.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU41_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU41_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU41_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU41_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU41_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd41.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU42_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd42.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU42_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU42_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU42_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU42_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU42_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd42.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU43_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd43.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU43_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU43_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU43_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU43_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU43_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd43.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU44_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd44.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU44_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU44_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU44_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU44_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU44_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd44.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU45_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd45.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU45_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU45_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU45_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU45_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU45_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd45.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU46_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd46.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU46_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU46_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU46_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU46_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU46_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd46.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU47_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd47.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU47_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU47_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU47_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU47_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU47_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd47.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU48_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd48.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU48_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU48_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU48_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU48_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU48_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd48.setDisable(false);
	 * }
	 * }
	 */

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
