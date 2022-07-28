package com.example.airsofttechhelper.part.application;

import com.example.airsofttechhelper.part.application.port.ReplicaPartUseCase;
import com.example.airsofttechhelper.part.db.PartJpaRepository;
import com.example.airsofttechhelper.part.db.ReplicaPartJpaRepository;
import com.example.airsofttechhelper.part.domain.Part;
import com.example.airsofttechhelper.part.domain.PartCategory;
import com.example.airsofttechhelper.part.domain.ReplicaPart;
import com.example.airsofttechhelper.replica.db.ReplicaJpaRepository;
import com.example.airsofttechhelper.replica.domain.Replica;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class ReplicaPartService implements ReplicaPartUseCase {

    private final ReplicaPartJpaRepository repository;
    private final PartJpaRepository partJpaRepository;
    private final ReplicaJpaRepository replicaJpaRepository;

    @Override
    public List<ReplicaPart> findAll() {
        return repository.findAll();
    }

    @Override
    public List<ReplicaPart> findAllByReplicaId(Long replicaId) {
        return repository.findAllByReplicaId(replicaId);
    }

    @Override
    @Transactional
    public ReplicaPart addReplicaPart(CreateReplicaPartCommand command) {
        Part part = getOrAddPart(command);
        Replica replica = replicaJpaRepository.getReferenceById(command.getReplicaId());
        ReplicaPart replicaPart = new ReplicaPart(command.getNotes(), part, replica);
        return repository.save(replicaPart);
    }

    private Part getOrAddPart(CreateReplicaPartCommand command) {
        Part part;
        if(command.getPartId().isPresent()){
            part = partJpaRepository
                    .findById(command.getPartId().get())
                    .orElseThrow(() -> new IllegalArgumentException("Part with id " + command.getPartId().get() + " not found"));
        }else {
            part = new Part(command.getName(), PartCategory.parseString(command.getCategory())
                    .orElseThrow(() -> new IllegalArgumentException("Category " + command.getCategory() + " not found")));
        }
        return part;
    }

    @Override
    public UpdateNotesResponse updateNotes(UpdateReplicaPartNotesCommand command) {
        return repository.findById(command.getReplicaPartId())
                .map(replicaPart -> {
                    replicaPart.setNotes(command.getNotes());
                    repository.save(replicaPart);
                    return UpdateNotesResponse.SUCCESS;
                }).orElse(new UpdateNotesResponse(false,
                        List.of("Replica part with id " + command.getReplicaPartId() + "not found")));
    }
}
