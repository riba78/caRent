package caRent;

//Admin class represents user with elevated privileges
//if differers from standard customer in terms of allowing the system to give him an access to
//managing cars, overseeing reservations, updating user role and generating reports

public class Admin extends User{
	
	//additional Admin field
	private int employeeID;

	
	//constructor for Admin class
	public Admin(int userID, String firstName, String lastName, String phoneNumber, String email, String passwordHash,
			boolean termsAccepted, int employeeID) {
		super(userID, firstName, lastName, phoneNumber, email, passwordHash, termsAccepted, "admin");
		// TODO Auto-generated constructor stub
		this.employeeID = employeeID;	
	}


	public int getEmployeeID() {
		return employeeID;
	}


	public void setEmployeeID(int employeeID) {
		this.employeeID = employeeID;
	}
	
	//override of logout method
	@Override
	public void logout() {
		System.out.println(getFirstName() + " (Admin) has been logged out" );
	}	
}
