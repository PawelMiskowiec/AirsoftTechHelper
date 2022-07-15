package com.example.airsofttechhelper.replica.db;

import com.example.airsofttechhelper.replica.domain.ReplicaPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReplicaPartJpaRepository extends JpaRepository<ReplicaPart, Long> {

    List<ReplicaPart> findAllByReplicaId(Long id);

    Optional<ReplicaPart> findFirstByPartId(Long partId);
}
