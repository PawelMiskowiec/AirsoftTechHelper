package com.example.airsofttechhelper.replica.application;

import com.example.airsofttechhelper.part.application.PartService;
import com.example.airsofttechhelper.part.db.PartJpaRepository;
import com.example.airsofttechhelper.part.domain.Part;
import com.example.airsofttechhelper.replica.application.port.ReplicaPartUseCase;
import com.example.airsofttechhelper.replica.db.ReplicaJpaRepository;
import com.example.airsofttechhelper.replica.db.ReplicaPartJpaRepository;
import com.example.airsofttechhelper.replica.domain.Replica;
import com.example.airsofttechhelper.replica.domain.ReplicaPart;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class ReplicaPartService implements ReplicaPartUseCase {

    private final PartService partService;
    private final ReplicaPartJpaRepository repository;
    private final PartJpaRepository partJpaRepository;
    private final ReplicaJpaRepository replicaJpaRepository;

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
        Optional<Part> part = partJpaRepository.findById(command.getPartId());
        if(part.isPresent()){
            Replica replicaReference = replicaJpaRepository.getReferenceById(command.getReplicaId());
            ReplicaPart replicaPart = new ReplicaPart(command.getNotes(), part.get(), replicaReference);
            return repository.save(replicaPart);
        }else {
            Part newPart = partService.addPart(command.toCreatePartCommand());
            Replica replicaReference = replicaJpaRepository.getReferenceById(command.getReplicaId());
            ReplicaPart rPart = new ReplicaPart(command.getNotes(), newPart, replicaReference);
            return repository.save(rPart);
        }
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
