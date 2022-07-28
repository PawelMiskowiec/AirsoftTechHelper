package com.example.airsofttechhelper.replica.web;

import com.example.airsofttechhelper.part.domain.ReplicaPart;
import com.example.airsofttechhelper.part.web.RestReplicaPart;
import com.example.airsofttechhelper.replica.application.port.ReplicaUseCase;
import com.example.airsofttechhelper.replica.application.port.ToDoUseCase;
import com.example.airsofttechhelper.replica.domain.Replica;
import com.example.airsofttechhelper.replica.domain.ToDo;
import com.example.airsofttechhelper.replica.web.dto.RestDetailedReplica;
import com.example.airsofttechhelper.replica.web.dto.RestToDo;
import com.example.airsofttechhelper.security.UserSecurity;
import com.example.airsofttechhelper.web.CreatedURI;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/replica")
@AllArgsConstructor
public class DetailedReplicaController {

    private final ReplicaUseCase replicaService;
    private final ToDoUseCase toDoService;

    private final UserSecurity userSecurity;

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/{id}")
    public ResponseEntity<RestDetailedReplica> getById(@PathVariable Long id, @AuthenticationPrincipal User user) {
        RestDetailedReplica restDetailedReplica = toRestDetailedReplica(replicaService.findById(id));
        URI uri = new CreatedURI("/replica/" + id).uri();
        return ResponseEntity.created(uri).build();
    }

    private RestDetailedReplica toRestDetailedReplica(Replica replica) {
        return new RestDetailedReplica(
                replica.getId(),
                replica.getName(),
                replica.getAdditionalEquipment(),
                replica.getOwner().getName(),
                replica.getCreatedAt(),
                replica.getUpdatedAt(),
                toRestReplicaParts(replica.getReplicaParts(), replica.getId()),
                toRestToDo(replica.getToDos())
        );
    }

    private Set<RestToDo> toRestToDo(Set<ToDo> toDos) {
        return toDos.stream()
                .map(toDo -> new RestToDo(toDo.getTitle(), toDo.getContent()))
                .collect(Collectors.toSet());
    }

    private Set<RestReplicaPart> toRestReplicaParts(Set<ReplicaPart> replicaParts, Long replicaId) {
        return replicaParts.stream()
                .map(replicaPart -> new RestReplicaPart(
                        replicaPart.getPart().getName(),
                        replicaPart.getPart().getCategory(),
                        replicaPart.getPart().getId(),
                        replicaId,
                        replicaPart.getNotes(),
                        replicaPart.getCreatedAt()
                )).collect(Collectors.toSet());
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReplica(@PathVariable Long id){
        replicaService.deleteReplica(id);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping("/todo")
    public ResponseEntity<Object> addToDo(@Valid @RequestBody RestToDoCommand command, @AuthenticationPrincipal User user) {
        ToDo toDo = toDoService.addToDo(command.toCreateToDoCommand());
        URI uri = new CreatedURI("/replica/" + command.replicaId + "/todo/" + toDo.getId()).uri();
        return ResponseEntity.created(uri).build();
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/todo/{id}")
    public ResponseEntity<RestToDo> getToDoById(@PathVariable Long id, @AuthenticationPrincipal User user) {
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
