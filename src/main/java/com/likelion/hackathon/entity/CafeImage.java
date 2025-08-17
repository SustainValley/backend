package com.likelion.hackathon.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
public class CafeImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;
}
