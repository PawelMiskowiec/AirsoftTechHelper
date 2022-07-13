package com.example.airsofttechhelper.replica.application;

import com.example.airsofttechhelper.replica.application.port.ReplicaUseCase;
import com.example.airsofttechhelper.replica.db.OwnerRepository;
import com.example.airsofttechhelper.replica.db.ReplicaRepository;
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
public class ReplicaService implements ReplicaUseCase {

    private final ReplicaRepository repository;

    private final OwnerRepository ownerRepository;

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
    @Transactional
    public Replica addReplica(CreateReplicaCommand command) {
        Replica replica = toReplica(command);
        return repository.save(replica);
    }

    @Override
    @Transactional
    public UpdateStatusResponse updateReplicaStatus(UpdateStatusCommand command) {
        return repository.findById(command.getReplicaId())
                .map(replica -> {
                    replica.updateStatus(toReplicaStatus(command.getReplicaStatus()));
                    repository.save(replica);
                    return UpdateStatusResponse.SUCCESS;
                }).orElse(new UpdateStatusResponse(false,
                        Collections.singletonList("Replica with id " + command.getReplicaId() + "not found")));
    }

    private ReplicaStatus toReplicaStatus(String status) {
        return ReplicaStatus.parseString(status)
                .orElseThrow(() ->
                        new IllegalArgumentException(status + " is not a valid replica status")
                );
    }

    private Replica toReplica(CreateReplicaCommand command) {
        return Replica.builder()
                .name(command.getName())
                .description(command.getDescription())
                .additionalEquipment(command.getAdditionalEquipment())
                .owner(getOrCreateOwner(command.getOwnerCommand()))
                .build();
    }

    private Owner getOrCreateOwner(CreateOwnerCommand ownerCommand) {
        return ownerRepository.findByEmailIgnoreCase(ownerCommand.getEmail())
                .orElse(ownerRepository.save(toOwner(ownerCommand)));
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
