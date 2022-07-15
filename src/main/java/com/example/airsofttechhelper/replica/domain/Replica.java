package com.example.airsofttechhelper.replica.domain;

import com.example.airsofttechhelper.jpa.BaseEntity;
import com.example.airsofttechhelper.part.domain.ReplicaPart;
import com.example.airsofttechhelper.todo.domain.ToDo;
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
@Table(name = "replica")
@EntityListeners(AuditingEntityListener.class)
public class Replica extends BaseEntity {

    private String name;

    private String description;

    private String additionalEquipment;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ReplicaStatus status = ReplicaStatus.NEW;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST},
            fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "replica", orphanRemoval = true)
    private Set<ReplicaPart> replicaParts = new HashSet<>(); //ToDo - Test how hashSets are working with new HashCode impl

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "replica", orphanRemoval = true)
    private Set<ToDo> toDos = new HashSet<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Replica(String name, String description, String additionalEquipment, Owner owner) {
        this.name = name;
        this.description = description;
        this.additionalEquipment = additionalEquipment;
        this.owner = owner;
    }

    public void addReplicaPart(ReplicaPart replicaPart){
        replicaParts.add(replicaPart);
    }

    public void updateStatus(ReplicaStatus newStatus){
        this.status = status.changeStatus(newStatus);
    }
}
