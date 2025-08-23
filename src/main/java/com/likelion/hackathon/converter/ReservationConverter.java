package com.likelion.hackathon.converter;


import com.likelion.hackathon.dto.ReservationDto.ReservationDto;
import com.likelion.hackathon.entity.Cafe;
import com.likelion.hackathon.entity.Reservation;
import com.likelion.hackathon.entity.User;
import com.likelion.hackathon.entity.enums.MeetingType;

public class ReservationConverter {


    public static Reservation toEntity(User user, Cafe cafe, ReservationDto.ReservationRequestDto dto, boolean isImmediate) {
        return Reservation.builder()
                .user(user)
                .cafe(cafe)
                .date(dto.getDate())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .peopleCount(dto.getPeopleCount())
                .meetingType(MeetingType.valueOf(dto.getMeetingType()))
                .isImmediate(isImmediate)
                .build();
    }

    public static ReservationDto.ReservationResponseDto toResponseDto(Reservation reservation) {
        User user = reservation.getUser();
        return ReservationDto.ReservationResponseDto.builder()
                .reservationsId(reservation.getId())
                .userId(user.getId())
                .cafeId(reservation.getCafe().getId())
                .meetingType(reservation.getMeetingType().name())
                .date(reservation.getDate())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .peopleCount(reservation.getPeopleCount())
                .ReservationStatus(reservation.getReservationStatus().name())
                .attendanceStatus(reservation.getAttendanceStatus().name())
                .nickname(user.getNickname())
                .phoneNumber(user.getPhoneNumber())
                .cancelReason(reservation.getCancelReason() == null
                        ? null
                        : reservation.getCancelReason().getDescription())
                .isImmediate(reservation.getIsImmediate())
                .build();
    }

}
