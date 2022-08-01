package com.example.airsofttechhelper.replica.application.port;

import com.example.airsofttechhelper.replica.domain.Replica;
import lombok.Value;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

public interface BasicReplicaUseCase {
    List<Replica> findAllUserReplicas(String username);
    List<Replica> findAllUserReplicasByStatus(String status, String username);
    Optional<Replica> findOneById(Long id);
    Replica addReplica(CreateReplicaCommand command);
    UpdateStatusResponse updateReplicaStatus(UpdateReplicaStatusCommand command);

    @Value
    class CreateReplicaCommand{
        String name;
        String description;
        String additionalEquipment;
        CreateOwnerCommand ownerCommand;
        UserDetails user;
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
    class UpdateReplicaStatusCommand {
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
