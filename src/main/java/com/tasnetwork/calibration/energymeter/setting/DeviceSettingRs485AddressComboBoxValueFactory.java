package com.tasnetwork.calibration.energymeter.setting;

import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.spring.orm.model.DeviceSetting;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class DeviceSettingRs485AddressComboBoxValueFactory implements Callback<TableColumn.CellDataFeatures<DeviceSetting, ComboBox<String>>, ObservableValue<ComboBox<String>>> {
    @Override
    public ObservableValue<ComboBox<String>> call(CellDataFeatures<DeviceSetting, ComboBox<String>> param) {
        DeviceSetting rowData = param.getValue();
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPrefWidth(100);

        // Populate ComboBox initially
        populateRs485AddressComboBox(rowData, comboBox);
        comboBox.getSelectionModel().select(0);


        comboBox.setOnAction(e -> {
            String selectedRs485Address = comboBox.getSelectionModel().getSelectedItem();
            rowData.setRs485Address(selectedRs485Address);

        });
        // Add listeners for clusterName and bayName to dynamically update positionNo ComboBox
        rowData.getClusterNameProperty().addListener((obs, oldVal, newVal) -> {
            populateRs485AddressComboBox(rowData, comboBox);
        });

        rowData.getBayNameProperty().addListener((obs, oldVal, newVal) -> {
            populateRs485AddressComboBox(rowData, comboBox);
        });

        return new SimpleObjectProperty<>(comboBox);
    }

    private void populateRs485AddressComboBox(DeviceSetting rowData, ComboBox<String> comboBox) {
    	try {
	        comboBox.getItems().clear();
	        String clusterName = rowData.getClusterName();
	        String bayName = rowData.getBayName();
	       /* ApplicationLauncher.logger.debug("populateRs485AddressComboBox: getDeviceType: " + rowData.getDeviceType());
	        ApplicationLauncher.logger.debug("populateRs485AddressComboBox: getDeviceTypeKey: " + rowData.getDeviceTypeKey());
	        ApplicationLauncher.logger.debug("populateRs485AddressComboBox: clusterName: " + clusterName);
	        ApplicationLauncher.logger.debug("populateRs485AddressComboBox: bayName: " + bayName);*/
	        if (clusterName != null && bayName != null) {
	            String key = clusterName + "_" + bayName+"_"+String.format("%02d", Integer.parseInt(rowData.getPositionNo()));
	           // ApplicationLauncher.logger.debug("populateRs485AddressComboBox: key: " + key);
	            
	            if (BayUtils.getFilteredClusterBayPositionNoAddressListMap().get(rowData.getDeviceType()).containsKey(key)) {
	                comboBox.getItems().addAll(BayUtils.getFilteredClusterBayPositionNoAddressListMap().get(rowData.getDeviceType()).get(key));
	                //comboBox.getSelectionModel().select(0); // Default to the first item
	                //rowData.setPositionNo(comboBox.getSelectionModel().getSelectedItem());
	                /*if(BayUtils.getFilteredClusterBayNamePositionListMap().get(rowData.getDeviceType()).get(key).contains(rowData.getPositionNo())) {
	                	comboBox.getSelectionModel().select(rowData.getPositionNo()); 	
	                }else {*/
	                	comboBox.getSelectionModel().select(0); 
	                	rowData.setRs485Address(comboBox.getSelectionModel().getSelectedItem());
	               // 	rowData.setPositionNo(comboBox.getSelectionModel().getSelectedItem());
	               // }
	            } else {
	            	ApplicationLauncher.logger.debug("populateRs485AddressComboBox : No positions found for key: " + key);
	                rowData.setPositionNo(null);
	            }
	        } else {
	        	ApplicationLauncher.logger.debug("populateRs485AddressComboBox : Cluster or Bay Name is null");
	        }
    	}catch(Exception e) {
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("populateRs485AddressComboBox: Exception: getDeviceType: " + rowData.getDeviceType() + " : " + e.getMessage());
    	}
    }


}