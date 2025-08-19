package com.likelion.hackathon.dto.CafeDto;

import com.likelion.hackathon.entity.enums.CafeReservationStatus;
import lombok.Data;
import java.time.LocalTime;

@Data
public class CafeAbleTimeRequestDto {
    private LocalTime ableStartTime;
    private LocalTime ableEndTime;
    private CafeReservationStatus reservationStatus;  // 추가
}