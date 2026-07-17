package com.tasnetwork.calibration.conveyor.bay.configloader;





import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class DutDevice {

	@SerializedName("clusterId")
	@Expose
	private String clusterId;
	@SerializedName("bayId")
	@Expose
	private String bayId;
/*	@SerializedName("portId")
	@Expose
	private String portId;*/
	@SerializedName("portName")
	@Expose
	private String portName;
	
	@SerializedName("deviceId")
	@Expose
	private String deviceId;
	
	@SerializedName("initState")
	@Expose
	private String initState;
	@SerializedName("idleState")
	@Expose
	private String idleState;
	
	@SerializedName("positionId")
	@Expose
	private String positionId;
	
	private boolean outputActive = false;
	
	private boolean updateBay = false;
	
	private String serialNo = "";
	
	
	public String getClusterId() {
		return clusterId;
	}
	public String getBayId() {
		return bayId;
	}
	public String getPositionId() {
		return positionId;
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
	public void setPositionId(String position_id) {
		this.positionId = position_id;
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
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

}


