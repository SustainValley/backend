package com.likelion.hackathon.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 로그인용 아이디 or 카카오고유번호 or 사업자번호
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = true)
    private String nickname;

    private String password;
    private String provider; // "local", "kakao", "google"

    // 회원 타입 (일반회원: "pro", 사장님: "cor")
    @Column(nullable = false)
    private String type;

    // ---- 사장님 전용 필드 ----
    @Column(nullable = true)
    private String businessnumber;

    @Column(nullable = true)
    private String presidentname;

    @Column(nullable = true)
    private String businessname;

    @Column(nullable = true)
    private String zipcode;

    @Column(nullable = true)
    private String address;

}





