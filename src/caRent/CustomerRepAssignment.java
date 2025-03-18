package caRent;

import java.sql.Timestamp;

public class CustomerRepAssignment {
    private int assignmentID;
    private int customerID;
    private int repID;
    private Timestamp assignedAt;
    
    public CustomerRepAssignment(int assignmentID, int customerID, int repID, Timestamp assignedAt) {
        this.assignmentID = assignmentID;
        this.customerID = customerID;
        this.repID = repID;
        this.assignedAt = assignedAt;
    }
    
    // Getters and setters
    public int getAssignmentID() {
        return assignmentID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public int getRepID() {
        return repID;
    }

    public Timestamp getAssignedAt() {
        return assignedAt;
    }
    
    @Override
    public String toString() {
        return "CustomerRepAssignment{" +
               "assignmentID=" + assignmentID +
               ", customerID=" + customerID +
               ", repID=" + repID +
               ", assignedAt=" + assignedAt +
               '}';
    }
}
