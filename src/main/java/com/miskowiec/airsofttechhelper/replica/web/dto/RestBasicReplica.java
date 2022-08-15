package com.miskowiec.airsofttechhelper.replica.web.dto;

import com.miskowiec.airsofttechhelper.replica.domain.ReplicaStatus;
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
