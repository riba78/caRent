package caRent;

/**
 * Utility class that provides a shared method to connect to the database.
 * Used throughout the DAO layer to ensure consistent and reusable database access.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
	private static final String URL = "jdbc:mysql://localhost:3306/caRent?useSSL=false&serverTimezone=UTC";
	private static final String USER = "root"; //root user
	private static final String PASSWORD = ""; //no password
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
	
}
