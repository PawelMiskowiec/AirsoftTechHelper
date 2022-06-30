package com.example.airsofttechhelper.replica.domain;

import com.example.airsofttechhelper.jpa.BaseEntity;

import javax.persistence.Entity;

@Entity
public class ToDo extends BaseEntity {
    private String title;
    private String content;
}
