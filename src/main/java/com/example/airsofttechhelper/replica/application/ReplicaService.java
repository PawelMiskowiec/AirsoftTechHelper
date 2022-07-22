package com.example.airsofttechhelper.replica.application;

import com.example.airsofttechhelper.part.domain.ReplicaPart;
import com.example.airsofttechhelper.part.web.RestReplicaPart;
import com.example.airsofttechhelper.replica.application.port.ReplicaUseCase;
import com.example.airsofttechhelper.replica.db.ReplicaJpaRepository;
import com.example.airsofttechhelper.replica.domain.Replica;
import com.example.airsofttechhelper.replica.web.RestReplica;
import com.example.airsofttechhelper.todo.domain.ToDo;
import com.example.airsofttechhelper.todo.web.RestToDo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class ReplicaService implements ReplicaUseCase {

    private final ReplicaJpaRepository repository;

    @Override
    public RestReplica findById(Long id) {
        return repository.findOneByIdEager(id)
                .map(this::toRestReplica)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find replica with id " + id));
    }

    private RestReplica toRestReplica(Replica replica) {
        return new RestReplica(
                replica.getId(),
                replica.getName(),
                replica.getAdditionalEquipment(),
                replica.getOwner().getName(),
                replica.getCreatedAt(),
                replica.getUpdatedAt(),
                toRestReplicaParts(replica.getReplicaParts(), replica.getId()),
                toRestToDo(replica.getToDos())
        );
    }

    private Set<RestToDo> toRestToDo(Set<ToDo> toDos) {
        return toDos.stream()
                .map(toDo -> new RestToDo(toDo.getTitle(), toDo.getContent()))
                .collect(Collectors.toSet());
    }

    private Set<RestReplicaPart> toRestReplicaParts(Set<ReplicaPart> replicaParts, Long replicaId) {
        return replicaParts.stream()
                .map(replicaPart -> new RestReplicaPart(
                        replicaPart.getPart().getName(),
                        replicaPart.getPart().getCategory(),
                        replicaPart.getPart().getId(),
                        replicaId,
                        replicaPart.getNotes(),
                        replicaPart.getCreatedAt()
                )).collect(Collectors.toSet());
    }

    @Override
    public void updateReplica() {

    }

    @Override
    public void deleteReplica(Long id) {

    }
}
