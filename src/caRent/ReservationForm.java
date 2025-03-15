package caRent;

import javax.swing.*;
import java.awt.*;
import java.text.*;
import java.util.Date;
import javax.swing.border.Border;

public class ReservationForm extends JFrame {
    private String reservationType;
    private double dailyPrice;
    private double purchasePrice;
    
    private int reservationID; // ID of the current reservation
    private int currentUserID; // Logged-in user ID (passed initially, but we'll check via UserManager)
    private int carID;         // Car's ID for which reservation is being made
    
    private JTextField dailyCostField;
    private JTextField startDateField;
    private JButton startDatePickerButton;
    private JTextField endDateField;
    private JButton endDatePickerButton;
    private JLabel totalCostLabel;
    private JButton calculateButton;
    private JButton paymentButton;
    
    // The same dimension for text fields as in RegistrationForm
    private Dimension fieldSize = new Dimension(200, 25);

    // Simple date format for displaying selected dates
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    /**
     * Constructor for ReservationForm.
     * @param reservationType "rental" or "purchase"
     * @param dailyPrice The daily cost (used for rentals)
     * @param purchasePrice The purchase price (used for purchases)
     * @param reservationID The current reservation's ID (0 if new)
     * @param currentUserID The logged-in user's ID (use a valid ID, or -1 if not logged in)
     * @param carID The ID of the car being reserved
     */
    public ReservationForm(String reservationType, double dailyPrice, double purchasePrice, int reservationID, int currentUserID, int carID) {
        this.reservationType = reservationType;
        this.dailyPrice = dailyPrice;
        this.purchasePrice = purchasePrice;
        this.reservationID = reservationID;
        this.currentUserID = currentUserID;
        this.carID = carID;
        
        setTitle("Reservation Form - " + reservationType);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Use GridBagLayout with a border for consistent spacing
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // ============= ROW 1: DAILY COST =============
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Daily Cost:"), gbc);
        
        gbc.gridx = 1;
        dailyCostField = new JTextField();
        dailyCostField.setPreferredSize(fieldSize);
        if ("rental".equalsIgnoreCase(reservationType)) {
            dailyCostField.setText(String.format("%.2f", dailyPrice));
            dailyCostField.setEditable(false);
        } else {
            // For purchase, daily cost isn't really relevant.
            dailyCostField.setText("N/A");
            dailyCostField.setEditable(false);
        }
        add(dailyCostField, gbc);
        
        // ============= ROW 2: START DATE =============
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Start Date:"), gbc);
        
        gbc.gridx = 1;
        startDateField = new JTextField();
        startDateField.setPreferredSize(fieldSize);
        
        // For purchase, auto-fill today's date (not editable)
        if ("purchase".equalsIgnoreCase(reservationType)) {
            startDateField.setText(dateFormat.format(new Date()));
            startDateField.setEditable(false);
        }
        add(startDateField, gbc);
        
        // Add a button next to the Start Date field to pick a date
        gbc.gridx = 2;
        startDatePickerButton = new JButton("...");
        startDatePickerButton.setEnabled("rental".equalsIgnoreCase(reservationType));
        startDatePickerButton.addActionListener(e -> openDatePicker(startDateField));
        add(startDatePickerButton, gbc);
        
        // ============= ROW 3: END DATE =============
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("End Date:"), gbc);
        
        gbc.gridx = 1;
        endDateField = new JTextField();
        endDateField.setPreferredSize(fieldSize);
        
        // For purchase, auto-fill today's date (not editable)
        if ("purchase".equalsIgnoreCase(reservationType)) {
            endDateField.setText(dateFormat.format(new Date()));
            endDateField.setEditable(false);
        }
        add(endDateField, gbc);
        
        // Add a button next to the End Date field to pick a date
        gbc.gridx = 2;
        endDatePickerButton = new JButton("...");
        endDatePickerButton.setEnabled("rental".equalsIgnoreCase(reservationType));
        endDatePickerButton.addActionListener(e -> openDatePicker(endDateField));
        add(endDatePickerButton, gbc);
        
        // ============= ROW 4: TOTAL COST =============
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Total Cost:"), gbc);

        gbc.gridx = 1;
        totalCostLabel = new JLabel("0.00");
        add(totalCostLabel, gbc);

        // ============= ROW 5: BUTTONS (Calculate, Payment, Save) =============
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        calculateButton = new JButton("Calculate Cost");
        add(calculateButton, gbc);

        gbc.gridx = 1;
        paymentButton = new JButton("Proceed to Payment");
        add(paymentButton, gbc);

        gbc.gridx = 2;
        JButton saveReservationButton = new JButton("Save Reservation");
        add(saveReservationButton, gbc);

        // Action listeners
        calculateButton.addActionListener(e -> calculateTotalCost());
        paymentButton.addActionListener(e -> handlePayment());
        saveReservationButton.addActionListener(e -> saveReservation());
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    // New method for handling payment logic
    private void handlePayment() {
        // Check if a user is logged in (current user ID should be > 0)
        if (UserManager.getCurrentUserID() <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Please log in before proceeding with payment.",
                    "Not Logged In",
                    JOptionPane.WARNING_MESSAGE);
            // Optionally, you can open the Login form here.
            return;
        }
        
        double totalCost;
        try {
            totalCost = Double.parseDouble(totalCostLabel.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid total cost.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create an array of payment options
        String[] paymentOptions = {"Visa", "Isracard", "Cal", "Mastercard"};
        
        // Show a dropdown dialog with these options
        String paymentMethod = (String) JOptionPane.showInputDialog(
                this,
                "Choose Payment Method:",
                "Payment Method",
                JOptionPane.QUESTION_MESSAGE,
                null,
                paymentOptions,
                paymentOptions[0]
        );
        
        // If user cancels or closes the dialog, paymentMethod will be null
        if (paymentMethod == null) {
            return;
        }
        
        // Create a Payment object with isPaid=true (simulate successful payment)
        Payment payment = new Payment(
                0,                          // paymentID (auto-generated by DB)
                reservationID,              // current reservation ID (must be valid after saving)
                UserManager.getCurrentUserID(), // use the logged-in user's ID
                totalCost,
                paymentMethod,
                new Date(),                 // payment date
                true                        // mark as paid immediately
        );
        
        PaymentService paymentService = new PaymentService();
        boolean success = paymentService.processAndSavePayment(payment);
        
        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Payment saved successfully.");
        } else {
            JOptionPane.showMessageDialog(this,
                    "Payment failed. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Calculates and updates the total cost displayed based on the reservation type
    private void calculateTotalCost() {
        if ("purchase".equalsIgnoreCase(reservationType)) {
            totalCostLabel.setText(String.format("%.2f", purchasePrice));
            return;
        }
        
        try {
            Date startDate = dateFormat.parse(startDateField.getText());
            Date endDate = dateFormat.parse(endDateField.getText());
            // Create a dummy Reservation instance to calculate cost.
            Reservation reservation = new Reservation(0, 0, 0, startDate, endDate, 0.0, reservationType);
            double cost = reservation.calculateCost(dailyPrice, purchasePrice);
            totalCostLabel.setText(String.format("%.2f", cost));
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid dates in the format yyyy-MM-dd.", "Date Format Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid Date Range", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Opens a date picker dialog or simulates date selection.
     * Here, we simply set today's date as a placeholder.
     * In the future, this may be replaced with an actual date picker integration.
     */
    private void openDatePicker(JTextField targetField) {
        targetField.setText(dateFormat.format(new Date()));
    }
    
    /**
     * Utility to apply a border to this JFrame content pane.
     */
    private void setBorder(Border border) {
        ((JComponent) getContentPane()).setBorder(border);
    }
    
    /**
     * Saves the reservation after checking for overlapping reservations.
     */
    private void saveReservation() {
        // Check if a user is logged in before saving reservation.
        if (UserManager.getCurrentUserID() <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Please log in before saving a reservation.",
                    "Not Logged In",
                    JOptionPane.WARNING_MESSAGE);
            // Optionally, you can open the Login form here.
            return;
        }
        
        try {
            Date startDate = dateFormat.parse(startDateField.getText());
            Date endDate = dateFormat.parse(endDateField.getText());
            
            // Check for overlapping reservations.
            if (ReservationDAO.hasOverlap(this.carID, startDate, endDate)) {
                JOptionPane.showMessageDialog(this,
                        "The car is already reserved for the selected dates. Please choose different dates.",
                        "Overlap Detected",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Use the total cost from the label.
            double cost = Double.parseDouble(totalCostLabel.getText());
            
            // Create a new Reservation object (reservationID is 0/dummy, will be set by the DB).
            Reservation reservation = new Reservation(0, UserManager.getCurrentUserID(), this.carID, startDate, endDate, cost, reservationType);
            
            // Insert the new reservation into the database.
            int newResID = ReservationDAO.createReservation(reservation);
            if (newResID != -1) {
                reservation.setReservationID(newResID);
                // Update the form-level reservationID so that payment references a valid reservation.
                this.reservationID = newResID;
                JOptionPane.showMessageDialog(this,
                        "Reservation saved successfully with ID: " + newResID,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to save reservation.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid dates in the format yyyy-MM-dd.",
                    "Date Format Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid total cost.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
