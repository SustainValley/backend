package com.likelion.hackathon.redis;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReservationEventMessage {
    private Long reservationId;
    private String reservationStatus;
    private String cancelReason;
}
