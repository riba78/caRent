package caRent;

/**
 * Admin-only Swing form (JPanel) that enables car management operations.
 * Allows adding, updating, and deleting cars from the inventory.
 *
 * Used in: MainFrame (Admin tab "Manage Cars")
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.Year;

public class ManageCarsForm extends JPanel {
    // Form fields for car information
    private JComboBox<Integer> yearCombo;   // manufacturing year
    private JComboBox<String> makeCombo;      // car brand
    private JTextField modelField;            // car model
    private JTextField dailyPriceField, purchasePriceField;
    private JComboBox<String> statusCombo;
    private JButton addButton, updateButton, deleteButton, refreshButton;
    private JTable carsTable;
    private DefaultTableModel tableModel;
    
    public ManageCarsForm() {
        setLayout(new BorderLayout());
        
        // Create a panel for input fields using GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Year field
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Year:"), gbc);
        gbc.gridx = 1;
        yearCombo = new JComboBox<>(getYearRange());
        formPanel.add(yearCombo, gbc);
        
        // Make field (brand)
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Make:"), gbc);
        gbc.gridx = 1;
        makeCombo = new JComboBox<>(getPopularBrands());
        formPanel.add(makeCombo, gbc);
        
        // Model field (specific model)
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Model:"), gbc);
        gbc.gridx = 1;
        modelField = new JTextField(15);
        formPanel.add(modelField, gbc);
        
        // Daily Price
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Daily Price:"), gbc);
        gbc.gridx = 1;
        dailyPriceField = new JTextField(15);
        formPanel.add(dailyPriceField, gbc);
        
        // Purchase Price
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Purchase Price:"), gbc);
        gbc.gridx = 1;
        purchasePriceField = new JTextField(15);
        formPanel.add(purchasePriceField, gbc);
        
        // Status (e.g., Available, Rented, Maintenance)
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        statusCombo = new JComboBox<>(new String[] {"Available", "Rented", "Maintenance"});
        formPanel.add(statusCombo, gbc);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addButton = new JButton("Add Car");
        updateButton = new JButton("Update Car");
        deleteButton = new JButton("Delete Car");
        refreshButton = new JButton("Refresh");
        buttonsPanel.add(addButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(refreshButton);
        
        // Table for displaying cars
        tableModel = new DefaultTableModel(
                new Object[]{"CarID", "Year", "Make", "Model", "Daily Price", "Purchase Price", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells read-only
            }
        };
        carsTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(carsTable);
        
        // Add panels to the main panel
        add(formPanel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.CENTER);
        add(tableScroll, BorderLayout.SOUTH);
        
        // Load existing car data into the table
        loadCars();
        
        // Define actions for the buttons
        addButton.addActionListener(e -> addCar());
        updateButton.addActionListener(e -> updateCar());
        deleteButton.addActionListener(e -> deleteCar());
        refreshButton.addActionListener(e -> loadCars());
        
        // Populate the input fields when a row is selected
        carsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && carsTable.getSelectedRow() != -1) {
                int row = carsTable.getSelectedRow();
                try {
                    int year = Integer.parseInt(tableModel.getValueAt(row, 1).toString());
                    yearCombo.setSelectedItem(year);
                } catch (NumberFormatException ex) {}
                makeCombo.setSelectedItem(tableModel.getValueAt(row, 2).toString());
                modelField.setText(tableModel.getValueAt(row, 3).toString());
                dailyPriceField.setText(tableModel.getValueAt(row, 4).toString());
                purchasePriceField.setText(tableModel.getValueAt(row, 5).toString());
                statusCombo.setSelectedItem(tableModel.getValueAt(row, 6).toString());
            }
        });
    }
    
    // Utility method to get array of years from 2000 to current year
    private Integer[] getYearRange() {
        int currentYear = Year.now().getValue();
        Integer[] years = new Integer[currentYear - 2000 + 1];
        for (int i = 0; i <= currentYear - 2000; i++) {
            years[i] = 2000 + i;
        }
        return years;
    }
    
    // Utility method to get popular car brands for "Make"
    private String[] getPopularBrands() {
        return new String[]{
            "Ford", "Mazda", "Toyota", "Suzuki", "Mercedes", 
            "Renault", "Volkswagen", "BMW", "Audi", "Chevrolet"
        };
    }
    
    // Loads cars from the Cars table into the JTable
    private void loadCars() {
        tableModel.setRowCount(0);
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT carID, year, make, model, dailyPrice, purchasePrice, status FROM Cars";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int carID = rs.getInt("carID");
                    int year = rs.getInt("year");
                    String make = rs.getString("make");
                    String model = rs.getString("model");
                    double dailyPrice = rs.getDouble("dailyPrice");
                    double purchasePrice = rs.getDouble("purchasePrice");
                    String status = rs.getString("status");
                    tableModel.addRow(new Object[]{carID, year, make, model, dailyPrice, purchasePrice, status});
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading cars: " + ex.getMessage());
        }
    }
    
    // Inserts a new car into the Cars table
    private void addCar() {
        int year = (int) yearCombo.getSelectedItem();
        String make = makeCombo.getSelectedItem().toString();
        String model = modelField.getText().trim();
        String dailyPriceStr = dailyPriceField.getText().trim();
        String purchasePriceStr = purchasePriceField.getText().trim();
        String status = statusCombo.getSelectedItem().toString();
        
        if (model.isEmpty() || dailyPriceStr.isEmpty() || purchasePriceStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }
        
        double dailyPrice, purchasePrice;
        try {
            dailyPrice = Double.parseDouble(dailyPriceStr);
            purchasePrice = Double.parseDouble(purchasePriceStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for prices.");
            return;
        }
        
        try (Connection connection = DatabaseUtil.getConnection()) {
            String insertQuery = "INSERT INTO Cars (year, make, model, dailyPrice, purchasePrice, status) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(insertQuery)) {
                ps.setInt(1, year);
                ps.setString(2, make);
                ps.setString(3, model);
                ps.setDouble(4, dailyPrice);
                ps.setDouble(5, purchasePrice);
                ps.setString(6, status);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Car added successfully.");
                    loadCars();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add car.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }
    
    // Updates the selected car in the Cars table
    private void updateCar() {
        int selectedRow = carsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a car to update.");
            return;
        }
        
        int carID = (int) tableModel.getValueAt(selectedRow, 0);
        int year = (int) yearCombo.getSelectedItem();
        String make = makeCombo.getSelectedItem().toString();
        String model = modelField.getText().trim();
        String dailyPriceStr = dailyPriceField.getText().trim();
        String purchasePriceStr = purchasePriceField.getText().trim();
        String status = statusCombo.getSelectedItem().toString();
        
        if (model.isEmpty() || dailyPriceStr.isEmpty() || purchasePriceStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }
        
        double dailyPrice, purchasePrice;
        try {
            dailyPrice = Double.parseDouble(dailyPriceStr);
            purchasePrice = Double.parseDouble(purchasePriceStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for prices.");
            return;
        }
        
        try (Connection connection = DatabaseUtil.getConnection()) {
            String updateQuery = "UPDATE Cars SET year = ?, make = ?, model = ?, dailyPrice = ?, purchasePrice = ?, status = ? WHERE carID = ?";
            try (PreparedStatement ps = connection.prepareStatement(updateQuery)) {
                ps.setInt(1, year);
                ps.setString(2, make);
                ps.setString(3, model);
                ps.setDouble(4, dailyPrice);
                ps.setDouble(5, purchasePrice);
                ps.setString(6, status);
                ps.setInt(7, carID);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Car updated successfully.");
                    loadCars();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update car.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }
    
    // Deletes the selected car from the Cars table
    private void deleteCar() {
        int selectedRow = carsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a car to delete.");
            return;
        }
        
        int carID = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the selected car?",
                                                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        try (Connection connection = DatabaseUtil.getConnection()) {
            String deleteQuery = "DELETE FROM Cars WHERE carID = ?";
            try (PreparedStatement ps = connection.prepareStatement(deleteQuery)) {
                ps.setInt(1, carID);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Car deleted successfully.");
                    loadCars();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete car.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }
}
