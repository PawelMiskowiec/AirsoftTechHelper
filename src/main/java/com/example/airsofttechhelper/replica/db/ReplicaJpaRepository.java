package com.example.airsofttechhelper.replica.db;

import com.example.airsofttechhelper.replica.domain.Replica;
import com.example.airsofttechhelper.replica.domain.ReplicaStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReplicaJpaRepository extends JpaRepository<Replica, Long> {
    @Query(" SELECT r FROM Replica r " +
            " JOIN FETCH r.replicaOwner JOIN FETCH r.tech " +
            " WHERE r.tech.username = :username AND r.status = :status ")
    List<Replica> findAllByStatusAndUsername(String status, String username);

    @Query(" SELECT r FROM Replica r " +
            " JOIN FETCH r.replicaOwner JOIN FETCH r.tech ")
    List<Replica> findAllByUsernameFetchOwnerAndTech(String username);

    @Query(" SELECT r FROM Replica r " +
            " LEFT JOIN FETCH r.replicaParts as rp LEFT JOIN FETCH rp.part LEFT JOIN FETCH r.toDos JOIN FETCH r.replicaOwner " +
            " WHERE r.id = :id ")
    Optional<Replica> findOneByIdEager(Long id);
}
