package com.likelion.hackathon.dto.CafeDto;

import com.likelion.hackathon.entity.Cafe;
import com.likelion.hackathon.entity.enums.CafeReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CafeAbleTimeRequestDto {
    private LocalTime ableStartTime;
    private LocalTime ableEndTime;
    private CafeReservationStatus reservationStatus;

    public static CafeAbleTimeRequestDto fromEntity(Cafe cafe) {
        return new CafeAbleTimeRequestDto(
                cafe.getAbleStartTime(),
                cafe.getAbleEndTime(),
                cafe.getReservationStatus()
        );
    }
}