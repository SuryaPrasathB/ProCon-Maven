package com.tasnetwork.calibration.conveyor.bay;

 
public class IoPortInfo {
 private String portId = "op04";
 private String clusterId = "1";
 private String bayId = "2";

 public IoPortInfo(String portId, String clusterId, String bayId) {
     this.portId = portId;
     this.clusterId = clusterId;
     this.bayId = bayId;
 }

 // Getters for each field
 public String getPortId() { return portId; }
 public String getClusterId() { return clusterId; }
 public String getBayId() { return bayId; }
}
