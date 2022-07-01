package com.example.airsofttechhelper.replica.web;

import com.example.airsofttechhelper.replica.domain.Owner;
import com.example.airsofttechhelper.replica.domain.ReplicaStatus;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class MenuReplica {
    Long id;
    String name;
    ReplicaStatus status;
    LocalDateTime createdAt;
    String ownerEmail;
}
