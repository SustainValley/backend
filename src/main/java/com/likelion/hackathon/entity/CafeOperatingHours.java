package com.likelion.hackathon.entity;

import jakarta.persistence.*;

import java.time.LocalTime;

public class CafeOperatingHours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    // 월요일
    private LocalTime monOpen;
    private LocalTime monClose;
    private Boolean monIsOpen;

    // 화요일
    private LocalTime tueOpen;
    private LocalTime tueClose;
    private Boolean tueIsOpen;

    // 수요일
    private LocalTime wedOpen;
    private LocalTime wedClose;
    private Boolean wedIsOpen;

    // 목요일
    private LocalTime thuOpen;
    private LocalTime thuClose;
    private Boolean thuIsOpen;

    // 금요일
    private LocalTime friOpen;
    private LocalTime friClose;
    private Boolean friIsOpen;

    // 토요일
    private LocalTime satOpen;
    private LocalTime satClose;
    private Boolean satIsOpen;

    // 일요일
    private LocalTime sunOpen;
    private LocalTime sunClose;
    private Boolean sunIsOpen;
}
