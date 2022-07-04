package com.example.airsofttechhelper.replica.application.port;

import com.example.airsofttechhelper.replica.domain.Owner;
import com.example.airsofttechhelper.replica.domain.Replica;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

public interface ReplicaUseCase {
    List<Replica> findAll();
    List<Replica> findByStatus(String status);
    Optional<Replica> findOneById(Long id);
    Replica addReplica(CreateReplicaCommand command);
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
}
