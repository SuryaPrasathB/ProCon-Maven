package com.tasnetwork.calibration.conveyor.serial.portmanager;

import java.util.LinkedList;
import java.util.Queue;

import com.tasnetwork.calibration.conveyor.util.GUIUtils;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;


public class SerialTxMessageQ {


	Queue<String> txMsgQueue = new LinkedList<>(); 
	Communicator serialPortObj= null;

	public SerialTxMessageQ(Communicator inpSerialPortObj){
		serialPortObj = inpSerialPortObj;

	}
	
	public void addTxMsgQueue(String outputMessage) {
		
		
		ApplicationLauncher.logger.info("SerialTxMessageQ: addMsgQueue invoked");
		ApplicationLauncher.logger.info("SerialTxMessageQ: outputMessage:"+outputMessage);
		ApplicationLauncher.logger.info("SerialTxMessageQ: outputMessage-String:"+GUIUtils.HexToString(outputMessage));
		txMsgQueue.add(outputMessage);
		
	}

}
