package com.likelion.hackathon.controller;

import com.likelion.hackathon.apiPayload.ApiResponse;
import com.likelion.hackathon.dto.ReservationDto.ReservationDto;
import com.likelion.hackathon.service.ReservationService;
import com.likelion.hackathon.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController{
    private final ReservationService reservationService;

    @Operation(summary = "모든 예약 조회", description = "일반 사용자의 모든 예약 데이터를 조회합니다.")
    @GetMapping
    public ApiResponse<List<ReservationDto.ReservationResponseDto>> getAllReservation(@RequestParam("userId") Long id) {
        List<ReservationDto.ReservationResponseDto> list = reservationService.getAllReservationWithUserCheck(id);
        return ApiResponse.onSuccess(list);
    }

    @Operation(summary = "예약 생성", description = "새로운 예약을 생성합니다.")
    @PostMapping("/create")
    public ApiResponse<ReservationDto.ReservationResponseDto> createReservation(@RequestBody ReservationDto.ReservationRequestDto dto) {
        System.out.println("createReservation Controller");
        ReservationDto.ReservationResponseDto reservationResponseDto = reservationService.createReservation(dto);
        return ApiResponse.onSuccess(reservationResponseDto);
    }

    @Operation(summary = "예약 삭제", description = "해당 예약을 삭제합니다.")
    @DeleteMapping("/delete/{reservationId}")
    public ApiResponse<String> deleteReservation(@RequestParam("userId") Long userId,
                                                                                @PathVariable("reservationId") Long reservationId) {
        reservationService.deleteReservation(userId, reservationId);
        return ApiResponse.onSuccess("success");
    }

    @Operation(summary = "받은 예약 전체 조회", description = "사장님이 받은 모든 예약을 조회합니다.")
    @GetMapping("/owner")
    public ApiResponse<List<ReservationDto.ReservationResponseDto>> getAllReservationForOwner(@RequestParam("userId") Long userId) {
        List<ReservationDto.ReservationResponseDto> list = reservationService.getReservationsForOwner(userId);
        return ApiResponse.onSuccess(list);
    }

    @Operation(summary = "예약 상태 변경", description = "예약을 수락하거나 거절하는 등 예약 상태를 변경합니다.")
    @PatchMapping("/owner/update")
    public ApiResponse<ReservationDto.ReservationResponseDto> updateReservationStatus(@RequestBody ReservationDto.ReservationStatusRequestDto dto) {
        ReservationDto.ReservationResponseDto reservationResponseDto = reservationService.updateReservationStatus(dto);
        return ApiResponse.onSuccess(reservationResponseDto);
    }

    @Operation(summary = "오늘 방문 예정인 예약 전체 조회", description = "사장님이 받은 모든 예약 중 오늘이 방문예정일인 예약을 조회합니다.")
    @GetMapping("/owner/today")
    public ApiResponse<List<ReservationDto.ReservationResponseDto>> getAllTodayReservationForOwner(@RequestParam("userId") Long userId) {
        List<ReservationDto.ReservationResponseDto> list = reservationService.getAllTodayReservation(userId);
        return ApiResponse.onSuccess(list);
    }

    @Operation(summary = "카페 이용 여부 변경", description = "예약한 사용자가 카페 이용을 시작했는지, 끝났는지에 대한 정보를 변경합니다.")
    @PatchMapping("/owner/today/{reservationId}")
    public ApiResponse<ReservationDto.ReservationResponseDto> updateAttendanceStatus(@PathVariable("reservationId") Long reservationId,
                                                                                     @RequestParam("attendance") String attendance) {
        ReservationDto.ReservationResponseDto reservationResponseDto = reservationService.updateAttendanceStatus(reservationId, attendance);
        return ApiResponse.onSuccess(reservationResponseDto);
    }

}
