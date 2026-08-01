package com.tasnetwork.calibration.energymeter.testreport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.tasnetwork.spring.orm.model.MeterResultDetailed;

public class LoeSorter implements Comparator<MeterResultDetailed> {

    public LoeSorter(List<String> testTypeOrder) {
    }

    @Override
    public int compare(MeterResultDetailed o1, MeterResultDetailed o2) {
        return compareLoeTests(o1.getTestName(), o2.getTestName());
    }

    /**  
     * Compares two test names based on current level and power factor priority.
     */
    public static int compareLoeTests(String testName1, String testName2) {
        // Compare by current value (Descending: Higher current first)
        int currentComparison = Double.compare(parseCurrent(testName2), parseCurrent(testName1));
        if (currentComparison != 0) return currentComparison;

        // Compare by power factor priority (Ascending: 1.0 > 0.8C > 0.5L)
        int pfComparison = Integer.compare(parsePowerFactor(testName1), parsePowerFactor(testName2));
        if (pfComparison != 0) return pfComparison;

        // Default: Sort alphabetically if all else is equal
        return testName1.compareTo(testName2);
    }

    /** Extracts the numerical current value from the test name */
    private static double parseCurrent(String loe) {
        try {
            String[] parts = loe.replaceFirst("LOE_\\d+-", "").split("-");
            String currentPart = parts[parts.length - 1];

            if (currentPart.endsWith("Imax")) {
                return Double.parseDouble(currentPart.replace("Imax", "")) + 10.0; // Boost Imax values
            } else {
                return Double.parseDouble(currentPart.replace("Ib", ""));
            }
        } catch (Exception e) {
            return 0.0; // Default for unknown format
        }
    }

    /** Assigns a priority to the power factor (lower number = higher priority) */
    private static int parsePowerFactor(String loe) {
        String[] parts = loe.replaceFirst("LOE_\\d+-", "").split("-");
        String pf = parts[1];

        // Assign priority (lower numbers sorted first)
        int priority;
        switch (pf) {
            case "1.0":
                priority = 0;  // UPF first
                break;
            case "0.8C":
                priority = 1;  // Lagging second
                break;
            case "0.5L":
                priority = 2;  // Leading last
                break;
            default:
                priority = 3;  // Unknown types (lowest priority)
        }
        return priority;
    }

    public static void main(String[] args) {
        List<String> testTypeOrder = Arrays.asList("LOE_01", "LOE_02", "LOE_03"); // Example order
        LoeSorter sorter = new LoeSorter(testTypeOrder);

/*        List<MeterResultDetailed> meterResults = List.of(
            new MeterResultDetailed("LOE_03", "LOE_03-100U-1.0-1.0Imax"),
            new MeterResultDetailed("LOE_01", "LOE_01-100U-0.5L-1.0Ib"),
            new MeterResultDetailed("LOE_02", "LOE_02-100U-0.8C-0.5Imax"),
            new MeterResultDetailed("LOE_03", "LOE_03-100U-1.0-0.1Ib")
        );*/
        
        List<MeterResultDetailed> meterResults = Arrays.asList(
        	    new MeterResultDetailed("LOE_03", "LOE_03-100U-1.0-1.0Imax"),
        	    new MeterResultDetailed("LOE_01", "LOE_01-100U-0.5L-1.0Ib"),
        	    new MeterResultDetailed("LOE_02", "LOE_02-100U-0.8C-0.5Imax"),
        	    new MeterResultDetailed("LOE_03", "LOE_03-100U-1.0-0.1Ib")
        	);

        List<MeterResultDetailed> sortedResults = new ArrayList<>(meterResults);
        sortedResults.sort(Comparator.comparing(MeterResultDetailed::getTestName, LoeSorter::compareLoeTests));

        sortedResults.forEach(mr -> System.out.println(mr.getTestName()));
    }
}
