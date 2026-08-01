package com.tasnetwork.calibration.conveyor.tree;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Represents a Cluster which contains multiple Bays
class Cluster {
    private String clusterId; // Unique identifier for the cluster
    private String clusterName;
    private String clusterIpAddress;
	private String clusterPortId;
    private List<Bay> bays; // List of bays associated with the cluster

    // Constructor to initialize Cluster using JSON data
    public Cluster(JSONObject clusterJson, JSONObject terminalJson) throws JSONException {
        this.clusterId = clusterJson.getString("clusterId"); // Extract cluster ID from JSON
        this.clusterName = clusterJson.getString("Name"); // Extract cluster name 
        this.clusterIpAddress = clusterJson.getString("ClusterIpAddress"); 
        this.clusterPortId = clusterJson.getString("ClusterPortId"); 
        this.bays = new ArrayList<>(); // Initialize the list of bays

        // Populate the bays list from the JSON array
        JSONArray bayArray = clusterJson.getJSONArray("bay");
        for (int i = 0; i < bayArray.length(); i++) {
            bays.add(new Bay(bayArray.getJSONObject(i), clusterJson.getString("clusterId"), terminalJson)); // Create and add Bay objects
        }
    }

    // Returns the unique identifier of the cluster
    public String getClusterId() {
        return clusterId;
    }

    // Retrieves the list of bays in the cluster
    public List<Bay> getBays() {
        return bays;
    }

	public String getClusterName() {
		return clusterName;
	}

	public String getClusterIpAddress() {
		return clusterIpAddress;
	}

	public String getClusterPortId() {
		return clusterPortId;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public void setClusterIpAddress(String clusterIpAddress) {
		this.clusterIpAddress = clusterIpAddress;
	}

	public void setClusterPortId(String clusterPortId) {
		this.clusterPortId = clusterPortId;
	}
}
