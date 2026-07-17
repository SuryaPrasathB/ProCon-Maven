package com.tasnetwork.calibration.energymeter.constant;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConveyorConfigModel {

	@SerializedName("ConfigFileVersion")
	@Expose
	private String configFileVersion;
	
	@SerializedName("ReportSerialNoPrefix")
	@Expose
	private String ReportSerialNoPrefix;
	
	@SerializedName("ReportSerialNoRegexSourceFormat")
	@Expose
	private String reportSerialNoRegexSourceFormat;
	
	
	@SerializedName("ReportSerialNoRegexTargetFormat")
	@Expose
	private String reportSerialNoRegexTargetFormat;
	
	@SerializedName("DutWriteSerialNoPassword")
	@Expose
	private String dutWriteSerialNoPassword;

	@SerializedName("MaxDutSupported")
	@Expose
	private int maxDutSupported;
	
	
	@SerializedName("HvExecutionTime_InSec")
	@Expose
	private int hvExecutionTime_InSec;
	
	@SerializedName("IrExecutionTime_InSec")
	@Expose
	private int irExecutionTime_InSec;
	
	
	@SerializedName("Sta1ExecutionTime_InSec")
	@Expose
	private int sta1ExecutionTime_InSec;
	
	@SerializedName("Sta2ExecutionTime_InSec")
	@Expose
	private int sta2ExecutionTime_InSec;
	
	@SerializedName("CalibSuperCapacitorChargeWaitTimeInSec")
	@Expose
	private int calibSuperCapacitorChargeWaitTimeInSec;
	

	
	@SerializedName("TotalTestPointInSta1")
	@Expose
	private int totalTestPointInSta1;
	
	@SerializedName("TotalTestPointInSta2")
	@Expose
	private int totalTestPointInSta2;
	
	@SerializedName("TotalTestPointInVerific1")
	@Expose
	private int totalTestPointInVerific1;
	


	@SerializedName("MaxNoOfPalletsInVerific1")
	@Expose
	private int maxNoOfPalletsInVerific1;
	
	@SerializedName("MaxNoOfPalletsInStaNld1")
	@Expose
	private int maxNoOfPalletsInStaNld1;
	
	
	@SerializedName("MaxNoOfPalletsInStaNld2")
	@Expose
	private int maxNoOfPalletsInStaNld2;
	
	@SerializedName("MaxNoOfPalletsInWaitingVerific1")
	@Expose
	private int maxNoOfPalletsInWaitingVerific1;
	
	
	@SerializedName("FtRelayOnPhaseCurrentMinAccepted")
	@Expose
	private float ftRelayOnPhaseCurrentMinAccepted;
	
	@SerializedName("FtRelayOnNeutralCurrentMinAccepted")
	@Expose
	private float ftRelayOnNeutralCurrentMinAccepted;
	
	@SerializedName("FtSetDutToDefaultCalibrationTest")
	@Expose
	private boolean ftSetDutToDefaultCalibrationTest = false;
	
	
	@SerializedName("FtDutCmdTestProjectName")
	@Expose
	private String ftDutCmdTestProjectName;
	
	@SerializedName("FtDutCmdEachTpSeqExecuteMode")
	@Expose
	private boolean ftDutCmdEachTpSeqExecuteMode;
	
	@SerializedName("FtDutCmdEachTpBufferTimeInSec")
	@Expose
	private int ftDutCmdEachTpBufferTimeInSec;
	
	
/*	@SerializedName("CustomerName")
	@Expose
	private String customerName;*/
	
	public boolean isFtDutCmdEachTpSeqExecuteMode() {
		return ftDutCmdEachTpSeqExecuteMode;
	}

	public void setFtDutCmdEachTpSeqExecuteMode(boolean ftDutCmdEachTpSeqExecuteMode) {
		this.ftDutCmdEachTpSeqExecuteMode = ftDutCmdEachTpSeqExecuteMode;
	}

	@SerializedName("PostDelaySta1ExitOpenStopLatch1_InSec")
	@Expose
	private int postDelaySta1ExitOpenStopLatch1_InSec;
	
	@SerializedName("DutHardwareIdInitialValue")
	@Expose
	private String dutHardwareIdInitialValue;
	
	

	@SerializedName("PostDelaySta1ExitCloseStopLatch1_InSec")
	@Expose
	private int postDelaySta1ExitCloseStopLatch1_InSec;
	
	@SerializedName("PostDelaySta1ExitOpenStopLatch2_InSec")
	@Expose
	private int postDelaySta1ExitOpenStopLatch2_InSec;
	
	@SerializedName("PostDelaySta1ExitCloseStopLatch2_InSec")
	@Expose
	private int postDelaySta1ExitCloseStopLatch2_InSec;
	
	@SerializedName("Sta1LastPalletExitWaitTime_InSec")
	@Expose
	private int sta1LastPalletExitWaitTime_InSec;
	
	@SerializedName("UnloadingPalletEmptyStableDetectionWaitTime_InSec")
	@Expose
	private int unloadingPalletEmptyStableDetectionWaitTime_InSec;
	
	@SerializedName("UnloadingQrScanningWaitTime_InSec")
	@Expose
	private int unloadingQrScanningWaitTime_InSec;

	@SerializedName("OpticalReadingInterfaceEnabled")
	@Expose
	private boolean opticalReadingInterfaceEnabled;

	@SerializedName("DutSerialInterfaceEnabled")
	@Expose
	private boolean dutSerialInterfaceEnabled;
	
	@SerializedName("UseDutSerialNoAsReportSerialNo")
	@Expose
	private boolean useDutSerialNoAsReportSerialNo;

	@SerializedName("TemplateFileLocationPath")
	@Expose
	private String templateFileLocationPath;

	@SerializedName("ReportOutputPath")
	@Expose
	private String reportOutputPath;
	
	@SerializedName("ReportProcessSummaryOverAllStatusTestTypeWhiteList")
	@Expose
	private List<String> reportProcessSummaryOverAllStatusTestTypeWhiteList;
	
	@SerializedName("ReportPrintSummaryTestTypeWhiteList")
	@Expose
	private List<String> reportPrintSummaryTestTypeWhiteList;
	
	
	@SerializedName("ReportPrintDetailedTestTypeWhiteList")
	@Expose
	private List<String> reportPrintDetailedTestTypeWhiteList;
	
	@SerializedName("ReportPrintDetailedTestPointWhiteList")
	@Expose
	private List<String> reportPrintDetailedTestPointWhiteList;

	@SerializedName("ReportResultDataCellPosition")
	@Expose
	private ReportResultDataCellPosition reportResultDataCellPosition;

	@SerializedName("ResultDataDisplay")
	@Expose
	private ResultDataDisplay resultDataDisplay;


	@SerializedName("ResultDataPreAndPostFixDisplay")
	@Expose
	private ResultDataPreAndPostFixDisplay resultDataPreAndPostFixDisplay;


	@SerializedName("ResultDataPreAndPostFixValue")
	@Expose
	private ResultDataPreAndPostFixValue resultDataPreAndPostFixValue;


/*	@SerializedName("ResultDataPreFixDisplay")
	@Expose
	private ResultDataPreFixDisplay resultDataPreFixDisplay;

	@SerializedName("ResultDataPreFixValue")
	@Expose
	private ResultDataPreFixValue resultDataPreFixValue;*/


	@SerializedName("MeterProfileReportCellPosition")
	@Expose
	private MeterProfileReportCellPosition meterProfileReportCellPosition;

	@SerializedName("MeterProfileReportDisplay")
	@Expose
	private MeterProfileReportDisplay meterProfileReportDisplay;
	
	
    @SerializedName("MeterProfileReportPreAndPostFixValue")
    @Expose
    private MeterProfileReportPreAndPostFixValue meterProfileReportPreAndPostFixValue;
	
	@SerializedName("NoLoadReportCellPosition")
	@Expose
	private NoLoadReportCellPosition noLoadReportCellPosition;

	@SerializedName("NoLoadReportDisplay")
	@Expose
	private NoLoadReportDisplay noLoadReportDisplay;

	@SerializedName("NoLoadReportResultValue")
	@Expose
	private NoLoadReportResultValue noLoadReportResultValue;
	
	
	@SerializedName("StartingCurrentReportCellPosition")
	@Expose
	private StartingCurrentReportCellPosition startingCurrentReportCellPosition;

	@SerializedName("StartingCurrentReportDisplay")
	@Expose
	private StartingCurrentReportDisplay startingCurrentReportDisplay;

	@SerializedName("StartingCurrentReportResultValue")
	@Expose
	private StartingCurrentReportResultValue startingCurrentReportResultValue;
	
	
	
	@SerializedName("FunctionalTestReportCellPosition")
	@Expose
	private FunctionalTestReportCellPosition functionalTestReportCellPosition;

	@SerializedName("FunctionalTestReportDisplay")
	@Expose
	private FunctionalTestReportDisplay functionalTestReportDisplay;

	@SerializedName("FunctionalTestReportResultValue")
	@Expose
	private FunctionalTestReportResultValue functionalTestReportResultValue;
	
    @SerializedName("HighVoltageReportCellPosition")
    @Expose
    private HighVoltageReportCellPosition highVoltageReportCellPosition;

    @SerializedName("HighVoltageReportDisplay")
    @Expose
    private HighVoltageReportDisplay highVoltageReportDisplay;

    @SerializedName("HighVoltageReportResultValue")
    @Expose
    private HighVoltageReportResultValue highVoltageReportResultValue;
    
    
	
    @SerializedName("InsulationResistanceReportCellPosition")
    @Expose
    private InsulationResistanceReportCellPosition insulationResistanceReportCellPosition;
    
    @SerializedName("InsulationResistanceReportResultValue")
    @Expose
    private InsulationResistanceReportResultValue insulationResistanceReportResultValue;

    @SerializedName("InsulationResistanceReportDisplay")
    @Expose
    private InsulationResistanceReportDisplay insulationResistanceReportDisplay;
    
    
    @SerializedName("RoutineSummaryReportCellPosition")
    @Expose
    private RoutineSummaryReportCellPosition routineSummaryReportCellPosition;

    @SerializedName("RoutineSummaryReportDisplay")
    @Expose
    private RoutineSummaryReportDisplay routineSummaryReportDisplay;

    @SerializedName("RoutineSummaryReportResultValue")
    @Expose
    private RoutineSummaryReportResultValue routineSummaryReportResultValue;
	
	

	    public NoLoadReportCellPosition getNoLoadReportCellPosition() {
	        return noLoadReportCellPosition;
	    }

	    public void setNoLoadReportCellPosition(NoLoadReportCellPosition noLoadReportCellPosition) {
	        this.noLoadReportCellPosition = noLoadReportCellPosition;
	    }

	    public NoLoadReportDisplay getNoLoadReportDisplay() {
	        return noLoadReportDisplay;
	    }

	    public void setNoLoadReportDisplay(NoLoadReportDisplay noLoadReportDisplay) {
	        this.noLoadReportDisplay = noLoadReportDisplay;
	    }

	    public NoLoadReportResultValue getNoLoadReportResultValue() {
	        return noLoadReportResultValue;
	    }

	    public void setNoLoadReportResultValue(NoLoadReportResultValue noLoadReportResultValue) {
	        this.noLoadReportResultValue = noLoadReportResultValue;
	    }

	public String getConfigFileVersion() {
		return configFileVersion;
	}

	public int getMaxDutSupported() {
		return maxDutSupported;
	}

	public boolean isOpticalReadingInterfaceEnabled() {
		return opticalReadingInterfaceEnabled;
	}

	public boolean isDutSerialInterfaceEnabled() {
		return dutSerialInterfaceEnabled;
	}

	public String getTemplateFileLocationPath() {
		return templateFileLocationPath;
	}

	public String getReportOutputPath() {
		return reportOutputPath;
	}

	public ReportResultDataCellPosition getReportResultDataCellPosition() {
		return reportResultDataCellPosition;
	}

	public ResultDataDisplay getResultDataDisplay() {
		return resultDataDisplay;
	}

	public MeterProfileReportCellPosition getMeterProfileReportCellPosition() {
		return meterProfileReportCellPosition;
	}

	public MeterProfileReportDisplay getMeterProfileReportDisplay() {
		return meterProfileReportDisplay;
	}

	public void setConfigFileVersion(String configFileVersion) {
		this.configFileVersion = configFileVersion;
	}

	public void setMaxDutSupported(int maxDutSupported) {
		this.maxDutSupported = maxDutSupported;
	}

	public void setOpticalReadingInterfaceEnabled(boolean opticalReadingInterfaceEnabled) {
		this.opticalReadingInterfaceEnabled = opticalReadingInterfaceEnabled;
	}

	public void setDutSerialInterfaceEnabled(boolean dutSerialInterfaceEnabled) {
		this.dutSerialInterfaceEnabled = dutSerialInterfaceEnabled;
	}

	public void setTemplateFileLocationPath(String templateFileLocationPath) {
		this.templateFileLocationPath = templateFileLocationPath;
	}

	public void setReportOutputPath(String reportOutputPath) {
		this.reportOutputPath = reportOutputPath;
	}

	public void setReportResultDataCellPosition(ReportResultDataCellPosition reportResultDataCellPosition) {
		this.reportResultDataCellPosition = reportResultDataCellPosition;
	}

	public void setResultDataDisplay(ResultDataDisplay resultDataDisplay) {
		this.resultDataDisplay = resultDataDisplay;
	}

	public void setMeterProfileReportCellPosition(MeterProfileReportCellPosition meterProfileReportCellPosition) {
		this.meterProfileReportCellPosition = meterProfileReportCellPosition;
	}

	public void setMeterProfileReportDisplay(MeterProfileReportDisplay meterProfileReportDisplay) {
		this.meterProfileReportDisplay = meterProfileReportDisplay;
	}
	//}
	
	

	public class InsulationResistanceReportCellPosition {
	    @SerializedName("reportSerialNoCell")
	    @Expose
	    private String reportSerialNoCell;

	    @SerializedName("testerNameCell")
	    @Expose
	    private String testerNameCell;

	    @SerializedName("pageNumberCell")
	    @Expose
	    private String pageNumberCell;

	    @SerializedName("voltageCell")
	    @Expose
	    private String voltageCell;

	    @SerializedName("testPeriodCell")
	    @Expose
	    private String testPeriodCell;

	    @SerializedName("descriptionCell")
	    @Expose
	    private String descriptionCell;

	    @SerializedName("resultStatusCell")
	    @Expose
	    private String resultStatusCell;

	    @SerializedName("resultValueCell")
	    @Expose
	    private String resultValueCell;

	    // Getters and Setters
	    public String getReportSerialNoCell() {
	        return reportSerialNoCell;
	    }

	    public void setReportSerialNoCell(String reportSerialNoCell) {
	        this.reportSerialNoCell = reportSerialNoCell;
	    }

		public String getTesterNameCell() {
			return testerNameCell;
		}

		public String getPageNumberCell() {
			return pageNumberCell;
		}

		public String getVoltageCell() {
			return voltageCell;
		}

		public String getTestPeriodCell() {
			return testPeriodCell;
		}

		public String getDescriptionCell() {
			return descriptionCell;
		}

		public String getResultStatusCell() {
			return resultStatusCell;
		}

		public String getResultValueCell() {
			return resultValueCell;
		}

		public void setTesterNameCell(String testerNameCell) {
			this.testerNameCell = testerNameCell;
		}

		public void setPageNumberCell(String pageNumberCell) {
			this.pageNumberCell = pageNumberCell;
		}

		public void setVoltageCell(String voltageCell) {
			this.voltageCell = voltageCell;
		}

		public void setTestPeriodCell(String testPeriodCell) {
			this.testPeriodCell = testPeriodCell;
		}

		public void setDescriptionCell(String descriptionCell) {
			this.descriptionCell = descriptionCell;
		}

		public void setResultStatusCell(String resultStatusCell) {
			this.resultStatusCell = resultStatusCell;
		}

		public void setResultValueCell(String resultValueCell) {
			this.resultValueCell = resultValueCell;
		}


	}
	
	
	
	public class InsulationResistanceReportDisplay {
	    @SerializedName("displayInsulationResistanceInReport")
	    @Expose
	    private boolean displayInsulationResistanceInReport;

	    @SerializedName("displayReportSerialNo")
	    @Expose
	    private boolean displayReportSerialNo;

	    @SerializedName("displayTesterName")
	    @Expose
	    private boolean displayTesterName;

	    @SerializedName("displayPageNumber")
	    @Expose
	    private boolean displayPageNumber;

	    @SerializedName("displayVoltage")
	    @Expose
	    private boolean displayVoltage;

	    @SerializedName("displayTestPeriod")
	    @Expose
	    private boolean displayTestPeriod;

	    @SerializedName("displayDescription")
	    @Expose
	    private boolean displayDescription;

	    @SerializedName("displayResultStatus")
	    @Expose
	    private boolean displayResultStatus;

	    @SerializedName("displayResultStatusPrefix")
	    @Expose
	    private boolean displayResultStatusPrefix;

	    @SerializedName("displayResultStatusPostfix")
	    @Expose
	    private boolean displayResultStatusPostfix;

	    @SerializedName("displayOverRideResultStatus")
	    @Expose
	    private boolean displayOverRideResultStatus;

	    @SerializedName("displayResultValue")
	    @Expose
	    private boolean displayResultValue;

	    @SerializedName("displayResultValuePrefix")
	    @Expose
	    private boolean displayResultValuePrefix;

	    @SerializedName("displayResultValuePostfix")
	    @Expose
	    private boolean displayResultValuePostfix;

	    @SerializedName("displayOverRideResultValue")
	    @Expose
	    private boolean displayOverRideResultValue;

	    // Getters and Setters
	    public boolean isDisplayInsulationResistanceInReport() {
	        return displayInsulationResistanceInReport;
	    }

	    public void setDisplayInsulationResistanceInReport(boolean displayInsulationResistanceInReport) {
	        this.displayInsulationResistanceInReport = displayInsulationResistanceInReport;
	    }

		public boolean isDisplayReportSerialNo() {
			return displayReportSerialNo;
		}

		public boolean isDisplayTesterName() {
			return displayTesterName;
		}

		public boolean isDisplayPageNumber() {
			return displayPageNumber;
		}

		public boolean isDisplayVoltage() {
			return displayVoltage;
		}

		public boolean isDisplayTestPeriod() {
			return displayTestPeriod;
		}

		public boolean isDisplayDescription() {
			return displayDescription;
		}

		public boolean isDisplayResultStatus() {
			return displayResultStatus;
		}

		public boolean isDisplayResultStatusPrefix() {
			return displayResultStatusPrefix;
		}

		public boolean isDisplayResultStatusPostfix() {
			return displayResultStatusPostfix;
		}

		public boolean isDisplayOverRideResultStatus() {
			return displayOverRideResultStatus;
		}

		public boolean isDisplayResultValue() {
			return displayResultValue;
		}

		public boolean isDisplayResultValuePrefix() {
			return displayResultValuePrefix;
		}

		public boolean isDisplayResultValuePostfix() {
			return displayResultValuePostfix;
		}

		public boolean isDisplayOverRideResultValue() {
			return displayOverRideResultValue;
		}

		public void setDisplayReportSerialNo(boolean displayReportSerialNo) {
			this.displayReportSerialNo = displayReportSerialNo;
		}

		public void setDisplayTesterName(boolean displayTesterName) {
			this.displayTesterName = displayTesterName;
		}

		public void setDisplayPageNumber(boolean displayPageNumber) {
			this.displayPageNumber = displayPageNumber;
		}

		public void setDisplayVoltage(boolean displayVoltage) {
			this.displayVoltage = displayVoltage;
		}

		public void setDisplayTestPeriod(boolean displayTestPeriod) {
			this.displayTestPeriod = displayTestPeriod;
		}

		public void setDisplayDescription(boolean displayDescription) {
			this.displayDescription = displayDescription;
		}

		public void setDisplayResultStatus(boolean displayResultStatus) {
			this.displayResultStatus = displayResultStatus;
		}

		public void setDisplayResultStatusPrefix(boolean displayResultStatusPrefix) {
			this.displayResultStatusPrefix = displayResultStatusPrefix;
		}

		public void setDisplayResultStatusPostfix(boolean displayResultStatusPostfix) {
			this.displayResultStatusPostfix = displayResultStatusPostfix;
		}

		public void setDisplayOverRideResultStatus(boolean displayOverRideResultStatus) {
			this.displayOverRideResultStatus = displayOverRideResultStatus;
		}

		public void setDisplayResultValue(boolean displayResultValue) {
			this.displayResultValue = displayResultValue;
		}

		public void setDisplayResultValuePrefix(boolean displayResultValuePrefix) {
			this.displayResultValuePrefix = displayResultValuePrefix;
		}

		public void setDisplayResultValuePostfix(boolean displayResultValuePostfix) {
			this.displayResultValuePostfix = displayResultValuePostfix;
		}

		public void setDisplayOverRideResultValue(boolean displayOverRideResultValue) {
			this.displayOverRideResultValue = displayOverRideResultValue;
		}


	}
	
	
	

	public class InsulationResistanceReportResultValue {
	    @SerializedName("DescriptionPassValue")
	    @Expose
	    private String descriptionPassValue;

	    @SerializedName("DescriptionFailValue")
	    @Expose
	    private String descriptionFailValue;

	    @SerializedName("ResultValueOverRidePassValue")
	    @Expose
	    private String resultValueOverRidePassValue;

	    @SerializedName("ResultValueOverRideFailValue")
	    @Expose
	    private String resultValueOverRideFailValue;

	    @SerializedName("ResultValuePrefixValue")
	    @Expose
	    private String resultValuePrefixValue;

	    @SerializedName("ResultValuePostfixValue")
	    @Expose
	    private String resultValuePostfixValue;

	    @SerializedName("ResultStatusOverRidePassValue")
	    @Expose
	    private String resultStatusOverRidePassValue;

	    @SerializedName("ResultStatusOverRideFailValue")
	    @Expose
	    private String resultStatusOverRideFailValue;

	    @SerializedName("ResultStatusPrefixValue")
	    @Expose
	    private String resultStatusPrefixValue;

	    @SerializedName("ResultStatusPostfixValue")
	    @Expose
	    private String resultStatusPostfixValue;

	    // Getters and Setters
	    public String getDescriptionPassValue() {
	        return descriptionPassValue;
	    }

	    public void setDescriptionPassValue(String descriptionPassValue) {
	        this.descriptionPassValue = descriptionPassValue;
	    }

		public String getDescriptionFailValue() {
			return descriptionFailValue;
		}

		public String getResultValueOverRidePassValue() {
			return resultValueOverRidePassValue;
		}

		public String getResultValueOverRideFailValue() {
			return resultValueOverRideFailValue;
		}

		public String getResultValuePrefixValue() {
			return resultValuePrefixValue;
		}

		public String getResultValuePostfixValue() {
			return resultValuePostfixValue;
		}

		public String getResultStatusOverRidePassValue() {
			return resultStatusOverRidePassValue;
		}

		public String getResultStatusOverRideFailValue() {
			return resultStatusOverRideFailValue;
		}

		public String getResultStatusPrefixValue() {
			return resultStatusPrefixValue;
		}

		public String getResultStatusPostfixValue() {
			return resultStatusPostfixValue;
		}

		public void setDescriptionFailValue(String descriptionFailValue) {
			this.descriptionFailValue = descriptionFailValue;
		}

		public void setResultValueOverRidePassValue(String resultValueOverRidePassValue) {
			this.resultValueOverRidePassValue = resultValueOverRidePassValue;
		}

		public void setResultValueOverRideFailValue(String resultValueOverRideFailValue) {
			this.resultValueOverRideFailValue = resultValueOverRideFailValue;
		}

		public void setResultValuePrefixValue(String resultValuePrefixValue) {
			this.resultValuePrefixValue = resultValuePrefixValue;
		}

		public void setResultValuePostfixValue(String resultValuePostfixValue) {
			this.resultValuePostfixValue = resultValuePostfixValue;
		}

		public void setResultStatusOverRidePassValue(String resultStatusOverRidePassValue) {
			this.resultStatusOverRidePassValue = resultStatusOverRidePassValue;
		}

		public void setResultStatusOverRideFailValue(String resultStatusOverRideFailValue) {
			this.resultStatusOverRideFailValue = resultStatusOverRideFailValue;
		}

		public void setResultStatusPrefixValue(String resultStatusPrefixValue) {
			this.resultStatusPrefixValue = resultStatusPrefixValue;
		}

		public void setResultStatusPostfixValue(String resultStatusPostfixValue) {
			this.resultStatusPostfixValue = resultStatusPostfixValue;
		}


	}

	
	

	
	
	public class StartingCurrentReportCellPosition {
	    @SerializedName("reportSerialNoCell")
	    @Expose
	    private String reportSerialNoCell;

	    @SerializedName("testerNameCell")
	    @Expose
	    private String testerNameCell;

	    @SerializedName("pageNumberCell")
	    @Expose
	    private String pageNumberCell;

	    @SerializedName("voltageCell")
	    @Expose
	    private String voltageCell;

	    @SerializedName("testPeriodCell")
	    @Expose
	    private String testPeriodCell;

	    @SerializedName("descriptionCell")
	    @Expose
	    private String descriptionCell;

	    @SerializedName("resultStatusCell")
	    @Expose
	    private String resultStatusCell;

	    @SerializedName("resultValueCell")
	    @Expose
	    private String resultValueCell;

	    // Getters and Setters
	    public String getReportSerialNoCell() {
	        return reportSerialNoCell;
	    }

	    public void setReportSerialNoCell(String reportSerialNoCell) {
	        this.reportSerialNoCell = reportSerialNoCell;
	    }

	    public String getTesterNameCell() {
	        return testerNameCell;
	    }

	    public void setTesterNameCell(String testerNameCell) {
	        this.testerNameCell = testerNameCell;
	    }

	    public String getPageNumberCell() {
	        return pageNumberCell;
	    }

	    public void setPageNumberCell(String pageNumberCell) {
	        this.pageNumberCell = pageNumberCell;
	    }

	    public String getVoltageCell() {
	        return voltageCell;
	    }

	    public void setVoltageCell(String voltageCell) {
	        this.voltageCell = voltageCell;
	    }

	    public String getTestPeriodCell() {
	        return testPeriodCell;
	    }

	    public void setTestPeriodCell(String testPeriodCell) {
	        this.testPeriodCell = testPeriodCell;
	    }

	    public String getDescriptionCell() {
	        return descriptionCell;
	    }

	    public void setDescriptionCell(String descriptionCell) {
	        this.descriptionCell = descriptionCell;
	    }

	    public String getResultStatusCell() {
	        return resultStatusCell;
	    }

	    public void setResultStatusCell(String resultStatusCell) {
	        this.resultStatusCell = resultStatusCell;
	    }

	    public String getResultValueCell() {
	        return resultValueCell;
	    }

	    public void setResultValueCell(String resultValueCell) {
	        this.resultValueCell = resultValueCell;
	    }
	}
	
	public class StartingCurrentReportDisplay {
	    @SerializedName("displayStartCurrentInReport")
	    @Expose
	    private boolean displayStartCurrentInReport;

	    @SerializedName("displayReportSerialNo")
	    @Expose
	    private boolean displayReportSerialNo;

	    @SerializedName("displayTesterName")
	    @Expose
	    private boolean displayTesterName;

	    @SerializedName("displayPageNumber")
	    @Expose
	    private boolean displayPageNumber;

	    @SerializedName("displayVoltage")
	    @Expose
	    private boolean displayVoltage;

	    @SerializedName("displayTestPeriod")
	    @Expose
	    private boolean displayTestPeriod;

	    @SerializedName("displayDescription")
	    @Expose
	    private boolean displayDescription;

	    @SerializedName("displayResultStatus")
	    @Expose
	    private boolean displayResultStatus;

	    @SerializedName("displayResultStatusPrefix")
	    @Expose
	    private boolean displayResultStatusPrefix;

	    @SerializedName("displayResultStatusPostfix")
	    @Expose
	    private boolean displayResultStatusPostfix;

	    @SerializedName("displayOverRideResultStatus")
	    @Expose
	    private boolean displayOverRideResultStatus;

	    @SerializedName("displayResultValue")
	    @Expose
	    private boolean displayResultValue;

	    @SerializedName("displayResultValuePrefix")
	    @Expose
	    private boolean displayResultValuePrefix;

	    @SerializedName("displayResultValuePostfix")
	    @Expose
	    private boolean displayResultValuePostfix;

	    @SerializedName("displayOverRideResultValue")
	    @Expose
	    private boolean displayOverRideResultValue;

	    // Getters and Setters
	    public boolean isDisplayStartCurrentInReport() {
	        return displayStartCurrentInReport;
	    }

	    public void setDisplayStartCurrentInReport(boolean displayStartCurrentInReport) {
	        this.displayStartCurrentInReport = displayStartCurrentInReport;
	    }

		public boolean isDisplayReportSerialNo() {
			return displayReportSerialNo;
		}

		public boolean isDisplayTesterName() {
			return displayTesterName;
		}

		public boolean isDisplayPageNumber() {
			return displayPageNumber;
		}

		public boolean isDisplayVoltage() {
			return displayVoltage;
		}

		public boolean isDisplayTestPeriod() {
			return displayTestPeriod;
		}

		public boolean isDisplayDescription() {
			return displayDescription;
		}

		public boolean isDisplayResultStatus() {
			return displayResultStatus;
		}

		public boolean isDisplayResultStatusPrefix() {
			return displayResultStatusPrefix;
		}

		public boolean isDisplayResultStatusPostfix() {
			return displayResultStatusPostfix;
		}

		public boolean isDisplayOverRideResultStatus() {
			return displayOverRideResultStatus;
		}

		public boolean isDisplayResultValue() {
			return displayResultValue;
		}

		public boolean isDisplayResultValuePrefix() {
			return displayResultValuePrefix;
		}

		public boolean isDisplayResultValuePostfix() {
			return displayResultValuePostfix;
		}

		public boolean isDisplayOverRideResultValue() {
			return displayOverRideResultValue;
		}

		public void setDisplayReportSerialNo(boolean displayReportSerialNo) {
			this.displayReportSerialNo = displayReportSerialNo;
		}

		public void setDisplayTesterName(boolean displayTesterName) {
			this.displayTesterName = displayTesterName;
		}

		public void setDisplayPageNumber(boolean displayPageNumber) {
			this.displayPageNumber = displayPageNumber;
		}

		public void setDisplayVoltage(boolean displayVoltage) {
			this.displayVoltage = displayVoltage;
		}

		public void setDisplayTestPeriod(boolean displayTestPeriod) {
			this.displayTestPeriod = displayTestPeriod;
		}

		public void setDisplayDescription(boolean displayDescription) {
			this.displayDescription = displayDescription;
		}

		public void setDisplayResultStatus(boolean displayResultStatus) {
			this.displayResultStatus = displayResultStatus;
		}

		public void setDisplayResultStatusPrefix(boolean displayResultStatusPrefix) {
			this.displayResultStatusPrefix = displayResultStatusPrefix;
		}

		public void setDisplayResultStatusPostfix(boolean displayResultStatusPostfix) {
			this.displayResultStatusPostfix = displayResultStatusPostfix;
		}

		public void setDisplayOverRideResultStatus(boolean displayOverRideResultStatus) {
			this.displayOverRideResultStatus = displayOverRideResultStatus;
		}

		public void setDisplayResultValue(boolean displayResultValue) {
			this.displayResultValue = displayResultValue;
		}

		public void setDisplayResultValuePrefix(boolean displayResultValuePrefix) {
			this.displayResultValuePrefix = displayResultValuePrefix;
		}

		public void setDisplayResultValuePostfix(boolean displayResultValuePostfix) {
			this.displayResultValuePostfix = displayResultValuePostfix;
		}

		public void setDisplayOverRideResultValue(boolean displayOverRideResultValue) {
			this.displayOverRideResultValue = displayOverRideResultValue;
		}
	}
	
	
	public class StartingCurrentReportResultValue {
	    @SerializedName("DescriptionPassValue")
	    @Expose
	    private String descriptionPassValue;

	    @SerializedName("DescriptionFailValue")
	    @Expose
	    private String descriptionFailValue;

	    @SerializedName("ResultValueOverRidePassValue")
	    @Expose
	    private String resultValueOverRidePassValue;

	    @SerializedName("ResultValueOverRideFailValue")
	    @Expose
	    private String resultValueOverRideFailValue;

	    @SerializedName("ResultValuePrefixValue")
	    @Expose
	    private String resultValuePrefixValue;

	    @SerializedName("ResultValuePostfixValue")
	    @Expose
	    private String resultValuePostfixValue;

	    @SerializedName("ResultStatusOverRidePassValue")
	    @Expose
	    private String resultStatusOverRidePassValue;

	    @SerializedName("ResultStatusOverRideFailValue")
	    @Expose
	    private String resultStatusOverRideFailValue;

	    @SerializedName("ResultStatusPrefixValue")
	    @Expose
	    private String resultStatusPrefixValue;

	    @SerializedName("ResultStatusPostfixValue")
	    @Expose
	    private String resultStatusPostfixValue;

	    // Getters and Setters
	    public String getDescriptionPassValue() {
	        return descriptionPassValue;
	    }

	    public void setDescriptionPassValue(String descriptionPassValue) {
	        this.descriptionPassValue = descriptionPassValue;
	    }

		public String getDescriptionFailValue() {
			return descriptionFailValue;
		}

		public String getResultValueOverRidePassValue() {
			return resultValueOverRidePassValue;
		}

		public String getResultValueOverRideFailValue() {
			return resultValueOverRideFailValue;
		}

		public String getResultValuePrefixValue() {
			return resultValuePrefixValue;
		}

		public String getResultValuePostfixValue() {
			return resultValuePostfixValue;
		}

		public String getResultStatusOverRidePassValue() {
			return resultStatusOverRidePassValue;
		}

		public String getResultStatusOverRideFailValue() {
			return resultStatusOverRideFailValue;
		}

		public String getResultStatusPrefixValue() {
			return resultStatusPrefixValue;
		}

		public String getResultStatusPostfixValue() {
			return resultStatusPostfixValue;
		}

		public void setDescriptionFailValue(String descriptionFailValue) {
			this.descriptionFailValue = descriptionFailValue;
		}

		public void setResultValueOverRidePassValue(String resultValueOverRidePassValue) {
			this.resultValueOverRidePassValue = resultValueOverRidePassValue;
		}

		public void setResultValueOverRideFailValue(String resultValueOverRideFailValue) {
			this.resultValueOverRideFailValue = resultValueOverRideFailValue;
		}

		public void setResultValuePrefixValue(String resultValuePrefixValue) {
			this.resultValuePrefixValue = resultValuePrefixValue;
		}

		public void setResultValuePostfixValue(String resultValuePostfixValue) {
			this.resultValuePostfixValue = resultValuePostfixValue;
		}

		public void setResultStatusOverRidePassValue(String resultStatusOverRidePassValue) {
			this.resultStatusOverRidePassValue = resultStatusOverRidePassValue;
		}

		public void setResultStatusOverRideFailValue(String resultStatusOverRideFailValue) {
			this.resultStatusOverRideFailValue = resultStatusOverRideFailValue;
		}

		public void setResultStatusPrefixValue(String resultStatusPrefixValue) {
			this.resultStatusPrefixValue = resultStatusPrefixValue;
		}

		public void setResultStatusPostfixValue(String resultStatusPostfixValue) {
			this.resultStatusPostfixValue = resultStatusPostfixValue;
		}
	}
	
	
	
	
	





    public  class HighVoltageReportCellPosition {
        @SerializedName("reportSerialNoCell")
        @Expose
        private String reportSerialNoCell;

        @SerializedName("testerNameCell")
        @Expose
        private String testerNameCell;

        @SerializedName("pageNumberCell")
        @Expose
        private String pageNumberCell;

        @SerializedName("voltageCell")
        @Expose
        private String voltageCell;

        @SerializedName("testPeriodCell")
        @Expose
        private String testPeriodCell;

        @SerializedName("descriptionCell")
        @Expose
        private String descriptionCell;

        @SerializedName("resultStatusCell")
        @Expose
        private String resultStatusCell;

        @SerializedName("resultValueCell")
        @Expose
        private String resultValueCell;

		public String getReportSerialNoCell() {
			return reportSerialNoCell;
		}

		public String getTesterNameCell() {
			return testerNameCell;
		}

		public String getPageNumberCell() {
			return pageNumberCell;
		}

		public String getVoltageCell() {
			return voltageCell;
		}

		public String getTestPeriodCell() {
			return testPeriodCell;
		}

		public String getDescriptionCell() {
			return descriptionCell;
		}

		public String getResultStatusCell() {
			return resultStatusCell;
		}

		public String getResultValueCell() {
			return resultValueCell;
		}

		public void setReportSerialNoCell(String reportSerialNoCell) {
			this.reportSerialNoCell = reportSerialNoCell;
		}

		public void setTesterNameCell(String testerNameCell) {
			this.testerNameCell = testerNameCell;
		}

		public void setPageNumberCell(String pageNumberCell) {
			this.pageNumberCell = pageNumberCell;
		}

		public void setVoltageCell(String voltageCell) {
			this.voltageCell = voltageCell;
		}

		public void setTestPeriodCell(String testPeriodCell) {
			this.testPeriodCell = testPeriodCell;
		}

		public void setDescriptionCell(String descriptionCell) {
			this.descriptionCell = descriptionCell;
		}

		public void setResultStatusCell(String resultStatusCell) {
			this.resultStatusCell = resultStatusCell;
		}

		public void setResultValueCell(String resultValueCell) {
			this.resultValueCell = resultValueCell;
		}

        // Getters and Setters
        // ... (similar to above)
    }

    public  class HighVoltageReportDisplay {
        @SerializedName("displayHighVoltageInReport")
        @Expose
        private boolean displayHighVoltageInReport;

        @SerializedName("displayReportSerialNo")
        @Expose
        private boolean displayReportSerialNo;

        @SerializedName("displayTesterName")
        @Expose
        private boolean displayTesterName;

        @SerializedName("displayPageNumber")
        @Expose
        private boolean displayPageNumber;

        @SerializedName("displayVoltage")
        @Expose
        private boolean displayVoltage;

        @SerializedName("displayTestPeriod")
        @Expose
        private boolean displayTestPeriod;

        @SerializedName("displayDescription")
        @Expose
        private boolean displayDescription;

        @SerializedName("displayResultStatus")
        @Expose
        private boolean displayResultStatus;

        @SerializedName("displayResultStatusPrefix")
        @Expose
        private boolean displayResultStatusPrefix;

        @SerializedName("displayResultStatusPostfix")
        @Expose
        private boolean displayResultStatusPostfix;

        @SerializedName("displayOverRideResultStatus")
        @Expose
        private boolean displayOverRideResultStatus;

        @SerializedName("displayResultValue")
        @Expose
        private boolean displayResultValue;

        @SerializedName("displayResultValuePrefix")
        @Expose
        private boolean displayResultValuePrefix;

        @SerializedName("displayResultValuePostfix")
        @Expose
        private boolean displayResultValuePostfix;

        @SerializedName("displayOverRideResultValue")
        @Expose
        private boolean displayOverRideResultValue;

		public boolean isDisplayHighVoltageInReport() {
			return displayHighVoltageInReport;
		}

		public boolean isDisplayReportSerialNo() {
			return displayReportSerialNo;
		}

		public boolean isDisplayTesterName() {
			return displayTesterName;
		}

		public boolean isDisplayPageNumber() {
			return displayPageNumber;
		}

		public boolean isDisplayVoltage() {
			return displayVoltage;
		}

		public boolean isDisplayTestPeriod() {
			return displayTestPeriod;
		}

		public boolean isDisplayDescription() {
			return displayDescription;
		}

		public boolean isDisplayResultStatus() {
			return displayResultStatus;
		}

		public boolean isDisplayResultStatusPrefix() {
			return displayResultStatusPrefix;
		}

		public boolean isDisplayResultStatusPostfix() {
			return displayResultStatusPostfix;
		}

		public boolean isDisplayOverRideResultStatus() {
			return displayOverRideResultStatus;
		}

		public boolean isDisplayResultValue() {
			return displayResultValue;
		}

		public boolean isDisplayResultValuePrefix() {
			return displayResultValuePrefix;
		}

		public boolean isDisplayResultValuePostfix() {
			return displayResultValuePostfix;
		}

		public boolean isDisplayOverRideResultValue() {
			return displayOverRideResultValue;
		}

		public void setDisplayHighVoltageInReport(boolean displayHighVoltageInReport) {
			this.displayHighVoltageInReport = displayHighVoltageInReport;
		}

		public void setDisplayReportSerialNo(boolean displayReportSerialNo) {
			this.displayReportSerialNo = displayReportSerialNo;
		}

		public void setDisplayTesterName(boolean displayTesterName) {
			this.displayTesterName = displayTesterName;
		}

		public void setDisplayPageNumber(boolean displayPageNumber) {
			this.displayPageNumber = displayPageNumber;
		}

		public void setDisplayVoltage(boolean displayVoltage) {
			this.displayVoltage = displayVoltage;
		}

		public void setDisplayTestPeriod(boolean displayTestPeriod) {
			this.displayTestPeriod = displayTestPeriod;
		}

		public void setDisplayDescription(boolean displayDescription) {
			this.displayDescription = displayDescription;
		}

		public void setDisplayResultStatus(boolean displayResultStatus) {
			this.displayResultStatus = displayResultStatus;
		}

		public void setDisplayResultStatusPrefix(boolean displayResultStatusPrefix) {
			this.displayResultStatusPrefix = displayResultStatusPrefix;
		}

		public void setDisplayResultStatusPostfix(boolean displayResultStatusPostfix) {
			this.displayResultStatusPostfix = displayResultStatusPostfix;
		}

		public void setDisplayOverRideResultStatus(boolean displayOverRideResultStatus) {
			this.displayOverRideResultStatus = displayOverRideResultStatus;
		}

		public void setDisplayResultValue(boolean displayResultValue) {
			this.displayResultValue = displayResultValue;
		}

		public void setDisplayResultValuePrefix(boolean displayResultValuePrefix) {
			this.displayResultValuePrefix = displayResultValuePrefix;
		}

		public void setDisplayResultValuePostfix(boolean displayResultValuePostfix) {
			this.displayResultValuePostfix = displayResultValuePostfix;
		}

		public void setDisplayOverRideResultValue(boolean displayOverRideResultValue) {
			this.displayOverRideResultValue = displayOverRideResultValue;
		}

        // Getters and Setters
        // ... (similar to above)
    }

    public  class HighVoltageReportResultValue {
        @SerializedName("DescriptionPassValue")
        @Expose
        private String descriptionPassValue;

        @SerializedName("DescriptionFailValue")
        @Expose
        private String descriptionFailValue;

        @SerializedName("ResultValueOverRidePassValue")
        @Expose
        private String resultValueOverRidePassValue;

        @SerializedName("ResultValueOverRideFailValue")
        @Expose
        private String resultValueOverRideFailValue;

        @SerializedName("ResultValuePrefixValue")
        @Expose
        private String resultValuePrefixValue;

        @SerializedName("ResultValuePostfixValue")
        @Expose
        private String resultValuePostfixValue;

        @SerializedName("ResultStatusOverRidePassValue")
        @Expose
        private String resultStatusOverRidePassValue;

        @SerializedName("ResultStatusOverRideFailValue")
        @Expose
        private String resultStatusOverRideFailValue;

        @SerializedName("ResultStatusPrefixValue")
        @Expose
        private String resultStatusPrefixValue;

        @SerializedName("ResultStatusPostfixValue")
        @Expose
        private String resultStatusPostfixValue;

		public String getDescriptionPassValue() {
			return descriptionPassValue;
		}

		public String getDescriptionFailValue() {
			return descriptionFailValue;
		}

		public String getResultValueOverRidePassValue() {
			return resultValueOverRidePassValue;
		}

		public String getResultValueOverRideFailValue() {
			return resultValueOverRideFailValue;
		}

		public String getResultValuePrefixValue() {
			return resultValuePrefixValue;
		}

		public String getResultValuePostfixValue() {
			return resultValuePostfixValue;
		}

		public String getResultStatusOverRidePassValue() {
			return resultStatusOverRidePassValue;
		}

		public String getResultStatusOverRideFailValue() {
			return resultStatusOverRideFailValue;
		}

		public String getResultStatusPrefixValue() {
			return resultStatusPrefixValue;
		}

		public String getResultStatusPostfixValue() {
			return resultStatusPostfixValue;
		}

		public void setDescriptionPassValue(String descriptionPassValue) {
			this.descriptionPassValue = descriptionPassValue;
		}

		public void setDescriptionFailValue(String descriptionFailValue) {
			this.descriptionFailValue = descriptionFailValue;
		}

		public void setResultValueOverRidePassValue(String resultValueOverRidePassValue) {
			this.resultValueOverRidePassValue = resultValueOverRidePassValue;
		}

		public void setResultValueOverRideFailValue(String resultValueOverRideFailValue) {
			this.resultValueOverRideFailValue = resultValueOverRideFailValue;
		}

		public void setResultValuePrefixValue(String resultValuePrefixValue) {
			this.resultValuePrefixValue = resultValuePrefixValue;
		}

		public void setResultValuePostfixValue(String resultValuePostfixValue) {
			this.resultValuePostfixValue = resultValuePostfixValue;
		}

		public void setResultStatusOverRidePassValue(String resultStatusOverRidePassValue) {
			this.resultStatusOverRidePassValue = resultStatusOverRidePassValue;
		}

		public void setResultStatusOverRideFailValue(String resultStatusOverRideFailValue) {
			this.resultStatusOverRideFailValue = resultStatusOverRideFailValue;
		}

		public void setResultStatusPrefixValue(String resultStatusPrefixValue) {
			this.resultStatusPrefixValue = resultStatusPrefixValue;
		}

		public void setResultStatusPostfixValue(String resultStatusPostfixValue) {
			this.resultStatusPostfixValue = resultStatusPostfixValue;
		}

        // Getters and Setters
        // ... (similar to above)
    }
	
	
		
	
	public  class FunctionalTestReportCellPosition {
        @SerializedName("reportSerialNoCell")
        @Expose
        private String reportSerialNoCell;
        
        @SerializedName("testerNameCell")
        @Expose
        private String testerNameCell;
        
        @SerializedName("pageNumberCell")
        @Expose
        private String pageNumberCell;
        
        @SerializedName("voltageCell")
        @Expose
        private String voltageCell;
        
        @SerializedName("testPeriodCell")
        @Expose
        private String testPeriodCell;
        
        @SerializedName("descriptionCell")
        @Expose
        private String descriptionCell;
        
        @SerializedName("resultStatusCell")
        @Expose
        private String resultStatusCell;
        
        @SerializedName("resultValueCell")
        @Expose
        private String resultValueCell;

		public String getReportSerialNoCell() {
			return reportSerialNoCell;
		}

		public String getTesterNameCell() {
			return testerNameCell;
		}

		public String getPageNumberCell() {
			return pageNumberCell;
		}

		public String getVoltageCell() {
			return voltageCell;
		}

		public String getTestPeriodCell() {
			return testPeriodCell;
		}

		public String getDescriptionCell() {
			return descriptionCell;
		}

		public String getResultStatusCell() {
			return resultStatusCell;
		}

		public String getResultValueCell() {
			return resultValueCell;
		}

		public void setReportSerialNoCell(String reportSerialNoCell) {
			this.reportSerialNoCell = reportSerialNoCell;
		}

		public void setTesterNameCell(String testerNameCell) {
			this.testerNameCell = testerNameCell;
		}

		public void setPageNumberCell(String pageNumberCell) {
			this.pageNumberCell = pageNumberCell;
		}

		public void setVoltageCell(String voltageCell) {
			this.voltageCell = voltageCell;
		}

		public void setTestPeriodCell(String testPeriodCell) {
			this.testPeriodCell = testPeriodCell;
		}

		public void setDescriptionCell(String descriptionCell) {
			this.descriptionCell = descriptionCell;
		}

		public void setResultStatusCell(String resultStatusCell) {
			this.resultStatusCell = resultStatusCell;
		}

		public void setResultValueCell(String resultValueCell) {
			this.resultValueCell = resultValueCell;
		}
        
        // Getters and setters
    }
    
    public class FunctionalTestReportDisplay {
        @SerializedName("displayFunctionTestInReport")
        @Expose
        private boolean displayFunctionTestInReport;
        
        @SerializedName("displayReportSerialNo")
        @Expose
        private boolean displayReportSerialNo;
        
        @SerializedName("displayTesterName")
        @Expose
        private boolean displayTesterName;
        
        @SerializedName("displayPageNumber")
        @Expose
        private boolean displayPageNumber;
        
        @SerializedName("displayVoltage")
        @Expose
        private boolean displayVoltage;
        
        @SerializedName("displayTestPeriod")
        @Expose
        private boolean displayTestPeriod;
        
        @SerializedName("displayDescription")
        @Expose
        private boolean displayDescription;
        
        @SerializedName("displayResultStatus")
        @Expose
        private boolean displayResultStatus;
        
        @SerializedName("displayResultStatusPrefix")
        @Expose
        private boolean displayResultStatusPrefix;
        
        @SerializedName("displayResultStatusPostfix")
        @Expose
        private boolean displayResultStatusPostfix;
        
        @SerializedName("displayOverRideResultStatus")
        @Expose
        private boolean displayOverRideResultStatus;
        
        @SerializedName("displayResultValue")
        @Expose
        private boolean displayResultValue;
        
        @SerializedName("displayResultValuePrefix")
        @Expose
        private boolean displayResultValuePrefix;
        
        @SerializedName("displayResultValuePostfix")
        @Expose
        private boolean displayResultValuePostfix;
        
        @SerializedName("displayOverRideResultValue")
        @Expose
        private boolean displayOverRideResultValue;

		public boolean isDisplayFunctionTestInReport() {
			return displayFunctionTestInReport;
		}

		public boolean isDisplayReportSerialNo() {
			return displayReportSerialNo;
		}

		public boolean isDisplayTesterName() {
			return displayTesterName;
		}

		public boolean isDisplayPageNumber() {
			return displayPageNumber;
		}

		public boolean isDisplayVoltage() {
			return displayVoltage;
		}

		public boolean isDisplayTestPeriod() {
			return displayTestPeriod;
		}

		public boolean isDisplayDescription() {
			return displayDescription;
		}

		public boolean isDisplayResultStatus() {
			return displayResultStatus;
		}

		public boolean isDisplayResultStatusPrefix() {
			return displayResultStatusPrefix;
		}

		public boolean isDisplayResultStatusPostfix() {
			return displayResultStatusPostfix;
		}

		public boolean isDisplayOverRideResultStatus() {
			return displayOverRideResultStatus;
		}

		public boolean isDisplayResultValue() {
			return displayResultValue;
		}

		public boolean isDisplayResultValuePrefix() {
			return displayResultValuePrefix;
		}

		public boolean isDisplayResultValuePostfix() {
			return displayResultValuePostfix;
		}

		public boolean isDisplayOverRideResultValue() {
			return displayOverRideResultValue;
		}

		public void setDisplayFunctionTestInReport(boolean displayFunctionTestInReport) {
			this.displayFunctionTestInReport = displayFunctionTestInReport;
		}

		public void setDisplayReportSerialNo(boolean displayReportSerialNo) {
			this.displayReportSerialNo = displayReportSerialNo;
		}

		public void setDisplayTesterName(boolean displayTesterName) {
			this.displayTesterName = displayTesterName;
		}

		public void setDisplayPageNumber(boolean displayPageNumber) {
			this.displayPageNumber = displayPageNumber;
		}

		public void setDisplayVoltage(boolean displayVoltage) {
			this.displayVoltage = displayVoltage;
		}

		public void setDisplayTestPeriod(boolean displayTestPeriod) {
			this.displayTestPeriod = displayTestPeriod;
		}

		public void setDisplayDescription(boolean displayDescription) {
			this.displayDescription = displayDescription;
		}

		public void setDisplayResultStatus(boolean displayResultStatus) {
			this.displayResultStatus = displayResultStatus;
		}

		public void setDisplayResultStatusPrefix(boolean displayResultStatusPrefix) {
			this.displayResultStatusPrefix = displayResultStatusPrefix;
		}

		public void setDisplayResultStatusPostfix(boolean displayResultStatusPostfix) {
			this.displayResultStatusPostfix = displayResultStatusPostfix;
		}

		public void setDisplayOverRideResultStatus(boolean displayOverRideResultStatus) {
			this.displayOverRideResultStatus = displayOverRideResultStatus;
		}

		public void setDisplayResultValue(boolean displayResultValue) {
			this.displayResultValue = displayResultValue;
		}

		public void setDisplayResultValuePrefix(boolean displayResultValuePrefix) {
			this.displayResultValuePrefix = displayResultValuePrefix;
		}

		public void setDisplayResultValuePostfix(boolean displayResultValuePostfix) {
			this.displayResultValuePostfix = displayResultValuePostfix;
		}

		public void setDisplayOverRideResultValue(boolean displayOverRideResultValue) {
			this.displayOverRideResultValue = displayOverRideResultValue;
		}
        
        // Getters and setters
    }
    
    public class FunctionalTestReportResultValue {
        @SerializedName("DescriptionPassValue")
        @Expose
        private String descriptionPassValue;
        
        @SerializedName("DescriptionFailValue")
        @Expose
        private String descriptionFailValue;
        
        @SerializedName("ResultValueOverRidePassValue")
        @Expose
        private String resultValueOverRidePassValue;
        
        @SerializedName("ResultValueOverRideFailValue")
        @Expose
        private String resultValueOverRideFailValue;
        
        @SerializedName("ResultValuePrefixValue")
        @Expose
        private String resultValuePrefixValue;
        
        @SerializedName("ResultValuePostfixValue")
        @Expose
        private String resultValuePostfixValue;
        
        @SerializedName("ResultStatusOverRidePassValue")
        @Expose
        private String resultStatusOverRidePassValue;
        
        @SerializedName("ResultStatusOverRideFailValue")
        @Expose
        private String resultStatusOverRideFailValue;
        
        @SerializedName("ResultStatusPrefixValue")
        @Expose
        private String resultStatusPrefixValue;
        
        @SerializedName("ResultStatusPostfixValue")
        @Expose
        private String resultStatusPostfixValue;

		public String getDescriptionPassValue() {
			return descriptionPassValue;
		}

		public String getDescriptionFailValue() {
			return descriptionFailValue;
		}

		public String getResultValueOverRidePassValue() {
			return resultValueOverRidePassValue;
		}

		public String getResultValueOverRideFailValue() {
			return resultValueOverRideFailValue;
		}

		public String getResultValuePrefixValue() {
			return resultValuePrefixValue;
		}

		public String getResultValuePostfixValue() {
			return resultValuePostfixValue;
		}

		public String getResultStatusOverRidePassValue() {
			return resultStatusOverRidePassValue;
		}

		public String getResultStatusOverRideFailValue() {
			return resultStatusOverRideFailValue;
		}

		public String getResultStatusPrefixValue() {
			return resultStatusPrefixValue;
		}

		public String getResultStatusPostfixValue() {
			return resultStatusPostfixValue;
		}

		public void setDescriptionPassValue(String descriptionPassValue) {
			this.descriptionPassValue = descriptionPassValue;
		}

		public void setDescriptionFailValue(String descriptionFailValue) {
			this.descriptionFailValue = descriptionFailValue;
		}

		public void setResultValueOverRidePassValue(String resultValueOverRidePassValue) {
			this.resultValueOverRidePassValue = resultValueOverRidePassValue;
		}

		public void setResultValueOverRideFailValue(String resultValueOverRideFailValue) {
			this.resultValueOverRideFailValue = resultValueOverRideFailValue;
		}

		public void setResultValuePrefixValue(String resultValuePrefixValue) {
			this.resultValuePrefixValue = resultValuePrefixValue;
		}

		public void setResultValuePostfixValue(String resultValuePostfixValue) {
			this.resultValuePostfixValue = resultValuePostfixValue;
		}

		public void setResultStatusOverRidePassValue(String resultStatusOverRidePassValue) {
			this.resultStatusOverRidePassValue = resultStatusOverRidePassValue;
		}

		public void setResultStatusOverRideFailValue(String resultStatusOverRideFailValue) {
			this.resultStatusOverRideFailValue = resultStatusOverRideFailValue;
		}

		public void setResultStatusPrefixValue(String resultStatusPrefixValue) {
			this.resultStatusPrefixValue = resultStatusPrefixValue;
		}

		public void setResultStatusPostfixValue(String resultStatusPostfixValue) {
			this.resultStatusPostfixValue = resultStatusPostfixValue;
		}
        
        // Getters and setters
    }
	
    
    



    public class RoutineSummaryReportResultValue {
        @SerializedName("DescriptionPassValue")
        @Expose
        private String descriptionPassValue;
        
        @SerializedName("PrefixReportSerialNoValue")
        @Expose
        private String prefixReportSerialNoValue;
        
        @SerializedName("PostfixReportSerialNoValue")
        @Expose
        private String postfixReportSerialNoValue;

        @SerializedName("DescriptionFailValue")
        @Expose
        private String descriptionFailValue;

        @SerializedName("TenderNoValue")
        @Expose
        private String tenderNoValue;

        @SerializedName("PropertyOfValue")
        @Expose
        private String propertyOfValue;
        
        @SerializedName("PrefixMeterSerialNoValue")
        @Expose
        private String prefixMeterSerialNoValue;
        
        @SerializedName("PrefixTestExecutedDateValue")
        @Expose
        private String prefixTestExecutedDateValue;
        
        
        @SerializedName("PrefixTenderNoValue")
        @Expose
        private String prefixTenderNoValue;

        @SerializedName("PrefixPropertyOfValue")
        @Expose
        private String prefixPropertyOfValue;
        
        
        
        
        
        @SerializedName("Is_SpecValue")
        @Expose
        private String isi_SpecValue;
        
        @SerializedName("PrefixIs_SpecValue")
        @Expose
        private String prefixIs_SpecValue;
        
        @SerializedName("PostfixIs_SpecValue")
        @Expose
        private String postfixIs_SpecValue;
        
        @SerializedName("CategoryValue")
        @Expose
        private String categoryValue;
        
        @SerializedName("PrefixCategoryValue")
        @Expose
        private String prefixCategoryValue;
        
        @SerializedName("PostfixCategoryValue")
        @Expose
        private String postfixCategoryValue;
        
        
        
        

        @SerializedName("OverAllResultValueOverRidePassValue")
        @Expose
        private String overAllResultValueOverRidePassValue;

        @SerializedName("OverAllResultValueOverRideFailValue")
        @Expose
        private String overAllResultValueOverRideFailValue;

        @SerializedName("OverAllResultValuePrefixValue")
        @Expose
        private String overAllResultValuePrefixValue;

        @SerializedName("OverAllResultValuePostfixValue")
        @Expose
        private String overAllResultValuePostfixValue;

        @SerializedName("OverAllResultStatusOverRidePassValue")
        @Expose
        private String overAllResultStatusOverRidePassValue;

        @SerializedName("OverAllResultStatusOverRideFailValue")
        @Expose
        private String overAllResultStatusOverRideFailValue;

        @SerializedName("OverAllResultStatusPrefixValue")
        @Expose
        private String overAllResultStatusPrefixValue;

        @SerializedName("OverAllResultStatusPostfixValue")
        @Expose
        private String overAllResultStatusPostfixValue;

        // Getters and Setters
        public String getDescriptionPassValue() {
            return descriptionPassValue;
        }

        public void setDescriptionPassValue(String descriptionPassValue) {
            this.descriptionPassValue = descriptionPassValue;
        }

		public String getDescriptionFailValue() {
			return descriptionFailValue;
		}

		public String getTenderNoValue() {
			return tenderNoValue;
		}

		public String getPropertyOfValue() {
			return propertyOfValue;
		}

		public String getOverAllResultValueOverRidePassValue() {
			return overAllResultValueOverRidePassValue;
		}

		public String getOverAllResultValueOverRideFailValue() {
			return overAllResultValueOverRideFailValue;
		}

		public String getOverAllResultValuePrefixValue() {
			return overAllResultValuePrefixValue;
		}

		public String getOverAllResultValuePostfixValue() {
			return overAllResultValuePostfixValue;
		}

		public String getOverAllResultStatusOverRidePassValue() {
			return overAllResultStatusOverRidePassValue;
		}

		public String getOverAllResultStatusOverRideFailValue() {
			return overAllResultStatusOverRideFailValue;
		}

		public String getOverAllResultStatusPrefixValue() {
			return overAllResultStatusPrefixValue;
		}

		public String getOverAllResultStatusPostfixValue() {
			return overAllResultStatusPostfixValue;
		}

		public void setDescriptionFailValue(String descriptionFailValue) {
			this.descriptionFailValue = descriptionFailValue;
		}

		public void setTenderNoValue(String tenderNoValue) {
			this.tenderNoValue = tenderNoValue;
		}

		public void setPropertyOfValue(String propertyOfValue) {
			this.propertyOfValue = propertyOfValue;
		}

		public void setOverAllResultValueOverRidePassValue(String overAllResultValueOverRidePassValue) {
			this.overAllResultValueOverRidePassValue = overAllResultValueOverRidePassValue;
		}

		public void setOverAllResultValueOverRideFailValue(String overAllResultValueOverRideFailValue) {
			this.overAllResultValueOverRideFailValue = overAllResultValueOverRideFailValue;
		}

		public void setOverAllResultValuePrefixValue(String overAllResultValuePrefixValue) {
			this.overAllResultValuePrefixValue = overAllResultValuePrefixValue;
		}

		public void setOverAllResultValuePostfixValue(String overAllResultValuePostfixValue) {
			this.overAllResultValuePostfixValue = overAllResultValuePostfixValue;
		}

		public void setOverAllResultStatusOverRidePassValue(String overAllResultStatusOverRidePassValue) {
			this.overAllResultStatusOverRidePassValue = overAllResultStatusOverRidePassValue;
		}

		public void setOverAllResultStatusOverRideFailValue(String overAllResultStatusOverRideFailValue) {
			this.overAllResultStatusOverRideFailValue = overAllResultStatusOverRideFailValue;
		}

		public void setOverAllResultStatusPrefixValue(String overAllResultStatusPrefixValue) {
			this.overAllResultStatusPrefixValue = overAllResultStatusPrefixValue;
		}

		public void setOverAllResultStatusPostfixValue(String overAllResultStatusPostfixValue) {
			this.overAllResultStatusPostfixValue = overAllResultStatusPostfixValue;
		}

		public String getPrefixTenderNoValue() {
			return prefixTenderNoValue;
		}

		public String getPrefixPropertyOfValue() {
			return prefixPropertyOfValue;
		}

		public void setPrefixTenderNoValue(String prefixTenderNoValue) {
			this.prefixTenderNoValue = prefixTenderNoValue;
		}

		public void setPrefixPropertyOfValue(String prefixPropertyOfValue) {
			this.prefixPropertyOfValue = prefixPropertyOfValue;
		}

		public String getPrefixMeterSerialNoValue() {
			return prefixMeterSerialNoValue;
		}

		public String getPrefixTestExecutedDateValue() {
			return prefixTestExecutedDateValue;
		}



		public void setPrefixMeterSerialNoValue(String prefixMeterSerialNoValue) {
			this.prefixMeterSerialNoValue = prefixMeterSerialNoValue;
		}

		public void setPrefixTestExecutedDateValue(String prefixTestExecutedDateValue) {
			this.prefixTestExecutedDateValue = prefixTestExecutedDateValue;
		}

		public String getIsi_SpecValue() {
			return isi_SpecValue;
		}

		public String getPrefixIs_SpecValue() {
			return prefixIs_SpecValue;
		}

		public String getPostfixIs_SpecValue() {
			return postfixIs_SpecValue;
		}

		public String getCategoryValue() {
			return categoryValue;
		}

		public String getPrefixCategoryValue() {
			return prefixCategoryValue;
		}

		public String getPostfixCategoryValue() {
			return postfixCategoryValue;
		}

		public void setIsi_SpecValue(String isi_SpecValue) {
			this.isi_SpecValue = isi_SpecValue;
		}

		public void setPrefixIs_SpecValue(String prefixIs_SpecValue) {
			this.prefixIs_SpecValue = prefixIs_SpecValue;
		}

		public void setPostfixIs_SpecValue(String postfixIs_SpecValue) {
			this.postfixIs_SpecValue = postfixIs_SpecValue;
		}

		public void setCategoryValue(String categoryValue) {
			this.categoryValue = categoryValue;
		}

		public void setPrefixCategoryValue(String prefixCategoryValue) {
			this.prefixCategoryValue = prefixCategoryValue;
		}

		public void setPostfixCategoryValue(String postfixCategoryValue) {
			this.postfixCategoryValue = postfixCategoryValue;
		}

		public String getPrefixReportSerialNoValue() {
			return prefixReportSerialNoValue;
		}

		public String getPostfixReportSerialNoValue() {
			return postfixReportSerialNoValue;
		}

		public void setPrefixReportSerialNoValue(String prefixReportSerialNoValue) {
			this.prefixReportSerialNoValue = prefixReportSerialNoValue;
		}

		public void setPostfixReportSerialNoValue(String postfixReportSerialNoValue) {
			this.postfixReportSerialNoValue = postfixReportSerialNoValue;
		}




    }

    public class RoutineSummaryReportDisplay {
        @SerializedName("displayRoutineTestInReport")
        @Expose
        private boolean displayRoutineTestInReport;
        
        @SerializedName("appendCellData")
        @Expose
        private boolean appendCellData;
        
        @SerializedName("mergeCells")
        @Expose
        private boolean mergeCells;
        
        @SerializedName("overwriteMergedCellWithAppendedData")
        @Expose
        private boolean overwriteMergedCellWithAppendedData;

        @SerializedName("displayReportSerialNo")
        @Expose
        private boolean displayReportSerialNo;
        
        
        @SerializedName("displayPrefixReportSerialNo")
	    @Expose
	    private boolean displayPrefixReportSerialNo;
	    
	    @SerializedName("displayPostfixReportSerialNo")
	    @Expose
	    private boolean displayPostfixReportSerialNo;
        
        
        
        @SerializedName("displayMeterSerialNo")
        @Expose
        private boolean displayMeterSerialNo;
        
        @SerializedName("displayPrefixMeterSerialNo")
        @Expose
        private boolean displayPrefixMeterSerialNo;

        @SerializedName("displayPropertyOf")
        @Expose
        private boolean displayPropertyOf;

        @SerializedName("displayTenderNo")
        @Expose
        private boolean displayTenderNo;
        
        @SerializedName("displayPrefixIs_Spec")
        @Expose
        private boolean displayPrefixIs_Spec;
        
        
        @SerializedName("displayIs_Spec")
        @Expose
        private boolean displayIs_Spec;
        
        @SerializedName("displayPostfixIs_Spec")
        @Expose
        private boolean displayPostfixIs_Spec;
        
        @SerializedName("displayCategory")
        @Expose
        private boolean displayCategory;
        
        @SerializedName("displayPrefixCategory")
        @Expose
        private boolean displayPrefixCategory;
        
        @SerializedName("displayPostfixCategory")
        @Expose
        private boolean displayPostfixCategory;       
       
        
        @SerializedName("displayPrefixPropertyOf")
        @Expose
        private boolean displayPrefixPropertyOf;

        @SerializedName("displayPrefixTenderNo")
        @Expose
        private boolean displayPrefixTenderNo;

        @SerializedName("displayTestExecutedDate")
        @Expose
        private boolean displayTestExecutedDate;
        
        
        @SerializedName("displayPrefixTestExecutedDate")
        @Expose
        private boolean displayPrefixTestExecutedDate;
        
        
/*        @SerializedName("displayPrefixModelNo")
        @Expose
        private boolean displayPrefixModelNo;*/


        @SerializedName("displayTesterName")
        @Expose
        private boolean displayTesterName;

        @SerializedName("displayPageNumber")
        @Expose
        private boolean displayPageNumber;

        @SerializedName("displayDescription")
        @Expose
        private boolean displayDescription;

        @SerializedName("displayRoutineTestOverAllResultStatus")
        @Expose
        private boolean displayRoutineTestOverAllResultStatus;

        @SerializedName("displayRoutineTestOverAllResultStatusPrefix")
        @Expose
        private boolean displayRoutineTestOverAllResultStatusPrefix;

        @SerializedName("displayRoutineTestOverAllResultStatusPostfix")
        @Expose
        private boolean displayRoutineTestOverAllResultStatusPostfix;

        @SerializedName("displayRoutineTestOverAllOverRideResultStatus")
        @Expose
        private boolean displayRoutineTestOverAllOverRideResultStatus;

        @SerializedName("displayRoutineTestOverAllResultValue")
        @Expose
        private boolean displayRoutineTestOverAllResultValue;

        @SerializedName("displayRoutineTestOverAllResultValuePrefix")
        @Expose
        private boolean displayRoutineTestOverAllResultValuePrefix;

        @SerializedName("displayRoutineTestOverAllResultValuePostfix")
        @Expose
        private boolean displayRoutineTestOverAllResultValuePostfix;

        @SerializedName("displayRoutineTestOverAllOverRideResultValue")
        @Expose
        private boolean displayRoutineTestOverAllOverRideResultValue;

        // Getters and Setters
        public boolean isDisplayRoutineTestInReport() {
            return displayRoutineTestInReport;
        }

        public void setDisplayRoutineTestInReport(boolean displayRoutineTestInReport) {
            this.displayRoutineTestInReport = displayRoutineTestInReport;
        }

		public boolean isDisplayReportSerialNo() {
			return displayReportSerialNo;
		}

		public boolean isDisplayPropertyOf() {
			return displayPropertyOf;
		}

		public boolean isDisplayTenderNo() {
			return displayTenderNo;
		}

		public boolean isDisplayTestExecutedDate() {
			return displayTestExecutedDate;
		}

		public boolean isDisplayTesterName() {
			return displayTesterName;
		}

		public boolean isDisplayPageNumber() {
			return displayPageNumber;
		}

		public boolean isDisplayDescription() {
			return displayDescription;
		}

		public boolean isDisplayRoutineTestOverAllResultStatus() {
			return displayRoutineTestOverAllResultStatus;
		}

		public boolean isDisplayRoutineTestOverAllResultStatusPrefix() {
			return displayRoutineTestOverAllResultStatusPrefix;
		}

		public boolean isDisplayRoutineTestOverAllResultStatusPostfix() {
			return displayRoutineTestOverAllResultStatusPostfix;
		}

		public boolean isDisplayRoutineTestOverAllOverRideResultStatus() {
			return displayRoutineTestOverAllOverRideResultStatus;
		}

		public boolean isDisplayRoutineTestOverAllResultValue() {
			return displayRoutineTestOverAllResultValue;
		}

		public boolean isDisplayRoutineTestOverAllResultValuePrefix() {
			return displayRoutineTestOverAllResultValuePrefix;
		}

		public boolean isDisplayRoutineTestOverAllResultValuePostfix() {
			return displayRoutineTestOverAllResultValuePostfix;
		}

		public boolean isDisplayRoutineTestOverAllOverRideResultValue() {
			return displayRoutineTestOverAllOverRideResultValue;
		}

		public void setDisplayReportSerialNo(boolean displayReportSerialNo) {
			this.displayReportSerialNo = displayReportSerialNo;
		}

		public void setDisplayPropertyOf(boolean displayPropertyOf) {
			this.displayPropertyOf = displayPropertyOf;
		}

		public void setDisplayTenderNo(boolean displayTenderNo) {
			this.displayTenderNo = displayTenderNo;
		}

		public void setDisplayTestExecutedDate(boolean displayTestExecutedDate) {
			this.displayTestExecutedDate = displayTestExecutedDate;
		}

		public void setDisplayTesterName(boolean displayTesterName) {
			this.displayTesterName = displayTesterName;
		}

		public void setDisplayPageNumber(boolean displayPageNumber) {
			this.displayPageNumber = displayPageNumber;
		}

		public void setDisplayDescription(boolean displayDescription) {
			this.displayDescription = displayDescription;
		}

		public void setDisplayRoutineTestOverAllResultStatus(boolean displayRoutineTestOverAllResultStatus) {
			this.displayRoutineTestOverAllResultStatus = displayRoutineTestOverAllResultStatus;
		}

		public void setDisplayRoutineTestOverAllResultStatusPrefix(boolean displayRoutineTestOverAllResultStatusPrefix) {
			this.displayRoutineTestOverAllResultStatusPrefix = displayRoutineTestOverAllResultStatusPrefix;
		}

		public void setDisplayRoutineTestOverAllResultStatusPostfix(boolean displayRoutineTestOverAllResultStatusPostfix) {
			this.displayRoutineTestOverAllResultStatusPostfix = displayRoutineTestOverAllResultStatusPostfix;
		}

		public void setDisplayRoutineTestOverAllOverRideResultStatus(boolean displayRoutineTestOverAllOverRideResultStatus) {
			this.displayRoutineTestOverAllOverRideResultStatus = displayRoutineTestOverAllOverRideResultStatus;
		}

		public void setDisplayRoutineTestOverAllResultValue(boolean displayRoutineTestOverAllResultValue) {
			this.displayRoutineTestOverAllResultValue = displayRoutineTestOverAllResultValue;
		}

		public void setDisplayRoutineTestOverAllResultValuePrefix(boolean displayRoutineTestOverAllResultValuePrefix) {
			this.displayRoutineTestOverAllResultValuePrefix = displayRoutineTestOverAllResultValuePrefix;
		}

		public void setDisplayRoutineTestOverAllResultValuePostfix(boolean displayRoutineTestOverAllResultValuePostfix) {
			this.displayRoutineTestOverAllResultValuePostfix = displayRoutineTestOverAllResultValuePostfix;
		}

		public void setDisplayRoutineTestOverAllOverRideResultValue(boolean displayRoutineTestOverAllOverRideResultValue) {
			this.displayRoutineTestOverAllOverRideResultValue = displayRoutineTestOverAllOverRideResultValue;
		}

		public boolean isDisplayMeterSerialNo() {
			return displayMeterSerialNo;
		}

		public void setDisplayMeterSerialNo(boolean displayMeterSerialNo) {
			this.displayMeterSerialNo = displayMeterSerialNo;
		}

		public boolean isDisplayPrefixPropertyOf() {
			return displayPrefixPropertyOf;
		}

		public boolean isDisplayPrefixTenderNo() {
			return displayPrefixTenderNo;
		}

		public void setDisplayPrefixPropertyOf(boolean displayPrefixPropertyOf) {
			this.displayPrefixPropertyOf = displayPrefixPropertyOf;
		}

		public void setDisplayPrefixTenderNo(boolean displayPrefixTenderNo) {
			this.displayPrefixTenderNo = displayPrefixTenderNo;
		}

		public boolean isDisplayPrefixMeterSerialNo() {
			return displayPrefixMeterSerialNo;
		}

		public boolean isDisplayPrefixTestExecutedDate() {
			return displayPrefixTestExecutedDate;
		}

		public void setDisplayPrefixMeterSerialNo(boolean displayPrefixMeterSerialNo) {
			this.displayPrefixMeterSerialNo = displayPrefixMeterSerialNo;
		}

		public void setDisplayPrefixTestExecutedDate(boolean displayPrefixTestExecutedDate) {
			this.displayPrefixTestExecutedDate = displayPrefixTestExecutedDate;
		}

		public boolean isDisplayIs_Spec() {
			return displayIs_Spec;
		}

		public boolean isDisplayCategory() {
			return displayCategory;
		}

		public void setDisplayIs_Spec(boolean displayIs_Spec) {
			this.displayIs_Spec = displayIs_Spec;
		}

		public void setDisplayCategory(boolean displayCategory) {
			this.displayCategory = displayCategory;
		}

		public boolean isDisplayPrefixIs_Spec() {
			return displayPrefixIs_Spec;
		}

		public boolean isDisplayPostfixIs_Spec() {
			return displayPostfixIs_Spec;
		}

		public boolean isDisplayPrefixCategory() {
			return displayPrefixCategory;
		}

		public boolean isDisplayPostfixCategory() {
			return displayPostfixCategory;
		}

		public void setDisplayPrefixIs_Spec(boolean displayPrefixIs_Spec) {
			this.displayPrefixIs_Spec = displayPrefixIs_Spec;
		}

		public void setDisplayPostfixIs_Spec(boolean displayPostfixIs_Spec) {
			this.displayPostfixIs_Spec = displayPostfixIs_Spec;
		}

		public void setDisplayPrefixCategory(boolean displayPrefixCategory) {
			this.displayPrefixCategory = displayPrefixCategory;
		}

		public void setDisplayPostfixCategory(boolean displayPostfixCategory) {
			this.displayPostfixCategory = displayPostfixCategory;
		}

		public boolean isDisplayPrefixReportSerialNo() {
			return displayPrefixReportSerialNo;
		}

		public boolean isDisplayPostfixReportSerialNo() {
			return displayPostfixReportSerialNo;
		}

		public void setDisplayPrefixReportSerialNo(boolean displayPrefixReportSerialNo) {
			this.displayPrefixReportSerialNo = displayPrefixReportSerialNo;
		}

		public void setDisplayPostfixReportSerialNo(boolean displayPostfixReportSerialNo) {
			this.displayPostfixReportSerialNo = displayPostfixReportSerialNo;
		}

		public boolean isAppendCellData() {
			return appendCellData;
		}

		public boolean isMergeCells() {
			return mergeCells;
		}

		public void setAppendCellData(boolean appendCellData) {
			this.appendCellData = appendCellData;
		}

		public void setMergeCells(boolean mergeCells) {
			this.mergeCells = mergeCells;
		}

		public boolean isOverwriteMergedCellWithAppendedData() {
			return overwriteMergedCellWithAppendedData;
		}

		public void setOverwriteMergedCellWithAppendedData(boolean overwriteMergedCellWithAppendedData) {
			this.overwriteMergedCellWithAppendedData = overwriteMergedCellWithAppendedData;
		}

/*		public boolean isDisplayPrefixModelNo() {
			return displayPrefixModelNo;
		}

		public void setDisplayPrefixModelNo(boolean displayPrefixModelNo) {
			this.displayPrefixModelNo = displayPrefixModelNo;
		}
*/
        

    }
    
    
    public class RoutineSummaryReportCellPosition {
        @SerializedName("reportSerialNoCell")
        @Expose
        private String reportSerialNoCell;
        
        @SerializedName("meterSerialNoCell")
        @Expose
        private String meterSerialNoCell;

        @SerializedName("testerNameCell")
        @Expose
        private String testerNameCell;

        @SerializedName("pageNumberCell")
        @Expose
        private String pageNumberCell;

        @SerializedName("executedDateCell")
        @Expose
        private String executedDateCell;

        @SerializedName("descriptionCell")
        @Expose
        private String descriptionCell;

        @SerializedName("propertyOfCell")
        @Expose
        private String propertyOfCell;

        @SerializedName("tenderNoCell")
        @Expose
        private String tenderNoCell;
        
        
        @SerializedName("iS_SpecCell")
        @Expose
        private String iS_SpecCell;
        
        @SerializedName("categoryCell")
        @Expose
        private String categoryCell;
        
        
        @SerializedName("appendCellPositionList")
        @Expose
        private String appendCellPositionList;
        
        
        @SerializedName("mergeStartingCell")
        @Expose
        private String mergeStartingCell;
        
        @SerializedName("mergeEndingCell")
        @Expose
        private String mergeEndingCell;

        @SerializedName("routineTestOverAllResultStatusCell")
        @Expose
        private String routineTestOverAllResultStatusCell;

        @SerializedName("routineTestOverAllResultValueCell")
        @Expose
        private String routineTestOverAllResultValueCell;

        // Getters and Setters
        public String getReportSerialNoCell() {
            return reportSerialNoCell;
        }

        public void setReportSerialNoCell(String reportSerialNoCell) {
            this.reportSerialNoCell = reportSerialNoCell;
        }

        public String getTesterNameCell() {
            return testerNameCell;
        }

        public void setTesterNameCell(String testerNameCell) {
            this.testerNameCell = testerNameCell;
        }

		public String getPageNumberCell() {
			return pageNumberCell;
		}

		public String getExecutedDateCell() {
			return executedDateCell;
		}

		public String getDescriptionCell() {
			return descriptionCell;
		}

		public String getPropertyOfCell() {
			return propertyOfCell;
		}

		public String getTenderNoCell() {
			return tenderNoCell;
		}

		public String getRoutineTestOverAllResultStatusCell() {
			return routineTestOverAllResultStatusCell;
		}

		public String getRoutineTestOverAllResultValueCell() {
			return routineTestOverAllResultValueCell;
		}

		public void setPageNumberCell(String pageNumberCell) {
			this.pageNumberCell = pageNumberCell;
		}

		public void setExecutedDateCell(String executedDateCell) {
			this.executedDateCell = executedDateCell;
		}

		public void setDescriptionCell(String descriptionCell) {
			this.descriptionCell = descriptionCell;
		}

		public void setPropertyOfCell(String propertyOfCell) {
			this.propertyOfCell = propertyOfCell;
		}

		public void setTenderNoCell(String tenderNoCell) {
			this.tenderNoCell = tenderNoCell;
		}

		public void setRoutineTestOverAllResultStatusCell(String routineTestOverAllResultStatusCell) {
			this.routineTestOverAllResultStatusCell = routineTestOverAllResultStatusCell;
		}

		public void setRoutineTestOverAllResultValueCell(String routineTestOverAllResultValueCell) {
			this.routineTestOverAllResultValueCell = routineTestOverAllResultValueCell;
		}

		public String getMeterSerialNoCell() {
			return meterSerialNoCell;
		}

		public void setMeterSerialNoCell(String meterSerialNoCell) {
			this.meterSerialNoCell = meterSerialNoCell;
		}

		public String getiS_SpecCell() {
			return iS_SpecCell;
		}

		public String getCategoryCell() {
			return categoryCell;
		}

		public void setiS_SpecCell(String iS_SpecCell) {
			this.iS_SpecCell = iS_SpecCell;
		}

		public void setCategoryCell(String categoryCell) {
			this.categoryCell = categoryCell;
		}

		public String getAppendCellPositionList() {
			return appendCellPositionList;
		}

		public void setAppendCellPositionList(String appendCellPositionList) {
			this.appendCellPositionList = appendCellPositionList;
		}

		public String getMergeStartingCell() {
			return mergeStartingCell;
		}

		public String getMergeEndingCell() {
			return mergeEndingCell;
		}

		public void setMergeStartingCell(String mergeStartingCell) {
			this.mergeStartingCell = mergeStartingCell;
		}

		public void setMergeEndingCell(String mergeEndingCell) {
			this.mergeEndingCell = mergeEndingCell;
		}


    }
    
    
    
    
		
	
	public class NoLoadReportCellPosition {
	    @SerializedName("reportSerialNoCell")
	    @Expose
	    private String reportSerialNoCell;

	    @SerializedName("testerNameCell")
	    @Expose
	    private String testerNameCell;

	    @SerializedName("pageNumberCell")
	    @Expose
	    private String pageNumberCell;

	    @SerializedName("voltageCell")
	    @Expose
	    private String voltageCell;

	    @SerializedName("testPeriodCell")
	    @Expose
	    private String testPeriodCell;
	    

	    @SerializedName("descriptionCell")
	    @Expose
	    private String descriptionCell;
	    
	    
	    @SerializedName("resultStatusCell")
	    @Expose
	    private String resultStatusCell;
	    
	    
	    @SerializedName("resultValueCell")
	    @Expose
	    private String resultValueCell;
	    
	    

		public String getReportSerialNoCell() {
			return reportSerialNoCell;
		}

		public String getTesterNameCell() {
			return testerNameCell;
		}

		public String getPageNumberCell() {
			return pageNumberCell;
		}

		public String getVoltageCell() {
			return voltageCell;
		}

		public String getTestPeriodCell() {
			return testPeriodCell;
		}

		public void setReportSerialNoCell(String reportSerialNoCell) {
			this.reportSerialNoCell = reportSerialNoCell;
		}

		public void setTesterNameCell(String testerNameCell) {
			this.testerNameCell = testerNameCell;
		}

		public void setPageNumberCell(String pageNumberCell) {
			this.pageNumberCell = pageNumberCell;
		}

		public void setVoltageCell(String voltageCell) {
			this.voltageCell = voltageCell;
		}

		public void setTestPeriodCell(String testPeriodCell) {
			this.testPeriodCell = testPeriodCell;
		}

		public String getDescriptionCell() {
			return descriptionCell;
		}

		public String getResultStatusCell() {
			return resultStatusCell;
		}

		public String getResultValueCell() {
			return resultValueCell;
		}

		public void setDescriptionCell(String descriptionCell) {
			this.descriptionCell = descriptionCell;
		}

		public void setResultStatusCell(String resultStatusCell) {
			this.resultStatusCell = resultStatusCell;
		}

		public void setResultValueCell(String resultValueCell) {
			this.resultValueCell = resultValueCell;
		}

	    // Getters and Setters
	}

	public class NoLoadReportDisplay {
		
		@SerializedName("displayNoLoadInReport")
	    @Expose
	    private boolean displayNoLoadInReport;

	    @SerializedName("displayReportSerialNo")
	    @Expose
	    private boolean displayReportSerialNo;
	    
	

	    @SerializedName("displayTesterName")
	    @Expose
	    private boolean displayTesterName;

	    @SerializedName("displayPageNumber")
	    @Expose
	    private boolean displayPageNumber;

	    @SerializedName("displayVoltage")
	    @Expose
	    private boolean displayVoltage;

	    @SerializedName("displayTestPeriod")
	    @Expose
	    private boolean displayTestPeriod;

	    @SerializedName("displayDescription")
	    @Expose
	    private boolean displayDescription;

	    @SerializedName("displayResultStatus")
	    @Expose
	    private boolean displayResultStatus;

	    @SerializedName("displayResultValue")
	    @Expose
	    private boolean displayResultValue;

	    @SerializedName("displayOverRideResultStatus")
	    @Expose
	    private boolean displayOverRideResultStatus;

	    @SerializedName("displayResultStatusPrefix")
	    @Expose
	    private boolean displayResultStatusPrefix;

	    @SerializedName("displayResultStatusPostfix")
	    @Expose
	    private boolean displayResultStatusPostfix;

	    @SerializedName("displayOverRideResultValue")
	    @Expose
	    private boolean displayOverRideResultValue;

	    @SerializedName("displayResultValuePrefix")
	    @Expose
	    private boolean displayResultValuePrefix;

	    @SerializedName("displayResultValuePostfix")
	    @Expose
	    private boolean displayResultValuePostfix;

	    // Getters and Setters
	    public boolean isDisplayNoLoadInReport() {
	        return displayNoLoadInReport;
	    }

	    public void setDisplayNoLoadInReport(boolean displayNoLoadInReport) {
	        this.displayNoLoadInReport = displayNoLoadInReport;
	    }

	    public boolean isDisplayReportSerialNo() {
	        return displayReportSerialNo;
	    }

	    public void setDisplayReportSerialNo(boolean displayReportSerialNo) {
	        this.displayReportSerialNo = displayReportSerialNo;
	    }

	    public boolean isDisplayTesterName() {
	        return displayTesterName;
	    }

	    public void setDisplayTesterName(boolean displayTesterName) {
	        this.displayTesterName = displayTesterName;
	    }

	    public boolean isDisplayPageNumber() {
	        return displayPageNumber;
	    }

	    public void setDisplayPageNumber(boolean displayPageNumber) {
	        this.displayPageNumber = displayPageNumber;
	    }

	    public boolean isDisplayVoltage() {
	        return displayVoltage;
	    }

	    public void setDisplayVoltage(boolean displayVoltage) {
	        this.displayVoltage = displayVoltage;
	    }

	    public boolean isDisplayTestPeriod() {
	        return displayTestPeriod;
	    }

	    public void setDisplayTestPeriod(boolean displayTestPeriod) {
	        this.displayTestPeriod = displayTestPeriod;
	    }

	    public boolean isDisplayDescription() {
	        return displayDescription;
	    }

	    public void setDisplayDescription(boolean displayDescription) {
	        this.displayDescription = displayDescription;
	    }

	    public boolean isDisplayResultStatus() {
	        return displayResultStatus;
	    }

	    public void setDisplayResultStatus(boolean displayResultStatus) {
	        this.displayResultStatus = displayResultStatus;
	    }

	    public boolean isDisplayResultValue() {
	        return displayResultValue;
	    }

	    public void setDisplayResultValue(boolean displayResultValue) {
	        this.displayResultValue = displayResultValue;
	    }

	    public boolean isDisplayOverRideResultStatus() {
	        return displayOverRideResultStatus;
	    }

	    public void setDisplayOverRideResultStatus(boolean displayOverRideResultStatus) {
	        this.displayOverRideResultStatus = displayOverRideResultStatus;
	    }

	    public boolean isDisplayResultStatusPrefix() {
	        return displayResultStatusPrefix;
	    }

	    public void setDisplayResultStatusPrefix(boolean displayResultStatusPrefix) {
	        this.displayResultStatusPrefix = displayResultStatusPrefix;
	    }

	    public boolean isDisplayResultStatusPostfix() {
	        return displayResultStatusPostfix;
	    }

	    public void setDisplayResultStatusPostfix(boolean displayResultStatusPostfix) {
	        this.displayResultStatusPostfix = displayResultStatusPostfix;
	    }

	    public boolean isDisplayOverRideResultValue() {
	        return displayOverRideResultValue;
	    }

	    public void setDisplayOverRideResultValue(boolean displayOverRideResultValue) {
	        this.displayOverRideResultValue = displayOverRideResultValue;
	    }

	    public boolean isDisplayResultValuePrefix() {
	        return displayResultValuePrefix;
	    }

	    public void setDisplayResultValuePrefix(boolean displayResultValuePrefix) {
	        this.displayResultValuePrefix = displayResultValuePrefix;
	    }

	    public boolean isDisplayResultValuePostfix() {
	        return displayResultValuePostfix;
	    }

	    public void setDisplayResultValuePostfix(boolean displayResultValuePostfix) {
	        this.displayResultValuePostfix = displayResultValuePostfix;
	    }
	}

	public class NoLoadReportResultValue {
		@SerializedName("DescriptionPassValue")
	    @Expose
	    private String descriptionPassValue;

	    @SerializedName("DescriptionFailValue")
	    @Expose
	    private String descriptionFailValue;

	    @SerializedName("ResultValueOverRidePassValue")
	    @Expose
	    private String resultValueOverRidePassValue;

	    @SerializedName("ResultValueOverRideFailValue")
	    @Expose
	    private String resultValueOverRideFailValue;

	    @SerializedName("ResultValuePrefixValue")
	    @Expose
	    private String resultValuePrefixValue;

	    @SerializedName("ResultValuePostfixValue")
	    @Expose
	    private String resultValuePostfixValue;

	    @SerializedName("ResultStatusOverRidePassValue")
	    @Expose
	    private String resultStatusOverRidePassValue;

	    @SerializedName("ResultStatusOverRideFailValue")
	    @Expose
	    private String resultStatusOverRideFailValue;

	    @SerializedName("ResultStatusPrefixValue")
	    @Expose
	    private String resultStatusPrefixValue;

	    @SerializedName("ResultStatusPostfixValue")
	    @Expose
	    private String resultStatusPostfixValue;

	    // Default constructor
	    public NoLoadReportResultValue() {
	    }

	    // Getters and Setters
	    public String getDescriptionPassValue() {
	        return descriptionPassValue;
	    }

	    public void setDescriptionPassValue(String descriptionPassValue) {
	        this.descriptionPassValue = descriptionPassValue;
	    }

	    public String getDescriptionFailValue() {
	        return descriptionFailValue;
	    }

	    public void setDescriptionFailValue(String descriptionFailValue) {
	        this.descriptionFailValue = descriptionFailValue;
	    }

	    public String getResultValueOverRidePassValue() {
	        return resultValueOverRidePassValue;
	    }

	    public void setResultValueOverRidePassValue(String resultValueOverRidePassValue) {
	        this.resultValueOverRidePassValue = resultValueOverRidePassValue;
	    }

	    public String getResultValueOverRideFailValue() {
	        return resultValueOverRideFailValue;
	    }

	    public void setResultValueOverRideFailValue(String resultValueOverRideFailValue) {
	        this.resultValueOverRideFailValue = resultValueOverRideFailValue;
	    }

	    public String getResultValuePrefixValue() {
	        return resultValuePrefixValue;
	    }

	    public void setResultValuePrefixValue(String resultValuePrefixValue) {
	        this.resultValuePrefixValue = resultValuePrefixValue;
	    }

	    public String getResultValuePostfixValue() {
	        return resultValuePostfixValue;
	    }

	    public void setResultValuePostfixValue(String resultValuePostfixValue) {
	        this.resultValuePostfixValue = resultValuePostfixValue;
	    }

	    public String getResultStatusOverRidePassValue() {
	        return resultStatusOverRidePassValue;
	    }

	    public void setResultStatusOverRidePassValue(String resultStatusOverRidePassValue) {
	        this.resultStatusOverRidePassValue = resultStatusOverRidePassValue;
	    }

	    public String getResultStatusOverRideFailValue() {
	        return resultStatusOverRideFailValue;
	    }

	    public void setResultStatusOverRideFailValue(String resultStatusOverRideFailValue) {
	        this.resultStatusOverRideFailValue = resultStatusOverRideFailValue;
	    }

	    public String getResultStatusPrefixValue() {
	        return resultStatusPrefixValue;
	    }

	    public void setResultStatusPrefixValue(String resultStatusPrefixValue) {
	        this.resultStatusPrefixValue = resultStatusPrefixValue;
	    }

	    public String getResultStatusPostfixValue() {
	        return resultStatusPostfixValue;
	    }

	    public void setResultStatusPostfixValue(String resultStatusPostfixValue) {
	        this.resultStatusPostfixValue = resultStatusPostfixValue;
	    }


	}

	public class ReportResultDataCellPosition {
		@SerializedName("SerialNoBeginCell")
		@Expose
		private String serialNoBeginCell;

		@SerializedName("TestTypeBeginCell")
		@Expose
		private String testTypeBeginCell;

		@SerializedName("TestNameBeginCell")
		@Expose
		private String testNameBeginCell;

		@SerializedName("AppliedVoltageBeginCell")
		@Expose
		private String appliedVoltageBeginCell;


		@SerializedName("AppliedCurrentBeginCell")
		@Expose
		private String appliedCurrentBeginCell;



		@SerializedName("PowerFactorBeginCell")
		@Expose
		private String powerFactorBeginCell;

		@SerializedName("PermissibleLimitBeginCell")
		@Expose
		private String permissibleLimitBeginCell;


		@SerializedName("ErrorValueBeginCell")
		@Expose
		private String errorValueBeginCell;

		@SerializedName("ErrorStatusBeginCell")
		@Expose
		private String errorStatusBeginCell;

		@SerializedName("OverAllStatusCell")
		@Expose
		private String overAllStatusCell;

		@SerializedName("ExecutedDateCell")
		@Expose
		private String executedDateCell;

		@SerializedName("ActiveMeterProfileModelMeterConfig")
		@Expose
		private String activeMeterProfileModelMeterConfig;

		public String getSerialNoBeginCell() {
			return serialNoBeginCell;
		}

		public String getTestTypeBeginCell() {
			return testTypeBeginCell;
		}

		public String getTestNameBeginCell() {
			return testNameBeginCell;
		}

		public String getAppliedCurrentBeginCell() {
			return appliedCurrentBeginCell;
		}

		public String getErrorValueBeginCell() {
			return errorValueBeginCell;
		}

		public String getErrorStatusBeginCell() {
			return errorStatusBeginCell;
		}

		public String getOverAllStatusCell() {
			return overAllStatusCell;
		}

		public String getExecutedDateCell() {
			return executedDateCell;
		}

		public String getActiveMeterProfileModelMeterConfig() {
			return activeMeterProfileModelMeterConfig;
		}

		public void setSerialNoBeginCell(String serialNoBeginCell) {
			this.serialNoBeginCell = serialNoBeginCell;
		}

		public void setTestTypeBeginCell(String testTypeBeginCell) {
			this.testTypeBeginCell = testTypeBeginCell;
		}

		public void setTestNameBeginCell(String testNameBeginCell) {
			this.testNameBeginCell = testNameBeginCell;
		}

		public void setAppliedCurrentBeginCell(String appliedCurrentBeginCell) {
			this.appliedCurrentBeginCell = appliedCurrentBeginCell;
		}

		public void setErrorValueBeginCell(String errorValueBeginCell) {
			this.errorValueBeginCell = errorValueBeginCell;
		}

		public void setErrorStatusBeginCell(String errorStatusBeginCell) {
			this.errorStatusBeginCell = errorStatusBeginCell;
		}

		public void setOverAllStatusCell(String overAllStatusCell) {
			this.overAllStatusCell = overAllStatusCell;
		}

		public void setExecutedDateCell(String executedDateCell) {
			this.executedDateCell = executedDateCell;
		}

		public void setActiveMeterProfileModelMeterConfig(String activeMeterProfileModelMeterConfig) {
			this.activeMeterProfileModelMeterConfig = activeMeterProfileModelMeterConfig;
		}

		public String getPowerFactorBeginCell() {
			return powerFactorBeginCell;
		}

		public String getPermissibleLimitBeginCell() {
			return permissibleLimitBeginCell;
		}

		public void setPowerFactorBeginCell(String powerFactorBeginCell) {
			this.powerFactorBeginCell = powerFactorBeginCell;
		}

		public void setPermissibleLimitBeginCell(String permissibleLimitBeginCell) {
			this.permissibleLimitBeginCell = permissibleLimitBeginCell;
		}

		public String getAppliedVoltageBeginCell() {
			return appliedVoltageBeginCell;
		}

		public void setAppliedVoltageBeginCell(String appliedVoltageBeginCell) {
			this.appliedVoltageBeginCell = appliedVoltageBeginCell;
		}
	}


	public class ResultDataPreAndPostFixDisplay {
		@SerializedName("DisplayPostFixAppliedVoltage")
		@Expose
		private boolean displayPostFixAppliedVoltage;
		
		@SerializedName("DisplayPreFixAppliedVoltage")
		@Expose
		private boolean displayPreFixAppliedVoltage;

		public boolean isDisplayPostFixAppliedVoltage() {
			return displayPostFixAppliedVoltage;
		}

		public void setDisplayPostFixAppliedVoltage(boolean displayPostFixAppliedVoltage) {
			this.displayPostFixAppliedVoltage = displayPostFixAppliedVoltage;
		}

		public boolean isDisplayPreFixAppliedVoltage() {
			return displayPreFixAppliedVoltage;
		}

		public void setDisplayPreFixAppliedVoltage(boolean displayPreFixAppliedVoltage) {
			this.displayPreFixAppliedVoltage = displayPreFixAppliedVoltage;
		}

	}


	public class ResultDataPreFixDisplay {
		@SerializedName("DisplayPreFixAppliedVoltage")
		@Expose
		private boolean displayPreFixAppliedVoltage;

		public boolean isDisplayPreFixAppliedVoltage() {
			return displayPreFixAppliedVoltage;
		}

		public void setDisplayPreFixAppliedVoltage(boolean displayPreFixAppliedVoltage) {
			this.displayPreFixAppliedVoltage = displayPreFixAppliedVoltage;
		}

	}

	public class ResultDataPreAndPostFixValue {
		@SerializedName("AppliedPostFixVoltageValue")
		@Expose
		private String appliedPostFixVoltageValue;
		
		@SerializedName("AppliedPreFixVoltageValue")
		@Expose
		private String appliedPreFixVoltageValue = "test";

		public String getAppliedPostFixVoltageValue() {
			return appliedPostFixVoltageValue;
		}

		public void setAppliedPostFixVoltageValue(String appliedPostFixVoltageValue) {
			this.appliedPostFixVoltageValue = appliedPostFixVoltageValue;
		}

		public String getAppliedPreFixVoltageValue() {
			return appliedPreFixVoltageValue;
		}

		public void setAppliedPreFixVoltageValue(String appliedPreFixVoltageValue) {
			this.appliedPreFixVoltageValue = appliedPreFixVoltageValue;
		}

	}





/*	public class ResultDataPreFixValue {
		@SerializedName("AppliedPreFixVoltageValue")
		@Expose
		private String appliedPreFixVoltageValue;

		public String getAppliedPreFixVoltageValue() {
			return appliedPreFixVoltageValue;
		}

		public void setAppliedPreFixVoltageValue(String appliedPreFixVoltageValue) {
			this.appliedPreFixVoltageValue = appliedPreFixVoltageValue;
		}

	}*/



	public class ResultDataDisplay {
		@SerializedName("DisplayAppliedVoltage")
		@Expose
		private boolean displayAppliedVoltage;
		
		@SerializedName("DisplayAppliedVoltageInFirstCellOnly")
		@Expose
		private boolean displayAppliedVoltageInFirstCellOnly;


		@SerializedName("DisplayAppliedCurrent")
		@Expose
		private boolean displayAppliedCurrent;


		@SerializedName("DisplayPowerFactor")
		@Expose
		private boolean displayPowerFactor;

		@SerializedName("DisplayPermissibleLimit")
		@Expose
		private boolean displayPermissibleLimit;
		

		@SerializedName("DisplayOverAllStatus")
		@Expose
		private boolean displayOverAllStatus;

		@SerializedName("DisplayErrorStatus")
		@Expose
		private boolean displayErrorStatus;

		@SerializedName("DisplayErrorValue")
		@Expose
		private boolean displayErrorValue;




		public boolean isDisplayAppliedCurrent() {
			return displayAppliedCurrent;
		}

		public boolean isDisplayErrorValue() {
			return displayErrorValue;
		}

		public void setDisplayAppliedCurrent(boolean displayAppliedCurrent) {
			this.displayAppliedCurrent = displayAppliedCurrent;
		}

		public void setDisplayErrorValue(boolean displayErrorValue) {
			this.displayErrorValue = displayErrorValue;
		}

		public boolean isDisplayPowerFactor() {
			return displayPowerFactor;
		}

		public boolean isDisplayPermissibleLimit() {
			return displayPermissibleLimit;
		}

		public boolean isDisplayErrorStatus() {
			return displayErrorStatus;
		}

		public void setDisplayPowerFactor(boolean displayPowerFactor) {
			this.displayPowerFactor = displayPowerFactor;
		}

		public void setDisplayPermissibleLimit(boolean displayPermissibleLimit) {
			this.displayPermissibleLimit = displayPermissibleLimit;
		}

		public void setDisplayErrorStatus(boolean displayErrorStatus) {
			this.displayErrorStatus = displayErrorStatus;
		}

		public boolean isDisplayAppliedVoltage() {
			return displayAppliedVoltage;
		}

		public void setDisplayAppliedVoltage(boolean displayAppliedVoltage) {
			this.displayAppliedVoltage = displayAppliedVoltage;
		}

		public boolean isDisplayAppliedVoltageInFirstCellOnly() {
			return displayAppliedVoltageInFirstCellOnly;
		}

		public void setDisplayAppliedVoltageInFirstCellOnly(boolean displayAppliedVoltageInFirstCellOnly) {
			this.displayAppliedVoltageInFirstCellOnly = displayAppliedVoltageInFirstCellOnly;
		}

		public boolean isDisplayOverAllStatus() {
			return displayOverAllStatus;
		}

		public void setDisplayOverAllStatus(boolean displayOverAllStatus) {
			this.displayOverAllStatus = displayOverAllStatus;
		}
	}

	public class MeterProfileReportCellPosition {
		@SerializedName("reportSerialNoCell")
		@Expose
		private String reportSerialNoCell;

		@SerializedName("customerNameCell")
		@Expose
		private String customerNameCell;

		@SerializedName("meterModelNoCell")
		@Expose
		private String meterModelNoCell;

		@SerializedName("meterTypeCell")
		@Expose
		private String meterTypeCell;

		@SerializedName("meterSerialNoCell")
		@Expose
		private String meterSerialNoCell;

		@SerializedName("meterClassCell")
		@Expose
		private String meterClassCell;

		@SerializedName("basicCurrentCell")
		@Expose
		private String basicCurrentCell;

		@SerializedName("maxCurrentCell")
		@Expose
		private String maxCurrentCell;

		@SerializedName("ratedVoltageCell")
		@Expose
		private String ratedVoltageCell;

		@SerializedName("noOfImpulsesPerUnitCell")
		@Expose
		private String noOfImpulsesPerUnitCell;

		@SerializedName("frequencyCell")
		@Expose
		private String frequencyCell;

		@SerializedName("ctTypeCell")
		@Expose
		private String ctTypeCell;

		@SerializedName("ctRatioCell")
		@Expose
		private String ctRatioCell;

		@SerializedName("ptRatioCell")
		@Expose
		private String ptRatioCell;

		@SerializedName("pageNumberCell")
		@Expose
		private String pageNumberCell;

		@SerializedName("noOfPagesCell")
		@Expose
		private String noOfPagesCell;

		@SerializedName("executedDateCell")
		@Expose
		private String executedDateCell;

		@SerializedName("reportGeneratedDateCell")
		@Expose
		private String reportGeneratedDateCell;

		@SerializedName("executedTimeCell")
		@Expose
		private String executedTimeCell;

		@SerializedName("reportGeneratedTimeCell")
		@Expose
		private String reportGeneratedTimeCell;

		@SerializedName("testerNameCell")
		@Expose
		private String testerNameCell;

		public String getReportSerialNoCell() {
			return reportSerialNoCell;
		}

		public String getCustomerNameCell() {
			return customerNameCell;
		}

		public String getMeterModelNoCell() {
			return meterModelNoCell;
		}

		public String getMeterTypeCell() {
			return meterTypeCell;
		}

		public String getMeterSerialNoCell() {
			return meterSerialNoCell;
		}

		public String getMeterClassCell() {
			return meterClassCell;
		}

		public String getBasicCurrentCell() {
			return basicCurrentCell;
		}

		public String getMaxCurrentCell() {
			return maxCurrentCell;
		}

		public String getRatedVoltageCell() {
			return ratedVoltageCell;
		}

		public String getNoOfImpulsesPerUnitCell() {
			return noOfImpulsesPerUnitCell;
		}

		public String getFrequencyCell() {
			return frequencyCell;
		}

		public String getCtTypeCell() {
			return ctTypeCell;
		}

		public String getCtRatioCell() {
			return ctRatioCell;
		}

		public String getPtRatioCell() {
			return ptRatioCell;
		}

		public String getPageNumberCell() {
			return pageNumberCell;
		}

		public String getNoOfPagesCell() {
			return noOfPagesCell;
		}

		public String getExecutedDateCell() {
			return executedDateCell;
		}

		public String getReportGeneratedDateCell() {
			return reportGeneratedDateCell;
		}

		public String getExecutedTimeCell() {
			return executedTimeCell;
		}

		public String getReportGeneratedTimeCell() {
			return reportGeneratedTimeCell;
		}

		public String getTesterNameCell() {
			return testerNameCell;
		}

		public void setReportSerialNoCell(String reportSerialNoCell) {
			this.reportSerialNoCell = reportSerialNoCell;
		}

		public void setCustomerNameCell(String customerNameCell) {
			this.customerNameCell = customerNameCell;
		}

		public void setMeterModelNoCell(String meterModelNoCell) {
			this.meterModelNoCell = meterModelNoCell;
		}

		public void setMeterTypeCell(String meterTypeCell) {
			this.meterTypeCell = meterTypeCell;
		}

		public void setMeterSerialNoCell(String meterSerialNoCell) {
			this.meterSerialNoCell = meterSerialNoCell;
		}

		public void setMeterClassCell(String meterClassCell) {
			this.meterClassCell = meterClassCell;
		}

		public void setBasicCurrentCell(String basicCurrentCell) {
			this.basicCurrentCell = basicCurrentCell;
		}

		public void setMaxCurrentCell(String maxCurrentCell) {
			this.maxCurrentCell = maxCurrentCell;
		}

		public void setRatedVoltageCell(String ratedVoltageCell) {
			this.ratedVoltageCell = ratedVoltageCell;
		}

		public void setNoOfImpulsesPerUnitCell(String noOfImpulsesPerUnitCell) {
			this.noOfImpulsesPerUnitCell = noOfImpulsesPerUnitCell;
		}

		public void setFrequencyCell(String frequencyCell) {
			this.frequencyCell = frequencyCell;
		}

		public void setCtTypeCell(String ctTypeCell) {
			this.ctTypeCell = ctTypeCell;
		}

		public void setCtRatioCell(String ctRatioCell) {
			this.ctRatioCell = ctRatioCell;
		}

		public void setPtRatioCell(String ptRatioCell) {
			this.ptRatioCell = ptRatioCell;
		}

		public void setPageNumberCell(String pageNumberCell) {
			this.pageNumberCell = pageNumberCell;
		}

		public void setNoOfPagesCell(String noOfPagesCell) {
			this.noOfPagesCell = noOfPagesCell;
		}

		public void setExecutedDateCell(String executedDateCell) {
			this.executedDateCell = executedDateCell;
		}

		public void setReportGeneratedDateCell(String reportGeneratedDateCell) {
			this.reportGeneratedDateCell = reportGeneratedDateCell;
		}

		public void setExecutedTimeCell(String executedTimeCell) {
			this.executedTimeCell = executedTimeCell;
		}

		public void setReportGeneratedTimeCell(String reportGeneratedTimeCell) {
			this.reportGeneratedTimeCell = reportGeneratedTimeCell;
		}

		public void setTesterNameCell(String testerNameCell) {
			this.testerNameCell = testerNameCell;
		}
	}
	
	public class MeterProfileReportPreAndPostFixValue{
		
        
        @SerializedName("PrefixMeterModelNoValue")
        @Expose
        private String prefixMeterModelNoValue;
        
        @SerializedName("PrefixBasicCurrentValue")
        @Expose
        private String prefixBasicCurrentValue;
        
        @SerializedName("PostfixBasicCurrentValue")
        @Expose
        private String postfixBasicCurrentValue;
        
        @SerializedName("PrefixMaxCurrentValue")
        @Expose
        private String prefixMaxCurrentValue;
        
        @SerializedName("PostfixMaxCurrentValue")
        @Expose
        private String postfixMaxCurrentValue;
        
        @SerializedName("PrefixRatedVoltageValue")
        @Expose
        private String prefixRatedVoltageValue;
        
        @SerializedName("PostfixRatedVoltageValue")
        @Expose
        private String postfixRatedVoltageValue;
        
                
        @SerializedName("PrefixMeterClassValue")
        @Expose
        private String prefixMeterClassValue;
        
        @SerializedName("PostfixMeterClassValue")
        @Expose
        private String postfixMeterClassValue;
        
        @SerializedName("PrefixFrequencyValue")
        @Expose
        private String prefixFrequencyValue;
        
        @SerializedName("PostfixFrequencyValue")
        @Expose
        private String postfixFrequencyValue;
        
        @SerializedName("PrefixNoOfImpulsesPerUnitValue")
        @Expose
        private String prefixNoOfImpulsesPerUnitValue;
        
        @SerializedName("PostfixNoOfImpulsesPerUnitValue")
        @Expose
        private String postfixNoOfImpulsesPerUnitValue;

		public String getPrefixMeterModelNoValue() {
			return prefixMeterModelNoValue;
		}

		public void setPrefixMeterModelNoValue(String prefixMeterModelNoValue) {
			this.prefixMeterModelNoValue = prefixMeterModelNoValue;
		}

		public String getPrefixBasicCurrentValue() {
			return prefixBasicCurrentValue;
		}

		public String getPostfixBasicCurrentValue() {
			return postfixBasicCurrentValue;
		}

		public String getPrefixMaxCurrentValue() {
			return prefixMaxCurrentValue;
		}

		public String getPostfixMaxCurrentValue() {
			return postfixMaxCurrentValue;
		}

		public String getPrefixRatedVoltageValue() {
			return prefixRatedVoltageValue;
		}

		public String getPostfixRatedVoltageValue() {
			return postfixRatedVoltageValue;
		}

		public void setPrefixBasicCurrentValue(String prefixBasicCurrentValue) {
			this.prefixBasicCurrentValue = prefixBasicCurrentValue;
		}

		public void setPostfixBasicCurrentValue(String postfixBasicCurrentValue) {
			this.postfixBasicCurrentValue = postfixBasicCurrentValue;
		}

		public void setPrefixMaxCurrentValue(String prefixMaxCurrentValue) {
			this.prefixMaxCurrentValue = prefixMaxCurrentValue;
		}

		public void setPostfixMaxCurrentValue(String postfixMaxCurrentValue) {
			this.postfixMaxCurrentValue = postfixMaxCurrentValue;
		}

		public void setPrefixRatedVoltageValue(String prefixRatedVoltageValue) {
			this.prefixRatedVoltageValue = prefixRatedVoltageValue;
		}

		public void setPostfixRatedVoltageValue(String postfixRatedVoltageValue) {
			this.postfixRatedVoltageValue = postfixRatedVoltageValue;
		}

		public String getPrefixMeterClassValue() {
			return prefixMeterClassValue;
		}

		public String getPostfixMeterClassValue() {
			return postfixMeterClassValue;
		}

		public String getPrefixFrequencyValue() {
			return prefixFrequencyValue;
		}

		public String getPostfixFrequencyValue() {
			return postfixFrequencyValue;
		}

		public String getPrefixNoOfImpulsesPerUnitValue() {
			return prefixNoOfImpulsesPerUnitValue;
		}

		public String getPostfixNoOfImpulsesPerUnitValue() {
			return postfixNoOfImpulsesPerUnitValue;
		}

		public void setPrefixMeterClassValue(String prefixMeterClassValue) {
			this.prefixMeterClassValue = prefixMeterClassValue;
		}

		public void setPostfixMeterClassValue(String postfixMeterClassValue) {
			this.postfixMeterClassValue = postfixMeterClassValue;
		}

		public void setPrefixFrequencyValue(String prefixFrequencyValue) {
			this.prefixFrequencyValue = prefixFrequencyValue;
		}

		public void setPostfixFrequencyValue(String postfixFrequencyValue) {
			this.postfixFrequencyValue = postfixFrequencyValue;
		}

		public void setPrefixNoOfImpulsesPerUnitValue(String prefixNoOfImpulsesPerUnitValue) {
			this.prefixNoOfImpulsesPerUnitValue = prefixNoOfImpulsesPerUnitValue;
		}

		public void setPostfixNoOfImpulsesPerUnitValue(String postfixNoOfImpulsesPerUnitValue) {
			this.postfixNoOfImpulsesPerUnitValue = postfixNoOfImpulsesPerUnitValue;
		}
		
		
	}

	public class MeterProfileReportDisplay {
		@SerializedName("displayReportSerialNo")
		@Expose
		private boolean displayReportSerialNo;

		@SerializedName("displayCustomerName")
		@Expose
		private boolean displayCustomerName;

		@SerializedName("displayMeterModelNo")
		@Expose
		private boolean displayMeterModelNo;
		
		@SerializedName("displayPrefixMeterModelNo")
		@Expose
		private boolean displayPrefixMeterModelNo;

		@SerializedName("displayMeterType")
		@Expose
		private boolean displayMeterType;

		@SerializedName("displayMeterClass")
		@Expose
		private boolean displayMeterClass;
		
		@SerializedName("displayPrefixMeterClass")
		@Expose
		private boolean displayPrefixMeterClass;
		
		@SerializedName("displayPostfixMeterClass")
		@Expose
		private boolean displayPostfixMeterClass;

		@SerializedName("displayMeterSerialNo")
		@Expose
		private boolean displayMeterSerialNo;

		@SerializedName("displayBasicCurrent")
		@Expose
		private boolean displayBasicCurrent;
		
		@SerializedName("displayPrefixBasicCurrent")
		@Expose
		private boolean displayPrefixBasicCurrent;
		
		@SerializedName("displayPostfixBasicCurrent")
		@Expose
		private boolean displayPostfixBasicCurrent;

		@SerializedName("displayMaxCurrent")
		@Expose
		private boolean displayMaxCurrent;
		
		@SerializedName("displayPrefixRatedVoltage")
		@Expose
		private boolean displayPrefixRatedVoltage;
		
		@SerializedName("displayPostfixRatedVoltage")
		@Expose
		private boolean displayPostfixRatedVoltage;

		@SerializedName("displayRatedVoltage")
		@Expose
		private boolean displayRatedVoltage;
		
		@SerializedName("displayPrefixMaxCurrent")
		@Expose
		private boolean displayPrefixMaxCurrent;
		
		@SerializedName("displayPostfixMaxCurrent")
		@Expose
		private boolean displayPostfixMaxCurrent;
		


		@SerializedName("displayNoOfImpulsesPerUnit")
		@Expose
		private boolean displayNoOfImpulsesPerUnit;
		
		
		@SerializedName("displayPrefixNoOfImpulsesPerUnit")
		@Expose
		private boolean displayPrefixNoOfImpulsesPerUnit;
		
		@SerializedName("displayPostfixNoOfImpulsesPerUnit")
		@Expose
		private boolean displayPostfixNoOfImpulsesPerUnit;

		public boolean isDisplayPrefixMeterClass() {
			return displayPrefixMeterClass;
		}

		public boolean isDisplayPostfixMeterClass() {
			return displayPostfixMeterClass;
		}

		public boolean isDisplayPrefixNoOfImpulsesPerUnit() {
			return displayPrefixNoOfImpulsesPerUnit;
		}

		public boolean isDisplayPostfixNoOfImpulsesPerUnit() {
			return displayPostfixNoOfImpulsesPerUnit;
		}

		public boolean isDisplayPrefixFrequency() {
			return displayPrefixFrequency;
		}

		public boolean isDisplayPostfixFrequency() {
			return displayPostfixFrequency;
		}

		public void setDisplayPrefixMeterClass(boolean displayPrefixMeterClass) {
			this.displayPrefixMeterClass = displayPrefixMeterClass;
		}

		public void setDisplayPostfixMeterClass(boolean displayPostfixMeterClass) {
			this.displayPostfixMeterClass = displayPostfixMeterClass;
		}

		public void setDisplayPrefixNoOfImpulsesPerUnit(boolean displayPrefixNoOfImpulsesPerUnit) {
			this.displayPrefixNoOfImpulsesPerUnit = displayPrefixNoOfImpulsesPerUnit;
		}

		public void setDisplayPostfixNoOfImpulsesPerUnit(boolean displayPostfixNoOfImpulsesPerUnit) {
			this.displayPostfixNoOfImpulsesPerUnit = displayPostfixNoOfImpulsesPerUnit;
		}

		public void setDisplayPrefixFrequency(boolean displayPrefixFrequency) {
			this.displayPrefixFrequency = displayPrefixFrequency;
		}

		public void setDisplayPostfixFrequency(boolean displayPostfixFrequency) {
			this.displayPostfixFrequency = displayPostfixFrequency;
		}

		@SerializedName("displayFrequency")
		@Expose
		private boolean displayFrequency;
		
		@SerializedName("displayPrefixFrequency")
		@Expose
		private boolean displayPrefixFrequency;
		
		@SerializedName("displayPostfixFrequency")
		@Expose
		private boolean displayPostfixFrequency;

		@SerializedName("displayCtType")
		@Expose
		private boolean displayCtType;

		@SerializedName("displayCtRatio")
		@Expose
		private boolean displayCtRatio;

		@SerializedName("displayPtRatio")
		@Expose
		private boolean displayPtRatio;

		public boolean isDisplayReportSerialNo() {
			return displayReportSerialNo;
		}

		public boolean isDisplayCustomerName() {
			return displayCustomerName;
		}

		public boolean isDisplayMeterModelNo() {
			return displayMeterModelNo;
		}

		public boolean isDisplayMeterType() {
			return displayMeterType;
		}

		public boolean isDisplayMeterClass() {
			return displayMeterClass;
		}

		public boolean isDisplayMeterSerialNo() {
			return displayMeterSerialNo;
		}

		public boolean isDisplayBasicCurrent() {
			return displayBasicCurrent;
		}

		public boolean isDisplayMaxCurrent() {
			return displayMaxCurrent;
		}

		public boolean isDisplayRatedVoltage() {
			return displayRatedVoltage;
		}

		public boolean isDisplayNoOfImpulsesPerUnit() {
			return displayNoOfImpulsesPerUnit;
		}

		public boolean isDisplayFrequency() {
			return displayFrequency;
		}

		public boolean isDisplayCtType() {
			return displayCtType;
		}

		public boolean isDisplayCtRatio() {
			return displayCtRatio;
		}

		public boolean isDisplayPtRatio() {
			return displayPtRatio;
		}

		public void setDisplayReportSerialNo(boolean displayReportSerialNo) {
			this.displayReportSerialNo = displayReportSerialNo;
		}

		public void setDisplayCustomerName(boolean displayCustomerName) {
			this.displayCustomerName = displayCustomerName;
		}

		public void setDisplayMeterModelNo(boolean displayMeterModelNo) {
			this.displayMeterModelNo = displayMeterModelNo;
		}

		public void setDisplayMeterType(boolean displayMeterType) {
			this.displayMeterType = displayMeterType;
		}

		public void setDisplayMeterClass(boolean displayMeterClass) {
			this.displayMeterClass = displayMeterClass;
		}

		public void setDisplayMeterSerialNo(boolean displayMeterSerialNo) {
			this.displayMeterSerialNo = displayMeterSerialNo;
		}

		public void setDisplayBasicCurrent(boolean displayBasicCurrent) {
			this.displayBasicCurrent = displayBasicCurrent;
		}

		public void setDisplayMaxCurrent(boolean displayMaxCurrent) {
			this.displayMaxCurrent = displayMaxCurrent;
		}

		public void setDisplayRatedVoltage(boolean displayRatedVoltage) {
			this.displayRatedVoltage = displayRatedVoltage;
		}

		public void setDisplayNoOfImpulsesPerUnit(boolean displayNoOfImpulsesPerUnit) {
			this.displayNoOfImpulsesPerUnit = displayNoOfImpulsesPerUnit;
		}

		public void setDisplayFrequency(boolean displayFrequency) {
			this.displayFrequency = displayFrequency;
		}

		public void setDisplayCtType(boolean displayCtType) {
			this.displayCtType = displayCtType;
		}

		public void setDisplayCtRatio(boolean displayCtRatio) {
			this.displayCtRatio = displayCtRatio;
		}

		public void setDisplayPtRatio(boolean displayPtRatio) {
			this.displayPtRatio = displayPtRatio;
		}

		public boolean isDisplayPrefixMeterModelNo() {
			return displayPrefixMeterModelNo;
		}

		public void setDisplayPrefixMeterModelNo(boolean displayPrefixMeterModelNo) {
			this.displayPrefixMeterModelNo = displayPrefixMeterModelNo;
		}

		public boolean isDisplayPrefixBasicCurrent() {
			return displayPrefixBasicCurrent;
		}

		public boolean isDisplayPostfixBasicCurrent() {
			return displayPostfixBasicCurrent;
		}

		public boolean isDisplayPrefixRatedVoltage() {
			return displayPrefixRatedVoltage;
		}

		public boolean isDisplayPostfixRatedVoltage() {
			return displayPostfixRatedVoltage;
		}

		public boolean isDisplayPrefixMaxCurrent() {
			return displayPrefixMaxCurrent;
		}

		public boolean isDisplayPostfixMaxCurrent() {
			return displayPostfixMaxCurrent;
		}

		public void setDisplayPrefixBasicCurrent(boolean displayPrefixBasicCurrent) {
			this.displayPrefixBasicCurrent = displayPrefixBasicCurrent;
		}

		public void setDisplayPostfixBasicCurrent(boolean displayPostfixBasicCurrent) {
			this.displayPostfixBasicCurrent = displayPostfixBasicCurrent;
		}

		public void setDisplayPrefixRatedVoltage(boolean displayPrefixRatedVoltage) {
			this.displayPrefixRatedVoltage = displayPrefixRatedVoltage;
		}

		public void setDisplayPostfixRatedVoltage(boolean displayPostfixRatedVoltage) {
			this.displayPostfixRatedVoltage = displayPostfixRatedVoltage;
		}

		public void setDisplayPrefixMaxCurrent(boolean displayPrefixMaxCurrent) {
			this.displayPrefixMaxCurrent = displayPrefixMaxCurrent;
		}

		public void setDisplayPostfixMaxCurrent(boolean displayPostfixMaxCurrent) {
			this.displayPostfixMaxCurrent = displayPostfixMaxCurrent;
		}
	}

	public ResultDataPreAndPostFixDisplay getResultDataPreAndPostFixDisplay() {
		return resultDataPreAndPostFixDisplay;
	}

	public ResultDataPreAndPostFixValue getResultDataPreAndPostFixValue() {
		return resultDataPreAndPostFixValue;
	}

/*	public ResultDataPreFixDisplay getResultDataPreFixDisplay() {
		return resultDataPreFixDisplay;
	}*/

/*	public ResultDataPreFixValue getResultDataPreFixValue() {
		return resultDataPreFixValue;
	}*/

	public void setResultDataPreAndPostFixDisplay(ResultDataPreAndPostFixDisplay resultDataPostFixDisplay) {
		this.resultDataPreAndPostFixDisplay = resultDataPostFixDisplay;
	}

	public void setResultDataPreAndPostFixValue(ResultDataPreAndPostFixValue resultDataPreAndPostFixValue) {
		this.resultDataPreAndPostFixValue = resultDataPreAndPostFixValue;
	}

	public StartingCurrentReportCellPosition getStartingCurrentReportCellPosition() {
		return startingCurrentReportCellPosition;
	}

	public StartingCurrentReportDisplay getStartingCurrentReportDisplay() {
		return startingCurrentReportDisplay;
	}

	public StartingCurrentReportResultValue getStartingCurrentReportResultValue() {
		return startingCurrentReportResultValue;
	}

	public void setStartingCurrentReportCellPosition(StartingCurrentReportCellPosition startingCurrentReportCellPosition) {
		this.startingCurrentReportCellPosition = startingCurrentReportCellPosition;
	}

	public void setStartingCurrentReportDisplay(StartingCurrentReportDisplay startingCurrentReportDisplay) {
		this.startingCurrentReportDisplay = startingCurrentReportDisplay;
	}

	public void setStartingCurrentReportResultValue(StartingCurrentReportResultValue startingCurrentReportResultValue) {
		this.startingCurrentReportResultValue = startingCurrentReportResultValue;
	}

	public FunctionalTestReportCellPosition getFunctionalTestReportCellPosition() {
		return functionalTestReportCellPosition;
	}

	public FunctionalTestReportDisplay getFunctionalTestReportDisplay() {
		return functionalTestReportDisplay;
	}

	public FunctionalTestReportResultValue getFunctionalTestReportResultValue() {
		return functionalTestReportResultValue;
	}

	public void setFunctionalTestReportCellPosition(FunctionalTestReportCellPosition functionalTestReportCellPosition) {
		this.functionalTestReportCellPosition = functionalTestReportCellPosition;
	}

	public void setFunctionalTestReportDisplay(FunctionalTestReportDisplay functionalTestReportDisplay) {
		this.functionalTestReportDisplay = functionalTestReportDisplay;
	}

	public void setFunctionalTestReportResultValue(FunctionalTestReportResultValue functionalTestReportResultValue) {
		this.functionalTestReportResultValue = functionalTestReportResultValue;
	}

	public HighVoltageReportCellPosition getHighVoltageReportCellPosition() {
		return highVoltageReportCellPosition;
	}

	public HighVoltageReportDisplay getHighVoltageReportDisplay() {
		return highVoltageReportDisplay;
	}

	public HighVoltageReportResultValue getHighVoltageReportResultValue() {
		return highVoltageReportResultValue;
	}

	public void setHighVoltageReportCellPosition(HighVoltageReportCellPosition highVoltageReportCellPosition) {
		this.highVoltageReportCellPosition = highVoltageReportCellPosition;
	}

	public void setHighVoltageReportDisplay(HighVoltageReportDisplay highVoltageReportDisplay) {
		this.highVoltageReportDisplay = highVoltageReportDisplay;
	}

	public void setHighVoltageReportResultValue(HighVoltageReportResultValue highVoltageReportResultValue) {
		this.highVoltageReportResultValue = highVoltageReportResultValue;
	}

	public InsulationResistanceReportCellPosition getInsulationResistanceReportCellPosition() {
		return insulationResistanceReportCellPosition;
	}

	public InsulationResistanceReportResultValue getInsulationResistanceReportResultValue() {
		return insulationResistanceReportResultValue;
	}

	public InsulationResistanceReportDisplay getInsulationResistanceReportDisplay() {
		return insulationResistanceReportDisplay;
	}

	public void setInsulationResistanceReportCellPosition(
			InsulationResistanceReportCellPosition insulationResistanceReportCellPosition) {
		this.insulationResistanceReportCellPosition = insulationResistanceReportCellPosition;
	}

	public void setInsulationResistanceReportResultValue(
			InsulationResistanceReportResultValue insulationResistanceReportResultValue) {
		this.insulationResistanceReportResultValue = insulationResistanceReportResultValue;
	}

	public void setInsulationResistanceReportDisplay(InsulationResistanceReportDisplay insulationResistanceReportDisplay) {
		this.insulationResistanceReportDisplay = insulationResistanceReportDisplay;
	}

	public RoutineSummaryReportCellPosition getRoutineSummaryReportCellPosition() {
		return routineSummaryReportCellPosition;
	}

	public RoutineSummaryReportDisplay getRoutineSummaryReportDisplay() {
		return routineSummaryReportDisplay;
	}

	public RoutineSummaryReportResultValue getRoutineSummaryReportResultValue() {
		return routineSummaryReportResultValue;
	}

	public void setRoutineSummaryReportCellPosition(RoutineSummaryReportCellPosition routineSummaryReportCellPosition) {
		this.routineSummaryReportCellPosition = routineSummaryReportCellPosition;
	}

	public void setRoutineSummaryReportDisplay(RoutineSummaryReportDisplay routineSummaryReportDisplay) {
		this.routineSummaryReportDisplay = routineSummaryReportDisplay;
	}

	public void setRoutineSummaryReportResultValue(RoutineSummaryReportResultValue routineSummaryReportResultValue) {
		this.routineSummaryReportResultValue = routineSummaryReportResultValue;
	}

	public MeterProfileReportPreAndPostFixValue getMeterProfileReportPreAndPostFixValue() {
		return meterProfileReportPreAndPostFixValue;
	}

	public void setMeterProfileReportPreAndPostFixValue(
			MeterProfileReportPreAndPostFixValue meterProfileReportPreAndPostFixValue) {
		this.meterProfileReportPreAndPostFixValue = meterProfileReportPreAndPostFixValue;
	}

	public boolean isUseDutSerialNoAsReportSerialNo() {
		return useDutSerialNoAsReportSerialNo;
	}

	public void setUseDutSerialNoAsReportSerialNo(boolean useDutSerialNoAsReportSerialNo) {
		this.useDutSerialNoAsReportSerialNo = useDutSerialNoAsReportSerialNo;
	}

	public String getReportSerialNoPrefix() {
		return ReportSerialNoPrefix;
	}

	public void setReportSerialNoPrefix(String reportSerialNoPrefix) {
		ReportSerialNoPrefix = reportSerialNoPrefix;
	}

	public String getDutWriteSerialNoPassword() {
		return dutWriteSerialNoPassword;
	}

	public void setDutWriteSerialNoPassword(String dutWriteSerialNoPassword) {
		this.dutWriteSerialNoPassword = dutWriteSerialNoPassword;
	}

	public String getReportSerialNoRegexSourceFormat() {
		return reportSerialNoRegexSourceFormat;
	}

	public void setReportSerialNoRegexSourceFormat(String reportSerialNoRegexSourceFormat) {
		this.reportSerialNoRegexSourceFormat = reportSerialNoRegexSourceFormat;
	}

	public String getReportSerialNoRegexTargetFormat() {
		return reportSerialNoRegexTargetFormat;
	}

	public void setReportSerialNoRegexTargetFormat(String reportSerialNoRegexTargetFormat) {
		this.reportSerialNoRegexTargetFormat = reportSerialNoRegexTargetFormat;
	}

	public int getMaxNoOfPalletsInVerific1() {
		return maxNoOfPalletsInVerific1;
	}

	public int getMaxNoOfPalletsInStaNld1() {
		return maxNoOfPalletsInStaNld1;
	}

	public void setMaxNoOfPalletsInVerific1(int maxNoOfPalletsInVerific1) {
		this.maxNoOfPalletsInVerific1 = maxNoOfPalletsInVerific1;
	}

	public void setMaxNoOfPalletsInStaNld1(int maxNoOfPalletsInStaNld1) {
		this.maxNoOfPalletsInStaNld1 = maxNoOfPalletsInStaNld1;
	}

	public int getPostDelaySta1ExitOpenStopLatch1_InSec() {
		return postDelaySta1ExitOpenStopLatch1_InSec;
	}

	public int getPostDelaySta1ExitCloseStopLatch1_InSec() {
		return postDelaySta1ExitCloseStopLatch1_InSec;
	}

	public int getPostDelaySta1ExitOpenStopLatch2_InSec() {
		return postDelaySta1ExitOpenStopLatch2_InSec;
	}

	public int getPostDelaySta1ExitCloseStopLatch2_InSec() {
		return postDelaySta1ExitCloseStopLatch2_InSec;
	}

	public void setPostDelaySta1ExitOpenStopLatch1_InSec(int postDelaySta1ExitOpenStopLatch1_InSec) {
		this.postDelaySta1ExitOpenStopLatch1_InSec = postDelaySta1ExitOpenStopLatch1_InSec;
	}

	public void setPostDelaySta1ExitCloseStopLatch1_InSec(int postDelaySta1ExitCloseStopLatch1_InSec) {
		this.postDelaySta1ExitCloseStopLatch1_InSec = postDelaySta1ExitCloseStopLatch1_InSec;
	}

	public void setPostDelaySta1ExitOpenStopLatch2_InSec(int postDelaySta1ExitOpenStopLatch2_InSec) {
		this.postDelaySta1ExitOpenStopLatch2_InSec = postDelaySta1ExitOpenStopLatch2_InSec;
	}

	public void setPostDelaySta1ExitCloseStopLatch2_InSec(int postDelaySta1ExitCloseStopLatch2_InSec) {
		this.postDelaySta1ExitCloseStopLatch2_InSec = postDelaySta1ExitCloseStopLatch2_InSec;
	}

	public int getSta1LastPalletExitWaitTime_InSec() {
		return sta1LastPalletExitWaitTime_InSec;
	}

	public void setSta1LastPalletExitWaitTime_InSec(int sta1LastPalletExitWaitTime_InSec) {
		this.sta1LastPalletExitWaitTime_InSec = sta1LastPalletExitWaitTime_InSec;
	}

/*	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}*/

	public int getUnloadingPalletEmptyStableDetectionWaitTime_InSec() {
		return unloadingPalletEmptyStableDetectionWaitTime_InSec;
	}

	public int getUnloadingQrScanningWaitTime_InSec() {
		return unloadingQrScanningWaitTime_InSec;
	}

	public void setUnloadingPalletEmptyStableDetectionWaitTime_InSec(
			int unloadingPalletEmptyStableDetectionWaitTime_InSec) {
		this.unloadingPalletEmptyStableDetectionWaitTime_InSec = unloadingPalletEmptyStableDetectionWaitTime_InSec;
	}

	public void setUnloadingQrScanningWaitTime_InSec(int unloadingQrScanningWaitTime_InSec) {
		this.unloadingQrScanningWaitTime_InSec = unloadingQrScanningWaitTime_InSec;
	}

	public float getFtRelayOnPhaseCurrentMinAccepted() {
		return ftRelayOnPhaseCurrentMinAccepted;
	}

	public float getFtRelayOnNeutralCurrentMinAccepted() {
		return ftRelayOnNeutralCurrentMinAccepted;
	}

	public void setFtRelayOnPhaseCurrentMinAccepted(float ftRelayOnPhaseCurrentMinAccepted) {
		this.ftRelayOnPhaseCurrentMinAccepted = ftRelayOnPhaseCurrentMinAccepted;
	}

	public void setFtRelayOnNeutralCurrentMinAccepted(float ftRelayOnNeutralCurrentMinAccepted) {
		this.ftRelayOnNeutralCurrentMinAccepted = ftRelayOnNeutralCurrentMinAccepted;
	}

	public int getMaxNoOfPalletsInStaNld2() {
		return maxNoOfPalletsInStaNld2;
	}

	public int getMaxNoOfPalletsInWaitingVerific1() {
		return maxNoOfPalletsInWaitingVerific1;
	}

	public void setMaxNoOfPalletsInStaNld2(int maxNoOfPalletsInStaNld2) {
		this.maxNoOfPalletsInStaNld2 = maxNoOfPalletsInStaNld2;
	}

	public void setMaxNoOfPalletsInWaitingVerific1(int maxNoOfPalletsInWaitingVerific1) {
		this.maxNoOfPalletsInWaitingVerific1 = maxNoOfPalletsInWaitingVerific1;
	}

	public int getHvExecutionTime_InSec() {
		return hvExecutionTime_InSec;
	}

	public void setHvExecutionTime_InSec(int hvExecutionTime_InSec) {
		this.hvExecutionTime_InSec = hvExecutionTime_InSec;
	}

	public int getIrExecutionTime_InSec() {
		return irExecutionTime_InSec;
	}

	public void setIrExecutionTime_InSec(int irExecutionTime_InSec) {
		this.irExecutionTime_InSec = irExecutionTime_InSec;
	}

	public boolean isFtSetDutToDefaultCalibrationTest() {
		return ftSetDutToDefaultCalibrationTest;
	}

	public void setFtSetDutToDefaultCalibrationTest(boolean ftSetDutToDefaultCalibrationTest) {
		this.ftSetDutToDefaultCalibrationTest = ftSetDutToDefaultCalibrationTest;
	}


	public List<String> getReportPrintDetailedTestTypeWhiteList() {
		return reportPrintDetailedTestTypeWhiteList;
	}

	public void setReportPrintDetailedTestTypeWhiteList(List<String> reportPrintDetailedTestTypeWhiteList) {
		this.reportPrintDetailedTestTypeWhiteList = reportPrintDetailedTestTypeWhiteList;
	}

	public List<String> getReportProcessSummaryOverAllStatusTestTypeWhiteList() {
		return reportProcessSummaryOverAllStatusTestTypeWhiteList;
	}

	public List<String> getReportPrintSummaryTestTypeWhiteList() {
		return reportPrintSummaryTestTypeWhiteList;
	}

	public void setReportProcessSummaryOverAllStatusTestTypeWhiteList(
			List<String> reportProcessSummaryOverAllStatusTestTypeWhiteList) {
		this.reportProcessSummaryOverAllStatusTestTypeWhiteList = reportProcessSummaryOverAllStatusTestTypeWhiteList;
	}

	public void setReportPrintSummaryTestTypeWhiteList(List<String> reportPrintSummaryTestTypeWhiteList) {
		this.reportPrintSummaryTestTypeWhiteList = reportPrintSummaryTestTypeWhiteList;
	}

	public List<String> getReportPrintDetailedTestPointWhiteList() {
		return reportPrintDetailedTestPointWhiteList;
	}

	public void setReportPrintDetailedTestPointWhiteList(List<String> reportPrintDetailedTestPointWhiteList) {
		this.reportPrintDetailedTestPointWhiteList = reportPrintDetailedTestPointWhiteList;
	}

	public int getSta1ExecutionTime_InSec() {
		return sta1ExecutionTime_InSec;
	}

	public int getSta2ExecutionTime_InSec() {
		return sta2ExecutionTime_InSec;
	}

	public void setSta1ExecutionTime_InSec(int sta1ExecutionTime_InSec) {
		this.sta1ExecutionTime_InSec = sta1ExecutionTime_InSec;
	}

	public void setSta2ExecutionTime_InSec(int sta2ExecutionTime_InSec) {
		this.sta2ExecutionTime_InSec = sta2ExecutionTime_InSec;
	}

	public int getTotalTestPointInSta1() {
		return totalTestPointInSta1;
	}

	public int getTotalTestPointInSta2() {
		return totalTestPointInSta2;
	}

	public int getTotalTestPointInVerific1() {
		return totalTestPointInVerific1;
	}

	public void setTotalTestPointInSta1(int totalTestPointInSta1) {
		this.totalTestPointInSta1 = totalTestPointInSta1;
	}

	public void setTotalTestPointInSta2(int totalTestPointInSta2) {
		this.totalTestPointInSta2 = totalTestPointInSta2;
	}

	public void setTotalTestPointInVerific1(int totalTestPointInVerific1) {
		this.totalTestPointInVerific1 = totalTestPointInVerific1;
	}

	public String getFtDutCmdTestProjectName() {
		return ftDutCmdTestProjectName;
	}

	public void setFtDutCmdTestProjectName(String ftDutCmdTestProjectName) {
		this.ftDutCmdTestProjectName = ftDutCmdTestProjectName;
	}

	public int getFtDutCmdEachTpBufferTimeInSec() {
		return ftDutCmdEachTpBufferTimeInSec;
	}

	public void setFtDutCmdEachTpBufferTimeInSec(int ftDutCmdEachTpBufferTimeInSec) {
		this.ftDutCmdEachTpBufferTimeInSec = ftDutCmdEachTpBufferTimeInSec;
	}

	public String getDutHardwareIdInitialValue() {
		return dutHardwareIdInitialValue;
	}

	public String setDutHardwareIdInitialValue(String dutHardwareIdInitialValue) {
		return this.dutHardwareIdInitialValue = dutHardwareIdInitialValue;
	}

	public int getCalibSuperCapacitorChargeWaitTimeInSec() {
		return calibSuperCapacitorChargeWaitTimeInSec;
	}

	public void setCalibSuperCapacitorChargeWaitTimeInSec(int calibSuperCapacitorChargeWaitTimeInSec) {
		this.calibSuperCapacitorChargeWaitTimeInSec = calibSuperCapacitorChargeWaitTimeInSec;
	}


}



