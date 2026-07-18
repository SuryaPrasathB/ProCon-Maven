package com.tasnetwork.calibration.conveyor.dashboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.net.ssl.SSLException;

import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.bay.calib.Calib;
import com.tasnetwork.calibration.conveyor.bay.ft.Ft;
import com.tasnetwork.calibration.conveyor.bay.verific.Verification;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.dashboard.PalletController.BayActionType;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.util.YesNoDialogFX;
import com.tasnetwork.spring.orm.model.PalletManage;
import com.tasnetwork.spring.orm.model.PalletMeter;

import javafx.application.Platform;

public class BayActionHandler {
	private String bayTypeKey;
	private final BayUtils bayUtils;

	public static final Boolean CLOSE = true;
	public static final Boolean OPEN = false;

	public BayActionHandler(String bayTypeKey) {
		this.bayTypeKey = bayTypeKey;
		this.bayUtils = new BayUtils();
	}

	public void setBayTypeKey(String bayTypeKey) {
		this.bayTypeKey = bayTypeKey;
	}

	public void handleActionByBayType(PalletController.BayActionType actionType) {
		// Wrap the entire action handling in a new thread to prevent UI blocking.
		// This ensures that any potentially long-running I/O operations or delays
		// do not freeze the JavaFX Application Thread.
		new Thread(() -> {
			switch (bayTypeKey) {
				case ConstantConveyor.FT_BAY_KEY:
					handleFunctionalTestAction(actionType);
					break;
				case ConstantConveyor.HV_BAY_KEY:
					handleHighVoltageTestAction(actionType);
					break;
				case ConstantConveyor.IR_BAY_KEY:
					handleInsulationResistanceTestAction(actionType);
					break;
				case ConstantConveyor.CALIBRATION_BAY_KEY:
					handleCalibrationAction(actionType);
					break;
				case ConstantConveyor.WAITING_PP1_BAY_KEY:
				case ConstantConveyor.WAITING_PP2_BAY_KEY:
				case ConstantConveyor.WAITING_PP3_BAY_KEY:
				case ConstantConveyor.WAITING_PP4_BAY_KEY:
					handleWaitingBayAction(actionType);
					break;
				case ConstantConveyor.VERIFICATION_PP1_BAY_KEY:
				case ConstantConveyor.VERIFICATION_PP2_BAY_KEY:
				case ConstantConveyor.VERIFICATION_PP3_BAY_KEY:
				case ConstantConveyor.VERIFICATION_PP4_BAY_KEY:
					handleVerificationBayAction(actionType);
					break;
				case ConstantConveyor.STA_NLD1_PP1_BAY_KEY:
				case ConstantConveyor.STA_NLD1_PP2_BAY_KEY:
				case ConstantConveyor.STA_NLD1_PP3_BAY_KEY:
				case ConstantConveyor.STA_NLD1_PP4_BAY_KEY:
					handleSTANLD1BayAction(actionType);
					break;
				case ConstantConveyor.STA_NLD2_PP1_BAY_KEY:
				case ConstantConveyor.STA_NLD2_PP2_BAY_KEY:
				case ConstantConveyor.STA_NLD2_PP3_BAY_KEY:
				case ConstantConveyor.STA_NLD2_PP4_BAY_KEY:
					handleSTANLD2BayAction(actionType);
					break;

				case ConstantConveyor.REJECTION_BAY_KEY:
					handleRejectionBayAction(actionType);
					break;

				case ConstantConveyor.UNLOADING_BAY_KEY:
					handleUnloadingBayAction(actionType);
					break;
				default:
					ApplicationLauncher.logger
							.warn("handleActionByBayType: Unknown bayType " + bayTypeKey + " for action " + actionType);
			}
			// If any UI updates were needed after the action, they would typically
			// be handled here using Platform.runLater(). However, since this
			// BayActionHandler class itself does not have direct access to UI components,
			// such updates would usually be delegated back to a UI controller.
			// Example (conceptual, assuming a UI controller would receive a callback):
			// Platform.runLater(() -> {
			// // DashboardController.updateStatusLabel("Action for " + bayTypeKey + "
			// completed.");
			// // Or trigger other UI changes if necessary
			// });
		}).start();
	}

	// S T A N L D B A Y
	// 2===================================================================================

	private void handleSTANLD2BayAction(BayActionType actionType) {
		switch (actionType) {
			case FINGERTIP_ENGAGE:
				engageSTANLD2Fingertip();
				break;
			case FINGERTIP_DISENGAGE:
				disengageSTANLD2Fingertip();
				break;
			case NO_ENTRY_ACTIVE:
				noEntryActiveSta2();
				break;
			case NO_ENTRY_INACTIVE:
				noEntryInActiveSta2();
				break;
			case HALT_PALLET_ACTIVE:
				haltPalletActiveSta2();
				break;
			case HALT_PALLET_INACTIVE:
				haltPalletInActiveSta2();
				break;
			case BLOCK_BAY_EXIT:
				closeSTANLD2BayStopper1();
				break;
			case RELEASE_METER_FROM_BAY:
				openSTANLD2BayStopper1();
				break;
			case REFRESH:
				refreshSta2Bay();
				break;
			default:
				ApplicationLauncher.logger.warn("STANLD2: Unsupported action " + actionType);
		}
	}

	private void handleRejectionBayAction(BayActionType actionType) {
		switch (actionType) {
			/*
			 * case FINGERTIP_ENGAGE:
			 * engageSTANLD2Fingertip();
			 * break;
			 * case FINGERTIP_DISENGAGE:
			 * disengageSTANLD2Fingertip();
			 * break;
			 * case BLOCK_BAY_EXIT:
			 * closeSTANLD2BayStopper1();
			 * break;
			 * case RELEASE_METER_FROM_BAY:
			 * openSTANLD2BayStopper1();
			 * break;
			 */

			case NO_ENTRY_ACTIVE:
				noEntryActiveRejection();
				break;
			case NO_ENTRY_INACTIVE:
				noEntryInActiveRejection();
				break;
			case REFRESH:
				refreshRejectionBay();
				break;
			default:
				ApplicationLauncher.logger.warn("Rejection: Unsupported action " + actionType);
		}
	}

	private void handleUnloadingBayAction(BayActionType actionType) {
		switch (actionType) {
			/*
			 * case FINGERTIP_ENGAGE:
			 * engageSTANLD2Fingertip();
			 * break;
			 * case FINGERTIP_DISENGAGE:
			 * disengageSTANLD2Fingertip();
			 * break;
			 * case BLOCK_BAY_EXIT:
			 * closeSTANLD2BayStopper1();
			 * break;
			 * case RELEASE_METER_FROM_BAY:
			 * openSTANLD2BayStopper1();
			 * break;
			 */
			case NO_ENTRY_ACTIVE:
				noEntryActiveUnloading();
				break;
			case NO_ENTRY_INACTIVE:
				noEntryInActiveUnloading();
				break;
			case REFRESH:
				refreshUnloadingBay();
				break;
			default:
				ApplicationLauncher.logger.warn("Unloading: Unsupported action " + actionType);
		}
	}

	private void disengageSTANLD2Fingertip() {
		ApplicationLauncher.logger.warn("disengageSTANLD2Fingertip: Entry");

		controlFingertip(ConstantBayPortNameMapping.SCT_NLT2_PORT_NAME_FINGER_TIP, OPEN);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setPalletsLockedImageDisplayOn(bayTypeKey,
				false);
	}

	private void engageSTANLD2Fingertip() {
		ApplicationLauncher.logger.warn("engageSTANLD2Fingertip: Entry");

		controlFingertip(ConstantBayPortNameMapping.SCT_NLT2_PORT_NAME_FINGER_TIP, CLOSE);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setPalletsLockedImageDisplayOn(bayTypeKey,
				true);
	}

	private void closeSTANLD2BayStopper1() {
		ApplicationLauncher.logger.warn("closeSTANLD2BayStopper : Entry");

		controlOutput(ConstantBayPortNameMapping.SCT_NLT2_PORT_NAME_STPR, CLOSE);
		ApplicationLauncher.logger.warn("closeSTANLD2BayStopper : bayTypeKey: " + bayTypeKey);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager()
				.updateBayExitStopper(ConstantConveyor.STA_NLD2_PP1_BAY_KEY, false);
	}

	private void openSTANLD2BayStopper1() {
		ApplicationLauncher.logger.warn("openSTANLD2BayStopper1 : Entry");

		controlOutput(ConstantBayPortNameMapping.SCT_NLT2_PORT_NAME_STPR, OPEN);
		ApplicationLauncher.logger.warn("openSTANLD2BayStopper1 : bayTypeKey: " + bayTypeKey);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager()
				.updateBayExitStopper(ConstantConveyor.STA_NLD2_PP1_BAY_KEY, true);
	}

	// S T A N L D B A Y
	// 1===================================================================================

	private void handleSTANLD1BayAction(BayActionType actionType) {
		switch (actionType) {
			case FINGERTIP_ENGAGE:
				engageSTANLD1Fingertip();
				break;
			case FINGERTIP_DISENGAGE:
				disengageSTANLD1Fingertip();
				break;
			case NO_ENTRY_ACTIVE:
				noEntryActiveSta1();
				break;
			case NO_ENTRY_INACTIVE:
				noEntryInActiveSta1();
				break;
			case HALT_PALLET_ACTIVE:
				haltPalletActiveSta1();
				break;
			case HALT_PALLET_INACTIVE:
				haltPalletInActiveSta1();
				break;
			case BLOCK_BAY_EXIT:
				closeSTANLD1BayStopper1();
				break;
			case BLOCK_BAY_EXIT2:
				closeSTANLD1BayStopper2();
				break;
			case RELEASE_METER_FROM_BAY:
				openSTANLD1BayStopper1();
				openSTANLD1BayStopper2();
				break;
			case DIVERTER_DOWN:
				openSTANLD1Diverter();
				break;
			case DIVERTER_UP:
				closeSTANLD1Diverter();
				break;
			case REFRESH:
				refreshSta1Bay();
				break;
			default:
				ApplicationLauncher.logger.warn("STANLD1: Unsupported action " + actionType);
		}
	}

	private void openSTANLD1Diverter() {
		ApplicationLauncher.logger.warn("openSTANLD1Diverter : Entry");

		controlOutput(ConstantBayPortNameMapping.OUT_AREA_DIVERTOR, OPEN);
	}

	private void closeSTANLD1Diverter() {
		ApplicationLauncher.logger.warn("closeSTANLD1Diverter : Entry");

		controlOutput(ConstantBayPortNameMapping.OUT_AREA_DIVERTOR, CLOSE);
	}

	private void disengageSTANLD1Fingertip() {
		ApplicationLauncher.logger.warn("disengageSTANLD1Fingertip: Entry");

		controlFingertip(ConstantBayPortNameMapping.SCT_NLT1_PORT_NAME_FINGER_TIP, OPEN);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setPalletsLockedImageDisplayOn(bayTypeKey,
				false);
	}

	private void engageSTANLD1Fingertip() {
		ApplicationLauncher.logger.warn("engageSTANLD1Fingertip: Entry");

		controlFingertip(ConstantBayPortNameMapping.SCT_NLT1_PORT_NAME_FINGER_TIP, CLOSE);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setPalletsLockedImageDisplayOn(bayTypeKey,
				true);
	}

	private void closeSTANLD1BayStopper1() {
		ApplicationLauncher.logger.warn("closeSTANLD1BayStopper : Entry");

		controlOutput(ConstantBayPortNameMapping.SCT_NLT1_PORT_NAME_STPR, CLOSE);
		ApplicationLauncher.logger.warn("closeSTANLD1BayStopper : bayTypeKey: " + bayTypeKey);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager()
				.updateBayExitStopper(ConstantConveyor.STA_NLD1_PP1_BAY_KEY, false);
	}

	private void openSTANLD1BayStopper1() {
		ApplicationLauncher.logger.warn("openSTANLD1BayStopper1 : Entry");

		controlOutput(ConstantBayPortNameMapping.SCT_NLT1_PORT_NAME_STPR, OPEN);
		ApplicationLauncher.logger.warn("openSTANLD1BayStopper1 : bayTypeKey: " + bayTypeKey);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager()
				.updateBayExitStopper(ConstantConveyor.STA_NLD1_PP1_BAY_KEY, true);
	}

	private void closeSTANLD1BayStopper2() {
		ApplicationLauncher.logger.warn("closeSTANLD1BayStopper : Entry");

		controlOutput(ConstantBayPortNameMapping.SCT_NLT1_PORT_NAME_STPR2, CLOSE);
	}

	private void openSTANLD1BayStopper2() {
		ApplicationLauncher.logger.warn("openSTANLD1BayStopper2 : Entry");

		controlOutput(ConstantBayPortNameMapping.SCT_NLT1_PORT_NAME_STPR2, OPEN);
	}

	// V E R I F I C A T I O N B A Y
	// ===================================================================================

	private void handleVerificationBayAction(BayActionType actionType) {
		switch (actionType) {
			case FINGERTIP_ENGAGE:
				engageVerificationFingertip();
				break;
			case FINGERTIP_DISENGAGE:
				disengageVerificationFingertip();
				break;
			case NO_ENTRY_ACTIVE:
				noEntryActiveVerific1();
				break;
			case NO_ENTRY_INACTIVE:
				noEntryInActiveVerific1();
				break;
			case HALT_PALLET_ACTIVE:
				haltPalletActiveVerific1();
				break;
			case HALT_PALLET_INACTIVE:
				haltPalletInActiveVerific1();
				break;
			case BLOCK_BAY_EXIT:
				closeVerificationBayStopper1();
				break;
			case BLOCK_BAY_EXIT2:
				closeVerificationBayStopper2();
				break;

			case RELEASE_METER_FROM_BAY:
				openVerificationBayStopper1();
				openVerificationBayStopper2();
				break;
			case DIVERTER_DOWN:
				openVerificationDiverter();
				break;
			case DIVERTER_UP:
				closeVerificationDiverter();
				break;
			case PALLETS_CLEARED:
				clearAllFlagsForPalletEntryInVerific1Bay();
				break;
			/*
			 * case PALLETS_BLOCK:
			 * blockPalletsInVerification1();
			 * break;
			 */
			case REFRESH:
				refreshVerific1Bay();
				break;
			default:
				ApplicationLauncher.logger.warn("Verification: Unsupported action " + actionType);
		}
	}

	private void clearAllFlagsForPalletEntryInVerific1Bay() {

		Verification.logger.info("BayActionHandler : clearAllFlagsForPalletEntryInVerific1Bay: Pallets Cleared");
		// ConstantConveyor.VERIFICATION_BAY_PALLETS_CLEARED = true;
		ConveyorDataManager.setVerific1PalletsAllCleared(true);
	}

	private void blockPalletsInVerification1() {

		Verification.logger.info("BayActionHandler : blockPalletsInVerification1: Pallets blocked");
		// ConstantConveyor.VERIFICATION_BAY_PALLETS_CLEARED = false;
		ConveyorDataManager.setVerific1PalletsAllCleared(false);
	}

	private void closeVerificationDiverter() {
		ApplicationLauncher.logger.warn("closeVerificationDiverter : Entry");

		controlOutput(ConstantBayPortNameMapping.VERIFIC_PORT_NAME_DIVERTOR_RELAY, CLOSE);
	}

	private void openVerificationDiverter() {
		ApplicationLauncher.logger.warn("openVerificationDiverter : Entry");

		controlOutput(ConstantBayPortNameMapping.VERIFIC_PORT_NAME_DIVERTOR_RELAY, OPEN);
	}

	private void disengageVerificationFingertip() {
		ApplicationLauncher.logger.warn("disengageVerificationFingertip: Entry");

		controlFingertip(ConstantBayPortNameMapping.VERIFIC_PORT_NAME_FINGER_TIP, OPEN);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setPalletsLockedImageDisplayOn(bayTypeKey,
				false);
	}

	private void engageVerificationFingertip() {
		ApplicationLauncher.logger.warn("engageVerificationFingertip: Entry");

		controlFingertip(ConstantBayPortNameMapping.VERIFIC_PORT_NAME_FINGER_TIP, CLOSE);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setPalletsLockedImageDisplayOn(bayTypeKey,
				true);
	}

	private void closeVerificationBayStopper1() {
		ApplicationLauncher.logger.warn("closeVerificationBayStopper : Entry");

		controlOutput(ConstantBayPortNameMapping.VERIFIC_PORT_NAME_STPR, CLOSE);
		ApplicationLauncher.logger.warn("closeVerificationBayStopper : bayTypeKey: " + bayTypeKey);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager()
				.updateBayExitStopper(ConstantConveyor.VERIFICATION_PP1_BAY_KEY, false);
	}

	private void openVerificationBayStopper1() {
		ApplicationLauncher.logger.warn("openVerificationBayStopper1 : Entry");

		controlOutput(ConstantBayPortNameMapping.VERIFIC_PORT_NAME_STPR, OPEN);
		ApplicationLauncher.logger.warn("openVerificationBayStopper1 : bayTypeKey: " + bayTypeKey);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager()
				.updateBayExitStopper(ConstantConveyor.VERIFICATION_PP1_BAY_KEY, true);
	}

	private void closeVerificationBayStopper2() {
		ApplicationLauncher.logger.warn("closeVerificationBayStopper2 : Entry");

		controlOutput(ConstantBayPortNameMapping.VERIFIC_PORT_NAME_STPR2, CLOSE);
	}

	private void openVerificationBayStopper2() {
		ApplicationLauncher.logger.warn("openVerificationBayStopper2 : Entry");

		controlOutput(ConstantBayPortNameMapping.VERIFIC_PORT_NAME_STPR2, OPEN);
	}

	// W A I T I N G B A Y
	// =============================================================================================

	private void handleWaitingBayAction(BayActionType actionType) {
		switch (actionType) {
			case BLOCK_BAY_EXIT:
				closeWaitingBayStopper();
				break;
			case RELEASE_METER_FROM_BAY:
				openWaitingBayStopper();
				break;
			case NO_ENTRY_ACTIVE:
				noEntryActiveVerific1Waiting();
				break;
			case NO_ENTRY_INACTIVE:
				noEntryInActiveVerific1Waiting();
				break;
			case HALT_PALLET_ACTIVE:
				haltPalletActiveVerific1Waiting();
				break;
			case HALT_PALLET_INACTIVE:
				haltPalletInActiveVerific1Waiting();
				break;
			case PALLETS_CLEARED:
				clearAllFlagsForPalletEntryInVerific1WaitingBay();
				break;
			/*
			 * case PALLETS_BLOCK:
			 * blockPalletsInVerific1WaitingBay();
			 * break;
			 */
			case REFRESH:
				refreshWaitingVerific1Bay();
				break;
			default:
				ApplicationLauncher.logger.warn("Waiting Bay: Unsupported action " + actionType);
		}
	}

	public void refreshCalibBay() {
		ApplicationLauncher.logger.info("refreshCalibBay: Entry ");

		refreshSinglePalletsInBay(ConstantConveyor.CALIBRATION_BAY_KEY);

		ApplicationLauncher.logger.info("refreshCalibBay: Exit ");
	}

	public void refreshFtBay() {
		ApplicationLauncher.logger.info("refreshFtBay: Entry ");

		refreshSinglePalletsInBay(ConstantConveyor.FT_BAY_KEY);

		ApplicationLauncher.logger.info("refreshFtBay: Exit ");

	}

	public void refreshHvBay() {
		ApplicationLauncher.logger.info("refreshHvBay: Entry ");

		refreshSinglePalletsInBay(ConstantConveyor.HV_BAY_KEY);

		ApplicationLauncher.logger.info("refreshHvBay: Exit ");

	}

	public void refreshRejectionBay() {
		ApplicationLauncher.logger.info("refreshRejectionBay: Entry ");

		refreshSinglePalletsInBay(ConstantConveyor.REJECTION_BAY_KEY);

		ApplicationLauncher.logger.info("refreshRejectionBay: Exit ");

	}

	public void refreshUnloadingBay() {
		ApplicationLauncher.logger.info("refreshUnloadingBay: Entry ");

		refreshSinglePalletsInBay(ConstantConveyor.UNLOADING_BAY_KEY);

		ApplicationLauncher.logger.info("refreshUnloadingBay: Exit ");

	}

	public void refreshIrBay() {
		ApplicationLauncher.logger.info("refreshIrBay: Entry ");

		refreshSinglePalletsInBay(ConstantConveyor.IR_BAY_KEY);

		ApplicationLauncher.logger.info("refreshIrBay: Exit ");

	}

	public void refreshWaitingVerific1Bay() {
		ApplicationLauncher.logger.info("refreshWaitingVerific1Bay: Entry ");

		refreshMultiplePalletInBay(ConstantConveyor.WAITING_BAY_KEY);

		ApplicationLauncher.logger.info("refreshWaitingVerific1Bay: Exit ");
	}

	public void refreshVerific1Bay() {
		ApplicationLauncher.logger.info("refreshVerific1Bay: Entry ");

		refreshMultiplePalletInBay(ConstantConveyor.VERIFICATION_BAY_KEY);

		ApplicationLauncher.logger.info("refreshVerific1Bay: Exit ");
	}

	public void refreshSta1Bay() {
		ApplicationLauncher.logger.info("refreshSta1Bay: Entry ");

		refreshMultiplePalletInBay(ConstantConveyor.STA_NLD1_BAY_KEY);

		ApplicationLauncher.logger.info("refreshSta1Bay: Exit ");
	}

	public void refreshSta2Bay() {
		ApplicationLauncher.logger.info("refreshSta2Bay: Entry ");

		refreshMultiplePalletInBay(ConstantConveyor.STA_NLD2_BAY_KEY);

		ApplicationLauncher.logger.info("refreshSta2Bay: Exit ");
	}

	/*
	 * public void refreshPalletsInWaitingVerific1Bay() {
	 * ApplicationLauncher.logger.info("refreshPalletsInWaitingVerific1Bay: Entry "
	 * );
	 * 
	 * 
	 * 
	 * ConveyorDeviceDataManagerController.getDashboardObject().
	 * removeAllPalletsFromWaitingVerific1Bays();
	 * ApplicationLauncher.logger.
	 * info("refreshPalletsInWaitingVerific1Bay: batch update: all WaitingVerific1 removed"
	 * );
	 * 
	 * BayUtils.delay(100);
	 * ApplicationLauncher.logger.
	 * debug("refreshPalletsInWaitingVerific1Bay: batch update : delay done :for removal: "
	 * );
	 * String bayKey = ConstantConveyor.WAITING_BAY_KEY;
	 * Map<Integer,String> meterListWithSerialNoMap = new HashMap<Integer,String>();
	 * Set<PalletMeter> palletMeterSetList = new HashSet<PalletMeter>();
	 * List<PalletManage> palletManageList =
	 * bayUtils.fetchPalletsByBayState(bayKey);
	 * ApplicationLauncher.logger.
	 * debug("refreshPalletsInWaitingVerific1Bay: refreshDashBoard: palletManageList size: "
	 * + palletManageList.size());
	 * boolean scannedPalletQrIdExist = false;
	 * for(PalletManage eachPalletManage : palletManageList ) {
	 * ApplicationLauncher.logger.
	 * debug("refreshPalletsInWaitingVerific1Bay: fetchPalletsByBayState: getPalletDistinctId:    "
	 * + eachPalletManage.getPalletDistinctId());
	 * //ApplicationLauncher.logger.
	 * debug("refreshDashBoard : fetchPalletsByBayState: palletQrId:    " +
	 * palletQrId);
	 * 
	 * if(eachPalletManage.getPalletDistinctId().contains(palletQrId)) {
	 * scannedPalletQrIdExist = true;
	 * ApplicationLauncher.logger.
	 * debug("refreshDashBoard : fetchPalletsByBayState: scannedPalletQrIdExist in fetch list"
	 * );
	 * }
	 * }
	 * String palletName = "";
	 * //ApplicationLauncher.logger.
	 * debug("refreshPalletsInWaitingVerific1Bay: batch update ");
	 * for(PalletManage eachPalletManage : palletManageList ) {
	 * meterListWithSerialNoMap.clear();
	 * //myPalletManage = myPalletManageList.get(0);
	 * palletMeterSetList = eachPalletManage.getPalletMeterList();
	 * List<PalletMeter> sortedPalletMeterList = palletMeterSetList.stream()
	 * .sorted(Comparator.comparingInt(PalletMeter::getRackPositionNo)).collect(
	 * Collectors.toList());
	 * for(PalletMeter eachPalletMeter : palletMeterSetList){
	 * meterListWithSerialNoMap.put(eachPalletMeter.getRackPositionNo(),
	 * eachPalletMeter.getMeterSerialNo());
	 * }
	 * //ConveyorDeviceDataManagerController.getDashboardObject().
	 * removePalletFromBay(selectedBayTypeKey);
	 * palletName = eachPalletManage.getPalletQrId();
	 * ApplicationLauncher.logger.
	 * debug("refreshPalletsInWaitingVerific1Bay: batch update : palletName: " +
	 * palletName);
	 * 
	 * //ConveyorDeviceDataManagerController.getDashboardObject().
	 * addPalletToFirstAvailableVerificationBay(palletName,
	 * meterListWithSerialNoMap);
	 * ConveyorDeviceDataManagerController.getDashboardObject().
	 * addPalletToFirstAvailableWaitingBay(palletName, meterListWithSerialNoMap);
	 * ApplicationLauncher.logger.
	 * info("refreshPalletsInWaitingVerific1Bay: batch update: WaitingVerific1 added"
	 * );
	 * 
	 * BayUtils.delay(100);
	 * Map<Integer, MeterStatus> statusMap = new HashMap<>();
	 * statusMap.put(1, MeterStatus.IDLE);
	 * statusMap.put(2, MeterStatus.IDLE);
	 * statusMap.put(3, MeterStatus.IDLE);
	 * statusMap.put(4, MeterStatus.IDLE);
	 * statusMap.put(5, MeterStatus.IDLE);
	 * statusMap.put(6, MeterStatus.IDLE);
	 * 
	 * Map<Integer, String> errorCodeMap = new HashMap<>();
	 * 
	 * errorCodeMap.put(1, "");
	 * errorCodeMap.put(2, "");
	 * errorCodeMap.put(3, "");
	 * errorCodeMap.put(4, "");
	 * errorCodeMap.put(5, "");
	 * errorCodeMap.put(6, "");
	 * //Platform.runLater(()->{
	 * ApplicationLauncher.logger.
	 * debug("refreshPalletsInWaitingVerific1Bay: batch update : updateDashBoardPalletStatus : "
	 * + palletName);
	 * ConveyorDeviceDataManagerController.getDashboardObject().
	 * updateDashBoardPalletStatus(palletName, statusMap, errorCodeMap);
	 * 
	 * BayUtils.delay(50);
	 * 
	 * ApplicationLauncher.logger.
	 * debug("refreshPalletsInWaitingVerific1Bay: batch update : delay done :palletName: "
	 * + palletName);
	 * 
	 * }
	 * 
	 * ApplicationLauncher.logger.info("refreshWaitingVerific1Bay: Exit ");
	 * }
	 */

	public void refreshMultiplePalletInBay(String bayKey) {
		ApplicationLauncher.logger.info("refreshMultiplePalletInBay: Entry: " + bayKey);

		if (bayKey.startsWith(ConstantConveyor.WAITING_BAY_KEY)) {
			ConveyorDataManager.getDashboardObject().removeAllPalletsFromWaitingVerific1Bays();
			ApplicationLauncher.logger.info("refreshMultiplePalletInBay: all WaitingVerific1 removed");
		} else if (bayKey.startsWith(ConstantConveyor.VERIFICATION_BAY_KEY)) {
			ConveyorDataManager.getDashboardObject().removeAllPalletsFromVerificationBays();
			ApplicationLauncher.logger.info("refreshMultiplePalletInBay: all Verific1 removed");
		}
		if (bayKey.startsWith(ConstantConveyor.STA_NLD1_BAY_KEY)) {
			ConveyorDataManager.getDashboardObject().removeAllPalletsFromSta1Bays();
			ApplicationLauncher.logger.info("refreshMultiplePalletInBay: all STA1 removed");
		}
		if (bayKey.startsWith(ConstantConveyor.STA_NLD2_BAY_KEY)) {
			ConveyorDataManager.getDashboardObject().removeAllPalletsFromSta2Bays();
			ApplicationLauncher.logger.info("refreshMultiplePalletInBay: all STA2 removed");
		}

		BayUtils.delay(100);
		ApplicationLauncher.logger.debug("refreshMultiplePalletInBay: delay done :for removal: ");
		// String bayKey = ConstantConveyor.WAITING_BAY_KEY;
		Map<Integer, String> meterListWithSerialNoMap = new HashMap<Integer, String>();
		Set<PalletMeter> palletMeterSetList = new HashSet<PalletMeter>();
		List<PalletManage> palletManageList = bayUtils.fetchPalletsByBayState(bayKey);
		ApplicationLauncher.logger
				.debug("refreshMultiplePalletInBay: palletManageList size: " + palletManageList.size());
		// boolean scannedPalletQrIdExist = false;
		// for(PalletManage eachPalletManage : palletManageList ) {
		// ApplicationLauncher.logger.debug("refreshMultiplePalletInBay:
		// fetchPalletsByBayState: getPalletDistinctId: " +
		// eachPalletManage.getPalletDistinctId());
		// ApplicationLauncher.logger.debug("refreshDashBoard : fetchPalletsByBayState:
		// palletQrId: " + palletQrId);

		/*
		 * if(eachPalletManage.getPalletDistinctId().contains(palletQrId)) {
		 * scannedPalletQrIdExist = true;
		 * ApplicationLauncher.logger.
		 * debug("refreshDashBoard : fetchPalletsByBayState: scannedPalletQrIdExist in fetch list"
		 * );
		 * }
		 */
		// }
		String palletName = "";
		// ApplicationLauncher.logger.debug("refreshMultiplePalletInBay: batch update
		// ");
		int noOfPalletsAcceptedInBay = 1;
		if (bayKey.equals(ConstantConveyor.VERIFICATION_BAY_KEY)) {
			noOfPalletsAcceptedInBay = DeviceDataManagerController.getConveyorConfigParsedKey()
					.getMaxNoOfPalletsInVerific1();
			ApplicationLauncher.logger.debug("refreshMultiplePalletInBay: verific");
		} else if (bayKey.equals(ConstantConveyor.STA_NLD1_BAY_KEY)) {
			ApplicationLauncher.logger.debug("refreshMultiplePalletInBay: sta1");
			noOfPalletsAcceptedInBay = DeviceDataManagerController.getConveyorConfigParsedKey()
					.getMaxNoOfPalletsInStaNld1();
		} else if (bayKey.equals(ConstantConveyor.STA_NLD2_BAY_KEY)) {
			ApplicationLauncher.logger.debug("refreshMultiplePalletInBay: sta2");
			noOfPalletsAcceptedInBay = DeviceDataManagerController.getConveyorConfigParsedKey()
					.getMaxNoOfPalletsInStaNld2();
		} else if (bayKey.equals(ConstantConveyor.WAITING_BAY_KEY)) {
			ApplicationLauncher.logger.debug("refreshMultiplePalletInBay: waiting bay");
			noOfPalletsAcceptedInBay = DeviceDataManagerController.getConveyorConfigParsedKey()
					.getMaxNoOfPalletsInWaitingVerific1();
		}

		ApplicationLauncher.logger
				.debug("refreshMultiplePalletInBay: noOfPalletsAcceptedInBay: " + noOfPalletsAcceptedInBay);
		List<String> palletDistinctIdList = new ArrayList<String>();
		for (PalletManage eachPalletManage : palletManageList) {
			ApplicationLauncher.logger
					.debug("refreshMultiplePalletInBay: fetchPalletsByBayState: getPalletDistinctId:    "
							+ eachPalletManage.getPalletDistinctId());
			palletDistinctIdList.add(eachPalletManage.getPalletDistinctId());
			// ApplicationLauncher.logger.debug("refreshDashBoard : fetchPalletsByBayState:
			// palletQrId: " + palletQrId);

			/*
			 * if(eachPalletManage.getPalletDistinctId().contains(palletQrId)) {
			 * scannedPalletQrIdExist = true;
			 * ApplicationLauncher.logger.
			 * debug("refreshDashBoard : fetchPalletsByBayState: scannedPalletQrIdExist in fetch list"
			 * );
			 * }
			 */
		}

		if (palletManageList.size() > noOfPalletsAcceptedInBay) {
			Platform.runLater(() -> {
				String header = bayKey + " Bay : More pallets found active.Kindly update the pallets bay state\n"
						+ String.join("\n", palletDistinctIdList);
				String title = bayKey + " Bay-More Pallets active";

				YesNoDialogFX dialog = new YesNoDialogFX(title, header, YesNoDialogFX.MessageType.WARNING);
				dialog.resultProperty().addListener((obs, oldVal, newVal) -> {
					if (Boolean.TRUE.equals(newVal)) {
						ApplicationLauncher.logger.debug("refreshMultiplePalletInBay: prompt user hit: YES");
					} else {
						ApplicationLauncher.logger.debug("refreshMultiplePalletInBay: prompt user hit: NO");
					}
				});
				dialog.show(); // This will NOT block the JavaFX thread
				ApplicationLauncher.logger.debug("refreshMultiplePalletInBay: Prompt shown, returning immediately");

			});
		} else {

			for (PalletManage eachPalletManage : palletManageList) {
				meterListWithSerialNoMap.clear();
				// myPalletManage = myPalletManageList.get(0);
				palletMeterSetList = eachPalletManage.getPalletMeterList();
				List<PalletMeter> sortedPalletMeterList = palletMeterSetList.stream()
						.sorted(Comparator.comparingInt(PalletMeter::getRackPositionNo)).collect(Collectors.toList());
				for (PalletMeter eachPalletMeter : palletMeterSetList) {
					meterListWithSerialNoMap.put(eachPalletMeter.getRackPositionNo(),
							eachPalletMeter.getMeterSerialNo());
				}
				// ConveyorDeviceDataManagerController.getDashboardObject().removePalletFromBay(selectedBayTypeKey);
				palletName = eachPalletManage.getPalletQrId();
				ApplicationLauncher.logger.debug("refreshMultiplePalletInBay: palletName: " + palletName);

				// ConveyorDeviceDataManagerController.getDashboardObject().addPalletToFirstAvailableVerificationBay(palletName,
				// meterListWithSerialNoMap);
				// ConveyorDeviceDataManagerController.getDashboardObject().addPalletToFirstAvailableWaitingBay(palletName,
				// meterListWithSerialNoMap);

				if (bayKey.equals(ConstantConveyor.VERIFICATION_BAY_KEY)) {

					ApplicationLauncher.logger.debug("refreshMultiplePalletInBay: verific: adding");
					ConveyorDataManager.getDashboardObject().addPalletToFirstAvailableVerificationBay(palletName,
							meterListWithSerialNoMap);

				} else if (bayKey.equals(ConstantConveyor.STA_NLD1_BAY_KEY)) {
					ApplicationLauncher.logger.debug("refreshMultiplePalletInBay: sta1: adding");
					ConveyorDataManager.getDashboardObject().addPalletToFirstAvailableSta1Bay(palletName,
							meterListWithSerialNoMap);

				} else if (bayKey.equals(ConstantConveyor.STA_NLD2_BAY_KEY)) {
					ApplicationLauncher.logger.debug("refreshMultiplePalletInBay: sta2: adding");
					ConveyorDataManager.getDashboardObject().addPalletToFirstAvailableSta2Bay(palletName,
							meterListWithSerialNoMap);

				} else if (bayKey.equals(ConstantConveyor.WAITING_BAY_KEY)) {
					ApplicationLauncher.logger.debug("refreshMultiplePalletInBay: waiting bay: adding");
					ConveyorDataManager.getDashboardObject().addPalletToFirstAvailableWaitingBay(palletName,
							meterListWithSerialNoMap);

				}
				// ApplicationLauncher.logger.info("refreshMultiplePalletInBay: WaitingVerific1
				// added");
				if (bayKey.startsWith(ConstantConveyor.WAITING_BAY_KEY)) {
					BayUtils.delay(100);
					Map<Integer, MeterStatus> statusMap = new HashMap<>();
					statusMap.put(1, MeterStatus.IDLE);
					statusMap.put(2, MeterStatus.IDLE);
					statusMap.put(3, MeterStatus.IDLE);
					statusMap.put(4, MeterStatus.IDLE);
					statusMap.put(5, MeterStatus.IDLE);
					statusMap.put(6, MeterStatus.IDLE);

					Map<Integer, String> errorCodeMap = new HashMap<>();

					errorCodeMap.put(1, "");
					errorCodeMap.put(2, "");
					errorCodeMap.put(3, "");
					errorCodeMap.put(4, "");
					errorCodeMap.put(5, "");
					errorCodeMap.put(6, "");
					// Platform.runLater(()->{
					ApplicationLauncher.logger.debug("refreshMultiplePalletInBay: palletName : " + palletName);
					ConveyorDataManager.getDashboardObject().updateDashBoardPalletStatus(palletName, statusMap,
							errorCodeMap);

					BayUtils.delay(50);

					ApplicationLauncher.logger
							.debug("refreshMultiplePalletInBay: delay done :palletName: " + palletName);
				}

			}
		}

		ApplicationLauncher.logger.info("refreshMultiplePalletInBay: Exit: " + bayKey);
	}

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep2 :InterruptedException:" + e.getMessage());
		}

	}

	public void refreshSinglePalletsInBay(String myBayKey) {
		ApplicationLauncher.logger.info("refreshSinglePalletsInBay: Entry : " + myBayKey);

		// String myBayKey = ConstantConveyor.CALIBRATION_BAY_KEY;
		ConveyorDataManager.getDashboardObject().removePalletFromBay(myBayKey);
		ApplicationLauncher.logger.info("refreshSinglePalletsInBay: bay removed :" + myBayKey);

		BayUtils.delay(100);
		ApplicationLauncher.logger.debug("refreshSinglePalletsInBay: delay done :for removal: " + myBayKey);

		Map<Integer, String> meterListWithSerialNoMap = new HashMap<Integer, String>();
		Set<PalletMeter> palletMeterSetList = new HashSet<PalletMeter>();
		List<PalletManage> palletManageList = bayUtils.fetchTillLastWeekPalletsByBayState(myBayKey);
		if ((myBayKey.equals(ConstantConveyor.REJECTION_BAY_KEY))
				|| (myBayKey.equals(ConstantConveyor.UNLOADING_BAY_KEY))) {
			palletManageList = MySqlServiceManager.getPalletManageService()
					.findTopByPresentBayKeyAndPalletActiveFalseToday(myBayKey);

		}
		ApplicationLauncher.logger.debug(
				"refreshSinglePalletsInBay: refreshDashBoard: palletManageList size: " + palletManageList.size());
		boolean scannedPalletQrIdExist = false;
		List<String> palletDistinctIdList = new ArrayList<String>();
		for (PalletManage eachPalletManage : palletManageList) {
			ApplicationLauncher.logger
					.debug("refreshSinglePalletsInBay: fetchPalletsByBayState: getPalletDistinctId:    "
							+ eachPalletManage.getPalletDistinctId());
			palletDistinctIdList.add(eachPalletManage.getPalletDistinctId());
			// ApplicationLauncher.logger.debug("refreshDashBoard : fetchPalletsByBayState:
			// palletQrId: " + palletQrId);

			/*
			 * if(eachPalletManage.getPalletDistinctId().contains(palletQrId)) {
			 * scannedPalletQrIdExist = true;
			 * ApplicationLauncher.logger.
			 * debug("refreshDashBoard : fetchPalletsByBayState: scannedPalletQrIdExist in fetch list"
			 * );
			 * }
			 */
		}
		String palletName = "";
		// ApplicationLauncher.logger.debug("refreshSinglePalletsInBay: batch update ");
		if (palletManageList.size() > 1) {
			Platform.runLater(() -> {
				String header = myBayKey + " Bay : Multiple pallets found active.Kindly update the pallets bay state\n"
						+ String.join("\n", palletDistinctIdList);
				String title = myBayKey + " Bay-Multiple Pallets active";

				YesNoDialogFX dialog = new YesNoDialogFX(title, header, YesNoDialogFX.MessageType.WARNING);
				dialog.resultProperty().addListener((obs, oldVal, newVal) -> {
					if (Boolean.TRUE.equals(newVal)) {
						ApplicationLauncher.logger.debug("refreshSinglePalletsInBay: prompt user hit: YES");
					} else {
						ApplicationLauncher.logger.debug("refreshSinglePalletsInBay: prompt user hit: NO");
					}
				});
				dialog.show(); // This will NOT block the JavaFX thread
				ApplicationLauncher.logger.debug("refreshSinglePalletsInBay: Prompt shown, returning immediately");

			});
		} else {
			for (PalletManage eachPalletManage : palletManageList) {
				meterListWithSerialNoMap.clear();
				// myPalletManage = myPalletManageList.get(0);
				palletMeterSetList = eachPalletManage.getPalletMeterList();
				List<PalletMeter> sortedPalletMeterList = palletMeterSetList.stream()
						.sorted(Comparator.comparingInt(PalletMeter::getRackPositionNo)).collect(Collectors.toList());
				for (PalletMeter eachPalletMeter : palletMeterSetList) {
					meterListWithSerialNoMap.put(eachPalletMeter.getRackPositionNo(),
							eachPalletMeter.getMeterSerialNo());
				}
				// ConveyorDeviceDataManagerController.getDashboardObject().removePalletFromBay(selectedBayTypeKey);
				palletName = eachPalletManage.getPalletQrId();
				ApplicationLauncher.logger.debug("refreshSinglePalletsInBay: palletName: " + palletName);
				// ApplicationLauncher.logger.debug("refreshSinglePalletsInBay: batch update :
				// palletName: " + palletName);
				// ConveyorDeviceDataManagerController.getDashboardObject().addPalletToFirstAvailableVerificationBay(palletName,
				// meterListWithSerialNoMap);
				ConveyorDataManager.getDashboardObject().addNewPalletViewDashboard(myBayKey, palletName,
						meterListWithSerialNoMap);
				ApplicationLauncher.logger
						.info("refreshSinglePalletsInBay: added : " + myBayKey + " , palletName: " + palletName);

				BayUtils.delay(100);
				Map<Integer, MeterStatus> statusMap = new HashMap<>();
				statusMap.put(1, MeterStatus.IDLE);
				statusMap.put(2, MeterStatus.IDLE);
				statusMap.put(3, MeterStatus.IDLE);
				statusMap.put(4, MeterStatus.IDLE);
				statusMap.put(5, MeterStatus.IDLE);
				statusMap.put(6, MeterStatus.IDLE);

				Map<Integer, String> errorCodeMap = new HashMap<>();

				errorCodeMap.put(1, "");
				errorCodeMap.put(2, "");
				errorCodeMap.put(3, "");
				errorCodeMap.put(4, "");
				errorCodeMap.put(5, "");
				errorCodeMap.put(6, "");
				// Platform.runLater(()->{
				ApplicationLauncher.logger
						.debug("refreshSinglePalletsInBay : updateDashBoardPalletStatus : " + palletName);
				ConveyorDataManager.getDashboardObject().updateDashBoardPalletStatus(palletName, statusMap,
						errorCodeMap);

				BayUtils.delay(50);

				ApplicationLauncher.logger.debug("refreshSinglePalletsInBay : delay done :palletName: " + palletName);

			}
		}

		ApplicationLauncher.logger.info("refreshSinglePalletsInBay: Exit : " + myBayKey);
	}

	private void clearAllFlagsForPalletEntryInVerific1WaitingBay() {
		ApplicationLauncher.logger.warn("clearAllFlagsForPalletEntryInVerific1WaitingBay : Entry");

		ConveyorDataManager.setWaitingVerific1BayPalletsAllCleared(true);
	}

	private void blockPalletsInVerific1WaitingBay() {
		ApplicationLauncher.logger.warn("blockPalletsInVerific1WaitingBay : Entry");

		ConveyorDataManager.setWaitingVerific1BayPalletsAllCleared(false);
	}

	private void closeWaitingBayStopper() {
		ApplicationLauncher.logger.warn("closeWaitingBayStopper : Entry");

		controlOutput(ConstantBayPortNameMapping.WAITING_PORT_NAME_STPR, CLOSE);
		ApplicationLauncher.logger.warn("closeWaitingBayStopper : bayTypeKey: " + bayTypeKey);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager()
				.updateBayExitStopper(ConstantConveyor.WAITING_PP1_BAY_KEY, false);
	}

	private void openWaitingBayStopper() {
		ApplicationLauncher.logger.warn("openWaitingBayStopper : Entry");

		controlOutput(ConstantBayPortNameMapping.WAITING_PORT_NAME_STPR, OPEN);
		ApplicationLauncher.logger.warn("openWaitingBayStopper : bayTypeKey: " + bayTypeKey);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager()
				.updateBayExitStopper(ConstantConveyor.WAITING_PP1_BAY_KEY, true);
	}

	// C A L I B R A T I O N B A Y
	// ======================================================================================

	private void handleCalibrationAction(BayActionType actionType) {
		switch (actionType) {
			case FINGERTIP_ENGAGE:
				engageCalibrationFingertip();
				break;
			case FINGERTIP_DISENGAGE:
				disengageCalibrationFingertip();
				break;
			case NO_ENTRY_ACTIVE:
				noEntryActiveCalib();
				break;
			case NO_ENTRY_INACTIVE:
				noEntryInActiveCalib();
				break;
			case HALT_PALLET_ACTIVE:
				haltPalletActiveCalib();
				break;
			case HALT_PALLET_INACTIVE:
				haltPalletInActiveCalib();
				break;
			case SOURCE_START:
				startCalibrationSource();
				break;
			case CURRENT_STOP:
				currentStopCalibrationSourcer();
			case SOURCE_STOP:
				stopCalibrationSource();
				break;
			case MAIN_CT:
				makeCalibrationMainCT();
				break;
			case NEUTRAL_CT:
				makeCalibrationNeutralCT();
				break;
			case BLOCK_BAY_EXIT:
				closeCalibrationBayStopper();
				break;
			case RELEASE_METER_FROM_BAY:
				openCalibrationBayStopper();
				break;
			case REFRESH:
				refreshCalibBay();
				break;
			default:
				ApplicationLauncher.logger.warn("Calibration: Unsupported action " + actionType);
		}
	}

	private void disengageCalibrationFingertip() {
		ApplicationLauncher.logger.warn("disengageCalibrationFingertip: Entry");

		controlFingertip(ConstantBayPortNameMapping.CALIB_PORT_NAME_FINGER_TIP, OPEN);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setPalletsLockedImageDisplayOn(bayTypeKey,
				false);
	}

	private void engageCalibrationFingertip() {
		ApplicationLauncher.logger.warn("engageCalibrationFingertip: Entry");

		controlFingertip(ConstantBayPortNameMapping.CALIB_PORT_NAME_FINGER_TIP, CLOSE);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setPalletsLockedImageDisplayOn(bayTypeKey,
				true);
	}

	private void noEntryActiveCalib() {
		ApplicationLauncher.logger.warn("noEntryActiveCalib: Entry");
		ConveyorDataManager.setNoEntryActiveInCalib(true);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setNoEntryImageDisplayOn(bayTypeKey, true);
	}

	private void noEntryInActiveCalib() {
		ApplicationLauncher.logger.warn("noEntryInActiveCalib: Entry");
		ConveyorDataManager.setNoEntryActiveInCalib(false);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setNoEntryImageDisplayOn(bayTypeKey, false);
	}

	private void noEntryActiveFt() {
		ApplicationLauncher.logger.warn("noEntryActiveFt: Entry");
		ConveyorDataManager.setNoEntryActiveInFt(true);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setNoEntryImageDisplayOn(bayTypeKey, true);
	}

	private void noEntryInActiveFt() {
		ApplicationLauncher.logger.warn("noEntryInActiveFt: Entry");
		ConveyorDataManager.setNoEntryActiveInFt(false);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setNoEntryImageDisplayOn(bayTypeKey, false);
	}

	private void noEntryActiveHv() {
		ApplicationLauncher.logger.warn("noEntryActiveHv: Entry");
		ConveyorDataManager.setNoEntryActiveInHv(true);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setNoEntryImageDisplayOn(bayTypeKey, true);
	}

	private void noEntryInActiveHv() {
		ApplicationLauncher.logger.warn("noEntryInActiveHv: Entry");
		ConveyorDataManager.setNoEntryActiveInHv(false);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setNoEntryImageDisplayOn(bayTypeKey, false);
	}

	private void noEntryActiveIr() {
		ApplicationLauncher.logger.warn("noEntryActiveIr: Entry");
		ConveyorDataManager.setNoEntryActiveInIr(true);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setNoEntryImageDisplayOn(bayTypeKey, true);
	}

	private void noEntryInActiveIr() {
		ApplicationLauncher.logger.warn("noEntryInActiveIr: Entry");
		ConveyorDataManager.setNoEntryActiveInIr(false);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setNoEntryImageDisplayOn(bayTypeKey, false);
	}

	private void noEntryActiveVerific1Waiting() {
		ApplicationLauncher.logger.warn("noEntryActiveVerific1Waiting: Entry");
		ConveyorDataManager.setNoEntryActiveInVerific1Waiting(true);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setNoEntryImageDisplayOn(bayTypeKey, true);
	}

	private void noEntryInActiveVerific1Waiting() {
		ApplicationLauncher.logger.warn("noEntryInActiveVerific1Waiting: Entry");
		ConveyorDataManager.setNoEntryActiveInVerific1Waiting(false);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setNoEntryImageDisplayOn(bayTypeKey, false);
	}

	private void noEntryActiveVerific1() {
		ApplicationLauncher.logger.warn("noEntryActiveVerific1: Entry");
		ConveyorDataManager.setNoEntryActiveInVerific1(true);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setNoEntryImageDisplayOn(bayTypeKey, true);
	}

	private void noEntryInActiveVerific1() {
		ApplicationLauncher.logger.warn("noEntryInActiveVerific1: Entry");
		ConveyorDataManager.setNoEntryActiveInVerific1(false);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setNoEntryImageDisplayOn(bayTypeKey, false);
	}

	private void noEntryActiveRejection() {
		ApplicationLauncher.logger.warn("noEntryActiveRejection: Entry");
		ConveyorDataManager.setNoEntryActiveInRejection(true);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setNoEntryImageDisplayOn(bayTypeKey, true);
	}

	private void noEntryInActiveRejection() {
		ApplicationLauncher.logger.warn("noEntryInActiveRejection: Entry");
		ConveyorDataManager.setNoEntryActiveInRejection(false);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setNoEntryImageDisplayOn(bayTypeKey, false);
	}

	private void noEntryActiveUnloading() {
		ApplicationLauncher.logger.warn("noEntryActiveUnloading: Entry");
		ConveyorDataManager.setNoEntryActiveInUnloading(true);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setNoEntryImageDisplayOn(bayTypeKey, true);
	}

	private void noEntryInActiveUnloading() {
		ApplicationLauncher.logger.warn("noEntryInActiveUnloading: Entry");
		ConveyorDataManager.setNoEntryActiveInUnloading(false);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setNoEntryImageDisplayOn(bayTypeKey, false);
	}

	private void noEntryActiveSta1() {
		ApplicationLauncher.logger.warn("noEntryActiveSta1: Entry");
		ConveyorDataManager.setNoEntryActiveInSta1(true);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setNoEntryImageDisplayOn(bayTypeKey, true);
	}

	private void noEntryInActiveSta1() {
		ApplicationLauncher.logger.warn("noEntryInActiveSta1: Entry");
		ConveyorDataManager.setNoEntryActiveInSta1(false);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setNoEntryImageDisplayOn(bayTypeKey, false);
	}

	private void noEntryActiveSta2() {
		ApplicationLauncher.logger.warn("noEntryActiveSta2: Entry");
		ConveyorDataManager.setNoEntryActiveInSta2(true);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setNoEntryImageDisplayOn(bayTypeKey, true);
	}

	private void noEntryInActiveSta2() {
		ApplicationLauncher.logger.warn("noEntryInActiveSta2: Entry");
		ConveyorDataManager.setNoEntryActiveInSta2(false);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setNoEntryImageDisplayOn(bayTypeKey, false);
	}

	private void haltPalletActiveFt() {
		ApplicationLauncher.logger.warn("haltPalletActiveFt: Entry");
		ConveyorDataManager.setHaltPalletActiveInFt(true);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setHaltImageDisplayOn(bayTypeKey, true);
	}

	private void haltPalletInActiveFt() {
		ApplicationLauncher.logger.warn("haltPalletInActiveFt: Entry");
		ConveyorDataManager.setHaltPalletActiveInFt(false);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setHaltImageDisplayOn(bayTypeKey, false);
	}

	private void haltPalletActiveHv() {
		ApplicationLauncher.logger.warn("haltPalletActiveHv: Entry");
		ConveyorDataManager.setHaltPalletActiveInHv(true);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setHaltImageDisplayOn(bayTypeKey, true);
	}

	private void haltPalletInActiveHv() {
		ApplicationLauncher.logger.warn("haltPalletInActiveHv: Entry");
		ConveyorDataManager.setHaltPalletActiveInHv(false);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setHaltImageDisplayOn(bayTypeKey, false);
	}

	private void haltPalletActiveIr() {
		ApplicationLauncher.logger.warn("haltPalletActiveIr: Entry");
		ConveyorDataManager.setHaltPalletActiveInIr(true);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setHaltImageDisplayOn(bayTypeKey, true);
	}

	private void haltPalletInActiveIr() {
		ApplicationLauncher.logger.warn("haltPalletInActiveIr: Entry");
		ConveyorDataManager.setHaltPalletActiveInIr(false);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setHaltImageDisplayOn(bayTypeKey, false);
	}

	private void haltPalletActiveCalib() {
		ApplicationLauncher.logger.warn("haltPalletActiveCalib: Entry");
		ConveyorDataManager.setHaltPalletActiveInCalib(true);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setHaltImageDisplayOn(bayTypeKey, true);
	}

	private void haltPalletInActiveCalib() {
		ApplicationLauncher.logger.warn("haltPalletInActiveCalib: Entry");
		ConveyorDataManager.setHaltPalletActiveInCalib(false);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setHaltImageDisplayOn(bayTypeKey, false);
	}

	private void haltPalletActiveVerific1Waiting() {
		ApplicationLauncher.logger.warn("haltPalletActiveVerific1Waiting: Entry");
		ConveyorDataManager.setHaltPalletActiveInVerific1Waiting(true);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setHaltImageDisplayOn(bayTypeKey, true);
	}

	private void haltPalletInActiveVerific1Waiting() {
		ApplicationLauncher.logger.warn("haltPalletInActiveVerific1Waiting: Entry");
		ConveyorDataManager.setHaltPalletActiveInVerific1Waiting(false);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setHaltImageDisplayOn(bayTypeKey, false);
	}

	private void haltPalletActiveVerific1() {
		ApplicationLauncher.logger.warn("haltPalletActiveVerific1: Entry");
		ConveyorDataManager.setHaltPalletActiveInVerific1(true);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setHaltImageDisplayOn(bayTypeKey, true);
	}

	private void haltPalletInActiveVerific1() {
		ApplicationLauncher.logger.warn("haltPalletInActiveVerific1: Entry");
		ConveyorDataManager.setHaltPalletActiveInVerific1(false);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setHaltImageDisplayOn(bayTypeKey, false);
	}

	private void haltPalletActiveSta1() {
		ApplicationLauncher.logger.warn("haltPalletActiveSta1: Entry");
		ConveyorDataManager.setHaltPalletActiveInSta1(true);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setHaltImageDisplayOn(bayTypeKey, true);
	}

	private void haltPalletInActiveSta1() {
		ApplicationLauncher.logger.warn("haltPalletInActiveSta1: Entry");
		ConveyorDataManager.setHaltPalletActiveInSta1(false);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setHaltImageDisplayOn(bayTypeKey, false);
	}

	private void haltPalletActiveSta2() {
		ApplicationLauncher.logger.warn("haltPalletActiveSta2: Entry");
		ConveyorDataManager.setHaltPalletActiveInSta2(true);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setHaltImageDisplayOn(bayTypeKey, true);
	}

	private void haltPalletInActiveSta2() {
		ApplicationLauncher.logger.warn("haltPalletInActiveSta2: Entry");
		ConveyorDataManager.setHaltPalletActiveInSta2(false);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setHaltImageDisplayOn(bayTypeKey, false);
	}

	private void closeCalibrationBayStopper() {
		ApplicationLauncher.logger.warn("closeCalibrationBayStopper : Entry");

		controlOutput(ConstantBayPortNameMapping.CALIB_PORT_NAME_STPR, CLOSE);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayExitStopper(bayTypeKey, false);
	}

	private void openCalibrationBayStopper() {
		ApplicationLauncher.logger.warn("openCalibrationBayStopper : Entry");

		controlOutput(ConstantBayPortNameMapping.CALIB_PORT_NAME_STPR, OPEN);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayExitStopper(bayTypeKey, true);
	}

	// public void startCalibrationSource() {
	// ApplicationLauncher.logger.warn("startCalibrationSource : Entry");
	//
	// controlOutput(ConstantBayPortNameMapping.CALIB_PORT_NAME_BOFA_240V_VOLTAGE_CURRENT_START,
	// CLOSE);
	// }

	public void startCalibrationSource() {
		ApplicationLauncher.logger.warn("startCalibrationSource : Entry");

		controlOutput(ConstantBayPortNameMapping.CALIB_PORT_NAME_BOFA_40V_VOLTAGE_CURRENT_START, CLOSE);

		// 45 sec delay - Capacitor Charging Time
		BayUtils.delay(5000);
		BayUtils.delay(5000);
		BayUtils.delay(5000);
		BayUtils.delay(5000);
		BayUtils.delay(5000);
		BayUtils.delay(5000);
		BayUtils.delay(5000);
		BayUtils.delay(5000);
		BayUtils.delay(5000);

		controlOutput(ConstantBayPortNameMapping.CALIB_PORT_NAME_BOFA_VOLTAGE_CURRENT_STOP, CLOSE);

		BayUtils.delay(5000);

		controlOutput(ConstantBayPortNameMapping.CALIB_PORT_NAME_BOFA_240V_VOLTAGE_CURRENT_START, CLOSE);
	}

	private void currentStopCalibrationSourcer() {
		ApplicationLauncher.logger.warn("currentStopCalibrationSourcer : Entry");

		controlOutput(ConstantBayPortNameMapping.CALIB_PORT_NAME_BOFA_CURRENT_ONLY_STOP, CLOSE);
	}

	public void stopCalibrationSource() {
		ApplicationLauncher.logger.warn("stopCalibrationSource : Entry");

		controlOutput(ConstantBayPortNameMapping.CALIB_PORT_NAME_BOFA_VOLTAGE_CURRENT_STOP, CLOSE);
	}

	public void makeCalibrationMainCT() {
		ApplicationLauncher.logger.warn("makeCalibrationMainCT : Entry");

		// Break CT
		controlOutput(ConstantBayPortNameMapping.CALIB_PORT_NAME_BREAK_CT, CLOSE);
		BayUtils.delay(500); // This is a blocking call
		controlOutput(ConstantBayPortNameMapping.CALIB_PORT_NAME_BREAK_CT, OPEN);
		// Make Main CT
		controlOutput(ConstantBayPortNameMapping.CALIB_PORT_NAME_MAIN_CT_MAKE, CLOSE);
		BayUtils.delay(500); // This is a blocking call
		controlOutput(ConstantBayPortNameMapping.CALIB_PORT_NAME_MAIN_CT_MAKE, OPEN);

	}

	private void makeCalibrationNeutralCT() {
		ApplicationLauncher.logger.warn("makeCalibrationNeutralCT : Entry");

		// Break CT
		controlOutput(ConstantBayPortNameMapping.CALIB_PORT_NAME_BREAK_CT, CLOSE);
		BayUtils.delay(500); // This is a blocking call
		controlOutput(ConstantBayPortNameMapping.CALIB_PORT_NAME_BREAK_CT, OPEN);
		// Make Neutral CT
		controlOutput(ConstantBayPortNameMapping.CALIB_PORT_NAME_NEUTRAL_CT_MAKE, CLOSE);
		BayUtils.delay(500); // This is a blocking call
		controlOutput(ConstantBayPortNameMapping.CALIB_PORT_NAME_NEUTRAL_CT_MAKE, OPEN);
	}

	// I N S U L A T I O N R E S I S T A N C E B A Y
	// ======================================================================================

	private void handleInsulationResistanceTestAction(BayActionType actionType) {
		switch (actionType) {
			case FINGERTIP_ENGAGE:
				engageInsulationResistanceTestFingertip();
				break;
			case FINGERTIP_DISENGAGE:
				disengageInsulationResistanceTestFingertip();
				break;
			case NO_ENTRY_ACTIVE:
				noEntryActiveIr();
				break;
			case NO_ENTRY_INACTIVE:
				noEntryInActiveIr();
				break;

			case HALT_PALLET_ACTIVE:
				haltPalletActiveIr();
				break;
			case HALT_PALLET_INACTIVE:
				haltPalletInActiveIr();
				break;
			case BLOCK_BAY_EXIT:
				closeInsulationResistanceTestStopper();
				break;
			case RELEASE_METER_FROM_BAY:
				openInsulationResistanceTestStopper();
				break;
			case REFRESH:
				refreshIrBay();
				break;
			default:
				break;
		}
	}

	private void engageInsulationResistanceTestFingertip() {
		ApplicationLauncher.logger.warn("engageInsulationResistanceTestFingertip : Entry");

		controlFingertip(ConstantBayPortNameMapping.IR_PORT_NAME_FINGER_TIP, CLOSE);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setPalletsLockedImageDisplayOn(bayTypeKey,
				true);
	}

	private void disengageInsulationResistanceTestFingertip() {
		ApplicationLauncher.logger.warn("disengageInsulationResistanceTestFingertip : Entry");

		controlFingertip(ConstantBayPortNameMapping.IR_PORT_NAME_FINGER_TIP, OPEN);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setPalletsLockedImageDisplayOn(bayTypeKey,
				false);
	}

	private void closeInsulationResistanceTestStopper() {
		ApplicationLauncher.logger.warn("closeInsulationResistanceTestStopper : Entry");

		controlOutput(ConstantBayPortNameMapping.IR_PORT_NAME_STPR, CLOSE);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayExitStopper(bayTypeKey, false);
	}

	private void openInsulationResistanceTestStopper() {
		ApplicationLauncher.logger.warn("openInsulationResistanceTestStopper : Entry");

		controlOutput(ConstantBayPortNameMapping.IR_PORT_NAME_STPR, OPEN);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayExitStopper(bayTypeKey, true);
	}

	// H I G H V O L T A G E B A Y
	// ======================================================================================

	private void handleHighVoltageTestAction(BayActionType actionType) {
		switch (actionType) {
			case FINGERTIP_ENGAGE:
				engageHighVoltageTestFingertip();
				break;
			case FINGERTIP_DISENGAGE:
				disengageHighVoltageTestFingertip();
				break;
			case NO_ENTRY_ACTIVE:
				noEntryActiveHv();
				break;
			case NO_ENTRY_INACTIVE:
				noEntryInActiveHv();
				break;
			case HALT_PALLET_ACTIVE:
				haltPalletActiveHv();
				break;
			case HALT_PALLET_INACTIVE:
				haltPalletInActiveHv();
				break;
			case BLOCK_BAY_EXIT:
				closeHighVoltageTestStopper();
				break;
			case RELEASE_METER_FROM_BAY:
				openHighVoltageTestStopper();
				break;
			case REFRESH:
				refreshHvBay();
				break;
			default:
				break;
		}
	}

	public BayUtils getBayUtils() {
		return bayUtils;
	}

	/*
	 * public void setBayUtils(BayUtils bayUtils) {
	 * this.bayUtils = bayUtils;
	 * }
	 */

	private void engageHighVoltageTestFingertip() {
		ApplicationLauncher.logger.warn("engageHighVoltageTestFingertip : Entry");

		controlFingertip(ConstantBayPortNameMapping.HV_PORT_NAME_FINGER_TIP, CLOSE);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setPalletsLockedImageDisplayOn(bayTypeKey,
				true);
	}

	private void disengageHighVoltageTestFingertip() {
		ApplicationLauncher.logger.warn("disengageHighVoltageTestFingertip : Entry");

		controlFingertip(ConstantBayPortNameMapping.HV_PORT_NAME_FINGER_TIP, OPEN);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setPalletsLockedImageDisplayOn(bayTypeKey,
				false);
	}

	private void closeHighVoltageTestStopper() {
		ApplicationLauncher.logger.warn("closeHighVoltageTestStopper : Entry");

		controlOutput(ConstantBayPortNameMapping.HV_PORT_NAME_STPR, CLOSE);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayExitStopper(bayTypeKey, false);
	}

	private void openHighVoltageTestStopper() {
		ApplicationLauncher.logger.warn("openHighVoltageTestStopper : Entry");

		controlOutput(ConstantBayPortNameMapping.HV_PORT_NAME_STPR, OPEN);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayExitStopper(bayTypeKey, true);
	}

	private void handleFunctionalTestAction(PalletController.BayActionType actionType) {
		switch (actionType) {
			case FINGERTIP_ENGAGE:
				engageFunctionalTestFingertip();
				break;
			case FINGERTIP_DISENGAGE:
				disengageFunctionalTestFingertip();
				break;

			case NO_ENTRY_ACTIVE:
				noEntryActiveFt();
				break;
			case NO_ENTRY_INACTIVE:
				noEntryInActiveFt();
				break;

			case HALT_PALLET_ACTIVE:
				haltPalletActiveFt();
				break;
			case HALT_PALLET_INACTIVE:
				haltPalletInActiveFt();
				break;
			case SOURCE_START:
				startFunctionalTestSource();
				break;
			case SOURCE_STOP:
				stopFunctionalTestSource();
				break;
			case BLOCK_BAY_ENTRY:
				closeFunctionalTestStopperEntry();
				break;

			case UNBLOCK_BAY_ENTRY:
				openFunctionalTestStopperEntry();
				break;
			case BLOCK_BAY_EXIT:
				closeFunctionalTestStopperExit();
				break;

			case UNBLOCK_BAY_EXIT:
				openFunctionalTestStopperExit();
				break;
			case RELEASE_METER_FROM_BAY:
				openFunctionalTestStopperEntry();
				openFunctionalTestStopperExit();
				break;
			case DIVERTER_UP:
				closeFunctionalTestDiverter();
				break;
			case DIVERTER_DOWN:
				openFunctionalTestDiverter();
				break;
			case REFRESH:
				refreshFtBay();
				break;
			default:
				break;
		}
	}

	private void engageFunctionalTestFingertip() {
		ApplicationLauncher.logger.warn("engageFunctionalTestFingertip : Entry");

		controlFingertip(ConstantBayPortNameMapping.FT_PORT_NAME_FINGER_TIP, CLOSE);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setPalletsLockedImageDisplayOn(bayTypeKey,
				true);
	}

	private void disengageFunctionalTestFingertip() {
		ApplicationLauncher.logger.warn("disengageFunctionalTestFingertip : Entry");

		controlFingertip(ConstantBayPortNameMapping.FT_PORT_NAME_FINGER_TIP, OPEN);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setPalletsLockedImageDisplayOn(bayTypeKey,
				false);
	}

	private void startFunctionalTestSource() {
		ApplicationLauncher.logger.warn("startFunctionalTestSource : Entry");

		controlOutput(ConstantBayPortNameMapping.FT_PORT_NAME_SRC_START, CLOSE);
	}

	private void stopFunctionalTestSource() {
		ApplicationLauncher.logger.warn("stopFunctionalTestSource : Entry");

		controlOutput(ConstantBayPortNameMapping.FT_PORT_NAME_SRC_START, OPEN);
	}

	private void closeFunctionalTestStopperEntry() {
		ApplicationLauncher.logger.warn("closeFunctionalTestStopperEntry : Entry");

		controlOutput(ConstantBayPortNameMapping.FT_PORT_NAME_STPR_B4, CLOSE);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayEntryStopper(bayTypeKey, false);
	}

	private void openFunctionalTestStopperEntry() {
		ApplicationLauncher.logger.warn("openFunctionalTestStopperEntry : Entry");

		controlOutput(ConstantBayPortNameMapping.FT_PORT_NAME_STPR_B4, OPEN);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayEntryStopper(bayTypeKey, true);
	}

	private void closeFunctionalTestStopperExit() {
		ApplicationLauncher.logger.warn("closeFunctionalTestStopperExit : Entry");

		controlOutput(ConstantBayPortNameMapping.FT_PORT_NAME_STPR_AT, CLOSE);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayExitStopper(bayTypeKey, false);

	}

	private void openFunctionalTestStopperExit() {
		ApplicationLauncher.logger.warn("openFunctionalTestStopperExit : Entry");

		controlOutput(ConstantBayPortNameMapping.FT_PORT_NAME_STPR_AT, OPEN);
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayExitStopper(bayTypeKey, true);
	}

	private void closeFunctionalTestDiverter() {
		ApplicationLauncher.logger.warn("closeFunctionalTestDiverter : Entry");

		controlOutput(ConstantBayPortNameMapping.FT_PORT_NAME_DIVERTOR_RELAY, CLOSE);
	}

	private void openFunctionalTestDiverter() {
		ApplicationLauncher.logger.warn("openFunctionalTestDiverter : Entry");

		controlOutput(ConstantBayPortNameMapping.FT_PORT_NAME_DIVERTOR_RELAY, OPEN);
	}

	// MECHANICAL FUNCTIONS
	// ====================================================================================================================
	// Generalized Method
	private void controlFingertip(String portNameKey, boolean shouldClose) {
		IoPortInfo portInfo = BayUtils.getOutputPortDetails(portNameKey);
		if (portInfo != null) {
			String outputAction = shouldClose
					? Constant_IO_ActionMapping.OPEN
					: Constant_IO_ActionMapping.CLOSE;

			if (ProcalFeatureEnable.MODBUS_PLC_SLAVE_MODE) {
				bayUtils.setOutputDataToPlcBay(
						portInfo.getClusterId(),
						portInfo.getBayId(),
						portInfo.getPortId(),
						outputAction);
			} else {
				bayUtils.setOutputDataToBay(
						portInfo.getClusterId(),
						portInfo.getBayId(),
						portInfo.getPortId(),
						outputAction);
			}
		}
	}

	// ====================================================================================================================

	private void controlOutput(String portNameKey, boolean shouldClose) {
		IoPortInfo portInfo = BayUtils.getOutputPortDetails(portNameKey);

		if (portInfo != null) {
			Ft.logger.debug("PortId    : " + portInfo.getPortId());
			Ft.logger.debug("ClusterId : " + portInfo.getClusterId());
			Ft.logger.debug("BayId     : " + portInfo.getBayId());

			String outputAction = shouldClose
					? Constant_IO_ActionMapping.ON // Close = OFF->ON
					: Constant_IO_ActionMapping.OFF; // Open = ON->OFF

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

	/*
	 * private Map<String, Object> voltage_current_start() {
	 * IoPortInfo portInfo =
	 * BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.
	 * CALIB_PORT_NAME_BOFA_VOLTAGE_CURRENT_START);
	 * 
	 * if (portInfo != null) {
	 * CalibrationBay.logger.debug("PortId    : " + portInfo.getPortId());
	 * CalibrationBay.logger.debug("ClusterId : " + portInfo.getClusterId());
	 * CalibrationBay.logger.debug("BayId     : " + portInfo.getBayId());
	 * 
	 * BayUtils bayUtils = new BayUtils();
	 * 
	 * String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
	 * portInfo.getBayId(),
	 * portInfo.getPortId(),
	 * Constant_IO_ActionMapping.OLD_OFF_NEW_ON);
	 * 
	 * status = state.equals(Constant_IO_ActionMapping.OLD_OFF_NEW_ON) ? true :
	 * false;
	 * 
	 * if (StateExecutorController.simulateCalibBayHappyPath) {
	 * status = true;
	 * }
	 * 
	 * CalibrationBay.logger.
	 * debug("S059_02_Power_Source_Start_Main_CT : voltage_current_start : status : "
	 * + status);
	 * //
	 * =============================================================================
	 * ===============
	 * 
	 * responseReturn.put("status", status);
	 * 
	 * CalibrationBay.logger.
	 * debug("S059_02_Power_Source_Start_Main_CT : voltage_current_start : Exit");
	 * return responseReturn;
	 * 
	 * }
	 */

}
