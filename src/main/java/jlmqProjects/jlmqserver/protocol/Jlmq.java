package jlmqProjects.jlmqserver.protocol;

import org.springframework.web.socket.client.standard.StandardWebSocketClient;


public class Jlmq {

    public static Connector connector() {
        return new Connector();
    }

    public static class Connector {
        private String address;

        public Connector address(String address) {
            this.address = address;
            return this;
        }

        public JlmqConnector connect() {
            return new JlmqConnector(address, new StandardWebSocketClient());
        }
    }
}
