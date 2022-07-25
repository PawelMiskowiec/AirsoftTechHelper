package com.example.airsofttechhelper.replica.web.dto;

import com.example.airsofttechhelper.part.web.RestReplicaPart;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Set;

@Value
public class RestDetailedReplica {
    Long id;
    String name;
    String additionalEquipment;
    String ownerName;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Set<RestReplicaPart> parts;
    Set<RestToDo> toDos;
}
