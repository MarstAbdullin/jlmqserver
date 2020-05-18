package jlmqProjects.jlmqserver.protocol;

import com.fasterxml.jackson.databind.ObjectMapper;
import jlmqProjects.jlmqserver.messages.JsonMessage;
import lombok.NoArgsConstructor;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@NoArgsConstructor
public class JlmqProducer {
    private String queueName;
    private WebSocketSession socketSession;
    private ObjectMapper objectMapper;

    public JlmqProducer(String queueName, WebSocketSession socketSession) {
        this.queueName = queueName;
        this.socketSession = socketSession;
        objectMapper = new ObjectMapper();
    }

    public void send(JsonMessage jsonMessage) {
        try {
            socketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(jsonMessage)));
        } catch (IOException e) {
            System.out.println();
            throw new IllegalArgumentException(e);
        }
    }
}
