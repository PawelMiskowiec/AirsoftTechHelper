package com.example.airsofttechhelper.replica.web;

import com.example.airsofttechhelper.replica.application.port.BasicReplicaUseCase;
import com.example.airsofttechhelper.replica.application.port.BasicReplicaUseCase.CreateOwnerCommand;
import com.example.airsofttechhelper.replica.application.port.BasicReplicaUseCase.UpdateReplicaStatusCommand;
import com.example.airsofttechhelper.replica.application.port.BasicReplicaUseCase.UpdateStatusResponse;
import com.example.airsofttechhelper.replica.domain.Replica;
import com.example.airsofttechhelper.replica.web.dto.RestBasicReplica;
import com.example.airsofttechhelper.security.UserSecurity;
import com.example.airsofttechhelper.web.CreatedURI;
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
import javax.validation.constraints.*;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.airsofttechhelper.replica.application.port.BasicReplicaUseCase.CreateReplicaCommand;

@RestController
@RequestMapping("/replicas-list")
@AllArgsConstructor
public class BasicReplicasController {
    private final BasicReplicaUseCase replicaService;
    private final UserSecurity userSecurity;

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RestBasicReplica> getAllUsersReplicas(@RequestParam Optional<String> status, @AuthenticationPrincipal UserDetails user) {
        List<RestBasicReplica> replicas;
        if (status.isPresent()) {
            replicas = replicaService.findByStatus(status.get())
                    .stream()
                    .map(this::toRestListReplica)
                    .collect(Collectors.toList());
        }
        replicas = replicaService.findAll()
                .stream()
                .map(this::toRestListReplica)
                .collect(Collectors.toList());

        return replicas;
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/{id}")
    public ResponseEntity<?> getReplicaById(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        return replicaService.findOneById(id)
                .map(r -> {
                    if (userSecurity.isOwnerOrAdmin(r.getTech().getUsername(), user)) {
                        return ResponseEntity.ok(toRestListReplica(r));
                    }
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }).orElse(
                        ResponseEntity.badRequest().build()
                );
    }


    private RestBasicReplica toRestListReplica(Replica replica) {
        return new RestBasicReplica(replica.getId(), replica.getName(), replica.getStatus(),
                replica.getCreatedAt(), replica.getReplicaOwner().getEmail());
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping
    public ResponseEntity<Object> addReplica(@Valid @RequestBody RestReplicaCommand command, @AuthenticationPrincipal UserDetails user) {
        Replica replica = replicaService.addReplica(command.toCreateCommand(user));
        URI uri = new CreatedURI("/replicas/" + replica.getId().toString()).uri();
        return ResponseEntity.created(uri).build();
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateReplicaStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String newStatus = body.get("status");
        UpdateReplicaStatusCommand command = new UpdateReplicaStatusCommand(id, newStatus);
        UpdateStatusResponse response = replicaService.updateReplicaStatus(command);
        if (!response.isSuccess()) {
            String errorMessage = String.join(", ", response.getErrors());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }
    }

    @Data
    private static class RestReplicaCommand {
        @NotBlank(message = "Please provide a name")
        private String name;

        @NotNull(message = "Please provide a description")
        private String description;

        @NotNull(message = "Please provide an additional equipment")
        private String additionalEquipment;

        @Valid
        private RestOwnerCommand owner;

        CreateReplicaCommand toCreateCommand(UserDetails user) {
            return new CreateReplicaCommand(name, description, additionalEquipment, owner.toCreateOwnerCommand(), user);
        }
    }

    @Data
    static class RestOwnerCommand {
        @NotBlank(message = "Please provide an Owners name")
        private String name;

        @NotBlank(message = "Please provide an Owners phone number")
        private String phone;

        @NotBlank(message = "Please provide an Owners street")
        private String street;

        @NotBlank(message = "Please provide an Owners city")
        private String city;

        @NotBlank(message = "Please provide an Owners zipCode")
        private String zipCode;

        @Email(message = "Please provide an Owners email")
        private String email;

        CreateOwnerCommand toCreateOwnerCommand() {
            return new CreateOwnerCommand(name, phone, street, city, zipCode, email);
        }
    }

}
