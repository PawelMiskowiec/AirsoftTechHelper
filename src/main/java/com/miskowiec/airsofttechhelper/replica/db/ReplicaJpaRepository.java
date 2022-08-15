package com.miskowiec.airsofttechhelper.replica.db;

import com.miskowiec.airsofttechhelper.replica.domain.Replica;
import com.miskowiec.airsofttechhelper.replica.domain.ReplicaStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReplicaJpaRepository extends JpaRepository<Replica, Long> {
    @Query(" SELECT r FROM Replica r " +
            " JOIN FETCH r.replicaOwner JOIN FETCH r.tech " +
            " WHERE r.tech.username = :username AND r.status = :status ")
    List<Replica> findAllByStatusAndUsername(ReplicaStatus status, String username);

    @Query(" SELECT r FROM Replica r " +
            " JOIN FETCH r.replicaOwner JOIN FETCH r.tech " +
            " WHERE r.tech.username = :username ")
    List<Replica> findAllByUsernameFetchOwnerAndTech(String username);

    @Query(" SELECT r FROM Replica r " +
            " LEFT JOIN FETCH r.replicaParts as rp LEFT JOIN FETCH rp.part LEFT JOIN FETCH r.toDos JOIN FETCH r.replicaOwner " +
            " WHERE r.id = :id ")
    Optional<Replica> findOneByIdEager(Long id);

    @Query(" SELECT r FROM Replica r " +
            " JOIN FETCH r.replicaOwner JOIN FETCH r.tech t JOIN FETCH t.roles " +
            " WHERE r.id = :id ")
    Optional<Replica> findOneByIdFetchForAuth(Long id);
}
