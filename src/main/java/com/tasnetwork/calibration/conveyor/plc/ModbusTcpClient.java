package com.tasnetwork.calibration.conveyor.plc;

import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.ft.Ft;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.WindowManager;
import com.tasnetwork.calibration.energymeter.util.ErrorCodeMapping;

import de.re.easymodbus.exceptions.ModbusException;
import de.re.easymodbus.modbusclient.ModbusClient;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

public class ModbusTcpClient {
	public static Logger logger = Logger.getLogger(ModbusTcpClient.class.getPackage().getName());
	Timer plcReadTimer;

	private ModbusClient modbusClient = new ModbusClient();
	private String ipAddress = "192.168.0.10";
	private String port = "502";
	private String comStatus = "";
	private String readPlcErrorCode = "";
	private String pistonWaitTimeInSec = "";
	private String meterMaxCurrent = "";
	private String actualSourceCurrent = "";
	private boolean plcCommSuccess = false;
	private volatile boolean plcReadDataContinous = false;
	private int dataRefreshTimeInMsec = 1000;
	private volatile boolean plcSemLock = false;

	private boolean readStartSuccess = false;
	private boolean readStartFailed = false;
	private boolean readStopSuccess = false;
	private boolean readStopFailed = false;
	private boolean readOverCurrent = false;
	private volatile boolean readFaultOccured = false;

	public String getIpAddress() {
		return ipAddress;
	}

	public String getPort() {
		return port;
	}

	public String getComStatus() {
		return comStatus;
	}

	public String getReadPlcErrorCode() {
		return readPlcErrorCode;
	}

	public String getPistonWaitTimeInSec() {
		return pistonWaitTimeInSec;
	}

	public String getMeterMaxCurrent() {
		return meterMaxCurrent;
	}

	public String getActualSourceCurrent() {
		return actualSourceCurrent;
	}

	public boolean isReadStartSuccess() {
		return readStartSuccess;
	}

	public boolean isReadFaultOccured() {
		return readFaultOccured;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setComStatus(String comStatus) {
		this.comStatus = comStatus;
	}

	public void setReadPlcErrorCode(String readPlcErrorCode) {
		this.readPlcErrorCode = readPlcErrorCode;
	}

	public void setReadStartSuccess(boolean readStartSuccess) {
		this.readStartSuccess = readStartSuccess;
	}

	public void setReadStopSuccess(boolean readStopSuccess) {
		this.readStopSuccess = readStopSuccess;
	}

	public void setReadFaultOccured(boolean readFaultOccured) {
		this.readFaultOccured = readFaultOccured;
	}

	public ModbusClient getModbusClient() {
		return modbusClient;
	}

	public void setModbusClient(ModbusClient modbusClient) {
		this.modbusClient = modbusClient;
	}

	public boolean modbusConnect() throws java.net.UnknownHostException, java.io.IOException {

		ModbusTcpClient.logger.debug("modbusConnect1 : Entry");

		boolean status = false;
		// ModbusClient modbusClient = new ModbusClient(address,port);
		try {
			String address = getIpAddress();
			int port = Integer.parseInt(getPort());
			ModbusTcpClient.logger.debug("modbusConnect : address: " + address);
			ModbusTcpClient.logger.debug("modbusConnect : port: " + port);
			ModbusClient modbus = new ModbusClient(address, port);

			modbusClient = modbus;
			modbusClient.setUnitIdentifier((byte) 1);
			// modbusClient.setConnectionTimeout(5);
			modbusClient.Connect();
			status = true;
			setPlcCommSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			ModbusTcpClient.logger.error("modbusConnect: Exception: " + e.getMessage());

			setPlcCommSuccess(false);

		}

		return status;
	}

	public boolean modbusConnect(String address, int port) throws java.net.UnknownHostException, java.io.IOException {
		ModbusTcpClient.logger.debug("modbusConnect : Entry");
		boolean status = false;
		// ModbusClient modbusClient = new ModbusClient(address,port);
		try {
			ModbusTcpClient.logger.debug("modbusConnect : address: " + address);
			ModbusTcpClient.logger.debug("modbusConnect : port: " + port);
			setIpAddress(address);
			setPort(String.valueOf(port));
			ModbusClient modbus = new ModbusClient(address, port);

			modbusClient = modbus;

			modbusClient.Connect();
			ModbusTcpClient.logger.debug("modbusConnect : address: " + modbusClient.getipAddress());
			status = true;
			setPlcCommSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			ModbusTcpClient.logger.error("modbusConnect: Exception: " + e.getMessage());
		}

		return status;
	}

	public void modbusDisconnect() throws java.net.UnknownHostException, java.io.IOException {

		ModbusTcpClient.logger.debug("modbusDisconnect : Entry");
		// ModbusClient modbusClient = new ModbusClient(address,port);
		try {

			if (!modbusClient.isConnected()) {
				setPlcCommSuccess(false);
			} else {
				ModbusTcpClient.logger.debug("modbusDisconnect : Disconnecting...");
				modbusClient.Disconnect();

				ModbusTcpClient.logger.debug("modbusDisconnect : Disconnected");
			}

		} catch (Exception e) {
			e.printStackTrace();
			ModbusTcpClient.logger.error("modbusDisconnect: Exception: " + e.getMessage());
		}
	}

	public boolean modbusConnectionStatus() throws java.net.UnknownHostException, java.io.IOException {

		ModbusTcpClient.logger.debug("modbusConnectionStatus : Entry");
		boolean status = false;
		try {

			if (modbusClient.isConnected()) {
				ModbusTcpClient.logger.debug("modbusConnectionStatus : connected");
				setPlcCommSuccess(true);
				status = true;
			} else {
				ModbusTcpClient.logger.debug("modbusConnectionStatus : Disconnected");
				setPlcCommSuccess(false);
			}

		} catch (Exception e) {
			e.printStackTrace();
			ModbusTcpClient.logger.error("modbusConnectionStatus: Exception: " + e.getMessage());
		}

		return status;
	}

	public boolean modbusReadPlcData()
			throws ModbusException, java.net.UnknownHostException, java.net.SocketException, java.io.IOException {

		// ModbusTcpClient.logger.debug("modbusReadPlcData : Entry" );
		boolean status = false;
		try {
			if (!modbusClient.isConnected()) {
				ModbusTcpClient.logger.debug("modbusReadPlcData : connecting..");
				if (modbusConnect()) {
					setPlcCommSuccess(true);
				}

			}
			if (modbusClient.isConnected()) {

				ModbusTcpClient.logger.debug("Start Cmd requested:  " + modbusClient.ReadCoils(0, 1)[0]);
				ModbusTcpClient.logger.debug("Start Cmd Ack: " + modbusClient.ReadCoils(14, 1)[0]);
				ModbusTcpClient.logger.debug("Meter Limit Switch: " + modbusClient.ReadCoils(1, 1)[0]);
				ModbusTcpClient.logger.debug("Emgency Stop Ok: " + modbusClient.ReadCoils(2, 1)[0]);

				ModbusTcpClient.logger.debug("Actuator Open Complete: " + modbusClient.ReadCoils(3, 1)[0]);
				ModbusTcpClient.logger.debug("not under maintenance: " + modbusClient.ReadCoils(4, 1)[0]);
				ModbusTcpClient.logger.debug("Saftey Gaurd Ok : " + modbusClient.ReadCoils(5, 1)[0]);
				ModbusTcpClient.logger.debug("Fault Occured: " + modbusClient.ReadCoils(10, 1)[0]);
				ModbusTcpClient.logger.debug("Fault reset requested: " + modbusClient.ReadCoils(11, 1)[0]);
				ModbusTcpClient.logger.debug("Actuator Closed Complete: " + modbusClient.ReadCoils(12, 1)[0]);
				ModbusTcpClient.logger.debug("Stop Cmd Requested: " + modbusClient.ReadCoils(15, 1)[0]);
				ModbusTcpClient.logger.debug("Stop Cmd Ack: " + modbusClient.ReadCoils(16, 1)[0]);
				ModbusTcpClient.logger.debug("Start Timer - On Complete Fail: " + modbusClient.ReadCoils(18, 1)[0]);
				ModbusTcpClient.logger.debug("Stop Timer - Off Complete Fail: " + modbusClient.ReadCoils(19, 1)[0]);

				int numberOfBitsToRead = 1;

				status = true;

			} else {
				ModbusTcpClient.logger.debug("modbusReadPlcData: comm failed: ");
			}

		} catch (Exception e) {
			e.printStackTrace();
			ModbusTcpClient.logger.error("modbusReadPlcData: Exception: " + e.getMessage());
		}

		return status;
	}

	public boolean isPlcCommSuccess() {
		return plcCommSuccess;
	}

	public void setPlcCommSuccess(boolean plcConnected) {
		this.plcCommSuccess = plcConnected;
	}

	public static void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {

			e.printStackTrace();
			ModbusTcpClient.logger.error("Sleep :InterruptedException:" + e.getMessage());
		}

	}

	public boolean modbusReadPlcFaultOccured()
			throws ModbusException, java.net.UnknownHostException, java.net.SocketException, java.io.IOException {

		// ModbusTcpClient.logger.debug("modbusReadPlcFaultOccured : Entry" );
		boolean status = false;
		try {
			if (!modbusClient.isConnected()) {
				ModbusTcpClient.logger.debug("modbusReadPlcFaultOccured : connecting..");
				if (modbusConnect()) {
					setPlcCommSuccess(true);
				}

			}
			if (modbusClient.isConnected()) {

				// ModbusTcpClient.logger.debug("modbusReadPlcFaultOccured: Fault Occured:
				// "+modbusClient.ReadCoils(10, 1)[0]);

				int numberOfBitsToRead = 1;
				boolean value = false;
				boolean faultOccured = false;
				// faultOccured =
				// modbusClient.ReadCoils(ConstantModBusPlc.ADDRESS_BIT_FAULT_OCCURED_STATUS,
				// numberOfBitsToRead )[0];
				// faultOccured = ! value;
				setReadFaultOccured(faultOccured);
				status = true;

			} else {
				ModbusTcpClient.logger.debug("modbusReadPlcFaultOccured: comm failed: ");
			}

		} catch (Exception e) {
			e.printStackTrace();
			ModbusTcpClient.logger.error("modbusReadPlcFaultOccured: Exception: " + e.getMessage());
		}

		return status;
	}

	public boolean isPlcReadDataContinous() {
		return plcReadDataContinous;
	}

	public void setPlcReadDataContinous(boolean plcReadDataContinous) {
		this.plcReadDataContinous = plcReadDataContinous;
	}

	public int getDataRefreshTimeInMsec() {
		return dataRefreshTimeInMsec;
	}

	public void setDataRefreshTimeInMsec(int dataRefreshTimeInMsec) {
		this.dataRefreshTimeInMsec = dataRefreshTimeInMsec;
	}

	public void modbusPlcMonitorFaultTrigger() {
		ModbusTcpClient.logger.debug("modbusPlcMonitorFaultTrigger :Entry");
		plcSemLock = true;
		plcReadTimer = new Timer();
		// setTimeExtendedForTimeBased(false);
		// ModbusTcpClient.logger.info("modbusPlcMonitorFaultTrigger
		// :getSerialLDU_ComRefreshTimeInMsec:" + getSerialLDU_ComRefreshTimeInMsec());
		plcReadTimer.schedule(new modbusPlcMonitorFaultTask(), getDataRefreshTimeInMsec());// 1000);
		ModbusTcpClient.logger.debug("modbusPlcMonitorFaultTrigger : isPlcConnected" + isPlcCommSuccess());
		setPlcReadDataContinous(false);
		if (isPlcCommSuccess()) {
			setPlcReadDataContinous(true);
			ModbusTcpClient.logger.debug("modbusPlcMonitorFaultTrigger : setting PlcReadDataContinous ");
		}
		ModbusTcpClient.logger.debug("modbusPlcMonitorFaultTrigger: Exit:");

	}

	class modbusPlcMonitorFaultTask extends TimerTask {
		public void run() {

			// int MaximumNumberOfDeviceConnected =
			// ProjectExecutionController.getListOfDevices().length();
			// ModbusTcpClient.logger.debug("MaximumNumberOfDeviceConnected: " +
			// MaximumNumberOfDeviceConnected);
			ModbusTcpClient.logger.debug("modbusPlcMonitorFaultTask: Entry ");
			// ModbusTcpClient.logger.debug("modbusPlcMonitorFaultTask:
			// isPlcReadDataContinous: " + isPlcReadDataContinous());
			// ModbusTcpClient.logger.debug("modbusPlcMonitorFaultTask: isPlcConnected: " +
			// isPlcCommSuccess());
			if (isPlcReadDataContinous() && isPlcCommSuccess()) {
				// ModbusTcpClient.logger.debug("modbusPlcMonitorFaultTask:
				// isPlcReadDataContinous True entry");
				if (plcSemLock) {
					// ModbusTcpClient.logger.debug("modbusPlcMonitorFaultTask: plcSemLock True
					// entry");
					plcSemLock = false;
					try {

						monitorPlcFaultOccurance();

					} catch (Exception e) {

						e.printStackTrace();
						ModbusTcpClient.logger.error("modbusPlcMonitorFaultTask: Exception:" + e.getMessage());
					}
					plcSemLock = true;
				}
				if (isPlcCommSuccess()) {
					try {

						if (!BayUtils.isUserAborted()) {
							if (isPlcReadDataContinous()) {
								// ModbusTcpClient.logger.debug("modbusPlcMonitorFaultTask: validating device to
								// be read");
								// if(DisplayDataObj.getDevicesToBeRead().size() !=0){
								// ModbusTcpClient.logger.debug("modbusPlcMonitorFaultTask:Scheduling task
								// again");
								plcReadTimer.schedule(new modbusPlcMonitorFaultTask(), getDataRefreshTimeInMsec());
								// }/*else{
								// ModbusTcpClient.logger.debug("modbusPlcMonitorFaultTask: send refresh
								// command");
								// lscsLDU_SendRefreshDataCommand();

								// }*/
							} else {
								ModbusTcpClient.logger.debug("modbusPlcMonitorFaultTask:Timer ExitY1 !%n");
								plcReadTimer.cancel(); // Terminate the timer thread
							}

						} else {
							ModbusTcpClient.logger.debug("modbusPlcMonitorFaultTask:Timer Exit3 !%n");
							plcReadTimer.cancel(); // Terminate the timer thread
						}
						// }

					} catch (Exception e) {
						e.printStackTrace();
						ModbusTcpClient.logger.error("modbusPlcMonitorFaultTask: Exception:" + e.getMessage());
						ModbusTcpClient.logger.info("modbusPlcMonitorFaultTask: plcReadTimer already Cancelled");

					}
				}

			} else {

				ModbusTcpClient.logger.debug("modbusPlcMonitorFaultTask:Timer Exit2 !%n");
				plcReadTimer.cancel(); // Terminate the timer thread

			}
		}
	}

	public boolean monitorPlcFaultOccurance() {
		// ModbusTcpClient.logger.debug("monitorPlcFaultOccurance :Entry");
		// ApplicationHomeController.update_left_status("Reading LDU
		// ErrorData",ConstantApp.LEFT_STATUS_DEBUG);
		boolean faultOccured = false;
		boolean status = false;
		try {
			status = modbusReadPlcFaultOccured();
			if (status) {
				if (isReadFaultOccured()) {
					// ModbusTcpClient.logger.debug("monitorPlcFaultOccurance :Fault occured");
					modbusReadPlcData();
					setPlcReadDataContinous(false);
					// ModbusTcpClient.logger.debug("monitorPlcFaultOccurance : setting
					// UserAbortedFlag");
					BayUtils.setUserAborted(true);
					// ModbusTcpClient.logger.info("monitorPlcFaultOccurance: user prompted:
					// Closure-Success , Project closed succesfully");
					String plcFaultReason = fetchPlcFaultReason();
					ModbusTcpClient.logger.info("monitorPlcFaultOccurance: Plc fault occured:"
							+ "Execution aborted!. Plc failed due to below reason: \n" + plcFaultReason
							+ " : prompted!");
					WindowManager.InformUser("Plc fault occured",
							"Execution aborted!. Plc failed due to below reason: \n" + plcFaultReason, AlertType.ERROR);

					// ModbusTcpClient.InformUser("PLC-Fault Occured","Aborting execution due to
					// fault occurance in PLC!", AlertType.ERROR);
				}
			}
		} catch (Exception E) {
			E.printStackTrace();
			ModbusTcpClient.logger.error("monitorPlcFaultOccurance Exception :" + E.getMessage());
		}

		return faultOccured;
	}

	public String fetchPlcFaultReason() {
		String failureReason = "";

		ModbusTcpClient.logger.info("fetchPlcFaultReason: FaultOccured: " + isReadFaultOccured());
		if (isReadFaultOccured()) {

		} else {
			failureReason = "No Fault Occured or fault cleared or reset now";
		}

		return failureReason;
	}

	// public static boolean modbusTcpSendWriteCoilCmd(int writeAddress,boolean
	// writeData) throws ModbusException, java.net.UnknownHostException,
	// java.net.SocketException, java.io.IOException{
	public boolean modbusTcpSendWriteCoilCmd(int writeAddress, boolean writeData) {

		// ModbusTcpClient.logger.debug("modbusTcpSendWriteCoilCmd : Entry" );
		boolean status = false;
		// boolean toBeStarted = true;
		try {
			if (!modbusClient.isConnected()) {
				ModbusTcpClient.logger.debug("modbusTcpSendWriteCoilCmd : connecting..");
				if (modbusConnect()) {
					setPlcCommSuccess(true);
				}

			}
			if (modbusClient.isConnected()) {
				// if(toBeStarted){

				modbusClient.WriteSingleCoil(writeAddress, writeData);// start Ack reset
				Sleep(100);
				status = true;

			} else {
				ModbusTcpClient.logger.debug("modbusTcpSendWriteCoilCmd: comm failed: ");
			}

		} catch (Exception e) {
			e.printStackTrace();
			ModbusTcpClient.logger
					.error("modbusTcpSendWriteCoilCmd: Exception: Address: " + writeAddress + " : " + e.getMessage());
			// Attempt to reconnect and retry once
			try {
				ModbusTcpClient.logger.debug("modbusTcpSendWriteCoilCmd: Attempting reconnection after exception...");

				try {
					modbusClient.Disconnect();
				} catch (Exception ignore) {
					// optional: log disconnect failure
				}

				if (modbusConnect()) {
					setPlcCommSuccess(true);
					modbusClient.WriteSingleCoil(writeAddress, writeData);
					Sleep(100);
					status = true;
					ModbusTcpClient.logger.debug("modbusTcpSendWriteCoilCmd: Retry after reconnection successful.");
				} else {
					ModbusTcpClient.logger.error("modbusTcpSendWriteCoilCmd: Reconnection failed.");
				}

			} catch (Exception retryEx) {
				ModbusTcpClient.logger.error("modbusTcpSendWriteCoilCmd: Retry after reconnection failed: Address: "
						+ writeAddress + " : " + retryEx.getMessage());
				retryEx.printStackTrace();
			}
		}
		return status;
	}

	public BayResponse modbusTcpSendReadCoilCmd(int readAddress) {

		// ModbusTcpClient.logger.debug("modbusTcpSendReadCoilCmd : Entry" );
		// boolean status = false;
		// boolean toBeStarted = true;
		BayResponse bayResponse = new BayResponse();
		try {
			if (!modbusClient.isConnected()) {
				ModbusTcpClient.logger.debug("modbusTcpSendReadCoilCmd : connecting..");
				if (modbusConnect()) {
					setPlcCommSuccess(true);
				}

			}
			if (modbusClient.isConnected()) {
				// if(toBeStarted){
				int numberOfBitsToRead = 1;
				// modbusClient.ReadCoils(ConstantModBusPlc.ADDRESS_BIT_START_CMD_ACK_STATUS,
				// 1)[0];
				boolean value = modbusClient.ReadCoils(readAddress, numberOfBitsToRead)[0];// start Ack reset
				Sleep(100);
				// status = true;
				bayResponse.setResponseBooleanData(value);
				if (value) {

					// bayResponse.setResponseData(Constant_IO_ActionMapping.OLD_ON_NEW_OFF);
					bayResponse.setResponseData(Constant_IO_ActionMapping.ON);
				} else {
					// bayResponse.setResponseData(Constant_IO_ActionMapping.OLD_OFF_NEW_ON);
					bayResponse.setResponseData(Constant_IO_ActionMapping.OFF);
				}
				bayResponse.setStatus(true);

			} else {
				ModbusTcpClient.logger.debug("modbusTcpSendReadCoilCmd: comm failed: ");
			}

		} catch (Exception e) {
			e.printStackTrace();
			ModbusTcpClient.logger
					.error("modbusTcpSendReadCoilCmd: Exception: Address: " + readAddress + " : " + e.getMessage());

			// Attempt to reconnect and retry once
			try {
				ModbusTcpClient.logger.debug("modbusTcpSendReadCoilCmd: Attempting reconnection after exception...");

				try {
					modbusClient.Disconnect();
				} catch (Exception ignore) {
					// optional: log disconnect failure
				}

				if (modbusConnect()) {
					int numberOfBitsToRead = 1;
					// modbusClient.ReadCoils(ConstantModBusPlc.ADDRESS_BIT_START_CMD_ACK_STATUS,
					// 1)[0];
					boolean value = modbusClient.ReadCoils(readAddress, numberOfBitsToRead)[0];// start Ack reset
					Sleep(100);
					// status = true;
					bayResponse.setResponseBooleanData(value);
					if (value) {

						// bayResponse.setResponseData(Constant_IO_ActionMapping.OLD_ON_NEW_OFF);
						bayResponse.setResponseData(Constant_IO_ActionMapping.ON);
					} else {
						// bayResponse.setResponseData(Constant_IO_ActionMapping.OLD_OFF_NEW_ON);
						bayResponse.setResponseData(Constant_IO_ActionMapping.OFF);
					}
					bayResponse.setStatus(true);
					ModbusTcpClient.logger.debug("modbusTcpSendReadCoilCmd: Retry after reconnection successful.");
				} else {
					ModbusTcpClient.logger.error("modbusTcpSendReadCoilCmd: Reconnection failed.");
				}

			} catch (Exception retryEx) {
				ModbusTcpClient.logger.error("modbusTcpSendReadCoilCmd: Retry after reconnection failed: Address: "
						+ readAddress + " : " + retryEx.getMessage());
				retryEx.printStackTrace();
			}

		}
		return bayResponse;
	}

	public BayResponse modbusTcpSendReadHoldingRegistersCmd(int readAddress) {

		ModbusTcpClient.logger.debug("modbusTcpSendReadHoldingRegistersCmd : Entry");
		// boolean status = false;
		// boolean toBeStarted = true;
		BayResponse bayResponse = new BayResponse();
		try {
			if (!modbusClient.isConnected()) {
				ModbusTcpClient.logger.debug("modbusTcpSendReadHoldingRegistersCmd : connecting..");
				if (modbusConnect()) {
					setPlcCommSuccess(true);
				}

			}
			if (modbusClient.isConnected()) {
				// if(toBeStarted){
				int numberOfDataToBeRead = 1;
				Integer value = modbusClient.ReadHoldingRegisters(readAddress, numberOfDataToBeRead)[0];
				int unsignedValue = value & 0xFFFF;
				Sleep(100);
				// status = true;
				bayResponse.setResponseData(String.valueOf(unsignedValue));
				bayResponse.setStatus(true);

			} else {
				ModbusTcpClient.logger.debug("modbusTcpSendReadHoldingRegistersCmd: comm failed: ");
			}

		} catch (Exception e) {
			e.printStackTrace();
			ModbusTcpClient.logger.error("modbusTcpSendReadHoldingRegistersCmd: Exception: " + e.getMessage());

			// Attempt to reconnect and retry once
			try {
				ModbusTcpClient.logger
						.debug("modbusTcpSendReadHoldingRegistersCmd: Attempting reconnection after exception...");

				try {
					modbusClient.Disconnect();
				} catch (Exception ignore) {
					// optional: log disconnect failure
				}

				if (modbusConnect()) {
					int numberOfDataToBeRead = 1;
					Integer value = modbusClient.ReadHoldingRegisters(readAddress, numberOfDataToBeRead)[0];
					int unsignedValue = value & 0xFFFF;
					Sleep(100);
					// status = true;
					bayResponse.setResponseData(String.valueOf(unsignedValue));
					bayResponse.setStatus(true);
					ModbusTcpClient.logger
							.debug("modbusTcpSendReadHoldingRegistersCmd: Retry after reconnection successful.");
				} else {
					ModbusTcpClient.logger.error("modbusTcpSendReadHoldingRegistersCmd: Reconnection failed.");
				}

			} catch (Exception retryEx) {
				ModbusTcpClient.logger
						.error("modbusTcpSendReadHoldingRegistersCmd: Retry after reconnection failed: Address: "
								+ readAddress + " : " + retryEx.getMessage());
				retryEx.printStackTrace();
			}
		}
		return bayResponse;
	}

	public BayResponse modbusTcpSendWriteHoldingRegistersCmd(int writeAddress, Integer writeValue) {

		ModbusTcpClient.logger.debug("modbusTcpSendWriteHoldingRegistersCmd : Entry");
		// boolean status = false;
		// boolean toBeStarted = true;
		BayResponse bayResponse = new BayResponse();
		try {
			if (!modbusClient.isConnected()) {
				ModbusTcpClient.logger.debug("modbusTcpSendWriteHoldingRegistersCmd : connecting..");
				if (modbusConnect()) {
					setPlcCommSuccess(true);
				}

			}
			if (modbusClient.isConnected()) {
				modbusClient.WriteSingleRegister(writeAddress, writeValue);// (startingAddressRead, quantityRead,
																			// startingAddressWrite,
																			// values)//.WriteHoldingRegisters(writeAddress,
																			// writeValue);
				Sleep(100);
				// status = true;
				// bayResponse.setResponseData(String.valueOf(value));
				bayResponse.setStatus(true);

			} else {
				ModbusTcpClient.logger.debug("modbusTcpSendWriteHoldingRegistersCmd: comm failed: ");
			}

		} catch (Exception e) {
			e.printStackTrace();
			ModbusTcpClient.logger.error("modbusTcpSendWriteHoldingRegistersCmd: Exception: " + e.getMessage());

			// Attempt to reconnect and retry once
			try {
				ModbusTcpClient.logger
						.debug("modbusTcpSendWriteHoldingRegistersCmd: Attempting reconnection after exception...");

				try {
					modbusClient.Disconnect();
				} catch (Exception ignore) {
					// optional: log disconnect failure
				}

				if (modbusConnect()) {
					modbusClient.WriteSingleRegister(writeAddress, writeValue);// (startingAddressRead, quantityRead,
																				// startingAddressWrite,
																				// values)//.WriteHoldingRegisters(writeAddress,
																				// writeValue);
					Sleep(100);
					// status = true;
					// bayResponse.setResponseData(String.valueOf(value));
					bayResponse.setStatus(true);
					ModbusTcpClient.logger
							.debug("modbusTcpSendWriteHoldingRegistersCmd: Retry after reconnection successful.");
				} else {
					ModbusTcpClient.logger.error("modbusTcpSendWriteHoldingRegistersCmd: Reconnection failed.");
				}

			} catch (Exception retryEx) {
				ModbusTcpClient.logger
						.error("modbusTcpSendWriteHoldingRegistersCmd: Retry after reconnection failed: Address: "
								+ writeAddress + " : " + retryEx.getMessage());
				retryEx.printStackTrace();
			}

		}
		return bayResponse;
	}

}
