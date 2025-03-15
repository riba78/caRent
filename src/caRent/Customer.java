package caRent;

public class Customer extends User{
	
	//additional field for Customer class
	private String driverLicense;

	
	//constructor
	public Customer(int userID, String username, String phoneNumber, String email, String passwordHash,
			boolean termsAccepted, String driverLicense) {
		super(userID, username, phoneNumber, email, passwordHash, termsAccepted);
		this.driverLicense = driverLicense;
	}


	public String getDriverLicense() {
		return driverLicense;
	}


	public void setDriverLicense(String driverLicense) {
		this.driverLicense = driverLicense;
	}


	@Override
	public void logout() {
		// TODO Auto-generated method stub
		System.out.println(getUsername() + " (Customer) has been logged out");
	}
}
