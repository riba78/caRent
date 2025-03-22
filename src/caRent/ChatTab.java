package caRent;

/**
 * Chat panel (JPanel) that provides messaging functionality between customers and service reps.
 * Uses WebSocket client for real-time communication.
 */

import jakarta.websocket.ContainerProvider;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class ChatTab extends JPanel {
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JButton startChatButton;  // Only visible for customers

    private Session session;

    // Current user details
    private int currentUserID;
    private String currentUserType; // e.g., "customer" or "serviceRep"

    public ChatTab(User currentUser) {
        this.currentUserID = currentUser.getUserID();
        this.currentUserType = currentUser.getUserType().toLowerCase();

        setLayout(new BorderLayout(10, 10));

        // -- Make the chat area bigger --
        // Setting rows/columns ensures the text area has an initial size.
        // Also, set line wrap for a better user experience if desired.
        chatArea = new JTextArea(/* rows */ 15, /* columns */ 50);
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);

        // Wrap the chat area in a scroll pane and give it a preferred size.
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        add(scrollPane, BorderLayout.CENTER);

        // Input panel for sending messages
        JPanel inputPanel = new JPanel(new BorderLayout());

        // -- Make the message field bigger --
        // specify columns preferred size:
        messageField = new JTextField(/* columns */ 40);
        // messageField.setPreferredSize(new Dimension(400, 30)); // Alternative approach

        sendButton = new JButton("Send");
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        // Top panel with "Start Chat" button for customers
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        if (currentUserType.equals("customer")) {
            startChatButton = new JButton("Start Chat");
            topPanel.add(startChatButton);
        }
        add(topPanel, BorderLayout.NORTH);

        // Set up the message callback so ChatClientEndpoint can notify this UI
        ChatClientEndpoint.setMessageConsumer(msg ->
            SwingUtilities.invokeLater(() -> appendMessage(msg))
        );

        // Connect to WebSocket server in a background thread
        connectToWebSocket();

        // Action for the "Start Chat" button (for customers)
        if (currentUserType.equals("customer")) {
            startChatButton.addActionListener(e -> {
                appendMessage("Chat session started with your assigned service rep.");
                startChatButton.setEnabled(false);
            });
        }

        // Action for sending messages
        sendButton.addActionListener(e -> {
            String msg = messageField.getText().trim();
            if (!msg.isEmpty()) {
                try {
                    if (session != null && session.isOpen()) {
                        session.getBasicRemote().sendText(msg);
                    }
                    appendMessage("Me: " + msg);
                    messageField.setText("");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(ChatTab.this,
                            "Failed to send message.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * Connects to the WebSocket server in a background thread
     * so it doesn't freeze the UI.
     */
    private void connectToWebSocket() {
        new Thread(() -> {
            try {
                WebSocketContainer container = ContainerProvider.getWebSocketContainer();
                session = container.connectToServer(ChatClientEndpoint.class, URI.create("ws://localhost:8025/ws/chat"));
            } catch (Exception ex) {
                ex.printStackTrace();
                SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(ChatTab.this,
                            "Unable to connect to the chat server.", "Connection Error", JOptionPane.ERROR_MESSAGE)
                );
            }
        }).start();
    }

    /**
     * Helper method to append a message to the chat area.
     */
    private void appendMessage(String message) {
        chatArea.append(message + "\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }
}
