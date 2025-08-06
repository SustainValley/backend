package com.likelion.hackathon.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;

    @Column(nullable = true)
    private String nickname;

    private String password;
    private String provider; // "local", "google", "kakao"

    private String email;
    // private String role;     -- "ROLE_USER"
}





