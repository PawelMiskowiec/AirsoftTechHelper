package com.miskowiec.airsofttechhelper.replica.application;

import com.miskowiec.airsofttechhelper.replica.application.port.DetailedReplicaUseCase;
import com.miskowiec.airsofttechhelper.replica.db.ReplicaJpaRepository;
import com.miskowiec.airsofttechhelper.replica.domain.Replica;
import com.miskowiec.airsofttechhelper.security.UserSecurity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class DetailedReplicaService implements DetailedReplicaUseCase {

    private final ReplicaJpaRepository repository;
    private final UserSecurity userSecurity;

    @Override
    public Optional<Replica> findById(Long id) {
        return repository.findOneByIdEager(id);
    }


    @Override
    public UpdateReplicaResponse updateReplica(UpdateReplicaCommand command) {
        return repository.findById(command.getReplicaId())
                .map(replica -> {
                    if(userSecurity.isOwnerOrAdmin(replica.getUser().getUsername(), command.getUser())){
                        updateFields(command, replica);
                        repository.save(replica);
                        return UpdateReplicaResponse.SUCCESS;
                    }
                    return new UpdateReplicaResponse(false, List.of("Unauthorised"), ErrorStatus.FORBIDDEN);
                })
                .orElse(new UpdateReplicaResponse(
                        false,
                        List.of("Replica with id " + command.getReplicaId() + " not found"),
                        ErrorStatus.NOT_FOUND
                ));
    }

    private Replica updateFields(UpdateReplicaCommand command, Replica replica) {
        if(command.getName() != null){
            replica.setName(command.getName());
        }
        if(command.getDescription() != null){
            replica.setDescription(command.getDescription());
        }
        if(command.getAdditionalEquipment() != null){
            replica.setAdditionalEquipment(command.getAdditionalEquipment());
        }
        return replica;
    }

    @Override
    public void deleteReplica(Long id) {
        repository.deleteById(id);
    }
}
