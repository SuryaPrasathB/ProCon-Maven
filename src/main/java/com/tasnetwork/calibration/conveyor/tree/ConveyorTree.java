package com.tasnetwork.calibration.conveyor.tree;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ConveyorTree {

    public static void main(String[] args) throws IOException, JSONException {
        // Load the JSON content from the file
        String jsonContent = new String(Files.readAllBytes(Paths.get("src\\resources\\DevSysTerminalConfigV1_1.json")));
        JSONObject jsonObject = new JSONObject(jsonContent);
        JSONArray terminalArray = jsonObject.getJSONArray("Terminal");

        // Parse the first terminal (assuming only one terminal in the array)
        MyTerminal terminalObj = new MyTerminal(terminalArray.getJSONObject(0));

        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Prompt user to enter Cluster ID or exit
            System.out.print("Enter Cluster ID (or 'exit'): ");
            String clusterId = scanner.nextLine();

            if (clusterId.equalsIgnoreCase("exit")) {
                break; // Exit the loop if user types "exit"
            }

            // Prompt user to enter Bay ID
            System.out.print("Enter Bay ID : ");
            String bayId = scanner.nextLine();

            // Find the selected cluster based on input Cluster ID
            Cluster selectedCluster = null;
            for (Cluster cluster : terminalObj.getClusters()) {
                if (cluster.getClusterId().equals(clusterId)) {
                    selectedCluster = cluster;
                    break;
                }
            }

            // Handle invalid Cluster ID
            if (selectedCluster == null) {
                System.out.println("Invalid Cluster ID");
                continue;
            }

            // Find the selected bay based on input Bay ID
            Bay selectedBay = null;
            for (Bay bay : selectedCluster.getBays()) {
                if (bay.getBayId().equals(bayId)) {
                    selectedBay = bay;
                    break;
                }
            }

            // Handle invalid Bay ID
            if (selectedBay == null) {
                System.out.println("Invalid Bay ID in this Cluster");
                continue;
            }

            // Define the order of device types for display and selection
            List<String> orderedTypes = Arrays.asList(
                    "DUT Device", "Mega Ohm Meter", "Volt Meter", "LDU", "QR Scanner", "Output Port", "Input Port");

            // Display the available device types
            System.out.println("Available Device Types:");
            for (int i = 0; i < orderedTypes.size(); i++) {
                System.out.println((i + 1) + ". " + orderedTypes.get(i));
            }

            // Prompt user to select a device type
            System.out.print("Enter Device Type to list: ");
            String deviceTypeInput = scanner.nextLine();

            try {
                int deviceTypeIndex = Integer.parseInt(deviceTypeInput) - 1;
                if (deviceTypeIndex >= 0 && deviceTypeIndex < orderedTypes.size()) {
                    String deviceType = orderedTypes.get(deviceTypeIndex);

                    // Get and display devices of the selected type
                    List<Device> devicesToPrint = selectedBay.getDevicesByType(deviceType);
                    if (!devicesToPrint.isEmpty()) {
                        System.out.println(deviceType + "s:");
                        for (Device device : devicesToPrint) {
                            device.printTree();
                        }
                    } else {
                        System.out.println("No " + deviceType + "s found in this bay.");
                    }
                } else {
                    System.out.println("Invalid Device Type number.");
                }
            } catch (NumberFormatException e) {
                // Handle invalid input for device type selection
                System.out.println("Invalid input. Please enter a number or 'exit'.");
            }

        }

        // Close the scanner and exit the program
        scanner.close();
        System.out.println("Exiting program.");
    }
}
