package demo.production.WebsocketTutorial.controller;

import demo.production.WebsocketTutorial.model.ChatMessage;
import demo.production.WebsocketTutorial.model.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessageSendingOperations sendingOperations;

    @EventListener
    public void handleWebSocketConnectListener(final SessionConnectedEvent event){
        LOGGER.info("Wow, we have a new connection here!");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(final SessionDisconnectEvent event){
        final StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        final String username = headerAccessor.getSessionAttributes().get("username").toString();

        final ChatMessage chatMessage = ChatMessage.builder()
                        .type(MessageType.DISCONNECT)
                                .sender(username)
                                        .build();

        sendingOperations.convertAndSend("/topic/public", chatMessage);
    }
}
