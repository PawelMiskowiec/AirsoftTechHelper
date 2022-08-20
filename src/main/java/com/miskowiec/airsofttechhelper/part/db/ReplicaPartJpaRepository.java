package com.miskowiec.airsofttechhelper.part.db;

import com.miskowiec.airsofttechhelper.part.domain.ReplicaPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReplicaPartJpaRepository extends JpaRepository<ReplicaPart, Long> {

    @Query(" SELECT rp FROM ReplicaPart rp " +
            " JOIN rp.user u " +
            " WHERE u.username = :username ")
    List<ReplicaPart> findAllByUsername(String username);

    @Query(" SELECT rp FROM ReplicaPart rp " +
            " JOIN rp.user u JOIN FETCH rp.replica r " +
            " WHERE u.username = :username AND r.id = :id ")
    List<ReplicaPart> findAllByReplicaIdAndUsername(Long id, String username);

    Optional<ReplicaPart> findFirstByPartId(Long partId);
}
