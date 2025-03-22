package caRent;

/**
 * ServiceRep and Admin panel (JPanel) to view customers assigned to a specific rep.
 * Lists customers and allows reps to view their reservations and payments.
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AssignedCustomersForm extends JPanel {
    private ServiceRep currentRep;
    private JTable customersTable;
    private DefaultTableModel tableModel;

    public AssignedCustomersForm(ServiceRep rep) {
        this.currentRep = rep;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Assigned Customers"));

        // Table headers
        String[] columnNames = {"Customer ID", "First Name", "Last Name", "Email", "Phone"};

        // Set up table model
        tableModel = new DefaultTableModel(columnNames, 0);
        customersTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(customersTable);
        add(scrollPane, BorderLayout.CENTER);

        // Load customers from DB
        loadAssignedCustomers();

        // View Details button
        JButton viewDetailsButton = new JButton("View Transactions");
        viewDetailsButton.addActionListener(e -> showCustomerTransactions());
        add(viewDetailsButton, BorderLayout.SOUTH);
    }

    private void loadAssignedCustomers() {
        tableModel.setRowCount(0); // Clear table

        List<User> assignedCustomers = CustomerRepAssignmentDAO.getCustomersForRep(currentRep.getUserID());

        for (User user : assignedCustomers) {
            tableModel.addRow(new Object[]{
                user.getUserID(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber()
            });
        }
    }

    private void showCustomerTransactions() {
        int selectedRow = customersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer first.");
            return;
        }

        int customerID = (int) tableModel.getValueAt(selectedRow, 0);
        String firstName = (String) tableModel.getValueAt(selectedRow, 1);
        String lastName = (String) tableModel.getValueAt(selectedRow, 2);

        // Fetch payments and reservations
        List<Payment> payments = PaymentDAO.getPaymentsByUserID(customerID);
        List<Reservation> reservations = ReservationDAO.getReservationsByUserID(customerID);

        StringBuilder info = new StringBuilder();
        info.append("Transactions for ").append(firstName).append(" ").append(lastName).append(":\n\n");

        info.append("Reservations:\n");
        for (Reservation r : reservations) {
            info.append("• ID: ").append(r.getReservationID())
                .append(", Car ID: ").append(r.getCarID())
                .append(", Cost: ").append(r.getTotalCost()).append("\n");
        }

        info.append("\nPayments:\n");
        for (Payment p : payments) {
            info.append("• Amount: ").append(p.getAmount())
                .append(", Method: ").append(p.getPaymentMethod())
                .append(", Paid: ").append(p.isPaid()).append("\n");
        }

        JTextArea area = new JTextArea(info.toString());
        area.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        JOptionPane.showMessageDialog(this, scrollPane, "Customer Transactions", JOptionPane.INFORMATION_MESSAGE);
    }
}
