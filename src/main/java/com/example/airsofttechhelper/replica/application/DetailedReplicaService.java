package com.example.airsofttechhelper.replica.application;

import com.example.airsofttechhelper.replica.application.port.DetailedReplicaUseCase;
import com.example.airsofttechhelper.replica.db.ReplicaJpaRepository;
import com.example.airsofttechhelper.replica.domain.Replica;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class DetailedReplicaService implements DetailedReplicaUseCase {

    private final ReplicaJpaRepository repository;

    @Override
    public Replica findById(Long id) {
        return repository.findOneByIdEager(id)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find replica with id " + id));
    }


    @Override
    public UpdateReplicaResponse updateReplica(UpdateReplicaCommand command) {
        return repository.findById(command.getReplicaId())
                .map(replica -> {
                    updateFields(command, replica);
                    return UpdateReplicaResponse.SUCCESS;
                })
                .orElse(new UpdateReplicaResponse(
                        false,
                        List.of("Replica with id " + command.getReplicaId() + " not found")
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
