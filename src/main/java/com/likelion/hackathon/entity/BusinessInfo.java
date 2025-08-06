package com.likelion.hackathon.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
public class BusinessInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String businessnumber;  // 사업자번호

    @Column(nullable = false)
    private String presidentname;

    @Column(nullable = false)
    private String businessname;

    @Column(nullable = true)
    private String zipcode;

    @Column(nullable = true)
    private String address;

    // User와 1:1 매핑
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
