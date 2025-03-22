package caRent;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.EndpointConfig;
import java.io.IOException;
import java.util.function.Consumer;

@ClientEndpoint
public class ChatClientEndpoint {

    // A static callback to pass messages back to the UI.
    private static Consumer<String> messageConsumer;

    // Setter for the message callback, which ChatTab will set up.
    public static void setMessageConsumer(Consumer<String> consumer) {
        messageConsumer = consumer;
    }

    private Session session;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        System.out.println("Connected to server. Session ID: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Received: " + message);

        // Forward to the UI callback if it’s set.
        if (messageConsumer != null) {
            messageConsumer.accept(message);
        }
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Connection closed. Session ID: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("Error on session " + session.getId() + ": " + throwable.getMessage());
        throwable.printStackTrace();
    }

    /**
     * Helper method to send messages.
     * (Optional: You can call this if you store a reference to the endpoint.)
     */
    public void sendMessage(String message) throws IOException {
        if (session != null && session.isOpen()) {
            session.getBasicRemote().sendText(message);
        }
    }
}
