package com.example.airsofttechhelper.replica.domain;

import com.example.airsofttechhelper.jpa.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ToDo extends BaseEntity {
    private String title;
    private String content;
}
