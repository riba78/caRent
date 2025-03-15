package caRent;

public class ServiceRep extends User {
	
	//additional fields for service representatives
	private int repID;
	private String department;
	
	//explicit constructor for ServiceRep class
	public ServiceRep(int userID, String username, String phoneNumber, String email, String passwordHash, boolean termsAccepted, int repID, String department) {
		super(userID, username, phoneNumber, email, passwordHash, termsAccepted);
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
		System.out.println(getUsername() + " (ServiceRep) has been logged out");
	}
	
	//method for following up with a customer
	//user in this case is a customer
	public void followUp(User user) {
		System.out.println("ServiceRep " + getUsername() + " is following up with user " + user.getUsername());
	}
	
	//the method to handle the support ticket
	public void handleIssue(SupportTicket ticket) {
		System.out.println("ServiceRep " + getUsername() + " is handling ticket: " + ticket.getSubject());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
