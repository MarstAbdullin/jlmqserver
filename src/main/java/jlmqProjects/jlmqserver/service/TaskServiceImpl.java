package jlmqProjects.jlmqserver.service;

import jlmqProjects.jlmqserver.model.Task;
import jlmqProjects.jlmqserver.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Override
    public void save(Task task) {
        taskRepository.save(task);
    }

    @Override
    public Task getByTaskId(String taskId) {
        return taskRepository.getByTaskId(taskId);
    }
}
