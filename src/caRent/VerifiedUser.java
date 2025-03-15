package caRent;
//the VerifiedUser class is an implementation of abstract User class
//the main purpose is to provide an implementation of logout method
//it serves as the default type who has been authenticated through sing in process
//it differentiate between standard customer and users with elevated  like "admin" 
public class VerifiedUser extends User {
	
	//constructor
	public VerifiedUser(int userID, String firstName, String lastName, String phoneNumber, String email, String passwordHash, boolean termsAccepted) {
		//Inherited userType from User class, default role of verified user is "customer"
		super(userID, firstName, lastName, phoneNumber, email, passwordHash, termsAccepted, "customer");
	}
	
	//override of logout method for verified user
	@Override
	public void logout() {
		System.out.println(getFirstName() + " has been logged out");
	}
}
