package com.tasnetwork.calibration.energymeter.serial.portmanager;

import java.util.TimerTask;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.device.Communicator;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

import java.util.LinkedList; 
import java.util.Queue;

/*import com.tasnetwork.calibration.ptct.ApplicationLauncher;
import com.tasnetwork.calibration.ptct.device.SerialRxPhysical.SerialRxPhysicalTask;
import com.tasnetwork.calibration.ptct.util.GUIUtils;*/

public class SerialRxMessageQ {
	
	//String msgHV_StopResponse = ConstantHV_Source.HV_CMD_STOP+ ConstantHV_Source.HV_ER_TERMINATOR;
	
	Queue<String> msgQueue = new LinkedList<>(); 
	
	String LastReadMessage = "";
	
	String lastMessage="";


	Boolean ExpectedResponseRecieved = false;
	Boolean ErrorResponseRecieved = false;
	Boolean UnknownResponseRecieved = false;
	Boolean responseRecieved = false;
	//Object productType = new Object();
	final int  defaultResponseWaitCount = 20;
	int responseWaitCount = defaultResponseWaitCount;
	Communicator serialPortObj= null;
	

	public SerialRxMessageQ(Communicator inpSerialPortObj){
		serialPortObj = inpSerialPortObj;

	}
	
	public Communicator getSerialPortObj() {
		return serialPortObj;
	}
	
	public void resetResponseWaitCount() {
		this.responseWaitCount = defaultResponseWaitCount;
	}
	
	public void setExpectedResponseRecieved(Boolean expectedResponseRecieved) {
		this.ExpectedResponseRecieved = expectedResponseRecieved;
	}

	public void setUnknownResponseRecieved(Boolean unKnownresponseRecieved) {
		this.UnknownResponseRecieved = unKnownresponseRecieved;
	}
	
	public void setResponseRecieved(Boolean responseRecieved) {
		this.responseRecieved = responseRecieved;
	}

	public void setErrorResponseRecieved(Boolean errorResponseRecieved) {
		this.ErrorResponseRecieved = errorResponseRecieved;
	}
	public Boolean isExpectedErrorResponseReceived(){
		return this.ErrorResponseRecieved;

	}
	
	/*public Boolean IsExpectedErrorResponseReceived(){
		ApplicationLauncher.logger.debug("IsExpectedErrorResponseReceived: Entry");
		resetResponseWaitCount();
		while((!ErrorResponseRecieved)&&(responseWaitCount!=0)){
			try {
				Thread.sleep(250);
				ProcessRxMsg();
				responseWaitCount--;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("IsExpectedErrorResponseReceived :Exception:"+ e.getMessage());
			}
		};
		ApplicationLauncher.logger.debug("IsExpectedErrorResponseReceived: Exit");
		return this.ErrorResponseRecieved;
	}*/
	
	
	public Boolean IsResponseReceived(){
		ApplicationLauncher.logger.debug("IsResponseReceived: Entry");
		resetResponseWaitCount();
		while((!responseRecieved)&&(responseWaitCount!=0)){
			//ApplicationLauncher.logger.debug("IsResponseReceived: responseWaitCount: " + responseWaitCount);
			//ApplicationLauncher.logger.debug("IsResponseReceived: responseRecieved: " + responseRecieved);
			try {
				Thread.sleep(250);
				//ApplicationLauncher.logger.debug("IsResponseReceived: test1");
				ProcessRxMsg();
				responseWaitCount--;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("IsResponseReceived :Exception:"+ e.getMessage());
			}
		};
		ApplicationLauncher.logger.debug("IsResponseReceived: Exit");
		return this.responseRecieved;
	}
	
	public String getLastReadMessage() {
		//return this.LastReadMessage;
		if(msgQueue.peek()!=null) {
			return msgQueue.peek();
		}else {
			return lastMessage;//"";dsgfd
		}
	}
	
	public void clearLastMessage() {
		lastMessage = "";
	}
	
	public void clearMsgQueue() {
		//return this.LastReadMessage;
		ApplicationLauncher.logger.info("clearMsgQueue: Entry");
		while(msgQueue.peek()!=null) {
			ApplicationLauncher.logger.info("clearMsgQueue: removing msg:" + msgQueue.peek());
			msgQueue.poll();
		}
		ApplicationLauncher.logger.info("clearMsgQueue: Cleared All msgQueue");
	}


	public void setLastReadMessage(String lastReadMessage) {
		this.LastReadMessage = lastReadMessage;
	}
	
	public void clearLastReadMessage() {
		this.LastReadMessage = "";
	}
	
	public Boolean isExpectedResponseReceived(){
		
		return this.ExpectedResponseRecieved;
	}
/*
	public Boolean IsExpectedResponseReceived(){
		ApplicationLauncher.logger.debug("IsExpectedResponseReceived: Entry");
		resetResponseWaitCount();
		while((!ExpectedResponseRecieved)&&(responseWaitCount!=0)){
			try {
				Thread.sleep(250);
				ProcessRxMsg();
				responseWaitCount--;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("SerialRxMessageQ IsExpectedResponseReceived :Exception:"+ e.getMessage());
			}
		};
		ApplicationLauncher.logger.debug("IsExpectedResponseReceived: Exit");
		return this.ExpectedResponseRecieved;
	}*/
	
	/*public Boolean IsUnknownResponseRecieved(){
		ApplicationLauncher.logger.debug("IsUnknownResponseRecieved: Entry");
		resetResponseWaitCount();
		while((!UnknownResponseRecieved)&&(responseWaitCount!=0)){
			try {
				Thread.sleep(250);
				ProcessRxMsg();
				responseWaitCount--;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("SerialRxMessageQ IsUnknownResponseRecieved :Exception:"+ e.getMessage());
			}
		};
		ApplicationLauncher.logger.debug("IsUnknownResponseRecieved: Exit");
		return this.UnknownResponseRecieved;
	}*/
	
	public Boolean isUnknownResponseRecieved(){
		ApplicationLauncher.logger.debug("isUnknownResponseRecieved: Entry");
		return this.UnknownResponseRecieved;
	}
	
	public void addMsgQueue(String inputMessage) {
		
		
		//ApplicationLauncher.logger.info("SerialRxMessageQ: addMsgQueue invoked");
		//ApplicationLauncher.logger.info("SerialRxMessageQ: inputMessage:"+inputMessage);
		//ApplicationLauncher.logger.info("SerialRxMessageQ: inputMessage-String:"+GuiUtils.HexToString(inputMessage));
		msgQueue.add(inputMessage);
		
	}
	
	public void removeProcessedMsgFromQueue() {
		
		
		ApplicationLauncher.logger.info("SerialRxMessageQ: removeProcessedMsgFromQueue invoked");
		//ApplicationLauncher.logger.info("SerialDataMsgHV_Manager: inputMessage:"+inputMessage);
		msgQueue.poll();
		
	}
	
	/*public void addMsgQueue() {
		
		
		ApplicationLauncher.logger.info("SerialDataMsgHV_Manager2: addMsgQueue invoked");
		//msgQueue.add(inputMessage);
		
	}*/
	
	public void ProcessRxMsg() {
		//String lastMessage = msgQueue.poll();
		//ApplicationLauncher.logger.debug("ProcessRxMsg: Entry:");
		lastMessage = msgQueue.peek();
		//ApplicationLauncher.logger.debug("ProcessRxMsg: Entry:lastMessage:"+lastMessage);
		if(lastMessage != null) {
			//ApplicationLauncher.logger.debug("ProcessRxMsg: lastMessage1:"+lastMessage);
			//ApplicationLauncher.logger.debug("ProcessRxMsg: getExpectedResult:"+serialPortObj.getExpectedResult());
			//ApplicationLauncher.logger.debug("ProcessRxMsg: getExpectedResult String:"+GuiUtils.HexToString(serialPortObj.getExpectedResult()));
			if(lastMessage.length() == serialPortObj.getExpectedLength()) {
				if(lastMessage.equals(serialPortObj.getExpectedResult())) {
					ApplicationLauncher.logger.debug("ProcessRxMsg: lastMessage2:"+lastMessage);
					setExpectedResponseRecieved( true);
					setResponseRecieved(true);
				} 
			}else if((lastMessage.startsWith(serialPortObj.getExpectedResult())) && (serialPortObj.getExpectedResult().length()>0)){
				ApplicationLauncher.logger.debug("ProcessRxMsg: lastMessage3:"+lastMessage);
				setExpectedResponseRecieved( true);
				setResponseRecieved(true);
			}else if(!serialPortObj.getExpectedError1Result().isEmpty()) {

				if(lastMessage.contains(serialPortObj.getExpectedError1Result())) {
					ApplicationLauncher.logger.debug("ProcessRxMsg: lastMessage4:"+lastMessage);
					setErrorResponseRecieved(true);
					setResponseRecieved(true);
				}else  if(!serialPortObj.getExpectedError2Result().isEmpty()) {
					if(lastMessage.contains(serialPortObj.getExpectedError2Result())) {
						ApplicationLauncher.logger.debug("ProcessRxMsg: lastMessage5:"+lastMessage);
						setErrorResponseRecieved(true);
						setResponseRecieved(true);
					}else {
						ApplicationLauncher.logger.debug("ProcessRxMsg: Unexpected message0: "+lastMessage);
						setUnknownResponseRecieved(true);
						setResponseRecieved(true);
						//ApplicationLauncher.logger.debug("ProcessRxMsg: Unexpected message dropping the packet1:"+lastMessage);
						//removeProcessedMsgFromQueue();
					}
				}else {
					ApplicationLauncher.logger.debug("ProcessRxMsg: Unexpected message1: "+lastMessage);
					setUnknownResponseRecieved(true);
					setResponseRecieved(true);
					//ApplicationLauncher.logger.debug("ProcessRxMsg: Unexpected message dropping the packet2:"+lastMessage);
					//removeProcessedMsgFromQueue();
				}
			}else  if(!serialPortObj.getExpectedError2Result().isEmpty()) {
				if(lastMessage.contains(serialPortObj.getExpectedError2Result())) {
					ApplicationLauncher.logger.debug("ProcessRxMsg: lastMessage6:"+lastMessage);
					setErrorResponseRecieved(true);
					setResponseRecieved(true);
				}else {
					ApplicationLauncher.logger.debug("ProcessRxMsg: Unexpected message dropping the packet3:"+lastMessage);
					removeProcessedMsgFromQueue();
				}
			}else {
				//ApplicationLauncher.logger.debug("ProcessRxMsg: Unexpected message dropping the packet4:"+lastMessage);
				//removeProcessedMsgFromQueue();
				ApplicationLauncher.logger.debug("ProcessRxMsg: Unexpected message1: "+lastMessage);
				setUnknownResponseRecieved(true);
				setResponseRecieved(true);
			}
		}else {
			//ApplicationLauncher.logger.info("ProcessRxMsg: No Message");
		}
		
	}
	


}

