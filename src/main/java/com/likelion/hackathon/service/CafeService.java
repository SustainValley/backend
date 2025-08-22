package com.likelion.hackathon.service;

import com.likelion.hackathon.apiPayload.code.status.ErrorStatus;
import com.likelion.hackathon.apiPayload.exception.handler.ReservationHandler;
import com.likelion.hackathon.dto.CafeDto.CafeListDto;
import com.likelion.hackathon.dto.CafeDto.CafeNameDto;
import com.likelion.hackathon.dto.CafeDto.CafeUpdateRequestDto;
import com.likelion.hackathon.entity.Cafe;
import com.likelion.hackathon.entity.enums.CafeReservationStatus;
import com.likelion.hackathon.entity.enums.SpaceType;
import com.likelion.hackathon.repository.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CafeService {

    private final CafeRepository cafeRepository;

    // 카페 상세 데이터 수정
    @Transactional
    public Cafe updateCafe(Long cafeId, CafeUpdateRequestDto request) {
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카페입니다."));

        if(request.getMinOrder() != null) cafe.setMinOrder(request.getMinOrder());
        if(request.getMaxCapacity() != null) cafe.setMaxSeats(request.getMaxCapacity().longValue());
        if(request.getSpaceType() != null) cafe.setSpaceType(request.getSpaceType());

        return cafeRepository.save(cafe);
    }

    // 카페 이름 불러오기
    public CafeNameDto getCafeNameById(Long cafeId) {
        return cafeRepository.findById(cafeId)
                .map(cafe -> new CafeNameDto(cafe.getId(), cafe.getName()))
                .orElseThrow(() -> new RuntimeException("해당 카페를 찾을 수 없습니다."));
    }

    // 카페 상세 데이터 불러오기
    @Transactional(readOnly = true)
    public Cafe getCafeById(Long cafeId) {
        return cafeRepository.findById(cafeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카페입니다."));
    }

    // 전체 카페 리스트 반환
    public List<CafeListDto> getCafeList() {
        List<Cafe> cafes = cafeRepository.findByReservationStatus(CafeReservationStatus.AVAILABLE);
        return cafes.stream()
                .map(CafeListDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 검색어로 검색
    public List<Cafe> searchByKeyword(String keyword) {
        return cafeRepository.findByNameContainingIgnoreCase(keyword);
    }

    // 공간 유형 & 최대 인원 필터
    public List<Cafe> filterCafes(SpaceType spaceType, Long maxSeats) {
        if (spaceType != null && maxSeats != null) {
            return cafeRepository.findBySpaceTypeAndMaxSeatsGreaterThanEqual(spaceType, maxSeats);
        } else if (spaceType != null) {
            return cafeRepository.findBySpaceType(spaceType);
        } else if (maxSeats != null) {
            return cafeRepository.findByMaxSeatsGreaterThanEqual(maxSeats);
        } else {
            return cafeRepository.findAll(); // 둘 다 없으면 전체 반환
        }
    }

    public Cafe findCafe(Long cafeId) {
        return cafeRepository.findById(cafeId)
                .orElseThrow(() -> new ReservationHandler(ErrorStatus._CAFE_NOT_FOUND));
    }

}
