package caRent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;

public class SignInForm extends JPanel {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton signInButton;
    private JLabel statusLabel;
    private Dimension fieldSize = new Dimension(200, 25);

    public SignInForm() {
        // Use GridBagLayout for consistent field sizes
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Email label and field
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1;
        emailField = new JTextField();
        emailField.setPreferredSize(fieldSize);
        add(emailField, gbc);
        
        // Password label and field
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(fieldSize);
        add(passwordField, gbc);
        
        // Sign in button (spanning two columns)
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        signInButton = new JButton("Sign In");
        add(signInButton, gbc);
        
        // Status label
        gbc.gridy++;
        statusLabel = new JLabel("");
        add(statusLabel, gbc);
        
        // Add action listener for sign in button
        signInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                signInUser();
            }
        });
    }
    
    private void signInUser() {
        String email = emailField.getText().trim();
        String plainPassword = new String(passwordField.getPassword());
        
        if (email.isEmpty() || plainPassword.isEmpty()) {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Please fill in all fields.");
            return;
        }
        
        try (Connection connection = DatabaseUtil.getConnection()) {
            User user = UserManager.signIn(email, plainPassword, connection);
            
            if (user != null) {
                // Retrieve MainFrame and update currentUser via setCurrentUser method
                MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(this);
                mainFrame.setCurrentUser(user);
                
                // Optionally clear the input fields after a successful sign-in
                emailField.setText("");
                passwordField.setText("");
                
                statusLabel.setForeground(new Color(0, 128, 0)); // dark green for success
                statusLabel.setText("Sign in successful! Welcome " + user.getFirstName());
            } else {
                statusLabel.setForeground(Color.RED);
                statusLabel.setText("Sign in failed. Check credentials.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Database error: " + ex.getMessage());
        }
    }
}
