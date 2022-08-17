package com.miskowiec.airsofttechhelper.replica.domain;

import com.miskowiec.airsofttechhelper.jpa.BaseEntity;
import com.miskowiec.airsofttechhelper.part.domain.ReplicaPart;
import com.miskowiec.airsofttechhelper.user.domain.UserEntity;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "replicas")
@EntityListeners(AuditingEntityListener.class)
public class Replica extends BaseEntity {

    private String name;

    private String description;

    private String additionalEquipment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tech_id")
    private UserEntity user;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ReplicaStatus status = ReplicaStatus.NEW;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "owner_id")
    private ReplicaOwner replicaOwner;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "replica", orphanRemoval = true)
    private Set<ReplicaPart> replicaParts = new HashSet<>(); //ToDo - Test how hashSets are working with new HashCode impl

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "replica", orphanRemoval = true)
    private Set<ToDo> toDos = new HashSet<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Replica(String name, String description, String additionalEquipment, ReplicaOwner replicaOwner) {
        this.name = name;
        this.description = description;
        this.additionalEquipment = additionalEquipment;
        this.replicaOwner = replicaOwner;
    }

    public void updateStatus(ReplicaStatus newStatus) {
        this.status = status.changeStatus(newStatus);
    }
}
