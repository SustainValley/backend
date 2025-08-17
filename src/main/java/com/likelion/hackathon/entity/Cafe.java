package com.likelion.hackathon.entity;

import com.likelion.hackathon.entity.enums.SpaceType;
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

    private String MinOrder;

    private LocalTime openTime;

    private LocalTime closeTime;

    @Enumerated(EnumType.STRING)
    private SpaceType spaceType;


}
