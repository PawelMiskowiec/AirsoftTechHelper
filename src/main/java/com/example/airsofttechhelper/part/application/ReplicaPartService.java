package com.example.airsofttechhelper.part.application;

import com.example.airsofttechhelper.part.application.port.PartUseCase;
import com.example.airsofttechhelper.part.db.PartJpaRepository;
import com.example.airsofttechhelper.part.domain.Part;
import com.example.airsofttechhelper.part.application.port.ReplicaPartUseCase;
import com.example.airsofttechhelper.replica.application.port.ReplicaUseCase;
import com.example.airsofttechhelper.replica.db.ReplicaJpaRepository;
import com.example.airsofttechhelper.part.db.ReplicaPartJpaRepository;
import com.example.airsofttechhelper.replica.domain.Replica;
import com.example.airsofttechhelper.part.domain.ReplicaPart;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class ReplicaPartService implements ReplicaPartUseCase {

    private final PartUseCase partService;
    private final ReplicaUseCase replicaService;
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
    public Optional<ReplicaPart> findOneById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional()
    public ReplicaPart addReplicaPart(CreateReplicaPartCommand command) {
        Part part = getOrAddPart(command);
        Replica replica = replicaJpaRepository.findById(command.getReplicaId())
                .orElseThrow(() -> new IllegalArgumentException("Replica with id " + command.getReplicaId() + " not found"));

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
            part = partService.addPart(command.toCreatePartCommand(command.getName(), command.getCategory()));
        }
        return part;
    }

    @Override
    public UpdateNotesResponse updateNotes(Long id, String newNotes) {
        return repository.findById(id)
                .map(replicaPart -> {
                    replicaPart.setNotes(newNotes);
                    repository.save(replicaPart);
                    return UpdateNotesResponse.SUCCESS;
                }).orElse(new UpdateNotesResponse(false,
                        List.of("Replica part with id " + id + "not found")));
    }
}
