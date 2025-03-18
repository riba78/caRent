package caRent;

import javax.swing.*;

public class MainFrameTest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            
            // Create a dummy Customer (ensure the constructor matches your Customer class signature)
            Customer customer = new Customer(21, "John", "Doe", "054-1112222", "john@example.com", "hashPW", true, "DL123", "customer");
            
            // Set current user to the customer
            frame.setCurrentUser(customer);
            
            // Retrieve and print the titles of all tabs for debugging
            JTabbedPane tabbedPane = frame.getTabbedPane();
            System.out.println("Tabs in MainFrame after customer login:");
            for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                System.out.println("Tab " + i + ": " + tabbedPane.getTitleAt(i));
            }
            
            // Show the frame so you can visually inspect the UI as well
            frame.setVisible(true);
        });
    }
}
