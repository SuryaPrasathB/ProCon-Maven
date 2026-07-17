package com.tasnetwork.calibration.conveyor.tree;
import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class BayScreen extends JFrame {
    private ConveyorTreeLogic logic; // Logic for managing conveyor tree operations
    private String clusterId; // Identifier for the current cluster

    // Constructor to initialize the BayScreen with the given logic and cluster ID
    public BayScreen(ConveyorTreeLogic logic, String clusterId) {
        super("Bays for Cluster " + clusterId); // Set the title of the JFrame
        this.logic = logic;
        this.clusterId = clusterId;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close application on window close
        setSize(1000, 1000); // Set the size of the window
        // setExtendedState(JFrame.MAXIMIZED_BOTH); // Optional: maximize the window
        setLayout(new FlowLayout(FlowLayout.CENTER, 50, 100)); // Set layout with custom spacing

        // Retrieve and display bays for the given cluster
        List<Bay> bays = logic.getBays(clusterId);
        for (Bay bay : bays) {
            JPanel bayPanel = createBayPanel(bay); // Create a panel for each bay
            add(bayPanel);
        }

        // Add a back button at the bottom of the frame
        add(createBackButton(), BorderLayout.SOUTH);
        
        setVisible(true); // Make the frame visible
    }

    // Creates a panel representing a bay
    private JPanel createBayPanel(Bay bay) {
        JPanel panel = new JPanel(); // Create a new JPanel
        panel.setPreferredSize(new Dimension(250, 150)); // Set preferred size
        panel.setBackground(Color.PINK); // Set background color
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add a black border

        JLabel label = new JLabel("Bay " + bay.getBayId()); // Create a label with the bay ID
        label.setFont(new Font("Arial", Font.BOLD, 20)); // Set font style and size
        panel.add(label); // Add the label to the panel

        // Add a mouse listener for double-click actions
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Check for double-click
                    // Open the DeviceTypeScreen and close the current frame
                    new DeviceTypeScreen(logic, clusterId, bay.getBayId()).setVisible(true);
                    dispose();
                }
            }
        });

        return panel; // Return the created panel
    }

    // Creates a back button to navigate to the previous screen
    private JButton createBackButton() {
        JButton backButton = new JButton("Back"); // Create a new button with label "Back"
        backButton.addActionListener(e -> {
            new ClusterScreen(logic).setVisible(true); // Open the ClusterScreen
            dispose(); // Close the current frame
        });
        return backButton; // Return the back button
    }
}
