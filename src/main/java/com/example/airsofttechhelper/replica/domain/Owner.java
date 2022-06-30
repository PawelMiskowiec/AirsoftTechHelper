package com.example.airsofttechhelper.replica.domain;

import com.example.airsofttechhelper.jpa.BaseEntity;
import lombok.*;

import javax.persistence.Entity;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Owner extends BaseEntity {
    private String name;
    private String phone;
    private String street;
    private String city;
    private String zipCode;
    private String email;
}
