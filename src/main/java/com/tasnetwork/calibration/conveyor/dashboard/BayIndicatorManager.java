package com.tasnetwork.calibration.conveyor.dashboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

/**
 * Handles all indicator operations for bay containers in the dashboard.
 * Separates indicator logic from DashboardController for better
 * maintainability.
 */
public class BayIndicatorManager {
	private final Map<String, AnchorPane> bayKeyToBayContainer;
	private final String bayViewFxmlFileName;

	public BayIndicatorManager(Map<String, AnchorPane> bayKeyToBayContainer, String bayViewFxmlFileName) {
		this.bayKeyToBayContainer = bayKeyToBayContainer;
		this.bayViewFxmlFileName = bayViewFxmlFileName;
	}

	/*
	 * public BayIndicatorManager(Map<String, AnchorPane> bayKeyToBayContainer) {
	 * this.bayKeyToBayContainer = bayKeyToBayContainer;
	 * }
	 */

	// ==================== Bay View Management ====================

	/**
	 * Adds a default bay view to the specified bay container
	 * 
	 * @param bayKey The key of the bay to initialize
	 */
	public void addDefaultBayView(String bayKey) {
		AnchorPane targetBay = bayKeyToBayContainer.get(bayKey);
		if (targetBay == null) {
			logEvent("Failed to initialize bay: Invalid bay key " + bayKey);
			ApplicationLauncher.logger.warn("addDefaultBayView: Invalid bay key " + bayKey);
			return;
		}

		Platform.runLater(() -> {
			if (hasExistingBayView(targetBay)) {
				logEvent("Bay " + bayKey + " already initialized");
				return;
			}

			try {
				initializeNewBayView(targetBay, bayKey);
			} catch (IOException e) {
				handleBayViewInitializationError(bayKey, e);
			}
		});
	}

	private boolean hasExistingBayView(AnchorPane targetBay) {
		return targetBay.getChildren().stream()
				.anyMatch(node -> node.getUserData() instanceof BayViewController);
	}

	private void initializeNewBayView(AnchorPane targetBay, String bayKey) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(
				"/fxml/conveyor/" + bayViewFxmlFileName + ConstantApp.THEME_FXML));
		Node bayView = loader.load();
		BayViewController controller = (BayViewController) loader.getController();
		controller.setBayKey(bayKey);

		resetAllIndicators(controller);
		bayView.setUserData(controller);
		targetBay.getChildren().add(bayView);

		ApplicationLauncher.logger.info("Initialized bay view for " + bayKey);
	}

	private void resetAllIndicators(BayViewController controller) {
		controller.resetEntryStopperOpenIndicator();
		controller.resetExitStopperOpenIndicator();
		controller.resetAllPalletsExistInBayIndicator();
		controller.resetPalletsExistInQueueIndicator();
		controller.resetAllPalletsExistInTargetBayIndicator();
	}

	private void handleBayViewInitializationError(String bayKey, IOException e) {
		logEvent("Failed to initialize bay view: " + e.getMessage());
		ApplicationLauncher.logger.error("addDefaultBayView: IOException for " + bayKey + ": " + e.getMessage());
	}

	private void logEvent(String message) {
		DashboardController.logEvent(message);
	}

	// Entry Stopper operations
	public void updateBayEntryStopper(String bayKey, boolean isOpen) {
		updateIndicator(bayKey, bayController -> bayController.setEntryStopperOpenIndicator(isOpen));
	}

	public void resetEntryStopperOpenIndicator(String bayKey) {
		updateIndicator(bayKey, BayViewController::resetEntryStopperOpenIndicator);
	}

	public void setEntryStopperOpenIndicatorVisible(String bayKey, boolean makeVisible) {
		updateIndicator(bayKey, bayController -> bayController.setEntryStopperOpenIndicatorVisible(makeVisible));
	}

	public void updateBayExitStopper(String bayKey, boolean isOpen) {
		ArrayList<String> eachBayGroupList = new ArrayList<String>();
		if (bayKey.contains(ConstantConveyor.VERIFICATION_BAY_KEY)) {
			eachBayGroupList = ConstantConveyor.VERIFIC_BAY_GROUP_LIST;
		} else if (bayKey.contains(ConstantConveyor.STA_NLD1_BAY_KEY)) {
			eachBayGroupList = ConstantConveyor.STA_NLD1_BAY_KEY_GROUP_LIST;
		} else if (bayKey.contains(ConstantConveyor.STA_NLD2_BAY_KEY)) {
			eachBayGroupList = ConstantConveyor.STA_NLD2_BAY_KEY_GROUP_LIST;
		} else if (bayKey.contains(ConstantConveyor.WAITING_BAY_KEY)) {
			eachBayGroupList = ConstantConveyor.WAITING_BAY_GROUP_LIST;
		}

		if (eachBayGroupList.size() > 0) {
			for (String eachSubBay : eachBayGroupList) {
				updateIndicator(eachSubBay, bayController -> bayController.setExitStopperOpenIndicator(isOpen));
			}
		} else {
			updateIndicator(bayKey, bayController -> bayController.setExitStopperOpenIndicator(isOpen));
		}
	}

	public void resetExitStopperOpenIndicator(String bayKey) {
		updateIndicator(bayKey, BayViewController::resetExitStopperOpenIndicator);
	}

	public void setExitStopperOpenIndicatorVisible(String bayKey, boolean makeVisible) {
		updateIndicator(bayKey, bayController -> bayController.setExitStopperOpenIndicatorVisible(makeVisible));
	}

	// Pallets Exist in Bay operations
	public void updateBayAllPalletsExistInBay(String bayKey, boolean isPalletExist) {
		updateIndicator(bayKey, bayController -> bayController.setAllPalletsExistInBayIndicator(isPalletExist));
	}

	public void resetAllPalletsExistInBayIndicator(String bayKey) {
		updateIndicator(bayKey, BayViewController::resetAllPalletsExistInBayIndicator);
	}

	public void setAllPalletsExistInBayIndicatorVisible(String bayKey, boolean makeVisible) {
		updateIndicator(bayKey, bayController -> bayController.setAllPalletsExistInBayIndicatorVisible(makeVisible));
	}

	public void setTimeUpDisplayVisible(String bayKey, boolean makeVisible) {
		updateIndicator(bayKey, bayController -> bayController.setTimeUpDisplayVisible(makeVisible));
	}

	public void startProgressBarWithTime(String bayKey, int executionTimeInSec) {

		ApplicationLauncher.logger.debug("startProgressBarWithTime : input bayKey: " + bayKey);
		String internalBayKey = bayKey;
		if (bayKey.contains(ConstantConveyor.VERIFICATION_BAY_KEY)) {
			ApplicationLauncher.logger.debug("startProgressBarWithTime : Hit1");
			internalBayKey = ConstantConveyor.VERIFICATION_PP1_BAY_KEY;

		} else if (bayKey.contains(ConstantConveyor.STA_NLD1_BAY_KEY)) {
			internalBayKey = ConstantConveyor.STA_NLD1_PP1_BAY_KEY;
			ApplicationLauncher.logger.debug("startProgressBarWithTime : Hit2");

		} else if (bayKey.contains(ConstantConveyor.STA_NLD2_BAY_KEY)) {
			internalBayKey = ConstantConveyor.STA_NLD2_PP1_BAY_KEY;
			ApplicationLauncher.logger.debug("startProgressBarWithTime : Hit3");

		} else if (bayKey.contains(ConstantConveyor.WAITING_BAY_KEY)) {
			internalBayKey = ConstantConveyor.WAITING_PP1_BAY_KEY;
			ApplicationLauncher.logger.debug("startProgressBarWithTime : Hit4");

		}

		ApplicationLauncher.logger.debug("startProgressBarWithTime : internalBayKey: " + internalBayKey);

		updateIndicator(internalBayKey, bayController -> bayController.startProgressBarWithTime(executionTimeInSec));
	}

	public void stopProgressBarWithTime(String bayKey) {

		ApplicationLauncher.logger.debug("stopProgressBarWithTime : input bayKey: " + bayKey);
		String internalBayKey = bayKey;
		if (bayKey.contains(ConstantConveyor.VERIFICATION_BAY_KEY)) {
			ApplicationLauncher.logger.debug("stopProgressBarWithTime : Hit1");
			internalBayKey = ConstantConveyor.VERIFICATION_PP1_BAY_KEY;

		} else if (bayKey.contains(ConstantConveyor.STA_NLD1_BAY_KEY)) {
			internalBayKey = ConstantConveyor.STA_NLD1_PP1_BAY_KEY;
			ApplicationLauncher.logger.debug("stopProgressBarWithTime : Hit2");

		} else if (bayKey.contains(ConstantConveyor.STA_NLD2_BAY_KEY)) {
			internalBayKey = ConstantConveyor.STA_NLD2_PP1_BAY_KEY;
			ApplicationLauncher.logger.debug("stopProgressBarWithTime : Hit3");

		} else if (bayKey.contains(ConstantConveyor.WAITING_BAY_KEY)) {
			internalBayKey = ConstantConveyor.WAITING_PP1_BAY_KEY;
			ApplicationLauncher.logger.debug("stopProgressBarWithTime : Hit4");

		}

		ApplicationLauncher.logger.debug("stopProgressBarWithTime : internalBayKey: " + internalBayKey);

		updateIndicator(internalBayKey, bayController -> bayController.stopProgressBarWithTime());
	}

	public void startProgressBarWithTpCount(String bayKey) {

		ApplicationLauncher.logger.debug("startProgressBarWithTpCount : input bayKey: " + bayKey);
		String internalBayKey = bayKey;
		if (bayKey.contains(ConstantConveyor.VERIFICATION_BAY_KEY)) {
			ApplicationLauncher.logger.debug("startProgressBarWithTpCount : Hit1");
			internalBayKey = ConstantConveyor.VERIFICATION_PP1_BAY_KEY;

		} else if (bayKey.contains(ConstantConveyor.STA_NLD1_BAY_KEY)) {
			internalBayKey = ConstantConveyor.STA_NLD1_PP1_BAY_KEY;
			ApplicationLauncher.logger.debug("startProgressBarWithTpCount : Hit2");

		} else if (bayKey.contains(ConstantConveyor.STA_NLD2_BAY_KEY)) {
			internalBayKey = ConstantConveyor.STA_NLD2_PP1_BAY_KEY;
			ApplicationLauncher.logger.debug("startProgressBarWithTpCount : Hit3");

		} else if (bayKey.contains(ConstantConveyor.WAITING_BAY_KEY)) {
			internalBayKey = ConstantConveyor.WAITING_PP1_BAY_KEY;
			ApplicationLauncher.logger.debug("startProgressBarWithTpCount : Hit4");

		}

		ApplicationLauncher.logger.debug("startProgressBarWithTpCount : internalBayKey: " + internalBayKey);

		updateIndicator(internalBayKey, bayController -> bayController.startProgressBarWithTpCount());
	}

	public void stopProgressBarWithTpCount(String bayKey) {

		ApplicationLauncher.logger.debug("stopProgressBarWithTpCount : input bayKey: " + bayKey);
		String internalBayKey = bayKey;
		if (bayKey.contains(ConstantConveyor.VERIFICATION_BAY_KEY)) {
			ApplicationLauncher.logger.debug("stopProgressBarWithTpCount : Hit1");
			internalBayKey = ConstantConveyor.VERIFICATION_PP1_BAY_KEY;

		} else if (bayKey.contains(ConstantConveyor.STA_NLD1_BAY_KEY)) {
			internalBayKey = ConstantConveyor.STA_NLD1_PP1_BAY_KEY;
			ApplicationLauncher.logger.debug("stopProgressBarWithTpCount : Hit2");

		} else if (bayKey.contains(ConstantConveyor.STA_NLD2_BAY_KEY)) {
			internalBayKey = ConstantConveyor.STA_NLD2_PP1_BAY_KEY;
			ApplicationLauncher.logger.debug("stopProgressBarWithTpCount : Hit3");

		} else if (bayKey.contains(ConstantConveyor.WAITING_BAY_KEY)) {
			internalBayKey = ConstantConveyor.WAITING_PP1_BAY_KEY;
			ApplicationLauncher.logger.debug("stopProgressBarWithTpCount : Hit4");

		}

		ApplicationLauncher.logger.debug("stopProgressBarWithTpCount : internalBayKey: " + internalBayKey);

		updateIndicator(internalBayKey, bayController -> bayController.stopProgressBarWithTpCount());
	}

	public void startTimeUpDisplay(String bayKey) {

		ApplicationLauncher.logger.debug("startTimeUpDisplay : input bayKey: " + bayKey);
		String internalBayKey = bayKey;
		if (bayKey.contains(ConstantConveyor.VERIFICATION_BAY_KEY)) {
			ApplicationLauncher.logger.debug("startTimeUpDisplay : Hit1");
			internalBayKey = ConstantConveyor.VERIFICATION_PP1_BAY_KEY;

		} else if (bayKey.contains(ConstantConveyor.STA_NLD1_BAY_KEY)) {
			internalBayKey = ConstantConveyor.STA_NLD1_PP1_BAY_KEY;
			ApplicationLauncher.logger.debug("startTimeUpDisplay : Hit2");

		} else if (bayKey.contains(ConstantConveyor.STA_NLD2_BAY_KEY)) {
			internalBayKey = ConstantConveyor.STA_NLD2_PP1_BAY_KEY;
			ApplicationLauncher.logger.debug("startTimeUpDisplay : Hit3");

		} else if (bayKey.contains(ConstantConveyor.WAITING_BAY_KEY)) {
			internalBayKey = ConstantConveyor.WAITING_PP1_BAY_KEY;
			ApplicationLauncher.logger.debug("startTimeUpDisplay : Hit4");

		}

		ApplicationLauncher.logger.debug("startTimeUpDisplay : internalBayKey: " + internalBayKey);

		updateIndicator(internalBayKey, bayController -> bayController.startTimeUpDisplay());
	}

	public void stopTimeUpDisplay(String bayKey) {
		ApplicationLauncher.logger.debug("stopTimeUpDisplay : input bayKey: " + bayKey);
		String internalBayKey = bayKey;
		if (bayKey.contains(ConstantConveyor.VERIFICATION_BAY_KEY)) {
			ApplicationLauncher.logger.debug("stopTimeUpDisplay : Hit1");
			internalBayKey = ConstantConveyor.VERIFICATION_PP1_BAY_KEY;

		} else if (bayKey.contains(ConstantConveyor.STA_NLD1_BAY_KEY)) {
			internalBayKey = ConstantConveyor.STA_NLD1_PP1_BAY_KEY;
			ApplicationLauncher.logger.debug("stopTimeUpDisplay : Hit2");

		} else if (bayKey.contains(ConstantConveyor.STA_NLD2_BAY_KEY)) {
			internalBayKey = ConstantConveyor.STA_NLD2_PP1_BAY_KEY;
			ApplicationLauncher.logger.debug("stopTimeUpDisplay : Hit3");

		} else if (bayKey.contains(ConstantConveyor.WAITING_BAY_KEY)) {
			internalBayKey = ConstantConveyor.WAITING_PP1_BAY_KEY;
			ApplicationLauncher.logger.debug("stopTimeUpDisplay : Hit4");

		}

		ApplicationLauncher.logger.debug("startTimeUpDisplay : internalBayKey: " + internalBayKey);

		updateIndicator(internalBayKey, bayController -> bayController.stopTimeUpDisplay());
	}

	public void setTpCountStatusVisible(String bayKey, boolean makeVisible) {
		updateIndicator(bayKey, bayController -> bayController.setTpCountStatusVisible(makeVisible));
	}

	public void updateTpCountStatus(String bayKey, int completedTestPoint, int totalTp) {

		ApplicationLauncher.logger.debug("updateTpCountStatus : input bayKey: " + bayKey);
		String internalBayKey = bayKey;
		if (bayKey.contains(ConstantConveyor.VERIFICATION_BAY_KEY)) {
			ApplicationLauncher.logger.debug("updateTpCountStatus : Hit1");
			internalBayKey = ConstantConveyor.VERIFICATION_PP1_BAY_KEY;

		} else if (bayKey.contains(ConstantConveyor.STA_NLD1_BAY_KEY)) {
			internalBayKey = ConstantConveyor.STA_NLD1_PP1_BAY_KEY;
			ApplicationLauncher.logger.debug("updateTpCountStatus : Hit2");

		} else if (bayKey.contains(ConstantConveyor.STA_NLD2_BAY_KEY)) {
			internalBayKey = ConstantConveyor.STA_NLD2_PP1_BAY_KEY;
			ApplicationLauncher.logger.debug("updateTpCountStatus : Hit3");

		} else if (bayKey.contains(ConstantConveyor.WAITING_BAY_KEY)) {
			internalBayKey = ConstantConveyor.WAITING_PP1_BAY_KEY;
			ApplicationLauncher.logger.debug("updateTpCountStatus : Hit4");

		}

		ApplicationLauncher.logger.debug("updateTpCountStatus : internalBayKey: " + internalBayKey);

		updateIndicator(internalBayKey,
				bayController -> bayController.updateTpCountStatus(completedTestPoint, totalTp));
	}

	public void updateTpCountStatus(String bayKey, int completedTestPoint) {

		ApplicationLauncher.logger.debug("updateTpCountStatus-2 : input bayKey: " + bayKey);
		String internalBayKey = bayKey;
		if (bayKey.contains(ConstantConveyor.VERIFICATION_BAY_KEY)) {
			ApplicationLauncher.logger.debug("updateTpCountStatus-2 : Hit1");
			internalBayKey = ConstantConveyor.VERIFICATION_PP1_BAY_KEY;

		} else if (bayKey.contains(ConstantConveyor.STA_NLD1_BAY_KEY)) {
			internalBayKey = ConstantConveyor.STA_NLD1_PP1_BAY_KEY;
			ApplicationLauncher.logger.debug("updateTpCountStatus-2 : Hit2");

		} else if (bayKey.contains(ConstantConveyor.STA_NLD2_BAY_KEY)) {
			internalBayKey = ConstantConveyor.STA_NLD2_PP1_BAY_KEY;
			ApplicationLauncher.logger.debug("updateTpCountStatus-2 : Hit3");

		} else if (bayKey.contains(ConstantConveyor.WAITING_BAY_KEY)) {
			internalBayKey = ConstantConveyor.WAITING_PP1_BAY_KEY;
			ApplicationLauncher.logger.debug("updateTpCountStatus-2 : Hit4");

		}

		ApplicationLauncher.logger.debug("updateTpCountStatus-2 : internalBayKey: " + internalBayKey);

		updateIndicator(internalBayKey, bayController -> bayController.updateTpCountStatus(completedTestPoint));
	}

	public void setProgressBarIndicatorVisible(String bayKey, boolean makeVisible) {
		updateIndicator(bayKey, bayController -> bayController.setProgressBarIndicatorVisible(makeVisible));
	}

	// Pallets Exist in Queue operations
	public void updateBayPalletsExistInQueue(String bayKey, boolean isPalletExist) {
		updateIndicator(bayKey, bayController -> bayController.setPalletsExistInQueueIndicator(isPalletExist));
	}

	public void resetPalletsExistInQueueIndicator(String bayKey) {
		updateIndicator(bayKey, BayViewController::resetPalletsExistInQueueIndicator);
	}

	public void setPalletsExistInQueueIndicatorVisible(String bayKey, boolean makeVisible) {
		updateIndicator(bayKey, bayController -> bayController.setPalletsExistInQueueIndicatorVisible(makeVisible));
	}

	// Target Bay operations
	public void updateBayAllPalletsExistInNextTargetBay(String bayKey, boolean isPalletExist) {
		updateIndicator(bayKey, bayController -> bayController.setAllPalletsExistInTargetBayIndicator(isPalletExist));
	}

	public void resetAllPalletsExistInTargetBayIndicator(String bayKey) {
		updateIndicator(bayKey, BayViewController::resetAllPalletsExistInTargetBayIndicator);
	}

	public void setAllPalletsExistInTargetBayIndicatorVisible(String bayKey, boolean makeVisible) {
		updateIndicator(bayKey,
				bayController -> bayController.setAllPalletsExistInTargetBayIndicatorVisible(makeVisible));
	}

	// Monitoring operations
	public void updateBayMonitoringAllPalletsExistInBay(String bayKey) {
		updateIndicator(bayKey, BayViewController::startBlinkingAllPalletsExistInBayIndicator);
	}

	public void updateBayMonitoringAllPalletsExistInTargetBayIndicator(String bayKey) {
		updateIndicator(bayKey, BayViewController::startBlinkingAllPalletsExistInTargetBayIndicator);
	}

	public void updateBayMonitoringPalletsExistInQueueIndicator(String bayKey) {
		updateIndicator(bayKey, BayViewController::startBlinkingPalletsExistInQueueIndicator);
	}

	public void setHaltImageDisplayOn(String bayKey, boolean isDisplayOn) {
		// updateIndicator(bayKey, bayController ->
		// bayController.haltImageDisplayOn(isDisplayOn));
		ApplicationLauncher.logger.debug("setHaltImageDisplayOn : input bayKey: " + bayKey);
		ArrayList<String> eachBayGroupList = new ArrayList<String>();
		if (bayKey.contains(ConstantConveyor.VERIFICATION_BAY_KEY)) {
			ApplicationLauncher.logger.debug("setHaltImageDisplayOn : Hit1");
			eachBayGroupList = ConstantConveyor.VERIFIC_BAY_GROUP_LIST;

		} else if (bayKey.contains(ConstantConveyor.STA_NLD1_BAY_KEY)) {
			// internalBayKey = ConstantConveyor.STA_NLD1_PP1_BAY_KEY;
			ApplicationLauncher.logger.debug("setHaltImageDisplayOn : Hit2");
			eachBayGroupList = ConstantConveyor.STA_NLD1_BAY_KEY_GROUP_LIST;
		} else if (bayKey.contains(ConstantConveyor.STA_NLD2_BAY_KEY)) {
			// internalBayKey = ConstantConveyor.STA_NLD2_PP1_BAY_KEY;
			ApplicationLauncher.logger.debug("setHaltImageDisplayOn : Hit3");
			eachBayGroupList = ConstantConveyor.STA_NLD2_BAY_KEY_GROUP_LIST;
		} else if (bayKey.contains(ConstantConveyor.WAITING_BAY_KEY)) {
			// internalBayKey = ConstantConveyor.STA_NLD2_PP1_BAY_KEY;
			ApplicationLauncher.logger.debug("setHaltImageDisplayOn : Hit4");
			eachBayGroupList = ConstantConveyor.WAITING_BAY_GROUP_LIST;
		}
		if (eachBayGroupList.size() > 0) {
			for (String eachSubBay : eachBayGroupList) {
				ApplicationLauncher.logger.debug("setHaltImageDisplayOn : eachSubBay: " + eachSubBay);
				updateIndicator(eachSubBay, bayController -> bayController.haltImageDisplayOn(isDisplayOn));
			}
		} else {
			ApplicationLauncher.logger.debug("setHaltImageDisplayOn : bayKey: " + bayKey);
			updateIndicator(bayKey, bayController -> bayController.haltImageDisplayOn(isDisplayOn));
		}

	}

	/*
	 * public void setNoEntryImageDisplayOn(String bayKey, boolean isDisplayOn) {
	 * updateIndicator(bayKey, bayController ->
	 * bayController.noEntryImageDisplayOn(isDisplayOn));
	 * 
	 * }
	 */

	public void setNoEntryImageDisplayOn(String bayKey, boolean isDisplayOn) {

		ApplicationLauncher.logger.debug("setNoEntryImageDisplayOn : input bayKey: " + bayKey);
		ArrayList<String> eachBayGroupList = new ArrayList<String>();
		if (bayKey.contains(ConstantConveyor.VERIFICATION_BAY_KEY)) {
			ApplicationLauncher.logger.debug("setNoEntryImageDisplayOn : Hit1");
			eachBayGroupList = ConstantConveyor.VERIFIC_BAY_GROUP_LIST;

		} else if (bayKey.contains(ConstantConveyor.STA_NLD1_BAY_KEY)) {
			// internalBayKey = ConstantConveyor.STA_NLD1_PP1_BAY_KEY;
			ApplicationLauncher.logger.debug("setNoEntryImageDisplayOn : Hit2");
			eachBayGroupList = ConstantConveyor.STA_NLD1_BAY_KEY_GROUP_LIST;
		} else if (bayKey.contains(ConstantConveyor.STA_NLD2_BAY_KEY)) {
			// internalBayKey = ConstantConveyor.STA_NLD2_PP1_BAY_KEY;
			ApplicationLauncher.logger.debug("setNoEntryImageDisplayOn : Hit3");
			eachBayGroupList = ConstantConveyor.STA_NLD2_BAY_KEY_GROUP_LIST;
		} else if (bayKey.contains(ConstantConveyor.WAITING_BAY_KEY)) {
			// internalBayKey = ConstantConveyor.STA_NLD2_PP1_BAY_KEY;
			ApplicationLauncher.logger.debug("setNoEntryImageDisplayOn : Hit4");
			eachBayGroupList = ConstantConveyor.WAITING_BAY_GROUP_LIST;
		}
		if (eachBayGroupList.size() > 0) {
			for (String eachSubBay : eachBayGroupList) {
				ApplicationLauncher.logger.debug("setNoEntryImageDisplayOn : eachSubBay: " + eachSubBay);
				updateIndicator(eachSubBay, bayController -> bayController.noEntryImageDisplayOn(isDisplayOn));
			}
		} else {
			ApplicationLauncher.logger.debug("setNoEntryImageDisplayOn : bayKey: " + bayKey);
			updateIndicator(bayKey, bayController -> bayController.noEntryImageDisplayOn(isDisplayOn));
		}
	}

	public void setByPassImageDisplayOn(String bayKey, boolean isDisplayOn) {
		updateIndicator(bayKey, bayController -> bayController.byPassModeImageDisplayOn(isDisplayOn));
	}

	public void setPalletsLockedImageDisplayOn(String bayKey, boolean isDisplayOn) {
		/*
		 * //String internalBayKey = bayKey;
		 * //Verification.logger.debug("setPalletsLockedImageDisplayOn : input bayKey: "
		 * +bayKey);
		 * 
		 * if(internalBayKey.contains(ConstantConveyor.VERIFICATION_BAY_KEY )){
		 * internalBayKey = ConstantConveyor.VERIFICATION_PP1_BAY_KEY;
		 * Verification.logger.debug("setPalletsLockedImageDisplayOn : Hit1");
		 * }else if(internalBayKey.contains(ConstantConveyor.STA_NLD1_BAY_KEY )){
		 * internalBayKey = ConstantConveyor.STA_NLD1_PP1_BAY_KEY;
		 * Verification.logger.debug("setPalletsLockedImageDisplayOn : Hit2");
		 * }else if(internalBayKey.contains(ConstantConveyor.STA_NLD2_BAY_KEY )){
		 * internalBayKey = ConstantConveyor.STA_NLD2_PP1_BAY_KEY;
		 * Verification.logger.debug("setPalletsLockedImageDisplayOn : Hit3");
		 * }
		 */
		// ApplicationLauncher.logger.debug("setPalletsLockedImageDisplayOn : bayKey: "
		// +bayKey);
		// updateIndicator(bayKey, bayController ->
		// bayController.palletsLockedImageDisplayOn(isDisplayOn));

		ApplicationLauncher.logger.debug("setPalletsLockedImageDisplayOn : input bayKey: " + bayKey);
		ArrayList<String> eachBayGroupList = new ArrayList<String>();
		if (bayKey.contains(ConstantConveyor.VERIFICATION_BAY_KEY)) {
			ApplicationLauncher.logger.debug("setPalletsLockedImageDisplayOn : Hit1");
			eachBayGroupList = ConstantConveyor.VERIFIC_BAY_GROUP_LIST;

		} else if (bayKey.contains(ConstantConveyor.STA_NLD1_BAY_KEY)) {
			// internalBayKey = ConstantConveyor.STA_NLD1_PP1_BAY_KEY;
			ApplicationLauncher.logger.debug("setPalletsLockedImageDisplayOn : Hit2");
			eachBayGroupList = ConstantConveyor.STA_NLD1_BAY_KEY_GROUP_LIST;
		} else if (bayKey.contains(ConstantConveyor.STA_NLD2_BAY_KEY)) {
			// internalBayKey = ConstantConveyor.STA_NLD2_PP1_BAY_KEY;
			ApplicationLauncher.logger.debug("setPalletsLockedImageDisplayOn : Hit3");
			eachBayGroupList = ConstantConveyor.STA_NLD2_BAY_KEY_GROUP_LIST;
		}
		if (eachBayGroupList.size() > 0) {
			for (String eachSubBay : eachBayGroupList) {
				ApplicationLauncher.logger.debug("setPalletsLockedImageDisplayOn : eachSubBay: " + eachSubBay);
				updateIndicator(eachSubBay, bayController -> bayController.palletsLockedImageDisplayOn(isDisplayOn));
			}
		} else {
			ApplicationLauncher.logger.debug("setPalletsLockedImageDisplayOn : bayKey: " + bayKey);
			updateIndicator(bayKey, bayController -> bayController.palletsLockedImageDisplayOn(isDisplayOn));
		}
	}

	public void setGroupedPalletsLockedImageDisplayOn(String bayKey, boolean isDisplayOn) {
		// String internalBayKey = bayKey;
		ApplicationLauncher.logger.debug("setGroupedPalletsLockedImageDisplayOn : input bayKey: " + bayKey);
		ArrayList<String> eachBayGroupList = new ArrayList<String>();
		if (bayKey.contains(ConstantConveyor.VERIFICATION_BAY_KEY)) {
			ApplicationLauncher.logger.debug("setGroupedPalletsLockedImageDisplayOn : Hit1");
			eachBayGroupList = ConstantConveyor.VERIFIC_BAY_GROUP_LIST;

		} else if (bayKey.contains(ConstantConveyor.STA_NLD1_BAY_KEY)) {
			// internalBayKey = ConstantConveyor.STA_NLD1_PP1_BAY_KEY;
			ApplicationLauncher.logger.debug("setGroupedPalletsLockedImageDisplayOn : Hit2");
			eachBayGroupList = ConstantConveyor.STA_NLD1_BAY_KEY_GROUP_LIST;
		} else if (bayKey.contains(ConstantConveyor.STA_NLD2_BAY_KEY)) {
			// internalBayKey = ConstantConveyor.STA_NLD2_PP1_BAY_KEY;
			ApplicationLauncher.logger.debug("setGroupedPalletsLockedImageDisplayOn : Hit3");
			eachBayGroupList = ConstantConveyor.STA_NLD2_BAY_KEY_GROUP_LIST;
		}
		for (String eachSubBay : eachBayGroupList) {
			ApplicationLauncher.logger.debug("setGroupedPalletsLockedImageDisplayOn : eachSubBay: " + eachSubBay);
			updateIndicator(eachSubBay, bayController -> bayController.palletsLockedImageDisplayOn(isDisplayOn));
		}
		// ApplicationLauncher.logger.debug("setGroupedPalletsLockedImageDisplayOn :
		// internalBayKey: " +internalBayKey);
		// updateIndicator(internalBayKey, bayController ->
		// bayController.palletsLockedImageDisplayOn(isDisplayOn));
	}

	public void byPassModeImageDisplayOn(String bayKey, boolean isDisplayOn) {
		updateIndicator(bayKey, bayController -> bayController.byPassModeImageDisplayOn(isDisplayOn));
	}

	/**
	 * Helper method to perform indicator updates on the JavaFX application thread.
	 * 
	 * @param bayKey The bay key to update
	 * @param action The action to perform on the BayViewController
	 */
	private void updateIndicator(String bayKey, IndicatorAction action) {
		String resolvedBayKey = bayKey;
		if (ConstantConveyor.VERIFICATION_BAY_KEY.equals(bayKey)) {
			resolvedBayKey = ConstantConveyor.VERIFICATION_PP1_BAY_KEY;
		} else if (ConstantConveyor.STA_NLD1_BAY_KEY.equals(bayKey)) {
			resolvedBayKey = ConstantConveyor.STA_NLD1_PP1_BAY_KEY;
		} else if (ConstantConveyor.STA_NLD2_BAY_KEY.equals(bayKey)) {
			resolvedBayKey = ConstantConveyor.STA_NLD2_PP1_BAY_KEY;
		} else if (ConstantConveyor.WAITING_BAY_KEY.equals(bayKey)) {
			resolvedBayKey = ConstantConveyor.WAITING_PP1_BAY_KEY;
		}

		final String finalBayKey = resolvedBayKey;

		Platform.runLater(() -> {
			AnchorPane targetBay = bayKeyToBayContainer.get(finalBayKey);
			if (targetBay == null) {
				ApplicationLauncher.logger.warn("BayIndicatorManager: Invalid bay key: " + finalBayKey);
				return;
			}

			for (Node node : targetBay.getChildren()) {
				if (node.getUserData() instanceof BayViewController) {
					BayViewController bayController = (BayViewController) node.getUserData();
					action.execute(bayController);
					return;
				}
			}
			ApplicationLauncher.logger.warn("BayIndicatorManager: No Bay found in " + finalBayKey);
		});
	}

	/**
	 * Functional interface for indicator actions.
	 */
	@FunctionalInterface
	private interface IndicatorAction {
		void execute(BayViewController bayController);
	}
}
