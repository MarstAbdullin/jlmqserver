package jlmqProjects.jlmqserver.service;

import jlmqProjects.jlmqserver.model.Task;

public interface TaskService {
    void save(Task task);
    Task getByTaskId(String taskId);
}
