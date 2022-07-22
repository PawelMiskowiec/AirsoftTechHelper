package com.example.airsofttechhelper.replica.application.port;

import com.example.airsofttechhelper.replica.domain.Replica;
import com.example.airsofttechhelper.replica.web.RestReplica;

import java.util.Optional;

public interface ReplicaUseCase {
    RestReplica findById(Long id);
    void updateReplica();
    void deleteReplica(Long id);
}
