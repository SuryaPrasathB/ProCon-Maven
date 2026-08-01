package com.tasnetwork.calibration.conveyor.dut_executors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.deployment.DutResponse;
import com.tasnetwork.calibration.energymeter.director.DutCmdDirectorV3;
import com.tasnetwork.calibration.energymeter.serial.portmanagerV2.DutCmdManager;
import com.tasnetwork.calibration.energymeter.serial.portmanagerV2.SerialPortManagerDutCmd_V3;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;
import com.tasnetwork.spring.orm.model.DutCommand;
import com.tasnetwork.spring.orm.model.DutExecutionResult;

import javafx.scene.control.TableView;

public class DutCmdTestPointExecutor {

    private CountDownLatch completionLatch;
    private final AtomicInteger pendingTasks;

    private TableView<DutExecutionResult> tvExecutor;
    // private DutExecutorFtController dutExecutorController;
    private final ScheduledExecutorService scheduler;
    private final ConcurrentMap<String, ScheduledFuture<?>> activeTasks;
    // private final String deviceId= "";
    // private List<String> deviceIdList= new ArrayList<String>();

    public DutCmdTestPointExecutor() {
        this.scheduler = Executors.newScheduledThreadPool(10); // Optimal thread pool size
        this.activeTasks = new ConcurrentHashMap<>();
        this.pendingTasks = new AtomicInteger(0);
        this.completionLatch = new CountDownLatch(0);
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

    public void dutCmdExecuteWithTestPointTrigger(int testPointIndex, List<String> deviceIdList,
            DutCmdManager dutCmdManager) {
        ApplicationLauncher.logger.debug("dutCmdExecuteWithTestPointTrigger : Entry");

        // Create a new latch for this execution
        final CountDownLatch executionLatch = new CountDownLatch(deviceIdList.size());
        this.completionLatch = executionLatch; // Replace with new latch

        final AtomicBoolean cleanupScheduled = new AtomicBoolean(false);
        final List<String> dutPositionList = Collections.synchronizedList(new ArrayList<>());

        pendingTasks.addAndGet(deviceIdList.size());

        deviceIdList.stream().forEachOrdered(eachDeviceId -> {
            String lastTwoChars = eachDeviceId.substring(eachDeviceId.length() - 2);
            String dutPosition = String.valueOf(Integer.parseInt(lastTwoChars));
            String uniqueDeviceTaskId = eachDeviceId + "_" + dutPosition;

            dutPositionList.add(dutPosition);
            cancelExistingTask(uniqueDeviceTaskId);

            ScheduledFuture<?> future = scheduler.schedule(() -> {
                try {
                    executeDutCommandWithTestPointForPosition(testPointIndex, eachDeviceId, dutPosition, dutCmdManager);
                } finally {
                    executionLatch.countDown();
                    pendingTasks.decrementAndGet();

                    if (executionLatch.getCount() == 0 && cleanupScheduled.compareAndSet(false, true)) {
                        scheduler.schedule(() -> {
                            cleanupAfterExecution();
                        }, 2000, TimeUnit.MILLISECONDS);
                    }
                }
            }, 100, TimeUnit.MILLISECONDS);

            activeTasks.put(uniqueDeviceTaskId, future);
            Sleep(200);
        });

        ApplicationLauncher.logger.debug("dutCmdExecuteWithTestPointTrigger : Exit");
    }

    // Add method to wait for completion
    public void waitForCompletion(long timeout, TimeUnit unit) throws InterruptedException {
        completionLatch.await(timeout, unit);
    }

    public boolean isExecutionComplete() {
        return completionLatch.getCount() == 0 && pendingTasks.get() == 0;
    }

    private void cancelExistingTask(String lduPosition) {
        ScheduledFuture<?> existingTask = activeTasks.get(lduPosition);
        if (existingTask != null && !existingTask.isDone()) {
            existingTask.cancel(false);
            ApplicationLauncher.logger.debug("Cancelled previous task for position :" + lduPosition);
        }
    }

    private void executeDutCommandWithTestPointForPosition(int testPointIndex, String deviceId, String lduPosition,
            DutCmdManager dutCmdManager) {// DutCommand dutCommand) {
        try {
            ApplicationLauncher.logger
                    .debug("executeDutCommandForPosition: Position: " + lduPosition + " -> deviceId: " + deviceId);
            DutCommand dutCommand = dutCmdManager.getDutCommand();
            int dutPositionNo = Integer.parseInt(lduPosition);

            DutResponse dutResponse = new DutResponse();

            if (!dutCmdManager.isComSerialStatusConnected(dutPositionNo)) {
                // String deviceId = "010101QR01";
                // ApplicationLauncher.logger.debug("executeDutCommandForPosition: Hit1: ");
                if (dutCmdManager.getPortErrorMap(dutPositionNo).isEmpty()) {
                    dutResponse = dutCmdManager.dutCmdSerialPortAccessible(deviceId, dutPositionNo);
                } else {

                    dutResponse.setResponseData(dutCmdManager.getPortErrorMap(dutPositionNo));
                    ApplicationLauncher.logger
                            .debug("executeDutCommandForPosition : Already port Access failed - Status: Position:"
                                    + dutPositionNo + " -> getResponseData: " + dutResponse.getResponseData());

                }
            } else {
                dutResponse.setStatus(true);
                ApplicationLauncher.logger.debug("executeDutCommandForPosition: Comport already connected : Position: "
                        + lduPosition + " -> deviceId: " + deviceId);

            }
            // ApplicationLauncher.logger.debug("executeDutCommandForPosition: Hit2: ");
            if (dutResponse.isStatus()) {
                SerialPortManagerDutCmd_V3 dutSpm = dutCmdManager.getDutSpm(dutPositionNo);


                DutCmdDirectorV3 dutCmdDirectorV3 = new DutCmdDirectorV3(dutSpm);
                // dutResponse = dutCmdDirectorV3.dutMsngrSendCommandProcess();
                String sourceThread = deviceId;
                // ApplicationLauncher.logger.debug("executeDutCommandForPosition-2 : Position:
                // " + lduPosition + " ->isDutSerialStatusConnected: "+
                // dutCmdDirectorV3.getDutMessenger().getDutCmdSpmObj().isDutSerialStatusConnected());

                dutResponse = dutCmdDirectorV3.dutMsngrSendCommandProcessV2(dutPositionNo, dutCommand, sourceThread);
                // ApplicationLauncher.logger.debug("executeDutCommandForPosition: Position: " +
                // dutPositionNo + " -> getStatus: " + dutResponse.getStatus());
                ApplicationLauncher.logger.debug("executeDutCommandForPosition: Position: " + dutPositionNo
                        + " -> getResponseData: " + dutResponse.getResponseData());

                String dutResponseDataInHex = GuiUtils.asciiToHex(dutResponse.getResponseData());
                ApplicationLauncher.logger.debug("executeDutCommandForPosition: Position: " + dutPositionNo
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
                    //

                } else {
                    resultStatus = ConstantReport.RESULT_STATUS_FAIL;
                    resultValue = ConstantReport.REPORT_POPULATE_FAIL;
                }
                // LiveTableDataManager.UpdateliveTableData(dutPositionNo, resultStatus,
                // resultValue);

                String resultStatusFinal = resultStatus;
                String resultValueFinal = resultValue;
                /*
                 * getTvExecutor().getItems().stream()
                 * .filter(e->e.getTestPointName().equals(dutCmdManager.getDutCommand().
                 * getTestCaseName()))
                 * .forEach(e1->{
                 * if(dutPositionNo==1) {
                 * e1.setResultPosition1(resultStatusFinal+ resultValueFinal);
                 * }
                 * if(dutPositionNo==2) {
                 * e1.setResultPosition2(resultStatusFinal+ resultValueFinal);
                 * }
                 * });
                 */

                DutExecutionResult row = getTvExecutor().getItems().get(testPointIndex);

                if (dutPositionNo == 1) {
                    row.setResultPosition1(resultStatusFinal + resultValueFinal);
                } else if (dutPositionNo == 2) {
                    row.setResultPosition2(resultStatusFinal + resultValueFinal);
                } else if (dutPositionNo == 3) {
                    row.setResultPosition3(resultStatusFinal + resultValueFinal);
                } else if (dutPositionNo == 4) {
                    row.setResultPosition4(resultStatusFinal + resultValueFinal);
                } else if (dutPositionNo == 5) {
                    row.setResultPosition5(resultStatusFinal + resultValueFinal);
                } else if (dutPositionNo == 6) {
                    row.setResultPosition6(resultStatusFinal + resultValueFinal);
                }
                // dutCmdManager.dutCmdDisconnectPort_V2(dutPositionNo);
            } else {

                ApplicationLauncher.logger.debug("executeDutCommandForPosition : Access failed - Status: Position:"
                        + dutPositionNo + " -> getResponseData: " + dutResponse.getResponseData());
                String responseData = dutResponse.getResponseData();// (String)responseReturn.get("result");
                /*
                 * LiveTableDataManager.UpdateliveTableData(
                 * dutPositionNo,
                 * ConstantReport.RESULT_STATUS_FAIL,
                 * responseData
                 * );
                 */

                String resultStatusFinal = ConstantReport.RESULT_STATUS_FAIL;
                String resultValueFinal = responseData;
                /*
                 * getTvExecutor().getItems().stream()
                 * .filter(e->e.getTestPointName().equals(dutCmdManager.getDutCommand().
                 * getTestCaseName()))
                 * .forEach(e1->{
                 * if(dutPositionNo==1) {
                 * e1.setResultPosition1(resultStatusFinal+ resultValueFinal);
                 * }
                 * if(dutPositionNo==2) {
                 * e1.setResultPosition2(resultStatusFinal+ resultValueFinal);
                 * }
                 * });
                 */
                DutExecutionResult row = getTvExecutor().getItems().get(testPointIndex);

                if (dutPositionNo == 1) {
                    row.setResultPosition1(resultStatusFinal + resultValueFinal);
                } else if (dutPositionNo == 2) {
                    row.setResultPosition2(resultStatusFinal + resultValueFinal);
                } else if (dutPositionNo == 3) {
                    row.setResultPosition3(resultStatusFinal + resultValueFinal);
                } else if (dutPositionNo == 4) {
                    row.setResultPosition4(resultStatusFinal + resultValueFinal);
                } else if (dutPositionNo == 5) {
                    row.setResultPosition5(resultStatusFinal + resultValueFinal);
                } else if (dutPositionNo == 6) {
                    row.setResultPosition6(resultStatusFinal + resultValueFinal);
                }
                getTvExecutor().refresh();
                dutCmdManager.setPortErrorMap(dutPositionNo, dutResponse.getResponseData());
                // Sleep(2000);

            }
        } catch (Exception e) {
            ApplicationLauncher.logger
                    .error("Exception: Error executing command for position : " + lduPosition + " : Exception:" + e);
        } finally {
            activeTasks.remove(lduPosition);
        }
        // return dutCmdManager;
    }

    private void cleanupAfterExecution() {// DutCmdManager dutCmdManager,List<String> dutPositionList) {
        try {

            /*
             * for(String dutPositionNo : dutPositionList) {
             * dutCmdManager.dutCmdDisconnectPort_V2(Integer.parseInt(dutPositionNo));
             * ApplicationLauncher.logger.
             * debug("diconnecting port up after execution for position: " + dutPositionNo);
             * 
             * }
             */

            // Sleep(2000);
            // ProjectExecutionController.setExecuteTimeCounter(0);
        } catch (Exception e) {
            ApplicationLauncher.logger.error("CleanupAfterExecution: Exception:" + e);
        }
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

    public void Sleep(int timeInMsec) {

        try {
            Thread.sleep(timeInMsec);
        } catch (InterruptedException e) {

            e.printStackTrace();
            ApplicationLauncher.logger.error("Sleep2 :InterruptedException:" + e.getMessage());
        }

    }

    public TableView<DutExecutionResult> getTvExecutor() {
        return tvExecutor;
    }

    public void setTvExecutor(TableView<DutExecutionResult> tvExecutor) {
        this.tvExecutor = tvExecutor;
    }
}