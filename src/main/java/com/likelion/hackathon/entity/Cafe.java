package com.likelion.hackathon.entity;

import com.likelion.hackathon.entity.enums.CafeReservationStatus;
import com.likelion.hackathon.entity.enums.SpaceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToOne(mappedBy = "cafe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CafeOperatingHours operatingHours;

    private String name;

    @OneToMany(mappedBy = "cafe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CafeImage> images = new ArrayList<>();

    private String location;

    private Long maxSeats;

    private String MinOrder;

    @Enumerated(EnumType.STRING)
    private SpaceType spaceType;

    @Column(columnDefinition = "TEXT")
    private String customerPromotion;

    private LocalTime ableStartTime;
    private LocalTime ableEndTime;

    @Enumerated(EnumType.STRING)
    private CafeReservationStatus reservationStatus = CafeReservationStatus.AVAILABLE;

}
