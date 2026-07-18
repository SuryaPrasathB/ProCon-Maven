package com.tasnetwork.calibration.conveyor.dashboard;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class PalletController {

	/*
	 * private Timeline blinkTimelinePalletsExistInQueue;
	 * private Timeline blinkTimelineEntryStopperOpen;
	 * private Timeline blinkTimelineExitStopperOpen;
	 * private Timeline blinkTimelineAllPalletsExistInBay;
	 * private Timeline blinkTimelineTargetBayAllPalletsFree;
	 */

	public static final Boolean CLOSE = true;
	public static final Boolean OPEN = false;
	/*
	 * public enum BayActionType {
	 * FINGERTIP_ENGAGE, FINGERTIP_DISENGAGE,
	 * BLOCK_BAY_ENTRY, BLOCK_BAY_EXIT,
	 * RELEASE_METER_FROM_BAY
	 * //, LATCH, DELATCH
	 * }
	 */

	public enum BayActionType {
		FINGERTIP_ENGAGE("Engage"),
		FINGERTIP_DISENGAGE("Disengage"),
		SOURCE_START("Start Source"),
		CURRENT_STOP("Current Stop"),
		SOURCE_STOP("Stop Source"),
		DIVERTER_UP("Diverter Up"),
		DIVERTER_DOWN("Diverter Down"),
		MAIN_CT("Make Main CT"),
		NEUTRAL_CT("Make Neutral CT"),
		BLOCK_BAY_ENTRY("Block Entry"),
		UNBLOCK_BAY_ENTRY("Free Entry"),
		BLOCK_BAY_EXIT("Block Exit"),
		UNBLOCK_BAY_EXIT("Free Exit"),
		BLOCK_BAY_EXIT2("Block Post Exit"),
		RELEASE_METER_FROM_BAY("Release Meter"),
		PALLETS_CLEARED("Pallets Cleared"),
		// PALLETS_BLOCK("Pallets Block"),
		NO_ENTRY_ACTIVE("On"),
		NO_ENTRY_INACTIVE("Off"),
		HALT_PALLET_ACTIVE("On"),
		HALT_PALLET_INACTIVE("Off"),
		REFRESH("Refresh");
		// LAST_PALLET_DISPLAY("Last Pallet");

		private final String displayName;

		BayActionType(String displayName) {
			this.displayName = displayName;
		}

		@Override
		public String toString() {
			return displayName;
		}
	}

	private String bayTypeKey = ""; // For example: "FunctionalTest", "Calibration", etc.
	// private ContextMenu contextMenu;

	private CommonContextMenu commonContextMenu;

	BayUtils bayUtils = new BayUtils();

	private Consumer<PalletController> onMoveToNextBayHandler;

	@FXML
	private HBox hBoxTop;
	@FXML
	private HBox hBoxBottom;
	@FXML
	private ProgressBar pBarPalletExecution;

	@FXML
	private Text palletName;

	@FXML
	private AnchorPane palletRoot; // Root container from FXML

	// @FXML
	// private Rectangle rect1, rect2, rect3, rect4, rect5, rect6;

	@FXML
	private TextField serial1;
	@FXML
	private TextField serial2;
	@FXML
	private TextField serial3;
	@FXML
	private TextField serial4;
	@FXML
	private TextField serial5;
	@FXML
	private TextField serial6;

	@FXML
	private Rectangle meter1;
	@FXML
	private Rectangle meter2;
	@FXML
	private Rectangle meter3;
	@FXML
	private Rectangle meter4;
	@FXML
	private Rectangle meter5;
	@FXML
	private Rectangle meter6;

	@FXML
	private Text errorCode1;
	@FXML
	private Text errorCode2;
	@FXML
	private Text errorCode3;
	@FXML
	private Text errorCode4;
	@FXML
	private Text errorCode5;
	@FXML
	private Text errorCode6;

	/*
	 * @FXML private Rectangle rectEntryStopperOpen;
	 * 
	 * @FXML private Rectangle rectExitStopperOpen;
	 * 
	 * @FXML private Rectangle rectAllPalletsExistInBay;
	 * 
	 * @FXML private Rectangle rectPalletsExistInQueue;
	 * 
	 * @FXML private Rectangle rectAllPalletsExistInTargetBay;
	 */

	/*
	 * static private Rectangle ref_rectEntryStopperOpen;
	 * static private Rectangle ref_rectExitStopperOpen;
	 * static private Rectangle ref_rectAllPalletsExistInBay;
	 * static private Rectangle ref_rectPalletsExistInQueue;
	 * static private Rectangle ref_rectTargetBayAllPalletsFree;
	 */

	private List<Consumer<String>> serialSetters;

	private BayActionHandler bayActionHandler;

	public PalletController() {
		// Initialize with default bay type, will be updated in initialize()
		this.bayActionHandler = new BayActionHandler("");
	}

	@FXML
	public void initialize() {
		refInitAssignment();
		// toolTipInit();
		// Update bayActionHandler with the actual bay type
		/*
		 * if (this.bayActionHandler != null) {
		 * // If BayActionHandler supports updating bay type
		 * this.bayActionHandler.setBayTypeKey(bayTypeKey);
		 * } else {
		 * // Recreate if needed (though constructor should prevent this)
		 * this.bayActionHandler = new BayActionHandler(bayTypeKey);
		 * }
		 */
		serialSetters = Arrays.asList(
				this::setSerial1,
				this::setSerial2,
				this::setSerial3,
				this::setSerial4,
				this::setSerial5,
				this::setSerial6);

		// createContextMenu();

		// Initialize common context menu
		commonContextMenu = new CommonContextMenu(bayTypeKey) {
			@Override
			protected void handleAction(BayActionType actionType) {
				// handleActionByBayType(actionType);
				bayActionHandler.handleActionByBayType(actionType);
			}
		};
		commonContextMenu.attachToNode(palletRoot);

		// 2ï¸�âƒ£ Attach right-click to show the context menu
		/*
		 * palletRoot.setOnContextMenuRequested(event -> {
		 * if (contextMenu != null) {
		 * contextMenu.show(palletRoot, event.getScreenX(), event.getScreenY());
		 * }
		 * });
		 */

		/*
		 * ContextMenu contextMenu = new ContextMenu();
		 * 
		 * MenuItem startItem = new MenuItem("Start");
		 * 
		 * InputStream stream = getClass().getResourceAsStream("/icons/start.png");
		 * if (stream == null) {
		 * System.err.println("Image not found: /icons/start.png");
		 * } else {
		 * ImageView icon = new ImageView(new Image(stream));
		 * icon.setFitWidth(16);
		 * icon.setFitHeight(16);
		 * startItem.setGraphic(icon);
		 * }
		 * 
		 * MenuItem stopItem = new MenuItem("Stop");
		 * MenuItem viewDetails = new MenuItem("View Details");
		 * MenuItem removePallet = new MenuItem("Remove Pallet");
		 * MenuItem highlight = new MenuItem("Highlight");
		 * MenuItem moveToNextBay = new MenuItem("Move to Next Bay");
		 * 
		 * Menu fingertipMenu = new Menu("Fingertip");
		 * MenuItem closeFingertip = new MenuItem("Engage Fingertip");
		 * MenuItem openFingertip = new MenuItem("Disengage Fingertip");
		 * fingertipMenu.getItems().addAll(closeFingertip, openFingertip);
		 * 
		 * // Block Meter Menu
		 * Menu blockMeterMenu = new Menu("Block Meter");
		 * MenuItem blockEntry = new MenuItem("Entry");
		 * MenuItem blockExit = new MenuItem("Exit");
		 * blockMeterMenu.getItems().addAll(blockEntry, blockExit);
		 * 
		 * // Unblock Meter Menu
		 * Menu unblockMeterMenu = new Menu("Unblock Meter");
		 * MenuItem unblockEntry = new MenuItem("Entry");
		 * MenuItem unblockExit = new MenuItem("Exit");
		 * unblockMeterMenu.getItems().addAll(unblockEntry, unblockExit);
		 * 
		 * 
		 * MenuItem sourceStart = new MenuItem("Source Start");
		 * MenuItem sourceStop = new MenuItem("Source Stop");
		 * 
		 * startItem.setDisable(false);
		 * stopItem.setDisable(true);
		 * 
		 * viewDetails .setOnAction(event -> handleViewDetails());
		 * removePallet .setOnAction(event -> handleRemovePallet());
		 * closeFingertip .setOnAction(event -> handleCloseFingertip());
		 * openFingertip .setOnAction(event -> handleOpenFingertip());
		 * 
		 * // Block Meter Submenu Handlers
		 * blockEntry .setOnAction(event -> handleCloseStopperEntry());
		 * blockExit .setOnAction(event -> handleCloseStopperExit());
		 * 
		 * // Unblock Meter Submenu Handlers
		 * unblockEntry .setOnAction(event -> handleOpenStopperEntry());
		 * unblockExit .setOnAction(event -> handleOpenStopperExit());
		 * 
		 * sourceStart .setOnAction(event -> handleSourceStart());
		 * sourceStop .setOnAction(event -> handleSourceStop());
		 * 
		 * highlight.setOnAction(e -> palletRoot.setStyle("-fx-border-color: red;"));
		 * 
		 * moveToNextBay.setOnAction(event -> {
		 * if (onMoveToNextBayHandler != null) {
		 * onMoveToNextBayHandler.accept(this);
		 * }
		 * });
		 * 
		 * startItem.setOnAction(event -> {
		 * ApplicationLauncher.logger.info("Start clicked for " + palletName.getText());
		 * 
		 * // Your start logic here (e.g. pulse detection, communication, etc.)
		 * 
		 * startItem.setDisable(true);
		 * stopItem.setDisable(false);
		 * });
		 * 
		 * stopItem.setOnAction(event -> {
		 * ApplicationLauncher.logger.info("Stop clicked for " + palletName.getText());
		 * 
		 * // Your stop logic here
		 * 
		 * startItem.setDisable(false);
		 * stopItem.setDisable(true);
		 * });
		 * 
		 * contextMenu.getItems().addAll(
		 * startItem,
		 * viewDetails,
		 * removePallet,
		 * highlight,
		 * moveToNextBay,
		 * stopItem,
		 * new SeparatorMenuItem(),
		 * fingertipMenu,
		 * blockMeterMenu,
		 * unblockMeterMenu,
		 * sourceStart,
		 * sourceStop
		 * );
		 * 
		 * 
		 * palletRoot.setOnContextMenuRequested(event ->
		 * contextMenu.show(palletRoot, event.getScreenX(), event.getScreenY())
		 * );
		 */
		// Hide menu on any left-click
		palletRoot.setOnMousePressed(event -> {
			if (event.isPrimaryButtonDown()) {
				// contextMenu.hide();
				// commonContextMenu.hide();
			}
		});

		// Set Tooltips for each Rectangle
		// Add Tooltip to each rectangle
		Tooltip tooltip1 = new Tooltip("Position 1");
		Tooltip tooltip2 = new Tooltip("Position 2");
		Tooltip tooltip3 = new Tooltip("Position 3");
		Tooltip tooltip4 = new Tooltip("Position 4");
		Tooltip tooltip5 = new Tooltip("Position 5");
		Tooltip tooltip6 = new Tooltip("Position 6");

		// Install tooltips on rectangles
		Tooltip.install(meter1, tooltip1);
		Tooltip.install(meter2, tooltip2);
		Tooltip.install(meter3, tooltip3);
		Tooltip.install(meter4, tooltip4);
		Tooltip.install(meter5, tooltip5);
		Tooltip.install(meter6, tooltip6);

		// tooltip1.setShowDelay(javafx.util.Duration.millis(500)); // You can adjust
		// this value
	}

	/*
	 * private void toolTipInit() {
	 * 
	 * Tooltip.install(rectEntryStopperOpen, new
	 * Tooltip("Entry stopper open status"));
	 * Tooltip.install(rectExitStopperOpen, new
	 * Tooltip("Exit stopper open status"));
	 * Tooltip.install(rectAllPalletsExistInBay, new
	 * Tooltip("All pallets exist in this bay"));
	 * Tooltip.install(rectPalletsExistInQueue, new
	 * Tooltip("Pallets are waiting in the queue"));
	 * Tooltip.install(rectAllPalletsExistInTargetBay, new
	 * Tooltip("Target bay all pallet exist"));
	 * }
	 */

	private void refInitAssignment() {

		/*
		 * ref_rectEntryStopperOpen = rectEntryStopperOpen;
		 * ref_rectExitStopperOpen = rectExitStopperOpen;
		 * ref_rectAllPalletsExistInBay = rectAllPalletsExistInBay;
		 * ref_rectPalletsExistInQueue = rectPalletsExistInQueue;
		 * ref_rectTargetBayAllPalletsFree = rectTargetBayAllPalletsFree;
		 */
	}

	/*
	 * private void createContextMenu() {
	 * contextMenu = new ContextMenu();
	 * 
	 * // Based on bayType, you can add/remove menu items
	 * if (bayTypeKey.equals(ConstantConveyor.FT_BAY_KEY)) {
	 * addMenuItem(BayActionType.FINGERTIP_ENGAGE);
	 * addMenuItem(BayActionType.FINGERTIP_DISENGAGE);
	 * addMenuItem(BayActionType.SOURCE_START);
	 * addMenuItem(BayActionType.SOURCE_STOP);
	 * addMenuItem(BayActionType.BLOCK_BAY_ENTRY);
	 * addMenuItem(BayActionType.BLOCK_BAY_EXIT);
	 * addMenuItem(BayActionType.RELEASE_METER_FROM_BAY);
	 * } else if (bayTypeKey.equals(ConstantConveyor.HV_BAY_KEY)) {
	 * addMenuItem(BayActionType.FINGERTIP_ENGAGE);
	 * addMenuItem(BayActionType.FINGERTIP_DISENGAGE);
	 * addMenuItem(BayActionType.BLOCK_BAY_ENTRY);
	 * addMenuItem(BayActionType.RELEASE_METER_FROM_BAY);
	 * } else if (bayTypeKey.equals(ConstantConveyor.IR_BAY_KEY)) {
	 * addMenuItem(BayActionType.FINGERTIP_ENGAGE);
	 * addMenuItem(BayActionType.FINGERTIP_DISENGAGE);
	 * addMenuItem(BayActionType.BLOCK_BAY_ENTRY);
	 * addMenuItem(BayActionType.RELEASE_METER_FROM_BAY);
	 * } else if (bayTypeKey.equals(ConstantConveyor.CALIBRATION_BAY_KEY)) {
	 * addMenuItem(BayActionType.FINGERTIP_ENGAGE);
	 * addMenuItem(BayActionType.FINGERTIP_DISENGAGE);
	 * addMenuItem(BayActionType.BLOCK_BAY_ENTRY);
	 * addMenuItem(BayActionType.RELEASE_METER_FROM_BAY);
	 * }else {
	 * ApplicationLauncher.logger.warn("createContextMenu: Unknown bayType " +
	 * bayTypeKey);
	 * }
	 * }
	 */

	/*
	 * private void addMenuItem(BayActionType actionType) {
	 * MenuItem menuItem = new MenuItem(actionType.toString());
	 * menuItem.setOnAction(e -> handleActionByBayType(actionType));
	 * //contextMenu.getItems().add(menuItem);
	 * commonContextMenu.getItems().add(menuItem);
	 * }
	 */

	/*
	 * public void handleActionByBayType(BayActionType actionType) {
	 * switch (bayTypeKey) {
	 * case ConstantConveyor.FT_BAY_KEY:
	 * handleFunctionalTestAction(actionType);
	 * break;
	 * case ConstantConveyor.HV_BAY_KEY:
	 * handleHighVoltageTestAction(actionType);
	 * break;
	 * case ConstantConveyor.IR_BAY_KEY:
	 * handleInsulationResistanceTestAction(actionType);
	 * break;
	 * case ConstantConveyor.CALIBRATION_BAY_KEY:
	 * handleCalibrationAction(actionType);
	 * break;
	 * // case "Waiting":
	 * // handleWaitingAction(actionType);
	 * // break;
	 * default:
	 * ApplicationLauncher.logger.warn("handleActionByBayType: Unknown bayType " +
	 * bayTypeKey + " for action " + actionType);
	 * }
	 * }
	 */

	// F U N C T I O N B A Y
	// ======================================================================================

	/*
	 * private void handleFunctionalTestAction(BayActionType actionType) {
	 * switch (actionType) {
	 * case FINGERTIP_ENGAGE:
	 * engageFunctionalTestFingertip();
	 * break;
	 * case FINGERTIP_DISENGAGE:
	 * disengageFunctionalTestFingertip();
	 * break;
	 * case SOURCE_START :
	 * startFunctionalTestSource();
	 * break;
	 * case SOURCE_STOP :
	 * stopFunctionalTestSource();
	 * break;
	 * case BLOCK_BAY_ENTRY:
	 * closeFunctionalTestStopperEntry();
	 * break;
	 * case BLOCK_BAY_EXIT:
	 * closeFunctionalTestStopperExit();
	 * break;
	 * case RELEASE_METER_FROM_BAY:
	 * openFunctionalTestStopperEntry();
	 * openFunctionalTestStopperExit();
	 * break;
	 * }
	 * }
	 */

	/*
	 * private void engageFunctionalTestFingertip() {
	 * ApplicationLauncher.logger.warn("engageFunctionalTestFingertip : Entry");
	 * 
	 * controlFingertip(ConstantBayPortNameMapping.FT_PORT_NAME_FINGER_TIP, CLOSE);
	 * }
	 * 
	 * private void disengageFunctionalTestFingertip() {
	 * ApplicationLauncher.logger.warn("disengageFunctionalTestFingertip : Entry");
	 * 
	 * controlFingertip(ConstantBayPortNameMapping.FT_PORT_NAME_FINGER_TIP, OPEN);
	 * }
	 * 
	 * private void startFunctionalTestSource() {
	 * ApplicationLauncher.logger.warn("startFunctionalTestSource : Entry");
	 * 
	 * controlOutput(ConstantBayPortNameMapping.FT_PORT_NAME_SRC_START, CLOSE);
	 * }
	 * 
	 * private void stopFunctionalTestSource() {
	 * ApplicationLauncher.logger.warn("stopFunctionalTestSource : Entry");
	 * 
	 * controlOutput(ConstantBayPortNameMapping.FT_PORT_NAME_SRC_START, OPEN);
	 * }
	 * 
	 * private void closeFunctionalTestStopperEntry() {
	 * controlOutput(ConstantBayPortNameMapping.FT_PORT_NAME_STPR_B4, CLOSE);
	 * }
	 * 
	 * private void openFunctionalTestStopperEntry() {
	 * controlOutput(ConstantBayPortNameMapping.FT_PORT_NAME_STPR_B4, OPEN);
	 * }
	 * 
	 * private void closeFunctionalTestStopperExit() {
	 * controlOutput(ConstantBayPortNameMapping.FT_PORT_NAME_STPR_AT, CLOSE);
	 * }
	 * 
	 * private void openFunctionalTestStopperExit() {
	 * controlOutput(ConstantBayPortNameMapping.FT_PORT_NAME_STPR_AT, OPEN);
	 * }
	 */

	/*
	 * // H I G H V O L T A G E B A Y
	 * =============================================================================
	 * =========
	 * 
	 * private void handleHighVoltageTestAction(BayActionType actionType) {
	 * switch (actionType) {
	 * case FINGERTIP_ENGAGE:
	 * engageHighVoltageTestFingertip();
	 * break;
	 * case FINGERTIP_DISENGAGE:
	 * disengageHighVoltageTestFingertip();
	 * break;
	 * case BLOCK_BAY_ENTRY:
	 * closeHighVoltageTestStopper();
	 * break;
	 * case RELEASE_METER_FROM_BAY:
	 * openHighVoltageTestStopper();
	 * break;
	 * default:
	 * break;
	 * }
	 * }
	 */

	/*
	 * private void engageHighVoltageTestFingertip() {
	 * ApplicationLauncher.logger.warn("disengageHighVoltageTestFingertip : Entry");
	 * 
	 * controlFingertip(ConstantBayPortNameMapping.HV_PORT_NAME_FINGER_TIP, CLOSE);
	 * }
	 * 
	 * private void disengageHighVoltageTestFingertip() {
	 * ApplicationLauncher.logger.warn("disengageHighVoltageTestFingertip : Entry");
	 * 
	 * controlFingertip(ConstantBayPortNameMapping.HV_PORT_NAME_FINGER_TIP, OPEN);
	 * }
	 * 
	 * private void closeHighVoltageTestStopper() {
	 * ApplicationLauncher.logger.warn("closeHighVoltageTestStopper : Entry");
	 * 
	 * controlOutput(ConstantBayPortNameMapping.HV_PORT_NAME_STPR, CLOSE);
	 * }
	 * 
	 * private void openHighVoltageTestStopper() {
	 * ApplicationLauncher.logger.warn("closeHighVoltageTestStopper : Entry");
	 * 
	 * controlOutput(ConstantBayPortNameMapping.HV_PORT_NAME_STPR, OPEN);
	 * }
	 */

	/*
	 * // I N S U L A T I O N R E S I S T A N C E B A Y
	 * =============================================================================
	 * =========
	 * 
	 * private void handleInsulationResistanceTestAction(BayActionType actionType) {
	 * switch (actionType) {
	 * case FINGERTIP_ENGAGE:
	 * engageInsulationResistanceTestFingertip();
	 * break;
	 * case FINGERTIP_DISENGAGE:
	 * disengageInsulationResistanceTestFingertip();
	 * break;
	 * case BLOCK_BAY_ENTRY:
	 * closeInsulationResistanceTestStopper();
	 * break;
	 * case RELEASE_METER_FROM_BAY:
	 * openInsulationResistanceTestStopper();
	 * break;
	 * default:
	 * break;
	 * }
	 * }
	 */

	/*
	 * private void engageInsulationResistanceTestFingertip() {
	 * ApplicationLauncher.logger.
	 * warn("disengageInsulationResistanceTestFingertip : Entry");
	 * 
	 * controlFingertip(ConstantBayPortNameMapping.IR_PORT_NAME_FINGER_TIP, CLOSE);
	 * }
	 * 
	 * private void disengageInsulationResistanceTestFingertip() {
	 * ApplicationLauncher.logger.
	 * warn("disengageInsulationResistanceTestFingertip : Entry");
	 * 
	 * controlFingertip(ConstantBayPortNameMapping.IR_PORT_NAME_FINGER_TIP, OPEN);
	 * }
	 * 
	 * private void closeInsulationResistanceTestStopper() {
	 * ApplicationLauncher.logger.
	 * warn("closeInsulationResistanceTestStopper : Entry");
	 * 
	 * controlOutput(ConstantBayPortNameMapping.IR_PORT_NAME_STPR, CLOSE);
	 * }
	 * 
	 * private void openInsulationResistanceTestStopper() {
	 * ApplicationLauncher.logger.
	 * warn("closeInsulationResistanceTestStopper : Entry");
	 * 
	 * controlOutput(ConstantBayPortNameMapping.IR_PORT_NAME_STPR, OPEN);
	 * }
	 */

	/*
	 * // C A L I B R A T I O N B A Y
	 * =============================================================================
	 * =========
	 * 
	 * 
	 * private void handleCalibrationAction(BayActionType actionType) {
	 * switch (actionType) {
	 * case FINGERTIP_ENGAGE:
	 * engageCalibrationFingertip();
	 * break;
	 * case FINGERTIP_DISENGAGE:
	 * disengageCalibrationFingertip();
	 * break;
	 * default:
	 * ApplicationLauncher.logger.warn("Calibration: Unsupported action " +
	 * actionType);
	 * }
	 * }
	 */

	/*
	 * private void disengageCalibrationFingertip() {
	 * ApplicationLauncher.logger.warn("disengageCalibrationFingertip: Entry");
	 * 
	 * controlFingertip(ConstantBayPortNameMapping.CALIB_PORT_NAME_FINGER_TIP,
	 * CLOSE);
	 * }
	 * 
	 * private void engageCalibrationFingertip() {
	 * ApplicationLauncher.logger.warn("engageCalibrationFingertip: Entry");
	 * 
	 * controlFingertip(ConstantBayPortNameMapping.CALIB_PORT_NAME_FINGER_TIP,
	 * OPEN);
	 * }
	 */

	// W A I T I N G B A Y
	// ======================================================================================

	private void handleWaitingAction(BayActionType actionType) {
		ApplicationLauncher.logger.info("Waiting bay: action " + actionType + " ignored.");
	}

	public void setSerialByPosition(int position, String serial) {
		if (position >= 1 && position <= serialSetters.size()) {
			serialSetters.get(position - 1).accept(serial);
		}
	}

	public String getPalletNameText() {
		return palletName.getText();
	}

	public void setErrorCodeByPosition(int position, String errorCode) {
		switch (position) {
			case 1:
				errorCode1.setText(errorCode);
				break;
			case 2:
				errorCode2.setText(errorCode);
				break;
			case 3:
				errorCode3.setText(errorCode);
				break;
			case 4:
				errorCode4.setText(errorCode);
				break;
			case 5:
				errorCode5.setText(errorCode);
				break;
			case 6:
				errorCode6.setText(errorCode);
				break;
		}
	}

	public void setOnMoveToNextBayHandler(Consumer<PalletController> handler) {
		this.onMoveToNextBayHandler = handler;
	}

	public AnchorPane getPalletRoot() {
		return palletRoot;
	}

	/*
	 * private void handleViewDetails() {
	 * System.out.println("View details for: " + palletName.getText());
	 * // Optionally: show alert, modal, etc.
	 * }
	 * 
	 * private void handleRemovePallet() {
	 * if (palletRoot.getParent() instanceof AnchorPane) {
	 * ((AnchorPane) palletRoot.getParent()).getChildren().remove(palletRoot);
	 * }
	 * }
	 * 
	 * private void handleCloseFingertip(){
	 * closeFingertip();
	 * }
	 * 
	 * private void handleOpenFingertip(){
	 * openFingertip();
	 * }
	 * 
	 * private void handleCloseStopperEntry() {
	 * closeStopperEntry();
	 * }
	 * 
	 * private void handleOpenStopperEntry() {
	 * openStopperEntry();
	 * }
	 * 
	 * private void handleCloseStopperExit() {
	 * closeStopperExit();
	 * }
	 * 
	 * private void handleOpenStopperExit() {
	 * openStopperExit();
	 * }
	 * 
	 * private void handleSourceStart() {
	 * sourceStart();
	 * }
	 * 
	 * private void handleSourceStop() {
	 * sourceStop();
	 * }
	 */

	public void setPalletName(String name) {
		/*
		 * if (palletName != null) {
		 * palletName.setText(name);
		 * }
		 */
		if (palletName != null) {
			// palletName.setText("Updated Pallet Name");
			palletName.setText(name);
		} else {
			System.out.println("Error: palletName is null");
		}
	}

	public void setMeterStatus(int meterIndex, MeterStatus status) {

		Rectangle target = null;
		switch (meterIndex) {
			case 1:
				target = meter1;
				break;
			case 2:
				target = meter2;
				break;
			case 3:
				target = meter3;
				break;
			case 4:
				target = meter4;
				break;
			case 5:
				target = meter5;
				break;
			case 6:
				target = meter6;
				break;
			default:
				break;
		}

		if (target != null && status != null) {
			target.setFill(status.getColor());
		}

		if (target != null) {
			target.setFill(status.getColor());
			if (status == MeterStatus.FAILED) {
				target.setStroke(Color.RED);
				target.setStrokeWidth(2);
			} else {
				target.setStroke(Color.BLACK);
			}
		}
	}

	public Text getPalletName() {
		return palletName;
	}

	public TextField getSerial1() {
		return serial1;
	}

	public TextField getSerial2() {
		return serial2;
	}

	public TextField getSerial3() {
		return serial3;
	}

	public TextField getSerial4() {
		return serial4;
	}

	public TextField getSerial5() {
		return serial5;
	}

	public TextField getSerial6() {
		return serial6;
	}

	public void setPalletName(Text palletName) {
		this.palletName = palletName;
	}

	public void setSerial1(String serial1) {
		this.serial1.setText(serial1);
	}

	public void setSerial2(String serial2) {
		this.serial2.setText(serial2);
	}

	public void setSerial3(String serial3) {
		this.serial3.setText(serial3);
	}

	public void setSerial4(String serial4) {
		this.serial4.setText(serial4);
	}

	public void setSerial5(String serial5) {
		this.serial5.setText(serial5);
	}

	public void setSerial6(String serial6) {
		this.serial6.setText(serial6);
	}

	/*
	 * public BayUtils getBayUtils() {
	 * return bayUtils;
	 * }
	 * 
	 * 
	 * public void setBayUtils(BayUtils bayUtils) {
	 * this.bayUtils = bayUtils;
	 * }
	 */

	// MECHANICAL FUNCTIONS
	// ====================================================================================================================
	// Generalized Method
	/*
	 * private void controlFingertip(String portNameKey, boolean shouldClose) {
	 * IoPortInfo portInfo = BayUtils.getOutputPortDetails(portNameKey);
	 * 
	 * if (portInfo != null) {
	 * FunctionalTestBay.logger.debug("PortId    : " + portInfo.getPortId());
	 * FunctionalTestBay.logger.debug("ClusterId : " + portInfo.getClusterId());
	 * FunctionalTestBay.logger.debug("BayId     : " + portInfo.getBayId());
	 * 
	 * String outputAction = shouldClose
	 * ? Constant_IO_ActionMapping.OLD_CLOSE_NEW_OPEN
	 * : Constant_IO_ActionMapping.OLD_OPEN_NEW_CLOSE;
	 * 
	 * if (ProcalFeatureEnable.MODBUS_PLC_SLAVE_MODE) {
	 * getBayUtils().setOutputDataToPlcBay(
	 * portInfo.getClusterId(),
	 * portInfo.getBayId(),
	 * portInfo.getPortId(),
	 * outputAction);
	 * } else {
	 * getBayUtils().setOutputDataToBay(
	 * portInfo.getClusterId(),
	 * portInfo.getBayId(),
	 * portInfo.getPortId(),
	 * outputAction);
	 * }
	 * }
	 * }
	 */

	// STOPPERS

	/*
	 * private void controlOutput(String portNameKey, boolean shouldClose) {
	 * IoPortInfo portInfo = BayUtils.getOutputPortDetails(portNameKey);
	 * 
	 * if (portInfo != null) {
	 * FunctionalTestBay.logger.debug("PortId    : " + portInfo.getPortId());
	 * FunctionalTestBay.logger.debug("ClusterId : " + portInfo.getClusterId());
	 * FunctionalTestBay.logger.debug("BayId     : " + portInfo.getBayId());
	 * 
	 * String outputAction = shouldClose
	 * ? Constant_IO_ActionMapping.OLD_OFF_NEW_ON // Close = OFF->ON
	 * : Constant_IO_ActionMapping.OLD_ON_NEW_OFF; // Open = ON->OFF
	 * 
	 * if (ProcalFeatureEnable.MODBUS_PLC_SLAVE_MODE) {
	 * getBayUtils().setOutputDataToPlcBay(
	 * portInfo.getClusterId(),
	 * portInfo.getBayId(),
	 * portInfo.getPortId(),
	 * outputAction);
	 * } else {
	 * getBayUtils().setOutputDataToBay(
	 * portInfo.getClusterId(),
	 * portInfo.getBayId(),
	 * portInfo.getPortId(),
	 * outputAction);
	 * }
	 * }
	 * }
	 */

	/*
	 * private void closeStopperEntry() {
	 * IoPortInfo portInfo =
	 * BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_STPR_B4
	 * );
	 * if (portInfo != null) {
	 * FunctionalTestBay.logger.debug("PortId    : " + portInfo.getPortId());
	 * FunctionalTestBay.logger.debug("ClusterId : " + portInfo.getClusterId());
	 * FunctionalTestBay.logger.debug("BayId     : " + portInfo.getBayId());
	 * String outputActive = Constant_IO_ActionMapping.OLD_OFF_NEW_ON;
	 * //RestApiJsonBodyResponse clusterResponseData =
	 * 
	 * if(ProcalFeatureEnable.MODBUS_PLC_SLAVE_MODE){
	 * getBayUtils().setOutputDataToPlcBay(portInfo.getClusterId(),
	 * portInfo.getBayId(),
	 * portInfo.getPortId(),
	 * outputActive);
	 * }else{
	 * getBayUtils().setOutputDataToBay( portInfo.getClusterId(),
	 * portInfo.getBayId(),
	 * portInfo.getPortId(),outputActive);
	 * }
	 * }
	 * }
	 * 
	 * private void closeStopperExit() {
	 * IoPortInfo portInfo =
	 * BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_STPR_AT
	 * );
	 * if (portInfo != null) {
	 * FunctionalTestBay.logger.debug("PortId    : " + portInfo.getPortId());
	 * FunctionalTestBay.logger.debug("ClusterId : " + portInfo.getClusterId());
	 * FunctionalTestBay.logger.debug("BayId     : " + portInfo.getBayId());
	 * String outputActive = Constant_IO_ActionMapping.OLD_OFF_NEW_ON;
	 * //RestApiJsonBodyResponse clusterResponseData =
	 * 
	 * if(ProcalFeatureEnable.MODBUS_PLC_SLAVE_MODE){
	 * getBayUtils().setOutputDataToPlcBay(portInfo.getClusterId(),
	 * portInfo.getBayId(),
	 * portInfo.getPortId(),
	 * outputActive);
	 * }else{
	 * getBayUtils().setOutputDataToBay( portInfo.getClusterId(),
	 * portInfo.getBayId(),
	 * portInfo.getPortId(),outputActive);
	 * }
	 * }
	 * }
	 * 
	 * private void openStopperEntry() {
	 * IoPortInfo portInfo =
	 * BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_STPR_B4
	 * );
	 * if (portInfo != null) {
	 * FunctionalTestBay.logger.debug("PortId    : " + portInfo.getPortId());
	 * FunctionalTestBay.logger.debug("ClusterId : " + portInfo.getClusterId());
	 * FunctionalTestBay.logger.debug("BayId     : " + portInfo.getBayId());
	 * String outputActive = Constant_IO_ActionMapping.OLD_ON_NEW_OFF;
	 * //RestApiJsonBodyResponse clusterResponseData =
	 * 
	 * if(ProcalFeatureEnable.MODBUS_PLC_SLAVE_MODE){
	 * getBayUtils().setOutputDataToPlcBay(portInfo.getClusterId(),
	 * portInfo.getBayId(),
	 * portInfo.getPortId(),
	 * outputActive);
	 * }else{
	 * getBayUtils().setOutputDataToBay( portInfo.getClusterId(),
	 * portInfo.getBayId(),
	 * portInfo.getPortId(),outputActive);
	 * }
	 * }
	 * }
	 * 
	 * private void openStopperExit() {
	 * IoPortInfo portInfo =
	 * BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_STPR_AT
	 * );
	 * if (portInfo != null) {
	 * FunctionalTestBay.logger.debug("PortId    : " + portInfo.getPortId());
	 * FunctionalTestBay.logger.debug("ClusterId : " + portInfo.getClusterId());
	 * FunctionalTestBay.logger.debug("BayId     : " + portInfo.getBayId());
	 * String outputActive = Constant_IO_ActionMapping.OLD_ON_NEW_OFF;
	 * //RestApiJsonBodyResponse clusterResponseData =
	 * 
	 * if(ProcalFeatureEnable.MODBUS_PLC_SLAVE_MODE){
	 * getBayUtils().setOutputDataToPlcBay(portInfo.getClusterId(),
	 * portInfo.getBayId(),
	 * portInfo.getPortId(),
	 * outputActive);
	 * }else{
	 * getBayUtils().setOutputDataToBay( portInfo.getClusterId(),
	 * portInfo.getBayId(),
	 * portInfo.getPortId(),outputActive);
	 * }
	 * }
	 * }
	 */

	/*
	 * //
	 * =============================================================================
	 * =======================================
	 * 
	 * // SOURCE
	 * private void sourceStart() {
	 * IoPortInfo portInfo =
	 * BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.
	 * FT_PORT_NAME_SRC_START);
	 * if (portInfo != null) {
	 * FunctionalTestBay.logger.debug("PortId    : " + portInfo.getPortId());
	 * FunctionalTestBay.logger.debug("ClusterId : " + portInfo.getClusterId());
	 * FunctionalTestBay.logger.debug("BayId     : " + portInfo.getBayId());
	 * String outputActive = Constant_IO_ActionMapping.OLD_OFF_NEW_ON;
	 * //RestApiJsonBodyResponse clusterResponseData =
	 * 
	 * if(ProcalFeatureEnable.MODBUS_PLC_SLAVE_MODE){
	 * getBayUtils().setOutputDataToPlcBay(portInfo.getClusterId(),
	 * portInfo.getBayId(),
	 * portInfo.getPortId(),
	 * outputActive);
	 * }else{
	 * getBayUtils().setOutputDataToBay( portInfo.getClusterId(),
	 * portInfo.getBayId(),
	 * portInfo.getPortId(),outputActive);
	 * }
	 * }
	 * }
	 * 
	 * private void sourceStop() {
	 * IoPortInfo portInfo =
	 * BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.
	 * FT_PORT_NAME_SRC_START);
	 * if (portInfo != null) {
	 * FunctionalTestBay.logger.debug("PortId    : " + portInfo.getPortId());
	 * FunctionalTestBay.logger.debug("ClusterId : " + portInfo.getClusterId());
	 * FunctionalTestBay.logger.debug("BayId     : " + portInfo.getBayId());
	 * String outputActive = Constant_IO_ActionMapping.OLD_ON_NEW_OFF;
	 * //RestApiJsonBodyResponse clusterResponseData =
	 * 
	 * if(ProcalFeatureEnable.MODBUS_PLC_SLAVE_MODE){
	 * getBayUtils().setOutputDataToPlcBay(portInfo.getClusterId(),
	 * portInfo.getBayId(),
	 * portInfo.getPortId(),
	 * outputActive);
	 * }else{
	 * getBayUtils().setOutputDataToBay( portInfo.getClusterId(),
	 * portInfo.getBayId(),
	 * portInfo.getPortId(),outputActive);
	 * }
	 * }
	 * }
	 */

	public String getMyBayKey() {
		return bayTypeKey;
	}

	public void setBayTypeKey(String myBayKey) {
		this.bayTypeKey = myBayKey;
		this.bayActionHandler = new BayActionHandler(myBayKey);
		// createContextMenu();
	}

	/*
	 * public Rectangle getRectEntryStopperOpen() {
	 * return rectEntryStopperOpen;
	 * }
	 * 
	 * 
	 * 
	 * public Rectangle getRectExitStopperOpen() {
	 * return rectExitStopperOpen;
	 * }
	 * 
	 * 
	 * 
	 * public Rectangle getRectAllPalletsExistInBay() {
	 * return rectAllPalletsExistInBay;
	 * }
	 * 
	 * 
	 * 
	 * public Rectangle getRectPalletsExistInQueue() {
	 * return rectPalletsExistInQueue;
	 * }
	 * 
	 * 
	 * 
	 * public Rectangle getAllPalletsExistInTargetBay() {
	 * return rectAllPalletsExistInTargetBay;
	 * }
	 * 
	 * 
	 * 
	 * public void setRectEntryStopperOpen(Rectangle rectEntryStopperOpen) {
	 * this.rectEntryStopperOpen = rectEntryStopperOpen;
	 * }
	 * 
	 * 
	 * 
	 * public void setRectExitStopperOpen(Rectangle rectExitStopperOpen) {
	 * this.rectExitStopperOpen = rectExitStopperOpen;
	 * }
	 * 
	 * 
	 * 
	 * public void setRectAllPalletsExistInBay(Rectangle rectAllPalletsExistInBay) {
	 * this.rectAllPalletsExistInBay = rectAllPalletsExistInBay;
	 * }
	 * 
	 * 
	 * 
	 * public void setRectPalletsExistInQueue(Rectangle rectPalletsExistInQueue) {
	 * this.rectPalletsExistInQueue = rectPalletsExistInQueue;
	 * }
	 * 
	 * 
	 * 
	 * public void setRectAllPalletsExistInTargetBay(Rectangle
	 * rectAllPalletsExistInTargetBay) {
	 * this.rectAllPalletsExistInTargetBay = rectAllPalletsExistInTargetBay;
	 * }
	 * 
	 * 
	 * 
	 * 
	 * public void startBlinkingPalletsExistInQueueIndicator() {
	 * if (rectPalletsExistInQueue == null) return;
	 * 
	 * blinkTimelinePalletsExistInQueue = new Timeline(
	 * new KeyFrame(Duration.seconds(0.5), e ->
	 * rectPalletsExistInQueue.setFill(Color.YELLOW)),
	 * new KeyFrame(Duration.seconds(1.0), e ->
	 * rectPalletsExistInQueue.setFill(Color.TRANSPARENT))
	 * );
	 * blinkTimelinePalletsExistInQueue.setCycleCount(Animation.INDEFINITE);
	 * blinkTimelinePalletsExistInQueue.play();
	 * }
	 * 
	 * public void stopBlinkingPalletsExistInQueueIndicator() {
	 * if (blinkTimelinePalletsExistInQueue != null) {
	 * blinkTimelinePalletsExistInQueue.stop();
	 * rectPalletsExistInQueue.setFill(Color.TRANSPARENT);
	 * }
	 * }
	 * 
	 * 
	 * public void startBlinkingEntryStopperOpenIndicator() {
	 * if (rectEntryStopperOpen == null) return;
	 * 
	 * blinkTimelineEntryStopperOpen = new Timeline(
	 * new KeyFrame(Duration.seconds(0.5), e ->
	 * rectEntryStopperOpen.setFill(Color.YELLOW)),
	 * new KeyFrame(Duration.seconds(1.0), e ->
	 * rectEntryStopperOpen.setFill(Color.TRANSPARENT))
	 * );
	 * blinkTimelineEntryStopperOpen.setCycleCount(Animation.INDEFINITE);
	 * blinkTimelineEntryStopperOpen.play();
	 * }
	 * 
	 * public void stopBlinkingEntryStopperOpenIndicator() {
	 * if (blinkTimelineEntryStopperOpen != null) {
	 * blinkTimelineEntryStopperOpen.stop();
	 * rectEntryStopperOpen.setFill(Color.TRANSPARENT);
	 * }
	 * }
	 * 
	 * 
	 * public void startBlinkingExitStopperOpenIndicator() {
	 * if (rectExitStopperOpen == null) return;
	 * 
	 * blinkTimelineExitStopperOpen = new Timeline(
	 * new KeyFrame(Duration.seconds(0.5), e ->
	 * rectExitStopperOpen.setFill(Color.YELLOW)),
	 * new KeyFrame(Duration.seconds(1.0), e ->
	 * rectExitStopperOpen.setFill(Color.TRANSPARENT))
	 * );
	 * blinkTimelineExitStopperOpen.setCycleCount(Animation.INDEFINITE);
	 * blinkTimelineExitStopperOpen.play();
	 * }
	 * 
	 * public void stopBlinkingExitStopperOpenIndicator() {
	 * if (blinkTimelineExitStopperOpen != null) {
	 * blinkTimelineExitStopperOpen.stop();
	 * rectExitStopperOpen.setFill(Color.TRANSPARENT);
	 * }
	 * }
	 * 
	 * public void startBlinkingAllPalletsExistInBayIndicator() {
	 * if (rectAllPalletsExistInBay == null) return;
	 * 
	 * blinkTimelineAllPalletsExistInBay = new Timeline(
	 * new KeyFrame(Duration.seconds(0.5), e ->
	 * rectAllPalletsExistInBay.setFill(Color.YELLOW)),
	 * new KeyFrame(Duration.seconds(1.0), e ->
	 * rectAllPalletsExistInBay.setFill(Color.TRANSPARENT))
	 * );
	 * blinkTimelineAllPalletsExistInBay.setCycleCount(Animation.INDEFINITE);
	 * blinkTimelineAllPalletsExistInBay.play();
	 * }
	 * 
	 * public void stopBlinkingAllPalletsExistInBayIndicator() {
	 * if (blinkTimelineAllPalletsExistInBay != null) {
	 * blinkTimelineAllPalletsExistInBay.stop();
	 * rectAllPalletsExistInBay.setFill(Color.TRANSPARENT);
	 * }
	 * }
	 * 
	 * 
	 * public void startBlinkingAllPalletsExistInTargetBayIndicator() {
	 * if (rectAllPalletsExistInTargetBay == null) return;
	 * 
	 * blinkTimelineTargetBayAllPalletsFree = new Timeline(
	 * new KeyFrame(Duration.seconds(0.5), e ->
	 * rectAllPalletsExistInTargetBay.setFill(Color.YELLOW)),
	 * new KeyFrame(Duration.seconds(1.0), e ->
	 * rectAllPalletsExistInTargetBay.setFill(Color.TRANSPARENT))
	 * );
	 * blinkTimelineTargetBayAllPalletsFree.setCycleCount(Animation.INDEFINITE);
	 * blinkTimelineTargetBayAllPalletsFree.play();
	 * }
	 * 
	 * public void stopBlinkingAllPalletsExistInTargetBayIndicator() {
	 * if (blinkTimelineTargetBayAllPalletsFree != null) {
	 * blinkTimelineTargetBayAllPalletsFree.stop();
	 * rectAllPalletsExistInTargetBay.setFill(Color.TRANSPARENT);
	 * }
	 * }
	 * 
	 * public void setEntryStopperOpenIndicator(boolean isOpen) {
	 * stopBlinkingEntryStopperOpenIndicator();
	 * rectEntryStopperOpen.setFill(isOpen ? Color.LIMEGREEN : Color.RED);
	 * }
	 * public void setExitStopperOpenIndicator(boolean isOpen) {
	 * stopBlinkingExitStopperOpenIndicator();
	 * rectExitStopperOpen.setFill(isOpen ? Color.LIMEGREEN : Color.RED);
	 * }
	 * 
	 * public void setAllPalletsExistInBayIndicator(boolean isPalletExist) {
	 * stopBlinkingAllPalletsExistInBayIndicator();
	 * rectAllPalletsExistInBay.setFill(isPalletExist ? Color.LIMEGREEN :
	 * Color.RED);
	 * }
	 * public void setPalletsExistInQueueIndicator(boolean isPalletExist) {
	 * stopBlinkingPalletsExistInQueueIndicator();
	 * rectPalletsExistInQueue.setFill(isPalletExist ? Color.LIMEGREEN : Color.RED);
	 * }
	 * 
	 * public void setAllPalletsExistInTargetBayIndicator(boolean isPalletExist) {
	 * stopBlinkingAllPalletsExistInTargetBayIndicator();
	 * rectAllPalletsExistInTargetBay.setFill(isPalletExist ? Color.LIMEGREEN :
	 * Color.RED);
	 * }
	 * 
	 * public void setEntryStopperOpenIndicatorVisible(boolean isVisible) {
	 * rectEntryStopperOpen.setVisible(isVisible);
	 * }
	 * 
	 * public void setExitStopperOpenIndicatorVisible(boolean isVisible) {
	 * rectExitStopperOpen.setVisible(isVisible);
	 * }
	 * 
	 * public void setAllPalletsExistInBayIndicatorVisible(boolean isVisible) {
	 * rectAllPalletsExistInBay.setVisible(isVisible);
	 * }
	 * 
	 * public void setPalletsExistInQueueIndicatorVisible(boolean isVisible) {
	 * rectPalletsExistInQueue.setVisible(isVisible);
	 * }
	 * 
	 * public void setTargetBayAllPalletsFreeIndicatorVisible(boolean isVisible) {
	 * rectAllPalletsExistInTargetBay.setVisible(isVisible);
	 * }
	 */

	public void setHBoxBottomVisible(boolean visible) {
		hBoxBottom.setVisible(visible);
	}

	public void setHBoxTopVisible(boolean visible) {
		hBoxTop.setVisible(visible);
	}

	public void setPBarPalletExecution(boolean visible) {
		pBarPalletExecution.setVisible(visible);
	}

	// ====================================================================================================================
}