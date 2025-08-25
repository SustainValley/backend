package com.likelion.hackathon.redis;

import com.likelion.hackathon.apiPayload.code.status.ErrorStatus;
import com.likelion.hackathon.apiPayload.exception.handler.ReservationHandler;
import com.likelion.hackathon.entity.Reservation;
import com.likelion.hackathon.entity.enums.AttendanceStatus;
import com.likelion.hackathon.entity.enums.CancelReason;
import com.likelion.hackathon.entity.enums.ReservationStatus;
import com.likelion.hackathon.repository.ReservationRepository;
import com.likelion.hackathon.service.ReservationMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReservationExpireListener implements ApplicationListener<RedisKeyExpiredEvent<?>> {

    private final ReservationRepository reservationRepository;
    private final ReservationMessageService reservationMessageService;

    @Override
    @Transactional
    public void onApplicationEvent(RedisKeyExpiredEvent<?> event) {
        // Redis 키
        String expiredKey = new String((byte[]) event.getSource());

        if (!expiredKey.startsWith("reservation:immediate:expire:")) {
            return;
        }

        Long reservationId = Long.valueOf(expiredKey.substring("reservation:immediate:expire:".length()));


        Reservation r = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationHandler(ErrorStatus._RESERVATION_NOT_FOUND));

        // 상태가 확정이면서 사용 전일 때만 처리
        if (r.getReservationStatus() == ReservationStatus.APPROVED
                && r.getAttendanceStatus() == AttendanceStatus.BEFORE_USE
                && Boolean.TRUE.equals(r.getIsImmediate())) {

            
            // 예약 취소 처리
            r.cancelReservation(CancelReason.NO_SHOW);
            reservationRepository.save(r);


            // 메시지 발송
            reservationMessageService.sendReservationExpired(reservationId);
        } else {
            System.out.println("예약 상태가 처리 조건에 맞지 않음 - 예약ID: " + reservationId);
        }
    }
}

