package com.tasnetwork.calibration.energymeter.serial.portmanagerV2;

import java.util.LinkedList;
import java.util.Queue;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

public class SerialTxMessageQ_V2 {


	Queue<String> txMsgQueue = new LinkedList<>(); 
	CommunicatorV2 serialPortObj= null;

	public SerialTxMessageQ_V2(CommunicatorV2 inpSerialPortObj){
		serialPortObj = inpSerialPortObj;

	}
	
	public void addTxMsgQueue(String outputMessage) {
		
		
		ApplicationLauncher.logger.info("SerialTxMessageQ: addMsgQueue invoked");
		ApplicationLauncher.logger.info("SerialTxMessageQ: outputMessage:"+outputMessage);
		ApplicationLauncher.logger.info("SerialTxMessageQ: outputMessage-String:"+GuiUtils.HexToString(outputMessage));
		txMsgQueue.add(outputMessage);
		
	}

}
