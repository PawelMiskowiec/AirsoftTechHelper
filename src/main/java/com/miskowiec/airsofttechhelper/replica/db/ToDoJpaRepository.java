package com.miskowiec.airsofttechhelper.replica.db;

import com.miskowiec.airsofttechhelper.replica.domain.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoJpaRepository extends JpaRepository<ToDo, Long> {
}
