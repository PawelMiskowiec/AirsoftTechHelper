package com.example.airsofttechhelper.menu.application;

import com.example.airsofttechhelper.menu.application.port.ReplicaUseCase;
import com.example.airsofttechhelper.menu.db.ReplicaRepository;
import com.example.airsofttechhelper.menu.domain.Replica;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReplicaService implements ReplicaUseCase {

    private final ReplicaRepository repository;

    @Override
    public List<Replica> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Replica> findOneById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Replica addReplica() {
        return null;
    }

    @Override
    public void deleteReplica(Long id) {
        repository.deleteById(id);
    }
}
