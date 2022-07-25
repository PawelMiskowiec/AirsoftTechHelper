package com.example.airsofttechhelper.replica.db;

import com.example.airsofttechhelper.replica.domain.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoJpaRepository extends JpaRepository<ToDo, Long> {
}
