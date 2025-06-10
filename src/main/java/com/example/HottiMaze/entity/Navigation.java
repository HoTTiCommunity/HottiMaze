package com.example.HottiMaze.entity;

import jakarta.persistence.*;
import org.springframework.beans.factory.parsing.Problem;

import java.time.LocalDateTime;

@Entity
public class Navigation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @JoinColumn(name = "mazeQuestion_id")
    @ManyToOne
    private MazeQuestion mazeQuestion;

    @JoinColumn(name = "maze_id")
    @ManyToOne
    private Maze maze;

    private boolean correct;

    private LocalDateTime timestamp;
}
