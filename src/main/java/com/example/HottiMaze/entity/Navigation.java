package com.example.HottiMaze.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import org.springframework.beans.factory.parsing.Problem;

import java.time.LocalDateTime;

@Entity
public class Navigation {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Problem problem;

    @ManyToOne(optional = false)
    private Maze maze;

    private boolean correct;

    private LocalDateTime timestamp;
}
