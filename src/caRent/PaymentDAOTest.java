package caRent;

import java.util.List;

public class PaymentDAOTest {
    public static void main(String[] args) {
        // Set a known customer ID (adjust this to match a customer in your database)
        int testCustomerID = 9;

        // Fetch the payments for the specified customer ID
        List<Payment> payments = PaymentDAO.getPaymentsByUserID(testCustomerID);

        // Print the results
        System.out.println("Payments for customer ID " + testCustomerID + ":");
        if (payments.isEmpty()) {
            System.out.println("No payments found.");
        } else {
            for (Payment payment : payments) {
                System.out.println(payment);
            }
        }
    }
}