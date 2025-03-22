package caRent;

/**
 * Admin and ServiceRep panel (JPanel) to display all reservations for a selected car.
 * Useful for reviewing booking history or identifying overlaps.
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ReservationReportForm extends JPanel {
    private JTextField carIDField;
    private JButton searchButton;
    private JTable reservationsTable;
    private DefaultTableModel tableModel;

    public ReservationReportForm() {
        // Use a BorderLayout for the panel
        setLayout(new BorderLayout(10, 10));
        
        // Input panel for car ID
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Enter Car ID:"));
        
        carIDField = new JTextField(10);
        inputPanel.add(carIDField);
        
        searchButton = new JButton("Search");
        inputPanel.add(searchButton);
        
        add(inputPanel, BorderLayout.NORTH);
        
        // Table model and table
        tableModel = new DefaultTableModel(new String[] {
            "Reservation ID", "User ID", "Car ID", "Start Date", "End Date", "Total Cost", "Type"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        reservationsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(reservationsTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Button action: search reservations for the entered carID
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchReservations();
            }
        });
    }
    
    /**
     * Searches for reservations by car ID and displays them in the table.
     */
    private void searchReservations() {
        String carIDText = carIDField.getText().trim();
        if (carIDText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Car ID", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int carID;
        try {
            carID = Integer.parseInt(carIDText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Car ID. Please enter a numeric value.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Retrieve reservations for this car using ReservationDAO
        List<Reservation> reservations = ReservationDAO.getReservationsByCar(carID);
        tableModel.setRowCount(0); // Clear existing rows
        
        if (reservations.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No reservations found for Car ID: " + carID,
                "Information",
                JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            for (Reservation res : reservations) {
                tableModel.addRow(new Object[] {
                    res.getReservationID(),
                    res.getUserID(),
                    res.getCarID(),
                    res.getStartDate(),
                    res.getEndDate(),
                    res.getTotalCost(),
                    res.getReservationType()
                });
            }
        }
    }
}
