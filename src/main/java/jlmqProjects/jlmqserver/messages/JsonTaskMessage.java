package jlmqProjects.jlmqserver.messages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonTaskMessage {
    String header;
    TaskMessage payload;
}
