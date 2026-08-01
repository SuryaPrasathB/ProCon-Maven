package com.tasnetwork.calibration.conveyor.tree;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyTerminal {
    
	private List<Cluster> clusters;  // List of clusters associated with this terminal

    // Constructor to initialize the Terminal object using the provided JSON data
    
    public MyTerminal(){
    	
    }
    public MyTerminal(JSONObject terminalJson) throws JSONException {
    	terminalJson.getString("TerminalId");
        terminalJson.getString("TerminalName");
        
        // Initialize the clusters list
        this.clusters = new ArrayList<>();

        // Get the "ClusterDetails" JSON array and iterate over it to initialize each cluster
        JSONArray clusterArray = terminalJson.getJSONArray("ClusterDetails");
        for (int i = 0; i < clusterArray.length(); i++) {
            // Create a new Cluster object for each element in the "ClusterDetails" array
            clusters.add(new Cluster(clusterArray.getJSONObject(i), terminalJson));
        }
    }
    
   

    // Getter method to retrieve the list of clusters in this terminal
    public List<Cluster> getClusters() {
        return clusters;
    }
}
