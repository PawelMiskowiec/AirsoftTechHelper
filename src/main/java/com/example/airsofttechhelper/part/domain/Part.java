package com.example.airsofttechhelper.part.domain;

import com.example.airsofttechhelper.jpa.BaseEntity;
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
@EntityListeners(AuditingEntityListener.class)
public class Part extends BaseEntity {

    private String name;

    @Enumerated(EnumType.STRING)
    private PartCategory category;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "part_id")
    private Set<SinglePart> singleParts = new HashSet<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
