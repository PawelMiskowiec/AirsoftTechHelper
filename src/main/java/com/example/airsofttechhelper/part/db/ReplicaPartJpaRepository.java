package com.example.airsofttechhelper.part.db;

import com.example.airsofttechhelper.part.domain.ReplicaPart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReplicaPartJpaRepository extends JpaRepository<ReplicaPart, Long> {

    List<ReplicaPart> findAllByReplicaId(Long id);

    Optional<ReplicaPart> findFirstByPartId(Long partId);
}
