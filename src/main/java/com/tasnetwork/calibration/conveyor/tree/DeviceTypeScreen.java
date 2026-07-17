package com.tasnetwork.calibration.conveyor.tree;
import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

public class DeviceTypeScreen extends JFrame {

    // Reference to ConveyorTreeLogic for data access
    private ConveyorTreeLogic logic;

    // Cluster and Bay IDs passed as constructor arguments
    private String clusterId;
    private String bayId;

    public DeviceTypeScreen(ConveyorTreeLogic logic, String clusterId, String bayId) {
        super("Device Types for Bay " + bayId); // Set window title

        this.logic = logic;
        this.clusterId = clusterId;
        this.bayId = bayId;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000); // Set window size
        // setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Center panels with 50px horizontal and 100px vertical spacing
        setLayout(new FlowLayout(FlowLayout.CENTER, 50, 100));

        // Pre-defined list of device types
        List<String> deviceTypes = Arrays.asList("DUT Device", "Mega Ohm Meter", "Volt Meter", "LDU", "QR Scanner", "Output Port", "Input Port");

        // Create and add panels for each device type
        for (String deviceType : deviceTypes) {
            JPanel deviceTypePanel = createDeviceTypePanel(deviceType);
            add(deviceTypePanel);
        }

        // Add back button to bottom of the window
        add(createBackButton(), BorderLayout.SOUTH);

        setVisible(true); // Make the window visible
    }

    private JPanel createDeviceTypePanel(String deviceType) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(200, 150)); // Set panel size
        panel.setBackground(Color.GREEN); // Set panel background color
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add border

        JLabel label = new JLabel(deviceType, SwingConstants.CENTER); // Center-aligned label
        label.setFont(new Font("Arial", Font.BOLD, 16)); // Set label font and style

        panel.add(label); // Add label to the panel

        // Handle double-click on panel to open DeviceScreen
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    new DeviceScreen(logic, clusterId, bayId, deviceType).setVisible(true);
                    dispose(); // Close current window
                }
            }
        });

        return panel;
    }

    private JButton createBackButton() {
        JButton backButton = new JButton("Back");

        // Use lambda expression for concise action listener
        backButton.addActionListener(e -> {
            new BayScreen(logic, clusterId).setVisible(true);
            dispose(); // Close current window
        });

        return backButton;
    }
}