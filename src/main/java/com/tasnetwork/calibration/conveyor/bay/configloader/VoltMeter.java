package com.tasnetwork.calibration.conveyor.bay.configloader;




import java.util.ArrayList;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class VoltMeter {

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
	
	@SerializedName("deviceId")
	@Expose
	private String deviceId;
	
	@SerializedName("rs485Enabled")
	@Expose
	private boolean rs485Enabled = false;
	
	@SerializedName("rs485DeviceIdList")
	@Expose
	private ArrayList<String> rs485DeviceIdList = new ArrayList<String>();
	
	@SerializedName("initState")
	@Expose
	private String initState;
	@SerializedName("idleState")
	@Expose
	private String idleState;
	
	private boolean outputActive = false;
	
	private boolean updateBay = false;
	
	private String serialNo = "";
	
	@SerializedName("positionId")
	@Expose
	private String positionId;
	
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
	public String getIdleState() {
		return idleState;
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
	public void setIdleState(String idleState) {
		this.idleState = idleState;
	}
	public boolean isOutputActive() {
		return outputActive;
	}
	public boolean isUpdateBay() {
		return updateBay;
	}
	public void setOutputActive(boolean outputActive) {
		this.outputActive = outputActive;
	}
	public void setUpdateBay(boolean updateBay) {
		this.updateBay = updateBay;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public void setPositionId(String position_id) {
		this.positionId = position_id;
	}
	public String getPositionId() {
		return positionId;
	}
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
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
}

