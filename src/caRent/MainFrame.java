package caRent;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private User currentUser;            // holds the currently logged-in user
    private JTabbedPane tabbedPane;      // instance variable to be used by all methods
    private JPanel headerPanel;          // shows welcome info and logout button
    private JLabel welcomeLabel;         // displays welcome message
    private JButton logoutButton;

    // Constructor for MainFrame class
    public MainFrame() {
        setTitle("Car Rental System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Use BorderLayout so header can sit at the top
        setLayout(new BorderLayout());

        // Header Panel Setup
        headerPanel = new JPanel(new BorderLayout());
        // Add some empty border to give space around the label and button
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        welcomeLabel = new JLabel("Welcome, customer"); // default
        logoutButton = new JButton("Logout");
        logoutButton.setVisible(false); // hidden by default when no one is logged in

        // Logout action: clear currentUser, update header and remove admin tabs if any
        logoutButton.addActionListener(e -> {
            // Optionally, call UserManager.signOut(currentUser) here
            setCurrentUser(null);
            // Remove admin-specific tabs if present
            removeAdminTabs();
            updateHeader();
        });

        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        this.tabbedPane = new JTabbedPane();

        // Wrap each form in a panel using FlowLayout so they keep their preferred size
        // Registration tab
        RegistrationForm registrationForm = new RegistrationForm();
        JPanel regWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        regWrapper.add(registrationForm);
        tabbedPane.addTab("Register", regWrapper);

        // Sign In tab
        SignInForm signInForm = new SignInForm();
        JPanel signInWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        signInWrapper.add(signInForm);
        tabbedPane.addTab("Sign In", signInWrapper);

        // Car Search tab
        CarSearchForm carSearchForm = new CarSearchForm();
        JPanel carSearchWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        carSearchWrapper.add(carSearchForm);
        tabbedPane.addTab("Car Search", carSearchWrapper);

        add(tabbedPane, BorderLayout.CENTER);

        setSize(1000, 800); // Make the window larger so all tabs are easily visible
        setLocationRelativeTo(null); // Center the frame on screen
    }

    // Getter for currentUser
    public User getCurrentUser() {
        return currentUser;
    }

    // Setter for currentUser; updates UI based on user role
    public void setCurrentUser(User user) {
        this.currentUser = user;
        if (user != null) {
            // Check user type via instanceof for more robust role detection
            if (user instanceof Admin) {
                addAdminTabs();
            } else if (user instanceof ServiceRep) {
                addServiceRepTabs();
            }
            logoutButton.setVisible(true);
        } else {
            // When no user is logged in, hide the logout button and remove admin/serviceRep tabs
            logoutButton.setVisible(false);
            removeAdminTabs();
        }
        updateHeader();
    }

    // Updates the header label based on currentUser state
    private void updateHeader() {
        if (currentUser == null) {
            welcomeLabel.setText("Welcome, customer");
        } else {
            welcomeLabel.setText("Welcome, " + currentUser.getFirstName() + " - Role: " + currentUser.getUserType());
        }
    }

    // Adds admin-specific tabs if they are not already added
    private void addAdminTabs() {
        // Check if admin tabs are already added
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            String title = tabbedPane.getTitleAt(i);
            if ("Manage Cars".equals(title)) {
                return; // Admin tabs already exist
            }
        }

        // Add a tab for managing cars
        JPanel manageCarsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        manageCarsPanel.add(new ManageCarsForm());
        tabbedPane.addTab("Manage Cars", manageCarsPanel);

        // Add a tab for user management
        JPanel userManagementPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        userManagementPanel.add(new UserManagementForm());
        tabbedPane.addTab("User Management", userManagementPanel);

        // Add Reservation Report tab
        addReservationReportTab();
    }

    // Optionally add serviceRep-specific tabs (if different from admin)
    private void addServiceRepTabs() {
        // For simplicity, we'll add the Reservation Report tab for service reps as well
        addReservationReportTab();
    }

    // Adds the Reservation Report tab if not already added
    private void addReservationReportTab() {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            String title = tabbedPane.getTitleAt(i);
            if ("Reservation Report".equals(title)) {
                return; // Reservation Report tab already exists
            }
        }
        // Create an instance of ReservationReportForm (ensure that this class exists)
        ReservationReportForm reportPanel = new ReservationReportForm();
        tabbedPane.addTab("Reservation Report", reportPanel);
    }

    // Removes admin/serviceRep-specific tabs when logging out
    private void removeAdminTabs() {
        // Remove by checking the tab titles
        for (int i = tabbedPane.getTabCount() - 1; i >= 0; i--) {
            String title = tabbedPane.getTitleAt(i);
            if ("Manage Cars".equals(title) 
                    || "User Management".equals(title)
                    || "Reservation Report".equals(title)) {
                tabbedPane.removeTabAt(i);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
