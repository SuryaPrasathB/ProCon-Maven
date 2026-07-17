package com.tasnetwork.calibration.conveyor.bay.configloader;


import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class OutputPort {

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
	@SerializedName("idleState")
	@Expose
	private String idleState;
	
	@SerializedName("positionId")
	@Expose
	private String positionId = "8888";
	
	@SerializedName("onStateDesc")
	@Expose
	private String onStateDesc = "On";
	
	@SerializedName("offStateDesc")
	@Expose
	private String offStateDesc = "Off";
	
	private boolean outputActive = false;
	
	private boolean updateBay = false;
	
	private String serialNo = "";
	
	@SerializedName("stateDescription")
	@Expose
	private String stateDescription = "";
	
	public OutputPort clone() {
        OutputPort copy = new OutputPort();
        copy.serialNo = this.serialNo;
        copy.outputActive = this.outputActive;
        copy.stateDescription = this.stateDescription;
        copy.onStateDesc = this.onStateDesc;
        copy.offStateDesc = this.offStateDesc;
        copy.clusterId = this.clusterId;
        copy.bayId = this.bayId;
        copy.portName= this.portName;
        copy.portId= this.portId;
        copy.positionId = this.positionId;
        return copy;
    }
	
	
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
	public String getPositionId() {
		return positionId;
	}
	public String getOnStateDesc() {
		return onStateDesc;
	}
	public String getOffStateDesc() {
		return offStateDesc;
	}
	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}
	public void setOnStateDesc(String onStateDesc) {
		this.onStateDesc = onStateDesc;
	}
	public void setOffStateDesc(String offStateDesc) {
		this.offStateDesc = offStateDesc;
	}
	public String getStateDescription() {
		return stateDescription;
	}
	public void setStateDescription(String stateDescription) {
		this.stateDescription = stateDescription;
	}

}
