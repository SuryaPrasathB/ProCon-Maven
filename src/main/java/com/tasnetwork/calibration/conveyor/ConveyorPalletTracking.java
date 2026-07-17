package com.tasnetwork.calibration.conveyor;

 
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ConveyorPalletTracking {

    // Define fixed bay sequence
    private static final List<String> BAY_SEQUENCE = Arrays.asList(
        "Functional Test Bay",
        "High Voltage Test Bay",
        "Calibration Bay",
        "Insulation Resistance Test Bay",
        "Waiting Bay",
        "Verification Test Bay",
        "Short Circuit and No Load Test Bay 1",
        "Short Circuit and No Load Test Bay 2",
        "Communication Test Bay",
        "Unloading Bay",
        "Loading Bay",
        "Rejection Bay"
        
    );
    
    private static  ConcurrentHashMap<Integer, String> positionToMeterSerialNoMap = new ConcurrentHashMap<>();

    private static  ConcurrentHashMap<Integer, String> positionToMeterHardwareIdNoMap = new ConcurrentHashMap<>();

    // Map to track pallet locations in single pallet bays
    private final Map<String, String> singlePalletLocations;

    // Map to track multiple pallet bays
    private final Map<String, List<String>> multiPalletLocations;

    public ConveyorPalletTracking() {
        singlePalletLocations = new HashMap<>();
        multiPalletLocations = new HashMap<>();

        // Initialize multi-pallet bays with empty lists
        multiPalletLocations.put("Waiting Bay", new ArrayList<>());
        multiPalletLocations.put("Verification Test Bay", new ArrayList<>());
        multiPalletLocations.put("Short Circuit and No Load Test Bay 1", new ArrayList<>());
        multiPalletLocations.put("Short Circuit and No Load Test Bay 2", new ArrayList<>());
    }

    // Method to update a single pallet out from a bay
    public void onePalletOutFrom(String bayName) {
        if (!BAY_SEQUENCE.contains(bayName)) {
            throw new IllegalArgumentException("Invalid bay name: " + bayName);
        }

        int nextBayIndex = (BAY_SEQUENCE.indexOf(bayName) + 1) % BAY_SEQUENCE.size();
        String nextBay = BAY_SEQUENCE.get(nextBayIndex);

        if (multiPalletLocations.containsKey(bayName)) {
            List<String> pallets = multiPalletLocations.get(bayName);
            if (!pallets.isEmpty()) {
                String palletId = pallets.remove(0);
                addPalletToBay(palletId, nextBay);
            } else {
                throw new IllegalStateException("No pallets available in " + bayName);
            }
        } else {
            String palletId = singlePalletLocations.entrySet().stream()
                .filter(entry -> entry.getValue().equals(bayName))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No pallets available in " + bayName));

            singlePalletLocations.remove(palletId);
            addPalletToBay(palletId, nextBay);
        }
    }

    // Method to update a single pallet into a bay
    public void onePalletEnteredIn(String bayName, String palletId) {
        if (!BAY_SEQUENCE.contains(bayName)) {
            throw new IllegalArgumentException("Invalid bay name: " + bayName);
        }
        
        addPalletToBay(palletId, bayName);
    }

    // Helper method to add a pallet to a bay
    private void addPalletToBay(String palletId, String bayName) {
        if (multiPalletLocations.containsKey(bayName)) {
            List<String> pallets = multiPalletLocations.get(bayName);
            if (pallets.size() < 4) {
                pallets.add(palletId);
            } else {
                throw new IllegalStateException("No space available in " + bayName);
            }
        } else {
            if (singlePalletLocations.containsValue(bayName)) {
                throw new IllegalStateException("Bay already occupied: " + bayName);
            }
            singlePalletLocations.put(palletId, bayName);
        }
    }

    // Method to retrieve the location of a pallet
    public String getPalletLocation(String palletId) {
        if (singlePalletLocations.containsKey(palletId)) {
            return singlePalletLocations.get(palletId);
        }
        for (Map.Entry<String, List<String>> entry : multiPalletLocations.entrySet()) {
            if (entry.getValue().contains(palletId)) {
                return entry.getKey();
            }
        }
        return "Unknown";
    }
    
    
    public static void setPositionToMeterSerialNoMap(int position, String serialNo) {
        positionToMeterSerialNoMap.put(position, serialNo);
    }
    
    public static void setPositionToMeterHardwareIdNoMap(int position, String serialNo) {
    	positionToMeterHardwareIdNoMap.put(position, serialNo);
    }

    // Get the meter serial number for a given position
    public static String getPositionToMeterSerialNoMap(int position) {
        return positionToMeterSerialNoMap.get(position);
    }
    
     public static String getPositionToMeterHardwareIdNoMap(int position) {
        return positionToMeterHardwareIdNoMap.get(position);
    }
     public static ConcurrentHashMap<Integer, String> getAllPositionToMeterHardwareIdNoMap() {
         return positionToMeterHardwareIdNoMap;
     }

    // Remove the meter from a given position
    public void removeMeter(int position) {
        positionToMeterSerialNoMap.remove(position);
    }
    
    // Remove the meter from a given position
    public void removeMeterHardwareIdNo(int position) {
    	positionToMeterHardwareIdNoMap.remove(position);
    }

    // Check if a position has a meter assigned
    public static boolean hasMeterPositionToMeterSerialNoMap(int position) {
        return positionToMeterSerialNoMap.containsKey(position);
    }
    
    
    // Check if a position has a meter hardware id assigned
    public static boolean hasMeterPositionToMeterHardwareIdNoMap(int position) {
        return positionToMeterHardwareIdNoMap.containsKey(position);
    }

    // Clear all meter serials from all positions
    public static void clearAll() {
        positionToMeterSerialNoMap.clear();
        
    }
    
    public static void clearPositionToMeterHardwareIdNoMap() {
        
        positionToMeterHardwareIdNoMap.clear();
    }

    // Optional: Get a copy of the full map
    public ConcurrentHashMap<Integer, String> getAllMappings() {
        return new ConcurrentHashMap<>(positionToMeterSerialNoMap);
    }
    
    public static void resetPositionToMeterSerialNoMapToDefaultMappings() {
        clearAll(); // Optional: clear before resetting
        for (int i = 1; i <= 6; i++) {
            char suffix = (char) ('a' + (i - 1)); // 'a' to 'f'
            positionToMeterSerialNoMap.put(i, "000000" + suffix);
        }
    }
    
    public static void resetPositionToMeterHardwareIdNoMapToDefaultMappings() {
    	clearPositionToMeterHardwareIdNoMap(); // Optional: clear before resetting
        for (int i = 1; i <= 6; i++) {
            char suffix = (char) ('a' + (i - 1)); // 'a' to 'f'
            positionToMeterHardwareIdNoMap.put(i, "000000" + suffix);
        }
    }

  /*  // Main method for demonstration
    public static void main(String[] args) {
    	ConveyorPalletTracking conveyorSystem = new ConveyorPalletTracking();

        // Add some pallets to the system
        conveyorSystem.onePalletEnteredIn("Loading Bay", "Pallet001");
        conveyorSystem.onePalletEnteredIn("Loading Bay", "Pallet002");

        // Simulate pallet movement
        conveyorSystem.onePalletOutFrom("Loading Bay");
        conveyorSystem.onePalletOutFrom("Functional Test Bay");

        // Display pallet locations
        System.out.println("Pallet001 is in: " + conveyorSystem.getPalletLocation("Pallet001"));
        System.out.println("Pallet002 is in: " + conveyorSystem.getPalletLocation("Pallet002"));

        // Move pallets to the next bays
        conveyorSystem.onePalletOutFrom("High Voltage Test Bay");

        // Display updated pallet locations
        System.out.println("Pallet001 is now in: " + conveyorSystem.getPalletLocation("Pallet001"));
        System.out.println("Pallet002 is now in: " + conveyorSystem.getPalletLocation("Pallet002"));
    }*/
}
