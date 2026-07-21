package com.tasnetwork.calibration.conveyor;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

import java.util.*;

public class StateRegistry {

    private static final Logger logger = LoggerFactory.getLogger(StateRegistry.class);

    private static final Map<String, String> BAY_DISPLAY_TO_PACKAGE = new HashMap<>();

    static {
        BAY_DISPLAY_TO_PACKAGE.put(ConstantConveyor.FT_BAY_DISPLAY_NAME, "com.tasnetwork.calibration.conveyor.bay.ft");
        BAY_DISPLAY_TO_PACKAGE.put(ConstantConveyor.HV_BAY_DISPLAY_NAME, "com.tasnetwork.calibration.conveyor.bay.hvt");
        BAY_DISPLAY_TO_PACKAGE.put(ConstantConveyor.IR_BAY_DISPLAY_NAME, "com.tasnetwork.calibration.conveyor.bay.ir");
        BAY_DISPLAY_TO_PACKAGE.put(ConstantConveyor.CALIBRATION_BAY_DISPLAY_NAME,
                "com.tasnetwork.calibration.conveyor.bay.calib");
        BAY_DISPLAY_TO_PACKAGE.put(ConstantConveyor.COMMUNICATION_BAY_DISPLAY_NAME,
                "com.tasnetwork.calibration.conveyor.bay.comm");
        BAY_DISPLAY_TO_PACKAGE.put(ConstantConveyor.LOADING_BAY_DISPLAY_NAME,
                "com.tasnetwork.calibration.conveyor.bay.loading");
        BAY_DISPLAY_TO_PACKAGE.put(ConstantConveyor.STA_NLD1_BAY_DISPLAY_NAME,
                "com.tasnetwork.calibration.conveyor.bay.sctnlt1");
        BAY_DISPLAY_TO_PACKAGE.put(ConstantConveyor.STA_NLD2_BAY_DISPLAY_NAME,
                "com.tasnetwork.calibration.conveyor.bay.sctnlt2");
        BAY_DISPLAY_TO_PACKAGE.put(ConstantConveyor.UNLOADING_BAY_DISPLAY_NAME,
                "com.tasnetwork.calibration.conveyor.bay.unloading");
        BAY_DISPLAY_TO_PACKAGE.put(ConstantConveyor.VERIFICATION_BAY_DISPLAY_NAME,
                "com.tasnetwork.calibration.conveyor.bay.verific");
        BAY_DISPLAY_TO_PACKAGE.put(ConstantConveyor.WAITING_BAY_DISPLAY_NAME,
                "com.tasnetwork.calibration.conveyor.bay.waiting");
        BAY_DISPLAY_TO_PACKAGE.put(ConstantConveyor.REJECTION_BAY_DISPLAY_NAME,
                "com.tasnetwork.calibration.conveyor.bay.rejection");
    }

    private static final Map<String, List<String>> CACHED_BAY_STATES = new HashMap<>();

    public static List<String> getStatesForBay(String bayDisplayName) {
        if (bayDisplayName == null || bayDisplayName.isEmpty() || "Select Bay".equalsIgnoreCase(bayDisplayName)) {
            return Collections.emptyList();
        }

        if (CACHED_BAY_STATES.containsKey(bayDisplayName)) {
            return CACHED_BAY_STATES.get(bayDisplayName);
        }

        String packageName = BAY_DISPLAY_TO_PACKAGE.get(bayDisplayName);
        if (packageName == null) {
            logger.warn("No package mapping found for bay display name: {}", bayDisplayName);
            return Collections.emptyList();
        }

        List<String> stateClassNames = discoverStatesInPackage(packageName);
        CACHED_BAY_STATES.put(bayDisplayName, stateClassNames);
        return stateClassNames;
    }

    private static List<String> discoverStatesInPackage(String packageName) {
        try {
            Reflections reflections = new Reflections(new ConfigurationBuilder()
                    .setUrls(ClasspathHelper.forPackage(packageName))
                    .setScanners(Scanners.SubTypes.filterResultsBy(s -> true)));

            Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);
            List<String> stateNames = new ArrayList<>();

            for (Class<?> clazz : classes) {
                if (clazz.getPackage() != null && clazz.getPackage().getName().equals(packageName)) {
                    String simpleName = clazz.getSimpleName();
                    if (!simpleName.contains("$") && simpleName.startsWith("S")) {
                        stateNames.add(simpleName);
                    }
                }
            }

            stateNames.sort(String::compareTo);
            return stateNames;
        } catch (Exception e) {
            logger.error("Failed to discover state classes in package {}: {}", packageName, e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
