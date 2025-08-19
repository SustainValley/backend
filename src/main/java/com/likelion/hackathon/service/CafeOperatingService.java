package com.likelion.hackathon.service;

import com.likelion.hackathon.dto.CafeDto.CafeAbleTimeRequestDto;
import com.likelion.hackathon.dto.CafeDto.CafeOperatingDto;
import com.likelion.hackathon.entity.Cafe;
import com.likelion.hackathon.entity.CafeOperatingHours;
import com.likelion.hackathon.repository.CafeOperatingHoursRepository;
import com.likelion.hackathon.repository.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CafeOperatingService {

    private final CafeRepository cafeRepository;
    private final CafeOperatingHoursRepository cafeOperatingHoursRepository;

    @Transactional(readOnly = true)
    public CafeOperatingHours getOperatingHours(Long cafeId) {
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카페입니다."));

        return cafeOperatingHoursRepository.findByCafe(cafe)
                .orElseThrow(() -> new IllegalArgumentException("운영시간 정보가 없습니다."));
    }

    @Transactional
    public CafeOperatingHours updateOperatingHours(Long cafeId, CafeOperatingDto request) {
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카페입니다."));

        // 기존 운영시간 조회, 없으면 새로 생성
        CafeOperatingHours hours = cafeOperatingHoursRepository.findByCafe(cafe)
                .orElseGet(() -> {
                    CafeOperatingHours newHours = new CafeOperatingHours();
                    newHours.setCafe(cafe);
                    return newHours;
                });

        hours.setMonOpen(request.getMonOpen());
        hours.setMonClose(request.getMonClose());
        hours.setMonIsOpen(request.getMonIsOpen());

        hours.setTueOpen(request.getTueOpen());
        hours.setTueClose(request.getTueClose());
        hours.setTueIsOpen(request.getTueIsOpen());

        hours.setWedOpen(request.getWedOpen());
        hours.setWedClose(request.getWedClose());
        hours.setWedIsOpen(request.getWedIsOpen());

        hours.setThuOpen(request.getThuOpen());
        hours.setThuClose(request.getThuClose());
        hours.setThuIsOpen(request.getThuIsOpen());

        hours.setFriOpen(request.getFriOpen());
        hours.setFriClose(request.getFriClose());
        hours.setFriIsOpen(request.getFriIsOpen());

        hours.setSatOpen(request.getSatOpen());
        hours.setSatClose(request.getSatClose());
        hours.setSatIsOpen(request.getSatIsOpen());

        hours.setSunOpen(request.getSunOpen());
        hours.setSunClose(request.getSunClose());
        hours.setSunIsOpen(request.getSunIsOpen());

        return cafeOperatingHoursRepository.save(hours);
    }

    // 카페 예약 가능 시간 설정
    @Transactional
    public void updateAbleTime(Long cafeId, CafeAbleTimeRequestDto dto) {
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 카페입니다."));

        cafe.setAbleStartTime(dto.getAbleStartTime());
        cafe.setAbleEndTime(dto.getAbleEndTime());

        if (dto.getReservationStatus() != null) {
            cafe.setReservationStatus(dto.getReservationStatus());
        }

        cafeRepository.save(cafe);
    }

    // 카페 예약 가능 시간 반환
    public CafeAbleTimeRequestDto getReservationTime(Long cafeId) {
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카페입니다."));
        return CafeAbleTimeRequestDto.fromEntity(cafe);
    }

}
