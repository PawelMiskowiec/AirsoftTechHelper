package com.example.airsofttechhelper.todo.application;

import com.example.airsofttechhelper.replica.db.ReplicaJpaRepository;
import com.example.airsofttechhelper.replica.domain.Replica;
import com.example.airsofttechhelper.todo.application.port.ToDoUseCase;
import com.example.airsofttechhelper.todo.db.ToDoJpaRepository;
import com.example.airsofttechhelper.todo.domain.ToDo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class ToDoService implements ToDoUseCase {
    private final ToDoJpaRepository repository;
    private final ReplicaJpaRepository replicaJpaRepository;

    @Override
    public ToDo addToDo(String title, String content, Long replicaId) {
        Replica replica = replicaJpaRepository.getReferenceById(replicaId);
        ToDo toDo = new ToDo(title, content, replica);
        return repository.save(toDo);
    }
}
