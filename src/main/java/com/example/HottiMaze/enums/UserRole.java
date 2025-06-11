package com.example.HottiMaze.enums;
import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN", "관리자"),
    USER("ROLE_USER", "일반사용자");

    private final String authority;
    private final String description;

    UserRole(String authority, String description) {
        this.authority = authority;
        this.description = description;
    }

}