package com.tasnetwork.calibration.energymeter.serial.portmanagerV2;

import java.util.HashMap;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

public class CommunicatorV2 {

	// private Enumeration ports = null;

	// private static SerialPort [] portsAvailable;
	SerialPort[] portsAvailable;
	private boolean deviceConnected = false;

	HashMap portMap = new HashMap();
	private String expectedResult = "";

	private String expectedError1Result = "";
	private String expectedError2Result = "";
	private int expectedLength = 0;

	private volatile String serialData = "";

	private String deviceType = "";
	private SerialPort selectedSerialPort = null;

	public CommunicatorV2(String serialPortDeviceType) {
		this.deviceType = serialPortDeviceType;
	}

	// public HashMap searchForPorts()
	public SerialPort[] searchForPorts() {
		try {
			// ports = CommPortIdentifier.getPortIdentifiers();
			portsAvailable = SerialPort.getCommPorts();

			for (SerialPort S : portsAvailable) {

				portMap.put(S.getSystemPortName(), S.getSystemPortPath());
			}
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("searchForPorts: exception:" + e.getMessage());
			// WindowManager.InformUser("SerialPort search failed","Serial Port scan failed
			// due to followinf reason\n\n" + e.getMessage(),AlertType.ERROR);
		}
		// return portMap;
		return portsAvailable;
		// ApplicationLauncher.logger.info("test1:"+portMap.get("COM3"));
	}

	public void assignSerialPort(String comPortId) {
		selectedSerialPort = SerialPort.getCommPort(comPortId);

	}

	public void connect(String SerialPort_ID) {
		// String selectedPort = SerialPort_ID;
		// selectedPortIdentifier = (CommPortIdentifier)portMap.get(selectedPort);

		// CommPort commPort = null;

		try {
			/*
			 * selectedPortIdentifier = (CommPortIdentifier)portMap.get(selectedPort);
			 * 
			 * CommPort commPort = null;
			 */
			// the method below returns an object of type CommPort
			// commPort = selectedPortIdentifier.open(selectedPort, TIMEOUT);
			// the CommPort object can be casted to a SerialPort object

			// serialPort = (SerialPort)commPort;

			boolean isPortOpened = selectedSerialPort.openPort();
			if (isPortOpened) {
				setDeviceConnected(true);
				String logText = selectedSerialPort.getSystemPortName() + " opened successfully.";
				ApplicationLauncher.logger.info("SerialCom:" + logText);
			} else {
				String logText = "Failed to open " + selectedSerialPort.getSystemPortName();
				// ApplicationLauncher.logger.error("Communicator: connect: " + e.getMessage());
				ApplicationLauncher.logger.debug("SerialCom:" + logText);
			}

			// for controlling GUI elements

			// logging

			// window.txtLog.setForeground(Color.black);
			// window.txtLog.append(logText + "\n");

			// CODE ON SETTING BAUD RATE ETC OMITTED
			// XBEE PAIR ASSUMED TO HAVE SAME SETTINGS ALREADY

			// enables the controls on the GUI if a successful connection is made
			// window.keybindingController.toggleControls();
		}

		catch (Exception e) {
			String logText = "Failed to open " + selectedSerialPort.getSystemPortName() + "(" + e.toString() + ")";
			ApplicationLauncher.logger.error("Communicator: connect: Exception: " + e.getMessage());
			ApplicationLauncher.logger.info("SerialCom:" + logText);

		}
	}

	public boolean serialPortConfig(int BaudRate) {

		try {
			/*
			 * serialPort.setSerialPortParams(BaudRate, SerialPort.DATABITS_8,
			 * SerialPort.STOPBITS_1,SerialPort.PARITY_EVEN);
			 * return true;
			 */

			// int BaudRate = 9600;
			int DataBits = 8;
			int StopBits = SerialPort.ONE_STOP_BIT;
			int Parity = SerialPort.NO_PARITY;
			// int Parity = SerialPort.EVEN_PARITY;
			selectedSerialPort.setComPortParameters(BaudRate, DataBits, StopBits, Parity);
			return true;

			// } catch (UnsupportedCommOperationException e) {
		} catch (Exception e) {
			String logText = "SerialPortConfig. (" + e.toString() + ")";
			ApplicationLauncher.logger.info("SerialCom:" + logText);
			ApplicationLauncher.logger
					.error("SerialPortConfigV2:  UnsupportedCommOperationException: " + e.getMessage());
			return false;
		}

	}

	public void writeStringMsgToPortV1(String send) {

		try {

			/*
			 * output.write(send.getBytes());
			 * output.flush();
			 */

			if (selectedSerialPort.isOpen()) {

				int bytesTxed = 0;
				// String send = "HelloWorld/!";//+ (byte)0x0B+ (byte)0x65;
				// byte[] iso88591Data = send.getBytes("ISO-8859-1");

				// bytesTxed = selectedSerialPort.writeBytes(send.getBytes(),
				// send.getBytes().length);
				// bytesTxed = selectedSerialPort.writeBytes(send.getBytes("ISO-8859-1"),
				// send.getBytes().length);
				// bytesTxed = selectedSerialPort.writeBytes(send.getBytes("UTF-8"),
				// send.getBytes().length);
				// bytesTxed = selectedSerialPort.writeBytes(send.getBytes("UTF-16"),
				// send.getBytes().length);

				byte[] iso88591Data = send.getBytes("ISO-8859-1");
				bytesTxed = selectedSerialPort.writeBytes(iso88591Data, iso88591Data.length);

				// ApplicationLauncher.logger.info(" Bytes Transmitted -> " + bytesTxed );
			} else {
				ApplicationLauncher.logger
						.info(" writeStringMsgToPort: " + selectedSerialPort.getSystemPortName() + " not open");
			}
		} catch (Exception e) {

			ApplicationLauncher.logger.error("writeStringMsgToPort:  IOException: " + e.getMessage());
			String logText = "Failed to write string I/O. (" + e.toString() + ")";
			// ApplicationLauncher.logger.info("SerialCom:"+getPortDeviceMapping()+":"+serialPort.getName()+":"+logText);
			ApplicationLauncher.logger.info("SerialCom:" + getPortDeviceMapping() + ":"
					+ selectedSerialPort.getSystemPortName() + ":" + logText);
			e.printStackTrace();

		}
	}

	public void writeStringMsgToPort(byte[] send) {

		try {

			/*
			 * output.write(send.getBytes());
			 * output.flush();
			 */

			if (selectedSerialPort.isOpen()) {

				int bytesTxed = 0;

				bytesTxed = selectedSerialPort.writeBytes(send, send.length);
				// ApplicationLauncher.logger.info(" Bytes Transmitted -> " + bytesTxed );
			} else {
				ApplicationLauncher.logger
						.info(" writeStringMsgToPort: " + selectedSerialPort.getSystemPortName() + " not open");
			}
		} catch (Exception e) {

			ApplicationLauncher.logger.error("writeStringMsgToPort:  IOException: " + e.getMessage());
			String logText = "Failed to write string I/O. (" + e.toString() + ")";
			// ApplicationLauncher.logger.info("SerialCom:"+getPortDeviceMapping()+":"+serialPort.getName()+":"+logText);
			ApplicationLauncher.logger.info("SerialCom:" + getPortDeviceMapping() + ":"
					+ selectedSerialPort.getSystemPortName() + ":" + logText);
			e.printStackTrace();

		}
	}

	public void disconnect() {
		// close the serial port
		try {

			selectedSerialPort.removeDataListener();
			selectedSerialPort.closePort();
			/*
			 * serialPort.close();
			 * input.close();
			 * output.close();
			 */
			setDeviceConnected(false);

			String logText = "Disconnected.";
			ApplicationLauncher.logger.info("ComV2 : SerialCom:" + getPortDeviceMapping() + ":"
					+ selectedSerialPort.getSystemPortName() + ":" + logText);
			// ApplicationLauncher.logger.info("disconnect: SerialCom:
			// "+selectedSerialPort.getSystemPortName());

		} catch (Exception e) {
			String logText = "Failed to close " + selectedSerialPort.getSystemPortName() + "(" + e.toString() + ")";
			ApplicationLauncher.logger.info("ComV2 : SerialCom:" + logText);
			ApplicationLauncher.logger.error("disconnect:  Exception: " + e.getMessage());

		}
	}

	public void initListener() {
		try {
			// serialPort.addEventListener(this);
			// serialPort.notifyOnDataAvailable(true);

			selectedSerialPort.addDataListener(new SerialPortDataListener() {
				@Override
				public int getListeningEvents() {
					return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
				}

				@Override
				public void serialEvent(SerialPortEvent event) {
					SerialPort comPort = event.getSerialPort();
					// System.out.println("Available: " + comPort.bytesAvailable() + " bytes.");
					byte[] newData = new byte[comPort.bytesAvailable()];
					int numRead = comPort.readBytes(newData, newData.length);
					// String HexData = String.format("%02X",newData);
					// ApplicationLauncher.logger.debug("initListener: Received the following
					// message HexData: " + HexData);
					// String str1 = new String(newData);
					// str1 = new String(HexData);
					// serialData= serialData+str1;
					// String str1 = GuiUtils.hexToAsciiV2(bytesToHex(newData));

					// String str1 = GuiUtils.hexToAsciiV2(new BigInteger(1, newData).toString(16));
					/*
					 * BigInteger n = new BigInteger(newData);
					 * String hexa = n.toString(16);
					 * String str1 = GuiUtils.hexToAsciiV2(hexa);
					 */
					// String str1 = GuiUtils.hexToAsciiV2(GuiUtils.bytesToHex(newData));
					String str1 = GuiUtils.bytesToAscii(newData);

					serialData = serialData + str1;

					// str1=String.format("%02X",HexData);
					// System.out.println("Read " + numRead + " bytes.");

					// ApplicationLauncher.logger.debug("initListener: Received the following
					// message str1: " + str1);
					// ApplicationLauncher.logger.debug("initListener: Received the following
					// message str1-Hex2: " + GuiUtils.asciiToHex(str1));

					// presentData = presentData + str1;
					//
					// try {
					// serialData= serialData+str1.getBytes("ISO-8859-1");
					// } catch (UnsupportedEncodingException e) {

					// e.printStackTrace();
					// }
					// ApplicationLauncher.logger.debug("initListener: Received the following
					// message: " + str1);
					// ApplicationLauncher.logger.debug("initListener: serialData: " + serialData);
				}

			});
		}
		// catch (TooManyListenersException e)
		catch (Exception e) {
			String logText = "Too many listeners. (" + e.toString() + ")";
			ApplicationLauncher.logger.info("SerialCom:" + logText);
			ApplicationLauncher.logger.error("initListener:  TooManyListenersException: " + e.getMessage());

		}

	}

	public String getExpectedResult() {
		return expectedResult;
	}

	public void setExpectedResult(String expectedResult) {
		this.expectedResult = expectedResult;
	}

	public String getExpectedError1Result() {
		return expectedError1Result;
	}

	public void setExpectedError1Result(String expectedError1Result1) {
		this.expectedError1Result = expectedError1Result1;
	}

	public String getExpectedError2Result() {
		return expectedError2Result;
	}

	public void setExpectedError2Result(String expectedError2Resultinp) {
		this.expectedError2Result = expectedError2Resultinp;
	}

	public String getPortDeviceMapping() {
		return this.deviceType;
	}

	public int getExpectedLength() {
		return expectedLength;
	}

	public void setExpectedLength(int expectedLength) {
		this.expectedLength = expectedLength;
	}

	public String getSerialData() {
		return serialData;
	}

	public void setSerialData(String serialData) {
		this.serialData = serialData;
	}

	public void clearSerialData() {
		ApplicationLauncher.logger.debug("ClearSerialData :Entry");
		this.serialData = "";
	}

	public void stripLength(Integer LengthTobeDeleted) {
		// ApplicationLauncher.logger.debug("StripLength: LengthTobeDeleted: " +
		// LengthTobeDeleted);
		try {
			serialData = serialData.substring(LengthTobeDeleted);
		} catch (Exception e) {
			// ApplicationLauncher.logger.info("StripLength: Exception" + e.toString());
			e.printStackTrace();
			ApplicationLauncher.logger.error("StripLength:  Exception: " + e.getMessage());
		}
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public boolean isDeviceConnected() {
		return deviceConnected;
	}

	public void setDeviceConnected(boolean deviceConnected) {
		this.deviceConnected = deviceConnected;
	}

	public void setFlowControlMode() {

		try {
			selectedSerialPort.setFlowControl(
					SerialPort.FLOW_CONTROL_DISABLED

			// SerialPort.FLOW_CONTROL_XONXOFF_IN_ENABLED
			// SerialPort.ODD_PARITY
			// SerialPort.EVEN_PARITY
			// SerialPort.NO_PARITY
			// SerialPort.FLOW_CONTROL_XONXOFF_OUT_ENABLED
			);

		} catch (Exception ex) {
			ApplicationLauncher.logger.debug("setFlowControlMode: " + ex.getMessage());
			ApplicationLauncher.logger.error("setFlowControlMode:  Exception: " + ex.getMessage());
		}
	}

	public boolean isDevicePortExist(String inputPortId) {

		for (SerialPort S : portsAvailable) {
			/*
			 * System.out.
			 * println("Port Number                                -> .getSystemPortName()     -> "
			 * + S.getSystemPortName()); //Gives the number of com port,Eg COM9
			 * System.out.
			 * println("Port Physical Location (OS)                -> .getSystemPortPath()     -> "
			 * + S.getSystemPortPath());
			 * System.out.
			 * println("Port Physical Location (USB hub)           -> .getSystemPortLocation() -> "
			 * + S.getPortLocation());
			 * System.out.
			 * println("Port Description as reported by the device -> .getDescriptivePortName()-> "
			 * + S.getDescriptivePortName());
			 * System.out.
			 * println("Port Description  .toString()              ->  .toString()             -> "
			 * + S.toString() + "\n");
			 * portMap.put(S.getSystemPortName(), S.getSystemPortPath());
			 */

			if (S.getSystemPortName().equals(inputPortId)) {
				ApplicationLauncher.logger.debug("isDevicePortExist: Serial port device found success: " + inputPortId);
				return true;
			}
		}

		ApplicationLauncher.logger.debug("isDevicePortExist: Serial port search failed for : " + inputPortId);
		return false;
	}

	public void setDevicePortExist(boolean devicePortExist) {
	}

	public SerialPort[] getPortsAvailable() {
		return portsAvailable;
	}

	public void setPortsAvailable(SerialPort[] portsAvailable) {
		this.portsAvailable = portsAvailable;
	}
}
