package com.tasnetwork.calibration.conveyor.bay.configloader;



import java.util.List;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tasnetwork.calibration.conveyor.tree.Device;

@Generated("jsonschema2pojo")
public class Bay {

	@SerializedName("bayId")
	@Expose
	private String bayId;
	@SerializedName("bayName")
	@Expose
	private String bayName;
	
	private List<Device> devices;
	
	public String getBayId() {
		return bayId;
	}
	public String getBayName() {
		return bayName;
	}
	public void setBayId(String bayId) {
		this.bayId = bayId;
	}
	public void setBayName(String bayName) {
		this.bayName = bayName;
	}
	public List<Device> getDevices() {
		return devices;
	}
	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}
}