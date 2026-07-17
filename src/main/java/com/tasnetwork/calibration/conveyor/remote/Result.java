package com.tasnetwork.calibration.conveyor.remote;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Result {

	private String alias_id = "";
    private String deployment_id = "";
    private String device_name = "";
    private String dut_serial_no = "";
    private String error_max = "";
    private String error_min = "";
    private String error_value = "";
    private String executed_date = "";
    private String main_neutral_ct_mode = "";
    private String pallet_distinct_id = "";
    private String pallet_rack_position_no = "";
    private String result_id = "";
    private String run_id = "";
    private String test_case_name = "";
    private String test_status = "";
    
    
	public String getAlias_id() {
		return alias_id;
	}
	public String getDeployment_id() {
		return deployment_id;
	}
	public String getDevice_name() {
		return device_name;
	}
	public String getDut_serial_no() {
		return dut_serial_no;
	}
	public String getError_max() {
		return error_max;
	}
	public String getError_min() {
		return error_min;
	}
	public String getError_value() {
		return error_value;
	}
	public String getExecuted_date() {
		return executed_date;
	}
	public String getMain_neutral_ct_mode() {
		return main_neutral_ct_mode;
	}
	public String getPallet_distinct_id() {
		return pallet_distinct_id;
	}
	public String getPallet_rack_position_no() {
		return pallet_rack_position_no;
	}
	public String getResult_id() {
		return result_id;
	}
	public String getRun_id() {
		return run_id;
	}
	public String getTest_case_name() {
		return test_case_name;
	}
	public String getTest_status() {
		return test_status;
	}
	public void setAlias_id(String alias_id) {
		this.alias_id = alias_id;
	}
	public void setDeployment_id(String deployment_id) {
		this.deployment_id = deployment_id;
	}
	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}
	public void setDut_serial_no(String dut_serial_no) {
		this.dut_serial_no = dut_serial_no;
	}
	public void setError_max(String error_max) {
		this.error_max = error_max;
	}
	public void setError_min(String error_min) {
		this.error_min = error_min;
	}
	public void setError_value(String error_value) {
		this.error_value = error_value;
	}
	public void setExecuted_date(String executed_date) {
		this.executed_date = executed_date;
	}
	public void setMain_neutral_ct_mode(String main_neutral_ct_mode) {
		this.main_neutral_ct_mode = main_neutral_ct_mode;
	}
	public void setPallet_distinct_id(String pallet_distinct_id) {
		this.pallet_distinct_id = pallet_distinct_id;
	}
	public void setPallet_rack_position_no(String pallet_rack_position_no) {
		this.pallet_rack_position_no = pallet_rack_position_no;
	}
	public void setResult_id(String result_id) {
		this.result_id = result_id;
	}
	public void setRun_id(String run_id) {
		this.run_id = run_id;
	}
	public void setTest_case_name(String test_case_name) {
		this.test_case_name = test_case_name;
	}
	public void setTest_status(String test_status) {
		this.test_status = test_status;
	}
}

