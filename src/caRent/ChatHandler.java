package caRent;

import java.util.List;

public class ChatHandler {

    /**
     * Picks an available service rep.
     * For now, this is a stub that returns a dummy repID.
     * In a real system, you’d query rep availability or use a load-balancing algorithm.
     */
    private int pickAvailableRep() {
        // TODO: Replace with actual rep selection logic.
        return 101; // Example repID
    }
    
    /**
     * Handles a chat request from a customer.
     * This method automatically assigns an available rep if the customer has no assignment,
     * or reassigns if needed, and then opens a chat session.
     *
     * @param customerID The userID of the customer initiating the chat.
     */
    public void handleChatRequest(int customerID) {
        int chosenRepID = pickAvailableRep();
        
        // Check if the customer already has an assignment
        List<CustomerRepAssignment> existing = CustomerRepAssignmentDAO.getAssignmentsByCustomerID(customerID);
        if (existing.isEmpty()) {
            // No assignment exists – insert a new record
            boolean success = CustomerRepAssignmentDAO.assignRepToCustomer(customerID, chosenRepID);
            if (!success) {
                System.out.println("Error assigning rep for customer " + customerID);
                return;
            }
        } else {
            // An assignment exists – update it (if desired)
            boolean success = CustomerRepAssignmentDAO.reassignCustomer(customerID, chosenRepID);
            if (!success) {
                System.out.println("Error reassigning rep for customer " + customerID);
                return;
            }
        }
        
        // Open a chat session between customer and rep
        openChatSession(customerID, chosenRepID);
    }
    
    /**
     * Opens a chat session between the customer and the chosen rep.
     * This is a stub – implement your real-time chat logic here (using WebSockets, for example).
     *
     * @param customerID The customer's userID.
     * @param repID      The assigned rep's userID.
     */
    private void openChatSession(int customerID, int repID) {
        // TODO: Replace with your chat session logic.
        System.out.println("Opening chat session between customer " + customerID + " and rep " + repID);
    }
    
    /**
     * Allows an admin to manually reassign a customer to a new service rep.
     *
     * @param customerID The customer's userID.
     * @param newRepID   The new service rep's userID.
     */
    public void adminReassignCustomer(int customerID, int newRepID) {
        boolean success = CustomerRepAssignmentDAO.reassignCustomer(customerID, newRepID);
        if (success) {
            System.out.println("Customer " + customerID + " has been reassigned to rep " + newRepID);
        } else {
            System.out.println("Failed to reassign customer " + customerID);
        }
    }
}
