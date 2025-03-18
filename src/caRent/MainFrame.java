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

        // Logout action: clear currentUser, update header and remove all dynamic tabs
        logoutButton.addActionListener(e -> {
            // Optionally, call UserManager.signOut(currentUser) here
            setCurrentUser(null);
            removeAdminTabs();
            removeServiceRepTabs();
            removeChatTab();
            removeAssignmentTab();
            updateHeader();
        });

        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        this.tabbedPane = new JTabbedPane();

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
        // Remove dynamic tabs when switching user
        removeAdminTabs();
        removeServiceRepTabs();
        removeChatTab();
        removeAssignmentTab();

        if (user != null) {
            String userType = user.getUserType() == null ? "" : user.getUserType().toLowerCase();

            // Admin check
            if (user instanceof Admin || "admin".equalsIgnoreCase(userType)) {
                addAdminTabs();
            }
            // ServiceRep check
            else if (user instanceof ServiceRep || "servicerep".equalsIgnoreCase(userType)) {
                addServiceRepTabs();
                addChatTab();
            }
            // Customer check
            else if (user instanceof Customer || "customer".equalsIgnoreCase(userType)) {
                addChatTab();
            }
            logoutButton.setVisible(true);
        } else {
            logoutButton.setVisible(false);
        }
        updateHeader();
    }
    
    // Getter for the JTabbedPane
    public JTabbedPane getTabbedPane() {
        return tabbedPane;
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

        // **Add the Rep Assignment tab for admin only**
        addAssignmentTab();
    }

    // Adds serviceRep-specific tabs if they are not already added
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
        ReservationReportForm reportPanel = new ReservationReportForm();
        tabbedPane.addTab("Reservation Report", reportPanel);
    }

    // Adds the Chat tab for customers and service reps
    private void addChatTab() {
        // Check if chat tab is already added
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if ("Chat".equals(tabbedPane.getTitleAt(i))) {
                return;
            }
        }
        ChatTab chatTab = new ChatTab(currentUser);
        JPanel chatWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        chatWrapper.add(chatTab);
        tabbedPane.addTab("Chat", chatWrapper);
    }

    // Adds the Rep Assignment tab for admin users only
    private void addAssignmentTab() {
        // Check if the tab is already added
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if ("Rep Assignment".equals(tabbedPane.getTitleAt(i))) {
                return;
            }
        }
        UserRepAssignmentForm assignmentForm = new UserRepAssignmentForm();
        JPanel assignmentWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        assignmentWrapper.add(assignmentForm);
        tabbedPane.addTab("Rep Assignment", assignmentWrapper);
    }

    // Removes admin-specific tabs
    private void removeAdminTabs() {
        for (int i = tabbedPane.getTabCount() - 1; i >= 0; i--) {
            String title = tabbedPane.getTitleAt(i);
            if ("Manage Cars".equals(title) 
                    || "User Management".equals(title)
                    || "Reservation Report".equals(title)
                    || "Rep Assignment".equals(title)) {
                tabbedPane.removeTabAt(i);
            }
        }
    }

    // Removes serviceRep-specific tabs (if any additional tabs are added)
    private void removeServiceRepTabs() {
        // Currently, serviceRep tabs include the Reservation Report (handled in removeAdminTabs).
        // Add any additional removal logic here if needed.
    }

    // Removes the Chat tab
    private void removeChatTab() {
        for (int i = tabbedPane.getTabCount() - 1; i >= 0; i--) {
            if ("Chat".equals(tabbedPane.getTitleAt(i))) {
                tabbedPane.removeTabAt(i);
            }
        }
    }

    // Removes the Rep Assignment tab
    private void removeAssignmentTab() {
        for (int i = tabbedPane.getTabCount() - 1; i >= 0; i--) {
            if ("Rep Assignment".equals(tabbedPane.getTitleAt(i))) {
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
