package com.likelion.hackathon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter @Setter
@Entity
public class Cafe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cafe_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "business_info_id")
    private BusinessInfo businessInfo;

    private String name;

    private String location;

    private Long maxSeats;

    private String content;

    private Long seatFee;

    private LocalTime openTime;

    private LocalTime closeTime;

}
