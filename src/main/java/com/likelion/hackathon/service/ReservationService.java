package com.likelion.hackathon.service;

import com.likelion.hackathon.apiPayload.code.status.ErrorStatus;
import com.likelion.hackathon.apiPayload.exception.handler.ReservationHandler;
import com.likelion.hackathon.converter.ReservationConverter;
import com.likelion.hackathon.dto.ReservationDto.ReservationDto;
import com.likelion.hackathon.entity.Cafe;
import com.likelion.hackathon.entity.Reservation;
import com.likelion.hackathon.entity.UserType;
import com.likelion.hackathon.entity.enums.AttendanceStatus;
import com.likelion.hackathon.entity.enums.ReservationStatus;
import com.likelion.hackathon.repository.CafeRepository;
import com.likelion.hackathon.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final CafeRepository cafeRepository;

    // 예약 생성
    public ReservationDto.ReservationResponseDto createReservation(ReservationDto.ReservationRequestDto dto) {
        Cafe cafe = cafeRepository.findById(dto.getCafeId())
                .orElseThrow(() -> new ReservationHandler(ErrorStatus._CAFE_NOT_FOUND));

        Reservation reservation = ReservationConverter.toEntity(userService.existUser(dto.getUserId()), cafe, dto);
        reservationRepository.save(reservation);

        return ReservationConverter.toResponseDto(reservation);
    }

    public List<ReservationDto.ReservationResponseDto> getAllReservationWithUserCheck(Long userId) {
        userService.existUser(userId);
        return getAllReservation(userId);
    }

    // 예약 상세페이지
    public ReservationDto.ReservationResponseDto getOneReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationHandler(ErrorStatus._RESERVATION_NOT_FOUND));
        return ReservationConverter.toResponseDto(reservation);
    }

    // 예약 삭제
    @Transactional
    public void deleteReservation(Long userId, Long reservationId, ReservationDto.CancelReservationRequestDto dto) {
        Reservation reservation = reservationRepository.findByIdAndUserId(reservationId, userId)
                .orElseThrow(() -> new ReservationHandler(ErrorStatus._RESERVATION_NOT_FOUND));
        reservation.cancelReservation(dto.getCancelReason());
//        reservation.updateReservationStatus(ReservationStatus.REJECTED);
        System.out.println("deleteReservation: " + reservation.getCancelReason());
    }


    // 사장님이 받은 전체 예약 조회
    public List<ReservationDto.ReservationResponseDto> getReservationsForOwner(Long ownerId) {
        userService.existUser(ownerId);
        List<Reservation> reservations = reservationRepository.findAllByOwnerId(ownerId);
        return reservations.stream()
                .map(ReservationConverter::toResponseDto)
                .toList();
    }

    // 카페 이용 정보 변경
    @Transactional
    public ReservationDto.ReservationResponseDto updateAttendanceStatus(Long reservationId, String attendance) {
        Reservation reservation = getReservation(reservationId);
        reservation.updateAttendanceStatus(AttendanceStatus.valueOf(attendance));

        return ReservationConverter.toResponseDto(reservation);
    }


    // 예약 상태 변경
    @Transactional
    public ReservationDto.ReservationResponseDto updateReservationStatus(ReservationDto.ReservationStatusRequestDto dto) {
        Reservation reservation = getReservation(dto.getReservationsId());
        System.out.println("updateReservationStatus getReservationStatus: "+ dto.getReservationStatus());

        reservation.updateReservationStatus(dto.getReservationStatus());
        return ReservationConverter.toResponseDto(reservation);
    }

    // 오늘 예약 조회
    public List<ReservationDto.ReservationResponseDto> getAllTodayReservation(Long userId) {
        LocalDate today = LocalDate.now();

        List<Reservation> reservations = reservationRepository
                .findReservationsForOwnerByDate(userId, UserType.COR, today);

        return reservations.stream()
                .map(ReservationConverter::toResponseDto)
                .toList();
    }

    // 모든 예약 조회
    public List<ReservationDto.ReservationResponseDto> getAllReservation(Long userId) {

        List<Reservation> reservationList = reservationRepository.findByUserId(userId);

        return reservationList.stream()
                .map(ReservationConverter::toResponseDto)
                .toList();
    }

    // 예약 존재 여부 조회
    public Reservation getReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationHandler(ErrorStatus._RESERVATION_NOT_FOUND));
    }

}
