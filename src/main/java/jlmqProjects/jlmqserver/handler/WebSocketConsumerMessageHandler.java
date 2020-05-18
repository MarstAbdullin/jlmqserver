package jlmqProjects.jlmqserver.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jlmqProjects.jlmqserver.messages.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component("jlmqHandler")
@EnableWebSocket
public class WebSocketConsumerMessageHandler extends TextWebSocketHandler {

    @Autowired
    private ObjectMapper mapper;

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String socketMessage = (String) message.getPayload();
        JsonMessage jsonMessage = mapper.readValue(socketMessage, JsonMessage.class);
        switch (jsonMessage.getHeader()) {
            case "receive" :
                JsonMessage messageToSend;
                JsonTaskMessage jsonTaskMessage = mapper.readValue(socketMessage, JsonTaskMessage.class);
                TaskMessage taskMessage = jsonTaskMessage.getPayload();

                ActionMessage acceptedMessage = ActionMessage.builder()
                        .command("accepted")
                        .messageId(taskMessage.getMessageId())
                        .build();
                messageToSend = JsonMessage.builder()
                        .header("accepted")
                        .payload(acceptedMessage)
                        .build();
                session.sendMessage(new TextMessage(mapper.writeValueAsString(messageToSend)));
                System.out.println(taskMessage.toString());
                ActionMessage completedMessage = ActionMessage.builder()
                        .command("completed")
                        .messageId(taskMessage.getMessageId())
                        .build();
                messageToSend = JsonMessage.builder()
                        .header("completed")
                        .payload(completedMessage)
                        .build();
                session.sendMessage(new TextMessage(mapper.writeValueAsString(messageToSend)));

                break;
        }
    }
}
