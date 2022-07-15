package com.example.airsofttechhelper.part.db;

import com.example.airsofttechhelper.part.domain.Part;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartJpaRepository extends JpaRepository<Part, Long> {
}
