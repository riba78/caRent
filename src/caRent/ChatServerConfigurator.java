package caRent;

import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.HandshakeResponse;
import jakarta.websocket.server.ServerEndpointConfig;
import java.util.List;
import java.util.Map;

public class ChatServerConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig sec,
                                HandshakeRequest request,
                                HandshakeResponse response) {
        // Extract "userId" from the query string, e.g. ws://localhost:8025/ws/chat?userId=5
        Map<String, List<String>> params = request.getParameterMap();
        if (params.containsKey("userId")) {
            try {
                int userId = Integer.parseInt(params.get("userId").get(0));
                sec.getUserProperties().put("userId", userId);
            } catch (NumberFormatException e) {
                // Handle parse error if needed
            }
        }
        super.modifyHandshake(sec, request, response);
    }
}
