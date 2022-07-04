package com.example.airsofttechhelper.replica.db;

import com.example.airsofttechhelper.replica.domain.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<Owner> findByEmailIgnoreCase(String email);

}
