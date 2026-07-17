package com.tasnetwork.spring.orm.model;



import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class DutExecutionResult {

    private final SimpleIntegerProperty serialNo = new SimpleIntegerProperty();
    private final SimpleStringProperty testPointName = new SimpleStringProperty();
    private final SimpleStringProperty testPointExecutionStatus = new SimpleStringProperty();

    private final SimpleStringProperty resultPosition1 = new SimpleStringProperty();
    private final SimpleStringProperty resultPosition2 = new SimpleStringProperty();
    private final SimpleStringProperty resultPosition3 = new SimpleStringProperty();
    private final SimpleStringProperty resultPosition4 = new SimpleStringProperty();
    private final SimpleStringProperty resultPosition5 = new SimpleStringProperty();
    private final SimpleStringProperty resultPosition6 = new SimpleStringProperty();
    
    private final String resultValuePosition1 = "";
    private final String resultValuePosition2 = "";
    private final String resultValuePosition3 = "";
    private final String resultValuePosition4 = "";
    private final String resultValuePosition5 = "";
    private final String resultValuePosition6 = "";
    
    private final String resultStatusPosition1 = "";
    private final String resultStatusPosition2 = "";
    private final String resultStatusPosition3 = "";
    private final String resultStatusPosition4 = "";
    private final String resultStatusPosition5 = "";
    private final String resultStatusPosition6 = "";
    
    private boolean isSummaryRow = false;
/*    private String summaryPosition1;
    private String summaryPosition2;
    private String summaryPosition3;
    private String summaryPosition4;
    private String summaryPosition5;
    private String summaryPosition6;*/

    private final SimpleStringProperty palletDistinctId = new SimpleStringProperty();
    
    private DutCommand dutCommand = new DutCommand();

    // Constructor
    public DutExecutionResult() {}
    
    public DutExecutionResult(boolean isSummaryRow) {
        this.isSummaryRow = isSummaryRow;
    }

    public DutExecutionResult(int serialNo, String testPointName, String status,
                                   String resultPosition1, String resultPosition2, String resultPosition3, String resultPosition4,
                                   String resultPosition5, String resultPosition6, String palletDistinctId) {
        this.serialNo.set(serialNo);
        this.testPointName.set(testPointName);
        this.testPointExecutionStatus.set(status);
        this.resultPosition1.set(resultPosition1);
        this.resultPosition2.set(resultPosition2);
        this.resultPosition3.set(resultPosition3);
        this.resultPosition4.set(resultPosition4);
        this.resultPosition5.set(resultPosition5);
        this.resultPosition6.set(resultPosition6);
        this.palletDistinctId.set(palletDistinctId);
    }
    
    

    // Getters & Property accessors
    
    public boolean isSummaryRow() {
        return isSummaryRow;
    }
    
    public void setSummaryRow(boolean summaryRow) {
        isSummaryRow = summaryRow;
    }
    
    // Getters and setters for summary columns
    /*public String getSummaryPosition1() {
        return summaryPosition1;
    }
    
    public void setSummaryPosition1(String summaryPosition1) {
        this.summaryPosition1 = summaryPosition1;
    }
    
    // Repeat for positions 2-6...
    public String getSummaryPosition2() { return summaryPosition2; }
    public void setSummaryPosition2(String summaryPosition2) { this.summaryPosition2 = summaryPosition2; }
    public String getSummaryPosition3() { return summaryPosition3; }
    public void setSummaryPosition3(String summaryPosition3) { this.summaryPosition3 = summaryPosition3; }
    public String getSummaryPosition4() { return summaryPosition4; }
    public void setSummaryPosition4(String summaryPosition4) { this.summaryPosition4 = summaryPosition4; }
    public String getSummaryPosition5() { return summaryPosition5; }
    public void setSummaryPosition5(String summaryPosition5) { this.summaryPosition5 = summaryPosition5; }
    public String getSummaryPosition6() { return summaryPosition6; }
    public void setSummaryPosition6(String summaryPosition6) { this.summaryPosition6 = summaryPosition6; }
    */
    // Helper method to get result based on row type
    public String getResultForPosition(int position) {
/*        if (isSummaryRow) {
            switch (position) {
                case 1: return summaryPosition1;
                case 2: return summaryPosition2;
                case 3: return summaryPosition3;
                case 4: return summaryPosition4;
                case 5: return summaryPosition5;
                case 6: return summaryPosition6;
                default: return "";
            }
        } else {*/
            switch (position) {
                case 1: return getResultPosition1();
                case 2: return getResultPosition2();
                case 3: return getResultPosition3();
                case 4: return getResultPosition4();
                case 5: return getResultPosition5();
                case 6: return getResultPosition6();
                default: return "";
            }
        //}
    }
    
    
    public int getSerialNo() { return serialNo.get(); }
    public void setSerialNo(int value) { serialNo.set(value); }
    public SimpleIntegerProperty serialNoProperty() { return serialNo; }

    public String getTestPointName() { return testPointName.get(); }
    public void setTestPointName(String value) { testPointName.set(value); }
    public SimpleStringProperty testPointNameProperty() { return testPointName; }

    public String getTestPointExecutionStatus() { return testPointExecutionStatus.get(); }
    public void setTestPointExecutionStatus(String value) { testPointExecutionStatus.set(value); }
    public SimpleStringProperty testPointExecutionStatusProperty() { return testPointExecutionStatus; }

    public String getResultPosition1() { return resultPosition1.get(); }
    public void setResultPosition1(String value) { resultPosition1.set(value); }
    public SimpleStringProperty resultPosition1Property() { return resultPosition1; }

    public String getResultPosition2() { return resultPosition2.get(); }
    public void setResultPosition2(String value) { resultPosition2.set(value); }
    public SimpleStringProperty resultPosition2Property() { return resultPosition2; }

    public String getResultPosition3() { return resultPosition3.get(); }
    public void setResultPosition3(String value) { resultPosition3.set(value); }
    public SimpleStringProperty resultPosition3Property() { return resultPosition3; }

    public String getResultPosition4() { return resultPosition4.get(); }
    public void setResultPosition4(String value) { resultPosition4.set(value); }
    public SimpleStringProperty resultPosition4Property() { return resultPosition4; }

    public String getResultPosition5() { return resultPosition5.get(); }
    public void setResultPosition5(String value) { resultPosition5.set(value); }
    public SimpleStringProperty resultPosition5Property() { return resultPosition5; }

    public String getResultPosition6() { return resultPosition6.get(); }
    public void setResultPosition6(String value) { resultPosition6.set(value); }
    public SimpleStringProperty resultPosition6Property() { return resultPosition6; }

    public String getPalletDistinctId() { return palletDistinctId.get(); }
    public void setPalletDistinctId(String value) { palletDistinctId.set(value); }
    public SimpleStringProperty palletDistinctIdProperty() { return palletDistinctId; }

	public String getResultValuePosition1() {
		return resultValuePosition1;
	}

	public String getResultValuePosition2() {
		return resultValuePosition2;
	}

	public String getResultValuePosition3() {
		return resultValuePosition3;
	}

	public String getResultValuePosition4() {
		return resultValuePosition4;
	}

	public String getResultValuePosition5() {
		return resultValuePosition5;
	}

	public String getResultValuePosition6() {
		return resultValuePosition6;
	}

	public String getResultStatusPosition1() {
		return resultStatusPosition1;
	}

	public String getResultStatusPosition2() {
		return resultStatusPosition2;
	}

	public String getResultStatusPosition3() {
		return resultStatusPosition3;
	}

	public String getResultStatusPosition4() {
		return resultStatusPosition4;
	}

	public String getResultStatusPosition5() {
		return resultStatusPosition5;
	}

	public String getResultStatusPosition6() {
		return resultStatusPosition6;
	}

	public DutCommand getDutCommand() {
		return dutCommand;
	}

	public void setDutCommand(DutCommand dutCommand) {
		this.dutCommand = dutCommand;
	}
}

