package caRent;

/**
 * Represents a service representative who provides personal support to customers.
 * Inherits from User and adds support-specific operations like following up on reservations.
 */

import java.util.List;

public class ServiceRep extends User {
	
	//additional fields for service representatives
	private int repID;
	private String department;
	
	//explicit constructor for ServiceRep class
	public ServiceRep(int userID, String firstName, String lastName, String phoneNumber, String email, String passwordHash,
			boolean termsAccepted, String userType, int repID, String department) {
		super(userID, firstName, lastName, phoneNumber, email, passwordHash, termsAccepted, userType);
		this.repID = repID;
		this.department = department;
	}

	public int getRepID() {
		return repID;
	}

	public void setRepID(int repID) {
		this.repID = repID;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	@Override
	public void logout() {
		// TODO Auto-generated method stub
		System.out.println(getFirstName() + " (ServiceRep) has been logged out");
	}
	
	//method for following up with a customer
	//user in this case is a customer
	public void followUp(User user) {
	    if (user instanceof Customer) {
	        Customer customer = (Customer) user;
	        System.out.println("ServiceRep " + getFirstName() + " is following up with customer " + customer.getFirstName());
	        
	        // Fetch the customer's payment records using PaymentDAO
	        // (Make sure PaymentDAO.getPaymentsByUserID is implemented and works correctly.)
	        List<Payment> payments = PaymentDAO.getPaymentsByUserID(customer.getUserID());
	        
	        // Check if payments exist and display them
	        if (payments.isEmpty()) {
	            System.out.println("No payment records found for customer " + customer.getFirstName());
	        } else {
	            System.out.println("Payment records for customer " + customer.getFirstName() + ":");
	            for (Payment payment : payments) {
	                System.out.println(payment);
	            }
	        }
	        
	        // Future expansions can include retrieving reservations or support tickets similarly.
	    } else {
	        System.out.println("User is not a customer. Follow-up not applicable.");
	    }
	}

	
	//the method to handle the support ticket
	public void handleIssue(SupportTicket ticket) {
		System.out.println("ServiceRep " + getFirstName() + " is handling ticket: " + ticket.getSubject());
	}
	
}