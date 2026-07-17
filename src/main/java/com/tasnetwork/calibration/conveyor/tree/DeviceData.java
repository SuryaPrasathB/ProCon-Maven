package com.tasnetwork.calibration.conveyor.tree;
public class DeviceData {

    private String clusterId;  // Cluster ID to which the device belongs
    private String bayId;      // Bay ID where the device is located
    private String name;       // Name of the device
    private String id;         // Unique ID of the device
    private String type;       // Type of the device

    // Constructor to initialize a DeviceData object with provided values
    public DeviceData(String clusterId, String bayId, String name, String id, String type) {
        this.clusterId = clusterId;
        this.bayId = bayId;
        this.name = name;
        this.id = id;
        this.type = type;
    }

    // Getter method to retrieve the cluster ID
    public String getClusterId() {
        return clusterId;
    }

    // Getter method to retrieve the bay ID
    public String getBayId() {
        return bayId;
    }

    // Getter method to retrieve the device name
    public String getName() {
        return name;
    }

    // Getter method to retrieve the device ID
    public String getId() {
        return id;
    }

    // Getter method to retrieve the device type
    public String getType() {
        return type;
    }
}
