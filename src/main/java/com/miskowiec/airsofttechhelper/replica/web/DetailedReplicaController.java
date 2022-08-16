package com.miskowiec.airsofttechhelper.replica.web;

import com.miskowiec.airsofttechhelper.part.domain.ReplicaPart;
import com.miskowiec.airsofttechhelper.part.web.RestReplicaPart;
import com.miskowiec.airsofttechhelper.replica.application.port.DetailedReplicaUseCase;
import com.miskowiec.airsofttechhelper.replica.application.port.ToDoUseCase;
import com.miskowiec.airsofttechhelper.replica.domain.Replica;
import com.miskowiec.airsofttechhelper.replica.domain.ToDo;
import com.miskowiec.airsofttechhelper.replica.web.dto.RestDetailedReplica;
import com.miskowiec.airsofttechhelper.replica.web.dto.RestToDo;
import com.miskowiec.airsofttechhelper.security.UserSecurity;
import com.miskowiec.airsofttechhelper.web.CreatedURI;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/replicas/details")
@AllArgsConstructor
public class DetailedReplicaController {
    private final DetailedReplicaUseCase replicaService;
    private final ToDoUseCase toDoService;

    private final UserSecurity userSecurity;

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/{id}")
    public ResponseEntity<RestDetailedReplica> getById(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        return replicaService.findById(id)
                .map(r -> {
                    authorize(user, r);
                    return ResponseEntity.ok(toRestDetailedReplica(r));
                }).orElse(
                        ResponseEntity.notFound().build()
                );
    }

    private RestDetailedReplica toRestDetailedReplica(Replica replica) {
        return new RestDetailedReplica(
                replica.getId(),
                replica.getName(),
                replica.getAdditionalEquipment(),
                replica.getReplicaOwner().getName(),
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
    public ResponseEntity<Object> addToDo(@Valid @RequestBody RestToDoCommand command, @AuthenticationPrincipal UserDetails user) {
        ToDo toDo = toDoService.addToDo(command.toCreateToDoCommand(user));
        URI uri = new CreatedURI("/replica/" + command.replicaId + "/todo/" + toDo.getId()).uri();
        return ResponseEntity.created(uri).build();
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/todo/{id}")
    public ResponseEntity<RestToDo> getToDoById(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        return toDoService
                .findOneById(id)
                .map(toDo -> {
                    authorize(user, toDo.getReplica());
                    return ResponseEntity.ok(toRestToDo(toDo));
                })
                .orElse(ResponseEntity.badRequest().build());
    }

    private void authorize(UserDetails user, Replica r) {
        if (!userSecurity.isOwnerOrAdmin(r.getTech().getUsername(), user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
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

        ToDoUseCase.CreateToDoCommand toCreateToDoCommand(UserDetails userDetails) {
            return new ToDoUseCase.CreateToDoCommand(title, content, replicaId, userDetails);
        }
    }


}
