package com.tasnetwork.calibration.energymeter.device;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantDut;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceMte;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdRadiant;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.constant.Constant_ICT;
import com.tasnetwork.calibration.energymeter.deployment.DutResponse;
import com.tasnetwork.calibration.energymeter.deployment.LiveTableDataManager;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.calibration.energymeter.director.DutCmdDirectorV3;
import com.tasnetwork.calibration.energymeter.serial.portmanagerV2.DutCmdManager;
import com.tasnetwork.calibration.energymeter.serial.portmanagerV2.SerialPortManagerDutCmd_V3;
import com.tasnetwork.calibration.energymeter.setting.DevicePortSetupController;
import com.tasnetwork.calibration.energymeter.setting.DutPortSetupController;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

import gnu.io.SerialPort;
import javafx.scene.control.Alert.AlertType;

public class DutSerialDataManager {
	
	public DutSerialDataManager(){
		if(!bInitOccured){
			try{
				InitSerialCommPort();
			}catch (UnsatisfiedLinkError e){
	    		e.printStackTrace();
	    		ApplicationLauncher.logger.error("DutSerialDataManager: exception:" + e.getMessage());
	    		ApplicationLauncher.InformUser("Dut SerialPort search failed","Kindly ensure <rxtxSerial.dll> file is placed under path <C:\\Program Files\\Java\\jre1.8.0_161\\bin\\> folder. \n Additional Info:\n\n" + e.getMessage(),AlertType.ERROR);
	    	}
			bInitOccured= true;
		}

	}
	
	DeviceDataManagerController DisplayDataObj =  new DeviceDataManagerController();
	
	public static boolean bInitOccured = false;
	Timer dutCommandTimer;
	
	public static Communicator commDUT1 = null;
	public static Communicator commDUT2 = null;
	public static Communicator commDUT3 = null;
	public static Communicator commDUT4 = null;
	public static Communicator commDUT5 = null;
	public static Communicator commDUT6 = null;
	public static Communicator commDUT7 = null;
	public static Communicator commDUT8 = null;
	public static Communicator commDUT9 = null;
	public static Communicator commDUT10 = null;
	
	public static Communicator commDUT11 = null;
	public static Communicator commDUT12 = null;
	public static Communicator commDUT13 = null;
	public static Communicator commDUT14 = null;
	public static Communicator commDUT15 = null;
	public static Communicator commDUT16 = null;
	public static Communicator commDUT17 = null;
	public static Communicator commDUT18 = null;
	public static Communicator commDUT19 = null;
	public static Communicator commDUT20 = null;
	
	public static Communicator commDUT21 = null;
	public static Communicator commDUT22 = null;
	public static Communicator commDUT23 = null;
	public static Communicator commDUT24 = null;
	public static Communicator commDUT25 = null;
	public static Communicator commDUT26 = null;
	public static Communicator commDUT27 = null;
	public static Communicator commDUT28 = null;
	public static Communicator commDUT29 = null;
	public static Communicator commDUT30 = null;
	
	public static Communicator commDUT31 = null;
	public static Communicator commDUT32 = null;
	public static Communicator commDUT33 = null;
	public static Communicator commDUT34 = null;
	public static Communicator commDUT35 = null;
	public static Communicator commDUT36 = null;
	public static Communicator commDUT37 = null;
	public static Communicator commDUT38 = null;
	public static Communicator commDUT39 = null;
	public static Communicator commDUT40 = null;
	
	public static Communicator commDUT41 = null;
	public static Communicator commDUT42 = null;
	public static Communicator commDUT43 = null;
	public static Communicator commDUT44 = null;
	public static Communicator commDUT45 = null;
	public static Communicator commDUT46 = null;
	public static Communicator commDUT47 = null;
	public static Communicator commDUT48 = null;
	
	public static  ArrayList<Communicator> comDutList = new ArrayList<Communicator>(Arrays.asList(
			
			commDUT1,commDUT2,commDUT3,commDUT4,commDUT5,
			commDUT6,commDUT7,commDUT8,commDUT9,commDUT10,
			commDUT11,commDUT12,commDUT13,commDUT14,commDUT15,
			commDUT16,commDUT17,commDUT18,commDUT19,commDUT20,
			commDUT21,commDUT22,commDUT23,commDUT24,commDUT25,
			commDUT26,commDUT27,commDUT28,commDUT29,commDUT30,
			commDUT31,commDUT32,commDUT33,commDUT34,commDUT35,
			commDUT36,commDUT37,commDUT38,commDUT39,commDUT40,
			commDUT41,commDUT42,commDUT43,commDUT44,commDUT45,
			commDUT46,commDUT47,commDUT48
			
			));
	
	public static boolean dut1ComSerialStatusConnected = false;
	public static boolean dut2ComSerialStatusConnected = false;
	public static boolean dut3ComSerialStatusConnected = false;
	public static boolean dut4ComSerialStatusConnected = false;
	public static boolean dut5ComSerialStatusConnected = false;
	public static boolean dut6ComSerialStatusConnected = false;
	public static boolean dut7ComSerialStatusConnected = false;
	public static boolean dut8ComSerialStatusConnected = false;
	public static boolean dut9ComSerialStatusConnected = false;
	public static boolean dut10ComSerialStatusConnected = false;
	
	public static boolean dut11ComSerialStatusConnected = false;
	public static boolean dut12ComSerialStatusConnected = false;
	public static boolean dut13ComSerialStatusConnected = false;
	public static boolean dut14ComSerialStatusConnected = false;
	public static boolean dut15ComSerialStatusConnected = false;
	public static boolean dut16ComSerialStatusConnected = false;
	public static boolean dut17ComSerialStatusConnected = false;
	public static boolean dut18ComSerialStatusConnected = false;
	public static boolean dut19ComSerialStatusConnected = false;
	public static boolean dut20ComSerialStatusConnected = false;
	
	public static boolean dut21ComSerialStatusConnected = false;
	public static boolean dut22ComSerialStatusConnected = false;
	public static boolean dut23ComSerialStatusConnected = false;
	public static boolean dut24ComSerialStatusConnected = false;
	public static boolean dut25ComSerialStatusConnected = false;
	public static boolean dut26ComSerialStatusConnected = false;
	public static boolean dut27ComSerialStatusConnected = false;
	public static boolean dut28ComSerialStatusConnected = false;
	public static boolean dut29ComSerialStatusConnected = false;
	public static boolean dut30ComSerialStatusConnected = false;
	
	public static boolean dut31ComSerialStatusConnected = false;
	public static boolean dut32ComSerialStatusConnected = false;
	public static boolean dut33ComSerialStatusConnected = false;
	public static boolean dut34ComSerialStatusConnected = false;
	public static boolean dut35ComSerialStatusConnected = false;
	public static boolean dut36ComSerialStatusConnected = false;
	public static boolean dut37ComSerialStatusConnected = false;
	public static boolean dut38ComSerialStatusConnected = false;
	public static boolean dut39ComSerialStatusConnected = false;
	public static boolean dut40ComSerialStatusConnected = false;
	
	public static boolean dut41ComSerialStatusConnected = false;
	public static boolean dut42ComSerialStatusConnected = false;
	public static boolean dut43ComSerialStatusConnected = false;
	public static boolean dut44ComSerialStatusConnected = false;
	public static boolean dut45ComSerialStatusConnected = false;
	public static boolean dut46ComSerialStatusConnected = false;
	public static boolean dut47ComSerialStatusConnected = false;
	public static boolean dut48ComSerialStatusConnected = false;
	
	
	public static ArrayList<Boolean> comSerialStatusConnectedList = new ArrayList<Boolean>(Arrays.asList(
			
			dut1ComSerialStatusConnected,dut2ComSerialStatusConnected,dut3ComSerialStatusConnected,dut4ComSerialStatusConnected,dut5ComSerialStatusConnected,
			dut6ComSerialStatusConnected,dut7ComSerialStatusConnected,dut8ComSerialStatusConnected,dut9ComSerialStatusConnected,dut10ComSerialStatusConnected,
			dut11ComSerialStatusConnected,dut12ComSerialStatusConnected,dut13ComSerialStatusConnected,dut14ComSerialStatusConnected,dut15ComSerialStatusConnected,
			dut16ComSerialStatusConnected,dut17ComSerialStatusConnected,dut18ComSerialStatusConnected,dut19ComSerialStatusConnected,dut20ComSerialStatusConnected,
			dut21ComSerialStatusConnected,dut22ComSerialStatusConnected,dut23ComSerialStatusConnected,dut24ComSerialStatusConnected,dut25ComSerialStatusConnected,
			dut26ComSerialStatusConnected,dut27ComSerialStatusConnected,dut28ComSerialStatusConnected,dut29ComSerialStatusConnected,dut30ComSerialStatusConnected,
			dut31ComSerialStatusConnected,dut32ComSerialStatusConnected,dut33ComSerialStatusConnected,dut34ComSerialStatusConnected,dut35ComSerialStatusConnected,
			dut36ComSerialStatusConnected,dut37ComSerialStatusConnected,dut38ComSerialStatusConnected,dut39ComSerialStatusConnected,dut40ComSerialStatusConnected,
			dut41ComSerialStatusConnected,dut42ComSerialStatusConnected,dut43ComSerialStatusConnected,dut44ComSerialStatusConnected,dut45ComSerialStatusConnected,
			dut46ComSerialStatusConnected,dut47ComSerialStatusConnected,dut48ComSerialStatusConnected
			
			
			));
	

	
	public void InitSerialCommPort(){
		createObjects();
		commDUT1.searchForPorts();  

	}
	
	private void createObjects()
	{
		ApplicationLauncher.logger.debug("DutSerialDataManager: createObjects :Entry");

		commDUT1 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT2 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT3 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT4 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT5 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT6 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT7 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT8 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT9 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT10 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		
		commDUT11 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT12 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT13 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT14 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT15 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT16 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT17 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT18 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT19 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT20 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		
		commDUT21 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT22 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT23 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT24 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT25 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT26 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT27 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT28 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT29 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT30 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		
		commDUT31 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT32 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT33 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT34 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT35 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT36 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT37 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT38 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT39 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT40 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		
		commDUT41 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT42 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT43 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT44 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT45 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT46 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT47 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		commDUT48 = new Communicator(ConstantDut.SERIAL_PORT_DUT);
		
		comDutList.set(0, commDUT1);
		comDutList.set(1, commDUT2);
		comDutList.set(2, commDUT3);
		comDutList.set(3, commDUT4);
		comDutList.set(4, commDUT5);
		comDutList.set(5, commDUT6);
		comDutList.set(6, commDUT7);
		comDutList.set(7, commDUT8);
		comDutList.set(8, commDUT9);
		comDutList.set(9, commDUT10);
		comDutList.set(10, commDUT11);
		comDutList.set(11, commDUT12);
		comDutList.set(12, commDUT13);
		comDutList.set(13, commDUT14);
		comDutList.set(14, commDUT15);
		comDutList.set(15, commDUT16);
		comDutList.set(16, commDUT17);
		comDutList.set(17, commDUT18);
		comDutList.set(18, commDUT19);
		comDutList.set(19, commDUT20);
		comDutList.set(20, commDUT21);
		comDutList.set(21, commDUT22);
		comDutList.set(22, commDUT23);
		comDutList.set(23, commDUT24);
		comDutList.set(24, commDUT25);
		comDutList.set(25, commDUT26);
		comDutList.set(26, commDUT27);
		comDutList.set(27, commDUT28);
		comDutList.set(28, commDUT29);
		comDutList.set(29, commDUT30);
		comDutList.set(30, commDUT31);
		comDutList.set(31, commDUT32);
		comDutList.set(32, commDUT33);
		comDutList.set(33, commDUT34);
		comDutList.set(34, commDUT35);
		comDutList.set(35, commDUT36);
		comDutList.set(36, commDUT37);
		comDutList.set(37, commDUT38);
		comDutList.set(38, commDUT39);
		comDutList.set(39, commDUT40);
		comDutList.set(40, commDUT41);
		comDutList.set(41, commDUT42);
		comDutList.set(42, commDUT43);
		comDutList.set(43, commDUT44);
		comDutList.set(44, commDUT45);
		comDutList.set(45, commDUT46);
		comDutList.set(46, commDUT47);
		comDutList.set(47, commDUT48);

	}
	
	public DutResponse DutX_Init(String CommPort_ID, String BaudRate, int positionId) {
		ApplicationLauncher.logger.debug("DutX_Init :Entry");

		boolean status =false;
		//Map<String,Object> responseReturn = new HashMap<String,Object>();
		//responseReturn.put("status", false);	
		DutResponse dutResponse = new DutResponse();
		try {
			//boolean serialStatusConnected = DutX_CommInit(CommPort_ID, BaudRate,positionId);
			
			dutResponse = DutX_CommInit(CommPort_ID, BaudRate,positionId);
			boolean serialStatusConnected = dutResponse.getStatus();//(boolean)responseReturn.get("status");
			String responseData =  dutResponse.getResponseData();//(String)responseReturn.get("result");
			comSerialStatusConnectedList.set((positionId-1), serialStatusConnected);
			

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DutX_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DutX_Init : Exit");
		status=comSerialStatusConnectedList.get(positionId-1);

		return dutResponse;

	}
	
	public boolean DUT1_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT1_Init :Entry");

		boolean status =false;
		try {
			dut1ComSerialStatusConnected = DUT1_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT1_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT1_Init : Exit");
		status=dut1ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT2_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT2_Init :Entry");

		boolean status =false;
		try {
			dut2ComSerialStatusConnected = DUT2_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT2_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT2_Init : Exit");
		status=dut2ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT3_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT3_Init :Entry");

		boolean status =false;
		try {
			dut3ComSerialStatusConnected = DUT3_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT3_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT3_Init : Exit");
		status=dut3ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT4_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT4_Init :Entry");

		boolean status =false;
		try {
			dut4ComSerialStatusConnected = DUT4_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT4_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT4_Init : Exit");
		status=dut4ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT5_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT5_Init :Entry");

		boolean status =false;
		try {
			dut5ComSerialStatusConnected = DUT5_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT5_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT5_Init : Exit");
		status=dut5ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT6_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT6_Init :Entry");

		boolean status =false;
		try {
			dut6ComSerialStatusConnected = DUT6_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT6_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT6_Init : Exit");
		status=dut6ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT7_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT7_Init :Entry");

		boolean status =false;
		try {
			dut7ComSerialStatusConnected = DUT7_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT7_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT7_Init : Exit");
		status=dut7ComSerialStatusConnected;

		return  status;

	}
	
	
	public void disconnectDutX(int positionId){
		//ApplicationLauncher.logger.debug("disconnectDutX :Entry : " + positionId);
		//ApplicationLauncher.logger.debug("DisconnectDUT1 :dut1ComSerialStatusConnected: " + dut1ComSerialStatusConnected);
		if(comSerialStatusConnectedList.get(positionId-1)){
			DisconnectDutX_SerialComm(positionId);
			//dut1ComSerialStatusConnected=false;
			comSerialStatusConnectedList.set(positionId-1, false);
		}
		
		
	}

	public void DisconnectDUT1(){
		ApplicationLauncher.logger.debug("DisconnectDUT1 :Entry");
		ApplicationLauncher.logger.debug("DisconnectDUT1 :dut1ComSerialStatusConnected: " + dut1ComSerialStatusConnected);
		if(dut1ComSerialStatusConnected){
			DisconnectDUT1_SerialComm();
			dut1ComSerialStatusConnected=false;
		}
	}
	
	public void DisconnectDUT2(){
		ApplicationLauncher.logger.debug("DisconnectDUT2 :Entry");
		if(dut2ComSerialStatusConnected){
			DisconnectDUT2_SerialComm();
			dut2ComSerialStatusConnected=false;
		}
	}
	
	public void DisconnectDUT3(){
		ApplicationLauncher.logger.debug("DisconnectDUT3 :Entry");
		if(dut3ComSerialStatusConnected){
			DisconnectDUT3_SerialComm();
			dut3ComSerialStatusConnected=false;
		}
	}
	
	public void DisconnectDUT4(){
		ApplicationLauncher.logger.debug("DisconnectDUT4 :Entry");
		if(dut4ComSerialStatusConnected){
			DisconnectDUT4_SerialComm();
			dut4ComSerialStatusConnected=false;
		}
	}
	
	public void DisconnectDUT5(){
		ApplicationLauncher.logger.debug("DisconnectDUT5 :Entry");
		if(dut5ComSerialStatusConnected){
			DisconnectDUT5_SerialComm();
			dut5ComSerialStatusConnected=false;
		}
	}
	
	public void DisconnectDUT6(){
		ApplicationLauncher.logger.debug("DisconnectDUT6 :Entry");
		if(dut6ComSerialStatusConnected){
			DisconnectDUT6_SerialComm();
			dut6ComSerialStatusConnected=false;
		}
	}
	
	public void DisconnectDUT7(){
		ApplicationLauncher.logger.debug("DisconnectDUT7 :Entry");
		if(dut7ComSerialStatusConnected){
			DisconnectDUT7_SerialComm();
			dut7ComSerialStatusConnected=false;
		}
	}
	
	public void ClearSerialDataInAllPorts(){

		ApplicationLauncher.logger.debug("ClearSerialDataInAllPorts :Entry");
		//commPowerSrc.ClearSerialData();
		try {
			commDUT1.ClearSerialData();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception1: "+e1.getMessage() );
		}
		try {
			commDUT2.ClearSerialData();
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception2: "+e2.getMessage() );
		}
		try {
			commDUT3.ClearSerialData();
			
		} catch (Exception e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception3: "+e3.getMessage() );
		}
		try {
			commDUT4.ClearSerialData();
		} catch (Exception e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception4: "+e4.getMessage() );
		}
		try {
			commDUT5.ClearSerialData();
		} catch (Exception e5) {
			// TODO Auto-generated catch block
			e5.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception5: "+e5.getMessage() );
		}
		try {
			commDUT6.ClearSerialData();
		} catch (Exception e6) {
			// TODO Auto-generated catch block
			e6.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception6: "+e6.getMessage() );
		}
		try {
			commDUT7.ClearSerialData();
		} catch (Exception e7) {
			// TODO Auto-generated catch block
			e7.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception7: "+e7.getMessage() );
		}

		try {
			commDUT8.ClearSerialData();
		} catch (Exception e8) {
			// TODO Auto-generated catch block
			e8.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception8: "+e8.getMessage() );
		}



		/******9********/

		try {
			commDUT9.ClearSerialData();
		} catch (Exception e9) {
			// TODO Auto-generated catch block
			e9.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception9: "+e9.getMessage() );
		}



		/**************/

		try {
			commDUT10.ClearSerialData();
		} catch (Exception e10) {
			// TODO Auto-generated catch block
			e10.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception10: "+e10.getMessage() );
		}



		/******11********/

		try {
			commDUT11.ClearSerialData();
		} catch (Exception e11) {
			// TODO Auto-generated catch block
			e11.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception11: "+e11.getMessage() );
		}



		/******12********/

		try {
			commDUT12.ClearSerialData();
		} catch (Exception e12) {
			// TODO Auto-generated catch block
			e12.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception12: "+e12.getMessage() );
		}



		/******13********/

		try {
			commDUT13.ClearSerialData();
		} catch (Exception e13) {
			// TODO Auto-generated catch block
			e13.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception13: "+e13.getMessage() );
		}



		/******14********/

		try {
			commDUT14.ClearSerialData();
		} catch (Exception e14) {
			// TODO Auto-generated catch block
			e14.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception14: "+e14.getMessage() );
		}



		/******15********/

		try {
			commDUT15.ClearSerialData();
		} catch (Exception e15) {
			// TODO Auto-generated catch block
			e15.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception15: "+e15.getMessage() );
		}



		/******16********/

		try {
			commDUT16.ClearSerialData();
		} catch (Exception e16) {
			// TODO Auto-generated catch block
			e16.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception16: "+e16.getMessage() );
		}

		/******17********/

		/******17********/

		try {
			commDUT17.ClearSerialData();
		} catch (Exception e17) {
			// TODO Auto-generated catch block
			e17.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception17: "+e17.getMessage() );
		}

		/**************/

		try {
			commDUT18.ClearSerialData();
		} catch (Exception e18) {
			// TODO Auto-generated catch block
			e18.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception18: "+e18.getMessage() );
		}

		/******19********/

		try {
			commDUT19.ClearSerialData();
		} catch (Exception e19) {
			// TODO Auto-generated catch block
			e19.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception19: "+e19.getMessage() );
		}

		/******20********/

		try {
			commDUT20.ClearSerialData();
		} catch (Exception e20) {
			// TODO Auto-generated catch block
			e20.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception20: "+e20.getMessage() );
		}

		/******21********/

		try {
			commDUT21.ClearSerialData();
		} catch (Exception e21) {
			// TODO Auto-generated catch block
			e21.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception21: "+e21.getMessage() );
		}



		/******22********/

		try {
			commDUT22.ClearSerialData();
		} catch (Exception e22) {
			// TODO Auto-generated catch block
			e22.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception22: "+e22.getMessage() );
		}


		/******23********/

		try {
			commDUT23.ClearSerialData();
		} catch (Exception e23) {
			// TODO Auto-generated catch block
			e23.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception23: "+e23.getMessage() );
		}



		/******24********/

		try {
			commDUT24.ClearSerialData();
		} catch (Exception e24) {
			// TODO Auto-generated catch block
			e24.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception24: "+e24.getMessage() );
		}



		/******25********/

		/******25********/

		try {
			commDUT25.ClearSerialData();
		} catch (Exception e25) {
			// TODO Auto-generated catch block
			e25.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception25: "+e25.getMessage() );
		}



		/**************/

		try {
			commDUT26.ClearSerialData();
		} catch (Exception e26) {
			// TODO Auto-generated catch block
			e26.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception26: "+e26.getMessage() );
		}

		/******27********/

		try {
			commDUT27.ClearSerialData();
		} catch (Exception e27) {
			// TODO Auto-generated catch block
			e27.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception27: "+e27.getMessage() );
		}


		/******28********/

		try {
			commDUT28.ClearSerialData();
		} catch (Exception e28) {
			// TODO Auto-generated catch block
			e28.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception28: "+e28.getMessage() );
		}



		/******29********/

		try {
			commDUT29.ClearSerialData();
		} catch (Exception e29) {
			// TODO Auto-generated catch block
			e29.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception29: "+e29.getMessage() );
		}



		/******30********/

		try {
			commDUT30.ClearSerialData();
		} catch (Exception e30) {
			// TODO Auto-generated catch block
			e30.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception30: "+e30.getMessage() );
		}



		/******31********/

		try {
			commDUT31.ClearSerialData();
		} catch (Exception e31) {
			// TODO Auto-generated catch block
			e31.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception31: "+e31.getMessage() );
		}



		/******32********/

		try {
			commDUT32.ClearSerialData();
		} catch (Exception e32) {
			// TODO Auto-generated catch block
			e32.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception32: "+e32.getMessage() );
		}



		/******33********/

		/******33********/

		try {
			commDUT33.ClearSerialData();
		} catch (Exception e33) {
			// TODO Auto-generated catch block
			e33.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception33: "+e33.getMessage() );
		}



		/**************/

		try {
			commDUT34.ClearSerialData();
		} catch (Exception e34) {
			// TODO Auto-generated catch block
			e34.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception34: "+e34.getMessage() );
		}



		/******35********/

		try {
			commDUT35.ClearSerialData();
		} catch (Exception e35) {
			// TODO Auto-generated catch block
			e35.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception35: "+e35.getMessage() );
		}



		/******36********/

		try {
			commDUT36.ClearSerialData();
		} catch (Exception e36) {
			// TODO Auto-generated catch block
			e36.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception36: "+e36.getMessage() );
		}



		/******37********/

		try {
			commDUT37.ClearSerialData();
		} catch (Exception e37) {
			// TODO Auto-generated catch block
			e37.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception37: "+e37.getMessage() );
		}



		/******37********/

		try {
			commDUT38.ClearSerialData();
		} catch (Exception e38) {
			// TODO Auto-generated catch block
			e38.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception38: "+e38.getMessage() );
		}



		/******39********/

		try {
			commDUT39.ClearSerialData();
		} catch (Exception e39) {
			// TODO Auto-generated catch block
			e39.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception39: "+e39.getMessage() );
		}



		/******40********/

		try {
			commDUT40.ClearSerialData();
		} catch (Exception e40) {
			// TODO Auto-generated catch block
			e40.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception40: "+e40.getMessage() );
		}



		/******41********/

		/******41********/

		try {
			commDUT41.ClearSerialData();
		} catch (Exception e41) {
			// TODO Auto-generated catch block
			e41.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception41: "+e41.getMessage() );
		}



		/**************/

		try {
			commDUT42.ClearSerialData();
		} catch (Exception e42) {
			// TODO Auto-generated catch block
			e42.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception42: "+e42.getMessage() );
		}


		/******43********/

		try {
			commDUT43.ClearSerialData();
		} catch (Exception e43) {
			// TODO Auto-generated catch block
			e43.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception43: "+e43.getMessage() );
		}


		/******44********/

		try {
			commDUT44.ClearSerialData();
		} catch (Exception e44) {
			// TODO Auto-generated catch block
			e44.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception44: "+e44.getMessage() );
		}


		/******45********/

		try {
			commDUT45.ClearSerialData();
		} catch (Exception e45) {
			// TODO Auto-generated catch block
			e45.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception45: "+e45.getMessage() );
		}


		/******46********/

		try {
			commDUT46.ClearSerialData();
		} catch (Exception e46) {
			// TODO Auto-generated catch block
			e46.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception46: "+e46.getMessage() );
		}



		/******47********/

		try {
			commDUT47.ClearSerialData();
		} catch (Exception e47) {
			// TODO Auto-generated catch block
			e47.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception47: "+e47.getMessage() );
		}



		/******48********/

		try {
			commDUT48.ClearSerialData();
		} catch (Exception e48) {
			// TODO Auto-generated catch block
			e48.printStackTrace();
			ApplicationLauncher.logger.error("ClearSerialDataInAllPorts :Exception48: "+e48.getMessage() );
		}

		
		//for(int address=1; address <=ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK; address++){
			//SerialDataDUT.ClearDUT_ReadSerialData();
		//}
		//lsDUT_ClearSerialData();
	}
	
		public boolean lscsDUT1_CheckCom(){
		ApplicationLauncher.logger.debug("lscsDUT1_CheckCom :Entry");
		boolean status=false;
		//status = Check_And_SendDUTCommand();

		if (DutPortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT1_CheckCom: getPortValidationTurnedON");
			
			//SerialDataDUT.ClearDUT_ReadSerialData();
			calibDUT1_ClearSerialData();
/*			calibDUT2_ClearSerialData();
			calibDUT3_ClearSerialData();
			calibDUT4_ClearSerialData();
			calibDUT5_ClearSerialData();
			calibDUT6_ClearSerialData();
			calibDUT7_ClearSerialData();*/
			//calibDUT_ClearSerialData();
			DisplayDataObj.setDUT1_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT1_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT1_ADDRESS,ConstantDut.CMD_MTR_DUT1_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT1_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			//lsDUT_SendCommandReadAccuracyData(ConstantLscsDUT.CMD_DUT_POSITION_01_HDR);
			//SerialDataDUT dutData = DUT_ReadData(ConstantLscsDUT.CMD_DUT_ERROR_DATA_ER.length(),"");//ConstantLsDUT.CMD_DUT_ERROR_DATA_ER);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT1_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT1_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT1_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT1_ReadDataFlag( false);
		}
		return status;
	}
	
	public boolean lscsDUT2_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT2_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT2_CheckCom: getPortValidationTurnedON");

			calibDUT2_ClearSerialData();

			DisplayDataObj.setDUT2_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT2_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT2_ADDRESS,ConstantDut.CMD_MTR_DUT2_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT2_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT2_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT2_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT2_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT2_ReadDataFlag( false);
		}
		return status;

	}
	
	public boolean lscsDUT3_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT3_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT3_CheckCom: getPortValidationTurnedON");

			calibDUT3_ClearSerialData();

			DisplayDataObj.setDUT3_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT3_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT3_ADDRESS,ConstantDut.CMD_MTR_DUT3_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT3_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT3_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT3_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT3_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT3_ReadDataFlag( false);
		}
		return status;
	}
	
	public boolean lscsDUT4_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT4_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT4_CheckCom: getPortValidationTurnedON");

			calibDUT4_ClearSerialData();

			DisplayDataObj.setDUT4_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT4_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT4_ADDRESS,ConstantDut.CMD_MTR_DUT4_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT4_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT4_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT4_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT4_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT4_ReadDataFlag( false);
		}
		return status;
		
	}
	
	public boolean lscsDUT5_CheckCom(){
		
		ApplicationLauncher.logger.debug("lscsDUT5_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT5_CheckCom: getPortValidationTurnedON");

			calibDUT5_ClearSerialData();

			DisplayDataObj.setDUT5_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT5_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT5_ADDRESS,ConstantDut.CMD_MTR_DUT5_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT5_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT5_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT5_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT5_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT5_ReadDataFlag( false);
		}
		return status;
		
	}
	
	public boolean lscsDUT6_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT6_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT6_CheckCom: getPortValidationTurnedON");

			calibDUT6_ClearSerialData();

			DisplayDataObj.setDUT6_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT6_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT6_ADDRESS,ConstantDut.CMD_MTR_DUT6_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT6_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT6_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT6_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT6_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT6_ReadDataFlag( false);
		}
		return status;
	}
	
	public boolean lscsDUT7_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT7_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT7_CheckCom: getPortValidationTurnedON");

			calibDUT7_ClearSerialData();

			DisplayDataObj.setDUT7_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT7_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT7_ADDRESS,ConstantDut.CMD_MTR_DUT7_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT7_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT7_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT7_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT7_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT7_ReadDataFlag( false);
		}
		return status;
	}
	
	


	public void DisconnectDUT8(){
		ApplicationLauncher.logger.debug("DisconnectDUT8 :Entry");
		if(dut8ComSerialStatusConnected){
			DisconnectDUT8_SerialComm();
			dut8ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT8_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT8_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT8_CheckCom: getPortValidationTurnedON");

			calibDUT8_ClearSerialData();

			DisplayDataObj.setDUT8_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT8_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT8_ADDRESS,ConstantDut.CMD_MTR_DUT8_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT8_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT8_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT8_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT8_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT8_ReadDataFlag( false);
		}
		return status;
	}
	
	/************9********/
	
	
		public void DisconnectDUT9(){
		ApplicationLauncher.logger.debug("DisconnectDUT9 :Entry");
		if(dut9ComSerialStatusConnected){
			DisconnectDUT9_SerialComm();
			dut9ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT9_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT9_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT9_CheckCom: getPortValidationTurnedON");

			calibDUT9_ClearSerialData();

			DisplayDataObj.setDUT9_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT9_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT9_ADDRESS,ConstantDut.CMD_MTR_DUT9_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT9_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT9_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT9_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT9_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT9_ReadDataFlag( false);
		}
		return status;
	}
	
	/************10********/
	
		public void DisconnectDUT10(){
		ApplicationLauncher.logger.debug("DisconnectDUT10 :Entry");
		if(dut10ComSerialStatusConnected){
			DisconnectDUT10_SerialComm();
			dut10ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT10_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT10_CheckCom :Entry");
		boolean status=false;
		if (DutPortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT10_CheckCom: getPortValidationTurnedON");

			calibDUT10_ClearSerialData();

			DisplayDataObj.setDUT10_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT10_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT10_ADDRESS,ConstantDut.CMD_MTR_DUT10_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT10_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT10_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT10_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT10_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT10_ReadDataFlag( false);
		}
		return status;
	}
	
	/************11********/
	
		public void DisconnectDUT11(){
		ApplicationLauncher.logger.debug("DisconnectDUT11 :Entry");
		if(dut11ComSerialStatusConnected){
			DisconnectDUT11_SerialComm();
			dut11ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT11_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT11_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT11_CheckCom: getPortValidationTurnedON");

			calibDUT11_ClearSerialData();

			DisplayDataObj.setDUT11_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT11_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT11_ADDRESS,ConstantDut.CMD_MTR_DUT11_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT11_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT11_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT11_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT11_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT11_ReadDataFlag( false);
		}
		return status;
	}
	
	/************12********/
	
	
		public void DisconnectDUT12(){
		ApplicationLauncher.logger.debug("DisconnectDUT12 :Entry");
		if(dut12ComSerialStatusConnected){
			DisconnectDUT12_SerialComm();
			dut12ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT12_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT12_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT12_CheckCom: getPortValidationTurnedON");

			calibDUT12_ClearSerialData();

			DisplayDataObj.setDUT12_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT12_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT12_ADDRESS,ConstantDut.CMD_MTR_DUT12_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT12_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT12_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT12_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT12_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT12_ReadDataFlag( false);
		}
		return status;
	}
	
	/************13********/
	
		public void DisconnectDUT13(){
		ApplicationLauncher.logger.debug("DisconnectDUT13 :Entry");
		if(dut13ComSerialStatusConnected){
			DisconnectDUT13_SerialComm();
			dut13ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT13_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT13_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT13_CheckCom: getPortValidationTurnedON");

			calibDUT13_ClearSerialData();

			DisplayDataObj.setDUT13_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT13_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT13_ADDRESS,ConstantDut.CMD_MTR_DUT13_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT13_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT13_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT13_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT13_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT13_ReadDataFlag( false);
		}
		return status;
	}
	
	/************14********/
	
		public void DisconnectDUT14(){
		ApplicationLauncher.logger.debug("DisconnectDUT14 :Entry");
		if(dut14ComSerialStatusConnected){
			DisconnectDUT14_SerialComm();
			dut14ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT14_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT14_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT14_CheckCom: getPortValidationTurnedON");

			calibDUT14_ClearSerialData();

			DisplayDataObj.setDUT14_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT14_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT14_ADDRESS,ConstantDut.CMD_MTR_DUT14_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT14_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT14_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT14_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT14_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT14_ReadDataFlag( false);
		}
		return status;
	}
	
	/************15********/
	
	
		public void DisconnectDUT15(){
		ApplicationLauncher.logger.debug("DisconnectDUT15 :Entry");
		if(dut15ComSerialStatusConnected){
			DisconnectDUT15_SerialComm();
			dut15ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT15_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT15_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT15_CheckCom: getPortValidationTurnedON");

			calibDUT15_ClearSerialData();

			DisplayDataObj.setDUT15_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT15_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT15_ADDRESS,ConstantDut.CMD_MTR_DUT15_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT15_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT15_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT15_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT15_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT15_ReadDataFlag( false);
		}
		return status;
	}
	
	/************16********/
	
		public void DisconnectDUT16(){
		ApplicationLauncher.logger.debug("DisconnectDUT16 :Entry");
		if(dut16ComSerialStatusConnected){
			DisconnectDUT16_SerialComm();
			dut16ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT16_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT16_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT16_CheckCom: getPortValidationTurnedON");

			calibDUT16_ClearSerialData();

			DisplayDataObj.setDUT16_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT16_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT16_ADDRESS,ConstantDut.CMD_MTR_DUT16_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT16_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT16_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT16_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT16_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT16_ReadDataFlag( false);
		}
		return status;
	}
	
	/************17********/
	
		public void DisconnectDUT17(){
		ApplicationLauncher.logger.debug("DisconnectDUT17 :Entry");
		if(dut17ComSerialStatusConnected){
			DisconnectDUT17_SerialComm();
			dut17ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT17_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT17_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT17_CheckCom: getPortValidationTurnedON");

			calibDUT17_ClearSerialData();

			DisplayDataObj.setDUT17_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT17_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT17_ADDRESS,ConstantDut.CMD_MTR_DUT17_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT17_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT17_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT17_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT17_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT17_ReadDataFlag( false);
		}
		return status;
	}
	
	/************18********/
	
	
		public void DisconnectDUT18(){
		ApplicationLauncher.logger.debug("DisconnectDUT18 :Entry");
		if(dut18ComSerialStatusConnected){
			DisconnectDUT18_SerialComm();
			dut18ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT18_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT18_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT18_CheckCom: getPortValidationTurnedON");

			calibDUT18_ClearSerialData();

			DisplayDataObj.setDUT18_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT18_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT18_ADDRESS,ConstantDut.CMD_MTR_DUT18_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT18_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT18_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT18_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT18_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT18_ReadDataFlag( false);
		}
		return status;
	}
	
	/************19********/
	
		public void DisconnectDUT19(){
		ApplicationLauncher.logger.debug("DisconnectDUT19 :Entry");
		if(dut19ComSerialStatusConnected){
			DisconnectDUT19_SerialComm();
			dut19ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT19_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT19_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT19_CheckCom: getPortValidationTurnedON");

			calibDUT19_ClearSerialData();

			DisplayDataObj.setDUT19_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT19_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT19_ADDRESS,ConstantDut.CMD_MTR_DUT19_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT19_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT19_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT19_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT19_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT19_ReadDataFlag( false);
		}
		return status;
	}
	
	/************20********/
	
	
		public void DisconnectDUT20(){
		ApplicationLauncher.logger.debug("DisconnectDUT20 :Entry");
		if(dut20ComSerialStatusConnected){
			DisconnectDUT20_SerialComm();
			dut20ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT20_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT20_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT20_CheckCom: getPortValidationTurnedON");

			calibDUT20_ClearSerialData();

			DisplayDataObj.setDUT20_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT20_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT20_ADDRESS,ConstantDut.CMD_MTR_DUT20_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT20_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT20_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT20_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT20_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT20_ReadDataFlag( false);
		}
		return status;
	}
	
	/************21********/
	
	
		public void DisconnectDUT21(){
		ApplicationLauncher.logger.debug("DisconnectDUT21 :Entry");
		if(dut21ComSerialStatusConnected){
			DisconnectDUT21_SerialComm();
			dut21ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT21_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT21_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT21_CheckCom: getPortValidationTurnedON");

			calibDUT21_ClearSerialData();

			DisplayDataObj.setDUT21_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT21_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT21_ADDRESS,ConstantDut.CMD_MTR_DUT21_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT21_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT21_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT21_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT21_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT21_ReadDataFlag( false);
		}
		return status;
	}
	
	/************22********/
	
		public void DisconnectDUT22(){
		ApplicationLauncher.logger.debug("DisconnectDUT22 :Entry");
		if(dut22ComSerialStatusConnected){
			DisconnectDUT22_SerialComm();
			dut22ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT22_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT22_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT22_CheckCom: getPortValidationTurnedON");

			calibDUT22_ClearSerialData();

			DisplayDataObj.setDUT22_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT22_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT22_ADDRESS,ConstantDut.CMD_MTR_DUT22_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT22_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT22_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT22_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT22_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT22_ReadDataFlag( false);
		}
		return status;
	}
	
	/************23********/
	
		public void DisconnectDUT23(){
		ApplicationLauncher.logger.debug("DisconnectDUT23 :Entry");
		if(dut23ComSerialStatusConnected){
			DisconnectDUT23_SerialComm();
			dut23ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT23_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT23_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT23_CheckCom: getPortValidationTurnedON");

			calibDUT23_ClearSerialData();

			DisplayDataObj.setDUT23_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT23_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT23_ADDRESS,ConstantDut.CMD_MTR_DUT23_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT23_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT23_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT23_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT23_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT23_ReadDataFlag( false);
		}
		return status;
	}
	
	/************24********/
	
	
		public void DisconnectDUT24(){
		ApplicationLauncher.logger.debug("DisconnectDUT24 :Entry");
		if(dut24ComSerialStatusConnected){
			DisconnectDUT24_SerialComm();
			dut24ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT24_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT24_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT24_CheckCom: getPortValidationTurnedON");

			calibDUT24_ClearSerialData();

			DisplayDataObj.setDUT24_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT24_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT24_ADDRESS,ConstantDut.CMD_MTR_DUT24_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT24_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT24_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT24_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT24_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT24_ReadDataFlag( false);
		}
		return status;
	}
	
	/************25********/
	
		public void DisconnectDUT25(){
		ApplicationLauncher.logger.debug("DisconnectDUT25 :Entry");
		if(dut25ComSerialStatusConnected){
			DisconnectDUT25_SerialComm();
			dut25ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT25_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT25_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT25_CheckCom: getPortValidationTurnedON");

			calibDUT25_ClearSerialData();

			DisplayDataObj.setDUT25_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT25_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT25_ADDRESS,ConstantDut.CMD_MTR_DUT25_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT25_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT25_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT25_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT25_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT25_ReadDataFlag( false);
		}
		return status;
	}
	
	/************26********/
	
		public void DisconnectDUT26(){
		ApplicationLauncher.logger.debug("DisconnectDUT26 :Entry");
		if(dut26ComSerialStatusConnected){
			DisconnectDUT26_SerialComm();
			dut26ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT26_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT26_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT26_CheckCom: getPortValidationTurnedON");

			calibDUT26_ClearSerialData();

			DisplayDataObj.setDUT26_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT26_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT26_ADDRESS,ConstantDut.CMD_MTR_DUT26_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT26_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT26_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT26_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT26_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT26_ReadDataFlag( false);
		}
		return status;
	}
	
	/************27********/
	
	
		public void DisconnectDUT27(){
		ApplicationLauncher.logger.debug("DisconnectDUT27 :Entry");
		if(dut27ComSerialStatusConnected){
			DisconnectDUT27_SerialComm();
			dut27ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT27_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT27_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT27_CheckCom: getPortValidationTurnedON");

			calibDUT27_ClearSerialData();

			DisplayDataObj.setDUT27_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT27_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT27_ADDRESS,ConstantDut.CMD_MTR_DUT27_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT27_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT27_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT27_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT27_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT27_ReadDataFlag( false);
		}
		return status;
	}
	
	/************28********/
	
		public void DisconnectDUT28(){
		ApplicationLauncher.logger.debug("DisconnectDUT28 :Entry");
		if(dut28ComSerialStatusConnected){
			DisconnectDUT28_SerialComm();
			dut28ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT28_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT28_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT28_CheckCom: getPortValidationTurnedON");

			calibDUT28_ClearSerialData();

			DisplayDataObj.setDUT28_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT28_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT28_ADDRESS,ConstantDut.CMD_MTR_DUT28_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT28_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT28_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT28_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT28_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT28_ReadDataFlag( false);
		}
		return status;
	}
	
	/************29********/
	
		public void DisconnectDUT29(){
		ApplicationLauncher.logger.debug("DisconnectDUT29 :Entry");
		if(dut29ComSerialStatusConnected){
			DisconnectDUT29_SerialComm();
			dut29ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT29_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT29_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT29_CheckCom: getPortValidationTurnedON");

			calibDUT29_ClearSerialData();

			DisplayDataObj.setDUT29_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT29_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT29_ADDRESS,ConstantDut.CMD_MTR_DUT29_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT29_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT29_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT29_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT29_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT29_ReadDataFlag( false);
		}
		return status;
	}
	
	/************30********/
	
	
		public void DisconnectDUT30(){
		ApplicationLauncher.logger.debug("DisconnectDUT30 :Entry");
		if(dut30ComSerialStatusConnected){
			DisconnectDUT30_SerialComm();
			dut30ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT30_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT30_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT30_CheckCom: getPortValidationTurnedON");

			calibDUT30_ClearSerialData();

			DisplayDataObj.setDUT30_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT30_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT30_ADDRESS,ConstantDut.CMD_MTR_DUT30_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT30_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT30_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT30_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT30_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT30_ReadDataFlag( false);
		}
		return status;
	}
	
	/************31********/
	
		public void DisconnectDUT31(){
		ApplicationLauncher.logger.debug("DisconnectDUT31 :Entry");
		if(dut31ComSerialStatusConnected){
			DisconnectDUT31_SerialComm();
			dut31ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT31_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT31_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT31_CheckCom: getPortValidationTurnedON");

			calibDUT31_ClearSerialData();

			DisplayDataObj.setDUT31_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT31_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT31_ADDRESS,ConstantDut.CMD_MTR_DUT31_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT31_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT31_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT31_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT31_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT31_ReadDataFlag( false);
		}
		return status;
	}
	
	/************32********/
	
		public void DisconnectDUT32(){
		ApplicationLauncher.logger.debug("DisconnectDUT32 :Entry");
		if(dut32ComSerialStatusConnected){
			DisconnectDUT32_SerialComm();
			dut32ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT32_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT32_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT32_CheckCom: getPortValidationTurnedON");

			calibDUT32_ClearSerialData();

			DisplayDataObj.setDUT32_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT32_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT32_ADDRESS,ConstantDut.CMD_MTR_DUT32_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT32_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT32_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT32_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT32_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT32_ReadDataFlag( false);
		}
		return status;
	}
	
	/************33********/
	
	
		public void DisconnectDUT33(){
		ApplicationLauncher.logger.debug("DisconnectDUT33 :Entry");
		if(dut33ComSerialStatusConnected){
			DisconnectDUT33_SerialComm();
			dut33ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT33_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT33_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT33_CheckCom: getPortValidationTurnedON");

			calibDUT33_ClearSerialData();

			DisplayDataObj.setDUT33_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT33_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT33_ADDRESS,ConstantDut.CMD_MTR_DUT33_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT33_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT33_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT33_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT33_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT33_ReadDataFlag( false);
		}
		return status;
	}
	
	/************34********/
	
		public void DisconnectDUT34(){
		ApplicationLauncher.logger.debug("DisconnectDUT34 :Entry");
		if(dut34ComSerialStatusConnected){
			DisconnectDUT34_SerialComm();
			dut34ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT34_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT34_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT34_CheckCom: getPortValidationTurnedON");

			calibDUT34_ClearSerialData();

			DisplayDataObj.setDUT34_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT34_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT34_ADDRESS,ConstantDut.CMD_MTR_DUT34_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT34_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT34_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT34_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT34_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT34_ReadDataFlag( false);
		}
		return status;
	}
	
	/************34********/
	
		public void DisconnectDUT35(){
		ApplicationLauncher.logger.debug("DisconnectDUT35 :Entry");
		if(dut35ComSerialStatusConnected){
			DisconnectDUT35_SerialComm();
			dut35ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT35_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT35_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT35_CheckCom: getPortValidationTurnedON");

			calibDUT35_ClearSerialData();

			DisplayDataObj.setDUT35_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT35_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT35_ADDRESS,ConstantDut.CMD_MTR_DUT35_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT35_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT35_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT35_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT35_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT35_ReadDataFlag( false);
		}
		return status;
	}
	
	/************36********/
	
	
		public void DisconnectDUT36(){
		ApplicationLauncher.logger.debug("DisconnectDUT36 :Entry");
		if(dut36ComSerialStatusConnected){
			DisconnectDUT36_SerialComm();
			dut36ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT36_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT36_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT36_CheckCom: getPortValidationTurnedON");

			calibDUT36_ClearSerialData();

			DisplayDataObj.setDUT36_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT36_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT36_ADDRESS,ConstantDut.CMD_MTR_DUT36_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT36_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT36_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT36_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT36_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT36_ReadDataFlag( false);
		}
		return status;
	}
	
	/************37********/
	
		public void DisconnectDUT37(){
		ApplicationLauncher.logger.debug("DisconnectDUT37 :Entry");
		if(dut37ComSerialStatusConnected){
			DisconnectDUT37_SerialComm();
			dut37ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT37_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT37_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT37_CheckCom: getPortValidationTurnedON");

			calibDUT37_ClearSerialData();

			DisplayDataObj.setDUT37_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT37_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT37_ADDRESS,ConstantDut.CMD_MTR_DUT37_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT37_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT37_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT37_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT37_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT37_ReadDataFlag( false);
		}
		return status;
	}
	
	/************38********/
	
		public void DisconnectDUT38(){
		ApplicationLauncher.logger.debug("DisconnectDUT38 :Entry");
		if(dut38ComSerialStatusConnected){
			DisconnectDUT38_SerialComm();
			dut38ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT38_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT38_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT38_CheckCom: getPortValidationTurnedON");

			calibDUT38_ClearSerialData();

			DisplayDataObj.setDUT38_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT38_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT38_ADDRESS,ConstantDut.CMD_MTR_DUT38_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT38_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT38_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT38_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT38_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT38_ReadDataFlag( false);
		}
		return status;
	}
	
	/************39********/
	
	
		public void DisconnectDUT39(){
		ApplicationLauncher.logger.debug("DisconnectDUT39 :Entry");
		if(dut39ComSerialStatusConnected){
			DisconnectDUT39_SerialComm();
			dut39ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT39_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT39_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT39_CheckCom: getPortValidationTurnedON");

			calibDUT39_ClearSerialData();

			DisplayDataObj.setDUT39_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT39_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT39_ADDRESS,ConstantDut.CMD_MTR_DUT39_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT39_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT39_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT39_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT39_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT39_ReadDataFlag( false);
		}
		return status;
	}
	
	/************40********/
	
		public void DisconnectDUT40(){
		ApplicationLauncher.logger.debug("DisconnectDUT40 :Entry");
		if(dut40ComSerialStatusConnected){
			DisconnectDUT40_SerialComm();
			dut40ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT40_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT40_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT40_CheckCom: getPortValidationTurnedON");

			calibDUT40_ClearSerialData();

			DisplayDataObj.setDUT40_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT40_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT40_ADDRESS,ConstantDut.CMD_MTR_DUT40_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT40_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT40_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT40_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT40_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT40_ReadDataFlag( false);
		}
		return status;
	}
	
	/************40********/
	
		public void DisconnectDUT41(){
		ApplicationLauncher.logger.debug("DisconnectDUT41 :Entry");
		if(dut41ComSerialStatusConnected){
			DisconnectDUT41_SerialComm();
			dut41ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT41_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT41_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT41_CheckCom: getPortValidationTurnedON");

			calibDUT41_ClearSerialData();

			DisplayDataObj.setDUT41_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT41_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT41_ADDRESS,ConstantDut.CMD_MTR_DUT41_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT41_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT41_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT41_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT41_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT41_ReadDataFlag( false);
		}
		return status;
	}
	
	/************42********/
	
	
		public void DisconnectDUT42(){
		ApplicationLauncher.logger.debug("DisconnectDUT42 :Entry");
		if(dut42ComSerialStatusConnected){
			DisconnectDUT42_SerialComm();
			dut42ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT42_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT42_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT42_CheckCom: getPortValidationTurnedON");

			calibDUT42_ClearSerialData();

			DisplayDataObj.setDUT42_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT42_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT42_ADDRESS,ConstantDut.CMD_MTR_DUT42_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT42_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT42_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT42_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT42_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT42_ReadDataFlag( false);
		}
		return status;
	}
	
	/************43********/
	
		public void DisconnectDUT43(){
		ApplicationLauncher.logger.debug("DisconnectDUT43 :Entry");
		if(dut43ComSerialStatusConnected){
			DisconnectDUT43_SerialComm();
			dut43ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT43_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT43_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT43_CheckCom: getPortValidationTurnedON");

			calibDUT43_ClearSerialData();

			DisplayDataObj.setDUT43_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT43_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT43_ADDRESS,ConstantDut.CMD_MTR_DUT43_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT43_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT43_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT43_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT43_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT43_ReadDataFlag( false);
		}
		return status;
	}
	
	/************7********/
	
	public void DisconnectDUT44(){
		ApplicationLauncher.logger.debug("DisconnectDUT44 :Entry");
		if(dut44ComSerialStatusConnected){
			DisconnectDUT44_SerialComm();
			dut44ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT44_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT44_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT44_CheckCom: getPortValidationTurnedON");

			calibDUT44_ClearSerialData();

			DisplayDataObj.setDUT44_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT44_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT44_ADDRESS,ConstantDut.CMD_MTR_DUT44_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT44_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT44_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT44_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT44_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT44_ReadDataFlag( false);
		}
		return status;
	}
	
	/************7********/
	
	public void DisconnectDUT45(){
		ApplicationLauncher.logger.debug("DisconnectDUT45 :Entry");
		if(dut45ComSerialStatusConnected){
			DisconnectDUT45_SerialComm();
			dut45ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT45_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT45_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT45_CheckCom: getPortValidationTurnedON");

			calibDUT45_ClearSerialData();

			DisplayDataObj.setDUT45_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT45_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT45_ADDRESS,ConstantDut.CMD_MTR_DUT45_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT45_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT45_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT45_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT45_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT45_ReadDataFlag( false);
		}
		return status;
	}
	
	/************7********/
	
	
	public void DisconnectDUT46(){
		ApplicationLauncher.logger.debug("DisconnectDUT46 :Entry");
		if(dut46ComSerialStatusConnected){
			DisconnectDUT46_SerialComm();
			dut46ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT46_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT46_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT46_CheckCom: getPortValidationTurnedON");

			calibDUT46_ClearSerialData();

			DisplayDataObj.setDUT46_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT46_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT46_ADDRESS,ConstantDut.CMD_MTR_DUT46_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT46_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT46_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT46_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT46_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT46_ReadDataFlag( false);
		}
		return status;
	}
	
	/************7********/
	
	
	public void DisconnectDUT47(){
		ApplicationLauncher.logger.debug("DisconnectDUT47 :Entry");
		if(dut47ComSerialStatusConnected){
			DisconnectDUT47_SerialComm();
			dut47ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT47_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT47_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT47_CheckCom: getPortValidationTurnedON");

			calibDUT47_ClearSerialData();

			DisplayDataObj.setDUT47_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT47_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT47_ADDRESS,ConstantDut.CMD_MTR_DUT47_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT47_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT47_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT47_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT47_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT47_ReadDataFlag( false);
		}
		return status;
	}
	
	/************7********/
	
	public void DisconnectDUT48(){
		ApplicationLauncher.logger.debug("DisconnectDUT48 :Entry");
		if(dut48ComSerialStatusConnected){
			DisconnectDUT48_SerialComm();
			dut48ComSerialStatusConnected=false;
		}
	}
	
	public boolean lscsDUT48_CheckCom(){

		ApplicationLauncher.logger.debug("lscsDUT48_CheckCom :Entry");
		boolean status=false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsDUT48_CheckCom: getPortValidationTurnedON");

			calibDUT48_ClearSerialData();

			DisplayDataObj.setDUT48_ReadDataFlag( true);
			
			int numberOfReadings = 1;
			String expectedResponse = ConstantDut.ER_MTR_DUT48_COM_CHECK;	
			calibrationSendCommand(ConstantDut.DUT48_ADDRESS,ConstantDut.CMD_MTR_DUT48_COM_CHECK);
			
			
			SerialDataDUT dutData = dutReadData(ConstantDut.DUT48_ADDRESS, expectedResponse.length(),expectedResponse,numberOfReadings);
			if(dutData.IsExpectedResponseReceived()){

				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT48_CheckCom: DUT Received DataA:"+CurrentDUT_Data);
				StripDUT_SerialData(ConstantDut.DUT48_ADDRESS,dutData.getReceivedLength());
				status=true;
			}else{
				String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
				ApplicationLauncher.logger.info("lscsDUT48_CheckCom: CurrentDUT_Data2: "+CurrentDUT_Data);
			}
			DisplayDataObj.setDUT48_ReadDataFlag( false);
		}
		return status;
	}

	
	public void WriteToSerialCommDUT1(String Data){
		//ApplicationLauncher.logger.debug("WriteToSerialCommDUT :DataHex:"+ConstantCcubeDUT.StringToHex(Data).toUpperCase());
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT1 :Data:"+Data);
		try {
			commDUT1.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT1 :Exception :" + e.getMessage());
		}

	}
	
	public void WriteToSerialCommDUT2(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT2 :Data:"+Data);
		try {
			commDUT2.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT2 :Exception :" + e.getMessage());
		}
	}
	
	public void WriteToSerialCommDUT3(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT3 :Data:"+Data);
		try {
			commDUT3.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommLD3 :Exception :" + e.getMessage());
		}
	}
	
	public void WriteToSerialCommDUT4(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT4 :Data:"+Data);
		try {
			commDUT4.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT4 :Exception :" + e.getMessage());
		}
	}
	
	public void WriteToSerialCommDUT5(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT5 :Data:"+Data);
		try {
			commDUT5.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT5 :Exception :" + e.getMessage());
		}
	}
	
	public void WriteToSerialCommDUT6(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT6 :Data:"+Data);
		try {
			commDUT6.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT6 :Exception :" + e.getMessage());
		}
	}
	
	public void WriteToSerialCommDUT7(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT7 :Data:"+Data);
		try {
			commDUT7.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT7 :Exception :" + e.getMessage());
		}
	}
	
	
	public boolean DUT8_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT8_Init :Entry");

		boolean status =false;
		try {
			dut8ComSerialStatusConnected = DUT8_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT8_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT8_Init : Exit");
		status=dut8ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT8_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT8_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT8,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT8.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT8_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	
	
	public void calibDUT8_ClearSerialData(){
		commDUT8.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT8(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT8 :Data:"+Data);
		try {
			commDUT8.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT8 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT9_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT9_Init :Entry");

		boolean status =false;
		try {
			dut9ComSerialStatusConnected = DUT9_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT9_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT9_Init : Exit");
		status=dut9ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT9_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT9_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT9,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT9.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT9_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	

	public void calibDUT9_ClearSerialData(){
		commDUT9.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT9(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT9 :Data:"+Data);
		try {
			commDUT9.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT9 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT10_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT10_Init :Entry");

		boolean status =false;
		try {
			dut10ComSerialStatusConnected = DUT10_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT10_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT10_Init : Exit");
		status=dut10ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT10_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT10_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT10,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT10.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT10_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 

	
	public void calibDUT10_ClearSerialData(){
		commDUT10.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT10(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT10 :Data:"+Data);
		try {
			commDUT10.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT10 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT11_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT11_Init :Entry");

		boolean status =false;
		try {
			dut11ComSerialStatusConnected = DUT11_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT11_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT11_Init : Exit");
		status=dut11ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT11_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT11_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT11,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT11.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT11_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 

	
	public void calibDUT11_ClearSerialData(){
		commDUT11.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT11(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT11 :Data:"+Data);
		try {
			commDUT11.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT11 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT12_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT12_Init :Entry");

		boolean status =false;
		try {
			dut12ComSerialStatusConnected = DUT12_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT12_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT12_Init : Exit");
		status=dut12ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT12_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT12_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT12,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT12.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT12_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	
	
	public void calibDUT12_ClearSerialData(){
		commDUT12.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT12(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT12 :Data:"+Data);
		try {
			commDUT12.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT12 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT13_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT13_Init :Entry");

		boolean status =false;
		try {
			dut13ComSerialStatusConnected = DUT13_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT13_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT13_Init : Exit");
		status=dut13ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT13_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT13_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT13,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT13.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT13_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	
	
	public void calibDUT13_ClearSerialData(){
		commDUT13.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT13(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT13 :Data:"+Data);
		try {
			commDUT13.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT13 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT14_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT14_Init :Entry");

		boolean status =false;
		try {
			dut14ComSerialStatusConnected = DUT14_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT14_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT14_Init : Exit");
		status=dut14ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT14_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT14_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT14,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT14.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT14_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	
	
	public void calibDUT14_ClearSerialData(){
		commDUT14.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT14(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT14 :Data:"+Data);
		try {
			commDUT14.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT14 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT15_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT15_Init :Entry");

		boolean status =false;
		try {
			dut15ComSerialStatusConnected = DUT15_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT15_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT15_Init : Exit");
		status=dut15ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT15_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT15_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT15,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT15.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT15_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	
	
	public String get_port_name(String src_type){
		String port_name = "";
		try {
			port_name = DevicePortSetupController.get_device_settings(src_type).getString("port_name");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("get_port_name :JSONException:"+ e.getMessage());
		}
		return port_name;
	}

	public String get_baud_rate(String src_type){
		String baud_rate ="";
		try {
			baud_rate = DevicePortSetupController.get_device_settings(src_type).getString("baud_rate");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("get_baud_rate :JSONException:"+ e.getMessage());
		}
		return baud_rate;
	}
	
	
	public DutResponse serialConnectDutX(int positionId){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	DutResponse dutResponse = new DutResponse();
    	
    	String srcType = ConstantDut.COM_SRC_TYPE_LIST.get(positionId-1);//"DUT1";
    	
    	
    	boolean status = false;
    	//txtValidateDUT_CmdStatus1.clear();
    	//Map<String,Object> responseReturn = new HashMap<String,Object>();
    	//responseReturn.put("status", false);	
    	try{
    		
    		HashMap portMap = new HashMap();
    		portMap = commDUT1.searchForPorts(); 
	    	//comDutList.get(positionId-1).searchForPorts(); ;
	    	DUT_CommPortID = get_port_name(srcType);//getCurrentDUT_ComPortID1();
	    	ApplicationLauncher.logger.debug("serialConnectDutX : portMap : " + portMap);
	    	if(portMap.containsKey(DUT_CommPortID)) {
	    		DUTCommBaudRate = get_baud_rate(srcType);//getCurrentDUT_ComBaudRate1();
		    	ApplicationLauncher.logger.debug("serialConnectDutX : serial port DUTCommBaudRate : " + DUTCommBaudRate);//
		    	
		    	dutResponse = DutX_Init(DUT_CommPortID,DUTCommBaudRate,positionId);
		    	status = dutResponse.getStatus();//(boolean)responseReturn.get("status");
				String responseData = dutResponse.getResponseData();//(String)responseReturn.get("result");
		    	if (!status){
		    		ApplicationLauncher.logger.debug("serialConnectDutX : serial port access failed : positionId: " + positionId + " -> " + DUT_CommPortID) ;
		    		//txtValidateDUT_CmdStatus1.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
		    		//txtAreaMeterSerialLog.appendText("Dut1: " +ConstantApp.SERIAL_PORT_ACCESS_FAILED + "\n");
		    	} else {
		    		ApplicationLauncher.logger.debug("serialConnectDutX : serial port connected : positionId: " + positionId + " -> " + DUT_CommPortID) ;
		    		
		    		//setPortValidationTurnedON(true);
		    		//status = dutSerialDM_Obj.lscsDUT1_UnlockCmd();
		    		
		    		//if (!status){
		    			//txtValidateDUT_CmdStatus1.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
		    		//	txtAreaMeterSerialLog.appendText("Dut1 Connected"+ "\n");
		    		//}else{
		    			//txtValidateDUT_CmdStatus1.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
		    		//	txtAreaMeterSerialLog.appendText(ConstantApp.SERIAL_PORT_COMMAND_Success + "\n");
		    		//}
		    		//setPortValidationTurnedON(false);
		    		//DisplayDataObj.setDUT1_ReadDataFlag(false);
		    	}
	    	}else {
	    		ApplicationLauncher.logger.debug("serialConnectDutX : serial port not found : positionId: " + positionId + " -> " + DUT_CommPortID) ;
	    		String responseData = DUT_CommPortID + " not found";
	    		dutResponse.setResponseData(responseData);
	    		//responseReturn.put("result", responseData);
	    	}
	    	
	    	//dutSerialDM_Obj.DisconnectDUT1();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("serialConnectDutX: Exception : positionId : " + positionId + " : "+e.getMessage());
    	}
    	
    	return dutResponse;
    }
	
	
	
	/*public  Map<String, Object> dutSendCmd(int positionId){
		ApplicationLauncher.logger.debug("dutSendCmd :Entry : positionId : " + positionId);
		boolean status=false;
		 Map<String, Object> responseReturn = new HashMap<>();
	        responseReturn.put("status", false);
	        responseReturn.put("result", "");
		//status = Check_And_SendDUTCommand();

		//if (DutPortSetupController.getPortValidationTurnedON()){
			//ApplicationLauncher.logger.info("lscsDUT1_RelayConnectCmd: getPortValidationTurnedON");
			
			
			calibDUT1_ClearSerialData();

			//DisplayDataObj.setDUT1_ReadDataFlag( true);
			DisplayDataObj.bDutReadDataList.set((positionId-1),  true);
			
			int numberOfReadings = 1;
			String expectedResponse = DisplayDataObj.getDutResponseExpectedData();//ConstantDut.ER_MTR_RTC_READ;
			String expectedResponseInHex ="";
			String targetCommand = DisplayDataObj.getDutTargetCommand();
			String targetCommandInAscii = "";
			
			//String timeOutStr  = DisplayDataObj.getDutResponseTimeOutInSec();
			String haltTimeInSecStr  = DisplayDataObj.getDutHaltTimeInSec();
			boolean commandInHexMode = DisplayDataObj.isDutCommandInHexMode();
			boolean dutResponseMandatory = DisplayDataObj.isDutResponseMandatory();
			String serialNoSourceType = DisplayDataObj.getDutSerialNoSourceType();
			
			boolean dutWriteSerialNoToDutEnabled = DisplayDataObj.isDutWriteSerialNoToDut();
			boolean dutReadSerialNoFromDutEnabled = DisplayDataObj.isDutReadSerialNoFromDut();
			if(commandInHexMode) {
				targetCommandInAscii = GuiUtils.hexToAsciiV2(targetCommand);
				expectedResponseInHex = expectedResponse;
				//expectedResponse = GuiUtils.asciiToHex(expectedResponse);
			}else {
				targetCommandInAscii = targetCommand;
				expectedResponseInHex = GuiUtils.asciiToHex(expectedResponse);
			}
			
			
			String commandTerminator = DisplayDataObj.getDutTargetCommandTerminator();
			String commandTerminatorInAscii = "";
			
			boolean commandTerminatorInHexMode = DisplayDataObj.isDutCommandTerminatorInHexMode();
			
			if(commandTerminatorInHexMode) {
				commandTerminatorInAscii = GuiUtils.hexToAsciiV2(commandTerminator);
			}else {
				commandTerminatorInAscii = commandTerminator;
			}
			String dutSerialNumberInAscii = "";
			if(dutWriteSerialNoToDutEnabled) {
				dutSerialNumberInAscii = DisplayDataObj.getDutSerialNumberMap(positionId);
				ApplicationLauncher.logger.debug("dutSendCmd : dutSerialNumberInAscii : <" + dutSerialNumberInAscii + "> : position id : " + positionId);
			}
			
			String absoluteCommandInAscii = targetCommandInAscii + dutSerialNumberInAscii + commandTerminatorInAscii;
			ApplicationLauncher.logger.debug("dutSendCmd : absoluteCommandInAscii : " + absoluteCommandInAscii + " : position id : " + positionId);
			ApplicationLauncher.logger.debug("dutSendCmd : expectedResponseInHex length: " + expectedResponseInHex.length()+ " : position id : " + positionId);
			calibrationSendCommandV2(positionId,absoluteCommandInAscii);
			
			if(dutResponseMandatory) {
				
				String expectedResponseTerminatorInHex = "";
				if(DisplayDataObj.isDutResponseTerminatorInHexMode()) {
					expectedResponseTerminatorInHex = DisplayDataObj.getDutResponseTerminator();
				}else {
					expectedResponseTerminatorInHex = GuiUtils.asciiToHex(DisplayDataObj.getDutResponseTerminator());
				}
					
				SerialDataDUT dutData = dutReadDataV2(positionId, (expectedResponseInHex.length()+14),expectedResponseInHex,numberOfReadings,expectedResponseTerminatorInHex);
				//lsDUT_SendCommandReadAccuracyData(ConstantLscsDUT.CMD_DUT_POSITION_01_HDR);
				//SerialDataDUT dutData = DUT_ReadData(ConstantLscsDUT.CMD_DUT_ERROR_DATA_ER.length(),"");//ConstantLsDUT.CMD_DUT_ERROR_DATA_ER);
				if(dutData.IsExpectedResponseReceived()){
	
					String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
					ApplicationLauncher.logger.info("dutSendCmd: Expected recieved:"+CurrentDUT_Data+ " : position id : " + positionId);
					StripDUT_SerialData(positionId,dutData.getReceivedLength());
					if(dutReadSerialNoFromDutEnabled) {
						String readSerialNoInHex = new String (CurrentDUT_Data);
						readSerialNoInHex = readSerialNoInHex.replaceFirst(expectedResponseInHex, "");// replace starting header
						readSerialNoInHex = readSerialNoInHex.replaceAll(expectedResponseTerminatorInHex+"$","");///replace last terminator
						String readSerialNoInAscii = GuiUtils.hexToAsciiV2(readSerialNoInHex);
						ApplicationLauncher.logger.info("dutSendCmd: Expected recieved: readSerialNoInAscii:<"+readSerialNoInAscii+ "> : position id : " + positionId);
						responseReturn.put("result", readSerialNoInAscii);
					}
					status=true;
				}else{
					String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
					ApplicationLauncher.logger.info("dutSendCmd: Failed: Unexpected Data: CurrentDUT_Data2: "+CurrentDUT_Data+ " : position id : " + positionId);
				}
			}else {
				
				status=true;
			}
			
			if(!haltTimeInSecStr.equals("")) {
				int timeOut = Integer.parseInt(haltTimeInSecStr);
				ApplicationLauncher.logger.info("dutSendCmd: timeOut Entry: "+ " : position id : " + positionId);		
				while((timeOut>0) && (!ProjectExecutionController.getUserAbortedFlag())) {
					ApplicationLauncher.logger.info("dutSendCmd: timeOut countDown: "+timeOut + " : position id : " + positionId);
					timeOut--;
					Sleep(1000);
				}
				ApplicationLauncher.logger.info("dutSendCmd: timeOut Exit: " + " : position id : " + positionId);
			}
			//DisplayDataObj.setDUT1_ReadDataFlag( false);
			DisplayDataObj.bDutReadDataList.set((positionId-1),  false);
		//}
			
		responseReturn.put("status", status);
		return responseReturn;//status;
	}*/
	
	
	public  DutResponse dutSendCmd(int positionId){
		ApplicationLauncher.logger.debug("dutSendCmd :Entry : positionId : " + positionId);
		boolean status=false;
		DutResponse dutResponse = new DutResponse();
		// Map<String, Object> responseReturn = new HashMap<>();
	    //    responseReturn.put("status", false);
	    //    responseReturn.put("result", "");
		//status = Check_And_SendDUTCommand();

		//if (DutPortSetupController.getPortValidationTurnedON()){
			//ApplicationLauncher.logger.info("lscsDUT1_RelayConnectCmd: getPortValidationTurnedON");
			
			
			//calibDUT1_ClearSerialData();
			calibDutX_ClearSerialData(positionId);

			//DisplayDataObj.setDUT1_ReadDataFlag( true);
			DisplayDataObj.bDutReadDataList.set((positionId-1),  true);
			
			int numberOfReadings = 1;
			String expectedResponse = DisplayDataObj.getDutCommandData().getResponseExpectedData();//ConstantDut.ER_MTR_RTC_READ;
			String expectedResponseInHex ="";
			String targetCommand = DisplayDataObj.getDutCommandData().getTargetCmd();
			/*if(isDataAppend) {
				targetCommand = targetCommand + appendData;
			}*/
			String targetCommandInAscii = "";
			
			//String timeOutStr  = DisplayDataObj.getDutResponseTimeOutInSec();
			int haltTimeInSec  = DisplayDataObj.getDutCommandData().getHaltTimeInSec();
			boolean commandInHexMode = DisplayDataObj.getDutCommandData().isTargetCmdInHex();//.isDutCommandInHexMode();
			boolean dutResponseMandatory = DisplayDataObj.getDutCommandData().isResponseMandatory();
			String serialNoSourceType = DisplayDataObj.getDutCommandData().getSerialNoSourceType();
			
			boolean dutWriteSerialNoToDutEnabled = DisplayDataObj.getDutCommandData().isWriteSerialNoToDut();
			boolean dutReadSerialNoFromDutEnabled = DisplayDataObj.getDutCommandData().isReadSerialNoFromDut();
			if(commandInHexMode) {
				targetCommandInAscii = GuiUtils.hexToAsciiV2(targetCommand);
				expectedResponseInHex = expectedResponse;
				//expectedResponse = GuiUtils.asciiToHex(expectedResponse);
			}else {
				targetCommandInAscii = targetCommand;
				expectedResponseInHex = GuiUtils.asciiToHex(expectedResponse);
			}
			
			
			String commandTerminator = DisplayDataObj.getDutCommandData().getTargetCmdTerminator();//.getDutTargetCommandTerminator();
			String commandTerminatorInAscii = "";
			
			boolean commandTerminatorInHexMode = DisplayDataObj.getDutCommandData().isTargetCmdTerminatorInHex();//.isDutCommandTerminatorInHexMode();
			
			if(commandTerminatorInHexMode) {
				commandTerminatorInAscii = GuiUtils.hexToAsciiV2(commandTerminator);
			}else {
				commandTerminatorInAscii = commandTerminator;
			}
			String dutSerialNumberInAscii = "";
			if(dutWriteSerialNoToDutEnabled) {
				dutSerialNumberInAscii = DisplayDataObj.getDutSerialNumberMap(positionId);
				ApplicationLauncher.logger.debug("dutSendCmd : dutSerialNumberInAscii : <" + dutSerialNumberInAscii + "> : position id : " + positionId);
			}
			
			boolean dutWriteRtcToDutEnabled = DisplayDataObj.getDutCommandData().isWriteRtcToDut();
			boolean dutReadRtcFromDutEnabled = DisplayDataObj.getDutCommandData().isReadRtcFromDut();
			
			String rtcInAscii = "";
			boolean rtcConvertTotHex = DisplayDataObj.getDutCommandData().isRtcConvertToHex();
			if(dutWriteRtcToDutEnabled) {
				String rtcDateTimeFormat = DisplayDataObj.getDutCommandData().getRtcDateTimeFormat();
				LocalDateTime now = LocalDateTime.now();
		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(rtcDateTimeFormat);
		        String formattedTime = now.format(formatter); 
		        ApplicationLauncher.logger.debug("dutSendCmd : formattedTime : <" + formattedTime + "> : position id : " + positionId);
		        if(rtcConvertTotHex) {
		        	 String formatedTimeInHex = bindTwoCharToHex(formattedTime);
				     ApplicationLauncher.logger.debug("dutSendCmd : formatedTimeInHex : <" + formatedTimeInHex + "> : position id : " + positionId);
				     rtcInAscii = GuiUtils.hexToAsciiV2(formatedTimeInHex);
				     ApplicationLauncher.logger.debug("dutSendCmd : rtcInAscii-binding : <" + rtcInAscii + "> : position id : " + positionId);
		        }else {
		        	 rtcInAscii = formattedTime;
				     ApplicationLauncher.logger.debug("dutSendCmd : rtcInAscii-2 : <" + rtcInAscii + "> : position id : " + positionId);
		        }
		        
		        
			}
			
			String absoluteCommandInAscii = targetCommandInAscii + dutSerialNumberInAscii + rtcInAscii +commandTerminatorInAscii;
			ApplicationLauncher.logger.debug("dutSendCmd : absoluteCommandInHex : <" + GuiUtils.asciiToHex(absoluteCommandInAscii) + "> : position id : " + positionId);
	        
			ApplicationLauncher.logger.debug("dutSendCmd : absoluteCommandInAscii : " + absoluteCommandInAscii + " : position id : " + positionId);
			ApplicationLauncher.logger.debug("dutSendCmd : expectedResponseInHex length: " + expectedResponseInHex.length()+ " : position id : " + positionId);
			calibrationSendCommandV2(positionId,absoluteCommandInAscii);
			
			if(dutResponseMandatory) {
				
				String expectedResponseTerminatorInHex = "";
				if(DisplayDataObj.getDutCommandData().getResponseTerminatorInHex()) {//.isDutResponseTerminatorInHexMode()) {
					expectedResponseTerminatorInHex = DisplayDataObj.getDutCommandData().getResponseTerminator();//.getDutResponseTerminator();
				}else {
					expectedResponseTerminatorInHex = GuiUtils.asciiToHex(DisplayDataObj.getDutCommandData().getResponseTerminator());//.getDutResponseTerminator());
				}
					
				SerialDataDUT dutData = dutReadDataV2(positionId, (expectedResponseInHex.length()+14),expectedResponseInHex,numberOfReadings,expectedResponseTerminatorInHex);
				//lsDUT_SendCommandReadAccuracyData(ConstantLscsDUT.CMD_DUT_POSITION_01_HDR);
				//SerialDataDUT dutData = DUT_ReadData(ConstantLscsDUT.CMD_DUT_ERROR_DATA_ER.length(),"");//ConstantLsDUT.CMD_DUT_ERROR_DATA_ER);
				if(dutData.IsExpectedResponseReceived()){
	
					String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
					ApplicationLauncher.logger.info("dutSendCmd: Expected recieved:"+CurrentDUT_Data+ " : position id : " + positionId);
					StripDUT_SerialData(positionId,dutData.getReceivedLength());
					if(dutReadSerialNoFromDutEnabled) {
						String readSerialNoInHex = new String (CurrentDUT_Data);
						readSerialNoInHex = readSerialNoInHex.replaceFirst(expectedResponseInHex, "");// replace starting header
						readSerialNoInHex = readSerialNoInHex.replaceAll(expectedResponseTerminatorInHex+"$","");///replace last terminator
						String readSerialNoInAscii = GuiUtils.hexToAsciiV2(readSerialNoInHex);
						ApplicationLauncher.logger.info("dutSendCmd: Expected recieved: readSerialNoInAscii:<"+readSerialNoInAscii+ "> : position id : " + positionId);
						//responseReturn.put("result", readSerialNoInAscii);
						dutResponse.setResponseData(readSerialNoInAscii);
					}
					if(dutReadRtcFromDutEnabled) {
						
						String readRtcInHex = new String (CurrentDUT_Data);
						readRtcInHex = readRtcInHex.replaceFirst(expectedResponseInHex, "");// replace starting header
						readRtcInHex = readRtcInHex.replaceAll(expectedResponseTerminatorInHex+"$","");///replace last terminator
						ApplicationLauncher.logger.info("dutSendCmd: Expected recieved: readRtcInHex-binding:<"+readRtcInHex+ "> : position id : " + positionId);
						
						String readRtcInAscii = "";
						if(rtcConvertTotHex) {
							readRtcInAscii = bindHexToTwoChar(readRtcInHex);
							ApplicationLauncher.logger.info("dutSendCmd: Expected recieved: readRtcInAscii-binding:<"+readRtcInAscii+ "> : position id : " + positionId);
							
						}else {
							readRtcInAscii = GuiUtils.hexToAsciiV2(readRtcInHex);
							ApplicationLauncher.logger.info("dutSendCmd: Expected recieved: readRtcInAscii-2:<"+readRtcInAscii+ "> : position id : " + positionId);
							
						}
						
						//responseReturn.put("result", readSerialNoInAscii);
						dutResponse.setResponseData(readRtcInAscii);
						
					}
					status=true;
				}else{
					String CurrentDUT_Data =dutData.getDUT_ReadSerialData();
					ApplicationLauncher.logger.info("dutSendCmd: Failed: Unexpected Data: CurrentDUT_Data2: <"+CurrentDUT_Data+ "> : position id : " + positionId);
				}
			}else {
				
				status=true;
			}
			
			//if(!haltTimeInSecStr.equals("")) {
				int timeOut = haltTimeInSec;// Integer.parseInt(haltTimeInSecStr);
				ApplicationLauncher.logger.info("dutSendCmd: timeOut Entry: "+ " : position id : " + positionId);		
				while((timeOut>0) && (!ProjectExecutionController.getUserAbortedFlag())) {
					ApplicationLauncher.logger.info("dutSendCmd: timeOut countDown: "+timeOut + " : position id : " + positionId);
					timeOut--;
					Sleep(1000);
				}
				ApplicationLauncher.logger.info("dutSendCmd: timeOut Exit: " + " : position id : " + positionId);
			//}
			//DisplayDataObj.setDUT1_ReadDataFlag( false);
			DisplayDataObj.bDutReadDataList.set((positionId-1),  false);
		//}
			
		//responseReturn.put("status", status);
		dutResponse.setStatus(status);
		return dutResponse;//status;
	}
	
	public String bindTwoCharToHex (String dateTimeStr){
		String hexData = "";
	        //String dateTimeStr = "250514102345"; // yyMMddHHmmss format
	        for (int i = 0; i < dateTimeStr.length(); i += 2) {
	            String pair = dateTimeStr.substring(i, i + 2);
	            int decimal = Integer.parseInt(pair);
	            hexData = hexData + String.format("%02X", decimal);
	            //System.out.println("Pair: " + pair + " -> Hex: " + hex);
	        }
	    //}
	    ApplicationLauncher.logger.info("dutSendCmd: bindTwoCharToHex : hexData " + hexData );
		return hexData;
	}
	
	
	public String bindHexToTwoChar(String hexStr) {
	    StringBuilder result = new StringBuilder();
	    for (int i = 0; i < hexStr.length(); i += 2) {
	        String hexPair = hexStr.substring(i, i + 2);
	        int decimal = Integer.parseInt(hexPair, 16);
	        result.append(String.format("%02d", decimal)); // Ensure 2-digit format
	    }
	    ApplicationLauncher.logger.info("dutSendCmd: bindHexToTwoChar : dateTimeStr " + result.toString());
	    return result.toString();
	}
	
	public SerialDataDUT dutReadDataV2(int positionId, int Expectedlength,String ExpectedResponse, int numberOfReadings,String expectedResponseTerminatorInHex){
		ApplicationLauncher.logger.debug("dutReadDataV2 :Entry " + " : position id : " + positionId);
		
		Communicator SerialPortObj =comDutList.get(positionId-1);;//commDUT1;
		SerialPortObj = getDutSerialComObject(positionId);
		SerialPortObj.setExpectedLength(Expectedlength);
		SerialPortObj.setExpectedResult(ExpectedResponse);
		//SerialPortObj.setExpectedResponseTerminatorInHex(expectedResponseTerminatorInHex);
		
		//SerialPortObj.ClearSerialData();
		ApplicationLauncher.logger.debug("dutReadDataV2: setExpectedResult: Hex   :"+SerialPortObj.getExpectedResult()+ " : position id : " + positionId);
		ApplicationLauncher.logger.debug("dutReadDataV2: setExpectedResult: Ascii :"+GuiUtils.hexToAsciiV2(SerialPortObj.getExpectedResult())+ " : position id : " + positionId);
		ApplicationLauncher.logger.debug("dutReadDataV2: setExpectedLength:"+SerialPortObj.getExpectedLength()+ " : position id : " + positionId);
		SerialDataDUT dutData = new SerialDataDUT(SerialPortObj,positionId);
		dutData.SerialReponseTimerStartV3(40,numberOfReadings, expectedResponseTerminatorInHex);
		SerialPortObj = null;//garbagecollector
		return dutData;
	}
	
	public void dutExecuteCommandTrigger() {
		ApplicationLauncher.logger.debug("dutExecuteCommandTrigger :Entry");
		dutCommandTimer = new Timer();
		dutCommandTimer.schedule(new dutExecuteCommandTask(), 100);
		ApplicationLauncher.logger.debug("dutExecuteCommandTrigger : Exit");

	}
	
	
	class dutExecuteCommandTask extends TimerTask {
		public void run() {

			
			ApplicationLauncher.logger.debug("dutExecuteCommandTask : getDeviceMountedMap: " + ProjectExecutionController.getDeviceMountedMap());
			ProjectExecutionController.getDeviceMountedMap().keySet().parallelStream().forEach(lduPosition -> {
			    try {
			        int positionId = Integer.parseInt(lduPosition);
			        //Map<String, Object> responseReturn = new HashMap<>();
			        DutResponse dutResponse = new DutResponse();
			        //responseReturn.put("status", false);
			        //responseReturn.put("result", "NG");
			        dutResponse.setResponseData("NG");
			        ApplicationLauncher.logger.debug("dutExecuteCommandTask: positionId:" + positionId);
			        boolean status = true;
			        if(!comSerialStatusConnectedList.get(positionId-1)) {    
			        	dutResponse = serialConnectDutX(positionId);
			            status =  dutResponse.getStatus();//(boolean)responseReturn.get("status");
			        }
			        if(status) {
			        	//status = dutSendCmd(positionId);
			        	
			        	dutResponse = dutSendCmd(positionId);
			        	status = dutResponse.getStatus();//(boolean)responseReturn.get("status");
			        	String resultStatus, resultValue;
			        	resultValue = dutResponse.getResponseData();//(String)responseReturn.get("result");

			        	if(status) {
			        		resultStatus = ConstantReport.RESULT_STATUS_PASS;
			        		if(resultValue.equals("")) {
			        			resultValue = ConstantReport.REPORT_POPULATE_PASS;
			        		}
			        		//
			        		
			        	} else {
			        		resultStatus = ConstantReport.RESULT_STATUS_FAIL;
			        		resultValue = ConstantReport.REPORT_POPULATE_FAIL;
			        	}
			        	LiveTableDataManager.UpdateliveTableData(positionId, resultStatus, resultValue);
			        } else {
			        	String responseData = dutResponse.getResponseData();//(String)responseReturn.get("result");
			        	LiveTableDataManager.UpdateliveTableData(
			        			positionId, 
			        			ConstantReport.RESULT_STATUS_FAIL, 
			        			responseData
			        			);
			        }
			       
			    } catch (Exception e) {
			        ApplicationLauncher.logger.error("Error in parallel execution for position " + lduPosition, e);
			    }
			});
			
			ApplicationLauncher.logger.debug("dutExecuteCommandTask :resetting setExecuteTimeCounter to zero");
			ProjectExecutionController.setExecuteTimeCounter(0);
			Sleep(2000);
			//ProjectExecutionController.semLockExecutionInprogress = false;
			dutCommandTimer.cancel();
		}
	}
	
/*	public void dutExecuteCommandV3Trigger() {
		ApplicationLauncher.logger.debug("dutExecuteCommandV3Trigger :Entry");
		dutCommandTimer = new Timer();
		dutCommandTimer.schedule(new dutExecuteCommandV3Task(), 100);
		ApplicationLauncher.logger.debug("dutExecuteCommandV3Trigger : Exit");

	}*/
	
	class dutExecuteCommandV3Task extends TimerTask {
		public void run() {
			
			
			//int dutPositionNo = 1;
			ProjectExecutionController.getDeviceMountedMap().keySet().parallelStream().forEach(lduPosition -> {
				try {
					int dutPositionNo = Integer.parseInt(lduPosition);
					DutCmdManager dutCmdManager =  new DutCmdManager();//DeviceDataManagerController.getDutCmdManagerV3();
					DutResponse dutResponse  = new DutResponse();
					if(!dutCmdManager.isComSerialStatusConnected(dutPositionNo))  {
						String deviceId = "010101QR01";//"010204EM01";
							dutResponse  = dutCmdManager.dutCmdSerialPortAccessible(deviceId,dutPositionNo);
							
					}
					if(dutResponse.isStatus()) {
						SerialPortManagerDutCmd_V3 dutSpm = dutCmdManager.getDutSpm(dutPositionNo);
						DutCmdDirectorV3 dutCmdDirectorV3 = new DutCmdDirectorV3(dutSpm);
						dutResponse = dutCmdDirectorV3.dutMsngrSendCommandProcess();
						ApplicationLauncher.logger.debug("dutCommandTesting: getStatus: " + dutResponse.getStatus());
						
						ApplicationLauncher.logger.debug("dutCommandTesting: getResponseData: " + dutResponse.getResponseData());
						String dutResponseDataInHex = GuiUtils.asciiToHex(dutResponse.getResponseData());
						ApplicationLauncher.logger.debug("dutCommandTesting: dutResponseDataInHex: " + dutResponseDataInHex);
						dutCmdManager.dutCmdDisconnectPort_V2(dutPositionNo);
					}else {
						ApplicationLauncher.logger.debug("dutCommandTesting: Access failed : getStatus: " + dutResponse.getStatus());
						
						ApplicationLauncher.logger.debug("dutCommandTesting: Access failed : getResponseData: " + dutResponse.getResponseData());
						
					}
			    } catch (Exception e) {
			        ApplicationLauncher.logger.error("Error in parallel execution for position " + lduPosition, e);
			    }
			});

/*			/.?>
			ApplicationLauncher.logger.debug("dutExecuteCommandV3Task : getDeviceMountedMap: " + ProjectExecutionController.getDeviceMountedMap());
			ProjectExecutionController.getDeviceMountedMap().keySet().parallelStream().forEach(lduPosition -> {
			    try {
			        int positionId = Integer.parseInt(lduPosition);
			        //Map<String, Object> responseReturn = new HashMap<>();
			        DutResponse dutResponse = new DutResponse();
			        //responseReturn.put("status", false);
			        //responseReturn.put("result", "NG");
			        dutResponse.setResponseData("NG");
			        ApplicationLauncher.logger.debug("dutExecuteCommandTask: positionId:" + positionId);
			        boolean status = true;
			        if(!comSerialStatusConnectedList.get(positionId-1)) {    
			        	dutResponse = serialConnectDutX(positionId);
			            status =  dutResponse.getStatus();//(boolean)responseReturn.get("status");
			        }
			        if(status) {
			        	//status = dutSendCmd(positionId);
			        	
			        	dutResponse = dutSendCmd(positionId);
			        	status = dutResponse.getStatus();//(boolean)responseReturn.get("status");
			        	String resultStatus, resultValue;
			        	resultValue = dutResponse.getResponseData();//(String)responseReturn.get("result");

			        	if(status) {
			        		resultStatus = ConstantReport.RESULT_STATUS_PASS;
			        		if(resultValue.equals("")) {
			        			resultValue = ConstantReport.REPORT_POPULATE_PASS;
			        		}
			        		//
			        		
			        	} else {
			        		resultStatus = ConstantReport.RESULT_STATUS_FAIL;
			        		resultValue = ConstantReport.REPORT_POPULATE_FAIL;
			        	}
			        	LiveTableDataManager.UpdateliveTableData(positionId, resultStatus, resultValue);
			        } else {
			        	String responseData = dutResponse.getResponseData();//(String)responseReturn.get("result");
			        	LiveTableDataManager.UpdateliveTableData(
			        			positionId, 
			        			ConstantReport.RESULT_STATUS_FAIL, 
			        			responseData
			        			);
			        }
			       
			    } catch (Exception e) {
			        ApplicationLauncher.logger.error("Error in parallel execution for position " + lduPosition, e);
			    }
			});*/
			
			ApplicationLauncher.logger.debug("dutExecuteCommandV3Task :resetting setExecuteTimeCounter to zero");
			ProjectExecutionController.setExecuteTimeCounter(0);
			Sleep(2000);
			//ProjectExecutionController.semLockExecutionInprogress = false;
			dutCommandTimer.cancel();
		}
	}
	

	
	public void calibDUT15_ClearSerialData(){
		commDUT15.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT15(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT15 :Data:"+Data);
		try {
			commDUT15.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT15 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT16_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT16_Init :Entry");

		boolean status =false;
		try {
			dut16ComSerialStatusConnected = DUT16_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT16_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT16_Init : Exit");
		status=dut16ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT16_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT16_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT16,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT16.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT16_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	

	public void calibDUT16_ClearSerialData(){
		commDUT16.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT16(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT16 :Data:"+Data);
		try {
			commDUT16.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT16 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT17_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT17_Init :Entry");

		boolean status =false;
		try {
			dut17ComSerialStatusConnected = DUT17_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT17_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT17_Init : Exit");
		status=dut17ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT17_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT17_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT17,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT17.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT17_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	
	
	public void calibDUT17_ClearSerialData(){
		commDUT17.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT17(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT17 :Data:"+Data);
		try {
			commDUT17.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT17 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT18_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT18_Init :Entry");

		boolean status =false;
		try {
			dut18ComSerialStatusConnected = DUT18_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT18_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT18_Init : Exit");
		status=dut18ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT18_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT18_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT18,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT18.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT18_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	

	public void calibDUT18_ClearSerialData(){
		commDUT18.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT18(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT18 :Data:"+Data);
		try {
			commDUT18.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT18 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT19_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT19_Init :Entry");

		boolean status =false;
		try {
			dut19ComSerialStatusConnected = DUT19_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT19_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT19_Init : Exit");
		status=dut19ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT19_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT19_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT19,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT19.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT19_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	
	
	public void calibDUT19_ClearSerialData(){
		commDUT19.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT19(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT19 :Data:"+Data);
		try {
			commDUT19.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT19 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT20_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT20_Init :Entry");

		boolean status =false;
		try {
			dut20ComSerialStatusConnected = DUT20_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT20_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT20_Init : Exit");
		status=dut20ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT20_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT20_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT20,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT20.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT20_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	
	
	public void calibDUT20_ClearSerialData(){
		commDUT20.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT20(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT20 :Data:"+Data);
		try {
			commDUT20.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT20 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT21_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT21_Init :Entry");

		boolean status =false;
		try {
			dut21ComSerialStatusConnected = DUT21_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT21_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT21_Init : Exit");
		status=dut21ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT21_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT21_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT21,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT21.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT21_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	
	
	public void calibDUT21_ClearSerialData(){
		commDUT21.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT21(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT21 :Data:"+Data);
		try {
			commDUT21.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT21 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT22_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT22_Init :Entry");

		boolean status =false;
		try {
			dut22ComSerialStatusConnected = DUT22_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT22_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT22_Init : Exit");
		status=dut22ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT22_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT22_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT22,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT22.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT22_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 

	
	public void calibDUT22_ClearSerialData(){
		commDUT22.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT22(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT22 :Data:"+Data);
		try {
			commDUT22.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT22 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT23_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT23_Init :Entry");

		boolean status =false;
		try {
			dut23ComSerialStatusConnected = DUT23_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT23_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT23_Init : Exit");
		status=dut23ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT23_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT23_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT23,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT23.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT23_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	
	
	public void calibDUT23_ClearSerialData(){
		commDUT23.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT23(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT23 :Data:"+Data);
		try {
			commDUT23.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT23 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT24_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT24_Init :Entry");

		boolean status =false;
		try {
			dut24ComSerialStatusConnected = DUT24_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT24_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT24_Init : Exit");
		status=dut24ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT24_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT24_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT24,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT24.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT24_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	

	public void calibDUT24_ClearSerialData(){
		commDUT24.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT24(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT24 :Data:"+Data);
		try {
			commDUT24.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT24 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT25_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT25_Init :Entry");

		boolean status =false;
		try {
			dut25ComSerialStatusConnected = DUT25_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT25_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT25_Init : Exit");
		status=dut25ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT25_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT25_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT25,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT25.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT25_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	
	
	public void calibDUT25_ClearSerialData(){
		commDUT25.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT25(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT25 :Data:"+Data);
		try {
			commDUT25.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT25 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT26_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT26_Init :Entry");

		boolean status =false;
		try {
			dut26ComSerialStatusConnected = DUT26_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT26_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT26_Init : Exit");
		status=dut26ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT26_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT26_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT26,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT26.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT26_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	
	
	public void calibDUT26_ClearSerialData(){
		commDUT26.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT26(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT26 :Data:"+Data);
		try {
			commDUT26.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT26 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT27_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT27_Init :Entry");

		boolean status =false;
		try {
			dut27ComSerialStatusConnected = DUT27_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT27_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT27_Init : Exit");
		status=dut27ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT27_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT27_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT27,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT27.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT27_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	
	
	public void calibDUT27_ClearSerialData(){
		commDUT27.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT27(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT27 :Data:"+Data);
		try {
			commDUT27.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT27 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT28_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT28_Init :Entry");

		boolean status =false;
		try {
			dut28ComSerialStatusConnected = DUT28_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT28_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT28_Init : Exit");
		status=dut28ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT28_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT28_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT28,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT28.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT28_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	
	
	public void calibDUT28_ClearSerialData(){
		commDUT28.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT28(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT28 :Data:"+Data);
		try {
			commDUT28.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT28 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT29_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT29_Init :Entry");

		boolean status =false;
		try {
			dut29ComSerialStatusConnected = DUT29_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT29_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT29_Init : Exit");
		status=dut29ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT29_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT29_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT29,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT29.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT29_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	

	public void calibDUT29_ClearSerialData(){
		commDUT29.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT29(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT29 :Data:"+Data);
		try {
			commDUT29.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT29 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT30_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT30_Init :Entry");

		boolean status =false;
		try {
			dut30ComSerialStatusConnected = DUT30_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT30_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT30_Init : Exit");
		status=dut30ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT30_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT30_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT30,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT30.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT30_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	
	
	public void calibDUT30_ClearSerialData(){
		commDUT30.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT30(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT30 :Data:"+Data);
		try {
			commDUT30.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT30 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT31_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT31_Init :Entry");

		boolean status =false;
		try {
			dut31ComSerialStatusConnected = DUT31_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT31_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT31_Init : Exit");
		status=dut31ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT31_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT31_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT31,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT31.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT31_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	
	
	public void calibDUT31_ClearSerialData(){
		commDUT31.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT31(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT31 :Data:"+Data);
		try {
			commDUT31.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT31 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT32_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT32_Init :Entry");

		boolean status =false;
		try {
			dut32ComSerialStatusConnected = DUT32_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT32_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT32_Init : Exit");
		status=dut32ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT32_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT32_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT32,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT32.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT32_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 

	
	public void calibDUT32_ClearSerialData(){
		commDUT32.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT32(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT32 :Data:"+Data);
		try {
			commDUT32.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT32 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT33_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT33_Init :Entry");

		boolean status =false;
		try {
			dut33ComSerialStatusConnected = DUT33_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT33_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT33_Init : Exit");
		status=dut33ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT33_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT33_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT33,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT33.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT33_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	

	
	public void calibDUT33_ClearSerialData(){
		commDUT33.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT33(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT33 :Data:"+Data);
		try {
			commDUT33.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT33 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT34_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT34_Init :Entry");

		boolean status =false;
		try {
			dut34ComSerialStatusConnected = DUT34_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT34_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT34_Init : Exit");
		status=dut34ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT34_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT34_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT34,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT34.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT34_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	

	public void calibDUT34_ClearSerialData(){
		commDUT34.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT34(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT34 :Data:"+Data);
		try {
			commDUT34.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT34 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT35_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT35_Init :Entry");

		boolean status =false;
		try {
			dut35ComSerialStatusConnected = DUT35_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT35_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT35_Init : Exit");
		status=dut35ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT35_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT35_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT35,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT35.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT35_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	

	public void calibDUT35_ClearSerialData(){
		commDUT35.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT35(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT35 :Data:"+Data);
		try {
			commDUT35.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT35 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT36_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT36_Init :Entry");

		boolean status =false;
		try {
			dut36ComSerialStatusConnected = DUT36_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT36_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT36_Init : Exit");
		status=dut36ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT36_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT36_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT36,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT36.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT36_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	

	public void calibDUT36_ClearSerialData(){
		commDUT36.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT36(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT36 :Data:"+Data);
		try {
			commDUT36.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT36 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT37_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT37_Init :Entry");

		boolean status =false;
		try {
			dut37ComSerialStatusConnected = DUT37_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT37_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT37_Init : Exit");
		status=dut37ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT37_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT37_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT37,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT37.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT37_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 

	
	public void calibDUT37_ClearSerialData(){
		commDUT37.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT37(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT37 :Data:"+Data);
		try {
			commDUT37.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT37 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT38_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT38_Init :Entry");

		boolean status =false;
		try {
			dut38ComSerialStatusConnected = DUT38_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT38_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT38_Init : Exit");
		status=dut38ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT38_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT38_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT38,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT38.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT38_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	

	
	public void calibDUT38_ClearSerialData(){
		commDUT38.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT38(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT38 :Data:"+Data);
		try {
			commDUT38.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT38 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT39_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT39_Init :Entry");

		boolean status =false;
		try {
			dut39ComSerialStatusConnected = DUT39_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT39_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT39_Init : Exit");
		status=dut39ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT39_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT39_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT39,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT39.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT39_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	

	public void calibDUT39_ClearSerialData(){
		commDUT39.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT39(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT39 :Data:"+Data);
		try {
			commDUT39.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT39 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT40_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT40_Init :Entry");

		boolean status =false;
		try {
			dut40ComSerialStatusConnected = DUT40_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT40_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT40_Init : Exit");
		status=dut40ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT40_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT40_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT40,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT40.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT40_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 

	public void calibDUT40_ClearSerialData(){
		commDUT40.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT40(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT40 :Data:"+Data);
		try {
			commDUT40.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT40 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT41_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT41_Init :Entry");

		boolean status =false;
		try {
			dut41ComSerialStatusConnected = DUT41_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT41_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT41_Init : Exit");
		status=dut41ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT41_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT41_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT41,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT41.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT41_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	

	
	public void calibDUT41_ClearSerialData(){
		commDUT41.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT41(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT41 :Data:"+Data);
		try {
			commDUT41.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT41 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT42_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT42_Init :Entry");

		boolean status =false;
		try {
			dut42ComSerialStatusConnected = DUT42_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT42_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT42_Init : Exit");
		status=dut42ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT42_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT42_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT42,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT42.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT42_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	

	
	public void calibDUT42_ClearSerialData(){
		commDUT42.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT42(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT42 :Data:"+Data);
		try {
			commDUT42.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT42 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT43_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT43_Init :Entry");

		boolean status =false;
		try {
			dut43ComSerialStatusConnected = DUT43_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT43_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT43_Init : Exit");
		status=dut43ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT43_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT43_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT43,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT43.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT43_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	

	public void calibDUT43_ClearSerialData(){
		commDUT43.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT43(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT43 :Data:"+Data);
		try {
			commDUT43.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT43 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT44_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT44_Init :Entry");

		boolean status =false;
		try {
			dut44ComSerialStatusConnected = DUT44_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT44_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT44_Init : Exit");
		status=dut44ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT44_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT44_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT44,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT44.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT44_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	

	
	public void calibDUT44_ClearSerialData(){
		commDUT44.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT44(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT44 :Data:"+Data);
		try {
			commDUT44.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT44 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT45_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT45_Init :Entry");

		boolean status =false;
		try {
			dut45ComSerialStatusConnected = DUT45_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT45_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT45_Init : Exit");
		status=dut45ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT45_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT45_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT45,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT45.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT45_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	

	public void calibDUT45_ClearSerialData(){
		commDUT45.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT45(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT45 :Data:"+Data);
		try {
			commDUT45.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT45 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT46_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT46_Init :Entry");

		boolean status =false;
		try {
			dut46ComSerialStatusConnected = DUT46_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT46_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT46_Init : Exit");
		status=dut46ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT46_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT46_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT46,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT46.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT46_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	

	
	public void calibDUT46_ClearSerialData(){
		commDUT46.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT46(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT46 :Data:"+Data);
		try {
			commDUT46.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT46 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT47_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT47_Init :Entry");

		boolean status =false;
		try {
			dut47ComSerialStatusConnected = DUT47_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT47_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT47_Init : Exit");
		status=dut47ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT47_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT47_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT47,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT47.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT47_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 

	public void calibDUT47_ClearSerialData(){
		commDUT47.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT47(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT47 :Data:"+Data);
		try {
			commDUT47.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT47 :Exception :" + e.getMessage());
		}
	}
	/*********/
	
	public boolean DUT48_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("DUT48_Init :Entry");

		boolean status =false;
		try {
			dut48ComSerialStatusConnected = DUT48_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("DUT48_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("DUT48_Init : Exit");
		status=dut48ComSerialStatusConnected;

		return  status;

	}
	
	public boolean DUT48_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT48_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT48,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT48.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT48_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	

	
	public void calibDUT48_ClearSerialData(){
		commDUT48.ClearSerialData();
	}
	
	public void WriteToSerialCommDUT48(String Data){
		
		ApplicationLauncher.logger.debug("WriteToSerialCommDUT48 :Data:"+Data);
		try {
			commDUT48.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommDUT48 :Exception :" + e.getMessage());
		}
	}
	
	
	
	public DutResponse DutX_CommInit(String ComPort_ID, String BaudRate, int positionId) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DutX_CommInit :Entry");
		DutResponse dutResponse = new DutResponse();
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		//Map<String,Object> responseReturn = new HashMap<String,Object>();
		String responseData = "";
		//responseReturn.put("status", false);		
		//responseReturn.put("result", responseData);
		boolean status = SetSerialCommV2(comDutList.get(positionId-1),ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			comDutList.get(positionId-1).SetFlowControlMode();
			//commDUT1.SetFlowControlModeV2(SerialPort.FLOWCONTROL_NONE);
			//commDUT1.SetFlowControlModeV3(SerialPort.STOPBITS_1);
			//commDUT1.SetFlowControlModeV3(SerialPort.FLOWCONTROL_NONE);
		} else {
			responseData = "Access Failed";
			ApplicationLauncher.logger.info("DutX_CommInit:"+ComPort_ID+" access failed");
		}
		//responseReturn.put("status", status);
		//responseReturn.put("result", responseData);
		dutResponse.setStatus(status);
		dutResponse.setResponseData(responseData);
		return dutResponse;

	} 
	
	public boolean DUT1_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT1_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT1,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT1.SetFlowControlMode();
			//commDUT1.SetFlowControlModeV2(SerialPort.FLOWCONTROL_NONE);
			//commDUT1.SetFlowControlModeV3(SerialPort.STOPBITS_1);
			//commDUT1.SetFlowControlModeV3(SerialPort.FLOWCONTROL_NONE);
		} else {

			ApplicationLauncher.logger.info("DUT1_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	
	public boolean DUT2_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT2_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT2,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT2.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT2_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	
	public boolean DUT3_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT3_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT3,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT3.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT3_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	
	public boolean DUT4_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT4_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT4,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT4.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT4_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	
	public boolean DUT5_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT5_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT5,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT5.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT5_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	
	public boolean DUT6_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT6_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT6,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT6.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT6_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	
	public boolean DUT7_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("DUT7_CommInit :Entry");
		//boolean status = SetSerialComm(commDUT,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommV2(commDUT7,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commDUT7.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("DUT7_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 
	
	
	public void DisconnectDutX_SerialComm(int positionId){
		ApplicationLauncher.logger.info("DisconnectDutX_SerialComm: positionId: " + positionId); 
		comDutList.get(positionId-1).disconnect();
	}
	
	
	public void DisconnectDUT1_SerialComm(){
		//ApplicationLauncher.logger.info("DisconnectDUT1_SerialComm: Entry "); 
		commDUT1.disconnect();
	}
	
	public void DisconnectDUT2_SerialComm(){
		commDUT2.disconnect();
	}
	
	public void DisconnectDUT3_SerialComm(){
		commDUT3.disconnect();
	}
	
	public void DisconnectDUT4_SerialComm(){
		commDUT4.disconnect();
	}
	
	public void DisconnectDUT5_SerialComm(){
		commDUT5.disconnect();
	}
	
	public void DisconnectDUT6_SerialComm(){
		commDUT6.disconnect();
	}
	
	public void DisconnectDUT7_SerialComm(){
		commDUT7.disconnect();
	}
	
	
	public void DisconnectDUT8_SerialComm(){
		commDUT8.disconnect();
	}	
	
	public void DisconnectDUT9_SerialComm(){
		commDUT9.disconnect();
	}	
	
	public void DisconnectDUT10_SerialComm(){
		commDUT10.disconnect();
	}	
	
	public void DisconnectDUT11_SerialComm(){
		commDUT11.disconnect();
	}	
	
	public void DisconnectDUT12_SerialComm(){
		commDUT12.disconnect();
	}	
	
	public void DisconnectDUT13_SerialComm(){
		commDUT13.disconnect();
	}	
	
	public void DisconnectDUT14_SerialComm(){
		commDUT14.disconnect();
	}	
	
	public void DisconnectDUT15_SerialComm(){
		commDUT15.disconnect();
	}	
	

	public void DisconnectDUT16_SerialComm(){
		commDUT16.disconnect();
	}	

	public void DisconnectDUT17_SerialComm(){
		commDUT17.disconnect();
	}	

	public void DisconnectDUT18_SerialComm(){
		commDUT18.disconnect();
	}	

	public void DisconnectDUT19_SerialComm(){
		commDUT19.disconnect();
	}	
	

	public void DisconnectDUT20_SerialComm(){
		commDUT20.disconnect();
	}	
	public void DisconnectDUT21_SerialComm(){
		commDUT21.disconnect();
	}	
	
	public void DisconnectDUT22_SerialComm(){
		commDUT22.disconnect();
	}	
	public void DisconnectDUT23_SerialComm(){
		commDUT23.disconnect();
	}	
	
	public void DisconnectDUT24_SerialComm(){
		commDUT24.disconnect();
	}	
	
	public void DisconnectDUT25_SerialComm(){
		commDUT25.disconnect();
	}	
	

	public void DisconnectDUT26_SerialComm(){
		commDUT26.disconnect();
	}	
	
	public void DisconnectDUT27_SerialComm(){
		commDUT27.disconnect();
	}	
	
	public void DisconnectDUT28_SerialComm(){
		commDUT28.disconnect();
	}	
	
	public void DisconnectDUT29_SerialComm(){
		commDUT29.disconnect();
	}	
	
	public void DisconnectDUT30_SerialComm(){
		commDUT30.disconnect();
	}	
	
	public void DisconnectDUT31_SerialComm(){
		commDUT31.disconnect();
	}	
	
	public void DisconnectDUT32_SerialComm(){
		commDUT32.disconnect();
	}
	
	public void DisconnectDUT33_SerialComm(){
		commDUT33.disconnect();
	}	
	
	public void DisconnectDUT34_SerialComm(){
		commDUT34.disconnect();
	}	
	
	public void DisconnectDUT35_SerialComm(){
		commDUT35.disconnect();
	}	
	
	public void DisconnectDUT36_SerialComm(){
		commDUT36.disconnect();
	}	
	
	public void DisconnectDUT37_SerialComm(){
		commDUT37.disconnect();
	}	
	public void DisconnectDUT38_SerialComm(){
		commDUT38.disconnect();
	}
	
	public void DisconnectDUT39_SerialComm(){
		commDUT39.disconnect();
	}
	
	public void DisconnectDUT40_SerialComm(){
		commDUT40.disconnect();
	}
	public void DisconnectDUT41_SerialComm(){
		commDUT41.disconnect();
	}	
	public void DisconnectDUT42_SerialComm(){
		commDUT42.disconnect();
	}
	public void DisconnectDUT43_SerialComm(){
		commDUT43.disconnect();
	}
	public void DisconnectDUT44_SerialComm(){
		commDUT44.disconnect();
	}	
	public void DisconnectDUT45_SerialComm(){
		commDUT45.disconnect();
	}
	public void DisconnectDUT46_SerialComm(){
		commDUT46.disconnect();
	}	
	public void DisconnectDUT47_SerialComm(){
		commDUT47.disconnect();
	}
	public void DisconnectDUT48_SerialComm(){
		commDUT48.disconnect();
	}	
	
	
	public void calibDutX_ClearSerialData(int positionId){
		ApplicationLauncher.logger.info("calibDutX_ClearSerialData: positionId: " + positionId); 
		comDutList.get(positionId-1).ClearSerialData();
	}
	public void calibDUT1_ClearSerialData(){
		commDUT1.ClearSerialData();
	}
	public void calibDUT2_ClearSerialData(){
		commDUT2.ClearSerialData();
	}
	public void calibDUT3_ClearSerialData(){
		commDUT3.ClearSerialData();
	}
	public void calibDUT4_ClearSerialData(){
		commDUT4.ClearSerialData();
	}
	public void calibDUT5_ClearSerialData(){
		commDUT5.ClearSerialData();
	}
	public void calibDUT6_ClearSerialData(){
		commDUT6.ClearSerialData();
	}
	public void calibDUT7_ClearSerialData(){
		commDUT7.ClearSerialData();
	}
	
		
	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep2 :InterruptedException:"+ e.getMessage());
		}

	}
	
	public boolean SetSerialCommV2(Communicator SerialPortObj, String SerialPort_ID, Integer BaudRate,Boolean ReadHexFormat){
		ApplicationLauncher.logger.debug("DutSerialDataManager: SetSerialCommV2 :Entry");
		ApplicationLauncher.logger.debug("DutSerialDataManager: SerialPort_ID : " + SerialPort_ID);
		boolean status=false;

		SerialPortObj.connect(SerialPort_ID);
		if(SerialPortObj.getDeviceConnected()==true){

			if (SerialPortObj.initIOStream() == true){
				//SerialPortObj.SerialPortConfigV2(BaudRate);
				SerialPortObj.SerialPortConfigParityNone(BaudRate);
				//SerialPortObj.setPortDeviceMapping(SerialPort_ID);
				ApplicationLauncher.logger.info("DutSerialDataManager: PortDeviceMapping:"+ SerialPortObj.getPortDeviceMapping()+":"+ReadHexFormat);
				SerialPortObj.initListener();
				SerialPortObj.setDataReadFormatInHex(ReadHexFormat);
				//SerialPortObj.setDataReadFormatInHex(false);
				status = true;
				return status;
			}
		}
		return status;
	}
	
	
	public void calibrationSendCommandV2(int dutAddress, String data) {//, String expectedResponseTerminatorInHex){
		ApplicationLauncher.logger.debug("calibrationSendCommand :Entry");
		//data = data + ConstantDut.CMD_TERMINATOR;
		ApplicationLauncher.logger.debug("calibrationSendCommand : data: "+data);
		Communicator SerialPortObj =comDutList.get(dutAddress-1);;//commDUT1;
		//SerialPortObj = getDutSerialComObject(positionId);
		//SerialPortObj.setExpectedResponseTerminatorInHex(expectedResponseTerminatorInHex);
		//SerialPortObj.setExpectedResult(ExpectedResponse);
		//ApplicationLauncher.logger.debug("DUT_SendCommandReadErrorData :DUT_ReadAddress:Hex:"+String.format("%02d" , DUT_ReadAddress));
		//String Data = ConstantCcubeDUT.CMD_DUT_READ_ERROR_DATA_HDR+ ConstantCcubeDUT.DecodeHextoString(String.format("%02x" , DUT_ReadAddress).toUpperCase())+ConstantCcubeDUT.CMD_DUT_READ_ERROR_DATA_CMD_DATA+ConstantApp.END_CR2;

		switch (dutAddress) {

			case 1:
				WriteToSerialCommDUT1(data);
				break;
				
			case 2:
				WriteToSerialCommDUT2(data);
				break;
				
			case 3:
				WriteToSerialCommDUT3(data);
				break;
				
			case 4:
				WriteToSerialCommDUT4(data);
				break;
				
			case 5:
				WriteToSerialCommDUT5(data);
				break;
				
			case 6:
				WriteToSerialCommDUT6(data);
				break;
				
			case 7:
				WriteToSerialCommDUT7(data);
				break;
				
			case 8:
				WriteToSerialCommDUT8(data);
				break;
				
			case 9:
				WriteToSerialCommDUT9(data);
				break;
				
			case 10:
				WriteToSerialCommDUT10(data);
				break;
				
			case 11:
				WriteToSerialCommDUT11(data);
				break;
				
			case 12:
				WriteToSerialCommDUT12(data);
				break;
				
			case 13:
				WriteToSerialCommDUT13(data);
				break;
				
			case 14:
				WriteToSerialCommDUT14(data);
				break;
				
			case 15:
				WriteToSerialCommDUT15(data);
				break;
				
			case 16:
				WriteToSerialCommDUT16(data);
				break;
				
			case 17:
				WriteToSerialCommDUT17(data);
				break;
				
			case 18:
				WriteToSerialCommDUT18(data);
				break;
				
			case 19:
				WriteToSerialCommDUT19(data);
				break;
				
			case 20:
				WriteToSerialCommDUT20(data);
				break;
				
				
			case 21:
				WriteToSerialCommDUT21(data);
				break;
				
			case 22:
				WriteToSerialCommDUT22(data);
				break;
				
			case 23:
				WriteToSerialCommDUT23(data);
				break;
				
			case 24:
				WriteToSerialCommDUT24(data);
				break;
				
			case 25:
				WriteToSerialCommDUT25(data);
				break;
				
			case 26:
				WriteToSerialCommDUT26(data);
				break;
				
			case 27:
				WriteToSerialCommDUT27(data);
				break;
				
			case 28:
				WriteToSerialCommDUT28(data);
				break;
				
			case 29:
				WriteToSerialCommDUT29(data);
				break;
				
			case 30:
				WriteToSerialCommDUT30(data);
				break;
				
			case 31:
				WriteToSerialCommDUT31(data);
				break;
				
			case 32:
				WriteToSerialCommDUT32(data);
				break;
				
			case 33:
				WriteToSerialCommDUT33(data);
				break;
				
			case 34:
				WriteToSerialCommDUT34(data);
				break;
				
			case 35:
				WriteToSerialCommDUT35(data);
				break;
				
			case 36:
				WriteToSerialCommDUT36(data);
				break;
				
			case 37:
				WriteToSerialCommDUT37(data);
				break;
				
			case 38:
				WriteToSerialCommDUT38(data);
				break;
				
			case 39:
				WriteToSerialCommDUT39(data);
				break;
				
			case 40:
				WriteToSerialCommDUT40(data);
				break;
				
			case 41:
				WriteToSerialCommDUT41(data);
				break;
				
			case 42:
				WriteToSerialCommDUT42(data);
				break;
				
			case 43:
				WriteToSerialCommDUT43(data);
				break;
				
			case 44:
				WriteToSerialCommDUT44(data);
				break;
				
			case 45:
				WriteToSerialCommDUT45(data);
				break;
				
			case 46:
				WriteToSerialCommDUT46(data);
				break;
				
			case 47:
				WriteToSerialCommDUT47(data);
				break;
				
			case 48:
				WriteToSerialCommDUT48(data);
				break;

	
			default:
				break;
		}
		//commDUT.SetRTS(false);
	}
	
	public void calibrationSendCommand(int dutAddress, String data) {//, String expectedResponseTerminatorInHex){
		ApplicationLauncher.logger.debug("calibrationSendCommand :Entry");
		data = data + ConstantDut.CMD_TERMINATOR;
		ApplicationLauncher.logger.debug("calibrationSendCommand : data: "+data);
		Communicator SerialPortObj =comDutList.get(dutAddress-1);;//commDUT1;
		//SerialPortObj = getDutSerialComObject(positionId);
		//SerialPortObj.setExpectedResponseTerminatorInHex(expectedResponseTerminatorInHex);
		//SerialPortObj.setExpectedResult(ExpectedResponse);
		//ApplicationLauncher.logger.debug("DUT_SendCommandReadErrorData :DUT_ReadAddress:Hex:"+String.format("%02d" , DUT_ReadAddress));
		//String Data = ConstantCcubeDUT.CMD_DUT_READ_ERROR_DATA_HDR+ ConstantCcubeDUT.DecodeHextoString(String.format("%02x" , DUT_ReadAddress).toUpperCase())+ConstantCcubeDUT.CMD_DUT_READ_ERROR_DATA_CMD_DATA+ConstantApp.END_CR2;

		switch (dutAddress) {

			case 1:
				WriteToSerialCommDUT1(data);
				break;
				
			case 2:
				WriteToSerialCommDUT2(data);
				break;
				
			case 3:
				WriteToSerialCommDUT3(data);
				break;
				
			case 4:
				WriteToSerialCommDUT4(data);
				break;
				
			case 5:
				WriteToSerialCommDUT5(data);
				break;
				
			case 6:
				WriteToSerialCommDUT6(data);
				break;
				
			case 7:
				WriteToSerialCommDUT7(data);
				break;
				
			case 8:
				WriteToSerialCommDUT8(data);
				break;
				
			case 9:
				WriteToSerialCommDUT9(data);
				break;
				
			case 10:
				WriteToSerialCommDUT10(data);
				break;
				
			case 11:
				WriteToSerialCommDUT11(data);
				break;
				
			case 12:
				WriteToSerialCommDUT12(data);
				break;
				
			case 13:
				WriteToSerialCommDUT13(data);
				break;
				
			case 14:
				WriteToSerialCommDUT14(data);
				break;
				
			case 15:
				WriteToSerialCommDUT15(data);
				break;
				
			case 16:
				WriteToSerialCommDUT16(data);
				break;
				
			case 17:
				WriteToSerialCommDUT17(data);
				break;
				
			case 18:
				WriteToSerialCommDUT18(data);
				break;
				
			case 19:
				WriteToSerialCommDUT19(data);
				break;
				
			case 20:
				WriteToSerialCommDUT20(data);
				break;
				
				
			case 21:
				WriteToSerialCommDUT21(data);
				break;
				
			case 22:
				WriteToSerialCommDUT22(data);
				break;
				
			case 23:
				WriteToSerialCommDUT23(data);
				break;
				
			case 24:
				WriteToSerialCommDUT24(data);
				break;
				
			case 25:
				WriteToSerialCommDUT25(data);
				break;
				
			case 26:
				WriteToSerialCommDUT26(data);
				break;
				
			case 27:
				WriteToSerialCommDUT27(data);
				break;
				
			case 28:
				WriteToSerialCommDUT28(data);
				break;
				
			case 29:
				WriteToSerialCommDUT29(data);
				break;
				
			case 30:
				WriteToSerialCommDUT30(data);
				break;
				
			case 31:
				WriteToSerialCommDUT31(data);
				break;
				
			case 32:
				WriteToSerialCommDUT32(data);
				break;
				
			case 33:
				WriteToSerialCommDUT33(data);
				break;
				
			case 34:
				WriteToSerialCommDUT34(data);
				break;
				
			case 35:
				WriteToSerialCommDUT35(data);
				break;
				
			case 36:
				WriteToSerialCommDUT36(data);
				break;
				
			case 37:
				WriteToSerialCommDUT37(data);
				break;
				
			case 38:
				WriteToSerialCommDUT38(data);
				break;
				
			case 39:
				WriteToSerialCommDUT39(data);
				break;
				
			case 40:
				WriteToSerialCommDUT40(data);
				break;
				
			case 41:
				WriteToSerialCommDUT41(data);
				break;
				
			case 42:
				WriteToSerialCommDUT42(data);
				break;
				
			case 43:
				WriteToSerialCommDUT43(data);
				break;
				
			case 44:
				WriteToSerialCommDUT44(data);
				break;
				
			case 45:
				WriteToSerialCommDUT45(data);
				break;
				
			case 46:
				WriteToSerialCommDUT46(data);
				break;
				
			case 47:
				WriteToSerialCommDUT47(data);
				break;
				
			case 48:
				WriteToSerialCommDUT48(data);
				break;

	
			default:
				break;
		}
		//commDUT.SetRTS(false);
	}
	
	public Communicator getDutSerialComObject(int dutAddress){
		
		Communicator SerialPortObj =commDUT1;
		
		switch (dutAddress) {

			case 1:
				SerialPortObj = commDUT1;
				break;
				
			case 2:
				SerialPortObj = commDUT2;
				break;
				
			case 3:
				SerialPortObj = commDUT3;
				break;
				
			case 4:
				SerialPortObj = commDUT4;
				break;
				
			case 5:
				SerialPortObj = commDUT5;
				break;
				
			case 6:
				SerialPortObj = commDUT6;
				break;
				
			case 7:
				SerialPortObj = commDUT7;
				break;
	
			case 8:
				SerialPortObj = commDUT8;
				break;
				
			case 9:
				SerialPortObj = commDUT9;
				break;
			case 10:
				SerialPortObj = commDUT10;
				break;

	
			case 11:
				SerialPortObj = commDUT11;
				break;
				
			case 12:
				SerialPortObj = commDUT12;
				break;
				
			case 13:
				SerialPortObj = commDUT13;
				break;
				
			case 14:
				SerialPortObj = commDUT14;
				break;
				
			case 15:
				SerialPortObj = commDUT15;
				break;
				
			case 16:
				SerialPortObj = commDUT16;
				break;
				
			case 17:
				SerialPortObj = commDUT17;
				break;
	
			case 18:
				SerialPortObj = commDUT18;
				break;
				
			case 19:
				SerialPortObj = commDUT19;
				break;
			case 20:
				SerialPortObj = commDUT20;
				break;
				
				
			case 21:
				SerialPortObj = commDUT21;
				break;
				
			case 22:
				SerialPortObj = commDUT22;
				break;
				
			case 23:
				SerialPortObj = commDUT23;
				break;
				
			case 24:
				SerialPortObj = commDUT24;
				break;
				
			case 25:
				SerialPortObj = commDUT25;
				break;
				
			case 26:
				SerialPortObj = commDUT26;
				break;
				
			case 27:
				SerialPortObj = commDUT27;
				break;
	
			case 28:
				SerialPortObj = commDUT28;
				break;
				
			case 29:
				SerialPortObj = commDUT29;
				break;
			case 30:
				SerialPortObj = commDUT30;
				break;
				
				
			case 31:
				SerialPortObj = commDUT31;
				break;
				
			case 32:
				SerialPortObj = commDUT32;
				break;
				
			case 33:
				SerialPortObj = commDUT33;
				break;
				
			case 34:
				SerialPortObj = commDUT34;
				break;
				
			case 35:
				SerialPortObj = commDUT35;
				break;
				
			case 36:
				SerialPortObj = commDUT36;
				break;
				
			case 37:
				SerialPortObj = commDUT37;
				break;
	
			case 38:
				SerialPortObj = commDUT38;
				break;
				
			case 39:
				SerialPortObj = commDUT39;
				break;
			case 40:
				SerialPortObj = commDUT40;
				break;
				
				
				
			case 41:
				SerialPortObj = commDUT41;
				break;
				
			case 42:
				SerialPortObj = commDUT42;
				break;
				
			case 43:
				SerialPortObj = commDUT43;
				break;
				
			case 44:
				SerialPortObj = commDUT44;
				break;
				
			case 45:
				SerialPortObj = commDUT45;
				break;
				
			case 46:
				SerialPortObj = commDUT46;
				break;
				
			case 47:
				SerialPortObj = commDUT47;
				break;
	
			case 48:
				SerialPortObj = commDUT48;
				break;
				

	
			default:
				break;
		}
		return SerialPortObj;
	}
	
	public SerialDataDUT dutReadData(int dutAddress, int Expectedlength,String ExpectedResponse, int numberOfReadings){
		ApplicationLauncher.logger.debug("calibrationReadData :Entry");
		
		Communicator SerialPortObj =commDUT1;
		SerialPortObj = getDutSerialComObject(dutAddress);
		SerialPortObj.setExpectedLength(Expectedlength);
		SerialPortObj.setExpectedResult(ExpectedResponse);
		//SerialPortObj.ClearSerialData();
		ApplicationLauncher.logger.debug("calibrationReadData: setExpectedResult:"+SerialPortObj.getExpectedResult());
		ApplicationLauncher.logger.debug("calibrationReadData: setExpectedResult2:"+GuiUtils.hexToAscii(SerialPortObj.getExpectedResult()));
		ApplicationLauncher.logger.debug("calibrationReadData: setExpectedLength:"+SerialPortObj.getExpectedLength());
		SerialDataDUT dutData = new SerialDataDUT(SerialPortObj,dutAddress);
		dutData.SerialReponseTimerStart(40,numberOfReadings);
		SerialPortObj = null;//garbagecollector
		return dutData;
	}
	
	public void StripDUT_SerialData(int dutAddress,Integer length){
		ApplicationLauncher.logger.debug("StripLDU_SerialData :Entry");
		Communicator SerialPortObj =commDUT1;
		SerialPortObj = getDutSerialComObject(dutAddress);
		SerialPortObj.StripLength(length);
		ApplicationLauncher.logger.debug("StripLDU_SerialData : getSerialData :"+SerialPortObj.getSerialData());

	}
	
		

}
