package com.tasnetwork.calibration.energymeter.setting;

import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.spring.orm.model.DeviceSetting;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class DeviceSettingPositionNoComboBoxValueFactory implements Callback<TableColumn.CellDataFeatures<DeviceSetting, ComboBox<String>>, ObservableValue<ComboBox<String>>> {
    @Override
    public ObservableValue<ComboBox<String>> call(CellDataFeatures<DeviceSetting, ComboBox<String>> param) {
        DeviceSetting rowData = param.getValue();
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPrefWidth(100);

        // Populate ComboBox initially
        populatePositionComboBox(rowData, comboBox);

        // Set initial value for positionNo
        if (rowData.getPositionNo() != null && !rowData.getPositionNo().isEmpty()) {
            comboBox.setValue(rowData.getPositionNo());
            updateCName(rowData, comboBox.getSelectionModel().getSelectedItem());
        } else if (!comboBox.getItems().isEmpty()) {
            comboBox.getSelectionModel().select(0);
            rowData.setPositionNo(comboBox.getSelectionModel().getSelectedItem());
            
        }

        // Add listener for positionNo selection
        comboBox.setOnAction(e -> {
            String selectedPositionNo = comboBox.getSelectionModel().getSelectedItem();
            rowData.setPositionNo(selectedPositionNo);

            // Update cName dynamically based on positionNo selection
            updateCName(rowData, selectedPositionNo);
        });

        // Add listeners for clusterName and bayName to dynamically update positionNo ComboBox
        rowData.getClusterNameProperty().addListener((obs, oldVal, newVal) -> {
            populatePositionComboBox(rowData, comboBox);
            updateCName(rowData, comboBox.getSelectionModel().getSelectedItem());
        });

        rowData.getBayNameProperty().addListener((obs, oldVal, newVal) -> {
            populatePositionComboBox(rowData, comboBox);
            updateCName(rowData, comboBox.getSelectionModel().getSelectedItem());
        });

        return new SimpleObjectProperty<>(comboBox);
    }

    private void populatePositionComboBox(DeviceSetting rowData, ComboBox<String> comboBox) {
    	try {
	        comboBox.getItems().clear();
	        String clusterName = rowData.getClusterName();
	        String bayName = rowData.getBayName();
/*	        ApplicationLauncher.logger.debug("populatePositionComboBox: getDeviceType: " + rowData.getDeviceType());
	        ApplicationLauncher.logger.debug("populatePositionComboBox: getDeviceTypeKey: " + rowData.getDeviceTypeKey());
	        ApplicationLauncher.logger.debug("populatePositionComboBox: clusterName: " + clusterName);
	        ApplicationLauncher.logger.debug("populatePositionComboBox: bayName: " + bayName);*/
	        if (clusterName != null && bayName != null) {
	            String key = clusterName + "_" + bayName;
	            //ApplicationLauncher.logger.debug("populatePositionComboBox: key: " + key);
	            
	            if (BayUtils.getFilteredClusterBayNamePositionListMap().get(rowData.getDeviceType()).containsKey(key)) {
	                comboBox.getItems().addAll(BayUtils.getFilteredClusterBayNamePositionListMap().get(rowData.getDeviceType()).get(key));
	                //comboBox.getSelectionModel().select(0); // Default to the first item
	                //rowData.setPositionNo(comboBox.getSelectionModel().getSelectedItem());
	                if(BayUtils.getFilteredClusterBayNamePositionListMap().get(rowData.getDeviceType()).get(key).contains(rowData.getPositionNo())) {
	                	comboBox.getSelectionModel().select(rowData.getPositionNo()); 	
	                }else {
	                	comboBox.getSelectionModel().select(0); 
	                	rowData.setPositionNo(comboBox.getSelectionModel().getSelectedItem());
	                }
	            } else {
	            	ApplicationLauncher.logger.debug("populatePositionComboBox : No positions found for key: " + key);
	                rowData.setPositionNo(null);
	            }
	        } else {
	        	ApplicationLauncher.logger.debug("populatePositionComboBox : Cluster or Bay Name is null");
	        }
    	}catch(Exception e) {
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("populatePositionComboBox: Exception: getDeviceType: " + rowData.getDeviceType() + " : " + e.getMessage());
    	}
    }

    private void updateCName(DeviceSetting rowData, String positionNo) {
        String clusterName = rowData.getClusterName();
        String bayName = rowData.getBayName();

        if (clusterName != null && bayName != null && positionNo != null) {
            String key = clusterName + "_" + bayName + "_" + positionNo;
            
            if (BayUtils.getFilteredClusterBayPositionNoCnameMap().get(rowData.getDeviceType()).containsKey(key)) {
                rowData.setCanName(BayUtils.getFilteredClusterBayPositionNoCnameMap().get(rowData.getDeviceType()).get(key));
            } else {
                rowData.setCanName("");
            }
        } else {
            rowData.setCanName("");
        }
    }
}