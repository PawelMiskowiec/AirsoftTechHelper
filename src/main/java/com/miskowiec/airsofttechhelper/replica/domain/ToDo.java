package com.miskowiec.airsofttechhelper.replica.domain;

import com.miskowiec.airsofttechhelper.jpa.BaseEntity;
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
    @ManyToOne(fetch = FetchType.EAGER)
    private Replica replica;
}
