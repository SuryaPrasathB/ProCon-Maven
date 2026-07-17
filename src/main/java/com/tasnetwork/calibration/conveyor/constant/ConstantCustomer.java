package com.tasnetwork.calibration.conveyor.constant;

import java.net.URL;
import java.util.ResourceBundle;

import com.tasnetwork.calibration.conveyor.customer.CustomerManager;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

import javafx.fxml.Initializable;

public class ConstantCustomer implements Initializable {
	
	public static CustomerManager currentCustomer = null;
	
	public static CustomerManager LAndT_Mysore = new CustomerManager("L & T Mysore","Procal",ConstantProTamp.DEVICE_NAME_LS_PROTAMP_3PHASE);

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		//LAndT_MysoreData();
		//setCurrentCustomer(LAndT_Mysore);
	}
	
	public static void init() {
		// TODO Auto-generated method stub
		
		LAndT_MysoreData();
		setCurrentCustomer(LAndT_Mysore);
		ApplicationLauncher.logger.info("ConstantCustomer: CurrentCustomer Name: "+ getCurrentCustomer().getCustomerName());
	}
	
	public static void setCurrentCustomer(CustomerManager value){
		currentCustomer = value;
	}
	
	public static CustomerManager getCurrentCustomer(){
		return currentCustomer;
	}
	
	public static void LAndT_MysoreData(){
		

		LAndT_MysoreInstalledVersion();
		//LAndT_MysoreUpDatedVersion(buildVersion,releasedDate ,dbSchemaVersion,configVersion,NoOfRacks,ImportMode,ExportMode)
		LAndT_MysoreUpDatedVersion  ("3.8.0"     ,"08-Oct-2018","1.2"          ,"1.0"        ,12       ,true      ,false     );
	}
	
	public static  void LAndT_MysoreInstalledVersion(){
		
		LAndT_Mysore.initialDelivery.setDeliveredDate("08-Oct-2018");
		LAndT_Mysore.initialDelivery.setBuildVersion("3.8.0");
		LAndT_Mysore.initialDelivery.setDbSchemaVersion("1.2");
		LAndT_Mysore.initialDelivery.setConfigFileVersion("1.0");
		
	}
	
	public static  void LAndT_MysoreUpDatedVersion(String buildVersion, String releasedDate, String dbSchemaVersion, 
			String configVersion, int NoOfRacks, Boolean ImportMode, Boolean ExportMode){
		
		LAndT_Mysore.latestDelivery.setDeliveredDate(releasedDate);
		LAndT_Mysore.latestDelivery.setBuildVersion(buildVersion);
		LAndT_Mysore.latestDelivery.setDbSchemaVersion(dbSchemaVersion);
		LAndT_Mysore.latestDelivery.setConfigFileVersion(configVersion);
		LAndT_Mysore.latestDelivery.setNoOfRacksOnPanel(NoOfRacks);
		LAndT_Mysore.latestDelivery.setImportModeEnabled(ImportMode);
		LAndT_Mysore.latestDelivery.setExportModeEnabled(ExportMode);

		
	}

}
