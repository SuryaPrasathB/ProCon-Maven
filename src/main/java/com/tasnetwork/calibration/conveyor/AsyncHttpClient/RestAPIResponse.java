package com.tasnetwork.calibration.conveyor.AsyncHttpClient;

public class RestAPIResponse {
    private String Message;
    private String statuscode;

    private String firmwareversion;
    private Integer Order_ID;
    private String testid;
    private String errorvalue;
    private String teststatus;
    private String refStdStatus;
    private Boolean occurEnabled;
    private Boolean restoreEnabled;
    private String port1;
    private String port2;
    private String[] Available_drives;
    private String[] zipfiles_in_drive;
    private String Enable_deploy;
    private String Deploy_status;
    private String[] NetworkList;
    private String device_name;
    private String[] available_log_folders;
    private String[] logfiles;
    private String logfilecontent;
    private String[] all_timezones;
    //private List available_drive;
    public Integer getOrder_ID(){
        return Order_ID;
    }
    public void setOrder_ID(Integer input){
        this.Order_ID = input;
    }

    public String[] getNetworkList(){
        return NetworkList;
    }
    public String getFirmwareVersion(){
        return firmwareversion;
    }

    public String[] getAvailableDrives(){
        //Available_drives = jsonObject.optString[]("Available_drives");
        //System.out.println("entering lstamper_Scan_Available_drives:Available_drives:"+Available_drives.toS);
        return Available_drives;
    }
    public void setAvailableDrives(String[] input){
        this.Available_drives = input;
    }

    public String[] getAvailableTimeZones(){
        return all_timezones;
    }
    public void setAvailableTimeZones(String[] input){
        this.all_timezones = input;
    }

    public String[] getAvailableLogFolders(){
        return available_log_folders;
    }
    public void setAvailableLogFolders(String[] input){
        this.available_log_folders = input;
    }

    public String[] getAvailableFilesinLogFolder(){
        return logfiles;
    }
    public void setAvailableFilesinLogFolder(String[] input){
        this.logfiles = input;
    }
   /* public void set_available_drives{
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Available_drives", Available_drives);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }*/
   public String getviewlogcontent(){
       return logfilecontent;
   }
   public void setviewlogcontent(String input){
        this.logfilecontent = input;
    }

   public String[] getAvailableFilesinDrives(){
       //Available_drives = jsonObject.optString[]("Available_drives");
       //System.out.println("entering lstamper_Scan_Available_drives:Available_drives:"+Available_drives.toS);
       return zipfiles_in_drive;
   }
    public void setAvailableFilesinDrives(String[] input){
        this.zipfiles_in_drive = input;
    }

    public String getValidationResult(){
        return Enable_deploy;
    }
    public void setValidationResult(String input){
        this.Enable_deploy = input;
    }

    public String getDeployResult(){
        return Deploy_status;
    }
    public void setDeployResult(String input){
        this.Deploy_status = input;
    }
    public void setFirmwareVersion(String input){
        this.firmwareversion = input;
    }

    public String getport1(){
        return port1;
    }
    public void setport1(String input){
        this.port1 = input;
    }

    public String getport2(){
        return port2;
    }
    public void setport2(String input){
        this.port2 = input;
    }

    public String getMessage(){
        return Message;
    }
    public void setMessage(String input){
        this.Message = input;
    }
    public String getStatusCode(){
        return statuscode;
    }
    public String getDevice_name(){
        return device_name;
    }
    public void setStatusCode(String  input){
        this.statuscode = input;
    }

    public String getTestid(){
        return testid;
    }
    public void setTestid(String input){
        this.testid = input;
    }

    public String getErrorvalue(){
        return errorvalue;
    }
    public void setErrorvalue(String input){
        this.errorvalue = input;
    }

    public String getTeststatus(){
        return teststatus;
    }
    public void setTeststatus(String input){
        this.teststatus = input;
    }

    public Boolean getOccurEnabled(){
        return occurEnabled;
    }
    public void setOccurEnabled(Boolean input){
        this.occurEnabled = input;
    }

    public Boolean getRestoreEnabled(){
        return restoreEnabled;
    }
    public void setRestoreEnabled(Boolean input){
        this.restoreEnabled = input;
    }
	public String getRefStdStatus() {
		return refStdStatus;
	}
	public void setRefStdStatus(String refStdStatus) {
		this.refStdStatus = refStdStatus;
	}

}
