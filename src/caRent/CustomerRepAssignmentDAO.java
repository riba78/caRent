package caRent;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepAssignmentDAO {

    /**
     * Assigns a rep to a customer by inserting a new record into CustomerRepAssignments.
     *
     * @param customerID The userID of the customer
     * @param repID      The userID of the service rep
     * @return true if insertion was successful; false otherwise
     */
    public static boolean assignRepToCustomer(int customerID, int repID) {
        String sql = "INSERT INTO CustomerRepAssignments (customerID, repID) VALUES (?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, customerID);
            ps.setInt(2, repID);
            
            int rowsAffected = ps.executeUpdate();
            return (rowsAffected > 0);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    /**
     * Retrieves all assignments, for admin to see all (customer <-> rep) pairs.
     * Returns a list of CustomerRepAssignment objects.
     */
    public static List<CustomerRepAssignment> getAllAssignments() {
        List<CustomerRepAssignment> assignments = new ArrayList<>();
        String sql = "SELECT assignmentID, customerID, repID, assignedAt FROM CustomerRepAssignments";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                CustomerRepAssignment assignment = new CustomerRepAssignment(
                    rs.getInt("assignmentID"),
                    rs.getInt("customerID"),
                    rs.getInt("repID"),
                    rs.getTimestamp("assignedAt")
                );
                assignments.add(assignment);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return assignments;
    }
    
    /**
     * Retrieves all assignments for a specific service rep.
     * Returns a list of CustomerRepAssignment objects.
     */
    public static List<CustomerRepAssignment> getAssignmentsByRepID(int repID) {
        List<CustomerRepAssignment> assignments = new ArrayList<>();
        String sql = "SELECT assignmentID, customerID, repID, assignedAt FROM CustomerRepAssignments "
                   + "WHERE repID = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, repID);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CustomerRepAssignment assignment = new CustomerRepAssignment(
                        rs.getInt("assignmentID"),
                        rs.getInt("customerID"),
                        rs.getInt("repID"),
                        rs.getTimestamp("assignedAt")
                    );
                    assignments.add(assignment);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return assignments;
    }
    /**
     * Fetch Assignment Data:
	 *The method looks up the database for any rows in the CustomerRepAssignments table where the customerID matches the provided value.
     * It returns a list of CustomerRepAssignment objects. Each object represents one record (or assignment)
     */
    public static List<CustomerRepAssignment> getAssignmentsByCustomerID(int customerID) {
        List<CustomerRepAssignment> assignments = new ArrayList<>();
        String sql = "SELECT assignmentID, customerID, repID, assignedAt FROM CustomerRepAssignments WHERE customerID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CustomerRepAssignment assignment = new CustomerRepAssignment(
                        rs.getInt("assignmentID"),
                        rs.getInt("customerID"),
                        rs.getInt("repID"),
                        rs.getTimestamp("assignedAt")
                    );
                    assignments.add(assignment);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return assignments;
    }

    //This method updates the existing assignment record for the customer with a new serviceRep
    public static boolean reassignCustomer(int customerID, int newRepID) {
        String sql = "UPDATE CustomerRepAssignments SET repID = ? WHERE customerID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newRepID);
            ps.setInt(2, customerID);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
   
    // Optionally, add methods like removeAssignment(...) or updateAssignment(...) if needed.
}
