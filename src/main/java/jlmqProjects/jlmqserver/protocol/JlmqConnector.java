package jlmqProjects.jlmqserver.protocol;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import jlmqProjects.jlmqserver.messages.*;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import jlmqProjects.jlmqserver.protocol.JlmqConsumer;
import jlmqProjects.jlmqserver.protocol.JlmqProducer;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutionException;

@AllArgsConstructor
public class JlmqConnector {
    private static String address;
    private static WebSocketClient webSocketClient;

    public JlmqConnector(String address, WebSocketClient webSocketClient) {
        jlmqProjects.jlmqserver.protocol.JlmqConnector.address = address;
        jlmqProjects.jlmqserver.protocol.JlmqConnector.webSocketClient = webSocketClient;
    }

    public JlmqProducerBuilder producer() {
        return new JlmqProducerBuilder();
    }

    public JlmqConsumerBuilder consumer() {
        return new JlmqConsumerBuilder();
    }


    public static class JlmqProducerBuilder {
        private String queueName;
        private WebSocketHandler webSocketHandler;

        public JlmqProducerBuilder toQueue(String queueName) {
            this.queueName = queueName;
            return this;
        }

        public JlmqProducerBuilder onReceive(WebSocketHandler webSocketHandler) {
            this.webSocketHandler = webSocketHandler;
            return this;
        }

        public JlmqProducer create() {
            try {
                WebSocketSession socketSession = webSocketClient.doHandshake(webSocketHandler, new WebSocketHttpHeaders(), URI.create(address)).get();
                return new JlmqProducer(queueName, socketSession);
            } catch (InterruptedException | ExecutionException e) {
                System.out.println();
                throw new IllegalArgumentException(e);
            }
        }
    }

    public static class JlmqConsumerBuilder {
        private String queueName;
        private WebSocketHandler webSocketHandler;

        public JlmqConsumerBuilder subscribe(String queueName) {
            this.queueName = queueName;
            return this;
        }

        public JlmqConsumerBuilder onReceive(WebSocketHandler webSocketHandler) {
            this.webSocketHandler = webSocketHandler;
            return this;
        }

        public JlmqConsumer create() {
            try {
                WebSocketSession socketSession = webSocketClient.doHandshake(webSocketHandler,
                        new WebSocketHttpHeaders(), URI.create(address)).get();

                SubscribeMessage subscribeMessage = SubscribeMessage.builder()
                        .queueName(queueName)
                        .build();

                JsonMessage jsonMessage = JsonMessage.builder()
                        .header("subscribe")
                        .payload(subscribeMessage)
                        .build();

                ObjectMapper objectMapper = new ObjectMapper();
                socketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(jsonMessage)));
                return new JlmqConsumer(queueName, socketSession);
            } catch (InterruptedException | ExecutionException | IOException e) {
                System.out.println();
                throw new IllegalArgumentException(e);
            }
        }
    }


}
