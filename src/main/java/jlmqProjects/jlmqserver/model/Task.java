package jlmqProjects.jlmqserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String taskId;
    String queueName;
    LocalDateTime created;
    LocalDateTime accepted;
    LocalDateTime completed;

    @Enumerated(EnumType.STRING)
    TaskStatus status;
}
