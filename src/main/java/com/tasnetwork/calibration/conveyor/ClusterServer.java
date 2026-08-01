package com.tasnetwork.calibration.conveyor;

public class ClusterServer {

	
    public static String HTTP_Protocol="http://";
    private String ipAddress="";
    private String port="999";
	private String rootUrl="";
	private String clusterId;
	
	
	/*public ClusterServer(){
		
	}*/
	public ClusterServer(String ip_address,String ip_port){
		this.ipAddress = ip_address;
		this.port = ip_port;
		//this.clusterId = ip_cluster_id;
		setRootUrl();
	}
	
	public ClusterServer(String ip_address,String ip_port,String ip_cluster_id){
		this.ipAddress = ip_address;
		this.port = ip_port;
		this.clusterId = ip_cluster_id;
		setRootUrl();
	}
	
	
	
	public  String  getRootUrl(){
    	return rootUrl;
    }
	
	public String getClusterId() {
		return clusterId;
	}
    
    public  void setRootUrl(){
    	rootUrl = HTTP_Protocol +
				ipAddress+":"+port;
    	
    }
    
    public  void setRootUrl(String ip_address,String ip_port){
    	
    	this.ipAddress = ip_address;
		this.port = ip_port;
    	rootUrl = HTTP_Protocol +
				ipAddress+":"+port;
    	
    }

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String ipPort) {
		this.port = ipPort;
	}
}
