package com.example.airsofttechhelper.replica.application;

import com.example.airsofttechhelper.replica.application.port.ReplicaListUseCase;
import com.example.airsofttechhelper.replica.db.OwnerJpaRepository;
import com.example.airsofttechhelper.replica.db.ReplicaJpaRepository;
import com.example.airsofttechhelper.replica.domain.Owner;
import com.example.airsofttechhelper.replica.domain.Replica;
import com.example.airsofttechhelper.replica.domain.ReplicaStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class ReplicaListService implements ReplicaListUseCase {

    private final ReplicaJpaRepository repository;

    private final OwnerJpaRepository ownerJpaRepository;

    @Override
    public List<Replica> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Replica> findByStatus(String status) {
        return repository.findByStatusIsContaining(toReplicaStatus(status));
    }

    @Override
    public Optional<Replica> findOneById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Replica addReplica(CreateReplicaCommand command) {
        Replica replica = toReplica(command);
        return repository.save(replica);
    }

    @Override
    public UpdateStatusResponse updateReplicaStatus(UpdateStatusCommand command) {
        return repository
                .findById(command.getReplicaId())
                .map(replica -> {
                    replica.updateStatus(toReplicaStatus(command.getReplicaStatus()));
                    repository.save(replica);
                    return UpdateStatusResponse.SUCCESS;
                }).orElse(new UpdateStatusResponse(false,
                        Collections.singletonList("Replica with id " + command.getReplicaId() + "not found")));
    }

    private ReplicaStatus toReplicaStatus(String status) {
        return ReplicaStatus
                .parseString(status)
                .orElseThrow(() ->
                        new IllegalArgumentException(status + " is not a valid replica status")
                );
    }

    private Replica toReplica(CreateReplicaCommand command) {
        Owner owner = getOrCreateOwner(command.getOwnerCommand());
        return Replica.builder()
                .name(command.getName())
                .description(command.getDescription())
                .additionalEquipment(command.getAdditionalEquipment())
                .owner(owner)
                .build();
    }

    private Owner getOrCreateOwner(CreateOwnerCommand ownerCommand) {
        return ownerJpaRepository
                .findByEmail(ownerCommand.getEmail())
                .orElse(toOwner(ownerCommand));
    }

    private Owner toOwner(CreateOwnerCommand command) {
        return new Owner(
                command.getName(),
                command.getPhone(),
                command.getStreet(),
                command.getCity(),
                command.getZipCode(),
                command.getEmail()
        );
    }

    @Override
    public void deleteReplica(Long id) {
        repository.deleteById(id);
    }
}
