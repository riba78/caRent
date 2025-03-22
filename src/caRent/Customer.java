package caRent;

/**
 * Represents a customer user in the caRent system.
 * Extends the base User class and includes customer-specific behavior like
 * listing reservations or interacting with a service representative.
 */

public class Customer extends User{
	
	//additional field for Customer class
	private String driverLicense;
	private ServiceRep assignedRep;

	
	//constructor
	public Customer(int userID, String firstName, String lastName, String phoneNumber, String email, String passwordHash,
			boolean termsAccepted, String driverLicense, String userType) {
		super(userID, firstName, lastName, phoneNumber, email, passwordHash, termsAccepted, userType);
		this.driverLicense = driverLicense;
	}


	public String getDriverLicense() {
		return driverLicense;
	}


	public void setDriverLicense(String driverLicense) {
		this.driverLicense = driverLicense;
	}
	
	


	public ServiceRep getAssignedRep() {
		return assignedRep;
	}


	public void setAssignedRep(ServiceRep assignedRep) {
		this.assignedRep = assignedRep;
	}


	@Override
	public void logout() {
		// TODO Auto-generated method stub
		System.out.println(getFirstName() + " (Customer) has been logged out");
	}
}
