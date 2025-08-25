package com.likelion.hackathon.service;

import com.likelion.hackathon.apiPayload.code.status.ErrorStatus;
import com.likelion.hackathon.apiPayload.exception.handler.ReservationHandler;
import com.likelion.hackathon.entity.enums.CancelReason;
import com.likelion.hackathon.entity.enums.ReservationStatus;
import com.likelion.hackathon.redis.ReservationEventMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationMessageService {
    
    private final SimpMessageSendingOperations messagingTemplate;
    
    //예약 취소 메시지 전송
    public void sendReservationCanceled(Long reservationId, ReservationStatus reservationStatus, CancelReason cancelReason) {
        sendMessage(reservationId, reservationStatus.name(), cancelReason.name());
    }
    
    // 예약 만료 메시지 전송
    public void sendReservationExpired(Long reservationId) {
        sendMessage(reservationId, "CANCELED", "NO_SHOW");
    }


    // 메시지 전송 메서드
    private void sendMessage(Long reservationId, String status, String reason) {
        try {
            log.info("예약 메시지 전송 시작 - 예약ID: {}, 상태: {}, 사유: {}", reservationId, status, reason);
            
            String destination = "/sub/reservations/" + reservationId;
            ReservationEventMessage message = new ReservationEventMessage(reservationId, status, reason);
            
            log.info("메시지 발송 대상: {}, 메시지 내용: {}", destination, message);
            
            messagingTemplate.convertAndSend(destination, message);
            
            log.info("예약 메시지 전송 완료 - 예약ID: {}", reservationId);
        } catch (Exception e) {
            throw new ReservationHandler(ErrorStatus._WEBSOCKET_SERVER_ERROR);
        }
    }
}

