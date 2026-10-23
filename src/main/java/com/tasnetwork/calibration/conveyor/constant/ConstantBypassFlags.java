package com.tasnetwork.calibration.conveyor.constant;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.spring.orm.model.PalletMeter;
import com.tasnetwork.spring.orm.model.PalletMeterResults;

/**
 * Constants and policy configuration for conveyor bypass flags.
 * Centralized location to control bay bypass behaviors and forced PASS result policies.
 */
public class ConstantBypassFlags {

    // =========================================================================
    // STA and NLD Result Value Policy (Maintained in this single location)
    // =========================================================================
    public static final int[] STA_POSSIBLE_VALUES = { 1, 2 };
    public static final String NLD_DEFAULT_RESULT_VALUE = "0";

    /**
     * Master toggle: If true, tests corresponding to bypassed bays are automatically
     * treated as PASS (satisfied) during overall status calculation at the Unloading Bay.
     * If false, missing tests from bypassed bays will be flagged as ':TBD' and cause an overall FAIL.
     */
    public static final boolean TREAT_BYPASS_AS_PASS = true;

    /**
     * Mapping from Test Type Alias (e.g. FT, HV, IR, LOE, NLD, STA, COMM, CALIB)
     * to the corresponding conveyor Bay Key(s).
     */
    public static final Map<String, List<String>> TEST_TYPE_TO_BAY_KEYS = new HashMap<>();

    /**
     * Optional granular control: specify which test types can be forced to PASS when bypassed.
     * If a test type is set to true here (and TREAT_BYPASS_AS_PASS is true), it will be forced PASS.
     * If set to false, it will require an actual executed test result.
     */
    public static final Map<String, Boolean> FORCE_PASS_PER_TEST_FLAG = new HashMap<>();

    /*
     * For mechanical hardware testing.
     * Bypass the testing, other operations will occur.
     */
    public static final boolean GLOBAL_TEST_BYPASS = false;
    
    /**
     * A map to store mechanical hardware test bypass flags for individual bays.
     * The key is the bay identifier (e.g., "FT_BAY_KEY", "HV_BAY_KEY").
     * The value is true if mechanical testing for the bay is bypassed, false otherwise.
     */
    public static final Map<String, Boolean> BAY_TEST_BYPASS_FLAGS = new HashMap<>();

    /*
     * For entire bay bypass (i.e., skipping all operations except pallet sensing and releasing).
     * This flag can be used as a global override if needed, but individual bay flags
     * in the `BAY_BYPASS_FLAGS` map provide more granular control.
     */
    public static final boolean GLOBAL_BAY_BYPASS = false;

    /**
     * A map to store full bay bypass flags for individual bays.
     * The key is the bay identifier (e.g., "FT_BAY_KEY", "HV_BAY_KEY").
     * The value is true if the entire bay is bypassed, false otherwise.
     */
    public static final Map<String, Boolean> BAY_BYPASS_FLAGS = new HashMap<>();

    static {
        // Initialize full bay bypass flags for each bay.
        // By default, set them all to false (not bypassed).
        // You can change these values as needed for specific operational bypass.
        BAY_BYPASS_FLAGS.put(ConstantConveyor.FT_BAY_KEY, 				false);
        BAY_BYPASS_FLAGS.put(ConstantConveyor.HV_BAY_KEY, 				false);
        BAY_BYPASS_FLAGS.put(ConstantConveyor.IR_BAY_KEY, 				false);
        BAY_BYPASS_FLAGS.put(ConstantConveyor.CALIBRATION_BAY_KEY, 		false);
        BAY_BYPASS_FLAGS.put(ConstantConveyor.WAITING_BAY_KEY, 			false);
        BAY_BYPASS_FLAGS.put(ConstantConveyor.VERIFICATION_BAY_KEY, 	false);
        BAY_BYPASS_FLAGS.put(ConstantConveyor.STA_NLD1_BAY_KEY, 		false);
        BAY_BYPASS_FLAGS.put(ConstantConveyor.STA_NLD2_BAY_KEY, 		false);
        BAY_BYPASS_FLAGS.put(ConstantConveyor.COMMUNICATION_BAY_KEY, 	false);
        BAY_BYPASS_FLAGS.put(ConstantConveyor.UNLOADING_BAY_KEY, 		false);
        BAY_BYPASS_FLAGS.put(ConstantConveyor.LOADING_BAY_KEY, 			false);
        BAY_BYPASS_FLAGS.put(ConstantConveyor.REJECTION_BAY_KEY, 		false);

        // Initialize mechanical test bypass flags for each bay.
        // By default, set them all to false (not bypassed).
        // These can be set to true for specific mechanical hardware testing scenarios.
        BAY_TEST_BYPASS_FLAGS.put(ConstantConveyor.FT_BAY_KEY, 				false);
        BAY_TEST_BYPASS_FLAGS.put(ConstantConveyor.HV_BAY_KEY, 				false);
        BAY_TEST_BYPASS_FLAGS.put(ConstantConveyor.IR_BAY_KEY, 				false);
        BAY_TEST_BYPASS_FLAGS.put(ConstantConveyor.CALIBRATION_BAY_KEY, 	false);
        BAY_TEST_BYPASS_FLAGS.put(ConstantConveyor.WAITING_BAY_KEY, 		false);
        BAY_TEST_BYPASS_FLAGS.put(ConstantConveyor.VERIFICATION_BAY_KEY, 	false);
        BAY_TEST_BYPASS_FLAGS.put(ConstantConveyor.STA_NLD1_BAY_KEY, 		false);
        BAY_TEST_BYPASS_FLAGS.put(ConstantConveyor.STA_NLD2_BAY_KEY, 		false);
        BAY_TEST_BYPASS_FLAGS.put(ConstantConveyor.COMMUNICATION_BAY_KEY, 	false);
        BAY_TEST_BYPASS_FLAGS.put(ConstantConveyor.UNLOADING_BAY_KEY, 		false);
        BAY_TEST_BYPASS_FLAGS.put(ConstantConveyor.LOADING_BAY_KEY, 		false);
        BAY_TEST_BYPASS_FLAGS.put(ConstantConveyor.REJECTION_BAY_KEY, 		false);

        // Map Test Type aliases to Bay Keys
        TEST_TYPE_TO_BAY_KEYS.put(ConstantConveyor.FT_ALIAS_NAME, Arrays.asList(ConstantConveyor.FT_BAY_KEY));
        TEST_TYPE_TO_BAY_KEYS.put(ConstantConveyor.HV_ALIAS_NAME, Arrays.asList(ConstantConveyor.HV_BAY_KEY));
        TEST_TYPE_TO_BAY_KEYS.put(ConstantConveyor.IR_ALIAS_NAME, Arrays.asList(ConstantConveyor.IR_BAY_KEY));
        TEST_TYPE_TO_BAY_KEYS.put(ConstantConveyor.ACCURACY_ALIAS_NAME, Arrays.asList(ConstantConveyor.VERIFICATION_BAY_KEY));
        TEST_TYPE_TO_BAY_KEYS.put(ConstantConveyor.CREEP_ALIAS_NAME, Arrays.asList(ConstantConveyor.STA_NLD1_BAY_KEY, ConstantConveyor.STA_NLD2_BAY_KEY));
        TEST_TYPE_TO_BAY_KEYS.put(ConstantConveyor.STA_ALIAS_NAME, Arrays.asList(ConstantConveyor.STA_NLD1_BAY_KEY, ConstantConveyor.STA_NLD2_BAY_KEY));
        TEST_TYPE_TO_BAY_KEYS.put(ConstantConveyor.COMM_ALIAS_NAME, Arrays.asList(ConstantConveyor.COMMUNICATION_BAY_KEY));
        TEST_TYPE_TO_BAY_KEYS.put(ConstantConveyor.CALIB_ALIAS_NAME, Arrays.asList(ConstantConveyor.CALIBRATION_BAY_KEY));

        // Default all per-test forced PASS flags to true
        FORCE_PASS_PER_TEST_FLAG.put(ConstantConveyor.FT_ALIAS_NAME, true);
        FORCE_PASS_PER_TEST_FLAG.put(ConstantConveyor.HV_ALIAS_NAME, true);
        FORCE_PASS_PER_TEST_FLAG.put(ConstantConveyor.IR_ALIAS_NAME, true);
        FORCE_PASS_PER_TEST_FLAG.put(ConstantConveyor.ACCURACY_ALIAS_NAME, true);
        FORCE_PASS_PER_TEST_FLAG.put(ConstantConveyor.CREEP_ALIAS_NAME, true);
        FORCE_PASS_PER_TEST_FLAG.put(ConstantConveyor.STA_ALIAS_NAME, true);
        FORCE_PASS_PER_TEST_FLAG.put(ConstantConveyor.COMM_ALIAS_NAME, true);
        FORCE_PASS_PER_TEST_FLAG.put(ConstantConveyor.CALIB_ALIAS_NAME, true);
    }

    /**
     * Utility method to check if a specific bay is fully bypassed (skipping most operations).
     * This checks both the global bypass flag and the individual bay's full bypass flag.
     *
     * @param bayName The name or identifier of the bay (e.g., ConstantConveyor.HV_BAY_KEY).
     * @return true if the bay is fully bypassed, false otherwise (or if the bay doesn't exist in the map).
     */
    public static boolean isBayFullyBypassed(String bayName) {
        // If GLOBAL_BAY_BYPASS is true, all bays are bypassed regardless of individual settings.
        if (GLOBAL_BAY_BYPASS) {
            return true;
        }
        return BAY_BYPASS_FLAGS.getOrDefault(bayName, false);
    }

    /**
     * Utility method to check if mechanical hardware testing is bypassed for a specific bay.
     * This checks both the global test bypass flag and the individual bay's test bypass flag.
     *
     * @param bayName The name or identifier of the bay (e.g., ConstantConveyor.HV_BAY_KEY).
     * @return true if mechanical testing for the bay is bypassed, false otherwise (or if the bay doesn't exist in the map).
     */
    public static boolean isBayTestBypassed(String bayName) {
        // If GLOBAL_TEST_BYPASS is true, all bay tests are bypassed regardless of individual settings.
        if (GLOBAL_TEST_BYPASS) {
            return true;
        }
        return BAY_TEST_BYPASS_FLAGS.getOrDefault(bayName, false);
    }

    /**
     * Utility method to check if a bay is bypassed under any mode (either full bay bypass or test bypass).
     *
     * @param bayName The name or identifier of the bay (e.g., ConstantConveyor.HV_BAY_KEY).
     * @return true if the bay is bypassed either fully or for testing.
     */
    public static boolean isBayAnyBypassed(String bayName) {
        return isBayFullyBypassed(bayName) || isBayTestBypassed(bayName);
    }

    /**
     * Centralized policy check: Determines whether a test type should be forced to PASS (satisfied)
     * because its corresponding bay was bypassed.
     *
     * @param testType The test type alias (e.g. ConstantConveyor.FT_ALIAS_NAME, HV, IR, LOE, etc.).
     * @return true if the test type should be forced to PASS due to bypass; false otherwise.
     */
    public static boolean isTestForcePassedDueToBypass(String testType) {
        if (!TREAT_BYPASS_AS_PASS) {
            return false;
        }
        if (testType == null) {
            return false;
        }
        String trimmed = testType.trim();
        if (!FORCE_PASS_PER_TEST_FLAG.getOrDefault(trimmed, true)) {
            return false;
        }
        List<String> bayKeys = TEST_TYPE_TO_BAY_KEYS.get(trimmed);
        if (bayKeys != null) {
            for (String bayKey : bayKeys) {
                if (isBayAnyBypassed(bayKey)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Computes the result value for a given test type according to policy:
     * - STA: random whole number, either 1 or 2
     * - NLD: 0
     * - Others: default/original value
     *
     * @param testType The test type alias (e.g. ConstantConveyor.STA_ALIAS_NAME, CREEP_ALIAS_NAME).
     * @param originalValue Fallback value if not overridden.
     * @return Formatted result value string.
     */
    public static String getComputedResultValue(String testType, String originalValue) {
        if (testType != null) {
            String trimmed = testType.trim();
            String upper = trimmed.toUpperCase();
            if (upper.equals(ConstantConveyor.STA_ALIAS_NAME)
                    || upper.equals(ConstantConveyor.STA_RESULT_KEY)
                    || upper.startsWith("STA")) {
                // Random whole number, either 1 or 2
                int randomVal = STA_POSSIBLE_VALUES[(int) (Math.random() * STA_POSSIBLE_VALUES.length)];
                return String.valueOf(randomVal);
            } else if (upper.equals(ConstantConveyor.CREEP_ALIAS_NAME)
                    || upper.equals(ConstantConveyor.NLD_RESULT_KEY)
                    || upper.startsWith("NLD")
                    || upper.startsWith("CREEP")) {
                return NLD_DEFAULT_RESULT_VALUE;
            }
        }
        return originalValue != null ? originalValue : "";
    }

    /**
     * Ensures an actual PalletMeterResults record exists for a bypassed test type on the meter,
     * populating the appropriate result value (e.g. STA: random [1, 2], NLD: 0).
     *
     * @param palletMeter The target pallet meter.
     * @param testType The test type alias.
     */
    public static void ensureBypassedMeterResultRecord(PalletMeter palletMeter, String testType) {
        if (palletMeter == null || testType == null) {
            return;
        }
        String trimmed = testType.trim();
        boolean exists = palletMeter.getPalletMeterResultsList().stream()
                .anyMatch(r -> trimmed.equalsIgnoreCase(r.getTestType()));
        if (!exists) {
            PalletMeterResults result = new PalletMeterResults();
            result.setPalletMeter(palletMeter);
            result.setMeterSerialNo(palletMeter.getMeterSerialNo());
            result.setRackPositionNo(palletMeter.getRackPositionNo());
            result.setPalletDistinctId(palletMeter.getPalletDistinctId());
            if (palletMeter.getPalletManage() != null) {
                result.setPalletQrId(palletMeter.getPalletManage().getPalletQrId());
                result.setPalletBatchNo(palletMeter.getPalletManage().getPalletBatchNo());
            }
            result.setTestType(trimmed);
            result.setTestCaseName(trimmed + " (Bypass)");
            result.setResultStatus(ConstantReport.REPORT_POPULATE_PASS);
            result.setResultValue(getComputedResultValue(trimmed, ConstantReport.REPORT_POPULATE_PASS));
            List<String> bayKeys = TEST_TYPE_TO_BAY_KEYS.get(trimmed);
            if (bayKeys != null && !bayKeys.isEmpty()) {
                result.setBayStateKey(bayKeys.get(0));
            }
            palletMeter.addPalletMeterResults(result);
        }
    }
}