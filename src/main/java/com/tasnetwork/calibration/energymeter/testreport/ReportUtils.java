package com.tasnetwork.calibration.energymeter.testreport;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IgnoredErrorType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;
import com.tasnetwork.spring.orm.model.MeterResultDetailed;
import com.tasnetwork.spring.orm.model.MeterResultSummary;
import com.tasnetwork.spring.orm.model.PalletManage;
import com.tasnetwork.spring.orm.model.PalletMeter;
import com.tasnetwork.spring.orm.model.PalletMeterResults;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.control.Alert.AlertType;

public class ReportUtils {

	
	public enum TestTypeHandler {
	    CREEP(ConstantConveyor.CREEP_ALIAS_NAME, true) {
	        @Override
	        public void handle(MeterResultSummary summary, String testResult) {
	            summary.setTestTypeNoLoad(testResult);
	        }
	    },
	    STA(ConstantConveyor.STA_ALIAS_NAME, true) {
	        @Override
	        public void handle(MeterResultSummary summary, String testResult) {
	            summary.setTestTypeSta(testResult);
	        }
	    },
	    CALIB(ConstantConveyor.CALIB_ALIAS_NAME, false) {
	        @Override
	        public void handle(MeterResultSummary summary, String testResult, PalletMeterResults result) {
	            if (result.getTestCaseName().equals(ConstantConveyor.SUMMARY_CALIB_RESULT_TEST_NAME)) {
	                summary.setTestTypeCalib(testResult);
	            }
	        }
	    },
	    FT(ConstantConveyor.FT_ALIAS_NAME, false) {
	        @Override
	        public void handle(MeterResultSummary summary, String testResult, PalletMeterResults result) {
	            if (result.getTestCaseName().equals(ConstantConveyor.FT_RESULT_TEST_NAME)) {
	                summary.setTestTypeFt(testResult);
	            }
	        }
	    },
	    HV(ConstantConveyor.HV_ALIAS_NAME, false) {
	        @Override
	        public void handle(MeterResultSummary summary, String testResult) {
	            summary.setTestTypeHv(testResult);
	        }
	    },
	    IR(ConstantConveyor.IR_ALIAS_NAME, false) {
	        @Override
	        public void handle(MeterResultSummary summary, String testResult) {
	            summary.setTestTypeIr(testResult);
	        }
	    },
	    ACCURACY(ConstantConveyor.ACCURACY_ALIAS_NAME, false) {
	        @Override
	        public void handle(MeterResultSummary summary, String testResult, PalletMeterResults result) {
	            if (result.getTestCaseName().equals(ConstantConveyor.SUMMARY_VERIFICATION_RESULT_TEST_NAME)) {
	                summary.setTestVerific1(testResult);
	            }
	        }
	    },
	    COMM(ConstantConveyor.COMM_ALIAS_NAME, false) {
	        @Override
	        public void handle(MeterResultSummary summary, String testResult) {
	            summary.setTestTypeComm(testResult);
	        }
	    };

	    private final String aliasName;
	    private final boolean statusOnly;

	    TestTypeHandler(String aliasName, boolean statusOnly) {
	        this.aliasName = aliasName;
	        this.statusOnly = statusOnly;
	    }

	    public static TestTypeHandler fromAlias(String aliasName) {
	        return Arrays.stream(values())
	                   .filter(h -> h.aliasName.equals(aliasName))
	                   .findFirst()
	                   .orElse(null);
	    }

	    public boolean needsStatusOnly() {
	        return statusOnly;
	    }

	    public void handle(MeterResultSummary summary, String testResult, PalletMeterResults result) {
	        handle(summary, testResult);
	    }

	    protected void handle(MeterResultSummary summary, String testResult) {
	        // Default implementation for simple cases
	    }
	}
	
	
	/*public void processPalletMeterIndividualResult(String inpPalletDistinctId)  {
		ApplicationLauncher.logger.debug("processPalletMeterIndividualResult: " +inpPalletDistinctId);
		
		//PalletManage palletManage = MySqlServiceManager.getPalletManageService().findLastByPalletDistinctId(inpDistinctId);
		if ((!inpPalletDistinctId.isEmpty()) && ( inpPalletDistinctId!=null) ) {
			Optional<PalletManage> palletManageOpt = MySqlServiceManager.getPalletManageService().findByPalletDistinctId(inpPalletDistinctId);
			
			if(palletManageOpt.isPresent()) {
				PalletManage palletManage = palletManageOpt.get();
				Set<PalletMeter> palletMeterSetList = palletManage.getPalletMeterList();

				List<PalletMeterResults> palletMeterResultsList = new ArrayList<PalletMeterResults>();
				
				
				for (PalletMeter eachPalletMeter : palletMeterSetList) {
					palletMeterResultsList.addAll(eachPalletMeter.getPalletMeterResultsList());
				}
				ApplicationLauncher.logger.debug("palletMeterResultsList: size: " + palletMeterResultsList.size());
				//automateReportsForPallet(palletMeterResultsList);
				List<MeterResultSummary> summaryList = getResultSummary( palletMeterResultsList);
				//List<String> dutSerialNoList = palletMeterResultsList.stream().map(e->e.getMeterSerialNo())
				//			.collect(Collectors.toList());
				
				List<MeterResultDetailed> meterPrintDetailedList = new ArrayList<MeterResultDetailed>();
				List<MeterResultDetailed> meterPrintSummaryList = new ArrayList<MeterResultDetailed>();		
				int totalNoOfReports = summaryList.size();
				String overallStatus = "";
				for (int i =0 ; i< summaryList.size() ; i++) {

					meterPrintSummaryList = getPrintSummaryResult(summaryList.get(i), palletMeterResultsList);
					meterPrintDetailedList = getPrintDetailedResult(summaryList.get(i), palletMeterResultsList);
					String dutSerialNo = summaryList.get(i).getMeterSerialNo();
					overallStatus  = palletMeterSetList.stream()
						    .filter(e -> e.getMeterSerialNo().equals(dutSerialNo))
						    .findFirst() // Get the first matching PalletMeterSet
						    .map(PalletMeter::getOverAllTestResultStatus) // Extract status
						    .orElse(ConstantReport.REPORT_POPULATE_FAIL);
					ApplicationLauncher.logger.debug("processPalletMeterIndividualResult: inpPalletDistinctId: " +inpPalletDistinctId + ", overallStatus:<" + overallStatus + "> => " + summaryList.get(i).getMeterSerialNo() );		
					String testExecutedDateRaw = summaryList.get(i).getPalletDistinctId();
					String testExecutedDate = testExecutedDateRaw.contains("T")
							? StringUtils.substringBefore(testExecutedDateRaw, "T")
									: testExecutedDateRaw;
							String formattedTestExecutedDate = testExecutedDate;
							try {
								Date date = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).parse(testExecutedDate);
								formattedTestExecutedDate = new SimpleDateFormat("dd-MM-yyyy").format(date);
							} catch (ParseException e) {
								ApplicationLauncher.logger.error("Error parsing test executed date", e);
							}
							
							boolean promptWhenCompleted = false;
							exportIndividualMeterDetailedReport(
									meterPrintDetailedList,
									meterPrintSummaryList,
									summaryList.get(i),
									overallStatus,
									formattedTestExecutedDate,
									promptWhenCompleted
									);
							
					int reportsCompleted = i+1;
					ApplicationHomeController.updateBottomSecondaryStatus("Report : "+ reportsCompleted + "/" + (totalNoOfReports),ConstantApp.LEFT_STATUS_DEBUG);
					//Sleep(delayBetweenReports);
					
				}
						
						
						
				
				
			}
			
			
		}else {
			ApplicationLauncher.logger.debug("exportPalletMeterIndividualResult: PalletDistinctId is empty or null");
		}
	}*/
	
	public void generateIndividualReportsForPalletMeters(String inpPalletDistinctId) {
	    ApplicationLauncher.logger.debug("processPalletMeterIndividualResult: " + inpPalletDistinctId);
	    
	    if (!inpPalletDistinctId.isEmpty() && inpPalletDistinctId != null) {
	        Optional<PalletManage> palletManageOpt = MySqlServiceManager.getPalletManageService()
	            .findByPalletDistinctId(inpPalletDistinctId);
	        
	        if (palletManageOpt.isPresent()) {
	            PalletManage palletManage = palletManageOpt.get();
	            Set<PalletMeter> palletMeterSetList = palletManage.getPalletMeterList();

	            List<PalletMeterResults> palletMeterResultsList = palletMeterSetList.stream()
	                .flatMap(eachPalletMeter -> eachPalletMeter.getPalletMeterResultsList().stream())
	                .collect(Collectors.toList());
	            
	            ApplicationLauncher.logger.debug("palletMeterResultsList: size: " + palletMeterResultsList.size());
	            List<MeterResultSummary> summaryList = getResultSummary(palletMeterResultsList);
	            int totalNoOfReports = summaryList.size();
	            
	            // Thread-safe counter for progress tracking
	            AtomicInteger completedCounter = new AtomicInteger(0);
	            
	            // Parallel processing
	            summaryList.parallelStream().forEach(currentSummary -> {
	                try {
	                    List<MeterResultDetailed> meterPrintSummaryList = getPrintSummaryResult(currentSummary, palletMeterResultsList);
	                    List<MeterResultDetailed> meterPrintDetailedList = getPrintDetailedResult(currentSummary, palletMeterResultsList);
	                    
	                    String dutSerialNo = currentSummary.getMeterSerialNo();
	                    String overallStatus = palletMeterSetList.stream()
	                        .filter(e -> e.getMeterSerialNo().equals(dutSerialNo))
	                        .findFirst()
	                        .map(PalletMeter::getOverAllTestResultStatus)
	                        .orElse(ConstantReport.REPORT_POPULATE_FAIL);
	                        
	                    ApplicationLauncher.logger.debug("Processing: " + inpPalletDistinctId 
	                        + ", Status: " + overallStatus + ", Meter: " + currentSummary.getMeterSerialNo());
	                    
	                    // Date formatting
	                    String testExecutedDateRaw = currentSummary.getPalletDistinctId();
	                    String testExecutedDate = testExecutedDateRaw.contains("T")
	                        ? StringUtils.substringBefore(testExecutedDateRaw, "T")
	                        : testExecutedDateRaw;
	                    
	                    String formattedTestExecutedDate;
	                    try {
	                        Date date = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).parse(testExecutedDate);
	                        formattedTestExecutedDate = new SimpleDateFormat("dd-MM-yyyy").format(date);
	                    } catch (ParseException e) {
	                        ApplicationLauncher.logger.error("Error parsing date", e);
	                        formattedTestExecutedDate = testExecutedDate;
	                    }
	                    
	                    // Export report
	                    exportIndividualMeterDetailedReport(
	                        meterPrintDetailedList,
	                        meterPrintSummaryList,
	                        currentSummary,
	                        overallStatus,
	                        formattedTestExecutedDate,
	                        false
	                    );
	                    
	                    // Update progress (thread-safe)
	                    Platform.runLater(() -> {
	                        int completed = completedCounter.incrementAndGet();
	                        ApplicationHomeController.updateBottomSecondaryStatus(
	                            "Report : " + completed + "/" + totalNoOfReports,
	                            ConstantApp.LEFT_STATUS_DEBUG
	                        );
	                    });
	                    
	                } catch (Exception e) {
	                    ApplicationLauncher.logger.error("Error processing report for meter: " 
	                        + currentSummary.getMeterSerialNo(), e);
	                }
	            });
	        }
	    } else {
	        ApplicationLauncher.logger.debug("exportPalletMeterIndividualResult: PalletDistinctId is empty or null");
	    }
	}
	
	public void automateReportsForPallet(List<PalletMeterResults> palletMeterResultsList) {
		
		List<MeterResultSummary> summaryList = getResultSummary(palletMeterResultsList);

		/*// Get the list of unique meter summaries from the table view.
		ObservableList<MeterResultSummary> summaryList = ref_tvReportMeterResultSummary.getItems();*/

		if (summaryList == null || summaryList.isEmpty()) {
			ApplicationLauncher.logger.warn("No meter summaries available for report generation.");
			return;
		}
		
		summaryList.stream().forEachOrdered(e->{
			ApplicationLauncher.logger.debug("exportPalletMeterIndividualResult: getRackPositionNo: " + e.getRackPositionNo() + 
					"-> getMeterSerialNo:" + e.getMeterSerialNo() + 
					"-> getOverAllStatus:" + e.getOverAllStatus() +
					"-> Ft:" + e.getTestTypeFt() + 
					"-> Hv:" + e.getTestTypeHv()  + 
					"-> IF:" + e.getTestTypeIr()  + 
					"-> CL:" + e.getTestTypeCalib()  + 
					"-> VR:" + e.getTestVerific1()  + 
					"-> ST:" + e.getTestTypeSta() 
					);
			
		});
		
		int totalNoOfReports = summaryList.size();
		for (int i =0 ; i< summaryList.size() ; i++) {

			generateReportForMeter(summaryList.get(i), palletMeterResultsList);
			int reportsCompleted = i+1;
			ApplicationHomeController.updateBottomSecondaryStatus("Report : "+ reportsCompleted + "/" + (totalNoOfReports),ConstantApp.LEFT_STATUS_DEBUG);
			//Sleep(delayBetweenReports);
			
		}
		
		if(summaryList.size()>0) {
			ApplicationLauncher.logger.info("automateReportsForPallet: Reports Completed- All selected reports are generated- Prompted ");
			ApplicationLauncher.InformUser("Reports Completed", "All selected reports are generated", AlertType.INFORMATION);
		}
	}
	
	public List<MeterResultDetailed> getPrintDetailedResult(MeterResultSummary summary, List<PalletMeterResults> palletMeterResultsList) {
		
		ApplicationLauncher.logger.debug("getPrintDetailedResult: Entry : summary.getMeterSerialNo():" + summary.getMeterSerialNo());
		List<MeterResultDetailed> meterResultDetailedList = new ArrayList<>();
		
		
		for (PalletMeterResults result : palletMeterResultsList) {
			//ApplicationLauncher.logger.debug("getPrintDetailedResult: result.getMeterSerialNo(): " +result.getMeterSerialNo());
			//ApplicationLauncher.logger.debug("getPrintDetailedResult: summary.getMeterSerialNo(): " +summary.getMeterSerialNo());
			//ApplicationLauncher.logger.debug("getPrintDetailedResult: result.getPalletDistinctId(): " +result.getPalletDistinctId());
			//ApplicationLauncher.logger.debug("getPrintDetailedResult: summary.getPalletDistinctId(): " +summary.getPalletDistinctId());
			
			if (result.getMeterSerialNo().equals(summary.getMeterSerialNo()) &&
					//result.getPalletBatchNo() == summary.getPalletBatchNo()) {
					result.getPalletDistinctId().equals(summary.getPalletDistinctId()) &&
					(!result.getResultSummary())
					) {
				//ApplicationLauncher.logger.debug("getPrintDetailedResult: Hit1:");
				MeterResultDetailed meterResultDetailed = new MeterResultDetailed();
				String testResult = result.getResultStatus() + " " + result.getResultValue();
				meterResultDetailed.setResultValue(testResult);
				meterResultDetailed.setResultStatus(result.getResultStatus());
				meterResultDetailed.setTestName(result.getTestCaseName());
				meterResultDetailed.setTestType(result.getTestType());
				meterResultDetailed.setDutSerialNo(result.getMeterSerialNo());
				meterResultDetailed.setPalletDistinctId(result.getPalletDistinctId());
				
				String lowerLimitStr = result.getPermissibleLowerLimit();
				String upperLimitStr = result.getPermissibleUpperLimit();

				Double lowerLimit = null;
				Double upperLimit = null;

				// Process lower limit safely
				if (lowerLimitStr != null && !lowerLimitStr.trim().isEmpty()) {
					try {
						lowerLimit = Double.parseDouble(lowerLimitStr);
					} catch (NumberFormatException e) {
						ApplicationLauncher.logger.debug("Invalid number format for lower limit: " + lowerLimitStr);
					}
				}

				// Process upper limit safely
				if (upperLimitStr != null && !upperLimitStr.trim().isEmpty()) {
					try {
						upperLimit = Double.parseDouble(upperLimitStr);
					} catch (NumberFormatException e) {
						ApplicationLauncher.logger.debug("Invalid number format for upper limit: " + upperLimitStr);
					}
				}

				// Formatting display value
				String permissibleLimitDisplay;
				if (lowerLimit != null && upperLimit != null) {
					if (Math.abs(lowerLimit) == Math.abs(upperLimit) && lowerLimit != upperLimit) {
						permissibleLimitDisplay = "�" + Math.abs(lowerLimit);
					} else {
						//permissibleLimitDisplay = lowerLimit + "/" + upperLimit;
						permissibleLimitDisplay = 
								(lowerLimit >= 0 ? "+" + lowerLimit : String.valueOf(lowerLimit)) + 
								"/" + 
								(upperLimit >= 0 ? "+" + upperLimit : String.valueOf(upperLimit));
					}
				} else {
					permissibleLimitDisplay = ""; // Display empty if values are null
				}

				meterResultDetailed.setPermissibleLimitDisplay(permissibleLimitDisplay);
				//ApplicationLauncher.logger.debug("getPrintDetailedResult: getReportPrintDetailedTestTypeWhiteList():" +DeviceDataManagerController.getConveyorConfigParsedKey().getReportPrintDetailedTestTypeWhiteList());
				//ApplicationLauncher.logger.debug("getPrintDetailedResult: result.getTestType():" +result.getTestType());
				
				if (DeviceDataManagerController.getConveyorConfigParsedKey().getReportPrintDetailedTestTypeWhiteList().contains(result.getTestType())) {
					meterResultDetailedList.add(meterResultDetailed);
				}
			}
		}


		
		List<MeterResultDetailed> loeTests = meterResultDetailedList.stream()
				.filter(e -> e.getTestType().equals(ConstantConveyor.ACCURACY_ALIAS_NAME))
				.sorted((a, b) -> LoeSorter.compareLoeTests(a.getTestName(), b.getTestName()))
				.collect(Collectors.toList());
/*
		// Non-LOE test cases
		List<MeterResultDetailed> otherTests = meterResultDetailedList.stream()
				.filter(e -> !e.getTestType().equals(ConstantConveyor.ACCURACY_ALIAS_NAME))
				.collect(Collectors.toList());

		// Sort non-LOE tests based on predefined order
		otherTests.sort(Comparator
				.comparing((MeterResultDetailed e) -> ConstantConveyor.RESULT_REPORT_TEST_TYPE_ORDER_LIST.indexOf(e.getTestType()))
				.thenComparing(MeterResultDetailed::getTestName));

		// Merge LOE and other test results
		meterResultDetailedList.clear();
		meterResultDetailedList.addAll(otherTests);
		meterResultDetailedList.addAll(loeTests);*/
		
		loeTests.stream().forEachOrdered(e->{
			ApplicationLauncher.logger.debug("meterResultDetailedList: getSerialNo: "
					+ e.getDutSerialNo() +
					"-> getTestType:" + e.getTestType() + 
					"-> getTestName:" + e.getTestName() +
					"-> getResultValue:<" + e.getResultValue() + 
					"> , getResultStatus:<" + e.getResultStatus()  + 
					"> , Limit:" + e.getPermissibleLimitDisplay()  
					
					);
			
		});
		
		return loeTests;
		
	}
	
	
	public List<MeterResultDetailed> getPrintSummaryResult(MeterResultSummary summary, List<PalletMeterResults> palletMeterResultsList) {
		
		ApplicationLauncher.logger.debug("getPrintSummaryResult: Entry : summary.getMeterSerialNo():" + summary.getMeterSerialNo());
		List<MeterResultDetailed> meterSummaryList = new ArrayList<>();
		
		
		for (PalletMeterResults result : palletMeterResultsList) {
			//ApplicationLauncher.logger.debug("getPrintSummaryResult: result.getMeterSerialNo(): " +result.getMeterSerialNo());
			//ApplicationLauncher.logger.debug("getPrintSummaryResult: summary.getMeterSerialNo(): " +summary.getMeterSerialNo());
			//ApplicationLauncher.logger.debug("getPrintSummaryResult: result.getPalletDistinctId(): " +result.getPalletDistinctId());
			//ApplicationLauncher.logger.debug("getPrintSummaryResult: summary.getPalletDistinctId(): " +summary.getPalletDistinctId());
			
			if (result.getMeterSerialNo().equals(summary.getMeterSerialNo()) &&
					//result.getPalletBatchNo() == summary.getPalletBatchNo()) {
					result.getPalletDistinctId().equals(summary.getPalletDistinctId()) &&
					(result.getResultSummary())
					) {
				//ApplicationLauncher.logger.debug("getPrintSummaryResult: Hit1:");
				MeterResultDetailed meterSummary = new MeterResultDetailed();
				String testResult = result.getResultStatus() + " " + result.getResultValue();
				meterSummary.setResultValue(testResult);
				meterSummary.setResultStatus(result.getResultStatus());
				meterSummary.setTestName(result.getTestCaseName());
				meterSummary.setTestType(result.getTestType());
				meterSummary.setDutSerialNo(result.getMeterSerialNo());
				meterSummary.setPalletDistinctId(result.getPalletDistinctId());
				ApplicationLauncher.logger.debug("getPrintSummaryResult: getSerialNo: " +result.getMeterSerialNo() + " , result.getTestCaseName():" + result.getTestCaseName());  

				if (DeviceDataManagerController.getConveyorConfigParsedKey().getReportPrintSummaryTestTypeWhiteList().contains(result.getTestType())) {
					meterSummaryList.add(meterSummary);
				}
			}
		}



		
		meterSummaryList.stream().forEachOrdered(e->{
			ApplicationLauncher.logger.debug("getPrintSummaryResult: getSerialNo: "
					+ e.getDutSerialNo() +
					"-> getTestType:" + e.getTestType() + 
					"-> getTestName:" + e.getTestName() +
					"-> getResultValue:<" + e.getResultValue() + 
					"> , getResultStatus:<" + e.getResultStatus()  + 
					"> , Limit:" + e.getPermissibleLimitDisplay()  
					
					);
			
		});
		
		return meterSummaryList;
		
	}
	
	public List<MeterResultSummary> getResultSummary(List<PalletMeterResults> palletMeterResultsList) {
	    
		ApplicationLauncher.logger.debug("getResultSummary: Entry");
		
		// Filter results where getResultSummary is true
	    List<PalletMeterResults> filteredResults = palletMeterResultsList.stream()
	            .filter(PalletMeterResults::getResultSummary)
	            .collect(Collectors.toList());
	    ApplicationLauncher.logger.debug("getResultSummary: filteredResults.size: " +filteredResults.size());
	    // Create a map to track existing summaries for quick lookup
	    Map<String, Map<String, MeterResultSummary>> summaryMap = new LinkedHashMap<>();

	    // First pass: Create summary objects and organize by palletDistinctId and meterSerialNo
	    for (PalletMeterResults result : filteredResults) {
	        summaryMap.computeIfAbsent(result.getPalletDistinctId(), k -> new HashMap<>())
	                 .computeIfAbsent(result.getMeterSerialNo(), k -> {
	                     MeterResultSummary summary = new MeterResultSummary();
	                     summary.setPalletDistinctId(result.getPalletDistinctId());
	                     summary.setMeterSerialNo(result.getMeterSerialNo());
	                     return summary;
	                 });
	    }

	    // Second pass: Populate the summary objects with test results
	    for (PalletMeterResults result : filteredResults) {
	        MeterResultSummary summary = summaryMap.get(result.getPalletDistinctId()).get(result.getMeterSerialNo());
	        
	        // Set common fields
	        summary.setPalletBatchNo(result.getPalletBatchNo());
	        summary.setPalletQrId(result.getPalletQrId());
	        summary.setRackPositionNo(result.getRackPositionNo());

	        // Handle test type specific results using enum
	        TestTypeHandler handler = TestTypeHandler.fromAlias(result.getTestType());
	        if (handler != null) {
	            String testResult = result.getResultStatus() + " " + 
	                (handler.needsStatusOnly() ? result.getResultStatus() : result.getResultValue());
	            handler.handle(summary, testResult, result);
	        }
	    }

	    List<MeterResultSummary> summaryList = summaryMap.values().stream()
        .flatMap(m -> m.values().stream())
        .sorted(Comparator.comparingInt(MeterResultSummary::getPalletBatchNo)
                        .thenComparingInt(MeterResultSummary::getRackPositionNo))
        .collect(Collectors.toList());
	    
	    summaryList.stream().forEachOrdered(e->{
			ApplicationLauncher.logger.debug("getResultSummary: getRackPositionNo: " + e.getRackPositionNo() + 
					"-> getMeterSerialNo:" + e.getMeterSerialNo() + 
					"-> getOverAllStatus:" + e.getOverAllStatus() +
					"-> Ft:" + e.getTestTypeFt() + 
					"-> Hv:" + e.getTestTypeHv()  + 
					"-> Ir:" + e.getTestTypeIr()  + 
					"-> Cal:" + e.getTestTypeCalib()  + 
					"-> LOE:" + e.getTestVerific1()  + 
					"-> STA:" + e.getTestTypeSta() +
					"-> NoLoad:" + e.getTestTypeNoLoad() +
					"-> DistinctId:" + e.getPalletDistinctId()
					);
			
		});
	    // Convert map values to list and sort
	    return summaryList;
	}
	
	
	private void generateReportForMeter(MeterResultSummary summary, List<PalletMeterResults> palletMeterResultsList) {
		ApplicationLauncher.logger.info("Automated report generation for meter: " + summary.getMeterSerialNo());

		// Build the detailed results list for this meter.
		List<MeterResultDetailed> meterResultDetailedList = new ArrayList<>();
		List<String> resultDetailedExclusionList = new ArrayList<>(Arrays.asList(
				ConstantConveyor.SUMMARY_CALIB_RESULT_TEST_NAME,
				ConstantConveyor.SUMMARY_VERIFICATION_RESULT_TEST_NAME,
				ConstantConveyor.LED_PULSE_CHECK_RESULT_TEST_NAME,
				//ConstantConveyor.READ_PHASE_CURRENT_RESULT_TEST_NAME,
				//ConstantConveyor.READ_NEUTRAL_CURRENT_RESULT_TEST_NAME,
				
				ConstantConveyor.RELAY_ON_READ_PHASE_CURRENT_RESULT_TEST_NAME,
				ConstantConveyor.RELAY_ON_READ_NEUTRAL_CURRENT_RESULT_TEST_NAME,
				ConstantConveyor.RELAY_OFF_READ_PHASE_CURRENT_RESULT_TEST_NAME,
				ConstantConveyor.RELAY_OFF_READ_NEUTRAL_CURRENT_RESULT_TEST_NAME,
				
				ConstantConveyor.RELAY_ON_CMD_RESULT_TEST_NAME,
				ConstantConveyor.RELAY_OFF_CMD_RESULT_TEST_NAME,
				ConstantConveyor.DUT_OPTICAL_SERIAL_NO_READ_CMD_RESULT_TEST_NAME
				));

		// Filter the detailed results for the current meter based on its serial number and pallet batch number.
		for (PalletMeterResults result : palletMeterResultsList) {
			if (result.getMeterSerialNo().equals(summary.getMeterSerialNo()) &&
					result.getPalletBatchNo() == summary.getPalletBatchNo()) {

				MeterResultDetailed meterResultDetailed = new MeterResultDetailed();
				String testResult = result.getResultStatus() + " " + result.getResultValue();
				meterResultDetailed.setResultValue(testResult);
				meterResultDetailed.setResultStatus(result.getResultStatus());
				meterResultDetailed.setTestName(result.getTestCaseName());
				meterResultDetailed.setTestType(result.getTestType());
				
				String lowerLimitStr = result.getPermissibleLowerLimit();
				String upperLimitStr = result.getPermissibleUpperLimit();

				Double lowerLimit = null;
				Double upperLimit = null;

				// Process lower limit safely
				if (lowerLimitStr != null && !lowerLimitStr.trim().isEmpty()) {
					try {
						lowerLimit = Double.parseDouble(lowerLimitStr);
					} catch (NumberFormatException e) {
						ApplicationLauncher.logger.debug("Invalid number format for lower limit: " + lowerLimitStr);
					}
				}

				// Process upper limit safely
				if (upperLimitStr != null && !upperLimitStr.trim().isEmpty()) {
					try {
						upperLimit = Double.parseDouble(upperLimitStr);
					} catch (NumberFormatException e) {
						ApplicationLauncher.logger.debug("Invalid number format for upper limit: " + upperLimitStr);
					}
				}

				// Formatting display value
				String permissibleLimitDisplay;
				if (lowerLimit != null && upperLimit != null) {
					if (Math.abs(lowerLimit) == Math.abs(upperLimit) && lowerLimit != upperLimit) {
						permissibleLimitDisplay = "�" + Math.abs(lowerLimit);
					} else {
						//permissibleLimitDisplay = lowerLimit + "/" + upperLimit;
						permissibleLimitDisplay = 
								(lowerLimit >= 0 ? "+" + lowerLimit : String.valueOf(lowerLimit)) + 
								"/" + 
								(upperLimit >= 0 ? "+" + upperLimit : String.valueOf(upperLimit));
					}
				} else {
					permissibleLimitDisplay = ""; // Display empty if values are null
				}

				meterResultDetailed.setPermissibleLimitDisplay(permissibleLimitDisplay);
				if (!resultDetailedExclusionList.contains(result.getTestCaseName())) {
					meterResultDetailedList.add(meterResultDetailed);
				}
			}
		}

		// Sort the detailed results as in your current logic.
		/*meterResultDetailedList.sort(Comparator
				.comparing((MeterResultDetailed e) -> ConstantConveyor.RESULT_REPORT_TEST_TYPE_ORDER_LIST.indexOf(e.getTestType()))
				.thenComparing(MeterResultDetailed::getTestName)
				);*/
		
		List<MeterResultDetailed> loeTests = meterResultDetailedList.stream()
				.filter(e -> e.getTestType().equals(ConstantConveyor.ACCURACY_ALIAS_NAME))
				.sorted((a, b) -> LoeSorter.compareLoeTests(a.getTestName(), b.getTestName()))
				.collect(Collectors.toList());

		// Non-LOE test cases
		List<MeterResultDetailed> otherTests = meterResultDetailedList.stream()
				.filter(e -> !e.getTestType().equals(ConstantConveyor.ACCURACY_ALIAS_NAME))
				.collect(Collectors.toList());

		// Sort non-LOE tests based on predefined order
		otherTests.sort(Comparator
				.comparing((MeterResultDetailed e) -> ConstantConveyor.RESULT_REPORT_TEST_TYPE_ORDER_LIST.indexOf(e.getTestType()))
				.thenComparing(MeterResultDetailed::getTestName));

		// Merge LOE and other test results
		meterResultDetailedList.clear();
		meterResultDetailedList.addAll(otherTests);
		meterResultDetailedList.addAll(loeTests);

		// Determine overall status based on the details.
		String overallStatus = meterResultDetailedList.stream().noneMatch(
				e -> e.getResultStatus().equals(ConstantReport.REPORT_POPULATE_FAIL))
				? ConstantReport.REPORT_POPULATE_PASS : ConstantReport.REPORT_POPULATE_FAIL;

		// Determine the test executed date.
		// In your original code the palletDistinctId holds a date/time stamp; adjust this as needed.
		String testExecutedDateRaw = summary.getPalletDistinctId();
		String testExecutedDate = testExecutedDateRaw.contains("T")
				? StringUtils.substringBefore(testExecutedDateRaw, "T")
						: testExecutedDateRaw;
				String formattedTestExecutedDate = testExecutedDate;
				try {
					Date date = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).parse(testExecutedDate);
					formattedTestExecutedDate = new SimpleDateFormat("dd-MM-yyyy").format(date);
				} catch (ParseException e) {
					ApplicationLauncher.logger.error("Error parsing test executed date", e);
				}

				// Finally, call your export method to generate the report for this meter.
				boolean promptWhenCompleted = false;
/*				exportIndividualMeterDetailedReport(
						meterResultDetailedList,
						summary.getMeterSerialNo(),
						overallStatus,
						formattedTestExecutedDate,
						promptWhenCompleted
						);*/
	}
	
	

	
	
	public Boolean exportIndividualMeterDetailedReport(List<MeterResultDetailed> meterPrintDetailedList , 
			List<MeterResultDetailed> meterPrintSummaryList, MeterResultSummary meterResultSummary,
			
			String dutOverAllStatus,String testExecutedDate,boolean promptWhenCompleted){

		ApplicationLauncher.logger.debug("exportIndividualMeterDetailedReport: Entry");
		
		String dutSerialNo = meterResultSummary.getMeterSerialNo();
		//String meterSerialNo = "MSer1234";
		boolean status = false;
		boolean TemplateFilePathExist = false;
		boolean OutputFilePathExist = true;
		HSSFWorkbook HSSFworkbook = null;
		HSSFSheet HSSF_Sheet = null;
		XSSFWorkbook XSSFworkbook = null;
		XSSFSheet XSSF_Sheet = null;
		boolean hssf_Format = true;
		FileInputStream file = null;

		boolean overAllStatus = true;

		String templateFilePathLocation = DeviceDataManagerController.getConveyorConfigParsedKey().getTemplateFileLocationPath();
		String targetOutputFilePathLocation = DeviceDataManagerController.getConveyorConfigParsedKey().getReportOutputPath();


		try {


			//file = new FileInputStream(new File(ConstantReport.METER_PROFILE_REPORT_TEMPL_FILE_LOCATION));
			file = new FileInputStream(new File(templateFilePathLocation));
			TemplateFilePathExist = true;

			try{
				//HSSFworkbook = new HSSFWorkbook(file);
				//HSSF_Sheet = HSSFworkbook.getSheetAt(0);
				hssf_Format= false;
				file = new FileInputStream(new File(templateFilePathLocation));

				XSSFworkbook = new XSSFWorkbook(file);
				XSSF_Sheet = XSSFworkbook.getSheetAt(0);
			}catch(Exception e1){
				e1.printStackTrace();
				hssf_Format= false;
				file.close();
				//file = new FileInputStream(new File(ConstantReport.METER_PROFILE_REPORT_TEMPL_FILE_LOCATION));
				/*file = new FileInputStream(new File(templateFilePathLocation));

				XSSFworkbook = new XSSFWorkbook(file);
				XSSF_Sheet = XSSFworkbook.getSheetAt(0);*/

			}
			//result = FilterResultByTestType(result, ConstantApp.SELF_HEATING_ALIAS_NAME);
			//String volt = ConstantReport.SELF_HEAT_TEMPL_VOLTAGE;
			//ArrayList<String> currents = ConstantReport.SELF_HEAT_TEMPL_CURRENTS;
			//ArrayList<String> pfs = ConstantReport.SELF_HEAT_TEMPL_PFS;

			//ApplicationLauncher.logger.debug("exportIndividualMeterDetailedReport: getSelectedProjectName: "+ getSelectedProjectName());
			if(hssf_Format){
				//status= populateMeterProfileData(HSSF_Sheet, result, volt, currents, pfs);
			}else{
				//status= populateMeterProfileData(XSSF_Sheet, getSelectedProjectName());

				status= populateMeterIndividualDetailedReportMetaData(XSSF_Sheet, meterPrintSummaryList,  dutSerialNo, dutOverAllStatus, testExecutedDate);

				status = populateMeterIndividualReportAccuracyData(XSSF_Sheet, meterPrintDetailedList);
				status = populateMeterIndividualReportNoLoadData(XSSF_Sheet, meterPrintSummaryList);
				//status = populateMeterIndividualReportSummaryNoLoadData(XSSF_Sheet, meterResultSummary);
				
				status = populateMeterIndividualReportStartingCurrentData(XSSF_Sheet, meterPrintSummaryList);
				status = populateMeterIndividualReportFunctionalTestData(XSSF_Sheet, meterPrintSummaryList);
				status = populateMeterIndividualReportHighVoltageData(XSSF_Sheet, meterPrintSummaryList);
				status = populateMeterIndividualReportInsulationResistanceData(XSSF_Sheet, meterPrintSummaryList);	

				if(!status) {
					overAllStatus = false;
				}


				status= populateMeterProfileDataV2(XSSF_Sheet);

				if(!status) {
					overAllStatus = false;
				}

				status= appendDataAndMergeCells(XSSF_Sheet);

				if(!status) {
					overAllStatus = false;
				}
			}

			/*			if(hssf_Format){
				status= PopulateSelfHeatResultsHSSF(HSSF_Sheet, result, volt, currents, pfs);
			}else{
				status= PopulateSelfHeatResultsXSSF(XSSF_Sheet, result, volt, currents, pfs);
			}*/
			file.close();


			String formattedTestExecutedDate = testExecutedDate;
			DateFormat originalFormat = new SimpleDateFormat(ConstantAppConfig.REPORT_DATE_FORMAT, Locale.ENGLISH);
			DateFormat targetFormat = new SimpleDateFormat("yyyy_MM_dd");
			//String formattedTestExecutedDate
			// date;
			try {
				Date date = originalFormat.parse(testExecutedDate);
				formattedTestExecutedDate = targetFormat.format(date); 
			} catch (ParseException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			String file_path = targetOutputFilePathLocation+formattedTestExecutedDate+"//";//getSaveFilePath(targetOutputFilePathLocation);//ConstantReport.SAVE_FILE_LOCATION);
			String file_name = dutSerialNo+".xls";//getSaveFileName(targetOutputFilePathLocation+"Result.xlsx");//ConstantReport.METER_PROFILE_REPORT_TEMPL_FILE_LOCATION);
			if (!new File(file_path).exists())
			{
				//OutputFilePathExist = false;
				Files.createDirectories(Paths.get(file_path));
			}

			FileOutputStream out = null;

			if(hssf_Format){
				out = 	new FileOutputStream(new File(file_path + file_name));
				HSSFFormulaEvaluator.evaluateAllFormulaCells(HSSFworkbook);
				HSSFworkbook.write(out);
			}else{
				out = new FileOutputStream(new File(file_path + file_name.replace(".xls", ".xlsx")));
				XSSFFormulaEvaluator.evaluateAllFormulaCells(XSSFworkbook);
				XSSFworkbook.write(out);
			}
			out.close();

			if(overAllStatus) {
				//Sleep(1000);
				/*ApplicationHomeController.update_left_status("Report generation Success",ConstantApp.LEFT_STATUS_DEBUG);

				ApplicationLauncher.logger.info("exportIndividualMeterDetailedReport: Export Successful- Report generated successfully- Prompted");

				ApplicationLauncher.InformUser("Export Successful", "Report generated successfully", AlertType.INFORMATION);*/

				//ApplicationHomeController.update_left_status("Report generation Success",ConstantApp.LEFT_STATUS_DEBUG);

				ApplicationLauncher.logger.info("exportIndividualMeterDetailedReport: Export Successful- Report generated successfully- Prompted");

				String outputReportFileName=file_name.replace(".xls", ".xlsx");//getReportGeneratedFileName();
				String outputReportPath = file_path.replace("//", "");//.replace("\\", "\\\\");//getReportGeneratedPath();

				if(ConstantAppConfig.REPORT_CUSTOM_EXPORT_AS_PDF_ENABLED){
					//if(ProcalFeatureEnable.REPORT_GENERATION_V2_ENABLED){
					//status = SaveExcelAsPDF();
					status = saveExcelAsPDFWithPathAndFileName( outputReportPath, outputReportFileName);
					outputReportFileName = outputReportFileName.replace(".xlsx", ".pdf").replace(".xls", ".pdf");
					//}
				}

				ApplicationLauncher.logger.info("exportIndividualMeterDetailedReport: outputReportFileName: "+ outputReportFileName);
				ApplicationLauncher.logger.info("exportIndividualMeterDetailedReport: outputReportPath: "+ outputReportPath);
				// success print below 
				//2025-03-22 13:24:48.176 INFO  ProCon:5475 - exportIndividualMeterDetailedReport: outputReportFileName: ID674056.pdf
				//2025-03-22 13:24:48.176 INFO  ProCon:5476 - exportIndividualMeterDetailedReport: outputReportPath: C:\Reports\Conveyor\Output\2025_03_20
				//ApplicationLauncher.InformUser("Export Successful", "Report generated successfully", AlertType.INFORMATION);
				if(promptWhenCompleted) {
					promptUserToOpenReportOutputFolderPath(outputReportFileName, outputReportPath);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("exportIndividualMeterDetailedReport: FileNotFoundException:"+e.getMessage());
			status = false;
			if (!TemplateFilePathExist){
				ApplicationLauncher.InformUser("Template not found", "Conveyor Report template configured for Meter Profile not found. Kindly reconfigure", AlertType.ERROR);
			}else if (!OutputFilePathExist){
				ApplicationLauncher.InformUser("Output Path not found", "Conveyor Report output path for Meter Profile not found. Kindly reconfigure", AlertType.ERROR);

			}else {
				ApplicationLauncher.InformUser("File access failed", "Conveyor Access denied for output path file for Meter Profile. Kindly close the excel file if opened and try again", AlertType.ERROR);

			}
		} catch (IOException e) {
			e.printStackTrace();
			status = false;
			ApplicationLauncher.logger.error("exportIndividualMeterDetailedReport: IOException:"+e.getMessage());
		}
/*		try {
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("exportIndividualMeterDetailedReport: Exception: setCursor-3"+e.getMessage());
			
		}*/
		return status;

	}
	
	public Boolean populateMeterIndividualDetailedReportMetaData(XSSFSheet sheet1, List<MeterResultDetailed> meterResultDetailedList,String dutSerialNo,
			String dutOverAllStatus,String testExecutedDate){

		boolean status = true;
		ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: Entry ");
		String resultCellPosition = "";
		boolean overAllStatus = true;
		String resultData = "";
		boolean populateMeterSerialNo = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportDisplay().isDisplayMeterSerialNo();
		if(populateMeterSerialNo){
			resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportCellPosition().getMeterSerialNoCell();

			status = FillReportDataColumnXSSF(sheet1,dutSerialNo ,resultCellPosition);
			if(!status) {
				overAllStatus =false;
			}
		}

		boolean populateRoutineSummaryMeterSerialNo = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportDisplay().isDisplayMeterSerialNo();
		if(populateRoutineSummaryMeterSerialNo){
			resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportCellPosition().getMeterSerialNoCell();
			resultData = dutSerialNo;
			boolean prefixEnabled = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportDisplay().isDisplayPrefixMeterSerialNo();
			//ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: RoutineSummary PropertyOf prefixEnabled: " + prefixEnabled);
			if(prefixEnabled) {
				String prefixData = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportResultValue().getPrefixMeterSerialNoValue();
				//ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: RoutineSummary PropertyOf prefixData: " + prefixData);

				resultData = prefixData + dutSerialNo;
			}
			status = FillReportDataColumnXSSF(sheet1, resultData,resultCellPosition);
			if(!status) {
				overAllStatus =false;
			}
		}


		/*		boolean populateOverAllStatus = DeviceDataManagerController.getConveyorConfigParsedKey().getResultDataDisplay().isDisplayOverAllStatus();
		if(populateOverAllStatus){
			resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getReportResultDataCellPosition().getOverAllStatusCell();

			status = FillReportDataColumnXSSF(sheet1, dutOverAllStatus,resultCellPosition);
			if(!status) {
				overAllStatus =false;
			}
		}*/




		boolean populateRoutineTestPropertOfEnabled = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportDisplay().isDisplayPropertyOf();
		if(populateRoutineTestPropertOfEnabled){
			ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: populateRoutineTestPropertOfEnabled enabled");	

			resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportCellPosition().getPropertyOfCell();
			//ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: populateRoutineTestPropertOfEnabled enabled");	
			resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportResultValue().getPropertyOfValue();
			ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: RoutineSummary PropertyOf resultData: " + resultData);
			boolean prefixEnabled = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportDisplay().isDisplayPrefixPropertyOf();
			ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: RoutineSummary PropertyOf prefixEnabled: " + prefixEnabled);
			if(prefixEnabled) {
				String prefixData = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportResultValue().getPrefixPropertyOfValue();
				ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: RoutineSummary PropertyOf prefixData: " + prefixData);

				resultData = prefixData + resultData;
			}

			status = FillReportDataColumnXSSF(sheet1, resultData,resultCellPosition);

		}


		boolean populateRoutineTestTendorNoEnabled = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportDisplay().isDisplayTenderNo();
		if(populateRoutineTestTendorNoEnabled){
			ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: populateRoutineTestTendorNoEnabled enabled");	

			resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportCellPosition().getTenderNoCell();
			ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: RoutineSummary TenderNo resultCellPosition: " + resultCellPosition);	
			resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportResultValue().getTenderNoValue();
			ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: RoutineSummary TenderNo resultData: " + resultData);
			boolean prefixEnabled = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportDisplay().isDisplayPrefixTenderNo();
			ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: RoutineSummary TenderNo prefixEnabled: " + prefixEnabled);
			if(prefixEnabled) {
				String prefixData = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportResultValue().getPrefixTenderNoValue();
				ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: RoutineSummary TenderNo prefixData: " + prefixData);

				resultData = prefixData + resultData;
			}

			status = FillReportDataColumnXSSF(sheet1, resultData,resultCellPosition);

		}


		boolean populateRoutineTestIsSpecEnabled = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportDisplay().isDisplayIs_Spec();
		if(populateRoutineTestIsSpecEnabled){
			ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: populateRoutineTestIsSpecEnabled enabled");	

			resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportCellPosition().getiS_SpecCell();
			//ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: populateRoutineTestPropertOfEnabled enabled");	
			resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportResultValue().getIsi_SpecValue();
			ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: RoutineSummary IsSpec resultData: " + resultData);
			boolean prefixEnabled = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportDisplay().isDisplayPrefixIs_Spec();
			ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: RoutineSummary IsSpec prefixEnabled: " + prefixEnabled);
			if(prefixEnabled) {
				String prefixData = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportResultValue().getPrefixIs_SpecValue();
				ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: RoutineSummary IsSpec prefixData: " + prefixData);

				resultData = prefixData + resultData;
			}

			boolean postfixEnabled = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportDisplay().isDisplayPostfixIs_Spec();
			ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: RoutineSummary IsSpec prefixEnabled: " + prefixEnabled);
			if(postfixEnabled) {
				String postfixData = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportResultValue().getPostfixIs_SpecValue();
				ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: RoutineSummary IsSpec postfixData: " + postfixData);

				resultData =  resultData + postfixData;
			}

			status = FillReportDataColumnXSSF(sheet1, resultData,resultCellPosition);

		}


		boolean populateRoutineTestCategoryEnabled = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportDisplay().isDisplayCategory();
		if(populateRoutineTestCategoryEnabled){
			ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: populateRoutineTestCategoryEnabled enabled");	

			resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportCellPosition().getCategoryCell();
			//ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: populateRoutineTestPropertOfEnabled enabled");	
			resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportResultValue().getCategoryValue();
			ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: RoutineSummary Category resultData: " + resultData);
			boolean prefixEnabled = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportDisplay().isDisplayPrefixCategory();
			ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: RoutineSummary Category prefixEnabled: " + prefixEnabled);
			if(prefixEnabled) {
				String prefixData = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportResultValue().getPrefixCategoryValue();
				ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: RoutineSummary Category prefixData: " + prefixData);

				resultData = prefixData + resultData;
			}

			boolean postfixEnabled = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportDisplay().isDisplayPostfixCategory();
			ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: RoutineSummary Category prefixEnabled: " + prefixEnabled);
			if(postfixEnabled) {
				String postfixData = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportResultValue().getPostfixCategoryValue();
				ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: RoutineSummary Category postfixData: " + postfixData);

				resultData =  resultData + postfixData;
			}

			status = FillReportDataColumnXSSF(sheet1, resultData,resultCellPosition);

		}




		status = populateRoutineTestSummaryDescription(sheet1,dutOverAllStatus);
		if(!status) {
			overAllStatus =false;
		}
		status = populateRoutineTestOverAllResult(sheet1,dutOverAllStatus);
		if(!status) {
			overAllStatus =false;
		}

		status = populateRoutineTestSummaryReportSerialNo(sheet1,dutSerialNo);
		if(!status) {
			overAllStatus =false;
		}





		/*		resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getReportResultDataCellPosition().getExecutedDateCell();

		status = FillReportDataColumnXSSF(sheet1, testExecutedDate,resultCellPosition);
		if(!status) {
			overAllStatus =false;
		}*/

		boolean populateExecutedDate = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportDisplay().isDisplayTestExecutedDate();
		if(populateExecutedDate){
			resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportCellPosition().getExecutedDateCell();
			boolean prefixEnabled = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportDisplay().isDisplayPrefixTestExecutedDate();
			//ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: RoutineSummary PropertyOf prefixEnabled: " + prefixEnabled);
			resultData = testExecutedDate;
			if(prefixEnabled) {
				String prefixData = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportResultValue().getPrefixTestExecutedDateValue();
				//ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: RoutineSummary PropertyOf prefixData: " + prefixData);

				resultData = prefixData + testExecutedDate;
			}
			status = FillReportDataColumnXSSF(sheet1, resultData,resultCellPosition);
			if(!status) {
				overAllStatus =false;
			}
		}

		return status;
	}
	
	public boolean FillReportDataColumnXSSF(XSSFSheet sheet1, String resultData, String cellPosition){
		boolean status = false;
		try {

			int inpRowPosition = getRowValueFromCellValue(cellPosition);
			int column_pos = getColValueFromCellValue(cellPosition);
			ApplicationLauncher.logger.debug("FillReportDataColumnXSSF: Entry ");
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			//JSONObject result_json = new JSONObject();
			//String rack_id = "";
			//String device_rack_id = "";
			//List<String> uniqueDeviceName= new ArrayList<String>();
			int row_pos = inpRowPosition;
			//for(int i=0; i<filteredResultData.size(); i++){
			try{
				Row row = sheet1.getRow(row_pos);

				if(row == null){
					row = sheet1.createRow(row_pos);

				}


				Cell column = row.getCell(column_pos);
				if(column == null){
					column = sheet1.getRow(row_pos).createCell(column_pos);
				}

				//ApplicationLauncher.logger.info("FillMeterColumnXSSF_V2: getDutSerialNo: " + result.get(i).getDutSerialNo());
				column.setCellValue(resultData); 
				row_pos++;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("FillReportDataColumnXSSF: Exception2:"+e.getMessage());
			}
			//}
			status = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FillReportDataColumnXSSF: Exception2:"+e.getMessage());
		}

		return status;
	}
	
	
	public  boolean populateRoutineTestSummaryDescription(XSSFSheet sheet1,String dutOverAllStatus) {
		// TODO Auto-generated method stub
		boolean status = true;
		String resultCellPosition = "";
		String resultData = "";


		boolean populateRotineTestSummaryEnabled = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportDisplay().isDisplayRoutineTestInReport();
		//InsulationResistanceMeterResult.setResultStatus("Pass");
		if(populateRotineTestSummaryEnabled){
			ApplicationLauncher.logger.debug("populateRoutineTestSummaryDescription: populateRotineTestSummaryEnabled enabled");	

			boolean populateDescription = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportDisplay().isDisplayDescription();
			ApplicationLauncher.logger.debug("populateRoutineTestSummaryDescription: RoutineSummary populateDescription enabled");	
			if(populateDescription) {
				resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportCellPosition().getDescriptionCell();
				ApplicationLauncher.logger.debug("populateRoutineTestSummaryDescription: RoutineSummary populateDescription CellPosition: " + resultCellPosition);
				ApplicationLauncher.logger.debug("populateRoutineTestSummaryDescription: RoutineSummary populateDescription dutOverAllStatus: " + dutOverAllStatus);

				if (dutOverAllStatus.toUpperCase().equals(ConstantReport.REPORT_POPULATE_PASS.toUpperCase())) {
					resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportResultValue().getDescriptionPassValue();
				}else {
					resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportResultValue().getDescriptionFailValue();

				}
				ApplicationLauncher.logger.debug("populateRoutineTestSummaryDescription: RoutineSummary populateDescription resultData: " + resultData);

				status = FillReportDataColumnXSSF(sheet1, resultData,resultCellPosition);

			}

		}


		return status;
	}
	
	
	public Boolean appendDataAndMergeCells(XSSFSheet sheet1){
		ApplicationLauncher.logger.debug("appendDataAndMergeCells: Entry ");	
		String resultCellPosition = "";
		boolean status = true;
		boolean appendCellEnabled = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportDisplay().isAppendCellData();
		int appendCellStartingRow = -1;
		int appendCellStartingColumn = -1;
		StringBuilder mergedContent = new StringBuilder();
		if(appendCellEnabled){
			ApplicationLauncher.logger.debug("appendDataAndMergeCells: appendCellEnabled");	

			resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportCellPosition().getAppendCellPositionList();

			List<String> appendCellList = Arrays.asList(resultCellPosition.split(","));


			int rowPosition = 0;
			int columnPosition = 0;
			for (int i =0; i < appendCellList.size(); i++) {


				rowPosition = getRowValueFromCellValue(appendCellList.get(i));
				columnPosition = getColValueFromCellValue(appendCellList.get(i));
				Row row = sheet1.getRow(rowPosition);
				Cell cell = row.getCell(columnPosition);
				if (cell != null ) {
					mergedContent.append(cell.getStringCellValue());
				}

				if(i == 0) {
					appendCellStartingRow = rowPosition;
					appendCellStartingColumn = columnPosition;
				}
			}
			ApplicationLauncher.logger.debug("appendDataAndMergeCells: mergedContent : <" + mergedContent + ">" );	


			//status = FillReportDataColumnXSSF(sheet1, resultData,resultCellPosition);

		}

		boolean mergeCellsEnabled = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportDisplay().isMergeCells();
		if(mergeCellsEnabled){
			String mergeStartingCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportCellPosition().getMergeStartingCell();
			String mergeEndingCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportCellPosition().getMergeEndingCell();

			int startRowPosition = getRowValueFromCellValue(mergeStartingCellPosition);
			int startColumnPosition = getColValueFromCellValue(mergeStartingCellPosition);
			int endColumnPosition = getColValueFromCellValue(mergeEndingCellPosition);

			sheet1.addMergedRegion(new CellRangeAddress(startRowPosition, startRowPosition, startColumnPosition, endColumnPosition));
			ApplicationLauncher.logger.debug("appendDataAndMergeCells: mergedcell  : Success");
		}

		boolean overWriteMergedCellEnabled = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportDisplay().isOverwriteMergedCellWithAppendedData();
		if(overWriteMergedCellEnabled){

			if(appendCellStartingRow != -1) {
				if(appendCellStartingColumn != -1) {
					Row row = sheet1.getRow(appendCellStartingRow);
					Cell cell = row.getCell(appendCellStartingColumn);
					cell.setCellValue(mergedContent.toString());
					ApplicationLauncher.logger.debug("appendDataAndMergeCells: overWrite Cell : Success" );
				}

			}

		}

		return status;
	}


	public static int getRowValueFromCellValue(String cellvalue){
		String str_row = cellvalue.replaceAll("[^0-9]", "");
		int row = Integer.parseInt(str_row)-1;
		return row;
	}



	public static int getColValueFromCellValue(String cellvalue){
		String col = cellvalue.replaceAll("[0-9]", "");

		int col_value = 0;
		char ch = ' ';
		int ascii_value = 0;
		for(int i=0; i<col.length(); i++){
			ch = col.charAt(i);
			ascii_value = (int)ch;
			col_value = (col_value*26) + ascii_value - 64;
		}

		col_value = col_value - 1;
		return col_value;
	}

	
	public Boolean populateMeterIndividualReportAccuracyData(XSSFSheet sheet1, List<MeterResultDetailed> meterResultDetailedList){

		boolean status = true;
		ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportData: Entry ");
		String resultCellPosition = "";
		boolean overAllStatus = true;

		List<MeterResultDetailed>  accuracyMeterResultDetailedList = meterResultDetailedList.stream()
				.filter(m -> m.getTestType().startsWith(ConstantConveyor.ACCURACY_ALIAS_NAME))
				.collect(Collectors.toList());

		int row_pos =0;
		int column_pos = 0;
		String resultData = "";





		boolean populateData = DeviceDataManagerController.getConveyorConfigParsedKey().getResultDataDisplay().isDisplayAppliedCurrent();
		if(populateData){

			resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getReportResultDataCellPosition().getAppliedCurrentBeginCell();
			row_pos = getRowValueFromCellValue(resultCellPosition);
			column_pos = getColValueFromCellValue(resultCellPosition);
			String testName = "";
			for(int i=0; i< accuracyMeterResultDetailedList.size() ; i++) {//meterResultDetailedList.size()

				//resultData = String.valueOf(accuracyMeterResultDetailedList.get(i).getTestName());

				testName = accuracyMeterResultDetailedList.get(i).getTestName();

				// Regular expression to match values like "0.01Imax", "1.0Ib", "0.5Imax"
				Pattern pattern = Pattern.compile("(\\d+\\.\\d+)(Imax|Ib)");
				Matcher matcher = pattern.matcher(testName);

				if (matcher.find()) {
					String numericValue = matcher.group(1); // Extract the numeric part
					String currentType = matcher.group(2);  // Extract "Imax" or "Ib"

					if (currentType.equals("Imax")) {
						resultData = numericValue + " Imax";
					} else if (currentType.equals("Ib")) {
						resultData = numericValue + " Ib";
					}
				} else {
					resultData = testName; // Keep the original name if no match
				}
				status = FillReportDataColumnWithOutStyleXSSF(sheet1, resultData, row_pos, column_pos);
				if(!status) {
					overAllStatus =false;
				}
				row_pos++;
			}
		}


		populateData = DeviceDataManagerController.getConveyorConfigParsedKey().getResultDataDisplay().isDisplayPowerFactor();
		if(populateData){

			resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getReportResultDataCellPosition().getPowerFactorBeginCell();
			row_pos = getRowValueFromCellValue(resultCellPosition);
			column_pos = getColValueFromCellValue(resultCellPosition);
			String testName = "";
			for(int i=0; i< accuracyMeterResultDetailedList.size() ; i++) {//meterResultDetailedList.size()

				testName = accuracyMeterResultDetailedList.get(i).getTestName();
				// Regular expression to match power factor (e.g., "1.0", "0.5L", "0.8C")
				Pattern pattern = Pattern.compile("(\\d+\\.\\d+)(L|C|)?-\\d+\\.\\d+(Imax|Ib)");
				Matcher matcher = pattern.matcher(testName);

				if (matcher.find()) {
					String powerFactorValue = matcher.group(1); // Extract power factor (e.g., "1.0", "0.5", "0.8")
					String powerFactorType = matcher.group(2); // "L" for lagging, "C" for leading, null if unity

					// Determine power factor display
					if ("1.0".equals(powerFactorValue)) {
						resultData = ConstantApp.PF_UPF; // Unity Power Factor
					} else if (ConstantApp.PF_LAG.equals(powerFactorType)) {
						resultData = powerFactorValue + " Lag"; // Lagging
					} else if (ConstantApp.PF_LEAD.equals(powerFactorType)) {
						resultData = powerFactorValue + " Lead"; // Leading
					} else {
						resultData = powerFactorValue; // Default case (shouldn't occur)
					}
				} else {
					resultData = testName; // Keep original if no match
				}
				status = FillReportDataColumnWithOutStyleXSSF(sheet1, resultData, row_pos, column_pos);
				if(!status) {
					overAllStatus =false;
				}
				row_pos++;
			}
		}

		populateData = DeviceDataManagerController.getConveyorConfigParsedKey().getResultDataDisplay().isDisplayErrorValue();
		if(populateData){
			resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getReportResultDataCellPosition().getErrorValueBeginCell();
			row_pos = getRowValueFromCellValue(resultCellPosition);
			column_pos = getColValueFromCellValue(resultCellPosition);
			for(int i=0; i< accuracyMeterResultDetailedList.size() ; i++) {//meterResultDetailedList.size()

				resultData = accuracyMeterResultDetailedList.get(i).getResultValue().replaceFirst(ConstantReport.REPORT_POPULATE_PASS, "").replaceFirst(ConstantReport.REPORT_POPULATE_FAIL, "");
				status = FillReportDataColumnWithOutStyleXSSF(sheet1, resultData, row_pos, column_pos);
				if(!status) {
					overAllStatus =false;
				}
				row_pos++;
			}
		}


		populateData = DeviceDataManagerController.getConveyorConfigParsedKey().getResultDataDisplay().isDisplayErrorStatus();
		if(populateData){

			resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getReportResultDataCellPosition().getErrorStatusBeginCell();
			row_pos = getRowValueFromCellValue(resultCellPosition);
			column_pos = getColValueFromCellValue(resultCellPosition);
			for(int i=0; i< accuracyMeterResultDetailedList.size() ; i++) {//meterResultDetailedList.size()

				resultData = String.valueOf(accuracyMeterResultDetailedList.get(i).getResultStatus());
				status = FillReportDataColumnWithOutStyleXSSF(sheet1, resultData.toUpperCase(), row_pos, column_pos);
				if(!status) {
					overAllStatus =false;
				}
				row_pos++;
			}

		}

		populateData = DeviceDataManagerController.getConveyorConfigParsedKey().getResultDataDisplay().isDisplayPermissibleLimit();
		if(populateData){

			resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getReportResultDataCellPosition().getPermissibleLimitBeginCell();
			row_pos = getRowValueFromCellValue(resultCellPosition);
			column_pos = getColValueFromCellValue(resultCellPosition);
			for(int i=0; i< accuracyMeterResultDetailedList.size() ; i++) {//meterResultDetailedList.size()

				resultData = String.valueOf(accuracyMeterResultDetailedList.get(i).getPermissibleLimitDisplay());
				status = FillReportDataColumnWithOutStyleXSSF(sheet1, resultData, row_pos, column_pos);
				if(!status) {
					overAllStatus =false;
				}
				row_pos++;
			}

		}
		try {
			JSONObject modeldata = null; 
			int modelId = 0;
			try {
				//JSONObject modeldata  = new JSONObject();
				String modelConfigName = DeviceDataManagerController.getConveyorConfigParsedKey().getReportResultDataCellPosition().getActiveMeterProfileModelMeterConfig();
				modelId = MySQL_Controller.sp_getModel_ID(modelConfigName);
				ApplicationLauncher.logger.debug("populateMeterIndividualReportAccuracyData: modelId: " + modelId);
				if(modelId!=0) {

					JSONObject modeldata1 = MySQL_Controller.sp_getem_model_data(modelId);
					modeldata = modeldata1;
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				ApplicationLauncher.logger.error("populateMeterIndividualReportAccuracyData: modelConfigName: Exception: " + e.getMessage());
				ApplicationLauncher.logger.debug("Error-EM00011:  Invalid Meter config in the Meter Profile.\\n\\nKindly reconfigure <ActiveMeterProfileModelMeterConfig> in conveyor_config.json, restart the application and try again - prompted");
				ApplicationLauncher.InformUser("Error-EM00011", "Invalid Meter config in the Meter Profile.\n\nKindly reconfigure <ActiveMeterProfileModelMeterConfig> in conveyor_config.json, restart the application and try again", AlertType.ERROR);

				return false;
			}

			if(modelId==0) {
				ApplicationLauncher.logger.debug("Error-EM00021:  Invalid Meter config in the Meter Profile.\\n\\nKindly reconfigure <ActiveMeterProfileModelMeterConfig> in conveyor_config.json, restart the application and try again - prompted");
				ApplicationLauncher.InformUser("Error-EM00021", "Invalid Meter config in the Meter Profile.\n\nKindly reconfigure <ActiveMeterProfileModelMeterConfig> in conveyor_config.json, restart the application and try again", AlertType.ERROR);

				return false;
			}
			populateData = DeviceDataManagerController.getConveyorConfigParsedKey().getResultDataDisplay().isDisplayAppliedVoltage();
			if(populateData){

				resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getReportResultDataCellPosition().getAppliedVoltageBeginCell();
				row_pos = getRowValueFromCellValue(resultCellPosition);
				column_pos = getColValueFromCellValue(resultCellPosition);
				for(int i=0; i< accuracyMeterResultDetailedList.size() ; i++) {//meterResultDetailedList.size()

					String ratedVoltageData = modeldata.getString("rated_voltage_vd");//"240";//String.valueOf(accuracyMeterResultDetailedList.get(i).getPermissibleLimitDisplay());
					ApplicationLauncher.logger.debug("populateMeterIndividualReportAccuracyData: ratedVoltageData : " + ratedVoltageData);
					populateData = DeviceDataManagerController.getConveyorConfigParsedKey().getResultDataPreAndPostFixDisplay().isDisplayPreFixAppliedVoltage();
					ApplicationLauncher.logger.debug("populateMeterIndividualReportAccuracyData: populateData : " + populateData);
					if(populateData) {
						String prefixData = DeviceDataManagerController.getConveyorConfigParsedKey().getResultDataPreAndPostFixValue().getAppliedPreFixVoltageValue();
						resultData = prefixData + ratedVoltageData;
						ApplicationLauncher.logger.debug("populateMeterIndividualReportAccuracyData: Prefixed resultData Value : " +resultData);
					}

					populateData = DeviceDataManagerController.getConveyorConfigParsedKey().getResultDataPreAndPostFixDisplay().isDisplayPostFixAppliedVoltage();
					if(populateData) {
						String postfixData = DeviceDataManagerController.getConveyorConfigParsedKey().getResultDataPreAndPostFixValue().getAppliedPostFixVoltageValue();
						resultData = resultData + postfixData;
						ApplicationLauncher.logger.debug("populateMeterIndividualReportAccuracyData: Postfixed resultData Value : " +resultData);
					}

					status = FillReportDataColumnWithOutStyleXSSF(sheet1, resultData, row_pos, column_pos);


					if(!status) {
						overAllStatus =false;
					}
					boolean populateDataOnlyOnFirstCell =  DeviceDataManagerController.getConveyorConfigParsedKey().getResultDataDisplay().isDisplayAppliedVoltageInFirstCellOnly();

					if(populateDataOnlyOnFirstCell) {
						ApplicationLauncher.logger.debug("populateMeterIndividualReportAccuracyData: AppliedVoltage: populateDataOnlyOnFirstCell Enabled: breaking the loop");
						break;
					}
					row_pos++;
				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("populateMeterIndividualReportAccuracyData: modelConfigName: Exception2: " + e.getMessage());
			//ApplicationLauncher.logger.debug("Error-EM0002:  Invalid Meter config in the Meter Profile.\\n\\nKindly reconfigure <ActiveMeterProfileModelMeterConfig> in conveyor_config.json, restart the application and try again - prompted");
			//ApplicationLauncher.InformUser("Error-EM0003", "Invalid Meter config in the Meter Profile.\n\nKindly reconfigure <ActiveMeterProfileModelMeterConfig> in conveyor_config.json, restart the application and try again", AlertType.ERROR);

			//return false;
		} 

		return status;
	}

	public  boolean populateRoutineTestSummaryReportSerialNo(XSSFSheet sheet1,String dutSerialNo) {
		// TODO Auto-generated method stub
		boolean status = true;
		String resultCellPosition = "";
		String resultData = "Undefined";


		boolean populateRotineTestSummaryEnabled = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportDisplay().isDisplayRoutineTestInReport();
		//InsulationResistanceMeterResult.setResultStatus("Pass");
		if(populateRotineTestSummaryEnabled){
			ApplicationLauncher.logger.debug("populateRoutineTestSummaryReportSerialNo: populateRotineTestSummaryEnabled enabled");	

			boolean populateReportSerialNo = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportDisplay().isDisplayReportSerialNo();
			ApplicationLauncher.logger.debug("populateRoutineTestSummaryReportSerialNo: RoutineSummary populateReportSerialNo enabled");	
			if(populateReportSerialNo) {
				resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportCellPosition().getReportSerialNoCell();
				ApplicationLauncher.logger.debug("populateRoutineTestSummaryReportSerialNo: RoutineSummary populateReportSerialNo CellPosition: " + resultCellPosition);
				//ApplicationLauncher.logger.debug("populateRoutineTestSummaryReportSerialNo: RoutineSummary populateReportSerialNo dutOverAllStatus: " + dutOverAllStatus);
				boolean useMeterSerialNoAsReportSerialNo = DeviceDataManagerController.getConveyorConfigParsedKey().isUseDutSerialNoAsReportSerialNo();
				if(useMeterSerialNoAsReportSerialNo) {
					ApplicationLauncher.logger.debug("populateRoutineTestSummaryReportSerialNo: RoutineSummary useMeterSerialNoAsReportSerialNo enabled");
					String prefixReportSerialNo = DeviceDataManagerController.getConveyorConfigParsedKey().getReportSerialNoPrefix();
					//resultData = prefixReportSerialNo+dutSerialNo;//DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportResultValue().getReportSerialNoPassValue();
					String serialNoRegexSourceFormat = DeviceDataManagerController.getConveyorConfigParsedKey().getReportSerialNoRegexSourceFormat();
					String serialNoRegexTargetFormat = DeviceDataManagerController.getConveyorConfigParsedKey().getReportSerialNoRegexTargetFormat();
					ApplicationLauncher.logger.debug("populateRoutineTestSummaryReportSerialNo: RoutineSummary serialNoRegexSourceFormat: " + serialNoRegexSourceFormat);
					ApplicationLauncher.logger.debug("populateRoutineTestSummaryReportSerialNo: RoutineSummary serialNoRegexTargetFormat:"  + serialNoRegexTargetFormat);

					if( (!serialNoRegexSourceFormat.equals("")) && (!serialNoRegexTargetFormat.equals("")) ) {
						//dutSerialNo = "01234567";
						resultData = prefixReportSerialNo+dutSerialNo;
						ApplicationLauncher.logger.debug("populateRoutineTestSummaryReportSerialNo: RoutineSummary resultData1:"  + resultData);

						resultData = resultData.replaceAll(serialNoRegexSourceFormat, serialNoRegexTargetFormat);
						ApplicationLauncher.logger.debug("populateRoutineTestSummaryReportSerialNo: RoutineSummary resultData2:"  + resultData);
					}else {
						resultData = prefixReportSerialNo+dutSerialNo;//DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportResultValue().getReportSerialNoPassValue();
						ApplicationLauncher.logger.debug("populateRoutineTestSummaryReportSerialNo: RoutineSummary resultData:"  + resultData);
					}
				}
				String prefixDisplayData = "";
				boolean prefixResultStatus = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportDisplay().isDisplayPrefixReportSerialNo();

				if(prefixResultStatus) {
					prefixDisplayData = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportResultValue().getPrefixReportSerialNoValue();
				}

				String postfixdisplayData = "";
				boolean postfixResultStatus = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportDisplay().isDisplayPostfixReportSerialNo();

				if(postfixResultStatus) {
					postfixdisplayData = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportResultValue().getPostfixReportSerialNoValue();
				}
				resultData = prefixDisplayData + resultData + postfixdisplayData;



				ApplicationLauncher.logger.debug("populateRoutineTestSummaryReportSerialNo: RoutineSummary populateDescription resultData: " + resultData);

				status = FillReportDataColumnXSSF(sheet1, resultData,resultCellPosition);
			}

		}


		return status;
	}

	
	public boolean FillReportDataColumnWithOutStyleXSSF(XSSFSheet sheet1, String resultData, int row_pos,int column_pos){
		boolean status = false;
		try {

			//int inpRowPosition = getRowValueFromCellValue(cellPosition);
			//int column_pos = getColValueFromCellValue(cellPosition);
			//ApplicationLauncher.logger.debug("FillReportDataColumnXSSF_V1_1: Entry ");
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			//JSONObject result_json = new JSONObject();
			//String rack_id = "";
			//String device_rack_id = "";
			//List<String> uniqueDeviceName= new ArrayList<String>();
			//int row_pos = inpRowPosition;
			//for(int i=0; i<filteredResultData.size(); i++){
			try{
				Row row = sheet1.getRow(row_pos);

				if(row == null){
					row = sheet1.createRow(row_pos);

				}


				Cell column = row.getCell(column_pos);
				if(column == null){
					column = sheet1.getRow(row_pos).createCell(column_pos);
				}

				//ApplicationLauncher.logger.info("FillMeterColumnXSSF_V2: getDutSerialNo: " + result.get(i).getDutSerialNo());
				column.setCellValue(resultData); 
				row_pos++;


			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("FillReportDataColumnXSSF_V1_1: Exception2:"+e.getMessage());
			}

			//}
			status = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FillReportDataColumnXSSF_V1_1: Exception2:"+e.getMessage());
		}

		return status;
	}
	
	public Boolean populateMeterIndividualReportNoLoadData(XSSFSheet sheet1, List<MeterResultDetailed> meterResultDetailedList){
		boolean status = true;

		ApplicationLauncher.logger.debug("populateMeterIndividualReportNoLoadData: Entry ");

		Optional<MeterResultDetailed>  noLoadMeterResultOpt = meterResultDetailedList.stream()
				.filter(m -> m.getTestType().startsWith(ConstantConveyor.CREEP_ALIAS_NAME))
				.findFirst();
		//.collect(Collectors.toList());
		String resultData = "";
		String resultCellPosition= "";
		if(noLoadMeterResultOpt.isPresent()) {
			ApplicationLauncher.logger.debug("populateMeterIndividualReportNoLoadData: NoLoad Result Found ");

			MeterResultDetailed noLoadMeterResult = noLoadMeterResultOpt.get();
			ApplicationLauncher.logger.debug("populateMeterIndividualReportNoLoadData: NoLoad : getTestName: " + noLoadMeterResult.getTestName());
			boolean populateData = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportDisplay().isDisplayNoLoadInReport();

			if(populateData){
				ApplicationLauncher.logger.debug("populateMeterIndividualReportNoLoadData: NoLoad Result Populate enabled");	

				boolean populateDescription = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportDisplay().isDisplayDescription();
				ApplicationLauncher.logger.debug("populateMeterIndividualReportNoLoadData: NoLoad populateDescription enabled");	
				if(populateDescription) {
					resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportCellPosition().getDescriptionCell();
					ApplicationLauncher.logger.debug("populateMeterIndividualReportNoLoadData: NoLoad populateDescription CellPosition: " + resultCellPosition);
					ApplicationLauncher.logger.debug("populateMeterIndividualReportNoLoadData: NoLoad populateDescription getResultStatus(): " + noLoadMeterResult.getResultStatus());

					if (noLoadMeterResult.getResultStatus().toUpperCase().equals(ConstantReport.REPORT_POPULATE_PASS.toUpperCase())) {
						resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportResultValue().getDescriptionPassValue();
					}else {
						resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportResultValue().getDescriptionFailValue();

					}
					ApplicationLauncher.logger.debug("populateMeterIndividualReportNoLoadData: NoLoad populateDescription resultData: " + resultData);

					status = FillReportDataColumnXSSF(sheet1, resultData,resultCellPosition);

				}


				boolean populateResultStatus = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportDisplay().isDisplayResultStatus();
				ApplicationLauncher.logger.debug("populateMeterIndividualReportNoLoadData: NoLoad populateResultStatus enabled");	
				//noLoadMeterResult.setResultStatus("Fail");
				if(populateResultStatus) {
					resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportCellPosition().getResultStatusCell();
					ApplicationLauncher.logger.debug("populateMeterIndividualReportNoLoadData: NoLoad populateResultStatus CellPosition: " + resultCellPosition);
					ApplicationLauncher.logger.debug("populateMeterIndividualReportNoLoadData: NoLoad populateResultStatus getResultStatus(): " + noLoadMeterResult.getResultStatus());

					boolean overRideResultStatus = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportDisplay().isDisplayOverRideResultStatus();
					if(overRideResultStatus) {


						if (noLoadMeterResult.getResultStatus().toUpperCase().equals(ConstantReport.REPORT_POPULATE_PASS.toUpperCase())) {
							resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportResultValue().getResultStatusOverRidePassValue();
						}else {
							resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportResultValue().getResultStatusOverRideFailValue();

						}
					}else {
						resultData = noLoadMeterResult.getResultStatus();
					}

					String prefixData = "";
					boolean prefixResultStatus = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportDisplay().isDisplayResultStatusPrefix();

					if(prefixResultStatus) {
						prefixData = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportResultValue().getResultStatusPrefixValue();
					}

					String postfixData = "";
					boolean postfixResultStatus = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportDisplay().isDisplayResultStatusPostfix();

					if(postfixResultStatus) {
						postfixData = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportResultValue().getResultStatusPostfixValue();
					}
					resultData = prefixData + resultData + postfixData;

					ApplicationLauncher.logger.debug("populateMeterIndividualReportNoLoadData: NoLoad populateResultStatus resultData: " + resultData);

					status = FillReportDataColumnXSSF(sheet1, resultData,resultCellPosition);

				}



				boolean populateResultValue = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportDisplay().isDisplayResultValue();
				ApplicationLauncher.logger.debug("populateMeterIndividualReportNoLoadData: NoLoad populateResultValue enabled");	
				//noLoadMeterResult.setResultStatus("Fail");
				if(populateResultValue) {
					resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportCellPosition().getResultValueCell();
					ApplicationLauncher.logger.debug("populateMeterIndividualReportNoLoadData: NoLoad populateResultValue CellPosition: " + resultCellPosition);
					ApplicationLauncher.logger.debug("populateMeterIndividualReportNoLoadData: NoLoad populateResultValue getResultValue(): " + noLoadMeterResult.getResultValue());

					boolean overRideResultValue = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportDisplay().isDisplayOverRideResultValue();
					if(overRideResultValue) {


						if (noLoadMeterResult.getResultStatus().toUpperCase().equals(ConstantReport.REPORT_POPULATE_PASS.toUpperCase())) {
							resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportResultValue().getResultValueOverRidePassValue();
						}else {
							resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportResultValue().getResultValueOverRideFailValue();

						}
					}else {
						resultData = noLoadMeterResult.getResultValue().replace(ConstantReport.REPORT_POPULATE_PASS, "").replace(ConstantReport.REPORT_POPULATE_FAIL, "").trim();
					}

					String prefixData = "";
					boolean prefixResultValue = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportDisplay().isDisplayResultValuePrefix();

					if(prefixResultValue) {
						prefixData = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportResultValue().getResultValuePrefixValue();
					}

					String postfixData = "";
					boolean postfixResultValue = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportDisplay().isDisplayResultValuePostfix();

					if(postfixResultValue) {
						postfixData = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportResultValue().getResultValuePostfixValue();
					}
					resultData = prefixData + resultData + postfixData;

					ApplicationLauncher.logger.debug("populateMeterIndividualReportNoLoadData: NoLoad populateResultValue resultData: " + resultData);

					status = FillReportDataColumnXSSF(sheet1, resultData,resultCellPosition);

				}

				//status = FillReportDataColumnXSSF(sheet1, dutSerialNo,resultCellPosition);
				if(!status) {
					//overAllStatus =false;
				}
			}
		}

		return status;

	}
	
	public Boolean populateMeterIndividualReportSummaryNoLoadData(XSSFSheet sheet1, 
			MeterResultSummary meterResultSummary){
		boolean status = true;

		ApplicationLauncher.logger.debug("populateMeterIndividualReportSummaryNoLoadData: Entry ");

/*		Optional<MeterResultDetailed>  noLoadMeterResultOpt = meterResultDetailedList.stream()
				.filter(m -> m.getTestType().startsWith(ConstantConveyor.CREEP_ALIAS_NAME))
				.findFirst();*/
		//.collect(Collectors.toList());
		String resultStatus= "";
		String resultValue = "";
		try {
			 String[] parts = meterResultSummary.getTestTypeNoLoad().split(" "); // Split by space
			
			resultStatus = parts[0]; // First part ("Pass")
	        resultValue = parts[1];
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			ApplicationLauncher.logger.error("populateMeterIndividualReportSummaryNoLoadData: Exception:"+e1.getMessage());
			
		}
		String resultPopulateData = "";
		String resultCellPosition= "";
		//if(noLoadMeterResultOpt.isPresent()) {
			//ApplicationLauncher.logger.debug("populateMeterIndividualReportSummaryNoLoadData: NoLoad Result Found ");

			//MeterResultDetailed noLoadMeterResult = noLoadMeterResultOpt.get();
			//ApplicationLauncher.logger.debug("populateMeterIndividualReportSummaryNoLoadData: NoLoad : getTestName: " + noLoadMeterResult.getTestName());
			boolean populateData = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportDisplay().isDisplayNoLoadInReport();

			if(populateData){
				ApplicationLauncher.logger.debug("populateMeterIndividualReportSummaryNoLoadData: NoLoad Result Populate enabled");	

				boolean populateDescription = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportDisplay().isDisplayDescription();
				ApplicationLauncher.logger.debug("populateMeterIndividualReportSummaryNoLoadData: NoLoad populateDescription enabled");	
				if(populateDescription) {
					resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportCellPosition().getDescriptionCell();
					ApplicationLauncher.logger.debug("populateMeterIndividualReportSummaryNoLoadData: NoLoad populateDescription CellPosition: " + resultCellPosition);
					ApplicationLauncher.logger.debug("populateMeterIndividualReportSummaryNoLoadData: NoLoad populateDescription resultStatus: " + resultStatus);

					if (resultStatus.toUpperCase().equals(ConstantReport.REPORT_POPULATE_PASS.toUpperCase())) {
						resultPopulateData = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportResultValue().getDescriptionPassValue();
					}else {
						resultPopulateData = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportResultValue().getDescriptionFailValue();

					}
					ApplicationLauncher.logger.debug("populateMeterIndividualReportSummaryNoLoadData: NoLoad populateDescription resultData: " + resultPopulateData);

					status = FillReportDataColumnXSSF(sheet1, resultPopulateData,resultCellPosition);

				}


				boolean populateResultStatus = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportDisplay().isDisplayResultStatus();
				ApplicationLauncher.logger.debug("populateMeterIndividualReportSummaryNoLoadData: NoLoad populateResultStatus enabled");	
				//noLoadMeterResult.setResultStatus("Fail");
				if(populateResultStatus) {
					resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportCellPosition().getResultStatusCell();
					ApplicationLauncher.logger.debug("populateMeterIndividualReportSummaryNoLoadData: NoLoad populateResultStatus CellPosition: " + resultCellPosition);
					ApplicationLauncher.logger.debug("populateMeterIndividualReportSummaryNoLoadData: NoLoad populateResultStatus resultStatus: " + resultStatus);

					boolean overRideResultStatus = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportDisplay().isDisplayOverRideResultStatus();
					if(overRideResultStatus) {


						if (resultStatus.toUpperCase().equals(ConstantReport.REPORT_POPULATE_PASS.toUpperCase())) {
							resultPopulateData = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportResultValue().getResultStatusOverRidePassValue();
						}else {
							resultPopulateData = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportResultValue().getResultStatusOverRideFailValue();

						}
					}else {
						resultPopulateData = resultStatus;
					}

					String prefixData = "";
					boolean prefixResultStatus = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportDisplay().isDisplayResultStatusPrefix();

					if(prefixResultStatus) {
						prefixData = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportResultValue().getResultStatusPrefixValue();
					}

					String postfixData = "";
					boolean postfixResultStatus = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportDisplay().isDisplayResultStatusPostfix();

					if(postfixResultStatus) {
						postfixData = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportResultValue().getResultStatusPostfixValue();
					}
					resultPopulateData = prefixData + resultPopulateData + postfixData;

					ApplicationLauncher.logger.debug("populateMeterIndividualReportSummaryNoLoadData: NoLoad populateResultStatus resultData: " + resultPopulateData);

					status = FillReportDataColumnXSSF(sheet1, resultPopulateData,resultCellPosition);

				}



				boolean populateResultValue = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportDisplay().isDisplayResultValue();
				ApplicationLauncher.logger.debug("populateMeterIndividualReportSummaryNoLoadData: NoLoad populateResultValue enabled");	
				//noLoadMeterResult.setResultStatus("Fail");
				if(populateResultValue) {
					resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportCellPosition().getResultValueCell();
					ApplicationLauncher.logger.debug("populateMeterIndividualReportSummaryNoLoadData: NoLoad populateResultValue CellPosition: " + resultCellPosition);
					ApplicationLauncher.logger.debug("populateMeterIndividualReportSummaryNoLoadData: NoLoad populateResultValue resultValue: " + resultValue);

					boolean overRideResultValue = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportDisplay().isDisplayOverRideResultValue();
					if(overRideResultValue) {


						if (resultValue.toUpperCase().equals(ConstantReport.REPORT_POPULATE_PASS.toUpperCase())) {
							resultPopulateData = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportResultValue().getResultValueOverRidePassValue();
						}else {
							resultPopulateData = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportResultValue().getResultValueOverRideFailValue();

						}
					}else {
						resultPopulateData = resultValue.replace(ConstantReport.REPORT_POPULATE_PASS, "").replace(ConstantReport.REPORT_POPULATE_FAIL, "").trim();
					}

					String prefixData = "";
					boolean prefixResultValue = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportDisplay().isDisplayResultValuePrefix();

					if(prefixResultValue) {
						prefixData = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportResultValue().getResultValuePrefixValue();
					}

					String postfixData = "";
					boolean postfixResultValue = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportDisplay().isDisplayResultValuePostfix();

					if(postfixResultValue) {
						postfixData = DeviceDataManagerController.getConveyorConfigParsedKey().getNoLoadReportResultValue().getResultValuePostfixValue();
					}
					resultPopulateData = prefixData + resultPopulateData + postfixData;

					ApplicationLauncher.logger.debug("populateMeterIndividualReportSummaryNoLoadData: NoLoad populateResultValue resultData: " + resultPopulateData);

					status = FillReportDataColumnXSSF(sheet1, resultPopulateData,resultCellPosition);

				}

				//status = FillReportDataColumnXSSF(sheet1, dutSerialNo,resultCellPosition);
				if(!status) {
					//overAllStatus =false;
				}
			}
		//}

		return status;

	}
	
	public Boolean populateMeterIndividualReportStartingCurrentData(XSSFSheet sheet1, List<MeterResultDetailed> meterResultDetailedList){
		boolean status = true;

		ApplicationLauncher.logger.debug("populateMeterIndividualReportStartingCurrentData: Entry ");

		Optional<MeterResultDetailed>  startingCurrentMeterResultOpt = meterResultDetailedList.stream()
				.filter(m -> m.getTestType().startsWith(ConstantConveyor.STA_ALIAS_NAME))
				.findFirst();
		//.collect(Collectors.toList());
		String resultData = "";
		String resultCellPosition= "";
		if(startingCurrentMeterResultOpt.isPresent()) {
			ApplicationLauncher.logger.debug("populateMeterIndividualReportStartingCurrentData: StartingCurrent Result Found ");

			MeterResultDetailed startingCurrentMeterResult = startingCurrentMeterResultOpt.get();
			ApplicationLauncher.logger.debug("populateMeterIndividualReportStartingCurrentData: StartingCurrent : getTestName: " + startingCurrentMeterResult.getTestName());
			boolean populateData = DeviceDataManagerController.getConveyorConfigParsedKey().getStartingCurrentReportDisplay().isDisplayStartCurrentInReport();
			//startingCurrentMeterResult.setResultStatus("Pass");
			if(populateData){
				ApplicationLauncher.logger.debug("populateMeterIndividualReportStartingCurrentData: StartingCurrent Result Populate enabled");	

				boolean populateDescription = DeviceDataManagerController.getConveyorConfigParsedKey().getStartingCurrentReportDisplay().isDisplayDescription();
				ApplicationLauncher.logger.debug("populateMeterIndividualReportStartingCurrentData: StartingCurrent populateDescription enabled");	
				if(populateDescription) {
					resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getStartingCurrentReportCellPosition().getDescriptionCell();
					ApplicationLauncher.logger.debug("populateMeterIndividualReportStartingCurrentData: StartingCurrent populateDescription CellPosition: " + resultCellPosition);
					ApplicationLauncher.logger.debug("populateMeterIndividualReportStartingCurrentData: StartingCurrent populateDescription getResultStatus(): " + startingCurrentMeterResult.getResultStatus());

					if (startingCurrentMeterResult.getResultStatus().toUpperCase().equals(ConstantReport.REPORT_POPULATE_PASS.toUpperCase())) {
						resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getStartingCurrentReportResultValue().getDescriptionPassValue();
					}else {
						resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getStartingCurrentReportResultValue().getDescriptionFailValue();

					}
					ApplicationLauncher.logger.debug("populateMeterIndividualReportStartingCurrentData: StartingCurrent populateDescription resultData: " + resultData);

					status = FillReportDataColumnXSSF(sheet1, resultData,resultCellPosition);

				}


				boolean populateResultStatus = DeviceDataManagerController.getConveyorConfigParsedKey().getStartingCurrentReportDisplay().isDisplayResultStatus();
				ApplicationLauncher.logger.debug("populateMeterIndividualReportStartingCurrentData: StartingCurrent populateResultStatus enabled");	

				if(populateResultStatus) {
					resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getStartingCurrentReportCellPosition().getResultStatusCell();
					ApplicationLauncher.logger.debug("populateMeterIndividualReportStartingCurrentData: StartingCurrent populateResultStatus CellPosition: " + resultCellPosition);
					ApplicationLauncher.logger.debug("populateMeterIndividualReportStartingCurrentData: StartingCurrent populateResultStatus getResultStatus(): " + startingCurrentMeterResult.getResultStatus());

					boolean overRideResultStatus = DeviceDataManagerController.getConveyorConfigParsedKey().getStartingCurrentReportDisplay().isDisplayOverRideResultStatus();
					if(overRideResultStatus) {


						if (startingCurrentMeterResult.getResultStatus().toUpperCase().equals(ConstantReport.REPORT_POPULATE_PASS.toUpperCase())) {
							resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getStartingCurrentReportResultValue().getResultStatusOverRidePassValue();
						}else {
							resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getStartingCurrentReportResultValue().getResultStatusOverRideFailValue();

						}
					}else {
						resultData = startingCurrentMeterResult.getResultStatus();
					}

					String prefixData = "";
					boolean prefixResultStatus = DeviceDataManagerController.getConveyorConfigParsedKey().getStartingCurrentReportDisplay().isDisplayResultStatusPrefix();

					if(prefixResultStatus) {
						prefixData = DeviceDataManagerController.getConveyorConfigParsedKey().getStartingCurrentReportResultValue().getResultStatusPrefixValue();
					}

					String postfixData = "";
					boolean postfixResultStatus = DeviceDataManagerController.getConveyorConfigParsedKey().getStartingCurrentReportDisplay().isDisplayResultStatusPostfix();

					if(postfixResultStatus) {
						postfixData = DeviceDataManagerController.getConveyorConfigParsedKey().getStartingCurrentReportResultValue().getResultStatusPostfixValue();
					}
					resultData = prefixData + resultData + postfixData;

					ApplicationLauncher.logger.debug("populateMeterIndividualReportStartingCurrentData: StartingCurrent populateResultStatus resultData: " + resultData);

					status = FillReportDataColumnXSSF(sheet1, resultData,resultCellPosition);

				}



				boolean populateResultValue = DeviceDataManagerController.getConveyorConfigParsedKey().getStartingCurrentReportDisplay().isDisplayResultValue();
				ApplicationLauncher.logger.debug("populateMeterIndividualReportStartingCurrentData: StartingCurrent populateResultValue enabled");	
				//StartingCurrentMeterResult.setResultStatus("Fail");
				if(populateResultValue) {
					resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getStartingCurrentReportCellPosition().getResultValueCell();
					ApplicationLauncher.logger.debug("populateMeterIndividualReportStartingCurrentData: StartingCurrent populateResultValue CellPosition: " + resultCellPosition);
					ApplicationLauncher.logger.debug("populateMeterIndividualReportStartingCurrentData: StartingCurrent populateResultValue getResultValue(): " + startingCurrentMeterResult.getResultValue());

					boolean overRideResultValue = DeviceDataManagerController.getConveyorConfigParsedKey().getStartingCurrentReportDisplay().isDisplayOverRideResultValue();
					if(overRideResultValue) {


						if (startingCurrentMeterResult.getResultStatus().toUpperCase().equals(ConstantReport.REPORT_POPULATE_PASS.toUpperCase())) {
							resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getStartingCurrentReportResultValue().getResultValueOverRidePassValue();
						}else {
							resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getStartingCurrentReportResultValue().getResultValueOverRideFailValue();

						}
					}else {
						resultData = startingCurrentMeterResult.getResultValue().replace(ConstantReport.REPORT_POPULATE_PASS, "").replace(ConstantReport.REPORT_POPULATE_FAIL, "").trim();
					}

					String prefixData = "";
					boolean prefixResultValue = DeviceDataManagerController.getConveyorConfigParsedKey().getStartingCurrentReportDisplay().isDisplayResultValuePrefix();

					if(prefixResultValue) {
						prefixData = DeviceDataManagerController.getConveyorConfigParsedKey().getStartingCurrentReportResultValue().getResultValuePrefixValue();
					}

					String postfixData = "";
					boolean postfixResultValue = DeviceDataManagerController.getConveyorConfigParsedKey().getStartingCurrentReportDisplay().isDisplayResultValuePostfix();

					if(postfixResultValue) {
						postfixData = DeviceDataManagerController.getConveyorConfigParsedKey().getStartingCurrentReportResultValue().getResultValuePostfixValue();
					}
					resultData = prefixData + resultData + postfixData;

					ApplicationLauncher.logger.debug("populateMeterIndividualReportStartingCurrentData: StartingCurrent populateResultValue resultData: " + resultData);

					status = FillReportDataColumnXSSF(sheet1, resultData,resultCellPosition);

				}

				//status = FillReportDataColumnXSSF(sheet1, dutSerialNo,resultCellPosition);
				if(!status) {
					//overAllStatus =false;
				}
			}
		}



		return status;

	}
	
	public Boolean populateMeterIndividualReportFunctionalTestData(XSSFSheet sheet1, List<MeterResultDetailed> meterResultDetailedList){
		boolean status = true;

		ApplicationLauncher.logger.debug("populateMeterIndividualReportFunctionalTestData: Entry ");

		Optional<MeterResultDetailed>  functionalTestMeterResultOpt = meterResultDetailedList.stream()
				.filter(m -> m.getTestType().startsWith(ConstantConveyor.FT_ALIAS_NAME))//STA_ALIAS_NAME))
				.findFirst();
		//.collect(Collectors.toList());
		String resultData = "";
		String resultCellPosition= "";
		if(functionalTestMeterResultOpt.isPresent()) {
			ApplicationLauncher.logger.debug("populateMeterIndividualReportFunctionalTestData: FunctionalTest Result Found ");

			MeterResultDetailed functionalTestMeterResult = functionalTestMeterResultOpt.get();
			ApplicationLauncher.logger.debug("populateMeterIndividualReportFunctionalTestData: FunctionalTest : getTestName: " + functionalTestMeterResult.getTestName());
			boolean populateData = DeviceDataManagerController.getConveyorConfigParsedKey().getFunctionalTestReportDisplay().isDisplayFunctionTestInReport();
			//FunctionalTestMeterResult.setResultStatus("Pass");
			if(populateData){
				ApplicationLauncher.logger.debug("populateMeterIndividualReportFunctionalTestData: FunctionalTest Result Populate enabled");	

				boolean populateDescription = DeviceDataManagerController.getConveyorConfigParsedKey().getFunctionalTestReportDisplay().isDisplayDescription();
				ApplicationLauncher.logger.debug("populateMeterIndividualReportFunctionalTestData: FunctionalTest populateDescription enabled");	
				if(populateDescription) {
					resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getFunctionalTestReportCellPosition().getDescriptionCell();
					ApplicationLauncher.logger.debug("populateMeterIndividualReportFunctionalTestData: FunctionalTest populateDescription CellPosition: " + resultCellPosition);
					ApplicationLauncher.logger.debug("populateMeterIndividualReportFunctionalTestData: FunctionalTest populateDescription getResultStatus(): " + functionalTestMeterResult.getResultStatus());

					if (functionalTestMeterResult.getResultStatus().toUpperCase().equals(ConstantReport.REPORT_POPULATE_PASS.toUpperCase())) {
						resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getFunctionalTestReportResultValue().getDescriptionPassValue();
					}else {
						resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getFunctionalTestReportResultValue().getDescriptionFailValue();

					}
					ApplicationLauncher.logger.debug("populateMeterIndividualReportFunctionalTestData: FunctionalTest populateDescription resultData: " + resultData);

					status = FillReportDataColumnXSSF(sheet1, resultData,resultCellPosition);

				}


				boolean populateResultStatus = DeviceDataManagerController.getConveyorConfigParsedKey().getFunctionalTestReportDisplay().isDisplayResultStatus();
				ApplicationLauncher.logger.debug("populateMeterIndividualReportFunctionalTestData: FunctionalTest populateResultStatus enabled");	

				if(populateResultStatus) {
					resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getFunctionalTestReportCellPosition().getResultStatusCell();
					ApplicationLauncher.logger.debug("populateMeterIndividualReportFunctionalTestData: FunctionalTest populateResultStatus CellPosition: " + resultCellPosition);
					ApplicationLauncher.logger.debug("populateMeterIndividualReportFunctionalTestData: FunctionalTest populateResultStatus getResultStatus(): " + functionalTestMeterResult.getResultStatus());

					boolean overRideResultStatus = DeviceDataManagerController.getConveyorConfigParsedKey().getFunctionalTestReportDisplay().isDisplayOverRideResultStatus();
					if(overRideResultStatus) {


						if (functionalTestMeterResult.getResultStatus().toUpperCase().equals(ConstantReport.REPORT_POPULATE_PASS.toUpperCase())) {
							resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getFunctionalTestReportResultValue().getResultStatusOverRidePassValue();
						}else {
							resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getFunctionalTestReportResultValue().getResultStatusOverRideFailValue();

						}
					}else {
						resultData = functionalTestMeterResult.getResultStatus();
					}

					String prefixData = "";
					boolean prefixResultStatus = DeviceDataManagerController.getConveyorConfigParsedKey().getFunctionalTestReportDisplay().isDisplayResultStatusPrefix();

					if(prefixResultStatus) {
						prefixData = DeviceDataManagerController.getConveyorConfigParsedKey().getFunctionalTestReportResultValue().getResultStatusPrefixValue();
					}

					String postfixData = "";
					boolean postfixResultStatus = DeviceDataManagerController.getConveyorConfigParsedKey().getFunctionalTestReportDisplay().isDisplayResultStatusPostfix();

					if(postfixResultStatus) {
						postfixData = DeviceDataManagerController.getConveyorConfigParsedKey().getFunctionalTestReportResultValue().getResultStatusPostfixValue();
					}
					resultData = prefixData + resultData + postfixData;

					ApplicationLauncher.logger.debug("populateMeterIndividualReportFunctionalTestData: FunctionalTest populateResultStatus resultData: " + resultData);

					status = FillReportDataColumnXSSF(sheet1, resultData,resultCellPosition);

				}



				boolean populateResultValue = DeviceDataManagerController.getConveyorConfigParsedKey().getFunctionalTestReportDisplay().isDisplayResultValue();
				ApplicationLauncher.logger.debug("populateMeterIndividualReportFunctionalTestData: FunctionalTest populateResultValue enabled");	
				//FunctionalTestMeterResult.setResultStatus("Fail");
				if(populateResultValue) {
					resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getFunctionalTestReportCellPosition().getResultValueCell();
					ApplicationLauncher.logger.debug("populateMeterIndividualReportFunctionalTestData: FunctionalTest populateResultValue CellPosition: " + resultCellPosition);
					ApplicationLauncher.logger.debug("populateMeterIndividualReportFunctionalTestData: FunctionalTest populateResultValue getResultValue(): " + functionalTestMeterResult.getResultValue());

					boolean overRideResultValue = DeviceDataManagerController.getConveyorConfigParsedKey().getFunctionalTestReportDisplay().isDisplayOverRideResultValue();
					if(overRideResultValue) {


						if (functionalTestMeterResult.getResultStatus().toUpperCase().equals(ConstantReport.REPORT_POPULATE_PASS.toUpperCase())) {
							resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getFunctionalTestReportResultValue().getResultValueOverRidePassValue();
						}else {
							resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getFunctionalTestReportResultValue().getResultValueOverRideFailValue();

						}
					}else {
						resultData = functionalTestMeterResult.getResultValue().replace(ConstantReport.REPORT_POPULATE_PASS, "").replace(ConstantReport.REPORT_POPULATE_FAIL, "").trim();
					}

					String prefixData = "";
					boolean prefixResultValue = DeviceDataManagerController.getConveyorConfigParsedKey().getFunctionalTestReportDisplay().isDisplayResultValuePrefix();

					if(prefixResultValue) {
						prefixData = DeviceDataManagerController.getConveyorConfigParsedKey().getFunctionalTestReportResultValue().getResultValuePrefixValue();
					}

					String postfixData = "";
					boolean postfixResultValue = DeviceDataManagerController.getConveyorConfigParsedKey().getFunctionalTestReportDisplay().isDisplayResultValuePostfix();

					if(postfixResultValue) {
						postfixData = DeviceDataManagerController.getConveyorConfigParsedKey().getFunctionalTestReportResultValue().getResultValuePostfixValue();
					}
					resultData = prefixData + resultData + postfixData;

					ApplicationLauncher.logger.debug("populateMeterIndividualReportFunctionalTestData: FunctionalTest populateResultValue resultData: " + resultData);

					status = FillReportDataColumnXSSF(sheet1, resultData,resultCellPosition);

				}

				//status = FillReportDataColumnXSSF(sheet1, dutSerialNo,resultCellPosition);
				if(!status) {
					//overAllStatus =false;
				}
			}
		}



		return status;

	}
	
	
	public  boolean populateRoutineTestOverAllResult(XSSFSheet sheet1,String dutOverAllStatus) {
		// TODO Auto-generated method stub

		ApplicationLauncher.logger.debug("populateRoutineTestOverAllResult: Entry");
		boolean status = true;
		String resultCellPosition = "";
		String resultData = "";

		boolean populateRotineTestSummaryEnabled = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportDisplay().isDisplayRoutineTestInReport();
		//InsulationResistanceMeterResult.setResultStatus("Pass");
		if(populateRotineTestSummaryEnabled){
			ApplicationLauncher.logger.debug("populateRoutineTestOverAllResult: populateRotineTestSummaryEnabled Populate enabled");
			boolean populateResultStatus = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportDisplay().isDisplayRoutineTestOverAllResultStatus();
			ApplicationLauncher.logger.debug("populateRoutineTestOverAllResult:  populateResultStatus enabled");	

			if(populateResultStatus) {
				resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportCellPosition().getRoutineTestOverAllResultStatusCell();
				ApplicationLauncher.logger.debug("populateRoutineTestOverAllResult:  populateResultStatus CellPosition: " + resultCellPosition);
				ApplicationLauncher.logger.debug("populateRoutineTestOverAllResult:  populateResultStatus dutOverAllStatus: " + dutOverAllStatus);

				boolean overRideResultStatus = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportDisplay().isDisplayRoutineTestOverAllOverRideResultStatus();
				if(overRideResultStatus) {


					if (dutOverAllStatus.toUpperCase().equals(ConstantReport.REPORT_POPULATE_PASS.toUpperCase())) {
						resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportResultValue().getOverAllResultStatusOverRidePassValue();
					}else {
						resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportResultValue().getOverAllResultStatusOverRideFailValue();

					}
				}else {
					resultData = dutOverAllStatus;
				}

				String prefixData = "";
				boolean prefixResultStatus = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportDisplay().isDisplayRoutineTestOverAllResultStatusPrefix();

				if(prefixResultStatus) {
					prefixData = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportResultValue().getOverAllResultStatusPrefixValue();
				}

				String postfixData = "";
				boolean postfixResultStatus = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportDisplay().isDisplayRoutineTestOverAllResultStatusPostfix();

				if(postfixResultStatus) {
					postfixData = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportResultValue().getOverAllResultStatusPostfixValue();
				}
				resultData = prefixData + resultData + postfixData;

				ApplicationLauncher.logger.debug("populateRoutineTestOverAllResult:  populateResultStatus resultData: " + resultData);

				status = FillReportDataColumnXSSF(sheet1, resultData,resultCellPosition);

			}



			boolean populateResultValue = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportDisplay().isDisplayRoutineTestOverAllResultValue();
			ApplicationLauncher.logger.debug("populateRoutineTestOverAllResult:  populateResultValue enabled");	
			//InsulationResistanceMeterResult.setResultStatus("Fail");
			if(populateResultValue) {
				resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportCellPosition().getRoutineTestOverAllResultValueCell();
				ApplicationLauncher.logger.debug("populateRoutineTestOverAllResult:  populateResultValue CellPosition: " + resultCellPosition);
				ApplicationLauncher.logger.debug("populateRoutineTestOverAllResult:  populateResultValue getResultValue(): " + dutOverAllStatus);

				boolean overRideResultValue = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportDisplay().isDisplayRoutineTestOverAllOverRideResultValue();
				if(overRideResultValue) {


					if (dutOverAllStatus.toUpperCase().equals(ConstantReport.REPORT_POPULATE_PASS.toUpperCase())) {
						resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportResultValue().getOverAllResultValueOverRidePassValue();
					}else {
						resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportResultValue().getOverAllResultValueOverRideFailValue();

					}
				}else {
					resultData = dutOverAllStatus.replace(ConstantReport.REPORT_POPULATE_PASS, "").replace(ConstantReport.REPORT_POPULATE_FAIL, "").trim();
				}

				String prefixData = "";
				boolean prefixResultValue = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportDisplay().isDisplayRoutineTestOverAllResultValuePrefix();

				if(prefixResultValue) {
					prefixData = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportResultValue().getOverAllResultValuePrefixValue();
				}

				String postfixData = "";
				boolean postfixResultValue = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportDisplay().isDisplayRoutineTestOverAllResultValuePostfix();

				if(postfixResultValue) {
					postfixData = DeviceDataManagerController.getConveyorConfigParsedKey().getRoutineSummaryReportResultValue().getOverAllResultValuePostfixValue();
				}
				resultData = prefixData + resultData + postfixData;

				ApplicationLauncher.logger.debug("populateRoutineTestOverAllResult:  populateResultValue resultData: " + resultData);

				status = FillReportDataColumnXSSF(sheet1, resultData,resultCellPosition);

			}

		}

		//status = FillReportDataColumnXSSF(sheet1, dutSerialNo,resultCellPosition);
		if(!status) {
			//overAllStatus =false;
		}
		return status;
	}
	

	public boolean saveExcelAsPDFWithPathAndFileName(String outputReportPath,String outputFileName){

		ApplicationLauncher.logger.info("saveExcelAsPDFWithPathAndFileName: Entry");
		boolean status = false;
		//Sleep(1000);
		String PYTHON_ABSOLUTE_PATH = ConstantAppConfig.PYTHON_EXE_LOCATION;
		String script_path = ConstantAppConfig.PYTHON_SCRIPT_LOCATION;
		ApplicationLauncher.logger.info("saveExcelAsPDFWithPathAndFileName: script_path1: "+script_path);
		/*		File file = new File(ConstantConfig.reportPythonFilePathName);
		script_path = file.getAbsolutePath();*/
		//script_path = ConstantConfig.reportPythonFilePathName;

		/*		try {
			URL resource = TestReportController.class.getResource(ConstantVersion.pythonFileName);
			File file = Paths.get(resource.toURI()).toFile();
			script_path = file.getAbsolutePath();
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			ApplicationLauncher.logger.error("saveExcelAsPDFWithPathAndFileName: ExceptionA:"+e1.getMessage());

		}*/

		/*		ClassLoader classLoader = getClass().getClassLoader();
		script_path  = classLoader.getResource(ConstantVersion.pythonFileName).getPath();*/

		URL resource = TestReportController.class.getResource(ConstantAppConfig.reportPythonFilePathName);
		try {
			//File file = Paths.get(resource.toURI()).toFile();
			script_path = Paths.get(resource.toURI()).toFile().getAbsolutePath();
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			ApplicationLauncher.logger.error("saveExcelAsPDFWithPathAndFileName: ExceptionA:"+e1.getMessage());
			script_path = ConstantAppConfig.PYTHON_SCRIPT_LOCATION;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			ApplicationLauncher.logger.error("saveExcelAsPDFWithPathAndFileName: ExceptionB:"+e1.getMessage());
			script_path = ConstantAppConfig.PYTHON_SCRIPT_LOCATION;
		} // return a file


		ApplicationLauncher.logger.info("saveExcelAsPDFWithPathAndFileName: script_path2: "+script_path);
		//ApplicationLauncher.logger.info("saveExcelAsPDFWithPathAndFileName: ConstantReport.SAVE_FILE_LOCATION: "+ConstantReport.SAVE_FILE_LOCATION);
		String ReportPath = outputReportPath+"\\";//getSaveFilePathV2(outputReportPath);

		ApplicationLauncher.logger.info("saveExcelAsPDFWithPathAndFileName: ReportPath:"+ReportPath);
		ReportPath = ReportPath.replace("\\\\", "\\");
		ReportPath = ReportPath.replace("\\\\", "\\");
		//ReportPath = ReportPath.replace("\\", "\\\\");
		ApplicationLauncher.logger.info("saveExcelAsPDFWithPathAndFileName: ReportPath2:"+ReportPath);
		ApplicationLauncher.logger.info("saveExcelAsPDFWithPathAndFileName: CONSOLIDATED_PDF_REPORT_FILE_NAME:"+ConstantReport.CONSOLIDATED_PDF_REPORT_FILE_NAME);
		//String outputFileName = getConsolidateFileNamePath(ConstantReport.CONSOLIDATED_PDF_REPORT_FILE_NAME);
		//outputFileName = outputFileName.replace(" ", "_");
		ApplicationLauncher.logger.debug("saveExcelAsPDFWithPathAndFileName: outputFileName:"+outputFileName);
		File pyExecFile = new File(PYTHON_ABSOLUTE_PATH);
		if(pyExecFile.isFile() ) { 
			// do something
			File pyScriptFile = new File(script_path);
			if(pyScriptFile.isFile() ) { 
				//String Command = PYTHON_ABSOLUTE_PATH + " " + script_path + " \"" + ReportPath + "\\\" \"" + outputFileName + "\"";
				//String Command = PYTHON_ABSOLUTE_PATH + " \"" + script_path + "\" \"" + ReportPath + "\" \"" + outputFileName + "\"";
				if(ReportPath.contains(" ")) {
					//ReportPath = "\"" + ReportPath + "\"";
					ApplicationLauncher.logger.debug("saveExcelAsPDFWithPathAndFileName: ReportPath1:"+ReportPath);
				}

				if(outputFileName.contains(" ")) {
					//outputFileName = "\"" + outputFileName + "\"";
					ApplicationLauncher.logger.debug("saveExcelAsPDFWithPathAndFileName: outputFileName1:"+outputFileName);
				}
				String Command = PYTHON_ABSOLUTE_PATH + " \"" + script_path + "\" " + ReportPath + " " + outputFileName ;//+ "\"";

				String args[] = { PYTHON_ABSOLUTE_PATH,  script_path , ReportPath, outputFileName};
				//Runtime.getRuntime().exec(args);

				ApplicationLauncher.logger.debug("saveExcelAsPDFWithPathAndFileName: Command: "+Command);
				ApplicationLauncher.logger.debug("saveExcelAsPDFWithPathAndFileName: args: "+Arrays.asList(args));
				try {
					//Process p = Runtime.getRuntime().exec(Command);
					/*Process p = Runtime.getRuntime().exec(args);
					BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
					String pythonOutput = "";
					//while ((pythonOutput = in.readLine()) != null) {
					pythonOutput = in.readLine();*/


					Process p = Runtime.getRuntime().exec(args);
					BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
					String pythonOutput;
					while ((pythonOutput = errorReader.readLine()) != null) {
						ApplicationLauncher.logger.debug("saveExcelAsPDFWithPathAndFileName: Python Error: " + pythonOutput);
					}

					ApplicationLauncher.logger.info("saveExcelAsPDFWithPathAndFileName: pythonOutput: "+pythonOutput);
					if(pythonOutput == null){

						ApplicationLauncher.logger.info("saveExcelAsPDFWithPathAndFileName: pythonOutput: null:  "+pythonOutput);
						return false;
					}




				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ApplicationLauncher.logger.error("saveExcelAsPDFWithPathAndFileName: Exception2:"+e.getMessage());
					return false;
				}
			}else{
				ApplicationLauncher.logger.info("saveExcelAsPDFWithPathAndFileName: Python Script file does not exist in path :"+script_path);
				ApplicationLauncher.InformUser("Error P201", "PDF generation failed due to invalid script path:\n"+script_path, AlertType.ERROR);
				return false;
			}
		}else{
			ApplicationLauncher.logger.info("saveExcelAsPDFWithPathAndFileName: Python execution file does not exist in path :"+PYTHON_ABSOLUTE_PATH);
			ApplicationLauncher.InformUser("Error P202", "PDF generation failed due to invalid python executable path:\n"+PYTHON_ABSOLUTE_PATH, AlertType.ERROR);
			return false;
		}


		ApplicationLauncher.logger.info("saveExcelAsPDFWithPathAndFileName: Exit");
		return status;

	}
	
	public Boolean populateMeterIndividualReportHighVoltageData(XSSFSheet sheet1, List<MeterResultDetailed> meterResultDetailedList){
		boolean status = true;

		ApplicationLauncher.logger.debug("populateMeterIndividualReportHighVoltageData: Entry ");

		Optional<MeterResultDetailed>  highVoltageMeterResultOpt = meterResultDetailedList.stream()
				.filter(m -> m.getTestType().startsWith(ConstantConveyor.HV_ALIAS_NAME))//STA_ALIAS_NAME))
				.findFirst();
		//.collect(Collectors.toList());
		String resultData = "";
		String resultCellPosition= "";
		if(highVoltageMeterResultOpt.isPresent()) {
			ApplicationLauncher.logger.debug("populateMeterIndividualReportHighVoltageData: HighVoltage Result Found ");

			MeterResultDetailed highVoltageMeterResult = highVoltageMeterResultOpt.get();
			ApplicationLauncher.logger.debug("populateMeterIndividualReportHighVoltageData: HighVoltage : getTestName: " + highVoltageMeterResult.getTestName());
			boolean populateData = DeviceDataManagerController.getConveyorConfigParsedKey().getHighVoltageReportDisplay().isDisplayHighVoltageInReport();
			//HighVoltageMeterResult.setResultStatus("Pass");
			if(populateData){
				ApplicationLauncher.logger.debug("populateMeterIndividualReportHighVoltageData: HighVoltage Result Populate enabled");	

				boolean populateDescription = DeviceDataManagerController.getConveyorConfigParsedKey().getHighVoltageReportDisplay().isDisplayDescription();
				ApplicationLauncher.logger.debug("populateMeterIndividualReportHighVoltageData: HighVoltage populateDescription enabled");	
				if(populateDescription) {
					resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getHighVoltageReportCellPosition().getDescriptionCell();
					ApplicationLauncher.logger.debug("populateMeterIndividualReportHighVoltageData: HighVoltage populateDescription CellPosition: " + resultCellPosition);
					ApplicationLauncher.logger.debug("populateMeterIndividualReportHighVoltageData: HighVoltage populateDescription getResultStatus(): " + highVoltageMeterResult.getResultStatus());

					if (highVoltageMeterResult.getResultStatus().toUpperCase().equals(ConstantReport.REPORT_POPULATE_PASS.toUpperCase())) {
						resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getHighVoltageReportResultValue().getDescriptionPassValue();
					}else {
						resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getHighVoltageReportResultValue().getDescriptionFailValue();

					}
					ApplicationLauncher.logger.debug("populateMeterIndividualReportHighVoltageData: HighVoltage populateDescription resultData: " + resultData);

					status = FillReportDataColumnXSSF(sheet1, resultData,resultCellPosition);

				}


				boolean populateResultStatus = DeviceDataManagerController.getConveyorConfigParsedKey().getHighVoltageReportDisplay().isDisplayResultStatus();
				ApplicationLauncher.logger.debug("populateMeterIndividualReportHighVoltageData: HighVoltage populateResultStatus enabled");	

				if(populateResultStatus) {
					resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getHighVoltageReportCellPosition().getResultStatusCell();
					ApplicationLauncher.logger.debug("populateMeterIndividualReportHighVoltageData: HighVoltage populateResultStatus CellPosition: " + resultCellPosition);
					ApplicationLauncher.logger.debug("populateMeterIndividualReportHighVoltageData: HighVoltage populateResultStatus getResultStatus(): " + highVoltageMeterResult.getResultStatus());

					boolean overRideResultStatus = DeviceDataManagerController.getConveyorConfigParsedKey().getHighVoltageReportDisplay().isDisplayOverRideResultStatus();
					if(overRideResultStatus) {


						if (highVoltageMeterResult.getResultStatus().toUpperCase().equals(ConstantReport.REPORT_POPULATE_PASS.toUpperCase())) {
							resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getHighVoltageReportResultValue().getResultStatusOverRidePassValue();
						}else {
							resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getHighVoltageReportResultValue().getResultStatusOverRideFailValue();

						}
					}else {
						resultData = highVoltageMeterResult.getResultStatus();
					}

					String prefixData = "";
					boolean prefixResultStatus = DeviceDataManagerController.getConveyorConfigParsedKey().getHighVoltageReportDisplay().isDisplayResultStatusPrefix();

					if(prefixResultStatus) {
						prefixData = DeviceDataManagerController.getConveyorConfigParsedKey().getHighVoltageReportResultValue().getResultStatusPrefixValue();
					}

					String postfixData = "";
					boolean postfixResultStatus = DeviceDataManagerController.getConveyorConfigParsedKey().getHighVoltageReportDisplay().isDisplayResultStatusPostfix();

					if(postfixResultStatus) {
						postfixData = DeviceDataManagerController.getConveyorConfigParsedKey().getHighVoltageReportResultValue().getResultStatusPostfixValue();
					}
					resultData = prefixData + resultData + postfixData;

					ApplicationLauncher.logger.debug("populateMeterIndividualReportHighVoltageData: HighVoltage populateResultStatus resultData: " + resultData);

					status = FillReportDataColumnXSSF(sheet1, resultData,resultCellPosition);

				}



				boolean populateResultValue = DeviceDataManagerController.getConveyorConfigParsedKey().getHighVoltageReportDisplay().isDisplayResultValue();
				ApplicationLauncher.logger.debug("populateMeterIndividualReportHighVoltageData: HighVoltage populateResultValue enabled");	
				//HighVoltageMeterResult.setResultStatus("Fail");
				if(populateResultValue) {
					resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getHighVoltageReportCellPosition().getResultValueCell();
					ApplicationLauncher.logger.debug("populateMeterIndividualReportHighVoltageData: HighVoltage populateResultValue CellPosition: " + resultCellPosition);
					ApplicationLauncher.logger.debug("populateMeterIndividualReportHighVoltageData: HighVoltage populateResultValue getResultValue(): " + highVoltageMeterResult.getResultValue());

					boolean overRideResultValue = DeviceDataManagerController.getConveyorConfigParsedKey().getHighVoltageReportDisplay().isDisplayOverRideResultValue();
					if(overRideResultValue) {


						if (highVoltageMeterResult.getResultStatus().toUpperCase().equals(ConstantReport.REPORT_POPULATE_PASS.toUpperCase())) {
							resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getHighVoltageReportResultValue().getResultValueOverRidePassValue();
						}else {
							resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getHighVoltageReportResultValue().getResultValueOverRideFailValue();

						}
					}else {
						resultData = highVoltageMeterResult.getResultValue().replace(ConstantReport.REPORT_POPULATE_PASS, "").replace(ConstantReport.REPORT_POPULATE_FAIL, "").trim();
					}

					String prefixData = "";
					boolean prefixResultValue = DeviceDataManagerController.getConveyorConfigParsedKey().getHighVoltageReportDisplay().isDisplayResultValuePrefix();

					if(prefixResultValue) {
						prefixData = DeviceDataManagerController.getConveyorConfigParsedKey().getHighVoltageReportResultValue().getResultValuePrefixValue();
					}

					String postfixData = "";
					boolean postfixResultValue = DeviceDataManagerController.getConveyorConfigParsedKey().getHighVoltageReportDisplay().isDisplayResultValuePostfix();

					if(postfixResultValue) {
						postfixData = DeviceDataManagerController.getConveyorConfigParsedKey().getHighVoltageReportResultValue().getResultValuePostfixValue();
					}
					resultData = prefixData + resultData + postfixData;

					ApplicationLauncher.logger.debug("populateMeterIndividualReportHighVoltageData: HighVoltage populateResultValue resultData: " + resultData);

					status = FillReportDataColumnXSSF(sheet1, resultData,resultCellPosition);

				}

				//status = FillReportDataColumnXSSF(sheet1, dutSerialNo,resultCellPosition);
				if(!status) {
					//overAllStatus =false;
				}
			}
		}



		return status;

	}
	
	public Boolean populateMeterProfileDataV2(XSSFSheet sheet1){


		ApplicationLauncher.logger.debug("populateMeterProfileData: Entry ");
		boolean status = true;
		//JSONObject modeldata  = new JSONObject();
		int modelId = 0;

		sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);  

		try {
			JSONObject modeldata = null; 
			try {
				//JSONObject modeldata  = new JSONObject();
				String modelConfigName = DeviceDataManagerController.getConveyorConfigParsedKey().getReportResultDataCellPosition().getActiveMeterProfileModelMeterConfig();
				modelId = MySQL_Controller.sp_getModel_ID(modelConfigName);
				ApplicationLauncher.logger.debug("populateMeterProfileDataV2: modelId: " + modelId);
				if(modelId!=0) {

					JSONObject modeldata1 = MySQL_Controller.sp_getem_model_data(modelId);
					modeldata = modeldata1;
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				ApplicationLauncher.logger.error("populateMeterProfileDataV2: modelConfigName: Exception: " + e.getMessage());
				ApplicationLauncher.logger.debug("Error-EM0001:  Invalid Meter config in the Meter Profile.\\n\\nKindly reconfigure <ActiveMeterProfileModelMeterConfig> in conveyor_config.json, restart the application and try again - prompted");
				ApplicationLauncher.InformUser("Error-EM0001", "Invalid Meter config in the Meter Profile.\n\nKindly reconfigure <ActiveMeterProfileModelMeterConfig> in conveyor_config.json, restart the application and try again", AlertType.ERROR);

				return false;
			}

			if(modelId==0) {
				ApplicationLauncher.logger.debug("Error-EM0002:  Invalid Meter config in the Meter Profile.\\n\\nKindly reconfigure <ActiveMeterProfileModelMeterConfig> in conveyor_config.json, restart the application and try again - prompted");
				ApplicationLauncher.InformUser("Error-EM0002", "Invalid Meter config in the Meter Profile.\n\nKindly reconfigure <ActiveMeterProfileModelMeterConfig> in conveyor_config.json, restart the application and try again", AlertType.ERROR);

				return false;
			}


			//setMeterProfileData(modeldata);

			//JSONObject modeldata = getMeterProfileData();

			boolean overAllStatus = true;
			//status = meterProfileReportUpdateFrequency(sheet1,modeldata);
			status = meterProfileReportUpdateCustomerNameV2(sheet1,modeldata);



			if(!status) {
				overAllStatus =false;
			}
			ApplicationLauncher.logger.debug("populateMeterProfileDataV2: Status1 : " + status);

			status = meterProfileReportUpdateMeterModelV2(sheet1,modeldata);
			if(!status) {
				overAllStatus =false;
			}

			ApplicationLauncher.logger.debug("populateMeterProfileDataV2: Status2 : " + status);

			status = meterProfileReportUpdateMeterTypeV2(sheet1,modeldata);

			if(!status) {
				overAllStatus =false;
			}
			ApplicationLauncher.logger.debug("populateMeterProfileDataV2: Status3 : " + status);

			status = meterProfileReportUpdateFrequencyV2(sheet1,modeldata);

			if(!status) {
				overAllStatus =false;
			}

			status = meterProfileReportUpdateMeterClassV2(sheet1,modeldata);

			if(!status) {
				overAllStatus =false;
			}
			ApplicationLauncher.logger.debug("populateMeterProfileDataV2: Status4 : " + status);

			status = meterProfileReportUpdateBasicCurrentV2(sheet1,modeldata);

			if(!status) {
				overAllStatus =false;
			}
			ApplicationLauncher.logger.debug("populateMeterProfileDataV2: Status5 : " + status);

			status = meterProfileReportUpdateMaxCurrentV2(sheet1,modeldata);	

			if(!status) {
				overAllStatus =false;
			}
			ApplicationLauncher.logger.debug("populateMeterProfileDataV2: Status6 : " + status);

			status = meterProfileReportUpdateRatedVoltageV2(sheet1,modeldata);	

			if(!status) {
				overAllStatus =false;
			}
			ApplicationLauncher.logger.debug("populateMeterProfileDataV2: Status7 : " + status);

			status = meterProfileReportUpdateNoOfImpulsesV2(sheet1,modeldata);	

			if(!status) {
				overAllStatus =false;
			}
			ApplicationLauncher.logger.debug("populateMeterProfileDataV2: Status8 : " + status);

			//status = meterProfileReportUpdateCtType(sheet1,modeldata);

			if(!status) {
				overAllStatus =false;
			}
			ApplicationLauncher.logger.debug("populateMeterProfileDataV2: Status9 : " + status);

			//status = meterProfileReportUpdateCtRatio(sheet1,modeldata);

			if(!status) {
				overAllStatus =false;
			}
			ApplicationLauncher.logger.debug("populateMeterProfileDataV2: Status10 : " + status);

			//status = meterProfileReportUpdatePtRatio(sheet1,modeldata);

			if(!status) {
				overAllStatus =false;
			}
			ApplicationLauncher.logger.debug("populateMeterProfileDataV2: Status11 : " + status);

			//status = meterProfileReportUpdateReportSerialNo(sheet1);

			if(!status) {
				overAllStatus =false;
			}
			ApplicationLauncher.logger.debug("populateMeterProfileDataV2: Status12 : " + status);

			//status = meterProfileReportUpdateNoOfPages(sheet1);

			if(!status) {
				overAllStatus =false;
			}
			ApplicationLauncher.logger.debug("populateMeterProfileDataV2: Status13 : " + status);

			//status = meterProfileReportUpdatePresentPageNo(sheet1);

			if(!status) {
				overAllStatus =false;
			}
			ApplicationLauncher.logger.debug("populateMeterProfileDataV2: Status14 : " + status);

			//status = meterProfileReportUpdateExecutedDate(sheet1);

			if(!status) {
				overAllStatus =false;
			}
			ApplicationLauncher.logger.debug("populateMeterProfileDataV2: Status15 : " + status);

			//status = meterProfileReportUpdateExecutedTime(sheet1);	

			if(!status) {
				overAllStatus =false;
			}
			ApplicationLauncher.logger.debug("populateMeterProfileDataV2: Status16 : " + status);

			//status = meterProfileReportUpdateGeneratedDate(sheet1);

			if(!status) {
				overAllStatus =false;
			}
			ApplicationLauncher.logger.debug("populateMeterProfileDataV2: Status17 : " + status);

			//status = meterProfileReportUpdateGeneratedTime(sheet1);

			if(!status) {
				overAllStatus =false;
			}
			ApplicationLauncher.logger.debug("populateMeterProfileDataV2: Status18 : " + status);

			status = meterProfileReportUpdateMeterSerialNo(sheet1);

			if(!status) {
				overAllStatus =false;
			}
			ApplicationLauncher.logger.debug("populateMeterProfileDataV2: Status19 : " + status);

			//status = meterProfileReportUpdateTesterName(sheet1);

			if(!status) {
				overAllStatus =false;
			}
			ApplicationLauncher.logger.debug("populateMeterProfileDataV2: Status20 : " + status);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("populateMeterProfileDataV2: modelConfigName: Exception2: " + e.getMessage());
			//ApplicationLauncher.logger.debug("Error-EM0002:  Invalid Meter config in the Meter Profile.\\n\\nKindly reconfigure <ActiveMeterProfileModelMeterConfig> in conveyor_config.json, restart the application and try again - prompted");
			//ApplicationLauncher.InformUser("Error-EM0003", "Invalid Meter config in the Meter Profile.\n\nKindly reconfigure <ActiveMeterProfileModelMeterConfig> in conveyor_config.json, restart the application and try again", AlertType.ERROR);

			return false;
		}

		return status;

	}
	
	
	public boolean meterProfileReportUpdateMeterTypeV2(XSSFSheet sheet1, JSONObject modeldata){
		ApplicationLauncher.logger.debug("meterProfileReportUpdateMeterTypeV2: Entry");

		boolean status = false;
		try {
			boolean populateMeterType = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportDisplay().isDisplayMeterType();
			if(populateMeterType){
				String meterTypeCell = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportCellPosition().getMeterTypeCell();

				try {
					String modelTypeData = modeldata.getString("model_type");
					status = FillReportDataColumnXSSF(sheet1, modelTypeData,meterTypeCell);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("meterProfileReportUpdateMeterTypeV2: model_type: JSONException: " + e.getMessage());
				}

			}else{
				status = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("meterProfileReportUpdateMeterType: Exception2: " + e.getMessage());
		}
		return status;
	}

	public boolean meterProfileReportUpdateMaxCurrentV2(XSSFSheet sheet1, JSONObject modeldata){
		ApplicationLauncher.logger.debug("meterProfileReportUpdateMaxCurrentV2: Entry");

		boolean status = false;
		String resultCellPosition = "";
		try {

			boolean populateMaxCurrent = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportDisplay().isDisplayMaxCurrent();
			if(populateMaxCurrent){
				ApplicationLauncher.logger.debug("meterProfileReportUpdateMaxCurrentV2: populateMaxCurrent enabled");	

				resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportCellPosition().getMaxCurrentCell();
				//ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: populateRoutineTestPropertOfEnabled enabled");	
				String maxCurrentData = modeldata.getString("max_current_imax");
				try {
					// Convert to float first (handles both int and float cases)
					float floatValue = Float.parseFloat(maxCurrentData);

					// Convert to int (rounding down)
					int intValue = Math.round(floatValue); // Use (int) floatValue for truncation

					// Convert back to string
					maxCurrentData = String.valueOf(intValue);
				} catch (NumberFormatException e) {
					ApplicationLauncher.logger.error("meterProfileReportUpdateMaxCurrentV2 : Exception :Invalid number format: " + maxCurrentData);
				}
				ApplicationLauncher.logger.debug("meterProfileReportUpdateMaxCurrentV2: MaxCurrent : " + maxCurrentData);
				boolean prefixEnabled = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportDisplay().isDisplayPrefixMaxCurrent();
				ApplicationLauncher.logger.debug("meterProfileReportUpdateMaxCurrentV2: MaxCurrent prefixEnabled: " + prefixEnabled);
				if(prefixEnabled) {
					String prefixData = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportPreAndPostFixValue().getPrefixMaxCurrentValue();
					ApplicationLauncher.logger.debug("meterProfileReportUpdateMaxCurrentV2: MaxCurrent prefixData: " + prefixData);

					maxCurrentData = prefixData + maxCurrentData;
				}

				boolean postfixEnabled = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportDisplay().isDisplayPostfixMaxCurrent();
				ApplicationLauncher.logger.debug("meterProfileReportUpdateMaxCurrentV2: MaxCurrent prefixEnabled: " + prefixEnabled);
				if(postfixEnabled) {
					String postfixData = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportPreAndPostFixValue().getPostfixMaxCurrentValue();
					ApplicationLauncher.logger.debug("meterProfileReportUpdateMaxCurrentV2: MaxCurrent postfixData: " + postfixData);

					maxCurrentData =  maxCurrentData + postfixData;
				}

				status = FillReportDataColumnXSSF(sheet1, maxCurrentData,resultCellPosition);

			}
			else{
				status = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("meterProfileReportUpdateMaxCurrentV2: Exception2: " + e.getMessage());
		}
		return status;
	}

	public boolean meterProfileReportUpdateMeterClassV2(XSSFSheet sheet1, JSONObject modeldata){
		ApplicationLauncher.logger.debug("meterProfileReportUpdateMeterClassV2: Entry");

		boolean status = false;
		try {



			boolean populateMeterClass = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportDisplay().isDisplayMeterClass();
			if(populateMeterClass){
				ApplicationLauncher.logger.debug("meterProfileReportUpdateMeterClassV2: populateMeterClass enabled");	

				String resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportCellPosition().getMeterClassCell();
				//ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: populateRoutineTestPropertOfEnabled enabled");	
				String meterClassData = modeldata.getString("model_class");

				ApplicationLauncher.logger.debug("meterProfileReportUpdateMeterClassV2: MeterClass : " + meterClassData);
				boolean prefixEnabled = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportDisplay().isDisplayPrefixMeterClass();
				ApplicationLauncher.logger.debug("meterProfileReportUpdateMeterClassV2: MeterClass prefixEnabled: " + prefixEnabled);
				if(prefixEnabled) {
					String prefixData = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportPreAndPostFixValue().getPrefixMeterClassValue();
					ApplicationLauncher.logger.debug("meterProfileReportUpdateMeterClassV2: MeterClass prefixData: " + prefixData);

					meterClassData = prefixData + meterClassData;
				}

				boolean postfixEnabled = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportDisplay().isDisplayPostfixMeterClass();
				ApplicationLauncher.logger.debug("meterProfileReportUpdateMeterClassV2: MeterClass prefixEnabled: " + prefixEnabled);
				if(postfixEnabled) {
					String postfixData = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportPreAndPostFixValue().getPostfixMeterClassValue();
					ApplicationLauncher.logger.debug("meterProfileReportUpdateMeterClassV2: MeterClass postfixData: " + postfixData);

					meterClassData =  meterClassData + postfixData;
				}

				status = FillReportDataColumnXSSF(sheet1, meterClassData,resultCellPosition);

			}else{
				status = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("meterProfileReportUpdateMeterClassV2: Exception2: " + e.getMessage());
		}
		return status;
	}

	public Boolean populateMeterIndividualReportInsulationResistanceData(XSSFSheet sheet1, List<MeterResultDetailed> meterResultDetailedList){
		boolean status = true;

		ApplicationLauncher.logger.debug("populateMeterIndividualReportInsulationResistanceData: Entry ");

		Optional<MeterResultDetailed>  InsulationResistanceMeterResultOpt = meterResultDetailedList.stream()
				.filter(m -> m.getTestType().startsWith(ConstantConveyor.IR_ALIAS_NAME))
				.findFirst();
		//.collect(Collectors.toList());
		String resultData = "";
		String resultCellPosition= "";
		if(InsulationResistanceMeterResultOpt.isPresent()) {
			ApplicationLauncher.logger.debug("populateMeterIndividualReportInsulationResistanceData: InsulationResistance Result Found ");

			MeterResultDetailed InsulationResistanceMeterResult = InsulationResistanceMeterResultOpt.get();
			ApplicationLauncher.logger.debug("populateMeterIndividualReportInsulationResistanceData: InsulationResistance : getTestName: " + InsulationResistanceMeterResult.getTestName());
			boolean populateData = DeviceDataManagerController.getConveyorConfigParsedKey().getInsulationResistanceReportDisplay().isDisplayInsulationResistanceInReport();
			//InsulationResistanceMeterResult.setResultStatus("Pass");
			if(populateData){
				ApplicationLauncher.logger.debug("populateMeterIndividualReportInsulationResistanceData: InsulationResistance Result Populate enabled");	

				boolean populateDescription = DeviceDataManagerController.getConveyorConfigParsedKey().getInsulationResistanceReportDisplay().isDisplayDescription();
				ApplicationLauncher.logger.debug("populateMeterIndividualReportInsulationResistanceData: InsulationResistance populateDescription enabled");	
				if(populateDescription) {
					resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getInsulationResistanceReportCellPosition().getDescriptionCell();
					ApplicationLauncher.logger.debug("populateMeterIndividualReportInsulationResistanceData: InsulationResistance populateDescription CellPosition: " + resultCellPosition);
					ApplicationLauncher.logger.debug("populateMeterIndividualReportInsulationResistanceData: InsulationResistance populateDescription getResultStatus(): " + InsulationResistanceMeterResult.getResultStatus());

					if (InsulationResistanceMeterResult.getResultStatus().toUpperCase().equals(ConstantReport.REPORT_POPULATE_PASS.toUpperCase())) {
						resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getInsulationResistanceReportResultValue().getDescriptionPassValue();
					}else {
						resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getInsulationResistanceReportResultValue().getDescriptionFailValue();

					}
					ApplicationLauncher.logger.debug("populateMeterIndividualReportInsulationResistanceData: InsulationResistance populateDescription resultData: " + resultData);

					status = FillReportDataColumnXSSF(sheet1, resultData,resultCellPosition);

				}


				boolean populateResultStatus = DeviceDataManagerController.getConveyorConfigParsedKey().getInsulationResistanceReportDisplay().isDisplayResultStatus();
				ApplicationLauncher.logger.debug("populateMeterIndividualReportInsulationResistanceData: InsulationResistance populateResultStatus enabled");	

				if(populateResultStatus) {
					resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getInsulationResistanceReportCellPosition().getResultStatusCell();
					ApplicationLauncher.logger.debug("populateMeterIndividualReportInsulationResistanceData: InsulationResistance populateResultStatus CellPosition: " + resultCellPosition);
					ApplicationLauncher.logger.debug("populateMeterIndividualReportInsulationResistanceData: InsulationResistance populateResultStatus getResultStatus(): " + InsulationResistanceMeterResult.getResultStatus());

					boolean overRideResultStatus = DeviceDataManagerController.getConveyorConfigParsedKey().getInsulationResistanceReportDisplay().isDisplayOverRideResultStatus();
					if(overRideResultStatus) {


						if (InsulationResistanceMeterResult.getResultStatus().toUpperCase().equals(ConstantReport.REPORT_POPULATE_PASS.toUpperCase())) {
							resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getInsulationResistanceReportResultValue().getResultStatusOverRidePassValue();
						}else {
							resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getInsulationResistanceReportResultValue().getResultStatusOverRideFailValue();

						}
					}else {
						resultData = InsulationResistanceMeterResult.getResultStatus();
					}

					String prefixData = "";
					boolean prefixResultStatus = DeviceDataManagerController.getConveyorConfigParsedKey().getInsulationResistanceReportDisplay().isDisplayResultStatusPrefix();

					if(prefixResultStatus) {
						prefixData = DeviceDataManagerController.getConveyorConfigParsedKey().getInsulationResistanceReportResultValue().getResultStatusPrefixValue();
					}

					String postfixData = "";
					boolean postfixResultStatus = DeviceDataManagerController.getConveyorConfigParsedKey().getInsulationResistanceReportDisplay().isDisplayResultStatusPostfix();

					if(postfixResultStatus) {
						postfixData = DeviceDataManagerController.getConveyorConfigParsedKey().getInsulationResistanceReportResultValue().getResultStatusPostfixValue();
					}
					resultData = prefixData + resultData + postfixData;

					ApplicationLauncher.logger.debug("populateMeterIndividualReportInsulationResistanceData: InsulationResistance populateResultStatus resultData: " + resultData);

					status = FillReportDataColumnXSSF(sheet1, resultData,resultCellPosition);

				}



				boolean populateResultValue = DeviceDataManagerController.getConveyorConfigParsedKey().getInsulationResistanceReportDisplay().isDisplayResultValue();
				ApplicationLauncher.logger.debug("populateMeterIndividualReportInsulationResistanceData: InsulationResistance populateResultValue enabled");	
				//InsulationResistanceMeterResult.setResultStatus("Fail");
				if(populateResultValue) {
					resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getInsulationResistanceReportCellPosition().getResultValueCell();
					ApplicationLauncher.logger.debug("populateMeterIndividualReportInsulationResistanceData: InsulationResistance populateResultValue CellPosition: " + resultCellPosition);
					ApplicationLauncher.logger.debug("populateMeterIndividualReportInsulationResistanceData: InsulationResistance populateResultValue getResultValue(): " + InsulationResistanceMeterResult.getResultValue());

					boolean overRideResultValue = DeviceDataManagerController.getConveyorConfigParsedKey().getInsulationResistanceReportDisplay().isDisplayOverRideResultValue();
					if(overRideResultValue) {


						if (InsulationResistanceMeterResult.getResultStatus().toUpperCase().equals(ConstantReport.REPORT_POPULATE_PASS.toUpperCase())) {
							resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getInsulationResistanceReportResultValue().getResultValueOverRidePassValue();
						}else {
							resultData = DeviceDataManagerController.getConveyorConfigParsedKey().getInsulationResistanceReportResultValue().getResultValueOverRideFailValue();

						}
					}else {
						resultData = InsulationResistanceMeterResult.getResultValue().replace(ConstantReport.REPORT_POPULATE_PASS, "").replace(ConstantReport.REPORT_POPULATE_FAIL, "").trim();
					}

					String prefixData = "";
					boolean prefixResultValue = DeviceDataManagerController.getConveyorConfigParsedKey().getInsulationResistanceReportDisplay().isDisplayResultValuePrefix();

					if(prefixResultValue) {
						prefixData = DeviceDataManagerController.getConveyorConfigParsedKey().getInsulationResistanceReportResultValue().getResultValuePrefixValue();
					}

					String postfixData = "";
					boolean postfixResultValue = DeviceDataManagerController.getConveyorConfigParsedKey().getInsulationResistanceReportDisplay().isDisplayResultValuePostfix();

					if(postfixResultValue) {
						postfixData = DeviceDataManagerController.getConveyorConfigParsedKey().getInsulationResistanceReportResultValue().getResultValuePostfixValue();
					}
					resultData = prefixData + resultData + postfixData;

					ApplicationLauncher.logger.debug("populateMeterIndividualReportInsulationResistanceData: InsulationResistance populateResultValue resultData: " + resultData);

					status = FillReportDataColumnXSSF(sheet1, resultData,resultCellPosition);

				}

				//status = FillReportDataColumnXSSF(sheet1, dutSerialNo,resultCellPosition);
				if(!status) {
					//overAllStatus =false;
				}
			}
		}



		return status;

	}
	
	public void promptUserToOpenReportOutputFolderPath(String outputPdfPathFileName,String outputFolderDocPath){
		ApplicationLauncher.logger.debug("promptUserToOpenReportOutputFolderPath: Entry");

		String header = "Do you want to open output folder path?";
		String title = "Report generation success";
		String filePath = outputPdfPathFileName.replace("\\\\", "\\");
		//String outputDocPathLocal = outputDocPath;
		Platform.runLater(() -> {
			String returnedData = GuiUtils.textAreaInputDialogDisplay(header,title,filePath,outputFolderDocPath);
			if(returnedData!=null){
				if(returnedData.equals("OpenOutputFolder")){
					if(!outputFolderDocPath.isEmpty()){
						try {
							Desktop.getDesktop().open(new File(outputFolderDocPath));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							ApplicationLauncher.logger.debug("assertGenerateManualTestJsonFileConfig : Exception: " + e.getMessage());
						}
					}
				}
			}

		});
	}
	
	public boolean meterProfileReportUpdateFrequencyV2(XSSFSheet sheet1, JSONObject modeldata){
		ApplicationLauncher.logger.debug("meterProfileReportUpdateFrequencyV2: Entry");

		boolean status = false;
		try {


			boolean populateFrequency = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportDisplay().isDisplayFrequency();
			if(populateFrequency){
				ApplicationLauncher.logger.debug("meterProfileReportUpdateFrequencyV2: populateFrequency enabled");	

				String resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportCellPosition().getFrequencyCell();
				//ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: populateRoutineTestPropertOfEnabled enabled");	
				String frequencyData = modeldata.getString("frequency");
				try {
					// Convert to float first (handles both int and float cases)
					float floatValue = Float.parseFloat(frequencyData);

					// Convert to int (rounding down)
					int intValue = Math.round(floatValue); // Use (int) floatValue for truncation

					// Convert back to string
					frequencyData = String.valueOf(intValue);
				} catch (NumberFormatException e) {
					ApplicationLauncher.logger.error("meterProfileReportUpdateFrequencyV2 : Exception :Invalid number format: " + frequencyData);
				}
				ApplicationLauncher.logger.debug("meterProfileReportUpdateFrequencyV2: Frequency : " + frequencyData);
				boolean prefixEnabled = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportDisplay().isDisplayPrefixFrequency();
				ApplicationLauncher.logger.debug("meterProfileReportUpdateFrequencyV2: Frequency prefixEnabled: " + prefixEnabled);
				if(prefixEnabled) {
					String prefixData = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportPreAndPostFixValue().getPrefixFrequencyValue();
					ApplicationLauncher.logger.debug("meterProfileReportUpdateFrequencyV2: Frequency prefixData: " + prefixData);

					frequencyData = prefixData + frequencyData;
				}

				boolean postfixEnabled = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportDisplay().isDisplayPostfixFrequency();
				ApplicationLauncher.logger.debug("meterProfileReportUpdateFrequencyV2: Frequency prefixEnabled: " + prefixEnabled);
				if(postfixEnabled) {
					String postfixData = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportPreAndPostFixValue().getPostfixFrequencyValue();
					ApplicationLauncher.logger.debug("meterProfileReportUpdateFrequencyV2: Frequency postfixData: " + postfixData);

					frequencyData =  frequencyData + postfixData;
				}

				status = FillReportDataColumnXSSF(sheet1, frequencyData,resultCellPosition);

			}else{
				status = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("meterProfileReportUpdateFrequencyV2: Exception2: " + e.getMessage());
		}
		return status;
	}
	
	public boolean meterProfileReportUpdateMeterModelV2(XSSFSheet sheet1, JSONObject modeldata){
		ApplicationLauncher.logger.debug("meterProfileReportUpdateMeterModelV2: Entry");

		boolean status = false;
		try {
			boolean populateMeterModel = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportDisplay().isDisplayMeterModelNo();
			if(populateMeterModel){
				String modelNameCell = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportCellPosition().getMeterModelNoCell();

				try {
					String modelNoData = modeldata.getString("customer_name");// meter model no is stored in customer name
					boolean prefixEnabled = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportDisplay().isDisplayPrefixMeterModelNo();
					if(prefixEnabled) {
						String prefixData = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportPreAndPostFixValue().getPrefixMeterModelNoValue();
						modelNoData=		prefixData + modelNoData;
					}
					status = FillReportDataColumnXSSF(sheet1, modelNoData,modelNameCell);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("meterProfileReportUpdateMeterModelV2: model_name: JSONException: " + e.getMessage());
				}

			}else{
				status = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("meterProfileReportUpdateMeterModelV2: Exception2: " + e.getMessage());
		}
		return status;
	}

	
	public boolean meterProfileReportUpdateCustomerNameV2(XSSFSheet sheet1, JSONObject modeldata){
		ApplicationLauncher.logger.debug("meterProfileReportUpdateCustomerName: Entry");

		boolean status = false;
		try {
			boolean populateCustomerName = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportDisplay().isDisplayCustomerName();
			if(populateCustomerName){
				String customerNameCell = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportCellPosition().getCustomerNameCell();


				try {
					String customerNameData = modeldata.getString("customer_name");
					status = FillReportDataColumnXSSF(sheet1, customerNameData,customerNameCell);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("meterProfileReportUpdateCustomerName: customer_name: JSONException: " + e.getMessage());
				}

			}else{
				status = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("meterProfileReportUpdateCustomerName: Exception2: " + e.getMessage());
		}
		return status;
	}

	public boolean meterProfileReportUpdateRatedVoltageV2(XSSFSheet sheet1, JSONObject modeldata){
		ApplicationLauncher.logger.debug("meterProfileReportUpdateRatedVoltageV2: Entry");

		boolean status = false;
		String resultCellPosition = "";
		try {



			boolean populateRatedVoltage = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportDisplay().isDisplayRatedVoltage();
			if(populateRatedVoltage){
				ApplicationLauncher.logger.debug("meterProfileReportUpdateRatedVoltageV2: populateRatedVoltage enabled");	

				resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportCellPosition().getRatedVoltageCell();
				//ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: populateRoutineTestPropertOfEnabled enabled");	
				String ratedVoltageData = modeldata.getString("rated_voltage_vd");
				try {
					// Convert to float first (handles both int and float cases)
					float floatValue = Float.parseFloat(ratedVoltageData);

					// Convert to int (rounding down)
					int intValue = Math.round(floatValue); // Use (int) floatValue for truncation

					// Convert back to string
					ratedVoltageData = String.valueOf(intValue);
				} catch (NumberFormatException e) {
					ApplicationLauncher.logger.error("meterProfileReportUpdateRatedVoltageV2 : Exception :Invalid number format: " + ratedVoltageData);
				}
				ApplicationLauncher.logger.debug("meterProfileReportUpdateRatedVoltageV2: RatedVoltage : " + ratedVoltageData);
				boolean prefixEnabled = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportDisplay().isDisplayPrefixRatedVoltage();
				ApplicationLauncher.logger.debug("meterProfileReportUpdateRatedVoltageV2: RatedVoltage prefixEnabled: " + prefixEnabled);
				if(prefixEnabled) {
					String prefixData = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportPreAndPostFixValue().getPrefixRatedVoltageValue();
					ApplicationLauncher.logger.debug("meterProfileReportUpdateRatedVoltageV2: RatedVoltage prefixData: " + prefixData);

					ratedVoltageData = prefixData + ratedVoltageData;
				}

				boolean postfixEnabled = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportDisplay().isDisplayPostfixRatedVoltage();
				ApplicationLauncher.logger.debug("meterProfileReportUpdateRatedVoltageV2: RatedVoltage prefixEnabled: " + prefixEnabled);
				if(postfixEnabled) {
					String postfixData = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportPreAndPostFixValue().getPostfixRatedVoltageValue();
					ApplicationLauncher.logger.debug("meterProfileReportUpdateRatedVoltageV2: RatedVoltage postfixData: " + postfixData);

					ratedVoltageData =  ratedVoltageData + postfixData;
				}

				status = FillReportDataColumnXSSF(sheet1, ratedVoltageData,resultCellPosition);

			}else{
				status = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("meterProfileReportUpdateRatedVoltageV2: Exception2: " + e.getMessage());
		}
		return status;
	}

	public boolean meterProfileReportUpdateMeterSerialNo(XSSFSheet sheet1){
		ApplicationLauncher.logger.debug("meterProfileReportUpdateMeterSerialNo: Entry");

		boolean status = false;
		try {
			boolean populateMeterSerialNo = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReportDisplay().getDisplayMeterSerialNo();
			if(populateMeterSerialNo){
				String meterSerialNoCell = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReport().getMeterSerialNoCell();

				try {
					String meterSerialNoData = "";//ConstantConfig.METER_PROFILE_REPORT_PAGE_NUMBER;//modeldata.getString("ptr_ratio");
					status = FillReportDataColumnXSSF(sheet1, meterSerialNoData,meterSerialNoCell);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("meterProfileReportUpdateMeterSerialNo: meterSerialNoData: Exception: " + e.getMessage());
				}

			}else{
				status = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("meterProfileReportUpdateMeterSerialNo:  Exception2: " + e.getMessage());
		}
		return status;
	}
	
	public boolean meterProfileReportUpdateBasicCurrentV2(XSSFSheet sheet1, JSONObject modeldata){
		ApplicationLauncher.logger.debug("meterProfileReportUpdateBasicCurrentV2: Entry");

		boolean status = false;
		String resultCellPosition = "";
		String resultData = "";
		try {

			boolean populateBasicCurrent = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportDisplay().isDisplayBasicCurrent();
			if(populateBasicCurrent){
				ApplicationLauncher.logger.debug("meterProfileReportUpdateBasicCurrentV2: populateBasicCurrent enabled");	

				resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportCellPosition().getBasicCurrentCell();
				//ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: populateRoutineTestPropertOfEnabled enabled");	
				String basicCurrentData = modeldata.getString("basic_current_ib");
				ApplicationLauncher.logger.debug("meterProfileReportUpdateBasicCurrentV2: BasicCurrent1 : " + basicCurrentData);
				try {
					// Convert to float first (handles both int and float cases)
					float floatValue = Float.parseFloat(basicCurrentData);

					// Convert to int (rounding down)
					int intValue = Math.round(floatValue); // Use (int) floatValue for truncation

					// Convert back to string
					basicCurrentData = String.valueOf(intValue);
				} catch (NumberFormatException e) {
					ApplicationLauncher.logger.error("meterProfileReportUpdateBasicCurrentV2 : Exception :Invalid number format: " + basicCurrentData);
				}
				ApplicationLauncher.logger.debug("meterProfileReportUpdateBasicCurrentV2: BasicCurrent2 : " + basicCurrentData);
				boolean prefixEnabled = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportDisplay().isDisplayPrefixBasicCurrent();
				ApplicationLauncher.logger.debug("meterProfileReportUpdateBasicCurrentV2: BasicCurrent prefixEnabled: " + prefixEnabled);
				if(prefixEnabled) {
					String prefixData = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportPreAndPostFixValue().getPrefixBasicCurrentValue();
					ApplicationLauncher.logger.debug("meterProfileReportUpdateBasicCurrentV2: BasicCurrent prefixData: " + prefixData);

					basicCurrentData = prefixData + basicCurrentData;
				}

				boolean postfixEnabled = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportDisplay().isDisplayPostfixBasicCurrent();
				ApplicationLauncher.logger.debug("meterProfileReportUpdateBasicCurrentV2: BasicCurrent prefixEnabled: " + prefixEnabled);
				if(postfixEnabled) {
					String postfixData = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportPreAndPostFixValue().getPostfixBasicCurrentValue();
					ApplicationLauncher.logger.debug("meterProfileReportUpdateBasicCurrentV2: BasicCurrent postfixData: " + postfixData);

					basicCurrentData =  basicCurrentData + postfixData;
				}

				status = FillReportDataColumnXSSF(sheet1, basicCurrentData,resultCellPosition);

			}

			else{
				status = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("meterProfileReportUpdateBasicCurrentV2: Exception2: " + e.getMessage());
		}
		return status;
	}
	

	public boolean meterProfileReportUpdateNoOfImpulsesV2(XSSFSheet sheet1, JSONObject modeldata){
		ApplicationLauncher.logger.debug("meterProfileReportUpdateNoOfImpulsesV2: Entry");

		boolean status = false;
		try{


			boolean populateNoOfImpulsesPerUnit = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportDisplay().isDisplayNoOfImpulsesPerUnit();
			if(populateNoOfImpulsesPerUnit){
				ApplicationLauncher.logger.debug("meterProfileReportUpdateNoOfImpulsesPerUnitV2: populateNoOfImpulsesPerUnit enabled");	

				String resultCellPosition = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportCellPosition().getNoOfImpulsesPerUnitCell();
				//ApplicationLauncher.logger.debug("populateMeterIndividualDetailedReportMetaData: populateRoutineTestPropertOfEnabled enabled");	
				String noOfImpulsesPerUnitData = modeldata.getString("impulses_per_unit");

				ApplicationLauncher.logger.debug("meterProfileReportUpdateNoOfImpulsesPerUnitV2: NoOfImpulsesPerUnit : " + noOfImpulsesPerUnitData);
				boolean prefixEnabled = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportDisplay().isDisplayPrefixNoOfImpulsesPerUnit();
				ApplicationLauncher.logger.debug("meterProfileReportUpdateNoOfImpulsesPerUnitV2: NoOfImpulsesPerUnit prefixEnabled: " + prefixEnabled);
				if(prefixEnabled) {
					String prefixData = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportPreAndPostFixValue().getPrefixNoOfImpulsesPerUnitValue();
					ApplicationLauncher.logger.debug("meterProfileReportUpdateNoOfImpulsesPerUnitV2: NoOfImpulsesPerUnit prefixData: " + prefixData);

					noOfImpulsesPerUnitData = prefixData + noOfImpulsesPerUnitData;
				}

				boolean postfixEnabled = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportDisplay().isDisplayPostfixNoOfImpulsesPerUnit();
				ApplicationLauncher.logger.debug("meterProfileReportUpdateNoOfImpulsesPerUnitV2: NoOfImpulsesPerUnit prefixEnabled: " + prefixEnabled);
				if(postfixEnabled) {
					String postfixData = DeviceDataManagerController.getConveyorConfigParsedKey().getMeterProfileReportPreAndPostFixValue().getPostfixNoOfImpulsesPerUnitValue();
					ApplicationLauncher.logger.debug("meterProfileReportUpdateNoOfImpulsesPerUnitV2: NoOfImpulsesPerUnit postfixData: " + postfixData);

					noOfImpulsesPerUnitData =  noOfImpulsesPerUnitData + postfixData;
				}

				status = FillReportDataColumnXSSF(sheet1, noOfImpulsesPerUnitData,resultCellPosition);

			}else{
				status = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("meterProfileReportUpdateNoOfImpulsesV2:  Exception2: " + e.getMessage());
		}

		return status;


	}

}
