package com.example.airsofttechhelper.part.domain;

import com.example.airsofttechhelper.jpa.BaseEntity;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity

@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "parts")
public class Part extends BaseEntity {

    private String name;

    @Enumerated(EnumType.STRING)
    private PartCategory category;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "part")
    private Set<ReplicaPart> replicaParts =  new HashSet<>();

    public Part(String name, PartCategory category) {
        this.name = name;
        this.category = category;
    }
}
