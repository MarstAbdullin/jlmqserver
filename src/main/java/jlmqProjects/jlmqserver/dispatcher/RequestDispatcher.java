package jlmqProjects.jlmqserver.dispatcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jlmqProjects.jlmqserver.messages.*;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import jlmqProjects.jlmqserver.model.Task;
import jlmqProjects.jlmqserver.model.TaskStatus;
import jlmqProjects.jlmqserver.service.TaskService;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

@Component
public class RequestDispatcher {

    private final Map<String, WebSocketSession> consumers;
    private final Map<String, Queue<TaskMessage>> queues;
    private final ObjectMapper objectMapper;
    private final TaskService taskService;
    private final TaskManager taskManager;

    public RequestDispatcher(Map<String, WebSocketSession> consumers, Map<String, Queue<TaskMessage>> queues, ObjectMapper objectMapper, TaskService taskService, TaskManager taskManager) {
        this.consumers = consumers;
        this.queues = queues;
        this.objectMapper = objectMapper;
        this.taskService = taskService;
        this.taskManager = taskManager;
    }

    public void dispatch(WebSocketMessage<?> message, WebSocketSession socketSession) throws JsonProcessingException {

        String socketMessage = (String) message.getPayload();
        JsonMessage jsonMessage = objectMapper.readValue(socketMessage, JsonMessage.class);

        switch (jsonMessage.getHeader()) {

            case "completed":
                System.out.println("Completed");
                TaskMessage completeTaskMessage = objectMapper.readValue(socketMessage, JsonTaskMessage.class).getPayload();
                Task completeTask = taskService.getByTaskId(completeTaskMessage.getMessageId());
                completeTask.setStatus(TaskStatus.COMPLETED);
                completeTask.setCompleted(LocalDateTime.now());
                taskService.save(completeTask);
                if (!queues.get(completeTask.getQueueName()).isEmpty()) {
                    taskManager.sendTask(completeTask.getQueueName());
                }
                break;

            case "subscribe":
                SubscribeMessage subscribeMessage = objectMapper.readValue(socketMessage, JsonSubscribeMessage.class).getPayload();
                consumers.put(subscribeMessage.getQueueName(), socketSession);
                if (queues.containsKey(subscribeMessage.getQueueName()) && !queues.get(subscribeMessage.getQueueName()).isEmpty()) {
                    taskManager.sendTask(subscribeMessage.getQueueName());
                }
                break;

            case "accepted":
                TaskMessage acceptedTaskMessage = objectMapper.readValue(socketMessage, JsonTaskMessage.class).getPayload();
                Task task = taskService.getByTaskId(acceptedTaskMessage.getMessageId());
                task.setStatus(TaskStatus.ACCEPTED);
                task.setAccepted(LocalDateTime.now());
                taskService.save(task);
                break;

            case "send":
                TaskMessage taskMessage = objectMapper.readValue(socketMessage, JsonTaskMessage.class).getPayload();
                taskMessage.setCommand("receive");
                taskMessage.setMessageId(UUID.randomUUID().toString());
                taskService.save(Task.builder()
                        .queueName(taskMessage.getQueueName())
                        .taskId(taskMessage.getMessageId())
                        .status(TaskStatus.CREATED)
                        .created(LocalDateTime.now())
                        .build());
                queues.get(taskMessage.getQueueName()).add(taskMessage);
                if (consumers.containsKey(taskMessage.getQueueName())) {
                    taskManager.sendTask(taskMessage.getQueueName());
                }
                break;

        }
    }
}
