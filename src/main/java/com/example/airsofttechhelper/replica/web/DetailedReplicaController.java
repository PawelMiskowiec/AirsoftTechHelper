package com.example.airsofttechhelper.replica.web;

import com.example.airsofttechhelper.replica.application.port.ReplicaUseCase;
import com.example.airsofttechhelper.replica.domain.Replica;
import com.example.airsofttechhelper.web.CreatedURI;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/replica")
@AllArgsConstructor
public class DetailedReplicaController {

    private final ReplicaUseCase replicaService;

    @GetMapping("/{id}")
    public ResponseEntity<RestReplica> getById(@PathVariable Long id){
        RestReplica restReplica = replicaService.findById(id);
        URI uri = new CreatedURI("/replica/" + id).uri();
        return ResponseEntity.created(uri).build();
    }


}
