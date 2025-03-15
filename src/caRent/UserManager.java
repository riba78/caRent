package caRent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

// The main purpose of UserManager is to centralize services that handle all user-related operations:
// it validates new user data, checks for duplicate emails, verifies password strength, hashes the password,
// inserts the new user record in the database, and handles sign in/out operations.

public class UserManager {
    
    // Holds the currently logged in user; null if no user is logged in.
    private static User currentUser = null;
    
    // User Sign Up method
    public static String signUp(String firstName, String lastName, String phoneNumber, String email, 
                                String plainPassword, boolean termsAccepted) {
        // Check if the user with this email already exists
        try (Connection connection = DatabaseUtil.getConnection()) {
            // 1) Check if user already exists
            String checkQuery = "SELECT COUNT(*) FROM Users WHERE email = ?";
            try (PreparedStatement psCheck = connection.prepareStatement(checkQuery)) {
                psCheck.setString(1, email);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return "A user with this email already exists.";
                    }
                }
            }
            // 2) Check if password is strong enough
            if (!isPasswordStrong(plainPassword)) {
                return "Password is not strong enough...";
            }
            // 3) Hash the password
            String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
            // 4) Insert the new user
            String insertQuery = "INSERT INTO Users (firstName, lastName, phoneNumber, email, passwordHash, termsAccepted, userType) "
                               + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(insertQuery)) {
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, phoneNumber);
                ps.setString(4, email);
                ps.setString(5, hashedPassword);
                ps.setBoolean(6, termsAccepted);
                // Default user type is "customer" during sign-up
                ps.setString(7, "customer");
                
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    return "User successfully registered";
                } else {
                    return "Registration failed, please try again";
                }
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "Database error during registration";
        }
    }

    // Method that authenticates the user.
    // On successful authentication, currentUser is set.
    public static User signIn(String email, String plainPassword, Connection connection) {
        String query = "SELECT userID, firstName, lastName, phoneNumber, email, passwordHash, termsAccepted, userType " + 
                       "FROM Users WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("passwordHash");
                    // Verify the password using BCrypt.
                    if (BCrypt.checkpw(plainPassword, storedHash)) {
                        int userID = rs.getInt("userID");
                        String firstName = rs.getString("firstName");  
                        String lastName = rs.getString("lastName");
                        String phoneNumber = rs.getString("phoneNumber");
                        boolean termsAccepted = rs.getBoolean("termsAccepted");
                        
                        // Trim userType to avoid trailing spaces
                        String userType = rs.getString("userType");
                        if (userType != null) {
                            userType = userType.trim();
                        }
                        
                        User user;
                        if ("admin".equalsIgnoreCase(userType)) {
                            // The default employeeID is 0 for now.
                            user = new Admin(userID, firstName, lastName, phoneNumber, email, storedHash, termsAccepted, 0);
                        } else {
                            // Returns a verified instance of User.
                            user = new VerifiedUser(userID, firstName, lastName, phoneNumber, email, storedHash, termsAccepted);
                        }
                        
                        // Set the current logged in user.
                        currentUser = user;
                        return user;
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    // Logout method: clears the current user.
    public static void signOut(User user) {
        if (user != null) {
            user.logout();
        }
        currentUser = null;
    }
    
    // Returns the currently logged-in user, or null if no user is logged in.
    public static User getCurrentUser() {
        return currentUser;
    }
    
    // Returns the current user's ID if logged in; otherwise, returns -1.
    public static int getCurrentUserID() {
        return (currentUser != null) ? currentUser.getUserID() : -1;
    }
    
    // Method that checks password strength.
    public static boolean isPasswordStrong(String password) {
        if (password.length() < 8) return false;
        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;
        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) hasUpper = true;
            else if (Character.isLowerCase(ch)) hasLower = true;
            else if (Character.isDigit(ch)) hasDigit = true;
            else if ("!@#$%^&*()_+-=[]{}|;':\",.<>/?".indexOf(ch) >= 0) hasSpecial = true;
        }
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }	
}
