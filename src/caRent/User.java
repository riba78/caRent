package caRent;

public abstract class User {
	//Fields
	private int userID;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String email;
	private String passwordHash;
	private boolean termsAccepted;
	private String userType;
	
	
	//Constructor
	public User(int userID, String firstName, String lastName, String phoneNumber, String email, String passwordHash, boolean termsAccepted, String userType) {
		super();
		this.userID = userID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.passwordHash = passwordHash;
		this.termsAccepted = termsAccepted;
		this.userType = userType;
	}

	
	//getters and setters
	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	
	public String getPasswordHash() {
		return passwordHash;
	}

	public boolean isTermsAccepted() {
		return termsAccepted;
	}

	public void setTermsAccepted(boolean termsAccepted) {
		this.termsAccepted = termsAccepted;
	}
	
	public String getUserType() {
		return userType;
	}
	
	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	public void updateProfile(String newUserFirstName, String newUserLastName, String newEmail, String newPhoneNumber) {
		this.firstName = newUserFirstName;
		this.lastName = newUserLastName;
		this.email = newEmail;
		this.phoneNumber = newPhoneNumber;
		System.out.println("Profile updated for user: " + firstName);
	}
	
	public abstract void logout();	
}
















