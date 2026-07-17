package com.tasnetwork.calibration.energymeter;


//pending task//


// 22-May-2024
// constantTestMaxErrorAllowedHashMap, repeatAverageMaxErrorAllowedHashMap  not updating correctly during report generation when tested with  
// lecs V4.2.1.2 (selfHeatAverageMaxErrorAllowedHashMap need to check)
/*
// consolidate all report in to single workbook or pdf
Reference feed back control in App - pending
firmware - password validation-remove -pending
voltage - warmup - 120% shooting only 50volt instead of 288- firmware issue

// pending 
 * version 4.1.0.0 Constant test on Export mode failed for Kiggs customer 
 * All type of testing
 * tab key event on table function pending please refer getNextColumn on RefStdConstController.java
 * single and three reactive mode Test
 * ICT control Hardware design and testing
 * ICT control Software design and testing
 * Phase angle adjustment with board clock
 * sands Active/ Reactive mode setting
 * sand over voltage - over current protection
 * target value display in execute screen
 * Control LSCS power source with RMS feedback value
 * limit ict with pair during deployment
 */


/*pending for ReportGenv2

No Load Test Period in minutes Seconds - header only
No Load - Initial Final Reading - each dut
STA Test Period in minutes Seconds - header only
STA - Duration for one pulse in seconds- each dut
STA - Initial , Final reading- each dut
Self Heat, warmup Time, Freq Interval Time in minutes - header only
Voltage Variation - Actual voltage for the percentage - header only
Harmonic Content Voltage Circuit - header only
HArmonic Content Current Circuit - header only

 */

/*
 * 
 * New Test case
Current Unbalanced
// 
 */


/* pending issues with Bofa source
 * serial port init validation on setting and execution
 * Harmonics need to be validated
 * Dial test to be tested with accurate stop
 * Data_LduBofa.sendReadRefMeterPulseNumCmd(1); // dial test works with only first meter
 * regression testing 
 * 
 */


//Version ProCon-Maven-s0.9.2.7  23-Apr-2026 - By Gopi and Surya
// added config for CalibSuperCapacitorChargeWaitTimeInSec in conveyor_config_devsys_v1_1.json
// fixed issue on STA2 stop
// fixed issue on STA2 - gui - dashboard mulitple pallet refresh for verific, STA1 and STA2
// when there is batch faileure in verific - pallet got release even after selecting "No". Fixed the issue for that
//Version ProCon-Maven-s0.9.2.4  16-Mar-2026 - By Gopi
// issue: some time BAy2 is not working properly
// updated states file name of STA1 and STA2 with Bay1 and Bay2 to differentiate the state
//Version ProCon-Maven-s0.9.2.2  08-Mar-2026 - By Gopi
// fixed few exception issues on the procon
// fixed issues on ProCAL for verification results fetch
// tested with ProCAL-Maven-4.2.1.8.5.6
//Version ProCon-Maven-s0.9.1.9  05-Jan-2026 - By Gopi
// tested the logic to update the HardwareId number instead of serial number and working good. In case default "YZ" observed on the MeterResult execution fails
//Version ProCon-Maven-s0.9.1.8  20-Jan-2026 - By Gopi
// new version for the CR implementation
// in the functional Bay - skip the Qr scanner and 
// in the optical mode write the unique serial number generated(HWID) by the ProCon
// This serial number(HWID) is used only for internal purpose and it can be read only optical serial port and 
// R&D team not planning to display this serial number(HWID) on their meter LCD
// when the meter are passed ,Production team assigns another serial number which is different from HWID
// and that will be Laser printed on the meter Top
// Production team have will external Mapping on their server for the Internal testing serial number (HWID) with Production printed serial number
// for the failed meters, new serial number(HWID) can be updated during retest or use the existing serial Number(this is not mandatory)
//Version ProCon-Maven-s0.9.1.6  15-Sep-2025 - By Gopi
// updated logic on startDutIndividualDeviceExecutionAsync and DutCmdIndividualDeviceExecutor
// to support progressbar, countUpTimer and CountDown Timer
//Version ProCon-Maven-s0.9.1.5  15-Sep-2025 - By Gopi
// added logic startDutIndividualDeviceExecutionAsync  with DutCmdIndividualDeviceExecutor in common , working good for multiple devices concurrently
// when FtDutCmdEachTpSeqExecuteMode flag set to false
// this will execute the each dut seperately with test point independantly 
// Version ProCon-Maven-s0.9.1.4  15-Sep-2025 - By Gopi
// startDutTestExecutionAsync  with testPoint in common , working good for multiple devices concurrently
// Version ProCon-Maven-s0.9.1.3  03-Sep-2025 - By Gopi
// added logic to load the test point on different independant execution screen for DUT
// added logic to support multiple serial port for the same position by adding targetDeviceType in DutCommand Gui
// this is required in the same dut project both optical comm and qrScanner comm required for each meter 
// tested and working good with only serial port, not with actual device
// added logic for saveas project for DutCommand Profile - SaveAsAllProjectComponents
// Version ProCon-Maven-s0.9.1.2  03-Sep-2025 - By Gopi
// updated DutCommand testing with jSerialComm with ProcalFeatureEnable.DUT_CMD_EXECUTE_MODE_V3
// tested with 2 serial port. working in parallel
//Version ProCon-Maven-s0.9.0.8  20-Aug-2025 - By Gopi
//updated icon and splash images
// replaced ConstantConveyor.VERIFICATION_BAY_PALLETS_CLEARED with ConveyorDataManager.isVerific1PalletsAllCleared()
//added logic for startProgressBarWithTime/stopProgressBarWithTime for sta1 and sta2
//added logic for startProgressBarWithTpCount/stopProgressBarWithTpCount for verific1 with dependency on updateTpCountStatus
// added logic for updateTpCountStatus to update testPointCount for verific1,sta1 and sta2
// added logic for startTimeUpDisplay on dashboard FT,HV,IR,Calib, verific1, Sta1 and Sta2 bays
// updated logic to display pass/fail percentage in display metrics
//added logic for NoEntry for HV, IR, Calib and verific1Waiting bays at OpenStopLatch. Need to be tested
// added logic for Halt-Pallet for FT,HV, IR, Calib and verific1Waiting bays at check pallet and OpenStopLatch. Need to be tested
// added logic markAsCompleteForPreviousBayPallet for FT,HV, IR and Calib bays. Need to be tested
// added testStatusDisplaySemaphore to fix the exception
// updated logic for <yesterday> display on metrics
//Version ProCon-Maven-s0.9.0.6  11-Aug-2025 - By Gopi
// added logic for NoEntry right click menu functionality for FT bay - once tested need to implement for other bays
// added addFingertipSubmenu grouping for all Bays
// added logic to display metrics based on date_h from summary metrics table
// added <last 7 days> display on the metrics display 
// added logic to refresh pallet display on dashboard for rejection and unloading. it displays todays top - non-active pallet
//Version ProCon-Maven-s0.9.0.4  02-Aug-2025 - By Gopi
// changed to stability version
// migrated to Maven project from ProCon d9.0.2
// in the orm Model changed the DeviceSetting field cName to canName due maven migration
// in the database DeviceSetting field c_name to can_name, this need to be taken care in the prod
// Since there is no EasyModbus maven, include the jar file using below command, please update the file path for -Dfile as per present location
// Run Configuration -> Maven Build -> new Configuration
//Base Directory = > ${project_loc:ProCon-Maven-s0.9.0.4}
//Goals => install:install-file -Dfile=C:\deleteMe1\config\EasyModbusJava.jar  -DgroupId=de.re.easymodbus   -DartifactId=easymodbus-java  -Dversion=1.0   -Dpackaging=jar
//Click Run button
//Version ProCon d9.0.2  31-July-2025 - By Gopi
//optimised logic metrics update, dashboard update, monitor display update for unloading & rejection 
//added reportUtils.processPalletMeterIndividualResult in unloading
//Version ProCon d9.0.1  29-July-2025 - By Gopi
//defect fixed to update database for permissible limits in Verific test result log
// in verific added logic to validate missed test points in the results with ReportPrintDetailedTestPointWhiteList in config
//Version ProCon d8.9.9  25-July-2025 - By Gopi
// updated the logic on the qr scanner for updating customer metrics, even if there is no pallet present in the bay
// added logic for procal dutserialNo mapping result processing in verification bay
// added ConstantConveyor.LDU_PLACEMENT_ORDER for ldu mapping 
// added setStopProcessRequestedStaNldBay1(false),setStopProcessRequestedStaNldBay2(false) in check for pallet in respective bays
//Version ProCon d8.9.7   24-July-2025 - By Gopi
//updated logic on verific, awaiting for STA1 and STA2 all clear flag with timeout
// updated logic for metrics with summary to process all the records of metrics with specific date
//Version ProCon d8.9.4   24-July-2025 - By Gopi
//updated result status logic for Unloading and rejection bay in BayUtils.updateConveyorMetrics (which was missed in merging)
//Version ProCon d8.9.3   23-July-2025 - By Gopi
// in Gopi pc tested with unified_meter_status_display_v2_5.py. Need to test with Prod
// in BayUtils added inputIoPortInfoMap , outputIoPortInfoMap for optimsation
//updated rejection bay logic similar to unloading
//Version ProCon d8.9.2   22-July-2025 - By Gopi
//procal interface working good with ProCAL-Maven-s4.2.1.8.0.7
// added logic to set default calibration command in FT with config FtSetDutToDefaultCalibrationTest
// updated logic for batch failure prompt and meter release with user confirmation
//Version ProCon d8.9.1   22-July-2025 - By Gopi and Surya
// added new logic to display unloading bay pallets with unloadingBayPalletDistinctIdList - yet to be tested
// once tested need to add the logic for rejection bay also with rejectionBayPalletDistinctIdList
// updated logic for Metric with common function in BayUitls.updateSummaryMetrics - Surya
// Fixed issues for ByPass mode- yet to be tested - Surya
//Version ProCon d8.8.5   21-July-2025 - By Gopi
// added archiveExistingResultInDb in Verific. Yet to be tested - bocz old result batch failed still exist, which is causing the new execution computeOverAll result - failed ( Batch Failed)
// added addResultToPalletMeterV1_1 to eliminate duplicate records on Noload in Sta1 , S043_Get_Test_Point_Status - need to be tested
//Version ProCon d8.8.3   20-July-2025 - By Gopi
// updated logic on computeMeterOverAllStatus to include the batch failed scenario
// added invoking processOverallCalibration in  S059_09_Stop_Execution
//Version ProCon d8.7.9   18-July-2025 - By Gopi
// added the logic to send palletDistinct Id with serialNo to ProcalVerific. Not tested
//updated the logic for metricSummary - by Surya
//Version ProCon d8.7.7   17-July-2025 - By Surya
// added logic for metricSummary
// fixed issue RejectionBayStop - button enable
//Version ProCon d8.7.6   17-July-2025 - By Gopinath
//in LauncherPaneController unloading bay user screen number entry - working good in devsys prod
// in LauncherPaneController added logic for rejection bay to get the user screen number,  - working good in prod
//Version ProCon d8.7.5   17-July-2025 - By Gopinath
// in LauncherPaneController added logic for unloading bay to get the user screen number, 
// if entered -1 (minus one), app will load the python script to load with Pid and monitor serial number
// for other than minus one value, it will load the python scrip to load with screen number 
//Version ProCon d8.7.2   16-July-2025 - By Gopinath
// added refresh menu for FT,HV,IR for dashboard gui
//Version ProCon d8.7.0   15-July-2025 - By Gopinath
//added ftRelayOnPhaseCurrentMinAccepted and ftRelayOnNeutralCurrentMinAccepted in the config
// updated MySqlServiceManager.getPalletMeterResultsService().save(palletMeterResult) in PalletTrackerController
//Version ProCon d8.6.8   14-July-2025 - By Gopinath
// on verification, for sta1 and sta2 monitoring added stable detection for no pallet and wait till isSta1PalletsAllCleared,isSta2PalletsAllCleared
// S08_check_for_pallets_at_SCT_NLT_Bay1 and S12_check_for_pallets_at_SCT_NLT_Bay2
// renamed addMeterResultSummary -> addMeterResultSummaryWithPalletDetails, due to duplicate and to avoid confusion 
//Version ProCon d8.6.7   13-July-2025 - By Gopinath
// in FT moved <Check pulses in meter> to the last 
// also in FT ReadNeutralCurrent commented bayResponse.getCurrentValue() > 0 condition // per below
//if (Math.floor(bayResponse.getCurrentValue()) == 0.0f ){//|| bayResponse.getCurrentValue() > 0) { // commented on version d.8.6.7  by Gopi 13-jul-2025
//Version ProCon d8.6.6   13-July-2025 - By Gopinath
// added BayIndicatorManager for optimisation
//Version ProCon d8.6.5   12-July-2025 - By Gopinath
//unloading bay working good
//Version ProCon d8.6.4   12-July-2025 - By Gopinath
// added delay on the unloading bay for qr scanning process - UnloadingQrScanningWaitTime_InSec
// added logic to seperate log for PLC client
// added logic bayUtils.computeMeterOverAllStatus(myPalletManage); . Yet to be tested
//Version ProCon d8.6.3   11-July-2025 - By Gopinath
// Added logic for unloading bay and it is working. Pallet removal to Next pallet placement time required minimum 3 sec 
//Version ProCon d8.6.1   10-July-2025 - By Gopinath
//updated logic for Unloading bay. Yet to be tested
//Version ProCon d8.6.0   09-July-2025 - By Gopinath
// added sendDefaultCalibrationCommand in Functional testing
// added logic to display pallets in sta1 and sta2
// added logic to sent rest api hit through restClient to external rejection display  
//Version ProCon d8.5.2   04-June-2025 - By Surya
// integrated metrics
//Version ProCon d8.4.9    02-June-2025 - By Gopinath
// updated Dashboard Gui with bay viewer for all containers
//Version ProCon d8.4.7    02-June-2025 - By Gopinath
// added bay viewer on dash board for FT bay only. Yet to test in prod
//Version ProCon d8.4.4    02-June-2025 - By Gopinath
// fixed issue for transit from Waiting Bay to Verification smoothly. Once all pallets are released then Waiting bay will release the pallets
// fixed issue on switchBatchToNextBay for the verific bay movement
// in switchBatchToNextBay - need to handle getPresentPalletAtBayMap for multiple Pallets 
//Version ProCon d8.4.2    30-June-2025 - By Gopinath
// updated palletView Gui with event display DashboardPalletView_v1_4_W.fxml
// added logic for stopper open/ close/ pallet monitoring blinking for FT bay alone
//Version ProCon d8.3.7    17-June-2025 - By Gopinath
//deleted CommunicationTestBay and renamed CommunicationTestBay2 to CommunicationTestBay
//fixed the defect for PLC - power reset failure- on ModbusTcpClient added logic on exception
// added PostDelaySta1ExitOpenStopLatch1_InSec,PostDelaySta1ExitCloseStopLatch1_InSec,PostDelaySta1ExitOpenStopLatch2_InSec,
// PostDelaySta1ExitCloseStopLatch2_InSec in conveyor_config_devsys_v1_1.json file
// in S19_release_pallets_from_STA_NLDT_Bay1 added plc failure error code in all else cases
//Version ProCon d8.3.6    16-June-2025 - By Gopinath
// S19_release_pallets_from_STA_NLDT_Bay1 fixed defects and tested - working good
//Version ProCon d8.3.5    16-June-2025 - By Gopinath
// added S19_release_pallets_from_STA_NLDT_Bay1 to release all pallet in one state.yet to be tested
// deleted CalibrationBay and renamed CalibrationBay2 to CalibrationBay
// deleted WaitingBay and renamed WaitingBay2 to WaitingBay
// deleted VerificationTestBay and renamed VerificationTestBay2 to VerificationTestBay
// deleted UnloadingBay and renamed UnloadingBay2 to UnloadingBay
// deleted STA_NoLoadTestBay2 and renamed STA_NoLoadTestBay2_2 to STA_NoLoadTestBay2
// deleted STA_NoLoadTestBay1 and renamed STA_NoLoadTestBay1_2 to STA_NoLoadTestBay1
// deleted RejectionBay and renamed RejectionBay2 to RejectionBay
// deleted LoadingBay and renamed LoadingBay2 to LoadingBay
// deleted HighVoltageTestBay and renamed HighVoltageTestBay2 to HighVoltageTestBay
// renamed bay packages to reduce the log file size
//Version ProCon d8.3.4    13-June-2025 - By Gopinath
//fixed defect for Verification exit to STANLD2
// for RestClient: getBayData: TARGET_URL: http://192.168.1.125:502/api/device/3/bay/12/get debug issue added print <Entry-failed-debug> to trace
//Version ProCon d8.3.1    11-June-2025 - By Gopinath & surya
// rejection bay working with out qr scanner with flag rejectionQrNotAvailable
// unloding implemented
//Version ProCon d8.3.0    09-June-2025 - By Gopinath
//this need to be revoked in prod ---- workaround added delay for the S03_close_the_fingerTip_Latch - IR finger - #Gopi-09-06-2025 
//Version ProCon d8.2.5    05-June-2025 - By Gopinath and surya 
//bug fixes done for miltiple issues 
//Version ProCon d8.2.4 04-June-2025 - By Gopinath
// added dutCommand gui feature
// Version ProCon d8.1.8 24-May-2025 - By Surya
/**
 * Dashboard Updates:
 * Added real-time meter status updates (TESTING, PASSED, FAILED) using ConveyorDeviceDataManagerController.getDashboardObject().updatePalletMeterStatusByBayAndPosition.
 * Integrated GUI updates via StateExecutorController.updateTestStatusGui for test progress (INP, COMPLETED) and results (Success, Failed).
 * Ensured per-meter feedback for up to six meters per bay, enhancing operator visibility.
 * Code Cleanup:
 * Removed commented-out code, unused constants, and redundant logging.
 * Fixed logging inconsistencies (e.g., corrected logger usage in S059_08_Neutral_Calibration).
 * Consolidated duplicate logic in calibration classes for result processing.
 * Documentation:
 * Added Javadoc comments for all methods to describe their purpose.
 * Included inline comments to clarify logic and highlight dashboard updates.
 * Error Handling:
 * Standardized error codes using ErrorCode.ERR_601 (success) and ERR_000 (default failure).
 * Maintained original error logic while aligning with dashboard requirements.
 * Specific Improvements:
 * S08_functional_Test: Simplified result processing and added per-meter dashboard updates.
 * S05_high_Voltage_Test: Enhanced with GUI updates for voltage application steps.
 * S05_insulationResistance_Test: Improved validation logic and removed redundant loops.
 * S059_03_Phase_Calibration: Streamlined current stability wait logic with dashboard integration.
 * S059_08_Neutral_Calibration: Added optical reader removal prompts with dashboard updates.
 */
// added logic for S070_opticalProbe_Scanning_of_Meters for meter serial number reading and adding to database
//Version ProCon d8.1.4 Date 02-May-2025 - By Gopinath
//Added logic for executable on all config loader
//Version ProCon d8.1.3 Date 01-May-2025 - By Gopinath
// working good with ProCAL-Maven-s4.2.1.4.2.8
//Version ProCon d8.1.1 Date 29-Apr-2025 - By Gopinath
// update logic report generation. Added select All, Unselect All and fixed issues on report data when generating multiple files , sorting loe result, update permissible limits
//Version ProCon d8.0.7 Date 25-Apr-2025 - By Gopinath
//fixed issue for dut serial no empty. removed invoking of addMetersToPallet on functionalTestProcess(int positionNum)
// added invoking of addMetersToPallet at dutQrScanSerialNoProcess(int positionNum){
// fixed issue - dut serial no update on procal verific is incorrect- updated logic findByPresentBayKeyAndPalletActiveOrderByPalletBatchNoAsc - need to test
// Version ProCon d8.0.2 Date 19-Apr-2025 - By Gopinath
// Refresh of pallet manage happens for recent number of days 
// app_config -> PalletManageRecentNoOfDaysDisplay - '0' displayes all
// Version ProCon d8.0.1 Date 18-Apr-2025 - By Surya
// Added motor control logic to communicate to PLC
// Configuration of which motor required for which bays = Bay -> Constant_Motor_Requirement
// Based on motor requirement - automatically generate word - write to plc coil 
// Bay Utils set_motor_not_required and set_motor_required
// Tested communication with PLC
// Version ProCon d8.0.0.0 Date 15-Apr-2025 - by Gopinath
// Fixed On - Off Constant Confusion
// Works as intended
//version ProCon d0.6.0.7 Date 06-Apr-2025 - by Gopinath
// added logic in ft to read meter serial number through qr scanner and write to meter memory through optical serial port
// tested and working good in parallel mode in functional test
// version ProCon d0.6.0.5 Date 02-Apr-2025 - by Gopinath
//integrated files from version ProCon-d0.6.0.5 for report generation in new format delivered on 29-Mar-2025�-�image�format
//version ProCon d0.6.0.2 Date 30-Mar-2025 - by Gopinath
//updated logic to support multiple plc and messagequeue approach 
//version ProCon d0.6.0.1 Date 29-Mar-2025 - by Gopinath
// //#PLC_MIGRATION_WORKAROUND
//version ProCon d0.6.0.0 Date 28-Mar-2025 - by Gopinath
// implementing the logic for Schnieder PLC - TM200CE40R
//version ProCon d0.5.3.6 Date 27-Mar-2025 - by Surya
//version ProCon d0.5.3.5 Date 21-Mar-2025 - by Gopinath
// update Result screen to generate individual meter report
// version ProCon d0.5.3.3 Date 02-Mar-2025 - by Surya
// updated semaphore lock for STM messages
// Bay Utils -> bayLocks || ConveyorClientManager -> messageBayData
// Updates on Meter Summary Update - FTB, HVB, IRB, Verific, STANLDT1B, STANLDT2B 
// Added logic to switch 4 pallets from WAITING BAY to VERIFIC BAY -> switchBatchToNextBay (PalletTrackerController)
// Added new function addPalletNextBayState (PalletTrackerController) to create BayState for all the pallets in multiPallet Bays

//version ProCon d0.5.3.1 Date 17-Feb-2025 - by GopinathM
// added RestClient to fix the network traffic error issue found with Asynchttpclient
// version ProCon d0.5.2.0 Date 12-Feb-2025 - by Surya
// added SEMAPHORE logic for STA-NLT Bay 1 and Bay 2.
// version ProCon d0.5.1.8  Date 07-Feb-2025 - by Gopinath M, Pradeep and  Surya
// added logic to execute procal from remote command through Ethernet mode - RestApi protocol
// worked good with ProCal-Maven-S4.2.1.4.0.3
// procon as client and Procal as server with ethernet port 8085
// tested for Procal start, stop and get test point result with alltestcomplted status
// on the conveyor placed only one pallet with Position loaded as 1,2,3 and 22,23,24
// result fteched by procon butlogic yet to develop the results on the proCON gui
// version ProCon d0.5.1.6  Date 03-Feb-2025 - by Gopinath M
// optimised calib power source trigger to work without GUI update, initiateRatedVoltageSetOnPowerSource, initiateRatedCurrentSetOnPowerSource,initiateZeroCurrentSetOnPowerSource, initiateStopRequestOnPowerSource
// version ProCon d0.5.1.2  Date 27-Jan-2025 - by Gopinath M
// merged ProCon d0.5.1.0.a in to ProCon d0.5.1.1
// version ProCon d0.5.0.7  Date 18-Jan-2025 - by Gopinath M
// version ProCon d0.5.0.3  Date 13-Jan-2025 - by Gopinath M
// *************Happy Pongal Karthik Maama********************
// pongal - remembering child hood sweet memories with Maama Karthik....will watch movies at Golden Eagle theatre by walk from Padi to nerkundram
// lost him during Covid 19...Miss You Maama - Rest In Peace
// added QrCodeScanningPallet book shelf and tested with FT,HV,IR and Calib bay
// version ProCon d0.5.0.2  Date 13-Jan-2025 - by Gopinath M
//***************Happy Bogi**************************
// seperated bayTestChild, stateExecutorChild from ConveyorDebug screen
// added bookshelf class CheckForPalletAtBay class and updated for FT,HV,IR and Calib Bay
// Updated createFtBayStateInstance to identify the class object by class name - removed switch statement
// Added logic for stopProcessRequestedFtBay and stopProcessCompletedFtBay, update the gui respectively
//version ProCon d0.5.0.1  Date 12-Jan-2025 - by Gopinath M
// deleted the duplicate files
//version ProCon d0.5.0  Date 12-Jan-2025 - by Gopinath M
// merged ProCon d0.4.2 and ProCAL s4.2.1.4
// derived from ProCAL s4.2.1.4 version for merging with ProCon d0.4.2
//version 4.2.1.4 Date 07-Dec-2024 - by Gopinath M
// for elmeasure customer build executables
//version 4.2.1.3.0.7 Date 07-Dec-2024 - by Gopinath M
// automation calibration process in progress with ProcalFeatureEnable.AUTO_CALIBRATION_MODE_ENABLE_FEATURE
//version 4.2.1.3.0.6 Date 07-Dec-2024 - by Gopinath M
// add logic to generate and update spread sheet for calibration verification using the flag METRICS_EXCEL_LOG_ENABLE_FEATURE
// version 4.2.1.3.0.5 Date 05-Dec-2024 - by Gopinath M
// updated logic to log the refstd data to log file using ProcalFeatureEnable.METRICS_EXCEL_LOG_ENABLE_FEATURE, to measure the source performance
// version 4.2.1.3.0.4 Date 09-Sep-2024 - by Gopinath M
// added logic to generate the report in pdf format
// fixed the python argument with whitespace on the report path
// version 4.2.1.3.0.3 Date 31-Aug-2024 - by Gopinath M
// in Fuji-bofa - LDU error after 8th Position i.e from 9th position not working- 
// issue fixed on parseErrorsResponse, parseDialPulsesResponse and parseStartingCurrentTestPulseResponse - working good 
// for dial test bofa bench always first meter should be used as reference to read energy from reference std pulse 
// for dial test with out first meter logic should be update here Data_LduBofa.sendReadRefMeterPulseNumCmd(1);
// tested at Fuji site
// version 4.2.1.3.0.1 Date 03-Aug-2024 - by Gopinath M
// updated logic to display execution station on live table with LIVE_TABLE_EXECUTION_STATUS_DISPLAY on Bofa mode
// Tested for LOE, STA, NOLOAD and Constant test working good both in Step run mode and full run mode on Bofa mode
// Dial test Energy watt - instant metrics display updated from kWh to wh with config flag INSTANT_METRICS_ENERGY_DISPLAY_IN_KWH
// version 4.2.1.3 Date 01-Aug-2024 - by Gopinath M
// milestone version created for installer generation
// version 4.2.1.2.1.4 Date 01-Aug-2024 - by Gopinath M
// installer need to be generated with this for Fuji-japan portable single phase
// tested for Fuji-japan portable single phase static source. Worked  good with 
// Fuji_portable_1phase-MASTER_V0_6_5 and 
// Fuji_portable_1phase-SLAVE_v1_4
// version 4.2.1.2.1.3a Date 24-Jul-2024 - by Gopinath M
// for ProcalFeatureEnable.PROPOWER_SRC_ONLY, on clicking LoadNext button on the third test point halted
// fixed on CustomRatingExecuteStart with version #s4.2.1.2.1.4
// version 4.2.1.2.1.3a Date 24-Jul-2024 - by Gopinath M
// updated execution status on the live table on Test Run screen. Updated db and read db and updated status
// tested with LOE working good, yet to test with STA, NoLoad and Constsnt test
// version 4.2.1.2.1.0 Date 20-Jul-2024 - by Gopinath M
// fixed the serial port semaphore issue for Bofa - (Fuji customer)
// version 4.2.1.2.0.9 Date 18-Jul-2024 - by Gopinath M
// tested Noload , STA and Constant test on jserialcom, and working good
// version 4.2.1.2.0.7 Date 18-Jul-2024 - by Gopinath M
// applied jserialcom serial interface for Bofa communication
// tested LOE only. yet to fix jserialcom with STA, NOLOAD and Constant Test
// version 4.2.1.2.0.6 Date 16-Jul-2024 - by Gopinath M
// fixed WFR issue for LOE testing for Bofa
// fixed Creep result status display for Bofa
// version 4.2.1.2.0.5  Date 06-Jul-2024 - by Gopinath M and Pradeep
// tested at Fuji - Chennai with bofa source
// harmonics values outputs are in range for voolateg and current, tested with 3rd harmonics only
// version 4.2.1.2.0.1 Date 28-May-2024 - by Gopinath M
// refStdFeedBackVoltControlV2 replaced with latest version 
// added logic for not to allow special characters except hypen and underscore on the project name. which is creating problem in report generation
//version 4.2.1.2 Date 21-May-2024 - by Gopinath M
// deployed to lecs on 22-May-2024
// added logic to read and upper and lower limit from config file 
// for constant test with ProcalFeatureEnable.CONSTANT_TEST_RESULT_LIMIT_FROM_CONFIG_ENABLED
// workaround for LECS test bench
//version 4.2.1.1 Date 15-May-2024 - by Gopinath M
// build for LECS dec 2022 for optimisation fix on firmware and java
// firmware version MASTER_HYP5-V0_6_2d1 - (issue observed V1 fine tune not working need to fix)
// reverted changes on refStdFeedBackVoltControlV2 with version 4.2.0.1
// added below param on the config for dail test 
//  "DialTest" : {"1P_NearingTarget1Percent": 90.0,"1P_NearingTarget1CurrentReductionPercentage" : 10.0,"1P_NearingTarget2Percent": 98.0,
//  "1P_NearingTarget2CurrentReductionPercentage" : 5.0,"3P_NearingTarget1Percent": 60.0,"3P_NearingTarget1CurrentReductionPercentage" : 10.0,
//  "3P_NearingTarget2Percent": 90.0,"3P_NearingTarget2CurrentReductionPercentage" : 2.0}
// reverted the changes done for Adya on refStdFeedBackVoltControlV2
// version 4.2.1.0.0.3  Date 13-May-2024 - by Gopinath M
// added logic for inPhase and OutPhase for Harmonics Gui
// for bofa ldu-data was updating on incorrect ldu on ProCAL when 
// stopping the execution-fixed by converting the variable to array and added below to reset the same
// yet to be tested
// Data_LduBofa.resetNthOfErrors();         Data_LduBofa.resetErrorValue();
// Data_LduBofa.resetDialTestPulseCount();  Data_LduBofa.resetStaTestPulseCount();
//version 4.2.1.0.0.1  Date 11-May-2024 - by Gopinath M
// merged 4.2.1.0 with ProCAL-s4.2.0.9.0.1b (Bofa test bench changes from pradeep)
//version 4.2.1.0  Date 06-May-2024 - by Gopinath M
// created installer for Adya Meter Hyderabad customer
//version 4.2.0.9.0.5  Date 26-Apr-2024 - by Gopinath M
// added getLastSetPowerSourceRphaseVoltage logic on lscsThreePhaseBalancedPwrSrcOn to suppress the reduntant commands to Panel
// added runConstTestV3 logic for 3 Phase but not tested
// added logic to diaply Meter serial number on the ReportView Header table on Gui
//version 4.2.0.9.0.3  Date 25-Apr-2024 - by Gopinath M
// worked good with with MASTER_HYP5-V0_6_2c5 and YMPL_PH_CHANGE_SLAVE_v1_3
// ceigLDU_SettingCalculationMethod fixed round off issue // error was showing higher earlier
//version 4.2.0.9.0.1  Date 20-Apr-2024 - by Gopinath M
//added logic to control slave signal generation through master for single phase and three phase
// tested with MASTER_HYP5-V0_6_2c5 and YMPL_PH_CHANGE_SLAVE_v1_3
//version 4.2.0.8.0.9  Date 20-Apr-2024 - by Gopinath M
// added logic for RefStdFeedBackMultiplierEnabled with config
// tested only R phase degree adjustment worked good
// worked good with masterAdyaV6_2c4.hex and slaveAdya_v1_2_singlePhase
//version 4.2.0.8.0.8  Date 20-Apr-2024 - by Gopinath M
// added forceset on lscsSetPowerSourceMctNctMode
// added logic for ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_MULTIPLIER_ENABLED
// SetPowerSourceOffV2 for forceset in shutdown mode
//version 4.2.0.8.0.7  Date 19-Apr-2024 - by Gopinath M
// worked good with MASTER_HYP5-V0_6_2c3
// made optimisation for crash and performance issue
//tested runConstTestV2 working good
// added runConstTestV2 with stepping down current at levels - 90% energy -> Current down to 10%
// 98% energy -> Current Down to 5%
//version 4.2.0.8.0.6  Date 18-Apr-2024 - by Gopinath M
// master masterAdyaV6_2c2.hex
// slaveAdya_v1_2-threePhase-Board2.ehx and slaveAdya_v1_2_singlePhase-Board2.ehx
// optimsed lscsSinglePhasePwrSrcOn for 
// added function resetLastSetPowerSourceData
// added function runRatedCurrentWithPercentage
//constant test 90% after 10% reduced tested ok
//version 4.2.0.8.0.5  Date 18-Apr-2024 - by Gopinath M
// worked good for single phase with 
//version 4.2.0.8.0.4  Date 15-Apr-2024 - by Gopinath M
//updated the harmonics acceptable limit logic for Adya
// ProCalCustomerConfiguration.ADYA_HYBRID_3NO_3PHASE_6NO_1PHASE_POSITION_2024
// ConstantApp.HARMONIC_COMPONENT_MAX = 6 ;//6 = up to 5th Order harmonics
// 2nd,3rd order harmonics 30% accepted
// 4th,5th order harmonics 20% accepted
//version 4.2.0.8.0.3  Date 13-Apr-2024 - by Gopinath M
//Tested with DSP MASTER_HYP5-V0_6_2c and DSP MASTER_HYP5-V0_6_2c-1
// optimised shutwon logic with ProcalFeatureEnable.SHUTDOWN_OPTIMISATION_ENABLED
//version 4.2.0.8.0.2  Date 12-Apr-2024 - by Pradeep R
// updated Data_LduBofa file 
//version 4.2.0.8  Date 11-Apr-2024 - by Gopinath M
// added new major version for conveyor project
//version 4.2.0.7.0.7  Date 10-Apr-2024 - by Gopinath M and pradeep
//this build  worked goodfor conveyor project with STM32 version
//version 4.2.0.7.0.5  Date 07-Apr-2024 - by Gopinath M
// reverted the optimisation done for the exit delay ///reverting the optimisation on S4.2.0.7.0.5 on 08-Apr-2024
// added writeStringMsgToPortV1, initListener on CommunicatorV2 to fix the issue with data to read and write data more than 0x7F
// fixed issue on asciiToHex on GuiUtils, tested with SerialPortManagerPwrSrc_V2 working good
// added hexToAsciiV2 on GuiUtils to convert hex to Ascii more than 0x7F, tested with SerialPortManagerPwrSrc_V2 working good
//version 4.2.0.7.0.2  Date 04-Apr-2024 - by Gopinath M
//ldu data from Bofa power source displayed on the Gui 
//version 4.2.0.7.0.1  Date 04-Apr-2024 - by Gopinath M
//bumped up mysql database schema version to from 2_5_2 to procal_v2_5_3
//auto_deploy_enabled field added in deploy_manage table for the conveyor demo project
//updated sp_procal_add_deploy_manage in database
// fixed the issue with Constant test for Adya
// added kreRefStdAccumulationStartTask on RefStdKreDirector_V2
//version 4.2.0.7  Date 29-Mar-2024 - by Gopinath M
//build the installer for Adya Hyderabad
//version 4.2.0.6.1.0  Date 22-Mar-2024 - by Gopinath M
// added a flag LduInvalidDataSkip in the config file to skip the lscsLDU_ReadErrorData invalid data "+0.000"
//version 4.2.0.6.0.9  Date 21-Mar-2024 - by Gopinath M
// Merged ProCAL-s4.2.0.6.0.4f version for Adya - kre-refStd with new serial port driver changes with
// ProCAL-s4.2.0.6.0.8 version for Bofa source,BofaRefStd,Bofa LDU for Fuji
//guiRefreshData.setFreqDisplayData(String.valueOf(freqDisplayData)); // pending data not available through commnad
///version 4.2.0.6.0.5  Date 02-Feb-2024 - by Gopinath M & Pradeep
// for Bofa power source stopVoltageCurrent() command working,
// phase angle degree and freq-48Hz tested working good
// pending stopCurrent (sendVoltageCurrentStopOutputCommand, sendCurrentStoppingCommand),harmonics Command , 
// pending serial port validation for bofa source
// pending starting current testing, NoLoad testing, Dial Test 
// pending low current 10mA testing 
// pending BOFA RSM integration  
// pending BOFA LDU integration
///version 4.2.0.6.0.4  Date 02-Feb-2024 - by Gopinath M & Pradeep
// integration with Bofa unit - for Power source. Power source setting parameter command tested. Other commands for power source not tested
// integration for Rsm and LDU with Bofa source pending
///version 4.2.0.6 -  Date 23-Jan-2024 - by Gopinath M 
// for nsoft - power source data was sent in VB-App mode instead of hyperterminal mode- fixed
// for nsoft - power source relay was not updated if hyperterminal mode is set- fixed
// for nsoft - refstd measurement reading was not triggered to refresh the data from Radiant - Fixed
// build or installer not generated. Applied the patch on the Nsoft PC (com.jar)
///version 4.2.0.5 -  Date 19-Jan-2024 - by Gopinath M 
// To create the build for nsoft upgrade
// updated try catch logic on function < public List<String> getProjectList() throws JSONException{ >
// After installation at site db connection failed with below error, so added ConstantAppConfig.DB_URL_TAIL_OPTION  in the config app_config_lscs_v1_4.json
//2024-01-20 21:29:07 ERROR ProCAL:103 - ConnectMySQL: Exception:Communications link failure
//The last packet successfully received from the server was 230 milliseconds ago.  The last packet sent successfully to the server was 228 milliseconds ago.
//moved DB_URL to config file ConstantAppConfig.DB_URL = ConstantAppConfigReader.getString("database", "url"); 
//added ConstantAppConfig.DB_URL_TAIL_OPTION in the config file
//08-10-2023 - Gopinath & Pradeep - Master : master-harmonics_V6_5 ; Slave : slave-harmonics-v1_8 ; version 4.2.0.4.01 - Automatic amplitude increment is working 
///version 4.2.0.4.01 - 03-11-2023 - Harmonics generation from java application is working. For multiple zero crossing output - not yet verified 
//version 4.2.0.4  29-Sep-2023      Gopinath
//created build for Sands hybrid 40 position for Sand chennai -location
//version 4.2.0.3.2.3  24-Sep-2023      Gopinath
//updaed lgicto display cusm es repr
// updaed lgic  prmp repr generaed pah
//version 4.2.0.3.2.2  22-Sep-2023      Gopinath
// Demoed to customer but report not generated
//working good with Firmware 0_5_6_1 at Sands chennai site
// disabled the phase degree correction with Reference std value on the LSCS power source. due to changes on the Slave are not made
//version 4.2.0.3.2.1  21-Sep-2023      Gopinath
// working good with Firmware 0_5_6 at Sands chennai site
//version 4.2.0.3.1.7  19-May-2023      Pradeep
//Working good with Procal-s4.2.0.3.1.7 , MASTER_HYP5-V0_6_4 and Harmonics generationV1_3
//in signal generation, but feedback on master board attenuated to zero after slave signal generation
// also there is a latency in Java, Master and slave sync up
//version 4.2.0.3.1.6  29-April-2023      Gopinath M
// added  LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED for the LSCS power source harmonics injection  and control
//version 4.2.0.3.1.5  28-Feb-2023      Gopinath M
// Added content column field in TestFilter HeaderTable in ReportGenV2 to display the header1 to header5 values
// added logic to block addition of Test filter with Empty Test filter name
//version 4.2.0.3.1.4  26-Feb-2023      Gopinath M
//switched to  procal_spring_v1_0_3 for ReportGenV2
// all data populated good in ReportGenV2 framework
// ReportGenv2 good for release with schema procal_v2_5_1 and procal_spring_v1_0_3 
// added getPageNumberPrompt for TestFilter in ReportGenV2
// validate current and voltage range during deployment - done
//version 4.2.0.3.1.3  23-Feb-2023      Gopinath M
// switched to database schema procal_v2_5_1
// added logic to populate values on all field in database procal_v2_5_1.deploy_test_cases using sp_add_deploy_test_cases_v2 except test_sub_type
// commented testCaseInputsTable.setDisable(true) for constant test case in PropertyInfluenceController, emin,emax required in ReportGenV2
//version 4.2.0.3.1.2  23-Feb-2023      Gopinath M
// added gui logic for customer license entry validation with flag ProcalFeatureEnable.LICENSE_FEATURE_DISPLAY_ENABLED
// Optimized printStyle Logic in to single function applySelectedPrintStyle for ReportGenV2
// worked good with procal_spring_v1_0_2 for ReportGenV2
//version 4.2.0.3.1.1  22-Feb-2023      Gopinath M
// scraped version 4.2.0.3.1.0, hence derived from 4.2.0.3.0.9 
// isAllSetParametersInRange in deployment is implemented and validated - results good
//removed below field after optimization
//Table procal_spring_v1_0_2.rp_operation_param_profile
//			field result_type_average
//Table procal_spring_v1_0_2.rp_test_data_filter
//	fields populate_header1, populate_header2, populate_header3
//fields populate_header_actual_voltage,populate_header_actual_current,populate_header_actual_pf
//fields populate_header_actual_freq, populate_header_actual_energy, populate_header_test_period_in_minutes
//	fields populate_header_warmup_period_in_minutes 
/// In ReportGenV2 TestFilter, added Header4,Header5 , checkbox and text field to support additional headers
//version 4.2.0.3.0.9  21-Feb-2023      Gopinath M
//Fixed all minor issues with procal_spring_v1_0_1 on the ReportGenV2 - All data populated good on the report//
//version 4.2.0.3.0.8  20-Feb-2023      Gopinath M
// performed Constant error test calculation even if the rsm ,dut results are not populated in ReportGenV2
// updated TestFilter GUI to view the header and dut selection with Titled with Scroll feature 
//version 4.2.0.3.0.7  20-Feb-2023      Gopinath M
// ReportGenV2 worked good for All test type
//version 4.2.0.3.0.6  20-Feb-2023      Gopinath M
//Constant Error error calculation and result populate done for Constant Test on single TestFilter and update the result on Master as well
//version 4.2.0.3.0.5  19-Feb-2023      Gopinath M
// For Constant Test displayed RSM-initial, RSM-Final,Dut-Initial,Dut-Final,Dut-Diff, Dut-Pulse Count on single Test Filter
// Error Calculation and display pending for Constant Test on single TestFilter
//version 4.2.0.3.0.4  19-Feb-2023      Gopinath M
// added logic to display the result processing exception as Error in the Excel Report for OverAll Status and Color Legends
// added guiPropertySetting for text field to accept only float value MeterReadingPopupController
// worked good with procal_spring_v1_0_1 for ReportGenV2
//version 4.2.0.3.0.3  18-Feb-2023      Gopinath M
// added logic to display the result processing exception as Error in the Excel Report for Page Status
// fixed issues to continue to operationProcess on ReportGenV2 due to incorrect value entered in RSM initial and final value - Issue reports by Kiggs on Version ProCAL 4.2.0.3
// added new field result_type_average on database table procal_spring_v1_1.rp_operation_param_profile
//version 4.2.0.3.0.1  17-Feb-2023      Gopinath M
// Added logic to display Repeat Average and it status using checkBox selection on ReportGenV2
// Pending extraction of upper and lower limit validation for Average Status from deployedtestcases table from database
//version 4.2.0.3  15-Feb-2023      Gopinath M
// build generated for Kiggs-deployment
//version 4.2.0.2.1.8  15-Feb-2023      Gopinath M
// added below check box on the ReportGenV2 GUI and added logic to store / retrieve the data from Database and GUI
// chkBxPopulateRsmInitial, chkBxPopulateRsmFinal, chkBxPopulateDutInitial, 
// chkBxPopulateDutFinal, chkBxPopulateDutDifference, chkBxPopulateDutPulseCount, chkBxPopulateDutCalcErrorPercentage, 
// chkBxPopulateDutOnePulsePeriod, chkBxPopulateDutAverage, chkBxPopulateHeader4, chkBxPopulateHeader5
// added ConstantAppConfig.DEFAULT_LOGIN_ID_POPULATE_ENABLED in the app config file to clear the default username and password on the login screen
//version 4.2.0.2.1.7  13-Feb-2023      Gopinath M
// on ReportGenV2, meter profile table view sort by page no - done
// on ReportGenV2, enable/disable filter in table view for test filter - done
//on ReportGenV2, make user entry non-editable when UPF selected in test filter - done
//on ReportGenV2, meter profile serial no wrong value updated in table view in edit mode - issue fixed 
//on ReportGenV2, highlighted failed status for each page status and overall status - done
// (added ResultOverAllStatusFailColorHighLightEnabled in app config json)
//on ReportGenV2,  added logic for REPLICATE_FEATURE_DISABLED since feature not implemented 
//version 4.2.0.2.1.6  13-Feb-2023      Gopinath M
// database scema with Version Procal_v2_4_1 with sp <sp_add_deploy_devicesV1_1> and tables for report profile
// database added meter_model_no field on deploy_devices table 
// database sp changes on sp_add_deploy_devices - > added meterModelNo -> sp_add_deploy_devicesV1_1
// added logic to display <meter model no> on report for both ReportGenV2 and KiggsHealthyReport_V1_2.json
// tested good for Kiggs Report
//version 4.2.0.2.1.5  03-Feb-2023      Gopinath M
// implemented isAllSetParametersInRange in DeploymentManagerController , but functionality not completed
// modified database attributes of procal_v2_5.deploy_devices -> ct_ratio, pt_ratio from int to VARCHAR 
//version 4.2.0.2.1.4  27-JAN-2023      Gopinath M
// added new fields to database procal_v2_5.deploy_test_cases for storing actual values, warmup time and test time
// added logic to display Custom test results on ReportGenV2
// added logic to display Active/Reactive Energy on Report
// added logic to display overall complies/ does not comply status on report
// Optimized logic for getDeployedDevicesJson in ReportGenV2
// added logic to display test period time, warmup time , Voltage, Current, pf , freq and energy in ReportGenv2
// population of Data for test period time, warmup time , Voltage, Current, pf , freq and energy in deploy_test_cases table is pending
//version 4.2.0.2.1.3  25-JAN-2023      Gopinath M
// All report generated good except Custom Test,  pending format for headers
// on the config file "ReportProfileDefaultActiveGroupName" added = *.* to import all groups from database 
// Added logic to load the PrintStyleProfile from config for Result data, Table Header and Generic Header in ReportGenV2 - PRINT_STYLE_LIST
//version 4.2.0.2.1.2  24-JAN-2023      Gopinath M
// fixed issues on ReportGenV2GUI test filter Gui edit mode
// fixed issues on ReportGenV2GUI on saving test filter , saved the dbId for the rpPrint and operationProcess
//ReportGenV2GUI adjustments
//version 4.2.0.2.1.0  24-JAN-2023      Gopinath M
// ReportGenV2GUI adjustments
//version 4.2.0.2.0.9  24-JAN-2023      Gopinath M
// added logic to populate present page number and Max page number on the ReportGenV2 Reports
// added logic to populate all Meter profile data on the ReportGenV2 Reports
// added logic to populate report generation timestamp and execution timestamp on the ReportGenV2 Reports
// added logic do map the parameter profile name to report profile group
// added logic to retrieve parameter profile and populate on the test filter tab - report profilr
//version 4.2.0.2.0.8  23-JAN-2023      Gopinath M
// parameter profile import from json completed
//version 4.2.0.2.0.5  12-JAN-2023      Gopinath M
// Added logic to display local Page Status and OverAll Report status on any Page - ReportGenV2
// tested with LecsHealthyMeterReport-V0_9
//version 4.2.0.2.0.4  11-JAN-2023      Gopinath M
// Post Processing completed for the ReportGenV2
//version 4.2.0.2.0.3  11-JAN-2023      Gopinath M
// completed master output data populate on ReportGenV2
//version 4.2.0.2.0.2  10-JAN-2023      Gopinath M
//completed error upper/lower limit calculation and result for LocalOutput in ReportGenV2
//version 4.2.0.2.0.1  10-JAN-2023      Gopinath M
// completed OPERATION_METHOD_ERROR_PERCENTAGE case in ReportGenV2
//version 4.2.0.1.1.1  09-JAN-2023      Gopinath M
// Added logic to calculate RSM difference for header populate only
// fixed issues on ReportGenV2 to populate Contant Diffference value
//version 4.2.0.1.1.0  08-JAN-2023      Gopinath M
// For Report GenV2 implemented all operation method except OPERATION_METHOD_ERROR_PERCENTAGE
// for Operation Process method difference -> Difference will calculated from Bottom to Top(listViewInputProcessList) to correct result
//version 4.2.0.1.0.9  08-JAN-2023      Gopinath M
// added logic to populate values of Procesed Local Output Data
//version 4.2.0.1.0.8  07-JAN-2023      Gopinath M
// Report GenV2 - except Constant Test all result populate completed for Report GenV2
// worked good with template LecsHealthyMeterReport-V0_9.xlsx
// Operation Process is yet to implement
//version 4.2.0.1.0.7  06-JAN-2023      Gopinath M
// Added logic to display Constant Test on Report GenV2
// Added logic to populate Rack Position on the Report
//worked good with DB test_springboot
//version 4.2.0.1.0.6  06-JAN-2023      Gopinath M
// Report GenV2 - data population on first page for All report except two RPS-Normal and ContantTest
//version 4.2.0.1.0.4  05-JAN-2023      Gopinath M
// added ReportGeneration to generate report V2 feature
// report serial number and dut serial number populated in report first page
//version 4.2.0.1.0.3  1-JAN-2023      Gopinath M
// Happy New Year
// Delete implemented for Meta Data and Test Filter using spring
// Added logic to add operation data to database using spring
//version 4.2.0.1.0.2  30-DEC-2022      Gopinath M
// updated logic to Edit Test Filter and save cell position data using Spring
//version 4.2.0.1.0.1  26-DEC-2022      Gopinath M
// Added logic to save Test filter in to database using spring - in progress
//version 4.2.0.1  26-DEC-2022      Gopinath M
// created build for LECS - deployed on 27-Dec-2022
// fixed defect while selecting default non standard report, prompted select atleast one report
// version 4.2.0.0.3.3  14-DEC-2022      Gopinath M
// added logic to fetch OnetoMany data in Spring framework for ReportGen V2
// changed default report profile GUI disable - report selection
// version 4.2.0.0.3.1  13-DEC-2022      Gopinath M
// fixed defects on Voltage unbalance execution not stopped
// fixed issues to generate Export Mode reports
// added logic to populate Voltage Variation,  phase reverse sequence, Freq Test, Volt Unbalance data in custom report generation
// in Database procal schema V2_4 added column energy_flow_mode in deploy_manage for Export mode identification
//version 4.2.0.0.3  13-DEC-2022      Gopinath M
// fixed defect : Constant test execution in Export mode not stopping with target limit
// fixed defect : Reverse phase sequence execution in export mode not initiated
// fixed defect : For Feed back control even when the Voltage or current is zero, 
// feed back control for phase angle tried through App - hunting occured 
//version 4.2.0.0.2  11-DEC-2022      Gopinath M
// added logic to make all current zero with special command lscsPowerSrcMakeAllCurrentZero
// with firmware version 0_6_2
//version 4.1.0.0.1.8   24-Nov-2022      Gopinath M
// added customer configuration for lecs- dec2022 - 20 position
// added spring data- Spring jpa and hibernate for reportGen version2
//version 4.1.0.0.1.8   24-Nov-2022      Gopinath M
//added logic to limit special character on project name SaveAsProjectController.guiInit();
//version 4.1.0.0.1.7   21-Oct-2022      Gopinath M
// for Kiggs checked the report generation with config Avvnl_HealthyMeterRepeatConstReportWithMultiplePageV1_6.json
// and template HealthyMeterRepeatConstMultiPage-V0_9.xlsx
//version 4.1.0.0.1.6   20-Oct-2022      Gopinath M
//added post operation logic on TestFilter for ReportGenV2
//version 4.1.0.0.1.5   19-Oct-2022      Gopinath M
// added logic to display more TestFilter data on the table for ReportGenV2
//version 4.1.0.0.1.4   19-Oct-2022      Gopinath M
// added logic to display the TestFilter data on the table for ReportGenV2
//version 4.1.0.0.1.3   19-Oct-2022      Gopinath M
//optimised testFilterData logic on ReportGenV2
//version 4.1.0.0.1.2   18-Oct-2022      Gopinath M
//updated logic for ReportGenV2 testFilterData for CellPosition duplicate validation
// and created a class ReportProfileTestFilterDataModel - yet to store the values on the class
//version 4.1.0.0.1.1   17-Oct-2022      Gopinath M
//updated logic for ReportGenV2 to add MeterMetaData to tableview
//version 4.1.0.0.1.0   16-Oct-2022      Gopinath M
// added validateTestFilterCellDataEntry and validateMeterMetaDataCellDataEntry for Report GenV2
// refactored the package name form callibration to calibration
//version 4.1.0.0.0.9   15-Oct-2022      Gopinath M
// added the logic for Operation titled pane in Report GenV2 GUI
//version 4.1.0.0.0.8   06-Oct-2022      Gopinath M
//optimised Report GenV2 GUI - frozen <Test Filter Data>
//version 4.1.0.0.0.7   06-Oct-2022      Gopinath M
//optimised Report GenV2 GUI
//version 4.1.0.0.0.6   04-Oct-2022      Gopinath M
// added logic to display MeterConstant and CT, PT ratio on the Report GenV2
// Added header3 Gui objects for Report GenV2
// Tested with Avvnl_HealthyMeterRepeatConstReportWithMultiplePageV1_5.json and template HealthyMeterRepeatConstMultiPage-V0_8.xlsx
//version 4.1.0.0.0.5   03-Oct-2022      Gopinath M
// added ReportProfileConfigController and fxml for report generation V2
//version 4.1.0.0.0.4   02-Oct-2022      Gopinath M
// On the report added logic to split the results in multiple pages (in Case if report exceeds more than 20 meters)
// tested with template HealthyMeterRepeatConstMultiPage-V0_7.xlsx and 
// report config file Avvnl_HealthyMeterRepeatConstReportWithMultiplePageV1_4.json
// added below data on the report config
//	"MaxNoOfPageInReport" : 2,
//	"MaxDutDisplayPerPage" : 11,
//	"SplitDutDisplayIntoMultiplePage": true,
//version 4.1.0.0.0.2   29-Sep-2022      Gopinath M
//issue fixed: after constant test LOE tsting failed. sending incorrect value to ldu -> fix setReadRefStdAccuDataFlag(false) added for Kiggs RefStd
//version 4.1.0.0.0.1   26-Sep-2022      Gopinath M
// report print enabled for multiple pages 
// tested with Avvnl_HealthyMeterRepeatConstReportV1_1.json - > HealthyMeterRepeatConst-V0_5.xlsx - >  HealthyMeterWithRepeatConstReport profile
//save as pdf python script working good in Gopi system
//version 4.1.0.0   25-Sep-2022      Gopinath M
// Added logic to populate results for Repeatabilty and Dial-Constant test on Custom Report
// Avvnl_HealthyMeterReport.json, Avvnl_HealthyMeterRepeatReport.json and Avvnl_HealthyMeterRepeatConstReport.json
// with template HealthyMeter-V0_4.xlsx, HealthyMeterRepeat-V0_4.xlsx and HealthyMeterRepeatConst-V0_4.xlsx template respectively
//version 4.0.5.1.1.8   23-Sep-2022      Gopinath M
// restructure resources config folder path
// added ConfigFilePathLocation and ConfigFileName app-config json file
// Fixed serial port crash - when refstd serial commmand check is failed // #RefStdSerialPortCrash_2022_09_24
//version 4.0.5.1.1.7   23-Sep-2022      Gopinath M
// updated config for Custom Report generation
//version 4.0.5.1.1.6   21-Sep-2022      Gopinath M
// execution from ConstTest - > LOE caused issue in Error value display - fixed 
// In RSM some times Current rasnge was not switch to Manual 100 Amps Tap, fixed , increased the retry from 15 to 25
// When Constant test aborted , issue in Error display on running next LOE test - issue yet to be resolved
//version 4.0.5.1.1.4   15-Sep-2022      Gopinath M
//version 4.0.5.1.1.3   15-Sep-2022      Gopinath M
//added logic to turnoff the power source current relay through LDU-01 position [MSCDCL V2.9] - yet to be tested 
// added POWERSOURCE_DOSAGE_CURRENT_RELAY_OFF_ENABLED
//version 4.0.5.1.1.2   13-Sep-2022      Gopinath M
// testing reactive mode failed
//version 4.0.5.1.1.1   12-Sep-2022      Gopinath M
// MeterReadingPopup added GUI for 20 position display
//version 4.0.5.1.1.0   12-Sep-2022      Gopinath M
// tested constant test with Kiggs refstd working - good
//version 4.0.5.1.0.9    12-Sep-2022      Gopinath M
// added ProcalFeatureEnable.POWERSOURCE_MANUAL_MODE
// added ConstTestManualSourceExecuteStart for Manual Source control - yet to be tested
// Added serial port command validation for Kiggs ref Std
//version 4.0.5.1.0.8    10-Sep-2022      Gopinath M
// rssPulseConstant auto calculation method in progress KIGGS_REFSTD_AUTO_CALCULATION
//in Kiggs refStd current Tap setting worked good
// in Kiggs refstd current Auto mode set failed
// for Power source maunal mode added prompt to reconfirm the current tuning POWERSOURCE_MANUAL_MODE_CURRENT_SET_RECONFIRMATION_REQUIRED
//version 4.0.5.1.0.6    08-Sep-2022      Gopinath M
// fixed the issues on the delay for refStd data refresh #REFSTD_READ_DATA_REFRESH_DELAY_FIX 
//#DEBUG_2022_09_08_REF_STD_KIGG
//version 4.0.5.1.0.5    08-Sep-2022      Gopinath M
// updated parsing logic for Dot placement for Kiggs Ref std data
// single_phase_RSM_communication_Protocol_v1_1.docx
//version 4.0.5.1.0.4    05-Sep-2022      Gopinath M
// single_phase_RSM_communication_Protocol.docx
//version 4.0.5.1.0.3    05-Sep-2022      Gopinath M
// added RefStd_KIGGS_hc3100h_1.json for kiggs
//for ref std pulse constant formula 
//CP = 36000000 / UUUUUU/IIIIII * 60000;(Single-phase power constant) // from document <single_phase_RSM_communication_Protocol.docx>
//CP = 3600000  / UUUUUU/IIIIII * 60000;(Single-phase power constant)  // but this worked good
//version 4.0.5.1.0.2    05-Sep-2022      Gopinath M
// all parameter from Kiggs ref std read and displayed
//version 4.0.5.1.0.1    03-Sep-2022      Gopinath M
//Cmd validation for Kiggs refstd validation pending #KIGGSREFSTD
// created for Kiggs - Semi-Automatic test bench - single phase
// added generateCheckSumWithOneByte for the  KIGGS ref Std to to calculate checksum and discard the carriage byte
//from KIGGS ref std read voltage and current value displayed
//version 4.0.5.1 deployment planned for enercent customer
// changed calibration file values for the product
//version 4.0.5.0.0.1    09-Jul-2022      Gopinath M
//customer configuration done for ENERCENT_1PHASE_3POSITION_2022
//version 4.0.5.0    23-Jun-2022      Gopinath M
// code freeze done for Electrobyte customer
//version 4.0.4.9.1.2    23-Jun-2022      Gopinath M
// added manipulateRatedVoltageFor3PhaseDeltaFromL_L_TO_L_N()  for the 3 Phase delta Meters
// added manipulateY_PhaseCurrentFor3PhaseDelta() for the 3 Phase delta Meters
//version 4.0.4.9.1.1    22-Jun-2022      Gopinath M
// added logic for 3phase delta on accuracy testing
// fixed issue for constant test reactive type - conversion from kvarh to vrah missed
//version 4.0.4.9.1.0    21-Jun-2022      Gopinath M
// for ref std feedback control - fixed issue to maintain the phase angle degree on export mode
// Applied Export mode only for Custom test type, extended the Export to all test type
//issue fixed - Electrobyte 3 Phase, 6th position LDU error value not updated in Application
//issue fixed  = Phase reverse sequence - when moved to repeatability Phase angle on V1 to V2 stayed in 240 instead of 120 degree
//version 4.0.4.9.0.9    12-Jun-2022      Gopinath M
//fixed issue on Freq not proceed to next test // added setExecuteStopStatus(false); after secondvalidation
//version 4.0.4.9.0.8    12-Jun-2022      Gopinath M
// on the ConstantApp.MeterType removed 3 Phase delta active and reactive type
//if ProcalFeatureEnable.LSCS_PWR_SRC_FINE_TUNE_WITH_REF_STD_ENABLED then enable
//ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_ENABLED logic added
//version 4.0.4.9.0.6    11-Jun-2022      Gopinath M
// made custome changes for loadDevices: ELECTROBYTE_HYBRID_2NO_3PHASE_10NO_1PHASE_POSITION_2022 custom
// in the Rack 3 phase rack is located in position1 and position 6 , made the custom changes
//version 4.0.4.9.0.4    09-Jun-2022      Gopinath M
//RefsTd feed back control for current added buffer value for the increment and decrement for the slow deviation
//version 4.0.4.9.0.3    05-Jun-2022      Gopinath M
// RefsTd feed back control completed for Volt, current and phase angle for 3 phase - 
// tested at volt = 230V, current = 5 A and pf = 0 degree, yet to test for other ranges
// RefsTd feed back control only implemented on Accuracy test
//worked good with with firmware - Master version 0.6.2
//version 4.0.4.9.0.2    05-Jun-2022      Gopinath M
// RefsTd feed back control in VB mode - still in progress with firmware - Master version 0.6.1
//voltage feedback working good for 240V with accuracy 0.05% allowed on all three phase. Rest of the value ranges need to be validated
// on config file "RefStdFeedBackVoltAllowedUpperLimitPercentage": 0.05,
// "RefStdFeedBackVoltAllowedLowerLimitPercentage": 0.05,
//version 4.0.4.9.0.1    02-Jun-2022      Gopinath M
// updated the customer configuration for electrobyte
//version 4.0.4.8.2.4    14-May-2022      Gopinath M
//updated the lscs_racanaa_apr2022_calibration.json for the Initial starting value of the current TAP
//display was showing RMS raw value instead of manipulated actual user value
//version 4.0.4.8.2.2    14-May-2022      Gopinath M
//fixed issue - when single selected , Y and B phase showing 8V , and current 0.22
//for manual mode, source was not turned off during User-Stop or Timer-Off stop - fixed the issue
//fixed the GUI for resolution less than 768 for Deployment and TestRun screen
//version 4.0.4.8.2.0    08-May-2022      Gopinath M
// Timer Mode and Balanced on Manual mode validated- working good
//version 4.0.4.8.1.9    07-May-2022      Gopinath M
// added logic for Timer Mode in Manual execution
// added logic for Manual mode - Balanced check box
// added below param in config
//ConstantAppConfig.PROPOWER_MANUAL_MODE_TIMER_INPUT_MIN_ACCEPTED 
// ConstantAppConfig.PROPOWER_MANUAL_MODE_TIMER_INPUT_MAX_ACCEPTED 
// ConstantAppConfig.PROPOWER_MANUAL_MODE_DEFAULT_TIMER_INPUT
//version 4.0.4.8.1.8    30-Apr-2022      Gopinath M
// tested Manual mode for import/export , single phase three phase, freq change , with out amplifier - working good
//version 4.0.4.8.1.6    30-Apr-2022      Gopinath M
// for custom made changes to support export mode
// tested working fine with Radiant reference standard
//version 4.0.4.8.1.5    25-Apr-2022      Gopinath M
// tested with D:\we can\DSP\DSP281x_examples\RACANAA\MASTER_HYP5-V0_6_0
// updated racanna calibration file
//version 4.0.4.8.1.4    24-Apr-2022      Gopinath M
//updated GUI components on Manual Mode Tab
//updated ProjectExecutionProPower_W.fxml with tab pane for <Sequence Mode> and <Manual Mode>
//added ConstantAppConfig.DEPLOYMENT_REFERENCE_NUMBER_LABEL_NAME for deployment manager
//added ConstantAppConfig.INSTANT_METRICS_DISPLAY_ALWAYS_ON_TOP to display instant metrics always on the top
//added calib folder under resource folder to place propower calibration file, updated the logic to 
// load the file from configured resource path ConstantAppConfig.LSCS_POWER_SOURCE_CALIBRATION_FILE_PATH
//added logic to load inhouse calibration mode in loadInHouseMode function
//added logic to enable loadnext and stop button in custom mode feature
//version 4.0.4.8.1.1    19-Apr-2022      Gopinath M
// added logic to disable stop button for source communication latency issue
// also added retry logic to stop process in shutdown process
//version 4.0.4.8.1.0    15-Apr-2022      Gopinath M
//added master config file to configure the customer specific app_config.json file 
// ConstantMasterConfig
//version 4.0.4.8.0.8    15-Apr-2022      Gopinath M
//added ProjectExecutionProPower_W.fxml for Propower button displays
//added logic to update EXECUTION_STATUS_COMPLETED & EXECUTION_STATUS_INPROGRESS for the ProPower mode
//optmised the code to enable/Disable load next button at appropriate logic
//version 4.0.4.8.0.7    15-Apr-2022      Gopinath M
// added ProcalFeatureEnable.PROPOWER_SRC_ONLY for propower only
// added ProcalFeatureEnable.PROPOWER_SRC_FEEDBACK_DISPLAY for feedback display from propower
// added DisplayInstantMetricsProPower_W.fxml for propower only
//version 4.0.4.8.0.6    14-Apr-2022      Gopinath M
// optimised code to display Propower feed back value
// added logic to display power factor  - GUIUtils.calculatePowerFactorWithDegree
//version 4.0.4.8.0.5    14-Apr-2022      Gopinath M
// updated logic for RACANAA customer
// added logic to display the Propower fedd back value from the power source
// working good with firmware Version 0_5_9 - When zero (0) is sent by PC, receives all parameter data from power source with prefeix header "ALL="
// ConstantLscsPowerSource.CMD_PWR_SRC_READ_FEEDBACK_ALL_DATA_HDR
//version 4.0.4.8    03-Feb-2022      Gopinath M
//deployed to LECS customer
// enabled phase reverse sequence and self heating report in Report gui
//when result count is less than total meter loaded count - data not populated - fixed issue added logic in FillErrorValueXSSF
// added XSSFFormulaEvaluator.evaluateAllFormulaCells to populate formula values in the excel - Tested with constant report
// added changes for starting current value on report
//version 4.0.4.7    27-Jan-2022      Gopinath M
// deployed to LECS customer
//version 4.0.4.6.4.1    20-Jan-2022      Gopinath M
// in calibration mode while loading next exeuction failed. Added execution semlock on loadnext click- issue resolved
// in custom test voltage reset occured - issue fixed
// in custom test aource command failed - increased the time wait - issue fixed
//version 4.0.4.6.4.0    20-Jan-2022      Gopinath M
// added ref_txtAreaRxSerialData, ref_txtAreaRxSerialDataInHex to display Rx all data on MaintenanceModeExecController
// added ref_chBxResponseExpectedForCmd on MaintenanceModeExecController
//version 4.0.4.6.3.9    18-Jan-2022      Gopinath M
// on RefStdConstController added tab event for TableView with EditCellRefStdPulseConstVoltageAndCurrent
// and EditCellRefStdPulseConstCurrentOnly
//version 4.0.4.6.3.8    17-Jan-2022      Gopinath M
// added gui logic for RefStd pulse Constant in setting - feature incomplete
// added ref_radioBtnMainCt hide logic in report controller
// saveJsonFileDisplay added to save the json file to external folder
// viewJsonFileClick added to view the present data in the GUI during calibration
// completed MaintenanceModeExecController which can be used instead of hyperterminal for lscs power source command
//version 4.0.4.6.3.7    14-Jan-2022      Gopinath M
// added ConstantApp.DEFAULT_NO_OF_PULSES which affected self heating warmup issue - fixed
// added ConstantApp.CALIBRATION_MODE_USER_NAME and MAINTENANCE_MODE_USER_NAME for calibration and self test mode
//version 4.0.4.6.3.5    13-Jan-2022      Gopinath M
// tested phase reverse sequence and it is working good with ProPower Firmware LECS 0_5_8
// added ConstantConfig.REF_STD_ENABLE_PARSING_LOGS
// MaintenanceModeExecController basic functionality working good. Just sending and receiving working good. Need to enhance the feature
//version 4.0.4.6.3.4    12-Jan-2022      Gopinath M
// added semLockExecutionInprogress for execution mode
// added logic for Phase reversal for LSCS power source with firmware version ProPower MASTER_HYP5-V0_5_8
//version 4.0.4.6.3.3    12-Jan-2022      Gopinath M
// fixed issue for voltage unbalance testing - This will also impact all previous customer release, Nsoft, Sands
// found issue on Phase reversal but not fixed the issue
//version 4.0.4.6.3.2    08-Jan-2022      Gopinath M
// added ignore errors for report excel sheets - not tested
// sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
// for voltage unbalance issue - LSCS power source current relay id selection was wrong. fixed it on manipulateTargetRmsValue()
// added LSCS_STATIC_POWER_SOURCE_INIT
//version 4.0.4.6.3.1    05-Jan-2022      Gopinath M
//fixed issue in report generation - when there is underscore on the project it failed
//version 4.0.4.6.3.0    04-Jan-2022      Gopinath M
//fixed issue for frequency change on the custom test execution
//fixed issue for Constant test and tested working good
//version 4.0.4.6.2.9    04-Jan-2022      Gopinath M
// fixed Validate_PhaseLagLead to accept -1 Lag on the Customtest to validate UPF on export
// stop execution error - fixed

//version 4.0.4.6.2.8    03-Jan-2022      Gopinath M
//updated Test Run type to default Pulse Based
//version 4.0.4.6.2.7    02-Jan-2022      Gopinath M
// Kre RSM Serial port RX/TX should be cross connected
// isLastDataSetupForRefStdKreSame updated for Kre setting configuration
// added refStdUpdateConfigurationSettings for all the test types -- included kre setting bnc configuration
//version 4.0.4.6.2.6    01-Jan-2022      Gopinath M
// worked good with Kre RSM with setting parameter update for Accuracy testing only
//version 4.0.4.6.2.5    01-Jan-2021      Gopinath M
// fixed exception on the warmup test case
// for KRE-RSM pulse constant 
// RSM output constant = 110000000 at KRE-RSM, and 110000 at Procal-App
// RssConstantInkiloWattHour = (REF_STD_MAX_OUTPUT_FREQ_IN_HERTZ *3600)/TotalPowerInKiloWatts;
// LSCS ldu working good for MasterMeter Pulse of 102 Khz, 
// checked at 400khz, Energy meter error result was too much
//version 4.0.4.6.1.6    22-Dec-2021      Gopinath M
// discussing with Kre obtained OutPut frequency of Kre RSM is 1Hz to 1Mhz
// updated logic to read Kre refstd setting data and basics data reading
//version 4.0.4.6.1.4    14-Dec-2021      Gopinath M
// updated database schema for sp_getproject_componentsSaveAs for inf_average null population issue
// added logic on manipulateNoOfPulsesForTimeBased for Time based feature for below issue - TimeBased Observation.xlsx
// for below calculation expected AverageCycle was 96, but actual was 97, hence made the correction
/*	meterConstantInImpulsesPerKiloWattHour = 3200;
	timeDurationInSec = 90;
	totalTargetPowerInKiloWatt = 1.2f;//0.6f;///1.2f;//3.6f;//1.2f;
	AverageCycle = DisplayDataObj.manipulateNoOfPulsesForTimeBased(meterConstantInImpulsesPerKiloWattHour,timeDurationInSec,totalTargetPowerInKiloWatt); 
	ApplicationLauncher.logger.debug("timeBasedAssert : AverageCycle3: " + AverageCycle  );
 */
// added logic to import the CSV file while deployment ConstantConfig.IMPORT_DEPLOYMENT_DATA_FEATURE_ENABLED
// DeploymentImport-V0_2.csv tested and working good
/*	Rack ID,Select,Serial No,CTR Ratio,PTR Ratio,Meter Constant,Make
	2,Yes,123456,102,202,2002,Ineesh2
	3,No,MT123456,103,203,2003,
	4,Y,MT123457,,204,2004,Ineesh4
	5,N,MT123458,105,,2005,Ineesh5
	6,y,MT123459,106,206,2006,Ineesh6
	7,n,MT123460,107,207,2007,Ineesh7
	8,yes,,108,208,2008,Ineesh8
	9,No,MT123462,109,209,,Ineesh9
	10,,MT123463,110,210,2010,Ineesh10*/
//version 4.0.4.6.1.3    13-Dec-2021      Gopinath M
// added logic for blacklist, whitelist and empty serial number meter id - validated and logic working good
// ConstantConfig.METER_ID_BLACKLIST_VALIDATION, METER_ID_WHITELIST_VALIDATION, 
// METER_ID_VALIDATE_ALREADY_TESTED, METER_ID_VALIDATE_FOR_EMPTY
//version 4.0.4.6.1.2    06-Dec-2021      Gopinath M
// added logic for User access control to the screen - tested okay for all screens
// updated db schema to 2.3
//version 4.0.4.6.1.1    08-Nov-2021      Gopinath M
// DUT: CommFeatureEnabled added in the Settings for 48 DUT Comm ports - Tested for DUT1 setting - passed
// updated DevicePortSetup GUI object arrangement
//version 4.0.4.6.1.0    16-Aug-2021      Gopinath MV2.0
//added calculateTotalTargetPower and manipulateNoOfPulsesForTimeBased for TimeBased on LDU - yet to be tested
//added logic to read the lscs power source calibration file from config file
//reference TimeBased.xlsx
// LSCS_POWER_SOURCE_CALIBRATION_FILE_NAME
//version 4.0.4.6    28-Jul-2021      Gopinath MV2.0
// deployed to Sands PC - 29-Jul-2021
//updated config file and implemented logic for replaceDataInReportExcel for custom report
//updated sands template as per customer requirement - DHBVN_HealthyMeter-V0_3.xlsx & DHBVN_UnhealthyMeter-V0_2.xlsx
//Added logic to populate CustomerReferenceNo/Batch no on the Custom report generation
//version 4.0.4.5.1.5    26-Jul-2021      Gopinath MV2.0
//completed for Sands Healthy Meter template DHBVN_UnhealthyMeter-V0_1.xlsx - all expected data populated
//added logic removeRackIdInMeterSerialNoColumnXSSF in customreport logic
//version 4.0.4.5.1.4    26-Jul-2021      Gopinath MV2.0
//completed for Sands Healthy Meter template DHBVN_HealthyMeter-V0_2.xlsx - all expected data populated
//version 4.0.4.5.1.3    26-Jul-2021      Gopinath MV2.0
// completed for Sands Healthy Meter template DHBVN_HealthyMeter-V0_1.xlsx
//version 4.0.4.5.1.2    26-Jul-2021      Gopinath MV2.0
// for custom1 healthy meter report data population done with STA,NoLoad and Accuracy test type
//version 4.0.4.5.1.1    25-Jul-2021      Gopinath MV2.0
// added Custom1ReportConfigLoader
// updated lscsLDU_ReadErrorData for the Sands site issue reported, sometimes LDU sends 0 for LOE testing
// added logic if(ErrorValue.length() > 1){ // sometimes LDU sends data 000000
//version 4.0.4.5.1.0    22-Jul-2021      Gopinath MV2.0
// added logic for RACK_HYBRID_MODE_ENABLED for sands customer
// added logic for DefaultReportProfileDisplay and added param in config.json
// added logic for TotalReportProfile and added param in config.json
//version 4.0.4.5    21-Jul-2021      Gopinath MV2.0
// For Sands - Current added 1mA configuration in lscs_calibration_config.json
// not released to customer yet
// worked good with Sands- MASTER_HYP5-V0_5_6
//version 4.0.4.4    8-Jul-2021      Gopinath MV2.0
//build created for sands
//version 4.0.4.3.1.1    7-Jul-2021      Gopinath MV2.0
// fixed the issue on constant test to work with Imax, tested at site working good
//version 4.0.4.3.1.0    7-Jul-2021      Gopinath MV2.0
//resized secldu display for 40 meters
//fixed issue: when starting current is in first index execution, current not pumped
//version 4.0.4.3    3-Jul-2021      Gopinath MV2.0
// build created for sands
//version 4.0.4.2.1.9    2-Jul-2021      Gopinath MV2.0
// fixed all issues for averageLduFeature and tested at gurguon site
//version 4.0.4.2.1.8    29-Jun-2021      Gopinath MV2.0
//for averageLduFeature updated status for average error results
// in database added field inf_average on table project_components 
// and updated logic on sp_add_project_components
// upaded the funtionality for adding field inf_average
// tested with Accurcy for db working good
//version 4.0.4.2.7    28-jun-2021      Gopinath 
//fixed extension of timing for average LDU feature LSCS-tested and verified working good
//version 4.0.4.2.6    28-Jun-2021      Gopinath MV2.0
// added lduErrorDataHashMap2d for averageLduFeature. yet to be tested
//version 4.0.4.2.4    25-Jun-2021      Gopinath MV2.0
//fixed the for averageLduFeature
//version 4.0.4.2.2    24-Jun-2021      Gopinath MV2.0
//********************************************
// Updated after Covid19 - worst pandemic of the century 
// Me Gopinath got rebirth/second life due to world around prayers
// its a Tsunami for my family. All of my Family Tree base members hospitalized and recovered from Covid19
// Oli -Karthik-IRTT and Bala-IRTT gave me positive and boost speech during my battle at hospital
// In 2 weeks period lost 2 relatives and 2 family members
// Lost My uncle Karthik aged - 47
// after 1 week lost both my parents between 3 days interval
// after 1 week Lost My Another uncle Muni - age may be 70
//***************************************************
// merged feature to execute the application with out Reference std/ rsm from kigg ProCAL-k4.0.3.2.5
// disabled below flag to test without sands rsm
// ProcalFeatureEnable.STABILIZATION_PWR_SRC_ENABLE_FEATURE = false; //for testing with out rsm, disabled the flag
// averageLduFeature implementation in progress
//version 4.0.4.2.1    23.june.2021
//Changes made for LDU Average reading - inprogress
//version 4.0.4.2    23.june.2021 
//creating setup installer file for sands client
// version 4.0.4.1.6.2.3 inspection passed with sands client  16.june.2021
//updated configuration from calibration mode to normal mode 14.june.2021
//Enabled LDU secondary display for 48 positions 14.june.2021
//version 4.0.4.1.6.2.2   - 9-May-2021             by Gopinath
// Slowly recovering for Karthik Loss, but i'm afraid to call his wife. i dont have strength to convey condolences to her
// added logic Sync selection for both Voltage and Calibration for LSCS power source calibration - 
// chkBxCurrentSyncSelection, chkBxVoltageSyncSelection
//version 4.0.4.1.6.2.1   - 8-May-2021             by Gopinath
// calibration working good on current, tested 50A, 10A tap
//version 4.0.4.1.6.2.0   - 8-May-2021             by Gopinath
// My Cousin Mr.Karthik left us due to Covid-19. It was so pathetic to watch him going to burial ground with out any relative. 
// and hospital allowed us to watch him only for one or two minute.
// Rest in Peace Karthik mama - God almighty will be with you
// Not sure how we will pass through India -Covid-19 wave two
// updated logic to compute gain and offset value during lscs source calibration. This data required to update in source firmware
// added logic to display lscsSourceCalibrationStageDisplay in seperate independant screen
//version 4.0.4.1.6.1.9   - 6-May-2021             by Gopinath
//added logic to perform semi-automation on calibration on Current Y-Phase and B-Phase
//added logic to perform semi-automation on calibration on Voltage Y-Phase and B-Phase
//version 4.0.4.1.6.1.8   - 6-May-2021             by Gopinath
//added logic to perform semi-automation on calibration on Current R-Phase
//version 4.0.4.1.6.1.7   - 6-May-2021             by Gopinath
// added logic to perform semi-automation on calibration on Voltage
//version 4.0.4.1.6.1.3   - 4-May-2021             by Gopinath
// Reactive mode - Current relay not set currect on Sands RMS - done - yet to be tested
// limit ict to only 3 phase during execution - done - yet to be tested
//version 4.0.4.1.6.1.2   - 3-May-2021             by Gopinath
// LDU all position working good. Yet to test Position16 with 3phase meter
// ICT com validation success
// ICT control for 3 phase in progress
//version 4.0.4.1.6.1.1   - 1-May-2021             by Gopinath
// Happy Labours Day Makkalae. Excited for Tamil nadu Assembly election Result... 24 hours clock count down started
// ICT serial port working good. Yet to test with device
// LDU for all same meter and different meter working good
// adjusted DeploymentManager screen with fields better placement
// Lscs single phase power source with VB mode worked good
// * disable mct/nct for 3 phase meter- gui in execution screen - done
// * sands Volt and Current mode setting - worked good
// Mct/MSVT testing - worked good
//version 4.0.4.1.6.0.8   - 30-April-2021             by Gopinath
// in WriteToSerialCommPwrSrc for LSCS_POWER_SOURCE_CONNECTED reduced sleep delay from 200 to 10
//version 4.0.4.1.6.0.7   - 29-April-2021             by Gopinath
// added logic for ICT with ICT_CONTROL_ENABLED and  ICT_KRE_KE6323_CONNECTED
// dial test no of meter input screen adjustment for 40 meters - done - yet to be tested
// fixed the delay issue with LSCS power source communication
//version 4.0.4.1.6.0.5   - 29-April-2021             by Gopinath
// Dial test good with 3 phase active meter
// sands Volt and Current mode setting tested for 10 A good
// sands Active/ Reactive mode setting tested for Active mode tested good
//version 4.0.4.1.6.0.4   - 29-April-2021             by Gopinath
// Sands - power unit display on kW,kVAR,kVA on the instant metrics- implemented
// added isPowerSourceTurnedOff flag to control the Power source stop redundant
// sands accumulation reset, start, stop and read implemented - yet to be tested
//version 4.0.4.1.6.0.3   - 28-April-2021             by Gopinath
// ldu with different meter const working good
// refstd-sands Degree phase angle reading good
//version 4.0.4.1.6.0.2   - 28-April-2021             by Gopinath 
// sands phase angle degree reading - done - yet to be tested
// fixed displaying of PF in negative with Sands ref std - SANDS_REFSTD_NEGATIVE_PF_DISPLAY_ISSUE_EXIST
// VB app mode implementation for different test profile - done - yet to be tested
// ldu - 48 with individual meter const - merged from kiggs version ProCAL-k4.0.3.2.5
//version 4.0.4.1.6.0.1   - 27-April-2021             by Gopinath 
// sands refstd reading data done - except degree value;
// calibration - cfg-refresh, src-refresh and load next implemented
//version 4.0.4.1.5.5   - 26-April-2021             by Gopinath M
// calibration from config file worked good. Tested at LSCS
//version 4.0.4.1.5.3   - 25-April-2021             by Gopinath M
// added logic to display error when rxtxSerial.dll is missing on the java installed path
// version 4.0.4.1.5.1   - 24-April-2021             by Gopinath M
// added logic to perform calibration using vb mode 
// added two flags - LSCS_CALIBRATION_MODE_ENABLED and LSCS_APP_CONTROL_MODE_ENABLED
// version 4.0.4.1.3   - 23-April-2021             by Gopinath M
// added logic for lscs source voltage calibration mode
// version 4.0.4.1.2   - 20-April-2021             by Gopinath M
// added data decoding logic for Sands RSM - SerialDataSandsRefStd, tested instant metrics and accumulated data
// version 4.0.4.1.1   - 11-April-2021             by Gopinath M
// merged MTE refStd code from CalSoft version CalSoft-s1.0.0.2 to procal version 4.0.4.1.1
// MTE refstd merged code NOT tested
// intentionally NOT merged PLC code from Cal Soft
// version 4.0.4.1.0   - 11-April-2021             by Gopinath M
// merged ProCAL-k4.0.3.2.5 with 4.0.4 but not tested
// updated tested_by on deploy_manage database for reporting purpose
// updated database sp_add_deploy_manage with tested_by field on procal_nsoft schema
// added meter profile report with MeterProfileReportGenerationEnabled
// added individual meter report
// added Setpulse constant for voltage and current SetPulseConstantDataWithVoltageAndCurrent
// added REPORT_SUPPORTING_DATA_POPULATE_ENABLED
//version 4.0.4   - 22-Feb-2021
// installed on Nsoft customer
// taken branch from version 4.0.3.1.2
// fixed the issue on Export Accuracy to generate report for all data, which was found during kigg testing
// updated logic for current less than 1 amps in CalculateInfCurrent
// udpated logic to create the tespoint with same current range on onTestSelectionSaveClick
// db version procal-nsoft-V1_2-AppVer-s4.0.4.sql
//version 4.0.3.1.2   - 14-Jan-2021 - its a pongal festival
// database schema version procal_nsoftV1_1-AppVer-s4.0.3.1.2.sql
//reference Standard -Data in Test Run screen - implemented
//version 4.0.3.1.1   - 14-Jan-2021 - its a pongal festival
// about screen updated for LSCS pwer source and LDU
// for Starting current format updated to support lower than 100mA current and tested good
// Report screen refactored and auto adjustment done
// added TEST_EXECUTION_DIFFERENT_METER_CONSTANT_FEATURE_ENABLED to disable the meter constant during deployment
//version 4.0.3.1.0   - 13-Jan-2021
//worked good for Active and power source looks stable
// fixed ths issue with power source off condition while stopping
// MCT/NCT relay working good
//version 4.0.3.0.9   - 12-Jan-2021
// MCT/NCT report generation - done
// Accuracy report generated success
//version 4.0.3.0.6   - 12-Jan-2021
// MCT/NCT firmware / App changes implemented - lscsSetPowerSourceMctNctMode
//version 4.0.3.0.5   - 12-Jan-2021
//ldu - LCD reset during test case init - done
//added Report Profile - logic in config, gui and database
//version 4.0.3.0.4   - 11-Jan-2021
// at LSCS tested test result update with sequence number
// with LDU firmware tried ldu-lcd-display version number and ldu position ID -but failed
//version 4.0.3.0.3   - 11-Jan-2021
//power source hardware reset - smooth transition - done
//LDU-reading and pending in bottom status -implemented
//close project button - implemented
//unable to set milliamps current in STA in project - fixed
// WFR - displayed in Red - fixed
// self heating warmup- secondary ldu data - not reset - fixed
//version 4.0.3.2   - 9-Jan-2021
//ldu not turned ON- fix issues on procal - fixed
//added logic to update the result with sequence number
// tested all the scenarios with Active mode for 48 position except 11,19,27,30,32,43,48 id due to ldu hardwar issue
// instable observed with power source, found WaitForPowerSrcTurnOn will resolve the issue. But yet to implement and test
//version 4.0.3.0   - 8-Jan-2021
//report view with MCT/NCT implementation done
// report generation with MCT/NCT pending
// disable timebased type option with RUN_TYPE_TIME_BASED_ENABLED
//version 4.0.2.9.6.4 - 6-Jan-2021
// active /reactive  mode tested with BNC output1 
// In test execution - data retrieved for MCT/NCT mode
// found Selfheating warmup is skipping always 
// and extended the logic lscsZeroCurrentPowerTurnOn to selfheating_warmup execution and also constant test
//version 4.0.2.9.6.3 - 6-Jan-2021
//added updateBottomSecondaryStatus label
// fixed ldu logic for Constant test - yet to be tested
//version 4.0.2.9.6.2 - 5-Jan-2021
//tested with Reactive pulses
//Deployed data recovery after app reboot implementation good
//Test execution screen - data display recovery good for already executed test points
//LDU secondary screen display - implementation - validated and good
//For Const test - LDU implementation pending
// disabled kWH initial and final disabled for Creep-NoLoad and STA testing with below flags
// CREEP_KWH_READING_PROMPT_ENABLED , STA_KWH_READING_PROMPT_ENABLED
//version 4.0.2.9.6.1 - 5-Jan-2021
// implemented logic for ldu Secondary display
//version 4.0.2.9.6 - 4-Jan-2021
//updated to retrieve deployed data even after application reboot
//version 4.0.2.9.5 - 1-Jan-2021
// tested for 48th Position with Reactive mode- working good
//version 4.0.2.9.4 - 1-Jan-2021
// reactive test with warmup, LOE and constant Test - worked good, but error displayed as -2.231 value
//version 4.0.2.9.3 - 31-Dec-2020
//Tested constant test and Custom test worked good
//Worked for Active, value set correctly but displayed as Watthr/Impulse instead of Impulses/Watthour in reference standard
// changed manually in ref std from Watthr/Impulse  to Impulses/Watthour
//For reactive unable to test due to power source issue
//for Active, value set correctly but displayed as VAhr/Impulse instead of Impulses/VAhour in reference standard
//changed manually in ref std from Watthr/Impulse  to Impulses/Watthour
// implemented skipped reading functionailty for lscsLDU and working good
// LDU sending readingIndex as one byte, but itis rolling over to zero after 255 increment- tested with warmup-Test-RunTest
//version 4.0.2.9.1 - 29-Dec-2020
// worked good with LDU id 0x11>- 1st Position, 0x12-> 2nd position and 0x13-> 3rd Position
// Tested at LSCS  - LOE, NoLoad, STarting Current, Self Heating
// Repeatability, Voltage variation
// Pending - Frequency variation, Constant Test and Custom Test
// Harmoics firmware implementation pending
//version 4.0.2.9.0 - 27-Dec-2020
//disabled the Profile feature for single phase
//version 4.0.2.8.8 - 26-Dec-2020
// STA and CREEP with timer need to be tested
//version 4.0.2.8.6 - 20-Dec-2020
// updated rack data input from 12 to 24 in MeterReadingPopupController
// added logic to strip prefix zeros in LDU data for NoLoad and Starting Current
//version 4.0.2.8.5 - 19-Dec-2020
// STA Pulse count, Creep Pulse count, Accuracy Error limit , result status validation completed , displayed and updated in DB
//version 4.0.2.8.3
//Tested the logic for Starting current in Power source simulation mood. Tested at LSCS - 19-Dec-2020
//version 4.0.2.8.2
//updated the logic for lscs ldu starting current. Yet to be tested
//version 4.0.2.7
// lscs - ldu working for accuracy testing
//version 4.0.1
//added logic for lscsStaticPower source with Elmeasure singlePhase mother board - yet to be tested
//version 4.0.0 created for project NSoft
// 48 rack support
// ProPower static power source
// Radiant reference standard
// LS-LDU unit
// added new database procal_nsoft version 1_0
// added ConstantVersion.APP_ICON_FILENAME
//version 3.9.5 update the LDU_DecodeSerialData to incorporate changes to display "F-IN" when there is no RSS Input
// added RoundRefConst but commented
//This is updated by Gopi on 5-july-2019, but not tested. LDU_PreRequisiteForReadErrorV2 was added with DisplayDataObj.setLDU_ReadDataFlag( true) - for TP execution not moving forward if response is bad
//version 3.9.4.6
// test with repeatability issue with various output frequency
// since the LDU is instable when Frequency is set to 1.9Mhz. Unable to fix the issue. Recommending vendor to upgrade the LDU product
//version 3.9.4.2
//Increased the time turation from 9 sec to 30 sec to Ref Standard Constant value on Ref standard device
// RefStd_BNC_ConfigConstTask
//version 3.9.4.1
// implemented logic for Reference standard Constant calculation - calculateRSS_ConstantV42
//version 3.9.4
//CMD_LDU_ERROR_RESET = DecodeHextoString("2900213030"); // ("2901213030") - This is reverted as it was not supported by CCube 
// added the logic to calculate the Refstd constant and feature can be enabled with ConstantFeatureEnable.REF_STD_CONST_CALCULATE flag
// - unable to create new energy meter profile- resolved
// - While execution test execution continuing only first, not moving to second test point- fixed - need to be tested
//version 3.9.3 - Defect# 333 fixed 
// updated below command for LDU
//CMD_LDU_ERROR_RESET = DecodeHextoString("2900213030"); // ("2901213030") - This is reverted as it was not supported by CCube 
// added splash screen procal-splashV2 with LS Controls Logo
// fixed issue with DeleteLogFilesforX_NoOfPreviousDays - changed from int to long, when days set more than 24, typecast to long required
// For NoLoad when the time is set to 60 Min, LDU TimeFormat set to "0000" instead of "6000" fixed for Creep, STA and FormatTimeForAvgPulses
// Defect # 334 fixed, when STA and NoLoad executed after 
// Added logic on the ProcessAllTestCasesTimerTrigger to getUserAbortedFlag and optimised logic for DecrementExecuteTimeCounter going negative
// Defect # TBD fixed , BNC output port was hardcoded for port 1, added "BNC_OutputPort" in config.json and updated the logic
//version 3.9.2 - Defect#328 Fixed the issue for Voltage unbalance checkbox data restoring while loading the project
// voltage percentage validation applied to all test profile during project saving
// added ./log on the log4j properties to create the log files under that folder
// added log folder to the source and added the same in installer package
// Defect #330 - added <HoldLogFilesforX_NoOfPreviousDays> in the config.json to purge the old log files. X no of day -> excluding today
// Defect #331 - fixed - for repeatability when the time-based is 30 seconds. application execution timer goes negative. 
// added break statement on repeatablity and selfheat
//version 3.9.1 - fixed the issue "Select All" not working for new project
//made Ib column first in influence instead of Imax
// in Voltage variation, input is voltage percentage, hence placed logic to convert the percentage to voltage value before validation with config 
//In the config.json reversed the IMappingDefaultValues from Lower to higher current order
//version 3.9.0 - bumped up db schema version 2.5
// fixed frequency data validation for Frequency variation, updated  VoltMin, VoltMin, CurrentMin & CurrentMax 
// for LTCT and HTCT on config.json
// added splash screen
//version 3.8.9 - increased the PulseContant configuration to 12 level in config.json for both LTCT and HTCT
//updated DB data on the version mysql DB2-4 on system_config table
//version 3.8.8 - defect fix changes for 0.2 class HTCT- changed the config to accept more that 10 char for RSS Pulse Constant
//version 3.8.7 - Defect fix for the HTCT meter validation failed for 0.2 class
// Added "CT Type" param for Meter profile on GUI and updated the DB as well.
// Updated the additional below Param on the config.json
// "RSS_HTCT_CurrentThresholdInAmpsLevel1": 2.0,
// "RSS_HTCT_ActivePulseConstantAboveOrEqualLevel1":  "1000000",
// "RSS_HTCT_ActivePulseConstantBelowLevel1":"100000",
// "RSS_HTCT_ReactivePulseConstantAboveOrEqualLevel1":  "1000000",
// "RSS_HTCT_ReactivePulseConstantBelowLevel1":"100000",
// Updated the logic to set the Pulse Constant on the RSS for all Active/Reactive and LTCT/HTCT meter profiles
//
//version 3.8.5 - Enhancement to support the Accuracy report for two pages, added configuration on the config.json
//"Report": {
//	"AccuracyNumberOfPages": 2,
//	"AccuracyNumberOfSectionPerPage": 3
//}
//version 3.8.4 - released the version to customer
//version 3.8.3 - fixed the issue for starting current for value 0.001 , changed the data set format
//tested for LOE export report genearation
//fixed constant test report -reference value data population
//Implemented terms and condition screen
//implemented logic, no application exit during execution in progress-yet to be tested
//implemented logic for Device id value for Export test point on sp_ltadd_result function
//Removed Deviation_Default_Value from config .json and added the same to ConstantApp.java
//version 3.8.2 - Added Select ALL button to select all the Test Point in Test Point Selection
//Export mode on report generation - implementation in progress. 
//Currently done for constant test report-but still refernce std data populating right 
//value is still pending on report generation and Excelconfig
//version 3.8.1 - fixed defect on Report generation for Excel 2007 format
//version 3.8.0 - updated testrport to support excel 2007 format
//removed AppInstanceServerPort from config.json
//updated logic when deleting the project cleared the summary data on db
//version 3.7.9 - Updated report menu drop down data enable disable based on Feature enable, also for report generation check box visibility
//version 3.7.6 - Added Q data on the instant metrics display - released to prod on 25-sep -2018
//version 3.7.5 - tested calculatelaglead and it passed.Updated db schema v2.1. Deployed to prod on 25 Sep 2018
//version 3.7.3 - Corrected reporting to populate only unique meter name on excel- This will avoid duplication of row if we executed same test point on the project
//version 3.7.2 - Optimised reporting part and corrected Calculatelaglead function-but yet to be tested
//version 3.7.1 - tested with new logic for import and export, but not carried forward to 3.7.2
//version 3.7.0 - fixed issue for Harmonics Save functionality(when editing Harmonics value were appended to the old test point name) 
//  optimised all for logic with declaring variable outside for loop
//version 3.6.9 - optimised logic for  ProjectLoad and Save Profiles
//version 3.6.8 working fine with below constant and BNC VARH0 configured to 0.000010000
//"RSS_ActivePulseConstant":  "100000000",
//"RSS_ReactivePulseConstant":"100000000",
//but it failed for HTCT Reactive alone with 63V/1-2 A hence changed the below configuration changes with BNC VARH0 configured to 0.0001000
//"RSS_ActivePulseConstant":  "100000000",
//"RSS_ReactivePulseConstant":"10000000",
//version 3.6.8 tested with DB schema version 1.9 and report were able to pull in 1 seconds(earlier 4 min in 5.5 version) after upgrading MYSQL to 5.7 version
//version 3.6.6 cleanedup all files, implemented changes for project.getEMModelType, removed unwanted files. added logic for Import and Export functionality
//version 3.6.5 - deployed to prod with Reactive Phase angle corrected
//version 3.6.3 - implemented PF configurable similar to Current mapping
//version 3.5.8 - tested single phase and deployed to prod
//version 3.5.7 - implementing changes for single phase
//version 3.5.6 -version deployed to prod on 31-Aug-2018
//Version s3.5.4 with Schema 1.8, implemented test status display on table view on excel output, 
// implemented result sp optmisation
//version s3.5.2 - implemented RYB display name as configurable 
// completed Com port command validation on version 31.4


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.DropShadow;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.scene.layout.AnchorPane;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tasnetwork.calibration.conveyor.ConveyorPalletTracking;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.bay.configloader.TerminalBayConfigLoader;
import com.tasnetwork.calibration.conveyor.bay.ft.S070_opticalProbe_Scanning_of_Meters;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfigReader;
import com.tasnetwork.calibration.energymeter.constant.ConstantLduCcube;
import com.tasnetwork.calibration.energymeter.constant.ConstantMasterConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantMasterConfigReader;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceMte;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdRadiant;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdConfigLoader;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdKre;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.constant.ConstantReportV2;
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;
import com.tasnetwork.calibration.energymeter.constant.ConveyorConfigLoader;
import com.tasnetwork.calibration.energymeter.constant.ProCalCustomerConfiguration;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.custom1report.Custom1ReportConfigLoader;
import com.tasnetwork.calibration.energymeter.custom1report.Custom1ReportConfigModel;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
import com.tasnetwork.calibration.energymeter.database.MySQL_Interface;
import com.tasnetwork.calibration.energymeter.deployment.TextBoxDialog;
import com.tasnetwork.calibration.energymeter.device.*;
import com.tasnetwork.calibration.energymeter.project.ProjectController;
import com.tasnetwork.calibration.energymeter.reportprofile.PrintStyle;
import com.tasnetwork.calibration.energymeter.reportprofile.ReportProfileOperationConfigLoader;
import com.tasnetwork.calibration.energymeter.testprofiles.TestProfileType;
import com.tasnetwork.calibration.energymeter.testreport.config.ReportConfigLoader;
import com.tasnetwork.calibration.energymeter.util.ErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;
import com.tasnetwork.calibration.marked_for_delete.PrintStyleModel;
import com.tasnetwork.spring.config.ConfigLoader;
import com.tasnetwork.spring.config.MasterConfig;
import com.tasnetwork.spring.orm.model.AppConfig;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.ss.usermodel.IgnoredErrorType;
import org.apache.poi.ss.util.CellRangeAddress;
//import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
/*import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;*/


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.w3c.dom.Document;

/*import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;*/
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


//@EnableJpaAuditing
//@ComponentScan(basePackages = "com.tasnetwork")
@ComponentScan(basePackages = "com.tasnetwork.spring")
@EnableJpaRepositories(basePackages = "com.tasnetwork.spring.orm.repository")
@EntityScan(basePackages = "com.tasnetwork.spring.orm.model")
//@EnableJpaRepositories(basePackages = "com.tasnetwork.repository")
//@EnableJpaRepositories(basePackages = "com.tasnetwork.spring.orm")
//@SpringBootApplication
@EnableJpaAuditing
@SpringBootApplication(scanBasePackages = {"com.tasnetwork.calibration", "com.tasnetwork.spring.controller"})
//@SpringBootApplication(scanBasePackages = {"com.tasnetwork.calibration"})

public class ApplicationLauncher extends Application {

	//public static Logger logger = Logger.getLogger(ConstantVersion.APPLICATION_NAME );
	
    @Autowired
    private static ConfigLoader configLoader; 
    
	//public static final Logger logger = LogManager.getLogger(ApplicationLauncher.class);//ConstantVersion.APPLICATION_NAME);
	public static Logger logger = Logger.getLogger(ConstantVersion.APPLICATION_NAME );
	private static Stage primaryStage;
	public static boolean allready_running = false;
	public static boolean reportGenerationFlag = false;

	private Pane splashLayout;

	private static final int SPLASH_WIDTH = 480;
	private static final int SPLASH_HEIGHT = 360;
	private Stage mainStage;
	private WebView webView;
	private Boolean SplashFadeOut ;

	static{

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
		System.setProperty("current.date.time", dateFormat.format(new Date()));
		String version =ConstantVersion.APPLICATION_VERSION.replace(".", "_");
		System.out.println("Application version:"+version);
		System.setProperty("Version", "V"+version);
		System.setProperty("AppName", ConstantVersion.APPLICATION_NAME);
		//System.setProperty("java.net.preferIPv4Stack" , "true");
	}
	
    public static ConfigurableApplicationContext springContext;

	
/*	static{

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
		System.setProperty("current.date.time", dateFormat.format(new Date()));
		String version =ConstantVersion.APPLICATION_VERSION.replace(".", "_");
		System.out.println("Application version:"+version);
		System.setProperty("Version", "V"+version);
		System.setProperty("AppName", ConstantVersion.APPLICATION_NAME);
		
		System.out.println("AppName" + System.getProperty("AppName"));
		System.out.println("Version: " + System.getProperty("Version"));
		System.out.println("current.date.time: " + System.getProperty("current.date.time"));
		//System.setProperty("java.net.preferIPv4Stack" , "true");
	}*/
    
    @Override
    public void init() throws Exception {
        //logger.info("Initializing Spring Boot and JavaFX application...");
        String[] args = getParameters().getRaw().toArray(new String[0]);
        //springContext = SpringApplication.run(JavaFXSpringApp.class, args);
        //JavaFXSpringApp.configLoader = springContext.getBean(ConfigLoader.class);
        springContext = SpringApplication.run(ApplicationLauncher.class, args);
        configLoader = springContext.getBean(ConfigLoader.class);
    }
    
    @Override
    public void stop() throws Exception {
        logger.info("Shutting down Spring Boot and JavaFX application...");
        springContext.close();
    }

	public static void deleteLogFilesOlderThanNdays(long daysBack, String dirWay) {
		File directory = new File(dirWay);
		if(directory.exists()){

			File[] listFiles = directory.listFiles();           
			long purgeTime = System.currentTimeMillis() - (daysBack * 24 * 60 * 60 * 1000);
			for(File listFile : listFiles) {
				if(listFile.lastModified() < purgeTime) {
					if(!listFile.delete()) {
						System.err.println("Unable to delete file: " + listFile);
					}
				}
			}
		}

	}

	public static void handleException(Thread t, Throwable e) {
		logger.info("ApplicationLauncher : Unhandled Exception Entry: " + e);
		logger.info("ApplicationLauncher : Unhandled getname:"+t.getName());
		logger.info("ApplicationLauncher : Unhandled getMessage:"+e.getMessage());
		logger.info("ApplicationLauncher : Unhandled getStackTrace:"+e.getStackTrace());

		if (e instanceof IndexOutOfBoundsException || e instanceof ArrayIndexOutOfBoundsException) {
			boolean isUpdateCachedBoundsBug = Arrays.stream(e.getStackTrace()).anyMatch(
					s -> s.getClassName().startsWith("javafx.scene.Parent") && "updateCachedBounds".equals(s.getMethodName()));

			if (isUpdateCachedBoundsBug) {
				logger.info("ApplicationLauncher :Unhandled Exception : Detected an AIOBE or IOBE from the updateCachedBounds bug:");

				e.printStackTrace();
			} else {
				logger.info("ApplicationLauncher :Unhandled Exception : Detected an AIOBE or IOBE that is not an updateCachedBounds bug: " + e);
			}
		} else {
			logger.info("ApplicationLauncher : Unhandled Exception: " + e);
		}
		if (reportGenerationFlag){

			reportGenerationFlag = false;
			logger.info("ApplicationLauncher :Unhandled Exception : Report Generation failed with unknown error code " + e);
			setCursor(Cursor.DEFAULT);
			Platform.runLater(() -> {
				Alert alert = new Alert(AlertType.ERROR);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				//stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
				stage.getIcons().add(new Image(ApplicationLauncher.class.getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME)));
				alert.setTitle(ConstantVersion.APPLICATION_NAME );
				alert.setHeaderText("Report generation failed");
				String s = "Report Generation failed with unknown error code";
				alert.setContentText(s);
				alert.showAndWait();
			});
		}
		logger.info("ApplicationLauncher :Unhandled Exception : getStackTrace2:"+e.getStackTrace().toString());
		ByteArrayOutputStream out1 = new ByteArrayOutputStream();
		PrintStream out2 = new PrintStream(out1);
		e.printStackTrace(out2);
		try {
			String message = out1.toString("UTF8");
			logger.info("ApplicationLauncher :Unhandled Exception : printStackTrace1:"+ message);

		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		logger.info("ApplicationLauncher :Unhandled Exception : tostring: "+ e.toString()); 
		logger.info("ApplicationLauncher :Unhandled Exception : getSuperclass: "+ e.getClass().getSuperclass().getName()); 
		logger.info("ApplicationLauncher :Unhandled Exception : getName: "+ e.getClass().getName()); 
		logger.info("ApplicationLauncher :Unhandled Exception : getSimpleName: "+ e.getClass().getSimpleName()); 
		logger.info("ApplicationLauncher :Unhandled Exception : getCanonicalName: "+ e.getClass().getCanonicalName()); 

	}

	public void SplashInit() {


		//File file = new File("@../../images/procal-splash.png");
		//ImageView splash = new ImageView(new Image(file.toURI().toString()));
		//ImageView splash = new ImageView(new Image("file:images/"+ConstantVersion.SPLASH_FILENAME));
		ImageView splash = new ImageView(new Image(getClass().getResourceAsStream("/images/" + ConstantVersion.SPLASH_FILENAME)));

		splashLayout = new VBox();
		splashLayout.getChildren().addAll(splash);
		splashLayout.setStyle("-fx-padding: 5; -fx-background-color: lightblue; -fx-border-width:5; -fx-border-color: linear-gradient(to bottom, lightblue, derive(lightblue, 50%));");
		splashLayout.setEffect(new DropShadow());
	}

	private void showSplash(Stage splashStage) {
		SplashInit();
		Scene splashScene = new Scene(splashLayout);
		splashStage.initStyle(StageStyle.UNDECORATED);
		final Rectangle2D bounds = Screen.getPrimary().getBounds();
		splashStage.setScene(splashScene);
		splashStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH / 2);
		splashStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT / 2);
		splashStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME)));
		
		splashStage.centerOnScreen();
		splashStage.show();
		splashStage.toFront();
		FadeTransition fadeSplash = new FadeTransition(Duration.seconds(3.0), splashLayout);
		fadeSplash.setFromValue(0.0);
		fadeSplash.setToValue(1.0);
		SplashFadeOut = false;
		fadeSplash.setOnFinished(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent actionEvent) {
				logger.info("ApplicationLauncher : showSplash Entry0");
				if(SplashFadeOut){
					logger.info("ApplicationLauncher : showSplash Entry1");
					splashStage.hide();
					LoginPage();
				}else {
					try {
						logger.info("ApplicationLauncher : showSplash Entry2");
						Thread.sleep(2000);
						fadeSplash.setFromValue(1.0);
						fadeSplash.setToValue(0.0);
						fadeSplash.playFromStart();
						logger.info("ApplicationLauncher : showSplash Entry3");
						SplashFadeOut= true;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		fadeSplash.play();
		/*        try {
			Thread.sleep(3200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		//initStage.hide();
	}

	@Override public void start(Stage initStage) throws Exception{

		String version = ConstantVersion.APPLICATION_VERSION;
		//showSplash(initStage);
		//stage = new Stage(StageStyle.DECORATED);
		// webView.getEngine().documentProperty().addListener(new ChangeListener<Document>() {
		//     @Override public void changed(ObservableValue<? extends Document> observableValue, Document document, Document document1) {
		//       if (initStage.isShowing()) {
		/*loadProgress.progressProperty().unbind();
	            loadProgress.setProgress(1);
	            progressText.setText("All hobbits are full.");*/
		// mainStage.setIconified(false);
		//initStage.toFront();
		//FadeTransition fadeSplash = new FadeTransition(Duration.seconds(1.2), splashLayout);
		//fadeSplash.setFromValue(1.0);
		//fadeSplash.setToValue(0.0);
		// fadeSplash.setOnFinished(new EventHandler<ActionEvent>() {
		//   @Override public void handle(ActionEvent actionEvent) {
		//     initStage.hide();
		//    }
		//   });
		// fadeSplash.play();
		//Thread.sleep(3200);
		//stage.hide();
		// }
		// }
		// });
		initLogger();

		initStage = new Stage(StageStyle.DECORATED);
		//mainStage.setTitle("FX Experience");
		//initStage.setIconified(true);
		//stage.setIconified(true);


		logger.info("<------------"+ConstantVersion.APPLICATION_NAME +" Version "+version+"----LAUNCHED---------->\n");
		initStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override

			public void handle(WindowEvent e) {

				if(check_alerttype_btn()){
					logger.info("<------------ Spring App Context closing... ---------->\n");
					if(ProcalFeatureEnable.REPORT_GENERATION_V2_ENABLED){
						//DeviceDataManagerController.getSpringAppCtx().close();
						ApplicationLauncher.springContext.close();
					}
					logger.info("<------------ Spring App Context closed ---------->\n");
					//ctx.close();
					logger.info("<------------Exiting "+ConstantVersion.APPLICATION_NAME +" application---------->\n");
					Platform.exit();
					System.exit(0);
				}

				else{
					e.consume();
				}
			}
		});
		setPrimaryStage(initStage);
		//LiveTableDataManager.ListLiveDataInitListener();

		/*		TestDebugData(1);
		TestDebugData(120);
		TestDebugData(121);
		TestDebugData(3600);
		TestDebugData(3660);
		TestDebugData(5400);
		TestDebugData(5940);
		TestDebugData(5999);
		TestDebugData(6000);
		TestDebugData(7200);*/
		LoadConfigProperty();
		ReportConfigLoader.setConfigFilePathName(ConstantAppConfig.REPORT_CONFIG_FILE_PATH ,ConstantAppConfig.REPORT_CONFIG_FILE_NAME);
		ReportConfigLoader.init();
		logger.info("ApplicationLauncher : CONFIG_FILE_VERSION: " + ConstantVersion.CONFIG_FILE_VERSION);
		logger.info("ApplicationLauncher : REF_STD_MAX_OUTPUT_FREQ_IN_MEGA_HERTZ: " + ConstantRefStdRadiant.REF_STD_MAX_OUTPUT_FREQ_IN_MEGA_HERTZ);
		//String settingParam = Data_RefStdKre.getCommandWriteSettingParameter();
		//logger.info("ApplicationLauncher : settingParam: " + GUIUtils.hexToAscii(settingParam));
		//SerialDataRefStdKre test = new SerialDataRefStdKre(null);
		//test.parseSettingsSerialData();
		//String unparsedData = "4B52453A2F2F465245513D35302E3031333936332C55313D3130362E36313035332C5531323D3138332E30313233332C49313D322E38393432343431652D3030352C50313D2D302E30303036313833323037312C51313D2D302E303032303932323032372C53313D302E303032313839393731352C434F53313D2D302E323832333431392C61313D3235322E38343237362C6131323D3132302E30303837352C616C313D3238322E35343031322C616C31323D3132302E30303837352C4550313D302C4551313D302C4553313D302C55323D3130342E36373532342C5532333D3138322E34343435312C49323D332E37393037363231652D3030352C50323D2D302E303031303637343339312C51323D2D302E3030333136323539352C53323D302E303033333435343933332C434F53323D2D302E33313930363737372C61323D3235302E36393339342C6132333D3132302E313337312C616C323D39302E3031393930362C616C32333D3132302E313337312C4550323D302C4551323D302C4553323D302C55333D3130352E38363639322C5533313D3138332E38363935382C49333D362E38383631373136652D3030352C50333D2D302E303031313338323139362C51333D2D302E303036383631363137392C53333D302E303036393631323934382C434F53333D2D302E31363335303638392C61333D3235392E35373132362C6133313D3131392E3835";
		//test.parseBasicsSerialData(unparsedData);
		//RefStdKreMessage.setWriteSettingIstall(ConstantRefStdKre.CURRENT_MAX);
		//AssertValidation.bofaCommandPrint();
		//Data_PowerSource
		//AssertValidation.assertSignalGenReadRamLogic();
		//AssertValidation.Main_Master_Send_FB_Func();
		//AssertValidation.assertCalculateDegreeWithPf();
		//AssertValidation.assertCalculateGain();
		//AssertValidation.timeBasedAssert();
		//AssertValidation.assertSandsRefStdResponse();
		//AssertValidation.assertCalculateXorCheckSum();
		//AssertValidation.assertLduErrorDataHashMap2d();
		//AssertValidation.assertLscsFineTuneRphaseCurrent();
		//AssertValidation.AssertIEEE_754();
		//AssertValidation.AssertBinaryToBCD();
		//AssertValidation.AssertSetPulseConstant();
		//AssertValidation.AssertValidateDataForRSS_BNC_Constant();
		//AssertValidation.assertCheckSum();
		//AssertValidation.assertSlaveSendIntDataToPC();
		//AssertValidation.assertconvertTo2sComplement();
		//AssertValidation.assertSpringJpa();
		//AssertValidation.assertCurrentRangeSettingManualMode();
		//AssertValidation.assertPrintTestTimePeriod();
		AssertValidation.assertLicenceVerification();
		/*		Communicator x = null;
		SerialDataRefStdKiggs testObj = new SerialDataRefStdKiggs(x);
		testObj.parseAccumulatedActiveEnergyfromRefStd("A30113FC110102030405120607080910131122334455");
		testObj.parseAccumulatedReactiveEnergyfromRefStd("A30113FC110102030405120607080910131122334455");
		testObj.parseAccumulatedApparentEnergyfromRefStd("A30113FC110102030405120607080910131122334455");*/
		//AssertValidation.assertParseVoltageDatafromRefStd();
		
		
		/*for (int i= 1 ; i< 41; i++) {
			Data_LduBofa.frameReadErrorsCmd(i);
		}
		
		Data_LduBofa.frameDoubleCircuitSwitchCmd(1, "31");
		
		Data_LduBofa.frameDoubleCircuitSwitchCmd(1, "32");*/
		
		GuiUtils.FormatPulseRate( "2500000000") ;
		GuiUtils.FormatPulseRate( "125") ;
		deleteLogFilesOlderThanNdays(ConstantAppConfig.DeleteLogFilesforX_NoOfPreviousDays,"./logs/");
		//AssertValidation.AssertLagLead();
		boolean dbConnected = MySQL_Controller.ValidateDB_Schema_Exist();
		if(dbConnected){

			LoadPropertiesFromDB();

			assertNoOtherInstanceRunning();

			Thread.sleep(2000);
			if((!allready_running)  ){

				//ProcalFeatureEnable.Init();
				ProCalCustomerConfiguration.Init();
				ConstantApp.powerSourceInit();
				ConstantReport.ConstReportInit();
				//logger.debug("ApplicationLauncher: Test1");
				LoadReportHeaderConfigProperty();
				//logger.debug("ApplicationLauncher: Test2");
				LoadReportExcelConfigProperty();
				//logger.debug("ApplicationLauncher: Test3");
				LoadReportFileLocationProperty();
				//logger.debug("ApplicationLauncher: Test4");

				//LscsCalibrationConfigLoader.setLscsCalibrationFileName(ConstantAppConfig.LSCS_POWER_SOURCE_CALIBRATION_FILE_PATH ,ConstantAppConfig.LSCS_POWER_SOURCE_CALIBRATION_FILE_NAME);
				//logger.debug("ApplicationLauncher: Test5");
				ConstantRefStdConfigLoader.setRefStdConstantConfigFileName(ConstantAppConfig.REFSTD_CONSTANT_CONFIG_FILE_PATH, ConstantAppConfig.REFSTD_CONSTANT_CONFIG_FILE_NAME);
				//logger.debug("ApplicationLauncher: Test6");
				ReportProfileOperationConfigLoader.setConfigFilePathName(ConstantAppConfig.REPORT_PROFILEV2_FILE_PATH ,ConstantAppConfig.REPORT_PROFILEV2_FILE_NAME);

				
				TerminalBayConfigLoader.setConfigFilePathName(ConstantConveyorConfig.TERMINAL_CONFIG_FILE_PATH ,ConstantConveyorConfig.TERMINAL_CONFIG_FILE_NAME);
				TerminalBayConfigLoader.init();
				//String configFilePath = "src\\resources\\DevSysTerminalConfigV1_1.json";
				//DeviceDataManagerController.getMyConveyorTree().init(configFilePath);
				MySqlServiceManager.springDataInit();
				ConstantConveyor.init();
				
				
				if(ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {
					ConveyorDataManager.loadDeviceSettingFromDb();
				}
				
				ConstantRefStdConfigLoader.init();
				//logger.debug("ApplicationLauncher: Test7");
				loadRefStdConstantConfigProperty();
				//logger.debug("ApplicationLauncher: Test8");
				//LscsCalibrationConfigLoader.init();
				ConveyorPalletTracking.resetPositionToMeterSerialNoMapToDefaultMappings();

				ReportProfileOperationConfigLoader.init();
				//if(ProcalFeatureEnable.CONVEYOR_FEATURE_ENABLED) {
					ConveyorConfigLoader.setConveyorConfigFileName(ConstantAppConfig.CONVEYOR_CONFIG_FILE_PATH ,ConstantAppConfig.CONVEYOR_CONFIG_FILE_NAME);
					ConveyorConfigLoader.init();
					ApplicationLauncher.logger.info("Conveyor: getMaxNoOfDutSupported: " + DeviceDataManagerController.getConveyorConfigParsedKey().getMaxDutSupported());
				
					
					
					ApplicationLauncher.logger.info("Conveyor: getDutHardwareIdInitialValue: " + DeviceDataManagerController.getConveyorConfigParsedKey().getDutHardwareIdInitialValue());
					
					
					//}
				if(ProcalFeatureEnable.REPORT_GENERATION_V2_ENABLED){
					DeviceDataManagerController.springDataInit();
					appConfigInit();
				}else{
					//DeviceDataManagerController.getSpringAppCtx().close();
					//JavaFXSpringApp.springContext.close();
				}

				try{
					if(ConstantAppConfig.REPORT_PROFILE_CONFIG_PATH_LIST.size()>1){
						Custom1ReportConfigLoader.setConfigFilePathName(ConstantAppConfig.REPORT_PROFILE_PATH ,ConstantAppConfig.REPORT_PROFILE_CONFIG_PATH_LIST.get(1));
						Custom1ReportConfigLoader.init();
					}else{
						ApplicationLauncher.logger.info("start: custome report profile not loaded!"); 
					}
				}catch (Exception e) {

					e.printStackTrace();
					ApplicationLauncher.logger.error("start: Exception: "+e.getMessage());


				}
				
				BayUtils.init();
				/*BayUtils_Cluster2.init();*/
				//AssertValidation.assertGetTargetVoltageRms();
				//AssertValidation.assertGetTargetCurrentRms();
				//AssertValidation.assertCalculateDegreeWithPf();
				//ConstantFeatureEnable.Init();
				//ConstantReport.ConstReportInit();
				//AssertValidation.dutCommandTesting();
				//AssertValidation.assertGenerateNextNewHardwareIds();
				//AssertValidation.fetchPalletsByBayState();
				Thread.setDefaultUncaughtExceptionHandler(ApplicationLauncher::handleException);
				boolean systemStatus = false;
				boolean systemStatusExceptionOccured = false;
				try{
					systemStatus = LoadSystemTime();

				}catch (Exception e) {

					e.printStackTrace();
					ApplicationLauncher.logger.error("start: system status Exception: "+e.getMessage());
					systemStatusExceptionOccured = true;


				}
				if(systemStatus){

					SplashAndLoginDisplay();
					//LoginPage();
				}
				else{

					if(ProcalFeatureEnable.LICENSE_FEATURE_DISPLAY_ENABLED){



						Alert alert = new Alert(AlertType.CONFIRMATION);
						Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
						//stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME));
						stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME)));
						alert.setTitle(ConstantVersion.APPLICATION_NAME);
						alert.setHeaderText(ErrorCodeMapping.ERROR_CODE_3002);
						alert.setContentText(ErrorCodeMapping.ERROR_CODE_3002_MSG);

						if(systemStatusExceptionOccured){
							alert.setHeaderText(ErrorCodeMapping.ERROR_CODE_3003);
							alert.setContentText(ErrorCodeMapping.ERROR_CODE_3003_MSG);	
						}

						alert.getButtonTypes().clear();
						alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

						// Deactivate Defaultbehavior for yes-Button:
						Button yesButton = (Button) alert.getDialogPane().lookupButton(ButtonType.YES);
						yesButton.setDefaultButton(true);

						// Activate Defaultbehavior for no-Button:
						Button noButton = (Button) alert.getDialogPane().lookupButton(ButtonType.NO);
						noButton.setDefaultButton(false);

						final Optional<ButtonType> result = alert.showAndWait();
						if(result.get() == ButtonType.YES){
							licenseHandlePage();
						}else{
							logger.info("<------------Exit "+ConstantVersion.APPLICATION_NAME +" application with error code 00---------->\n");
							Platform.exit();
							System.exit(0);
						}

					}else{

						Alert alert = new Alert(AlertType.ERROR);
						Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
						//stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME));
						stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME)));
						alert.setTitle(ConstantVersion.APPLICATION_NAME );
						alert.setHeaderText(ErrorCodeMapping.ERROR_CODE_3001);
						String s = ErrorCodeMapping.ERROR_CODE_3001_MSG;
						alert.setContentText(s);
						alert.showAndWait();
						logger.info("<------------Exit "+ConstantVersion.APPLICATION_NAME +" application with error code 0---------->\n");
						Platform.exit();
						System.exit(0);
					}

				}


			}
			else{

				Alert alert = new Alert(AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				//stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME));
				stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME)));
				alert.setTitle(ConstantVersion.APPLICATION_NAME );
				String s = ConstantVersion.APPLICATION_NAME +" already running";
				alert.setContentText(s);


				alert.showAndWait();
				logger.info("<------------Exit "+ConstantVersion.APPLICATION_NAME +" application---------->\n");
				Platform.exit();
				System.exit(0);

			}
		}else{
			if(!MySQL_Interface.bDB_SchemaExist){
				Install_New_schema();
			}else{

				logger.info("<------------Exit "+ConstantVersion.APPLICATION_NAME +" application exit with errorcode 1\n---------->\n");
				Platform.exit();
				System.exit(1);
			}
		}
	}

	private void appConfigInit() {
		// TODO Auto-generated method stub
		String customerId = "0";
		String propertyName = "ReportTestExecutionFilterByEndDate";
		Optional<AppConfig> appConfigDataOpt = DeviceDataManagerController.getAppConfigService().findFirstByCustomerIdAndPropertyName(customerId, propertyName);
		if(appConfigDataOpt.isPresent()) {
			AppConfig appConfigData = appConfigDataOpt.get();
			 String result  = appConfigData.getPropertyValue();
			 if( (result.equalsIgnoreCase("yes")) || (result.equalsIgnoreCase("y")) ) {
				// ConstantAppConfig.REPORT_TEST_EXECUTION_FILTER_BY_END_DATE = true; 
			 }
			 
		}
	
	}

	public static void setCursor(Cursor value){
		logger.info("ApplicationLauncher : setCursor: Entry: Value: " + value);
		getPrimaryStage().getScene().setCursor(value);

	}



	public static void InformUser(String title, String info,AlertType Alert_type){
		TextBoxDialog TextBoxDialogobj = new TextBoxDialog();
		TextBoxDialogobj.TriggerUserInfoPlatFormLater(title, info, Alert_type);
	}


	public boolean check_alerttype_btn(){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		//stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME));
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME)));
		alert.setTitle(ConstantVersion.APPLICATION_NAME +" Exit");
		String s = "Are you sure, you want to exit?";
//		if(ProjectExecutionController.getExecutionInProgress()){
//			s = "Test Execution is still in progress. Kindly stop execution or wait until execution is completed";
//			alert = new Alert(AlertType.INFORMATION);
//			stage = (Stage) alert.getDialogPane().getScene().getWindow();
//			stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME)));
//			alert.setTitle(ConstantVersion.APPLICATION_NAME +" Exit");
//		}
		alert.setContentText(s);
		boolean check_test_run=false;
		Optional<ButtonType> result = alert.showAndWait();


		if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
//			if(!ProjectExecutionController.getExecutionInProgress()){
//				check_test_run=true;
//			}
		}
		/*else{
			check_test_run=false;

		}*/
		return check_test_run;
	}

	public static void assertNoOtherInstanceRunning() {    

		new Thread(() -> { 
			try {


				new ServerSocket(ConstantAppConfig.APP_INSTANCE_SERVER_PORT).accept();


			} catch (IOException e) {

				allready_running = true;
				ApplicationLauncher.logger.error("assertNoOtherInstanceRunning: APP_INSTANCE_SERVER_PORT: "+ ConstantAppConfig.APP_INSTANCE_SERVER_PORT);
				ApplicationLauncher.logger.error("assertNoOtherInstanceRunning: IOException: "+e.getMessage());


			}
		}).start();  
	}


	public  void Install_New_schema() {


		ApplicationLauncher.logger.info("Install_New_schema: Entry");
		Alert alert = new Alert(AlertType.ERROR);
		Stage stage1 = (Stage) alert.getDialogPane().getScene().getWindow();
		stage1.getIcons().add(new Image(getClass().getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME)));
		alert.setTitle("Database connection failed");

		String sqlURL=ConstantAppConfig.DB_URL+ConstantAppConfig.DB_NAME;
		String DBname=(sqlURL.substring(sqlURL.lastIndexOf("/") + 1));
		alert.setContentText("Database schema <" +DBname+"> does not exist\nDo you want to install schema?");
		alert.getButtonTypes().clear();
		alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

		Optional<ButtonType> result = alert.showAndWait();

		if ((result.isPresent()) && (result.get() == ButtonType.YES)) {
			ApplicationLauncher.logger.info("Install_New_schema :button ok pressed!!");
			Parent root = null;
			Stage stage = getPrimaryStage();

			try {


				root = FXMLLoader.load(getClass().getResource("/fxml/main/Install_schema" + ConstantApp.THEME_FXML));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("Install_New_schema : Exception:"+e.getMessage());
			}


			Scene scene = new Scene(root);
			stage.setScene(scene);
			scene.setFill(Color.TRANSPARENT);
			stage.setTitle(ConstantVersion.APPLICATION_NAME + " - "+ConstantVersion.APPLICATION_VERSION);
			stage.show();

		}else{
			ApplicationLauncher.logger.info("Install_New_schema :button <No> pressed!!");
		}





	}

	public void SplashAndLoginDisplay(){



		Stage stage1 = new Stage();

		showSplash(stage1);

	}

	public void LoginPage(){


		Parent root = null;
		Stage stage = getPrimaryStage();


		try {
			root = FXMLLoader.load(getClass().getResource("/fxml/main/LoginPage" + ConstantApp.THEME_FXML));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("LoginPage: IOException: "+e.getMessage());
		}
		//showSplash(stage);
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME)));
		stage.setMinWidth(300);
		stage.setMinHeight(200);
		//stage.hide();
		//stage.initStyle(StageStyle.DECORATED);
		//stage.setIconified(true);

		Scene scene = new Scene(root);
		//stage.setScene(scene);
		scene.setFill(Color.TRANSPARENT);
		stage.setTitle(ConstantVersion.APPLICATION_NAME + " - "+ConstantVersion.APPLICATION_VERSION);
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
		//stage.setAlwaysOnTop(true);
		stage.toFront();

	}


	public void licenseHandlePage(){


		Parent root = null;
		Stage stage = getPrimaryStage();


		try {
			//root = FXMLLoader.load(getClass().getResource("/fxml/main/LicenseHandle" + ConstantApp.THEME_FXML));
			root = FXMLLoader.load(getClass().getResource("/fxml/main/LicenseHandle" + ConstantApp.THEME_FXML));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("licenseHandlePage: IOException: "+e.getMessage());
		}
		//showSplash(stage);
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME)));
		stage.setMinWidth(300);
		stage.setMinHeight(200);
		//stage.hide();
		//stage.initStyle(StageStyle.DECORATED);
		//stage.setIconified(true);

		Scene scene = new Scene(root);
		//stage.setScene(scene);
		scene.setFill(Color.TRANSPARENT);
		stage.setTitle(ConstantVersion.APPLICATION_NAME + " - "+ConstantVersion.APPLICATION_VERSION);
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
		//stage.setAlwaysOnTop(true);
		stage.toFront();

	}


	/*public void licenseHandlePage(){
		ApplicationLauncher.logger.info("licenseHandlePage: Entry");	
		//ApplicationLauncher.logger.info("MeterReadingPopup: entry");	
		 FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main/LicenseHandle" + ConstantApp.THEME_FXML));

	        Scene newScene;
	        try {
	            newScene = new Scene(loader.load());
	        } catch (IOException ex) {
	            // TODO: handle error
	        	ex.printStackTrace();
				ApplicationLauncher.logger.error("licenseHandlePage :IOException:"+ ex.getMessage());
	            return;
	        }

	        Stage licenseHandleStage = new Stage();
	        //https://stackoverflow.com/questions/36071779/how-to-open-an-additional-window-in-a-javafx-fxml-app?rq=1

	        Stage primaryStage = ApplicationLauncher.getPrimaryStage();

	        licenseHandleStage.setTitle(ConstantVersion.APPLICATION_NAME + " - "+ConstantVersion.APPLICATION_VERSION);
	        licenseHandleStage.initModality(Modality.NONE);
	        licenseHandleStage.initOwner(primaryStage);
	        //https://stackoverflow.com/questions/38481914/disable-background-stage-javafx?rq=1

	        licenseHandleStage.setScene(newScene);
	        //Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
	        licenseHandleStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME));
	        licenseHandleStage.setAlwaysOnTop(true);
	        licenseHandleStage.setOnCloseRequest(e->e.consume());
	        licenseHandleStage.showAndWait();

	}

	 */

	public void CustomDialogInit() {



		Alert alert = new Alert(AlertType.WARNING);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME)));
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText("This is a Custom Confirmation Dialog");
		alert.setContentText("We override the style classes of the dialog");

		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(
				getClass().getResource("/resources/myDialogs.css").toExternalForm());
		dialogPane.getStyleClass().add("/resources/myDialog");

		alert.show();
	}

	void initLogger() {


		//String log4jConfigFile = "/resources/log4j.properties";
		String log4jConfigFile = "log4j.properties";
		try{
			//PropertyConfigurator.configure(getClass().getResource(log4jConfigFile));
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(log4jConfigFile);
			if (inputStream != null) {
	            Properties props = new Properties();
	            props.load(inputStream);
	            PropertyConfigurator.configure(props);
	        } else {
	            throw new FileNotFoundException("log4j.properties not found in classpath");
	        }
		
		}catch (Exception e){
			e.printStackTrace();
			System.out.println("initLogger: Exception:" + e.getMessage());
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME)));
			alert.setTitle(ConstantVersion.APPLICATION_NAME );
			alert.setHeaderText("Error in log file");
			String s = "Exception while creating log file \n" + e.getMessage();
			alert.setContentText(s);

			alert.showAndWait();
			logger.info("<------------Exit "+ConstantVersion.APPLICATION_NAME +" application with error code 2---------->\n");
			Platform.exit();
			System.exit(2);

		}

	}


	public static Stage getPrimaryStage() {
		return primaryStage;
	}




	private void setPrimaryStage(Stage pStage) {
		ApplicationLauncher.primaryStage = pStage;
	}

	public void EnableFileLog(){
		Date date = new Date() ;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
		File Outfile = new File("std.out_"+dateFormat.format(date) + ".txt") ;
		File Errorfile = new File("std.Error_"+dateFormat.format(date) + ".txt") ;

		try {

			System.setOut(new PrintStream(Outfile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("EnableFileLog: FileNotFoundException1: "+e.getMessage());
		}

		try {
			System.setErr(new PrintStream(Errorfile));

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("EnableFileLog: FileNotFoundException2: "+e.getMessage());
		}

	}

	private void setBoundaries(Stage stage) {
		stage.setWidth(1120);
		stage.setHeight(810);
		stage.setResizable(false);
	}

	private void loadDevicesPage(Scene scene) throws IOException{
		logger.debug("<--LOADING THE DEVICE PAGE--->\n");
		AnchorPane childpane = (AnchorPane)scene.lookup("#childPane");

		childpane.getChildren().add(FXMLLoader.load(getClass().getResource("/fxml/setting/SystemSetting" + ConstantApp.THEME_FXML)));


	}



	public void TestDebugData(Integer CreepTimeInSec){
		/*int LDU_ReadAddress = 10;
		ApplicationLauncher.logger.debug("LDU_SendCommandReadErrorData :LDU_ReadAddress:"+LDU_ReadAddress);
		ApplicationLauncher.logger.debug("LDU_SendCommandReadErrorData :LDU_ReadAddress:Hex:"+String.format("%02x" , LDU_ReadAddress).toUpperCase());

		ApplicationLauncher.logger.debug("LDU_SendCommandReadErrorData :LDU_ReadAddress:"+ConstantLDU.DecodeHextoString(String.format("%02d" , LDU_ReadAddress)));
		 */
		//Integer CreepTimeInSec = 3600+60;
		ApplicationLauncher.logger.info("test :Entry");	
		int sec = (CreepTimeInSec % 60);
		int min = CreepTimeInSec/60;
		if(min >99){
			min =0;
		}
		//int min =  CreepTimeInSec/60;
		/*int hours = CreepTimeInSec / 3600;
		if (hours == 1){
			min = min +60;
		}*/
		//int min = ((CreepTimeInSec / 60)%60);
		//int min  = ((CreepTimeInSec % 86400 ) % 3600 ) / 60 ;
		//long min = TimeUnit.SECONDS.toMinutes(CreepTimeInSec) - (TimeUnit.SECONDS.toHours(CreepTimeInSec)* 60);
		//long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);

		ApplicationLauncher.logger.info("test :sec:"+sec);	
		ApplicationLauncher.logger.info("test :min:"+min);	
		String CreepTimeDuration = String.format("%02d", min)  + String.format("%02d", sec);
		ApplicationLauncher.logger.info("test :CreepTimeDuration:"+CreepTimeDuration);

	}



	public static void main(String[] args) {
		launch(args);
	}

	public static void loadRefStdConstantConfigProperty(){

		ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_1 = ConstantRefStdConfigLoader.getFloat("RefStdDeviceConstant", "RSS_HTCT_CurrentThresholdInAmpsLevel_1");
		ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_2 = ConstantRefStdConfigLoader.getFloat("RefStdDeviceConstant", "RSS_HTCT_CurrentThresholdInAmpsLevel_2");
		ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_3 = ConstantRefStdConfigLoader.getFloat("RefStdDeviceConstant", "RSS_HTCT_CurrentThresholdInAmpsLevel_3");
		ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_4 = ConstantRefStdConfigLoader.getFloat("RefStdDeviceConstant", "RSS_HTCT_CurrentThresholdInAmpsLevel_4");
		ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_5 = ConstantRefStdConfigLoader.getFloat("RefStdDeviceConstant", "RSS_HTCT_CurrentThresholdInAmpsLevel_5");
		ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_6 = ConstantRefStdConfigLoader.getFloat("RefStdDeviceConstant", "RSS_HTCT_CurrentThresholdInAmpsLevel_6");
		ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_7 = ConstantRefStdConfigLoader.getFloat("RefStdDeviceConstant", "RSS_HTCT_CurrentThresholdInAmpsLevel_7");
		ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_8 = ConstantRefStdConfigLoader.getFloat("RefStdDeviceConstant", "RSS_HTCT_CurrentThresholdInAmpsLevel_8");
		ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_9 = ConstantRefStdConfigLoader.getFloat("RefStdDeviceConstant", "RSS_HTCT_CurrentThresholdInAmpsLevel_9");
		ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_10 = ConstantRefStdConfigLoader.getFloat("RefStdDeviceConstant", "RSS_HTCT_CurrentThresholdInAmpsLevel_10");
		ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_11 = ConstantRefStdConfigLoader.getFloat("RefStdDeviceConstant", "RSS_HTCT_CurrentThresholdInAmpsLevel_11");
		ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_12 = ConstantRefStdConfigLoader.getFloat("RefStdDeviceConstant", "RSS_HTCT_CurrentThresholdInAmpsLevel_12");


		ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_1    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_1");
		ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_2    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_2");
		ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_3    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_3");
		ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_4    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_4");
		ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_5    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_5");
		ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_6    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_6");
		ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_7    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_7");
		ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_8    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_8");
		ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_9    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_9");
		ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_10    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_10");
		ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_11    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_11");
		ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_12    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_12");


		ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_BELOW_LEVEL_12             = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_HTCT_ActivePulseConstantInImpPerWh_BelowLevel_12");

		ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_1  = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_1");
		ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_2  = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_2");
		ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_3  = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_3");
		ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_4  = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_4");
		ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_5  = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_5");
		ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_6  = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_6");
		ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_7  = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_7");
		ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_8  = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_8");
		ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_9  = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_9");
		ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_10  = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_10");
		ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_11  = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_11");
		ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_12  = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_12");

		ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_BELOW_LEVEL_12           = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_HTCT_ReactivePulseConstantInImpPerWh_BelowLevel_12");



		ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_1 = ConstantRefStdConfigLoader.getFloat("RefStdDeviceConstant", "RSS_LTCT_CurrentThresholdInAmpsLevel_1");
		ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_2 = ConstantRefStdConfigLoader.getFloat("RefStdDeviceConstant", "RSS_LTCT_CurrentThresholdInAmpsLevel_2");
		ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_3 = ConstantRefStdConfigLoader.getFloat("RefStdDeviceConstant", "RSS_LTCT_CurrentThresholdInAmpsLevel_3");
		ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_4 = ConstantRefStdConfigLoader.getFloat("RefStdDeviceConstant", "RSS_LTCT_CurrentThresholdInAmpsLevel_4");
		ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_5 = ConstantRefStdConfigLoader.getFloat("RefStdDeviceConstant", "RSS_LTCT_CurrentThresholdInAmpsLevel_5");
		ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_6 = ConstantRefStdConfigLoader.getFloat("RefStdDeviceConstant", "RSS_LTCT_CurrentThresholdInAmpsLevel_6");
		ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_7 = ConstantRefStdConfigLoader.getFloat("RefStdDeviceConstant", "RSS_LTCT_CurrentThresholdInAmpsLevel_7");
		ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_8 = ConstantRefStdConfigLoader.getFloat("RefStdDeviceConstant", "RSS_LTCT_CurrentThresholdInAmpsLevel_8");
		ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_9 = ConstantRefStdConfigLoader.getFloat("RefStdDeviceConstant", "RSS_LTCT_CurrentThresholdInAmpsLevel_9");
		ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_10 = ConstantRefStdConfigLoader.getFloat("RefStdDeviceConstant", "RSS_LTCT_CurrentThresholdInAmpsLevel_10");
		ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_11 = ConstantRefStdConfigLoader.getFloat("RefStdDeviceConstant", "RSS_LTCT_CurrentThresholdInAmpsLevel_11");
		ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_12 = ConstantRefStdConfigLoader.getFloat("RefStdDeviceConstant", "RSS_LTCT_CurrentThresholdInAmpsLevel_12");


		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_1     = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_1");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_2     = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_2");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_3     = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_3");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_4     = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_4");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_5     = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_5");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_6     = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_6");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_7     = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_7");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_8     = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_8");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_9     = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_9");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_10     = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_10");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_11     = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_11");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_12     = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_12");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_BELOW_OR_EQUAL_LEVEL_12      = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentBelowLevel_12");


		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_1     = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_1");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_2    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_2");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_3    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_3");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_4    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_4");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_5    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_5");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_6    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_6");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_7    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_7");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_8    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_8");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_9    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_9");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_10    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_10");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_11    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_11");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_12    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_12");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_BELOW_OR_EQUAL_LEVEL_12             = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentBelowLevel_12");

		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_1    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_1");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_2    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_2");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_3    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_3");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_4    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_4");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_5    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_5");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_6    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_6");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_7    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_7");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_8    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_8");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_9    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_9");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_10    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_10");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_11    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_11");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_12    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_12");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_BELOW_OR_EQUAL_LEVEL_12             = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentBelowLevel_12");



		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_1    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_1");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_2    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_2");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_3    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_3");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_4    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_4");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_5    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_5");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_6    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_6");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_7    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_7");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_8    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_8");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_9    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_9");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_10    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_10");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_11    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_11");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_12    = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_12");
		ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_BELOW_OR_EQUAL_LEVEL_12  			= ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentBelowLevel_12");


		ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_1  = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_1");
		ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_2  = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_2");
		ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_3  = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_3");
		ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_4  = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_4");
		ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_5  = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_5");
		ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_6  = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_6");
		ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_7  = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_7");
		ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_8  = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_8");
		ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_9  = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_9");
		ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_10  = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_10");
		ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_11  = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_11");
		ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_12  = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_12");

		ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_BELOW_LEVEL_12           = ConstantRefStdConfigLoader.getString("RefStdDeviceConstant", "RSS_LTCT_ReactivePulseConstantInImpPerWh_BelowLevel_12");


	}

	public static void LoadConfigProperty(){
		
		MasterConfig masterConfig = configLoader.getMasterConfig();
		ApplicationLauncher.logger.info("LoadConfigProperty: getConfigFileVersion: " +masterConfig.getConfigFileVersion() );
		try{
			ConstantMasterConfig.APP_CONFIG_CUSTOMER_FILE_NAME = masterConfig.getCustomerDetails().getAppConfigFileName();//ConstantMasterConfigReader.getString("CustomerDetails", "AppConfigFileName");
			ApplicationLauncher.logger.info("LoadConfigProperty : APP_CONFIG_CUSTOMER_FILE_NAME: "+ ConstantMasterConfig.APP_CONFIG_CUSTOMER_FILE_NAME);

			ConstantMasterConfig.CONFIG_FILE_PATH = masterConfig.getCustomerDetails().getConfigFilePath();//ConstantMasterConfigReader.getString("CustomerDetails", "ConfigFilePath");
			ApplicationLauncher.logger.info("LoadConfigProperty : CONFIG_FILE_PATH: "+ ConstantMasterConfig.CONFIG_FILE_PATH);


		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("LoadConfigProperty : Exception1"+ e.getMessage());


		}
		/*ConstantMasterConfigReader.init();
		try{
			ConstantMasterConfig.APP_CONFIG_CUSTOMER_FILE_NAME = ConstantMasterConfigReader.getString("CustomerDetails", "AppConfigFileName");
			ApplicationLauncher.logger.info("LoadConfigProperty : APP_CONFIG_CUSTOMER_FILE_NAME: "+ ConstantMasterConfig.APP_CONFIG_CUSTOMER_FILE_NAME);

			ConstantMasterConfig.CONFIG_FILE_PATH = ConstantMasterConfigReader.getString("CustomerDetails", "ConfigFilePath");
			ApplicationLauncher.logger.info("LoadConfigProperty : CONFIG_FILE_PATH: "+ ConstantMasterConfig.CONFIG_FILE_PATH);


		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("LoadConfigProperty : Exception1"+ e.getMessage());


		}*/
		ConstantAppConfigReader.setAppConfigFilePathName(ConstantMasterConfig.APP_CONFIG_CUSTOMER_FILE_NAME);
		ApplicationLauncher.logger.info("LoadConfigProperty : ConstantAppConfigReader.getAppConfigFilePathName: "+ ConstantAppConfigReader.getAppConfigFilePathName());
		ConstantAppConfigReader.init();

		try{
			ConstantAppConfig.DB_URL = ConstantAppConfigReader.getString("database", "url"); 
			ConstantAppConfig.DB_URL_TAIL_OPTION = ConstantAppConfigReader.getString("database", "urlTailOption"); 		
			ConstantAppConfig.DB_USERNAME = ConstantAppConfigReader.getString("database", "username"); 
			ConstantAppConfig.DB_PASSWORD = ConstantAppConfigReader.getString("database", "password"); 
			//ConstantConfig.DB_NAME = ConstantConfigReader.getString("database", "url"); 
			ConstantAppConfig.DB_NAME = ConstantAppConfigReader.getString("database", "db_name"); 
			ConstantAppConfig.SQL_LOCATION = ConstantAppConfigReader.getString("database", "DefaultMySQL_InstalledLocation");
			ConstantAppConfig.SQL_BACKUP_LOCATION = ConstantAppConfigReader.getString("database", "DefaultBackupLocation");
			ConstantAppConfig.POWERSRC = ConstantAppConfigReader.getString("app", "PowerSrc"); 
			ConstantAppConfig.REFSTD = ConstantAppConfigReader.getString("app", "RefStd");   
			ConstantAppConfig.LDU = ConstantAppConfigReader.getString("app", "LDU"); 
			ConstantAppConfig.ICT = ConstantAppConfigReader.getString("app", "ICT"); 
			ConstantAppConfig.HARMONICS_SRC = ConstantAppConfigReader.getString("app", "HarmonicsSrc"); 
			ConstantAppConfig.ScreenWidthThreshold = ConstantAppConfigReader.getLong("app","ScreenWidthThreshold");
			ConstantAppConfig.ScreenHeightThreshold =  ConstantAppConfigReader.getLong("app","ScreenHeightThreshold");  
			ConstantAppConfig.DeleteLogFilesforX_NoOfPreviousDays =  ConstantAppConfigReader.getLong("app","HoldLogFilesforX_NoOfPreviousDays"); 
			ConstantAppConfig.DEPLOYMENT_DB_SEARCH_MAX_TIME_LIMIT_IN_DAYS = ConstantAppConfigReader.getLong("app", "DeploymentDbSearchMaxTimeLimitInDays");

			ConstantAppConfig.REPORT_DATE_FORMAT = ConstantAppConfigReader.getString("Report", "ReportDateFormat");
			ConstantAppConfig.REPORT_TIME_FORMAT = ConstantAppConfigReader.getString("Report", "ReportTimeFormat");
			ConstantAppConfig.REPORT_TIME_ZONE = ConstantAppConfigReader.getString("Report", "ReportTimeZone");

			ConstantAppConfig.REPORT_TOTAL_NO_OF_PAGES = ConstantAppConfigReader.getString("Report", "ReportTotalNumberOfPages");
			ConstantAppConfig.METER_PROFILE_REPORT_PAGE_NUMBER = ConstantAppConfigReader.getString("Report", "MeterProfileReportPageNumber");
			ConstantAppConfig.METER_PROFILE_REPORT_ENABLED = ConstantAppConfigReader.getBoolean("Report", "MeterProfileReportGenerationEnabled");
			ConstantAppConfig.POWERONWAITCOUNTER = ConstantAppConfigReader.getInt("StablizationValidation", "PowerSrcValidationWaitTimeInSec");
			ConstantAppConfig.SKIP_TP_TIME_INSEC = ConstantAppConfigReader.getInt("StablizationValidation", "SkipTestPointTimeInSec"); 
			ConstantAppConfig.LTCT_VOLT_MIN = ConstantAppConfigReader.getFloat("AcceptedVoltageRange", "LTCT_VoltMin"); 
			ConstantAppConfig.LTCT_VOLT_MAX = ConstantAppConfigReader.getFloat("AcceptedVoltageRange", "LTCT_VoltMax"); 
			ConstantAppConfig.LTCT_CURRENT_MIN = ConstantAppConfigReader.getFloat("AcceptedCurrentRange", "LTCT_CurrentMin");  
			ConstantAppConfig.LTCT_CURRENT_MAX = ConstantAppConfigReader.getFloat("AcceptedCurrentRange", "LTCT_CurrentMax"); 
			//ConstantAppConfig.LTCT_CURRENT_IMAX_MAX = ConstantAppConfigReader.getFloat("AcceptedCurrentRange", "LTCT_CurrentImax_Max"); 

			ConstantAppConfig.HTCT_VOLT_MIN = ConstantAppConfigReader.getFloat("AcceptedVoltageRange", "HTCT_VoltMin"); 
			ConstantAppConfig.HTCT_VOLT_MAX = ConstantAppConfigReader.getFloat("AcceptedVoltageRange", "HTCT_VoltMax"); 
			ConstantAppConfig.HTCT_CURRENT_MIN = ConstantAppConfigReader.getFloat("AcceptedCurrentRange", "HTCT_CurrentMin");  
			ConstantAppConfig.HTCT_CURRENT_MAX = ConstantAppConfigReader.getFloat("AcceptedCurrentRange", "HTCT_CurrentMax"); 
			//ConstantAppConfig.HTCT_CURRENT_IMAX_MAX = ConstantAppConfigReader.getFloat("AcceptedCurrentRange", "HTCT_CurrentImax_Max"); 

			ConstantAppConfig.TIME_MIN = ConstantAppConfigReader.getInt("AcceptedCreepTimeRange", "TimeMin");
			ConstantAppConfig.TIME_MAX = ConstantAppConfigReader.getInt("AcceptedCreepTimeRange", "TimeMax");
			ConstantAppConfig.DEGREE_MIN = ConstantAppConfigReader.getInt("AcceptedDegreeRange", "DegreeMin"); 
			ConstantAppConfig.DEGREE_MAX = ConstantAppConfigReader.getInt("AcceptedDegreeRange", "DegreeMax"); 
			ConstantAppConfig.FREQUENCY_MIN = ConstantAppConfigReader.getInt("AcceptedFrequencyRange", "FrequencyMin");
			ConstantAppConfig.FREQUENCY_MAX = ConstantAppConfigReader.getInt("AcceptedFrequencyRange", "FrequencyMax"); 
			ConstantAppConfig.CMD_PWR_SRC_PHASE_RY_NORMAL = ConstantAppConfigReader.getString("PhaseReversal", "NormalRY"); 
			ConstantAppConfig.CMD_PWR_SRC_PHASE_RB_NORMAL = ConstantAppConfigReader.getString("PhaseReversal", "NormalRB"); 
			ConstantAppConfig.CMD_PWR_SRC_PHASE_RY_PHASEREV = ConstantAppConfigReader.getString("PhaseReversal", "PhaseReversalRY");
			ConstantAppConfig.CMD_PWR_SRC_PHASE_RB_PHASEREV = ConstantAppConfigReader.getString("PhaseReversal", "PhaseReversalRB"); 
			//ConstantConfig.I_MAPPING_SIZE = (long) ConstantConfigReader.getAttribute("IMappingSize");
			//ConstantConfig.PF_MAPPING_SIZE = (long) ConstantConfigReader.getAttribute("PFMappingSize");
			ConstantAppConfig.I_MAPPING_SIZE = ProcalFeatureEnable.I_MAPPING_SIZE;
			ConstantAppConfig.PF_MAPPING_SIZE = ProcalFeatureEnable.PF_MAPPING_SIZE;
			ConstantAppConfig.I_MAPPING_DEFAULT_VALUES = getIMappingDefaultValues();
			////*************When PF_MAPPING_DEFAULT_VALUES functionality is complemented... delete below three lines
			/*		ConstantConfig.ABC_PF_VALUES = getABC_PF_Values();//PFMappingToDO
		ConstantConfig.A_B_C_PF_VALUES = getA_B_C_PF_Values();//PFMappingToDO
		ConstantConfig.PF_MAPPING_ROW_HEADER = getPF_RowHeaderValues();//PFMappingToDO
			 */		////*************When PF_MAPPING_DEFAULT_VALUES functionality is complemented... delete above three lines
			ConstantAppConfig.PF_MAPPING_DEFAULT_VALUES = getPF_MappingDefaultValues();
			//ConstantConfig.PF_MAPPING_SIZE = (long) ConstantConfigReader.getAttribute("PFMappingSize");
			//ConstantConfig.REF_STD_PHASE_180_FLAG = ConstantConfigReader.getBoolean("RefStdDevice", "RefStdPhase180Flag");
			ConstantAppConfig.REF_STD_PHASE_180_FLAG = ProcalFeatureEnable.REF_STD_PHASE_180_FLAG;
			logger.debug("LoadConfigProperty: REF_STD_PHASE_180_FLAG:"+ConstantAppConfig.REF_STD_PHASE_180_FLAG);
			ConstantAppConfig.REF_STD_CONSTANT_CONFIG_ENABLE = ConstantAppConfigReader.getBoolean("RefStdDevice", "ConstConfigDisplayEnable");
			logger.debug("LoadConfigProperty: REF_STD_CONSTANT_CONFIG_ENABLE:"+ConstantAppConfig.REF_STD_CONSTANT_CONFIG_ENABLE);
			ConstantAppConfig.ERROR_MIN = ConstantAppConfigReader.getFloat("AcceptedErrorRange", "ErrorMin");
			ConstantAppConfig.ERROR_MAX = ConstantAppConfigReader.getFloat("AcceptedErrorRange", "ErrorMax");
			ConstantAppConfig.ERROR_MIN_DEFAULT_VALUE = ConstantAppConfigReader.getFloat("TestPointInputDefaultValues", "Error_Min_Default_Value");
			ConstantAppConfig.ERROR_MAX_DEFAULT_VALUE = ConstantAppConfigReader.getFloat("TestPointInputDefaultValues", "Error_Max_Default_Value");
			ConstantAppConfig.PULSES_DEFAULT_VALUE = ConstantAppConfigReader.getInt("TestPointInputDefaultValues", "Pulses_Default_Value");
			ConstantAppConfig.AVERAGE_DEFAULT_VALUE = ConstantAppConfigReader.getInt("TestPointInputDefaultValues", "Average_Default_Value");


			ConstantAppConfig.TIME_DEFAULT_VALUE = ConstantAppConfigReader.getInt("TestPointInputDefaultValues", "TimeInSec_Default_Value");
			ConstantAppConfig.SKIP_READING_DEFAULT_VALUE = ConstantAppConfigReader.getInt("TestPointInputDefaultValues", "Skip_Reading_Default_Value");
			//ConstantConfig.DEVIATION_DEFAULT_VALUE = ConstantConfigReader.getFloat("TestPointInputDefaultValues", "Deviation_Default_Value");
			ConstantAppConfig.DEVIATION_DEFAULT_VALUE = ConstantApp.DEVIATION_DEFAULT_VALUE;

			//ConstantConfig.VOLT_CURRENT_ZERO_ACCEPTED_VALUE = ConstantConfigReader.getFloat("AcceptedErrorPercentage", "VoltCurrentZeroAcceptedValue"); 
			ConstantAppConfig.VOLT_ZERO_ACCEPTED_VALUE = ConstantAppConfigReader.getFloat("AcceptedErrorPercentage", "VoltZeroAcceptedValue"); 
			ConstantAppConfig.CURRENT_ZERO_ACCEPTED_VALUE = ConstantAppConfigReader.getFloat("AcceptedErrorPercentage", "CurrentZeroAcceptedValue"); 
			ConstantAppConfig.DEGREE_ZERO_ACCEPTED_VALUE = ConstantAppConfigReader.getFloat("AcceptedErrorPercentage", "DegreeZeroAcceptedValue"); 
			//ConstantAppConfig.DEGREE_NON_ZERO_ACCEPTED_VALUE = ConstantAppConfigReader.getFloat("AcceptedErrorPercentage", "DegreeNonZeroAcceptedValue"); 
			//ConstantConfig.POWER_SRC_REBOOT_UP_WAIT_TIME_IN_MSEC = (long) ConstantConfigReader.getAttribute("PowerSrcRebootUpWaitTimeInMsec");
			ConstantAppConfig.POWER_SRC_REBOOT_UP_WAIT_TIME_IN_MSEC = (long) ConstantAppConfigReader.getInt("PwrSrcDevice", "PowerSrcRebootUpWaitTimeInMsec");
			ConstantAppConfig.COOL_OFF_TIME_IN_MSEC = (long) ConstantAppConfigReader.getAttribute("CoolOffTimeInMSec");
			//ConstantConfig.RSS_ACTIVE_PULSE_CONSTANT = ConstantConfigReader.getString("RefStdDevice", "RSS_ActivePulseConstant");
			//ConstantConfig.RSS_REACTIVE_PULSE_CONSTANT = ConstantConfigReader.getString("RefStdDevice", "RSS_ReactivePulseConstant");
			//ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_DEFAULT = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstant");
			//ConstantConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_DEFAULT = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ReactivePulseConstant");
			//ConstantConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT = ConstantConfigReader.getString("RefStdDevice", "RSS_HTCT_ActivePulseConstant");
			//ConstantConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT = ConstantConfigReader.getString("RefStdDevice", "RSS_HTCT_ReactivePulseConstant");


			/*		ConstantConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_1 = ConstantConfigReader.getFloat("RefStdDevice", "RSS_HTCT_CurrentThresholdInAmpsLevel_1");
		ConstantConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_2 = ConstantConfigReader.getFloat("RefStdDevice", "RSS_HTCT_CurrentThresholdInAmpsLevel_2");
		ConstantConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_3 = ConstantConfigReader.getFloat("RefStdDevice", "RSS_HTCT_CurrentThresholdInAmpsLevel_3");
		ConstantConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_4 = ConstantConfigReader.getFloat("RefStdDevice", "RSS_HTCT_CurrentThresholdInAmpsLevel_4");
		ConstantConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_5 = ConstantConfigReader.getFloat("RefStdDevice", "RSS_HTCT_CurrentThresholdInAmpsLevel_5");
		ConstantConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_6 = ConstantConfigReader.getFloat("RefStdDevice", "RSS_HTCT_CurrentThresholdInAmpsLevel_6");
		ConstantConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_7 = ConstantConfigReader.getFloat("RefStdDevice", "RSS_HTCT_CurrentThresholdInAmpsLevel_7");
		ConstantConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_8 = ConstantConfigReader.getFloat("RefStdDevice", "RSS_HTCT_CurrentThresholdInAmpsLevel_8");
		ConstantConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_9 = ConstantConfigReader.getFloat("RefStdDevice", "RSS_HTCT_CurrentThresholdInAmpsLevel_9");
		ConstantConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_10 = ConstantConfigReader.getFloat("RefStdDevice", "RSS_HTCT_CurrentThresholdInAmpsLevel_10");
		ConstantConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_11 = ConstantConfigReader.getFloat("RefStdDevice", "RSS_HTCT_CurrentThresholdInAmpsLevel_11");
		ConstantConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_12 = ConstantConfigReader.getFloat("RefStdDevice", "RSS_HTCT_CurrentThresholdInAmpsLevel_12");


		ConstantConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_1    = ConstantConfigReader.getString("RefStdDevice", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_1");
		ConstantConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_2    = ConstantConfigReader.getString("RefStdDevice", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_2");
		ConstantConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_3    = ConstantConfigReader.getString("RefStdDevice", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_3");
		ConstantConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_4    = ConstantConfigReader.getString("RefStdDevice", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_4");
		ConstantConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_5    = ConstantConfigReader.getString("RefStdDevice", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_5");
		ConstantConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_6    = ConstantConfigReader.getString("RefStdDevice", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_6");
		ConstantConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_7    = ConstantConfigReader.getString("RefStdDevice", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_7");
		ConstantConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_8    = ConstantConfigReader.getString("RefStdDevice", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_8");
		ConstantConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_9    = ConstantConfigReader.getString("RefStdDevice", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_9");
		ConstantConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_10    = ConstantConfigReader.getString("RefStdDevice", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_10");
		ConstantConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_11    = ConstantConfigReader.getString("RefStdDevice", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_11");
		ConstantConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_12    = ConstantConfigReader.getString("RefStdDevice", "RSS_HTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_12");


		ConstantConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_BELOW_LEVEL_12             = ConstantConfigReader.getString("RefStdDevice", "RSS_HTCT_ActivePulseConstantInImpPerWh_BelowLevel_12");

		ConstantConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_1  = ConstantConfigReader.getString("RefStdDevice", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_1");
		ConstantConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_2  = ConstantConfigReader.getString("RefStdDevice", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_2");
		ConstantConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_3  = ConstantConfigReader.getString("RefStdDevice", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_3");
		ConstantConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_4  = ConstantConfigReader.getString("RefStdDevice", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_4");
		ConstantConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_5  = ConstantConfigReader.getString("RefStdDevice", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_5");
		ConstantConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_6  = ConstantConfigReader.getString("RefStdDevice", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_6");
		ConstantConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_7  = ConstantConfigReader.getString("RefStdDevice", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_7");
		ConstantConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_8  = ConstantConfigReader.getString("RefStdDevice", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_8");
		ConstantConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_9  = ConstantConfigReader.getString("RefStdDevice", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_9");
		ConstantConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_10  = ConstantConfigReader.getString("RefStdDevice", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_10");
		ConstantConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_11  = ConstantConfigReader.getString("RefStdDevice", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_11");
		ConstantConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_12  = ConstantConfigReader.getString("RefStdDevice", "RSS_HTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_12");

		ConstantConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_BELOW_LEVEL_12           = ConstantConfigReader.getString("RefStdDevice", "RSS_HTCT_ReactivePulseConstantInImpPerWh_BelowLevel_12");



		ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_1 = ConstantConfigReader.getFloat("RefStdDevice", "RSS_LTCT_CurrentThresholdInAmpsLevel_1");
		ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_2 = ConstantConfigReader.getFloat("RefStdDevice", "RSS_LTCT_CurrentThresholdInAmpsLevel_2");
		ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_3 = ConstantConfigReader.getFloat("RefStdDevice", "RSS_LTCT_CurrentThresholdInAmpsLevel_3");
		ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_4 = ConstantConfigReader.getFloat("RefStdDevice", "RSS_LTCT_CurrentThresholdInAmpsLevel_4");
		ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_5 = ConstantConfigReader.getFloat("RefStdDevice", "RSS_LTCT_CurrentThresholdInAmpsLevel_5");
		ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_6 = ConstantConfigReader.getFloat("RefStdDevice", "RSS_LTCT_CurrentThresholdInAmpsLevel_6");
		ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_7 = ConstantConfigReader.getFloat("RefStdDevice", "RSS_LTCT_CurrentThresholdInAmpsLevel_7");
		ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_8 = ConstantConfigReader.getFloat("RefStdDevice", "RSS_LTCT_CurrentThresholdInAmpsLevel_8");
		ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_9 = ConstantConfigReader.getFloat("RefStdDevice", "RSS_LTCT_CurrentThresholdInAmpsLevel_9");
		ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_10 = ConstantConfigReader.getFloat("RefStdDevice", "RSS_LTCT_CurrentThresholdInAmpsLevel_10");
		ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_11 = ConstantConfigReader.getFloat("RefStdDevice", "RSS_LTCT_CurrentThresholdInAmpsLevel_11");
		ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_12 = ConstantConfigReader.getFloat("RefStdDevice", "RSS_LTCT_CurrentThresholdInAmpsLevel_12");


		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_1     = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_1");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_2     = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_2");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_3     = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_3");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_4     = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_4");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_5     = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_5");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_6     = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_6");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_7     = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_7");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_8     = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_8");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_9     = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_9");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_10     = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_10");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_11     = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_11");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_12     = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentAboveOrEqualLevel_12");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_BELOW_OR_EQUAL_LEVEL_12      = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltBelowOrEqualLevel_4_CurrentBelowLevel_12");


		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_1     = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_1");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_2    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_2");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_3    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_3");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_4    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_4");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_5    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_5");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_6    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_6");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_7    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_7");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_8    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_8");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_9    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_9");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_10    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_10");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_11    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_11");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_12    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentAboveOrEqualLevel_12");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_BELOW_OR_EQUAL_LEVEL_12             = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_4_CurrentBelowLevel_12");

		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_1    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_1");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_2    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_2");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_3    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_3");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_4    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_4");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_5    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_5");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_6    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_6");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_7    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_7");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_8    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_8");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_9    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_9");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_10    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_10");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_11    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_11");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_12    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentAboveOrEqualLevel_12");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_BELOW_OR_EQUAL_LEVEL_12             = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_3_CurrentBelowLevel_12");



		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_1    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_1");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_2    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_2");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_3    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_3");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_4    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_4");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_5    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_5");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_6    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_6");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_7    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_7");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_8    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_8");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_9    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_9");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_10    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_10");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_11    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_11");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_12    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentAboveOrEqualLevel_12");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_BELOW_OR_EQUAL_LEVEL_12  			= ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_VoltAboveLevel_2_CurrentBelowLevel_12");

			 */		
			/*		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_1    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_1");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_2    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_2");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_3    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_3");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_4    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_4");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_5    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_5");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_6    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_6");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_7    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_7");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_8    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_8");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_9    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_9");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_10    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_10");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_11    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_11");
		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_12    = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_AboveOrEqualLevel_12");


		ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_BELOW_LEVEL_12             = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ActivePulseConstantInImpPerWh_BelowLevel_12");
			 */
			/*		ConstantConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_1  = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_1");
		ConstantConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_2  = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_2");
		ConstantConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_3  = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_3");
		ConstantConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_4  = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_4");
		ConstantConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_5  = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_5");
		ConstantConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_6  = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_6");
		ConstantConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_7  = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_7");
		ConstantConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_8  = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_8");
		ConstantConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_9  = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_9");
		ConstantConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_10  = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_10");
		ConstantConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_11  = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_11");
		ConstantConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_12  = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ReactivePulseConstantInImpPerWh_AboveOrEqualLevel_12");

		ConstantConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_BELOW_LEVEL_12           = ConstantConfigReader.getString("RefStdDevice", "RSS_LTCT_ReactivePulseConstantInImpPerWh_BelowLevel_12");

			 */

			ConstantAppConfig.APP_INSTANCE_SERVER_PORT = ConstantAppConfigReader.getInt("app", "AppInstanceServerPort");
			//ConstantAppConfig.APP_INSTANCE_SERVER_PORT = ConstantApp.AppInstanceServerPort;


			ConstantAppConfig.POWER_SRC_ACCEPTED_CONTINUOUS_FAILURE_COUNTER = ConstantAppConfigReader.getInt("StablizationValidation", "PowerSrcAcceptedContinousFailure");

			ConstantAppConfig.LDU_STA_READING_WAIT_TIME_IN_SEC = ConstantAppConfigReader.getInt("LDU_Device", "STA_ReadingWaitTimeInSec"); 
			ConstantAppConfig.LDU_CREEP_READING_WAIT_TIME_IN_SEC = ConstantAppConfigReader.getInt("LDU_Device", "CreepReadingWaitTimeInSec"); 
			ConstantAppConfig.LDU_DIAL_TEST_READING_WAIT_TIME_IN_SEC = ConstantAppConfigReader.getInt("LDU_Device", "DialTestReadingWaitTimeInSec"); 
			ConstantAppConfig.LDU_REPEAT_SELFHEATING_READING_WAIT_TIME_IN_SEC = ConstantAppConfigReader.getInt("LDU_Device", "RepeatSelfHeatingReadingWaitTimeInSec");

			ConstantAppConfig.PYTHON_EXE_LOCATION = ConstantAppConfigReader.getString("ConfigFileName", "PythonExeLocation");
			ConstantAppConfig.PYTHON_SCRIPT_LOCATION = ConstantAppConfigReader.getString("ConfigFileName", "PythonScriptLocation");

			ConstantAppConfig.GENERATE_INDIVIDUAL_METER_REPORT_ENABLED = ConstantAppConfigReader.getBoolean("app", "GenerateIndividualReportEnabled");
			ConstantAppConfig.REPORT_HIGHLIGHT_COLOR_FOR_PASS = ConstantAppConfigReader.getBoolean("Report", "PassColorHighLightEnabled");
			ConstantAppConfig.REPORT_HIGHLIGHT_COLOR_FOR_FAIL = ConstantAppConfigReader.getBoolean("Report", "FailColorHighLightEnabled");
			ConstantAppConfig.REPORT_FONT_SIZE = ConstantAppConfigReader.getInt("Report", "FontSize");
			ConstantAppConfig.REPORT_FONT_NAME = ConstantAppConfigReader.getString("Report", "FontName");



			if (ProcalFeatureEnable.EMH_PWR_SRC_VOLT_UNBALANCE_ZEROVOLT_WORKAROUND){
				ConstantAppConfig.VOLT_UNBALANCE_ZERO_VOLT_PERCENTAGE_LESS_THAN_100V = ConstantAppConfigReader.getFloat("PwrSrcDevice", "VoltUnbalanceZeroVoltLessThan100V_InPercentage");
				ConstantAppConfig.VOLT_UNBALANCE_ZERO_VOLT_PERCENTAGE_GREATER_THAN_100V = ConstantAppConfigReader.getFloat("PwrSrcDevice", "VoltUnbalanceZeroVoltGreaterThan100V_InPercentage");
			}

			ConstantAppConfig.ACC_NO_OF_PAGES_IN_REPORT = ConstantAppConfigReader.getInt("Report", "AccuracyNumberOfPages"); 
			ConstantAppConfig.ACC_NO_OF_PF_VARIANT_IN_EACH_PAGE = ConstantAppConfigReader.getInt("Report", "AccuracyNumberOfSectionPerPage");

			ConstantAppConfig.totalReportProfile = ConstantAppConfigReader.getInt("Report", "TotalReportProfile");
			for(int i = 1; i<= ConstantAppConfig.totalReportProfile; i++){
				ConstantAppConfig.REPORT_PROFILE_LIST.add(ConstantAppConfigReader.getString("Report", ("ReportProfile"+i) ));
				ConstantAppConfig.REPORT_PROFILE_CONFIG_PATH_LIST.add(ConstantAppConfigReader.getString("Report", ("ConfigFileNameReportProfile"+i) ));
				//ConstantConfig.REPORT_PROFILE_LIST.add(ConstantConfigReader.getString("Report", "ReportProfile2"));
				//ConstantConfig.REPORT_PROFILE_LIST.add(ConstantConfigReader.getString("Report", "ReportProfile3"));
			}


			ConstantAppConfig.REPORT_DATA_REPLACE_ENABLED = ConstantAppConfigReader.getBoolean("Report", "ReportDataReplaceEnabled");
			if(ConstantAppConfig.REPORT_DATA_REPLACE_ENABLED){
				ConstantAppConfig.REPORT_DATA_REPLACE_COUNT = ConstantAppConfigReader.getInt("Report", "ReportDataReplaceCount");
				for(int i = 1; i<= ConstantAppConfig.REPORT_DATA_REPLACE_COUNT; i++){
					ConstantAppConfig.REPORT_DATA_FIND.add(ConstantAppConfigReader.getString("Report", ("ReportDataFind"+i) ));
					ConstantAppConfig.REPORT_DATA_REPLACE.add(ConstantAppConfigReader.getString("Report", ("ReportDataReplace"+i) ));

				}
			}
			
			
			
			
			//if(ConstantAppConfig.REPORT_DATA_REPLACE_ENABLED){
				ConstantAppConfig.CONSTANT_TEST_RESULT_CONFIG_LIMIT_COUNT = ConstantAppConfigReader.getInt("Report", "TotalConstantTestResultLimits");
				
				ConstantAppConfig.CONSTANT_TEST_DEFAULT_ACCEPTED_UPPER_LIMIT = ConstantAppConfigReader.getString("Report", "ConstantTestAcceptedDefaultUpperLimits");
				ConstantAppConfig.CONSTANT_TEST_DEFAULT_ACCEPTED_LOWER_LIMIT = ConstantAppConfigReader.getString("Report", "ConstantTestAcceptedDefaultLowerLimits");
				

				
				for(int i = 1; i<= ConstantAppConfig.CONSTANT_TEST_RESULT_CONFIG_LIMIT_COUNT; i++){
					
					ConstantAppConfig.CONSTANT_TEST_FILTER_UNIT.add(ConstantAppConfigReader.getString("Report", ("ConstantTestFilterUnit"+i) ));
					ConstantAppConfig.CONSTANT_TEST_FILTER_VALUE.add(ConstantAppConfigReader.getString("Report", ("ConstantTestFilterValue"+i) ));
					ConstantAppConfig.CONSTANT_TEST_ACCEPTED_UPPER_LIMIT.add(ConstantAppConfigReader.getString("Report", ("ConstantTestAcceptedUpperLimits"+i) ));
					ConstantAppConfig.CONSTANT_TEST_ACCEPTED_LOWER_LIMIT.add(ConstantAppConfigReader.getString("Report", ("ConstantTestAcceptedLowerLimits"+i) ));
					
				

				}
			//}
			
			
			


			ConstantAppConfig.REPORT_PROFILE_DEFAULT_ACTIVE_GROUP_NAME_LIST = ConstantAppConfigReader.getStringList("Report", "ReportProfileDefaultActiveGroupName");
			ApplicationLauncher.logger.info("LoadConfigProperty :Loaded REPORT_PROFILE_DEFAULT_ACTIVE_GROUP_NAME_LIST: "+ConstantAppConfig.REPORT_PROFILE_DEFAULT_ACTIVE_GROUP_NAME_LIST);
			ApplicationLauncher.logger.info("LoadConfigProperty :Loaded REPORT_PROFILE_DEFAULT_ACTIVE_GROUP_NAME_LIST.get(0): "+ConstantAppConfig.REPORT_PROFILE_DEFAULT_ACTIVE_GROUP_NAME_LIST.get(0));


			ConstantAppConfig.REPORT_PROFILE_DEFAULT_ACTIVE_CUSTOMER_ID = ConstantAppConfigReader.getString("Report", "ReportProfileDefaultActiveCustomerId");
			ApplicationLauncher.logger.info("LoadConfigProperty :Loaded REPORT_PROFILE_DEFAULT_ACTIVE_CUSTOMER_ID: "+ConstantAppConfig.REPORT_PROFILE_DEFAULT_ACTIVE_CUSTOMER_ID);




			ConstantAppConfig.DefaultReportProfileDisplay = ConstantAppConfigReader.getString("Report", "DefaultReportProfileDisplay");
			ConstantAppConfig.BNC_OUTPORT_PORT = ConstantAppConfigReader.getInt("RefStdDevice", "BNC_OutputPort");
			ApplicationLauncher.logger.info("LoadConfigProperty :Loaded BNC_OutputPort: "+ConstantAppConfig.BNC_OUTPORT_PORT);
			if ((ConstantAppConfig.BNC_OUTPORT_PORT > ConstantApp.BNC_OUTPUTPORT_MAX ) || (ConstantAppConfig.BNC_OUTPORT_PORT < 0 )){
				ApplicationLauncher.logger.error("LoadConfigProperty : Invalid BNC output Port: "+ConstantAppConfig.BNC_OUTPORT_PORT);
				ApplicationLauncher.InformUser("Invalid BNC port","BNC output port should be between 1 to 3. Currently configured to "+ConstantAppConfig.BNC_OUTPORT_PORT,AlertType.ERROR);
			}

			if(ProcalFeatureEnable.USER_ACCESS_CONTROL_ENABLED){

				ConstantAppConfig.NO_OF_UAC_PROFILES = ConstantAppConfigReader.getInt("UserAccessControl", "NoOfUacProfiles");
				ConstantAppConfig.UAC_DEFAULT_PROFILE = ConstantAppConfigReader.getString("UserAccessControl", "UacDefaultProfileName");


				for(int i = 1; i <= ConstantAppConfig.NO_OF_UAC_PROFILES ; i++ ){
					String profileName = ConstantAppConfigReader.getString("UserAccessControl", "UacProfileName" + String.valueOf(i));
					ApplicationLauncher.logger.info("LoadConfigProperty : UAC profileName" + i + " : " + profileName);
					ConstantAppConfig.UAC_PROFILE_LIST.add(profileName);
				}
			}

			ApplicationLauncher.logger.info("LoadConfigProperty :Configured config file version:"+ConstantVersion.CONFIG_FILE_VERSION);
			ConstantVersion.CONFIG_FILE_VERSION = (String) ConstantAppConfigReader.getAttribute("ConfigFileVersion");
			ApplicationLauncher.logger.info("LoadConfigProperty :Loaded config file version:"+ConstantVersion.CONFIG_FILE_VERSION);
			/*}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("LoadConfigProperty : Exception2"+ e.getMessage());


		}*/
			ConstantAppConfig.LSCS_POWER_SOURCE_CALIBRATION_FILE_PATH = ConstantAppConfigReader.getString("ConfigFilePathLocation", "ProPowerCalibrationFolderPath");
			ApplicationLauncher.logger.info("LoadConfigProperty :Configured LSCS_POWER_SOURCE_CALIBRATION_FILE_PATH: "+ConstantAppConfig.LSCS_POWER_SOURCE_CALIBRATION_FILE_PATH);


			ConstantAppConfig.LSCS_POWER_SOURCE_CALIBRATION_FILE_NAME = ConstantAppConfigReader.getString("ConfigFileName", "ProPowerCalibrationFileName");
			ApplicationLauncher.logger.info("LoadConfigProperty :Configured LSCS_POWER_SOURCE_CALIBRATION_FILE_NAME: "+ConstantAppConfig.LSCS_POWER_SOURCE_CALIBRATION_FILE_NAME);



			ConstantAppConfig.REPORT_CONFIG_FILE_PATH = ConstantAppConfigReader.getString("ConfigFilePathLocation", "ReportConfigFolderPath");
			ApplicationLauncher.logger.info("LoadConfigProperty :Configured REPORT_CONFIG_FILE_PATH: "+ConstantAppConfig.REPORT_CONFIG_FILE_PATH);


			ConstantAppConfig.REPORT_CONFIG_FILE_NAME = ConstantAppConfigReader.getString("ConfigFileName", "ReportConfigFileName");
			ApplicationLauncher.logger.info("LoadConfigProperty :Configured REPORT_CONFIG_FILE_NAME: "+ConstantAppConfig.REPORT_CONFIG_FILE_NAME);



			ConstantAppConfig.REPORT_PROFILE_PATH = ConstantAppConfigReader.getString("ConfigFilePathLocation", "ReportProfileFolderPath");
			ApplicationLauncher.logger.info("LoadConfigProperty :Configured REPORT_PROFILE_PATH: "+ConstantAppConfig.REPORT_PROFILE_PATH);

			//if(ProcalFeatureEnable.CONVEYOR_FEATURE_ENABLED) {
			
			
				ConstantAppConfig.CONVEYOR_CONFIG_FILE_PATH = ConstantAppConfigReader.getString("ConfigFilePathLocation", "ConveyorProfileFolderPath");
				ApplicationLauncher.logger.info("LoadConfigProperty :Configured CONVEYOR_CONFIG_FILE_PATH: "+ConstantAppConfig.CONVEYOR_CONFIG_FILE_PATH);
	
				ConstantAppConfig.CONVEYOR_CONFIG_FILE_NAME = ConstantAppConfigReader.getString("ConfigFileName", "ConveyorProfileConfigFileName");
				ApplicationLauncher.logger.info("LoadConfigProperty :Configured CONVEYOR_CONFIG_FILE_NAME: "+ConstantAppConfig.CONVEYOR_CONFIG_FILE_NAME);

			//}


			ConstantAppConfig.DUT_COMM_FEATURE_ENABLED = ConstantAppConfigReader.getBoolean("DUT", "CommFeatureEnabled");
			ApplicationLauncher.logger.info("LoadConfigProperty :Configured DUT_COMM_SETTINGS: "+ConstantAppConfig.DUT_COMM_FEATURE_ENABLED);

			ConstantAppConfig.DUT = ConstantAppConfigReader.getString("app", "DUT");

			ConstantAppConfig.DEVICE_SETTINGS_POWER_SOURCE_ID = ConstantAppConfigReader.getInt("DeviceSettings", "PowerSource_Id");
			ConstantAppConfig.DEVICE_SETTINGS_REF_STD_ID = ConstantAppConfigReader.getInt("DeviceSettings", "RefStd_Id");
			ConstantAppConfig.DEVICE_SETTINGS_LDU_ID = ConstantAppConfigReader.getInt("DeviceSettings", "Ldu_Id");
			ConstantAppConfig.DEVICE_SETTINGS_ICT_ID = ConstantAppConfigReader.getInt("DeviceSettings", "Ict_Id");

			ConstantAppConfig.DEVICE_SETTINGS_DUT01_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut01_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT02_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut02_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT03_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut03_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT04_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut04_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT05_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut05_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT06_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut06_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT07_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut07_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT08_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut08_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT09_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut09_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT10_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut10_Id");

			ConstantAppConfig.DEVICE_SETTINGS_DUT11_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut11_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT12_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut12_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT13_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut13_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT14_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut14_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT15_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut15_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT16_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut16_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT17_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut17_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT18_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut18_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT19_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut19_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT20_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut20_Id");

			ConstantAppConfig.DEVICE_SETTINGS_DUT21_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut21_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT22_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut22_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT23_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut23_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT24_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut24_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT25_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut25_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT26_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut26_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT27_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut27_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT28_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut28_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT29_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut29_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT30_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut30_Id");

			ConstantAppConfig.DEVICE_SETTINGS_DUT31_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut31_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT32_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut32_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT33_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut33_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT34_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut34_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT35_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut35_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT36_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut36_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT37_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut37_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT38_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut38_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT39_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut39_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT40_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut40_Id");

			ConstantAppConfig.DEVICE_SETTINGS_DUT41_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut41_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT42_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut42_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT43_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut43_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT44_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut44_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT45_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut45_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT46_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut46_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT47_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut47_Id");
			ConstantAppConfig.DEVICE_SETTINGS_DUT48_ID = ConstantAppConfigReader.getInt("DeviceSettings", "dut48_Id");

			ConstantAppConfig.METER_ID_BLACKLIST_VALIDATION = ConstantAppConfigReader.getBoolean("DataValidation", "DutIdBlackListValidation"); 
			String data ="";
			if(ConstantAppConfig.METER_ID_BLACKLIST_VALIDATION){
				ConstantAppConfig.TOTAL_METER_ID_BLACKLISTED = ConstantAppConfigReader.getInt("DataValidation", "TotalDutIdBlackList"); 

				for(int i=0; i< ConstantAppConfig.TOTAL_METER_ID_BLACKLISTED ; i++){

					data = ConstantAppConfigReader.getString("DataValidation", "DutIdBlackList"+(i+1));
					ApplicationLauncher.logger.info("LoadConfigProperty : TOTAL_METER_ID_BLACKLISTED: index: " + i + " ,  data = " + data);
					ConstantAppConfig.METER_ID_BLACKLISTED_LIST.add(data);

				}
			}

			ConstantAppConfig.METER_ID_WHITELIST_VALIDATION = ConstantAppConfigReader.getBoolean("DataValidation", "DutIdWhiteListValidation"); 

			if(ConstantAppConfig.METER_ID_WHITELIST_VALIDATION){
				ConstantAppConfig.TOTAL_METER_ID_WHITELISTED = ConstantAppConfigReader.getInt("DataValidation", "TotalDutIdWhiteList"); 

				for(int i=0; i< ConstantAppConfig.TOTAL_METER_ID_WHITELISTED ; i++){

					data = ConstantAppConfigReader.getString("DataValidation", "DutIdWhiteList"+(i+1));
					ApplicationLauncher.logger.info("LoadConfigProperty : TOTAL_METER_ID_WHITELISTED: index: " + i + " ,  data = " + data);
					ConstantAppConfig.METER_ID_WHITELISTED_LIST.add(data);

				}
			}

			ConstantAppConfig.METER_ID_VALIDATE_ALREADY_TESTED = ConstantAppConfigReader.getBoolean("DataValidation", "DutIdValidateAlreadyTested");
			ConstantAppConfig.METER_ID_VALIDATE_FOR_EMPTY = ConstantAppConfigReader.getBoolean("DataValidation", "DutIdValidateForEmpty");

			ConstantAppConfig.IMPORT_DEPLOYMENT_DATA_FEATURE_ENABLED = ConstantAppConfigReader.getBoolean("ImportDeploymentData", "ImportDeploymentDataFeatureEnabled");



			ConstantAppConfig.IMPORT_DEPLOYMENT_DATA_RACK_POSITION_ID_HEADER = ConstantAppConfigReader.getString("ImportDeploymentData", "RackPositionIdHeader");		
			ConstantAppConfig.IMPORT_DEPLOYMENT_DATA_RACK_POSITION_SELECTED_HEADER = ConstantAppConfigReader.getString("ImportDeploymentData", "RackPositionSelectedHeader");	
			ConstantAppConfig.IMPORT_DEPLOYMENT_DATA_SERIAL_NO_HEADER = ConstantAppConfigReader.getString("ImportDeploymentData", "SerialNoHeader");
			ConstantAppConfig.IMPORT_DEPLOYMENT_DATA_CT_RATIO_HEADER = ConstantAppConfigReader.getString("ImportDeploymentData", "CtRatioHeader");
			ConstantAppConfig.IMPORT_DEPLOYMENT_DATA_PT_RATIO_HEADER = ConstantAppConfigReader.getString("ImportDeploymentData", "PtRatioHeader");
			ConstantAppConfig.IMPORT_DEPLOYMENT_DATA_METER_CONSTANT_HEADER = ConstantAppConfigReader.getString("ImportDeploymentData", "MeterConstantHeader");
			ConstantAppConfig.IMPORT_DEPLOYMENT_DATA_MAKE_HEADER = ConstantAppConfigReader.getString("ImportDeploymentData", "MeterMakeHeader");
			ConstantAppConfig.IMPORT_DEPLOYMENT_DATA_MODEL_NO_HEADER = ConstantAppConfigReader.getString("ImportDeploymentData", "MeterModelNoHeader");


			ConstantAppConfig.REFSTD_CONSTANT_CONFIG_FILE_NAME = ConstantAppConfigReader.getString("ConfigFileName", "RefStdConstantConfigFileName");
			ConstantAppConfig.REFSTD_CONSTANT_CONFIG_FILE_PATH = ConstantAppConfigReader.getString("ConfigFilePathLocation", "RefStdConstantConfigFolderPath");

			ConstantAppConfig.LSCS_STATIC_POWER_SOURCE_INIT = ConstantAppConfigReader.getString("app", "LscsPowerSrcInit");
			ConstantAppConfig.INSTANT_METRICS_DISPLAY_ALWAYS_ON_TOP = ConstantAppConfigReader.getBoolean("app", "InstantMetricsDisplayAlwaysOnTop");
			ConstantAppConfig.INSTANT_METRICS_ENERGY_DISPLAY_IN_KWH = ConstantAppConfigReader.getBoolean("app", "InstantMetricsEnergyDisplayInKilloWattHour");

			
			ConstantAppConfig.REPORT_EXCEL_LAST_ROW = ConstantAppConfigReader.getInt("Report", "ExcelLastRow");
			ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN = ConstantAppConfigReader.getInt("Report", "ExcelLastColumn");

			ConstantAppConfig.REF_STD_ENABLE_PARSING_LOGS = ConstantAppConfigReader.getBoolean("RefStdDevice", "EnableParsingLogs");
			ConstantAppConfig.DEPLOYMENT_REFERENCE_NUMBER_LABEL_NAME = ConstantAppConfigReader.getString("app", "DeploymentReferenceNoLabelName");

			ConstantAppConfig.PROPOWER_MANUAL_MODE_TIMER_INPUT_MIN_ACCEPTED = ConstantAppConfigReader.getInt("PwrSrcDevice", "ManualModeTimerInputMinAccepted");
			ConstantAppConfig.PROPOWER_MANUAL_MODE_TIMER_INPUT_MAX_ACCEPTED = ConstantAppConfigReader.getInt("PwrSrcDevice", "ManualModeTimerInputMaxAccepted");
			ConstantAppConfig.PROPOWER_MANUAL_MODE_DEFAULT_TIMER_INPUT = ConstantAppConfigReader.getInt("PwrSrcDevice", "ManualModeTimerInputDefaultValue");
			if(ProcalFeatureEnable.LSCS_PWR_SRC_FINE_TUNE_WITH_REF_STD_ENABLED){
				ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_ENABLED = ConstantAppConfigReader.getBoolean("RefStdFeedBackControl", "RefStdFeedBackControlEnabled");
				ApplicationLauncher.logger.info("LoadConfigProperty :Configured REF_STD_FEEDBACK_CONTROL_ENABLED: "+ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_ENABLED);
			
				
				
				ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_MULTIPLIER_ENABLED = ConstantAppConfigReader.getBoolean("RefStdFeedBackControl", "RefStdFeedBackMultiplierEnabled");
				ApplicationLauncher.logger.info("LoadConfigProperty :Configured REF_STD_FEEDBACK_CONTROL_MULTIPLIER_ENABLED: "+ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_MULTIPLIER_ENABLED);
			
				
			
			}else{
				ApplicationLauncher.logger.info("LoadConfigProperty :Configured REF_STD_FEEDBACK_CONTROL_ENABLED not read from config: "+ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_ENABLED);
			}

			ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_DEGREE_ALLOWED_UPPER_LIMIT = ConstantAppConfigReader.getFloat("RefStdFeedBackControl", "RefStdFeedBackDegreeAllowedUpperLimit");
			ApplicationLauncher.logger.info("LoadConfigProperty :Configured REF_STD_FEEDBACK_CONTROL_DEGREE_ALLOWED_UPPER_LIMIT: "+ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_DEGREE_ALLOWED_UPPER_LIMIT);

			ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_DEGREE_ALLOWED_LOWER_LIMIT = ConstantAppConfigReader.getFloat("RefStdFeedBackControl", "RefStdFeedBackDegreeAllowedLowerLimit");
			ApplicationLauncher.logger.info("LoadConfigProperty :Configured REF_STD_FEEDBACK_CONTROL_DEGREE_ALLOWED_LOWER_LIMIT: "+ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_DEGREE_ALLOWED_LOWER_LIMIT);

			ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_DEGREE_RETRY_MAX_COUNT = ConstantAppConfigReader.getInt("RefStdFeedBackControl", "RefStdFeedBackDegreeRetryCountMax");
			ApplicationLauncher.logger.info("LoadConfigProperty :Configured REF_STD_FEEDBACK_CONTROL_DEGREE_RETRY_MAX_COUNT: "+ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_DEGREE_RETRY_MAX_COUNT);

			ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_UPPER_LIMIT_PERCENT = ConstantAppConfigReader.getFloat("RefStdFeedBackControl", "RefStdFeedBackVoltAllowedUpperLimitPercentage");
			ApplicationLauncher.logger.info("LoadConfigProperty :Configured REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_UPPER_LIMIT: "+ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_UPPER_LIMIT_PERCENT);

			ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_LOWER_LIMIT_PERCENT = ConstantAppConfigReader.getFloat("RefStdFeedBackControl", "RefStdFeedBackVoltAllowedLowerLimitPercentage");
			ApplicationLauncher.logger.info("LoadConfigProperty :Configured REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_LOWER_LIMIT: "+ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_LOWER_LIMIT_PERCENT);


			ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_CURRENT_ALLOWED_UPPER_LIMIT_PERCENT = ConstantAppConfigReader.getFloat("RefStdFeedBackControl", "RefStdFeedBackCurrentAllowedUpperLimitPercentage");
			ApplicationLauncher.logger.info("LoadConfigProperty :Configured REF_STD_FEEDBACK_CONTROL_CURRENT_ALLOWED_UPPER_LIMIT_PERCENT: "+ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_CURRENT_ALLOWED_UPPER_LIMIT_PERCENT);

			ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_CURRENT_ALLOWED_LOWER_LIMIT_PERCENT = ConstantAppConfigReader.getFloat("RefStdFeedBackControl", "RefStdFeedBackCurrentAllowedLowerLimitPercentage");
			ApplicationLauncher.logger.info("LoadConfigProperty :Configured REF_STD_FEEDBACK_CONTROL_CURRENT_ALLOWED_LOWER_LIMIT_PERCENT: "+ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_CURRENT_ALLOWED_LOWER_LIMIT_PERCENT);

			ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_DEGREE_ALLOWED_UPPER_LIMIT_SINGLE_PHASE = ConstantAppConfigReader.getFloat("RefStdFeedBackControl", "RefStdFeedBackDegreeAllowedUpperLimitSinglePhase");
			ApplicationLauncher.logger.info("LoadConfigProperty :Configured REF_STD_FEEDBACK_CONTROL_DEGREE_ALLOWED_UPPER_LIMIT_SINGLE_PHASE: "+ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_DEGREE_ALLOWED_UPPER_LIMIT_SINGLE_PHASE);

			ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_DEGREE_ALLOWED_LOWER_LIMIT_SINGLE_PHASE = ConstantAppConfigReader.getFloat("RefStdFeedBackControl", "RefStdFeedBackDegreeAllowedLowerLimitSinglePhase");
			ApplicationLauncher.logger.info("LoadConfigProperty :Configured REF_STD_FEEDBACK_CONTROL_DEGREE_ALLOWED_LOWER_LIMIT_SINGLE_PHASE: "+ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_DEGREE_ALLOWED_LOWER_LIMIT_SINGLE_PHASE);




			//ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_FINETUNE_CURRENT_ALLOWED_PERCENTAGE = ConstantAppConfigReader.getFloat("RefStdFeedBackControl", "RefStdFeedBackCurrentFineTuneAllowedPercentage");
			//ApplicationLauncher.logger.info("LoadConfigProperty :Configured REF_STD_FEEDBACK_CONTROL_FINETUNE_CURRENT_ALLOWED_PERCENTAGE: "+ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_FINETUNE_CURRENT_ALLOWED_PERCENTAGE);


			ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_FINETUNE_CURRENT_WAIT_TIME_IN_SEC = ConstantAppConfigReader.getInt("RefStdFeedBackControl", "RefStdFeedBackCurrentFineTuneWaitTimeInSec");
			ApplicationLauncher.logger.info("LoadConfigProperty :Configured REF_STD_FEEDBACK_CONTROL_FINETUNE_CURRENT_WAIT_TIME_IN_SEC: "+ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_FINETUNE_CURRENT_WAIT_TIME_IN_SEC);

			ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_FINETUNE_CURRENT_CONFIRMATION_COUNT = ConstantAppConfigReader.getInt("RefStdFeedBackControl", "RefStdFeedBackCurrentFineTuneConfirmationCount");
			ApplicationLauncher.logger.info("LoadConfigProperty :Configured REF_STD_FEEDBACK_CONTROL_FINETUNE_CURRENT_CONFIRMATION_COUNT: "+ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_FINETUNE_CURRENT_CONFIRMATION_COUNT);

			ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_SEND_DATA_TO_POWER_SRC_REFRESH_TIME_IN_MILLI_SEC = ConstantAppConfigReader.getInt("RefStdFeedBackControl", "RefStdFeedBackControlSendDataToPowerSourceRefreshTimeInMsec");
			ApplicationLauncher.logger.info("LoadConfigProperty :Configured REF_STD_FEEDBACK_CONTROL_SEND_DATA_TO_POWER_SRC_REFRESH_TIME_IN_MILLI_SEC: "+ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_SEND_DATA_TO_POWER_SRC_REFRESH_TIME_IN_MILLI_SEC);

			ConstantAppConfig.REPORT_CUSTOM_EXPORT_AS_PDF_ENABLED = ConstantAppConfigReader.getBoolean("Report", "CustomReportExportAsPdf");


			ConstantAppConfig.DUT_DISPLAY_KEY = ConstantAppConfigReader.getString("app", "DutDisplayKey");


			ConstantAppConfig.REPORT_PROFILEV2_FILE_PATH = ConstantAppConfigReader.getString("ConfigFilePathLocation", "ReportProfileFolderPath");
			ApplicationLauncher.logger.info("LoadConfigProperty :Configured REPORT_PROFILEV2_FILE_PATH: "+ConstantAppConfig.REPORT_PROFILEV2_FILE_PATH);


			ConstantAppConfig.REPORT_PROFILEV2_FILE_NAME = ConstantAppConfigReader.getString("ConfigFileName", "ReportProfileV2FileName");
			ApplicationLauncher.logger.info("LoadConfigProperty :Configured REPORT_PROFILEV2_FILE_NAME: "+ConstantAppConfig.REPORT_PROFILEV2_FILE_NAME);


			ConstantAppConfig.TP_USER_ENTRY_VOLT_PERCENT_ALLOWED_MAX = ConstantAppConfigReader.getFloat("TestPointUserEntryLimit", "VoltPercentAllowedMax");

			ConstantAppConfig.DEPLOYMENT_VOLT_PERCENT_VALIDATION = ConstantAppConfigReader.getBoolean("TestPointUserEntryLimit", "deploymentVoltagePercentValidation");
			ConstantAppConfig.DEPLOYMENT_CURRENT_PERCENT_VALIDATION = ConstantAppConfigReader.getBoolean("TestPointUserEntryLimit", "deploymentCurrentPercentValidation");



			ConstantAppConfig.TP_USER_ENTRY_VOLT_PERCENT_ALLOWED_MIN  = ConstantAppConfigReader.getFloat("TestPointUserEntryLimit", "VoltPercentAllowedMin");
			ConstantAppConfig.TP_USER_ENTRY_CURRENT_Ib_PERCENT_ALLOWED_MAX  = ConstantAppConfigReader.getFloat("TestPointUserEntryLimit", "CurrentIb_PercentAllowedMax");
			ConstantAppConfig.TP_USER_ENTRY_CURRENT_IMAX_PERCENT_ALLOWED_MAX  = ConstantAppConfigReader.getFloat("TestPointUserEntryLimit", "CurrentImax_PercentAllowedMax");
			ConstantAppConfig.TP_USER_ENTRY_CURRENT_PERCENT_ALLOWED_MIN_ABOVE  = ConstantAppConfigReader.getFloat("TestPointUserEntryLimit", "CurrentPercentAllowedMinAbove");
			ConstantAppConfig.TP_USER_ENTRY_PF_ALLOWED_MAX  = ConstantAppConfigReader.getFloat("TestPointUserEntryLimit", "PowerFactorAllowedMax");
			ConstantAppConfig.TP_USER_ENTRY_PF_ALLOWED_MIN  = ConstantAppConfigReader.getFloat("TestPointUserEntryLimit", "PowerFactorAllowedMin");

			ConstantAppConfig.TP_USER_ENTRY_ACTIVE_ENERGY_KWH_ALLOWED_MAX  = ConstantAppConfigReader.getFloat("TestPointUserEntryLimit", "ActiveEnergy_kWh_AllowedMax");
			ConstantAppConfig.TP_USER_ENTRY_ACTIVE_ENERGY_KWH_ALLOWED_MIN_ABOVE  = ConstantAppConfigReader.getFloat("TestPointUserEntryLimit", "ActiveEnergy_kWh_AllowedMinAbove");
			ConstantAppConfig.TP_USER_ENTRY_REACTIVE_ENERGY_KVARH_ALLOWED_MAX  = ConstantAppConfigReader.getFloat("TestPointUserEntryLimit", "ReactiveEnergy_kVARh_AllowedMax");
			ConstantAppConfig.TP_USER_ENTRY_REACTIVE_ENERGY_KVARH_ALLOWED_MIN_ABOVE  = ConstantAppConfigReader.getFloat("TestPointUserEntryLimit", "ReactiveEnergy_kVARh_AllowedMinAbove");
			ConstantAppConfig.TP_USER_ENTRY_APPARENT_ENERGY_KVAH_ALLOWED_MAX  = ConstantAppConfigReader.getFloat("TestPointUserEntryLimit", "ApparentEnergy_kVAh_AllowedMax");
			ConstantAppConfig.TP_USER_ENTRY_APPARENT_ENERGY_KVAH_ALLOWED_MIN_ABOVE  = ConstantAppConfigReader.getFloat("TestPointUserEntryLimit", "ApparentEnergy_kVAh_AllowedMinAbove");

			ConstantAppConfig.TP_USER_ENTRY_ITERATION_ALLOWED_MAX  = ConstantAppConfigReader.getInt("TestPointUserEntryLimit", "IterationAllowedMax");
			ConstantAppConfig.TP_USER_ENTRY_ITERATION_ALLOWED_MIN  = ConstantAppConfigReader.getInt("TestPointUserEntryLimit", "IterationAllowedMin");
			//ConstantAppConfig.PRINT_STYLE_MODEL = ConstantAppConfigReader.getPrintStyleModelObject("PrintStyle");
			List<Object> printStyle =  (List<Object>) ConstantAppConfigReader.getList("Report","PrintStyle");
			//List<PrintStyle> printStyle = ConstantAppConfigReader.getObjectArray("Report","PrintStyle");
			ApplicationLauncher.logger.debug("LoadConfigProperty : printStyle: " + printStyle.toString());
			//ApplicationLauncher.logger.debug("LoadConfigProperty : printStyle: " + printStyle.get(1));

			for( int i =0; i< printStyle.size(); i++ ){
				Gson gson = new Gson();
				//String yourJson = printStyle.get(i).toString();
				//ApplicationLauncher.logger.debug("LoadConfigProperty : yourJson: " + yourJson);
				PrintStyle eachPrintStyle = gson.fromJson(printStyle.get(i).toString(), PrintStyle.class);
				ApplicationLauncher.logger.debug("LoadConfigProperty : eachPrintStyle: " + eachPrintStyle.getReportPrintStyleName());
				ConstantAppConfig.PRINT_STYLE_LIST.add(eachPrintStyle);
			}

			ConstantAppConfig.DEFAULT_LOGIN_ID_POPULATE_ENABLED = ConstantAppConfigReader.getBoolean("app", "DefaultLidEnabled");

			ConstantAppConfig.LSC_LDU_INVALID_DATA_SKIP = ConstantAppConfigReader.getBoolean("app", "LduInvalidDataSkip");
			
			
			
			
			ConstantAppConfig.DIAL_TEST_SINGLE_PHASE_NEARING_TARGET1_PERCENT = ConstantAppConfigReader.getFloat("DialTest", "1P_NearingTarget1Percent");
			ConstantAppConfig.DIAL_TEST_SINGLE_PHASE_NEARING_TARGET1_CURRENT_REDUCTION_PERCENT = ConstantAppConfigReader.getFloat("DialTest", "1P_NearingTarget1CurrentReductionPercentage");
			ConstantAppConfig.DIAL_TEST_SINGLE_PHASE_NEARING_TARGET2_PERCENT = ConstantAppConfigReader.getFloat("DialTest", "1P_NearingTarget2Percent");
			ConstantAppConfig.DIAL_TEST_SINGLE_PHASE_NEARING_TARGET2_CURRENT_REDUCTION_PERCENT = ConstantAppConfigReader.getFloat("DialTest", "1P_NearingTarget2CurrentReductionPercentage");
			
			ConstantAppConfig.DIAL_TEST_THREE_PHASE_NEARING_TARGET1_PERCENT = ConstantAppConfigReader.getFloat("DialTest", "3P_NearingTarget1Percent");
			ConstantAppConfig.DIAL_TEST_THREE_PHASE_NEARING_TARGET1_CURRENT_REDUCTION_PERCENT = ConstantAppConfigReader.getFloat("DialTest", "3P_NearingTarget1CurrentReductionPercentage");
			ConstantAppConfig.DIAL_TEST_THREE_PHASE_NEARING_TARGET2_PERCENT = ConstantAppConfigReader.getFloat("DialTest", "3P_NearingTarget2Percent");
			ConstantAppConfig.DIAL_TEST_THREE_PHASE_NEARING_TARGET2_CURRENT_REDUCTION_PERCENT = ConstantAppConfigReader.getFloat("DialTest", "3P_NearingTarget2CurrentReductionPercentage");
			
			
			ConstantConveyorConfig.MY_TERMINAL_ID = ConstantAppConfigReader.getString("conveyor", "MyTerminalId");
			ConstantConveyorConfig.CLUSTER_NAME_01 = ConstantAppConfigReader.getString("conveyor", "Cluster1_Name");
			ConstantConveyorConfig.TERMINAL_CONFIG_FILE_PATH = ConstantAppConfigReader.getString("conveyor", "TerminalConfigFolderPath"); 
			ConstantConveyorConfig.TERMINAL_CONFIG_FILE_NAME = ConstantAppConfigReader.getString("conveyor", "TerminalConfigFolderName"); 
			ConstantConveyorConfig.CONVEYOR_DEBUG_SCREEN_DISPLAY_ENABLED = ConstantAppConfigReader.getBoolean("conveyor", "ConveyorDebugScreenDisplayEnabled");
			ConstantConveyorConfig.PALLET_MANAGE_RECENT_NO_OF_DAYS_DISPLAY = ConstantAppConfigReader.getInt("conveyor", "PalletManageRecentNoOfDaysDisplay");
			
			ConstantConveyorConfig.WAITING_TO_VERIFIC_BAY_PALLET_MOVEMENT_WAIT_TIME_IN_SEC = ConstantAppConfigReader.getInt("conveyor", "WaitingToVerificBayPalletMovementWaitTimeInSec");
			ConstantConveyorConfig.VERIFIC_TO_STA1_BAY_PALLET_MOVEMENT_WAIT_TIME_IN_SEC = ConstantAppConfigReader.getInt("conveyor", "VerificToSta1BayPalletMovementWaitTimeInSec");
			ConstantConveyorConfig.VERIFIC_TO_STA2_BAY_PALLET_MOVEMENT_WAIT_TIME_IN_SEC = ConstantAppConfigReader.getInt("conveyor", "VerificToSta2BayPalletMovementWaitTimeInSec");
			
			
			ConstantConveyorConfig.STA1_TO_UNLOADING_BAY_PALLET_MOVEMENT_WAIT_TIME_IN_SEC = ConstantAppConfigReader.getInt("conveyor", "Sta1ToUnloadingBayPalletMovementWaitTimeInSec");
			ConstantConveyorConfig.STA2_TO_UNLOADING_BAY_PALLET_MOVEMENT_WAIT_TIME_IN_SEC = ConstantAppConfigReader.getInt("conveyor", "Sta2ToUnloadingBayPalletMovementWaitTimeInSec");
			
			ConstantConveyorConfig.CONVEYOR_PROCAL_CALIB_IP_ADDRESS = ConstantAppConfigReader.getString("conveyor_procal_calib", "IpAddress");
			ConstantConveyorConfig.CONVEYOR_PROCAL_CALIB_IP_PORT = ConstantAppConfigReader.getString("conveyor_procal_calib", "IpPort"); 
			ConstantConveyorConfig.CONVEYOR_PROCAL_CALIB_END_PATH = ConstantAppConfigReader.getString("conveyor_procal_calib", "targetEndPath");
			
			ConstantConveyorConfig.CONVEYOR_PROCAL_VERIFIC_IP_ADDRESS 	= ConstantAppConfigReader.getString("conveyor_procal_verific", "IpAddress");
			ConstantConveyorConfig.CONVEYOR_PROCAL_VERIFIC_IP_PORT 		= ConstantAppConfigReader.getString("conveyor_procal_verific", "IpPort"); 
			ConstantConveyorConfig.CONVEYOR_PROCAL_VERIFIC_END_PATH 	= ConstantAppConfigReader.getString("conveyor_procal_verific", "targetEndPath"); 
			ConstantConveyorConfig.CONVEYOR_PROCAL_VERIFIC_CMD_LOCATION = ConstantAppConfigReader.getString("conveyor_procal_verific", "CmdLocation");       
			ConstantConveyorConfig.CONVEYOR_PROCAL_VERIFIC_JAR_FILE 	= ConstantAppConfigReader.getString("conveyor_procal_verific", "JarFile");
			
			ConstantConveyorConfig.CONVEYOR_PROCAL_STA1_IP_ADDRESS 		= ConstantAppConfigReader.getString("conveyor_procal_sta1", "IpAddress");
			ConstantConveyorConfig.CONVEYOR_PROCAL_STA1_IP_PORT 		= ConstantAppConfigReader.getString("conveyor_procal_sta1", "IpPort"); 
			ConstantConveyorConfig.CONVEYOR_PROCAL_STA1_END_PATH 		= ConstantAppConfigReader.getString("conveyor_procal_sta1", "targetEndPath"); 
			ConstantConveyorConfig.CONVEYOR_PROCAL_STA1_CMD_LOCATION 	= ConstantAppConfigReader.getString("conveyor_procal_sta1", "CmdLocation");       
			ConstantConveyorConfig.CONVEYOR_PROCAL_STA1_JAR_FILE 		= ConstantAppConfigReader.getString("conveyor_procal_sta1", "JarFile");
			
			ConstantConveyorConfig.CONVEYOR_PROCAL_STA2_IP_ADDRESS 		= ConstantAppConfigReader.getString("conveyor_procal_sta2", "IpAddress");
			ConstantConveyorConfig.CONVEYOR_PROCAL_STA2_IP_PORT 		= ConstantAppConfigReader.getString("conveyor_procal_sta2", "IpPort"); 
			ConstantConveyorConfig.CONVEYOR_PROCAL_STA2_END_PATH 		= ConstantAppConfigReader.getString("conveyor_procal_sta2", "targetEndPath"); 
			ConstantConveyorConfig.CONVEYOR_PROCAL_STA2_CMD_LOCATION 	= ConstantAppConfigReader.getString("conveyor_procal_sta2", "CmdLocation");       
			ConstantConveyorConfig.CONVEYOR_PROCAL_STA2_JAR_FILE 		= ConstantAppConfigReader.getString("conveyor_procal_sta2", "JarFile");
			
			ConstantConveyorConfig.CONVEYOR_REJECTION_CMD_LOCATION               = ConstantAppConfigReader.getString("conveyor_python_rejection", "CmdLocation");
			ConstantConveyorConfig.CONVEYOR_REJECTION_PYTHON_SCRIPT              = ConstantAppConfigReader.getString("conveyor_python_rejection", "PythonScript");  
			ConstantConveyorConfig.CONVEYOR_REJECTION_PYTHON_SCRIPT_SCREEN_NUMBER              = ConstantAppConfigReader.getString("conveyor_python_rejection", "PythonScriptScreenNumber");       
			
			ConstantConveyorConfig.CONVEYOR_REJECTION_PYTHON_EXECUTABLE_PATH     = ConstantAppConfigReader.getString("conveyor_python_rejection", "PythonExecutablePath"); 
			
			
			ConstantConveyorConfig.CONVEYOR_REJECTION_MONITOR_DISPLAY_NAME     = ConstantAppConfigReader.getString("conveyor_python_rejection", "MonitorDisplayName"); 
			ConstantConveyorConfig.CONVEYOR_REJECTION_BAY_DISPLAY_NAME    			   = ConstantAppConfigReader.getString("conveyor_python_rejection", "BayNameDisplay"); 
			ConstantConveyorConfig.CONVEYOR_REJECTION_PORT    			   = ConstantAppConfigReader.getString("conveyor_python_rejection", "port"); 
			
			
			ConstantConveyorConfig.CONVEYOR_REJECTION_MONITOR_MANUFACTURER_NAME     = ConstantAppConfigReader.getString("conveyor_python_rejection", "MonitorManufacturerName"); 
			ConstantConveyorConfig.CONVEYOR_REJECTION_MONITOR_PID_NO     = ConstantAppConfigReader.getString("conveyor_python_rejection", "MonitorPidNo"); 
			ConstantConveyorConfig.CONVEYOR_REJECTION_MONITOR_SERIAL_NO     = ConstantAppConfigReader.getString("conveyor_python_rejection", "MonitorSerialNo"); 
			
			
			ConstantConveyorConfig.CONVEYOR_REJECTION_SCREEN_NUMBER				 = ConstantAppConfigReader.getInt("conveyor_python_rejection", "ScreenNumber");
			ConstantConveyorConfig.CONVEYOR_REJECTION_CLUSTER_ID				 = ConstantAppConfigReader.getString("conveyor_python_rejection", "RejectionBayClusterId");
			ConstantConveyorConfig.REST_API_TAIL_END_REJECTION_DISPLAY           = ConstantAppConfigReader.getString("conveyor_python_rejection", "RejectionServerTailEnd");
			ConstantConveyorConfig.REST_API_BATCH_UPDATE_TAIL_END_REJECTION_DISPLAY           = ConstantAppConfigReader.getString("conveyor_python_rejection", "RejectionServerBatchUpdateTailEnd");
			ConstantConveyorConfig.REST_API_IDLE_DISPLAY_TAIL_END_REJECTION_DISPLAY           = ConstantAppConfigReader.getString("conveyor_python_rejection", "RejectionServerIdleDisplayTailEnd");

			
			ConstantConveyorConfig.CONVEYOR_UNLOADING_CLUSTER_ID				 = ConstantAppConfigReader.getString("conveyor_python_unloading", "UnloadingBayClusterId");
			ConstantConveyorConfig.REST_API_TAIL_END_UNLOADING_DISPLAY           = ConstantAppConfigReader.getString("conveyor_python_unloading", "UnloadingServerTailEnd");
			ConstantConveyorConfig.REST_API_BATCH_UPDATE_TAIL_END_UNLOADING_DISPLAY           = ConstantAppConfigReader.getString("conveyor_python_unloading", "UnloadingServerBatchUpdateTailEnd");
			ConstantConveyorConfig.REST_API_IDLE_DISPLAY_TAIL_END_UNLOADING_DISPLAY           = ConstantAppConfigReader.getString("conveyor_python_unloading", "RejectionServerIdleDisplayTailEnd");

			
			ConstantConveyorConfig.CONVEYOR_UNLOADING_CMD_LOCATION               = ConstantAppConfigReader.getString("conveyor_python_unloading", "CmdLocation");    
			ConstantConveyorConfig.CONVEYOR_UNLOADING_PYTHON_SCRIPT              = ConstantAppConfigReader.getString("conveyor_python_unloading", "PythonScript");       
			ConstantConveyorConfig.CONVEYOR_UNLOADING_PYTHON_SCRIPT_SCREEN_NUMBER              = ConstantAppConfigReader.getString("conveyor_python_unloading", "PythonScriptScreenNumber");       
			
			ConstantConveyorConfig.CONVEYOR_UNLOADING_PYTHON_EXECUTABLE_PATH     = ConstantAppConfigReader.getString("conveyor_python_unloading", "PythonExecutablePath");
			ConstantConveyorConfig.CONVEYOR_UNLOADING_SCREEN_NUMBER				 = ConstantAppConfigReader.getInt("conveyor_python_unloading", "ScreenNumber");
			
			ConstantConveyorConfig.CONVEYOR_UNLOADING_MONITOR_DISPLAY_NAME     = ConstantAppConfigReader.getString("conveyor_python_unloading", "MonitorDisplayName"); 
			ConstantConveyorConfig.CONVEYOR_UNLOADING_BAY_DISPLAY_NAME    			   = ConstantAppConfigReader.getString("conveyor_python_unloading", "BayNameDisplay"); 
			ConstantConveyorConfig.CONVEYOR_UNLOADING_PORT    			   = ConstantAppConfigReader.getString("conveyor_python_unloading", "port"); 
			
			ConstantConveyorConfig.CONVEYOR_UNLOADING_MONITOR_MANUFACTURER_NAME     = ConstantAppConfigReader.getString("conveyor_python_unloading", "MonitorManufacturerName"); 
			ConstantConveyorConfig.CONVEYOR_UNLOADING_MONITOR_PID_NO     = ConstantAppConfigReader.getString("conveyor_python_unloading", "MonitorPidNo"); 
			ConstantConveyorConfig.CONVEYOR_UNLOADING_MONITOR_SERIAL_NO     = ConstantAppConfigReader.getString("conveyor_python_unloading", "MonitorSerialNo"); 
			
			
			
			//ConstantConveyorConfig.BAY_NAME_FT = ConstantAppConfigReader.getString("conveyor", "BayNameFt");

			
			
			//ApplicationLauncher.logger.debug("LoadConfigProperty : printStyle: getReportPrintStyleName: " + printStyle.get(0).getReportPrintStyleName());

			/*		Gson gson = new Gson();
		String yourJson = printStyle.toString();
		List<PrintStyle> data1 = gson.fromJson(yourJson, PrintStyle.class);
		ApplicationLauncher.logger.debug("LoadConfigProperty : printStyle: data1: " + data1);*/

			ConstantReportV2.init();

		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("LoadConfigProperty : Exception2: "+ e.getMessage());


		}


	}

	public void LoadPropertiesFromDB(){
		ApplicationLauncher.logger.debug("LoadPropertiesFromDB :Entry");
		ConstantAppConfig.SECOND_VALIDATION_RETRY_COUNT = ConstantAppConfigReader.getInt("StablizationValidation", "DefaultRefStandardValidationRetryCount"); 
		ConstantAppConfig.SECOND_VALIDATION_WAIT_TIME = ConstantAppConfigReader.getInt("StablizationValidation", "DefaultRefStandardValidationWaitTimeInSec"); 
		ConstantAppConfig.VOLT_ACCEPTED_PERCENTAGE = ConstantAppConfigReader.getInt("AcceptedErrorPercentage",ConstantApp.VOLTAGE_ACCEPTED_PERCENTAGE_KEY); 
		ConstantAppConfig.CURRENT_ACCEPTED_PERCENTAGE = ConstantAppConfigReader.getInt("AcceptedErrorPercentage", ConstantApp.CURRENT_ACCEPTED_PERCENTAGE_KEY); 
		ConstantAppConfig.DEGREE_ACCEPTED_PERCENTAGE = ConstantAppConfigReader.getInt("AcceptedErrorPercentage", ConstantApp.PHASE_ACCEPTED_PERCENTAGE_KEY); 
		ConstantAppConfig.FREQUENCY_ACCEPTED_PERCENTAGE = ConstantAppConfigReader.getFloat("AcceptedErrorPercentage", ConstantApp.FREQUENCY_ACCEPTED_PERCENTAGE_KEY);
		ConstantAppConfig.HAR_VOLT_ACCEPTED_PERCENTAGE = ConstantAppConfigReader.getInt("AcceptedErrorPercentage", ConstantApp.HAR_VOLTAGE_ACCEPTED_PERCENTAGE_KEY); 
		ConstantAppConfig.HAR_CURRENT_ACCEPTED_PERCENTAGE = ConstantAppConfigReader.getInt("AcceptedErrorPercentage", ConstantApp.HAR_CURRENT_ACCEPTED_PERCENTAGE_KEY); 

		JSONObject system_properties = MySQL_Controller.sp_getsystem_config();
		try {
			if(system_properties.length()>0){
				JSONArray properties_arr = system_properties.getJSONArray("Properties");
				ApplicationLauncher.logger.debug("LoadPropertiesFromDB :properties_arr" + properties_arr.length());
				if(properties_arr.length() == ConstantApp.TOTAL_NO_OF_SYSTEM_CONFIG_KEY){
					JSONObject jobj= new JSONObject ();
					for(int i=0; i<properties_arr.length(); i++){
						jobj = properties_arr.getJSONObject(i);
						String property_name = jobj.getString("property");
						String property_value = jobj.getString("value");
						setCorrespondingProperty(property_name, property_value);
						ApplicationLauncher.logger.debug("LoadPropertiesFromDB :property_name:"+property_name);
						ApplicationLauncher.logger.debug("LoadPropertiesFromDB :property_value:"+property_value);

					}
				}
			}


		} catch (JSONException e) {
			ApplicationLauncher.logger.error("LoadPropertiesFromDB :property_value: JSONException"+ e.getMessage());

			e.printStackTrace();
		}
	}

	public void setCorrespondingProperty(String property_name, String property_value){
		switch(property_name){

		case ConstantApp.VOLTAGE_ACCEPTED_PERCENTAGE_KEY:
			ConstantAppConfig.VOLT_ACCEPTED_PERCENTAGE = Integer.parseInt(property_value); 
			break;

		case ConstantApp.CURRENT_ACCEPTED_PERCENTAGE_KEY:
			ConstantAppConfig.CURRENT_ACCEPTED_PERCENTAGE = Integer.parseInt(property_value); 
			break;

		case ConstantApp.PHASE_ACCEPTED_PERCENTAGE_KEY:
			ConstantAppConfig.DEGREE_ACCEPTED_PERCENTAGE = Integer.parseInt(property_value); 
			ApplicationLauncher.logger.info("setCorrespondingProperty : DEGREE_ACCEPTED_PERCENTAGE:"+ ConstantAppConfig.DEGREE_ACCEPTED_PERCENTAGE);
			break;

		case ConstantApp.FREQUENCY_ACCEPTED_PERCENTAGE_KEY:
			ConstantAppConfig.FREQUENCY_ACCEPTED_PERCENTAGE = Float.parseFloat(property_value); 
			break;

		case ConstantApp.HAR_VOLTAGE_ACCEPTED_PERCENTAGE_KEY:
			ConstantAppConfig.HAR_VOLT_ACCEPTED_PERCENTAGE = Integer.parseInt(property_value); 
			break;

		case ConstantApp.HAR_CURRENT_ACCEPTED_PERCENTAGE_KEY:
			ConstantAppConfig.HAR_CURRENT_ACCEPTED_PERCENTAGE = Integer.parseInt(property_value); 
			break;

		case ConstantApp.SECOND_VALIDATION_RETRY_COUNT_KEY:
			ConstantAppConfig.SECOND_VALIDATION_RETRY_COUNT = Integer.parseInt(property_value); 
			break;

		case ConstantApp.SECOND_VALIDATION_WAIT_TIME_IN_SEC_KEY:
			ConstantAppConfig.SECOND_VALIDATION_WAIT_TIME = Integer.parseInt(property_value); 
			break;

		default:
			break;
		}
	}

	public static ArrayList<String> getIMappingDefaultValues(){
		ArrayList<String> default_values = new ArrayList<String>();
		for (int i = 1; i <= ConstantAppConfig.I_MAPPING_SIZE; i++){
			try{
				if(ConstantAppConfigReader.getString("IMappingDefaultValues", "I"+i)!=null){
					default_values.add(ConstantAppConfigReader.getString("IMappingDefaultValues", "I"+i));
				}
			}catch(Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("getIMappingDefaultValues: Exception:" + i+":"+e.getMessage());
			}
		}

		return default_values;
	}

	public static ArrayList<String> getPF_MappingDefaultValues(){
		ArrayList<String> default_values = new ArrayList<String>();
		for (int i = 1; i <= ConstantAppConfig.PF_MAPPING_SIZE; i++){
			try{
				if(ConstantAppConfigReader.getString("PF_MappingDefaultValues", "PF"+i)!=null){
					default_values.add(ConstantAppConfigReader.getString("PF_MappingDefaultValues", "PF"+i));
				}
			}catch(Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("getPF_MappingDefaultValues: Exception:" + i+":"+e.getMessage());
			}
		}

		return default_values;
	}

	public ArrayList<String> getABC_PF_Values(){
		ApplicationLauncher.logger.info("getABC_PF_Values: Entry:");
		ArrayList<String> default_values = new ArrayList<String>();


		for (int i = 1; i <= ConstantAppConfig.PF_MAPPING_SIZE; i++){
			try{
				if(ConstantAppConfigReader.getString("ABC_PF_MappingDefaultValues", "PF"+i)!=null){
					default_values.add(ConstantAppConfigReader.getString("ABC_PF_MappingDefaultValues", "PF"+i));
				}
			}catch(Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("getABC_PF_Values: Exception:" + i+":"+e.getMessage());
			}
		}

		return default_values;
	}

	public ArrayList<String> getA_B_C_PF_Values(){
		ApplicationLauncher.logger.info("getA_B_C_PF_Values: Entry" );
		ArrayList<String> default_values = new ArrayList<String>();

		for (int i = 1; i <= ConstantAppConfig.PF_MAPPING_SIZE; i++){
			try{
				if(ConstantAppConfigReader.getString("A_B_C_PF_MappingDefaultValues", "PF"+i)!=null){
					default_values.add(ConstantAppConfigReader.getString("A_B_C_PF_MappingDefaultValues", "PF"+i));
				}
			}catch(Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("getA_B_C_PF_Values: Exception:" + i+":"+e.getMessage());
			}
		}

		return default_values;
	}



	public ArrayList<String> getPF_RowHeaderValues(){
		ApplicationLauncher.logger.info("getPF_RowHeaderValues: Entry" );
		ArrayList<String> default_values = new ArrayList<String>();

		for (int i = 1; i <= ConstantAppConfig.PF_MAPPING_SIZE; i++){
			try{
				if(ConstantAppConfigReader.getString("PF_MappingRowHeader", "PF"+i)!=null){
					default_values.add(ConstantAppConfigReader.getString("PF_MappingRowHeader", "PF"+i));
				}
			}catch(Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("getPF_RowHeaderValues: Exception:" + i+":"+e.getMessage());
			}
		}

		return default_values;
	}

	public static void LoadReportHeaderConfigProperty(){
		ResetAllReportConfigValues();
		ArrayList<String> test_types_list = ConstantReport.REPORT_TEST_TYPES;
		ApplicationLauncher.logger.info("LoadSavedData : test_types_list: " + test_types_list);
		for(int i=0; i<test_types_list.size(); i++){
			FetchDataByTestType(test_types_list.get(i));
		}

	}

	public static void ResetAllReportConfigValues(){
		ConstantReport.FREQ_TEMPL_VOLTAGE = "";
		ConstantReport.FREQ_TEMPL_CURRENTS = new ArrayList<String>();
		ConstantReport.FREQ_TEMPL_PFS = new ArrayList<String>();
		ConstantReport.FREQ_TEMPL_FREQUENCIES = new ArrayList<String>();
		ConstantReport.FREQ_TEMPL_DEFAULT_FREQ = "";
		ConstantReport.VV_TEMPL_VOLTS = new ArrayList<String>();
		ConstantReport.VV_TEMPL_CURRENTS = new ArrayList<String>();
		ConstantReport.VV_TEMPL_PFS = new ArrayList<String>();
		ConstantReport.VV_TEMPL_DEFAULT_VOLT = "";
		ConstantReport.REP_TEMPL_VOLTAGE = "";
		ConstantReport.REP_TEMPL_CURRENTS = new ArrayList<String>();
		ConstantReport.REP_TEMPL_PF = "";
		ConstantReport.REP_TEMPL_NO_OF_TESTS = 0;
		ConstantReport.SELF_HEAT_TEMPL_VOLTAGE = "";
		ConstantReport.SELF_HEAT_TEMPL_CURRENTS = new ArrayList<String>();
		ConstantReport.SELF_HEAT_TEMPL_PFS = new ArrayList<String>();
		ConstantReport.SELF_HEAT_TEMPL_NO_OF_TESTS = 0;
		ConstantReport.ACC_TEMPL_VOLT = "";
		ConstantReport.ACC_TEMPL_CURRENTS = new ArrayList<String>();
		ConstantReport.ACC_TEMPL_PFS = new ArrayList<String>();
		ConstantReport.REP_TEMPL_VOLTAGE = "";
		ConstantReport.RPS_TEMPL_CURRENT = "";
		ConstantReport.RPS_TEMPL_PF = "";
		ConstantReport.HARM_TEMPL_VOLTAGE = "";
		ConstantReport.HARM_TEMPL_CURRENTS = new ArrayList<String>();
		ConstantReport.HARM_TEMPL_PF = "";
		ConstantReport.HARM_TEMPL_HARM_TIMES = new ArrayList<String>();
		ConstantReport.VU_TEMPL_VOLTAGES = new ArrayList<String>();
		ConstantReport.VU_TEMPL_CURRENT = "";
		ConstantReport.VU_TEMPL_PF = "";
		ConstantReport.VU_TEMPL_DEF_VOLT = "";
		ConstantReport.CONST_TEMPL_VOLTAGE = "";
		ConstantReport.CONST_TEMPL_CURRENT = "";
		ConstantReport.CONST_TEMPL_PF = "";
		ConstantReport.CONST_TEMPL_POWER = "";
		ConstantReport.CREEP_TEMPL_VOLTAGE = "";
		ConstantReport.STA_TEMPL_VOLTAGE = "";
		ConstantReport.STA_TEMPL_CURRENT = "";
		ConstantReport.UNBALANCED_LOAD_TEMPL_CURRENTS = new ArrayList<String>();
		ConstantReport.UNBALANCED_LOAD_TEMPL_PFS = new ArrayList<String>();
	}

	public static void FetchDataByTestType(String test_type){
		JSONObject report_header_config = MySQL_Controller.sp_getreport_header_config(ConstantAppConfig.REPORT_PROFILE_LIST.get(0),test_type);
		try{
			JSONArray report_header_arr = report_header_config.getJSONArray("Report_Headers");
			ApplicationLauncher.logger.debug("LoadSavedData : report_header_arr: " + report_header_arr);
			if(report_header_arr.length() != 0){
				JSONObject jobj = new JSONObject();
				for(int i=0; i<report_header_arr.length();i++){
					jobj = report_header_arr.getJSONObject(i);
					String header_type = jobj.getString("header_type");
					String header_value = jobj.getString("header_value");
					LoadReportHeaderConfigValues(test_type,header_type, header_value);
				}
			}	
		}
		catch(JSONException e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("FetchDataByTestType : JSONException: " + e.getMessage());
		}
	}

	public static void LoadReportHeaderConfigValues(String test_type,String header_type, String header_value){
		switch(test_type){
		//case "InfluenceFreq":
		case ConstantApp.TEST_PROFILE_INFLUENCE_FREQ:
			LoadFVToReportProperty(header_type, header_value);
			break;

			//case "InfluenceVolt":
		case ConstantApp.TEST_PROFILE_INFLUENCE_VOLT:
			LoadVVToReportProperty(header_type, header_value);
			break;

			//case "Repeatability":
		case ConstantApp.TEST_PROFILE_REPEATABILITY:
			LoadREPToReportProperty(header_type, header_value);
			break;

			//case "SelfHeating":
		case ConstantApp.TEST_PROFILE_SELF_HEATING:
			LoadSELFHToReportProperty(header_type, header_value);
			break;

			//case "Accuracy":
		case ConstantApp.TEST_PROFILE_ACCURACY:
			LoadACCToReportProperty(header_type, header_value);
			break;

			//case "PhaseReversal":
		case ConstantApp.TEST_PROFILE_PHASE_REVERSAL:
			LoadRPSToReportProperty(header_type, header_value);
			break;

			//case "InfluenceHarmonic":
		case ConstantApp.TEST_PROFILE_INFLUENCE_HARMONIC:
			LoadHARMToReportProperty(header_type, header_value);
			break;

			//case "VoltageUnbalance":
		case ConstantApp.TEST_PROFILE_VOLTAGE_UNBALANCE:
			LoadVUToReportProperty(header_type, header_value);
			break;

			//case "ConstantTest":
		case ConstantApp.TEST_PROFILE_CONSTANT_TEST:
			LoadCONSTToReportProperty(header_type, header_value);
			break;

			//case "NoLoad":
		case ConstantApp.TEST_PROFILE_NOLOAD:
			LoadCREEPToReportProperty(header_type, header_value);
			break;

			//case "STA":
		case ConstantApp.TEST_PROFILE_STA:
			LoadSTAToReportProperty(header_type, header_value);
			break;

			//case "UnbalancedLoad":
		case ConstantApp.TEST_PROFILE_UNBALANCED_LOAD:
			if(ProcalFeatureEnable.REPORT_3PHASE_UNBALANCED_LOAD){
				LoadUnbalanceLoadToReportProperty(header_type, header_value);
			}
			break;
		default:
			break;
		}
	}

	public static void LoadFVToReportProperty(String header_type, String header_value){
		if(header_type.equals(ConstantReport.HEADER_TYPE_VOLTAGE)){
			ConstantReport.FREQ_TEMPL_VOLTAGE = header_value;
		}
		else if(header_type.equals(ConstantReport.HEADER_TYPE_CURRENT)){
			ConstantReport.FREQ_TEMPL_CURRENTS.add(header_value);
		}
		else if(header_type.equals(ConstantReport.HEADER_TYPE_PF)){
			ConstantReport.FREQ_TEMPL_PFS.add(header_value);
		}
		else if(header_type.equals(ConstantReport.HEADER_TYPE_FREQUENCY)){
			ConstantReport.FREQ_TEMPL_FREQUENCIES.add(header_value);
		}
		else if(header_type.equals(ConstantReport.HEADER_TYPE_REFERENCE_VALUE)){
			ConstantReport.FREQ_TEMPL_DEFAULT_FREQ = header_value;
		}
		else{
			ApplicationLauncher.logger.info("LoadFVToReportProperty : Mismatch");
		}
	}

	public static void LoadVVToReportProperty(String header_type, String header_value){
		if(header_type.equals(ConstantReport.HEADER_TYPE_VOLTAGE)){
			ConstantReport.VV_TEMPL_VOLTS.add(header_value);
		}
		else if(header_type.equals(ConstantReport.HEADER_TYPE_CURRENT)){
			ConstantReport.VV_TEMPL_CURRENTS.add(header_value);
		}
		else if(header_type.equals(ConstantReport.HEADER_TYPE_PF)){
			ConstantReport.VV_TEMPL_PFS.add(header_value);
		}
		else if(header_type.equals(ConstantReport.HEADER_TYPE_REFERENCE_VALUE)){
			ConstantReport.VV_TEMPL_DEFAULT_VOLT = header_value;
		}
		else{
			ApplicationLauncher.logger.info("LoadVVToReportProperty : Mismatch");
		}
	}

	public static void LoadREPToReportProperty(String header_type, String header_value){
		if(header_type.equals(ConstantReport.HEADER_TYPE_VOLTAGE)){
			ConstantReport.REP_TEMPL_VOLTAGE = header_value;
		}
		else if(header_type.equals(ConstantReport.HEADER_TYPE_CURRENT)){
			ConstantReport.REP_TEMPL_CURRENTS.add(header_value);
		}
		else if(header_type.equals(ConstantReport.HEADER_TYPE_PF)){
			ConstantReport.REP_TEMPL_PF = header_value;
		}
		else if(header_type.equals(ConstantReport.HEADER_TYPE_REFERENCE_VALUE)){
			ConstantReport.REP_TEMPL_NO_OF_TESTS = Integer.parseInt(header_value);
		}
		else{
			ApplicationLauncher.logger.info("LoadREPToReportProperty : Mismatch");
		}
	}

	public static void LoadSELFHToReportProperty(String header_type, String header_value){
		if(header_type.equals(ConstantReport.HEADER_TYPE_VOLTAGE)){
			ConstantReport.SELF_HEAT_TEMPL_VOLTAGE = header_value;
		}
		else if(header_type.equals(ConstantReport.HEADER_TYPE_CURRENT)){
			ConstantReport.SELF_HEAT_TEMPL_CURRENTS.add(header_value);
		}
		else if(header_type.equals(ConstantReport.HEADER_TYPE_PF)){
			ConstantReport.SELF_HEAT_TEMPL_PFS.add(header_value);
		}
		else if(header_type.equals(ConstantReport.HEADER_TYPE_REFERENCE_VALUE)){
			ConstantReport.SELF_HEAT_TEMPL_NO_OF_TESTS = Integer.parseInt(header_value);
		}
		else{
			ApplicationLauncher.logger.info("LoadSELFHToReportProperty : Mismatch");
		}
	}

	public static void LoadACCToReportProperty(String header_type, String header_value){
		if(header_type.equals(ConstantReport.HEADER_TYPE_VOLTAGE)){
			ConstantReport.ACC_TEMPL_VOLT = header_value;
		}
		else if(header_type.equals(ConstantReport.HEADER_TYPE_CURRENT)){
			ConstantReport.ACC_TEMPL_CURRENTS.add(header_value);
		}
		else if(header_type.equals(ConstantReport.HEADER_TYPE_PF)){
			ConstantReport.ACC_TEMPL_PFS.add(header_value);
		}
		else{
			ApplicationLauncher.logger.info("LoadACCToReportProperty : Mismatch");
		}
	}

	public static void LoadRPSToReportProperty(String header_type, String header_value){
		if(header_type.equals(ConstantReport.HEADER_TYPE_VOLTAGE)){
			ConstantReport.RPS_TEMPL_VOLTAGE = header_value;
		}
		else if(header_type.equals(ConstantReport.HEADER_TYPE_CURRENT)){
			ConstantReport.RPS_TEMPL_CURRENT = header_value;
		}
		else if(header_type.equals(ConstantReport.HEADER_TYPE_PF)){
			ConstantReport.RPS_TEMPL_PF = header_value;
		}
		else{
			ApplicationLauncher.logger.info("LoadRPSToReportProperty : Mismatch");
		}
	}

	public static void LoadHARMToReportProperty(String header_type, String header_value){
		if(header_type.equals(ConstantReport.HEADER_TYPE_VOLTAGE)){
			ConstantReport.HARM_TEMPL_VOLTAGE = header_value;
		}
		else if(header_type.equals(ConstantReport.HEADER_TYPE_CURRENT)){
			ConstantReport.HARM_TEMPL_CURRENTS.add(header_value);
		}
		else if(header_type.equals(ConstantReport.HEADER_TYPE_PF)){
			ConstantReport.HARM_TEMPL_PF = header_value;
		}
		else if(header_type.equals(ConstantReport.HEADER_TYPE_HARMONICS)){
			ConstantReport.HARM_TEMPL_HARM_TIMES.add(header_value);
		}
		else{
			ApplicationLauncher.logger.info("LoadHARMToReportProperty : Mismatch");
		}
	}

	public static void LoadVUToReportProperty(String header_type, String header_value){
		if(header_type.equals(ConstantReport.HEADER_TYPE_VOLTAGE)){
			ConstantReport.VU_TEMPL_VOLTAGES.add(header_value);
		}
		else if(header_type.equals(ConstantReport.HEADER_TYPE_CURRENT)){
			ConstantReport.VU_TEMPL_CURRENT = header_value;
		}
		else if(header_type.equals(ConstantReport.HEADER_TYPE_PF)){
			ConstantReport.VU_TEMPL_PF = header_value;
		}
		else if(header_type.equals(ConstantReport.HEADER_TYPE_REFERENCE_VALUE)){
			ConstantReport.VU_TEMPL_DEF_VOLT = header_value;
		}
		else{
			ApplicationLauncher.logger.info("LoadVUToReportProperty : Mismatch");
		}
	}

	public static void LoadCONSTToReportProperty(String header_type, String header_value){
		if(header_type.equals(ConstantReport.HEADER_TYPE_VOLTAGE)){
			ConstantReport.CONST_TEMPL_VOLTAGE = header_value;
		}
		else if(header_type.equals(ConstantReport.HEADER_TYPE_CURRENT)){
			ConstantReport.CONST_TEMPL_CURRENT = header_value;
		}
		else if(header_type.equals(ConstantReport.HEADER_TYPE_PF)){
			ConstantReport.CONST_TEMPL_PF = header_value;
		}
		else if(header_type.equals(ConstantReport.HEADER_TYPE_REFERENCE_VALUE)){
			ConstantReport.CONST_TEMPL_POWER = header_value;
		}
		else{
			ApplicationLauncher.logger.info("LoadCONSTToReportProperty : Mismatch");
		}
	}

	public static void LoadCREEPToReportProperty(String header_type, String header_value){
		if(header_type.equals(ConstantReport.HEADER_TYPE_VOLTAGE)){
			ConstantReport.CREEP_TEMPL_VOLTAGE = header_value;
		}
		else{
			ApplicationLauncher.logger.info("LoadCREEPToReportProperty : Mismatch");
		}
	}

	public static void LoadSTAToReportProperty(String header_type, String header_value){
		if(header_type.equals(ConstantReport.HEADER_TYPE_VOLTAGE)){
			ConstantReport.STA_TEMPL_VOLTAGE = header_value;
		}
		else if(header_type.equals(ConstantReport.HEADER_TYPE_CURRENT)){
			ConstantReport.STA_TEMPL_CURRENT = header_value;
		}
		else{
			ApplicationLauncher.logger.info("LoadSTAToReportProperty : Mismatch");
		}
	}

	public static void LoadUnbalanceLoadToReportProperty(String header_type, String header_value){
		if(header_type.equals(ConstantReport.HEADER_TYPE_CURRENT)){
			ConstantReport.UNBALANCED_LOAD_TEMPL_CURRENTS.add(header_value);
		}
		else if(header_type.equals(ConstantReport.HEADER_TYPE_PF)){
			ConstantReport.UNBALANCED_LOAD_TEMPL_PFS.add(header_value);
		}
		else{
			ApplicationLauncher.logger.info("LoadUnbalanceLoadToReportProperty : Mismatch");
		}
	}

	public static void LoadReportExcelConfigProperty(){
		ResetAllReportExcelConfigValues();
		ArrayList<String> test_types_list = ConstantReport.REPORT_TEST_TYPES;
		for(int i=0; i<test_types_list.size(); i++){
			FetchReportExcelDataByTestType(test_types_list.get(i));
		}

	}

	public static void ResetAllReportExcelConfigValues(){

		ConstantReport.FREQ_TEMPL_ROWS = new ArrayList<Integer>();
		ConstantReport.FREQ_TEMPL_METER_COLS = new ArrayList<Integer>();
		ConstantReport.FREQ_TEMPL_DEF_FREQ_COLS = new ArrayList<Integer>();
		ConstantReport.VV_TEMPL_ROWS = new ArrayList<Integer>();
		ConstantReport.VV_TEMPL_METER_COLS = new ArrayList<Integer>();
		ConstantReport.VV_TEMPL_DEF_VOLT_COLS = new ArrayList<Integer>();
		ConstantReport.REP_TEMPL_ROWS = new ArrayList<Integer>();
		ConstantReport.REP_TEMPL_METER_COLS = new ArrayList<Integer>();
		ConstantReport.REP_TEMPL_TEST_COL = 0;
		ConstantReport.SELF_HEAT_TEMPL_ROWS = new ArrayList<Integer>();
		ConstantReport.SELF_HEAT_TEMPL_METER_COLS = new ArrayList<Integer>();
		ConstantReport.SELF_HEAT_TEMPL_TEST_COLS = new ArrayList<Integer>();
		//ConstantReport.ACC_TEMPL_ROW = 0;
		//ConstantReport.ACC_TEMPL_METER_COL = 0;
		ConstantReport.ACC_TEMPL_ROW = new ArrayList<Integer>();
		ConstantReport.ACC_TEMPL_METER_COL = new ArrayList<Integer>();
		ConstantReport.ACC_TEMPL_DEF_I_COLS= new ArrayList<Integer>();
		ConstantReport.RPS_TEMPL_ROW = 0;
		ConstantReport.RPS_TEMPL_METER_COL = 0;
		ConstantReport.RPS_TEMPL_NORMAL_REV_COL = new ArrayList<Integer>();
		ConstantReport.HARM_TEMPL_ROWS = new ArrayList<Integer>();
		ConstantReport.HARM_TEMPL_METER_COLS = new ArrayList<Integer>();
		ConstantReport.HARM_TEMPL_PHASE_COLS = new ArrayList<Integer>();
		ConstantReport.VU_TEMPL_ROW = 0;
		ConstantReport.VU_TEMPL_METER_COL = 0;
		ConstantReport.VU_TEMPL_DEF_VOLT_COL = 0;
		ConstantReport.CONST_TEMPL_ROW = 0;
		ConstantReport.CONST_TEMPL_METER_COL = 0;
		ConstantReport.CONST_TEMPL_CONST_COLS = new ArrayList<Integer>();
		ConstantReport.CREEP_TEMPL_ROW = 0;
		ConstantReport.CREEP_TEMPL_METER_COL = 0;
		ConstantReport.CREEP_TEMPL_CREEP_COLS = new ArrayList<Integer>();
		ConstantReport.STA_TEMPL_ROW = 0;
		ConstantReport.STA_TEMPL_METER_COL = 0;
		ConstantReport.STA_TEMPL_STA_COLS = new ArrayList<Integer>();
		ConstantReport.UNBALANCED_LOAD_TEMPL_ROWS = new ArrayList<Integer>();
		ConstantReport.UNBALANCED_LOAD_TEMPL_METER_COLS = new ArrayList<Integer>();
		ConstantReport.UNBALANCED_LOAD_TEMPL_COLS = new ArrayList<Integer>();
	}

	public static void FetchReportExcelDataByTestType(String test_type){
		JSONObject report_excel_config = MySQL_Controller.sp_getreport_excel_config(ConstantAppConfig.REPORT_PROFILE_LIST.get(0),test_type);
		try{
			JSONArray report_excel_arr = report_excel_config.getJSONArray("Report_Excel_Cells");
			ApplicationLauncher.logger.info("LoadSavedData : report_excel_arr: " + report_excel_arr);
			if(report_excel_arr.length() != 0){
				JSONObject jobj = new JSONObject();
				for(int i=0; i<report_excel_arr.length();i++){
					jobj = report_excel_arr.getJSONObject(i);
					String cell_type = jobj.getString("cell_type");
					String cell_value = jobj.getString("cell_value");
					LoadReportExcelConfigValues(test_type,cell_type, cell_value);
				}
			}	
		}
		catch(JSONException e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("FetchReportExcelDataByTestType : JSONException: " + e.getMessage());

		}
	}

	public static void LoadReportExcelConfigValues(String test_type,String cell_type, 
			String cell_value){
		switch(test_type){
		//case "InfluenceFreq":
		case ConstantApp.TEST_PROFILE_INFLUENCE_FREQ:
			LoadFVExcelToReportProperty(cell_type, cell_value);
			break;
			//case "InfluenceVolt":
		case ConstantApp.TEST_PROFILE_INFLUENCE_VOLT:
			LoadVVExcelToReportProperty(cell_type, cell_value);
			break;
			//case "Repeatability":
		case ConstantApp.TEST_PROFILE_REPEATABILITY:
			LoadREPExcelToReportProperty(cell_type, cell_value);
			break;
			//case "SelfHeating":
		case ConstantApp.TEST_PROFILE_SELF_HEATING:
			LoadSELFHExcelToReportProperty(cell_type, cell_value);
			break;
			//case "Accuracy":
		case ConstantApp.TEST_PROFILE_ACCURACY:
			LoadACCExcelToReportProperty(cell_type, cell_value);
			break;
			//case "PhaseReversal":
		case ConstantApp.TEST_PROFILE_PHASE_REVERSAL:
			LoadRPSExcelToReportProperty(cell_type, cell_value);
			break;
			//case "InfluenceHarmonic":
		case ConstantApp.TEST_PROFILE_INFLUENCE_HARMONIC:
			LoadHARMExcelToReportProperty(cell_type, cell_value);
			break;
			//case "VoltageUnbalance":
		case ConstantApp.TEST_PROFILE_VOLTAGE_UNBALANCE:
			LoadVUExcelToReportProperty(cell_type, cell_value);
			break;
			//case "ConstantTest":
		case ConstantApp.TEST_PROFILE_CONSTANT_TEST:
			LoadCONSTExcelToReportProperty(cell_type, cell_value);
			break;
			//case "NoLoad":
		case ConstantApp.TEST_PROFILE_NOLOAD:
			LoadCREEPExcelToReportProperty(cell_type, cell_value);
			break;
			//case "STA":
		case ConstantApp.TEST_PROFILE_STA:
			LoadSTAExcelToReportProperty(cell_type, cell_value);
			break;
			//case "UnbalancedLoad":
		case ConstantApp.TEST_PROFILE_UNBALANCED_LOAD:
			if(ProcalFeatureEnable.REPORT_3PHASE_UNBALANCED_LOAD){
				LoadUnbalanceLoadExcelToReportProperty(cell_type, cell_value);
			}
			break;
		default:
			break;

		}
	}

	public static void LoadFVExcelToReportProperty(String cell_type, String cell_value){
		if(cell_type.equals(ConstantReport.EXCEL_ALPHA)){
			ConstantReport.FREQ_TEMPL_ROWS.add(getRowValueFromCellValue(cell_value));
			ConstantReport.FREQ_TEMPL_METER_COLS.add(getColValueFromCellValue(cell_value));
		}
		else if(cell_type.equals(ConstantReport.EXCEL_BETA)){
			ConstantReport.FREQ_TEMPL_DEF_FREQ_COLS.add(getColValueFromCellValue(cell_value));
		}
		else{
			ApplicationLauncher.logger.info("LoadFVExcelToReportProperty : Mismatch");
		}
	}

	public static void LoadVVExcelToReportProperty(String cell_type, String cell_value){
		if(cell_type.equals(ConstantReport.EXCEL_ALPHA)){
			ConstantReport.VV_TEMPL_ROWS.add(getRowValueFromCellValue(cell_value));
			ConstantReport.VV_TEMPL_METER_COLS.add(getColValueFromCellValue(cell_value));
		}
		else if(cell_type.equals(ConstantReport.EXCEL_BETA)){
			ConstantReport.VV_TEMPL_DEF_VOLT_COLS.add(getColValueFromCellValue(cell_value));
		}
		else{
			ApplicationLauncher.logger.info("LoadVVExcelToReportProperty : Mismatch");
		}
	}

	public static void LoadREPExcelToReportProperty(String cell_type, String cell_value){
		if(cell_type.equals(ConstantReport.EXCEL_ALPHA)){
			ConstantReport.REP_TEMPL_ROWS.add(getRowValueFromCellValue(cell_value));
			ConstantReport.REP_TEMPL_METER_COLS.add(getColValueFromCellValue(cell_value));
			//ApplicationLauncher.logger.info("LoadREPExcelToReportProperty : getRowValueFromCellValue(cell_value):"+getRowValueFromCellValue(cell_value));

		}
		else if(cell_type.equals(ConstantReport.EXCEL_BETA)){
			ConstantReport.REP_TEMPL_TEST_COL = getColValueFromCellValue(cell_value);
		}
		else{
			ApplicationLauncher.logger.info("LoadREPExcelToReportProperty : Mismatch");
		}
	}

	public static void LoadSELFHExcelToReportProperty(String cell_type, String cell_value){
		if(cell_type.equals(ConstantReport.EXCEL_ALPHA)){
			ConstantReport.SELF_HEAT_TEMPL_ROWS.add(getRowValueFromCellValue(cell_value));
			ConstantReport.SELF_HEAT_TEMPL_METER_COLS.add(getColValueFromCellValue(cell_value));
		}
		else if(cell_type.equals(ConstantReport.EXCEL_BETA)){
			ConstantReport.SELF_HEAT_TEMPL_TEST_COLS.add(getColValueFromCellValue(cell_value));
		}
		else{
			ApplicationLauncher.logger.info("LoadSELFHExcelToReportProperty : Mismatch");
		}
	}

	public static void LoadACCExcelToReportProperty(String cell_type, String cell_value){
		if(cell_type.equals(ConstantReport.EXCEL_ALPHA)){
			//ConstantReport.ACC_TEMPL_ROW = getRowValueFromCellValue(cell_value);
			//ConstantReport.ACC_TEMPL_METER_COL = getColValueFromCellValue(cell_value);
			ConstantReport.ACC_TEMPL_ROW.add(getRowValueFromCellValue(cell_value));
			ConstantReport.ACC_TEMPL_METER_COL.add(getColValueFromCellValue(cell_value));
		}
		else if(cell_type.equals(ConstantReport.EXCEL_BETA)){
			ConstantReport.ACC_TEMPL_DEF_I_COLS.add(getColValueFromCellValue(cell_value));
		}
		else{
			ApplicationLauncher.logger.info("LoadACCExcelToReportProperty : Mismatch");
		}
	}

	public static void LoadRPSExcelToReportProperty(String cell_type, String cell_value){
		if(cell_type.equals(ConstantReport.EXCEL_ALPHA)){
			ConstantReport.RPS_TEMPL_ROW = getRowValueFromCellValue(cell_value);
			ConstantReport.RPS_TEMPL_METER_COL = getColValueFromCellValue(cell_value);
		}
		else if(cell_type.equals(ConstantReport.EXCEL_BETA)){
			ConstantReport.RPS_TEMPL_NORMAL_REV_COL.add(getColValueFromCellValue(cell_value));
		}
		else{
			ApplicationLauncher.logger.info("LoadRPSExcelToReportProperty : Mismatch");
		}
	}

	public static void LoadHARMExcelToReportProperty(String cell_type, String cell_value){
		if(cell_type.equals(ConstantReport.EXCEL_ALPHA)){
			ConstantReport.HARM_TEMPL_ROWS.add(getRowValueFromCellValue(cell_value));
			ConstantReport.HARM_TEMPL_METER_COLS.add(getColValueFromCellValue(cell_value));
		}
		else if(cell_type.equals(ConstantReport.EXCEL_BETA)){
			ConstantReport.HARM_TEMPL_PHASE_COLS.add(getColValueFromCellValue(cell_value));
		}
		else{
			ApplicationLauncher.logger.info("LoadHARMExcelToReportProperty : Mismatch");
		}
	}

	public static void LoadVUExcelToReportProperty(String cell_type, String cell_value){
		if(cell_type.equals(ConstantReport.EXCEL_ALPHA)){
			ConstantReport.VU_TEMPL_ROW = getRowValueFromCellValue(cell_value);
			ConstantReport.VU_TEMPL_METER_COL = getColValueFromCellValue(cell_value);
		}
		else if(cell_type.equals(ConstantReport.EXCEL_BETA)){
			ConstantReport.VU_TEMPL_DEF_VOLT_COL = getColValueFromCellValue(cell_value);
		}
		else{
			ApplicationLauncher.logger.info("LoadVUExcelToReportProperty : Mismatch");
		}
	}

	public static void LoadCONSTExcelToReportProperty(String cell_type, String cell_value){
		if(cell_type.equals(ConstantReport.EXCEL_ALPHA)){
			ConstantReport.CONST_TEMPL_ROW = getRowValueFromCellValue(cell_value);
			ConstantReport.CONST_TEMPL_METER_COL = getColValueFromCellValue(cell_value);
		}
		else if(cell_type.equals(ConstantReport.EXCEL_BETA)){
			ConstantReport.CONST_TEMPL_CONST_COLS.add(getColValueFromCellValue(cell_value));
		}
		else{
			ApplicationLauncher.logger.info("LoadVUExcelToReportProperty : Mismatch");
		}
	}

	public static void LoadCREEPExcelToReportProperty(String cell_type, String cell_value){
		if(cell_type.equals(ConstantReport.EXCEL_ALPHA)){
			ConstantReport.CREEP_TEMPL_ROW = getRowValueFromCellValue(cell_value);
			ConstantReport.CREEP_TEMPL_METER_COL = getColValueFromCellValue(cell_value);
		}
		else if(cell_type.equals(ConstantReport.EXCEL_BETA)){
			ConstantReport.CREEP_TEMPL_CREEP_COLS.add(getColValueFromCellValue(cell_value));
		}
		else{
			ApplicationLauncher.logger.info("LoadCREEPExcelToReportProperty : Mismatch");
		}
	}

	public static void LoadSTAExcelToReportProperty(String cell_type, String cell_value){
		if(cell_type.equals(ConstantReport.EXCEL_ALPHA)){
			ConstantReport.STA_TEMPL_ROW = getRowValueFromCellValue(cell_value);
			ConstantReport.STA_TEMPL_METER_COL = getColValueFromCellValue(cell_value);
		}
		else if(cell_type.equals(ConstantReport.EXCEL_BETA)){
			ConstantReport.STA_TEMPL_STA_COLS.add(getColValueFromCellValue(cell_value));
		}
		else{
			ApplicationLauncher.logger.info("LoadSTAExcelToReportProperty : Mismatch");
		}
	}

	public static void LoadUnbalanceLoadExcelToReportProperty(String cell_type, String cell_value){
		if(cell_type.equals(ConstantReport.EXCEL_ALPHA)){
			ConstantReport.UNBALANCED_LOAD_TEMPL_ROWS.add(getRowValueFromCellValue(cell_value));
			ConstantReport.UNBALANCED_LOAD_TEMPL_METER_COLS.add(getColValueFromCellValue(cell_value));
		}
		else if(cell_type.equals(ConstantReport.EXCEL_BETA)){
			ConstantReport.UNBALANCED_LOAD_TEMPL_COLS.add(getColValueFromCellValue(cell_value));
		}
		else{
			ApplicationLauncher.logger.info("LoadUnbalanceLoadExcelToReportProperty : Mismatch");
		}
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

	public void LoadReportFileLocationProperty(){
		ResetAllReportFileLocation();
		ArrayList<String> test_types_list = ConstantReport.REPORT_TEST_TYPES;
		for(int i=0; i<test_types_list.size(); i++){
			FetchFileLocationByTestType(test_types_list.get(i));
		}
	}

	public void ResetAllReportFileLocation(){
		ConstantReport.SAVE_FILE_LOCATION =  "";
		ConstantReport.INPUT_TEMPLATE_LOCATION =  "";
		ConstantReport.ACC_TEMPL_FILE_LOCATION =  "";
		ConstantReport.CONST_TEMPL_FILE_LOCATION =  "";
		ConstantReport.CREEP_TEMPL_FILE_LOCATION =  "";
		ConstantReport.FREQ_TEMPL_FILE_LOCATION =  "";
		ConstantReport.HARM_TEMPL_FILE_LOCATION =  "";
		ConstantReport.REP_TEMP_FILE_LOCATION =  "";
		ConstantReport.RPS_TEMPL_FILE_LOCATION =  "";
		ConstantReport.SELF_HEAT_TEMPL_FILE_LOCATION =  "";
		ConstantReport.STA_TEMPL_FILE_LOCATION =  "";
		ConstantReport.UNBALANCED_LOAD_TEMPL_FILE_LOCATION =  "";
		ConstantReport.VU_TEMPL_FILE_LOCATION =  "";
		ConstantReport.VV_TEMPL_FILE_LOCATION =  "";
	}


	public static void FetchFileLocationByTestType(String test_type){
		JSONObject data = MySQL_Controller.sp_getreport_file_location(ConstantAppConfig.REPORT_PROFILE_LIST.get(0),test_type);
		try{
			if(!data.isNull("template_file_location")){
				String template_file = data.getString("template_file_location");
				String save_file_loc = data.getString("save_file_location");
				LoadReportFileLocationValues(test_type, template_file,save_file_loc);
			}	
		}
		catch(JSONException e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("FetchFileLOcationByTestType : JSONException: " + e.getMessage());
		}
	}

	public static void LoadReportFileLocationValues(String test_type,String template_file, 
			String save_file_loc){

		ConstantReport.SAVE_FILE_LOCATION =  save_file_loc;
		switch(test_type){
		//case "InfluenceFreq":
		case	ConstantApp.TEST_PROFILE_INFLUENCE_FREQ:
			ConstantReport.FREQ_TEMPL_FILE_LOCATION =  template_file;
			break;
			//case "InfluenceVolt":
		case ConstantApp.TEST_PROFILE_INFLUENCE_VOLT:
			ConstantReport.VV_TEMPL_FILE_LOCATION =  template_file;
			break;
			//case "Repeatability":
		case ConstantApp.TEST_PROFILE_REPEATABILITY:
			ConstantReport.REP_TEMP_FILE_LOCATION =  template_file;
			break;
			//case "SelfHeating":
		case ConstantApp.TEST_PROFILE_SELF_HEATING:
			ConstantReport.SELF_HEAT_TEMPL_FILE_LOCATION =  template_file;
			break;
			//case "Accuracy":
		case ConstantApp.TEST_PROFILE_ACCURACY:
			ConstantReport.ACC_TEMPL_FILE_LOCATION =  template_file;
			break;
			//case "PhaseReversal":
		case ConstantApp.TEST_PROFILE_PHASE_REVERSAL:
			ConstantReport.RPS_TEMPL_FILE_LOCATION =  template_file;
			break;
			//case "InfluenceHarmonic":
		case ConstantApp.TEST_PROFILE_INFLUENCE_HARMONIC:
			ConstantReport.HARM_TEMPL_FILE_LOCATION =  template_file;
			break;
			//case "VoltageUnbalance":
		case ConstantApp.TEST_PROFILE_VOLTAGE_UNBALANCE:
			ConstantReport.VU_TEMPL_FILE_LOCATION =  template_file;
			break;
			//case "ConstantTest":
		case ConstantApp.TEST_PROFILE_CONSTANT_TEST:
			ConstantReport.CONST_TEMPL_FILE_LOCATION =  template_file;
			break;
			//case "NoLoad":
		case ConstantApp.TEST_PROFILE_NOLOAD:
			ConstantReport.CREEP_TEMPL_FILE_LOCATION =  template_file;
			break;
			//case "STA":
		case ConstantApp.TEST_PROFILE_STA:
			ConstantReport.STA_TEMPL_FILE_LOCATION =  template_file;
			break;
			//case "UnbalancedLoad":
		case ConstantApp.TEST_PROFILE_UNBALANCED_LOAD:
			if(ProcalFeatureEnable.REPORT_3PHASE_UNBALANCED_LOAD){
				ConstantReport.UNBALANCED_LOAD_TEMPL_FILE_LOCATION =  template_file;
			}
			break;

		case ConstantApp.METER_PROFILE_REPORT:
			ConstantReport.METER_PROFILE_REPORT_TEMPL_FILE_LOCATION =  template_file;
			break;

		default:
			break;

		}
	}

	public boolean LoadSystemTime() throws Exception,NumberFormatException{
		String current_timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		//String current_timestamp = "2020-09-01 00:00:00";
		JSONObject system_config = MySQL_Controller.sp_getsystem_config();
		JSONArray json_arr = new JSONArray();
		String start_time_stamp = "";
		try {
			if(system_config.length()>0){
				json_arr = system_config.getJSONArray("Properties");
				JSONObject jobj = new JSONObject();
				for(int i=0; i<json_arr.length(); i++){
					jobj = json_arr.getJSONObject(i);
					String property = jobj.getString("property");
					if(property.equals(ConstantApp.SYSTEM_CONFIG_KEY)){
						start_time_stamp = jobj.getString("value");
						break;
					}
				}
			}else{
				return false;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("LoadSystemTime : JSONException: " + e.getMessage());
		}

		long epoch = 2 * 366 * 24 * 60 * 60 ;
		long current_value = calcEpoch(current_timestamp);
		long max_value = Long.parseLong(start_time_stamp) + epoch;

		if(current_value < max_value){
			return true;
		}
		else{
			return false;
		}

	}

	public long calcEpoch(String Date_time){
		long epoch = 0;
		//String str = "2014-07-04 04:05:10";   // UTC
		String str = Date_time;   // UTC

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date datenew = null;
		try {
			datenew = df.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("calcEpoch : ParseException: " + e.getMessage());
		}
		epoch = datenew.getTime() /1000;

		ApplicationLauncher.logger.info("ce: "+epoch);
		return epoch;
	}





	public static void deleteLogFilesOlderThanNdays2(int daysBack, String dirWay) {
		File directory = new File(dirWay);
		logger.info("deleteLogFilesOlderThanNdays: daysBack: " + daysBack);
		if(directory.exists()){

			File[] listFiles = directory.listFiles();           
			long purgeTime = System.currentTimeMillis() - (daysBack * 24 * 60 * 60 * 1000);

			logger.info("deleteLogFilesOlderThanNdays: purgeTime: " + purgeTime);
			for(File listFile : listFiles) {
				logger.info("deleteLogFilesOlderThanNdays: listFile: " + listFile);
				logger.info("deleteLogFilesOlderThanNdays: listFile.lastModified(): " + listFile.lastModified());
				if(listFile.lastModified() < purgeTime) {
					if(!listFile.delete()) {
						logger.info("deleteLogFilesOlderThanNdays: Unable to delete file: " + listFile);
					}
				}
			}
		}

	}


}
