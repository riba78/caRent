package caRent;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    /**
     * Saves a Payment record to the database.
     *
     * @param payment The Payment object to be saved.
     * @return true if the operation was successful; false otherwise.
     */
    public static boolean savePayment(Payment payment) {
        String sql = "INSERT INTO Payments (reservationID, userID, amount, paymentMethod, paymentDate, isPaid, gatewayTransactionID, createdAt, updatedAt) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, DEFAULT, DEFAULT)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
             
            // Set parameters for the prepared statement
            stmt.setInt(1, payment.getReservationID());    // Reservation ID associated with the payment
            stmt.setInt(2, payment.getUserID());             // User/Customer ID making the payment
            stmt.setDouble(3, payment.getAmount());
            stmt.setString(4, payment.getPaymentMethod());
            stmt.setTimestamp(5, new Timestamp(payment.getPaymentDate().getTime()));
            stmt.setBoolean(6, payment.isPaid());
            stmt.setString(7, payment.getGatewayTransactionId());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Retrieve and set the generated payment ID
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int generatedId = rs.getInt(1);
                        payment.setPaymentID(generatedId);
                    }
                }
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    /**
     * Retrieves all Payment records for a given user.
     *
     * @param userID The ID of the user.
     * @return A List of Payment objects.
     */
    public static List<Payment> getPaymentsByUserID(int userID) {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM Payments WHERE userID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, userID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Payment payment = new Payment(
                        rs.getInt("paymentID"),
                        rs.getInt("reservationID"),
                        rs.getInt("userID"),
                        rs.getDouble("amount"),
                        rs.getString("paymentMethod"),
                        rs.getTimestamp("paymentDate"),
                        rs.getBoolean("isPaid")
                    );
                    // Set the gateway transaction ID if available
                    payment.setGatewayTransactionId(rs.getString("gatewayTransactionID"));
                    payments.add(payment);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return payments;
    }
    
    // Additional methods (like updatePayment or refundPayment) can be added here if needed.
}
