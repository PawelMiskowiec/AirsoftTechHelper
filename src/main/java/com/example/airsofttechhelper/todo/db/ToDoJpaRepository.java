package com.example.airsofttechhelper.todo.db;

import com.example.airsofttechhelper.todo.domain.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoJpaRepository extends JpaRepository<ToDo, Long> {
}
