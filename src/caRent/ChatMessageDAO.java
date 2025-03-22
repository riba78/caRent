package caRent;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChatMessageDAO {

    /**
     * Inserts a new chat message into the database.
     * @param message a ChatMessage object (without messageID).
     * @return true if insertion was successful, false otherwise.
     */
    public static boolean createMessage(ChatMessage message) {
        String sql = "INSERT INTO ChatMessages (fromUserID, toUserID, messageText) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, message.getFromUserID());
            ps.setInt(2, message.getToUserID());
            ps.setString(3, message.getMessageText());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                // Retrieve the generated messageID and sentAt
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        message.setMessageID(rs.getInt(1));
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
     * Retrieves all messages between two users (e.g., customer and service rep).
     * You can order by sentAt ascending or descending.
     */
    public static List<ChatMessage> getMessagesBetween(int userA, int userB) {
        List<ChatMessage> messages = new ArrayList<>();
        String sql = "SELECT messageID, fromUserID, toUserID, messageText, sentAt "
                   + "FROM ChatMessages "
                   + "WHERE (fromUserID = ? AND toUserID = ?) OR (fromUserID = ? AND toUserID = ?) "
                   + "ORDER BY sentAt ASC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userA);
            ps.setInt(2, userB);
            ps.setInt(3, userB);
            ps.setInt(4, userA);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ChatMessage msg = new ChatMessage(
                        rs.getInt("messageID"),
                        rs.getInt("fromUserID"),
                        rs.getInt("toUserID"),
                        rs.getString("messageText"),
                        rs.getTimestamp("sentAt")
                    );
                    messages.add(msg);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return messages;
    }

    /**
     * (Optional) Retrieves all messages for a specific user, 
     * e.g., all messages to or from a service rep.
     */
    public static List<ChatMessage> getMessagesForUser(int userID) {
        List<ChatMessage> messages = new ArrayList<>();
        String sql = "SELECT messageID, fromUserID, toUserID, messageText, sentAt "
                   + "FROM ChatMessages "
                   + "WHERE fromUserID = ? OR toUserID = ? "
                   + "ORDER BY sentAt ASC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userID);
            ps.setInt(2, userID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ChatMessage msg = new ChatMessage(
                        rs.getInt("messageID"),
                        rs.getInt("fromUserID"),
                        rs.getInt("toUserID"),
                        rs.getString("messageText"),
                        rs.getTimestamp("sentAt")
                    );
                    messages.add(msg);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return messages;
    }
}
