package com.example.airsofttechhelper.part.web;

import com.example.airsofttechhelper.part.application.port.ReplicaPartUseCase;
import com.example.airsofttechhelper.part.application.port.ReplicaPartUseCase.CreateReplicaPartCommand;
import com.example.airsofttechhelper.part.domain.ReplicaPart;
import com.example.airsofttechhelper.web.CreatedURI;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/")
    public ResponseEntity<Object> getAll(@RequestParam Optional<Long> replicaId){
        if(replicaId.isPresent()){
            List<ReplicaPart> replicaParts = replicaPartService.findAllByReplicaId(replicaId.get());
            return ResponseEntity.ok(toRestReplicaPartsList(replicaParts));
        }
        List<ReplicaPart> replicaParts = replicaPartService.findAll();
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

    @PostMapping("/{partId}")
    public ResponseEntity<Object> addReplicaPartToExistingPart(@RequestBody @Valid RestAddReplicaPartCommand command) {
        ReplicaPart replicaPart = replicaPartService.addReplicaPart(command.toCreateReplicaPartCommand());
        URI uri = new CreatedURI("/replica-part/" + replicaPart.getId()).uri();
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/")
    public ResponseEntity<Object> addReplicaPart(@RequestBody @Valid RestAddPartAndReplicaPartCommand command) {
        ReplicaPart replicaPart = replicaPartService.addReplicaPart(command.toCreateReplicaPartCommand());
        URI uri = new CreatedURI("/replica-part/" + replicaPart.getId()).uri();
        return ResponseEntity.created(uri).build();
    }

    @Value
    static class RestAddReplicaPartCommand{
        @NotNull
        Long replicaId;
        @NotNull
        Long partId;
        @NotBlank
        String notes;
        CreateReplicaPartCommand toCreateReplicaPartCommand(){
            return new CreateReplicaPartCommand(replicaId, Optional.of(partId), null, null, notes);
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
        CreateReplicaPartCommand toCreateReplicaPartCommand(){
            return new CreateReplicaPartCommand(replicaId, Optional.empty(), name, category, notes);
        }
    }
}
