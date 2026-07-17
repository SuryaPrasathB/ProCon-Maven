package com.tasnetwork.calibration.energymeter.custom1report;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class ExcelReportTestTypeFilter {

	@SerializedName("TestTypeFilter")
	@Expose
	private List<TestTypeFilter> testTypeFilter = null;

	public List<TestTypeFilter> getTestTypeFilter() {
		return testTypeFilter;
	}

	public void setTestTypeFilter(List<TestTypeFilter> testTypeFilter) {
		this.testTypeFilter = testTypeFilter;
	}

}