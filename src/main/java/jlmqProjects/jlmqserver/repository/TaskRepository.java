package jlmqProjects.jlmqserver.repository;

import org.springframework.data.repository.CrudRepository;
import jlmqProjects.jlmqserver.model.Task;

public interface TaskRepository extends CrudRepository<Task, Integer> {
    Task getByTaskId(String taskId);
}
