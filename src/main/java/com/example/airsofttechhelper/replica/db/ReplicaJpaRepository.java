package com.example.airsofttechhelper.replica.db;

import com.example.airsofttechhelper.replica.domain.Replica;
import com.example.airsofttechhelper.replica.domain.ReplicaStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReplicaJpaRepository extends JpaRepository<Replica, Long> {
    List<Replica> findByStatusIsContaining(ReplicaStatus status);

    @Query(" SELECT r FROM Replica r " +
            " JOIN FETCH r.replicaOwner JOIN FETCH r.tech ")
    List<Replica> findAllFetchOwnerAndTech();

    @Query(" SELECT r FROM Replica r " +
            " LEFT JOIN FETCH r.replicaParts as rp JOIN FETCH rp.part LEFT JOIN FETCH r.toDos JOIN FETCH r.replicaOwner " +
            " WHERE r.id = :id ")
    Optional<Replica> findOneByIdEager(@Param("id") Long id);
}
