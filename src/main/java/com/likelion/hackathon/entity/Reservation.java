package com.likelion.hackathon.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.likelion.hackathon.dto.ReservationDto.ReservationDto;
import com.likelion.hackathon.entity.enums.AttendanceStatus;
import com.likelion.hackathon.entity.enums.CancelReason;
import com.likelion.hackathon.entity.enums.MeetingType;
import com.likelion.hackathon.entity.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @JsonFormat(pattern = "HH:mm:ss", timezone = "Asia/Seoul")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm:ss", timezone = "Asia/Seoul")
    private LocalTime endTime;

    private LocalDateTime testTime;

    private int peopleCount;

    @Enumerated(EnumType.STRING)
    private MeetingType meetingType;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus = ReservationStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus attendanceStatus = AttendanceStatus.BEFORE_USE;

    @Enumerated(EnumType.STRING)
    private CancelReason cancelReason;

    @Column(name = "is_immediate", nullable = false)
    private Boolean isImmediate = false;

    private LocalDateTime reservationApprovedTime;


    // DB 레벨에서 생성시에도 초기화되도록
    @PrePersist
    public void prePersist() {
        if (this.reservationStatus == null) {
            this.reservationStatus = ReservationStatus.PENDING;
        }
        if (this.attendanceStatus == null) {
            this.attendanceStatus = AttendanceStatus.BEFORE_USE;
        }
        if (this.isImmediate == null) {
            this.isImmediate = false;
        }
    }

    public void updateReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public void updateAttendanceStatus(AttendanceStatus attendanceStatus){
        this.attendanceStatus = attendanceStatus;
    }

    public void cancelReservation(CancelReason cancelReason) {
        this.cancelReason = cancelReason;
        updateReservationStatus(ReservationStatus.REJECTED);
    }

    public void updateReservationApprovedTime(){
        this.reservationApprovedTime = LocalDateTime.now();
    }
}
