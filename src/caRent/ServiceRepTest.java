package caRent;

public class ServiceRepTest {
    public static void main(String[] args) {
        // 1. Create a ServiceRep (hard-coded or retrieved from DB)
        ServiceRep rep = new ServiceRep(
            999,                 // userID
            "repJohn",           // firstName
            "repJohnovich",      // lastName
            "054-5551111",       // phoneNumber
            "john@service.com",  // email
            "someHash",          // passwordHash
            true,                // termsAccepted
            "servicerep",        //serviceRep
            101,                 // repID
            "Support"            // department
        );
        
        // 2. Create a Customer object with a known userID that has payments
        //    (In a real scenario, you might load the customer from DB)
        Customer customer = new Customer(
            26,                  // userID (must match a DB record)
            "cust26",            // firstName
            "custich26",         // lastName
            "054-9991111",       // phoneNumber
            "cust26@example.com",// email
            "hashPW",            // passwordHash
            true,                // termsAccepted
            "D12345",            // driverLicense
            "customer"           //customer
        );
        
        // 3. Assign the service rep to the customer (if desired)
        customer.setAssignedRep(rep);
        
        // 4. Call followUp to see the payment history
        System.out.println("----- Testing followUp -----");
        rep.followUp(customer);
        
        // Optionally, you can do more tests or validations here
    }
}
