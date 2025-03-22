package caRent;

import java.sql.Timestamp;

public class ChatMessage {
    private int messageID;
    private int fromUserID;
    private int toUserID;
    private String messageText;
    private Timestamp sentAt;

    // Constructor (usually for reading from DB)
    public ChatMessage(int messageID, int fromUserID, int toUserID, String messageText, Timestamp sentAt) {
        this.messageID = messageID;
        this.fromUserID = fromUserID;
        this.toUserID = toUserID;
        this.messageText = messageText;
        this.sentAt = sentAt;
    }

    // Overloaded constructor (if needed for creating new messages before insertion)
    public ChatMessage(int fromUserID, int toUserID, String messageText) {
        this.fromUserID = fromUserID;
        this.toUserID = toUserID;
        this.messageText = messageText;
    }

    // Getters and setters
    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public int getFromUserID() {
        return fromUserID;
    }

    public void setFromUserID(int fromUserID) {
        this.fromUserID = fromUserID;
    }

    public int getToUserID() {
        return toUserID;
    }

    public void setToUserID(int toUserID) {
        this.toUserID = toUserID;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Timestamp getSentAt() {
        return sentAt;
    }

    public void setSentAt(Timestamp sentAt) {
        this.sentAt = sentAt;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
               "messageID=" + messageID +
               ", fromUserID=" + fromUserID +
               ", toUserID=" + toUserID +
               ", messageText='" + messageText + '\'' +
               ", sentAt=" + sentAt +
               '}';
    }
}
