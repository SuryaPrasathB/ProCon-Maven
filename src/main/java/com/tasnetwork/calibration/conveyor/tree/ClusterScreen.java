package com.tasnetwork.calibration.conveyor.tree;
import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

// Represents the screen displaying all clusters
public class ClusterScreen extends JFrame {
    private ConveyorTreeLogic logic; // Logic for managing conveyor tree operations

    // Constructor to initialize the ClusterScreen with logic
    public ClusterScreen(ConveyorTreeLogic logic) {
        super("Clusters"); // Set the title of the JFrame
        this.logic = logic;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close application on window close
        setSize(1000, 1000); // Set the size of the window
        // setExtendedState(JFrame.MAXIMIZED_BOTH); // Optional: maximize the window
        setLayout(new FlowLayout(FlowLayout.CENTER, 50, 100)); // Center clusters with spacing

        // Retrieve and display clusters from the logic
        List<Cluster> clusters = logic.getClusters();
        for (Cluster cluster : clusters) {
            JPanel clusterPanel = createClusterPanel(cluster); // Create a panel for each cluster
            add(clusterPanel); // Add the panel to the frame
        }

        setVisible(true); // Make the frame visible
    }

    // Creates a panel representing a cluster
    private JPanel createClusterPanel(Cluster cluster) {
        JPanel panel = new JPanel(); // Create a new JPanel
        panel.setPreferredSize(new Dimension(250, 150)); // Set preferred size
        panel.setBackground(Color.YELLOW); // Set background color
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add a black border

        JLabel label = new JLabel("Cluster " + cluster.getClusterId()); // Create a label with the cluster ID
        label.setFont(new Font("Arial", Font.BOLD, 20)); // Set font style and size
        panel.add(label); // Add the label to the panel

        // Add a mouse listener for double-click actions
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Check for double-click
                    // Open the BayScreen for the selected cluster and close the current frame
                    new BayScreen(logic, cluster.getClusterId()).setVisible(true);
                    dispose(); // Close the current window
                }
            }
        });

        return panel; // Return the created panel
    }
}
