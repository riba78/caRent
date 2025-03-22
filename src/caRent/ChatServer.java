package caRent;

import org.glassfish.tyrus.server.Server;
import java.util.HashMap;

public class ChatServer {
    private Server server;

    public void startServer() {
        try {
            // Use a Tyrus constructor with a Map<String, Object> for server properties (can be empty)
            // and varargs of endpoint classes.
            server = new Server(
                "localhost", 
                8025, 
                "/ws", 
                new HashMap<String, Object>(), 
                ChatServerEndpoint.class // varargs -> you can list multiple endpoints
            );

            server.start();
            System.out.println("WebSocket server started at ws://localhost:8025/ws/chat");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        if (server != null) {
            server.stop();
            System.out.println("WebSocket server stopped.");
        }
    }
}
