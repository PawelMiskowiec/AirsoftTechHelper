package com.example.airsofttechhelper.replica.application.port;

import com.example.airsofttechhelper.replica.domain.Replica;

import java.util.List;
import java.util.Optional;

public interface ReplicaUseCase {
    List<Replica> findAll();
    Optional<Replica> findOneById(Long id);
    Replica addReplica();
    void deleteReplica(Long id);
}
