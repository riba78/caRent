package caRent;

import java.util.Date;

public class Payment {
    private int paymentID;
    private int reservationID;
    private int userID;
    private double amount;
    private String paymentMethod; // e.g., "Visa", "Mastercard", "Isracard", etc.
    private Date paymentDate;
    private boolean isPaid;
    // New field for future payment gateway integration
    private String gatewayTransactionId;
    
    /**
     * Constructor for Payment.
     * @param paymentID Auto-generated payment ID.
     * @param reservationID ID of the associated reservation.
     * @param userID ID of the user making the payment.
     * @param amount The amount paid.
     * @param paymentMethod The payment method used.
     * @param paymentDate The date and time of payment.
     * @param isPaid Payment status.
     */
    public Payment(int paymentID, int reservationID, int userID, double amount, String paymentMethod, Date paymentDate, boolean isPaid) {
        this.paymentID = paymentID;
        this.reservationID = reservationID;
        this.userID = userID;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.isPaid = isPaid;
    }
    
    // Getters and setters for reservationID and userID
    public int getReservationID() {
        return reservationID;
    }
    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    
    // Getter and Setter for gatewayTransactionId
    public String getGatewayTransactionId() {
        return gatewayTransactionId;
    }
    public void setGatewayTransactionId(String gatewayTransactionId) {
        this.gatewayTransactionId = gatewayTransactionId;
    }
    
    // Other getters and setters
    public int getPaymentID() {
        return paymentID;
    }
    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public String getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public Date getPaymentDate() {
        return paymentDate;
    }
    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
    public boolean isPaid() {
        return isPaid;
    }
    public void setPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }
    
    /**
     * Processes the payment.
     * For now, it simulates a successful payment by:
     * - Setting the current date as the payment date.
     * - Marking the payment as processed (isPaid true).
     * - Assigning a dummy gateway transaction ID.
     * 
     * TODO: Integrate with a real payment gateway in the future.
     */
    public boolean processPayment() {
        paymentDate = new Date(); // Set current date as payment date
        isPaid = true;
        // Simulate a gateway transaction ID
        gatewayTransactionId = "TXN" + System.currentTimeMillis();
        System.out.println("Payment " + paymentID + " processed successfully on " + paymentDate);
        return true;
    }
    
    // Refunds the payment if it was processed
    public boolean refundPayment() {
        if (isPaid) {
            isPaid = false;
            System.out.println("Payment " + paymentID + " refunded successfully");
            return true;
        } else {
            System.out.println("Payment " + paymentID + " refund failed, payment not processed");
            return false;
        }
    }
    
    // Sends a receipt email (placeholder)
    public void sendRecieptEmail() {
        System.out.println("Reciept email sent for payment: " + paymentID);
    }
    
    // Calculates the due amount (if payment hasn't been processed, the due equals the total amount)
    public double calculateDueAmount() {
        double due = isPaid ? 0 : amount;
        System.out.println("Due amount for payment " + paymentID + " is: " + due);
        return due;
    }
    
    // Sends a payment reminder (placeholder)
    public void sendPaymentReminder() {
        System.out.println("This is payment reminder for payment: " + paymentID);
    }
    
    @Override
    public String toString() {
        return "Payment [paymentID=" + paymentID + ", reservationID=" + reservationID + ", userID=" + userID +
               ", amount=" + amount + ", paymentMethod=" + paymentMethod + ", paymentDate=" + paymentDate +
               ", isPaid=" + isPaid + ", gatewayTransactionId=" + gatewayTransactionId + "]";
    }
}