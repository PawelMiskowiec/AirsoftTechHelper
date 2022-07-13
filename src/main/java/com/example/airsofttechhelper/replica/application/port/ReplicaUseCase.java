package com.example.airsofttechhelper.replica.application.port;

import com.example.airsofttechhelper.replica.domain.Replica;
import com.example.airsofttechhelper.replica.domain.ReplicaStatus;
import lombok.Value;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

public interface ReplicaUseCase {
    List<Replica> findAll();
    List<Replica> findByStatus(String status);
    Optional<Replica> findOneById(Long id);
    Replica addReplica(CreateReplicaCommand command);
    UpdateStatusResponse updateReplicaStatus(UpdateStatusCommand command);
    void deleteReplica(Long id);

    @Value
    class CreateReplicaCommand{
        String name;
        String description;
        String additionalEquipment;
        CreateOwnerCommand ownerCommand;
    }

    @Value
    class CreateOwnerCommand{
        String name;
        String phone;
        String street;
        String city;
        String zipCode;
        String email;
    }

    @Value
    class UpdateStatusCommand{
        Long replicaId;
        String replicaStatus;
    }

    @Value
    class UpdateStatusResponse{
        public static UpdateStatusResponse SUCCESS = new UpdateStatusResponse(true, emptyList());
        boolean success;
        List<String> errors;
    }

}
