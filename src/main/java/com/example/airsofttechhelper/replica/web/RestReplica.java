package com.example.airsofttechhelper.replica.web;

import com.example.airsofttechhelper.part.web.RestReplicaPart;
import com.example.airsofttechhelper.todo.web.RestToDo;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Set;

@Value
public class RestReplica {
    Long id;
    String name;
    String additionalEquipment;
    String ownerName;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Set<RestReplicaPart> parts;
    Set<RestToDo> toDos;
}
