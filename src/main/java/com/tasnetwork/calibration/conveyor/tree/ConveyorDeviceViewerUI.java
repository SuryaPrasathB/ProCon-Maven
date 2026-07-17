package com.tasnetwork.calibration.conveyor.tree;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import org.json.JSONException;

// Main UI for viewing and interacting with Conveyor Devices
public class ConveyorDeviceViewerUI extends JFrame {

    private JTextField clusterIdField; // Input field for cluster ID
    private JTextField bayIdField; // Input field for bay ID
    private JComboBox<String> deviceTypeComboBox; // Dropdown for device types
    private ConveyorTreeLogic logic; // Logic handler for conveyor tree operations

    private JTable outputTable; // Table to display device data
    private DefaultTableModel tableModel; // Model for the output table

    private final Font FONT_ARIAL_16 = new Font("Arial", Font.PLAIN, 16); // Font for UI components

    // Constructor to initialize the UI
    public ConveyorDeviceViewerUI() throws IOException, JSONException {
        super("Conveyor Device Viewer"); // Set window title
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close application on window close
        setSize(800, 600); // Set default window size
        // setExtendedState(JFrame.MAXIMIZED_BOTH); // Optional: maximize the window
        setLayout(new BorderLayout(10, 10)); // Add gaps between components

        logic = new ConveyorTreeLogic("src\\resources\\DevSysTerminalConfigV1_1.json"); // Load logic with configuration

        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5)); // Add padding around main panel

        // Input panel to collect cluster, bay, and device type
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 2, 2)); // Grid layout with gaps
        TitledBorder inputBorder = BorderFactory.createTitledBorder("Input"); // Title for input section
        inputBorder.setTitleFont(FONT_ARIAL_16); // Set font for input border title
        inputPanel.setBorder(inputBorder);

        JLabel clusterLabel = new JLabel("Cluster: ", SwingConstants.CENTER); // Label for cluster input
        clusterLabel.setFont(FONT_ARIAL_16);
        inputPanel.add(clusterLabel);

        clusterIdField = new JTextField(); // Text field for cluster input
        clusterIdField.setFont(FONT_ARIAL_16);
        inputPanel.add(clusterIdField);

        JLabel bayLabel = new JLabel("Bay: ", SwingConstants.CENTER); // Label for bay input
        bayLabel.setFont(FONT_ARIAL_16);
        inputPanel.add(bayLabel);

        bayIdField = new JTextField(); // Text field for bay input
        bayIdField.setFont(FONT_ARIAL_16);
        inputPanel.add(bayIdField);

        JLabel deviceTypeLabel = new JLabel("Device Type: ", SwingConstants.CENTER); // Label for device type
        deviceTypeLabel.setFont(FONT_ARIAL_16);
        inputPanel.add(deviceTypeLabel);

        // Dropdown for selecting device type
        String[] deviceTypes = {"Select Device", "DUT Device", "Mega Ohm Meter", "Volt Meter", "LDU", "QR Scanner", "Output Port", "Input Port"};
        deviceTypeComboBox = new JComboBox<>(deviceTypes);
        deviceTypeComboBox.setFont(FONT_ARIAL_16);
        inputPanel.add(deviceTypeComboBox);

        // Add action listener to update table when a device type is selected
        deviceTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!deviceTypeComboBox.getSelectedItem().equals("Select Device")) { // Ensure a valid selection
                    try {
                        displayDevices(); // Display devices for the selected type
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(ConveyorDeviceViewerUI.this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    tableModel.setRowCount(0); // Clear table if no valid selection
                }
            }
        });

        mainPanel.add(inputPanel, BorderLayout.NORTH); // Add input panel to the top

        // Table setup for displaying devices
        String[] columnNames = {"Serial No.", "Cluster ID", "Bay ID", "Name", "ID", "Device Type", "Select"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == getColumnCount() - 1) {
                    return Boolean.class; // Last column is a checkbox
                }
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == getColumnCount() - 1; // Only the last column is editable
            }
        };

        outputTable = new JTable(tableModel); // Create table with the model
        outputTable.setFont(FONT_ARIAL_16);
        outputTable.setRowHeight(30); // Set row height

        JTableHeader header = outputTable.getTableHeader(); // Header of the table
        header.setFont(FONT_ARIAL_16);

        // Set column widths
        outputTable.getColumnModel().getColumn(0).setPreferredWidth(70); // Serial No.
        outputTable.getColumnModel().getColumn(outputTable.getColumnCount() - 1).setPreferredWidth(70); // Checkbox
        outputTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); // Auto-resize columns

        // Center text in all cells except the checkbox column
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setFont(FONT_ARIAL_16);
        for (int i = 0; i < outputTable.getColumnCount() - 1; i++) {
            outputTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        outputTable.getColumnModel().getColumn(outputTable.getColumnCount() - 1).setCellRenderer(new CheckBoxRenderer()); // Custom renderer for checkbox

        JScrollPane scrollPane = new JScrollPane(outputTable); // Scrollable pane for the table

        TitledBorder outputBorder = BorderFactory.createTitledBorder("Output"); // Title for output section
        outputBorder.setTitleFont(FONT_ARIAL_16);
        scrollPane.setBorder(outputBorder);

        mainPanel.add(scrollPane, BorderLayout.CENTER); // Add scroll pane to center

        add(mainPanel); // Add main panel to the frame
        setVisible(true); // Make the frame visible
    }

    // Renderer for checkboxes in the table
    private static class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {
        public CheckBoxRenderer() {
            setHorizontalAlignment(JLabel.CENTER); // Center the checkbox
            setBorder(new EmptyBorder(10, 5, 10, 5)); // Add padding
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setSelected((Boolean) value); // Set checkbox state
            return this;
        }
    }

    // Displays devices based on the input fields
    private void displayDevices() throws IOException {
        String clusterId = clusterIdField.getText(); // Get cluster ID from input
        String bayId = bayIdField.getText(); // Get bay ID from input
        String deviceType = (String) deviceTypeComboBox.getSelectedItem(); // Get selected device type

        List<DeviceData> deviceDataList = logic.getDeviceDataList(clusterId, bayId, deviceType); // Retrieve devices

        tableModel.setRowCount(0); // Clear existing rows

        if (deviceDataList != null) {
            int serialNo = 1; // Serial number for devices
            for (DeviceData deviceData : deviceDataList) {
                Object[] rowData = {
                        serialNo++,
                        deviceData.getClusterId(),
                        deviceData.getBayId(),
                        deviceData.getName(),
                        deviceData.getId(),
                        deviceData.getType(),
                        false // Default state for checkbox
                };
                tableModel.addRow(rowData); // Add row to the table
            }
        } else {
            JOptionPane.showMessageDialog(this, logic.getLastErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
        }
    }
}
