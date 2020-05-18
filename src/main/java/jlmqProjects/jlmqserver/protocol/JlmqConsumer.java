package jlmqProjects.jlmqserver.protocol;

import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

@NoArgsConstructor
public class JlmqConsumer {
    private String queueName;
    private WebSocketSession socketSession;

    public JlmqConsumer(String queueName, WebSocketSession socketSession) {
        this.queueName = queueName;
        this.socketSession = socketSession;
    }
}
