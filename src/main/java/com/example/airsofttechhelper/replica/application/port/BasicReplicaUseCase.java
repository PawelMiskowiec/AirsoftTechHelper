package com.example.airsofttechhelper.replica.application.port;

import com.example.airsofttechhelper.replica.domain.Replica;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import org.springframework.http.HttpStatus;
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
        UserDetails user;
    }

    @Value
    class UpdateStatusResponse{
        public static UpdateStatusResponse SUCCESS = new UpdateStatusResponse(true, null, null);
        boolean success;
        String  errorMessage;
        Error errorStatus;
    }

    @AllArgsConstructor
    @Getter
    enum Error {
        NOT_FOUND(HttpStatus.NOT_FOUND),
        FORBIDDEN(HttpStatus.FORBIDDEN);

        private final HttpStatus status;
    }

}
