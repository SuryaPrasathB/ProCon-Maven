package com.tasnetwork.calibration.energymeter.serial.portmanager;

import java.util.LinkedList;
import java.util.Queue;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.device.Communicator;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

/*import com.tasnetwork.calibration.ptct.ApplicationLauncher;
import com.tasnetwork.calibration.ptct.util.GUIUtils;*/

public class SerialTxMessageQ {
	
	Queue<String> txMsgQueue = new LinkedList<>(); 
	Communicator serialPortObj= null;

	public SerialTxMessageQ(Communicator inpSerialPortObj){
		serialPortObj = inpSerialPortObj;

	}
	
	public void addTxMsgQueue(String outputMessage) {
		
		
		ApplicationLauncher.logger.info("SerialTxMessageQ: addMsgQueue invoked");
		ApplicationLauncher.logger.info("SerialTxMessageQ: outputMessage:"+outputMessage);
		ApplicationLauncher.logger.info("SerialTxMessageQ: outputMessage-String:"+GuiUtils.HexToString(outputMessage));
		txMsgQueue.add(outputMessage);
		
	}

}
