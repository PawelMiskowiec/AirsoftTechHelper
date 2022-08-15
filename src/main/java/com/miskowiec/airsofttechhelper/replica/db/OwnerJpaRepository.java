package com.miskowiec.airsofttechhelper.replica.db;

import com.miskowiec.airsofttechhelper.replica.domain.ReplicaOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerJpaRepository extends JpaRepository<ReplicaOwner, Long> {
    Optional<ReplicaOwner> findByEmail(String email);

}
