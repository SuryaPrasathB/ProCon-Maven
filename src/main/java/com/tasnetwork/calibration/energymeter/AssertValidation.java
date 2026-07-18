package com.tasnetwork.calibration.energymeter;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.ft.S074_Set_HardwareId_On_Meters;
import com.tasnetwork.calibration.conveyor.bay.unloading.Unloading;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.dut_executors.DutCmdTestPointExecutor;
//import com.tasnetwork.calibration.conveyor.bay_functionaltest.S08_functional_Test;
//import com.tasnetwork.calibration.energymeter.calib.CalibPoints;
//import com.tasnetwork.calibration.energymeter.calib.CurrentCalibration;
//import com.tasnetwork.calibration.energymeter.calib.CurrentTap;
//import com.tasnetwork.calibration.energymeter.calib.VoltageCalibration;
//import com.tasnetwork.calibration.energymeter.calib.VoltageTap;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceMte;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdRadiant;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.constant.ConstantReportV2;
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.deployment.DutResponse;
import com.tasnetwork.calibration.energymeter.device.Communicator;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.director.DutCmdDirectorV3;
//import com.tasnetwork.calibration.energymeter.director.DutCmdTestPointExecutor;
import com.tasnetwork.calibration.energymeter.serial.portmanagerV2.DutCmdManager;
import com.tasnetwork.calibration.energymeter.serial.portmanagerV2.SerialPortManagerDutCmd_V3;
import com.tasnetwork.calibration.energymeter.testreport.ReportUtils;
import com.tasnetwork.calibration.energymeter.testreport.ReportUtils;
import com.tasnetwork.calibration.energymeter.testreport.TestReportController;
import com.tasnetwork.calibration.energymeter.testreport.TestReportConveyorController;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;
import com.tasnetwork.calibration.energymeter.util.IEEE754_Format;
import com.tasnetwork.calibration.energymeter.util.TMS_FloatConversion;
import com.tasnetwork.spring.orm.model.ConveyorOutputMetrics;
import com.tasnetwork.spring.orm.model.ConveyorOutputMetricsSummary;
import com.tasnetwork.spring.orm.model.PalletManage;

public class AssertValidation {

	static DeviceDataManagerController DisplayDataObj = new DeviceDataManagerController();

	/*
	 * public static void pulseTest(){
	 * 
	 * S08_functional_Test s08_functional_Test = new S08_functional_Test();
	 * s08_functional_Test.functionalTestProcess(1);
	 * }
	 */

	public static void assertGenerateNextNewHardwareIds() {

		S074_Set_HardwareId_On_Meters s074_Set_HardwareId_On_Meters = new S074_Set_HardwareId_On_Meters();
		s074_Set_HardwareId_On_Meters.generateNextNewHardwareIds();
	}

	public static void dutCommandTesting() {

		List<String> deviceIdList = new ArrayList<String>();
		deviceIdList.add("010101QR01");
		// deviceIdList.add("010101QR02");
		List<String> deviceIdList2 = new ArrayList<String>();
		deviceIdList2.add("010101QR01");
		// deviceIdList2.add("010101QR02");
		DutCmdTestPointExecutor commandExecutor = new DutCmdTestPointExecutor();
		DutCmdTestPointExecutor commandExecutor2 = new DutCmdTestPointExecutor();
		// commandExecutor.dutExecuteCommandTrigger();
		// commandExecutor.dutExecuteCommandWithDeviceIdListTrigger(deviceIdList);
		// commandExecutor2.dutExecuteCommandWithDeviceIdListTrigger(deviceIdList2);
		/*
		 * int dutPositionNo =1;
		 * //
		 * DutCmdManager dutCmdManager = new
		 * DutCmdManager();//DeviceDataManagerController.getDutCmdManagerV3();
		 * DutResponse dutResponse = new DutResponse();
		 * if(!dutCmdManager.isComSerialStatusConnected(dutPositionNo)) {
		 * String deviceId = "010101QR01";//"010204EM01";
		 * dutResponse =
		 * dutCmdManager.dutCmdSerialPortAccessible(deviceId,dutPositionNo);
		 * 
		 * }
		 * if(dutResponse.isStatus()) {
		 * SerialPortManagerDutCmd_V3 dutSpm = dutCmdManager.getDutSpm(dutPositionNo);
		 * DutCmdDirectorV3 dutCmdDirectorV3 = new DutCmdDirectorV3(dutSpm);
		 * dutResponse = dutCmdDirectorV3.dutMsngrSendCommandProcess();
		 * ApplicationLauncher.logger.debug("dutCommandTesting: getStatus: " +
		 * dutResponse.getStatus());
		 * 
		 * ApplicationLauncher.logger.debug("dutCommandTesting: getResponseData: " +
		 * dutResponse.getResponseData());
		 * String dutResponseDataInHex =
		 * GuiUtils.asciiToHex(dutResponse.getResponseData());
		 * ApplicationLauncher.logger.debug("dutCommandTesting: dutResponseDataInHex: "
		 * + dutResponseDataInHex);
		 * dutCmdManager.dutCmdDisconnectPort_V2(dutPositionNo);
		 * }else {
		 * ApplicationLauncher.logger.
		 * debug("dutCommandTesting: Access failed : getStatus: " +
		 * dutResponse.getStatus());
		 * 
		 * ApplicationLauncher.logger.
		 * debug("dutCommandTesting: Access failed : getResponseData: " +
		 * dutResponse.getResponseData());
		 * 
		 * }
		 */
	}

	public static void fetchPalletsByBayState() {

		// BayUtils bayUtils = new BayUtils();
		/*
		 * String bayKey = ConstantConveyor.VERIFICATION_BAY_KEY;
		 * List<PalletManage> palletManageList =
		 * bayUtils.fetchPalletsByBayState(bayKey);
		 * for(PalletManage eachPalletManage : palletManageList ) {
		 * ApplicationLauncher.logger.
		 * debug("fetchPalletsByBayState: getPalletDistinctId:    " +
		 * eachPalletManage.getPalletDistinctId());
		 * }
		 */

		// String palletDistinctId =
		// "20250720T160639_0079_LSCSM06P023";//"20250719T210233_0122_LSCSM06P020";
		// bayUtils.computeMeterOverAllStatus(palletDistinctId);
		// bayUtils.clearExistingResultInDb("VERIFICB1", palletDistinctId);

		/*
		 * String palletQrData = "LSCSM06P012";
		 * boolean exitAppeared = false;
		 * 
		 * String palletDistinctId = "20250724T204612_0148_LSCSM06P012";
		 * palletDistinctId = "20250727T134739_0022_LSCSM06P021";
		 * palletDistinctId = "20250726T124001_0028_LSCSM06P003";
		 * ReportUtilsV2 reportUtils = new ReportUtilsV2();
		 */
		// ReportUtils reportUtils = new ReportUtils();
		// reportUtils.processPalletMeterIndividualResult(palletDistinctId);
		// Optional<PalletManage> myPalletManageOptional =
		// MySqlServiceManager.getPalletManageService().findTopByTodayDateAndPresentBayKeyAndPalletQrIdAndExitAppeared(
		// ConstantConveyor.UNLOADING_BAY_KEY, palletQrData,exitAppeared);

		/*
		 * String inpDistinctId = "20250721T213454_0130_LSCSM06P023";
		 * Optional<PalletManage> myPalletManageOptional =
		 * MySqlServiceManager.getPalletManageService().findByPalletDistinctId(
		 * inpDistinctId);
		 * 
		 * if(myPalletManageOptional.isPresent()){
		 * TestReportConveyorController testReportConveyorController = new
		 * TestReportConveyorController();
		 * try {
		 * testReportConveyorController.exportPalletMeterResult(myPalletManageOptional.
		 * get());
		 * } catch (IOException e) {
		 * 
		 * e.printStackTrace();
		 * }
		 * }
		 */
		/*
		 * if(myPalletManageOptional.isPresent()){
		 * PalletManage myPalletManage = myPalletManageOptional.get();
		 * Unloading.logger.
		 * info("S05_qR_Code_Scanning_of_Pallet : found result myPalletManage : " +
		 * myPalletManage.getPalletDistinctId());
		 * //BayUtils bayUtils = new BayUtils();
		 * bayUtils.computeMeterOverAllStatus(myPalletManage.getPalletDistinctId());
		 * //List<Map<String, Object>> metersData =
		 * readPalletMetersData(getMyBayKey(),qrData);
		 * 
		 * if (metersData != null && !metersData.isEmpty()) {
		 * //batchUpdateUnloadingMeters(qrData,metersData); // This will now update
		 * ConveyorOutputMetrics daily with average hourly output
		 * } else {
		 * Unloading.logger.
		 * debug("S05_qR_Code_Scanning_of_Pallet: No meter data found for batch update for bay: "
		 * );//+ getMyBayKey());
		 * }
		 * }else {
		 * Unloading.logger.
		 * info("S05_qR_Code_Scanning_of_Pallet : result not found for Pallet : " +
		 * palletQrData);
		 * }
		 */

		// ConveyorOutputMetricsSummary metrics = new ConveyorOutputMetricsSummary();
		// metrics.setCustomerName("Test1-Gopi");;
		// MySqlServiceManager.getConveyorOutputMetricsSummaryService().saveToDb(metrics);
	}

	public static void bofaCommandPrint() {

		// String voltResolution = "%03.01f";
		// String currentResolution = "%02.02f";

		DecimalFormat voltResolution = new DecimalFormat("000.0");
		DecimalFormat currentResolution = new DecimalFormat("00.00");

		float voltage = 20.67f;
		float current = 5.452f;

		String formattedVolt = voltResolution.format(voltage);
		String formattedCurrent = currentResolution.format(current);
		ApplicationLauncher.logger.debug("bofaCommandPrint: formattedVolt1:    " + formattedVolt);
		ApplicationLauncher.logger.debug("bofaCommandPrint: formattedCurrent1: " + formattedCurrent);

		voltage = 20f;
		current = 5f;

		formattedVolt = voltResolution.format(voltage);
		formattedCurrent = currentResolution.format(current);
		ApplicationLauncher.logger.debug("bofaCommandPrint: formattedVolt2:    " + formattedVolt);
		ApplicationLauncher.logger.debug("bofaCommandPrint: formattedCurrent2: " + formattedCurrent);

		voltage = 120.6f;
		current = 0.052f;

		formattedVolt = voltResolution.format(voltage);
		formattedCurrent = currentResolution.format(current);
		ApplicationLauncher.logger.debug("bofaCommandPrint: formattedVolt3:    " + formattedVolt);
		ApplicationLauncher.logger.debug("bofaCommandPrint: formattedCurrent3: " + formattedCurrent);

		voltage = 0f;
		current = 0;

		formattedVolt = voltResolution.format(voltage);
		formattedCurrent = currentResolution.format(current);
		ApplicationLauncher.logger.debug("bofaCommandPrint: formattedVolt1:    " + formattedVolt);
		ApplicationLauncher.logger.debug("bofaCommandPrint: formattedCurrent1: " + formattedCurrent);

		voltage = 300f;
		current = 100f;

		formattedVolt = voltResolution.format(voltage);
		formattedCurrent = currentResolution.format(current);
		ApplicationLauncher.logger.debug("bofaCommandPrint: formattedVolt1:    " + formattedVolt);
		ApplicationLauncher.logger.debug("bofaCommandPrint: formattedCurrent1: " + formattedCurrent);

		DeviceDataManagerController.setR_PhaseOutputVoltage(240.0f);
		DeviceDataManagerController.setR_PhaseOutputCurrent(240.0f);
		DeviceDataManagerController.set_PwrSrcR_PhaseDegreePhase("0.0");
		DeviceDataManagerController.set_PwrSrc_Freq("50.0");
		// BofaManager.testBofaCommands();
	}

	/*
	 * public static String hexToFloat(String hexString) {
	 * //ApplicationLauncher.logger.debug("hexToFloat : input Hex String : "+
	 * hexString);
	 * Long i = 0L;
	 * Float f = 0.0f;
	 * String Data ="";
	 * try{
	 * 
	 * //i = Integer.parseInt(hexString, 16);
	 * // f = Float.intBitsToFloat(i);
	 * i = Long.parseLong(hexString, 16);
	 * f = Float.intBitsToFloat(i.intValue());
	 * // ApplicationLauncher.logger.debug("hexToFloat : converted float value : "+
	 * f);
	 * 
	 * try{
	 * Data =String.format("%.4f",f);
	 * if(Data.equals("-0.0000")){
	 * Data = "0.0000";
	 * }
	 * } catch (Exception e){
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("hextofloat: Exception2:" +e.getMessage());
	 * 
	 * }
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("hexToFloat: Exception: "+e.getMessage());
	 * 
	 * 
	 * }
	 * 
	 * return Data;
	 * }
	 */

	// 1676381566

	public static void assertLicenceVerification() {
		ApplicationLauncher.logger.debug("assertLicenceVerification: Entry");

		/*
		 * Data_LduBofa.resetNthOfErrors();
		 * Data_LduBofa.resetErrorValue();
		 * Data_LduBofa.resetDialTestPulseCount();
		 * Data_LduBofa.resetStaCreepTestPulseCount();
		 * 
		 * String response = "0149323217";
		 * int address = 8;
		 * Data_LduBofa.parseStartingCurrentTestPulseResponse(response, address);
		 * 
		 * 
		 * response = "014A323217";
		 * address = 9;
		 * Data_LduBofa.parseStartingCurrentTestPulseResponse(response, address);
		 * 
		 * response = "014B323217";
		 * address = 10;
		 * Data_LduBofa.parseStartingCurrentTestPulseResponse(response, address);
		 */

		/*
		 * String response = "0148332B30303035323032B717";
		 * int address = 7;
		 * Data_LduBofa.parseErrorsResponse( response, address);
		 * 
		 * response = "0149322B30303038303036BB17";
		 * address = 8;
		 * Data_LduBofa.parseErrorsResponse( response, address);
		 * 
		 * response = "0150312B30303035363033BA17";
		 * address = 15;
		 * Data_LduBofa.parseErrorsResponse( response, address);
		 * 
		 * 
		 * 
		 * 
		 * response = "014A332B30303036363034BE17";
		 * address = 9;
		 * Data_LduBofa.parseErrorsResponse( response, address);
		 * 
		 * response = "014E312D30303035393936CB17";
		 * address = 13;
		 * Data_LduBofa.parseErrorsResponse( response, address);
		 * 
		 * response = "014F312B30303130363131B517";
		 * address = 14;
		 * Data_LduBofa.parseErrorsResponse( response, address);
		 * 
		 * response = "0150312B30303035363033BA17";
		 * address = 15;
		 * Data_LduBofa.parseErrorsResponse( response, address);
		 */

		/*
		 * String current_i1 = "0.076";
		 * float outputTargetImaxCurrent = Float.parseFloat(current_i1);;//0.0f;
		 * 
		 * int setScaleCurrentAfterDecimal = 3;
		 * BigDecimal bigValue = new BigDecimal(outputTargetImaxCurrent);
		 * bigValue = bigValue.setScale(setScaleCurrentAfterDecimal,
		 * RoundingMode.CEILING);
		 * outputTargetImaxCurrent= bigValue.floatValue();
		 * 
		 * double outputTargetImaxCurrent = Double.parseDouble(current_i1);;//0.0f;
		 * 
		 * int setScaleCurrentAfterDecimal = 3;
		 * BigDecimal bigValue = new BigDecimal(outputTargetImaxCurrent);
		 * bigValue = bigValue.setScale(setScaleCurrentAfterDecimal,
		 * RoundingMode.CEILING);
		 * outputTargetImaxCurrent = bigValue.doubleValue();
		 * 
		 * ApplicationLauncher.logger.
		 * debug("assertLicenceVerification: outputTargetImaxCurrent: " +
		 * outputTargetImaxCurrent);
		 * 
		 * 
		 * current_i1 = "0.074";
		 * outputTargetImaxCurrent = Float.parseFloat(current_i1);;//0.0f;
		 * 
		 * setScaleCurrentAfterDecimal = 3;
		 * bigValue = new BigDecimal(outputTargetImaxCurrent);
		 * bigValue = bigValue.setScale(setScaleCurrentAfterDecimal,
		 * RoundingMode.FLOOR);
		 * outputTargetImaxCurrent= bigValue.floatValue();
		 * 
		 * ApplicationLauncher.logger.
		 * debug("assertLicenceVerification: outputTargetImaxCurrent2: " +
		 * outputTargetImaxCurrent);
		 * 
		 * current_i1 = "0.075";
		 * outputTargetImaxCurrent = Float.parseFloat(current_i1);;//0.0f;
		 * 
		 * setScaleCurrentAfterDecimal = 3;
		 * bigValue = new BigDecimal(outputTargetImaxCurrent);
		 * bigValue = bigValue.setScale(setScaleCurrentAfterDecimal,
		 * RoundingMode.FLOOR);
		 * outputTargetImaxCurrent= bigValue.floatValue();
		 * 
		 * ApplicationLauncher.logger.
		 * debug("assertLicenceVerification: outputTargetImaxCurrent3: " +
		 * outputTargetImaxCurrent);
		 * 
		 * current_i1 = "0.079";
		 * outputTargetImaxCurrent = Float.parseFloat(current_i1);;//0.0f;
		 * 
		 * setScaleCurrentAfterDecimal = 3;
		 * bigValue = new BigDecimal(outputTargetImaxCurrent);
		 * bigValue = bigValue.setScale(setScaleCurrentAfterDecimal,
		 * RoundingMode.FLOOR);
		 * outputTargetImaxCurrent= bigValue.floatValue();
		 * 
		 * ApplicationLauncher.logger.
		 * debug("assertLicenceVerification: outputTargetImaxCurrent4: " +
		 * outputTargetImaxCurrent);
		 * 
		 * 
		 * current_i1 = "0.08";
		 * outputTargetImaxCurrent = Float.parseFloat(current_i1);;//0.0f;
		 * ApplicationLauncher.logger.
		 * debug("assertLicenceVerification: outputTargetImaxCurrent5: " +
		 * outputTargetImaxCurrent);
		 */

		long epochTimeInSec = 1677133782;// 1676381566;
		String userInputSaltedKey = "-- -- ";
		isLicenceVerificationMatching(epochTimeInSec, userInputSaltedKey);
		// userInputSaltedKey = "620460-67";
		userInputSaltedKey = "1";// "523466";//"620429";
		isLicenceVerificationMatching(epochTimeInSec, userInputSaltedKey);

		String testCasePreview = "REP_XX-100U-1.0-1.0Ib-1-2";// "REP_XX-100U-1.0-1.0Ib-2";
		getTestPointFilterData(testCasePreview);
	}

	public static String getTestPointFilterData(String testCasePreview) {

		ApplicationLauncher.logger.debug("getTestPointFilterData: testCasePreview: " + testCasePreview);

		try {
			// String testCasePreview = "STA_XX-100U-1.0-0.004Ib";
			// testCasePreview = "NLD_XX-115U";
			// if(testCasePreview.startsWith(ConstantApp.TEST_PROFILE_STA)){
			// testCasePreview = testCasePreview.replace("U-1.0-", "U-");
			// }
			String[] splittedData = testCasePreview.split(ConstantReportV2.TEST_ID_MASK);

			ApplicationLauncher.logger.debug("getTestPointFilterData: splittedData[0]:" + splittedData[0]);
			ApplicationLauncher.logger.debug("getTestPointFilterData: splittedData[1]:" + splittedData[1]);
			String testCaseFilterName = splittedData[1].substring(0,
					splittedData[1].lastIndexOf(ConstantReportV2.TEST_POINT_FILTER_SEPERATOR));
			testCaseFilterName = testCaseFilterName.substring(0,
					testCaseFilterName.lastIndexOf(ConstantReportV2.TEST_POINT_FILTER_SEPERATOR));
			testCaseFilterName = testCaseFilterName.replace(ConstantReport.EXTENSION_TYPE_CURRENT_IB, "I")
					.replace(ConstantReport.EXTENSION_TYPE_CURRENT_IMAX, "I");
			ApplicationLauncher.logger.debug("getTestPointFilterData: testCaseFilterName:" + testCaseFilterName);
			return testCaseFilterName;

		} catch (Exception e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("getTestPointFilterData : Exception:" + e.getMessage());
		}

		return "";
	}

	public static Map<Boolean, String> isLicenceVerificationMatching(long epochTimeInSec, String userInputSaltedKey) {

		ApplicationLauncher.logger.debug("isLicenceVerificationMatching: Entry");
		ApplicationLauncher.logger.debug("isLicenceVerificationMatching: epochTimeInSec: " + epochTimeInSec);
		ApplicationLauncher.logger.debug("isLicenceVerificationMatching: userInputSaltedKey : " + userInputSaltedKey);

		/*
		 * int year =
		 * Instant.ofEpochSecond(epochTimeInSec).atZone(ZoneId.systemDefault()).getYear(
		 * );
		 * int month =
		 * Instant.ofEpochSecond(epochTimeInSec).atZone(ZoneId.systemDefault()).getYear(
		 * );
		 * //int date =
		 * Instant.ofEpochSecond(epochTimeInSec).atZone(ZoneId.systemDefault()).getYear(
		 * );
		 * 
		 * int hour =
		 * Instant.ofEpochSecond(epochTimeInSec).atZone(ZoneId.systemDefault()).getHour(
		 * );
		 * int minutes =
		 * Instant.ofEpochSecond(epochTimeInSec).atZone(ZoneId.systemDefault()).
		 * getMinute();
		 * int seconds =
		 * Instant.ofEpochSecond(epochTimeInSec).atZone(ZoneId.systemDefault()).
		 * getSecond();
		 * 
		 * ApplicationLauncher.logger.debug("isLicenceVerificationMatching: Time : " +
		 * year );
		 */

		userInputSaltedKey = userInputSaltedKey.replaceAll("[^0-9]", "");
		ApplicationLauncher.logger
				.debug("isLicenceVerificationMatching: userInputSaltedKey with only numbers: " + userInputSaltedKey);

		Map<Boolean, String> verificationStatusHashMap = new HashMap<Boolean, String>();
		if (userInputSaltedKey.equals(ConstantApp.USER_LICENCE_SKIP_SALTED_KEY_VALIDATION_DEFAULT_VALUE)) {
			ApplicationLauncher.logger.debug("isLicenceVerificationMatching: salted default key matching");
			verificationStatusHashMap.put(true, "");
			return verificationStatusHashMap;
			// return true;
		}
		Date date = new Date(epochTimeInSec * 1000);// 1500464550423L);
		// use correct format ('S' for milliseconds)
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyMMdd");
		dateFormatter.setTimeZone(TimeZone.getTimeZone("IST"));
		// format date
		String dateFormattedData = dateFormatter.format(date);

		SimpleDateFormat timeFormatter = new SimpleDateFormat("HHmmss");
		timeFormatter.setTimeZone(TimeZone.getTimeZone("IST"));
		// format date
		String timeFormattedData = timeFormatter.format(date);

		ApplicationLauncher.logger.debug("isLicenceVerificationMatching: dateFormattedData : " + dateFormattedData);
		ApplicationLauncher.logger.debug("isLicenceVerificationMatching: timeFormattedData : " + timeFormattedData);

		// Salt key generation Algorithm
		// Step01: inputEpochTime -> A => 1676381566
		// Step02: Take first two characters of Year and Multiply with 10,000 -> B => 20
		// * 10000 = 200000
		// Step03: Decode input epochTime in to yyMMdd in IST (yy is last two char of
		// year) -> C => 230214
		// Step04: Decode input epochTime in to HHmmss in IST -> D => 190246
		// Step05: Sum B + C + D -> X (sumOfEpochFormattedData) => Total = 620460
		// Step06: Sum each Digit Of X -> E (sumOfEachDigitOfEpochFormattedData) =>
		// SumEachDigit(620460) = 18
		// Step07: Sum each Digit Of A -> F (sumOfEachDigitInputEpochTimeInSec) =>
		// SumEachDigit(1676381566) = 49
		// Step08: Sum E - F -> Y
		// (diffOfEachDigitOfEpochFormattedAndInputEpochTimeInSec) => 18 - 49 = -31
		// Step09: Sum X + Y -> Z (sumOfEpochFormattedData +
		// diffOfEachDigitOfEpochFormattedAndInputEpochTimeInSec) => 620460 + (-31) =
		// 620429
		// Step10: Z = SaltedKeyCalculatedResult (expectedSaltedValue)

		long firstTwoYearValue = 20 * 10000; // 2,00,000

		long sumOfEpochFormattedData = firstTwoYearValue + Long.parseLong(dateFormattedData)
				+ Long.parseLong(timeFormattedData);

		ApplicationLauncher.logger
				.debug("isLicenceVerificationMatching: sumOfEpochFormattedData : " + sumOfEpochFormattedData);

		int sumOfEachDigitOfEpochFormattedData = Arrays.stream(String.valueOf(sumOfEpochFormattedData).split(""))
				.mapToInt(e -> Integer.valueOf(e))
				.sum();
		ApplicationLauncher.logger.debug("isLicenceVerificationMatching: sumOfEachDigitOfEpochFormattedData : "
				+ sumOfEachDigitOfEpochFormattedData);

		/*
		 * int sumOfEachDigitInputEpochTimeInSec =
		 * Arrays.stream(String.valueOf(epochTimeInSec).split(""))
		 * .mapToInt( e -> Integer.valueOf(e))
		 * .sum();
		 * ApplicationLauncher.logger.
		 * debug("isLicenceVerificationMatching: sumOfEachDigitInputEpochTimeInSec : " +
		 * sumOfEachDigitInputEpochTimeInSec );
		 * 
		 */

		int sumOfEachDigitInputEpochTimeInSec = Stream.of(String.valueOf(epochTimeInSec).split(""))
				.mapToInt(e -> Integer.valueOf(e))
				.sum();
		ApplicationLauncher.logger.debug("isLicenceVerificationMatching: sumOfEachDigitInputEpochTimeInSec : "
				+ sumOfEachDigitInputEpochTimeInSec);

		int diffOfEachDigitOfEpochFormattedAndInputEpochTimeInSec = sumOfEachDigitOfEpochFormattedData
				- sumOfEachDigitInputEpochTimeInSec;

		ApplicationLauncher.logger
				.debug("isLicenceVerificationMatching: diffOfEachDigitOfEpochFormattedAndInputEpochTimeInSec : "
						+ diffOfEachDigitOfEpochFormattedAndInputEpochTimeInSec);

		// String expectedSaltedValue = String.valueOf(sumOfEpochFormattedData) + "-"
		// +String.valueOf(diffOfEachDigitOfEpochFormattedAndInputEpochTimeInSec);
		long expectedSaltedValue = sumOfEpochFormattedData + diffOfEachDigitOfEpochFormattedAndInputEpochTimeInSec;
		ApplicationLauncher.logger.debug("isLicenceVerificationMatching: expectedSaltedValue : " + expectedSaltedValue);
		ApplicationLauncher.logger.debug("isLicenceVerificationMatching: userInputSaltedKey : " + userInputSaltedKey);
		if (userInputSaltedKey.equals(String.valueOf(expectedSaltedValue))) {
			ApplicationLauncher.logger.debug("isLicenceVerificationMatching: salted key matching");
			verificationStatusHashMap.put(true, "");
			return verificationStatusHashMap;
			// return true;
		} else {
			ApplicationLauncher.logger.debug("isLicenceVerificationMatching: salted key not matching");
			// return false;
			// int randomValue1 = ThreadLocalRandom.current().nextInt(5000, 8000);
			int randomValue2 = ThreadLocalRandom.current().nextInt(9000, 10000);
			String errorCode = "0x" + Long.toHexString(expectedSaltedValue).toUpperCase() + "BADC"
					+ Integer.toHexString(randomValue2).toUpperCase();
			ApplicationLauncher.logger.debug("isLicenceVerificationMatching: errorCode: " + errorCode);
			verificationStatusHashMap.put(false, errorCode);
			return verificationStatusHashMap;

		}

	}

	public static String assertPrintTestTimePeriod() {

		/*
		 * String dataInFloat = hexToFloat("002D7FFE");
		 * //dataInFloat = hexToFloat("2D00FE7F");
		 * //dataInFloat = hexToFloat("7FFE002D");
		 * //dataInFloat = hexToFloat("002D");
		 * ApplicationLauncher.logger.debug("assertPrintTestTimePeriod: dataInFloat: " +
		 * dataInFloat);
		 * 
		 * ApplicationLauncher.logger.debug("assertPrintTestTimePeriod: Hex: " +
		 * (Integer.toHexString(Float.floatToIntBits(Float.parseFloat(dataInFloat)))));
		 * Float f = new Float("50.32");
		 * String strInHex = f.toHexString(45.0f);
		 * ApplicationLauncher.logger.debug("assertPrintTestTimePeriod: strInHex: " +
		 * strInHex);
		 */
		ApplicationLauncher.logger.debug("assertPrintTestTimePeriod: Entry");

		IndexedColors indexedColorIndex = IndexedColors.valueOf("RED1");// IndexedColors.RED.getIndex();

		ApplicationLauncher.logger.debug("assertPrintTestTimePeriod: indexedColorIndex: " + indexedColorIndex);

		String keyParam = "ryb_actual_voltage";// "test_period_in_sec";

		String Projectname = "LECSDEC22"; // TestReportController.getSelectedProjectName();
		String deploymentID = "330";// TestReportController.getSelectedDeploymentID();
		float value = 0.0f;
		JSONArray testcaselist = null;
		// try {
		// testcaselist =
		// ProjectExecutionController.getListOfTestPoints(Projectname,deploymentID);
		// setDeployedTestCaseDetailsList(testcaselist);
		// ArrayList<ArrayList<Object>> i_rowValues = initRowValues(testcaselist,
		// columnNames);
		String testCasePreview = "STA_XX-100U-1.0-0.004Ib";
		// testCasePreview = "NLD_XX-115U";
		testCasePreview = "LOE_XX-100U-1.0-1.0Ib";
		testCasePreview = "VU_XX-RY:100U-1.0-1.0Ib";
		testCasePreview = "VU_XX-Y:100U-1.0-1.0Ib";
		if (testCasePreview.startsWith(ConstantApp.TEST_PROFILE_STA)) {
			testCasePreview = testCasePreview.replace("U-1.0-", "U-");
		}
		String[] splittedData = testCasePreview.split(ConstantReportV2.TEST_ID_MASK);

		ApplicationLauncher.logger.debug("assertPrintTestTimePeriod: splittedData[0]:" + splittedData[0]);
		ApplicationLauncher.logger.debug("assertPrintTestTimePeriod: splittedData[1]:" + splittedData[1]);

		String[] paramSplittedData = testCasePreview.split("-");
		String voltageData = paramSplittedData[1];
		ApplicationLauncher.logger.debug("assertPrintTestTimePeriod: voltageDatasplittedData[1]:" + voltageData);

		if (voltageData.contains(ConstantApp.FIRST_PHASE_DISPLAY_NAME)) {
			keyParam = "r_actual_voltage";
		} else if (voltageData.contains(ConstantApp.SECOND_PHASE_DISPLAY_NAME)) {
			keyParam = "y_actual_voltage";
		} else if (voltageData.contains(ConstantApp.THIRD_PHASE_DISPLAY_NAME)) {
			keyParam = "b_actual_voltage";
		} else {
			keyParam = "ryb_actual_voltage";
		}
		ApplicationLauncher.logger.debug("assertPrintTestTimePeriod: keyParam2:" + keyParam);
		JSONObject jobj = new JSONObject();
		boolean testCaseIdentified = false;
		/*
		 * for(int i=0; i<testcaselist.length(); i++){
		 * //ArrayList<Object> EachRowValues = new ArrayList<Object>();
		 * jobj = testcaselist.getJSONObject(i);
		 * String readTestCaseName = jobj.getString("test_case");
		 * 
		 * ApplicationLauncher.logger.
		 * debug("assertPrintTestTimePeriod: readTestCaseName: " + readTestCaseName);
		 * if(readTestCaseName.startsWith(splittedData[0])){
		 * if(readTestCaseName.endsWith(splittedData[1])){
		 * ApplicationLauncher.logger.
		 * debug("assertPrintTestTimePeriod: readTestCaseName matching ");
		 * testCaseIdentified = true;
		 * String readData = jobj.getString(keyParam);
		 * if(!readData.isEmpty()){
		 * value = Float.parseFloat(readData);
		 * if(keyParam.equals("test_period_in_sec")){
		 * value = value/60;
		 * ApplicationLauncher.logger.
		 * debug("assertPrintTestTimePeriod: test_period_in_sec: value: " +
		 * String.valueOf(value));
		 * return String.valueOf(value);
		 * }else if(keyParam.equals("warmup_period_in_sec")){
		 * value = value/60;
		 * ApplicationLauncher.logger.
		 * debug("assertPrintTestTimePeriod: warmup_period_in_sec: value: " +
		 * String.valueOf(value));
		 * return String.valueOf(value);
		 * }
		 * ApplicationLauncher.logger.debug("assertPrintTestTimePeriod: value: " +
		 * String.valueOf(value));
		 * return String.valueOf(value);
		 * }
		 * }
		 * 
		 * }
		 * }
		 */
		// if(testCaseIdentified){

		// }

		/*
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.error("loadTestCaseDetails : JSONException:"+e.
		 * getMessage());
		 * }
		 */

		return "";

	}

	public static void assertconvertTo2sComplement() {

		int inputValue = 2048;

		int outputValue = convertTo2sComplement(inputValue);

		inputValue = 2047;

		outputValue = convertTo2sComplement(inputValue);

		inputValue = 1;
		outputValue = convertTo2sComplement(inputValue);
		inputValue = 0;
		outputValue = convertTo2sComplement(inputValue);

		inputValue = -1;
		outputValue = convertTo2sComplement(inputValue);

		inputValue = -2;
		outputValue = convertTo2sComplement(inputValue);

		inputValue = -1028;
		outputValue = convertTo2sComplement(inputValue);

		inputValue = -2046;
		outputValue = convertTo2sComplement(inputValue);

		inputValue = -2047;
		outputValue = convertTo2sComplement(inputValue);

		inputValue = -2048;
		outputValue = convertTo2sComplement(inputValue);

	}

	static int convertTo2sComplement(int sin_value) {
		System.out.println("AssertValidation: convertTo2sComplement  input : sin_value :" + sin_value);
		// System.out.println("AssertValidation: convertTo2sComplement input : sin_value
		// Binary :" + Integer.toBinaryString(sin_value));
		// System.out.println("AssertValidation: convertTo2sComplement input : sin_value
		// Hex :" + String.format("%04X", sin_value));
		// sin_value = sin_value & 0xFFF;
		// System.out.println("AssertValidation: convertTo2sComplement input :
		// sin_value0 :" + sin_value);
		if (sin_value < 0) {
			sin_value = ~(sin_value); // Finding 2s Complement - Step 1
			// sin_value = sin_value - 0x0FFF;
			System.out.println("AssertValidation: convertTo2sComplement  : sin_value1 :" + sin_value);
			System.out.println("AssertValidation: convertTo2sComplement  : sin_value1 Binary :"
					+ Integer.toBinaryString(sin_value));
			// sin_value = sin_value & 0xFFF;
			// System.out.println("AssertValidation: convertTo2sComplement : sin_value1 Hex
			// :" + Integer.toHexString(sin_value));
			sin_value = sin_value + 0x0001; // Finding 2s Complement - Step 2
			// sin_value = ~(sin_value);
			// System.out.println("AssertValidation: convertTo2sComplement : sin_value2 :" +
			// sin_value);
			// System.out.println("AssertValidation: convertTo2sComplement : sin_value2 Hex
			// :" + Integer.toHexString(sin_value));
			int tempValue = sin_value & 0x0800;
			System.out.println("AssertValidation: convertTo2sComplement  : tempValue :" + tempValue);
			// System.out.println("AssertValidation: convertTo2sComplement : sin_value3 :" +
			// sin_value);
			// System.out.println("AssertValidation: convertTo2sComplement : sin_value3 Hex
			// :" + Integer.toHexString(sin_value));
			if (tempValue == 0x0800) { // 1111 1111 1111 1111 & 0000 1000 0000 0000 = 0000 1000 0000 0000 //
				sin_value = sin_value & (0xF7FF); // making 12th bit 0 ; 1111 0111 1111 1111 ; to get negative value
				System.out.println("AssertValidation: convertTo2sComplement  : sin_value4 :" + sin_value);
				// System.out.println("AssertValidation: convertTo2sComplement : sin_value4 Hex
				// :" + Integer.toHexString(sin_value));
			} else {
				sin_value = sin_value | (0x0800); // making 12th bit 1 ; 0000 1000 0000 0000 ; to get positive value
				System.out.println("AssertValidation: convertTo2sComplement  : sin_value5 :" + sin_value);
				// System.out.println("AssertValidation: convertTo2sComplement : sin_value5 Hex
				// :" + Integer.toHexString(sin_value));
			}
		} else {
			sin_value = sin_value & 0x0FFF;
		}
		// System.out.println("AssertValidation: convertTo2sComplement output :
		// sin_value :" + sin_value);
		// System.out.println("AssertValidation: convertTo2sComplement output :
		// sin_value Hex :" + String.format("%04X", sin_value));
		// sin_value = sin_value & 0x0FFF;// added new
		// System.out.println("AssertValidation: convertTo2sComplement output :
		// sin_value2 :" + sin_value);
		System.out.println(
				"AssertValidation: convertTo2sComplement output : sin_value Hex2 :" + String.format("%04X", sin_value));
		return sin_value;
	}

	public static void assertSpringJpa() {

		System.out.println("AssertValidation: assertSpringJpa: Entry ");
		// ClassPathXmlApplicationContext ctx = new
		// ClassPathXmlApplicationContext("classpath:resources/spring.xml");
		// ClassPathXmlApplicationContext ctx = ConstantVersion.springAppContext;

		// Get service from context.
		/*
		 * ProductService productService = ctx.getBean(ProductService.class);
		 * 
		 * 
		 * //Add some items
		 * productService.add(new Product(1, "Television1"));
		 * productService.add(new Product(2, "Phone1"));
		 * productService.addAll(Arrays.asList(
		 * new Product(3, "Peach"),
		 * new Product(4, "Strawberry"),
		 * new Product(5, "Melone"),
		 * new Product(6, "Onion")
		 * ));
		 * 
		 * productService.add(new Product("Laddu"));
		 * //Test entity listing
		 * System.out.println("findAll=" + productService.findAll());
		 * 
		 * //Test specified find methods
		 * System.out.println("findByName is 'Peach': " +
		 * productService.findByNameIs("Peach"));
		 * System.out.println("findByNameContainingIgnoreCase 'on': " +
		 * productService.findByNameContainingIgnoreCase("on"));
		 */

		/*
		 * TutionService tutionService = ctx.getBean(TutionService.class);
		 * List<Tution> tutionDataList = tutionService.findAll();
		 * System.out.println("Home : tutionDataList: getUserComment: " +
		 * tutionDataList.get(0).getStudentName());
		 * 
		 * //tutionRepository = ctx.getBean(TutionService.class);
		 * //List<Tution> tutionDataList = tutionRepository.findAll();
		 * //List<Tution> tutionDataList = tutionRepository.findAll();
		 * //tutionService.add(new Tution("Jithesh",600));
		 * tutionDataList = tutionService.getAllCustomData();
		 * //System.out.println("Home : tutionDataList: getUserComment2: " +
		 * tutionDataList.get(0).getStudentName());
		 * tutionDataList.stream().forEach(e->ApplicationLauncher.logger.
		 * debug("Home : tutionDataList: getUserComment2: " + e.getStudentName()));
		 */
		// List<Tution> tutionDataList = tutionRepository.getAllCustomData();

		// ctx.close();

		System.out.println("AssertValidation: assertSpringJpa: Entry ");
	}

	public static void assertSlaveSendIntDataToPC() {

		int i = 12345;
		slaveSendIntDataToPC(i);
		double doubleValue = 123456.78923;
		slaveSendDoubleDataToPC(doubleValue);
		doubleValue = 1234.5678923;
		slaveSendDoubleDataToPCV2(doubleValue);

		float floatValue = 123456.1234f;
		slaveSendFloatDataToPC(floatValue);
	}

	public static void slaveSendFloatDataToPC(float floatValue) {
		int b = 0, c = 0, d = 0, e = 0, f = 0, g = 0, h = 0, m = 0, n = 0;
		int c_1 = 0, d_1 = 0, e_1 = 0, f_1 = 0, g_1 = 0, h_1 = 0, m_1 = 0, n_1 = 0, p_1 = 0;

		int l_Main_Master_FB_Data = (int) (floatValue * 10000);

		// l_Main_Master_FB_Data = (int)(doubleValue * 100000);

		ApplicationLauncher.logger.debug("slaveSendFloatDataToPC: l_Main_Master_FB_Data: " + l_Main_Master_FB_Data);

		// a=56;
		b = l_Main_Master_FB_Data / 1000000000;
		c = l_Main_Master_FB_Data / 100000000;
		d = l_Main_Master_FB_Data / 10000000;
		e = l_Main_Master_FB_Data / 1000000;
		f = l_Main_Master_FB_Data / 100000;
		g = l_Main_Master_FB_Data / 10000;
		h = l_Main_Master_FB_Data / 1000;
		m = l_Main_Master_FB_Data / 100;
		n = l_Main_Master_FB_Data / 10;

		c_1 = c - (b * 10);
		d_1 = d - (c * 10);
		e_1 = e - (d * 10);
		f_1 = f - (e * 10);
		g_1 = g - (f * 10);
		h_1 = h - (g * 10);
		m_1 = m - (h * 10);
		n_1 = n - (m * 10);
		// e_1=e-(n*10);dxcv
		p_1 = l_Main_Master_FB_Data - (n * 10);

		ApplicationLauncher.logger.debug("slaveSendFloatDataToPC: Int: " + (b));
		// Serial_Slave_Send_Data2_System_Func (b+48);
		ApplicationLauncher.logger.debug("slaveSendFloatDataToPC: Int: " + (c_1));
		// Serial_Slave_Send_Data2_System_Func (c_1+48);
		ApplicationLauncher.logger.debug("slaveSendFloatDataToPC: Int: " + (d_1));
		// Serial_Slave_Send_Data2_System_Func (d_1+48);
		ApplicationLauncher.logger.debug("slaveSendFloatDataToPC: Int: " + (e_1));
		// Serial_Slave_Send_Data2_System_Func (a_1+48);
		ApplicationLauncher.logger.debug("slaveSendFloatDataToPC: Int: " + (f_1));
		// Serial_Slave_Send_Data2_System_Func (e_1+48);

		ApplicationLauncher.logger.debug("slaveSendFloatDataToPC: Int: " + (g_1));
		ApplicationLauncher.logger.debug("slaveSendFloatDataToPC: Int: " + ".");
		ApplicationLauncher.logger.debug("slaveSendFloatDataToPC: Int: " + (h_1));
		ApplicationLauncher.logger.debug("slaveSendFloatDataToPC: Int: " + (m_1));
		ApplicationLauncher.logger.debug("slaveSendFloatDataToPC: Int: " + (n_1));
		ApplicationLauncher.logger.debug("slaveSendFloatDataToPC: Int: " + (p_1));

	}

	public static void slaveSendDoubleDataToPC(double doubleValue) {
		int b = 0, c = 0, d = 0, e = 0, f = 0, g = 0, h = 0, m = 0, n = 0;
		int c_1 = 0, d_1 = 0, e_1 = 0, f_1 = 0, g_1 = 0, h_1 = 0, m_1 = 0, n_1 = 0, p_1 = 0;

		int l_Main_Master_FB_Data = (int) (doubleValue * 10000);

		// l_Main_Master_FB_Data = (int)(doubleValue * 100000);

		ApplicationLauncher.logger.debug("slaveSendDoubleDataToPC: l_Main_Master_FB_Data: " + l_Main_Master_FB_Data);

		// a=56;
		b = l_Main_Master_FB_Data / 1000000000;
		c = l_Main_Master_FB_Data / 100000000;
		d = l_Main_Master_FB_Data / 10000000;
		e = l_Main_Master_FB_Data / 1000000;
		f = l_Main_Master_FB_Data / 100000;
		g = l_Main_Master_FB_Data / 10000;
		h = l_Main_Master_FB_Data / 1000;
		m = l_Main_Master_FB_Data / 100;
		n = l_Main_Master_FB_Data / 10;

		c_1 = c - (b * 10);
		d_1 = d - (c * 10);
		e_1 = e - (d * 10);
		f_1 = f - (e * 10);
		g_1 = g - (f * 10);
		h_1 = h - (g * 10);
		m_1 = m - (h * 10);
		n_1 = n - (m * 10);
		// e_1=e-(n*10);dxcv
		p_1 = l_Main_Master_FB_Data - (n * 10);

		ApplicationLauncher.logger.debug("slaveSendDoubleDataToPC: Int: " + (b));
		// Serial_Slave_Send_Data2_System_Func (b+48);
		ApplicationLauncher.logger.debug("slaveSendDoubleDataToPC: Int: " + (c_1));
		// Serial_Slave_Send_Data2_System_Func (c_1+48);
		ApplicationLauncher.logger.debug("slaveSendDoubleDataToPC: Int: " + (d_1));
		// Serial_Slave_Send_Data2_System_Func (d_1+48);
		ApplicationLauncher.logger.debug("slaveSendDoubleDataToPC: Int: " + (e_1));
		// Serial_Slave_Send_Data2_System_Func (e_1+48);
		ApplicationLauncher.logger.debug("slaveSendDoubleDataToPC: Int: " + (f_1));
		// Serial_Slave_Send_Data2_System_Func (f_1+48);

		ApplicationLauncher.logger.debug("slaveSendDoubleDataToPC: Int: " + (g_1));
		// Serial_Slave_Send_Data2_System_Func (g_1+48);
		ApplicationLauncher.logger.debug("slaveSendDoubleDataToPC: Int: " + ".");
		// Serial_Slave_Send_Data2_System_Func ('.');
		ApplicationLauncher.logger.debug("slaveSendDoubleDataToPC: Int: " + (h_1));
		// Serial_Slave_Send_Data2_System_Func (h_1+48);
		ApplicationLauncher.logger.debug("slaveSendDoubleDataToPC: Int: " + (m_1));
		// Serial_Slave_Send_Data2_System_Func (m_1+48);
		ApplicationLauncher.logger.debug("slaveSendDoubleDataToPC: Int: " + (n_1));
		// Serial_Slave_Send_Data2_System_Func (n_1+48);
		ApplicationLauncher.logger.debug("slaveSendDoubleDataToPC: Int: " + (p_1));
		// Serial_Slave_Send_Data2_System_Func (p_1+48);

	}

	public static void slaveSendDoubleDataToPCV2(double doubleValue) {
		int b = 0, c = 0, d = 0, e = 0, f = 0, g = 0, h = 0, m = 0, n = 0;
		int c_1 = 0, d_1 = 0, e_1 = 0, f_1 = 0, g_1 = 0, h_1 = 0, m_1 = 0, n_1 = 0, p_1 = 0;

		int l_Main_Master_FB_Data = (int) (doubleValue * 10000);

		// l_Main_Master_FB_Data = (int)(doubleValue * 100000);

		ApplicationLauncher.logger.debug("slaveSendDoubleDataToPCV2: l_Main_Master_FB_Data: " + l_Main_Master_FB_Data);

		// a=56;
		b = l_Main_Master_FB_Data / 1000000000;
		c = l_Main_Master_FB_Data / 100000000;
		d = l_Main_Master_FB_Data / 10000000;
		e = l_Main_Master_FB_Data / 1000000;
		f = l_Main_Master_FB_Data / 100000;
		g = l_Main_Master_FB_Data / 10000;
		h = l_Main_Master_FB_Data / 1000;
		m = l_Main_Master_FB_Data / 100;
		n = l_Main_Master_FB_Data / 10;

		c_1 = c - (b * 10);
		d_1 = d - (c * 10);
		e_1 = e - (d * 10);
		f_1 = f - (e * 10);
		g_1 = g - (f * 10);
		h_1 = h - (g * 10);
		m_1 = m - (h * 10);
		n_1 = n - (m * 10);
		// e_1=e-(n*10);dxcv
		p_1 = l_Main_Master_FB_Data - (n * 10);

		ApplicationLauncher.logger.debug("slaveSendDoubleDataToPCV2: Int: " + (b));
		// Serial_Slave_Send_Data2_System_Func (b+48);
		ApplicationLauncher.logger.debug("slaveSendDoubleDataToPCV2: Int: " + (c_1));
		// Serial_Slave_Send_Data2_System_Func (c_1+48);
		ApplicationLauncher.logger.debug("slaveSendDoubleDataToPCV2: Int: " + (d_1));
		// Serial_Slave_Send_Data2_System_Func (d_1+48);
		ApplicationLauncher.logger.debug("slaveSendDoubleDataToPCV2: Int: " + (e_1));
		// Serial_Slave_Send_Data2_System_Func (a_1+48);
		ApplicationLauncher.logger.debug("slaveSendDoubleDataToPCV2: Int: " + (f_1));
		// Serial_Slave_Send_Data2_System_Func (e_1+48);

		ApplicationLauncher.logger.debug("slaveSendDoubleDataToPCV2: Int: " + (g_1));
		ApplicationLauncher.logger.debug("slaveSendDoubleDataToPCV2: Int: " + ".");
		ApplicationLauncher.logger.debug("slaveSendDoubleDataToPCV2: Int: " + (h_1));
		ApplicationLauncher.logger.debug("slaveSendDoubleDataToPCV2: Int: " + (m_1));
		ApplicationLauncher.logger.debug("slaveSendDoubleDataToPCV2: Int: " + (n_1));
		ApplicationLauncher.logger.debug("slaveSendDoubleDataToPCV2: Int: " + (p_1));

	}

	public static void slaveSendIntDataToPC(int l_Main_Master_FB_Data) {
		int b = 0, c = 0, d = 0, e = 0;
		int a_1 = 0, c_1 = 0, d_1 = 0, e_1 = 0;

		// a=56;
		b = l_Main_Master_FB_Data / 10000;
		c = l_Main_Master_FB_Data / 1000;
		d = l_Main_Master_FB_Data / 100;
		e = l_Main_Master_FB_Data / 10;

		c_1 = c - (b * 10);
		d_1 = d - (c * 10);
		a_1 = e - (d * 10);
		e_1 = l_Main_Master_FB_Data - (e * 10);

		ApplicationLauncher.logger.debug("slaveSendIntDataToPC: Int: " + (b));
		// Serial_Slave_Send_Data2_System_Func (b+48);
		ApplicationLauncher.logger.debug("slaveSendIntDataToPC: Int: " + (c_1));
		// Serial_Slave_Send_Data2_System_Func (c_1+48);
		ApplicationLauncher.logger.debug("slaveSendIntDataToPC: Int: " + (d_1));
		// Serial_Slave_Send_Data2_System_Func (d_1+48);
		ApplicationLauncher.logger.debug("slaveSendIntDataToPC: Int: " + (a_1));
		// Serial_Slave_Send_Data2_System_Func (a_1+48);
		ApplicationLauncher.logger.debug("slaveSendIntDataToPC: Int: " + (e_1));
		// Serial_Slave_Send_Data2_System_Func (e_1+48);

	}

	public static void assertCurrentRangeSettingManualMode() {

	}

	public static void assertParseVoltageDatafromRefStd() {

		// "A3010EF6 01 02 02419903 0400099494707D";
		String inputData = "A3010EF60102024199030400099494707D";
		parseVoltageDatafromRefStd(inputData);
		inputData = "0202419903";
		parseHexStringToFloat(inputData);
		inputData = "0009887766";
		parseHexStringToFloat(inputData);
		inputData = "0019887766";
		parseHexStringToFloat(inputData);
		inputData = "1019887766";
		parseHexStringToFloat(inputData);
		inputData = "1119887766";
		parseHexStringToFloat(inputData);
		inputData = "1109887766";
		parseHexStringToFloat(inputData);
		inputData = "1309887766";
		parseHexStringToFloat(inputData);

		inputData = "0204444444";
		parseHexStringToFloat(inputData);

		inputData = "1319987645";
		parseHexStringToFloat(inputData);

		inputData = "0202460044";
		parseHexStringToFloat(inputData);

		inputData = "0202460044";
		GuiUtils.parseHexStringToFloatString(inputData);
	}

	public static String parseHexStringToFloat(String inputHexString) {
		ApplicationLauncher.logger.debug("parseHexStringToFloat: inputHexString: " + inputHexString);
		String exponentialParsedSign = inputHexString.substring(0, 1);
		int exponentialParsedValue = Integer.parseInt(inputHexString.substring(1, 2));
		// float exponentialParsedValue =
		// Float.parseFloat(inputHexString.substring(1,2));
		if (exponentialParsedSign.equals("1")) {
			exponentialParsedValue = -exponentialParsedValue;
		}
		ApplicationLauncher.logger.debug("parseHexStringToFloat: exponentialParsedValue: " + exponentialParsedValue);
		String mantissaParsedSign = inputHexString.substring(2, 3);
		// ApplicationLauncher.logger.debug("parseHexStringToFloat: mantissaParsedValue:
		// " + inputHexString.substring(3,10));
		// int mantissaParsedValue = Integer.parseInt(inputHexString.substring(3,10));
		float mantissaParsedValue = Float.parseFloat(inputHexString.substring(3, 10));
		if (mantissaParsedSign.equals("1")) {
			mantissaParsedValue = -mantissaParsedValue;
		}
		ApplicationLauncher.logger.debug("parseHexStringToFloat: mantissaParsedValue: " + mantissaParsedValue);

		float value = (mantissaParsedValue / 1000000) * (float) Math.pow(10, exponentialParsedValue);
		ApplicationLauncher.logger.debug("parseHexStringToFloat: value: " + value);

		/*
		 * String valueStr = String.format("%.05f", value);
		 * ApplicationLauncher.logger.debug("parseHexStringToFloat: valueStr: " +
		 * valueStr);
		 * valueStr = valueStr.substring(0, valueStr.length() - 1);
		 * 
		 * 
		 * ApplicationLauncher.logger.
		 * debug("parseHexStringToFloat: ######################################");
		 * ApplicationLauncher.logger.debug("parseHexStringToFloat: inputHexString2: " +
		 * inputHexString);
		 * ApplicationLauncher.logger.debug("parseHexStringToFloat: valueStr1: " +
		 * valueStr);
		 */
		int setScaleAfterDecimal = 5;
		BigDecimal bigValue = new BigDecimal(value);
		// bigValue = bigValue.setScale(setScaleAfterDecimal, RoundingMode.FLOOR);
		bigValue = new BigDecimal(bigValue.toPlainString()).setScale(setScaleAfterDecimal, RoundingMode.CEILING);
		// ApplicationLauncher.logger.debug("parseHexStringToFloat: bigValue: " +
		// bigValue);
		// value = bigValue.floatValue();
		String valueStr = bigValue.toPlainString();
		valueStr = valueStr.substring(0, valueStr.length() - 1);
		// ApplicationLauncher.logger.debug("parseHexStringToFloat: value2: " + value);
		// valueStr = String.format("%.04f", value);
		ApplicationLauncher.logger.debug("parseHexStringToFloat: ######################################");
		ApplicationLauncher.logger.debug("parseHexStringToFloat: inputHexString2: " + inputHexString);
		ApplicationLauncher.logger.debug("parseHexStringToFloat: valueStr2: " + valueStr);
		ApplicationLauncher.logger.debug("parseHexStringToFloat: =====================================");
		return valueStr;
	}

	public static String parseVoltageDatafromRefStd(String serialInputData) {

		String rPhaseVoltageDisplayData = "";
		// yPhaseVoltageDisplayData="";
		// bPhaseVoltageDisplayData="";
		try {

			// ApplicationLauncher.logger.debug("parseVoltageDatafromRefStd : dotPosition: "
			// + dotPosition);
			// ApplicationLauncher.logger.debug("parseVoltageDatafromRefStd : voltageValue:
			// " + voltageValue);
			String floatDataInHex = serialInputData.substring(10, 20);
			ApplicationLauncher.logger.debug("parseVoltageDatafromRefStd : floatDataInHex: " + floatDataInHex);
			rPhaseVoltageDisplayData = parseHexStringToFloat(floatDataInHex);
			// float divisor = 10;

			// rPhaseVoltageDisplayData = String.format("%.4f",
			// Float.parseFloat(voltageValue)/divisor);

			ApplicationLauncher.logger
					.debug("parseVoltageDatafromRefStd : rPhaseVoltageDisplayData:" + rPhaseVoltageDisplayData);
			// rPhaseVoltageDisplayData = parsedData[1];

			// }
		} catch (Exception E) {
			E.printStackTrace();
			ApplicationLauncher.logger.error("parseVoltageDatafromRefStd: Exception :" + E.toString());

		}

		return rPhaseVoltageDisplayData;
	}

	public static void assertCheckSum() {

		String inputString = "FE3456";
		String checkSum = GuiUtils.generateCheckSumWithOneByte(inputString);
		ApplicationLauncher.logger.info("assertCheckSum: checkSum: " + checkSum);

		inputString = "0304";
		checkSum = GuiUtils.generateCheckSumWithOneByte(inputString);
		ApplicationLauncher.logger.info("assertCheckSum: checkSum2: " + checkSum);

	}

	public static void Main_Master_Send_FB_Func() {

		int l_Main_Master_FB_Data = 123456;

		int b = 0, c = 0, d = 0, e = 0, f = 0;
		int a_1 = 0, c_1 = 0, d_1 = 0, e_1 = 0, f_1 = 0;
		;

		// a=56;
		b = l_Main_Master_FB_Data / 100000;
		c = l_Main_Master_FB_Data / 10000;
		d = l_Main_Master_FB_Data / 1000;
		e = l_Main_Master_FB_Data / 100;
		f = l_Main_Master_FB_Data / 10;

		c_1 = c - (b * 10);
		d_1 = d - (c * 10);
		a_1 = e - (d * 10);
		e_1 = f - (e * 10);
		f_1 = l_Main_Master_FB_Data - (f * 10);

		/*
		 * Serial_Master_Send_Data2_System_Func (b+48);
		 * 
		 * Serial_Master_Send_Data2_System_Func (c_1+48);
		 * 
		 * Serial_Master_Send_Data2_System_Func (d_1+48);
		 * 
		 * Serial_Master_Send_Data2_System_Func (a_1+48);
		 * 
		 * Serial_Master_Send_Data2_System_Func (e_1+48);
		 */

		ApplicationLauncher.logger.info("Main_Master_Send_FB_Func: b: " + b);
		ApplicationLauncher.logger.info("Main_Master_Send_FB_Func: c_1: " + c_1);
		ApplicationLauncher.logger.info("Main_Master_Send_FB_Func: d_1: " + d_1);
		ApplicationLauncher.logger.info("Main_Master_Send_FB_Func: a_1: " + a_1);
		ApplicationLauncher.logger.info("Main_Master_Send_FB_Func: e_1: " + e_1);
		ApplicationLauncher.logger.info("Main_Master_Send_FB_Func: f_1: " + f_1);

	}

	public static void assertSignalGenReadRamLogic() {

		// This Function is to place datas in Signal DACS

		// Main_Slave_Get_shift_Data_Func ();

		/*
		 * SIG_DAC_V1_CS = DISABLE; //make all the DAC control pins high
		 * SIG_DAC_I1_CS = DISABLE;
		 * SIG_DAC_V2_CS = DISABLE;
		 * SIG_DAC_I2_CS = DISABLE;
		 * SIG_DAC_V3_CS = DISABLE;
		 * SIG_DAC_I3_CS = DISABLE;
		 */

		// l_Main_Slave_V2_Pfcnt_16 = (Uint16)((l_Main_Slave_RAM_Total_Mem_Occupy_16 *
		// 12.0) / 36.0);

		// all source 12.028999

		// change for 3.3015,3.3016 12.038999
		/*
		 * if(l_Main_Slave_present_phase_16 == 7)//If Master sent I1_Pfcnt
		 * {
		 * 
		 * l_Main_Slave_V2_Pfcnt_16 = (Uint16)((l_Main_Slave_RAM_Total_Mem_Occupy_16 *
		 * 11.9675) / 36.0);//now 12.002//12.028999//11.9675//11.8475//11.625,40
		 * l_Main_Slave_V3_Pfcnt_16 = (Uint16)((l_Main_Slave_RAM_Total_Mem_Occupy_16 *
		 * 24.0027) / 36.0);//now 24.002//24.02974//24.08//23.964//23.475,35
		 * // l_Main_Slave_V2_Pfcnt_16 = (Uint16)((l_Main_Slave_RAM_Total_Mem_Occupy_16
		 * * 11.6) / 36.0);
		 * // l_Main_Slave_V3_Pfcnt_16 = (Uint16)((l_Main_Slave_RAM_Total_Mem_Occupy_16
		 * * 23.9) / 36.0);
		 * }
		 * if(l_Main_Slave_present_phase_16 == 8)//If Master sent I1_Pfcnt
		 * {
		 * l_Main_Slave_V3_Pfcnt_16 = (Uint16)((l_Main_Slave_RAM_Total_Mem_Occupy_16 *
		 * 11.9675) / 36.0);//11.9675//11.8475//11.625,40
		 * l_Main_Slave_V2_Pfcnt_16 = (Uint16)((l_Main_Slave_RAM_Total_Mem_Occupy_16 *
		 * 24.0027) / 36.0);//24.08//23.964//23.475,35
		 * // l_Main_Slave_V2_Pfcnt_16 = (Uint16)((l_Main_Slave_RAM_Total_Mem_Occupy_16
		 * * 11.6) / 36.0);
		 * // l_Main_Slave_V3_Pfcnt_16 = (Uint16)((l_Main_Slave_RAM_Total_Mem_Occupy_16
		 * * 23.9) / 36.0);
		 * }
		 */
		int l_Main_Slave_V2_Pfcnt_16 = 1200;
		int l_Main_Slave_V3_Pfcnt_16 = 2400;
		int l_Main_Slave_Adrs_V1_16 = 0;
		int l_Main_Slave_I1_Pfcnt_16 = 10;
		int l_Main_Slave_I2_Pfcnt_16 = 0;
		int l_Main_Slave_I3_Pfcnt_16 = 0;
		int l_Main_Slave_Adrs_I1_16 = 0;
		int l_Main_Slave_Adrs_V2_16 = 0;

		int l_Main_Slave_Adrs_I2_16 = 0;
		int l_Main_Slave_Adrs_V3_16 = 0;
		int l_Main_Slave_Adrs_I3_16 = 0;

		int l_Main_Slave_I1_Pfbit_16 = 0;
		int l_Main_Slave_V2_Pfbit_16 = 0;

		int l_Main_Slave_I2_Pfbit_16 = 0;
		int l_Main_Slave_V3_Pfbit_16 = 0;
		int l_Main_Slave_I3_Pfbit_16 = 0;

		int l_Main_Slave_RAM_Total_Mem_Occupy_16 = 3600;

		int counter = 0;
		while (counter < 36)// !ScibRegs.SCIRXST.bit.RXRDY)//checking this serial bit is taking 5ms delay in
							// the total signal output
		{
			counter++;

			// RAM_Slave_Read_RAMData_Write_SigDACV1_Func(l_Main_Slave_Adrs_V1_16);
			ApplicationLauncher.logger
					.info("assertSignalGenReadRamLogic : l_Main_Slave_Adrs_V1_16: " + l_Main_Slave_Adrs_V1_16);
			if ((l_Main_Slave_Adrs_V1_16 > (l_Main_Slave_I1_Pfcnt_16))
					&& (l_Main_Slave_Adrs_V1_16 <= (l_Main_Slave_I1_Pfcnt_16 + 3))) {
				// Start the Current(I1) Signal if Adrs_V1 is equal to I1 pf count
				ApplicationLauncher.logger
						.info("assertSignalGenReadRamLogic : l_Main_Slave_I1_Pfcnt_16: " + l_Main_Slave_I1_Pfcnt_16);

				l_Main_Slave_Adrs_I1_16 = 0;
				// l_Main_Slave_Adrs_I1_16 = 2;
				l_Main_Slave_I1_Pfbit_16 = 1;
			}
			if (l_Main_Slave_I1_Pfbit_16 == 1) {
				// RAM_Slave_Read_RAMData_Write_SigDACI1_Func(l_Main_Slave_Adrs_I1_16);
				// l_Main_Slave_Adrs_I1_16++++++;
				l_Main_Slave_Adrs_I1_16 += 3;
			}

			if (l_Main_Slave_Adrs_V1_16 >= l_Main_Slave_V2_Pfcnt_16) {
				// Start the Voltage(V2) Signal if Adrs_V1 is equal to V2 pf count
				l_Main_Slave_Adrs_V2_16 = 0;
				// l_Main_Slave_Adrs_V2_16 = 4;
				l_Main_Slave_V2_Pfbit_16 = 1;
			}
			if (l_Main_Slave_V2_Pfbit_16 == 1) {
				// RAM_Slave_Read_RAMData_Write_SigDACV2_Func(l_Main_Slave_Adrs_V2_16);
				// l_Main_Slave_Adrs_V2_16++;
			}
			if (l_Main_Slave_Adrs_V2_16 >= l_Main_Slave_I2_Pfcnt_16) {
				// Start the Current(I2) Signal if Adrs_V2 is equal to I2 pf count
				l_Main_Slave_Adrs_I2_16 = 0;
				// l_Main_Slave_Adrs_I2_16 = 2;
				l_Main_Slave_I2_Pfbit_16 = 1;
			}
			if (l_Main_Slave_I2_Pfbit_16 == 1) {
				// RAM_Slave_Read_RAMData_Write_SigDACI2_Func(l_Main_Slave_Adrs_I2_16);
				// l_Main_Slave_Adrs_I2_16++++++;
				l_Main_Slave_Adrs_I2_16 += 3;
			}
			if (l_Main_Slave_Adrs_V1_16 >= l_Main_Slave_V3_Pfcnt_16) {
				// Start the Voltage(V3) Signal if Adrs_V1 is equal to V3 pf count
				l_Main_Slave_Adrs_V3_16 = 0;
				l_Main_Slave_V3_Pfbit_16 = 1;
			}
			if (l_Main_Slave_V3_Pfbit_16 == 1) {
				// RAM_Slave_Read_RAMData_Write_SigDACV3_Func(l_Main_Slave_Adrs_V3_16);
				// l_Main_Slave_Adrs_V3_16++;
			}
			if (l_Main_Slave_Adrs_V3_16 >= l_Main_Slave_I3_Pfcnt_16) {
				// Start the Current(I3) Signal if Adrs_V3 is equal to I3 pf count
				l_Main_Slave_Adrs_I3_16 = 0;
				l_Main_Slave_I3_Pfbit_16 = 1;
			}
			if (l_Main_Slave_I3_Pfbit_16 == 1) {
				// RAM_Slave_Read_RAMData_Write_SigDACI3_Func(l_Main_Slave_Adrs_I3_16);
				// l_Main_Slave_Adrs_I3_16++++++;
				l_Main_Slave_Adrs_I3_16 += 3;
			}

			// l_Main_Slave_Adrs_V1_16++++++;
			l_Main_Slave_Adrs_V1_16 += 3;

			if (l_Main_Slave_Adrs_V1_16 >= l_Main_Slave_RAM_Total_Mem_Occupy_16) {
				// Set this variable to zero once l_Main_Slave_Adrs_V1_16 equal to
				// l_Main_Slave_RAM_Total_Mem_Occupy_16
				l_Main_Slave_Adrs_V1_16 = 0;
			}

			if (l_Main_Slave_V2_Pfbit_16 == 1) {
				// Increment this variable once l_Main_Slave_V2_Pfbit_16 is set
				// l_Main_Slave_Adrs_V2_16++++++;
				l_Main_Slave_Adrs_V2_16 += 3;
			}
			if (l_Main_Slave_V3_Pfbit_16 == 1) {
				// Increment this variable once l_Main_Slave_V3_Pfbit_16 is set
				// l_Main_Slave_Adrs_V3_16++++++;
				l_Main_Slave_Adrs_V3_16 += 3;
			}

		}

	}

	public static void assertCalculateDegreeWithPf() {

		/*
		 * String selectedPhase = "R";
		 * String inpCurrentValue = "0.050";
		 * 
		 * String outputCurrentValue = "";
		 * String outputRelayId = "";
		 * outputCurrentValue = DisplayDataObj.getTargetCurrentRms(selectedPhase,
		 * inpCurrentValue);
		 * outputRelayId = getTargetCurrentRelayId(selectedPhase, inpCurrentValue);
		 * ApplicationLauncher.logger.
		 * info("assertCalculateDegreeWithPf : outputCurrentValue1: "+
		 * outputCurrentValue);
		 * ApplicationLauncher.logger.
		 * info("assertCalculateDegreeWithPf : outputRelayId1: "+ outputRelayId);
		 * inpCurrentValue = "0.051";
		 * 
		 * outputCurrentValue = DisplayDataObj.getTargetCurrentRms(selectedPhase,
		 * inpCurrentValue);
		 * outputRelayId = getTargetCurrentRelayId(selectedPhase, inpCurrentValue);
		 * ApplicationLauncher.logger.
		 * info("assertCalculateDegreeWithPf : outputCurrentValue2: "+
		 * outputCurrentValue);
		 * ApplicationLauncher.logger.
		 * info("assertCalculateDegreeWithPf : outputRelayId2: "+ outputRelayId);
		 * 
		 * inpCurrentValue = "0.0509";
		 * 
		 * outputCurrentValue = DisplayDataObj.getTargetCurrentRms(selectedPhase,
		 * inpCurrentValue);
		 */
		// outputRelayId = getTargetCurrentRelayId(selectedPhase, inpCurrentValue);
		// ApplicationLauncher.logger.info("assertCalculateDegreeWithPf :
		// outputCurrentValue3: "+ outputCurrentValue);
		// ApplicationLauncher.logger.info("assertCalculateDegreeWithPf :
		// outputRelayId3: "+ outputRelayId);
		// DeviceDataManagerController.CalculateLagLeadAngle
		// String phasedegree = "-1L";
		// GUIUtils.Validate_PhaseLagLead(phasedegree);
		// CalculateLagLeadAngle(phasedegree);
		String displayFormat = ConstantApp.DISPLAY_PHASE_ANGLE_DEGREE_RESOLUTION;
		/*
		 * String pfValue = "0.0000";
		 * GUIUtils.calculateDegreeWithPf(pfValue,displayFormat);
		 * pfValue = "0.5";
		 * GUIUtils.calculateDegreeWithPf(pfValue,displayFormat);
		 * pfValue = "0.866";
		 * GUIUtils.calculateDegreeWithPf(pfValue,displayFormat);
		 * pfValue = "0.8";
		 * GUIUtils.calculateDegreeWithPf(pfValue,displayFormat);
		 * pfValue = "1";
		 * GUIUtils.calculateDegreeWithPf(pfValue,displayFormat);
		 * pfValue = "-1";
		 * GUIUtils.calculateDegreeWithPf(pfValue,displayFormat);
		 * pfValue = "-0.5";
		 * GUIUtils.calculateDegreeWithPf(pfValue,displayFormat);
		 * pfValue = "-0.8";
		 * GUIUtils.calculateDegreeWithPf(pfValue,displayFormat);
		 * pfValue = "-0.866";
		 * GUIUtils.calculateDegreeWithPf(pfValue,displayFormat);
		 */

		displayFormat = ConstantApp.DISPLAY_PHASE_ANGLE_PF_RESOLUTION;

		String degreeValue = "0";
		GuiUtils.calculatePowerFactorWithDegree(degreeValue, displayFormat);
		degreeValue = "60";
		GuiUtils.calculatePowerFactorWithDegree(degreeValue, displayFormat);
		degreeValue = "90";
		GuiUtils.calculatePowerFactorWithDegree(degreeValue, displayFormat);
		degreeValue = "180";
		GuiUtils.calculatePowerFactorWithDegree(degreeValue, displayFormat);
		degreeValue = "270";
		GuiUtils.calculatePowerFactorWithDegree(degreeValue, displayFormat);
		degreeValue = "300";
		GuiUtils.calculatePowerFactorWithDegree(degreeValue, displayFormat);
		degreeValue = "323";
		GuiUtils.calculatePowerFactorWithDegree(degreeValue, displayFormat);
		degreeValue = "360";
		GuiUtils.calculatePowerFactorWithDegree(degreeValue, displayFormat);
	}

	public static ArrayList<String> CalculateLagLeadAngle(String input) {
		ApplicationLauncher.logger.info("CalculateLagLeadAngle : Entry");
		String lag_lead = input.substring(input.length() - 1);
		ApplicationLauncher.logger.info("input: " + input);
		// ApplicationLauncher.logger.info("get_EM_Model_type: " +
		// getDeployedEM_ModelType());
		double phasedegree = 0;
		float lag_lead_value = 0;
		String phase = "";

		String FirstPhaseDisplayName = "A";
		String SecondPhaseDisplayName = "B";
		String ThirdPhaseDisplayName = "C";

		if (ProcalFeatureEnable.PHASE_DISPLAY_ENABLE_FEATURE) {
			FirstPhaseDisplayName = ConstantApp.FIRST_PHASE_DISPLAY_NAME;
			SecondPhaseDisplayName = ConstantApp.SECOND_PHASE_DISPLAY_NAME;
			ThirdPhaseDisplayName = ConstantApp.THIRD_PHASE_DISPLAY_NAME;
		}

		/*
		 * int ReactiveImportExportSignAngle = 1;
		 * 
		 * if(getEnergyFlowMode().equals( ConstantPowerSource.IMPORT_MODE)){
		 * ReactiveImportExportSignAngle = 1;
		 * }else if(getEnergyFlowMode().equals(ConstantPowerSource.EXPORT_MODE)){
		 * ReactiveImportExportSignAngle = -1;
		 * }
		 */
		ArrayList<String> All_phases = new ArrayList<String>();
		if (lag_lead.equals(ConstantApp.PF_LAG)) {
			try {
				lag_lead_value = Float.parseFloat(input.substring(0, input.length() - 1));
				// if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
				// phasedegree = ConstantMtePowerSource.POWER_SRC_COS_ACTIVE_UPF_ANGLE
				// +(Math.acos(lag_lead_value) * (180/Math.PI));

				// }
				/*
				 * else if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)){
				 */ phasedegree = ConstantPowerSourceMte.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE
						- (Math.acos(lag_lead_value) * (180 / Math.PI));

				/*
				 * }
				 * else {
				 * ApplicationLauncher.logger.info("Mismatch: Lead Lag ");
				 * phasedegree = Math.acos(lag_lead_value) * (180/Math.PI);
				 * }
				 */

				All_phases.add("All");
				All_phases.add(String.format("%.2f", phasedegree));
				ApplicationLauncher.logger.info("CalculateLagLeadAngle : phasedegree-22:" + phasedegree);

			} catch (Exception e1) {
				// e1.printStackTrace();
				// ApplicationLauncher.logger.info("calculatelag_lead :Exception1:"+
				// e1.getMessage());
				phase = input.substring(0, 1);
				/*
				 * lag_lead_value = Float.parseFloat(input.substring(2, input.length() - 1));
				 * if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
				 * phasedegree = ConstantMtePowerSource.POWER_SRC_COS_ACTIVE_UPF_ANGLE+
				 * (Math.acos(lag_lead_value) * (180/Math.PI));
				 * 
				 * }
				 * else if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)){
				 * phasedegree =
				 * ConstantMtePowerSource.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE-(Math.acos(
				 * lag_lead_value) * (180/Math.PI));
				 * 
				 * }
				 * else {
				 * ApplicationLauncher.logger.info("Mismatch: Lead Lag ");
				 * phasedegree = Math.acos(lag_lead_value) * (180/Math.PI);
				 * }
				 */

				if (phase.equals(FirstPhaseDisplayName)) {

					All_phases.add(FirstPhaseDisplayName);
					All_phases.add(String.format("%.2f", phasedegree));

				}

				else if (phase.equals(SecondPhaseDisplayName)) {

					All_phases.add(SecondPhaseDisplayName);
					All_phases.add(String.format("%.2f", phasedegree));

				}

				else if (phase.equals(ThirdPhaseDisplayName)) {

					All_phases.add(ThirdPhaseDisplayName);
					All_phases.add(String.format("%.2f", phasedegree));

				} else {

					All_phases.add("All");
					All_phases.add("0.0");
				}
			}

		} else if (lag_lead.equals(ConstantApp.PF_LEAD)) {
			try {
				lag_lead_value = Float.parseFloat(input.substring(0, input.length() - 1));
				/*
				 * if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
				 * phasedegree =
				 * ConstantMtePowerSource.POWER_SRC_COS_ACTIVE_UPF_ANGLE-(Math.acos(
				 * lag_lead_value) * (180/Math.PI));
				 * 
				 * }
				 * else if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)){
				 * phasedegree =
				 * ConstantMtePowerSource.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE+(Math.acos(
				 * lag_lead_value) * (180/Math.PI));
				 * 
				 * }
				 * else {
				 * ApplicationLauncher.logger.info("Mismatch: Lead Lag ");
				 * phasedegree = Math.acos(lag_lead_value) * (180/Math.PI);
				 * }
				 */
				// phasedegree = -(phasedegree);

				All_phases.add("All");
				All_phases.add(String.format("%.2f", phasedegree));

			} catch (Exception e2) {
				// e2.printStackTrace();
				// ApplicationLauncher.logger.info("calculatelag_lead :Exception2:"+
				// e2.getMessage());
				phase = input.substring(0, 1);
				lag_lead_value = Float.parseFloat(input.substring(2, input.length() - 1));
				/*
				 * if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
				 * phasedegree =
				 * ConstantMtePowerSource.POWER_SRC_COS_ACTIVE_UPF_ANGLE-(Math.acos(
				 * lag_lead_value) * (180/Math.PI));
				 * 
				 * }
				 * else if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)){
				 * phasedegree =
				 * ConstantMtePowerSource.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE+(Math.acos(
				 * lag_lead_value) * (180/Math.PI));
				 * 
				 * }
				 * else {
				 * ApplicationLauncher.logger.info("Mismatch: Lead Lag ");
				 * phasedegree = Math.acos(lag_lead_value) * (180/Math.PI);
				 * }
				 */

				// phasedegree = - phasedegree;

				if (phase.equals(FirstPhaseDisplayName)) {

					All_phases.add(FirstPhaseDisplayName);
					All_phases.add(String.format("%.2f", phasedegree));

				}

				else if (phase.equals(SecondPhaseDisplayName)) {

					All_phases.add(SecondPhaseDisplayName);
					All_phases.add(String.format("%.2f", phasedegree));

				}

				else if (phase.equals(ThirdPhaseDisplayName)) {

					All_phases.add(ThirdPhaseDisplayName);
					All_phases.add(String.format("%.2f", phasedegree));

				} else {

					All_phases.add("All");
					All_phases.add("0.0");
				}
			}

			ApplicationLauncher.logger.info("phasedegree: " + phasedegree);
		} else {
			try {
				lag_lead_value = Float.parseFloat(input);
				phasedegree = 0.0;
				/*
				 * if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
				 * //phasedegree = 0.0;
				 * phasedegree = ConstantMtePowerSource.POWER_SRC_COS_ACTIVE_UPF_ANGLE;
				 * }
				 * else if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)){
				 * 
				 * 
				 * phasedegree = ConstantMtePowerSource.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE;
				 * }
				 * else {
				 * ApplicationLauncher.logger.info("Mismatch: Lead Lag ");
				 * phasedegree = 0.0;
				 * }
				 * All_phases.add("All");
				 * All_phases.add(String.format("%.2f", phasedegree));
				 */

			} catch (Exception e3) {
				// e2.printStackTrace();
				// ApplicationLauncher.logger.info("calculatelag_lead :Exception2:"+
				// e2.getMessage());
				phase = input.substring(0, 1);
				phasedegree = 0.0;
				/*
				 * if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
				 * //phasedegree = 0.0;
				 * phasedegree = ConstantMtePowerSource.POWER_SRC_COS_ACTIVE_UPF_ANGLE;
				 * }
				 * else if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)){
				 * 
				 * phasedegree = ConstantMtePowerSource.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE;
				 * }
				 * else {
				 * ApplicationLauncher.logger.info("Mismatch: Lead Lag ");
				 * phasedegree = 0.0;
				 * }
				 */

				// phasedegree = - phasedegree;

				if (phase.equals(FirstPhaseDisplayName)) {

					All_phases.add(FirstPhaseDisplayName);
					All_phases.add(String.format("%.2f", phasedegree));

				}

				else if (phase.equals(SecondPhaseDisplayName)) {

					All_phases.add(SecondPhaseDisplayName);
					All_phases.add(String.format("%.2f", phasedegree));

				}

				else if (phase.equals(ThirdPhaseDisplayName)) {

					All_phases.add(ThirdPhaseDisplayName);
					All_phases.add(String.format("%.2f", phasedegree));

				} else {

					All_phases.add("All");
					All_phases.add("0.0");
				}
			}

			ApplicationLauncher.logger.info("phasedegree: " + phasedegree);

		}
		ApplicationLauncher.logger.info("All_phases: " + All_phases);
		// setPhaseDegreeOutput(All_phases);
		return All_phases;
	}

	public static void assertCalculateGain() {
		float lastReadRmsValue_Y1 = 39361.0f;
		float lastReadCalibPointValue_X1 = 64.0f;
		float presentReadRmsValue_Y2 = 68263.0f;
		float presentReadCalibPointValue_X2 = 110.0f;
		calculateGain(lastReadRmsValue_Y1, lastReadCalibPointValue_X1,
				presentReadRmsValue_Y2, presentReadCalibPointValue_X2);
	}

	public static String calculateGain(float lastReadRmsValue_Y1, float lastReadCalibPointValue_X1,
			float presentReadRmsValue_Y2, float presentReadCalibPointValue_X2) {

		// y= mx+c , m is gain , c is offset
		ApplicationLauncher.logger.debug("calculateGain: presentReadRmsValue_Y2: " + presentReadRmsValue_Y2);
		ApplicationLauncher.logger.debug("calculateGain: lastReadRmsValue_Y1: " + lastReadRmsValue_Y1);
		ApplicationLauncher.logger
				.debug("calculateGain: presentReadCalibPointValue_X2: " + presentReadCalibPointValue_X2);
		ApplicationLauncher.logger.debug("calculateGain: lastReadCalibPointValue_X1: " + lastReadCalibPointValue_X1);
		double resultGain = ((presentReadRmsValue_Y2 - lastReadRmsValue_Y1)
				/ (presentReadCalibPointValue_X2 - lastReadCalibPointValue_X1));
		ApplicationLauncher.logger.debug("calculateGain: resultGain: " + resultGain);
		String resultRoundedString = String.format("%.02f", resultGain);
		ApplicationLauncher.logger.debug("calculateGain: resultString: " + resultRoundedString);
		return resultRoundedString;
	}

	public static void roundOffValidation(float noOfPulsesFloat) {
		ApplicationLauncher.logger.debug("manipulateNoOfPulsesForTimeBased : noOfPulsesFloat : " + noOfPulsesFloat);
		// BigDecimal bigValue = new BigDecimal(targetRphasePowerFactor);
		BigDecimal bigValue = new BigDecimal(noOfPulsesFloat);
		ApplicationLauncher.logger.debug("manipulateNoOfPulsesForTimeBased : bigValue2 : " + bigValue);
		bigValue = bigValue.setScale(2, RoundingMode.FLOOR);
		ApplicationLauncher.logger.debug("manipulateNoOfPulsesForTimeBased : bigValue scaled up1: " + bigValue);
		bigValue = bigValue.setScale(1, RoundingMode.UP);
		ApplicationLauncher.logger.debug("manipulateNoOfPulsesForTimeBased : bigValue scaled up2: " + bigValue);
		bigValue = bigValue.setScale(0, RoundingMode.UP);
		ApplicationLauncher.logger.debug("manipulateNoOfPulsesForTimeBased : bigValue scaled up3: " + bigValue);
		ApplicationLauncher.logger
				.debug("manipulateNoOfPulsesForTimeBased : ######################################## ");
	}

	public static void assertLduErrorDataHashMap2d() {

		ApplicationLauncher.logger.debug("assertLduErrorDataHashMap2d : Entry");
		DisplayDataObj.resetLduErrorDataHashMap2d();
		DisplayDataObj.addLduErrorDataHashMap2d(1, 1, "+1.01");
		DisplayDataObj.addLduErrorDataHashMap2d(1, 2, "+1.02");
		DisplayDataObj.addLduErrorDataHashMap2d(1, 3, "+1.03");
		DisplayDataObj.addLduErrorDataHashMap2d(1, 4, "+1.04");
		DisplayDataObj.addLduErrorDataHashMap2d(1, 5, "+1.05");

		String errorData = DisplayDataObj.getLduErrorDataHashMap2d(1, 1);
		ApplicationLauncher.logger.debug("assertLduErrorDataHashMap2d : Address 1: index 1: " + errorData);
		DisplayDataObj.getAverageLduErrorDataHashMap2d(1);
	}

	public static void assertLscsFineTuneRphaseCurrent() {

		lscsFineTuneRphaseCurrent();

	}

	public static void lscsFineTuneRphaseCurrent() {

		ApplicationLauncher.logger.debug("lscsFineTuneRphaseCurrent : Entry");

		float currentFinetuneAcceptableMinPercent = -0.02f;
		float currentFinetuneAcceptableMaxPercent = 0.02f;

		float expectedTargetCurrent = 5.0f;// Float.parseFloat(Data_CI_Src.getWritePrimaryTargetCurrent());
		float actualCurrent = 4.9f;
		float expectedCurrentLowerLimit = 0;
		float expectedCurrentUpperLimit = 0;
		float currentAcceptedFineTunePercentage = 0.2f;
		// String actualPercentage = "";
		// float missingPercentage = 0.0f;

		int maxRetryCount = 5;// ConstantConfigProGEN.FineTuneI_MaxRetryCount;//10;zvxc
		int retryCount = maxRetryCount;

		expectedCurrentUpperLimit = expectedTargetCurrent * ((100 + currentAcceptedFineTunePercentage) / 100);
		expectedCurrentLowerLimit = expectedTargetCurrent;// expectedTargetCurrent * ((100 -
															// currentAcceptedFineTunePercentage) / 100);
		ApplicationLauncher.logger.info("lscsFineTuneRphaseCurrent: expectedTargetCurrent: " + expectedTargetCurrent);
		ApplicationLauncher.logger.info(
				"lscsFineTuneRphaseCurrent: currentAcceptedFineTunePercentage: " + currentAcceptedFineTunePercentage);
		ApplicationLauncher.logger
				.info("lscsFineTuneRphaseCurrent: expectedCurrentLowerLimit: " + expectedCurrentLowerLimit);
		ApplicationLauncher.logger
				.info("lscsFineTuneRphaseCurrent: expectedCurrentUpperLimit: " + expectedCurrentUpperLimit);

		if ((expectedCurrentLowerLimit >= actualCurrent) && (actualCurrent <= expectedCurrentUpperLimit)) {
			ApplicationLauncher.logger.info("lscsFineTuneRphaseCurrent: already in range");
		} else {

		}

	}

	public static void assertCalculateXorCheckSum() {

		/*
		 * List<Float> list = Arrays.asList(20.0f, 10.0f, 100.0f, 140.0f, 250.0f);
		 * Float max = Collections.max(list);
		 * ApplicationLauncher.logger.info("Maximum element is: "+max);
		 */

		String payLoadInHex = "53010201";// 52
		String xorCheckSum = generateXorCheckSum(payLoadInHex);
		ApplicationLauncher.logger.info("generateXorCheckSu: xorCheckSum : " + xorCheckSum);

		payLoadInHex = "53010101";// 52
		xorCheckSum = generateXorCheckSum(payLoadInHex);
		ApplicationLauncher.logger.info("generateXorCheckSu: xorCheckSum2 : " + xorCheckSum);
	}

	public static String generateXorCheckSum(String InputHexString) {
		String hex_value = new String();
		// 'hex_value' will be used to store various hex values as a string
		int x, i, checksum = 0;
		// 'x' will be used for general purpose storage of integer values
		// 'i' is used for loops
		// 'checksum' will store the final checksum
		for (i = 0; i < InputHexString.length() - 2; i = i + 2) {
			hex_value = Character.toString(InputHexString.charAt(i)) + Character.toString(InputHexString.charAt(i + 1));
			// Extract two characters and get their hexadecimal ASCII values
			ApplicationLauncher.logger.info("generateXorCheckSum: InputHexString1:" + InputHexString.charAt(i) + ""
					+ InputHexString.charAt(i + 1) + " : "
					+ hex_value);
			x = Integer.parseInt(hex_value, 16);
			ApplicationLauncher.logger.info("generateXorCheckSum: Int Value:" + x);
			// Convert the hex_value into int and store it
			// checksum += x;
			checksum ^= x;
			// Add 'x' into 'checksum'
		}
		if (InputHexString.length() % 2 == 0) {
			hex_value = Character.toString(InputHexString.charAt(i)) + Character.toString(InputHexString.charAt(i + 1));
			ApplicationLauncher.logger.info("generateXorCheckSum: InputHexString2:" + InputHexString.charAt(i) + ""
					+ InputHexString.charAt(i + 1) + " : "
					+ hex_value);
			x = Integer.parseInt(hex_value, 16);
		} else {
			// If number of characters is odd, last 2 digits will be 00.
			x = (int) (InputHexString.charAt(i));
			hex_value = "00" + Character.toString(InputHexString.charAt(i));// Integer.toHexString(x);
			x = Integer.parseInt(hex_value, 16);
			ApplicationLauncher.logger
					.info("generateXorCheckSum: InputHexString3:" + InputHexString.charAt(i) + " : " + hex_value);
		}
		checksum ^= x;
		// Add the generated value of 'x' from the if-else case into 'checksum'
		hex_value = Integer.toHexString(checksum);
		hex_value = StringUtils.leftPad(hex_value.toUpperCase(), 2, '0');
		return hex_value;
	}

	public static void assertGetTargetVoltageRms() {

		// String voltage = "70.0";
		// String selectedPhase = "R";
		// String value = getTargetVoltageRms(selectedPhase, voltage);
		// ApplicationLauncher.logger.debug("assertGetTargetVoltageRms: value: "+
		// value);
		// voltage = "60.0";
		// value = getTargetVoltageRms(selectedPhase, voltage);
		// ApplicationLauncher.logger.debug("assertGetTargetVoltageRms: value: "+
		// value);
		// voltage = "50.0";
		// value = getTargetVoltageRms(selectedPhase, voltage);
		// ApplicationLauncher.logger.debug("assertGetTargetVoltageRms: value: "+
		// value);
		// selectedPhase = "Y";
		// voltage = "70.0";
		// value = getTargetVoltageRms(selectedPhase, voltage);
		// ApplicationLauncher.logger.debug("assertGetTargetVoltageRms: value2: "+
		// value);
		// selectedPhase = "B";
		// voltage = "50.0";
		// value = getTargetVoltageRms(selectedPhase, voltage);
		// ApplicationLauncher.logger.debug("assertGetTargetVoltageRms: value3: "+
		// value);
	}

	public static void assertGetTargetCurrentRms() {

		String current = "0.060";
		String selectedPhase = "R";
		String value = getTargetCurrentRms(selectedPhase, current);
		String currentRelayId = getTargetCurrentRelayId(selectedPhase, current);
		ApplicationLauncher.logger.debug("assertGetTargetCurrentRms: value: " + value);
		ApplicationLauncher.logger.debug("assertGetTargetCurrentRms: currentRelayId0: " + currentRelayId);

		current = "0.252";
		selectedPhase = "R";
		value = getTargetCurrentRms(selectedPhase, current);
		currentRelayId = getTargetCurrentRelayId(selectedPhase, current);
		ApplicationLauncher.logger.debug("assertGetTargetCurrentRms: value: " + value);
		ApplicationLauncher.logger.debug("assertGetTargetCurrentRms: currentRelayId1: " + currentRelayId);
		current = "1.212";
		value = getTargetCurrentRms(selectedPhase, current);
		currentRelayId = getTargetCurrentRelayId(selectedPhase, current);
		ApplicationLauncher.logger.debug("assertGetTargetCurrentRms: value: " + value);
		ApplicationLauncher.logger.debug("assertGetTargetCurrentRms: currentRelayId2: " + currentRelayId);
		current = "11.128";
		value = getTargetCurrentRms(selectedPhase, current);
		currentRelayId = getTargetCurrentRelayId(selectedPhase, current);
		ApplicationLauncher.logger.debug("assertGetTargetCurrentRms: value: " + value);
		ApplicationLauncher.logger.debug("assertGetTargetCurrentRms: currentRelayId3: " + currentRelayId);

		current = "53.480";
		value = getTargetCurrentRms(selectedPhase, current);
		currentRelayId = getTargetCurrentRelayId(selectedPhase, current);
		ApplicationLauncher.logger.debug("assertGetTargetCurrentRms: value: " + value);
		ApplicationLauncher.logger.debug("assertGetTargetCurrentRms: currentRelayId3: " + currentRelayId);

		current = "110.124";
		value = getTargetCurrentRms(selectedPhase, current);
		currentRelayId = getTargetCurrentRelayId(selectedPhase, current);
		ApplicationLauncher.logger.debug("assertGetTargetCurrentRms: value: " + value);
		ApplicationLauncher.logger.debug("assertGetTargetCurrentRms: currentRelayId3: " + currentRelayId);
	}

	// public static String getTargetVoltageRms(String selectedPhase, String
	// inpVoltageValue){
	// String voltageRmsValueStr = "0";
	// try{
	// int calibSize =
	// DeviceDataManagerController.getLscsCalibrationConfigParsedKey().getVoltageCalibration().size();
	// List<VoltageCalibration> voltCalibList =
	// DeviceDataManagerController.getLscsCalibrationConfigParsedKey().getVoltageCalibration();
	// VoltageCalibration voltCalib = new VoltageCalibration();
	// float lastReadRmsValue = 0.0f;
	// float lastReadCalibPointValue = 0.0f;
	// for(int phaseIndex=0; phaseIndex< voltCalibList.size() ; phaseIndex++){
	//
	// if(voltCalibList.get(phaseIndex).getVoltagePhase().equals(selectedPhase)){
	// ApplicationLauncher.logger.debug("getTargetVoltageRms: phaseIndex: "+
	// phaseIndex);
	// voltCalib = voltCalibList.get(phaseIndex);
	// float inpVoltage = Float.parseFloat(inpVoltageValue);
	// ApplicationLauncher.logger.debug("getTargetVoltageRms: inpVoltage: "+
	// inpVoltage);
	// List<VoltageTap> voltCalibTapList = voltCalib.getVoltageTap();
	// for(int tapIndex = 0; tapIndex< voltCalib.getVoltageTap().size();
	// tapIndex++){
	// ApplicationLauncher.logger.debug("getTargetVoltageRms: getVoltageTapMax: "+
	// voltCalibTapList.get(tapIndex).getVoltageTapMax());
	// if(inpVoltage <= voltCalibTapList.get(tapIndex).getVoltageTapMax()){
	// ApplicationLauncher.logger.debug("getTargetVoltageRms: phaseIndex2: "+
	// phaseIndex);
	// ApplicationLauncher.logger.debug("getTargetVoltageRms: tapIndex: "+
	// tapIndex);
	// List<CalibPoints> calibPointList =
	// voltCalibTapList.get(tapIndex).getCalibPoints();
	// for(int calibPointIndex = 0; calibPointIndex < calibPointList.size();
	// calibPointIndex++){
	// if(inpVoltage <= calibPointList.get(calibPointIndex).getCalibPointValue() ){
	// ApplicationLauncher.logger.debug("getTargetVoltageRms: getCalibPointValue: "+
	// calibPointList.get(calibPointIndex).getCalibPointValue());
	// if(calibPointList.get(calibPointIndex).getCalibPointValue() == inpVoltage){
	// voltageRmsValueStr =
	// String.valueOf(calibPointList.get(calibPointIndex).getRmsValue());
	// return voltageRmsValueStr;
	// }else {
	//
	// float inpVolt_X = inpVoltage;
	// float lastReadRmsValue_Y1 = lastReadRmsValue;
	// float lastReadCalibPointValue_X1 = lastReadCalibPointValue;
	// float lastReadRmsValue_Y2 =
	// calibPointList.get(calibPointIndex).getRmsValue();
	// float lastReadCalibPointValue_X2 =
	// calibPointList.get(calibPointIndex).getCalibPointValue();
	// voltageRmsValueStr = calculateLinearInterpolation(inpVolt_X,
	// lastReadRmsValue_Y1, lastReadCalibPointValue_X1,
	// lastReadRmsValue_Y2,lastReadCalibPointValue_X2); //y= mx+c //y = y1 +
	// (x-x1)[(y2-y1)/(x2-x1)] // unknown is y
	// return voltageRmsValueStr;
	// }
	// }else{
	// lastReadRmsValue = calibPointList.get(calibPointIndex).getRmsValue();
	// lastReadCalibPointValue =
	// calibPointList.get(calibPointIndex).getCalibPointValue();
	// }
	// }
	// }
	// }
	//
	// }
	//
	// }
	// } catch (Exception e) {
	//
	// e.printStackTrace();
	// ApplicationLauncher.logger.error("getTargetVoltageRms: Exception:
	// "+e.getMessage());
	// }
	//
	// return voltageRmsValueStr;
	// }

	public static String getTargetCurrentRms(String selectedPhase, String inpCurrentValue) {
		String currentRmsValueStr = "0";
		// try{
		// int calibSize =
		// DeviceDataManagerController.getLscsCalibrationConfigParsedKey().getCurrentCalibration().size();
		// List<CurrentCalibration> currentCalibList =
		// DeviceDataManagerController.getLscsCalibrationConfigParsedKey().getCurrentCalibration();
		// CurrentCalibration currentCalib = new CurrentCalibration();
		// float lastReadRmsValue = 0.0f;
		// float lastReadCalibPointValue = 0.0f;
		// for(int phaseIndex=0; phaseIndex< currentCalibList.size() ; phaseIndex++){
		//
		// if(currentCalibList.get(phaseIndex).getCurrentPhase().equals(selectedPhase)){
		// ApplicationLauncher.logger.debug("getTargetCurrentRms: phaseIndex: "+
		// phaseIndex);
		// currentCalib = currentCalibList.get(phaseIndex);
		// float inpCurrent = Float.parseFloat(inpCurrentValue);
		// ApplicationLauncher.logger.debug("getTargetCurrentRms: inpCurrent: "+
		// inpCurrent);
		// List<CurrentTap> currentCalibTapList = currentCalib.getCurrentTap();
		// for(int tapIndex = 0; tapIndex< currentCalib.getCurrentTap().size();
		// tapIndex++){
		// ApplicationLauncher.logger.debug("getTargetCurrentRms: getCurrentTapMax: "+
		// currentCalibTapList.get(tapIndex).getCurrentTapMax());
		// if(inpCurrent <= currentCalibTapList.get(tapIndex).getCurrentTapMax()){
		// ApplicationLauncher.logger.debug("getTargetCurrentRms: phaseIndex2: "+
		// phaseIndex);
		// ApplicationLauncher.logger.debug("getTargetCurrentRms: tapIndex: "+
		// tapIndex);
		// List<CalibPoints> calibPointList =
		// currentCalibTapList.get(tapIndex).getCalibPoints();
		// for(int calibPointIndex = 0; calibPointIndex < calibPointList.size();
		// calibPointIndex++){
		// if(inpCurrent <= calibPointList.get(calibPointIndex).getCalibPointValue() ){
		// ApplicationLauncher.logger.debug("getTargetCurrentRms: getCalibPointValue: "+
		// calibPointList.get(calibPointIndex).getCalibPointValue());
		// if(calibPointList.get(calibPointIndex).getCalibPointValue() == inpCurrent){
		// currentRmsValueStr =
		// String.valueOf(calibPointList.get(calibPointIndex).getRmsValue());
		// return currentRmsValueStr;
		// }else {
		//
		// float inpCurrent_X = inpCurrent;
		// float lastReadRmsValue_Y1 = lastReadRmsValue;
		// float lastReadCalibPointValue_X1 = lastReadCalibPointValue;
		// float lastReadRmsValue_Y2 =
		// calibPointList.get(calibPointIndex).getRmsValue();
		// float lastReadCalibPointValue_X2 =
		// calibPointList.get(calibPointIndex).getCalibPointValue();
		// currentRmsValueStr = calculateLinearInterpolation(inpCurrent_X,
		// lastReadRmsValue_Y1, lastReadCalibPointValue_X1,
		// lastReadRmsValue_Y2,lastReadCalibPointValue_X2); //y= mx+c //y = y1 +
		// (x-x1)[(y2-y1)/(x2-x1)] // unknown is y
		// return currentRmsValueStr;
		// }
		// }else{
		// lastReadRmsValue = calibPointList.get(calibPointIndex).getRmsValue();
		// lastReadCalibPointValue =
		// calibPointList.get(calibPointIndex).getCalibPointValue();
		// }
		// }
		// }
		// }
		//
		// }
		//
		// }
		// } catch (Exception e) {
		//
		// e.printStackTrace();
		// ApplicationLauncher.logger.error("getTargetCurrentRms: Exception:
		// "+e.getMessage());
		// }

		return currentRmsValueStr;
	}

	public static String getTargetCurrentRelayId(String selectedPhase, String inpCurrentValue) {
		String currentRelayId = "000";

		// try{
		// int calibSize =
		// DeviceDataManagerController.getLscsCalibrationConfigParsedKey().getCurrentCalibration().size();
		// List<CurrentCalibration> currentCalibList =
		// DeviceDataManagerController.getLscsCalibrationConfigParsedKey().getCurrentCalibration();
		// CurrentCalibration currentCalib = new CurrentCalibration();
		// float lastReadRmsValue = 0.0f;
		// float lastReadCalibPointValue = 0.0f;
		// for(int phaseIndex=0; phaseIndex< currentCalibList.size() ; phaseIndex++){
		//
		// if(currentCalibList.get(phaseIndex).getCurrentPhase().equals(selectedPhase)){
		// ApplicationLauncher.logger.debug("getTargetCurrentRelayId: phaseIndex: "+
		// phaseIndex);
		// currentCalib = currentCalibList.get(phaseIndex);
		// float inpCurrent = Float.parseFloat(inpCurrentValue);
		// ApplicationLauncher.logger.debug("getTargetCurrentRelayId: inpCurrent: "+
		// inpCurrent);
		// List<CurrentTap> currentCalibTapList = currentCalib.getCurrentTap();
		// for(int tapIndex = 0; tapIndex< currentCalib.getCurrentTap().size();
		// tapIndex++){
		// ApplicationLauncher.logger.debug("getTargetCurrentRelayId: getCurrentTapMax:
		// "+ currentCalibTapList.get(tapIndex).getCurrentTapMax());
		// if(inpCurrent <= currentCalibTapList.get(tapIndex).getCurrentTapMax()){
		// ApplicationLauncher.logger.debug("getTargetCurrentRelayId: phaseIndex2: "+
		// phaseIndex);
		// ApplicationLauncher.logger.debug("getTargetCurrentRelayId: tapIndex: "+
		// tapIndex);
		// currentRelayId = currentCalibTapList.get(tapIndex).getCurrentRelayId();
		// ApplicationLauncher.logger.debug("getTargetCurrentRelayId: currentRelayId: "+
		// currentRelayId);
		// return currentRelayId;
		//
		// }
		// }
		//
		// }
		//
		// }
		// } catch (Exception e) {
		//
		// e.printStackTrace();
		// ApplicationLauncher.logger.error("getTargetCurrentRelayId: Exception:
		// "+e.getMessage());
		// }

		return currentRelayId;
	}

	public static String calculateLinearInterpolation(float inpValue_X, float lastReadRmsValue_Y1,
			float lastReadCalibPointValue_X1,
			float lastReadRmsValue_Y2, float lastReadCalibPointValue_X2) {

		// y= mx+c //y = y1 + (x-x1)[(y2-y1)/(x2-x1)] // unknown is y
		float result_Y = lastReadRmsValue_Y1 +
				((inpValue_X - lastReadCalibPointValue_X1) *
						((lastReadRmsValue_Y2 - lastReadRmsValue_Y1)
								/ (lastReadCalibPointValue_X2 - lastReadCalibPointValue_X1)));
		ApplicationLauncher.logger.debug("calculateLinearInterpolation: result_Y: " + result_Y);
		String resultLongString = String.valueOf((long) Double.parseDouble(String.valueOf(result_Y)));
		ApplicationLauncher.logger.debug("calculateLinearInterpolation: resultLongString: " + resultLongString);
		return resultLongString;
	}

	public static void assertSandsRefStdResponse() {

		// String responseR_Phase =
		// "52000000003F8FA15E000000000000000000000000000000004247D808C8";
		// String responseY_Phase =
		// "59000000003D1F34AE80000000000000008000000000000000424923A76E";
		// String responseB_Phase =
		// "42000000003D2339A9800000000000000080000000000000004248320DF9";
		// String responseData = responseY_Phase;
		// Communicator mCom = new Communicator("Com1");
		// SerialDataSandsRefStd mSandsRefStd = new SerialDataSandsRefStd(mCom);
		/// * String voltage = mSandsRefStd.ParseVoltageDatafromRefStd(responseData);
		// ApplicationLauncher.logger.debug("assertSandsRefStdResponse: voltage: "+
		// voltage);
		// String current = mSandsRefStd.ParseCurrentDatafromRefStd(responseData);
		// ApplicationLauncher.logger.debug("assertSandsRefStdResponse: current: "+
		// current);
		//
		//
		// String watt = mSandsRefStd.ParseWattDatafromRefStd(responseData);
		// ApplicationLauncher.logger.debug("assertSandsRefStdResponse: watt: "+ watt);
		// String apparantPower = mSandsRefStd.ParseVA_DatafromRefStd(responseData);
		// ApplicationLauncher.logger.debug("assertSandsRefStdResponse: apparantPower:
		// "+ apparantPower);
		// String reactivePower = mSandsRefStd.ParseVAR_DatafromRefStd(responseData);
		// ApplicationLauncher.logger.debug("assertSandsRefStdResponse: reactivePower:
		// "+ reactivePower);
		// String activePower = mSandsRefStd.ParseWattDatafromRefStd(responseData);
		// ApplicationLauncher.logger.debug("assertSandsRefStdResponse: activePower: "+
		// activePower);
		// String freq = mSandsRefStd.ParseFreqDatafromRefStd(responseData);
		// ApplicationLauncher.logger.debug("assertSandsRefStdResponse: freq: "+
		// freq);*/
		//
		// //String responseTotalPowerAndAngle =
		// "54370CC51EB6E871493AA350AFBBDCA8F24387316342B400004387000043B09F7F4279E57D4256B83607";
		/// * String responseTotalPowerAndAngle =
		// "5439AFF665B6A90C923C211D9CBD0BCB934387FA5A42B4000042B4000043B1A46A426D0B43435A769E6D";
		// String rDegree =
		// mSandsRefStd.ParseDegreeR_PhaseDatafromRefStd(responseTotalPowerAndAngle);
		// ApplicationLauncher.logger.debug("assertSandsRefStdResponse: rDegree: "+
		// rDegree);
		//
		// String yDegree =
		// mSandsRefStd.ParseDegreeY_PhaseDatafromRefStd(responseTotalPowerAndAngle);
		// ApplicationLauncher.logger.debug("assertSandsRefStdResponse: yDegree: "+
		// yDegree);
		//
		// String bDegree =
		// mSandsRefStd.ParseDegreeB_PhaseDatafromRefStd(responseTotalPowerAndAngle);
		// ApplicationLauncher.logger.debug("assertSandsRefStdResponse: bDegree: "+
		// bDegree);*/
		// //"5439AFF665B6A90C923C211D9CBD0BCB934387FA5A42B4000042B4000043B1A46A426D0B43435A769E6D";
		// DeviceDataManagerController DisplayDataObj = new
		// DeviceDataManagerController();
		// DisplayDataObj.setDeployedEM_ModelType(ConstantApp.METERTYPE_THREEPHASE);
		// String responseAccumulatedEnergy=
		// "510000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000051";
		// mSandsRefStd.RefStd_DecodeSerialDataForConstTest(responseAccumulatedEnergy);
	}

	public static String RSS_Pulse_Rate = "100000000";

	/*
	 * public static int ARDUINO_PORT00 = 0x0001;
	 * public static int ARDUINO_PORT01 = 0x0002;
	 * public static int ARDUINO_PORT02 = 0x0004;
	 * public static int ARDUINO_PORT03 = 0x0008;
	 * public static int ARDUINO_PORT04 = 0x0010;
	 * public static int ARDUINO_PORT05 = 0x0020;
	 * public static int ARDUINO_PORT06 = 0x0040;
	 * public static int ARDUINO_PORT07 = 0x0080;
	 * public static int ARDUINO_PORT08 = 0x0100;
	 * public static int ARDUINO_PORT09 = 0x0200;
	 * public static int ARDUINO_PORT10 = 0x0400;
	 * public static int ARDUINO_PORT11 = 0x0800;
	 * public static int ARDUINO_PORT12 = 0x1000;
	 * public static int ARDUINO_PORT13 = 0x2000;
	 * public static int ARDUINO_PORT14 = 0x4000;
	 * public static int ARDUINO_PORT15 = 0x8000;
	 */

	public static int ARDUINO_PORT_ALL_OFF = 0x00; // All Off
	public static int ARDUINO_PORT01 = 0x01; // Port1 On
	public static int ARDUINO_PORT02 = 0x02; // Port1 On
	public static int ARDUINO_PORT03 = 0x03; // Port1 On
	public static int ARDUINO_PORT04 = 0x04; // Port1 On
	public static int ARDUINO_PORT05 = 0x05; // Port1 On
	public static int ARDUINO_PORT06 = 0x06; // Port1 On
	public static int ARDUINO_PORT07 = 0x07; // Port1 On
	public static int ARDUINO_PORT08 = 0x08; // Port1 On
	public static int ARDUINO_PORT09 = 0x09; // Port1 On
	public static int ARDUINO_PORT10 = 0x0A; // Port1 On
	public static int ARDUINO_PORT11 = 0x0B; // Port1 On
	public static int ARDUINO_PORT12 = 0x0C; // Port1 On
	public static int ARDUINO_PORT13 = 0x0D; // Port1 On
	public static int ARDUINO_PORT14 = 0x0E; // Port1 On
	public static int ARDUINO_PORT15 = 0x0F; // Port1 On

	public static boolean ARDUINO_PORT_STATUS_ON = true;
	public static boolean ARDUINO_PORT_STATUS_OFF = false;

	public static void AssertIEEE_754() {

		String hexString = "4375F6EF";
		IEEE754_Format.hexToFloat(hexString);// printed 245.96458

	}

	public static void AssertBinaryToBCD() {
		int binaryInput = ARDUINO_PORT_ALL_OFF;// 0x01;
		// convertBinaryToBCD(binaryInput,ARDUINO_PORT_STATUS_OFF);
		binaryInput = ARDUINO_PORT15;// 0x01;
		// convertBinaryToBCD(binaryInput,ARDUINO_PORT_STATUS_OFF);

	}

	public static void convertBinaryToBCD(int binaryInput, boolean status) {

		// int binaryInput = 0x01;
		int bcdResult = 0;
		int shift = 0;
		// for( int i = 0x00 ; i <= 0x0F; i++){
		// binaryInput = i;
		ApplicationLauncher.logger.debug("binaryInput: " + binaryInput + " , Hex: "
				+ String.format("0x%02X", binaryInput) + ", Binary:" + Integer.toBinaryString(binaryInput));
		bcdResult = 0;
		shift = 0;

		while (binaryInput > 0) {
			bcdResult |= (binaryInput % 10) << (shift++ << 2);
			binaryInput /= 10;
		}

		ApplicationLauncher.logger.debug("BCD: " + bcdResult + ", Hex:" + String.format("0x%02X", bcdResult)
				+ ", Binary:" + Integer.toBinaryString(bcdResult));
		// binaryInput++;
		// }
	}

	public static void AssertSetPulseConstant() {
		/*
		 * ApplicationLauncher.logger.debug("Assert hextofloat");
		 * String SerialInputData =
		 * "B60D00540E1C40080D1C28840329C2BAFB22824EF842DB110770EDDA0323703D0A14791A0B19D1150B06B76905481E1E058B7013FE771B3F7F7FFFFF800000000770EDDA0A14791A0B19D1150B06B19B80000000800000001B53";
		 * if(SerialInputData.length()>=(ConstantRadiantRefStd.
		 * REF_STD_INSTANT_METRICS_VOLTAGE_POSITION+8)){
		 * ApplicationLauncher.logger.debug("Assert SerialInputData: " +
		 * SerialInputData);
		 * String VoltageInHex = SerialInputData.substring(ConstantRadiantRefStd.
		 * REF_STD_INSTANT_METRICS_VOLTAGE_POSITION,
		 * (ConstantRadiantRefStd.REF_STD_INSTANT_METRICS_VOLTAGE_POSITION+8));
		 * ApplicationLauncher.logger.debug("Assert VoltageInHex: " + VoltageInHex);
		 * String VoltageDisplayData=TMS_FloatConversion.hextofloat(VoltageInHex);
		 * ApplicationLauncher.logger.debug("Assert VoltageDisplayData: " +
		 * VoltageDisplayData);
		 * }
		 */

		// TMS_FloatConversion.hextofloat(hexString);
		/*
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0249f, 59.0f);
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0249f, 60.0f);
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0249f, 61.0f);
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * 
		 * 
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0249f, 119.99f);
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0249f, 120.0f);
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0249f, 120.01f);
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * 
		 * 
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0249f, 239.99f);
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0249f, 240.0f);
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0249f, 240.01f);
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * 
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0249f, 479.99f);
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0249f, 480.0f);
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0249f, 480.01f);
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 */

		/*
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0250f, 59.0f);
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0250f, 60.0f);
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0250f, 61.0f);
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * 
		 * 
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0250f, 119.99f);
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0250f, 120.0f);
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0250f, 120.01f);
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * 
		 * 
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0250f, 239.99f);
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0250f, 240.0f);
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0250f, 240.01f);
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * 
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0250f, 479.99f);
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0250f, 480.0f);
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0250f, 480.01f);
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 */

		/*
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0251f, 59.0f);
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0251f, 60.0f);
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0251f, 61.0f);
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * 
		 * 
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0251f, 119.99f);
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0251f, 120.0f);
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0251f, 120.01f);
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * 
		 * 
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0251f, 239.99f);
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0251f, 240.0f);
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0251f, 240.01f);
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * 
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0251f, 479.99f);
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0251f, 480.0f);
		 * SetPulseConstantDataWithVoltageAndCurrent(0.0251f, 480.01f);
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 */

		/*
		 * SetPulseConstantDataWithVoltageAndCurrent(9.9f, 59.0f);
		 * SetPulseConstantDataWithVoltageAndCurrent(9.9f, 60.0f);
		 * SetPulseConstantDataWithVoltageAndCurrent(9.9f, 61.0f);
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * 
		 * 
		 * SetPulseConstantDataWithVoltageAndCurrent(9.9f, 119.99f);
		 * SetPulseConstantDataWithVoltageAndCurrent(9.9f, 120.0f);
		 * SetPulseConstantDataWithVoltageAndCurrent(9.9f, 120.01f);
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * 
		 * 
		 * SetPulseConstantDataWithVoltageAndCurrent(9.9f, 239.99f);
		 * SetPulseConstantDataWithVoltageAndCurrent(9.9f, 240.0f);
		 * SetPulseConstantDataWithVoltageAndCurrent(9.9f, 240.01f);
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * 
		 * SetPulseConstantDataWithVoltageAndCurrent(9.9f, 479.99f);
		 * SetPulseConstantDataWithVoltageAndCurrent(9.9f, 480.0f);
		 * SetPulseConstantDataWithVoltageAndCurrent(9.9f, 480.01f);
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 */

		/*
		 * SetPulseConstantDataWithVoltageAndCurrent(10.0f, 59.0f);
		 * SetPulseConstantDataWithVoltageAndCurrent(10.0f, 60.0f);
		 * SetPulseConstantDataWithVoltageAndCurrent(10.0f, 61.0f);
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * 
		 * 
		 * SetPulseConstantDataWithVoltageAndCurrent(10.0f, 119.99f);
		 * SetPulseConstantDataWithVoltageAndCurrent(10.0f, 120.0f);
		 * SetPulseConstantDataWithVoltageAndCurrent(10.0f, 120.01f);
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * 
		 * 
		 * SetPulseConstantDataWithVoltageAndCurrent(10.0f, 239.99f);
		 * SetPulseConstantDataWithVoltageAndCurrent(10.0f, 240.0f);
		 * SetPulseConstantDataWithVoltageAndCurrent(10.0f, 240.01f);
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * 
		 * SetPulseConstantDataWithVoltageAndCurrent(10.0f, 479.99f);
		 * SetPulseConstantDataWithVoltageAndCurrent(10.0f, 480.0f);
		 * SetPulseConstantDataWithVoltageAndCurrent(10.0f, 480.01f);
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 */

		/*
		 * SetPulseConstantDataWithVoltageAndCurrent(10.01f, 59.0f);
		 * SetPulseConstantDataWithVoltageAndCurrent(10.01f, 60.0f);
		 * SetPulseConstantDataWithVoltageAndCurrent(10.01f, 61.0f);
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * 
		 * 
		 * SetPulseConstantDataWithVoltageAndCurrent(10.01f, 119.99f);
		 * SetPulseConstantDataWithVoltageAndCurrent(10.01f, 120.0f);
		 * SetPulseConstantDataWithVoltageAndCurrent(10.01f, 120.01f);
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * 
		 * 
		 * SetPulseConstantDataWithVoltageAndCurrent(10.01f, 239.99f);
		 * SetPulseConstantDataWithVoltageAndCurrent(10.01f, 240.0f);
		 * SetPulseConstantDataWithVoltageAndCurrent(10.01f, 240.01f);
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * 
		 * SetPulseConstantDataWithVoltageAndCurrent(10.01f, 479.99f);
		 * SetPulseConstantDataWithVoltageAndCurrent(10.01f, 480.0f);
		 * SetPulseConstantDataWithVoltageAndCurrent(10.01f, 480.01f);
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 * ApplicationLauncher.logger.debug(
		 * "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		 */

		SetPulseConstantDataWithVoltageAndCurrent(20.0f, 59.0f);
		SetPulseConstantDataWithVoltageAndCurrent(20.0f, 60.0f);
		SetPulseConstantDataWithVoltageAndCurrent(20.0f, 61.0f);
		ApplicationLauncher.logger.debug("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		ApplicationLauncher.logger.debug("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

		SetPulseConstantDataWithVoltageAndCurrent(20.0f, 119.99f);
		SetPulseConstantDataWithVoltageAndCurrent(20.0f, 120.0f);
		SetPulseConstantDataWithVoltageAndCurrent(20.0f, 120.01f);
		ApplicationLauncher.logger.debug("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		ApplicationLauncher.logger.debug("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

		SetPulseConstantDataWithVoltageAndCurrent(20.0f, 239.99f);
		SetPulseConstantDataWithVoltageAndCurrent(20.0f, 240.0f);
		SetPulseConstantDataWithVoltageAndCurrent(20.0f, 240.01f);
		ApplicationLauncher.logger.debug("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		ApplicationLauncher.logger.debug("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

		SetPulseConstantDataWithVoltageAndCurrent(20.0f, 479.99f);
		SetPulseConstantDataWithVoltageAndCurrent(20.0f, 480.0f);
		SetPulseConstantDataWithVoltageAndCurrent(20.0f, 480.01f);
		ApplicationLauncher.logger.debug("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		ApplicationLauncher.logger.debug("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

		/*
		 * SetPulseConstantDataWithVoltageAndCurrent(101.0f, 470.0f);
		 * 
		 * SetPulseConstantDataWithVoltageAndCurrent(59.0f, 230.0f);
		 * SetPulseConstantDataWithVoltageAndCurrent(20.0f, 240.0f);
		 */

		// SetPulseConstantDataWithVoltageAndCurrent(20.0f, 240.0f);
		// SetPulseConstantDataWithVoltageAndCurrent(20.0f, 240.0f);

	}

	public static void setRSSPulseRate(String pulseValue) {
		RSS_Pulse_Rate = pulseValue;
	}

	public static String getRSSPulseRate() {
		return RSS_Pulse_Rate;

	}

	public static void SetPulseConstantDataWithVoltageAndCurrent(float CurrentValue, float voltageValue) {

		ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: CurrentValue: " + CurrentValue);
		ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: voltageValue: " + voltageValue);
		/*
		 * setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_DEFAULT);//
		 * setting default value
		 * if(ProcalFeatureEnable.REF_STD_CONST_CALCULATE){
		 * 
		 * setRSSPulseRate(String.valueOf(calculateRSS_ConstantV42(getDutImpulsesPerUnit
		 * ())));
		 * ApplicationLauncher.logger.
		 * debug("SetPulseConstantDataWithCurrent: calculateRSS_ConstantV42: "
		 * +getRSSPulseRate());
		 * 
		 * return;
		 * }
		 */

		// if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
		// ApplicationLauncher.logger.debug("SetActiveReactivePulseConstant: Setting
		// Active Pulse Constant:"+DisplayDataObj.RSS_ActivePulseConstant);
		// DisplayDataObj.setRSSPulseRate(DisplayDataObj.RSS_ActivePulseConstant);
		// if(getDeployedEM_CT_Type().equals(ConstantApp.METER_CT_TYPE_LTCT)){
		// setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_DEFAULT);
		if (voltageValue > ConstantRefStdConfig.RSS_LTCT_VOLTAGE_THRESHOLD_IN_AMPS_LEVEL_2) {
			if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_1) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_1");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_1);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_2) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_2");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_2);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_3) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_3");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_3);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_4) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_4");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_4);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_5) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_5");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_5);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_6) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_6");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_6);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_7) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_7");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_7);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_8) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_8");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_8);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_9) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_9");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_9);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_10) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_10");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_10);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_11) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_11");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_11);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_12) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_12");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_12);

			} else {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_below level 12");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_BELOW_OR_EQUAL_LEVEL_12);
				// ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: Setting
				// HTCT Active Pulse Constant below 2 Amp: "+getRSSPulseRate());

			}
		} else if (voltageValue > ConstantRefStdConfig.RSS_LTCT_VOLTAGE_THRESHOLD_IN_AMPS_LEVEL_3) {
			if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_1) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_1");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_1);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_2) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_2");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_2);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_3) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_3");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_3);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_4) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_4");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_4);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_5) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_5");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_5);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_6) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_6");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_6);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_7) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_7");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_7);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_8) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_8");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_8);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_9) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_9");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_9);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_10) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_10");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_10);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_11) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_11");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_11);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_12) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_12");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_12);

			} else {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_below LEVEL_12");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_BELOW_OR_EQUAL_LEVEL_12);
				// ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: Setting
				// HTCT Active Pulse Constant below 2 Amp: "+getRSSPulseRate());

			}
		} else if (voltageValue > ConstantRefStdConfig.RSS_LTCT_VOLTAGE_THRESHOLD_IN_AMPS_LEVEL_4) {
			if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_1) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_1");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_1);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_2) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_2");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_2);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_3) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_3");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_3);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_4) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_4");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_4);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_5) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_5");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_5);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_6) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_6");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_6);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_7) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_7");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_7);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_8) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_8");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_8);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_9) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_9");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_9);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_10) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_10");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_10);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_11) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_11");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_11);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_12) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_12");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_12);

			} else {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_below_level 12");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_BELOW_OR_EQUAL_LEVEL_12);
				// ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: Setting
				// HTCT Active Pulse Constant below 2 Amp: "+getRSSPulseRate());

			}
		} else if (voltageValue <= ConstantRefStdConfig.RSS_LTCT_VOLTAGE_THRESHOLD_IN_AMPS_LEVEL_4) {
			if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_1) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_1");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_1);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_2) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_2");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_2);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_3) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_3");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_3);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_4) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_4");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_4);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_5) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_5");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_5);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_6) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_6");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_6);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_7) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_7");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_7);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_8) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_8");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_8);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_9) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_9");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_9);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_10) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_10");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_10);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_11) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_11");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_11);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_12) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_12");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_12);

			} else {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_below_LEVEL_12");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_BELOW_OR_EQUAL_LEVEL_12);
				// ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: Setting
				// HTCT Active Pulse Constant below 2 Amp: "+getRSSPulseRate());

			}
		}
		ApplicationLauncher.logger.debug(
				"SetPulseConstantDataWithVoltageAndCurrent: Setting LTCT Active Pulse Constant: " + getRSSPulseRate());
	}

	public static void AssertValidateDataForRSS_BNC_Constant() {
		// double InputData = Float.valueOf( ConstantConfig.RSS_ACTIVE_PULSE_CONSTANT);

		generateCommandforRefStdBNC_Constant("10000000", ConstantRefStdRadiant.CMD_REF_STD_BNC_CONSTANT_WH_SET);
		generateCommandforRefStdBNC_Constant("10000000", ConstantRefStdRadiant.CMD_REF_STD_BNC_CONSTANT_VARH_SET);
		// calculateRSS_Constant(3, 63.5f,4.0f,50000,1.905f);
		// calculateRSS_Constant(3, 119.38f,4.0f,50000,1.905f);
		// calculateRSS_Constant(3, 38.1f,4.0f,50000,1.905f);
		// calculateRSS_Constant(3, 63.5f,7.5f,50000,1.905f);

		/*
		 * calculateRSS_ConstantV2(63.5f,63.5f,63.5f,4.0f,4.0f,4.0f,1.0f,1.0f,1.0f,50000
		 * ,1.905f);
		 * calculateRSS_ConstantV2(63.5f,63.5f,63.5f,4.0f,4.0f,4.0f,0.5f,0.5f,0.5f,50000
		 * ,1.905f);
		 * calculateRSS_ConstantV2(63.5f,63.5f,63.5f,4.0f,4.0f,4.0f,-0.5f,-0.5f,-0.5f,
		 * 50000,1.905f);
		 * calculateRSS_ConstantV2(63.5f,63.5f,63.5f,4.0f,4.0f,4.0f,0.866f,0.866f,0.866f
		 * ,50000,1.905f);
		 * calculateRSS_ConstantV2(63.5f,63.5f,63.5f,4.0f,4.0f,4.0f,-0.866f,-0.866f,-0.
		 * 866f,50000,1.905f);
		 * calculateRSS_ConstantV3(63.5f,63.5f,63.5f,4.0f,4.0f,4.0f,0.0f,0.0f,0.0f,50000
		 * ,1.905f);
		 * calculateRSS_ConstantV3(63.5f,63.5f,63.5f,4.0f,4.0f,4.0f,60.0f,60.0f,60.0f,
		 * 50000,1.905f);
		 * calculateRSS_ConstantV3(63.5f,63.5f,63.5f,4.0f,4.0f,4.0f,-60.0f,-60.0f,-60.0f
		 * ,50000,1.905f);
		 * calculateRSS_ConstantV3(63.5f,63.5f,63.5f,4.0f,4.0f,4.0f,30.0f,30.0f,30.0f,
		 * 50000,1.905f);
		 * calculateRSS_ConstantV3(63.5f,63.5f,63.5f,4.0f,4.0f,4.0f,-30.0f,-30.0f,-30.0f
		 * ,50000,1.905f);
		 * calculateRSS_ConstantV3(63.5f,63.5f,63.5f,4.0f,4.0f,4.0f,-30.0f+180,-30.0f+
		 * 180,-30.0f+180,50000,1.905f);
		 * calculateRSS_ConstantV4(63.5f,63.5f,63.5f,4.0f,4.0f,4.0f,-30.0f+180,-30.0f+
		 * 180,-30.0f+180,50000,1.905f);
		 * calculateRSS_ConstantV3(63.5f,63.5f,63.5f,4.0f,4.0f,4.0f,30.0f+180,30.0f+180,
		 * 30.0f+180,50000,1.905f);
		 * calculateRSS_ConstantV4(63.5f,63.5f,63.5f,4.0f,4.0f,4.0f,30.0f+180,30.0f+180,
		 * 30.0f+180,50000,1.905f);
		 * 
		 * calculateRSS_ConstantV3(63.5f,63.5f,63.5f,4.0f,4.0f,4.0f,-60.0f+180,-60.0f+
		 * 180,-60.0f+180,50000,1.905f);
		 * calculateRSS_ConstantV4(63.5f,63.5f,63.5f,4.0f,4.0f,4.0f,-60.0f+180,-60.0f+
		 * 180,-60.0f+180,50000,1.905f);
		 * calculateRSS_ConstantV3(63.5f,63.5f,63.5f,4.0f,4.0f,4.0f,60.0f+180,60.0f+180,
		 * 60.0f+180,50000,1.905f);
		 * calculateRSS_ConstantV4(63.5f,63.5f,63.5f,4.0f,4.0f,4.0f,60.0f+180,60.0f+180,
		 * 60.0f+180,50000,1.905f);
		 */

	}

	static long calculateRSS_Constant(int NoOfPhases, float Voltage, float Current,
			float MUT_ConstInImpulsesPerKiloWattHour, float maxFreqInMegaHz) {

		float power = Voltage * Current * NoOfPhases;
		double RssConstantInWattHour = (maxFreqInMegaHz * 1000 * MUT_ConstInImpulsesPerKiloWattHour) / power;
		long OutputConstantInWattHour = (long) RssConstantInWattHour;
		System.out.println("Double RssConstantInWattHour : " + RssConstantInWattHour);
		System.out.println("Long OutputConstantInWattHour : " + OutputConstantInWattHour);
		return OutputConstantInWattHour;
	}

	static long calculateRSS_ConstantV2(float Volt1, float Volt2, float Volt3, float Current1, float Current2,
			float Current3, float PF1, float PF2, float PF3, float MUT_ConstInImpulsesPerKiloWattHour,
			float maxFreqInMegaHz) {

		float power = (Volt1 * Current1 * PF1) + (Volt2 * Current2 * PF2) + (Volt3 * Current3 * PF3);
		double RssConstantInWattHour = (maxFreqInMegaHz * 1000 * MUT_ConstInImpulsesPerKiloWattHour) / power;
		long OutputConstantInWattHour = (long) RssConstantInWattHour;
		// System.out.println("Double RssConstantInWattHour : " +
		// RssConstantInWattHour);
		System.out.println("Long OutputConstantInWattHour : " + OutputConstantInWattHour);
		return OutputConstantInWattHour;
	}

	static long calculateRSS_ConstantV3(float Volt1, float Volt2, float Volt3, float Current1, float Current2,
			float Current3, float PF1, float PF2, float PF3, float MUT_ConstInImpulsesPerKiloWattHour,
			float maxFreqInMegaHz) {

		// float power = (Volt1 * Current1 * PF1)+(Volt2 * Current2 * PF2)+ (Volt3 *
		// Current3 * PF3);
		System.out.println("PF1 : " + Math.cos(Math.toRadians(PF1)));
		float power = (Volt1 * Current1 * (float) Math.cos(Math.toRadians(PF1)))
				+ (Volt2 * Current2 * (float) Math.cos(Math.toRadians(PF2)))
				+ (Volt3 * Current3 * (float) Math.cos(Math.toRadians(PF3)));

		double RssConstantInWattHour = (maxFreqInMegaHz * 1000 * MUT_ConstInImpulsesPerKiloWattHour) / power;
		long OutputConstantInWattHour = (long) RssConstantInWattHour;
		// System.out.println("Double RssConstantInWattHour : " +
		// RssConstantInWattHour);
		System.out.println("Long OutputConstantInWattHour : " + Math.abs(OutputConstantInWattHour));
		return Math.abs(OutputConstantInWattHour);
	}

	static long calculateRSS_ConstantV4(float Volt1, float Volt2, float Volt3, float Current1, float Current2,
			float Current3, float PF1, float PF2, float PF3, float MUT_ConstInImpulsesPerKiloWattHour,
			float maxFreqInMegaHz) {

		// float power = (Volt1 * Current1 * PF1)+(Volt2 * Current2 * PF2)+ (Volt3 *
		// Current3 * PF3);
		System.out.println("PF1 : " + Math.cos(Math.toRadians(PF1)));
		float power = (Volt1 * Current1 * (float) Math.cos(Math.toRadians(PF1)))
				+ (Volt2 * Current2 * (float) Math.cos(Math.toRadians(PF2)))
				+ (Volt3 * Current3 * (float) Math.cos(Math.toRadians(PF3)));

		double RssConstantInWattHour = (maxFreqInMegaHz * 1000 * MUT_ConstInImpulsesPerKiloWattHour) / power;
		long OutputConstantInWattHour = (long) RssConstantInWattHour;
		// System.out.println("Double RssConstantInWattHour : " +
		// RssConstantInWattHour);
		System.out.println("Long OutputConstantInWattHour : " + Math.abs(OutputConstantInWattHour));
		return Math.abs(OutputConstantInWattHour);
	}

	public static String generateCommandforRefStdBNC_Constant(String bncConstant, String bncMetricIndexCommand) {
		double InputData = Float.valueOf(bncConstant);
		double RSS_InputSetValue = (1 / (InputData)) * 1000;
		ApplicationLauncher.logger.info("InputData:" + InputData);
		// ApplicationLauncher.logger.info("RSS_InputSetValue:"+RSS_InputSetValue);
		// String OutputData = TMS_FloatConversion.FloatToTMS320_Hex(InputData);
		// ApplicationLauncher.logger.info("OutputData:"+OutputData);
		String RSS_InputSetValueString = String.format("%.09f", RSS_InputSetValue);
		ApplicationLauncher.logger.info("RSS_InputSetValueString:" + RSS_InputSetValueString);
		InputData = Float.valueOf(RSS_InputSetValueString);
		String FloatHexData = TMS_FloatConversion.FloatToTMS320_Hex(InputData);
		ApplicationLauncher.logger.info("FloatHexData:" + FloatHexData);
		String RSS_SetCommandWithOutCheckSum = bncMetricIndexCommand + FloatHexData;
		String checkSum = generateCheckSum(RSS_SetCommandWithOutCheckSum);
		ApplicationLauncher.logger.info("checkSum:" + checkSum);
		String OutputData = RSS_SetCommandWithOutCheckSum + checkSum;
		ApplicationLauncher.logger.info("OutputData:" + OutputData);

		/*
		 * RSS_SetCommandWithOutCheckSum = "A632000700000100000000";
		 * checkSum = generateCheckSum(RSS_SetCommandWithOutCheckSum);
		 * ApplicationLauncher.logger.info("checkSum:"+checkSum);
		 * OutputData = RSS_SetCommandWithOutCheckSum+checkSum;
		 * ApplicationLauncher.logger.info("OutputData:"+OutputData);
		 */
		return OutputData;
	}

	public static String generateCheckSum(String InputHexString) {

		String hex_value = new String();
		// 'hex_value' will be used to store various hex values as a string
		int x, i, checksum = 0;
		// 'x' will be used for general purpose storage of integer values
		// 'i' is used for loops
		// 'checksum' will store the final checksum
		for (i = 0; i < InputHexString.length() - 2; i = i + 2) {
			// x = (int) (InputHexString.charAt(i));
			// hex_value = (InputHexString.charAt(i));//Integer.toHexString(x);
			// x = (int) (InputHexString.charAt(i + 1));
			// hex_value = hex_value + Integer.toHexString(x);
			hex_value = Character.toString(InputHexString.charAt(i)) + Character.toString(InputHexString.charAt(i + 1));
			// Extract two characters and get their hexadecimal ASCII values
			ApplicationLauncher.logger
					.info("generateCheckSum: " + InputHexString.charAt(i) + "" + InputHexString.charAt(i + 1) + " : "
							+ hex_value);
			x = Integer.parseInt(hex_value, 16);
			System.out.println("Int Value:" + x);
			// Convert the hex_value into int and store it
			checksum += x;
			// Add 'x' into 'checksum'
		}
		if (InputHexString.length() % 2 == 0) {
			// If number of characters is even, then repeat above loop's steps
			// one more time.
			// x = (int) (InputHexString.charAt(i));
			// hex_value = Integer.toHexString(x);
			// x = (int) (InputHexString.charAt(i + 1));
			// hex_value = hex_value + Integer.toHexString(x);
			hex_value = Character.toString(InputHexString.charAt(i)) + Character.toString(InputHexString.charAt(i + 1));

			ApplicationLauncher.logger
					.info("generateCheckSum: " + InputHexString.charAt(i) + "" + InputHexString.charAt(i + 1) + " : "
							+ hex_value);
			x = Integer.parseInt(hex_value, 16);
		} else {
			// If number of characters is odd, last 2 digits will be 00.
			x = (int) (InputHexString.charAt(i));
			hex_value = "00" + Character.toString(InputHexString.charAt(i));// Integer.toHexString(x);
			x = Integer.parseInt(hex_value, 16);
			ApplicationLauncher.logger.info("generateCheckSum: " + InputHexString.charAt(i) + " : " + hex_value);
		}
		checksum += x;
		// Add the generated value of 'x' from the if-else case into 'checksum'
		hex_value = Integer.toHexString(checksum);
		hex_value = StringUtils.leftPad(hex_value.toUpperCase(), 4, '0');
		// hex_value= String.format("%04", hex_value);
		// Convert into hexadecimal string
		/*
		 * if (hex_value.length() > 4)
		 * {
		 * // If a carry is generated, then we wrap the carry
		 * int carry = Integer.parseInt(("" + hex_value.charAt(0)), 16);
		 * // Get the value of the carry bit
		 * hex_value = hex_value.substring(1, 5);
		 * // Remove it from the string
		 * checksum = Integer.parseInt(hex_value, 16);
		 * // Convert it into an int
		 * checksum += carry;
		 * // Add it to the checksum
		 * }
		 */
		// checksum = generateComplement(checksum);
		// Get the complement
		return hex_value;

	}

	public void AssertValidateForHeaderForRefStd() {
		String InputData = "47B60D00540E1D2EA40D2074000108DA6DFA54B7C3F4B8D64C0030D01BEE7DB067ECD46204EF2EEA36EAF459C50615519D0728804BFC84D3957F7FFFFF80000000FF09C5B6EA4AD744EE086D00E7443A27EE6C8DBBF0497B6829";
		String OutputData = AssertValidateForHeaderAndStripFirstByteIfInvalid(InputData);
		ApplicationLauncher.logger.info("OutputData:" + OutputData);
		InputData = "7DC60D00540E1D2EA40D1F5C00011F182BFB01E010F4DA0D0C010E289DEF228468EDCB6D13F0347E3CEAD934840615B6E1072D69E8FDFFF1537F7FFFFF0779FA970744058C800000008000000080000000EC44449D8000000020";
		OutputData = AssertValidateForHeaderAndStripFirstByteIfInvalid(InputData);
		ApplicationLauncher.logger.info("OutputData:" + OutputData);
		InputData = "B5D60D00540E1D2EA40D1EDC00013E06A2FEEF9FF5FB5C8D2407686B15013E06A00929AA18092CB65F020990A3053E0236FEC62DEBFF7B7B727F7FFFFF020A85380767ABE90928DBA5092C283F01C95F93FFE3A562017053FE23";
		OutputData = AssertValidateForHeaderAndStripFirstByteIfInvalid(InputData);
		ApplicationLauncher.logger.info("OutputData:" + OutputData);

		InputData = "B60D00540E1D2EA40D208800017FFBC0F2A964D3F688679407701161017FFBC009700D5109700D59FD073D6E053BFFE5F9FEE222FF7FFFF77F7FFFFF80000000084FE87A0A3406320A4FE4FB094FF5F6FC6F0211094FEE102665";
		OutputData = AssertValidateForHeaderAndStripFirstByteIfInvalid(InputData);
		ApplicationLauncher.logger.info("OutputData:" + OutputData);
		InputData = "C60D00540E1D2EA40D208800017FFA04F6133B9EF4E053A107701D43EF2C77CEF23285B7F721C47EF080DBAD053C0036041D4B0EFB0D41E27F7FFFFF068FF488084FD64A800000008000000080000000F080E5B3800000002292";
		OutputData = AssertValidateForHeaderAndStripFirstByteIfInvalid(InputData);
		ApplicationLauncher.logger.info("OutputData:" + OutputData);
		InputData = "D60D00540E1D2EA40D208400017FF8ECF8E50340F6D02B1607700AE6EE50F83AF01563E7F643F221F253285B053BFFE806E00F76F9432D087F7FFFFF066FFE2B084FD830F26EC3A2F729A9C1F30E1E94F252F9AEF30E080B29A7";
		OutputData = AssertValidateForHeaderAndStripFirstByteIfInvalid(InputData);
		ApplicationLauncher.logger.info("OutputData:" + OutputData);
	}

	public String AssertValidateForHeaderAndStripFirstByteIfInvalid(String InputData) {

		if (InputData.substring(2, 18).equals(ConstantRefStdRadiant.REF_STD_R_PHASE_RESPONSE_HEADER)) {
			ApplicationLauncher.logger
					.debug("ValidateForHeaderAndStripFirstByteIfInvalid-B6: First Byte Data Stripped");
			return InputData.substring(2, 180);
		} else if (InputData.substring(2, 18).equals(ConstantRefStdRadiant.REF_STD_Y_PHASE_RESPONSE_HEADER)) {
			ApplicationLauncher.logger
					.debug("ValidateForHeaderAndStripFirstByteIfInvalid-C6: First Byte Data Stripped");
			return InputData.substring(2, 180);
		} else if (InputData.substring(2, 18).equals(ConstantRefStdRadiant.REF_STD_B_PHASE_RESPONSE_HEADER)) {
			ApplicationLauncher.logger
					.debug("ValidateForHeaderAndStripFirstByteIfInvalid-D6: First Byte Data Stripped");
			return InputData.substring(2, 180);
		} else {
			return InputData;
		}

	}

	public void AssertErrorInputData() {

		String E_Max1 = "1.0";
		String E_Min1 = "1.0";

		String E_Max2 = "+2.00";
		String E_Min2 = "+1.00";

		String E_Max3 = "-2.0";
		String E_Min3 = "-3.000";

		String E_Max4 = "+4.00";
		String E_Min4 = "-5.00";

		String E_Max5 = "+6";
		String E_Min5 = "-7";

		String E_Max6 = "8";
		String E_Min6 = "9";

		String E_Max7 = "10";
		String E_Min7 = "-11";

		String E_Max8 = "+12.0";
		String E_Min8 = "-12.0";

		String E_Max9 = "+1j";
		String E_Min9 = "-1j";

		String E_Max10 = "2j";
		String E_Min10 = "2j.0";

		String E_Max11 = "3.k";
		String E_Min11 = "a";

		String E_Max12 = "+b";
		String E_Min12 = "-c";

		String E_Max13 = "+.52";
		String E_Min13 = "-.12";

		String E_Max14 = ".631";
		String E_Min14 = "-.122";

		String E_Max15 = "+.731";
		String E_Min15 = "-.3221";

		String E_Max16 = ".8";
		String E_Min16 = "-.4";

		String E_Max17 = "+.9";
		String E_Min17 = "-.5";

		System.out.println("E_Max1 input:" + E_Max1);
		E_Max1 = GuiUtils.FormatErrorInput(E_Max1);
		System.out.println("E_Max1 output:" + E_Max1);
		System.out.println("E_Min1 input:" + E_Min1);
		E_Min1 = GuiUtils.FormatErrorInput(E_Min1);
		System.out.println("E_Min1 output:" + E_Min1);
		System.out.println("============================");
		System.out.println("E_Max2 input:" + E_Max2);
		E_Max2 = GuiUtils.FormatErrorInput(E_Max2);
		System.out.println("E_Max2 output:" + E_Max2);
		System.out.println("E_Min2 input:" + E_Min2);
		E_Min2 = GuiUtils.FormatErrorInput(E_Min2);
		System.out.println("E_Min2 output:" + E_Min2);
		System.out.println("============================");
		System.out.println("E_Max3 input:" + E_Max3);
		E_Max3 = GuiUtils.FormatErrorInput(E_Max3);
		System.out.println("E_Max3 output:" + E_Max3);
		System.out.println("E_Min3 input:" + E_Min3);
		E_Min3 = GuiUtils.FormatErrorInput(E_Min3);
		System.out.println("E_Min3 output:" + E_Min3);
		System.out.println("============================");
		System.out.println("E_Max4 input:" + E_Max4);
		E_Max4 = GuiUtils.FormatErrorInput(E_Max4);
		System.out.println("E_Max4 output:" + E_Max4);
		System.out.println("E_Min4 input:" + E_Min4);
		E_Min4 = GuiUtils.FormatErrorInput(E_Min4);
		System.out.println("E_Min4 output:" + E_Min4);
		System.out.println("============================");
		System.out.println("E_Max5 input:" + E_Max5);
		E_Max5 = GuiUtils.FormatErrorInput(E_Max5);
		System.out.println("E_Max5 output:" + E_Max5);
		System.out.println("E_Min5 input:" + E_Min5);
		E_Min5 = GuiUtils.FormatErrorInput(E_Min5);
		System.out.println("E_Min5 output:" + E_Min5);
		System.out.println("============================");
		System.out.println("E_Max6 input:" + E_Max6);
		E_Max6 = GuiUtils.FormatErrorInput(E_Max6);
		System.out.println("E_Max6 output:" + E_Max6);
		System.out.println("E_Min6 input:" + E_Min6);
		E_Min6 = GuiUtils.FormatErrorInput(E_Min6);
		System.out.println("E_Min6 output:" + E_Min6);
		System.out.println("============================");

		System.out.println("E_Max13 input:" + E_Max13);
		E_Max13 = GuiUtils.FormatErrorInput(E_Max13);
		System.out.println("E_Max13 output:" + E_Max13);
		System.out.println("E_Min13 input:" + E_Min13);
		E_Min13 = GuiUtils.FormatErrorInput(E_Min13);
		System.out.println("E_Min13 output:" + E_Min13);
		System.out.println("============================");
		System.out.println("E_Max14 input:" + E_Max14);
		E_Max14 = GuiUtils.FormatErrorInput(E_Max14);
		System.out.println("E_Max14 output:" + E_Max14);
		System.out.println("E_Min14 input:" + E_Min14);
		E_Min14 = GuiUtils.FormatErrorInput(E_Min14);
		System.out.println("E_Min14 output:" + E_Min14);
		System.out.println("============================");
		System.out.println("E_Max15 input:" + E_Max15);
		E_Max15 = GuiUtils.FormatErrorInput(E_Max15);
		System.out.println("E_Max15 output:" + E_Max15);
		System.out.println("E_Min15 input:" + E_Min15);
		E_Min15 = GuiUtils.FormatErrorInput(E_Min15);
		System.out.println("E_Min15 output:" + E_Min15);
		System.out.println("============================");
		System.out.println("E_Max16 input:" + E_Max16);
		E_Max16 = GuiUtils.FormatErrorInput(E_Max16);
		System.out.println("E_Max16 output:" + E_Max16);
		System.out.println("E_Min16 input:" + E_Min16);
		E_Min16 = GuiUtils.FormatErrorInput(E_Min16);
		System.out.println("E_Min16 output:" + E_Min16);
		System.out.println("============================");
		System.out.println("E_Max17 input:" + E_Max17);
		E_Max17 = GuiUtils.FormatErrorInput(E_Max17);
		System.out.println("E_Max17 output:" + E_Max17);
		System.out.println("E_Min17 input:" + E_Min17);
		E_Min17 = GuiUtils.FormatErrorInput(E_Min17);
		System.out.println("E_Min15 output:" + E_Min17);

		System.out.println("============================");
		System.out.println("=======Negative cases=======");
		System.out.println("============================");
		System.out.println("============================");
		System.out.println("E_Max7 input:" + E_Max7);
		E_Max7 = GuiUtils.FormatErrorInput(E_Max7);
		System.out.println("E_Max7 output:" + E_Max7);
		System.out.println("E_Min7 input:" + E_Min7);
		E_Min7 = GuiUtils.FormatErrorInput(E_Min7);
		System.out.println("E_Min7 output:" + E_Min7);
		System.out.println("============================");
		System.out.println("E_Max8 input:" + E_Max8);
		E_Max8 = GuiUtils.FormatErrorInput(E_Max8);
		System.out.println("E_Max8 output:" + E_Max8);
		System.out.println("E_Min8 input:" + E_Min8);
		E_Min8 = GuiUtils.FormatErrorInput(E_Min8);
		System.out.println("E_Min8 output:" + E_Min8);
		System.out.println("============================");
		System.out.println("E_Max9 input:" + E_Max9);
		E_Max9 = GuiUtils.FormatErrorInput(E_Max9);
		System.out.println("E_Max9 output:" + E_Max9);
		System.out.println("E_Min9 input:" + E_Min9);
		E_Min9 = GuiUtils.FormatErrorInput(E_Min9);
		System.out.println("E_Min9 output:" + E_Min9);
		System.out.println("============================");
		System.out.println("E_Max10 input:" + E_Max10);
		E_Max10 = GuiUtils.FormatErrorInput(E_Max10);
		System.out.println("E_Max10 output:" + E_Max10);
		System.out.println("E_Min10 input:" + E_Min10);
		E_Min10 = GuiUtils.FormatErrorInput(E_Min10);
		System.out.println("E_Min10 output:" + E_Min10);
		System.out.println("============================");
		System.out.println("E_Max11 input:" + E_Max11);
		E_Max11 = GuiUtils.FormatErrorInput(E_Max11);
		System.out.println("E_Max11 output:" + E_Max11);
		System.out.println("E_Min11 input:" + E_Min11);
		E_Min11 = GuiUtils.FormatErrorInput(E_Min11);
		System.out.println("E_Min11 output:" + E_Min11);
		System.out.println("============================");
		System.out.println("E_Max12 input:" + E_Max12);
		E_Max12 = GuiUtils.FormatErrorInput(E_Max12);
		System.out.println("E_Max12 output:" + E_Max12);
		System.out.println("E_Min12 input:" + E_Min12);
		E_Min12 = GuiUtils.FormatErrorInput(E_Min12);
		System.out.println("E_Min12 output:" + E_Min12);
		System.out.println("============================");
	}

	public static void AssertLagLead() {

		ArrayList<String> PF_angle = new ArrayList<String>();

		PF_angle.add("1.0");
		PF_angle.add("0.5L");
		PF_angle.add("0.25L");
		PF_angle.add("0.866L");
		PF_angle.add("0.5C");
		PF_angle.add("0.25C");
		PF_angle.add("0.866C");
		PF_angle.add("R:1.0");
		PF_angle.add("R:0.5L");
		PF_angle.add("R:0.25L");
		PF_angle.add("R:0.866L");
		PF_angle.add("R:0.5C");
		PF_angle.add("R:0.25C");
		PF_angle.add("R:0.866C");

		for (int i = 0; i < PF_angle.size(); i++) {
			ApplicationLauncher.logger.info("PF_angle.get(i): " + PF_angle.get(i));
			CalculateLagLeadAngle(PF_angle.get(i), ConstantApp.METERTYPE_ACTIVE, ConstantPowerSourceMte.IMPORT_MODE);
		}
		ApplicationLauncher.logger.info("*********************************************************");
		ApplicationLauncher.logger.info("*********************************************************");
		for (int i = 0; i < PF_angle.size(); i++) {
			ApplicationLauncher.logger.info("PF_angle.get(i): " + PF_angle.get(i));
			CalculateLagLeadAngle(PF_angle.get(i), ConstantApp.METERTYPE_REACTIVE, ConstantPowerSourceMte.IMPORT_MODE);
		}
		ApplicationLauncher.logger.info("*********************************************************");
		ApplicationLauncher.logger.info("*********************************************************");
		for (int i = 0; i < PF_angle.size(); i++) {
			ApplicationLauncher.logger.info("PF_angle.get(i): " + PF_angle.get(i));
			CalculateLagLeadAngle(PF_angle.get(i), ConstantApp.METERTYPE_ACTIVE, ConstantPowerSourceMte.EXPORT_MODE);
		}
		ApplicationLauncher.logger.info("*********************************************************");
		ApplicationLauncher.logger.info("*********************************************************");
		for (int i = 0; i < PF_angle.size(); i++) {
			ApplicationLauncher.logger.info("PF_angle.get(i): " + PF_angle.get(i));
			CalculateLagLeadAngle(PF_angle.get(i), ConstantApp.METERTYPE_REACTIVE, ConstantPowerSourceMte.EXPORT_MODE);
		}
		ApplicationLauncher.logger.info("*********************************************************");
		ApplicationLauncher.logger.info("*********************************************************");
	}

	public static void CalculateLagLeadAngle(String input, String ActiveReactive, String EnergyFlowMode) {
		ApplicationLauncher.logger.info("CalculateLagLeadAngle : Entry");
		String lag_lead = input.substring(input.length() - 1);
		ApplicationLauncher.logger.info("input: " + input);
		ApplicationLauncher.logger.info("get_EM_Model_type: " + ActiveReactive);
		ApplicationLauncher.logger.info("EnergyFlowMode: " + EnergyFlowMode);
		double phasedegree = 0;
		float lag_lead_value = 0;
		String phase = "";

		if (ActiveReactive.contains(ConstantApp.METERTYPE_ACTIVE)) {
			if (EnergyFlowMode.equals(ConstantPowerSourceMte.IMPORT_MODE)) {
				ConstantPowerSourceMte.POWER_SRC_COS_ACTIVE_UPF_ANGLE = ConstantPowerSourceMte.POWER_SRC_3P4W_COS_IMPORT_ACTIVE_UPF_ANGLE;
			} else if (EnergyFlowMode.equals(ConstantPowerSourceMte.EXPORT_MODE)) {
				ConstantPowerSourceMte.POWER_SRC_COS_ACTIVE_UPF_ANGLE = ConstantPowerSourceMte.POWER_SRC_3P4W_COS_EXPORT_ACTIVE_UPF_ANGLE;
			}
		} else if (ActiveReactive.contains(ConstantApp.METERTYPE_REACTIVE)) {
			if (EnergyFlowMode.equals(ConstantPowerSourceMte.IMPORT_MODE)) {
				ConstantPowerSourceMte.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE = ConstantPowerSourceMte.POWER_SRC_3P4W_SINE_IMPORT_REACTIVE_ZPF_ANGLE;
			} else if (EnergyFlowMode.equals(ConstantPowerSourceMte.EXPORT_MODE)) {
				ConstantPowerSourceMte.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE = ConstantPowerSourceMte.POWER_SRC_3P4W_SINE_EXPORT_REACTIVE_ZPF_ANGLE;
			}
		}

		// int ReactiveImportExportAngle = 0;

		/*
		 * if(EnergyFlowMode.equals( ConstantPowerSource.IMPORT_MODE)){
		 * ReactiveImportExportAngle = 0;//Gopi- to be added on prod
		 * }else if(EnergyFlowMode.equals(ConstantPowerSource.EXPORT_MODE)){
		 * ReactiveImportExportAngle = -180;//Gopi- to be added on prod
		 * }
		 */
		ArrayList<String> All_phases = new ArrayList<String>();
		if (lag_lead.equals(ConstantApp.PF_LAG)) {
			try {
				lag_lead_value = Float.parseFloat(input.substring(0, input.length() - 1));
				ApplicationLauncher.logger.info("CalculateLagLeadAngle : Lag-Path1");
				if (ActiveReactive.contains(ConstantApp.METERTYPE_ACTIVE)) {
					ApplicationLauncher.logger.info("CalculateLagLeadAngle : Lag-Path1-Active");
					phasedegree = ConstantPowerSourceMte.POWER_SRC_COS_ACTIVE_UPF_ANGLE
							+ (Math.acos(lag_lead_value) * (180 / Math.PI));
					// phasedegree =
					// ImportExportSignAngle*(Math.acos(ImportExportSignAngle*lag_lead_value) *
					// (180/Math.PI));
					/*
					 * if(getEnergyFlowMode().equals(ConstantPowerSource.EXPORT_MODE)){
					 * phasedegree = -180+(Math.acos(lag_lead_value) * (180/Math.PI));
					 * }
					 */
				} else if (ActiveReactive.contains(ConstantApp.METERTYPE_REACTIVE)) {
					ApplicationLauncher.logger.info("CalculateLagLeadAngle : Lag-Path1-Reactive");
					// phasedegree = ReactiveImportExportSignAngle*(Math.asin(lag_lead_value) *
					// (180/Math.PI));
					phasedegree = ConstantPowerSourceMte.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE
							- (Math.acos(lag_lead_value) * (180 / Math.PI));
					// phasedegree = -phasedegree;
				} else {
					ApplicationLauncher.logger.info("Mismatch: Lead Lag ");
					phasedegree = Math.acos(lag_lead_value) * (180 / Math.PI);
				}

				// ApplicationLauncher.logger.info("CalculateLagLeadAngle :
				// phasedegree:"+phasedegree);

			} catch (Exception e1) {
				// e1.printStackTrace();
				// ApplicationLauncher.logger.info("calculatelag_lead :Exception1:"+
				// e1.getMessage());
				phase = input.substring(0, 1);
				ApplicationLauncher.logger.info("CalculateLagLeadAngle : Lag-Path2");
				lag_lead_value = Float.parseFloat(input.substring(2, input.length() - 1));
				if (ActiveReactive.contains(ConstantApp.METERTYPE_ACTIVE)) {
					ApplicationLauncher.logger.info("CalculateLagLeadAngle : Lag-Path2-Active");
					phasedegree = ConstantPowerSourceMte.POWER_SRC_COS_ACTIVE_UPF_ANGLE
							+ (Math.acos(lag_lead_value) * (180 / Math.PI));
					/*
					 * if(getEnergyFlowMode().equals(ConstantPowerSource.EXPORT_MODE)){
					 * phasedegree = -180+(Math.acos(lag_lead_value) * (180/Math.PI));
					 * }
					 */
				} else if (ActiveReactive.contains(ConstantApp.METERTYPE_REACTIVE)) {
					ApplicationLauncher.logger.info("CalculateLagLeadAngle : Lag-Path2-Reactive");
					// phasedegree = ReactiveImportExportSignAngle*(Math.asin(lag_lead_value) *
					// (180/Math.PI));
					phasedegree = ConstantPowerSourceMte.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE
							- (Math.acos(lag_lead_value) * (180 / Math.PI));
					// phasedegree = -phasedegree;

				} else {
					ApplicationLauncher.logger.info("Mismatch: Lead Lag ");
					phasedegree = Math.acos(lag_lead_value) * (180 / Math.PI);
				}

			}

			ApplicationLauncher.logger.info("phasedegree: Lag:" + String.format("%.2f", phasedegree));
			// phasedegree = Math.round(phasedegree);
			ApplicationLauncher.logger.info("phasedegree: Lag1:" + String.format("%.2f", phasedegree));

		} else if (lag_lead.equals(ConstantApp.PF_LEAD)) {
			try {
				lag_lead_value = Float.parseFloat(input.substring(0, input.length() - 1));
				ApplicationLauncher.logger.info("CalculateLagLeadAngle : Lead-Path1");
				if (ActiveReactive.contains(ConstantApp.METERTYPE_ACTIVE)) {
					ApplicationLauncher.logger.info("CalculateLagLeadAngle : Lead-Path1-Active");
					phasedegree = ConstantPowerSourceMte.POWER_SRC_COS_ACTIVE_UPF_ANGLE
							- (Math.acos(lag_lead_value) * (180 / Math.PI));
					// phasedegree = -(phasedegree);//Gopi- to be added on prod
					/*
					 * if(getEnergyFlowMode().equals(ConstantPowerSource.EXPORT_MODE)){
					 * phasedegree = -180+(Math.acos(lag_lead_value) * (180/Math.PI));
					 * }
					 */
				} else if (ActiveReactive.contains(ConstantApp.METERTYPE_REACTIVE)) {
					ApplicationLauncher.logger.info("CalculateLagLeadAngle : Lead-Path1-Reactive");
					/*
					 * phasedegree =
					 * ConstantPowerSource.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE+(Math.asin(
					 * lag_lead_value) * (180/Math.PI));
					 * ApplicationLauncher.logger.info("Lead-Path1-Reactive:phasedegree0 :" +
					 * String.format("%.2f", phasedegree));
					 * phasedegree = ReactiveImportExportSignAngle*(Math.asin(lag_lead_value) *
					 * (180/Math.PI));
					 * ApplicationLauncher.logger.info("Lead-Path1-Reactive:phasedegree1 :" +
					 * String.format("%.2f", phasedegree));
					 * ApplicationLauncher.logger.
					 * info("CalculateLagLeadAngle : (Math.asin(lag_lead_value) * (180/Math.PI)):"+(
					 * Math.asin(lag_lead_value) ));
					 */// ApplicationLauncher.logger.info("CalculateLagLeadAngle
						// :"+(ConstantPowerSource.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE+(Math.acos(lag_lead_value)
						// * (180/Math.PI))));
						// ApplicationLauncher.logger.info("CalculateLagLeadAngle
						// :"+(-ConstantPowerSource.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE+(Math.acos(lag_lead_value)
						// * (180/Math.PI))));
						// phasedegree = -(Math.asin(lag_lead_value) * (180/Math.PI));

					phasedegree = ConstantPowerSourceMte.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE
							+ (Math.acos(lag_lead_value) * (180 / Math.PI));
					ApplicationLauncher.logger
							.info("Lead-Path1-Reactive:phasedegree2 :" + String.format("%.2f", phasedegree));

					// phasedegree = -(phasedegree);
				} else {
					ApplicationLauncher.logger.info("Mismatch: Lead Lag ");
					phasedegree = Math.acos(lag_lead_value) * (180 / Math.PI);
				}
				// phasedegree = -(phasedegree); //Gopi- to be commented on prod

				All_phases.add("All");
				All_phases.add(String.format("%.2f", phasedegree));

			} catch (Exception e2) {
				// e2.printStackTrace();
				// ApplicationLauncher.logger.info("calculatelag_lead :Exception2:"+
				// e2.getMessage());
				phase = input.substring(0, 1);
				ApplicationLauncher.logger.info("CalculateLagLeadAngle : Lead-Path2");
				lag_lead_value = Float.parseFloat(input.substring(2, input.length() - 1));
				if (ActiveReactive.contains(ConstantApp.METERTYPE_ACTIVE)) {
					ApplicationLauncher.logger.info("CalculateLagLeadAngle : Lead-Path2-Active");
					phasedegree = ConstantPowerSourceMte.POWER_SRC_COS_ACTIVE_UPF_ANGLE
							- (Math.acos(lag_lead_value) * (180 / Math.PI));
					// phasedegree = - phasedegree;//Gopi- to be added on prod
					/*
					 * if(getEnergyFlowMode().equals(ConstantPowerSource.EXPORT_MODE)){
					 * phasedegree = -180+(Math.acos(lag_lead_value) * (180/Math.PI));
					 * }
					 */
				} else if (ActiveReactive.contains(ConstantApp.METERTYPE_REACTIVE)) {
					ApplicationLauncher.logger.info("CalculateLagLeadAngle : Lead-Path2-Reactive");
					// phasedegree = ReactiveImportExportSignAngle*(Math.asin(lag_lead_value) *
					// (180/Math.PI));
					phasedegree = ConstantPowerSourceMte.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE
							+ (Math.acos(lag_lead_value) * (180 / Math.PI));

				} else {
					ApplicationLauncher.logger.info("Mismatch: Lead Lag ");
					phasedegree = Math.acos(lag_lead_value) * (180 / Math.PI);
				}

				// phasedegree = - phasedegree;//Gopi- to be commented on prod

			}

			ApplicationLauncher.logger.info("phasedegree: Lead:" + String.format("%.2f", phasedegree));
			// phasedegree = Math.round(phasedegree);
			ApplicationLauncher.logger.info("phasedegree: Lead1:" + String.format("%.2f", phasedegree));
		} else {
			try {
				lag_lead_value = Float.parseFloat(input);
				phasedegree = 0.0;
				ApplicationLauncher.logger.info("CalculateLagLeadAngle : Unity-Path1");
				if (ActiveReactive.contains(ConstantApp.METERTYPE_ACTIVE)) {
					// phasedegree = 0.0;
					ApplicationLauncher.logger.info("CalculateLagLeadAngle : Unity-Path1-Active");
					phasedegree = ConstantPowerSourceMte.POWER_SRC_COS_ACTIVE_UPF_ANGLE;
				} else if (ActiveReactive.contains(ConstantApp.METERTYPE_REACTIVE)) {

					ApplicationLauncher.logger.info("CalculateLagLeadAngle : Unity-Path1-Reactive");
					phasedegree = ConstantPowerSourceMte.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE;
				} else {
					ApplicationLauncher.logger.info("Mismatch: Lead Lag ");
					phasedegree = 0.0;
				}

			} catch (Exception e3) {
				// e2.printStackTrace();
				// ApplicationLauncher.logger.info("calculatelag_lead :Exception2:"+
				// e2.getMessage());
				phase = input.substring(0, 1);
				ApplicationLauncher.logger.info("CalculateLagLeadAngle : Unity-Path2");
				phasedegree = 0.0;
				if (ActiveReactive.contains(ConstantApp.METERTYPE_ACTIVE)) {
					ApplicationLauncher.logger.info("CalculateLagLeadAngle : Unity-Path2-Active");
					phasedegree = ConstantPowerSourceMte.POWER_SRC_COS_ACTIVE_UPF_ANGLE;
				} else if (ActiveReactive.contains(ConstantApp.METERTYPE_REACTIVE)) {
					ApplicationLauncher.logger.info("CalculateLagLeadAngle : Unity-Path2-Reactive");
					phasedegree = ConstantPowerSourceMte.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE;
				} else {
					ApplicationLauncher.logger.info("Mismatch: Lead Lag ");
					phasedegree = 0.0;
				}

				// phasedegree = - phasedegree; //Gopi- to be commented on prod

			}

			ApplicationLauncher.logger.info("phasedegree: Unity: " + String.format("%.2f", phasedegree));
			// phasedegree = Math.round(phasedegree);
			ApplicationLauncher.logger.info("phasedegree: Unity1:" + String.format("%.2f", phasedegree));

		}

		ApplicationLauncher.logger.info("*********************************************************");
	}
}
