package caRent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

public class UserManagementForm extends JPanel {
    // Form fields for user details
    private JTextField firstNameField, lastNameField, emailField, phoneField;
    private JComboBox<String> roleCombo;
    private JCheckBox activeCheckBox; // Indicates if the user is active
    private JButton addButton, updateButton, deactivateButton, deleteButton, refreshButton;
    private JTable userTable;
    private DefaultTableModel tableModel;
    
    public UserManagementForm() {
        setLayout(new BorderLayout());
        
        // Form panel with input fields
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // First Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        firstNameField = new JTextField(15);
        formPanel.add(firstNameField, gbc);
        
        // Last Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        lastNameField = new JTextField(15);
        formPanel.add(lastNameField, gbc);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(15);
        formPanel.add(emailField, gbc);
        
        // Phone
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(15);
        formPanel.add(phoneField, gbc);
        
        // Role Combo Box (e.g., customer, serviceRep, admin)
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        roleCombo = new JComboBox<>(new String[] {"customer", "serviceRep", "admin"});
        formPanel.add(roleCombo, gbc);
        
        // Active Check Box
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Active:"), gbc);
        gbc.gridx = 1;
        activeCheckBox = new JCheckBox("Active");
        activeCheckBox.setSelected(true);
        formPanel.add(activeCheckBox, gbc);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addButton = new JButton("Add User");
        updateButton = new JButton("Update Role");
        deactivateButton = new JButton("Deactivate User");
        deleteButton = new JButton("Delete User");
        refreshButton = new JButton("Refresh");
        buttonsPanel.add(addButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(deactivateButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(refreshButton);
        
        // Table for displaying users
        tableModel = new DefaultTableModel(
                new Object[]{"UserID", "First Name", "Last Name", "Email", "Phone", "Role", "Active"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells read-only
            }
        };
        userTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(userTable);
        
        // Add panels to main panel
        add(formPanel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.CENTER);
        add(tableScroll, BorderLayout.SOUTH);
        
        loadUsers();  // Load existing users into the table
        
        // Button actions
        addButton.addActionListener(e -> addUser());
        updateButton.addActionListener(e -> updateUserRole());
        deactivateButton.addActionListener(e -> deactivateUser());
        deleteButton.addActionListener(e -> deleteUser());
        refreshButton.addActionListener(e -> loadUsers());
        
        // Populate form fields when a row is selected in the table
        userTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && userTable.getSelectedRow() != -1) {
                int row = userTable.getSelectedRow();
                firstNameField.setText(tableModel.getValueAt(row, 1).toString());
                lastNameField.setText(tableModel.getValueAt(row, 2).toString());
                emailField.setText(tableModel.getValueAt(row, 3).toString());
                phoneField.setText(tableModel.getValueAt(row, 4).toString());
                roleCombo.setSelectedItem(tableModel.getValueAt(row, 5).toString());
                activeCheckBox.setSelected(Boolean.parseBoolean(tableModel.getValueAt(row, 6).toString()));
            }
        });
    }
    
    // Loads users from the database into the JTable
    private void loadUsers() {
        tableModel.setRowCount(0);
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT userID, firstName, lastName, email, phoneNumber, userType, active FROM Users";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int userID = rs.getInt("userID");
                    String firstName = rs.getString("firstName");
                    String lastName = rs.getString("lastName");
                    String email = rs.getString("email");
                    String phone = rs.getString("phoneNumber");
                    String role = rs.getString("userType");
                    boolean active = rs.getBoolean("active");
                    tableModel.addRow(new Object[]{userID, firstName, lastName, email, phone, role, active});
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading users: " + ex.getMessage());
        }
    }
    
    // Adds a new user to the database
    private void addUser() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String role = roleCombo.getSelectedItem().toString();
        boolean active = activeCheckBox.isSelected();
        
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }
        
        // For admin-created accounts, we assign a default password (hashed).
        // In a real system, you'd notify the user to change this.
        String defaultPasswordHash = BCrypt.hashpw("defaultPass123", BCrypt.gensalt());
        
        try (Connection connection = DatabaseUtil.getConnection()) {
            String insertQuery = "INSERT INTO Users (firstName, lastName, email, phoneNumber, passwordHash, termsAccepted, userType, active) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(insertQuery)) {
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, email);
                ps.setString(4, phone);
                ps.setString(5, defaultPasswordHash);
                ps.setBoolean(6, true); // Assume terms accepted
                ps.setString(7, role);
                ps.setBoolean(8, active);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "User added successfully.");
                    loadUsers();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add user.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }
    
    // Updates the privilege role for the selected user
    private void updateUserRole() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to update.");
            return;
        }
        int userID = (int) tableModel.getValueAt(selectedRow, 0);
        String newRole = roleCombo.getSelectedItem().toString();
        
        try (Connection connection = DatabaseUtil.getConnection()) {
            String updateQuery = "UPDATE Users SET userType = ? WHERE userID = ?";
            try (PreparedStatement ps = connection.prepareStatement(updateQuery)) {
                ps.setString(1, newRole);
                ps.setInt(2, userID);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "User role updated successfully.");
                    loadUsers();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update user role.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }
    
    // Deactivates the selected user (sets active = false)
    private void deactivateUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to deactivate.");
            return;
        }
        int userID = (int) tableModel.getValueAt(selectedRow, 0);
        try (Connection connection = DatabaseUtil.getConnection()) {
            String updateQuery = "UPDATE Users SET active = false WHERE userID = ?";
            try (PreparedStatement ps = connection.prepareStatement(updateQuery)) {
                ps.setInt(1, userID);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "User deactivated successfully.");
                    loadUsers();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to deactivate user.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }
    
    // Deletes the selected user from the database
    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.");
            return;
        }
        int userID = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?",
                                                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        try (Connection connection = DatabaseUtil.getConnection()) {
            String deleteQuery = "DELETE FROM Users WHERE userID = ?";
            try (PreparedStatement ps = connection.prepareStatement(deleteQuery)) {
                ps.setInt(1, userID);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "User deleted successfully.");
                    loadUsers();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete user.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }
}
