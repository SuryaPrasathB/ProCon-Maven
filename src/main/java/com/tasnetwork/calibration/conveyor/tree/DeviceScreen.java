package com.tasnetwork.calibration.conveyor.tree;
import javax.swing.*;

import java.awt.*;
import java.util.List;

public class DeviceScreen extends JFrame {

    private ConveyorTreeLogic logic;  // Logic to interact with the terminal data
    private String clusterId;         // Cluster ID for filtering devices
    private String bayId;             // Bay ID for filtering devices

    // Constructor to initialize the DeviceScreen frame with the provided logic, cluster, bay, and device type
    public DeviceScreen(ConveyorTreeLogic logic, String clusterId, String bayId, String deviceType) {
        super("Devices of type " + deviceType + " in Bay " + bayId);  // Set the window title
        this.logic = logic;
        this.clusterId = clusterId;
        this.bayId = bayId;
        
        // Set basic properties for the JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);  // Set the size of the frame
        //setExtendedState(JFrame.MAXIMIZED_BOTH);  // Uncomment to maximize window
        setLayout(new FlowLayout(FlowLayout.CENTER, 50, 100));  // Set the layout manager with some spacing

        // Retrieve the list of devices from the logic object
        List<Device> devices = logic.getDevices(clusterId, bayId, deviceType);

        // If devices are found, display them in a scrollable container
        if (devices != null && !devices.isEmpty()) {
            JPanel devicePanelContainer = new JPanel(new GridLayout(0, 3, 40, 40));  // Grid Layout for devices
            for (Device device : devices) {
                JPanel devicePanel = createDevicePanel(device);  // Create individual panels for each device
                devicePanelContainer.add(devicePanel);
            }
            JScrollPane scrollPane = new JScrollPane(devicePanelContainer);  // Add a scroll pane for scrolling through devices
            add(scrollPane, BorderLayout.CENTER);
        } else {
            // If no devices found, display a "No Devices Found" label
            JLabel noDevicesLabel = new JLabel("No Devices Found", SwingConstants.CENTER);
            noDevicesLabel.setFont(new Font("Arial", Font.BOLD, 20));
            add(noDevicesLabel, BorderLayout.CENTER);
        }

        // Add the back button to the bottom of the frame
        add(createBackButton(), BorderLayout.SOUTH);

        setVisible(true);  // Make the frame visible
    }

    // Method to create a panel displaying information for a single device
    private JPanel createDevicePanel(Device device) {
        JPanel panel = new JPanel(new GridBagLayout());  // Use GridBagLayout to center the content
        panel.setPreferredSize(new Dimension(250, 150));  // Set the preferred size of the panel
        panel.setBackground(Color.CYAN);  // Set background color of the panel
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));  // Add a border around the panel

        // Create a label to display the device's name and ID in a centered format
        JLabel label = new JLabel("<html><center>" + device.getName() + "<br>(ID: " + device.getId() + ")</center></html>");
        label.setFont(new Font("Arial", Font.BOLD, 16));  // Set the font style for the label

        // Set constraints to center the label inside the panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(label, gbc);

        return panel;
    }

    // Method to create a "Back" button that navigates to the previous screen
    private JButton createBackButton() {
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            // On button click, navigate to the previous screen and dispose the current one
            new DeviceTypeScreen(logic, clusterId, bayId).setVisible(true);
            dispose();
        });
        return backButton;
    }
}
