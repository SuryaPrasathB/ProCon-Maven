package com.tasnetwork.calibration.conveyor.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Constants for conveyor bypass flags.
 */
public class ConstantBypassFlags {

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
        BAY_BYPASS_FLAGS.put(ConstantConveyor.FT_BAY_KEY, 				true);
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
}