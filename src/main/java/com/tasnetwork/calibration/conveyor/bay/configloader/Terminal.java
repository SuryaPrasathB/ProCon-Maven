package com.tasnetwork.calibration.conveyor.bay.configloader;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Terminal {

	@SerializedName("TerminalId")
	@Expose
	private String terminalId;
	@SerializedName("TerminalName")
	@Expose
	private String terminalName;
	@SerializedName("NoOfClusters")
	@Expose
	private String noOfClusters;
	@SerializedName("TotalNoOfBays")
	@Expose
	private String totalNoOfBays;
	@SerializedName("ClusterDetails")
	@Expose
	private List<ClusterDetail> clusterDetails;
	@SerializedName("outputPort")
	@Expose
	private List<OutputPort> outputPort;
	
	@SerializedName("qrScanner")
	@Expose
	private List<QrScanner> qrScanner;
	
	
	@SerializedName("megaOhmMeter")
	@Expose
	private List<MegaOhmMeter> megaOhmMeter;
	
	
	@SerializedName("ldu")
	@Expose
	private List<Ldu> ldu;
	
	
	
	
	@SerializedName("voltMeter")
	@Expose
	private List<VoltMeter> voltMeter;
	
	@SerializedName("dutDevice")
	@Expose
	private List<DutDevice> dutDevice;
	
	@SerializedName("inputPort")
	@Expose
	private List<InputPort> inputPort;
	
	
	public String getTerminalId() {
		return terminalId;
	}
	public String getTerminalName() {
		return terminalName;
	}
	public String getNoOfClusters() {
		return noOfClusters;
	}
	public String getTotalNoOfBays() {
		return totalNoOfBays;
	}
	public List<ClusterDetail> getClusterDetails() {
		return clusterDetails;
	}
	public List<OutputPort> getOutputPort() {
		return outputPort;
	}
	
	public List<QrScanner> getQrScanner() {
		return qrScanner;
	}
	
	public List<MegaOhmMeter> getMegaOhmMeter() {
		return megaOhmMeter;
	}
	public List<DutDevice> getDutDevice() {
		return dutDevice;
	}
	public List<InputPort> getInputPort() {
		return inputPort;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}
	public void setNoOfClusters(String noOfClusters) {
		this.noOfClusters = noOfClusters;
	}
	public void setTotalNoOfBays(String totalNoOfBays) {
		this.totalNoOfBays = totalNoOfBays;
	}
	public void setClusterDetails(List<ClusterDetail> clusterDetails) {
		this.clusterDetails = clusterDetails;
	}
	public void setOutputPort(List<OutputPort> outputPort) {
		this.outputPort = outputPort;
	}
	
	public void setQrScanner(List<QrScanner> qr_scanner) {
		this.qrScanner = qr_scanner;
	}
	
	public void setMegaOhmMeter(List<MegaOhmMeter> megaOhmMeter) {
		this.megaOhmMeter = megaOhmMeter;
	}
	
	
	public void setDutDevice(List<DutDevice> dut_device) {
		this.dutDevice = dut_device;
	}
	
	public void setInputPort(List<InputPort> inputPort) {
		this.inputPort = inputPort;
	}
	public List<VoltMeter> getVoltMeter() {
		return voltMeter;
	}
	public void setVoltMeter(List<VoltMeter> voltMeter) {
		this.voltMeter = voltMeter;
	}
	public List<Ldu> getLdu() {
		return ldu;
	}
	public void setLdu(List<Ldu> ldu) {
		this.ldu = ldu;
	}

}