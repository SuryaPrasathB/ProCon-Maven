package com.tasnetwork.calibration.energymeter.reportprofile;

import java.util.ArrayList;
import java.util.Arrays;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantReportV2;
import com.tasnetwork.calibration.energymeter.testreport.ReportMeterMetaDataTypeSubModel;
import com.tasnetwork.spring.orm.model.OperationParam;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class MeterDataPopulateTypeListComboBoxValueFactory implements Callback<TableColumn.CellDataFeatures<ReportMeterMetaDataTypeSubModel, ComboBox<String>>, ObservableValue<ComboBox<String>>> {
	   
	
	ArrayList<String> headersOnlyList = new ArrayList<String> (Arrays.asList(ConstantReportV2.REPORT_META_DATATYPE_EXEC_TIME_STAMP,
				ConstantReportV2.REPORT_META_DATATYPE_EXEC_DATE,
				ConstantReportV2.REPORT_META_DATATYPE_EXEC_TIME,
				ConstantReportV2.REPORT_META_DATATYPE_REPORT_GEN_TIME_STAMP,
				ConstantReportV2.REPORT_META_DATATYPE_REPORT_GEN_DATE,
				ConstantReportV2.REPORT_META_DATATYPE_REPORT_GEN_TIME, 
				ConstantReportV2.REPORT_META_DATATYPE_APPROVED_TIME_STAMP,
				ConstantReportV2.REPORT_META_DATATYPE_APPROVED_DATE,
				ConstantReportV2.REPORT_META_DATATYPE_APPROVED_TIME,
				ConstantReportV2.REPORT_META_DATATYPE_TESTED_BY,
				ConstantReportV2.REPORT_META_DATATYPE_WITNESSED_BY,
				ConstantReportV2.REPORT_META_DATATYPE_APPROVED_BY,
				ConstantReportV2.REPORT_META_DATATYPE_PAGE_NO,
				ConstantReportV2.REPORT_META_DATATYPE_MAX_NO_OF_PAGES,
				ConstantReportV2.REPORT_META_DATATYPE_PAGE_NO_WITH_MAX_NO_OF_PAGES,
				ConstantReportV2.REPORT_META_DATATYPE_ENERGY_FLOW_MODE,
				ConstantReportV2.REPORT_META_DATATYPE_EXECUTION_CT_MODE,
				ConstantReportV2.REPORT_META_DATATYPE_ACTIVE_REACTIVE_ENERGY,
				ConstantReportV2.REPORT_META_DATATYPE_COMPLIES
			));
		@Override
	    public ObservableValue<ComboBox<String>> call(CellDataFeatures<ReportMeterMetaDataTypeSubModel, ComboBox<String>> param) {
			ReportMeterMetaDataTypeSubModel rowData = param.getValue();
			ComboBox<String> Combo_Box = new ComboBox<String>();
			ArrayList<String> cellPositionList = ConstantReportV2.POPULATE_DATA_TYPE_LIST;//new ArrayList<String>();
			int columnWidth = 167;//165 150;//100//75;//100;//50;//150;//250//50
			
/*			if(rowData.getReplicateResultCellPositionList().size()>0){	
				ArrayList<String> dataList = new ArrayList<String>(rowData.getReplicateResultCellPositionList());
				cellPositionList.addAll(dataList);
			}else {
				cellPositionList.add(ConstantReportV2.NONE_DISPLAYED);
			}*/
			Combo_Box.getItems().clear();
			
			
			if(rowData.getDataTypeKey().equals(ConstantReportV2.REPORT_META_DATATYPE_SERIAL_NO)){
				ArrayList<String> cellPositionList1 = new ArrayList<String> (cellPositionList);
				//ApplicationLauncher.logger.debug("MeterDataPopulateTypeListComboBoxValueFactory: getDataTypeKey1: " + rowData.getDataTypeKey());
				cellPositionList1.removeIf(e -> e.equals(ConstantReportV2.POPULATE_DATA_TYPE_ONLY_HEADERS));
				Combo_Box.getItems().addAll(cellPositionList1);
			}else if(rowData.getDataTypeKey().equals(ConstantReportV2.REPORT_META_DATATYPE_DUT_SERIAL_NO)){
				//ApplicationLauncher.logger.debug("MeterDataPopulateTypeListComboBoxValueFactory: getDataTypeKey2: " + rowData.getDataTypeKey());
				ArrayList<String> cellPositionList2 = new ArrayList<String> (cellPositionList);
				cellPositionList2.removeIf(e -> e.equals(ConstantReportV2.POPULATE_DATA_TYPE_ONLY_HEADERS));
				cellPositionList2.removeIf(e -> e.equals(ConstantReportV2.NONE_DISPLAYED));
				Combo_Box.getItems().addAll(cellPositionList2);
				rowData.setPopulateDataSelection(ConstantReportV2.POPULATE_DATA_TYPE_ALL_DUT);
			}else if(headersOnlyList.contains(rowData.getDataTypeKey())){
				ArrayList<String> cellPositionList3 = new ArrayList<String> (cellPositionList);
				cellPositionList3.removeIf(e -> e.equals(ConstantReportV2.POPULATE_DATA_TYPE_ALL_DUT));
				//cellPositionList3.removeIf(e -> e.equals(ConstantReportV2.NONE_DISPLAYED));
				Combo_Box.getItems().addAll(cellPositionList3);
				//rowData.setPopulateDataSelection(ConstantReportV2.POPULATE_DATA_TYPE_ALL_DUT);
			}else{
			
				Combo_Box.getItems().addAll(cellPositionList);
			}
			Combo_Box.getSelectionModel().select(0);
			//rowData.setPopulateType(Combo_Box.getSelectionModel().getSelectedItem());
			Combo_Box.setValue(rowData.getPopulateDataSelection());

			Combo_Box.setOnAction((e) -> {
	             ApplicationLauncher.logger.info(Combo_Box.getSelectionModel().getSelectedItem());
	             rowData.setPopulateDataSelection(Combo_Box.getSelectionModel().getSelectedItem());
	        });
/*			Combo_Box.setOnAction((e) -> {
	             ApplicationLauncher.logger.info("OperationParamPopulateTypeListComboBoxValueFactory: " + Combo_Box.getSelectionModel().getSelectedItem());
	             //rowData.setTestRunType(Combo_Box.getSelectionModel().getSelectedItem());
	        });*/
			
			Combo_Box.setMinWidth(columnWidth);
			Combo_Box.setPrefWidth(columnWidth);
			Combo_Box.setMaxWidth(columnWidth);
	        
	        return new SimpleObjectProperty<>(Combo_Box);
	    }


	}
