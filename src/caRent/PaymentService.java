package caRent;

import java.util.List;

public class PaymentService {
    
    /**
     * Processes the payment (simulating gateway integration) and saves it in the database.
     * After a successful process, it updates the reservation status to "PAID" and optionally sends a receipt email.
     * 
     * @param payment The Payment object containing payment details.
     * @return true if the payment is processed and saved successfully; false otherwise.
     */
    public boolean processAndSavePayment(Payment payment) {
        // Simulate payment processing: mark the payment as paid immediately
        payment.setPaid(true);
        
        boolean saved = false;
        try {
            // Save the payment record via PaymentDAO
            saved = PaymentDAO.savePayment(payment);
            if (saved) {
                // Update the corresponding reservation status to "PAID"
                ReservationDAO.updateReservationStatus(payment.getReservationID(), "PAID");
                
                // Optionally, send a receipt or confirmation email
                payment.sendRecieptEmail();
            } else {
                System.err.println("Payment saving failed in PaymentDAO.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            saved = false;
        }
        return saved;
    }
    
    /**
     * Retrieves a list of payments made by a specific user.
     * 
     * @param userID The user's ID.
     * @return A list of Payment objects associated with the user.
     */
    public List<Payment> getPaymentsForUser(int userID) {
        return PaymentDAO.getPaymentsByUserID(userID);
    }
    
    // Additional business methods (such as refund, update, or transaction logging)
    // can be added here if needed.
}
