package com.example.airsofttechhelper.menu.application.port;

import com.example.airsofttechhelper.menu.domain.Replica;

import java.util.List;
import java.util.Optional;

public interface ReplicaUseCase {
    List<Replica> findAll();
    Optional<Replica> findOneById(Long id);
    Replica addReplica();
    void deleteReplica(Long id);
}
