package com.miskowiec.airsofttechhelper.replica.application;

import com.miskowiec.airsofttechhelper.replica.db.ReplicaJpaRepository;
import com.miskowiec.airsofttechhelper.replica.domain.Replica;
import com.miskowiec.airsofttechhelper.replica.application.port.ToDoUseCase;
import com.miskowiec.airsofttechhelper.replica.db.ToDoJpaRepository;
import com.miskowiec.airsofttechhelper.replica.domain.ToDo;
import com.miskowiec.airsofttechhelper.security.UnauthorisedAccessException;
import com.miskowiec.airsofttechhelper.security.UserSecurity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class ToDoService implements ToDoUseCase {
    private final ToDoJpaRepository repository;
    private final ReplicaJpaRepository replicaJpaRepository;
    private final UserSecurity userSecurity;

    @Override
    public Optional<ToDo> findOneById(Long id) {
        return repository.findById(id);
    }

    @Override
    public ToDo addToDo(CreateToDoCommand command) {
        Replica replica = replicaJpaRepository
                .findOneByIdFetchForAuth(command.getReplicaId())
                .map(r -> {
                    if(!userSecurity.isOwnerOrAdmin(r.getTech().getUsername(), command.getUser())){
                        throw new UnauthorisedAccessException(command.getUser().getUsername() + " is not authorised to add todos to Replica with id " + r.getId());
                    }
                    return r;
                }).orElseThrow(() -> new IllegalArgumentException("Replica not found"));
        ToDo toDo = new ToDo(command.getTitle(), command.getContent(), replica);
        return repository.save(toDo);
    }

    @Override
    public UpdateToDoResponse updateToDO(UpdateToDoCommand command) {
        return repository
                .findById(command.getToDoId())
                .map(toDo -> {
                    if(userSecurity.isOwnerOrAdmin(toDo.getReplica().getTech().getUsername(), command.getUser())){
                        toDo.setContent(command.getContent());
                        toDo.setTitle(command.getTitle());
                        return UpdateToDoResponse.SUCCESS;
                    }
                    return new UpdateToDoResponse(false, List.of("Forbidden"), ErrorStatus.FORBIDDEN);
                })
                .orElse(new UpdateToDoResponse(false, List.of("ToDo not found"), ErrorStatus.NOTFOUND));
    }
}
