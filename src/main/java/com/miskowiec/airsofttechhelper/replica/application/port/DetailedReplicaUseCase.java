package com.miskowiec.airsofttechhelper.replica.application.port;

import com.miskowiec.airsofttechhelper.replica.domain.Replica;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;

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
        UserDetails user;
    }

    @Value
    class UpdateReplicaResponse{
        public static UpdateReplicaResponse SUCCESS = new UpdateReplicaResponse(true, Collections.emptyList(), null);
        boolean success;
        List<String> errors;
        ErrorStatus status;
    }

    @Getter
    @AllArgsConstructor
    enum ErrorStatus{
        NOT_FOUND(HttpStatus.NOT_FOUND),
        FORBIDDEN(HttpStatus.FORBIDDEN);
        private final HttpStatus status;
    }
}
