package com.tasnetwork.calibration.conveyor.dut_executors;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.deployment.DutResponse;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.director.DutCmdDirectorV3;
import com.tasnetwork.calibration.energymeter.serial.portmanagerV2.DutCmdManager;
import com.tasnetwork.calibration.energymeter.serial.portmanagerV2.SerialPortManagerDutCmd_V3;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;
import com.tasnetwork.spring.orm.model.DutCommand;
import com.tasnetwork.spring.orm.model.DutExecutionResult;
import com.tasnetwork.spring.orm.model.TerminalProfileSetting;

import javafx.application.Platform;
import javafx.scene.control.TableView;

public class DutCmdIndividualDeviceExecutor {

    private Consumer<Double> progressCallback;
    private DutExecutionResult summaryRow;
    private Logger eachBaylogger = null;
    private Map<Integer, AtomicInteger> positionPassCount = new ConcurrentHashMap<>();
    private Map<Integer, AtomicInteger> positionFailCount = new ConcurrentHashMap<>();
    private Map<Integer, AtomicInteger> positionTotalExecutedCount = new ConcurrentHashMap<>();
    // private boolean userAborted = false;
    private AtomicBoolean isUserCancelled = new AtomicBoolean(false);
    private String parentBayName = "";
    private int totalTestCount = 0;

    public ConcurrentMap<String, DutCmdManager> deviceTypeDutCmdManagerMap = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, CountDownLatch> testPointCompletionMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Map<String, String>> testPointResultsMap = new ConcurrentHashMap<>();

    private CountDownLatch executionLatch;
    private CountDownLatch completionLatch;
    private final AtomicInteger pendingTasks;

    // private Object parentObject ;

    private TableView<DutExecutionResult> tvExecutor;
    // private DutExecutorFtController dutExecutorController;
    private final ScheduledExecutorService scheduler;
    private final ConcurrentMap<String, ScheduledFuture<?>> activeTasks;
    // private final String deviceId= "";
    // private List<String> deviceIdList= new ArrayList<String>();

    public DutCmdIndividualDeviceExecutor() {// Object parent) {
        this.scheduler = Executors.newScheduledThreadPool(10); // Optimal thread pool size
        this.activeTasks = new ConcurrentHashMap<>();
        this.pendingTasks = new AtomicInteger(0);
        this.completionLatch = new CountDownLatch(0);

        positionPassCount = new ConcurrentHashMap<>();
        positionFailCount = new ConcurrentHashMap<>();
        positionTotalExecutedCount = new ConcurrentHashMap<>();

        int maxDutSupported = DeviceDataManagerController.getConveyorConfigParsedKey().getMaxDutSupported();

        for (int i = 1; i <= maxDutSupported; i++) {
            positionPassCount.put(i, new AtomicInteger(0));
            positionFailCount.put(i, new AtomicInteger(0));
            positionTotalExecutedCount.put(i, new AtomicInteger(0));
        }
        // this.parentObject = parent;
        // this.deviceIdList = myDeviceIdList;
    }

    public void dutExecuteCommandTrigger() {
        ApplicationLauncher.logger.debug("dutExecuteCommandTrigger : Entry");
        String lduPosition = "1";
        // ProjectExecutionController.getDeviceMountedMap().keySet().forEach(lduPosition
        // -> {
        // Cancel previous task for same position if still running
        String deviceId = "010101QR01";
        String uniqueDeviceTaskId = deviceId + "_" + lduPosition;
        cancelExistingTask(uniqueDeviceTaskId);

        ScheduledFuture<?> future = scheduler.schedule(() -> {
            // executeDutCommandForPosition(deviceId,lduPosition);
            // cleanupAfterExecution(lduPosition);
        }, 100, TimeUnit.MILLISECONDS);

        activeTasks.put(uniqueDeviceTaskId, future);
        // });

        ApplicationLauncher.logger.debug("dutExecuteCommandTrigger : Exit");
    }

    public void dutCmdExecuteWithIndividualDeviceTrigger(List<String> deviceIdList, List<DutCommand> dutCommandList,
            TerminalProfileSetting terminalBayProfile) {
        eachBaylogger.debug("dutCmdExecuteWithIndividualDeviceTrigger : Entry");

        // Initialize completion tracking for each test point
        for (DutCommand dutCommand : dutCommandList) {
            String testCaseName = dutCommand.getTestCaseName();
            testPointCompletionMap.put(testCaseName, new CountDownLatch(deviceIdList.size()));
            testPointResultsMap.put(testCaseName, new ConcurrentHashMap<>());
        }

        this.executionLatch = new CountDownLatch(deviceIdList.size() * dutCommandList.size());
        this.completionLatch = executionLatch;
        setTotalTestCount(dutCommandList.size());
        pendingTasks.addAndGet(deviceIdList.size() * dutCommandList.size());
        int totalDeviceCount = deviceIdList.size();

        for (int i = 0; i < totalDeviceCount; i++) {
            String dutPositionNo = deviceIdList.get(i);

            ScheduledFuture<?> future = scheduler.schedule(() -> {
                try {
                    executeAllTestPoint(dutPositionNo, dutCommandList, terminalBayProfile);
                } finally {
                    // executionLatch.countDown();
                    pendingTasks.decrementAndGet();
                }
            }, 100, TimeUnit.MILLISECONDS);

            activeTasks.put(dutPositionNo, future);
            Sleep(200);
        }

        eachBaylogger.debug("dutCmdExecuteWithIndividualDeviceTrigger : Exit");
    }

    public void executeAllTestPoint(String dutPositionNo, List<DutCommand> dutCommandList,
            TerminalProfileSetting terminalBayProfile) {
        eachBaylogger.debug("executeAllTestPoint : Entry : dutPositionNo: " + dutPositionNo);

        int totalTestCount = dutCommandList.size();
        eachBaylogger.debug("executeAllTestPoint : totalTestCount: " + totalTestCount);
        int positionNo = Integer.parseInt(dutPositionNo);

        // Clear all results for this position first
        for (int i = 0; i < totalTestCount; i++) {
            DutExecutionResult tvTestPointRow = getTvExecutor().getItems().get(i);
            clearPositionResult(tvTestPointRow, positionNo);
        }

        Platform.runLater(() -> getTvExecutor().refresh());

        for (int i = 0; i < totalTestCount; i++) {
            if (getIsUserCancelled().get()) {
                eachBaylogger.debug("Execution aborted by user: " + getParentBayName() + " -> " + dutPositionNo);

                setPositionResult(tvExecutor.getItems().get(i), positionNo, ConstantApp.EXECUTION_STATUS_ABORTED);
                break;
            }

            DutCommand dutCommand = dutCommandList.get(i);
            String testCaseName = dutCommand.getTestCaseName();
            String targetDeviceType = dutCommand.getTargetDeviceType();
            String deviceIdPrefix = terminalBayProfile.getTerminalId() + terminalBayProfile.getClusterId() +
                    terminalBayProfile.getBayId() + targetDeviceType;
            String deviceId = deviceIdPrefix + dutPositionNo;
            String targetDeviceTypeUniqueId = targetDeviceType + "_" + dutPositionNo;
            eachBaylogger.debug("executeAllTestPoint : targetDeviceTypeUniqueId: " + targetDeviceTypeUniqueId
                    + " : dutPositionNo: " + dutPositionNo);
            for (Entry<String, DutCmdManager> eachDeviceTypeDutManager : getDeviceTypeDutCmdManagerMap().entrySet()) {
                eachBaylogger.debug(
                        "executeAllTestPoint : eachDeviceTypeDutManager: key: " + eachDeviceTypeDutManager.getKey());

            }

            if (!getDeviceTypeDutCmdManagerMap().containsKey(targetDeviceTypeUniqueId)) {
                addDeviceTypeDutCmdManagerMap(targetDeviceTypeUniqueId, new DutCmdManager());
            }

            DutCmdManager dutCmdManager = getDeviceTypeDutCmdManagerMap().get(targetDeviceTypeUniqueId);
            dutCmdManager.setDutCommand(dutCommand);

            try {
                executeDutCommandForPosition(i, deviceId, dutPositionNo, dutCmdManager);

                // Store result for this test point and device
                DutExecutionResult row = getTvExecutor().getItems().get(i);
                String result = getPositionResult(row, positionNo);
                testPointResultsMap.get(testCaseName).put(dutPositionNo, result);

            } catch (Exception e) {
                eachBaylogger.error("Error executing test point: " + testCaseName + " for device: " + dutPositionNo, e);
                testPointResultsMap.get(testCaseName).put(dutPositionNo, ConstantReport.RESULT_STATUS_FAIL + "Error");
            }

            // Count down for this test point completion
            testPointCompletionMap.get(testCaseName).countDown();

            // Check if all devices have completed this test point
            if (testPointCompletionMap.get(testCaseName).getCount() == 0) {
                updateTestPointSummary(testCaseName);
            }
            if (!getIsUserCancelled().get()) {
                Sleep(5000);
            }
            executionLatch.countDown();
        }
    }

    private void updateTestPointSummary(String testCaseName) {

        Platform.runLater(() -> {
            // Find the test point row
            for (DutExecutionResult row : getTvExecutor().getItems()) {
                if (testCaseName.equals(row.getTestPointName())) {
                    // Calculate overall status for this test point
                    Map<String, String> results = testPointResultsMap.get(testCaseName);
                    boolean allPassed = results.values().stream()
                            .allMatch(result -> result.startsWith(ConstantReport.RESULT_STATUS_PASS));

                    boolean anyFailed = results.values().stream()
                            .anyMatch(result -> result.startsWith(ConstantReport.RESULT_STATUS_FAIL));

                    String overallTestPointStatus;
                    if (allPassed) {
                        overallTestPointStatus = ConstantApp.EXECUTION_STATUS_COMPLETED;// ConstantReport.RESULT_STATUS_PASS
                                                                                        // +
                                                                                        // ConstantReport.REPORT_POPULATE_PASS;
                    } else if (anyFailed) {
                        overallTestPointStatus = ConstantReport.REPORT_POPULATE_FAIL;
                    } else {
                        overallTestPointStatus = ConstantReport.REPORT_POPULATE_UNDEFINED;// "Unknown status";
                    }

                    // Update the overall status column
                    row.setTestPointExecutionStatus(overallTestPointStatus);
                    break;
                }
            }
            getTvExecutor().refresh();
            int noOfTestCompleted = (int) getTvExecutor().getItems().stream()
                    .filter(e -> !e.isSummaryRow())
                    .filter(e1 -> !e1.getTestPointExecutionStatus().equals(ConstantApp.EXECUTION_STATUS_NOT_EXECUTED))
                    .count();
            updateProgress(noOfTestCompleted);
        });
    }

    private String getPositionResult(DutExecutionResult row, int positionNo) {
        switch (positionNo) {
            case 1:
                return row.getResultPosition1();
            case 2:
                return row.getResultPosition2();
            case 3:
                return row.getResultPosition3();
            case 4:
                return row.getResultPosition4();
            case 5:
                return row.getResultPosition5();
            case 6:
                return row.getResultPosition6();
            default:
                return "";
        }
    }

    private void setPositionResult(DutExecutionResult row, int positionNo, String value) {
        switch (positionNo) {
            case 1:
                row.setResultPosition1(value);
                break;
            case 2:
                row.setResultPosition2(value);
                break;
            case 3:
                row.setResultPosition3(value);
                break;
            case 4:
                row.setResultPosition4(value);
                break;
            case 5:
                row.setResultPosition5(value);
                break;
            case 6:
                row.setResultPosition6(value);
                break;
            default:
                break;
        }
    }

    private void clearPositionResult(DutExecutionResult row, int positionNo) {
        switch (positionNo) {
            case 1:
                row.setResultPosition1("");
                break;
            case 2:
                row.setResultPosition2("");
                break;
            case 3:
                row.setResultPosition3("");
                break;
            case 4:
                row.setResultPosition4("");
                break;
            case 5:
                row.setResultPosition5("");
                break;
            case 6:
                row.setResultPosition6("");
                break;
        }
    }

    // Add method to wait for completion
    /*
     * public void waitForCompletion(long timeout, TimeUnit unit) throws
     * InterruptedException {
     * completionLatch.await(timeout, unit);
     * }
     */

    public void cancelExecution() {
        setIsUserCancelled(true);

        // Cancel all active tasks
        activeTasks.values().forEach(task -> task.cancel(true));
        activeTasks.clear();

        // Interrupt the wait by counting down the latch to zero
        while (completionLatch.getCount() > 0) {
            completionLatch.countDown();
        }

        getDeviceTypeDutCmdManagerMap().entrySet().forEach(e -> {
            e.getValue().setUserAborted(true);
        });

        // Shutdown scheduler if needed
        scheduler.shutdownNow();

        // setPositionResult(tvExecutor.getItems().get(i),positionNo,ConstantApp.EXECUTION_STATUS_ABORTED);

    }

    public void waitForCompletion(long timeout, TimeUnit unit) throws InterruptedException {
        if (getIsUserCancelled().get()) {
            throw new InterruptedException("Execution cancelled by user");
        }

        long endTime = System.currentTimeMillis() + unit.toMillis(timeout);

        while (completionLatch.getCount() > 0) {
            if (getIsUserCancelled().get()) {
                throw new InterruptedException("Execution cancelled by user");
            }

            long remaining = endTime - System.currentTimeMillis();
            if (remaining <= 0) {
                // Check if we were cancelled during the timeout
                if (getIsUserCancelled().get()) {
                    throw new InterruptedException("Execution cancelled by user");
                }
                throw new InterruptedException("Execution timed out");
            }

            // Wait in smaller chunks to allow cancellation
            long waitTime = Math.min(remaining, 100); // Check every 100ms
            try {
                if (completionLatch.await(waitTime, TimeUnit.MILLISECONDS)) {
                    return; // Completed normally
                }
            } catch (InterruptedException e) {
                // Check if the interruption was due to cancellation
                if (getIsUserCancelled().get()) {
                    throw new InterruptedException("Execution cancelled by user");
                }
                throw e; // Re-throw if it's a different interruption
            }
        }
    }

    public boolean isExecutionComplete() {
        return completionLatch.getCount() == 0 && pendingTasks.get() == 0;
    }

    private void cancelExistingTask(String lduPosition) {
        ScheduledFuture<?> existingTask = activeTasks.get(lduPosition);
        if (existingTask != null && !existingTask.isDone()) {
            existingTask.cancel(false);
            eachBaylogger.debug("Cancelled previous task for position :" + lduPosition);
        }
    }

    private void executeDutCommandForPosition(int testPointIndex, String deviceId, String lduPosition,
            DutCmdManager dutCmdManager) {// DutCommand dutCommand) {
        try {
            eachBaylogger.debug("executeDutCommandForPosition: Position: " + lduPosition + " -> deviceId: " + deviceId);
            DutCommand dutCommand = dutCmdManager.getDutCommand();
            int dutPositionNo = Integer.parseInt(lduPosition);

            DutExecutionResult tvTestPointRow = getTvExecutor().getItems().get(testPointIndex);
            clearPositionResult(tvTestPointRow, dutPositionNo);

            DutResponse dutResponse = new DutResponse();

            if (!dutCmdManager.isComSerialStatusConnected(dutPositionNo)) {
                // String deviceId = "010101QR01";
                // eachBaylogger.debug("executeDutCommandForPosition: Hit1: ");
                if (dutCmdManager.getPortErrorMap(dutPositionNo).isEmpty()) {
                    dutResponse = dutCmdManager.dutCmdSerialPortAccessible(deviceId, dutPositionNo);
                } else {

                    dutResponse.setResponseData(dutCmdManager.getPortErrorMap(dutPositionNo));
                    eachBaylogger.debug("executeDutCommandForPosition : Already port Access failed - Status: Position:"
                            + dutPositionNo + " -> getResponseData: " + dutResponse.getResponseData());

                }
            } else {
                dutResponse.setStatus(true);
                eachBaylogger.debug("executeDutCommandForPosition: Comport already connected : Position: " + lduPosition
                        + " -> deviceId: " + deviceId);

            }
            // eachBaylogger.debug("executeDutCommandForPosition: Hit2: ");
            if (dutResponse.isStatus()) {
                SerialPortManagerDutCmd_V3 dutSpm = dutCmdManager.getDutSpm(dutPositionNo);

                DutCmdDirectorV3 dutCmdDirectorV3 = new DutCmdDirectorV3(dutSpm);

                String sourceThread = deviceId;

                dutResponse = dutCmdDirectorV3.dutMsngrSendCommandProcessV2(dutPositionNo, dutCommand, sourceThread);

                eachBaylogger.debug("executeDutCommandForPosition: Position: " + dutPositionNo + " -> getResponseData: "
                        + dutResponse.getResponseData());

                String dutResponseDataInHex = GuiUtils.asciiToHex(dutResponse.getResponseData());
                eachBaylogger.debug("executeDutCommandForPosition: Position: " + dutPositionNo
                        + " -> dutResponseDataInHex:" + dutResponseDataInHex);
                String resultStatus, resultValue;
                resultValue = dutResponse.getResponseData();// (String)responseReturn.get("result");

                if (dutResponse.getStatus()) {
                    resultStatus = ConstantReport.RESULT_STATUS_PASS;
                    if (resultValue.equals("")) {
                        resultValue = ConstantReport.REPORT_POPULATE_PASS;
                    } else {
                        resultValue = resultValue.replaceAll("^[\\r\\n]+|[\\r\\n]+$", "");// remove \r\n at the end
                    }
                    positionPassCount.get(dutPositionNo).incrementAndGet();
                    //

                } else {
                    resultStatus = ConstantReport.RESULT_STATUS_FAIL;
                    resultValue = ConstantReport.REPORT_POPULATE_FAIL;
                    positionFailCount.get(dutPositionNo).incrementAndGet();
                }
                // LiveTableDataManager.UpdateliveTableData(dutPositionNo, resultStatus,
                // resultValue);

                String resultStatusFinal = resultStatus;
                String resultValueFinal = resultValue;

                setPositionResult(tvTestPointRow, dutPositionNo, resultStatusFinal + resultValueFinal);

                // dutCmdManager.dutCmdDisconnectPort_V2(dutPositionNo);
            } else {

                eachBaylogger.debug("executeDutCommandForPosition : Access failed - Status: Position:" + dutPositionNo
                        + " -> getResponseData: " + dutResponse.getResponseData());
                String responseData = dutResponse.getResponseData();// (String)responseReturn.get("result");
                /*
                 * LiveTableDataManager.UpdateliveTableData(
                 * dutPositionNo,
                 * ConstantReport.RESULT_STATUS_FAIL,
                 * responseData
                 * );
                 */

                String resultStatusFinal = ConstantReport.RESULT_STATUS_FAIL;
                positionFailCount.get(dutPositionNo).incrementAndGet();
                String resultValueFinal = responseData;

                DutExecutionResult row = getTvExecutor().getItems().get(testPointIndex);

                setPositionResult(row, dutPositionNo, resultStatusFinal + resultValueFinal);

                getTvExecutor().refresh();
                dutCmdManager.setPortErrorMap(dutPositionNo, dutResponse.getResponseData());

            }
            positionTotalExecutedCount.get(dutPositionNo).incrementAndGet();
            eachBaylogger.debug("executeDutCommandForPosition: dutPositionNo: " + dutPositionNo + " -HitX");
            updateSummaryForPosition(dutPositionNo);
        } catch (Exception e) {
            eachBaylogger
                    .error("Exception: Error executing command for position : " + lduPosition + " : Exception:" + e);
            int dutPositionNo = Integer.parseInt(lduPosition);
            positionFailCount.get(dutPositionNo).incrementAndGet();
            positionTotalExecutedCount.get(dutPositionNo).incrementAndGet();
            eachBaylogger.debug("executeDutCommandForPosition: dutPositionNo: " + dutPositionNo + " -HitY");
            updateSummaryForPosition(dutPositionNo);
        } finally {
            activeTasks.remove(lduPosition);
        }

    }

    public void setProgressCallback(Consumer<Double> progressCallback) {
        this.progressCallback = progressCallback;
    }

    private void updateProgress(int noOfTestCompleted) {
        eachBaylogger.debug("updateProgress: getTotalTestCount(): " + getTotalTestCount());
        if (progressCallback != null && getTotalTestCount() > 0) {
            double progress = (double) noOfTestCompleted / getTotalTestCount();
            progressCallback.accept(progress);
        }
    }

    private void updateSummaryForPosition(int positionNo) {
        eachBaylogger.debug("updateSummaryForPosition: positionNo: " + positionNo);
        eachBaylogger.debug("updateSummaryForPosition: positionNo: " + positionNo +
                " | Pass: " + positionPassCount.get(positionNo).get() +
                " | Fail: " + positionFailCount.get(positionNo).get() +
                " | Total: " + positionTotalExecutedCount.get(positionNo).get());
        Platform.runLater(() -> {
            if (summaryRow != null) {
                int passCount = positionPassCount.get(positionNo).get();
                int totalCount = positionTotalExecutedCount.get(positionNo).get();
                int failCount = positionFailCount.get(positionNo).get();

                String summaryText;
                if (totalCount == 0) {
                    eachBaylogger.debug("updateSummaryForPosition: positionNo: " + positionNo + " -Hit1");

                    summaryText = "Pending";
                } else if (failCount == 0) {
                    eachBaylogger.debug("updateSummaryForPosition: positionNo: " + positionNo + " -Hit2");
                    summaryText = ConstantReport.RESULT_STATUS_PASS + passCount + "/" + totalCount;
                } else if (passCount == 0) {
                    eachBaylogger.debug("updateSummaryForPosition: positionNo: " + positionNo + " -Hit3");
                    summaryText = ConstantReport.RESULT_STATUS_FAIL + failCount + "/" + totalCount;
                } else {
                    eachBaylogger.debug("updateSummaryForPosition: positionNo: " + positionNo + " -Hit4");
                    summaryText = ConstantReport.RESULT_STATUS_FAIL + passCount + "/" + totalCount + " (" + failCount
                            + " failed)";
                }

                switch (positionNo) {
                    case 1:
                        summaryRow.setResultPosition1(summaryText);
                        break;
                    case 2:
                        summaryRow.setResultPosition2(summaryText);
                        break;
                    case 3:
                        summaryRow.setResultPosition3(summaryText);
                        break;
                    case 4:
                        summaryRow.setResultPosition4(summaryText);
                        break;
                    case 5:
                        summaryRow.setResultPosition5(summaryText);
                        break;
                    case 6:
                        summaryRow.setResultPosition6(summaryText);
                        break;
                }

                getTvExecutor().refresh();

            }
        });
    }

    public void resetSummary() {

        int maxDutSupported = DeviceDataManagerController.getConveyorConfigParsedKey().getMaxDutSupported();

        for (int i = 1; i <= maxDutSupported; i++) {
            positionPassCount.get(i).set(0);
            positionFailCount.get(i).set(0);
            positionTotalExecutedCount.get(i).set(0);
        }

        Platform.runLater(() -> {
            if (summaryRow != null) {
                summaryRow.setResultPosition1("Pending");
                summaryRow.setResultPosition2("Pending");
                summaryRow.setResultPosition3("Pending");
                summaryRow.setResultPosition4("Pending");
                summaryRow.setResultPosition5("Pending");
                summaryRow.setResultPosition5("Pending");
                getTvExecutor().refresh();
            }
        });
    }

    public void shutdown() {
        // Cancel all active tasks
        activeTasks.values().forEach(task -> task.cancel(false));
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /*
     * public void Sleep(int timeInMsec) {
     * 
     * try {
     * Thread.sleep(timeInMsec);
     * } catch (InterruptedException e) {
     * 
     * e.printStackTrace();
     * eachBaylogger.error("Sleep2 :InterruptedException:"+ e.getMessage());
     * }
     * 
     * }
     */

    public void Sleep(int timeInMsec) {
        try {
            Thread.sleep(timeInMsec);
        } catch (InterruptedException e) {
            // Check if this interruption is due to cancellation
            if (getIsUserCancelled().get()) {
                eachBaylogger.debug("Sleep : Sleep interrupted due to cancellation");
                Thread.currentThread().interrupt(); // Preserve interrupt status
                return; // Exit gracefully
            }
            // If not due to cancellation, log and re-interrupt
            eachBaylogger.error("Sleep : Sleep interrupted unexpectedly : Exception: " + e.getMessage());
            Thread.currentThread().interrupt();
            throw new RuntimeException("Sleep interrupted", e);
        }
    }

    public TableView<DutExecutionResult> getTvExecutor() {
        return tvExecutor;
    }

    public void setTvExecutor(TableView<DutExecutionResult> tvExecutor) {
        this.tvExecutor = tvExecutor;
        int totelTestPoint = this.tvExecutor.getItems().size() + 1;
        Platform.runLater(() -> {
            this.tvExecutor.getItems().removeIf(DutExecutionResult::isSummaryRow);
            summaryRow = new DutExecutionResult(true);
            summaryRow.setTestPointName("OverallDeviceStatus");
            summaryRow.setSummaryRow(true);
            summaryRow.setSerialNo(totelTestPoint);
            tvExecutor.getItems().add(summaryRow);
        });
    }

    public void resetAllCounters() {
        // Reset all position counters
        int maxDutSupported = DeviceDataManagerController.getConveyorConfigParsedKey().getMaxDutSupported();

        for (int i = 1; i <= maxDutSupported; i++) {
            positionPassCount.get(i).set(0);
            positionFailCount.get(i).set(0);
            positionTotalExecutedCount.get(i).set(0);
        }

        // Reset summary row
        Platform.runLater(() -> {
            if (summaryRow != null) {
                summaryRow.setResultPosition1("Pending");
                summaryRow.setResultPosition2("Pending");
                summaryRow.setResultPosition3("Pending");
                summaryRow.setResultPosition4("Pending");
                summaryRow.setResultPosition5("Pending");
                summaryRow.setResultPosition6("Pending");
                getTvExecutor().refresh();
            }
        });
    }

    public Map<String, DutCmdManager> getDeviceTypeDutCmdManagerMap() {
        return deviceTypeDutCmdManagerMap;
    }

    public void addDeviceTypeDutCmdManagerMap(String deviceType, DutCmdManager dutCmdManager) {
        this.deviceTypeDutCmdManagerMap.put(deviceType, dutCmdManager);
    }

    public String getParentBayName() {
        return parentBayName;
    }

    public void setParentBayName(String parentBayName) {
        this.parentBayName = parentBayName;
    }

    public Logger getEachBaylogger() {
        return eachBaylogger;
    }

    public void setEachBaylogger(Logger eachBaylogger) {
        this.eachBaylogger = eachBaylogger;
    }

    public AtomicBoolean getIsUserCancelled() {
        return isUserCancelled;
    }

    public void setIsUserCancelled(boolean userCancelled) {
        this.isUserCancelled.set(userCancelled);
    }

    public int getTotalTestCount() {
        return totalTestCount;
    }

    public void setTotalTestCount(int totalTestCount) {
        this.totalTestCount = totalTestCount;
    }

    /*
     * public List<String> getDeviceIdList() {
     * return deviceIdList;
     * }
     * 
     * 
     * 
     * public void setDeviceIdList(List<String> deviceIdList) {
     * this.deviceIdList = deviceIdList;
     * }
     */
}