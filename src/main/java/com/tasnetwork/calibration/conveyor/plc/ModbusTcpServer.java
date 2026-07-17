package com.tasnetwork.calibration.conveyor.plc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

import de.re.easymodbus.server.ModbusServer;
import javafx.scene.control.Alert.AlertType;

public class ModbusTcpServer {

	private ModbusServer modbusServer;
    private Timer serverMonitorTimer;
    private Timer monitoringTimer;
    private Consumer<int[]> holdingRegisterUpdateListener;
    private Consumer<boolean[]> coilUpdateListener;

    
    public ModbusTcpServer() {
    	
    }
    public ModbusTcpServer(String ipAddress, int portAddress) {
        init(ipAddress,portAddress);
    }

    // ✅ Initialize Modbus Server Configuration
    public void init(String ipAddress, int portAddress) {
    	
    	//try {
    		//InetAddress bindAddress = InetAddress.getByName(ipAddress);
        
			//ServerSocket serverSocket = new ServerSocket(portAddress, 50, bindAddress);
			
			modbusServer = new ModbusServer();
	        modbusServer.setPort(portAddress);//502);
	        modbusServer.setClientConnectionTimeout(0);
	        //modbusServer.setServerIPAddress("192.168.0.103");
	        //modbusServer.setUnitIdentifier((byte) 1);
	       // modbusServer.
	        //modbusServer.setTimeout(10000);
/*	        //modbusServer.tcpListener.setAddress(ipAddress); 
			//modbusServer.setServerSocket(serverSocket);
	        //modbusServer.UnitIdentifier = 1;
	        //modbusServer.setUnitIdentifier((byte) 1);
	        //modbusServer.setServerIPAddress("192.168.0.103");
			
			Field socketField;
			try {
				socketField = ModbusServer.class.getDeclaredField("serverSocket");
				socketField.setAccessible(true);
	            try {
					socketField.set(modbusServer, serverSocket);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
            


	        // Initialize Holding Registers (1000 registers)
	        modbusServer.holdingRegisters = new int[1000];
	        modbusServer.holdingRegisters[0] = 123;
	        modbusServer.holdingRegisters[9] = 456;
	        modbusServer.holdingRegisters[12] = 789;
	        modbusServer.holdingRegisters[201] = 20000;
	        modbusServer.holdingRegisters[301] = 30000;

	        // Initialize Coils (100 coils)
	        modbusServer.coils = new boolean[100];
	        modbusServer.coils[0] = true;
	        modbusServer.coils[1] = false;
	        modbusServer.coils[2] = false;
	        modbusServer.coils[3] = true;
	        modbusServer.coils[4] = true;
	        modbusServer.coils[5] = false;
	        modbusServer.coils[6] = true;
	        modbusServer.coils[10] = true;

	        ApplicationLauncher.logger.debug("🔧 Modbus Server Initialized.");
		//} catch (IOException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
        
        
    }
    
    public boolean isPortAvailable(int port) {
    	
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.close();
            return true; // Port is free
        } catch (IOException e) {
        	ServerSocket serverSocket;
			try {
				serverSocket = new ServerSocket(port);
				try {
					serverSocket.setReuseAddress(true);
					try {
						serverSocket.bind(new InetSocketAddress(port));
						return true; // Port is free
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (SocketException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			} catch (IOException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
        	
        	
            return false; // Port is in use
        }
    }
    
    public boolean[] getCoils() {
    	return modbusServer.coils;
    }

    // ✅ Start the Modbus Server
    public boolean start() {
        try {
            ApplicationLauncher.logger.debug("🚀 Starting Modbus TCP Server on Port 502...");
            //modbusServer.Listen();
            //ApplicationLauncher.logger.debug("✅ Modbus Server Running...");

            // ✅ Start Monitoring Thread for Holding Register Changes
            //monitorHoldingRegisters();
            if (!isPortAvailable(modbusServer.getPort())) {
            	ApplicationLauncher.logger.error("❌ ERROR: Port " + modbusServer.getPort() + " is already in use!");
                //JOptionPane.showMessageDialog(null, "❌ ERROR: Port " + modbusServer.getPort() + " is already in use!\nStop any other process and try again.", 
                //                              "Port Conflict", JOptionPane.ERROR_MESSAGE);
                
                ApplicationLauncher.InformUser("Error-K01-Port Conflict","❌ ERROR: Port " + modbusServer.getPort() +" is already in use!\nPlease stop any other process using it and try again.",AlertType.ERROR);
                
                return false;
            }
            
            try {
                modbusServer.Listen();
                ApplicationLauncher.logger.debug("✅ Modbus Server Running...");
                startMonitoring();
            } catch (java.net.BindException e) {
                ApplicationLauncher.logger.error("❌ ERROR: Port is already in use. Please stop any other process using port 502.");
                /*JOptionPane.showMessageDialog(null, "❌ ERROR: Port 502 is already in use!\nPlease stop any other process using it and try again.", 
                                              "Port Conflict", JOptionPane.ERROR_MESSAGE);*/
                
                ApplicationLauncher.InformUser("Error-K01-Port Conflict","❌ ERROR: Port 502 is already in use!\\nPlease stop any other process using it and try again.",AlertType.ERROR);
                return false;
            } catch (Exception e) {
                ApplicationLauncher.logger.error("❌ ERROR: Failed to start Modbus Server - " + e.getMessage());
                return false;
            }

        } catch (Exception e) {
            ApplicationLauncher.logger.debug("❌ Error starting Modbus Server: " + e.getMessage());
            return false;
        }
        return true;
    }

    // ✅ Stop the Modbus Server
    public void stop() {
        if (modbusServer != null) {
        	
        	int port = modbusServer.getPort();
            modbusServer.StopListening();

            modbusServer.stop();
            try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
            //forceClosePort(port);
            forceKillPort(port);
            //ApplicationLauncher.logger.debug("🛑 Modbus Server Stopped.");
            if (monitoringTimer != null) {
                monitoringTimer.cancel();
                monitoringTimer.purge();
                monitoringTimer = null;
            }
            modbusServer = null; // ✅ Release memory
            System.gc(); // ✅ Suggest garbage collection
            ApplicationLauncher.logger.debug("🛑 Modbus Server Stopped.");
        }
    }
    
    private void forceClosePort(int port) {
        try (ServerSocket socket = new ServerSocket(port)) {
        	ApplicationLauncher.logger.debug("forceClosePort: Port 502 released.");
        } catch (IOException e) {
        	ApplicationLauncher.logger.debug("forceClosePort :  Port 502 already free.");
        }
    }
    
    
    public void forceKillPort(int port) {
    	ApplicationLauncher.logger.debug("forceKillPort: Port : " + port);
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
        	forceKillPortWindows(port);
        } else {
            killPortLinux(port);
        }
    }
    
    /*public void killPortWindows(int port) {
    	ApplicationLauncher.logger.debug("killPortWindows: Entry : port: " + port);
        try {
            // Find process ID (PID) using the port
            Process process = Runtime.getRuntime().exec("netstat -ano | findstr :" + port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                ApplicationLauncher.logger.debug("Netstat Output: " + line);
                String[] parts = line.trim().split("\\s+");
                String pid = parts[parts.length - 1]; // Extract last column (PID)

                // Kill the process using the PID
                Runtime.getRuntime().exec("taskkill /F /PID " + pid);
                ApplicationLauncher.logger.debug("✅ Port " + port + " freed (PID: " + pid + ")");
            }
        } catch (Exception e) {
            ApplicationLauncher.logger.debug("⚠️ Error closing port: " + e.getMessage());
        }
    }*/
    
    /*public void forceKillPortWindows(int port) {
        ApplicationLauncher.logger.debug("forceKillPortWindows: Entry : port: " + port);
        try {
            // Find process ID (PID) using the port
            Process process = Runtime.getRuntime().exec("netstat -ano | findstr :" + port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            boolean killed = false;

            while ((line = reader.readLine()) != null) {
                ApplicationLauncher.logger.debug("Netstat Output: " + line);
                String[] parts = line.trim().split("\\s+");
                if (parts.length > 4) {
                    String pid = parts[parts.length - 1]; // Extract last column (PID)

                    // Kill the process using the PID
                    Runtime.getRuntime().exec("taskkill /F /PID " + pid);
                    ApplicationLauncher.logger.debug("✅ Process with PID " + pid + " killed.");
                    killed = true;
                    Thread.sleep(2000); // Give OS time to clean up
                }
            }

            // If no process was killed, log a message
            if (!killed) {
                ApplicationLauncher.logger.debug("⚠️ No process found on port " + port);
            }

            // Verify if port is still occupied
            if (!isPortAvailable(port)) {
                ApplicationLauncher.logger.debug("⚠️ Port " + port + " is still in use! Retrying...");
                Thread.sleep(3000);
                forceKillPortWindows(port); // Retry once if needed
            } else {
                ApplicationLauncher.logger.debug("✅ Port " + port + " is now free.");
            }

        } catch (Exception e) {
            ApplicationLauncher.logger.debug("⚠️ Error closing port: " + e.getMessage());
        }
    }*/
    
    /*public void forceKillPortWindows(int port) {
        ApplicationLauncher.logger.debug("forceKillPortWindows: Entry : port: " + port);

        try {
            // Run netstat and capture output
            Process process = Runtime.getRuntime().exec("netstat -ano | findstr :" + port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            boolean found = false;

            ApplicationLauncher.logger.debug("🔍 Checking port: " + port);

            while ((line = reader.readLine()) != null) {
                ApplicationLauncher.logger.debug("📄 Netstat Output: " + line);
                found = true;

                // Extract the last column (PID)
                String[] parts = line.trim().split("\\s+");
                String pid = parts[parts.length - 1];

                // Kill the process using the PID
                Runtime.getRuntime().exec("taskkill /F /PID " + pid);
                ApplicationLauncher.logger.debug("✅ Port " + port + " freed (PID: " + pid + ")");
            }

            if (!found) {
                ApplicationLauncher.logger.debug("❌ No process found using port: " + port);
            }

        } catch (Exception e) {
            ApplicationLauncher.logger.debug("⚠️ Error closing port: " + e.getMessage());
        }
    }*/
    
    
   /* public void forceKillPortWindows(int port) {
        ApplicationLauncher.logger.debug("forceKillPortWindows: Entry : port: " + port);

        try {
            // Use cmd.exe /c to properly execute the command
            Process process = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "netstat -ano | findstr :" + port});
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            boolean found = false;

            ApplicationLauncher.logger.debug("🔍 Checking port: " + port);

            while ((line = reader.readLine()) != null) {
                ApplicationLauncher.logger.debug("📄 Netstat Output: " + line);  // <-- This should now print!
                found = true;

                // Extract the last column (PID)
                String[] parts = line.trim().split("\\s+");
                if (parts.length > 4) {  // Ensure valid parsing
                    String pid = parts[parts.length - 1];

                    // Kill the process using the PID
                    //Runtime.getRuntime().exec("taskkill /F /PID " + pid);
                    Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "taskkill /T /F /PID " + pid});

                    ApplicationLauncher.logger.debug("✅ Port " + port + " freed (PID: " + pid + ")");
                }
            }

            if (!found) {
                ApplicationLauncher.logger.debug("❌ No process found using port: " + port);
            }

            // Ensure process output is fully flushed
            process.waitFor();
            process.destroy();

        } catch (Exception e) {
            ApplicationLauncher.logger.debug("⚠️ Error closing port: " + e.getMessage());
        }
    }

*/
    
    public static void forceKillPortWindows(int port) {
        try {
            Process process = Runtime.getRuntime().exec("netstat -ano | findstr :" + port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("📝 Netstat Output: " + line);
                if (line.contains("LISTENING")) {
                    String[] parts = line.trim().split("\\s+");
                    int pid = Integer.parseInt(parts[parts.length - 1]);  // Get PID from last column

                    // Check if PID belongs to Java before killing
                    Process checkProcess = Runtime.getRuntime().exec("tasklist /FI \"PID eq " + pid + "\"");
                    BufferedReader checkReader = new BufferedReader(new InputStreamReader(checkProcess.getInputStream()));
                    String checkLine;
                    boolean isJava = false;
                    while ((checkLine = checkReader.readLine()) != null) {
                        if (checkLine.contains("java.exe")) {
                            isJava = true;
                            break;
                        }
                    }

                    if (!isJava) {
                        System.out.println("🔪 Killing process with PID: " + pid);
                        Runtime.getRuntime().exec("taskkill /F /PID " + pid);
                    } else {
                        System.out.println("⚠️ Skipping Java PID: " + pid);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void killPortLinux(int port) {
        try {
            // Find process ID (PID) using the port
            Process process = Runtime.getRuntime().exec("lsof -t -i:" + port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String pid;
            while ((pid = reader.readLine()) != null) {
                // Kill the process
                Runtime.getRuntime().exec("kill -9 " + pid);
                ApplicationLauncher.logger.debug("✅ Port " + port + " freed (PID: " + pid + ")");
            }
        } catch (Exception e) {
            ApplicationLauncher.logger.debug("⚠️ Error closing port: " + e.getMessage());
        }
    }
    

    // ✅ Monitor Holding Registers for changes
    private void monitorHoldingRegisters() {
        serverMonitorTimer = new Timer();
        serverMonitorTimer.schedule(new TimerTask() {
            private int[] previousValues = modbusServer.holdingRegisters.clone();

            @Override
            public void run() {
                for (int i = 0; i < modbusServer.holdingRegisters.length; i++) {
                    if (modbusServer.holdingRegisters[i] != previousValues[i]) {
                        ApplicationLauncher.logger.debug("Register[" + i + "] changed from " + previousValues[i] +
                                " to " + modbusServer.holdingRegisters[i]);
                        previousValues[i] = modbusServer.holdingRegisters[i];
                    }
                }
            }
        }, 0, 1000);
    }

    // ✅ Write a value to a specific Holding Register
    public void writeHoldingRegister(int address, int value) {
        if (address >= 0 && address < modbusServer.holdingRegisters.length) {
            modbusServer.holdingRegisters[address] = value;
            ApplicationLauncher.logger.debug("✅ Holding Register[" + address + "] updated to " + value);
        } else {
            ApplicationLauncher.logger.debug("❌ Invalid Register Address: " + address);
        }
    }
    
    
    private void startMonitoring() {
        monitoringTimer = new Timer();
        monitoringTimer.schedule(new TimerTask() {
            private int[] previousHoldingRegisters = modbusServer.holdingRegisters.clone();
            private boolean[] previousCoils = modbusServer.coils.clone();

            @Override
            public void run() {
                boolean holdingChanged = false, coilsChanged = false;

                // Check Holding Registers
                for (int i = 0; i < modbusServer.holdingRegisters.length; i++) {
                    if (modbusServer.holdingRegisters[i] != previousHoldingRegisters[i]) {
                        ApplicationLauncher.logger.debug("🔄 Register[" + i + "] changed: " + previousHoldingRegisters[i] +
                                " → " + modbusServer.holdingRegisters[i]);
                        previousHoldingRegisters[i] = modbusServer.holdingRegisters[i];
                        holdingChanged = true;
                    }
                }

                // Check Coils
                for (int i = 0; i < modbusServer.coils.length; i++) {
                    if (modbusServer.coils[i] != previousCoils[i]) {
                        ApplicationLauncher.logger.debug("🔄 Coil[" + i + "] changed: " + previousCoils[i] +
                                " → " + modbusServer.coils[i]);
                        previousCoils[i] = modbusServer.coils[i];
                        coilsChanged = true;
                    }
                }

                // Notify UI
                if (holdingChanged && holdingRegisterUpdateListener != null) {
                    holdingRegisterUpdateListener.accept(previousHoldingRegisters.clone());
                }
                if (coilsChanged && coilUpdateListener != null) {
                    coilUpdateListener.accept(previousCoils.clone());
                }
            }
        }, 0, 1000);
    }

    // ✅ Set UI Listeners
    public void setHoldingRegisterUpdateListener(Consumer<int[]> listener) {
        this.holdingRegisterUpdateListener = listener;
    }

    public void setCoilUpdateListener(Consumer<boolean[]> listener) {
        this.coilUpdateListener = listener;
    }

    public ModbusServer getModbusServer() {
        return modbusServer;
    }
}
