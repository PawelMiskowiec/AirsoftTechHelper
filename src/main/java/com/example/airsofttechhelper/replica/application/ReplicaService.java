package com.example.airsofttechhelper.replica.application;

import com.example.airsofttechhelper.replica.application.port.ReplicaUseCase;
import com.example.airsofttechhelper.replica.db.OwnerRepository;
import com.example.airsofttechhelper.replica.db.ReplicaRepository;
import com.example.airsofttechhelper.replica.domain.Owner;
import com.example.airsofttechhelper.replica.domain.Replica;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    public Optional<Replica> findOneById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Replica addReplica(CreateReplicaCommand command) {
        Replica replica = toReplica(command);
        return repository.save(replica);
    }

    private Replica toReplica(CreateReplicaCommand command) {
        return Replica.builder()
                .name(command.getName())
                .description(command.getDescription())
                .additionalEquipment(command.getAdditionalEquipment())
                .owner(getOrCreateOwner(command.getOwner()))
                .build();
    }

    private Owner getOrCreateOwner(Owner owner) {
        return ownerRepository.findByEmailIgnoreCase(owner.getEmail())
                .orElse(ownerRepository.save(owner));
    }

    @Override
    public void deleteReplica(Long id) {
        repository.deleteById(id);
    }
}
