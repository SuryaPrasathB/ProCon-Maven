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

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.bay.configloader.Bay;
import com.tasnetwork.calibration.conveyor.bay.configloader.ClusterDetail;
import com.tasnetwork.calibration.conveyor.bay.configloader.DutDevice;
import com.tasnetwork.calibration.conveyor.bay.configloader.Terminal;
import com.tasnetwork.calibration.conveyor.bay.configloader.TerminalBayConfigModel;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantDutDevSys;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.database.MySQL_Controller;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.serial.director.DutDirector;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmDut;
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

public class DutPortSetupController implements Initializable {

	Timer dut1ClusterSelectionOnChangeTimer;
	Timer dut2ClusterSelectionOnChangeTimer;
	Timer dut3ClusterSelectionOnChangeTimer;
	Timer dut4ClusterSelectionOnChangeTimer;
	Timer dut5ClusterSelectionOnChangeTimer;
	Timer dut6ClusterSelectionOnChangeTimer;

	Timer dut1BaySelectionOnChangeTimer;
	Timer dut2BaySelectionOnChangeTimer;
	Timer dut3BaySelectionOnChangeTimer;
	Timer dut4BaySelectionOnChangeTimer;
	Timer dut5BaySelectionOnChangeTimer;
	Timer dut6BaySelectionOnChangeTimer;

	Timer dut1PositionIdOnChangeTimer;
	Timer dut2PositionIdOnChangeTimer;
	Timer dut3PositionIdOnChangeTimer;
	Timer dut4PositionIdOnChangeTimer;
	Timer dut5PositionIdOnChangeTimer;
	Timer dut6PositionIdOnChangeTimer;

	private Map<String, ArrayList<String>> clusterBayNameListMap = new HashMap<String, ArrayList<String>>();
	private Map<String, String> clusterNameIdListMap = new HashMap<String, String>();
	private Map<String, String> clusterBayNameIdMap = new HashMap<String, String>();
	private Map<String, ArrayList<String>> clusterBayNamePositionListMap = new HashMap<String, ArrayList<String>>();
	private Map<String, String> clusterBayPositionNoCnameMap = new LinkedHashMap<String, String>();
	private Map<String, String> clusterBayPositionNoDeviceIdMap = new LinkedHashMap<String, String>();

	private static TerminalBayConfigModel bayConfigModel = ConveyorDataManager.getTerminalBayConfig();

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
	private ComboBox<String> cmbBxDut1ClusterId;
	@FXML
	private ComboBox<String> cmbBxDut2ClusterId;
	@FXML
	private ComboBox<String> cmbBxDut3ClusterId;
	@FXML
	private ComboBox<String> cmbBxDut4ClusterId;
	@FXML
	private ComboBox<String> cmbBxDut5ClusterId;
	@FXML
	private ComboBox<String> cmbBxDut6ClusterId;

	@FXML
	private ComboBox<String> cmbBxDut1BayId;
	@FXML
	private ComboBox<String> cmbBxDut2BayId;
	@FXML
	private ComboBox<String> cmbBxDut3BayId;
	@FXML
	private ComboBox<String> cmbBxDut4BayId;
	@FXML
	private ComboBox<String> cmbBxDut5BayId;
	@FXML
	private ComboBox<String> cmbBxDut6BayId;

	@FXML
	private ComboBox<String> cmbBxDut1PositionId;
	@FXML
	private ComboBox<String> cmbBxDut2PositionId;
	@FXML
	private ComboBox<String> cmbBxDut3PositionId;
	@FXML
	private ComboBox<String> cmbBxDut4PositionId;
	@FXML
	private ComboBox<String> cmbBxDut5PositionId;
	@FXML
	private ComboBox<String> cmbBxDut6PositionId;

	private static ComboBox<String> ref_cmbBxDut1ClusterId;
	private static ComboBox<String> ref_cmbBxDut2ClusterId;
	private static ComboBox<String> ref_cmbBxDut3ClusterId;
	private static ComboBox<String> ref_cmbBxDut4ClusterId;
	private static ComboBox<String> ref_cmbBxDut5ClusterId;
	private static ComboBox<String> ref_cmbBxDut6ClusterId;

	private static ComboBox<String> ref_cmbBxDut1BayId;
	private static ComboBox<String> ref_cmbBxDut2BayId;
	private static ComboBox<String> ref_cmbBxDut3BayId;
	private static ComboBox<String> ref_cmbBxDut4BayId;
	private static ComboBox<String> ref_cmbBxDut5BayId;
	private static ComboBox<String> ref_cmbBxDut6BayId;

	private static ComboBox<String> ref_cmbBxDut1PositionId;
	private static ComboBox<String> ref_cmbBxDut2PositionId;
	private static ComboBox<String> ref_cmbBxDut3PositionId;
	private static ComboBox<String> ref_cmbBxDut4PositionId;
	private static ComboBox<String> ref_cmbBxDut5PositionId;
	private static ComboBox<String> ref_cmbBxDut6PositionId;

	@FXML
	private TextField txtDut1Cname;
	@FXML
	private TextField txtDut2Cname;
	@FXML
	private TextField txtDut3Cname;
	@FXML
	private TextField txtDut4Cname;
	@FXML
	private TextField txtDut5Cname;
	@FXML
	private TextField txtDut6Cname;

	private static TextField ref_txtDut1Cname;
	private static TextField ref_txtDut2Cname;
	private static TextField ref_txtDut3Cname;
	private static TextField ref_txtDut4Cname;
	private static TextField ref_txtDut5Cname;
	private static TextField ref_txtDut6Cname;

	// @FXML private ComboBox<String> cmbBxDutCname2;

	// @FXML private ComboBox<String> cmbBxDutCname1;
	// @FXML private ComboBox<String> cmbBxDutCname2;

	@FXML
	private ComboBox<String> cmbBxQrScannerName3;
	@FXML
	private ComboBox<String> cmbBxQrScannerName4;
	@FXML
	private ComboBox<String> cmbBxQrScannerName5;
	@FXML
	private ComboBox<String> cmbBxQrScannerName6;
	@FXML
	private ComboBox<String> cmbBxQrScannerName7;
	@FXML
	private ComboBox<String> cmbBxQrScannerName8;
	@FXML
	private ComboBox<String> cmbBxQrScannerName9;
	@FXML
	private ComboBox<String> cmbBxQrScannerName10;
	@FXML
	private ComboBox<String> cmbBxQrScannerName11;
	@FXML
	private ComboBox<String> cmbBxQrScannerName12;
	@FXML
	private ComboBox<String> cmbBxQrScannerName13;
	@FXML
	private ComboBox<String> cmbBxQrScannerName14;
	@FXML
	private ComboBox<String> cmbBxQrScannerName15;
	@FXML
	private ComboBox<String> cmbBxQrScannerName16;

	// private static ComboBox<String> ref_cmbBxDutCname1;

	// private static ComboBox<String> ref_cmbBxDutCname2;

	private static ComboBox<String> ref_cmbBxQrScannerName3;
	private static ComboBox<String> ref_cmbBxQrScannerName4;
	private static ComboBox<String> ref_cmbBxQrScannerName5;
	private static ComboBox<String> ref_cmbBxQrScannerName6;
	private static ComboBox<String> ref_cmbBxQrScannerName7;
	private static ComboBox<String> ref_cmbBxQrScannerName8;
	private static ComboBox<String> ref_cmbBxQrScannerName9;
	private static ComboBox<String> ref_cmbBxQrScannerName10;
	private static ComboBox<String> ref_cmbBxQrScannerName11;
	private static ComboBox<String> ref_cmbBxQrScannerName12;
	private static ComboBox<String> ref_cmbBxQrScannerName13;
	private static ComboBox<String> ref_cmbBxQrScannerName14;
	private static ComboBox<String> ref_cmbBxQrScannerName15;
	private static ComboBox<String> ref_cmbBxQrScannerName16;

	@FXML
	private ComboBox<Integer> cmbBxDut1_BaudRate;
	@FXML
	private ComboBox<Integer> cmbBxDut2_BaudRate;
	@FXML
	private ComboBox<Integer> cmbBxDut3_BaudRate;
	@FXML
	private ComboBox<Integer> cmbBxDut4_BaudRate;
	@FXML
	private ComboBox<Integer> cmbBxDut5_BaudRate;
	@FXML
	private ComboBox<Integer> cmbBxDut6_BaudRate;

	static private ComboBox<Integer> ref_cmbBxDut1_BaudRate;
	static private ComboBox<Integer> ref_cmbBxDut2_BaudRate;
	static private ComboBox<Integer> ref_cmbBxDut3_BaudRate;
	static private ComboBox<Integer> ref_cmbBxDut4_BaudRate;
	static private ComboBox<Integer> ref_cmbBxDut5_BaudRate;
	static private ComboBox<Integer> ref_cmbBxDut6_BaudRate;

	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate7;

	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate8;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate9;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate10;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate11;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate12;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate13;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate14;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate15;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate16;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate17;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate18;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate19;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate20;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate21;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate22;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate23;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate24;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate25;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate26;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate27;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate28;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate29;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate30;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate31;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate32;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate33;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate34;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate35;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate36;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate37;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate38;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate39;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate40;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate41;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate42;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate43;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate44;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate45;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate46;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate47;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate48;

	@FXML
	private ComboBox<String> cmbBxPowerSrcPortSelection;
	@FXML
	private ComboBox<String> cmbBxRefStdPortSelection;

	@FXML
	private ComboBox<String> cmbBxDut1_PortSelection;
	@FXML
	private ComboBox<String> cmbBxDut2_PortSelection;
	@FXML
	private ComboBox<String> cmbBxDut3_PortSelection;
	@FXML
	private ComboBox<String> cmbBxDut4_PortSelection;
	@FXML
	private ComboBox<String> cmbBxDut5_PortSelection;
	@FXML
	private ComboBox<String> cmbBxDut6_PortSelection;

	static private ComboBox<String> ref_cmbBxDut1_PortSelection;
	static private ComboBox<String> ref_cmbBxDut2_PortSelection;
	static private ComboBox<String> ref_cmbBxDut3_PortSelection;
	static private ComboBox<String> ref_cmbBxDut4_PortSelection;
	static private ComboBox<String> ref_cmbBxDut5_PortSelection;
	static private ComboBox<String> ref_cmbBxDut6_PortSelection;

	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection7;

	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection8;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection9;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection10;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection11;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection12;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection13;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection14;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection15;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection16;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection17;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection18;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection19;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection20;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection21;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection22;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection23;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection24;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection25;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection26;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection27;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection28;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection29;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection30;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection31;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection32;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection33;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection34;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection35;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection36;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection37;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection38;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection39;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection40;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection41;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection42;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection43;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection44;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection45;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection46;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection47;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection48;

	/*
	 * @FXML
	 * private ComboBox<String> cmbBxPowerSource_ModelName;
	 * 
	 * @FXML
	 * private ComboBox<String> cmbBxReferanceStd_ModelName;
	 */

	@FXML
	private ComboBox<String> cmbBxDut1_ModelName;
	@FXML
	private ComboBox<String> cmbBxDut2_ModelName;
	@FXML
	private ComboBox<String> cmbBxDut3_ModelName;
	@FXML
	private ComboBox<String> cmbBxDut4_ModelName;
	@FXML
	private ComboBox<String> cmbBxDut5_ModelName;
	@FXML
	private ComboBox<String> cmbBxDut6_ModelName;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName7;

	static private ComboBox<String> ref_cmbBxDut1_ModelName;
	static private ComboBox<String> ref_cmbBxDut2_ModelName;
	static private ComboBox<String> ref_cmbBxDut3_ModelName;
	static private ComboBox<String> ref_cmbBxDut4_ModelName;
	static private ComboBox<String> ref_cmbBxDut5_ModelName;
	static private ComboBox<String> ref_cmbBxDut6_ModelName;

	@FXML
	private ComboBox<String> cmbBxLDU_ModelName8;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName9;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName10;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName11;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName12;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName13;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName14;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName15;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName16;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName17;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName18;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName19;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName20;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName21;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName22;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName23;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName24;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName25;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName26;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName27;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName28;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName29;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName30;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName31;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName32;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName33;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName34;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName35;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName36;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName37;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName38;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName39;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName40;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName41;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName42;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName43;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName44;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName45;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName46;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName47;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName48;

	@FXML
	private TextField txtValidatePwrSrcCmdStatus;
	@FXML
	private TextField txtValidateRefStdCmdStatus;

	@FXML
	private TextField txtDut1ReadData;
	@FXML
	private TextField txtDut2ReadData;
	@FXML
	private TextField txtDut3ReadData;
	@FXML
	private TextField txtDut4ReadData;
	@FXML
	private TextField txtDut5ReadData;
	@FXML
	private TextField txtDut6ReadData;

	static private TextField ref_txtDut1ReadData;
	static private TextField ref_txtDut2ReadData;
	static private TextField ref_txtDut3ReadData;
	static private TextField ref_txtDut4ReadData;
	static private TextField ref_txtDut5ReadData;
	static private TextField ref_txtDut6ReadData;

	@FXML
	private TextField txtQrScannerReadData3;
	@FXML
	private TextField txtQrScannerReadData4;
	@FXML
	private TextField txtQrScannerReadData5;
	@FXML
	private TextField txtQrScannerReadData6;
	@FXML
	private TextField txtQrScannerReadData7;
	@FXML
	private TextField txtQrScannerReadData8;
	@FXML
	private TextField txtQrScannerReadData9;
	@FXML
	private TextField txtQrScannerReadData10;
	@FXML
	private TextField txtQrScannerReadData11;
	@FXML
	private TextField txtQrScannerReadData12;
	@FXML
	private TextField txtQrScannerReadData13;
	@FXML
	private TextField txtQrScannerReadData14;
	@FXML
	private TextField txtQrScannerReadData15;
	@FXML
	private TextField txtQrScannerReadData16;

	@FXML
	private TextField txtValidateDut1_CmdStatus;
	@FXML
	private TextField txtValidateDut2_CmdStatus;
	@FXML
	private TextField txtValidateDut3_CmdStatus;
	@FXML
	private TextField txtValidateDut4_CmdStatus;
	@FXML
	private TextField txtValidateDut5_CmdStatus;
	@FXML
	private TextField txtValidateDut6_CmdStatus;

	static private TextField ref_txtValidateDut1_CmdStatus;
	static private TextField ref_txtValidateDut2_CmdStatus;
	static private TextField ref_txtValidateDut3_CmdStatus;
	static private TextField ref_txtValidateDut4_CmdStatus;
	static private TextField ref_txtValidateDut5_CmdStatus;
	static private TextField ref_txtValidateDut6_CmdStatus;

	@FXML
	private TextField txtValidateLDU_CmdStatus3;
	@FXML
	private TextField txtValidateLDU_CmdStatus4;
	@FXML
	private TextField txtValidateLDU_CmdStatus5;
	@FXML
	private TextField txtValidateLDU_CmdStatus6;
	@FXML
	private TextField txtValidateLDU_CmdStatus7;

	@FXML
	private TextField txtValidateLDU_CmdStatus8;
	@FXML
	private TextField txtValidateLDU_CmdStatus9;
	@FXML
	private TextField txtValidateLDU_CmdStatus10;
	@FXML
	private TextField txtValidateLDU_CmdStatus11;
	@FXML
	private TextField txtValidateLDU_CmdStatus12;
	@FXML
	private TextField txtValidateLDU_CmdStatus13;
	@FXML
	private TextField txtValidateLDU_CmdStatus14;
	@FXML
	private TextField txtValidateLDU_CmdStatus15;
	@FXML
	private TextField txtValidateLDU_CmdStatus16;
	@FXML
	private TextField txtValidateLDU_CmdStatus17;
	@FXML
	private TextField txtValidateLDU_CmdStatus18;
	@FXML
	private TextField txtValidateLDU_CmdStatus19;
	@FXML
	private TextField txtValidateLDU_CmdStatus20;
	@FXML
	private TextField txtValidateLDU_CmdStatus21;
	@FXML
	private TextField txtValidateLDU_CmdStatus22;
	@FXML
	private TextField txtValidateLDU_CmdStatus23;
	@FXML
	private TextField txtValidateLDU_CmdStatus24;
	@FXML
	private TextField txtValidateLDU_CmdStatus25;
	@FXML
	private TextField txtValidateLDU_CmdStatus26;
	@FXML
	private TextField txtValidateLDU_CmdStatus27;
	@FXML
	private TextField txtValidateLDU_CmdStatus28;
	@FXML
	private TextField txtValidateLDU_CmdStatus29;
	@FXML
	private TextField txtValidateLDU_CmdStatus30;
	@FXML
	private TextField txtValidateLDU_CmdStatus31;
	@FXML
	private TextField txtValidateLDU_CmdStatus32;
	@FXML
	private TextField txtValidateLDU_CmdStatus33;
	@FXML
	private TextField txtValidateLDU_CmdStatus34;
	@FXML
	private TextField txtValidateLDU_CmdStatus35;
	@FXML
	private TextField txtValidateLDU_CmdStatus36;
	@FXML
	private TextField txtValidateLDU_CmdStatus37;
	@FXML
	private TextField txtValidateLDU_CmdStatus38;
	@FXML
	private TextField txtValidateLDU_CmdStatus39;
	@FXML
	private TextField txtValidateLDU_CmdStatus40;
	@FXML
	private TextField txtValidateLDU_CmdStatus41;
	@FXML
	private TextField txtValidateLDU_CmdStatus42;
	@FXML
	private TextField txtValidateLDU_CmdStatus43;
	@FXML
	private TextField txtValidateLDU_CmdStatus44;
	@FXML
	private TextField txtValidateLDU_CmdStatus45;
	@FXML
	private TextField txtValidateLDU_CmdStatus46;
	@FXML
	private TextField txtValidateLDU_CmdStatus47;
	@FXML
	private TextField txtValidateLDU_CmdStatus48;

	@FXML
	private Button btnValidatePwrSrcCmd;
	@FXML
	private Button btnValidateRefStdCmd;

	@FXML
	private Button btnValidateDut1_Cmd;
	@FXML
	private Button btnValidateDut2_Cmd;
	@FXML
	private Button btnValidateDut3_Cmd;
	@FXML
	private Button btnValidateDut4_Cmd;
	@FXML
	private Button btnValidateDut5_Cmd;
	@FXML
	private Button btnValidateDut6_Cmd;

	@FXML
	private Button btnValidateLDU_Cmd3;
	@FXML
	private Button btnValidateLDU_Cmd4;
	@FXML
	private Button btnValidateLDU_Cmd5;
	@FXML
	private Button btnValidateLDU_Cmd6;
	@FXML
	private Button btnValidateLDU_Cmd7;
	@FXML
	private Button btnValidateLDU_Cmd8;
	@FXML
	private Button btnValidateLDU_Cmd9;
	@FXML
	private Button btnValidateLDU_Cmd10;
	@FXML
	private Button btnValidateLDU_Cmd11;
	@FXML
	private Button btnValidateLDU_Cmd12;
	@FXML
	private Button btnValidateLDU_Cmd13;
	@FXML
	private Button btnValidateLDU_Cmd14;
	@FXML
	private Button btnValidateLDU_Cmd15;
	@FXML
	private Button btnValidateLDU_Cmd16;
	@FXML
	private Button btnValidateLDU_Cmd17;
	@FXML
	private Button btnValidateLDU_Cmd18;
	@FXML
	private Button btnValidateLDU_Cmd19;
	@FXML
	private Button btnValidateLDU_Cmd20;
	@FXML
	private Button btnValidateLDU_Cmd21;
	@FXML
	private Button btnValidateLDU_Cmd22;
	@FXML
	private Button btnValidateLDU_Cmd23;
	@FXML
	private Button btnValidateLDU_Cmd24;
	@FXML
	private Button btnValidateLDU_Cmd25;
	@FXML
	private Button btnValidateLDU_Cmd26;
	@FXML
	private Button btnValidateLDU_Cmd27;
	@FXML
	private Button btnValidateLDU_Cmd28;
	@FXML
	private Button btnValidateLDU_Cmd29;
	@FXML
	private Button btnValidateLDU_Cmd30;
	@FXML
	private Button btnValidateLDU_Cmd31;
	@FXML
	private Button btnValidateLDU_Cmd32;
	@FXML
	private Button btnValidateLDU_Cmd33;
	@FXML
	private Button btnValidateLDU_Cmd34;
	@FXML
	private Button btnValidateLDU_Cmd35;
	@FXML
	private Button btnValidateLDU_Cmd36;
	@FXML
	private Button btnValidateLDU_Cmd37;
	@FXML
	private Button btnValidateLDU_Cmd38;
	@FXML
	private Button btnValidateLDU_Cmd39;
	@FXML
	private Button btnValidateLDU_Cmd40;
	@FXML
	private Button btnValidateLDU_Cmd41;
	@FXML
	private Button btnValidateLDU_Cmd42;
	@FXML
	private Button btnValidateLDU_Cmd43;
	@FXML
	private Button btnValidateLDU_Cmd44;
	@FXML
	private Button btnValidateLDU_Cmd45;
	@FXML
	private Button btnValidateLDU_Cmd46;
	@FXML
	private Button btnValidateLDU_Cmd47;
	@FXML
	private Button btnValidateLDU_Cmd48;

	private static Button ref_btnValidateDut1_Cmd;
	private static Button ref_btnValidateDut2_Cmd;
	private static Button ref_btnValidateDut3_Cmd;
	private static Button ref_btnValidateDut4_Cmd;
	private static Button ref_btnValidateDut5_Cmd;
	private static Button ref_btnValidateDut6_Cmd;
	private static Button ref_btnValidateLDU_Cmd7;
	private static Button ref_btnValidateLDU_Cmd8;
	private static Button ref_btnValidateLDU_Cmd9;
	private static Button ref_btnValidateLDU_Cmd10;
	private static Button ref_btnValidateLDU_Cmd11;
	private static Button ref_btnValidateLDU_Cmd12;
	private static Button ref_btnValidateLDU_Cmd13;
	private static Button ref_btnValidateLDU_Cmd14;
	private static Button ref_btnValidateLDU_Cmd15;
	private static Button ref_btnValidateLDU_Cmd16;
	private static Button ref_btnValidateLDU_Cmd17;
	private static Button ref_btnValidateLDU_Cmd18;
	private static Button ref_btnValidateLDU_Cmd19;
	private static Button ref_btnValidateLDU_Cmd20;
	private static Button ref_btnValidateLDU_Cmd21;
	private static Button ref_btnValidateLDU_Cmd22;
	private static Button ref_btnValidateLDU_Cmd23;
	private static Button ref_btnValidateLDU_Cmd24;
	private static Button ref_btnValidateLDU_Cmd25;
	private static Button ref_btnValidateLDU_Cmd26;
	private static Button ref_btnValidateLDU_Cmd27;
	private static Button ref_btnValidateLDU_Cmd28;
	private static Button ref_btnValidateLDU_Cmd29;
	private static Button ref_btnValidateLDU_Cmd30;
	private static Button ref_btnValidateLDU_Cmd31;
	private static Button ref_btnValidateLDU_Cmd32;
	private static Button ref_btnValidateLDU_Cmd33;
	private static Button ref_btnValidateLDU_Cmd34;
	private static Button ref_btnValidateLDU_Cmd35;
	private static Button ref_btnValidateLDU_Cmd36;
	private static Button ref_btnValidateLDU_Cmd37;
	private static Button ref_btnValidateLDU_Cmd38;
	private static Button ref_btnValidateLDU_Cmd39;
	private static Button ref_btnValidateLDU_Cmd40;
	private static Button ref_btnValidateLDU_Cmd41;
	private static Button ref_btnValidateLDU_Cmd42;
	private static Button ref_btnValidateLDU_Cmd43;
	private static Button ref_btnValidateLDU_Cmd44;
	private static Button ref_btnValidateLDU_Cmd45;
	private static Button ref_btnValidateLDU_Cmd46;
	private static Button ref_btnValidateLDU_Cmd47;
	private static Button ref_btnValidateLDU_Cmd48;

	Timer PwrSrcValidateTimer;
	Timer RefStdValidateTimer;
	Timer dut1_ValidateTimer;
	Timer dut2_ValidateTimer;
	Timer dut3_ValidateTimer;
	Timer dut4_ValidateTimer;
	Timer dut5_ValidateTimer;
	Timer dut6_ValidateTimer;
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

		ApplicationLauncher.logger.info("DutPortSetupController : applyUacSettings :  Entry");
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

		ref_cmbBxDut1_BaudRate = cmbBxDut1_BaudRate;
		ref_cmbBxDut2_BaudRate = cmbBxDut2_BaudRate;
		ref_cmbBxDut3_BaudRate = cmbBxDut3_BaudRate;
		ref_cmbBxDut4_BaudRate = cmbBxDut4_BaudRate;
		ref_cmbBxDut5_BaudRate = cmbBxDut5_BaudRate;
		ref_cmbBxDut6_BaudRate = cmbBxDut6_BaudRate;

		ref_cmbBxDut1_PortSelection = cmbBxDut1_PortSelection;
		ref_cmbBxDut2_PortSelection = cmbBxDut2_PortSelection;
		ref_cmbBxDut3_PortSelection = cmbBxDut3_PortSelection;
		ref_cmbBxDut4_PortSelection = cmbBxDut4_PortSelection;
		ref_cmbBxDut5_PortSelection = cmbBxDut5_PortSelection;
		ref_cmbBxDut6_PortSelection = cmbBxDut6_PortSelection;

		ref_cmbBxDut1_ModelName = cmbBxDut1_ModelName;
		ref_cmbBxDut2_ModelName = cmbBxDut2_ModelName;
		ref_cmbBxDut3_ModelName = cmbBxDut3_ModelName;
		ref_cmbBxDut4_ModelName = cmbBxDut4_ModelName;
		ref_cmbBxDut5_ModelName = cmbBxDut5_ModelName;
		ref_cmbBxDut6_ModelName = cmbBxDut6_ModelName;

		ref_txtValidateDut1_CmdStatus = txtValidateDut1_CmdStatus;
		ref_txtValidateDut2_CmdStatus = txtValidateDut2_CmdStatus;
		ref_txtValidateDut3_CmdStatus = txtValidateDut3_CmdStatus;
		ref_txtValidateDut4_CmdStatus = txtValidateDut4_CmdStatus;
		ref_txtValidateDut5_CmdStatus = txtValidateDut5_CmdStatus;
		ref_txtValidateDut6_CmdStatus = txtValidateDut6_CmdStatus;

		ref_txtDut1ReadData = txtDut1ReadData;
		ref_txtDut2ReadData = txtDut2ReadData;
		ref_txtDut3ReadData = txtDut3ReadData;
		ref_txtDut4ReadData = txtDut4ReadData;
		ref_txtDut5ReadData = txtDut5ReadData;
		ref_txtDut6ReadData = txtDut6ReadData;

		ref_btnValidateDut1_Cmd = btnValidateDut1_Cmd;
		ref_btnValidateDut2_Cmd = btnValidateDut2_Cmd;
		ref_btnValidateDut3_Cmd = btnValidateDut3_Cmd;
		ref_btnValidateDut4_Cmd = btnValidateDut4_Cmd;
		ref_btnValidateDut5_Cmd = btnValidateDut5_Cmd;
		ref_btnValidateDut6_Cmd = btnValidateDut6_Cmd;
		/*
		 * ref_btnValidateLDU_Cmd7 = btnValidateLDU_Cmd7;
		 * ref_btnValidateLDU_Cmd8 = btnValidateLDU_Cmd8;
		 * ref_btnValidateLDU_Cmd9 = btnValidateLDU_Cmd9;
		 * ref_btnValidateLDU_Cmd10 = btnValidateLDU_Cmd10;
		 * ref_btnValidateLDU_Cmd11 = btnValidateLDU_Cmd11;
		 * ref_btnValidateLDU_Cmd12 = btnValidateLDU_Cmd12;
		 * ref_btnValidateLDU_Cmd13 = btnValidateLDU_Cmd13;
		 * ref_btnValidateLDU_Cmd14 = btnValidateLDU_Cmd14;
		 * ref_btnValidateLDU_Cmd15 = btnValidateLDU_Cmd15;
		 * ref_btnValidateLDU_Cmd16 = btnValidateLDU_Cmd16;
		 * ref_btnValidateLDU_Cmd17 = btnValidateLDU_Cmd17;
		 * ref_btnValidateLDU_Cmd18 = btnValidateLDU_Cmd18;
		 * ref_btnValidateLDU_Cmd19 = btnValidateLDU_Cmd19;
		 * ref_btnValidateLDU_Cmd20 = btnValidateLDU_Cmd20;
		 * ref_btnValidateLDU_Cmd21 = btnValidateLDU_Cmd21;
		 * ref_btnValidateLDU_Cmd22 = btnValidateLDU_Cmd22;
		 * ref_btnValidateLDU_Cmd23 = btnValidateLDU_Cmd23;
		 * ref_btnValidateLDU_Cmd24 = btnValidateLDU_Cmd24;
		 * ref_btnValidateLDU_Cmd25 = btnValidateLDU_Cmd25;
		 * ref_btnValidateLDU_Cmd26 = btnValidateLDU_Cmd26;
		 * ref_btnValidateLDU_Cmd27 = btnValidateLDU_Cmd27;
		 * ref_btnValidateLDU_Cmd28 = btnValidateLDU_Cmd28;
		 * ref_btnValidateLDU_Cmd29 = btnValidateLDU_Cmd29;
		 * ref_btnValidateLDU_Cmd30 = btnValidateLDU_Cmd30;
		 * ref_btnValidateLDU_Cmd31 = btnValidateLDU_Cmd31;
		 * ref_btnValidateLDU_Cmd32 = btnValidateLDU_Cmd32;
		 * ref_btnValidateLDU_Cmd33 = btnValidateLDU_Cmd33;
		 * ref_btnValidateLDU_Cmd34 = btnValidateLDU_Cmd34;
		 * ref_btnValidateLDU_Cmd35 = btnValidateLDU_Cmd35;
		 * ref_btnValidateLDU_Cmd36 = btnValidateLDU_Cmd36;
		 * ref_btnValidateLDU_Cmd37 = btnValidateLDU_Cmd37;
		 * ref_btnValidateLDU_Cmd38 = btnValidateLDU_Cmd38;
		 * ref_btnValidateLDU_Cmd39 = btnValidateLDU_Cmd39;
		 * ref_btnValidateLDU_Cmd40 = btnValidateLDU_Cmd40;
		 * 
		 * ref_btnValidateLDU_Cmd41 = btnValidateLDU_Cmd41;
		 * ref_btnValidateLDU_Cmd42 = btnValidateLDU_Cmd42;
		 * ref_btnValidateLDU_Cmd43 = btnValidateLDU_Cmd43;
		 * ref_btnValidateLDU_Cmd44 = btnValidateLDU_Cmd44;
		 * ref_btnValidateLDU_Cmd45 = btnValidateLDU_Cmd45;
		 * ref_btnValidateLDU_Cmd46 = btnValidateLDU_Cmd46;
		 * ref_btnValidateLDU_Cmd47 = btnValidateLDU_Cmd47;
		 * ref_btnValidateLDU_Cmd48 = btnValidateLDU_Cmd48;
		 */
		ref_btn_Save = btn_Save;

		ref_cmbBxDut1ClusterId = cmbBxDut1ClusterId;
		ref_cmbBxDut2ClusterId = cmbBxDut2ClusterId;
		ref_cmbBxDut3ClusterId = cmbBxDut3ClusterId;
		ref_cmbBxDut4ClusterId = cmbBxDut4ClusterId;
		ref_cmbBxDut5ClusterId = cmbBxDut5ClusterId;
		ref_cmbBxDut6ClusterId = cmbBxDut6ClusterId;

		ref_cmbBxDut1BayId = cmbBxDut1BayId;
		ref_cmbBxDut2BayId = cmbBxDut2BayId;
		ref_cmbBxDut3BayId = cmbBxDut3BayId;
		ref_cmbBxDut4BayId = cmbBxDut4BayId;
		ref_cmbBxDut5BayId = cmbBxDut5BayId;
		ref_cmbBxDut6BayId = cmbBxDut6BayId;

		ref_cmbBxDut1PositionId = cmbBxDut1PositionId;
		ref_cmbBxDut2PositionId = cmbBxDut2PositionId;
		ref_cmbBxDut3PositionId = cmbBxDut3PositionId;
		ref_cmbBxDut4PositionId = cmbBxDut4PositionId;
		ref_cmbBxDut5PositionId = cmbBxDut5PositionId;
		ref_cmbBxDut6PositionId = cmbBxDut6PositionId;

		// ref_cmbBxDutCname1 = cmbBxDutCname1;
		ref_txtDut1Cname = txtDut1Cname;
		ref_txtDut2Cname = txtDut2Cname;
		ref_txtDut3Cname = txtDut3Cname;
		ref_txtDut4Cname = txtDut4Cname;
		ref_txtDut5Cname = txtDut5Cname;
		ref_txtDut6Cname = txtDut6Cname;
		// ref_cmbBxDutCname2 = cmbBxDutCname2;
		ref_cmbBxQrScannerName3 = cmbBxQrScannerName3;
		ref_cmbBxQrScannerName4 = cmbBxQrScannerName4;
		ref_cmbBxQrScannerName5 = cmbBxQrScannerName5;
		ref_cmbBxQrScannerName6 = cmbBxQrScannerName6;
		ref_cmbBxQrScannerName7 = cmbBxQrScannerName7;
		ref_cmbBxQrScannerName8 = cmbBxQrScannerName8;
		ref_cmbBxQrScannerName9 = cmbBxQrScannerName9;
		ref_cmbBxQrScannerName10 = cmbBxQrScannerName10;
		ref_cmbBxQrScannerName11 = cmbBxQrScannerName11;
		ref_cmbBxQrScannerName12 = cmbBxQrScannerName12;
		ref_cmbBxQrScannerName13 = cmbBxQrScannerName13;
		ref_cmbBxQrScannerName14 = cmbBxQrScannerName14;
		ref_cmbBxQrScannerName15 = cmbBxQrScannerName15;
		ref_cmbBxQrScannerName16 = cmbBxQrScannerName16;

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
		// loadDutCname();
		load_saved_device_settings();
	}

	public void loadDutCname() {

		ArrayList<String> ModelList = new ArrayList<String>();
		String ModelName = "";
		// ConstantApp MyPropertyObj= new ConstantApp();
		// ModelName = ConstantQrScanner.QR_SCANNER_MODEL;

		List<DutDevice> dutDeviceList = ConveyorDataManager.getTerminalBayConfig().getTerminal().stream()
				.filter(e -> e.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID))
				.flatMap(terminal -> terminal.getDutDevice().stream())
				.collect(Collectors.toList());
		// .filter(p -> searchPortName.equals(p.getPortName()))
		// .findFirst();

		for (DutDevice eachDutDevice : dutDeviceList) {
			ModelList.add(eachDutDevice.getPortName());
		}

		/*
		 * ref_cmbBxDutCname1.getItems().clear();
		 * ref_cmbBxDutCname2.getItems().clear();
		 * 
		 * if(ModelList.size()>0){
		 * ref_cmbBxDutCname1.getItems().addAll(ModelList);
		 * ref_cmbBxDutCname1.getSelectionModel().select(0);
		 * 
		 * ref_cmbBxDutCname2.getItems().addAll(ModelList);
		 * ref_cmbBxDutCname2.getSelectionModel().select(0);
		 * }
		 */

		// =============================================
	}

	public void loadDataFromConfig() {

		ApplicationLauncher.logger.debug("loadDataFromConfig-D: Entry");
		// ref_cmbBxDut1ClusterId.getItems().clear();

		// ref_cmbBxDutClusterId1;
		// ref_cmbBxDutBayId1;

		// =======================================
		ref_cmbBxDut1ClusterId.getItems().clear();
		ref_cmbBxDut2ClusterId.getItems().clear();
		ref_cmbBxDut3ClusterId.getItems().clear();
		ref_cmbBxDut4ClusterId.getItems().clear();
		ref_cmbBxDut5ClusterId.getItems().clear();
		ref_cmbBxDut6ClusterId.getItems().clear();
		for (Terminal eachTerminal : getBayConfigModel().getTerminal()) {
			if (eachTerminal.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID)) {
				for (ClusterDetail eachClusterDetail : eachTerminal.getClusterDetails()) {
					ref_cmbBxDut1ClusterId.getItems().add(eachClusterDetail.getName());
					ref_cmbBxDut2ClusterId.getItems().add(eachClusterDetail.getName());
					ref_cmbBxDut3ClusterId.getItems().add(eachClusterDetail.getName());
					ref_cmbBxDut4ClusterId.getItems().add(eachClusterDetail.getName());
					ref_cmbBxDut5ClusterId.getItems().add(eachClusterDetail.getName());
					ref_cmbBxDut6ClusterId.getItems().add(eachClusterDetail.getName());
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
						for (DutDevice eachDutDevice : eachTerminal.getDutDevice()) {
							if ((eachDutDevice.getClusterId().equals(clusterId))
									&& (eachDutDevice.getBayId().equals(bayId))) {
								// getClusterBayNamePositionListMap().put
								positionNoList.add(eachDutDevice.getPositionId());
								getClusterBayPositionNoCnameMap().put(
										eachClusterDetail.getName() + "_" + eachBay.getBayName() + "_"
												+ eachDutDevice.getPositionId(),
										eachDutDevice.getPortName());
								getClusterBayPositionNoDeviceIdMap().put(
										eachClusterDetail.getName() + "_" + eachBay.getBayName() + "_"
												+ eachDutDevice.getPositionId(),
										eachDutDevice.getDeviceId());

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
			ApplicationLauncher.logger.debug("loadDataFromConfig-1: getClusterBayPositionNoDeviceIdMap: key : "
					+ e.getKey() + " -> " + e.getValue());
		});

		if (ref_cmbBxDut1ClusterId.getItems().size() > 0) {
			ref_cmbBxDut1ClusterId.getSelectionModel().select(0);
			ref_cmbBxDut2ClusterId.getSelectionModel().select(0);
			ref_cmbBxDut3ClusterId.getSelectionModel().select(0);
			ref_cmbBxDut4ClusterId.getSelectionModel().select(0);
			ref_cmbBxDut5ClusterId.getSelectionModel().select(0);
			ref_cmbBxDut6ClusterId.getSelectionModel().select(0);
		}

		if (getClusterBayNameListMap().size() > 0) {
			if (getClusterBayNameListMap()
					.containsKey(ref_cmbBxDut1ClusterId.getSelectionModel().getSelectedItem().toString())) {
				ref_cmbBxDut1BayId.getItems().addAll(getClusterBayNameListMap()
						.get(ref_cmbBxDut1ClusterId.getSelectionModel().getSelectedItem().toString()));
				ref_cmbBxDut2BayId.getItems().addAll(getClusterBayNameListMap()
						.get(ref_cmbBxDut2ClusterId.getSelectionModel().getSelectedItem().toString()));
				ref_cmbBxDut3BayId.getItems().addAll(getClusterBayNameListMap()
						.get(ref_cmbBxDut2ClusterId.getSelectionModel().getSelectedItem().toString()));
				ref_cmbBxDut4BayId.getItems().addAll(getClusterBayNameListMap()
						.get(ref_cmbBxDut2ClusterId.getSelectionModel().getSelectedItem().toString()));
				ref_cmbBxDut5BayId.getItems().addAll(getClusterBayNameListMap()
						.get(ref_cmbBxDut2ClusterId.getSelectionModel().getSelectedItem().toString()));
				ref_cmbBxDut6BayId.getItems().addAll(getClusterBayNameListMap()
						.get(ref_cmbBxDut2ClusterId.getSelectionModel().getSelectedItem().toString()));
			}
			// ref_cmbBxBaySelection.getSelectionModel().select(0);

		}
		if (ref_cmbBxDut1BayId.getItems().size() > 0) {

			ref_cmbBxDut1BayId.getSelectionModel().select(0);
			ref_cmbBxDut2BayId.getSelectionModel().select(0);
			ref_cmbBxDut3BayId.getSelectionModel().select(0);
			ref_cmbBxDut4BayId.getSelectionModel().select(0);
			ref_cmbBxDut5BayId.getSelectionModel().select(0);
			ref_cmbBxDut6BayId.getSelectionModel().select(0);
			String clusterName = (ref_cmbBxDut1ClusterId.getSelectionModel().getSelectedItem().toString());
			String bayName = (ref_cmbBxDut1BayId.getSelectionModel().getSelectedItem().toString());

			String clusterId = getClusterNameIdListMap().get(clusterName);
			String bayId = getClusterBayNameIdMap().get(clusterName + "_" + bayName);
			// ApplicationLauncher.logger.debug("loadDataFromConfig :
			// getClusterBayNameIdMap().get(clusterName)-2 :"+
			// getClusterBayNameIdMap().get(clusterName));
			// String bayId = "11";//getClusterBayNameIdMap().get(clusterName).get(bayName);

			ApplicationLauncher.logger.debug("loadDataFromConfig : clusterId :" + clusterId);
			ApplicationLauncher.logger.debug("loadDataFromConfig : bayId :" + bayId);

			ArrayList<DutDevice> dutDeviceList = (ArrayList<DutDevice>) getBayConfigModel().getTerminal().stream()
					.filter(e1 -> e1.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID))
					.flatMap(terminal -> terminal.getDutDevice().stream())
					.filter(e2 -> e2.getBayId().equals(bayId))
					.collect(Collectors.toList());
			ArrayList<String> cNameList = new ArrayList<String>();
			for (DutDevice eachDutDevice : dutDeviceList) {

				ref_cmbBxDut1PositionId.getItems().add(eachDutDevice.getPositionId());
				ref_cmbBxDut2PositionId.getItems().add(eachDutDevice.getPositionId());
				ref_cmbBxDut3PositionId.getItems().add(eachDutDevice.getPositionId());
				ref_cmbBxDut4PositionId.getItems().add(eachDutDevice.getPositionId());
				ref_cmbBxDut5PositionId.getItems().add(eachDutDevice.getPositionId());
				ref_cmbBxDut6PositionId.getItems().add(eachDutDevice.getPositionId());
				cNameList.add(eachDutDevice.getPortName());
			}
			// ref_cmbBxDutCname1.getItems().addAll(cNameList);

			if (ref_cmbBxDut1PositionId.getItems().size() > 0) {
				ref_cmbBxDut1PositionId.getSelectionModel().select(0);
				ref_cmbBxDut2PositionId.getSelectionModel().select(0);
				ref_cmbBxDut3PositionId.getSelectionModel().select(0);
				ref_cmbBxDut4PositionId.getSelectionModel().select(0);
				ref_cmbBxDut5PositionId.getSelectionModel().select(0);
				ref_cmbBxDut6PositionId.getSelectionModel().select(0);
				// ref_cmbBxDutCname1.getSelectionModel().select(0);
				// ref_txtDutCname1.setText(arg0);
			}
			if (cNameList.size() > 0) {
				ref_txtDut1Cname.setText(cNameList.get(0));
				ref_txtDut2Cname.setText(cNameList.get(0));
				ref_txtDut3Cname.setText(cNameList.get(0));
				ref_txtDut4Cname.setText(cNameList.get(0));
				ref_txtDut5Cname.setText(cNameList.get(0));
				ref_txtDut6Cname.setText(cNameList.get(0));
			}

		}
	}

	public void guiDutRefresh(
			int dutNo,
			String modelKey,
			Object clusterIdObj,
			Object bayIdObj,
			Object positionIdObj,
			TextField c_nameObj,
			Object portNameObj,
			Object baudRateObj) {

		JSONObject saved_ldu_setting = MySQL_Controller.sp_getdevice_setting(modelKey);

		ComboBox<String> clusterId = (ComboBox<String>) clusterIdObj;
		ComboBox<String> bayId = (ComboBox<String>) bayIdObj;
		ComboBox<String> positionId = (ComboBox<String>) positionIdObj;
		TextField c_name = (TextField) c_nameObj;
		ComboBox<String> portName = (ComboBox<String>) portNameObj;
		ComboBox<Integer> baudRate = (ComboBox<Integer>) baudRateObj;

		String selectedClusterName = "";
		try {
			if (saved_ldu_setting.has("cluster_name")) {
				// ref_cmbBxDutCname1.setValue(saved_ldu1_setting.getString("c_name"));
				selectedClusterName = saved_ldu_setting.getString("cluster_name");
				clusterId.getSelectionModel().select(selectedClusterName);// ref_cmbBxDut1ClusterId
				bayId.getItems().clear();// ref_cmbBxDut1BayId
				bayId.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));

			} else {
				// ref_cmbBxDutCname1.setValue("");
				clusterId.getSelectionModel().select("No-ClusterId" + dutNo);
				ApplicationLauncher.logger.debug("load_saved_device_settings: dut" + dutNo
						+ " cluster_name  Selection: Data not retrieved from DB");

			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error(
					"load_saved_device_settings: dut" + dutNo + " cluster_name: JSONException-1:" + e.getMessage());
			// ref_cmbBxDutCname1.setValue("");
			clusterId.getSelectionModel().select("Cluster-Ex" + dutNo);
			ApplicationLauncher.logger.info(
					"load_saved_device_settings: dut" + dutNo + " cluster_name:  Data not retrieved from database");

		}

		String selectedBayName = "";

		try {
			if (saved_ldu_setting.has("bay_name")) {
				// ref_cmbBxDutCname1.setValue(saved_ldu1_setting.getString("c_name"));
				selectedBayName = saved_ldu_setting.getString("bay_name");
				bayId.getSelectionModel().select(selectedBayName);
				selectedClusterName = (String) clusterId.getSelectionModel().getSelectedItem();
				positionId.getItems().clear();
				positionId.getItems()
						.addAll(getClusterBayNamePositionListMap().get(selectedClusterName + "_" + selectedBayName));

			} else {
				// ref_cmbBxDutCname1.setValue("");
				bayId.getSelectionModel().select("No-BayId" + dutNo);
				ApplicationLauncher.logger.debug(
						"load_saved_device_settings: dut" + dutNo + " bay_name  Selection: Data not retrieved from DB");

			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger
					.error("load_saved_device_settings: dut" + dutNo + " bay_name: JSONException-1:" + e.getMessage());
			// ref_cmbBxDutCname1.setValue("");
			bayId.getSelectionModel().select("Bay-Ex" + dutNo);
			ApplicationLauncher.logger
					.info("load_saved_device_settings: dut" + dutNo + " bay_name:  Data not retrieved from database");

		}

		String selectedPositionNo = "";

		try {
			if (saved_ldu_setting.has("position_no")) {
				// ref_cmbBxDutCname1.setValue(saved_ldu1_setting.getString("c_name"));
				selectedPositionNo = saved_ldu_setting.getString("position_no");
				positionId.getSelectionModel().select(selectedPositionNo);
				/*
				 * selectedClusterName =
				 * (String)ref_cmbBxDut1ClusterId.getSelectionModel().getSelectedItem();
				 * ref_cmbBxDut1PositionId.getItems().clear();
				 * ref_cmbBxDut1PositionId.getItems().addAll(getClusterBayNamePositionListMap().
				 * get(selectedClusterName + "_"+selectedBayName));
				 */

			} else {
				// ref_cmbBxDutCname1.setValue("");
				positionId.getSelectionModel().select("No-PositionId" + dutNo);
				ApplicationLauncher.logger.debug("load_saved_device_settings: dut" + dutNo
						+ " position_no  Selection: Data not retrieved from DB");

			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error(
					"load_saved_device_settings: dut" + dutNo + " position_no: JSONException-1:" + e.getMessage());
			// ref_cmbBxDutCname1.setValue("");
			positionId.getSelectionModel().select("PositionId-Ex" + dutNo);
			ApplicationLauncher.logger.info(
					"load_saved_device_settings: dut" + dutNo + " position_no:  Data not retrieved from database");

		}

		try {
			if (saved_ldu_setting.has("c_name")) {
				// ref_cmbBxDutCname1.setValue(saved_ldu1_setting.getString("c_name"));
				c_name.setText(saved_ldu_setting.getString("c_name"));
			} else {
				// ref_cmbBxDutCname1.setValue("");
				c_name.setText("noCname-test1");
				ApplicationLauncher.logger.debug(
						"load_saved_device_settings: qr Port name" + dutNo + " Selection: Data not retrieved from DB");

			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger
					.error("load_saved_device_settings: qr_port_name" + dutNo + ": JSONException-1:" + e.getMessage());
			// ref_cmbBxDutCname1.setValue("");
			c_name.setText("Exception-test2");
			ApplicationLauncher.logger
					.info("load_saved_device_settings: qr_port_name" + dutNo + ":  Data not retrieved from database");

		}

		try {
			if (saved_ldu_setting.has("port_name")) {
				portName.setValue(saved_ldu_setting.getString("port_name"));
			} else {
				portName.setValue("");
				ApplicationLauncher.logger
						.info("load_saved_device_settings: LDU" + dutNo + "_PortSelection: Data not retrieved from DB");

			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-1:" + e.getMessage());
			portName.setValue("");
			ApplicationLauncher.logger.info(
					"load_saved_device_settings: LDU" + dutNo + "_PortSelection: Data not retrieved from database");

		}

		try {
			if (saved_ldu_setting.has("baud_rate")) {
				baudRate.setValue(Integer.parseInt(saved_ldu_setting.getString("baud_rate")));
			} else {
				baudRate.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: LDU_BaudRate: Data not retrieved from DB");

			}
		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-1:" + e.getMessage());
			baudRate.setValue(9600);
			ApplicationLauncher.logger
					.info("load_saved_device_settings: LDU_BaudRate: Data not retrieved from database");

		}
	}

	public void guiRefreshDutV2(
			int qrNo,
			String modelKey,
			Object clusterIdObj,
			Object bayIdObj,
			Object positionIdObj,
			TextField c_nameObj,
			Object portNameObj,
			Object baudRateObj) {

		DeviceSetting savedDutDeviceSetting = MySqlServiceManager.getDeviceSettingService()
				.findFirstByDeviceTypeKey(modelKey);

		// JSONObject saved_ldu_setting =
		// MySQL_Controller.sp_getdevice_setting(modelKey);
		if (savedDutDeviceSetting != null) {
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
				selectedClusterName = savedDutDeviceSetting.getClusterName();// saved_ldu_setting.getString("cluster_name");
				clusterId.getSelectionModel().select(selectedClusterName);
				bayId.getItems().clear();
				bayId.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));

				/*
				 * } else {
				 * //ref_cmbBxQrCname1.setValue("");
				 * clusterId.getSelectionModel().select("No-ClusterId"+ qrNo);
				 * ApplicationLauncher.logger.debug("guiRefreshDutV2: qr"+ qrNo +
				 * " cluster_name  Selection: Data not retrieved from DB");
				 * 
				 * }
				 */

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger
						.error("guiRefreshDutV2: dut-" + qrNo + " cluster_name: Exception-1:" + e.getMessage());
				// ref_cmbBxQrCname1.setValue("");
				clusterId.getSelectionModel().select("Cluster-Ex" + qrNo);
				ApplicationLauncher.logger
						.info("guiRefreshDutV2: dut-" + qrNo + " cluster_name:  Data not retrieved from database");

			}

			String selectedBayName = "";

			try {
				// if(saved_ldu_setting.has("bay_name")){
				// ref_cmbBxQrCname1.setValue(saved_ldu1_setting.getString("c_name"));
				selectedBayName = savedDutDeviceSetting.getBayName();// saved_ldu_setting.getString("bay_name");
				ApplicationLauncher.logger.debug("guiRefreshDutV2: selectedBayName: " + selectedBayName);
				ApplicationLauncher.logger.debug("guiRefreshDutV2: selectedClusterName: " + selectedClusterName);
				bayId.getSelectionModel().select(selectedBayName);
				selectedClusterName = (String) clusterId.getSelectionModel().getSelectedItem();
				positionId.getItems().clear();
				positionId.getItems()
						.addAll(getClusterBayNamePositionListMap().get(selectedClusterName + "_" + selectedBayName));

				/*
				 * } else {
				 * //ref_cmbBxQrCname1.setValue("");
				 * bayId.getSelectionModel().select("No-BayId"+ qrNo);
				 * ApplicationLauncher.logger.debug("guiRefreshDutV2: qr"+ qrNo +
				 * " bay_name  Selection: Data not retrieved from DB");
				 * 
				 * }
				 */

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger
						.error("guiRefreshDutV2: dut-" + qrNo + " bay_name: Exception-1:" + e.getMessage());
				// ref_cmbBxQrCname1.setValue("");
				bayId.getSelectionModel().select("Bay-Ex1");
				ApplicationLauncher.logger
						.info("guiRefreshDutV2: dut-" + qrNo + " bay_name:  Data not retrieved from database");

			}

			String selectedPositionNo = "";

			try {
				// if(saved_ldu_setting.has("position_no")){
				// ref_cmbBxQrCname1.setValue(saved_ldu1_setting.getString("c_name"));
				selectedPositionNo = savedDutDeviceSetting.getPositionNo();// saved_ldu_setting.getString("position_no");
				positionId.getSelectionModel().select(selectedPositionNo);
				/*
				 * selectedClusterName =
				 * (String)ref_cmbBxQr1ClusterId.getSelectionModel().getSelectedItem();
				 * ref_cmbBxQr1PositionId.getItems().clear();
				 * ref_cmbBxQr1PositionId.getItems().addAll(getClusterBayNamePositionListMap().
				 * get(selectedClusterName + "_"+selectedBayName));
				 */
				/*
				 * if(getClusterBayPositionNoAddressListMap().containsKey(selectedClusterName+
				 * "_"+selectedBayName + "_"+ selectedPositionNo)) {
				 * ref_cmbBxLdu1DeviceAddress.getItems().addAll(
				 * getClusterBayPositionNoAddressListMap().get(selectedClusterName+"_"+
				 * selectedBayName + "_"+ selectedPositionNo));
				 * ref_cmbBxLdu1DeviceAddress.getSelectionModel().select(0);
				 * }
				 */

				/*
				 * } else {
				 * //ref_cmbBxQrCname1.setValue("");
				 * positionId.getSelectionModel().select("No-PositionId"+ qrNo);
				 * ApplicationLauncher.logger.debug("guiRefreshDutV2: qr"+ qrNo +
				 * " position_no  Selection: Data not retrieved from DB");
				 * 
				 * }
				 */

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger
						.error("guiRefreshDutV2: dut-" + qrNo + " position_no: Exception-1:" + e.getMessage());
				// ref_cmbBxQrCname1.setValue("");
				positionId.getSelectionModel().select("PositionId-Ex" + qrNo);
				ApplicationLauncher.logger
						.info("guiRefreshDutV2: dut-" + qrNo + " position_no:  Data not retrieved from database");

			}

			try {
				// if(saved_ldu_setting.has("c_name")){
				c_name.setText(savedDutDeviceSetting.getCanName());// saved_ldu_setting.getString("c_name"));
				/*
				 * } else {
				 * c_name.setText("");
				 * ApplicationLauncher.logger.debug("guiRefreshDutV2: qr Port name"+ qrNo +
				 * " Selection: Data not retrieved from DB");
				 * 
				 * }
				 */

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger
						.error("guiRefreshDutV2: dut c_name" + qrNo + ": Exception-1:" + e.getMessage());
				c_name.setText("");
				ApplicationLauncher.logger
						.info("guiRefreshDutV2: dut c_name" + qrNo + ":  Data not retrieved from database");

			}

			try {
				// if(saved_ldu_setting.has("port_name")){
				portName.setValue(savedDutDeviceSetting.getPortName());// saved_ldu_setting.getString("port_name"));
				/*
				 * } else {
				 * portName.setValue("");
				 * ApplicationLauncher.logger.info("guiRefreshDutV2: LDU"+ qrNo +
				 * "_PortSelection: Data not retrieved from DB");
				 * 
				 * }
				 */

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger.error("guiRefreshDutV2: JSONException5-1:" + e.getMessage());
				portName.setValue("");
				ApplicationLauncher.logger
						.info("guiRefreshDutV2: Dut-" + qrNo + "_PortSelection: Data not retrieved from database");

			}

			try {
				// if(saved_ldu_setting.has("baud_rate")){
				baudRate.setValue(Integer.parseInt(savedDutDeviceSetting.getBaudRate()));// saved_ldu_setting.getString("baud_rate")));
				/*
				 * } else {
				 * baudRate.setValue(9600);
				 * ApplicationLauncher.logger.
				 * info("guiRefreshDutV2: LDU_BaudRate: Data not retrieved from DB");
				 * 
				 * }
				 */
			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger.error("guiRefreshDutV2: JSONException6-1:" + e.getMessage());
				baudRate.setValue(9600);
				ApplicationLauncher.logger.info("guiRefreshDutV2: dut_BaudRate: Data not retrieved from database");

			}
		}
	}

	public void load_saved_device_settings() {

		// JSONObject saved_pwr_setting =
		// MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_POWER_SOURCE);
		// JSONObject saved_ref_setting =
		// MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_REF_STD);
		// JSONObject saved_ldu1_setting =
		// MySQL_Controller.sp_getdevice_setting(ConstantDutDevSys.DUT_EM_1);
		// JSONObject saved_ldu2_setting =
		// MySQL_Controller.sp_getdevice_setting(ConstantDutDevSys.DUT_EM_2);
		// guiDutRefresh(1, ConstantDutDevSys.DUT_EM_1,ref_cmbBxDut1ClusterId,
		// ref_cmbBxDut1BayId, ref_cmbBxDut1PositionId, ref_txtDut1Cname,
		// cmbBxDut_PortSelection1, cmbBxDut_BaudRate1);
		// guiDutRefresh(2, ConstantDutDevSys.DUT_EM_2,ref_cmbBxDut2ClusterId,
		// ref_cmbBxDut2BayId, ref_cmbBxDut2PositionId, ref_txtDut2Cname,
		// cmbBxDut_PortSelection2, cmbBxDut_BaudRate2);

		ArrayList<String> modelKeyList = new ArrayList<String>();
		modelKeyList.add(ConstantDutDevSys.DUT_EM_1);
		modelKeyList.add(ConstantDutDevSys.DUT_EM_2);
		modelKeyList.add(ConstantDutDevSys.DUT_EM_3);
		modelKeyList.add(ConstantDutDevSys.DUT_EM_4);
		modelKeyList.add(ConstantDutDevSys.DUT_EM_5);
		modelKeyList.add(ConstantDutDevSys.DUT_EM_6);

		ArrayList<Object> clusterIdList = new ArrayList<Object>();
		clusterIdList.add(ref_cmbBxDut1ClusterId);
		clusterIdList.add(ref_cmbBxDut2ClusterId);
		clusterIdList.add(ref_cmbBxDut3ClusterId);
		clusterIdList.add(ref_cmbBxDut4ClusterId);
		clusterIdList.add(ref_cmbBxDut5ClusterId);
		clusterIdList.add(ref_cmbBxDut6ClusterId);

		ArrayList<Object> bayIdList = new ArrayList<Object>();
		bayIdList.add(ref_cmbBxDut1BayId);
		bayIdList.add(ref_cmbBxDut2BayId);
		bayIdList.add(ref_cmbBxDut3BayId);
		bayIdList.add(ref_cmbBxDut4BayId);
		bayIdList.add(ref_cmbBxDut5BayId);
		bayIdList.add(ref_cmbBxDut6BayId);

		ArrayList<Object> positionIdList = new ArrayList<Object>();
		positionIdList.add(ref_cmbBxDut1PositionId);
		positionIdList.add(ref_cmbBxDut2PositionId);
		positionIdList.add(ref_cmbBxDut3PositionId);
		positionIdList.add(ref_cmbBxDut4PositionId);
		positionIdList.add(ref_cmbBxDut5PositionId);
		positionIdList.add(ref_cmbBxDut6PositionId);

		ArrayList<TextField> CnameList = new ArrayList<TextField>();
		CnameList.add(ref_txtDut1Cname);
		CnameList.add(ref_txtDut2Cname);
		CnameList.add(ref_txtDut3Cname);
		CnameList.add(ref_txtDut4Cname);
		CnameList.add(ref_txtDut5Cname);
		CnameList.add(ref_txtDut6Cname);

		ArrayList<Object> portNameList = new ArrayList<Object>();
		portNameList.add(ref_cmbBxDut1_PortSelection);
		portNameList.add(ref_cmbBxDut2_PortSelection);
		portNameList.add(ref_cmbBxDut3_PortSelection);
		portNameList.add(ref_cmbBxDut4_PortSelection);
		portNameList.add(ref_cmbBxDut5_PortSelection);
		portNameList.add(ref_cmbBxDut6_PortSelection);

		ArrayList<Object> baudRateList = new ArrayList<Object>();
		baudRateList.add(ref_cmbBxDut1_BaudRate);
		baudRateList.add(ref_cmbBxDut2_BaudRate);
		baudRateList.add(ref_cmbBxDut3_BaudRate);
		baudRateList.add(ref_cmbBxDut4_BaudRate);
		baudRateList.add(ref_cmbBxDut5_BaudRate);
		baudRateList.add(ref_cmbBxDut6_BaudRate);

		for (int i = 0; i < modelKeyList.size(); i++) {
			if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {
				guiRefreshDutV2(i + 1, modelKeyList.get(i), clusterIdList.get(i), bayIdList.get(i),
						positionIdList.get(i), CnameList.get(i), portNameList.get(i), baudRateList.get(i));
			} else {
				guiDutRefresh(i + 1, modelKeyList.get(i), clusterIdList.get(i), bayIdList.get(i), positionIdList.get(i),
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
		ref_cmbBxDut1_BaudRate.getItems().clear();
		ref_cmbBxDut1_BaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
		ref_cmbBxDut1_BaudRate.getSelectionModel().select(ConstantDutDevSys.DUT_DEFAULT_BAUD_RATE);

		ref_cmbBxDut2_BaudRate.getItems().clear();
		ref_cmbBxDut2_BaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
		ref_cmbBxDut2_BaudRate.getSelectionModel().select(ConstantDutDevSys.DUT_DEFAULT_BAUD_RATE);

		ref_cmbBxDut3_BaudRate.getItems().clear();
		ref_cmbBxDut3_BaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
		ref_cmbBxDut3_BaudRate.getSelectionModel().select(ConstantDutDevSys.DUT_DEFAULT_BAUD_RATE);

		ref_cmbBxDut4_BaudRate.getItems().clear();
		ref_cmbBxDut4_BaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
		ref_cmbBxDut4_BaudRate.getSelectionModel().select(ConstantDutDevSys.DUT_DEFAULT_BAUD_RATE);

		ref_cmbBxDut5_BaudRate.getItems().clear();
		ref_cmbBxDut5_BaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
		ref_cmbBxDut5_BaudRate.getSelectionModel().select(ConstantDutDevSys.DUT_DEFAULT_BAUD_RATE);

		ref_cmbBxDut6_BaudRate.getItems().clear();
		ref_cmbBxDut6_BaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
		ref_cmbBxDut6_BaudRate.getSelectionModel().select(ConstantDutDevSys.DUT_DEFAULT_BAUD_RATE);

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

	public void updateLDUModel() {
		ArrayList<String> ModelList = new ArrayList<String>();
		String ModelName = "";
		ConstantApp MyPropertyObj = new ConstantApp();
		ModelName = ConstantDutDevSys.DUT_EM_MODEL;
		ModelList.add(ModelName);

		// ref_cmbBxQrScannerName.get

		ref_cmbBxDut1_ModelName.getItems().clear();
		ref_cmbBxDut1_ModelName.getItems().addAll(ModelList);
		ref_cmbBxDut1_ModelName.getSelectionModel().select(0);

		ref_cmbBxDut2_ModelName.getItems().clear();
		ref_cmbBxDut2_ModelName.getItems().addAll(ModelList);
		ref_cmbBxDut2_ModelName.getSelectionModel().select(0);

		ref_cmbBxDut3_ModelName.getItems().clear();
		ref_cmbBxDut3_ModelName.getItems().addAll(ModelList);
		ref_cmbBxDut3_ModelName.getSelectionModel().select(0);

		ref_cmbBxDut4_ModelName.getItems().clear();
		ref_cmbBxDut4_ModelName.getItems().addAll(ModelList);
		ref_cmbBxDut4_ModelName.getSelectionModel().select(0);

		ref_cmbBxDut5_ModelName.getItems().clear();
		ref_cmbBxDut5_ModelName.getItems().addAll(ModelList);
		ref_cmbBxDut5_ModelName.getSelectionModel().select(0);

		ref_cmbBxDut6_ModelName.getItems().clear();
		ref_cmbBxDut6_ModelName.getItems().addAll(ModelList);
		ref_cmbBxDut6_ModelName.getSelectionModel().select(0);

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
				ref_cmbBxDut1_PortSelection.getItems().clear();
				ref_cmbBxDut2_PortSelection.getItems().clear();
				ref_cmbBxDut3_PortSelection.getItems().clear();
				ref_cmbBxDut4_PortSelection.getItems().clear();
				ref_cmbBxDut5_PortSelection.getItems().clear();
				ref_cmbBxDut6_PortSelection.getItems().clear();

				// Enumeration ports is now provided above

				while (ports.hasMoreElements()) {
					String curPortName = (String) ports.nextElement();

					if (true) {
						/*
						 * cmbBxPowerSrcPortSelection.getItems().add(curPortName);
						 * cmbBxRefStdPortSelection.getItems().add(curPortName);
						 */
						ref_cmbBxDut1_PortSelection.getItems().add(curPortName);
						ref_cmbBxDut2_PortSelection.getItems().add(curPortName);
						ref_cmbBxDut3_PortSelection.getItems().add(curPortName);
						ref_cmbBxDut4_PortSelection.getItems().add(curPortName);
						ref_cmbBxDut5_PortSelection.getItems().add(curPortName);
						ref_cmbBxDut6_PortSelection.getItems().add(curPortName);
					}
				}

				try {
					ref_cmbBxDut1_PortSelection.getSelectionModel().select(0);
				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-1:" + e.getMessage());
				}

				try {
					ref_cmbBxDut2_PortSelection.getSelectionModel().select(0);
				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-2:" + e.getMessage());
				}
				try {
					ref_cmbBxDut3_PortSelection.getSelectionModel().select(0);
				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-3:" + e.getMessage());
				}
				try {
					ref_cmbBxDut4_PortSelection.getSelectionModel().select(0);
				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-4:" + e.getMessage());
				}
				try {
					ref_cmbBxDut5_PortSelection.getSelectionModel().select(0);
				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-5:" + e.getMessage());
				}
				try {
					ref_cmbBxDut6_PortSelection.getSelectionModel().select(0);
				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-6:" + e.getMessage());
				}

			});
		}).start();
	}

	public void SaveOnClick() {
		String pwr_type = ConstantApp.SOURCE_TYPE_POWER_SOURCE;
		String dut1_type_key = ConstantDutDevSys.DUT_EM_1;
		String dut2_type_key = ConstantDutDevSys.DUT_EM_2;
		String dut3_type_key = ConstantDutDevSys.DUT_EM_3;
		String dut4_type_key = ConstantDutDevSys.DUT_EM_4;
		String dut5_type_key = ConstantDutDevSys.DUT_EM_5;
		String dut6_type_key = ConstantDutDevSys.DUT_EM_6;

		String dut1_model_name = ref_cmbBxDut1_ModelName.getSelectionModel().getSelectedItem();
		String dut2_model_name = ref_cmbBxDut2_ModelName.getSelectionModel().getSelectedItem();
		String dut3_model_name = ref_cmbBxDut3_ModelName.getSelectionModel().getSelectedItem();
		String dut4_model_name = ref_cmbBxDut4_ModelName.getSelectionModel().getSelectedItem();
		String dut5_model_name = ref_cmbBxDut5_ModelName.getSelectionModel().getSelectedItem();
		String dut6_model_name = ref_cmbBxDut6_ModelName.getSelectionModel().getSelectedItem();

		String dut_port_name1 = ref_cmbBxDut1_PortSelection.getSelectionModel().getSelectedItem();
		String dut_port_name2 = ref_cmbBxDut2_PortSelection.getSelectionModel().getSelectedItem();
		String dut_port_name3 = ref_cmbBxDut3_PortSelection.getSelectionModel().getSelectedItem();
		String dut_port_name4 = ref_cmbBxDut4_PortSelection.getSelectionModel().getSelectedItem();
		String dut_port_name5 = ref_cmbBxDut5_PortSelection.getSelectionModel().getSelectedItem();
		String dut_port_name6 = ref_cmbBxDut6_PortSelection.getSelectionModel().getSelectedItem();

		String dut1_baud_rate = ref_cmbBxDut1_BaudRate.getSelectionModel().getSelectedItem().toString();
		String dut2_baud_rate = ref_cmbBxDut2_BaudRate.getSelectionModel().getSelectedItem().toString();
		String dut3_baud_rate = ref_cmbBxDut3_BaudRate.getSelectionModel().getSelectedItem().toString();
		String dut4_baud_rate = ref_cmbBxDut4_BaudRate.getSelectionModel().getSelectedItem().toString();
		String dut5_baud_rate = ref_cmbBxDut5_BaudRate.getSelectionModel().getSelectedItem().toString();
		String dut6_baud_rate = ref_cmbBxDut6_BaudRate.getSelectionModel().getSelectedItem().toString();

		String dut1ClusterName = (String) ref_cmbBxDut1ClusterId.getSelectionModel().getSelectedItem();
		String dut2ClusterName = (String) ref_cmbBxDut2ClusterId.getSelectionModel().getSelectedItem();
		String dut3ClusterName = (String) ref_cmbBxDut3ClusterId.getSelectionModel().getSelectedItem();
		String dut4ClusterName = (String) ref_cmbBxDut4ClusterId.getSelectionModel().getSelectedItem();
		String dut5ClusterName = (String) ref_cmbBxDut5ClusterId.getSelectionModel().getSelectedItem();
		String dut6ClusterName = (String) ref_cmbBxDut6ClusterId.getSelectionModel().getSelectedItem();

		String dut1BayName = (String) ref_cmbBxDut1BayId.getSelectionModel().getSelectedItem();
		String dut2BayName = (String) ref_cmbBxDut2BayId.getSelectionModel().getSelectedItem();
		String dut3BayName = (String) ref_cmbBxDut3BayId.getSelectionModel().getSelectedItem();
		String dut4BayName = (String) ref_cmbBxDut4BayId.getSelectionModel().getSelectedItem();
		String dut5BayName = (String) ref_cmbBxDut5BayId.getSelectionModel().getSelectedItem();
		String dut6BayName = (String) ref_cmbBxDut6BayId.getSelectionModel().getSelectedItem();

		String dut1PositionNo = (String) ref_cmbBxDut1PositionId.getSelectionModel().getSelectedItem();
		String dut2PositionNo = (String) ref_cmbBxDut2PositionId.getSelectionModel().getSelectedItem();
		String dut3PositionNo = (String) ref_cmbBxDut3PositionId.getSelectionModel().getSelectedItem();
		String dut4PositionNo = (String) ref_cmbBxDut4PositionId.getSelectionModel().getSelectedItem();
		String dut5PositionNo = (String) ref_cmbBxDut5PositionId.getSelectionModel().getSelectedItem();
		String dut6PositionNo = (String) ref_cmbBxDut6PositionId.getSelectionModel().getSelectedItem();

		String dut1Cname = ref_txtDut1Cname.getText();// ref_cmbBxDutCname1.getSelectionModel().getSelectedItem();
		String dut2Cname = ref_txtDut2Cname.getText();
		String dut3Cname = ref_txtDut3Cname.getText();
		String dut4Cname = ref_txtDut4Cname.getText();
		String dut5Cname = ref_txtDut5Cname.getText();
		String dut6Cname = ref_txtDut6Cname.getText();

		String deviceId1 = getClusterBayPositionNoDeviceIdMap().get(
				dut1ClusterName + "_" + dut1BayName + "_" + dut1PositionNo);// ref_txtDut1Cname.getText();//ref_cmbBxDutCname1.getSelectionModel().getSelectedItem();
		String deviceId2 = getClusterBayPositionNoDeviceIdMap().get(
				dut2ClusterName + "_" + dut2BayName + "_" + dut2PositionNo);
		String deviceId3 = getClusterBayPositionNoDeviceIdMap().get(
				dut3ClusterName + "_" + dut3BayName + "_" + dut3PositionNo);
		String deviceId4 = getClusterBayPositionNoDeviceIdMap().get(
				dut4ClusterName + "_" + dut4BayName + "_" + dut4PositionNo);
		String deviceId5 = getClusterBayPositionNoDeviceIdMap().get(
				dut5ClusterName + "_" + dut5BayName + "_" + dut5PositionNo);
		String deviceId6 = getClusterBayPositionNoDeviceIdMap().get(
				dut6ClusterName + "_" + dut6BayName + "_" + dut6PositionNo);
		// MySQL_Controller.sp_add_device_settings(1, pwr_type, pwr_model_name,
		// pwr_port_name, pwr_baud_rate);
		// MySQL_Controller.sp_add_device_settings(2, ref_type, ref_model_name,
		// ref_port_name, ref_baud_rate);
		// MySQL_Controller.sp_add_device_settings(3001, ldu1_type, ldu1_model_name,
		// ldu_port_name1, ldu1_baud_rate,qrPortName1);
		// MySQL_Controller.sp_add_device_settings(3002, ldu2_type, ldu2_model_name,
		// ldu_port_name2, ldu2_baud_rate,qrPortName2);

		String clusterId = "";
		String bayId = "";
		String deviceType = ConstantConveyor.DEVICE_TYPE_DUT;
		if ((!dut1ClusterName.startsWith("No-")) &&
				(!dut1BayName.startsWith("No-")) &&
				(!dut1PositionNo.startsWith("No-"))) {
			if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {
				clusterId = getClusterNameIdListMap().get(dut1ClusterName);
				bayId = getClusterBayNameIdMap().get(dut1ClusterName + "_" + dut1BayName);
				DeviceSetting deviceSetting = new DeviceSetting(dut1_type_key, dut1_model_name, dut_port_name1,
						dut1_baud_rate,
						clusterId, dut1ClusterName, bayId, dut1BayName, dut1PositionNo, dut1Cname, deviceId1,
						deviceType);

				MySqlServiceManager.getDeviceSettingService().saveToDb(deviceSetting);
			} else {
				MySQL_Controller.sp_add_device_settings_v2(dut1_type_key, dut1_model_name, dut_port_name1,
						dut1_baud_rate, dut1ClusterName, dut1BayName, dut1PositionNo, dut1Cname, deviceId1);
			}
		}
		if ((!dut2ClusterName.startsWith("No-")) &&
				(!dut2BayName.startsWith("No-")) &&
				(!dut2PositionNo.startsWith("No-"))) {
			if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {
				clusterId = getClusterNameIdListMap().get(dut2ClusterName);
				bayId = getClusterBayNameIdMap().get(dut2ClusterName + "_" + dut2BayName);
				DeviceSetting deviceSetting = new DeviceSetting(dut2_type_key, dut2_model_name, dut_port_name2,
						dut2_baud_rate,
						clusterId, dut2ClusterName, bayId, dut2BayName, dut2PositionNo, dut2Cname, deviceId2,
						deviceType);

				MySqlServiceManager.getDeviceSettingService().saveToDb(deviceSetting);
			} else {
				MySQL_Controller.sp_add_device_settings_v2(dut2_type_key, dut2_model_name, dut_port_name2,
						dut2_baud_rate, dut2ClusterName, dut2BayName, dut2PositionNo, dut2Cname, deviceId2);
			}
		}

		if ((!dut3ClusterName.startsWith("No-")) &&
				(!dut3BayName.startsWith("No-")) &&
				(!dut3PositionNo.startsWith("No-"))) {
			if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {
				clusterId = getClusterNameIdListMap().get(dut3ClusterName);
				bayId = getClusterBayNameIdMap().get(dut3ClusterName + "_" + dut3BayName);
				DeviceSetting deviceSetting = new DeviceSetting(dut3_type_key, dut3_model_name, dut_port_name3,
						dut3_baud_rate,
						clusterId, dut3ClusterName, bayId, dut3BayName, dut3PositionNo, dut3Cname, deviceId3,
						deviceType);

				MySqlServiceManager.getDeviceSettingService().saveToDb(deviceSetting);
			} else {
				MySQL_Controller.sp_add_device_settings_v2(dut3_type_key, dut3_model_name, dut_port_name3,
						dut3_baud_rate, dut3ClusterName, dut3BayName, dut3PositionNo, dut3Cname, deviceId3);
			}
		}
		if ((!dut4ClusterName.startsWith("No-")) &&
				(!dut4BayName.startsWith("No-")) &&
				(!dut4PositionNo.startsWith("No-"))) {
			if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {
				clusterId = getClusterNameIdListMap().get(dut4ClusterName);
				bayId = getClusterBayNameIdMap().get(dut4ClusterName + "_" + dut4BayName);
				DeviceSetting deviceSetting = new DeviceSetting(dut4_type_key, dut4_model_name, dut_port_name4,
						dut4_baud_rate,
						clusterId, dut4ClusterName, bayId, dut4BayName, dut4PositionNo, dut4Cname, deviceId4,
						deviceType);

				MySqlServiceManager.getDeviceSettingService().saveToDb(deviceSetting);
			} else {
				MySQL_Controller.sp_add_device_settings_v2(dut4_type_key, dut4_model_name, dut_port_name4,
						dut4_baud_rate, dut4ClusterName, dut4BayName, dut4PositionNo, dut4Cname, deviceId4);
			}
		}
		if ((!dut5ClusterName.startsWith("No-")) &&
				(!dut5BayName.startsWith("No-")) &&
				(!dut5PositionNo.startsWith("No-"))) {
			if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {
				clusterId = getClusterNameIdListMap().get(dut5ClusterName);
				bayId = getClusterBayNameIdMap().get(dut5ClusterName + "_" + dut5BayName);
				DeviceSetting deviceSetting = new DeviceSetting(dut5_type_key, dut5_model_name, dut_port_name5,
						dut5_baud_rate,
						clusterId, dut5ClusterName, bayId, dut5BayName, dut5PositionNo, dut5Cname, deviceId5,
						deviceType);

				MySqlServiceManager.getDeviceSettingService().saveToDb(deviceSetting);
			} else {
				MySQL_Controller.sp_add_device_settings_v2(dut5_type_key, dut5_model_name, dut_port_name5,
						dut5_baud_rate, dut5ClusterName, dut5BayName, dut5PositionNo, dut5Cname, deviceId5);
			}
		}
		if ((!dut6ClusterName.startsWith("No-")) &&
				(!dut6BayName.startsWith("No-")) &&
				(!dut6PositionNo.startsWith("No-"))) {
			if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {
				clusterId = getClusterNameIdListMap().get(dut6ClusterName);
				bayId = getClusterBayNameIdMap().get(dut6ClusterName + "_" + dut6BayName);
				DeviceSetting deviceSetting = new DeviceSetting(dut6_type_key, dut6_model_name, dut_port_name6,
						dut6_baud_rate,
						clusterId, dut6ClusterName, bayId, dut6BayName, dut6PositionNo, dut6Cname, deviceId6,
						deviceType);

				MySqlServiceManager.getDeviceSettingService().saveToDb(deviceSetting);
			} else {
				MySQL_Controller.sp_add_device_settings_v2(dut6_type_key, dut6_model_name, dut_port_name6,
						dut6_baud_rate, dut6ClusterName, dut6BayName, dut6PositionNo, dut6Cname, deviceId6);
			}
		}

		if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {
			ConveyorDataManager.loadDeviceSettingFromDb();
		}

		WindowManager.InformUser("Saved Successfully", "Saved data successfully", AlertType.INFORMATION);

	}

	public void RefStdValidateSerialCmd() {

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

	public void dut1_ValidateSerialCmd() {
		ApplicationLauncher.logger.debug("dut1_ValidateSerialCmd : Entry");

		String LDU_CommPortID = null;
		String LDUCommBaudRate = null;
		ref_txtValidateDut1_CmdStatus.clear();
		ref_txtDut1ReadData.clear();
		try {
			// serialDM_Obj.commLDU1.searchForPorts();
			LDU_CommPortID = getCurrentLDU_ComPortID1();
			LDUCommBaudRate = getCurrentLDU_ComBaudRate1();
			// boolean status =
			// DisplayDataObj.pwrSrcPortAccessible_V2_1(LDU_CommPortID,LDUCommBaudRate);//false;//serialDM_Obj.LDU1_Init(LDU_CommPortID,LDUCommBaudRate);
			String portCname = ref_txtDut1Cname.getText();
			boolean terminatorMandatory = true;
			SpmDut spManager = new SpmDut(portCname, terminatorMandatory);
			boolean status = spManager.powerSourceComInitV2(LDU_CommPortID, LDUCommBaudRate);
			if (!status) {

				ref_txtValidateDut1_CmdStatus.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);

			} else {
				setPortValidationTurnedON(true);
				// status = serialDM_Obj.lscsLDU1_CheckCom();
				// DisplayDataObj.pwrSrcEnableSerialMonitoring_V2();
				spManager.startSerialRxPhysical_Dut();
				spManager.enableSerialRxPhysical_DutMonitor();

				DutDirector pwrSrcDirector = new DutDirector(spManager);
				Map<String, Object> responseMap = pwrSrcDirector.fetchDutSerialNumber();

				status = (boolean) responseMap.get("status");
				String responseFromDut = "";
				try {
					responseFromDut = (String) responseMap.get("responseData");
					ApplicationLauncher.logger.debug("dut1_ValidateSerialCmd: responseFromDut: " + responseFromDut);
					// qrData =
					// NewlandQRCodeScanner.extractScannedData((String)responseMap.get("responseData"));
					ApplicationLauncher.logger.debug("dut1_ValidateSerialCmd: responseFromDut: " + responseFromDut);
				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger.error("dut1_ValidateSerialCmd: Exception" + e.getMessage());
				}
				if (!status) {
					ref_txtValidateDut1_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
				} else {
					ref_txtValidateDut1_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
					ref_txtDut1ReadData.setText(responseFromDut);
				}
				setPortValidationTurnedON(false);
				// DisplayDataObj.pwrSrcDisconnectPort_V2();
				spManager.disconnectDut();
				// DisplayDataObj.setLDU1_ReadDataFlag(false);
			}
			// serialDM_Obj.DisconnectLDU1();
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("dut1_ValidateSerialCmd: Exception" + e.getMessage());
		}

		ApplicationLauncher.logger.debug("dut1_ValidateSerialCmd : Exit");

	}

	public void dut2_ValidateSerialCmd() {
		ApplicationLauncher.logger.debug("dut2_ValidateSerialCmd : Entry");

		String LDU_CommPortID = null;
		String LDUCommBaudRate = null;
		ref_txtValidateDut2_CmdStatus.clear();
		ref_txtDut2ReadData.clear();
		try {
			// serialDM_Obj.commLDU1.searchForPorts();
			LDU_CommPortID = getCurrentLDU_ComPortID2();
			LDUCommBaudRate = getCurrentLDU_ComBaudRate2();
			String portCname = ref_txtDut1Cname.getText();
			boolean terminatorMandatory = true;
			SpmDut spManager = new SpmDut(portCname, terminatorMandatory);
			boolean status = spManager.powerSourceComInitV2(LDU_CommPortID, LDUCommBaudRate);
			if (!status) {

				ref_txtValidateDut2_CmdStatus.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);

			} else {
				setPortValidationTurnedON(true);
				spManager.startSerialRxPhysical_Dut();
				spManager.enableSerialRxPhysical_DutMonitor();

				DutDirector pwrSrcDirector = new DutDirector(spManager);
				Map<String, Object> responseMap = pwrSrcDirector.fetchDutSerialNumber();

				status = (boolean) responseMap.get("status");
				String responseFromDut = "";
				try {
					responseFromDut = (String) responseMap.get("responseData");
					ApplicationLauncher.logger.debug("dut2_ValidateSerialCmd: responseFromDut: " + responseFromDut);
					// qrData =
					// NewlandQRCodeScanner.extractScannedData((String)responseMap.get("responseData"));
					// ApplicationLauncher.logger.debug("dut2_ValidateSerialCmd: responseFromDut:
					// "+responseFromDut);
				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger.error("dut2_ValidateSerialCmd: Exception" + e.getMessage());
				}
				if (!status) {
					ref_txtValidateDut2_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
				} else {
					ref_txtValidateDut2_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
					ref_txtDut2ReadData.setText(responseFromDut);
				}
				setPortValidationTurnedON(false);
				spManager.disconnectDut();
			}
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("dut2_ValidateSerialCmd: Exception" + e.getMessage());
		}

		ApplicationLauncher.logger.debug("dut2_ValidateSerialCmd : Exit");

	}

	public void dut3_ValidateSerialCmd() {
		ApplicationLauncher.logger.debug("dut3_ValidateSerialCmd : Entry");

		String LDU_CommPortID = null;
		String LDUCommBaudRate = null;
		ref_txtValidateDut3_CmdStatus.clear();
		ref_txtDut3ReadData.clear();
		try {
			// serialDM_Obj.commLDU1.searchForPorts();
			LDU_CommPortID = getCurrentLDU_ComPortID3();
			LDUCommBaudRate = getCurrentLDU_ComBaudRate3();
			String portCname = ref_txtDut1Cname.getText();
			boolean terminatorMandatory = true;
			SpmDut spManager = new SpmDut(portCname, terminatorMandatory);
			boolean status = spManager.powerSourceComInitV2(LDU_CommPortID, LDUCommBaudRate);
			if (!status) {

				ref_txtValidateDut3_CmdStatus.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);

			} else {
				setPortValidationTurnedON(true);
				spManager.startSerialRxPhysical_Dut();
				spManager.enableSerialRxPhysical_DutMonitor();

				DutDirector pwrSrcDirector = new DutDirector(spManager);
				Map<String, Object> responseMap = pwrSrcDirector.fetchDutSerialNumber();

				status = (boolean) responseMap.get("status");
				String responseFromDut = "";
				try {
					responseFromDut = (String) responseMap.get("responseData");
					ApplicationLauncher.logger.debug("dut3_ValidateSerialCmd: responseFromDut: " + responseFromDut);
					// qrData =
					// NewlandQRCodeScanner.extractScannedData((String)responseMap.get("responseData"));
					// ApplicationLauncher.logger.debug("dut3_ValidateSerialCmd: responseFromDut:
					// "+responseFromDut);
				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger.error("dut3_ValidateSerialCmd: Exception" + e.getMessage());
				}
				if (!status) {
					ref_txtValidateDut3_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
				} else {
					ref_txtValidateDut3_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
					ref_txtDut3ReadData.setText(responseFromDut);
				}
				setPortValidationTurnedON(false);
				spManager.disconnectDut();
			}
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("dut3_ValidateSerialCmd: Exception" + e.getMessage());
		}

		ApplicationLauncher.logger.debug("dut3_ValidateSerialCmd : Exit");

	}

	public void dut4_ValidateSerialCmd() {
		ApplicationLauncher.logger.debug("dut4_ValidateSerialCmd : Entry");

		String LDU_CommPortID = null;
		String LDUCommBaudRate = null;
		ref_txtValidateDut4_CmdStatus.clear();
		ref_txtDut4ReadData.clear();
		try {
			// serialDM_Obj.commLDU1.searchForPorts();
			LDU_CommPortID = getCurrentLDU_ComPortID4();
			LDUCommBaudRate = getCurrentLDU_ComBaudRate4();
			String portCname = ref_txtDut1Cname.getText();
			boolean terminatorMandatory = true;
			SpmDut spManager = new SpmDut(portCname, terminatorMandatory);
			boolean status = spManager.powerSourceComInitV2(LDU_CommPortID, LDUCommBaudRate);
			if (!status) {

				ref_txtValidateDut4_CmdStatus.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);

			} else {
				setPortValidationTurnedON(true);
				spManager.startSerialRxPhysical_Dut();
				spManager.enableSerialRxPhysical_DutMonitor();

				DutDirector pwrSrcDirector = new DutDirector(spManager);
				Map<String, Object> responseMap = pwrSrcDirector.fetchDutSerialNumber();

				status = (boolean) responseMap.get("status");
				String responseFromDut = "";
				try {
					responseFromDut = (String) responseMap.get("responseData");
					ApplicationLauncher.logger.debug("dut4_ValidateSerialCmd: responseFromDut: " + responseFromDut);
					// qrData =
					// NewlandQRCodeScanner.extractScannedData((String)responseMap.get("responseData"));
					// ApplicationLauncher.logger.debug("dut4_ValidateSerialCmd: responseFromDut:
					// "+responseFromDut);
				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger.error("dut4_ValidateSerialCmd: Exception" + e.getMessage());
				}
				if (!status) {
					ref_txtValidateDut4_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
				} else {
					ref_txtValidateDut4_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
					ref_txtDut4ReadData.setText(responseFromDut);
				}
				setPortValidationTurnedON(false);
				spManager.disconnectDut();
			}
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("dut4_ValidateSerialCmd: Exception" + e.getMessage());
		}

		ApplicationLauncher.logger.debug("dut4_ValidateSerialCmd : Exit");

	}

	public void dut5_ValidateSerialCmd() {
		ApplicationLauncher.logger.debug("dut5_ValidateSerialCmd : Entry");

		String LDU_CommPortID = null;
		String LDUCommBaudRate = null;
		ref_txtValidateDut5_CmdStatus.clear();
		ref_txtDut5ReadData.clear();
		try {
			// serialDM_Obj.commLDU1.searchForPorts();
			LDU_CommPortID = getCurrentLDU_ComPortID5();
			LDUCommBaudRate = getCurrentLDU_ComBaudRate5();
			String portCname = ref_txtDut1Cname.getText();
			boolean terminatorMandatory = true;
			SpmDut spManager = new SpmDut(portCname, terminatorMandatory);
			boolean status = spManager.powerSourceComInitV2(LDU_CommPortID, LDUCommBaudRate);
			if (!status) {

				ref_txtValidateDut5_CmdStatus.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);

			} else {
				setPortValidationTurnedON(true);
				spManager.startSerialRxPhysical_Dut();
				spManager.enableSerialRxPhysical_DutMonitor();

				DutDirector pwrSrcDirector = new DutDirector(spManager);
				Map<String, Object> responseMap = pwrSrcDirector.fetchDutSerialNumber();

				status = (boolean) responseMap.get("status");
				String responseFromDut = "";
				try {
					responseFromDut = (String) responseMap.get("responseData");
					ApplicationLauncher.logger.debug("dut5_ValidateSerialCmd: responseFromDut: " + responseFromDut);
					// qrData =
					// NewlandQRCodeScanner.extractScannedData((String)responseMap.get("responseData"));
					// ApplicationLauncher.logger.debug("dut5_ValidateSerialCmd: responseFromDut:
					// "+responseFromDut);
				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger.error("dut5_ValidateSerialCmd: Exception" + e.getMessage());
				}
				if (!status) {
					ref_txtValidateDut5_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
				} else {
					ref_txtValidateDut5_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
					ref_txtDut5ReadData.setText(responseFromDut);
				}
				setPortValidationTurnedON(false);
				spManager.disconnectDut();
			}
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("dut5_ValidateSerialCmd: Exception" + e.getMessage());
		}

		ApplicationLauncher.logger.debug("dut5_ValidateSerialCmd : Exit");

	}

	public void dut6_ValidateSerialCmd() {
		ApplicationLauncher.logger.debug("dut6_ValidateSerialCmd : Entry");

		String LDU_CommPortID = null;
		String LDUCommBaudRate = null;
		ref_txtValidateDut6_CmdStatus.clear();
		ref_txtDut6ReadData.clear();
		try {
			// serialDM_Obj.commLDU1.searchForPorts();
			LDU_CommPortID = getCurrentLDU_ComPortID6();
			LDUCommBaudRate = getCurrentLDU_ComBaudRate6();
			String portCname = ref_txtDut1Cname.getText();
			boolean terminatorMandatory = true;
			SpmDut spManager = new SpmDut(portCname, terminatorMandatory);
			boolean status = spManager.powerSourceComInitV2(LDU_CommPortID, LDUCommBaudRate);
			if (!status) {

				ref_txtValidateDut6_CmdStatus.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);

			} else {
				setPortValidationTurnedON(true);
				spManager.startSerialRxPhysical_Dut();
				spManager.enableSerialRxPhysical_DutMonitor();

				DutDirector pwrSrcDirector = new DutDirector(spManager);
				Map<String, Object> responseMap = pwrSrcDirector.fetchDutSerialNumber();

				status = (boolean) responseMap.get("status");
				String responseFromDut = "";
				try {
					responseFromDut = (String) responseMap.get("responseData");
					ApplicationLauncher.logger.debug("dut6_ValidateSerialCmd: responseFromDut: " + responseFromDut);
					// qrData =
					// NewlandQRCodeScanner.extractScannedData((String)responseMap.get("responseData"));
					// ApplicationLauncher.logger.debug("dut6_ValidateSerialCmd: responseFromDut:
					// "+responseFromDut);
				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger.error("dut6_ValidateSerialCmd: Exception" + e.getMessage());
				}
				if (!status) {
					ref_txtValidateDut6_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
				} else {
					ref_txtValidateDut6_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
					ref_txtDut6ReadData.setText(responseFromDut);
				}
				setPortValidationTurnedON(false);
				spManager.disconnectDut();
			}
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("dut6_ValidateSerialCmd: Exception" + e.getMessage());
		}

		ApplicationLauncher.logger.debug("dut6_ValidateSerialCmd : Exit");

	}

	private String getCurrentLDU_ComBaudRate1() {

		return ref_cmbBxDut1_BaudRate.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentLDU_ComPortID1() {

		return ref_cmbBxDut1_PortSelection.getSelectionModel().getSelectedItem();
	}

	private String getCurrentLDU_ComBaudRate2() {

		return ref_cmbBxDut2_BaudRate.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentLDU_ComPortID2() {

		return ref_cmbBxDut2_PortSelection.getSelectionModel().getSelectedItem();
	}

	private String getCurrentLDU_ComBaudRate3() {

		return ref_cmbBxDut3_BaudRate.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentLDU_ComPortID3() {

		return ref_cmbBxDut3_PortSelection.getSelectionModel().getSelectedItem();
	}

	private String getCurrentLDU_ComBaudRate4() {

		return ref_cmbBxDut4_BaudRate.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentLDU_ComPortID4() {

		return ref_cmbBxDut4_PortSelection.getSelectionModel().getSelectedItem();
	}

	private String getCurrentLDU_ComBaudRate5() {

		return ref_cmbBxDut5_BaudRate.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentLDU_ComPortID5() {

		return ref_cmbBxDut5_PortSelection.getSelectionModel().getSelectedItem();
	}

	private String getCurrentLDU_ComBaudRate6() {

		return ref_cmbBxDut6_BaudRate.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentLDU_ComPortID6() {

		return ref_cmbBxDut6_PortSelection.getSelectionModel().getSelectedItem();
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
	public void dut1_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("dut1_ValidateSerialCmdTrigger: Invoked:");
		dut1_ValidateTimer = new Timer();
		dut1_ValidateTimer.schedule(new Dut1_ValidateTimerTask(), 100);// 1000);

	}

	@FXML
	public void dut2_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("dut2_ValidateSerialCmdTrigger: Invoked:");
		dut2_ValidateTimer = new Timer();
		dut2_ValidateTimer.schedule(new Dut2_ValidateTimerTask(), 200);// 2000);
	}

	@FXML
	public void dut3_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("dut3_ValidateSerialCmdTrigger: Invoked:");
		dut3_ValidateTimer = new Timer();
		dut3_ValidateTimer.schedule(new Dut3_ValidateTimerTask(), 200);
		// LDU3_ValidateTimer.schedule(new LDU3_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void dut4_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("dut4_ValidateSerialCmdTrigger: Invoked:");
		dut4_ValidateTimer = new Timer();
		dut4_ValidateTimer.schedule(new Dut4_ValidateTimerTask(), 200);
		// LDU4_ValidateTimer.schedule(new LDU4_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void dut5_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("dut5_ValidateSerialCmdTrigger: Invoked:");
		dut5_ValidateTimer = new Timer();
		dut5_ValidateTimer.schedule(new Dut5_ValidateTimerTask(), 200);
		// LDU5_ValidateTimer.schedule(new LDU5_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void dut6_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("dut6_ValidateSerialCmdTrigger: Invoked:");
		dut6_ValidateTimer = new Timer();
		dut6_ValidateTimer.schedule(new Dut6_ValidateTimerTask(), 200);
		// LDU6_ValidateTimer.schedule(new LDU6_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU7_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU7_ValidateSerialCmdTrigger: Invoked:");
		LDU7_ValidateTimer = new Timer();
		// LDU7_ValidateTimer.schedule(new LDU7_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU8_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU8_ValidateSerialCmdTrigger: Invoked:");
		LDU8_ValidateTimer = new Timer();
		// LDU8_ValidateTimer.schedule(new LDU8_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU9_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU9_ValidateSerialCmdTrigger: Invoked:");
		LDU9_ValidateTimer = new Timer();
		// LDU9_ValidateTimer.schedule(new LDU9_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU10_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU10_ValidateSerialCmdTrigger: Invoked:");
		LDU10_ValidateTimer = new Timer();
		// LDU10_ValidateTimer.schedule(new LDU10_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU11_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU11_ValidateSerialCmdTrigger: Invoked:");
		LDU11_ValidateTimer = new Timer();
		// LDU11_ValidateTimer.schedule(new LDU11_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU12_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU12_ValidateSerialCmdTrigger: Invoked:");
		LDU12_ValidateTimer = new Timer();
		// LDU12_ValidateTimer.schedule(new LDU12_ValidateTimerTask(),100);// 1000);
	}

	@FXML
	public void LDU13_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("LDU13_ValidateSerialCmdTrigger: Invoked:");
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

	class Dut1_ValidateTimerTask extends TimerTask {
		public void run() {
			ref_btnValidateDut1_Cmd.setDisable(true);
			WindowManager.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("Dut1_ValidateTimerTask: WAIT");
			try {
				/*
				 * if(ProcalFeatureEnable.CCUBE_LDU_CONNECTED){
				 * LDU_ValidateSerialCmd();
				 * } else if (ProcalFeatureEnable.LSCS_LDU_CONNECTED){
				 * lscsLDU1_ValidateSerialCmd();
				 * }
				 */
				dut1_ValidateSerialCmd();

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger.error("Dut1_ValidateTimerTask: Exception:" + e.getMessage());
			}
			dut1_ValidateTimer.cancel();
			WindowManager.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("Dut1_ValidateTimerTask: DEFAULT");
			ref_btnValidateDut1_Cmd.setDisable(false);
		}
	}

	class Dut2_ValidateTimerTask extends TimerTask {
		public void run() {
			ref_btnValidateDut2_Cmd.setDisable(true);
			WindowManager.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("Dut2_ValidateTimerTask: WAIT");
			try {
				dut2_ValidateSerialCmd();

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger.error("Dut2_ValidateTimerTask: Exception:" + e.getMessage());
			}
			dut2_ValidateTimer.cancel();
			WindowManager.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("Dut2_ValidateTimerTask: DEFAULT");
			ref_btnValidateDut2_Cmd.setDisable(false);
		}
	}

	class Dut3_ValidateTimerTask extends TimerTask {
		public void run() {
			ref_btnValidateDut3_Cmd.setDisable(true);
			WindowManager.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("Dut3_ValidateTimerTask: WAIT");
			try {
				dut3_ValidateSerialCmd();

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger.error("Dut3_ValidateTimerTask: Exception:" + e.getMessage());
			}
			dut3_ValidateTimer.cancel();
			WindowManager.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("Dut3_ValidateTimerTask: DEFAULT");
			ref_btnValidateDut3_Cmd.setDisable(false);
		}
	}

	class Dut4_ValidateTimerTask extends TimerTask {
		public void run() {
			ref_btnValidateDut4_Cmd.setDisable(true);
			WindowManager.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("Dut4_ValidateTimerTask: WAIT");
			try {
				dut4_ValidateSerialCmd();

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger.error("Dut4_ValidateTimerTask: Exception:" + e.getMessage());
			}
			dut4_ValidateTimer.cancel();
			WindowManager.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("Dut4_ValidateTimerTask: DEFAULT");
			ref_btnValidateDut4_Cmd.setDisable(false);
		}
	}

	class Dut5_ValidateTimerTask extends TimerTask {
		public void run() {
			ref_btnValidateDut5_Cmd.setDisable(true);
			WindowManager.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("Dut5_ValidateTimerTask: WAIT");
			try {
				dut5_ValidateSerialCmd();

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger.error("Dut5_ValidateTimerTask: Exception:" + e.getMessage());
			}
			dut5_ValidateTimer.cancel();
			WindowManager.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("Dut5_ValidateTimerTask: DEFAULT");
			ref_btnValidateDut5_Cmd.setDisable(false);
		}
	}

	class Dut6_ValidateTimerTask extends TimerTask {
		public void run() {
			ref_btnValidateDut6_Cmd.setDisable(true);
			WindowManager.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("Dut6_ValidateTimerTask: WAIT");
			try {
				dut6_ValidateSerialCmd();

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger.error("Dut6_ValidateTimerTask: Exception:" + e.getMessage());
			}
			dut6_ValidateTimer.cancel();
			WindowManager.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("Dut6_ValidateTimerTask: DEFAULT");
			ref_btnValidateDut6_Cmd.setDisable(false);
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

	public static TerminalBayConfigModel getBayConfigModel() {
		return bayConfigModel;
	}

	public static void setBayConfigModel(TerminalBayConfigModel bayConfigModel) {
		DutPortSetupController.bayConfigModel = bayConfigModel;
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

	@FXML
	public void cmbBxDut1BaySelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxBaySelectionOnChange: Entry");
		dut1BaySelectionOnChangeTimer = new Timer();
		dut1BaySelectionOnChangeTimer.schedule(new Dut1BaySelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxDut3BaySelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxDut3BaySelectionOnChange: Entry");
		dut3BaySelectionOnChangeTimer = new Timer();
		dut3BaySelectionOnChangeTimer.schedule(new Dut3BaySelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxDut4BaySelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxDut4BaySelectionOnChange: Entry");
		dut4BaySelectionOnChangeTimer = new Timer();
		dut4BaySelectionOnChangeTimer.schedule(new Dut4BaySelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxDut5BaySelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxDut5BaySelectionOnChange: Entry");
		dut5BaySelectionOnChangeTimer = new Timer();
		dut5BaySelectionOnChangeTimer.schedule(new Dut5BaySelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxDut6BaySelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxDut6BaySelectionOnChange: Entry");
		dut6BaySelectionOnChangeTimer = new Timer();
		dut6BaySelectionOnChangeTimer.schedule(new Dut6BaySelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxDut1ClusterSelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxClusterSelectionOnChange: Entry");
		dut1ClusterSelectionOnChangeTimer = new Timer();
		dut1ClusterSelectionOnChangeTimer.schedule(new Dut1ClusterSelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxDut3ClusterSelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxDut3ClusterSelectionOnChange: Entry");
		dut3ClusterSelectionOnChangeTimer = new Timer();
		dut3ClusterSelectionOnChangeTimer.schedule(new Dut3ClusterSelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxDut4ClusterSelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxDut4ClusterSelectionOnChange: Entry");
		dut4ClusterSelectionOnChangeTimer = new Timer();
		dut4ClusterSelectionOnChangeTimer.schedule(new Dut4ClusterSelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxDut5ClusterSelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxDut5ClusterSelectionOnChange: Entry");
		dut5ClusterSelectionOnChangeTimer = new Timer();
		dut5ClusterSelectionOnChangeTimer.schedule(new Dut5ClusterSelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxDut6ClusterSelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxDut6ClusterSelectionOnChange: Entry");
		dut6ClusterSelectionOnChangeTimer = new Timer();
		dut6ClusterSelectionOnChangeTimer.schedule(new Dut6ClusterSelectionOnChangeTask(), 10);
	}

	// ref_cmbBxDutClusterId1;
	// ref_cmbBxDutBayId1;

	class Dut1ClusterSelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxDut1ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxDut1BayId.getItems().clear();
					if (getClusterBayNameListMap().containsKey(selectedClusterName)) {
						ref_cmbBxDut1BayId.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));
					}
					ref_cmbBxDut1BayId.getSelectionModel().select(0);

				}
			});
			dut1ClusterSelectionOnChangeTimer.cancel();

		}
	}

	class Dut3ClusterSelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxDut3ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxDut3BayId.getItems().clear();
					if (getClusterBayNameListMap().containsKey(selectedClusterName)) {
						ref_cmbBxDut3BayId.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));
					}
					ref_cmbBxDut3BayId.getSelectionModel().select(0);

				}
			});
			dut3ClusterSelectionOnChangeTimer.cancel();

		}
	}

	class Dut4ClusterSelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxDut4ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxDut4BayId.getItems().clear();
					if (getClusterBayNameListMap().containsKey(selectedClusterName)) {
						ref_cmbBxDut4BayId.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));
					}
					ref_cmbBxDut4BayId.getSelectionModel().select(0);

				}
			});
			dut4ClusterSelectionOnChangeTimer.cancel();

		}
	}

	class Dut5ClusterSelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxDut5ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxDut5BayId.getItems().clear();
					if (getClusterBayNameListMap().containsKey(selectedClusterName)) {
						ref_cmbBxDut5BayId.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));
					}
					ref_cmbBxDut5BayId.getSelectionModel().select(0);

				}
			});
			dut5ClusterSelectionOnChangeTimer.cancel();

		}
	}

	class Dut6ClusterSelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxDut6ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxDut6BayId.getItems().clear();
					if (getClusterBayNameListMap().containsKey(selectedClusterName)) {
						ref_cmbBxDut6BayId.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));
					}
					ref_cmbBxDut6BayId.getSelectionModel().select(0);

				}
			});
			dut6ClusterSelectionOnChangeTimer.cancel();

		}
	}

	class Dut1BaySelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {

				String selectedClusterName = (String) ref_cmbBxDut1ClusterId.getSelectionModel().getSelectedItem();
				String selectedBayName = (String) ref_cmbBxDut1BayId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxDut1PositionId.getItems().clear();
					if (getClusterBayNamePositionListMap().containsKey(selectedClusterName + "_" + selectedBayName)) {
						ref_cmbBxDut1PositionId.getItems().addAll(
								getClusterBayNamePositionListMap().get(selectedClusterName + "_" + selectedBayName));
						ref_cmbBxDut1PositionId.getSelectionModel().select(0);
						String selectionPositionNo = (String) ref_cmbBxDut1PositionId.getSelectionModel()
								.getSelectedItem();
						if (getClusterBayPositionNoCnameMap()
								.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo)) {
							ref_txtDut1Cname.setText(getClusterBayPositionNoCnameMap()
									.get(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo));
						}
					} else {
						ref_txtDut1Cname.setText("");
					}
				}
			});
			dut1BaySelectionOnChangeTimer.cancel();
		}
	}

	class Dut3BaySelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {

				String selectedClusterName = (String) ref_cmbBxDut3ClusterId.getSelectionModel().getSelectedItem();
				String selectedBayName = (String) ref_cmbBxDut3BayId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxDut3PositionId.getItems().clear();
					if (getClusterBayNamePositionListMap().containsKey(selectedClusterName + "_" + selectedBayName)) {
						ref_cmbBxDut3PositionId.getItems().addAll(
								getClusterBayNamePositionListMap().get(selectedClusterName + "_" + selectedBayName));
						ref_cmbBxDut3PositionId.getSelectionModel().select(0);
						String selectionPositionNo = (String) ref_cmbBxDut3PositionId.getSelectionModel()
								.getSelectedItem();
						if (getClusterBayPositionNoCnameMap()
								.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo)) {
							ref_txtDut3Cname.setText(getClusterBayPositionNoCnameMap()
									.get(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo));
						}
					} else {
						ref_txtDut3Cname.setText("");
					}
				}
			});
			dut3BaySelectionOnChangeTimer.cancel();
		}
	}

	class Dut4BaySelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {

				String selectedClusterName = (String) ref_cmbBxDut4ClusterId.getSelectionModel().getSelectedItem();
				String selectedBayName = (String) ref_cmbBxDut4BayId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxDut4PositionId.getItems().clear();
					if (getClusterBayNamePositionListMap().containsKey(selectedClusterName + "_" + selectedBayName)) {
						ref_cmbBxDut4PositionId.getItems().addAll(
								getClusterBayNamePositionListMap().get(selectedClusterName + "_" + selectedBayName));
						ref_cmbBxDut4PositionId.getSelectionModel().select(0);
						String selectionPositionNo = (String) ref_cmbBxDut4PositionId.getSelectionModel()
								.getSelectedItem();
						if (getClusterBayPositionNoCnameMap()
								.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo)) {
							ref_txtDut4Cname.setText(getClusterBayPositionNoCnameMap()
									.get(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo));
						}
					} else {
						ref_txtDut4Cname.setText("");
					}
				}
			});
			dut4BaySelectionOnChangeTimer.cancel();
		}
	}

	class Dut5BaySelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {

				String selectedClusterName = (String) ref_cmbBxDut5ClusterId.getSelectionModel().getSelectedItem();
				String selectedBayName = (String) ref_cmbBxDut5BayId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxDut5PositionId.getItems().clear();
					if (getClusterBayNamePositionListMap().containsKey(selectedClusterName + "_" + selectedBayName)) {
						ref_cmbBxDut5PositionId.getItems().addAll(
								getClusterBayNamePositionListMap().get(selectedClusterName + "_" + selectedBayName));
						ref_cmbBxDut5PositionId.getSelectionModel().select(0);
						String selectionPositionNo = (String) ref_cmbBxDut5PositionId.getSelectionModel()
								.getSelectedItem();
						if (getClusterBayPositionNoCnameMap()
								.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo)) {
							ref_txtDut5Cname.setText(getClusterBayPositionNoCnameMap()
									.get(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo));
						}
					} else {
						ref_txtDut5Cname.setText("");
					}
				}
			});
			dut5BaySelectionOnChangeTimer.cancel();
		}
	}

	class Dut6BaySelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {

				String selectedClusterName = (String) ref_cmbBxDut6ClusterId.getSelectionModel().getSelectedItem();
				String selectedBayName = (String) ref_cmbBxDut6BayId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxDut6PositionId.getItems().clear();
					if (getClusterBayNamePositionListMap().containsKey(selectedClusterName + "_" + selectedBayName)) {
						ref_cmbBxDut6PositionId.getItems().addAll(
								getClusterBayNamePositionListMap().get(selectedClusterName + "_" + selectedBayName));
						ref_cmbBxDut6PositionId.getSelectionModel().select(0);
						String selectionPositionNo = (String) ref_cmbBxDut6PositionId.getSelectionModel()
								.getSelectedItem();
						if (getClusterBayPositionNoCnameMap()
								.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo)) {
							ref_txtDut6Cname.setText(getClusterBayPositionNoCnameMap()
									.get(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo));
						}
					} else {
						ref_txtDut6Cname.setText("");
					}
				}
			});
			dut6BaySelectionOnChangeTimer.cancel();
		}
	}

	@FXML
	public void cmbBxDut1PositionIdOnChange() {
		ApplicationLauncher.logger.debug("cmbBxDut1PositionIdOnChange: Entry");
		dut1PositionIdOnChangeTimer = new Timer();
		dut1PositionIdOnChangeTimer.schedule(new Dut1PositionIdOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxDut3PositionIdOnChange() {
		ApplicationLauncher.logger.debug("cmbBxDut3PositionIdOnChange: Entry");
		dut3PositionIdOnChangeTimer = new Timer();
		dut3PositionIdOnChangeTimer.schedule(new Dut3PositionIdOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxDut4PositionIdOnChange() {
		ApplicationLauncher.logger.debug("cmbBxDut4PositionIdOnChange: Entry");
		dut4PositionIdOnChangeTimer = new Timer();
		dut4PositionIdOnChangeTimer.schedule(new Dut4PositionIdOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxDut5PositionIdOnChange() {
		ApplicationLauncher.logger.debug("cmbBxDut5PositionIdOnChange: Entry");
		dut5PositionIdOnChangeTimer = new Timer();
		dut5PositionIdOnChangeTimer.schedule(new Dut5PositionIdOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxDut6PositionIdOnChange() {
		ApplicationLauncher.logger.debug("cmbBxDut6PositionIdOnChange: Entry");
		dut6PositionIdOnChangeTimer = new Timer();
		dut6PositionIdOnChangeTimer.schedule(new Dut6PositionIdOnChangeTask(), 10);
	}

	// ref_cmbBxDutClusterId1;
	// ref_cmbBxDutBayId1;

	class Dut1PositionIdOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxDut1ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {
					/*
					 * ref_tbViewOutputPortData.getItems().clear();
					 * ref_tbViewInputPortData.getItems().clear();
					 */
					// ref_cmbBxDutBayId1.getItems().clear();
					/*
					 * ref_txtClusterIpAddress.setText("");
					 * ref_txtClusterPortNo.setText("");
					 */
					String selectedBayName = (String) ref_cmbBxDut1BayId.getSelectionModel().getSelectedItem();
					String selectedPositionNo = (String) cmbBxDut1PositionId.getSelectionModel().getSelectedItem();
					if (getClusterBayPositionNoCnameMap()
							.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo)) {
						ref_txtDut1Cname.setText(getClusterBayPositionNoCnameMap()
								.get(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo));
					}

				}
			});
			dut1PositionIdOnChangeTimer.cancel();

		}
	}

	class Dut3PositionIdOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxDut3ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					String selectedBayName = (String) ref_cmbBxDut3BayId.getSelectionModel().getSelectedItem();
					String selectedPositionNo = (String) cmbBxDut3PositionId.getSelectionModel().getSelectedItem();
					if (getClusterBayPositionNoCnameMap()
							.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo)) {
						ref_txtDut3Cname.setText(getClusterBayPositionNoCnameMap()
								.get(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo));
					}

				}
			});
			dut3PositionIdOnChangeTimer.cancel();

		}
	}

	class Dut4PositionIdOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxDut4ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					String selectedBayName = (String) ref_cmbBxDut4BayId.getSelectionModel().getSelectedItem();
					String selectedPositionNo = (String) cmbBxDut4PositionId.getSelectionModel().getSelectedItem();
					if (getClusterBayPositionNoCnameMap()
							.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo)) {
						ref_txtDut4Cname.setText(getClusterBayPositionNoCnameMap()
								.get(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo));
					}

				}
			});
			dut4PositionIdOnChangeTimer.cancel();

		}
	}

	class Dut5PositionIdOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxDut5ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					String selectedBayName = (String) ref_cmbBxDut5BayId.getSelectionModel().getSelectedItem();
					String selectedPositionNo = (String) cmbBxDut5PositionId.getSelectionModel().getSelectedItem();
					if (getClusterBayPositionNoCnameMap()
							.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo)) {
						ref_txtDut5Cname.setText(getClusterBayPositionNoCnameMap()
								.get(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo));
					}

				}
			});
			dut5PositionIdOnChangeTimer.cancel();

		}
	}

	class Dut6PositionIdOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxDut6ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					String selectedBayName = (String) ref_cmbBxDut6BayId.getSelectionModel().getSelectedItem();
					String selectedPositionNo = (String) cmbBxDut6PositionId.getSelectionModel().getSelectedItem();
					if (getClusterBayPositionNoCnameMap()
							.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo)) {
						ref_txtDut6Cname.setText(getClusterBayPositionNoCnameMap()
								.get(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo));
					}

				}
			});
			dut6PositionIdOnChangeTimer.cancel();

		}
	}

	@FXML
	public void cmbBxDut2BaySelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxBaySelectionOnChange: Entry");
		dut2BaySelectionOnChangeTimer = new Timer();
		dut2BaySelectionOnChangeTimer.schedule(new Dut2BaySelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxDut2ClusterSelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxClusterSelectionOnChange: Entry");
		dut2ClusterSelectionOnChangeTimer = new Timer();
		dut2ClusterSelectionOnChangeTimer.schedule(new Dut2ClusterSelectionOnChangeTask(), 10);
	}

	// ref_cmbBxDutClusterId1;
	// ref_cmbBxDutBayId1;

	class Dut2ClusterSelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxDut2ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {
					/*
					 * ref_tbViewOutputPortData.getItems().clear();
					 * ref_tbViewInputPortData.getItems().clear();
					 */
					ref_cmbBxDut2BayId.getItems().clear();
					/*
					 * ref_txtClusterIpAddress.setText("");
					 * ref_txtClusterPortNo.setText("");
					 */
					if (getClusterBayNameListMap().containsKey(selectedClusterName)) {
						ref_cmbBxDut2BayId.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));
					}
					ref_cmbBxDut2BayId.getSelectionModel().select(0);

				}
			});
			dut2ClusterSelectionOnChangeTimer.cancel();

		}
	}

	class Dut2BaySelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {

				String selectedClusterName = (String) ref_cmbBxDut2ClusterId.getSelectionModel().getSelectedItem();
				String selectedBayName = (String) ref_cmbBxDut2BayId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {
					// ref_tbViewOutputPortData.getItems().clear();
					// ref_tbViewInputPortData.getItems().clear();
					ref_cmbBxDut2PositionId.getItems().clear();
					if (getClusterBayNamePositionListMap().containsKey(selectedClusterName + "_" + selectedBayName)) {
						ref_cmbBxDut2PositionId.getItems().addAll(
								getClusterBayNamePositionListMap().get(selectedClusterName + "_" + selectedBayName));
						ref_cmbBxDut2PositionId.getSelectionModel().select(0);
						String selectionPositionNo = (String) ref_cmbBxDut2PositionId.getSelectionModel()
								.getSelectedItem();
						if (getClusterBayPositionNoCnameMap()
								.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo)) {
							ref_txtDut2Cname.setText(getClusterBayPositionNoCnameMap()
									.get(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo));
						}
					} else {
						ref_txtDut2Cname.setText("");
					}
					// if(getClusterBayNameListMap().containsKey(selectedClusterName)){
					// ref_cmbBxDutPositionId1.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));
					// }
					// ref_cmbBxDutPositionId1.getSelectionModel().select(0);

				}
			});
			dut2BaySelectionOnChangeTimer.cancel();

		}
	}

	@FXML
	public void cmbBxDut2PositionIdOnChange() {
		ApplicationLauncher.logger.debug("cmbBxDut2PositionIdOnChange: Entry");
		dut2PositionIdOnChangeTimer = new Timer();
		dut2PositionIdOnChangeTimer.schedule(new Dut2PositionIdOnChangeTask(), 10);
	}

	// ref_cmbBxDutClusterId1;
	// ref_cmbBxDutBayId1;

	class Dut2PositionIdOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxDut2ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {
					/*
					 * ref_tbViewOutputPortData.getItems().clear();
					 * ref_tbViewInputPortData.getItems().clear();
					 */
					// ref_cmbBxDutBayId1.getItems().clear();
					/*
					 * ref_txtClusterIpAddress.setText("");
					 * ref_txtClusterPortNo.setText("");
					 */
					String selectedBayName = (String) ref_cmbBxDut2BayId.getSelectionModel().getSelectedItem();
					String selectedPositionNo = (String) cmbBxDut2PositionId.getSelectionModel().getSelectedItem();
					if (getClusterBayPositionNoCnameMap()
							.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo)) {
						ref_txtDut2Cname.setText(getClusterBayPositionNoCnameMap()
								.get(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo));
					}

				}
			});
			dut2PositionIdOnChangeTimer.cancel();

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

	public Map<String, String> getClusterBayPositionNoDeviceIdMap() {
		return clusterBayPositionNoDeviceIdMap;
	}

	public void setClusterBayPositionNoDeviceIdMap(Map<String, String> clusterBayPositionNoDeviceIdMap) {
		this.clusterBayPositionNoDeviceIdMap = clusterBayPositionNoDeviceIdMap;
	}

}
