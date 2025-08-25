package com.likelion.hackathon.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import com.likelion.hackathon.entity.enums.DayOfWeek;

@Setter
@Getter
@Entity
public class CafeUnavailableBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    private LocalTime startTime;
    private LocalTime endTime;
}
