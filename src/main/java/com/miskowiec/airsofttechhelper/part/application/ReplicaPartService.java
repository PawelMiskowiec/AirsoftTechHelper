package com.miskowiec.airsofttechhelper.part.application;

import com.miskowiec.airsofttechhelper.part.application.port.ReplicaPartUseCase;
import com.miskowiec.airsofttechhelper.part.db.PartJpaRepository;
import com.miskowiec.airsofttechhelper.part.db.ReplicaPartJpaRepository;
import com.miskowiec.airsofttechhelper.part.domain.Part;
import com.miskowiec.airsofttechhelper.part.domain.PartCategory;
import com.miskowiec.airsofttechhelper.part.domain.ReplicaPart;
import com.miskowiec.airsofttechhelper.replica.db.ReplicaJpaRepository;
import com.miskowiec.airsofttechhelper.replica.domain.Replica;
import com.miskowiec.airsofttechhelper.user.db.UserEntityJpaRepository;
import com.miskowiec.airsofttechhelper.user.domain.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class ReplicaPartService implements ReplicaPartUseCase {

    private final ReplicaPartJpaRepository repository;
    private final PartJpaRepository partJpaRepository;
    private final ReplicaJpaRepository replicaJpaRepository;
    private final UserEntityJpaRepository userEntityJpaRepository;

    @Override
    public List<ReplicaPart> findAllBy(UserDetails user) {
        return repository.findAllByUsername(user.getUsername());
    }

    @Override
    public List<ReplicaPart> findAllBy(Long replicaId, UserDetails user) {
        return repository.findAllByReplicaIdAndUsername(replicaId, user.getUsername());
    }

    @Override
    public ReplicaPart addReplicaPart(CreateReplicaPartCommand command) {
        Part part = getOrAddPart(command);
        Replica replica = replicaJpaRepository.getReferenceById(command.getReplicaId());
        UserEntity user = userEntityJpaRepository.findByUsername(command.getUser().getUsername())
                .orElseThrow(() -> new IllegalStateException("Currently logged in user is not present in database"));
        ReplicaPart replicaPart = new ReplicaPart(command.getNotes(), part, replica, user);
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
