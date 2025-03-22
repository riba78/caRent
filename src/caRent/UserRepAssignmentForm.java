package caRent;

/**
 * Admin-only panel (JPanel) to assign or reassign service representatives to customers.
 * Displays all users and allows linking customers to reps using CustomerRepAssignmentDAO.
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserRepAssignmentForm extends JPanel {
    private JTable assignmentTable;
    private DefaultTableModel tableModel;
    
    public UserRepAssignmentForm() {
        setLayout(new BorderLayout(10, 10));
        
        // Table model
        tableModel = new DefaultTableModel(new String[] {
            "Assignment ID", "Customer ID", "Rep ID", "Assigned At"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        assignmentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(assignmentTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Example: A button to refresh or assign new reps
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadAssignments());
        buttonPanel.add(refreshButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Initially load data
        loadAssignments();
    }
    
    private void loadAssignments() {
        // Clear current rows
        tableModel.setRowCount(0);
        
        // Example: fetch all assignments from your linking table
        List<CustomerRepAssignment> all = CustomerRepAssignmentDAO.getAllAssignments();
        
        for (CustomerRepAssignment assignment : all) {
            tableModel.addRow(new Object[] {
                assignment.getAssignmentID(),
                assignment.getCustomerID(),
                assignment.getRepID(),
                assignment.getAssignedAt()
            });
        }
    }
}
