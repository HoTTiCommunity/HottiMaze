package com.example.HottiMaze.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vote",
        uniqueConstraints = @UniqueConstraint(columnNames = {"post_id", "username"}))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private boolean isLike;
}