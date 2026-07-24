package com.tasnetwork.calibration.conveyor.dashboard;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class PalletController {

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
		BAY_START("Start"),
		BAY_STOP("Stop"),
		BAY_RESET("Reset"),
		RUN_BAY_ONCE("Run Bay Once"),
		RELEASE_METER_FROM_BAY("Release Meter(s)"),
		PALLETS_CLEARED("Pallets Cleared"),
		NO_ENTRY_ACTIVE("On"),
		NO_ENTRY_INACTIVE("Off"),
		HALT_PALLET_ACTIVE("On"),
		HALT_PALLET_INACTIVE("Off"),
		BYPASS_MODE_ACTIVE("On"),
		BYPASS_MODE_INACTIVE("Off"),
		REFRESH("Refresh");

		private final String displayName;

		BayActionType(String displayName) {
			this.displayName = displayName;
		}

		@Override
		public String toString() {
			return displayName;
		}
	}

	private String bayTypeKey = "";

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

	private List<Consumer<String>> serialSetters;

	private BayActionHandler bayActionHandler;

	public PalletController() {
		// Initialize with default bay type, will be updated in initialize()
		this.bayActionHandler = new BayActionHandler("");
	}

	@FXML
	public void initialize() {
		refInitAssignment();

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
				bayActionHandler.handleActionByBayType(actionType);
			}
		};
		commonContextMenu.attachToNode(palletRoot);

		// Hide menu on any left-click
		palletRoot.setOnMousePressed(event -> {
			if (event.isPrimaryButtonDown()) {

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

	private void refInitAssignment() {
	}

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

	public String getMyBayKey() {
		return bayTypeKey;
	}

	public void setBayTypeKey(String myBayKey) {
		this.bayTypeKey = myBayKey;
		this.bayActionHandler = new BayActionHandler(myBayKey);
		// createContextMenu();
	}

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