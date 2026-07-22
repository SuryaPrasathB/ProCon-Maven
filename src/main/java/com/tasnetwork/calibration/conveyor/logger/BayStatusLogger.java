package com.tasnetwork.calibration.conveyor.logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.application.Platform;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;
import java.util.Map;
import java.util.HashMap;

public class BayStatusLogger {

    private static BayStatusLogger instance = null;
    private final int MAX_ROWS = 1000;
    
    // Map of BayKey to its ObservableList of logs
    private Map<String, ObservableList<TestInterfaceStatus>> bayLogs = new HashMap<>();
    
    // A consolidated list for the "All Bays" overview tab
    private ObservableList<TestInterfaceStatus> allBaysLog = FXCollections.observableArrayList();
    
    private BayStatusLogger() {
    }
    
    public static synchronized BayStatusLogger getInstance() {
        if (instance == null) {
            instance = new BayStatusLogger();
        }
        return instance;
    }
    
    public ObservableList<TestInterfaceStatus> getLogForBay(String bayKey) {
        if (!bayLogs.containsKey(bayKey)) {
            bayLogs.put(bayKey, FXCollections.observableArrayList());
        }
        return bayLogs.get(bayKey);
    }
    
    public ObservableList<TestInterfaceStatus> getAllBaysLog() {
        return allBaysLog;
    }
    
    public void logNewStatus(TestInterfaceStatus status) {
        Platform.runLater(() -> {
            String bayKey = status.getBayName();
            ObservableList<TestInterfaceStatus> bayList = getLogForBay(bayKey);
            
            bayList.add(status);
            allBaysLog.add(status);
            
            if (bayList.size() > MAX_ROWS) {
                bayList.remove(0);
            }
            if (allBaysLog.size() > MAX_ROWS) {
                allBaysLog.remove(0);
            }
        });
    }
    
    public void updateStatus(TestInterfaceStatus status) {
        Platform.runLater(() -> {
            String bayKey = status.getBayName();
            ObservableList<TestInterfaceStatus> bayList = getLogForBay(bayKey);
            
            // Update in Bay List
            for (int i = 0; i < bayList.size(); i++) {
                TestInterfaceStatus e = bayList.get(i);
                if (e != null && e.getSerialNo() != null && e.getSerialNo().equals(status.getSerialNo())) {
                    updateFields(e, status);
                    bayList.set(i, e); // Force TableView refresh
                    break;
                }
            }
            
            // Update in All Bays List
            for (int i = 0; i < allBaysLog.size(); i++) {
                TestInterfaceStatus e = allBaysLog.get(i);
                if (e != null && e.getSerialNo() != null && e.getSerialNo().equals(status.getSerialNo())) {
                    updateFields(e, status);
                    allBaysLog.set(i, e); // Force TableView refresh
                    break;
                }
            }
        });
    }
    
    private void updateFields(TestInterfaceStatus target, TestInterfaceStatus source) {
        target.setTestStatus(source.getTestStatus());
        target.setDeviceResponseStatus(source.getDeviceResponseStatus());
        target.setDeviceResponseData(source.getDeviceResponseData());
        target.setPathNo(source.getPathNo());
        target.setPositionNo(source.getPositionNo());
    }
}
