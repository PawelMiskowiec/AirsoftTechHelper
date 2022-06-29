package com.example.airsofttechhelper.menu.domain;

import com.example.airsofttechhelper.jpa.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ReplicaStatus status = ReplicaStatus.NEW;

    private String description;

    private String additionalEquipment;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<ToDo> toDos;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
