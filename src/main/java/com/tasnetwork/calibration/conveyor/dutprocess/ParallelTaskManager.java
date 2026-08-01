package com.tasnetwork.calibration.conveyor.dutprocess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Timer;
import java.util.TimerTask;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.WindowManager;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;

import javafx.scene.control.Alert.AlertType;

public class ParallelTaskManager {

	Timer monitorDutControlProcessTimer;

	private HashMap<Integer, String> dutResultSummary = new HashMap<>();
	private HashMap<Integer, String> dutResultResponse = new HashMap<>();

	private ArrayList<DutControlProcess> dutControlProcessList = new ArrayList<DutControlProcess>();

	private ArrayList<String> dutAddressList = new ArrayList<String>();

	private PriorityQueue<DutDatabaseWriteModel> databaseWriteQueue = new PriorityQueue<DutDatabaseWriteModel>();

	private volatile boolean monitorDutControlProcessInitiated = false;
	private volatile boolean dutAllControlProcessCompleted = false;

	public ConveyorDataManager DisplayDataObj = new ConveyorDataManager();
	// public SerialDataManager SerialDM_Obj = new SerialDataManager();

	// private static boolean refStdLogResultsEnabled = false;

	private ArrayList<String> blackListedMeterIdFoundList = new ArrayList<String>();
	private ArrayList<String> meterIdAlreadyCalibratedFoundList = new ArrayList<String>();
	private ArrayList<String> emptyMeterIdFoundList = new ArrayList<String>();

	private boolean blackListedMeterIdFoundOverAllStatus = false;
	private boolean meterIdAlreadyCalibratedFoundOverAllStatus = false;
	private boolean emptyMeterIdFoundOverAllStatus = false;

	private boolean projectExitProcess = false;

	public DutControlProcess startCalibrationQrScanProcess(int dutAddress) {
		ApplicationLauncher.logger.debug("startCalibrationQrScanProcess :Entry");

		// int dutAddress = Integer.parseInt(dutAddressStr);
		ApplicationLauncher.logger.debug("startCalibrationQrScanProcess :dutAddress : " + dutAddress);
		addDutAddressList(String.valueOf(dutAddress));
		DutControlProcess dutprocess = new DutControlProcess();
		dutprocess.setDutAddress(dutAddress);
		dutprocess.setDutProcessExecutionCompleted(false);
		dutprocess.setDutProcessExecutionStarted(true);
		Timer processExecutionTimer = new Timer();
		processExecutionTimer.schedule(new ProcessCalibrationQrScanExecuteTimerTask(dutprocess, dutAddress), 50);
		Sleep(100);
		// dutprocess.executeDutControlProcess(dutAddress);
		addDutControlProcessList(dutprocess);
		processExecutionTimer.cancel();

		// monitorDutControlProcessTrigger();
		return dutprocess;

	}

	public DutControlProcess startFtQrScanProcess(int dutAddress) {
		ApplicationLauncher.logger.debug("startFtQrScanProcess :Entry");

		// int dutAddress = Integer.parseInt(dutAddressStr);
		ApplicationLauncher.logger.debug("startFtQrScanProcess :dutAddress : " + dutAddress);
		addDutAddressList(String.valueOf(dutAddress));
		DutControlProcess dutprocess = new DutControlProcess();
		dutprocess.setDutAddress(dutAddress);
		dutprocess.setDutProcessExecutionCompleted(false);
		dutprocess.setDutProcessExecutionStarted(true);
		Timer processExecutionTimer = new Timer();
		processExecutionTimer.schedule(new ProcessFtQrScanExecuteTimerTask(dutprocess, dutAddress), 50);
		Sleep(100);
		// dutprocess.executeDutControlProcess(dutAddress);
		addDutControlProcessList(dutprocess);
		processExecutionTimer.cancel();

		// monitorDutControlProcessTrigger();
		return dutprocess;

	}

	public DutControlProcess startWriteMeterSerialNoToDutProcess(int dutAddress) {
		ApplicationLauncher.logger.debug("startWriteMeterSerialNoToDutProcess :Entry");

		// int dutAddress = Integer.parseInt(dutAddressStr);
		ApplicationLauncher.logger.debug("startWriteMeterSerialNoToDutProcess :dutAddress : " + dutAddress);
		addDutAddressList(String.valueOf(dutAddress));
		DutControlProcess dutprocess = new DutControlProcess();
		dutprocess.setDutAddress(dutAddress);
		dutprocess.setDutProcessExecutionCompleted(false);
		dutprocess.setDutProcessExecutionStarted(true);
		Timer processExecutionTimer = new Timer();
		processExecutionTimer.schedule(new ProcessFtWriteMeterSerialNoToDutExecuteTimerTask(dutprocess, dutAddress),
				50);
		Sleep(100);
		// dutprocess.executeDutControlProcess(dutAddress);
		addDutControlProcessList(dutprocess);
		processExecutionTimer.cancel();

		// monitorDutControlProcessTrigger();
		return dutprocess;

	}

	public DutControlProcess startWriteMeterHardwareIdNoToDutProcess(int dutAddress) {
		ApplicationLauncher.logger.debug("startWriteMeterHardwareIdNoToDutProcess :Entry");

		// int dutAddress = Integer.parseInt(dutAddressStr);
		ApplicationLauncher.logger.debug("startWriteMeterHardwareIdNoToDutProcess :dutAddress : " + dutAddress);
		addDutAddressList(String.valueOf(dutAddress));
		DutControlProcess dutprocess = new DutControlProcess();
		dutprocess.setDutAddress(dutAddress);
		dutprocess.setDutProcessExecutionCompleted(false);
		dutprocess.setDutProcessExecutionStarted(true);
		Timer processExecutionTimer = new Timer();
		processExecutionTimer.schedule(new ProcessFtWriteMeterHardwareIdNoToDutExecuteTimerTask(dutprocess, dutAddress),
				50);

		Sleep(100);
		// dutprocess.executeDutControlProcess(dutAddress);
		addDutControlProcessList(dutprocess);
		processExecutionTimer.cancel();

		// monitorDutControlProcessTrigger();
		return dutprocess;

	}

	public DutControlProcess startDutPhaseCalibrationProcess(int dutAddress) {
		ApplicationLauncher.logger.debug("startDutPhaseCalibrationProcess :Entry");

		// int dutAddress = Integer.parseInt(dutAddressStr);
		ApplicationLauncher.logger.debug("startDutPhaseCalibrationProcess :dutAddress : " + dutAddress);
		addDutAddressList(String.valueOf(dutAddress));
		DutControlProcess dutprocess = new DutControlProcess();
		dutprocess.setDutAddress(dutAddress);
		dutprocess.setDutProcessExecutionCompleted(false);
		dutprocess.setDutProcessExecutionStarted(true);

		Timer processExecutionTimer = new Timer();
		processExecutionTimer.schedule(new ProcessPhaseCalibrationDutExecuteTimerTask(dutprocess, dutAddress), 50);
		Sleep(100);

		// dutprocess.executeDutControlProcess(dutAddress);
		addDutControlProcessList(dutprocess);
		processExecutionTimer.cancel();

		// monitorDutControlProcessTrigger();
		return dutprocess;

	}

	public DutControlProcess startDutNeutralCalibrationProcess(int dutAddress) {
		ApplicationLauncher.logger.debug("startDutNeutralCalibrationProcess :Entry");

		// int dutAddress = Integer.parseInt(dutAddressStr);
		ApplicationLauncher.logger.debug("startDutNeutralCalibrationProcess :dutAddress : " + dutAddress);

		addDutAddressList(String.valueOf(dutAddress));
		DutControlProcess dutprocess = new DutControlProcess();
		dutprocess.setDutAddress(dutAddress);
		dutprocess.setDutProcessExecutionCompleted(false);
		dutprocess.setDutProcessExecutionStarted(true);

		Timer processExecutionTimer = new Timer();
		processExecutionTimer.schedule(new ProcessNeutralCalibrationDutExecuteTimerTask(dutprocess, dutAddress), 50);
		Sleep(100);
		// dutprocess.executeDutControlProcess(dutAddress);
		addDutControlProcessList(dutprocess);
		processExecutionTimer.cancel();

		// monitorDutControlProcessTrigger();
		return dutprocess;

	}

	public DutControlProcess startDutCalibrationProcess(int dutAddress) {
		ApplicationLauncher.logger.debug("startDutCalibrationProcess :Entry");

		// int dutAddress = Integer.parseInt(dutAddressStr);
		ApplicationLauncher.logger.debug("startDutCalibrationProcess :dutAddress : " + dutAddress);
		addDutAddressList(String.valueOf(dutAddress));
		DutControlProcess dutprocess = new DutControlProcess();
		dutprocess.setDutAddress(dutAddress);
		dutprocess.setDutProcessExecutionCompleted(false);
		dutprocess.setDutProcessExecutionStarted(true);

		Timer processExecutionTimer = new Timer();
		processExecutionTimer.schedule(new ProcessCalibrationDutExecuteTimerTask(dutprocess, dutAddress), 50);
		Sleep(100);
		// dutprocess.executeDutControlProcess(dutAddress);
		addDutControlProcessList(dutprocess);
		processExecutionTimer.cancel();

		// monitorDutControlProcessTrigger();
		return dutprocess;

	}

	public DutControlProcess startFtProcess(int dutAddress) {
		ApplicationLauncher.logger.debug("startFtProcess :Entry");

		// int dutAddress = Integer.parseInt(dutAddressStr);

		ApplicationLauncher.logger.debug("startFtProcess :Position : " + dutAddress);
		addDutAddressList(String.valueOf(dutAddress));
		DutControlProcess dutprocess = new DutControlProcess();
		dutprocess.setDutAddress(dutAddress);
		dutprocess.setDutProcessExecutionCompleted(false);
		dutprocess.setDutProcessExecutionStarted(true);

		Timer processExecutionTimer = new Timer();
		processExecutionTimer.schedule(new ProcessFtExecuteTimerTask(dutprocess, dutAddress), 50);
		Sleep(100);
		// dutprocess.executeDutControlProcess(dutAddress);
		addDutControlProcessList(dutprocess);
		processExecutionTimer.cancel();

		// monitorDutControlProcessTrigger();
		return dutprocess;

	}

	class ProcessCalibrationQrScanExecuteTimerTask extends TimerTask {
		DutControlProcess dutProcess = new DutControlProcess();
		int dutAddress = 0;

		ProcessCalibrationQrScanExecuteTimerTask(DutControlProcess dutProcess, int address) {
			this.dutProcess = dutProcess;
			this.dutAddress = address;
		}

		public void run() {
			// boolean status = dutProcess.executeCalibrationQrScanProcess(dutAddress);
			Map<String, Object> responseReturn = dutProcess.executeCalibrationQrScanProcess(dutAddress);

			String resultStatus = ConstantReport.RESULT_STATUS_FAIL;
			boolean status = (boolean) responseReturn.get("status");
			if (status) {
				resultStatus = ConstantReport.RESULT_STATUS_PASS;
			}
			setDutResultSummary(dutAddress, resultStatus);
			setDutResultResponse(dutAddress, (String) responseReturn.get("responseData"));
			// stepRunTimer.cancel();

		}
	}

	class ProcessFtQrScanExecuteTimerTask extends TimerTask {
		DutControlProcess dutProcess = new DutControlProcess();
		int dutAddress = 0;

		ProcessFtQrScanExecuteTimerTask(DutControlProcess dutProcess, int address) {
			this.dutProcess = dutProcess;
			this.dutAddress = address;
		}

		public void run() {
			// boolean status = dutProcess.executeCalibrationQrScanProcess(dutAddress);
			BayResponse bayResponse = dutProcess.executeFtQrScanProcess(dutAddress);

			String resultStatus = ConstantReport.RESULT_STATUS_FAIL;
			boolean status = bayResponse.getStatus();// (boolean)responseReturn.get("status");
			if (status) {
				resultStatus = ConstantReport.RESULT_STATUS_PASS;
			}
			setDutResultSummary(dutAddress, resultStatus);
			setDutResultResponse(dutAddress, bayResponse.getResponseData());// (String)responseReturn.get("responseData"));
			// stepRunTimer.cancel();

		}
	}

	class ProcessFtWriteMeterSerialNoToDutExecuteTimerTask extends TimerTask {
		DutControlProcess dutProcess = new DutControlProcess();
		int dutAddress = 0;

		ProcessFtWriteMeterSerialNoToDutExecuteTimerTask(DutControlProcess dutProcess, int address) {
			this.dutProcess = dutProcess;
			this.dutAddress = address;
		}

		public void run() {
			// boolean status = dutProcess.executeCalibrationQrScanProcess(dutAddress);
			BayResponse bayResponse = dutProcess.executeFtWriteMeterSerialNoToDutProcess(dutAddress);

			String resultStatus = ConstantReport.RESULT_STATUS_FAIL;
			boolean status = bayResponse.getStatus();// (boolean)responseReturn.get("status");
			if (status) {
				resultStatus = ConstantReport.RESULT_STATUS_PASS;
			}
			setDutResultSummary(dutAddress, resultStatus);
			setDutResultResponse(dutAddress, bayResponse.getResponseData());// (String)responseReturn.get("responseData"));
			// stepRunTimer.cancel();

		}
	}

	class ProcessFtWriteMeterHardwareIdNoToDutExecuteTimerTask extends TimerTask {
		DutControlProcess dutProcess = new DutControlProcess();
		int dutAddress = 0;

		ProcessFtWriteMeterHardwareIdNoToDutExecuteTimerTask(DutControlProcess dutProcess, int address) {
			this.dutProcess = dutProcess;
			this.dutAddress = address;
		}

		public void run() {
			// boolean status = dutProcess.executeCalibrationQrScanProcess(dutAddress);
			BayResponse bayResponse = dutProcess.executeFtWriteMeterHardwareIdNoToDutProcess(dutAddress);

			String resultStatus = ConstantReport.RESULT_STATUS_FAIL;
			boolean status = bayResponse.getStatus();// (boolean)responseReturn.get("status");
			if (status) {
				resultStatus = ConstantReport.RESULT_STATUS_PASS;
			}
			setDutResultSummary(dutAddress, resultStatus);
			setDutResultResponse(dutAddress, bayResponse.getResponseData());// (String)responseReturn.get("responseData"));
			// stepRunTimer.cancel();

		}
	}

	class ProcessCalibrationDutExecuteTimerTask extends TimerTask {
		DutControlProcess dutProcess = new DutControlProcess();
		int dutAddress = 0;

		ProcessCalibrationDutExecuteTimerTask(DutControlProcess dutProcess, int address) {
			this.dutProcess = dutProcess;
			this.dutAddress = address;
		}

		public void run() {
			boolean status = dutProcess.executeDutCalibrationProcess(dutAddress);
			String resultStatus = ConstantReport.RESULT_STATUS_FAIL;
			if (status) {
				resultStatus = ConstantReport.RESULT_STATUS_PASS;
			}
			setDutResultSummary(dutAddress, resultStatus);
			// stepRunTimer.cancel();

		}
	}

	class ProcessPhaseCalibrationDutExecuteTimerTask extends TimerTask {
		DutControlProcess dutProcess = new DutControlProcess();
		int dutAddress = 0;

		ProcessPhaseCalibrationDutExecuteTimerTask(DutControlProcess dutProcess, int address) {
			this.dutProcess = dutProcess;
			this.dutAddress = address;
		}

		public void run() {
			boolean status = dutProcess.executeDutPhaseCalibrationProcess(dutAddress);
			String resultStatus = ConstantReport.RESULT_STATUS_FAIL;
			if (status) {
				resultStatus = ConstantReport.RESULT_STATUS_PASS;
			}
			setDutResultSummary(dutAddress, resultStatus);
			// stepRunTimer.cancel();

		}
	}

	class ProcessNeutralCalibrationDutExecuteTimerTask extends TimerTask {
		DutControlProcess dutProcess = new DutControlProcess();
		int dutAddress = 0;

		ProcessNeutralCalibrationDutExecuteTimerTask(DutControlProcess dutProcess, int address) {
			this.dutProcess = dutProcess;
			this.dutAddress = address;
		}

		public void run() {
			boolean status = dutProcess.executeDutNeutralCalibrationProcess(dutAddress);
			String resultStatus = ConstantReport.RESULT_STATUS_FAIL;
			if (status) {
				resultStatus = ConstantReport.RESULT_STATUS_PASS;
			}
			setDutResultSummary(dutAddress, resultStatus);
			// stepRunTimer.cancel();

		}
	}

	class ProcessFtExecuteTimerTask extends TimerTask {
		DutControlProcess dutProcess = new DutControlProcess();
		int dutAddress = 0;

		ProcessFtExecuteTimerTask(DutControlProcess dutProcess, int address) {
			this.dutProcess = dutProcess;
			this.dutAddress = address;
		}

		public void run() {
			boolean status = dutProcess.executeFtProcess(dutAddress);
			String resultStatus = ConstantReport.RESULT_STATUS_FAIL;
			if (status) {
				resultStatus = ConstantReport.RESULT_STATUS_PASS;
			}
			setDutResultSummary(dutAddress, resultStatus);
			// stepRunTimer.cancel();

		}
	}
	/*
	 * public void ProcessExecuteTask(){
	 * ApplicationLauncher.logger.info("ProcessExecuteTask: Entry");
	 * setStepRunFlag(true);
	 * setResumeFlag(false);
	 * try {
	 * StartTestExecution();
	 * } catch (JSONException e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("StepRunOnClick : JSONException:"+e.
	 * getMessage());
	 * }catch (ParseException e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("StepRunOnClick : ParseException:"+e.
	 * getMessage());
	 * }
	 * }
	 */

	public void setSkipCurrentTestPoint(boolean status) {

		ApplicationLauncher.logger.debug("setSkipCurrentTestPoint :Entry");
		for (int i = 0; i < getDutControlProcessList().size(); i++) {
			getDutControlProcessList().get(i).setSkipCurrentTP_Execution(status);
		}
		if (status) {
			ApplicationLauncher.logger.debug("setSkipCurrentTestPoint : setDutAllControlProcessCompleted");
			setDutAllControlProcessCompleted(true);
		}

	}

	public void monitorDutControlProcessTrigger() {

		ApplicationLauncher.logger.debug("monitorDutControlProcessTrigger :Entry");
		if (!isMonitorDutControlProcessInitiated()) {
			setMonitorDutControlProcessInitiated(true);

			monitorDutControlProcessTimer = new Timer();
			monitorDutControlProcessTimer.schedule(new monitorDutControlProcess(), 100);
		}
	}

	class monitorDutControlProcess extends TimerTask {

		@Override
		public void run() {

			ApplicationLauncher.logger.debug("monitorDutControlProcess: Entry");
			if (!isDutAllControlProcessCompleted()) {

				monitorDutControlProcessTask();

				monitorDutControlProcessTimer.schedule(new monitorDutControlProcess(), 1000);

			} else {

				ApplicationLauncher.logger
						.debug("monitorDutControlProcess: monitorDutControlProcessTimer cancelling....");
				// Sleep(20000);
				monitorDutControlProcessTimer.cancel();
				ApplicationLauncher.logger.debug("monitorDutControlProcess: monitorDutControlProcessTimer cancelled");
			}

		}

	}

	public void monitorDutControlProcessTask() {

		ApplicationLauncher.logger.debug("monitorDutControlProcessTask :Entry");
		int noOfDutProcessCompleted = 0;

		if (!BayUtils.isUserAborted()) {
			ApplicationLauncher.logger.debug("monitorDutControlProcessTask : getDutControlProcessList().size() : "
					+ getDutControlProcessList().size());

			for (int i = 0; i < getDutControlProcessList().size(); i++) {
				if (getDutControlProcessList().get(i).isDutProcessExecutionCompleted()) {
					ApplicationLauncher.logger.debug("monitorDutControlProcessTask : Index : " + i);
					noOfDutProcessCompleted++;
				}
			}
			ApplicationLauncher.logger
					.debug("monitorDutControlProcessTask : noOfDutProcessCompleted : " + noOfDutProcessCompleted);
			if (noOfDutProcessCompleted == getDutControlProcessList().size()) {
				setDutAllControlProcessCompleted(true);
				// Sleep(20000);
				ApplicationLauncher.logger.debug("monitorDutControlProcessTask : DutAllControlProcessCompleted");
			}

		}

	}

	public void validateMeterIdProcess() {

		ApplicationLauncher.logger.debug("validateMeterIdProcess :Entry");
		validateForBlackListedMeterId();
		validateForAlreadyCalibratedMeterId();
		validateForEmptyMeterId();
		if ((isBlackListedMeterIdFoundOverAllStatus()) || (isMeterIdAlreadyCalibratedFoundOverAllStatus()) ||
				(isEmptyMeterIdFoundOverAllStatus())) {
			String meterIdList = "";
			if (isBlackListedMeterIdFoundOverAllStatus()) {

				for (int i = 0; i < getBlackListedMeterIdFoundList().size(); i++) {

					meterIdList = meterIdList + "\n" + getBlackListedMeterIdFoundList().get(i);
				}
				ApplicationLauncher.logger
						.info("validateMeterIdProcess: invoking exit process due to black listed meter id found");
				ApplicationLauncher.logger.debug("validateMeterIdProcess: ERROR_CODE_5001 :"
						+ ConvErrorCodeMapping.ERROR_CODE_5001_MSG + "\n\nMeter Id: " + meterIdList + " : Prompted");
				WindowManager.InformUser(ConvErrorCodeMapping.ERROR_CODE_5001,
						ConvErrorCodeMapping.ERROR_CODE_5001_MSG + "\n\nMeter Id: " + meterIdList, AlertType.ERROR);
				// ApplicationHomeController.updateBottomSecondaryStatus("Aborting execution:
				// Meter id -black list",ConstantApp.LEFT_STATUS_INFO);

			}

			if (isMeterIdAlreadyCalibratedFoundOverAllStatus()) {
				meterIdList = "";
				for (int i = 0; i < getMeterIdAlreadyCalibratedFoundList().size(); i++) {

					meterIdList = meterIdList + "\n" + getMeterIdAlreadyCalibratedFoundList().get(i);
				}
				ApplicationLauncher.logger
						.info("validateMeterIdProcess: invoking exit process due to meter id found already calibrated");
				ApplicationLauncher.logger.debug("validateMeterIdProcess: ERROR_CODE_5002 :"
						+ ConvErrorCodeMapping.ERROR_CODE_5002_MSG + "\n\nMeter Id: " + meterIdList + " : Prompted");
				WindowManager.InformUser(ConvErrorCodeMapping.ERROR_CODE_5002,
						ConvErrorCodeMapping.ERROR_CODE_5002_MSG + "\n\nMeter Id: " + meterIdList, AlertType.ERROR);
				// ApplicationHomeController.updateBottomSecondaryStatus("Aborting execution:
				// Meter id -Already calibrated List",ConstantApp.LEFT_STATUS_INFO);

			}

			if (isEmptyMeterIdFoundOverAllStatus()) {
				meterIdList = "";
				for (int i = 0; i < getEmptyMeterIdFoundList().size(); i++) {

					meterIdList = meterIdList + "\nRack Position " + getEmptyMeterIdFoundList().get(i);
				}
				ApplicationLauncher.logger
						.info("validateMeterIdProcess: invoking exit process due to meter id found empty");
				ApplicationLauncher.logger.debug("validateMeterIdProcess: ERROR_CODE_5003 :"
						+ ConvErrorCodeMapping.ERROR_CODE_5003_MSG + "\n\nMeter Id: " + meterIdList + " : Prompted");
				WindowManager.InformUser(ConvErrorCodeMapping.ERROR_CODE_5003,
						ConvErrorCodeMapping.ERROR_CODE_5003_MSG + "\n\nMeter Id: " + meterIdList, AlertType.ERROR);
				// ApplicationHomeController.updateBottomSecondaryStatus("Aborting execution:
				// Empty Meter id or error in reading meter id
				// found",ConstantApp.LEFT_STATUS_INFO);

			}

			setProjectExitProcess(true);
			// return status;
		}
	}

	public void validateDutSummaryResult() {

		ApplicationLauncher.logger.debug("validateDutSummaryResult :Entry");
		String resultStatus = "";
		int dutAddress = 0;
		for (int i = 0; i < getDutControlProcessList().size(); i++) {
			resultStatus = getDutControlProcessList().get(i).getPresentResultStatus();
			dutAddress = getDutControlProcessList().get(i).getDutAddress();
			ApplicationLauncher.logger.debug("validateDutSummaryResult : dutAddress: " + dutAddress);
			ApplicationLauncher.logger.debug("validateDutSummaryResult : resultStatus: " + resultStatus);
			if (resultStatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())) {
				ConveyorDataManager.setDutResultSummary(resultStatus, dutAddress);
				ApplicationLauncher.logger.debug("validateDutSummaryResult : failed status updated");
			}
		}

	}

	public void validateForEmptyMeterId() {
		ApplicationLauncher.logger.debug("validateForEmptyMeterId :Entry");

		for (int i = 0; i < getDutControlProcessList().size(); i++) {
			String readMeterId = getDutControlProcessList().get(i).getDutSerialNumber();
			if (readMeterId.isEmpty()) {
				setEmptyMeterIdFoundOverAllStatus(true);
				getEmptyMeterIdFoundList().add(String.valueOf(getDutControlProcessList().get(i).getDutAddress()));
			}
		}

	}

	public void validateForBlackListedMeterId() {
		ApplicationLauncher.logger.debug("validateForBlackListedMeterId :Entry");
		// if(ProjectExecutionController.getCurrentTestPointName().contains(ConstantCalibration.CALIB_READ_METER_ID)){
		for (int i = 0; i < getDutControlProcessList().size(); i++) {

			boolean blackListedMeterIdFound = isMeterIdExistInBlackList(
					getDutControlProcessList().get(i).getDutSerialNumber());
			if (blackListedMeterIdFound) {
				getBlackListedMeterIdFoundList().add(getDutControlProcessList().get(i).getDutSerialNumber());
				setBlackListedMeterIdFoundOverAllStatus(true);

			}
		}
		// }
	}

	public void validateForAlreadyCalibratedMeterId() {
		ApplicationLauncher.logger.debug("validateForAlreadyCalibratedMeterId :Entry");

		for (int i = 0; i < getDutControlProcessList().size(); i++) {
			boolean meterIdAlreadyCalibrated = isMeterIdExistInAlreadyCalibratedList(
					getDutControlProcessList().get(i).getDutSerialNumber());
			if (meterIdAlreadyCalibrated) {
				getMeterIdAlreadyCalibratedFoundList().add(getDutControlProcessList().get(i).getDutSerialNumber());
				setMeterIdAlreadyCalibratedFoundOverAllStatus(true);

			}

		}

	}

	public boolean isMeterIdExistInBlackList(String readMeterId) {
		boolean status = false;
		ApplicationLauncher.logger.debug("isMeterIdExistInBlackList: Entry");
		return status;
	}

	public boolean isMeterIdExistInAlreadyCalibratedList(String readMeterId) {
		boolean status = false;

		return status;
	}

	public void updateResultsToDatabase() {
		ApplicationLauncher.logger.debug("updateResultsToDatabase :Entry");
		while (getDatabaseWriteQueue().peek() != null) {
			DutDatabaseWriteModel dbWriteData = getDatabaseWriteQueue().poll();
			if (dbWriteData.isResultForTestTypeWithCurrentParameter()) {
				ApplicationLauncher.logger.debug("updateResultsToDatabase : result with current parameter");
			} else {

				ApplicationLauncher.logger.debug("updateResultsToDatabase : result with out current parameter");

			}

			ApplicationHomeController.update_left_status("Finished Updating DB DeviceLDU ErrorData",
					ConstantApp.LEFT_STATUS_DEBUG);

		}

	}

	public static void enableRefStdLogResults() {
		ApplicationLauncher.logger.debug("enableRefStdLogResults :Entry");
	}

	public static void disableRefStdLogResults(int dutAddress) {
		ApplicationLauncher.logger.debug("disableRefStdLogResults :Entry");
		ApplicationLauncher.logger.debug("disableRefStdLogResults : dutAddress: " + dutAddress);

	}

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep :InterruptedException:" + e.getMessage());
		}

	}

	public void clearDutControlProcess() {
		ApplicationLauncher.logger.debug("clearDutControlProcess :Entry");
		clearDutControlProcessList();
		clearDutAddressList();
		clearDatabaseWriteQueue();
		clearMeterIdFoundList();
		clearBlackListedMeterIdFoundList();
		clearMeterIdAlreadyCalibratedFoundList();
		setBlackListedMeterIdFoundOverAllStatus(false);
		setMeterIdAlreadyCalibratedFoundOverAllStatus(false);
		setEmptyMeterIdFoundOverAllStatus(false);
		setProjectExitProcess(false);
	}

	public void clearDutControlProcessList() {
		ApplicationLauncher.logger.debug("clearDutControlProcessList :Entry");
		dutControlProcessList.clear();
	}

	public void clearDutAddressList() {
		ApplicationLauncher.logger.debug("clearDutAddressList :Entry");
		dutAddressList.clear();
	}

	public PriorityQueue<DutDatabaseWriteModel> getDatabaseWriteQueue() {
		ApplicationLauncher.logger.debug("getDatabaseWriteQueue :Entry");
		return databaseWriteQueue;
	}

	public void clearDatabaseWriteQueue() {
		ApplicationLauncher.logger.debug("clearDatabaseWriteQueue :Entry");
		this.databaseWriteQueue.clear();
	}

	public void setDatabaseWriteQueue(PriorityQueue<DutDatabaseWriteModel> databaseWriteQueue) {
		ApplicationLauncher.logger.debug("setDatabaseWriteQueue :Entry");
		this.databaseWriteQueue = databaseWriteQueue;
	}

	public void addDatabaseWriteQueue(DutDatabaseWriteModel databaseWrQueue, int dutAddress) {
		ApplicationLauncher.logger.debug("addDatabaseWriteQueue :Entry");
		ApplicationLauncher.logger.debug("addDatabaseWriteQueue : dutAddress : " + dutAddress);
		databaseWriteQueue.add(databaseWrQueue);
	}

	public ArrayList<DutControlProcess> getDutControlProcessList() {
		return dutControlProcessList;
	}

	public void setDutControlProcessList(ArrayList<DutControlProcess> dutControlProcessList) {
		this.dutControlProcessList = dutControlProcessList;
	}

	public void addDutControlProcessList(DutControlProcess dutControlProcess) {
		this.dutControlProcessList.add(dutControlProcess);
	}

	public ArrayList<String> getDutAddressList() {
		return dutAddressList;
	}

	public void setDutAddressList(ArrayList<String> dutAddressList) {
		this.dutAddressList = dutAddressList;
	}

	public void addDutAddressList(String dutAddress) {
		this.dutAddressList.add(dutAddress);
	}

	public boolean isMonitorDutControlProcessInitiated() {
		return monitorDutControlProcessInitiated;
	}

	public void setMonitorDutControlProcessInitiated(boolean monitorDutControlProcessInitiated) {
		this.monitorDutControlProcessInitiated = monitorDutControlProcessInitiated;
	}

	public boolean isDutAllControlProcessCompleted() {
		return dutAllControlProcessCompleted;
	}

	public void setDutAllControlProcessCompleted(boolean allControlProcessCompleted) {
		dutAllControlProcessCompleted = allControlProcessCompleted;
	}

	public ArrayList<String> getBlackListedMeterIdFoundList() {
		return blackListedMeterIdFoundList;
	}

	public void setBlackListedMeterIdFoundList(ArrayList<String> blackListedMeterIdFoundList) {
		this.blackListedMeterIdFoundList = blackListedMeterIdFoundList;
	}

	public void clearBlackListedMeterIdFoundList() {
		this.blackListedMeterIdFoundList.clear();
		;
	}

	public ArrayList<String> getMeterIdAlreadyCalibratedFoundList() {
		return meterIdAlreadyCalibratedFoundList;
	}

	public void setMeterIdAlreadyCalibratedFoundList(ArrayList<String> meterIdAlreadyCalibratedFoundList) {
		this.meterIdAlreadyCalibratedFoundList = meterIdAlreadyCalibratedFoundList;
	}

	public void clearMeterIdAlreadyCalibratedFoundList() {
		this.meterIdAlreadyCalibratedFoundList.clear();
	}

	public ArrayList<String> getEmptyMeterIdFoundList() {
		return emptyMeterIdFoundList;
	}

	public void setEmptyMeterIdFoundList(ArrayList<String> emptyMeterIdFoundList) {
		this.emptyMeterIdFoundList = emptyMeterIdFoundList;
	}

	public void clearMeterIdFoundList() {
		this.emptyMeterIdFoundList.clear();
	}

	public boolean isBlackListedMeterIdFoundOverAllStatus() {
		return blackListedMeterIdFoundOverAllStatus;
	}

	public void setBlackListedMeterIdFoundOverAllStatus(boolean blackListedMeterIdFoundOverAllStatus) {
		this.blackListedMeterIdFoundOverAllStatus = blackListedMeterIdFoundOverAllStatus;
	}

	public boolean isMeterIdAlreadyCalibratedFoundOverAllStatus() {
		return meterIdAlreadyCalibratedFoundOverAllStatus;
	}

	public void setMeterIdAlreadyCalibratedFoundOverAllStatus(boolean meterIdAlreadyCalibratedFoundOverAllStatus) {
		this.meterIdAlreadyCalibratedFoundOverAllStatus = meterIdAlreadyCalibratedFoundOverAllStatus;
	}

	public boolean isEmptyMeterIdFoundOverAllStatus() {
		return emptyMeterIdFoundOverAllStatus;
	}

	public void setEmptyMeterIdFoundOverAllStatus(boolean emptyMeterIdFoundOverAllStatus) {
		this.emptyMeterIdFoundOverAllStatus = emptyMeterIdFoundOverAllStatus;
	}

	public boolean isProjectExitProcess() {
		return projectExitProcess;
	}

	public void setProjectExitProcess(boolean projectExitProcess) {
		this.projectExitProcess = projectExitProcess;
	}

	public void setDutResultSummary(int lduAddress, String dutSummaryStatus) {

		ApplicationLauncher.logger.debug("setDutResultSummary : lduAddress: " + lduAddress);
		ApplicationLauncher.logger.debug("setDutResultSummary : dutSummaryStatus: " + dutSummaryStatus);
		this.dutResultSummary.put(lduAddress, dutSummaryStatus);

	}

	public void clearDutResultSummary() {
		dutResultSummary.clear();
	}

	public String getDutResultSummary(int lduAddress) {
		String dutSummaryStatus = ConstantReport.RESULT_STATUS_PASS.trim();
		// String dutSummaryStatus = ConstantReport.RESULT_STATUS_UNDEFINED.trim();
		try {
			if (dutResultSummary.containsKey(lduAddress)) {
				dutSummaryStatus = dutResultSummary.get(lduAddress);
			}
		} catch (Exception e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("getDutResultSummary : Exception :" + e.getMessage());
		}
		return dutSummaryStatus;
	}

	public String getDutResultResponse(int lduAddress) {
		// return dutResultResponse;
		String dutResponseData = "";// ConstantReport.RESULT_STATUS_PASS.trim();
		// String dutSummaryStatus = ConstantReport.RESULT_STATUS_UNDEFINED.trim();
		try {
			if (dutResultResponse.containsKey(lduAddress)) {
				dutResponseData = dutResultResponse.get(lduAddress);
			}
		} catch (Exception e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("getDutResultResponse : Exception :" + e.getMessage());
		}
		return dutResponseData;
	}

	public void setDutResultResponse(int lduAddress, String dutResponse) {
		this.dutResultResponse.put(lduAddress, dutResponse);
	}

}
