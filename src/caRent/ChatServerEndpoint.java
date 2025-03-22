package caRent;

import com.google.gson.Gson; // We'll use Gson to parse JSON
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/ws/chat", configurator = ChatServerConfigurator.class)
public class ChatServerEndpoint {

    // Maps userId -> Session
    private static final Map<Integer, Session> userIdToSession = new ConcurrentHashMap<>();
    // Maps Session -> userId (reverse lookup)
    private static final Map<Session, Integer> sessionToUserId = new ConcurrentHashMap<>();

    private static final Gson gson = new Gson();

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        // Our configurator will have parsed the query param "userId" and stored it in userProperties.
        Object userIdObj = config.getUserProperties().get("userId");
        if (userIdObj instanceof Integer) {
            int userId = (Integer) userIdObj;
            userIdToSession.put(userId, session);
            sessionToUserId.put(session, userId);
            System.out.println("New client connected: sessionID=" + session.getId() + ", userId=" + userId);
        } else {
            System.err.println("No userId found in query parameters. Closing session.");
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "Missing userId param"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Incoming message is assumed to be JSON:
     * {
     *   "fromUserID": 5,
     *   "toUserID": 7,
     *   "messageText": "Hello!"
     * }
     */
    @OnMessage
    public void onMessage(String jsonMessage, Session session) {
        try {
            // Parse JSON to get fromUserID, toUserID, messageText
            ChatPayload payload = gson.fromJson(jsonMessage, ChatPayload.class);

            // 1) Store in DB
            ChatMessage chatMsg = new ChatMessage(payload.getFromUserID(), payload.getToUserID(), payload.getMessageText());
            ChatMessageDAO.createMessage(chatMsg);

            // 2) Forward to recipient if connected
            Session recipientSession = userIdToSession.get(payload.getToUserID());
            if (recipientSession != null && recipientSession.isOpen()) {
                recipientSession.getBasicRemote().sendText("From " + payload.getFromUserID() + ": " + payload.getMessageText());
            }

            // 3) Optionally echo back to sender
            session.getBasicRemote().sendText("Me: " + payload.getMessageText());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        Integer userId = sessionToUserId.remove(session);
        if (userId != null) {
            userIdToSession.remove(userId);
            System.out.println("Client disconnected: userId=" + userId + ", session=" + session.getId());
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error for session " + session.getId() + ": " + throwable);
    }

    /**
     * A simple helper class to parse JSON payloads.
     */
    private static class ChatPayload {
        private int fromUserID;
        private int toUserID;
        private String messageText;

        public int getFromUserID() { return fromUserID; }
        public int getToUserID() { return toUserID; }
        public String getMessageText() { return messageText; }
    }
}
