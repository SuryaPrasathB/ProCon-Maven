/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tasnetwork.calibration.energymeter.device;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.TooManyListenersException;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceMte;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

public class Communicator implements SerialPortEventListener {
    // passed from main GUI
    // GUI window = null;

    // for containing the ports that will be found
    private Enumeration ports = null;
    // map the port names to CommPortIdentifiers
    public static HashMap portMap = new HashMap();

    // this is the object that contains the opened port
    private CommPortIdentifier selectedPortIdentifier = null;
    private SerialPort serialPort = null;

    private InputStream input = null;
    private OutputStream output = null;

    // just a boolean flag that i use for enabling
    // and disabling buttons depending on whether the program
    // is connected to a serial port or not
    private boolean bPowerSourceDeviceConnected = false;
    private boolean bRefStandardDeviceConnected = false;
    private boolean bDeviceConnected = false;
    private boolean ReadFormatInHex = false;

    // the timeout value for connecting with the port
    final static int TIMEOUT = 2000;

    // some ascii values for for certain things
    final static int SPACE_ASCII = 32;
    final static int DASH_ASCII = 45;
    final static int NEW_LINE_ASCII = 10;

    // a string for recording what goes on in the program
    // this string is written to the GUI
    String logText = "";
    // private Main window;

    private String DeviceType = "";
    private String ExpectedResult = "";
    private String ExpectedDataErrorResult = "";
    private String ExpectedSetErrorResult = "";
    private volatile String SerialData = "";
    private Integer ExpectedLength = 0;

    private String ExpectedError1Result = "";
    private String ExpectedError2Result = "";

    public Communicator(String SerialPortDevice) {
        this.DeviceType = SerialPortDevice;
    }

    public void ClearSerialData() {
        ApplicationLauncher.logger.debug("ClearSerialData :Entry");
        this.SerialData = "";
    }

    public boolean SerialPortConfigParityNone(Integer BaudRate) {

        try {

            serialPort.setSerialPortParams(BaudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            return true;

        } catch (UnsupportedCommOperationException e) {

            logText = "SerialPortConfig. (" + e.toString() + ")";
            ApplicationLauncher.logger.info("SerialPortConfigEvenParity:" + logText);
            ApplicationLauncher.logger
                    .error("SerialPortConfigEvenParity:  UnsupportedCommOperationException: " + e.getMessage());
            return false;
        }

    }

    final public String getExpectedError1Result() {
        return this.ExpectedError1Result;
    }

    final public String getExpectedError2Result() {
        return this.ExpectedError2Result;
    }

    public void setExpectedError1Result(String ExpectedErr1Result) {
        this.ExpectedError1Result = ExpectedErr1Result;
    }

    public void setExpectedError2Result(String ExpectedErr2Result) {
        this.ExpectedError2Result = ExpectedErr2Result;
    }

    // search for all the serial ports
    // pre: none
    // post: adds all the found ports to a combo box on the GUI
    public HashMap searchForPorts() {
        try {
            ports = CommPortIdentifier.getPortIdentifiers();

            while (ports.hasMoreElements()) {
                CommPortIdentifier curPort = (CommPortIdentifier) ports.nextElement();

                // get only serial ports
                if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                    // window.cboxPorts.addItem(curPort.getName());
                    portMap.put(curPort.getName(), curPort);
                    ApplicationLauncher.logger.info("Existing ComPort ID:" + curPort.getName());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            ApplicationLauncher.logger.error("searchForPorts: exception:" + e.getMessage());
            // WindowManager.InformUser("SerialPort search failed","Serial Port scan failed
            // due to followinf reason\n\n" + e.getMessage(),AlertType.ERROR);
        }
        return portMap;
        // ApplicationLauncher.logger.info("test1:"+portMap.get("COM3"));
    }

    // connect to the selected port in the combo box
    // pre: ports are already found by using the searchForPorts method
    // post: the connected comm port is stored in commPort, otherwise,
    // an exception is generated
    public void connect(String SerialPort_ID) {
        String selectedPort = SerialPort_ID;

        // ApplicationLauncher.logger.info("SerialCom: portMap: ");
        selectedPortIdentifier = (CommPortIdentifier) portMap.get(selectedPort);

        CommPort commPort = null;

        try {
            /*
             * selectedPortIdentifier = (CommPortIdentifier)portMap.get(selectedPort);
             * 
             * CommPort commPort = null;
             */
            // the method below returns an object of type CommPort
            commPort = selectedPortIdentifier.open(selectedPort, TIMEOUT);
            // the CommPort object can be casted to a SerialPort object

            serialPort = (SerialPort) commPort;
            setDeviceConnected(true);

            // for controlling GUI elements

            // logging
            logText = selectedPort + " opened successfully.";
            ApplicationLauncher.logger.info("SerialCom:" + logText);
            // window.txtLog.setForeground(Color.black);
            // window.txtLog.append(logText + "\n");

            // CODE ON SETTING BAUD RATE ETC OMITTED
            // XBEE PAIR ASSUMED TO HAVE SAME SETTINGS ALREADY

            // enables the controls on the GUI if a successful connection is made
            // window.keybindingController.toggleControls();
        } catch (PortInUseException e) {
            e.printStackTrace();
            logText = selectedPort + " is in use. (" + e.toString() + ")";
            ApplicationLauncher.logger.error("Communicator: connect: PortInUseException: " + e.getMessage());
            ApplicationLauncher.logger.info("SerialCom:" + logText);

        } catch (Exception e) {
            logText = "Failed to open " + selectedPort + "(" + e.toString() + ")";
            ApplicationLauncher.logger.error("Communicator: connect: Exception: " + e.getMessage());
            ApplicationLauncher.logger.info("SerialCom:" + logText);

        }
    }

    // open the input and output streams
    // pre: an open port
    // post: initialized intput and output streams for use to communicate data
    public boolean initIOStream() {
        // return value for whather opening the streams is successful or not
        boolean successful = false;
        ApplicationLauncher.logger.info("SerialCom:initIOStream");
        try {
            //

            input = serialPort.getInputStream();
            output = serialPort.getOutputStream();

            successful = true;
            return successful;

        } catch (IOException e) {
            logText = "I/O Streams failed to open. (" + e.toString() + ")";
            ApplicationLauncher.logger.info("SerialCom:" + logText);
            ApplicationLauncher.logger.error("initIOStream:  IOException: " + e.getMessage());

            return successful;
        }

    }

    // starts the event listener that knows whenever data is available to be read
    // pre: an open serial port
    // post: an event listener for the serial port that knows when data is recieved
    public void initListener() {
        try {
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);

        } catch (TooManyListenersException e) {
            logText = "Too many listeners. (" + e.toString() + ")";
            ApplicationLauncher.logger.info("SerialCom:" + logText);
            ApplicationLauncher.logger.error("initListener:  TooManyListenersException: " + e.getMessage());

        }

    }

    public boolean SerialPortConfig(Integer BaudRate) {

        try {

            serialPort.setSerialPortParams(BaudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            return true;

        } catch (UnsupportedCommOperationException e) {

            logText = "SerialPortConfig. (" + e.toString() + ")";
            ApplicationLauncher.logger.info("SerialCom:" + logText);
            ApplicationLauncher.logger.error("SerialPortConfig:  UnsupportedCommOperationException: " + e.getMessage());
            return false;
        }

    }

    public boolean SerialPortConfigEvenParity(Integer BaudRate) {

        try {

            serialPort.setSerialPortParams(BaudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                    SerialPort.PARITY_EVEN);
            return true;

        } catch (UnsupportedCommOperationException e) {

            logText = "SerialPortConfig. (" + e.toString() + ")";
            ApplicationLauncher.logger.info("SerialPortConfigEvenParity:" + logText);
            ApplicationLauncher.logger
                    .error("SerialPortConfigEvenParity:  UnsupportedCommOperationException: " + e.getMessage());
            return false;
        }

    }

    // disconnect the serial port
    // pre: an open serial port
    // post: clsoed serial port

    // http://rxtx.qbang.org/pub/rxtx/rxtx-2.1-7/CNI/RXTXPort.java
    // public native void setRTS( boolean state );

    public void SetFlowControlMode() {

        try {
            serialPort.setFlowControlMode(
                    SerialPort.FLOWCONTROL_NONE);

        } catch (UnsupportedCommOperationException ex) {
            ApplicationLauncher.logger.debug("SetFlowControlMode: " + ex.getMessage());
            ApplicationLauncher.logger
                    .error("SetFlowControlMode:  UnsupportedCommOperationException: " + ex.getMessage());
        }
    }

    public void SetFlowControlModeV2() {

        // try {
        // serialPort.setFlowControlMode(
        // SerialPort.PARITY_NONE );
        // serialPort.setParityErrorChar(SerialPort.PARITY_NONE);
        // serialPort.getLowLatency()
        // serialPort.setLowLatency();
        serialPort.setInputBufferSize(1000);
        serialPort.setOutputBufferSize(1000);
        // serialPort.`

        /*
         * } catch (UnsupportedCommOperationException ex) {
         * ApplicationLauncher.logger.debug("SetFlowControlModeV2: "+ex.getMessage());
         * ApplicationLauncher.logger.
         * error("SetFlowControlModeV2:  UnsupportedCommOperationException: " +
         * ex.getMessage());
         * }
         */
    }

    public void SetRTS(Boolean Status) {

        serialPort.setRTS(Status);
        Sleep(200);

    }

    public void Sleep(int timeInMsec) {

        try {
            Thread.sleep(timeInMsec);
        } catch (InterruptedException e) {

            e.printStackTrace();
            ApplicationLauncher.logger.error("Sleep3:  InterruptedException: " + e.getMessage());
        }

    }

    public void disconnect() {
        // close the serial port
        try {

            serialPort.removeEventListener();
            serialPort.close();
            input.close();
            output.close();
            setDeviceConnected(false);

            logText = "Disconnected.";
            ApplicationLauncher.logger
                    .info("SerialCom:" + getPortDeviceMapping() + ":" + serialPort.getName() + ":" + logText);

        } catch (Exception e) {
            logText = "Failed to close " + serialPort.getName() + "(" + e.toString() + ")";
            ApplicationLauncher.logger.info("SerialCom:" + logText);
            ApplicationLauncher.logger.error("disconnect:  Exception: " + e.getMessage());

        }
    }

    public String getSerialPortOwner(String selectedPort) {
        String serialPortOwnerName = "";
        try {

            CommPortIdentifier portIdentifier = (CommPortIdentifier) portMap.get(selectedPort);
            serialPortOwnerName = portIdentifier.getCurrentOwner();
            logText = " : serialPortOwnerName.: " + serialPortOwnerName;
            ApplicationLauncher.logger
                    .info("SerialCom:" + getPortDeviceMapping() + ":" + serialPort.getName() + ":" + logText);

        } catch (Exception e) {
            logText = "Failed to get owner: " + serialPort.getName() + "(" + e.toString() + ")";
            ApplicationLauncher.logger.info("getSerialPortOwner:" + logText);
            ApplicationLauncher.logger.error("getSerialPortOwner:  Exception: " + e.getMessage());

        }
        return serialPortOwnerName;
    }

    final public boolean getPowerSourceDeviceConnected() {
        return bPowerSourceDeviceConnected;
    }

    public void setPowerSourceDeviceConnected(boolean bConnected) {
        this.bPowerSourceDeviceConnected = bConnected;
    }

    final public boolean getDeviceConnected() {
        return bDeviceConnected;
    }

    public void getOwner() {

    }

    public void setExpectedResult(String ExpectedResult) {
        setExpectedDataErrorResult(ExpectedResult.replaceAll(ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE,
                ConstantPowerSourceMte.CMD_PWR_SRC_DATA_ERROR_RESPONSE));
        setExpectedSetErrorResult(ExpectedResult.replaceAll(ConstantPowerSourceMte.CMD_PWR_SRC_SET_EXPECTED_RESPONSE,
                ConstantPowerSourceMte.CMD_PWR_SRC_SET_ERROR_RESPONSE));

        this.ExpectedResult = ExpectedResult;
    }

    final public String getExpectedResult() {
        return this.ExpectedResult;
    }

    public void setExpectedDataErrorResult(String ExpectedDataErrResult) {
        this.ExpectedDataErrorResult = ExpectedDataErrResult;
    }

    public void setExpectedSetErrorResult(String ExpectedSetErrResult) {
        this.ExpectedSetErrorResult = ExpectedSetErrResult;
    }

    final public String getExpectedDataErrorResult() {
        return this.ExpectedDataErrorResult;
    }

    final public String getExpectedSetErrorResult() {
        return this.ExpectedSetErrorResult;
    }

    public void setExpectedLength(Integer ExpectedLength) {
        this.ExpectedLength = ExpectedLength;
    }

    final public Integer getExpectedLength() {
        return this.ExpectedLength;
    }

    final public String getSerialData() {
        return this.SerialData;
    }

    public void StripMatchedString(String ToBeReplacedData) {
        ApplicationLauncher.logger.debug("StripMatchedString:  ToBeReplacedData" + ToBeReplacedData);
        try {
            SerialData = SerialData.replaceFirst(ToBeReplacedData, "");
        } catch (Exception e) {
            // ApplicationLauncher.logger.info("StripMatchedString: Exception" +
            // e.toString());
            e.printStackTrace();
            ApplicationLauncher.logger.error("StripMatchedString:  Exception: " + e.getMessage());
        }

    }

    public void StripLength(Integer LengthTobeDeleted) {
        ApplicationLauncher.logger.debug("StripLength:  LengthTobeDeleted: " + LengthTobeDeleted);
        try {
            SerialData = SerialData.substring(LengthTobeDeleted);
        } catch (Exception e) {
            // ApplicationLauncher.logger.info("StripLength: Exception" + e.toString());
            e.printStackTrace();
            ApplicationLauncher.logger.error("StripLength:  Exception: " + e.getMessage());
        }
    }

    public void setDataReadFormatInHex(boolean ReadFormatToHex) {
        this.ReadFormatInHex = ReadFormatToHex;
    }

    final public String getPortDeviceMapping() {
        return this.DeviceType;
    }

    public void setDeviceConnected(boolean bConnected) {
        this.bDeviceConnected = bConnected;
    }

    final public boolean getRefStandardDeviceConnected() {
        return bRefStandardDeviceConnected;
    }

    public void setRefStandardDeviceConnected(boolean bConnected) {
        this.bRefStandardDeviceConnected = bConnected;
    }

    // what happens when data is received
    // pre: serial event is triggered
    // post: processing on the data it reads
    public void serialEvent(SerialPortEvent evt) {

        if (evt.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                //
                if (ReadFormatInHex) {
                    int HexData = (int) input.read();
                    logText = String.format("%02X", HexData);
                    // ApplicationLauncher.logger.info("Hex:" +
                    // logText);//#DEBUG_2022_09_08_REF_STD_KIGG
                    this.SerialData = this.SerialData + logText;
                } else {

                    byte byteData = (byte) input.read();

                    if (byteData != NEW_LINE_ASCII) {
                        logText = new String(new byte[] { byteData });
                        this.SerialData = this.SerialData + logText;

                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
                ApplicationLauncher.logger.error("serialEvent:  Exception: " + e.getMessage());
                logText = "Failed to read data. (" + e.toString() + ")";
                ApplicationLauncher.logger
                        .info("SerialCom:" + getPortDeviceMapping() + ":" + serialPort.getName() + ":" + logText);

            }
        }
    }

    // method that can be called to send data
    // pre: open serial port
    // post: data sent to the other device
    public void writeData(int leftThrottle, int rightThrottle) {
        try {
            // output.write(20);
            // output.flush();
            // writeStringMsgToPort("Hello");
            /*
             * //this is a delimiter for the data
             * output.write(DASH_ASCII);
             * output.flush();
             * 
             * output.write(rightThrottle);
             * output.flush();
             * //will be read as a byte so it is a space key
             * output.write(SPACE_ASCII);
             * output.flush();
             */
        } catch (Exception e) {
            e.printStackTrace();
            ApplicationLauncher.logger.error("writeData:  Exception: " + e.getMessage());
            logText = "Failed to write data. (" + e.toString() + ")";
            ApplicationLauncher.logger.info("SerialCom:" + logText);

        }
    }

    public void writeStringMsgToPort(String send) {

        try {

            output.write(send.getBytes());
            output.flush();

        } catch (IOException e) {

            ApplicationLauncher.logger.error("writeStringMsgToPort:  IOException: " + e.getMessage());
            logText = "Failed to write string I/O. (" + e.toString() + ")";
            ApplicationLauncher.logger
                    .info("SerialCom:" + getPortDeviceMapping() + ":" + serialPort.getName() + ":" + logText);
            e.printStackTrace();

        }
    }

    public void writeStringMsgToPortInHex(String inHex) {
        try {
            String Hex_output = "";
            int Hex_intOutput = 0;
            for (int i = 0; i < inHex.length() - 1; i += 2) {

                // grab the hex in pairs
                Hex_output = inHex.substring(i, (i + 2));
                Hex_intOutput = Integer.parseInt(Hex_output, 16);
                output.write(Hex_intOutput & 0xff);// send.getBytes());
            }

            output.flush();

        } catch (IOException e) {

            e.printStackTrace();
            ApplicationLauncher.logger.error("writeStringMsgToPort:  IOException: " + e.getMessage());
            logText = "Failed to write string I/O. (" + e.toString() + ")";
            ApplicationLauncher.logger
                    .info("SerialCom:" + getPortDeviceMapping() + ":" + serialPort.getName() + ":" + logText);

        }
    }

    public boolean SerialPortConfigV2(Integer BaudRate) {

        try {

            serialPort.setSerialPortParams(BaudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                    SerialPort.PARITY_EVEN);
            return true;

        } catch (UnsupportedCommOperationException e) {

            logText = "SerialPortConfig. (" + e.toString() + ")";
            ApplicationLauncher.logger.info("SerialCom:" + logText);
            ApplicationLauncher.logger.error("SerialPortConfig:  UnsupportedCommOperationException: " + e.getMessage());
            return false;
        }

    }
}
