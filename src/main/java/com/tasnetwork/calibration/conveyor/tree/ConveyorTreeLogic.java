package com.tasnetwork.calibration.conveyor.tree;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConveyorTreeLogic {

    private MyTerminal terminalObj; // Object representing the terminal
    private String lastErrorMessage; // Holds the last error message

    public ConveyorTreeLogic() {

    }

    // Constructor that initializes the terminal object using a configuration file
    public ConveyorTreeLogic(String configFilePath) throws IOException, JSONException {
        // Read and parse the JSON configuration file
        String jsonContent = new String(Files.readAllBytes(Paths.get(configFilePath)));
        JSONObject jsonObject = new JSONObject(jsonContent);

        // Extract and initialize the terminal object from the JSON content
        JSONArray terminalArray = jsonObject.getJSONArray("Terminal");
        terminalObj = new MyTerminal(terminalArray.getJSONObject(0));
    }

    public void init(String configFilePath) {
        ApplicationLauncher.logger.debug("ConveyorTreeLogic :init: Entry");
        String jsonContent;
        try {
            jsonContent = new String(Files.readAllBytes(Paths.get(configFilePath)));
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(jsonContent);
                try {
                    JSONArray terminalArray = jsonObject.getJSONArray("Terminal");
                    this.terminalObj = new MyTerminal(terminalArray.getJSONObject(0));
                    terminalObj.getClusters().stream().forEach(e -> {
                        ApplicationLauncher.logger.debug("ConveyorTreeLogic :init: getClusterId: " + e.getClusterId()
                                + " -> " + e.getClusterName());
                        e.getBays().stream().forEach(e1 -> {
                            ApplicationLauncher.logger
                                    .debug("ConveyorTreeLogic :init: bay: " + e1.getBayId() + " -> " + e1.getBayName());
                            // ApplicationLauncher.logger.debug("ConveyorTreeLogic :init: bay:
                            // getDutDeviceList: size: "+ e1.getDutDeviceList().size());

                            e1.getDutDeviceList().stream().forEach(e2 -> {
                                ApplicationLauncher.logger
                                        .debug("ConveyorTreeLogic :init: dutDevices: " + e2.getName());
                            });
                        });
                    });
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            } catch (JSONException e) {

                e.printStackTrace();
            }
        } catch (IOException e1) {

            e1.printStackTrace();
        }

        // Extract and initialize the terminal object from the JSON content

        // terminalObj = new MyTerminal(terminalArray.getJSONObject(0));
    }

    // Method to retrieve a list of device data based on the cluster, bay, and
    // device type
    public List<DeviceData> getDeviceDataList(String clusterId, String bayId, String deviceType) {
        lastErrorMessage = null; // Reset any previous error message

        Cluster selectedCluster = null;

        // Find the cluster matching the given clusterId
        for (Cluster cluster : terminalObj.getClusters()) {
            if (cluster.getClusterId().equals(clusterId)) {
                selectedCluster = cluster;
                break;
            }
        }

        // If cluster is not found, set error message and return null
        if (selectedCluster == null) {
            lastErrorMessage = "Invalid Cluster ID";
        }

        Bay selectedBay = null;

        // Find the bay matching the given bayId within the selected cluster
        for (Bay bay : selectedCluster.getBays()) {
            if (bay.getBayId().equals(bayId)) {
                selectedBay = bay;
                break;
            }
        }

        // If bay is not found, set error message and return null
        if (selectedBay == null) {
            lastErrorMessage = "Invalid Bay ID in this Cluster";
        }

        // List of valid device types in the correct order
        List<String> orderedTypes = Arrays.asList(
                "DUT Device", "Mega Ohm Meter", "Volt Meter", "LDU", "QR Scanner", "Output Port", "Input Port");

        // If the device type is valid, retrieve and return the corresponding devices
        if (orderedTypes.contains(deviceType)) {
            List<Device> devicesToPrint = selectedBay.getDevicesByType(deviceType);
            List<DeviceData> deviceDataList = new ArrayList<>();

            // If devices are found, map them to device data objects
            if (!devicesToPrint.isEmpty()) {
                for (Device device : devicesToPrint) {
                    deviceDataList
                            .add(new DeviceData(clusterId, bayId, device.getName(), device.getId(), device.getType()));
                }
                return deviceDataList; // Return the list of device data
            } else {
                // If no devices of the specified type are found, set error message
                lastErrorMessage = "No " + deviceType + "s found in this bay.";
                return null;
            }
        } else {
            // If device type is invalid, set error message
            lastErrorMessage = "Invalid Device Type.";
            return null;
        }
    }

    // Method to get all clusters from the terminal
    public List<Cluster> getClusters() {
        return terminalObj.getClusters();
    }

    // Method to get all bays within a specific cluster by clusterId
    public List<Bay> getBays(String clusterId) {
        for (Cluster cluster : terminalObj.getClusters()) {
            if (cluster.getClusterId().equals(clusterId)) {
                return cluster.getBays(); // Return the bays of the selected cluster
            }
        }
        return null; // Return null if clusterId is not found
    }

    // Method to get devices of a specific type in a specific bay and cluster
    public List<Device> getDevices(String clusterId, String bayId, String deviceType) {
        for (Cluster cluster : terminalObj.getClusters()) {
            if (cluster.getClusterId().equals(clusterId)) {
                for (Bay bay : cluster.getBays()) {
                    if (bay.getBayId().equals(bayId)) {
                        return bay.getDevicesByType(deviceType); // Return devices of the specified type
                    }
                }
            }
        }
        return null; // Return null if no matching devices are found
    }

    // Method to get the last error message
    public String getLastErrorMessage() {
        return lastErrorMessage;
    }
}
