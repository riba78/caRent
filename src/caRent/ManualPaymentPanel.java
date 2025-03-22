package caRent;

/**
 * Form panel (JPanel) that allows ServiceRep or Admin users to manually enter payments.
 * Used when external or cash payments need to be logged in the system.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class ManualPaymentPanel extends JPanel {
    private JTextField reservationIDField;
    private JTextField userIDField;
    private JTextField amountField;
    private JComboBox<String> paymentMethodBox;
    private JCheckBox isPaidBox;
    private JButton submitButton;

    public ManualPaymentPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Reservation ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Reservation ID:"), gbc);
        gbc.gridx = 1;
        reservationIDField = new JTextField(15);
        add(reservationIDField, gbc);

        // User ID
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("User ID:"), gbc);
        gbc.gridx = 1;
        userIDField = new JTextField(15);
        add(userIDField, gbc);

        // Amount
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1;
        amountField = new JTextField(15);
        add(amountField, gbc);

        // Payment Method
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Payment Method:"), gbc);
        gbc.gridx = 1;
        paymentMethodBox = new JComboBox<>(new String[]{"Cash", "Bank Transfer", "Visa", "Mastercard"});
        add(paymentMethodBox, gbc);

        // Is Paid
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Mark as Paid:"), gbc);
        gbc.gridx = 1;
        isPaidBox = new JCheckBox("Paid");
        isPaidBox.setSelected(true);
        add(isPaidBox, gbc);

        // Submit Button
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        submitButton = new JButton("Submit Payment");
        add(submitButton, gbc);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleManualPayment();
            }
        });
    }

    private void handleManualPayment() {
        try {
            int reservationID = Integer.parseInt(reservationIDField.getText().trim());
            int userID = Integer.parseInt(userIDField.getText().trim());
            double amount = Double.parseDouble(amountField.getText().trim());
            String method = (String) paymentMethodBox.getSelectedItem();
            boolean isPaid = isPaidBox.isSelected();

            Payment payment = new Payment(
                0,
                reservationID,
                userID,
                amount,
                method,
                new Date(),
                isPaid
            );
            payment.setGatewayTransactionId("manual");

            boolean success = PaymentDAO.savePayment(payment);
            if (success) {
                if (isPaid) {
                    ReservationDAO.updateReservationStatus(reservationID, "PAID");
                }
                JOptionPane.showMessageDialog(this, "Manual payment saved successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save payment.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
