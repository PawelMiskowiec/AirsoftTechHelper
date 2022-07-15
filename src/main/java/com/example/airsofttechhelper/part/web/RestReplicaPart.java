package com.example.airsofttechhelper.part.web;

import com.example.airsofttechhelper.part.domain.PartCategory;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class RestReplicaPart {
    String name;
    PartCategory category;
    Long partId;
    Long replicaId;
    String notes;
    LocalDateTime createdAt;
}
