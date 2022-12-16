package com.kshzzang44.todo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TodoImageEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tii_seq")       private Long seq;
    @Column(name = "tii_ti_seq")    private Long tiSeq;
    @Column(name = "tii_file_name") private String filename;
    @Column(name = "tii_uri")       private String uri;
}
