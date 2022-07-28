package com.example.airsofttechhelper.replica.domain;

import com.example.airsofttechhelper.jpa.BaseEntity;
import com.example.airsofttechhelper.replica.domain.Replica;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "to_dos")
public class ToDo extends BaseEntity {
    private String title;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    private Replica replica;
}
