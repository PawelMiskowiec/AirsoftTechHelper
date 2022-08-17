package com.miskowiec.airsofttechhelper.part.domain;

import com.miskowiec.airsofttechhelper.jpa.BaseEntity;
import com.miskowiec.airsofttechhelper.replica.domain.Replica;
import com.miskowiec.airsofttechhelper.user.domain.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "replica_parts")
public class ReplicaPart extends BaseEntity {
    private String notes;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private Part part;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private Replica replica;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tech_id")
    private UserEntity user;

    public ReplicaPart(String notes, Part part, Replica replica, UserEntity user) {
        this.notes = notes;
        this.part = part;
        this.replica = replica;
        this.user = user;
    }

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
