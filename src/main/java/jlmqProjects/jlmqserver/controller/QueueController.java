package jlmqProjects.jlmqserver.controller;

import jlmqProjects.jlmqserver.messages.TaskMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;

@Controller
public class QueueController {

    @Autowired
    private Map<String, Queue<TaskMessage>> queues;

    @GetMapping("/queue")
    public ModelAndView getQueuePage() {
        return new ModelAndView("queue");
    }

    @PostMapping("/queue")
    public ModelAndView createQueue(@RequestParam("queueName") String queueName) {
        queues.put(queueName, new ArrayDeque<>());
        System.out.println(queues.toString());
        return new ModelAndView("redirect:/consumer");
    }
}
