package com.fis.agh.gcp.domain;

import com.google.cloud.Timestamp;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class TodoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String emailAddress;
    private String title;
    private String content;
    private Timestamp date;
    private boolean completed;
}
