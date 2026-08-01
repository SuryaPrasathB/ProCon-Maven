package com.tasnetwork.calibration.conveyor.plc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.tasnetwork.calibration.conveyor.ClusterServer;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;

public class ModbusTcpClientManager {
	
    private final Map<String, ModbusTcpClient> modbusTcpServerMap = new ConcurrentHashMap<>();
    private final Map<String, Boolean> modbusTcpServerStatus = new ConcurrentHashMap<>();
    private final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    private static boolean isScheduled = false;
	
	 public ModbusTcpClientManager() {
    	
    	if (!isScheduled) {
            synchronized (this) {
                if (!isScheduled) {  // Double-check locking
                    
                    scheduler.scheduleAtFixedRate(this::modbusKeepAliveTask, 1, 30, TimeUnit.MINUTES);//1 initial delay, 30 minutes periodic keep alive
                    isScheduled = true; 
                    ModbusTcpClient.logger.debug("ModbusTcpClientManager: modbus client : Keep-alive task scheduled!");
                }
            }
        } else {
        	ModbusTcpClient.logger.debug("ModbusTcpClientManager: modbus client : Keep-alive task is already running.");
        }
    }
    private String getKey(String ip, int port) {
        return ip + ":" + port;
    }
    
    private void modbusKeepAliveTask() {
        for (Map.Entry<String, ModbusTcpClient> entry : getModbusTcpServerMap().entrySet()) {
            String serverKey = entry.getKey();
            ModbusTcpClient client = entry.getValue();

         // Extract IP address and port from serverKey
            String[] parts = serverKey.split(":");
            if (parts.length != 2) {
                ModbusTcpClient.logger.debug("modbusKeepAliveTask: Invalid server key format: keep-alive : " + serverKey);
                continue;
            }

            String ipAddress = parts[0];
            String port = parts[1];
           
            ModbusTcpClient.logger.info("modbusKeepAliveTask: Sending keep-alive to " + ipAddress + ":" + port);

            int address = 1;
            if (client.getModbusClient().isConnected()) {
                ModbusTcpClient.logger.info("Sending keep-alive to " + serverKey + " -> address : " + address );
                ClusterServer clusterServer = new ClusterServer(ipAddress,port);
                ModbusRequestProcessor.addRequest(serverKey, () -> {
                    BayResponse response = BayUtils.getModbusTcpClientManager()
                            .modbusReadCoil(clusterServer, address);

                    if (!response.getStatus()) {
                        ModbusTcpClient.logger.debug("modbusKeepAliveTask: Keep-alive failed for " + serverKey + " -> address : " + address);
                    }
                });
            }
        }
    }
    
    public boolean ensureModbusConnection(ClusterServer clusterServer) {
        String ipAddress = clusterServer.getIpAddress();
        int port = Integer.parseInt(clusterServer.getPort());
        String key = getKey(ipAddress, port);

        // Check if connection already exists
        if (modbusTcpServerStatus.getOrDefault(key, false)) {
            //ApplicationLauncher.logger.info("Already connected to " + key);
            return true;
        }

        // Attempt to establish a new connection
        ModbusTcpClient client = new ModbusTcpClient();
        try {
            client.modbusConnect(ipAddress, port);
            Thread.sleep(1000); // Allow connection time

            if (client.getModbusClient().isConnected()) {
                modbusTcpServerMap.put(key, client);
                modbusTcpServerStatus.put(key, true);
                ModbusTcpClient.logger.info("Successfully connected to " + key);
                return true;
            } else {
                ModbusTcpClient.logger.error("Connection failed to " + key);
                return false;
            }
        } catch (Exception e) {
            ModbusTcpClient.logger.error("Error connecting to " + key + ": " + e.getMessage());
            return false;
        }
    }


    public BayResponse modbusConnect(String ipAddress, int port) {
        String key = getKey(ipAddress, port);
        BayResponse response = new BayResponse();

        if (modbusTcpServerMap.containsKey(key) && modbusTcpServerStatus.getOrDefault(key, false)) {
            response.setResponseData("Already Connected to " + key);
            response.setStatus(true);
            return response;
        }

        ModbusTcpClient client = new ModbusTcpClient();
        try {
            client.modbusConnect(ipAddress, port);
            Thread.sleep(1000);
            if (client.getModbusClient().isConnected()) {
                modbusTcpServerMap.put(key, client);
                modbusTcpServerStatus.put(key, true);
                response.setResponseData("Connected to " + key);
                response.setStatus(true);
            } else {
                response.setResponseData("Connection failed");
            }
        } catch (Exception e) {
            ModbusTcpClient.logger.error("modbusConnect Error: " + e.getMessage());
            response.setResponseData("Error: " + e.getMessage());
        }
        return response;
    }

    public BayResponse modbusDisconnect(String ipAddress, int port) {
        String key = getKey(ipAddress, port);
        BayResponse response = new BayResponse();

        ModbusTcpClient client = modbusTcpServerMap.get(key);
        if (client == null || !modbusTcpServerStatus.getOrDefault(key, false)) {
            response.setResponseData("Not connected to " + key);
            return response;
        }
        try {
            client.modbusDisconnect();
            modbusTcpServerMap.remove(key);
            modbusTcpServerStatus.remove(key);
            response.setResponseData("Disconnected from " + key);
            response.setStatus(true);
        } catch (Exception e) {
            response.setResponseData("Disconnection error: " + e.getMessage());
        }
        return response;
    }
    
    public BayResponse modbusWriteCoil(ClusterServer clusterServer, int writeAddress, boolean writeData) {
        String key = getKey(clusterServer.getIpAddress(), Integer.parseInt(clusterServer.getPort()));
        BayResponse response = new BayResponse();
        ModbusTcpClient client = modbusTcpServerMap.get(key);

        if (client == null || !client.getModbusClient().isConnected()) {
            response.setResponseData("Not connected to " + key);
            return response;
        }
        try {
            client.modbusTcpSendWriteCoilCmd(writeAddress, writeData);
            response.setResponseData("Write Coil Successful");
            response.setStatus(true);
        } catch (Exception e) {
            response.setResponseData("Write Coil Error: " + e.getMessage());
        }
        return response;
    }
    
    public BayResponse modbusReadCoil(ClusterServer clusterServer, int readAddress) {
        String key = getKey(clusterServer.getIpAddress(), Integer.parseInt(clusterServer.getPort()));
        BayResponse response = new BayResponse();
        ModbusTcpClient client = modbusTcpServerMap.get(key);

        if (client == null || !client.getModbusClient().isConnected()) {
            response.setResponseData("Not connected to " + key);
            return response;
        }
        return client.modbusTcpSendReadCoilCmd(readAddress);
    }

    public BayResponse modbusWriteCoil(String ipAddress, int port, int writeAddress, boolean writeData) {
        String key = getKey(ipAddress, port);
        BayResponse response = new BayResponse();
        ModbusTcpClient client = modbusTcpServerMap.get(key);

        if (client == null || !client.getModbusClient().isConnected()) {
            response.setResponseData("Not connected to " + key);
            return response;
        }
        try {
            client.modbusTcpSendWriteCoilCmd(writeAddress, writeData);
            response.setResponseData("Write Coil Successful");
            response.setStatus(true);
        } catch (Exception e) {
            response.setResponseData("Write Coil Error: " + e.getMessage());
        }
        return response;
    }

    public BayResponse modbusReadCoil(String ipAddress, int port, int readAddress) {
        String key = getKey(ipAddress, port);
        BayResponse response = new BayResponse();
        ModbusTcpClient client = modbusTcpServerMap.get(key);

        if (client == null || !client.getModbusClient().isConnected()) {
            response.setResponseData("Not connected to " + key);
            return response;
        }
        return client.modbusTcpSendReadCoilCmd(readAddress);
    }
    
    public BayResponse modbusReadHoldingRegister(ClusterServer clusterServer, int readAddress) {
        String key = getKey(clusterServer.getIpAddress(), Integer.parseInt(clusterServer.getPort()));
        BayResponse response = new BayResponse();
        ModbusTcpClient client = modbusTcpServerMap.get(key);

        if (client == null || !client.getModbusClient().isConnected()) {
            response.setResponseData("Not connected to " + key);
            return response;
        }
        return client.modbusTcpSendReadHoldingRegistersCmd(readAddress);
    }

    public BayResponse modbusReadHoldingRegister(String ipAddress, int port, int readAddress) {
        String key = getKey(ipAddress, port);
        BayResponse response = new BayResponse();
        ModbusTcpClient client = modbusTcpServerMap.get(key);

        if (client == null || !client.getModbusClient().isConnected()) {
            response.setResponseData("Not connected to " + key);
            return response;
        }
        return client.modbusTcpSendReadHoldingRegistersCmd(readAddress);
    }
    
    public BayResponse modbusWriteHoldingRegister(ClusterServer clusterServer, int writeAddress, int writeValue) {
        String key = getKey(clusterServer.getIpAddress(), Integer.parseInt(clusterServer.getPort()));
        BayResponse response = new BayResponse();
        ModbusTcpClient client = modbusTcpServerMap.get(key);

        if (client == null || !client.getModbusClient().isConnected()) {
            response.setResponseData("Not connected to " + key);
            return response;
        }
        return client.modbusTcpSendWriteHoldingRegistersCmd(writeAddress, writeValue);
    }

    public BayResponse modbusWriteHoldingRegister(String ipAddress, int port, int writeAddress, int writeValue) {
        String key = getKey(ipAddress, port);
        BayResponse response = new BayResponse();
        ModbusTcpClient client = modbusTcpServerMap.get(key);

        if (client == null || !client.getModbusClient().isConnected()) {
            response.setResponseData("Not connected to " + key);
            return response;
        }
        return client.modbusTcpSendWriteHoldingRegistersCmd(writeAddress, writeValue);
    }

	public Map<String, ModbusTcpClient> getModbusTcpServerMap() {
		return modbusTcpServerMap;
	}

	public Map<String, Boolean> getModbusTcpServerStatus() {
		return modbusTcpServerStatus;
	}
    

}
