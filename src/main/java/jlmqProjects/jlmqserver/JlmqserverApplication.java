package jlmqProjects.jlmqserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import jlmqProjects.jlmqserver.messages.TaskMessage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

@SpringBootApplication
public class JlmqserverApplication {

    @Bean
    public Map<String, Queue<TaskMessage>> queues() {
        return new HashMap<>();
    }

    @Bean
    public Map<String, WebSocketSession> webSocketSessionMap() {
        return new HashMap<>();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(JlmqserverApplication.class, args);
    }

}
