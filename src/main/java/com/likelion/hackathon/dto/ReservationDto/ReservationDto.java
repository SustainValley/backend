package com.likelion.hackathon.dto.ReservationDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.likelion.hackathon.entity.enums.CancelReason;
import com.likelion.hackathon.entity.enums.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationDto {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReservationRequestDto {
        private Long userId;
        private Long cafeId;
        private String meetingType;
        private LocalDate date;
        private int peopleCount;

        @JsonFormat(pattern = "HH:mm:ss")
        @Schema(type = "string", example = "15:00:00", description = "예약 시작 시간(HH:mm:ss)")
        private LocalTime startTime;

        @JsonFormat(pattern = "HH:mm:ss")
        @Schema(type = "string", example = "15:00:00", description = "예약 종료 시간(HH:mm:ss)")
        private LocalTime endTime;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReservationResponseDto {
        private Long reservationsId;
        private Long userId;
        private String userName;
        private Long cafeId;
        private String meetingType;
        private LocalDate date;
        private LocalTime startTime;
        private LocalTime endTime;
        private int peopleCount;
        private String ReservationStatus;
        private String attendanceStatus;
        private String cancelReason;

    }

    @Builder
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReservationStatusRequestDto {
        private Long reservationsId;
        private ReservationStatus ReservationStatus;
    }

    @Builder
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CancelReservationRequestDto {
        private CancelReason cancelReason;
    }

}
