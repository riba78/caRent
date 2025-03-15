package caRent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegistrationForm extends JPanel {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JPasswordField passwordField; 
    private JPasswordField confirmPasswordField;
    private JCheckBox termsCheckBox;        
    private JButton registerButton;
    private JLabel statusLabel;
    
    // Define a uniform field size (adjust the width/height as needed)
    private Dimension fieldSize = new Dimension(200, 25);

    public RegistrationForm() {
        // Use a border and GridBagLayout for finer control
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // First Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("First Name:"), gbc);
        
        gbc.gridx = 1;
        firstNameField = new JTextField();
        firstNameField.setPreferredSize(fieldSize);
        add(firstNameField, gbc);
        
        // Last Name
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Last Name:"), gbc);
        
        gbc.gridx = 1;
        lastNameField = new JTextField();
        lastNameField.setPreferredSize(fieldSize);
        add(lastNameField, gbc);
        
        // Phone Number
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Phone Number:"), gbc);
        
        gbc.gridx = 1;
        phoneField = new JTextField();
        phoneField.setPreferredSize(fieldSize);
        add(phoneField, gbc);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1;
        emailField = new JTextField();
        emailField.setPreferredSize(fieldSize);
        add(emailField, gbc);
        
        // Password
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(fieldSize);
        add(passwordField, gbc);
        
        // Confirm Password
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Confirm Password:"), gbc);
        
        gbc.gridx = 1;
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setPreferredSize(fieldSize);
        add(confirmPasswordField, gbc);
        
        // Terms acceptance
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Accept Terms:"), gbc);
        
        gbc.gridx = 1;
        termsCheckBox = new JCheckBox("I agree to the terms.");
        add(termsCheckBox, gbc);
        
        // Register button
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        registerButton = new JButton("Register");
        add(registerButton, gbc);
        
        // Status label
        gbc.gridy++;
        statusLabel = new JLabel("");
        add(statusLabel, gbc);
        
        // Register action
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String plainPassword = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        boolean termsAccepted = termsCheckBox.isSelected();

        if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || email.isEmpty() 
                || plainPassword.isEmpty() || confirmPassword.isEmpty()) {
            statusLabel.setText("Please fill in all required fields.");
            return;
        }
        
        if (!plainPassword.equals(confirmPassword)) {
            statusLabel.setText("Passwords do not match!");
            return;
        }
        
        if (!termsAccepted) {
            statusLabel.setText("You must accept the terms to register.");
            return;
        }
        
        String result = UserManager.signUp(firstName, lastName, phone, email, plainPassword, termsAccepted);
        statusLabel.setText(result);
    }
}
