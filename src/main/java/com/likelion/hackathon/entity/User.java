package com.likelion.hackathon.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // 로그인용 아이디 or 카카오고유번호 or 사업자번호

    @Column(nullable = true)
    private String nickname;

    private String password;

    private String provider; // "local", "kakao", "google"

    private String phoneNumber;

    @Column(nullable = false)
    private String type; // 일반회원: "per", 사장님: "cor"

    // 사장님 전용 정보 (1:1 관계)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private BusinessInfo businessInfo;

}





