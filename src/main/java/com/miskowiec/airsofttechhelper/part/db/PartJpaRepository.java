package com.miskowiec.airsofttechhelper.part.db;

import com.miskowiec.airsofttechhelper.part.domain.Part;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartJpaRepository extends JpaRepository<Part, Long> {
}
