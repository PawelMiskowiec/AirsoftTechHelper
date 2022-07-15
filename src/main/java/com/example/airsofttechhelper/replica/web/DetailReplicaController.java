package com.example.airsofttechhelper.replica.web;

import com.example.airsofttechhelper.replica.application.port.ReplicaPartUseCase;
import com.example.airsofttechhelper.replica.application.port.ReplicaPartUseCase.CreateReplicaPartCommand;
import com.example.airsofttechhelper.replica.domain.Replica;
import com.example.airsofttechhelper.replica.domain.ReplicaPart;
import com.example.airsofttechhelper.web.CreatedURI;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/replicas")
@AllArgsConstructor
public class DetailReplicaController {

    private final ReplicaPartUseCase replicaPartService;

    @GetMapping("/{id}/replica-part")
    public ResponseEntity<Object> getAllByReplicaId(@PathVariable Long id){
        List<ReplicaPart> replicaPart = replicaPartService.findAllByReplicaId(id);
        List<RestReplicaPart> restReplicaParts = new ArrayList<>(replicaPart.size());
        replicaPart.forEach(rp -> restReplicaParts.add(toRestReplicaPart(rp)));
        return ResponseEntity.ok(restReplicaParts);
    }

    private RestReplicaPart toRestReplicaPart(ReplicaPart rp){
        return new RestReplicaPart(rp.getPart().getName(), rp.getPart().getCategory(), rp.getPart().getId(),
                rp.getReplica().getId(), rp.getNotes(), rp.getCreatedAt());
    }

    @PostMapping("/{id}/replica-part")
    public ResponseEntity<Object> addReplicaPart(@PathVariable Long id, @RequestBody CreateReplicaPartRestCommand command){
        ReplicaPart replicaPart = replicaPartService.addReplicaPart(command.toCreateReplicaPartCommand(id));
        URI uri = new CreatedURI("/replicas/" + id + "/replica-part/" + replicaPart.getId()).uri();
        return ResponseEntity.created(uri).build();
    }

    @Value
    static class CreateReplicaPartRestCommand{
        Long partId;
        String name;
        String category;
        String notes;
        CreateReplicaPartCommand toCreateReplicaPartCommand(Long replicaId){
            return new CreateReplicaPartCommand(replicaId, partId, name, category, notes);
        }
    }
}
