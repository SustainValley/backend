package com.likelion.hackathon.entity;

import com.likelion.hackathon.dto.ReservationDto.ReservationDto;
import com.likelion.hackathon.entity.enums.AttendanceStatus;
import com.likelion.hackathon.entity.enums.MeetingType;
import com.likelion.hackathon.entity.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservations_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private int peopleCount;

    @Enumerated(EnumType.STRING)
    private MeetingType meetingType;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus = ReservationStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus attendanceStatus = AttendanceStatus.BEFORE_USE;


    // DB 레벨에서 생성시에도 초기화되도록
    @PrePersist
    public void prePersist() {
        if (this.reservationStatus == null) {
            this.reservationStatus = ReservationStatus.PENDING;
        }
        if (this.attendanceStatus == null) {
            this.attendanceStatus = AttendanceStatus.BEFORE_USE;
        }
    }

    public void updateReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public void updateAttendanceStatus(AttendanceStatus attendanceStatus){
        this.attendanceStatus = attendanceStatus;
    }
}
