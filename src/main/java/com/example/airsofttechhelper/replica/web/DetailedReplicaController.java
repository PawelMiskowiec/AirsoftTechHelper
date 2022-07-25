package com.example.airsofttechhelper.replica.web;

import com.example.airsofttechhelper.replica.application.port.ReplicaUseCase;
import com.example.airsofttechhelper.replica.application.port.ToDoUseCase;
import com.example.airsofttechhelper.replica.domain.ToDo;
import com.example.airsofttechhelper.replica.web.dto.RestDetailedReplica;
import com.example.airsofttechhelper.replica.web.dto.RestToDo;
import com.example.airsofttechhelper.web.CreatedURI;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.net.URI;

@RestController
@RequestMapping("/replica")
@AllArgsConstructor
public class DetailedReplicaController {

    private final ReplicaUseCase replicaService;
    private final ToDoUseCase toDoService;

    @GetMapping("/{id}")
    public ResponseEntity<RestDetailedReplica> getById(@PathVariable Long id) {
        RestDetailedReplica restDetailedReplica = replicaService.findById(id);
        URI uri = new CreatedURI("/replica/" + id).uri();
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/todo")
    public ResponseEntity<Object> addToDo(@Valid @RequestBody RestToDoCommand command) {
        ToDo toDo = toDoService.addToDo(command.toCreateToDoCommand());
        URI uri = new CreatedURI("/replica/" + command.replicaId + "/todo/" + toDo.getId()).uri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/todo/{id}")
    public ResponseEntity<RestToDo> getToDoById(@PathVariable Long id) {
        return toDoService
                .findOneById(id)
                .map(toDo -> ResponseEntity.ok(toRestToDo(toDo)))
                .orElse(ResponseEntity.badRequest().build());
    }

    private RestToDo toRestToDo(ToDo toDo) {
        return new RestToDo(toDo.getTitle(), toDo.getContent());
    }

    @Data
    static class RestToDoCommand {
        @NotBlank
        private String title;
        @NotBlank
        private String content;
        @NotNull
        @Positive
        private Long replicaId;

        ToDoUseCase.CreateToDoCommand toCreateToDoCommand() {
            return new ToDoUseCase.CreateToDoCommand(title, content, replicaId);
        }
    }


}
