package caRent;

import java.util.List;

public class CustomerRepAssignmentDAOTest {
    public static void main(String[] args) {
        int testCustomerID = 1;  // Replace with a valid customer ID from your database.
        int testRepID = 101;       // Replace with a valid rep ID.

        // 1. Assign a rep to a customer.
        boolean assignSuccess = CustomerRepAssignmentDAO.assignRepToCustomer(testCustomerID, testRepID);
        System.out.println("Assign rep result: " + assignSuccess);
        
        // 2. Retrieve and display the assignment for the customer.
        List<CustomerRepAssignment> assignments = CustomerRepAssignmentDAO.getAssignmentsByCustomerID(testCustomerID);
        System.out.println("Assignments for customer " + testCustomerID + ":");
        for (CustomerRepAssignment assignment : assignments) {
            System.out.println(assignment);
        }
        
        // 3. Reassign the customer to a new rep.
        int newRepID = 102; // Replace with a different valid rep ID.
        boolean reassignSuccess = CustomerRepAssignmentDAO.reassignCustomer(testCustomerID, newRepID);
        System.out.println("Reassign rep result: " + reassignSuccess);
        
        // 4. Retrieve and display the updated assignment.
        assignments = CustomerRepAssignmentDAO.getAssignmentsByCustomerID(testCustomerID);
        System.out.println("Updated assignments for customer " + testCustomerID + ":");
        for (CustomerRepAssignment assignment : assignments) {
            System.out.println(assignment);
        }
    }
}
