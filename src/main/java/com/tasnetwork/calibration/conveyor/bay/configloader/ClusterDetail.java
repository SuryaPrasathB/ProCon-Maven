package com.tasnetwork.calibration.conveyor.bay.configloader;


import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class ClusterDetail {

	@SerializedName("clusterId")
	@Expose
	private String clusterId;
	@SerializedName("Name")
	@Expose
	private String name;
	@SerializedName("ClusterIpAddress")
	@Expose
	private String clusterIpAddress;
	@SerializedName("ClusterPortId")
	@Expose
	private String clusterPortId;
	@SerializedName("bay")
	@Expose
	private List<Bay> bay;
	
	
	public String getClusterId() {
		return clusterId;
	}
	public String getName() {
		return name;
	}
	public String getClusterIpAddress() {
		return clusterIpAddress;
	}
	public String getClusterPortId() {
		return clusterPortId;
	}
	public List<Bay> getBay() {
		return bay;
	}
	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setClusterIpAddress(String clusterIpAddress) {
		this.clusterIpAddress = clusterIpAddress;
	}
	public void setClusterPortId(String clusterPortId) {
		this.clusterPortId = clusterPortId;
	}
	public void setBay(List<Bay> bay) {
		this.bay = bay;
	}

}
