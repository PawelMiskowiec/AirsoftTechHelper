package com.example.airsofttechhelper.replica.db;

import com.example.airsofttechhelper.replica.domain.Replica;
import com.example.airsofttechhelper.replica.domain.ReplicaStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplicaRepository extends JpaRepository<Replica, Long> {
    List<Replica> findByStatusIsContaining(ReplicaStatus status);
}
