package com.example.HottiMaze.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Maze {
    @Id
    private Long id;
    @Column(name = "answer")
    private String answer;
    @Column(name = "MazeDir")
    private String MazeDir;
}
