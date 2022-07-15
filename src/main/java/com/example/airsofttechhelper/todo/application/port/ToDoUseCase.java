package com.example.airsofttechhelper.todo.application.port;

import com.example.airsofttechhelper.todo.domain.ToDo;

public interface ToDoUseCase {
    ToDo addToDo(String title, String content, Long replicaId);
}
