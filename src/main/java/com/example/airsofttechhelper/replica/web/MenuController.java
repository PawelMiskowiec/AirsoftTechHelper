package com.example.airsofttechhelper.replica.web;

import com.example.airsofttechhelper.replica.application.ReplicaService;
import com.example.airsofttechhelper.replica.application.port.ReplicaUseCase;
import com.example.airsofttechhelper.replica.domain.Owner;
import com.example.airsofttechhelper.replica.domain.Replica;
import com.example.airsofttechhelper.web.CreatedURI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.airsofttechhelper.replica.application.port.ReplicaUseCase.CreateReplicaCommand;

@RestController
@RequestMapping("/menu")
@AllArgsConstructor
public class MenuController {
    private final ReplicaUseCase replicaService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MenuReplica> getReplicas(){
        return replicaService.findAll()
                .stream()
                .map(this::toMenuReplica)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuReplica> getReplicaById(@PathVariable Long id){
        return replicaService.findOneById(id)
                .map(replica -> ResponseEntity.ok(toMenuReplica(replica)))
                .orElse(ResponseEntity.notFound().build());
    }

    private MenuReplica toMenuReplica(Replica replica) {
        return new MenuReplica(replica.getId(), replica.getName(), replica.getStatus(), replica.getCreatedAt(), replica.getOwner().getEmail());
    }

    @PostMapping
    public ResponseEntity<Object> addReplica(@Valid @RequestBody RestReplicaCommand command){
        Replica replica = replicaService.addReplica(command.toCreateCommand());
        URI uri = new CreatedURI("/" + replica.getId().toString()).uri();
        return ResponseEntity.created(uri).build();
    }

    @Data
    private static class RestReplicaCommand{
        @NotBlank(message = "Please provide a name")
        private String name;

        @NotNull(message = "Please provide a description")
        private String description;

        @NotNull(message = "Please provide an additional equipment")
        private String additionalEquipment;

        @NotNull(message = "Please provide an Owner")
        private Owner owner;

        CreateReplicaCommand toCreateCommand(){ return new CreateReplicaCommand(name, description, additionalEquipment, owner); }
    }
}
