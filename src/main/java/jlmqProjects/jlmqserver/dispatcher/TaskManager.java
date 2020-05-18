package jlmqProjects.jlmqserver.dispatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import jlmqProjects.jlmqserver.messages.JsonMessage;
import jlmqProjects.jlmqserver.messages.TaskMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.Queue;

@Component
public class TaskManager {
    @Autowired
    private Map<String, WebSocketSession> consumers;

    @Autowired
    private Map<String, Queue<TaskMessage>> queues;

    @Autowired
    private ObjectMapper mapper;

    public void sendTask(String queueName) {
        try {
            String message = mapper.writeValueAsString(JsonMessage.builder()
                    .header("receive")
                    .payload(queues.get(queueName).poll())
                    .build());

            TextMessage textMessage = new TextMessage(message);
            consumers.get(queueName).sendMessage(textMessage);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
