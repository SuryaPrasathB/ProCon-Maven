package com.tasnetwork.calibration.conveyor.bay.configloader;

import java.util.ArrayList;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Ldu {

	@SerializedName("clusterId")
	@Expose
	private String clusterId;
	@SerializedName("bayId")
	@Expose
	private String bayId;
	@SerializedName("portId")
	@Expose
	private String portId;
	@SerializedName("portName")
	@Expose
	private String portName;
	@SerializedName("initState")
	@Expose
	private String initState;
	
	@SerializedName("positionId")
	@Expose
	private String positionId="7777";
	
	
	@SerializedName("deviceId")
	@Expose
	private String deviceId;
	
	@SerializedName("rs485Enabled")
	@Expose
	private boolean rs485Enabled = false;
	
	@SerializedName("rs485DeviceIdList")
	@Expose
	private ArrayList<String> rs485DeviceIdList = new ArrayList<String>();
	
	private boolean inputActive = false;
	
	private boolean readBay = false;
	
	private String serialNo = "";
	
	
	
	public String getClusterId() {
		return clusterId;
	}
	public String getBayId() {
		return bayId;
	}
	public String getPortId() {
		return portId;
	}
	public String getPortName() {
		return portName;
	}
	public String getInitState() {
		return initState;
	}
	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}
	public void setBayId(String bayId) {
		this.bayId = bayId;
	}
	public void setPortId(String portId) {
		this.portId = portId;
	}
	public void setPortName(String portName) {
		this.portName = portName;
	}
	public void setInitState(String initState) {
		this.initState = initState;
	}
	public boolean isInputActive() {
		return inputActive;
	}
	public boolean isReadBay() {
		return readBay;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setInputActive(boolean inputActive) {
		this.inputActive = inputActive;
	}
	public void setReadBay(boolean readBay) {
		this.readBay = readBay;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getPositionId() {
		return positionId;
	}
	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}
	public boolean isRs485Enabled() {
		return rs485Enabled;
	}
	public ArrayList<String> getRs485DeviceIdList() {
		return rs485DeviceIdList;
	}
	public void setRs485Enabled(boolean rs485Enabled) {
		this.rs485Enabled = rs485Enabled;
	}
	public void setRs485DeviceIdList(ArrayList<String> rs485DeviceIdList) {
		this.rs485DeviceIdList = rs485DeviceIdList;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

}
