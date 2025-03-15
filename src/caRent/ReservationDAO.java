package caRent;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReservationDAO {

    /**
     * Checks if there's an overlapping reservation for the same car within the given date range.
     * Returns true if an overlap exists, false otherwise.
     */
    public static boolean hasOverlap(int carID, Date startDate, Date endDate) {
        // We'll do an inclusive overlap check:
        // Overlap condition: existing.start_date <= new.endDate
        //                    AND existing.end_date >= new.startDate
        String sql = "SELECT COUNT(*) AS count "
                   + "FROM Reservations "
                   + "WHERE car_id = ? "
                   + "  AND (start_date <= ? AND end_date >= ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, carID);
            ps.setDate(2, new java.sql.Date(endDate.getTime()));
            ps.setDate(3, new java.sql.Date(startDate.getTime()));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("count");
                    return (count > 0); // if count > 0, an overlap exists
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Inserts a new reservation record into the database.
     * Returns the generated reservation_id if successful, or -1 if something goes wrong.
     */
    public static int createReservation(Reservation reservation) {
        // Insert the new reservation, defaulting the status to 'PENDING' (or choose your own default)
        String sql = "INSERT INTO Reservations "
                   + "(user_id, car_id, start_date, end_date, total_cost, reservation_type, reservation_status, created_at, updated_at) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, DEFAULT, DEFAULT)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, reservation.getUserID());        // user_id
            ps.setInt(2, reservation.getCarID());         // car_id
            ps.setDate(3, new java.sql.Date(reservation.getStartDate().getTime()));
            ps.setDate(4, new java.sql.Date(reservation.getEndDate().getTime()));
            ps.setDouble(5, reservation.getTotalCost());
            ps.setString(6, reservation.getReservationType());
            ps.setString(7, "PENDING"); // or reservation.getStatus() if you want dynamic

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                // Retrieve the generated reservation_id
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1; // indicates failure
    }
    
    /**
     * Updates the reservation_status column for the given reservation.
     * For example: "PAID", "CANCELED", etc.
     */
    public static void updateReservationStatus(int reservationID, String newStatus) {
        String sql = "UPDATE Reservations SET reservation_status = ?, updated_at = CURRENT_TIMESTAMP WHERE reservation_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, newStatus);
            ps.setInt(2, reservationID);
            ps.executeUpdate();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Retrieves all reservations for a specific car, ordered by start date.
     */
    public static List<Reservation> getReservationsByCar(int carID) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT reservation_id, user_id, car_id, start_date, end_date, total_cost, reservation_type, reservation_status "
                   + "FROM Reservations "
                   + "WHERE car_id = ? "
                   + "ORDER BY start_date";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, carID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Reservation res = new Reservation(
                        rs.getInt("reservation_id"),
                        rs.getInt("user_id"),
                        rs.getInt("car_id"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date"),
                        rs.getDouble("total_cost"),
                        rs.getString("reservation_type")
                    );
                    // If you add 'status' to your Reservation class, set it here:
                    // res.setStatus(rs.getString("reservation_status"));
                    
                    reservations.add(res);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return reservations;
    }
}
