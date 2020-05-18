package jlmqProjects.jlmqserver.controller;

import jlmqProjects.jlmqserver.protocol.Jlmq;
import jlmqProjects.jlmqserver.protocol.JlmqConnector;
import jlmqProjects.jlmqserver.protocol.JlmqConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.socket.WebSocketHandler;

@Controller
public class ConsumerController {

    @Autowired
    @Qualifier("jlmqHandler")
    WebSocketHandler webSocketHandler;

    @GetMapping("/consumer")
    public ModelAndView getConsumerPage() {
        return new ModelAndView("consumer");
    }

    @PostMapping("/consumer")
    public ModelAndView createConsumer(@RequestParam("queueName") String queueName) {
        System.out.println(queueName + ", Сазадаю те кансумера");
        JlmqConnector connector = Jlmq.connector()
                .address("ws://localhost:8080/jlmq")
                .connect();
        JlmqConsumer consumer = connector.consumer()
                .subscribe(queueName)
                .onReceive(webSocketHandler)
                .create();
        return new ModelAndView("redirect:/produce");
    }
}
