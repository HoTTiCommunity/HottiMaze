package com.example.HottiMaze.entity;

import com.example.HottiMaze.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "point")
    private int point = 0;

    @Column(name = "chulcheck", nullable = false)
    private int chulcheck = 0;

    @Column(name = "is_available_chulcheck")
    private Integer isAvailableChulcheck = 1;

    // 권한 필드 추가
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role = UserRole.USER;
}
