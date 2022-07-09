package com.example.airsofttechhelper.replica.web;

import com.example.airsofttechhelper.replica.application.port.ReplicaUseCase;
import com.example.airsofttechhelper.replica.application.port.ReplicaUseCase.CreateOwnerCommand;
import com.example.airsofttechhelper.replica.application.port.ReplicaUseCase.UpdateStatusCommand;
import com.example.airsofttechhelper.replica.application.port.ReplicaUseCase.UpdateStatusResponse;
import com.example.airsofttechhelper.replica.domain.Replica;
import com.example.airsofttechhelper.replica.domain.ReplicaStatus;
import com.example.airsofttechhelper.web.CreatedURI;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.airsofttechhelper.replica.application.port.ReplicaUseCase.CreateReplicaCommand;

@RestController
@RequestMapping("/replicas")
@AllArgsConstructor
public class ReplicasController {
    private final ReplicaUseCase replicaService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RestReplica> getAllReplicas(@RequestParam Optional<String> status){
        List<RestReplica> replicas;
        if(status.isPresent()){
            replicas = replicaService.findByStatus(status.get())
                    .stream()
                    .map(this::toRestReplica)
                    .collect(Collectors.toList());
        }
        replicas= replicaService.findAll()
                .stream()
                .map(this::toRestReplica)
                .collect(Collectors.toList());

        return replicas;
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestReplica> getReplicaById(@PathVariable Long id){
        return replicaService.findOneById(id)
                .map(replica -> ResponseEntity.ok(toRestReplica(replica)))
                .orElse(ResponseEntity.notFound().build());
    }

    private RestReplica toRestReplica(Replica replica) {
        return new RestReplica(replica.getId(), replica.getName(), replica.getStatus(), replica.getCreatedAt(), replica.getOwner().getEmail());
    }

    @PostMapping
    public ResponseEntity<Object> addReplica(@Valid @RequestBody RestReplicaCommand command){
        Replica replica = replicaService.addReplica(command.toCreateCommand());
        URI uri = new CreatedURI("/replicas/" + replica.getId().toString()).uri();
        return ResponseEntity.created(uri).build();
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateReplicaStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
            ){
        String newStatus = body.get("status");
        ReplicaStatus replicaStatus = ReplicaStatus
                .parseString(newStatus)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Uknown status: " + newStatus)
                );
        UpdateStatusCommand command = new UpdateStatusCommand(id, replicaStatus);
        UpdateStatusResponse response = replicaService.updateReplicaStatus(command);
        if(!response.isSuccess()){
            String errorMessage = String.join(", ", response.getErrors());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReplica(@PathVariable Long id){
        replicaService.deleteReplica(id);
    }

    @Data
    private static class RestReplicaCommand{
        @NotBlank(message = "Please provide a name")
        private String name;

        @NotNull(message = "Please provide a description")
        private String description;

        @NotNull(message = "Please provide an additional equipment")
        private String additionalEquipment;

        @Valid
        private RestOwnerCommand owner;

        CreateReplicaCommand toCreateCommand(){
            return new CreateReplicaCommand(name, description, additionalEquipment, owner.toCreateOwnerCommand());
        }
    }

    @Data
    static class RestOwnerCommand {
        @NotBlank(message = "Please provide an Owner's name")
        private String name;

        @NotBlank(message = "Please provide an Owner's phone number")
        private String phone;

        @NotBlank(message = "Please provide an Owner's street")
        private String street;

        @NotBlank(message = "Please provide an Owner's city")
        private String city;

        @NotBlank(message = "Please provide an Owner's zipCode")
        private String zipCode;

        @Email(message = "Please provide an Owner's email")
        private String email;

        CreateOwnerCommand toCreateOwnerCommand(){
            return new CreateOwnerCommand(name, phone, street, city, zipCode, email);
        }
    }

}
