package com.example.airsofttechhelper.replica.db;

import com.example.airsofttechhelper.replica.domain.ReplicaOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerJpaRepository extends JpaRepository<ReplicaOwner, Long> {
    Optional<ReplicaOwner> findByEmail(String email);

}
