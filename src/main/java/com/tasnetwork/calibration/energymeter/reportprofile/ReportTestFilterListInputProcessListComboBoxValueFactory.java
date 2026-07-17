package com.tasnetwork.calibration.energymeter.reportprofile;

import java.util.ArrayList;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantReportV2;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.testprofiles.TestCaseData;
import com.tasnetwork.spring.orm.model.ReportProfileTestDataFilter;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class ReportTestFilterListInputProcessListComboBoxValueFactory implements Callback<TableColumn.CellDataFeatures<ReportProfileTestDataFilter, ComboBox<String>>, ObservableValue<ComboBox<String>>> {
	   
		@Override
	    public ObservableValue<ComboBox<String>> call(CellDataFeatures<ReportProfileTestDataFilter, ComboBox<String>> param) {
			ReportProfileTestDataFilter rowData = param.getValue();
			ComboBox<String> Combo_Box = new ComboBox<String>();
			ArrayList<String> inputProcessList = new ArrayList<String>();
			int columnWidth = 175;//200;//150;//250//50
			/*if(ProcalFeatureEnable.RUN_TYPE_TIME_BASED_ENABLED){
				testruntype.add(ConstantApp.TESTPOINT_RUNTYPE_TIMEBASED);
			}*/
/*			inputProcessList.add(ConstantApp.TESTPOINT_RUNTYPE_PULSEBASED);
			if(ProcalFeatureEnable.RUN_TYPE_TIME_BASED_ENABLED){
				inputProcessList.add(ConstantApp.TESTPOINT_RUNTYPE_TIMEBASED);
			}*/
			//if(ReportProfileConfigController.getPresentInputProcessList().size()>0){
			if(rowData.getOperationProcessInputKeyList().size()>0){	
				
				 //ArrayList<String> dataList = new ArrayList<String>(ReportProfileConfigController.getPresentInputProcessList());
				//inputProcessList.addAll( (ArrayList<String>) ReportProfileConfigController.getPresentInputProcessList().clone());
				ArrayList<String> dataList = new ArrayList<String>(rowData.getOperationProcessInputKeyList());
				inputProcessList.addAll(dataList);
			}else {
				inputProcessList.add(ConstantReportV2.NONE_DISPLAYED);
			}
			Combo_Box.getItems().clear();
			Combo_Box.getItems().addAll(inputProcessList);
			Combo_Box.getSelectionModel().select(0);
			//Combo_Box.setValue(rowData.getTestRunType());

			Combo_Box.setOnAction((e) -> {
	             ApplicationLauncher.logger.info("ReportTestFilterListInputProcessListComboBoxValueFactory: "+Combo_Box.getSelectionModel().getSelectedItem());
	             //rowData.setTestRunType(Combo_Box.getSelectionModel().getSelectedItem());
	        });
			
			Combo_Box.setMinWidth(columnWidth);
			Combo_Box.setPrefWidth(columnWidth);
			Combo_Box.setMaxWidth(columnWidth);
	        
	        return new SimpleObjectProperty<>(Combo_Box);
	    }


	}