package com.example.airsofttechhelper.replica.application;

import com.example.airsofttechhelper.replica.db.ReplicaJpaRepository;
import com.example.airsofttechhelper.replica.domain.Replica;
import com.example.airsofttechhelper.replica.application.port.ToDoUseCase;
import com.example.airsofttechhelper.replica.db.ToDoJpaRepository;
import com.example.airsofttechhelper.replica.domain.ToDo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class ToDoService implements ToDoUseCase {
    private final ToDoJpaRepository repository;
    private final ReplicaJpaRepository replicaJpaRepository;

    @Override
    public Optional<ToDo> findOneById(Long id) {
        return repository.findById(id);
    }

    @Override
    public ToDo addToDo(CreateToDoCommand command) {
        Replica replica = replicaJpaRepository.getReferenceById(command.getReplicaId());
        ToDo toDo = new ToDo(command.getTitle(), command.getContent(), replica);
        return repository.save(toDo);
    }

    @Override
    public UpdateToDoResponse updateToDO(UpdateToDoCommand command) {
        ToDo toDo = repository.findById(command.getToDoId())
                .orElseThrow(() -> new IllegalArgumentException("Incorrect ToDo id"));
        toDo.setContent(command.getContent());
        toDo.setTitle(command.getTitle());
        repository.save(toDo);
        return UpdateToDoResponse.SUCCESS;
    }
}
