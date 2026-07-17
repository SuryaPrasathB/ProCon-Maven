package com.tasnetwork.calibration.conveyor.tree;
import javax.swing.*;
import java.io.IOException;
import org.json.JSONException;

public class ConveyorUIEntry {
    
    // Main method to initialize the user interface
    public static void main(String[] args) {
        // Execute UI initialization on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            try {
                // Set the Look and Feel to the system's native look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                try {
                    // Initialize the ClusterScreen with the specified config file
                    new ClusterScreen(new ConveyorTreeLogic("src\\resources\\DevSysTerminalConfigV1_1.json"));
                    
                    // Initialize the ConveyorDeviceViewerUI
                    new ConveyorDeviceViewerUI();
                } catch (JSONException e) {
                    // Handle JSON-related exceptions, such as malformed or missing configuration
                    e.printStackTrace();
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | 
                     UnsupportedLookAndFeelException | IOException e) {
                // Handle all exceptions related to setting the Look and Feel or file I/O
                e.printStackTrace();
            }
        });
    }
}
