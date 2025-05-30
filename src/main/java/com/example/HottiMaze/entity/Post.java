package com.example.HottiMaze.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Post {
    @Id
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "content")
    private String content;
}
