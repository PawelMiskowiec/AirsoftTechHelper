package com.example.airsofttechhelper.replica.web.dto;

import com.example.airsofttechhelper.replica.domain.ReplicaStatus;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class RestBasicReplica {
    Long id;
    String name;
    ReplicaStatus status;
    LocalDateTime createdAt;
    String ownerEmail;
}
