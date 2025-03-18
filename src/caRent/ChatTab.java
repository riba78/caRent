package caRent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatTab extends JPanel {
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JButton startChatButton;  // Only visible for customers
    private ChatHandler chatHandler;
    
    // We'll store the current user's ID and type for decision-making.
    private int currentUserID;
    private String currentUserType; // e.g., "customer" or "serviceRep"
    
    public ChatTab(User currentUser) {
        this.chatHandler = new ChatHandler();
        this.currentUserID = currentUser.getUserID();
        this.currentUserType = currentUser.getUserType().toLowerCase();
        
        setLayout(new BorderLayout(10, 10));
        
        // Create a text area to display chat messages
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);
        
        // Create an input panel at the bottom for sending messages
        JPanel inputPanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        sendButton = new JButton("Send");
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);
        
        // Create a top panel with a "Start Chat" button (only for customers)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        if (currentUserType.equals("customer")) {
            startChatButton = new JButton("Start Chat");
            topPanel.add(startChatButton);
        }
        add(topPanel, BorderLayout.NORTH);
        
        // Action for the "Start Chat" button (only for customers)
        if (currentUserType.equals("customer")) {
            startChatButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // This triggers automatic assignment and starts the chat session.
                    chatHandler.handleChatRequest(currentUserID);
                    chatArea.append("Chat session started with your assigned service rep.\n");
                    // Optionally disable the button after starting the chat.
                    startChatButton.setEnabled(false);
                }
            });
        }
        
        // Common action for sending messages (simulate sending for now)
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = messageField.getText().trim();
                if (!msg.isEmpty()) {
                    chatArea.append("Me: " + msg + "\n");
                    messageField.setText("");
                    // In a real system, here you would send the message via WebSockets or similar.
                }
            }
        });
    }
}
