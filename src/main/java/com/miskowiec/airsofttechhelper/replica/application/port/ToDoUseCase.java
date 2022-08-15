package com.miskowiec.airsofttechhelper.replica.application.port;

import com.miskowiec.airsofttechhelper.replica.domain.ToDo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;

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
        UserDetails user;
    }

    @Value
    class UpdateToDoCommand{
        String title;
        String content;
        Long toDoId;
        UserDetails user;
    }

    @Value
    class UpdateToDoResponse{
        public static UpdateToDoResponse SUCCESS = new UpdateToDoResponse(true, Collections.emptyList(), null);
        boolean success;
        List<String> errors;
        ErrorStatus status;
    }

    @Getter
    @AllArgsConstructor
    enum ErrorStatus{
        FORBIDDEN(HttpStatus.FORBIDDEN),
        NOTFOUND(HttpStatus.NOT_FOUND);
        private final HttpStatus status;
    }
}
