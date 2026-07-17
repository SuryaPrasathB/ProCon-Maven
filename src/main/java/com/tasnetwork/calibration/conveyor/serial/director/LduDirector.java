package com.tasnetwork.calibration.conveyor.serial.director;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.serial.messenger.LduMessenger;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmLdu;
import com.tasnetwork.calibration.conveyor.util.GUIUtils;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;


public class LduDirector {
	
	private SpmLdu serialPortManager = new SpmLdu();
	public LduDirector(SpmLdu serialPortManagerQrScanner){
		this.serialPortManager = serialPortManagerQrScanner;
	}
	
	public LduDirector(){
		
	}


	
	

	

	  //===============================================================================================================	
  	public Map<String,Object> lduCheckCom(String slaveId) {
  		// TODO Auto-generated method stub
  		boolean status = false;
  		Map<String,Object>  responseMap = new HashMap<String,Object>();
  		//Sleep(30000);
  /*		if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
  			
  		
  		}else if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){*/
  			//BofaManager.setBofaPowerSourceOff();
  			//PowerSourceBofaMessenger pwrSrcBofaMessenger = new PowerSourceBofaMessenger();
  			//status = pwrSrcBofaMessenger.sendVoltageCurrentStopOutputCommand();
  			
  			if(ProconFeatureEnable.LSCS_LDU){
  				LduMessenger messenger = new LduMessenger(getSerialPortManager());
  				responseMap = messenger.lsLDU_SendCommandReadAccuracyData(slaveId);
  			}
  		/*}else {
  			
  		}*/
  		return responseMap;
  	}
  	
  	
  	


//=====================================================================================================================	
/*	public boolean setPowerSourceMctNctMode(String mctNctMode, boolean forceSet) {
		// TODO Auto-generated method stub
		boolean status = false;
		//Sleep(30000);
		if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			
		
		}else if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
			//BofaManager.setBofaPowerSourceOff();
			PowerSourceBofaMessenger pwrSrcBofaMessenger = new PowerSourceBofaMessenger();
			status = pwrSrcBofaMessenger.bofaSetPowerSourceMctNctMode(mctNctMode, forceSet);
		}else {
			
		}
		return status;
	}*/
	
	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("PowerSourceDirector: Sleep2 :InterruptedException:"+ e.getMessage());
		}

	}

	public SpmLdu getSerialPortManager() {
		return serialPortManager;
	}

	public void setSerialPortManager(SpmLdu spManager) {
		this.serialPortManager = spManager;
	}

	

}
