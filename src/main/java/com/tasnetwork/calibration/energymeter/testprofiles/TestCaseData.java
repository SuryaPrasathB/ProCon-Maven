package com.tasnetwork.calibration.energymeter.testprofiles;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class TestCaseData {

	private SimpleStringProperty name;
	private SimpleFloatProperty errorMin;
	private SimpleFloatProperty errorMax;
	private SimpleIntegerProperty pulse;
	private SimpleIntegerProperty average;
	private SimpleIntegerProperty times;
	private SimpleIntegerProperty skipreadingcount;
	private SimpleFloatProperty deviation;
	private SimpleStringProperty testruntype;


	public TestCaseData(final String name, float e_min, float e_max, int pulse,int avg, int times,
			int SkipReadingCount, float deviation, String testruntype) {
		this.name = new SimpleStringProperty(name);
		this.errorMin = new SimpleFloatProperty(e_min);
		this.errorMax = new SimpleFloatProperty(e_max);
		this.pulse = new SimpleIntegerProperty(pulse);
		this.average = new SimpleIntegerProperty(avg);
		this.times = new SimpleIntegerProperty(times);
		this.skipreadingcount = new SimpleIntegerProperty(SkipReadingCount);
		this.deviation = new SimpleFloatProperty(deviation);
		this.testruntype = new SimpleStringProperty(testruntype);
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public Float getErrorMin() {
		return errorMin.get();
	}

	public void setErrorMin(Float e_min) {
		this.errorMin.set(e_min);
	}

	public Float getErrorMax() {
		return errorMax.get();
	}

	public void setErrorMax(Float e_max) {
		this.errorMax.set(e_max);
	}

	public Integer getPulse() {
		return pulse.get();
	}

	public void setPulse(Integer pulse) {
		this.pulse.set(pulse);
	}
	
	
	
	
	public Integer getAverage() {
		return average.get();
	}

	public void setAverage(Integer avg) {
		this.average.set(avg);
	}
	
	

	public Integer getTimes() {
		return times.get();
	}

	public void setTimes(Integer times) {
		ApplicationLauncher.logger.info("setTimes: " + times);
		this.times.set(times);
	}

	public int getSkipreadingcount() {
		return skipreadingcount.get();
	}

	public void setSkipreadingcount(int readingcount) {
		this.skipreadingcount.set(readingcount);
	}
	
	public Float getDeviation() {
		return deviation.get();
	}
	
	public String getTestRunType() {
		return testruntype.get();
	}

	public void setTestRunType(String runtype) {
		this.testruntype.set(runtype);
	}

	public void setDeviation(Float deviation) {
		this.deviation.set(deviation);
	}
	
	public SimpleStringProperty nameProperty() {
		return name;
	}
	public SimpleFloatProperty errorMinProperty() {
		return errorMin;
	}
	public SimpleFloatProperty errorMaxProperty() {
		return errorMax;
	}
	public SimpleIntegerProperty pulseProperty() {
		return pulse;
	}
	
	public SimpleIntegerProperty averageProperty() {
		return average;
	}
	
	
	
	public SimpleIntegerProperty countProperty() {
		return times;
	}
	public SimpleIntegerProperty skipreadingcountProperty() {
		return skipreadingcount;
	}
	public SimpleFloatProperty deviationProperty() {
		return deviation;
	}
	public SimpleStringProperty testruntypeProperty() {
		return testruntype;
	}
}
