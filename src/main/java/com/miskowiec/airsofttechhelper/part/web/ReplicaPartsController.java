package com.miskowiec.airsofttechhelper.part.web;

import com.miskowiec.airsofttechhelper.part.application.port.ReplicaPartUseCase;
import com.miskowiec.airsofttechhelper.part.application.port.ReplicaPartUseCase.CreateReplicaPartCommand;
import com.miskowiec.airsofttechhelper.part.application.port.ReplicaPartUseCase.UpdateNotesResponse;
import com.miskowiec.airsofttechhelper.part.application.port.ReplicaPartUseCase.UpdateReplicaPartNotesCommand;
import com.miskowiec.airsofttechhelper.part.domain.ReplicaPart;
import com.miskowiec.airsofttechhelper.web.CreatedURI;
import lombok.AllArgsConstructor;
import lombok.Value;
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
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/replica-parts")
@AllArgsConstructor
public class ReplicaPartsController {

    private final ReplicaPartUseCase replicaPartService;

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/")
    public ResponseEntity<Object> getAllUser(@RequestParam Optional<Long> replicaId, @AuthenticationPrincipal UserDetails user){
        if(replicaId.isPresent()){
            List<ReplicaPart> replicaParts = replicaPartService.findAllBy(replicaId.get(), user);
            return ResponseEntity.ok(toRestReplicaPartsList(replicaParts));
        }
        List<ReplicaPart> replicaParts = replicaPartService.findAllBy(user);
        return ResponseEntity.ok(toRestReplicaPartsList(replicaParts));
    }

    private List<RestReplicaPart> toRestReplicaPartsList(List<ReplicaPart> replicaPart) {
        List<RestReplicaPart> restReplicaParts = new ArrayList<>(replicaPart.size());
        replicaPart.forEach(rp -> restReplicaParts.add(toRestReplicaPart(rp)));
        return restReplicaParts;
    }

    private RestReplicaPart toRestReplicaPart(ReplicaPart rp){
        return new RestReplicaPart(rp.getPart().getName(), rp.getPart().getCategory(), rp.getPart().getId(),
                rp.getReplica().getId(), rp.getNotes(), rp.getCreatedAt());
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping("/")
    public ResponseEntity<Object> addReplicaPart(
            @RequestParam Optional<Long> partId,
            @RequestBody @Valid RestAddPartAndReplicaPartCommand command,
            @AuthenticationPrincipal UserDetails user
    ){
        ReplicaPart replicaPart = replicaPartService.addReplicaPart(command.toCreateReplicaPartCommand(partId, user));
        URI uri = new CreatedURI("/replica-part/" + replicaPart.getId()).uri();
        return ResponseEntity.created(uri).build();
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PatchMapping("/notes")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateReplicaPartNotes(@Valid @RequestBody RestUpdateNotesCommand command){
        UpdateNotesResponse updateNotesResponse = replicaPartService.updateNotes(command.toUpdateNotesCommand());
        if(!updateNotesResponse.isSuccess()){
            String errors = String.join(", ", updateNotesResponse.getErrors());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors);
        }
    }

    @Value
    static class RestAddPartAndReplicaPartCommand{
        @NotNull
        Long replicaId;
        @NotBlank
        String name;
        @NotBlank
        String category;
        @NotBlank
        String notes;
        CreateReplicaPartCommand toCreateReplicaPartCommand(Optional<Long> partId, UserDetails user){
            return new CreateReplicaPartCommand(replicaId, partId, name, category, notes, user);
        }
    }

    @Value
    static class RestUpdateNotesCommand{
        @NotNull
        Long replicaPartId;
        @NotBlank
        String notes;

        UpdateReplicaPartNotesCommand toUpdateNotesCommand(){
            return new UpdateReplicaPartNotesCommand(replicaPartId, notes);
        }
    }
}
