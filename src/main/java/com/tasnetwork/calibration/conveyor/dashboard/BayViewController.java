package com.tasnetwork.calibration.conveyor.dashboard;

import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class BayViewController {

	private Timeline blinkTimelinePalletsExistInQueue;
	private Timeline blinkTimelineEntryStopperOpen;
	private Timeline blinkTimelineExitStopperOpen;
	private Timeline blinkTimelineAllPalletsExistInBay;
	private Timeline blinkTimelineAllPalletsExistInTargetBay;

	private Timeline timeUpTimeline;
	private Timeline timeProgressBarTimeline;
	private Timeline tpCountProgressBarTimeline;

	private int totalProgressBarTimeInSec = 0;
	private int presentProgressBarTimeInSec = 0;
	private int timeUpSecondsElapsed = 0;
	private int testPointCompleted = 0;
	private int totalTestPoints = 0;

	@FXML
	private Label lblTimeUpDisplay;
	@FXML
	private Label lblTpCountStatus;
	@FXML
	private ProgressBar pBarExecution;

	@FXML
	private Rectangle rectEntryStopperOpen;
	@FXML
	private Rectangle rectExitStopperOpen;
	@FXML
	private Rectangle rectAllPalletsExistInBay;
	@FXML
	private Rectangle rectPalletsExistInQueue;
	@FXML
	private Rectangle rectAllPalletsExistInTargetBay;

	@FXML
	private ImageView imgHaltPallet;
	@FXML
	private ImageView imgNoEntry;
	@FXML
	private ImageView imgByPassMode;

	@FXML
	private ImageView imgPalletsLocked;

	private String bayKey;
	private boolean isPalletsLocked = false;
	private boolean isExitClosed = false;
	private boolean isEntryClosed = false;

	public void setBayKey(String bayKey) {
		this.bayKey = bayKey;
		if (bayKey != null && bayKey.startsWith(ConstantConveyor.WAITING_BAY_KEY)) {
			if (imgPalletsLocked != null) imgPalletsLocked.setVisible(false);
		}
		if (imgByPassMode != null && bayKey != null) {
			imgByPassMode.setVisible(com.tasnetwork.calibration.conveyor.constant.ConstantBypassFlags.isBayFullyBypassed(bayKey));
		}
	}

	@FXML
	public void initialize() {
		refInitAssignment();
		toolTipInit();

		if (imgPalletsLocked != null) {
			imgPalletsLocked.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && bayKey != null) {
					PalletController.BayActionType action = isPalletsLocked
							? PalletController.BayActionType.FINGERTIP_DISENGAGE
							: PalletController.BayActionType.FINGERTIP_ENGAGE;

					BayActionHandler handler = new BayActionHandler(bayKey);
					handler.handleActionByBayType(action);
				}
			});
		}

		if (imgByPassMode != null) {
			imgByPassMode.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && bayKey != null) {
					boolean isBypassed = com.tasnetwork.calibration.conveyor.constant.ConstantBypassFlags.isBayFullyBypassed(bayKey);
					PalletController.BayActionType action = isBypassed
							? PalletController.BayActionType.BYPASS_MODE_INACTIVE
							: PalletController.BayActionType.BYPASS_MODE_ACTIVE;

					BayActionHandler handler = new BayActionHandler(bayKey);
					handler.handleActionByBayType(action);
				}
			});
		}

		if (rectExitStopperOpen != null) {
			rectExitStopperOpen.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && bayKey != null) {
					PalletController.BayActionType action = isExitClosed
							? PalletController.BayActionType.UNBLOCK_BAY_EXIT
							: PalletController.BayActionType.BLOCK_BAY_EXIT;

					BayActionHandler handler = new BayActionHandler(bayKey);
					handler.handleActionByBayType(action);
				}
			});
		}

		if (rectEntryStopperOpen != null) {
			rectEntryStopperOpen.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && bayKey != null) {
					PalletController.BayActionType action = isEntryClosed
							? PalletController.BayActionType.UNBLOCK_BAY_ENTRY
							: PalletController.BayActionType.BLOCK_BAY_ENTRY;

					BayActionHandler handler = new BayActionHandler(bayKey);
					handler.handleActionByBayType(action);
				}
			});
		}
	}

	private void toolTipInit() {

		Tooltip.install(rectEntryStopperOpen, new Tooltip("Entry stopper open status"));
		Tooltip.install(rectExitStopperOpen, new Tooltip("Exit stopper open status"));
		Tooltip.install(rectAllPalletsExistInBay, new Tooltip("All pallets exist in this bay"));
		Tooltip.install(rectPalletsExistInQueue, new Tooltip("Pallets are waiting in the queue"));
		Tooltip.install(rectAllPalletsExistInTargetBay, new Tooltip("Target bay all pallet exist"));

		Tooltip.install(imgHaltPallet, new Tooltip("Halt Pallet"));
		Tooltip.install(imgNoEntry, new Tooltip("No Entry"));
		Tooltip.install(imgByPassMode, new Tooltip("By Pass mode"));

		Tooltip.install(lblTimeUpDisplay, new Tooltip("Bay Time"));
		Tooltip.install(lblTpCountStatus, new Tooltip("Test point count"));
		Tooltip.install(pBarExecution, new Tooltip("Execution Progress"));
	}

	private void refInitAssignment() {

		/*
		 * ref_rectEntryStopperOpen = rectEntryStopperOpen;
		 * ref_rectExitStopperOpen = rectExitStopperOpen;
		 * ref_rectAllPalletsExistInBay = rectAllPalletsExistInBay;
		 * ref_rectPalletsExistInQueue = rectPalletsExistInQueue;
		 * ref_rectTargetBayAllPalletsFree = rectTargetBayAllPalletsFree;
		 */
	}

	public Rectangle getRectEntryStopperOpen() {
		return rectEntryStopperOpen;
	}

	public Rectangle getRectExitStopperOpen() {
		return rectExitStopperOpen;
	}

	public Rectangle getRectAllPalletsExistInBay() {
		return rectAllPalletsExistInBay;
	}

	public Rectangle getRectPalletsExistInQueue() {
		return rectPalletsExistInQueue;
	}

	public Rectangle getAllPalletsExistInTargetBay() {
		return rectAllPalletsExistInTargetBay;
	}

	public void setRectEntryStopperOpen(Rectangle rectEntryStopperOpen) {
		this.rectEntryStopperOpen = rectEntryStopperOpen;
	}

	public void setRectExitStopperOpen(Rectangle rectExitStopperOpen) {
		this.rectExitStopperOpen = rectExitStopperOpen;
	}

	public void setRectAllPalletsExistInBay(Rectangle rectAllPalletsExistInBay) {
		this.rectAllPalletsExistInBay = rectAllPalletsExistInBay;
	}

	public void setRectPalletsExistInQueue(Rectangle rectPalletsExistInQueue) {
		this.rectPalletsExistInQueue = rectPalletsExistInQueue;
	}

	public void setRectAllPalletsExistInTargetBay(Rectangle rectAllPalletsExistInTargetBay) {
		this.rectAllPalletsExistInTargetBay = rectAllPalletsExistInTargetBay;
	}

	public void startBlinkingPalletsExistInQueueIndicator() {
		if (rectPalletsExistInQueue == null)
			return;

		blinkTimelinePalletsExistInQueue = new Timeline(
				new KeyFrame(Duration.seconds(0.5), e -> rectPalletsExistInQueue.setFill(Color.YELLOW)),
				new KeyFrame(Duration.seconds(1.0), e -> rectPalletsExistInQueue.setFill(Color.TRANSPARENT)));
		blinkTimelinePalletsExistInQueue.setCycleCount(Animation.INDEFINITE);
		blinkTimelinePalletsExistInQueue.play();
	}

	public void stopBlinkingPalletsExistInQueueIndicator() {
		if (blinkTimelinePalletsExistInQueue != null) {
			blinkTimelinePalletsExistInQueue.stop();
			rectPalletsExistInQueue.setFill(Color.TRANSPARENT);
		}
	}

	public void startBlinkingEntryStopperOpenIndicator() {
		if (rectEntryStopperOpen == null)
			return;

		blinkTimelineEntryStopperOpen = new Timeline(
				new KeyFrame(Duration.seconds(0.5), e -> rectEntryStopperOpen.setFill(Color.YELLOW)),
				new KeyFrame(Duration.seconds(1.0), e -> rectEntryStopperOpen.setFill(Color.TRANSPARENT)));
		blinkTimelineEntryStopperOpen.setCycleCount(Animation.INDEFINITE);
		blinkTimelineEntryStopperOpen.play();
	}

	public void stopBlinkingEntryStopperOpenIndicator() {
		if (blinkTimelineEntryStopperOpen != null) {
			blinkTimelineEntryStopperOpen.stop();
			rectEntryStopperOpen.setFill(Color.TRANSPARENT);
		}
	}

	public void startBlinkingExitStopperOpenIndicator() {
		if (rectExitStopperOpen == null)
			return;

		blinkTimelineExitStopperOpen = new Timeline(
				new KeyFrame(Duration.seconds(0.5), e -> rectExitStopperOpen.setFill(Color.YELLOW)),
				new KeyFrame(Duration.seconds(1.0), e -> rectExitStopperOpen.setFill(Color.TRANSPARENT)));
		blinkTimelineExitStopperOpen.setCycleCount(Animation.INDEFINITE);
		blinkTimelineExitStopperOpen.play();
	}

	public void stopBlinkingExitStopperOpenIndicator() {
		if (blinkTimelineExitStopperOpen != null) {
			blinkTimelineExitStopperOpen.stop();
			rectExitStopperOpen.setFill(Color.TRANSPARENT);
		}
	}

	public void startBlinkingAllPalletsExistInBayIndicator() {
		if (rectAllPalletsExistInBay == null)
			return;

		blinkTimelineAllPalletsExistInBay = new Timeline(
				new KeyFrame(Duration.seconds(0.5), e -> rectAllPalletsExistInBay.setFill(Color.YELLOW)),
				new KeyFrame(Duration.seconds(1.0), e -> rectAllPalletsExistInBay.setFill(Color.TRANSPARENT)));
		blinkTimelineAllPalletsExistInBay.setCycleCount(Animation.INDEFINITE);
		blinkTimelineAllPalletsExistInBay.play();
	}

	public void stopBlinkingAllPalletsExistInBayIndicator() {
		if (blinkTimelineAllPalletsExistInBay != null) {
			blinkTimelineAllPalletsExistInBay.stop();
			rectAllPalletsExistInBay.setFill(Color.TRANSPARENT);
		}
	}

	public void startBlinkingAllPalletsExistInTargetBayIndicator() {
		if (rectAllPalletsExistInTargetBay == null)
			return;

		blinkTimelineAllPalletsExistInTargetBay = new Timeline(
				new KeyFrame(Duration.seconds(0.5), e -> rectAllPalletsExistInTargetBay.setFill(Color.YELLOW)),
				new KeyFrame(Duration.seconds(1.0), e -> rectAllPalletsExistInTargetBay.setFill(Color.TRANSPARENT)));
		blinkTimelineAllPalletsExistInTargetBay.setCycleCount(Animation.INDEFINITE);
		blinkTimelineAllPalletsExistInTargetBay.play();
	}

	public void stopBlinkingAllPalletsExistInTargetBayIndicator() {
		if (blinkTimelineAllPalletsExistInTargetBay != null) {
			blinkTimelineAllPalletsExistInTargetBay.stop();
			rectAllPalletsExistInTargetBay.setFill(Color.TRANSPARENT);
		}
	}

	public void setEntryStopperOpenIndicator(boolean isOpen) {
		stopBlinkingEntryStopperOpenIndicator();
		isEntryClosed = !isOpen;
		// rectEntryStopperOpen.setFill(Color.TRANSPARENT);esdfd
		rectEntryStopperOpen.setFill(isOpen ? Color.LIMEGREEN : Color.RED);
	}

	public void setExitStopperOpenIndicator(boolean isOpen) {
		stopBlinkingExitStopperOpenIndicator();
		isExitClosed = !isOpen;
		rectExitStopperOpen.setFill(isOpen ? Color.LIMEGREEN : Color.RED);
	}

	public void setAllPalletsExistInBayIndicator(boolean isPalletExist) {
		stopBlinkingAllPalletsExistInBayIndicator();
		rectAllPalletsExistInBay.setFill(isPalletExist ? Color.LIMEGREEN : Color.RED);
	}

	public void setPalletsExistInQueueIndicator(boolean isPalletExist) {
		stopBlinkingPalletsExistInQueueIndicator();
		rectPalletsExistInQueue.setFill(isPalletExist ? Color.LIMEGREEN : Color.RED);
	}

	public void setAllPalletsExistInTargetBayIndicator(boolean isPalletExist) {
		stopBlinkingAllPalletsExistInTargetBayIndicator();
		rectAllPalletsExistInTargetBay.setFill(isPalletExist ? Color.LIMEGREEN : Color.RED);
	}

	public void setEntryStopperOpenIndicatorVisible(boolean isVisible) {
		rectEntryStopperOpen.setVisible(isVisible);
	}

	public void setExitStopperOpenIndicatorVisible(boolean isVisible) {
		rectExitStopperOpen.setVisible(isVisible);
	}

	public void setAllPalletsExistInBayIndicatorVisible(boolean isVisible) {
		rectAllPalletsExistInBay.setVisible(isVisible);
	}

	public void setPalletsExistInQueueIndicatorVisible(boolean isVisible) {
		rectPalletsExistInQueue.setVisible(isVisible);
	}

	public void setAllPalletsExistInTargetBayIndicatorVisible(boolean isVisible) {
		rectAllPalletsExistInTargetBay.setVisible(isVisible);
	}

	public void resetEntryStopperOpenIndicator() {
		stopBlinkingEntryStopperOpenIndicator();
		rectEntryStopperOpen.setFill(Color.TRANSPARENT);
	}

	public void resetExitStopperOpenIndicator() {
		stopBlinkingExitStopperOpenIndicator();
		rectExitStopperOpen.setFill(Color.TRANSPARENT);
	}

	public void resetAllPalletsExistInBayIndicator() {
		stopBlinkingAllPalletsExistInBayIndicator();
		rectAllPalletsExistInBay.setFill(Color.TRANSPARENT);
	}

	public void resetPalletsExistInQueueIndicator() {
		stopBlinkingPalletsExistInQueueIndicator();
		rectPalletsExistInQueue.setFill(Color.TRANSPARENT);
	}

	public void resetAllPalletsExistInTargetBayIndicator() {
		stopBlinkingAllPalletsExistInTargetBayIndicator();
		rectAllPalletsExistInTargetBay.setFill(Color.TRANSPARENT);
	}

	public ImageView getImgHaltPallet() {
		return imgHaltPallet;
	}

	public ImageView getImgNoEntry() {
		return imgNoEntry;
	}

	public ImageView getImgByPassMode() {
		return imgByPassMode;
	}

	public void setImgHaltPallet(ImageView imgHaltPallet) {
		this.imgHaltPallet = imgHaltPallet;
	}

	public void setImgNoEntry(ImageView imgNoEntry) {
		this.imgNoEntry = imgNoEntry;
	}

	public void setImgByPassMode(ImageView imgByPassMode) {
		this.imgByPassMode = imgByPassMode;
	}

	public void haltImageDisplayOn(boolean isDisplayOn) {
		imgHaltPallet.setVisible(isDisplayOn);
	}

	public void noEntryImageDisplayOn(boolean isDisplayOn) {
		imgNoEntry.setVisible(isDisplayOn);
	}

	public void palletsLockedImageDisplayOn(boolean isDisplayOn) {
		isPalletsLocked = isDisplayOn;
		if (bayKey != null && bayKey.startsWith(ConstantConveyor.WAITING_BAY_KEY)) {
			imgPalletsLocked.setVisible(false);
			return;
		}
		if (!isDisplayOn) {
			javafx.scene.effect.ColorAdjust grayscale = new javafx.scene.effect.ColorAdjust();
			grayscale.setSaturation(-1.0);
			grayscale.setBrightness(-0.7);
			imgPalletsLocked.setEffect(grayscale);
			imgPalletsLocked.setOpacity(0.7);
		} else {
			imgPalletsLocked.setEffect(null);
			imgPalletsLocked.setOpacity(1.0);
		}
		imgPalletsLocked.setVisible(true);
	}

	public void byPassModeImageDisplayOn(boolean isDisplayOn) {
		imgByPassMode.setVisible(isDisplayOn);
	}

	public ImageView getImgPalletsLocked() {
		return imgPalletsLocked;
	}

	public void setImgPalletsLocked(ImageView imgPalletsLocked) {
		this.imgPalletsLocked = imgPalletsLocked;
	}

	public void setTimeUpDisplayVisible(boolean isVisible) {
		lblTimeUpDisplay.setVisible(isVisible);
	}

	public void startTimeUpDisplay() {
		stopTimeUpDisplay();

		timeUpSecondsElapsed = 0;
		lblTimeUpDisplay.setText("00:00");
		timeUpTimeline = new Timeline();
		KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), e -> {
			timeUpSecondsElapsed++;
			int minutes = timeUpSecondsElapsed / 60;
			int seconds = timeUpSecondsElapsed % 60;
			String formattedTime = String.format("%02d:%02d", minutes, seconds);
			lblTimeUpDisplay.setText(formattedTime);
		});
		timeUpTimeline.setCycleCount(Animation.INDEFINITE);
		timeUpTimeline.getKeyFrames().add(keyFrame);
		timeUpTimeline.play();
	}

	public void stopTimeUpDisplay() {
		if (timeUpTimeline != null) {
			try {
				timeUpTimeline.stop();
			} catch (Exception e1) {
				ApplicationLauncher.logger.error("stopTimeUpDisplay: Exception: " + e1.getMessage());
			}
			timeUpTimeline = null;
		}
		timeUpSecondsElapsed = 0;
		lblTimeUpDisplay.setText("00:00");
	}

	public void setTpCountStatusVisible(boolean isVisible) {
		lblTpCountStatus.setVisible(isVisible);
	}

	public void updateTpCountStatus(int completedTestPoint, int totalTp) {
		testPointCompleted = completedTestPoint;
		totalTestPoints = totalTp;
		lblTpCountStatus.setText(testPointCompleted + "/" + totalTestPoints);
	}

	public void updateTpCountStatus(int completedTestPoint) {

		testPointCompleted = completedTestPoint;
		lblTpCountStatus.setText(testPointCompleted + "/" + totalTestPoints);
	}

	public void setProgressBarIndicatorVisible(boolean isVisible) {
		pBarExecution.setVisible(isVisible);
	}

	public void resetProgressBarIndicator() {
		pBarExecution.setProgress(0.0);
	}

	public void startProgressBarWithTime(int executionTimeInSec) {

		timeProgressBarTimeline = new Timeline();
		totalProgressBarTimeInSec = executionTimeInSec;
		presentProgressBarTimeInSec = 0;
		pBarExecution.setProgress(0.0);
		KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), e -> {
			presentProgressBarTimeInSec++;
			// int minutes= timeUpSecondsElapsed/60;
			// int seconds = timeUpSecondsElapsed%60;
			// String formattedTime = String.format("%d:%02d", minutes,seconds);
			// Platform.runLater(()->{
			pBarExecution.setProgress((float) presentProgressBarTimeInSec / (float) totalProgressBarTimeInSec);

			// });
			if (presentProgressBarTimeInSec == totalProgressBarTimeInSec) {
				// ((Timeline) e.getSource()).stop();
				try {
					timeProgressBarTimeline.stop();
					ApplicationLauncher.logger.info("startProgressBarWithTime :Stopped");
				} catch (Exception e1) {
					ApplicationLauncher.logger.error("startProgressBarWithTime: Exception: " + e1.getMessage());
				}
			}
		});
		timeProgressBarTimeline.setCycleCount(Animation.INDEFINITE);
		timeProgressBarTimeline.getKeyFrames().add(keyFrame);
		timeProgressBarTimeline.play();

	}

	public void stopProgressBarWithTime() {

		totalProgressBarTimeInSec = 0;
		presentProgressBarTimeInSec = 0;

		try {
			timeProgressBarTimeline.stop();
			// pBarExecution.setProgress(-1.0);
			ApplicationLauncher.logger.info("stopProgressBarWithTime :Stopped");
		} catch (Exception e1) {
			ApplicationLauncher.logger.error("stopProgressBarWithTime: Exception: " + e1.getMessage());
		}

	}

	public void startProgressBarWithTpCount() {

		tpCountProgressBarTimeline = new Timeline();

		KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), e -> {

			if (totalTestPoints != 0) {
				pBarExecution.setProgress((float) testPointCompleted / (float) totalTestPoints);
			} else {
				ApplicationLauncher.logger.info("startProgressBarWithTpCount :totalTestPoints is zero");
			}

			if (testPointCompleted == totalTestPoints) {
				try {
					tpCountProgressBarTimeline.stop();
					ApplicationLauncher.logger.info("startProgressBarWithTpCount :Stopped");
				} catch (Exception e1) {
					ApplicationLauncher.logger.error("startProgressBarWithTpCount: Exception: " + e1.getMessage());
				}
			}
		});
		tpCountProgressBarTimeline.setCycleCount(Animation.INDEFINITE);
		tpCountProgressBarTimeline.getKeyFrames().add(keyFrame);
		tpCountProgressBarTimeline.play();

	}

	public void stopProgressBarWithTpCount() {

		totalTestPoints = 0;
		testPointCompleted = 0;

		try {
			tpCountProgressBarTimeline.stop();
			// pBarExecution.setProgress(-1.0);
			ApplicationLauncher.logger.info("stopProgressBarWithTpCount :Stopped");
		} catch (Exception e1) {
			ApplicationLauncher.logger.error("stopProgressBarWithTpCount: Exception: " + e1.getMessage());
		}

	}

}

