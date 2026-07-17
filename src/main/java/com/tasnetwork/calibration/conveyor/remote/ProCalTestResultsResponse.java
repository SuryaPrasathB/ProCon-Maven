package com.tasnetwork.calibration.conveyor.remote;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProCalTestResultsResponse {

	private int No_of_results;
    private List<Result> Results;
	public int getNo_of_results() {
		return No_of_results;
	}
	public List<Result> getResults() {
		return Results;
	}
	public void setNo_of_results(int no_of_results) {
		No_of_results = no_of_results;
	}
	public void setResults(List<Result> results) {
		Results = results;
	}


}

