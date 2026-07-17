package com.tasnetwork.calibration.conveyor.tree;
import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

public class Device {
    
    private String id;  // Device ID
    private String name;  // Device name
    private String type;  // Device type

    // Constructor to initialize the device using the JSON object and type
    public Device(JSONObject json, String type) throws JSONException {
    	//ApplicationLauncher.logger.debug("Device : json: " + json.toString());
    	//ApplicationLauncher.logger.debug("Device : type: " + type);
        this.type = type;

        // Check if the JSON object contains "deviceId" or "portId" and initialize id and name accordingly
        if (json.has("deviceId")) {
            this.id = json.getString("deviceId");
            this.name = json.getString("portName");
        } else if (json.has("portId")) {
            this.id = json.getString("portId");
            this.name = json.getString("portName");
        } else {
            // If no valid ID is found, set default values
            this.id = "N/A";
            this.name = "N/A";
        }
    }

    // Getter method for the device type
    public String getType() {
        return this.type;
    }

    // Getter method for the device ID
    public String getId() {
        return this.id;
    }

    // Getter method for the device name
    public String getName() {
        return this.name;
    }

    // Method to print the device details in a structured format
    public void printTree() {
        System.out.println("                " + name + " (ID: " + id + ") (" + type + ")");
    }
}
