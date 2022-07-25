package com.example.airsofttechhelper.replica.application.port;

import com.example.airsofttechhelper.replica.domain.ToDo;
import lombok.Value;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface ToDoUseCase {
    Optional<ToDo> findOneById(Long id);
    ToDo addToDo(CreateToDoCommand command);
    UpdateToDoResponse updateToDO(UpdateToDoCommand command);

    @Value
    class CreateToDoCommand{
        String title;
        String content;
        Long replicaId;
    }

    @Value
    class UpdateToDoCommand{
        String title;
        String content;
        Long toDoId;
    }

    @Value
    class UpdateToDoResponse{
        public static UpdateToDoResponse SUCCESS = new UpdateToDoResponse(true, Collections.emptyList());
        boolean success;
        List<String> errors;
    }
}
