package com.example.airsofttechhelper.replica.application.port;

import com.example.airsofttechhelper.replica.domain.Replica;
import lombok.Value;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface DetailedReplicaUseCase {
    Optional<Replica> findById(Long id);
    UpdateReplicaResponse updateReplica(UpdateReplicaCommand command);
    void deleteReplica(Long id);

    @Value
    class UpdateReplicaCommand{
        Long replicaId;
        String name;
        String description;
        String additionalEquipment;
    }

    @Value
    class UpdateReplicaResponse{
        public static UpdateReplicaResponse SUCCESS = new UpdateReplicaResponse(true, Collections.emptyList());
        boolean success;
        List<String> errors;
    }
}
