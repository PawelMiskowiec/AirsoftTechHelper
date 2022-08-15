package com.miskowiec.airsofttechhelper.replica.domain;

import com.miskowiec.airsofttechhelper.jpa.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "owners")
public class ReplicaOwner extends BaseEntity {
    private String name;
    private String phone;
    private String street;
    private String city;
    private String zipCode;
    @Column(unique = true)
    private String email;
}
