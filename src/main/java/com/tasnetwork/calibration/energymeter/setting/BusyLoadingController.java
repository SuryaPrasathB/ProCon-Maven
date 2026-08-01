package com.tasnetwork.calibration.energymeter.setting;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.WindowManager;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

public class BusyLoadingController implements Initializable {

	private static int scanDeviceTimerTimeOutInSec = 180;
	private static int scanDeviceTimerCounter = 0;
	public static volatile boolean bootupStatusTimerAlreadyStarted = false;
	public static volatile boolean ScanDeviceTimerAlreadyStarted = false;
	static Timer bootupStatusTimer;
	static Timer IsDeviceConnectedTaskTimer;
	// TimerTask bootupStatusTimerTask;
	static Timer scanDeviceTimer;
	static Timer ResponseTaskTimer;
	public static String panelBootingDisplayStatus = "";
	public static Boolean ScanDeviceClickedByUser = false;

	@FXML
	private StackPane stackPane;

	@FXML
	private AnchorPane AnchorPaneScanDevice;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		ScanDeviceAdjustScreen();
		AnchorPaneScanDevice.setStyle("-fx-background-color: rgba(0, 100, 100, 0.5); ");
		ProgressIndicator pi = new ProgressIndicator();
		Label labelScanningDevices = new Label();
		labelScanningDevices.setText("Loading data");
		VBox box = new VBox(pi, labelScanningDevices);
		box.setAlignment(Pos.CENTER);
		// Grey Background
		// bx.setDisable(true);
		stackPane.getChildren().add(box);
		ScanDeviceClickedByUser = true;
		bootupStatusTimerAlreadyStarted = false;
		ScanDeviceTimerAlreadyStarted = false;
		setPanelBootingDisplayStatus("");
		ApplicationHomeController.update_labelBootupStatus("");
		ScanDeviceTimerTaskTrigger();
		// stackPane.getChildren().add(labelScanningDevices);

		/*
		 * try {
		 * Thread.sleep(5000);
		 * } catch (InterruptedException e) {
		 * 
		 * e.printStackTrace();
		 * }
		 */
		// stackPane.getChildren().remove(box);
	}

	// @FXML
	public static void removeBusyLoadingScreenOverlay() {
		ApplicationLauncher.logger.debug("removeBusyLoadingScreenOverlay : Entry");
		Platform.runLater(() -> {
			try {
				ApplicationHomeController.unloadScanDeviceFXML();
				ApplicationHomeController.update_left_status("", ConstantApp.LEFT_STATUS_DEBUG);
				ApplicationHomeController.enableLeftMenuButtonsForTestRun();
				try {
					scanDeviceTimer.cancel();
					ApplicationLauncher.logger
							.info("ScanDeviceCompletedPostProcess : scanDeviceTimer: Timer Cancelled1");
				} catch (Exception ex) {
					ApplicationLauncher.logger
							.info("ScanDeviceCompletedPostProcess: scanDeviceTimer : Exception:" + ex.getMessage());
				}
			} catch (Exception e) {

				scanDeviceTimer.cancel();
				e.printStackTrace();
				ApplicationLauncher.logger.error("removeBusyLoadingScreenOverlay : Exception:" + e.getMessage());
			}
		});

		ApplicationLauncher.logger.debug("removeBusyLoadingScreenOverlay : Exit");
	}

	public void ScanDeviceAdjustScreen() {

		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		double ScreenHeight = primaryScreenBounds.getHeight();
		double ScreenWidth = primaryScreenBounds.getWidth();
		double SplitPaneOffset = 149;
		double BottomStatusPaneOffset = 64;// 56;
		/*
		 * double ProgressStatusPaneHeight = 192;
		 * double Monitor_RefData_Width = 0;//250;
		 * double RunningStatusHeaderOffset = 60;
		 * double PhaseDisplayTitledPaneHeight = 310;
		 */

		long ScreenWidthThreshold = 1500;
		long ScreenHeightThreshold = 700;

		ScreenWidthThreshold = ConstantAppConfig.ScreenWidthThreshold;
		ScreenHeightThreshold = ConstantAppConfig.ScreenHeightThreshold;

		ApplicationLauncher.logger.info("ScanDeviceAdjustScreen :Current Screen Height:" + ScreenHeight);
		ApplicationLauncher.logger.info("ScanDeviceAdjustScreen: Current Screen Width:" + ScreenWidth);
		ApplicationLauncher.logger.info("ScanDeviceAdjustScreen :ScreenHeightThreshold:" + ScreenHeightThreshold);
		ApplicationLauncher.logger.info("ScanDeviceAdjustScreen: ScreenWidthThreshold:" + ScreenWidthThreshold);
		/*
		 * if (ScreenHeight> ScreenHeightThreshold){
		 * titlePaneLiveStatus.setMinHeight(ScreenHeight - ProgressStatusPaneHeight -
		 * RunningStatusHeaderOffset+100);
		 * 
		 * 
		 * }
		 * double RunningStatusWidth =
		 * ScreenWidth-SplitPaneOffset-Monitor_RefData_Width+100;//-25;
		 * 
		 * if(ScreenWidth >ScreenWidthThreshold){
		 * vBoxRunningStatus.setPrefWidth(RunningStatusWidth-70);
		 * tbl_liveStatus.setPrefWidth(RunningStatusWidth-1000);//-70
		 * titlePaneLiveStatus.setPrefWidth(RunningStatusWidth-900);
		 * vBoxRunningStatus.setPrefWidth(RunningStatusWidth-900);
		 */
		AnchorPaneScanDevice.setPrefWidth(ScreenWidth - 110);
		AnchorPaneScanDevice.setPrefHeight(ScreenHeight - BottomStatusPaneOffset);
		stackPane.setPrefWidth(ScreenWidth - 110);
		stackPane.setPrefHeight(ScreenHeight - BottomStatusPaneOffset);
		/* } */
	}

	public static void ScanDeviceTimerTaskTrigger() {
		if (!ScanDeviceTimerAlreadyStarted) {
			ApplicationHomeController.disableLeftMenuButtonsForTestRun();
			scanDeviceTimerCounter = 0;
			ScanTimerDisplayVisible(true);
			// ScanDeviceButtonStatus(false);
			scanDeviceTimer = new Timer();
			scanDeviceTimer.schedule(new scanDeviceTimerTask(), 0, 1000);// 1000
			ScanDeviceTimerAlreadyStarted = true;
		}
	}

	public static void triggerBootupStatusTimerTask() {
		ApplicationLauncher.logger.info("triggerBootupStatusTimerTask : Entry");
		if (!bootupStatusTimerAlreadyStarted) {
			ApplicationLauncher.logger.info("triggerBootupStatusTimerTask : Entry2");
			bootupStatusTimerAlreadyStarted = true;
			ProtampBootupDisplayVisible(true);
			ScanTimerDisplayVisible(true);
			bootupStatusTimer = new Timer();
			bootupStatusTimer.schedule(new bootupStatusTimerTask(), 0, 1000);
		}
		ApplicationLauncher.logger.info("triggerBootupStatusTimerTask : Exit");
	}

	public static void ScanTimerDisplayVisible(Boolean value) {
		// txtViewScanTimerDisplay.setVisibility(value);
		// ApplicationHomeController.ScanTimerDisplayVisible(value);
	}

	public static void ProtampBootupDisplayVisible(Boolean value) {
		// txtViewProtampBootupDisplay.setVisibility(value);
		// ApplicationHomeController.ProtampBootupDisplayVisible(value);
	}

	/*
	 * public static void ScanDeviceButtonStatus(Boolean value){
	 * 
	 * //scan_device.setEnabled(value);
	 * Platform.runLater(() -> {
	 * ref_btnScanDevice.setDisable(!value);
	 * });
	 * 
	 * }
	 */

	public static void ScanDeviceCompletedPostProcess() {
		ApplicationLauncher.logger.info("ScanDeviceCompletedPostProcess: Entry");
		// ScanDeviceButtonStatus(true);

		// btnSystemSettings.setEnabled(true);//migrateincomplete
		// txtViewProtampBootupDisplay.setVisibility(View.INVISIBLE);
		ProtampBootupDisplayVisible(false);
		ScanTimerDisplayVisible(false);
		try {
			bootupStatusTimer.cancel();
			bootupStatusTimerAlreadyStarted = false;
			ApplicationLauncher.logger.info("ScanDeviceCompletedPostProcess: bootupStatusTimer: Timer Cancelled");
		} catch (Exception ex) {
			ApplicationLauncher.logger
					.info("ScanDeviceCompletedPostProcess: bootupStatusTimer : Exception:" + ex.getMessage());
		}
		try {
			scanDeviceTimer.cancel();
			ApplicationLauncher.logger.info("ScanDeviceCompletedPostProcess : scanDeviceTimer: Timer Cancelled2");
		} catch (Exception ex) {
			ApplicationLauncher.logger
					.info("ScanDeviceCompletedPostProcess: scanDeviceTimer : Exception:" + ex.getMessage());
		}

		/*
		 * RemoveScanningScreenOverlay();
		 * ApplicationHomeController.EnableLeftMenuButtonsForTestRun();
		 */
	}

	public final static String getPanelBootingDisplayStatus() {
		return panelBootingDisplayStatus;
	}

	public final static void setPanelBootingDisplayStatus(String value) {
		panelBootingDisplayStatus = value;
	}

	public final static Boolean getBootupStatusTimerAlreadyStarted() {
		return bootupStatusTimerAlreadyStarted;
	}

	public final static void setBootupStatusTimerAlreadyStarted(Boolean value) {
		bootupStatusTimerAlreadyStarted = value;
	}

	public final static Boolean getScanDeviceTimerAlreadyStarted() {
		return ScanDeviceTimerAlreadyStarted;
	}

	public final static void setScanDeviceTimerAlreadyStarted(Boolean value) {
		ScanDeviceTimerAlreadyStarted = value;
	}

	static class bootupStatusTimerTask extends TimerTask {

		public void run() {

			System.out.println("bootupStatusTimerTask: Invoked");

			// bootupStatusHandler.post(new Runnable() {

			// public void run() {
			// System.out.println("bootupStatusTimerTask: Post Invoked");
			if (getPanelBootingDisplayStatus().equals("Booting")) {
				// txtViewProtampBootupDisplay.setText("Booting.");
				setPanelBootingDisplayStatus("Booting.");
				ApplicationHomeController.update_labelBootupStatus("Booting.");

			} else if (getPanelBootingDisplayStatus().equals("Booting.")) {
				// txtViewProtampBootupDisplay.setText("Booting..");
				setPanelBootingDisplayStatus("Booting..");
				ApplicationHomeController.update_labelBootupStatus("Booting..");
			} else if (getPanelBootingDisplayStatus().equals("Booting..")) {
				// txtViewProtampBootupDisplay.setText("Booting...");
				setPanelBootingDisplayStatus("Booting...");
				ApplicationHomeController.update_labelBootupStatus("Booting...");
			} else if (getPanelBootingDisplayStatus().equals("Booting...")) {
				// txtViewProtampBootupDisplay.setText("Booting");
				setPanelBootingDisplayStatus("Booting");
				ApplicationHomeController.update_labelBootupStatus("Booting");
			} else {
				// txtViewProtampBootupDisplay.setText("Booting");
				setPanelBootingDisplayStatus("Booting");
				ApplicationHomeController.update_labelBootupStatus("Booting");
			}

			// }

			// }
			// );
		}

	};

	public static void getValidateCredResponseOnThrowTaskTrigger(String message) {
		ApplicationLauncher.logger.info("getValidateCredResponseOnThrowTaskTrigger : Entry");
		ResponseTaskTimer = new Timer();
		ResponseTaskTimer.schedule(new getValidateCredResponseOnThrowTask(message), 100);
	}

	public static void IsDeviceConnectedTaskTrigger() {
		ApplicationLauncher.logger.info("IsDeviceConnectedTaskTrigger : Entry");
		IsDeviceConnectedTaskTimer = new Timer();
		IsDeviceConnectedTaskTimer.schedule(new IsDeviceConnectedTask(), 100);
	}

	public static void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep :InterruptedException:" + e.getMessage());
		}

	}

	static class scanDeviceTimerTask extends TimerTask {
		public void run() {

			ApplicationLauncher.logger.debug("scanDeviceTimerTask : Entry");
			// WindowManager.setCursor(Cursor.WAIT);
			// System.out.println("scanDeviceTimerTask: Invoked");
			scanDeviceTimerCounter++;

			// txtViewScanTimerDisplay.setText((scanDeviceTimerTimeOutInSec-scanDeviceTimerCounter)+
			// " Sec");
			// ApplicationHomeController.update_labelBootupTimer((scanDeviceTimerTimeOutInSec-scanDeviceTimerCounter)+
			// " Sec");
			// ApplicationHomeController.update_labelBootupTimer(" Sec");
			if (scanDeviceTimerCounter % 6 == 0) {
				System.out.println("scanDeviceTimerTask: Every 6 sec");
				try {
					// ApplicationLauncher.logger.debug("monitorServerTimerTask:
					// getServerPollingFrequencyInMsec(): "+
					// ProjectExecutionController.getServerPollingFrequencyInMsec() );
					// AsyncClientManager asycnClient = new AsyncClientManager();
					// asycnClient.ValidateCredential();
				} catch (Exception e) {
					System.out.println("scanDeviceTimerTask: Exception: " + e.getMessage());
				}
			}
			if (scanDeviceTimerCounter > scanDeviceTimerTimeOutInSec) {
				ScanDeviceCompletedPostProcess();
				ApplicationLauncher.logger.info(
						"scanDeviceTimerTask: Error 501: Device : Ensure equipment is with in the range or Switch OFF and Switch ON the equipment");
				if (ScanDeviceClickedByUser) {
					ScanDeviceClickedByUser = false;
					WindowManager.InformUser("Error 501 - Device not found",
							"Error 501 - Ensure equipment is with in the range or Switch OFF and Switch ON the equipment",
							AlertType.ERROR);

				}
				removeBusyLoadingScreenOverlay();
				// ConstantProTamp.ScanDeviceFound = false;
				ApplicationHomeController.enableLeftMenuButtonsForTestRun();
				/*
				 * AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
				 * builder.setTitle("Device not found:")
				 * .setMessage("Ensure equipment is with in the range or Switch OFF and Switch ON the equipment"
				 * )
				 * 
				 * .setPositiveButton("ok",new DialogInterface.OnClickListener()
				 * {
				 * 
				 * @Override
				 * 
				 * public void onClick(DialogInterface dialog,int which){
				 * 
				 * 
				 * }
				 * });
				 * 
				 * 
				 * AlertDialog alter = builder.create();
				 * alter.show();
				 */

				// WindowManager.setCursor(Cursor.DEFAULT);
				scanDeviceTimer.cancel();
			}
		}
	};

	static class IsDeviceConnectedTask extends TimerTask {

		public void run() {
			ApplicationLauncher.logger.info("IsDeviceConnectedTask: Entry");

			// IsDeviceConnected();

		}
	};

	static class getValidateCredResponseOnThrowTask extends TimerTask {

		private String ThrowMessage = "";

		getValidateCredResponseOnThrowTask(String Message) {
			ThrowMessage = Message;
		}

		public void run() {
			ApplicationLauncher.logger.info("getValidateCredResponseOnThrowTask: Entry");
			ApplicationLauncher.logger.info("getValidateCredResponseOnThrowTask: ThrowMessage:" + ThrowMessage);
			// ProjectExecutionController.updateServerStatus(ServerProperties.SERVER_CONNECTION_FAILED);
			if ((ThrowMessage.contains("No route")) || (ThrowMessage.contains("Connection timed out"))) {
				ScanDeviceCompletedPostProcess();
				ApplicationLauncher.logger.info(
						"getValidateCredResponseOnThrowTask: Error 412: Device not found. Wifi communication failed");

				if (ScanDeviceClickedByUser) {
					ScanDeviceClickedByUser = false;
					WindowManager.InformUser("Error 412", "Error 412: Device not found. Wifi communication failed",
							AlertType.ERROR);

				}
				removeBusyLoadingScreenOverlay();
				// ConstantProTamp.ScanDeviceFound = false;
				ApplicationHomeController.enableLeftMenuButtonsForTestRun();

			}
			// ApplicationLauncher.logger.info("getValidateCredResponseOnThrowTask: Error
			// 502: Device : Ensure equipment is with in the range or Switch OFF and Switch
			// ON the equipment");

			/*
			 * if(ScanDeviceClickedByUser){
			 * WindowManager.InformUser("Error 502 - Device not found"
			 * ,"Error 502: Ensure equipment is with in the range or Switch OFF and Switch ON the equipment"
			 * ,AlertType.ERROR);
			 * 
			 * ScanDeviceClickedByUser = false;
			 * }
			 */
			/*
			 * ConstantProTamp.ScanDeviceFound = false;
			 * ApplicationHomeController.EnableLeftMenuButtonsForTestRun();
			 */

		}
	};

}
