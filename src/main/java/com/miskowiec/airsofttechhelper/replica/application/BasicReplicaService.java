package com.miskowiec.airsofttechhelper.replica.application;

import com.miskowiec.airsofttechhelper.replica.application.port.BasicReplicaUseCase;
import com.miskowiec.airsofttechhelper.replica.db.OwnerJpaRepository;
import com.miskowiec.airsofttechhelper.replica.db.ReplicaJpaRepository;
import com.miskowiec.airsofttechhelper.replica.domain.ReplicaOwner;
import com.miskowiec.airsofttechhelper.replica.domain.Replica;
import com.miskowiec.airsofttechhelper.replica.domain.ReplicaStatus;
import com.miskowiec.airsofttechhelper.security.UserSecurity;
import com.miskowiec.airsofttechhelper.user.db.UserEntityRepository;
import com.miskowiec.airsofttechhelper.user.domain.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class BasicReplicaService implements BasicReplicaUseCase {

    private final ReplicaJpaRepository repository;
    private final OwnerJpaRepository ownerJpaRepository;
    private final UserEntityRepository userEntityRepository;
    private final UserSecurity userSecurity;


    @Override
    public List<Replica> findAllUserReplicas(String username) {
        return repository.findAllByUsernameFetchOwnerAndTech(username);
    }

    @Override
    public List<Replica> findAllUserReplicasByStatus(String status, String username) {
        ReplicaStatus replicaStatus = ReplicaStatus.parseString(status)
                .orElseThrow(() -> new IllegalArgumentException("Invalid status " + status));
        return repository.findAllByStatusAndUsername(replicaStatus, username);
    }

    @Override
    public Optional<Replica> findOneById(Long id) {
        return repository.findOneByIdFetchForAuth(id);
    }

    @Override
    public Replica addReplica(CreateReplicaCommand command) {
        Replica replica = toReplica(command);
        return repository.save(replica);
    }

    @Override
    public UpdateStatusResponse updateReplicaStatus(UpdateReplicaStatusCommand command) {
        return repository
                .findOneByIdFetchForAuth(command.getReplicaId())
                .map(replica -> {
                    if(!userSecurity.isOwnerOrAdmin(replica.getUser().getUsername(), command.getUser())){
                        return new UpdateStatusResponse(
                                false,
                                "User " + command.getUser().getUsername() + " is not authorised to update this replica",
                                Error.FORBIDDEN
                        );
                    }
                    replica.updateStatus(toReplicaStatus(command.getReplicaStatus()));
                    repository.save(replica);
                    return UpdateStatusResponse.SUCCESS;
                }).orElse(
                        new UpdateStatusResponse(
                                false,
                                "Replica with id " + command.getReplicaId() + " not found",
                                Error.NOT_FOUND
                        )
                );
    }

    private ReplicaStatus toReplicaStatus(String status){
        return ReplicaStatus
                .parseString(status)
                .orElseThrow(() ->
                        new IllegalArgumentException(status + " is not a valid replica status")
                );
    }

    private Replica toReplica(CreateReplicaCommand command) {
        UserEntity user = userEntityRepository
                .findByUsername(command.getUser().getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Couldn't find user " + command.getUser().getUsername()));
        ReplicaOwner replicaOwner = getOrCreateOwner(command.getOwnerCommand());
        return Replica.builder()
                .name(command.getName())
                .description(command.getDescription())
                .additionalEquipment(command.getAdditionalEquipment())
                .replicaOwner(replicaOwner)
                .user(user)
                .build();
    }

    private ReplicaOwner getOrCreateOwner(CreateOwnerCommand ownerCommand) {
        return ownerJpaRepository
                .findByEmail(ownerCommand.getEmail())
                .orElse(toOwner(ownerCommand));
    }

    private ReplicaOwner toOwner(CreateOwnerCommand command) {
        return new ReplicaOwner(
                command.getName(),
                command.getPhone(),
                command.getStreet(),
                command.getCity(),
                command.getZipCode(),
                command.getEmail()
        );
    }
}
