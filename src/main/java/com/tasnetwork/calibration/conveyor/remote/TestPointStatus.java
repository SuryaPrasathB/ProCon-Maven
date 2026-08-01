package com.tasnetwork.calibration.conveyor.remote;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
//Main class
public class TestPointStatus {
	    @JsonProperty("presentTestPointId")
	    private String presentTestPointId="";
	    
	    @JsonProperty("allTestExecutionCompleted")
	    private boolean allTestExecutionCompleted=false;
	    
	    @JsonProperty("totalTpCount")
	    private int totalTestPointCount=0;
	    
	    @JsonProperty("completedTpCount")
	    private int completedTestPointCount=-2;
	    
	    @JsonProperty("totalTestingTimeInSec")
	    private int totalTestingTimeInSec=0;
	    
	    @JsonProperty("currentCircuitOpenDetectedInBatch")
	    private boolean currentCircuitOpenDetectedInBatch=false;
	    
	    @JsonProperty("faultDetectedInContiguousBatch")
	    private boolean faultDetectedInContiguousBatch=false;
	    
	    private boolean allTestExecutionCompletedCheck=false;
	    
	    @JsonProperty("presentTpResult")
	    private ArrayList<TestResult> presentTpResult = new ArrayList<TestResult> ();

	    // Getters and Setters
	    public String getPresentTestPointId() {
	        return presentTestPointId;
	    }

	    public void setPresentTestPointId(String presentTestPointId) {
	        this.presentTestPointId = presentTestPointId;
	    }

	    public boolean isAllTestExecutionCompleted() {
	        return allTestExecutionCompleted;
	    }

	    public void setAllTestExecutionCompleted(boolean testExecutionStatus) {
	        this.allTestExecutionCompleted = testExecutionStatus;
	    }

	    public ArrayList<TestResult> getPresentTpResult() {
	        return presentTpResult;
	    }

	    public void setPresentTpResult(ArrayList<TestResult> presentTpResult) {
	        this.presentTpResult = presentTpResult;
	    }

		public boolean isCurrentCircuitOpenDetectedInBatch() {
			return currentCircuitOpenDetectedInBatch;
		}

		public boolean isFaultDetectedInContiguousBatch() {
			return faultDetectedInContiguousBatch;
		}

		public boolean isAllTestExecutionCompletedCheck() {
			return allTestExecutionCompletedCheck;
		}

		public void setCurrentCircuitOpenDetectedInBatch(boolean currentCircuitOpenDetectedInBatch) {
			this.currentCircuitOpenDetectedInBatch = currentCircuitOpenDetectedInBatch;
		}

		public void setFaultDetectedInContiguousBatch(boolean faultDetectedInContiguousBatch) {
			this.faultDetectedInContiguousBatch = faultDetectedInContiguousBatch;
		}

		public void setAllTestExecutionCompletedCheck(boolean allTestExecutionCompletedCheck) {
			this.allTestExecutionCompletedCheck = allTestExecutionCompletedCheck;
		}
		
		@Override
		public String toString() {
		    StringBuilder sb = new StringBuilder();
		    sb.append("TestPointStatus {\n");
		    sb.append("  presentTestPointId: ").append(presentTestPointId).append(",\n");
		    sb.append("  allTestExecutionCompleted: ").append(allTestExecutionCompleted).append(",\n");
		    sb.append("  allTestExecutionCompletedCheck: ").append(allTestExecutionCompletedCheck).append(",\n");
		    sb.append("  currentCircuitOpenDetectedInBatch: ").append(currentCircuitOpenDetectedInBatch).append(",\n");
		    sb.append("  faultDetectedInContiguousBatch: ").append(faultDetectedInContiguousBatch).append(",\n");
		    sb.append("  totalTestPointCount: ").append(totalTestPointCount).append(",\n");
		    sb.append("  completedTestPointCount: ").append(completedTestPointCount).append(",\n");
		    sb.append("  totalTestingTimeInSec: ").append(totalTestingTimeInSec).append(",\n");
		    sb.append("  presentTpResult: [\n");

		    for (TestResult result : presentTpResult) {
		        sb.append("    ").append(result.toString()).append(",\n");
		    }

		    sb.append("  ]\n");
		    sb.append("}");
		    return sb.toString();
		}

		public int getTotalTestPointCount() {
			return totalTestPointCount;
		}

		public int getCompletedTestPointCount() {
			return completedTestPointCount;
		}

		public void setTotalTestPointCount(int totalTestPointCount) {
			this.totalTestPointCount = totalTestPointCount;
		}

		public void setCompletedTestPointCount(int completedTestPointCount) {
			this.completedTestPointCount = completedTestPointCount;
		}

		public int getTotalTestingTimeInSec() {
			return totalTestingTimeInSec;
		}

		public void setTotalTestingTimeInSec(int totalTestingTimeInSec) {
			this.totalTestingTimeInSec = totalTestingTimeInSec;
		}
	}

	// Sub-class for test results
	