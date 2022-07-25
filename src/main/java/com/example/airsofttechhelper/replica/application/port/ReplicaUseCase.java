package com.example.airsofttechhelper.replica.application.port;

import com.example.airsofttechhelper.replica.web.dto.RestDetailedReplica;

public interface ReplicaUseCase {
    RestDetailedReplica findById(Long id);
    void updateReplica();
    void deleteReplica(Long id);
}
