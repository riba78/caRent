package caRent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CarSearchForm extends JPanel {
    private JComboBox<String> yearCombo;
    private JComboBox<String> makeCombo;
    private JComboBox<String> modelCombo;
    private JComboBox<String> statusCombo;
    private JButton searchButton;

    // Table to display results
    private JTable resultsTable;
    private DefaultTableModel tableModel;
    
    // Flag to determine if the current user is admin or service rep.
    private boolean isAdminOrRep = false; // default; can be set via setter/constructor

    public CarSearchForm() {
        setLayout(new BorderLayout(10, 10));
        
        // Create the input panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        
        // Year combo
        inputPanel.add(new JLabel("Year:"));
        yearCombo = new JComboBox<>();
        inputPanel.add(yearCombo);
        
        // Make combo
        inputPanel.add(new JLabel("Make:"));
        makeCombo = new JComboBox<>();
        inputPanel.add(makeCombo);
        
        // Model combo
        inputPanel.add(new JLabel("Model:"));
        modelCombo = new JComboBox<>();
        inputPanel.add(modelCombo);
        
        // Status combo
        inputPanel.add(new JLabel("Status:"));
        statusCombo = new JComboBox<>(new String[] {"Any", "Available", "Rented", "Maintenance"});
        inputPanel.add(statusCombo);
        
        // Search button
        searchButton = new JButton("Search");
        inputPanel.add(searchButton);
        
        // Filler for alignment
        inputPanel.add(new JLabel(""));
        add(inputPanel, BorderLayout.NORTH);
        
        // Set up table model based on user type.
        // For admin/serviceRep, include extra columns (CarID, ReservationID, UserID).
        if (isAdminOrRep) {
            tableModel = new DefaultTableModel(new String[] {
                "CarID", "Year", "Make", "Model", "Daily Price", "Purchase Price", "Status", "ReservationID", "UserID"
            }, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
        } else {
            tableModel = new DefaultTableModel(new String[] {
                "CarID", "Year", "Make", "Model", "Daily Price", "Purchase Price", "Status"
            }, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
        }
        
        resultsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Load distinct year and make values from DB into combos
        loadYearAndMakeOptions();
        makeCombo.addActionListener(e -> updateModelOptions());
        updateModelOptions();
        
        // Search button logic
        searchButton.addActionListener(e -> performSearch());
        
        // Panel with Rental and Purchase buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton rentalButton = new JButton("Rental");
        JButton purchaseButton = new JButton("Purchase");
        actionPanel.add(rentalButton);
        actionPanel.add(purchaseButton);
        add(actionPanel, BorderLayout.SOUTH);
        
        // Action handler for Rental button
        rentalButton.addActionListener(e -> {
            int selectedRow = resultsTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a car from the list for rental.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Retrieve car details from the selected row.
            // For both admin and customer views, CarID is in column 0.
            int carID = (int) tableModel.getValueAt(selectedRow, 0);
            int year = (int) tableModel.getValueAt(selectedRow, 1);
            String make = (String) tableModel.getValueAt(selectedRow, 2);
            String model = (String) tableModel.getValueAt(selectedRow, 3);
            double dailyPrice = (double) tableModel.getValueAt(selectedRow, 4);
            double purchasePrice = (double) tableModel.getValueAt(selectedRow, 5);
            
            // For admin view, retrieve ReservationID and currentUserID; otherwise, use dummy values (0).
            int reservationID = 0;
            int currentUserID = 0;
            if (isAdminOrRep) {
                reservationID = (int) tableModel.getValueAt(selectedRow, 7);
                currentUserID = (int) tableModel.getValueAt(selectedRow, 8);
            }
            
            // Open the ReservationForm as a separate window for a rental, passing the carID.
            ReservationForm reservationForm = new ReservationForm("rental", dailyPrice, purchasePrice, reservationID, currentUserID, carID);
            reservationForm.setVisible(true);
        });
        
        // Action handler for Purchase button (similar logic)
        purchaseButton.addActionListener(e -> {
            int selectedRow = resultsTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a car from the list for purchase.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int carID = (int) tableModel.getValueAt(selectedRow, 0);
            int year = (int) tableModel.getValueAt(selectedRow, 1);
            String make = (String) tableModel.getValueAt(selectedRow, 2);
            String model = (String) tableModel.getValueAt(selectedRow, 3);
            double purchasePrice = (double) tableModel.getValueAt(selectedRow, 5);
            JOptionPane.showMessageDialog(this, "Proceeding with purchase for: " + year + " " + make + " " + model);
            // TODO: Open ReservationForm for purchase as needed.
        });
    }
    
    // Setter for the admin flag (could also be set via constructor)
    public void setAdminOrRep(boolean isAdminOrRep) {
        this.isAdminOrRep = isAdminOrRep;
    }
    
    // Load distinct year and make values from the Cars table
    private void loadYearAndMakeOptions() {
        yearCombo.addItem("Any");
        makeCombo.addItem("Any");
        
        try (Connection connection = DatabaseUtil.getConnection()) {
            String yearQuery = "SELECT DISTINCT year FROM Cars ORDER BY year";
            try (PreparedStatement ps = connection.prepareStatement(yearQuery);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    yearCombo.addItem(String.valueOf(rs.getInt("year")));
                }
            }
            
            String makeQuery = "SELECT DISTINCT make FROM Cars ORDER BY make";
            try (PreparedStatement ps2 = connection.prepareStatement(makeQuery);
                 ResultSet rs2 = ps2.executeQuery()) {
                while (rs2.next()) {
                    makeCombo.addItem(rs2.getString("make"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    // Updates the modelCombo based on the selected make
    private void updateModelOptions() {
        String selectedMake = (String) makeCombo.getSelectedItem();
        modelCombo.removeAllItems();
        modelCombo.addItem("Any");
        
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query;
            if ("Any".equalsIgnoreCase(selectedMake)) {
                query = "SELECT DISTINCT model FROM Cars ORDER BY model";
            } else {
                query = "SELECT DISTINCT model FROM Cars WHERE make = ? ORDER BY model";
            }
            
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                if (!"Any".equalsIgnoreCase(selectedMake)) {
                    ps.setString(1, selectedMake);
                }
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    modelCombo.addItem(rs.getString("model"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    // Perform the search query based on selected filters
    private void performSearch() {
        tableModel.setRowCount(0);
        String selectedYear  = (String) yearCombo.getSelectedItem();
        String selectedMake  = (String) makeCombo.getSelectedItem();
        String selectedModel = (String) modelCombo.getSelectedItem();
        String selectedStatus = (String) statusCombo.getSelectedItem();
        
        String query;
        if (isAdminOrRep) {
            // For admin/serviceRep: Join Cars with Reservations to fetch extra columns.
            query = "SELECT c.carID, c.year, c.make, c.model, c.dailyPrice, c.purchasePrice, c.status, " +
                    "r.reservation_id AS ReservationID, r.user_id AS UserID " +
                    "FROM Cars c LEFT JOIN Reservations r ON c.carID = r.car_id " +
                    "WHERE 1=1";
        } else {
            // For customers: fetch only car details.
            query = "SELECT carID, year, make, model, dailyPrice, purchasePrice, status " +
                    "FROM Cars WHERE 1=1";
        }
        
        if (!"Any".equalsIgnoreCase(selectedYear)) {
            query += " AND year = ?";
        }
        if (!"Any".equalsIgnoreCase(selectedMake)) {
            query += " AND make = ?";
        }
        if (!"Any".equalsIgnoreCase(selectedModel)) {
            query += " AND model = ?";
        }
        if (!"Any".equalsIgnoreCase(selectedStatus)) {
            query += " AND status = ?";
        }
        
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            
            int index = 1;
            if (!"Any".equalsIgnoreCase(selectedYear)) {
                ps.setInt(index++, Integer.parseInt(selectedYear));
            }
            if (!"Any".equalsIgnoreCase(selectedMake)) {
                ps.setString(index++, selectedMake);
            }
            if (!"Any".equalsIgnoreCase(selectedModel)) {
                ps.setString(index++, selectedModel);
            }
            if (!"Any".equalsIgnoreCase(selectedStatus)) {
                ps.setString(index++, selectedStatus);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int carID = rs.getInt("carID");
                    int year = rs.getInt("year");
                    String make = rs.getString("make");
                    String model = rs.getString("model");
                    double dPrice = rs.getDouble("dailyPrice");
                    double pPrice = rs.getDouble("purchasePrice");
                    String status = rs.getString("status");
                    
                    if (isAdminOrRep) {
                        int reservationID = rs.getInt("ReservationID"); // may be 0 if none
                        int userID = rs.getInt("UserID");               // may be 0 if none
                        tableModel.addRow(new Object[]{
                            carID, year, make, model, dPrice, pPrice, status, reservationID, userID
                        });
                    } else {
                        tableModel.addRow(new Object[]{
                            carID, year, make, model, dPrice, pPrice, status
                        });
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
