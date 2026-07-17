package com.tasnetwork.calibration.conveyor.tree;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

import java.util.ArrayList;
import java.util.List;

// Represents Bay which contains devices and ports
class Bay {
    private String bayId; // Unique identifier for the bay
    private String bayName;
    private List<Device> devices; // List of devices associated with this bay

    // Constructor to initialize Bay with details from JSON objects
    public Bay(JSONObject bayJson, String clusterId, JSONObject terminalJson) throws JSONException {
        this.bayId = bayJson.getString("bayId"); // Extract bayId from the JSON object
        this.bayName = bayJson.getString("bayName"); // Extract bayName 
        this.devices = new ArrayList<>(); // Initialize the devices list

        // Add various types of devices to the bay
        addDevices(terminalJson, clusterId, bayId, "dutDevice", ConstantConveyor.DEVICE_TYPE_DUT);
        addDevices(terminalJson, clusterId, bayId, "megaOhmMeter", "Mega Ohm Meter");
        addDevices(terminalJson, clusterId, bayId, "voltMeter", "Volt Meter");
        addDevices(terminalJson, clusterId, bayId, "ldu", "LDU");
        addDevices(terminalJson, clusterId, bayId, "qrScanner", "QR Scanner");
        
        // Add input and output ports to the bay
        addPorts(terminalJson, clusterId, bayId, "outputPort", "Output Port");
        addPorts(terminalJson, clusterId, bayId, "inputPort", "Input Port");
        ApplicationLauncher.logger.debug("Bay : clusterId " + clusterId);
        ApplicationLauncher.logger.debug("Bay : bayId: " + bayId);
        ApplicationLauncher.logger.debug("Bay : devices.size: " + devices.size());
       // getDutDeviceList();
    }

    // Adds devices of a specific type to the bay based on JSON data
    private void addDevices(JSONObject terminalJson, String clusterId, String bayId, String deviceType, String typeDescription) throws JSONException {
        if (terminalJson.has(deviceType)) { // Check if the JSON contains the device type
            JSONArray deviceArray = terminalJson.getJSONArray(deviceType); // Get the array of devices
            for (int i = 0; i < deviceArray.length(); i++) {
                JSONObject deviceJson = deviceArray.getJSONObject(i);
                // Check if the device matches the given clusterId and bayId
                if (deviceJson.getString("clusterId").equals(clusterId) && deviceJson.getString("bayId").equals(bayId)) {
                    devices.add(new Device(deviceJson, typeDescription)); // Add the device to the list
                }
            }
        }
    }

    // Adds ports of a specific type to the bay based on JSON data
    private void addPorts(JSONObject terminalJson, String clusterId, String bayId, String portType, String typeDescription) throws JSONException {
        if (terminalJson.has(portType)) { // Check if the JSON contains the port type
            JSONArray portArray = terminalJson.getJSONArray(portType); // Get the array of ports
            for (int i = 0; i < portArray.length(); i++) {
                JSONObject portJson = portArray.getJSONObject(i);
                // Check if the port matches the given clusterId and bayId
                if (portJson.getString("clusterId").equals(clusterId) && portJson.getString("bayId").equals(bayId)) {
                    devices.add(new Device(portJson, typeDescription)); // Add the port to the list as a device
                }
            }
        }
    }

    // Retrieves a list of devices filtered by their type
    public List<Device> getDevicesByType(String deviceType) {
        List<Device> filteredDevices = new ArrayList<>(); // List to store filtered devices
        for (Device device : devices) {
            if (device.getType().equals(deviceType)) { // Check if the device matches the type
                filteredDevices.add(device);
            }
        }
        return filteredDevices; // Return the filtered list
    }
    
    public List<Device> getDutDeviceList() {
    	String deviceType = ConstantConveyor.DEVICE_TYPE_DUT;
        List<Device> filteredDevices = new ArrayList<>(); // List to store filtered devices
        for (Device device : this.devices) {
        	//ApplicationLauncher.logger.debug("getDutDeviceList: device-1: " + device.getName());
            if (device.getType().equals(deviceType)) { // Check if the device matches the type
                filteredDevices.add(device);
                //ApplicationLauncher.logger.debug("getDutDeviceList: device-2: " + device.getName());
            }
        }
        return filteredDevices; // Return the filtered list
    }

    // Returns the unique identifier of the bay
    public String getBayId() {
        return bayId;
    }

	public String getBayName() {
		return bayName;
	}

	public void setBayName(String bayName) {
		this.bayName = bayName;
	}
}
